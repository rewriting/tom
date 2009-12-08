package mi3.mapping;

public interface IMapping {
  public boolean isInstanceOf(Object subject);
  public Class getImplementation();
  public tom.library.sl.Introspector getIntrospector();
}
