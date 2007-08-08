package lemu;

import lemu.sequents.*;
import lemu.sequents.types.*;

import java.util.*;


public class Inductive {

  %include { string.tom }
  %include { sequents/sequents.tom }

  public static Prop getLhs(Sig s) {
    %match (Sig s) {
      sig(def,_) -> {return  `relationAppl(def,concTerm(Var("t")));}
    }
    return  null;
  }

  public static Prop getRhs(Sig s, boolean recursive) {
    %match (Sig s) {
      sig(def,(l*)) -> {return `forAll("p",RuleClists(l,def,0,recursive));}
    }
    return  null;
  }

  // for conveniance
  %op Prop In(t1:Term,t2:Term) { make(t1,t2) { `relationAppl("in",concTerm(t1,t2)) } }
  %op Term Cons(t1:Term,t2:Term) { make(t1,t2) { `funAppl("cons",concTerm(t1,t2)) } }
  %op Term Nil() { make() { `funAppl("nil",concTerm()) } }

  public static Prop RuleClists(Ctorlist cl, String s, int i, boolean recursive) {
    Prop res = `In(Cons(Var("t"),Nil()),Var("p"));
    %match (Ctorlist cl) {
      (ctor(t,())) -> {
        return  `implies(In(Cons(Var(t),Nil()),Var("p")),res);
      }
      (ctor(t,(l1*))) -> {
        int n = longListe(`l1);
        if (recursive) {
          return  `implies(RuleTlistQqAndType(i,0,n,l1,l1,t,s), res);
        } else {
          return  `implies(RuleTlistQq(i,0,n,l1,t,s), res);
        }
      }
      (ctor(t,()), l2*) -> {
        return  `implies(In(Cons(Var(t),Nil()),Var("p")), RuleClists(l2,s,i+1,recursive));
      }
      (ctor(t,(l1*)), l2*) -> {
        int n = longListe(`l1);
        if (recursive) {
          return  `implies(RuleTlistQqAndType(i,0,n,l1,l1,t,s), RuleClists(l2,s,i+1,recursive));
        } else {
          return  `implies(RuleTlistQq(i,0,n,l1,t,s), RuleClists(l2,s,i+1,recursive));
        }
      }
    }
    return  res;
  }

  public static int longListe(TypeList l) {
    %match(TypeList l) {
      (x,l1*) -> {return  (1 + longListe(`l1));}
      () -> {return  0;}
    }
    return 0;
  }

  public static Prop RuleTlistQq(int i, int j, int n, TypeList tl, String t, String s) {
    if (j >= n) {
      Prop p  = `In(Cons(funAppl(t,RuleCtor(tl,s,i,0)), Nil()), Var("p"));
      return  RuleTlistP(tl,s,i,0,p);
    }
    return  `forAll("x"+i+"_"+j, RuleTlistQq(i,j+1,n,tl,t,s));
  }

  public static Prop RuleTlistQqAndType(int i, int j, int n, TypeList tl1, TypeList tl2, String t, String s) {
    //Prop p  = `relationAppl("in", concTerm(funAppl("cons", concTerm(funAppl(t, RuleCtor(tl2, s, i, 0)), funAppl("nil", concTerm()))), Var("p")));
    Prop p  = `In(Cons(funAppl(t,RuleCtor(tl2,s,i,0)), Nil()), Var("p"));
    %match (TypeList tl1) {
      (type(t1), l*) -> {
        if (s.equals(`t1)) {
          return  `forAll("x"+i+"_"+j,implies(relationAppl(t1, concTerm(Var("x"+i+"_"+j))), RuleTlistQqAndType(i, j+1, n, l, tl2, t, s)));
        } else {
          return  `forAll("x"+i+"_"+j, RuleTlistQqAndType(i,j+1,n,l,tl2,t,s));
        }
      }
    }
    return  RuleTlistP(tl2,s,i,0,p);
  }

  public static Prop RuleTlistP(TypeList tl, String s, int i, int j, Prop p) {
    Prop res = p;
    %match (TypeList tl) {
      (type(t1), l*) -> {
        if(s.equals(`t1)) {
          res = `implies(In(Cons(Var("x"+i+"_"+j), Nil()), Var("p")), RuleTlistP(l,s,i,j+1,p));
        } else {
          res = RuleTlistP(`l,s,i,j+1,p);
        }
      }
    }
    return res;
  }

  %op TermList RuleCtor(t: TypeList, s: String, i: int, j: int) {}

  public static TermList RuleCtor(TypeList tl, String s, int i, int j) {
    TermList res = `concTerm();
    %match (TypeList tl) {
      (type(_),l*) -> {res = `concTerm(Var("x"+i+"_"+j), RuleCtor(l,s,i,j+1));}
    }
    return res;
  }
}
