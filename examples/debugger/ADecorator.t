package debugger;

import tom.library.sl.*;

public abstract class ADecorator extends AbstractStrategyCombinator {

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
  public Strategy getSlot(int i) { return getVisitor(i-1); }

  private String pretty(Strategy s) {
    //System.out.println("pretty : " + s.getClass().getName());
    %match(s) { MuVar(x) -> { return `x; } }
    return s.toString();
  }

  public String toString() {
    //System.out.println("toString : " + name);
    int arity = getChildCount();
    StringBuffer buf = new StringBuffer();
    buf.append(name);
    buf.append("(");
    for(int i=0; i<arity-1; i++) {
      buf.append(pretty(getVisitor(i)));
      buf.append(",");
    }
    if (arity>0) buf.append(pretty(getVisitor(arity-1)));
    buf.append(")");
    return buf.toString();
  }

  public Object visitLight(Object any, Introspector m) throws VisitFailure {
      System.out.println(this);
      return realCalee.visitLight(any,m);
  }

  public int visit(Introspector m) {
      String repr = this.toString();
      StringBuffer spaces = new StringBuffer();
      for(int i=0; i<60-repr.length(); i++) spaces.append(' ');
      System.out.println(repr + spaces + getEnvironment().getSubject());
      //System.out.println(environment);
      //System.out.println(realCalee);
      AbstractStrategyCombinator.init(realCalee,getEnvironment());
      return realCalee.visit(m);
  }
}
