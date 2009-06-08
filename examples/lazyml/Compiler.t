package lazyml;

import tom.library.sl.*;
import lazyml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Compiler {

  %include { lambda/lambda.tom }
  %include { sl.tom }

/*
  private static String type(RawLType ty) {
    %match(ty) {
      RawAtom(s) -> { return `s; }
      RawTypeVar(i) -> { return "A" + `i; }
      RawArrow(a,b) -> { return "Fun<" + `type(a) + "," + `type(b) + ">"; }
    }
    return null;
  }
*/
  
  private static int cptr = 0;
  private static String freshSubject() {
    return "_sub" + (cptr++);
  }

  private static String compile(RawFTermList l) {
    return "(new Object[] {" + compileAux(l) + "})";
  }
  private static String compileAux(RawFTermList l) {
    %match(l) {
      RawFTermList() -> { return ""; }
      RawFTermList(x) -> { return `compile(x); }
      RawFTermList(x,xs*) -> { return `compile(x) + "," + `compileAux(xs); }
    }
    return null;
  }

  private static String compileArgs(RawFTermList l) {
    %match(l) {
      RawFTermList() -> { return ""; }
      RawFTermList(x) -> { return "force(" + `compile(x) + ")"; }
      RawFTermList(x,xs*) -> { return "force(" + `compile(x) + ")," + `compileArgs(xs); }
    }
    return null;
  }

  private static String compile(RawFRules l, String s) {
    %match(l) {
      RawFRList() -> { return "throw new RuntimeException(\"non exhaustive patterns for " + s +"\");"; }
      RawFRList(RawFRule(RawFPVar(x,_),rhs),_*) -> { 
        return %[final Object @`x@ = @`s@; return @`compile(rhs)@;]%;
      }
      RawFRList(RawFRule(RawFPFun(c,vars),rhs),xs*) -> {
        String assigns = "";
        int i = 0;
        %match(vars) {
          RawFPList(_*,RawFPVar(x,_),_*) -> { 
            assigns += %[final Object @`x@ = ((C)@`s@).c[@`i@];]%; 
            i++;
          }
        }
        return %[
          if (((C)@`s@).hash == @`c.hashCode()@) /* @`c@ */ {
            @assigns@
            return @`compile(rhs)@;
          } else { @`compile(xs,s)@ }]%;
      }
    }
    return null;
  }

  public static String compile(RawFTerm t) {
    %match(t) {
      RawFVar(x) -> { 
        return `x;
      }
      RawFApp(u,v) -> { 
        return %[((F)(@`compile(u)@)).f(@`compile(v)@)]%;
      }
      RawFAbs(RawFLam(x,_,u)) -> {
        return %[
          new F() {
            public Object f(final Object @`x@) {
              return @`compile(u)@;
            }
          }]%;
      }
      RawFTAbs(RawFTLam(_,u)) -> {
        return `compile(u);
      }
      RawFTApp(u,_) -> {
        return `compile(u);
      }
      RawFConstr("Unit",RawFTermList()) -> {
        return null;
      }
      RawFConstr(f,tl) -> {
        return %[(new C("@`f@",@`f.hashCode()@,@`compile(tl)@))]%;
      }
      RawFPrimFun(f,tl) -> {
        String fname = `f.substring(1);
        return %[@fname@(@`compileArgs(tl)@)]%;
      }
      RawFLit(i) -> {
        return ""+`i;
      }
      RawFStr(s) -> {
        return "\"" + `s + "\"";
      }
      RawFCase(s,rls) -> {
        String sub = freshSubject();
        return %[
          (new V() {
            public Object v() {
              final Object @sub@ = @`compile(s)@;
              @`compile(rls,sub)@
            }
          }).v()]%;
      }
      RawFFix(RawFFixpoint(f,_,RawFAbs(RawFLam(x,_,u)))) -> {
        return %[
          new F() {
            public Object f(final Object @`x@) {
              final Object @`f@ = this;
              return @`compile(u)@;
            }
          }]%;
      }
      RawFLet(RawFLetin(x,_,u,v)) -> {
        return %[
          (new V() {
            public Object v() {
              final Object @`x@ = @`compile(u)@;
              return @`compile(v)@;
            }
          }).v()]%;
      }
    }
    return null;
  }
}
