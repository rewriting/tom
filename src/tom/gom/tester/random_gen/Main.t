import sort.strategy.expr.*;
import sort.types.*;
import sort.types.expr.*;
import tom.library.sl.*;


public class Main {
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
    RandomizerGenerator generator = new RandomizerGenerator();
    
    
    /* debut test */
    Strategy s_test = generator.testStrategyID();
    System.out.println(s_test);
    Expr b = null;
    try{
      b=s_test.visit(`zero());
    } catch (VisitFailure e){
      System.out.println("erreur");
    }
    System.out.println(b + "\n\n");
    /* fin test */
    
    
    Strategy s = generator.make_random();
    Expr a = null;
    try{
      a = s.visit(`zero());
    } catch (VisitFailure e){
      System.out.println("impossible");
    }
    
    System.out.println(a);
    System.out.println("\n\n");
    //represente(a); 
  }
}
