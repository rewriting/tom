import example1.example1.types.*;
public class  Example1{
  %gom {
    module Example1 
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    B tt = `b();
    %match{
      b() << tt -> { System.out.println(`tt); }
    }
  }
}
