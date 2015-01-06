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

package fr.loria.eclipse.gom.editor;

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.JavaTextTools;

import fr.loria.eclipse.gom.editor.util.GomEditorEnvironment;

/**
 * The GomEditor with synthax coloration and TODO completion
 * 
 * @author Martin GRANDCOLAS
 */

public class GomEditor extends CompilationUnitEditor {

	/*
	 * (non-Javadoc) Method declared on AbstractTextEditor
	 */
	protected void initializeEditor() {
		super.initializeEditor();
		GomEditorEnvironment.connect(this); 
		setSourceViewerConfiguration(new GomSourceViewerConfiguration(
				new JavaTextTools(PreferenceConstants.getPreferenceStore()),
				this));

	}
	
	
	public void dispose() {
  		GomEditorEnvironment.disconnect(this);
  		super.dispose();
  	}

} // class GomEditor
