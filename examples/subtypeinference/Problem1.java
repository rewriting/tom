import problem1.example.types.*;
public class Problem1{
  private static boolean tom_equal_term_Nat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Nat(Object t) {return  (t instanceof problem1.example.types.Nat) ;}private static boolean tom_equal_term_Float(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Float(Object t) {return  (t instanceof problem1.example.types.Float) ;}private static boolean tom_equal_term_Int(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Int(Object t) {return  (t instanceof problem1.example.types.Int) ;}private static  problem1.example.types.Nat  tom_make_zero() { return  problem1.example.types.nat.zero.make() ;}  












  public static void main(String[] args) {
    {{Object tomMatch1_0=tom_make_zero();if (tom_is_sort_Nat(tomMatch1_0)) {
 System.out.println((( problem1.example.types.Nat )tomMatch1_0)); }}}

  }
}
