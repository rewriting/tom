package tom.library.strategy.mutraveler.reflective;

import tom.library.strategy.mutraveler.Position;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
/**
 * A visitor that it iself visitable with a VisitorVisitor needs
 * to implement the VisitableVisitor interface. The visitor's arguments
 * should play the role of children.
 */

public abstract class AbstractVisitableVisitor implements VisitableVisitor {
  protected VisitableVisitor[] visitors;
  private Position position;

  protected void setPosition(Position pos) {
    this.position = pos;
  }

  public Position getPosition() {
    return position;
  }

  protected void initPosition(Position pos) {
    setPosition(pos);
    for(int i=0 ; i<getChildCount() ; i++) {
      ((AbstractVisitableVisitor)getChildAt(i)).initPosition(pos); 
    }
  }

  public VisitableVisitor init() {
    Position pos = new Position();
    initPosition(pos);
    return this;
  }
  
  protected void initSubterm() {
    visitors = new VisitableVisitor[] {};
  }
  protected void initSubterm(VisitableVisitor v1) {
    visitors = new VisitableVisitor[] {v1};
  }
  protected void initSubterm(VisitableVisitor v1, VisitableVisitor v2) {
    visitors = new VisitableVisitor[] {v1,v2};
  }
  protected void initSubterm(VisitableVisitor v1, VisitableVisitor v2, VisitableVisitor v3) {
    visitors = new VisitableVisitor[] {v1,v2,v3};
  }

  public VisitableVisitor getArgument(int i) {
    return visitors[i];
  }
  public void setArgument(int i, VisitableVisitor child) {
    visitors[i]= child;
  }

  public int getChildCount() {
    return visitors.length;
  }

  public Visitable getChildAt(int i) {
      return visitors[i];
  }
  
  public Visitable setChildAt(int i, Visitable child) {
    visitors[i]= (VisitableVisitor) child;
    return this;
  }

}
