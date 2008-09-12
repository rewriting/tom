package miniml;

import tom.library.sl.*;
import miniml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Printer {

  %include { lambda/Lambda.tom }

  public static String prettyLTermList(LTermList l) {
    %match(l) {
      EmptyLTList() -> { return ""; }
      ConsLTList(x,EmptyLTList()) -> { return `pretty(x); }
      ConsLTList(x,xs) -> { return `pretty(x) + "," + `prettyLTermList(xs); }
    }
    return null;
  }

  public static String prettyPatternList(PatternList l) {
    %match(l) {
      EmptyPList() -> { return ""; }
      ConsPList(x,EmptyPList()) -> { return `prettyPattern(x); }
      ConsPList(x,xs) -> { return `prettyPattern(x) + "," 
        + `prettyPatternList(xs); }
    }
    return null;
  }

  public static String prettyRules(Rules l) {
    %match(l) {
      EmptyRList() -> { return ""; }
      ConsRList(x,xs) -> { 
        return " | " + `prettyClause(x) + `prettyRules(xs); }
    }
    return null;
  }

  public static String prettyClause(Clause c) {
    %match(c) {
      Rule(p,t) -> { return `prettyPattern(p) + " -> " + `pretty(t); }
    }
    return null;
  }

  public static String prettyPattern(Pattern p) {
    %match (p) {
      PVar(LVar(x,i),_) -> { return `i + `x; }
      PFun(f,EmptyPList()) -> { return `f; }
      PFun(f,l) -> { return `f + "(" + `prettyPatternList(l) + ")"; }
    }
    return null;
  }

  public static String pretty(LTerm t) {
    %match (t) {
      Var(LVar(x,i)) -> { return `i + `x ; }
      Abs(lam(LVar(x,i),_,u)) -> { 
        return %[(fun @`i +`x@ -> @`pretty(u)@)]%; 
      }
      App(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      Fix(fixpoint(x,_,u)) -> { return %[fix @`x@ @`pretty(u)@]%; }
      Let(letin(LVar(x,i),u,v)) -> { 
        return %[(let @`i + `x@ = @`pretty(u)@ in @`pretty(v)@)]%; }
      Constr(f,EmptyLTList()) -> { return `f; }
      Constr(f,l) -> { return `f + "(" + `prettyLTermList(l) + ")"; }
      Case(s,l) -> { 
        return %[(match @`pretty(s)@ with@`prettyRules(l)@ end)]%; }
    }
    return null;
  }

  public static String pretty(LType ty) {
    %match (ty) {
      Atom(a) -> { return `a; }
      Arrow(a@Arrow[],b) -> { return %[(@`pretty(a)@) -> @`pretty(b)@]%; }
      Arrow(a,b) -> { return %[@`pretty(a)@ -> @`pretty(b)@]%; }
      TypeVar(i) -> { return "'a" + `i; }
    }
    return null;
  }
 

  public static String prettyLTermList(RawLTermList l) {
    %match(l) {
      EmptyRawLTList() -> { return ""; }
      ConsRawLTList(x,EmptyRawLTList()) -> { return `pretty(x); }
      ConsRawLTList(x,xs) -> { return `pretty(x) + "," + `prettyLTermList(xs); }
    }
    return null;
  }

  public static String prettyPatternList(RawPatternList l) {
    %match(l) {
      EmptyRawPList() -> { return ""; }
      ConsRawPList(x,EmptyRawPList()) -> { return `prettyPattern(x); }
      ConsRawPList(x,xs) -> { return `prettyPattern(x) + "," 
        + `prettyPatternList(xs); }
    }
    return null;
  }

  public static String prettyRules(RawRules l) {
    %match(l) {
      EmptyRawRList() -> { return ""; }
      ConsRawRList(x,xs) -> { 
        return " | " + `prettyClause(x) + `prettyRules(xs); }
    }
    return null;
  }

  public static String prettyClause(RawClause c) {
    %match(c) {
      RawRule(p,t) -> { return `prettyPattern(p) + " -> " + `pretty(t); }
    }
    return null;
  }

  public static String prettyPattern(RawPattern p) {
    %match (p) {
      RawPVar(x,_) -> { return `x; }
      RawPFun(f,EmptyRawPList()) -> { return `f; }
      RawPFun(f,l) -> { return `f + "(" + `prettyPatternList(l) + ")"; }
    }
    return null;
  }

  public static class ConversionError extends Exception {};

  public static int prettyNat(RawLTerm t) throws ConversionError {
    %match(t) {
      RawConstr("O",EmptyRawLTList()) -> { return 0; }
      RawConstr("S",ConsRawLTList(u,EmptyRawLTList())) -> {
        return 1+prettyNat(`u);
      }
    }
    throw new ConversionError();
  }

  public static String pretty(RawLTerm t) {
    try { return "" + prettyNat(t); }
    catch (ConversionError e) { }
    %match (t) {
      RawVar(x) -> { return `x; }
      RawAbs(Rawlam(x,_,u)) -> { 
        return %[(fun @`x@ -> @`pretty(u)@)]%; 
      }
      RawApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      RawFix(Rawfixpoint(x,_,u)) -> { 
        return %[fix @`x@ @`pretty(u)@]%; 
      }
      RawLet(Rawletin(x,u,v)) -> { 
        return %[(let @`x@ = @`pretty(u)@ in @`pretty(v)@)]%; }
      RawConstr(f,EmptyRawLTList()) -> { return `f; }
      RawConstr(f,l) -> { return `f + "(" + `prettyLTermList(l) + ")"; }
      RawCase(s,l) -> { 
        return %[(match @`pretty(s)@ with@`prettyRules(l)@ end)]%; }
    }
    return null;
  }

  public static String pretty(RawTyLTerm t) {
    /*
    try { return "" + prettyNat(t); }
    catch (ConversionError e) { }
    */
    %match (t) {
      RawTyVar(x) -> { return `x; }
      RawTyAbs(RawTylam(x,ty,u)) -> { 
        return %[(fun (@`x@:@`pretty(ty)@) -> @`pretty(u)@)]%; 
      }
      RawTyApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      RawTyFix(RawTyfixpoint(x,_,u)) -> { 
        return %[(fix @`x@ @`pretty(u)@)]%; 
      }
      /*
      RawConstr(f,EmptyRawLTList()) -> { return `f; }
      RawConstr(f,l) -> { return `f + "(" + `prettyLTermList(l) + ")"; }
      RawCase(s,l) -> { 
        return %[(match @`pretty(s)@ with@`prettyRules(l)@ end)]%; }
        */
    }
    return null;
  }

  public static String pretty(RawTyped t) {
    %match(t) {
      RawTyped(u,ty) -> { return %[(@`pretty(u)@ : @`pretty(ty)@)]%; }
    }
    return null;
  }

  public static String pretty(RawLType ty) {
    %match (ty) {
      RawAtom(a) -> { return `a; }
      RawArrow(a@RawArrow[],b) -> { return %[(@`pretty(a)@) -> @`pretty(b)@]%; }
      RawArrow(a,b) -> { return %[@`pretty(a)@ -> @`pretty(b)@]%; }
      RawTypeVar(i) -> { return "'a" + `i; }
    }
    return null;
  }

}
