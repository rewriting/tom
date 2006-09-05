package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>Omega(i,v)</code>
 * <p>
 * Basic visitor combinator which applies v the i-th subterm
 * 0-th subterm is the term itself
 * 1-th subterm corresponds to the first subterm
 * ...
 * arity-th subterm corresponds to the last subterm
 * <p>
*/

public class Omega extends AbstractMuStrategy {
  public final static int ARG = 0;
  protected int indexPosition;

  public Omega(int indexPosition, VisitableVisitor v) {
    initSubterm(v);
    this.indexPosition = indexPosition;
  }

  public int getPos() {
    return indexPosition;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    if(indexPosition==0) {
      return any;
    } else if(indexPosition>0 && indexPosition<=any.getChildCount()) {
      int childNumber = indexPosition-1;
      if(position==null) {
        Visitable newChild = getArgument(ARG).visit(any.getChildAt(childNumber));
        return any.setChildAt(childNumber,newChild);
      } else {
        try {
          position.down(indexPosition);
          Visitable newChild = getArgument(ARG).visit(any.getChildAt(childNumber));
          position.up();
          return any.setChildAt(childNumber,newChild);
        } catch(VisitFailure f) {
          position.up();
          throw new VisitFailure();
        }
      }
    } else {
      throw new VisitFailure();
    }
  }
}

