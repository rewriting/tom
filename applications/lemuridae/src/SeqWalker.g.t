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
    ;

term returns [Term t] 
    {
        t = null;
        TermList l;
    }
    : v:VAR { t = `Var(v.getText());  }
    | #(LPAREN f:VAR l=term_list) { t = `funAppl(fun(f.getText()),l); }
    ;

