/*
 * Created on Aug 12, 2003
 */
package fr.loria.eclipse.jtom;

import java.util.Map;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;

import apigen.gen.java.Main;

/**
 * @author julien
 */
public class ADTToJavaBuilder extends IncrementalProjectBuilder implements IResourceDeltaVisitor, IResourceVisitor {

	/* (non-Javadoc)
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
		getProject().accept(this /*as a IResourceVisitor*/);
	}
	public boolean visit(IResource resource) throws CoreException {
		IPath path = resource.getFullPath();
		String tomExt = JtomPlugin.getDefault().getPreferenceStore().getString("FileExtension");
		if("adt".equals(path.getFileExtension())){
			System.out.println("ADTBuilder in action");
			buildADTFile(resource);
		}
		return true;
	}

	protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		// the visitor (this) does the work.
		delta.accept(this /*as a IResourceDeltaVisitor*/);
	}
	public boolean visit(IResourceDelta delta) throws CoreException {
		// call visit where all the stuff is done 
		return visit(delta.getResource());
	}
	
	private void buildADTFile(IResource resource) throws CoreException {
		if(resource.exists()) {
			IContainer destinationFolder = resource.getParent();				
			QualifiedName qName = new QualifiedName("ADTCOMMAND", resource.getLocation().toString());
			String command = resource.getPersistentProperty(qName);
			if (command == null) {
				command = "-v";
			}
			System.out.println("adt-to-java "+command+" "+resource.getLocation().toString());
			String commandArgs[] = command.split(" ");
			String finalCommand[] = new String[commandArgs.length+2];
			for (int i = 0; i<commandArgs.length;i++) {
				finalCommand[i] = commandArgs[i];
			}
			finalCommand[commandArgs.length] = "-input";
			finalCommand[commandArgs.length+1] = resource.getLocation().toString();
			for (int i = 0; i<finalCommand.length;i++) {
				System.out.println(finalCommand[i]);
			}
			try {	
				Main.main(finalCommand);
			} catch (Exception e) {
			}
			// Refresh 
			destinationFolder.refreshLocal(IResource.DEPTH_ONE, null);
		}
	}

} // Class TomBuilder
