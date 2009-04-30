/* This grammar parses simple firewall */
grammar Langage;
options {
  output=AST;
  ASTLabelType=Tree;
}

tokens {
	%include { iptables/analyser/AnalyserTokenList.txt }
	%include { iptables/analyserwrapper/AnalyserWrapperTokenList.txt }
	%include { iptables/iptables/IptablesTokenList.txt }
}

@header {
	package iptables;
}
@lexer::header {
	package iptables;
}

file :
	(block)* EOF -> ^(IptablesBlocks (block)*)
     ;

block:	'Chain' target '(policy' action ')'
	'target' 'prot' 'opt' 'source' 'destination'
	(rule)* -> ^(
		IptablesBlock target action IptablesRules (rule)*
	);

rule:	action proto oopt address address opts -> ^(
		IptablesRule action proto address address opts
	);

action : 
	'ACCEPT'	-> ^(Accept)
	| 'DROP'	-> ^(Drop)
	| 'REJECT'	-> ^(Reject)
	| 'LOG'		-> ^(Log)
	;

proto : 
	'all' 	-> ^(ProtoAny)
	| 'tcp' -> ^(TCP)
	| 'udp' -> ^(UDP)
	| 'ip4' -> ^(IPv4)
	| 'ip6' -> ^(IPv6)
	| 'icmp'-> ^(ICMP)
	| 'eth' -> ^(Ethernet)
	;

target :
	'INPUT'		-> ^(In)
	| 'OUTPUT'	-> ^(Out)
	| 'FORWARD'	-> ^(Forward)
	;

address	: 'anywhere' 	-> ^(AddrAny)
	| IPV4DOTDEC 	-> ^(AddrStringDotDecimal4 IPV4DOTDEC)
	| IPV4CIDR 	-> ^(AddrStringCIDR4 IPV4CIDR)
	| IPV6HEX	-> ^(AddrStringHexadecimal6 IPV6HEX)
	| IPV6CIDR	-> ^(AddrStringCIDR6 IPV6CIDR)
	;

oopt: 	'--';

port:	'dpt:INT'	-> ^(DestPort INT)
	| 'spt:INT'	-> ^(SourcePort INT)
	;

state : 'NEW'		-> ^(New)
	| 'RELATED'	-> ^(Related)
	| 'ESTABLISHED'	-> ^(Established)
	| 'INVALID'	-> ^(Invalid)
	;

states:	state stateIter* -> ^(States state stateIter*);

stateIter : ',' state -> ^(state);

opts:	opt* -> ^(IptablesOptions opt*);

opt:	proto port		-> ^(port)
	| 'state' states	-> ^(IptablesStates states)
	| STRING		-> ^(UnknownOption STRING)
	;

IPV4DOTDEC	: (INT|'*') '.' (INT|'*') '.' (INT|'*') '.' (INT|'*');
IPV4CIDR	: (INT '.')? (INT '.')? (INT '.')? INT '/' INT;
IPV6HEX		: (HEX8 HEX8 ':') (HEX8 HEX8 ':') (HEX8 HEX8 ':')
		(HEX8 HEX8 ':') (HEX8 HEX8 ':') (HEX8 HEX8 ':') (HEX8 HEX8 ':')
		(HEX8 HEX8);
IPV6CIDR	: IPV6HEX '/' INT;

HEX8: ('0'..'9') ('0'..'9') ;
INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
