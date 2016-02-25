
package sa.rule.types.rulelist;

import java.util.*;


public abstract class ConcRule extends sa.rule.types.RuleList implements java.util.Collection<sa.rule.types.Rule>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof sa.rule.types.rulelist.ConsConcRule) {
      sa.rule.types.RuleList tl = this.getTailConcRule();
      if (tl instanceof ConcRule) {
        return 1+((ConcRule)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static sa.rule.types.RuleList fromArray(sa.rule.types.Rule[] array) {
    sa.rule.types.RuleList res = sa.rule.types.rulelist.EmptyConcRule.make();
    for(int i = array.length; i>0;) {
      res = sa.rule.types.rulelist.ConsConcRule.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public sa.rule.types.RuleList reverse() {
    if(this instanceof sa.rule.types.rulelist.ConsConcRule) {
      sa.rule.types.RuleList cur = this;
      sa.rule.types.RuleList rev = sa.rule.types.rulelist.EmptyConcRule.make();
      while(cur instanceof sa.rule.types.rulelist.ConsConcRule) {
        rev = sa.rule.types.rulelist.ConsConcRule.make(cur.getHeadConcRule(),rev);
        cur = cur.getTailConcRule();
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
  public sa.rule.types.RuleList append(sa.rule.types.Rule element) {
    if(this instanceof sa.rule.types.rulelist.ConsConcRule) {
      sa.rule.types.RuleList tl = this.getTailConcRule();
      if (tl instanceof ConcRule) {
        return sa.rule.types.rulelist.ConsConcRule.make(this.getHeadConcRule(),((ConcRule)tl).append(element));
      } else {

        return sa.rule.types.rulelist.ConsConcRule.make(this.getHeadConcRule(),sa.rule.types.rulelist.ConsConcRule.make(element,tl));

      }
    } else {
      return sa.rule.types.rulelist.ConsConcRule.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ConcRule(");
    if(this instanceof sa.rule.types.rulelist.ConsConcRule) {
      sa.rule.types.RuleList cur = this;
      while(cur instanceof sa.rule.types.rulelist.ConsConcRule) {
        sa.rule.types.Rule elem = cur.getHeadConcRule();
        cur = cur.getTailConcRule();
        elem.toStringBuilder(buffer);

        if(cur instanceof sa.rule.types.rulelist.ConsConcRule) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof sa.rule.types.rulelist.EmptyConcRule)) {
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
   * Checks if sa.rule.types.RuleList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if sa.rule.types.RuleList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    sa.rule.types.RuleList cur = this;
    if(o==null) { return false; }
    if(cur instanceof sa.rule.types.rulelist.ConsConcRule) {
      while(cur instanceof sa.rule.types.rulelist.ConsConcRule) {
        if( o.equals(cur.getHeadConcRule()) ) {
          return true;
        }
        cur = cur.getTailConcRule();
      }
      if(!(cur instanceof sa.rule.types.rulelist.EmptyConcRule)) {
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
  public boolean isEmpty() { return isEmptyConcRule() ; }

  public java.util.Iterator<sa.rule.types.Rule> iterator() {
    return new java.util.Iterator<sa.rule.types.Rule>() {
      sa.rule.types.RuleList list = ConcRule.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyConcRule();
      }

      public sa.rule.types.Rule next() {
        if(list.isEmptyConcRule()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsConcRule()) {
          sa.rule.types.Rule head = list.getHeadConcRule();
          list = list.getTailConcRule();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (sa.rule.types.Rule)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(sa.rule.types.Rule o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends sa.rule.types.Rule> c) {
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
    if(this instanceof sa.rule.types.rulelist.ConsConcRule) {
      sa.rule.types.RuleList cur = this;
      while(cur instanceof sa.rule.types.rulelist.ConsConcRule) {
        sa.rule.types.Rule elem = cur.getHeadConcRule();
        array[i] = elem;
        cur = cur.getTailConcRule();
        i++;
      }
      if(!(cur instanceof sa.rule.types.rulelist.EmptyConcRule)) {
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
    if(this instanceof sa.rule.types.rulelist.ConsConcRule) {
      sa.rule.types.RuleList cur = this;
      while(cur instanceof sa.rule.types.rulelist.ConsConcRule) {
        sa.rule.types.Rule elem = cur.getHeadConcRule();
        array[i] = (T)elem;
        cur = cur.getTailConcRule();
        i++;
      }
      if(!(cur instanceof sa.rule.types.rulelist.EmptyConcRule)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }
  
  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<sa.rule.types.Rule> getCollection() {
    return new CollectionConcRule(this);
  }

  public java.util.Collection<sa.rule.types.Rule> getCollectionConcRule() {
    return new CollectionConcRule(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionConcRule implements java.util.Collection<sa.rule.types.Rule> {
    private ConcRule list;

    public ConcRule getRuleList() {
      return list;
    }

    public CollectionConcRule(ConcRule list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends sa.rule.types.Rule> c) {
    boolean modified = false;
    java.util.Iterator<? extends sa.rule.types.Rule> it = c.iterator();
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
    return getRuleList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getRuleList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getRuleList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getRuleList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<sa.rule.types.Rule> iterator() {
    return getRuleList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getRuleList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getRuleList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */

  public <T> T[] toArray(T[] array) {
    return getRuleList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getRuleList().length();
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
    public boolean add(sa.rule.types.Rule o) {
      list = (ConcRule)sa.rule.types.rulelist.ConsConcRule.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ConcRule)sa.rule.types.rulelist.EmptyConcRule.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyConcRule();
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
