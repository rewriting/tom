package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>x.accept(ChoiceId(v1,v2)) = x.accept(v1) if x.accept(v1)!=x</code>
 * <code>x.accept(ChoiceId(v1,v2)) = x.accept(v2) if x.accept(v1)=x</code>
 * <p>
 * Basic visitor combinator with two visitor arguments, that applies
 * these visitors one after the other (sequential composition), if the first
 * one is not the identity.
 */

public class ChoiceId extends AbstractMuStrategy {
	public final static int FIRST = 0;
	public final static int THEN = 1;

  private VisitableVisitor S_FIRST;
  private VisitableVisitor S_THEN;
  public ChoiceId(VisitableVisitor first, VisitableVisitor then) {
    initSubterm(first,then);
    S_FIRST = first;
    S_THEN = then;
  }

  public ChoiceId(VisitableVisitor v1, VisitableVisitor v2, VisitableVisitor v3) {
    initSubterm(v1,new ChoiceId(v2, v3));
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    Visitable v = S_FIRST.visit(any);
    if (v == any) {
      return S_THEN.visit(v);
    } else {
      return v;
    }
    
  }
  
}
