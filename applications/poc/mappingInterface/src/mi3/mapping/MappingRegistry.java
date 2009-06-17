package mi3.mapping;

import java.util.Map;
import java.util.HashMap;

public class MappingRegistry {

  public static final Map<Class, Mapping> runtime = new HashMap<Class, Mapping>();

  public static Mapping getMappingOf(Object o) {
    return runtime.get(o.getClass());
  }

  public static void registerMappingOf(Mapping mapping) {
    runtime.put(mapping.forType(), mapping);
  }
}
