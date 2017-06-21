import term.term.types.*;
import java.util.*;

public class Term {
%include { string.tom }

/*
  %gom {
    module Term
    imports String int
    abstract syntax

    T = | a()
        | b()
        | c()
        | f(x1:T, x2:T) 
        | g(x1:T)
        | h(v:int)
     
     L = conc( T* )
     M = somme( int* )
  }
*/
  public final static void main(String[] args) {
    String t = "abcabc";

    List l = new ArrayList();
  
    %match(t) {
      concString(_*,x,_*,x,_*) -> { 
        if(`x != 'a') {
          l.add(`x); 
        }
      }
    }
 
    System.out.println("t = " + t);
    System.out.println("l = " + l);

  }

}

