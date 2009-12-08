package mi3.mapping;

public abstract class ListMapping<C, D> implements IListMapping<C,D> {

  public ListMapping() {
    MappingRegistry.registerMappingOf(this);
  }

  public tom.library.sl.Introspector getIntrospector() {
    return introspector;
  }

  private tom.library.sl.Introspector introspector = new tom.library.sl.Introspector() {

    public <T> T setChildAt(T o, int i, Object child) {
      C l = (C)o;
      if(i==0) {
        return (T) makeInsert((D)child,l);
      } else if(i>0 && !isEmpty(l)) {
        return (T) setChildAt(getTail(l),i-1,child);
      }
      throw new RuntimeException("index " + i + " does not exist");
    }

    public Object getChildAt(Object o, int i) {
      //System.out.println("getChildAt: " + o + ", " + i);
      C l = (C)o;
      int index = 0;
      while(index<=i && !isEmpty(l)) {
        if(index==i) {
          //System.out.println("getChildAt: res = " + getHead(l));
          return getHead(l);
        }
        index++;
        l = getTail(l);
      }
      throw new RuntimeException("index " + i + " does not exist");
    }

    public int getChildCount(Object o) {
      //System.out.println("getChildCount: " + o);
      C l = (C)o;
      int res = 0;
      while(!isEmpty(l)) {
        res++;
        l = getTail(l);
      }
      //System.out.println("getChildCount: res = " + res);
      return res;
      //throw new RuntimeException(" not defined for: " + o.getClass());
    }

    public <T> T setChildren(T o, Object[] children) {
      C res = makeEmpty();
      for(int i=children.length-1; i>=0 ; i--) {
        res = makeInsert((D)children[i],res);
      }
      return (T) res;
    }

    public Object[] getChildren(Object o) {
      //System.out.println("getChildren: " + o);
      C l = (C)o;
      java.util.ArrayList<D> res = new java.util.ArrayList<D>();
      while(!isEmpty(l)) {
        res.add(getHead(l));
        l = getTail(l);
      }
      //System.out.println("getChildren: res = " + res);
      return res.toArray();
    }

  };

}
