package ted;
import jjtraveler.Visitable;

class DummyObserver implements DebugStrategyObserver {

  protected int scope = 0;

  public void before(DebugStrategy s) {
    String[] names = s.getStrat().getClass().getName().split("[\\.\\$]");
    String name = names[names.length-1];
    System.out.println("[" + (scope++) + "] applying " + name + " at " + s.getPosition());
  }
  public void after(DebugStrategy s, Visitable res) {
    System.out.println("[" + (--scope) + "] new subtree : " + res);
  }
}
