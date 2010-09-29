/* A grammar for expressions */
grammar Lambda;

@header {
  package lazyml;

  import lazyml.lambda.types.*;
  import java.util.Collections;
  import java.util.ArrayList;
}

@members {
  %include { lambda/lambda.tom }

  private RawPatternList append(RawPatternList l, RawPattern p) {
    %match(l) {
      EmptyRawPList() -> { return `ConsRawPList(p,EmptyRawPList()); }
      ConsRawPList(x,xs) -> { return `ConsRawPList(x,append(xs,p)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawRules append(RawRules l, RawClause c) {
    %match(l) {
      EmptyRawRList() -> { return `ConsRawRList(c,EmptyRawRList()); }
      ConsRawRList(x,xs) -> { return `ConsRawRList(x,append(xs,c)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawLTermList append(RawLTermList l, RawLTerm t) {
    %match(l) {
      EmptyRawLTList() -> { return `ConsRawLTList(t,EmptyRawLTList()); }
      ConsRawLTList(x,xs) -> { return `ConsRawLTList(x,append(xs,t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawBVarList append(RawBVarList l, String t) {
    %match(l) {
      EmptyRawBVarList() -> { return `ConsRawBVarList(t,EmptyRawBVarList()); }
      ConsRawBVarList(x,xs) -> { return `ConsRawBVarList(x,append(xs,t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawTyList append(RawTyList l, RawLType t) {
    %match(l) {
      EmptyRawTyList() -> { return `ConsRawTyList(t,EmptyRawTyList()); }
      ConsRawTyList(x,xs) -> { return `ConsRawTyList(x,append(xs,t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawDomain append(RawDomain l, RawLType t) {
    %match(l) {
      EmptyRawDomain() -> { return `ConsRawDomain(t,EmptyRawDomain()); }
      ConsRawDomain(x,xs) -> { return `ConsRawDomain(x,append(xs,t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawContext append(RawContext l, RawJugement t) {
    %match(l) {
      EmptyRawContext() -> { return `ConsRawContext(t,EmptyRawContext()); }
      ConsRawContext(x,xs) -> { return `ConsRawContext(x,append(xs,t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static RawLTerm makeWithArgs(ArrayList<String> args, RawLTerm t) {
    Collections.reverse(args);
    for(String x:args) {
      t = `RawAbs(Rawlam(x,t));
    }
    return t;
  }

  public static RawTyList mapTypeVar(RawBVarList vl) {
    %match(vl) {
      EmptyRawBVarList() -> { return `EmptyRawTyList(); }
      ConsRawBVarList(v,vs) -> { 
        return `ConsRawTyList(RawTypeVar(v),mapTypeVar(vs)); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static RawContext concat(RawContext c1, RawContext c2) {
    return `RawContext(c1*,c2*);
  }
}

@lexer::header {
  package lazyml;
}

args returns [ArrayList<String> res]
@init { res = new ArrayList<String>(); }
: (ID { $res.add($ID.text); } )*
;

lterm returns [RawLTerm res]
: t=app_lterm { $res=t; }
| LAMBDA a=args DOT t=lterm { $res = `makeWithArgs(a,t); }
| LET REC ID a=args EQUALS u=lterm IN t=lterm { 
    $res = `RawLet(Rawletin($ID.text,RawFix(Rawfixpoint($ID.text,makeWithArgs(a,u))),t)); 
  }
| LET ID a=args EQUALS u=lterm IN t=lterm { $res = `RawLet(Rawletin($ID.text,makeWithArgs(a,u),t)); }
| LET UPID '(' l=patternlist ')' EQUALS u=lterm IN t=lterm { $res = `RawCase(u,RawRList(RawRule(RawPFun($UPID.text,l),t))); }
| MATCH t=lterm WITH r=rules END { $res = `RawCase(t,r); }
| IF b=lterm THEN t=lterm ELSE e=lterm END { $res = `RawCase(b,RawRList(
      RawRule(RawPFun("True",RawPList()),t),
      RawRule(RawPFun("False",RawPList()),e))); }

;

app_lterm returns [RawLTerm res]
: v=aterm { $res=v; } ( v=aterm { $res=`RawApp($res,v); } )*
;

aterm returns [RawLTerm res]
: '(' w=lterm ')' { $res = w; }
| ID { $res=`RawVar($ID.text); }
| NUM { 
   try { $res = `RawLit(Integer.parseInt($NUM.text)); }
   catch (NumberFormatException e) { throw new RuntimeException(); } 
  }
| CHAR { $res = `RawChr($CHAR.text.charAt(1)); }
| STRING { $res = `RawStr($STRING.text.substring(1,$STRING.text.length()-1)); }
| UPID '(' l=ltermlist ')' { $res = `RawConstr($UPID.text,l); }
| PRIMID '(' l=ltermlist ')' { $res = `RawPrimFun($PRIMID.text,l); }
;

ltermlist returns [RawLTermList res]
@init{ res = `EmptyRawLTList(); }
: x=lterm? { if(x!=null) $res = append($res,x); } 
  (COMMA y=lterm { $res = append($res,y); } )*
;

rules returns [RawRules res] 
@init{ res = `EmptyRawRList(); }
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
@init{ res = `EmptyRawPList(); }
: x=pattern? { if(x!=null) $res = append($res,x); } 
  (COMMA y=pattern { $res = append($res,y); } )*
;


params returns [RawBVarList res]
@init { res = `EmptyRawBVarList(); }
: '(' a=ID? { if(a!=null) $res = append($res,$a.text); } 
  (COMMA b=ID { $res = append($res,$b.text); } )* ')'
;

type returns [RawLType res]
: p=atomtype { $res = p; } ('->' p=type { $res = `RawArrow($res,p); })?
;

atomtype returns [RawLType res]
: '(' ty=type ')' { $res = ty; } 
| ID { $res = `RawTypeVar($ID.text); }
| UPID a=tyargs?
  { if (a != null)
      $res = `RawTyConstr($UPID.text,a);
    else 
      $res = `RawAtom($UPID.text);
  }
;

tyargs returns [RawTyList res]
@init { res = `EmptyRawTyList(); }
: '(' ty=type { if(ty!=null) $res = append($res,ty); } 
  (COMMA ty=type { $res = append($res,ty); } )* ')'
;

domain returns [RawDomain res]
@init { res = `EmptyRawDomain(); }
: '(' ty=type? { if(ty!=null) $res = append($res,ty); } 
  (COMMA ty=type { $res = append($res,ty); } )* ')'
;

clist returns [ArrayList<RawNameAndDomain> res]
@init { res = new ArrayList<RawNameAndDomain>(); }
: a=UPID c=domain? { 
    if (c==null) c = `EmptyRawDomain();
    $res.add(`RawNameAndDomain($a.text,c)); 
  }
  ('|' b=UPID c=domain? {
     if (c==null) c = `EmptyRawDomain();
     $res.add(`RawNameAndDomain($b.text,c)); 
  })*
;

tydec returns [RawContext res]
@init { res = `EmptyRawContext(); }
: 'type' UPID p=params? EQUALS l=clist 
  { RawLType codom = p == null ? `RawAtom($UPID.text) : `RawTyConstr($UPID.text,mapTypeVar(p));
    RawBVarList vars = p == null ? `EmptyRawBVarList() : p;
    for (RawNameAndDomain dom: l) {
      res = `ConsRawContext(RawRangeOf(
            dom.getname(),
            RawRange(RawRa(vars,dom.getdom(),codom))),res);
    }
  }
;

extern returns [RawJugement res]
: EXTERN codom=type PRIMID dom=domain
  { $res = `RawRangeOf($PRIMID.text,
      RawRange(RawRa(EmptyRawBVarList(),dom,codom))); }
;

tydecs returns [RawContext res]
@init { res = `EmptyRawContext(); }
: ( x=tydec { if (x!=null) $res = `concat(x,$res); } 
  | j=extern { if (j!=null) $res=`append($res,j); } )* ';'
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
IF : 'if';
THEN: 'then';
ELSE: 'else';
EXTERN: 'extern';

NUM : ('0'..'9')+ ;

ID : ('a'..'z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
UPID : ('A'..'Z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
PRIMID : ('@')('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'\.')+ ;

CHAR: '\'' 
      (   EscapeSequence 
      |   ~( '\'' | '\\' | '\r' | '\n' )
      ) 
      '\''
    ; 

STRING: '"' 
        (   EscapeSequence
        |   ~( '\\' | '"' | '\r' | '\n' )        
        )* 
        '"' 
    ;

EscapeSequence: 
  '\\' ('b' 
       | 't' 
       | 'n' 
       | 'f' 
       | 'r' 
       | '\"' 
       | '\'' 
       | '\\' 
       | ('0'..'3') ('0'..'7') ('0'..'7')
       | ('0'..'7') ('0'..'7') 
       | ('0'..'7')
       )          
;     

COMMA : ',';
SEMI  : ';' ;

EQUALS  : '=' ;

MATHOP  : '/'
        | '*'
        | '+'
        | '-'
;

WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;




