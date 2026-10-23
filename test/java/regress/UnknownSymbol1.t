import regress.unknownsymbol1.simple.types.*;
public class UnknownSymbol1 {
  %gom {
    module Simple 
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    B tt = `b();
    %match(B tt) {
      a() -> { System.out.println(`tt); }
    }
  }
}
