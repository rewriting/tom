package debugger;

import tom.library.sl.*;

public class BasicDecorator extends ADecorator {

  public BasicDecorator(Strategy s) {
    String[] names = s.getClass().getName().split("[\\.\\$]");
    name = names[names.length-1];
    realCalee = s;
    Visitable[] children = s.getChildren();
    visitors = new Strategy[children.length];
    for(int i=0; i<children.length; i++) 
      visitors[i] = (Strategy) children[i];
  }

  // mimics AbstractStrategy
  public Visitable setChildren(Visitable[] cdn) {
    //System.out.println("cdn = " + cdn);
    realCalee.setChildren(cdn);
    return supersetChildren(cdn);
  }

  // mimics AbstractStrategy
  public Visitable setChildAt(int i, Visitable child) {
    //System.out.println("child = " + child);
    realCalee.setChildAt(i,child);
    return supersetChildAt(i,child);
  }
}
