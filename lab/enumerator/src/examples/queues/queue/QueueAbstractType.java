
package examples.queues.queue;


/**
  * This class provides a skeletal implementation of <i>terms</i>
  * When implementing the interface <tt>shared.SharedObjectWithID</tt>,
  * the objects are immutable and can be compared in constant time, using
  * <tt>==</tt>.
  */

public abstract class QueueAbstractType implements shared.SharedObjectWithID, tom.library.sl.Visitable, Comparable  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected QueueAbstractType() {}



  protected static final shared.SharedObjectFactory factory = shared.SingletonSharedObjectFactory.getInstance();

  protected static final aterm.ATermFactory atermFactory = aterm.pure.SingletonFactory.getInstance();

  /**
    * Returns an ATerm representation of this term.
    *
    * @return an ATerm representation of this term.
    */
  public abstract aterm.ATerm toATerm();

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
    * Converts an ATerm into a string
    *
    * @param at ATerm to convert
    * @param atConv converter applied to the ATerm before coersion
    * @return a string
    */
  protected static String convertATermToString(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) at;
      aterm.AFun fun = appl.getAFun();
      if (fun.isQuoted() && fun.getArity() == 0 ) { // this test ensures that we have a String and not an "exotic" ATerm like this one : "f"(a)
        return fun.getName();
      }
    }
    throw new RuntimeException("Not a String : " + at);
  }

  /**
    * Converts an ATerm into an int
    *
    * @param at ATerm to convert
    * @param atConv converter applied to the ATerm before coersion
    * @return an int
    */
  protected static int convertATermToInt(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermInt) {
      aterm.ATermInt atint = (aterm.ATermInt) at;
      return atint.getInt();
    }
    throw new RuntimeException("Not an Int : " + at);
  }

  /**
    * Converts an ATerm into a float
    *
    * @param at ATerm to convert
    * @param atConv converter applied to the ATerm before coersion
    * @return a float
    */
  protected static float convertATermToFloat(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermReal) {
      aterm.ATermReal atfloat = (aterm.ATermReal) at;
      return (float) atfloat.getReal();
    }
    throw new RuntimeException("Not a Float : " + at);
  }

  /**
    * Converts an ATerm into a double
    *
    * @param at ATerm to convert
    * @param atConv converter applied to the ATerm before coersion
    * @return a double
    */
  protected static double convertATermToDouble(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermReal) {
      aterm.ATermReal atdouble = (aterm.ATermReal) at;
      return atdouble.getReal();
    }
    throw new RuntimeException("Not a Double : " + at);
  }

  /**
    * Converts an ATerm into a long
    *
    * @param at ATerm to convert
    * @param atConv converter applied to the ATerm before coersion
    * @return a long
    */
  protected static long convertATermToLong(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermLong) {
      aterm.ATermLong atlong = (aterm.ATermLong) at;
      return atlong.getLong();
    }
    throw new RuntimeException("Not a Long : " + at);
  }

  /**
    * Converts an ATerm into a boolean
    *
    * @param at ATerm to convert
    * @param atConv converter applied to the ATerm before coersion
    * @return a boolean
    */
  protected static boolean convertATermToBoolean(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermInt) {
      aterm.ATermInt atint = (aterm.ATermInt) at;
      return (atint.getInt()==0?false:true);
    }
    throw new RuntimeException("Not a Boolean : " + at);
  }

  /**
    * Converts an ATerm into a char
    *
    * @param at ATerm to convert
    * @param atConv converter applied to the ATerm before coersion
    * @return a char
    */
  protected static char convertATermToChar(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermInt) {
      int atint = ((aterm.ATermInt)at).getInt();
      atint = atint + (int)'0';
      return (char) atint;
    }
    throw new RuntimeException("Not a Char : " + at);
  }


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
