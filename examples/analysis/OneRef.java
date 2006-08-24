package analysis;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
import tom.library.strategy.mutraveler.Position;
import jjtraveler.Visitable;
import jjtraveler.Visitor;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import analysis.cfg.types.reference.Ref;
import java.util.ArrayList;

/**
 * <code>T(t1,...,ti,...,tN).accept(OneRef(v)) = T(t1,...,ti.accept(v),...,tN)</code>
 * if <code>ti</code> is the first child that succeeds.
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to exactly one child of type Ref. If no children are visited 
 * successfully, then OneRef(v) fails.
 * <p>
 * Note that side-effects of failing visits to children are not
 * undone.
 */

public class OneRef extends AbstractMuStrategy {
  public final static int ARG = 0;
  private Visitable originalSubj;


  public OneRef(Visitable s, VisitableVisitor v) {
    initSubterm(v);
    originalSubj = s;
  }

  public Ref visitRef(Ref ref, Visitor strat) throws VisitFailure{
    //find the subterm at the position and modify it
    Position pos = new Position(ref.toArray());
    Visitable subtermRef = pos.getSubterm().visit(originalSubj);
    final Visitable subtermModified = strat.visit(subtermRef);
     pos.getReplace(subtermModified).visit(originalSubj);
    return ref;
  }

 
  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    if(!hasPosition()) {
      for(int i = 0; i < childCount; i++) {
        try {
          Visitable oldChild = any.getChildAt(i);
          if (oldChild instanceof Ref){
            Visitable newChild = visitRef((Ref) oldChild,getArgument(ARG));
            return any.setChildAt(i,newChild);
          }
        } catch(VisitFailure f) { }
      }
    } else {
      for(int i = 0; i < childCount; i++) {
        try { 
          getPosition().down(i+1);
          Visitable oldChild = any.getChildAt(i);
          if (oldChild instanceof Ref){
            Visitable newChild = visitRef((Ref) oldChild,getArgument(ARG));
            getPosition().up();
            return any.setChildAt(i,newChild);
          }
        } catch(VisitFailure f) {
          getPosition().up();
        }
      }
    }
    throw new VisitFailure();
  }

}
