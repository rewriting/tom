package lemu;

import lemu.sequents.*;
import lemu.sequents.types.*;

import tom.library.sl.*;

public class ProofChecker {
  %include { sequents/sequents.tom }
  %include { sl.tom }

  private static Visitable tryVisit(Strategy s, Visitable v) {
    Visitable res = null;
    try { res = s.visit(v); return res; }
    catch (VisitFailure e) { return v; }
  }


  public static boolean boundedInContext(Term var, Context ctxt) {
    return Utils.replaceFreeVars(ctxt,var,`Var("@notavar@")) == ctxt;
  }

  public static boolean proofcheck(Tree term) {
    %match(Tree term) {
      /* rule(type, premisses, conclusion, focussed proposition) */

      // propositional fragment
      rule(axiomInfo[],_,sequent(context(_*,x,_*),context(_*,x,_*)),_) -> {
        return true;
      }
      rule(topInfo[],_,sequent(_,context(_*,top(),_*)),_) -> {
        return true;
      }
      rule(bottomInfo[],_,sequent(context(_*,bottom(),_*),_),_) -> {
        return true;
      }
      rule(
          andLeftInfo[],
          premisses(p@rule(_,_,sequent(context(g1*,A,B,g2*), d),_)),
          sequent(context(g1*,a,g2*),d),
          a@and(A,B)
          )
        -> { return proofcheck(`p);}
      rule(
          andRightInfo[],
          premisses(p1@rule(_,_,sequent(g,context(d1*,A,d2*)),_),p2@rule(_,_,sequent(g,context(d1*,B,d2*)),_)),
          sequent(g,context(d1*,a,d2*)),
          a@and(A,B)
          )
        -> { return (proofcheck(`p1) && proofcheck(`p2)); }
      rule(
          orRightInfo[],
          premisses(p@rule(_,_,sequent(g, context(d1*,A,B,d2*)),_)),
          sequent(g, context(d1*,a,d2*)),
          a@or(A,B)
          )
        -> { return proofcheck(`p);}
      rule(
          orLeftInfo[],
          premisses(p1@rule(_,_,sequent(context(g1*,A,g2*), d),_),p2@rule(_,_,sequent(context(g1*,B,g2*), d),_)),
          sequent(context(g1*,a,g2*), d),
          a@or(A,B)
          )
        -> { return (proofcheck(`p1) && proofcheck(`p2)); }
      rule(
          impliesRightInfo[],
          premisses(p@rule(_,_,sequent(context(g*,A), context(d1*, B, d2*)),_)),
          sequent(g, context(d1*,a,d2*)),
          a@implies(A,B)
          )
        -> { return proofcheck(`p);}
      rule(
          impliesLeftInfo[],
          premisses(p1@rule(_,_,sequent(context(g1*,g2*),context(A,d*)),_), p2@rule(_,_,sequent(context(g1*,B,g2*),d),_)),
          sequent(context(g1*,a,g2*),d),
          a@implies(A,B)
          )
        -> { return (proofcheck(`p1) && proofcheck(`p2)); }
      rule(
          contractionLeftInfo[],
          premisses(p@rule(_,_,sequent(context(g1*,a,a,g2*), d),_)),
          sequent(context(g1*,a,g2*),d),
          a
          )
        -> { return proofcheck(`p);}
      rule(
          contractionRightInfo[],
          premisses(p@rule(_,_,sequent(g,context(d1*,a,a,d2*)),_)),
          sequent(g,context(d1*,a,d2*)),
          a
          )
        -> { return proofcheck(`p); }
      rule(
          weakLeftInfo[],
          premisses(p@rule(_,_,sequent(context(g1*,g2*), d),_)),
          sequent(context(g1*,a,g2*),d),
          a
          )
        -> { return proofcheck(`p);}
      rule(
          weakRightInfo[],
          premisses(p@rule(_,_,sequent(g,context(d1*,d2*)),_)),
          sequent(g,context(d1*,a,d2*)),
          a
          )
        -> { return proofcheck(`p); }
      /* the syntax could be improved when the bug #3786 is fixed */
      rule(
          cutInfo(a),
          premisses(p1@rule(_,_,sequent(context(g*),context(d*,a)),_), p2@rule(_,_,sequent(context(g*,a),context(d*)),_)),
          sequent(context(g*),context(d*)),
          _
          )
        -> { return proofcheck(`p1) && proofcheck(`p2); }

      // first order logic
      rule(
          forallRightInfo(new_var),
          premisses(p@rule(_,_,sequent(g,context(d1*,B,d2*)),_)),
          sequent(g,context(d1*,a,d2*)),
          a@forall(v,A)
          )
        -> {
          if (Utils.replaceFreeVars(`A,`Var(v),`new_var) == `B && boundedInContext(`new_var,`context(d1*,d2*,g*)))
            return proofcheck(`p);
        }

      rule(
          forallLeftInfo(new_term),
          premisses(p@rule(_,_,sequent(context(g1*,B,g2*),d),_)),
          sequent(context(g1*,a,g2*),d),
          a@forall(v,A)
          )
        -> { if (Utils.replaceFreeVars(`A,`Var(v),`new_term) == `B) return proofcheck(`p); }

      rule(
          existsRightInfo(new_term),
          premisses(p@rule(_,_,sequent(g,context(d1*,B,d2*)),_)),
          sequent(g,context(d1*,a,d2*)),
          a@exists(v,A)
          )
        -> { if (Utils.replaceFreeVars(`A,`Var(v),`new_term) == `B) return proofcheck(`p);
        }

      rule(
          existsLeftInfo(new_var),
          premisses(p@rule(_,_,sequent(context(g1*,B,g2*),d),_)),
          sequent(context(g1*,a,g2*),d),
          a@exists(v,A)
          )
        -> {
          if (Utils.replaceFreeVars(`A,`Var(v),`new_var) == `B && boundedInContext(`new_var,`context(g1*,g2*,d*)))
            return proofcheck(`p);
        }

      // error case
      x -> {
        System.out.println(`x);
        try {PrettyPrinter.display(`x);}
        catch (Exception e) {};
        return false;
      }
    }
    return false;
  }
}
