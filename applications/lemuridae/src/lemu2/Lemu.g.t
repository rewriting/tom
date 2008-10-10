
grammar Lemu;

@header {
  package lemu2;
  import lemu2.proofterms.types.*;
}

@members {
  %include { proofterms/proofterms.tom }

  private RawTermList append(RawTermList l, RawTerm t) {
    %match(l) {
      EmptyRawtermList() -> { return `ConsRawtermList(t,EmptyRawtermList()); }
      ConsRawtermList(x,xs) -> { return `ConsRawtermList(x,append(xs,t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawPatternList append(RawPatternList l, RawPattern t) {
    %match(l) {
      EmptyRawpatternList() -> { return `ConsRawpatternList(t,EmptyRawpatternList()); }
      ConsRawpatternList(x,xs) -> { return `ConsRawpatternList(x,append(xs,t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private RawTermRewriteRules append(RawTermRewriteRules l, RawTermRewriteRule t) {
    %match(l) {
      EmptyRawtermrrules() -> { return `ConsRawtermrrules(t,EmptyRawtermrrules()); }
      ConsRawtermrrules(x,xs) -> { return `ConsRawtermrrules(x,append(xs,t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }


}

@lexer::header {
  package lemu2;
}

/* terms and patterns */

term_list returns [RawTermList res]
: t=term { $res=`RawtermList(t); } (COMMA t=term { $res = append($res,t); })* 
;

term returns [RawTerm res]
: LPAR t=term RPAR { $res = t; } 
| f=funappl { $res = f; }
| ID { $res = `Rawvar($ID.text); }
;

funappl returns [RawTerm res] 
: ID LPAR l=term_list RPAR { $res = `RawfunApp($ID.text,l); } 
| ID LPAR RPAR { $res = `RawfunApp($ID.text,RawtermList()); }
;

pattern_list returns [RawPatternList res]
: p=pattern { $res=`RawpatternList(p); } (COMMA p=pattern { $res = append($res,p); })* 
;

pattern returns [RawPattern res]
: LPAR p=pattern RPAR { $res = p; } 
| f=pfunappl { $res = f; }
| ID { $res = `Rawpvar($ID.text); }
;

pfunappl returns [RawPattern res] 
: ID LPAR l=pattern_list RPAR { $res = `RawpfunApp($ID.text,l); } 
| ID LPAR RPAR { $res = `RawpfunApp($ID.text,RawpatternList()); }
;

/* propositions */

prop returns [RawProp res]
: p=orprop { $res = p; } (IMPL p=orprop { $res = `Rawimplies($res,p); })*;

orprop returns [RawProp res]
: p=andprop { $res = p; } (OR p=andprop { $res = `Rawor($res,p); })*;

andprop returns [RawProp res]
: p=forallprop { $res = p; } (AND p=forallprop { $res = `Rawand($res,p); })*;

forallprop returns [RawProp res]
: FORALL ID COMMA p=atom { $res = `Rawforall(RawFa($ID.text,p)); }
| EXISTS ID COMMA p=atom { $res = `Rawexists(RawEx($ID.text,p)); }
| p=atom { $res = p; }
;

atom returns [RawProp res]
: LPAR p=prop RPAR { $res = p; }
| p=appl { $res = p; } 
| BOTTOM { $res = `Rawbottom(); }
| TOP { $res = `Rawtop(); } 
;

appl returns [RawProp res]
: UID LPAR l=term_list RPAR { $res = `RawrelApp($UID.text,l); }
| UID { $res = `RawrelApp($UID.text,RawtermList()); }
;

/* rewrite rules */

termrrule returns [RawTermRewriteRule res]
: lhs=pattern ARROW rhs=term { $res = `Rawtermrrule(Rawtrule(lhs,rhs)); }
;

termrrules returns [RawTermRewriteRules res]
@init{
  $res = `Rawtermrrules();
}
: (r=termrrule { $res = append($res,r); } DOT)* 
;

/* proofterms */

proofterm returns [RawProofTerm res]
: ROOTR '(' '<' a=ID ':' pa=prop '>' m=proofterm ')' 
  { $res = `RawrootR(RawRootRPrem1($a.text,pa,m)); }
| AX '(' x=ID ',' a=ID ')' 
  { $res = `Rawax($x.text,$a.text); }
| CUT '(' '<' a=ID ':' pa=prop '>' m=proofterm ',' 
          '<' x=ID ':' px=prop '>' n=proofterm ')' 
  { $res = `Rawcut(RawCutPrem1($a.text,pa,m),RawCutPrem2($x.text,px,n)); }
| IMPLYR '(' '<' x=ID ':' px=prop '>' '<' a=ID ':' pa=prop '>' m=proofterm ',' cn=ID')'
  { $res = `RawimplyR(RawImplyRPrem1($x.text,px,$a.text,pa,m),$cn.text); }
| IMPLYL '(' '<' x=ID ':' px=prop '>' m=proofterm ',' 
             '<' a=ID ':' pa=prop '>' n=proofterm ',' y=ID ')' 
  { $res = `RawimplyL(RawImplyLPrem1($x.text,px,m),RawImplyLPrem2($a.text,pa,n),$y.text); }
| ANDR '(' '<' a=ID ':' pa=prop '>' m=proofterm ','
           '<' b=ID ':' pb=prop '>' n=proofterm ',' c=ID ')'
  { $res = `RawandR(RawAndRPrem1($a.text,pa,m),RawAndRPrem2($b.text,pb,n),$c.text); }
| ANDL '(' '<' x=ID ':' px=prop '>' '<' y=ID ':' py=prop '>' m=proofterm ',' z=ID ')'
  { $res = `RawandL(RawAndLPrem1($x.text,px,$y.text,py,m),$z.text); }
| ORR '(' '<' a=ID ':' pa=prop '>' '<' b=ID ':' pb=prop '>' m=proofterm ',' c=ID ')'
  { $res = `RaworR(RawOrRPrem1($a.text,pa,$b.text,pb,m),$c.text); }
| ORL '(' '<' x=ID ':' px=prop '>' m=proofterm ','
          '<' y=ID ':' py=prop '>' n=proofterm ',' z=ID ')'
  { $res = `RaworL(RawOrLPrem1($x.text,px,m),RawOrLPrem2($y.text,py,n),$z.text); }
| EXISTSR '(' '<' a=ID ':' pa=prop '>' m=proofterm ',' t=term ',' b=ID ')'
  { $res = `RawexistsR(RawExistsRPrem1($a.text,pa,m),t,$b.text); }
| EXISTSL '(' '<' x=ID ':' px=prop '>' '<' fx=ID '>' m=proofterm ',' y=ID ')'
  { $res = `RawexistsL(RawExistsLPrem1($x.text,px,$fx.text,m),$y.text); }
| FORALLR '(' '<' a=ID ':' pa=prop '>' '<' fx=ID '>' m=proofterm ',' b=ID ')'
  { $res = `RawforallR(RawForallRPrem1($a.text,pa,$fx.text,m),$b.text); }
| FORALLL '(' '<' x=ID ':' px=prop '>' m=proofterm ',' t=term ',' y=ID ')'
  { $res = `RawforallL(RawForallLPrem1($x.text,px,m),t,$y.text); }
;

/* declarations */
termmodulo returns [RawTermRewriteRules res] 
: TERM MODULO LBRACE l=termrrules RBRACE { $res=l; }
;

toplevel : proofterm EOF;


COMMENT : '(*' ( options {greedy=false;} : . )* '*)' {$channel=HIDDEN;} ;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

/* colons, brackets, braces and parentheses */
DOT : '.';
LPAR : '(' ;
RPAR : ')' ;
LBR : '<';
RBR : '>';
COLON : ':';
LBRACE : '{';
RBRACE : '}';

/* other symbols */
ARROW : '->';

/* logical connectors */
FORALL  : 'forall';
EXISTS : 'exists';
BOTTOM : 'False';
TOP : 'True';
IMPL : '=>';
OR : '\\/' ;
AND :  '/\\';
COMMA : ',';

/* keywords */
TERM : 'term';
PROP : 'prop';
MODULO : 'modulo';

/* proofterms */
AX : 'ax';
CUT : 'cut';
FALSEL : 'falseL';
TRUER : 'trueR';
ANDR : 'andR';
ANDL : 'andL';
ORR : 'orR';
ORL : 'orL';
IMPLYR : 'implyR';
IMPLYL : 'implyL';
EXISTSR : 'existsR';
EXISTSL : 'existsL';
FORALLR : 'forallR';
FORALLL : 'forallL';
ROOTL : 'rootL';
ROOTR : 'rootR';

/* identifiers */
ID : ('a'..'z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
UID : ('A'..'Z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
