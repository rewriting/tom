package jjtraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.reflective.AbstractVisitableVisitor;

/**
 * <code>MuVar(v)</code> always raises a VisitFailure exception. 
 * <p>
 * Basic visitor combinator used to build recursive visitors
 * <p>

*/

public class MuVar extends AbstractVisitableVisitor {
  private VisitableVisitor instance = null;
  protected String name;
	
  public MuVar(String name) {
    init();
    this.name = name;
  }

  /*
  public MuVar(VisitableVisitor v) {
    init();
    this.instance = v;
  }
  */


  public boolean equals(Object o) {
    if(o instanceof MuVar) {
      MuVar muVar = (MuVar)o;
      if(muVar != null) {
        return muVar.name.equals(this.name);
      }
    }
    return false;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    //System.out.println("MuVar.visit(" + any.getClass() + ")");
    if(instance != null) {
      //System.out.println(" -> " + getArgument(0).getClass() + ".visit(" + any.getClass() + ")");
      return instance.visit(any);
    } else {
      //System.out.println(" -> failure");
      throw new VisitFailure();
    }
  }

  public void setInstance(VisitableVisitor v) {
    this.instance = v;
  }
  public void setName(String name) {
    this.name = name;
  }

  public static VisitableVisitor mu(VisitableVisitor var, VisitableVisitor v) {
    try {
      VisitableVisitor muExpander = new BottomUp(new MuExpander(var,v));
      return (VisitableVisitor) muExpander.visit((Visitable)v);
    } catch (VisitFailure e) {
      System.out.println("mu reduction failed");
    }
    return v;
  }
  
}

class MuExpander implements VisitableVisitor {
  VisitableVisitor variable;
  VisitableVisitor instance;
  public MuExpander(VisitableVisitor variable, VisitableVisitor instance) {
    this.variable = variable;
    this.instance = instance;
  }
    
  public Visitable visit(Visitable v) throws VisitFailure { 
    if(v instanceof MuVar) {
      MuVar muV = (MuVar)v;
      MuVar muVariable = (MuVar)variable;
      if(muV.equals(muVariable)) {
        muV.setInstance(instance);
        muV.setName(null);
      } 
    }
    return v;
  }

  public int getChildCount() {
    return 2;
  }

  public Visitable getChildAt(int i) {
    switch (i) {
    case 0: return variable;
    case 1: return instance;
    default: throw new IndexOutOfBoundsException();
    }
  }

  public Visitable setChildAt(int i, Visitable child) {
    switch (i) {
    case 0: variable = (VisitableVisitor)child; return this;
    case 1: instance = (VisitableVisitor)child; return this;
    default: throw new IndexOutOfBoundsException();
    }
  }

}
