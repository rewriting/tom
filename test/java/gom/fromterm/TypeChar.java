import aterm.*;
import aterm.pure.*;

import foo.*;
import foo.types.*;

import tom.library.utils.IdConverter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class TypeChar {

  private static PureFactory factory = SingletonFactory.getInstance();

  private static FooConverter fooConverter = new FooConverter();
  private static IdConverter idConverter = new IdConverter();
  
  private static final char charComparator = 'e'; // 'e' <=> 101
  
  private static String[] input = { "n(g(101))", "n(101)", "n('e')", "n(\"101\")", "n(et)", "n(2)" };

//input[0] => n(g(e))

  @Test
  public void booleanFooConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,fooConverter); // other converter
    //System.out.println("input0, t.gett9() = " + (int)((foo.types.t.n)t).gett9()); => 149 (decalage de '0')
    //System.out.println("t = " + t); //t(101)
    assertEquals(charComparator,((foo.types.t.n)t).gett9() - (int)'0');
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

//input[1] => n(e)

  @Test
  public void booleanFooConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertEquals(charComparator,((foo.types.t.n)t).gett9() - (int)'0');
  }

  @Test
  public void booleanIdConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertEquals(charComparator,((foo.types.t.n)t).gett9() - (int)'0');
  }

  @Test
  public void booleanNoConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertEquals(charComparator,((foo.types.t.n)t).gett9() - (int)'0');
  }

//input[2] => n('e')
  
  @Test(expected=aterm.ParseError.class)
  public void booleanFooConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=aterm.ParseError.class)
  public void booleanIdConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=aterm.ParseError.class)
  public void booleanNoConverterInput2() {
    ATerm at = factory.parse(input[2]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[3] => n(\"e\")
  
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

//input[4] => n(et)
 
  @Test(expected=RuntimeException.class)
  public void booleanFooConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void booleanIdConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void booleanNoConverterInput4() {
    ATerm at = factory.parse(input[4]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

//input[5] => n(2)
  
  @Test
  public void booleanFooConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,fooConverter); // other converter
    assertEquals('2',((foo.types.t.n)t).gett9());
  }

  @Test
  public void booleanIdConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertEquals('2',((foo.types.t.n)t).gett9());
  }

  @Test
  public void booleanNoConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertEquals('2',((foo.types.t.n)t).gett9());
  }

} // TypeChar
