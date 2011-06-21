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

import streamanalysis.*;
import newparser.*;
import org.antlr.runtime.tree.Tree;
}

@lexer::header {
package newparser;

import debug.*;

import streamanalysis.*;
import newparser.*;
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
}

/* parser rules */
matchconstruct
returns [int closingBracketLine, int closingBracketPosInLine]
:LPAR RPAR LBR patternaction* RBR
{$closingBracketLine = $RBR.line; $closingBracketPosInLine = $RBR.pos;}
-> ^(MATCH patternaction*) 
;

patternaction :  (pattern=STRING)  HostBlockOpen RBR
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
{
// we want to rewind to a point where
// input.LA(1) is a '}'
// we need to perform mark BEFORE
// match('}') is called, because match
// call consume on success.

MarkStore.getInstance().storeMark(input.mark());
System.out.println("ONE MORE MARK !");
}
 '}'
;

LBR                : '{';
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