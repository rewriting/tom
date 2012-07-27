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

package fr.loria.eclipse.jtom.editor.completion;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.swt.graphics.Image;

import fr.loria.eclipse.jtom.JtomPlugin;
import fr.loria.eclipse.jtom.editor.template.TomContextType;

/**
 * A completion processor for Tom templates.
 */
public class TomTemplateCompletionProcessor extends TemplateCompletionProcessor {
	private static final String DEFAULT_IMAGE = "icons/BigTom.gif"; //$NON-NLS-1$
	private static final ICompletionProposal[] NO_PROPOSALS = new ICompletionProposal[0];
	private static final IContextInformation[] NO_CONTEXT = new IContextInformation[0]; 
 
	/**
	 * We watch for angular brackets since those are often part of XML
	 * templates.
	 */
	protected String extractPrefix(ITextViewer viewer, int offset) {
		IDocument document = viewer.getDocument();
		int i = offset;
		if (i > document.getLength())
			return ""; //$NON-NLS-1$

		try {
			while (i > 0) {
				char ch = document.getChar(i - 1);
				if (ch != '%' && !Character.isJavaIdentifierPart(ch))
					break;
				i--;
			}
			return document.get(i, offset - i);
		} catch (BadLocationException e) {
			return ""; //$NON-NLS-1$
		}
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		
		String prefix = extractPrefix(viewer, offset);
		if (prefix == null || prefix.length() == 0) {
			return NO_PROPOSALS;
		}

		return getSuggestions(viewer, offset, prefix);
	}
	
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset){
		
		return NO_CONTEXT;
	}
	
/**
 * Suggests simply the words that match with the given prefix
 * @param viewer
 * @param offset
 * @param prefix
 * @return
 */
	private ICompletionProposal[] getSuggestions(ITextViewer viewer,
			int offset, String prefix) {

		Region region = new Region(offset - prefix.length(), prefix.length());
		TemplateContext context = createContext(viewer, region);
		if (context == null)
			return new ICompletionProposal[0];

		Template[] templates = getTemplates(context.getContextType().getId());

		List<ICompletionProposal> matches = new ArrayList<ICompletionProposal>();
		for (int i = 0; i < templates.length; i++) {
			Template template = templates[i];
			try {
				context.getContextType().validate(template.getPattern());
			} catch (TemplateException e) {
				continue;
			}
			if (prefix == "" || template.getName().startsWith(prefix))
				matches.add(createProposal(template, context, (IRegion) region,
						getRelevance(template, prefix)));
		}

		return (ICompletionProposal[]) matches
				.toArray(new ICompletionProposal[matches.size()]);
	}

	/**
	 * Simply return all templates.
	 */
	protected Template[] getTemplates(String contextTypeId) {

		return JtomPlugin.getDefault().getTemplateStore().getTemplates();
	}

	/**
	 * Return the XML context type that is supported by this plugin.
	 */
	protected TemplateContextType getContextType(ITextViewer viewer,
			IRegion region) {
		return JtomPlugin.getDefault().getContextTypeRegistry().getContextType(
				TomContextType.TOM_CONTEXT_TYPE);
	}

	/**
	 * Always return the default image.
	 */
	protected Image getImage(Template template) {
		ImageRegistry registry = JtomPlugin.getDefault().getImageRegistry();
		Image image = registry.get(DEFAULT_IMAGE);
		if (image == null) {
			ImageDescriptor desc = JtomPlugin.imageDescriptorFromPlugin(
					"fr.loria.eclipse.tom", DEFAULT_IMAGE); //$NON-NLS-1$
			registry.put(DEFAULT_IMAGE, desc);
			image = registry.get(DEFAULT_IMAGE);
		}
		return image;
	}

}
