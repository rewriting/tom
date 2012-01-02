package tom.gom.backend ;

import java.util.Map;
import java.util.HashMap;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.GomEnvironment;

public interface MappingTemplateClass extends TemplateClass {

  public abstract void addTemplates(Map<ClassName,TemplateClass> map); 

}
