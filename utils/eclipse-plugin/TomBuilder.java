/*
 * Created on Aug 12, 2003
 */
package fr.loria.eclipse.jtom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
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
import jtom.adt.TomErrorList;

/**
 * @author julien
 */
public class TomBuilder extends IncrementalProjectBuilder implements IResourceDeltaVisitor, IResourceVisitor {

  private static HashMap errorMap = new HashMap(); 
  private static TomErrorList errors = null;
	private static ByteArrayOutputStream outputStream = null;
	
	public static HashMap getErrorMap() {
		return errorMap;
	}
	
	public static void clearErrorMap() {
		errorMap = new HashMap();
	}
	
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
	
	public static ByteArrayOutputStream getLastOutputStream() {
		 return outputStream;
	}
	
	private void buildTomFile(IResource resource) throws CoreException {
		FileWriter writer;
		InputStream input;
		outputStream = new ByteArrayOutputStream();
		PrintStream errorStream, oldErrorStream = System.err, oldOutputStream = System.out;
		
		if(resource.exists()) {
			IContainer destinationFolder = resource.getParent();
			IPath tomFilePath = resource.getLocation();
			// Create destination IFile and remove it from the workspace if already exists
			IPath generatedFilePath = tomFilePath.removeFileExtension().addFileExtension("java");
			File dest = new File(generatedFilePath.toString());
			if(dest.exists()) {
				dest.delete();
			}
			// get the command line argument			
			QualifiedName qName = new QualifiedName("TOMCOMMAND", resource.getLocation().toString());
			String command = resource.getPersistentProperty(qName);
			if (command == null) {
				command = "-v";
			}
			String commandArgs[] = command.split(" ");
			String finalCommand[] = new String[commandArgs.length+2];
			finalCommand[commandArgs.length] = "--eclipse";
			finalCommand[commandArgs.length+1] = resource.getLocation().toString();
			for (int i = 0; i<commandArgs.length;i++) {
				finalCommand[i] = commandArgs[i];
			}
			
			// Execute the TOM command 
			try {
				errorStream = new PrintStream(outputStream);
				System.setErr(errorStream);
				System.setOut(errorStream);
				errors = null;
				Tom tom = new Tom(finalCommand);
				if(!tom.getErrors().isEmpty()) {
					System.out.println("Error creating Tom");
					errors = tom.getErrors();
					errorMap.put(resource, errors);
				}
				tom.run();
				if(!tom.getErrors().isEmpty()) {
					System.out.println("Error running Tom");
					errors = tom.getErrors();
					errorMap.put(resource, errors);
				} else {
					errors = null;
				}
				errorStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally
			{
				System.setErr(oldErrorStream);
				System.setOut(oldOutputStream);
			}
			// refresh folder
			destinationFolder.refreshLocal(IResource.DEPTH_ONE, null);
		}
	}

	protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
		getProject().accept(this /*as a IResourceVisitor*/);
	}

	protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		// the visitor (this) does the work.
		delta.accept(this /*as a IResourceDeltaVisitor*/);
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
	public boolean visit(IResourceDelta delta) throws CoreException {
		// call visit where all the stuff is done 
		return visit(delta.getResource());
	}


	public static TomErrorList getLastErrors(IResource res) {
		return (TomErrorList)errorMap.remove(res);
	}

} // Class TomBuilder
