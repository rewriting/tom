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
      rule(_,_,c,_) -> { return term2proofterm(term, seq2nseq(`c), 0, 0); }
    }
    return null;
  }
  
  public static ProofTerm term2proofterm(Tree term, NSequent nseq, int ncount, int cncount) { // INCOMPLET manque les cas 1er ordre
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
        return `andL(nprop(name(ncount+1),A),nprop(name(ncount+2),B),term2proofterm(p,nsequent(ncontext(ng1*,nprop(name(ncount+1),A),nprop(name(ncount+2),B),ng2*),cnd),ncount+2,cncount),name);
      }
      
      rule(
          andRightInfo[],
          (p1@rule(_,_,sequent(g,(d1*,A,d2*)),_),p2@rule(_,_,sequent(g,(d1*,B,d2*)),_)),
          sequent(g,(d1*,a,d2*)),
          a@and(A,B)
          ),nsequent(ng,(nd1*,cnprop(coname, a),nd2*)) -> {
        return `andR(cnprop(coname(cncount+1),A),term2proofterm(p1,nsequent(ng,cncontext(nd1*,cnprop(coname(cncount+1),A),nd2*)),ncount,cncount+2),cnprop(coname(cncount+2),B),term2proofterm(p2,nsequent(ng,cncontext(nd1*,cnprop(coname(cncount+2),B),nd2*)),ncount,cncount+2),coname);
      }

      rule(
          orRightInfo[],
          (p@rule(_,_,sequent(g, (d1*,A,B,d2*)),_)),
          sequent(g, (d1*,a,d2*)),
          a@or(A,B)
          ), nsequent(ng, (cnd1*,cnprop(coname,a), cnd2*)) -> {
        return `orR(cnprop(coname(cncount+1),A),cnprop(coname(cncount+2),B),term2proofterm(p,nsequent(ng,cncontext(cnd1*,cnprop(coname(cncount+1),A),cnprop(coname(cncount+2),B),cnd2*)),ncount,cncount+2), coname);
      }

      rule(
          orLeftInfo[],
          (p1@rule(_,_,sequent((g1*,A,g2*), d),_),p2@rule(_,_,sequent((g1*,B,g2*), d),_)),
          sequent((g1*,a,g2*), d),
          a@or(A,B)
          ),nsequent((ng1*,nprop(name,a),ng2*),cnd) -> {
        return `orL(nprop(name(ncount+1),A),term2proofterm(p1,nsequent(ncontext(ng1*,nprop(name(ncount+1),A),ng2*),cnd),ncount+1,cncount),nprop(name(ncount+2),B),term2proofterm(p2,nsequent(ncontext(ng1*,nprop(name(ncount+2),B),ng2*),cnd),ncount+2,cncount),name);
      }

      rule(
          impliesRightInfo[],
          (p@rule(_,_,sequent((g*,A), (d1*, B, d2*)),_)),
          sequent(g, (d1*,a,d2*)),
          a@implies(A,B)
          ),nsequent(ng,(cnd1*,cnprop(coname,a),cnd2*)) -> {
        return `implyR(nprop(name(ncount+1),A),cnprop(coname(cncount+1),B),term2proofterm(p,nsequent(ncontext(ng*,nprop(name(ncount+1),A)),cncontext(cnd1*,cnprop(coname(cncount+1),B),cnd2*)),ncount+1,cncount+1),coname);
      }

      rule(
          impliesLeftInfo[],
          (p1@rule(_,_,sequent((g1*,g2*),(A,d*)),_), p2@rule(_,_,sequent((g1*,B,g2*),d),_)), 
          sequent((g1*,a,g2*),d),
          a@implies(A,B)
          ),nsequent((ng1*,nprop(name,a),ng2*),cnd) -> {
        return `implyL(cnprop(coname(cncount+1),A),term2proofterm(p1,nsequent(ncontext(ng1*,ng2*),cncontext(cnprop(coname(cncount+1),A), cnd*)),ncount,cncount+1),nprop(name(ncount+1),B),term2proofterm(p2,nsequent(ncontext(ng1*,nprop(name(ncount+1),B),ng2*),cnd),ncount+1,cncount),name);
      }
      /*

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
        */

      rule(
          weakLeftInfo[],
          (p@rule(_,_,sequent((g1*,g2*), d),_)),
          sequent((g1*,a,g2*),d),
          a
          ),nsequ
        -> { return term2proofterm(`p, `nsequ, ncount, cncount);}
      
      rule(
          weakRightInfo[],
          (p@rule(_,_,sequent(g,(d1*,d2*)),_)),
          sequent(g,(d1*,a,d2*)),
          a
          ),nsequ
        -> { return term2proofterm(`p,`nsequ, ncount, cncount); }
      
      rule(
          cutInfo(a),
          (p1@rule(_,_,sequent(g,(d*,a)),_), p2@rule(_,_,sequent((g*,a),d),_)),
          sequent(g,d),
          _
          ),nsequent(ng,cnd) -> {
        return `cut(cnprop(coname(cncount+1),a),term2proofterm(p1,nsequent(ng,cncontext(cnd*,cnprop(coname(cncount+1),a))),ncount+1,cncount+1),nprop(name(ncount+1),a),term2proofterm(p2,nsequent(ncontext(ng*,nprop(name(ncount+1),a)),cnd),ncount+1,cncount+1));
      }

      // first order logic
      rule(
          forAllRightInfo(new_var),
          (p@rule(_,_,sequent(g,(d1*,B,d2*)),_)), 
          sequent(g,(d1*,a,d2*)),
          a@forAll(v,A)
          ),nsequent(ng,(cnd1*,cnprop(coname,a),cnd2*))
        -> {
          if ((Utils.replaceFreeVars(`A,`Var(v),`new_var) == `B) && !Utils.getFreeVars(`context(d1*,d2*,g*)).contains(`new_var.getname()))  
         return `forallR(cnprop(coname(cncount+1),B),new_var,term2proofterm(p,nsequent(ng,cncontext(cnd1*,cnprop(coname(cncount+1),B),cnd2*)),ncount,cncount+1),coname);
        }

      
      rule(
          forAllLeftInfo(new_term),
          (p@rule(_,_,sequent((g1*,B,g2*),d),_)), 
          sequent((g1*,a,g2*),d),
          a@forAll(v,A)
          ),nsequent((ng1*,nprop(name,a),ng2*),cnd)
        -> { 
          if (Utils.replaceFreeVars(`A,`Var(v),`new_term) == `B)
            return `forallL(nprop(name(ncount+1),B),term2proofterm(p, nsequent(ncontext(ng1*,nprop(name(ncount+1),B),ng2*),cnd), ncount+1, cncount), new_term, name); 
        }

      rule(
          existsRightInfo(new_term),
          (p@rule(_,_,sequent(g,(d1*,B,d2*)),_)), 
          sequent(g,(d1*,a,d2*)),
          a@exists(v,A)
          ),nsequent(ng,(cnd1*,cnprop(coname,a),cnd2*))
        -> {
          if (Utils.replaceFreeVars(`A,`Var(v),`new_term) == `B) 
            return `existsR(cnprop(coname(cncount+1),B),term2proofterm(p, nsequent(ng,cncontext(cnd1*,cnprop(coname(cncount+1),B),cnd2*)),ncount, cncount+1),new_term,coname); 
        } 

      rule(
          existsLeftInfo(new_var),
          (p@rule(_,_,sequent((g1*,B,g2*),d),_)), 
          sequent((g1*,a,g2*),d),
          a@exists(v,A)
          ),nsequent((ng1*,nprop(name,a),ng2*),cnd)
        -> {
          if ((Utils.replaceFreeVars(`A,`Var(v),`new_var) == `B) && !Utils.getFreeVars(`context(g1*,g2*,d*)).contains(`new_var.getname()))
            return `existsL(nprop(name(ncount+1),B),new_var,term2proofterm(p,nsequent(ncontext(ng1*,nprop(name(cncount+1),B),ng2*),cnd),ncount+1,cncount),name); 
        }
      /*

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

  public static NTree typeProofterm (ProofTerm pi, NSequent nseq) { // INCOMPLET
    %match (ProofTerm pi, NSequent nseq) {
      pt@ax(name,coname),ns@nsequent((_*,nprop(name,a),_*),(_*,cnprop(coname,a),_*)) -> {
        return `nrule(axiomInfo(),npremisses(),ns,pt);
      }
      pt@cut(cn@cnprop(_,a),pt1,n@nprop(_,a),pt2), ns@nsequent(ng, cnd) -> {
        return `nrule(cutInfo(a),npremisses(typeProofterm(pt1,nsequent(ng, cncontext(cn,cnd*))),typeProofterm(pt2,nsequent(ncontext(ng*,n),cnd))),ns,pt);
      }
      pt@falseL(name), ns@nsequent((_*,nprop(name,bottom()),_*),_) -> {
        return `nrule(bottomInfo(),npremisses(),ns,pt);
      }
      pt@trueR(coname), ns@nsequent(_,(_*,cnprop(coname,top()),_*)) -> {
        return `nrule(topInfo(),npremisses(),ns,pt);
      }
      pt@andR(a@cnprop(cn1,A),m1,b@cnprop(cn2,B),m2,cn), ns@nsequent(ng,(cnd1*,cnprop(cn, and(A,B)),cnd2*)) -> {
        return `nrule(andRightInfo(),npremisses(typeProofterm(m1,nsequent(ng,cncontext(cnd1*,a,cnd2*))),typeProofterm(m2,nsequent(ng,cncontext(cnd1*,b,cnd2*)))),ns,pt);
      }
      pt@andL(x@nprop(n1,A),y@nprop(n2,B),m,n), ns@nsequent((ng1*, nprop(n, and(A,B)),ng2*), cnd) -> {
        return `nrule(andLeftInfo(),npremisses(typeProofterm(m,nsequent(ncontext(ng1*,x,y,ng2*),cnd))),ns,pt);
      }
      pt@orL(x@nprop(n1,A),m1,y@nprop(n2,B),m2,n), ns@nsequent((ng1*,nprop(n, or(A,B)),ng2*),cnd) -> {
        return `nrule(orLeftInfo(),npremisses(typeProofterm(m1,nsequent(ncontext(ng1*,x,ng2*),cnd)),typeProofterm(m2,nsequent(ncontext(ng1*,y,ng2*),cnd))),ns,pt);
      }
      pt@orR(a@cnprop(cn1,A),b@cnprop(cn2,B),m,cn), ns@nsequent(ng,(cnd1*, cnprop(cn, or(A,B)),cnd2*)) -> {
        return `nrule(orRightInfo(),npremisses(typeProofterm(m,nsequent(ng,cncontext(cnd1*,a,b,cnd2*)))),ns,pt);
      }
      pt@implyL(a@cnprop(cn,A),m1, x@nprop(n,B), m2, name), ns@nsequent((ng1*,nprop(name,implies(A,B)),ng2*),cnd) -> {
        return `nrule(impliesLeftInfo(),npremisses(typeProofterm(m1,nsequent(ncontext(ng1*,ng2*),cncontext(a,cnd*))),typeProofterm(m2,nsequent(ncontext(ng1*,x,ng2*),cnd))),ns,pt);
      }
      pt@implyR(x@nprop(n,A),a@cnprop(cn,B),m,coname), ns@nsequent(ng,(cnd1*,cnprop(coname,implies(A,B)),cnd2*)) -> {
        return `nrule(impliesRightInfo(),npremisses(typeProofterm(m,nsequent(ncontext(ng*,x),cncontext(cnd1*,a,cnd2*)))),ns,pt);
      }
      pt@forallR(a@cnprop(cn,A), varx, m, coname), ns@nsequent(ng,(cnd1*,cnprop(coname,forAll(var,B)),cnd2*)) -> {
        return `nrule(forAllRightInfo(varx),npremisses(typeProofterm(m,nsequent(ng,cncontext(cnd1*,a,cnd2*)))),ns,pt);
      }
      pt@forallL(x@nprop(n,A), m, t, name), ns@nsequent((ng1*,nprop(name,forAll(var,B)),ng2*),cnd) -> {
        return `nrule(forAllLeftInfo(t),npremisses(typeProofterm(m,nsequent(ncontext(ng1*,x,ng2*),cnd))),ns,pt);
      }
      pt@existsL(x@nprop(n,A), varx, m, name), ns@nsequent((ng1*,nprop(name,exists(var,B)),ng2*),cnd) -> {
        return `nrule(existsLeftInfo(varx),npremisses(typeProofterm(m,nsequent(ncontext(ng1*,x,ng2*),cnd))),ns,pt);
      }
      pt@existsR(a@cnprop(cn,A), m, t, coname), ns@nsequent(ng,(cnd1*,cnprop(coname,exists(var,B)),cnd2*)) -> {
        return `nrule(existsRightInfo(t),npremisses(typeProofterm(m,nsequent(ng,cncontext(cnd1*,a,cnd2*)))),ns,pt);
      }

    }
    return null;
  }
 
  public static NTree typeProof(Tree proof) {
    %match(Tree proof) {
      rule(_,_,c,_) -> {
        NSequent nseq = seq2nseq(`c);
        ProofTerm pt = term2proofterm(proof, nseq, 0, 0); 
        return typeProofterm(pt, nseq);
      }
    }
   return null;
  }
}
