package miniml;

import tom.library.sl.*;
import miniml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Compiler {

  %include { lambda/Lambda.tom }
  %include { sl.tom }

  private static int scount = 0;

  private static String type(RawLType ty) {
    %match(ty) {
      RawAtom(s) -> { return `s; }
      RawTypeVar(i) -> { return "A" + `i; }
      RawArrow(a,b) -> { return "Fun<" + `type(a) + "," + `type(b) + ">"; }
    }
    return null;
  }

  private static String compile(RawTyLTermList l, FixVars vl) {
    %match(l) {
      RawTyLTList() -> { return ""; }
      RawTyLTList(x) -> { return `compile(x,vl); }
      RawTyLTList(x,xs*) -> { return `compile(x,vl) + "," + `compile(xs,vl); }
    }
    return null;
  }

  private static String compile(RawTyPatternList l, FixVars vl) {
    %match(l) {
      RawTyPList() -> { return ""; }
      RawTyPList(x) -> { return `compile(x,vl); }
      RawTyPList(x,xs*) -> { return `compile(x,vl) + "," + `compile(xs,vl); }
    }
    return null;
  }

  private static String compile(RawTyRules l, FixVars vl) {
    %match(l) {
      RawTyRList() -> { return ""; }
      RawTyRList(x,xs*) -> { 
        return "\n" + `compile(x,vl) + `compile(xs,vl); }
    }
    return null;
  }

  private static String compile(RawTyClause c, FixVars vl) {
    %match(c) {
      RawTyRule(p,t) -> { 
        return `compile(p,vl) + " -> { return " + `compile(t,vl) + "; }" ;
      }
    }
    return null;
  }

  private static String compile(RawTyPattern p, FixVars vl) {
    %match(p) {
      RawTyPVar(x,_) -> { return `x; }
      RawTyPFun(f,l) -> { return `f + "(" + `compile(l,vl) + ")"; }
    }
    return null;
  }

  public static String compile(RawTyped t) {
    return compile(t,`FixVarList());
  }
  private static String compile(RawTyped t, FixVars vl) {
    %match(t) {
      RawTyped(RawTyVar(x),_) -> { 
        %match(FixVars vl) {
          FixVarList(_*,v,_*) && v << String x -> { return "(" + `x + ".value())"; }
          _                          -> { return "`(" + `x + ")"; }
        }
      }
      RawTyped(RawTyApp(u,v),_) -> { 
        return %[(@`compile(u,vl)@).apply(@`compile(v,vl)@)]%;
      }
      RawTyped(RawTyAbs(RawTylam(x,ty1,u)),ty3@RawArrow(ty1,ty2)) -> {
        return %[
          new @`type(ty3)@() {
            public @`type(ty2)@ apply(final @`type(ty1)@ @`x@) {
              return @`compile(u,vl)@;
            }
          }]%;
      }
      RawTyped(RawTyConstr(f,tl),_) -> {
        return %[tom_make_@`f@(@`compile(tl,vl)@)]%;
      }
      RawTyped(RawTyCase(s@RawTyped(_,sty),rls),ty) -> {
        int counter = scount++;
        return %[((new Value<@`type(ty)@>() {
          public @`type(ty)@ value() {
            final @`type(sty)@ _subject@counter@ = @`compile(s,vl)@;
            %match(_subject@counter@) { @`compile(rls,vl)@ }
            throw new RuntimeException("non-exhaustive patterns");
          }
        }).value())]%;
      }
      RawTyped(RawTyFix(RawTyfixpoint(x,ty,u)),_) -> {
        return %[((new Value<@`type(ty)@>() {
          public final Value<@`type(ty)@> @`x@ = this;
          public @`type(ty)@ value() {
            return @`compile(u,FixVarList(x,vl*))@;
          }
        }).value())]%;
      }
    }
    return null;
  }
}
