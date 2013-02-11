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

package fr.loria.eclipse.gom.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * This class implements the interface required by the desktop
 * for all 'New' wizards.  This wizard creates readme files.
 */
public class GOMFileCreationWizard extends Wizard implements INewWizard {
    private IStructuredSelection selection;
    private IWorkbench workbench;
    private GOMFileCreationWizardPage mainPage;
    
  /** (non-Javadoc)
   * Method declared on Wizard.
   */
  public void addPages() {
    mainPage = new GOMFileCreationWizardPage(workbench, selection);
    addPage(mainPage);
  }
  
  /** (non-Javadoc)
   * Method declared on INewWizard
   */
  public void init(IWorkbench workbench,IStructuredSelection selection) {
	  this.workbench = workbench;
	  this.selection = selection;
	  setWindowTitle(JtomPlugin.getResourceString("GOMFileCreationWizard.title"));
  }
  
  /** (non-Javadoc)
   * Method declared on IWizard
   */
  public boolean performFinish() {
	  return mainPage.finish();
  }
  
} //VASFileCreationWizard
