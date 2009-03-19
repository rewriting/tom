import aterm.*;
import aterm.pure.*;

import foo.*;
import foo.types.*;

import tom.library.utils.IdConverter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class TypeInt {

  private static PureFactory factory = SingletonFactory.getInstance();

  private static FooConverter fooConverter = new FooConverter();
  private static IdConverter idConverter = new IdConverter();
  
  private static String[] input = { "k(42)",  "k(g(42))", "k(\"42\")", "k(42.5)", "f(42,b())", "h(42)" } ;

//input[0] => k(42)

  @Test
  public void intFooConverterInput0() {
    ATerm at = factory.parse(input[0]);
    ATerm arg = ((ATermAppl)at).getArgument(0);
    ATermInt atint = (ATermInt) arg;
    
    T t = T.fromTerm(at,fooConverter); // other converter
    
    assertEquals(t.gett4(),42);
    ATerm newAt = t.toATerm();
    ATerm newArg = ((ATermAppl)newAt).getArgument(0);
    ATermInt newAtint = (ATermInt) newArg;
    assertEquals(atint.getInt(), newAtint.getInt());
  }

  @Test
  public void intIdConverterInput0() {
    ATerm at = factory.parse(input[0]);
    ATerm arg = ((ATermAppl)at).getArgument(0);
    ATermInt atint = (ATermInt) arg;

    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    
    assertEquals(t.gett4(),42);
    ATerm newAt = t.toATerm();
    ATerm newArg = ((ATermAppl)newAt).getArgument(0);
    ATermInt newAtint = (ATermInt) newArg;
    assertEquals(atint.getInt(), newAtint.getInt());
  }

  @Test
  public void intNoConverterInput0() {
    ATerm at = factory.parse(input[0]);
    ATerm arg = ((ATermAppl)at).getArgument(0);
    ATermInt atint = (ATermInt) arg;
    
    T t = T.fromTerm(at); // implicite IdConverter
    
    assertEquals(t.gett4(),42);
    ATerm newAt = t.toATerm();
    ATerm newArg = ((ATermAppl)newAt).getArgument(0);
    ATermInt newAtint = (ATermInt) newArg;
    assertEquals(atint.getInt(), newAtint.getInt());
  }


//input[1] => k(g(42))

  @Test
  public void intFooConverterInput1() {
    ATerm at = factory.parse(input[1]);
    ATerm argk = ((ATermAppl)at).getArgument(0);
    ATerm argg = ((ATermAppl)argk).getArgument(0);
    ATermInt atint = (ATermInt) argg;
    
    T t = T.fromTerm(at,fooConverter); // other converter
    
    assertEquals(t.gett4(),42);
    ATerm newAt = t.toATerm();
    ATerm newArg = ((ATermAppl)newAt).getArgument(0);
    ATermInt newAtint = (ATermInt) newArg;
    assertEquals(atint.getInt(), newAtint.getInt());
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    //ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at); // implicite IdConverter
    //ATerm newAt = t.toATerm();
  }


//input[2] => k("42")

  @Test(expected=RuntimeException.class)
  public void intFooConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,fooConverter); // other converter
    //ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    //ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at); // implicite IdConverter
    //ATerm newAt = t.toATerm();
  }


//input[3] => k(42.5)

  @Test(expected=RuntimeException.class)
  public void intFooConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,fooConverter); // other converter
    //ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    //ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at); // implicite IdConverter
    //ATerm newAt = t.toATerm();
  }


//input[4] => f(42,b())  // better in TypeString ?

  @Test(expected=RuntimeException.class)
  public void intFooConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,fooConverter); // other converter
    //ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    //ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at); // implicite IdConverter
    //ATerm newAt = t.toATerm();
  }

//input[5] => h(42)

  @Test(expected=IllegalArgumentException.class)
  public void intFooConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,fooConverter); // other converter
    //ATerm newAt = t.toATerm();
  }

  @Test(expected=IllegalArgumentException.class)
  public void intIdConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    //ATerm newAt = t.toATerm();
  }

  @Test(expected=IllegalArgumentException.class)
  public void intNoConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at); // implicite IdConverter
    //ATerm newAt = t.toATerm();
  }

} // TypeInt
