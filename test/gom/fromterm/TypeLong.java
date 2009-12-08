import aterm.*;
import aterm.pure.*;

import foo.*;
import foo.types.*;

import tom.library.utils.IdConverter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class TypeLong {

  private static PureFactory factory = SingletonFactory.getInstance();

  private static FooConverter fooConverter = new FooConverter();
  private static IdConverter idConverter = new IdConverter();
  
  private static String[] input = { "l(g(9223372036854775807l))", "l(-9223372036854775808l)", "l(42.5)", "l(\"9223372036854775807l\")", "d(9223372036854775807l)", "j(9223372036854775807l)", "i(9223372036854775807l)" } ;

  public static final long MAX = java.lang.Long.MAX_VALUE; // ==  9223372036854775807L
  public static final long MIN = java.lang.Long.MIN_VALUE; // == -9223372036854775808L

//input[0] => l(g(9223372036854775807l))
  @Test
  public void intFooConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t instanceof foo.types.t.l);
    assertEquals(((foo.types.t.l)t).gett7(),MAX);
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at); // explicite IdConverter 
  }

//input[1] => l(-92233720368547758081)
  @Test
  public void intFooConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t instanceof foo.types.t.l);
    assertEquals(((foo.types.t.l)t).gett7(),MIN);
  }

  @Test
  public void intIdConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertTrue(t instanceof foo.types.t.l);
    assertEquals(((foo.types.t.l)t).gett7(),MIN);
  }

  @Test
  public void intNoConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertTrue(t instanceof foo.types.t.l);
    assertEquals(((foo.types.t.l)t).gett7(),MIN);
  }


//input[2] => l(42.5)
  @Test(expected=RuntimeException.class)
  public void intFooConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[3] =>l(\"9223372036854775807l\")

  @Test(expected=RuntimeException.class)
  public void intFooConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[4] => d(9223372036854775807l)

  @Test(expected=RuntimeException.class)
  public void intFooConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[5] => j(9223372036854775807l)

  @Test(expected=RuntimeException.class)
  public void intFooConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[6] => i(9223372036854775807l)

  @Test(expected=RuntimeException.class)
  public void intFooConverterInput6() {
    ATerm at = factory.parse(input[6]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void intIdConverterInput6() {
    ATerm at = factory.parse(input[6]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void intNoConverterInput6() {
    ATerm at = factory.parse(input[6]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

} // TypeLong
