/* A grammar for expressions */
grammar Lambda;

@header {
  import lambda.types.*;
  import java.util.ArrayList;
}

@members {
  %include { lambda/Lambda.tom }
}

@lexer::header {
}

toplevel returns [ArrayList<RawLTerm> res]
@init { res = new ArrayList<RawLTerm>(); }
: x=lterm { $res.add(x); } (DOUBLESEMI x=lterm { $res.add(x); } )*
;

lterm returns [RawLTerm res]
: t=app_lterm { $res=t; }
| LAMBDA ID DOT t=lterm { $res = `RawAbs(Rawlam($ID.text,t)); }
| LET ID EQUALS u=lterm IN t=lterm { $res = `RawLet(Rawletin($ID.text,u,t)); }
| LET REC ID EQUALS u=lterm IN t=lterm { $res = `RawLet(Rawletin($ID.text,RawFix(Rawfixpoint($ID.text,u)),t)); }
| IF b=lterm THEN u=lterm ELSE v=lterm END { $res = `RawBranch(b,u,v); }
;

app_lterm returns [RawLTerm res]
: CALLCC u=aterm { $res=`RawCallCC(u); }
| THROW u=aterm v=aterm { $res=`RawThrow(u,v); }
| v=aterm { $res=v; } ( v=aterm { $res=`RawApp($res,v); } )*
;

aterm returns [RawLTerm res]
: '(' w=ltermseq ')' { $res = w; }
| ID { $res=`RawVar($ID.text); }
| UNIT { $res = `RawUnit(); }
| NUM { 
   try { $res = `RawInteger(Integer.parseInt($NUM.text)); }
   catch (NumberFormatException e) { throw new RuntimeException(); } 
  }
| TRUE  { $res = `RawTrue(); }
| FALSE  { $res = `RawFalse(); }
| EQ  { $res = `RawAbs(Rawlam("x",RawAbs(Rawlam("y",RawEq(RawVar("x"),RawVar("y")))))); }
| PLUS  { $res = `RawAbs(Rawlam("x",RawAbs(Rawlam("y",RawPlus(RawVar("x"),RawVar("y")))))); }
| MINUS  { $res = `RawAbs(Rawlam("x",RawAbs(Rawlam("y",RawMinus(RawVar("x"),RawVar("y")))))); }
| TIMES  { $res = `RawAbs(Rawlam("x",RawAbs(Rawlam("y",RawTimes(RawVar("x"),RawVar("y")))))); }
| PRINT { $res = `RawAbs(Rawlam("x",RawPrint(RawVar("x")))); }
| GT  { $res = `RawAbs(Rawlam("x",RawAbs(Rawlam("y",RawGT(RawVar("x"),RawVar("y")))))); }
| LT  { $res = `RawAbs(Rawlam("x",RawAbs(Rawlam("y",RawLT(RawVar("x"),RawVar("y")))))); }
;

ltermseq returns [RawLTerm res]
: t=lterm { $res=t; } (SEMI t=lterm { $res=`RawApp(RawAbs(Rawlam("_",t)),$res); })* 
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
IF : 'if' ;
THEN : 'then';
ELSE : 'else';
TRUE : 'true';
FALSE : 'false';
EQ : 'eq';
PLUS : 'plus';
MINUS : 'minus';
TIMES : 'times';
CALLCC : 'callcc';
THROW : 'throw';
PRINT : 'print';
UNIT : '()';
GT : 'gt';
LT : 'lt';

ID : ('a'..'z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
UPID : ('A'..'Z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;

NUM : ('0'..'9')+ ;

COMMA : ',';
SEMI  : ';' ;
DOUBLESEMI : ';;';

EQUALS  : '=' ;

MATHOP  : '/'
        | '*'
        | '+'
        | '-'
;

WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;




