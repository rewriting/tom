/* A grammar for expressions */
grammar Lambda;

@header {
  import lambda.types.*;
  import java.util.ArrayList;
}

@members {
  %include { tweaked_Lambda.tom }

  private RawPatternList append(RawPatternList l, RawPattern p) {
    %match(l) {
      RawEmptyPList() -> { return `RawConsPList(p,RawEmptyPList()); }
      RawConsPList(x,xs) -> { return `RawConsPList(x,append(xs,p)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawRules append(RawRules l, RawClause c) {
    %match(l) {
      RawEmptyRList() -> { return `RawConsRList(c,RawEmptyRList()); }
      RawConsRList(x,xs) -> { return `RawConsRList(x,append(xs,c)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawLTermList append(RawLTermList l, RawLTerm t) {
    %match(l) {
      RawEmptyLTList() -> { return `RawConsLTList(t,RawEmptyLTList()); }
      RawConsLTList(x,xs) -> { return `RawConsLTList(x,append(xs,t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawLTerm convertInt(int i) {
    if (i==0) return `RawConstr("O",RawEmptyLTList());
    else return `RawConstr("S",
        RawConsLTList(convertInt(i-1),RawEmptyLTList()));
  }
}

@lexer::header {
}

toplevel returns [ArrayList<RawLTerm> res]
@init { res = new ArrayList<RawLTerm>(); }
: x=lterm { $res.add(x); } (SEMI x=lterm { $res.add(x); } )*
;

lterm returns [RawLTerm res]
: t=app_lterm { $res=t; }
| LAMBDA ID DOT t=lterm { $res = `RawAbs(Rawlam($ID.text,t)); }
| LET ID EQUALS u=lterm IN t=lterm { $res = `RawLet(Rawletin($ID.text,u,t)); }
| MATCH t=lterm WITH r=rules END { $res = `RawCase(t,r); }
;

app_lterm returns [RawLTerm res]
: v=aterm { $res=v; } ( v=aterm { $res=`RawApp($res,v); } )*
;

aterm returns [RawLTerm res]
: '(' w=lterm ')' { $res = w; }
| ID { $res=`RawVar($ID.text); }
| NUM { try { $res = convertInt(Integer.parseInt($NUM.text)); }
        catch (NumberFormatException e) { throw new RuntimeException(); } 
      }
| UPID '(' l=ltermlist ')' { $res = `RawConstr($UPID.text,l); }
;

ltermlist returns [RawLTermList res]
@init{ res = `RawEmptyLTList(); }
: x=lterm? { if(x!=null) $res = append($res,x); } 
  (COMMA y=lterm { $res = append($res,y); } )*
;

rules returns [RawRules res] 
@init{ res = `RawEmptyRList(); }
: CONJ? r1=clause { $res = append($res,r1); } 
  (CONJ r2=clause { $res = append($res,r2); } )*
;

clause returns [RawClause res]
: p=pattern DOT t=lterm { $res = `RawRule(p,t); }
;

pattern returns [RawPattern res]
: UPID '(' l=patternlist ')' { $res = `RawPFun($UPID.text,l); }
| ID { $res = `RawPVar($ID.text); }
;

patternlist returns [RawPatternList res]
@init{ res = `RawEmptyPList(); }
: x=pattern? { if(x!=null) $res = append($res,x); } 
  (COMMA y=pattern { $res = append($res,y); } )*
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




