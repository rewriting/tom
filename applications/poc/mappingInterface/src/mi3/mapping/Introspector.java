package mi3.mapping;

public class Introspector implements tom.library.sl.Introspector {
  private static final Introspector instance = new Introspector();
  public static Introspector getInstance() { 
    return instance;
  }

  /**
   * Replace all children of a given object, and return the new object
   * @param o the object whose children are replace
   * @param children the new subterm of the object
   */
  public <T> T setChildren(T o, Object[] children) {
    IMapping m = MappingRegistry.getMappingOf(o);
    return m.getIntrospector().setChildren (o, children);
  }

  /**
   * Return the children of a given object
   * @param o the subject
   */
  public Object[] getChildren(Object o) { 
    IMapping m = MappingRegistry.getMappingOf(o);
    return m.getIntrospector().getChildren(o);
  }

  /**
   * Replace the i-th subterm of the object o by child
   * @param o the subject
   * @param i the index of the subterm
   * @param child the new subterm 
   */
  public <T> T setChildAt(T o, int i, Object child)  {
    IMapping m = MappingRegistry.getMappingOf(o);
    return m.getIntrospector().setChildAt(o, i , child);
  }

  /**
   * Return the i-th subterm of the object o
   * @param o the subject
   * @param i the index of the subterm (0<=i<=childCount)
   */
  public Object getChildAt(Object o, int i)  {
    IMapping m = MappingRegistry.getMappingOf(o);
    Object res =  m.getIntrospector().getChildAt(o, i);
    //System.out.println("Intro child " + i + ": " + res);
    return res;
  }

  /**
   * Return the number of subterms of the object o
   * @param o the subject
   * @return the number of children
   */
  public int getChildCount(Object o)  {
    IMapping m = MappingRegistry.getMappingOf(o);
    int res = m.getIntrospector().getChildCount(o);
    //System.out.println("Intro count: " + res);
    return res;
  }

}
