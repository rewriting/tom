package freshgom_sharing;

import tom.library.sl.*;
import freshgom_sharing.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Printer {

  %include { tweaked_lambda.tom }

  private static String spaces(int n) {
    String res = "";
    for(int i=0; i<n; i++) res += "  ";
    return res;
  }

  public static String prettyLTermList(RawLTermList l) {
    %match(l) {
      RawLTList() -> { return ""; }
      RawLTList(x) -> { return `pretty(x); }
      RawLTList(x,xs*) -> { return `pretty(x) + "," + `prettyLTermList(xs); }
    }
    return null;
  }

  public static String prettyPatternList(RawPatternList l) {
    %match(l) {
      RawPList() -> { return ""; }
      RawPList(x) -> { return `prettyPattern(x); }
      RawPList(x,xs*) -> { return `prettyPattern(x) + "," 
        + `prettyPatternList(xs); }
    }
    return null;
  }

  public static String prettyRules(RawRules l) {
    %match(l) {
      RawRList() -> { return ""; }
      RawRList(x,xs*) -> { 
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
      RawPFun(f,RawPList()) -> { return `f; }
      RawPFun(f,l) -> { return `f + "(" + `prettyPatternList(l) + ")"; }
    }
    return null;
  }

  public static String pretty(RLTerm t) {
    %match (t) {
      RawVar(x) -> { return `x; }
      RawAbs(Rawlam(x,u)) -> { return %[(fun @`x@ -> @`pretty(u)@)]%; }
      RawApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      RawLet(Rawletin(x,u,v)) -> { 
        return %[(let @`x@ = @`pretty(u)@ in @`pretty(v)@)]%; }
      RawConstr(f,RawLTList()) -> { return `f; }
      RawConstr(f,l) -> { return `f + "(" + `prettyLTermList(l) + ")"; }
      RawCase(s,l) -> { 
        return %[(match @`pretty(s)@ with@`prettyRules(l)@ end)]%; }
    }
    return null;
  }
}
