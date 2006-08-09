
package strategy.graphterm.types;        

public abstract class GraphTerm extends strategy.graphterm.GraphTermAbstractType {

  public strategy.graphterm.GraphTermAbstractType accept(strategy.graphterm.GraphTermVisitor v) throws jjtraveler.VisitFailure {
    return v.visit_GraphTerm(this);
  }


  public boolean isnode() {
    return false;
  }


  public boolean isgraph() {
    return false;
  }


  public boolean isConsrefGraphTerm() {
    return false;
  }


  public boolean isEmptyrefGraphTerm() {
    return false;
  }


  public strategy.graphterm.types.GraphTerm getTailrefGraphTerm() {
    throw new UnsupportedOperationException("This GraphTerm has no TailrefGraphTerm");
  }

  public GraphTerm setTailrefGraphTerm(strategy.graphterm.types.GraphTerm _arg) {
    throw new UnsupportedOperationException("This GraphTerm has no TailrefGraphTerm");
  }


  public String getname() {
    throw new UnsupportedOperationException("This GraphTerm has no name");
  }

  public GraphTerm setname(String _arg) {
    throw new UnsupportedOperationException("This GraphTerm has no name");
  }


  public int getHeadrefGraphTerm() {
    throw new UnsupportedOperationException("This GraphTerm has no HeadrefGraphTerm");
  }

  public GraphTerm setHeadrefGraphTerm(int _arg) {
    throw new UnsupportedOperationException("This GraphTerm has no HeadrefGraphTerm");
  }


  public strategy.graphterm.types.GraphTermList getsubterms() {
    throw new UnsupportedOperationException("This GraphTerm has no subterms");
  }

  public GraphTerm setsubterms(strategy.graphterm.types.GraphTermList _arg) {
    throw new UnsupportedOperationException("This GraphTerm has no subterms");
  }


  public static strategy.graphterm.types.GraphTerm fromTerm(aterm.ATerm trm) {
    strategy.graphterm.types.GraphTerm tmp;

    tmp = strategy.graphterm.types.graphterm.node.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = strategy.graphterm.types.graphterm.graph.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = strategy.graphterm.types.graphterm.ConsrefGraphTerm.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = strategy.graphterm.types.graphterm.EmptyrefGraphTerm.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    throw new IllegalArgumentException("This is not a GraphTerm " + trm);
  }


  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public strategy.graphterm.types.GraphTerm reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }


}
