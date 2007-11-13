
package polygraphicprogram.types.threepath;

public abstract class ThreeC2 extends polygraphicprogram.types.ThreePath implements java.util.Collection<polygraphicprogram.types.ThreePath>  {


  @Override
  public int length() {
    if(this instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
      polygraphicprogram.types.ThreePath tl = ((polygraphicprogram.types.threepath.ConsThreeC2)this).getTailThreeC2();
      if (tl instanceof ThreeC2) {
        return 1+((ThreeC2)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
    }


  public static polygraphicprogram.types.ThreePath fromArray(polygraphicprogram.types.ThreePath[] array) {
    polygraphicprogram.types.ThreePath res = polygraphicprogram.types.threepath.EmptyThreeC2.make();
    for(int i = array.length; i>0;) {
      res = polygraphicprogram.types.threepath.ConsThreeC2.make(array[--i],res);
    }
    return res;
  }

  @Override
  public polygraphicprogram.types.ThreePath reverse() {
    if(this instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
      polygraphicprogram.types.ThreePath cur = this;
      polygraphicprogram.types.ThreePath rev = polygraphicprogram.types.threepath.EmptyThreeC2.make();
      while(cur instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
        rev = polygraphicprogram.types.threepath.ConsThreeC2.make(((polygraphicprogram.types.threepath.ConsThreeC2)cur).getHeadThreeC2(),rev);
        cur = ((polygraphicprogram.types.threepath.ConsThreeC2)cur).getTailThreeC2();
      }
      return rev;
    } else {
      return this;
    }
  }

  public polygraphicprogram.types.ThreePath append(polygraphicprogram.types.ThreePath element) {
    if(this instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
      polygraphicprogram.types.ThreePath tl = ((polygraphicprogram.types.threepath.ConsThreeC2)this).getTailThreeC2();
      if (tl instanceof ThreeC2) {
        return polygraphicprogram.types.threepath.ConsThreeC2.make(this.getHeadThreeC2(),((ThreeC2)tl).append(element));
      } else {

        return polygraphicprogram.types.threepath.ConsThreeC2.make(this.getHeadThreeC2(),polygraphicprogram.types.threepath.ConsThreeC2.make(element,tl));

      }
    } else {
      return polygraphicprogram.types.threepath.ConsThreeC2.make(element,this);
    }
  }

  @Override
  public void toStringBuffer(java.lang.StringBuffer buffer) {
    buffer.append("ThreeC2(");
    if(this instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
      polygraphicprogram.types.ThreePath cur = this;
      while(cur instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
        polygraphicprogram.types.ThreePath elem = ((polygraphicprogram.types.threepath.ConsThreeC2)cur).getHeadThreeC2();
        cur = ((polygraphicprogram.types.threepath.ConsThreeC2)cur).getTailThreeC2();
        elem.toStringBuffer(buffer);

        if(cur instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof polygraphicprogram.types.threepath.EmptyThreeC2)) {
        buffer.append(",");
        cur.toStringBuffer(buffer);
      }
    }
    buffer.append(")");
  }

  public static polygraphicprogram.types.ThreePath fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("ThreeC2".equals(appl.getName())) {
        polygraphicprogram.types.ThreePath res = polygraphicprogram.types.threepath.EmptyThreeC2.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          polygraphicprogram.types.ThreePath elem = polygraphicprogram.types.ThreePath.fromTerm(array[i]);
          res = polygraphicprogram.types.threepath.ConsThreeC2.make(elem,res);
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
    polygraphicprogram.types.ThreePath cur = this;
    if(o==null) { return false; }
    if(cur instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
      while(cur instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
        if( o.equals(((polygraphicprogram.types.threepath.ConsThreeC2)cur).getHeadThreeC2()) ) {
          return true;
        }
        cur = ((polygraphicprogram.types.threepath.ConsThreeC2)cur).getTailThreeC2();
      }
    }
    return false;
  }

  //public boolean equals(Object o) { return this == o; }

  //public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return isEmptyThreeC2() ; }

  public java.util.Iterator<polygraphicprogram.types.ThreePath> iterator() {
    return new java.util.Iterator<polygraphicprogram.types.ThreePath>() {
      polygraphicprogram.types.ThreePath list = ThreeC2.this;

      public boolean hasNext() {
        return list.isConsThreeC2();
      }

      public polygraphicprogram.types.ThreePath next() {
        if(list.isEmptyThreeC2()) {
          throw new java.util.NoSuchElementException();
        }
        polygraphicprogram.types.ThreePath head = ((polygraphicprogram.types.threepath.ConsThreeC2)list).getHeadThreeC2();
        list = ((polygraphicprogram.types.threepath.ConsThreeC2)list).getTailThreeC2();
        return head;
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(polygraphicprogram.types.ThreePath o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }


  public boolean addAll(java.util.Collection<? extends polygraphicprogram.types.ThreePath> c) {
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
    if(this instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
      polygraphicprogram.types.ThreePath cur = this;
      while(cur instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
        polygraphicprogram.types.ThreePath elem = ((polygraphicprogram.types.threepath.ConsThreeC2)cur).getHeadThreeC2();
        array[i] = elem;
        cur = ((polygraphicprogram.types.threepath.ConsThreeC2)cur).getTailThreeC2();
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
    if(this instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
      polygraphicprogram.types.ThreePath cur = this;
      while(cur instanceof polygraphicprogram.types.threepath.ConsThreeC2) {
        polygraphicprogram.types.ThreePath elem = ((polygraphicprogram.types.threepath.ConsThreeC2)cur).getHeadThreeC2();
        array[i] = (T)elem;
        cur = ((polygraphicprogram.types.threepath.ConsThreeC2)cur).getTailThreeC2();
        i++;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<polygraphicprogram.types.ThreePath> getCollection() {
    return new CollectionThreeC2(this);
  }

  public java.util.Collection<polygraphicprogram.types.ThreePath> getCollectionThreeC2() {
    return new CollectionThreeC2(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionThreeC2 implements java.util.Collection<polygraphicprogram.types.ThreePath> {
    private ThreeC2 list;

    public ThreeC2 getThreePath() {
      return list; 
    }

    public CollectionThreeC2(ThreeC2 list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends polygraphicprogram.types.ThreePath> c) {
    boolean modified = false;
    java.util.Iterator<? extends polygraphicprogram.types.ThreePath> it = c.iterator();
    while(it.hasNext()) {
      modified = modified || add(it.next());
    }
    return modified;
  }

  public boolean contains(Object o) {
    return getThreePath().contains(o);
  }

  public boolean containsAll(java.util.Collection<?> c) {
    return getThreePath().containsAll(c);
  }

  @Override
  public boolean equals(Object o) { 
    return getThreePath().equals(o); 
  }

  @Override
  public int hashCode() {
    return getThreePath().hashCode(); 
  }

  public java.util.Iterator<polygraphicprogram.types.ThreePath> iterator() {
    return getThreePath().iterator();
  }

  public int size() { 
    return getThreePath().size(); 
  }

  public Object[] toArray() {
    return getThreePath().toArray();
  }

  public <T> T[] toArray(T[] array) {
    return getThreePath().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getThreePath().length();
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

    public boolean add(polygraphicprogram.types.ThreePath o) {
      list = (ThreeC2)polygraphicprogram.types.threepath.ConsThreeC2.make(o,list);
      return true;
    }

    public void clear() {
      list = (ThreeC2)polygraphicprogram.types.threepath.EmptyThreeC2.make();
    }

    public boolean isEmpty() { 
      return list.isEmptyThreeC2(); 
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
