/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-20012 INRIA Nancy, France.
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

package fr.loria.eclipse.aircube.core.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import fr.loria.eclipse.jtom.JtomPlugin;


/**
 * @author julien
 *
 */
public abstract class AircubePropertyPage extends PropertyPage {
	  // High level widgets
	protected Group mainGeneratedVsCustomGroup;
	private Button useCustomCommandLine, useGeneratedCommandLine; 
	protected Text customCommandLineText, generatedCommandLineText;
	private Composite commandGenerationComposite;
	  // A Status composite
	private Text status;
	private Composite statusComposite;
	private Label errorStatusLabel, warnStatusLabel;
	private Image errorImg, warnImg;
	  // 
	protected String oldCustomCommandLine = null, oldGeneratedCommandLine = null;
	private boolean propertyChanged = false, isInitiallyCustomCommand = false,
	 generatedCommandLine = false;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Control res = null;
		if(checkResource()) {
			res = createDisplayedContent(parent);
			displayInitialSettings();
		} else {
			res = showBadResourceCheckedDisplay(parent);
		}
		return res;
	}
	
	
	/**
	 * @param parent
	 * @return what need to be displayed in case of checkResource returns false
	 */
	protected Control showBadResourceCheckedDisplay(Composite parent) {
		noDefaultAndApplyButton();
		Label label = new Label(parent, SWT.LEFT);
		label.setText("Not an expected file:!!");
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		return label;
	}


	/**
	 * check if necessary the resource if filter is not defined explicitely 
	 * in plugin.xml
	 */
	protected boolean checkResource() {
		return true;
	}


	protected Control createDisplayedContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1,false);
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
		
		
			// The status composite
		createStatusSection(composite);
		  // The main group
		createCustomVsGeneratedGroup(composite);
		  // The Custom Section
		createCustomSection();
			// The Custom Section
		createGenerationSection();
		
		return composite;
	}
	
	protected void displayInitialSettings() {
		IResource resource = (IResource) getElement();
		try {
			QualifiedName qName = new QualifiedName(JtomPlugin.USE_CUSTOM_COMMAND_PROPERTY, resource.getLocation().toString());
			String commandType = resource.getPersistentProperty(qName);
			if (commandType == null || commandType.equals("true")) {
				isInitiallyCustomCommand = true;
				useCustomCommandLine.setSelection(true);
				refreshCustomCommand(resource);
			} else {
				isInitiallyCustomCommand = false;
				useGeneratedCommandLine.setSelection(true);
				refreshGeneratedCommand(resource);
			}
		} catch (CoreException e) {
			System.out.println("Exception catched in TomFilePropertyPage::createContents "+e.getMessage());
		}
	}
	
	private void createCustomSection() {
		final IResource resource = (IResource) getElement();
		// the first button
		useCustomCommandLine = new Button(mainGeneratedVsCustomGroup, SWT.RADIO);
		useCustomCommandLine.setText("Custom command line");
		useCustomCommandLine.setToolTipText("For expert people");
		useCustomCommandLine.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				refreshCustomCommand(resource);
			}
		});
		  // and its corresponding component: a text box
		Composite comp = new Composite(mainGeneratedVsCustomGroup, SWT.NONE);
		customCommandLineText = new Text(comp,
				SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		customCommandLineText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				analyseCommandLine(customCommandLineText.getText());
			}
		});
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		customCommandLineText.setSize(300, 60);
		customCommandLineText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
		
	private void createGenerationSection() {
		final IResource resource = (IResource) getElement();
			// the second button
		useGeneratedCommandLine = new Button(mainGeneratedVsCustomGroup, SWT.RADIO);
		useGeneratedCommandLine.setText("Generated command line");
		useCustomCommandLine.setToolTipText("Hoover over each option to get some help");
		useGeneratedCommandLine.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
				refreshGeneratedCommand(resource);
			}
		});
		// and its composite
		commandGenerationComposite = new Composite(mainGeneratedVsCustomGroup, SWT.NONE);		
		GridLayout commandGenerationLayout = new GridLayout ();
		commandGenerationLayout.numColumns = 1;
		commandGenerationComposite.setLayout(commandGenerationLayout);
		createCustomCommandComposite(commandGenerationComposite);
	}

	/**
	 * @param mainComposite
	 */
	private void createCustomVsGeneratedGroup(Composite mainComposite) {
		// A first main group with 2 check button with each a component associated
		mainGeneratedVsCustomGroup = new Group(mainComposite, SWT.NONE);
		mainGeneratedVsCustomGroup.setText("Custom Vs. Generated command arguments");
		GridLayout mainLayout = new GridLayout(1, true);
		mainGeneratedVsCustomGroup.setLayout(mainLayout);
	}

	/**
	 * @param parent
	 */
	private void createStatusSection(Composite parent) {
		statusComposite = new Composite(parent, SWT.BORDER);
		//statusComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		statusComposite.setLayout(new FormLayout());
		statusComposite.setBackground(new Color(parent.getDisplay(), 255,255,255));
		String errorPath = null, warnPath = null;
		try {
			errorPath = FileLocator.resolve(JtomPlugin.getDefault().getBundle().getEntry("icons/error.gif")).getPath();
			warnPath  = FileLocator.resolve(JtomPlugin.getDefault().getBundle().getEntry("icons/warning.gif")).getPath();
		} catch (Exception e) {
			System.out.println("Exception when loading images for Tom property page");
		}
		
		errorImg = new Image(statusComposite.getDisplay(), errorPath);
		warnImg = new Image(statusComposite.getDisplay(), warnPath);
		errorStatusLabel = new Label(statusComposite, SWT.NONE);
		errorStatusLabel.setImage(errorImg);		
		warnStatusLabel = new Label(statusComposite, SWT.NONE);
		warnStatusLabel.setImage(warnImg);
		status = new Text(statusComposite, SWT.NONE);
		FormData fData = new FormData(300, 20);
		fData.left = new FormAttachment(5, 0);
		status.setLayoutData(fData);
		statusComposite.setVisible(false);
	}

	abstract protected boolean analyseCommandLine(String command);
	
	/**
	 * @param statusMessage
	 * @param warningLevel
	 */
	public boolean showStatus(String statusMessage, int warningLevel) {
		//System.out.println(statusMessage+" level "+warningLevel);
		if(warningLevel>0) {
			status.setText(statusMessage);
			if(warningLevel == 1) {
				errorStatusLabel.setVisible(false);
				warnStatusLabel.setVisible(true);
			} else {
				errorStatusLabel.setVisible(true);
				warnStatusLabel.setVisible(false);
			}
			statusComposite.setVisible(true);
			return false;
		} else {
			statusComposite.setVisible(false);
			return true;
		}
	}

	/**
	 * @param commandGenerationComposite
	 */
	protected abstract void createCustomCommandComposite(Composite commandGenerationComposite);
	
	public class generatedCommandSelectionAdapter extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			updateGeneratedCommandLineText(generateCommand());
		}
	};
	
	protected void createGeneratedCommandLineText() {
		Composite comp = new Composite(commandGenerationComposite, SWT.BORDER);
		generatedCommandLineText= new Text(comp, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		generatedCommandLineText.setSize(300, 60);
		GridData generatedCommandLineTextGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		generatedCommandLineTextGridData.horizontalSpan = 2;
		generatedCommandLineTextGridData.verticalSpan = 2;
		comp.setLayoutData(generatedCommandLineTextGridData);
		generatedCommandLineText.setEditable(false);
		generatedCommandLine = true;
	}
	
	/**
	 * @param resource
	 */
	private void refreshCustomCommand(IResource resource) {
		commandGenerationComposite.setVisible(false);
		customCommandLineText.setVisible(true);
		try{
			QualifiedName qName = new QualifiedName(JtomPlugin.CUSTOM_COMMAND_PROPERTY, resource.getLocation().toString());
			String command = resource.getPersistentProperty(qName);
			if(command == null) {
				customCommandLineText.setText("");
				oldCustomCommandLine = "";
			} else {
				customCommandLineText.setText(command);
				oldCustomCommandLine = command;
			}
		} catch (CoreException e) {
			System.out.println("Exception catched in TomFilePropertyPage::refreshCustomCommand "+e.getMessage());
		}

		
	}

	/**
	 * @param resource
	 */
	private void refreshGeneratedCommand(IResource resource) {
		String command = null;
		statusComposite.setVisible(false);
		commandGenerationComposite.setVisible(true);
		customCommandLineText.setVisible(false);
		
		try{
			QualifiedName qName = new QualifiedName(JtomPlugin.GENERATED_COMMAND_PROPERTY, resource.getLocation().toString());
			command = resource.getPersistentProperty(qName);
		} catch (CoreException e) {
			System.out.println("Exception catched in TomFilePropertyPage::refreshGeneratedCommand "+e.getMessage());
		}
		if(command == null) {
			updateGeneratedCommandLineText("");
			oldGeneratedCommandLine = "";
		} else {
			oldGeneratedCommandLine = command;
			updateGeneratedCommandLineText(command);
			refreshGeneratedCommandFromString(command);
		}
	}

	/**
	 * @param string
	 */
	protected void updateGeneratedCommandLineText(String command) {
		if(generatedCommandLine) {
			generatedCommandLineText.setText(command);
		}
	}


	protected abstract void refreshGeneratedCommandFromString(String command);


	protected void performApply() {
		try {
			myPerformApply();
		} catch (Exception e) {
			showStatus("Not able to apply: Bad command line "+status.getText(), 2);
		}
	}
	
	private void myPerformApply() throws Exception {
		IResource resource = (IResource) getElement();
		QualifiedName qName;
		String command, typeCommand;
		if(useCustomCommandLine.getSelection()) {
			command = customCommandLineText.getText().trim();
			typeCommand = JtomPlugin.CUSTOM_COMMAND_PROPERTY;
			propertyChanged = (oldCustomCommandLine != null && !command.equals(oldCustomCommandLine)) // commandLine has changed
		  	|| (oldCustomCommandLine == null && !command.equals("")) // new CommandLine
				|| (!isInitiallyCustomCommand); // change type of command
			if(!analyseCommandLine(command) && propertyChanged) {// custom commanf is not corrext now correct)
				throw new Exception("Not able to apply"); 
			}
		} else {
			command = generateCommand();
			typeCommand = JtomPlugin.GENERATED_COMMAND_PROPERTY;
			propertyChanged = (oldGeneratedCommandLine != null && !command.equals(oldGeneratedCommandLine)) // commandLine has changed
		    || (oldGeneratedCommandLine == null && !command.equals("")) // new CommandLine
				|| (isInitiallyCustomCommand); // change type of command
		}
		if(propertyChanged){
			qName = new QualifiedName(typeCommand, resource.getLocation().toString());
			resource.setPersistentProperty(qName, command);
			qName = new QualifiedName(JtomPlugin.USE_CUSTOM_COMMAND_PROPERTY, resource.getLocation().toString());
			resource.setPersistentProperty(qName, useCustomCommandLine.getSelection()?"true":"false");
			qName = new QualifiedName(JtomPlugin.USE_GENERATED_COMMAND_PROPERTY, resource.getLocation().toString());
			resource.setPersistentProperty(qName, useGeneratedCommandLine.getSelection()?"true":"false");
			propertyChanged = true;
		}
	}
	
	protected abstract String generateCommand();
	
	public void performDefaults() {
		IResource resource = (IResource) getElement();
		try {
			QualifiedName qName = new QualifiedName(JtomPlugin.USE_CUSTOM_COMMAND_PROPERTY, resource.getLocation().toString());
			String commandType = resource.getPersistentProperty(qName);
			if (commandType == null || commandType.equals("true")) {
				customCommandLineText.setText(oldCustomCommandLine);
			} else {
				qName = new QualifiedName(JtomPlugin.GENERATED_COMMAND_PROPERTY, resource.getLocation().toString());
				resource.setPersistentProperty(qName, oldGeneratedCommandLine);
				refreshGeneratedCommand(resource);
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean performOk() {
		try {
			myPerformApply();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		if(propertyChanged) {
			IResource resource = (IResource) getElement();
			try{
				resource.touch(null);
				resource.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean performCancel() {
		return false;
	}
	
} // Class TomFilePropertyPage
