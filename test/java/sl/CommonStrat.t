package sl;
import sl.testsl.types.*;
import tom.library.sl.*;

class CommonStrat {
  %include { sl.tom }
  %include { testsl/testsl.tom}

  %strategy R() extends Fail() {
    visit Term {
      a() -> b()
    }
  }
}
