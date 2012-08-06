package logic;

import logic.system.types.*;
import logic.system.types.args.*;
import logic.system.types.formula.*;
import logic.system.types.term.*;

public class ExamplesFormula{

  %include{system/system.tom}

  public static Formula f1 = `Forall("x", "D", Predicate("P", ListArgs(Var("x"))));
  public static Formula odd_even = `Forall(
      "x", 
      "int", 
      Imply(
        Predicate("EVEN", 
          ListArgs(
            Var("x")
            )
          ), 
        Predicate("ODD", 
          ListArgs(
            Sig("succ", 
              ListArgs(
                Var("x")
                )
              )
            )
          )
        )
      );

  //public static Formula error = `Forall(
  //    "x", 
  //    "int", 
  //    Imply(
  //      Predicate("EVEN", 
  //        ListArgs(
  //          Var("x")
  //          )
  //        ), 
  //      Predicate("ODD", 
  //        ListArgs(
  //          Var(
  //            Sig("succ", 
  //              ListArgs(
  //                Var("x")
  //                )
  //              )
  //            )
  //          )
  //        )
  //      )
  //    );

}
