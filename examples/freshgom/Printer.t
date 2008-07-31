import tom.library.sl.*;
import lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Printer {

  %include { tweaked_Lambda.tom }

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
      PVar(LVar(x,i)) -> { return `i + `x; }
      PFun(f,EmptyPList()) -> { return `f; }
      PFun(f,l) -> { return `f + "(" + `prettyPatternList(l) + ")"; }
    }
    return null;
  }

  public static String pretty(LTerm t) {
    %match (t) {
      Var(LVar(x,i)) -> { return `i + `x ; }
      Abs(lam(LVar(x,i),u)) -> { return %[(fun @`i + `x@ -> @`pretty(u)@)]%; }
      App(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      Let(letin(LVar(x,i),u,v)) -> { 
        return %[(let @`i + `x@ = @`pretty(u)@ in @`pretty(v)@)]%; }
      Constr(f,EmptyLTList()) -> { return `f; }
      Constr(f,l) -> { return `f + "(" + `prettyLTermList(l) + ")"; }
      Case(s,l) -> { 
        return %[(match @`pretty(s)@ with@`prettyRules(l)@ end)]%; }
    }
    return null;
  }



  public static String prettyLTermList(RawLTermList l) {
    %match(l) {
      RawEmptyLTList() -> { return ""; }
      RawConsLTList(x,RawEmptyLTList()) -> { return `pretty(x); }
      RawConsLTList(x,xs) -> { return `pretty(x) + "," + `prettyLTermList(xs); }
    }
    return null;
  }

  public static String prettyPatternList(RawPatternList l) {
    %match(l) {
      RawEmptyPList() -> { return ""; }
      RawConsPList(x,RawEmptyPList()) -> { return `prettyPattern(x); }
      RawConsPList(x,xs) -> { return `prettyPattern(x) + "," 
        + `prettyPatternList(xs); }
    }
    return null;
  }

  public static String prettyRules(RawRules l) {
    %match(l) {
      RawEmptyRList() -> { return ""; }
      RawConsRList(x,xs) -> { 
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
      RawPVar(x) -> { return `x; }
      RawPFun(f,RawEmptyPList()) -> { return `f; }
      RawPFun(f,l) -> { return `f + "(" + `prettyPatternList(l) + ")"; }
    }
    return null;
  }

  public static String pretty(RawLTerm t) {
    %match (t) {
      RawVar(x) -> { return `x; }
      RawAbs(Rawlam(x,u)) -> { return %[(fun @`x@ -> @`pretty(u)@)]%; }
      RawApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      RawLet(Rawletin(x,u,v)) -> { 
        return %[(let @`x@ = @`pretty(u)@ in @`pretty(v)@)]%; }
      RawConstr(f,RawEmptyLTList()) -> { return `f; }
      RawConstr(f,l) -> { return `f + "(" + `prettyLTermList(l) + ")"; }
      RawCase(s,l) -> { 
        return %[(match @`pretty(s)@ with@`prettyRules(l)@ end)]%; }
    }
    return null;
  }
}
