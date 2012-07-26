package gen;

import system.types.*;
import system.types.args.*;
import system.types.formula.*;
import system.types.term.*;

public class ExamplesFormula{

  %include{system/system.tom}

  public static Formula f1 = `Forall("x", "D", Predicate("P", ListArgs(Var("x"))));

}
