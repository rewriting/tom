import aterm.pure.*;
import aterm.*;
import adt.builtin.term.*;
import adt.builtin.term.types.*;

public class ApiString {

  private Factory factory;
  
  %include { term.tom }

  public ApiString(Factory factory) {
    this.factory = factory;
  }

  public Factory getTermFactory() {
    return factory;
  }

  public void run() {

    String n = "Roger";
    Term t = `Name("Albert");

    matchBlock1: {
      %match(Term t) {
        Name("Albert") -> { System.out.println("Albert"); break matchBlock1; }
        Name("Roger")  -> { System.out.println("Roger"); break matchBlock1;}
        _ -> { System.out.println("Unknonw"); }
      }
    }
    
    matchBlock2: {
      %match(String n) {
        "Albert" -> { System.out.println("Albert"); break matchBlock2; }
        "Roger"  -> { System.out.println("Roger"); break matchBlock2;}
        _ -> { System.out.println("Unknonw"); }
      }
    }

  }
  
  public final static void main(String[] args) {
    ApiString test = new ApiString(new Factory(new PureFactory()));
    test.run();
  }
  
}
