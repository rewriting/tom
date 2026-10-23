import aterm.*;
import aterm.pure.*;

import foo.*;
import foo.types.*;

import tom.library.utils.IdConverter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class TypeBoolean {

  private static PureFactory factory = SingletonFactory.getInstance();

  private static FooConverter fooConverter = new FooConverter();
  private static IdConverter idConverter = new IdConverter();
  
  private static String[] input = { "o(g(0))",  "o(g(1))", "o(0)", "o(-42)", "g(o(t))", "g(o(tr))", "g(o(\"tr\"))", "h(o(1))" } ;

//input[0] => o(g(0))

  @Test
  public void booleanFooConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertFalse(t.gett10());
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

//input[1] => o(g(1))

  @Test
  public void booleanFooConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t.gett10());
  }

  @Test(expected=RuntimeException.class)
  public void booleanIdConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void booleanNoConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[2] => o(0)

  @Test
  public void booleanFooConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertFalse(t.gett10());
  }

  @Test
  public void booleanIdConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertFalse(t.gett10());
  }

  @Test
  public void booleanNoConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertFalse(t.gett10());
  }

//input[3] => o(-42)

  @Test
  public void booleanFooConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t.gett10());
  }

  @Test
  public void booleanIdConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertTrue(t.gett10());
  }

  @Test
  public void booleanNoConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertTrue(t.gett10());
  }

//input[4] => g(o(t))

  @Test(expected=RuntimeException.class)
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


//input[5] => g(o(tr))

  @Test(expected=RuntimeException.class)
  public void booleanFooConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=IllegalArgumentException.class)
  public void booleanIdConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=IllegalArgumentException.class)
  public void booleanNoConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[6] => g(o("tr"))

  @Test(expected=RuntimeException.class)
  public void booleanFooConverterInput6() {
    ATerm at = factory.parse(input[6]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=IllegalArgumentException.class)
  public void booleanIdConverterInput6() {
    ATerm at = factory.parse(input[6]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=IllegalArgumentException.class)
  public void booleanNoConverterInput6() {
    ATerm at = factory.parse(input[6]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[7] => h(o(1))

  @Test
  public void booleanFooConverterInput7() {
    ATerm at = factory.parse(input[7]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t.gett3() instanceof foo.types.t.o);
    assertTrue(((foo.types.t.o)t.gett3()).gett10());
  }

  @Test
  public void booleanIdConverterInput7() {
    ATerm at = factory.parse(input[7]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertTrue(t.gett3() instanceof foo.types.t.o);
    assertTrue(((foo.types.t.o)t.gett3()).gett10());
  }

  @Test
  public void booleanNoConverterInput7() {
    ATerm at = factory.parse(input[7]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertTrue(t.gett3() instanceof foo.types.t.o);
    assertTrue(((foo.types.t.o)t.gett3()).gett10());
  }

} // TypeBoolean
