
package polygraphicprogram.types.onepath;

public abstract class OneC0 extends polygraphicprogram.types.OnePath implements java.util.Collection<polygraphicprogram.types.OnePath>  {


  @Override
  public int length() {
    if(this instanceof polygraphicprogram.types.onepath.ConsOneC0) {
      polygraphicprogram.types.OnePath tl = ((polygraphicprogram.types.onepath.ConsOneC0)this).getTailOneC0();
      if (tl instanceof OneC0) {
        return 1+((OneC0)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
    }


  public static polygraphicprogram.types.OnePath fromArray(polygraphicprogram.types.OnePath[] array) {
    polygraphicprogram.types.OnePath res = polygraphicprogram.types.onepath.EmptyOneC0.make();
    for(int i = array.length; i>0;) {
      res = polygraphicprogram.types.onepath.ConsOneC0.make(array[--i],res);
    }
    return res;
  }

  @Override
  public polygraphicprogram.types.OnePath reverse() {
    if(this instanceof polygraphicprogram.types.onepath.ConsOneC0) {
      polygraphicprogram.types.OnePath cur = this;
      polygraphicprogram.types.OnePath rev = polygraphicprogram.types.onepath.EmptyOneC0.make();
      while(cur instanceof polygraphicprogram.types.onepath.ConsOneC0) {
        rev = polygraphicprogram.types.onepath.ConsOneC0.make(((polygraphicprogram.types.onepath.ConsOneC0)cur).getHeadOneC0(),rev);
        cur = ((polygraphicprogram.types.onepath.ConsOneC0)cur).getTailOneC0();
      }
      return rev;
    } else {
      return this;
    }
  }

  public polygraphicprogram.types.OnePath append(polygraphicprogram.types.OnePath element) {
    if(this instanceof polygraphicprogram.types.onepath.ConsOneC0) {
      polygraphicprogram.types.OnePath tl = ((polygraphicprogram.types.onepath.ConsOneC0)this).getTailOneC0();
      if (tl instanceof OneC0) {
        return polygraphicprogram.types.onepath.ConsOneC0.make(this.getHeadOneC0(),((OneC0)tl).append(element));
      } else {

        return polygraphicprogram.types.onepath.ConsOneC0.make(this.getHeadOneC0(),polygraphicprogram.types.onepath.ConsOneC0.make(element,tl));

      }
    } else {
      return polygraphicprogram.types.onepath.ConsOneC0.make(element,this);
    }
  }

  @Override
  public void toStringBuffer(java.lang.StringBuffer buffer) {
    buffer.append("OneC0(");
    if(this instanceof polygraphicprogram.types.onepath.ConsOneC0) {
      polygraphicprogram.types.OnePath cur = this;
      while(cur instanceof polygraphicprogram.types.onepath.ConsOneC0) {
        polygraphicprogram.types.OnePath elem = ((polygraphicprogram.types.onepath.ConsOneC0)cur).getHeadOneC0();
        cur = ((polygraphicprogram.types.onepath.ConsOneC0)cur).getTailOneC0();
        elem.toStringBuffer(buffer);

        if(cur instanceof polygraphicprogram.types.onepath.ConsOneC0) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof polygraphicprogram.types.onepath.EmptyOneC0)) {
        buffer.append(",");
        cur.toStringBuffer(buffer);
      }
    }
    buffer.append(")");
  }

  public static polygraphicprogram.types.OnePath fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("OneC0".equals(appl.getName())) {
        polygraphicprogram.types.OnePath res = polygraphicprogram.types.onepath.EmptyOneC0.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          polygraphicprogram.types.OnePath elem = polygraphicprogram.types.OnePath.fromTerm(array[i]);
          res = polygraphicprogram.types.onepath.ConsOneC0.make(elem,res);
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
    polygraphicprogram.types.OnePath cur = this;
    if(o==null) { return false; }
    if(cur instanceof polygraphicprogram.types.onepath.ConsOneC0) {
      while(cur instanceof polygraphicprogram.types.onepath.ConsOneC0) {
        if( o.equals(((polygraphicprogram.types.onepath.ConsOneC0)cur).getHeadOneC0()) ) {
          return true;
        }
        cur = ((polygraphicprogram.types.onepath.ConsOneC0)cur).getTailOneC0();
      }
    }
    return false;
  }

  //public boolean equals(Object o) { return this == o; }

  //public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return isEmptyOneC0() ; }

  public java.util.Iterator<polygraphicprogram.types.OnePath> iterator() {
    return new java.util.Iterator<polygraphicprogram.types.OnePath>() {
      polygraphicprogram.types.OnePath list = OneC0.this;

      public boolean hasNext() {
        return list.isConsOneC0();
      }

      public polygraphicprogram.types.OnePath next() {
        if(list.isEmptyOneC0()) {
          throw new java.util.NoSuchElementException();
        }
        polygraphicprogram.types.OnePath head = ((polygraphicprogram.types.onepath.ConsOneC0)list).getHeadOneC0();
        list = ((polygraphicprogram.types.onepath.ConsOneC0)list).getTailOneC0();
        return head;
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(polygraphicprogram.types.OnePath o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }


  public boolean addAll(java.util.Collection<? extends polygraphicprogram.types.OnePath> c) {
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
    if(this instanceof polygraphicprogram.types.onepath.ConsOneC0) {
      polygraphicprogram.types.OnePath cur = this;
      while(cur instanceof polygraphicprogram.types.onepath.ConsOneC0) {
        polygraphicprogram.types.OnePath elem = ((polygraphicprogram.types.onepath.ConsOneC0)cur).getHeadOneC0();
        array[i] = elem;
        cur = ((polygraphicprogram.types.onepath.ConsOneC0)cur).getTailOneC0();
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
    if(this instanceof polygraphicprogram.types.onepath.ConsOneC0) {
      polygraphicprogram.types.OnePath cur = this;
      while(cur instanceof polygraphicprogram.types.onepath.ConsOneC0) {
        polygraphicprogram.types.OnePath elem = ((polygraphicprogram.types.onepath.ConsOneC0)cur).getHeadOneC0();
        array[i] = (T)elem;
        cur = ((polygraphicprogram.types.onepath.ConsOneC0)cur).getTailOneC0();
        i++;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<polygraphicprogram.types.OnePath> getCollection() {
    return new CollectionOneC0(this);
  }

  public java.util.Collection<polygraphicprogram.types.OnePath> getCollectionOneC0() {
    return new CollectionOneC0(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionOneC0 implements java.util.Collection<polygraphicprogram.types.OnePath> {
    private OneC0 list;

    public OneC0 getOnePath() {
      return list; 
    }

    public CollectionOneC0(OneC0 list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends polygraphicprogram.types.OnePath> c) {
    boolean modified = false;
    java.util.Iterator<? extends polygraphicprogram.types.OnePath> it = c.iterator();
    while(it.hasNext()) {
      modified = modified || add(it.next());
    }
    return modified;
  }

  public boolean contains(Object o) {
    return getOnePath().contains(o);
  }

  public boolean containsAll(java.util.Collection<?> c) {
    return getOnePath().containsAll(c);
  }

  @Override
  public boolean equals(Object o) { 
    return getOnePath().equals(o); 
  }

  @Override
  public int hashCode() {
    return getOnePath().hashCode(); 
  }

  public java.util.Iterator<polygraphicprogram.types.OnePath> iterator() {
    return getOnePath().iterator();
  }

  public int size() { 
    return getOnePath().size(); 
  }

  public Object[] toArray() {
    return getOnePath().toArray();
  }

  public <T> T[] toArray(T[] array) {
    return getOnePath().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getOnePath().length();
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

    public boolean add(polygraphicprogram.types.OnePath o) {
      list = (OneC0)polygraphicprogram.types.onepath.ConsOneC0.make(o,list);
      return true;
    }

    public void clear() {
      list = (OneC0)polygraphicprogram.types.onepath.EmptyOneC0.make();
    }

    public boolean isEmpty() { 
      return list.isEmptyOneC0(); 
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
