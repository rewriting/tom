package debugger;

import debugger.debug.sig.types.*;
import tom.library.utils.Viewer;
import tom.library.sl.*;

public class Debug {

  %include { sl_debug.tom }

  %gom {
    module sig
    abstract syntax

      S1 = f(x:S1) | a()
  }

  public static void main(String[] argv) {
    System.out.println(`f(a()));
    Strategy s = `TopDown(Try(Fail()));
    /*
    %match (s) {
      TopDown(Try(x)) -> { System.out.println("x = " + `x); }
    }
    */
    System.out.println(s);
    System.out.println("--- debug ---");
    Strategy s2 = debugger.Lib.weaveBasicDecorators(s);
    System.out.println("s2 = " + s2);
    try { s2.visit(`f(f(a()))); }
    catch (VisitFailure f) {  }
  }
}
