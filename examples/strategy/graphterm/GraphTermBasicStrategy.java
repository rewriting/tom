
package strategy.graphterm;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.MuStrategy;
    
  public class GraphTermBasicStrategy extends GraphTermForward implements MuStrategy {
  private Position position;

  public void setPosition(Position pos) {
    this.position = pos;
  }

  public Position getPosition() {
    if(hasPosition()) {
      return (Position) position.clone();
    } else {
      throw new RuntimeException("position not initialized");
    }
  }

  public boolean hasPosition() {
    return position!=null;
  }

    
  public int getChildCount() {
    return 1;
  }
    
  public jjtraveler.Visitable getChildAt(int i) {
    switch (i) {
      case 0: return (jjtraveler.Visitable) any;
      default: throw new IndexOutOfBoundsException();
    }
  }
    
  public jjtraveler.Visitable setChildAt(int i, jjtraveler.Visitable child) {
    switch (i) {
      case 0: any = (jjtraveler.reflective.VisitableVisitor) child; return this;
      default: throw new IndexOutOfBoundsException();
    }
  }

  /*
   * Apply the strategy, and returns the subject in case of VisitFailure
   */
  public jjtraveler.Visitable apply(jjtraveler.Visitable any) {
    try {
      return tom.library.strategy.mutraveler.MuTraveler.init(this).visit(any);
    } catch (jjtraveler.VisitFailure f) {
      return any;
    }
  }

  public tom.library.strategy.mutraveler.MuStrategy accept(tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }
    
  public GraphTermBasicStrategy(jjtraveler.reflective.VisitableVisitor any) {
    super(any);
  }
}
