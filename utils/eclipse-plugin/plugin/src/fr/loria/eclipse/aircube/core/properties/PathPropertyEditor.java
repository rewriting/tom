/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2015 Inria Nancy, France.
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

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.core.runtime.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author julien
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PathPropertyEditor {
	/**
	 * The list widget; <code>null</code> if none
	 * (before creation or after disposal).
	 */
	private List list;

	/**
	 * The button box containing the Add, Remove, Up, and Down buttons;
	 * <code>null</code> if none (before creation or after disposal).
	 */
	private Composite buttonBox;

	/**
	 * The Add button.
	 */
	private Button addButton;

	/**
	 * The Remove button.
	 */
	private Button removeButton;

	/**
	 * The Up button.
	 */
	private Button upButton;

	/**
	 * The Down button.
	 */
	private Button downButton;

	/**
	 * The selection listener.
	 */
	private SelectionListener selectionListener;
	
	/**
	 * The last path, or <code>null</code> if none.
	 */
	private String lastPath;

	/**
	 * The special label text for directory chooser, 
	 * or <code>null</code> if none.
	 */
	private String dirChooserLabelText;
	
	/**
	 * The label control.
	 */
	private Label label;
	
	/**
	 * The label's text.
	 */
	private String labelText;
	
	
	
	public PathPropertyEditor(String labelText, String dirChooserLabelText, Composite parent) {
		this.dirChooserLabelText = dirChooserLabelText;
		this.labelText = labelText;
		createControl(parent);
	}
	
	private void createControl(Composite parent) {
		/*GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 8;
		parent.setLayout(layout);*/
		doFillIntoGrid(parent, 2);//layout.numColumns);
	}
	
	private void doFillIntoGrid(Composite parent, int numColumns) {
		Control control = getLabelControl(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = numColumns;
		control.setLayoutData(gd);

		list = getListControl(parent);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.FILL;
		gd.horizontalSpan = numColumns - 1;
		gd.grabExcessHorizontalSpace = true;
		list.setLayoutData(gd);

		buttonBox = getButtonBoxControl(parent);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		buttonBox.setLayoutData(gd);
	}
	
	private Label getLabelControl(Composite parent) {
		if (label == null) {
			label = new Label(parent, SWT.LEFT);
			label.setFont(parent.getFont());
			String text = getLabelText();
			if (text != null)
				label.setText(text);
			label.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					label = null;
				}
			});
		} else {
			checkParent(label, parent);
		}
		return label;
	}
	
	/**
	 * Returns this field editor's label text.
	 *
	 * @return the label text
	 */
	private String getLabelText() {
		return labelText;
	}
	
	/**
	 * Returns this field editor's list control.
	 *
	 * @param parent the parent control
	 * @return the list control
	 */
	public List getListControl(Composite parent) {
		if (list == null) {
			list = new List(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
			list.setFont(parent.getFont());
			list.addSelectionListener(getSelectionListener());
			list.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					list = null;
				}
			});
		} else {
			checkParent(list, parent);
		}
		return list;
	}
	
	private SelectionListener getSelectionListener() {
		if (selectionListener == null)
			createSelectionListener();
		return selectionListener;
	}
	/**
	 * Creates a selection listener.
	 */
	private void createSelectionListener() {
		selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Widget widget = event.widget;
				if (widget == addButton) {
					addPressed();
				} else
					if (widget == removeButton) {
						removePressed();
					} else
						if (widget == upButton) {
							upPressed();
						} else
							if (widget == downButton) {
								downPressed();
							} else
								if (widget == list) {
									selectionChanged();
								}
			}
		};
	}
	/**
	 * Notifies that the Add button has been pressed.
	 */
	private void addPressed() {
		String input = getNewInputObject();

		if (input != null) {
			int index = list.getSelectionIndex();
			if (index >= 0) {
				list.add(input, index + 1);
			} else {
				list.add(input, 0);
			}
			selectionChanged();
		}
	}
	
	/**
	 * Notifies that the Remove button has been pressed.
	 */
	private void removePressed() {
		int index = list.getSelectionIndex();
		if (index >= 0) {
			list.remove(index);
			selectionChanged();
		}
	}
	
	/**
	 * Moves the currently selected item up or down.
	 *
	 * @param up <code>true</code> if the item should move up,
	 *  and <code>false</code> if it should move down
	 */
	private void swap(boolean up) {
		
		int index = list.getSelectionIndex();
		int target = up ? index - 1 : index + 1;

		if (index >= 0) {
			String[] selection = list.getSelection();
			Assert.isTrue(selection.length == 1);
			list.remove(index);
			list.add(selection[0], target);
			list.setSelection(target);
		}
		selectionChanged();
	}
	/**
	 * Notifies that the Up button has been pressed.
	 */
	private void upPressed() {
		swap(true);
	}
	
	/**
	 * Notifies that the Down button has been pressed.
	 */
	private void downPressed() {
		swap(false);
	}
	
	/**
	 * Notifies that the list selection has changed.
	 */
	private void selectionChanged() {

		int index = list.getSelectionIndex();
		int size = list.getItemCount();

		removeButton.setEnabled(index >= 0);
		upButton.setEnabled(size > 1 && index > 0);
		downButton.setEnabled(size > 1 && index >= 0 && index < size - 1);
		list.setFocus();
	}
	

	
	/* (non-Javadoc)
	 * Method declared on ListEditor.
	 * Creates a new path element by means of a directory dialog.
	 */
	private String getNewInputObject() {

		DirectoryDialog dialog = new DirectoryDialog(getShell());
		if (dirChooserLabelText != null)
			dialog.setMessage(dirChooserLabelText);
		if (lastPath != null) {
			if (new File(lastPath).exists())
				dialog.setFilterPath(lastPath);
		}
		String dir = dialog.open();
		if (dir != null) {
			dir = dir.trim();
			if (dir.length() == 0)
				return null;
			lastPath = dir;
		}
		return dir;
	}
	
	/**
	 * Returns this field editor's shell.
	 * <p>
	 * This method is internal to the framework; subclassers should not call
	 * this method.
	 * </p>
	 *
	 * @return the shell
	 */
	private Shell getShell() {
		if (addButton == null)
			return null;
		return addButton.getShell();
	}
	
	/**
	 * Checks if the given parent is the current parent of the
	 * supplied control; throws an (unchecked) exception if they
	 * are not correctly related.
	 *
	 * @param control the control
	 * @param parent the parent control
	 */
	private void checkParent(Control control, Composite parent) {
		Assert.isTrue(control.getParent() == parent, "Different parents");//$NON-NLS-1$
	}
	
	/**
	 * Returns this field editor's button box containing the Add, Remove,
	 * Up, and Down button.
	 *
	 * @param parent the parent control
	 * @return the button box
	 */
	private Composite getButtonBoxControl(Composite parent) {
		if (buttonBox == null) {
			buttonBox = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout();
			layout.marginWidth = 0;
			buttonBox.setLayout(layout);
			createButtons(buttonBox);
			buttonBox.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					addButton = null;
					removeButton = null;
					upButton = null;
					downButton = null;
					buttonBox = null;
				}
			});

		} else {
			checkParent(buttonBox, parent);
		}

		selectionChanged();
		return buttonBox;
	}
	
	/**
	 * Creates the Add, Remove, Up, and Down button in the given button box.
	 *
	 * @param buttonBox the box for the buttons
	 */
	private void createButtons(Composite buttonBox) {
		addButton = createPushButton(buttonBox, "ListEditor.add");//$NON-NLS-1$
		removeButton = createPushButton(buttonBox, "ListEditor.remove");//$NON-NLS-1$
		upButton = createPushButton(buttonBox, "ListEditor.up");//$NON-NLS-1$
		downButton = createPushButton(buttonBox, "ListEditor.down");//$NON-NLS-1$
	}
	
	/**
	 * Helper method to create a push button.
	 * 
	 * @param parent the parent control
	 * @param key the resource name used to supply the button's label text
	 */
	private Button createPushButton(Composite parent, String key) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(JFaceResources.getString(key));
		button.setFont(parent.getFont());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = convertVerticalDLUsToPixels(button, IDialogConstants.BUTTON_HEIGHT);
		int widthHint = convertHorizontalDLUsToPixels(button, IDialogConstants.BUTTON_WIDTH);
		data.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		button.setLayoutData(data);
		button.addSelectionListener(getSelectionListener());
		return button;
	}
	
	/**
	 * Returns the number of pixels corresponding to the
	 * given number of horizontal dialog units.
	 * <p>
	 * Clients may call this framework method, but should not override it.
	 * </p>
	 *
	 * @param control the control being sized
	 * @param dlus the number of horizontal dialog units
	 * @return the number of pixels
	 */
	private int convertHorizontalDLUsToPixels(Control control, int dlus) {
		GC gc= new GC(control);
		gc.setFont(control.getFont());
		int averageWidth= gc.getFontMetrics().getAverageCharWidth();
		gc.dispose();

		double horizontalDialogUnitSize = averageWidth * 0.25;

		return (int)Math.round(dlus * horizontalDialogUnitSize);
	}
	/**
	 * Returns the number of pixels corresponding to the
	 * given number of vertical dialog units.
	 * <p>
	 * Clients may call this framework method, but should not override it.
	 * </p>
	 *
	 * @param control the control being sized
	 * @param dlus the number of vertical dialog units
	 * @return the number of pixels
	 */
	private int convertVerticalDLUsToPixels(Control control, int dlus) {
		GC gc= new GC(control);
		gc.setFont(control.getFont());
		int height = gc.getFontMetrics().getHeight();
		gc.dispose();

		double verticalDialogUnitSize = height * 0.125;
		
		return (int)Math.round(dlus * verticalDialogUnitSize);
	}
	
	/* (non-Javadoc)
	 * Method declared on ListEditor.
	 * Creates a single string from the given array by separating each
	 * string with the appropriate OS-specific path separator.
	 */
	private String createList(String[] items) {
		StringBuffer path = new StringBuffer("");//$NON-NLS-1$

		for (int i = 0; i < items.length; i++) {
			path.append(items[i]);
			path.append(File.pathSeparator);
		}
		return path.toString();
	}
	
	private String[] parseString(String stringList) {
		StringTokenizer st = new StringTokenizer(stringList, File.pathSeparator + "\n\r");//$NON-NLS-1$
		ArrayList<Object> v = new ArrayList<Object>();
		while (st.hasMoreElements()) {
			v.add(st.nextElement());
		}
		return (String[])v.toArray(new String[v.size()]);
	}

	
	public String[] getArrayValue() {
		 return list.getItems();
	}
	
	public String getStringValue() {
		 return createList(list.getItems());
	}
	
	public void doLoad(String s) {
		if (list != null) {
			String[] array = parseString(s);
			for (int i = 0; i < array.length; i++){
				list.add(array[i]);
			}
		}
	}
	
} //class PathPropertyEditor