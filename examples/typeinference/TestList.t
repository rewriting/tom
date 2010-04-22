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
    TestIf test = new TestList();
    test.printList(`f(b(),b()));
  }

  public void printList(L l) {
    %match{
      f(_*,e1,_*) << L l -> { 
        System.out.println("("+`e1+")");
      }
    }
  }
}
