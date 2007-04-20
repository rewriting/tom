// $ANTLR 2.7.7 (20060906): "ANTLRMapperGomParser.g" -> "ANTLRMapperGomParser.java"$

  /*
   * Gom
   *
   * Copyright (c) 2000-2006, INRIA
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
   * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
   *
   **/
  package tom.gom.parser;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

  import antlr.LexerSharedInputState;

public class ANTLRMapperGomParser extends antlr.LLkParser       implements ANTLRMapperGomParserTokenTypes
 {

  private LexerSharedInputState lexerstate = null;

  public ANTLRMapperGomParser(ANTLRMapperGomLexer lexer, String name) {
    this(lexer);
    /* the name attribute is used for constructor disambiguation */
    this.lexerstate = lexer.getInputState();
  }

protected ANTLRMapperGomParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public ANTLRMapperGomParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected ANTLRMapperGomParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public ANTLRMapperGomParser(TokenStream lexer) {
  this(lexer,2);
}

public ANTLRMapperGomParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void module() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST module_AST = null;
		
		AST tmp1_AST = null;
		tmp1_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp1_AST);
		match(MODULE);
		modulename();
		astFactory.addASTChild(currentAST, returnAST);
		{
		switch ( LA(1)) {
		case IMPORTS:
		{
			imports();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case PUBLIC:
		case SORTS:
		case ABSTRACT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		section();
		astFactory.addASTChild(currentAST, returnAST);
		module_AST = (AST)currentAST.root;
		returnAST = module_AST;
	}
	
	public final void modulename() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modulename_AST = null;
		Token  mod = null;
		AST mod_AST = null;
		Token  moduleName = null;
		AST moduleName_AST = null;
		
		{
		_loop5:
		do {
			if ((LA(1)==ID) && (LA(2)==DOT)) {
				mod = LT(1);
				mod_AST = astFactory.create(mod);
				astFactory.addASTChild(currentAST, mod_AST);
				match(ID);
				AST tmp2_AST = null;
				tmp2_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp2_AST);
				match(DOT);
			}
			else {
				break _loop5;
			}
			
		} while (true);
		}
		moduleName = LT(1);
		moduleName_AST = astFactory.create(moduleName);
		astFactory.addASTChild(currentAST, moduleName_AST);
		match(ID);
		modulename_AST = (AST)currentAST.root;
		returnAST = modulename_AST;
	}
	
	public final void imports() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST imports_AST = null;
		Token  importedModuleName = null;
		AST importedModuleName_AST = null;
		
		AST tmp3_AST = null;
		tmp3_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp3_AST);
		match(IMPORTS);
		{
		_loop8:
		do {
			if ((LA(1)==ID)) {
				importedModuleName = LT(1);
				importedModuleName_AST = astFactory.create(importedModuleName);
				astFactory.addASTChild(currentAST, importedModuleName_AST);
				match(ID);
			}
			else {
				break _loop8;
			}
			
		} while (true);
		}
		imports_AST = (AST)currentAST.root;
		returnAST = imports_AST;
	}
	
	public final void section() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST section_AST = null;
		
		{
		switch ( LA(1)) {
		case PUBLIC:
		{
			AST tmp4_AST = null;
			tmp4_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp4_AST);
			match(PUBLIC);
			break;
		}
		case SORTS:
		case ABSTRACT:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		grammar();
		astFactory.addASTChild(currentAST, returnAST);
		section_AST = (AST)currentAST.root;
		returnAST = section_AST;
	}
	
	public final void grammar() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST grammar_AST = null;
		
		{
		int _cnt13=0;
		_loop13:
		do {
			switch ( LA(1)) {
			case SORTS:
			{
				sortdef();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case ABSTRACT:
			{
				syntax();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				if ( _cnt13>=1 ) { break _loop13; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			}
			_cnt13++;
		} while (true);
		}
		grammar_AST = (AST)currentAST.root;
		returnAST = grammar_AST;
	}
	
	public final void sortdef() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST sortdef_AST = null;
		
		AST tmp5_AST = null;
		tmp5_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp5_AST);
		match(SORTS);
		{
		_loop16:
		do {
			if ((LA(1)==ID)) {
				type();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop16;
			}
			
		} while (true);
		}
		sortdef_AST = (AST)currentAST.root;
		returnAST = sortdef_AST;
	}
	
	public final void syntax() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST syntax_AST = null;
		
		match(ABSTRACT);
		AST tmp7_AST = null;
		tmp7_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp7_AST);
		match(SYNTAX);
		{
		_loop20:
		do {
			if ((LA(1)==ID) && (LA(2)==LEFT_BRACE)) {
				production();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_0.member(LA(1))) && (LA(2)==ID||LA(2)==COLON)) {
				hookConstruct();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==ID) && (LA(2)==EQUALS)) {
				typedecl();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop20;
			}
			
		} while (true);
		}
		syntax_AST = (AST)currentAST.root;
		returnAST = syntax_AST;
	}
	
	public final void type() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST type_AST = null;
		Token  i = null;
		AST i_AST = null;
		
		i = LT(1);
		i_AST = astFactory.create(i);
		astFactory.addASTChild(currentAST, i_AST);
		match(ID);
		type_AST = (AST)currentAST.root;
		returnAST = type_AST;
	}
	
	public final void production() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST production_AST = null;
		Token  id = null;
		AST id_AST = null;
		
		id = LT(1);
		id_AST = astFactory.create(id);
		astFactory.addASTChild(currentAST, id_AST);
		match(ID);
		fieldlist();
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp8_AST = null;
		tmp8_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp8_AST);
		match(ARROW);
		type();
		astFactory.addASTChild(currentAST, returnAST);
		production_AST = (AST)currentAST.root;
		returnAST = production_AST;
	}
	
	public final void hookConstruct() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST hookConstruct_AST = null;
		AST hookScope_AST = null;
		Token  pointCut = null;
		AST pointCut_AST = null;
		AST hook_AST = null;
		
		String code = "";
		
		
		{
		switch ( LA(1)) {
		case MODULE:
		case SORT:
		case OPERATOR:
		{
			hookScope();
			hookScope_AST = (AST)returnAST;
			break;
		}
		case ID:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		pointCut = LT(1);
		pointCut_AST = astFactory.create(pointCut);
		match(ID);
		AST tmp9_AST = null;
		tmp9_AST = astFactory.create(LT(1));
		match(COLON);
		hook();
		hook_AST = (AST)returnAST;
		hookConstruct_AST = (AST)currentAST.root;
		
		BlockParser blockparser = BlockParser.makeBlockParser(lexerstate);
		code = blockparser.block();
		
		if (hookScope_AST == null) {
		hookConstruct_AST = (AST)astFactory.make( (new ASTArray(4)).add(tmp9_AST).add(astFactory.create(OPERATOR)).add(pointCut_AST).add(hook_AST));
		} else {
		hookConstruct_AST = (AST)astFactory.make( (new ASTArray(4)).add(tmp9_AST).add(hookScope_AST).add(pointCut_AST).add(hook_AST));
		}
		hookConstruct_AST.setText(code);
		
		currentAST.root = hookConstruct_AST;
		currentAST.child = hookConstruct_AST!=null &&hookConstruct_AST.getFirstChild()!=null ?
			hookConstruct_AST.getFirstChild() : hookConstruct_AST;
		currentAST.advanceChildToEnd();
		returnAST = hookConstruct_AST;
	}
	
	public final void typedecl() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typedecl_AST = null;
		Token  id = null;
		AST id_AST = null;
		
		id = LT(1);
		id_AST = astFactory.create(id);
		astFactory.addASTChild(currentAST, id_AST);
		match(ID);
		AST tmp10_AST = null;
		tmp10_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp10_AST);
		match(EQUALS);
		alternatives();
		astFactory.addASTChild(currentAST, returnAST);
		typedecl_AST = (AST)currentAST.root;
		returnAST = typedecl_AST;
	}
	
	public final void fieldlist() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fieldlist_AST = null;
		
		match(LEFT_BRACE);
		{
		switch ( LA(1)) {
		case ID:
		{
			field();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop31:
			do {
				if ((LA(1)==COMMA)) {
					AST tmp12_AST = null;
					tmp12_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp12_AST);
					match(COMMA);
					field();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop31;
				}
				
			} while (true);
			}
			break;
		}
		case RIGHT_BRACE:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(RIGHT_BRACE);
		fieldlist_AST = (AST)currentAST.root;
		returnAST = fieldlist_AST;
	}
	
	public final void alternatives() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST alternatives_AST = null;
		Token  id = null;
		AST id_AST = null;
		Token  altid = null;
		AST altid_AST = null;
		
		{
		switch ( LA(1)) {
		case ALT:
		{
			AST tmp14_AST = null;
			tmp14_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp14_AST);
			match(ALT);
			break;
		}
		case ID:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		id = LT(1);
		id_AST = astFactory.create(id);
		astFactory.addASTChild(currentAST, id_AST);
		match(ID);
		fieldlist();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop26:
		do {
			if ((LA(1)==ALT)) {
				AST tmp15_AST = null;
				tmp15_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp15_AST);
				match(ALT);
				altid = LT(1);
				altid_AST = astFactory.create(altid);
				astFactory.addASTChild(currentAST, altid_AST);
				match(ID);
				fieldlist();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop26;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case SEMI:
		{
			AST tmp16_AST = null;
			tmp16_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp16_AST);
			match(SEMI);
			break;
		}
		case EOF:
		case MODULE:
		case ID:
		case SORTS:
		case ABSTRACT:
		case SORT:
		case OPERATOR:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		alternatives_AST = (AST)currentAST.root;
		returnAST = alternatives_AST;
	}
	
	public final void field() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST field_AST = null;
		Token  id = null;
		AST id_AST = null;
		
		if ((LA(1)==ID) && (LA(2)==STAR)) {
			type();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp17_AST = null;
			tmp17_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp17_AST);
			match(STAR);
			field_AST = (AST)currentAST.root;
		}
		else if ((LA(1)==ID) && (LA(2)==COLON)) {
			id = LT(1);
			id_AST = astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(ID);
			AST tmp18_AST = null;
			tmp18_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp18_AST);
			match(COLON);
			type();
			astFactory.addASTChild(currentAST, returnAST);
			field_AST = (AST)currentAST.root;
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		returnAST = field_AST;
	}
	
	public final void arglist() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST arglist_AST = null;
		Token  arg = null;
		AST arg_AST = null;
		Token  supplarg = null;
		AST supplarg_AST = null;
		
		{
		switch ( LA(1)) {
		case LEFT_BRACE:
		{
			match(LEFT_BRACE);
			{
			switch ( LA(1)) {
			case ID:
			{
				arg = LT(1);
				arg_AST = astFactory.create(arg);
				astFactory.addASTChild(currentAST, arg_AST);
				match(ID);
				{
				_loop36:
				do {
					if ((LA(1)==COMMA)) {
						AST tmp20_AST = null;
						tmp20_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp20_AST);
						match(COMMA);
						supplarg = LT(1);
						supplarg_AST = astFactory.create(supplarg);
						astFactory.addASTChild(currentAST, supplarg_AST);
						match(ID);
					}
					else {
						break _loop36;
					}
					
				} while (true);
				}
				break;
			}
			case RIGHT_BRACE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RIGHT_BRACE);
			break;
		}
		case EOF:
		case MODULE:
		case ID:
		case SORTS:
		case ABSTRACT:
		case SORT:
		case OPERATOR:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		arglist_AST = (AST)currentAST.root;
		returnAST = arglist_AST;
	}
	
	public final void hookScope() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST hookScope_AST = null;
		
		switch ( LA(1)) {
		case SORT:
		{
			AST tmp22_AST = null;
			tmp22_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp22_AST);
			match(SORT);
			hookScope_AST = (AST)currentAST.root;
			break;
		}
		case MODULE:
		{
			AST tmp23_AST = null;
			tmp23_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp23_AST);
			match(MODULE);
			hookScope_AST = (AST)currentAST.root;
			break;
		}
		case OPERATOR:
		{
			AST tmp24_AST = null;
			tmp24_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp24_AST);
			match(OPERATOR);
			hookScope_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = hookScope_AST;
	}
	
	public final void hook() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST hook_AST = null;
		Token  hookType = null;
		AST hookType_AST = null;
		
		hookType = LT(1);
		hookType_AST = astFactory.create(hookType);
		astFactory.addASTChild(currentAST, hookType_AST);
		match(ID);
		arglist();
		astFactory.addASTChild(currentAST, returnAST);
		hook_AST = (AST)currentAST.root;
		returnAST = hook_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"module\"",
		"ID",
		"DOT",
		"\"imports\"",
		"\"public\"",
		"\"sorts\"",
		"\"abstract\"",
		"\"syntax\"",
		"ARROW",
		"EQUALS",
		"ALT",
		"SEMI",
		"LEFT_BRACE",
		"COMMA",
		"RIGHT_BRACE",
		"COLON",
		"\"sort\"",
		"\"operator\"",
		"STAR",
		"\"private\"",
		"\"import\"",
		"LBRACE",
		"RBRACE",
		"WS",
		"SLCOMMENT",
		"ML_COMMENT"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 3145776L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	
	}
