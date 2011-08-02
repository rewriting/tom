grammar miniTom;

options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
  tokenVocab=CSTTokens;
}

@parser::header {
package tom.engine.newparser.parser;
import org.antlr.runtime.tree.Tree;
}

@lexer::header {
package tom.engine.newparser.parser;

import tom.engine.newparser.debug.*;

import tom.engine.newparser.streamanalysis.*;
import org.antlr.runtime.tree.Tree;
}

@lexer::members{
  // === DEBUG ============//
  public String getClassDesc(){
    return "ANTLRParser";
     // actually this is Lexer but we are interested in
     // the way HostParser and ANTLR generated Parser
     // call each other. We don't need to make a
     // difference between ANTLR's Parser and Lexer
  }

  private boolean opensBlockLBR = false;
  
  private final TokenCustomizer tokenCustomizer = new TokenCustomizer();
 
  // add custom fields to ANTLR generated Tokens
  @Override 
  public void emit(Token t){
    super.emit(tokenCustomizer.customize(t));
  }
  
}

// IncludeConstruct
csIncludeConstruct
returns [int marker] :

  /* '%include' */ LBR filename=IDENTIFIER RBR

  // extract and return CharStream's marker
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> ^(CsIncludeConstruct $filename)
;

// MatchConstruct ===========================================================
/*
When parsing parser rule ANTLR's Parse tends to consume "to much" chars.
To respect ParserAction.doAction contract, doAction implementation needs 
to rewind stream to the marker returned by matchconstruct.
*/
matchConstruct
returns [int marker]:
  
  // "%match" already consumed when thid rule is called

  // with args
  LPAR csMatchArgument ((COMMA csMatchArgument)*)? (COMMA)? RPAR
  LBR
    csExtendedConstraintAction*
  RBR

  // extract and return CharStream's marker
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^( CsMatchConstruct
       ^( CsMatchArgumentList csMatchArgument* )
       ^( CsConstraintActionList csExtendedConstraintAction* )
      )

 |
  // witout args
  (LPAR (COMMA)? RPAR)?
  LBR
    csConstraintAction*
  RBR

  // extract and return CharStream's marker
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^( CsMatchConstruct
       ^( CsMatchArgumentList )
       ^( CsConstraintActionList csConstraintAction* )
      )
;

csConstraintAction :
 csConstraint ARROW LBR RBR
 -> ^(CsConstraintAction csConstraint
      {((CustomToken)$LBR).getPayload(Tree.class)}
     )
;

csExtendedConstraintAction :
 csExtendedConstraint ARROW LBR RBR
 -> ^(CsConstraintAction csExtendedConstraint
      {((CustomToken)$LBR).getPayload(Tree.class)}
     )
;

csMatchArgument :
  (type=IDENTIFIER)? csTerm

  ->{type!=null}? ^(CsTypedTerm csTerm ^(CsTermType $type))
  ->              ^(CsTypedTerm csTerm ^(CsTermType ^(CsTermTypeUnknown)))
;
 // Constraints ===============================================
/**
- pattern list with or without additionnal constraints :
  pattern (',' pattern )* (('&&'|'||') csontraint)?

everything is then stored in CST as a constraint.
every pattern in pattern list is seen as a constraint to
match identicaly positionned matchConstruct's argument.
*/
csExtendedConstraint :
 csMatchArgumentConstraintList ((a=AND|o=OR) csConstraint)?
 ->{a!=null}? ^(CsAndConstraint csMatchArgumentConstraintList csConstraint)
 ->{o!=null}? ^(CsOrConstraint  csMatchArgumentConstraintList csConstraint)
 ->           csMatchArgumentConstraintList
; 

csMatchArgumentConstraintList :
  csMatchArgumentConstraint (COMMA c2=csMatchArgumentConstraint)* (COMMA)?
  ->{c2!=null}? ^(CsAndConstraint csMatchArgumentConstraint*)
  ->            csMatchArgumentConstraint
;

csMatchArgumentConstraint :
  csPattern
  -> ^(CsMatchArgumentConstraint csPattern)
;

/**
- match constraints :   pattern << term
- logical operations :  constraint ('&&'|'||') constraint
- numeric constraints : term ('>'|'<'|'>='|'<='|'=='|'!=') term 

proirity in operators is :
(from higher to lower)
 '<<'
 '>'|'<'|'>='|'<='|'=='|'!='
 '&&'
 '||'
*/
csConstraint : 
 csConstraint_priority1
;

csConstraint_priority1 :
  csConstraint_priority2 (or=OR csConstraint_priority2)*
  ->{or!=null}? ^(CsOrConstraint csConstraint_priority2*)
  -> csConstraint_priority2
;

csConstraint_priority2 :
  csConstraint_priority3 (and=AND csConstraint_priority3)*
  ->{and!=null}? ^(CsAndConstraint csConstraint_priority3*)
  -> csConstraint_priority3
;

csConstraint_priority3 :
 
  l=csTerm
  (gt=GREATERTHAN|ge=GREATEROREQU|lt=LOWERTHAN
  |le=LOWEROREQU |eq=DOUBLEEQUAL |ne=DIFFERENT)
  r=csTerm

 ->{gt!=null}? ^(CsNumGreaterThan      $l $r)
 ->{ge!=null}? ^(CsNumGreaterOrEqualTo $l $r)
 ->{lt!=null}? ^(CsNumLessThan         $l $r)
 ->{le!=null}? ^(CsNumLessOrEqualTo    $l $r)
 ->{eq!=null}? ^(CsNumEqualTo          $l $r)
 ->/*ne!=null*/^(CsNumDifferent        $l $r)
 

 | csConstraint_priority4
 -> csConstraint_priority4
;

csConstraint_priority4 :
 csPattern LARROW csTerm
 -> ^(CsMatchTermConstraint csPattern csTerm)

 | LPAR csConstraint RPAR
 -> csConstraint
;
// Terms ======================================================
csTerm :
  IDENTIFIER (s=STAR)?
  ->{s!=null}? ^(CsVariableNameStar IDENTIFIER )
  ->           ^(CsVariableName IDENTIFIER)
 
 |IDENTIFIER LPAR (csTerm (COMMA csTerm)*)? RPAR
  -> ^(CsTerm IDENTIFIER ^(CsTermList csTerm*))
;
// Patterns ===================================================
csPattern :
  // myA@a(...
  IDENTIFIER AT csPattern
  -> ^(CsAnnotatedPattern csPattern IDENTIFIER)

  //!a(...
 |ANTI csPattern
  -> ^(CsAnti csPattern)

  //f'('...')'
 |csHeadSymbolList csExplicitTermList
  -> ^(CsSymbolList csHeadSymbolList csExplicitTermList)
 
  //f'['...']'
 |csHeadSymbolList csImplicitPairList
  -> ^(CsSymbolList csHeadSymbolList csImplicitPairList)

  // x
  // x*
 |IDENTIFIER (s=STAR)?
  -> {s!=null}? ^(CsVariableStar IDENTIFIER)
  ->/*s==null*/ ^(CsVariable IDENTIFIER)
  
  // _
  // _*
 |UNDERSCORE (s=STAR)? 
  -> {s!=null}? ^(CsUnamedVariableStar)
  ->/*s==null*/ ^(CsUnamedVariable)
  
  // 'a'
  // 'a'*
 |csConstantValue (s=STAR)?
  -> {s!=null}? ^(CsConstantStar csConstantValue)
  ->/*s==null*/ ^(CsConstant csConstantValue)
;

// f
// (f|g)
// f?  -- should be --> f{theory:AU}
// f?? -- shoud be  --> f{theory:AC}
csHeadSymbolList :
  csHeadSymbol
  -> ^(CsHeadSymbolList csHeadSymbol)
 | LPAR csHeadSymbol (PIPE csHeadSymbol)* RPAR 
  -> ^(CsHeadSymbolList	 csHeadSymbol*)
; 

csHeadSymbol :
  IDENTIFIER
  -> ^(CsHeadSymbol IDENTIFIER ^(CsTheoryDEFAULT))
 |IDENTIFIER QMARK
  -> ^(CsHeadSymbol IDENTIFIER ^(CsTheoryAU))
 |IDENTIFIER DQMARK
  -> ^(CsHeadSymbol IDENTIFIER ^(CsTheoryAC))
 |csConstantValue 
 -> ^(CsConstantHeadSymbol csConstantValue ^(CsTheoryDEFAULT))
 |csConstantValue QMARK
 -> ^(CsConstantHeadSymbol csConstantValue ^(CsTheoryAU))
 |csConstantValue DQMARK
 -> ^(CsConstantHeadSymbol csConstantValue ^(CsTheoryAC))
;

csExplicitTermList :
   LPAR (csPattern (COMMA csPattern)*)? RPAR

 -> ^(CsExplicitPatternList csPattern*)
;

csImplicitPairList :
  LSQUAREBR (csPairPattern (COMMA csPairPattern)*)?  RSQUAREBR

  -> ^(CsImplicitPairList csPairPattern*)
;

csPairPattern :
 IDENTIFIER EQUAL csPattern

 -> ^(CsPairPattern IDENTIFIER csPattern)
;

csConstantValue :
  INTEGER|DOUBLE|STRING|CHAR
;
// OperatorConstruct & TypeTermConstruct ====================================
csOperatorConstruct 
returns [int marker] :

  //%op already consumed when this rule is called
  tomTypeName=csName ctorName=csName LPAR csSlotList RPAR
  LBR 
    ks+=csKeywordIsFsym (ks+=csKeywordMake | ks+= csKeywordGetSlot)*
  RBR

  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> ^(CsOpConstruct
        $tomTypeName $ctorName csSlotList ^(CsOperatorList $ks*)
      ) 
;

csOperatorArrayConstruct
returns [int marker] :

  //%oparray already consumed when this rule is called
  tomTypeName=csName ctorName=csName LPAR typeName=csName STAR RPAR
  LBR  
     ks+=csKeywordIsFsym
   ( ks+=csKeywordMakeEmpty_Array
    |ks+=csKeywordMakeAppend
    |ks+=csKeywordGetElement
    |ks+=csKeywordGetSize
   )*
  RBR

  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> ^(CsOpArrayConstruct
        $tomTypeName $ctorName $typeName ^(CsOperatorList $ks*)
      )
; 

csOperatorListConstruct
returns [int marker] :

  //%oplist already consumed when this rule is called
  tomTypeName=csName ctorName=csName LPAR typeName=csName STAR RPAR
  LBR
     ks+=csKeywordIsFsym
   ( ks+=csKeywordMakeEmpty_List
    |ks+=csKeywordMakeInsert
    |ks+=csKeywordGetHead
    |ks+=csKeywordGetTail
    |ks+=csKeywordIsEmpty
   )*
  RBR

  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> ^(CsOpListConstruct
        $tomTypeName $ctorName $typeName ^(CsOperatorList $ks*)
      )
; 


csTypetermConstruct
returns [int marker] :

  //%typeterm already consumed when this rule is called
  typeName=csName (EXTENDS extend=csName)?
  LBR
    ks+=csKeywordImplement (ks+=csKeywordIsSort)? (ks+=csKeywordEquals)?
  RBR

 {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> {extend==null}?
   ^(CsTypetermConstruct $typeName ^(CsEmptyName) ^(CsOperatorList $ks* ))

  -> /*{$extend==null}*/
   ^(CsTypetermConstruct $typeName $extend ^(CsOperatorList $ks*))
;



csSlotList :
  (csSlot (COMMA csSlot)*)?

  -> ^(CsSlotList csSlot*)
;

csSlot : 
  slotName=csName COLON slotType=csName

  -> ^(CsSlot $slotName $slotType)
;


// csKeyword* ====
csKeywordIsFsym :
  KEYWORD_IS_FSYM LPAR argName=csName RPAR
  LBR /* Host Code doing the test */ RBR

  -> ^(CsIsFsym $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetSlot :
  KEYWORD_GET_SLOT LPAR slotName=csName COMMA termName=csName RPAR
  LBR /* Host Code accessing slot content */ RBR

  -> ^(CsGetSlot $slotName $termName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMake :
  KEYWORD_MAKE LPAR argList=csNameList RPAR
  LBR /* Host Code making new object */ RBR

  -> ^(CsMake $argList
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetHead :
  KEYWORD_GET_HEAD LPAR argName=csName RPAR 
  LBR /* Host Code accessing list head */ RBR

  -> ^(CsGetHead $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetTail :
  KEYWORD_GET_TAIL LPAR argName=csName RPAR
  LBR /* Host Code accessing list tail */ RBR

  -> ^(CsGetTail $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordIsEmpty :
  KEYWORD_IS_EMPTY LPAR argName=csName RPAR
  LBR /* Host Code emptiness expression */ RBR

  -> ^(CsIsEmpty $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeEmpty_List :
  KEYWORD_MAKE_EMPTY LPAR RPAR
  LBR /* Host Code creating empty list */ RBR

  -> ^(CsMakeEmptyList
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeEmpty_Array :
  KEYWORD_MAKE_EMPTY LPAR argName=csName RPAR
  LBR /* Host Code creating empty array */ RBR

  -> ^(CsMakeEmptyArray $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeInsert :
  KEYWORD_MAKE_INSERT
    LPAR elementArgName=csName COMMA termArgName=csName RPAR
  LBR /* Host Code making insertion */ RBR

  -> ^(CsMakeInsert $elementArgName $termArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetElement :
  KEYWORD_GET_ELEMENT
    LPAR termArgName=csName COMMA elementIndexArgName=csName RPAR
  LBR /* Host Code accessing this element */ RBR

  -> ^(CsGetElement $termArgName $elementIndexArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetSize :
  KEYWORD_GET_SIZE LPAR termArgName=csName RPAR
  LBR /* Host Code accessing size */ RBR

  -> ^(CsGetSize $termArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeAppend :
  KEYWORD_MAKE_APPEND 
    LPAR elementArgName=csName COMMA termArgName=csName RPAR
  LBR /* Host Code appending element */ RBR

  -> ^(CsMakeAppend $elementArgName $termArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordImplement :
  KEYWORD_IMPLEMENT LBR /* Host Code implementation name*/ RBR

  -> ^(CsImplement {((CustomToken)$LBR).getPayload(Tree.class)})
;

csKeywordIsSort :
  KEYWORD_IS_SORT LPAR argName=csName RPAR
    LBR /* Host Code boolean expression */ RBR

  -> ^(CsIsSort $argName {((CustomToken)$LBR).getPayload(Tree.class)})
;

csKeywordEquals :
  KEYWORD_EQUALS LPAR arg1=csName COMMA arg2=csName RPAR
    LBR /* Host Code boolean expression */ RBR

  -> ^(CsEquals $arg1 $arg2 
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csNameList :
  (csName (COMMA csName)*)?
  -> ^(CsNameList csName*)
;

csName :
  IDENTIFIER
  -> ^(CsName IDENTIFIER)
;

// Lexer Rules =============================================================

// funky ones
KEYWORD_IS_FSYM     : 'is_fsym'     { opensBlockLBR = true; };
KEYWORD_GET_SLOT    : 'get_slot'    { opensBlockLBR = true; };
KEYWORD_MAKE        : 'make'        { opensBlockLBR = true; };
KEYWORD_GET_HEAD    : 'get_head'    { opensBlockLBR = true; };
KEYWORD_GET_TAIL    : 'get_tail'    { opensBlockLBR = true; };
KEYWORD_IS_EMPTY    : 'is_empty'    { opensBlockLBR = true; };
KEYWORD_MAKE_EMPTY  : 'make_empty'  { opensBlockLBR = true; };
KEYWORD_MAKE_INSERT : 'make_insert' { opensBlockLBR = true; };
KEYWORD_GET_ELEMENT : 'get_element' { opensBlockLBR = true; };
KEYWORD_GET_SIZE    : 'get_size'    { opensBlockLBR = true; };
KEYWORD_MAKE_APPEND : 'make_append' { opensBlockLBR = true; };
KEYWORD_IMPLEMENT   : 'implement'   { opensBlockLBR = true; };
KEYWORD_IS_SORT	    : 'is_sort'     { opensBlockLBR = true; };
KEYWORD_EQUALS	    : 'equals'      { opensBlockLBR = true; };

ARROW	: '->' { opensBlockLBR = true;} ;

LBR     :  '{'
{
  if(opensBlockLBR){
     opensBlockLBR = false;

    HostParser parser = new HostParser(
    	null, null, new NegativeImbricationDetector('{', '}', 0));

      // XXX DEBUG ===
      if(HostParserDebugger.isOn()){
        HostParserDebugger.getInstance()
        .debugNewCall(parser.getClassDesc(), input, "");
      }
      // === DEBUG ===

    Tree tree = parser.parseBlockList(input);

      // XXX DEBUG ===
      if(HostParserDebugger.isOn()){
        HostParserDebugger.getInstance()
        .debugReturnedCall(parser.getClassDesc(), input, "");
      }
      // === DEBUG ===
      
    tokenCustomizer.prepareNextToken(tree);
  }
}
;

RBR : '}'
{
// every token generated by antlr pass throw our
// tokenCustomizer.customize(Token). Next emitted
// token, this RBR token, will be "customized" with
// with input.mark() returned value as payload
tokenCustomizer.prepareNextToken(input.mark());
}
;

// basic ones
EXTENDS : 'extends';

LARROW : '<<';
GREATEROREQU : '>=';
LOWEROREQU : '<=';
GREATERTHAN : '>';
LOWERTHAN : '<';
DOUBLEEQUAL : '==';
DIFFERENT : '!=';

AND : '&&';
OR : '||';
PIPE 	: '|';
QMARK	:'?';
DQMARK :'??';
EQUAL	: '=';
LSQUAREBR : '[';
RSQUAREBR : ']';
RPAR 	: ')';
LPAR	: '(';
COMMA	: ',';
STAR	: '*';
UNDERSCORE:'_';
AT	: '@';
ANTI	: '!';
DQUOTE  : '"'; //"
SQUOTE  : '\'';
BQUTE   : '`';
COLON   : ':';

IDENTIFIER 	: LETTER(LETTER | DIGIT | '_' | '-')*;
INTEGER 	: (DIGIT)+;
DOUBLE	        : (DIGIT)+'.'(DIGIT)* | '.' (DIGIT)+;
STRING		: DQUOTE (~(DQUOTE)|'\\"')* DQUOTE; //"
CHAR		: SQUOTE (LETTER|DIGIT) SQUOTE ;
fragment
LETTER	: 'A'..'Z' | 'a'..'z';
fragment
DIGIT	: '0'..'9';

WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };

SL_COMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
ML_COMMENT : '/*' ( options {greedy=false;} : . )* '*/'{ $channel=HIDDEN; } ;

// lexer need a rule for every input
// even for chars we don't use
DEFAULT : . { $channel=HIDDEN;};
