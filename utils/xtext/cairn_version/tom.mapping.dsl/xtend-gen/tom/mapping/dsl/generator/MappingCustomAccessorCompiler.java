package tom.mapping.dsl.generator;

import com.google.inject.Inject;
import tom.mapping.dsl.java.CustomOperatorsCompiler;
import tom.mapping.model.Mapping;

@SuppressWarnings("all")
public class MappingCustomAccessorCompiler {
  @Inject
  private CustomOperatorsCompiler injcusopco;
  
  public CharSequence main(final Mapping m) {
    CharSequence _main = this.injcusopco.main(m);
    return _main;
  }
}
