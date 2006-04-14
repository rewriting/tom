package strategy.hand;

public abstract class Term implements jjtraveler.Visitable {

  public Term accept(strategy.hand.BasicStrategy v) throws jjtraveler.VisitFailure {
    return v.visit_Term(this);
  }
}
