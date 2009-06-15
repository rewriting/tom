package lazyml;

import tom.library.sl.*;
import lazyml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Printer {

  %include { lambda/lambda.tom }

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
      ConsPList(x,xs) -> { return `prettyPattern(x) + "," + `prettyPatternList(xs); }
    }
    return null;
  }

  public static String prettyRules(Rules l) {
    %match(l) {
      EmptyRList() -> { return ""; }
      ConsRList(x,xs) -> { return " | " + `prettyClause(x) + `prettyRules(xs); }
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
      PVar(LVar(x,i)) -> { return `i + `x; }
      PFun(f,EmptyPList()) -> { return `f; }
      PFun(f,l) -> { return `f + "(" + `prettyPatternList(l) + ")"; }
    }
    return null;
  }

  public static String pretty(LTerm t) {
    %match (t) {
      Var(LVar(x,i)) -> { return `i + `x ; }
      Abs(lam(LVar(x,i),u)) -> { return %[(fun @`i +`x@ -> @`pretty(u)@)]%; }
      App(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      Fix(fixpoint(x,u)) -> { return %[fix @`x@ @`pretty(u)@]%; }
      Let(letin(LVar(x,i),u,v)) -> {
        return %[(let @`i + `x@ = @`pretty(u)@ in @`pretty(v)@)]%;
      }
      Constr(f,l) -> { return `f + "(" + `prettyLTermList(l) + ")"; }
      PrimFun(f,l) -> { return `f + "(" + `prettyLTermList(l) + ")"; }
      Lit(i) -> { return ""+`i; }
      Chr(c) -> { return "'" + `c + "'"; }
      Str(s) -> { return "\"" + `s + "\""; }
      Case(s,l) -> {
        return %[(match @`pretty(s)@ with@`prettyRules(l)@ end)]%;
      }
    }
    return null;
  }

  public static String pretty(LType ty) {
    %match (ty) {
      Atom(a) -> { return `a; }
      Arrow(a@Arrow[],b) -> { return %[(@`pretty(a)@) -> @`pretty(b)@]%; }
      Arrow(a,b) -> { return %[@`pretty(a)@ -> @`pretty(b)@]%; }
      TypeVar(TVar(i,s)) -> { return `s + `i; }
      Forall(Fa(TVar(i,s),a)) -> { return "(\u2200 " + `s + `i + ". " + `pretty(a) +")"; }
      TyConstr(c,tyl) -> { return %[@`c@(@`pretty(tyl)@)]%; }
    }
    return null;
  }

  public static String pretty(TyList tyl) {
    %match(tyl) {
      TyList() -> { return ""; }
      TyList(t) -> { return `pretty(t); }
      TyList(t,ts) -> { return `pretty(t) + "," + `pretty(ts); }
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
      ConsRawPList(x,xs) -> { return `prettyPattern(x) + "," + `prettyPatternList(xs); }
    }
    return null;
  }

  public static String prettyRules(RawRules l) {
    %match(l) {
      EmptyRawRList() -> { return ""; }
      ConsRawRList(x,xs) -> { return " | " + `prettyClause(x) + `prettyRules(xs); }
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
      RawPVar(x) -> { return `x; }
      //RawPFun(f,EmptyRawPList()) -> { return `f; }
      RawPFun(f,l) -> { return `f + "(" + `prettyPatternList(l) + ")"; }
    }
    return null;
  }

  public static class ConversionError extends Exception {};

  public static String pretty(RawLTerm t) {
    %match (t) {
      RawVar(x) -> { return `x; }
      RawAbs(Rawlam(x,u)) -> {
        return %[(fun @`x@ -> @`pretty(u)@)]%;
      }
      RawApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      RawFix(Rawfixpoint(x,u)) -> {
        return %[fix @`x@ @`pretty(u)@]%;
      }
      RawLet(Rawletin(x,u,v)) -> {
        return %[(let @`x@ = @`pretty(u)@ in @`pretty(v)@)]%;
      }
      RawConstr(f,l) -> { return "(" + `f + "(" + `prettyLTermList(l) + "))"; }
      RawPrimFun(f,l) -> { return "(" + `f + "(" + `prettyLTermList(l) + "))"; }
      RawLit(i) -> { return ""+`i; }
      RawChr(c) -> { return "'" + `c + "'"; }
      RawStr(s) -> { return "\"" + `s + "\""; }
      RawCase(s,l) -> {
        return %[(match @`pretty(s)@ with@`prettyRules(l)@ end)]%;
      }
    }
    return null;
  }

  public static String pretty(LVar x) {
    %match(x) { LVar(i,s) -> { return `s + `i; }}
    throw new RuntimeException("never happens");
  }

  public static String pretty(TVar x) {
    %match(x) { TVar(i,s) -> { return `s + `i; }}
    throw new RuntimeException("never happens");
  }

  public static String pretty(FTerm t) {
    %match (t) {
      FVar(x) -> { return `pretty(x); }
      FAbs(FLam(x,ty,u)) -> { return "(\u03BB" + %[@`pretty(x)@:@`pretty(ty)@. @`pretty(u)@)]%; }
      FTAbs(FTLam(a,u)) -> {
        return "(\u039B" + %[@`pretty(a)@. @`pretty(u)@)]%;
      }
      FApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      FTApp(u,v) -> { return %[(@`pretty(u)@ [@`pretty(v)@])]%; }
      FLet(FLetin(x,ty,u,v)) -> { return %[(let @`pretty(x)@:@`pretty(ty)@ = @`pretty(u)@ in @`pretty(v)@)]%; }
      FFix(FFixpoint(x,_,u)) -> {
        return %[(fix @`pretty(x)@ @`pretty(u)@)]%;
      }
      FConstr(f,l) -> { return `f + "(" + `prettyFTermList(l) + ")"; }
      FPrimFun(f,l) -> { return `f + "(" + `prettyFTermList(l) + ")"; }
      FLit(i) -> { return ""+`i; }
      FChr(c) -> { return "'" + `c + "'"; }
      FStr(s) -> { return "\"" + `s + "\""; }
      FCase(s,l) -> {
        return %[(match @`pretty(s)@ with@`prettyFRules(l)@ end)]%; }
    }
    return null;
  }

  public static String pretty(RawFTerm t) {
    /*
       try { return "" + prettyNat(t); }
       catch (ConversionError e) { }
     */
    %match (t) {
      RawFVar(x) -> { return `x; }
      RawFAbs(RawFLam(x,ty,u)) -> { return "(\u03BB" + %[@`x@:@`pretty(ty)@. @`pretty(u)@)]%; }
      RawFTAbs(RawFTLam(a,u)) -> {
        return "(\u039B" + %[@`a@. @`pretty(u)@)]%;
      }
      RawFApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      RawFTApp(u,v) -> { return %[(@`pretty(u)@ [@`pretty(v)@])]%; }
      RawFLet(RawFLetin(x,ty,u,v)) -> { return %[(let @`x@:@`pretty(ty)@ = @`pretty(u)@ in @`pretty(v)@)]%; }
      RawFFix(RawFFixpoint(x,_,u)) -> {
        return %[(fix @`x@ @`pretty(u)@)]%;
      }
      RawFConstr(f,l) -> { return `f + "(" + `prettyFTermList(l) + ")"; }
      RawFPrimFun(f,l) -> { return `f + "(" + `prettyFTermList(l) + ")"; }
      RawFLit(i) -> { return ""+`i; }
      RawFChr(c) -> { return "'" +`c + "'"; }
      RawFStr(s) -> { return "\"" + `s + "\""; }
      RawFCase(s,l) -> {
        return %[(match @`pretty(s)@ with@`prettyFRules(l)@ end)]%; }
    }
    return null;
  }

  public static String prettyFTermList(FTermList l) {
    %match(l) {
      EmptyFTermList() -> { return ""; }
      ConsFTermList(x,EmptyFTermList()) -> { return `pretty(x); }
      ConsFTermList(x,xs) -> { return `pretty(x) + "," + `prettyFTermList(xs); }
    }
    return null;
  }

  public static String prettyFRules(FRules l) {
    %match(l) {
      EmptyFRList() -> { return ""; }
      ConsFRList(x,xs) -> { return " | " + `prettyFClause(x) + `prettyFRules(xs); }
    }
    return null;
  }

  public static String prettyFClause(FClause c) {
    %match(c) {
      FRule(p,t) -> { return `prettyFPattern(p) + " -> " + `pretty(t); }
    }
    return null;
  }

  public static String prettyFPattern(FPattern p) {
    %match (p) {
      FPVar(x,ty) -> { return `pretty(x) +":" + `pretty(ty); }
      FPFun(f,EmptyFPList()) -> { return `f; }
      FPFun(f,l) -> { return `f + "(" + `prettyFPatternList(l) + ")"; }
    }
    return null;
  }

  public static String prettyFPatternList(FPatternList l) {
    %match(l) {
      EmptyFPList() -> { return ""; }
      ConsFPList(x,EmptyFPList()) -> { return `prettyFPattern(x); }
      ConsFPList(x,xs) -> { return `prettyFPattern(x) + "," + `prettyFPatternList(xs); }
    }
    return null;
  }

  public static String prettyFTermList(RawFTermList l) {
    %match(l) {
      EmptyRawFTermList() -> { return ""; }
      ConsRawFTermList(x,EmptyRawFTermList()) -> { return `pretty(x); }
      ConsRawFTermList(x,xs) -> { return `pretty(x) + "," + `prettyFTermList(xs); }
    }
    return null;
  }

  public static String prettyFRules(RawFRules l) {
    %match(l) {
      EmptyRawFRList() -> { return ""; }
      ConsRawFRList(x,xs) -> { return " | " + `prettyFClause(x) + `prettyFRules(xs); }
    }
    return null;
  }

  public static String prettyFClause(RawFClause c) {
    %match(c) {
      RawFRule(p,t) -> { return `prettyFPattern(p) + " -> " + `pretty(t); }
    }
    return null;
  }

  public static String prettyFPattern(RawFPattern p) {
    %match (p) {
      RawFPVar(x,ty) -> { return `x +":" + `pretty(ty); }
      RawFPFun(f,EmptyRawFPList()) -> { return `f; }
      RawFPFun(f,l) -> { return `f + "(" + `prettyFPatternList(l) + ")"; }
    }
    return null;
  }

  public static String prettyFPatternList(RawFPatternList l) {
    %match(l) {
      EmptyRawFPList() -> { return ""; }
      ConsRawFPList(x,EmptyRawFPList()) -> { return `prettyFPattern(x); }
      ConsRawFPList(x,xs) -> { return `prettyFPattern(x) + "," + `prettyFPatternList(xs); }
    }
    return null;
  }

  public static String pretty(RawLType ty) {
    %match (ty) {
      RawAtom(a) -> { return `a; }
      RawArrow(a@RawArrow[],b) -> { return %[(@`pretty(a)@) -> @`pretty(b)@]%; }
      RawArrow(a,b) -> { return %[@`pretty(a)@ -> @`pretty(b)@]%; }
      RawTypeVar(s) -> { return `s; }
      RawForall(RawFa(s,a)) -> { return "(\u2200 " + `s + ". " + `pretty(a) +")"; }
      RawTyConstr(c,tyl) -> { return %[@`c@(@`pretty(tyl)@)]%; }
    }
    return null;
  }

  public static String pretty(RawTyList tyl) {
    %match(tyl) {
      RawTyList() -> { return ""; }
      RawTyList(t) -> { return `pretty(t); }
      RawTyList(t,ts*) -> { return `pretty(t) + "," + `pretty(ts); }
    }
    return null;
  }



}
