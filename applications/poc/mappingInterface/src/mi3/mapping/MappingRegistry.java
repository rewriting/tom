package mi3.mapping;

import java.util.Map;
import java.util.HashMap;

public class MappingRegistry {

  public static final Map<Class, IMapping> runtime = new HashMap<Class, IMapping>();

  public static IMapping getMappingOf(Object o) {
    return runtime.get(o.getClass());
  }

  public static void registerMappingOf(IMapping mapping) {
    runtime.put(mapping.getImplementation(), mapping);
  }
}
