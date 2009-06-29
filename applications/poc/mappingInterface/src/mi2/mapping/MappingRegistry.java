package mi2.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nvintila
 * @date 4:36:20 PM Jun 14, 2009
 */
public class MappingRegistry {

    public static final Map<Class, Mapping> runtime = new HashMap<Class, Mapping>();

    public static Mapping getMappingOf(Object o) {
        return runtime.get(o.getClass());
    }

    public static void registerMappingOf(Mapping mapping) {
        runtime.put(mapping.forType(), mapping);
    }
}