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

public class RelativeRef extends Ref {

  public RelativeRef(Visitable s, MuStrategy v) {
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

