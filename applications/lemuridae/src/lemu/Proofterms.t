import sequents.*;
import sequents.types.*;

import urban.*;
import urban.types.*;

import tom.library.sl.*;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;

public class Proofterms {
//  %include { sequents/sequents.tom }
  %include { urban/urban.tom }
  %include { urban/_urban.tom}
  %include { sl.tom }
  %include { util/types/Collection.tom}

    %typeterm VarGenerator {
    implement { VarGenerator }
    is_sort(t) { t instanceof VarGenerator }
  }

  public static ProofTerm getProofterm(Tree term) {
    %match(Tree term) {
      rule(_,_,c,_) -> { return term2proofterm(term, seq2nseq(`c), 0, 0); }
    }
    return null;
  }

  public static TypableProofTerm getTypableProofterm(Tree term) {
    %match(Tree term) {
      rule(_,_,c,_) -> {
        NSequent nseq = seq2nseq(`c);
        return `typablePT(term2proofterm(term, nseq, 0, 0), nseq); 
      }
    }
    return null;
  }

  public static ProofTerm typableProofterm2Proofterm(TypableProofTerm tpt) {
    %match (TypableProofTerm tpt) {
      typablePT(pt,nseq) -> { return `pt;}
    }
    return null;
  }

  public static NSequent typableProofterm2NSequent(TypableProofTerm tpt) {
    %match (TypableProofTerm tpt) {
      typablePT(pt,nseq) -> { return `nseq;}
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

      rule(
          contractionLeftInfo[],
          (p@rule(_,_,sequent((g1*,a,a,g2*), d),_)),
          sequent((g1*,a,g2*),d),
          a
          ),nsequent((ng1*,np@nprop(name,a),ng2*),cnd)
        -> {
          return `term2proofterm(p,nsequent(ncontext(ng1*,np,np,ng2*),cnd),ncount,cncount);
        }
      
      rule(
          contractionRightInfo[],
          (p@rule(_,_,sequent(g,(d1*,a,a,d2*)),_)),
          sequent(g,(d1*,a,d2*)),
          a
          ),nsequent(ng,(cnd1*,cnp@cnprop(coname,a),cnd2*))
        -> {
          return `term2proofterm(p,nsequent(ng,cncontext(cnd1*,cnp,cnp,cnd2*)),ncount,cncount);
        }
        

      rule(
          weakLeftInfo[],
          (p@rule(_,_,_,_)),
          _,
          a
          ),nsequ
        -> { return term2proofterm(`p, `nsequ, ncount, cncount);}
      
      rule(
          weakRightInfo[],
          (p@rule(_,_,_,_)),
          _,
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
//    System.out.println(pi);
    if (isImplicitContraction(pi)) {
//      System.out.println("implicit contraction detected");
      %match (ProofTerm pi, NSequent nseq) {
        /* plusieurs liaison d'une meme variable...
        _, nsequent((_*,nprop(n,phi1),_*,nprop(n,phi2),_*),_) -> {
          if (! (`phi1 == `phi2)) { System.out.println("double occurence of "+`n); return null;}
        }
        _, nsequent(_,(_*,cnprop(cn,phi1),_*,cnprop(cn,phi2),_*)) -> {
          if (! (`phi1 == `phi2)) { System.out.println("double occurence of "+`cn); return null;}
        }
        */
         pt@andR(a@cnprop(cn1,A),m1,b@cnprop(cn2,B),m2,cn), ns@nsequent(ng,(cnd1*,active@cnprop(cn, and(A,B)),cnd2*)) -> {
          return `nrule(andRightInfo(),npremisses(typeProofterm(m1,nsequent(ng,cncontext(cnd1*,active,a,cnd2*))),typeProofterm(m2,nsequent(ng,cncontext(cnd1*,active,b,cnd2*)))),ns,pt);
        }
        pt@andL(x@nprop(n1,A),y@nprop(n2,B),m,n), ns@nsequent((ng1*, active@nprop(n, and(A,B)),ng2*), cnd) -> {
          return `nrule(andLeftInfo(),npremisses(typeProofterm(m,nsequent(ncontext(ng1*,active,x,y,ng2*),cnd))),ns,pt);
        }
        pt@orL(x@nprop(n1,A),m1,y@nprop(n2,B),m2,n), ns@nsequent((ng1*,active@nprop(n, or(A,B)),ng2*),cnd) -> {
          return `nrule(orLeftInfo(),npremisses(typeProofterm(m1,nsequent(ncontext(ng1*,active,x,ng2*),cnd)),typeProofterm(m2,nsequent(ncontext(ng1*,active,y,ng2*),cnd))),ns,pt);
        }
        pt@orR(a@cnprop(cn1,A),b@cnprop(cn2,B),m,cn), ns@nsequent(ng,(cnd1*, active@cnprop(cn, or(A,B)),cnd2*)) -> {
          return `nrule(orRightInfo(),npremisses(typeProofterm(m,nsequent(ng,cncontext(cnd1*,active,a,b,cnd2*)))),ns,pt);
        }
        pt@implyL(a@cnprop(cn,A),m1, x@nprop(n,B), m2, name), ns@nsequent((ng1*,active@nprop(name,implies(A,B)),ng2*),cnd) -> {
          return `nrule(impliesLeftInfo(),npremisses(typeProofterm(m1,nsequent(ncontext(ng1*,active,ng2*),cncontext(a,cnd*))),typeProofterm(m2,nsequent(ncontext(ng1*,active,x,ng2*),cnd))),ns,pt);
        }
        pt@implyR(x@nprop(n,A),a@cnprop(cn,B),m,coname), ns@nsequent(ng,(cnd1*,active@cnprop(coname,implies(A,B)),cnd2*)) -> {
          return `nrule(impliesRightInfo(),npremisses(typeProofterm(m,nsequent(ncontext(ng*,x),cncontext(cnd1*,active,a,cnd2*)))),ns,pt);
        }
        pt@forallR(a@cnprop(cn,A), varx, m, coname), ns@nsequent(ng,(cnd1*,active@cnprop(coname,forAll(var,B)),cnd2*)) -> {
          return `nrule(forAllRightInfo(varx),npremisses(typeProofterm(m,nsequent(ng,cncontext(cnd1*,active,a,cnd2*)))),ns,pt);
        }
        pt@forallL(x@nprop(n,A), m, t, name), ns@nsequent((ng1*,active@nprop(name,forAll(var,B)),ng2*),cnd) -> {
          return `nrule(forAllLeftInfo(t),npremisses(typeProofterm(m,nsequent(ncontext(ng1*,active,x,ng2*),cnd))),ns,pt);
        }
        pt@existsL(x@nprop(n,A), varx, m, name), ns@nsequent((ng1*,active@nprop(name,exists(var,B)),ng2*),cnd) -> {
          return `nrule(existsLeftInfo(varx),npremisses(typeProofterm(m,nsequent(ncontext(ng1*,active,x,ng2*),cnd))),ns,pt);
        }
        pt@existsR(a@cnprop(cn,A), m, t, coname), ns@nsequent(ng,(cnd1*,active@cnprop(coname,exists(var,B)),cnd2*)) -> {
          return `nrule(existsRightInfo(t),npremisses(typeProofterm(m,nsequent(ng,cncontext(cnd1*,active,a,cnd2*)))),ns,pt);
        }
      }
    }
    else {
//      System.out.println("no Implicit Contraction");
//      System.out.println(pi);
      %match (ProofTerm pi, NSequent nseq) {
        /* plusieurs liaison d'une meme variable
        _, nsequent((_*,nprop(n,phi1),_*,nprop(n,phi2),_*),_) -> {
          if (! (`phi1 == `phi2)) { System.out.println("double occurence of "+`n); return null;}
        }
        _, nsequent(_,(_*,cnprop(cn,phi1),_*,cnprop(cn,phi2),_*)) -> {
          if (! (`phi1 == `phi2)) { System.out.println("double occurence of "+`cn); return null;}
        }
        */
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

    }
    //System.out.println(pi+" , "+nseq);
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

  public static NTree typeTypableProofterm(TypableProofTerm tpt) {
    %match(TypableProofTerm tpt) {
      typablePT(pt,nseq) -> { return `typeProofterm(pt, nseq);}
    }
    return null;
  }

/*  public static boolean nameAppearsFree(urbanAbstractType term, Name name) {
    boolean bool = nameAppearsFreeAux(term,name);
    System.out.println(term+" , "+name+" : "+bool);
    return bool;
  }*/

  public static boolean nameAppearsFree(urbanAbstractType term, Name name) {
    %match (NProp term) {
      nprop(n,p) -> {
        if (name.equals(`n)) { return true;}
        else {return false;}
      }
    }

    %match (NContext term) {
      () -> {return false;}
      (a,b*) -> {return (`nameAppearsFree(a,name) || `nameAppearsFree(b,name));}
    }

    %match (NSequent term) {
      nsequent(ncont,cncont) -> {return `nameAppearsFree(ncont,name); }
    }

    %match (Name term) {
//      name -> { System.out.println("occurence of "+name+" found :"+term); return true;} // c'est la que ca coince
//      n -> {System.out.println(`n+" non egal a "+name) ;return false;}
      n -> {
        if (term.equals(name)) {
          //System.out.println("occurence of "+name+" found :"+term);
          return true;
        }
        else {
          //System.out.println(`n+" non egal a "+name) ;
          return false;
        }
      }
    }

    %match (ProofTerm term) { 
      ax(n,cn) -> {return `nameAppearsFree(n,name); }
      cut(a,m1,x,m2) -> {return (`nameAppearsFree(m1,name) || (`nameAppearsFree(m2,name) && (! `nameAppearsFree(x,name))));}
      falseL(n) -> {return `nameAppearsFree(n,name); }
      trueR(cn) -> {return false;}
      andR(a,m1,b,m2,nc) -> {return (`nameAppearsFree(m1,name) || `nameAppearsFree(m2,name)) ; }
      andL(x,y,m,n) -> {return (`nameAppearsFree(n,name) || (`nameAppearsFree(m,name) && (!`nameAppearsFree(x,name)) && (!`nameAppearsFree(y,name)))) ;}
      orR(a,b,m,cn) -> {return `nameAppearsFree(m,name); }
      orL(x,m1,y,m2,n) -> {return (`nameAppearsFree(n,name) || (`nameAppearsFree(m1,name) && (! `nameAppearsFree(x,name))) || (`nameAppearsFree(m2,name) && (! `nameAppearsFree(y,name)))); }
      implyR(x,a,m1,cn) -> {return (`nameAppearsFree(m1,name) && (! `nameAppearsFree(x,name))) ;}
      implyL(a,m1,x,m2,n) -> {return (`nameAppearsFree(n,name) || (`nameAppearsFree(m1,name)) || (`nameAppearsFree(m2,name) && (! `nameAppearsFree(x,name))));}
      forallR(a,varx,m,cn) -> {return (`nameAppearsFree(m,name));}
      forallL(x,m,t,n) -> {return (`nameAppearsFree(n,name) || (`nameAppearsFree(m,name) && (! `nameAppearsFree(x,name))));}
      existsR(a,m,t,cn) -> {return `nameAppearsFree(m,name);}
      existsL(x,varx,m,n) -> {return (`nameAppearsFree(n,name) || (`nameAppearsFree(m,name) && (! `nameAppearsFree(x,name))));}
    }

    // in any other case, return false
    return false;
  }

  public static boolean nameTopIntroduced(ProofTerm term, Name name) { 
    %match (ProofTerm term) {
      ax(n,cn) -> {return `nameAppearsFree(n,name); }
      falseL(n) -> {return `nameAppearsFree(n,name); }
      andL(x,y,m,n) -> {return `nameAppearsFree(n,name) ;}
      orL(x,m1,y,m2,n) -> {return `nameAppearsFree(n,name) ; }
      implyL(a,m1,x,m2,n) -> {return `nameAppearsFree(n,name) ;}
      forallL(x,m,t,n) -> {return (`nameAppearsFree(n,name));}
      existsL(x,varx,m,n) -> {return (`nameAppearsFree(n,name));}
    }
    return false;
  }

  public static boolean conameAppearsFree(urbanAbstractType term, CoName coname) {
    %match (CNProp term) {
      cnprop(cn,p) -> {
        if (coname.equals(`cn)) { return true;}
        else {return false;}
      }
    }

    %match (CNContext term) {
      () -> {return false;}
      (a,b*) -> {return (`conameAppearsFree(a,coname) || `conameAppearsFree(b,coname));}
    }

    %match (NSequent term) {
      nsequent(ncont,cncont) -> {return `conameAppearsFree(cncont,coname); }
    }

    %match (CoName term) {
      n -> {
        if (term.equals(coname)) {
          return true;
        }
        else {
          return false;
        }
      }
    }

    %match (ProofTerm term) { 
      ax(n,cn) -> {return `conameAppearsFree(cn,coname); }
      cut(a,m1,x,m2) -> {return (`conameAppearsFree(m2,coname) || (`conameAppearsFree(m1,coname) && (! `conameAppearsFree(a,coname))));}
      falseL(n) -> {return false;}
      trueR(cn) -> {return `conameAppearsFree(cn,coname); }
      orL(x,m1,y,m2,n) -> {return (`conameAppearsFree(m1,coname) || `conameAppearsFree(m2,coname)) ; }
      orR(a,b,m,cn) -> {return (`conameAppearsFree(cn,coname) || (`conameAppearsFree(m,coname) && (!`conameAppearsFree(a,coname)) && (!`conameAppearsFree(b,coname)))) ;}
      andL(x,y,m,n) -> {return `conameAppearsFree(m,coname); }
      andR(a,m1,b,m2,cn) -> {return (`conameAppearsFree(cn,coname) || (`conameAppearsFree(m1,coname) && (! `conameAppearsFree(a,coname))) || (`conameAppearsFree(m2,coname) && (! `conameAppearsFree(b,coname)))); }
      implyR(x,a,m1,cn) -> {return (`conameAppearsFree(cn,coname) || (`conameAppearsFree(m1,coname) && (! `conameAppearsFree(a,coname)))) ;}
      implyL(a,m1,x,m2,n) -> {return (`conameAppearsFree(m2,coname)) || (`conameAppearsFree(m1,coname) && (! `conameAppearsFree(a,coname)));}
      forallR(a,varx,m,cn) -> {return (`conameAppearsFree(cn,coname) || (`conameAppearsFree(m,coname) && (! `conameAppearsFree(a,coname))));}
      forallL(x,m,t,n) -> {return `conameAppearsFree(m,coname);}
      existsR(a,m,t,cn) -> {return (`conameAppearsFree(cn,coname) || (`conameAppearsFree(m,coname) && (! `conameAppearsFree(a,coname))));}
      existsL(x,varx,m,n) -> {return `conameAppearsFree(m,coname);}
    }

    // in any other case, return false
    return false;
  }

  public static boolean conameTopIntroduced(ProofTerm term, CoName coname) { 
    %match (ProofTerm term) {
      ax(n,cn) -> {return `conameAppearsFree(cn,coname); }
      trueR(cn) -> {return `conameAppearsFree(cn,coname); }
      orR(a,b,m,cn) -> {return `conameAppearsFree(cn,coname) ;}
      andR(a,m1,by,m2,cn) -> {return `conameAppearsFree(cn,coname) ; }
      implyR(x,a,m,cn) -> {return `conameAppearsFree(cn,coname) ;}
      existsR(a,m,t,cn) -> {return (`conameAppearsFree(cn,coname));}
      forallR(a,varx,m,cn) -> {return (`conameAppearsFree(cn,coname));}
    }
    return false;
  }

  public static boolean isImplicitContraction(ProofTerm term) {
    %match (ProofTerm term) {
      // left
      andL(x,y,m,n) -> {return (`nameAppearsFree(m,n) && (! `nameAppearsFree(x,n)) && (! `nameAppearsFree(y,n))); }
      orL(x,m1,y,m2,n) -> {return ((`nameAppearsFree(m1,n) && (! `nameAppearsFree(x,n))) || (`nameAppearsFree(m2,n) && (! `nameAppearsFree(y,n))));}
      implyL(a,m1,x,m2,n) -> {return (`nameAppearsFree(m1,n) || (`nameAppearsFree(m2,n) && (! `nameAppearsFree(x,n)))) ;}
      forallL(x,m,t,n) -> {return (`nameAppearsFree(m,n) && (! `nameAppearsFree(x,n)));}
      existsL(x,varx,m,n) -> {return (`nameAppearsFree(m,n) && (! `nameAppearsFree(x,n)));}
      // right
      orR(a,b,m,cn) -> {return (`conameAppearsFree(m,cn) && (! `conameAppearsFree(a,cn)) && (! `conameAppearsFree(b,cn))); }
      andR(a,m1,b,m2,cn) -> {return ((`conameAppearsFree(m1,cn) && (! `conameAppearsFree(a,cn))) || (`conameAppearsFree(m2,cn) && (! `conameAppearsFree(b,cn))));}
      implyR(x,a,m1,cn) -> {return (`conameAppearsFree(m1,cn) && (! `conameAppearsFree(a,cn)));}
      existsR(a,m,t,cn) -> {return (`conameAppearsFree(m,cn) && (! `conameAppearsFree(a,cn)));}
      forallR(a,varx,m,cn) -> {return (`conameAppearsFree(m,cn) && (! `conameAppearsFree(a,cn)));}
    }
    return false;
  }

  public static boolean nameFreshlyIntroduced(ProofTerm t, Name n) {
    return (nameTopIntroduced(t,n) && (! isImplicitContraction(t)));
  }

  public static boolean conameFreshlyIntroduced(ProofTerm t, CoName cn) {
    return (conameTopIntroduced(t,cn) && (! isImplicitContraction(t)));
  }

  // Reductions des termes

  private static ProofTerm ReName(ProofTerm pt, Name name1, Name name2) { // INCOMPLET MANQUE PREMIER ORDRE
    %match(ProofTerm pt) {
      ax(n,cn) -> {
        if (name1 == `n) return `ax(name2,cn); else return pt;
      }
      cut(a,m1,x@nprop(n,_),m2) -> {
        ProofTerm new_m2 = `m2;
        if (! (name1 == `n)) new_m2 = `ReName(m2,name1,name2);
        ProofTerm new_m1 = `ReName(m1,name1,name2);
        return `cut(a, new_m1, x, new_m2);
      }
      trueR(cn) -> { return pt;}
      falseL(n) -> { if (name1 == `n) return `falseL(name2); else return pt;}
      andL(x@nprop(n1,_),y@nprop(n2,_),m,n) -> {
        ProofTerm new_m = `m;
        if ((! (name1 == `n1)) && (! (name1 == `n2))) new_m = `ReName(m,name1,name2);
        Name new_n = `n;
        if (name1 == `n) new_n = name2;
        return `andL(x,y,new_m, new_n);
      }
      andR(a,m1,b,m2,cn) -> {
        ProofTerm new_m1 = `ReName(m1,name1,name2);
        ProofTerm new_m2 = `ReName(m2,name1,name2);
        return `andR(a,new_m1,b,new_m2,cn);
      }
      orL(x@nprop(n1,_),m1,y@nprop(n2,_),m2,n) -> {
        ProofTerm new_m1 = `m1;
        if (! (name1 == `n1)) new_m1 = `ReName(m1,name1,name2);
        ProofTerm new_m2 = `m2;
        if (! (name1 == `n2)) new_m2 = `ReName(m2,name1,name2);
         Name new_n = `n;
        if (name1 == `n) new_n = name2;
        return `orL(x,new_m1,y,new_m2,new_n);
      }
      orR(a,b,m,cn) -> {
        ProofTerm new_m = `ReName(m,name1,name2);
        return `orR(a,b,new_m,cn);
      }
      implyL(a,m1,x@nprop(n2,_),m2,n) ->{
        ProofTerm new_m1 = `ReName(m1,name1,name2);
        ProofTerm new_m2 = `m2;
        if (! (name1 == `n2)) new_m2 = `ReName(m2,name1,name2);
        Name new_n = `n;
        if (name1 == `n) new_n = name2;
        return `implyL(a,new_m1,x,new_m2,new_n);
      }
      implyR(x@nprop(n1,_),a,m1,cn) -> {
        ProofTerm new_m1 = `m1;
        if (! (name1 == `n1)) new_m1 = `ReName(m1,name1,name2);
        return `implyR(x,a,new_m1,cn);
       }
    }
    return null;
  }

  private static ProofTerm ReCoName(ProofTerm pt, CoName coname1, CoName coname2) { // INCOMPLET MANQUE PREMIER ORDRE
    %match(ProofTerm pt) {
      ax(n,cn) -> {
        if (coname1 == `cn) return `ax(n,coname2); else return pt;
      }
      cut(a@cnprop(cn,_),m1,x,m2) -> {
        ProofTerm new_m1 = `m1;
        if (! (coname1 == `cn)) new_m1 = `ReCoName(m1,coname1,coname2);
        ProofTerm new_m2 = `ReCoName(m2,coname1,coname2);
        return `cut(a, new_m1, x, new_m2);
      }
      falseL(n) -> { return pt;}
      trueR(cn) -> { if (coname1 == `cn) return `trueR(coname2); else return pt;}
      orR(a@cnprop(cn1,_),b@cnprop(cn2,_),m,cn) -> {
        ProofTerm new_m = `m;
        if ((! (coname1 == `cn1)) && (! (coname1 == `cn2))) new_m = `ReCoName(m,coname1,coname2);
        CoName new_cn = `cn;
        if (coname1 == `cn) new_cn = coname2;
        return `orR(a,b,new_m, new_cn);
      }
      orL(x,m1,y,m2,n) -> {
        ProofTerm new_m1 = `ReCoName(m1,coname1,coname2);
        ProofTerm new_m2 = `ReCoName(m2,coname1,coname2);
        return `orL(x,new_m1,x,new_m2,n);
      }
      andR(a@cnprop(cn1,_),m1,b@cnprop(cn2,_),m2,cn) -> {
        ProofTerm new_m1 = `m1;
        if (! (coname1 == `cn1)) new_m1 = `ReCoName(m1,coname1,coname2);
        ProofTerm new_m2 = `m2;
        if (! (coname1 == `cn2)) new_m2 = `ReCoName(m2,coname1,coname2);
        CoName new_cn = `cn;
        if (coname1 == `cn) new_cn = coname2;
        return `andR(a,new_m1,b,new_m2,new_cn);
      }
      andL(x,y,m,n) -> {
        ProofTerm new_m = `ReCoName(m,coname1,coname2);
        return `andL(x,y,new_m,n);
      }
      implyL(a@cnprop(cn1,_),m1,x,m2,n) ->{
        ProofTerm new_m2 = `ReCoName(m2,coname1,coname2);
        ProofTerm new_m1 = `m1;
        if (! (coname1 == `cn1)) new_m1 = `ReCoName(m1,coname1,coname2);
        return `implyL(a,new_m1,x,new_m2,n);
        }
      implyR(x,a@cnprop(cn1,_),m1,cn) -> {
        ProofTerm new_m1 = `m1;
        if (! (coname1 == `cn1)) new_m1 = `ReCoName(m1,coname1,coname2);
        CoName new_cn = `cn;
        if (coname1 == `cn) new_cn = coname2;
        return `implyR(x,a,new_m1,new_cn);
        }
    }
    return null;
  }

  /*
  %strategy Reconame(CoName a, CoName b) extends `Identity() {
    visit CoName {
      x -> { if (`x == a) return b;}
    }
  }

 private static Strategy TopDownReconame(CoName a, CoName b) {
    return `mu(MuVar("x"), Choice(Is_cnprop(),Sequence(Reconame(a,b), All(MuVar("x")))));
  }

  %strategy Rename(Name x, Name y) extends `Identity() {
    visit Name {
      a -> { if (`a == x) return y;}
    }
  }

  private static Strategy TopDownRename(Name x, Name y) {
    return `mu(MuVar("x"), Choice(Is_nprop(),Sequence(Rename(x,y), All(MuVar("x")))));
  }

  private static boolean bindsNameOnTop(ProofTerm pt, Name z) {
    %match(ProofTerm pt) {
      cut(nprop(x,_),_,_,_) -> {return (`x == z);}
      andL(nprop(x,_),nprop(y,_),_,_) -> {return ((`x == z) || (`x == y));}
//      orL(nprop(x,_),_,nprop(y,_),_,_) -> {}
      implyR(nprop(x,_),_,_,_) -> {return (`x == z);}
      implyL(_,_,nprop(x,_),_,_) -> {return (`x == z);}
      existsL(nprop(x,_),_,_,_) -> {return (`x == z);}
      forallL(nprop(x,_),_,_,_) -> {return (`x == z);}

    }
    return false;
  }*/

  %strategy NameSubs(n:Name,cn:CoName,pt:ProofTerm, phi:Prop) extends `Fail() { // ATTENTION CAPTURE DE VARIABLE
    visit ProofTerm {
/*      andL(x,y,m,n1) -> {
        if (n == `n1) {
          return `cut(cnprop(cn,phi),pt,nprop(n,phi),andL(x,y,m,n1));
        }
      }
      orL(x,m1,y,m2,n1) -> {
        if (n == `n1) {
          return `cut(cnprop(cn,phi),pt,nprop(n,phi),orL(x,m1,y,m2,n1));
        }
      }
      */
      p -> {
        if (nameTopIntroduced(`p,n)) return `cut(cnprop(cn,phi),pt,nprop(n,phi),p);
      }
    }
  }

  %strategy NameSubsAx(n:Name,cn:CoName,pt:ProofTerm, phi:Prop) extends `Fail() { // ATTENTION CAPTURE DE VARIABLE
    visit ProofTerm {
      ax(n1,cn1) -> {
        if (n == `n1) return (ProofTerm) `ReCoName(pt,cn,cn1);
      }
    }
  }


  private static Strategy TopDownNameSubs(Name n,CoName cn,ProofTerm pt, Prop phi) {
    return `mu(MuVar("x"),Choice(Choice(NameSubsAx(n,cn,pt,phi),Sequence(NameSubs(n,cn,pt,phi),_cut(Identity(),Identity(),Identity(),All(MuVar("x"))))),All(MuVar("x"))));
  }

 %strategy CoNameSubs(cn:CoName,n:Name,pt:ProofTerm, phi:Prop) extends `Fail() { // ATTENTION CAPTURE DE VARIABLE
    visit ProofTerm {
/*      andL(x,y,m,n1) -> {
        if (n == `n1) {
          return `cut(cnprop(cn,phi),pt,nprop(n,phi),andL(x,y,m,n1));
        }
      }
      orL(x,m1,y,m2,n1) -> {
        if (n == `n1) {
          return `cut(cnprop(cn,phi),pt,nprop(n,phi),orL(x,m1,y,m2,n1));
        }
      }
      */
      p -> {
        if (conameTopIntroduced(`p,cn)) return `cut(cnprop(cn,phi),p,nprop(n,phi),pt); 
      }
    }
  }

  %strategy CoNameSubsAx(cn:CoName,n:Name,pt:ProofTerm, phi:Prop) extends `Fail() { // ATTENTION CAPTURE DE VARIABLE
    visit ProofTerm {
      ax(n1,cn1) -> {
        if (cn == `cn1) {
//          System.out.println("renommage "+n+" en "+`n1+" dans \n"+pt);
          ProofTerm pt2 = (ProofTerm) `ReName(pt,n,n1);
//          System.out.println(pt2);
          return pt2;
        }
      }
    }
  }


  private static Strategy TopDownCoNameSubs(CoName cn,Name n,ProofTerm pt, Prop phi) {
    return `mu(MuVar("x"),Choice(Choice(CoNameSubsAx(cn,n,pt,phi),Sequence(CoNameSubs(cn,n,pt,phi),_cut(Identity(),All(MuVar("x")),Identity(),Identity()))),All(MuVar("x"))));
  }

 private static ProofTerm CoNameSubstitution(ProofTerm subj, CoName cn, Name n, ProofTerm pt, Prop phi) {
    %match (ProofTerm subj) {
      p@ax(n1,cn1) -> {
        if (cn == `cn1) {
          System.out.println("renommage "+n+" en "+`n1+" dans \n"+PrettyPrinter.prettyPrint(pt));
          ProofTerm pt2 = null;
          try {
            pt2 = (ProofTerm) `ReName(pt,n,n1);
          }
          catch (Exception e) { System.out.println(e); }
          System.out.println(PrettyPrinter.prettyPrint(pt2));
          return pt2;
        }
        else return `p;
      }
      p -> {
        if (conameTopIntroduced(`p,cn)) return `cut(cnprop(cn,phi),CoNameSubstitutionDescend(p,cn,n,pt,phi),nprop(n,phi),pt);
        else return CoNameSubstitutionDescend(`p,cn,n,pt,phi);
      }
    }
    return null;
  }

  private static ProofTerm CoNameSubstitutionDescend(ProofTerm subj, CoName cn, Name n, ProofTerm pt, Prop phi) { // MANQUE TRUE ET FALSE ET PREMIER ORDRE
    %match (ProofTerm subj) {
      cut(a@cnprop(coname,_),m1,x,m2) -> {
        ProofTerm new_m1 = `m1;
        if (! (`coname == cn)) new_m1 = CoNameSubstitution(`m1,cn,n,pt,phi);
        ProofTerm new_m2 = CoNameSubstitution(`m2,cn,n,pt,phi);
        return `cut(a,new_m1,x,new_m2);
      }
      orR(a@cnprop(coname1,_),b@cnprop(coname2,_),m,coname) -> {
        ProofTerm new_m = `m;
        if ((! (`coname1 == cn)) && (! (`coname2 == cn))) new_m = CoNameSubstitution(`m,cn,n,pt,phi);
        return `orR(a,b,new_m,coname);
      }
      orL(x,m1,y,m2,name) -> {
        ProofTerm new_m1 = CoNameSubstitution(`m1,cn,n,pt,phi);
        ProofTerm new_m2 = CoNameSubstitution(`m2,cn,n,pt,phi);
        return `orL(x,new_m1,y,new_m2,name);

      }
      andR(a@cnprop(coname1,_),m1,b@cnprop(coname2,_),m2,coname) -> {
        ProofTerm new_m1 = `m1;
        if (! (`coname1 == cn)) new_m1 = CoNameSubstitution(`m1,cn,n,pt,phi);
        ProofTerm new_m2 = `m2;
        if (! (`coname2 == cn)) new_m2 = CoNameSubstitution(`m2,cn,n,pt,phi);
        return `andR(a,new_m1,b,new_m2,coname);
      }
      andL(x,y,m,name) -> {
        ProofTerm new_m = CoNameSubstitution(`m,cn,n,pt,phi);
        return `andL(x,y,new_m,name);
      }
      implyR(x,a@cnprop(coname1,_),m,coname) -> {
        ProofTerm new_m = `m;
        if (! (`coname1 == cn)) new_m = CoNameSubstitution(`m,cn,n,pt,phi);
        return `implyR(x,a,new_m,coname);
      }
      implyL(a@cnprop(coname,_),m1,x,m2,name) -> {
        ProofTerm new_m1 = `m1;
        if (! (`coname == cn)) new_m1 = CoNameSubstitution(`m1,cn,n,pt,phi);
        ProofTerm new_m2 = CoNameSubstitution(`m2,cn,n,pt,phi);
        return `implyL(a,new_m1,x,new_m2,name);
      }
//      existsL(nprop(x,_),_,_,_) -> {return (`x == z);}
//      forallL(nprop(x,_),_,_,_) -> {return (`x == z);}
    }
    return null;
  }

   private static ProofTerm NameSubstitution(ProofTerm subj, Name n, CoName cn, ProofTerm pt, Prop phi) {
    %match (ProofTerm subj) {
      p@ax(n1,cn1) -> {
        if (n == `n1) {
          System.out.println("renommage "+cn+" en "+`cn1+" dans \n"+PrettyPrinter.prettyPrint(pt));
          ProofTerm pt2 = null;
          try {
            pt2 = (ProofTerm) `ReCoName(pt,cn,cn1);
          }
          catch (Exception e) { System.out.println(e); }
          System.out.println(PrettyPrinter.prettyPrint(pt2));
          return pt2;
        }
        else return `p;
      }
      p -> {
        if (nameTopIntroduced(`p,n)) return `cut(cnprop(cn,phi),pt,nprop(n,phi),NameSubstitutionDescend(p,n,cn,pt,phi));
        else return NameSubstitutionDescend(`p,n,cn,pt,phi);
      }
    }
    return null;
  }
  
  private static ProofTerm NameSubstitutionDescend(ProofTerm subj, Name n, CoName cn, ProofTerm pt, Prop phi) { // MANQUE TRUE ET FALSE ET PREMIER ORDRE
    %match (ProofTerm subj) {
      cut(a,m1,x@nprop(name,_),m2) -> {
        ProofTerm new_m2 = `m2;
        if (! (`name == n)) new_m2 = NameSubstitution(`m2,n,cn,pt,phi);
        return `cut(a,NameSubstitution(m1,n,cn,pt,phi),x,new_m2);
      }
      andL(x@nprop(name1,_),y@nprop(name2,_),m,name) -> {
        ProofTerm new_m = `m;
        if ((! (`name1 == n)) && (! (`name2 == n))) new_m = NameSubstitution(`m,n,cn,pt,phi);
        return `andL(x,y,new_m,name);
      }
      andR(a,m1,b,m2,coname) -> {
        ProofTerm new_m1 = NameSubstitution(`m1,n,cn,pt,phi);
        ProofTerm new_m2 = NameSubstitution(`m2,n,cn,pt,phi);
        return `andR(a,new_m1,b,new_m2,coname);

      }
      orL(x@nprop(name1,_),m1,y@nprop(name2,_),m2,name) -> {
        ProofTerm new_m1 = `m1;
        if (! (`name1 == n)) new_m1 = NameSubstitution(`m1,n,cn,pt,phi);
        ProofTerm new_m2 = `m2;
        if (! (`name2 == n)) new_m2 = NameSubstitution(`m2,n,cn,pt,phi);
        return `orL(x,new_m1,y,new_m2,name);
      }
      orR(a,b,m,coname) -> {
        ProofTerm new_m = NameSubstitution(`m,n,cn,pt,phi);
        return `orR(a,b,new_m,coname);
      }
      implyR(x@nprop(name,_),a,m,coname) -> {
        ProofTerm new_m = `m;
        if (! (`name == n)) new_m = NameSubstitution(`m,n,cn,pt,phi);
        return `implyR(x,a,new_m,coname);
      }
      implyL(a,m1,x@nprop(name2,_),m2,name) -> {
        ProofTerm new_m2 = `m2;
        if (! (`name2 == n)) new_m2 = NameSubstitution(`m2,n,cn,pt,phi);
        ProofTerm new_m1 = NameSubstitution(`m1,n,cn,pt,phi);
        return `implyL(a,new_m1,x,new_m2,name);
      }
//      existsL(nprop(x,_),_,_,_) -> {return (`x == z);}
//      forallL(nprop(x,_),_,_,_) -> {return (`x == z);}
    }
    return null;
  }

 
  %strategy OneStep(subject:ProofTerm,c:Collection,refresher:VarGenerator) extends `Identity() {
    visit ProofTerm {
      // COMMUTING CUTS
      
      /*
      cut(cnprop(cn,phi),m1,x,m2) -> {
        if (! `conameAppearsFree(m1,cn)) {
          c.add(getEnvironment().getPosition().getReplace(`m1).visit(subject));
        }
      }

      cut(a,m1,nprop(n,phi),m2) -> {
        if (! `nameAppearsFree(m2,n)) {
          c.add(getEnvironment().getPosition().getReplace(`m2).visit(subject));
        }
      }
      */

      cut(cnprop(cn,phi),m1,nprop(n,phi),m2) -> { // ATTENTION CAPTURE DE VARIABLE
//        if ((! `nameFreshlyIntroduced(m2,n)) && (`boundNamesAreNotFree(m2,m1)) && (`boundCoNamesAreNotFree(m2,m1))) {
        if (! `nameFreshlyIntroduced(m2,n)) {
          ProofTerm new_m2 = refresher.refreshBoundVars(`m2);
          System.out.println("\n refreshing bound vars of");
          System.out.println(PrettyPrinter.prettyPrint(`m2));
          System.out.println("into");
          System.out.println(PrettyPrinter.prettyPrint(new_m2));
          ProofTerm mm = (ProofTerm) `NameSubstitution(new_m2,n,cn,m1,phi);
          c.add(getEnvironment().getPosition().getReplace(mm).visit(subject));
        }
      }

      cut(cnprop(cn,phi),m1,nprop(n,phi),m2) -> { // ATTENTION CAPTURE DE VARIABLE
        if ((! `conameFreshlyIntroduced(m1,cn)) && (`boundNamesAreNotFree(m1,m2)) && (`boundCoNamesAreNotFree(m1,m2))) {
          ProofTerm new_m1 = refresher.refreshBoundVars(`m1);
          System.out.println("\n refreshing bound vars of");
          System.out.println(PrettyPrinter.prettyPrint(`m1));
          System.out.println("into");
          System.out.println(PrettyPrinter.prettyPrint(new_m1));
          ProofTerm mm = (ProofTerm) `CoNameSubstitution(new_m1,cn,n,m2,phi);
          c.add(getEnvironment().getPosition().getReplace(mm).visit(subject));
        }
      }

      // LOGICAL CUTS
      cut(cnprop(cn2,phi),m,nprop(n,phi),ax(n,cn)) -> { // ATTENTION
        if (`conameFreshlyIntroduced(m,cn2)) {
          ProofTerm mm = (ProofTerm) `ReCoName(m,cn2,cn); 
          c.add(getEnvironment().getPosition().getReplace(mm).visit(subject));
        }
      }
      cut(cnprop(cn,phi),ax(n,cn),nprop(n2,phi),m) -> { // ATTENTION
        if (`nameFreshlyIntroduced(m,n2)) {
          ProofTerm mm = (ProofTerm) `ReName(m,n2,n); 
          c.add(getEnvironment().getPosition().getReplace(mm).visit(subject));}
      }
      cut(cnprop(cn,phi),p1@andR(a1,m1,a2,m2,cn),nprop(n,phi),p2@andL(x1,x2,m3,n)) -> {
        if (`conameFreshlyIntroduced(p1,cn) && `nameFreshlyIntroduced(p2,n)) {
          c.add(getEnvironment().getPosition().getReplace(`cut(a2,m2,x2,cut(a1,m1,x1,m3))).visit(subject));
          c.add(getEnvironment().getPosition().getReplace(`cut(a1,m1,x1,cut(a2,m2,x2,m3))).visit(subject));
        }
      }
      cut(cnprop(cn,phi),p1@orR(a1,a2,m1,cn),nprop(n,phi),p2@orL(x1,m2,x2,m3,n)) -> {
        if (`conameFreshlyIntroduced(p1,cn) && `nameFreshlyIntroduced(p2,n)) {
          c.add(getEnvironment().getPosition().getReplace(`cut(a1,cut(a2,m1,x2,m3),x1,m2)).visit(subject));
          c.add(getEnvironment().getPosition().getReplace(`cut(a2,cut(a1,m1,x1,m2),x2,m3)).visit(subject));
        }
      }
      cut(cnprop(cn,phi),p1@implyR(x1,a1,m1,cn),nprop(n,phi),p2@implyL(a2,m2,x2,m3,n)) -> {
        if (`conameFreshlyIntroduced(p1,cn) && `nameFreshlyIntroduced(p2,n)) {
          c.add(getEnvironment().getPosition().getReplace(`cut(a2,m2,x1,cut(a1,m1,x2,m3))).visit(subject));
          c.add(getEnvironment().getPosition().getReplace(`cut(a1,cut(a2,m2,x1,m1),x2,m3)).visit(subject));
        }
      }
      cut(cnprop(cn,phi),p1@trueR(cn),nprop(n,phi),p2) -> {
          c.add(getEnvironment().getPosition().getReplace(`p2).visit(subject));
      }
      cut(cnprop(cn,phi),p1,nprop(n,phi),p2@falseL(n)) -> {
          c.add(getEnvironment().getPosition().getReplace(`p1).visit(subject));
      }
    }
  }

  public static Collection reduce(ProofTerm t, VarGenerator refresher) {
    Collection c = new ArrayList();
    try {
      `TopDown(OneStep(t,c, refresher)).visit(t);
    }
    catch (VisitFailure e) { System.out.println(e); }
    return c;
  }

  public static Collection computeNormalForms(ProofTerm t, NSequent nseq) {
    VarGenerator refresher = new VarGenerator();
    refresher.synchronize(t);
    Collection nf = new ArrayList();
    Collection c = new ArrayList();
    c.add(t);
    while (true) {
      Collection buffer = new ArrayList();
      for (Object x : c) {
        System.out.println("\n----\n"+PrettyPrinter.prettyPrint((ProofTerm) x));
        Collection d = reduce((ProofTerm) x, refresher);
        for (Object y : d) { System.out.println("\nreduces to \n"+PrettyPrinter.prettyPrint((ProofTerm) y)); typeTypableProofterm(`typablePT((ProofTerm) y, nseq)); }
        if (d.isEmpty()) {
          nf.add(x);
          System.out.println(nf.size()+" normal forms found");
        }
        else buffer.addAll(d);
      }
      if (buffer.isEmpty()) break;
      else c=buffer;
    }
    return nf;
  }

  %strategy GetBoundCoNamesAux(c:Collection) extends Identity() {
    visit CNProp {
      cnprop(x,_) -> {c.add(`x); }
    }
  }

  private static Strategy getBoundCoNames(Collection c) {
    return `TopDown(GetBoundCoNamesAux(c));
  }

  private static boolean boundCoNamesAreNotFree(ProofTerm m1, ProofTerm m2) {
    Collection c = new HashSet();
    try {
      getBoundCoNames(c).visit(m1);
    }
    catch (Exception e) { System.out.println(e);}
    boolean res = true;
    for (Object o : c) {
      if (conameAppearsFree(m2,(CoName) o)) { res=false; } 
    }
    return res;
  }

  %strategy GetBoundNamesAux(c:Collection) extends Identity() {
    visit NProp {
      nprop(x,_) -> {c.add(`x); }
    }
  }

  private static Strategy getBoundNames(Collection c) {
    return `TopDown(GetBoundNamesAux(c));
  }

  private static boolean boundNamesAreNotFree(ProofTerm m1, ProofTerm m2) {
    Collection c = new HashSet();
    try {
      getBoundNames(c).visit(m1);
    }
    catch (Exception e) { System.out.println(e);}
    boolean res = true;
    for (Object o : c) {
      if (nameAppearsFree(m2,(Name) o)) { res=false;}
    }
    return res;
  }

  %strategy GetCoNamesAux(c:Collection) extends Identity() {
    visit CoName {
      coname(x) -> {c.add(`x); }
    }
  }

  private static Strategy getCoNames(Collection c) {
    return `TopDown(GetCoNamesAux(c));
  }

  %strategy GetNamesAux(c:Collection) extends Identity() {
    visit Name {
      name(x) -> {c.add(`x); }
    }
  }

  private static Strategy getNames(Collection c) {
    return `TopDown(GetNamesAux(c));
  }

  private static class VarGenerator {
    private int name_int=0;
    private int coname_int=0;

    public Name newName() {
      return `name(name_int++);
    }

    public CoName newCoName() {
      return `coname(coname_int++);
    }

    public void synchronize(ProofTerm pt) {
      Collection<Integer> c = new HashSet<Integer>();
      try {
        getNames(c).visit(pt);
      }
      catch (Exception e) {System.out.println(e);}
      name_int = Collections.max(c)+1 ;
      c = new HashSet<Integer>();
      try {
        getCoNames(c).visit(pt);
      }
      catch (Exception e) {System.out.println(e);}
      coname_int = Collections.max(c)+1;
    }

    public ProofTerm refreshBoundVars(ProofTerm pt) { // INCOMPLET MANQUE 1er ORDRE
      %match(ProofTerm pt) {
        ax(_,_) -> {return pt; }
        cut(cnprop(cn1,phi),m1,nprop(n1,phi),m2) -> {
          CoName new_cn1 = newCoName();
          ProofTerm new_m1 = `ReCoName(m1,cn1,new_cn1);
          Name new_n1 = newName();
          ProofTerm new_m2 = `ReName(m2,n1,new_n1);
          new_m1 = refreshBoundVars(new_m1);
          new_m2 = refreshBoundVars(new_m2);
          return `cut(cnprop(new_cn1,phi),new_m1,nprop(new_n1,phi),new_m2);
        }
        andL(nprop(n1,phi1),nprop(n2,phi2),m,n) -> {
          Name new_n1 = newName();
          Name new_n2 = newName();
          ProofTerm new_m = `ReName(m,n1,new_n1);
          new_m = `ReName(new_m,n2,new_n2);
          new_m = refreshBoundVars(new_m);
          return `andL(nprop(new_n1,phi1),nprop(new_n2,phi2), new_m, n);
        }
        andR(cnprop(cn1,phi1),m1,cnprop(cn2,phi2),m2,cn) -> {
          CoName new_cn1 = newCoName();
          CoName new_cn2 = newCoName();
          ProofTerm new_m1 = `ReCoName(m1,cn1,new_cn1);
          ProofTerm new_m2 = `ReCoName(m2,cn2,new_cn2);
          new_m1 = refreshBoundVars(new_m1);
          new_m2 = refreshBoundVars(new_m2);
          return `andR(cnprop(new_cn1,phi1),new_m1,cnprop(new_cn2,phi2),new_m2,cn);
        }
        orR(cnprop(cn1,phi1),cnprop(cn2,phi2),m,cn) -> {
          CoName new_cn1 = newCoName();
          CoName new_cn2 = newCoName();
          ProofTerm new_m = `ReCoName(m,cn1,new_cn1);
          new_m = `ReCoName(new_m,cn2,new_cn2);
          new_m = refreshBoundVars(new_m);
          return `orR(cnprop(new_cn1,phi1),cnprop(new_cn2,phi2), new_m, cn);
        }
        orL(nprop(n1,phi1),m1,nprop(n2,phi2),m2,n) -> {
          Name new_n1 = newName();
          Name new_n2 = newName();
          ProofTerm new_m1 = `ReName(m1,n1,new_n1);
          ProofTerm new_m2 = `ReName(m2,n2,new_n2);
          new_m1 = refreshBoundVars(new_m1);
          new_m2 = refreshBoundVars(new_m2);
          return `orL(nprop(new_n1,phi1),new_m1,nprop(new_n2,phi2),new_m2,n);
        }
        implyL(cnprop(cn1,phi1),m1,nprop(n2,phi2),m2,n) -> {
          CoName new_cn1 = newCoName();
          ProofTerm new_m1 = `ReCoName(m1,cn1,new_cn1);
          Name new_n2 = newName();
          ProofTerm new_m2 = `ReName(m2,n2,new_n2);
          new_m1 = refreshBoundVars(new_m1);
          new_m2 = refreshBoundVars(new_m2);
          return `implyL(cnprop(new_cn1,phi1),new_m1,nprop(new_n2,phi2),new_m2,n);
        }
        implyR(nprop(n1,phi1),cnprop(cn2,phi2),m1,cn) -> {
          CoName new_cn2 = newCoName();
          Name new_n1 = newName();
          ProofTerm new_m1 = `ReCoName(m1,cn2,new_cn2);
          new_m1 = `ReName(new_m1,n1,new_n1);
          new_m1 = refreshBoundVars(new_m1);
          return `implyR(nprop(new_n1,phi1),cnprop(new_cn2,phi2),new_m1,cn);
         }
       }
      return null;
    }
  }

}
