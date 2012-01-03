package tom.gom.backend;

import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;
import tom.platform.OptionManager;
import tom.gom.tools.GomEnvironment;


public abstract class TemplateFactory {

  protected GomEnvironment gomEnvironment;

  public GomEnvironment getGomEnvironment() {
    return gomEnvironment;
  }

  public TemplateFactory getFactory(OptionManager manager) {
    String mode = (String) manager.getOptionValue("generator");
    if (mode.equals("shared")) {

	    if (((Boolean)manager.getOptionValue("aCode")).booleanValue()) {
      return new tom.gom.backend.ada.SharedTemplateFactory(manager,getGomEnvironment());
      		} else { /* Default mode = Java*/
      return new tom.gom.backend.java.SharedTemplateFactory(manager,getGomEnvironment());
		}
    } else {
      throw new GomRuntimeException("Output mode "+mode+" not supported");
    }
  }

  public abstract MappingTemplateClass makeTomMappingTemplate(GomClass gomClass,TemplateClass strategyMapping, GomEnvironment gomEnvironment);
  public abstract TemplateClass makeAbstractTypeTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean maximalsharing, GomEnvironment gomEnvironment);
  public abstract TemplateClass makeSortTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean maximalsharingi, GomEnvironment gomEnvironment);
  public abstract TemplateClass makeOperatorTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean multithread, boolean maximalsharing, boolean jmicompatible, GomEnvironment gomEnvironment);
  public abstract TemplateClass makeVariadicOperatorTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, GomEnvironment gomEnvironment);
}
