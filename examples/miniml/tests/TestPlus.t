import tom.library.sl.*;
import testplus.peano.types.*;

public class TestPlus {
  
  %include { sl.tom }

  %gom {
    module peano
      abstract syntax

      Nat = z()
          | s(n:Nat)
  }

  %include { plus.tom }

  public void run() {
    try {
      // plus2 = (plus 2)
      Strategy plus2 = (Strategy) (new plus()).visit( `s(s(z())) );
      System.out.println("plus2 0 = " + plus2.visit( `z() ));
      System.out.println("plus2 2 = " + plus2.visit( `s(s(z())) ));
    } catch (VisitFailure ex) {
      System.out.println(ex);
    }
  }

  public static void main(String[] args) {
    TestPlus t = new TestPlus();
    t.run();
  }
}
