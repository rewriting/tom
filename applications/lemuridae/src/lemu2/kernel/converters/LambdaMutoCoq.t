package lemu2.kernel.converters;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.coc.types.*;
import lemu2.kernel.*;
import lemu2.util.*;
import fj.data.List;
import fj.data.Option;
import fj.pre.*;
import fj.*;

public class LambdaMutoCoq {
  
  %include { kernel/proofterms/proofterms.tom } 
  %include { kernel/coc/coc.tom }

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
  private static CoCTerm ex  = `cocConst("ex");
  private static CoCTerm False  = `cocConst("False");
  private static CoCTerm True  = `cocConst("True");
  private static CoCTerm land  = `cocConst("and");
  private static CoCTerm lor  = `cocConst("or");
  private static CoCTerm or_introl  = `cocConst("@or_introl");
  private static CoCTerm or_intror  = `cocConst("@or_intror");
  private static CoCTerm or_ind  = `cocConst("or_ind");
  private static CoCTerm conj  = `cocConst("conj");
  private static CoCTerm and_ind = `cocConst("and_ind");

  private static CoCTerm apply(CoCTerm t, TermList tl, List<P2<FoVar,CoCAtom>> fomap) {
    %match(tl) {
      termList() -> { return t; }
      termList(u,us*) -> {
        return `apply(cocApp(t,convert(u,fomap)),us,fomap);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static CoCTerm convert(Term t, List<P2<FoVar,CoCAtom>> fomap) {
    %match(t) {
      var(x) -> {
        Option<CoCAtom> xx = folookup.`f(fomap,x);
        if (xx.isNone()) throw new RuntimeException("conversion error");
        else return `cocVar(xx.some());
      }
      funApp(f,tl) -> {
        return `apply(cocConst(f),tl,fomap);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static CoCTerm convert(Prop ty, List<P2<FoVar,CoCAtom>> fomap) {
    %match(ty) {
      relApp(R,tl) -> { return `apply(cocConst(R),tl,fomap); }
      implies(A,B) -> { 
        CoCAtom fresh = fresh("foo");
        return `cocPi(CoCPi(fresh,convert(A,fomap),convert(B,fomap)));
      }
      forall(Fa(fx,A)) -> {
        CoCAtom fresh = `fresh(fx);
        return `cocPi(CoCPi(fresh,iota,convert(A,fomap)));
      }
      exists(Ex(fx,A)) -> {
        CoCAtom fresh = `fresh(fx);
        return `cocApp(cocApp(ex,iota),cocPi(CoCPi(fresh,iota,convert(A,fomap))));
      }
      and(A,B) -> {
        return `cocApp(cocApp(land,convert(A,fomap)),convert(B,fomap));
      }
      or(A,B) -> {
        return `cocApp(cocApp(lor,convert(A,fomap)),convert(B,fomap));
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
      List<P2<FoVar,CoCAtom>> fomap) {
    %match(t) {
      lvar(x) -> { 
        Option<CoCAtom> xx = nlookup.`f(nmap,x);
        if (xx.isNone()) throw new RuntimeException("conversion error");
        else return `cocVar(xx.some());
      }
      lam(Lam(x,ty,u)) -> {
        CoCAtom xx = `fresh(x);
        return `cocLam(CoCLam(xx,convert(ty,fomap),convert(u,nmap.cons(P.p(x,xx)),cnmap,fomap)));
      }
      lapp(u,v) -> {
        return `cocApp(convert(u,nmap,cnmap,fomap),convert(v,nmap,cnmap,fomap));
      }
      flam(FLam(x,u)) -> {
        CoCAtom xx = `fresh(x);
        return `cocLam(CoCLam(xx,iota,convert(u,nmap,cnmap,fomap.cons(P.p(x,xx)))));
      }
      fapp(u,ft) -> {
        return `cocApp(convert(u,nmap,cnmap,fomap),convert(ft,fomap));
      }
      activ(Act(a,pa,u)) -> {
        CoCAtom aa = `fresh(a);
        CoCTerm ty = `convert(pa,fomap);
        CoCTerm not_ty = `cocApp(not,ty);
        return 
          `cocApp(cocApp(nnpp,ty),
              cocLam(CoCLam(aa,not_ty,convert(u,nmap,cnmap.cons(P.p(a,aa)),fomap))));
      }
      passiv(a,u) -> {
        Option<CoCAtom> aa = cnlookup.`f(cnmap,a);
        if (aa.isNone()) throw new RuntimeException("conversion error");
        else return `cocApp(cocVar(aa.some()),convert(u,nmap,cnmap,fomap));
      }
      left(u) -> {
        return `cocApp(cocApp(cocApp(or_introl,cocConst("_")),cocConst("_")),convert(u,nmap,cnmap,fomap));
      }
      right(u) -> {
        return `cocApp(cocApp(cocApp(or_intror,cocConst("_")),cocConst("_")),convert(u,nmap,cnmap,fomap));
      }
      caseof(s,Alt(x,px,u),Alt(y,py,v)) -> {
        CoCAtom xx = `fresh(x);
        CoCAtom yy = `fresh(y);
        return `cocApp(cocApp(cocApp(or_ind,
                  cocLam(CoCLam(xx,convert(px,fomap),convert(u,nmap.cons(P.p(x,xx)),cnmap,fomap)))),
                  cocLam(CoCLam(yy,convert(py,fomap),convert(v,nmap.cons(P.p(y,yy)),cnmap,fomap)))),
                  convert(s,nmap,cnmap,fomap));
      }
      pair(u,v) -> {
        return `cocApp(cocApp(conj,
                  convert(u,nmap,cnmap,fomap)),
                  convert(v,nmap,cnmap,fomap));
      }
      proj1(u) -> {
        CoCAtom x = `fresh("x");
        CoCAtom y = `fresh("y");
        CoCTerm un = `cocConst("_");
        return `cocApp(cocApp(
                  and_ind,
                  cocLam(CoCLam(x,un,cocLam(CoCLam(y,un,cocVar(x)))))),
                  convert(u,nmap,cnmap,fomap));
      }
      proj2(u) -> {
        CoCAtom x = `fresh("x");
        CoCAtom y = `fresh("y");
        CoCTerm un = `cocConst("_");
        return `cocApp(cocApp(
                  and_ind,
                  cocLam(CoCLam(x,un,cocLam(CoCLam(y,un,cocVar(y)))))),
                  convert(u,nmap,cnmap,fomap));
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static CoCTerm convert(LTerm t) {
    return convert(
        t,
        List.<P2<Name,CoCAtom>>nil(),
        List.<P2<CoName,CoCAtom>>nil(),
        List.<P2<FoVar,CoCAtom>>nil());
  }

}


