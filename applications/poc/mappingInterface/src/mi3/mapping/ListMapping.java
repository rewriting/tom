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
      throw new RuntimeException("not yet implemented");
    }

    public Object getChildAt(Object o, int i) {
      throw new RuntimeException("not yet implemented");
    }

    public int getChildCount(Object o) {
      throw new RuntimeException("not yet implemented");
    }

    public <T> T setChildren(T o, Object[] children) {
      throw new RuntimeException("not yet implemented");
    }

    public Object[] getChildren(Object o) {
      throw new RuntimeException("not yet implemented");
    }
  };

}
