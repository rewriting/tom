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
  RARROW_BR;
  
  StoredTree;
}

@parser::header {
package newparser;

import streamanalysis.*;
import newparser.*;
import org.antlr.runtime.tree.Tree;
}

@lexer::header {
package newparser;

import streamanalysis.*;
import newparser.*;
import org.antlr.runtime.tree.Tree;
}

/* parser rules */
matchconstruct : LPAR RPAR LBR patternaction* RBR -> ^(MATCH patternaction*) ;

patternaction :  (s=STRING) HostBlockOpen  RBR {System.out.println("Joie récursive !");}-> ^(PATTERNACTION $s {TreeStore.getInstance().getTree()});  // pick stored tree and insert

HostBlockOpen : '->' ( options {greedy=true;} : WS) '{'
{
TreeStore store = TreeStore.getInstance();

HostParser parser = new HostParser(new NegativeImbricationDetector('{', '}', 0));
Tree tree = parser.parse(input);

store.storeTree(tree);

//XXX
System.out.println("Joie !");
}
;

//HostBlockClose : '}';

//RARROW 	: '->';
RBR	: '}';
LBR                : '{';
RPAR 	: ')';
LPAR	: '(';

STOREDTREE : ;

LETTER     : 'A'..'Z' | 'a'..'z';
DIGIT      : '0'..'9';
STRING     : LETTER (LETTER | DIGIT)*;
WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };


