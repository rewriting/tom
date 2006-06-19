package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

public class IfThenElse extends AbstractMuStrategy {
  
  public final static int CONDITION = 0;
  public final static int TRUE_CASE = 1;
  public final static int FALSE_CASE = 2;

  public IfThenElse(VisitableVisitor c, VisitableVisitor t, VisitableVisitor f) {
    initSubterm(c,t,f);
  }

  public IfThenElse(VisitableVisitor c, VisitableVisitor t) {
    initSubterm(c,t, new Identity());
  }


  public Visitable visit(Visitable x) throws VisitFailure {
    boolean success;
    Visitable result;
    try {
      getArgument(CONDITION).visit(x);
      success = true;
    } catch (VisitFailure vf) {
      success = false;
    }
    if (success) {
      result = getArgument(TRUE_CASE).visit(x);
    } else {
      result = getArgument(FALSE_CASE).visit(x);
    }
    return result;
  }
}
