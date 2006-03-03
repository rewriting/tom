package gterm;

public interface Visitor {
  public List visit_List(List arg) throws jjtraveler.VisitFailure;
  public Element visit_Element(Element arg) throws jjtraveler.VisitFailure;
}
