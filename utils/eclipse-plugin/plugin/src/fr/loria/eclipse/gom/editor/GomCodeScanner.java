/*

    TOM - To One Matching Compiler

    Copyright (C) 2004-2014 Inria Nancy, France.

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

package fr.loria.eclipse.gom.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.ui.text.IJavaColorConstants;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.util.PropertyChangeEvent;

import fr.loria.eclipse.gom.editor.rules.TypeRule;
import fr.loria.eclipse.gom.editor.util.GomColorManager;
import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * A Gom code scanner
 * 
 * @author Martin GRANDCOLAS
 */
public class GomCodeScanner extends BufferedRuleBasedScanner {

	public final static String[] HeaderKeywords = { "module" };

	public final static String[] gomKeyWords = { "abstract", "imports",
			"syntax", "=", "|", ":", "->" };

	public GomCodeScanner() {
		//fast implementation
		super(255);
		List<?> rules = createRules();
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result); 
		
		

		

	}
	
	public boolean affectsBehavior(PropertyChangeEvent event){
		return false; 
	}

	/**
	 * a Gom Word detector
	 */

	private class GomWordDetector implements IWordDetector {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
		 */
		public boolean isWordStart(char c) {

			return Character.isLetter(c) || c == '|' || c == '=' || c == ':' || c =='-';
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
		 */
		public boolean isWordPart(char c) {
			return Character.isLetter(c) || c == '|' || c == '=' || c == ':' || c=='-' || c=='>';
		}

	} // class GomWordDetector

	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();

		GomColorManager provider = JtomPlugin.getDefault()
				.getGomColorManager();
		IToken gomcode = new Token(provider
				.getAttribute(GomColorManager.GOM_CODE_ATTRIBUTE));
		IToken gomheader = new Token(provider
				.getAttribute(GomColorManager.GOM_HEADER_ATTRIBUTE));
		IToken gomtype = new Token(provider
				.getAttribute(GomColorManager.GOM_TYPE_ATTRIBUTE));
		IToken undefined = new Token(provider
				.getAttribute(GomColorManager.DEFAULT_ATTRIBUTE));
		IToken string = new Token(provider
				.getAttribute(IJavaColorConstants.JAVA_STRING));
		IToken javadoc = new Token(provider
				.getAttribute(IJavaColorConstants.JAVADOC_DEFAULT));
		IToken multiline = new Token(provider
				.getAttribute(IJavaColorConstants.JAVA_MULTI_LINE_COMMENT));
		IToken singleline = new Token(provider
				.getAttribute(IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT));
		//String detection
		rules.add(new SingleLineRule("\"", "\"", string, '\0', true));
		//Character detection
		rules.add(new SingleLineRule("'", "'", string, '\0', true));
		//single comments detection
		rules.add(new SingleLineRule("//", "\r", singleline, '\0', true));
		//javadoc detection
		rules.add(new MultiLineRule("/**", "*/", javadoc, '\0', true));
		//multiline comments detection
		rules.add(new MultiLineRule("/*", "*/", multiline, '\0', true));
		//to find the gom words
		rules.add(new TypeRule(gomtype));
		rules.add(new WhitespaceRule(new IWhitespaceDetector() {

			public boolean isWhitespace(char c) {
				return Character.isWhitespace(c);
			}
		}));

		WordRule wr = new WordRule(new GomWordDetector(), undefined);
		for (int i = 0; i < HeaderKeywords.length; ++i) {
			wr.addWord(HeaderKeywords[i], gomheader);
		}

		for (int i = 0; i < gomKeyWords.length; ++i) {
			wr.addWord(gomKeyWords[i], gomcode);
		}

		rules.add(wr);
		
		return rules; 
	}

	protected String[] getTokenProperties() {
		return new String[]{
				GomColorManager.DEFAULT_ATTRIBUTE,
				GomColorManager.GOM_CODE_ATTRIBUTE,
				GomColorManager.GOM_HEADER_ATTRIBUTE,
				GomColorManager.GOM_TYPE_ATTRIBUTE
		};
		
	}

} //class GomCodeScanner