package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>Choice(v1,v2) = v1</code>    if v1 succeeds
 * <p>
 * <code>Choice(v1,v2) = v2</code>    if v1 fails
 * <p>
 * Visitor combinator with two visitor arguments, that tries to
 * apply the first visitor and if it fails tries the other 
 * (left-biased choice).
 * <p>
 * Note that any side-effects of v1 are not undone when it fails.
 */

public class Choice extends AbstractVisitableVisitor {
  protected final static int FIRST = 0;
  protected final static int THEN = 1;
  public Choice(VisitableVisitor first, VisitableVisitor then) {
    initSubterm(first,then);
  }
    
  public Visitable visit(Visitable visitable) throws VisitFailure {
    try {
      return getArgument(FIRST).visit(visitable);
    } catch (VisitFailure f) {
      return getArgument(THEN).visit(visitable);
    }
  }
}
