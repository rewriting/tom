import regress.badbackquote.example.types.*;
public class BadBackquote {
  %gom {
    module Example
      abstract syntax
      A = a()
      | h(num:B)

      B = b()
  }

  public static void main(String[] args) {
    A t = `h(a());
  }
}
