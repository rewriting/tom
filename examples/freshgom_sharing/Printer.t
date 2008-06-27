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

  /*
  private static String print(RLTerm t, int n) {
    %match(t) {
      RawVar(x) -> { return spaces(n) + `x; }
      RawAbs(x,u) -> { 
        return spaces(n)
          + "fun " + `x + " -> (\n" 
          + `print(u,n+1)
          + "\n" + spaces(n) + ")";
      }  
      RawApp(u,v) -> {
        return spaces(n)  
          + "(" + `print(u,0) + ") ("+ `print(v,0) + ")";
      }
      RawLet(x,u,v) -> {
        return spaces(n) + "let " + `x + " =\n" 
          + print(`u,n+1) 
          + "\n" + spaces(n) + "in\n" + print(`v,n+1);
      }
    }
    return null;
  }
  */

  public static String pretty(RLTerm t) {
    %match (t) {
      RawVar(x) -> { return `x; }
      RawAbs(Rawlam(x,u)) -> { return %[(fun @`x@ -> @`pretty(u)@)]%; }
      RawApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
      RawLet(Rawletin(x,u,v)) -> { 
        return %[(let @`x@ = @`pretty(u)@ in @`pretty(v)@)]%; 
      }
    }
    return null;
  }
}
