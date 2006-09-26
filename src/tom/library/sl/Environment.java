package tom.library.sl;

/**
 * Object that represents an environment of a strategy
 *  the position where the strategy is applied
 *  a pointer to subterm
 *  the root is stored in the first cell
 */

public class Environment {
  private static final int DEFAULT_LENGTH = 8;
  /*
   * number of elements in the arrays
   * the current position is int omega[size-1]
   * */
  protected int size; 
  protected int[] omega;
  protected jjtraveler.Visitable[] subterm;

  public Environment() {
    this(DEFAULT_LENGTH);
  }

  private Environment(int length) {
    omega = new int[length+1];
    subterm = new jjtraveler.Visitable[length+1];
    size = 1; // root is in subterm[0]
    omega[0]=0; // the first cell is not used
  }
  
  private void ensureLength(int minLength) {
    int current = omega.length;
    if (minLength > current) {
      int max = Math.max(current * 2, minLength);
      int[] newOmega = new int[max];
      jjtraveler.Visitable[] newSubterm = new jjtraveler.Visitable[max];
      System.arraycopy(omega, 0, newOmega, 0, size);
      System.arraycopy(subterm, 0, newSubterm, 0, size);
      omega = newOmega;
      subterm = newSubterm;
    }
  }

  public Object clone() {
    Environment clone = new Environment(omega.length);
    clone.size = size;
    System.arraycopy(omega, 0, clone.omega, 0, size);
    System.arraycopy(subterm, 0, clone.subterm, 0, size);
    return clone;
  }

  /**
   * Tests if two environments are equals
   */
  public boolean equals(Object o) {
    if (o instanceof Environment) {
      Environment p = (Environment)o;
      /* we need to check only the meaningful part of the omega and subterm arrays */
      if (size==p.size) {
        for(int i=0; i<size; i++) {
          if (omega[i]!=p.omega[i] || subterm[i]!=p.subterm[i]) {
            return false;
          }
        }
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public int hashCode() {
    /* Hash only the interesting part of the array */
    int[] hashedOmega = new int[size];
    jjtraveler.Visitable[] hashedSubterm = new jjtraveler.Visitable[size];
    System.arraycopy(omega,0,hashedOmega,0,size);
    System.arraycopy(subterm,0,hashedSubterm,0,size);
    return size * hashedOmega.hashCode() * hashedSubterm.hashCode();
  }

  /**
   * Tests is prefix
   */
  public boolean isPrefix(Environment p) {
    if(p.size < size) {
      return false;
    }
    for(int i=0 ; i<size ; i++) {
      if(omega[i]!=p.omega[i] || subterm[i]!=p.subterm[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * get the current root
   * @return the current root
   */
  protected jjtraveler.Visitable getRoot() {
    return subterm[0];
  }

  /**
   * set the current root
   */
  protected void setRoot(jjtraveler.Visitable root) {
    this.subterm[0] = root;
  }

  /**
   * get the term that corresponds to the current position
   * @return the current term
   */
  protected jjtraveler.Visitable getSubject() {
    return subterm[size-1];
  }

  /**
   * set the current term
   */
  protected void setSubject(jjtraveler.Visitable root) {
    this.subterm[size-1] = root;
  }
  /**
   * get the current sub-position
   * @return the current sub-position
   */
  protected int getSubOmega() {
    return omega[size-1];
  }

  /**
   * Get the depth of the position in the tree
   * @return depth on the position
   */
  public int depth() {
    return size-1;
  }

  /**
   * remove the last sub-position
   * Up and down are made public to allow to write compiled strategies by hand
   * This may be a BAD idea
   */
  public void up() {
    //System.out.println("before up: " + this);
    int childIndex = getSubOmega()-1;
    jjtraveler.Visitable child = getSubject();
    size--;
    setSubject(getSubject().setChildAt(childIndex,child));
    //System.out.println("after up: " + this);
  }

  /**
   * add a sub-position n
   *
   * @param n sub-position number. 1 is the first possible sub-position
   */
  public void down(int n) {
    //System.out.println("before down: " + this);
    if(n>0) {
      if (size == omega.length) {
        ensureLength(size+1);
      }
      omega[size] = n;
      subterm[size] = getSubject().getChildAt(n-1);
      size++;
    }
    //System.out.println("after down: " + this);
  }
  /**
    * Returns a <code>String</code> object representing the position.
    * The string representation consists of a list of elementary positions
    *
    * @return a string representation of this position
    */
  public String toString() {
    StringBuffer r = new StringBuffer("[");
    for(int i=0 ; i<size ; i++) {
      r.append(omega[i]);
      if(i<size-1) {
        r.append(", ");
      }
    }
    r.append("]");
    
    r.append("\n[");

    for(int i=0 ; i<size ; i++) {
      r.append(subterm[i]);
      if(i<size-1) {
        r.append(", ");
      }
    }
    r.append("]");
    return r.toString();
  }
}
