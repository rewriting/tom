package jjtraveler;
import jjtraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.reflective.VisitableVisitor;

public class IfThenElse extends AbstractVisitableVisitor {
	
  private final static int CONDITION = 1;
  private final static int TRUE_CASE = 2;
  private final static int FALSE_CASE = 3;

  public IfThenElse(VisitableVisitor c, VisitableVisitor t, VisitableVisitor f) {
    init(c,t,f);
  }

  public IfThenElse(VisitableVisitor c, VisitableVisitor t) {
    init(c,t, new Identity());
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
