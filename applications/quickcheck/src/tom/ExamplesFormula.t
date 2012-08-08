package examples;

import logic.system.types.*;

public class ExamplesFormula{

  %include{../logic/system/system.tom}

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
