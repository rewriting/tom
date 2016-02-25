
package sa.rule.types.termlist;

import java.util.*;


public abstract class TermList extends sa.rule.types.TermList implements java.util.Collection<sa.rule.types.Term>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.termlist.ConsTermList) {
      sa.rule.types.TermList tl = this.getTailTermList();
      if (tl instanceof TermList) {
        return 1+((TermList)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.TermList fromArray(sa.rule.types.Term[] array) {
    sa.rule.types.TermList res = sa.rule.types.termlist.EmptyTermList.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.termlist.ConsTermList.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.TermList reverse() {
    if(this instanceof sa.rule.types.termlist.ConsTermList) {
      sa.rule.types.TermList cur = this;
      sa.rule.types.TermList rev = sa.rule.types.termlist.EmptyTermList.make();
      while(cur instanceof sa.rule.types.termlist.ConsTermList) {
        rev = sa.rule.types.termlist.ConsTermList.make(cur.getHeadTermList(),rev);
        cur = cur.getTailTermList();
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
  public sa.rule.types.TermList append(sa.rule.types.Term element) {
    if(this instanceof sa.rule.types.termlist.ConsTermList) {
      sa.rule.types.TermList tl = this.getTailTermList();
      if (tl instanceof TermList) {
        return sa.rule.types.termlist.ConsTermList.make(this.getHeadTermList(),((TermList)tl).append(element));
      } else {

        return sa.rule.types.termlist.ConsTermList.make(this.getHeadTermList(),sa.rule.types.termlist.ConsTermList.make(element,tl));

      }
    } else {
      return sa.rule.types.termlist.ConsTermList.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("TermList(");
    if(this instanceof sa.rule.types.termlist.ConsTermList) {
      sa.rule.types.TermList cur = this;
      while(cur instanceof sa.rule.types.termlist.ConsTermList) {
        sa.rule.types.Term elem = cur.getHeadTermList();
        cur = cur.getTailTermList();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.termlist.ConsTermList) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.termlist.EmptyTermList)) {
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
   * Checks if sa.rule.types.TermList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.TermList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.TermList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.termlist.ConsTermList) {
      while(cur instanceof sa.rule.types.termlist.ConsTermList) {
        if( o.equals(cur.getHeadTermList()) ) {
          return true;
        }
        cur = cur.getTailTermList();
      }
      if(!(cur instanceof sa.rule.types.termlist.EmptyTermList)) {
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
  public boolean isEmpty() { return isEmptyTermList() ; }

  public java.util.Iterator<sa.rule.types.Term> iterator() {
    return new java.util.Iterator<sa.rule.types.Term>() {
      sa.rule.types.TermList list = TermList.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyTermList();
      }

      public sa.rule.types.Term next() {
        if(list.isEmptyTermList()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsTermList()) {
          sa.rule.types.Term head = list.getHeadTermList();
          list = list.getTailTermList();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (sa.rule.types.Term)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(sa.rule.types.Term o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends sa.rule.types.Term> c) {
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
    if(this instanceof sa.rule.types.termlist.ConsTermList) {
      sa.rule.types.TermList cur = this;
      while(cur instanceof sa.rule.types.termlist.ConsTermList) {
        sa.rule.types.Term elem = cur.getHeadTermList();
        array[i] = elem;
        cur = cur.getTailTermList();
        i++;
      }
      if(!(cur instanceof sa.rule.types.termlist.EmptyTermList)) {
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
    if(this instanceof sa.rule.types.termlist.ConsTermList) {
      sa.rule.types.TermList cur = this;
      while(cur instanceof sa.rule.types.termlist.ConsTermList) {
        sa.rule.types.Term elem = cur.getHeadTermList();
        array[i] = (T)elem;
        cur = cur.getTailTermList();
        i++;
      }
      if(!(cur instanceof sa.rule.types.termlist.EmptyTermList)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.Term> getCollection() {
    return new CollectionTermList(this);
  }

  public java.util.Collection<sa.rule.types.Term> getCollectionTermList() {
    return new CollectionTermList(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionTermList implements java.util.Collection<sa.rule.types.Term> {
    private TermList list;

    public TermList getTermList() {
      return list;
    }

    public CollectionTermList(TermList list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends sa.rule.types.Term> c) {
    boolean modified = false;
    java.util.Iterator<? extends sa.rule.types.Term> it = c.iterator();
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
    return getTermList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getTermList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getTermList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getTermList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.Term> iterator() {
    return getTermList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getTermList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getTermList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getTermList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getTermList().length();
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
    public boolean add(sa.rule.types.Term o) {
      list = (TermList)sa.rule.types.termlist.ConsTermList.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (TermList)sa.rule.types.termlist.EmptyTermList.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyTermList();
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
