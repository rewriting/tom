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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import fr.loria.eclipse.jtom.JtomPlugin;


/**
 * @author julien
 */
public abstract class TomWizard extends Wizard {
    private IWorkbench workbench;
  	private IStructuredSelection selection;
  	
  	/**
  	 * Subclasses should override to perform the actions of the wizard.
  	 * This method is run in the wizard container's context as a workspace runnable.
  	 * @param monitor
  	 * @throws InterruptedException
  	 * @throws CoreException
  	 */
  	protected abstract void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException;
  	
  	/**
  	 * Returns the scheduling rule for creating the element.
  	 */
  	protected abstract ISchedulingRule getSchedulingRule();
  	
    /*
		 * @see Wizard#performFinish
		 */		
		public boolean performFinish() {
			IWorkspaceRunnable op= new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
					try {
						finishPage(monitor);
					} catch (InterruptedException e) {
						throw new OperationCanceledException(e.getMessage());
					}
				}
			};
			try {
				getContainer().run(false, true, new WorkbenchRunnableAdapter(op, getSchedulingRule()));
			} catch (InvocationTargetException e) {
				handleFinishException(getShell(), e);
				return false;
			} catch  (InterruptedException e) {
				return false;
			}
			return true;
		}
		
  	protected void selectAndReveal(IResource newResource) {
    		BasicNewResourceWizard.selectAndReveal(newResource, workbench.getActiveWorkbenchWindow());
    	}
  	
  	/* (non-Javadoc)
  	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
  	 */
  	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
  		this.workbench= workbench;
  		this.selection= currentSelection;
  	}
  	
  	public IStructuredSelection getSelection() {
  		return selection;
  	}

  	public IWorkbench getWorkbench() {
  		return workbench;
  	}
  
  	protected void openResource(final IFile resource) {
  			final IWorkbenchPage activePage= JtomPlugin.getActivePage();
  			if (activePage != null) {
  				final Display display= getShell().getDisplay();
  				if (display != null) {
  					display.asyncExec(new Runnable() {
  						public void run() {
  							try {
  								IDE.openEditor(activePage, resource, true);
  							} catch (PartInitException e) {
  								JtomPlugin.log(e);
  							}
  						}
  					});
  				}
  			}
  		}

		protected void handleFinishException(Shell shell, InvocationTargetException e) {
				String title= JtomPlugin.getResourceString("NewFileWizard.op_error.title"); //$NON-NLS-1$
				String message= JtomPlugin.getResourceString("NewFileWizard.op_error.message"); //$NON-NLS-1$
				ExceptionHandler.handle(e, shell, title, message);
			}
}
