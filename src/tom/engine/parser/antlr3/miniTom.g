grammar miniTom;


options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
  tokenVocab=CSTTokens;
}

@parser::header {
package tom.engine.parser.antlr3;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.CommonToken;
import static tom.engine.parser.antlr3.TreeFactory.*;
}

@lexer::header {
package tom.engine.parser.antlr3;
import org.antlr.runtime.tree.Tree;

import static tom.engine.parser.antlr3.TreeFactory.*;
import static tom.engine.parser.antlr3.miniTomParser.*;
import tom.engine.parser.antlr3.streamanalysis.*;
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
    int firstCharColumn = t.getCharPositionInLine()+1;
    int lastCharLine = firstCharLine+lines.length-1;
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

  -> ^(Cst_IncludeConstruct $filename)
;

// StrategyConstruct
csStrategyConstruct
returns [int marker]:
csName LPAR csSlotList RPAR EXTENDS BQUOTE? csBQTerm[false] LBR csStrategyVisitList RBR

{$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> ^(Cst_StrategyConstruct
            {extractOptions((CommonToken)$LPAR, (CommonToken)$RBR)}
            csName
            csSlotList
            csBQTerm
            csStrategyVisitList
      )
  ;

csStrategyVisitList :
  csStrategyVisit* -> ^(ConcCstVisit csStrategyVisit*)
  ;

csStrategyVisit :
  VISIT IDENTIFIER LBR (csVisitAction)* RBR
    -> ^(Cst_VisitTerm 
          ^(Cst_Type IDENTIFIER ) 
          ^(ConcCstConstraintAction csVisitAction* )
          {extractOptions((CommonToken)$VISIT, (CommonToken)$RBR)}
        )
  ;

csVisitAction :
  (IDENTIFIER l=COLON)? csExtendedConstraint ARROW LBR RBR //handle  toto -> { blocklist }
    -> {$l!=null}? ^(Cst_ConstraintAction csExtendedConstraint
                           {((CustomToken)$LBR).getPayload(Tree.class)}
                           ^(ConcCstOption ^(Cst_Label IDENTIFIER))
                           )
    ->            ^(Cst_ConstraintAction csExtendedConstraint
                           {((CustomToken)$LBR).getPayload(Tree.class)}
                           ^(ConcCstOption)
                     )
  | (IDENTIFIER l=COLON)? csExtendedConstraint ARROW csBQTerm[false] //handle toto -> f(a()) - is a BQTerm
    -> {$l!=null}? ^(Cst_ConstraintAction csExtendedConstraint
                           /*{((CustomToken)$ARROW).getPayload(Tree.class)}*/
                           /*csBQTerm*/
                           ^(ConcCstBlock ^(Cst_BQTermToBlock csBQTerm))
                           ^(ConcCstOption ^(Cst_Label IDENTIFIER))
                   )
    ->            ^(Cst_ConstraintAction csExtendedConstraint
                           /*{((CustomToken)$ARROW).getPayload(Tree.class)}*/
                           /*csBQTerm*/
                           ^(ConcCstBlock ^(Cst_BQTermToBlock csBQTerm))
                           ^(ConcCstOption)
                   )
  ;

// MatchConstruct ===========================================================
/*
   When parsing parser rule ANTLR's Parse tends to consume "to much" chars.
   To respect ParserAction.doAction contract, doAction implementation needs 
   to rewind stream to the marker returned by matchconstruct.
 */
csMatchConstruct
returns [int marker]:

// "%match" already consumed when this rule is called

// with args
LPAR csBQTerm[false] /*csMatchArgument*/ (COMMA csBQTerm[false] /*csMatchArgument*/)* RPAR
LBR
csExtendedConstraintAction*
RBR

// extract and return CharStream's marker
{$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

-> ^( Cst_MatchConstruct
    {extractOptions((CommonToken)$LPAR, (CommonToken)$RBR)}
    ^( ConcCstBQTerm csBQTerm*)
    ^( ConcCstConstraintAction csExtendedConstraintAction* )
    )

|
// witout args
(LPAR RPAR)?
LBR
csConstraintAction*
RBR

// extract and return CharStream's marker
{$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

-> ^( Cst_MatchConstruct
    {extractOptions((CommonToken)$LBR, (CommonToken)$RBR)}
    ^( ConcCstBQTerm )
    ^( ConcCstConstraintAction csConstraintAction* )
    )
;

csConstraintAction :
(IDENTIFIER l=COLON)? csConstraint ARROW LBR RBR
  -> {$l!=null}? ^(Cst_ConstraintAction csConstraint
                  {((CustomToken)$LBR).getPayload(Tree.class)}
                  ^(ConcCstOption ^(Cst_Label IDENTIFIER ))
                 )
  ->             ^(Cst_ConstraintAction csConstraint
                  {((CustomToken)$LBR).getPayload(Tree.class)}
                  ^(ConcCstOption)
                 )
  ;

csExtendedConstraintAction :
(IDENTIFIER l=COLON)? csExtendedConstraint ARROW LBR RBR
  -> {$l!=null}? ^(Cst_ConstraintAction csExtendedConstraint
                  {((CustomToken)$LBR).getPayload(Tree.class)}
                  ^(ConcCstOption ^(Cst_Label IDENTIFIER ))
                 )
  ->             ^(Cst_ConstraintAction csExtendedConstraint
                  {((CustomToken)$LBR).getPayload(Tree.class)}
                  ^(ConcCstOption)
                 )
  ;

csBQTerm [ boolean typedAppl ] :
  (type=IDENTIFIER)? BQUOTE? bqname=IDENTIFIER LPAR (a+=csBQTerm[false] (COMMA a+=csBQTerm[false])*)? RPAR
   -> {$typedAppl && type==null}? ^(Cst_BQAppl {extractOptions((CommonToken)$LPAR, (CommonToken)$RPAR)}
       $bqname ^(ConcCstBQTerm $a*)) ^(Cst_TypeUnknown)
   -> {$typedAppl && type!=null}? ^(Cst_BQAppl {extractOptions((CommonToken)$LPAR, (CommonToken)$RPAR)}
       $bqname ^(ConcCstBQTerm $a*)) ^(Cst_Type $type)
   ->                             ^(Cst_BQAppl {extractOptions((CommonToken)$LPAR, (CommonToken)$RPAR)}
       $bqname ^(ConcCstBQTerm $a*))
  
  |(type=IDENTIFIER)? BQUOTE? name=IDENTIFIER (s=STAR)?
   ->{s!=null && type!=null && $typedAppl}? ^(Cst_BQVarStar {extractOptions((CommonToken)$name)} $name ^(Cst_Type $type)) ^(Cst_Type $type)
   ->{s!=null && type!=null && !$typedAppl}? ^(Cst_BQVarStar {extractOptions((CommonToken)$name)} $name ^(Cst_Type $type))
   
   ->{s!=null && type==null && $typedAppl}? ^(Cst_BQVarStar {extractOptions((CommonToken)$name)} $name ^(Cst_TypeUnknown )) ^(Cst_TypeUnknown)
   ->{s!=null && type==null && !$typedAppl}? ^(Cst_BQVarStar {extractOptions((CommonToken)$name)} $name ^(Cst_TypeUnknown ))
   
   ->{s==null && type!=null && $typedAppl}? ^(Cst_BQVar {extractOptions((CommonToken)$name)} $name ^(Cst_Type $type)) ^(Cst_Type $type)
   ->{s==null && type!=null && !$typedAppl}? ^(Cst_BQVar {extractOptions((CommonToken)$name)} $name ^(Cst_Type $type))
   
   ->{s==null && type==null && $typedAppl}? ^(Cst_BQVar {extractOptions((CommonToken)$name)} $name ^(Cst_TypeUnknown )) ^(Cst_TypeUnknown)
   ->                                       ^(Cst_BQVar {extractOptions((CommonToken)$name)} $name ^(Cst_TypeUnknown ))
  
  | csConstantValue -> ^(Cst_BQConstant
      {extractOptions((CommonToken)$csConstantValue.start, (CommonToken)$csConstantValue.stop)} csConstantValue )
;

 // Constraints ===============================================
/**
- pattern list with or without additionnal constraints:
  pattern (',' pattern )* (('&&'|'||') csontraint)?

  everything is then stored in CST as a constraint.
  every pattern in pattern list is seen as a constraint to
  match identically positionned matchConstruct's argument.
*/
csExtendedConstraint :
 csMatchArgumentConstraintList ((a=AND|o=OR) csConstraint)?
 ->{a!=null}? ^(Cst_AndConstraint csMatchArgumentConstraintList csConstraint)
 ->{o!=null}? ^(Cst_OrConstraint csMatchArgumentConstraintList csConstraint)
 ->           csMatchArgumentConstraintList
; 

csMatchArgumentConstraintList :
  csMatchArgumentConstraint (COMMA c2=csMatchArgumentConstraint)* (COMMA)?
  ->{c2!=null}? ^(Cst_AndConstraint csMatchArgumentConstraint*)
  ->            csMatchArgumentConstraint
;

csMatchArgumentConstraint :
  csPattern
  -> ^(Cst_MatchArgumentConstraint csPattern)
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
  ->{or!=null}? ^(Cst_OrConstraint csConstraint_priority2*)
  -> csConstraint_priority2
;

csConstraint_priority2 :
  csConstraint_priority3 (and=AND csConstraint_priority3)*
  ->{and!=null}? ^(Cst_AndConstraint csConstraint_priority3*)
  -> csConstraint_priority3
;

csConstraint_priority3 :
 
  csPattern LARROW csBQTerm[true]
  -> ^(Cst_MatchTermConstraint csPattern csBQTerm)
 
  | l=csTerm 
    (gt=GREATERTHAN|ge=GREATEROREQU|lt=LOWERTHAN
    |le=LOWEROREQU |eq=DOUBLEEQUAL |ne=DIFFERENT)
    r=csTerm

  ->{gt!=null}? ^(Cst_NumGreaterThan      $l $r)
  ->{ge!=null}? ^(Cst_NumGreaterOrEqualTo $l $r)
  ->{lt!=null}? ^(Cst_NumLessThan         $l $r)
  ->{le!=null}? ^(Cst_NumLessOrEqualTo    $l $r)
  ->{eq!=null}? ^(Cst_NumEqualTo          $l $r)
  ->/*ne!=null*/^(Cst_NumDifferent        $l $r)
 
  | LPAR csConstraint RPAR
  -> csConstraint

;

// Terms ======================================================
csTerm :
  IDENTIFIER (s=STAR)?
  ->{s!=null}? ^(Cst_TermVariableStar IDENTIFIER )
  ->           ^(Cst_TermVariable IDENTIFIER)
 
 |IDENTIFIER LPAR (csTerm (COMMA csTerm)*)? RPAR
  -> ^(Cst_TermAppl IDENTIFIER ^(ConcCstTerm csTerm*))
;
// Patterns ===================================================
csPattern :
  // myA@a(...
  IDENTIFIER AT csPattern
  -> ^(Cst_AnnotatedPattern csPattern IDENTIFIER)

  //!a(...
 |ANTI csPattern
  -> ^(Cst_Anti csPattern)

  //f'('...')'
 |csHeadSymbolList csExplicitTermList
  -> ^(Cst_Appl csHeadSymbolList csExplicitTermList)
 
  //f'['...']'
 |csHeadSymbolList csImplicitPairList
  -> ^(Cst_RecordAppl csHeadSymbolList csImplicitPairList)

  // x
  // x*
 |IDENTIFIER (s=STAR)?
  -> {s!=null}? ^(Cst_VariableStar IDENTIFIER)
  ->/*s==null*/ ^(Cst_Variable IDENTIFIER)
  
  // _
  // _*
 |UNDERSCORE (s=STAR)? 
  -> {s!=null}? ^(Cst_UnamedVariableStar)
  ->/*s==null*/ ^(Cst_UnamedVariable)
  
  // 1 | 3.14 | "foo" | 'a'
  // 'a'*
 |csConstantValue (s=STAR)?
  -> {s!=null}? ^(Cst_ConstantStar csConstantValue)
  ->            ^(Cst_Constant csConstantValue)
;

// f
// (f|g)
// f?  -- should be --> f{theory:AU}
// f?? -- should be  --> f{theory:AC}
csHeadSymbolList :
  csHeadSymbol
  -> ^(ConcCstSymbol csHeadSymbol)
 | LPAR csHeadSymbol (PIPE csHeadSymbol)* RPAR 
  -> ^(ConcCstSymbol	 csHeadSymbol*)
; 

csHeadSymbol :
  IDENTIFIER
  -> ^(Cst_Symbol IDENTIFIER ^(Cst_TheoryDEFAULT))
 |IDENTIFIER QMARK
  -> ^(Cst_Symbol IDENTIFIER ^(Cst_TheoryAU))
 |IDENTIFIER DQMARK
  -> ^(Cst_Symbol IDENTIFIER ^(Cst_TheoryAC))
 |INTEGER
  -> ^(Cst_ConstantInt INTEGER)
 |LONG
  -> ^(Cst_ConstantLong LONG)
 |CHAR
  -> ^(Cst_ConstantChar CHAR)
 |DOUBLE
  -> ^(Cst_ConstantDouble DOUBLE)
 |STRING
  -> ^(Cst_ConstantString STRING)
;

csConstantValue :
  INTEGER|LONG|CHAR|DOUBLE|STRING
  ;

csExplicitTermList :
   LPAR (csPattern (COMMA csPattern)*)? RPAR

 -> ^(ConcCstPattern csPattern*)
;

csImplicitPairList :
  LSQUAREBR (csPairPattern (COMMA csPairPattern)*)?  RSQUAREBR

  -> ^(ConcCstPairPattern csPairPattern*)
;

csPairPattern :
 IDENTIFIER EQUAL csPattern

 -> ^(Cst_PairPattern IDENTIFIER csPattern)
;

// OperatorConstruct & TypeTermConstruct ====================================
csOperatorConstruct 
returns [int marker] :

  //%op already consumed when this rule is called
  codomain=IDENTIFIER ctorName=csName LPAR csSlotList RPAR
  LBR 
    (  ks+=csKeywordIsFsym
     | ks+=csKeywordMake
     | ks+=csKeywordGetSlot
     | ks+=csKeywordGetDefault
     | ks+=csKeywordOpImplement 
    )*
  RBR

  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> ^(Cst_OpConstruct
        {extractOptions((CommonToken)$codomain, (CommonToken)$RBR)}
        ^(Cst_Type $codomain) $ctorName csSlotList ^(ConcCstOperator $ks*)
      ) 
;

csOperatorArrayConstruct
returns [int marker] :

  //%oparray already consumed when this rule is called
  codomain=IDENTIFIER ctorName=csName LPAR domain=IDENTIFIER STAR RPAR
  LBR  
     ks+=csKeywordIsFsym
   ( ks+=csKeywordMakeEmpty_Array
    |ks+=csKeywordMakeAppend
    |ks+=csKeywordGetElement
    |ks+=csKeywordGetSize
   )*
  RBR

  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> ^(Cst_OpArrayConstruct
        {extractOptions((CommonToken)$codomain, (CommonToken)$RBR)}
        ^(Cst_Type $codomain) $ctorName ^(Cst_Type $domain) ^(ConcCstOperator $ks*)
      )
; 

csOperatorListConstruct
returns [int marker] :

  //%oplist already consumed when this rule is called
  codomain=IDENTIFIER ctorName=csName LPAR domain=IDENTIFIER STAR RPAR
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

  -> ^(Cst_OpListConstruct
        {extractOptions((CommonToken)$codomain, (CommonToken)$RBR)}
        ^(Cst_Type $codomain) $ctorName ^(Cst_Type $domain) ^(ConcCstOperator $ks*)
      )
; 


csTypetermConstruct
returns [int marker] :

  //%typeterm already consumed when this rule is called
  typeName=IDENTIFIER (EXTENDS extend=IDENTIFIER)?
  LBR
    ks+=csKeywordImplement (ks+=csKeywordIsSort)? (ks+=csKeywordEquals)?
  RBR

 {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}

  -> {extend==null}?
   ^(Cst_TypetermConstruct
        {extractOptions((CommonToken)$typeName, (CommonToken)$RBR)}
        ^(Cst_Type $typeName) ^(Cst_TypeUnknown) ^(ConcCstOperator $ks* )
    )

  -> /*{$extend==null}*/
   ^(Cst_TypetermConstruct
        {extractOptions((CommonToken)$typeName, (CommonToken)$RBR)}
        ^(Cst_Type $typeName) ^(Cst_Type $extend) ^(ConcCstOperator $ks*)
    )
;

csSlotList :
  (csSlot (COMMA csSlot)*)?  -> ^(ConcCstSlot csSlot*)
;

csSlot : 
    name=IDENTIFIER COLON type=IDENTIFIER -> ^(Cst_Slot ^(Cst_Name $name) ^(Cst_Type $type))
  | type=IDENTIFIER name=IDENTIFIER ->  ^(Cst_Slot ^(Cst_Name $name) ^(Cst_Type $type))
;


// csKeyword* ====
csKeywordIsFsym :
  KEYWORD_IS_FSYM LPAR argName=csName RPAR
  LBR /* Host Code doing the test */ RBR

  -> ^(Cst_IsFsym $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetSlot :
  KEYWORD_GET_SLOT LPAR slotName=csName COMMA termName=csName RPAR
  LBR /* Host Code accessing slot content */ RBR

  -> ^(Cst_GetSlot $slotName $termName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMake :
  KEYWORD_MAKE LPAR argList=csNameList RPAR
  LBR /* Host Code making new object */ RBR

  -> ^(Cst_Make $argList
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetDefault :
  KEYWORD_GET_DEFAULT LPAR argName=csName RPAR
  LBR /* Host Code making new object */ RBR

  -> ^(Cst_GetDefault $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordOpImplement :
  KEYWORD_IMPLEMENT LPAR RPAR
  LBR /* Host Code doing the test */ RBR
  -> ^(Cst_Implement {((CustomToken)$LBR).getPayload(Tree.class)})
;


csKeywordGetHead :
  KEYWORD_GET_HEAD LPAR argName=csName RPAR 
  LBR /* Host Code accessing list head */ RBR

  -> ^(Cst_GetHead $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetTail :
  KEYWORD_GET_TAIL LPAR argName=csName RPAR
  LBR /* Host Code accessing list tail */ RBR

  -> ^(Cst_GetTail $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordIsEmpty :
  KEYWORD_IS_EMPTY LPAR argName=csName RPAR
  LBR /* Host Code emptiness expression */ RBR

  -> ^(Cst_IsEmpty $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeEmpty_List :
  KEYWORD_MAKE_EMPTY LPAR RPAR
  LBR /* Host Code creating empty list */ RBR

  -> ^(Cst_MakeEmptyList
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeEmpty_Array :
  KEYWORD_MAKE_EMPTY LPAR argName=csName RPAR
  LBR /* Host Code creating empty array */ RBR

  -> ^(Cst_MakeEmptyArray $argName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeInsert :
  KEYWORD_MAKE_INSERT
    LPAR elementArgName=csName COMMA termArgName=csName RPAR
  LBR /* Host Code making insertion */ RBR

  -> ^(Cst_MakeInsert $elementArgName $termArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetElement :
  KEYWORD_GET_ELEMENT
    LPAR termArgName=csName COMMA elementIndexArgName=csName RPAR
  LBR /* Host Code accessing this element */ RBR

  -> ^(Cst_GetElement $termArgName $elementIndexArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordGetSize :
  KEYWORD_GET_SIZE LPAR termArgName=csName RPAR
  LBR /* Host Code accessing size */ RBR

  -> ^(Cst_GetSize $termArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordMakeAppend :
  KEYWORD_MAKE_APPEND 
    LPAR elementArgName=csName COMMA termArgName=csName RPAR
  LBR /* Host Code appending element */ RBR

  -> ^(Cst_MakeAppend $elementArgName $termArgName
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csKeywordImplement :
  KEYWORD_IMPLEMENT LBR /* Host Code implementation name*/ RBR

  -> ^(Cst_Implement {((CustomToken)$LBR).getPayload(Tree.class)})
;

csKeywordIsSort :
  KEYWORD_IS_SORT LPAR argName=csName RPAR
    LBR /* Host Code boolean expression */ RBR

  -> ^(Cst_IsSort $argName {((CustomToken)$LBR).getPayload(Tree.class)})
;

csKeywordEquals :
  KEYWORD_EQUALS LPAR arg1=csName COMMA arg2=csName RPAR
    LBR /* Host Code boolean expression */ RBR

  -> ^(Cst_Equals $arg1 $arg2 
        {((CustomToken)$LBR).getPayload(Tree.class)}
      )
;

csNameList :
  (csName (COMMA csName)*)?
  -> ^(ConcCstName csName*)
;

csName :
  IDENTIFIER
  -> ^(Cst_Name IDENTIFIER)
;

// Lexer Rules =============================================================

// funky ones
KEYWORD_IS_FSYM     : 'is_fsym'     { opensBlockLBR = true; };
KEYWORD_GET_SLOT    : 'get_slot'    { opensBlockLBR = true; };
KEYWORD_GET_DEFAULT : 'get_default' { opensBlockLBR = true; };
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
      //if(HostParserDebugger.isOn()) {
      //  HostParserDebugger.getInstance()
      //  .debugNewCall(parser.getClassDesc(), input, "");
      //}
      // === DEBUG ===

    Tree tree = parser.parseBlockList(input);

      // XXX DEBUG ===
      //if(HostParserDebugger.isOn()) {
      //  HostParserDebugger.getInstance()
      //  .debugReturnedCall(parser.getClassDesc(), input, "");
     // }
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
VISIT   : 'visit';

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

IDENTIFIER 	: ('_')? LETTER (LETTER | DIGIT | '_' | '.' )*;

fragment
MINUS : '-' ;

INTEGER : (MINUS)? (DIGIT)+;
fragment
UNSIGNED_DOUBLE : (DIGIT)+'.'(DIGIT)* | '.' (DIGIT)+;
DOUBLE  : (MINUS)? UNSIGNED_DOUBLE;
LONG    : (MINUS)? (DIGIT)+ LONG_SUFFIX;
/*NUM    : 
  (MINUS)? ('0' ( 
                 ( ('x'|'X') HEX_DIGIT+ )
                |( ('0'..'9')+ (DOT|EXPONENT|FLOAT_SUFFIX) )
                |( ('0'..'7')+ )
                )?
            |('1'..'9') ('0'..'9')*
           )
           (
           ('l'|'L')
           )?
  ;*/

/*fragment
PLUS  : '+' ;
fragment
DOT   : '.' ;*/

/*fragment
ID_MINUS:
  IDENTIFIER MINUS ('a'..'z'|'A'..'Z'|(DIGIT)+|UNSIGNED_DOUBLE) (
        MINUS ('a'..'z' | 'A'..'Z')
      | IDENTIFIER
      )*
  ;*/

fragment
HEX_DIGIT : ('0'..'9'|'A'..'F'|'a'..'f');
/*fragment
EXPONENT : ('e'|'E') ( PLUS | MINUS )? ('0'..'9')+ ;
fragment
FLOAT_SUFFIX : 'f'|'F'|'d'|'D' ;*/
fragment
LONG_SUFFIX : 'l'|'L' ;

STRING  : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"';
CHAR    : '\'' ( ESC | ~('\''|'\n'|'\r'|'\\') )+ '\'';

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

fragment
ESC
  : '\\'
    ( 'n'
    | 'r'
    | 't'
    | 'b'
    | 'f'
    | '"'
    | '\''
    | '\\'
    | ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    | '0'..'3'
      (
        '0'..'7'
        (
          '0'..'7'
        )?
      )?
    | '4'..'7'
      (
      '0'..'7'
      )?
    )
  ;
