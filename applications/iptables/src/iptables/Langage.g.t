/* This grammar parses simple firewall */
grammar Langage;
options {
  output=AST;
  ASTLabelType=Tree;
}

tokens {
  %include { iptables/analyser/AnalyserTokenList.txt }
}

@header {
  package iptables;
}
@lexer::header {
  package iptables;
}

file :
	ACCEPT EOF -> ^(Accept)
     ;

ACCEPT : 'Accept' ;
INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
