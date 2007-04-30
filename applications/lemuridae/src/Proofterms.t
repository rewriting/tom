import sequents.*;
import sequents.types.*;

import urban.*;
import urban.types.*;

import tom.library.sl.*;

public class Proofterms {
//  %include { sequents/sequents.tom }
  %include { urban/urban.tom }
  %include { sl.tom }

  public static ProofTerm getProofterm(Tree term) {
    %match(Tree term) {
      rule(_,_,c,_) -> { return term2proofterm(term, seq2nseq(`c), 0, 1); }
    }
    return null;
  }
  
  public static ProofTerm term2proofterm(Tree term, NSequent nseq, int ncount, int cncount) {
    %match(Tree term,NSequent nseq) {
      /* rule(type, premisses, conclusion, focussed proposition) */

      // propositional fragment
      rule(axiomInfo[],_,sequent((_*,x,_*),(_*,x,_*)),_), nsequent((_*,nprop(name,x),_*),(_*,cnprop(coname,x),_*)) -> {
        return `ax(name,coname);
      }

      rule(topInfo[],_,sequent(_,(_*,top(),_*)),_), nsequent((_*),(_*,cnprop(coname,top()),_*)) -> {
        return `trueR(coname);
      }

      rule(bottomInfo[],_,sequent((_*,bottom(),_*),_),_),nsequent((_*,nprop(name,bottom()),_*),(_*)) -> {
        return `falseL(name);
      }

      rule(
          andLeftInfo[],
          (p@rule(_,_,sequent((g1*,A,B,g2*), d),_)),
          sequent((g1*,a,g2*),d),
          a@and(A,B)
          ),nsequent((ng1*,nprop(name,a),ng2*),cnd) -> {
        return `andL(name(ncount+1),name(ncount+2),term2proofterm(p,nsequent(ncontext(ng1*,nprop(name(ncount+1),A),nprop(name(ncount+2),B),ng2*),cnd),ncount+2,cncount),name);
      }

      /*
      rule(
          andRightInfo[],
          (p1@rule(_,_,sequent(g,(d1*,A,d2*)),_),p2@rule(_,_,sequent(g,(d1*,B,d2*)),_)),
          sequent(g,(d1*,a,d2*)),
          a@and(A,B)
          ),nsequent(ng,(nd1*,cnprop(coname, a),nd2*)) -> {
        return andR(coname(cncount+1),term2proofterm(p1,nsequent(ng,cncontext(nd1*,cnprop(coname(cncount+1),A),nd2*)),ncount,cncount+2),coname(cncount+2),term2proofterm(p1,nsequent(ng,cncontext(nd1*,cnprop(coname(cncount+2),B),nd2*)),ncount,cncount+2),coname)
      }

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
          contractionLeftInfo[],
          (p@rule(_,_,sequent((g1*,a,a,g2*), d),_)),
          sequent((g1*,a,g2*),d),
          a
          )
        -> { return proofcheck(`p);}
      rule(
          contractionRightInfo[],
          (p@rule(_,_,sequent(g,(d1*,a,a,d2*)),_)),
          sequent(g,(d1*,a,d2*)),
          a
          )
        -> { return proofcheck(`p); }
      rule(
          weakLeftInfo[],
          (p@rule(_,_,sequent((g1*,g2*), d),_)),
          sequent((g1*,a,g2*),d),
          a
          )
        -> { return proofcheck(`p);}
      rule(
          weakRightInfo[],
          (p@rule(_,_,sequent(g,(d1*,d2*)),_)),
          sequent(g,(d1*,a,d2*)),
          a
          )
        -> { return proofcheck(`p); }
      rule(
          cutInfo(a),
          (p1@rule(_,_,sequent(g,(d*,a)),_), p2@rule(_,_,sequent((g*,a),d),_)),
          sequent(g,d),
          _
          )
        -> { return proofcheck(`p1) && proofcheck(`p2); }

      // first order logic
      rule(
          forAllRightInfo(new_var),
          (p@rule(_,_,sequent(g,(d1*,B,d2*)),_)), 
          sequent(g,(d1*,a,d2*)),
          a@forAll(v,A)
          )
        -> {
          if (`replaceFreeVars(Var(v),new_var).fire(`A) == `B && boundedInContext(`new_var,`context(d1*,d2*,g*)))  
            return proofcheck(`p); 
        }

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
        -> { if (`replaceFreeVars(Var(v),new_term).fire(`A) == `B) return proofcheck(`p); 
        } 

      rule(
          existsLeftInfo(new_var),
          (p@rule(_,_,sequent((g1*,B,g2*),d),_)), 
          sequent((g1*,a,g2*),d),
          a@exists(v,A)
          )
        -> {
          if (`replaceFreeVars(Var(v),new_var).fire(`A) == `B && boundedInContext(`new_var,`context(g1*,g2*,d*)))
            return proofcheck(`p); 
        } 

      // error case
      x -> {
        System.out.println(`x);
        try {PrettyPrinter.display(`x);}
        catch (Exception e) {};
        return false;
      }
      */
    }
    return null;
  }

  public static NSequent seq2nseq (Sequent seq) {
    %match(Sequent seq) {
      sequent(h,c) -> { return `nsequent(cont2ncont(h, 0), cont2cncont(c, 0)); }
    }
    return null;
  }

  public static NContext cont2ncont (Context c,int n) {
    %match(Context c) {
      (x,y*) -> {
        NContext nc= cont2ncont(`y,n+1);
        return `ncontext(nprop(name(n),x), nc*) ;
      }
      () -> { return `ncontext(); }
    }
    return null;
  }

  public static CNContext cont2cncont (Context c,int n) {
    %match(Context c) {
      (x,y*) -> {
        CNContext cnc= cont2cncont(`y,n+1);
        return `cncontext(cnprop(coname(n),x), cnc*) ;
      }
      () -> { return `cncontext(); }
    }
    return null;
  }
  
}
