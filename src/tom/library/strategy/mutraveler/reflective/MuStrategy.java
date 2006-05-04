package tom.library.strategy.mutraveler.reflective;

import jjtraveler.reflective.VisitableVisitor;
import tom.library.strategy.mutraveler.Position;

public interface MuStrategy extends VisitableVisitor {

  public void setPosition(Position pos);

  public Position getPosition();

  public boolean hasPosition();
}
