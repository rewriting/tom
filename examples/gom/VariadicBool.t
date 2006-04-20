package gom;

import gom.variadicbool.bool.types.*;
import gom.variadicbool.bool.*;

public class VariadicBool {

  public static void main(String[] args) {
    VariadicBool test = new VariadicBool();
    test.run();
  }

  %include { mutraveler.tom }
  %gom {
    module Bool
    imports String
    abstract syntax
    Bool = True()
         | False()
         | And(Bool*)
         | Or(Bool*)
         | Neg(b:Bool)
         | Var(name:String)
  }

  public void run() {
    Bool subject = `And(Or(And(True(),Var("x"),Var("y"))));
    System.out.println(subject);
  }

}
