package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>T(t1,..,ti,..,tN).accept(Some(v)) = T(t1,...ti.accept(v),..,tN)</code>
 * for each <code>ti</code> that succeeds.
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children. If no children are visited 
 * successfully, then Some(v) fails.
 * <p>
 * Note that side-effects of failing visits to children are not
 * undone.
 *
 * @author Arie van Deursen. Based on One.java
 * @date December 2002.
 */

public class Some extends AbstractVisitableVisitor {
  public Some(VisitableVisitor v) {
    init(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    Visitable result = any;
    int successCount = 0;
    for (int i = 0; i < childCount; i++) {
	    try { 
        result = result.setChildAt(i,getArgument(0).visit(any.getChildAt(i))); 
        successCount++;
	    } catch(VisitFailure f) { }
    }
    if (successCount == 0) {
	    throw new VisitFailure("Some: None of the " + 
                             childCount + " arguments of " +
                             any + " succeeded.");
    }
    return result;
  }

}
