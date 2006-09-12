package bpel;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.MuVisitable;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.MuReference;
import jjtraveler.Visitable;
import jjtraveler.Visitor;
import jjtraveler.VisitFailure;
import java.util.*;
/**
 * <code>OneRelativeRefSensitive(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children and if the children is a position to
 * the term at this relative position.
 */

public class OneRelativeRefSensitive extends OneRefSensitive {

  public OneRelativeRefSensitive(Visitable s, MuStrategy v) {
    super(s,v);
  }


  public MuReference visitReference(MuReference ref, MuStrategy strat) throws VisitFailure{
    RelativePosition relativePos = new RelativePosition(ref.toArray());
    Position p = getPosition();
    Position pos = relativePos.getAbsolutePosition(p); 
    visitPosition(pos,strat);
    return ref;
  }

}
