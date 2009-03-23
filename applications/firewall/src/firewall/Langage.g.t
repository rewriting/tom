/* This grammar parses simple firewall */
grammar Langage;
options {
  output=AST;
  ASTLabelType=Tree;
}

tokens {
  %include { ast/AstTokenList.txt }
}

@header {
  package firewall;
}
@lexer::header {
  package firewall;
}

file :
	(block)* EOF -> ^(Blocks (block)*)
     ;

block :
	CHAIN rule LPAR ID RPAR TARGET PROT OPT SOURCE DESTINATION (instruction)* -> ^(Block  rule ^(InstructionList (instruction)*))
      ;

rule :
	'INPUT'       -> ^(Input)
| 'FORWARD'     -> ^(Forward)
| 'OUTPUT'      -> ^(Output)
| 'PREROUTING'  -> ^(Prerouting)
| 'POSTROUTING' -> ^(Postrouting)
| ID            -> ^(UserRuleDef ID)
;

instruction : 
  target protocol opt communication communication ID -> ^(Ins target protocol opt communication communication ^(Options ID))
;

opt : 
	'--' -> ^(None)
;

protocol : 
  'icmp' -> ^(Icmp)
| 'all' -> ^(All_)
| 'tcp' -> ^(Tcp)
| 'udp' -> ^(Udp)
;

target :
('ACCEPT' -> ^(Accept)
| 'CLASSIFY' -> ^(Classify)
| 'CLUSTERIP' -> ^(ClusterIp)
| 'CONNMARK' -> ^(ConnMark)
| 'CONNSECMARK' -> ^(ConnSecMark)
| 'DNAT' -> ^(Dnat)
| 'DROP' -> ^(Drop)
| 'DSCP' -> ^(Dscp)
| 'ECN' -> ^(Ecn)
| 'LOG' -> ^(Log)
| 'Mark' -> ^(Mark)
| 'MASQUERADE' -> ^(Masquerade)
| 'MIRROR' -> ^(Mirror)
| 'NETMAP' -> ^(NetMap)
| 'NFLOG' -> ^(NfLog)
| 'NFQUEUE' -> ^(NfQueue)
| 'NOTRACK' -> ^(NoTrack)
| 'QUEUE' -> ^(Queue)
| 'REDIRECT' -> ^(Redirect)
| 'REJECT' -> ^(Reject)
| 'RETURN' -> ^(Return)
| 'SAME' -> ^(Same)
| 'SECMARK' -> ^(SecMark)
| 'SNAT' -> ^(Snat)
| 'TCPMSS' -> ^(TcpMss)
| 'TOS' -> ^(Tos)
| 'TRACE' -> ^(Trace)
| 'TTL' -> ^(Ttl)
| 'ULOG' -> ^(Ulog)
| 'IPV40PTSSTRIP' -> ^(Ipv4OptSstRip)
| 'SET' -> ^(Set)
| 'TARPIT' -> ^(Tarpit)  
//| ID -> ^(User_Rule) //TO MODIFY
);


opts : 
'NONE' -> ^(None);

communication : 
	      	'anywhere' -> ^(Anywhere)
	       |'localhost' -> ^(Localhost)
//	       |IP -> ^(Ip_Addr IP)
	      ;

CHAIN : 'Chain' ;
LPAR : '(' ;
RPAR : ')' ;
TARGET : 'target' ;
PROT : 'prot' ;
OPT : 'opt' ;
SOURCE : 'source' ;
DESTINATION : 'destination' ;
//IP : ('a'..'f'|'A'..'F'|'0'..'9'|'.'|':')* ;
INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
