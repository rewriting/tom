/*
 * Created on Aug 13, 2003
 */
package fr.loria.eclipse.jtom;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;


/**
 * @author julien
 *
 */
public class TomFilePropertyPage extends PropertyPage {

	private Text commandLineField;
	private Button commandLineChangeButton;
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		IResource resource = (IResource) getElement();
		String tomExt = JtomPlugin.getDefault().getPreferenceStore().getString("FileExtension");
		
		if (resource.getType() == IResource.FILE) {
			if ( ((IFile)resource).getFileExtension().equals(tomExt)) {
				Composite composite_extField = createComposite(parent, 2);
				createLabel(composite_extField, "Command Line arguments");
				commandLineField = createTextField(composite_extField);
				try {
					QualifiedName qName = new QualifiedName("TOMCOMMAND", resource.getLocation().toString());
					String command = resource.getPersistentProperty(qName);
					if (command == null) {
						commandLineField.setText("");
					} else {
						commandLineField.setText(command);
					}
				} catch (CoreException e) {
					//TODO
				}
				commandLineChangeButton = createPushButton(composite_extField, "Set");
			} else {
				noDefaultAndApplyButton();
				Label label = new Label(parent, SWT.LEFT);
				label.setText("Not a TOM file: Use the extension `."+tomExt+"`");
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				label.setLayoutData(data);
				return label;
			}
		}
		return null;
	}

	/**
		 * Utility method that creates a label instance
		 * and sets the default layout data.
		 *
		 * @param parent  the parent for the new label
		 * @param text  the text for the new label
		 * @return the new label
		 */
		private Label createLabel(Composite parent, String text) {
			Label label = new Label(parent, SWT.LEFT);
			label.setText(text);
			GridData data = new GridData();
			data.horizontalSpan = 2;
			data.horizontalAlignment = GridData.FILL;
			label.setLayoutData(data);
			return label;
		}
		/**
		 * Utility method that creates a push button instance
		 * and sets the default layout data.
		 *
		 * @param parent  the parent for the new button
		 * @param label  the label for the new button
		 * @return the newly-created button
		 */
		private Button createPushButton(Composite parent, String label) {
			Button button = new Button(parent, SWT.PUSH);
			button.setText(label);
			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			button.setLayoutData(data);
			return button;
		}
		
	/**
	 * Creates composite control and sets the default layout data.
	 *
	 * @param parent  the parent of the new composite
	 * @param numColumns  the number of columns for the new composite
	 * @return the newly-created coposite
	 */
	private Composite createComposite(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NULL);
		//GridLayout
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		composite.setLayout(layout);
		//GridData
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);
		return composite;
	}
	/**
	 * Create a text field specific for this application
	 *
	 * @param parent  the parent of the new text field
	 * @return the new text field
	 */
	private Text createTextField(Composite parent) {
		Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.verticalAlignment = GridData.CENTER;
		data.grabExcessVerticalSpace = false;
		text.setLayoutData(data);
		return text;
	}

	protected void performApply() {
		IResource resource = (IResource) getElement();
		try {
			QualifiedName qName = new QualifiedName("TOMCOMMAND", resource.getLocation().toString());
			resource.setPersistentProperty(qName, commandLineField.getText());
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean performCancel() {
		return false;
	}
	
} // Class TomFilePropertyPage
