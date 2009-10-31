grammar IptablesCmdParser;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=IptablesCmdParserAstTokens;
}

@header {
	package iptables;
}
@lexer::header {
	package iptables;
}

file : (rule)* EOF -> ^(FirewallRulesIptablesCmd ^(IptablesCmdRules (rule)*));

rule: CMD_IPTABLES command -> ^(command);

command:
	CMD_APPEND target (opts)* OPT_ACTION action { String str = $command.text; }
		-> ^(IptablesCmdAppend target (opts)* action STRING[str])
	| CMD_POLICY target action { String str = $command.text; }
		-> ^(IptablesCmdPolicy target action STRING[str])
	;

action :
	'ACCEPT'	-> ^(Accept)
	| 'DROP'	-> ^(Drop)
	| 'REJECT'	-> ^(Reject)
	| 'LOG'		-> ^(Log)
	;

target :
   'INPUT'		-> ^(In)
 | 'OUTPUT'	-> ^(Out)
 | 'FORWARD'	-> ^(Forward)
	;

opts:
	protoopt	-> ^(protoopt)
	| ifaceopt	-> ^(ifaceopt)
	| addressopt	-> ^(addressopt)
	| portopt	-> ^(portopt)
	| statesopt	-> ^(statesopt)
	;

protoopt: OPT_PROTO proto -> ^(proto);
proto :
	'all'	-> ^(ProtoAny)
	| 'tcp' -> ^(TCP)
	| 'udp' -> ^(UDP)
	| 'ip4' -> ^(IPv4)
	| 'ip6' -> ^(IPv6)
	| 'icmp'-> ^(ICMP)
	| 'eth' -> ^(Ethernet)
	;

ifaceopt:
	OPT_IFACE_IN STRING	-> ^(IptablesCmdIfaceOpt ^(Iface STRING))
	| OPT_IFACE_OUT STRING	-> ^(IptablesCmdIfaceOpt ^(Iface STRING))
	;

addressopt:
	OPT_ADDR_SRC address	-> ^(IptablesCmdAddressSrc address)
	| OPT_ADDR_DEST address -> ^(IptablesCmdAddressDest address)
	;

address	: 'anywhere' 	-> ^(AddrAnyRaw)
	| 'localhost'	-> ^(AddrStringDotDecimal4 'localhost')
	| IPV4DOTDEC 	-> ^(AddrStringDotDecimal4 IPV4DOTDEC)
	| IPV4CIDR 	-> ^(AddrStringCIDR4 IPV4CIDR)
	| IPV6HEX	-> ^(AddrStringHexadecimal6 IPV6HEX)
	| IPV6CIDR	-> ^(AddrStringCIDR6 IPV6CIDR)
	;

portopt:
	OPT_PORT_SRC INT	-> ^(IptablesCmdPortSrc INT)
	| OPT_PORT_DEST INT	-> ^(IptablesCmdPortDest INT)
	;

statesopt: '-m state' OPT_STATE state stateIter* -> ^(States state stateIter*);
stateIter : ',' state -> ^(state);
state : 'NEW'		-> ^(New)
	| 'RELATED'	-> ^(Related)
	| 'ESTABLISHED'	-> ^(Established)
	| 'INVALID'	-> ^(Invalid)
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
