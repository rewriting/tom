
package sa.rule.types.gomtypelist;

import java.util.*;


public abstract class ConcGomType extends sa.rule.types.GomTypeList implements java.util.Collection<sa.rule.types.GomType>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
      sa.rule.types.GomTypeList tl = this.getTailConcGomType();
      if (tl instanceof ConcGomType) {
        return 1+((ConcGomType)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.GomTypeList fromArray(sa.rule.types.GomType[] array) {
    sa.rule.types.GomTypeList res = sa.rule.types.gomtypelist.EmptyConcGomType.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.gomtypelist.ConsConcGomType.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.GomTypeList reverse() {
    if(this instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
      sa.rule.types.GomTypeList cur = this;
      sa.rule.types.GomTypeList rev = sa.rule.types.gomtypelist.EmptyConcGomType.make();
      while(cur instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
        rev = sa.rule.types.gomtypelist.ConsConcGomType.make(cur.getHeadConcGomType(),rev);
        cur = cur.getTailConcGomType();
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
  public sa.rule.types.GomTypeList append(sa.rule.types.GomType element) {
    if(this instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
      sa.rule.types.GomTypeList tl = this.getTailConcGomType();
      if (tl instanceof ConcGomType) {
        return sa.rule.types.gomtypelist.ConsConcGomType.make(this.getHeadConcGomType(),((ConcGomType)tl).append(element));
      } else {

        return sa.rule.types.gomtypelist.ConsConcGomType.make(this.getHeadConcGomType(),sa.rule.types.gomtypelist.ConsConcGomType.make(element,tl));

      }
    } else {
      return sa.rule.types.gomtypelist.ConsConcGomType.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ConcGomType(");
    if(this instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
      sa.rule.types.GomTypeList cur = this;
      while(cur instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
        sa.rule.types.GomType elem = cur.getHeadConcGomType();
        cur = cur.getTailConcGomType();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) {
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
   * Checks if sa.rule.types.GomTypeList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.GomTypeList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.GomTypeList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
      while(cur instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
        if( o.equals(cur.getHeadConcGomType()) ) {
          return true;
        }
        cur = cur.getTailConcGomType();
      }
      if(!(cur instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) {
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
  public boolean isEmpty() { return isEmptyConcGomType() ; }

  public java.util.Iterator<sa.rule.types.GomType> iterator() {
    return new java.util.Iterator<sa.rule.types.GomType>() {
      sa.rule.types.GomTypeList list = ConcGomType.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyConcGomType();
      }

      public sa.rule.types.GomType next() {
        if(list.isEmptyConcGomType()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsConcGomType()) {
          sa.rule.types.GomType head = list.getHeadConcGomType();
          list = list.getTailConcGomType();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (sa.rule.types.GomType)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(sa.rule.types.GomType o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends sa.rule.types.GomType> c) {
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
    if(this instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
      sa.rule.types.GomTypeList cur = this;
      while(cur instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
        sa.rule.types.GomType elem = cur.getHeadConcGomType();
        array[i] = elem;
        cur = cur.getTailConcGomType();
        i++;
      }
      if(!(cur instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) {
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
    if(this instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
      sa.rule.types.GomTypeList cur = this;
      while(cur instanceof sa.rule.types.gomtypelist.ConsConcGomType) {
        sa.rule.types.GomType elem = cur.getHeadConcGomType();
        array[i] = (T)elem;
        cur = cur.getTailConcGomType();
        i++;
      }
      if(!(cur instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.GomType> getCollection() {
    return new CollectionConcGomType(this);
  }

  public java.util.Collection<sa.rule.types.GomType> getCollectionConcGomType() {
    return new CollectionConcGomType(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionConcGomType implements java.util.Collection<sa.rule.types.GomType> {
    private ConcGomType list;

    public ConcGomType getGomTypeList() {
      return list;
    }

    public CollectionConcGomType(ConcGomType list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends sa.rule.types.GomType> c) {
    boolean modified = false;
    java.util.Iterator<? extends sa.rule.types.GomType> it = c.iterator();
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
    return getGomTypeList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getGomTypeList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getGomTypeList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getGomTypeList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.GomType> iterator() {
    return getGomTypeList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getGomTypeList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getGomTypeList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getGomTypeList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getGomTypeList().length();
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
    public boolean add(sa.rule.types.GomType o) {
      list = (ConcGomType)sa.rule.types.gomtypelist.ConsConcGomType.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ConcGomType)sa.rule.types.gomtypelist.EmptyConcGomType.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyConcGomType();
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
