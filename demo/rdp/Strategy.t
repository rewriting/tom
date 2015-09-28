import strategy.term.types.*;
import tom.library.sl.*;
import java.util.*;
class Strategy {
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
    T t = `f(a(),g(a()));
    try {
      T result = (T) `Repeat(OnceBottomUp(ReplaceAB())).visit(t);
      System.out.println("result = " + result);
    } catch(VisitFailure e) {
      System.out.println("failure on " + t);
    }
  }
  
  // Replace a by b
  // ReplaceAB : a -> b
  %strategy ReplaceAB() extends Fail() {
    visit T {
      a() -> { return `b(); }
    }
  }










  %strategy TraceAB() extends Identity() {
    visit T {
      x -> { System.out.println("Application de a->b: " + getEnvironment().getPosition());  }
    }
  }
 
  %strategy Collect(c:Collection) extends Identity() {
    visit T {
      g(x) -> { c.add(`x); }
    }
  }
}
