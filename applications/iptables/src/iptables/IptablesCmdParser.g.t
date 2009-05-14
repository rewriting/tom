grammar IptablesCmdParser;
options {
  output=AST;
  ASTLabelType=Tree;
}

tokens {
	%include { iptables/ast/AstTokenList.txt }
}

@header {
	package iptables;
}
@lexer::header {
	package iptables;
}

file : (rule)* EOF;

rule: CMD_IPTABLES command;

command:
	CMD_APPEND target (opts)* OPT_ACTION action
	| CMD_POLICY target action
	;

action : 
	'ACCEPT'
	| 'DROP'
	| 'REJECT'
	| 'LOG'
	;

target :
	'INPUT'
	| 'OUTPUT'
	| 'FORWARD'
	;

opts:
	protoopt
	| ifaceopt
	| addressopt
	| portopt
	| statesopt
	;

protoopt: OPT_PROTO proto;
proto : 
	'all'
	| 'tcp'
	| 'udp'
	| 'ip4'
	| 'ip6'
	| 'icmp'
	| 'eth'
	;

ifaceopt:
	OPT_IFACE_IN STRING
	| OPT_IFACE_OUT STRING
	;

addressopt:
	OPT_ADDR_SRC address
	| OPT_ADDR_DEST address
	;
address	: 'anywhere'
	| 'localhost'
	| IPV4DOTDEC
	| IPV4CIDR
	| IPV6HEX
	| IPV6CIDR
	;

portopt:
	OPT_PORT_SRC INT
	| OPT_PORT_DEST INT
	;
	
statesopt : '-m state' OPT_STATE states;
states:	state stateIter*;
stateIter : ',' state;
state : 'NEW'
	| 'RELATED'
	| 'ESTABLISHED'
	| 'INVALID'
	;


OPTSHORT: '-';
OPTLONG: '--';

CMD_IPTABLES: 'iptables';
CMD_APPEND: (OPTSHORT 'A' | OPTLONG 'append');
CMD_POLICY: (OPTSHORT 'P' | OPTLONG 'policy');

OPT_ACTION: (OPTSHORT 'j' | OPTLONG 'jump');
OPT_IFACE_IN: (OPTSHORT 'i' | OPTLONG 'in-interface');
OPT_IFACE_OUT: (OPTSHORT 'o' | OPTLONG 'out-interface');
OPT_PROTO: (OPTSHORT 'p' | OPTLONG 'protocol');
OPT_ADDR_SRC: (OPTSHORT 's' | OPTLONG 'source');
OPT_ADDR_DEST: (OPTSHORT 'd' | OPTLONG 'destination');
OPT_PORT_SRC: (OPTLONG 'sport' | OPTLONG 'source-port');
OPT_PORT_DEST: (OPTLONG 'dport' | OPTLONG 'destination-port');
OPT_STATE: (OPTLONG 'state');


INT : ('0'..'9')+ ;
INTDOT		: (INT '.');
INTSTARDOT	: ((INT|'*') '.');
IPV4DOTDEC	: (b+=INTSTARDOT)+ { $b.size() <= 3 }? (INT|'*');
IPV4CIDR	: (b+=INTDOT)+ { $b.size() <= 3 }? INT '/' INT;
HEX2COLON	: (('0'..'9') ('0'..'9') ('0'..'9') ('0'..'9') ':');
IPV6HEX		: (b+=HEX2COLON)+ { $b.size() <= 7 }? (('0'..'9') ('0'..'9') ('0'..'9') ('0'..'9'));
IPV6CIDR 	: IPV6HEX '/' INT;

ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
STRING : (ESC|~('"'|'\\'|'\n'|'\r'))*;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;

