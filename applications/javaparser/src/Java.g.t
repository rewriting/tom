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
  CLASSCREATORREST;
}

@header {
package parser;
import org.antlr.runtime.tree.Tree;
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
    |   p=packageDeclaration? importDeclaration* typeDeclaration*
        -> {p==null}?
            ^(CompilationUnit
                ^(AnnotationList )
                ^(QualifiedName )
                ^(ImportList importDeclaration*)
                ^(TypeDeclList typeDeclaration*)
                )
        ->  ^(CompilationUnit
                ^(AnnotationList )
                packageDeclaration
                ^(ImportList importDeclaration*)
                ^(TypeDeclList typeDeclaration*)
                )
    ;

packageDeclaration
    :   'package' qualifiedName ';' -> qualifiedName
    ;

importDeclaration
    :   'import' s='static'? qualifiedName ('.' p='*')? ';'
        -> {p==null && s==null}? ^(Import qualifiedName EmptyModifier False)
        -> {p==null}?            ^(Import qualifiedName Static        False)
        -> {s==null}?            ^(Import qualifiedName EmptyModifier True )
        ->                       ^(Import qualifiedName Static        True )
    ;
    
typeDeclaration
    :   classOrInterfaceDeclaration
    |   ';' -> EmptyTypeDecl
    ;

classOrInterfaceDeclaration
    :   classOrInterfaceModifiers
        (    classDeclaration[$classOrInterfaceModifiers.tree] -> classDeclaration
        |    interfaceDeclaration[$classOrInterfaceModifiers.tree] -> interfaceDeclaration
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

classDeclaration[Tree modifiers]
    :   normalClassDeclaration[modifiers]
    |   enumDeclaration[modifiers]
    ;

normalClassDeclaration[Tree modifiers]
    :   'class' Identifier tp=typeParameters?
        ('extends' e=type)?
        ('implements' i=typeList)?
        classBody
        -> {tp==null && e==null && i==null}?
            ^(NormalClass Identifier {modifiers} ^(TypeParameterList ) Void ^(TypeList ) classBody)
        -> {tp==null && e==null}?
            ^(NormalClass Identifier {modifiers} ^(TypeParameterList ) Void $i classBody)
        -> {tp==null && i==null}?
            ^(NormalClass Identifier {modifiers} ^(TypeParameterList ) $e ^(TypeList ) classBody)
        -> {tp==null}?
            ^(NormalClass Identifier {modifiers} ^(TypeParameterList ) $e $i classBody)
        -> {e==null && i==null}?
            ^(NormalClass Identifier {modifiers} $tp Void ^(TypeList ) classBody)
        -> {e==null}?
            ^(NormalClass Identifier {modifiers} $tp Void $i classBody)
        -> {i==null}?
            ^(NormalClass Identifier {modifiers} $tp $e ^(TypeList ) classBody)
        ->  ^(NormalClass Identifier {modifiers} $tp $e $i classBody)
    ;
    
typeParameters
    :   '<' typeParameter (',' typeParameter)* '>'
        ->  ^(TypeParameterList typeParameter+)
    ;

typeParameter
    :   Identifier ('extends' tb=typeBound)?
        -> {tb==null}? ^(TypeParameter Identifier ^(TypeList ))
        ->             ^(TypeParameter Identifier $tb)
    ;
        
typeBound
    :   type ('&' type)* -> ^(TypeList type+)
    ;

enumDeclaration[Tree modifiers]
    :   ENUM Identifier ('implements' i=typeList)? enumBody
        -> {i==null}? ^(EnumClass Identifier {modifiers} ^(TypeList ) enumBody )
        ->            ^(EnumClass Identifier {modifiers} $i enumBody )
    ;

enumBody
    :   '{' c=enumConstants? ','? b=enumBodyDeclarations? '}'
        -> {c==null && b==null}? ^(EnumBody ^(EnumConstantList ) ^(BodyDeclList ))
        -> {c==null}?            ^(EnumBody ^(EnumConstantList ) $b)
        -> {b==null}?            ^(EnumBody $c ^(BodyDeclList ))
        ->                       ^(EnumBody $c $b)
    ;

enumConstants
    :   enumConstant (',' enumConstant)* -> ^(EnumConstantList enumConstant+)
    ;

enumConstant
    :   anns=annotations? Identifier args=arguments? b=classBody?
        -> {anns==null && args==null && b==null}? ^(EnumConstant Identifier ^(AnnotationList ) ^(ExpressionList ) ^(BodyDeclList ))
        -> {args==null && b==null}?               ^(EnumConstant Identifier $anns ^(ExpressionList ) ^(BodyDeclList ))
        -> {anns==null && b==null}?               ^(EnumConstant Identifier ^(AnnotationList ) $args ^(BodyDeclList ))
        -> {b==null}?                             ^(EnumConstant Identifier $anns $args ^(BodyDeclList ))
        -> {anns==null && args==null}?            ^(EnumConstant Identifier ^(AnnotationList ) ^(ExpressionList ) $b)
        -> {args==null}?                          ^(EnumConstant Identifier $anns ^(ExpressionList ) $b)
        -> {anns==null}?                          ^(EnumConstant Identifier ^(AnnotationList ) $args $b)
        ->                                        ^(EnumConstant Identifier $anns $args $b)
    ;
    
enumBodyDeclarations
    :   ';' classBodyDeclaration*
        ->  ^(BodyDeclList classBodyDeclaration*)
    ;

interfaceDeclaration[Tree modifiers]
    :   normalInterfaceDeclaration[modifiers]
    |   annotationTypeDeclaration[modifiers]
    ;

normalInterfaceDeclaration[Tree modifiers]
    :   'interface' Identifier tp=typeParameters? ('extends' e=typeList)? interfaceBody
        -> {tp==null && e==null}? ^(NormalInterface Identifier {modifiers} ^(TypeParameterList ) ^(TypeList ) interfaceBody)
        -> {tp==null}?            ^(NormalInterface Identifier {modifiers} ^(TypeParameterList ) $e interfaceBody)
        -> {e==null}?             ^(NormalInterface Identifier {modifiers} $tp ^(TypeList ) interfaceBody)
        ->                        ^(NormalInterface Identifier {modifiers} $tp $e interfaceBody)
    ;
    
typeList
    :   type (',' type)* -> ^(TypeList type+)
    ;
    
classBody
    :   '{' classBodyDeclaration* '}' -> ^(BodyDeclList classBodyDeclaration*)
    ;

interfaceBody
    :   '{' interfaceBodyDeclaration* '}'
        ->  ^(BodyDeclList interfaceBodyDeclaration*)
    ;

classBodyDeclaration
    :   ';'
        ->  EmptyBodyDecl
    |   s='static'? block
        -> {s==null}? ^(BlockToBodyDecl block EmptyModifier)
        ->            ^(BlockToBodyDecl block Static)
    |   modifiers! memberDecl[$modifiers.tree]
    ;

memberDecl[Tree modifiers]
    :   genericMethodOrConstructorDecl[modifiers]
    |   memberDeclaration[modifiers]
    |   'void'! Identifier! voidMethodDeclaratorRest[modifiers, new AstTree($Identifier)]
    |   Identifier! constructorDeclaratorRest[modifiers, null, new AstTree($Identifier)]
    |   interfaceDeclaration[modifiers]
        ->  ^(TypeDeclToBodyDecl interfaceDeclaration)
    |   classDeclaration[modifiers]
        ->  ^(TypeDeclToBodyDecl classDeclaration)
    ;

memberDeclaration[Tree modifiers]
    :   type!
        (   methodDeclaration[modifiers, $type.tree]
        |   fieldDeclaration[modifiers, $type.tree]
        )
    ;

genericMethodOrConstructorDecl[Tree modifiers]
    :   typeParameters! genericMethodOrConstructorRest[modifiers, $typeParameters.tree]
    ;

// GRAMMAR MODIFICATION : expanding (type | 'void')

genericMethodOrConstructorRest[Tree modifiers, Tree typeparameters]
    :   type! Identifier! methodDeclaratorRest[modifiers, typeparameters, $type.tree, new AstTree($Identifier)]
    |   'void'! Identifier! methodDeclaratorRest[modifiers, typeparameters, null, new AstTree($Identifier)]
    |   Identifier! constructorDeclaratorRest[modifiers, typeparameters, new AstTree($Identifier)]
    ;

methodDeclaration[Tree modifiers, Tree type]
    :   Identifier! methodDeclaratorRest[modifiers, null, type, new AstTree($Identifier)]
    ;

fieldDeclaration[Tree modifiers, Tree type]
    :   variableDeclarators[type] ';'
        ->  ^(FieldDecl {modifiers} variableDeclarators)
    ;

interfaceBodyDeclaration
    :   modifiers! interfaceMemberDecl[$modifiers.tree]
    |   ';' -> EmptyBodyDecl
    ;

interfaceMemberDecl[Tree modifiers]
    :   interfaceMethodOrFieldDecl[modifiers]
    |   interfaceGenericMethodDecl[modifiers]
    |   'void'! Identifier! voidInterfaceMethodDeclaratorRest[modifiers, new AstTree($Identifier)]
    |   interfaceDeclaration[modifiers]
        ->  ^(TypeDeclToBodyDecl interfaceDeclaration)
    |   classDeclaration[modifiers]
        ->  ^(TypeDeclToBodyDecl classDeclaration)
    ;

interfaceMethodOrFieldDecl[Tree modifiers]
    :   type! Identifier! interfaceMethodOrFieldRest[modifiers, $type.tree, new AstTree($Identifier)]
    ;

interfaceMethodOrFieldRest[Tree modifiers, Tree type, Tree name]
    :   constantDeclaratorsRest[type, name] ';'
        ->  ^(FieldDecl {modifiers} constantDeclaratorsRest)
    |   interfaceMethodDeclaratorRest[modifiers, null, type, name]
    ;

methodDeclaratorRest[Tree modifiers, Tree typeparameters, Tree type, Tree name]
    :   formalParameters ('[' ']')*
        ('throws' t=qualifiedNameList)?
        (   methodBody
            -> {typeparameters==null && type==null && t==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters ^(QualifiedNameList ) methodBody)
            -> {typeparameters==null && type==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters $t methodBody)
            -> {typeparameters==null && t==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) {type} {name} formalParameters ^(QualifiedNameList ) methodBody)
            -> {typeparameters==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) {type} {name} formalParameters $t methodBody)
            -> {type==null && t==null}?
                ^(MethodDecl {modifiers} {typeparameters} Void {name} formalParameters ^(QualifiedNameList ) methodBody)
            -> {type==null}?
                ^(MethodDecl {modifiers} {typeparameters} Void {name} formalParameters $t methodBody)
            -> {t==null}?
                ^(MethodDecl {modifiers} {typeparameters} {type} {name} formalParameters ^(QualifiedNameList ) methodBody)
            ->  ^(MethodDecl {modifiers} {typeparameters} {type} {name} formalParameters $t methodBody)
        |   ';'
            -> {typeparameters==null && type==null && t==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
            -> {typeparameters==null && type==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters $t ^(BlockStatementList ))
            -> {typeparameters==null && t==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) {type} {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
            -> {typeparameters==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) {type} {name} formalParameters $t ^(BlockStatementList ))
            -> {type==null && t==null}?
                ^(MethodDecl {modifiers} {typeparameters} Void {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
            -> {type==null}?
                ^(MethodDecl {modifiers} {typeparameters} Void {name} formalParameters $t ^(BlockStatementList ))
            -> {t==null}?
                ^(MethodDecl {modifiers} {typeparameters} {type} {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
            ->  ^(MethodDecl {modifiers} {typeparameters} {type} {name} formalParameters $t ^(BlockStatementList ))
        )
    ;

voidMethodDeclaratorRest[Tree modifiers, Tree name]
    :   formalParameters ('throws' t=qualifiedNameList)?
        (   methodBody
            -> {t==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters ^(QualifiedNameList ) methodBody)
            ->  ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters $t methodBody)
        |   ';'
            -> {t==null}?
                ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
            ->  ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters $t ^(BlockStatementList ))
        )
    ;

interfaceMethodDeclaratorRest[Tree modifiers, Tree typeparameters, Tree type, Tree name]
    :   formalParameters ('[' ']')* ('throws' t=qualifiedNameList)? ';'
        -> {typeparameters==null && type==null && t==null}?
            ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
        -> {typeparameters==null && type==null}?
            ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters $t ^(BlockStatementList ))
        -> {typeparameters==null && t==null}?
            ^(MethodDecl {modifiers} ^(TypeParameterList ) {type} {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
        -> {typeparameters==null}?
            ^(MethodDecl {modifiers} ^(TypeParameterList ) {type} {name} formalParameters $t ^(BlockStatementList ))
        -> {type==null && t==null}?
            ^(MethodDecl {modifiers} {typeparameters} Void {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
        -> {type==null}?
            ^(MethodDecl {modifiers} {typeparameters} Void {name} formalParameters $t ^(BlockStatementList ))
        -> {t==null}?
            ^(MethodDecl {modifiers} {typeparameters} {type} {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
        ->  ^(MethodDecl {modifiers} {typeparameters} {type} {name} formalParameters $t ^(BlockStatementList ))
    ;

// GRAMMAR MODIFICATION : expanding (type | 'void')

interfaceGenericMethodDecl[Tree modifiers]
    :   typeParameters
        (   type i1=Identifier interfaceMethodDeclaratorRest[modifiers, $typeParameters.tree, $type.tree, new AstTree($i1)]
        |   'void' i2=Identifier interfaceMethodDeclaratorRest[modifiers, $typeParameters.tree, null, new AstTree($i2)]
        )
    ;

voidInterfaceMethodDeclaratorRest[Tree modifiers, Tree name]
    :   formalParameters ('throws' t=qualifiedNameList)? ';'
        -> {t==null}?
            ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters ^(QualifiedNameList ) ^(BlockStatementList ))
        ->  ^(MethodDecl {modifiers} ^(TypeParameterList ) Void {name} formalParameters $t ^(BlockStatementList ))
    ;

constructorDeclaratorRest[Tree modifiers, Tree typeparameters, Tree name]
    :   formalParameters ('throws' t=qualifiedNameList)? constructorBody
        -> {typeparameters==null && t==null}?
            ^(ConstructorDecl {modifiers} ^(TypeParameterList ) {name} formalParameters ^(QualifiedNameList ) constructorBody)
        -> {typeparameters==null}?
            ^(ConstructorDecl {modifiers} ^(TypeParameterList ) {name} formalParameters $t constructorBody)
        -> {t==null}?
            ^(ConstructorDecl {modifiers} {typeparameters} {name} formalParameters ^(QualifiedNameList ) constructorBody)
        ->  ^(ConstructorDecl {modifiers} {typeparameters} {name} formalParameters $t constructorBody)
    ;

constantDeclarator[Tree type]
    :   Identifier! constantDeclaratorRest[type, new AstTree($Identifier)]
    ;
    
variableDeclarators[Tree type]
    :   variableDeclarator[type] (',' variableDeclarator[type])*
        ->  ^(VariableDeclList variableDeclarator+)
    ;

variableDeclarator[Tree type]
    :   variableDeclaratorId ('=' i=variableInitializer)?
        -> {i==null}? ^(VariableDecl {type} variableDeclaratorId EmptyVariableInitializer)
        ->            ^(VariableDecl {type} variableDeclaratorId $i)
    ;
    
constantDeclaratorsRest[Tree type, Tree name]
    :   constantDeclaratorRest[type, name] (',' constantDeclarator[type])*
        ->  ^(VariableDeclList constantDeclaratorRest constantDeclarator*)
    ;

constantDeclaratorRest[Tree type, Tree name]
    :   ('[' ']')* '=' variableInitializer
        ->  ^(VariableDecl {type} {name} variableInitializer)
    ;
    
variableDeclaratorId
    :   Identifier ('[' ']')* -> Identifier
    ;

variableInitializer
    :   arrayInitializer
    |   expression -> ^(ExpressionToVariableInitializer expression)
    ;

arrayInitializer
    :   '{' (variableInitializer (',' variableInitializer)* (',')? )? '}'
        ->  ^(ArrayInitializer variableInitializer*)
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
    |   'synchronized' -> Synchronized
    |   'transient'    -> Transient
    |   'volatile'     -> Volatile
    |   'strictfp'     -> StrictFP
    ;

packageOrTypeName
    :   qualifiedName
    ;

enumConstantName
    :   Identifier
    ;

typeName
    :   qualifiedName
    ;

type
    :   classOrInterfaceType ('[' ']')*
        ->  ^(ClassOrInterfaceType classOrInterfaceType)
    |   primitiveType ('[' ']')* -> primitiveType
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
    |   annotation -> ^(AnnotationToModifier annotation)
    ;

typeArguments
    :   '<' typeArgument (',' typeArgument)* '>'
        ->  ^(TypeArgumentList typeArgument+)
    ;

typeArgument
    :   type
        -> ^(ExplicitTypeArgument type)
    |   '?' ((e='extends' | s='super') type)?
        -> {e!=null}? ^(ExtendTypeArgument type)
        -> {s!=null}? ^(SuperTypeArgument type)
        ->            WildCardTypeArgument
    ;

qualifiedNameList
    :   qualifiedName (',' qualifiedName)* -> ^(QualifiedNameList qualifiedName+)
    ;

formalParameters
    :   '(' d=formalParameterDecls? ')'
        -> {d==null}? ^(FormalParameterDeclList )
        -> $d
    ;

formalParameterDecls
    :   variableModifiers! type! formalParameterDeclsRest[$variableModifiers.tree, $type.tree]
    ;
    //FORMALPARAMETERDECLS
formalParameterDeclsRest[Tree modifiers, Tree type]
    :   (variableDeclaratorId
        ->  ^(FormalParameterDeclList
                          ^(FormalParameterDecl {modifiers} {type} variableDeclaratorId False)
                          ))
            (',' formalParameterDecls
                //{$formalParameterDeclsRest.tree.addChildren($formalParameterDecls.tree.getChildren());}
                )?
    |   '...' variableDeclaratorId
        ->  ^(FormalParameterDeclList
                ^(FormalParameterDecl {modifiers} {type} variableDeclaratorId True)
                )
    ;

methodBody
    :   block
    ;

constructorBody
    :   '{' ci=explicitConstructorInvocation? blockStatement* '}'
        -> {ci==null}? ^(ConstructorBody EmptyExplicitConstructorInvocation ^(BlockStatementList blockStatement*))
        ->             ^(ConstructorBody $ci ^(BlockStatementList blockStatement*))
    ;

explicitConstructorInvocation
    :   ta=nonWildcardTypeArguments? ( 'this' | x='super' ) arguments ';'
        -> {ta==null && x==null}? ^(ThisInvocation ^(TypeList ) arguments)
        -> {x==null}?             ^(ThisInvocation $ta arguments)
        -> {ta==null}?            ^(SuperInvocation ^(TypeList ) arguments)
        ->                        ^(SuperInvocation $ta arguments)
    |   primary '.' ta=nonWildcardTypeArguments? 'super' arguments ';'
        -> {ta==null}? ^(PrimarySuperInvocation primary ^(TypeList ) arguments)
        ->             ^(PrimarySuperInvocation primary $ta arguments)
    ;


qualifiedName
    :   Identifier ('.' Identifier)* -> ^(QualifiedName Identifier+)
    ;
    
literal 
    :   integerLiteral
    |   l=FloatingPointLiteral -> ^(FloatingPointLiteral $l)
    |   l=CharacterLiteral -> ^(CharacterLiteral $l)
    |   l=StringLiteral -> ^(StringLiteral $l)
    |   booleanLiteral
    |   'null' -> Null
    ;

integerLiteral
    :   i=HexLiteral -> ^(HexLiteral $i)
    |   i=OctalLiteral -> ^(OctalLiteral $i)
    |   i=DecimalLiteral -> ^(DecimalLiteral $i)
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
    :   '@' annotationName ( '(' (evp=elementValuePairs | ev=elementValue)? ')' )?
        -> {evp!=null}? ^(Pairs annotationName $evp)
        -> {ev!=null}? ^(Element annotationName $ev)
        -> ^(NameToAnnotation annotationName)
    ;
    
annotationName
    : Identifier ('.' Identifier)* -> ^(QualifiedName Identifier+)
    ;

elementValuePairs
    :   elementValuePair (',' elementValuePair)*
        ->  ^(ElementValuePairList elementValuePair+)
    ;

elementValuePair
    :   Identifier '=' elementValue -> ^(ElementValuePair Identifier elementValue)
    ;

elementValue
    :   conditionalExpression -> ^(ExpressionToElementValue conditionalExpression)
    |   annotation -> ^(AnnotationToElementValue annotation)
    |   elementValueArrayInitializer
    ;

elementValueArrayInitializer
    :   '{' (elementValue (',' elementValue)*)? (',')? '}'
        ->  ^(ElementValueArrayInitializer elementValue*)
    ;

annotationTypeDeclaration[Tree modifiers]
    :   '@' 'interface' Identifier annotationTypeBody
        ->  ^(AnnotationType Identifier {modifiers} annotationTypeBody)
    ;
    
annotationTypeBody
    :   '{' (annotationTypeElementDeclaration)* '}'
        ->  ^(AnnotationTypeBody annotationTypeElementDeclaration*)
    ;
    
annotationTypeElementDeclaration
    :   modifiers! annotationTypeElementRest[$modifiers.tree]
    ;

annotationTypeElementRest[Tree modifiers]
    :   type! annotationMethodOrConstantRest[modifiers, $type.tree] ';'!
    |   normalClassDeclaration[modifiers] ';'?
        ->  ^(TypeDeclToAnnotationElement normalClassDeclaration)
    |   normalInterfaceDeclaration[modifiers] ';'?
        ->  ^(TypeDeclToAnnotationElement normalInterfaceDeclaration)
    |   enumDeclaration[modifiers] ';'?
        ->  ^(TypeDeclToAnnotationElement enumDeclaration)
    |   annotationTypeDeclaration[modifiers] ';'?
        ->  ^(TypeDeclToAnnotationElement annotationTypeDeclaration)
    ;
    
annotationMethodOrConstantRest[Tree modifiers, Tree type]
    :   annotationMethodRest[modifiers, type]
    |   annotationConstantRest[modifiers, type]
    ;

annotationMethodRest[Tree modifiers, Tree type]
    :   Identifier '(' ')' d=defaultValue?
        -> {d==null}? ^(AnnotationMethod {modifiers} {type} Identifier ^(EmptyElementValue ))
        ->            ^(AnnotationMethod {modifiers} {type} Identifier $d)
    ;

annotationConstantRest[Tree modifiers, Tree type]
    :   variableDeclarators[type]
        ->  ^(AnnotationConstant {modifiers} variableDeclarators)
    ;

defaultValue
    :   'default'! elementValue
    ;

// STATEMENTS / BLOCKS

block
    :   '{' blockStatement* '}' -> ^(BlockStatementList blockStatement* )
    ;
    
blockStatement
    :   localVariableDeclarationStatement
        ->  ^(FieldDeclToBlockStatement localVariableDeclarationStatement)
    |   classOrInterfaceDeclaration
        ->  ^(TypeDeclToBlockStatement classOrInterfaceDeclaration)
    |   statement -> ^(Statement statement)
    ;

localVariableDeclarationStatement
    :    localVariableDeclaration ';'!
    ;

localVariableDeclaration
    :   variableModifiers type variableDeclarators[$type.tree]
        ->  ^(FieldDecl variableModifiers variableDeclarators)
    ;

variableModifiers
    :   variableModifier* -> ^(ModifierList variableModifier* )
    ;

statement
    :   block
        ->  ^(BlockToStatement block)
    |   ASSERT expr=expression (':' val=expression)? ';'
        -> {val==null}? ^(Assert $expr EmptyExpression)
        ->              ^(Assert $expr $val)
    |   'if' parExpression tstat=statement (options {k=1;}:'else' estat=statement)?
        -> {estat==null}? ^(If parExpression $tstat EmptyStatement)
        ->                ^(If parExpression $tstat $estat)
    |   'for' '(' forControl ')' statement
        ->  ^(For forControl statement)
    |   'while' parExpression statement
        ->  ^(While parExpression statement)
    |   'do' statement 'while' parExpression ';'
        ->  ^(DoWhile statement parExpression)
    |   'try' tblock=block
        ( c=catches 'finally' fblock=block
        | c=catches
        | 'finally' fblock=block
        )
        -> {fblock==null}? ^(Try $tblock $c ^(BlockStatementList ))
        -> {c==null}?      ^(Try $tblock ^(CatchClauseList ) $fblock)
        ->                 ^(Try $tblock $c $fblock)
    |   'switch' parExpression '{' switchBlockStatementGroups '}'
        ->  ^(Switch parExpression switchBlockStatementGroups)
    |   'synchronized' parExpression block
        ->  ^(SynchronizedStatement parExpression block)
    |   'return' res=expression? ';'
        -> {res==null}? EmptyStatement
        ->              ^(Return $res)
    |   'throw' expression ';'
        ->  ^(Throw expression)
    |   'break' lab=Identifier? ';'
        -> {lab==null}? EmptyStatement
        ->              ^(Break $lab)
    |   'continue' lab=Identifier? ';'
        -> {lab==null}? EmptyStatement
        ->              ^(Continue $lab)
    |   ';' ->  EmptyStatement
    |   statementExpression ';'!
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
    :   enhancedForControl
    |   init=forInit? ';' cond=expression? ';' incr=forUpdate?
        -> {init==null && cond==null && incr==null}?
            ^(StandardForControl ^(ExpressionListToForInit ^(ExpressionList )) EmptyExpression ^(ExpressionList ))
        -> {init==null && cond==null}?
            ^(StandardForControl ^(ExpressionListToForInit ^(ExpressionList )) EmptyExpression $incr)
        -> {init==null && incr==null}?
            ^(StandardForControl ^(ExpressionListToForInit ^(ExpressionList )) $cond ^(ExpressionList ))
        -> {init==null}?
            ^(StandardForControl ^(ExpressionListToForInit ^(ExpressionList )) $cond $incr)
        -> {cond==null && incr==null}?
            ^(StandardForControl $init EmptyExpression ^(ExpressionList ))
        -> {cond==null}?
            ^(StandardForControl $init EmptyExpression $incr)
        -> {incr==null}?
            ^(StandardForControl $init $cond ^(ExpressionList ))
        ->  ^(StandardForControl $init $cond $incr)
    ;

forInit
    :   localVariableDeclaration
        ->  ^(FieldDeclToForInit localVariableDeclaration)
    |   expressionList
        ->  ^(ExpressionListToForInit expressionList)
    ;
    
enhancedForControl
    :   variableModifiers type Identifier ':' expression
        ->  ^(EnhancedForControl variableModifiers type Identifier expression)
    ;

forUpdate
    :   expressionList
    ;

// (* PASSE "finale" (sauf comptage et autres actions) *)

// EXPRESSIONS

parExpression
    :   '('! expression ')'!
    ;
    
expressionList
    :   expression (',' expression)* -> ^(ExpressionList expression+)
    ;

statementExpression
    :   expression -> ^(ExpressionToStatement expression)
    ;
    
constantExpression
    :   expression
    ;
    
expression
    :   conditionalExpression (o=assignmentOperator expression)?
        -> {o==null}? conditionalExpression
        ->            ^(Assignment $o conditionalExpression expression)
    ;
    
assignmentOperator
    :   '='  -> Assign
    |   '+=' -> PlusAssign
    |   '-=' -> MinusAssign
    |   '*=' -> TimesAssign
    |   '/=' -> DivAssign
    |   '&=' -> AndAssign
    |   '|=' -> OrAssign
    |   '^=' -> XOrAssign
    |   '%=' -> ModAssign
    |   ('<' '<' '=')=> t1='<' t2='<' t3='=' 
        { $t1.getLine() == $t2.getLine() &&
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
        -> LeftShiftAssign
    |   ('>' '>' '>' '=')=> t1='>' t2='>' t3='>' t4='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() &&
          $t3.getLine() == $t4.getLine() && 
          $t3.getCharPositionInLine() + 1 == $t4.getCharPositionInLine() }?
        -> UnsignedRightShiftAssign
    |   ('>' '>' '=')=> t1='>' t2='>' t3='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
        -> SignedRightShiftAssign
    ;

conditionalExpression
    :   conditionalOrExpression ( '?' (texpr=expression) ':' (eexpr=expression) )?
        -> {texpr==null}? conditionalOrExpression
        ->                ^(ConditionalExpression conditionalOrExpression $texpr $eexpr)
    ;

conditionalOrExpression
    :   conditionalAndExpression ( '||' conditionalAndExpression )*
        -> ^(AssociativeOperation ConditionalOr ^(ExpressionList conditionalAndExpression+))
    ;

conditionalAndExpression
    :   inclusiveOrExpression ( '&&' inclusiveOrExpression)*
        -> ^(AssociativeOperation ConditionalAnd ^(ExpressionList inclusiveOrExpression+))
    ;

inclusiveOrExpression
    :   exclusiveOrExpression ( '|' exclusiveOrExpression )*
        -> ^(AssociativeOperation InclusiveOr ^(ExpressionList exclusiveOrExpression+))
    ;

exclusiveOrExpression
    :   andExpression ( '^' andExpression )*
        -> ^(AssociativeOperation ExclusiveOr ^(ExpressionList andExpression+))
    ;

andExpression
    :   equalityExpression ( '&' equalityExpression )*
        -> ^(AssociativeOperation And ^(ExpressionList equalityExpression+))
    ;

equalityExpression
    :   instanceOfExpression ( ('==' | '!=') instanceOfExpression )* -> instanceOfExpression
    ;

instanceOfExpression
    :   relationalExpression ('instanceof' t=type)?
        -> {t==null}? relationalExpression
        ->            ^(InstanceOf relationalExpression $t)
    ;

relationalExpression
    :   shiftExpression ( o=relationalOp shiftExpression )*
        -> {o==null}? shiftExpression
        ->            ^(AssociativeOperation relationalOp ^(ExpressionList shiftExpression+))
    ;
    
relationalOp
    :   ('<' '=')=> t1='<' t2='=' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
        -> LowerOrEqual
    |   ('>' '=')=> t1='>' t2='=' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
        -> GreaterOrEqual
    |   '<' -> Lower
    |   '>' -> Greater
    ;

shiftExpression
    :   additiveExpression ( o=shiftOp additiveExpression )*
        -> {o==null}? additiveExpression
        ->            ^(AssociativeOperation $o ^(ExpressionList additiveExpression+))
    ;

shiftOp
    :   ('<' '<')=> t1='<' t2='<' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
        -> LeftShift
    |   ('>' '>' '>')=> t1='>' t2='>' t3='>' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
        -> UnsignedRightShift
    |   ('>' '>')=> t1='>' t2='>'
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
        -> SignedRightShift
    ;

additiveExpression
    :   multiplicativeExpression ( ('+' | '-') multiplicativeExpression )* -> multiplicativeExpression
    ;

multiplicativeExpression
    :   unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* -> unaryExpression
    ;

unaryExpression
    :   '+' unaryExpression -> ^(UnaryOperation UPlus unaryExpression)
    |   '-' unaryExpression -> ^(UnaryOperation UMinus unaryExpression)
    |   '++' unaryExpression -> ^(UnaryOperation PreInc unaryExpression)
    |   '--' unaryExpression -> ^(UnaryOperation PreDec unaryExpression)
    |   unaryExpressionNotPlusMinus
    ;

unaryExpressionNotPlusMinus
    :   '~' unaryExpression -> ^(UnaryOperation BitwiseNot unaryExpression)
    |   '!' unaryExpression -> ^(UnaryOperation LogicalNot unaryExpression)
    |   castExpression
    |   primary selector* (i='++'|d='--')?
        -> {i!=null}? ^(UnaryOperation PostInc ^(Primary primary ^(SuffixList selector*)))
        -> {d!=null}? ^(UnaryOperation PostDec ^(Primary primary ^(SuffixList selector*)))
        ->            ^(Primary primary ^(SuffixList selector*))
    ;

castExpression
    :   '(' primitiveType ')' unaryExpression
        ->  ^(TypeCast primitiveType unaryExpression)
    |   '(' (t=type | expression) ')' unaryExpressionNotPlusMinus
        -> {t!=null}? ^(TypeCast $t unaryExpressionNotPlusMinus)
        ->            ^(ExpressionCast expression unaryExpressionNotPlusMinus)  // semantics ?
    ;

primary
    :   parExpression
        ->  ^(ExpressionToPrimary parExpression)
    |   'this' ('.' Identifier)* s=identifierSuffix?
        -> {s==null}? ^(This ^(QualifiedName Identifier*) EmptySuffix)
        ->            ^(This ^(QualifiedName Identifier*) $s)
    |   'super' superSuffix
        ->  ^(Super superSuffix)
    |   literal
    |   'new'! creator
    |   Identifier ('.' Identifier)* s=identifierSuffix?
        -> {s==null}? ^(QualifiedNameToPrimary ^(QualifiedName Identifier*) EmptySuffix)
        ->            ^(QualifiedNameToPrimary ^(QualifiedName Identifier*) $s)
    |   primitiveType ('[' ']')* '.' 'class'
        -> ^(Class primitiveType)
    |   'void' '.' 'class'
        ->  ^(Class Void)
    ;

identifierSuffix
    :   ('[' ']')+ '.' 'class'
         ->  ^(ArrayClassSuffix {0})
    |   ('[' expression ']')+ // can also be matched by selector, but do here
        ->  ^(ArrayIndexSuffix ^(ExpressionList expression+))
    |   arguments
        ->  ^(ArgumentsSuffix arguments)
    |   '.' 'class'
        ->  ^(ArrayClassSuffix {0})
    |   '.'! explicitGenericInvocation
    |   '.' 'this'
        ->  ThisSuffix
    |   '.' 'super' arguments
        ->  ^(SuperSuffix ^(SuperConstruction arguments))
    |   '.'! 'new'! innerCreator
    ;

creator
    :   nonWildcardTypeArguments createdName r=classCreatorRest
        ->  ^(ClassCreator nonWildcardTypeArguments createdName {$r.tree.getChild(0)} {$r.tree.getChild(1)})
    |   createdName
        (   arrayCreatorRest[$createdName.tree]
        |   r=classCreatorRest
            ->  ^(ClassCreator ^(TypeList ) createdName {$r.tree.getChild(0)} {$r.tree.getChild(1)})
        )
    ;

createdName
    :   classOrInterfaceType -> ^(ClassOrInterfaceType classOrInterfaceType)
    |   primitiveType
    ;
    
innerCreator
    :   ta=nonWildcardTypeArguments? Identifier r=classCreatorRest
        -> {ta==null}?
            ^(InnerCreator
                ^(TypeList )
                ^(ClassOrInterfaceType ^(IndependentType Identifier ^(TypeArgumentList )))
                {$r.tree.getChild(0)}
                {$r.tree.getChild(1)}
                )
        ->  ^(InnerCreator
                $ta
                Identifier
                {$r.tree.getChild(0)}
                {$r.tree.getChild(1)}
                )
    ;

arrayCreatorRest[Tree type]
    :   '['
        (   ']' ('[' ']')* arrayInitializer
            ->  ^(InitializedArrayCreator {type} arrayInitializer)
        |   expression ']' ('[' expression ']')* ('[' ']')*
            ->  ^(NonInitializedArrayCreator ^(ExpressionList expression+) {0})
        )
    ;

classCreatorRest
    :   arguments b=classBody?
        -> {b==null}? ^(CLASSCREATORREST arguments ^(BodyDeclList ))
        ->            ^(CLASSCREATORREST arguments $b)
    ;

explicitGenericInvocation
    :   nonWildcardTypeArguments Identifier arguments
        ->  ^(ExplicitGenericInvocation nonWildcardTypeArguments Identifier arguments)
    ;

nonWildcardTypeArguments
    :   '<'! typeList '>'!
    ;

selector
    :   '.' Identifier a=arguments?
        -> {a==null}? ^(VariableAccessSuffix Identifier)
        ->            ^(CallSuffix Identifier $a)
    |   '.' 'this' -> ThisSuffix
    |   '.' 'super' superSuffix -> ^(SuperSuffix superSuffix)
    |   '.'! 'new'! innerCreator
    |   '[' expression ']' -> ^(ArrayIndexSuffix ^(ExpressionList expression))
    ;

superSuffix
    :   arguments
        ->  ^(SuperConstruction arguments)
    |   '.' Identifier a=arguments?
        -> {a==null}? ^(SuperVariableAccess Identifier)
        ->            ^(SuperCall Identifier arguments)
    ;

arguments
    :   '(' l=expressionList? ')'
        -> {l==null}? ^(ExpressionList )
        -> $l
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
