package tom.library.strategy.mutraveler;
import jjtraveler.reflective.VisitableVisitor;

public class TravelerFactory {
  %include { mutraveler.tom }

  public VisitableVisitor mu(VisitableVisitor var, VisitableVisitor v) {
    return tom.library.strategy.mutraveler.MuVar.mu(var,v);
  }

  public VisitableVisitor x() {
    return `MuVar("x");
  }

  public VisitableVisitor Try(VisitableVisitor v) {
    return `Choice(v,Identity);
  }
  
  public VisitableVisitor BottomUp(VisitableVisitor v) {
    return `mu(x(),Sequence(All(x()),v));
  }

  public VisitableVisitor OnceBottomUp(VisitableVisitor v) {
    return `mu(x(),Choice(One(x()),v));
  }

  public VisitableVisitor Innermost(VisitableVisitor v) {
    return `mu(x(),Sequence(All(x()),Choice(Sequence(v,x()),Identity)));
  }

  public VisitableVisitor InnermostId(VisitableVisitor v) {
    return `mu(x(),Sequence(All(x()),ChoiceOnId(v,x())));
  }

  public VisitableVisitor Repeat(VisitableVisitor v) {
    return `mu(x(),Choice(Sequence(v,x()),Identity()));
  }

  
  /*
  public VisitableVisitor Pchoice(VisitableVisitor v) {
    return `mu(x(),Choice(Sequence(v,x()),Identity()));
  }
  */

}
