
package sa.rule.types.fieldlist;

import java.util.*;


public abstract class ConcField extends sa.rule.types.FieldList implements java.util.Collection<sa.rule.types.Field>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.fieldlist.ConsConcField) {
      sa.rule.types.FieldList tl = this.getTailConcField();
      if (tl instanceof ConcField) {
        return 1+((ConcField)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.FieldList fromArray(sa.rule.types.Field[] array) {
    sa.rule.types.FieldList res = sa.rule.types.fieldlist.EmptyConcField.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.fieldlist.ConsConcField.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.FieldList reverse() {
    if(this instanceof sa.rule.types.fieldlist.ConsConcField) {
      sa.rule.types.FieldList cur = this;
      sa.rule.types.FieldList rev = sa.rule.types.fieldlist.EmptyConcField.make();
      while(cur instanceof sa.rule.types.fieldlist.ConsConcField) {
        rev = sa.rule.types.fieldlist.ConsConcField.make(cur.getHeadConcField(),rev);
        cur = cur.getTailConcField();
      }

      return rev;
    } else {
      return this;
    }
  }

  /**
   * Appends an element
   *
   * @param element element which has to be added
   * @return the term with the added element
   */
  public sa.rule.types.FieldList append(sa.rule.types.Field element) {
    if(this instanceof sa.rule.types.fieldlist.ConsConcField) {
      sa.rule.types.FieldList tl = this.getTailConcField();
      if (tl instanceof ConcField) {
        return sa.rule.types.fieldlist.ConsConcField.make(this.getHeadConcField(),((ConcField)tl).append(element));
      } else {

        return sa.rule.types.fieldlist.ConsConcField.make(this.getHeadConcField(),sa.rule.types.fieldlist.ConsConcField.make(element,tl));

      }
    } else {
      return sa.rule.types.fieldlist.ConsConcField.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ConcField(");
    if(this instanceof sa.rule.types.fieldlist.ConsConcField) {
      sa.rule.types.FieldList cur = this;
      while(cur instanceof sa.rule.types.fieldlist.ConsConcField) {
        sa.rule.types.Field elem = cur.getHeadConcField();
        cur = cur.getTailConcField();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.fieldlist.ConsConcField) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.fieldlist.EmptyConcField)) {
        buffer.append(",");
        cur.toStringBuilder(buffer);
      }
    }
    buffer.append(")");
  }

  /*
   * Checks if the Collection contains all elements of the parameter Collection
   *
   * @param c the Collection of elements to check
   * @return true if the Collection contains all elements of the parameter, otherwise false
   */
  public boolean containsAll(java.util.Collection c) {
    java.util.Iterator it = c.iterator();
    while(it.hasNext()) {
      if(!this.contains(it.next())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if sa.rule.types.FieldList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.FieldList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.FieldList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.fieldlist.ConsConcField) {
      while(cur instanceof sa.rule.types.fieldlist.ConsConcField) {
        if( o.equals(cur.getHeadConcField()) ) {
          return true;
        }
        cur = cur.getTailConcField();
      }
      if(!(cur instanceof sa.rule.types.fieldlist.EmptyConcField)) {
        if( o.equals(cur) ) {
          return true;
        }
      }
    }
    return false;
  }

  //public boolean equals(Object o) { return this == o; }

  //public int hashCode() { return hashCode(); }

  /**
   * Checks the emptiness
   *
   * @return true if empty, otherwise false
   */
  public boolean isEmpty() { return isEmptyConcField() ; }

  public java.util.Iterator<sa.rule.types.Field> iterator() {
    return new java.util.Iterator<sa.rule.types.Field>() {
      sa.rule.types.FieldList list = ConcField.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyConcField();
      }

      public sa.rule.types.Field next() {
        if(list.isEmptyConcField()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsConcField()) {
          sa.rule.types.Field head = list.getHeadConcField();
          list = list.getTailConcField();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (sa.rule.types.Field)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(sa.rule.types.Field o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends sa.rule.types.Field> c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean remove(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public void clear() {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean removeAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean retainAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  /**
   * Returns the size of the collection
   *
   * @return the size of the collection
   */
  public int size() { return length(); }

  /**
   * Returns an array containing the elements of the collection
   *
   * @return an array of elements
   */

  public Object[] toArray() {
    int size = this.length();
    Object[] array = new Object[size];
    int i=0;
    if(this instanceof sa.rule.types.fieldlist.ConsConcField) {
      sa.rule.types.FieldList cur = this;
      while(cur instanceof sa.rule.types.fieldlist.ConsConcField) {
        sa.rule.types.Field elem = cur.getHeadConcField();
        array[i] = elem;
        cur = cur.getTailConcField();
        i++;
      }
      if(!(cur instanceof sa.rule.types.fieldlist.EmptyConcField)) {
        array[i] = cur;
      }
    }
    return array;
  }


  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] array) {
    int size = this.length();
    if (array.length < size) {
        ArrayList<T> a = new ArrayList<T>();
        for(int i = 0; i<size; i++) {
          a.add(null);
        }
        array = a.toArray(array);
    } else if (array.length > size) {
      array[size] = null;
    }
    int i=0;
    if(this instanceof sa.rule.types.fieldlist.ConsConcField) {
      sa.rule.types.FieldList cur = this;
      while(cur instanceof sa.rule.types.fieldlist.ConsConcField) {
        sa.rule.types.Field elem = cur.getHeadConcField();
        array[i] = (T)elem;
        cur = cur.getTailConcField();
        i++;
      }
      if(!(cur instanceof sa.rule.types.fieldlist.EmptyConcField)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.Field> getCollection() {
    return new CollectionConcField(this);
  }

  public java.util.Collection<sa.rule.types.Field> getCollectionConcField() {
    return new CollectionConcField(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionConcField implements java.util.Collection<sa.rule.types.Field> {
    private ConcField list;

    public ConcField getFieldList() {
      return list;
    }

    public CollectionConcField(ConcField list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends sa.rule.types.Field> c) {
    boolean modified = false;
    java.util.Iterator<? extends sa.rule.types.Field> it = c.iterator();
    while(it.hasNext()) {
      modified = modified || add(it.next());
    }
    return modified;
  }

  /**
   * Checks if the collection contains an element
   *
   * @param o element whose presence has to be checked
   * @return true if the element is found, otherwise false
   */
  public boolean contains(Object o) {
    return getFieldList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getFieldList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getFieldList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getFieldList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.Field> iterator() {
    return getFieldList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getFieldList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getFieldList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getFieldList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getFieldList().length();
    if (array.length < size) {
      array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
    } else if (array.length > size) {
      array[size] = null;
    }
    int i=0;
    for(java.util.Iterator it=iterator() ; it.hasNext() ; i++) {
        array[i] = (T)it.next();
    }
    return array;
  }
*/


    /**
     * Collection
     */

    /**
     * Adds an element to the collection
     *
     * @param o element to add to the collection
     * @return true if it is a success
     */
    public boolean add(sa.rule.types.Field o) {
      list = (ConcField)sa.rule.types.fieldlist.ConsConcField.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ConcField)sa.rule.types.fieldlist.EmptyConcField.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyConcField();
    }

    public boolean remove(Object o) {
      throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean removeAll(java.util.Collection<?> c) {
      throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean retainAll(java.util.Collection<?> c) {
      throw new UnsupportedOperationException("Not yet implemented");
    }

  }


}
