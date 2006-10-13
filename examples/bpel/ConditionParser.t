package bpel;

import bpel.wfg.types.*;
import aterm.*;
import aterm.pure.*;

public class ConditionParser {
  %include {wfg/Wfg.tom}
  %include {Mapping.tom}

  public Condition getCondition(ATerm t) {
    %match(t) {
      OR(_,(c1,c2)) -> { 
        return `or(getCondition(c1), getCondition(c2));
      }
      AND(_,(c1,c2)) -> { 
        return `and(getCondition(c1), getCondition(c2));
      }
      ID(NodeInfo[text=text],_) -> {
        // TODO
        return `cond(Empty());
      }
    }
    return null;
  }
}
