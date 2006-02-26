public class VisitorForward implements jjtraveler.Visitor {
  protected jjtraveler.Visitor any;

  public VisitorForward(jjtraveler.Visitor v) {
    this.any = v;
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable v) throws jjtraveler.VisitFailure {
    if (v instanceof AbstractType) {
      return ((AbstractType) v).accept(this);
    } else {
      return any.visit(v);
    }
  }

  public List visit_List(List arg) throws jjtraveler.VisitFailure {
    return (List) any.visit(arg);
  }
  
	public List visit_List_Empty(Empty arg) throws jjtraveler.VisitFailure {
    return visit_List(arg);
  }

	public List visit_List_Cons(Cons arg) throws jjtraveler.VisitFailure {
    return visit_List(arg);
  }

	public List visit_List_ConsInt(ConsInt arg) throws jjtraveler.VisitFailure {
    return visit_List(arg);
  }

  public Element visit_Element(Element arg) throws jjtraveler.VisitFailure {
    return (Element) any.visit(arg);
  }
  
	public Element visit_Element_Plop(Plop arg) throws jjtraveler.VisitFailure {
    return visit_Element(arg);
  }

	public Element visit_Element_Int(Int arg) throws jjtraveler.VisitFailure {
    return visit_Element(arg);
  }
}
