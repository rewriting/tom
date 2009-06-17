package mapping;

/**
 * @author nvintila
 * @date 4:35:53 PM Jun 14, 2009
 */
public abstract class Mapping<T> implements tom.library.sl.Introspector {

    public Mapping() {
        MappingRegistry.registerMappingOf(this);
    }

    public abstract Class forType();

    public abstract T make(Object[] children);
}
