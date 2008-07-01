/* A grammar for expressions */
grammar Lambda;

@header {
  package freshgom_sharing;
  import freshgom_sharing.lambda.types.*;
  import java.util.ArrayList;
}

@members {
  %include { tweaked_lambda.tom }
}

@lexer::header {
  package freshgom_sharing;
}

toplevel returns [ArrayList<RLTerm> res]
@init { res = new ArrayList<RLTerm>(); }
: x=lterm { $res.add(x); } (SEMI x=lterm { $res.add(x); } )*
;

lterm returns [RLTerm res]
: t=app_lterm { $res=t; }
| LAMBDA ID DOT t=lterm { $res = `RawAbs(Rawlam($ID.text,t)); }
| LET ID EQUALS u=lterm IN t=lterm { $res = `RawLet(Rawletin($ID.text,u,t)); }
| UPID '(' l=ltermlist ')' { $res = `RawConstr($UPID.text,l); }
| UPID { $res = `RawConstr($UPID.text,RawLTList()); }
| MATCH t=lterm WITH r=rules END { $res = `RawCase(t,r); }
;

app_lterm returns [RLTerm res]
: v=aterm { $res=v; } ( v=aterm { $res=`RawApp($res,v); } )*
;

aterm returns [RLTerm res]
: '(' w=lterm ')' { $res = w; }
| ID { $res=`RawVar($ID.text); }
;

ltermlist returns [RawLTermList res]
@init{ res = `RawLTList(); }
: x=lterm? { if(x!=null) $res = `ConsRawLTList(x,$res); } 
  (COMMA y=lterm { $res = `ConsRawLTList(y,$res*); } )*
;

rules returns [RawRules res] 
@init{ res = `RawRList(); }
: CONJ? r1=clause { $res = `ConsRawRList(r1,$res); } 
  (CONJ r2=clause { $res = `ConsRawRList(r2,$res*); } )*
;

clause returns [RawClause res]
: p=pattern DOT t=lterm { $res = `RawRule(p,t); }
;

pattern returns [RawPattern res]
: UPID '(' l=patternlist ')' { $res = `RawPFun($UPID.text,l); }
| UPID { $res = `RawPFun($UPID.text,RawPList()); }
| ID { $res = `RawPVar($ID.text); }
;

patternlist returns [RawPatternList res]
@init{ res = `RawPList(); }
: x=pattern? { if(x!=null) $res = `ConsRawPList(x,$res); } 
  (COMMA y=pattern { $res = `ConsRawPList(y,$res*); } )*
;

COMMENT : '(*' ( options {greedy=false;} : . )* '*)' {$channel=HIDDEN;} ;

DOT : '->';
LAMBDA : 'fun' ;
LET : 'let' ;
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

MATHOP  : '/'
        | '*'
        | '+'
        | '-'
;

WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;




