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

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.internal.ui.text.java.JavaCompletionProcessor;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.text.IAutoIndentStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.ITextEditor;

import fr.loria.eclipse.gom.editor.GomCodeScanner;
import fr.loria.eclipse.gom.editor.util.GomEditorEnvironment;
import fr.loria.eclipse.jtom.JtomPlugin;
import fr.loria.eclipse.jtom.editor.util.TomAnnotationHover;
import fr.loria.eclipse.jtom.editor.util.TomCodeScanner;
import fr.loria.eclipse.jtom.editor.util.TomColorManager;
import fr.loria.eclipse.jtom.editor.util.TomCompletionProcessor;
import fr.loria.eclipse.jtom.editor.util.TomEditorEnvironment;
import fr.loria.eclipse.jtom.editor.util.TomTextHover;

public class TomSourceViewerConfiguration extends TextSourceViewerConfiguration
		implements ITomPartitions {

	/**
	 * Single token scanner used only in the debbug mode.
	 */
	static class SingleTokenScanner extends BufferedRuleBasedScanner {
		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	};

	private TomTextHover tomSortTextHover, tomOperatorTextHover;

 	private JavaSourceViewerConfiguration javaSVConfig;

	private JavaTextTools jTextTools;

	private ITextEditor textEditor;

	private TomCodeScanner tomCodeScanner;
	private GomCodeScanner gomCodeScanner;


	/*public TomSourceViewerConfiguration(JavaTextTools jTools, ITextEditor editor, String fileName) {
	 this.javaSVConfig = new JavaSourceViewerConfiguration(jTools.getColorManager(), JtomPlugin.getDefault().getPreferenceStore(), editor, JtomPlugin.TOM_PARTITIONING);
	 this.jTextTools = jTools;
	 this.textEditor = editor;
	 this.tomSortTextHover = new TomTextHover(fileName, TomTextHover.SORT_HOVER);
	 this.tomOperatorTextHover = new TomTextHover(fileName, TomTextHover.SIGNATURE_HOVER);
	 tomCodeScanner = new TomCodeScanner(TomEditorEnvironment.getTomColorManager(), fileName);
	 }*/

	public TomSourceViewerConfiguration(JavaTextTools jTools, ITextEditor editor) {
		
		this.javaSVConfig = new JavaSourceViewerConfiguration(
				jTools.getColorManager(),
				PreferenceConstants.getPreferenceStore()/*JtomPlugin.getDefault().getPreferenceStore()*/,
				editor, JtomPlugin.TOM_PARTITIONING);
		this.jTextTools = jTools;
		this.textEditor = editor;
	}

	public void doSetInput(IEditorInput input) {
		//    configure the source viewer with a specific .filename.tfix.parsed.table
		String associatedFile = null;
		IFile file = (input instanceof IFileEditorInput) ? ((IFileEditorInput) input)
				.getFile()
				: null;

		if (file != null) {
			String fileName = "."
					+ file.getLocation().removeFileExtension()
							.addFileExtension("tfix.parsed.table")
							.lastSegment();
			associatedFile = file.getParent().getLocation().append(fileName)
					.toString();
		} else {
			System.out.println(input.getName()
					+ "associated file not found in workspace");
		}
		this.tomSortTextHover = new TomTextHover(associatedFile,
				TomTextHover.SORT_HOVER);
		this.tomOperatorTextHover = new TomTextHover(associatedFile,
				TomTextHover.SIGNATURE_HOVER);
		this.tomCodeScanner = new TomCodeScanner(TomEditorEnvironment
				.getTomColorManager(), associatedFile);
		this.gomCodeScanner = (GomCodeScanner) GomEditorEnvironment.getGomCodeScanner();
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
		return getJavaSVConfig().getReconciler(sourceViewer);
	}

	/*
	 * @see SourceViewerConfiguration#getPresentationReconciler(ISourceViewer)
	 */
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler;
		DefaultDamagerRepairer dr;
		if (JtomPlugin.getDefault().isDebugging()) {
			// Debug mode
			reconciler = new PresentationReconciler();
			reconciler
					.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
			TomColorManager colorProvider = TomEditorEnvironment
					.getTomColorManager();

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.RED_COLOR))));
			reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
			reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.BLUE_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.JAVA_DOC);
			reconciler.setRepairer(dr, ITomPartitions.JAVA_DOC);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.GREEN_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.JAVA_MULTI_LINE_COMMENT);
			reconciler.setRepairer(dr, ITomPartitions.JAVA_MULTI_LINE_COMMENT);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.REDGREEN_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.JAVA_SINGLE_LINE_COMMENT);
			reconciler.setRepairer(dr, ITomPartitions.JAVA_SINGLE_LINE_COMMENT);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.REDBLUE_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.JAVA_STRING);
			reconciler.setRepairer(dr, ITomPartitions.JAVA_STRING);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.REDBLUE_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.JAVA_CHARACTER);
			reconciler.setRepairer(dr, ITomPartitions.JAVA_CHARACTER);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.OTHER_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.TOM_HEADER_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_HEADER_PART);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.OTHER_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.TOM_CLOSURE_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_CLOSURE_PART);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.GREEN_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.TOM_CODE_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_CODE_PART);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.GREENBLUE_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.TOM_JAVA_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_JAVA_PART);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.OTHER_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.TOM_BACKQUOTE_JAVA_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_BACKQUOTE_JAVA_PART);

			dr = new DefaultDamagerRepairer(new SingleTokenScanner(
					new TextAttribute(colorProvider
							.getColor(TomColorManager.OTHER_COLOR))));
			reconciler.setDamager(dr, ITomPartitions.TOM_BACKQUOTE_TOM_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_BACKQUOTE_TOM_PART);
		} else {
			reconciler = (PresentationReconciler) (getJavaSVConfig()
					.getPresentationReconciler(sourceViewer));
			reconciler
					.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
			dr = new DefaultDamagerRepairer(tomCodeScanner/*TomEditorEnvironment.getTomCodeScanner()*/);
			reconciler.setDamager(dr, ITomPartitions.TOM_CODE_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_CODE_PART);

			dr = new DefaultDamagerRepairer(tomCodeScanner/*TomEditorEnvironment.getTomCodeScanner()*/);
			reconciler.setDamager(dr, ITomPartitions.TOM_HEADER_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_HEADER_PART);

			dr = new DefaultDamagerRepairer(tomCodeScanner/*TomEditorEnvironment.getTomCodeScanner()*/);
			reconciler.setDamager(dr, ITomPartitions.TOM_BACKQUOTE_JAVA_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_BACKQUOTE_JAVA_PART);

			dr = new DefaultDamagerRepairer(tomCodeScanner/*TomEditorEnvironment.getTomCodeScanner()*/);
			reconciler.setDamager(dr, ITomPartitions.TOM_BACKQUOTE_TOM_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_BACKQUOTE_TOM_PART);
			
			dr = new DefaultDamagerRepairer(gomCodeScanner);
			reconciler.setDamager(dr, ITomPartitions.GOM_PART);
			reconciler.setRepairer(dr,ITomPartitions.GOM_PART);
			
			
			dr = new DefaultDamagerRepairer(getJTextTools().getCodeScanner());
			reconciler.setDamager(dr, ITomPartitions.TOM_JAVA_PART);
			reconciler.setRepairer(dr, ITomPartitions.TOM_JAVA_PART);
		}
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
		if (getTextEditor() != null) {
			
			ContentAssistant assistant = (ContentAssistant) (getJavaSVConfig()
					.getContentAssistant(sourceViewer));

//			IContentAssistProcessor processor = new JavaCompletionProcessor(textEditor, assistant, "");
// 				new TomAndJavaCompletionProcessor(
//					new TomTemplateCompletionProcessor(), getTextEditor());
//			assistant.setContentAssistProcessor(processor,
//					IDocument.DEFAULT_CONTENT_TYPE);
			

//			IContentAssistProcessor	processor = new TomAndJavaCompletionProcessor(new TomTemplateCompletionProcessor(), getTextEditor());;  
//			assistant.setContentAssistProcessor(processor,); 
//			
			JavaCompletionProcessor javaproc=(JavaCompletionProcessor)assistant.getContentAssistProcessor(IDocument.DEFAULT_CONTENT_TYPE);
			javaproc.restrictProposalsToMatchingCases(true); 
			char[] c={'.','%'};
			javaproc.setCompletionProposalAutoActivationCharacters(c); 
			
			IContentAssistProcessor	processor =new TomCompletionProcessor();

			assistant.setContentAssistProcessor(processor, ITomPartitions.TOM_CODE_PART);
			assistant.setContentAssistProcessor(processor, ITomPartitions.TOM_HEADER_PART);
			assistant.setContentAssistProcessor(processor, ITomPartitions.TOM_BACKQUOTE_JAVA_PART);
			assistant.setContentAssistProcessor(processor, ITomPartitions.TOM_JAVA_PART);
			assistant.setContentAssistProcessor(processor, ITomPartitions.TOM_BACKQUOTE_TOM_PART);

			assistant
					.setContextInformationPopupOrientation(ContentAssistant.CONTEXT_INFO_ABOVE);
			assistant
					.setInformationControlCreator(getInformationControlCreator(sourceViewer));

			return assistant;
		}
		return null;
	}

	/*
	 * @see SourceViewerConfiguration#getAutoIndentStrategy(ISourceViewer, String)
	 */
//	public IAutoIndentStrategy getAutoIndentStrategy(
//			ISourceViewer sourceViewer, String contentType) {
//		return getJavaSVConfig().getAutoIndentStrategy(sourceViewer,
//				contentType);
//	}

	/*
	 * @see SourceViewerConfiguration#getDefaultPrefixes(ISourceViewer, String)
	 * @since 2.0
	 */
	public String[] getDefaultPrefixes(ISourceViewer sourceViewer,
			String contentType) {
		return getJavaSVConfig().getDefaultPrefixes(sourceViewer, contentType);
	}
	
	/*
	 * @see SourceViewerConfiguration#getDoubleClickStrategy(ISourceViewer, String)
	 */
	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		return getJavaSVConfig().getDoubleClickStrategy(sourceViewer,
				contentType);
	}

	/*
	 * @see SourceViewerConfiguration#getIndentPrefixes(ISourceViewer, String)
	 */
	public String[] getIndentPrefixes(ISourceViewer sourceViewer,
			String contentType) {
		return getJavaSVConfig().getIndentPrefixes(sourceViewer, contentType);
	}

	/*
	 * @see SourceViewerConfiguration#getAnnotationHover(ISourceViewer)
	 */
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new TomAnnotationHover();
	}

	/*
	 * @see SourceViewerConfiguration#getOverviewRulerAnnotationHover(ISourceViewer)
	 * @since 3.0
	 */
	public IAnnotationHover getOverviewRulerAnnotationHover(
			ISourceViewer sourceViewer) {
		return new TomAnnotationHover();
	}

	/*
	 * @see SourceViewerConfiguration#getConfiguredTextHoverStateMasks(ISourceViewer, String)
	 * @since 2.1
	 
	 public int[] getConfiguredTextHoverStateMasks(ISourceViewer sourceViewer, String contentType) {
	 return getJavaSVConfig().getConfiguredTextHoverStateMasks( sourceViewer, contentType);
	 }*/

	/* (non-Javadoc)
	 * Method declared on SourceViewerConfiguration
	 */
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		if (contentType.equals(ITomPartitions.TOM_HEADER_PART)) {
			return tomSortTextHover;
		} else if (contentType.equals(ITomPartitions.TOM_CODE_PART)
				|| contentType.equals(ITomPartitions.TOM_BACKQUOTE_JAVA_PART)
				|| contentType.equals(ITomPartitions.TOM_BACKQUOTE_TOM_PART)) {
			return tomOperatorTextHover;
		} else {
			return javaSVConfig.getTextHover(sourceViewer, contentType);
				
		}
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
	 * @see SourceViewerConfiguration#getInformationPresenter(ISourceViewer)
	 * @since 2.0
	 */
	public IInformationPresenter getInformationPresenter(
			ISourceViewer sourceViewer) {
		return getJavaSVConfig().getInformationPresenter(sourceViewer);
	}

	/*
	 * @see SourceViewerConfiguration#getConfiguredContentTypes(ISourceViewer)
	 */
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return TomEditorPartitionScanner.PARTITION_TYPES;
	}

	/*
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredDocumentPartitioning(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return JtomPlugin.TOM_PARTITIONING;
	}

	private JavaSourceViewerConfiguration getJavaSVConfig() {
		return javaSVConfig;
	}

	private JavaTextTools getJTextTools() {
		return jTextTools;
	}

	private ITextEditor getTextEditor() {
		return textEditor;
	}

} //class TomSourceViewerConfiguration