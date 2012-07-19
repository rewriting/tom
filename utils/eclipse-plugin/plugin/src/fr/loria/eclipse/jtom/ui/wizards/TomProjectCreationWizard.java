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

package fr.loria.eclipse.jtom.ui.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import fr.loria.eclipse.jtom.JtomPlugin;

public class TomProjectCreationWizard extends TomWizard implements INewWizard, IExecutableExtension {

    private TomProjectCreationWizardPage mainPage;
    private TomProjectJavaCreationWizardPage javaPage;
    
    private IConfigurationElement fConfigElement;
    
    public TomProjectCreationWizard() {
      setDialogSettings(JtomPlugin.getDefault().getDialogSettings());
      setWindowTitle(JtomPlugin.getResourceString("ProjectWizard.title"));
    }

  	/**
  	 * Stores the configuration element for the wizard.  The config element will be used
  	 * in <code>performFinish</code> to set the result perspective.
  	 */
  	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
  		fConfigElement= cfig;
  		initializeDefaultPageImageDescriptor();
  	}
  	
  	private void initializeDefaultPageImageDescriptor() {
  		if (fConfigElement != null) {
  			String banner= fConfigElement.getAttribute("banner"); //$NON-NLS-1$
  			if (banner != null) {
  				ImageDescriptor desc= JtomPlugin.getDefault().getImageDescriptor(banner);
  				setDefaultPageImageDescriptor(desc);
  			}
  		}
  	}
  	
    /*
     * @see Wizard#addPages
     */	
    public void addPages() {
        super.addPages();
        mainPage= new TomProjectCreationWizardPage();
        addPage(mainPage);
        javaPage= new TomProjectJavaCreationWizardPage(mainPage, fConfigElement);
        addPage(javaPage);
    }
  	
    protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
    	javaPage.performFinish(monitor); // use the full progress monitor
      BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
      selectAndReveal(javaPage.getJavaProject().getProject());
    }
    
    protected ISchedulingRule getSchedulingRule() {
    	return mainPage.getProjectHandle();
    }
    
    /* (non-Javadoc)
     * @see IWizard#performCancel()
     */
    public boolean performCancel() {
        javaPage.performCancel();
        return super.performCancel();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#canFinish()
     */
    public boolean canFinish() {
        return super.canFinish();
    }
}