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
	:	annotations?
		packageDeclaration?
        importDeclaration*
        typeDeclaration*
        { cu=null; }
	;

packageDeclaration returns [Gomantlr_Java_packageDeclaration pd]
	:	'package' qualifiedName ';'
	;
	
importDeclaration returns [Gomantlr_Java_importDeclaration id]
	:	'import' 'static'? i1=Identifier ('.' i2=Identifier { System.out.println("i2:"+i2.getText()); })* ('.' '*')? ';'
        {
            System.out.println("i1:"+i1.getText());
        }
	;
	
typeDeclaration returns [Gomantlr_Java_typeDeclaration td]
	:	classOrInterfaceDeclaration
    |   ';'
	;
	
classOrInterfaceDeclaration returns [Gomantlr_Java_classOrInterfaceDeclaration coid]
	:	modifier* (classDeclaration | interfaceDeclaration)
	;
	
classDeclaration returns [Gomantlr_Java_classDeclaration cd]
	:	normalClassDeclaration
    |   enumDeclaration
	;
	
normalClassDeclaration returns [Gomantlr_Java_normalClassDeclaration ncd]
	:	'class' i=Identifier (typeParameters)?
        ('extends' type)?
        ('implements' typeList)?
        classBody
        { 
            Gomantlr_Java_Identifier gji=`Gomantlr_Java_Identifier(i.getText()); 
            System.out.println(gji);
        }
	;
	
typeParameters returns [Gomantlr_Java_typeParameters tp]
	:	'<' typeParameter (',' typeParameter)* '>'
	;

typeParameter returns [Gomantlr_Java_typeParameter tp]
	:	Identifier ('extends' bound)?
	;
		
bound returns [Gomantlr_Java_bound b]
	:	type ('&' type)*
	;

enumDeclaration returns [Gomantlr_Java_enumDeclaration ed]
	:	ENUM Identifier ('implements' typeList)? enumBody
	;
	
enumBody returns [Gomantlr_Java_enumBody eb]
	:	'{' enumConstants? ','? enumBodyDeclarations? '}'
	;

enumConstants returns [Gomantlr_Java_enumConstants ec]
	:	enumConstant (',' enumConstant)*
	;
	
enumConstant returns [Gomantlr_Java_enumConstant ec]
	:	annotations? Identifier (arguments)? (classBody)?
	;
	
enumBodyDeclarations returns [Gomantlr_Java_enumBodyDeclarations ebd]
	:	';' (classBodyDeclaration)*
	;
	
interfaceDeclaration returns [Gomantlr_Java_interfaceDeclaration id]
	:	normalInterfaceDeclaration
		| annotationTypeDeclaration
	;
	
normalInterfaceDeclaration returns [Gomantlr_Java_normalInterfaceDeclaration nid]
	:	'interface' Identifier typeParameters? ('extends' typeList)? interfaceBody
	;
	
typeList returns [Gomantlr_Java_typeList tl]
	:	type (',' type)*
	;
	
classBody returns [Gomantlr_Java_classBody cb]
	:	'{' classBodyDeclaration* '}'
	;
	
interfaceBody returns [Gomantlr_Java_interfaceBody ib]
	:	'{' interfaceBodyDeclaration* '}'
	;

classBodyDeclaration returns [Gomantlr_Java_classBodyDeclaration ebd]
	:	';'
	|	'static'? block
	|	modifier* memberDecl
	;
	
memberDecl returns [Gomantlr_Java_memberDecl md]
	:	genericMethodOrConstructorDecl
	|	methodDeclaration
	|	fieldDeclaration
	|	'void' Identifier voidMethodDeclaratorRest
	|	Identifier constructorDeclaratorRest
	|	interfaceDeclaration
	|	classDeclaration
	;
	
genericMethodOrConstructorDecl returns [Gomantlr_Java_genericMethodOrConstructorDecl gmocd]
	:	typeParameters genericMethodOrConstructorRest
	;
	
genericMethodOrConstructorRest returns [Gomantlr_Java_genericMethodOrConstructorRest gmocr]
	:	(type | 'void') Identifier methodDeclaratorRest
	|	Identifier constructorDeclaratorRest
	;

methodDeclaration returns [Gomantlr_Java_methodDeclaration md]
	:	type Identifier methodDeclaratorRest
	;

fieldDeclaration returns [Gomantlr_Java_fieldDeclaration fd]
	:	type variableDeclarators ';'
	;
		
interfaceBodyDeclaration returns [Gomantlr_Java_interfaceBodyDeclaration ibd]
	:	modifier* interfaceMemberDecl
	|   ';'
	;

interfaceMemberDecl returns [Gomantlr_Java_interfaceMemberDecl imd]
	:	interfaceMethodOrFieldDecl
	|   interfaceGenericMethodDecl
    |   'void' Identifier voidInterfaceMethodDeclaratorRest
    |   interfaceDeclaration
    |   classDeclaration
	;
	
interfaceMethodOrFieldDecl returns [Gomantlr_Java_interfaceMethodOrFieldDecl imofd]
	:	type Identifier interfaceMethodOrFieldRest
	;
	
interfaceMethodOrFieldRest returns [Gomantlr_Java_interfaceMethodOrFieldRest imofr]
	:	constantDeclaratorsRest ';'
	|	interfaceMethodDeclaratorRest
	;
	
methodDeclaratorRest returns [Gomantlr_Java_methodDeclaratorRest mdr]
	:	formalParameters ('[' ']')*
        ('throws' qualifiedNameList)?
        (   methodBody
        |   ';'
        )
	;
	
voidMethodDeclaratorRest returns [Gomantlr_Java_voidMethodDeclaratorRest vmd]
	:	formalParameters ('throws' qualifiedNameList)?
        (   methodBody
        |   ';'
        )
	;
	
interfaceMethodDeclaratorRest returns [Gomantlr_Java_interfaceMethodDeclaratorRest imdr]
	:	formalParameters ('[' ']')* ('throws' qualifiedNameList)? ';'
	;
	
interfaceGenericMethodDecl returns [Gomantlr_Java_interfaceGenericMethodDecl igmd]
	:	typeParameters (type | 'void') Identifier
        interfaceMethodDeclaratorRest
	;
	
voidInterfaceMethodDeclaratorRest returns [Gomantlr_Java_voidInterfaceMethodDeclaratorRest vimd]
	:	formalParameters ('throws' qualifiedNameList)? ';'
	;
	
constructorDeclaratorRest returns [Gomantlr_Java_constructorDeclaratorRest cdr]
	:	formalParameters ('throws' qualifiedNameList)? methodBody
	;

constantDeclarator returns [Gomantlr_Java_constantDeclarator cd]
	:	Identifier constantDeclaratorRest
	;
	
variableDeclarators returns [Gomantlr_Java_variableDeclarators vd]
	:	variableDeclarator (',' variableDeclarator)*
	;

variableDeclarator returns [Gomantlr_Java_variableDeclarator vd]
	:	Identifier variableDeclaratorRest
	;
	
variableDeclaratorRest returns [Gomantlr_Java_variableDeclaratorRest vdr]
	:	('[' ']')+ ('=' variableInitializer)?
	|	'=' variableInitializer
	|
	;
	
constantDeclaratorsRest returns [Gomantlr_Java_constantDeclaratorsRest cdr]
    :   constantDeclaratorRest (',' constantDeclarator)*
    ;

constantDeclaratorRest returns [Gomantlr_Java_constantDeclaratorRest cdr]
	:	('[' ']')* '=' variableInitializer
	;
	
variableDeclaratorId returns [Gomantlr_Java_variableDeclaratorId vdi]
	:	Identifier ('[' ']')*
	;

variableInitializer returns [Gomantlr_Java_variableInitializer vi]
	:	arrayInitializer
    |   expression
	;
	
arrayInitializer returns [Gomantlr_Java_arrayInitializer ai]
	:	'{' (variableInitializer (',' variableInitializer)* (',')? )? '}'
	;

modifier returns [Gomantlr_Java_modifier m]
    :   annotation
    |   'public'
    |   'protected'
    |   'private'
    |   'static'
    |   'abstract'
    |   'final'
    |   'native'
    |   'synchronized'
    |   'transient'
    |   'volatile'
    |   'strictfp'
    ;

packageOrTypeName returns [Gomantlr_Java_packageOrTypeName potn]
	:	Identifier ('.' Identifier)*
	;

enumConstantName returns [Gomantlr_Java_enumConstantName ecn]
    :   i=Identifier
        {
            ecn=`Gomantlr_Java_enumConstantName(Gomantlr_Java_Identifier(i.getText()));
        }
    ;

typeName returns [Gomantlr_Java_typeName tn]
	:   Identifier
    |   packageOrTypeName '.' Identifier
	;

type returns [Gomantlr_Java_type t]
@init {
    Gomantlr_Java_type_2_1_2 gt=`Gomantlr_Java_type_2_1_2_1();
}
	:	Identifier (typeArguments)? ('.' Identifier (typeArguments)? )* ('[' ']')*
	|	pt=primitiveType
        ('[' ']' { gt=`Gomantlr_Java_type_2_1_2_1(gt*,Gomantlr_Java_type_2_1_2_1_1()); } )*
        {
            t=`Gomantlr_Java_type_2(Gomantlr_Java_type_2_1(pt,gt));
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
    |   annotation
	;

typeArguments returns [Gomantlr_Java_typeArguments ta]
	:	'<' typeArgument (',' typeArgument)* '>'
	;
	
typeArgument returns [Gomantlr_Java_typeArgument ta]
	:	type
	|	'?' (('extends' | 'super') type)?
	;
	
qualifiedNameList returns [Gomantlr_Java_qualifiedNameList qnl]
	:	qualifiedName (',' qualifiedName)*
	;
	
formalParameters returns [Gomantlr_Java_formalParameters fp]
	:	'(' formalParameterDecls? ')'
	;
	
formalParameterDecls returns [Gomantlr_Java_formalParameterDecls fpd]
	:	'final'? annotations? type formalParameterDeclsRest?
	;
	
formalParameterDeclsRest returns [Gomantlr_Java_formalParameterDeclsRest fpdr]
	:	variableDeclaratorId (',' formalParameterDecls)?
	|   '...' variableDeclaratorId
	;
	
methodBody returns [Gomantlr_Java_methodBody mb]
	:	b=block
        { mb=`Gomantlr_Java_methodBody(b); }
	;

qualifiedName returns [Gomantlr_Java_qualifiedName qn]
@init {
    Gomantlr_Java_qualifiedName_2 qn2=`Gomantlr_Java_qualifiedName_2_1();
}
	:	i1=Identifier 
        (
            '.' i2=Identifier
            {
                qn2=`Gomantlr_Java_qualifiedName_2_1(qn2*,Gomantlr_Java_qualifiedName_2_1_1(Gomantlr_Java_Identifier(i2.getText())));
            }
        )*
        {
            qn=`Gomantlr_Java_qualifiedName(Gomantlr_Java_Identifier(i1.getText()),qn2);
        }
	;
	
literal	returns [Gomantlr_Java_literal l]
	:   il=integerLiteral
        { l=`Gomantlr_Java_literal_1(il); }
    |   fpl=FloatingPointLiteral
        { l=`Gomantlr_Java_literal_2(Gomantlr_Java_FloatingPointLiteral(fpl.getText())); }
    |   cl=CharacterLiteral
        { l=`Gomantlr_Java_literal_3(Gomantlr_Java_CharacterLiteral(cl.getText())); }
    |   sl=StringLiteral
        { l=`Gomantlr_Java_literal_4(Gomantlr_Java_StringLiteral(sl.getText())); }
    |   bl=booleanLiteral
        { l=`Gomantlr_Java_literal_5(bl); }
    |   'null'
        { l=`Gomantlr_Java_literal_6(); }
	;

integerLiteral returns [Gomantlr_Java_integerLiteral il]
    :   h=HexLiteral 
        { il=`Gomantlr_Java_integerLiteral_1(Gomantlr_Java_HexLiteral(h.getText())); }
    |   o=OctalLiteral
        { il=`Gomantlr_Java_integerLiteral_2(Gomantlr_Java_OctalLiteral(o.getText())); }
    |   d=DecimalLiteral
        { il=`Gomantlr_Java_integerLiteral_3(Gomantlr_Java_DecimalLiteral(d.getText())); }
    ;

booleanLiteral returns [Gomantlr_Java_booleanLiteral bl]
    :   'true'
        { bl=`Gomantlr_Java_booleanLiteral_1(); }
    |   'false'
        { bl=`Gomantlr_Java_booleanLiteral_2(); }
    ;

// ANNOTATIONS

annotations returns [Gomantlr_Java_annotations a]
	:	annotation+
	;

annotation returns [Gomantlr_Java_annotation a]
	:	'@' typeName ('(' (Identifier '=')? elementValue ')')?
	;
	
elementValue returns [Gomantlr_Java_elementValue ev]
	:	conditionalExpression
	|   annotation
	|   elementValueArrayInitializer
	;
	
elementValueArrayInitializer returns [Gomantlr_Java_elementValueArrayInitializer evai]
	:	'{' (elementValue)? (',')? '}'
	;
	
annotationTypeDeclaration returns [Gomantlr_Java_annotationTypeDeclaration atd]
	:	'@' 'interface' Identifier annotationTypeBody
	;
	
annotationTypeBody returns [Gomantlr_Java_annotationTypeBody atb]
	:	'{' (annotationTypeElementDeclarations)? '}'
	;
	
annotationTypeElementDeclarations returns [Gomantlr_Java_annotationTypeElementDeclarations ated]
	:	(annotationTypeElementDeclaration) (annotationTypeElementDeclaration)*
	;
	
annotationTypeElementDeclaration returns [Gomantlr_Java_annotationTypeElementDeclaration ated]
	:	(modifier)* annotationTypeElementRest
	;
	
annotationTypeElementRest returns [Gomantlr_Java_annotationTypeElementRest ater]
	:	type Identifier annotationMethodOrConstantRest ';'
	|   classDeclaration
	|   interfaceDeclaration
	|   enumDeclaration
	|   annotationTypeDeclaration
	;
	
annotationMethodOrConstantRest returns [Gomantlr_Java_annotationMethodOrConstantRest amocr]
	:	annotationMethodRest
	|   annotationConstantRest
	;
	
annotationMethodRest returns [Gomantlr_Java_annotationMethodRest amr]
 	:	'(' ')' (defaultValue)?
 	;
 	
annotationConstantRest returns [Gomantlr_Java_annotationConstantRest acr]
 	:	variableDeclarators
 	;
 	
defaultValue returns [Gomantlr_Java_defaultValue dv]
 	:	'default' elementValue
 	;

// STATEMENTS / BLOCKS

block returns [Gomantlr_Java_block b]
	:	'{' blockStatement* '}'
	;
	
blockStatement returns [Gomantlr_Java_blockStatement bs]
	:	localVariableDeclaration
    |   classOrInterfaceDeclaration
    |   statement
	;
	
localVariableDeclaration returns [Gomantlr_Java_localVariableDeclaration lvd]
	:	('final')? type variableDeclarators ';'
	;
	
statement returns [Gomantlr_Java_statement s]
	: block
    | 'assert' expression (':' expression)? ';'
    | 'if' parExpression statement ('else' statement)?
    | 'for' '(' forControl ')' statement
    | 'while' parExpression statement
    | 'do' statement 'while' parExpression ';'
    | 'try' block
      (	catches 'finally' block
      | catches
      | 'finally' block
      )
    | 'switch' parExpression '{' switchBlockStatementGroups '}'
    | 'synchronized' parExpression block
    | 'return' expression? ';'
    | 'throw' expression ';'
    | 'break' Identifier? ';'
    | 'continue' Identifier? ';'
    | ';'
    | statementExpression ';'
    | Identifier ':' statement
	;
	
catches returns [Gomantlr_Java_catches c]
	:	catchClause (catchClause)*
	;
	
catchClause returns [Gomantlr_Java_catchClause cc]
	:	'catch' '(' formalParameter ')' block
	;

formalParameter returns [Gomantlr_Java_formalParameter fp]
	:	variableModifier* type variableDeclaratorId
	;
	
switchBlockStatementGroups returns [Gomantlr_Java_switchBlockStatementGroups sbsg]
	:	(switchBlockStatementGroup)*
	;
	
switchBlockStatementGroup returns [Gomantlr_Java_switchBlockStatementGroup sbsg]
	:	switchLabel blockStatement*
	;
	
switchLabel returns [Gomantlr_Java_switchLabel sl]
	:	'case' constantExpression ':'
	|   'case' enumConstantName ':'
	|   'default' ':'
	;
	
moreStatementExpressions returns [Gomantlr_Java_moreStatementExpressions mse]
	:	(',' statementExpression)*
	;

forControl returns [Gomantlr_Java_forControl fc]
	:	    fvc=forVarControl
            {
                fc=`Gomantlr_Java_forControl_1(fvc);
            }
	    |   
            (
                fi=forInit
            )?
            ';'
            (
                e=expression
            )?
            ';'
            (
                fu=forUpdate
            )?
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
