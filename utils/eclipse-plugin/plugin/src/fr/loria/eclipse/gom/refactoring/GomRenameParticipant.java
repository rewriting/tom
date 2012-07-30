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
package fr.loria.eclipse.gom.refactoring;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

/**
 * 
 * 
 * This class will add participant to the refactoring rename action. All the
 * genereted java files will be deleted <br>
 * 
 * @author Martin GRANDCOLAS
 */
public class GomRenameParticipant extends RenameParticipant {
	private IProgressMonitor monitor;

	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		this.monitor = pm;
		return new RefactoringStatus();
	}

	// For instance , no changes are avaibles in the workspace: all is done during the initialization
	public Change createChange(IProgressMonitor pm) throws CoreException,OperationCanceledException {
		return null;
	}

	public String getName() {
		return "Rename classes referenced in Gom file Definiton";
	}

	protected boolean initialize(Object element) {
		// we found a IFile selected , the initialization is correctly finished
		if (element instanceof IFile) {
			IFile oldfile = (IFile) element;
			// if the IFile is a tom file
			if (Util.TOM_EXTENSION.equals(oldfile.getFileExtension())) {
				// We delete the generated .java , .parsed and .class files if
				// exist
				Util.deleteTomGeneratedFiles(oldfile);
				// we delete the generated packages which have the "oldname" name
				try {
					String oldname = oldfile.getName().substring(0, oldfile.getName().indexOf('.'));
					oldfile.getParent().accept(new ResourceVisitor(oldname, monitor));
				} catch (Exception e) {
					System.out.println("Failed to add a Resourcevisitor");
					// e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
}
