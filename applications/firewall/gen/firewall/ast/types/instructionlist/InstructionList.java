
package firewall.ast.types.instructionlist;

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;

public abstract class InstructionList extends firewall.ast.types.InstructionList implements java.util.Collection<firewall.ast.types.Instruction>  {


  @Override
  public int length() {
    if(this instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
      firewall.ast.types.InstructionList tl = this.getTailInstructionList();
      if (tl instanceof InstructionList) {
        return 1+((InstructionList)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

  public static firewall.ast.types.InstructionList fromArray(firewall.ast.types.Instruction[] array) {
    firewall.ast.types.InstructionList res = firewall.ast.types.instructionlist.EmptyInstructionList.make();
    for(int i = array.length; i>0;) {
      res = firewall.ast.types.instructionlist.ConsInstructionList.make(array[--i],res);
    }
    return res;
  }

  @Override
  public firewall.ast.types.InstructionList reverse() {
    if(this instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
      firewall.ast.types.InstructionList cur = this;
      firewall.ast.types.InstructionList rev = firewall.ast.types.instructionlist.EmptyInstructionList.make();
      while(cur instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
        rev = firewall.ast.types.instructionlist.ConsInstructionList.make(cur.getHeadInstructionList(),rev);
        cur = cur.getTailInstructionList();
      }

      return rev;
    } else {
      return this;
    }
  }

  public firewall.ast.types.InstructionList append(firewall.ast.types.Instruction element) {
    if(this instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
      firewall.ast.types.InstructionList tl = this.getTailInstructionList();
      if (tl instanceof InstructionList) {
        return firewall.ast.types.instructionlist.ConsInstructionList.make(this.getHeadInstructionList(),((InstructionList)tl).append(element));
      } else {

        return firewall.ast.types.instructionlist.ConsInstructionList.make(this.getHeadInstructionList(),firewall.ast.types.instructionlist.ConsInstructionList.make(element,tl));

      }
    } else {
      return firewall.ast.types.instructionlist.ConsInstructionList.make(element,this);
    }
  }

  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("InstructionList(");
    if(this instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
      firewall.ast.types.InstructionList cur = this;
      while(cur instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
        firewall.ast.types.Instruction elem = cur.getHeadInstructionList();
        cur = cur.getTailInstructionList();
        elem.toStringBuilder(buffer);

        if(cur instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof firewall.ast.types.instructionlist.EmptyInstructionList)) {
        buffer.append(",");
        cur.toStringBuilder(buffer);
      }
    }
    buffer.append(")");
  }

  public aterm.ATerm toATerm() {
    aterm.ATerm res = atermFactory.makeList();
    if(this instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
      firewall.ast.types.InstructionList tail = this.getTailInstructionList();
      res = atermFactory.makeList(getHeadInstructionList().toATerm(),(aterm.ATermList)tail.toATerm());
    } 
    return res;
  }

  public static firewall.ast.types.InstructionList fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("InstructionList".equals(appl.getName())) {
        firewall.ast.types.InstructionList res = firewall.ast.types.instructionlist.EmptyInstructionList.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          firewall.ast.types.Instruction elem = firewall.ast.types.Instruction.fromTerm(array[i],atConv);
          res = firewall.ast.types.instructionlist.ConsInstructionList.make(elem,res);
        }
        return res;
      }
    }

    if(trm instanceof aterm.ATermList) {
      aterm.ATermList list = (aterm.ATermList) trm;
      firewall.ast.types.InstructionList res = firewall.ast.types.instructionlist.EmptyInstructionList.make();
      try {
        while(!list.isEmpty()) {
          firewall.ast.types.Instruction elem = firewall.ast.types.Instruction.fromTerm(list.getFirst(),atConv);
          res = firewall.ast.types.instructionlist.ConsInstructionList.make(elem,res);
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
    firewall.ast.types.InstructionList cur = this;
    if(o==null) { return false; }
    if(cur instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
      while(cur instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
        if( o.equals(cur.getHeadInstructionList()) ) {
          return true;
        }
        cur = cur.getTailInstructionList();
      }
      if(!(cur instanceof firewall.ast.types.instructionlist.EmptyInstructionList)) { 
        if( o.equals(cur) ) {
          return true;
        }
      }

    }
    return false;
  }

  //public boolean equals(Object o) { return this == o; }

  //public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return isEmptyInstructionList() ; }

  public java.util.Iterator<firewall.ast.types.Instruction> iterator() {
    return new java.util.Iterator<firewall.ast.types.Instruction>() {
      firewall.ast.types.InstructionList list = InstructionList.this;

      public boolean hasNext() {
        return list!=null && !list.isEmptyInstructionList();
      }

      public firewall.ast.types.Instruction next() {
        if(list.isEmptyInstructionList()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isConsInstructionList()) {
          firewall.ast.types.Instruction head = list.getHeadInstructionList();
          list = list.getTailInstructionList();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (firewall.ast.types.Instruction)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(firewall.ast.types.Instruction o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }


  public boolean addAll(java.util.Collection<? extends firewall.ast.types.Instruction> c) {
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
    if(this instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
      firewall.ast.types.InstructionList cur = this;
      while(cur instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
        firewall.ast.types.Instruction elem = cur.getHeadInstructionList();
        array[i] = elem;
        cur = cur.getTailInstructionList();
        i++;
      }
      if(!(cur instanceof firewall.ast.types.instructionlist.EmptyInstructionList)) {
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
    if(this instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
      firewall.ast.types.InstructionList cur = this;
      while(cur instanceof firewall.ast.types.instructionlist.ConsInstructionList) {
        firewall.ast.types.Instruction elem = cur.getHeadInstructionList();
        array[i] = (T)elem;
        cur = cur.getTailInstructionList();
        i++;
      }
      if(!(cur instanceof firewall.ast.types.instructionlist.EmptyInstructionList)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<firewall.ast.types.Instruction> getCollection() {
    return new CollectionInstructionList(this);
  }

  public java.util.Collection<firewall.ast.types.Instruction> getCollectionInstructionList() {
    return new CollectionInstructionList(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class CollectionInstructionList implements java.util.Collection<firewall.ast.types.Instruction> {
    private InstructionList list;

    public InstructionList getInstructionList() {
      return list; 
    }

    public CollectionInstructionList(InstructionList list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends firewall.ast.types.Instruction> c) {
    boolean modified = false;
    java.util.Iterator<? extends firewall.ast.types.Instruction> it = c.iterator();
    while(it.hasNext()) {
      modified = modified || add(it.next());
    }
    return modified;
  }

  public boolean contains(Object o) {
    return getInstructionList().contains(o);
  }

  public boolean containsAll(java.util.Collection<?> c) {
    return getInstructionList().containsAll(c);
  }

  @Override
  public boolean equals(Object o) { 
    return getInstructionList().equals(o); 
  }

  @Override
  public int hashCode() {
    return getInstructionList().hashCode(); 
  }

  public java.util.Iterator<firewall.ast.types.Instruction> iterator() {
    return getInstructionList().iterator();
  }

  public int size() { 
    return getInstructionList().size(); 
  }

  public Object[] toArray() {
    return getInstructionList().toArray();
  }

  public <T> T[] toArray(T[] array) {
    return getInstructionList().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = getInstructionList().length();
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

    public boolean add(firewall.ast.types.Instruction o) {
      list = (InstructionList)firewall.ast.types.instructionlist.ConsInstructionList.make(o,list);
      return true;
    }

    public void clear() {
      list = (InstructionList)firewall.ast.types.instructionlist.EmptyInstructionList.make();
    }

    public boolean isEmpty() { 
      return list.isEmptyInstructionList(); 
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
