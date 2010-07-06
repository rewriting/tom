import examplelist.examplelist.types.*;
public class ExampleList{
  %gom {
    module ExampleList
      imports int
      abstract syntax
      B = b()
      L = f(B*)
  }

  public static void main(String[] args) {
    ExampleList test = new ExampleList();
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
