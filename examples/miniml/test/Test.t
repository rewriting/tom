import test.sig.types.*;

interface Fun<A,B> { B apply(A x); }
interface Value<A> { A value(); }

public class Test {

    %gom {
      module sig
      abstract syntax

      Nat = Z() | S(n:Nat)
      NatList = NList(Nat*)
    }

  /* java Main < apply_n_times.mml outputs
    ('a15 -> 'a15) -> Nat -> 'a15 -> 'a15
    on the error channel */
  public static class ApplyN<A15> {
    public Fun<Fun<A15,A15>,Fun<Nat,Fun<A15,A15>>> get() {
      return %include{ apply_n_times.tom };
    }
  }

  /* java Main < fold_left.mml outputs
     ('a9 -> Nat -> 'a9) -> 'a9 -> NatList -> 'a9
     on the error channel */
  public static class FoldLeft<A9> {
    public Fun<Fun<A9,Fun<Nat,A9>>,Fun<A9,Fun<NatList,A9>>> get() {
      return %include { fold_left.tom } ;
    }
  }

  public static int convert(Nat n) {
    %match(n) {
      Z() -> { return 0; }
      S(m) -> { return 1+`convert(m); }
    }
    throw new RuntimeException();
  }

  public static void main(String[] args) {
    Nat one = `S(Z());
    Nat two = `S(one);
    Nat three = `S(two);
    Nat four = `S(three);
    Nat five = `S(four);

    // basic computation
    Nat fib10 = %include{ fib10.tom } ;
    System.out.println("fib 10 = " + convert(fib10));

    // partial application
    Fun<Nat,Fun<Nat,Nat>> plus = %include { plus.tom };
    Fun<Nat,Nat> plus3 = plus.apply(three);
    System.out.println("plus 3 4 = " + convert(plus3.apply(four)));
    System.out.println("plus 3 5 = " + convert(plus3.apply(five)));
    
    // We instanciate ApplyN for Nat
    System.out.println("ntimes (plus 3) 4 1 = " + 
        convert(new ApplyN<Nat>().get().apply(plus3).apply(four).apply(one)));

    // sum as fold_left plus 0
    Fun<NatList,Nat> sum = new FoldLeft<Nat>().get().apply(plus).apply(`Z());
    System.out.println("sum [1,2,3,4,5] = " + convert(sum.apply(`NList(one,two,three,four,five))));
  }
}
