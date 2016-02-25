
package sa.rule.types.stratdecllist;

import java.util.*;


public abstract class ConcStratDecl extends sa.rule.types.StratDeclList implements java.util.Collection<sa.rule.types.StratDecl>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
      sa.rule.types.StratDeclList tl = this.getTailConcStratDecl();
      if (tl instanceof ConcStratDecl) {
        return 1+((ConcStratDecl)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.StratDeclList fromArray(sa.rule.types.StratDecl[] array) {
    sa.rule.types.StratDeclList res = sa.rule.types.stratdecllist.EmptyConcStratDecl.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.stratdecllist.ConsConcStratDecl.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.StratDeclList reverse() {
    if(this instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
      sa.rule.types.StratDeclList cur = this;
      sa.rule.types.StratDeclList rev = sa.rule.types.stratdecllist.EmptyConcStratDecl.make();
      while(cur instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
        rev = sa.rule.types.stratdecllist.ConsConcStratDecl.make(cur.getHeadConcStratDecl(),rev);
        cur = cur.getTailConcStratDecl();
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
  public sa.rule.types.StratDeclList append(sa.rule.types.StratDecl element) {
    if(this instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
      sa.rule.types.StratDeclList tl = this.getTailConcStratDecl();
      if (tl instanceof ConcStratDecl) {
        return sa.rule.types.stratdecllist.ConsConcStratDecl.make(this.getHeadConcStratDecl(),((ConcStratDecl)tl).append(element));
      } else {

        return sa.rule.types.stratdecllist.ConsConcStratDecl.make(this.getHeadConcStratDecl(),sa.rule.types.stratdecllist.ConsConcStratDecl.make(element,tl));

      }
    } else {
      return sa.rule.types.stratdecllist.ConsConcStratDecl.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ConcStratDecl(");
    if(this instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
      sa.rule.types.StratDeclList cur = this;
      while(cur instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
        sa.rule.types.StratDecl elem = cur.getHeadConcStratDecl();
        cur = cur.getTailConcStratDecl();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.stratdecllist.EmptyConcStratDecl)) {
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
   * Checks if sa.rule.types.StratDeclList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.StratDeclList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.StratDeclList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
      while(cur instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
        if( o.equals(cur.getHeadConcStratDecl()) ) {
          return true;
        }
        cur = cur.getTailConcStratDecl();
      }
      if(!(cur instanceof sa.rule.types.stratdecllist.EmptyConcStratDecl)) {
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
  public boolean isEmpty() { return isEmptyConcStratDecl() ; }

  public java.util.Iterator<sa.rule.types.StratDecl> iterator() {
    return new java.util.Iterator<sa.rule.types.StratDecl>() {
      sa.rule.types.StratDeclList list = ConcStratDecl.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyConcStratDecl();
      }

      public sa.rule.types.StratDecl next() {
        if(list.isEmptyConcStratDecl()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsConcStratDecl()) {
          sa.rule.types.StratDecl head = list.getHeadConcStratDecl();
          list = list.getTailConcStratDecl();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (sa.rule.types.StratDecl)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(sa.rule.types.StratDecl o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends sa.rule.types.StratDecl> c) {
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
    if(this instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
      sa.rule.types.StratDeclList cur = this;
      while(cur instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
        sa.rule.types.StratDecl elem = cur.getHeadConcStratDecl();
        array[i] = elem;
        cur = cur.getTailConcStratDecl();
        i++;
      }
      if(!(cur instanceof sa.rule.types.stratdecllist.EmptyConcStratDecl)) {
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
    if(this instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
      sa.rule.types.StratDeclList cur = this;
      while(cur instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) {
        sa.rule.types.StratDecl elem = cur.getHeadConcStratDecl();
        array[i] = (T)elem;
        cur = cur.getTailConcStratDecl();
        i++;
      }
      if(!(cur instanceof sa.rule.types.stratdecllist.EmptyConcStratDecl)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.StratDecl> getCollection() {
    return new CollectionConcStratDecl(this);
  }

  public java.util.Collection<sa.rule.types.StratDecl> getCollectionConcStratDecl() {
    return new CollectionConcStratDecl(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionConcStratDecl implements java.util.Collection<sa.rule.types.StratDecl> {
    private ConcStratDecl list;

    public ConcStratDecl getStratDeclList() {
      return list;
    }

    public CollectionConcStratDecl(ConcStratDecl list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends sa.rule.types.StratDecl> c) {
    boolean modified = false;
    java.util.Iterator<? extends sa.rule.types.StratDecl> it = c.iterator();
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
    return getStratDeclList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getStratDeclList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getStratDeclList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getStratDeclList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.StratDecl> iterator() {
    return getStratDeclList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getStratDeclList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getStratDeclList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getStratDeclList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getStratDeclList().length();
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
    public boolean add(sa.rule.types.StratDecl o) {
      list = (ConcStratDecl)sa.rule.types.stratdecllist.ConsConcStratDecl.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ConcStratDecl)sa.rule.types.stratdecllist.EmptyConcStratDecl.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyConcStratDecl();
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
