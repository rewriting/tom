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
  
  public static State eval(InstructionList il) {
    return eval(`Tortue(0,0,0,true),il);
  }

  public static State eval(State state,InstructionList il) {
    %match(il) {
      InstructionList(_*,inst,_*) -> {
        state = eval(state,`inst);
        System.out.println(state);
      }
    }
    return state;
  }

  public static State eval(State state,Instruction inst) {
    %match(state,inst) {
      s@Tortue[],LC() -> {
        System.out.println("leve crayon"); 
        return `s.setCrayonBas(false);
      }

      s@Tortue[],BC() -> {
        System.out.println("baisse crayon"); 
        return `s.setCrayonBas(true);
      }

      s@Tortue[X=x,Y=y,Angle=a],AV(dist) -> {
        int value = eval(`dist);
        System.out.println("avance " + value);
        int newX = `x + (value*Math.cos((double)`a));
        int newY = `y + (value*Math.sin((double)`a));
        return `s.setX(newX).setY(newY);
      }
      
      s@Tortue[X=x,Y=y,Angle=a],RE(dist) -> {
        int value = eval(`dist);
        System.out.println("recule " + value);
        int newX = `x - (value*Math.cos((double)`a));
        int newY = `y - (value*Math.sin((double)`a));
        return `s.setX(newX).setY(newY);
      }
      
      s@Tortue[Angle=a],TG(angle) -> {
        int value = eval(`angle);
        System.out.println("tourne gauche " + value);
        return `s.setAngle((`a+value)%360);
      }

      s@Tortue[Angle=a],TD(angle) -> {
        int value = eval(`angle);
        System.out.println("tourne droite " + value);
        return `s.setAngle((`a-value)%360);
      }

      s@Tortue[],REP(n,il) -> {
        State res=`s;
        for(int i=0 ; i<`n ; i++) {
          res = eval(res,`il);
        }
        return res;
      }

      s@Tortue[],WORLD(il) -> {
          State tmp = eval(`s,`il);
          return `s;
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
