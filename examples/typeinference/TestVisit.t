import testvisit.testvisit.types.*;
public class TestVisit{
	%include { sl.tom }

  %gom {
    module TestVisit
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
