/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 * Julien Guyon						e-mail: Julien.guyon@loria.fr
 * 
 **/

package fr.loria.eclipse.jtom.builders;

import java.util.Map;

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
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;

import tom.platform.RuntimeAlert;
import tom.platform.adt.platformalert.types.Alert;
import tom.platform.adt.platformalert.types.AlertList;
import fr.loria.eclipse.jtom.JtomPlugin;

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
		String tomExt = JtomPlugin.getDefault().getPreferenceStore().getString(JtomPlugin.TOM_EXTENSION_PREFERENCE);
		if (tomExt.equals(path.getFileExtension())) {
			if(JavaCore.create(getProject()).isOnClasspath(resource)) {
				analyseTomBuild(resource);
			}
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
			IPath tomFilePath = resource.getLocation();
			
			System.out.println("TomBuildAnalyser in action for "+tomFilePath);
			  // clear previous markers
			if(TomBuilder.isNoGeneration(resource)) {
				return;
			}
			resource.deleteMarkers(null, false, IResource.DEPTH_ZERO);
			  // Look for markers in generated resource 
			analyseGeneratedResourceMarker(resource);
			  // In all case: Analyse Output
			analyseOutput(resource); 
		}
	}

	/**
	 * @param resource
	 */
	private void analyseGeneratedResourceMarker(IResource resource) {
		String generatedResourceName = TomBuilder.getLastBuildResource(resource);
		if (generatedResourceName != null) {
			  // transfer of generated resource Markers to resource
			IPath generatedResourcePath = new Path(generatedResourceName).setDevice(null).removeFirstSegments(getProject().getLocation().segmentCount());
			System.out.println("Tom build analyser generatedResourcePath "+generatedResourcePath);
			IResource generatedResource = getProject().findMember(generatedResourcePath);
			if(generatedResource != null) {
			  try {
			    IMarker[] problems = null;
				Map<String, ?> map;
				IMarker marker;
			    problems = generatedResource.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
			    //System.out.println("Java problems "+problems.length);
			    for (int i=0;i<problems.length;i++) {
				 	  marker = resource.createMarker(IMarker.PROBLEM);
					  map = problems[i].getAttributes();
					  map.remove("id");
				 	  map.remove("charStart");
					  map.remove("charEnd");
					  marker.setAttributes(map);
			    }
				  // remove all resource from source and target files
				  if(!JtomPlugin.getDefault().getPreferenceStore().getBoolean(JtomPlugin.DISPLAY_JAVA_ERRORS_PREFERENCE)) {
					  generatedResource.deleteMarkers(null, false, IResource.DEPTH_ZERO);
				  }
			  } catch (CoreException e) {
					System.out.println("TomBuildAnalyser: Something went wrong finding markers");
					return;
			  }
			} else {
				  System.out.println("TomBuildAnalyser: not found: "+generatedResourcePath);
			}
		} else {
		  System.out.println("TomBuildAnalyser: no file generated");
		}
	}

	private void analyseOutput(IResource resource) {
		IMarker marker;
		RuntimeAlert runAlert = TomBuilder.getLastAlert(resource);
		if(runAlert == null) {
			System.out.println("TomBuildAnalyser: no tom errors found");
			return;
		}
		AlertList errors = runAlert.getErrors();
		System.out.println("errors: "+errors);
		if(errors != null && !errors.isEmptyconcAlert()) {
			try {
				while(!errors.isEmptyconcAlert()) {
					Alert err = errors.getHeadconcAlert();
					marker = resource.createMarker(JtomPlugin.MARKER_ID);
					marker.setAttribute(IMarker.LINE_NUMBER, err.getLine());
					marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
					marker.setAttribute(IMarker.MESSAGE, err.getMessage());
					marker.setAttribute(IMarker.LOCATION, err.getLine());
					errors = errors.getTailconcAlert();
				}
			} catch (CoreException e) {
				System.out.println("TomBuildAnalyser: Something went wrong analysing errors");
			}
		}
		errors = runAlert.getWarnings();
		System.out.println("warnings: "+errors);
		if(errors != null && !errors.isEmptyconcAlert()) {
			try {
				while(!errors.isEmptyconcAlert()) {
					Alert err = errors.getHeadconcAlert();
					marker = resource.createMarker(JtomPlugin.MARKER_ID);
					marker.setAttribute(IMarker.LINE_NUMBER, err.getLine());
					marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
					marker.setAttribute(IMarker.MESSAGE, err.getMessage());
					marker.setAttribute(IMarker.LOCATION, err.getFile());
					errors = errors.getTailconcAlert();
				}
			} catch (CoreException e) {
				System.out.println("TomBuildAnalyser: Something went wrong analysing errors");
			}
		}
	}
	
} // Class TomBuilAnalyser
