import gates.logic.types.*;
  public class Gates {
    private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_Bool(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Bool(Object t) {return  (t instanceof gates.logic.types.Bool) ;}private static  gates.logic.types.Bool  tom_make_True() { return  gates.logic.types.bool.True.make() ;}private static  gates.logic.types.Bool  tom_make_False() { return  gates.logic.types.bool.False.make() ;}private static  gates.logic.types.Bool  tom_make_Xor( gates.logic.types.Bool  t0,  gates.logic.types.Bool  t1) { return  gates.logic.types.bool.Xor.make(t0, t1) ;}  






















    public final static void main(String[] args) {
    Bool b = tom_make_Xor(tom_make_True(),tom_make_False());
    System.out.println("b = " + b);
  }
}

