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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import fr.loria.eclipse.jtom.JtomPlugin;


/**
 * @author julien
 */
public class TomFilePropertyCreationWizardPage extends WizardPage {

  private Text commandLineField;

  public TomFilePropertyCreationWizardPage() {
    super("TomFilePropertyCreationWizardPage");
    setTitle(JtomPlugin.getResourceString("TomFilePropertyCreationWizard.title"));
    setDescription(JtomPlugin.getResourceString("TomFilePropertyCreationWizard.description"));
  }

  public void createControl(Composite parent) {
	initializeDialogUnits(parent);
  			
  	Composite topLevel = new Composite(parent,SWT.NONE);
  	topLevel.setLayout(new GridLayout());
  	topLevel.setLayoutData(new GridData(
  	GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
  	topLevel.setFont(parent.getFont());
  			
  	Label label = new Label(topLevel, SWT.LEFT);
  	label.setText("Tom arguments command:(optional: can be set later using property page)");
  		
  	commandLineField = new Text(topLevel, SWT.SINGLE | SWT.BORDER);
  	GridData data = new GridData();
  	data.horizontalAlignment = GridData.FILL;
  	data.grabExcessHorizontalSpace = true;
  	data.verticalAlignment = GridData.CENTER;
  	data.grabExcessVerticalSpace = false;
  	commandLineField.setLayoutData(data);
  		
  	setControl(topLevel);
  }

  public String getCommandLine() {
  	return commandLineField.getText().trim();
  }
  
} //class TomFilePropertyCreationWizardPage