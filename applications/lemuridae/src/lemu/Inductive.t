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
      sig(def,(l*)) -> {return `forAll("p",getAxiom(l,def,0,recursive));}
    }
    return  null;
  }

  // for conveniance
  %op Term Cons(t1:Term,t2:Term) { make(t1,t2) { `funAppl("cons",concTerm(t1,t2)) } }
  %op Term Nil() { make() { `funAppl("nil",concTerm()) } }
  %op Prop In(t1:Term,t2:Term) { make(t1,t2) { `relationAppl("in",concTerm(t1,t2)) } }
  %op Term Constant(s:String) { make(s) { `funAppl(s,concTerm()) } }

  public static Prop getAxiom(Ctorlist cl, String def, int cptr, boolean recursive) {
    Prop res = `In(Cons(Var("t"),Nil()),Var("p"));
    %match (Ctorlist cl) {
      () -> { 
        return `In(Cons(Var("t"),Nil()),Var("p"));
      }
      (ctor(t,(l1*)), l2*) -> {
        if (recursive) {
          return  `implies(RuleTlistQqAndType(cptr,0,l1.length(),l1,l1,t,def), getAxiom(l2,def,cptr+1,recursive));
        } else {
          return  `implies(getQuantified(cptr,0,l1.length(),l1,t,def), getAxiom(l2,def,cptr+1,recursive));
        }
      }
    }
    // should not happen
    throw new RuntimeException();
  }

  public static Prop getQuantified(int i, int j, int len, TypeList tl, String t, String s) {
    if (j >= len) {
      Prop p  = `In(Cons(funAppl(t,RuleCtor(tl,s,i,0)), Nil()), Var("p"));
      return  RuleTlistP(tl,s,i,0,p);
    }
    return  `forAll("x"+i+"_"+j, getQuantified(i,j+1,len,tl,t,s));
  }

  %op TermList RuleCtor(t: TypeList, s: String, i: int, j: int) {}

  public static TermList RuleCtor(TypeList tl, String s, int i, int j) {
    TermList res = `concTerm();
    %match (TypeList tl) {
      (type(_),l*) -> {res = `concTerm(Var("x"+i+"_"+j), RuleCtor(l*,s,i,j+1));}
    }
    return res;
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

  public static Prop RuleTlistQqAndType(int i, int j, int n, TypeList tl1, TypeList tl2, String t, String s) {
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

}
