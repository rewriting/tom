package ted;

import tom.library.strategy.mutraveler.*;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import tom.library.strategy.mutraveler.MuStrategy;

import ted.stratdebugger.entier.types.*;


public class StratDebugger {

  %gom {
    module Entier 
    imports String
    abstract syntax

    Exp = Zero()
        | S(n:Exp)
        | plus(n:Exp,m:Exp)
  }

  %include{ mustrategy.tom }
//  %include{ ref.tom }
//  %include{ visitable.tom }

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

  %strategy RS() extends `Identity() {
    visit Exp {
      plus(S(n),m) -> { return `S(plus(n,m)); }
      plus(Zero(),n) -> { return `n; }
    }
  }
 

  public static Visitable applyDebug(Visitable subject, MuStrategy strat) {
    DummyObserver observer = new DummyObserver();
    strat = decorateStrategy(observer, strat);
    return strat.apply(subject);
  }

  public static Visitable applyGraphicalDebug(Visitable subject, MuStrategy strat) {
    GraphicalObserver observer = new GraphicalObserver(subject,strat);
    strat = decorateStrategy(observer, strat);
    return strat.apply(subject);
  }


  public static void main(String[] argv) {
    MuStrategy s = `InnermostId(RS());
    Exp n = `plus(S(Zero()),S(Zero()));

    n = (Exp) applyGraphicalDebug(n,s);
    //n = (Exp) applyDebug(n,s);
    System.out.println("final result = " + n);
  }
}





