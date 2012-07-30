/*

    TOM - To One Matching Compiler

    Copyright (C) 2004 INRIA
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;

import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * A Tom code scanner.
 */
public class TomCodeScanner extends RuleBasedScanner {
	private TomColorManager colorProvider;
	private String fileName;
	public final static String[] fgKeywords = { "%typestring", "%typeint",
			"%include", "%match", "%op", "%oplist", "%rule", "%typeterm",
			"%typelist", "%typearray", "%oplist", "%oparray", "%strategy",
			"%gom" };

	
	/**
	 * A java aware white space detector.
	 */
	private class JavaWhitespaceDetector implements IWhitespaceDetector {

		/**
		 * @see IWhitespaceDetector#isWhitespace
		 */
		public boolean isWhitespace(char c) {
			return Character.isWhitespace(c);
		}
	}
	
	/**
	 * A Java aware word detector.
	 */
	public class JavaWordDetector implements IWordDetector {

		/**
		 * @see IWordDetector#isWordStart
		 */
		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}

		/**
		 * @see IWordDetector#isWordPart
		 */
		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}
	}

	/**
	 * Creates a Tom code scanner
	 */
	public TomCodeScanner(TomColorManager provider) {
		this.colorProvider = provider;
		initRules();
	}

	/**
	 * Creates a Tom code scanner
	 */
	public TomCodeScanner(TomColorManager provider, String fileName) {
		this.colorProvider = provider;
		this.fileName = fileName;
		initRules();
	}

	// IToken type= new Token(new
	// TextAttribute(TomColorProvider.getColor(TomColorProvider.TYPE)));

	private void initRules() {
		List<IRule> rules = new ArrayList<IRule>();
		if (fileName != null) {
			getInfoFromFile(rules);
		}
		// Add rule for single line comments.
		 IToken comment= new Token(new
		 TextAttribute(colorProvider.getColor(TomColorManager.SINGLE_LINE_COMMENT_COLOR)));
		 rules.add(new EndOfLineRule("//", comment)); //$NON-NLS-1$

			// Add rule for single line comments.
		 IToken javadoc= new Token(new
		 TextAttribute(colorProvider.getColor(TomColorManager.JAVADOC_DEFAULT_COLOR)));
		
		 IToken multiline=  new Token(new
		TextAttribute(colorProvider.getColor(TomColorManager.MULTI_LINE_COMMENT_COLOR)));
		 
		 
		// Add rule for strings and character constants.
		 IToken string= new Token(new
		 TextAttribute(colorProvider.getColor(TomColorManager.STRING_COLOR))); 
		 
		 rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		 rules.add(new SingleLineRule("'", "'", string, '\\'));
		// Add rules for multi-line comments and javadoc.
		 rules.add(new MultiLineRule("/**", "*/", javadoc, (char) 0, true));
		 rules.add(new MultiLineRule("/*", "*/", multiline, (char) 0, true));
		// Add generic whitespace rule.
		 rules.add(new WhitespaceRule(new JavaWhitespaceDetector()));

		// Add word rule for keywords, types, and constants.
		IToken other = new Token(new TextAttribute(colorProvider
				.getColor(TomColorManager.DEFAULT_COLOR)));
		WordRule javaWordRule = new WordRule(new JavaWordDetector(), other);
		rules.add(javaWordRule);

		WordRule tomWordRule = new WordRule(new TomWordDetector());

		IToken keyword = new Token(new TextAttribute(colorProvider
				.getColor(JtomPlugin.getDefault().getPreferenceStore()
						.getString(JtomPlugin.HIGHLIGHT_PREFERENCE))));
		for (int i = 0; i < fgKeywords.length; i++) {
			tomWordRule.addWord(fgKeywords[i], keyword);
		}
		rules.add(tomWordRule);

		IToken tomAnchor = new Token(new TextAttribute(colorProvider
				.getColor(JtomPlugin.getDefault().getPreferenceStore()
						.getString(JtomPlugin.HIGHLIGHT_PREFERENCE))));
	
		
		class TomLimiterRule implements IRule {
			private IToken fDefaultToken = Token.UNDEFINED;
			private int readChar;

			TomLimiterRule(IToken fDefaultToken) {
				this.fDefaultToken = fDefaultToken;
			}

			public IToken evaluate(ICharacterScanner scanner) {
				int c = scanner.read();
				readChar++;
				if (c == '-') {
					c = scanner.read();
					readChar++;
					if (c == '>') {
						return fDefaultToken;
					}
				} else if (c == '{' || c == '}' || c == '`' || c == '@'
						|| c == '[' || c == ']' || c == '=') {
					return fDefaultToken;
				} else if (c == '_') {
					c = scanner.read();
					readChar++;
					if (!Character.isLetterOrDigit((char) c)) {
						return fDefaultToken;
					}
				}
				scanner.unread();
				return Token.UNDEFINED;
			}

		
		} // class TomLimiterRule

		rules.add(new TomLimiterRule(tomAnchor));

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}

	/**
	 * 
	 */
	private void getInfoFromFile(List<IRule> rules) {
		String[] key = { "toto", "toto2" };
		WordRule tomWordRule = new WordRule(new JavaWordDetector());
		IToken keyword = new Token(new TextAttribute(colorProvider
				.getColor(JtomPlugin.getDefault().getPreferenceStore()
						.getString(JtomPlugin.HIGHLIGHT_PREFERENCE))));
		for (int i = 0; i < key.length; i++) {
			tomWordRule.addWord(key[i], keyword);
		}
		rules.add(tomWordRule);
	}

} //class TomCodeScanner