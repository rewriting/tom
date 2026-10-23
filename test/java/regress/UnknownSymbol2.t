import regress.unknownsymbol2.simple.types.*;
public class UnknownSymbol2 {
  %gom {
    module Simple 
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    B tt = `b();
    %match(tt) {
      b() -> { System.out.println(`tt); }
      a() -> { System.out.println(`tt); }
    }
  }
}
