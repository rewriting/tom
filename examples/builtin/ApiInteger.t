import aterm.*;
import java.util.*;
import adt.*;

public class ApiInteger {

  private TermFactory factory;
  
  %include { adt/term.tom }

  public ApiInteger(TermFactory factory) {
    this.factory = factory;
  }

  public TermFactory getTermFactory() {
    return factory;
  }

  public void run() {

    int n = 32;
    Term t = `Age(10);

    matchBlock1: {
      %match(Term t) {
        Age(10) -> { System.out.println("10"); break matchBlock1; }
        Age(32) -> { System.out.println("32"); break matchBlock1;}
        _ -> { System.out.println("Unknonw"); }
      }
    }
    
    matchBlock2: {
      %match(int n) {
        10 -> { System.out.println("10"); break matchBlock2; }
        32 -> { System.out.println("32"); break matchBlock2;}
        _ -> { System.out.println("Unknonw"); }
      }
    }
  }
  
  public final static void main(String[] args) {
    ApiInteger test = new ApiInteger(new TermFactory(16));
    test.run();
  }
  
}
