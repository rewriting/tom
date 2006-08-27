package tom.library.strategy.mutraveler.reflective;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

public class StrategyVisitorFwd extends AbstractMuStrategy {
  public final static int ARG = 0;

  public StrategyVisitorFwd(VisitableVisitor v) {
    initSubterm(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    if (any instanceof MuStrategy) {
      return ((MuStrategy) any).accept(this);
    } else {
      return getArgument(ARG).visit(any);
    }
  }

  public MuStrategy visit_Strategy(MuStrategy any) throws VisitFailure {
    return (MuStrategy) getArgument(ARG).visit(any);
  }
}

