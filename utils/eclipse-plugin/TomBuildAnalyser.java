/*
 * Created on Aug 12, 2003
 */
package fr.loria.eclipse.jtom;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import jtom.adt.TomErrorList;
import jtom.adt.TomError;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author julien
 */
public class TomBuildAnalyser extends IncrementalProjectBuilder implements IResourceDeltaVisitor, IResourceVisitor {

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
			System.out.println("TomBuildAnalyser in action");
			analyseTomBuild(resource);
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
	
	private void analyseTomBuild(IResource resource) throws CoreException {
		if(resource.exists()) {
			  // clear previous markers
			resource.deleteMarkers(null, false, IResource.DEPTH_ZERO);
			  // Look for generated resource 
			IContainer destinationFolder = resource.getParent();
			IPath tomFilePath = resource.getLocation();
			String generatedFileName = resource.getName();
			generatedFileName = generatedFileName.substring(0,
						generatedFileName.length()-(JtomPlugin.getDefault().getPreferenceStore().getString("FileExtension").length()+1));
			generatedFileName = generatedFileName.concat(".java");
			IResource generatedResource = destinationFolder.findMember(generatedFileName);
			if (generatedResource != null) {
				  // set derived + readonly
				generatedResource.setDerived(true);
				generatedResource.setReadOnly(true);
				  // transfer of generated resource Markers to resource
				IMarker[] problems = null;
				Iterator it ;
				Map map;
				IMarker marker;
				try {
				  problems = generatedResource.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
				  for (int i=0;i<problems.length;i++) {
						marker = resource.createMarker(IMarker.PROBLEM);
						map = problems[i].getAttributes();
  					it  = map.keySet().iterator();
						marker.setAttributes(map);
						while(it.hasNext()) {
							Object next = it.next();
						}
				  }
					// remove all resource from source and target files
				  generatedResource.deleteMarkers(null, false, IResource.DEPTH_ZERO);
				   } catch (CoreException e) {
					System.out.println("something went wrong finding markers");
				   }
			} else {
				System.out.println(generatedFileName+" does not have been generated");
			}
			// In all case: Analyse Output
			analyseOutput(resource); 
		}
	}

	private void analyseOutput(IResource resource) {
		IMarker marker;
		TomErrorList errors = TomBuilder.getLastErrors(resource);
		if(errors != null && !errors.isEmpty()) {
			System.out.println("Analysing "+errors.getLength()+ "errors");
			try {
				while(!errors.isEmpty()) {
					TomError err = errors.getHead();
					System.out.println(err);
					marker = resource.createMarker(IMarker.PROBLEM);
					marker.setAttribute(IMarker.LINE_NUMBER, err.getLine());
					marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
					marker.setAttribute(IMarker.MESSAGE, err.getMessage());
					marker.setAttribute(IMarker.LOCATION, err.getFile());
					errors = errors.getTail();
				}
			} catch (CoreException e) {
				System.out.println("something went wrong analysing errors");
			}
		}	
		
			 	try {
				ByteArrayInputStream inputStream = new ByteArrayInputStream(TomBuilder.getLastOutputStream().toByteArray());
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(">>>"+line);
				}
				} 	catch (Exception e) {
					e.printStackTrace();
				}
	}
	
} // Class TomBuilAnalyser
