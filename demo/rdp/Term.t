import term.term.types.*;
public class Term {
 
  %gom {
    module Term
    imports String
    abstract syntax

    T = | a()
        | b()
        | c()
        | f(x1:T, x2:T) 
        | g(x1:T)
    
  }

  public final static void main(String[] args) {
    T s = `a();
    System.out.println("s = " + s);
  }

}

