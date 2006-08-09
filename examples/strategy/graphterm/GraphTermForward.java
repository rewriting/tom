
package strategy.graphterm;

public class GraphTermForward implements GraphTermVisitor, jjtraveler.Visitor {
  protected jjtraveler.Visitor any;

  public GraphTermForward(jjtraveler.Visitor v) {
    this.any = v;
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable v) throws jjtraveler.VisitFailure {
    if (v instanceof strategy.graphterm.GraphTermAbstractType) {
      return ((strategy.graphterm.GraphTermAbstractType) v).accept(this);
    }

    else {
      return any.visit(v);
    }
  }


  public strategy.graphterm.types.GraphTerm visit_GraphTerm(strategy.graphterm.types.GraphTerm arg) throws jjtraveler.VisitFailure {
    return (strategy.graphterm.types.GraphTerm) any.visit(arg);
  }

  public strategy.graphterm.types.GraphTermList visit_GraphTermList(strategy.graphterm.types.GraphTermList arg) throws jjtraveler.VisitFailure {
    return (strategy.graphterm.types.GraphTermList) any.visit(arg);
  }


}
