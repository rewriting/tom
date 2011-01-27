/*
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2007-2011, INPL, INRIA
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

grammar NewTomLanguage;

options {
  backtrack=true;
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=TomTokens;
}

@header {
  package newtom;
  //package tom.engine.parser;
  import org.antlr.runtime.tree.Tree;
}

@lexer::header {
  package newtom;
  //package tom.engine.parser;
  import org.antlr.runtime.tree.Tree;
  import org.antlr.runtime.ParserRuleReturnScope;
}

@lexer::members{
  public static int nesting = 0;
  public boolean isBlockLbrace = false;
  public Tree result;
  
  // override standard token emission
  public Token emit() {
    TomToken t = new TomToken(input, state.type, state.channel,
        state.tokenStartCharIndex, getCharIndex()-1,result);
    t.setLine(state.tokenStartLine);
    t.setText(state.text);
    t.setCharPositionInLine(state.tokenStartCharPositionInLine);
    t.setTree(result);
    emit(t);
    result = null;
    return t;
  }

}

/**
 * NewTom : the grammar has been written by following the 'official grammar' on the wiki
 */

/* '{' BlockList '}' */
goalLanguageBlock :
  g=LBRACE /*g=GOALLBRACE*/ -> ^({((TomToken)$g).getTree()})
  ;

matchConstruct :
  /*MATCH*/ LPAREN matchArguments RPAREN LBRACE patternActionList RBRACE -> ^(MatchConstruct matchArguments patternActionList)
  | /*MATCH*/ LBRACE constraintActionList RBRACE -> ^(MatchConstructWithoutArgs constraintActionList)
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

patternAction :/*@init{ int value=1; }*/ 
  patternList a=ARROWLBRACE 
  //patternList ARROW a=LBRACE[1] 
    -> ^(PatternAction patternList ^({((TomToken)$a).getTree()}))
  | labelName=Identifier COLON patternList a=ARROWLBRACE 
  //| labelName=Identifier COLON patternList ARROW a=LBRACE[1] 
    -> ^(LabelledPatternAction patternList ^({((TomToken)$a).getTree()}) $labelName)
  ;

////compositeTerm :
////  Identifier -> ^(CompositeTerm Identifier ) //temporary rule
////  ;
//  | id=Identifier -> ^(CompositeTerm $id ) //temporary rule
//  : unit=(javaCompilationUnit|matchConstruct)* -> ^(CompositeUnit $unit*)
/*  : cu=(matchConstruct* javaCompilationUnit (matchConstruct|javaCompilationUnit)*) -> ^(CompositeTerm ^(CompilationUnit $cu )) */
//  cu=compilationUnitList /*(ct=compilationUnit)**/ -> ^(CompositeTerm
//  compilationUnitList /*^(BlockList ($ct)* )*/) /*(compilationUnit)* ))*/
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
/*
  Identifier STAR -> ^(PlainPattern ^(VariableStar ^(Name Identifier )  ^(EmptyType )) )
  | Identifier -> ^(PlainPattern ^(Variable ^(Name Identifier )  ^(EmptyType )) )
  | UNDERSCORE STAR -> ^(PlainPattern ^(UnamedVariableStar ) )
  | UNDERSCORE -> ^(PlainPattern ^(UnamedVariable ) )
  | ANTI_SYM headSymbolList tail -> ^(AntiSymbolList headSymbolList tail)
//  | ANTI_SYM Identifier STAR -> ^(PlainPattern ^(AntiVariableStar ^(Name Identifier)  ^(EmptyType )) )
  | ANTI_SYM Identifier -> ^(PlainPattern ^(AntiVariable ^(Name Identifier)  ^(EmptyType )) )
*/
  ANTI_SYM (
      Identifier -> ^(PlainPattern ^(AntiVariable ^(Name Identifier)  ^(EmptyType )) )
      | headSymbolList tail -> ^(AntiSymbolList headSymbolList tail)
      )
  | Identifier s=STAR?
    -> {s!=null}? ^(PlainPattern ^(VariableStar ^(Name Identifier )  ^(EmptyType )) )
    ->            ^(PlainPattern ^(Variable ^(Name Identifier )  ^(EmptyType )) )
  | UNDERSCORE s=STAR?
    -> {s!=null}? ^(PlainPattern ^(UnamedVariableStar ) )
    ->            ^(PlainPattern ^(UnamedVariable ) )
  | xmlTerm
  ;

constraintAction : 
  constraint a=ARROWLBRACE -> ^(ConstraintAction constraint ^({((TomToken)$a).getTree()}))
  //constraint ARROW a=LBRACE[1] -> ^(ConstraintAction constraint ^({((TomToken)$a).getTree()}))
//constraint ARROW LBRACE blockList RBRACE -> ^(ConstraintAction constraint blockList)
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

//Strategy
strategyConstruct : 
  /*STRATEGY*/ Identifier LPAREN strategyArguments* RPAREN 'extends' term LBRACE strategyVisitList RBRACE -> ^(Strategy ^(Name Identifier) term strategyVisitList strategyArguments* )
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
  //label=Identifier COLON patternList ARROW LBRACE blockList RBRACE -> ^(LabelledVisitActionBL patternList blockList $label)
  //label=Identifier COLON patternList ARROW a=LBRACE[0] -> ^(LabelledVisitActionBL patternList ^({((TomToken)$a).getTree()}) $label)
  label=Identifier COLON patternList ARROWLBRACE -> ^(LabelledVisitActionBL patternList ^({((TomToken)$a).getTree()}) $label)
  | label=Identifier COLON patternList ARROW term -> ^(LabelledVisitActionT patternList term $label)
  //| patternList ARROW LBRACE blockList RBRACE -> ^(VisitActionBL patternList blockList)
  ///| patternList ARROW a=LBRACE[0] -> ^(VisitActionBL patternList ^({((TomToken)$a).getTree()}))
  | patternList a=ARROWLBRACE -> ^(VisitActionBL patternList ^({((TomToken)$a).getTree()}))
  | patternList ARROW term -> ^(VisitActionT patternList term)
  ;

//Operator : fix it :  keywordIsFsym is optional since Tom 2.5
operator : 
  /*OPERATOR*/ t=Identifier n=Identifier LPAREN s=slotList? RPAREN LBRACE l=listKeywordsOp? RBRACE
    -> {s!=null && l!=null}? ^(Operator ^(Name $n) ^(Type $t) $s $l )
    -> {s!=null && l==null}? ^(Operator ^(Name $n) ^(Type $t) $s ^(OperatorList ))
    -> {s==null && l!=null}? ^(Operator ^(Name $n) ^(Type $t) ^(SlotList ) $l)
    ->                       ^(Operator ^(Name $n) ^(Type $t) ^(SlotList ) ^(OperatorList ))
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
  /*'is_fsym'*/IS_FSYM LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(IsFsym ^(Name $name) goalLanguageBlock)
  //'is_fsym' LPAREN name=Identifier RPAREN t=GOALLBRACE -> ^(IsFsym ^(Name $name) ^({((TomToken)$t).getTree()}))
  ;

keywordMake : 
  //'make' LPAREN /*nameList*/ l=(Identifier ( COMMA Identifier )* )? RPAREN goalLanguageBlock -> ^(Make /*nameList*/ ^(TomNameList $l? ) goalLanguageBlock)
  //'make' LPAREN /*nameList*/ l=(identifierToName ( COMMA identifierToName )* )? RPAREN goalLanguageBlock -> ^(Make /*nameList*/ ^(TomNameList $l? ) goalLanguageBlock)
  MAKE /*'make'*/ LPAREN nameList RPAREN goalLanguageBlock -> ^(Make nameList goalLanguageBlock)
//  | 'make' LPAREN RPAREN -> ^(Make ^(TomNameList ) goalLanguageBlock)
  ;

nameList :
  (id+=identifierToName (COMMA id+=identifierToName)* )?
  -> {id!=null}? ^(TomNameList $id+)
  ->             ^(TomNameList )
  ;

identifierToName :
  n=Identifier -> ^(Name $n)
  ;

keywordGetSlot : 
  GET_SLOT /*'get_slot'*/ LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(GetSlot ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

keywordGetHead : 
  GET_HEAD /*'get_head'*/ LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(GetHead ^(Name $name) goalLanguageBlock)
  ;

keywordGetTail : 
  GET_TAIL/*'get_tail'*/ LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(GetTail ^(Name $name) goalLanguageBlock)
  ;

keywordIsEmpty : 
  IS_EMPTY /*'is_empty'*/ LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(IsEmpty ^(Name $name) goalLanguageBlock)
  ;

keywordMakeEmptyList : 
  MAKE_EMPTY /*'make_empty'*/ LPAREN RPAREN goalLanguageBlock -> ^(MakeEmptyList goalLanguageBlock)
  ;

keywordMakeInsert : 
  MAKE_INSERT /*'make_insert'*/ LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(MakeInsert ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

keywordGetElement : 
  GET_ELEMENT /*'get_element'*/ LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(GetElement ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

keywordGetSize : 
  GET_SIZE /*'get_size'*/ LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(GetSize ^(Name $name) goalLanguageBlock)
  ;

keywordMakeEmptyArray : 
  MAKE_EMPTY /*'make_empty'*/ LPAREN name=Identifier RPAREN goalLanguageBlock -> ^(MakeEmptyArray ^(Name $name) goalLanguageBlock)
  ;

keywordMakeAppend : 
  MAKE_APPEND /*'make_append'*/ LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(MakeAppend ^(Name $n1) ^(Name $n2) goalLanguageBlock)
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

operatorList : 
  /*OPLIST*/ t=Identifier n=Identifier LPAREN t2=Identifier STAR RPAREN LBRACE l=listKeywordsOpList? RBRACE
    -> {l!=null}? ^(OpList ^(Name $n) ^(Type $t) ^(Type $t2) $l)
    ->            ^(OpList ^(Name $n) ^(Type $t) ^(Type $t2) ^(OperatorList ))
  ;

operatorArray : 
  /*OPARRAY*/ t=Identifier n=Identifier LPAREN t2=Identifier STAR RPAREN LBRACE l=listKeywordsOpArray? RBRACE
    -> {l!=null}? ^(OpArray ^(Name $n) ^(Type $t) ^(Type $t2) $l)
    ->            ^(OpArray ^(Name $n) ^(Type $t) ^(Type $t2) ^(OperatorList ))
  ;

listKeywordsTypeTerm : 
  k1=keywordImplement (k2=keywordIsSort)? (k3=keywordEquals)? ->  ^(OperatorList $k1 ($k2)? ($k3)? )
;

typeTerm : 
  /*TYPETERM*/ //Identifier LBRACE keywordImplement keywordIsSort? keywordEquals? RBRACE ->  ^(TypeTerm  ^(Type Identifier ) keywordImplement keywordIsSort? keywordEquals? )
  /*TYPETERM*/ Identifier LBRACE listKeywordsTypeTerm  RBRACE ->  ^(TypeTerm  ^(Type Identifier ) listKeywordsTypeTerm)
  ;

keywordImplement : 
  IMPLEMENT /*'implement'*/ goalLanguageBlock -> ^(Implement goalLanguageBlock )
  ;

keywordIsSort : 
  IS_SORT /*'is_sort'*/ LPAREN Identifier RPAREN /*goalLanguageSortCheck*/ goalLanguageBlock -> ^(IsSort ^(Name Identifier) goalLanguageBlock ) //temp
  ;

keywordEquals : 
  EQUALS /*'equals'*/ LPAREN n1=Identifier COMMA n2=Identifier RPAREN goalLanguageBlock -> ^(Equals ^(Name $n1 ) ^(Name $n2 ) goalLanguageBlock )
  ;


/////////////////////////////////////////

//Minitom lexer

// LEXER
//keywords <- now useless since they are in Host
//MATCH       :   '%match'    ;
//INCLUDE     :   '%include'  ;
//STRATEGY    :   '%strategy' ;
//OPERATOR    :   '%op'       ;
//OPLIST      :   '%oplist'   ;
//OPARRAY     :   '%oparray'  ;
//TYPETERM    :   '%typeterm' ;
//GOM         :   '%gom'      ;

ARROWLBRACE : { isBlockLbrace = true; } ARROW WS* LBRACE;

IS_FSYM     : 'is_fsym'     { isBlockLbrace = true; } ;
MAKE        : 'make'        { isBlockLbrace = true; } ;
GET_SLOT    : 'get_slot'    { isBlockLbrace = true; } ;
EQUALS      : 'equals'      { isBlockLbrace = true; } ;
IS_SORT     : 'is_sort'     { isBlockLbrace = true; } ;
IMPLEMENT   : 'implement'   { isBlockLbrace = true; } ;
MAKE_EMPTY  : 'make_empty'  { isBlockLbrace = true; } ;
MAKE_INSERT : 'make_insert' { isBlockLbrace = true; } ;
MAKE_APPEND : 'make_append' { isBlockLbrace = true; } ;
GET_SIZE    : 'get_size'    { isBlockLbrace = true; } ;
GET_ELEMENT : 'get_element' { isBlockLbrace = true; } ;
GET_HEAD    : 'get_head'    { isBlockLbrace = true; } ;
GET_TAIL    : 'get_tail'    { isBlockLbrace = true; } ;
IS_EMPTY    : 'is_empty'    { isBlockLbrace = true; } ;

//fragment
LBRACE : '{'
  {
    if (isBlockLbrace) {
      isBlockLbrace = false; // reset isBlockLbrace variable
      nesting--;
      //System.out.println("tom nesting, isBlockLbrace = " + nesting); 
      //System.out.println("\nbefore new Host*");
      //System.out.println("in goallbrace / tom nesting = " + nesting);
      NewHostLanguageLexer lexer = new NewHostLanguageLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      //System.out.println("tom, tokens = " + tokens.toString() + " /fin");
      //System.out.println("tom, tokens list = " + tokens.getTokens().toString());
      NewHostLanguageParser parser = new NewHostLanguageParser(tokens);
      //System.out.println("before parser.goalLanguageBlock()");
      NewHostLanguageParser.blockList_return res = parser.blockList();
      //System.out.println("tom, res.getTree() =\n" + ((Tree)res.getTree()).toStringTree());
      result = (Tree)res.getTree();
      //System.out.println("tom, end, result =\n" + result.toStringTree());
    } else {
      nesting++;
    }
  }
  ;

RBRACE : '}'
  {
    isBlockLbrace = false;
    if ( nesting<=0 ) {
      emit(Token.EOF_TOKEN);
      //System.out.println("exit tom language and emit(Token.EOF)\n");
    }
    else {
      //System.out.println("tom nesting before -- = " + nesting);
      nesting--;
    }
  }
  ;
//LBRACE      :   '{' ;
//RBRACE      :   '}' ;


//
LPAREN      :   '(' ;
RPAREN      :   ')' ;
LBRACKET    :   '[' ;
RBRACKET    :   ']' ;
COMMA       :   ',' ;
//fragment
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
SLCOMMENT:
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

fragment
CID: ( Identifier | WS | STRING | DOT | LPAREN .* RPAREN /*| LBRACE .* RBRACE*/ )+ ;

//fragment
//XMLID: (Identifier | STRING);

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


/*CharacterLiteral
    :   '\'' ( EscapeSequence | ~('\''|'\\') ) '\''
    ;

StringLiteral
    :  '"' ( EscapeSequence | ~('\\'|'"') )* '"'
    ;*/


/*WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
    ;*/

