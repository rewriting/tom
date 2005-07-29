package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>T(t1,..,ti,..,tN).accept(OneId(v)) = T(t1,...ti.accept(v),..,tN)</code>
 * if <code>ti</code> is the first child that is modified.
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to exactly one child. If no children are visited 
 * successfully, then OneId(v) fails.
 * <p>
 * Note that side-effects of failing visits to children are not
 * undone.
 */

public class OneId extends AbstractVisitableVisitor {
  public OneId(VisitableVisitor v) {
    initSubterm(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    if(getPosition()!=null) {
      for (int i = 0; i < childCount; i++) {
        getPosition().down(i);
        Visitable newSubterm = getArgument(0).visit(any.getChildAt(i));
        getPosition().up();
        if (newSubterm != any.getChildAt(i)) {
          return any.setChildAt(i,newSubterm);
        } 
      } 
    } else {
      for (int i = 0; i < childCount; i++) {
        Visitable newSubterm = getArgument(0).visit(any.getChildAt(i));
        if (newSubterm != any.getChildAt(i)) {
          return any.setChildAt(i,newSubterm);
        } 
      } 
    }
    return any;
  }

}
