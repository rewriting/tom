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
  protected int position;

  public Omega(int position, VisitableVisitor v) {
    initSubterm(v);
    this.position = position;
  }

  public int getPos() {
    return position;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    if(position==0) {
      return any;
    } else if(position>0 && position<=any.getChildCount()) {
      int childNumber = position-1;
      if(!hasPosition()) {
        Visitable newChild = getArgument(ARG).visit(any.getChildAt(childNumber));
        return any.setChildAt(childNumber,newChild);
      } else {
        try {
          getPosition().down(position);
          Visitable newChild = getArgument(ARG).visit(any.getChildAt(childNumber));
          getPosition().up();
          return any.setChildAt(childNumber,newChild);
        } catch(VisitFailure f) {
          getPosition().up();
          throw new VisitFailure();
        }
      }
    } else {
      throw new VisitFailure();
    }
  }
}

