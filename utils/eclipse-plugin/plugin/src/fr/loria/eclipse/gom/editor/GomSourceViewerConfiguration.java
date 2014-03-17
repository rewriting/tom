/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2014 Inria Nancy, France.
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
 * 
 **/

package fr.loria.eclipse.gom.editor;

import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.quickassist.IQuickAssistAssistant;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.ITextEditor;

import fr.loria.eclipse.gom.editor.util.GomEditorEnvironment;
import fr.loria.eclipse.jtom.JtomPlugin;

/** 
 * Class which set all the functions off the editor 
 * @author Martin GRANDCOLAS
 */
public class GomSourceViewerConfiguration extends TextSourceViewerConfiguration {

	private GomCodeScanner gomScanner;
	private JavaSourceViewerConfiguration javaSV;

	/**
	 * Default constructor
	 * @param jTools
	 * @param editor
	 */
	public GomSourceViewerConfiguration(JavaTextTools jTools, ITextEditor editor) {
		this.javaSV = new JavaSourceViewerConfiguration(jTools
				.getColorManager(), PreferenceConstants.getPreferenceStore(),
				editor, JtomPlugin.GOM_PARTITIONING);
		this.gomScanner = (GomCodeScanner) GomEditorEnvironment.getGomCodeScanner();
	}

	/*
	 * @see SourceViewerConfiguration#getTabWidth(ISourceViewer)
	 */
	public int getTabWidth(ISourceViewer sourceViewer) {
		return getJavaSVConfig().getTabWidth(sourceViewer);
	}

	/*
	 * @see SourceViewerConfiguration#getReconciler(ISourceViewer)
	 */
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		return javaSV.getReconciler(sourceViewer);
	}

	/**
	 * Set the partitions to use when a word is found ,  by default we use the gomCodescanner
	 */
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		DefaultDamagerRepairer dr;
		PresentationReconciler reconciler;
	
		reconciler = (PresentationReconciler) (getJavaSVConfig()
				.getPresentationReconciler(sourceViewer));
		reconciler
				.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		dr = new DefaultDamagerRepairer(gomScanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
	
		return reconciler;
	}
	
	

	/*
	 * @see SourceViewerConfiguration#getContentFormatter(ISourceViewer)
	 */
	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		return getJavaSVConfig().getContentFormatter(sourceViewer);
	}

	/*
	 * @see SourceViewerConfiguration#getContentAssistant(ISourceViewer)
	 */
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
	
		return getJavaSVConfig().getContentAssistant(sourceViewer);
	}

	/*
	 * @see SourceViewerConfiguration#getDefaultPrefixes(ISourceViewer, String)
	 * @since 2.0
	 */
	public String[] getDefaultPrefixes(ISourceViewer sourceViewer, String contentType) {
		return new String[] { "//", "" }; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * @see SourceViewerConfiguration#getDoubleClickStrategy(ISourceViewer, String)
	 */
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		return getJavaSVConfig().getDoubleClickStrategy(sourceViewer, contentType);
	}

	/*
	 * @see SourceViewerConfiguration#getIndentPrefixes(ISourceViewer, String)
	 */
	public String[] getIndentPrefixes(ISourceViewer sourceViewer,
			String contentType) {
		return getJavaSVConfig().getIndentPrefixes(sourceViewer, contentType);
	}

	/**
	 * Gives the string corresponding to the view
	 */
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return JtomPlugin.GOM_PARTITIONING;
	}

	/*
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAutoEditStrategies(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		return getJavaSVConfig().getAutoEditStrategies(sourceViewer, contentType);
	}
	
	/*
	 * @see SourceViewerConfiguration#getInformationControlCreator(ISourceViewer)
	 * @since 2.0
	 */
	public IInformationControlCreator getInformationControlCreator(
			ISourceViewer sourceViewer) {
		return getJavaSVConfig().getInformationControlCreator(sourceViewer);
	}
	
	/*
	 * @see SourceViewerConfiguration#getContentAssistant(ISourceViewer)
	 */
	public IQuickAssistAssistant getQuickAssistAssistant(ISourceViewer sourceViewer) {
		return getJavaSVConfig().getQuickAssistAssistant(sourceViewer);
	}
	
	
	/*
	 * @see SourceViewerConfiguration#getInformationPresenter(ISourceViewer)
	 * @since 2.0
	 */
	public IInformationPresenter getInformationPresenter(
			ISourceViewer sourceViewer) {
		return getJavaSVConfig().getInformationPresenter(sourceViewer);
	}


	private JavaSourceViewerConfiguration getJavaSVConfig() {
		return javaSV;
	}

	/**
	 * Small internal class used when there are big partitions  such as comments
	 * @author Martin GRANDCOLAS
	 *
	 */
	static class SingleTokenScanner extends BufferedRuleBasedScanner {
		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	}
}
