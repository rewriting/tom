package jjtraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.reflective.AbstractVisitableVisitor;

/**
 * <code>All(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children.
 */

public class All extends AbstractVisitableVisitor {
		
	public All(VisitableVisitor v) {
		init(v);
	}

	public Visitable visit(Visitable any) throws VisitFailure {
    //System.out.println("All.visit(" + any.getClass() + ")");
		int childCount = any.getChildCount();
		Visitable result = any;
		for (int i = 0; i < childCount; i++) {
      //System.out.println(" -> " + getArgument(0).getClass() + ".visit(" + result.getChildAt(i) + ")");
			result = result.setChildAt(i, getArgument(0).visit(result.getChildAt(i)));
		}
		return result;
	}

}
