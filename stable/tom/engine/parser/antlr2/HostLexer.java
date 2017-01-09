// $ANTLR 2.7.7 (20060906): "HostLanguage.g" -> "HostLexer.java"$
/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
 *
 **/

package tom.engine.parser.antlr2;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

  import antlr.*;

public class HostLexer extends antlr.CharScanner implements HostParserTokenTypes, TokenStream
 {

    // this buffer contains the target code
    // we append each read character by lexer
    public StringBuilder target = new StringBuilder("");

    // the target parser
    private HostParser parser = null;

    public void setParser(HostParser parser) {
        this.parser = parser;
    }

    // clear the buffer
    public void clearTarget(){
        target.delete(0,target.length());
    }

    private TokenStreamSelector selector(){
        return parser.getSelector();
    }

public HostLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public HostLexer(Reader in) {
	this(new CharBuffer(in));
}
public HostLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public HostLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		setCommitToPath(false);
		int _m;
		_m = mark();
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				switch ( LA(1)) {
				case '`':
				{
					mBACKQUOTE(true);
					theRetToken=_returnToken;
					break;
				}
				case '{':
				{
					mLBRACE(true);
					theRetToken=_returnToken;
					break;
				}
				case '}':
				{
					mRBRACE(true);
					theRetToken=_returnToken;
					break;
				}
				case '"':
				{
					mSTRING(true);
					theRetToken=_returnToken;
					break;
				}
				case '\t':  case '\n':  case '\u000c':  case '\r':
				case ' ':
				{
					mWS(true);
					theRetToken=_returnToken;
					break;
				}
				case '/':
				{
					mCOMMENT(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((LA(1)=='%') && (LA(2)=='t') && (LA(3)=='r') && (LA(4)=='a') && (LA(5)=='n')) {
						mTRANSFORMATION(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='t') && (LA(3)=='r') && (LA(4)=='a') && (LA(5)=='c')) {
						mTRACELINK(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='o') && (LA(3)=='p') && (LA(4)=='l')) {
						mOPERATORLIST(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='o') && (LA(3)=='p') && (LA(4)=='a')) {
						mOPERATORARRAY(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='s') && (LA(3)=='t')) {
						mSTRATEGY(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='o') && (LA(3)=='p') && (true)) {
						mOPERATOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='s') && (LA(3)=='u')) {
						mSUBTYPE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='t') && (LA(3)=='y')) {
						mTYPETERM(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='m')) {
						mMATCH(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='r')) {
						mRESOLVE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='i')) {
						mINCLUDE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='g')) {
						mGOM(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='[')) {
						mCODE(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {
					commit();
					try {mTARGET(false);}
					catch(RecognitionException e) {
						// catastrophic failure
						reportError(e);
						consume();
					}
					continue tryAgain;
				}
				}
				}
				commit();
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				if ( !getCommitToPath() ) {
					rewind(_m);
					resetText();
					try {mTARGET(false);}
					catch(RecognitionException ee) {
						// horrendous failure: error in filter rule
						reportError(ee);
						consume();
					}
					continue tryAgain;
				}
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mBACKQUOTE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BACKQUOTE;
		int _saveIndex;
		
		match("`");
		selector().push("bqlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTRATEGY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRATEGY;
		int _saveIndex;
		
		match("%strategy");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMATCH(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MATCH;
		int _saveIndex;
		
		match("%match");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOPERATOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OPERATOR;
		int _saveIndex;
		
		match("%op");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSUBTYPE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SUBTYPE;
		int _saveIndex;
		
		match("%subtype");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mTRANSFORMATION(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TRANSFORMATION;
		int _saveIndex;
		
		match("%transformation");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mTRACELINK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TRACELINK;
		int _saveIndex;
		
		match("%tracelink");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRESOLVE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RESOLVE;
		int _saveIndex;
		
		match("%resolve");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mTYPETERM(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TYPETERM;
		int _saveIndex;
		
		match("%typeterm");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOPERATORLIST(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OPERATORLIST;
		int _saveIndex;
		
		match("%oplist");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOPERATORARRAY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OPERATORARRAY;
		int _saveIndex;
		
		match("%oparray");
		selector().push("tomlexer");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mINCLUDE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = INCLUDE;
		int _saveIndex;
		
		match("%include");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGOM(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GOM;
		int _saveIndex;
		
		match("%gom");
		{
		if ((LA(1)=='(')) {
			{
			match('(');
			{
			_loop39:
			do {
				// nongreedy exit test
				if ((LA(1)==')') && (true)) break _loop39;
				if ((LA(1)=='\r') && (LA(2)=='\n') && ((LA(3) >= '\u0000' && LA(3) <= '\uffff')) && (true) && (true) && (true)) {
					match('\r');
					match('\n');
					newline();
				}
				else if ((LA(1)=='\r') && ((LA(2) >= '\u0000' && LA(2) <= '\uffff')) && (true) && (true) && (true) && (true)) {
					match('\r');
					newline();
				}
				else if ((_tokenSet_0.member(LA(1))) && ((LA(2) >= '\u0000' && LA(2) <= '\uffff'))) {
					{
					match(_tokenSet_0);
					}
				}
				else if ((LA(1)=='\n')) {
					match('\n');
					newline();
				}
				else {
					break _loop39;
				}
				
			} while (true);
			}
			match(')');
			}
		}
		else {
		}
		
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLBRACE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LBRACE;
		int _saveIndex;
		
		match('{');
		
		target.append(new String(text.getBuffer(),_begin,text.length()-_begin));
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRBRACE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RBRACE;
		int _saveIndex;
		
		match('}');
		
		target.append(new String(text.getBuffer(),_begin,text.length()-_begin));
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING;
		int _saveIndex;
		
		match('"');
		{
		_loop45:
		do {
			if ((LA(1)=='\\')) {
				mESC(false);
			}
			else if ((_tokenSet_1.member(LA(1)))) {
				{
				match(_tokenSet_1);
				}
			}
			else {
				break _loop45;
			}
			
		} while (true);
		}
		match('"');
		
		target.append(new String(text.getBuffer(),_begin,text.length()-_begin));
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESC;
		int _saveIndex;
		
		match('\\');
		{
		switch ( LA(1)) {
		case 'n':
		{
			match('n');
			break;
		}
		case 'r':
		{
			match('r');
			break;
		}
		case 't':
		{
			match('t');
			break;
		}
		case 'b':
		{
			match('b');
			break;
		}
		case 'f':
		{
			match('f');
			break;
		}
		case '"':
		{
			match('"');
			break;
		}
		case '\'':
		{
			match('\'');
			break;
		}
		case '\\':
		{
			match('\\');
			break;
		}
		case 'u':
		{
			{
			int _cnt49=0;
			_loop49:
			do {
				if ((LA(1)=='u')) {
					match('u');
				}
				else {
					if ( _cnt49>=1 ) { break _loop49; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt49++;
			} while (true);
			}
			mHEX_DIGIT(false);
			mHEX_DIGIT(false);
			mHEX_DIGIT(false);
			mHEX_DIGIT(false);
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		{
			matchRange('0','3');
			{
			if (((LA(1) >= '0' && LA(1) <= '7')) && (_tokenSet_0.member(LA(2))) && (true) && (true) && (true) && (true)) {
				matchRange('0','7');
				{
				if (((LA(1) >= '0' && LA(1) <= '7')) && (_tokenSet_0.member(LA(2))) && (true) && (true) && (true) && (true)) {
					matchRange('0','7');
				}
				else if ((_tokenSet_0.member(LA(1))) && (true) && (true) && (true) && (true) && (true)) {
				}
				else {
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
			}
			else if ((_tokenSet_0.member(LA(1))) && (true) && (true) && (true) && (true) && (true)) {
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			break;
		}
		case '4':  case '5':  case '6':  case '7':
		{
			matchRange('4','7');
			{
			if (((LA(1) >= '0' && LA(1) <= '7')) && (_tokenSet_0.member(LA(2))) && (true) && (true) && (true) && (true)) {
				matchRange('0','7');
			}
			else if ((_tokenSet_0.member(LA(1))) && (true) && (true) && (true) && (true) && (true)) {
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEX_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEX_DIGIT;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			matchRange('0','9');
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':
		{
			matchRange('A','F');
			break;
		}
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':
		{
			matchRange('a','f');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case ' ':
		{
			match(' ');
			break;
		}
		case '\t':
		{
			match('\t');
			break;
		}
		case '\u000c':
		{
			match('\f');
			break;
		}
		case '\n':  case '\r':
		{
			{
			if ((LA(1)=='\r') && (LA(2)=='\n')) {
				match("\r\n");
			}
			else if ((LA(1)=='\r') && (true)) {
				match('\r');
			}
			else if ((LA(1)=='\n')) {
				match('\n');
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			newline();
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		
		target.append(new String(text.getBuffer(),_begin,text.length()-_begin));
		_ttype = Token.SKIP;
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMENT;
		int _saveIndex;
		
		{
		if ((LA(1)=='/') && (LA(2)=='/')) {
			mSL_COMMENT(false);
		}
		else if ((LA(1)=='/') && (LA(2)=='*')) {
			mML_COMMENT(false);
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		_ttype = Token.SKIP;
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSL_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SL_COMMENT;
		int _saveIndex;
		
		match("//");
		{
		_loop63:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				{
				match(_tokenSet_0);
				}
			}
			else {
				break _loop63;
			}
			
		} while (true);
		}
		{
		if ((LA(1)=='\r') && (LA(2)=='\n')) {
			match('\r');
			match('\n');
		}
		else if ((LA(1)=='\r') && (true)) {
			match('\r');
		}
		else if ((LA(1)=='\n')) {
			match('\n');
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		
							if(!parser.isSkipComment()) {
		target.append(new String(text.getBuffer(),_begin,text.length()-_begin));
		}
		newline();
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mML_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ML_COMMENT;
		int _saveIndex;
		
		match("/*");
		{
		if (((LA(1)=='*') && ((LA(2) >= '\u0000' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0000' && LA(3) <= '\uffff')) && (true) && (true) && (true))&&( LA(2)!='/' )) {
			match('*');
		}
		else if (((LA(1) >= '\u0000' && LA(1) <= '\uffff')) && ((LA(2) >= '\u0000' && LA(2) <= '\uffff')) && (true) && (true) && (true) && (true)) {
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		{
		_loop69:
		do {
			// nongreedy exit test
			if ((LA(1)=='*') && (LA(2)=='/') && (true)) break _loop69;
			if ((LA(1)=='\r') && (LA(2)=='\n') && ((LA(3) >= '\u0000' && LA(3) <= '\uffff')) && ((LA(4) >= '\u0000' && LA(4) <= '\uffff')) && (true) && (true)) {
				match('\r');
				match('\n');
				newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");
			}
			else if ((LA(1)=='\r') && ((LA(2) >= '\u0000' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0000' && LA(3) <= '\uffff')) && (true) && (true) && (true)) {
				match('\r');
				newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");
			}
			else if ((_tokenSet_0.member(LA(1))) && ((LA(2) >= '\u0000' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0000' && LA(3) <= '\uffff'))) {
				{
				match(_tokenSet_0);
				}
				if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");
			}
			else if ((LA(1)=='\n')) {
				match('\n');
				newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");
			}
			else {
				break _loop69;
			}
			
		} while (true);
		}
		match("*/");
		
							if(!parser.isSkipComment()) {
								target.append(new String(text.getBuffer(),_begin,text.length()-_begin));
							}
						
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCODE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CODE;
		int _saveIndex;
		
		match('%');
		match('[');
		{
		if (((LA(1)==']') && ((LA(2) >= '\u0000' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0000' && LA(3) <= '\uffff')) && (true) && (true) && (true))&&( LA(2)!='%' )) {
			match(']');
		}
		else if (((LA(1) >= '\u0000' && LA(1) <= '\uffff')) && ((LA(2) >= '\u0000' && LA(2) <= '\uffff')) && (true) && (true) && (true) && (true)) {
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		{
		_loop74:
		do {
			// nongreedy exit test
			if ((LA(1)==']') && (LA(2)=='%') && (true)) break _loop74;
			if ((LA(1)=='\r') && (LA(2)=='\n') && ((LA(3) >= '\u0000' && LA(3) <= '\uffff')) && ((LA(4) >= '\u0000' && LA(4) <= '\uffff')) && (true) && (true)) {
				match('\r');
				match('\n');
				newline();
			}
			else if ((LA(1)=='\r') && ((LA(2) >= '\u0000' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0000' && LA(3) <= '\uffff')) && (true) && (true) && (true)) {
				match('\r');
				newline();
			}
			else if ((_tokenSet_0.member(LA(1))) && ((LA(2) >= '\u0000' && LA(2) <= '\uffff')) && ((LA(3) >= '\u0000' && LA(3) <= '\uffff'))) {
				{
				match(_tokenSet_0);
				}
			}
			else if ((LA(1)=='\n')) {
				match('\n');
				newline();
			}
			else {
				break _loop74;
			}
			
		} while (true);
		}
		match(']');
		match('%');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mTARGET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TARGET;
		int _saveIndex;
		
		{
		matchNot(EOF_CHAR);
		}
		target.append(new String(text.getBuffer(),_begin,text.length()-_begin));
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[2048];
		data[0]=-9217L;
		for (int i = 1; i<=1023; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[2048];
		data[0]=-17179878401L;
		data[1]=-268435457L;
		for (int i = 2; i<=1023; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	
	}
