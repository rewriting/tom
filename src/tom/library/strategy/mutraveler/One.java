package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

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
    initSubterm(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    for (int i = 0; i < childCount; i++) {
      try { 
        if(getPosition()!=null) {
          //System.out.println("One.pos = " + getPosition());
          getPosition().down(i);
        }
        Visitable newChild = getArgument(0).visit(any.getChildAt(i));
        if(getPosition()!=null) {
          getPosition().up();
        }
        return any.setChildAt(i,newChild);
      } catch(VisitFailure f) {
        if(getPosition()!=null) {
          getPosition().up();
        }
      }
    }
    throw new VisitFailure();
  }

}
