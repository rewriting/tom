//import signature.signature.types.*;
import sequents.*;
import sequents.types.*;

import java.util.*;


public class Signature {

  %include { string.tom }
  %include { sequents/sequents.tom }

  public Prop evalRule1(Sig s) {
    %match (Sig s) {
      Sig(clist(ctor(t,tlist()),l*)) -> {return  `relationAppl(t,concTerm(Var("t")));}
      _ -> {}
    }
    return  `nullProp();
  }

  public Prop evalRule2(Sig s, boolean casRec) {
    %match (Sig s) {
      Sig(clist(ctor(t,tlist()),l*)) -> {return  `forAll("p", RuleClists(l, t, 0, casRec));}
      _ -> {}
    }
    return  `nullProp();
  }

  public Prop RuleClists(Ctorlist cl, String s, int i, boolean casRec) {
    Prop res = `relationAppl("in", concTerm(funAppl("cons", concTerm(Var("t"), funAppl("nil", concTerm()))), Var("p")));
    %match (Ctorlist cl) {
      (ctor(t,tlist())) -> {
        return  `implies(relationAppl("in", concTerm(funAppl("cons", concTerm(Var(t), funAppl("nil", concTerm()))), Var("p"))), res);
      }
      (ctor(t,tlist(l1*))) -> {
        int n = longListe(`l1);
        if (casRec) {
          return  `implies(RuleTlistQqAndType(i, 0, n, l1, l1, t, s), res);
        } else {
          return  `implies(RuleTlistQq(i, 0, n, l1, t, s), res);
        }
      }
      (ctor(t,tlist()), l2*) -> {
        return  `implies(relationAppl("in", concTerm(funAppl("cons", concTerm(Var(t), funAppl("nil", concTerm()))), Var("p"))), RuleClists(l2, s, i + 1, casRec));
      }
      (ctor(t,tlist(l1*)), l2*) -> {
        int n = longListe(`l1);
        if (casRec) {
          return  `implies(RuleTlistQqAndType(i, 0, n, l1, l1, t, s), RuleClists(l2, s, i + 1, casRec));
        } else {
          return  `implies(RuleTlistQq(i, 0, n, l1, t, s), RuleClists(l2, s, i + 1, casRec));
        }
      }
    }
    return  res;
  }

  public int longListe(TypeList l) {
    %match(TypeList l) {
      (x,l1*) -> {return  (1 + longListe(`l1));}
      () -> {return  0;}
    }
    return 0;
  }

  public Prop RuleTlistQq(int i, int j, int n, TypeList tl, String t, String s) {
    if (j >= n) {
      Prop p  = `relationAppl("in", concTerm(funAppl("cons", concTerm(funAppl(t, RuleCtor(tl, s, i, 0)), funAppl("nil", concTerm()))), Var("p")));
      return  RuleTlistP(tl, s, i, 0, p);
    }
    return  `forAll("x"+i+"_"+j, RuleTlistQq(i, j+1, n, tl, t, s));
  }

  public Prop RuleTlistQqAndType(int i, int j, int n, TypeList tl1, TypeList tl2, String t, String s) {
    Prop p  = `relationAppl("in", concTerm(funAppl("cons", concTerm(funAppl(t, RuleCtor(tl2, s, i, 0)), funAppl("nil", concTerm()))), Var("p")));
    %match (TypeList tl1) {
      (type(t1), l*) -> {
        if (s.equals(`t1)) {
          return  `forAll("x"+i+"_"+j,implies(relationAppl(t1, concTerm(Var("x"+i+"_"+j))), RuleTlistQqAndType(i, j+1, n, l, tl2, t, s)));
        } else {
          return  `forAll("x"+i+"_"+j, RuleTlistQqAndType(i, j+1, n, l, tl2, t, s));
        }
      }
    }
    return  RuleTlistP(tl2, s, i, 0, p);
  }

  public Prop RuleTlistP(TypeList tl, String s, int i, int j, Prop p) {
    Prop res = p;
    %match (TypeList tl) {
      (type(t1), l*) -> {
        if(s.equals(`t1)) {
	    res = `implies(relationAppl("in", concTerm(funAppl("cons", concTerm(Var("x"+i+"_"+j), funAppl("nil", concTerm()))), Var("p"))), RuleTlistP(l, s, i, j+1, p));
        } else {
          res = RuleTlistP(`l, s, i ,j+1, p);
        }
      }
    }
    return res;
  }

  %op TermList RuleCtor(t: TypeList, s: String, i: int, j: int) {}

  public static TermList RuleCtor(TypeList tl, String s, int i, int j) {
    TermList res = `concTerm();
    %match (TypeList tl) {
      (type(_),l*) -> {res = `concTerm(Var("x"+i+"_"+j), RuleCtor(l, s, i, j+1));}
    }
    return res;
  }

  public final static void main(String[] args) {
    Signature test = new Signature();
  }
}
