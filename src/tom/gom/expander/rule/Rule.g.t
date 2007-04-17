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
ruleset	:	(rule)* ;
rule	:	pattern ARROW term (IF term)?;
pattern	:	ID LPAR (term (COMMA term)*)? RPAR ;
term	:	pattern | ID | builtin;
builtin	:	integer | STRING ;

integer	:	DIGIT+ ;

ARROW	:	'->' ;
LPAR	:	'(' ;
RPAR	:	')' ;
COMMA	:	',' ;
IF	:	'if' ;
LETTER	:	('a'..'z' | 'A'..'Z') ;
DIGIT	:	('0'..'9') ;
ESC	:	'\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING	:	'"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID	:
	('_')? LETTER
       	(
            options {greedy = true; }:
            ( LETTER | DIGIT | '_' )
        )*
    ;
WS	:	(' '|'\t'|'\n')+ { $channel=99; } ;
