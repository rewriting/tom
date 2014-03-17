/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2014 Inria Nancy, France.
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

package fr.loria.eclipse.gom.builders;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;

//import com.sun.tools.javac.util.List;

import tom.gom.Gom;
import tom.platform.PluginPlatform;
import tom.platform.PluginPlatformFactory;
import tom.platform.RuntimeAlert;
import tom.platform.adt.platformalert.types.Alert;
import tom.platform.adt.platformalert.types.AlertList;
import fr.loria.eclipse.jtom.JtomPlugin;
import fr.loria.eclipse.jtom.builders.TomBuilder;

public class GomBuilder extends IncrementalProjectBuilder implements
		IResourceDeltaVisitor, IResourceVisitor {

	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
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

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		getProject().accept(this /* as a IResourceVisitor */);
	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor (this) does the work.
		delta.accept(this /* as a IResourceDeltaVisitor */);
	}

	public boolean visit(IResource resource) throws CoreException {
		IPath path = resource.getFullPath();
		if ("gom".equals(path.getFileExtension())) {
			// Only build if the resource is included as a source.
			if (JavaCore.create(getProject()).isOnClasspath(resource)) {
				buildGOMFile(resource);
			}
		}
		return true;
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		// call visit where all the stuff is done
		return visit(delta.getResource());
	}

	private void buildGOMFile(IResource resource) throws CoreException {
		if (resource.exists()) {

			IPath gomFilePath = resource.getLocation();

			System.out.println("GOMBuilder in action for " + gomFilePath);
			// clear previous markers
			resource
					.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_ZERO);

			String commandArgs[] = {};
			String command = null;
			try {
				QualifiedName qName = new QualifiedName(
						JtomPlugin.USE_CUSTOM_COMMAND_PROPERTY, resource
								.getLocation().toString());
				String commandType = resource.getPersistentProperty(qName);
				if (commandType == null || commandType.equals("true")) {
					qName = new QualifiedName(
							JtomPlugin.CUSTOM_COMMAND_PROPERTY, resource
									.getLocation().toString());
					command = resource.getPersistentProperty(qName);
				} else {
					qName = new QualifiedName(
							JtomPlugin.GENERATED_COMMAND_PROPERTY, resource
									.getLocation().toString());
					command = resource.getPersistentProperty(qName);
				}
			} catch (CoreException e) {
				System.out
						.println("GOMBuilder: Exception catched getting resource command line "
								+ e.getMessage());
			}
			if (command != null && !command.trim().equals("")) {
				commandArgs = command.split(" ");
			}

			// create a good command
			ArrayList<String> finalCommand = new ArrayList<String>();
			finalCommand.add(resource.getLocation().toString());
			
			IJavaProject javaProject = (IJavaProject) JavaCore.create(getProject());
			
			// handle package name
			IPackageFragment packSuffix = javaProject.findPackageFragment(resource.getParent().getFullPath());
			finalCommand.add("--package");
			finalCommand.add(packSuffix.getElementName());
			
			// destination dir
			String destDir = packSuffix.getParent().getResource().getLocation().toString();
			finalCommand.add("--destdir");
			finalCommand.add(destDir);
			int baseDirArgPos = finalCommand.size() - 1;

			// finalCommand.add("--verbose");
			// the used Gom.xml
			try {
				String xmlConfigFile = FileLocator.resolve(
						JtomPlugin.getDefault().getBundle().getEntry(
							"config" + File.separator + "Gom.xml"))
						.getPath();				
				finalCommand.add("-X");
				finalCommand.add(new File(xmlConfigFile).getCanonicalPath());
			} catch (Exception e) {
				System.out.println("GomBuilder: Error getting xml config file");
			}

			String notManagedOption = "";
			boolean noGeneration = false;
			int i = 0;
			try {
				for (i = 0; i < commandArgs.length; i++) {
					if (commandArgs[i].equals("")) {
						// does not matter
					} else if (commandArgs[i].equals("--nogeneration")) {
						// not managed
						noGeneration = true;
						break;
					} else if (commandArgs[i].equals("--destdir")
							|| commandArgs[i].equals("-d")) {
						i++;
						finalCommand.set(baseDirArgPos, commandArgs[i]);
						destDir = commandArgs[i];
					} else {
						finalCommand.add(commandArgs[i]);
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				notManagedOption += "GomBuilder: Error: '" + commandArgs[--i]
						+ "' is supposed to have something after";
			}

			if (noGeneration) {
				IMarker marker = resource.createMarker(IMarker.PROBLEM);
				marker.setAttribute(IMarker.LINE_NUMBER, 1);
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
				marker.setAttribute(IMarker.MESSAGE,
						"Nogeneration property set, update Gom property");
				marker.setAttribute(IMarker.LOCATION, 1);
				System.out.println("GOMBuilder: Nogeneration aborting...");
				return;
			}

			try{
				TomBuilder.manageIncludes(finalCommand);
			}catch(IOException e){
				System.out.println("Unable to generate imports:" + e.getMessage());
			}
			
			//System.out.println("FINAL COMMAND:" + finalCommand);
			
			String[] commandArray = new String[finalCommand.size()];
			for (i = 0; i < finalCommand.size(); i++) {
				commandArray[i] = (String) finalCommand.get(i);
				//System.out.println(commandArray[i]);
			}

			try {
				java.util.List<String> l = new ArrayList<String>();
				l.add(gomFilePath.toString());
				PluginPlatform platform = PluginPlatformFactory.getInstance()
						.create(commandArray, Gom.LOGRADICAL, l); 
				if (platform == null) {
					IMarker marker = resource.createMarker(IMarker.PROBLEM);
					marker.setAttribute(IMarker.LINE_NUMBER, 1);
					marker.setAttribute(IMarker.SEVERITY,
							IMarker.SEVERITY_ERROR);
					marker.setAttribute(IMarker.MESSAGE,
							"Unable to create a PluginPlatform");
					marker.setAttribute(IMarker.LOCATION, 1);
					System.out.println("Unable to create a PluginPlatform");
					return;
				}
				platform.clearGlobalStatusHandler();
				try{
					platform.run();
				}catch(Exception ex){
					System.out.println("Exception occured during platform run in GomBuilder:" + ex.getMessage());
				}
				RuntimeAlert alert = platform.getGlobalStatusHandler().getAlertForInput(resource
						.getLocation().toString());
				if (alert != null) {
					// Analyse output and errors
					AlertList errors = alert.getErrors();
					Alert error;
					while (!errors.isEmptyconcAlert()) {
						error = errors.getHeadconcAlert();
						IMarker marker = resource.createMarker(IMarker.PROBLEM);
						marker.setAttribute(IMarker.LINE_NUMBER, error
								.getLine());
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_ERROR);
						marker
								.setAttribute(IMarker.MESSAGE, error
										.getMessage());
						errors = errors.getTailconcAlert();
					}

					errors = alert.getWarnings();
					while (!errors.isEmptyconcAlert()) {
						error = errors.getHeadconcAlert();
						IMarker marker = resource.createMarker(IMarker.PROBLEM);
						marker.setAttribute(IMarker.LINE_NUMBER, error
								.getLine());
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_WARNING);
						marker
								.setAttribute(IMarker.MESSAGE, error
										.getMessage());
						errors = errors.getTailconcAlert();
					}
				}
			} catch (Exception e) {
				System.out
						.println("GOMBuilder: Error: Unhandled exception occurs during GOM generation: "
								+ e.getMessage());
				e.printStackTrace();
			}
			// Refresh
			IPath generatedResourcePath = new Path(destDir).setDevice(null)
					.removeFirstSegments(
							getProject().getLocation().segmentCount());
			// System.out.println("Refreshing gom generation
			// "+generatedResourcePath);
			IResource destFolder = getProject().findMember(
					generatedResourcePath);
			if (destFolder != null) {
				destFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
			} else {
				getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			}
		}
	}

} // Class GomBuilder
