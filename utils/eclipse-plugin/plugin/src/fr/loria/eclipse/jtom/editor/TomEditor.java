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

package fr.loria.eclipse.jtom.editor;

import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextOperationAction;

import fr.loria.eclipse.gom.editor.util.GomEditorEnvironment;
import fr.loria.eclipse.jtom.editor.util.TomEditorEnvironment;

/**
 * @author julien
 */


public class TomEditor extends CompilationUnitEditor {

    
  	/* (non-Javadoc)
  	 * Method declared on AbstractTextEditor
  	 */
  	protected void initializeEditor() {
  			super.initializeEditor();
  			TomEditorEnvironment.connect(this);
  			GomEditorEnvironment.connect(this); 
  			setEditorContextMenuId("#TomEditorContect");
  			setRulerContextMenuId("#TomRulerContext");
 			JavaTextTools jTextTools= new JavaTextTools(PreferenceConstants.getPreferenceStore());
 			setSourceViewerConfiguration(new TomSourceViewerConfiguration(jTextTools, this));
  		}
  	
  	/** The TomEditor implementation of this AbstractTextEditormethod extend the 
  	 * actions to add those specific to the receiver
  	 */
  	protected void createActions() {
  		super.createActions();
  		ResourceBundle bundle = TomEditorMessages.getResourceBundle();
  		
  		IAction a= new TextOperationAction(bundle, "TomContentAssistProposal.", this, ISourceViewer.CONTENTASSIST_PROPOSALS); //$NON-NLS-1$
  		a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
  		setAction("TomContentAssistProposal", a); //$NON-NLS-1$
  		
  		a= new TextOperationAction(bundle, "TomContentAssistTip.", this, ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION);  //$NON-NLS-1$
  		a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION);
  		setAction("TomContentAssistTip", a); //$NON-NLS-1$
  	}
  	
  	protected void doSetInput(IEditorInput input) throws CoreException {		
  	    super.doSetInput(input);
  	    
  			((TomSourceViewerConfiguration) getSourceViewerConfiguration()).doSetInput(input);
  	}
  	
  	public void dispose() {
  		TomEditorEnvironment.disconnect(this);
  		GomEditorEnvironment.disconnect(this);
  		super.dispose();
  	}
  	
  	public boolean isSaveAsAllowed() {
  	   return false;
  	}
  	
  	/** The <code>TomEditor</code> implementation of this 
  	 * <code>AbstractTextEditor</code> method adds any 
  	 * TomEditor specific entries.
  	 */ 
  	public void editorContextMenuAboutToShow(MenuManager menu) {
  		super.editorContextMenuAboutToShow(menu);
  		addAction(menu, "TomContentAssistProposal");
  		addAction(menu, "TomContentAssistTip");
  	}
} // class TomEditor
