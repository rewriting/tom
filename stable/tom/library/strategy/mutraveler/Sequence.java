package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>x.accept(Sequence(v1,v2)) = x.accept(v1) ; x.accept(v2)</code>
 * <p>
 * Basic visitor combinator with two visitor arguments, that applies
 * these visitors one after the other (sequential composition).
 */

public class Sequence extends AbstractMuStrategy {
  public final static int FIRST = 0;
  public final static int THEN = 1;
  public Sequence(VisitableVisitor first, VisitableVisitor then) {
    initSubterm(first,then);
  }

  public Sequence(VisitableVisitor v1, VisitableVisitor v2, VisitableVisitor v3) {
    initSubterm(v1,new Sequence(v2, v3));
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    //System.out.println("Sequence.visit(" + any.getClass() + ")");
    //System.out.println(" -> " + getArgument(FIRST).getClass() + ".visit(" + any.getClass() + ")");
    return getArgument(THEN).visit(getArgument(FIRST).visit(any));
  }

}
