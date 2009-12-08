package lazyml;

import tom.library.sl.*;
import lazyml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Compiler {

  %include { lambda/lambda.tom }
  %include { sl.tom }

  private static int cptr = 0;
  private static String freshSubject() {
    return "_sub" + (cptr++);
  }

  private static String compile(RawFTermList l, RawFixVars fv) {
    return "(new Object[] {" + compileAux(l,fv) + "})";
  }
  private static String compileAux(RawFTermList l, RawFixVars fv) {
    %match(l) {
      RawFTermList() -> { return ""; }
      RawFTermList(x) -> { return `compile(x,fv); }
      RawFTermList(x,xs*) -> { return `compile(x,fv) + "," + `compileAux(xs,fv); }
    }
    return null;
  }

  private static String compileArgs(RawFTermList l, RawFixVars fv) {
    %match(l) {
      RawFTermList() -> { return ""; }
      RawFTermList(x) -> { return `compile(x,fv); }
      RawFTermList(x,xs*) -> { return `compile(x,fv) + "," + `compileArgs(xs,fv); }
    }
    return null;
  }

  private static String compile(RawFRules l, String s, RawFixVars fv) {
    %match(l) {
      RawFRList(RawFRule(RawFDefault(),rhs),_*) -> { 
        return %[return @`compile(rhs,fv)@;]%;
      }
      RawFRList(RawFRule(RawFPFun(c,vars),rhs),xs*) -> {
        String assigns = "";
        int i = 0;
        %match(vars) {
          RawFPVarList(_*,RawFPVar(x,_),_*) -> { 
            assigns += %[final Object @`x@ = ((C)@`s@).c[@`i@];]%; 
            i++;
          }
        }
        return %[
          if (((C)@`s@).f.equals("@`c@")) {
            @assigns@
            return @`compile(rhs,fv)@;
          } else { @`compile(xs,s,fv)@ }]%;
      }
    }
    return null;
  }

  public static String compile(RawFTerm t) {
    return `compile(t,RawFixVarList());
  }

  private static String compile(RawFTerm t, RawFixVars fv) {
    %match(t) {
      RawFVar(x) -> { 
        if (((Collection) fv).contains(`x)) return %[((V)@`x@).v()]%;
        else                                return `x;
      }
      RawFApp(u,v) -> { 
        return %[((F)(@`compile(u,fv)@)).f(@`compile(v,fv)@)]%;
      }
      RawFAbs(RawFLam(x,_,u)) -> {
        return %[
          new F() {
            public Object f(final Object @`x@) {
              return @`compile(u,fv)@;
            }
          }]%;
      }
      RawFTAbs(RawFTLam(_,u)) -> {
        return `compile(u,fv);
      }
      RawFTApp(u,_) -> {
        return `compile(u,fv);
      }
      RawFConstr(f,_,tl) -> {
        return %[(new C("@`f@",@`compile(tl,fv)@))]%;
      }
      RawFPrimFun(f,tl) -> {
        String fname = `f.substring(1);
        return %[@fname@(@`compileArgs(tl,fv)@)]%;
      }
      RawFLit(i) -> {
        return ""+`i;
      }
      RawFChr(c) -> {
        return "'" + `c + "'";
      }
      RawFStr(s) -> {
        return "\"" + `s + "\"";
      }
      RawFCase(s,rls) -> {
        String sub = freshSubject();
        return %[
          (new V() {
            public Object v() {
              final Object @sub@ = @`compile(s,fv)@;
              @`compile(rls,sub,fv)@
            }
          }).v()]%;
      }
      RawFFix(RawFFixpoint(f,_,u)) -> {
        return %[
          (new V() {
            public Object v() {
              final Object @`f@ = this;
              return @`compile(u,RawFixVarList(f,fv*))@;
            }
          }).v()]%;
      }
      let@RawFLet[] -> {
        %match (compileLets(let,fv)) {
          RawLets(prelude,concl) -> {
            return %[
              (new V() {
                 @`prelude@
                 public Object v() {
                   return @`compile(concl,fv)@;
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
    return null;
  }

  public static RawLets compileLets (RawFTerm t,RawFixVars fv) {
      %match(t) {
        RawFLet(RawFLetin(x,_,u,v)) -> {
          %match(compileLets(v,fv)) {
            RawLets(s,c) -> {
              String s1 = %[
                public final Object @`x@ = @`compile(u,fv)@;
              ]%;
              return `RawLets(s1+s,c);
            }
          }
        }
        o -> {
          return `RawLets("",o);
        }
      }
      return null;
    }

}
