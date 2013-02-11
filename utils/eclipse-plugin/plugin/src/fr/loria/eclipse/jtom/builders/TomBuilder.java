/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 Inria
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import tom.engine.Tom;
import tom.platform.PluginPlatform;
import tom.platform.PluginPlatformFactory;
import tom.platform.RuntimeAlert;
import tom.platform.adt.platformalert.types.Alert;
import tom.platform.adt.platformalert.types.AlertList;
import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * @author julien
 */
public class TomBuilder extends IncrementalProjectBuilder implements
		IResourceDeltaVisitor, IResourceVisitor {

	private static HashMap<IResource, RuntimeAlert> errorMap = new HashMap<IResource, RuntimeAlert>();

	private static HashMap<IResource, File> generationMap = new HashMap<IResource, File>();

	private static HashSet<IResource> nogenerationSet = new HashSet<IResource>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
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
		String tomExt = JtomPlugin.getDefault().getPreferenceStore()
				.getString(JtomPlugin.TOM_EXTENSION_PREFERENCE);
		if (tomExt.equals(path.getFileExtension())) {
			// Only build if the resource is included as a source CLASSPATH.
			IJavaProject jProject = JavaCore.create(getProject());
			if (jProject.isOnClasspath(resource)) {
				// look for the PackageFragmentRoot
				IPackageFragmentRoot packageFragmentRoot = null;
				IResource lookedResource = resource.getParent();
				while (packageFragmentRoot == null) {
					packageFragmentRoot = jProject
							.findPackageFragmentRoot(lookedResource
									.getFullPath());
					lookedResource = lookedResource.getParent();
					if (!jProject.isOnClasspath(lookedResource)) {
						break;
					}
				}
				if (packageFragmentRoot == null) {
					System.out.println("No IPackageFragmentRoot found");
					return false;
				}
				buildTomFile(resource, jProject, packageFragmentRoot.getPath());
			}
		}
		return true;
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		// call visit where all the stuff is done
		return visit(delta.getResource());
	}

	private void buildTomFile(IResource resource, IJavaProject jProject,
			IPath packageFragmentRoot) throws CoreException {
		if (resource.exists()) {
			IPath tomFilePath = resource.getLocation();
			System.out.println("TomBuilder in action for " + tomFilePath);

			// List of args
			ArrayList<String> finalCommandList = new ArrayList<String>();
			finalCommandList.add("--eclipse");
			finalCommandList.add(tomFilePath.toString());

			// get the command line arguments
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
						.println("TomBuilder: Exception catched getting resource command line "
								+ e.getMessage());
			}
			if (command == null
					|| (command != null && command.trim().equals(""))) {
				command = "";
			}
			boolean destdir = false;
			boolean output = false;
			boolean noGeneration = false;
			String args[] = command.trim().split(" ");
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				// System.out.println(arg);
				if (!arg.equals("")) {
					if (arg.equals("--nogeneration")) {
						noGeneration = true;
						break;
					} else if (arg.equals("--destdir") || arg.equals("-d")) {
						destdir = true;
					}
					if (arg.equals("--output") || arg.equals("-o")) {
						output = true;
					}
					finalCommandList.add(arg);
				}
			}

			if (noGeneration) {
				resource.deleteMarkers(null, false, IResource.DEPTH_ZERO);
				IMarker marker = resource.createMarker(IMarker.PROBLEM);
				marker.setAttribute(IMarker.LINE_NUMBER, 1);
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
				marker.setAttribute(IMarker.MESSAGE,
						"Nogeneration property set, update Tom property");
				marker.setAttribute(IMarker.LOCATION, 1);
				System.out.println("TomBuilder: Nogeneration aborting...");
				nogenerationSet.add(resource);
				return;
			}

			// By default, destDir is "<projectname>/src" folder under the local
			// workspace directory. If user create its project in another 
			// location, java generated source files will be in a wrong place
			String destdirPath = jProject.getProject().getParent()
					.getLocation().append(packageFragmentRoot).toString();

			// Get the list of project's source folders
			List<String> sourcesAbsPaths = new ArrayList<String>();
			for (IPackageFragmentRoot fragment : jProject
					.getAllPackageFragmentRoots()) {
				if (fragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
					sourcesAbsPaths.add(fragment.getCorrespondingResource()
							.getLocation().toFile().getAbsolutePath());
				}
			}

			// Find the resource's parents which is a project's source folder
			File checkedLoc = resource.getLocation().toFile();
			do {
				if (sourcesAbsPaths.contains(checkedLoc.getAbsolutePath())) {
					destdirPath = checkedLoc.getAbsolutePath();
					break;
				}
				checkedLoc = checkedLoc.getParentFile();
			} while (checkedLoc != null);

			// generate destdir if necessary

			if (!destdir && !output) {
				finalCommandList.add("--destdir");
				finalCommandList.add(destdirPath);
			}

			try {
				manageIncludes(finalCommandList);
			} catch (Exception e) {
				System.out.println("TomBuilder: Error getting include folders");
			}

			// the used tom.xml
			try {
				String xmlConfigFile = FileLocator
						.resolve(
								JtomPlugin
										.getDefault()
										.getBundle()
										.getEntry(
												"config" + File.separator
														+ "Tom.xml")).getPath();
				finalCommandList.add("-X");
				finalCommandList
						.add(new File(xmlConfigFile).getCanonicalPath());
			} catch (Exception e) {
				System.out.println("TomBuilder: Error getting xml config file");
			}
			// build the final String[]
			String commandArgs[] = new String[finalCommandList.size()];
			for (int i = 0; i < finalCommandList.size(); i++) {
				String arg = (String) finalCommandList.get(i);
				commandArgs[i] = arg;
				System.out.println(commandArgs[i]);
			}

			// Execute the TOM command
			try {
				boolean needRefresh = false;
				java.util.List<String> l = new ArrayList<String>();
				l.add(tomFilePath.toString());
				PluginPlatform platform = PluginPlatformFactory.getInstance()
						.create(commandArgs, Tom.LOG_RADICAL, l);
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
				int generationSuccess = 1;
				try {
					platform.run();
					generationSuccess = platform.getRunResult();
				} catch (Exception ex) {
					System.out.println("Exception occured during platform run:"
							+ ex.getMessage());
					generationSuccess = 1;
				}
				File generatedFile = null;
				// if the generation was successful
				if (generationSuccess == 0
						&& platform.getLastGeneratedObjects().get(0) != null) {
					generatedFile = new File(
							(String) (platform.getLastGeneratedObjects().get(0)));
					generationMap.put(resource, generatedFile);
					needRefresh = true;
				} else {
					// Remove previously generated file from the workspace if
					// already exists
					generatedFile = (File) generationMap.get(resource);
					if (generatedFile != null && generatedFile.exists()) {
						generatedFile.delete();
						needRefresh = true;
					}
				}

				RuntimeAlert globalAlert = platform.getGlobalStatusHandler()
						.getAlertForInput("-");
				RuntimeAlert currentPlatformAlert = platform
						.getGlobalStatusHandler().getAlertForInput(
								tomFilePath.toString());
				if (currentPlatformAlert == null) {
					currentPlatformAlert = new RuntimeAlert();
				}
				// this means that the errors came from Gom actually
				// or they can come from the compiler
				if (globalAlert != null) {
					// for obtaining the correct line number and the correct
					// file name
					Alert alertTom = currentPlatformAlert.getErrors()
							.getHeadconcAlert();
					AlertList gomErrors = globalAlert.getErrors();
					while (!gomErrors.isEmptyconcAlert()) {
						Alert alertGom = gomErrors.getHeadconcAlert();
						currentPlatformAlert.addError(alertGom.getMessage(),
								alertTom.getFile(), alertTom.getLine());
						gomErrors = gomErrors.getTailconcAlert();
					}
				}
				// analyse also the errors that come from other files other than
				// the
				// current one - from an invalid import for example: if true,
				// than the
				// errors will corespond to the included file and no the the
				// current one
				Iterator<?> it = platform.getGlobalStatusHandler()
						.getAlertMap().keySet().iterator();
				while (it.hasNext()) {
					String fileName = (String) it.next();
					// avoid duplications
					if (tomFilePath.toOSString().equals(fileName)
							|| tomFilePath.toString().equals(fileName)
							|| "-".equals(fileName)) {
						continue;
					}
					RuntimeAlert ra = (RuntimeAlert) platform
							.getAlertForInput(fileName);
					AlertList errors = ra.getErrors();
					while (!errors.isEmptyconcAlert()) {
						Alert alert = errors.getHeadconcAlert();
						currentPlatformAlert.addError(alert.getMessage(),
								alert.getFile(), 1);
						errors = errors.getTailconcAlert();
					}
				}
				errorMap.put(resource, currentPlatformAlert);

				// System.out.println("getting runtimealert for:"
				// +tomFilePath.toString());

				// refresh folder
				if (needRefresh && generatedFile != null) {
					IPath generatedResourcePath = new Path(
							generatedFile.getParent()).setDevice(null)
							.removeFirstSegments(
									getProject().getLocation().segmentCount());
					IResource generatedResource = getProject().findMember(
							generatedResourcePath);
					if (generatedResource != null) {
						System.out.println("Tom Builder Refreshing "
								+ generatedResourcePath);
						generatedResource.refreshLocal(
								IResource.DEPTH_INFINITE, null);
					}
				}
			} catch (Throwable e) { // because aparrently eclipse ingnores the
				// errors
				// like NoDefClassFound
				System.out.println("Exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Handles the includes - both the from the user and the ones needed for tom
	 * 
	 * @param finalCommandList
	 *            - the command list to add the "--import" to
	 * @throws IOException
	 */
	public static void manageIncludes(ArrayList<String> finalCommandList)
			throws IOException {

		String includedFile = JtomPlugin.getDefault().getPreferenceStore()
				.getString(JtomPlugin.TOM_INCLUDE_PREFERENCE);

		// use plug-in default include path
		String defaultIncludedFile = FileLocator.resolve(
				JtomPlugin.getDefault().getBundle().getEntry("include"))
				.getPath();
		String includeFolders = JtomPlugin.getResourceString("include.folders");
		includeFolders(defaultIncludedFile + includeFolders, finalCommandList);

		// String defaultJavaIncludedFile = defaultIncludedFile + "java" +
		String canonicalIncludedFile = new File(includedFile)
				.getCanonicalFile().toString()
				+ File.pathSeparator
				+ new File(defaultIncludedFile).getCanonicalFile().toString();
		String paths[] = canonicalIncludedFile.split(File.pathSeparator);
		for (int i = 0; i < paths.length; i++) {
			if (!paths[i].equals("") && !paths[i].contains(".svn")) {
				finalCommandList.add("--import");
				finalCommandList.add(paths[i]);
				System.out.println("import: " + paths[i]);
			}
		}
	}

	private static void includeFolders(String rootForInclude,
			ArrayList<String> finalCommandList) throws IOException {

		File files[] = new File(rootForInclude).getCanonicalFile().listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()
					&& !files[i].getCanonicalPath().contains(".svn")) {
				includeFolders(files[i].getCanonicalPath(), finalCommandList);
			}
		}

		finalCommandList.add("--import");
		finalCommandList.add(new File(rootForInclude).getCanonicalPath());
	}

	public static boolean isNoGeneration(IResource res) {
		return nogenerationSet.remove(res);
	}

	public static RuntimeAlert getLastAlert(IResource res) {
		return (RuntimeAlert) errorMap.remove(res);
	}

	public static String getLastBuildResource(IResource res) {
		File lastFile = (File) generationMap.remove(res);
		return (lastFile == null) ? null : lastFile.toString();
	}

	public static String getErrorsStatistics() {
		String res = "There are ";
		res += errorMap.size() + " resources with Tom errors\n";
		int totalError = 0;
		Iterator<IResource> it = errorMap.keySet().iterator();
		int error = 0;
		while (it.hasNext()) {
			IResource resource = (IResource) it.next();
			error = ((RuntimeAlert) errorMap.get(resource)).getErrors()
					.getChildCount();
			res += resource.toString() + " " + error + "\n";
			totalError += error;
		}
		res += totalError + " errors";
		return res;
	}

	public static HashMap<IResource, RuntimeAlert> getErrorMap() {
		return errorMap;
	}

	public static void clearErrorMap() {
		errorMap.clear();
	}

}
