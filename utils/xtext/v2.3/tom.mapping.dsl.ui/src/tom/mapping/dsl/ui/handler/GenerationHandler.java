package tom.mapping.dsl.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;

import tom.mapping.dsl.generator.TomMappingGenerator;

import com.google.inject.Inject;
import com.google.inject.Provider;

@SuppressWarnings("deprecation")
public class GenerationHandler extends AbstractHandler implements IHandler {

    @Inject
    private IGenerator generator;
    
    @Inject
    private TomMappingGenerator tmgenerator;

    @Inject
    private Provider<EclipseResourceFileSystemAccess> fileAccessProvider;

    @Inject
    IResourceDescriptions resourceDescriptions;
    
    @Inject
    IResourceSetProvider resourceSetProvider;
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {

        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Object firstElement = structuredSelection.getFirstElement();
            if (firstElement instanceof IFile) {
                IFile file = (IFile) firstElement;
                IProject project = file.getProject();
                IFolder srcGenFolder = project.getFolder("src-gen");
                if (!srcGenFolder.exists()) {
                    try {
                        srcGenFolder.create(true, true,
                                new NullProgressMonitor());
                    } catch (CoreException e) {
                        return null;
                    }
                }
                
                final EclipseResourceFileSystemAccess fsa = fileAccessProvider.get();
                fsa.setOutputPath(srcGenFolder.getFullPath().toString());

                URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
                ResourceSet rs = resourceSetProvider.get(project);
                Resource r = rs.getResource(uri, true);
                tmgenerator.doGenerate(r, fsa); // Instead of the basic generator

            }
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
