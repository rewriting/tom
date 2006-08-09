
package strategy.graphterm;

public abstract class GraphTermAbstractType implements shared.SharedObjectWithID, jjtraveler.Visitable, Comparable {
  private int uniqueID;

  public abstract aterm.ATerm toATerm();

  public abstract String symbolName();

  public abstract String toString();

  public abstract int compareTo(Object o);

  public abstract int compareToLPO(Object o);

  public int getUniqueIdentifier() {
    return uniqueID;
  }

  public void setUniqueIdentifier(int uniqueID) {
    this.uniqueID = uniqueID;
  }

  abstract public GraphTermAbstractType accept(strategy.graphterm.GraphTermVisitor v) throws jjtraveler.VisitFailure;
}
