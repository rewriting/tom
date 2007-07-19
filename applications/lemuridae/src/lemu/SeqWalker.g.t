
tree grammar SeqWalker;
options {
    tokenVocab=Seq;
    ASTLabelType = Tree;
    backtrack=true;
}

@members{
  %include { sequents/sequents.tom }
}

pred returns [sequents.types.Prop p]
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
      | ^(FORALL vl=varlist a=pred) { p = Utils.forAllList(vl,a); }
      | ^(EXISTS vl=varlist a=pred) { p = Utils.existsList(vl,a); }
      ;

varlist returns [sequents.types.StringList l] 
  : ^(VARLIST i=ID) vl=varlist { l = `strlist(vl*,$i.text); } 
  | ^(VARLIST i=ID) { l = `strlist($i.text); } 
  ;

term_list returns [sequents.types.TermList l] 
@init {
  l = `concTerm();
}
    : t=term { l = `concTerm(t,l*); }
    | ^(COMMA left=term_list t=term) {  l = `concTerm(left*,t); }
    ;

term returns [sequents.types.Term t] 
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

command returns [sequents.types.Command c] 
  : ^(PROOF i1=ID l=pred) { c = `proof($i1.text,l); }
  | ^(RRULE l=pred r=pred) { c = `rewritesuper(l,r); }
  | ^(PRULE l=pred r=pred) { c = `rewriteprop(l,r); }
  | ^(PROP l=pred) { c = `normalizeProp(l); }
  | ^(TERM lhs=term) { c = `normalizeTerm(lhs); }
  | ^(TRULE lhs=term rhs=term) { c = `rewriteterm(lhs,rhs); }
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

proofcommand returns [sequents.types.ProofCommand c]
  : i=ID { c = `proofCommand($i.text); }
  | ^(FOCUS v=ID) {c = `focusCommand($v.text); }
  | ^(RRULE n=NUMBER) {c = `ruleCommand(Integer.parseInt($n.text)); }
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

rule1 returns [sequents.types.Sig s]
  : ^(VOIDRULEINDUCT h=ctor) { s = `Sig(clist(h)); }
  | ^(RULEINDUCT h=ctor l=ctor_list) { s = `Sig(clist(h,l*)); }
  ;

ctor_list returns [sequents.types.Ctorlist cl]
  : ^(CTORLIST c=ctor) tail=ctor_list { cl = `clist(c,tail*); }
  | ^(CTORLIST c=ctor) { cl = `clist(c); }
  ;

ctor returns [sequents.types.Ctor c]
  : ^(RULECTORARITYZERO i=ID) { c = `ctor($i.text,tlist()); }
  | ^(RULECTOR i=ID tl=type_list) { c = `ctor($i.text,tl*); }
  ;

type_list returns [sequents.types.TypeList tl]
  : ^(TYPELIST t=type) tail=type_list { tl = `tlist(t,tail*); }
  | ^(TYPELIST t=type) { tl = `tlist(t); }
  ;

type returns [sequents.types.Type s]
  : i = ID { s = `type($i.text); }
  ;
