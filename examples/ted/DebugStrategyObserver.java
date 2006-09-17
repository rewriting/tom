package ted;

import jjtraveler.Visitable;

interface DebugStrategyObserver {
  public void before(DebugStrategy s);
  public void after(DebugStrategy s, Visitable res);
}


