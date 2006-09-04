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
  private Visitor S_ARG;
  public All(VisitableVisitor v) {
    initSubterm(v);
    S_ARG = v;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    Visitable result = any;
    //Visitor S = getArgument(ARG); // remove for Scompiler
    if (any instanceof MuVisitable) {
      Visitable[] childs = null;
      if(!hasPosition()) {
        for (int i = 0; i < childCount; i++) {
          Visitable oldChild = any.getChildAt(i);
          Visitable newChild = S_ARG.visit(oldChild); // remove for Scompiler
          //Visitable newChild = getArgument(ARG).visit(oldChild); // activate for Scompiler
          if(childs != null) {
            childs[i] = newChild;
          } else if(newChild != oldChild) {
            // allocate the array, and fill it
            childs = new Visitable[childCount];
            for (int j = 0 ; j<i ; j++) {
              childs[j] = any.getChildAt(j);
            }
            childs[i] = newChild;
          }
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            Visitable oldChild = any.getChildAt(i);
            getPosition().down(i+1);
            Visitable newChild = S_ARG.visit(oldChild); // remove for Scompiler
            //Visitable newChild = getArgument(ARG).visit(oldChild); // activate for Scompiler
            getPosition().up();
            if(childs != null) {
              childs[i] = newChild;
            } else if(newChild != oldChild) {
              // allocate the array, and fill it
              childs = new Visitable[childCount];
              for (int j = 0 ; j<i ; j++) {
                childs[j] = any.getChildAt(j);
              }
              childs[i] = newChild;
            }
          }
        } catch(VisitFailure f) {
          getPosition().up();
          throw new VisitFailure();
        }
      }
      if(childs!=null) {
        result = ((MuVisitable) any).setChilds(childs);
      }
    } else {
      //System.out.println("All.visit(" + any.getClass() + ")");
      if(!hasPosition()) {
        for (int i = 0; i < childCount; i++) {
          Visitable newChild = S_ARG.visit(result.getChildAt(i)); // remove for Scompiler
          //Visitable newChild = getArgument(ARG).visit(result.getChildAt(i)); // activate for Scompiler
          result = result.setChildAt(i, newChild);
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            //System.out.println(" -> " + getArgument(0).getClass() + ".visit(" + result.getChildAt(i) + ")");
            //System.out.println("All.pos = " + getPosition());
            getPosition().down(i+1);
            Visitable newChild = S_ARG.visit(result.getChildAt(i)); // remove for Scompiler
            // Visitable newChild = getArgument(ARG).visit(result.getChildAt(i)); // activate for Scompiler
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
