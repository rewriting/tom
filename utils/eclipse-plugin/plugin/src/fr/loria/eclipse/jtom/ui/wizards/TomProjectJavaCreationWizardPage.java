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

package fr.loria.eclipse.jtom.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;

import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * As addition to the JavaCapabilityConfigurationPage, the wizard does an early
 * project creation (so that linked folders can be defined) and, if an existing
 * external location was specified, offers to do a classpath detection
 */
public class TomProjectJavaCreationWizardPage extends
		JavaCapabilityConfigurationPage {

	private final TomProjectCreationWizardPage mainPage;

	private IConfigurationElement configElement;
	private int nBuilders = 0, nbLibs;
	IConfigurationElement[] builders, libraries;

	protected URI fCurrProjectLocation;
	protected IProject fCurrProject;

	protected boolean fKeepContent;

	/**
	 * Constructor for NewProjectCreationWizardPage.
	 */
	public TomProjectJavaCreationWizardPage(
			TomProjectCreationWizardPage mainPage, IConfigurationElement config) {
		super();
		this.mainPage = mainPage;
		fCurrProjectLocation = null;
		fCurrProject = null;
		fKeepContent = true;
		configElement = config;
		IConfigurationElement[] projectsetup = configElement
				.getChildren("projectsetup");
		if (projectsetup == null || projectsetup.length != 1) {
			JtomPlugin
					.log("Wizard descriptor must contain one ore more projectsetup tags");
			return;
		}
		builders = projectsetup[0].getChildren("builder");
		nBuilders = (builders == null) ? 0 : builders.length;
		libraries = projectsetup[0].getChildren("library");
		nbLibs = (libraries == null) ? 0 : libraries.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		if (visible) {
			changeToNewProject();
		} else {
			removeProject();
		}
		super.setVisible(visible);
	}

	private void changeToNewProject() {
		final IProject newProjectHandle = mainPage.getProjectHandle();
		final IPath newProjectLocation = mainPage.getLocationPath();

		fKeepContent = mainPage.getDetect();

		final boolean initialize = !(newProjectHandle.equals(fCurrProject) && newProjectLocation
				.equals(fCurrProjectLocation));

		final IWorkspaceRunnable op = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				updateProject(initialize, monitor);
			}
		};

		ISchedulingRule rule = newProjectHandle.getWorkspace().getRuleFactory()
				.modifyRule(newProjectHandle);

		try {
			newProjectHandle.getWorkspace().run(op, rule, 0, null);
		} catch (CoreException e) {
			final String title = JtomPlugin
					.getResourceString("ProjectCreationJavaWizardPage.error.title"); //$NON-NLS-1$
			final String message = JtomPlugin
					.getResourceString("ProjectCreationJavaWizardPage.error.message"); //$NON-NLS-1$
			ExceptionHandler.handle(e, getShell(), title, message);
		}
	}

	protected void updateProject(boolean initialize, IProgressMonitor monitor)
			throws CoreException {

		final boolean noProgressMonitor = !initialize && !mainPage.getDetect();

		if (monitor == null || noProgressMonitor) {
			monitor = new NullProgressMonitor();
		}
		try {
			monitor.beginTask(
					JtomPlugin
							.getResourceString("ProjectCreationJavaWizardPage.operation.initialize"), 2); //$NON-NLS-1$
			fCurrProject = mainPage.getProjectHandle();
			fCurrProjectLocation = new URI(mainPage.getLocationPath()
					.toString());
			createProject(fCurrProject, fCurrProjectLocation,
					new SubProgressMonitor(monitor, 1));

			if (initialize) {

				IClasspathEntry[] entries = null;
				IPath outputLocation = null;

				if (mainPage.getDetect()) {
					if (!fCurrProject.getFile(".classpath").exists()) {
						IPath projectPath = fCurrProject.getFullPath();
						List<IClasspathEntry> cpEntries = new ArrayList<IClasspathEntry>();
						cpEntries.add(JavaCore.newSourceEntry(projectPath));
						cpEntries.addAll(Arrays.asList(PreferenceConstants
								.getDefaultJRELibrary()));
						entries = (IClasspathEntry[]) cpEntries
								.toArray(new IClasspathEntry[cpEntries.size()]);
						outputLocation = projectPath;

					}

				} else if (mainPage.isSrcBin()) {
					IPreferenceStore store = PreferenceConstants
							.getPreferenceStore();
					IPath srcPath = new Path(
							store.getString(PreferenceConstants.SRCBIN_SRCNAME));
					IPath binPath = new Path(
							store.getString(PreferenceConstants.SRCBIN_BINNAME));

					if (srcPath.segmentCount() > 0) {
						IFolder folder = fCurrProject.getFolder(srcPath);
						createFolder(folder, true, true, null);
					}

					if (binPath.segmentCount() > 0 && !binPath.equals(srcPath)) {
						IFolder folder = fCurrProject.getFolder(binPath);
						createFolder(folder, true, true, null);
					}

					final IPath projectPath = fCurrProject.getFullPath();

					// configure the classpath entries, including the default
					// jre library.
					List<IClasspathEntry> cpEntries = new ArrayList<IClasspathEntry>();
					cpEntries.add(JavaCore.newSourceEntry(projectPath
							.append(srcPath)));
					cpEntries.addAll(Arrays.asList(PreferenceConstants
							.getDefaultJRELibrary()));
					entries = (IClasspathEntry[]) cpEntries
							.toArray(new IClasspathEntry[cpEntries.size()]);

					// configure the output location
					outputLocation = projectPath.append(binPath);
				} else {
					IPath projectPath = fCurrProject.getFullPath();
					List<IClasspathEntry> cpEntries = new ArrayList<IClasspathEntry>();
					cpEntries.add(JavaCore.newSourceEntry(projectPath));
					cpEntries.addAll(Arrays.asList(PreferenceConstants
							.getDefaultJRELibrary()));
					entries = (IClasspathEntry[]) cpEntries
							.toArray(new IClasspathEntry[cpEntries.size()]);

					outputLocation = projectPath;
				}
				// Add AIRCUBE BUNDLE Entries and other
				//
				/*
				 * On ajoute les libraries
				 */
				int userEntriesSize = entries.length;
				IClasspathEntry[] allEntries = new IClasspathEntry[userEntriesSize
						+ nbLibs];
				for (int i = 0; i < userEntriesSize; i++) {
					allEntries[i] = entries[i];
				}

				IConfigurationElement curr;
				IPath libPath;
				for (int i = 0; i < nbLibs; i++) {
					curr = libraries[i];

					String importSrc = curr.getAttribute("src");
					if (importSrc == null) {
						JtomPlugin
								.log("Projectsetup descriptor: library src missing");
						return;
					}
					String pluginId = curr.getAttribute("pluginId");
					if (pluginId == null) {
						JtomPlugin
								.log("Projectsetup descriptor: library src missing");
						return;
					}
					libPath = getJarPluginLocation(pluginId, importSrc);
					allEntries[userEntriesSize + i] = JavaCore.newLibraryEntry(
							libPath, null, null);
				}

				init(JavaCore.create(fCurrProject), outputLocation, allEntries,
						false);
			}
			monitor.worked(1);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {
			monitor.done();
		}
	}

	private IPath getJarPluginLocation(String pluginId, String importSrc) {
		IPath res = null;
		try {
			URL url = FileLocator.resolve(Platform.getBundle(pluginId)
					.getEntry(importSrc));

			res = new Path(url.getPath());
		} catch (Exception e) {
			JtomPlugin.log("Error getting location for " + importSrc
					+ " from plugiId " + pluginId);
		}
		return res;
	}

	/**
	 * Creates a folder and all parent folders if not existing. Project must
	 * exist. <code> org.eclipse.ui.dialogs.ContainerGenerator</code> is too
	 * heavy (creates a runnable)
	 */
	private void createFolder(IFolder folder, boolean force, boolean local,
			IProgressMonitor monitor) throws CoreException {
		if (!folder.exists()) {
			IContainer parent = folder.getParent();
			if (parent instanceof IFolder) {
				createFolder((IFolder) parent, force, local, null);
			}
			folder.create(force, local, monitor);
		}
	}

	/**
	 * Called from the wizard on finish.
	 */
	public void performFinish(IProgressMonitor monitor) throws CoreException,
			InterruptedException {
		try {
			monitor.beginTask(
					JtomPlugin
							.getResourceString("ProjectCreationJavaWizardPage.operation.create"),
					3);
			if (fCurrProject == null) {
				updateProject(true, new SubProgressMonitor(monitor, 1));
			}
			configureJavaProject(new SubProgressMonitor(monitor, 2));
		} finally {
			monitor.done();
			fCurrProject = null;
		}
	}

	/**
	 * Add extra stuff like builder and AIRCUBE bundle
	 */
	public void configureJavaProject(IProgressMonitor monitor)
			throws CoreException, InterruptedException {
		super.configureJavaProject(monitor);

		IProject project = mainPage.getProjectHandle();
		IProjectDescription description = mainPage.getProjectHandle()
				.getDescription();

		ICommand[] builds = new ICommand[nBuilders];
		ICommand ic;
		for (int i = 0; i < nBuilders; i++) {
			ic = description.newCommand();
			ic.setBuilderName(builders[i].getAttribute("id"));
			builds[i] = ic;
		}

		description.setBuildSpec(builds);
		project.setDescription(description, new SubProgressMonitor(monitor, 1));

	}

	private void removeProject() {
		if (fCurrProject == null || !fCurrProject.exists()) {
			return;
		}

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {

				final boolean noProgressMonitor = Platform.getLocation()
						.equals(fCurrProjectLocation);

				if (monitor == null || noProgressMonitor) {
					monitor = new NullProgressMonitor();
				}

				monitor.beginTask(
						JtomPlugin
								.getResourceString("ProjectCreationJavaWizardPage.operation.remove"),
						3);

				try {
					boolean removeContent = !fKeepContent
							&& fCurrProject
									.isSynchronized(IResource.DEPTH_INFINITE);
					fCurrProject.delete(removeContent, false, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
					fCurrProject = null;
					fKeepContent = false;
				}
			}
		};

		try {
			getContainer().run(false, true,
					new WorkspaceModifyDelegatingOperation(op, fCurrProject));
		} catch (InvocationTargetException e) {
			final String title = JtomPlugin
					.getResourceString("ProjectCreationJavaWizardPage.error.remove.title");
			final String message = JtomPlugin
					.getResourceString("ProjectCreationJavaWizardPage.error.remove.message");
			ExceptionHandler.handle(e, getShell(), title, message);
		} catch (InterruptedException e) {
			// cancel pressed
		}
	}

	/**
	 * Called from the wizard on cancel.
	 */
	public void performCancel() {
		removeProject();
	}
}
