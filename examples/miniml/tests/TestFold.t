import tom.library.sl.*;
import testfold.peano.types.*;

public class TestFold {
  
  %include { sl.tom }

  %gom {
    module peano
      abstract syntax 

      Nat = z()
          | s(n:Nat)

      Natlist = nlist(Nat*)
  }

  %include { plus.tom }
  %include { mult.tom }
  %include { fold.tom }

  private static int natToInt(Nat n, int i) {
    %match(n) { s(m) -> { return `natToInt(m,i+1); } }
    return i;
  }

  private static String prettyNat(Nat n) {
    return "" + natToInt(n,0);
  }

  private static String prettyList(Natlist l) {
    String res = "[";
    %match(Natlist l) {
      (_*,n,t*) -> {
        res += `prettyNat(n);
        if (`t.length() > 0) res += ",";
      }
    }
    return res + "]";
  }

  public static Nat intToNat(int i) {
    Nat res = `z();
    for(int j=0; j<i; j++) { res = `s(res); }
    return res;
  }
  
  public void run() {
    try {
      Natlist l = `nlist();
      Nat max = intToNat(8);
      for(Nat n=`s(z()); n != max ;n = `s(n)) {
        l = `nlist(l*,n);
      }
      System.out.println("l = " + `prettyList(l));

      Strategy fld = `fold();

      Strategy add = `plus();
      // addall = fold add 0
      Strategy addall = (Strategy) ((Strategy) fld.visit(add)).visit(`z());
      // n = addall l
      Nat n = (Nat) addall.visit(l);
      System.out.println("fold plus 0 l = " + prettyNat(n));

      Strategy times = (Strategy) `mult();
      n = (Nat) ((Strategy) ((Strategy) fld.visit(times)).visit(`s(z()))).visit(l);
      System.out.println("fold mult 1 l = " + prettyNat(n));

    } catch (VisitFailure ex) {
      System.out.println(ex);
    }
  }

  public static void main(String[] args) {
    TestFold t = new TestFold();
    t.run();
  }
}
