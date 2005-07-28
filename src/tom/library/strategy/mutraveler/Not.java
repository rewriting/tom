package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>Not(v)</code> succeeds if and only if <code>v</code> fails.
 */

public class Not extends AbstractVisitableVisitor {
  
  public Not(VisitableVisitor v) {
    initSubterm(v);
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
