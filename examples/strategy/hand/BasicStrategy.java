package strategy.hand;

public class BasicStrategy implements jjtraveler.Visitor, jjtraveler.reflective.VisitableVisitor {
  protected jjtraveler.Visitor any;

  public BasicStrategy(jjtraveler.Visitor v) {
    this.any = v;
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable v) throws jjtraveler.VisitFailure {
    if (v instanceof strategy.hand.Term) {
      return ((strategy.hand.Term) v).accept(this);
    } else if (v instanceof strategy.hand.Slot) {
      return ((strategy.hand.Slot) v).accept(this);
    } else {
      return any.visit(v);
    }
  }

  public strategy.hand.Term visit_Term(strategy.hand.Term arg) throws jjtraveler.VisitFailure {
    return (strategy.hand.Term) any.visit(arg);
  }

  public strategy.hand.Slot visit_Slot(strategy.hand.Slot arg) throws jjtraveler.VisitFailure {
    return (strategy.hand.Slot) any.visit(arg);
  }

  public int getChildCount() { return 1; }

  public jjtraveler.Visitable getChildAt(int index) {
    if(index == 0) {
      return (jjtraveler.Visitable) any;
    }
    throw new IndexOutOfBoundsException();
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    if(index == 0) {
      any = ((jjtraveler.Visitor) v);
      return this;
    }
    throw new IndexOutOfBoundsException();
  }
}
