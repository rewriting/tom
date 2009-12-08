package lazyml;

import tom.library.sl.*;
import lazyml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class LazyCompiler {

  %include { lambda/lambda.tom }
  %include { sl.tom }

  private static int cptr = 0;
  private static String freshSubject() {
    return "_sub" + (cptr++);
  }

  private static String compile(RawFTermList l, FrozenVars frv) {
    return "(new Object[] {" + compileAux(l,frv) + "})";
  }

  private static String compileAux(RawFTermList l, FrozenVars frv) {
    %match(l) {
      RawFTermList() -> { return ""; }
      RawFTermList(x) -> { return `compile(x,frv); }
      RawFTermList(x,xs*) -> { 
        return `compile(x,frv) + "," + `compileAux(xs,frv); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static String compileArgs(RawFTermList l, FrozenVars frv) {
    %match(l) {
      RawFTermList() -> { return ""; }
      RawFTermList(x) -> { return "force(" + `compile(x,frv) + ")"; }
      RawFTermList(x,xs*) -> { 
        return "force(" + `compile(x,frv) + ")," + `compileArgs(xs,frv); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static String 
    compile(RawFRules l, String s, FrozenVars frv) {
    %match(l) {
      /*
      RawFRList() -> {
        return "throw new RuntimeException(\"should never happen\");";
      }
      */
      RawFRList(RawFRule(RawFDefault(),rhs),_*) -> { 
        return %[return @`compile(rhs,frv)@;]%;
      }
      RawFRList(RawFRule(RawFPFun(c,vars),rhs),xs*) -> {
        String assigns = "";
        FrozenVars frv1 = frv;
        int i = 0;
        %match(vars) {
          RawFPVarList(_*,RawFPVar(x,_),_*) -> { 
            assigns += %[final Object @`x@ = ((C)@`s@).c[@`i@];]%; 
            frv1 = `FrozenVarList(x,frv1*);
            i++;
          }
        }
        return %[
          if (((C)@`s@).f.equals("@`c@")) {
            @assigns@
            return @`compile(rhs,frv1)@;
          } else { @`compile(xs,s,frv)@ }]%;
      }
    }
    throw new RuntimeException("non exhaustive patterns: " + Printer.prettyFRules(l));
  }

  public static String compile(RawFTerm t) {
    return `compile(t,FrozenVarList());
  }

  private static String freeze(String s) {
    return %[
      (new Thunk() {
        final public Object val() {
          return @s@;
        }
      })]%;
  }

  private static String unfreeze(String s) {
    return %[((Thunk)@s@).get()]%;
  }

  private static String compile(RawFTerm t, FrozenVars frv) {
    %match(t) {
      RawFVar(x) -> { 
        return ((Collection) frv).contains(`x) ? `x : `unfreeze(x);
      }
      RawFApp(u,v) -> { 
        return %[((F)(@`compile(u,frv)@)).f(@`freeze(compile(v,frv))@)]%;
      }
      RawFAbs(RawFLam(x,_,u)) -> {
        return %[
          new F() {
            public Object f(final Object @`x@) {
              return @`compile(u,frv)@;
            }
          }]%;
      }
      RawFTAbs(RawFTLam(_,u)) -> {
        return `compile(u,frv);
      }
      RawFTApp(u,_) -> {
        return `compile(u,frv);
      }
      RawFConstr(f,_,tl) -> {
        return freeze(%[(new C("@`f@",@`compile(tl,frv)@))]%);
      }
      RawFPrimFun(f,tl) -> {
        String fname = `f.substring(1);
        return freeze(%[@fname@(@`compileArgs(tl,frv)@)]%);
      }
      RawFLit(i) -> {
        return freeze(""+`i);
      }
      RawFChr(c) -> {
        return freeze("'" + `c + "'");
      }
      RawFStr(s) -> {
        return freeze("\"" + `s + "\"");
      }
      RawFCase(s,rls) -> {
        String sub = freshSubject();
        return %[
          (new V() {
            public Object v() {
              final Object @sub@ = @`unfreeze(compile(s,frv))@;
              @`compile(rls,sub,frv)@
            }
          }).v()]%;
      }
      RawFFix(RawFFixpoint(f,_,u)) -> {
        return %[
          (new Thunk() {
            final public Object val() {
              final Object @`f@ = this;
              return @`compile(u,frv)@;
            }
          }).get()]%;
      }
      let@RawFLet[] -> {
        %match (compileLets(let,frv)) {
          RawLets(prelude,concl) -> {
            return %[
              (new V() {
                 @`prelude@
                 public Object v() {
                   return @`compile(concl,frv)@;
                 }
               }).v()]%;
          }
        }
      }
      RawFError(m) -> {
        return %[
          (new V() { 
            public Object v() { 
              throw new RuntimeException("@`m@"); 
            }
          }).v()]%;
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static RawLets compileLets (RawFTerm t, FrozenVars frv) {
      %match(t) {
        RawFLet(RawFLetin(x,_,u,v)) -> {
          %match(compileLets(v,frv)) {
            RawLets(s,c) -> {
              String s1 = %[
                public final Object @`x@ = @freeze(`compile(u,frv))@;
              ]%;
              return `RawLets(s1+s,c);
            }
          }
        }
        o -> { return `RawLets("",o); }
      }
      throw new RuntimeException("non exhaustive patterns");
    }
}
