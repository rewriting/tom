
package firewall.ast;


public abstract class AstAbstractType implements shared.SharedObjectWithID, tom.library.sl.Visitable, Comparable  {



  private int uniqueID;

  protected static final shared.SharedObjectFactory factory = shared.SingletonSharedObjectFactory.getInstance();

  protected static final aterm.ATermFactory atermFactory = aterm.pure.SingletonFactory.getInstance();

  public abstract aterm.ATerm toATerm();

  public abstract String symbolName();

  @Override
  public String toString() {
    java.lang.StringBuilder buffer = new java.lang.StringBuilder();
    toStringBuilder(buffer);
    return buffer.toString();
  }

  public abstract void toStringBuilder(java.lang.StringBuilder buffer);

  public abstract int compareTo(Object o);

  public abstract int compareToLPO(Object o);


  public int getUniqueIdentifier() {
    return uniqueID;
  }

  public void setUniqueIdentifier(int uniqueID) {
    this.uniqueID = uniqueID;
  }

}
