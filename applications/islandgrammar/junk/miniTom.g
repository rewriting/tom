grammar miniTom;
options {
	output=AST;
	ASTLabelType=CommonTree;
}

tokens {
	LEFTPAR='(';
	RIGHTPAR=')';
	LEFTBR='{';
	RIGHTBR='}';
	CODE;
	A='alice';
	B='bob';
}

program
	:	LEFTPAR RIGHTPAR LEFTBR code RIGHTBR 
	;

code
	:	(s=statement {System.out.println($s.text);} ';') -> ^(CODE $s)
	;

statement
	:	'a'+
	;

BLANK
	:	('\r' | '\n' | '\r\n'  | '\t' | ' ' ) {$channel=HIDDEN;}
	;

