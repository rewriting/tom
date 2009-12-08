package debugger;

import tom.library.sl.*;

public class BasicDecorator extends ADecorator {

  public BasicDecorator(Strategy s) {
    String[] names = s.getClass().getName().split("[\\.\\$]");
    name = names[names.length-1];
    realCalee = s;
    if(s instanceof AbstractStrategyCombinator) {
    initSubterm((Strategy[])s.getChildren());
    }
  }

  // mimics AbstractStrategyCombinator
  public Visitable setChildren(Visitable[] cdn) {
    //System.out.println("cdn = " + cdn);
    realCalee.setChildren(cdn);
    return supersetChildren(cdn);
  }

  // mimics AbstractStrategyCombinator
  public Visitable setChildAt(int i, Visitable child) {
    //System.out.println("child = " + child);
    realCalee.setChildAt(i,child);
    return supersetChildAt(i,child);
  }
}
