import sort.strategy.expr.*;
import sort.types.*;
import sort.types.expr.*;
import tom.library.sl.*;


public class Representation {
  %include { sl.tom }
  %include {sort/Sort.tom}
  

  private static void represente_aux(Expr e, String way){
    %match(e){
      zero() -> {System.out.println("\"" + way + "\"");}
      un() -> {System.out.println("\"" + way + "\"");}
      plus(a,b) -> {
        System.out.println("\"" + way + "\"" + "->" + "\"" + way + 0 + "\"");
        System.out.println("\"" + way + "\"" + "->" + "\"" + way + 1 + "\"");
        represente_aux(`a, way + 0);
        represente_aux(`b, way + 1);
      }
      mult(a,b) -> {
        System.out.println("\"" + way + "\"" + "->" + "\"" + way + 0 + "\"");
        System.out.println("\"" + way + "\"" + "->" + "\"" + way + 1 + "\"");
        represente_aux(`a, way + 0);
        represente_aux(`b, way + 1);
      }
    }
  }

  public static void represente(Expr e){
    System.out.println("digraph mon_graphe {");
    represente_aux(e, "8");
    System.out.println("}");
  }

  public static void main(String[] args) {
    //Expr a = `plus(zero(),plus(plus(plus(plus(zero(),zero()),plus(zero(),zero())),plus(plus(plus(zero(),zero()),plus(zero(),zero())),plus(plus(plus(zero(),zero()),plus(zero(),zero())),plus(plus(plus(zero(),zero()),plus(zero(),zero())),plus(plus(zero(),zero()),plus(zero(),zero())))))),plus(plus(plus(zero(),zero()),plus(zero(),zero())),plus(plus(plus(zero(),zero()),plus(zero(),zero())),plus(plus(plus(zero(),zero()),plus(zero(),zero())),plus(plus(plus(zero(),zero()),plus(zero(),zero())),plus(plus(zero(),zero()),plus(zero(),zero()))))))));
    
    Expr a = `plus(plus(zero(),plus(zero(),plus(zero(),zero()))),plus(zero(),plus(zero(),plus(zero(),zero()))));
    
   represente(a); 
  }
}
