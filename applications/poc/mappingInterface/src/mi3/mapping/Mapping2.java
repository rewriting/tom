package mi3.mapping;

public abstract class Mapping2<C, D0, D1> implements IMapping2<C, D0, D1> {

  public Mapping2() {
    MappingRegistry.registerMappingOf(this);
  }

  public tom.library.sl.Introspector getIntrospector() {
    return introspector;
  }

  private tom.library.sl.Introspector introspector = new tom.library.sl.Introspector() {

    public <T> T setChildren(T o, Object[] children) {
      return (T) make((D0) children[0], (D1) children[1]);
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ get0((C)o), get1((C)o) };
    }

    public <T> T setChildAt(T o, int i, Object child) {
      switch (i) {
        case 0: return (T) make((D0) child, get1((C)o));
        case 1: return (T) make(get0((C)o), (D1) child);
      }
      assert false : "Unexpected call.";
      return null;
    }

    public Object getChildAt(Object o, int i) {
      switch (i) {
        case 0: return get0((C)o);
        case 1: return get1((C)o);
      }
      assert false : "Unexpected call.";
      return null;
    }

    public int getChildCount(Object o) {
      return 2;
    }
  };

}
