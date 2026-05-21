
package gom.list.types.list;


public abstract class conc extends gom.list.types.List implements java.util.Collection<java.lang.Integer>  {


  /** 
   * Returns the number of arguments of the variadic operator
   * 
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof gom.list.types.list.Consconc) {
      gom.list.types.List tl = this.getTailconc();
      if (tl instanceof conc) {
        return 1+((conc)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static gom.list.types.List fromArray(int[] array) {
    gom.list.types.List res = gom.list.types.list.Emptyconc.make();
    for(int i = array.length; i>0;) {
      res = gom.list.types.list.Consconc.make(array[--i],res);
    }
    return res;
  }

  /** 
   * Inverses the term if it is a list
   * 
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public gom.list.types.List reverse() {
    if(this instanceof gom.list.types.list.Consconc) {
      gom.list.types.List cur = this;
      gom.list.types.List rev = gom.list.types.list.Emptyconc.make();
      while(cur instanceof gom.list.types.list.Consconc) {
        rev = gom.list.types.list.Consconc.make(cur.getHeadconc(),rev);
        cur = cur.getTailconc();
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
  public gom.list.types.List append(int element) {
    if(this instanceof gom.list.types.list.Consconc) {
      gom.list.types.List tl = this.getTailconc();
      if (tl instanceof conc) {
        return gom.list.types.list.Consconc.make(this.getHeadconc(),((conc)tl).append(element));
      } else {

        return gom.list.types.list.Consconc.make(this.getHeadconc(),gom.list.types.list.Consconc.make(element,tl));

      }
    } else {
      return gom.list.types.list.Consconc.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("conc(");
    if(this instanceof gom.list.types.list.Consconc) {
      gom.list.types.List cur = this;
      while(cur instanceof gom.list.types.list.Consconc) {
        int elem = cur.getHeadconc();
        cur = cur.getTailconc();
        buffer.append(elem);

        if(cur instanceof gom.list.types.list.Consconc) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof gom.list.types.list.Emptyconc)) {
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
    if(this instanceof gom.list.types.list.Consconc) {
      gom.list.types.List tail = this.getTailconc();
      res = atermFactory.makeList((aterm.ATerm) atermFactory.makeInt(getHeadconc()),(aterm.ATermList)tail.toATerm());
    } 
    return res;
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a gom.list.types.List from it
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static gom.list.types.List fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("conc".equals(appl.getName())) {
        gom.list.types.List res = gom.list.types.list.Emptyconc.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          int elem = convertATermToInt(array[i], atConv);
          res = gom.list.types.list.Consconc.make(elem,res);
        }
        return res;
      }
    }

    if(trm instanceof aterm.ATermList) {
      aterm.ATermList list = (aterm.ATermList) trm;
      gom.list.types.List res = gom.list.types.list.Emptyconc.make();
      try {
        while(!list.isEmpty()) {
          int elem = convertATermToInt(list.getFirst(), atConv);
          res = gom.list.types.list.Consconc.make(elem,res);
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
   * Checks if gom.list.types.List contains a specified object
   * 
   * @param o object whose presence is tested
   * @return true if gom.list.types.List contains the object, otherwise false
   */
  public boolean contains(Object o) {
    gom.list.types.List cur = this;
    if(o==null) { return false; }
    if(cur instanceof gom.list.types.list.Consconc) {
      while(cur instanceof gom.list.types.list.Consconc) {
        if( o.equals(cur.getHeadconc()) ) {
          return true;
        }
        cur = cur.getTailconc();
      }
      if(!(cur instanceof gom.list.types.list.Emptyconc)) { 
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
  public boolean isEmpty() { return isEmptyconc() ; }

  public java.util.Iterator<java.lang.Integer> iterator() {
    return new java.util.Iterator<java.lang.Integer>() {
      gom.list.types.List list = conc.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyconc();
      }

      public java.lang.Integer next() {
        if(list.isEmptyconc()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsconc()) {
          java.lang.Integer head = list.getHeadconc();
          list = list.getTailconc();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (java.lang.Integer)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(java.lang.Integer o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends java.lang.Integer> c) {
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
    if(this instanceof gom.list.types.list.Consconc) {
      gom.list.types.List cur = this;
      while(cur instanceof gom.list.types.list.Consconc) {
        java.lang.Integer elem = cur.getHeadconc();
        array[i] = elem;
        cur = cur.getTailconc();
        i++;
      }
      if(!(cur instanceof gom.list.types.list.Emptyconc)) {
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
    if(this instanceof gom.list.types.list.Consconc) {
      gom.list.types.List cur = this;
      while(cur instanceof gom.list.types.list.Consconc) {
        java.lang.Integer elem = cur.getHeadconc();
        array[i] = (T)elem;
        cur = cur.getTailconc();
        i++;
      }
      if(!(cur instanceof gom.list.types.list.Emptyconc)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<java.lang.Integer> getCollection() {
    return new Collectionconc(this);
  }

  public java.util.Collection<java.lang.Integer> getCollectionconc() {
    return new Collectionconc(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class Collectionconc implements java.util.Collection<java.lang.Integer> {
    private conc list;

    public conc getList() {
      return list; 
    }

    public Collectionconc(conc list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends java.lang.Integer> c) {
    boolean modified = false;
    java.util.Iterator<? extends java.lang.Integer> it = c.iterator();
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
    return getList().contains(o);
  }

  /** 
   * Checks if the collection contains elements given as parameter
   * 
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getList().containsAll(c);
  }

  /** 
   * Checks if an object is equal
   * 
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) { 
    return getList().equals(o); 
  }

  /** 
   * Returns the hashCode
   * 
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getList().hashCode(); 
  }

  /** 
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<java.lang.Integer> iterator() {
    return getList().iterator();
  }

  /** 
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() { 
    return getList().size(); 
  }

  /** 
   * Returns an array containing all of the elements in this collection.
   * 
   * @return an array of elements
   */
  public Object[] toArray() {
    return getList().toArray();
  }

  /** 
   * Returns an array containing all of the elements in this collection.
   * 
   * @param array array which will contain the result
   * @return an array of elements
   */
  public <T> T[] toArray(T[] array) {
    return getList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getList().length();
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
    public boolean add(java.lang.Integer o) {
      list = (conc)gom.list.types.list.Consconc.make(o,list);
      return true;
    }

    /** 
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (conc)gom.list.types.list.Emptyconc.make();
    }

    /** 
     * Tests the emptiness of the collection
     * 
     * @return true if the collection is empty
     */
    public boolean isEmpty() { 
      return list.isEmptyconc(); 
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
