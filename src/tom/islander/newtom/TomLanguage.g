/*
 * 
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
 **/

grammar TomLanguage;

options {
  backtrack=true;
//  memoize=true;
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=NewTomAstTokens;
}

@header {
  //package tom.engine.parser;
  package newtom;
//  import org.antlr.runtime.tree.Tree;
}

@lexer::header {
  //package tom.engine.parser;
  package newtom;
}

/*@lexer::members {
  protected boolean enumIsKeyword = true;
  protected boolean assertIsKeyword = true;
}*/



/**
 * Tom : the grammar has been written by following the 'official grammar' on the wiki
 */

// Starting point 
tomCompilationUnit :
  blockList EOF -> ^(TomCompilationUnit blockList)
  ;

blockList :
  (block)* -> ^(BlockList (block)*)
  ;

block :
  matchConstruct
  | operator
  | operatorList
  | operatorArray
  | typeTerm
  | includeConstruct
  | strategyConstruct
  | LBRACE blockList RBRACE -> blockList
  //| compositeTerm // to modify
  | backQuoteTerm // compositeTerm
  ;
/*
  | code
  | gomConstruct // gomsignature
*/


/*
gomConstruct :
*/
/*
backquoteTerm :
*/


goalLanguageBlock :
  LBRACE (block)* RBRACE -> ^(BlockList (block)*)  /*^(BlockList blockList)*/
  //LBRACE blockList RBRACE -> blockList  /*^(BlockList blockList)*/
  ;

matchConstruct :
  MATCH LPAREN matchArguments RPAREN LBRACE patternActionList RBRACE -> ^(MatchConstruct matchArguments patternActionList)
  | MATCH LBRACE constraintActionList RBRACE -> ^(MatchConstructWithoutArgs constraintActionList)
  ;

pair :
  t=Identifier n=Identifier -> ^(Variable ^(Name $n) ^(Type $t))
  ;

pairList :
  pair (COMMA pair)* -> ^(TomTermList pair+ )
  ;

matchArguments :
  termList //pairTerm (COMMA pairTerm)* -> ^(TomTermList pairTerm+)
  ;

constraintActionList :
  (constraintAction)* -> ^(ConstraintActionList (constraintAction)*);

patternActionList :
  (patternAction)* -> ^(PatternActionList (patternAction)*);

patternAction :
  patternList ARROW LBRACE blockList RBRACE
    -> ^(PatternAction patternList blockList)
  | labelName=Identifier COLON patternList ARROW LBRACE blockList RBRACE
    -> ^(LabelledPatternAction patternList blockList $labelName)
  ;

/*compositeTerm :
  Identifier -> ^(CompositeTerm Identifier ) //temporary rule
  ;*/
//  | id=Identifier -> ^(CompositeTerm $id ) //temporary rule
//  : unit=(javaCompilationUnit|matchConstruct)* -> ^(CompositeUnit $unit*)
/*  : cu=(matchConstruct* javaCompilationUnit (matchConstruct|javaCompilationUnit)*) -> ^(CompositeTerm ^(CompilationUnit $cu )) */
//  cu=compilationUnitList /*(ct=compilationUnit)**/ -> ^(CompositeTerm
//  compilationUnitList /*^(BlockList ($ct)* )*/) /*(compilationUnit)* ))*/
//  ;

//compilationUnitList :
//  (l=compilationUnit+)? -> ^(BlockList $l? )
//  ;

//compilationUnit : 
//  matchConstruct
//  | LBRACE blockList RBRACE -> ^(BlockList blockList)
//  ;

// Is it necessary to create a node for a plainPattern ?
pattern : 
  plainPattern -> ^(Pattern plainPattern )
  | annotatedName=Identifier AT plainPattern -> ^(AnnotedPattern plainPattern Identifier[annotatedName])
  ;

patternList : 
  pattern (COMMA pattern)* -> ^(PatternList pattern+)
  ;

plainPattern : 
  UNDERSCORE STAR -> ^(PlainPattern ^(UnamedVariableStar ) )
  | UNDERSCORE -> ^(PlainPattern ^(UnamedVariable ) )
//  | ANTI_SYM Identifier STAR -> ^(PlainPattern ^(AntiVariableStar ^(Name Identifier)  ^(EmptyType )) )
  | ANTI_SYM Identifier -> ^(PlainPattern ^(AntiVariable ^(Name Identifier)  ^(EmptyType )) )
  | Identifier STAR -> ^(PlainPattern ^(VariableStar ^(Name Identifier )  ^(EmptyType )) )
  | Identifier -> ^(PlainPattern ^(Variable ^(Name Identifier )  ^(EmptyType )) )
  | ANTI_SYM headSymbolList tail -> ^(AntiSymbolList headSymbolList tail)
  | headSymbolList tail -> ^(SymbolList headSymbolList tail)
  | explicitTermList -> ^(ExplicitTermList explicitTermList ) //^(PPExplicitTermList explicitTermList )
  | xmlTerm
  ;

constraintAction : 
constraint ARROW LBRACE blockList RBRACE -> ^(ConstraintAction constraint blockList)
  ;

constraint : 
  matchConstraint //left=pattern MATCH_CONSTRAINT /*t=Identifier? right=term[t]*/ right=term -> ^(MatchConstraint $left $right)
/*  | left=constraint AND_CONNECTOR right=constraint -> ^(AndConstraint $left $right)*/
  | andConstraint
  | simpleAndConstraint
/*  | left=constraint OR_CONNECTOR right=constraint -> ^(OrConstraint $left $right) */
  | orConstraint
  | simpleOrConstraint
  | LPAREN constraint RPAREN -> constraint
  | numOperator
  ;

// 'or' and 'and' constraints have the same priority as in Java
orConstraint : 
  simpleOrConstraint OR_CONNECTOR andConstraint -> ^(OrConstraint simpleOrConstraint andConstraint)
  | matchConstraint OR_CONNECTOR andConstraint -> ^(OrConstraint matchConstraint andConstraint)
  | numOperator OR_CONNECTOR andConstraint -> ^(OrConstraint numOperator andConstraint)
  | simpleOrConstraint
  ;

simpleOrConstraint : 
  m1=matchConstraint OR_CONNECTOR m2=matchConstraint -> ^(OrConstraint $m1 $m2 )
  | o1=numOperator OR_CONNECTOR o2=numOperator -> ^(OrConstraint $o1 $o2 )
  | numOperator OR_CONNECTOR matchConstraint -> ^(OrConstraint numOperator matchConstraint )
  | matchConstraint OR_CONNECTOR numOperator -> ^(OrConstraint matchConstraint numOperator )
  ;

andConstraint :
  matchConstraint AND_CONNECTOR andConstraint -> ^(AndConstraint matchConstraint andConstraint)
  | numOperator AND_CONNECTOR andConstraint -> ^(AndConstraint numOperator andConstraint )
  | simpleAndConstraint
  ;

simpleAndConstraint : 
  m1=matchConstraint AND_CONNECTOR m2=matchConstraint -> ^(AndConstraint $m1 $m2)
  | matchConstraint AND_CONNECTOR numOperator -> ^(AndConstraint matchConstraint numOperator)
  | numOperator AND_CONNECTOR matchConstraint -> ^(AndConstraint numOperator matchConstraint )
  | o1=numOperator AND_CONNECTOR o2=numOperator -> ^(AndConstraint $o1 $o2 )
  ;

matchConstraint : 
  left=pattern MATCH_CONSTRAINT right=term -> ^(MatchConstraint $left $right)
  ;

numOperator : 
  left=term LESSOREQUAL_CONSTRAINT right=term
    -> ^(OpConstraint $left $right ^(NumLessOrEqualThan ))
  | left=term GREATEROREQUAL_CONSTRAINT right=term
    -> ^(OpConstraint $left $right ^(NumGreaterOrEqualThan ))
  | left=term DOUBLEEQ right=term
    -> ^(OpConstraint $left $right ^(NumEqual ))
  | left=term DIFFERENT_CONSTRAINT right=term
    -> ^(OpConstraint $left $right ^(NumDifferent))
  | left=term XML_START right=term
    -> ^(OpConstraint $left $right ^(NumLessThan ))
  | left=term XML_CLOSE right=term
    -> ^(OpConstraint $left $right ^(NumGreaterThan ))
  ;

/*type : 
  Identifier -> ^(Type Identifier)
  ;*/

term : //why 'Identifier? Identifier' and '?Identifier Identifier STAR' do not work fine ???
  t1=Identifier n1=Identifier STAR -> ^(VariableStar ^(Name $n1 ) ^(Type $t1) )
  | n2=Identifier STAR -> ^(VariableStar ^(Name $n2 ) ^(EmptyType ) )
  | t3=Identifier n3=Identifier -> ^(Variable ^(Name $n3 ) ^(Type $t3) )
  | n4=Identifier (LPAREN RPAREN)? -> ^(Variable ^(Name $n4 ) ^(EmptyType )) // '(LPAREN RPAREN)?' in order to regognize correctly a()
  | name=Identifier LPAREN termList RPAREN -> ^(NamedTermList ^(Name $name ) termList)
  ;
/*term[Token type]
  : Identifier
    -> {type!=null}? ^(Variable ^(Name Identifier) ^(Type Identifier[type]))
    -> ^(Variable ^(Name Identifier) ^(EmptyType ))
  | Identifier STAR
    -> {type!=null}? ^(VariableStar ^(Name Identifier) ^(Type Identifier[type]))
    -> ^(VariableStar ^(Name Identifier) ^(EmptyType ))
  | Identifier LPAREN tl=(noTypedTerm (COMMA noTypedTerm)*)? RPAREN -> ^(NamedTermList ^(Name Identifier) ^(TomTermList $tl))
  ;*/

termList : 
  term (COMMA term)* -> ^(TomTermList term+)
  ;

//XML
xmlTerm : 
  XML_START xmlNameList xmlAttrList XML_CLOSE_SINGLETON -> ^(XMLTerm xmlNameList xmlAttrList ^(TomTermList ))
  | XML_START xmlNameList xmlAttrList XML_CLOSE xmlChilds XML_START_ENDING xmlNameList XML_CLOSE -> ^(XMLTerm xmlNameList xmlAttrList xmlChilds)
  | XML_TEXT LPAREN (id=Identifier | id=STRING ) RPAREN -> ^(XMLTerm ^(TomNameList ^(Name $id)) ^(EmptyList ) ^(EmptyList ))
/*  | XML_COMMENT LPAREN ( Identifier | STRING ) RPAREN -> ^(XMLTerm )
  | XML_PROC LPAREN ( Identifier | STRING ) COMMA ( Identifier | STRING ) RPAREN -> ^(XMLTerm )
*/
  ;

xmlAttrList : 
  LBRACKET (xmlAttr (COMMA xmlAttr)*)? RBRACKET -> ^(TomTermList xmlAttr+)
  | xmlAttr* -> ^(TomTermList xmlAttr*)
  ;

xmlAttr : 
  UNDERSCORE STAR -> ^(UnamedVariableStar ) //^(UnderscoreStar )
  | Identifier STAR -> ^(VariableStar ^(Name Identifier) ^(EmptyType ))
  | id=Identifier EQUAL id1=Identifier AT id2=Identifier/* | STRING )*/ -> ^(XMLAttr ^(Name $id ) Identifier[$id2] ^(Annotation $id1) ) // ?
  | id=Identifier EQUAL id2=Identifier/* | STRING )*/ -> ^(XMLAttr ^(Name $id ) Identifier[$id2] ^(EmptyAnnotation )) // ?
  //| (id1=Identifier AT)? UNDERSCORE EQUAL (id2=Identifier AT)? id3=Identifier/* | STRING )*/ -> ^(UnamedXMLAttr Identifier[$id3] (Identifier[$id1])? (Identifier[$id2])? )// ?
  | id1=Identifier AT UNDERSCORE EQUAL id2=Identifier AT id3=Identifier/* | STRING )*/ -> ^(UnamedXMLAttr Identifier[$id3] ^(Annotation $id2) ^(Annotation $id1))// ?
  | id1=Identifier AT UNDERSCORE EQUAL id3=Identifier/* | STRING )*/ -> ^(UnamedXMLAttr Identifier[$id3] ^(Annotation $id1) ^(EmptyAnnotation ))// ?
  | UNDERSCORE EQUAL id2=Identifier AT id3=Identifier/* | STRING )*/ -> ^(UnamedXMLAttr Identifier[$id3] ^(EmptyAnnotation ) ^(Annotation $id2))// ?
  | UNDERSCORE EQUAL id3=Identifier/* | STRING )*/ -> ^(UnamedXMLAttr Identifier[$id3] ^(EmptyAnnotation ) ^(EmptyAnnotation ))// ?
  ;

xmlChilds : //TermList
  term* -> ^(TomTermList term*)
  | LBRACKET /*term (COMMA term)* */ termList RBRACKET -> termList //^(TomTermList term+)
  ;

xmlNameList : 
  Identifier -> ^(TomNameList ^(Name Identifier ))
  | LPAREN Identifier (ALTERNATIVE Identifier)* RPAREN -> ^(TomNameList ^(Name Identifier )+)
  ;

//something like that ?
/*headSymbol : 
  Identifier -> ^(HeadSymbol Identifier )
  | INT -> ^(Integer )
  | DOUBLE -> ^(Double )
  | STRING -> ^(String )
  | CHAR -> ^(Char )
  ;*/

headSymbolList :
  Identifier QMARK -> ^(HeadSymbolList ^(HeadSymbolQMark Identifier ))
  | Identifier -> ^(HeadSymbolList ^(HeadSymbol Identifier ))
  | LPAREN Identifier (ALTERNATIVE Identifier)+ RPAREN -> ^(HeadSymbolList Identifier+)
  ;

tail : 
  explicitTermList -> ^(ExplicitTermList explicitTermList )
  | implicitPairList -> ^(ImplicitPairList implicitPairList )
  ;

explicitTermList : 
  LPAREN (pattern (COMMA pattern)* )? RPAREN -> ^(PatternList (pattern+)? )
  ;

implicitPairList : 
  LBRACKET (pairPattern (COMMA pairPattern)* )? RBRACKET -> ^(PairPatternList (pairPattern+)? )
  ;

pairPattern : 
  Identifier EQUAL pattern -> ^(PairPattern Identifier ^(Pattern pattern ))
  ;

//Include
includeConstruct : 
  INCLUDE LBRACE /*(f=FILENAME | f=FQN | f=)*/Identifier RBRACE -> ^(Include Identifier) 
  ;

//Strategy
strategyConstruct : 
  STRATEGY Identifier LPAREN strategyArguments* RPAREN 'extends' term LBRACE strategyVisitList RBRACE -> ^(Strategy ^(Name Identifier) term strategyVisitList strategyArguments* )
  ;

strategyArguments : 
  n=Identifier COLON t=Identifier ( COMMA n2=Identifier COLON t2=Identifier )* -> ^(TomTermList ^(Variable ^(Name $n) ^(Type $t))+ )
  | t=Identifier n=Identifier ( COMMA t2=Identifier n2=Identifier )* -> ^(TomTermList ^(Variable ^(Name $n) ^(Type $t))+ )
  ;

strategyVisitList : 
  strategyVisit* -> ^(StrategyVisitList strategyVisit*)
  ;

strategyVisit : 
  'visit' t=Identifier LBRACE visitAction* RBRACE -> ^(StrategyVisit ^(Type $t) ^(VisitActionList visitAction*))
  ;

visitAction :// almost the same as patternAction 
  label=Identifier COLON patternList ARROW LBRACE blockList RBRACE -> ^(LabelledVisitActionBL patternList blockList $label)
  | label=Identifier COLON patternList ARROW term -> ^(LabelledVisitActionT patternList term $label)
  | patternList ARROW LBRACE blockList RBRACE -> ^(VisitActionBL patternList blockList)
  | patternList ARROW term -> ^(VisitActionT patternList term)
  ;

//Operator : fix it :  keywordIsFsym is optional since Tom 2.5
operator : 
  OPERATOR t=Identifier n=Identifier LPAREN slotList? RPAREN LBRACE /*keywordIsFsym*/ l=listKeywordsOp? /*ol=((keywordMake | keywordGetSlot)+)?*/ RBRACE
    -> {l!=null}? ^(Operator ^(Name $n) ^(Type $t) slotList? /*keywordIsFsym*/ $l /*^(OperatorList ($l)? )*/)
    -> ^(Operator ^(Name $n) ^(Type $t) slotList? /*keywordIsFsym*/ ^(OperatorList ))
  ;

/*keywords for %op*/ 
keywordsOp : 
  keywordIsFsym
  | keywordMake
  | keywordGetSlot
  ;

listKeywordsOp : 
  keywordsOp+ -> ^(OperatorList keywordsOp+)
  ;

/*keywords for %oparray*/
keywordsOpArray : 
  keywordIsFsym
  | keywordMakeEmptyArray
  | keywordMakeAppend
  | keywordGetElement
  | keywordGetSize
  ;

listKeywordsOpArray : 
  keywordsOpArray+ -> ^(OperatorList keywordsOpArray+)
  ;

/*keywords for %oplist*/
keywordsOpList : 
  keywordIsFsym
  | keywordMakeEmptyList
  | keywordMakeInsert
  | keywordGetHead
  | keywordGetTail
  | keywordIsEmpty
  ;

listKeywordsOpList : 
  keywordsOpList+ -> ^(OperatorList keywordsOpList+)
  ;
//

keywordIsFsym : 
  'is_fsym' LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(IsFsym ^(Name $name) goalLanguageBlock)
  ;

keywordMake : 
  'make' LPAREN /*nameList*/ l=(Identifier ( COMMA Identifier )* )? RPAREN goalLanguageBlock -> ^(Make /*nameList*/ ^(TomNameList $l? ) goalLanguageBlock)
//  | 'make' LPAREN RPAREN -> ^(Make ^(TomNameList ) goalLanguageBlock)
  ;

keywordGetSlot : 
  'get_slot' LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(GetSlot ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

keywordGetHead : 
  'get_head' LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(GetHead ^(Name $name) goalLanguageBlock)
  ;

keywordGetTail : 
  'get_tail' LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(GetTail ^(Name $name) goalLanguageBlock)
  ;

keywordIsEmpty : 
  'is_empty' LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(IsEmpty ^(Name $name) goalLanguageBlock)
  ;

keywordMakeEmptyList : 
  'make_empty' LPAREN RPAREN goalLanguageBlock -> ^(MakeEmptyList goalLanguageBlock)
  ;

keywordMakeInsert : 
  'make_insert' LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(MakeInsert ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

keywordGetElement : 
  'get_element' LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(GetElement ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

keywordGetSize : 
  'get_size' LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(GetSize ^(Name $name) goalLanguageBlock)
  ;

keywordMakeEmptyArray : 
  'make_empty' LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(MakeEmptyArray ^(Name $name) goalLanguageBlock)
  ;

keywordMakeAppend : 
  'make_append' LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(MakeAppend ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

// 
//  : Identifier -> ^(TomNameList ^(Name Identifier ))
//  | LPAREN Identifier (ALTERNATIVE Identifier)* RPAREN -> ^(TomNameList ^(Name Identifier )+)
//nameList
//  : Identifier ( COMMA Identifier )* -> ^(TomNameList ^(Name Identifier)+)
//  ;

slot : 
  n=Identifier COLON t=Identifier -> ^(Slot ^(Name $n) ^(Type $t))
  ;

slotList : 
  slot ( COMMA slot )* -> ^(SlotList slot+)
  ;
//

operatorList : 
  OPLIST t=Identifier n=Identifier LPAREN t2=Identifier STAR RPAREN LBRACE /*keywordIsFsym*/ l=listKeywordsOpList? RBRACE
    -> {l!=null}? ^(OpList ^(Name $n) ^(Type $t) ^(Type $t2) /*keywordIsFsym*/ $l)
    -> ^(OpArray ^(Name $n) ^(Type $t) ^(Type $t2) /*keywordIsFsym*/ ^(OperatorList ))
  ;

operatorArray : 
  OPARRAY t=Identifier n=Identifier LPAREN t2=Identifier STAR RPAREN LBRACE /*keywordIsFsym*/ l=listKeywordsOpArray? RBRACE
    -> {l!=null}? ^(OpArray ^(Name $n) ^(Type $t) ^(Type $t2) /*keywordIsFsym*/ $l)
    -> ^(OpArray ^(Name $n) ^(Type $t) ^(Type $t2) /*keywordIsFsym*/ ^(OperatorList ))
  ;

typeTerm : 
  TYPETERM Identifier LBRACE keywordImplement keywordIsSort? keywordEquals? RBRACE ->  ^(TypeTerm  ^(Type Identifier ) keywordImplement keywordIsSort? keywordEquals? )
  ;

keywordImplement : 
  'implement' goalLanguageBlock -> ^(Implement goalLanguageBlock )
  ;

keywordIsSort : 
  'is_sort' LPAREN Identifier RPAREN /*goalLanguageSortCheck*/ goalLanguageBlock -> ^(IsSort ^(Name Identifier) goalLanguageBlock ) //temp
  ;

keywordEquals : 
  'equals' LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(Equals ^(Name $n1 ) ^(Name $n2 ) goalLanguageBlock )
  ;


//############################################################################
//############################################################################

//Backquote starting point

backQuoteTerm : '`(' compositeTerm ')' -> ^(BackQuoteTerm compositeTerm) ;

compositeTerm :
  id=Identifier '*' -> ^(BQVariableStar $id) // x*
//  | id=Identifier ('()')? -> ^(Variable $id )  // x and x()
//  | name=Identifier '(' termList ')' -> ^(NamedVariable $name termList) //  //f(...)
  | '_' -> ^(BQUnamedVariable )
  | '_*' -> ^(BQUnamedVariableStar )
  | name=Identifier '(' cl=compositeList ')' -> ^(Composite $name $cl) // f(...)
  | expression -> ^(Expression expression)
  ;

compositeList : 
  compositeTerm (',' compositeTerm)* -> ^(CompositeList compositeTerm+)
  ;


/*
compositeTerm :
  term -> ^(TomVariable term)
  //| expression* -> ^(Expression ^(ExpressionList expression*))
  | expression -> ^(Expression expression)
//  | xmlTerm
  ;

compositeList : 
  //c1=(term|expression) (',' c2+=(term|expression))* -> ^(CompositeList $c1 $c2)
  compositeTerm (',' compositeTerm)* -> ^(CompositeList compositeTerm+)
  ;

term : // x, x*, x(), x(...)
   id=Identifier '*' -> ^(VariableStar $id)
  | id=Identifier -> ^(Variable $id )
  | id=Identifier '('')' -> ^(Variable $id)
//  | name=Identifier '(' termList ')' -> ^(NamedVariable $name termList)
  | name=Identifier '(' cl=compositeList ')' -> ^(Composite $name $cl)
//  | termList
  ;
*/

/*termList :
  term (',' term)* -> ^(TermList term+)
  ;*/

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
  '('! expression ')'!
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
  '(' primitiveType ')' unaryExpression
      ->  ^(TypeCast primitiveType unaryExpression)
  | '(' (t=type | expression) ')' unaryExpressionNotPlusMinus
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
/*    |   '.'! 'new'! innerCreator */
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
  '(' l=expressionList? ')'
      -> {l==null}? ^(ExpressionList )
      -> $l
  ;

/////////////////////////////////////////
// Lexer
/////////////////////////////////////////

//Minitom lexer

// LEXER
//keywords
MATCH       :   '%match'    ;
INCLUDE     :   '%include'  ;
STRATEGY    :   '%strategy' ;
OPERATOR    :   '%op'       ;
OPLIST      :   '%oplist'   ;
OPARRAY     :   '%oparray'  ;
TYPETERM    :   '%typeterm' ;
GOM         :   '%gom'      ;

BACKQUOTE   :   '`'           ;

// simple symbols
LBRACE      :   '{' ;
RBRACE      :   '}' ;
LPAREN      :   '(' ;
RPAREN      :   ')' ;
LBRACKET    :   '[' ;
RBRACKET    :   ']' ;
COMMA       :   ',' ;
ARROW       :   '->';
DOULEARROW  :   '=>';
ALTERNATIVE :   '|' ;
AFFECT      :   ':=';
DOUBLEEQ    :   '==';
COLON       :   ':' ;
SEMI        :   ';' ;
EQUAL       :   '=' ;
AT          :   '@' ;
STAR        :   '*' ;
ANTI_SYM    :    '!';
QMARK       :   '?' ;
UNDERSCORE  :   '_' ;
MINUS       :   '-' ;
PLUS        :   '+' ;
DOT         :   '.' ;
SLASH       :   '/' ;

//XML Tokens
XML_START   :   '<';
XML_CLOSE   :   '>' ;
DOUBLE_QUOTE:   '\"';
XML_TEXT    :   '#TEXT';
XML_COMMENT :   '#COMMENT';
XML_PROC    :   '#PROCESSING-INSTRUCTION';
XML_START_ENDING    : '</' ;
XML_CLOSE_SINGLETON : '/>' ;

// tokens to skip : white spaces
WS : ( ' '
       | '\t'
       | ( '\r\n' // DOS
           | '\n'   // Unix
           | '\r'   // Macintosh
           )
       )
  {$channel=HIDDEN;}
  ;

// tokens to skip : Single Line Comments
SL_COMMENT:
  '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
  {$channel=HIDDEN;}
  ;

// tokens to skip : Multi Lines Comments
ML_COMMENT:
  '/*' ( options {greedy=false;} : . )* '*/'
  {$channel=HIDDEN;}
  ;

STRING:
  '"' (~('"'|'\\'|'\n'|'\r'))* '"'
  /* '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' */
  ;


Identifier:
  Letter (Letter|JavaIDDigit)*
  ;

MATCH_CONSTRAINT  : '<<';
LESSOREQUAL_CONSTRAINT  : '<=';  
GREATEROREQUAL_CONSTRAINT  : '>=';  
DIFFERENT_CONSTRAINT  : '!=';
AND_CONNECTOR  : '&&';
OR_CONNECTOR  : '||';

/*fragment
LETTER:   ('a'..'z' | 'A'..'Z' )   ;

fragment
DIGIT:   ('0'..'9')  ;*/

//Identifier: ( LETTER | UNDERSCORE ) ( LETTER | UNDERSCORE | DIGIT )* ;

/*ID : ('a'..'z' | 'A'..'Z')
     ('a'..'z' | 'A'..'Z' | '0'..'9' | '_' | '-')* ;*/


//FQN: (Identifier DOT)* Identifier;
//Identifier ('.' Identifier)*

// filename is : ./fileName | ./../../fileName | ../../fileName | fileName | path/to/fileName | ./path/to/fileName | ../../path/to/fileName
//fragment
/*FILENAME : // to modify
 (DOT SLASH | (DOT DOT SLASH)+ | SLASH)? 
 ((DIGIT | LETTER | UNDERSCORE)+ SLASH)*
 (LETTER | UNDERSCORE) (LETTER | UNDERSCORE | DIGIT | DOT)*
 ;*/

//MiniJava lexer

/////////////////////////////////////////
//partial Java lexer

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

/*Identifier:
  Letter (Letter|JavaIDDigit)*
  ;*/



