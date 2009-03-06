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

instruction :
    ('ACCEPT' -> ^(Accept)
    |'CLASSIFY' -> ^(Classify)
    |'CLUSTERIP' -> ^(ClusterIp)
    |'CONNMARK' -> ^(ConnMark)
    |'CONNSECMARK' -> ^(ConnSecMark)
    |'DNAT' -> ^(Dnat)
    |'DROP' -> ^(Drop)
    |'DSCP' -> ^(Dscp)
    |'ECN' -> ^(Ecn)
    |'LOG' -> ^(Log)
    |'Mark' -> ^(Mark)
    |'MASQUERADE' -> ^(Masquerade)
    |'MIRROR' -> ^(Mirror)
    |'NETMAP' -> ^(NetMap)
    |'NFLOG' -> ^(NfLog)
    |'NFQUEUE' -> ^(NfQueue)
    |'NOTRACK' -> ^(NoTrack)
    |'QUEUE' -> ^(Queue)
    |'REDIRECT' -> ^(Redirect)
    |'REJECT' -> ^(Reject)
    |'RETURN' -> ^(Return)
    |'USER_RULE'	-> ^(User_Rule) //TO MODIFY
    |'SAME' -> ^(Same)
    |'SECMARK' -> ^(SecMark)
    |'SNAT' -> ^(Snat)
    |'TCPMSS' -> ^(TcpMss)
    |'TOS' -> ^(Tos)
    |'TRACE' -> ^(Trace)
    |'TTL' -> ^(Ttl)
    |'ULOG' -> ^(Ulog)
    |'IPV40PTSSTRIP' -> ^(Ipv4OptSstRip)
    |'SET' -> ^(Set)
    |'TARPIT' -> ^(Tarpit))	
  ;

INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
