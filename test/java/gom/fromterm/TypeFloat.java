import aterm.*;
import aterm.pure.*;

import foo.*;
import foo.types.*;

import tom.library.utils.IdConverter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class TypeFloat {

  private static PureFactory factory = SingletonFactory.getInstance();

  private static FooConverter fooConverter = new FooConverter();
  private static IdConverter idConverter = new IdConverter();
  private static final float floatComparator = (float)42.1234;
  
  private static String[] input = { "j(g(42.1234567890))", "j(42.1234567890)", "j(42)", "j(42.24)", "j(\"42.24\")", "h(42.24)" } ;

//input[0] => j(g(42.1234567890))

  @Test
  public void floatFooConverterInput() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t instanceof foo.types.t.j);
    assertEquals(floatComparator,((foo.types.t.j)t).gett5(),(float)0.0001);
    ATerm newAt = t.toATerm();
    ATermReal arg = (ATermReal)((ATermAppl)newAt).getArgument(0);
    assertEquals(floatComparator,(float)arg.getReal(),(float)0.0001);
   }

  @Test(expected=RuntimeException.class)
  public void floatIdConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void floatNoConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at); // implicite IdConverter
   }


//input[1] => j(42.1234567890)
  @Test
  public void floatFooConverterInput1() {
    ATerm at = factory.parse(input[1]);
     T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t instanceof foo.types.t.j);
    assertEquals(floatComparator,((foo.types.t.j)t).gett5(),(float)0.0001);
    ATerm newAt = t.toATerm();
    ATermReal arg = (ATermReal)((ATermAppl)newAt).getArgument(0);
    assertEquals(floatComparator,(float)arg.getReal(),(float)0.0001);
  }

  @Test
  public void floatIdConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertTrue(t instanceof foo.types.t.j);
    assertEquals(floatComparator,((foo.types.t.j)t).gett5(),(float)0.0001);
    ATerm newAt = t.toATerm();
    ATermReal arg = (ATermReal)((ATermAppl)newAt).getArgument(0);
    assertEquals(floatComparator,(float)arg.getReal(),(float)0.0001);
  }

  @Test
  public void floatNoConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertTrue(t instanceof foo.types.t.j);
    assertEquals(floatComparator,((foo.types.t.j)t).gett5(),(float)0.0001);
    ATerm newAt = t.toATerm();
    ATermReal arg = (ATermReal)((ATermAppl)newAt).getArgument(0);
    assertEquals(floatComparator,(float)arg.getReal(),(float)0.0001);
  }


//input[2] => j(42)

  @Test(expected=RuntimeException.class)
  public void floatFooConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void floatIdConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void floatNoConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at); // implicite IdConverter
  }


//input[3] => j(42.24)

  @Test
  public void floatFooConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertTrue(t instanceof foo.types.t.j);
    assertEquals(42.24,((foo.types.t.j)t).gett5(),0.001);
    ATerm newAt = t.toATerm();
    ATermReal arg = (ATermReal)((ATermAppl)newAt).getArgument(0);
    assertEquals(42.24,(float)arg.getReal(),0.001);
  }

  @Test
  public void floatIdConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertTrue(t instanceof foo.types.t.j);
    assertEquals(42.24,((foo.types.t.j)t).gett5(),0.001);
    ATerm newAt = t.toATerm();
    ATermReal arg = (ATermReal)((ATermAppl)newAt).getArgument(0);
    assertEquals(42.24,(float)arg.getReal(),0.001);
  }

  @Test
  public void floatNoConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertTrue(t instanceof foo.types.t.j);
    assertEquals(42.24,((foo.types.t.j)t).gett5(),0.001);
    ATerm newAt = t.toATerm();
    ATermReal arg = (ATermReal)((ATermAppl)newAt).getArgument(0);
    assertEquals(42.24,(float)arg.getReal(),0.001);
  }


//input[4] => j(\"42.24\")

  @Test(expected=RuntimeException.class)
  public void floatFooConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void floatIdConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void floatNoConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[5] => h(42.24)

  @Test(expected=IllegalArgumentException.class)
  public void floatFooConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=IllegalArgumentException.class)
  public void floatIdConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=IllegalArgumentException.class)
  public void floatNoConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

} // TypeFloat
