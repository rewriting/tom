package debugger;

import tom.library.sl.*;
import java.util.Arrays;

public class Decorator extends ADecorator {

  %include{sl.tom}

  protected Maker maker;

  public Decorator(String name, Maker maker, Strategy[] children) {
    initSubterm(children);
    this.maker = maker;
    this.realCalee = maker.buildDecorated(getVisitors());
    this.name = name;
  }

  // mimics AbstractStrategyCombinator
  public Visitable setChildren(Visitable[] cdn) {
    realCalee = maker.buildDecorated((Strategy[]) cdn);
    return supersetChildren(cdn);
  }

  // mimics AbstractStrategyCombinator
  public Visitable setChildAt(int i, Visitable child) {
    Strategy[] copy = new Strategy[getChildCount()];
    for (int k=0; k<getChildCount(); k++) {
      copy[k] = getVisitor(k);
    }
    copy[i] = (Strategy) child;
    realCalee = maker.buildDecorated(copy);
    return supersetChildAt(i,child);
  }

}
