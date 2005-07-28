package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;

public class MuTraveler {
  public static VisitableVisitor init(VisitableVisitor v) {
    VisitableVisitor res = v;
    if(v instanceof AbstractVisitableVisitor) {
      AbstractVisitableVisitor avv = (AbstractVisitableVisitor) v;
      res = avv.init();
    } else {
      throw new RuntimeException("init(v): v should be an AbstractVisitableVisitor");
    }
    return res;
  }
}
