import explore.term.types.*;
import java.util.*;
import tom.library.sl.*;

class Explore {
  %include { sl.tom }
  %include { java/util/types/Collection.tom }

  %gom {
    module Term
    abstract syntax

    T = | f(x1:T, x2:T) 
        | g(x1:T) 
        | a()
        | b()
        | c()
  }

  public final static void main(String[] args) {
    try {
    
      T t = `f(a(),g(f(b(),a())));
      System.out.println("t = " + t);

      Collection bag = new HashSet();
      `TopDown(ComputeNext(t,bag)).visit(t);
      System.out.println("next = " + bag);

    } catch(VisitFailure e) {
      System.out.println("failure");
    }
  }
  
  // Collect variable
  %strategy ComputeNext(t:T, c:Collection) extends Identity() {
    visit T {
      a() -> { c.add( getEnvironment().getPosition().getReplace(`b()).visitLight(`t) ); }
      a() -> { c.add( getEnvironment().getPosition().getReplace(`c()).visitLight(`t) ); }
    }
  }
  
}

/*
 * add a rule a -> c
 */