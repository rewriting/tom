import testif.testif.types.*;
public class TestIf{
  %gom {
    module TestIf
      imports int
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    int tt = 10;
    %match{
      x << int tt -> { 
        if (`x>1) {  
          System.out.println(`x); 
        }
      }
    }
  }
}
