package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.Position;

public interface MuStrategy extends jjtraveler.reflective.VisitableVisitor {

  public void setPosition(Position pos);

  public Position getPosition();

  public boolean hasPosition();

  public jjtraveler.Visitable apply(jjtraveler.Visitable any);
}
