package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

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
    // make sure the MuVar is seen as a leaf for all visitors
    initSubterm();
    this.name = name;
  }

  public boolean equals(Object o) {
    if(o instanceof MuVar) {
      MuVar muVar = (MuVar)o;
      if(muVar != null) {
        return muVar.name.equals(this.name);
      }
    }
    return false;
  }

  public int hashCode() {
    return name.hashCode();
  }
  
  public Visitable visit(Visitable any) throws VisitFailure {
    if(instance != null) {
      return instance.visit(any);
    } else {
      throw new VisitFailure();
    }
  }

  public void setInstance(VisitableVisitor v) {
    this.instance = v;
  }
  public void setName(String name) {
    this.name = name;
  }

}
