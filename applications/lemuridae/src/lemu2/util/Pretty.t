package lemu2.util;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.coc.types.*;

public class Pretty {

  %include { kernel/proofterms/proofterms.tom } 
  %include { kernel/coc/coc.tom } 

  private static String pr(Name var, Prop prop) {
    return churchStyle ? %[@var.gethint() + var.getn()@:@pretty(prop)@]% : %[@var@]%;
  }

  private static String pr(CoName var, Prop prop) {
    return churchStyle ? %[@var.gethint() + var.getn()@:@pretty(prop)@]% : %[@var@]%;
  }

  public static String pretty(LTerm t) {
    %match(t) {
      lvar(Name(i,n)) -> { return `n + `i; }
      lam(Lam(x,ty,u)) -> { return "\u03BB" + %[@`pr(x,ty)@.@`pretty(u)@]%; }
      flam(FLam(x,u)) -> { return "\u03BC" + %[@`x@.@`pretty(u)@]%; }
      activ(Act(x,ty,u)) -> { return "\u03BC" + %[@`pr(x,ty)@.@`pretty(u)@]%; }
      lapp(u@(lapp|fapp|lvar)[],v) -> { 
        return %[@`pretty(u)@ @`pretty(v)@]%; 
      }
      lapp(u,v) -> { return %[(@`pretty(u)@) @`pretty(v)@]%; }
      fapp(u@(lapp|fapp|lvar)[],v) -> { 
        return %[@`pretty(u)@ @`pretty(v)@]%; 
      }
      fapp(u,v) -> { return %[(@`pretty(u)@) @`pretty(v)@]%; }
      pair(u,v) -> { return %[(@`pretty(u)@,@`pretty(v)@)]%; }
      proj1(u) -> { return %[fst @`pretty(u)@]%; }
      proj2(u) -> { return %[snd @`pretty(u)@]%; }
      caseof(u,Alt(x,px,v),Alt(y,py,w)) -> { 
        return %[(case @`pretty(u)@ of (left @`pr(x,px)@) -> @`pretty(v)@ | (right @`pr(y,py)@) -> @`pretty(w)@)]%;
      }
      //letin(Letin(fx,x,px,u,v)) -> { 
      letin(Letin(fx,x,_,u,v)) -> { 
        return %[(let <@`fx@,@`x@> = @`pretty(u)@ in @`pretty(v)@)]%;
      }
      witness(ft,u,p) -> { return %[(<@`pretty(ft)@,@`pretty(u)@>:@`pretty(p)@)]%; }
      left(u,p) -> { return %[left{@`pretty(p)@} @`pretty(u)@]%; }
      right(u,p) -> { return %[right{@`pretty(p)@} @`pretty(u)@]%; }
      passiv(CoName(i,mv),lvar(Name(j,x))) -> { return %[[@`mv+`i@]@`x + `j@]%; }
      passiv(CoName(i,mv),u) -> { return %[[@`mv+`i@](@`pretty(u)@)]%; }
      unit() -> { return "()"; }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  /* raw lambda-mu terms */

  private static String pr(String var, RawProp prop) {
    return churchStyle ? %[@var@:@pretty(prop)@]% : %[@var@]%;
  }

  public static String pretty(RawLTerm t) {
    %match(t) {
      Rawlvar(v) -> { return `v; }
      Rawlam(RawLam(x,ty,u)) -> { return "\u03BB" + %[@`pr(x,ty)@.@`pretty(u)@]%; }
      Rawflam(RawFLam(x,u)) -> { return "\u03BC" + %[@`x@.@`pretty(u)@]%; }
      Rawactiv(RawAct(x,ty,u)) -> { return "\u03BC" + %[@`pr(x,ty)@.@`pretty(u)@]%; }
      Rawlapp(u@(Rawlapp|Rawfapp|Rawlvar)[],v) -> { 
        return %[@`pretty(u)@ @`pretty(v)@]%; 
      }
      Rawlapp(u,v) -> { return %[(@`pretty(u)@) @`pretty(v)@]%; }
      Rawfapp(u@(Rawlapp|Rawfapp|Rawlvar)[],v) -> { 
        return %[@`pretty(u)@ @`pretty(v)@]%; 
      }
      Rawfapp(u,v) -> { return %[(@`pretty(u)@) @`pretty(v)@]%; }
      Rawpair(u,v) -> { return %[(@`pretty(u)@,@`pretty(v)@)]%; }
      Rawproj1(u) -> { return %[fst @`pretty(u)@]%; }
      Rawproj2(u) -> { return %[snd @`pretty(u)@]%; }
      Rawcaseof(u,RawAlt(x,px,v),RawAlt(y,py,w)) -> { 
        return %[(case @`pretty(u)@ of (left @`pr(x,px)@) -> @`pretty(v)@ | (right @`pr(y,py)@) -> @`pretty(w)@)]%;
      }
      //Rawletin(RawLetin(fx,x,px,u,v)) -> { 
      Rawletin(RawLetin(fx,x,_,u,v)) -> { 
        return %[(let <@`fx@,@`x@> = @`pretty(u)@ in @`pretty(v)@)]%;
      }
      Rawwitness(ft,u,p) -> { return %[(<@`pretty(ft)@,@`pretty(u)@>:@`pretty(p)@)]%; }
      Rawleft(u,p) -> { return %[left{@`pretty(p)@} @`pretty(u)@]%; }
      Rawright(u,p) -> { return %[right{@`pretty(p)@} @`pretty(u)@]%; }
      Rawpassiv(mv,Rawlvar(x)) -> { return %[[@`mv@]@`x@]%; }
      Rawpassiv(mv,u) -> { return %[[@`mv@](@`pretty(u)@)]%; }
      Rawunit() -> { return "()"; }
    }
    throw new RuntimeException("non exhaustive patterns");
  }


  /* raw propositions */

  public static String pretty(RawProp p) {
    %match(p) {
      Rawimplies(p1@RawrelApp[],p2) -> { return %[@`pretty(p1)@ => @`pretty(p2)@]% ; }
      Rawimplies(p1,p2) -> { return %[(@`pretty(p1)@) => @`pretty(p2)@]% ; }
      Rawor(p1@RawrelApp[],p2) -> { return %[@`pretty(p1)@ \/ @`pretty(p2)@]%; }
      Rawor(p1,p2) -> { return %[(@`pretty(p1)@) \/ @`pretty(p2)@]%; }
      Rawand(p1@RawrelApp[],p2) -> { return %[@`pretty(p1)@ /\ @`pretty(p2)@]%; }
      Rawand(p1,p2) -> { return %[(@`pretty(p1)@) /\ @`pretty(p2)@]%; }
      Rawforall(RawFa(x,p1)) -> { return %[forall @`x@, (@`pretty(p1)@)]%; }
      Rawexists(RawEx(x,p1)) -> { return %[exists @`x@, (@`pretty(p1)@)]%; }
      RawrelApp(r,()) -> { return `r; }
      // arithmetic pretty print
      RawrelApp("eq",(x,y)) -> { return %[@`pretty(x)@ = @`pretty(y)@]%; }
      RawrelApp("gt",(x,y)) -> { return %[@`pretty(x)@ > @`pretty(y)@]%; }
      RawrelApp("lt",(x,y)) -> { return %[@`pretty(x)@ < @`pretty(y)@]%; }
      RawrelApp("le",(x,y)) -> { return %[@`pretty(x)@ <= @`pretty(y)@]%; }
      // set theory prettyprint
      RawrelApp("in",(x,y)) -> { return `pretty(x) + " \u2208 " + `pretty(y); }
      RawrelApp(r,x) -> { return %[@`r@(@`pretty(x)@)]%; }
      Rawbottom() -> { return "False"; }
      Rawtop() -> { return "True"; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  /* raw first-order terms */

  public static String pretty(RawTermList tl) {
    %match(tl) {
      RawtermList() -> { return ""; }
      RawtermList(x) -> { return `pretty(x); }
      RawtermList(h,t*) -> { return %[@`pretty(h)@,@pretty(`t)@]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }
  public static String pretty(RawTerm t) {
    %match(t) {
      Rawvar(x) -> { return `x;}
      // arithmetic pretty print
      RawfunApp("z",()) -> { return "0"; }
      i@RawfunApp("s",_) -> {
        try { return Integer.toString(peanoToInt(`i));}
        catch (ConversionError e) { }
      }
      RawfunApp("plus",(t1,t2)) -> { return %[(@`pretty(t1)@ + @`pretty(t2)@)]%; }
      RawfunApp("mult",(t1,t2)) -> { return %[(@`pretty(t1)@ * @`pretty(t2)@)]%; }
      RawfunApp("minus",(t1,t2)) -> { return %[(@`pretty(t1)@ - @`pretty(t2)@)]%; }
      RawfunApp("div",(t1,t2)) -> { return %[(@`pretty(t1)@ / @`pretty(t2)@)]%; }
      // finite 1st order theory of classes 
      RawfunApp("appl",(p,x*)) -> { return %[@`pretty(p)@[@`pretty(x)@]]%; }
      RawfunApp("nil",()) -> { return "nil"; }
      l@RawfunApp("cons",(x,y)) -> {
        try { return %[<@`prettyList(l)@>]%; }
        catch(ConversionError e) { return %[@`pretty(x)@::@`pretty(y)@]%; }
      }
      // normal case
      RawfunApp(name,x) -> { return %[@`name@(@`pretty(x)@)]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static class ConversionError extends Exception { }

  private static int peanoToInt(RawTerm t) throws ConversionError {
    %match(t) {
      RawfunApp("z",()) -> { return 0; }
      RawfunApp("s",(n)) -> { return 1+`peanoToInt(n); }
    }
    throw new ConversionError();
  }

  public static String prettyList(RawTerm t) throws ConversionError {
    %match(t) {
      RawfunApp("cons",(x,RawfunApp("nil",()))) -> { return `pretty(x); }
      RawfunApp("cons",(x,y)) -> { return %[@`pretty(x)@,@`prettyList(y)@]%; }
    }
    throw new ConversionError();
  }

  /* -- raw proofterms -- */ 

  private static boolean churchStyle = true;

  public static void setChurchStyle(boolean b) {
    churchStyle = b;
  }

  private static String space(int sp) {
    String res = "";
    for(int i=0; i<sp; i++)
      res += "  ";
    return res;
  }

  private static String pr(String var, RawProp prop, int sp) {
    return "\n" + space(sp) + (churchStyle ? %[<@var@:@pretty(prop)@>]% : %[<@var@>]%);
  }

  private static String pr2(String var, RawProp prop, String fovar, int sp) {
    return "\n" + space(sp) + (churchStyle ? %[<@var@:@pretty(prop)@><@fovar@>]% : %[<@var@><@fovar@>]%);
  }

  private static String pr3(String var1, RawProp prop1, String var2, RawProp prop2, int sp) {
    return "\n" + space(sp) + (churchStyle ? %[<@var1@:@pretty(prop1)@><@var2@:@pretty(prop2)@>]% : %[<@var1@><@var2@>]%);
  }

  private static String pretty(RawProofTerm t, int sp) {
    %match(t) {
      Rawax(n,cn) -> {return %[ax(@`n@,@`cn@)]%; }
      RawfalseL(n) -> { return %[falseL(@`n@)]%; }
      RawtrueR(cn) -> { return %[trueR(@`cn@)]%; }
      Rawcut(RawCutPrem1(cn,pcn,M),RawCutPrem2(n,pn,N)) -> { return %[cut(@`pr(cn,pcn,sp)@ @`pretty(M,sp+1)@,@`pr(n,pn,sp)@ @`pretty(N,sp+1)@)]%; }
      RawfalseL(n) -> { return %[falseL(@`n@)]%; }
      RawfalseL(cn) -> { return %[falseL(@`cn@)]%; }
      RawandR(RawAndRPrem1(a,pa,M),RawAndRPrem2(b,pb,N),cn) -> { return %[andR(@`pr(a,pa,sp)@ @`pretty(M,sp+1)@,@`pr(b,pb,sp)@ @`pretty(N,sp+1)@,@`cn@)]%; }
      RawandL(RawAndLPrem1(x,px,y,py,M),n) -> { return %[andL(@`pr3(x,px,y,py,sp)@ @`pretty(M,sp+2)@,@`n@)]%; }
      RaworR(RawOrRPrem1(a,pa,b,pb,M),cn) -> { return %[orR(@`pr3(a,pa,b,pb,sp)@ @`pretty(M,sp+1)@,@`cn@)]%; }
      RaworL(RawOrLPrem1(x,px,M),RawOrLPrem2(y,py,N),n) -> { return %[orL(@`pr(x,px,sp)@ @`pretty(M,sp+1)@,@`pr(y,py,sp)@ @`pretty(N,sp+1)@,@`n@)]%; }
      RawimplyR(RawImplyRPrem1(x,px,a,pa,M),cn) -> { return %[implyR(@`pr3(x,px,a,pa,sp)@ @`pretty(M,sp+1)@,@`cn@)]%; }
      RawimplyL(RawImplyLPrem1(x,px,M),RawImplyLPrem2(a,pa,N),n) -> { return %[implyL(@`pr(x,px,sp)@ @`pretty(M,sp+1)@,@`pr(a,pa,sp)@ @`pretty(N,sp+1)@,@`n@)]%; }
      RawexistsR(RawExistsRPrem1(a,pa,M),term,cn) -> { return %[existsR(@`pr(a,pa,sp)@ @`pretty(M,sp+1)@,@`pretty(term)@,@`cn@)]%; }
      RawexistsL(RawExistsLPrem1(x,px,fx,M),n) -> { return %[existsL(@`pr2(x,px,fx,sp)@ @`pretty(M,sp+1)@,@`n@)]%; }
      RawforallR(RawForallRPrem1(a,pa,fx,M),cn) -> { return %[forallR(@`pr2(a,pa,fx,sp)@ @`pretty(M,sp+1)@,@`cn@)]%; }
      RawforallL(RawForallLPrem1(x,px,M),term,n) -> { return %[forallL(@`pr(x,px,sp)@ @`pretty(M,sp+1)@,@`pretty(term)@,@`n@)]%; }
      RawrootL(RawRootLPrem1(x,px,M)) -> { return %[@`pr(x,px,sp)@ @`pretty(M,sp+1)@]%; }
      RawrootR(RawRootRPrem1(a,pa,M)) -> { return %[@`pr(a,pa,sp)@ @`pretty(M,sp+1)@]%; }
      RawfoldL(id,RawFoldLPrem1(x,px,M),n) -> { return %[foldL[@`id@](@`pr(x,px,sp)@ @`pretty(M,sp+1)@,@`n@)]%; }
      RawfoldR(id,RawFoldRPrem1(a,pa,M),cn) -> { return %[foldR[@`id@](@`pr(a,pa,sp)@ @`pretty(M,sp+1)@,@`cn@)]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(RawProofTerm t) {
    return pretty(t,0);
  }

  /* -- rewrite rules --*/

  public static String pretty(RawFoBound tl) {
    %match(tl) {
      RawfoBound() -> { return ""; }
      RawfoBound(x) -> { return `x; }
      RawfoBound(h,t*) -> { return %[@`h@,@`pretty(t)@]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(RawTermRewriteRules tl) {
    %match(tl) {
      Rawtermrrules() -> { return ""; }
      Rawtermrrules(h,t*) -> { return `pretty(h) + "\n" + `pretty(t); }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(RawTermRewriteRule r) {
    %match(r) {
      Rawtermrrule(id,Rawtrule(b,lhs,rhs)) -> { 
        return %[@`id@ : [@`pretty(b)@] @`pretty(lhs)@ -> @`pretty(rhs)@]%; 
      }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(RawPropRewriteRules tl) {
    %match(tl) {
      Rawproprrules() -> { return ""; }
      Rawproprrules(h,t*) -> { return `pretty(h) + "\n" + `pretty(t); }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(RawPropRewriteRule r) {
    %match(r) {
      Rawproprrule(id,Rawprule(b,lhs,rhs)) -> { 
        return %[@`id@ : [@`pretty(b)@] @`pretty(lhs)@ -> @`pretty(rhs)@]%; 
      }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  /* propositions */

  public static String pretty(Prop p) {
    %match(p) {
      implies(p1@relApp[],p2) -> { return %[@`pretty(p1)@ => @`pretty(p2)@]% ; }
      implies(p1,p2) -> { return %[(@`pretty(p1)@) => @`pretty(p2)@]% ; }
      or(p1@relApp[],p2) -> { return %[@`pretty(p1)@ \/ @`pretty(p2)@]%; }
      or(p1,p2) -> { return %[(@`pretty(p1)@) \/ @`pretty(p2)@]%; }
      and(p1@relApp[],p2) -> { return %[@`pretty(p1)@ /\ @`pretty(p2)@]%; }
      and(p1,p2) -> { return %[(@`pretty(p1)@) /\ @`pretty(p2)@]%; }
      forall(Fa(x,p1)) -> { return %[forall @`pretty(x)@, (@`pretty(p1)@)]%; }
      exists(Ex(x,p1)) -> { return %[exists @`pretty(x)@, (@`pretty(p1)@)]%; }
      relApp(r,()) -> { return `r; }
      relApp(r,x) -> { return %[@`r@(@`pretty(x)@)]%; }
      bottom() -> { return "False"; }
      top() -> { return "True"; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  /* first-order terms */

  public static String pretty(TermList tl) {
    %match(tl) {
      termList() -> { return ""; }
      termList(x) -> { return `pretty(x); }
      termList(h,t*) -> { return %[@`pretty(h)@,@pretty(`t)@]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(Term t) {
    %match(t) {
      var(x) -> { return `pretty(x);}
      funApp(name,x) -> { return %[@`name@(@`pretty(x)@)]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  /*-- proofterms --*/

  private static String pr(String var, Prop prop, int sp) {
    return "\n" + space(sp) + (churchStyle ? %[<@var@:@pretty(prop)@>]% : %[<@var@>]%);
  }

  private static String pr2(String var, Prop prop, String fovar, int sp) {
    return "\n" + space(sp) + (churchStyle ? %[<@var@:@pretty(prop)@><@fovar@>]% : %[<@var@><@fovar@>]%);
  }

  private static String pr3(String var1, Prop prop1, String var2, Prop prop2, int sp) {
    return "\n" + space(sp) + (churchStyle ? %[<@var1@:@pretty(prop1)@><@var2@:@pretty(prop2)@>]% : %[<@var1@><@var2@>]%);
  }

  private static String pretty(Name n) {
    %match(n) { Name(p,i) -> { return `i + `p; } }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static String pretty(CoName n) {
    %match(n) { CoName(p,i) -> { return `i + `p; } }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static String pretty(FoVar n) {
    %match(n) { FoVar(p,i) -> { return `i + `p; } }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static String pretty(ProofTerm t, int sp) {
    %match(t) {
      ax(n,cn) -> {return %[ax(@`pretty(n)@,@`pretty(cn)@)]%; }
      falseL(n) -> { return %[falseL(@`pretty(n)@)]%; }
      trueR(cn) -> { return %[trueR(@`pretty(cn)@)]%; }
      cut(CutPrem1(cn,pcn,M),CutPrem2(n,pn,N)) -> { return %[cut(@`pr(pretty(cn),pcn,sp)@ @`pretty(M,sp+1)@,@`pr(pretty(n),pn,sp)@ @`pretty(N,sp+1)@)]%; }
      falseL(n) -> { return %[falseL(@`pretty(n)@)]%; }
      falseL(cn) -> { return %[falseL(@`pretty(cn)@)]%; }
      andR(AndRPrem1(a,pa,M),AndRPrem2(b,pb,N),cn) -> { return %[andR(@`pr(pretty(a),pa,sp)@ @`pretty(M,sp+1)@,@`pr(pretty(b),pb,sp)@ @`pretty(N,sp+1)@,@`pretty(cn)@)]%; }
      andL(AndLPrem1(x,px,y,py,M),n) -> { return %[andL(@`pr3(pretty(x),px,pretty(y),py,sp)@ @`pretty(M,sp+2)@,@`pretty(n)@)]%; }
      orR(OrRPrem1(a,pa,b,pb,M),cn) -> { return %[orR(@`pr3(pretty(a),pa,pretty(b),pb,sp)@ @`pretty(M,sp+1)@,@`pretty(cn)@)]%; }
      orL(OrLPrem1(x,px,M),OrLPrem2(y,py,N),n) -> { return %[orL(@`pr(pretty(x),px,sp)@ @`pretty(M,sp+1)@,@`pr(pretty(y),py,sp)@ @`pretty(N,sp+1)@,@`pretty(n)@)]%; }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> { return %[implyR(@`pr3(pretty(x),px,pretty(a),pa,sp)@ @`pretty(M,sp+1)@,@`pretty(cn)@)]%; }
      implyL(ImplyLPrem1(x,px,M),ImplyLPrem2(a,pa,N),n) -> { return %[implyL(@`pr(pretty(x),px,sp)@ @`pretty(M,sp+1)@,@`pr(pretty(a),pa,sp)@ @`pretty(N,sp+1)@,@`pretty(n)@)]%; }
      existsR(ExistsRPrem1(a,pa,M),term,cn) -> { return %[existsR(@`pr(pretty(a),pa,sp)@ @`pretty(M,sp+1)@,@`pretty(term)@,@`pretty(cn)@)]%; }
      existsL(ExistsLPrem1(x,px,fx,M),n) -> { return %[existsL(@`pr2(pretty(x),px,pretty(fx),sp)@ @`pretty(M,sp+1)@,@`pretty(n)@)]%; }
      forallR(ForallRPrem1(a,pa,fx,M),cn) -> { return %[forallR(@`pr2(pretty(a),pa,pretty(fx),sp)@ @`pretty(M,sp+1)@,@`pretty(cn)@)]%; }
      forallL(ForallLPrem1(x,px,M),term,n) -> { return %[forallL(@`pr(pretty(x),px,sp)@ @`pretty(M,sp+1)@,@`pretty(term)@,@`pretty(n)@)]%; }
      rootL(RootLPrem1(x,px,M)) -> { return %[@`pr(pretty(x),px,sp)@ @`pretty(M,sp+1)@]%; }
      rootR(RootRPrem1(a,pa,M)) -> { return %[@`pr(pretty(a),pa,sp)@ @`pretty(M,sp+1)@]%; }
      foldL(id,FoldLPrem1(x,px,M),n) -> { return %[foldL[@`id@](@`pr(pretty(x),px,sp)@ @`pretty(M,sp+1)@,@`pretty(n)@)]%; }
      foldR(id,FoldRPrem1(a,pa,M),cn) -> { return %[foldR[@`id@](@`pr(pretty(a),pa,sp)@ @`pretty(M,sp+1)@,@`pretty(cn)@)]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(ProofTerm t) {
    return pretty(t,0);
  }

  /* sequents */

  private static String pretty(NProp np) {
    %match(np) { nprop(n,p) -> { return %[@`pretty(n)@:@`pretty(p)@]%; }}
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static String pretty(CNProp np) {
    %match(np) { cnprop(cn,p) -> { return %[@`pretty(cn)@:@`pretty(p)@]%; }}
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static String pretty(LCtx ctx) {
    %match(ctx) {
      lctx() -> { return ""; }
      lctx(x) -> { return `pretty(x); }
      lctx(x,xs*) -> { return `pretty(x) + "\n" + `pretty(xs); }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static String pretty(RCtx ctx) {
    %match(ctx) {
      rctx() -> { return ""; }
      rctx(x) -> { return `pretty(x); }
      rctx(x,xs*) -> { return `pretty(x) + "\n" + `pretty(xs); }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static String pretty(FoVarList l) {
    %match(l) {
      fovarList() -> { return ""; }
      fovarList(x) -> { return `pretty(x); }
      fovarList(x,xs*) -> { return %[@`pretty(x)@,@`pretty(xs)@]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(Sequent s) {
    String res;
    %match(s) { 
      seq(fv,lctx,rctx) -> {
        res  = `pretty(lctx) + "\n";
        res += "----------------------------------[" + `pretty(fv) + "]\n";
        res += `pretty(rctx);
        return res;
      }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  /* --------------- coc terms -------------- */

  public static String pretty(CoCTerm t) {
    %match(t) {
      cocVar(CoCAtom(x,i)) -> { return `x + `i; }
      cocLam(CoCLam(CoCAtom(x,i),ty,u)) -> { 
        return %[(fun @`x + `i@:@`pretty(ty)@ => @`pretty(u)@)]%; }
      cocPi(CoCPi(CoCAtom(x,i),ty,u)) -> { 
        return %[(forall @`x + `i@:@`pretty(ty)@, @`pretty(u)@)]%; 
      }
      cocApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      cocConst(c) -> { return `c; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(RawCoCTerm t) {
    %match(t) {
      RawcocVar(x) -> { return `x; }
      RawcocLam(RawCoCLam(x,ty,u)) -> { return %[(fun @`x@:@`pretty(ty)@ => @`pretty(u)@)]%; }
      RawcocPi(RawCoCPi(x,ty,u)) -> { return %[(forall @`x@:@`pretty(ty)@, @`pretty(u)@)]%; }
      RawcocApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      RawcocConst(c) -> { return `c; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

}

