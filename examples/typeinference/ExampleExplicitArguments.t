import exampleexplicitarguments.example.types.*;
public class ExampleExplicitArguments {
  %gom {
    module Example 
      abstract syntax
      A = a()
        | h(n:B)
      B = b()
        | f(n:B,m:A)
      BList = concB(B*)

  }

  public static void main(String[] args) {
    BList list = `concB(b(),f(b(),a()));
    A name = `a();
    %match(list, A name) {
      concB(_*, x@f[m=fname], _*), fname -> {
        System.out.println(`x);
      }
    }
  }
}

