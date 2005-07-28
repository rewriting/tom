package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>x.accept(SequenceId(v1,v2)) = x.accept(v1) ; x.accept(v2) if x.accept(v1)!=x</code>
 * <p>
 * Basic visitor combinator with two visitor arguments, that applies
 * these visitors one after the other (sequential composition), if the first
 * one is not the identity.
 */

public class SequenceId extends AbstractVisitableVisitor {
	protected final static int FIRST = 0;
	protected final static int THEN = 1;
  public SequenceId(VisitableVisitor first, VisitableVisitor then) {
    initSubterm(first,then);
  }

  public SequenceId(VisitableVisitor v1, VisitableVisitor v2, VisitableVisitor v3) {
    initSubterm(v1,new SequenceId(v2, v3));
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    Visitable v = getArgument(FIRST).visit(any);
    if (!(v == any)) {
      return getArgument(THEN).visit(v);
    } else {
      return v;
    }
    
  }
  
}
