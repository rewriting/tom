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

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardPage;

import org.eclipse.jdt.ui.PreferenceConstants;

import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * The first page of the <code>TomProjectCreationWizard</code>.
 */
public class TomProjectCreationWizardPage extends WizardPage {


	/**
	 * Request a project name. Fires an event whenever the text field is
	 * changed, regardless of its content.
	 */
	private final class NameGroup extends Observable {

		protected final Text fNameField;

		public NameGroup(Composite composite) {
			final Composite nameComposite= new Composite(composite, SWT.NONE);
			nameComposite.setFont(composite.getFont());
			nameComposite.setLayout(initGridLayout(new GridLayout(2, false), false));
			nameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			// label "Project name:"
			final Label nameLabel= new Label(nameComposite, SWT.NONE);
			nameLabel.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.NameGroup.label.text")); //$NON-NLS-1$
			nameLabel.setFont(composite.getFont());

			// text field for project name
			fNameField= new Text(nameComposite, SWT.BORDER);
			fNameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fNameField.setFont(composite.getFont());

			fNameField.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					fNameField.setSelection(0, fNameField.getText().length());
				}
			});

			fNameField.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					fireEvent();
				}
			});

			setChanged();
		}
		
		protected void fireEvent() {
			setChanged();
			notifyObservers();
		}

		public String getName() {
			return fNameField.getText().trim();
		}

		public void setFocus() {
			fNameField.setFocus();
		}
	}

	/**
	 * Request a location. Fires an event whenever the checkbox or the location
	 * field is changed, regardless of whether the change originates from the
	 * user or has been invoked programmatically.
	 */
	private final class LocationGroup extends Observable implements Observer {

		protected final Button fWorkspaceRadio;
		protected final Button fExternalRadio;
		protected final Label fLocationLabel;
		protected final Text fLocationField;
		protected final Button fLocationButton;
		protected String fExternalLocation;

		public LocationGroup(Composite composite) {

			final int numColumns= 3;
			
			fExternalLocation= ""; //$NON-NLS-1$

			final Group group= new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(initGridLayout(new GridLayout(numColumns, false), true));
			group.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.LocationGroup.title")); //$NON-NLS-1$

			fWorkspaceRadio= new Button(group, SWT.RADIO | SWT.RIGHT);
			fWorkspaceRadio.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.LocationGroup.workspace.desc")); //$NON-NLS-1$
			fWorkspaceRadio.setSelection(true);
			
			final GridData gd= new GridData();
			gd.horizontalSpan= numColumns;
			fWorkspaceRadio.setLayoutData(gd);
			
			fExternalRadio= new Button(group, SWT.RADIO | SWT.RIGHT);
			fExternalRadio.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.LocationGroup.external.desc")); //$NON-NLS-1$
			fExternalRadio.setSelection(false);

			final GridData gd2= new GridData();
			gd2.horizontalSpan= numColumns;
			fExternalRadio.setLayoutData(gd2);

			fLocationLabel= new Label(group, SWT.NONE);
			fLocationLabel.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.LocationGroup.locationLabel.desc")); //$NON-NLS-1$
			fLocationLabel.setEnabled(false);
			fLocationLabel.setLayoutData(new GridData());

			fLocationField= new Text(group, SWT.BORDER);
			fLocationField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fLocationField.setEnabled(false);
			fLocationField.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (fLocationField.getEnabled()) {
						fExternalLocation= fLocationField.getText();
						fireEvent();
					}
				}
			});
			fLocationField.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					fLocationField.setSelection(0, fLocationField.getText().length());
				}
			});

			fLocationButton= new Button(group, SWT.PUSH);
			setButtonLayoutData(fLocationButton);
			fLocationButton.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.LocationGroup.browseButton.desc")); //$NON-NLS-1$
			fLocationButton.setEnabled(false);
			fLocationButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					final DirectoryDialog dialog= new DirectoryDialog(fLocationField.getShell());
					dialog.setMessage(JtomPlugin.getResourceString("ProjectCreationWizardPage.directory.message")); //$NON-NLS-1$
					final String directoryName = fLocationField.getText().trim();
					if (directoryName.length() > 0) {
						final File path = new File(directoryName);
						if (path.exists())
							dialog.setFilterPath(new Path(directoryName).toOSString());
					}
					final String selectedDirectory = dialog.open();
					if (selectedDirectory != null) {
						fExternalLocation= selectedDirectory;
						fLocationField.setText(fExternalLocation);
						fireEvent();
					}
				}
			});
			fWorkspaceRadio.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					final boolean checked= fWorkspaceRadio.getSelection();
					fLocationLabel.setEnabled(!checked);
					fLocationField.setEnabled(!checked);
					fLocationButton.setEnabled(!checked);
					if (checked) {
						fLocationField.setText(getDefaultPath(fNameGroup.getName()));
					} else {
						fLocationField.setText(fExternalLocation);
					}
					fireEvent();
				}
			});
		}
		
		protected void fireEvent() {
			setChanged();
			notifyObservers();
		}

		protected String getDefaultPath(String name) {
			final IPath path= Platform.getLocation().append(name);
			return path.toOSString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Observer#update(java.util.Observable,
		 *      java.lang.Object)
		 */
		public void update(Observable o, Object arg) {
			if (fWorkspaceRadio.getSelection()) {
				fLocationField.setText(getDefaultPath(fNameGroup.getName()));
			}
			fireEvent();
		}

		public IPath getLocation() {
			if (isInWorkspace()) {
				return Platform.getLocation();
			}
			return new Path(fLocationField.getText().trim());
		}

		public boolean isInWorkspace() {
			return fWorkspaceRadio.getSelection();
		}
	}

	/**
	 * Request a project layout.
	 */
	private final class LayoutGroup implements Observer {

		protected final Button fStdRadio, fSrcBinRadio;
		protected final Group fGroup;
		
		public LayoutGroup(Composite composite) {
			
			fGroup= new Group(composite, SWT.NONE);
			fGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fGroup.setLayout(initGridLayout(new GridLayout(), true));
			fGroup.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.LayoutGroup.title")); //$NON-NLS-1$
			
			fStdRadio= new Button(fGroup, SWT.RADIO);
			fStdRadio.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fStdRadio.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.LayoutGroup.option.oneFolder")); //$NON-NLS-1$
			
			fSrcBinRadio= new Button(fGroup, SWT.RADIO);
			fSrcBinRadio.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fSrcBinRadio.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.LayoutGroup.option.separateFolders")); //$NON-NLS-1$
						
			boolean useSrcBin= PreferenceConstants.getPreferenceStore().getBoolean(PreferenceConstants.SRCBIN_FOLDERS_IN_NEWPROJ);
			fSrcBinRadio.setSelection(useSrcBin);
			fStdRadio.setSelection(!useSrcBin);
			
		}

		public void update(Observable o, Object arg) {
			final boolean detect= fDetectGroup.mustDetect();
			fStdRadio.setEnabled(!detect);
			fSrcBinRadio.setEnabled(!detect);
			fGroup.setEnabled(!detect);
		}
		
		public boolean isSrcBin() {
			return fSrcBinRadio.getSelection();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	}

	/**
	 * Show a warning when the project location contains files.
	 */
	private final class DetectGroup extends Observable implements Observer {

		private final Text fText;
		private boolean fDetect;
		
		public DetectGroup(Composite composite) {
			fText= new Text(composite, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
			final GridData gd= new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
			gd.widthHint= 0;
			gd.heightHint= convertHeightInCharsToPixels(6);
			fText.setLayoutData(gd);
			fText.setFont(composite.getFont());
			fText.setText(JtomPlugin.getResourceString("ProjectCreationWizardPage.DetectGroup.message")); //$NON-NLS-1$
			fText.setVisible(false);
		}

		public void update(Observable o, Object arg) {
			if (fLocationGroup.isInWorkspace()) {
				String name= getProjectName();
				if (name.length() == 0 || JtomPlugin.getWorkspace().getRoot().findMember(name) != null) {
					fDetect= false;
				} else {
					final File directory= fLocationGroup.getLocation().append(getProjectName()).toFile();
					fDetect= directory.isDirectory();
				}
			} else {
				final File directory= fLocationGroup.getLocation().toFile();
				fDetect= directory.isDirectory();
			}
			fText.setVisible(fDetect);
			setChanged();
			notifyObservers();
		}
		
		public boolean mustDetect() {
			return fDetect;
		}
	}

	/**
	 * Validate this page and show appropriate warnings and error NewWizardMessages.
	 */
	private final class Validator implements Observer {

		public void update(Observable o, Object arg) {

			final IWorkspace workspace= JtomPlugin.getWorkspace();

			final String name= fNameGroup.getName();

			// check wether the project name field is empty
			if (name.length() == 0) { //$NON-NLS-1$
				setErrorMessage(null);
				setMessage(JtomPlugin.getResourceString("ProjectCreationWizardPage.Message.enterProjectName")); //$NON-NLS-1$
				setPageComplete(false);
				return;
			}

			// check whether the project name is valid
			final IStatus nameStatus= workspace.validateName(name, IResource.PROJECT);
			if (!nameStatus.isOK()) {
				setErrorMessage(nameStatus.getMessage());
				setPageComplete(false);
				return;
			}

			// check whether project already exists
			final IProject handle= getProjectHandle();
			if (handle.exists()) {
				setErrorMessage(JtomPlugin.getResourceString("ProjectCreationWizardPage.Message.projectAlreadyExists")); //$NON-NLS-1$
				setPageComplete(false);
				return;
			}

			final String location= fLocationGroup.getLocation().toOSString();

			// check whether location is empty
			if (location.length() == 0) {
				setErrorMessage(null);
				setMessage(JtomPlugin.getResourceString("ProjectCreationWizardPage.Message.enterLocation")); //$NON-NLS-1$
				setPageComplete(false);
				return;
			}

			// check whether the location is a syntactically correct path
			if (!Path.EMPTY.isValidPath(location)) { //$NON-NLS-1$
				setErrorMessage(JtomPlugin.getResourceString("ProjectCreationWizardPage.Message.invalidDirectory")); //$NON-NLS-1$
				setPageComplete(false);
				return;
			}

			// check whether the location has the workspace as prefix
			IPath projectPath= new Path(location);
			if (!fLocationGroup.isInWorkspace() && Platform.getLocation().isPrefixOf(projectPath)) {
				setErrorMessage(JtomPlugin.getResourceString("ProjectCreationWizardPage.Message.cannotCreateInWorkspace")); //$NON-NLS-1$
				setPageComplete(false);
				return;
			}

			// If we do not place the contents in the workspace validate the
			// location.
			if (!fLocationGroup.isInWorkspace()) {
				final IStatus locationStatus= workspace.validateProjectLocation(handle, projectPath);
				if (!locationStatus.isOK()) {
					setErrorMessage(locationStatus.getMessage());
					setPageComplete(false);
					return;
				}
			}
			
			setPageComplete(true);

			setErrorMessage(null);
			setMessage(null);
		}

	}

	protected NameGroup fNameGroup;
	protected LocationGroup fLocationGroup;
	protected LayoutGroup fLayoutGroup;
	protected DetectGroup fDetectGroup;
	protected Validator fValidator;

	
	private static final String PAGE_NAME= JtomPlugin.getResourceString("ProjectCreationWizardPage.page.pageName"); //$NON-NLS-1$

	/**
	 * Create a new <code>TomProjectCreationWizardPage</code>.
	 */
	public TomProjectCreationWizardPage() {
		super(PAGE_NAME);
		setPageComplete(false);
		setTitle(JtomPlugin.getResourceString("ProjectCreationWizardPage.page.title"));
		setDescription(JtomPlugin.getResourceString("ProjectCreationWizardPage.page.description"));
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		final Composite composite= new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(initGridLayout(new GridLayout(1, false), true));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// create UI elements
		fNameGroup= new NameGroup(composite);
		fLocationGroup= new LocationGroup(composite);
		fLayoutGroup= new LayoutGroup(composite);
		fDetectGroup= new DetectGroup(composite);
		
		// establish connections
		fNameGroup.addObserver(fLocationGroup);
		fDetectGroup.addObserver(fLayoutGroup);
		fLocationGroup.addObserver(fDetectGroup);

		// initialize all elements
		fNameGroup.notifyObservers();
		
		// create and connect validator
		fValidator= new Validator();
		fNameGroup.addObserver(fValidator);
		fLocationGroup.addObserver(fValidator);

		setControl(composite);
	}

	/**
	 * Returns the current project location path as entered by the user, or its
	 * anticipated initial value. Note that if the default has been returned
	 * the path in a project description used to create a project should not be
	 * set.
	 * 
	 * @return the project location path or its anticipated initial value.
	 */
	public IPath getLocationPath() {
		return fLocationGroup.getLocation();
	}


	/**
	 * Creates a project resource handle for the current project name field
	 * value.
	 * <p>
	 * This method does not create the project resource; this is the
	 * responsibility of <code>IProject::create</code> invoked by the new
	 * project resource wizard.
	 * </p>
	 * 
	 * @return the new project resource handle
	 */
	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(fNameGroup.getName());
	}
	
	public boolean isInWorkspace() {
		return fLocationGroup.isInWorkspace();
	}
	
	public String getProjectName() {
		return fNameGroup.getName();
	}

	public boolean getDetect() {
		return fDetectGroup.mustDetect();
	}
	
	public boolean isSrcBin() {
		return fLayoutGroup.isSrcBin();
	}
	
	
	/*
	 * see @DialogPage.setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) fNameGroup.setFocus();
	}

	
	/**
	 * Initialize a grid layout with the default Dialog settings.
	 */
	protected GridLayout initGridLayout(GridLayout layout, boolean margins) {
		layout.horizontalSpacing= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		if (margins) {
			layout.marginWidth= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.marginHeight= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		} else {
			layout.marginWidth= 0;
			layout.marginHeight= 0;
		}
		return layout;
	}
	
	/**
	 * Set the layout data for a button.
	 */
	protected GridData setButtonLayoutData(Button button) {
		return super.setButtonLayoutData(button);
	}
	
}