grammar GParser;
options {output=AST;}

tokens {
	LEFTPAR='(';
	RIGHTPAR=')';
	LEFTBR='{';
	RIGHTBR='}';
}

program
	: LEFTPAR subject* RIGHTPAR LEFTBR (statement ';')* RIGHTBR
	;

subject
	:	Type? ' ' var
	;

statement
	:	BLANK* LETTER+ BLANK*
	;

BLANK
	:	'\r' | '\n' | ' '
	;

LETTER
	:	'A'..'Z' | 'a'..'z'|'_'
	;

Type
	:	'int' | 'float'|'truc'
	;
var
	:	LETTER+
	;

