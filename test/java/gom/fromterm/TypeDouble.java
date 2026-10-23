import aterm.*;
import aterm.pure.*;

import foo.*;
import foo.types.*;

import tom.library.utils.IdConverter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class TypeDouble {

  private static PureFactory factory = SingletonFactory.getInstance();

  private static FooConverter fooConverter = new FooConverter();
  private static IdConverter idConverter = new IdConverter();
  private static final double doubleComparator = 42.24;
  
  private static String[] input = { "d(g(42.24))", "d(42.24)", "d(42)", "d(\"42.24\")", "h(42.24)" } ;

//input[0] => d(g(42.24)) 
  @Test
  public void booleanFooConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t instanceof foo.types.t.d);
    assertEquals(((foo.types.t.d)t).gett8(),doubleComparator,0);
  }

  @Test(expected=RuntimeException.class)
  public void booleanIdConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void booleanNoConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[1] => d(42.24)
  @Test
  public void booleanFooConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t instanceof foo.types.t.d);
    assertEquals(((foo.types.t.d)t).gett8(),doubleComparator,0);
  }

  @Test
  public void booleanIdConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertTrue(t instanceof foo.types.t.d);
    assertEquals(((foo.types.t.d)t).gett8(),doubleComparator,0);
  }

  @Test
  public void booleanNoConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertTrue(t instanceof foo.types.t.d);
    assertEquals(((foo.types.t.d)t).gett8(),doubleComparator,0);
  }

//input[2] => d(42)
  @Test(expected=RuntimeException.class)
  public void booleanFooConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void booleanIdConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void booleanNoConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at); // implicite IdConverter
  }


//input[3] => d(\"42.24\")
  @Test(expected=RuntimeException.class)
  public void booleanFooConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void booleanIdConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void booleanNoConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[4] => h(42.24)
  public void booleanFooConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=IllegalArgumentException.class)
  public void booleanIdConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=IllegalArgumentException.class)
  public void booleanNoConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

} // TypeDouble
