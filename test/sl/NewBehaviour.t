package sl;
import sl.testsl.types.*;

public class NewBehaviour {

  %include { sl.tom }
  %include { testsl/testsl.tom }

  %strategy Rule1() extends `Identity() {
    visit Term {
      a() -> { return `b(); }
    }
  }

  public Term test1(Term t) {
    return (Term) `All(Rule1()).apply(t);
  }
}


