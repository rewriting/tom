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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.ui.INewWizard;

import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * @author julien
 */
public class TomFileCreationWizard extends TomWizard implements INewWizard {

	private NewClassWizardPage javaPage;
	private TomFilePropertyCreationWizardPage propertyPage;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public TomFileCreationWizard() {
		super();
		setDialogSettings(JtomPlugin.getDefault().getDialogSettings());
		setWindowTitle(JtomPlugin
				.getResourceString("TomFileCreationWizard.title"));
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		javaPage = new NewClassWizardPage();
		addPage(javaPage);
		javaPage.init(getSelection());
		propertyPage = new TomFilePropertyCreationWizardPage();
		addPage(propertyPage);
	}

	protected void finishPage(IProgressMonitor monitor)
			throws InterruptedException, CoreException {
		javaPage.createType(monitor); // use the full progress monitor
		IResource resource = javaPage.getModifiedResource();

		if (resource != null) {
			IPath newFilePath = resource
					.getProjectRelativePath()
					.removeFileExtension()
					.addFileExtension(
							JtomPlugin
									.getDefault()
									.getPreferenceStore()
									.getString(
											JtomPlugin.TOM_EXTENSION_PREFERENCE));
			try {
				resource.copy(new Path(newFilePath.lastSegment()), true, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			IFile dest = resource.getProject().getFile(newFilePath);

			if (dest != null) {
				String property = propertyPage.getCommandLine().trim();
				if (property.equals("")) {

					// delete java file
					IType createdType = javaPage.getCreatedType();
					IJavaElement createdJava = javaPage.getPackageFragment()
							.getCompilationUnit(
									javaPage.getTypeName() + ".java");
					createdType.getJavaModel().delete(
							new IJavaElement[] { createdJava }, true, null);

					IResource parent = resource.getParent();
					resource.delete(true, null);
					parent.refreshLocal(IResource.DEPTH_ONE, monitor);
				}
				QualifiedName qName = new QualifiedName(
						JtomPlugin.USE_CUSTOM_COMMAND_PROPERTY, dest
								.getLocation().toString());
				dest.setPersistentProperty(qName, "true");
				qName = new QualifiedName(
						JtomPlugin.USE_GENERATED_COMMAND_PROPERTY, dest
								.getLocation().toString());
				dest.setPersistentProperty(qName, "false");
				qName = new QualifiedName(JtomPlugin.CUSTOM_COMMAND_PROPERTY,
						dest.getLocation().toString());
				dest.setPersistentProperty(qName, property);

				selectAndReveal(dest);

				dest.setContents(getInitialContents(), false, true, null);
				openResource((IFile) dest);
			}
		}
	}

	/**
	 * A simple example of tom code that is put in the initial file
	 * @return
	 */
	private InputStream getInitialContents() {
		StringBuffer sb = new StringBuffer();
		javaPage.getCreatedType().getElementName();
		sb.append("import " 
				+ javaPage.getCreatedType().getElementName().toLowerCase()
				+ ".example.*;\n\npublic class "
				+ javaPage.getCreatedType().getElementName()
				+ "{\n\n\t%gom{ \n\t\t module Example \n\t\t imports int\n\t \t abstract syntax \n\t}\n}");
		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	protected ISchedulingRule getSchedulingRule() {
		return javaPage.getModifiedResource();
	}

} //class TomFileCreationWizard