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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import fr.loria.eclipse.jtom.JtomPlugin;
import fr.loria.eclipse.jtom.editor.util.BufferedDocumentScanner;
import fr.loria.eclipse.jtom.editor.util.TomCodeScanner;

/**
 * This scanner recognizes the JavaDoc comments, Java multi line comments, Java
 * single line comments, Java strings and Java characters and TOM code.
 */
public class TomEditorPartitionScanner implements IPartitionTokenScanner,
		ITomPartitions {

	private static int MAX_KEY_SIZE = 8;
	private static Set<String> tomKeywords = new HashSet<String>();

	public final static String[] PARTITION_TYPES = new String[] {// 0
	// (Default)
			JAVA_DOC, // 1
			JAVA_MULTI_LINE_COMMENT, // 2
			JAVA_SINGLE_LINE_COMMENT, // 3
			JAVA_STRING, // 4
			JAVA_CHARACTER, // 5
			TOM_HEADER_PART, // 6
			TOM_CODE_PART, // 7
			TOM_JAVA_PART, // 8
			TOM_CLOSURE_PART, // 9
			TOM_BACKQUOTE_TOM_PART, // 10
			TOM_BACKQUOTE_JAVA_PART, // 11,
			GOM_PART };

	// states
	private static final int JAVA = 0;
	private static final int JAVADOC = 1;
	private static final int MULTI_LINE_COMMENT = 2;
	private static final int SINGLE_LINE_COMMENT = 3;
	private static final int STRING = 4;
	private static final int CHARACTER = 5;
	private static final int TOM_HEADER = 6;
	private static final int TOM_CODE = 7;
	private static final int TOM_JAVA = 8;
	private static final int TOM_CLOSURE = 9;
	private static final int TOM_BACKQUOTE_JAVA = 10;
	private static final int TOM_BACKQUOTE_TOM = 11;
	private static final int GOM_CODE = 12;

	private final IToken[] fTokens = new IToken[] { new Token(null),
			new Token(JAVA_DOC), new Token(JAVA_MULTI_LINE_COMMENT),
			new Token(JAVA_SINGLE_LINE_COMMENT), new Token(JAVA_STRING),
			new Token(JAVA_CHARACTER), new Token(TOM_HEADER_PART),
			new Token(TOM_CODE_PART), new Token(TOM_JAVA_PART),
			new Token(TOM_CLOSURE_PART), new Token(TOM_BACKQUOTE_JAVA_PART),
			new Token(TOM_BACKQUOTE_TOM_PART), new Token(GOM_PART), };

	public TomEditorPartitionScanner() {

		// TODO: static final field or initialized once
		for (int i = 0; i < TomCodeScanner.fgKeywords.length; i++) {
			// we delete the '%' char

			tomKeywords.add(TomCodeScanner.fgKeywords[i].substring(1));
		}
		// tomKeywords.add("match");
		// tomKeywords.add("rule");
		// tomKeywords.add("op");
		// tomKeywords.add("oplist");
		// tomKeywords.add("oparray");
		// tomKeywords.add("typeterm");
		// tomKeywords.add("typelist");
		// tomKeywords.add("typearray");
		// tomKeywords.add("include");
		// tomKeywords.add("typeint");
		// tomKeywords.add("typestring");
	}

	/** The amount of open bracket found in TOM/JAVA mode */
	// add a hashTable between TOM_JAVA state and the corresponding open bracket
	// value
	// private int nbOpenBracketFound;
	private int nbJavaOpenBracketFound;

	private int nbTomOpenBracketFound;

	// beginning of prefixes and postfixes
	private static final int NONE = 0;
	private static final int BACKSLASH = 1; // postfix for STRING and CHARACTER
	private static final int SLASH = 2; // prefix for SINGLE_LINE or MULTI_LINE
	// or JAVADOC
	private static final int SLASH_STAR = 3; // prefix for MULTI_LINE_COMMENT
	// or JAVADOC
	private static final int SLASH_STAR_STAR = 4; // prefix for
	// MULTI_LINE_COMMENT or
	// JAVADOC
	private static final int STAR = 5; // postfix for MULTI_LINE_COMMENT or
	// JAVADOC
	private static final int CARRIAGE_RETURN = 6; // postfix for STRING,
	// CHARACTER and
	// SINGLE_LINE_COMMENT
	private static final int POURCENT = 7;
	private static final int OPEN_BRACKET = 8;
	private static final int CLOSE_BRACKET = 9;

	/** The scanner. */
	private final BufferedDocumentScanner fScanner = new BufferedDocumentScanner(
			1000); // faster implementation

	/** The offset of the last returned token. */
	private int lastReturnedTokenOffset;
	/** The length of the last returned token. */
	private int lastReturnedTokenLength;

	/** The state of the scanner. */
	private int scannerState;
	/** The last significant characters read. */
	private int lastSignificantChar;
	/** The amount of characters already read on first call to nextToken(). */
	private int nbCharAlreadyReadOnFirstCallToNextToken;

	/*
	 * @see org.eclipse.jface.text.rules.ITokenScanner#nextToken()
	 */
	public IToken nextToken() {
		// debugPrtln("++++++++++++++++++++++++++++++++++++++++++++++++");
		// debugPrtln("=====> NextToken");
		IToken token = internalNextToken();
		String res = token.getData() == null ? token.isEOF() ? "EOF" : "Java"
			: token.getData().toString();
		debugPrtln("=====> NextToken: "+res+ " with length="
				+lastReturnedTokenLength + " with offset="
				+lastReturnedTokenOffset);
				debugPrtln("++++++++++++++++++++++++++++++++++++++++++++++++");
		return token;
	}

	private IToken internalNextToken() {
		StringBuffer fBuffer = new StringBuffer();
		fBuffer.setLength(0);

		// debugPrtln("init lastReturnedTokenOffset with lastReturnTokenLength:
		// "+lastReturnedTokenLength);
		lastReturnedTokenOffset += lastReturnedTokenLength;
		// debugPrtln("init lastReturnedTokenLength with
		// nbCharAlreadyReadOnFirstCallToNextToken:
		// "+nbCharAlreadyReadOnFirstCallToNextToken);
		lastReturnedTokenLength = nbCharAlreadyReadOnFirstCallToNextToken;
		nbJavaOpenBracketFound = 0;

		while (true) {
			final int ch = fScanner.read();
			/*
			 * if(ch != '\n') { debugPrtln("Reading `"+Character.toString((char)
			 * ch)+"` => int value "+ch+" state is: "+scannerState+"
			 * lastReturnedTokenLength= "+lastReturnedTokenLength); } else {
			 * debugPrtln("Reading \\n => int value "+ch+" state is:
			 * "+scannerState+" lastReturnedTokenLength=
			 * "+lastReturnedTokenLength); }
			 */
			// characters
			switch (ch) {// first pass

			case ICharacterScanner.EOF:
				lastSignificantChar = NONE; // ignore last
				if (lastReturnedTokenLength > 0) {
					// debugPrtln("ICharacterScanner.EOF read with
					// lastReturnTokenLength= "+lastReturnedTokenLength);
					// debugPrtln("returning token associated to state
					// "+scannerState);
					return preFix(scannerState, scannerState, NONE, 0);
				} else {
					// debugPrtln("ICharacterScanner.EOF read with
					// lastReturnTokenLength == 0");
					nbCharAlreadyReadOnFirstCallToNextToken = 0;
					// debugPrtln("Returning Token.EOF");
					return Token.EOF;
				}

			case '\r':
				// debugPrtln("STUDYING \\r");
				if (lastSignificantChar != CARRIAGE_RETURN) {
					lastSignificantChar = CARRIAGE_RETURN;
					lastReturnedTokenLength++;
					continue;
				} else {
					switch (scannerState) {
					case SINGLE_LINE_COMMENT:
					case CHARACTER:
					case STRING:
						if (lastReturnedTokenLength > 0) {
							IToken token = fTokens[scannerState];
							lastSignificantChar = CARRIAGE_RETURN;
							nbCharAlreadyReadOnFirstCallToNextToken = 1;
							scannerState = JAVA;
							return token;
						} else {
							consume();
							continue;
						}
					default:
						consume();
						continue;
					}
				}

			case '\n':
				// debugPrtln("STUDYING \\n");
				switch (scannerState) {
				case SINGLE_LINE_COMMENT:
				case CHARACTER:
				case STRING:
					return postFix(scannerState, JAVA);

				default:
					consume();
					continue;
				}

			default:
				if (lastSignificantChar == CARRIAGE_RETURN) {
					// debugPrtln("lastSignificantChar == CARRIAGE_RETURN");
					switch (scannerState) {
					case SINGLE_LINE_COMMENT:
					case CHARACTER:
					case STRING:
						int last;
						int newState;
						switch (ch) {
						case '/':
							last = SLASH;
							newState = JAVA;
							break;

						case '*':
							last = STAR;
							newState = JAVA;
							break;

						case '\'':
							last = NONE;
							newState = CHARACTER;
							break;

						case '"':
							last = NONE;
							newState = STRING;
							break;

						case '\r':
							last = CARRIAGE_RETURN;
							newState = JAVA;
							break;

						case '\\':
							last = BACKSLASH;
							newState = JAVA;
							break;

						default:
							last = NONE;
							newState = JAVA;
							break;
						} // switch (ch)
						lastSignificantChar = NONE; // ignore
						// lastSignificantChar
						return preFix(scannerState, newState, last, 1);
					default:
						break;
					} // switch (scannerState)
				} // if (lastSignificantChar == CARRIAGE_RETURN)
			} // switch (ch) first pass

			// states
			switch (scannerState) {

			case JAVA:
				switch (ch) {
				case '%':
					String key = ensureTomKey();
					if (key != null) {
						debugPrtln("Found a %tom_key in JAVA state");
						if (lastReturnedTokenLength > 0) {
							return preFix(JAVA, TOM_HEADER, POURCENT, 1);
						} else {
							preFix(JAVA, TOM_HEADER, POURCENT, 1);
							lastReturnedTokenOffset += lastReturnedTokenLength;
							lastReturnedTokenLength = nbCharAlreadyReadOnFirstCallToNextToken;
							break;
						}
					} else {
						consume();
						break;
					}

				case '`':
					debugPrtln("Found a ` in JAVA state");
					if (lastReturnedTokenLength > 0) {
						return preFix(JAVA, TOM_BACKQUOTE_JAVA, POURCENT, 1);
					} else {
						preFix(JAVA, TOM_BACKQUOTE_JAVA, POURCENT, 1);
						lastReturnedTokenOffset += lastReturnedTokenLength;
						lastReturnedTokenLength = nbCharAlreadyReadOnFirstCallToNextToken;
						break;
					}

				case '/':
					if (lastSignificantChar == SLASH) {
						if (lastReturnedTokenLength
								- getLastLength(lastSignificantChar) > 0) {
							return preFix(JAVA, SINGLE_LINE_COMMENT, NONE, 2);
						} else {
							preFix(JAVA, SINGLE_LINE_COMMENT, NONE, 2);
							lastReturnedTokenOffset += lastReturnedTokenLength;
							lastReturnedTokenLength = nbCharAlreadyReadOnFirstCallToNextToken;
							break;
						}
					} else {
						lastReturnedTokenLength++;
						lastSignificantChar = SLASH;
						break;
					}

				case '*':
					if (lastSignificantChar == SLASH) {
						if (lastReturnedTokenLength
								- getLastLength(lastSignificantChar) > 0)
							return preFix(JAVA, MULTI_LINE_COMMENT, SLASH_STAR,
									2);
						else {
							preFix(JAVA, MULTI_LINE_COMMENT, SLASH_STAR, 2);
							lastReturnedTokenOffset += lastReturnedTokenLength;
							lastReturnedTokenLength = nbCharAlreadyReadOnFirstCallToNextToken;
							break;
						}
					} else {
						consume();
						break;
					}

				case '\'':// SINGLE_QUOTE
					lastSignificantChar = NONE; // ignore lastSignificantChar
					if (lastReturnedTokenLength > 0) {
						return preFix(JAVA, CHARACTER, NONE, 1);
					} else {
						preFix(JAVA, CHARACTER, NONE, 1);
						lastReturnedTokenOffset += lastReturnedTokenLength;
						lastReturnedTokenLength = nbCharAlreadyReadOnFirstCallToNextToken;
						break;
					}

				case '"':// DOUBLE_QUOTE
					lastSignificantChar = NONE; // ignore lastSignificantChar
					if (lastReturnedTokenLength > 0) {
						return preFix(JAVA, STRING, NONE, 1);
					} else {
						preFix(JAVA, STRING, NONE, 1);
						lastReturnedTokenOffset += lastReturnedTokenLength;
						lastReturnedTokenLength = nbCharAlreadyReadOnFirstCallToNextToken;
						break;
					}

				default:
					consume();
					break;
				} // switch (ch) case JAVA
				break;

			case SINGLE_LINE_COMMENT:
				consume();
				break;

			case JAVADOC:
				switch (ch) {
				case '/':
					switch (lastSignificantChar) {
					case SLASH_STAR_STAR:
						return postFix(MULTI_LINE_COMMENT, JAVA);
					case STAR:
						return postFix(JAVADOC, JAVA);
					default:
						consume();
						break;
					} // switch (lastSignificantChar)
					break;
				case '*':
					lastReturnedTokenLength++;
					lastSignificantChar = STAR;
					break;

				default:
					consume();
					break;
				}
				break;

			case MULTI_LINE_COMMENT:
				switch (ch) {
				case '*':
					if (lastSignificantChar == SLASH_STAR) {
						lastSignificantChar = SLASH_STAR_STAR;
						lastReturnedTokenLength++;
						scannerState = JAVADOC;
					} else {
						lastReturnedTokenLength++;
						lastSignificantChar = STAR;
					}
					break;
				case '/':
					if (lastSignificantChar == STAR) {
						return postFix(MULTI_LINE_COMMENT, JAVA);
					} else {
						consume();
						break;
					}

				default:
					consume();
					break;
				} // switch (ch) case MULTI_LINE_COMMENT
				break;

			case STRING:
				switch (ch) {
				case '\\':
					lastSignificantChar = (lastSignificantChar == BACKSLASH) ? NONE
							: BACKSLASH;
					lastReturnedTokenLength++;
					break;

				case '\"':
					if (lastSignificantChar != BACKSLASH) {
						return postFix(STRING, JAVA);
					} else {
						consume();
						break;
					}

				default:
					consume();
					break;
				} // switch (ch) case STRING
				break;

			case CHARACTER:
				switch (ch) {
				case '\\':
					lastSignificantChar = (lastSignificantChar == BACKSLASH) ? NONE
							: BACKSLASH;
					lastReturnedTokenLength++;
					break;

				case '\'':
					if (lastSignificantChar != BACKSLASH) {
						return postFix(CHARACTER, JAVA);
					} else {
						consume();
						break;
					}

				default:
					consume();
					break;
				} // switch (ch) case CHARACTER
				break;

			case TOM_HEADER:
				switch (ch) {
				case '{':
					nbTomOpenBracketFound++;
					debugPrtln("TR1: Closing Tom_header -> Tom_code");
					// We know that there is at laeast the tom key and '{' so
					// lastReturnedTokenLength is >0
					return postFix(TOM_HEADER, TOM_CODE);

				default:
					fBuffer.append((char) ch);
					if (fBuffer.toString().equals("typeint")
							|| fBuffer.toString().equals("typestring")) {
						return postFix(TOM_HEADER, JAVA);
					}
					if (fBuffer.toString().equals("gom")) {

						return postFix(TOM_HEADER, GOM_CODE);
					}
					consume();
					break;
				} // switch (ch) case TOM_HEADER
				break;
			case GOM_CODE:
				switch (ch) {
				case '{':
					nbTomOpenBracketFound++;
					return postFix(GOM_CODE, GOM_CODE);
				case '}':

					lastSignificantChar = NONE;
					if (lastReturnedTokenLength > 0) {

						fScanner.unread();
						return preFix(GOM_CODE, TOM_CLOSURE, CLOSE_BRACKET, 0);
					} else {
						nbTomOpenBracketFound--;
						return postFix(GOM_CODE, GOM_CODE);

					}

				default:
					// System.out.print((char)ch);
					consume();
					break;
				}
				break;
			case TOM_CLOSURE:
				switch (ch) {

				case '}':
					nbTomOpenBracketFound--;
					// lastReturnedTokenLength can be null; %match() {}
					debugPrtln("TR2: Closing Tom_closure -> Java");
					return postFix(TOM_CLOSURE, JAVA);

				default:
					consume();
					break;
				} // switch (ch) case TOM_CLOSURE
				break;

			case TOM_CODE:
				switch (ch) {
				case '{':
					nbTomOpenBracketFound++;
					debugPrtln("TR3: Closing Tom_code-> Tom_java");
					return postFix(TOM_CODE, TOM_JAVA);

				case '}':

					lastSignificantChar = NONE;
					if (lastReturnedTokenLength > 0) {

						debugPrtln("TR4: Closing Tom_code-> Tom-closure");
						fScanner.unread();
						return preFix(TOM_CODE, TOM_CLOSURE, CLOSE_BRACKET, 0);
					} else {
						nbTomOpenBracketFound--;
						debugPrtln("TR5: Closing Tom_code-> Tom-code");
						return postFix(TOM_CODE, TOM_CODE);
						// return postFix(TOM_CLOSURE, JAVA);
					}

				default:
					consume();
					break;
				} // switch (ch) case TOM_CODE
				break;

			case TOM_JAVA:
				switch (ch) {
				case '{':
					nbJavaOpenBracketFound++;
					debugPrtln("nb JavaOpenbracket=" + nbJavaOpenBracketFound);
					consume();
					break;

				case '}':
					nbJavaOpenBracketFound--;
					debugPrtln("nb JavaOpenbracket=" + nbJavaOpenBracketFound);
					if (nbJavaOpenBracketFound < 0) {
						if (lastReturnedTokenLength > 0) {
							debugPrtln("TR6: Closing Tom_java-> Tom_code");
							lastSignificantChar = NONE;
							return preFix(TOM_JAVA, TOM_CODE, CLOSE_BRACKET, 1);
						} else {
							debugPrtln("TR7: Closing Tom_java-> Tom_code");
							lastSignificantChar = NONE;
							preFix(TOM_JAVA, TOM_CODE, CLOSE_BRACKET, 1);
						}
					} else {
						consume();
						break;
					}
					break;
				case '(':
					nbJavaOpenBracketFound++;
					debugPrtln("nb JavaOpenPar=" + nbJavaOpenBracketFound);
					consume();
					break;
				case ')':
					nbJavaOpenBracketFound--;
					debugPrtln("nb JavaOpenPar=" + nbJavaOpenBracketFound);
					consume();
					break;

				case '`':
					debugPrtln("Found a ` in TOM_JAVA state");
					if (lastReturnedTokenLength > 0) {
						return preFix(TOM_JAVA, TOM_BACKQUOTE_TOM, POURCENT, 1);
					} else {
						preFix(TOM_JAVA, TOM_BACKQUOTE_TOM, POURCENT, 1);
						lastReturnedTokenOffset += lastReturnedTokenLength;
						lastReturnedTokenLength = nbCharAlreadyReadOnFirstCallToNextToken;
						break;
					}
				default:
					consume();
					break;
				} // switch (ch) case TOM_JAVA
				break;

			case TOM_BACKQUOTE_JAVA:
				switch (ch) {
				case '(':
					nbJavaOpenBracketFound++;
					debugPrtln("nb JavaOpenPar=" + nbJavaOpenBracketFound);
					consume();
					break;
				case ')':
					nbJavaOpenBracketFound--;
					debugPrtln("nb JavaOpenPar=" + nbJavaOpenBracketFound);
					if (nbJavaOpenBracketFound == 0) {
						if (lastReturnedTokenLength > 0) {
							debugPrtln("TR8: Closing Tom_back_java-> java");
							lastSignificantChar = NONE;
							return postFix(TOM_BACKQUOTE_JAVA, JAVA);
						} else {
							debugPrtln("TR9: Closing Tom_back_java-> java");
							lastSignificantChar = NONE;
							postFix(TOM_BACKQUOTE_JAVA, JAVA);
						}
					} else {
						consume();
						break;
					}
					break;

				default:
					consume();
					break;
				} // switch (ch) case TOM_BACKQUOTE_JAVA
				break;

			case TOM_BACKQUOTE_TOM:
				switch (ch) {
				case '(':
					nbJavaOpenBracketFound++;
					debugPrtln("nb JavaOpenPar=" + nbJavaOpenBracketFound);
					consume();
					break;
				case ')':
					nbJavaOpenBracketFound--;
					debugPrtln("nb JavaOpenPar=" + nbJavaOpenBracketFound);
					if (nbJavaOpenBracketFound <= 0) {
						if (lastReturnedTokenLength > 0) {
							debugPrtln("TR10: Closing Tom_back_java-> Tom_java");
							lastSignificantChar = NONE;
							return postFix(TOM_BACKQUOTE_TOM, TOM_JAVA);
						} else {
							debugPrtln("TR11: Closing Tom_back_java-> Tom_java");
							lastSignificantChar = NONE;
							postFix(TOM_BACKQUOTE_TOM, TOM_JAVA);
						}
					} else {
						consume();
						break;
					}
				case '}':
					nbTomOpenBracketFound--;
					lastSignificantChar = NONE;
					if (nbTomOpenBracketFound <= 0) {
						return postFix(TOM_BACKQUOTE_TOM, TOM_JAVA);
						/** ****JAVA********* */
					} else {
						return postFix(TOM_BACKQUOTE_TOM, TOM_CODE);
					}
				default:
					consume();
					break;
				} // switch (ch) case TOM_BACKQUOTE_TOM
				break;
			} // switch (scannerState)
		} // while (true)
	} // nextToken

	/**
	 * @return
	 */
	private String ensureTomKey() {
		// read char and validate its a TOM key
		StringBuffer fBuffer = new StringBuffer();
		boolean found = false;
		int c = fScanner.read();
		fBuffer.setLength(0);
		for (int i = 0; i < MAX_KEY_SIZE && c != ICharacterScanner.EOF; i++) {
			fBuffer.append((char) c);
			if (tomKeywords.contains(fBuffer.toString())) {
				c = fScanner.read();
				if (Character.isLetterOrDigit((char) c)) {
					found = false;
				} else {
					found = true;
				}
				fScanner.unread();
				break;
			}
			c = fScanner.read();
		}
		unreadBuffer(fBuffer);
		if (found) {
			return fBuffer.toString();
		} else {
			return null;
		}

	}

	private void unreadBuffer(StringBuffer fBuffer) {
		for (int i = fBuffer.length() - 1; i >= 0; i--)
			fScanner.unread();
	}

	private static final int getLastLength(int last) {
		switch (last) {
		default:
			return -1;
		case NONE:
			return 0;
		case CARRIAGE_RETURN:
		case BACKSLASH:
		case SLASH:
		case STAR:
		case POURCENT:
		case OPEN_BRACKET:
		case CLOSE_BRACKET:
			return 1;
		case SLASH_STAR:
			return 2;
		case SLASH_STAR_STAR:
			return 3;
		}
	}

	private final void consume() {
		lastReturnedTokenLength++;
		lastSignificantChar = NONE;
	}

	private final IToken postFix(int state, int newState) {
		lastReturnedTokenLength++;
		lastSignificantChar = NONE;
		nbCharAlreadyReadOnFirstCallToNextToken = 0;
		scannerState = newState;
		return fTokens[state];
	}

	private final IToken preFix(int state, int newState, int last,
			int prefixLength) {
		lastReturnedTokenLength -= getLastLength(lastSignificantChar);
		lastSignificantChar = last;
		nbCharAlreadyReadOnFirstCallToNextToken = prefixLength;
		scannerState = newState;
		return fTokens[state];
	}

	private static int getState(String contentType) {
		if (contentType != null) {
			if (contentType.equals(JAVA_SINGLE_LINE_COMMENT)) {
				return SINGLE_LINE_COMMENT;
			} else if (contentType.equals(JAVA_MULTI_LINE_COMMENT)) {
				return MULTI_LINE_COMMENT;
			} else if (contentType.equals(JAVA_DOC)) {
				return JAVADOC;
			} else if (contentType.equals(JAVA_STRING)) {
				return STRING;
			} else if (contentType.equals(JAVA_CHARACTER)) {
				return CHARACTER;
			} else if (contentType.equals(TOM_CODE_PART)) {
				return TOM_CODE;
			} else if (contentType.equals(TOM_HEADER_PART)) {
				return TOM_HEADER;
			} else if (contentType.equals(TOM_JAVA_PART)) {
				return TOM_JAVA;
			} else if (contentType.equals(TOM_CLOSURE_PART)) {
				return TOM_CLOSURE;
			} else if (contentType.equals(GOM_PART)) {
				return GOM_CODE;
			} else /* if(contentType.equals(JAVA)) */{
				return JAVA;
			}
		} else {
			return JAVA;
		}
	}

	/*
	 * @see IPartitionTokenScanner#setPartialRange(IDocument, int, int, String,
	 *      int)
	 */
	public void setPartialRange(IDocument document, int offset, int length,
			String contentType, int partitionOffset) {
		fScanner.setRange(document, offset, length);
		lastReturnedTokenOffset = partitionOffset;
		lastReturnedTokenLength = 0;
		nbCharAlreadyReadOnFirstCallToNextToken = offset - partitionOffset;
		lastSignificantChar = NONE;

		if (offset == partitionOffset && !contentType.equals(TOM_CLOSURE_PART)
				&& !contentType.equals(TOM_JAVA_PART)
				&& !contentType.equals(TOM_CODE_PART)
				&& !contentType.equals(TOM_HEADER_PART)) {
			// restart at beginning of partition
			scannerState = JAVA;
		} else {
			scannerState = getState(contentType);
		}
		debugPrtln("\n**********************************************************************");
		debugPrtln("setPartialRange: OFFSET= " + offset + " LENGTH= " + length
				+ " CONTENTTYPE= " + contentType + " PARTITIONOFFSET= "
				+ partitionOffset + " NEWSTATE=" + scannerState);
	}

	/*
	 * @see ITokenScanner#setRange(IDocument, int, int)
	 */
	public void setRange(IDocument document, int offset, int length) {
		debugPrtln("\n**********************************************************************");
		debugPrtln("setRange: OFFSET= " + offset + " LENGTH " + length);
		fScanner.setRange(document, offset, length);
		lastReturnedTokenOffset = offset;
		lastReturnedTokenLength = 0;
		nbCharAlreadyReadOnFirstCallToNextToken = 0;
		lastSignificantChar = NONE;
		scannerState = JAVA;
	}

	/*
	 * @see ITokenScanner#getTokenLength()
	 */
	public int getTokenLength() {
		return lastReturnedTokenLength;
	}

	/*
	 * @see ITokenScanner#getTokenOffset()
	 */
	public int getTokenOffset() {
		return lastReturnedTokenOffset;
	}

	private void debugPrtln(String debug) {
		if (JtomPlugin.getDefault().isDebugging()) {
			System.out.println(debug);
		}
	}

} //class TomEditorPartitionScanner
