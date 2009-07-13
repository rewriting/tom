package mi3.mapping;

import java.util.Map;
import java.util.HashMap;

public class MappingRegistry {

  public static final Map<Class, IMapping> runtime = new HashMap<Class, IMapping>();

  public static IMapping getMappingOf(Object o) {
    return getMappingOf(o.getClass());
  }

  private static IMapping getMappingOf(Class c) {
    //System.out.println("class: " + c);
    if(c!=null) {
      IMapping res = runtime.get(c);
        if(res != null) {
          return res;
        }
      return getMappingOf(c.getSuperclass());
    }
    throw new RuntimeException("no mapping for: " + c);
  }

  public static void registerMappingOf(IMapping mapping) {
    runtime.put(mapping.getImplementation(), mapping);
  }
}
