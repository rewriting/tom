// $ANTLR 2.7.6 (20060516): "ANTLRMapperGomParser.g" -> "ANTLRMapperGomParser.java"$

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
			else if ((LA(1)==ID||LA(1)==OPERATOR) && (LA(2)==ID||LA(2)==COLON)) {
				hookOperator();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==MODULE||LA(1)==SORT)) {
				hookSortModule();
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
	
	public final void hookOperator() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST hookOperator_AST = null;
		Token  typeId = null;
		AST typeId_AST = null;
		Token  id = null;
		AST id_AST = null;
		AST hook_AST = null;
		
		String code = "";
		
		
		{
		switch ( LA(1)) {
		case OPERATOR:
		{
			typeId = LT(1);
			typeId_AST = astFactory.create(typeId);
			match(OPERATOR);
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
		match(ID);
		AST tmp9_AST = null;
		tmp9_AST = astFactory.create(LT(1));
		match(COLON);
		hook();
		hook_AST = (AST)returnAST;
		hookOperator_AST = (AST)currentAST.root;
		
		BlockParser blockparser = BlockParser.makeBlockParser(lexerstate);
		code = blockparser.block();
		
		hookOperator_AST = (AST)astFactory.make( (new ASTArray(3)).add(tmp9_AST).add(id_AST).add(hook_AST));
		hookOperator_AST.setText(code);
		
		currentAST.root = hookOperator_AST;
		currentAST.child = hookOperator_AST!=null &&hookOperator_AST.getFirstChild()!=null ?
			hookOperator_AST.getFirstChild() : hookOperator_AST;
		currentAST.advanceChildToEnd();
		returnAST = hookOperator_AST;
	}
	
	public final void hookSortModule() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST hookSortModule_AST = null;
		AST typeId_AST = null;
		Token  id = null;
		AST id_AST = null;
		AST hook_AST = null;
		
		String code = "";
		
		
		typeId();
		typeId_AST = (AST)returnAST;
		id = LT(1);
		id_AST = astFactory.create(id);
		match(ID);
		AST tmp10_AST = null;
		tmp10_AST = astFactory.create(LT(1));
		match(COLON);
		otherHook();
		hook_AST = (AST)returnAST;
		hookSortModule_AST = (AST)currentAST.root;
		
		BlockParser blockparser = BlockParser.makeBlockParser(lexerstate);
		code = blockparser.block();
		
		hookSortModule_AST = (AST)astFactory.make( (new ASTArray(4)).add(tmp10_AST).add(typeId_AST).add(id_AST).add(hook_AST));
		hookSortModule_AST.setText(code);
		
		currentAST.root = hookSortModule_AST;
		currentAST.child = hookSortModule_AST!=null &&hookSortModule_AST.getFirstChild()!=null ?
			hookSortModule_AST.getFirstChild() : hookSortModule_AST;
		currentAST.advanceChildToEnd();
		returnAST = hookSortModule_AST;
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
		AST tmp11_AST = null;
		tmp11_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp11_AST);
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
					AST tmp13_AST = null;
					tmp13_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp13_AST);
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
			AST tmp15_AST = null;
			tmp15_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp15_AST);
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
				AST tmp16_AST = null;
				tmp16_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp16_AST);
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
			AST tmp17_AST = null;
			tmp17_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp17_AST);
			match(SEMI);
			break;
		}
		case EOF:
		case MODULE:
		case ID:
		case SORTS:
		case ABSTRACT:
		case OPERATOR:
		case SORT:
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
			AST tmp18_AST = null;
			tmp18_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp18_AST);
			match(STAR);
			field_AST = (AST)currentAST.root;
		}
		else if ((LA(1)==ID) && (LA(2)==COLON)) {
			id = LT(1);
			id_AST = astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(ID);
			AST tmp19_AST = null;
			tmp19_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp19_AST);
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
			_loop35:
			do {
				if ((LA(1)==COMMA)) {
					AST tmp21_AST = null;
					tmp21_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp21_AST);
					match(COMMA);
					supplarg = LT(1);
					supplarg_AST = astFactory.create(supplarg);
					astFactory.addASTChild(currentAST, supplarg_AST);
					match(ID);
				}
				else {
					break _loop35;
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
		arglist_AST = (AST)currentAST.root;
		returnAST = arglist_AST;
	}
	
	public final void hook() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST hook_AST = null;
		
		switch ( LA(1)) {
		case MAKE:
		case MAKEINSERT:
		{
			makeHook();
			astFactory.addASTChild(currentAST, returnAST);
			hook_AST = (AST)currentAST.root;
			break;
		}
		case BLOCK:
		case INTERFACE:
		case IMPORT:
		{
			otherHook();
			astFactory.addASTChild(currentAST, returnAST);
			hook_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = hook_AST;
	}
	
	public final void makeHook() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST makeHook_AST = null;
		
		{
		switch ( LA(1)) {
		case MAKE:
		{
			AST tmp23_AST = null;
			tmp23_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp23_AST);
			match(MAKE);
			break;
		}
		case MAKEINSERT:
		{
			AST tmp24_AST = null;
			tmp24_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp24_AST);
			match(MAKEINSERT);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		arglist();
		astFactory.addASTChild(currentAST, returnAST);
		makeHook_AST = (AST)currentAST.root;
		returnAST = makeHook_AST;
	}
	
	public final void otherHook() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST otherHook_AST = null;
		
		switch ( LA(1)) {
		case BLOCK:
		{
			AST tmp25_AST = null;
			tmp25_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp25_AST);
			match(BLOCK);
			otherHook_AST = (AST)currentAST.root;
			break;
		}
		case INTERFACE:
		{
			AST tmp26_AST = null;
			tmp26_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp26_AST);
			match(INTERFACE);
			otherHook_AST = (AST)currentAST.root;
			break;
		}
		case IMPORT:
		{
			AST tmp27_AST = null;
			tmp27_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp27_AST);
			match(IMPORT);
			otherHook_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = otherHook_AST;
	}
	
	public final void typeId() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeId_AST = null;
		
		switch ( LA(1)) {
		case SORT:
		{
			AST tmp28_AST = null;
			tmp28_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp28_AST);
			match(SORT);
			typeId_AST = (AST)currentAST.root;
			break;
		}
		case MODULE:
		{
			AST tmp29_AST = null;
			tmp29_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp29_AST);
			match(MODULE);
			typeId_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeId_AST;
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
		"\"operator\"",
		"COLON",
		"\"make\"",
		"\"make_insert\"",
		"\"block\"",
		"\"interface\"",
		"\"import\"",
		"\"sort\"",
		"STAR",
		"\"private\"",
		"LBRACE",
		"RBRACE",
		"WS",
		"SLCOMMENT",
		"ML_COMMENT"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	
	}
