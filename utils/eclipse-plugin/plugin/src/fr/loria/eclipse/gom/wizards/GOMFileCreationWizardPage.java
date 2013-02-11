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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import fr.loria.eclipse.jtom.JtomPlugin;

public class GOMFileCreationWizardPage extends WizardNewFileCreationPage {
	private IWorkbench	workbench;
	
	// widgets

	private Button  openFileCheckbox;
	private Text commandLineText, moduleNameText;

	/**
	 * Creates the page for the readme creation wizard.
	 *
	 * @param workbench  the workbench on which the page should be created
	 * @param selection  the current selection
	 */
	public GOMFileCreationWizardPage(IWorkbench workbench, IStructuredSelection selection) {
	  super("GOMFileCreationPage", selection);
	  setTitle(JtomPlugin.getResourceString("GOMFileCreationWizardPage.title"));
	  setDescription(JtomPlugin.getResourceString("GOMFileCreationWizardPage.description"));
	  this.workbench = workbench;
	}

	/** (non-Javadoc)
	 * Method declared on IDialogPage.
	 */
	public void createControl(Composite parent) {
	    // inherit default container and name specification widgets
	  super.createControl(parent);
	  Composite composite = (Composite)getControl();
		
	  this.setFileName("");
	
	    //module name field
	  Label label2 = new Label(composite, SWT.LEFT);
	  label2.setText("Module name:");
	  moduleNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
	  GridData data2 = new GridData();
	  data2.horizontalAlignment = GridData.FILL;
	  data2.grabExcessHorizontalSpace = true;
	  data2.verticalAlignment = GridData.CENTER;
	  data2.grabExcessVerticalSpace = false;
	  moduleNameText.setLayoutData(data2);
	  
	    // vas arguments field
	  Label label = new Label(composite, SWT.LEFT);
	  label.setText("GOM File arguments command:(optional: can be set later using property page)");
	  commandLineText = new Text(composite, SWT.SINGLE | SWT.BORDER);
	  GridData data = new GridData();
	  data.horizontalAlignment = GridData.FILL;
	  data.grabExcessHorizontalSpace = true;
	  data.verticalAlignment = GridData.CENTER;
	  data.grabExcessVerticalSpace = false;
	  commandLineText.setLayoutData(data);
	  
	 
	  	// open file for editing checkbox
	  openFileCheckbox = new Button(composite,SWT.CHECK);
	  openFileCheckbox.setText("Open File for editing when done");
	  openFileCheckbox.setSelection(true);

	  setPageComplete(validatePage());
	
	}

	protected boolean validatePage() {
	  return super.validatePage() && hasCorrectFileNameExtension();
	}
	
	/**
     * @return
     */
    private boolean hasCorrectFileNameExtension() {
      String fileName =getFileName();
      if(!fileName.endsWith(".gom")) {
        setErrorMessage(fileName+" should have the extension: .gom");
        return false;
      } else {
      	moduleNameText.setText(fileName.substring(0, fileName.length()-4));
      }
      return true;
    }

    /**
   * Creates a new file resource as requested by the user. If everything
   * is OK then answer true. If not, false will cause the dialog
   * to stay open.
   *
   * @return whether creation was successful
   * @see ReadmeCreationWizard#performFinish()
   */
  public boolean finish() {
      // create the new file resource
	  IFile newFile = createNewFile();
	  if (newFile == null) {
	    return false;	// ie.- creation was unsuccessful
	  }
	  // attach the command line args
	  try {
	    String commandText = commandLineText.getText().trim();
	    
		  QualifiedName qName = new QualifiedName(JtomPlugin.USE_CUSTOM_COMMAND_PROPERTY, newFile.getLocation().toString());
		  newFile.setPersistentProperty(qName, "true");
		  qName = new QualifiedName(JtomPlugin.USE_GENERATED_COMMAND_PROPERTY, newFile.getLocation().toString());
		  newFile.setPersistentProperty(qName, "false");
		  qName = new QualifiedName(JtomPlugin.CUSTOM_COMMAND_PROPERTY, newFile.getLocation().toString());
		  newFile.setPersistentProperty(qName, commandText);
	  } catch (CoreException e) {
		  e.printStackTrace();
	  }

	  // Since the file resource was created fine, open it for editing
	  // if requested by the user
	  try {
	    if (openFileCheckbox.getSelection()) {
			  IWorkbenchWindow dwindow = workbench.getActiveWorkbenchWindow();
			  IWorkbenchPage page = dwindow.getActivePage();
			  if (page != null) {
				  IDE.openEditor(page, newFile, true);
			  }
		  }
	  } catch (PartInitException e) {
		  e.printStackTrace();
		  return false;
 	  }
	
	  try {
	    newFile.touch(null);
			newFile.getParent().refreshLocal(IResource.DEPTH_ONE, null);
		} catch (Exception e) {
			System.out.println("Error touching "+newFile.getName());
		}
		return true;
  }
  
 /** 
  * The <code>ReadmeCreationPage</code> implementation of this
  * <code>WizardNewFileCreationPage</code> method 
  * generates sample headings for sections and subsections in the
  * newly-created Readme file according to the selections of self's
  * checkbox widgets
  */
  protected InputStream getInitialContents() {
	  StringBuffer sb = new StringBuffer();
	  sb.append("module "+moduleNameText.getText()+"\n\timports\n\tabstract syntax\n");
	  return new ByteArrayInputStream(sb.toString().getBytes());
  }
  
  /** (non-Javadoc)
   * Method declared on WizardNewFileCreationPage.
   */
  protected String getNewFileLabel() {
	  return "GOM file name (*.gom)";
  }

  /** (non-Javadoc)
   * Method declared on WizardNewFileCreationPage.
   
   public void handleEvent(Event e) {
   Widget source = e.widget;
   super.handleEvent(e);
   }
   */
}
