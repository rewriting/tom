
package polygraphicprogram.types.twopath;

public abstract class TwoC0 extends polygraphicprogram.types.TwoPath implements java.util.Collection<polygraphicprogram.types.TwoPath>  {


  @Override
  public int length() {
    if(this instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
      polygraphicprogram.types.TwoPath tl = ((polygraphicprogram.types.twopath.ConsTwoC0)this).getTailTwoC0();
      if (tl instanceof TwoC0) {
        return 1+((TwoC0)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
    }


  public static polygraphicprogram.types.TwoPath fromArray(polygraphicprogram.types.TwoPath[] array) {
    polygraphicprogram.types.TwoPath res = polygraphicprogram.types.twopath.EmptyTwoC0.make();
    for(int i = array.length; i>0;) {
      res = polygraphicprogram.types.twopath.ConsTwoC0.make(array[--i],res);
    }
    return res;
  }

  @Override
  public polygraphicprogram.types.TwoPath reverse() {
    if(this instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
      polygraphicprogram.types.TwoPath cur = this;
      polygraphicprogram.types.TwoPath rev = polygraphicprogram.types.twopath.EmptyTwoC0.make();
      while(cur instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
        rev = polygraphicprogram.types.twopath.ConsTwoC0.make(((polygraphicprogram.types.twopath.ConsTwoC0)cur).getHeadTwoC0(),rev);
        cur = ((polygraphicprogram.types.twopath.ConsTwoC0)cur).getTailTwoC0();
      }
      return rev;
    } else {
      return this;
    }
  }

  public polygraphicprogram.types.TwoPath append(polygraphicprogram.types.TwoPath element) {
    if(this instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
      polygraphicprogram.types.TwoPath tl = ((polygraphicprogram.types.twopath.ConsTwoC0)this).getTailTwoC0();
      if (tl instanceof TwoC0) {
        return polygraphicprogram.types.twopath.ConsTwoC0.make(this.getHeadTwoC0(),((TwoC0)tl).append(element));
      } else {

        return polygraphicprogram.types.twopath.ConsTwoC0.make(this.getHeadTwoC0(),polygraphicprogram.types.twopath.ConsTwoC0.make(element,tl));

      }
    } else {
      return polygraphicprogram.types.twopath.ConsTwoC0.make(element,this);
    }
  }

  @Override
  public void toStringBuffer(java.lang.StringBuffer buffer) {
    buffer.append("TwoC0(");
    if(this instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
      polygraphicprogram.types.TwoPath cur = this;
      while(cur instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
        polygraphicprogram.types.TwoPath elem = ((polygraphicprogram.types.twopath.ConsTwoC0)cur).getHeadTwoC0();
        cur = ((polygraphicprogram.types.twopath.ConsTwoC0)cur).getTailTwoC0();
        elem.toStringBuffer(buffer);

        if(cur instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof polygraphicprogram.types.twopath.EmptyTwoC0)) {
        buffer.append(",");
        cur.toStringBuffer(buffer);
      }
    }
    buffer.append(")");
  }

  public static polygraphicprogram.types.TwoPath fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("TwoC0".equals(appl.getName())) {
        polygraphicprogram.types.TwoPath res = polygraphicprogram.types.twopath.EmptyTwoC0.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          polygraphicprogram.types.TwoPath elem = polygraphicprogram.types.TwoPath.fromTerm(array[i]);
          res = polygraphicprogram.types.twopath.ConsTwoC0.make(elem,res);
        }
        return res;
      }
    }
    return null;
  }

  /*
   * methods from Collection
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

  public boolean contains(Object o) {
    polygraphicprogram.types.TwoPath cur = this;
    if(o==null) { return false; }
    if(cur instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
      while(cur instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
        if( o.equals(((polygraphicprogram.types.twopath.ConsTwoC0)cur).getHeadTwoC0()) ) {
          return true;
        }
        cur = ((polygraphicprogram.types.twopath.ConsTwoC0)cur).getTailTwoC0();
      }
    }
    return false;
  }

  //public boolean equals(Object o) { return this == o; }

  //public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return isEmptyTwoC0() ; }

  public java.util.Iterator<polygraphicprogram.types.TwoPath> iterator() {
    return new java.util.Iterator<polygraphicprogram.types.TwoPath>() {
      polygraphicprogram.types.TwoPath list = TwoC0.this;

      public boolean hasNext() {
        return list.isConsTwoC0();
      }

      public polygraphicprogram.types.TwoPath next() {
        if(list.isEmptyTwoC0()) {
          throw new java.util.NoSuchElementException();
        }
        polygraphicprogram.types.TwoPath head = ((polygraphicprogram.types.twopath.ConsTwoC0)list).getHeadTwoC0();
        list = ((polygraphicprogram.types.twopath.ConsTwoC0)list).getTailTwoC0();
        return head;
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(polygraphicprogram.types.TwoPath o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }


  public boolean addAll(java.util.Collection<? extends polygraphicprogram.types.TwoPath> c) {
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

  public int size() { return length(); }

  public Object[] toArray() {
    int size = this.length();
    Object[] array = new Object[size];
    int i=0;
    if(this instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
      polygraphicprogram.types.TwoPath cur = this;
      while(cur instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
        polygraphicprogram.types.TwoPath elem = ((polygraphicprogram.types.twopath.ConsTwoC0)cur).getHeadTwoC0();
        array[i] = elem;
        cur = ((polygraphicprogram.types.twopath.ConsTwoC0)cur).getTailTwoC0();
        i++;
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
    if(this instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
      polygraphicprogram.types.TwoPath cur = this;
      while(cur instanceof polygraphicprogram.types.twopath.ConsTwoC0) {
        polygraphicprogram.types.TwoPath elem = ((polygraphicprogram.types.twopath.ConsTwoC0)cur).getHeadTwoC0();
        array[i] = (T)elem;
        cur = ((polygraphicprogram.types.twopath.ConsTwoC0)cur).getTailTwoC0();
        i++;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<polygraphicprogram.types.TwoPath> getCollection() {
    return new CollectionTwoC0(this);
  }

  public java.util.Collection<polygraphicprogram.types.TwoPath> getCollectionTwoC0() {
    return new CollectionTwoC0(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionTwoC0 implements java.util.Collection<polygraphicprogram.types.TwoPath> {
    private TwoC0 list;

    public TwoC0 getTwoPath() {
      return list; 
    }

    public CollectionTwoC0(TwoC0 list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends polygraphicprogram.types.TwoPath> c) {
    boolean modified = false;
    java.util.Iterator<? extends polygraphicprogram.types.TwoPath> it = c.iterator();
    while(it.hasNext()) {
      modified = modified || add(it.next());
    }
    return modified;
  }

  public boolean contains(Object o) {
    return getTwoPath().contains(o);
  }

  public boolean containsAll(java.util.Collection<?> c) {
    return getTwoPath().containsAll(c);
  }

  @Override
  public boolean equals(Object o) { 
    return getTwoPath().equals(o); 
  }

  @Override
  public int hashCode() {
    return getTwoPath().hashCode(); 
  }

  public java.util.Iterator<polygraphicprogram.types.TwoPath> iterator() {
    return getTwoPath().iterator();
  }

  public int size() { 
    return getTwoPath().size(); 
  }

  public Object[] toArray() {
    return getTwoPath().toArray();
  }

  public <T> T[] toArray(T[] array) {
    return getTwoPath().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getTwoPath().length();
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

    public boolean add(polygraphicprogram.types.TwoPath o) {
      list = (TwoC0)polygraphicprogram.types.twopath.ConsTwoC0.make(o,list);
      return true;
    }

    public void clear() {
      list = (TwoC0)polygraphicprogram.types.twopath.EmptyTwoC0.make();
    }

    public boolean isEmpty() { 
      return list.isEmptyTwoC0(); 
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
