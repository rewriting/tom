/* A grammar for expressions */
grammar Lambda;

@header {
  package termgraph;

  import termgraph.freshlambda.types.*;
  import java.util.ArrayList;
}

@members { %include { freshlambda/freshlambda.tom } }

@lexer::header { 
  package termgraph;
}

toplevel returns [ArrayList<LTerm> res]
@init { res = new ArrayList<LTerm>(); }
: x=lterm { $res.add(x); } (SEMI x=lterm { $res.add(x); } )*
;

lterm returns [LTerm res]
: t=app_lterm { $res=t; }
| LET ID EQUALS u=lterm IN t=lterm { $res = `Let(Name($ID.text),u,t); }
;

app_lterm returns [LTerm res]
: v=aterm { $res=v; } ( PLUS v=aterm { $res=`Plus($res,v); } )*
;

aterm returns [LTerm res]
: '(' w=lterm ')' { $res = w; }
| ID { $res=`Var(Name($ID.text)); }
| NUM { $res=`Num(Val(Integer.parseInt($NUM.text))); }
;

COMMENT : '(*' ( options {greedy=false;} : . )* '*)' {$channel=HIDDEN;} ;

DOT : '->';
LAMBDA : 'fun' ;
LET : 'let' ;
REC : 'rec' ;
IN : 'in' ;
MATCH : 'match' ;
WITH : 'with' ;
END : 'end' ;
CONJ : '|' ;

ID : ('a'..'z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
UPID : ('A'..'Z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;

NUM : ('0'..'9')+ ;

COMMA : ',';
SEMI  : ';' ;

EQUALS  : '=' ;

PLUS : '+' ;

WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

