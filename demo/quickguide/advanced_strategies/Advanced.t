import advanced.logic.types.*; 
import tom.library.sl.*; 
import java.util.LinkedList;
import java.util.HashSet;

public class Advanced {
  
  %include { sl.tom }
  %include { util/types/Collection.tom }

  %gom {
    module logic
      imports String
      abstract syntax

      Term = var(name:String)
           | f(t:Term)

      Prop = P(t: Term)
           | implies(l:Prop, r:Prop)
           | forall(name:String, p:Prop)
  }

  %strategy CollectFree(String name, Collection c) extends `Fail() {
    visit Prop { p@forall(x,_) -> { if(`x == name) return `p; } }
    visit Term { v@var(x)      -> { if(`x == name) c.add(getEnvironment().getPosition()); } }
  }

  private static LinkedList<Position> 
    collectFreeOccurences(String name, Prop p) throws VisitFailure {
      LinkedList res = new LinkedList();
      `mu(MuVar("x"), Choice(CollectFree(name,res),All(MuVar("x")))).visit(p);
      return res;
    }

  public static void main(String[] args) throws VisitFailure {
    Prop p = `forall("x", implies(implies(P(f(var("x"))), P(var("y"))),P(f(var("y")))));
    
    System.out.println("free occurences of x: " + collectFreeOccurences("x",p));
    System.out.println("free occurences of y: " + collectFreeOccurences("y",p));
  }
}

