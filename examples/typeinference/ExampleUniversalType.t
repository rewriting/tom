import exampleuniversaltype.exampleuniversaltype.types.*;
public class ExampleUniversalType{
  %gom {
    module ExampleUniversalType
      abstract syntax
      Term = a()
       | f(s:Term)
  }

  public static void main(String[] args) {
    ExampleUniversalType test = new ExampleUniversalType();
    %match {
      a() << B test() -> { System.out.println("test OK!"); }
    }
  }

  public static Term test() {
    Term n = `a();
    %match(n) {
      x@f(a()) -> { return `x; }
    }
    return `n;
  }
}
