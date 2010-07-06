import examplebugcomments.examplebugcomments.types.*;
public class ExampleBugComments{
  %gom {
    module ExampleBugComments
      abstract syntax
      B = b()
        | f(n1:B)
  }

  public static void main(String[] args) {
    B ss = `f(b());
    %match{
      f(x) << B ss || b() << B ss -> { 
        //System.out.println("Test");
        System.out.println(`x);
      }
    }
  }
}
