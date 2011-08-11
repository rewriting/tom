grammar miniTom;

options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
  tokenVocab=CSTTokens;
}

@parser::header {
package tom.engine.newparser.parser;
import static tom.engine.newparser.util.TreeFactory.*;
import static tom.engine.newparser.parser.miniTomLexer.*;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.CommonToken;
}

@lexer::header {
package tom.engine.newparser.parser;

import tom.engine.newparser.debug.*;

import tom.engine.newparser.streamanalysis.*;
import org.antlr.runtime.tree.Tree;
}

@lexer::members {
  // === DEBUG ============//
  public String getClassDesc() {
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
  public void emit(Token t) {
    super.emit(tokenCustomizer.customize(t));
  }
  
}

@parser::members {

  public static CommonTree extractOptions(CommonToken t) {
    String newline = System.getProperty("line.separator");
    String lines[] = t.getText().split(newline);
    
    int firstCharLine = t.getLine();
    int firstCharColumn = t.getCharPositionInLine();
    int lastCharLine = firstCharColumn+lines.length-1;
    int lastCharColumn;
    if(lines.length==1) {
      lastCharColumn = firstCharColumn + lines[0].length();
    } else {
      lastCharColumn = lines[lines.length-1].length();
    }
  
    return makeOptions(t.getInputStream().getSourceName(),
      firstCharLine, firstCharColumn, lastCharLine, lastCharColumn);  
  }

  public static CommonTree extractOptions(CommonToken start, CommonToken end) {
    String newline = System.getProperty("line.separator");
    String lines[] = end.getText().split(newline);

    int lastCharLine = end.getLine()+lines.length;
    int lastCharColumn;
    if(lines.length==1) {
      lastCharColumn = end.getCharPositionInLine() + lines[0].length();
    } else {
      lastCharColumn = lines[lines.length-1].length();
    }

    return makeOptions(start.getInputStream().getSourceName(),
      start.getLine(), start.getCharPositionInLine(), lastCharLine,
      lastCharColumn);
  }
}

// IncludeConstruct
csIncludeConstruct
returns [int marker] :

  /* '%include' */ LBR filename=IDENTIFIER RBR

  // extract and return CharStream's marker
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> ^(cst_IncludeConstruct $filename)
;

// MatchConstruct ===========================================================
/*
When parsing parser rule ANTLR's Parse tends to consume "to much" chars.
To respect ParserAction.doAction contract, doAction implementation needs 
to rewind stream to the marker returned by matchconstruct.
*/
matchConstruct
returns [int marker]:
  
  // "%match" already consumed when this rule is called

  // with args
  LPAR csMatchArgument ((COMMA csMatchArgument)*)? (COMMA)? RPAR
  LBR
    csExtendedConstraintAction*
  RBR

  // extract and return CharStream's marker
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^( cst_MatchConstruct
       {extractOptions((CommonToken)$LPAR, (CommonToken)$RBR)}
       ^( cst_concCstTypedTerm csMatchArgument* )
       ^( cst_concConstraintAction csExtendedConstraintAction* )
      )

 |
  // witout args
  (LPAR (COMMA)? RPAR)?
  LBR
    csConstraintAction*
  RBR

  // extract and return CharStream's marker
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^( cst_MatchConstruct
      {extractOptions((CommonToken)$LBR, (CommonToken)$RBR)}
       ^( cst_concCstTypedTerm )
       ^( cst_concConstraintAction csConstraintAction* )
      )
;

csConstraintAction :
 csConstraint ARROW LBR RBR
 -> ^(cst_ConstraintAction csConstraint
      {((CustomToken)$LBR).getPayload(Tree.class)}
     )
;

csExtendedConstraintAction :
 csExtendedConstraint ARROW LBR RBR
 -> ^(cst_ConstraintAction csExtendedConstraint
      {((CustomToken)$LBR).getPayload(Tree.class)}
     )
;

csMatchArgument :
  (type=IDENTIFIER)? csTerm

  ->{type!=null}? ^(cst_TypedTerm csTerm ^(cst_TermType $type))
  ->              ^(cst_TypedTerm csTerm ^(cst_TermType ^(cst_TermTypeUnknown)))
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
 ->{a!=null}? ^(cst_AndConstraint csMatchArgumentConstraintList csConstraint)
 ->{o!=null}? ^(cst_OrConstraint csMatchArgumentConstraintList csConstraint)
 ->           csMatchArgumentConstraintList
; 

csMatchArgumentConstraintList :
  csMatchArgumentConstraint (COMMA c2=csMatchArgumentConstraint)* (COMMA)?
  ->{c2!=null}? ^(cst_AndConstraint csMatchArgumentConstraint*)
  ->            csMatchArgumentConstraint
;

csMatchArgumentConstraint :
  csPattern
  -> ^(cst_MatchArgumentConstraint csPattern)
;

/**
- match constraints :   pattern << term
- logical operations :  constraint ('&&'|'||') constraint
- numeric constraints : term ('>'|'<'|'>='|'<='|'=='|'!=') term 

priority in operators is :
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
  ->{or!=null}? ^(cst_OrConstraint csConstraint_priority2*)
  -> csConstraint_priority2
;

csConstraint_priority2 :
  csConstraint_priority3 (and=AND csConstraint_priority3)*
  ->{and!=null}? ^(cst_AndConstraint csConstraint_priority3*)
  -> csConstraint_priority3
;

csConstraint_priority3 :
 
  l=csTerm
  (gt=GREATERTHAN|ge=GREATEROREQU|lt=LOWERTHAN
  |le=LOWEROREQU |eq=DOUBLEEQUAL |ne=DIFFERENT)
  r=csTerm

 ->{gt!=null}? ^(cst_NumGreaterThan      $l $r)
 ->{ge!=null}? ^(cst_NumGreaterOrEqualTo $l $r)
 ->{lt!=null}? ^(cst_NumLessThan         $l $r)
 ->{le!=null}? ^(cst_NumLessOrEqualTo    $l $r)
 ->{eq!=null}? ^(cst_NumEqualTo          $l $r)
 ->/*ne!=null*/^(cst_NumDifferent        $l $r)
 

 | csConstraint_priority4
 -> csConstraint_priority4
;

csConstraint_priority4 :
 csPattern LARROW csTerm
 -> ^(cst_MatchTermConstraint csPattern csTerm)

 | LPAR csConstraint RPAR
 -> csConstraint
;
// Terms ======================================================
csTerm :
  IDENTIFIER (s=STAR)?
  ->{s!=null}? ^(cst_VariableNameStar IDENTIFIER )
  ->           ^(cst_VariableName IDENTIFIER)
 
 |IDENTIFIER LPAR (csTerm (COMMA csTerm)*)? RPAR
  -> ^(cst_Term IDENTIFIER ^(cst_concCstTerm csTerm*))
;
// Patterns ===================================================
csPattern :
  // myA@a(...
  IDENTIFIER AT csPattern
  -> ^(cst_AnnotatedPattern csPattern IDENTIFIER)

  //!a(...
 |ANTI csPattern
  -> ^(cst_Anti csPattern)

  //f'('...')'
 |csHeadSymbolList csExplicitTermList
  -> ^(cst_SymbolList csHeadSymbolList csExplicitTermList)
 
  //f'['...']'
 |csHeadSymbolList csImplicitPairList
  -> ^(cst_SymbolList csHeadSymbolList csImplicitPairList)

  // x
  // x*
 |IDENTIFIER (s=STAR)?
  -> {s!=null}? ^(cst_VariableStar IDENTIFIER)
  ->/*s==null*/ ^(cst_Variable IDENTIFIER)
  
  // _
  // _*
 |UNDERSCORE (s=STAR)? 
  -> {s!=null}? ^(cst_UnamedVariableStar)
  ->/*s==null*/ ^(cst_UnamedVariable)
  
  // 'a'
  // 'a'*
 |csConstantValue (s=STAR)?
  -> {s!=null}? ^(cst_ConstantStar csConstantValue)
  ->/*s==null*/ ^(cst_Constant csConstantValue)
;

// f
// (f|g)
// f?  -- should be --> f{theory:AU}
// f?? -- should be  --> f{theory:AC}
csHeadSymbolList :
  csHeadSymbol
  -> ^(cst_concCstHeadSymbol csHeadSymbol)
 | LPAR csHeadSymbol (PIPE csHeadSymbol)* RPAR 
  -> ^(cst_concCstHeadSymbol	 csHeadSymbol*)
; 

csHeadSymbol :
  IDENTIFIER
  -> ^(cst_HeadSymbol IDENTIFIER ^(cst_TheoryDEFAULT))
 |IDENTIFIER QMARK
  -> ^(cst_HeadSymbol IDENTIFIER ^(cst_TheoryAU))
 |IDENTIFIER DQMARK
  -> ^(cst_HeadSymbol IDENTIFIER ^(cst_TheoryAC))
 |csConstantValue 
 -> ^(cst_ConstantHeadSymbol csConstantValue ^(cst_TheoryDEFAULT))
 |csConstantValue QMARK
 -> ^(cst_ConstantHeadSymbol csConstantValue ^(cst_TheoryAU))
 |csConstantValue DQMARK
 -> ^(cst_ConstantHeadSymbol csConstantValue ^(cst_TheoryAC))
;

csExplicitTermList :
   LPAR (csPattern (COMMA csPattern)*)? RPAR

 -> ^(cst_concCstPattern csPattern*)
;

csImplicitPairList :
  LSQUAREBR (csPairPattern (COMMA csPairPattern)*)?  RSQUAREBR

  -> ^(cst_concCstPairPattern csPairPattern*)
;

csPairPattern :
 IDENTIFIER EQUAL csPattern

 -> ^(cst_PairPattern IDENTIFIER csPattern)
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

  -> ^(cst_OpConstruct
        {extractOptions((CommonToken)$tomTypeName.start, (CommonToken)$RBR)}
        $tomTypeName $ctorName csSlotList ^(cst_concCstOperator $ks*)
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

  -> ^(cst_OpArrayConstruct
        {extractOptions((CommonToken)$tomTypeName.start, (CommonToken)$RBR)}
        $tomTypeName $ctorName $typeName ^(cst_concCstOperator $ks*)
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

  -> ^(cst_OpListConstruct
        {extractOptions((CommonToken)$tomTypeName.start, (CommonToken)$RBR)}
        $tomTypeName $ctorName $typeName ^(cst_concCstOperator $ks*)
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
   ^(cst_TypetermConstruct
        {extractOptions((CommonToken)$typeName.start, (CommonToken)$RBR)}
        $typeName ^(cst_EmptyName) ^(cst_concCstOperator $ks* )
    )

  -> /*{$extend==null}*/
   ^(cst_TypetermConstruct
        {extractOptions((CommonToken)$typeName.start, (CommonToken)$RBR)}
        $typeName $extend ^(cst_concCstOperator $ks*)
    )
;



csSlotList :
  (csSlot (COMMA csSlot)*)?

  -> ^(cst_concCstSlot csSlot*)
;

csSlot : 
  slotName=csName COLON slotType=csName

  -> ^(cst_Slot $slotName $slotType)
;


// csKeyword* ====
csKeywordIsFsym :
  KEYWORD_IS_FSYM LPAR argName=csName RPAR
  LBR /* Host Code doing the test */ RBR

  -> ^(cst_IsFsym $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetSlot :
  KEYWORD_GET_SLOT LPAR slotName=csName COMMA termName=csName RPAR
  LBR /* Host Code accessing slot content */ RBR

  -> ^(cst_GetSlot $slotName $termName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMake :
  KEYWORD_MAKE LPAR argList=csNameList RPAR
  LBR /* Host Code making new object */ RBR

  -> ^(cst_Make $argList
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetHead :
  KEYWORD_GET_HEAD LPAR argName=csName RPAR 
  LBR /* Host Code accessing list head */ RBR

  -> ^(cst_GetHead $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetTail :
  KEYWORD_GET_TAIL LPAR argName=csName RPAR
  LBR /* Host Code accessing list tail */ RBR

  -> ^(cst_GetTail $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordIsEmpty :
  KEYWORD_IS_EMPTY LPAR argName=csName RPAR
  LBR /* Host Code emptiness expression */ RBR

  -> ^(cst_IsEmpty $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeEmpty_List :
  KEYWORD_MAKE_EMPTY LPAR RPAR
  LBR /* Host Code creating empty list */ RBR

  -> ^(cst_MakeEmptyList
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeEmpty_Array :
  KEYWORD_MAKE_EMPTY LPAR argName=csName RPAR
  LBR /* Host Code creating empty array */ RBR

  -> ^(cst_MakeEmptyArray $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeInsert :
  KEYWORD_MAKE_INSERT
    LPAR elementArgName=csName COMMA termArgName=csName RPAR
  LBR /* Host Code making insertion */ RBR

  -> ^(cst_MakeInsert $elementArgName $termArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetElement :
  KEYWORD_GET_ELEMENT
    LPAR termArgName=csName COMMA elementIndexArgName=csName RPAR
  LBR /* Host Code accessing this element */ RBR

  -> ^(cst_GetElement $termArgName $elementIndexArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetSize :
  KEYWORD_GET_SIZE LPAR termArgName=csName RPAR
  LBR /* Host Code accessing size */ RBR

  -> ^(cst_GetSize $termArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeAppend :
  KEYWORD_MAKE_APPEND 
    LPAR elementArgName=csName COMMA termArgName=csName RPAR
  LBR /* Host Code appending element */ RBR

  -> ^(cst_MakeAppend $elementArgName $termArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordImplement :
  KEYWORD_IMPLEMENT LBR /* Host Code implementation name*/ RBR

  -> ^(cst_Implement {((CustomToken)$LBR).getPayload(Tree.class)})
;

csKeywordIsSort :
  KEYWORD_IS_SORT LPAR argName=csName RPAR
    LBR /* Host Code boolean expression */ RBR

  -> ^(cst_IsSort $argName {((CustomToken)$LBR).getPayload(Tree.class)})
;

csKeywordEquals :
  KEYWORD_EQUALS LPAR arg1=csName COMMA arg2=csName RPAR
    LBR /* Host Code boolean expression */ RBR

  -> ^(cst_Equals $arg1 $arg2 
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csNameList :
  (csName (COMMA csName)*)?
  -> ^(cst_concCstName csName*)
;

csName :
  IDENTIFIER
  -> ^(cst_Name IDENTIFIER)
;

// Bq Terms =================================================================
/*
bqConstruct 
returns [int marker] :
  /*BQUOTE*/ /*t=csBQTerm
  -> $t
;

csBQTerm :
  csBQVar
  -> csBQVar

| csBQVarStar
  -> csBQVarStar

| csBQAppl
  -> csBQAppl

;

csBQVar :
  csName
  -> ^(cst_BQVar csName)
;

csBQVarStar :
  csName STAR
  -> ^(cst_BQVarStar csName)
;

csBQAppl :
  csName LPAR (csBQTerm (COMMA csBQTerm)*)? RPAR
  -> ^(cst_BQAppl csName ^(cst_concBQTerm csBQTerm*))
;
*/
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
  if(opensBlockLBR) {
     opensBlockLBR = false;

    HostParser parser = new HostParser(
    	null, null, new NegativeImbricationDetector('{', '}', 0));

      // XXX DEBUG ===
      if(HostParserDebugger.isOn()) {
        HostParserDebugger.getInstance()
        .debugNewCall(parser.getClassDesc(), input, "");
      }
      // === DEBUG ===

    Tree tree = parser.parseBlockList(input);

      // XXX DEBUG ===
      if(HostParserDebugger.isOn()) {
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
BQUOTE   : '`';
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
