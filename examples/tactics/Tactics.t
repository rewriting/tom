package tactics;

import tactics.trees.*;
import tactics.trees.types.*;
import tom.library.strategy.mutraveler.*;

// debugger
import ted.*;
import jjtraveler.VisitFailure;

public class Tactics {

  %include {trees/Trees.tom}
  %include {trees/_Trees.tom}
  %include {mustrategy.tom}


  %op Strategy Apply(s:Strategy) {
    make(s) { `Choice(Sequence(Is_Consnlist(),s), Choice(_node(Identity(),s), _open(Identity()) )) }
  }

  %op Strategy TriesAll(s1:Strategy,s2:Strategy) {
    make(s1,s2) { `mu(MuVar("x"),  Choice( Apply(_Consnlist(s1,Choice(s2,Identity()))),
                                   Choice( Apply(_Consnlist(Identity(),MuVar("x"))),
                                           Apply(_Emptynlist())
                                         ))
                     )
        
                }
  }

  %op Strategy Brackets(s1:Strategy, s2:Strategy) {
    make(s1,s2) { `mu(MuVar("y"), TriesAll(Sequence(MuVar("y"),s1),s2) )  }
  }

  %strategy Rule1() extends `Identity() {
    visit Tree {
      open("a") -> { return `open("b"); }
      open(x) -> { throw new VisitFailure(); } 
    }
  }

  %strategy Rule2() extends `Identity() {
    visit Tree {
      open("a") -> { return `open("c"); }
      open(x) -> { throw new VisitFailure(); } 
    }
  }

  %strategy Rule3() extends `Identity() {
    visit Tree {
      open("a") -> { return `open("d"); }
      open(x) -> { throw new VisitFailure(); } 
    }
  }

  public static void main(String[] argv) {
    Tree tree = `node("root",nlist(open("a"),node("rule",nlist(open("z"),open("a"),open("a")))));
    MuStrategy tact = (MuStrategy) `Brackets(Rule1(), Brackets(Rule2(), Brackets(Rule3(), Identity())));

    System.out.println(tree);
    tree = (Tree) tact.apply(tree);
    //tree = (Tree) StratDebugger.applyGraphicalDebug(tree,tact);
    //tree = (Tree) StratDebugger.applyDebug(tree,tact);
    System.out.println(tree);

    return;
  }
}
