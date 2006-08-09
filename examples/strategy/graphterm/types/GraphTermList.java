
package strategy.graphterm.types;        

public abstract class GraphTermList extends strategy.graphterm.GraphTermAbstractType {

  public strategy.graphterm.GraphTermAbstractType accept(strategy.graphterm.GraphTermVisitor v) throws jjtraveler.VisitFailure {
    return v.visit_GraphTermList(this);
  }


  public boolean isConsconc() {
    return false;
  }


  public boolean isEmptyconc() {
    return false;
  }


  public strategy.graphterm.types.GraphTerm getHeadconc() {
    throw new UnsupportedOperationException("This GraphTermList has no Headconc");
  }

  public GraphTermList setHeadconc(strategy.graphterm.types.GraphTerm _arg) {
    throw new UnsupportedOperationException("This GraphTermList has no Headconc");
  }


  public strategy.graphterm.types.GraphTermList getTailconc() {
    throw new UnsupportedOperationException("This GraphTermList has no Tailconc");
  }

  public GraphTermList setTailconc(strategy.graphterm.types.GraphTermList _arg) {
    throw new UnsupportedOperationException("This GraphTermList has no Tailconc");
  }


  public static strategy.graphterm.types.GraphTermList fromTerm(aterm.ATerm trm) {
    strategy.graphterm.types.GraphTermList tmp;

    tmp = strategy.graphterm.types.graphtermlist.Consconc.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = strategy.graphterm.types.graphtermlist.Emptyconc.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    throw new IllegalArgumentException("This is not a GraphTermList " + trm);
  }


  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public strategy.graphterm.types.GraphTermList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }


}
