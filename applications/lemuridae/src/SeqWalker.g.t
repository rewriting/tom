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

/*
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
*/

pred returns [Prop p]
    { 
      Prop a,b;
      TermList l;
      p = null;
      Term t1,t2;
    }
      : i:ID { p = `relationAppl(relation(i.getText()), concTerm()); } 
      | BOTTOM { p = `bottom(); }
      | TOP { p = `top(); }
      | #(EQUIV a=pred b=pred )  { p = `and(implies(a,b),implies(b,a)); }
      | #(IMPL a=pred b=pred )  { p = `implies(a,b); }
      | #(OR a=pred b=pred ) { p = `or(a,b); }
      | #(AND a=pred b=pred ) {  p = `and(a,b); }
      | #(NOT a=pred ) {p = `implies(a,bottom());}
      | #(LPAREN ap:ID l=term_list) { p = `relationAppl(relation(ap.getText()), l); }
      | #(FORALL x:ID a=pred) { p = `forAll(x.getText(),a); }
      | #(EXISTS y:ID a=pred) { p = `exists(y.getText(),a); }
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
        Term t1,t2;
    }
    : v:ID { t = `Var(v.getText());  }
    | i:NUMBER {
        try { t = PrettyPrinter.intToPeano(Integer.parseInt(i.getText())); }
        catch (NumberFormatException e) { e.printStackTrace(); } 
      }
    | #(TIMES t1=term t2=term) { t = `funAppl(fun("mult"),concTerm(t1,t2)); }
    | #(PLUS t1=term t2=term) { t = `funAppl(fun("plus"),concTerm(t1,t2)); }
    | #(DIV t1=term t2=term) { t = `funAppl(fun("div"),concTerm(t1,t2)); }
    | #(MINUS t1=term t2=term) { t = `funAppl(fun("minus"),concTerm(t1,t2)); }
    | #(LPAREN f:ID l=term_list) { t = `funAppl(fun(f.getText()),l); }
    ;


command returns [Command c] 
  {
    c = null;
    Prop r,l;
    //Sequent s;
    Prop p;
    Term lhs,rhs;
  }
  : #(PROOF i1:ID p=pred) { c = `proof(i1.getText(),p); }
  | #(RRULE l=pred r=pred) { c = `rewritesuper(l,r); }
  | #(PRULE l=pred r=pred) { c = `rewriteprop(l,r); }
  | #(TRULE lhs=term rhs=term) { c = `rewriteterm(lhs,rhs); }
  | #(DISPLAY i2:ID) { c = `display(i2.getText()); }
  | QUIT { c = `quit(); }
  | #(PROOFCHECK i4:ID) { c = `proofcheck(i4.getText()); }
  | #(PRINT i3:ID) { c = `print(i3.getText()); }
  | #(RESUME i5:ID) { c = `resume(i5.getText()); }
  | GIBBER { c = `gibber(); }
  | #(IMPORT i6:PATH) { c = `importfile(i6.getText()); }
  | EOF { c = `endoffile(); }
  ;

proofcommand returns [ProofCommand c]
  {
    c = null;
    Prop p;
  }
  : i:ID { c = `proofCommand(i.getText()); }
  | #(FOCUS v:ID) {c = `focusCommand(v.getText()); }
  | #(RRULE n:NUMBER) {c = `ruleCommand(Integer.parseInt(n.getText())); }
  | RULEALONE { c = `ruleCommand(-1);}
  | #(CUT p=pred) { c = `cutCommand(p); }
  | #(THEOREM name:ID) { c = `theoremCommand(name.getText()); }
  | DISPLAY { c = `proofCommand("display"); }
  | ASKRULES { c = `askrulesCommand(); }
  | QUIT { c = `proofquit(); }
  | ABORT { c = `abort(); }
  | EOF { c = `proofendoffile(); }
  ;

ident returns [String s]
  {
    s = null;
  }
  : i:ID { s = i.getText(); }
  ;

