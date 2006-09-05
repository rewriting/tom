package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd;
/**
 * A visitor that it iself visitable with a VisitorVisitor needs
 * to implement the MuStrategy interface. The visitor's arguments
 * should play the role of children.
 */

public abstract class AbstractMuStrategy implements MuStrategy {
  protected jjtraveler.reflective.VisitableVisitor[] visitors;
  private Position position;

  public void setPosition(Position pos) {
    this.position = pos;
  }

  public Position getPosition() {
    if(position!=null) {
      return position;
    } else {
      throw new RuntimeException("position not initialized");
    }
  }

  public final boolean hasPosition() {
    return position!=null;
  }

  protected void initSubterm() {
    visitors = new jjtraveler.reflective.VisitableVisitor[] {};
  }
  protected void initSubterm(jjtraveler.reflective.VisitableVisitor v1) {
    visitors = new jjtraveler.reflective.VisitableVisitor[] {v1};
  }
  protected void initSubterm(jjtraveler.reflective.VisitableVisitor v1, jjtraveler.reflective.VisitableVisitor v2) {
    visitors = new jjtraveler.reflective.VisitableVisitor[] {v1,v2};
  }
  protected void initSubterm(jjtraveler.reflective.VisitableVisitor v1, jjtraveler.reflective.VisitableVisitor v2, jjtraveler.reflective.VisitableVisitor v3) {
    visitors = new jjtraveler.reflective.VisitableVisitor[] {v1,v2,v3};
  }
  protected void initSubterm(jjtraveler.reflective.VisitableVisitor[] v) {
    visitors = v;
  }

  public final jjtraveler.reflective.VisitableVisitor getArgument(int i) {
    return visitors[i];
  }

  public void setArgument(int i, jjtraveler.reflective.VisitableVisitor child) {
    visitors[i]= child;
  }

  public int getChildCount() {
    return visitors.length;
  }

  public jjtraveler.Visitable getChildAt(int i) {
      return visitors[i];
  }
  
  public jjtraveler.Visitable setChildAt(int i, jjtraveler.Visitable child) {
    visitors[i]= (jjtraveler.reflective.VisitableVisitor) child;
    return this;
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

  public MuStrategy accept(StrategyVisitorFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }
}

