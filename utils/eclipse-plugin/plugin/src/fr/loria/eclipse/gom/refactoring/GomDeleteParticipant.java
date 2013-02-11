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
package fr.loria.eclipse.gom.refactoring;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.DeleteParticipant;

/**
 * This class will delete the genereted java packages when a gom file is
 * destroyed
 * 
 * @author Martin GRANDCOLAS
 * 
 */

public class GomDeleteParticipant extends DeleteParticipant {

	private IProgressMonitor monitor;

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		this.monitor = pm;
		return new RefactoringStatus();
	}

	// For instance , no changes are avaibles in the workspace
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
	OperationCanceledException {
		return null;
	}

	@Override
	public String getName() {
		return "Delete classes referenced in Gom file Definiton";
	}

	public void delete(IFile file) {
		this.initialize(file);
	}

	@Override
	protected boolean initialize(Object element) {
		// we found a IFile selected , the initialisation is correctly finished

		if (element instanceof IFile) {
			IFile oldfile = (IFile) element;

			// if the IFile is a tom file
			if (Util.TOM_EXTENSION.equals(oldfile.getFileExtension())) {
				IPath path = Util.getPathDestination(oldfile);
				// System.out.println("path ="+path.toString());
				IContainer container;
				// this test is because of incompatibilities between eclipse 3.2
				// and 3.3
				if (path != null && !"".equals(path.toString())) {
					container = oldfile.getProject().getFolder(path);
				} else {
					container = oldfile.getParent();
				}
				Util.deleteTomGeneratedFiles(oldfile);
				// delete the files with the oldfile package name
				try {
					String oldname = oldfile.getProjectRelativePath()
					.removeFileExtension().lastSegment();
					container.getParent().accept(
							new ResourceVisitor(oldname, monitor));
				} catch (Exception e) {
					System.out.println("Failed to add a Resourcevisitor");
				}
			} else if (Util.GOM_EXTENSION.equals(oldfile.getFileExtension())) {
				// The IFile is a gom file
				// we delete the generated packages containing the modules name
				ArrayList<String> modules = Util.findModuleName(oldfile);
				Path path = (Path) Util.getPathDestination(oldfile);
				// no destination path was selected
				boolean noDestinationPath = (path.toString().equals(oldfile
						.getParent().getProjectRelativePath().toString()));

				for (int i = 0; i < modules.size(); i++) {
					// delete for each module package
					try {
						if (noDestinationPath) {
							oldfile.getParent().getFolder(
									new Path((String) modules.get(i))).delete(
											true, monitor);
						} else {
							oldfile.getProject().getFolder(path).getFolder(
									new Path((String) modules.get(i))).delete(
											true, monitor);
						}
					} catch (CoreException e) {
						System.out.println("Failed to delete the "
								+ oldfile.getParent().getFolder(
										new Path((String) modules.get(i)))
										+ " directory");
					}
				}
			}
			return true;
		}
		return false;
	}
}
