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

    L = conc( T* )

  }

  public final static void main(String[] args) {
    L s = `conc(a(),b(),c(),a(),b());
    System.out.println("s = " + s);
    %match(s) {
      conc(C1*,x,C2*,y,_*) -> {
        return `conc(C1*,y,C2*,x,)
      }
    }
    
  }
 
}

