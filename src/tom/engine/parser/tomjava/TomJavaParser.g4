/*
 [The "BSD licence"]
 Copyright (c) 2013 Terence Parr, Sam Harwell
 Copyright (c) 2017 Ivan Kochurkin (upgrade to Java 8)
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

parser grammar TomJavaParser;

options { tokenVocab=TomJavaLexer; }

compilationUnit
    : packageDeclaration? importDeclaration* typeDeclaration* EOF
    ;

declarations
    : classBodyDeclaration* EOF
    ;

packageDeclaration
    : annotation* PACKAGE qualifiedName ';'
    ;

importDeclaration
    : IMPORT STATIC? qualifiedName ('.' '*')? ';'
    ;

typeDeclaration
    : classOrInterfaceModifier*
      (classDeclaration | enumDeclaration | interfaceDeclaration | annotationTypeDeclaration)
    | ';'
    ;

modifier
    : classOrInterfaceModifier
    | NATIVE
    | SYNCHRONIZED
    | TRANSIENT
    | VOLATILE
    ;

classOrInterfaceModifier
    : annotation
    | PUBLIC
    | PROTECTED
    | PRIVATE
    | STATIC
    | ABSTRACT
    | FINAL    // FINAL for class only -- does not apply to interfaces
    | STRICTFP
    ;

variableModifier
    : FINAL
    | annotation
    ;

classDeclaration
    : CLASS javaIdentifier typeParameters?
      (EXTENDS typeType)?
      (IMPLEMENTS typeList)?
      classBody
    ;

typeParameters
    : '<' typeParameter (',' typeParameter)* '>'
    ;

typeParameter
    : annotation* javaIdentifier (EXTENDS typeBound)?
    ;

typeBound
    : typeType ('&' typeType)*
    ;

enumDeclaration
    : ENUM javaIdentifier (IMPLEMENTS typeList)? '{' enumConstants? ','? enumBodyDeclarations? '}'
    ;

enumConstants
    : enumConstant (',' enumConstant)*
    ;

enumConstant
    : annotation* javaIdentifier arguments? classBody?
    ;

enumBodyDeclarations
    : ';' classBodyDeclaration*
    ;

interfaceDeclaration
    : INTERFACE javaIdentifier typeParameters? (EXTENDS typeList)? interfaceBody
    ;

classBody
    : '{' classBodyDeclaration* '}'
    ;

interfaceBody
    : '{' interfaceBodyDeclaration* '}'
    ;

classBodyDeclaration
    : ';'
    | STATIC? block
    | modifier* memberDeclaration
    | tomDeclaration
    ;

memberDeclaration
    : methodDeclaration
    | genericMethodDeclaration
    | fieldDeclaration
    | constructorDeclaration
    | genericConstructorDeclaration
    | interfaceDeclaration
    | annotationTypeDeclaration
    | classDeclaration
    | enumDeclaration
    ;

/* We use rule this even for void methods which cannot have [] after parameters.
   This simplifies grammar and we can consider void to be a type, which
   renders the [] matching as a context-sensitive issue or a semantic check
   for invalid return type after parsing.
 */
methodDeclaration
    : typeTypeOrVoid javaIdentifier formalParameters ('[' ']')*
      (THROWS qualifiedNameList)?
      methodBody
    ;

methodBody
    : block
    | ';'
    ;

typeTypeOrVoid
    : typeType
    | VOID
    ;

genericMethodDeclaration
    : typeParameters methodDeclaration
    ;

genericConstructorDeclaration
    : typeParameters constructorDeclaration
    ;

constructorDeclaration
    : javaIdentifier formalParameters (THROWS qualifiedNameList)? constructorBody=block
    ;

fieldDeclaration
    : typeType variableDeclarators ';'
    ;

interfaceBodyDeclaration
    : modifier* interfaceMemberDeclaration
    | tomDeclaration
    | ';'
    ;

interfaceMemberDeclaration
    : constDeclaration
    | interfaceMethodDeclaration
    | genericInterfaceMethodDeclaration
    | interfaceDeclaration
    | annotationTypeDeclaration
    | classDeclaration
    | enumDeclaration
    ;

constDeclaration
    : typeType constantDeclarator (',' constantDeclarator)* ';'
    ;

constantDeclarator
    : javaIdentifier ('[' ']')* '=' variableInitializer
    ;

// see matching of [] comment in methodDeclaratorRest
// methodBody from Java8
interfaceMethodDeclaration
    : interfaceMethodModifier* (typeTypeOrVoid | typeParameters annotation* typeTypeOrVoid)
      javaIdentifier formalParameters ('[' ']')* (THROWS qualifiedNameList)? methodBody
    ;

// Java8
interfaceMethodModifier
    : annotation
    | PUBLIC
    | ABSTRACT
    | DEFAULT
    | STATIC
    | STRICTFP
    ;

genericInterfaceMethodDeclaration
    : typeParameters interfaceMethodDeclaration
    ;

variableDeclarators
    : variableDeclarator (',' variableDeclarator)*
    ;

variableDeclarator
    : variableDeclaratorId ('=' variableInitializer)?
    ;

variableDeclaratorId
    : javaIdentifier ('[' ']')*
    ;

variableInitializer
    : arrayInitializer
    | expression
    ;

arrayInitializer
    : '{' (variableInitializer (',' variableInitializer)* (',')? )? '}'
    ;

classOrInterfaceType
    : javaIdentifier typeArguments? ('.' javaIdentifier typeArguments?)*
    ;

typeArgument
    : typeType
    | '?' ((EXTENDS | SUPER) typeType)?
    ;

qualifiedNameList
    : qualifiedName (',' qualifiedName)*
    ;

formalParameters
    : '(' formalParameterList? ')'
    ;

formalParameterList
    : formalParameter (',' formalParameter)* (',' lastFormalParameter)?
    | lastFormalParameter
    ;

formalParameter
    : variableModifier* typeType variableDeclaratorId
    ;

lastFormalParameter
    : variableModifier* typeType '...' variableDeclaratorId
    ;

qualifiedName
    : javaIdentifier ('.' javaIdentifier)*
    ;

literal
    : integerLiteral
    | floatLiteral
    | CHAR_LITERAL
    | STRING_LITERAL
    | BOOL_LITERAL
    | NULL_LITERAL
    ;

integerLiteral
    : DECIMAL_LITERAL
    | HEX_LITERAL
    | OCT_LITERAL
    | BINARY_LITERAL
    ;

floatLiteral
    : FLOAT_LITERAL
    | HEX_FLOAT_LITERAL
    ;

// ANNOTATIONS

annotation
    : '@' qualifiedName ('(' ( elementValuePairs | elementValue )? ')')?
    ;

elementValuePairs
    : elementValuePair (',' elementValuePair)*
    ;

elementValuePair
    : javaIdentifier '=' elementValue
    ;

elementValue
    : expression
    | annotation
    | elementValueArrayInitializer
    ;

elementValueArrayInitializer
    : '{' (elementValue (',' elementValue)*)? (',')? '}'
    ;

annotationTypeDeclaration
    : '@' INTERFACE javaIdentifier annotationTypeBody
    ;

annotationTypeBody
    : '{' (annotationTypeElementDeclaration)* '}'
    ;

annotationTypeElementDeclaration
    : modifier* annotationTypeElementRest
    | tomDeclaration
    | ';' // this is not allowed by the grammar, but apparently allowed by the actual compiler
    ;

annotationTypeElementRest
    : typeType annotationMethodOrConstantRest ';'
    | classDeclaration ';'?
    | interfaceDeclaration ';'?
    | enumDeclaration ';'?
    | annotationTypeDeclaration ';'?
    ;

annotationMethodOrConstantRest
    : annotationMethodRest
    | annotationConstantRest
    ;

annotationMethodRest
    : javaIdentifier '(' ')' defaultValue?
    ;

annotationConstantRest
    : variableDeclarators
    ;

defaultValue
    : DEFAULT elementValue
    ;

// STATEMENTS / BLOCKS

block
    : '{' blockStatement* '}'
    ;

blockStatement
    : localVariableDeclaration ';'
    | statement
    | localTypeDeclaration
    | tomStatement
    ;

localVariableDeclaration
    : variableModifier* typeType variableDeclarators
    ;

localTypeDeclaration
    : classOrInterfaceModifier*
      (classDeclaration | interfaceDeclaration)
    | ';'
    ;

statement
    : blockLabel=block
    | ASSERT expression (':' expression)? ';'
    | IF parExpression statement (ELSE statement)?
    | FOR '(' forControl ')' statement
    | WHILE parExpression statement
    | DO statement WHILE parExpression ';'
    | TRY block (catchClause+ finallyBlock? | finallyBlock)
    | TRY resourceSpecification block catchClause* finallyBlock?
    | SWITCH parExpression '{' switchBlockStatementGroup* switchLabel* '}'
    | SYNCHRONIZED parExpression block
    | RETURN expression? ';'
    | THROW expression ';'
    | BREAK javaIdentifier? ';'
    | CONTINUE javaIdentifier? ';'
    | SEMI
    | statementExpression=expression ';'
    | identifierLabel=javaIdentifier ':' statement
    ;

catchClause
    : CATCH '(' variableModifier* catchType javaIdentifier ')' block
    ;

catchType
    : qualifiedName ('|' qualifiedName)*
    ;

finallyBlock
    : FINALLY block
    ;

resourceSpecification
    : '(' resources ';'? ')'
    ;

resources
    : resource (';' resource)*
    ;

resource
    : variableModifier* classOrInterfaceType variableDeclaratorId '=' expression
    ;

/** Matches cases then statements, both of which are mandatory.
 *  To handle empty cases at the end, we add switchLabel* to statement.
 */
switchBlockStatementGroup
    : switchLabel+ blockStatement+
    ;

switchLabel
    : CASE (constantExpression=expression | enumConstantName=javaIdentifier) ':'
    | DEFAULT ':'
    ;

forControl
    : enhancedForControl
    | forInit? ';' expression? ';' forUpdate=expressionList?
    ;

forInit
    : localVariableDeclaration
    | expressionList
    ;

enhancedForControl
    : variableModifier* typeType variableDeclaratorId ':' expression
    ;

// EXPRESSIONS

parExpression
    : '(' expression ')'
    ;

expressionList
    : expression (',' expression)*
    ;

expression
    : primary
    | expression bop='.'
      (javaIdentifier
      | THIS
      | NEW nonWildcardTypeArguments? innerCreator
      | SUPER superSuffix
      | explicitGenericInvocation
      )
    | expression '[' expression ']'
    | expression '(' expressionList? ')'
    | NEW creator
    | '(' typeType ')' expression
    | expression postfix=('++' | '--')
    | prefix=('+'|'-'|'++'|'--') expression
    | prefix=('~'|'!') expression
    | expression bop=('*'|'/'|'%') expression
    | expression bop=('+'|'-') expression
    | expression ('<' '<' | '>' '>' '>' | '>' '>') expression
    | expression bop=('<=' | '>=' | '>' | '<') expression
    | expression bop=INSTANCEOF typeType
    | expression bop=('==' | '!=') expression
    | expression bop='&' expression
    | expression bop='^' expression
    | expression bop='|' expression
    | expression bop='&&' expression
    | expression bop='||' expression
    | expression bop='?' expression ':' expression
    | <assoc=right> expression
      bop=('=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '>>=' | '>>>=' | '<<=' | '%=')
      expression
    | lambdaExpression // Java8

    // Java 8 methodReference
    | expression '::' typeArguments? javaIdentifier
    | typeType '::' (typeArguments? javaIdentifier | NEW)
    | classType '::' typeArguments? NEW
    | tomTerm
    ;

// Java8
lambdaExpression
    : lambdaParameters '->' lambdaBody
    ;

// Java8
lambdaParameters
    : javaIdentifier
    | '(' formalParameterList? ')'
    | '(' javaIdentifier (',' javaIdentifier)* ')'
    ;

// Java8
lambdaBody
    : expression
    | block
    ;

primary
    : '(' expression ')'
    | THIS
    | SUPER
    | literal
    | javaIdentifier
    | typeTypeOrVoid '.' CLASS
    | nonWildcardTypeArguments (explicitGenericInvocationSuffix | THIS arguments)
    ;

classType
    : (classOrInterfaceType '.')? annotation* javaIdentifier typeArguments?
    ;

creator
    : nonWildcardTypeArguments createdName classCreatorRest
    | createdName (arrayCreatorRest | classCreatorRest)
    ;

createdName
    : javaIdentifier typeArgumentsOrDiamond? ('.' javaIdentifier typeArgumentsOrDiamond?)*
    | primitiveType
    ;

innerCreator
    : javaIdentifier nonWildcardTypeArgumentsOrDiamond? classCreatorRest
    ;

arrayCreatorRest
    : '[' (']' ('[' ']')* arrayInitializer | expression ']' ('[' expression ']')* ('[' ']')*)
    ;

classCreatorRest
    : arguments classBody?
    ;

explicitGenericInvocation
    : nonWildcardTypeArguments explicitGenericInvocationSuffix
    ;

typeArgumentsOrDiamond
    : '<' '>'
    | typeArguments
    ;

nonWildcardTypeArgumentsOrDiamond
    : '<' '>'
    | nonWildcardTypeArguments
    ;

nonWildcardTypeArguments
    : '<' typeList '>'
    ;

typeList
    : typeType (',' typeType)*
    ;

typeType
    : annotation? (classOrInterfaceType | primitiveType) ('[' ']')*
    ;

primitiveType
    : BOOLEAN
    | CHAR
    | BYTE
    | SHORT
    | INT
    | LONG
    | FLOAT
    | DOUBLE
    ;

typeArguments
    : '<' typeArgument (',' typeArgument)* '>'
    ;

superSuffix
    : arguments
    | '.' javaIdentifier arguments?
    ;

explicitGenericInvocationSuffix
    : SUPER superSuffix
    | javaIdentifier arguments
    ;

arguments
    : '(' expressionList? ')'
    ;

//IDENTIFIER or a Tom Keyword
javaIdentifier
  : IS_FSYM
  | IS_SORT
  | MAKE
  | MAKE_EMPTY
  | MAKE_APPEND
  | MAKE_INSERT
  | GET_SLOT
  | GET_DEFAULT
  | GET_ELEMENT
  | GET_HEAD
  | GET_TAIL
  | GET_SIZE
  | IS_EMPTY
  | IMPLEMENT
  | EQUALS
  | VISIT
  | WHEN
  | IDENTIFIER
  ;

/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2017, Universite de Lorraine
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

tomDeclaration
  : strategyStatement
  | includeStatement
  | gomStatement
  | ruleStatement
  | typeterm
  | operator
  | oplist
  | oparray
  ;

tomStatement
  : tomDeclaration
  | matchStatement
  ;

tomTerm
  : bqcomposite
  | metaquote
  ;

metaquote
  : METAQUOTE
  ;

matchStatement
  : MATCH (LPAREN (bqterm (COMMA bqterm)*)? RPAREN)? LBRACE actionRule* RBRACE 
  ;

strategyStatement
  : STRATEGY tomIdentifier LPAREN slotList? RPAREN EXTENDS bqterm LBRACE visit* RBRACE
  ;

includeStatement
  : INCLUDE LBRACE (DOT* SLASH)? tomIdentifier ((DOT|SLASH|BACKSLASH) tomIdentifier)* RBRACE 
  ;

//GOM grammar? TODO FIX
gomStatement
  : GOM gomOptions? unknownBlock
  ;

//TODO %rule
ruleStatement
  : RULE unknownBlock
  ;

unknownBlock
  : LBRACE (tomBlock | unknownBlock | .)*? RBRACE
  ;

gomOptions
  : LPAREN gomOption (COMMA gomOption)* RPAREN
  ;
  
gomOption
  : '--' tomIdentifier
  ;

visit
  : VISIT tomIdentifier LBRACE actionRule* RBRACE
  ;

actionRule
  : patternlist ((AND | OR) constraint)? ARROW tomBlock
  | patternlist ((AND | OR) constraint)? ARROW bqterm
  | c=constraint ARROW tomBlock
  | c=constraint ARROW bqterm
  ;

tomBlock 
  : LBRACE (tomBlock | blockStatement)*? RBRACE
  ;

slotList
  : slot (COMMA slot)*
  ;

slot
  : id1=tomIdentifier COLON? id2=tomIdentifier
  ;

//TODO FIX when?
patternlist
  : pattern (COMMA pattern)* (WHEN term (COMMA term)*)?
  ;

constraint
  : constraint AND constraint
  | constraint OR constraint
  | pattern match_symbol='<' '<' bqterm
  | term GT term
  | term GE term
  | term LT term
  | term LE term
  | term EQUAL term
  | term NOTEQUAL term
  | LPAREN c=constraint RPAREN
  ;

//used in constraints 
term
  : var=tomIdentifier STAR?
  | fsym=tomIdentifier LPAREN (term (COMMA term)*)? RPAREN 
  | constant
  ;

// may be change this syntax: `term:sort
// retricted form of bqterm
// used in rhs, match, strategy
bqterm
  : codomain=tomIdentifier? BQUOTE? fsym=tomIdentifier LPAREN (bqterm (COMMA bqterm)*)? RPAREN 
  | codomain=tomIdentifier? BQUOTE? fsym=tomIdentifier LBRACK (pairSlotBqterm (COMMA pairSlotBqterm)*)? RBRACK 
  | codomain=tomIdentifier? BQUOTE? var=tomIdentifier STAR?
  | codomain=tomIdentifier? constant
  | UNDERSCORE
  ;

pairSlotBqterm
  : tomIdentifier ASSIGN bqterm
  ;

// general form of bqterm
// used as island nd in metaquote
bqcomposite
  : BQUOTE fsym=tomIdentifier LBRACK (pairSlotBqterm (COMMA pairSlotBqterm)*)? RBRACK 
  | BQUOTE composite
  ;

composite
  : fsym=tomIdentifier LPAREN (composite (COMMA? composite)*)? RPAREN
  | LPAREN composite* RPAREN
  | var=tomIdentifier STAR?
  | constant
  | UNDERSCORE
  | expression //java
  ;

pattern
  : tomIdentifier AT pattern 
  | ANTI pattern
  | fsymbol explicitArgs
  | fsymbol implicitArgs
  | var=tomIdentifier STAR?
  | UNDERSCORE STAR?
  | constant (PIPE constant)* 
  ;

fsymbol 
  : headSymbol
  | LPAREN headSymbol (PIPE headSymbol)+ RPAREN
  ;

headSymbol
  : tomIdentifier QMARK?
  | tomIdentifier DQMARK?
  | constant
  ;

constant
  : SUB? DECIMAL_LITERAL
  | SUB? FLOAT_LITERAL
  | CHAR_LITERAL
  | EXTENDED_CHAR_LITERAL
  | STRING_LITERAL
  ;

explicitArgs
  : LPAREN (pattern (COMMA pattern)*)? RPAREN
  ;

implicitArgs
  : LBRACK (tomIdentifier ASSIGN pattern (COMMA tomIdentifier ASSIGN pattern)*)? RBRACK 
  ;

/*
 * signature
 */
typeterm
  : TYPETERM type=tomIdentifier (EXTENDS supertype=tomIdentifier)? LBRACE 
    implement isSort? equalsTerm?
    RBRACE
  ;

operator
  : OP codomain=tomIdentifier opname=tomIdentifier LPAREN slotList? RPAREN LBRACE 
    (isFsym | make | getSlot | getDefault)*
    RBRACE
  ;

oplist
  : OPLIST codomain=tomIdentifier opname=tomIdentifier LPAREN domain=tomIdentifier STAR RPAREN LBRACE 
    (isFsym | makeEmptyList | makeInsertList | getHead | getTail | isEmptyList)*
    RBRACE
  ;

oparray
  : OPARRAY codomain=tomIdentifier opname=tomIdentifier LPAREN domain=tomIdentifier STAR RPAREN LBRACE 
    (isFsym | makeEmptyArray | makeAppendArray | getElement | getSize)*
    RBRACE
  ;

termBlock
  : LBRACE expression RBRACE
  ;

implement
  : IMPLEMENT LBRACE typeType RBRACE
  ;

equalsTerm
  : EQUALS LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
  ;

isSort
  : IS_SORT LPAREN tomIdentifier RPAREN termBlock
  ;

isFsym
  : IS_FSYM LPAREN tomIdentifier RPAREN termBlock
  ;

make
  : MAKE LPAREN (tomIdentifier (COMMA tomIdentifier)*)? RPAREN termBlock
  ;

makeEmptyList
  : MAKE_EMPTY LPAREN RPAREN termBlock
  ;

makeEmptyArray
  : MAKE_EMPTY LPAREN tomIdentifier RPAREN termBlock
  ;

makeAppendArray
  : MAKE_APPEND LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
  ;
  
makeInsertList
  : MAKE_INSERT LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
  ;
  
getSlot
  : GET_SLOT LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
  ;

getHead
  : GET_HEAD LPAREN tomIdentifier RPAREN termBlock
  ;

getTail
  : GET_TAIL LPAREN tomIdentifier RPAREN termBlock
  ;

getElement
  : GET_ELEMENT LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
  ;

isEmptyList
  : IS_EMPTY LPAREN tomIdentifier RPAREN termBlock
  ;

getSize
  : GET_SIZE LPAREN tomIdentifier RPAREN termBlock
  ;

getDefault
  : GET_DEFAULT LPAREN tomIdentifier RPAREN termBlock
  ;

//IDENTIFIER or a Java Keyword
tomIdentifier
  : ABSTRACT
  | ASSERT
  | BOOLEAN
  | BREAK
  | BYTE
  | CASE
  | CATCH
  | CHAR
  | CLASS
  | CONST
  | CONTINUE
  | DEFAULT
  | DO
  | DOUBLE
  | ELSE
  | ENUM
  | FINAL
  | FINALLY
  | FLOAT
  | FOR
  | IF
  | GOTO
  | IMPLEMENTS
  | IMPORT
  | INSTANCEOF
  | INT
  | INTERFACE
  | LONG
  | NATIVE
  | NEW
  | PACKAGE
  | PRIVATE
  | PROTECTED
  | PUBLIC
  | RETURN
  | SHORT
  | STATIC
  | STRICTFP
  | SUPER
  | SWITCH
  | SYNCHRONIZED
  | THIS
  | THROW
  | THROWS
  | TRANSIENT
  | TRY
  | VOID
  | VOLATILE
  | WHILE
  | BOOL_LITERAL
  | IDENTIFIER
  ;
