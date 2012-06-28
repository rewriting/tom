package tom.mapping.dsl.generator;

import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import tom.mapping.dsl.generator.tom.TomTemplateCompiler;
import tom.mapping.model.Mapping;

@SuppressWarnings("all")
public class TomMappingGenerator implements IGenerator {
  @Inject
  private TomTemplateCompiler tomCompiler;
  
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
      EList<EObject> _contents = resource.getContents();
      EObject _get = _contents.get(0);
      final Mapping mapping = ((Mapping) _get);
      this.tomCompiler.compile(mapping, fsa);
  }
}
