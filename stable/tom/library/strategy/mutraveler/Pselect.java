package tom.library.strategy.mutraveler;
import java.util.Random;

import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>Pselect(p,q,v1,v2) = v1</code> with probability p/q
 * <p>
 * <code>Pselect(p,q,v1,v2) = v2</code> with probability 1-(p/q)
 * <p>
 * Visitor combinator with a probability and two visitor arguments, 
 * that select a visitor according to the probability p/q
 * The strategy fails if the selected strategy fails
 * <p>
 * Note that any side-effects of v1 are not undone when it fails.
 */

public class Pselect extends AbstractVisitableVisitor {
	protected final static int FIRST = 0;
	protected final static int THEN = 1;
  private int p;
  private int q;
  private static Random random = null;

  public Pselect(int p, int q,VisitableVisitor first, VisitableVisitor then) {
    init(first,then);
    this.p = p;
    this.q = q;
    if(random == null) {
      random = new Random();
    }
  }
    
  public Visitable visit(Visitable visitable) throws VisitFailure {
    int randomInt = Math.abs(random.nextInt());
    if(randomInt % q < p) {
      return getArgument(FIRST).visit(visitable);
    } else {
      return getArgument(THEN).visit(visitable);
    }
  }
}
