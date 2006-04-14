package strategy.hand;

public abstract class Slot implements jjtraveler.Visitable {

  public Slot accept(strategy.hand.BasicStrategy v) throws jjtraveler.VisitFailure {
    return v.visit_Slot(this);
  }
}
