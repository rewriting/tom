import java.lang.*;
import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;
import aterm.pure.PureFactory;
import aterm.pure.SingletonFactory;
import tom.library.utils.ATermConverter;
import foo.*;
import foo.types.*;

public class FooConverter implements ATermConverter {

  public static PureFactory factory = SingletonFactory.getInstance();

  /**
   * Method from ATermConverter interface
   */
  public ATerm convert(ATerm at) {
    switch(at.getType()) {
      case ATerm.APPL:
      ATermAppl appl = (ATermAppl) at;
      String name = appl.getName();
      ATermList args = appl.getArguments();
      if(name.equals("g")) { // case : "g(x)) -> x" whatever is the type of x / arity = 1
        at = appl.getArgument(0);
      }
    }
    return at;
  } //convert

} //FooConverter
