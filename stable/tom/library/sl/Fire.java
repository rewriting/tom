package tom.library.sl;

public class Fire extends AbstractMuStrategy {
  public final static int ARG = 0;

  public Fire(jjtraveler.reflective.VisitableVisitor v) {
    initSubterm(v);
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable subject) throws jjtraveler.VisitFailure {
    return visitors[ARG].visit(subject);
  }

  protected void visit() throws jjtraveler.VisitFailure {
    System.out.println("try Fire on: " + getSubject());
    setSubject(visitors[ARG].visit(getSubject()));
    System.out.println("succed: " + getSubject());
  }

}
