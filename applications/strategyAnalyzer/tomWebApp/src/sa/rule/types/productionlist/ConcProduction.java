
package sa.rule.types.productionlist;

import java.util.*;


public abstract class ConcProduction extends sa.rule.types.ProductionList implements java.util.Collection<sa.rule.types.Production>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.productionlist.ConsConcProduction) {
      sa.rule.types.ProductionList tl = this.getTailConcProduction();
      if (tl instanceof ConcProduction) {
        return 1+((ConcProduction)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.ProductionList fromArray(sa.rule.types.Production[] array) {
    sa.rule.types.ProductionList res = sa.rule.types.productionlist.EmptyConcProduction.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.productionlist.ConsConcProduction.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.ProductionList reverse() {
    if(this instanceof sa.rule.types.productionlist.ConsConcProduction) {
      sa.rule.types.ProductionList cur = this;
      sa.rule.types.ProductionList rev = sa.rule.types.productionlist.EmptyConcProduction.make();
      while(cur instanceof sa.rule.types.productionlist.ConsConcProduction) {
        rev = sa.rule.types.productionlist.ConsConcProduction.make(cur.getHeadConcProduction(),rev);
        cur = cur.getTailConcProduction();
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
  public sa.rule.types.ProductionList append(sa.rule.types.Production element) {
    if(this instanceof sa.rule.types.productionlist.ConsConcProduction) {
      sa.rule.types.ProductionList tl = this.getTailConcProduction();
      if (tl instanceof ConcProduction) {
        return sa.rule.types.productionlist.ConsConcProduction.make(this.getHeadConcProduction(),((ConcProduction)tl).append(element));
      } else {

        return sa.rule.types.productionlist.ConsConcProduction.make(this.getHeadConcProduction(),sa.rule.types.productionlist.ConsConcProduction.make(element,tl));

      }
    } else {
      return sa.rule.types.productionlist.ConsConcProduction.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ConcProduction(");
    if(this instanceof sa.rule.types.productionlist.ConsConcProduction) {
      sa.rule.types.ProductionList cur = this;
      while(cur instanceof sa.rule.types.productionlist.ConsConcProduction) {
        sa.rule.types.Production elem = cur.getHeadConcProduction();
        cur = cur.getTailConcProduction();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.productionlist.ConsConcProduction) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.productionlist.EmptyConcProduction)) {
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
   * Checks if sa.rule.types.ProductionList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.ProductionList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.ProductionList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.productionlist.ConsConcProduction) {
      while(cur instanceof sa.rule.types.productionlist.ConsConcProduction) {
        if( o.equals(cur.getHeadConcProduction()) ) {
          return true;
        }
        cur = cur.getTailConcProduction();
      }
      if(!(cur instanceof sa.rule.types.productionlist.EmptyConcProduction)) {
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
  public boolean isEmpty() { return isEmptyConcProduction() ; }

  public java.util.Iterator<sa.rule.types.Production> iterator() {
    return new java.util.Iterator<sa.rule.types.Production>() {
      sa.rule.types.ProductionList list = ConcProduction.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyConcProduction();
      }

      public sa.rule.types.Production next() {
        if(list.isEmptyConcProduction()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsConcProduction()) {
          sa.rule.types.Production head = list.getHeadConcProduction();
          list = list.getTailConcProduction();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (sa.rule.types.Production)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(sa.rule.types.Production o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends sa.rule.types.Production> c) {
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
    if(this instanceof sa.rule.types.productionlist.ConsConcProduction) {
      sa.rule.types.ProductionList cur = this;
      while(cur instanceof sa.rule.types.productionlist.ConsConcProduction) {
        sa.rule.types.Production elem = cur.getHeadConcProduction();
        array[i] = elem;
        cur = cur.getTailConcProduction();
        i++;
      }
      if(!(cur instanceof sa.rule.types.productionlist.EmptyConcProduction)) {
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
    if(this instanceof sa.rule.types.productionlist.ConsConcProduction) {
      sa.rule.types.ProductionList cur = this;
      while(cur instanceof sa.rule.types.productionlist.ConsConcProduction) {
        sa.rule.types.Production elem = cur.getHeadConcProduction();
        array[i] = (T)elem;
        cur = cur.getTailConcProduction();
        i++;
      }
      if(!(cur instanceof sa.rule.types.productionlist.EmptyConcProduction)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.Production> getCollection() {
    return new CollectionConcProduction(this);
  }

  public java.util.Collection<sa.rule.types.Production> getCollectionConcProduction() {
    return new CollectionConcProduction(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionConcProduction implements java.util.Collection<sa.rule.types.Production> {
    private ConcProduction list;

    public ConcProduction getProductionList() {
      return list;
    }

    public CollectionConcProduction(ConcProduction list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends sa.rule.types.Production> c) {
    boolean modified = false;
    java.util.Iterator<? extends sa.rule.types.Production> it = c.iterator();
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
    return getProductionList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getProductionList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getProductionList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getProductionList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.Production> iterator() {
    return getProductionList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getProductionList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getProductionList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getProductionList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getProductionList().length();
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
    public boolean add(sa.rule.types.Production o) {
      list = (ConcProduction)sa.rule.types.productionlist.ConsConcProduction.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ConcProduction)sa.rule.types.productionlist.EmptyConcProduction.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyConcProduction();
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
