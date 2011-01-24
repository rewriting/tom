import main.peano.types.*;
public class Peano {
  private static boolean tom_equal_term_Nat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Nat(Object t) {return  (t instanceof peano.peano.types.Nat) ;}private static  peano.peano.types.Nat  tom_make_zero() { return  peano.peano.types.nat.zero.make() ;}  




  public final static void main(String[] args) {
    Nat z = tom_make_zero();
  }
}

