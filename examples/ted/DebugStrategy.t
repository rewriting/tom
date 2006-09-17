package ted;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;
import tom.library.strategy.mutraveler.*;
import jjtraveler.Visitable;

class DebugStrategy extends AbstractMuStrategy {
  protected DebugStrategyObserver obs;

  public DebugStrategy(DebugStrategyObserver obs, VisitableVisitor v) {
    initSubterm(v);
    this.obs = obs;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    obs.before(this);
    Visitable res = getStrat().visit(any);
    obs.after(this, res);
    return res;
  }

  public DebugStrategyObserver getObserver() {
    return obs;
  }

  public VisitableVisitor getStrat() {
    return (VisitableVisitor) getChildAt(0);
  }
}

