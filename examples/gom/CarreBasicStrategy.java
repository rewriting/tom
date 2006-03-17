package gom;

public class CarreBasicStrategy extends gom.rond.RondBasicStrategy {

  public CarreBasicStrategy(jjtraveler.reflective.VisitableVisitor v) {
    super(v);
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable v) throws jjtraveler.VisitFailure {
    if (v instanceof Carre) {
      return this.visit_Carre((Carre)v);
    } else {
      return super.visit(v);
    }
  }


  public Carre visit_Carre(Carre arg) throws jjtraveler.VisitFailure {
    return (Carre) any.visit(arg);
  }

}
