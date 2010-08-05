/*
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2010, INPL, INRIA
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
 */

grammar NewBackQuoteLanguage;

options {
  backtrack=true;
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=BackQuoteTokens;
}

@header {
  //package tom.engine.parser;
  package newtom;
}


@lexer::header {
  //package tom.engine.parser;
  package newtom;
}

@lexer::members {
  public static int nesting = 0;
  //protected boolean enumIsKeyword = true;
  //protected boolean assertIsKeyword = true;
}


//Starting point
//backQuoteTerm : '`(' expression ')' -> ^(BackQuoteTerm expression ) ;
//backQuoteConstruct : '`(' backQuoteTerm ')' -> ^(BackQuoteConstruct backQuoteTerm) ;

//backQuoteTerm :
//  id=Identifier '*' -> ^(BQVariableStar $id) // x*
//  | name=Identifier LPAREN cl=compositeList RPAREN -> ^(BQNamedComposite $name $cl) // f(...)
//  | expression -> ^(BQExpression expression)
// to add
//  | cl=compositeList -> ^(BQComposite $cl)  //BQComposite(cl:CompositeList)
//  ;

///
/*
compositeList : 
  backQuoteTerm (',' backQuoteTerm)* -> ^(CompositeList backQuoteTerm+)
  ;
///*/



// in TomLanguage.g
//compositeList
//compositeTerm
//composite

/**
 * Partial Java
 */
typeList :
  type (',' type)* -> ^(TypeList type+)
  ;

type :
  classOrInterfaceType ('[' ']')* ->  ^(ClassOrInterfaceType classOrInterfaceType) // NOT CORRECT
  | primitiveType ('[' ']')* -> primitiveType // NOT CORRECT
  ;
  
classOrInterfaceType : 
  Identifier typeArguments? ('.' Identifier typeArguments? )* ->  ^(IndependentType Identifier ^(TypeArgumentList ))
  ;

primitiveType : 
// 'boolean' -> Boolean
  |   'char'    -> Char
  |   'byte'    -> Byte
  |   'short'   -> Short
//    |   'int'     -> Int
  |   'long'    -> Long
  |   'float'   -> Float
  |   'double'  -> Double
  ;

literal : 
  integerLiteral
  |   l=FloatingPointLiteral -> ^(FloatingPointLiteral $l)
  |   l=CharacterLiteral -> ^(CharacterLiteral $l)
  |   l=StringLiteral -> ^(StringLiteral $l)
  |   booleanLiteral
  |   'null' -> Null
  ;

integerLiteral : 
  i=HexLiteral -> ^(HexLiteral $i)
  |   i=OctalLiteral -> ^(OctalLiteral $i)
  |   i=DecimalLiteral -> ^(DecimalLiteral $i)
  ;

booleanLiteral : 
  'true'  -> ^(BooleanLiteral True)
  | 'false' -> ^(BooleanLiteral False)
  ;

variableInitializer : 
  arrayInitializer
  | expression -> ^(ExpressionToVariableInitializer expression)
  ;

arrayInitializer : 
  '{' (variableInitializer (',' variableInitializer)* (',')? )? '}' ->  ^(ArrayInitializer variableInitializer*)
  ;

typeArguments : 
  '<' typeArgument (',' typeArgument)* '>' ->  ^(TypeArgumentList typeArgument+)
  ;

typeArgument : 
  type -> ^(ExplicitTypeArgument type)
  | '?' ((e='extends' | s='super') type)?
      -> {e!=null}? ^(ExtendTypeArgument type)
      -> {s!=null}? ^(SuperTypeArgument type)
      ->            WildCardTypeArgument
  ;

// EXPRESSIONS

parExpression : 
  LPAREN! expression RPAREN!
  ;
    
expressionList : 
  expression (',' expression)* -> ^(ExpressionList expression+)
  ;

expression : 
  conditionalExpression (o=assignmentOperator expression)?
      -> {o==null}? conditionalExpression
      ->            ^(Assignment $o conditionalExpression expression)
  ;
    
assignmentOperator : 
    '='  -> Assign
  | '+=' -> PlusAssign
  | '-=' -> MinusAssign
  | '*=' -> TimesAssign
  | '/=' -> DivAssign
  | '&=' -> AndAssign
  | '|=' -> OrAssign
  | '^=' -> XOrAssign
  | '%=' -> ModAssign
  | ('<' '<' '=')=> t1='<' t2='<' t3='=' 
        { $t1.getLine() == $t2.getLine() &&
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
      -> LeftShiftAssign
  | ('>' '>' '>' '=')=> t1='>' t2='>' t3='>' t4='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() &&
          $t3.getLine() == $t4.getLine() && 
          $t3.getCharPositionInLine() + 1 == $t4.getCharPositionInLine() }?
        -> UnsignedRightShiftAssign
  | ('>' '>' '=')=> t1='>' t2='>' t3='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
      -> SignedRightShiftAssign
  ;

conditionalExpression : 
  conditionalOrExpression ( '?' (texpr=expression) ':' (eexpr=expression) )? 
    -> {texpr==null}? conditionalOrExpression
    ->                ^(ConditionalExpression conditionalOrExpression $texpr $eexpr)
  ;

conditionalOrExpression : 
  conditionalAndExpression ( '||' conditionalAndExpression )*
      -> ^(AssociativeOperation ConditionalOr ^(ExpressionList conditionalAndExpression+))
  ;

conditionalAndExpression : 
  inclusiveOrExpression ( '&&' inclusiveOrExpression)*
      -> ^(AssociativeOperation ConditionalAnd ^(ExpressionList inclusiveOrExpression+))
  ;

inclusiveOrExpression : 
  exclusiveOrExpression ( '|' exclusiveOrExpression )*
      -> ^(AssociativeOperation InclusiveOr ^(ExpressionList exclusiveOrExpression+))
  ;

exclusiveOrExpression : 
  andExpression ( '^' andExpression )*
        -> ^(AssociativeOperation ExclusiveOr ^(ExpressionList andExpression+))
  ;

andExpression : 
  (a=equalityExpression -> $a) ( '&' b=equalityExpression -> ^(BinaryAnd $andExpression $b) )*
  ;


equalityExpression :
  (a=instanceOfExpression -> $a) ( ( '==' b=instanceOfExpression -> ^(BinaryEqual $equalityExpression $b))
  | '!=' b=instanceOfExpression -> ^(BinaryNotEqual $equalityExpression $b) )*
  ;

instanceOfExpression : 
  relationalExpression ('instanceof' t=type)?
      -> {t==null}? relationalExpression
      ->            ^(InstanceOf relationalExpression $t)
  ;

relationalExpression : 
  shiftExpression ( o=relationalOp shiftExpression )*
      -> {o==null}? shiftExpression
      ->            ^(AssociativeOperation relationalOp ^(ExpressionList shiftExpression+))
  ;
    
relationalOp : 
  ('<' '=')=> t1='<' t2='=' 
      { $t1.getLine() == $t2.getLine() && 
        $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
      -> LowerOrEqual
  | ('>' '=')=> t1='>' t2='=' 
      { $t1.getLine() == $t2.getLine() && 
        $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
      -> GreaterOrEqual
  | '<' -> Lower
  | '>' -> Greater
  ;

shiftExpression : 
  additiveExpression ( o=shiftOp additiveExpression )*
      -> {o==null}? additiveExpression
      ->            ^(AssociativeOperation $o ^(ExpressionList additiveExpression+))
  ;

shiftOp : 
  ('<' '<')=> t1='<' t2='<' 
      { $t1.getLine() == $t2.getLine() && 
        $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
      -> LeftShift
  | ('>' '>' '>')=> t1='>' t2='>' t3='>' 
      { $t1.getLine() == $t2.getLine() && 
        $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
        $t2.getLine() == $t3.getLine() && 
        $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
      -> UnsignedRightShift
  | ('>' '>')=> t1='>' t2='>'
      { $t1.getLine() == $t2.getLine() && 
        $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
      -> SignedRightShift
  ;

additiveExpression : 
  (a=multiplicativeExpression -> $a) ( ( '+' b=multiplicativeExpression -> ^(BinaryPlus $additiveExpression $b))
  | '-' b=multiplicativeExpression -> ^(BinaryMinus $additiveExpression $b) )* 
  ;

multiplicativeExpression : 
  (a=unaryExpression -> $a) ( ( '*' b=unaryExpression -> ^(BinaryMult $multiplicativeExpression $b))
  | '/' b=unaryExpression -> ^(BinaryDiv $multiplicativeExpression $b)
  | '%' b=unaryExpression -> ^(BinaryModulo $multiplicativeExpression $b) )* 
  ;

unaryExpression : 
  '+' unaryExpression -> ^(UnaryOperation UPlus unaryExpression)
  | '-' unaryExpression -> ^(UnaryOperation UMinus unaryExpression)
  | '++' unaryExpression -> ^(UnaryOperation PreInc unaryExpression)
  | '--' unaryExpression -> ^(UnaryOperation PreDec unaryExpression)
  | unaryExpressionNotPlusMinus
  ;

unaryExpressionNotPlusMinus : 
  '~' unaryExpression -> ^(UnaryOperation BitwiseNot unaryExpression)
  | '!' unaryExpression -> ^(UnaryOperation LogicalNot unaryExpression)
  | castExpression
  | primary selector* (i='++'|d='--')?
      -> {i!=null}? ^(UnaryOperation PostInc ^(Primary primary ^(SuffixList selector*)))
      -> {d!=null}? ^(UnaryOperation PostDec ^(Primary primary ^(SuffixList selector*)))
      ->            ^(Primary primary ^(SuffixList selector*))
  ;

castExpression : 
  LPAREN primitiveType RPAREN unaryExpression
      ->  ^(TypeCast primitiveType unaryExpression)
  | LPAREN (t=type | expression) RPAREN unaryExpressionNotPlusMinus
      -> {t!=null}? ^(TypeCast $t unaryExpressionNotPlusMinus)
      ->            ^(ExpressionCast expression unaryExpressionNotPlusMinus)  // semantics ?
  ;

primary : 
  parExpression
      ->  ^(ExpressionToPrimary parExpression)
  | 'this' ('.' Identifier)* s=identifierSuffix?
      -> {s==null}? ^(This ^(QualifiedName Identifier*) EmptySuffix)
      ->            ^(This ^(QualifiedName Identifier*) $s)
  | 'super' superSuffix
      ->  ^(Super superSuffix)
  | literal
  | 'new'! creator
  | Identifier ('.' Identifier)* s=identifierSuffix?
      -> {s==null}? ^(QualifiedNameToPrimary ^(QualifiedName Identifier*) EmptySuffix)
      ->            ^(QualifiedNameToPrimary ^(QualifiedName Identifier*) $s)
  | primitiveType ('[' ']')* '.' 'class'
      -> ^(Class primitiveType) // NOT CORRECT 
  | 'void' '.' 'class'
      ->  ^(Class Void)
  ;

identifierSuffix : 
  ('[' ']')+ '.' 'class'
       ->  ^(ArrayClassSuffix {0}) // NOT CORRECT // DOES NOT WORK
  | ('[' expression ']')+ // can also be matched by selector, but do here
      ->  ^(ArrayIndexSuffix ^(ExpressionList expression+))
  | arguments
      ->  ^(ArgumentsSuffix arguments)
  | '.' 'class'
      ->  ^(ArrayClassSuffix {0}) // DOES NOT WORK
  | '.'! explicitGenericInvocation
  | '.' 'this'
      ->  ThisSuffix
  | '.' 'super' arguments
      ->  ^(SuperSuffix ^(SuperConstruction arguments))
  | '*'
      -> ^(VarStarSuffix )
  ;

explicitGenericInvocation : 
  nonWildcardTypeArguments Identifier arguments
    ->  ^(ExplicitGenericInvocation nonWildcardTypeArguments Identifier arguments)
  ;

creator : 
  nonWildcardTypeArguments createdName arguments
      ->  ^(ClassCreator nonWildcardTypeArguments createdName arguments)
  | createdName
      (   arrayCreatorRest[$createdName.tree] -> arrayCreatorRest
      | arguments
          ->  ^(ClassCreator ^(TypeList ) createdName arguments)
      )
  ;

createdName : 
  classOrInterfaceType -> ^(ClassOrInterfaceType classOrInterfaceType)
  | primitiveType
  ;
    
arrayCreatorRest[Tree type] : 
  '['
      (   ']' ('[' ']')* arrayInitializer
          ->  ^(InitializedArrayCreator {type} arrayInitializer) // NOT CORRECT
      | expression ']' ('[' expression ']')* ('[' ']')*
          ->  ^(InitializedArrayCreator {type} ^(ArrayInitializer )) // NOT CORRECT
      )
  ;

nonWildcardTypeArguments : '<'! typeList '>'! ;

selector : 
  '.' Identifier a=arguments?
      -> {a==null}? ^(VariableAccessSuffix Identifier)
      ->            ^(CallSuffix Identifier $a)
  | '.' 'this' -> ThisSuffix
  | '.' 'super' superSuffix -> ^(SuperSuffix superSuffix)
/*    |   '.'! 'new'! innerCreator */
  | '[' expression ']' -> ^(ArrayIndexSuffix ^(ExpressionList expression))
  ;

superSuffix : 
  arguments ->  ^(SuperConstruction arguments)
  | '.' Identifier a=arguments?
      -> {a==null}? ^(SuperVariableAccess Identifier)
      ->            ^(SuperCall Identifier arguments)
  ;

arguments : 
  LPAREN l=expressionList? RPAREN
      -> {l==null}? ^(ExpressionList )
      -> $l
  ;

// Lexer

//partial Java lexer
HexLiteral : '0' ('x'|'X') HexDigit+ IntegerTypeSuffix? ;

DecimalLiteral : ('0' | '1'..'9' '0'..'9'*) IntegerTypeSuffix? ;

OctalLiteral : '0' ('0'..'7')+ IntegerTypeSuffix? ;

fragment
HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
IntegerTypeSuffix : ('l'|'L') ;

FloatingPointLiteral :
  ('0'..'9')+ '.' ('0'..'9')* Exponent? FloatTypeSuffix?
  | '.' ('0'..'9')+ Exponent? FloatTypeSuffix?
  | ('0'..'9')+ Exponent FloatTypeSuffix?
  | ('0'..'9')+ FloatTypeSuffix
  ;

fragment
Exponent : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
FloatTypeSuffix : ('f'|'F'|'d'|'D') ;

CharacterLiteral : 
  '\'' ( EscapeSequence | ~('\''|'\\') ) '\''
  ;

StringLiteral : 
  '"' ( EscapeSequence | ~('\\'|'"') )* '"'
  ;

fragment
EscapeSequence : 
  '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
  | UnicodeEscape
  | OctalEscape
  ;

fragment
OctalEscape : 
  '\\' ('0'..'3') ('0'..'7') ('0'..'7')
  | '\\' ('0'..'7') ('0'..'7')
  | '\\' ('0'..'7')
  ;

fragment
UnicodeEscape : 
  '\\' 'u' HexDigit HexDigit HexDigit HexDigit
  ;

/*ENUM:   'enum' {if (!enumIsKeyword) $type=Identifier;}
    ;
    
ASSERT
    :   'assert' {if (!assertIsKeyword) $type=Identifier;}
    ;*/
    

/* I found this char range in JavaCC's grammar, but Letter and Digit overlap.
   Still works, but...
 */

Identifier:
  Letter (Letter|JavaIDDigit)*
  ;

fragment
Letter : 
  '\u0024' | '\u005f' |
  '\u0041'..'\u005a' |
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
JavaIDDigit :
  '\u0030'..'\u0039' |
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

LPAREN : '(' { nesting++; } { System.out.println("backquote nesting++ = " + nesting);}
  ;

RPAREN : ')'
  {
    if ( nesting<=0 ) {
      System.out.println("exit backquote language and emit(Token.EOF)\n");
      emit(Token.EOF_TOKEN);
    }
    else {
      nesting--;
      System.out.println("backquote nesting-- = " + nesting);
    }
  }
  ;

WS : 
  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
  ;

ML_COMMENT : 
  '/*' ( options {greedy=false;} : . )* '*/'
  {$channel=HIDDEN;}
  ;

SL_COMMENT :
  '//' ~('\n'|'\r')* '\r'? '\n'
  {$channel=HIDDEN;}
  ;
