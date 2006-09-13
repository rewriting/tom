package bpel;

import bpel.wfg.*;
import bpel.wfg.types.*;
import tom.library.strategy.mutraveler.Position;
import java.util.HashMap;

public class Tool{

  %include{java/util/HashMap.tom}
  %include{java/mustrategy.tom}
  %include{wfg/Wfg.tom}


  %strategy CollectWfgs(table:HashMap) extends `Identity() {
    visit Wfg{
      labWfg(label,wfg) -> {
        table.put(`label,getPosition());
        return `wfg;
      }
    }
  }

  %strategy Replacelabels(table:HashMap) extends `Identity() {
    visit Wfg{
      refWfg(label) -> {
        Position pos = (Position) table.get(`label);
        if (pos == null) throw new RuntimeException("The label "+`label+" is not referenced");
        Wfg ref = `posWfg();
        int[] array = pos.toArray();
        for(int i=0;i<pos.depth();i++){
          ref = `posWfg(ref*,array[i]);
        }
        return ref; 
      }
    }
  }

  public static Wfg expand(Wfg t){
    HashMap table = new HashMap();
    return (Wfg) `Sequence(RepeatId(TopDown(CollectWfgs(table))),TopDown(Replacelabels(table))).apply(t);
  }

}

