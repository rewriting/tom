import testlist.testlist.types.*;
public class TestList{
  %gom {
    module TestList
      imports int
      abstract syntax
      B = b()
      L = f(B*)
  }

  public static void main(String[] args) {
    TestList test = new TestList();
    test.printList(`f(b(),b()));
  }

  public void printList(L l) {
    %match{
      f(x*,e1,y*) << L l -> { 
        System.out.println("("+`e1+")");
        System.out.println("("+`f(e1,x*,y*)+")");
        `printList(f(e1,b()));
      }
    }
  }
}
