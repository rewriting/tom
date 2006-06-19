package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>T(t1,...,ti,...,tN).accept(Some(v)) = T(t1,...,ti.accept(v),...,tN)</code>
 * for each <code>ti</code> that succeeds.
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children. If no children are visited 
 * successfully, then Some(v) fails.
 * <p>
 * Note that side-effects of failing visits to children are not
 * undone.
 *
 */

public class Some extends AbstractMuStrategy {
  public final static int ARG = 0;
  public Some(VisitableVisitor v) {
    initSubterm(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    Visitable result = any;
    int successCount = 0;
    if(!hasPosition()) {
      for (int i = 0; i < childCount; i++) {
        try { 
          result = result.setChildAt(i,getArgument(ARG).visit(any.getChildAt(i))); 
          successCount++;
        } catch(VisitFailure f) { }
      }
    } else {
      for (int i = 0; i < childCount; i++) {
        try { 
          getPosition().down(i+1);
          Visitable newChild = getArgument(ARG).visit(any.getChildAt(i));
          getPosition().up();
          result = result.setChildAt(i,newChild); 
          successCount++;
        } catch(VisitFailure f) { 
          getPosition().up();
        }
      }
    }
    if (successCount == 0) {
      throw new VisitFailure("Some: None of the " + 
                             childCount + " arguments of " +
                             any + " succeeded.");
    }
    return result;
  }

}
