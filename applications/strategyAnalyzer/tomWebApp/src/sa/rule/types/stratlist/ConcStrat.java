
package sa.rule.types.stratlist;

import java.util.*;


public abstract class ConcStrat extends sa.rule.types.StratList implements java.util.Collection<sa.rule.types.Strat>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.stratlist.ConsConcStrat) {
      sa.rule.types.StratList tl = this.getTailConcStrat();
      if (tl instanceof ConcStrat) {
        return 1+((ConcStrat)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.StratList fromArray(sa.rule.types.Strat[] array) {
    sa.rule.types.StratList res = sa.rule.types.stratlist.EmptyConcStrat.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.stratlist.ConsConcStrat.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.StratList reverse() {
    if(this instanceof sa.rule.types.stratlist.ConsConcStrat) {
      sa.rule.types.StratList cur = this;
      sa.rule.types.StratList rev = sa.rule.types.stratlist.EmptyConcStrat.make();
      while(cur instanceof sa.rule.types.stratlist.ConsConcStrat) {
        rev = sa.rule.types.stratlist.ConsConcStrat.make(cur.getHeadConcStrat(),rev);
        cur = cur.getTailConcStrat();
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
  public sa.rule.types.StratList append(sa.rule.types.Strat element) {
    if(this instanceof sa.rule.types.stratlist.ConsConcStrat) {
      sa.rule.types.StratList tl = this.getTailConcStrat();
      if (tl instanceof ConcStrat) {
        return sa.rule.types.stratlist.ConsConcStrat.make(this.getHeadConcStrat(),((ConcStrat)tl).append(element));
      } else {

        return sa.rule.types.stratlist.ConsConcStrat.make(this.getHeadConcStrat(),sa.rule.types.stratlist.ConsConcStrat.make(element,tl));

      }
    } else {
      return sa.rule.types.stratlist.ConsConcStrat.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ConcStrat(");
    if(this instanceof sa.rule.types.stratlist.ConsConcStrat) {
      sa.rule.types.StratList cur = this;
      while(cur instanceof sa.rule.types.stratlist.ConsConcStrat) {
        sa.rule.types.Strat elem = cur.getHeadConcStrat();
        cur = cur.getTailConcStrat();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.stratlist.ConsConcStrat) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.stratlist.EmptyConcStrat)) {
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
   * Checks if sa.rule.types.StratList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.StratList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.StratList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.stratlist.ConsConcStrat) {
      while(cur instanceof sa.rule.types.stratlist.ConsConcStrat) {
        if( o.equals(cur.getHeadConcStrat()) ) {
          return true;
        }
        cur = cur.getTailConcStrat();
      }
      if(!(cur instanceof sa.rule.types.stratlist.EmptyConcStrat)) {
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
  public boolean isEmpty() { return isEmptyConcStrat() ; }

  public java.util.Iterator<sa.rule.types.Strat> iterator() {
    return new java.util.Iterator<sa.rule.types.Strat>() {
      sa.rule.types.StratList list = ConcStrat.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyConcStrat();
      }

      public sa.rule.types.Strat next() {
        if(list.isEmptyConcStrat()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsConcStrat()) {
          sa.rule.types.Strat head = list.getHeadConcStrat();
          list = list.getTailConcStrat();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (sa.rule.types.Strat)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(sa.rule.types.Strat o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends sa.rule.types.Strat> c) {
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
    if(this instanceof sa.rule.types.stratlist.ConsConcStrat) {
      sa.rule.types.StratList cur = this;
      while(cur instanceof sa.rule.types.stratlist.ConsConcStrat) {
        sa.rule.types.Strat elem = cur.getHeadConcStrat();
        array[i] = elem;
        cur = cur.getTailConcStrat();
        i++;
      }
      if(!(cur instanceof sa.rule.types.stratlist.EmptyConcStrat)) {
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
    if(this instanceof sa.rule.types.stratlist.ConsConcStrat) {
      sa.rule.types.StratList cur = this;
      while(cur instanceof sa.rule.types.stratlist.ConsConcStrat) {
        sa.rule.types.Strat elem = cur.getHeadConcStrat();
        array[i] = (T)elem;
        cur = cur.getTailConcStrat();
        i++;
      }
      if(!(cur instanceof sa.rule.types.stratlist.EmptyConcStrat)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.Strat> getCollection() {
    return new CollectionConcStrat(this);
  }

  public java.util.Collection<sa.rule.types.Strat> getCollectionConcStrat() {
    return new CollectionConcStrat(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionConcStrat implements java.util.Collection<sa.rule.types.Strat> {
    private ConcStrat list;

    public ConcStrat getStratList() {
      return list;
    }

    public CollectionConcStrat(ConcStrat list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends sa.rule.types.Strat> c) {
    boolean modified = false;
    java.util.Iterator<? extends sa.rule.types.Strat> it = c.iterator();
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
    return getStratList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getStratList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getStratList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getStratList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.Strat> iterator() {
    return getStratList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getStratList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getStratList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getStratList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getStratList().length();
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
    public boolean add(sa.rule.types.Strat o) {
      list = (ConcStrat)sa.rule.types.stratlist.ConsConcStrat.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ConcStrat)sa.rule.types.stratlist.EmptyConcStrat.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyConcStrat();
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
