package mi3.mapping;

public abstract class Mapping<T> {

  public Mapping() {
    MappingRegistry.registerMappingOf(this);
  }

  public abstract Class forType();
}
