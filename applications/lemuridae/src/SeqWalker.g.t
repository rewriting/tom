//header {
//package parsingtests;
//}

{
	import sequents.*;
	import sequents.types.*;
}

//{{{ class SeqTreeWalker extends TreeParser;
class SeqTreeParser extends TreeParser;

options {
	importVocab=SeqParser;
}

{
  %include { sequents/sequents.tom }
}

seq returns [Sequent s]
{
  Context ctxt;
  Context concl;
  s = null;  
}
  : SEQ concl=list_pred { s = `sequent(context(),concl); }
  | ctxt=list_pred SEQ concl=list_pred { s = `sequent(ctxt,concl); }
  ;

list_pred returns [Context c]
{
	c = `context();
	Context left;
	Prop p;
}
  : p = pred { c = `context(p,c*); }
  | #(LIST left=list_pred p=pred) { c = `context(left*,p); }
  ; 
  
pred returns [Prop p]
    { 
      Prop a,b;
      TermList l;
      p = null;
    }
      : i:ID { p = `relationAppl(relation(i.getText()), concTerm()); } 
      | BOTTOM { p = `bottom(); }
      | TOP { p = `top(); }
      | #(IMPL a=pred b=pred )  { p = `implies(a,b); }
      | #(OR a=pred b=pred ) { p = `or(a,b); }
      | #(AND a=pred b=pred ) {  p = `and(a,b); }
//      | #(NOT a=pred ) {p = `neg(a);}
      | #(LPAREN ap:ID l=term_list) { p = `relationAppl(relation(ap.getText()), l); }
      | #(FORALL x:VAR a=pred) { p = `forAll(x.getText(),a); }
      | #(EXISTS y:VAR a=pred) { p = `exists(y.getText(),a); }
      ;

term_list returns [TermList l] 
    {
       l = `concTerm();
       TermList left;
       Term t;
    }
    : t = term { l = `concTerm(t,l*); }
    | #(LIST left=term_list t=term) {  l = `concTerm(left*,t); }
    | VOIDLIST {}
    ;

term returns [Term t] 
    {
        t = null;
        TermList l;
    }
    : v:VAR { t = `Var(v.getText());  }
    | #(LPAREN f:VAR l=term_list) { t = `funAppl(fun(f.getText()),l); }
    ;


command returns [Command c] 
  {
    c = null;
    Prop r,l;
    Sequent s;
  }
  : #(PROOF s=seq) { c = `proof(s); }
  | #(RRULE l=pred r=pred) { c = `rewritep(l,r); }
  ;

proofcommand returns [ProofCommand c]
  {
    c = null;
  }
  : i:VAR { c = `proofCommand(i.getText()); }
  | #(FOCUS v:VAR) {c = `focusCommand(v.getText()); }
  | #(RRULE n:NUMBER) {c = `ruleCommand(Integer.parseInt(n.getText())); }
  | ASKRULES { c = `askrulesCommand(); }
  ;

  /*
rewritep returns [RewriteP rw]
  {
    rw = null;
    Prop l, r;
  }
  : #(ARROW l=pred r=pred) {rw = `rewritep(l,r);}
  ;

  rewritet returns [RewriteT rw]
  {
    rw = null;
    Term l,r;
  }
  : #(ARROW l=term r=term) {rw = `rewritet(l,r);}
  ;
*/
