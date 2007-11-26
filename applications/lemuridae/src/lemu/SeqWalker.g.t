
tree grammar SeqWalker;
options {
    tokenVocab=Seq;
    ASTLabelType = Tree;
}

@header{
  package lemu;
}

@members{
  %include { sequents/sequents.tom }
}

pred returns [lemu.sequents.types.Prop p]
      : i=ID { p = `relationAppl($i.text, concTerm()); } 
      | BOTTOM { p = `bottom(); }
      | TOP { p = `top(); }
      | ^(EQUIV a=pred b=pred)  { p = `and(implies(a,b),implies(b,a)); }
      | ^(IMPL a=pred b=pred )  { p = `implies(a,b); }
      | ^(OR a=pred b=pred ) { p = `or(a,b); }
      | ^(AND a=pred b=pred ) {  p = `and(a,b); }
      | ^(NOT a=pred ) {p = `implies(a,bottom());}
      | ^(APPL ap=ID l=term_list) { p = `relationAppl($ap.text, l); }
      | ^(APPL ap=ID) { p = `relationAppl($ap.text,concTerm()); }
      | ^(FORALL vl=varlist a=pred) { p = Utils.forallList(vl,a); }
      | ^(EXISTS vl=varlist a=pred) { p = Utils.existsList(vl,a); }
      ;

varlist returns [lemu.sequents.types.StringList l] 
  : ^(VARLIST i=ID) vl=varlist { l = `strlist(vl*,$i.text); } 
  | ^(VARLIST i=ID) { l = `strlist($i.text); } 
  ;

term_list returns [lemu.sequents.types.TermList l] 
@init {
  l = `concTerm();
}
    : t=term { l = `concTerm(t,l*); }
    | ^(COMMA left=term_list t=term) {  l = `concTerm(left*,t); }
    ;

term returns [lemu.sequents.types.Term t] 
    : v=ID { t = `Var($v.text);  }
    | i=NUMBER {
        try { t = PrettyPrinter.intToPeano(Integer.parseInt($i.text)); }
        catch (NumberFormatException e) { e.getMessage(); } 
      }
    | ^(TIMES t1=term t2=term) { t = `funAppl("mult",concTerm(t1,t2)); }
    | ^(PLUS t1=term t2=term) { t = `funAppl("plus",concTerm(t1,t2)); }
    | ^(DIV t1=term t2=term) { t = `funAppl("div",concTerm(t1,t2)); }
    | ^(MINUS t1=term t2=term) { t = `funAppl("minus",concTerm(t1,t2)); }
    | ^(FAPPL f=ID l=term_list) { t = `funAppl($f.text,l); }
    | ^(FAPPL f=ID) { t = `funAppl($f.text,concTerm()); }
    ;

command returns [lemu.sequents.types.Command c] 
  : ^(PROOF i1=ID l=pred) { c = `proof($i1.text,l); }
  | ^(RRULE l=pred r=pred) { c = `rewritesuper(l,r); }
  | ^(RRULE th=ID) { c = `rewritesuperFromTheorem($th.text); }
  | ^(PRULE l=pred r=pred) { c = `rewriteprop(l,r); }
  | ^(PRULE th=ID) { c = `rewritepropFromTheorem($th.text); }
  | ^(PROP l=pred) { c = `normalizeProp(l); }
  | ^(TERM lhs=term) { c = `normalizeTerm(lhs); }
  | ^(TRULE lhs=term rhs=term) { c = `rewriteterm(lhs,rhs); }
  | ^(TRULE th=ID) { c = `rewritetermFromTheorem($th.text); }
  | ^(DISPLAY i2=ID) { c = `display($i2.text); }
  | ^(PROOFTERM i7=ID) { c = `proofterm($i7.text); }
  | QUIT { c = `quit(); }
  | REINIT { c = `reinit(); }
  | ^(PROOFCHECK i4=ID) { c = `proofcheck($i4.text); }
  | ^(PRINT i3=ID) { c = `print($i3.text); }
  | ^(RESUME i5=ID) { c = `resume($i5.text); }
  | GIBBER { c = `gibber(); }
  | ^(IMPORT i6=PATH) { c = `importfile($i6.text); }
  | ^(INDUCTIVER s=rule1) { c = `inductiver(s); }
  | ^(INDUCTIVE s=rule1) { c = `inductive(s); }
  | EOF { c = `endoffile(); }
  ;

proofcommand returns [lemu.sequents.types.ProofCommand c]
  : i=ID { c = `proofCommand($i.text); }
  | ^(FOCUS v=ID) {c = `focusCommand($v.text); }
  | ^(RRULE n=NUMBER) {c = `ruleCommand(Integer.parseInt($n.text)); }
  | FOLD {c = `foldCommand(); }
  | RRULEALONE { c = `ruleCommand(-1); }
  | ^(CUT p=pred) { c = `cutCommand(p); }
  | ^(THEOREM name=ID) { c = `theoremCommand($name.text); }
  | NORMALIZE { c = `normalizeSequent(); }
  | DISPLAY { c = `proofCommand("display"); }
  | ASKRULES { c = `askrulesCommand(); }
  | QUIT { c = `proofquit(); }
  | REINIT { c = `proofreinit(); }
  | ABORT { c = `abort(); }
  | EOF { c = `proofendoffile(); }
  ;

ident returns [String s]
  : i=ID { s = $i.text; }
  ;

/* -- stagiaires -- */

rule1 returns [lemu.sequents.types.Sig s]
  : ^(VOIDRULEINDUCT i=ID) { s = `sig($i.text,clist()); }
  | ^(RULEINDUCT i=ID l=ctor_list) { s = `sig($i.text,l); }
  ;

ctor_list returns [lemu.sequents.types.Ctorlist cl]
@init {
  cl = `clist();
}
  : (^(CTORLIST c=ctor) {cl = `clist(cl*,c);} )+
  ;

ctor returns [lemu.sequents.types.Ctor c]
  : ^(RULECTORARITYZERO i=ID) { c = `ctor($i.text,tlist()); }
  | ^(RULECTOR i=ID tl=type_list) { c = `ctor($i.text,tl*); }
  ;

type_list returns [lemu.sequents.types.TypeList tl]
@init {
  tl = `tlist();
}
  : (^(TYPELIST t=type) { tl = `tlist(tl*,t); } )+
  ;

type returns [lemu.sequents.types.Type s]
  : ^(LABELLEDTYPE l=ID i=ID) { s = `type($l.text,$i.text); }
  ;

/* -----------------*/
