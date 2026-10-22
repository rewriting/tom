/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2004 Inria
	   			       Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
	  Julien Guyon			e-mail: Julien.Guyon@loria.fr
	
*/

package fr.loria.eclipse.jtom.editor.util;

import java.text.MessageFormat;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationPresenter;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import fr.loria.eclipse.jtom.editor.TomEditorMessages;

public class TomCompletionProcessor implements IContentAssistProcessor {

	protected static class Validator implements IContextInformationValidator, IContextInformationPresenter {

		protected int fInstallOffset;

		/*
		 * @see IContextInformationValidator#isContextInformationValid(int)
		 */
		public boolean isContextInformationValid(int offset) {
			return Math.abs(fInstallOffset - offset) < 5;
		}

		/*
		 * @see IContextInformationValidator#install(IContextInformation, ITextViewer, int)
		 */
		public void install(IContextInformation info, ITextViewer viewer, int offset) {
			fInstallOffset= offset;
		}
		
		/*
		 * @see org.eclipse.jface.text.contentassist.IContextInformationPresenter#updatePresentation(int, TextPresentation)
		 */
		public boolean updatePresentation(int documentPosition, TextPresentation presentation) {
			return false;
		}
	};

	protected final static String[] fgProposals=
	{"implement { }", "get_fun_sym(t) {}", "cmp_fun_sym(s1,s2) { }", "get_fun_sym(t) { }", "cmp_fun_sym(s1,s2) { }", 
		"cmp_fun_sym(s1,s2) { }", "get_subterm(t,n) {}", "equals(t1,t2) { }", 
		"get_head(l) { }", "get_tail(l) { }","is_empty(l) { }",
		"fsym {}", "is_fsym(t) { }", "get_slot(slotname,t) { }", "make(t0,..., tn) { }"
	};
	protected final static String[] fgProposalsText=
		{"implement { /*TODO*/}", "get_fun_sym(t) { /*TODO*/}", "cmp_fun_sym(s1,s2) { /*TODO*/}", "get_fun_sym(t) { }", "cmp_fun_sym(s1,s2) { /*TODO*/}", 
			"cmp_fun_sym(s1,s2) { /*TODO*/}", "get_subterm(t,n) { /*TODO*/}", "equals(t1,t2) { /*TODO*/}",
			"get_head(l) { /*TODO*/}", "get_tail(l) { /*TODO*/}","is_empty(l) { /*TODO*/}",
			"fsym {}", "is_fsym(t) { /*TODO*/}", "get_slot(?slotname?,t) { /*TODO*/}", "make(/*TODO*/) { /*TODO*/}"
		};  

	protected IContextInformationValidator fValidator= new Validator();

	/* (non-Javadoc)
	 * Method declared on IContentAssistProcessor
	 */
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
		ICompletionProposal[] result= new ICompletionProposal[fgProposals.length];
		for (int i= 0; i < fgProposals.length; i++) {
			IContextInformation info= new ContextInformation(fgProposalsText[i], MessageFormat.format(TomEditorMessages.getResourceString("CompletionProcessor.Proposal.hovertInfo.pattern"), new Object[] { fgProposalsText[i] })); //$NON-NLS-1$
			result[i]= new CompletionProposal(fgProposalsText[i], documentOffset, 0, fgProposals[i].length(), null, fgProposals[i], info, MessageFormat.format(TomEditorMessages.getResourceString("CompletionProcessor.Proposal.hoverinfo.pattern"), new Object[] { fgProposalsText[i]})); //$NON-NLS-1$
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * Method declared on IContentAssistProcessor
	 */
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
		System.out.println("computeContextInformation");
		IContextInformation[] result= new IContextInformation[5];
		for (int i= 0; i < result.length; i++)
			result[i]= new ContextInformation(
				MessageFormat.format(TomEditorMessages.getResourceString("CompletionProcessor.ContextInfo.display.pattern"), new Object[] { new Integer(i), new Integer(documentOffset) }),  //$NON-NLS-1$
				MessageFormat.format(TomEditorMessages.getResourceString("CompletionProcessor.ContextInfo.value.pattern"), new Object[] { new Integer(i), new Integer(documentOffset - 5), new Integer(documentOffset + 5)})); //$NON-NLS-1$
		return result;
	}
	
	/* (non-Javadoc)
	 * Method declared on IContentAssistProcessor
	 */
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '%' };
	}
	
	/* (non-Javadoc)
	 * Method declared on IContentAssistProcessor
	 */
	public char[] getContextInformationAutoActivationCharacters() {
		return null;//
	}
	
	/* (non-Javadoc)
	 * Method declared on IContentAssistProcessor
	 */
	public IContextInformationValidator getContextInformationValidator() {
		return fValidator;
	}
	
	/* (non-Javadoc)
	 * Method declared on IContentAssistProcessor
	 */
	public String getErrorMessage() {
		return null;
	}

} //class TomCompletionProcessor