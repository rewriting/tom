package jjtraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.reflective.AbstractVisitableVisitor;

/**
 * <code>T(t1,..,ti,..,tN).accept(One(v)) = T(t1,...ti.accept(v),..,tN)</code>
 * if <code>ti</code> is the first child that succeeds.
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to exactly one child. If no children are visited 
 * successfully, then One(v) fails.
 * <p>
 * Note that side-effects of failing visits to children are not
 * undone.
 */

public class One extends AbstractVisitableVisitor {
  public One(VisitableVisitor v) {
    init(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    for (int i = 0; i < childCount; i++) {
      try { 
        return any.setChildAt(i,getArgument(0).visit(any.getChildAt(i))); 
      } catch(VisitFailure f) { }
    }
    throw new VisitFailure();
  }

}
