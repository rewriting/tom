package jjtraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.reflective.AbstractVisitableVisitor;

/**
 * <code>Not(v)</code> succeeds if and only if <code>v</code> fails.
 */

public class Not extends AbstractVisitableVisitor {
	
  public Not(VisitableVisitor v) {
    init(v);
  }

  public Visitable visit(Visitable x) throws VisitFailure {
    try {
	    getArgument(0).visit(x);
    } catch (VisitFailure f) {
	    return x;
    }
    throw new VisitFailure();
  }

}
