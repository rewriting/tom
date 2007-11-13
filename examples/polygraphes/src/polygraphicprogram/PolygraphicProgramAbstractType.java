
package polygraphicprogram;


public abstract class PolygraphicProgramAbstractType implements shared.SharedObjectWithID, tom.library.sl.Visitable, Comparable  {



  private int uniqueID;

  protected static final shared.SharedObjectFactory factory = shared.SingletonSharedObjectFactory.getInstance();
  protected static final aterm.ATermFactory atermFactory = aterm.pure.SingletonFactory.getInstance();

  public abstract aterm.ATerm toATerm();

  public abstract String symbolName();

  @Override
  public String toString() {
    java.lang.StringBuffer buffer = new java.lang.StringBuffer();
    toStringBuffer(buffer);
    return buffer.toString();
  }

  public abstract void toStringBuffer(java.lang.StringBuffer buffer);

  public abstract int compareTo(Object o);

  public abstract int compareToLPO(Object o);

  public int getUniqueIdentifier() {
    return uniqueID;
  }

  public void setUniqueIdentifier(int uniqueID) {
    this.uniqueID = uniqueID;
  }

  abstract public PolygraphicProgramAbstractType accept(polygraphicprogram.PolygraphicProgramVisitor v) throws tom.library.sl.VisitFailure;
}
