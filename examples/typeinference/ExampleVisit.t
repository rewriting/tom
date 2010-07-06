import examplevisit.examplevisit.types.*;
public class ExampleVisit{
	%include { sl.tom }

  %gom {
    module ExampleVisit
      imports
      int String

      abstract syntax
      T1 = a() 
        | b() 
        | c()
        | f(x:T1)
  }

  %strategy R() extends Identity() {
    visit T1 {
      a() -> b()
      b() -> c()
    }
  }
 
  public final static void main(String[] args) {
    T1 test = `f(a());
    Strategy s = `RepeatId(BottomUp(R()));
  }
}
