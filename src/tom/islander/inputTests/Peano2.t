%include { peano/Peano.tom }

rule Nat peano.MyPeano.plus(Nat t1, Nat t2) {
//  %match(t2) {
    zero() << t2 -> { return t1; }
    suc(y) << t2 -> { return suc(plus(t1,y)); }
//  }
//    return null;
}

rule Nat peano.MyPeano.fib(Nat t) {
    zero() << t      -> { return suc(zero()); }
    suc(zero()) << t -> { return suc(zero()); }
    suc(suc(x)) << t -> { return plus(fib(x),fib(suc(x))); }
}
