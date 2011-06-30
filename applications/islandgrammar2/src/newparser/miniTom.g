grammar miniTom;

options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
}

tokens {
  HOSTBLOCK;
  MATCH;
  MATCHARG;
  TYPED;
  UNTYPED;
  TYPETERM;
  PATTERNACTION;

// csPattern
  CsPattern;
  CsPlainPattern;
  CsAnnotation;  
  CsPatternList;

// Pairs
  CsPairPattern;
  CsSlotName;
  CsImplicitPairList;

  CsSymbolList;

  CsHeadSymbol; 
  CsHeadSymbolQMark;
  CsHeadSymbolList;

// csTerm
  CsTerm;
  CsExplicitTermList; // CsNamedTermList;
 
  CsTermList;
  CsVariable;
  CsAntiVariable;
  CsVariableStar;
  CsAntiVariableStar;
  CsUnamedVariable;
  CsUnamedVariableStar;
  CsConstant;
  CsAntiConstant;
  CsConstantStar;
  CsAntiConstantStar;

  CsName;
  CsValue;
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
:LPAR  RPAR LBR (patternAction)* RBR
{
$marker = ((CustomToken)$RBR).getPayload(Integer.class);
}
-> ^(MATCH patternAction*)
;

matchArguments : ;


patternAction :
 (pattern=csPatternList) HostBlockOpen RBR
 -> ^(PATTERNACTION $pattern
 {
 ((CustomToken)$HostBlockOpen).getPayload(Tree.class)
 });

HostBlockOpen : ( options {greedy=true;} : ARROW WS '{')
{
HostParser parser = new HostParser(new NegativeImbricationDetector('{', '}', 0));

      // XXX DEBUG ===
      if(HostParserDebugger.isOn()){
        HostParserDebugger.getInstance()
        .debugNewCall(parser.getClassDesc(), input, "");
      }
      // === DEBUG ===

Tree tree = parser.parse(input);

      // XXX DEBUG ===
      if(HostParserDebugger.isOn()){
        HostParserDebugger.getInstance()
        .debugReturnedCall(parser.getClassDesc(), input, "");
      }
      // === DEBUG ===
      
tokenCustomizer.prepareNextToken(tree);
}
;

// Patterns ===================================================
csPatternList :
  csPattern (COMMA csPattern)*

  -> ^(CsPatternList csPattern*)
;

csPattern :
 (i=IDENTIFIER AT)? csPlainPattern

  -> {i==null}? ^(CsPattern csPlainPattern)
  ->/*i!=null*/ ^(CsPattern ^(CsAnnotation IDENTIFIER) csPlainPattern)
;


csPlainPattern :
 //csExplicitTermList // TODO => include in HeadSymbol
 // -> ^(CsPlainPattern csExplicitTermList)
 csHeadSymbolList csExplicitTermList
  -> ^(CsPlainPattern csHeadSymbolList csExplicitTermList)
|csHeadSymbolList csImplicitPairList
  -> ^(CsPlainPattern csHeadSymbolList csImplicitPairList)
|csExplicitTermList
  -> ^(CsPlainPattern csExplicitTermList)
|csVariable // produces Cs(Anti)?Variable(Star)?
  -> ^(CsPlainPattern csVariable)
|csUnamedVariable // produces CsUnamedVariable(Star)?
  -> ^(CsPlainPattern csUnamedVariable)
|csConstant
  -> ^(CsPlainPattern csConstant)
;

csHeadSymbolList :
  csHeadSymbol
  -> ^(CsHeadSymbolList csHeadSymbol)
 | LPAR csHeadSymbol (PIPE csHeadSymbol)* RPAR 
  -> ^(CsHeadSymbolList	 csHeadSymbol*)
; 

csHeadSymbol :
  IDENTIFIER
  -> ^(CsHeadSymbol IDENTIFIER)
 |IDENTIFIER QMARK
  -> ^(CsHeadSymbolQMark IDENTIFIER)
;

csExplicitTermList : // {System.out.println("Parsing CsNamedTermList");}
  LPAR (csPattern (COMMA csPattern)*)? RPAR

 -> ^(CsExplicitTermList csPattern*)
;

csImplicitPairList :
  LSQUAREBR (csPairPattern (COMMA csPairPattern)*)?  RSQUAREBR

  -> ^(CsImplicitPairList csPairPattern*)
;

csPairPattern :
 IDENTIFIER EQUAL csPattern

 -> ^(CsPairPattern ^(CsSlotName IDENTIFIER) csPattern)
;
	
csVariable : // {System.out.println("Parsing CsVariable");}
  (a=ANTI)? IDENTIFIER (s=STAR)?
 
  -> {a!=null && s!=null}? ^(CsAntiVariableStar ^(CsName IDENTIFIER))
  -> {a==null && s!=null}? ^(CsVariableStar ^(CsName IDENTIFIER))
  -> {a!=null && s==null}? ^(CsAntiVariable ^(CsName IDENTIFIER))
  ->/*a==null && s==null*/ ^(CsVariable ^(CsName IDENTIFIER))
;

csUnamedVariable : // {System.out.println("Parsing CsUnamedVariable");}
  UNDERSCORE (s=STAR)? 

  -> {s!=null}? ^(CsUnamedVariableStar)
  ->/*s==null*/ ^(CsUnamedVariable)
;
        
csConstant : // {System.out.println("Parsing CsConstant");}
  (a=ANTI)? (value=(INTEGER|DOUBLE|STRING|CHAR)) (s=STAR)?

  -> {a!=null && s!=null}? ^(CsAntiConstantStar ^(CsValue IDENTIFIER))
  -> {a==null && s!=null}? ^(CsConstantStar ^(CsValue IDENTIFIER))
  -> {a!=null && s==null}? ^(CsAntiConstant ^(CsValue IDENTIFIER))
  ->/*a!=null && s==null*/ ^(CsConstant ^(CsValue IDENTIFIER))
;



RBR :  '}'
{
// every token generated by antlr pass throw our
// tokenCustomizer.customize(Token). Next emitted
// token, this RBR token, will be "customized" with
// with input.mark() returned value as payload
tokenCustomizer.prepareNextToken(input.mark());
}
;

PIPE 	: '|';
QMARK	:'?';
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

// the solution below doesn't seems better
// it's just more painful to write...
//DEFAULT : ~('('|')'|'{'|'}'|'\r'|'\n'|'\t'|' '|'A'..'Z'|'a'..'z'|'0'..'9') { $channel=HIDDEN; };

