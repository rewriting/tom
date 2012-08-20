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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

/**
 * An extension point to allow the TOM completion words into java
 */
public final class TomAndJavaCompletionProposalComputer implements
		IJavaCompletionProposalComputer {
	/** The wrapped processor. */
	private final TomTemplateCompletionProcessor fProcessor = new TomTemplateCompletionProcessor();

	/**
	 * Default constructor to make it instantiatable via the extension
	 * mechanism.
	 */
	public TomAndJavaCompletionProposalComputer() {

	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalComputer#computeCompletionProposals(org.eclipse.jface.text.contentassist.TextContentAssistInvocationContext,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List<ICompletionProposal> computeCompletionProposals(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		return Arrays.asList(fProcessor.computeCompletionProposals(context
				.getViewer(), context.getInvocationOffset()));
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalComputer#computeContextInformation(org.eclipse.jface.text.contentassist.TextContentAssistInvocationContext,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		
			return Arrays.asList(fProcessor.computeContextInformation(context
					.getViewer(), context.getInvocationOffset()));
		
		
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalComputer#getErrorMessage()
	 */
	public String getErrorMessage() {
		return fProcessor.getErrorMessage();
	}

	/*
	 * @see org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer#sessionStarted()
	 */
	public void sessionStarted() {
	}

	/*
	 * @see org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer#sessionEnded()
	 */
	public void sessionEnded() {
	}
}
