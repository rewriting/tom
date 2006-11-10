import match.term.types.*;

import java.io.*;

class Match {
  %gom {
    module Term
    abstract syntax
    T = a()
        | b()
        | f(t1:T)
        | h(t2:T)
        | g(left:T,right:T)
  }

  public static void main(String[] args) {
    Match essai = new Match();
    essai.test();
  }

  void test() {
    T test = `g(a(),b());
    T test2 = `g(a(),b());
    T test3 = `g(a(),b());
    %match(T test) {
      g(a(),b()) -> { System.out.println("a and b"); }
      g(x,b())   -> { System.out.println(`x); }
    }

    %match(test) {
      g[] -> {}
      x@g[] -> { System.out.println(`x); }
      f(h(x)) -> {System.out.println(`x); }
    }
  }
}
