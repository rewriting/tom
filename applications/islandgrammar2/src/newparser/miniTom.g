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
  
  private final TokenCustomizer tokenCustomizer = new TokenCustomizer();
 
  // add custom fields to ANTLR generated Tokens
  @Override 
  public void emit(Token t){
    super.emit(tokenCustomizer.customize(t));
  }
  
}

/* parser rules */
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
 csConstraint HostBlockOpen RBR
 -> ^(CsConstraintAction csConstraint
      {((CustomToken)$HostBlockOpen).getPayload(Tree.class)}
     )
;

csExtendedConstraintAction :
 csExtendedConstraint HostBlockOpen RBR
 -> ^(CsConstraintAction csExtendedConstraint
      {((CustomToken)$HostBlockOpen).getPayload(Tree.class)}
     )
;

csMatchArgument :
  (type=IDENTIFIER)? csTerm

  ->{type!=null}? ^(CsTypedTerm csTerm ^(CsTermType $type))
  ->              ^(CsTypedTerm csTerm ^(CsTermType ^(CsTermTypeUnknown)))
;

HostBlockOpen : ( options {greedy=true;} : ARROW WS '{')
{
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
 (i=IDENTIFIER AT)* csPlainPattern

  -> ^(CsPattern ^(CsAnnotationList IDENTIFIER*) csPlainPattern)
;

csPlainPattern
scope{ boolean anti;} @init{ $csPlainPattern::anti = false;}
:
 (ANTI {$csPlainPattern::anti=!$csPlainPattern::anti;} )*

(
  //(!)* f'('...')'
  csHeadSymbolList csExplicitTermList
  -> {$csPlainPattern::anti}?
     ^(CsAntiSymbolList csHeadSymbolList csExplicitTermList)
  -> ^(CsSymbolList csHeadSymbolList csExplicitTermList)
 //(!)* f'['...']'
 |csHeadSymbolList csImplicitPairList
  -> {$csPlainPattern::anti}?
     ^(CsAntiSymbolList csHeadSymbolList csImplicitPairList)
  -> ^(CsSymbolList csHeadSymbolList csImplicitPairList)

 |IDENTIFIER (s=STAR)?
  ->{$csPlainPattern::anti  && s!=null}? ^(CsAntiVariableStar IDENTIFIER)
  ->{$csPlainPattern::anti  && s==null}? ^(CsAntiVariable IDENTIFIER)
  ->{!$csPlainPattern::anti && s!=null}? ^(CsVariableStar IDENTIFIER)
  ->/*                 anti && s==null*/ ^(CsVariable IDENTIFIER)
 
 |{!$csPlainPattern::anti}?=> // don't allow anti before wildcard
  UNDERSCORE (s=STAR)? 
  -> {s!=null}? ^(CsUnamedVariableStar)
  ->/*s==null*/ ^(CsUnamedVariable)
 
 |csConstantValue (s=STAR)?
  ->{$csPlainPattern::anti  && s!=null}? ^(CsAntiConstantStar csConstantValue)
  ->{$csPlainPattern::anti  && s==null}? ^(CsAntiConstant csConstantValue)
  ->{!$csPlainPattern::anti && s!=null}? ^(CsConstantStar csConstantValue)
  ->/*               anti  && s==null*/ ^(CsConstant csConstantValue)
)
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
  -> ^(CsHeadSymbol IDENTIFIER ^(CsTheoryCS))
 |csConstantValue 
 -> ^(CsConstantHeadSymbol csConstantValue ^(CsTheoryDEFAULT))
 |csConstantValue QMARK
 -> ^(CsConstantHeadSymbol csConstantValue ^(CsTheoryAU))
 |csConstantValue DQMARK
 -> ^(CsConstantHeadSymbol csConstantValue ^(CsTheoryCS))
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
LBR     : '{';
RPAR 	: ')';
LPAR	: '(';
COMMA	: ',';
ARROW	: '->';
STAR	: '*';
UNDERSCORE:'_';
AT	: '@';
ANTI	: '!';
DQUOTE  : '"'; //"
SQUOTE  : '\'';
BQUTE   : '`';

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
