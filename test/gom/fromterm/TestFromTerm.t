import aterm.*;
import aterm.pure.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    TypeString.class,
    TypeInt.class,
    TypeBoolean.class,
    TypeChar.class,
    TypeLong.class,
    TypeDouble.class,
    TypeFloat.class
    }) //will contain one class per builtin type to test
public class TestFromTerm {

 /*
   We need to test (at least) :
   each bug
 x each type (builtin  = string, int, float, boolean, long, double, char, ATerm, ATermList)
 x with a FooConverter, with the IdConverter and without a converter (should be the same thing as with the IdConverter)  

 => 3 bugs (at least 3 patterns to test)
  x 8 builtin types
  x 3 "converters"
  = 72 tests (at least)

 */

  /* 
     Bug1 : convert method not called in this case :
     gom signature : T = f(String) | a()
     input : h("a")
   */

  /* 
     Bug2 : f(g(a)) -> f("g")
   */

  /*
    Bug3 : f("a") -> f(a())
    (bug appeared when a string was equal to an identifier)
   */

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestFromTerm.class.getName());
  }
}
