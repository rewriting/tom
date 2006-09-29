package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.Visitor;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>All(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children.
 */

public class All extends AbstractMuStrategy {
  public final static int ARG = 0;
  public All(VisitableVisitor v) {
    initSubterm(v);
  }

  public final Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    Visitable result = any;
    if (any instanceof MuVisitable) {
      Visitable[] childs = null;
      if(position==null) {
        for (int i = 0; i < childCount; i++) {
          Visitable oldChild = any.getChildAt(i);
          Visitable newChild = visitors[ARG].visit(oldChild);
          if(childs != null) {
            childs[i] = newChild;
          } else if(newChild != oldChild) {
            // allocate the array, and fill it
            childs = ((MuVisitable) any).getChilds();
            childs[i] = newChild;
          }
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            Visitable oldChild = any.getChildAt(i);
            position.down(i+1);
            Visitable newChild = visitors[ARG].visit(oldChild);
            position.up();
            if(childs != null) {
              childs[i] = newChild;
            } else if(newChild != oldChild) {
              // allocate the array, and fill it
              childs = ((MuVisitable) any).getChilds();
              childs[i] = newChild;
            }
          }
        } catch(VisitFailure f) {
          position.up();
          throw new VisitFailure();
        }
      }
      if(childs!=null) {
        result = ((MuVisitable) any).setChilds(childs);
      }
    } else {
      if(position==null) {
        for (int i = 0; i < childCount; i++) {
          Visitable newChild = visitors[ARG].visit(result.getChildAt(i));
          result = result.setChildAt(i, newChild);
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            //System.out.println(" -> " + getArgument(0).getClass() + ".visit(" + result.getChildAt(i) + ")");
            //System.out.println("All.pos = " + getPosition());
            position.down(i+1);
            Visitable newChild = visitors[ARG].visit(result.getChildAt(i));
            position.up();
            result = result.setChildAt(i, newChild);
          }
        } catch(VisitFailure f) {
          position.up();
          throw new VisitFailure();
        }
      }
    }
    return result;
  }

}
