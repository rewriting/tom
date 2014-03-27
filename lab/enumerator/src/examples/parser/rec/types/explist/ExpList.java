
package examples.parser.rec.types.explist;



public abstract class ExpList extends examples.parser.rec.types.ExpList implements java.util.Collection<examples.parser.rec.types.Exp>  {


  /**
   * Returns the number of arguments of the variadic operator
   *
   * @return the number of arguments of the variadic operator
   */
  @Override
  public int length() {
    if(this instanceof examples.parser.rec.types.explist.ConsExpList) {
      examples.parser.rec.types.ExpList tl = this.getTailExpList();
      if (tl instanceof ExpList) {
        return 1+((ExpList)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static examples.parser.rec.types.ExpList fromArray(examples.parser.rec.types.Exp[] array) {
    examples.parser.rec.types.ExpList res = examples.parser.rec.types.explist.EmptyExpList.make();
    for(int i = array.length; i>0;) {
      res = examples.parser.rec.types.explist.ConsExpList.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @return the inverted term if it is a list, otherwise the term itself
   */
  @Override
  public examples.parser.rec.types.ExpList reverse() {
    if(this instanceof examples.parser.rec.types.explist.ConsExpList) {
      examples.parser.rec.types.ExpList cur = this;
      examples.parser.rec.types.ExpList rev = examples.parser.rec.types.explist.EmptyExpList.make();
      while(cur instanceof examples.parser.rec.types.explist.ConsExpList) {
        rev = examples.parser.rec.types.explist.ConsExpList.make(cur.getHeadExpList(),rev);
        cur = cur.getTailExpList();
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
  public examples.parser.rec.types.ExpList append(examples.parser.rec.types.Exp element) {
    if(this instanceof examples.parser.rec.types.explist.ConsExpList) {
      examples.parser.rec.types.ExpList tl = this.getTailExpList();
      if (tl instanceof ExpList) {
        return examples.parser.rec.types.explist.ConsExpList.make(this.getHeadExpList(),((ExpList)tl).append(element));
      } else {

        return examples.parser.rec.types.explist.ConsExpList.make(this.getHeadExpList(),examples.parser.rec.types.explist.ConsExpList.make(element,tl));

      }
    } else {
      return examples.parser.rec.types.explist.ConsExpList.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("ExpList(");
    if(this instanceof examples.parser.rec.types.explist.ConsExpList) {
      examples.parser.rec.types.ExpList cur = this;
      while(cur instanceof examples.parser.rec.types.explist.ConsExpList) {
        examples.parser.rec.types.Exp elem = cur.getHeadExpList();
        cur = cur.getTailExpList();
        elem.toStringBuilder(buffer);

        if(cur instanceof examples.parser.rec.types.explist.ConsExpList) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof examples.parser.rec.types.explist.EmptyExpList)) {
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
    if(this instanceof examples.parser.rec.types.explist.ConsExpList) {
      examples.parser.rec.types.ExpList tail = this.getTailExpList();
      res = atermFactory.makeList(getHeadExpList().toATerm(),(aterm.ATermList)tail.toATerm());
    }
    return res;
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.ExpList from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.ExpList fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("ExpList".equals(appl.getName())) {
        examples.parser.rec.types.ExpList res = examples.parser.rec.types.explist.EmptyExpList.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          examples.parser.rec.types.Exp elem = examples.parser.rec.types.Exp.fromTerm(array[i],atConv);
          res = examples.parser.rec.types.explist.ConsExpList.make(elem,res);
        }
        return res;
      }
    }

    if(trm instanceof aterm.ATermList) {
      aterm.ATermList list = (aterm.ATermList) trm;
      examples.parser.rec.types.ExpList res = examples.parser.rec.types.explist.EmptyExpList.make();
      try {
        while(!list.isEmpty()) {
          examples.parser.rec.types.Exp elem = examples.parser.rec.types.Exp.fromTerm(list.getFirst(),atConv);
          res = examples.parser.rec.types.explist.ConsExpList.make(elem,res);
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
   * Checks if parser.rec.types.ExpList contains a specified object
   *
   * @param o object whose presence is tested
   * @return true if parser.rec.types.ExpList contains the object, otherwise false
   */
  public boolean contains(Object o) {
    examples.parser.rec.types.ExpList cur = this;
    if(o==null) { return false; }
    if(cur instanceof examples.parser.rec.types.explist.ConsExpList) {
      while(cur instanceof examples.parser.rec.types.explist.ConsExpList) {
        if( o.equals(cur.getHeadExpList()) ) {
          return true;
        }
        cur = cur.getTailExpList();
      }
      if(!(cur instanceof examples.parser.rec.types.explist.EmptyExpList)) {
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
  public boolean isEmpty() { return isEmptyExpList() ; }

  public java.util.Iterator<examples.parser.rec.types.Exp> iterator() {
    return new java.util.Iterator<examples.parser.rec.types.Exp>() {
      examples.parser.rec.types.ExpList list = ExpList.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyExpList();
      }

      public examples.parser.rec.types.Exp next() {
        if(list.isEmptyExpList()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsExpList()) {
          examples.parser.rec.types.Exp head = list.getHeadExpList();
          list = list.getTailExpList();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (examples.parser.rec.types.Exp)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(examples.parser.rec.types.Exp o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends examples.parser.rec.types.Exp> c) {
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
    if(this instanceof examples.parser.rec.types.explist.ConsExpList) {
      examples.parser.rec.types.ExpList cur = this;
      while(cur instanceof examples.parser.rec.types.explist.ConsExpList) {
        examples.parser.rec.types.Exp elem = cur.getHeadExpList();
        array[i] = elem;
        cur = cur.getTailExpList();
        i++;
      }
      if(!(cur instanceof examples.parser.rec.types.explist.EmptyExpList)) {
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
    if(this instanceof examples.parser.rec.types.explist.ConsExpList) {
      examples.parser.rec.types.ExpList cur = this;
      while(cur instanceof examples.parser.rec.types.explist.ConsExpList) {
        examples.parser.rec.types.Exp elem = cur.getHeadExpList();
        array[i] = (T)elem;
        cur = cur.getTailExpList();
        i++;
      }
      if(!(cur instanceof examples.parser.rec.types.explist.EmptyExpList)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<examples.parser.rec.types.Exp> getCollection() {
    return new CollectionExpList(this);
  }

  public java.util.Collection<examples.parser.rec.types.Exp> getCollectionExpList() {
    return new CollectionExpList(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionExpList implements java.util.Collection<examples.parser.rec.types.Exp> {
    private ExpList list;

    public ExpList getExpList() {
      return list;
    }

    public CollectionExpList(ExpList list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends examples.parser.rec.types.Exp> c) {
    boolean modified = false;
    java.util.Iterator<? extends examples.parser.rec.types.Exp> it = c.iterator();
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
    return getExpList().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @param c elements whose presence has to be checked
   * @return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return getExpList().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @param o object which is compared
   * @return true if objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return getExpList().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @return the hashCode
   */
  @Override
  public int hashCode() {
    return getExpList().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @return an iterator over the elements in the collection
   */
  public java.util.Iterator<examples.parser.rec.types.Exp> iterator() {
    return getExpList().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @return the size of the collection
   */
  public int size() {
    return getExpList().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array of elements
   */
  public Object[] toArray() {
    return getExpList().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @param array array which will contain the result
   * @return an array of elements
   */
  public <T> T[] toArray(T[] array) {
    return getExpList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getExpList().length();
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
    public boolean add(examples.parser.rec.types.Exp o) {
      list = (ExpList)examples.parser.rec.types.explist.ConsExpList.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (ExpList)examples.parser.rec.types.explist.EmptyExpList.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmptyExpList();
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
