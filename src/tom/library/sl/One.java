package tom.library.sl;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>T(t1,...,ti,...,tN).accept(One(v)) = T(t1,...,ti.accept(v),...,tN)</code>
 * if <code>ti</code> is the first child that succeeds.
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to exactly one child. If no children are visited 
 * successfully, then One(v) fails.
 * <p>
 * Note that side-effects of failing visits to children are not
 * undone.
 */

public class One extends AbstractMuStrategy {
  public final static int ARG = 0;
  
  public One(VisitableVisitor v) {
    initSubterm(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    if(position==null) {
      for(int i = 0; i < childCount; i++) {
        try {
          Visitable newChild = visitors[ARG].visit(any.getChildAt(i));
          return any.setChildAt(i,newChild);
        } catch(VisitFailure f) { }
      }
    } else {
      for(int i = 0; i < childCount; i++) {
        try { 
          //System.out.println("One.pos = " + position);
          position.down(i+1);
          Visitable newChild = visitors[ARG].visit(any.getChildAt(i));
          position.up();
          return any.setChildAt(i,newChild);
        } catch(VisitFailure f) {
          position.up();
        }
      }
    }
    throw new VisitFailure();
  }

  protected void visit() throws jjtraveler.VisitFailure {
    int childCount = getSubject().getChildCount();
      
    for(int i = 0; i < childCount; i++) {
      System.out.println("One: try i = " + i + " on " + getSubject());
      try { 
        //System.out.println("One.pos = " + environment);
        environment.down(i+1);
        ((AbstractMuStrategy)visitors[ARG]).visit();
        System.out.println("One: succeeds: " + getSubject());
        environment.up();
        return ;
      } catch(jjtraveler.VisitFailure f) {
        System.out.println("One: failed on: " + getSubject());
        environment.up();
      }
    }
    throw new jjtraveler.VisitFailure();
  }

}
