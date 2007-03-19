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
      L = conc( E* )
      E = a() | b() | c() | d()
  }
	
  public void test1() {
    L l = `conc(a(), b(), c(), a(), b());
    L l2 = `conc();
    // do not modify the layout
    // the problem was that space after the 'z*'
    %match(L l) {
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

  %op L abc() {}
  private static L abc() {
    return `conc(a(),b(),c());
  }

  public void test2() {
    assertTrue(
        "function of sort list",
        `conc(a(),b(),c()) == `conc(abc()));
    if(true) {
      return ;
    }
    fail("should not be there");
  }

}
