package mi3.mapping;

public abstract class Mapping1<C, D0> implements IMapping1<C, D0> {

  public Mapping1() {
    MappingRegistry.registerMappingOf(this);
  }

  public tom.library.sl.Introspector getIntrospector() {
    return introspector;
  }

  private tom.library.sl.Introspector introspector = new tom.library.sl.Introspector() {

    public <T> T setChildAt(T o, int i, Object child) {
      switch (i) {
        case 0: return (T) make((D0) child);
      }
      assert false : "Unexpected call.";
      return null;
    }

    public Object getChildAt(Object o, int i) {
      switch (i) {
        case 0: return get0((C)o);
      }
      assert false : "Unexpected call.";
      return null;
    }

    public int getChildCount(Object o) {
      return 1;
    }

    public <T> T setChildren(T o, Object[] children) {
      return (T) make((D0) children[0]);
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ get0((C)o) };
    }
  };

}
