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
    Java_typeDeclarationList cu4=`Java_typeDeclarationList();
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
                cu2=`Java_package(pd);
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
                cu4=`Java_typeDeclarationList(cu4*,td);
            }
        )*
        {
            cu=`Java_compilationUnit(cu1,cu2,cu3,cu4);
        }
	;

packageDeclaration returns [Java_packageDeclaration pd]
	:	'package' qn=qualifiedName ';'
        {
            pd=`Java_packageDeclaration(qn);
        }
	;
	
importDeclaration returns [Java_importDeclaration id]
@init {
    Java_isStaticImport id1=`Java_nonStaticImport();
    Java_importDeclaration_3 id3=`Java_importDeclaration_3_1();
    Java_importDeclaration_4 id4=`Java_importDeclaration_4_2();
}
	:	'import' 
        (
            'static'
            {
                id1=`Java_staticImport();
            }
        )? 
        i1=Identifier 
        (
            '.' i2=Identifier
            {
                id3=`Java_importDeclaration_3_1(id3*,Java_importDeclaration_3_1_1(Java_Identifier(i2.getText())));
            }
        )* 
        (
            '.' '*'
            {
                id4=`Java_importDeclaration_4_1();
            }
        )?
        ';'
        {
            id=`Java_importDeclaration(id1,Java_Identifier(i1.getText()),id3,id4);
        }
	;
	
typeDeclaration returns [Java_typeDeclaration td]
	:	coid=classOrInterfaceDeclaration
        {
            td=`Java_typeDeclaration_1(coid);
        }
    |   ';'
        {
            td=`Java_typeDeclaration_2();
        }
	;
	
classOrInterfaceDeclaration returns [Java_classOrInterfaceDeclaration coid]
@init {
    Java_classOrInterfaceDeclaration_1 coid1=`Java_classOrInterfaceDeclaration_1_1();
    Java_classOrInterfaceDeclaration_2 coid2=null;
}
	:	
        (
            m=modifier
            {
                coid1=`Java_classOrInterfaceDeclaration_1_1(coid1*,m);
            }
        )* 
        ( 
                cd=classDeclaration
                {
                    coid2=`Java_classOrInterfaceDeclaration_2_1(cd);
                }
            | 
                id=interfaceDeclaration
                {
                    coid2=`Java_classOrInterfaceDeclaration_2_2(id);
                }
        )
        {
            coid=`Java_classOrInterfaceDeclaration(coid1,coid2);
        }
	;
	
classDeclaration returns [Java_classDeclaration cd]
	:	ncd=normalClassDeclaration
        {
            cd=`Java_classDeclaration_1(ncd);
        }
    |   ed=enumDeclaration
        {
            cd=`Java_classDeclaration_2(ed);
        }
	;
	
normalClassDeclaration returns [Java_normalClassDeclaration ncd]
@init {
    Java_normalClassDeclaration_2 ncd2=`Java_normalClassDeclaration_2_2();
    Java_normalClassDeclaration_3 ncd3=`Java_normalClassDeclaration_3_2();
    Java_normalClassDeclaration_4 ncd4=`Java_normalClassDeclaration_4_2();
}
	:	'class' i=Identifier
        (
            tp=typeParameters
            {
                ncd2=`Java_normalClassDeclaration_2_1(tp);
            }
        )?
        (
            'extends' t=type
            {
                ncd3=`Java_normalClassDeclaration_3_1(t);
            }
        )?
        (
            'implements' tl=typeList
            {
                ncd4=`Java_normalClassDeclaration_4_1(tl);
            }
        )?
        cb=classBody
        {
            ncd=`Java_normalClassDeclaration(Java_Identifier(i.getText()),ncd2,ncd3,ncd4,cb);
        }
	;
	
typeParameters returns [Java_typeParameters tp]
@init {
    Java_typeParameters_2 ttp=`Java_typeParameters_2_1();
}
	:	'<' tp1=typeParameter 
        (
            ',' tp2=typeParameter
            {
                ttp=`Java_typeParameters_2_1(ttp*,Java_typeParameters_2_1_1(tp2));
            }
        )*
        '>'
        {
            tp=`Java_typeParameters(tp1,ttp);
        }
	;

typeParameter returns [Java_typeParameter tp]
@init {
    Java_typeParameter_2 tp2=`Java_typeParameter_2_2();
}
	:	i=Identifier 
        (
            'extends' b=bound
            {
                tp2=`Java_typeParameter_2_1(b);
            }
        )?
        {
            tp=`Java_typeParameter(Java_Identifier(i.getText()),tp2);
        }
	;
		
bound returns [Java_bound b]
@init {
    Java_bound_2 b2=`Java_bound_2_1();
}
	:	t1=type
        (
            '&' t2=type
            {
                b2=`Java_bound_2_1(b2*,Java_bound_2_1_1(t2));
            }
        )*
        {
            b=`Java_bound(t1,b2);
        }
	;

enumDeclaration returns [Java_enumDeclaration ed]
@init {
    Java_enumDeclaration_3 ed3=`Java_enumDeclaration_3_2();
}
	:	e=ENUM i=Identifier 
        (
            'implements' tl=typeList
            {
                ed3=`Java_enumDeclaration_3_1(tl);
            }
        )?
        eb=enumBody
        {
            ed=`Java_enumDeclaration(Java_ENUM(e.getText()),Java_Identifier(i.getText()),ed3,eb);
        }
	;
	
enumBody returns [Java_enumBody eb]
@init {
    Java_enumBody_1 eb1=`Java_enumBody_1_2();
    Java_enumBody_2 eb2=`Java_enumBody_2_2();
    Java_enumBody_3 eb3=`Java_enumBody_3_2();
}
	:	'{'
        (
            ec=enumConstants
            {
                eb1=`Java_enumBody_1_1(ec);
            }
        )? 
        (
            ','
            {
                eb2=`Java_enumBody_2_1();
            }
        )? 
        (
            ebd=enumBodyDeclarations
            {
                eb3=`Java_enumBody_3_1(ebd);
            }
        )?
        '}'
        {
            eb=`Java_enumBody(eb1,eb2,eb3);
        }
	;

enumConstants returns [Java_enumConstants ec]
@init {
    Java_enumConstants_2 tec=`Java_enumConstants_2_1();
}
	:	ec1=enumConstant 
        (
            ',' ec2=enumConstant
            {
                tec=`Java_enumConstants_2_1(tec*,Java_enumConstants_2_1_1(ec2));
            }
        )*
        {
            ec=`Java_enumConstants(ec1,tec);
        }
	;
	
enumConstant returns [Java_enumConstant ec]
@init {
    Java_enumConstant_1 ec1=`Java_enumConstant_1_2();
    Java_enumConstant_3 ec3=`Java_enumConstant_3_2();
    Java_enumConstant_4 ec4=`Java_enumConstant_4_2();
}
	:	(
            an=annotations
            {
                ec1=`Java_enumConstant_1_1(an);
            }
        )? 
        i=Identifier 
        (
            ar=arguments
            {
                ec3=`Java_enumConstant_3_1(ar);
            }
        )?
        (
            cb=classBody
            {
                ec4=`Java_enumConstant_4_1(cb);
            }
        )?
        {
            ec=`Java_enumConstant(ec1,Java_Identifier(i.getText()),ec3,ec4);
        }
	;
	
enumBodyDeclarations returns [Java_enumBodyDeclarations ebd]
@init {
    Java_enumBodyDeclarations_1 ebd1=`Java_enumBodyDeclarations_1_1();
}
	:	';' 
        (
            cbd=classBodyDeclaration
            {
                ebd1=`Java_enumBodyDeclarations_1_1(ebd1*,cbd);
            }
        )*
        {
            ebd=`Java_enumBodyDeclarations(ebd1);
        }
	;
	
interfaceDeclaration returns [Java_interfaceDeclaration id]
	:   nid=normalInterfaceDeclaration
        {
            id=`Java_interfaceDeclaration_1(nid);
        }
    |   atd=annotationTypeDeclaration
        {
            id=`Java_interfaceDeclaration_2(atd);
        }
	;
	
normalInterfaceDeclaration returns [Java_normalInterfaceDeclaration nid]
@init {
    Java_normalInterfaceDeclaration_2 nid2=`Java_normalInterfaceDeclaration_2_2();
    Java_normalInterfaceDeclaration_3 nid3=`Java_normalInterfaceDeclaration_3_2();
}
	:	'interface' i=Identifier 
        (
            tp=typeParameters
            {
                nid2=`Java_normalInterfaceDeclaration_2_1(tp);
            }
        )?
        (
            'extends' tl=typeList
            {
                nid3=`Java_normalInterfaceDeclaration_3_1(tl);
            }
        )?
        ib=interfaceBody
        {
            nid=`Java_normalInterfaceDeclaration(Java_Identifier(i.getText()),nid2,nid3,ib);
        }
	;
	
typeList returns [Java_typeList tl]
@init {
    Java_typeList_2 tl2=`Java_typeList_2_1();
}
	:	t1=type 
        (
            ',' t2=type
            {
                tl2=`Java_typeList_2_1(tl2*,Java_typeList_2_1_1(t2));
            }
        )*
        {
            tl=`Java_typeList(t1,tl2);
        }
	;
	
classBody returns [Java_classBody cb]
@init {
    Java_classBody_1 cb1=`Java_classBody_1_1();
}
	:	'{' 
        (
            cbd=classBodyDeclaration
            {
                cb1=`Java_classBody_1_1(cb1*,cbd);
            }
        )*
        '}'
        {
            cb=`Java_classBody(cb1);
        }
	;
	
interfaceBody returns [Java_interfaceBody ib]
@init {
    Java_interfaceBody_1 ib1=`Java_interfaceBody_1_1();
}
	:	'{' 
        (
            ibd=interfaceBodyDeclaration
            {
                ib1=`Java_interfaceBody_1_1(ib1*,ibd);
            }
        )*
        '}'
        {
            ib=`Java_interfaceBody(ib1);
        }
	;

classBodyDeclaration returns [Java_classBodyDeclaration ebd]
@init {
    Java_classBodyDeclaration_2_1 ebd2=`Java_classBodyDeclaration_2_1_2();
    Java_classBodyDeclaration_3_1 ebd3=`Java_classBodyDeclaration_3_1_1();
}
	:	    ';'
            {
                ebd=`Java_classBodyDeclaration_1();
            }
	    |
            (
                'static'
                {
                    ebd2=`Java_classBodyDeclaration_2_1_1();
                }
            )?
            b=block
            {
                ebd=`Java_classBodyDeclaration_2(ebd2,b);
            }
	    |
            (
                m=modifier
                {
                    ebd3=`Java_classBodyDeclaration_3_1_1(ebd3*,m);
                }
            )*
            md=memberDecl
            {
                ebd=`Java_classBodyDeclaration_3(ebd3,md);
            }
	;
	
memberDecl returns [Java_memberDecl md]
	:	gmocd=genericMethodOrConstructorDecl
        {
            md=`Java_memberDecl_1(gmocd);
        }
	|	md1=methodDeclaration
        {
            md=`Java_memberDecl_2(md1);
        }
	|	fd=fieldDeclaration
        {
            md=`Java_memberDecl_3(fd);
        }
	|	'void' i=Identifier vmdr=voidMethodDeclaratorRest
        {
            md=`Java_memberDecl_4(Java_Identifier(i.getText()),vmdr);
        }
	|	i=Identifier cdr=constructorDeclaratorRest
        {
            md=`Java_memberDecl_5(Java_Identifier(i.getText()),cdr);
        }
	|	id=interfaceDeclaration
        {
            md=`Java_memberDecl_6(id);
        }
	|	cd=classDeclaration
        {
            md=`Java_memberDecl_7(cd);
        }
	;
	
genericMethodOrConstructorDecl returns [Java_genericMethodOrConstructorDecl gmocd]
	:	tp=typeParameters gmocr=genericMethodOrConstructorRest
        {
            gmocd=`Java_genericMethodOrConstructorDecl(tp,gmocr);
        }
	;
	
genericMethodOrConstructorRest returns [Java_genericMethodOrConstructorRest gmocr]
@init {
    Java_genericMethodOrConstructorRest_1_1 gmocr1=null;
}
	:	(
                t=type 
                {
                    gmocr1=`Java_genericMethodOrConstructorRest_1_1_1(t);
                }
            |
                'void'
                {
                    gmocr1=`Java_genericMethodOrConstructorRest_1_1_2();
                }
        )
        i=Identifier mdr=methodDeclaratorRest
        {
            gmocr=`Java_genericMethodOrConstructorRest_1(gmocr1,Java_Identifier(i.getText()),mdr);
        }
	|	i=Identifier cdr=constructorDeclaratorRest
        {
            gmocr=`Java_genericMethodOrConstructorRest_2(Java_Identifier(i.getText()),cdr);
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
    Java_interfaceBodyDeclaration_1_1 ibd1=`Java_interfaceBodyDeclaration_1_1_1();
}
    :       (
                m=modifier
                {
                    ibd1=`Java_interfaceBodyDeclaration_1_1_1(ibd1*,m);
                }
            )* 
            imd=interfaceMemberDecl
            {
                ibd=`Java_interfaceBodyDeclaration_1(ibd1,imd);
            }
	    |
            ';'
            {
                ibd=`Java_interfaceBodyDeclaration_2();
            }
	;

interfaceMemberDecl returns [Java_interfaceMemberDecl imd]
	:	imofd=interfaceMethodOrFieldDecl
        {
            imd=`Java_interfaceMemberDecl_1(imofd);
        }
	|   igmd=interfaceGenericMethodDecl
        {
            imd=`Java_interfaceMemberDecl_2(igmd);
        }
    |   'void' i=Identifier vimdr=voidInterfaceMethodDeclaratorRest
        {
            imd=`Java_interfaceMemberDecl_3(Java_Identifier(i.getText()),vimdr);
        }
    |   id=interfaceDeclaration
        {
            imd=`Java_interfaceMemberDecl_4(id);
        }
    |   cd=classDeclaration
        {
            imd=`Java_interfaceMemberDecl_5(cd);
        }
	;
	
interfaceMethodOrFieldDecl returns [Java_interfaceMethodOrFieldDecl imofd]
	:	t=type i=Identifier imofr=interfaceMethodOrFieldRest
        {
            imofd=`Java_interfaceMethodOrFieldDecl(t,Java_Identifier(i.getText()),imofr);
        }
	;
	
interfaceMethodOrFieldRest returns [Java_interfaceMethodOrFieldRest imofr]
	:	cdr=constantDeclaratorsRest ';'
        {
            imofr=`Java_interfaceMethodOrFieldRest_1(cdr);
        }
	|	imdr=interfaceMethodDeclaratorRest
        {
            imofr=`Java_interfaceMethodOrFieldRest_2(imdr);
        }
	;
	
methodDeclaratorRest returns [Java_methodDeclaratorRest mdr]
@init {
    Java_methodDeclaratorRest_2 mdr2=`Java_methodDeclaratorRest_2_1();
    Java_methodDeclaratorRest_3 mdr3=`Java_methodDeclaratorRest_3_2();
    Java_methodDeclaratorRest_4 mdr4=null;
}
	:	fp=formalParameters 
        (
            '[' ']'
            {
                mdr2=`Java_methodDeclaratorRest_2_1(mdr2*,Java_methodDeclaratorRest_2_1_1());
            }
        )*
        (
            'throws' qnl=qualifiedNameList
            {
                mdr3=`Java_methodDeclaratorRest_3_1(qnl);
            }
        )?
        (
                mb=methodBody
                {
                    mdr4=`Java_methodDeclaratorRest_4_1(mb);
                }
            |
                ';'
                {
                    mdr4=`Java_methodDeclaratorRest_4_2();
                }
        )
        {
            mdr=`Java_methodDeclaratorRest(fp,mdr2,mdr3,mdr4);
        }
	;
	
voidMethodDeclaratorRest returns [Java_voidMethodDeclaratorRest vmd]
@init {
    Java_voidMethodDeclaratorRest_2 vmd2=`Java_voidMethodDeclaratorRest_2_2();
    Java_voidMethodDeclaratorRest_3 vmd3=null;
}
	:	fp=formalParameters 
        (
            'throws' qnl=qualifiedNameList
            {
                vmd2=`Java_voidMethodDeclaratorRest_2_1(qnl);
            }
        )?
        (
                mb=methodBody
                {
                    vmd3=`Java_voidMethodDeclaratorRest_3_1(mb);
                }
            |
                ';'
                {
                    vmd3=`Java_voidMethodDeclaratorRest_3_2();
                }
        )
        {
            vmd=`Java_voidMethodDeclaratorRest(fp,vmd2,vmd3);
        }
	;
	
interfaceMethodDeclaratorRest returns [Java_interfaceMethodDeclaratorRest imdr]
@init {
    Java_interfaceMethodDeclaratorRest_2 imdr2=`Java_interfaceMethodDeclaratorRest_2_1();
    Java_interfaceMethodDeclaratorRest_3 imdr3=`Java_interfaceMethodDeclaratorRest_3_2();
}
	:	fp=formalParameters 
        (
            '[' ']'
            {
                imdr2=`Java_interfaceMethodDeclaratorRest_2_1(imdr2*,Java_interfaceMethodDeclaratorRest_2_1_1());
            }
            
        )* 
        (
            'throws' qnl=qualifiedNameList
            {
                imdr3=`Java_interfaceMethodDeclaratorRest_3_1(qnl);
            }
        )?
        ';'
        {
            imdr=`Java_interfaceMethodDeclaratorRest(fp,imdr2,imdr3);
        }
	;
	
interfaceGenericMethodDecl returns [Java_interfaceGenericMethodDecl igmd]
@init {
    Java_interfaceGenericMethodDecl_2 igmd2=null;
}
	:	tp=typeParameters 
        (
                t=type 
                {
                    igmd2=`Java_interfaceGenericMethodDecl_2_1(t);
                }
            |
                'void'
                {
                    igmd2=`Java_interfaceGenericMethodDecl_2_2();
                }
        ) 
        i=Identifier
        imdr=interfaceMethodDeclaratorRest
        {
            igmd=`Java_interfaceGenericMethodDecl(tp,igmd2,Java_Identifier(i.getText()),imdr);
        }
	;
	
voidInterfaceMethodDeclaratorRest returns [Java_voidInterfaceMethodDeclaratorRest vimd]
@init {
    Java_voidInterfaceMethodDeclaratorRest_2 vimd2=`Java_voidInterfaceMethodDeclaratorRest_2_2();
}
	:	fp=formalParameters 
        (
            'throws' qnl=qualifiedNameList
            {
                vimd2=`Java_voidInterfaceMethodDeclaratorRest_2_1(qnl);
            }
        )? 
        ';'
        {
            vimd=`Java_voidInterfaceMethodDeclaratorRest(fp,vimd2);
        }
	;
	
constructorDeclaratorRest returns [Java_constructorDeclaratorRest cdr]
@init {
    Java_constructorDeclaratorRest_2 cdr2=`Java_constructorDeclaratorRest_2_2();
}
	:	fp=formalParameters 
        (
            'throws' qnl=qualifiedNameList
            {
                cdr2=`Java_constructorDeclaratorRest_2_1(qnl);
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
	
variableDeclarators returns [Java_variableDeclarators vd]
@init {
    Java_variableDeclarators_2 tvd=`Java_variableDeclarators_2_1();
}
	:	vd1=variableDeclarator 
        (
            ',' vd2=variableDeclarator
            {
                tvd=`Java_variableDeclarators_2_1(tvd*,Java_variableDeclarators_2_1_1(vd2));
            }
        )*
        {
            vd=`Java_variableDeclarators(vd1,tvd);
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
    Java_variableDeclaratorRest_1_1 vdr1=`Java_variableDeclaratorRest_1_1_1();
    Java_variableDeclaratorRest_1_2 vdr2=`Java_variableDeclaratorRest_1_2_2();
}
	:	(
            '[' ']'
            {
                vdr1=`Java_variableDeclaratorRest_1_1_1(vdr1*,Java_variableDeclaratorRest_1_1_1_1());
            }
        )+ 
        (
            '=' vi=variableInitializer
            {
                vdr2=`Java_variableDeclaratorRest_1_2_1(vi);
            }
        )?
        {
            vdr=`Java_variableDeclaratorRest_1(vdr1,vdr2);
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
    Java_constantDeclaratorsRest_2 tcdr=`Java_constantDeclaratorsRest_2_1();
}
    :   cdr1=constantDeclaratorRest 
        (
            ',' cd=constantDeclarator
            {
                tcdr=`Java_constantDeclaratorsRest_2_1(tcdr*,Java_constantDeclaratorsRest_2_1_1(cd));
            }
        )*
        {
            cdr=`Java_constantDeclaratorsRest(cdr1,tcdr);
        }
    ;

constantDeclaratorRest returns [Java_constantDeclaratorRest cdr]
@init {
    Java_constantDeclaratorRest_1 cdr1=`Java_constantDeclaratorRest_1_1();
}
	:	(
            '[' ']'
            {
                cdr1=`Java_constantDeclaratorRest_1_1(cdr1*,Java_constantDeclaratorRest_1_1_1());
            }
        )* 
        '=' vi=variableInitializer
        {
            cdr=`Java_constantDeclaratorRest(cdr1,vi);
        }
	;
	
variableDeclaratorId returns [Java_variableDeclaratorId vdi]
@init {
    Java_variableDeclaratorId_2 vdi2=`Java_variableDeclaratorId_2_1();
}
	:	i=Identifier 
        (
            '[' ']'
            {
                vdi2=`Java_variableDeclaratorId_2_1(vdi2*,Java_variableDeclaratorId_2_1_1());
            }
        )*
        {
            vdi=`Java_variableDeclaratorId(Java_Identifier(i.getText()),vdi2);
        }
	;

variableInitializer returns [Java_variableInitializer vi]
	:	ai=arrayInitializer
        {
            vi=`Java_variableInitializer_1(ai);
        }
    |   e=expression
        {
            vi=`Java_variableInitializer_2(e);
        }
	;
	
arrayInitializer returns [Java_arrayInitializer ai]
@init {
    Java_arrayInitializer_1 ai1=`Java_arrayInitializer_1_2();
    Java_arrayInitializer_1_1_2 ai2=`Java_arrayInitializer_1_1_2_1();
    Java_arrayInitializer_1_1_3 ai3=`Java_arrayInitializer_1_1_3_2();
}
	:	'{' 
        (
            vi1=variableInitializer 
            (
                ',' vi2=variableInitializer
                {
                    ai2=`Java_arrayInitializer_1_1_2_1(ai2*,Java_arrayInitializer_1_1_2_1_1(vi2));
                }
            )*
            (
                ','
                {
                    ai3=`Java_arrayInitializer_1_1_3_1();
                }
            )?
            {
                ai1=`Java_arrayInitializer_1_1(vi1,ai2,ai3);
            }
        )?
        '}'
        {
            ai=`Java_arrayInitializer(ai1);
        }
	;

modifier returns [Java_modifier m]
    :   a=annotation { m=`Java_modifier_1(a); }
    |   'public' { m=`Java_modifier_2(); }
    |   'protected' { m=`Java_modifier_3(); }
    |   'private' { m=`Java_modifier_4(); }
    |   'static' { m=`Java_modifier_5(); }
    |   'abstract' { m=`Java_modifier_6(); }
    |   'final' { m=`Java_modifier_7(); }
    |   'native' { m=`Java_modifier_8(); }
    |   'synchronized' { m=`Java_modifier_9(); }
    |   'transient' { m=`Java_modifier_10(); }
    |   'volatile' { m=`Java_modifier_11(); }
    |   'strictfp' { m=`Java_modifier_12(); }
    ;

packageOrTypeName returns [Java_packageOrTypeName potn]
@init {
    Java_packageOrTypeName_2 potn2=`Java_packageOrTypeName_2_1();
}
	:	i1=Identifier 
        (
            '.' i2=Identifier
            {
                potn2=`Java_packageOrTypeName_2_1(potn2*,Java_packageOrTypeName_2_1_1(Java_Identifier(i2.getText())));
            }
        )*
        {
            potn=`Java_packageOrTypeName(Java_Identifier(i1.getText()),potn2);
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
            tn=`Java_typeName_1(Java_Identifier(i.getText()));
        }
    |   potn=packageOrTypeName '.' i=Identifier
        {
            tn=`Java_typeName_2(potn,Java_Identifier(i.getText()));
        }
	;

type returns [Java_type t]
@init {
    Java_type_1_2 t1_2=`Java_type_1_2_2();
    Java_type_1_3 t1_3=`Java_type_1_3_1();
    Java_type_1_3_1_1_2 t1_3_2=`Java_type_1_3_1_1_2_2();
    Java_type_1_4 t1_4=`Java_type_1_4_1();
    Java_type_2_2 t2_2=`Java_type_2_2_1();
}
	:	i1=Identifier 
        (
            ta1=typeArguments
            {
                t1_2=`Java_type_1_2_1(ta1);
            }
        )? 
        (
            '.' i2=Identifier 
            (
                ta2=typeArguments
                {
                    t1_3_2=`Java_type_1_3_1_1_2_1(ta2);
                }
            )?
            {
                t1_3=`Java_type_1_3_1(t1_3*,Java_type_1_3_1_1(Java_Identifier(i2.getText()),t1_3_2));
            }
        )*
        (
            '[' ']'
            {
                t1_4=`Java_type_1_4_1(t1_4*,Java_type_1_4_1_1());
            }
        )*
        {
            t=`Java_type_1(Java_Identifier(i1.getText()),t1_2,t1_3,t1_4);
        }
	|	pt=primitiveType
        (
            '[' ']' 
            {
                t2_2=`Java_type_2_2_1(t2_2*,Java_type_2_2_1_1());
            }
        )*
        {
            t=`Java_type_2(pt,t2_2);
        }
	;

primitiveType returns [Java_primitiveType pt]
    :   'boolean' { pt=`Java_primitiveType_1(); }
    |	'char' { pt=`Java_primitiveType_2(); }
    |	'byte' { pt=`Java_primitiveType_3(); }
    |	'short' { pt=`Java_primitiveType_4(); }
    |	'int' { pt=`Java_primitiveType_5(); }
    |	'long' { pt=`Java_primitiveType_6(); }
    |	'float' { pt=`Java_primitiveType_7(); }
    |	'double' { pt=`Java_primitiveType_8(); }
    ;

variableModifier returns [Java_variableModifier vm]
	:	'final'
        {
            vm=`Java_variableModifier_1();
        }
    |   a=annotation
        {
            vm=`Java_variableModifier_2(a);
        }
	;

typeArguments returns [Java_typeArguments ta]
@init {
    Java_typeArguments_2 ta3=`Java_typeArguments_2_1();
}
	:	'<' ta1=typeArgument
        (
            ',' ta2=typeArgument
            {
                ta3=`Java_typeArguments_2_1(ta3*,Java_typeArguments_2_1_1(ta2));
            }
        )*
        '>'
        {
            ta=`Java_typeArguments(ta1,ta3);
        }
	;
	
typeArgument returns [Java_typeArgument ta]
@init {
    Java_typeArgument_2_1 ta1=`Java_typeArgument_2_1_2();
    Java_typeArgument_2_1_1_1 ta2=null;
}
	:	t=type
        {
            ta=`Java_typeArgument_1(t);
        }
	|	'?' 
        (
            (
                    'extends' 
                    {
                        ta2=`Java_typeArgument_2_1_1_1_1();
                    }
                |
                    'super'
                    {
                        ta2=`Java_typeArgument_2_1_1_1_2();
                    }
            )
            t=type
            {
                ta1=`Java_typeArgument_2_1_1(ta2,t);
            }
        )?
        {
            ta=`Java_typeArgument_2(ta1);
        }
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
	
formalParameters returns [Java_formalParameters fp]
@init {
    Java_formalParameters_1 fp1=`Java_formalParameters_1_2();
}
	:	'(' 
        (
            fpd=formalParameterDecls
            {
                fp1=`Java_formalParameters_1_1(fpd);
            }
        )?
        ')'
        {
            fp=`Java_formalParameters(fp1);
        }
	;
	
formalParameterDecls returns [Java_formalParameterDecls fpd]
@init {
    Java_formalParameterDecls_1 fpd1=`Java_formalParameterDecls_1_2();
    Java_formalParameterDecls_2 fpd2=`Java_formalParameterDecls_2_2();
    Java_formalParameterDecls_4 fpd4=`Java_formalParameterDecls_4_2();
}
	:	
        (
            'final'
            {
                fpd1=`Java_formalParameterDecls_1_1();
            }
        )?
        (
            a=annotations
            {
                fpd2=`Java_formalParameterDecls_2_1(a);
            }
        )?
        t=type 
        (
            fpdr=formalParameterDeclsRest
            {
                fpd4=`Java_formalParameterDecls_4_1(fpdr);
            }
        )?
        {
            fpd=`Java_formalParameterDecls(fpd1,fpd2,t,fpd4);
        }
	;
	
formalParameterDeclsRest returns [Java_formalParameterDeclsRest fpdr]
@init {
    Java_formalParameterDeclsRest_1_2 fpdr2=`Java_formalParameterDeclsRest_1_2_2();
}
	:	vdi=variableDeclaratorId 
        (
            ',' fpd=formalParameterDecls
            {
                fpdr2=`Java_formalParameterDeclsRest_1_2_1(fpd);
            }
        )?
        {
            fpdr=`Java_formalParameterDeclsRest_1(vdi,fpdr2);
        }
	|   '...' vdi=variableDeclaratorId
        {
            fpdr=`Java_formalParameterDeclsRest_2(vdi);
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
        { l=`Java_integerLiteral(il); }
    |   fpl=FloatingPointLiteral
        { l=`Java_floatingPointLiteral(Java_FloatingPointLiteral(fpl.getText())); }
    |   cl=CharacterLiteral
        { l=`Java_characterLiteral(Java_CharacterLiteral(cl.getText())); }
    |   sl=StringLiteral
        { l=`Java_stringLiteral(Java_StringLiteral(sl.getText())); }
    |   bl=booleanLiteral
        { l=`Java_booleanLiteral(bl); }
    |   'null'
        { l=`Java_nullLiteral(); }
	;

integerLiteral returns [Java_integerLiteral il]
    :   h=HexLiteral 
        { il=`Java_HexLiteral(h.getText()); }
    |   o=OctalLiteral
        { il=`Java_OctalLiteral(o.getText()); }
    |   d=DecimalLiteral
        { il=`Java_DecimalLiteral(d.getText()); }
    ;

booleanLiteral returns [Java_booleanLiteral bl]
    :   'true'
        { bl=`Java_True(); }
    |   'false'
        { bl=`Java_False(); }
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
    Java_annotation_2 a2=`Java_annotation_2_2();
    Java_annotation_2_1_1 a3=`Java_annotation_2_1_1_2();
}
	:	'@' tn=typeName
        (
            '(' 
            (
                i=Identifier '='
                {
                    a3=`Java_annotation_2_1_1_1(Java_Identifier(i.getText()));
                }
            )? 
            ev=elementValue
            ')'
            {
                a2=`Java_annotation_2_1(a3,ev);
            }
        )?
        {
            a=`Java_annotation(tn,a2);
        }
	;
	
elementValue returns [Java_elementValue ev]
	:	ce=conditionalExpression
        {
            ev=`Java_elementValue_1(ce);
        }
	|   a=annotation
        {
            ev=`Java_elementValue_2(a);
        }
	|   evai=elementValueArrayInitializer
        {
            ev=`Java_elementValue_3(evai);
        }
	;
	
elementValueArrayInitializer returns [Java_elementValueArrayInitializer evai]
@init {
    Java_elementValueArrayInitializer_1 evai1=`Java_elementValueArrayInitializer_1_2();
    Java_elementValueArrayInitializer_2 evai2=`Java_elementValueArrayInitializer_2_2();
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
                evai2=`Java_elementValueArrayInitializer_2_1();
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
    Java_annotationTypeBody_1 atb1=`Java_annotationTypeBody_1_2();
}
	:	'{'
        (
            ated=annotationTypeElementDeclarations
            {
                atb1=`Java_annotationTypeBody_1_1(ated);
            }
        )? 
        '}'
        {
            atb=`Java_annotationTypeBody(atb1);
        }
	;
	
annotationTypeElementDeclarations returns [Java_annotationTypeElementDeclarations ated]
@init {
    Java_annotationTypeElementDeclarations_2 tated=`Java_annotationTypeElementDeclarations_2_1();
}
	:	ated1=annotationTypeElementDeclaration 
        (
            ated2=annotationTypeElementDeclaration
            {
                tated=`Java_annotationTypeElementDeclarations_2_1(tated*,ated2);
            }
        )*
        {
            ated=`Java_annotationTypeElementDeclarations(ated1,tated);
        }
	;
	
annotationTypeElementDeclaration returns [Java_annotationTypeElementDeclaration ated]
@init {
    Java_annotationTypeElementDeclaration_1 ated1=`Java_annotationTypeElementDeclaration_1_1();
}
	:	(
            m=modifier
            {
                ated1=`Java_annotationTypeElementDeclaration_1_1(ated1*,m);
            }
        )*
        ater=annotationTypeElementRest
        {
            ated=`Java_annotationTypeElementDeclaration(ated1,ater);
        }
	;
	
annotationTypeElementRest returns [Java_annotationTypeElementRest ater]
	:	t=type i=Identifier amocr=annotationMethodOrConstantRest ';'
        {
            ater=`Java_annotationTypeElementRest_1(Java_annotationTypeElementRest_1_1(t,Java_Identifier(i.getText()),amocr));
        }
	|   cd=classDeclaration
        {
            ater=`Java_annotationTypeElementRest_2(cd);
        }
	|   id=interfaceDeclaration
        {
            ater=`Java_annotationTypeElementRest_3(id);
        }
	|   ed=enumDeclaration
        {
            ater=`Java_annotationTypeElementRest_4(ed);
        }
	|   atd=annotationTypeDeclaration
        {
            ater=`Java_annotationTypeElementRest_5(atd);
        }
	;
	
annotationMethodOrConstantRest returns [Java_annotationMethodOrConstantRest amocr]
	:	amr=annotationMethodRest
        {
            amocr=`Java_annotationMethodOrConstantRest_1(amr);
        }
	|   acr=annotationConstantRest
        {
            amocr=`Java_annotationMethodOrConstantRest_2(acr);
        }
	;
	
annotationMethodRest returns [Java_annotationMethodRest amr]
@init {
    Java_annotationMethodRest_1 amr1=`Java_annotationMethodRest_1_2();
}
 	:	'(' ')' 
        (
            dv=defaultValue
            {
                amr1=`Java_annotationMethodRest_1_1(dv);
            }
        )?
        {
            amr=`Java_annotationMethodRest(amr1);
        }
 	;
 	
annotationConstantRest returns [Java_annotationConstantRest acr]
 	:	vd=variableDeclarators
        {
            acr=`Java_annotationConstantRest(vd);
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
    Java_statement_2_2 ts2=`Java_statement_2_2_2();
    Java_statement_3_3 ts3=`Java_statement_3_3_2();
    Java_statement_7_2 ts7=null;
    Java_statement_10_1 ts10=`Java_statement_10_1_2();
    Java_statement_12_1 ts12=`Java_statement_12_1_2();
    Java_statement_13_1 ts13=`Java_statement_13_1_2();
}
	: 
            b=block
            {
                s=`Java_statement_1(b);
            }
        | 
            'assert' e1=expression 
            (
                ':' e2=expression
                {
                    ts2=`Java_statement_2_2_1(e2);
                }
            )?
            ';'
            {
                s=`Java_statement_2(e1,ts2);
            }
        | 
            'if' pe=parExpression s1=statement 
            (
                'else' s2=statement
                {
                    ts3=`Java_statement_3_3_1(s2);
                }
            )?
            {
                s=`Java_statement_3(pe,s1,ts3);
            }
        | 
            'for' '(' fc=forControl ')' s1=statement
            {
                s=`Java_statement_4(fc,s1);
            }
        |
            'while' pe=parExpression s1=statement
            {
                s=`Java_statement_5(pe,s1);
            }
        |
            'do' s1=statement 'while' pe=parExpression ';'
            {
                s=`Java_statement_6(s1,pe);
            }
        |
            'try' b=block
            (
                    c=catches 'finally' b=block
                    {
                        ts7=`Java_statement_7_2_1(c,b);
                    }
                | 
                    c=catches
                    {
                        ts7=`Java_statement_7_2_2(c);
                    }
                |
                    'finally' b=block
                    {
                        ts7=`Java_statement_7_2_3(b);
                    }
            )
            {
                s=`Java_statement_7(b,ts7);
            }
        |
            'switch' pe=parExpression '{' sbsg=switchBlockStatementGroups '}'
            {
                s=`Java_statement_8(pe,sbsg);
            }
        |
            'synchronized' pe=parExpression b=block
            {
                s=`Java_statement_9(pe,b);
            }
        |
            'return' 
            (
                e=expression
                {
                    ts10=`Java_statement_10_1_1(e);
                }
            )? 
            ';'
            {
                s=`Java_statement_10(ts10);
            }
        |
            'throw' e=expression ';'
            {
                s=`Java_statement_11(e);
            }
        |
            'break'
            (
                i=Identifier
                {
                    ts12=`Java_statement_12_1_1(Java_Identifier(i.getText()));
                }
            )?
            ';'
            {
                s=`Java_statement_12(ts12);
            }
        |
            'continue' 
            (
                i=Identifier
                {
                    ts13=`Java_statement_13_1_1(Java_Identifier(i.getText()));
                }
            )?
            ';'
            {
                s=`Java_statement_13(ts13);
            }
        |
            ';'
            {
                s=`Java_statement_14();
            }
        |
            se=statementExpression ';'
            {
                s=`Java_statement_15(se);
            }
        | 
            i=Identifier ':' s1=statement
            {
                s=`Java_statement_16(Java_Identifier(i.getText()),s1);
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
    Java_formalParameter_1 fp1=`Java_formalParameter_1_1();
}
	:	(
            vm=variableModifier
            {
                fp1=`Java_formalParameter_1_1(fp1*,vm);
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
                sl=`Java_switchLabel_1(ce);
            }
	|
        'case' ecn=enumConstantName ':'
            {
                sl=`Java_switchLabel_2(ecn);
            }
	|   'default' ':'
            {
                sl=`Java_switchLabel_3();
            }
	;
	
moreStatementExpressions returns [Java_moreStatementExpressions mse]
@init {
    mse=`Java_moreStatementExpressions_1();
}
	:	(
            ',' se=statementExpression
            {
                mse=`Java_moreStatementExpressions_1(mse*,Java_moreStatementExpressions_1_1(se));
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

statementExpression returns [Java_statementExpression se]
	:	e=expression
        {
            se=`Java_statementExpression(e);
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
    Java_expression_2 e2=`Java_expression_2_2();
}
	:	ce=conditionalExpression 
        (
            ao=assignmentOperator e1=expression
            {
                e2=`Java_expression_2_1(ao,e1);
            }
        )?
        {
            e=`Java_expression(ce,e2);
        }
	;
	
assignmentOperator returns [Java_assignmentOperator ao]
	:	'='
        {
            ao=`Java_assignmentOperator_1();
        }
    |   '+='
        {
            ao=`Java_assignmentOperator_2();
        }
    |   '-='
        {
            ao=`Java_assignmentOperator_3();
        }
    |   '*='
        {
            ao=`Java_assignmentOperator_4();
        }
    |   '/='
        {
            ao=`Java_assignmentOperator_5();
        }
    |   '&='
        {
            ao=`Java_assignmentOperator_6();
        }
    |   '|='
        {
            ao=`Java_assignmentOperator_7();
        }
    |   '^='
        {
            ao=`Java_assignmentOperator_8();
        }
    |   '%='
        {
            ao=`Java_assignmentOperator_9();
        }
    |   '<' '<' '='
        {
            ao=`Java_assignmentOperator_10();
        }
    |   '>' '>' '='
        {
            ao=`Java_assignmentOperator_11();
        }
    |   '>' '>' '>' '='
        {
            ao=`Java_assignmentOperator_12();
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
    Java_conditionalOrExpression_2 coe2=`Java_conditionalOrExpression_2_1();
}
    :   cae1=conditionalAndExpression 
        ( 
            '||' cae2=conditionalAndExpression 
            {
                coe2=`Java_conditionalOrExpression_2_1(coe2*,Java_conditionalOrExpression_2_1_1(cae2));
            }
        )*
        {
            coe=`Java_conditionalOrExpression(cae1,coe2);
        }
	;

conditionalAndExpression returns [Java_conditionalAndExpression cae]
@init {
    Java_conditionalAndExpression_2 cae2=`Java_conditionalAndExpression_2_1();
}
    :   ioe1=inclusiveOrExpression 
        (
            '&&' ioe2=inclusiveOrExpression 
            {
                cae2=`Java_conditionalAndExpression_2_1(cae2*,Java_conditionalAndExpression_2_1_1(ioe2));
            }
        )*
        {
            cae=`Java_conditionalAndExpression(ioe1,cae2);
        }
	;

inclusiveOrExpression returns [Java_inclusiveOrExpression ioe]
@init {
    Java_inclusiveOrExpression_2 ioe2=`Java_inclusiveOrExpression_2_1();
}
    :   eoe1=exclusiveOrExpression 
        (
            '|' eoe2=exclusiveOrExpression 
            {
                ioe2=`Java_inclusiveOrExpression_2_1(ioe2*,Java_inclusiveOrExpression_2_1_1(eoe2));
            }
        )*
        {
            ioe=`Java_inclusiveOrExpression(eoe1,ioe2);
        }
	;

exclusiveOrExpression returns [Java_exclusiveOrExpression eoe]
@init {
    Java_exclusiveOrExpression_2 eoe2=`Java_exclusiveOrExpression_2_1();
}
    :   ae1=andExpression
        (
            '^' ae2=andExpression 
            {
                eoe2=`Java_exclusiveOrExpression_2_1(eoe2*,Java_exclusiveOrExpression_2_1_1(ae2));
            }
        )*
        {
            eoe=`Java_exclusiveOrExpression(ae1,eoe2);
        }
	;

andExpression returns [Java_andExpression ae]
@init {
    Java_andExpression_2 ae2=`Java_andExpression_2_1();
}
    :   ee1=equalityExpression 
        (
            '&' ee2=equalityExpression 
            {
                ae2=`Java_andExpression_2_1(ae2*,Java_andExpression_2_1_1(ee2));
            }
        )*
        {
            ae=`Java_andExpression(ee1,ae2);
        }
	;

equalityExpression returns [Java_equalityExpression ee]
@init {
    Java_equalityExpression_2 ee2=`Java_equalityExpression_2_1();
    Java_equalityExpression_2_1_1_1 ee3=null;
}
    :   ioe1=instanceOfExpression 
        (
            (
                    '=='
                    {
                        ee3=`Java_equalityExpression_2_1_1_1_1();
                    }
                |
                    '!='
                    {
                        ee3=`Java_equalityExpression_2_1_1_1_2();
                    }
            ) 
            ioe2=instanceOfExpression
            {
                ee2=`Java_equalityExpression_2_1(ee2*,Java_equalityExpression_2_1_1(ee3,ioe2));
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
    Java_relationalExpression_2 re2=`Java_relationalExpression_2_1();
}
    :   se1=shiftExpression 
        ( 
            ro=relationalOp se2=shiftExpression 
            {
                re2=`Java_relationalExpression_2_1(re2*,Java_relationalExpression_2_1_1(ro,se2));
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
                ro=`Java_relationalOp_1();
            }
            | '>' '=' 
            {
                ro=`Java_relationalOp_2();
            }
            | '<' 
            {
                ro=`Java_relationalOp_3();
            }
            | '>'
            {
                ro=`Java_relationalOp_4();
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
                so=`Java_shiftOp_1();
            }
            | '>' '>' '>'
            {
                so=`Java_shiftOp_2();
            }
            | '>' '>'
            {
                so=`Java_shiftOp_3();
            }
        )
	;


additiveExpression returns [Java_additiveExpression ae]
@init {
    Java_additiveExpression_2 ae2=`Java_additiveExpression_2_1();
    Java_additiveExpression_2_1_1_1 ae3=null;
}
    :   me1=multiplicativeExpression 
        ( 
            (
                    '+' 
                    {
                        ae3=`Java_additiveExpression_2_1_1_1_1();
                    }
                | 
                    '-'
                    {
                        ae3=`Java_additiveExpression_2_1_1_1_2();
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
    Java_multiplicativeExpression_2_1_1_1 me3=null;
}
    :   ue1=unaryExpression 
        (
            ( 
                    '*' 
                    {
                        me3=`Java_multiplicativeExpression_2_1_1_1_1();
                    }
                | 
                    '/' 
                    {
                        me3=`Java_multiplicativeExpression_2_1_1_1_2();
                    }
                | 
                    '%' 
                    {
                        me3=`Java_multiplicativeExpression_2_1_1_1_3();
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
    Java_primary_8_2 p8=`Java_primary_8_2_1();
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
                    p8=`Java_primary_8_2_1(p8*,Java_primary_8_2_1_1());
                }
            )*
            '.' 'class'
            {
                p=`Java_primary_8(pt,p8);
            }
        |
            'void' '.' 'class'
            {
                p=`Java_primary_9();
            }
	;

identifierSuffix returns [Java_identifierSuffix is]
@init {
    Java_identifierSuffix_1_1 is1=`Java_identifierSuffix_1_1_1();
    Java_identifierSuffix_2_1 is2=`Java_identifierSuffix_2_1_1();
    Java_identifierSuffix_8_1 is8=`Java_identifierSuffix_8_1_2();
}
	:	    (
                '[' ']'
                {
                    is1=`Java_identifierSuffix_1_1_1(is1*,Java_identifierSuffix_1_1_1_1());
                }
            )+ 
            '.' 'class'
            {
                is=`Java_identifierSuffix_1(is1);
            }
	    |	
            (
                '[' e=expression ']'
                {
                    is2=`Java_identifierSuffix_2_1_1(is2*,Java_identifierSuffix_2_1_1_1(e));
                }
            )+ // can also be matched by selector, but do here
            {
                is=`Java_identifierSuffix_2(is2);
            }
        |   
            a=arguments
            {
                is=`Java_identifierSuffix_3(a);
            }
        |
            '.' 'class'
            {
                is=`Java_identifierSuffix_4();
            }
        |
            '.' egi=explicitGenericInvocation
            {
                is=`Java_identifierSuffix_5(egi);
            }
        |
            '.' 'this'
            {
                is=`Java_identifierSuffix_6();
            }
        |
            '.' 'super' a=arguments
            {
                is=`Java_identifierSuffix_7(a);
            }
        |
            '.' 'new' 
            (
                nwta=nonWildcardTypeArguments
                {
                    is8=`Java_identifierSuffix_8_1_1(nwta);
                }
            )?
            ic=innerCreator
            {
                is=`Java_identifierSuffix_8(is8,ic);
            }
	;

creator returns [Java_creator c]
@init {
    Java_creator_1 c1=`Java_creator_1_2();
    Java_creator_3 c3=null;
}
	:	(
            nwta=nonWildcardTypeArguments
            {
                c1=`Java_creator_1_1(nwta);
            }
        )?
        cn=createdName
        (
                acr=arrayCreatorRest 
                {
                    c3=`Java_creator_3_1(acr);
                }
            |
                ccr=classCreatorRest
                {
                    c3=`Java_creator_3_2(ccr);
                }
        )
        {
            c=`Java_creator(c1,cn,c3);
        }
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

arrayCreatorRest returns [Java_arrayCreatorRest acr]
@init {
    Java_arrayCreatorRest_1_1 acr1=`Java_arrayCreatorRest_1_1_1();
    Java_arrayCreatorRest_2_2 acr2=`Java_arrayCreatorRest_2_2_1();
    Java_arrayCreatorRest_2_3 acr3=`Java_arrayCreatorRest_2_3_1();
}
	:	'['
        (
                ']' 
                (
                    '[' ']' 
                    {
                        acr1=`Java_arrayCreatorRest_1_1_1(acr1*,Java_arrayCreatorRest_1_1_1_1()); 
                    } 
                )*
                a=arrayInitializer
                {
                    acr=`Java_arrayCreatorRest_1(acr1,a);
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
                        acr3=`Java_arrayCreatorRest_2_3_1(acr3*,Java_arrayCreatorRest_2_3_1_1());
                    }
                )*
                {
                    acr=`Java_arrayCreatorRest_2(e,acr2,acr3);
                }
        )
	;

classCreatorRest returns [Java_classCreatorRest ccr]
@init {
    Java_classCreatorRest_2 ccr2=`Java_classCreatorRest_2_2();
}
	:	a=arguments 
        (
            cb=classBody
            {
                ccr2=`Java_classCreatorRest_2_1(cb);
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
                egis=`Java_explicitGenericInvocationSuffix_1(s);
            }
	    |
            i=Identifier a=arguments
            {
                egis=`Java_explicitGenericInvocationSuffix_2(Java_Identifier(i.getText()),a);
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
