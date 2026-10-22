package fromterm

// Port of test/gom/fromterm/TestFromTerm.t: a JUnit Suite of the Java
// classes TypeString, TypeInt, TypeBoolean, TypeChar, TypeLong,
// TypeDouble and TypeFloat (test/gom/fromterm/Type*.java, with
// FooConverter.java) that test Java Gom's fromTerm and toATerm on the
// builtin sorts of foo.gom.
// port: the original has no package; the Go package is fromterm.
// port: the Suite runs the @Test methods of its classes: the Go test of
// the method m of the class C is TestC_m (two classes declare methods of
// the same name); the static fields of each class (input, MIN, MAX,
// floatComparator, …) are package variables prefixed by the class name
// (typeStringInput, typeLongMAX, …).
// port: `import foo.*; import foo.types.*;` is the dot import of the Go
// package that gomgo generates from foo.gom (sub-directory foo);
// aterm.pure.SingletonFactory.getInstance() is aterm.GetInstance();
// tom.library.utils.IdConverter and ATermConverter are aterm.IdConverter
// and aterm.ATermConverter; T.fromTerm(at) and T.fromTerm(at, conv) are
// TFromTerm(at) and TFromTermConv(at, conv); t.toATerm() is t.ToATerm();
// ((foo.types.t.j)t).gett5() is t.Gett5() (a method of the sort);
// t instanceof foo.types.t.j is is[*JT](t); t.toString() is
// fmt.Sprint(t); (ATermAppl)x is x.(*aterm.ATermAppl), getArgument(i) is
// GetArgument(i), getInt()/getReal() are GetInt()/GetReal().
// port: @Test(expected=E.class) is expectPanic: the test passes when its
// body panics with a Java E (IllegalArgumentException: an ATerm that no
// operator of the sort converts; aterm.ParseError: a text the ATerm
// reader rejects; RuntimeException: any of them, a failed conversion of
// a builtin, a failed type assertion, an index out of range).
// port: a variable the Java test declares but does not use is used by
// `_ = x` (Go rejects unused variables).
// port: JUnit's assertEquals(expected, actual, delta) on floats and
// doubles is assertEqualsDelta.
// port: no main — `go test` runs the tests.

import (
  "fmt"
  "math"
  "testing"

  . "fixture/foo"
  "tom/library/aterm"
)


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

// FooConverter.java: an ATermConverter that replaces g(x) by x, whatever
// x is.
type FooConverter struct{}

var _ aterm.ATermConverter = FooConverter{}

// Convert is the method of the ATermConverter interface.
func (FooConverter) Convert(at aterm.ATerm) aterm.ATerm {
  switch at.Type() {
  case aterm.APPL:
    var appl *aterm.ATermAppl = at.(*aterm.ATermAppl)
    var name string = appl.GetName()
    var args []aterm.ATerm = appl.GetArguments()
    _ = args
    if name == "g" { // case : "g(x)) -> x" whatever is the type of x / arity = 1
      at = appl.GetArgument(0)
    }
  }
  return at
} //convert

// the static fields common to the Type* classes
var factory *aterm.PureFactory = aterm.GetInstance()
var fooConverter FooConverter = FooConverter{}
var idConverter aterm.IdConverter = aterm.IdConverter{}

// TypeString.java: input
var typeStringInput = []string{"h(\"a\")", "f(g(a))", "f(g(\"a\"),b())", "h(a)", "h(a())"}

func TestTypeString_stringFooConverterInput0(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeStringInput[0])
      var t T = TFromTermConv(at, fooConverter) // other converter
      var newAt aterm.ATerm = t.ToATerm()
      _ = newAt
  })
}

func TestTypeString_stringIdConverterInput0(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeStringInput[0])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      var newAt aterm.ATerm = t.ToATerm()
      _ = newAt
  })
}

func TestTypeString_stringNoConverterInput0(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeStringInput[0])
      var t T = TFromTerm(at) // implicite IdConverter
      var newAt aterm.ATerm = t.ToATerm()
      _ = newAt
  })
}

func TestTypeString_stringFooConverterInput1(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeStringInput[1])
      var t T = TFromTermConv(at, fooConverter) // other converter
      var newAt aterm.ATerm = t.ToATerm()
      _ = newAt
  })
}

func TestTypeString_stringIdConverterInput1(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeStringInput[1])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      var newAt aterm.ATerm = t.ToATerm()
      _ = newAt
  })
}

func TestTypeString_stringNoConverterInput1(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeStringInput[1])
      var t T = TFromTerm(at) // implicite IdConverter
      var newAt aterm.ATerm = t.ToATerm()
      _ = newAt
  })
}

func TestTypeString_stringFooConverterInput2(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeStringInput[2])
    var t T = TFromTermConv(at, fooConverter) // other converter
// if printed : t = f("a",b())
    assertEquals(tt, fmt.Sprint(t), "f(\"a\",b())")
// if printed t.toATerm = f("a",b)
    var newAt aterm.ATerm = t.ToATerm()
    var atComparator aterm.ATerm = factory.Parse("f(\"a\",b)")
    assertEquals(tt, newAt, atComparator)
}

func TestTypeString_stringIdConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeStringInput[2])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      var newAt aterm.ATerm = t.ToATerm()
      _ = newAt
  })
}

func TestTypeString_stringNoConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeStringInput[2])
      var t T = TFromTerm(at) // implicite IdConverter
      var newAt aterm.ATerm = t.ToATerm()
      _ = newAt
  })
}

func TestTypeString_stringFooConverterInput3(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeStringInput[3])
    var t T = TFromTermConv(at, fooConverter) // other converter
// if printed : t = h(a())
    assertEquals(tt, fmt.Sprint(t), "h(a())")
// if printed t.toATerm = h(a)
    var newAt aterm.ATerm = t.ToATerm()
    var atComparator aterm.ATerm = factory.Parse("h(a)")
    assertEquals(tt, newAt, atComparator)
}

func TestTypeString_stringIdConverterInput3(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeStringInput[3])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
// if printed : t = h(a())
    assertEquals(tt, fmt.Sprint(t), "h(a())")
// if printed t.toATerm = h(a)
    var newAt aterm.ATerm = t.ToATerm()
    var atComparator aterm.ATerm = factory.Parse("h(a)")
    assertEquals(tt, newAt, atComparator)
}

func TestTypeString_stringNoConverterInput3(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeStringInput[3])
    var t T = TFromTerm(at) // implicite IdConverter
// if printed : t = h(a())
    assertEquals(tt, fmt.Sprint(t), "h(a())")
// if printed t.toATerm = h(a)
    var newAt aterm.ATerm = t.ToATerm()
    var atComparator aterm.ATerm = factory.Parse("h(a)")
    assertEquals(tt, newAt, atComparator)
}

func TestTypeString_stringFooConverterInput4(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeStringInput[4])
    var t T = TFromTermConv(at, fooConverter) // other converter
// if printed : t = h(a())
    assertEquals(tt, fmt.Sprint(t), "h(a())")
// if printed t.toATerm = h(a)
    var newAt aterm.ATerm = t.ToATerm()
    var atComparator aterm.ATerm = factory.Parse("h(a)")
    assertEquals(tt, newAt, atComparator)
}

func TestTypeString_stringIdConverterInput4(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeStringInput[4])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
// if printed : t = h(a())
    assertEquals(tt, fmt.Sprint(t), "h(a())")
// if printed t.toATerm = h(a)
    var newAt aterm.ATerm = t.ToATerm()
    var atComparator aterm.ATerm = factory.Parse("h(a)")
    assertEquals(tt, newAt, atComparator)
}

func TestTypeString_stringNoConverterInput4(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeStringInput[4])
    var t T = TFromTerm(at) // implicite IdConverter
// if printed : t = h(a())
    assertEquals(tt, fmt.Sprint(t), "h(a())")
// if printed t.toATerm = h(a)
    var newAt aterm.ATerm = t.ToATerm()
    var atComparator aterm.ATerm = factory.Parse("h(a)")
    assertEquals(tt, newAt, atComparator)
}

// TypeInt.java: input
var typeIntInput = []string{"i(42)",  "i(g(42))", "i(\"42\")", "i(42.5)", "f(42,b())", "h(42)"}

func TestTypeInt_intFooConverterInput0(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeIntInput[0])
    var arg aterm.ATerm = at.(*aterm.ATermAppl).GetArgument(0)
    var atint *aterm.ATermInt = arg.(*aterm.ATermInt)
    
    var t T = TFromTermConv(at, fooConverter) // other converter
    
    assertEquals(tt, t.Gett4(), int64(42))
    var newAt aterm.ATerm = t.ToATerm()
    var newArg aterm.ATerm = newAt.(*aterm.ATermAppl).GetArgument(0)
    var newAtint *aterm.ATermInt = newArg.(*aterm.ATermInt)
    assertEquals(tt, atint.GetInt(), newAtint.GetInt())
}

func TestTypeInt_intIdConverterInput0(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeIntInput[0])
    var arg aterm.ATerm = at.(*aterm.ATermAppl).GetArgument(0)
    var atint *aterm.ATermInt = arg.(*aterm.ATermInt)

    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    
    assertEquals(tt, t.Gett4(), int64(42))
    var newAt aterm.ATerm = t.ToATerm()
    var newArg aterm.ATerm = newAt.(*aterm.ATermAppl).GetArgument(0)
    var newAtint *aterm.ATermInt = newArg.(*aterm.ATermInt)
    assertEquals(tt, atint.GetInt(), newAtint.GetInt())
}

func TestTypeInt_intNoConverterInput0(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeIntInput[0])
    var arg aterm.ATerm = at.(*aterm.ATermAppl).GetArgument(0)
    var atint *aterm.ATermInt = arg.(*aterm.ATermInt)
    
    var t T = TFromTerm(at) // implicite IdConverter
    
    assertEquals(tt, t.Gett4(), int64(42))
    var newAt aterm.ATerm = t.ToATerm()
    var newArg aterm.ATerm = newAt.(*aterm.ATermAppl).GetArgument(0)
    var newAtint *aterm.ATermInt = newArg.(*aterm.ATermInt)
    assertEquals(tt, atint.GetInt(), newAtint.GetInt())
}

func TestTypeInt_intFooConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeIntInput[1])
    var argi aterm.ATerm = at.(*aterm.ATermAppl).GetArgument(0)
    var argg aterm.ATerm = argi.(*aterm.ATermAppl).GetArgument(0)
    var atint *aterm.ATermInt = argg.(*aterm.ATermInt)
    
    var t T = TFromTermConv(at, fooConverter) // other converter
    
    assertEquals(tt, t.Gett4(), int64(42))
    var newAt aterm.ATerm = t.ToATerm()
    var newArg aterm.ATerm = newAt.(*aterm.ATermAppl).GetArgument(0)
    var newAtint *aterm.ATermInt = newArg.(*aterm.ATermInt)
    assertEquals(tt, atint.GetInt(), newAtint.GetInt())
}

func TestTypeInt_intIdConverterInput1(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[1])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intNoConverterInput1(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[1])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intFooConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[2])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intIdConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[2])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intNoConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[2])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intFooConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[3])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intIdConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[3])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intNoConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[3])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intFooConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[4])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intIdConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[4])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intNoConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[4])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intFooConverterInput5(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[5])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intIdConverterInput5(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[5])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

func TestTypeInt_intNoConverterInput5(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeIntInput[5])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
      //ATerm newAt = t.toATerm();
  })
}

// TypeBoolean.java: input
var typeBooleanInput = []string{"o(g(0))",  "o(g(1))", "o(0)", "o(-42)", "g(o(t))", "g(o(tr))", "g(o(\"tr\"))", "h(o(1))"}

func TestTypeBoolean_booleanFooConverterInput0(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[0])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertFalse(tt, t.Gett10())
}

func TestTypeBoolean_booleanIdConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[0])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanNoConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[0])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanFooConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[1])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, t.Gett10())
}

func TestTypeBoolean_booleanIdConverterInput1(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[1])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanNoConverterInput1(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[1])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanFooConverterInput2(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[2])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertFalse(tt, t.Gett10())
}

func TestTypeBoolean_booleanIdConverterInput2(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[2])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    assertFalse(tt, t.Gett10())
}

func TestTypeBoolean_booleanNoConverterInput2(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[2])
    var t T = TFromTerm(at) // implicite IdConverter
    assertFalse(tt, t.Gett10())
}

func TestTypeBoolean_booleanFooConverterInput3(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[3])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, t.Gett10())
}

func TestTypeBoolean_booleanIdConverterInput3(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[3])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    assertTrue(tt, t.Gett10())
}

func TestTypeBoolean_booleanNoConverterInput3(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[3])
    var t T = TFromTerm(at) // implicite IdConverter
    assertTrue(tt, t.Gett10())
}

func TestTypeBoolean_booleanFooConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[4])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeBoolean_booleanIdConverterInput4(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[4])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanNoConverterInput4(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[4])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanFooConverterInput5(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[5])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeBoolean_booleanIdConverterInput5(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[5])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanNoConverterInput5(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[5])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanFooConverterInput6(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[6])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeBoolean_booleanIdConverterInput6(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[6])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanNoConverterInput6(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeBooleanInput[6])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeBoolean_booleanFooConverterInput7(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[7])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, is[*OT](t.Gett3()))
    assertTrue(tt, t.Gett3().Gett10())
}

func TestTypeBoolean_booleanIdConverterInput7(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[7])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    assertTrue(tt, is[*OT](t.Gett3()))
    assertTrue(tt, t.Gett3().Gett10())
}

func TestTypeBoolean_booleanNoConverterInput7(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeBooleanInput[7])
    var t T = TFromTerm(at) // implicite IdConverter
    assertTrue(tt, is[*OT](t.Gett3()))
    assertTrue(tt, t.Gett3().Gett10())
}

// TypeChar.java: input
var typeCharInput = []string{"n(g(101))", "n(101)", "n('e')", "n(\"101\")", "n(et)", "n(2)"}
var typeCharCharComparator rune = 'e'

func TestTypeChar_booleanFooConverterInput0(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeCharInput[0])
    var t T = TFromTermConv(at, fooConverter) // other converter
    //System.out.println("input0, t.gett9() = " + (int)((foo.types.t.n)t).gett9()); => 149 (decalage de '0')
    //System.out.println("t = " + t); //t(101)
    assertEquals(tt, int64(typeCharCharComparator), int64(t.Gett9()) - int64('0'))
}

func TestTypeChar_booleanIdConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[0])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeChar_booleanNoConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[0])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeChar_booleanFooConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeCharInput[1])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertEquals(tt, int64(typeCharCharComparator), int64(t.Gett9()) - int64('0'))
}

func TestTypeChar_booleanIdConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeCharInput[1])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    assertEquals(tt, int64(typeCharCharComparator), int64(t.Gett9()) - int64('0'))
}

func TestTypeChar_booleanNoConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeCharInput[1])
    var t T = TFromTerm(at) // implicite IdConverter
    assertEquals(tt, int64(typeCharCharComparator), int64(t.Gett9()) - int64('0'))
}

func TestTypeChar_booleanFooConverterInput2(tt *testing.T) {
  expectPanic(tt, "aterm.ParseError", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[2])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeChar_booleanIdConverterInput2(tt *testing.T) {
  expectPanic(tt, "aterm.ParseError", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[2])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeChar_booleanNoConverterInput2(tt *testing.T) {
  expectPanic(tt, "aterm.ParseError", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[2])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeChar_booleanFooConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[3])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeChar_booleanIdConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[3])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeChar_booleanNoConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[3])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeChar_booleanFooConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[4])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeChar_booleanIdConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[4])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeChar_booleanNoConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeCharInput[4])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeChar_booleanFooConverterInput5(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeCharInput[5])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertEquals(tt, '2', t.Gett9())
}

func TestTypeChar_booleanIdConverterInput5(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeCharInput[5])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    assertEquals(tt, '2', t.Gett9())
}

func TestTypeChar_booleanNoConverterInput5(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeCharInput[5])
    var t T = TFromTerm(at) // implicite IdConverter
    assertEquals(tt, '2', t.Gett9())
}

// TypeLong.java: input
var typeLongInput = []string{"l(g(9223372036854775807l))", "l(-9223372036854775808l)", "l(42.5)", "l(\"9223372036854775807l\")", "d(9223372036854775807l)", "j(9223372036854775807l)", "i(9223372036854775807l)"}
var typeLongMIN int64 = math.MinInt64
var typeLongMAX int64 = math.MaxInt64

func TestTypeLong_intFooConverterInput0(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeLongInput[0])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, is[*LT](t))
    assertEquals(tt, t.Gett7(), typeLongMAX)
}

func TestTypeLong_intIdConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[0])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeLong_intNoConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[0])
      var t T = TFromTerm(at) // explicite IdConverter
      _ = t
  })
}

func TestTypeLong_intFooConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeLongInput[1])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, is[*LT](t))
    assertEquals(tt, t.Gett7(), typeLongMIN)
}

func TestTypeLong_intIdConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeLongInput[1])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    assertTrue(tt, is[*LT](t))
    assertEquals(tt, t.Gett7(), typeLongMIN)
}

func TestTypeLong_intNoConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeLongInput[1])
    var t T = TFromTerm(at) // implicite IdConverter
    assertTrue(tt, is[*LT](t))
    assertEquals(tt, t.Gett7(), typeLongMIN)
}

func TestTypeLong_intFooConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[2])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeLong_intIdConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[2])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeLong_intNoConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[2])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeLong_intFooConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[3])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeLong_intIdConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[3])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeLong_intNoConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[3])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeLong_intFooConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[4])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeLong_intIdConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[4])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeLong_intNoConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[4])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeLong_intFooConverterInput5(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[5])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeLong_intIdConverterInput5(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[5])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeLong_intNoConverterInput5(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[5])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeLong_intFooConverterInput6(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[6])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeLong_intIdConverterInput6(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[6])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeLong_intNoConverterInput6(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeLongInput[6])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

// TypeDouble.java: input
var typeDoubleInput = []string{"d(g(42.24))", "d(42.24)", "d(42)", "d(\"42.24\")", "h(42.24)"}
var typeDoubleDoubleComparator float64 = 42.24

func TestTypeDouble_booleanFooConverterInput0(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeDoubleInput[0])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, is[*DT](t))
    assertEqualsDelta(tt, t.Gett8(), typeDoubleDoubleComparator, 0)
}

func TestTypeDouble_booleanIdConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[0])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeDouble_booleanNoConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[0])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeDouble_booleanFooConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeDoubleInput[1])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, is[*DT](t))
    assertEqualsDelta(tt, t.Gett8(), typeDoubleDoubleComparator, 0)
}

func TestTypeDouble_booleanIdConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeDoubleInput[1])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    assertTrue(tt, is[*DT](t))
    assertEqualsDelta(tt, t.Gett8(), typeDoubleDoubleComparator, 0)
}

func TestTypeDouble_booleanNoConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeDoubleInput[1])
    var t T = TFromTerm(at) // implicite IdConverter
    assertTrue(tt, is[*DT](t))
    assertEqualsDelta(tt, t.Gett8(), typeDoubleDoubleComparator, 0)
}

func TestTypeDouble_booleanFooConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[2])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeDouble_booleanIdConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[2])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeDouble_booleanNoConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[2])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeDouble_booleanFooConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[3])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeDouble_booleanIdConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[3])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeDouble_booleanNoConverterInput3(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[3])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeDouble_booleanIdConverterInput4(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[4])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeDouble_booleanNoConverterInput4(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeDoubleInput[4])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

// TypeFloat.java: input
var typeFloatInput = []string{"j(g(42.1234567890))", "j(42.1234567890)", "j(42)", "j(42.24)", "j(\"42.24\")", "h(42.24)"}
var typeFloatFloatComparator float32 = float32(42.1234)

func TestTypeFloat_floatFooConverterInput(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeFloatInput[0])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, is[*JT](t))
    assertEqualsDelta(tt, float64(typeFloatFloatComparator), float64(t.Gett5()), float64(float32(0.0001)))
    var newAt aterm.ATerm = t.ToATerm()
    var arg *aterm.ATermReal = newAt.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermReal)
    assertEqualsDelta(tt, float64(typeFloatFloatComparator), float64(float32(arg.GetReal())), float64(float32(0.0001)))
}

func TestTypeFloat_floatIdConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[0])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeFloat_floatNoConverterInput0(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[0])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeFloat_floatFooConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeFloatInput[1])
     var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, is[*JT](t))
    assertEqualsDelta(tt, float64(typeFloatFloatComparator), float64(t.Gett5()), float64(float32(0.0001)))
    var newAt aterm.ATerm = t.ToATerm()
    var arg *aterm.ATermReal = newAt.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermReal)
    assertEqualsDelta(tt, float64(typeFloatFloatComparator), float64(float32(arg.GetReal())), float64(float32(0.0001)))
}

func TestTypeFloat_floatIdConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeFloatInput[1])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    assertTrue(tt, is[*JT](t))
    assertEqualsDelta(tt, float64(typeFloatFloatComparator), float64(t.Gett5()), float64(float32(0.0001)))
    var newAt aterm.ATerm = t.ToATerm()
    var arg *aterm.ATermReal = newAt.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermReal)
    assertEqualsDelta(tt, float64(typeFloatFloatComparator), float64(float32(arg.GetReal())), float64(float32(0.0001)))
}

func TestTypeFloat_floatNoConverterInput1(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeFloatInput[1])
    var t T = TFromTerm(at) // implicite IdConverter
    assertTrue(tt, is[*JT](t))
    assertEqualsDelta(tt, float64(typeFloatFloatComparator), float64(t.Gett5()), float64(float32(0.0001)))
    var newAt aterm.ATerm = t.ToATerm()
    var arg *aterm.ATermReal = newAt.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermReal)
    assertEqualsDelta(tt, float64(typeFloatFloatComparator), float64(float32(arg.GetReal())), float64(float32(0.0001)))
}

func TestTypeFloat_floatFooConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[2])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeFloat_floatIdConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[2])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeFloat_floatNoConverterInput2(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[2])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeFloat_floatFooConverterInput3(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeFloatInput[3])
    var t T = TFromTermConv(at, fooConverter) // other converter
    assertTrue(tt, is[*JT](t))
    assertEqualsDelta(tt, float64(42.24), float64(t.Gett5()), float64(0.001))
    var newAt aterm.ATerm = t.ToATerm()
    var arg *aterm.ATermReal = newAt.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermReal)
    assertEqualsDelta(tt, float64(42.24), float64(float32(arg.GetReal())), float64(0.001))
}

func TestTypeFloat_floatIdConverterInput3(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeFloatInput[3])
    var t T = TFromTermConv(at, idConverter) // explicite IdConverter
    assertTrue(tt, is[*JT](t))
    assertEqualsDelta(tt, float64(42.24), float64(t.Gett5()), float64(0.001))
    var newAt aterm.ATerm = t.ToATerm()
    var arg *aterm.ATermReal = newAt.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermReal)
    assertEqualsDelta(tt, float64(42.24), float64(float32(arg.GetReal())), float64(0.001))
}

func TestTypeFloat_floatNoConverterInput3(tt *testing.T) {
    var at aterm.ATerm = factory.Parse(typeFloatInput[3])
    var t T = TFromTerm(at) // implicite IdConverter
    assertTrue(tt, is[*JT](t))
    assertEqualsDelta(tt, float64(42.24), float64(t.Gett5()), float64(0.001))
    var newAt aterm.ATerm = t.ToATerm()
    var arg *aterm.ATermReal = newAt.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermReal)
    assertEqualsDelta(tt, float64(42.24), float64(float32(arg.GetReal())), float64(0.001))
}

func TestTypeFloat_floatFooConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[4])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeFloat_floatIdConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[4])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeFloat_floatNoConverterInput4(tt *testing.T) {
  expectPanic(tt, "RuntimeException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[4])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}

func TestTypeFloat_floatFooConverterInput5(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[5])
      var t T = TFromTermConv(at, fooConverter) // other converter
      _ = t
  })
}

func TestTypeFloat_floatIdConverterInput5(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[5])
      var t T = TFromTermConv(at, idConverter) // explicite IdConverter
      _ = t
  })
}

func TestTypeFloat_floatNoConverterInput5(tt *testing.T) {
  expectPanic(tt, "IllegalArgumentException", func() {
      var at aterm.ATerm = factory.Parse(typeFloatInput[5])
      var t T = TFromTerm(at) // implicite IdConverter
      _ = t
  })
}


// is reports whether x is a T (Java's instanceof).
func is[T any](x any) bool {
  _, ok := x.(T)
  return ok
}

// expectPanic is @Test(expected=E.class): f must panic with a Java E.
func expectPanic(t *testing.T, exception string, f func()) {
  t.Helper()
  var r any
  func() {
    defer func() { r = recover() }()
    f()
  }()
  if r == nil {
    t.Fatalf("Expected exception: %s", exception)
  }
  switch exception {
  case "IllegalArgumentException":
    if e, ok := r.(*aterm.GomError); !ok || !e.IllegalArgument {
      t.Fatalf("Unexpected exception, expected<%s> but was<%v>", exception, r)
    }
  case "aterm.ParseError":
    if _, ok := r.(*aterm.ParseError); !ok {
      t.Fatalf("Unexpected exception, expected<%s> but was<%v>", exception, r)
    }
  }
}

// org.junit.Assert.assertTrue(condition)
func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}

// org.junit.Assert.assertFalse(condition)
func assertFalse(t *testing.T, condition bool) {
  t.Helper()
  if condition {
    t.Fatal()
  }
}

// org.junit.Assert.assertEquals(expected, actual)
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

// org.junit.Assert.assertEquals(expected, actual, delta)
func assertEqualsDelta(t *testing.T, expected, actual, delta float64) {
  t.Helper()
  if math.Abs(expected-actual) > delta {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

var _ = fmt.Sprint
