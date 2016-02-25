
package sa.rule;


/**
  * This class provides a skeletal implementation of <i>terms</i>
  * When implementing the interface <tt>shared.SharedObjectWithID</tt>,
  * the objects are immutable and can be compared in constant time, using
  * <tt>==</tt>.
  */

public abstract class RuleAbstractType implements shared.SharedObjectWithID, tom.library.sl.Visitable, Comparable  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected RuleAbstractType() {}



  protected static final shared.SharedObjectFactory factory = shared.SingletonSharedObjectFactory.getInstance();

  /**
    * Returns a string representation of the top symbol of this term.
    *
    * @return a string representation of the top symbol of this term.
    */
  public abstract String symbolName();

  /**
    * Returns a string representation of this term.
    *
    * @return a string representation of this term.
    */
  @Override
  public String toString() {
    java.lang.StringBuilder buffer = new java.lang.StringBuilder();
    toStringBuilder(buffer);
    return buffer.toString();
  }

  /**
    * Appends a string representation of this term to the buffer given as argument.
    *
    * @param buffer the buffer to which a string represention of this term is appended.
    */
  public abstract void toStringBuilder(java.lang.StringBuilder buffer);

  /**
    * Compares two terms. This functions implements a total order.
    *
    * @param o object to which this term is compared
    * @return a negative integer, zero, or a positive integer as this
    *         term is less than, equal to, or greater than the argument
    */
  public abstract int compareTo(Object o);

  /**
    * Compares two terms. This functions implements a total lexicographic path ordering.
    *
    * @param o object to which this term is compared
    * @return a negative integer, zero, or a positive integer as this
    *         term is less than, equal to, or greater than the argument
    */
  public abstract int compareToLPO(Object o);

  /**
    * from SharedObjectWithID
    */
  private int uniqueID;

  /**
    * Returns an integer that identifies this term in a unique way.
    * <tt>t1.getUniqueIdentifier()==t2.getUniqueIdentifier()</tt> iff <tt>t1==t2</tt>.
    *
    * @return an integer that identifies this term in a unique way.
    */
  public int getUniqueIdentifier() {
    return uniqueID;
  }

  /**
    * Modifies the integer that identifies this term in a unique way.
    *
    * @param uniqueID integer that identifies this term in a unique way.
    */
  public void setUniqueIdentifier(int uniqueID) {
    this.uniqueID = uniqueID;
  }
}
