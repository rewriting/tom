grammar miniTom;

options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
  tokenVocab=miniTomTokens;
}

@parser::header {
package newparser;
import org.antlr.runtime.tree.Tree;
}

@lexer::header {
package newparser;

import debug.*;

import streamanalysis.*;
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


// OperatorConstruct ========================================================
csOperatorConstruct 
returns [int marker] :

  //%op already consumed when this rule is called
  tomTypeName=IDENTIFIER ctorName=IDENTIFIER LPAR csSlotList RPAR
  LBR csKeywordList_OperatorConstruct RBR
;

csSlotList :
  csSlot (COMMA csSlot)*
;

csSlot : 
  slotName=IDENTIFIER COLON slotType=IDENTIFIER 
;

csKeywordList_OperatorConstruct :
  ks+=csKeywordIsFsym (ks+=csKeywordMake | ks+= csKeywordGetSlot)*
;

csKeywordList_OperatorArrayConstruct :
   ks+=csKeywordIsFsym
 ( ks+=csKeywordMakeEmpty_Array
  |ks+=csKeywordMakeAppend
  |ks+=csKeywordGetElement
  |ks+=csKeywordGetSize
 )*
;

csKeywordList_OperatorListConstruct :
   ks+=csKeywordIsFsym
 ( ks+=csKeywordMakeEmpty_List
  |ks+=csKeywordMakeInsert
  |ks+=csKeywprdGetHead
  |ks+=csKeywordGetTail
  |ks+=csKeywordIsEmpty
 )*
;

csKeywordIsFsym :
  KEYWORD_IS_FSYM LPAR csName RPAR
  LBR /* Host Code doing the test */ RBR
;

csKeywordGetSlot :
 KEYWORD_GET_SLOT LPAR slotName=csName COMMA argName=csName RPAR
 LBR /* Host Code accessing slot content */ RBR
;

csKeywordMake :
 KEYWORD_MAKE LPAR csNameList RPAR 
 RBR /* Host Code making new object */ LBR
;

csKeywprdGetHead :
 KEYWORD_GET_HEAD LPAR argName=csName RPAR 
 LBR /* Host Code accessing list head */ RBR
;

csKeywordGetTail :
 KEYWORD_GET_TAIL LPAR argName=csName RPAR
 LBR /* Host Code accessing list tail */ RBR
;

csKeywordIsEmpty :
 KEYWORD_IS_EMPTY LPAR argName=csName RPAR
 LBR /* Host Code emptiness expression */ RBR
;

csKeywordMakeEmpty_List :
 KEYWORD_MAKE_EMPTY LPAR RPAR
 LBR /* Host Code creating empty list */ RBR
;

csKeywordMakeEmpty_Array :
 KEYWORD_MAKE_EMPTY LPAR argName=csName RPAR
 LBR /* Host Code creating empty array */ RBR
;

csKeywordMakeInsert :
 KEYWORD_MAKE_INSERT
   LPAR elementArgName=csName COMMA listArgName=csName RPAR
 LBR /* Host Code making insertion */ RBR
;

csKeywordGetElement :
 KEYWORD_GET_ELEMENT
  LPAR listArgName=csName COMMA elementIndexArgName=csName RPAR
 LBR /* Host Code accessing this element */ RBR
;

csKeywordGetSize :
 KEYWORD_GET_SIZE LPAR listArgName=csName RPAR
 LBR /* Host Code accessing size */ RBR
;

csKeywordMakeAppend :
 KEYWORD_MAKE_APPEND 
   LPAR elementArgName=csName COMMA listArgName=csName RPAR
 LBR /* Host Code appending element */ RBR
;

csNameList :
 IDENTIFIER (COMMA IDENTIFIER)*
;

csName :
 IDENTIFIER
;

// Lexer Rules =============================================================

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

ARROW	: '->' { opensBlockLBR = true;} ;

LBR     :  '{'
{
  if(opensBlockLBR){
     opensBlockLBR = false;

    HostParser parser = new HostParser(
    new NegativeImbricationDetector('{', '}', 0));

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
//STRING	        : DQUOTE (LETTER | DIGIT)* DQUOTE ;
STRING		: DQUOTE (~(DQUOTE)|'\\"')* DQUOTE; //"
//->consider escape sequence (and chars that are not part of LETTER OR DIGIT)
CHAR		: SQUOTE (LETTER|DIGIT) SQUOTE ;
// should consider escape sequenes
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
