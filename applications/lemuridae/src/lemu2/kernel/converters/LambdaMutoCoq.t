package lemu2.kernel.converters;

import tom.library.sl.*;
import java.util.HashMap;
import java.util.Map.Entry;
import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.coc.types.*;
import lemu2.kernel.*;
import lemu2.util.*;
import fj.data.List;
import fj.data.Option;
import fj.pre.*;
import fj.*;

public class LambdaMutoCoq {
  
  %include { sl.tom }
  %include { kernel/proofterms/proofterms.tom } 
  %include { kernel/coc/coc.tom }

  %typeterm ArityMap { implement { HashMap<String,P2<Integer,CoCAtom>> } }

  %strategy Arities(ArityMap funs, ArityMap preds) extends Identity() {
    visit Term { funApp(f,l) -> { funs.`put(f,P.p(l.length(),fresh(f))); } }
    visit Prop { relApp(r,l) -> { preds.`put(r,P.p(l.length(),fresh(r))); } }
  }

  private static P2<HashMap<String,P2<Integer,CoCAtom>>,HashMap<String,P2<Integer,CoCAtom>>> 
    getArities(LTerm pt) {
      HashMap<String,P2<Integer,CoCAtom>> funs = new HashMap<String,P2<Integer,CoCAtom>>();
      HashMap<String,P2<Integer,CoCAtom>> preds = new HashMap<String,P2<Integer,CoCAtom>>();
      try { `TopDown(Arities(funs,preds)).visit(pt); }
      catch (VisitFailure e) { throw new RuntimeException("never happens"); }
      return P.p(funs,preds);
    }

  private static CoCTerm arityFun(int n) {
    if (n == 0) return iota;
    else {
      CoCAtom foo = fresh("foo");
      return `cocPi(CoCPi(foo,iota,arityFun(n-1)));
    }
  }

  private static CoCTerm arityPred(int n) {
    if (n == 0) return prop;
    else {
      CoCAtom foo = fresh("foo");
      return `cocPi(CoCPi(foo,iota,arityPred(n-1)));
    }
  }

  private static CoCTerm 
    prependFuns(HashMap<String,P2<Integer,CoCAtom>> funs, CoCTerm t) {
      for (Entry<String,P2<Integer,CoCAtom>> e: funs.entrySet()) {
        String f = e.getKey();
        P2<Integer,CoCAtom> ar_ff = e.getValue();
        int ar = ar_ff._1();
        CoCAtom ff = ar_ff._2();
        t = `cocLam(CoCLam(ff,arityFun(ar),t));
      }
      return t;
  }

  private static CoCTerm 
    prependPreds(HashMap<String,P2<Integer,CoCAtom>> preds, CoCTerm t) {
      for (Entry<String,P2<Integer,CoCAtom>> e: preds.entrySet()) {
        String p = e.getKey();
        P2<Integer,CoCAtom> ar_pp = e.getValue();
        int ar = ar_pp._1();
        CoCAtom pp = ar_pp._2();
        t = `cocLam(CoCLam(pp,arityPred(ar),t));
      }
      return t;
  }

  private static F2<List<P2<Name,CoCAtom>>,Name,Option<CoCAtom>> 
    nlookup = List.<Name,CoCAtom>lookup(Equal.<Name>anyEqual());

  private static F2<List<P2<CoName,CoCAtom>>,CoName,Option<CoCAtom>> 
    cnlookup = List.<CoName,CoCAtom>lookup(Equal.<CoName>anyEqual());

  private static F2<List<P2<FoVar,CoCAtom>>,FoVar,Option<CoCAtom>> 
    folookup = List.<FoVar,CoCAtom>lookup(Equal.<FoVar>anyEqual());

  private static CoCAtom fresh(String x) {
    return CoCAtom.freshCoCAtom(x);
  }
  private static CoCAtom fresh(Name x) {
    return fresh(x.gethint());
  }
  private static CoCAtom fresh(CoName a) {
    return fresh(a.gethint());
  }
  private static CoCAtom fresh(FoVar fx) {
    return fresh(fx.gethint());
  }

  private static CoCTerm prop = `cocConst("Prop");
  private static CoCTerm set  = `cocConst("Set");
  private static CoCTerm type = `cocConst("Type");
  private static CoCTerm nnpp = `cocConst("NNPP");
  private static CoCTerm iota = `cocConst("iota");
  private static CoCTerm not  = `cocConst("not");
  private static CoCTerm ex = `cocConst("ex");
  private static CoCTerm ex_intro = `cocConst("ex_intro");
  private static CoCTerm ex_ind = `cocConst("ex_ind");
  private static CoCTerm False = `cocConst("False");
  private static CoCTerm True = `cocConst("True");
  private static CoCTerm land = `cocConst("and");
  private static CoCTerm lor = `cocConst("or");
  private static CoCTerm or_introl = `cocConst("@or_introl");
  private static CoCTerm or_intror = `cocConst("@or_intror");
  private static CoCTerm or_ind = `cocConst("or_ind");
  private static CoCTerm conj = `cocConst("conj");
  private static CoCTerm and_ind = `cocConst("and_ind");
  private static CoCTerm un = `cocConst("_");

  private static CoCTerm apply(
      CoCTerm t, 
      TermList tl, 
      List<P2<FoVar,CoCAtom>> fomap,
      HashMap<String,P2<Integer,CoCAtom>> funs,
      HashMap<String,P2<Integer,CoCAtom>> preds) {
    %match(tl) {
      termList() -> { return t; }
      termList(u,us*) -> {
        return `apply(cocApp(t,convert(u,fomap,funs,preds)),us,fomap,funs,preds);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static CoCTerm convert(
      Term t, 
      List<P2<FoVar,CoCAtom>> fomap,
      HashMap<String,P2<Integer,CoCAtom>> funs,
      HashMap<String,P2<Integer,CoCAtom>> preds) {
    %match(t) {
      var(x) -> {
        Option<CoCAtom> xx = folookup.`f(fomap,x);
        if (xx.isNone()) throw new RuntimeException("conversion error");
        else return `cocVar(xx.some());
      }
      funApp(f,tl) -> {
        CoCAtom ff = funs.get(`f)._2();
        return `apply(cocVar(ff),tl,fomap,funs,preds);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static CoCTerm convert(
      Prop ty, 
      List<P2<FoVar,CoCAtom>> fomap,
      HashMap<String,P2<Integer,CoCAtom>> funs,
      HashMap<String,P2<Integer,CoCAtom>> preds) {
    %match(ty) {
      relApp(R,tl) -> { 
        CoCAtom RR = preds.get(`R)._2();
        return `apply(cocVar(RR),tl,fomap,funs,preds); 
      }
      implies(A,B) -> { 
        CoCAtom fresh = fresh("foo");
        return `cocPi(CoCPi(fresh,convert(A,fomap,funs,preds),convert(B,fomap,funs,preds)));
      }
      forall(Fa(fx,A)) -> {
        CoCAtom fresh = `fresh(fx);
        return `cocPi(CoCPi(fresh,iota,convert(A,fomap.cons(P.p(fx,fresh)),funs,preds)));
      }
      exists(Ex(fx,A)) -> {
        CoCAtom fresh = `fresh(fx);
        return `cocApp(ex,cocLam(CoCLam(fresh,iota,
                             convert(A,fomap.cons(P.p(fx,fresh)),funs,preds))));
      }
      and(A,B) -> {
        return `cocApp(cocApp(land,convert(A,fomap,funs,preds)),convert(B,fomap,funs,preds));
      }
      or(A,B) -> {
        return `cocApp(cocApp(lor,convert(A,fomap,funs,preds)),convert(B,fomap,funs,preds));
      }
      bottom() -> { return False; }
      top()    -> { return True; }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static CoCTerm convert(
      LTerm t, 
      List<P2<Name,CoCAtom>> nmap,
      List<P2<CoName,CoCAtom>> cnmap,
      List<P2<FoVar,CoCAtom>> fomap,
      HashMap<String,P2<Integer,CoCAtom>> funs,
      HashMap<String,P2<Integer,CoCAtom>> preds) {
    %match(t) {
      lvar(x) -> { 
        Option<CoCAtom> xx = nlookup.`f(nmap,x);
        if (xx.isNone()) throw new RuntimeException("conversion error");
        else return `cocVar(xx.some());
      }
      lam(Lam(x,ty,u)) -> {
        CoCAtom xx = `fresh(x);
        return `cocLam(CoCLam(xx,convert(ty,fomap,funs,preds),
                  convert(u,nmap.cons(P.p(x,xx)),cnmap,fomap,funs,preds)));
      }
      lapp(u,v) -> {
        return `cocApp(convert(u,nmap,cnmap,fomap,funs,preds),convert(v,nmap,cnmap,fomap,funs,preds));
      }
      flam(FLam(x,u)) -> {
        CoCAtom xx = `fresh(x);
        return `cocLam(CoCLam(xx,iota,convert(u,nmap,cnmap,fomap.cons(P.p(x,xx)),funs,preds)));
      }
      fapp(u,ft) -> {
        return `cocApp(convert(u,nmap,cnmap,fomap,funs,preds),convert(ft,fomap,funs,preds));
      }
      activ(Act(a,pa,u)) -> {
        CoCAtom aa = `fresh(a);
        CoCTerm ty = `convert(pa,fomap,funs,preds);
        CoCTerm not_ty = `cocApp(not,ty);
        return 
          `cocApp(cocApp(nnpp,ty),
              cocLam(CoCLam(aa,not_ty,convert(u,nmap,cnmap.cons(P.p(a,aa)),fomap,funs,preds))));
      }
      passiv(a,u) -> {
        Option<CoCAtom> aa = cnlookup.`f(cnmap,a);
        if (aa.isNone()) throw new RuntimeException("conversion error");
        else return `cocApp(cocVar(aa.some()),convert(u,nmap,cnmap,fomap,funs,preds));
      }
      left(u,p) -> {
        return `cocApp(cocApp(cocApp(
                  or_introl,
                  un),
                  convert(p,fomap,funs,preds)),
                  convert(u,nmap,cnmap,fomap,funs,preds));
      }
      right(u,p) -> {
        return `cocApp(cocApp(cocApp(
                  or_intror,
                  convert(p,fomap,funs,preds)),
                  un),
                  convert(u,nmap,cnmap,fomap,funs,preds));
      }
      caseof(s,Alt(x,px,u),Alt(y,py,v)) -> {
        CoCAtom xx = `fresh(x);
        CoCAtom yy = `fresh(y);
        return `cocApp(cocApp(cocApp(or_ind,
                  cocLam(CoCLam(xx,convert(px,fomap,funs,preds),
                      convert(u,nmap.cons(P.p(x,xx)),cnmap,fomap,funs,preds)))),
                  cocLam(CoCLam(yy,convert(py,fomap,funs,preds),
                      convert(v,nmap.cons(P.p(y,yy)),cnmap,fomap,funs,preds)))),
                  convert(s,nmap,cnmap,fomap,funs,preds));
      }
      pair(u,v) -> {
        return `cocApp(cocApp(conj,
                  convert(u,nmap,cnmap,fomap,funs,preds)),
                  convert(v,nmap,cnmap,fomap,funs,preds));
      }
      proj1(u) -> {
        CoCAtom x = `fresh("x");
        CoCAtom y = `fresh("y");
        return `cocApp(cocApp(
                  and_ind,
                  cocLam(CoCLam(x,un,cocLam(CoCLam(y,un,cocVar(x)))))),
                  convert(u,nmap,cnmap,fomap,funs,preds));
      }
      proj2(u) -> {
        CoCAtom x = `fresh("x");
        CoCAtom y = `fresh("y");
        return `cocApp(cocApp(
                  and_ind,
                  cocLam(CoCLam(x,un,cocLam(CoCLam(y,un,cocVar(y)))))),
                  convert(u,nmap,cnmap,fomap,funs,preds));
      }
      witness(ft,u,p) -> {
        %match(p) {
          exists(Ex(fx,A)) -> {
            CoCAtom fxx = `fresh(fx);
            return `cocApp(cocApp(cocApp(
                      ex_intro,
                      cocLam(CoCLam(fxx,iota,convert(A,fomap.cons(P.p(fx,fxx)),funs,preds)))),
                      convert(ft,fomap,funs,preds)),
                      convert(u,nmap,cnmap,fomap,funs,preds));
          }
        }
      }
      letin(Letin(fx,x,px,u,v)) -> {
        CoCAtom fxx = `fresh(fx);
        CoCAtom xx  = `fresh(x);
        List<P2<FoVar,CoCAtom>> fomap1 = fomap.`cons(P.p(fx,fxx));
        List<P2<Name,CoCAtom>> nmap1 = nmap.`cons(P.p(x,xx));
        return `cocApp(cocApp(
                 ex_ind,
                 cocLam(CoCLam(fxx,iota,
                    cocLam(CoCLam(xx,convert(px,fomap1,funs,preds),convert(u,nmap1,cnmap,fomap1,funs,preds)))))),
                 convert(v,nmap,cnmap,fomap,funs,preds));
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static CoCTerm convert(
      LTerm t,
      HashMap<String,P2<Integer,CoCAtom>> funs,
      HashMap<String,P2<Integer,CoCAtom>> preds) {
    return convert(
        t,
        List.<P2<Name,CoCAtom>>nil(),
        List.<P2<CoName,CoCAtom>>nil(),
        List.<P2<FoVar,CoCAtom>>nil(),
        funs,
        preds);
  }

  public static CoCTerm convert(LTerm t) {
    P2<HashMap<String,P2<Integer,CoCAtom>>,HashMap<String,P2<Integer,CoCAtom>>> 
      funs_pred = getArities(t);
    HashMap<String,P2<Integer,CoCAtom>> funs = funs_pred._1();
    HashMap<String,P2<Integer,CoCAtom>> preds = funs_pred._2();
    CoCTerm res = convert(t,funs,preds);
    res = prependFuns(funs,res);
    res = prependPreds(preds,res);
    return res;
  }

}


