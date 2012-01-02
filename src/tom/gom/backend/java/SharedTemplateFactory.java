package tom.gom.backend.java;

import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;
import tom.platform.OptionManager;
import tom.gom.tools.GomEnvironment;

public class SharedTemplateFactory extends tom.gom.backend.TemplateFactory {

  private OptionManager manager;
  SharedTemplateFactory(OptionManager manager) {
    this.manager = manager;
  }
  public SharedTemplateFactory(OptionManager manager, GomEnvironment gomEnvironment) {
    this.manager = manager;
    this.gomEnvironment = gomEnvironment;
  }

  public MappingTemplateClass makeTomMappingTemplate(
      GomClass gomClass,
      TemplateClass strategyMapping,
      GomEnvironment gomEnvironment) {
    return new tom.gom.backend.java.shared.MappingTemplate(gomClass,strategyMapping,gomEnvironment);
  }

  public TemplateClass makeAbstractTypeTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean maximalsharing, GomEnvironment gomEnvironment) {
    return new tom.gom.backend.java.shared.AbstractTypeTemplate(tomHomePath, manager, importList, gomClass, mapping, maximalsharing, gomEnvironment);
  }
  public TemplateClass makeSortTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean maximalsharing, GomEnvironment gomEnvironment) {
    return new tom.gom.backend.java.shared.SortTemplate(tomHomePath, manager, maximalsharing, importList, gomClass, mapping, gomEnvironment);
  }
  public TemplateClass makeOperatorTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean multithread, boolean maximalsharing, boolean jmicompatible, GomEnvironment gomEnvironment) {
    return new tom.gom.backend.java.shared.OperatorTemplate(tomHomePath, manager, importList, gomClass,mapping,multithread,maximalsharing,jmicompatible,gomEnvironment);
  }
  public TemplateClass makeVariadicOperatorTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, GomEnvironment gomEnvironment) {
    return new tom.gom.backend.java.shared.VariadicOperatorTemplate(tomHomePath, manager, importList, gomClass, mapping, gomEnvironment);
  }
}
