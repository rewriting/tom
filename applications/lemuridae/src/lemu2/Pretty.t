package lemu2;
import lemu2.proofterms.types.*;

public class Pretty {

  %include { proofterms/proofterms.tom } 

  /* propositions */

  public static String pretty(RawProp p) {
    %match(p) {
      Rawimplies(p1@RawrelApp[],p2) -> { return %[@`pretty(p1)@ => @`pretty(p2)@]% ; }
      Rawimplies(p1,p2) -> { return %[(@`pretty(p1)@) => @`pretty(p2)@]% ; }
      Rawor(p1@RawrelApp[],p2) -> { return %[@`pretty(p1)@ \/ @`pretty(p2)@]%; }
      Rawor(p1,p2) -> { return %[(@`pretty(p1)@) \/ @`pretty(p2)@]%; }
      Rawand(p1@RawrelApp[],p2) -> { return %[@`pretty(p1)@ /\ @`pretty(p2)@]%; }
      Rawand(p1,p2) -> { return %[(@`pretty(p1)@) /\ @`pretty(p2)@]%; }
      Rawforall(RawFa(x,p1)) -> { return %[forall @`x@, @`pretty(p1)@]%; }
      Rawexists(RawEx(x,p1)) -> { return %[exists @`x@, @`pretty(p1)@]%; }
      RawrelApp(r,()) -> { return `r; }
      // arithmetic pretty print
      RawrelApp("eq",(x,y)) -> { return %[@`pretty(x)@ = @`pretty(y)@]%; }
      RawrelApp("gt",(x,y)) -> { return %[@`pretty(x)@ > @`pretty(y)@]%; }
      RawrelApp("lt",(x,y)) -> { return %[@`pretty(x)@ < @`pretty(y)@]%; }
      RawrelApp("le",(x,y)) -> { return %[@`pretty(x)@ >= @`pretty(y)@]%; }
      // set theory prettyprint
      RawrelApp("in",(x,y)) -> { return `pretty(x) + " \u2208 " + `pretty(y); }
      RawrelApp(r,x) -> { return %[@`r@(@`pretty(x)@)]%; }
      Rawbottom() -> { return "False"; }
      Rawtop() -> { return "True"; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  /* first-order terms */

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

  /* -- proofterms -- */ 

  private static boolean churchStyle = true;

  public static void setChurchStyle(boolean b) {
    churchStyle = b;
  }

  private static String pr(String var, RawProp prop) {
    return churchStyle ? %[<@var@:@`pretty(prop)@>]% : %[<@var@>]%;
  }

  private static String pr3(String var, RawProp prop, String fovar) {
    return churchStyle ? %[<@var@:@`pretty(prop)@,@fovar@>]% : %[<@var@,@fovar@>]%;
  }

  public static String pretty(RawProofTerm t) {
    %match(t) {
      Rawax(n,cn) -> {return %[ax(@`n@,@`cn@)]%; }
      Rawcut(RawCutPrem1(cn,pcn,M),RawCutPrem2(n,pn,N)) -> { return %[cut(@`pr(cn,pcn)@ @`pretty(M)@,@`pr(n,pn)@ @`pretty(N)@)]%; }
      RawfalseL(n) -> { return %[falseL(@`n@)]%; }
      RawfalseL(cn) -> { return %[falseL(@`cn@)]%; }
      RawandR(RawAndRPrem1(a,pa,M),RawAndRPrem2(b,pb,N),cn) -> { return %[andR(@`pr(a,pa)@ @`pretty(M)@,@`pr(b,pb)@ @`pretty(N)@,@`cn@)]%; }
      RawandL(RawAndLPrem1(x,px,y,py,M),n) -> { return %[andL(@`pr(x,px) + `pr(y,py)@ @`pretty(M)@,@`n@)]%; }
      RaworR(RawOrRPrem1(a,pa,b,pb,M),cn) -> { return %[orR(<@`pr(a,pa) + `pr(b,pb)@ @`pretty(M)@,@`cn@)]%; }
      RaworL(RawOrLPrem1(x,px,M),RawOrLPrem2(y,py,N),n) -> { return %[orL(@`pr(x,px)@ @`pretty(M)@,@`pr(y,py)@ @`pretty(N)@,@`n@)]%; }
      RawimplyR(RawImplyRPrem1(x,px,a,pa,M),cn) -> { return %[implyR(@`pr(x,px) + `pr(a,pa)@ @`pretty(M)@,@`cn@)]%; }
      RawimplyL(RawImplyLPrem1(x,px,M),RawImplyLPrem2(a,pa,N),n) -> { return %[implyL(@`pr(x,px)@ @`pretty(M)@,@`pr(a,pa)@ @`pretty(N)@,@`n@)]%; }
      RawexistsR(RawExistsRPrem1(a,pa,M),term,cn) -> { return %[existsR(@`pr(a,pa)@ @`pretty(M)@,@`pretty(term)@,@`cn@)]%; }
      RawexistsL(RawExistsLPrem1(x,px,fx,M),n) -> { return %[existsL(@`pr3(x,px,fx)@ @`pretty(M)@,@`n@)]%; }
      RawforallR(RawForallRPrem1(a,pa,fx,M),cn) -> { return %[forallR(@`pr3(a,pa,fx)@ @`pretty(M)@,@`cn@)]%; }
      RawforallL(RawForallLPrem1(x,px,M),term,n) -> { return %[forallL(@`pr(x,px)@ @`pretty(M)@,@`pretty(term)@,@`n@)]%; }
      RawrootL(RawRootLPrem1(x,px,M)) -> { return %[@`pr(x,px)@ @`pretty(M)@]%; }
      RawrootR(RawRootRPrem1(a,pa,M)) -> { return %[@`pr(a,pa)@ @`pretty(M)@]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  /* -- rewrite rules --*/

  public static String pretty(RawTermRewriteRules tl) {
    %match(tl) {
      Rawtermrrules() -> { return ""; }
      Rawtermrrules(x) -> { return `pretty(x); }
      Rawtermrrules(h,t*) -> { return `pretty(h) + "\n" + `pretty(t); }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(RawFoBound tl) {
    %match(tl) {
      RawfoBound() -> { return ""; }
      RawfoBound(x) -> { return `x; }
      RawfoBound(h,t*) -> { return %[@`h@,@`pretty(t)@]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static String pretty(RawTermRewriteRule r) {
    %match(r) {
      Rawtermrrule(Rawtrule(b,lhs,rhs)) -> { 
        return %[[@`pretty(b)@] @`pretty(lhs)@ -> @`pretty(rhs)@]%; 
      }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

}

