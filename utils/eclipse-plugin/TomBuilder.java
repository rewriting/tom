/*
 * Created on Aug 12, 2003
 */
package fr.loria.eclipse.jtom;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
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

import jtom.Tom;

/**
 * @author julien
 */
public class TomBuilder extends IncrementalProjectBuilder implements IResourceDeltaVisitor, IResourceVisitor {

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
		if (tomExt.equals(path.getFileExtension())) {
			System.out.println("TomBuilder in action");
			buildTomFile(resource);
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
	
	private void buildTomFile(IResource resource) throws CoreException {
		FileWriter writer;
		InputStream input;
		if(resource.exists()) {
			IContainer destinationFolder = resource.getParent();
			IPath tomFilePath = resource.getLocation();

			// Create destination IFile and remove it from the workspace 
			IPath generatedFilePath = tomFilePath.removeFileExtension().addFileExtension("java");
			File dest = new File(generatedFilePath.toString());
			if(dest.exists()) {
				System.out.println("==>deleting "+generatedFilePath.toString());
				dest.delete();
			}						
			QualifiedName qName = new QualifiedName("TOMCOMMAND", resource.getLocation().toString());
			String command = resource.getPersistentProperty(qName);
			if (command == null) {
				command = "-v";
			}
			System.out.println("jtom "+command+" "+resource.getLocation().toString());
			System.out.println("==>Shall generate:"+generatedFilePath);
			String commandArgs[] = command.split(" ");
			String finalCommand[] = new String[commandArgs.length+1];
			finalCommand[commandArgs.length] = resource.getLocation().toString();
			for (int i = 0; i<commandArgs.length;i++) {
				finalCommand[i] = commandArgs[i];
			}
			try {	
				Tom tom = new Tom(finalCommand);
				tom.run();
			} catch (Exception e) {
			}
			
			String generatedFileName = resource.getName();
			generatedFileName = generatedFileName.substring(0,
											generatedFileName.length()-(JtomPlugin.getDefault().getPreferenceStore().getString("FileExtension").length()+1));
			generatedFileName = generatedFileName.concat(".java");
			IResource generatedResource = destinationFolder.findMember(generatedFileName);
			if (generatedResource != null) {
				generatedResource.setDerived(true);
				generatedResource.setReadOnly(true);
			} else {
				System.out.println(generatedFileName+" do not have been generated");
			}
			destinationFolder.refreshLocal(IResource.DEPTH_ONE, null);
		}
	}

} // Class TomBuilder
