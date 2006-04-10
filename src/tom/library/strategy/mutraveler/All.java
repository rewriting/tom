package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>All(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children.
 */

public class All extends AbstractVisitableVisitor {
  protected final static int ARG = 0;

  public All(VisitableVisitor v) {
    initSubterm(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    Visitable result = any;
    if (any instanceof MuVisitable) {
      Visitable[] childs = new Visitable[childCount];

      for (int i = 0; i < childCount; i++) {
        childs[i] = getArgument(ARG).visit(any.getChildAt(i));
      }
      result = ((MuVisitable) any).setChilds(childs);
    } else {
      //System.out.println("All.visit(" + any.getClass() + ")");
      if(!hasPosition()) {
        for (int i = 0; i < childCount; i++) {
          Visitable newChild = getArgument(ARG).visit(result.getChildAt(i));
          result = result.setChildAt(i, newChild);
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            //System.out.println(" -> " + getArgument(0).getClass() + ".visit(" + result.getChildAt(i) + ")");
            //System.out.println("All.pos = " + getPosition());
            getPosition().down(i+1);
            Visitable newChild = getArgument(ARG).visit(result.getChildAt(i));
            getPosition().up();
            result = result.setChildAt(i, newChild);
          }
        } catch(VisitFailure f) {
          getPosition().up();
          throw new VisitFailure();
        }
      }
    }
    return result;
  }

}
