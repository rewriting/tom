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
It's usually not a problem, but here we need input CharStream index to be
at closing bracket after this rule have been parsed.

To do so we :
 1) mark every closing bracket (when Lexing, see 'RBR' Lexer rule) and store
 marks in a way that allow us to get them back with datas available at at
 Parsing time (line number and position position in line)
 2) return line number and position in line RBR Token that close
 matchconstruct.
 3) once matchconstruct have returned, we use returned values to get back
 the right mark and rewind input.
 
Remarks :
 0.a) it's possible to rewind an ANTLR CharStream ONLY IF it have previously been
 marked at the point in stream we want to rewind to.
 0.b) ANTLR's Parsers and Lexers also uses marks, so we need to provide mark
 indentifier to rewind().
 1) we mark more closing bracket than we need (actually we mark all of them)
 we do that because to mark the input CharStream we need to have access to it.
 this can be done only at Lexing time. While lexing, there is no way the know
 which closing bracket is closing a matchconsruct and which one is not, this is only
 possible at Parsing time. So we mark all of them and figure out later wich one to
 use.
 2) be aware that RBR's (RBR is a Token) position in line value is 1 less than position
 in line of the mark we want to rewind to.
*/
matchconstruct
returns [int marker]
:LPAR RPAR LBR (patternaction)* RBR
{
// set returned value marker
// allows caller to rewind input
// every RBR Token is actually a
// CustomToken.
$marker = ((CustomToken)$RBR).getPayload(Integer.class);
}
-> ^(MATCH patternaction*)
;

patternaction :
 ((pattern=STRING) | (pattern=LETTER))  HostBlockOpen RBR
 -> ^(PATTERNACTION $pattern {TreeStore.getInstance().getTree()});

HostBlockOpen : ( options {greedy=true;} : '->' WS '{')
{

TreeStore store = TreeStore.getInstance();

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

store.storeTree(tree);

}
;

RBR :
 '}'
{
// every token generated bay antlr pass throw our
// tokenCustomizer.customize(Token). Next emitted
// token, this RBR token will be "customized" with
// with input.mark() return value as payload
tokenCustomizer.prepareNextToken(input.mark());
}
;

LBR     : '{';
RPAR 	: ')';
LPAR	: '(';

LETTER     : 'A'..'Z' | 'a'..'z';
DIGIT      : '0'..'9';
STRING     : LETTER (LETTER | DIGIT)*;
WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };

SL_COMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
  { $channel=HIDDEN; } ;
ML_COMMENT : '/*' ( options {greedy=false;} : . )* '*/'
  { $channel=HIDDEN; } ;

// lexer need a rule for every input
// even for chars we don't use
DEFAULT : . { $channel=HIDDEN;};

// the solution below doesn't seems better
// it's just more painful to write...
//DEFAULT : ~('('|')'|'{'|'}'|'\r'|'\n'|'\t'|' '|'A'..'Z'|'a'..'z'|'0'..'9') { $channel=HIDDEN; };

