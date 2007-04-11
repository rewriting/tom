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
compilationUnit returns [Gomantlr_Java_compilationUnit cu]
@init {
    Gomantlr_Java_compilationUnit_1 cu1=`Gomantlr_Java_compilationUnit_1_2();
    Gomantlr_Java_compilationUnit_2 cu2=`Gomantlr_Java_compilationUnit_2_2();
    Gomantlr_Java_compilationUnit_3 cu3=`Gomantlr_Java_compilationUnit_3_1();
    Gomantlr_Java_compilationUnit_4 cu4=`Gomantlr_Java_compilationUnit_4_1();
}
	:	(
            a=annotations
            {
                cu1=`Gomantlr_Java_compilationUnit_1_1(a);
            }
        )?
		(
            pd=packageDeclaration
            {
                cu2=`Gomantlr_Java_compilationUnit_2_1(pd);
            }
        )?
        (
            id=importDeclaration
            {
                cu3=`Gomantlr_Java_compilationUnit_3_1(cu3*,id);
            }
        )*
        (
            td=typeDeclaration
            {
                cu4=`Gomantlr_Java_compilationUnit_4_1(cu4*,td);
            }
        )*
        {
            cu=`Gomantlr_Java_compilationUnit(cu1,cu2,cu3,cu4);
        }
	;

packageDeclaration returns [Gomantlr_Java_packageDeclaration pd]
	:	'package' qn=qualifiedName ';'
        {
            pd=`Gomantlr_Java_packageDeclaration(qn);
        }
	;
	
importDeclaration returns [Gomantlr_Java_importDeclaration id]
@init {
    Gomantlr_Java_importDeclaration_1 id1=`Gomantlr_Java_importDeclaration_1_2();
    Gomantlr_Java_importDeclaration_3 id3=`Gomantlr_Java_importDeclaration_3_1();
    Gomantlr_Java_importDeclaration_4 id4=`Gomantlr_Java_importDeclaration_4_2();
}
	:	'import' 
        (
            'static'
            {
                id1=`Gomantlr_Java_importDeclaration_1_1();
            }
        )? 
        i1=Identifier 
        (
            '.' i2=Identifier
            {
                id3=`Gomantlr_Java_importDeclaration_3_1(id3*,Gomantlr_Java_importDeclaration_3_1_1(Gomantlr_Java_Identifier(i2.getText())));
            }
        )* 
        (
            '.' '*'
            {
                id4=`Gomantlr_Java_importDeclaration_4_1();
            }
        )?
        ';'
        {
            id=`Gomantlr_Java_importDeclaration(id1,Gomantlr_Java_Identifier(i1.getText()),id3,id4);
        }
	;
	
typeDeclaration returns [Gomantlr_Java_typeDeclaration td]
	:	coid=classOrInterfaceDeclaration
        {
            td=`Gomantlr_Java_typeDeclaration_1(coid);
        }
    |   ';'
        {
            td=`Gomantlr_Java_typeDeclaration_2();
        }
	;
	
classOrInterfaceDeclaration returns [Gomantlr_Java_classOrInterfaceDeclaration coid]
@init {
    Gomantlr_Java_classOrInterfaceDeclaration_1 coid1=`Gomantlr_Java_classOrInterfaceDeclaration_1_1();
    Gomantlr_Java_classOrInterfaceDeclaration_2 coid2=null;
}
	:	
        (
            m=modifier
            {
                coid1=`Gomantlr_Java_classOrInterfaceDeclaration_1_1(coid1*,m);
            }
        )* 
        ( 
                cd=classDeclaration
                {
                    coid2=`Gomantlr_Java_classOrInterfaceDeclaration_2_1(cd);
                }
            | 
                id=interfaceDeclaration
                {
                    coid2=`Gomantlr_Java_classOrInterfaceDeclaration_2_2(id);
                }
        )
        {
            coid=`Gomantlr_Java_classOrInterfaceDeclaration(coid1,coid2);
        }
	;
	
classDeclaration returns [Gomantlr_Java_classDeclaration cd]
	:	ncd=normalClassDeclaration
        {
            cd=`Gomantlr_Java_classDeclaration_1(ncd);
        }
    |   ed=enumDeclaration
        {
            cd=`Gomantlr_Java_classDeclaration_2(ed);
        }
	;
	
normalClassDeclaration returns [Gomantlr_Java_normalClassDeclaration ncd]
@init {
    Gomantlr_Java_normalClassDeclaration_2 ncd2=`Gomantlr_Java_normalClassDeclaration_2_2();
    Gomantlr_Java_normalClassDeclaration_3 ncd3=`Gomantlr_Java_normalClassDeclaration_3_2();
    Gomantlr_Java_normalClassDeclaration_4 ncd4=`Gomantlr_Java_normalClassDeclaration_4_2();
}
	:	'class' i=Identifier
        (
            tp=typeParameters
            {
                ncd2=`Gomantlr_Java_normalClassDeclaration_2_1(tp);
            }
        )?
        (
            'extends' t=type
            {
                ncd3=`Gomantlr_Java_normalClassDeclaration_3_1(t);
            }
        )?
        (
            'implements' tl=typeList
            {
                ncd4=`Gomantlr_Java_normalClassDeclaration_4_1(tl);
            }
        )?
        cb=classBody
        {
            ncd=`Gomantlr_Java_normalClassDeclaration(Gomantlr_Java_Identifier(i.getText()),ncd2,ncd3,ncd4,cb);
        }
	;
	
typeParameters returns [Gomantlr_Java_typeParameters tp]
@init {
    Gomantlr_Java_typeParameters_2 ttp=`Gomantlr_Java_typeParameters_2_1();
}
	:	'<' tp1=typeParameter 
        (
            ',' tp2=typeParameter
            {
                ttp=`Gomantlr_Java_typeParameters_2_1(ttp*,Gomantlr_Java_typeParameters_2_1_1(tp2));
            }
        )*
        '>'
        {
            tp=`Gomantlr_Java_typeParameters(tp1,ttp);
        }
	;

typeParameter returns [Gomantlr_Java_typeParameter tp]
@init {
    Gomantlr_Java_typeParameter_2 tp2=`Gomantlr_Java_typeParameter_2_2();
}
	:	i=Identifier 
        (
            'extends' b=bound
            {
                tp2=`Gomantlr_Java_typeParameter_2_1(b);
            }
        )?
        {
            tp=`Gomantlr_Java_typeParameter(Gomantlr_Java_Identifier(i.getText()),tp2);
        }
	;
		
bound returns [Gomantlr_Java_bound b]
@init {
    Gomantlr_Java_bound_2 b2=`Gomantlr_Java_bound_2_1();
}
	:	t1=type
        (
            '&' t2=type
            {
                b2=`Gomantlr_Java_bound_2_1(b2*,Gomantlr_Java_bound_2_1_1(t2));
            }
        )*
        {
            b=`Gomantlr_Java_bound(t1,b2);
        }
	;

enumDeclaration returns [Gomantlr_Java_enumDeclaration ed]
@init {
    Gomantlr_Java_enumDeclaration_3 ed3=`Gomantlr_Java_enumDeclaration_3_2();
}
	:	e=ENUM i=Identifier 
        (
            'implements' tl=typeList
            {
                ed3=`Gomantlr_Java_enumDeclaration_3_1(tl);
            }
        )?
        eb=enumBody
        {
            ed=`Gomantlr_Java_enumDeclaration(Gomantlr_Java_ENUM(e.getText()),Gomantlr_Java_Identifier(i.getText()),ed3,eb);
        }
	;
	
enumBody returns [Gomantlr_Java_enumBody eb]
@init {
    Gomantlr_Java_enumBody_1 eb1=`Gomantlr_Java_enumBody_1_2();
    Gomantlr_Java_enumBody_2 eb2=`Gomantlr_Java_enumBody_2_2();
    Gomantlr_Java_enumBody_3 eb3=`Gomantlr_Java_enumBody_3_2();
}
	:	'{'
        (
            ec=enumConstants
            {
                eb1=`Gomantlr_Java_enumBody_1_1(ec);
            }
        )? 
        (
            ','
            {
                eb2=`Gomantlr_Java_enumBody_2_1();
            }
        )? 
        (
            ebd=enumBodyDeclarations
            {
                eb3=`Gomantlr_Java_enumBody_3_1(ebd);
            }
        )?
        '}'
        {
            eb=`Gomantlr_Java_enumBody(eb1,eb2,eb3);
        }
	;

enumConstants returns [Gomantlr_Java_enumConstants ec]
@init {
    Gomantlr_Java_enumConstants_2 tec=`Gomantlr_Java_enumConstants_2_1();
}
	:	ec1=enumConstant 
        (
            ',' ec2=enumConstant
            {
                tec=`Gomantlr_Java_enumConstants_2_1(tec*,Gomantlr_Java_enumConstants_2_1_1(ec2));
            }
        )*
        {
            ec=`Gomantlr_Java_enumConstants(ec1,tec);
        }
	;
	
enumConstant returns [Gomantlr_Java_enumConstant ec]
@init {
    Gomantlr_Java_enumConstant_1 ec1=`Gomantlr_Java_enumConstant_1_2();
    Gomantlr_Java_enumConstant_3 ec3=`Gomantlr_Java_enumConstant_3_2();
    Gomantlr_Java_enumConstant_4 ec4=`Gomantlr_Java_enumConstant_4_2();
}
	:	(
            an=annotations
            {
                ec1=`Gomantlr_Java_enumConstant_1_1(an);
            }
        )? 
        i=Identifier 
        (
            ar=arguments
            {
                ec3=`Gomantlr_Java_enumConstant_3_1(ar);
            }
        )?
        (
            cb=classBody
            {
                ec4=`Gomantlr_Java_enumConstant_4_1(cb);
            }
        )?
        {
            ec=`Gomantlr_Java_enumConstant(ec1,Gomantlr_Java_Identifier(i.getText()),ec3,ec4);
        }
	;
	
enumBodyDeclarations returns [Gomantlr_Java_enumBodyDeclarations ebd]
@init {
    Gomantlr_Java_enumBodyDeclarations_1 ebd1=`Gomantlr_Java_enumBodyDeclarations_1_1();
}
	:	';' 
        (
            cbd=classBodyDeclaration
            {
                ebd1=`Gomantlr_Java_enumBodyDeclarations_1_1(ebd1*,cbd);
            }
        )*
        {
            ebd=`Gomantlr_Java_enumBodyDeclarations(ebd1);
        }
	;
	
interfaceDeclaration returns [Gomantlr_Java_interfaceDeclaration id]
	:   nid=normalInterfaceDeclaration
        {
            id=`Gomantlr_Java_interfaceDeclaration_1(nid);
        }
    |   atd=annotationTypeDeclaration
        {
            id=`Gomantlr_Java_interfaceDeclaration_2(atd);
        }
	;
	
normalInterfaceDeclaration returns [Gomantlr_Java_normalInterfaceDeclaration nid]
@init {
    Gomantlr_Java_normalInterfaceDeclaration_2 nid2=`Gomantlr_Java_normalInterfaceDeclaration_2_2();
    Gomantlr_Java_normalInterfaceDeclaration_3 nid3=`Gomantlr_Java_normalInterfaceDeclaration_3_2();
}
	:	'interface' i=Identifier 
        (
            tp=typeParameters
            {
                nid2=`Gomantlr_Java_normalInterfaceDeclaration_2_1(tp);
            }
        )?
        (
            'extends' tl=typeList
            {
                nid3=`Gomantlr_Java_normalInterfaceDeclaration_3_1(tl);
            }
        )?
        ib=interfaceBody
        {
            nid=`Gomantlr_Java_normalInterfaceDeclaration(Gomantlr_Java_Identifier(i.getText()),nid2,nid3,ib);
        }
	;
	
typeList returns [Gomantlr_Java_typeList tl]
@init {
    Gomantlr_Java_typeList_2 tl2=`Gomantlr_Java_typeList_2_1();
}
	:	t1=type 
        (
            ',' t2=type
            {
                tl2=`Gomantlr_Java_typeList_2_1(tl2*,Gomantlr_Java_typeList_2_1_1(t2));
            }
        )*
        {
            tl=`Gomantlr_Java_typeList(t1,tl2);
        }
	;
	
classBody returns [Gomantlr_Java_classBody cb]
@init {
    Gomantlr_Java_classBody_1 cb1=`Gomantlr_Java_classBody_1_1();
}
	:	'{' 
        (
            cbd=classBodyDeclaration
            {
                cb1=`Gomantlr_Java_classBody_1_1(cb1*,cbd);
            }
        )*
        '}'
        {
            cb=`Gomantlr_Java_classBody(cb1);
        }
	;
	
interfaceBody returns [Gomantlr_Java_interfaceBody ib]
@init {
    Gomantlr_Java_interfaceBody_1 ib1=`Gomantlr_Java_interfaceBody_1_1();
}
	:	'{' 
        (
            ibd=interfaceBodyDeclaration
            {
                ib1=`Gomantlr_Java_interfaceBody_1_1(ib1*,ibd);
            }
        )*
        '}'
        {
            ib=`Gomantlr_Java_interfaceBody(ib1);
        }
	;

classBodyDeclaration returns [Gomantlr_Java_classBodyDeclaration ebd]
@init {
    Gomantlr_Java_classBodyDeclaration_2_1 ebd2=`Gomantlr_Java_classBodyDeclaration_2_1_2();
    Gomantlr_Java_classBodyDeclaration_3_1 ebd3=`Gomantlr_Java_classBodyDeclaration_3_1_1();
}
	:	    ';'
            {
                ebd=`Gomantlr_Java_classBodyDeclaration_1();
            }
	    |
            (
                'static'
                {
                    ebd2=`Gomantlr_Java_classBodyDeclaration_2_1_1();
                }
            )?
            b=block
            {
                ebd=`Gomantlr_Java_classBodyDeclaration_2(ebd2,b);
            }
	    |
            (
                m=modifier
                {
                    ebd3=`Gomantlr_Java_classBodyDeclaration_3_1_1(ebd3*,m);
                }
            )*
            md=memberDecl
            {
                ebd=`Gomantlr_Java_classBodyDeclaration_3(ebd3,md);
            }
	;
	
memberDecl returns [Gomantlr_Java_memberDecl md]
	:	gmocd=genericMethodOrConstructorDecl
        {
            md=`Gomantlr_Java_memberDecl_1(gmocd);
        }
	|	md1=methodDeclaration
        {
            md=`Gomantlr_Java_memberDecl_2(md1);
        }
	|	fd=fieldDeclaration
        {
            md=`Gomantlr_Java_memberDecl_3(fd);
        }
	|	'void' i=Identifier vmdr=voidMethodDeclaratorRest
        {
            md=`Gomantlr_Java_memberDecl_4(Gomantlr_Java_Identifier(i.getText()),vmdr);
        }
	|	i=Identifier cdr=constructorDeclaratorRest
        {
            md=`Gomantlr_Java_memberDecl_5(Gomantlr_Java_Identifier(i.getText()),cdr);
        }
	|	id=interfaceDeclaration
        {
            md=`Gomantlr_Java_memberDecl_6(id);
        }
	|	cd=classDeclaration
        {
            md=`Gomantlr_Java_memberDecl_7(cd);
        }
	;
	
genericMethodOrConstructorDecl returns [Gomantlr_Java_genericMethodOrConstructorDecl gmocd]
	:	tp=typeParameters gmocr=genericMethodOrConstructorRest
        {
            gmocd=`Gomantlr_Java_genericMethodOrConstructorDecl(tp,gmocr);
        }
	;
	
genericMethodOrConstructorRest returns [Gomantlr_Java_genericMethodOrConstructorRest gmocr]
@init {
    Gomantlr_Java_genericMethodOrConstructorRest_1_1 gmocr1=null;
}
	:	(
                t=type 
                {
                    gmocr1=`Gomantlr_Java_genericMethodOrConstructorRest_1_1_1(t);
                }
            |
                'void'
                {
                    gmocr1=`Gomantlr_Java_genericMethodOrConstructorRest_1_1_2();
                }
        )
        i=Identifier mdr=methodDeclaratorRest
        {
            gmocr=`Gomantlr_Java_genericMethodOrConstructorRest_1(gmocr1,Gomantlr_Java_Identifier(i.getText()),mdr);
        }
	|	i=Identifier cdr=constructorDeclaratorRest
        {
            gmocr=`Gomantlr_Java_genericMethodOrConstructorRest_2(Gomantlr_Java_Identifier(i.getText()),cdr);
        }
	;

methodDeclaration returns [Gomantlr_Java_methodDeclaration md]
	:	t=type i=Identifier mdr=methodDeclaratorRest
        {
            md=`Gomantlr_Java_methodDeclaration(t,Gomantlr_Java_Identifier(i.getText()),mdr);
        }
	;

fieldDeclaration returns [Gomantlr_Java_fieldDeclaration fd]
	:	t=type vd=variableDeclarators ';'
        {
            fd=`Gomantlr_Java_fieldDeclaration(t,vd);
        }
	;
		
interfaceBodyDeclaration returns [Gomantlr_Java_interfaceBodyDeclaration ibd]
@init {
    Gomantlr_Java_interfaceBodyDeclaration_1_1 ibd1=`Gomantlr_Java_interfaceBodyDeclaration_1_1_1();
}
    :       (
                m=modifier
                {
                    ibd1=`Gomantlr_Java_interfaceBodyDeclaration_1_1_1(ibd1*,m);
                }
            )* 
            imd=interfaceMemberDecl
            {
                ibd=`Gomantlr_Java_interfaceBodyDeclaration_1(ibd1,imd);
            }
	    |
            ';'
            {
                ibd=`Gomantlr_Java_interfaceBodyDeclaration_2();
            }
	;

interfaceMemberDecl returns [Gomantlr_Java_interfaceMemberDecl imd]
	:	imofd=interfaceMethodOrFieldDecl
        {
            imd=`Gomantlr_Java_interfaceMemberDecl_1(imofd);
        }
	|   igmd=interfaceGenericMethodDecl
        {
            imd=`Gomantlr_Java_interfaceMemberDecl_2(igmd);
        }
    |   'void' i=Identifier vimdr=voidInterfaceMethodDeclaratorRest
        {
            imd=`Gomantlr_Java_interfaceMemberDecl_3(Gomantlr_Java_Identifier(i.getText()),vimdr);
        }
    |   id=interfaceDeclaration
        {
            imd=`Gomantlr_Java_interfaceMemberDecl_4(id);
        }
    |   cd=classDeclaration
        {
            imd=`Gomantlr_Java_interfaceMemberDecl_5(cd);
        }
	;
	
interfaceMethodOrFieldDecl returns [Gomantlr_Java_interfaceMethodOrFieldDecl imofd]
	:	t=type i=Identifier imofr=interfaceMethodOrFieldRest
        {
            imofd=`Gomantlr_Java_interfaceMethodOrFieldDecl(t,Gomantlr_Java_Identifier(i.getText()),imofr);
        }
	;
	
interfaceMethodOrFieldRest returns [Gomantlr_Java_interfaceMethodOrFieldRest imofr]
	:	cdr=constantDeclaratorsRest ';'
        {
            imofr=`Gomantlr_Java_interfaceMethodOrFieldRest_1(cdr);
        }
	|	imdr=interfaceMethodDeclaratorRest
        {
            imofr=`Gomantlr_Java_interfaceMethodOrFieldRest_2(imdr);
        }
	;
	
methodDeclaratorRest returns [Gomantlr_Java_methodDeclaratorRest mdr]
@init {
    Gomantlr_Java_methodDeclaratorRest_2 mdr2=`Gomantlr_Java_methodDeclaratorRest_2_1();
    Gomantlr_Java_methodDeclaratorRest_3 mdr3=`Gomantlr_Java_methodDeclaratorRest_3_2();
    Gomantlr_Java_methodDeclaratorRest_4 mdr4=null;
}
	:	fp=formalParameters 
        (
            '[' ']'
            {
                mdr2=`Gomantlr_Java_methodDeclaratorRest_2_1(mdr2*,Gomantlr_Java_methodDeclaratorRest_2_1_1());
            }
        )*
        (
            'throws' qnl=qualifiedNameList
            {
                mdr3=`Gomantlr_Java_methodDeclaratorRest_3_1(qnl);
            }
        )?
        (
                mb=methodBody
                {
                    mdr4=`Gomantlr_Java_methodDeclaratorRest_4_1(mb);
                }
            |
                ';'
                {
                    mdr4=`Gomantlr_Java_methodDeclaratorRest_4_2();
                }
        )
        {
            mdr=`Gomantlr_Java_methodDeclaratorRest(fp,mdr2,mdr3,mdr4);
        }
	;
	
voidMethodDeclaratorRest returns [Gomantlr_Java_voidMethodDeclaratorRest vmd]
@init {
    Gomantlr_Java_voidMethodDeclaratorRest_2 vmd2=`Gomantlr_Java_voidMethodDeclaratorRest_2_2();
    Gomantlr_Java_voidMethodDeclaratorRest_3 vmd3=null;
}
	:	fp=formalParameters 
        (
            'throws' qnl=qualifiedNameList
            {
                vmd2=`Gomantlr_Java_voidMethodDeclaratorRest_2_1(qnl);
            }
        )?
        (
                mb=methodBody
                {
                    vmd3=`Gomantlr_Java_voidMethodDeclaratorRest_3_1(mb);
                }
            |
                ';'
                {
                    vmd3=`Gomantlr_Java_voidMethodDeclaratorRest_3_2();
                }
        )
        {
            vmd=`Gomantlr_Java_voidMethodDeclaratorRest(fp,vmd2,vmd3);
        }
	;
	
interfaceMethodDeclaratorRest returns [Gomantlr_Java_interfaceMethodDeclaratorRest imdr]
@init {
    Gomantlr_Java_interfaceMethodDeclaratorRest_2 imdr2=`Gomantlr_Java_interfaceMethodDeclaratorRest_2_1();
    Gomantlr_Java_interfaceMethodDeclaratorRest_3 imdr3=`Gomantlr_Java_interfaceMethodDeclaratorRest_3_2();
}
	:	fp=formalParameters 
        (
            '[' ']'
            {
                imdr2=`Gomantlr_Java_interfaceMethodDeclaratorRest_2_1(imdr2*,Gomantlr_Java_interfaceMethodDeclaratorRest_2_1_1());
            }
            
        )* 
        (
            'throws' qnl=qualifiedNameList
            {
                imdr3=`Gomantlr_Java_interfaceMethodDeclaratorRest_3_1(qnl);
            }
        )?
        ';'
        {
            imdr=`Gomantlr_Java_interfaceMethodDeclaratorRest(fp,imdr2,imdr3);
        }
	;
	
interfaceGenericMethodDecl returns [Gomantlr_Java_interfaceGenericMethodDecl igmd]
@init {
    Gomantlr_Java_interfaceGenericMethodDecl_2 igmd2=null;
}
	:	tp=typeParameters 
        (
                t=type 
                {
                    igmd2=`Gomantlr_Java_interfaceGenericMethodDecl_2_1(t);
                }
            |
                'void'
                {
                    igmd2=`Gomantlr_Java_interfaceGenericMethodDecl_2_2();
                }
        ) 
        i=Identifier
        imdr=interfaceMethodDeclaratorRest
        {
            igmd=`Gomantlr_Java_interfaceGenericMethodDecl(tp,igmd2,Gomantlr_Java_Identifier(i.getText()),imdr);
        }
	;
	
voidInterfaceMethodDeclaratorRest returns [Gomantlr_Java_voidInterfaceMethodDeclaratorRest vimd]
@init {
    Gomantlr_Java_voidInterfaceMethodDeclaratorRest_2 vimd2=`Gomantlr_Java_voidInterfaceMethodDeclaratorRest_2_2();
}
	:	fp=formalParameters 
        (
            'throws' qnl=qualifiedNameList
            {
                vimd2=`Gomantlr_Java_voidInterfaceMethodDeclaratorRest_2_1(qnl);
            }
        )? 
        ';'
        {
            vimd=`Gomantlr_Java_voidInterfaceMethodDeclaratorRest(fp,vimd2);
        }
	;
	
constructorDeclaratorRest returns [Gomantlr_Java_constructorDeclaratorRest cdr]
@init {
    Gomantlr_Java_constructorDeclaratorRest_2 cdr2=`Gomantlr_Java_constructorDeclaratorRest_2_2();
}
	:	fp=formalParameters 
        (
            'throws' qnl=qualifiedNameList
            {
                cdr2=`Gomantlr_Java_constructorDeclaratorRest_2_1(qnl);
            }
        )? 
        mb=methodBody
        {
            cdr=`Gomantlr_Java_constructorDeclaratorRest(fp,cdr2,mb);
        }
	;

constantDeclarator returns [Gomantlr_Java_constantDeclarator cd]
	:	i=Identifier cdr=constantDeclaratorRest
        {
            cd=`Gomantlr_Java_constantDeclarator(Gomantlr_Java_Identifier(i.getText()),cdr);
        }
	;
	
variableDeclarators returns [Gomantlr_Java_variableDeclarators vd]
@init {
    Gomantlr_Java_variableDeclarators_2 tvd=`Gomantlr_Java_variableDeclarators_2_1();
}
	:	vd1=variableDeclarator 
        (
            ',' vd2=variableDeclarator
            {
                tvd=`Gomantlr_Java_variableDeclarators_2_1(tvd*,Gomantlr_Java_variableDeclarators_2_1_1(vd2));
            }
        )*
        {
            vd=`Gomantlr_Java_variableDeclarators(vd1,tvd);
        }
	;

variableDeclarator returns [Gomantlr_Java_variableDeclarator vd]
	:	i=Identifier vdr=variableDeclaratorRest
        {
            vd=`Gomantlr_Java_variableDeclarator(Gomantlr_Java_Identifier(i.getText()),vdr);
        }
	;
	
variableDeclaratorRest returns [Gomantlr_Java_variableDeclaratorRest vdr]
@init {
    Gomantlr_Java_variableDeclaratorRest_1_1 vdr1=`Gomantlr_Java_variableDeclaratorRest_1_1_1();
    Gomantlr_Java_variableDeclaratorRest_1_2 vdr2=`Gomantlr_Java_variableDeclaratorRest_1_2_2();
}
	:	(
            '[' ']'
            {
                vdr1=`Gomantlr_Java_variableDeclaratorRest_1_1_1(vdr1*,Gomantlr_Java_variableDeclaratorRest_1_1_1_1());
            }
        )+ 
        (
            '=' vi=variableInitializer
            {
                vdr2=`Gomantlr_Java_variableDeclaratorRest_1_2_1(vi);
            }
        )?
        {
            vdr=`Gomantlr_Java_variableDeclaratorRest_1(vdr1,vdr2);
        }
	|	'=' vi=variableInitializer
        {
            vdr=`Gomantlr_Java_variableDeclaratorRest_2(vi);
        }
	|
        {
            vdr=`Gomantlr_Java_variableDeclaratorRest_3();
        }
	;
	
constantDeclaratorsRest returns [Gomantlr_Java_constantDeclaratorsRest cdr]@init {
    Gomantlr_Java_constantDeclaratorsRest_2 tcdr=`Gomantlr_Java_constantDeclaratorsRest_2_1();
}
    :   cdr1=constantDeclaratorRest 
        (
            ',' cd=constantDeclarator
            {
                tcdr=`Gomantlr_Java_constantDeclaratorsRest_2_1(tcdr*,Gomantlr_Java_constantDeclaratorsRest_2_1_1(cd));
            }
        )*
        {
            cdr=`Gomantlr_Java_constantDeclaratorsRest(cdr1,tcdr);
        }
    ;

constantDeclaratorRest returns [Gomantlr_Java_constantDeclaratorRest cdr]
@init {
    Gomantlr_Java_constantDeclaratorRest_1 cdr1=`Gomantlr_Java_constantDeclaratorRest_1_1();
}
	:	(
            '[' ']'
            {
                cdr1=`Gomantlr_Java_constantDeclaratorRest_1_1(cdr1*,Gomantlr_Java_constantDeclaratorRest_1_1_1());
            }
        )* 
        '=' vi=variableInitializer
        {
            cdr=`Gomantlr_Java_constantDeclaratorRest(cdr1,vi);
        }
	;
	
variableDeclaratorId returns [Gomantlr_Java_variableDeclaratorId vdi]
@init {
    Gomantlr_Java_variableDeclaratorId_2 vdi2=`Gomantlr_Java_variableDeclaratorId_2_1();
}
	:	i=Identifier 
        (
            '[' ']'
            {
                vdi2=`Gomantlr_Java_variableDeclaratorId_2_1(vdi2*,Gomantlr_Java_variableDeclaratorId_2_1_1());
            }
        )*
        {
            vdi=`Gomantlr_Java_variableDeclaratorId(Gomantlr_Java_Identifier(i.getText()),vdi2);
        }
	;

variableInitializer returns [Gomantlr_Java_variableInitializer vi]
	:	ai=arrayInitializer
        {
            vi=`Gomantlr_Java_variableInitializer_1(ai);
        }
    |   e=expression
        {
            vi=`Gomantlr_Java_variableInitializer_2(e);
        }
	;
	
arrayInitializer returns [Gomantlr_Java_arrayInitializer ai]
@init {
    Gomantlr_Java_arrayInitializer_1 ai1=`Gomantlr_Java_arrayInitializer_1_2();
    Gomantlr_Java_arrayInitializer_1_1_2 ai2=`Gomantlr_Java_arrayInitializer_1_1_2_1();
    Gomantlr_Java_arrayInitializer_1_1_3 ai3=`Gomantlr_Java_arrayInitializer_1_1_3_2();
}
	:	'{' 
        (
            vi1=variableInitializer 
            (
                ',' vi2=variableInitializer
                {
                    ai2=`Gomantlr_Java_arrayInitializer_1_1_2_1(ai2*,Gomantlr_Java_arrayInitializer_1_1_2_1_1(vi2));
                }
            )*
            (
                ','
                {
                    ai3=`Gomantlr_Java_arrayInitializer_1_1_3_1();
                }
            )?
            {
                ai1=`Gomantlr_Java_arrayInitializer_1_1(vi1,ai2,ai3);
            }
        )?
        '}'
        {
            ai=`Gomantlr_Java_arrayInitializer(ai1);
        }
	;

modifier returns [Gomantlr_Java_modifier m]
    :   a=annotation { m=`Gomantlr_Java_modifier_1(a); }
    |   'public' { m=`Gomantlr_Java_modifier_2(); }
    |   'protected' { m=`Gomantlr_Java_modifier_3(); }
    |   'private' { m=`Gomantlr_Java_modifier_4(); }
    |   'static' { m=`Gomantlr_Java_modifier_5(); }
    |   'abstract' { m=`Gomantlr_Java_modifier_6(); }
    |   'final' { m=`Gomantlr_Java_modifier_7(); }
    |   'native' { m=`Gomantlr_Java_modifier_8(); }
    |   'synchronized' { m=`Gomantlr_Java_modifier_9(); }
    |   'transient' { m=`Gomantlr_Java_modifier_10(); }
    |   'volatile' { m=`Gomantlr_Java_modifier_11(); }
    |   'strictfp' { m=`Gomantlr_Java_modifier_12(); }
    ;

packageOrTypeName returns [Gomantlr_Java_packageOrTypeName potn]
@init {
    Gomantlr_Java_packageOrTypeName_2 potn2=`Gomantlr_Java_packageOrTypeName_2_1();
}
	:	i1=Identifier 
        (
            '.' i2=Identifier
            {
                potn2=`Gomantlr_Java_packageOrTypeName_2_1(potn2*,Gomantlr_Java_packageOrTypeName_2_1_1(Gomantlr_Java_Identifier(i2.getText())));
            }
        )*
        {
            potn=`Gomantlr_Java_packageOrTypeName(Gomantlr_Java_Identifier(i1.getText()),potn2);
        }
	;

enumConstantName returns [Gomantlr_Java_enumConstantName ecn]
    :   i=Identifier
        {
            ecn=`Gomantlr_Java_enumConstantName(Gomantlr_Java_Identifier(i.getText()));
        }
    ;

typeName returns [Gomantlr_Java_typeName tn]
	:   i=Identifier
        {
            tn=`Gomantlr_Java_typeName_1(Gomantlr_Java_Identifier(i.getText()));
        }
    |   potn=packageOrTypeName '.' i=Identifier
        {
            tn=`Gomantlr_Java_typeName_2(potn,Gomantlr_Java_Identifier(i.getText()));
        }
	;

type returns [Gomantlr_Java_type t]
@init {
    Gomantlr_Java_type_1_2 t1_2=`Gomantlr_Java_type_1_2_2();
    Gomantlr_Java_type_1_3 t1_3=`Gomantlr_Java_type_1_3_1();
    Gomantlr_Java_type_1_3_1_1_2 t1_3_2=`Gomantlr_Java_type_1_3_1_1_2_2();
    Gomantlr_Java_type_1_4 t1_4=`Gomantlr_Java_type_1_4_1();
    Gomantlr_Java_type_2_2 t2_2=`Gomantlr_Java_type_2_2_1();
}
	:	i1=Identifier 
        (
            ta1=typeArguments
            {
                t1_2=`Gomantlr_Java_type_1_2_1(ta1);
            }
        )? 
        (
            '.' i2=Identifier 
            (
                ta2=typeArguments
                {
                    t1_3_2=`Gomantlr_Java_type_1_3_1_1_2_1(ta2);
                }
            )?
            {
                t1_3=`Gomantlr_Java_type_1_3_1(t1_3*,Gomantlr_Java_type_1_3_1_1(Gomantlr_Java_Identifier(i2.getText()),t1_3_2));
            }
        )*
        (
            '[' ']'
            {
                t1_4=`Gomantlr_Java_type_1_4_1(t1_4*,Gomantlr_Java_type_1_4_1_1());
            }
        )*
        {
            t=`Gomantlr_Java_type_1(Gomantlr_Java_Identifier(i1.getText()),t1_2,t1_3,t1_4);
        }
	|	pt=primitiveType
        (
            '[' ']' 
            {
                t2_2=`Gomantlr_Java_type_2_2_1(t2_2*,Gomantlr_Java_type_2_2_1_1());
            }
        )*
        {
            t=`Gomantlr_Java_type_2(pt,t2_2);
        }
	;

primitiveType returns [Gomantlr_Java_primitiveType pt]
    :   'boolean' { pt=`Gomantlr_Java_primitiveType_1(); }
    |	'char' { pt=`Gomantlr_Java_primitiveType_2(); }
    |	'byte' { pt=`Gomantlr_Java_primitiveType_3(); }
    |	'short' { pt=`Gomantlr_Java_primitiveType_4(); }
    |	'int' { pt=`Gomantlr_Java_primitiveType_5(); }
    |	'long' { pt=`Gomantlr_Java_primitiveType_6(); }
    |	'float' { pt=`Gomantlr_Java_primitiveType_7(); }
    |	'double' { pt=`Gomantlr_Java_primitiveType_8(); }
    ;

variableModifier returns [Gomantlr_Java_variableModifier vm]
	:	'final'
        {
            vm=`Gomantlr_Java_variableModifier_1();
        }
    |   a=annotation
        {
            vm=`Gomantlr_Java_variableModifier_2(a);
        }
	;

typeArguments returns [Gomantlr_Java_typeArguments ta]
@init {
    Gomantlr_Java_typeArguments_2 ta3=`Gomantlr_Java_typeArguments_2_1();
}
	:	'<' ta1=typeArgument
        (
            ',' ta2=typeArgument
            {
                ta3=`Gomantlr_Java_typeArguments_2_1(ta3*,Gomantlr_Java_typeArguments_2_1_1(ta2));
            }
        )*
        '>'
        {
            ta=`Gomantlr_Java_typeArguments(ta1,ta3);
        }
	;
	
typeArgument returns [Gomantlr_Java_typeArgument ta]
@init {
    Gomantlr_Java_typeArgument_2_1 ta1=`Gomantlr_Java_typeArgument_2_1_2();
    Gomantlr_Java_typeArgument_2_1_1_1 ta2=null;
}
	:	t=type
        {
            ta=`Gomantlr_Java_typeArgument_1(t);
        }
	|	'?' 
        (
            (
                    'extends' 
                    {
                        ta2=`Gomantlr_Java_typeArgument_2_1_1_1_1();
                    }
                |
                    'super'
                    {
                        ta2=`Gomantlr_Java_typeArgument_2_1_1_1_2();
                    }
            )
            t=type
            {
                ta1=`Gomantlr_Java_typeArgument_2_1_1(ta2,t);
            }
        )?
        {
            ta=`Gomantlr_Java_typeArgument_2(ta1);
        }
	;
	
qualifiedNameList returns [Gomantlr_Java_qualifiedNameList qnl]
@init {
    qnl=`Gomantlr_Java_qualifiedNameList();
}
	:	qn1=qualifiedName 
        (
            ',' qn2=qualifiedName
            {
                qnl=`Gomantlr_Java_qualifiedNameList(qnl*,qn2);
            }
        )*
        {
            qnl=`Gomantlr_Java_qualifiedNameList(qn1,qnl*);
        }
	;
	
formalParameters returns [Gomantlr_Java_formalParameters fp]
@init {
    Gomantlr_Java_formalParameters_1 fp1=`Gomantlr_Java_formalParameters_1_2();
}
	:	'(' 
        (
            fpd=formalParameterDecls
            {
                fp1=`Gomantlr_Java_formalParameters_1_1(fpd);
            }
        )?
        ')'
        {
            fp=`Gomantlr_Java_formalParameters(fp1);
        }
	;
	
formalParameterDecls returns [Gomantlr_Java_formalParameterDecls fpd]
@init {
    Gomantlr_Java_formalParameterDecls_1 fpd1=`Gomantlr_Java_formalParameterDecls_1_2();
    Gomantlr_Java_formalParameterDecls_2 fpd2=`Gomantlr_Java_formalParameterDecls_2_2();
    Gomantlr_Java_formalParameterDecls_4 fpd4=`Gomantlr_Java_formalParameterDecls_4_2();
}
	:	
        (
            'final'
            {
                fpd1=`Gomantlr_Java_formalParameterDecls_1_1();
            }
        )?
        (
            a=annotations
            {
                fpd2=`Gomantlr_Java_formalParameterDecls_2_1(a);
            }
        )?
        t=type 
        (
            fpdr=formalParameterDeclsRest
            {
                fpd4=`Gomantlr_Java_formalParameterDecls_4_1(fpdr);
            }
        )?
        {
            fpd=`Gomantlr_Java_formalParameterDecls(fpd1,fpd2,t,fpd4);
        }
	;
	
formalParameterDeclsRest returns [Gomantlr_Java_formalParameterDeclsRest fpdr]
@init {
    Gomantlr_Java_formalParameterDeclsRest_1_2 fpdr2=`Gomantlr_Java_formalParameterDeclsRest_1_1_2();
}
	:	vdi=variableDeclaratorId 
        (
            ',' fpd=formalParameterDecls
            {
                fpdr2=`Gomantlr_Java_formalParameterDeclsRest_1_2_1(fpd);
            }
        )?
        {
            fpdr=`Gomantlr_Java_formalParameterDeclsRest_1(vdi,fpdr2);
        }
	|   '...' vdi=variableDeclaratorId
        {
            fpdr=`Gomantlr_Java_formalParameterDeclsRest_2(vdi);
        }
	;
	
methodBody returns [Gomantlr_Java_methodBody mb]
	:	b=block
        { mb=`Gomantlr_Java_methodBody(b); }
	;

qualifiedName returns [Gomantlr_Java_qualifiedName qn]
@init {
    Gomantlr_Java_qualifierList qn2=`Gomantlr_Java_qualifierList();
}
	:	i1=Identifier 
        (
            '.' i2=Identifier
            {
                qn2=`Gomantlr_Java_qualifierList(qn2*,Gomantlr_Java_Identifier(i2.getText()));
            }
        )*
        {
            qn=`Gomantlr_Java_qualifiedName(Gomantlr_Java_Identifier(i1.getText()),qn2);
        }
	;
	
literal	returns [Gomantlr_Java_literal l]
	:   il=integerLiteral
        { l=`Gomantlr_Java_integerLiteral(il); }
    |   fpl=FloatingPointLiteral
        { l=`Gomantlr_Java_floatingPointLiteral(Gomantlr_Java_FloatingPointLiteral(fpl.getText())); }
    |   cl=CharacterLiteral
        { l=`Gomantlr_Java_characterLiteral(Gomantlr_Java_CharacterLiteral(cl.getText())); }
    |   sl=StringLiteral
        { l=`Gomantlr_Java_stringLiteral(Gomantlr_Java_StringLiteral(sl.getText())); }
    |   bl=booleanLiteral
        { l=`Gomantlr_Java_booleanLiteral(bl); }
    |   'null'
        { l=`Gomantlr_Java_nullLiteral(); }
	;

integerLiteral returns [Gomantlr_Java_integerLiteral il]
    :   h=HexLiteral 
        { il=`Gomantlr_Java_HexLiteral(h.getText()); }
    |   o=OctalLiteral
        { il=`Gomantlr_Java_OctalLiteral(o.getText()); }
    |   d=DecimalLiteral
        { il=`Gomantlr_Java_DecimalLiteral(d.getText()); }
    ;

booleanLiteral returns [Gomantlr_Java_booleanLiteral bl]
    :   'true'
        { bl=`Gomantlr_Java_True(); }
    |   'false'
        { bl=`Gomantlr_Java_False(); }
    ;

// ANNOTATIONS

annotations returns [Gomantlr_Java_annotations a]
@init {
    a=`Gomantlr_Java_annotations_1();
}
	:	
        (
            a1=annotation
            {
                a=`Gomantlr_Java_annotations_1(a*,a1);
            }
        )+
	;

annotation returns [Gomantlr_Java_annotation a]
@init {
    Gomantlr_Java_annotation_2 a2=`Gomantlr_Java_annotation_2_2();
    Gomantlr_Java_annotation_2_1_1_1 a3=`Gomantlr_Java_annotation_2_1_1_1_2();
}
	:	'@' tn=typeName
        (
            '(' 
            (
                i=Identifier '='
                {
                    a3=`Gomantlr_Java_annotation_2_1_1_1_1(Gomantlr_Java_annotation_2_1_1_1_1_1(Gomantlr_Java_Identifier(i.getText())));
                }
            )? 
            ev=elementValue
            ')'
            {
                a2=`Gomantlr_Java_annotation_2_1(Gomantlr_Java_annotation_2_1_1(a3,ev));
            }
        )?
        {
            a=`Gomantlr_Java_annotation(tn,a2);
        }
	;
	
elementValue returns [Gomantlr_Java_elementValue ev]
	:	ce=conditionalExpression
        {
            ev=`Gomantlr_Java_elementValue_1(ce);
        }
	|   a=annotation
        {
            ev=`Gomantlr_Java_elementValue_2(a);
        }
	|   evai=elementValueArrayInitializer
        {
            ev=`Gomantlr_Java_elementValue_3(evai);
        }
	;
	
elementValueArrayInitializer returns [Gomantlr_Java_elementValueArrayInitializer evai]
@init {
    Gomantlr_Java_elementValueArrayInitializer_1 evai1=`Gomantlr_Java_elementValueArrayInitializer_1_2();
    Gomantlr_Java_elementValueArrayInitializer_2 evai2=`Gomantlr_Java_elementValueArrayInitializer_2_2();
}
	:	'{' 
        (
            ev=elementValue
            {
                evai1=`Gomantlr_Java_elementValueArrayInitializer_1_1(ev);
            }
        )? 
        (
            ','
            {
                evai2=`Gomantlr_Java_elementValueArrayInitializer_2_1();
            }
        )?
        '}'
        {
            evai=`Gomantlr_Java_elementValueArrayInitializer(evai1,evai2);
        }
	;
	
annotationTypeDeclaration returns [Gomantlr_Java_annotationTypeDeclaration atd]
	:	'@' 'interface' i=Identifier atb=annotationTypeBody
        {
            atd=`Gomantlr_Java_annotationTypeDeclaration(Gomantlr_Java_Identifier(i.getText()),atb);
        }
	;
	
annotationTypeBody returns [Gomantlr_Java_annotationTypeBody atb]
@init {
    Gomantlr_Java_annotationTypeBody_1 atb1=`Gomantlr_Java_annotationTypeBody_1_2();
}
	:	'{'
        (
            ated=annotationTypeElementDeclarations
            {
                atb1=`Gomantlr_Java_annotationTypeBody_1_1(ated);
            }
        )? 
        '}'
        {
            atb=`Gomantlr_Java_annotationTypeBody(atb1);
        }
	;
	
annotationTypeElementDeclarations returns [Gomantlr_Java_annotationTypeElementDeclarations ated]
@init {
    Gomantlr_Java_annotationTypeElementDeclarations_2 tated=`Gomantlr_Java_annotationTypeElementDeclarations_2_1();
}
	:	ated1=annotationTypeElementDeclaration 
        (
            ated2=annotationTypeElementDeclaration
            {
                tated=`Gomantlr_Java_annotationTypeElementDeclarations_2_1(tated*,ated2);
            }
        )*
        {
            ated=`Gomantlr_Java_annotationTypeElementDeclarations(ated1,tated);
        }
	;
	
annotationTypeElementDeclaration returns [Gomantlr_Java_annotationTypeElementDeclaration ated]
@init {
    Gomantlr_Java_annotationTypeElementDeclaration_1 ated1=`Gomantlr_Java_annotationTypeElementDeclaration_1_1();
}
	:	(
            m=modifier
            {
                ated1=`Gomantlr_Java_annotationTypeElementDeclaration_1_1(ated1*,m);
            }
        )*
        ater=annotationTypeElementRest
        {
            ated=`Gomantlr_Java_annotationTypeElementDeclaration(ated1,ater);
        }
	;
	
annotationTypeElementRest returns [Gomantlr_Java_annotationTypeElementRest ater]
	:	t=type i=Identifier amocr=annotationMethodOrConstantRest ';'
        {
            ater=`Gomantlr_Java_annotationTypeElementRest_1(Gomantlr_Java_annotationTypeElementRest_1_1(t,Gomantlr_Java_Identifier(i.getText()),amocr));
        }
	|   cd=classDeclaration
        {
            ater=`Gomantlr_Java_annotationTypeElementRest_2(cd);
        }
	|   id=interfaceDeclaration
        {
            ater=`Gomantlr_Java_annotationTypeElementRest_3(id);
        }
	|   ed=enumDeclaration
        {
            ater=`Gomantlr_Java_annotationTypeElementRest_4(ed);
        }
	|   atd=annotationTypeDeclaration
        {
            ater=`Gomantlr_Java_annotationTypeElementRest_5(atd);
        }
	;
	
annotationMethodOrConstantRest returns [Gomantlr_Java_annotationMethodOrConstantRest amocr]
	:	amr=annotationMethodRest
        {
            amocr=`Gomantlr_Java_annotationMethodOrConstantRest_1(amr);
        }
	|   acr=annotationConstantRest
        {
            amocr=`Gomantlr_Java_annotationMethodOrConstantRest_2(acr);
        }
	;
	
annotationMethodRest returns [Gomantlr_Java_annotationMethodRest amr]
@init {
    Gomantlr_Java_annotationMethodRest_1 amr1=`Gomantlr_Java_annotationMethodRest_1_2();
}
 	:	'(' ')' 
        (
            dv=defaultValue
            {
                amr1=`Gomantlr_Java_annotationMethodRest_1_1(dv);
            }
        )?
        {
            amr=`Gomantlr_Java_annotationMethodRest(amr1);
        }
 	;
 	
annotationConstantRest returns [Gomantlr_Java_annotationConstantRest acr]
 	:	vd=variableDeclarators
        {
            acr=`Gomantlr_Java_annotationConstantRest(vd);
        }
 	;
 	
defaultValue returns [Gomantlr_Java_defaultValue dv]
 	:	'default' ev=elementValue
        {
            dv=`Gomantlr_Java_defaultValue(ev);
        }
 	;

// STATEMENTS / BLOCKS

block returns [Gomantlr_Java_block b]
@init {
    b=`Gomantlr_Java_block();
}
	:	'{' 
        (
            bs=blockStatement
            {
                b=`Gomantlr_Java_block(b*,bs);
            }
        )*
        '}'
	;
	
blockStatement returns [Gomantlr_Java_blockStatement bs]
	:	lvd=localVariableDeclaration
        {
            bs=`Gomantlr_Java_localVariable(lvd);
        }
    |   coid=classOrInterfaceDeclaration
        {
            bs=`Gomantlr_Java_classOrInterface(coid);
        }
    |   s=statement
        {
            bs=`Gomantlr_Java_statement(s);
        }
	;
	
localVariableDeclaration returns [Gomantlr_Java_localVariableDeclaration lvd]
@init {
    Gomantlr_Java_localVariableModifier lvd1=`Gomantlr_Java_localVariableNoModifier();
}
	:	(
            'final'
            {
                lvd1=`Gomantlr_Java_localVariableModifierFinal();
            }
        )? 
        t=type vd=variableDeclarators ';'
        {
            lvd=`Gomantlr_Java_localVariableDeclaration(lvd1,t,vd);
        }
        
	;
	
statement returns [Gomantlr_Java_statement s]
@init {
    Gomantlr_Java_statement_2_1_2 ts2=`Gomantlr_Java_statement_2_1_2_2();
    Gomantlr_Java_statement_3_1_3 ts3=`Gomantlr_Java_statement_3_1_3_2();
    Gomantlr_Java_statement_7_1_2 ts7=null;
    Gomantlr_Java_statement_10_1_1 ts10=`Gomantlr_Java_statement_10_1_1_2();
    Gomantlr_Java_statement_12_1_1 ts12=`Gomantlr_Java_statement_12_1_1_2();
    Gomantlr_Java_statement_13_1_1 ts13=`Gomantlr_Java_statement_13_1_1_2();
}
	: 
            b=block
            {
                s=`Gomantlr_Java_statement_1(b);
            }
        | 
            'assert' e1=expression 
            (
                ':' e2=expression
                {
                    ts2=`Gomantlr_Java_statement_2_1_2_1(Gomantlr_Java_statement_2_1_2_1_1(e2));
                }
            )?
            ';'
            {
                s=`Gomantlr_Java_statement_2(Gomantlr_Java_statement_2_1(e1,ts2));
            }
        | 
            'if' pe=parExpression s1=statement 
            (
                'else' s2=statement
                {
                    ts3=`Gomantlr_Java_statement_3_1_3_1(Gomantlr_Java_statement_3_1_3_1_1(s2));
                }
            )?
            {
                s=`Gomantlr_Java_statement_3(Gomantlr_Java_statement_3_1(pe,s1,ts3));
            }
        | 
            'for' '(' fc=forControl ')' s1=statement
            {
                s=`Gomantlr_Java_statement_4(Gomantlr_Java_statement_4_1(fc,s1));
            }
        |
            'while' pe=parExpression s1=statement
            {
                s=`Gomantlr_Java_statement_5(Gomantlr_Java_statement_5_1(pe,s1));
            }
        |
            'do' s1=statement 'while' pe=parExpression ';'
            {
                s=`Gomantlr_Java_statement_6(Gomantlr_Java_statement_6_1(s1,pe));
            }
        |
            'try' b=block
            (
                    c=catches 'finally' b=block
                    {
                        ts7=`Gomantlr_Java_statement_7_1_2_1(Gomantlr_Java_statement_7_1_2_1_1(c,b));
                    }
                | 
                    c=catches
                    {
                        ts7=`Gomantlr_Java_statement_7_1_2_2(c);
                    }
                |
                    'finally' b=block
                    {
                        ts7=`Gomantlr_Java_statement_7_1_2_3(Gomantlr_Java_statement_7_1_2_3_1(b));
                    }
            )
            {
                s=`Gomantlr_Java_statement_7(Gomantlr_Java_statement_7_1(b,ts7));
            }
        |
            'switch' pe=parExpression '{' sbsg=switchBlockStatementGroups '}'
            {
                s=`Gomantlr_Java_statement_8(Gomantlr_Java_statement_8_1(pe,sbsg));
            }
        |
            'synchronized' pe=parExpression b=block
            {
                s=`Gomantlr_Java_statement_9(Gomantlr_Java_statement_9_1(pe,b));
            }
        |
            'return' 
            (
                e=expression
                {
                    ts10=`Gomantlr_Java_statement_10_1_1_1(e);
                }
            )? 
            ';'
            {
                s=`Gomantlr_Java_statement_10(Gomantlr_Java_statement_10_1(ts10));
            }
        |
            'throw' e=expression ';'
            {
                s=`Gomantlr_Java_statement_11(Gomantlr_Java_statement_11_1(e));
            }
        |
            'break'
            (
                i=Identifier
                {
                    ts12=`Gomantlr_Java_statement_12_1_1_1(Gomantlr_Java_Identifier(i.getText()));
                }
            )?
            ';'
            {
                s=`Gomantlr_Java_statement_12(Gomantlr_Java_statement_12_1(ts12));
            }
        |
            'continue' 
            (
                i=Identifier
                {
                    ts13=`Gomantlr_Java_statement_13_1_1_1(Gomantlr_Java_Identifier(i.getText()));
                }
            )?
            ';'
            {
                s=`Gomantlr_Java_statement_13(Gomantlr_Java_statement_13_1(ts13));
            }
        |
            ';'
            {
                s=`Gomantlr_Java_statement_14();
            }
        |
            se=statementExpression ';'
            {
                s=`Gomantlr_Java_statement_15(Gomantlr_Java_statement_15_1(se));
            }
        | 
            i=Identifier ':' s1=statement
            {
                s=`Gomantlr_Java_statement_16(Gomantlr_Java_statement_16_1(Gomantlr_Java_Identifier(i.getText()),s1));
            }
	;
	
catches returns [Gomantlr_Java_catches c]
@init {
    c=`Gomantlr_Java_catches();
}
	:	cc1=catchClause
        (
            cc2=catchClause
            {
                c=`Gomantlr_Java_catches(c*,cc2);
            }
        )*
        {
            c=`Gomantlr_Java_catches(cc1,c*);
        }
	;
	
catchClause returns [Gomantlr_Java_catchClause cc]
	:	'catch' '(' fp=formalParameter ')' b=block
        {
            cc=`Gomantlr_Java_catchClause(fp,b);
        }
	;

formalParameter returns [Gomantlr_Java_formalParameter fp]
@init {
    Gomantlr_Java_formalParameter_1 fp1=`Gomantlr_Java_formalParameter_1_1();
}
	:	(
            vm=variableModifier
            {
                fp1=`Gomantlr_Java_formalParameter_1_1(fp1*,vm);
            }
        )*
        t=type vdi=variableDeclaratorId
        {
            fp=`Gomantlr_Java_formalParameter(fp1,t,vdi);
        }
	;
	
switchBlockStatementGroups returns [Gomantlr_Java_switchBlockStatementGroups sbsg]
@init {
    sbsg=`Gomantlr_Java_switchBlockStatementGroups_1();
}
	:	(
            sbsg1=switchBlockStatementGroup
            {
                sbsg=`Gomantlr_Java_switchBlockStatementGroups_1(sbsg*,sbsg1);
            }
        )*
	;
	
switchBlockStatementGroup returns [Gomantlr_Java_switchBlockStatementGroup sbsg]
@init {
    Gomantlr_Java_switchBlockStatementGroup_2 sbsg2=`Gomantlr_Java_switchBlockStatementGroup_2_1();
}
	:	sl=switchLabel 
        (
            bs=blockStatement
            {
                sbsg2=`Gomantlr_Java_switchBlockStatementGroup_2_1(sbsg2*,bs);
            }
        )*
        {
            sbsg=`Gomantlr_Java_switchBlockStatementGroup(sl,sbsg2);
        }
	;
	
switchLabel returns [Gomantlr_Java_switchLabel sl]
	:
            'case' ce=constantExpression ':'
            {
                sl=`Gomantlr_Java_switchLabel_1(Gomantlr_Java_switchLabel_1_1(ce));
            }
	|
        'case' ecn=enumConstantName ':'
            {
                sl=`Gomantlr_Java_switchLabel_2(Gomantlr_Java_switchLabel_2_1(ecn));
            }
	|   'default' ':'
            {
                sl=`Gomantlr_Java_switchLabel_3(Gomantlr_Java_switchLabel_3_1());
            }
	;
	
moreStatementExpressions returns [Gomantlr_Java_moreStatementExpressions mse]
@init {
    mse=`Gomantlr_Java_moreStatementExpressions_1();
}
	:	(
            ',' se=statementExpression
            {
                mse=`Gomantlr_Java_moreStatementExpressions_1(mse*,Gomantlr_Java_moreStatementExpressions_1_1(se));
            }
        )*
	;

forControl returns [Gomantlr_Java_forControl fc]
@init {
    Gomantlr_Java_forControl_2_1_1 fc1=`Gomantlr_Java_forControl_2_1_1_2();
    Gomantlr_Java_forControl_2_1_2 fc2=`Gomantlr_Java_forControl_2_1_2_2();
    Gomantlr_Java_forControl_2_1_3 fc3=`Gomantlr_Java_forControl_2_1_3_2();}
	:	    fvc=forVarControl
            {
                fc=`Gomantlr_Java_forControl_1(fvc);
            }
	    |   
            (
                fi=forInit
                {
                    fc1=`Gomantlr_Java_forControl_2_1_1_1(fi);
                }
            )?
            ';'
            (
                e=expression
                {
                    fc2=`Gomantlr_Java_forControl_2_1_2_1(e);
                }
            )?
            ';'
            (
                fu=forUpdate
                {
                    fc3=`Gomantlr_Java_forControl_2_1_3_1(fu);
                }
            )?
            {
                fc=`Gomantlr_Java_forControl_2(Gomantlr_Java_forControl_2_1(fc1,fc2,fc3));
            }
	;

forInit returns [Gomantlr_Java_forInit fi]
@init {
    Gomantlr_Java_forInit_1_1_1 fi1=`Gomantlr_Java_forInit_1_1_1_2();
}
	:	    (
                'final'
                {
                    fi1=`Gomantlr_Java_forInit_1_1_1_1();
                }
            )?
            t=type vd=variableDeclarators
            {
                fi=`Gomantlr_Java_forInit_1(Gomantlr_Java_forInit_1_1(fi1,t,vd));
            }
        |  
            el=expressionList
            {
                `Gomantlr_Java_forInit_2(el);
            }
	;
	
forVarControl returns [Gomantlr_Java_forVarControl fvc]
@init {
    Gomantlr_Java_forVarControl_1 fvc1=`Gomantlr_Java_forVarControl_1_2();
    Gomantlr_Java_forVarControl_2 fvc2=`Gomantlr_Java_forVarControl_2_2();
}
	:	(
            'final'
            {
                fvc1=`Gomantlr_Java_forVarControl_1_1();
            }
        )?
        (
            a=annotation
            {
                fvc2=`Gomantlr_Java_forVarControl_2_1(a);
            }
        )? 
        t=type i=Identifier fvcr=forVarControlRest
        {
            fvc=`Gomantlr_Java_forVarControl(fvc1,fvc2,t,Gomantlr_Java_Identifier(i.getText()),fvcr);
        }
	;

forVarControlRest returns [Gomantlr_Java_forVarControlRest fvcr]
@init {
    Gomantlr_Java_forVarControlRest_1_1_2 fvcr2=`Gomantlr_Java_forVarControlRest_1_1_2_1();
    Gomantlr_Java_forVarControlRest_1_1_3 fvcr3=`Gomantlr_Java_forVarControlRest_1_1_3_2();
    Gomantlr_Java_forVarControlRest_1_1_4 fvcr4=`Gomantlr_Java_forVarControlRest_1_1_4_2();
}
	:	    vdr=variableDeclaratorRest 
            (
                ',' vd=variableDeclarator
                {
                    fvcr2=`Gomantlr_Java_forVarControlRest_1_1_2_1(fvcr2*,Gomantlr_Java_forVarControlRest_1_1_2_1_1(vd));
                }
            )*
            ';' 
            (
                e=expression
                {
                    fvcr3=`Gomantlr_Java_forVarControlRest_1_1_3_1(e);
                }
            )?
            ':' 
            (
                fu=forUpdate
                {
                    fvcr4=`Gomantlr_Java_forVarControlRest_1_1_4_1(fu);
                }
            )?
            {
                fvcr=`Gomantlr_Java_forVarControlRest_1(Gomantlr_Java_forVarControlRest_1_1(vdr,fvcr2,fvcr3,fvcr4));
            }
        |   
            ':' e=expression
            {
                fvcr=`Gomantlr_Java_forVarControlRest_2(Gomantlr_Java_forVarControlRest_2_1(e));
            }
	;

forUpdate returns [Gomantlr_Java_forUpdate fu]
	:	el=expressionList
        {
            fu=`Gomantlr_Java_forUpdate(el);
        }
                                
	;

// EXPRESSIONS

parExpression returns [Gomantlr_Java_parExpression pe]
	:	'(' e=expression ')'
        {
            pe=`Gomantlr_Java_parExpression(e);
        }
	;
	
expressionList returns [Gomantlr_Java_expressionList el]
@init {
    Gomantlr_Java_expressionList_2 el2=`Gomantlr_Java_expressionList_2_1();
}
    :   e1=expression 
        (
            ',' e2=expression
            {
                el2=`Gomantlr_Java_expressionList_2_1(el2*,Gomantlr_Java_expressionList_2_1_1(e2));
            }
        )*
        {
            el=`Gomantlr_Java_expressionList(e1,el2);
        }
    ;

statementExpression returns [Gomantlr_Java_statementExpression se]
	:	e=expression
        {
            se=`Gomantlr_Java_statementExpression(e);
        }
	;
	
constantExpression returns [Gomantlr_Java_constantExpression ce]
	:	e=expression
        {
            ce=`Gomantlr_Java_constantExpression(e);
        }
	;
	
expression returns [Gomantlr_Java_expression e]
@init {
    Gomantlr_Java_expression_2 e2=`Gomantlr_Java_expression_2_2();
}
	:	ce=conditionalExpression 
        (
            ao=assignmentOperator e1=expression
            {
                e2=`Gomantlr_Java_expression_2_1(Gomantlr_Java_expression_2_1_1(ao,e1));
            }
        )?
        {
            e=`Gomantlr_Java_expression(ce,e2);
        }
	;
	
assignmentOperator returns [Gomantlr_Java_assignmentOperator ao]
	:	'='
        {
            ao=`Gomantlr_Java_assignmentOperator_1();
        }
    |   '+='
        {
            ao=`Gomantlr_Java_assignmentOperator_2();
        }
    |   '-='
        {
            ao=`Gomantlr_Java_assignmentOperator_3();
        }
    |   '*='
        {
            ao=`Gomantlr_Java_assignmentOperator_4();
        }
    |   '/='
        {
            ao=`Gomantlr_Java_assignmentOperator_5();
        }
    |   '&='
        {
            ao=`Gomantlr_Java_assignmentOperator_6();
        }
    |   '|='
        {
            ao=`Gomantlr_Java_assignmentOperator_7();
        }
    |   '^='
        {
            ao=`Gomantlr_Java_assignmentOperator_8();
        }
    |   '%='
        {
            ao=`Gomantlr_Java_assignmentOperator_9();
        }
    |   '<' '<' '='
        {
            ao=`Gomantlr_Java_assignmentOperator_10(Gomantlr_Java_assignmentOperator_10_1());
        }
    |   '>' '>' '='
        {
            ao=`Gomantlr_Java_assignmentOperator_11(Gomantlr_Java_assignmentOperator_11_1());
        }
    |   '>' '>' '>' '='
        {
            ao=`Gomantlr_Java_assignmentOperator_12(Gomantlr_Java_assignmentOperator_12_1());
        }
	;

conditionalExpression returns [Gomantlr_Java_conditionalExpression ce]
@init {
    Gomantlr_Java_conditionalExpression_2 ce2=`Gomantlr_Java_conditionalExpression_2_2();
}
    :   coe=conditionalOrExpression 
        (
            '?' e1=expression ':' e2=expression 
            {
                ce2=`Gomantlr_Java_conditionalExpression_2_1(Gomantlr_Java_conditionalExpression_2_1_1(e1,e2));
            }
        )?
        {
            ce=`Gomantlr_Java_conditionalExpression(coe,ce2);
        }
	;

conditionalOrExpression returns [Gomantlr_Java_conditionalOrExpression coe]
@init {
    Gomantlr_Java_conditionalOrExpression_2 coe2=`Gomantlr_Java_conditionalOrExpression_2_1();
}
    :   cae1=conditionalAndExpression 
        ( 
            '||' cae2=conditionalAndExpression 
            {
                coe2=`Gomantlr_Java_conditionalOrExpression_2_1(coe2*,Gomantlr_Java_conditionalOrExpression_2_1_1(cae2));
            }
        )*
        {
            coe=`Gomantlr_Java_conditionalOrExpression(cae1,coe2);
        }
	;

conditionalAndExpression returns [Gomantlr_Java_conditionalAndExpression cae]
@init {
    Gomantlr_Java_conditionalAndExpression_2 cae2=`Gomantlr_Java_conditionalAndExpression_2_1();
}
    :   ioe1=inclusiveOrExpression 
        (
            '&&' ioe2=inclusiveOrExpression 
            {
                cae2=`Gomantlr_Java_conditionalAndExpression_2_1(cae2*,Gomantlr_Java_conditionalAndExpression_2_1_1(ioe2));
            }
        )*
        {
            cae=`Gomantlr_Java_conditionalAndExpression(ioe1,cae2);
        }
	;

inclusiveOrExpression returns [Gomantlr_Java_inclusiveOrExpression ioe]
@init {
    Gomantlr_Java_inclusiveOrExpression_2 ioe2=`Gomantlr_Java_inclusiveOrExpression_2_1();
}
    :   eoe1=exclusiveOrExpression 
        (
            '|' eoe2=exclusiveOrExpression 
            {
                ioe2=`Gomantlr_Java_inclusiveOrExpression_2_1(ioe2*,Gomantlr_Java_inclusiveOrExpression_2_1_1(eoe2));
            }
        )*
        {
            ioe=`Gomantlr_Java_inclusiveOrExpression(eoe1,ioe2);
        }
	;

exclusiveOrExpression returns [Gomantlr_Java_exclusiveOrExpression eoe]
@init {
    Gomantlr_Java_exclusiveOrExpression_2 eoe2=`Gomantlr_Java_exclusiveOrExpression_2_1();
}
    :   ae1=andExpression
        (
            '^' ae2=andExpression 
            {
                eoe2=`Gomantlr_Java_exclusiveOrExpression_2_1(eoe2*,Gomantlr_Java_exclusiveOrExpression_2_1_1(ae2));
            }
        )*
        {
            eoe=`Gomantlr_Java_exclusiveOrExpression(ae1,eoe2);
        }
	;

andExpression returns [Gomantlr_Java_andExpression ae]
@init {
    Gomantlr_Java_andExpression_2 ae2=`Gomantlr_Java_andExpression_2_1();
}
    :   ee1=equalityExpression 
        (
            '&' ee2=equalityExpression 
            {
                ae2=`Gomantlr_Java_andExpression_2_1(ae2*,Gomantlr_Java_andExpression_2_1_1(ee2));
            }
        )*
        {
            ae=`Gomantlr_Java_andExpression(ee1,ae2);
        }
	;

equalityExpression returns [Gomantlr_Java_equalityExpression ee]
@init {
    Gomantlr_Java_equalityExpression_2 ee2=`Gomantlr_Java_equalityExpression_2_1();
    Gomantlr_Java_equalityExpression_2_1_1_1 ee3=null;
}
    :   ioe1=instanceOfExpression 
        (
            (
                    '=='
                    {
                        ee3=`Gomantlr_Java_equalityExpression_2_1_1_1_1();
                    }
                |
                    '!='
                    {
                        ee3=`Gomantlr_Java_equalityExpression_2_1_1_1_2();
                    }
            ) 
            ioe2=instanceOfExpression
            {
                ee2=`Gomantlr_Java_equalityExpression_2_1(ee2*,Gomantlr_Java_equalityExpression_2_1_1(ee3,ioe2));
            }
        )*
        {
            ee=`Gomantlr_Java_equalityExpression(ioe1,ee2);
        }
	;

instanceOfExpression returns [Gomantlr_Java_instanceOfExpression ioe]
@init {
    Gomantlr_Java_instanceOfExpression_2 ioe2=`Gomantlr_Java_instanceOfExpression_2_2();
}
    :   rexp=relationalExpression 
        (
            'instanceof' t=type
            {
                ioe2=`Gomantlr_Java_instanceOfExpression_2_1(Gomantlr_Java_instanceOfExpression_2_1_1(t));
            }
        )?
        {
            ioe=`Gomantlr_Java_instanceOfExpression(rexp,ioe2);
        }
	;

relationalExpression returns [Gomantlr_Java_relationalExpression rexp]
@init {
    Gomantlr_Java_relationalExpression_2 re2=`Gomantlr_Java_relationalExpression_2_1();
}
    :   se1=shiftExpression 
        ( 
            ro=relationalOp se2=shiftExpression 
            {
                re2=`Gomantlr_Java_relationalExpression_2_1(re2*,Gomantlr_Java_relationalExpression_2_1_1(ro,se2));
            }
        )*
        {
            rexp=`Gomantlr_Java_relationalExpression(se1,re2);
        }
	;
	
relationalOp returns [Gomantlr_Java_relationalOp ro]
	:	(
            '<' '=' 
            {
                ro=`Gomantlr_Java_relationalOp_1(Gomantlr_Java_relationalOp_1_1());
            }
            | '>' '=' 
            {
                ro=`Gomantlr_Java_relationalOp_2(Gomantlr_Java_relationalOp_2_1());
            }
            | '<' 
            {
                ro=`Gomantlr_Java_relationalOp_3();
            }
            | '>'
            {
                ro=`Gomantlr_Java_relationalOp_4();
            }
        )
	;

shiftExpression returns [Gomantlr_Java_shiftExpression se]
@init {
    Gomantlr_Java_shiftExpression_2 se2=`Gomantlr_Java_shiftExpression_2_1();
}
    :   ae1=additiveExpression 
        ( 
            so=shiftOp 
            ae2=additiveExpression 
            {
                se2=`Gomantlr_Java_shiftExpression_2_1(se2*,Gomantlr_Java_shiftExpression_2_1_1(so,ae2));
            }
        )*
        {
            se=`Gomantlr_Java_shiftExpression(ae1,se2);
        }
	;

        // TODO: need a sem pred to check column on these >>>
shiftOp returns [Gomantlr_Java_shiftOp so]
	:	(
            '<' '<' 
            {
                so=`Gomantlr_Java_shiftOp_1(Gomantlr_Java_shiftOp_1_1());
            }
            | '>' '>' '>'
            {
                so=`Gomantlr_Java_shiftOp_2(Gomantlr_Java_shiftOp_2_1());
            }
            | '>' '>'
            {
                so=`Gomantlr_Java_shiftOp_3(Gomantlr_Java_shiftOp_3_1());
            }
        )
	;


additiveExpression returns [Gomantlr_Java_additiveExpression ae]
@init {
    Gomantlr_Java_additiveExpression_2 ae2=`Gomantlr_Java_additiveExpression_2_1();
    Gomantlr_Java_additiveExpression_2_1_1_1 ae3=null;
}
    :   me1=multiplicativeExpression 
        ( 
            (
                    '+' 
                    {
                        ae3=`Gomantlr_Java_additiveExpression_2_1_1_1_1();
                    }
                | 
                    '-'
                    {
                        ae3=`Gomantlr_Java_additiveExpression_2_1_1_1_2();
                    }
            )
            me2=multiplicativeExpression
            {
                ae2=`Gomantlr_Java_additiveExpression_2_1(ae2*,Gomantlr_Java_additiveExpression_2_1_1(ae3,me2));
            }
        )*
        {
            ae=`Gomantlr_Java_additiveExpression(me1,ae2);
        }
	;

multiplicativeExpression returns [Gomantlr_Java_multiplicativeExpression me]
@init {
    Gomantlr_Java_multiplicativeExpression_2 me2=`Gomantlr_Java_multiplicativeExpression_2_1();
    Gomantlr_Java_multiplicativeExpression_2_1_1_1 me3=null;
}
    :   ue1=unaryExpression 
        (
            ( 
                    '*' 
                    {
                        me3=`Gomantlr_Java_multiplicativeExpression_2_1_1_1_1();
                    }
                | 
                    '/' 
                    {
                        me3=`Gomantlr_Java_multiplicativeExpression_2_1_1_1_2();
                    }
                | 
                    '%' 
                    {
                        me3=`Gomantlr_Java_multiplicativeExpression_2_1_1_1_3();
                    }
            ) 
            ue2=unaryExpression
            {
                me2=`Gomantlr_Java_multiplicativeExpression_2_1(me2*,Gomantlr_Java_multiplicativeExpression_2_1_1(me3,ue2));
            }
        )*
        {
            me=`Gomantlr_Java_multiplicativeExpression(ue1,me2);
        }
	;
	
unaryExpression returns [Gomantlr_Java_unaryExpression ue]
    :       '+' ue1=unaryExpression
            {
                ue=`Gomantlr_Java_unaryExpression_1(Gomantlr_Java_unaryExpression_1_1(ue1));
            }
        |	
            '-' ue1=unaryExpression
            {
                ue=`Gomantlr_Java_unaryExpression_2(Gomantlr_Java_unaryExpression_2_1(ue1));
            }
        |
            '++' p=primary
            {
                ue=`Gomantlr_Java_unaryExpression_3(Gomantlr_Java_unaryExpression_3_1(p));
            }
        |
            '--' p=primary
            {
                ue=`Gomantlr_Java_unaryExpression_4(Gomantlr_Java_unaryExpression_4_1(p));
            }
        |
            uenpm=unaryExpressionNotPlusMinus
            {
                ue=`Gomantlr_Java_unaryExpression_5(uenpm);
            }
    ;

unaryExpressionNotPlusMinus returns [Gomantlr_Java_unaryExpressionNotPlusMinus uenpm]
@init {
    Gomantlr_Java_unaryExpressionNotPlusMinus_4_1_2 uenpm2=`Gomantlr_Java_unaryExpressionNotPlusMinus_4_1_2_1();
    Gomantlr_Java_unaryExpressionNotPlusMinus_4_1_3 uenpm3=`Gomantlr_Java_unaryExpressionNotPlusMinus_4_1_3_2();
}
    :       '~' ue=unaryExpression
            {
                uenpm=`Gomantlr_Java_unaryExpressionNotPlusMinus_1(Gomantlr_Java_unaryExpressionNotPlusMinus_1_1(ue));
            }
        |
            '!' ue=unaryExpression
            {
                uenpm=`Gomantlr_Java_unaryExpressionNotPlusMinus_2(Gomantlr_Java_unaryExpressionNotPlusMinus_2_1(ue));
            }
        |
            ce=castExpression
            {
                uenpm=`Gomantlr_Java_unaryExpressionNotPlusMinus_3(ce);
            }
        |
            p=primary 
            (
                s=selector
                {
                    uenpm2=`Gomantlr_Java_unaryExpressionNotPlusMinus_4_1_2_1(uenpm2*,s);
                }
            )* 
            (
                    '++'
                    {
                        uenpm3=`Gomantlr_Java_unaryExpressionNotPlusMinus_4_1_3_1(Gomantlr_Java_unaryExpressionNotPlusMinus_4_1_3_1_1_1());
                    }
                |
                    '--'
                    {
                        uenpm3=`Gomantlr_Java_unaryExpressionNotPlusMinus_4_1_3_1(Gomantlr_Java_unaryExpressionNotPlusMinus_4_1_3_1_1_2());
                    }
            )?
            {
                uenpm=`Gomantlr_Java_unaryExpressionNotPlusMinus_4(Gomantlr_Java_unaryExpressionNotPlusMinus_4_1(p,uenpm2,uenpm3));
            }
    ;

castExpression returns [Gomantlr_Java_castExpression ce]
@init {
    Gomantlr_Java_castExpression_2_1_1 ce2=null;
}
    :       '(' pt=primitiveType ')' ue=unaryExpression
            {
                ce=`Gomantlr_Java_castExpression_1(Gomantlr_Java_castExpression_1_1(pt,ue));
            }
        |  
            '(' 
            (
                    e=expression
                    {
                        ce2=`Gomantlr_Java_castExpression_2_1_1_1(e);
                    }
                | 
                    t=type
                    {
                        ce2=`Gomantlr_Java_castExpression_2_1_1_2(t);
                    }
            )
            ')' uenpm=unaryExpressionNotPlusMinus
            {
                ce=`Gomantlr_Java_castExpression_2(Gomantlr_Java_castExpression_2_1(ce2,uenpm));
            }
    ;

primary returns [Gomantlr_Java_primary p]
@init {
    Gomantlr_Java_primary_2_1_2 p2=null;
    Gomantlr_Java_primary_3_1_1 p3=`Gomantlr_Java_primary_3_1_1_2();
    Gomantlr_Java_primary_7_1_2 p7_2=`Gomantlr_Java_primary_7_1_2_1();
    Gomantlr_Java_primary_7_1_3 p7_3=`Gomantlr_Java_primary_7_1_3_2();
    Gomantlr_Java_primary_8_1_2 p8=`Gomantlr_Java_primary_8_1_2_1();
}
    :	    pe=parExpression
            {
                p=`Gomantlr_Java_primary_1(pe);
            }
        |   
            nwta=nonWildcardTypeArguments
            (
                    egis=explicitGenericInvocationSuffix 
                    {
                        p2=`Gomantlr_Java_primary_2_1_2_1(egis);
                    }
                |
                    'this' a=arguments
                    {
                        p2=`Gomantlr_Java_primary_2_1_2_2(Gomantlr_Java_primary_2_1_2_2_1(a));
                    }
            )
            {
                p=`Gomantlr_Java_primary_2(Gomantlr_Java_primary_2_1(nwta,p2));
            }
        |
            'this' 
            (
                a=arguments
                {
                    p3=`Gomantlr_Java_primary_3_1_1_1(a);
                }
            )?
            {
                p=`Gomantlr_Java_primary_3(Gomantlr_Java_primary_3_1(p3));
            }
        |
            'super' sup=superSuffix
            {
                p=`Gomantlr_Java_primary_4(Gomantlr_Java_primary_4_1(sup));
            }
        |
            l=literal
            {
                p=`Gomantlr_Java_primary_5(l);
            }
        |
            'new' c=creator
            {
                p=`Gomantlr_Java_primary_6(Gomantlr_Java_primary_6_1(c));
            }
        |
            i1=Identifier 
            (
                '.' i2=Identifier
                {
                    p7_2=`Gomantlr_Java_primary_7_1_2_1(p7_2*,Gomantlr_Java_primary_7_1_2_1_1(Gomantlr_Java_Identifier(i2.getText())));
                }
            )* 
            (
                is=identifierSuffix
                {
                    p7_3=`Gomantlr_Java_primary_7_1_3_1(is);
                }
            )?
            {
                p=`Gomantlr_Java_primary_7(Gomantlr_Java_primary_7_1(Gomantlr_Java_Identifier(i1.getText()),p7_2,p7_3));
            }
        |
            pt=primitiveType 
            (
                '[' ']'
                {
                    p8=`Gomantlr_Java_primary_8_1_2_1(p8*,Gomantlr_Java_primary_8_1_2_1_1());
                }
            )*
            '.' 'class'
            {
                p=`Gomantlr_Java_primary_8(Gomantlr_Java_primary_8_1(pt,p8));
            }
        |
            'void' '.' 'class'
            {
                p=`Gomantlr_Java_primary_9(Gomantlr_Java_primary_9_1());
            }
	;

identifierSuffix returns [Gomantlr_Java_identifierSuffix is]
@init {
    Gomantlr_Java_identifierSuffix_1_1_1 is1=`Gomantlr_Java_identifierSuffix_1_1_1_1();
    Gomantlr_Java_identifierSuffix_2_1 is2=`Gomantlr_Java_identifierSuffix_2_1_1();
    Gomantlr_Java_identifierSuffix_8_1_1 is8=`Gomantlr_Java_identifierSuffix_8_1_1_2();
}
	:	    (
                '[' ']'
                {
                    is1=`Gomantlr_Java_identifierSuffix_1_1_1_1(is1*,Gomantlr_Java_identifierSuffix_1_1_1_1_1());
                }
            )+ 
            '.' 'class'
            {
                is=`Gomantlr_Java_identifierSuffix_1(Gomantlr_Java_identifierSuffix_1_1(is1));
            }
	    |	
            (
                '[' e=expression ']'
                {
                    is2=`Gomantlr_Java_identifierSuffix_2_1_1(is2*,Gomantlr_Java_identifierSuffix_2_1_1_1(e));
                }
            )+ // can also be matched by selector, but do here
            {
                is=`Gomantlr_Java_identifierSuffix_2(is2);
            }
        |   
            a=arguments
            {
                is=`Gomantlr_Java_identifierSuffix_3(a);
            }
        |
            '.' 'class'
            {
                is=`Gomantlr_Java_identifierSuffix_4(Gomantlr_Java_identifierSuffix_4_1());
            }
        |
            '.' egi=explicitGenericInvocation
            {
                is=`Gomantlr_Java_identifierSuffix_5(Gomantlr_Java_identifierSuffix_5_1(egi));
            }
        |
            '.' 'this'
            {
                is=`Gomantlr_Java_identifierSuffix_6(Gomantlr_Java_identifierSuffix_6_1());
            }
        |
            '.' 'super' a=arguments
            {
                is=`Gomantlr_Java_identifierSuffix_7(Gomantlr_Java_identifierSuffix_7_1(a));
            }
        |
            '.' 'new' 
            (
                nwta=nonWildcardTypeArguments
                {
                    is8=`Gomantlr_Java_identifierSuffix_8_1_1_1(nwta);
                }
            )?
            ic=innerCreator
            {
                is=`Gomantlr_Java_identifierSuffix_8(Gomantlr_Java_identifierSuffix_8_1(is8,ic));
            }
	;

creator returns [Gomantlr_Java_creator c]
@init {
    Gomantlr_Java_creator_1 c1=`Gomantlr_Java_creator_1_2();
    Gomantlr_Java_creator_3 c3=null;
}
	:	(
            nwta=nonWildcardTypeArguments
            {
                c1=`Gomantlr_Java_creator_1_1(nwta);
            }
        )?
        cn=createdName
        (
                acr=arrayCreatorRest 
                {
                    c3=`Gomantlr_Java_creator_3_1(acr);
                }
            |
                ccr=classCreatorRest
                {
                    c3=`Gomantlr_Java_creator_3_2(ccr);
                }
        )
        {
            c=`Gomantlr_Java_creator(c1,cn,c3);
        }
	;

createdName returns [Gomantlr_Java_createdName cn]
@init {
    Gomantlr_Java_createdName_1_1_2 cn2=`Gomantlr_Java_createdName_1_1_2_2();
    Gomantlr_Java_createdName_1_1_3 cn1=`Gomantlr_Java_createdName_1_1_3_1();
    Gomantlr_Java_createdName_1_1_3_1_1_2 cn3=`Gomantlr_Java_createdName_1_1_3_1_1_2_2();
}
	:	    i=Identifier 
            (
                nwta1=nonWildcardTypeArguments
                {
                    cn2=`Gomantlr_Java_createdName_1_1_2_1(nwta1);
                }
            )?
            (
                '.' i=Identifier 
                (
                    nwta2=nonWildcardTypeArguments
                    {
                        cn3=`Gomantlr_Java_createdName_1_1_3_1_1_2_1(nwta2);
                    }
                )?
                {
                    cn1=`Gomantlr_Java_createdName_1_1_3_1(cn1*,Gomantlr_Java_createdName_1_1_3_1_1(Gomantlr_Java_Identifier(i.getText()),cn3));
                }
            )*
            {
                cn=`Gomantlr_Java_createdName_1(Gomantlr_Java_createdName_1_1(Gomantlr_Java_Identifier(i.getText()),cn2,cn1));
            }
    |
            pt=primitiveType
            {
                cn=`Gomantlr_Java_createdName_2(pt);
            }
	;
	
innerCreator returns [Gomantlr_Java_innerCreator ic]
	:	i=Identifier ccr=classCreatorRest
        {
            ic=`Gomantlr_Java_innerCreator(Gomantlr_Java_Identifier(i.getText()),ccr);
        }
	;

arrayCreatorRest returns [Gomantlr_Java_arrayCreatorRest acr]
@init {
    Gomantlr_Java_arrayCreatorRest_1_1_1_1 acr1=`Gomantlr_Java_arrayCreatorRest_1_1_1_1_1();
    Gomantlr_Java_arrayCreatorRest_1_2_1_2 acr2=`Gomantlr_Java_arrayCreatorRest_1_2_1_2_1();
    Gomantlr_Java_arrayCreatorRest_1_2_1_3 acr3=`Gomantlr_Java_arrayCreatorRest_1_2_1_3_1();
}
	:	'['
        (
                ']' 
                (
                    '[' ']' 
                    {
                        acr1=`Gomantlr_Java_arrayCreatorRest_1_1_1_1_1(acr1*,Gomantlr_Java_arrayCreatorRest_1_1_1_1_1_1()); 
                    } 
                )*
                a=arrayInitializer
                {
                    acr=`Gomantlr_Java_arrayCreatorRest(Gomantlr_Java_arrayCreatorRest_1_1(Gomantlr_Java_arrayCreatorRest_1_1_1(acr1,a)));
                }
            | 
                e=expression ']' 
                (
                    '[' e1=expression ']'
                    {
                        acr2=`Gomantlr_Java_arrayCreatorRest_1_2_1_2_1(acr2*,Gomantlr_Java_arrayCreatorRest_1_2_1_2_1_1(e1));
                    }
                )*
                (
                    '[' ']'
                    {
                        acr3=`Gomantlr_Java_arrayCreatorRest_1_2_1_3_1(acr3*,Gomantlr_Java_arrayCreatorRest_1_2_1_3_1_1());
                    }
                )*
                {
                    acr=`Gomantlr_Java_arrayCreatorRest(Gomantlr_Java_arrayCreatorRest_1_2(Gomantlr_Java_arrayCreatorRest_1_2_1(e,acr2,acr3)));
                }
        )
	;

classCreatorRest returns [Gomantlr_Java_classCreatorRest ccr]
@init {
    Gomantlr_Java_classCreatorRest_2 ccr2=`Gomantlr_Java_classCreatorRest_2_2();
}
	:	a=arguments 
        (
            cb=classBody
            {
                ccr2=`Gomantlr_Java_classCreatorRest_2_1(cb);
            }
        )?
        {
            ccr=`Gomantlr_Java_classCreatorRest(a,ccr2);
        }
	;
	
explicitGenericInvocation returns [Gomantlr_Java_explicitGenericInvocation egi]
	:	nwta=nonWildcardTypeArguments egis=explicitGenericInvocationSuffix
        {
            egi=`Gomantlr_Java_explicitGenericInvocation(nwta,egis);
        }
	;
	
nonWildcardTypeArguments returns [Gomantlr_Java_nonWildcardTypeArguments nwta]
	:	'<' t=typeList '>'
        {
            nwta=`Gomantlr_Java_nonWildcardTypeArguments(t);
        }
	;
	
explicitGenericInvocationSuffix returns [Gomantlr_Java_explicitGenericInvocationSuffix egis]
	:	    'super' s=superSuffix
            {
                egis=`Gomantlr_Java_explicitGenericInvocationSuffix_1(Gomantlr_Java_explicitGenericInvocationSuffix_1_1(s));
            }
	    |
            i=Identifier a=arguments
            {
                egis=`Gomantlr_Java_explicitGenericInvocationSuffix_2(Gomantlr_Java_explicitGenericInvocationSuffix_2_1(Gomantlr_Java_Identifier(i.getText()),a));
            }
 	;
	
selector returns [Gomantlr_Java_selector s]
@init {
    Gomantlr_Java_selector_1_1_2 sel1=`Gomantlr_Java_selector_1_1_2_2();
    Gomantlr_Java_selector_4_1_1 sel4=`Gomantlr_Java_selector_4_1_1_2();
}
	:	    '.' i=Identifier 
            (
                a=arguments
                {
                    sel1=`Gomantlr_Java_selector_1_1_2_1(a);
                }
            )?
            {
                s=`Gomantlr_Java_selector_1(Gomantlr_Java_selector_1_1(Gomantlr_Java_Identifier(i.getText()),sel1));
            }
	    |
            '.' 'this'
            {
                s=`Gomantlr_Java_selector_2(Gomantlr_Java_selector_2_1());
            } 
	    |
            '.' 'super' s2=superSuffix
            {
                s=`Gomantlr_Java_selector_3(Gomantlr_Java_selector_3_1(s2));
            }
	    |
            '.' 'new' 
            (
                nwta=nonWildcardTypeArguments
                {
                    sel4=`Gomantlr_Java_selector_4_1_1_1(nwta);
                }
            )?
            ic=innerCreator
            {
                s=`Gomantlr_Java_selector_4(Gomantlr_Java_selector_4_1(sel4,ic));
            }
	    |
            '[' e=expression ']'
            {
                s=`Gomantlr_Java_selector_5(Gomantlr_Java_selector_5_1(e));
            }
	;
	
superSuffix returns [Gomantlr_Java_superSuffix sup]
@init {
    Gomantlr_Java_superSuffix_2_1_2 s=`Gomantlr_Java_superSuffix_2_1_2_2();
}
	:	    a=arguments
            {
                sup=`Gomantlr_Java_superSuffix_1(a);
            }
	    | 
            '.' i=Identifier 
            (
                a=arguments
                {
                    s=`Gomantlr_Java_superSuffix_2_1_2_1(a);
                }
            )?
            {
                sup=`Gomantlr_Java_superSuffix_2(Gomantlr_Java_superSuffix_2_1(Gomantlr_Java_Identifier(i.getText()),s));
            }
        
    ;

arguments returns [Gomantlr_Java_arguments a]
@init {
    Gomantlr_Java_arguments_1 a1=`Gomantlr_Java_arguments_1_2();
}
	:	'('
        (
            el=expressionList
            {
                a1=`Gomantlr_Java_arguments_1_1(el);
            }
        )? ')'
        {
            a=`Gomantlr_Java_arguments(a1);
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
