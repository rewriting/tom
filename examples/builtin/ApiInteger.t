import aterm.*;
import Builtin.term.*;
import Builtin.term.types.*;
import aterm.pure.PureFactory;

public class ApiInteger {

  private Factory factory;
  
  %include { term.tom }

  public ApiInteger(Factory factory) {
    this.factory = factory;
  }

  public Factory getTermFactory() {
    return factory;
  }

  public void run() {

    int n = 32;
    Term t = `Age(10);

    matchBlock1: {
      %match(Term t) {
        Age(10) -> { System.out.println("10"); break matchBlock1; }
        Age(32) -> { System.out.println("32"); break matchBlock1;}
        _ -> { System.out.println("Unknown"); }
      }
    }
    
    matchBlock2: {
      %match(int n) {
        10 -> { System.out.println("10"); break matchBlock2; }
        32 -> { System.out.println("32"); break matchBlock2;}
        _ -> { System.out.println("Unknown"); }
      }
    }
  }
  
  public final static void main(String[] args) {
    ApiInteger test = new ApiInteger(new Factory(new PureFactory(16)));
    test.run();
  }
  
}
