package mapping;

public class Introspector implements tom.library.sl.Introspector {

  public static final Introspector instance = new Introspector();

  /**
   * Replace all children of a given object, and return the new object
   * @param o the object whose children are replace
   * @param children the new subterm of the object
   */
  public <T> T setChildren(T o, Object[] children) {
    Mapping m = MappingRegistry.getMappingOf(o);
    if (m != null) {
      return m.setChildren (o, children);
    }
    return null;
  }

  /**
   * Return the children of a given object
   * @param o the subject
   */
  public Object[] getChildren(Object o) { 
    Mapping m = MappingRegistry.getMappingOf(o);
    if (m != null) {
      return m.getChildren (o);
    }
    return null;
  }

  /**
   * Replace the i-th subterm of the object o by child
   * @param o the subject
   * @param i the index of the subterm
   * @param child the new subterm 
   */
  public <T> T setChildAt(T o, int i, Object child)  {
    Mapping m = MappingRegistry.getMappingOf(o);
    if (m != null) {
      return m.setChildAt(o, i , child);
    }
    return null;
  }

  /**
   * Return the i-th subterm of the object o
   * @param o the subject
   * @param i the index of the subterm (0<=i<=childCount)
   */
  public Object getChildAt(Object o, int i)  {
    Mapping m = MappingRegistry.getMappingOf(o);
    if (m != null) {
      return m.getChildAt(o, i);
    }
    return null;
  }


  /**
   * Return the number of subterms of the object o
   * @param o the subject
   * @return the number of children
   */
  public int getChildCount(Object o)  {
    Mapping m = MappingRegistry.getMappingOf(o);
    if (m != null) {
      return m.getChildCount(o);
    }
    return 0;
  }


}
