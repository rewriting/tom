import test.sig.types.*;

interface Fun<A,B> { B apply(A x); }
interface Value<A> { A value(); }

public class Test {

    %gom {
      module sig
      abstract syntax

      Nat = Z() | S(n:Nat)
    }

  /* java Main < apply_n_times.mml outputs
    ('a15 -> 'a15) -> Nat -> 'a15 -> 'a15
    on the error channel */
  public static class ApplyN<A15> {
    public Fun<Fun<A15,A15>,Fun<Nat,Fun<A15,A15>>> apply_n_times() {
      return %include{ apply_n_times.tom };
    }
  }

  public static void main(String[] args) {
    Nat fib10 = %include{ fib10.tom } ;
    Fun<Nat,Nat> plus3 = %include{ plus3.tom } ;
    System.out.println("fib 10 = " + fib10);
    System.out.println("plus 3 4 = " + plus3.apply(`S(S(S(S(Z()))))));
    // We instanciate ApplyN for Nat
    System.out.println("ntimes 2 (plus 3) 1 = " + new ApplyN<Nat>().apply_n_times().apply(plus3).apply(`S(S(Z()))).apply(`S(Z())));
  }
}
