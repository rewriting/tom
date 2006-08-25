package termgraph;

import termgraph.term.*;
import termgraph.term.types.*;
import tom.library.strategy.mutraveler.Position;
import java.util.HashMap;

public class Tool{

  %include{java/util/HashMap.tom}
  %include{java/mustrategy.tom}
  %include{term/term.tom}


  %strategy CollectTerms(table:HashMap) extends `Identity() {
    visit Term{
      labTerm(label,term) -> {
        table.put(`label,getPosition());
        return `term;
      }
    }
  }

  %strategy Replacelabels(table:HashMap) extends `Identity() {
    visit Term{
      refTerm(label) -> {
        Position pos = (Position) table.get(`label);
        if (pos == null) throw new RuntimeException("The label "+`label+" is not referenced");
        Term ref = `posTerm();
        int[] array = pos.toArray();
        for(int i=0;i<pos.depth();i++){
          ref = `posTerm(ref*,array[i]);
        }
        return ref; 
      }
    }
  }

  public static Term expand(Term t){
    HashMap table = new HashMap();
    return (Term) `Sequence(TopDown(CollectTerms(table)),TopDown(Replacelabels(table))).apply(t);
  }

}

