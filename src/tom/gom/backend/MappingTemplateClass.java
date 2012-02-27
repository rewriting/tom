package tom.gom.backend ;

import java.util.Map;
import java.util.HashMap;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.GomEnvironment;

public abstract class MappingTemplateClass extends tom.gom.backend.TemplateClass  {

  public MappingTemplateClass(GomClass gomClass, GomEnvironment gomEnvironment) {
    	  super(gomClass,gomEnvironment);
	  this.templates = new HashMap<ClassName,tom.gom.backend.TemplateClass>();
  }
  protected Map<ClassName,tom.gom.backend.TemplateClass> templates;

  public void addTemplates(Map<ClassName,tom.gom.backend.TemplateClass> map) {
    this.templates.putAll(map);
  }


}
