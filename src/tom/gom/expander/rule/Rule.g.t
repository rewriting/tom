/* This grammar parses simple rules */
grammar Rule;
options {
  output=AST;
}

@header {
  package tom.gom.expander.rule;
}
@lexer::header {
  package tom.gom.expander.rule;
}
ruleset :	(rule)* EOF ;
rule	:	pattern ARROW term (IF term)?  ;
pattern	:	ID LPAR (term (COMA term)*)? RPAR ;
term	:	pattern | ID | builtin;
builtin	:	integer | STRING ;
integer	:	DIGIT+ ;

ARROW	:	'->' ;
LPAR	:	'(' ;
RPAR	:	')' ;
COMA	:	',' ;
IF	:	'if' ;
DIGIT	:	('0'..'9') ;
ESC	:	'\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING	:	'"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID	: ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
WS	:	(' '|'\t'|'\n')+ { $channel=HIDDEN; } ;
