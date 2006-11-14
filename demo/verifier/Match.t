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
    T test = `g(a(),b());
    %match(T test) {
      g(a(),b()) -> { System.out.println("a and b"); }
      g(x,b())   -> { System.out.println(`x); }
      //g[] -> {}
      //x@g[] -> { System.out.println(`x); }
      //g(x,x) -> {System.out.println(`x); }
    }

  }

}
