package tom.library.sl;

import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

import java.util.LinkedList;
import java.util.Iterator;

public class Mu extends AbstractStrategyLanguage {
  public final static int VAR = 0;
  public final static int V = 1;

  private MuStrategyTopDown muStrategyTopDown;
  private SLStrategyTopDown slStrategyTopDown;
  private boolean expanded =false;
  public Mu(VisitableVisitor var, VisitableVisitor v) {
    initSubterm(var, v);
    muStrategyTopDown = new MuStrategyTopDown();
    slStrategyTopDown = new SLStrategyTopDown();
  }

  public final Visitable visit(Visitable any) throws VisitFailure {
    if(!expanded) { muExpand(); }
    return visitors[V].visit(any);
  }

  protected void visit() throws jjtraveler.VisitFailure {
    //System.out.println("try Mu on: " + getSubject());
    if(!expanded) { slExpand(); }
    ((AbstractStrategyLanguage)visitors[V]).visit();
    //System.out.println("Mu succeeds: " + getSubject());
  }

  private boolean isExpanded() {
    return ((MuVar)visitors[VAR]).isExpanded();
  }

  public void muExpand() {
    try {
      muStrategyTopDown.init();
      muStrategyTopDown.visit(this);
      expanded = true;
    } catch (VisitFailure e) {
      System.out.println("mu reduction failed");
    }
  }
  
  public void slExpand() {
    try {
      //System.out.println("SL TopDown.init");
      slStrategyTopDown.init();
      //System.out.println("SL TopDown.visit");
      slStrategyTopDown.visit(this);
      //((AbstractStrategyLanguage)myMuTopDown).gapply(this);
      expanded = true;
    } catch (VisitFailure e) {
      System.out.println("SL mu reduction failed");
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
class MuStrategyTopDown {
  %typeterm MuStrategy {
    implement { tom.library.strategy.mutraveler.MuStrategy }
    equals(t1,t2) {t1.equals(t2)}
    visitor_fwd { tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd }
  }

  %op MuStrategy Mu(s1:MuStrategy, s2:MuStrategy) {
    is_fsym(t) { (t instanceof tom.library.strategy.mutraveler.Mu) }
    make(var, v) { new tom.library.strategy.mutraveler.Mu(var, v) }
    get_slot(s1, t) { (tom.library.strategy.mutraveler.MuStrategy)t.getChildAt(tom.library.strategy.mutraveler.Mu.VAR) }
    get_slot(s2, t) { (tom.library.strategy.mutraveler.MuStrategy)t.getChildAt(tom.library.strategy.mutraveler.Mu.V) }
  }

  %typeterm MuStrategyString {
    implement { String }
    equals(t1,t2) {t1.equals(t2)}
  }

  %op MuStrategy MuVar(var:MuStrategyString) {
    is_fsym(t) { (t instanceof tom.library.strategy.mutraveler.MuVar) }
    make(name) { new tom.library.strategy.mutraveler.MuVar(name) }
    get_slot(var, t) { ((tom.library.strategy.mutraveler.MuVar)t).getName() }
  }

  private LinkedList stack;

  public MuStrategyTopDown() {
    stack = new LinkedList();
  }
  public void init() {
    stack.clear();
  }

  public void visit(Visitable any) throws VisitFailure {
    %match(any) {
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
              //System.out.println("MuVar: setInstance " + `n );
              muvar.setInstance(m);
              return;
            }
          }
          //System.out.println("MuVar: " + `n + " not found");
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

class SLStrategyTopDown {
  %typeterm SLStrategy {
    implement { tom.library.sl.StrategyLanguage }
    equals(t1,t2) {t1.equals(t2)}
    visitor_fwd { tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd }
  }

  %op SLStrategy MuSL(s1:SLStrategy, s2:SLStrategy) {
    is_fsym(t) { (t instanceof tom.library.sl.Mu) }
    make(var, v) { new tom.library.sl.Mu(var, v) }
    get_slot(s1, t) { (tom.library.sl.StrategyLanguage)t.getChildAt(tom.library.sl.Mu.VAR) }
    get_slot(s2, t) { (tom.library.sl.StrategyLanguage)t.getChildAt(tom.library.sl.Mu.V) }
  }

  %typeterm SLStrategyString {
    implement { String }
    equals(t1,t2) {t1.equals(t2)}
  }

  %op SLStrategy MuVarSL(var:SLStrategyString) {
    is_fsym(t) { (t instanceof tom.library.sl.MuVar) }
    make(name) { new tom.library.sl.MuVar(name) }
    get_slot(var, t) { ((tom.library.sl.MuVar)t).getName() }
  }

  private LinkedList stack;

  public SLStrategyTopDown() {
    stack = new LinkedList();
  }
  public void init() {
    stack.clear();
  }

  public void visit(Visitable any) throws VisitFailure {
    %match(any) {
      m@MuSL(var@MuVarSL(_), v) -> {
        stack.addFirst(`m);
        visit(`v);
        visit(`var);
        stack.removeFirst();
        return;
      }

      var@MuVarSL(n) -> {
        MuVar muvar = (MuVar)`var;
        if(!muvar.isExpanded()) {
          Iterator it = stack.iterator();
          while(it.hasNext()) {
            Mu m = (Mu)it.next();
            if(((MuVar)m.getArgument(Mu.VAR)).getName().equals(`n)) {
              //System.out.println("MuVar: setInstance " + `n );
              muvar.setInstance(m);
              return;
            }
          }
          //System.out.println("MuVar: " + `n + " not found");
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
