import sequents.*;
import sequents.types.*;

import tom.library.sl.*;

public class ProofChecker {
  %include { sequents/sequents.tom }
  %include { sl.tom }

  %strategy ReplaceVar(var:Term, new_term:Term) extends `Fail() {
    visit Term {
      v@Var[] -> { if (`v == var) return new_term; }
    }
  }

  %strategy ReplaceFreeVars(var: Term, new_term: Term) extends `Fail() {
    visit Prop {
      relationAppl(r, tl) -> {
        TermList res = (TermList) `mu(MuVar("x"),Choice(ReplaceVar(var,new_term),All(MuVar("x")))).fire(`tl);
        return `relationAppl(r, res);
      }
      r@(forAll|exists)(x,_) -> { 
        if  (`Var(x) == var)  return `r; 
      }
    }
  }

  %op Strategy replaceFreeVars(var:Term, new_term:Term) {
    make(var,new_term) {
      `mu(MuVar("x"),Choice(ReplaceFreeVars(var,new_term),All(MuVar("x"))))
    }
  }

  public static boolean proofcheck (Tree term) {
    %match(Tree term) {
      /* rule(NOM,PREMISSES,CONCLUSIONS,ACTIVEPROP) */
      rule(
          axiomInfo[],
          _,
          sequent((_*,x,_*),(_*,x,_*)),
          _
          )
        -> {return true;}
      rule(
          andLeftInfo[],
          (p@rule(_,_,sequent((g1*,A,B,g2*), d),_)),
          sequent((g1*,a,g2*),d),
          a@and(A,B)
          )
        -> { return proofcheck(`p);}
      rule(
          andRightInfo[],
          (p1@rule(_,_,sequent(g,(d1*,A,d2*)),_),p2@rule(_,_,sequent(g,(d1*,B,d2*)),_)),
          sequent(g,(d1*,a,d2*)),
          a@and(A,B)
          )
        -> { return (proofcheck(`p1) && proofcheck(`p2)); }
      rule(
          orRightInfo[],
          (p@rule(_,_,sequent(g, (d1*,A,B,d2*)),_)),
          sequent(g, (d1*,a,d2*)),
          a@or(A,B)
          )
        -> { return proofcheck(`p);}
      rule(
          orLeftInfo[],
          (p1@rule(_,_,sequent((g1*,A,g2*), d),_),p2@rule(_,_,sequent((g1*,B,g2*), d),_)),
          sequent((g1*,a,g2*), d),
          a@or(A,B)
          )
        -> { return (proofcheck(`p1) && proofcheck(`p2)); }
      rule(
          impliesRightInfo[],
          (p@rule(_,_,sequent((g*,A), (d1*, B, d2*)),_)),
          sequent(g, (d1*,a,d2*)),
          a@implies(A,B)
          )
        -> { return proofcheck(`p);}
      rule(
          impliesLeftInfo[],
          (p1@rule(_,_,sequent((g1*,g2*),(A,d*)),_), p2@rule(_,_,sequent((g1*,B,g2*),d),_)), 
          sequent((g1*,a,g2*),d),
          a@implies(A,B)
          )
        -> { return (proofcheck(`p1) && proofcheck(`p2)); } 

      rule(
          forAllRightInfo(new_var),
          (p@rule(_,_,sequent(g,(d1*,B,d2*)),_)), 
          sequent(g,(d1*,a,d2*)),
          a@forAll(v,A)
          )
        -> { if (`replaceFreeVars(Var(v),new_var).fire(`A) == `B) return proofcheck(`p); } 

      rule(
          forAllLeftInfo(new_term),
          (p@rule(_,_,sequent((g1*,B,g2*),d),_)), 
          sequent((g1*,a,g2*),d),
          a@forAll(v,A)
          )
        -> { if (`replaceFreeVars(Var(v),new_term).fire(`A) == `B) return proofcheck(`p); } 

      rule(
          existsRightInfo(new_term),
          (p@rule(_,_,sequent(g,(d1*,B,d2*)),_)), 
          sequent(g,(d1*,a,d2*)),
          a@exists(v,A)
          )
        -> { if (`replaceFreeVars(Var(v),new_term).fire(`A) == `B) return proofcheck(`p); } 

      rule(
          existsLeftInfo(new_var),
          (p@rule(_,_,sequent((g1*,B,g2*),d),_)), 
          sequent((g1*,a,g2*),d),
          a@exists(v,A)
          )
        -> { if (`replaceFreeVars(Var(v),new_var).fire(`A) == `B) return proofcheck(`p); } 

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
