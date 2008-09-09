import tom.library.sl.*;
import lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Compiler {

  %include { lambda/Lambda.tom }
  %include { sl.tom }

  private static String type(RawLType ty) {
    %match(ty) {
      RawAtom(s) -> { return `s; }
      RawTypeVar(i) -> { return "A" + `i; }
      RawArrow(a,b) -> { return "Fun<" + `type(a) + "," + `type(b) + ">"; }
    }
    return null;
  }

  private static String compile(RawTyLTermList l) {
    %match(l) {
      RawTyLTList() -> { return ""; }
      RawTyLTList(x) -> { return `compile(x); }
      RawTyLTList(x,xs*) -> { return `compile(x) + "," + `compile(xs); }
    }
    return null;
  }

  private static String compile(RawTyPatternList l) {
    %match(l) {
      RawTyPList() -> { return ""; }
      RawTyPList(x) -> { return `compile(x); }
      RawTyPList(x,xs*) -> { return `compile(x) + "," + `compile(xs); }
    }
    return null;
  }

  private static String compile(RawTyRules l) {
    %match(l) {
      RawTyRList() -> { return ""; }
      RawTyRList(x,xs*) -> { 
        return "\n" + `compile(x) + `compile(xs); }
    }
    return null;
  }

  private static String compile(RawTyClause c) {
    %match(c) {
      RawTyRule(p,t) -> { 
        return `compile(p) + " -> { return " + `compile(t) + "; }" ;
      }
    }
    return null;
  }

  private static String compile(RawTyPattern p) {
    %match (p) {
      RawTyPVar(x,_) -> { return `x; }
      RawTyPFun(f,l) -> { return `f + "(" + `compile(l) + ")"; }
    }
    return null;
  }

  public static String compile(RawTyped t) {
    %match(t) {
      RawTyped(RawTyVar(x),_) -> { return "`(" + `x + ")"; }
      RawTyped(RawTyApp(u,v),_) -> { 
        return %[(@`compile(u)@).apply(@`compile(v)@)]%;
      }
      RawTyped(RawTyAbs(RawTylam(x,ty1,u)),ty3@RawArrow(ty1,ty2)) -> {
        return %[
          new @`type(ty3)@() {
            public @`type(ty2)@ apply(final @`type(ty1)@ @`x@) {
              return @`compile(u)@;
            }
          }]%;
      }
      RawTyped(RawTyConstr(f,tl),_) -> {
        return %[tom_make_@`f@(@`compile(tl)@)]%;
      }
      RawTyped(RawTyCase(s@RawTyped(_,sty),rls),ty) -> {
        return %[((new Value<@`type(ty)@>() {
          public @`type(ty)@ value() {
            @`type(sty)@ _subject = @`compile(s)@;
            %match(_subject) { @`compile(rls)@ }
            throw new RuntimeException("non-exhaustive patterns");
          }
        }).value())]%;
      }
    }
    return null;
  }
}
