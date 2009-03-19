
package firewall.ast.types.file;

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;

public abstract class Blocks extends firewall.ast.types.File implements java.util.Collection<firewall.ast.types.Block>  {


  @Override
  public int length() {
    if(this instanceof firewall.ast.types.file.ConsBlocks) {
      firewall.ast.types.File tl = this.getTailBlocks();
      if (tl instanceof Blocks) {
        return 1+((Blocks)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static firewall.ast.types.File fromArray(firewall.ast.types.Block[] array) {
    firewall.ast.types.File res = firewall.ast.types.file.EmptyBlocks.make();
    for(int i = array.length; i>0;) {
      res = firewall.ast.types.file.ConsBlocks.make(array[--i],res);
    }
    return res;
  }

  @Override
  public firewall.ast.types.File reverse() {
    if(this instanceof firewall.ast.types.file.ConsBlocks) {
      firewall.ast.types.File cur = this;
      firewall.ast.types.File rev = firewall.ast.types.file.EmptyBlocks.make();
      while(cur instanceof firewall.ast.types.file.ConsBlocks) {
        rev = firewall.ast.types.file.ConsBlocks.make(cur.getHeadBlocks(),rev);
        cur = cur.getTailBlocks();
      }

      return rev;
    } else {
      return this;
    }
  }

  public firewall.ast.types.File append(firewall.ast.types.Block element) {
    if(this instanceof firewall.ast.types.file.ConsBlocks) {
      firewall.ast.types.File tl = this.getTailBlocks();
      if (tl instanceof Blocks) {
        return firewall.ast.types.file.ConsBlocks.make(this.getHeadBlocks(),((Blocks)tl).append(element));
      } else {

        return firewall.ast.types.file.ConsBlocks.make(this.getHeadBlocks(),firewall.ast.types.file.ConsBlocks.make(element,tl));

      }
    } else {
      return firewall.ast.types.file.ConsBlocks.make(element,this);
    }
  }

  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Blocks(");
    if(this instanceof firewall.ast.types.file.ConsBlocks) {
      firewall.ast.types.File cur = this;
      while(cur instanceof firewall.ast.types.file.ConsBlocks) {
        firewall.ast.types.Block elem = cur.getHeadBlocks();
        cur = cur.getTailBlocks();
        elem.toStringBuilder(buffer);

        if(cur instanceof firewall.ast.types.file.ConsBlocks) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof firewall.ast.types.file.EmptyBlocks)) {
        buffer.append(",");
        cur.toStringBuilder(buffer);
      }
    }
    buffer.append(")");
  }

  public aterm.ATerm toATerm() {
    aterm.ATerm res = atermFactory.makeList();
    if(this instanceof firewall.ast.types.file.ConsBlocks) {
      firewall.ast.types.File tail = this.getTailBlocks();
      res = atermFactory.makeList(getHeadBlocks().toATerm(),(aterm.ATermList)tail.toATerm());
    } 
    return res;
  }

  public static firewall.ast.types.File fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("Blocks".equals(appl.getName())) {
        firewall.ast.types.File res = firewall.ast.types.file.EmptyBlocks.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          firewall.ast.types.Block elem = firewall.ast.types.Block.fromTerm(array[i],atConv);
          res = firewall.ast.types.file.ConsBlocks.make(elem,res);
        }
        return res;
      }
    }

    if(trm instanceof aterm.ATermList) {
      aterm.ATermList list = (aterm.ATermList) trm;
      firewall.ast.types.File res = firewall.ast.types.file.EmptyBlocks.make();
      try {
        while(!list.isEmpty()) {
          firewall.ast.types.Block elem = firewall.ast.types.Block.fromTerm(list.getFirst(),atConv);
          res = firewall.ast.types.file.ConsBlocks.make(elem,res);
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
    firewall.ast.types.File cur = this;
    if(o==null) { return false; }
    if(cur instanceof firewall.ast.types.file.ConsBlocks) {
      while(cur instanceof firewall.ast.types.file.ConsBlocks) {
        if( o.equals(cur.getHeadBlocks()) ) {
          return true;
        }
        cur = cur.getTailBlocks();
      }
      if(!(cur instanceof firewall.ast.types.file.EmptyBlocks)) { 
        if( o.equals(cur) ) {
          return true;
        }
      }

    }
    return false;
  }

  //public boolean equals(Object o) { return this == o; }

  //public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return isEmptyBlocks() ; }

  public java.util.Iterator<firewall.ast.types.Block> iterator() {
    return new java.util.Iterator<firewall.ast.types.Block>() {
      firewall.ast.types.File list = Blocks.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyBlocks();
      }

      public firewall.ast.types.Block next() {
        if(list.isEmptyBlocks()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsBlocks()) {
          firewall.ast.types.Block head = list.getHeadBlocks();
          list = list.getTailBlocks();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (firewall.ast.types.Block)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(firewall.ast.types.Block o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }


  public boolean addAll(java.util.Collection<? extends firewall.ast.types.Block> c) {
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
    if(this instanceof firewall.ast.types.file.ConsBlocks) {
      firewall.ast.types.File cur = this;
      while(cur instanceof firewall.ast.types.file.ConsBlocks) {
        firewall.ast.types.Block elem = cur.getHeadBlocks();
        array[i] = elem;
        cur = cur.getTailBlocks();
        i++;
      }
      if(!(cur instanceof firewall.ast.types.file.EmptyBlocks)) {
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
    if(this instanceof firewall.ast.types.file.ConsBlocks) {
      firewall.ast.types.File cur = this;
      while(cur instanceof firewall.ast.types.file.ConsBlocks) {
        firewall.ast.types.Block elem = cur.getHeadBlocks();
        array[i] = (T)elem;
        cur = cur.getTailBlocks();
        i++;
      }
      if(!(cur instanceof firewall.ast.types.file.EmptyBlocks)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<firewall.ast.types.Block> getCollection() {
    return new CollectionBlocks(this);
  }

  public java.util.Collection<firewall.ast.types.Block> getCollectionBlocks() {
    return new CollectionBlocks(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionBlocks implements java.util.Collection<firewall.ast.types.Block> {
    private Blocks list;

    public Blocks getFile() {
      return list; 
    }

    public CollectionBlocks(Blocks list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends firewall.ast.types.Block> c) {
    boolean modified = false;
    java.util.Iterator<? extends firewall.ast.types.Block> it = c.iterator();
    while(it.hasNext()) {
      modified = modified || add(it.next());
    }
    return modified;
  }

  public boolean contains(Object o) {
    return getFile().contains(o);
  }

  public boolean containsAll(java.util.Collection<?> c) {
    return getFile().containsAll(c);
  }

  @Override
  public boolean equals(Object o) { 
    return getFile().equals(o); 
  }

  @Override
  public int hashCode() {
    return getFile().hashCode(); 
  }

  public java.util.Iterator<firewall.ast.types.Block> iterator() {
    return getFile().iterator();
  }

  public int size() { 
    return getFile().size(); 
  }

  public Object[] toArray() {
    return getFile().toArray();
  }

  public <T> T[] toArray(T[] array) {
    return getFile().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getFile().length();
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

    public boolean add(firewall.ast.types.Block o) {
      list = (Blocks)firewall.ast.types.file.ConsBlocks.make(o,list);
      return true;
    }

    public void clear() {
      list = (Blocks)firewall.ast.types.file.EmptyBlocks.make();
    }

    public boolean isEmpty() { 
      return list.isEmptyBlocks(); 
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
