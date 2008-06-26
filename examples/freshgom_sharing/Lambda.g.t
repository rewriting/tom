/* A grammar for expressions */
grammar Lambda;

@header {
  package freshgom_sharing;
  import freshgom_sharing.lambda.types.*;
}

@members {
  %include { tweaked_lambda.tom }
}

@lexer::header {
  package freshgom_sharing;
}

lterm returns [RLTerm res]
: t=app_lterm { $res=t; }
| LAMBDA ID DOT t=lterm { $res = `RawAbs($ID.text,t); }
| LET ID EQUALS u=lterm IN t=lterm { $res = `RawLet($ID.text,u,t); }
;

app_lterm returns [RLTerm res]
: v=aterm { $res=v; } ( v=aterm { $res=`RawApp($res,v); } )*
;

aterm returns [RLTerm res]
: '(' w=lterm ')' { $res = w; }
| ID { $res=`RawVar($ID.text); }
;


DOT : '->';
LAMBDA : 'fun' ;
LET : 'let' ;
IN : 'in' ;


ID : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;

NUM : ('0'..'9')+ ;

SEMI  : ';' ;

EQUALS  : '=' ;

MATHOP  : '/'
        | '*'
        | '+'
        | '-'
;

WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } 
;


