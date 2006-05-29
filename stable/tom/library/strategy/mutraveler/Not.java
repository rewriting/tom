package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>Not(v)</code> succeeds if and only if <code>v</code> fails.
 */

public class Not extends AbstractMuStrategy {
  protected final static int ARG = 0;

  public Not(VisitableVisitor v) {
    initSubterm(v);
  }

  public Visitable visit(Visitable x) throws VisitFailure {
    try {
      getArgument(ARG).visit(x);
    } catch (VisitFailure f) {
      return x;
    }
    throw new VisitFailure();
  }

}
