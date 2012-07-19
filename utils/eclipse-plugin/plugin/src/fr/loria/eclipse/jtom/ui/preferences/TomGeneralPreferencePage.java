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

package fr.loria.eclipse.jtom.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * @author julien
 */
public class TomGeneralPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	
	/**
	 * Constructor
	 */
	public TomGeneralPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		  // Set the preference store for the preference page.
		IPreferenceStore store =
			JtomPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		// Initialize all field editors.
		StringFieldEditor fileExtension = new StringFieldEditor(
				JtomPlugin.TOM_EXTENSION_PREFERENCE,
				"&File extension",
				getFieldEditorParent());
		addField(fileExtension);
		
		SpacerFieldEditor spacer1 = new SpacerFieldEditor(
				getFieldEditorParent());
		addField(spacer1);
		
		StringFieldEditor defaultCommand = new StringFieldEditor(
				JtomPlugin.TOM_DEFAULT_COMMAND_PREFERENCE,
				"&Default command line",
				getFieldEditorParent());
		addField(defaultCommand);
		
		SpacerFieldEditor spacer2 = new SpacerFieldEditor(
				getFieldEditorParent());
		addField(spacer2);
		
		PathEditor listInclude = new PathEditor(
				JtomPlugin.TOM_INCLUDE_PREFERENCE,
				"&Include file path location",
				"Choose",
				getFieldEditorParent());
		addField(listInclude);
		
		SpacerFieldEditor spacer3 = new SpacerFieldEditor(
				getFieldEditorParent());
		addField(spacer3);

		BooleanFieldEditor displayJavaErrors = new BooleanFieldEditor(
				JtomPlugin.DISPLAY_JAVA_ERRORS_PREFERENCE, 
				"&Display Java Errors", 
				getFieldEditorParent());
		addField(displayJavaErrors);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}
	
} //class TomGeneralPreferencePage
