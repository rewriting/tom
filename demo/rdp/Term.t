import term.term.types.*;
public class Term {

  %gom {
    module Term
    imports String int
    abstract syntax

    T = | a()
        | b()
        | c()
        | f(x1:T, x2:T) 
        | g(x1:T)
  }

  public final static void main(String[] args) {
    T s = `f(a(),b());

   %match(s) {
     f(x,y) -> { s = `g(y); }
   }

    System.out.println("s = " + s);
  }

}

