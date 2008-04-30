package debugger;

import tom.library.sl.*;

public abstract class ADecorator extends AbstractStrategy {

  %include{sl.tom}
 
  protected String name;
  protected Strategy realCalee;

  public abstract Visitable setChildren(Visitable[] c);
  public abstract Visitable setChildAt(int i, Visitable c);

  // because there is no super.super in java
  protected Visitable supersetChildren(Visitable[] cdn) {
    return super.setChildren(cdn);
  }

  protected Visitable supersetChildAt(int i, Visitable c) {
    return super.setChildAt(i,c);
  }

  public String getName() { return name; }
  public Strategy getSlot(int i) { return visitors[i-1]; }

  private String pretty(Strategy s) {
    //System.out.println("pretty : " + s.getClass().getName());
    %match(s) { MuVar(x) -> { return `x; } }
    return s.toString();
  }

  public String toString() {
    //System.out.println("toString : " + name);
    int arity = visitors.length;
    StringBuffer buf = new StringBuffer();
    buf.append(name);
    buf.append("(");
    for(int i=0; i<arity-1; i++) {
      buf.append(pretty(visitors[i]));
      buf.append(",");
    }
    if (arity>0) buf.append(pretty(visitors[arity-1]));
    buf.append(")");
    return buf.toString();
  }

  public Object visitLight(Object any, Introspector m) throws VisitFailure {
      System.out.println(this);
      return realCalee.visitLight(any,m);
  }

  protected void printState() {
    System.out.println("---------------------\n");
    System.out.println(this);
    tom.library.utils.Viewer.toTree((Visitable) getEnvironment().getSubject());
  }

  public int visit(Introspector m) {
    printState();
    AbstractStrategy.init(realCalee,getEnvironment());
    return realCalee.visit(m);
  }
}
