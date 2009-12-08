package mi3.mapping;

public abstract class Mapping0<C> implements IMapping0<C> {

  public Mapping0() {
    MappingRegistry.registerMappingOf(this);
  }

  public tom.library.sl.Introspector getIntrospector() {
    return introspector;
  }

  private tom.library.sl.Introspector introspector = new tom.library.sl.Introspector() {
    public <T> T setChildAt(T o, int i, Object child) {
      throw new RuntimeException("unexpected call");
    }

    public Object getChildAt(Object o, int i) {
      throw new RuntimeException("unexpected call");
    }

    public int getChildCount(Object o) {
      return 0;
    }

    public <T> T setChildren(T o, Object[] children) {
      throw new RuntimeException("unexpected call");
    }

    public Object[] getChildren(Object o) {
      return new Object[]{};
    }
  };

}
