
package minimal.types.nop;


public abstract class Vary extends minimal.types.Nop implements java.util.Collection<minimal.types.Nop>  {


  /** 
   * Returns the number of arguments of the variadic operator
   * 
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof minimal.types.nop.ConsVary) {
      minimal.types.Nop tl = this.getTailVary();
      if (tl instanceof Vary) {
        return 1+((Vary)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static minimal.types.Nop fromArray(minimal.types.Nop[] array) {
    minimal.types.Nop res = minimal.types.nop.EmptyVary.make();
    for(int i = array.length; i>0;) {
      res = minimal.types.nop.ConsVary.make(array[--i],res);
    }
    return res;
  }

  /** 
   * Inverses the term if it is a list
   * 
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public minimal.types.Nop reverse() {
    if(this instanceof minimal.types.nop.ConsVary) {
      minimal.types.Nop cur = this;
      minimal.types.Nop rev = minimal.types.nop.EmptyVary.make();
      while(cur instanceof minimal.types.nop.ConsVary) {
        rev = minimal.types.nop.ConsVary.make(cur.getHeadVary(),rev);
        cur = cur.getTailVary();
      }

      if(!(cur instanceof minimal.types.nop.EmptyVary)) { 
        rev = minimal.types.nop.ConsVary.make(cur,rev);
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
  public minimal.types.Nop append(minimal.types.Nop element) {
    if(this instanceof minimal.types.nop.ConsVary) {
      minimal.types.Nop tl = this.getTailVary();
      if (tl instanceof Vary) {
        return minimal.types.nop.ConsVary.make(this.getHeadVary(),((Vary)tl).append(element));
      } else {

        return minimal.types.nop.ConsVary.make(this.getHeadVary(),minimal.types.nop.ConsVary.make(tl,element));

      }
    } else {
      return minimal.types.nop.ConsVary.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Vary(");
    if(this instanceof minimal.types.nop.ConsVary) {
      minimal.types.Nop cur = this;
      while(cur instanceof minimal.types.nop.ConsVary) {
        minimal.types.Nop elem = cur.getHeadVary();
        cur = cur.getTailVary();
        elem.toStringBuilder(buffer);

        if(cur instanceof minimal.types.nop.ConsVary) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof minimal.types.nop.EmptyVary)) {
        buffer.append(",");
        cur.toStringBuilder(buffer);
      }
    }
    buffer.append(")");
  }

  /** 
   * Returns an ATerm representation of this term.
   * 
   * @return an ATerm representation of this term.
   */
  public aterm.ATerm toATerm() {
    aterm.ATerm res = atermFactory.makeList();
    if(this instanceof minimal.types.nop.ConsVary) {
      minimal.types.Nop tail = this.getTailVary();
      res = atermFactory.makeList(getHeadVary().toATerm(),(aterm.ATermList)tail.toATerm());
    } 
    return res;
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a minimal.types.Nop from it
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static minimal.types.Nop fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("Vary".equals(appl.getName())) {
        minimal.types.Nop res = minimal.types.nop.EmptyVary.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          minimal.types.Nop elem = minimal.types.Nop.fromTerm(array[i],atConv);
          res = minimal.types.nop.ConsVary.make(elem,res);
        }
        return res;
      }
    }

    if(trm instanceof aterm.ATermList) {
      aterm.ATermList list = (aterm.ATermList) trm;
      minimal.types.Nop res = minimal.types.nop.EmptyVary.make();
      try {
        while(!list.isEmpty()) {
          minimal.types.Nop elem = minimal.types.Nop.fromTerm(list.getFirst(),atConv);
          res = minimal.types.nop.ConsVary.make(elem,res);
          list = list.getNext();
        }
      } catch(IllegalArgumentException e) {
        // returns null when the fromATerm call failed
        return null;
      }
      return res.reverse();
    }

    return null;
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
   * Checks if minimal.types.Nop contains a specified object
   * 
   * @param o object whose presence is tested
   * @return true if minimal.types.Nop contains the object, otherwise false
   */
  public boolean contains(Object o) {
    minimal.types.Nop cur = this;
    if(o==null) { return false; }
    if(cur instanceof minimal.types.nop.ConsVary) {
      while(cur instanceof minimal.types.nop.ConsVary) {
        if( o.equals(cur.getHeadVary()) ) {
          return true;
        }
        cur = cur.getTailVary();
      }
      if(!(cur instanceof minimal.types.nop.EmptyVary)) { 
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
  public boolean isEmpty() { return isEmptyVary() ; }

  public java.util.Iterator<minimal.types.Nop> iterator() {
    return new java.util.Iterator<minimal.types.Nop>() {
      minimal.types.Nop list = Vary.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyVary();
      }

      public minimal.types.Nop next() {
        if(list.isEmptyVary()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsVary()) {
          minimal.types.Nop head = list.getHeadVary();
          list = list.getTailVary();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (minimal.types.Nop)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(minimal.types.Nop o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends minimal.types.Nop> c) {
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
    if(this instanceof minimal.types.nop.ConsVary) {
      minimal.types.Nop cur = this;
      while(cur instanceof minimal.types.nop.ConsVary) {
        minimal.types.Nop elem = cur.getHeadVary();
        array[i] = elem;
        cur = cur.getTailVary();
        i++;
      }
      if(!(cur instanceof minimal.types.nop.EmptyVary)) {
        array[i] = cur;
      }
    }
    return array;
  }

  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] array) {
    int size = this.length();
    if (array.length < size) {
      array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
    } else if (array.length > size) {
      array[size] = null;
    }
    int i=0;
    if(this instanceof minimal.types.nop.ConsVary) {
      minimal.types.Nop cur = this;
      while(cur instanceof minimal.types.nop.ConsVary) {
        minimal.types.Nop elem = cur.getHeadVary();
        array[i] = (T)elem;
        cur = cur.getTailVary();
        i++;
      }
      if(!(cur instanceof minimal.types.nop.EmptyVary)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<minimal.types.Nop> getCollection() {
    return new CollectionVary(this);
  }

  public java.util.Collection<minimal.types.Nop> getCollectionVary() {
    return new CollectionVary(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionVary implements java.util.Collection<minimal.types.Nop> {
    private Vary list;

    public Vary getNop() {
      return list; 
    }

    public CollectionVary(Vary list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends minimal.types.Nop> c) {
    boolean modified = false;
    java.util.Iterator<? extends minimal.types.Nop> it = c.iterator();
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
    return getNop().contains(o);
  }

  /** 
   * Checks if the collection contains elements given as parameter
   * 
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getNop().containsAll(c);
  }

  /** 
   * Checks if an object is equal
   * 
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) { 
    return getNop().equals(o); 
  }

  /** 
   * Returns the hashCode
   * 
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getNop().hashCode(); 
  }

  /** 
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<minimal.types.Nop> iterator() {
    return getNop().iterator();
  }

  /** 
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() { 
    return getNop().size(); 
  }

  /** 
   * Returns an array containing all of the elements in this collection.
   * 
   * @return an array of elements
   */
  public Object[] toArray() {
    return getNop().toArray();
  }

  /** 
   * Returns an array containing all of the elements in this collection.
   * 
   * @param array array which will contain the result
   * @return an array of elements
   */
  public <T> T[] toArray(T[] array) {
    return getNop().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getNop().length();
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
    public boolean add(minimal.types.Nop o) {
      list = (Vary)minimal.types.nop.ConsVary.make(o,list);
      return true;
    }

    /** 
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (Vary)minimal.types.nop.EmptyVary.make();
    }

    /** 
     * Tests the emptiness of the collection
     * 
     * @return true if the collection is empty
     */
    public boolean isEmpty() { 
      return list.isEmptyVary(); 
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
