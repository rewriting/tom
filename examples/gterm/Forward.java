public class Forward implements Visitor, jjtraveler.Visitor {
  protected jjtraveler.Visitor any;

  public Forward(jjtraveler.Visitor v) {
    this.any = v;
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable v) throws jjtraveler.VisitFailure {
    if (v instanceof Accept) {
      return ((Accept) v).accept(this);
    } else {
      return any.visit(v);
    }
  }

  public List visit_List(List arg) throws jjtraveler.VisitFailure {
    return (List) any.visit(arg);
  }
  
  public Element visit_Element(Element arg) throws jjtraveler.VisitFailure {
    return (Element) any.visit(arg);
  }
}
