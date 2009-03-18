import aterm.*;
import aterm.pure.*;

import foo.*;
import foo.types.*;

import tom.library.utils.IdConverter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class TypeString {

  private static PureFactory factory = SingletonFactory.getInstance();

  private static FooConverter fooConverter = new FooConverter();
  private static IdConverter idConverter = new IdConverter();
  
  private static String[] input = { "h(\"a\")", "f(g(a))", "f(g(\"a\"),b())", "h(a)", "h(a())" } ;
 
  // input[3] id que input[4] (normalement)
  // tests IdConverter explicite et implicite (pas de conv) id (normalement)


  // autre possibilite que "expected ;
  /*
     ...
     try {
     blabla ... fromterm(...);
     fail( "My method didn't throw when I expected it to" );
     } catch (MyException expectedException) {
     }
   ...
   */

  /*public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestString.class.getName());
  } */


//input[0]

  @Test(expected=IllegalArgumentException.class)
  public void stringFooConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,fooConverter); // other converter
    ATerm newAt = t.toATerm();
  }

  @Test(expected=IllegalArgumentException.class)
  public void stringIdConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    ATerm newAt = t.toATerm();
  }

  @Test(expected=IllegalArgumentException.class)
  public void stringNoConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at); // implicite IdConverter
    ATerm newAt = t.toATerm();
  }


//input[1]

  @Test(expected=RuntimeException.class)
  public void stringFooConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,fooConverter); // other converter
    ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void stringIdConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void stringNoConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at); // implicite IdConverter
    ATerm newAt = t.toATerm();
  }


//input[2]

  @Test
  public void stringFooConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,fooConverter); // other converter
// if printed : t = f("a",b())
    assertEquals(t.toString(),"f(\"a\",b())");
// if printed t.toATerm = f("a",b)
    ATerm newAt = t.toATerm();
    ATerm atComparator = factory.parse("f(\"a\",b)");
    assertEquals(newAt,atComparator);
  }

  @Test(expected=RuntimeException.class)
  public void stringIdConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    ATerm newAt = t.toATerm();
  }

  @Test(expected=RuntimeException.class)
  public void stringNoConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at); // implicite IdConverter
    ATerm newAt = t.toATerm();
  }


//input[3] : these tests (and results) should be exactly the same as input[4]

  @Test
  public void stringFooConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,fooConverter); // other converter
// if printed : t = h(a())
    assertEquals(t.toString(),"h(a())");
// if printed t.toATerm = h(a)
    ATerm newAt = t.toATerm();
    ATerm atComparator = factory.parse("h(a)");
    assertEquals(newAt,atComparator);
  }

  @Test
  public void stringIdConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
// if printed : t = h(a())
    assertEquals(t.toString(),"h(a())");
// if printed t.toATerm = h(a)
    ATerm newAt = t.toATerm();
    ATerm atComparator = factory.parse("h(a)");
    assertEquals(newAt,atComparator);
  }

  @Test
  public void stringNoConverterInput3() {
    ATerm at = factory.parse(input[3]);
    T t = T.fromTerm(at); // implicite IdConverter
// if printed : t = h(a())
    assertEquals(t.toString(),"h(a())");
// if printed t.toATerm = h(a)
    ATerm newAt = t.toATerm();
    ATerm atComparator = factory.parse("h(a)");
    assertEquals(newAt,atComparator);
  }


//input[4] : these tests (and results) should be exactly the same as input[3]

  @Test
  public void stringFooConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,fooConverter); // other converter
// if printed : t = h(a())
    assertEquals(t.toString(),"h(a())");
// if printed t.toATerm = h(a)
    ATerm newAt = t.toATerm();
    ATerm atComparator = factory.parse("h(a)");
    assertEquals(newAt,atComparator);
  }

  @Test
  public void stringIdConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
// if printed : t = h(a())
    assertEquals(t.toString(),"h(a())");
// if printed t.toATerm = h(a)
    ATerm newAt = t.toATerm();
    ATerm atComparator = factory.parse("h(a)");
    assertEquals(newAt,atComparator);
  }

  @Test
  public void stringNoConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at); // implicite IdConverter
// if printed : t = h(a())
    assertEquals(t.toString(),"h(a())");
// if printed t.toATerm = h(a)
    ATerm newAt = t.toATerm();
    ATerm atComparator = factory.parse("h(a)");
    assertEquals(newAt,atComparator);
  }


} // TypeString
