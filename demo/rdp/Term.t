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
        
    L = conc(T*)

  }

  public final static void main(String[] args) {
    //L s = `conc(a(),b(),c(),a(),b());
    String s = "abc" + "bc";
    System.out.println("s = " + s);

    %match(s) {
      concString(_*,x,_*,x,_*) -> { System.out.println("x = " + `x); }
    }
  }
 
  public static T rule(T t) {
    %match(t) {
      f(a(),y) -> { return `f(b(),y); }
    }
    return t;
  }

}

