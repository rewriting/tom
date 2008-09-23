import tom.library.sl.*;
import lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Printer {

  %include { lambda/Lambda.tom }

  public static String pretty(Value v) {
    %match(v) {
      VUnit() -> { return "()"; }
      VInt(i) -> { return `i+""; }
      VBool(b) -> { return `b+""; }
      VClos[] -> { return "<fun>"; }
    }
    return null;
  }

  public static String pretty(RawLTerm t) {
    %match (t) {
      RawEq(x,y) -> { return %[(@`pretty(x)@ = @`pretty(y)@)]%; }
      RawPlus(x,y) -> { return %[(@`pretty(x)@ + @`pretty(y)@)]%; }
      RawMinus(x,y) -> { return %[(@`pretty(x)@ - @`pretty(y)@)]%; }
      RawTimes(x,y) -> { return %[(@`pretty(x)@ * @`pretty(y)@)]%; }
      RawGT(x,y) -> { return %[(@`pretty(x)@ > @`pretty(y)@)]%; }
      RawLT(x,y) -> { return %[(@`pretty(x)@ < @`pretty(y)@)]%; }
      RawOr(x,y) -> { return %[(@`pretty(x)@ || @`pretty(y)@)]%; }
      RawAnd(x,y) -> { return %[(@`pretty(x)@ && @`pretty(y)@)]%; }
      RawLNot(x) -> { return %[(not @`pretty(x)@)]%; }
      RawTrue() -> { return "true"; }
      RawFalse() -> { return "false"; }
      RawInteger(i) -> { return ""+`i; }
      RawBranch(c,a,b) -> { return %[(if @`pretty(c)@ then @`pretty(a)@ else @`pretty(b)@)]%; }
      RawVar(x) -> { return `x; }
      RawAbs(Rawlam(x,u)) -> { return %[(fun @`x@ -> @`pretty(u)@)]%; }
      RawFix(Rawfixpoint(x,u)) -> { return %[(fix @`x@ @`pretty(u)@)]%; }
      RawApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      RawLet(Rawletin(x,u,v)) -> { 
        return %[(let @`x@ = @`pretty(u)@ in @`pretty(v)@)]%; }
      RawCallCC(v) -> { return %[(callcc @`pretty(v)@)]%; }
      RawThrow(a,b) -> { return %[(throw @`pretty(a)@ @`pretty(b)@)]%; }
      RawPrint(x) -> { return %[(print @`pretty(x)@)]%; }
      RawUnit() -> { return "()"; }
    }
    return null;
  }
}
