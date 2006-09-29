package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd;

public interface MuStrategy extends jjtraveler.reflective.VisitableVisitor {

  public void setPosition(Position p);

  public Position getPosition();

  public boolean hasPosition();

  public jjtraveler.Visitable apply(jjtraveler.Visitable any);

  public MuStrategy accept(StrategyVisitorFwd v) throws jjtraveler.VisitFailure;
}

