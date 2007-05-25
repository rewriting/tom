import sequents.*;
import sequents.types.*;

import urban.*;
import urban.types.*;

import tom.library.sl.*;

import java.io.*;
import antlr.*;
import antlr.collections.*;

import java.util.Collection;

public class Proofterms {
//  %include { sequents/sequents.tom }
  %include { urban/urban.tom }

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
    if (isImplicitContraction(pi)) {
//      System.out.println("implicit contraction detected");
      %match (ProofTerm pi, NSequent nseq) {
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

  public static boolean isImplicitContraction(ProofTerm term) { // INCOMPLET MANQUE conames
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

  // Reductions des termes
/*
  %typeterm Collection {
    implement { Collection }
    is_sort(t) { t instanceof Collection }
  }

  public static ProofTerm reconame(CoName a, CoName b, ProofTerm m) {
    return m;
  }

  %strategy OneStep(subject:ProofTerm,c:Collection) extends `Identity() {
    visit ProofTerm {
      cut(cnprop(cn2,phi),m,nprop(n,phi),ax(n,cn)) -> { c.add(((Position) subject).getReplace(this, `reconame(cn2,cn,m)).visit(subject));}
    }
  }
*/
  
}
