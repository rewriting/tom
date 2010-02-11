
package tom.donnees.types.sequent;


public abstract class List extends tom.donnees.types.Sequent implements java.util.Collection<tom.donnees.types.Formule>  {


  /** 
   * Returns the number of arguments of the variadic operator
   * 
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof tom.donnees.types.sequent.ConsList) {
      tom.donnees.types.Sequent tl = this.getTailList();
      if (tl instanceof List) {
        return 1+((List)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static tom.donnees.types.Sequent fromArray(tom.donnees.types.Formule[] array) {
    tom.donnees.types.Sequent res = tom.donnees.types.sequent.EmptyList.make();
    for(int i = array.length; i>0;) {
      res = tom.donnees.types.sequent.ConsList.make(array[--i],res);
    }
    return res;
  }

  /** 
   * Inverses the term if it is a list
   * 
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public tom.donnees.types.Sequent reverse() {
    if(this instanceof tom.donnees.types.sequent.ConsList) {
      tom.donnees.types.Sequent cur = this;
      tom.donnees.types.Sequent rev = tom.donnees.types.sequent.EmptyList.make();
      while(cur instanceof tom.donnees.types.sequent.ConsList) {
        rev = tom.donnees.types.sequent.ConsList.make(cur.getHeadList(),rev);
        cur = cur.getTailList();
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
  public tom.donnees.types.Sequent append(tom.donnees.types.Formule element) {
    if(this instanceof tom.donnees.types.sequent.ConsList) {
      tom.donnees.types.Sequent tl = this.getTailList();
      if (tl instanceof List) {
        return tom.donnees.types.sequent.ConsList.make(this.getHeadList(),((List)tl).append(element));
      } else {

        return tom.donnees.types.sequent.ConsList.make(this.getHeadList(),tom.donnees.types.sequent.ConsList.make(element,tl));

      }
    } else {
      return tom.donnees.types.sequent.ConsList.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("List(");
    if(this instanceof tom.donnees.types.sequent.ConsList) {
      tom.donnees.types.Sequent cur = this;
      while(cur instanceof tom.donnees.types.sequent.ConsList) {
        tom.donnees.types.Formule elem = cur.getHeadList();
        cur = cur.getTailList();
        elem.toStringBuilder(buffer);

        if(cur instanceof tom.donnees.types.sequent.ConsList) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof tom.donnees.types.sequent.EmptyList)) {
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
    if(this instanceof tom.donnees.types.sequent.ConsList) {
      tom.donnees.types.Sequent tail = this.getTailList();
      res = atermFactory.makeList(getHeadList().toATerm(),(aterm.ATermList)tail.toATerm());
    } 
    return res;
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a tom.donnees.types.Sequent from it
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static tom.donnees.types.Sequent fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("List".equals(appl.getName())) {
        tom.donnees.types.Sequent res = tom.donnees.types.sequent.EmptyList.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          tom.donnees.types.Formule elem = tom.donnees.types.Formule.fromTerm(array[i],atConv);
          res = tom.donnees.types.sequent.ConsList.make(elem,res);
        }
        return res;
      }
    }

    if(trm instanceof aterm.ATermList) {
      aterm.ATermList list = (aterm.ATermList) trm;
      tom.donnees.types.Sequent res = tom.donnees.types.sequent.EmptyList.make();
      try {
        while(!list.isEmpty()) {
          tom.donnees.types.Formule elem = tom.donnees.types.Formule.fromTerm(list.getFirst(),atConv);
          res = tom.donnees.types.sequent.ConsList.make(elem,res);
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
   * Checks if tom.donnees.types.Sequent contains a specified object
   * 
   * @param o object whose presence is tested
   * @return true if tom.donnees.types.Sequent contains the object, otherwise false
   */
  public boolean contains(Object o) {
    tom.donnees.types.Sequent cur = this;
    if(o==null) { return false; }
    if(cur instanceof tom.donnees.types.sequent.ConsList) {
      while(cur instanceof tom.donnees.types.sequent.ConsList) {
        if( o.equals(cur.getHeadList()) ) {
          return true;
        }
        cur = cur.getTailList();
      }
      if(!(cur instanceof tom.donnees.types.sequent.EmptyList)) { 
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
  public boolean isEmpty() { return isEmptyList() ; }

  public java.util.Iterator<tom.donnees.types.Formule> iterator() {
    return new java.util.Iterator<tom.donnees.types.Formule>() {
      tom.donnees.types.Sequent list = List.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyList();
      }

      public tom.donnees.types.Formule next() {
        if(list.isEmptyList()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsList()) {
          tom.donnees.types.Formule head = list.getHeadList();
          list = list.getTailList();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (tom.donnees.types.Formule)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(tom.donnees.types.Formule o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends tom.donnees.types.Formule> c) {
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
    if(this instanceof tom.donnees.types.sequent.ConsList) {
      tom.donnees.types.Sequent cur = this;
      while(cur instanceof tom.donnees.types.sequent.ConsList) {
        tom.donnees.types.Formule elem = cur.getHeadList();
        array[i] = elem;
        cur = cur.getTailList();
        i++;
      }
      if(!(cur instanceof tom.donnees.types.sequent.EmptyList)) {
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
    if(this instanceof tom.donnees.types.sequent.ConsList) {
      tom.donnees.types.Sequent cur = this;
      while(cur instanceof tom.donnees.types.sequent.ConsList) {
        tom.donnees.types.Formule elem = cur.getHeadList();
        array[i] = (T)elem;
        cur = cur.getTailList();
        i++;
      }
      if(!(cur instanceof tom.donnees.types.sequent.EmptyList)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<tom.donnees.types.Formule> getCollection() {
    return new CollectionList(this);
  }

  public java.util.Collection<tom.donnees.types.Formule> getCollectionList() {
    return new CollectionList(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionList implements java.util.Collection<tom.donnees.types.Formule> {
    private List list;

    public List getSequent() {
      return list; 
    }

    public CollectionList(List list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends tom.donnees.types.Formule> c) {
    boolean modified = false;
    java.util.Iterator<? extends tom.donnees.types.Formule> it = c.iterator();
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
    return getSequent().contains(o);
  }

  /** 
   * Checks if the collection contains elements given as parameter
   * 
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getSequent().containsAll(c);
  }

  /** 
   * Checks if an object is equal
   * 
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) { 
    return getSequent().equals(o); 
  }

  /** 
   * Returns the hashCode
   * 
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getSequent().hashCode(); 
  }

  /** 
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<tom.donnees.types.Formule> iterator() {
    return getSequent().iterator();
  }

  /** 
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() { 
    return getSequent().size(); 
  }

  /** 
   * Returns an array containing all of the elements in this collection.
   * 
   * @return an array of elements
   */
  public Object[] toArray() {
    return getSequent().toArray();
  }

  /** 
   * Returns an array containing all of the elements in this collection.
   * 
   * @param array array which will contain the result
   * @return an array of elements
   */
  public <T> T[] toArray(T[] array) {
    return getSequent().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getSequent().length();
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
    public boolean add(tom.donnees.types.Formule o) {
      list = (List)tom.donnees.types.sequent.ConsList.make(o,list);
      return true;
    }

    /** 
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (List)tom.donnees.types.sequent.EmptyList.make();
    }

    /** 
     * Tests the emptiness of the collection
     * 
     * @return true if the collection is empty
     */
    public boolean isEmpty() { 
      return list.isEmptyList(); 
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
