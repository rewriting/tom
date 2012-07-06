package tom.mapping.dsl.generator.tom;

import org.eclipse.xtext.xbase.lib.Functions.Function0;
import tom.mapping.dsl.generator.TomMappingExtensions;

@SuppressWarnings("all")
public class TomFactoryCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
}
