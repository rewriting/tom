import junit.framework.TestCase;
import junit.framework.TestSuite;
import testbackquote.thing.*;
import testbackquote.thing.types.*;

public class TestBackQuote extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(Test.class));
  }
  
  %gom {
    module thing
      imports String
      abstract syntax
      Thing = conc( Elem* )
      Elem = a() | b() | c() | d()
  }
	
  public void test1() {
    Thing l = `conc(a(), b(), c(), a(), b());
    Thing l2 = `conc();
    %match(Thing l) {
      conc(
	  x*, 
	  b(), 
	  y*, 
	  a(), 
	  z*
	  ) -> {
	l2 = `conc(x*,
	    b(),
	    y*,
	    a(),
	    z*
	    );
      }
    }
    return;
  }

}
