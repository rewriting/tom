
package examples.parser.rec.types.stm;



public abstract class Seq extends examples.parser.rec.types.Stm implements java.util.Collection<examples.parser.rec.types.Stm>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof examples.parser.rec.types.stm.ConsSeq) {
      examples.parser.rec.types.Stm tl = this.getTailSeq();
      if (tl instanceof Seq) {
        return 1+((Seq)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static examples.parser.rec.types.Stm fromArray(examples.parser.rec.types.Stm[] array) {
    examples.parser.rec.types.Stm res = examples.parser.rec.types.stm.EmptySeq.make();
    for(int i = array.length; i>0;) {
      res = examples.parser.rec.types.stm.ConsSeq.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public examples.parser.rec.types.Stm reverse() {
    if(this instanceof examples.parser.rec.types.stm.ConsSeq) {
      examples.parser.rec.types.Stm cur = this;
      examples.parser.rec.types.Stm rev = examples.parser.rec.types.stm.EmptySeq.make();
      while(cur instanceof examples.parser.rec.types.stm.ConsSeq) {
        rev = examples.parser.rec.types.stm.ConsSeq.make(cur.getHeadSeq(),rev);
        cur = cur.getTailSeq();
      }

      if(!(cur instanceof examples.parser.rec.types.stm.EmptySeq)) {
        rev = examples.parser.rec.types.stm.ConsSeq.make(cur,rev);
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
  public examples.parser.rec.types.Stm append(examples.parser.rec.types.Stm element) {
    if(this instanceof examples.parser.rec.types.stm.ConsSeq) {
      examples.parser.rec.types.Stm tl = this.getTailSeq();
      if (tl instanceof Seq) {
        return examples.parser.rec.types.stm.ConsSeq.make(this.getHeadSeq(),((Seq)tl).append(element));
      } else {

        return examples.parser.rec.types.stm.ConsSeq.make(this.getHeadSeq(),examples.parser.rec.types.stm.ConsSeq.make(tl,element));

      }
    } else {
      return examples.parser.rec.types.stm.ConsSeq.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Seq(");
    if(this instanceof examples.parser.rec.types.stm.ConsSeq) {
      examples.parser.rec.types.Stm cur = this;
      while(cur instanceof examples.parser.rec.types.stm.ConsSeq) {
        examples.parser.rec.types.Stm elem = cur.getHeadSeq();
        cur = cur.getTailSeq();
        elem.toStringBuilder(buffer);

        if(cur instanceof examples.parser.rec.types.stm.ConsSeq) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof examples.parser.rec.types.stm.EmptySeq)) {
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
    if(this instanceof examples.parser.rec.types.stm.ConsSeq) {
      examples.parser.rec.types.Stm tail = this.getTailSeq();
      res = atermFactory.makeList(getHeadSeq().toATerm(),(aterm.ATermList)tail.toATerm());
    }
    return res;
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Stm from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Stm fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("Seq".equals(appl.getName())) {
        examples.parser.rec.types.Stm res = examples.parser.rec.types.stm.EmptySeq.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          examples.parser.rec.types.Stm elem = examples.parser.rec.types.Stm.fromTerm(array[i],atConv);
          res = examples.parser.rec.types.stm.ConsSeq.make(elem,res);
        }
        return res;
      }
    }

    if(trm instanceof aterm.ATermList) {
      aterm.ATermList list = (aterm.ATermList) trm;
      examples.parser.rec.types.Stm res = examples.parser.rec.types.stm.EmptySeq.make();
      try {
        while(!list.isEmpty()) {
          examples.parser.rec.types.Stm elem = examples.parser.rec.types.Stm.fromTerm(list.getFirst(),atConv);
          res = examples.parser.rec.types.stm.ConsSeq.make(elem,res);
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
   * Checks if parser.rec.types.Stm contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if parser.rec.types.Stm contains the object, otherwise false
   */
  public boolean contains(Object o) {
    examples.parser.rec.types.Stm cur = this;
    if(o==null) { return false; }
    if(cur instanceof examples.parser.rec.types.stm.ConsSeq) {
      while(cur instanceof examples.parser.rec.types.stm.ConsSeq) {
        if( o.equals(cur.getHeadSeq()) ) {
          return true;
        }
        cur = cur.getTailSeq();
      }
      if(!(cur instanceof examples.parser.rec.types.stm.EmptySeq)) {
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
  public boolean isEmpty() { return isEmptySeq() ; }

  public java.util.Iterator<examples.parser.rec.types.Stm> iterator() {
    return new java.util.Iterator<examples.parser.rec.types.Stm>() {
      examples.parser.rec.types.Stm list = Seq.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptySeq();
      }

      public examples.parser.rec.types.Stm next() {
        if(list.isEmptySeq()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsSeq()) {
          examples.parser.rec.types.Stm head = list.getHeadSeq();
          list = list.getTailSeq();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (examples.parser.rec.types.Stm)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(examples.parser.rec.types.Stm o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends examples.parser.rec.types.Stm> c) {
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
    if(this instanceof examples.parser.rec.types.stm.ConsSeq) {
      examples.parser.rec.types.Stm cur = this;
      while(cur instanceof examples.parser.rec.types.stm.ConsSeq) {
        examples.parser.rec.types.Stm elem = cur.getHeadSeq();
        array[i] = elem;
        cur = cur.getTailSeq();
        i++;
      }
      if(!(cur instanceof examples.parser.rec.types.stm.EmptySeq)) {
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
    if(this instanceof examples.parser.rec.types.stm.ConsSeq) {
      examples.parser.rec.types.Stm cur = this;
      while(cur instanceof examples.parser.rec.types.stm.ConsSeq) {
        examples.parser.rec.types.Stm elem = cur.getHeadSeq();
        array[i] = (T)elem;
        cur = cur.getTailSeq();
        i++;
      }
      if(!(cur instanceof examples.parser.rec.types.stm.EmptySeq)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<examples.parser.rec.types.Stm> getCollection() {
    return new CollectionSeq(this);
  }

  public java.util.Collection<examples.parser.rec.types.Stm> getCollectionSeq() {
    return new CollectionSeq(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionSeq implements java.util.Collection<examples.parser.rec.types.Stm> {
    private Seq list;

    public Seq getStm() {
      return list;
    }

    public CollectionSeq(Seq list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends examples.parser.rec.types.Stm> c) {
    boolean modified = false;
    java.util.Iterator<? extends examples.parser.rec.types.Stm> it = c.iterator();
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
    return getStm().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getStm().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getStm().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getStm().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<examples.parser.rec.types.Stm> iterator() {
    return getStm().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getStm().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getStm().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */
  public <T> T[] toArray(T[] array) {
    return getStm().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getStm().length();
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
    public boolean add(examples.parser.rec.types.Stm o) {
      list = (Seq)examples.parser.rec.types.stm.ConsSeq.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (Seq)examples.parser.rec.types.stm.EmptySeq.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptySeq();
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
