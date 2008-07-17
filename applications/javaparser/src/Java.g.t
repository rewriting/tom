/*
 [The "BSD licence"]
 Copyright (c) 2007-2008 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
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
 *
 *  Version 1.0.2 -- Terence Parr, Nov 27, 2006
 *      java spec I built this from had some bizarre for-loop control.
 *          Looked weird and so I looked elsewhere...Yep, it's messed up.
 *          simplified.
 *
 *  Version 1.0.3 -- Chris Hogue, Feb 26, 2007
 *      Factored out an annotationName rule and used it in the annotation rule.
 *          Not sure why, but typeName wasn't recognizing references to inner
 *          annotations (e.g. @InterfaceName.InnerAnnotation())
 *      Factored out the elementValue section of an annotation reference.  Created 
 *          elementValuePair and elementValuePairs rules, then used them in the 
 *          annotation rule.  Allows it to recognize annotation references with 
 *          multiple, comma separated attributes.
 *      Updated elementValueArrayInitializer so that it allows multiple elements.
 *          (It was only allowing 0 or 1 element).
 *      Updated localVariableDeclaration to allow annotations.  Interestingly the JLS
 *          doesn't appear to indicate this is legal, but it does work as of at least
 *          JDK 1.5.0_06.
 *      Moved the Identifier portion of annotationTypeElementRest to annotationMethodRest.
 *          Because annotationConstantRest already references variableDeclarator which 
 *          has the Identifier portion in it, the parser would fail on constants in 
 *          annotation definitions because it expected two identifiers.  
 *      Added optional trailing ';' to the alternatives in annotationTypeElementRest.
 *          Wouldn't handle an inner interface that has a trailing ';'.
 *      Swapped the expression and type rule reference order in castExpression to 
 *          make it check for genericized casts first.  It was failing to recognize a
 *          statement like  "Class<Byte> TYPE = (Class<Byte>)...;" because it was seeing
 *          'Class<Byte' in the cast expression as a less than expression, then failing 
 *          on the '>'.
 *      Changed createdName to use typeArguments instead of nonWildcardTypeArguments.
 *          Again, JLS doesn't seem to allow this, but java.lang.Class has an example of
 *          of this construct.
 *      Changed the 'this' alternative in primary to allow 'identifierSuffix' rather than
 *          just 'arguments'.  The case it couldn't handle was a call to an explicit
 *          generic method invocation (e.g. this.<E>doSomething()).  Using identifierSuffix
 *          may be overly aggressive--perhaps should create a more constrained thisSuffix rule?
 *      
 *  Version 1.0.4 -- Hiroaki Nakamura, May 3, 2007
 *
 *  Fixed formalParameterDecls, localVariableDeclaration, forInit,
 *  and forVarControl to use variableModifier* not 'final'? (annotation)?
 *
 *  Version 1.0.5 -- Terence, June 21, 2007
 *  --a[i].foo didn't work. Fixed unaryExpression
 *
 *  Version 1.0.6 -- John Ridgway, March 17, 2008
 *      Made "assert" a switchable keyword like "enum".
 *      Fixed compilationUnit to disallow "annotation importDeclaration ...".
 *      Changed "Identifier ('.' Identifier)*" to "qualifiedName" in more 
 *          places.
 *      Changed modifier* and/or variableModifier* to classOrInterfaceModifiers,
 *          modifiers or variableModifiers, as appropriate.
 *      Renamed "bound" to "typeBound" to better match language in the JLS.
 *      Added "memberDeclaration" which rewrites to methodDeclaration or 
 *      fieldDeclaration and pulled type into memberDeclaration.  So we parse 
 *          type and then move on to decide whether we're dealing with a field
 *          or a method.
 *      Modified "constructorDeclaration" to use "constructorBody" instead of
 *          "methodBody".  constructorBody starts with explicitConstructorInvocation,
 *          then goes on to blockStatement*.  Pulling explicitConstructorInvocation
 *          out of expressions allowed me to simplify "primary".
 *      Changed variableDeclarator to simplify it.
 *      Changed type to use classOrInterfaceType, thus simplifying it; of course
 *          I then had to add classOrInterfaceType, but it is used in several 
 *          places.
 *      Fixed annotations, old version allowed "@X(y,z)", which is illegal.
 *      Added optional comma to end of "elementValueArrayInitializer"; as per JLS.
 *      Changed annotationTypeElementRest to use normalClassDeclaration and 
 *          normalInterfaceDeclaration rather than classDeclaration and 
 *          interfaceDeclaration, thus getting rid of a couple of grammar ambiguities.
 *      Split localVariableDeclaration into localVariableDeclarationStatement
 *          (includes the terminating semi-colon) and localVariableDeclaration.  
 *          This allowed me to use localVariableDeclaration in "forInit" clauses,
 *           simplifying them.
 *      Changed switchBlockStatementGroup to use multiple labels.  This adds an
 *          ambiguity, but if one uses appropriately greedy parsing it yields the
 *           parse that is closest to the meaning of the switch statement.
 *      Renamed "forVarControl" to "enhancedForControl" -- JLS language.
 *      Added semantic predicates to test for shift operations rather than other
 *          things.  Thus, for instance, the string "< <" will never be treated
 *          as a left-shift operator.
 *      In "creator" we rule out "nonWildcardTypeArguments" on arrayCreation, 
 *          which are illegal.
 *      Moved "nonWildcardTypeArguments into innerCreator.
 *      Removed 'super' superSuffix from explicitGenericInvocation, since that
 *          is only used in explicitConstructorInvocation at the beginning of a
 *           constructorBody.  (This is part of the simplification of expressions
 *           mentioned earlier.)
 *      Simplified primary (got rid of those things that are only used in
 *          explicitConstructorInvocation).
 *      Lexer -- removed "Exponent?" from FloatingPointLiteral choice 4, since it
 *          led to an ambiguity.
 *
 *      This grammar successfully parses every .java file in the JDK 1.5 source 
 *          tree (excluding those whose file names include '-', which are not
 *          valid Java compilation units).
 *
 *  Known remaining problems:
 *      "Letter" and "JavaIDDigit" are wrong.  The actual specification of
 *      "Letter" should be "a character for which the method
 *      Character.isJavaIdentifierStart(int) returns true."  A "Java 
 *      letter-or-digit is a character for which the method 
 *      Character.isJavaIdentifierPart(int) returns true."
 */
grammar Java;
options {
  backtrack=true;
  memoize=true;
  output=AST;
  ASTLabelType=AstTree;
}

tokens {
  %include { parser/ast/AstTokenList.txt }
}

@header {
package parser;
import parser.ast.AstTree;
}

@lexer::header {
package parser;
import parser.ast.AstTree;
}

@lexer::members {
  protected boolean enumIsKeyword = true;
  protected boolean assertIsKeyword = true;
}

// starting point for parsing a java file
/* The annotations are separated out to make parsing faster, but must be associated with
   a packageDeclaration or a typeDeclaration (and not an empty one). */
compilationUnit
    :   annotations
        (   packageDeclaration importDeclaration* typeDeclaration*
            ->  ^(CompilationUnit
                    annotations
                    packageDeclaration
                    ^(ImportList importDeclaration*)
                    ^(TypeDeclList typeDeclaration*)
                    )
        |   classOrInterfaceDeclaration typeDeclaration*
            ->  ^(CompilationUnit
                    annotations
                    ^(QualifiedName )
                    ^(ImportList )
                    ^(TypeDeclList classOrInterfaceDeclaration typeDeclaration*)
                    )
        )
    |   packageDeclaration? importDeclaration* typeDeclaration*
        ->  ^(CompilationUnit
                ^(AnnotationList )
                packageDeclaration?
                ^(ImportList importDeclaration*)
                ^(TypeDeclList typeDeclaration*)
                )
    ;

packageDeclaration
    :   'package' qualifiedName ';' -> qualifiedName
    ;
    
importDeclaration
    :   'import' 'static'? qualifiedName ('.' '*')? ';'
        ->  ^(Import qualifiedName False False)
    ;
    
typeDeclaration
    :   classOrInterfaceDeclaration -> classOrInterfaceDeclaration
    |   ';' -> EmptyTypeDecl
    ;
    
classOrInterfaceDeclaration
    :   classOrInterfaceModifiers
        (    classDeclaration -> classDeclaration
        |    interfaceDeclaration -> interfaceDeclaration
        )
    ;
    
classOrInterfaceModifiers
    :   classOrInterfaceModifier* -> ^(ModifierList classOrInterfaceModifier*)
    ;

classOrInterfaceModifier
    :   annotation -> ^(AnnotationToModifier annotation)
    |   'public'    -> Public
    |   'protected' -> Protected
    |   'private'   -> Private
    |   'abstract'  -> Abstract
    |   'static'    -> Static
    |   'final'     -> Final
    |   'strictfp'  -> StrictFP
    ;

modifiers
    :   modifier* -> ^(ModifierList modifier* )
    ;

classDeclaration
    :   normalClassDeclaration -> normalClassDeclaration
    |   enumDeclaration -> enumDeclaration
    ;
    
normalClassDeclaration
    :   'class' Identifier typeParameters?
        ('extends' type)?
        ('implements' typeList)?
        classBody
        ->  ^(NormalClass
                Identifier
                ^(ModifierList )
                typeParameters
                type
                typeList
                classBody
                )
    ;
    
typeParameters
    :   '<' l+=typeParameter (',' l+=typeParameter)* '>'
        ->  ^(TypeList $l+)
    ;

typeParameter
    :   Identifier ('extends' typeBound)?
        ->  ^(TypeParameter Identifier typeBound)
    ;
        
typeBound
    :   type ('&' type)* -> ^(TypeList type+)
    ;

enumDeclaration
    :   ENUM Identifier ('implements' typeList)? enumBody
        ->  ^(EnumClass Identifier ^(ModifierList ) typeList enumBody )
    ;

enumBody
    :   '{' enumConstants? ','? enumBodyDeclarations? '}'
        ->  ^(EnumBody enumConstants enumBodyDeclarations)
    ;

enumConstants
    :   l+=enumConstant (',' l+=enumConstant)* -> ^(EnumConstantList $l+)
    ;
    
enumConstant
    :   annotations? Identifier arguments? classBody?
        ->  ^(EnumConstant Identifier annotations arguments classBody)
    ;
    
enumBodyDeclarations
    :   ';' (classBodyDeclaration)*
        ->  ^(ClassBodyDeclList classBodyDeclaration*)
    ;
    
interfaceDeclaration
    :   normalInterfaceDeclaration -> normalInterfaceDeclaration
    |   annotationTypeDeclaration -> annotationTypeDeclaration
    ;
    
normalInterfaceDeclaration
    :   'interface' Identifier typeParameters? ('extends' typeList)? interfaceBody
        ->  ^(NormalInterface
                Identifier
                ^(ModifierList )
                typeParameters
                typeList
                interfaceBody
                )
    ;
    
typeList
    :   type (',' type)* -> ^(TypeList type+)
    ;
    
classBody
    :   '{' classBodyDeclaration* '}' -> ^(ClassBodyDeclList classBodyDeclaration*)
    ;
    
interfaceBody
    :   '{' interfaceBodyDeclaration* '}'
        ->  ^(InterfaceBodyDeclList interfaceBodyDeclaration*)
    ;

classBodyDeclaration
    :   ';'
    |   'static'? block -> ^(BlockToClassBodyDecl block False)
    |   modifiers memberDecl -> memberDecl
    ;
    
memberDecl
    :   genericMethodOrConstructorDecl
        ->  genericMethodOrConstructorDecl
    |   memberDeclaration
        ->  memberDeclaration
    |   'void' Identifier voidMethodDeclaratorRest
        ->  ^(MethodDecl
                ^(ModifierList )
                ^(TypeParameterList )
                Void
                Identifier
                ^(FormalParameterDeclList )
                ^(QualifiedNameList )
                ^(BlockStatementList )
                )
    |   Identifier constructorDeclaratorRest
        ->  ^(ConstructorDecl
                ^(ModifierList )
                ^(TypeParameterList )
                Identifier
                ^(FormalParameterDeclList )
                ^(QualifiedNameList )
                ^(ConstructorBody
                    ^(NoExplicitConstructorInvocation )
                    ^(BlockStatementList )
                    )
                )
    |   interfaceDeclaration
        ->  ^(TypeDeclToClassBodyDecl ^(ModifierList ) interfaceDeclaration)
    |   classDeclaration
        ->  ^(TypeDeclToClassBodyDecl ^(ModifierList ) classDeclaration)
    ;
    
memberDeclaration
    :   type
        (   methodDeclaration -> methodDeclaration
        |   fieldDeclaration -> fieldDeclaration
        )
    ;

genericMethodOrConstructorDecl
    :   typeParameters genericMethodOrConstructorRest
        -> genericMethodOrConstructorRest
    ;
    
genericMethodOrConstructorRest
    :   (type | 'void') Identifier methodDeclaratorRest
        ->  ^(MethodDecl
                ^(ModifierList )
                ^(TypeParameterList )
                Void
                Identifier
                ^(FormalParameterDeclList )
                ^(QualifiedNameList )
                ^(BlockStatementList )
                )
    |   Identifier constructorDeclaratorRest
        ->  ^(ConstructorDecl
                ^(ModifierList )
                ^(TypeParameterList )
                Identifier
                ^(FormalParameterDeclList )
                ^(QualifiedNameList )
                ^(ConstructorBody
                    ^(NoExplicitConstructorInvocation )
                    ^(BlockStatementList )
                    )
                )
    ;

methodDeclaration
    :   Identifier methodDeclaratorRest
        ->  ^(MethodDecl
                ^(ModifierList )
                ^(TypeParameterList )
                Void
                Identifier
                ^(FormalParameterDeclList )
                ^(QualifiedNameList )
                ^(BlockStatementList )
                )
    ;

fieldDeclaration
    :   variableDeclarators ';'
        ->  ^(VariablesDeclToClassBodyDecl ^(VariablesDecl ^(ModifierList ) variableDeclarators))
    ;
        
interfaceBodyDeclaration
    :   modifiers interfaceMemberDecl -> interfaceMemberDecl
    |   ';'
    ;

interfaceMemberDecl
    :   interfaceMethodOrFieldDecl
        ->  interfaceMethodOrFieldDecl
    |   interfaceGenericMethodDecl
        ->  interfaceGenericMethodDecl
    |   'void' Identifier voidInterfaceMethodDeclaratorRest
        ->  ^(InterfaceMethodDecl
                ^(ModifierList )
                ^(TypeParameterList )
                Void
                Identifier
                ^(FormalParameterDeclList )
                ^(QualifiedNameList )
                )
    |   interfaceDeclaration
        ->  ^(TypeDeclToInterfaceBodyDecl interfaceDeclaration)
    |   classDeclaration
        ->  ^(TypeDeclToInterfaceBodyDecl classDeclaration)
    ;
    
interfaceMethodOrFieldDecl
    :   type Identifier interfaceMethodOrFieldRest
        ->  ^(ConstantDecl ^(ModifierList ) type Identifier ^(ArrayInitializer ))
    ;
    
interfaceMethodOrFieldRest
    :   constantDeclaratorsRest ';' -> constantDeclaratorsRest
    |   interfaceMethodDeclaratorRest -> interfaceMethodDeclaratorRest
    ;
    
methodDeclaratorRest
    :   formalParameters ('[' ']')*
        ('throws' qualifiedNameList)?
        (   methodBody
        |   ';'
        )
    ;
    
voidMethodDeclaratorRest
    :   formalParameters ('throws' qualifiedNameList)?
        (   methodBody
        |   ';'
        )
    ;
    
interfaceMethodDeclaratorRest
    :   formalParameters ('[' ']')* ('throws' qualifiedNameList)? ';'
    ;
    
interfaceGenericMethodDecl
    :   typeParameters (type | 'void') Identifier
        interfaceMethodDeclaratorRest
        ->  ^(InterfaceMethodDecl
                ^(ModifierList )
                typeParameters
                type
                Identifier
                ^(FormalParameterDeclList )
                ^(QualifiedNameList )
                ^(BlockStatementList )
                )
    ;
    
voidInterfaceMethodDeclaratorRest
    :   formalParameters ('throws' qualifiedNameList)? ';'
    ;
    
constructorDeclaratorRest
    :   formalParameters ('throws' qualifiedNameList)? constructorBody
    ;

constantDeclarator
    :   Identifier constantDeclaratorRest
        ->  ^(ConstantDecl ^(ModifierList ) Void Identifier ^(ArrayInitializer ))
    ;
    
variableDeclarators
    :   variableDeclarator (',' variableDeclarator)*
        ->  ^(VariableDeclList variableDeclarator+)
    ;

variableDeclarator
    :   variableDeclaratorId ('=' variableInitializer)?
        ->  ^(VariableDecl Void variableDeclaratorId NoVariableInitializer)
    ;
    
constantDeclaratorsRest
    :   constantDeclaratorRest (',' constantDeclarator)*
    ;

constantDeclaratorRest
    :   ('[' ']')* '=' variableInitializer
    ;
    
variableDeclaratorId
    :   Identifier ('[' ']')* -> Identifier
    ;

variableInitializer
    :   arrayInitializer -> arrayInitializer
    |   expression -> ^(ExpressionToVariableInitializer expression)
    ;
        
arrayInitializer
    :   '{' (l+=variableInitializer (',' l+=variableInitializer)* (',')? )? '}'
        ->  ^(ArrayInitializer $l+)
    ;

modifier
    :   annotation -> ^(AnnotationToModifier annotation)
    |   'public'       -> Public
    |   'protected'    -> Protected
    |   'private'      -> Private
    |   'static'       -> Static
    |   'abstract'     -> Abstract
    |   'final'        -> Final
    |   'native'       -> Native
    |   'synchronized' -> SynchronizedModifier
    |   'transient'    -> Transient
    |   'volatile'     -> Volatile
    |   'strictfp'     -> StrictFP
    ;

packageOrTypeName
    :   qualifiedName -> qualifiedName
    ;

enumConstantName
    :   Identifier -> Identifier
    ;

typeName
    :   qualifiedName -> qualifiedName
    ;

type
    :   classOrInterfaceType ('[' ']')*
        ->  ^(ClassOrInterfaceType classOrInterfaceType)
    |   primitiveType ('[' ']')*
        ->  ^(PrimitiveType primitiveType)
    ;

classOrInterfaceType
    :    Identifier typeArguments? ('.' Identifier typeArguments? )*
         ->  ^(IndependentType Identifier ^(TypeArgumentList ))
    ;

primitiveType
    :   'boolean' -> Boolean
    |   'char'    -> Char
    |   'byte'    -> Byte
    |   'short'   -> Short
    |   'int'     -> Int
    |   'long'    -> Long
    |   'float'   -> Float
    |   'double'  -> Double
    ;

variableModifier
    :   'final' -> Final
    |   annotation ^(AnnotationToModifier annotation)
    ;

typeArguments
    :   '<' typeArgument (',' typeArgument)* '>'
        ->  ^(TypeArgumentList typeArgument+)
    ;
    
typeArgument
    :(   type
    |   '?' (( 'extends'// -> ^(ExtendTypeArgument Void)
             | 'super'  // -> ^(SuperTypeArgument Void)
             ) type)?) -> ^(ExplicitTypeArgument Void)
    ;
    
qualifiedNameList
    :   qualifiedName (',' qualifiedName)* -> ^(QualifiedNameList qualifiedName+)
    ;

formalParameters
    :   '(' formalParameterDecls? ')' -> ^(FormalParameterDeclList )
    ;
    
formalParameterDecls
    :   variableModifiers type formalParameterDeclsRest
    ;
    
formalParameterDeclsRest
   :(   variableDeclaratorId (',' formalParameterDecls)?
    |   '...' variableDeclaratorId
    )   ->  ^(FormalParameterDecl
                ^(ModifierList )
                Void
                variableDeclaratorId
                False
                )
    ;
    
methodBody
    :   block -> block
    ;

constructorBody
    :   '{' explicitConstructorInvocation? blockStatement* '}'
        ->  ^(ConstructorBody
                NoExplicitConstructorInvocation
                ^(BlockStatementList blockStatement*)
                )
    ;

explicitConstructorInvocation
    :   nonWildcardTypeArguments?
        (   'this'  -> ^(ThisInvocation ^(TypeList ) ^(ExpressionList ))
        |   'super' -> ^(SuperInvocation ^(TypeList ) ^(ExpressionList ))
        ) arguments ';'
    |   primary '.' nonWildcardTypeArguments? 'super' arguments ';'
        ->  ^(PrimarySuperInvocation primary nonWildcardTypeArguments arguments)
    ;


qualifiedName
    :   Identifier ('.' Identifier)* -> ^(QualifiedName Identifier+)
    ;
    
literal 
    :   integerLiteral -> integerLiteral
    |   l=FloatingPointLiteral -> ^(FloatingPointLiteral $l)
    |   l=CharacterLiteral -> ^(CharacterLiteral $l)
    |   l=StringLiteral -> ^(StringLiteral $l)
    |   booleanLiteral -> booleanLiteral
    |   'null' -> Null
    ;

integerLiteral
    :   x=HexLiteral -> ^(HexLiteral $x)
    |   x=OctalLiteral -> ^(OctalLiteral $x)
    |   x=DecimalLiteral -> ^(DecimalLiteral $x)
    ;

booleanLiteral
    :   'true'  -> ^(BooleanLiteral True)
    |   'false' -> ^(BooleanLiteral False)
    ;

// ANNOTATIONS

annotations
    :   annotation+ -> ^(AnnotationList annotation+)
    ;

annotation
    :   '@' annotationName ( '('
        (   elementValuePairs -> ^(Pairs annotationName elementValuePairs)
        |   elementValue -> ^(Element annotationName elementValue)
        )? ')' )?
    ;
    
annotationName
    : Identifier ('.' Identifier)* -> ^(QualifiedName Identifier+)
    ;

elementValuePairs
    :   l+=elementValuePair (',' l+=elementValuePair)*
        ->  ^(ElementValuePairList $l+)
    ;

elementValuePair
    :   Identifier '=' elementValue -> ^(ElementValuePair Identifier elementValue)
    ;
    
elementValue
    :   conditionalExpression -> ^(ExpressionToElementValue conditionalExpression)
    |   annotation -> ^(AnnotationToElementValue annotation)
    |   elementValueArrayInitializer -> elementValueArrayInitializer
    ;
    
elementValueArrayInitializer
    :   '{' (l+=elementValue (',' l+=elementValue)*)? (',')? '}'
        ->  ^(ElementValueArrayInitializer $l+)
    ;
    
annotationTypeDeclaration
    :   '@' 'interface' Identifier annotationTypeBody
        ->  ^(AnnotationType Identifier ^(ModifierList ) annotationTypeBody)
    ;
    
annotationTypeBody
    :   '{' (annotationTypeElementDeclaration)* '}'
        ->  ^(AnnotationTypeBody annotationTypeElementDeclaration*)
    ;
    
annotationTypeElementDeclaration
    :   modifiers annotationTypeElementRest -> annotationTypeElementRest
    ;
    
annotationTypeElementRest
    :   type annotationMethodOrConstantRest ';' -> annotationMethodOrConstantRest
    |   normalClassDeclaration ';'?
        ->  ^(TypeDeclToAnnotationElement normalClassDeclaration)
    |   normalInterfaceDeclaration ';'?
        ->  ^(TypeDeclToAnnotationElement normalInterfaceDeclaration)
    |   enumDeclaration ';'?
        ->  ^(TypeDeclToAnnotationElement enumDeclaration)
    |   annotationTypeDeclaration ';'?
        ->  ^(TypeDeclToAnnotationElement annotationTypeDeclaration)
    ;
    
annotationMethodOrConstantRest
    :   annotationMethodRest -> annotationMethodRest
    |   annotationConstantRest -> annotationConstantRest
    ;
    
annotationMethodRest
    :   Identifier '(' ')' defaultValue?
        ->  ^(AnnotationMethod ^(ModifierList ) Void Identifier defaultValue)
    ;
    
annotationConstantRest
    :   variableDeclarators
        ->  ^(AnnotationConstant ^(ModifierList ) Void variableDeclarators)
    ;
    
defaultValue
    :   'default' elementValue -> elementValue
    ;

// STATEMENTS / BLOCKS

block
    :   '{' blockStatement* '}' -> ^(BlockStatementList blockStatement* )
    ;
    
blockStatement
    :   localVariableDeclarationStatement
        ->  ^(VariablesDeclToBlockStatement localVariableDeclarationStatement)
    |   classOrInterfaceDeclaration
        ->  ^(TypeDeclToBlockStatement classOrInterfaceDeclaration)
    |   statement -> ^(Statement statement)
    ;
    
localVariableDeclarationStatement
    :    localVariableDeclaration ';' -> localVariableDeclaration
    ;

localVariableDeclaration
    :   variableModifiers type variableDeclarators
        ->  ^(VariablesDecl variableModifiers variableDeclarators)
    ;
    
variableModifiers
    :   variableModifier* -> ^(ModifierList variableModifier* )
    ;

statement
    :   block
        ->  ^(BlockToStatement block)
    |   ASSERT e=expression (':' v=expression)? ';'
        ->  ^(Assert $e $v)
    |   'if' parExpression ts=statement (options {k=1;}:'else' es=statement)?
        ->  ^(If parExpression $ts EmptyStatement)
    |   'for' '(' forControl ')' statement
        ->  ^(For forControl statement)
    |   'while' parExpression statement
        ->  ^(While parExpression statement)
    |   'do' statement 'while' parExpression ';'
        ->  ^(DoWhile statement parExpression)
    |   'try' block
        ( catches 'finally' f=block //-> ^(Try $t catches $f)
        | catches                   //-> ^(Try $t catches ^(BlockStatementList ))
        |   'finally' f=block       //-> ^(Try $t ^(CatchClauseList ) $f)
        ) -> ^(Try block ^(CatchClauseList ) ^(BlockStatementList ))
    |   'switch' parExpression '{' switchBlockStatementGroups '}'
        ->  ^(Switch parExpression switchBlockStatementGroups)
    |   'synchronized' parExpression block
        ->  ^(SynchronizedStatement parExpression block)
    |   'return' expression? ';'
        ->  EmptyStatement //^(Return expression)
    |   'throw' expression ';'
        ->  ^(Throw expression)
    |   'break' Identifier? ';'
        ->  EmptyStatement //^(Break Identifier)
    |   'continue' Identifier? ';'
        ->  EmptyStatement //^(Continue Identifier)
    |   ';' 
        ->  EmptyStatement
    |   statementExpression ';'
        ->  statementExpression
    |   Identifier ':' statement
        ->  ^(Label Identifier statement)
    ;
    
catches
    :   catchClause (catchClause)* -> ^(CatchClauseList catchClause+)
    ;
    
catchClause
    :   'catch' '(' formalParameter ')' block
        ->  ^(CatchClause formalParameter block)
    ;

formalParameter
    :   variableModifiers type variableDeclaratorId
        ->  ^(FormalParameter variableModifiers type variableDeclaratorId)
    ;
        
switchBlockStatementGroups
    :   (switchBlockStatementGroup)*
        ->  ^(SwitchBlockStatementGroupList switchBlockStatementGroup* )
    ;
    
/* The change here (switchLabel -> switchLabel+) technically makes this grammar
   ambiguous; but with appropriately greedy parsing it yields the most
   appropriate AST, one in which each group, except possibly the last one, has
   labels and statements. */
switchBlockStatementGroup
    :   switchLabel+ blockStatement*
        ->  ^(SwitchBlockStatementGroup
                ^(SwitchLabelList switchLabel+ )
                ^(BlockStatementList blockStatement* )
                )
    ;
    
switchLabel
    :   'case' constantExpression ':'
        ->  ^(ConstantExpressionToSwitchLabel constantExpression)
    |   'case' enumConstantName ':'
        ->  ^(EnumConstantNameToSwitchLabel enumConstantName)
    |   'default' ':' -> Default
    ;
    
forControl
options {k=3;} // be efficient for common case: for (ID ID : ID) ...
    :   enhancedForControl -> enhancedForControl
    |   forInit? ';' expression? ';' forUpdate?
        ->  ^(StandardForControl forInit expression forUpdate)
    ;

forInit
    :   localVariableDeclaration
        ->  ^(VariablesDeclToForInit localVariableDeclaration)
    |   expressionList
        ->  ^(ExpressionListToForInit expressionList)
    ;
    
enhancedForControl
    :   variableModifiers type Identifier ':' expression
        ->  ^(EnhancedForControl variableModifiers type Identifier expression)
    ;

forUpdate
    :   expressionList -> expressionList
    ;

// EXPRESSIONS

parExpression
    :   '(' expression ')' -> expression
    ;
    
expressionList
    :   expression (',' expression)* -> ^(ExpressionList expression+)
    ;

statementExpression
    :   expression -> ^(ExpressionToStatement expression)
    ;
    
constantExpression
    :   expression -> expression
    ;
    
expression
    :   conditionalExpression (assignmentOperator expression)?
        ->  EmptyExpression
    ;
    
assignmentOperator
    :   '='
    |   '+='
    |   '-='
    |   '*='
    |   '/='
    |   '&='
    |   '|='
    |   '^='
    |   '%='
    |   ('<' '<' '=')=> t1='<' t2='<' t3='=' 
        { $t1.getLine() == $t2.getLine() &&
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
    |   ('>' '>' '>' '=')=> t1='>' t2='>' t3='>' t4='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() &&
          $t3.getLine() == $t4.getLine() && 
          $t3.getCharPositionInLine() + 1 == $t4.getCharPositionInLine() }?
    |   ('>' '>' '=')=> t1='>' t2='>' t3='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
    ;

conditionalExpression
    :   conditionalOrExpression ( '?' expression ':' expression )?
        ->  EmptyExpression
    ;

conditionalOrExpression
    :   conditionalAndExpression ( '||' conditionalAndExpression )*
    ;

conditionalAndExpression
    :   inclusiveOrExpression ( '&&' inclusiveOrExpression )*
    ;

inclusiveOrExpression
    :   exclusiveOrExpression ( '|' exclusiveOrExpression )*
    ;

exclusiveOrExpression
    :   andExpression ( '^' andExpression )*
    ;

andExpression
    :   equalityExpression ( '&' equalityExpression )*
    ;

equalityExpression
    :   instanceOfExpression ( ('==' | '!=') instanceOfExpression )*
    ;

instanceOfExpression
    :   relationalExpression ('instanceof' type)?
    ;

relationalExpression
    :   shiftExpression ( relationalOp shiftExpression )*
    ;
    
relationalOp
    :   ('<' '=')=> t1='<' t2='=' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    |   ('>' '=')=> t1='>' t2='=' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    |   '<' 
    |   '>' 
    ;

shiftExpression
    :   additiveExpression ( shiftOp additiveExpression )*
    ;

shiftOp
    :   ('<' '<')=> t1='<' t2='<' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    |   ('>' '>' '>')=> t1='>' t2='>' t3='>' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
    |   ('>' '>')=> t1='>' t2='>'
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    ;


additiveExpression
    :   multiplicativeExpression ( ('+' | '-') multiplicativeExpression )*
    ;

multiplicativeExpression
    :   unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
    ;
    
unaryExpression
    :   '+' unaryExpression
    |   '-' unaryExpression
    |   '++' unaryExpression
    |   '--' unaryExpression
    |   unaryExpressionNotPlusMinus
    ;

unaryExpressionNotPlusMinus
    :   '~' unaryExpression
    |   '!' unaryExpression
    |   castExpression
    |   primary selector* ('++'|'--')?
    ;

castExpression
    :  '(' primitiveType ')' unaryExpression
    |  '(' (type | expression) ')' unaryExpressionNotPlusMinus
    ;

primary
    :(   parExpression
    |   'this' ('.' Identifier)* identifierSuffix?
    |   'super' superSuffix
    |   literal
    |   'new' creator
    |   Identifier ('.' Identifier)* identifierSuffix?
    |   primitiveType ('[' ']')* '.' 'class'
    |   'void' '.' 'class'
    ) -> Null
    ;

identifierSuffix
    :   ('[' ']')+ '.' 'class'
    |   ('[' expression ']')+ // can also be matched by selector, but do here
    |   arguments
    |   '.' 'class'
    |   '.' explicitGenericInvocation
    |   '.' 'this'
    |   '.' 'super' arguments
    |   '.' 'new' innerCreator
    ;

creator
    :   nonWildcardTypeArguments createdName classCreatorRest
    |   createdName (arrayCreatorRest | classCreatorRest)
    ;

createdName
    :   classOrInterfaceType
    |   primitiveType
    ;
    
innerCreator
    :   nonWildcardTypeArguments? Identifier classCreatorRest
    ;

arrayCreatorRest
    :   '['
        (   ']' ('[' ']')* arrayInitializer
        |   expression ']' ('[' expression ']')* ('[' ']')*
        )
    ;

classCreatorRest
    :   arguments classBody?
    ;
    
explicitGenericInvocation
    :   nonWildcardTypeArguments Identifier arguments
    ;
    
nonWildcardTypeArguments
    :   '<' typeList '>'
    ;
    
selector
    :   '.' Identifier arguments?
    |   '.' 'this'
    |   '.' 'super' superSuffix
    |   '.' 'new' innerCreator
    |   '[' expression ']'
    ;
    
superSuffix
    :   arguments
    |   '.' Identifier arguments?
    ;

arguments
    :   '(' expressionList? ')' -> ^(ExpressionList )
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
    |   ('0'..'9')+ FloatTypeSuffix
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

ENUM:   'enum' {if (!enumIsKeyword) $type=Identifier;}
    ;
    
ASSERT
    :   'assert' {if (!assertIsKeyword) $type=Identifier;}
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
