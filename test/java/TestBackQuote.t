import static org.junit.Assert.*;
import org.junit.Test;
import testbackquote.thing.*;
import testbackquote.thing.types.*;

public class TestBackQuote {
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestBackQuote.class.getName());
  }

  %gom {
    module thing
      imports String
      abstract syntax
      L = conc( E* )
      E = a() | b() | c() | d()
  }
	
  @Test
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

  @Test
  public void test2() {
    assertEquals(`conc(a(),b(),c()),`conc(abc*())); // was without *
  }

}
