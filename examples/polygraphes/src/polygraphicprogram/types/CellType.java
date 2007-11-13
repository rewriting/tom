
package polygraphicprogram.types;        

//import polygraphicprogram.types.celltype.*;
//import polygraphicprogram.*;

public abstract class CellType extends polygraphicprogram.PolygraphicProgramAbstractType {


  @Override
  public polygraphicprogram.PolygraphicProgramAbstractType accept(polygraphicprogram.PolygraphicProgramVisitor v) throws tom.library.sl.VisitFailure {
    return v.visit_CellType(this);
  }

  public boolean isConstructor() {
    return false;
  }


  public boolean isFunction() {
    return false;
  }


  public static polygraphicprogram.types.CellType fromTerm(aterm.ATerm trm) {
    polygraphicprogram.types.CellType tmp;

    tmp = polygraphicprogram.types.celltype.Constructor.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = polygraphicprogram.types.celltype.Function.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    throw new IllegalArgumentException("This is not a CellType " + trm);
  }

  public static polygraphicprogram.types.CellType fromString(String s) {
    return fromTerm(atermFactory.parse(s));
  }

  public static polygraphicprogram.types.CellType fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream));
  }


  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public polygraphicprogram.types.CellType reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  /**
   * Collection
   */
  /*
  public boolean add(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public void clear() {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean containsAll(java.util.Collection c) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean contains(Object o) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean equals(Object o) { return this == o; }

  public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return false; }

  public java.util.Iterator iterator() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean remove(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean removeAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean retainAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public int size() { return length(); }

  public Object[] toArray() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public Object[] toArray(Object[] a) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
  */
  
}
