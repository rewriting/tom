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

package fr.loria.eclipse.aircube.core.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;

import fr.loria.eclipse.jtom.mappinggenerator.MappingGeneratorWizard;

/**
 * @author julien
 * 
 */
public class PopupBuilder implements IObjectActionDelegate {

	private IWorkbenchPart part;
	private StructuredSelection selection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if (action
				.getId()
				.equalsIgnoreCase(
				"fr.loria.eclipse.jtom.popup.actions.MappingGeneratorPopupBuilderJava.Action")) {
			showMappingWizard(false);
		} else if (action
				.getId()
				.equalsIgnoreCase(
				"fr.loria.eclipse.jtom.popup.actions.MappingGeneratorPopupBuilderFolder.Action")) {
			showMappingWizard(true);
		} else {
			IFile file = (IFile) (selection.getFirstElement());
			try {
				System.out.println("Touching " + file.getName());
				file.touch(null);
				file.getParent().refreshLocal(IResource.DEPTH_ONE, null);
			} catch (Exception e) {
				System.out.println("Error touching " + file.getName());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.part = targetPart;
		this.selection = (StructuredSelection) (this.part.getSite()
				.getWorkbenchWindow().getActivePage().getSelection());
	}
	
	private void showMappingWizard(boolean isFolder){
		IWorkbenchWizard wizard = new MappingGeneratorWizard(isFolder);		
		wizard.init(PlatformUI.getWorkbench(), selection);
		WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.open();
	}

} //class PopupBuilder