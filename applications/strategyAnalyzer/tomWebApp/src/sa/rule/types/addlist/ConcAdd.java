
package sa.rule.types.addlist;

import java.util.*;


public abstract class ConcAdd extends sa.rule.types.AddList implements java.util.Collection<sa.rule.types.Term>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.addlist.ConsConcAdd) {
      sa.rule.types.AddList tl = this.getTailConcAdd();
      if (tl instanceof ConcAdd) {
        return 1+((ConcAdd)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.AddList fromArray(sa.rule.types.Term[] array) {
    sa.rule.types.AddList res = sa.rule.types.addlist.EmptyConcAdd.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.addlist.ConsConcAdd.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.AddList reverse() {
    if(this instanceof sa.rule.types.addlist.ConsConcAdd) {
      sa.rule.types.AddList cur = this;
      sa.rule.types.AddList rev = sa.rule.types.addlist.EmptyConcAdd.make();
      while(cur instanceof sa.rule.types.addlist.ConsConcAdd) {
        rev = sa.rule.types.addlist.ConsConcAdd.make(cur.getHeadConcAdd(),rev);
        cur = cur.getTailConcAdd();
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
  public sa.rule.types.AddList append(sa.rule.types.Term element) {
    if(this instanceof sa.rule.types.addlist.ConsConcAdd) {
      sa.rule.types.AddList tl = this.getTailConcAdd();
      if (tl instanceof ConcAdd) {
        return sa.rule.types.addlist.ConsConcAdd.make(this.getHeadConcAdd(),((ConcAdd)tl).append(element));
      } else {

        return sa.rule.types.addlist.ConsConcAdd.make(this.getHeadConcAdd(),sa.rule.types.addlist.ConsConcAdd.make(element,tl));

      }
    } else {
      return sa.rule.types.addlist.ConsConcAdd.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ConcAdd(");
    if(this instanceof sa.rule.types.addlist.ConsConcAdd) {
      sa.rule.types.AddList cur = this;
      while(cur instanceof sa.rule.types.addlist.ConsConcAdd) {
        sa.rule.types.Term elem = cur.getHeadConcAdd();
        cur = cur.getTailConcAdd();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.addlist.ConsConcAdd) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.addlist.EmptyConcAdd)) {
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
   * Checks if sa.rule.types.AddList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.AddList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.AddList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.addlist.ConsConcAdd) {
      while(cur instanceof sa.rule.types.addlist.ConsConcAdd) {
        if( o.equals(cur.getHeadConcAdd()) ) {
          return true;
        }
        cur = cur.getTailConcAdd();
      }
      if(!(cur instanceof sa.rule.types.addlist.EmptyConcAdd)) {
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
  public boolean isEmpty() { return isEmptyConcAdd() ; }

  public java.util.Iterator<sa.rule.types.Term> iterator() {
    return new java.util.Iterator<sa.rule.types.Term>() {
      sa.rule.types.AddList list = ConcAdd.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyConcAdd();
      }

      public sa.rule.types.Term next() {
        if(list.isEmptyConcAdd()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsConcAdd()) {
          sa.rule.types.Term head = list.getHeadConcAdd();
          list = list.getTailConcAdd();
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
    if(this instanceof sa.rule.types.addlist.ConsConcAdd) {
      sa.rule.types.AddList cur = this;
      while(cur instanceof sa.rule.types.addlist.ConsConcAdd) {
        sa.rule.types.Term elem = cur.getHeadConcAdd();
        array[i] = elem;
        cur = cur.getTailConcAdd();
        i++;
      }
      if(!(cur instanceof sa.rule.types.addlist.EmptyConcAdd)) {
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
    if(this instanceof sa.rule.types.addlist.ConsConcAdd) {
      sa.rule.types.AddList cur = this;
      while(cur instanceof sa.rule.types.addlist.ConsConcAdd) {
        sa.rule.types.Term elem = cur.getHeadConcAdd();
        array[i] = (T)elem;
        cur = cur.getTailConcAdd();
        i++;
      }
      if(!(cur instanceof sa.rule.types.addlist.EmptyConcAdd)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.Term> getCollection() {
    return new CollectionConcAdd(this);
  }

  public java.util.Collection<sa.rule.types.Term> getCollectionConcAdd() {
    return new CollectionConcAdd(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionConcAdd implements java.util.Collection<sa.rule.types.Term> {
    private ConcAdd list;

    public ConcAdd getAddList() {
      return list;
    }

    public CollectionConcAdd(ConcAdd list) {
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
    return getAddList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getAddList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getAddList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getAddList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.Term> iterator() {
    return getAddList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getAddList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getAddList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getAddList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getAddList().length();
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
      list = (ConcAdd)sa.rule.types.addlist.ConsConcAdd.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ConcAdd)sa.rule.types.addlist.EmptyConcAdd.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyConcAdd();
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
