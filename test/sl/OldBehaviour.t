package sl;
import sl.testsl.types.*;

public class OldBehaviour {
  %include { mustrategy.tom }
  %include { testsl/testsl.tom }

  %strategy Rule1() extends `Identity() {
    visit Term {
      a() -> { return `b(); }
    }
  }

  public Term test1() {
    Term t = `f(a(),a());
    return (Term) `All(Rule1()).apply(t);
  }

}

