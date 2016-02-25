
package sa.rule.types.paramlist;

import java.util.*;


public abstract class ConcParam extends sa.rule.types.ParamList implements java.util.Collection<sa.rule.types.Param>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.paramlist.ConsConcParam) {
      sa.rule.types.ParamList tl = this.getTailConcParam();
      if (tl instanceof ConcParam) {
        return 1+((ConcParam)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.ParamList fromArray(sa.rule.types.Param[] array) {
    sa.rule.types.ParamList res = sa.rule.types.paramlist.EmptyConcParam.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.paramlist.ConsConcParam.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.ParamList reverse() {
    if(this instanceof sa.rule.types.paramlist.ConsConcParam) {
      sa.rule.types.ParamList cur = this;
      sa.rule.types.ParamList rev = sa.rule.types.paramlist.EmptyConcParam.make();
      while(cur instanceof sa.rule.types.paramlist.ConsConcParam) {
        rev = sa.rule.types.paramlist.ConsConcParam.make(cur.getHeadConcParam(),rev);
        cur = cur.getTailConcParam();
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
  public sa.rule.types.ParamList append(sa.rule.types.Param element) {
    if(this instanceof sa.rule.types.paramlist.ConsConcParam) {
      sa.rule.types.ParamList tl = this.getTailConcParam();
      if (tl instanceof ConcParam) {
        return sa.rule.types.paramlist.ConsConcParam.make(this.getHeadConcParam(),((ConcParam)tl).append(element));
      } else {

        return sa.rule.types.paramlist.ConsConcParam.make(this.getHeadConcParam(),sa.rule.types.paramlist.ConsConcParam.make(element,tl));

      }
    } else {
      return sa.rule.types.paramlist.ConsConcParam.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ConcParam(");
    if(this instanceof sa.rule.types.paramlist.ConsConcParam) {
      sa.rule.types.ParamList cur = this;
      while(cur instanceof sa.rule.types.paramlist.ConsConcParam) {
        sa.rule.types.Param elem = cur.getHeadConcParam();
        cur = cur.getTailConcParam();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.paramlist.ConsConcParam) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.paramlist.EmptyConcParam)) {
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
   * Checks if sa.rule.types.ParamList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.ParamList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.ParamList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.paramlist.ConsConcParam) {
      while(cur instanceof sa.rule.types.paramlist.ConsConcParam) {
        if( o.equals(cur.getHeadConcParam()) ) {
          return true;
        }
        cur = cur.getTailConcParam();
      }
      if(!(cur instanceof sa.rule.types.paramlist.EmptyConcParam)) {
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
  public boolean isEmpty() { return isEmptyConcParam() ; }

  public java.util.Iterator<sa.rule.types.Param> iterator() {
    return new java.util.Iterator<sa.rule.types.Param>() {
      sa.rule.types.ParamList list = ConcParam.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyConcParam();
      }

      public sa.rule.types.Param next() {
        if(list.isEmptyConcParam()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsConcParam()) {
          sa.rule.types.Param head = list.getHeadConcParam();
          list = list.getTailConcParam();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (sa.rule.types.Param)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(sa.rule.types.Param o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends sa.rule.types.Param> c) {
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
    if(this instanceof sa.rule.types.paramlist.ConsConcParam) {
      sa.rule.types.ParamList cur = this;
      while(cur instanceof sa.rule.types.paramlist.ConsConcParam) {
        sa.rule.types.Param elem = cur.getHeadConcParam();
        array[i] = elem;
        cur = cur.getTailConcParam();
        i++;
      }
      if(!(cur instanceof sa.rule.types.paramlist.EmptyConcParam)) {
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
    if(this instanceof sa.rule.types.paramlist.ConsConcParam) {
      sa.rule.types.ParamList cur = this;
      while(cur instanceof sa.rule.types.paramlist.ConsConcParam) {
        sa.rule.types.Param elem = cur.getHeadConcParam();
        array[i] = (T)elem;
        cur = cur.getTailConcParam();
        i++;
      }
      if(!(cur instanceof sa.rule.types.paramlist.EmptyConcParam)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.Param> getCollection() {
    return new CollectionConcParam(this);
  }

  public java.util.Collection<sa.rule.types.Param> getCollectionConcParam() {
    return new CollectionConcParam(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionConcParam implements java.util.Collection<sa.rule.types.Param> {
    private ConcParam list;

    public ConcParam getParamList() {
      return list;
    }

    public CollectionConcParam(ConcParam list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends sa.rule.types.Param> c) {
    boolean modified = false;
    java.util.Iterator<? extends sa.rule.types.Param> it = c.iterator();
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
    return getParamList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getParamList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getParamList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getParamList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.Param> iterator() {
    return getParamList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getParamList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getParamList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getParamList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getParamList().length();
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
    public boolean add(sa.rule.types.Param o) {
      list = (ConcParam)sa.rule.types.paramlist.ConsConcParam.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ConcParam)sa.rule.types.paramlist.EmptyConcParam.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyConcParam();
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
