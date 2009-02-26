package logo;

import logo.ast.types.*;
import tom.library.sl.*;
import java.util.*;

public class Compiler {
  %include { ast/Ast.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/HashSet.tom }

  public static void eval(InstructionList il) {
    %match(il) {
      InstructionList(_*,inst,_*) -> {
        eval(`inst);
      }
    }
  }

  public static void eval(Instruction inst) {
    %match(inst) {
      LC() -> {
        System.out.println("leve crayon"); 
      }

      BC() -> {
        System.out.println("baisse crayon"); 
      }

      AV(dist) -> {
        int value = eval(`dist);
        System.out.println("avance " + value);
      }

      TG(angle) -> {
        int value = eval(`angle);
        System.out.println("tourne gauche " + value);
      }

      TD(angle) -> {
        int value = eval(`angle);
        System.out.println("tourne droite " + value);
      }

      RE(dist) -> {
        int value = eval(`dist);
        System.out.println("recule " + value);
      }

      REP(n,il) -> {
        for(int i=0 ; i<`n ; i++) {
          eval(`il);
        }
      }
    }
  }

  public static int eval(Expression exp) {
    %match(exp) {
      Cst(value) -> { 
        return `value;
      }

      Plus(e1,e2) -> { 
        return eval(`e1) + eval(`e2);
      }
    }
    return 0;
  }
  
  public static InstructionList optimize(InstructionList il) {
    try {
      InstructionList res = `InnermostId(StaticEval()).visitLight(il);
      System.out.println("res = " + res);
      return res;
    } catch (VisitFailure e) {
      System.out.println("failure");
    }
    return null;
  }

  %strategy StaticEval() extends Identity() {
    visit Expression {
      Plus(Cst(v1),Cst(v2)) -> {
        return `Cst(v1+v2); 
      }

    }

    
    visit InstructionList {
      InstructionList(TG(Cst(x)),TG(Cst(y)),tail*) -> { return `InstructionList(TG(Cst(x + y)),tail*); }
      InstructionList(TD(Cst(x)),TD(Cst(y)),tail*) -> { return `InstructionList(TD(Cst(x + y)),tail*); }
      InstructionList(TG(Cst(x)),TD(Cst(y)),tail*) -> { return `InstructionList(TG(Cst(x - y)),tail*); }
      InstructionList(TD(Cst(x)),TG(Cst(y)),tail*) -> { return `InstructionList(TD(Cst(x - y)),tail*); }
    }
   
  }

}
