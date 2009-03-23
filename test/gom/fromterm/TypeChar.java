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
  
  private static String[] input = { "n(g(e))", "n(e)", "n('e')", "n(\"e\")", "n(et)", "n(2)","n(42)" };

//input[0] => n(g(e))

  @Test
  public void booleanFooConverterInput0() {
    ATerm at = factory.parse(input[0]);
    T t = T.fromTerm(at,fooConverter); // other converter
    //System.out.println("input0, t.gett9() = " + (int)((foo.types.t.n)t).gett9()); => 149 (decalage de '0')
    //System.out.println("t = " + t); //t(101)
    assertEquals(((foo.types.t.n)t).gett9() - (int)'0','e'); // 'e' <=> 101
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
    assertEquals(((foo.types.t.n)t).gett9() - (int)'0','e'); // 'e' <=> 101
  }

  @Test
  public void booleanIdConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertEquals(((foo.types.t.n)t).gett9() - (int)'0','e'); // 'e' <=> 101
  }

  @Test
  public void booleanNoConverterInput1() {
    ATerm at = factory.parse(input[1]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertEquals(((foo.types.t.n)t).gett9() - (int)'0','e'); // 'e' <=> 101
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
    assertEquals(((foo.types.t.n)t).gett9(),'2'); // '2' <=> 50
  }

  @Test
  public void booleanIdConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
    assertEquals(((foo.types.t.n)t).gett9(),'2'); // '2' <=> 50
  }

  @Test
  public void booleanNoConverterInput5() {
    ATerm at = factory.parse(input[5]);
    T t = T.fromTerm(at); // implicite IdConverter
    assertEquals(((foo.types.t.n)t).gett9(),'2'); // '2' <=> 50
  }

//input[6] => n(42)
  
  @Test(expected=RuntimeException.class)
  public void booleanFooConverterInput6() {
    ATerm at = factory.parse(input[6]);
    T t = T.fromTerm(at,fooConverter); // other converter
  }

  @Test(expected=RuntimeException.class)
  public void booleanIdConverterInput6() {
    ATerm at = factory.parse(input[6]);
    T t = T.fromTerm(at,idConverter); // explicite IdConverter 
  }

  @Test(expected=RuntimeException.class)
  public void booleanNoConverterInput6() {
    ATerm at = factory.parse(input[6]);
    T t = T.fromTerm(at); // implicite IdConverter
  }

} // TypeChar
