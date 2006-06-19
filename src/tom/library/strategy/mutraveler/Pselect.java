package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
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

public class Pselect extends AbstractMuStrategy {
  public final static int FIRST = 0;
  public final static int THEN = 1;
  private int p;
  private int q;
  private static java.util.Random random = null;

  public Pselect(int p, int q,VisitableVisitor first, VisitableVisitor then) {
    initSubterm(first,then);
    this.p = p;
    this.q = q;
    if(random == null) {
      random = new java.util.Random();
    }
  }

  public int getP() {
    return p;
  }
  public int getQ() {
    return q;
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
