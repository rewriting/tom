import examplebugcomposite.example.types.*;

public class ExampleBugComposite {
  %gom {
    module Example 
      imports int
      abstract syntax
      B = b(value:int)
        | empty()
  }

  public final static void main(String[] args) {
    ExampleBugComposite test = new ExampleBugComposite();
    test.run(`b(10));
  }
 
  // return true if fire a rule
  public void run(B n) {
    B newB = n;
    %match(B n) {
      empty() -> {
        newB = `b(n.getvalue() + 10);
      }
    }
    System.out.println("newB = " + `newB);
  }
}
