/** A Java 1.5 grammar for ANTLR v3 derived from the spec
 *
 *  This is a very close representation of the spec; the changes
 *  are comestic (remove left recursion) and also fixes (the spec
 *  isn't exactly perfect).  I have run this on the 1.4.2 source
 *  and some nasty looking enums from 1.5, but have not really
 *  tested for 1.5 compatibility.
 *
 *  I built this with: java -Xmx100M org.antlr.Tool java.g 
 *  and got two errors that are ok (for now):
 *  java.g:691:9: Decision can match input such as
 *    "'0'..'9'{'E', 'e'}{'+', '-'}'0'..'9'{'D', 'F', 'd', 'f'}"
 *    using multiple alternatives: 3, 4
 *  As a result, alternative(s) 4 were disabled for that input
 *  java.g:734:35: Decision can match input such as "{'$', 'A'..'Z',
 *    '_', 'a'..'z', '\u00C0'..'\u00D6', '\u00D8'..'\u00F6',
 *    '\u00F8'..'\u1FFF', '\u3040'..'\u318F', '\u3300'..'\u337F',
 *    '\u3400'..'\u3D2D', '\u4E00'..'\u9FFF', '\uF900'..'\uFAFF'}"
 *    using multiple alternatives: 1, 2
 *  As a result, alternative(s) 2 were disabled for that input
 *
 *  You can turn enum on/off as a keyword :)
 *
 *  Version 1.0 -- initial release July 5, 2006 (requires 3.0b2 or higher)
 *
 *  Primary author: Terence Parr, July 2006
 *
 *  Version 1.0.1 -- corrections by Koen Vanderkimpen & Marko van Dooren,
 *      October 25, 2006;
 *      fixed normalInterfaceDeclaration: now uses typeParameters instead
 *          of typeParameter (according to JLS, 3rd edition)
 *      fixed castExpression: no longer allows expression next to type
 *          (according to semantics in JLS, in contrast with syntax in JLS)
 */
grammar Gomantlr_Java;
options {k=2; backtrack=true; memoize=true; }

@parser::header {
    import gomantlr_java.types.*;
}

@parser::members {
    %include { gomantlr_java/Gomantlr_Java.tom }
}

@lexer::members {
protected boolean enumIsKeyword = false;
}

// starting point for parsing a java file
compilationUnit returns [Java_compilationUnit cu]
@init {
    Java_annotationList cu1=`Java_annotationList();
    Java_package cu2=`Java_noPackage();
    Java_importDeclarationList cu3=`Java_importDeclarationList();
    Java_classOrInterfaceDeclarationList cu4=`Java_concClassOrInterfaceDeclaration();
}
	:	(
            a=annotations
            {
                cu1=a;
            }
        )?
		(
            pd=packageDeclaration
            {
                cu2=pd;
            }
        )?
        (
            id=importDeclaration
            {
                cu3=`Java_importDeclarationList(cu3*,id);
            }
        )*
        (
            td=typeDeclaration
            {
                cu4=`Java_concClassOrInterfaceDeclaration(cu4*,td);
            }
        )*
        {
            cu=`Java_compilationUnit(cu1,cu2,cu3,cu4);
        }
	;

packageDeclaration returns [Java_package pd]
	:	'package' qn=qualifiedName ';'
        {
            pd=`Java_package(qn);
        }
	;
	
importDeclaration returns [Java_importDeclaration id]
@init {
    Java_isStatic id1=`Java_nonStatic();
    Java_IdentifierList id3=`Java_IdentifierList();
    Java_importSuffix id4=`Java_importNoSuffix();
}
	:	'import' 
        (
            'static'
            {
                id1=`Java_static();
            }
        )? 
        i1=Identifier 
        (
            '.' i2=Identifier
            {
                id3=`Java_IdentifierList(id3*,Java_Identifier(i2.getText()));
            }
        )* 
        (
            '.' '*'
            {
                id4=`Java_importSuffixStar();
            }
        )?
        ';'
        {
            id=`Java_importDeclaration(id1,Java_Identifier(i1.getText()),id3,id4);
        }
	;
	
typeDeclaration returns [Java_classOrInterfaceDeclaration res]
	:	coid=classOrInterfaceDeclaration
        {
            res=coid;
        }
    |   ';'
        {
            res=null;
        }
	;
	
classOrInterfaceDeclaration returns [Java_classOrInterfaceDeclaration res]
@init {
    Java_modifierList ml=`Java_concModifier();
}
	:	
        (
            m=modifier
            {
                ml=`Java_concModifier(ml*,m);
            }
        )* 
        ( 
                cd=classDeclaration
                {
                    res=`Java_classDeclaration(ml,cd);
                }
            | 
                id=interfaceDeclaration
                {
                    res=`Java_interfaceDeclaration(ml,id);
                }
        )
	;
	
classDeclaration returns [Java_classDeclaration cd]
	:	ncd=normalClassDeclaration
        {
            cd=ncd;
        }
    |   ed=enumDeclaration
        {
            cd=`Java_classDeclaration_2(ed);
        }
	;
	
normalClassDeclaration returns [Java_classDeclaration ncd]
@init {
    Java_typeParameterList ncd2=`Java_typeParameterList();
    Java_typeList ncd3=`Java_typeList();
    Java_typeList ncd4=`Java_typeList();
}
	:	'class' i=Identifier
        (
            tp=typeParameters
            {
                ncd2=tp;
            }
        )?
        (
            'extends' t=type
            {
                ncd3=`Java_typeList(t);
            }
        )?
        (
            'implements' tl=typeList
            {
                ncd4=tl;
            }
        )?
        cb=classBody
        {
            ncd=`Java_normalClassDeclaration(Java_Identifier(i.getText()),ncd2,ncd3,ncd4,cb);
        }
	;
	
typeParameters returns [Java_typeParameterList tp]
@init {
    tp=`Java_typeParameterList();
}
	:	'<' tp1=typeParameter 
        (
            ',' tp2=typeParameter
            {
                tp=`Java_typeParameterList(tp*,tp2);
            }
        )*
        '>'
        {
            tp=`Java_typeParameterList(tp1,tp*);
        }
	;

typeParameter returns [Java_typeParameter tp]
@init {
    Java_bound tp2=`Java_bound();
}
	:	i=Identifier 
        (
            'extends' b=bound
            {
                tp2=b;
            }
        )?
        {
            tp=`Java_typeParameter(Java_Identifier(i.getText()),tp2);
        }
	;
		
bound returns [Java_bound b]
@init {
    b=`Java_bound();
}
	:	t1=type
        (
            '&' t2=type
            {
                b=`Java_bound(b*,t2);
            }
        )*
        {
            b=`Java_bound(t1,b*);
        }
	;

enumDeclaration returns [Java_enumDeclaration ed]
@init {
    Java_typeList ed3=`Java_typeList();
}
	:	e=ENUM i=Identifier 
        (
            'implements' tl=typeList
            {
                ed3=tl;
            }
        )?
        eb=enumBody
        {
            ed=`Java_enumDeclaration(Java_ENUM(e.getText()),Java_Identifier(i.getText()),ed3,eb);
        }
	;
	
enumBody returns [Java_enumBody eb]
@init {
    Java_enumConstantList eb1=`Java_enumConstantList();
    Java_optionalComa eb2=`Java_noOptionalComa();
    Java_enumBodyDeclarationList eb3=`Java_enumBodyDeclarationList();
}
	:	'{'
        (
            ec=enumConstants
            {
                eb1=ec;
            }
        )? 
        (
            ','
            {
                eb2=`Java_optionalComa();
            }
        )? 
        (
            ebd=enumBodyDeclarations
            {
                eb3=ebd;
            }
        )?
        '}'
        {
            eb=`Java_enumBody(eb1,eb2,eb3);
        }
	;

enumConstants returns [Java_enumConstantList ec]
@init {
    ec=`Java_enumConstantList();
}
	:	ec1=enumConstant 
        (
            ',' ec2=enumConstant
            {
                ec=`Java_enumConstantList(ec*,ec2);
            }
        )*
        {
            ec=`Java_enumConstantList(ec1,ec*);
        }
	;
	
enumConstant returns [Java_enumConstant ec]
@init {
    Java_annotationList ec1=`Java_annotationList();
    Java_arguments ec3=`Java_arguments(Java_expressionList());
    Java_classBody ec4=`Java_classBody();
}
	:	(
            an=annotations
            {
                ec1=an;
            }
        )? 
        i=Identifier 
        (
            ar=arguments
            {
                ec3=ar;
            }
        )?
        (
            cb=classBody
            {
                ec4=cb;
            }
        )?
        {
            ec=`Java_enumConstant(ec1,Java_Identifier(i.getText()),ec3,ec4);
        }
	;
	
enumBodyDeclarations returns [Java_enumBodyDeclarationList ebd]
@init {
    ebd=`Java_enumBodyDeclarationList();
}
	:	';' 
        (
            cbd=classBodyDeclaration
            {
                ebd=`Java_enumBodyDeclarationList(ebd*,cbd);
            }
        )*
	;
	
interfaceDeclaration returns [Java_interfaceDeclaration id]
	:   nid=normalInterfaceDeclaration
        {
            id=nid;
        }
    |   atd=annotationTypeDeclaration
        {
            id=`Java_annotationInterfaceDeclaration(atd);
        }
	;
	
normalInterfaceDeclaration returns [Java_interfaceDeclaration nid]
@init {
    Java_typeParameterList nid2=`Java_typeParameterList();
    Java_typeList nid3=`Java_typeList();
}
	:	'interface' i=Identifier 
        (
            tp=typeParameters
            {
                nid2=tp;
            }
        )?
        (
            'extends' tl=typeList
            {
                nid3=tl;
            }
        )?
        ib=interfaceBody
        {
            nid=`Java_normalInterfaceDeclaration(Java_Identifier(i.getText()),nid2,nid3,ib);
        }
	;
	
typeList returns [Java_typeList tl]
@init {
    tl=`Java_typeList();
}
	:	t1=type 
        (
            ',' t2=type
            {
                tl=`Java_typeList(tl*,t2);
            }
        )*
        {
            tl=`Java_typeList(t1,tl*);
        }
	;
	
classBody returns [Java_classBody cb]
@init {
    cb = `Java_classBody();
}
	:	'{' 
        (
            cbd=classBodyDeclaration
            {
                cb=`Java_classBody(cb*,cbd);
            }
        )*
        '}'
	;
	
interfaceBody returns [Java_interfaceBody ib]
@init {
    ib=`Java_interfaceBody();
}
	:	'{' 
        (
            ibd=interfaceBodyDeclaration
            {
                ib=`Java_interfaceBody(ib*,ibd);
            }
        )*
        '}'
	;

classBodyDeclaration returns [Java_classBodyDeclaration ebd]
@init {
    Java_isStatic ebd2=`Java_nonStatic();
    Java_modifierList ebd3=`Java_concModifier();
}
	:	    ';'
            {
                ebd=`Java_classBodyDeclarationEmpty();
            }
	    |
            (
                'static'
                {
                    ebd2=`Java_static();
                }
            )?
            b=block
            {
                ebd=`Java_classBodyDeclarationBlock(ebd2,b);
            }
	    |
            (
                m=modifier
                {
                    ebd3=`Java_concModifier(ebd3*,m);
                }
            )*
            md=memberDecl
            {
                ebd=`Java_classBodyDeclarationMember(ebd3,md);
            }
	;
	
memberDecl returns [Java_memberDecl md]
	:	gmocd=genericMethodOrConstructorDecl
        {
            md=`Java_memberGenericMethodOrConstructorDecl(gmocd);
        }
	|	md1=methodDeclaration
        {
            md=`Java_memberMethodDeclaration(md1);
        }
	|	fd=fieldDeclaration
        {
            md=`Java_memberFieldDeclaration(fd);
        }
	|	'void' i=Identifier vmdr=voidMethodDeclaratorRest
        {
            md=`Java_memberVoidMethodDeclaratorRest(Java_Identifier(i.getText()),vmdr);
        }
	|	i=Identifier cdr=constructorDeclaratorRest
        {
            md=`Java_memberConstructorDeclaratorRest(Java_Identifier(i.getText()),cdr);
        }
	|	id=interfaceDeclaration
        {
            md=`Java_memberInterfaceDeclaration(id);
        }
	|	cd=classDeclaration
        {
            md=`Java_memberClassDeclaration(cd);
        }
	;
	
genericMethodOrConstructorDecl returns [Java_genericMethodOrConstructorDecl gmocd]
	:	tp=typeParameters gmocr=genericMethodOrConstructorRest[tp]
        {
            gmocd=gmocr;
        }
	;
	
genericMethodOrConstructorRest [Java_typeParameterList tp] returns [Java_genericMethodOrConstructorDecl gmocr]
@init {
    Java_returnType gmocr1=`Java_returnVoid();
}
	:	(
                t=type
                {
                    gmocr1=`Java_returnType(t);
                }
            |
                'void'
                {
                    gmocr1=`Java_returnVoid();
                }
        )
        i=Identifier mdr=methodDeclaratorRest
        {
            gmocr=`Java_genericMethodDecl(tp,gmocr1,Java_Identifier(i.getText()),mdr);
        }
	|	i=Identifier cdr=constructorDeclaratorRest
        {
            gmocr=`Java_genericConstructorDecl(tp,Java_Identifier(i.getText()),cdr);
        }
	;

methodDeclaration returns [Java_methodDeclaration md]
	:	t=type i=Identifier mdr=methodDeclaratorRest
        {
            md=`Java_methodDeclaration(t,Java_Identifier(i.getText()),mdr);
        }
	;

fieldDeclaration returns [Java_fieldDeclaration fd]
	:	t=type vd=variableDeclarators ';'
        {
            fd=`Java_fieldDeclaration(t,vd);
        }
	;
		
interfaceBodyDeclaration returns [Java_interfaceBodyDeclaration ibd]
@init {
    Java_modifierList ibd1=`Java_concModifier();
}
    :       (
                m=modifier
                {
                    ibd1=`Java_concModifier(ibd1*,m);
                }
            )* 
            imd=interfaceMemberDecl
            {
                ibd=`Java_interfaceBodyDeclaration(ibd1,imd);
            }
	    |
            ';'
            {
                ibd=`Java_interfaceEmptyBody();
            }
	;

interfaceMemberDecl returns [Java_interfaceMemberDecl imd]
	:	imofd=interfaceMethodOrFieldDecl
        {
            imd=`Java_interfaceMemberMethodOrFieldDecl(imofd);
        }
	|   igmd=interfaceGenericMethodDecl
        {
            imd=`Java_interfaceMemberGenericMethodDecl(igmd);
        }
    |   'void' i=Identifier vimdr=voidInterfaceMethodDeclaratorRest[`Java_Identifier(i.getText())]
        {
            imd=vimdr;
        }
    |   id=interfaceDeclaration
        {
            imd=`Java_interfaceMemberInterfaceDecl(id);
        }
    |   cd=classDeclaration
        {
            imd=`Java_interfaceMemberClassDecl(cd);
        }
	;
	
interfaceMethodOrFieldDecl returns [Java_interfaceMethodOrFieldDecl imofr]
	:	t=type i=Identifier imofd=interfaceMethodOrFieldRest[t,`Java_Identifier(i.getText())]
{
  imofr=imofd;
}
	;
	
interfaceMethodOrFieldRest [Java_type typ, Java_Identifier ident] returns [Java_interfaceMethodOrFieldDecl imofr]
	:	cdr=constantDeclaratorsRest ';'
        {
            imofr=`Java_interfaceFieldDecl(typ,ident,cdr);
        }
	|	imdr=interfaceMethodDeclaratorRest
        {
            imofr=`Java_interfaceMethodDecl(typ,ident,imdr);
        }
	;
	
methodDeclaratorRest returns [Java_methodDeclaratorRest mdr]
@init {
    Java_bracketsList b=`Java_bracketsList();
    Java_qualifiedNameList mdr3=`Java_qualifiedNameList();
    Java_methodBody mdr4=null;
}
	:	fp=formalParameters 
        (
            '[' ']'
            {
                b=`Java_bracketsList(b*,Java_brackets());
            }
        )*
        (
            'throws' qnl=qualifiedNameList
            {
                mdr3=qnl;
            }
        )?
        (
                mb=methodBody
                {
                    mdr4=mb;
                }
            |
                ';'
                {
                    mdr4=`Java_emptyMethodBody();
                }
        )
        {
            mdr=`Java_methodDeclaratorRest(fp,b,mdr3,mdr4);
        }
	;
	
voidMethodDeclaratorRest returns [Java_voidMethodDeclaratorRest vmd]
@init {
    Java_qualifiedNameList vmd2=`Java_qualifiedNameList();
    Java_methodBody vmd3=null;
}
	:	fp=formalParameters 
        (
            'throws' qnl=qualifiedNameList
            {
                vmd2=qnl;
            }
        )?
        (
                mb=methodBody
                {
                    vmd3=mb;
                }
            |
                ';'
                {
                    vmd3=`Java_emptyMethodBody();
                }
        )
        {
            vmd=`Java_voidMethodDeclaratorRest(fp,vmd2,vmd3);
        }
	;
	
interfaceMethodDeclaratorRest returns [Java_interfaceMethodDeclaratorRest imdr]
@init {
    Java_bracketsList b=`Java_bracketsList();
    Java_qualifiedNameList imdr3=`Java_qualifiedNameList();
}
	:	fp=formalParameters 
        (
            '[' ']'
            {
                b=`Java_bracketsList(b*,Java_brackets());
            }
            
        )* 
        (
            'throws' qnl=qualifiedNameList
            {
                imdr3=qnl;
            }
        )?
        ';'
        {
            imdr=`Java_interfaceMethodDeclaratorRest(fp,b,imdr3);
        }
	;
	
interfaceGenericMethodDecl returns [Java_interfaceGenericMethodDecl igmd]
@init {
    Java_returnType igmd2=null;
}
	:	tp=typeParameters 
        (
                t=type 
                {
                    igmd2=`Java_returnType(t);
                }
            |
                'void'
                {
                    igmd2=`Java_returnVoid();
                }
        ) 
        i=Identifier
        imdr=interfaceMethodDeclaratorRest
        {
            igmd=`Java_interfaceGenericMethodDecl(tp,igmd2,Java_Identifier(i.getText()),imdr);
        }
	;
	
voidInterfaceMethodDeclaratorRest [Java_Identifier ident] returns [Java_interfaceMemberDecl vimd]
@init {
    Java_qualifiedNameList vimd2=`Java_qualifiedNameList();
}
	:	fp=formalParameters 
        (
            'throws' qnl=qualifiedNameList
            {
                vimd2=qnl;
            }
        )? 
        ';'
        {
            vimd=`Java_interfaceMemberVoidMethodDecl(ident,fp,vimd2);
        }
	;
	
constructorDeclaratorRest returns [Java_constructorDeclaratorRest cdr]
@init {
    Java_qualifiedNameList cdr2=`Java_qualifiedNameList();
}
	:	fp=formalParameters 
        (
            'throws' qnl=qualifiedNameList
            {
                cdr2=qnl;
            }
        )? 
        mb=methodBody
        {
            cdr=`Java_constructorDeclaratorRest(fp,cdr2,mb);
        }
	;

constantDeclarator returns [Java_constantDeclarator cd]
	:	i=Identifier cdr=constantDeclaratorRest
        {
            cd=`Java_constantDeclarator(Java_Identifier(i.getText()),cdr);
        }
	;
	
variableDeclarators returns [Java_variableDeclaratorList vd]
@init {
    vd=`Java_variableDeclaratorList();
}
	:	vd1=variableDeclarator 
        (
            ',' vd2=variableDeclarator
            {
                vd=`Java_variableDeclaratorList(vd*,vd2);
            }
        )*
        {
            vd=`Java_variableDeclaratorList(vd1,vd*);
        }
	;

variableDeclarator returns [Java_variableDeclarator vd]
	:	i=Identifier vdr=variableDeclaratorRest
        {
            vd=`Java_variableDeclarator(Java_Identifier(i.getText()),vdr);
        }
	;
	
variableDeclaratorRest returns [Java_variableDeclaratorRest vdr]
@init {
    Java_bracketsList b=`Java_bracketsList();
    Java_variableDeclaratorRest_1_2 vdr2=`Java_variableDeclaratorRest_1_2_2();
}
	:	(
            '[' ']'
            {
                b=`Java_bracketsList(b*,Java_brackets());
            }
        )+ 
        (
            '=' vi=variableInitializer
            {
                vdr2=`Java_variableDeclaratorRest_1_2_1(vi);
            }
        )?
        {
            vdr=`Java_variableDeclaratorRest_1(b,vdr2);
        }
	|	'=' vi=variableInitializer
        {
            vdr=`Java_variableDeclaratorRest_2(vi);
        }
	|
        {
            vdr=`Java_variableDeclaratorRest_3();
        }
	;
	
constantDeclaratorsRest returns [Java_constantDeclaratorsRest cdr]@init {
    Java_constantDeclaratorList tcdr=`Java_constantDeclaratorList();
}
    :   cdr1=constantDeclaratorRest 
        (
            ',' cd=constantDeclarator
            {
                tcdr=`Java_constantDeclaratorList(tcdr*,cd);
            }
        )*
        {
            cdr=`Java_constantDeclaratorsRest(cdr1,tcdr);
        }
    ;

constantDeclaratorRest returns [Java_constantDeclaratorRest cdr]
@init {
    Java_bracketsList b=`Java_bracketsList();
}
	:	(
            '[' ']'
            {
                b=`Java_bracketsList(b*,Java_brackets());
            }
        )* 
        '=' vi=variableInitializer
        {
            cdr=`Java_constantDeclaratorRest(b,vi);
        }
	;
	
variableDeclaratorId returns [Java_variableDeclaratorId vdi]
@init {
    Java_bracketsList b=`Java_bracketsList();
}
	:	i=Identifier 
        (
            '[' ']'
            {
                b=`Java_bracketsList(b*,Java_brackets());
            }
        )*
        {
            vdi=`Java_variableDeclaratorId(Java_Identifier(i.getText()),b);
        }
	;

variableInitializer returns [Java_variableInitializer vi]
	:	ai=arrayInitializer
        {
            vi=`Java_variableInitializerArray(ai);
        }
    |   e=expression
        {
            vi=`Java_variableInitializerExpression(e);
        }
	;
	
arrayInitializer returns [Java_arrayInitializer ai]
@init {
    ai = `Java_emptyArrayInitializer();
    Java_variableInitializerList ai2=`Java_variableInitializerList();
    Java_optionalComa ai3=`Java_noOptionalComa();
}
	:	'{' 
        (
            vi1=variableInitializer 
            (
                ',' vi2=variableInitializer
                {
                    ai2=`Java_variableInitializerList(ai2*,vi2);
                }
            )*
            (
                ','
                {
                    ai3=`Java_optionalComa();
                }
            )?
            {
                ai=`Java_arrayInitializer(ai2,ai3);
            }
        )?
        '}'
	;

modifier returns [Java_modifier m]
    :   a=annotation { m=`Java_modifier_annotation(a); }
    |   'public' { m=`Java_modifier_public(); }
    |   'protected' { m=`Java_modifier_protected(); }
    |   'private' { m=`Java_modifier_private(); }
    |   'static' { m=`Java_modifier_static(); }
    |   'abstract' { m=`Java_modifier_abstract(); }
    |   'final' { m=`Java_modifier_final(); }
    |   'native' { m=`Java_modifier_native(); }
    |   'synchronized' { m=`Java_modifier_synchronized(); }
    |   'transient' { m=`Java_modifier_transient(); }
    |   'volatile' { m=`Java_modifier_volatile(); }
    |   'strictfp' { m=`Java_modifier_strictfp(); }
    ;

packageOrTypeName returns [Java_packageOrTypeName potn]
@init {
    potn=`Java_packageOrTypeName();
}
	:	i1=Identifier 
        (
            '.' i2=Identifier
            {
                potn=`Java_packageOrTypeName(potn*,Java_Identifier(i2.getText()));
            }
        )*
        {
            potn=`Java_packageOrTypeName(Java_Identifier(i1.getText()),potn*);
        }
	;

enumConstantName returns [Java_enumConstantName ecn]
    :   i=Identifier
        {
            ecn=`Java_enumConstantName(Java_Identifier(i.getText()));
        }
    ;

typeName returns [Java_typeName tn]
	:   i=Identifier
        {
            tn=`Java_typeNameIdentifier(Java_Identifier(i.getText()));
        }
    |   potn=packageOrTypeName '.' i=Identifier
        {
            tn=`Java_typeNameQualifiedIdentifier(potn,Java_Identifier(i.getText()));
        }
	;

type returns [Java_type t]
@init {
    Java_typeWithArgsList t1_2=`Java_typeWithArgsList();
    Java_typeArgumentList t1_2_2=`Java_typeArgumentList();
    Java_bracketsList b=`Java_bracketsList();
}
	:	i1=Identifier 
        (
            ta1=typeArguments
            {
                t1_2_2=ta1;
            }
        )? 
        {
            t1_2=`Java_typeWithArgsList(Java_typeWithArgs(Java_Identifier(i1.getText()),t1_2_2));
            t1_2_2=`Java_typeArgumentList();
        }
        (
            '.' i2=Identifier 
            (
                ta2=typeArguments
                {
                    t1_2_2=ta2;
                }
            )?
            {
                t1_2=`Java_typeWithArgsList(t1_2*,Java_typeWithArgs(Java_Identifier(i2.getText()),t1_2_2));
                t1_2_2=`Java_typeArgumentList();
            }
        )*
        (
            '[' ']'
            {
                b=`Java_bracketsList(b*,Java_brackets());
            }
        )*
        {
            t=`Java_type(t1_2,b);
        }
	|	pt=primitiveType
        (
            '[' ']' 
            {
                b=`Java_bracketsList(b*,Java_brackets());
            }
        )*
        {
            t=`Java_typePrimitive(pt,b);
        }
	;

primitiveType returns [Java_primitiveType pt]
    : 'boolean' { pt=`Java_primitiveTypeBoolean(); }
    |	'char' { pt=`Java_primitiveTypeChar(); }
    |	'byte' { pt=`Java_primitiveTypeByte(); }
    |	'short' { pt=`Java_primitiveTypeShort(); }
    |	'int' { pt=`Java_primitiveTypeInt(); }
    |	'long' { pt=`Java_primitiveTypeLong(); }
    |	'float' { pt=`Java_primitiveTypeFloat(); }
    |	'double' { pt=`Java_primitiveTypeDouble(); }
    ;

variableModifier returns [Java_variableModifier vm]
	:	'final'
        {
            vm=`Java_variableModifierFinal();
        }
    |   a=annotation
        {
            vm=`Java_variableModifierAnnotation(a);
        }
	;

typeArguments returns [Java_typeArgumentList ta]
@init {
    ta=`Java_typeArgumentList();
}
	:	'<' ta1=typeArgument
        (
            ',' ta2=typeArgument
            {
                ta=`Java_typeArgumentList(ta*,ta2);
            }
        )*
        '>'
        {
            ta=`Java_typeArgumentList(ta1,ta*);
        }
	;
	
typeArgument returns [Java_typeArgument ta]
@init {
    ta=`Java_emptyTypeArgumentExtended();
    Java_typeArgumentQualifier ta2=null;
}
	:	t=type
        {
            ta=`Java_typeArgumentType(t);
        }
	|	'?' 
        (
            (
                    'extends' 
                    {
                        ta2=`Java_typeArgumentExtends();
                    }
                |
                    'super'
                    {
                        ta2=`Java_typeArgumentSuper();
                    }
            )
            t=type
            {
                ta=`Java_typeArgumentExtended(ta2,t);
            }
        )?
	;
	
qualifiedNameList returns [Java_qualifiedNameList qnl]
@init {
    qnl=`Java_qualifiedNameList();
}
	:	qn1=qualifiedName 
        (
            ',' qn2=qualifiedName
            {
                qnl=`Java_qualifiedNameList(qnl*,qn2);
            }
        )*
        {
            qnl=`Java_qualifiedNameList(qn1,qnl*);
        }
	;
	
formalParameters returns [Java_formalParameterList fp]
@init {
    fp=`Java_formalParameterList();
}
	:	'(' 
        (
            fpd=formalParameterDecls
            {
                fp=fpd;
            }
        )?
        ')'
	;
	
formalParameterDecls returns [Java_formalParameterList fpd]
@init {
    fpd = `Java_formalParameterList();
    Java_formalParameterDeclsIsFinal fpd1=`Java_formalParameterDeclsNotFinal();
    Java_annotationList fpd2=`Java_annotationList();
    Java_formalParameterList fpdrest=`Java_formalParameterList();
}
	:	
        (
            'final'
            {
                fpd1=`Java_formalParameterDeclsFinal();
            }
        )?
        (
            a=annotations
            {
                fpd2=`a;
            }
        )?
        t=type 
        (
            fpdr=formalParameterDeclsRest[fpd1,fpd2,t]
            {
                fpdrest=fpdr;
            }
        )?
        {
          if(fpdrest==`Java_formalParameterList()) {
            fpd=`Java_formalParameterList(Java_formalParameterDeclIncomplete(fpd1,fpd2,t));
          } else {
            fpd=fpdrest;
          }
        }
	;
	
formalParameterDeclsRest [Java_formalParameterDeclsIsFinal isFinal, Java_annotationList annos, Java_type type] returns [Java_formalParameterList fpdr]
@init {
    fpdr = `Java_formalParameterList();
    Java_formalParameterList fpdr2=`Java_formalParameterList();
}
	:	vdi=variableDeclaratorId 
        (
            ',' fpd=formalParameterDecls
            {
                fpdr2=fpd;
            }
        )?
        {
            fpdr=`Java_formalParameterList(Java_formalParameterDecl(isFinal,annos,type,vdi),fpdr2*);
        }
	|   '...' vdi=variableDeclaratorId
        {
            fpdr=`Java_formalParameterList(Java_formalParameterDeclVarArg(isFinal,annos,type,vdi));
        }
	;
	
methodBody returns [Java_methodBody mb]
	:	b=block
        { mb=`Java_methodBody(b); }
	;

qualifiedName returns [Java_qualifiedName qn]
@init {
    Java_qualifierList qn2=`Java_qualifierList();
}
	:	i1=Identifier 
        (
            '.' i2=Identifier
            {
                qn2=`Java_qualifierList(qn2*,Java_Identifier(i2.getText()));
            }
        )*
        {
            qn=`Java_qualifiedName(Java_Identifier(i1.getText()),qn2);
        }
	;
	
literal	returns [Java_literal l]
	:   il=integerLiteral
        { l=il; }
    |   fpl=FloatingPointLiteral
        { l=`Java_floatingPointLiteral(fpl.getText()); }
    |   cl=CharacterLiteral
        { l=`Java_characterLiteral(cl.getText()); }
    |   sl=StringLiteral
        { l=`Java_stringLiteral(sl.getText()); }
    |   bl=booleanLiteral
        { l=bl; }
    |   'null'
        { l=`Java_nullLiteral(); }
	;

integerLiteral returns [Java_literal il]
    :   h=HexLiteral 
        { il=`Java_hexLiteral(h.getText()); }
    |   o=OctalLiteral
        { il=`Java_octalLiteral(o.getText()); }
    |   d=DecimalLiteral
        { il=`Java_decimalLiteral(d.getText()); }
    ;

booleanLiteral returns [Java_literal bl]
    :   'true'
        { bl=`Java_true(); }
    |   'false'
        { bl=`Java_false(); }
    ;

// ANNOTATIONS

annotations returns [Java_annotationList a]
@init {
    a=`Java_annotationList();
}
	:	
        (
            a1=annotation
            {
                a=`Java_annotationList(a*,a1);
            }
        )+
	;

annotation returns [Java_annotation a]
@init {
  Java_Identifier ident = null;
  Java_elementValue elemv = null;
}
	:	'@' tn=typeName
        (
            '(' 
            (
                i=Identifier '='
                {
                    ident=`Java_Identifier(i.getText());
                }
            )? 
            ev=elementValue
            ')'
            {
                elemv=ev;
            }
        )?
        {
          if (elemv != null) {
            if(null == ident) {
              a=`Java_annotationId(tn,elemv);
            } else {
              a=`Java_annotationElem(tn,ident,elemv);
            }
          } else {
            a=`Java_annotationSimple(tn);
          }
        }
	;
	
elementValue returns [Java_elementValue ev]
	:	ce=conditionalExpression
        {
            ev=`Java_elementValueConditional(ce);
        }
	|   a=annotation
        {
            ev=`Java_elementValueAnnotation(a);
        }
	|   evai=elementValueArrayInitializer
        {
            ev=`evai;
        }
	;
	
elementValueArrayInitializer returns [Java_elementValue evai]
@init {
    Java_elementValueArrayInitializer_1 evai1=`Java_elementValueArrayInitializer_1_2();
    Java_optionalComa evai2=`Java_noOptionalComa();
}
	:	'{' 
        (
            ev=elementValue
            {
                evai1=`Java_elementValueArrayInitializer_1_1(ev);
            }
        )? 
        (
            ','
            {
                evai2=`Java_optionalComa();
            }
        )?
        '}'
        {
            evai=`Java_elementValueArrayInitializer(evai1,evai2);
        }
	;
	
annotationTypeDeclaration returns [Java_annotationTypeDeclaration atd]
	:	'@' 'interface' i=Identifier atb=annotationTypeBody
        {
            atd=`Java_annotationTypeDeclaration(Java_Identifier(i.getText()),atb);
        }
	;
	
annotationTypeBody returns [Java_annotationTypeBody atb]
@init {
    atb=`Java_annotationTypeBody();
}
	:	'{'
        (
            ated=annotationTypeElementDeclarations
            {
                atb=ated;
            }
        )? 
        '}'
	;
	
annotationTypeElementDeclarations returns [Java_annotationTypeBody ated]
@init {
    ated=`Java_annotationTypeBody();
}
	:	ated1=annotationTypeElementDeclaration 
        (
            ated2=annotationTypeElementDeclaration
            {
                ated=`Java_annotationTypeBody(ated*,ated2);
            }
        )*
        {
            ated=`Java_annotationTypeBody(ated1,ated*);
        }
	;
	
annotationTypeElementDeclaration returns [Java_annotationTypeElementDeclaration ated]
@init {
    Java_modifierList ated1=`Java_concModifier();
}
	:	(
            m=modifier
            {
                ated1=`Java_concModifier(ated1*,m);
            }
        )*
        ater=annotationTypeElementRest
        {
            ated=`Java_annotationTypeElementDeclaration(ated1,ater);
        }
	;
	
annotationTypeElementRest returns [Java_annotationTypeElementRest ater]
	:	t=type i=Identifier amocr=annotationMethodOrConstantRest[t,`Java_Identifier(i.getText())] ';'
        {
            ater=amocr;
        }
	|   cd=classDeclaration
        {
            ater=`Java_annotationTypeElementClass(cd);
        }
	|   id=interfaceDeclaration
        {
            ater=`Java_annotationTypeElementInterface(id);
        }
	|   ed=enumDeclaration
        {
            ater=`Java_annotationTypeElementEnum(ed);
        }
	|   atd=annotationTypeDeclaration
        {
            ater=`Java_annotationTypeElementType(atd);
        }
	;
	
annotationMethodOrConstantRest [Java_type typ, Java_Identifier ident] returns [Java_annotationTypeElementRest amocr]
	:	amr=annotationMethodRest
        {
            amocr=`Java_annotationTypeElementMethod(typ,ident,amr);
        }
	|   acr=annotationConstantRest
        {
            amocr=`Java_annotationTypeElementConstant(typ,ident,acr);
        }
	;
	
annotationMethodRest returns [Java_annotationMethod amr]
@init {
    amr=`Java_annotationMethodEmpty();
}
 	:	'(' ')' 
        (
            dv=defaultValue
            {
                amr=`Java_annotationMethodDefault(dv);
            }
        )?
 	;
 	
annotationConstantRest returns [Java_annotationConstant acr]
 	:	vd=variableDeclarators
        {
            acr=`Java_annotationConstant(vd);
        }
 	;
 	
defaultValue returns [Java_defaultValue dv]
 	:	'default' ev=elementValue
        {
            dv=`Java_defaultValue(ev);
        }
 	;

// STATEMENTS / BLOCKS

block returns [Java_block b]
@init {
    b=`Java_block();
}
	:	'{' 
        (
            bs=blockStatement
            {
                b=`Java_block(b*,bs);
            }
        )*
        '}'
	;
	
blockStatement returns [Java_blockStatement bs]
	:	lvd=localVariableDeclaration
        {
            bs=`Java_localVariable(lvd);
        }
    |   coid=classOrInterfaceDeclaration
        {
            bs=`Java_classOrInterface(coid);
        }
    |   s=statement
        {
            bs=`Java_statement(s);
        }
	;
	
localVariableDeclaration returns [Java_localVariableDeclaration lvd]
@init {
    Java_localVariableModifier lvd1=`Java_localVariableNoModifier();
}
	:	(
            'final'
            {
                lvd1=`Java_localVariableModifierFinal();
            }
        )? 
        t=type vd=variableDeclarators ';'
        {
            lvd=`Java_localVariableDeclaration(lvd1,t,vd);
        }
        
	;
	
statement returns [Java_statement s]
@init {
    Java_expression ts2=null;
    Java_statement ts3=null;
    Java_block ts7=null;
    Java_expression ts10=null;
    Java_IdentifierOrNone ts12=`Java_NoIdentifier();
    Java_IdentifierOrNone ts13=`Java_NoIdentifier();
    Java_catches tcatch=`Java_catches();
}
	: 
            b=block
            {
                s=`Java_statementBlock(b);
            }
        | 
            'assert' e1=expression 
            (
                ':' e2=expression
                {
                    ts2=e2;
                }
            )?
            ';'
            {
              if (null == ts2) {
                s=`Java_statementAssert(e1);
              } else {
                s=`Java_statementAssertWithValue(e1,ts2);
              }
            }
        | 
            'if' pe=parExpression s1=statement 
            (
                'else' s2=statement
                {
                    ts3=s2;
                }
            )?
            {
              if (null == ts3) {
                s=`Java_statementIfThen(pe,s1);
              } else {
                s=`Java_statementIfThenElse(pe,s1,ts3);
              }
            }
        | 
            'for' '(' fc=forControl ')' s1=statement
            {
                s=`Java_statementFor(fc,s1);
            }
        |
            'while' pe=parExpression s1=statement
            {
                s=`Java_statementWhile(pe,s1);
            }
        |
            'do' s1=statement 'while' pe=parExpression ';'
            {
                s=`Java_statementDoWhile(s1,pe);
            }
        |
            'try' b=block
            (
                    c=catches 'finally' b=block
                    {
                        ts7=b;
                        tcatch=c;
                    }
                | 
                    c=catches
                    {
                        tcatch=c;
                    }
                |
                    'finally' b=block
                    {
                        ts7=b;
                    }
            )
            {
              if(null==ts7) {
                s=`Java_statementTry(b,tcatch);
              } else {
                s=`Java_statementTryFinally(b,tcatch,ts7);
              }
            }
        |
            'switch' pe=parExpression '{' sbsg=switchBlockStatementGroups '}'
            {
                s=`Java_statementSwitch(pe,sbsg);
            }
        |
            'synchronized' pe=parExpression b=block
            {
                s=`Java_statementSynchronized(pe,b);
            }
        |
            'return' 
            (
                e=expression
                {
                    ts10=e;
                }
            )? 
            ';'
            {
              if(null == ts10) {
                s=`Java_statementReturnVoid();
              } else {
                s=`Java_statementReturn(ts10);
              }
            }
        |
            'throw' e=expression ';'
            {
                s=`Java_statementThrow(e);
            }
        |
            'break'
            (
                i=Identifier
                {
                    ts12=`Java_hasIdentifier(Java_Identifier(i.getText()));
                }
            )?
            ';'
            {
                s=`Java_statementBreak(ts12);
            }
        |
            'continue' 
            (
                i=Identifier
                {
                    ts13=`Java_hasIdentifier(Java_Identifier(i.getText()));
                }
            )?
            ';'
            {
                s=`Java_statementContinue(ts13);
            }
        |
            ';'
            {
                s=`Java_statementNop();
            }
        |
            se=statementExpression ';'
            {
                s=`Java_statementExpression(se);
            }
        | 
            i=Identifier ':' s1=statement
            {
                s=`Java_statementLabel(Java_Identifier(i.getText()),s1);
            }
	;
	
catches returns [Java_catches c]
@init {
    c=`Java_catches();
}
	:	cc1=catchClause
        (
            cc2=catchClause
            {
                c=`Java_catches(c*,cc2);
            }
        )*
        {
            c=`Java_catches(cc1,c*);
        }
	;
	
catchClause returns [Java_catchClause cc]
	:	'catch' '(' fp=formalParameter ')' b=block
        {
            cc=`Java_catchClause(fp,b);
        }
	;

formalParameter returns [Java_formalParameter fp]
@init {
    Java_variableModifierList fp1=`Java_variableModifierList();
}
	:	(
            vm=variableModifier
            {
                fp1=`Java_variableModifierList(fp1*,vm);
            }
        )*
        t=type vdi=variableDeclaratorId
        {
            fp=`Java_formalParameter(fp1,t,vdi);
        }
	;
	
switchBlockStatementGroups returns [Java_switchBlockStatementGroups sbsg]
@init {
    sbsg=`Java_switchBlockStatementGroups_1();
}
	:	(
            sbsg1=switchBlockStatementGroup
            {
                sbsg=`Java_switchBlockStatementGroups_1(sbsg*,sbsg1);
            }
        )*
	;
	
switchBlockStatementGroup returns [Java_switchBlockStatementGroup sbsg]
@init {
    Java_switchBlockStatementGroup_2 sbsg2=`Java_switchBlockStatementGroup_2_1();
}
	:	sl=switchLabel 
        (
            bs=blockStatement
            {
                sbsg2=`Java_switchBlockStatementGroup_2_1(sbsg2*,bs);
            }
        )*
        {
            sbsg=`Java_switchBlockStatementGroup(sl,sbsg2);
        }
	;
	
switchLabel returns [Java_switchLabel sl]
	:
            'case' ce=constantExpression ':'
            {
                sl=`Java_switchLabelExpression(ce);
            }
	|
        'case' ecn=enumConstantName ':'
            {
                sl=`Java_switchLabelEnum(ecn);
            }
	|   'default' ':'
            {
                sl=`Java_switchLabelDefault();
            }
	;
	
moreStatementExpressions returns [Java_moreStatementExpressions mse]
@init {
    mse=`Java_moreStatementExpressions();
}
	:	(
            ',' se=statementExpression
            {
                mse=`Java_moreStatementExpressions(mse*,se);
            }
        )*
	;

forControl returns [Java_forControl fc]
@init {
    Java_forControl_2_1 fc1=`Java_forControl_2_1_2();
    Java_forControl_2_2 fc2=`Java_forControl_2_2_2();
    Java_forControl_2_3 fc3=`Java_forControl_2_3_2();}
	:	    fvc=forVarControl
            {
                fc=`Java_forControl_1(fvc);
            }
	    |   
            (
                fi=forInit
                {
                    fc1=`Java_forControl_2_1_1(fi);
                }
            )?
            ';'
            (
                e=expression
                {
                    fc2=`Java_forControl_2_2_1(e);
                }
            )?
            ';'
            (
                fu=forUpdate
                {
                    fc3=`Java_forControl_2_3_1(fu);
                }
            )?
            {
                fc=`Java_forControl_2(fc1,fc2,fc3);
            }
	;

forInit returns [Java_forInit fi]
@init {
    Java_forInit_1_1 fi1=`Java_forInit_1_1_2();
}
	:	    (
                'final'
                {
                    fi1=`Java_forInit_1_1_1();
                }
            )?
            t=type vd=variableDeclarators
            {
                fi=`Java_forInit_1(fi1,t,vd);
            }
        |  
            el=expressionList
            {
                `Java_forInit_2(el);
            }
	;
	
forVarControl returns [Java_forVarControl fvc]
@init {
    Java_forVarControl_1 fvc1=`Java_forVarControl_1_2();
    Java_forVarControl_2 fvc2=`Java_forVarControl_2_2();
}
	:	(
            'final'
            {
                fvc1=`Java_forVarControl_1_1();
            }
        )?
        (
            a=annotation
            {
                fvc2=`Java_forVarControl_2_1(a);
            }
        )? 
        t=type i=Identifier fvcr=forVarControlRest
        {
            fvc=`Java_forVarControl(fvc1,fvc2,t,Java_Identifier(i.getText()),fvcr);
        }
	;

forVarControlRest returns [Java_forVarControlRest fvcr]
@init {
    Java_forVarControlRest_1_2 fvcr2=`Java_forVarControlRest_1_2_1();
    Java_forVarControlRest_1_3 fvcr3=`Java_forVarControlRest_1_3_2();
    Java_forVarControlRest_1_4 fvcr4=`Java_forVarControlRest_1_4_2();
}
	:	    vdr=variableDeclaratorRest 
            (
                ',' vd=variableDeclarator
                {
                    fvcr2=`Java_forVarControlRest_1_2_1(fvcr2*,Java_forVarControlRest_1_2_1_1(vd));
                }
            )*
            ';' 
            (
                e=expression
                {
                    fvcr3=`Java_forVarControlRest_1_3_1(e);
                }
            )?
            ':' 
            (
                fu=forUpdate
                {
                    fvcr4=`Java_forVarControlRest_1_4_1(fu);
                }
            )?
            {
                fvcr=`Java_forVarControlRest_1(vdr,fvcr2,fvcr3,fvcr4);
            }
        |   
            ':' e=expression
            {
                fvcr=`Java_forVarControlRest_2(e);
            }
	;

forUpdate returns [Java_forUpdate fu]
	:	el=expressionList
        {
            fu=`Java_forUpdate(el);
        }
                                
	;

// EXPRESSIONS

parExpression returns [Java_parExpression pe]
	:	'(' e=expression ')'
        {
            pe=`Java_parExpression(e);
        }
	;
	
expressionList returns [Java_expressionList el]
@init {
    el=`Java_expressionList();
}
    :   e1=expression 
        (
            ',' e2=expression
            {
                el=`Java_expressionList(el*,e2);
            }
        )*
        {
            el=`Java_expressionList(e1,el*);
        }
    ;

statementExpression returns [Java_expression se]
	:	e=expression
        {
            se=e;
        }
	;
	
constantExpression returns [Java_constantExpression ce]
	:	e=expression
        {
            ce=`Java_constantExpression(e);
        }
	;
	
expression returns [Java_expression e]
@init {
    Java_expressionOptionalAssign e2=`Java_expressionNoAssign();
}
	:	ce=conditionalExpression 
        (
            ao=assignmentOperator e1=expression
            {
                e2=`Java_expressionAssign(ao,e1);
            }
        )?
        {
            e=`Java_expression(ce,e2);
        }
	;
	
assignmentOperator returns [Java_assignmentOperator ao]
	:	'='
        {
            ao=`Java_assignmentOperator("=");
        }
    |   '+='
        {
            ao=`Java_assignmentOperator("+=");
        }
    |   '-='
        {
            ao=`Java_assignmentOperator("-=");
        }
    |   '*='
        {
            ao=`Java_assignmentOperator("*=");
        }
    |   '/='
        {
            ao=`Java_assignmentOperator("/=");
        }
    |   '&='
        {
            ao=`Java_assignmentOperator("&=");
        }
    |   '|='
        {
            ao=`Java_assignmentOperator("|=");
        }
    |   '^='
        {
            ao=`Java_assignmentOperator("^=");
        }
    |   '%='
        {
            ao=`Java_assignmentOperator("%=");
        }
    |   '<' '<' '='
        {
            ao=`Java_assignmentOperator("<<=");
        }
    |   '>' '>' '='
        {
            ao=`Java_assignmentOperator(">>=");
        }
    |   '>' '>' '>' '='
        {
            ao=`Java_assignmentOperator(">>>=");
        }
	;

conditionalExpression returns [Java_conditionalExpression ce]
@init {
    Java_conditionalExpression_2 ce2=`Java_conditionalExpression_2_2();
}
    :   coe=conditionalOrExpression 
        (
            '?' e1=expression ':' e2=expression 
            {
                ce2=`Java_conditionalExpression_2_1(e1,e2);
            }
        )?
        {
            ce=`Java_conditionalExpression(coe,ce2);
        }
	;

conditionalOrExpression returns [Java_conditionalOrExpression coe]
@init {
    coe=`Java_conditionalOrExpression();
}
    :   cae1=conditionalAndExpression 
        ( 
            '||' cae2=conditionalAndExpression 
            {
                coe=`Java_conditionalOrExpression(coe*,cae2);
            }
        )*
        {
            coe=`Java_conditionalOrExpression(cae1,coe*);
        }
	;

conditionalAndExpression returns [Java_conditionalAndExpression cae]
@init {
    cae=`Java_conditionalAndExpression();
}
    :   ioe1=inclusiveOrExpression 
        (
            '&&' ioe2=inclusiveOrExpression 
            {
                cae=`Java_conditionalAndExpression(cae*,ioe2);
            }
        )*
        {
            cae=`Java_conditionalAndExpression(ioe1,cae*);
        }
	;

inclusiveOrExpression returns [Java_inclusiveOrExpression ioe]
@init {
    ioe=`Java_inclusiveOrExpression();
}
    :   eoe1=exclusiveOrExpression 
        (
            '|' eoe2=exclusiveOrExpression 
            {
                ioe=`Java_inclusiveOrExpression(ioe*,eoe2);
            }
        )*
        {
            ioe=`Java_inclusiveOrExpression(eoe1,ioe*);
        }
	;

exclusiveOrExpression returns [Java_exclusiveOrExpression eoe]
@init {
    eoe=`Java_exclusiveOrExpression();
}
    :   ae1=andExpression
        (
            '^' ae2=andExpression 
            {
                eoe=`Java_exclusiveOrExpression(eoe*,ae2);
            }
        )*
        {
            eoe=`Java_exclusiveOrExpression(ae1,eoe*);
        }
	;

andExpression returns [Java_andExpression ae]
@init {
    ae=`Java_andExpression();
}
    :   ee1=equalityExpression 
        (
            '&' ee2=equalityExpression 
            {
                ae=`Java_andExpression(ae*,ee2);
            }
        )*
        {
            ae=`Java_andExpression(ee1,ae*);
        }
	;

equalityExpression returns [Java_equalityExpression ee]
@init {
    Java_equalityExpressionList ee2=`Java_equalityExpressionList();
    Java_equalityOperator ee3=null;
}
    :   ioe1=instanceOfExpression 
        (
            (
                    '=='
                    {
                        ee3=`Java_equalityOperator("==");
                    }
                |
                    '!='
                    {
                        ee3=`Java_equalityOperator("!=");
                    }
            ) 
            ioe2=instanceOfExpression
            {
                ee2=`Java_equalityExpressionList(ee2*,Java_equalityExpressionElem(ee3,ioe2));
            }
        )*
        {
            ee=`Java_equalityExpression(ioe1,ee2);
        }
	;

instanceOfExpression returns [Java_instanceOfExpression ioe]
@init {
    Java_instanceOfExpression_2 ioe2=`Java_instanceOfExpression_2_2();
}
    :   rexp=relationalExpression 
        (
            'instanceof' t=type
            {
                ioe2=`Java_instanceOfExpression_2_1(t);
            }
        )?
        {
            ioe=`Java_instanceOfExpression(rexp,ioe2);
        }
	;

relationalExpression returns [Java_relationalExpression rexp]
@init {
    Java_relationalExpressionList re2=`Java_relationalExpressionList();
}
    :   se1=shiftExpression 
        ( 
            ro=relationalOp se2=shiftExpression 
            {
                re2=`Java_relationalExpressionList(re2*,Java_relationalExpressionElem(ro,se2));
            }
        )*
        {
            rexp=`Java_relationalExpression(se1,re2);
        }
	;
	
relationalOp returns [Java_relationalOp ro]
	:	(
            '<' '=' 
            {
                ro=`Java_relationalOp("<=");
            }
            | '>' '=' 
            {
                ro=`Java_relationalOp(">=");
            }
            | '<' 
            {
                ro=`Java_relationalOp("<");
            }
            | '>'
            {
                ro=`Java_relationalOp(">");
            }
        )
	;

shiftExpression returns [Java_shiftExpression se]
@init {
    Java_shiftExpression_2 se2=`Java_shiftExpression_2_1();
}
    :   ae1=additiveExpression 
        ( 
            so=shiftOp 
            ae2=additiveExpression 
            {
                se2=`Java_shiftExpression_2_1(se2*,Java_shiftExpression_2_1_1(so,ae2));
            }
        )*
        {
            se=`Java_shiftExpression(ae1,se2);
        }
	;

        // TODO: need a sem pred to check column on these >>>
shiftOp returns [Java_shiftOp so]
	:	(
            '<' '<' 
            {
                so=`Java_shiftOp("<<");
            }
            | '>' '>' '>'
            {
                so=`Java_shiftOp(">>>");
            }
            | '>' '>'
            {
                so=`Java_shiftOp(">>");
            }
        )
	;


additiveExpression returns [Java_additiveExpression ae]
@init {
    Java_additiveExpression_2 ae2=`Java_additiveExpression_2_1();
    Java_additiveOperation ae3=null;
}
    :   me1=multiplicativeExpression 
        ( 
            (
                    '+' 
                    {
                        ae3=`Java_addition();
                    }
                | 
                    '-'
                    {
                        ae3=`Java_subtration();
                    }
            )
            me2=multiplicativeExpression
            {
                ae2=`Java_additiveExpression_2_1(ae2*,Java_additiveExpression_2_1_1(ae3,me2));
            }
        )*
        {
            ae=`Java_additiveExpression(me1,ae2);
        }
	;

multiplicativeExpression returns [Java_multiplicativeExpression me]
@init {
    Java_multiplicativeExpression_2 me2=`Java_multiplicativeExpression_2_1();
    Java_multiplicativeOperation me3=null;
}
    :   ue1=unaryExpression 
        (
            ( 
                    '*' 
                    {
                        me3=`Java_multiplicativeOperation("*");
                    }
                | 
                    '/' 
                    {
                        me3=`Java_multiplicativeOperation("/");
                    }
                | 
                    '%' 
                    {
                        me3=`Java_multiplicativeOperation("%");
                    }
            ) 
            ue2=unaryExpression
            {
                me2=`Java_multiplicativeExpression_2_1(me2*,Java_multiplicativeExpression_2_1_1(me3,ue2));
            }
        )*
        {
            me=`Java_multiplicativeExpression(ue1,me2);
        }
	;
	
unaryExpression returns [Java_unaryExpression ue]
    :       '+' ue1=unaryExpression
            {
                ue=`Java_unaryExpression_1(ue1);
            }
        |	
            '-' ue1=unaryExpression
            {
                ue=`Java_unaryExpression_2(ue1);
            }
        |
            '++' p=primary
            {
                ue=`Java_unaryExpression_3(p);
            }
        |
            '--' p=primary
            {
                ue=`Java_unaryExpression_4(p);
            }
        |
            uenpm=unaryExpressionNotPlusMinus
            {
                ue=`Java_unaryExpression_5(uenpm);
            }
    ;

unaryExpressionNotPlusMinus returns [Java_unaryExpressionNotPlusMinus uenpm]
@init {
    Java_unaryExpressionNotPlusMinus_4_2 uenpm2=`Java_unaryExpressionNotPlusMinus_4_2_1();
    Java_unaryExpressionNotPlusMinus_4_3 uenpm3=`Java_unaryExpressionNotPlusMinus_4_3_0();
}
    :       '~' ue=unaryExpression
            {
                uenpm=`Java_unaryExpressionNotPlusMinus_1(ue);
            }
        |
            '!' ue=unaryExpression
            {
                uenpm=`Java_unaryExpressionNotPlusMinus_2(ue);
            }
        |
            ce=castExpression
            {
                uenpm=`Java_unaryExpressionNotPlusMinus_3(ce);
            }
        |
            p=primary 
            (
                s=selector
                {
                    uenpm2=`Java_unaryExpressionNotPlusMinus_4_2_1(uenpm2*,s);
                }
            )* 
            (
                    '++'
                    {
                        uenpm3=`Java_unaryExpressionNotPlusMinus_4_3_1();
                    }
                |
                    '--'
                    {
                        uenpm3=`Java_unaryExpressionNotPlusMinus_4_3_2();
                    }
            )?
            {
                uenpm=`Java_unaryExpressionNotPlusMinus_4(p,uenpm2,uenpm3);
            }
    ;

castExpression returns [Java_castExpression ce]
@init {
  Java_expression exp = null;
  Java_type typ = null;
}
    :       '(' pt=primitiveType ')' ue=unaryExpression
            {
                ce=`Java_castExpressionPrimitive(pt,ue);
            }
        |  
            '(' 
            (
                    e=expression
                    {
                        exp=e;
                    }
                | 
                    t=type
                    {
                        typ=t;
                    }
            )
            ')' uenpm=unaryExpressionNotPlusMinus
            {
              if(e != null) {
                ce=`Java_castExpressionExpression(e,uenpm);
              } else {
                ce=`Java_castExpressionType(t,uenpm);
              }
            }
    ;

primary returns [Java_primary p]
@init {
    Java_primary_2_2 p2=null;
    Java_primary_3_1 p3=`Java_primary_3_1_2();
    Java_primary_7_2 p7_2=`Java_primary_7_2_1();
    Java_primary_7_3 p7_3=`Java_primary_7_3_2();
    Java_bracketsList b=`Java_bracketsList();
}
    :	    pe=parExpression
            {
                p=`Java_primary_1(pe);
            }
        |   
            nwta=nonWildcardTypeArguments
            (
                    egis=explicitGenericInvocationSuffix 
                    {
                        p2=`Java_primary_2_2_1(egis);
                    }
                |
                    'this' a=arguments
                    {
                        p2=`Java_primary_2_2_2(a);
                    }
            )
            {
                p=`Java_primary_2(nwta,p2);
            }
        |
            'this' 
            (
                a=arguments
                {
                    p3=`Java_primary_3_1_1(a);
                }
            )?
            {
                p=`Java_primary_3(p3);
            }
        |
            'super' sup=superSuffix
            {
                p=`Java_primary_4(sup);
            }
        |
            l=literal
            {
                p=`Java_primary_5(l);
            }
        |
            'new' c=creator
            {
                p=`Java_primary_6(c);
            }
        |
            i1=Identifier 
            (
                '.' i2=Identifier
                {
                    p7_2=`Java_primary_7_2_1(p7_2*,Java_primary_7_2_1_1(Java_Identifier(i2.getText())));
                }
            )* 
            (
                is=identifierSuffix
                {
                    p7_3=`Java_primary_7_3_1(is);
                }
            )?
            {
                p=`Java_primary_7(Java_Identifier(i1.getText()),p7_2,p7_3);
            }
        |
            pt=primitiveType 
            (
                '[' ']'
                {
                    b=`Java_bracketsList(b*,Java_brackets());
                }
            )*
            '.' 'class'
            {
                p=`Java_primary_8(pt,b);
            }
        |
            'void' '.' 'class'
            {
                p=`Java_primary_9();
            }
	;

identifierSuffix returns [Java_identifierSuffix is]
@init {
    Java_bracketsList b=`Java_bracketsList();
    Java_identifierSuffix is2=`Java_identifierSuffixBracket();
    Java_nonWildcardTypeArguments is8=`Java_emptyNonWildcardTypeArguments();
}
	:	    (
                '[' ']'
                {
                   b=`Java_bracketsList(b*,Java_brackets());
                }
            )+ 
            '.' 'class'
            {
                is=`Java_identifierSuffixEmptyBracket(b);
            }
	    |	
            (
                '[' e=expression ']'
                {
                    is2=`Java_identifierSuffixBracket(is2*,e);
                }
            )+ // can also be matched by selector, but do here
            {
                is=is2;
            }
        |   
            a=arguments
            {
                is=`Java_identifierSuffixArguments(a);
            }
        |
            '.' 'class'
            {
                is=`Java_identifierSuffixClass();
            }
        |
            '.' egi=explicitGenericInvocation
            {
                is=`Java_identifierSuffixInvocation(egi);
            }
        |
            '.' 'this'
            {
                is=`Java_identifierSuffixThis();
            }
        |
            '.' 'super' a=arguments
            {
                is=`Java_identifierSuffixSuper(a);
            }
        |
            '.' 'new' 
            (
                nwta=nonWildcardTypeArguments
                {
                    is8=nwta;
                }
            )?
            ic=innerCreator
            {
                is=`Java_identifierSuffixInnerClass(is8,ic);
            }
	;

creator returns [Java_creator c]
@init {
    Java_nonWildcardTypeArguments c1=`Java_emptyNonWildcardTypeArguments();
}
	:	(
            nwta=nonWildcardTypeArguments
            {
                c1=nwta;
            }
        )?
        cn=createdName
        (
                acr=arrayCreatorRest[c1,cn]
                {
                    c=acr;
                }
            |
                ccr=classCreatorRest
                {
                    c=`Java_classCreator(c1,cn,ccr);
                }
        )
	;

createdName returns [Java_createdName cn]
@init {
    Java_createdName_1_2 cn2=`Java_createdName_1_2_2();
    Java_createdName_1_3 cn1=`Java_createdName_1_3_1();
    Java_createdName_1_3_1_1_2 cn3=`Java_createdName_1_3_1_1_2_2();
}
	:	    i=Identifier 
            (
                nwta1=nonWildcardTypeArguments
                {
                    cn2=`Java_createdName_1_2_1(nwta1);
                }
            )?
            (
                '.' i=Identifier 
                (
                    nwta2=nonWildcardTypeArguments
                    {
                        cn3=`Java_createdName_1_3_1_1_2_1(nwta2);
                    }
                )?
                {
                    cn1=`Java_createdName_1_3_1(cn1*,Java_createdName_1_3_1_1(Java_Identifier(i.getText()),cn3));
                }
            )*
            {
                cn=`Java_createdName_1(Java_Identifier(i.getText()),cn2,cn1);
            }
    |
            pt=primitiveType
            {
                cn=`Java_createdName_2(pt);
            }
	;
	
innerCreator returns [Java_innerCreator ic]
	:	i=Identifier ccr=classCreatorRest
        {
            ic=`Java_innerCreator(Java_Identifier(i.getText()),ccr);
        }
	;

arrayCreatorRest [Java_nonWildcardTypeArguments c1, Java_createdName cn] returns [Java_creator acr]
@init {
    Java_bracketsList b=`Java_bracketsList();
    Java_arrayCreatorRest_2_2 acr2=`Java_arrayCreatorRest_2_2_1();
}
	:	'['
        (
                ']' 
                (
                    '[' ']' 
                    {
                        b=`Java_bracketsList(b*,Java_brackets()); 
                    } 
                )*
                a=arrayInitializer
                {
                    acr=`Java_arrayCreator(c1,cn,Java_arrayCreatorRest_1(b,a));
                }
            | 
                e=expression ']' 
                (
                    '[' e1=expression ']'
                    {
                        acr2=`Java_arrayCreatorRest_2_2_1(acr2*,Java_arrayCreatorRest_2_2_1_1(e1));
                    }
                )*
                (
                    '[' ']'
                    {
                        b=`Java_bracketsList(b*,Java_brackets());
                    }
                )*
                {
                    acr=`Java_arrayCreator(c1,cn,Java_arrayCreatorRest_2(e,acr2,b));
                }
        )
	;

classCreatorRest returns [Java_classCreatorRest ccr]
@init {
    Java_classBody ccr2=`Java_classBody();
}
	:	a=arguments 
        (
            cb=classBody
            {
                ccr2=cb;
            }
        )?
        {
            ccr=`Java_classCreatorRest(a,ccr2);
        }
	;
	
explicitGenericInvocation returns [Java_explicitGenericInvocation egi]
	:	nwta=nonWildcardTypeArguments egis=explicitGenericInvocationSuffix
        {
            egi=`Java_explicitGenericInvocation(nwta,egis);
        }
	;
	
nonWildcardTypeArguments returns [Java_nonWildcardTypeArguments nwta]
	:	'<' t=typeList '>'
        {
            nwta=`Java_nonWildcardTypeArguments(t);
        }
	;
	
explicitGenericInvocationSuffix returns [Java_explicitGenericInvocationSuffix egis]
	:	    'super' s=superSuffix
            {
                egis=`Java_explicitGenericInvocationSuffixSuper(s);
            }
	    |
            i=Identifier a=arguments
            {
                egis=`Java_explicitGenericInvocationSuffixMethod(Java_Identifier(i.getText()),a);
            }
 	;
	
selector returns [Java_selector s]
@init {
    Java_arguments sel1=null;
    Java_nonWildcardTypeArguments sel4=`Java_emptyNonWildcardTypeArguments();
}
	:	    '.' i=Identifier 
            (
                a=arguments
                {
                    sel1=a;
                }
            )?
            {
              if(null==sel1) {
                s=`Java_selectorField(Java_Identifier(i.getText()));
              } else {
                s=`Java_selectorMethod(Java_Identifier(i.getText()),sel1);
              }
            }
	    |
            '.' 'this'
            {
                s=`Java_selectorThis();
            } 
	    |
            '.' 'super' s2=superSuffix
            {
                s=`Java_selectorSuper(s2);
            }
	    |
            '.' 'new' 
            (
                nwta=nonWildcardTypeArguments
                {
                    sel4=nwta;
                }
            )?
            ic=innerCreator
            {
                s=`Java_selectorInner(sel4,ic);
            }
	    |
            '[' e=expression ']'
            {
                s=`Java_selectorExpression(e);
            }
	;
	
superSuffix returns [Java_superSuffix sup]
@init {
    Java_arguments s=null;
}
	:	    a=arguments
            {
                sup=`Java_superConstructor(a);
            }
	    | 
            '.' i=Identifier 
            (
                a=arguments
                {
                    s=a;
                }
            )?
            {
              if(null == s) {
                sup=`Java_superMember(Java_Identifier(i.getText()));
              } else {
                sup=`Java_superMethod(Java_Identifier(i.getText()),s);
              }
            }
        
    ;

arguments returns [Java_arguments a]
@init {
    Java_expressionList a1=`Java_expressionList();
}
	:	'('
        (
            el=expressionList
            {
                a1=el;
            }
        )? ')'
        {
            a=`Java_arguments(a1);
        }
	;

// LEXER

HexLiteral : '0' ('x'|'X') HexDigit+ IntegerTypeSuffix? ;

DecimalLiteral : ('0' | '1'..'9' '0'..'9'*) IntegerTypeSuffix? ;

OctalLiteral : '0' ('0'..'7')+ IntegerTypeSuffix? ;

fragment
HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
IntegerTypeSuffix : ('l'|'L') ;

FloatingPointLiteral
    :   ('0'..'9')+ '.' ('0'..'9')* Exponent? FloatTypeSuffix?
    |   '.' ('0'..'9')+ Exponent? FloatTypeSuffix?
    |   ('0'..'9')+ Exponent FloatTypeSuffix?
    |   ('0'..'9')+ Exponent? FloatTypeSuffix
	;

fragment
Exponent : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
FloatTypeSuffix : ('f'|'F'|'d'|'D') ;

CharacterLiteral
    :   '\'' ( EscapeSequence | ~('\''|'\\') ) '\''
    ;

StringLiteral
    :  '"' ( EscapeSequence | ~('\\'|'"') )* '"'
    ;

fragment
EscapeSequence
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UnicodeEscape
    |   OctalEscape
    ;

fragment
OctalEscape
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

ENUM:	'enum' {if ( !enumIsKeyword ) $type=Identifier;}
	;
	
Identifier 
    :   Letter (Letter|JavaIDDigit)*
    ;

/**I found this char range in JavaCC's grammar, but Letter and Digit overlap.
   Still works, but...
 */
fragment
Letter
    :  '\u0024' |
       '\u0041'..'\u005a' |
       '\u005f' |
       '\u0061'..'\u007a' |
       '\u00c0'..'\u00d6' |
       '\u00d8'..'\u00f6' |
       '\u00f8'..'\u00ff' |
       '\u0100'..'\u1fff' |
       '\u3040'..'\u318f' |
       '\u3300'..'\u337f' |
       '\u3400'..'\u3d2d' |
       '\u4e00'..'\u9fff' |
       '\uf900'..'\ufaff'
    ;

fragment
JavaIDDigit
    :  '\u0030'..'\u0039' |
       '\u0660'..'\u0669' |
       '\u06f0'..'\u06f9' |
       '\u0966'..'\u096f' |
       '\u09e6'..'\u09ef' |
       '\u0a66'..'\u0a6f' |
       '\u0ae6'..'\u0aef' |
       '\u0b66'..'\u0b6f' |
       '\u0be7'..'\u0bef' |
       '\u0c66'..'\u0c6f' |
       '\u0ce6'..'\u0cef' |
       '\u0d66'..'\u0d6f' |
       '\u0e50'..'\u0e59' |
       '\u0ed0'..'\u0ed9' |
       '\u1040'..'\u1049'
   ;

WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
    ;

COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;
