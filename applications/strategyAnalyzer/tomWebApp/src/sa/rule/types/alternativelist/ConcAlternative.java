
package sa.rule.types.alternativelist;

import java.util.*;


public abstract class ConcAlternative extends sa.rule.types.AlternativeList implements java.util.Collection<sa.rule.types.Alternative>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
      sa.rule.types.AlternativeList tl = this.getTailConcAlternative();
      if (tl instanceof ConcAlternative) {
        return 1+((ConcAlternative)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.AlternativeList fromArray(sa.rule.types.Alternative[] array) {
    sa.rule.types.AlternativeList res = sa.rule.types.alternativelist.EmptyConcAlternative.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.alternativelist.ConsConcAlternative.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.AlternativeList reverse() {
    if(this instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
      sa.rule.types.AlternativeList cur = this;
      sa.rule.types.AlternativeList rev = sa.rule.types.alternativelist.EmptyConcAlternative.make();
      while(cur instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
        rev = sa.rule.types.alternativelist.ConsConcAlternative.make(cur.getHeadConcAlternative(),rev);
        cur = cur.getTailConcAlternative();
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
  public sa.rule.types.AlternativeList append(sa.rule.types.Alternative element) {
    if(this instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
      sa.rule.types.AlternativeList tl = this.getTailConcAlternative();
      if (tl instanceof ConcAlternative) {
        return sa.rule.types.alternativelist.ConsConcAlternative.make(this.getHeadConcAlternative(),((ConcAlternative)tl).append(element));
      } else {

        return sa.rule.types.alternativelist.ConsConcAlternative.make(this.getHeadConcAlternative(),sa.rule.types.alternativelist.ConsConcAlternative.make(element,tl));

      }
    } else {
      return sa.rule.types.alternativelist.ConsConcAlternative.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ConcAlternative(");
    if(this instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
      sa.rule.types.AlternativeList cur = this;
      while(cur instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
        sa.rule.types.Alternative elem = cur.getHeadConcAlternative();
        cur = cur.getTailConcAlternative();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.alternativelist.EmptyConcAlternative)) {
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
   * Checks if sa.rule.types.AlternativeList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.AlternativeList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.AlternativeList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
      while(cur instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
        if( o.equals(cur.getHeadConcAlternative()) ) {
          return true;
        }
        cur = cur.getTailConcAlternative();
      }
      if(!(cur instanceof sa.rule.types.alternativelist.EmptyConcAlternative)) {
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
  public boolean isEmpty() { return isEmptyConcAlternative() ; }

  public java.util.Iterator<sa.rule.types.Alternative> iterator() {
    return new java.util.Iterator<sa.rule.types.Alternative>() {
      sa.rule.types.AlternativeList list = ConcAlternative.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyConcAlternative();
      }

      public sa.rule.types.Alternative next() {
        if(list.isEmptyConcAlternative()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsConcAlternative()) {
          sa.rule.types.Alternative head = list.getHeadConcAlternative();
          list = list.getTailConcAlternative();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (sa.rule.types.Alternative)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(sa.rule.types.Alternative o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends sa.rule.types.Alternative> c) {
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
    if(this instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
      sa.rule.types.AlternativeList cur = this;
      while(cur instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
        sa.rule.types.Alternative elem = cur.getHeadConcAlternative();
        array[i] = elem;
        cur = cur.getTailConcAlternative();
        i++;
      }
      if(!(cur instanceof sa.rule.types.alternativelist.EmptyConcAlternative)) {
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
    if(this instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
      sa.rule.types.AlternativeList cur = this;
      while(cur instanceof sa.rule.types.alternativelist.ConsConcAlternative) {
        sa.rule.types.Alternative elem = cur.getHeadConcAlternative();
        array[i] = (T)elem;
        cur = cur.getTailConcAlternative();
        i++;
      }
      if(!(cur instanceof sa.rule.types.alternativelist.EmptyConcAlternative)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.Alternative> getCollection() {
    return new CollectionConcAlternative(this);
  }

  public java.util.Collection<sa.rule.types.Alternative> getCollectionConcAlternative() {
    return new CollectionConcAlternative(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionConcAlternative implements java.util.Collection<sa.rule.types.Alternative> {
    private ConcAlternative list;

    public ConcAlternative getAlternativeList() {
      return list;
    }

    public CollectionConcAlternative(ConcAlternative list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends sa.rule.types.Alternative> c) {
    boolean modified = false;
    java.util.Iterator<? extends sa.rule.types.Alternative> it = c.iterator();
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
    return getAlternativeList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getAlternativeList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getAlternativeList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getAlternativeList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.Alternative> iterator() {
    return getAlternativeList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getAlternativeList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getAlternativeList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getAlternativeList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getAlternativeList().length();
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
    public boolean add(sa.rule.types.Alternative o) {
      list = (ConcAlternative)sa.rule.types.alternativelist.ConsConcAlternative.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ConcAlternative)sa.rule.types.alternativelist.EmptyConcAlternative.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyConcAlternative();
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
