import collect.logic.types.*; 
import tom.library.sl.*; 
import java.util.LinkedList;
import java.util.HashSet;

public class Collect {
  
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

  %strategy CollectVars(Collection c) extends `Identity() {
    visit Term { v@var(_) -> { c.add(`v); } }
  }

  public static void main(String[] args) throws VisitFailure {
    Prop p = `forall("x", implies(implies(P(f(var("x"))), P(var("y"))),P(f(var("y")))));
    
    HashSet result = new HashSet();
    `TopDown(CollectVars(result)).visit(p);
    System.out.println("vars: " + result);
  }
}

