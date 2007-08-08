package lemu;
import lemu.sequents.*;
import lemu.sequents.types.*;

public class Inductive {

  %include { sequents/sequents.tom }

  // e.g. Nat(t) 
  public static Prop getLhs(Sig s) {
    %match (Sig s) {
      sig(def,_) -> {return  `relationAppl(def,concTerm(Var("t")));}
    }
    throw new RuntimeException("should not happen");
  }

  // e.g. forall P, (P(0) -> forall m ...
  public static Prop getRhs(Sig s, boolean recursive) {
    %match (Sig s) {
      sig(def,(l*)) -> {return `forAll("p",getAxiom(l,def,recursive));}
    }
    throw new RuntimeException("should not happen");
  }

  // for conveniance
  %op Term Cons(t1:Term,t2:Term) { make(t1,t2) { `funAppl("cons",concTerm(t1,t2)) } }
  %op Term Nil() { make() { `funAppl("nil",concTerm()) } }
  %op Prop In(t1:Term,t2:Term) { make(t1,t2) { `relationAppl("in",concTerm(Cons(t1,Nil()),t2)) } }

  private static Prop getAxiom(Ctorlist cl, String def, boolean recursive) {
    // res = P(t)
    Prop res = `In(Var("t"),Var("p"));
    // res = getQuantified(nil) -> getQuantified(bin) -> P(t)
    %match (Ctorlist cl) {
      () -> { 
        return `In(Var("t"),Var("p"));
      }
      (ctor(c,(l1*)), l2*) -> {
          return  `implies(getQuantified(c,l1,def,recursive), getAxiom(l2,def,recursive));
      }
    }
    throw new RuntimeException("should not happen");
  }

  private static Prop getQuantified(String c, TypeList tl, String def, boolean recursive) {
    // p = P(bin(n,t1,t2))
    Prop p  = `In(funAppl(c,getLabels(tl)), Var("p"));

    // inner implications : p = P(t1) -> P(t2) -> P(bin(n,t1,t2))
    %match(TypeList tl, String def) {
      (_*,type(lab,name),_*) , name -> {
        p = `implies(In(Var(lab),Var("p")),p);
      }
    }

    /* quantification and label typing :
        - !recursive: p = forall n, Nat(n) -> forall t1, forall t2, [old p]
        - !recursive: p = forall n, Nat(n) -> forall t1, BinTree(t1) ...
     */
    %match(TypeList tl) {
      (_*,type(lab,name),_*) -> {
        if(recursive || !`name.equals(def)) {
          p = `implies(relationAppl(name,concTerm(Var(lab))),p);
        }
        p = `forAll(lab, p);
      }
    }

    return p;
  }

  private static TermList getLabels(TypeList tl) {
    TermList res = `concTerm();
    %match(TypeList tl) {
      (_*,type(lab,_),_*) -> { res = `concTerm(res*,Var(lab)); } 
    }
    return res;
  }
}
