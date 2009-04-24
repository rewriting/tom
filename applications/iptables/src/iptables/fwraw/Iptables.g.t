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
	(block)* EOF -> ^(IptablesBlocks (block)*)
     ;

block : 'Chain' target '(policy' action ')' 'target' 'prot' 'opt' 'source' 'destination' (rule)* -> ^(IptablesBlock target action IptablesRules (rule)*);
rule : action proto opt address address opts -> ^(IptableRule action proto address address opts);

action : 
	'ACCEPT' -> ^(Accept)
	| 'DROP' -> ^(Drop)
	| 'REJECT' -> ^(Reject)
	| 'LOG' -> ^(Log)
	;

proto : 
	'all' -> ^(ProtoAny)
	| 'tcp' -> ^(TCP)
	| 'udp' -> ^(UDP)
	| 'ip4' -> ^(IPv4)
	| 'ip6' -> ^(IPv6)
	| 'icmp' -> ^(ICMP)
	| 'eth' -> ^(Ethernet)
	;

target :
	'INPUT' -> ^(In)
	| 'OUTPUT' -> ^(Out)
	| 'FORWARD' -> ^(Forward)
	;

address : STRING -> ^(AddrAny);

opt : '--' -> ^(NoOpt);

opts : STRING -> ^(NoOpt);

INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
