package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

import java.util.LinkedList;
import java.util.Iterator;

public class Mu extends AbstractMuStrategy {
  public final static int VAR = 0;
  public final static int V = 1;

  private MuTopDown myMuTopDown;
  private boolean expanded =false;
  public Mu(VisitableVisitor var, VisitableVisitor v) {
    initSubterm(var, v);
    myMuTopDown = new MuTopDown();
  }

  public final Visitable visit(Visitable any) throws VisitFailure {
    if(!expanded) { expand(); }
    return visitors[V].visit(any);
  }

  private boolean isExpanded() {
    return ((MuVar)visitors[VAR]).isExpanded();
  }

  public void expand() {
    try {
      myMuTopDown.init();
      myMuTopDown.visit(this);
      expanded = true;
    } catch (VisitFailure e) {
      System.out.println("mu reduction failed");
    }
  }

}

/**
 * Custom TopDown strategy which realizes the mu expansion.
 * The visit method seeks all Mu and MuVar nodes.
 *
 * When a Mu node is matched, it is pushed on a stack. Then child nodes are
 * visited and finally, the Mu node is popped.
 *
 * When a MuVar node is matched, then the stack is browsed to find the
 * corresponding Mu (the last pushed with the same variable name). The MuVar is
 * then expanded.
 *
 * When the current node is not a Mu or a MuVar, we visit all children of the
 * current node.
 */
class MuTopDown {
  %include { mustrategy.tom }

  private LinkedList stack;

  public MuTopDown() {
    stack = new LinkedList();
  }
  public void init() {
    stack.clear();
  }

  public void visit(Visitable any) throws VisitFailure {
    %match(Strategy any) {
      m@Mu(var@MuVar(_), v) -> {
	stack.addFirst(`m);
	visit(`v);
	visit(`var);
	stack.removeFirst();
	return;
      }

      var@MuVar(n) -> {
	MuVar muvar = (MuVar)`var;
	if(!muvar.isExpanded()) {
	  Iterator it = stack.iterator();
	  while(it.hasNext()) {
	    Mu m = (Mu)it.next();
	    if(((MuVar)m.getArgument(Mu.VAR)).getName().equals(`n)) {
	      muvar.setInstance(m);
	      return;
	    }
	  }
	  throw new VisitFailure();
	}
      }
    }

    int childCount = any.getChildCount();
    for(int i = 0; i < childCount; i++) {
      visit(any.getChildAt(i));
    }
  }
}
