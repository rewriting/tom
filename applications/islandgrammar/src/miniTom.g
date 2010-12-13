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
	:	LEFTPAR RIGHTPAR LEFTBR code ';' ? RIGHTBR {System.out.println("C'est gaaaaagné");}
	;

code
	:	(statement ';')* -> ^(CODE statement*)
	;

statement
	:	'a'+
	;

BLANK
	:	('\r' | '\n' | '\r\n'  | '\t' | ' ' ) {$channel=HIDDEN;}
	;

