package ted;

import tom.library.strategy.mutraveler.*;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import tom.library.strategy.mutraveler.MuStrategy;

import ted.stratdebugger.entier.types.*;


interface DebugStrategyObserver {
  public void before(DebugStrategy s);
  public void after(DebugStrategy s, Visitable res);
}

class DebugStrategy extends AbstractMuStrategy {
  protected DebugStrategyObserver obs;

  public DebugStrategy(DebugStrategyObserver obs, VisitableVisitor v) {
    initSubterm(v);
    this.obs = obs;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    obs.before(this);
    Visitable res = getStrat().visit(any);
    obs.after(this, res);
    return res;
  }

  public DebugStrategyObserver getObserver() {
    return obs;
  }

  public VisitableVisitor getStrat() {
    return (VisitableVisitor) getChildAt(0);
  }

}

class DummyController implements DebugStrategyObserver {

  int scope = 0;

  public void before(DebugStrategy s) {
    String[] names = s.getStrat().getClass().getName().split("[\\.\\$]");
    String name = names[names.length-1];
    System.out.println("[" + (scope++) + "] applying " + name + " at " + s.getPosition());
  }
  public void after(DebugStrategy s, Visitable res) {
    System.out.println("[" + (--scope) + "] new subtree : " + res);
  }
}


public class StratDebugger {

  %gom {
    module Entier 
    imports String
    abstract syntax

    Exp = Zero()
        | Var(name:String)
        | S(n:Exp)
        | plus(n:Exp,m:Exp)
  }

  %include{ mustrategy.tom }

  %typeterm DebugStrategyObserver {
    implement { ted.DebugStrategyObserver }
  }

  %op Strategy DebugStrategy(obs:DebugStrategyObserver, s:Strategy) {
    is_fsym(t) { (t instanceof ted.DebugStrategy) }
    make(obs,s) { new DebugStrategy(obs,s) }
    get_slot(obs, t) { (DebugStrategyObserver) t.getObserver() }
    get_slot(s, t) { (MuStrategy) t.getStrat() }
  }


  %strategy DecorateStrategy(obs:DebugStrategyObserver) extends `Identity() {
    visit Strategy {
      s@MuVar[] -> { return `s; }
      s -> { return  `DebugStrategy(obs,s); }
    }
  }

  public static MuStrategy decorateStrategy(DebugStrategyObserver obs, MuStrategy s) {
    return (MuStrategy) `BottomUp(DecorateStrategy(obs)).apply(s);
  }

  %strategy Bidon() extends `Identity() {
    visit Exp {
      x -> { System.out.println("bidon : " + `x); }
      Var("x") ->  { return `plus(Var("y"),Var("z")); } 
    }
  }
  

  public static void main(String[] argv) {
    MuStrategy s = `mu(MuVar("x"),Sequence(Bidon(),All(MuVar("x"))));
    DummyController controller = new DummyController();
    s = decorateStrategy(controller, s);
    Exp n = `plus(S(Zero()),Var("x")); 
    s.apply(n);
  }
}





