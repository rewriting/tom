package tom.library.strategy.mutraveler.reflective;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
/**
 * A visitor that it iself visitable with a VisitorVisitor needs
 * to implement the VisitableVisitor interface. The visitor's arguments
 * should play the role of children.
 */

public abstract class AbstractVisitableVisitor implements VisitableVisitor {
  protected VisitableVisitor[] visitors;

  public void init() {
    visitors = new VisitableVisitor[] {};
  }
  public void init(VisitableVisitor v1) {
    visitors = new VisitableVisitor[] {v1};
  }
  public void init(VisitableVisitor v1, VisitableVisitor v2) {
    visitors = new VisitableVisitor[] {v1,v2};
  }
  public void init(VisitableVisitor v1, VisitableVisitor v2, VisitableVisitor v3) {
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
