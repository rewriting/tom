package mi2.mapping;

/**
 * @author nvintila
 * @date 4:35:53 PM Jun 14, 2009
 */
public abstract class Mapping<T> {

  public Mapping() {
    MappingRegistry.registerMappingOf(this);
  }

  public abstract Class forType();

}
