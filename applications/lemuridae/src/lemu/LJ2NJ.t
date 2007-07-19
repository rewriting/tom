import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

class LJ2NJ {

  private Utils ut = new Utils();

  %include { sequents/sequents.tom }
  %include { mutraveler.tom }

  // tire du cours de gilles dowek sur les types, chapitre 3, page 40 a 43
  %strategy Transformer() extends `Identity() {
    visit Tree {

      // on simplifie un peu en prenant s1 à la place de pi1, ça revient au même
      rule("implies L", (s1,rule(n,pi2p,_,_)), c, implies(A,B)) -> { // mieux matcher plutot que de compter sur l'orde des premisses
        `pi2p = (Premisses) MuTraveler.init(`RepeatId(TopDown(EraseInHypothesys(B)))).visit(`pi2p);
        `pi2p = (Premisses) MuTraveler.init(`TopDown(AddInHypothesys(context(implies(A,B))))).visit(`pi2p);
        // il faut appliquer ReplaceImpliesLeft a la regle entiere pour traiter le cas des axiomes
        Tree res = `rule(n, pi2p, c, nullProp());
        res = (Tree) MuTraveler.init(`BottomUp(ReplaceImpliesLeft(A, B, s1))).visit(`res);
        return res;  
      }

      rule("implies R", (s), c, _) -> {
        return `rule("implies I", premisses(s), c, nullProp()); 
      }

      rule("and L", (rule(n,pip,_,_)), c, and(A,B)) -> {
        `pip = (Premisses) MuTraveler.init(`RepeatId(TopDown(EraseInHypothesys(A)))).visit(`pip);
        `pip = (Premisses) MuTraveler.init(`RepeatId(TopDown(EraseInHypothesys(B)))).visit(`pip);
        `pip = (Premisses) MuTraveler.init(`TopDown(AddInHypothesys(context(and(A,B))))).visit(`pip);
        // il faut appliquer ReplaceAndLeft a la regle entiere pour traiter le cas des axiomes
        Tree res = `rule(n,pip,c,nullProp());
        res = (Tree) MuTraveler.init(`BottomUp(ReplaceAndLeft(A, B))).visit(`res);
        return res;
      }

      rule("and R", (s1,s2), c, _) -> { 
        return `rule("and I", premisses(s1,s2), c, nullProp());
      }

      rule("or L", (rule(n1,pi1p,_,_),rule(n2,pi2p,_,_)), c@sequent(ctxt,C), or(A,B)) -> {
        Tree p1 = `rule("axiom", premisses(), sequent(ctxt,or(A,B)), nullProp());
        Premisses pi1pp = (Premisses) MuTraveler.init(`TopDown(AddInHypothesys(context(or(A,B))))).visit(`pi1p);
        Premisses pi2pp = (Premisses) MuTraveler.init(`TopDown(AddInHypothesys(context(or(A,B))))).visit(`pi2p);
        Tree p2 = `rule(n1, pi1pp, sequent(context(ctxt*,A),C), nullProp());
        Tree p3 = `rule(n2, pi2pp, sequent(context(ctxt*,B),C), nullProp());
        return `rule("or E", premisses(p1,p2,p3), c, nullProp());
      }
    }
  }


  %strategy EraseInHypothesys(prop:Prop) extends `Identity() {
    visit Context {
      l@(X*,x,Y*) -> { 
        if (`x == prop) 
          return `context(X*,Y*) ;
        else 
          return `l;
      }
    }
  }

  %strategy AddInHypothesys(ctx:Context) extends `Identity() {
    visit Sequent {
      sequent((X*),c) -> { return `sequent(context(X*,ctx*),c);} 
    }
  }

  // voir la regle de gilles p. 41 section 3.1.1 
  %strategy ReplaceImpliesLeft(A:Prop, B:Prop, pi1_prime:Tree) extends `Identity() {
    visit Tree {
      rule("axiom", (), sequent((H*,implies(x,y),T*),y), _) -> { 
        if (`x == A && `y == B) {
          Tree pi1_prime_plus = (Tree) MuTraveler.init(`BottomUp(AddInHypothesys(context(implies(A,B), T*)))).visit(pi1_prime);
          Tree s1 = `rule("axiom", premisses(), sequent(context(H*,implies(A,B),T*),implies(A,B)), nullProp());
          Sequent c = `sequent(context(H*,implies(A,B),T*),B);
          return `rule("implies E", premisses(s1, pi1_prime_plus), c, nullProp());
        }
      }
    }
  }

  // voir la regle de gilles p. 42 section 3.1.1
  %strategy ReplaceAndLeft(A:Prop, B:Prop) extends `Identity() {
    visit Tree {
      rule("axiom", (), sequent(delta,x), _) -> {
        Tree ax = `rule("axiom",premisses(),sequent(delta,and(A,B)),nullProp());
        if (`x == A) {
          return `rule("and E", premisses(ax),sequent(delta,A),nullProp());
        } else if (`x == B) {
          return `rule("and E", premisses(ax),sequent(delta,B),nullProp());
        }
      }
    }
  }


  public void run() {
    Prop A = ut.prop("A");
    Prop B = ut.prop("B");
    Prop C = ut.prop("C");
    Prop D = ut.prop("D");

    Sequent s0 = `sequent(context(or(implies(and(A,B),and(C,D)), implies(A,implies(B,and(C,D))))), implies(A,implies(B,and(C,D))));
    Sequent s1 = `sequent(context(implies(and(A,B),and(C,D))), implies(A,implies(B,and(C,D))));
    Sequent s2 = `sequent(context(implies(and(A,B),and(C,D)),A), implies(B,and(C,D)));
    Sequent s3 = `sequent(context(implies(and(A,B),and(C,D)),A,B), and(C,D));
    Sequent s4 = `sequent(context(A,B),and(A,B));
    Sequent s5 = `sequent(context(A,B),A);
    Sequent s6 = `sequent(context(A,B),B);
    Sequent s7 = `sequent(context(A,B,and(C,D)),and(C,D));
    Sequent s8 = `sequent(context(A,B,and(C,D)),C);
    Sequent s9 = `sequent(context(A,B,C,D),C);
    Sequent s10 = `sequent(context(A,B,and(C,D)),D);
    Sequent s11 = `sequent(context(A,B,C,D),D);
    Sequent s12 = `sequent(context(implies(A,implies(B,and(C,D)))),implies(A,implies(B,and(C,D))));

    Tree tree = ut.orL(ut.impliesR(ut.impliesR(ut.impliesL(ut.andR(ut.axiom(s5), ut.axiom(s6), s4), ut.andR(ut.andL(ut.axiom(s9), s8, `and(C,D)), ut.andL(ut.axiom(s11), s10, `and(C,D)), s7), s3, `implies(and(A,B), and(C,D))), s2), s1), ut.axiom(s12), s0, `or(implies(and(A,B),and(C,D)), implies(A, implies(B,and(C,D)))));

    System.out.println("\\documentclass{article}\n\\usep"+"ackage{proof}\n\\begin{document}\n\\[");
    System.out.println(PrettyPrinter.toLatex(tree));
    System.out.println("\\]\n\\bigskip\n");

    try {	
      Tree res = (Tree) MuTraveler.init(`BottomUp(Transformer())).visit(tree);
      System.out.println(PrettyPrinter.latexPreProcessing(res));
    } catch (VisitFailure e){
      e.printStackTrace();
    }

    System.out.println("\\end{document}\n");
  }

  public final static void main(String[] args) {
    LJ2NJ test = new LJ2NJ();
    test.run();
  }
}
