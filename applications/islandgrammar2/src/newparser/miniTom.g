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
returns [int marker]
:LPAR  RPAR LBR (csPatternOrConstraintAction)* RBR
{
$marker = ((CustomToken)$RBR).getPayload(Integer.class);
}
-> ^(MATCH csPatternOrConstraintAction* )
;

csPatternOrConstraintAction :
 csPatternAction | csConstraintAction
;

csPatternAction :
 (pattern=csPatternList) ((and=AND|or=OR) csConstraint)? HostBlockOpen RBR
 
  ->{$and!=null}? ^(CsAndConstraintPatternAction csPatternList csConstraint
  {((CustomToken)$HostBlockOpen).getPayload(Tree.class)})

  ->{$or!=null}? ^(CsOrConstraintPatternAction csPatternList csConstraint
  {((CustomToken)$HostBlockOpen).getPayload(Tree.class)})

  -> ^(CsPatternAction $pattern
  {((CustomToken)$HostBlockOpen).getPayload(Tree.class)})
;

csConstraintAction :
  csConstraint HostBlockOpen RBR

  -> ^(CsConstraintAction csConstraint
  {
  ((CustomToken)$HostBlockOpen).getPayload(Tree.class)
  }
  )
;

HostBlockOpen : ( options {greedy=true;} : ARROW WS '{')
{
HostParser parser = new HostParser(new NegativeImbricationDetector('{', '}', 0));

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

// csConstraint uses several rules to ensure operators
// priority is respected. csConstraint_* should not be
// called from others rules, only csConstraint.
csConstraint : 
 csConstraint_priority1
;

csConstraint_priority1 :
  c1=csConstraint_priority2 OR c2=csConstraint_priority2
  -> ^(CsOrConstraint $c1 $c2)
  | csConstraint_priority2
;
csConstraint_priority2 :
  c1=csConstraint_priority3 AND c2=csConstraint_priority3
  -> ^(CsAndConstraint $c1 $c2)
  | csConstraint_priority3
;

csConstraint_priority3 :
  csPattern LARROW csTerm
 -> ^(CsMatchConstraint csPattern csTerm)
 | csConstraint_priority4
;

csConstraint_priority4 :
  l=csTerm GREATERTHAN r=csTerm
  -> ^(CsNumGreaterThan $l $r)
 
 |l=csTerm GREATEROREQU r=csTerm
  -> ^(CsNumGreaterOrEqualTo $l $r)

 |l=csTerm LOWERTHAN r=csTerm
  -> ^(CsNumLessThan $l $r)
 
 |l=csTerm LOWEROREQU r=csTerm
  -> ^(CsNumLessOrEqualTo $l $r)
 
 |l=csTerm DOUBLEEQUAL r=csTerm
  -> ^(CsNumEqualTo $l $r)

 |l=csTerm DIFFERENT r=csTerm
  -> ^(CsNumDifferent $l $r)

 |LPAR csConstraint RPAR
 -> csConstraint
;

// constraint's subject
csTerm :
  IDENTIFIER (s=STAR)*
  -> {s!=null}? ^(CsVariableNameStar IDENTIFIER)
  ->            ^(CsVariableName IDENTIFIER)
 
 |IDENTIFIER LPAR (csTerm (COMMA csTerm)*)? RPAR
  -> ^(CsTerm IDENTIFIER ^(CsTermList csTerm*))
;

// Patterns ===================================================
csPatternList :
  csPattern (COMMA csPattern)*

  -> ^(CsPatternList csPattern*)
;

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
  -> {$csPlainPattern::anti}? ^(CsAntiSymbolList csHeadSymbolList csExplicitTermList)
  ->                          ^(CsSymbolList csHeadSymbolList csExplicitTermList)
 //(!)* f'['...']'
 |csHeadSymbolList csImplicitPairList
  -> {$csPlainPattern::anti}? ^(CsAntiSymbolList csHeadSymbolList csImplicitPairList)
  ->                          ^(CsSymbolList csHeadSymbolList csImplicitPairList)

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

 -> ^(CsExplicitTermList csPattern*)
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

GREATERTHAN : '>';
GREATEROREQU : '>=';
LOWERTHAN : '<';
LOWEROREQU : '<=';
DOUBLEEQUAL : '==';
DIFFERENT : '!=';

LARROW : '<<';
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
