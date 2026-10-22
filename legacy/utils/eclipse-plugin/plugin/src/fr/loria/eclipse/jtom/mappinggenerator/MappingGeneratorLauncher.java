package fr.loria.eclipse.jtom.mappinggenerator;

import java.io.File;
import java.util.Iterator;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Launches the mapping generator
 * @author Radu
 *
 */
public class MappingGeneratorLauncher {

	/**
	 * Launches the generator
	 * 
	 * @param isFolder true if a folder was selected, false otherwise
	 * @param file the destination file of the mappings
	 * @param selection 
	 */
	public static void launchGenerator(boolean isFolder, IFile file, IStructuredSelection selection) {		
		TomMappingGenerator gen = new TomMappingGenerator();
		Iterator<?> it = selection.iterator();
		while(it.hasNext()){
			Object currentObj = it.next();
			String currentFilePath = null;
			if (currentObj instanceof Folder) {
				currentFilePath = ((Folder) currentObj).getLocation().toOSString();
			}else{
				currentFilePath = ((IFile)currentObj).getLocation().toOSString();
			}			
			try {
				// include into classpath the following:
				// 1. all classes/jars found in the project classpath
				// 2. all classes in the output folder				
				IJavaProject javaProject = (IJavaProject) JavaCore.create(file.getProject());				
				IClasspathEntry[] classPathEntries = javaProject.getResolvedClasspath(true);
				String resultPath = "";
				//String rootPath = JtomPlugin.getWorkspace().getRoot().getLocation().toOSString();
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IWorkspaceRoot root = workspace.getRoot();
				for(IClasspathEntry cpe: classPathEntries){
					IPath path = cpe.getPath();					   
					IResource resource = root.findMember(path);
					IPath location = null;
					if (resource != null) {
						location = resource.getLocation(); // something in the project
					} else {
						location = path; // a reference outside the project
					}
					if (location == null) { continue; } 
					resultPath += location.toOSString() + ";";
				}
				IResource resource = root.findMember(javaProject.getOutputLocation());
				IPath location = null;
				if (resource != null) {
					location = resource.getLocation();
				}				
				if (location != null) { 
					resultPath += location.toOSString();
				}
				gen.generate(new File(currentFilePath), file.getLocation().toOSString(),resultPath);
			} catch (Throwable e) {
				System.out.println("Cannot generate mapping for:" + currentObj + 
						".An exception occured:" + e.getMessage());
			}			
		}
		
	}
}
