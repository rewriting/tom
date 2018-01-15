// Generated from /Users/pem/github/tom/src/tom/engine/parser/tomjava/TomJavaParser.g4 by ANTLR 4.5.3
package tom.engine.parser.tomjava;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TomJavaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ABSTRACT=1, ASSERT=2, BOOLEAN=3, BREAK=4, BYTE=5, CASE=6, CATCH=7, CHAR=8, 
		CLASS=9, CONST=10, CONTINUE=11, DEFAULT=12, DO=13, DOUBLE=14, ELSE=15, 
		ENUM=16, EXTENDS=17, FINAL=18, FINALLY=19, FLOAT=20, FOR=21, IF=22, GOTO=23, 
		IMPLEMENTS=24, IMPORT=25, INSTANCEOF=26, INT=27, INTERFACE=28, LONG=29, 
		NATIVE=30, NEW=31, PACKAGE=32, PRIVATE=33, PROTECTED=34, PUBLIC=35, RETURN=36, 
		SHORT=37, STATIC=38, STRICTFP=39, SUPER=40, SWITCH=41, SYNCHRONIZED=42, 
		THIS=43, THROW=44, THROWS=45, TRANSIENT=46, TRY=47, VOID=48, VOLATILE=49, 
		WHILE=50, MATCH=51, STRATEGY=52, INCLUDE=53, GOM=54, OP=55, OPARRAY=56, 
		OPLIST=57, TYPETERM=58, RULE=59, VISIT=60, IS_FSYM=61, IS_SORT=62, MAKE=63, 
		MAKE_EMPTY=64, MAKE_APPEND=65, MAKE_INSERT=66, GET_SLOT=67, GET_DEFAULT=68, 
		GET_ELEMENT=69, GET_HEAD=70, GET_TAIL=71, GET_SIZE=72, IS_EMPTY=73, IMPLEMENT=74, 
		EQUALS=75, WHEN=76, DECIMAL_LITERAL=77, HEX_LITERAL=78, OCT_LITERAL=79, 
		BINARY_LITERAL=80, FLOAT_LITERAL=81, HEX_FLOAT_LITERAL=82, BOOL_LITERAL=83, 
		CHAR_LITERAL=84, EXTENDED_CHAR_LITERAL=85, STRING_LITERAL=86, NULL_LITERAL=87, 
		LPAREN=88, RPAREN=89, LBRACE=90, RBRACE=91, LBRACK=92, RBRACK=93, SEMI=94, 
		COMMA=95, DOT=96, ASSIGN=97, GT=98, LT=99, TILDE=100, COLON=101, EQUAL=102, 
		LE=103, GE=104, NOTEQUAL=105, AND=106, OR=107, INC=108, DEC=109, ADD=110, 
		SUB=111, BITAND=112, CARET=113, MOD=114, ADD_ASSIGN=115, SUB_ASSIGN=116, 
		MUL_ASSIGN=117, DIV_ASSIGN=118, AND_ASSIGN=119, OR_ASSIGN=120, XOR_ASSIGN=121, 
		MOD_ASSIGN=122, LSHIFT_ASSIGN=123, RSHIFT_ASSIGN=124, URSHIFT_ASSIGN=125, 
		STAR=126, UNDERSCORE=127, ANTI=128, PIPE=129, QMARK=130, DQMARK=131, SLASH=132, 
		BACKSLASH=133, BQUOTE=134, METAQUOTE=135, ARROW=136, COLONCOLON=137, AT=138, 
		ELLIPSIS=139, WS=140, COMMENT=141, LINE_COMMENT=142, IDENTIFIER=143, BLOCKSTART=144, 
		OPTIONSTART=145, OPTIONEND=146, DMINUSID=147, UNKNOWNBLOCK_WS=148, UNKNOWNBLOCK_COMMENT=149, 
		UNKNOWNBLOCK_LINE_COMMENT=150, SUBBLOCKSTART=151, BLOCKEND=152, INSIDE_WS=153, 
		INSIDE_COMMENT=154, INSIDE_LINE_COMMENT=155, ANY=156, SUBSUBBLOCKSTART=157, 
		SUBBLOCKEND=158, SUB_WS=159, SUB_COMMENT=160, SUB_LINE_COMMENT=161, SUB_ANY=162;
	public static final int
		RULE_compilationUnit = 0, RULE_declarationsUnit = 1, RULE_expressionUnit = 2, 
		RULE_packageDeclaration = 3, RULE_importDeclaration = 4, RULE_typeDeclaration = 5, 
		RULE_modifier = 6, RULE_classOrInterfaceModifier = 7, RULE_variableModifier = 8, 
		RULE_classDeclaration = 9, RULE_typeParameters = 10, RULE_typeParameter = 11, 
		RULE_typeBound = 12, RULE_enumDeclaration = 13, RULE_enumConstants = 14, 
		RULE_enumConstant = 15, RULE_enumBodyDeclarations = 16, RULE_interfaceDeclaration = 17, 
		RULE_classBody = 18, RULE_interfaceBody = 19, RULE_classBodyDeclaration = 20, 
		RULE_memberDeclaration = 21, RULE_methodDeclaration = 22, RULE_methodBody = 23, 
		RULE_typeTypeOrVoid = 24, RULE_genericMethodDeclaration = 25, RULE_genericConstructorDeclaration = 26, 
		RULE_constructorDeclaration = 27, RULE_fieldDeclaration = 28, RULE_interfaceBodyDeclaration = 29, 
		RULE_interfaceMemberDeclaration = 30, RULE_constDeclaration = 31, RULE_constantDeclarator = 32, 
		RULE_interfaceMethodDeclaration = 33, RULE_interfaceMethodModifier = 34, 
		RULE_genericInterfaceMethodDeclaration = 35, RULE_variableDeclarators = 36, 
		RULE_variableDeclarator = 37, RULE_variableDeclaratorId = 38, RULE_variableInitializer = 39, 
		RULE_arrayInitializer = 40, RULE_classOrInterfaceType = 41, RULE_typeArgument = 42, 
		RULE_qualifiedNameList = 43, RULE_formalParameters = 44, RULE_formalParameterList = 45, 
		RULE_formalParameter = 46, RULE_lastFormalParameter = 47, RULE_qualifiedName = 48, 
		RULE_literal = 49, RULE_integerLiteral = 50, RULE_floatLiteral = 51, RULE_annotation = 52, 
		RULE_elementValuePairs = 53, RULE_elementValuePair = 54, RULE_elementValue = 55, 
		RULE_elementValueArrayInitializer = 56, RULE_annotationTypeDeclaration = 57, 
		RULE_annotationTypeBody = 58, RULE_annotationTypeElementDeclaration = 59, 
		RULE_annotationTypeElementRest = 60, RULE_annotationMethodOrConstantRest = 61, 
		RULE_annotationMethodRest = 62, RULE_annotationConstantRest = 63, RULE_defaultValue = 64, 
		RULE_block = 65, RULE_blockStatement = 66, RULE_localVariableDeclaration = 67, 
		RULE_localTypeDeclaration = 68, RULE_statement = 69, RULE_catchClause = 70, 
		RULE_catchType = 71, RULE_finallyBlock = 72, RULE_resourceSpecification = 73, 
		RULE_resources = 74, RULE_resource = 75, RULE_switchBlockStatementGroup = 76, 
		RULE_switchLabel = 77, RULE_forControl = 78, RULE_forInit = 79, RULE_enhancedForControl = 80, 
		RULE_parExpression = 81, RULE_expressionList = 82, RULE_expression = 83, 
		RULE_funTerm = 84, RULE_lambdaExpression = 85, RULE_lambdaParameters = 86, 
		RULE_lambdaBody = 87, RULE_primary = 88, RULE_classType = 89, RULE_creator = 90, 
		RULE_createdName = 91, RULE_innerCreator = 92, RULE_arrayCreatorRest = 93, 
		RULE_classCreatorRest = 94, RULE_explicitGenericInvocation = 95, RULE_typeArgumentsOrDiamond = 96, 
		RULE_nonWildcardTypeArgumentsOrDiamond = 97, RULE_nonWildcardTypeArguments = 98, 
		RULE_typeList = 99, RULE_typeType = 100, RULE_primitiveType = 101, RULE_typeArguments = 102, 
		RULE_superSuffix = 103, RULE_explicitGenericInvocationSuffix = 104, RULE_arguments = 105, 
		RULE_javaIdentifier = 106, RULE_tomDeclaration = 107, RULE_tomStatement = 108, 
		RULE_tomTerm = 109, RULE_metaquote = 110, RULE_matchStatement = 111, RULE_strategyStatement = 112, 
		RULE_includeStatement = 113, RULE_gomStatement = 114, RULE_ruleStatement = 115, 
		RULE_unknownBlock = 116, RULE_gomOptions = 117, RULE_visit = 118, RULE_actionRule = 119, 
		RULE_tomBlock = 120, RULE_slotList = 121, RULE_slot = 122, RULE_patternlist = 123, 
		RULE_constraint = 124, RULE_term = 125, RULE_bqterm = 126, RULE_pairSlotBqterm = 127, 
		RULE_bqcomposite = 128, RULE_composite = 129, RULE_pattern = 130, RULE_fsymbol = 131, 
		RULE_headSymbol = 132, RULE_constant = 133, RULE_explicitArgs = 134, RULE_implicitArgs = 135, 
		RULE_typeterm = 136, RULE_operator = 137, RULE_oplist = 138, RULE_oparray = 139, 
		RULE_termBlock = 140, RULE_implement = 141, RULE_equalsTerm = 142, RULE_isSort = 143, 
		RULE_isFsym = 144, RULE_make = 145, RULE_makeEmptyList = 146, RULE_makeEmptyArray = 147, 
		RULE_makeAppendArray = 148, RULE_makeInsertList = 149, RULE_getSlot = 150, 
		RULE_getHead = 151, RULE_getTail = 152, RULE_getElement = 153, RULE_isEmptyList = 154, 
		RULE_getSize = 155, RULE_getDefault = 156, RULE_tomIdentifier = 157;
	public static final String[] ruleNames = {
		"compilationUnit", "declarationsUnit", "expressionUnit", "packageDeclaration", 
		"importDeclaration", "typeDeclaration", "modifier", "classOrInterfaceModifier", 
		"variableModifier", "classDeclaration", "typeParameters", "typeParameter", 
		"typeBound", "enumDeclaration", "enumConstants", "enumConstant", "enumBodyDeclarations", 
		"interfaceDeclaration", "classBody", "interfaceBody", "classBodyDeclaration", 
		"memberDeclaration", "methodDeclaration", "methodBody", "typeTypeOrVoid", 
		"genericMethodDeclaration", "genericConstructorDeclaration", "constructorDeclaration", 
		"fieldDeclaration", "interfaceBodyDeclaration", "interfaceMemberDeclaration", 
		"constDeclaration", "constantDeclarator", "interfaceMethodDeclaration", 
		"interfaceMethodModifier", "genericInterfaceMethodDeclaration", "variableDeclarators", 
		"variableDeclarator", "variableDeclaratorId", "variableInitializer", "arrayInitializer", 
		"classOrInterfaceType", "typeArgument", "qualifiedNameList", "formalParameters", 
		"formalParameterList", "formalParameter", "lastFormalParameter", "qualifiedName", 
		"literal", "integerLiteral", "floatLiteral", "annotation", "elementValuePairs", 
		"elementValuePair", "elementValue", "elementValueArrayInitializer", "annotationTypeDeclaration", 
		"annotationTypeBody", "annotationTypeElementDeclaration", "annotationTypeElementRest", 
		"annotationMethodOrConstantRest", "annotationMethodRest", "annotationConstantRest", 
		"defaultValue", "block", "blockStatement", "localVariableDeclaration", 
		"localTypeDeclaration", "statement", "catchClause", "catchType", "finallyBlock", 
		"resourceSpecification", "resources", "resource", "switchBlockStatementGroup", 
		"switchLabel", "forControl", "forInit", "enhancedForControl", "parExpression", 
		"expressionList", "expression", "funTerm", "lambdaExpression", "lambdaParameters", 
		"lambdaBody", "primary", "classType", "creator", "createdName", "innerCreator", 
		"arrayCreatorRest", "classCreatorRest", "explicitGenericInvocation", "typeArgumentsOrDiamond", 
		"nonWildcardTypeArgumentsOrDiamond", "nonWildcardTypeArguments", "typeList", 
		"typeType", "primitiveType", "typeArguments", "superSuffix", "explicitGenericInvocationSuffix", 
		"arguments", "javaIdentifier", "tomDeclaration", "tomStatement", "tomTerm", 
		"metaquote", "matchStatement", "strategyStatement", "includeStatement", 
		"gomStatement", "ruleStatement", "unknownBlock", "gomOptions", "visit", 
		"actionRule", "tomBlock", "slotList", "slot", "patternlist", "constraint", 
		"term", "bqterm", "pairSlotBqterm", "bqcomposite", "composite", "pattern", 
		"fsymbol", "headSymbol", "constant", "explicitArgs", "implicitArgs", "typeterm", 
		"operator", "oplist", "oparray", "termBlock", "implement", "equalsTerm", 
		"isSort", "isFsym", "make", "makeEmptyList", "makeEmptyArray", "makeAppendArray", 
		"makeInsertList", "getSlot", "getHead", "getTail", "getElement", "isEmptyList", 
		"getSize", "getDefault", "tomIdentifier"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'abstract'", "'assert'", "'boolean'", "'break'", "'byte'", "'case'", 
		"'catch'", "'char'", "'class'", "'const'", "'continue'", "'default'", 
		"'do'", "'double'", "'else'", "'enum'", "'extends'", "'final'", "'finally'", 
		"'float'", "'for'", "'if'", "'goto'", "'implements'", "'import'", "'instanceof'", 
		"'int'", "'interface'", "'long'", "'native'", "'new'", "'package'", "'private'", 
		"'protected'", "'public'", "'return'", "'short'", "'static'", "'strictfp'", 
		"'super'", "'switch'", "'synchronized'", "'this'", "'throw'", "'throws'", 
		"'transient'", "'try'", "'void'", "'volatile'", "'while'", "'%match'", 
		"'%strategy'", "'%include'", "'%gom'", "'%op'", "'%oparray'", "'%oplist'", 
		"'%typeterm'", "'%rule'", "'visit'", "'is_fsym'", "'is_sort'", "'make'", 
		"'make_empty'", "'make_append'", "'make_insert'", "'get_slot'", "'get_default'", 
		"'get_element'", "'get_head'", "'get_tail'", "'get_size'", "'is_empty'", 
		"'implement'", "'equals'", "'when'", null, null, null, null, null, null, 
		null, null, null, null, "'null'", null, null, null, null, "'['", "']'", 
		"';'", "','", "'.'", "'='", "'>'", "'<'", "'~'", "':'", "'=='", "'<='", 
		"'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", "'+'", "'-'", "'&'", "'^'", 
		"'%'", "'+='", "'-='", "'*='", "'/='", "'&='", "'|='", "'^='", "'%='", 
		"'<<='", "'>>='", "'>>>='", "'*'", "'_'", "'!'", "'|'", "'?'", "'??'", 
		"'/'", "'\\'", "'`'", null, "'->'", "'::'", "'@'", "'...'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE", "CASE", "CATCH", 
		"CHAR", "CLASS", "CONST", "CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE", 
		"ENUM", "EXTENDS", "FINAL", "FINALLY", "FLOAT", "FOR", "IF", "GOTO", "IMPLEMENTS", 
		"IMPORT", "INSTANCEOF", "INT", "INTERFACE", "LONG", "NATIVE", "NEW", "PACKAGE", 
		"PRIVATE", "PROTECTED", "PUBLIC", "RETURN", "SHORT", "STATIC", "STRICTFP", 
		"SUPER", "SWITCH", "SYNCHRONIZED", "THIS", "THROW", "THROWS", "TRANSIENT", 
		"TRY", "VOID", "VOLATILE", "WHILE", "MATCH", "STRATEGY", "INCLUDE", "GOM", 
		"OP", "OPARRAY", "OPLIST", "TYPETERM", "RULE", "VISIT", "IS_FSYM", "IS_SORT", 
		"MAKE", "MAKE_EMPTY", "MAKE_APPEND", "MAKE_INSERT", "GET_SLOT", "GET_DEFAULT", 
		"GET_ELEMENT", "GET_HEAD", "GET_TAIL", "GET_SIZE", "IS_EMPTY", "IMPLEMENT", 
		"EQUALS", "WHEN", "DECIMAL_LITERAL", "HEX_LITERAL", "OCT_LITERAL", "BINARY_LITERAL", 
		"FLOAT_LITERAL", "HEX_FLOAT_LITERAL", "BOOL_LITERAL", "CHAR_LITERAL", 
		"EXTENDED_CHAR_LITERAL", "STRING_LITERAL", "NULL_LITERAL", "LPAREN", "RPAREN", 
		"LBRACE", "RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", "DOT", "ASSIGN", 
		"GT", "LT", "TILDE", "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND", 
		"OR", "INC", "DEC", "ADD", "SUB", "BITAND", "CARET", "MOD", "ADD_ASSIGN", 
		"SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", 
		"MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "STAR", 
		"UNDERSCORE", "ANTI", "PIPE", "QMARK", "DQMARK", "SLASH", "BACKSLASH", 
		"BQUOTE", "METAQUOTE", "ARROW", "COLONCOLON", "AT", "ELLIPSIS", "WS", 
		"COMMENT", "LINE_COMMENT", "IDENTIFIER", "BLOCKSTART", "OPTIONSTART", 
		"OPTIONEND", "DMINUSID", "UNKNOWNBLOCK_WS", "UNKNOWNBLOCK_COMMENT", "UNKNOWNBLOCK_LINE_COMMENT", 
		"SUBBLOCKSTART", "BLOCKEND", "INSIDE_WS", "INSIDE_COMMENT", "INSIDE_LINE_COMMENT", 
		"ANY", "SUBSUBBLOCKSTART", "SUBBLOCKEND", "SUB_WS", "SUB_COMMENT", "SUB_LINE_COMMENT", 
		"SUB_ANY"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "TomJavaParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TomJavaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class CompilationUnitContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(TomJavaParser.EOF, 0); }
		public PackageDeclarationContext packageDeclaration() {
			return getRuleContext(PackageDeclarationContext.class,0);
		}
		public List<ImportDeclarationContext> importDeclaration() {
			return getRuleContexts(ImportDeclarationContext.class);
		}
		public ImportDeclarationContext importDeclaration(int i) {
			return getRuleContext(ImportDeclarationContext.class,i);
		}
		public List<TypeDeclarationContext> typeDeclaration() {
			return getRuleContexts(TypeDeclarationContext.class);
		}
		public TypeDeclarationContext typeDeclaration(int i) {
			return getRuleContext(TypeDeclarationContext.class,i);
		}
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterCompilationUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitCompilationUnit(this);
		}
	}

	public final CompilationUnitContext compilationUnit() throws RecognitionException {
		CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_compilationUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(316);
				packageDeclaration();
				}
				break;
			}
			setState(322);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(319);
				importDeclaration();
				}
				}
				setState(324);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << CLASS) | (1L << ENUM) | (1L << FINAL) | (1L << INTERFACE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << STATIC) | (1L << STRICTFP))) != 0) || _la==SEMI || _la==AT) {
				{
				{
				setState(325);
				typeDeclaration();
				}
				}
				setState(330);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(331);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationsUnitContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(TomJavaParser.EOF, 0); }
		public List<ClassBodyDeclarationContext> classBodyDeclaration() {
			return getRuleContexts(ClassBodyDeclarationContext.class);
		}
		public ClassBodyDeclarationContext classBodyDeclaration(int i) {
			return getRuleContext(ClassBodyDeclarationContext.class,i);
		}
		public DeclarationsUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarationsUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterDeclarationsUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitDeclarationsUnit(this);
		}
	}

	public final DeclarationsUnitContext declarationsUnit() throws RecognitionException {
		DeclarationsUnitContext _localctx = new DeclarationsUnitContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declarationsUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(336);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(333);
				classBodyDeclaration();
				}
				}
				setState(338);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(339);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionUnitContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(TomJavaParser.EOF, 0); }
		public ExpressionUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterExpressionUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitExpressionUnit(this);
		}
	}

	public final ExpressionUnitContext expressionUnit() throws RecognitionException {
		ExpressionUnitContext _localctx = new ExpressionUnitContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_expressionUnit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(341);
			expression(0);
			setState(342);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PackageDeclarationContext extends ParserRuleContext {
		public TerminalNode PACKAGE() { return getToken(TomJavaParser.PACKAGE, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public PackageDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPackageDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPackageDeclaration(this);
		}
	}

	public final PackageDeclarationContext packageDeclaration() throws RecognitionException {
		PackageDeclarationContext _localctx = new PackageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(344);
				annotation();
				}
				}
				setState(349);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(350);
			match(PACKAGE);
			setState(351);
			qualifiedName();
			setState(352);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportDeclarationContext extends ParserRuleContext {
		public TerminalNode IMPORT() { return getToken(TomJavaParser.IMPORT, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode STATIC() { return getToken(TomJavaParser.STATIC, 0); }
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterImportDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitImportDeclaration(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(354);
			match(IMPORT);
			setState(356);
			_la = _input.LA(1);
			if (_la==STATIC) {
				{
				setState(355);
				match(STATIC);
				}
			}

			setState(358);
			qualifiedName();
			setState(361);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(359);
				match(DOT);
				setState(360);
				match(STAR);
				}
			}

			setState(363);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeDeclarationContext extends ParserRuleContext {
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public List<ClassOrInterfaceModifierContext> classOrInterfaceModifier() {
			return getRuleContexts(ClassOrInterfaceModifierContext.class);
		}
		public ClassOrInterfaceModifierContext classOrInterfaceModifier(int i) {
			return getRuleContext(ClassOrInterfaceModifierContext.class,i);
		}
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeDeclaration(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_typeDeclaration);
		try {
			int _alt;
			setState(378);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case CLASS:
			case ENUM:
			case FINAL:
			case INTERFACE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case STATIC:
			case STRICTFP:
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(368);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(365);
						classOrInterfaceModifier();
						}
						} 
					}
					setState(370);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				}
				setState(375);
				switch (_input.LA(1)) {
				case CLASS:
					{
					setState(371);
					classDeclaration();
					}
					break;
				case ENUM:
					{
					setState(372);
					enumDeclaration();
					}
					break;
				case INTERFACE:
					{
					setState(373);
					interfaceDeclaration();
					}
					break;
				case AT:
					{
					setState(374);
					annotationTypeDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(377);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModifierContext extends ParserRuleContext {
		public ClassOrInterfaceModifierContext classOrInterfaceModifier() {
			return getRuleContext(ClassOrInterfaceModifierContext.class,0);
		}
		public TerminalNode NATIVE() { return getToken(TomJavaParser.NATIVE, 0); }
		public TerminalNode SYNCHRONIZED() { return getToken(TomJavaParser.SYNCHRONIZED, 0); }
		public TerminalNode TRANSIENT() { return getToken(TomJavaParser.TRANSIENT, 0); }
		public TerminalNode VOLATILE() { return getToken(TomJavaParser.VOLATILE, 0); }
		public ModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitModifier(this);
		}
	}

	public final ModifierContext modifier() throws RecognitionException {
		ModifierContext _localctx = new ModifierContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_modifier);
		try {
			setState(385);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case FINAL:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case STATIC:
			case STRICTFP:
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(380);
				classOrInterfaceModifier();
				}
				break;
			case NATIVE:
				enterOuterAlt(_localctx, 2);
				{
				setState(381);
				match(NATIVE);
				}
				break;
			case SYNCHRONIZED:
				enterOuterAlt(_localctx, 3);
				{
				setState(382);
				match(SYNCHRONIZED);
				}
				break;
			case TRANSIENT:
				enterOuterAlt(_localctx, 4);
				{
				setState(383);
				match(TRANSIENT);
				}
				break;
			case VOLATILE:
				enterOuterAlt(_localctx, 5);
				{
				setState(384);
				match(VOLATILE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassOrInterfaceModifierContext extends ParserRuleContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public TerminalNode PUBLIC() { return getToken(TomJavaParser.PUBLIC, 0); }
		public TerminalNode PROTECTED() { return getToken(TomJavaParser.PROTECTED, 0); }
		public TerminalNode PRIVATE() { return getToken(TomJavaParser.PRIVATE, 0); }
		public TerminalNode STATIC() { return getToken(TomJavaParser.STATIC, 0); }
		public TerminalNode ABSTRACT() { return getToken(TomJavaParser.ABSTRACT, 0); }
		public TerminalNode FINAL() { return getToken(TomJavaParser.FINAL, 0); }
		public TerminalNode STRICTFP() { return getToken(TomJavaParser.STRICTFP, 0); }
		public ClassOrInterfaceModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterClassOrInterfaceModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitClassOrInterfaceModifier(this);
		}
	}

	public final ClassOrInterfaceModifierContext classOrInterfaceModifier() throws RecognitionException {
		ClassOrInterfaceModifierContext _localctx = new ClassOrInterfaceModifierContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_classOrInterfaceModifier);
		try {
			setState(395);
			switch (_input.LA(1)) {
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(387);
				annotation();
				}
				break;
			case PUBLIC:
				enterOuterAlt(_localctx, 2);
				{
				setState(388);
				match(PUBLIC);
				}
				break;
			case PROTECTED:
				enterOuterAlt(_localctx, 3);
				{
				setState(389);
				match(PROTECTED);
				}
				break;
			case PRIVATE:
				enterOuterAlt(_localctx, 4);
				{
				setState(390);
				match(PRIVATE);
				}
				break;
			case STATIC:
				enterOuterAlt(_localctx, 5);
				{
				setState(391);
				match(STATIC);
				}
				break;
			case ABSTRACT:
				enterOuterAlt(_localctx, 6);
				{
				setState(392);
				match(ABSTRACT);
				}
				break;
			case FINAL:
				enterOuterAlt(_localctx, 7);
				{
				setState(393);
				match(FINAL);
				}
				break;
			case STRICTFP:
				enterOuterAlt(_localctx, 8);
				{
				setState(394);
				match(STRICTFP);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableModifierContext extends ParserRuleContext {
		public TerminalNode FINAL() { return getToken(TomJavaParser.FINAL, 0); }
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public VariableModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterVariableModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitVariableModifier(this);
		}
	}

	public final VariableModifierContext variableModifier() throws RecognitionException {
		VariableModifierContext _localctx = new VariableModifierContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_variableModifier);
		try {
			setState(399);
			switch (_input.LA(1)) {
			case FINAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(397);
				match(FINAL);
				}
				break;
			case AT:
				enterOuterAlt(_localctx, 2);
				{
				setState(398);
				annotation();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclarationContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(TomJavaParser.CLASS, 0); }
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public TerminalNode EXTENDS() { return getToken(TomJavaParser.EXTENDS, 0); }
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode IMPLEMENTS() { return getToken(TomJavaParser.IMPLEMENTS, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterClassDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitClassDeclaration(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_classDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(CLASS);
			setState(402);
			javaIdentifier();
			setState(404);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(403);
				typeParameters();
				}
			}

			setState(408);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(406);
				match(EXTENDS);
				setState(407);
				typeType();
				}
			}

			setState(412);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(410);
				match(IMPLEMENTS);
				setState(411);
				typeList();
				}
			}

			setState(414);
			classBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeParametersContext extends ParserRuleContext {
		public List<TypeParameterContext> typeParameter() {
			return getRuleContexts(TypeParameterContext.class);
		}
		public TypeParameterContext typeParameter(int i) {
			return getRuleContext(TypeParameterContext.class,i);
		}
		public TypeParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeParameters(this);
		}
	}

	public final TypeParametersContext typeParameters() throws RecognitionException {
		TypeParametersContext _localctx = new TypeParametersContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_typeParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			match(LT);
			setState(417);
			typeParameter();
			setState(422);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(418);
				match(COMMA);
				setState(419);
				typeParameter();
				}
				}
				setState(424);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(425);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeParameterContext extends ParserRuleContext {
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public TerminalNode EXTENDS() { return getToken(TomJavaParser.EXTENDS, 0); }
		public TypeBoundContext typeBound() {
			return getRuleContext(TypeBoundContext.class,0);
		}
		public TypeParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeParameter(this);
		}
	}

	public final TypeParameterContext typeParameter() throws RecognitionException {
		TypeParameterContext _localctx = new TypeParameterContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_typeParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(427);
				annotation();
				}
				}
				setState(432);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(433);
			javaIdentifier();
			setState(436);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(434);
				match(EXTENDS);
				setState(435);
				typeBound();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeBoundContext extends ParserRuleContext {
		public List<TypeTypeContext> typeType() {
			return getRuleContexts(TypeTypeContext.class);
		}
		public TypeTypeContext typeType(int i) {
			return getRuleContext(TypeTypeContext.class,i);
		}
		public TypeBoundContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeBound; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeBound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeBound(this);
		}
	}

	public final TypeBoundContext typeBound() throws RecognitionException {
		TypeBoundContext _localctx = new TypeBoundContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_typeBound);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438);
			typeType();
			setState(443);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BITAND) {
				{
				{
				setState(439);
				match(BITAND);
				setState(440);
				typeType();
				}
				}
				setState(445);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumDeclarationContext extends ParserRuleContext {
		public TerminalNode ENUM() { return getToken(TomJavaParser.ENUM, 0); }
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public TerminalNode IMPLEMENTS() { return getToken(TomJavaParser.IMPLEMENTS, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public EnumConstantsContext enumConstants() {
			return getRuleContext(EnumConstantsContext.class,0);
		}
		public EnumBodyDeclarationsContext enumBodyDeclarations() {
			return getRuleContext(EnumBodyDeclarationsContext.class,0);
		}
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterEnumDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitEnumDeclaration(this);
		}
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_enumDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			match(ENUM);
			setState(447);
			javaIdentifier();
			setState(450);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(448);
				match(IMPLEMENTS);
				setState(449);
				typeList();
				}
			}

			setState(452);
			match(LBRACE);
			setState(454);
			_la = _input.LA(1);
			if (((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (VISIT - 60)) | (1L << (IS_FSYM - 60)) | (1L << (IS_SORT - 60)) | (1L << (MAKE - 60)) | (1L << (MAKE_EMPTY - 60)) | (1L << (MAKE_APPEND - 60)) | (1L << (MAKE_INSERT - 60)) | (1L << (GET_SLOT - 60)) | (1L << (GET_DEFAULT - 60)) | (1L << (GET_ELEMENT - 60)) | (1L << (GET_HEAD - 60)) | (1L << (GET_TAIL - 60)) | (1L << (GET_SIZE - 60)) | (1L << (IS_EMPTY - 60)) | (1L << (IMPLEMENT - 60)) | (1L << (EQUALS - 60)) | (1L << (WHEN - 60)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				setState(453);
				enumConstants();
				}
			}

			setState(457);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(456);
				match(COMMA);
				}
			}

			setState(460);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(459);
				enumBodyDeclarations();
				}
			}

			setState(462);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumConstantsContext extends ParserRuleContext {
		public List<EnumConstantContext> enumConstant() {
			return getRuleContexts(EnumConstantContext.class);
		}
		public EnumConstantContext enumConstant(int i) {
			return getRuleContext(EnumConstantContext.class,i);
		}
		public EnumConstantsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumConstants; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterEnumConstants(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitEnumConstants(this);
		}
	}

	public final EnumConstantsContext enumConstants() throws RecognitionException {
		EnumConstantsContext _localctx = new EnumConstantsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_enumConstants);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(464);
			enumConstant();
			setState(469);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(465);
					match(COMMA);
					setState(466);
					enumConstant();
					}
					} 
				}
				setState(471);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumConstantContext extends ParserRuleContext {
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public EnumConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumConstant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterEnumConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitEnumConstant(this);
		}
	}

	public final EnumConstantContext enumConstant() throws RecognitionException {
		EnumConstantContext _localctx = new EnumConstantContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_enumConstant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(475);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(472);
				annotation();
				}
				}
				setState(477);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(478);
			javaIdentifier();
			setState(480);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(479);
				arguments();
				}
			}

			setState(483);
			_la = _input.LA(1);
			if (_la==LBRACE) {
				{
				setState(482);
				classBody();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumBodyDeclarationsContext extends ParserRuleContext {
		public List<ClassBodyDeclarationContext> classBodyDeclaration() {
			return getRuleContexts(ClassBodyDeclarationContext.class);
		}
		public ClassBodyDeclarationContext classBodyDeclaration(int i) {
			return getRuleContext(ClassBodyDeclarationContext.class,i);
		}
		public EnumBodyDeclarationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumBodyDeclarations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterEnumBodyDeclarations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitEnumBodyDeclarations(this);
		}
	}

	public final EnumBodyDeclarationsContext enumBodyDeclarations() throws RecognitionException {
		EnumBodyDeclarationsContext _localctx = new EnumBodyDeclarationsContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_enumBodyDeclarations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
			match(SEMI);
			setState(489);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(486);
				classBodyDeclaration();
				}
				}
				setState(491);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceDeclarationContext extends ParserRuleContext {
		public TerminalNode INTERFACE() { return getToken(TomJavaParser.INTERFACE, 0); }
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public InterfaceBodyContext interfaceBody() {
			return getRuleContext(InterfaceBodyContext.class,0);
		}
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public TerminalNode EXTENDS() { return getToken(TomJavaParser.EXTENDS, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public InterfaceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterInterfaceDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitInterfaceDeclaration(this);
		}
	}

	public final InterfaceDeclarationContext interfaceDeclaration() throws RecognitionException {
		InterfaceDeclarationContext _localctx = new InterfaceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_interfaceDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(492);
			match(INTERFACE);
			setState(493);
			javaIdentifier();
			setState(495);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(494);
				typeParameters();
				}
			}

			setState(499);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(497);
				match(EXTENDS);
				setState(498);
				typeList();
				}
			}

			setState(501);
			interfaceBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<ClassBodyDeclarationContext> classBodyDeclaration() {
			return getRuleContexts(ClassBodyDeclarationContext.class);
		}
		public ClassBodyDeclarationContext classBodyDeclaration(int i) {
			return getRuleContext(ClassBodyDeclarationContext.class,i);
		}
		public ClassBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterClassBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitClassBody(this);
		}
	}

	public final ClassBodyContext classBody() throws RecognitionException {
		ClassBodyContext _localctx = new ClassBodyContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_classBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(503);
			match(LBRACE);
			setState(507);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(504);
				classBodyDeclaration();
				}
				}
				setState(509);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(510);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<InterfaceBodyDeclarationContext> interfaceBodyDeclaration() {
			return getRuleContexts(InterfaceBodyDeclarationContext.class);
		}
		public InterfaceBodyDeclarationContext interfaceBodyDeclaration(int i) {
			return getRuleContext(InterfaceBodyDeclarationContext.class,i);
		}
		public InterfaceBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterInterfaceBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitInterfaceBody(this);
		}
	}

	public final InterfaceBodyContext interfaceBody() throws RecognitionException {
		InterfaceBodyContext _localctx = new InterfaceBodyContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_interfaceBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(512);
			match(LBRACE);
			setState(516);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DEFAULT) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(513);
				interfaceBodyDeclaration();
				}
				}
				setState(518);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(519);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyDeclarationContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode STATIC() { return getToken(TomJavaParser.STATIC, 0); }
		public MemberDeclarationContext memberDeclaration() {
			return getRuleContext(MemberDeclarationContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public TomDeclarationContext tomDeclaration() {
			return getRuleContext(TomDeclarationContext.class,0);
		}
		public ClassBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBodyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterClassBodyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitClassBodyDeclaration(this);
		}
	}

	public final ClassBodyDeclarationContext classBodyDeclaration() throws RecognitionException {
		ClassBodyDeclarationContext _localctx = new ClassBodyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_classBodyDeclaration);
		int _la;
		try {
			int _alt;
			setState(534);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(521);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(523);
				_la = _input.LA(1);
				if (_la==STATIC) {
					{
					setState(522);
					match(STATIC);
					}
				}

				setState(525);
				block();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(529);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(526);
						modifier();
						}
						} 
					}
					setState(531);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
				}
				setState(532);
				memberDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(533);
				tomDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemberDeclarationContext extends ParserRuleContext {
		public MethodDeclarationContext methodDeclaration() {
			return getRuleContext(MethodDeclarationContext.class,0);
		}
		public GenericMethodDeclarationContext genericMethodDeclaration() {
			return getRuleContext(GenericMethodDeclarationContext.class,0);
		}
		public FieldDeclarationContext fieldDeclaration() {
			return getRuleContext(FieldDeclarationContext.class,0);
		}
		public ConstructorDeclarationContext constructorDeclaration() {
			return getRuleContext(ConstructorDeclarationContext.class,0);
		}
		public GenericConstructorDeclarationContext genericConstructorDeclaration() {
			return getRuleContext(GenericConstructorDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public MemberDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMemberDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMemberDeclaration(this);
		}
	}

	public final MemberDeclarationContext memberDeclaration() throws RecognitionException {
		MemberDeclarationContext _localctx = new MemberDeclarationContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_memberDeclaration);
		try {
			setState(545);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(536);
				methodDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(537);
				genericMethodDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(538);
				fieldDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(539);
				constructorDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(540);
				genericConstructorDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(541);
				interfaceDeclaration();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(542);
				annotationTypeDeclaration();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(543);
				classDeclaration();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(544);
				enumDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodDeclarationContext extends ParserRuleContext {
		public TypeTypeOrVoidContext typeTypeOrVoid() {
			return getRuleContext(TypeTypeOrVoidContext.class,0);
		}
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public FormalParametersContext formalParameters() {
			return getRuleContext(FormalParametersContext.class,0);
		}
		public MethodBodyContext methodBody() {
			return getRuleContext(MethodBodyContext.class,0);
		}
		public TerminalNode THROWS() { return getToken(TomJavaParser.THROWS, 0); }
		public QualifiedNameListContext qualifiedNameList() {
			return getRuleContext(QualifiedNameListContext.class,0);
		}
		public MethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMethodDeclaration(this);
		}
	}

	public final MethodDeclarationContext methodDeclaration() throws RecognitionException {
		MethodDeclarationContext _localctx = new MethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_methodDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(547);
			typeTypeOrVoid();
			setState(548);
			javaIdentifier();
			setState(549);
			formalParameters();
			setState(554);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(550);
				match(LBRACK);
				setState(551);
				match(RBRACK);
				}
				}
				setState(556);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(559);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(557);
				match(THROWS);
				setState(558);
				qualifiedNameList();
				}
			}

			setState(561);
			methodBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodBodyContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MethodBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMethodBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMethodBody(this);
		}
	}

	public final MethodBodyContext methodBody() throws RecognitionException {
		MethodBodyContext _localctx = new MethodBodyContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_methodBody);
		try {
			setState(565);
			switch (_input.LA(1)) {
			case LBRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(563);
				block();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(564);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeTypeOrVoidContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode VOID() { return getToken(TomJavaParser.VOID, 0); }
		public TypeTypeOrVoidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeTypeOrVoid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeTypeOrVoid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeTypeOrVoid(this);
		}
	}

	public final TypeTypeOrVoidContext typeTypeOrVoid() throws RecognitionException {
		TypeTypeOrVoidContext _localctx = new TypeTypeOrVoidContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_typeTypeOrVoid);
		try {
			setState(569);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(567);
				typeType();
				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 2);
				{
				setState(568);
				match(VOID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GenericMethodDeclarationContext extends ParserRuleContext {
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public MethodDeclarationContext methodDeclaration() {
			return getRuleContext(MethodDeclarationContext.class,0);
		}
		public GenericMethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericMethodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGenericMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGenericMethodDeclaration(this);
		}
	}

	public final GenericMethodDeclarationContext genericMethodDeclaration() throws RecognitionException {
		GenericMethodDeclarationContext _localctx = new GenericMethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_genericMethodDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(571);
			typeParameters();
			setState(572);
			methodDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GenericConstructorDeclarationContext extends ParserRuleContext {
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public ConstructorDeclarationContext constructorDeclaration() {
			return getRuleContext(ConstructorDeclarationContext.class,0);
		}
		public GenericConstructorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericConstructorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGenericConstructorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGenericConstructorDeclaration(this);
		}
	}

	public final GenericConstructorDeclarationContext genericConstructorDeclaration() throws RecognitionException {
		GenericConstructorDeclarationContext _localctx = new GenericConstructorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_genericConstructorDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(574);
			typeParameters();
			setState(575);
			constructorDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstructorDeclarationContext extends ParserRuleContext {
		public BlockContext constructorBody;
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public FormalParametersContext formalParameters() {
			return getRuleContext(FormalParametersContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode THROWS() { return getToken(TomJavaParser.THROWS, 0); }
		public QualifiedNameListContext qualifiedNameList() {
			return getRuleContext(QualifiedNameListContext.class,0);
		}
		public ConstructorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterConstructorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitConstructorDeclaration(this);
		}
	}

	public final ConstructorDeclarationContext constructorDeclaration() throws RecognitionException {
		ConstructorDeclarationContext _localctx = new ConstructorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_constructorDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(577);
			javaIdentifier();
			setState(578);
			formalParameters();
			setState(581);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(579);
				match(THROWS);
				setState(580);
				qualifiedNameList();
				}
			}

			setState(583);
			((ConstructorDeclarationContext)_localctx).constructorBody = block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldDeclarationContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public VariableDeclaratorsContext variableDeclarators() {
			return getRuleContext(VariableDeclaratorsContext.class,0);
		}
		public FieldDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterFieldDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitFieldDeclaration(this);
		}
	}

	public final FieldDeclarationContext fieldDeclaration() throws RecognitionException {
		FieldDeclarationContext _localctx = new FieldDeclarationContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_fieldDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(585);
			typeType();
			setState(586);
			variableDeclarators();
			setState(587);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceBodyDeclarationContext extends ParserRuleContext {
		public InterfaceMemberDeclarationContext interfaceMemberDeclaration() {
			return getRuleContext(InterfaceMemberDeclarationContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public TomDeclarationContext tomDeclaration() {
			return getRuleContext(TomDeclarationContext.class,0);
		}
		public InterfaceBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceBodyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterInterfaceBodyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitInterfaceBodyDeclaration(this);
		}
	}

	public final InterfaceBodyDeclarationContext interfaceBodyDeclaration() throws RecognitionException {
		InterfaceBodyDeclarationContext _localctx = new InterfaceBodyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_interfaceBodyDeclaration);
		try {
			int _alt;
			setState(598);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case CLASS:
			case DEFAULT:
			case DOUBLE:
			case ENUM:
			case FINAL:
			case FLOAT:
			case INT:
			case INTERFACE:
			case LONG:
			case NATIVE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case SHORT:
			case STATIC:
			case STRICTFP:
			case SYNCHRONIZED:
			case TRANSIENT:
			case VOID:
			case VOLATILE:
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case LT:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(592);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(589);
						modifier();
						}
						} 
					}
					setState(594);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
				}
				setState(595);
				interfaceMemberDeclaration();
				}
				break;
			case STRATEGY:
			case INCLUDE:
			case GOM:
			case OP:
			case OPARRAY:
			case OPLIST:
			case TYPETERM:
			case RULE:
				enterOuterAlt(_localctx, 2);
				{
				setState(596);
				tomDeclaration();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 3);
				{
				setState(597);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceMemberDeclarationContext extends ParserRuleContext {
		public ConstDeclarationContext constDeclaration() {
			return getRuleContext(ConstDeclarationContext.class,0);
		}
		public InterfaceMethodDeclarationContext interfaceMethodDeclaration() {
			return getRuleContext(InterfaceMethodDeclarationContext.class,0);
		}
		public GenericInterfaceMethodDeclarationContext genericInterfaceMethodDeclaration() {
			return getRuleContext(GenericInterfaceMethodDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public InterfaceMemberDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceMemberDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterInterfaceMemberDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitInterfaceMemberDeclaration(this);
		}
	}

	public final InterfaceMemberDeclarationContext interfaceMemberDeclaration() throws RecognitionException {
		InterfaceMemberDeclarationContext _localctx = new InterfaceMemberDeclarationContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_interfaceMemberDeclaration);
		try {
			setState(607);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(600);
				constDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(601);
				interfaceMethodDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(602);
				genericInterfaceMethodDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(603);
				interfaceDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(604);
				annotationTypeDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(605);
				classDeclaration();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(606);
				enumDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstDeclarationContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public List<ConstantDeclaratorContext> constantDeclarator() {
			return getRuleContexts(ConstantDeclaratorContext.class);
		}
		public ConstantDeclaratorContext constantDeclarator(int i) {
			return getRuleContext(ConstantDeclaratorContext.class,i);
		}
		public ConstDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterConstDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitConstDeclaration(this);
		}
	}

	public final ConstDeclarationContext constDeclaration() throws RecognitionException {
		ConstDeclarationContext _localctx = new ConstDeclarationContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_constDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(609);
			typeType();
			setState(610);
			constantDeclarator();
			setState(615);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(611);
				match(COMMA);
				setState(612);
				constantDeclarator();
				}
				}
				setState(617);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(618);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantDeclaratorContext extends ParserRuleContext {
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public VariableInitializerContext variableInitializer() {
			return getRuleContext(VariableInitializerContext.class,0);
		}
		public ConstantDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterConstantDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitConstantDeclarator(this);
		}
	}

	public final ConstantDeclaratorContext constantDeclarator() throws RecognitionException {
		ConstantDeclaratorContext _localctx = new ConstantDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_constantDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(620);
			javaIdentifier();
			setState(625);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(621);
				match(LBRACK);
				setState(622);
				match(RBRACK);
				}
				}
				setState(627);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(628);
			match(ASSIGN);
			setState(629);
			variableInitializer();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceMethodDeclarationContext extends ParserRuleContext {
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public FormalParametersContext formalParameters() {
			return getRuleContext(FormalParametersContext.class,0);
		}
		public MethodBodyContext methodBody() {
			return getRuleContext(MethodBodyContext.class,0);
		}
		public TypeTypeOrVoidContext typeTypeOrVoid() {
			return getRuleContext(TypeTypeOrVoidContext.class,0);
		}
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public List<InterfaceMethodModifierContext> interfaceMethodModifier() {
			return getRuleContexts(InterfaceMethodModifierContext.class);
		}
		public InterfaceMethodModifierContext interfaceMethodModifier(int i) {
			return getRuleContext(InterfaceMethodModifierContext.class,i);
		}
		public TerminalNode THROWS() { return getToken(TomJavaParser.THROWS, 0); }
		public QualifiedNameListContext qualifiedNameList() {
			return getRuleContext(QualifiedNameListContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public InterfaceMethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceMethodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterInterfaceMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitInterfaceMethodDeclaration(this);
		}
	}

	public final InterfaceMethodDeclarationContext interfaceMethodDeclaration() throws RecognitionException {
		InterfaceMethodDeclarationContext _localctx = new InterfaceMethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_interfaceMethodDeclaration);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(634);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(631);
					interfaceMethodModifier();
					}
					} 
				}
				setState(636);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			}
			setState(647);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case VOID:
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case AT:
			case IDENTIFIER:
				{
				setState(637);
				typeTypeOrVoid();
				}
				break;
			case LT:
				{
				setState(638);
				typeParameters();
				setState(642);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(639);
						annotation();
						}
						} 
					}
					setState(644);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				}
				setState(645);
				typeTypeOrVoid();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(649);
			javaIdentifier();
			setState(650);
			formalParameters();
			setState(655);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(651);
				match(LBRACK);
				setState(652);
				match(RBRACK);
				}
				}
				setState(657);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(660);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(658);
				match(THROWS);
				setState(659);
				qualifiedNameList();
				}
			}

			setState(662);
			methodBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceMethodModifierContext extends ParserRuleContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public TerminalNode PUBLIC() { return getToken(TomJavaParser.PUBLIC, 0); }
		public TerminalNode ABSTRACT() { return getToken(TomJavaParser.ABSTRACT, 0); }
		public TerminalNode DEFAULT() { return getToken(TomJavaParser.DEFAULT, 0); }
		public TerminalNode STATIC() { return getToken(TomJavaParser.STATIC, 0); }
		public TerminalNode STRICTFP() { return getToken(TomJavaParser.STRICTFP, 0); }
		public InterfaceMethodModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceMethodModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterInterfaceMethodModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitInterfaceMethodModifier(this);
		}
	}

	public final InterfaceMethodModifierContext interfaceMethodModifier() throws RecognitionException {
		InterfaceMethodModifierContext _localctx = new InterfaceMethodModifierContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_interfaceMethodModifier);
		try {
			setState(670);
			switch (_input.LA(1)) {
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(664);
				annotation();
				}
				break;
			case PUBLIC:
				enterOuterAlt(_localctx, 2);
				{
				setState(665);
				match(PUBLIC);
				}
				break;
			case ABSTRACT:
				enterOuterAlt(_localctx, 3);
				{
				setState(666);
				match(ABSTRACT);
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 4);
				{
				setState(667);
				match(DEFAULT);
				}
				break;
			case STATIC:
				enterOuterAlt(_localctx, 5);
				{
				setState(668);
				match(STATIC);
				}
				break;
			case STRICTFP:
				enterOuterAlt(_localctx, 6);
				{
				setState(669);
				match(STRICTFP);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GenericInterfaceMethodDeclarationContext extends ParserRuleContext {
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public InterfaceMethodDeclarationContext interfaceMethodDeclaration() {
			return getRuleContext(InterfaceMethodDeclarationContext.class,0);
		}
		public GenericInterfaceMethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericInterfaceMethodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGenericInterfaceMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGenericInterfaceMethodDeclaration(this);
		}
	}

	public final GenericInterfaceMethodDeclarationContext genericInterfaceMethodDeclaration() throws RecognitionException {
		GenericInterfaceMethodDeclarationContext _localctx = new GenericInterfaceMethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_genericInterfaceMethodDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(672);
			typeParameters();
			setState(673);
			interfaceMethodDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclaratorsContext extends ParserRuleContext {
		public List<VariableDeclaratorContext> variableDeclarator() {
			return getRuleContexts(VariableDeclaratorContext.class);
		}
		public VariableDeclaratorContext variableDeclarator(int i) {
			return getRuleContext(VariableDeclaratorContext.class,i);
		}
		public VariableDeclaratorsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarators; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterVariableDeclarators(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitVariableDeclarators(this);
		}
	}

	public final VariableDeclaratorsContext variableDeclarators() throws RecognitionException {
		VariableDeclaratorsContext _localctx = new VariableDeclaratorsContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_variableDeclarators);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(675);
			variableDeclarator();
			setState(680);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(676);
				match(COMMA);
				setState(677);
				variableDeclarator();
				}
				}
				setState(682);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclaratorContext extends ParserRuleContext {
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public VariableInitializerContext variableInitializer() {
			return getRuleContext(VariableInitializerContext.class,0);
		}
		public VariableDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterVariableDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitVariableDeclarator(this);
		}
	}

	public final VariableDeclaratorContext variableDeclarator() throws RecognitionException {
		VariableDeclaratorContext _localctx = new VariableDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_variableDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(683);
			variableDeclaratorId();
			setState(686);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(684);
				match(ASSIGN);
				setState(685);
				variableInitializer();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclaratorIdContext extends ParserRuleContext {
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public VariableDeclaratorIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaratorId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterVariableDeclaratorId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitVariableDeclaratorId(this);
		}
	}

	public final VariableDeclaratorIdContext variableDeclaratorId() throws RecognitionException {
		VariableDeclaratorIdContext _localctx = new VariableDeclaratorIdContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_variableDeclaratorId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(688);
			javaIdentifier();
			setState(693);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(689);
				match(LBRACK);
				setState(690);
				match(RBRACK);
				}
				}
				setState(695);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableInitializerContext extends ParserRuleContext {
		public ArrayInitializerContext arrayInitializer() {
			return getRuleContext(ArrayInitializerContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterVariableInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitVariableInitializer(this);
		}
	}

	public final VariableInitializerContext variableInitializer() throws RecognitionException {
		VariableInitializerContext _localctx = new VariableInitializerContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_variableInitializer);
		try {
			setState(698);
			switch (_input.LA(1)) {
			case LBRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(696);
				arrayInitializer();
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case NEW:
			case SHORT:
			case SUPER:
			case THIS:
			case VOID:
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case LPAREN:
			case LT:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case ANTI:
			case BQUOTE:
			case METAQUOTE:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(697);
				expression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayInitializerContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<VariableInitializerContext> variableInitializer() {
			return getRuleContexts(VariableInitializerContext.class);
		}
		public VariableInitializerContext variableInitializer(int i) {
			return getRuleContext(VariableInitializerContext.class,i);
		}
		public ArrayInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterArrayInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitArrayInitializer(this);
		}
	}

	public final ArrayInitializerContext arrayInitializer() throws RecognitionException {
		ArrayInitializerContext _localctx = new ArrayInitializerContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_arrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(700);
			match(LBRACE);
			setState(712);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LBRACE - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
				{
				setState(701);
				variableInitializer();
				setState(706);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(702);
						match(COMMA);
						setState(703);
						variableInitializer();
						}
						} 
					}
					setState(708);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
				}
				setState(710);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(709);
					match(COMMA);
					}
				}

				}
			}

			setState(714);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassOrInterfaceTypeContext extends ParserRuleContext {
		public List<JavaIdentifierContext> javaIdentifier() {
			return getRuleContexts(JavaIdentifierContext.class);
		}
		public JavaIdentifierContext javaIdentifier(int i) {
			return getRuleContext(JavaIdentifierContext.class,i);
		}
		public List<TypeArgumentsContext> typeArguments() {
			return getRuleContexts(TypeArgumentsContext.class);
		}
		public TypeArgumentsContext typeArguments(int i) {
			return getRuleContext(TypeArgumentsContext.class,i);
		}
		public ClassOrInterfaceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterClassOrInterfaceType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitClassOrInterfaceType(this);
		}
	}

	public final ClassOrInterfaceTypeContext classOrInterfaceType() throws RecognitionException {
		ClassOrInterfaceTypeContext _localctx = new ClassOrInterfaceTypeContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_classOrInterfaceType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(716);
			javaIdentifier();
			setState(718);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(717);
				typeArguments();
				}
				break;
			}
			setState(727);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(720);
					match(DOT);
					setState(721);
					javaIdentifier();
					setState(723);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
					case 1:
						{
						setState(722);
						typeArguments();
						}
						break;
					}
					}
					} 
				}
				setState(729);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeArgumentContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode EXTENDS() { return getToken(TomJavaParser.EXTENDS, 0); }
		public TerminalNode SUPER() { return getToken(TomJavaParser.SUPER, 0); }
		public TypeArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeArgument(this);
		}
	}

	public final TypeArgumentContext typeArgument() throws RecognitionException {
		TypeArgumentContext _localctx = new TypeArgumentContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_typeArgument);
		int _la;
		try {
			setState(736);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(730);
				typeType();
				}
				break;
			case QMARK:
				enterOuterAlt(_localctx, 2);
				{
				setState(731);
				match(QMARK);
				setState(734);
				_la = _input.LA(1);
				if (_la==EXTENDS || _la==SUPER) {
					{
					setState(732);
					_la = _input.LA(1);
					if ( !(_la==EXTENDS || _la==SUPER) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(733);
					typeType();
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedNameListContext extends ParserRuleContext {
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public QualifiedNameListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedNameList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterQualifiedNameList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitQualifiedNameList(this);
		}
	}

	public final QualifiedNameListContext qualifiedNameList() throws RecognitionException {
		QualifiedNameListContext _localctx = new QualifiedNameListContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_qualifiedNameList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(738);
			qualifiedName();
			setState(743);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(739);
				match(COMMA);
				setState(740);
				qualifiedName();
				}
				}
				setState(745);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormalParametersContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public FormalParameterListContext formalParameterList() {
			return getRuleContext(FormalParameterListContext.class,0);
		}
		public FormalParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterFormalParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitFormalParameters(this);
		}
	}

	public final FormalParametersContext formalParameters() throws RecognitionException {
		FormalParametersContext _localctx = new FormalParametersContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_formalParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(746);
			match(LPAREN);
			setState(748);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << SHORT) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				setState(747);
				formalParameterList();
				}
			}

			setState(750);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormalParameterListContext extends ParserRuleContext {
		public List<FormalParameterContext> formalParameter() {
			return getRuleContexts(FormalParameterContext.class);
		}
		public FormalParameterContext formalParameter(int i) {
			return getRuleContext(FormalParameterContext.class,i);
		}
		public LastFormalParameterContext lastFormalParameter() {
			return getRuleContext(LastFormalParameterContext.class,0);
		}
		public FormalParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterFormalParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitFormalParameterList(this);
		}
	}

	public final FormalParameterListContext formalParameterList() throws RecognitionException {
		FormalParameterListContext _localctx = new FormalParameterListContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_formalParameterList);
		int _la;
		try {
			int _alt;
			setState(765);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(752);
				formalParameter();
				setState(757);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(753);
						match(COMMA);
						setState(754);
						formalParameter();
						}
						} 
					}
					setState(759);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
				}
				setState(762);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(760);
					match(COMMA);
					setState(761);
					lastFormalParameter();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(764);
				lastFormalParameter();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormalParameterContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public FormalParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterFormalParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitFormalParameter(this);
		}
	}

	public final FormalParameterContext formalParameter() throws RecognitionException {
		FormalParameterContext _localctx = new FormalParameterContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_formalParameter);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(770);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(767);
					variableModifier();
					}
					} 
				}
				setState(772);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			}
			setState(773);
			typeType();
			setState(774);
			variableDeclaratorId();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LastFormalParameterContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public LastFormalParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lastFormalParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterLastFormalParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitLastFormalParameter(this);
		}
	}

	public final LastFormalParameterContext lastFormalParameter() throws RecognitionException {
		LastFormalParameterContext _localctx = new LastFormalParameterContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_lastFormalParameter);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(779);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(776);
					variableModifier();
					}
					} 
				}
				setState(781);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			}
			setState(782);
			typeType();
			setState(783);
			match(ELLIPSIS);
			setState(784);
			variableDeclaratorId();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedNameContext extends ParserRuleContext {
		public List<JavaIdentifierContext> javaIdentifier() {
			return getRuleContexts(JavaIdentifierContext.class);
		}
		public JavaIdentifierContext javaIdentifier(int i) {
			return getRuleContext(JavaIdentifierContext.class,i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitQualifiedName(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_qualifiedName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(786);
			javaIdentifier();
			setState(791);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(787);
					match(DOT);
					setState(788);
					javaIdentifier();
					}
					} 
				}
				setState(793);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public IntegerLiteralContext integerLiteral() {
			return getRuleContext(IntegerLiteralContext.class,0);
		}
		public FloatLiteralContext floatLiteral() {
			return getRuleContext(FloatLiteralContext.class,0);
		}
		public TerminalNode CHAR_LITERAL() { return getToken(TomJavaParser.CHAR_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(TomJavaParser.STRING_LITERAL, 0); }
		public TerminalNode BOOL_LITERAL() { return getToken(TomJavaParser.BOOL_LITERAL, 0); }
		public TerminalNode NULL_LITERAL() { return getToken(TomJavaParser.NULL_LITERAL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitLiteral(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_literal);
		try {
			setState(800);
			switch (_input.LA(1)) {
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(794);
				integerLiteral();
				}
				break;
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(795);
				floatLiteral();
				}
				break;
			case CHAR_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(796);
				match(CHAR_LITERAL);
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 4);
				{
				setState(797);
				match(STRING_LITERAL);
				}
				break;
			case BOOL_LITERAL:
				enterOuterAlt(_localctx, 5);
				{
				setState(798);
				match(BOOL_LITERAL);
				}
				break;
			case NULL_LITERAL:
				enterOuterAlt(_localctx, 6);
				{
				setState(799);
				match(NULL_LITERAL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerLiteralContext extends ParserRuleContext {
		public TerminalNode DECIMAL_LITERAL() { return getToken(TomJavaParser.DECIMAL_LITERAL, 0); }
		public TerminalNode HEX_LITERAL() { return getToken(TomJavaParser.HEX_LITERAL, 0); }
		public TerminalNode OCT_LITERAL() { return getToken(TomJavaParser.OCT_LITERAL, 0); }
		public TerminalNode BINARY_LITERAL() { return getToken(TomJavaParser.BINARY_LITERAL, 0); }
		public IntegerLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integerLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterIntegerLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitIntegerLiteral(this);
		}
	}

	public final IntegerLiteralContext integerLiteral() throws RecognitionException {
		IntegerLiteralContext _localctx = new IntegerLiteralContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_integerLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(802);
			_la = _input.LA(1);
			if ( !(((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (HEX_LITERAL - 77)) | (1L << (OCT_LITERAL - 77)) | (1L << (BINARY_LITERAL - 77)))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FloatLiteralContext extends ParserRuleContext {
		public TerminalNode FLOAT_LITERAL() { return getToken(TomJavaParser.FLOAT_LITERAL, 0); }
		public TerminalNode HEX_FLOAT_LITERAL() { return getToken(TomJavaParser.HEX_FLOAT_LITERAL, 0); }
		public FloatLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterFloatLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitFloatLiteral(this);
		}
	}

	public final FloatLiteralContext floatLiteral() throws RecognitionException {
		FloatLiteralContext _localctx = new FloatLiteralContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_floatLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(804);
			_la = _input.LA(1);
			if ( !(_la==FLOAT_LITERAL || _la==HEX_FLOAT_LITERAL) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public ElementValuePairsContext elementValuePairs() {
			return getRuleContext(ElementValuePairsContext.class,0);
		}
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAnnotation(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(806);
			match(AT);
			setState(807);
			qualifiedName();
			setState(814);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(808);
				match(LPAREN);
				setState(811);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
				case 1:
					{
					setState(809);
					elementValuePairs();
					}
					break;
				case 2:
					{
					setState(810);
					elementValue();
					}
					break;
				}
				setState(813);
				match(RPAREN);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementValuePairsContext extends ParserRuleContext {
		public List<ElementValuePairContext> elementValuePair() {
			return getRuleContexts(ElementValuePairContext.class);
		}
		public ElementValuePairContext elementValuePair(int i) {
			return getRuleContext(ElementValuePairContext.class,i);
		}
		public ElementValuePairsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePairs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterElementValuePairs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitElementValuePairs(this);
		}
	}

	public final ElementValuePairsContext elementValuePairs() throws RecognitionException {
		ElementValuePairsContext _localctx = new ElementValuePairsContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_elementValuePairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(816);
			elementValuePair();
			setState(821);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(817);
				match(COMMA);
				setState(818);
				elementValuePair();
				}
				}
				setState(823);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementValuePairContext extends ParserRuleContext {
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public ElementValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterElementValuePair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitElementValuePair(this);
		}
	}

	public final ElementValuePairContext elementValuePair() throws RecognitionException {
		ElementValuePairContext _localctx = new ElementValuePairContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_elementValuePair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(824);
			javaIdentifier();
			setState(825);
			match(ASSIGN);
			setState(826);
			elementValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementValueContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public ElementValueArrayInitializerContext elementValueArrayInitializer() {
			return getRuleContext(ElementValueArrayInitializerContext.class,0);
		}
		public ElementValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterElementValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitElementValue(this);
		}
	}

	public final ElementValueContext elementValue() throws RecognitionException {
		ElementValueContext _localctx = new ElementValueContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_elementValue);
		try {
			setState(831);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(828);
				expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(829);
				annotation();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(830);
				elementValueArrayInitializer();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementValueArrayInitializerContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<ElementValueContext> elementValue() {
			return getRuleContexts(ElementValueContext.class);
		}
		public ElementValueContext elementValue(int i) {
			return getRuleContext(ElementValueContext.class,i);
		}
		public ElementValueArrayInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValueArrayInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterElementValueArrayInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitElementValueArrayInitializer(this);
		}
	}

	public final ElementValueArrayInitializerContext elementValueArrayInitializer() throws RecognitionException {
		ElementValueArrayInitializerContext _localctx = new ElementValueArrayInitializerContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_elementValueArrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(833);
			match(LBRACE);
			setState(842);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LBRACE - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
				{
				setState(834);
				elementValue();
				setState(839);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(835);
						match(COMMA);
						setState(836);
						elementValue();
						}
						} 
					}
					setState(841);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
				}
				}
			}

			setState(845);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(844);
				match(COMMA);
				}
			}

			setState(847);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationTypeDeclarationContext extends ParserRuleContext {
		public TerminalNode INTERFACE() { return getToken(TomJavaParser.INTERFACE, 0); }
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public AnnotationTypeBodyContext annotationTypeBody() {
			return getRuleContext(AnnotationTypeBodyContext.class,0);
		}
		public AnnotationTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAnnotationTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAnnotationTypeDeclaration(this);
		}
	}

	public final AnnotationTypeDeclarationContext annotationTypeDeclaration() throws RecognitionException {
		AnnotationTypeDeclarationContext _localctx = new AnnotationTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_annotationTypeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(849);
			match(AT);
			setState(850);
			match(INTERFACE);
			setState(851);
			javaIdentifier();
			setState(852);
			annotationTypeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationTypeBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<AnnotationTypeElementDeclarationContext> annotationTypeElementDeclaration() {
			return getRuleContexts(AnnotationTypeElementDeclarationContext.class);
		}
		public AnnotationTypeElementDeclarationContext annotationTypeElementDeclaration(int i) {
			return getRuleContext(AnnotationTypeElementDeclarationContext.class,i);
		}
		public AnnotationTypeBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAnnotationTypeBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAnnotationTypeBody(this);
		}
	}

	public final AnnotationTypeBodyContext annotationTypeBody() throws RecognitionException {
		AnnotationTypeBodyContext _localctx = new AnnotationTypeBodyContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_annotationTypeBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(854);
			match(LBRACE);
			setState(858);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (SEMI - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(855);
				annotationTypeElementDeclaration();
				}
				}
				setState(860);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(861);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationTypeElementDeclarationContext extends ParserRuleContext {
		public AnnotationTypeElementRestContext annotationTypeElementRest() {
			return getRuleContext(AnnotationTypeElementRestContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public TomDeclarationContext tomDeclaration() {
			return getRuleContext(TomDeclarationContext.class,0);
		}
		public AnnotationTypeElementDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeElementDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAnnotationTypeElementDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAnnotationTypeElementDeclaration(this);
		}
	}

	public final AnnotationTypeElementDeclarationContext annotationTypeElementDeclaration() throws RecognitionException {
		AnnotationTypeElementDeclarationContext _localctx = new AnnotationTypeElementDeclarationContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_annotationTypeElementDeclaration);
		try {
			int _alt;
			setState(872);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case CLASS:
			case DOUBLE:
			case ENUM:
			case FINAL:
			case FLOAT:
			case INT:
			case INTERFACE:
			case LONG:
			case NATIVE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case SHORT:
			case STATIC:
			case STRICTFP:
			case SYNCHRONIZED:
			case TRANSIENT:
			case VOLATILE:
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(866);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(863);
						modifier();
						}
						} 
					}
					setState(868);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
				}
				setState(869);
				annotationTypeElementRest();
				}
				break;
			case STRATEGY:
			case INCLUDE:
			case GOM:
			case OP:
			case OPARRAY:
			case OPLIST:
			case TYPETERM:
			case RULE:
				enterOuterAlt(_localctx, 2);
				{
				setState(870);
				tomDeclaration();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 3);
				{
				setState(871);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationTypeElementRestContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public AnnotationMethodOrConstantRestContext annotationMethodOrConstantRest() {
			return getRuleContext(AnnotationMethodOrConstantRestContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public AnnotationTypeElementRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeElementRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAnnotationTypeElementRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAnnotationTypeElementRest(this);
		}
	}

	public final AnnotationTypeElementRestContext annotationTypeElementRest() throws RecognitionException {
		AnnotationTypeElementRestContext _localctx = new AnnotationTypeElementRestContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_annotationTypeElementRest);
		try {
			setState(894);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(874);
				typeType();
				setState(875);
				annotationMethodOrConstantRest();
				setState(876);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(878);
				classDeclaration();
				setState(880);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
				case 1:
					{
					setState(879);
					match(SEMI);
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(882);
				interfaceDeclaration();
				setState(884);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
				case 1:
					{
					setState(883);
					match(SEMI);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(886);
				enumDeclaration();
				setState(888);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
				case 1:
					{
					setState(887);
					match(SEMI);
					}
					break;
				}
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(890);
				annotationTypeDeclaration();
				setState(892);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
				case 1:
					{
					setState(891);
					match(SEMI);
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationMethodOrConstantRestContext extends ParserRuleContext {
		public AnnotationMethodRestContext annotationMethodRest() {
			return getRuleContext(AnnotationMethodRestContext.class,0);
		}
		public AnnotationConstantRestContext annotationConstantRest() {
			return getRuleContext(AnnotationConstantRestContext.class,0);
		}
		public AnnotationMethodOrConstantRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationMethodOrConstantRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAnnotationMethodOrConstantRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAnnotationMethodOrConstantRest(this);
		}
	}

	public final AnnotationMethodOrConstantRestContext annotationMethodOrConstantRest() throws RecognitionException {
		AnnotationMethodOrConstantRestContext _localctx = new AnnotationMethodOrConstantRestContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_annotationMethodOrConstantRest);
		try {
			setState(898);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(896);
				annotationMethodRest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(897);
				annotationConstantRest();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationMethodRestContext extends ParserRuleContext {
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public DefaultValueContext defaultValue() {
			return getRuleContext(DefaultValueContext.class,0);
		}
		public AnnotationMethodRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationMethodRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAnnotationMethodRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAnnotationMethodRest(this);
		}
	}

	public final AnnotationMethodRestContext annotationMethodRest() throws RecognitionException {
		AnnotationMethodRestContext _localctx = new AnnotationMethodRestContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_annotationMethodRest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(900);
			javaIdentifier();
			setState(901);
			match(LPAREN);
			setState(902);
			match(RPAREN);
			setState(904);
			_la = _input.LA(1);
			if (_la==DEFAULT) {
				{
				setState(903);
				defaultValue();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationConstantRestContext extends ParserRuleContext {
		public VariableDeclaratorsContext variableDeclarators() {
			return getRuleContext(VariableDeclaratorsContext.class,0);
		}
		public AnnotationConstantRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationConstantRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAnnotationConstantRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAnnotationConstantRest(this);
		}
	}

	public final AnnotationConstantRestContext annotationConstantRest() throws RecognitionException {
		AnnotationConstantRestContext _localctx = new AnnotationConstantRestContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_annotationConstantRest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(906);
			variableDeclarators();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefaultValueContext extends ParserRuleContext {
		public TerminalNode DEFAULT() { return getToken(TomJavaParser.DEFAULT, 0); }
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public DefaultValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterDefaultValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitDefaultValue(this);
		}
	}

	public final DefaultValueContext defaultValue() throws RecognitionException {
		DefaultValueContext _localctx = new DefaultValueContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_defaultValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(908);
			match(DEFAULT);
			setState(909);
			elementValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(911);
			match(LBRACE);
			setState(915);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << CONTINUE) | (1L << DO) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NEW) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << TRY) | (1L << VOID) | (1L << WHILE) | (1L << MATCH) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
				{
				{
				setState(912);
				blockStatement();
				}
				}
				setState(917);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(918);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockStatementContext extends ParserRuleContext {
		public LocalVariableDeclarationContext localVariableDeclaration() {
			return getRuleContext(LocalVariableDeclarationContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public LocalTypeDeclarationContext localTypeDeclaration() {
			return getRuleContext(LocalTypeDeclarationContext.class,0);
		}
		public TomDeclarationContext tomDeclaration() {
			return getRuleContext(TomDeclarationContext.class,0);
		}
		public BlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitBlockStatement(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_blockStatement);
		try {
			setState(926);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(920);
				localVariableDeclaration();
				setState(921);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(923);
				statement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(924);
				localTypeDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(925);
				tomDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalVariableDeclarationContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public VariableDeclaratorsContext variableDeclarators() {
			return getRuleContext(VariableDeclaratorsContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public LocalVariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localVariableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterLocalVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitLocalVariableDeclaration(this);
		}
	}

	public final LocalVariableDeclarationContext localVariableDeclaration() throws RecognitionException {
		LocalVariableDeclarationContext _localctx = new LocalVariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_localVariableDeclaration);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(931);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(928);
					variableModifier();
					}
					} 
				}
				setState(933);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
			}
			setState(934);
			typeType();
			setState(935);
			variableDeclarators();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalTypeDeclarationContext extends ParserRuleContext {
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public List<ClassOrInterfaceModifierContext> classOrInterfaceModifier() {
			return getRuleContexts(ClassOrInterfaceModifierContext.class);
		}
		public ClassOrInterfaceModifierContext classOrInterfaceModifier(int i) {
			return getRuleContext(ClassOrInterfaceModifierContext.class,i);
		}
		public LocalTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterLocalTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitLocalTypeDeclaration(this);
		}
	}

	public final LocalTypeDeclarationContext localTypeDeclaration() throws RecognitionException {
		LocalTypeDeclarationContext _localctx = new LocalTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_localTypeDeclaration);
		int _la;
		try {
			setState(948);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case CLASS:
			case FINAL:
			case INTERFACE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case STATIC:
			case STRICTFP:
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(940);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << FINAL) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << STATIC) | (1L << STRICTFP))) != 0) || _la==AT) {
					{
					{
					setState(937);
					classOrInterfaceModifier();
					}
					}
					setState(942);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(945);
				switch (_input.LA(1)) {
				case CLASS:
					{
					setState(943);
					classDeclaration();
					}
					break;
				case INTERFACE:
					{
					setState(944);
					interfaceDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(947);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public BlockContext blockLabel;
		public ExpressionContext statementExpression;
		public JavaIdentifierContext identifierLabel;
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode ASSERT() { return getToken(TomJavaParser.ASSERT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode IF() { return getToken(TomJavaParser.IF, 0); }
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(TomJavaParser.ELSE, 0); }
		public TerminalNode FOR() { return getToken(TomJavaParser.FOR, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public ForControlContext forControl() {
			return getRuleContext(ForControlContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TerminalNode WHILE() { return getToken(TomJavaParser.WHILE, 0); }
		public TerminalNode DO() { return getToken(TomJavaParser.DO, 0); }
		public TerminalNode TRY() { return getToken(TomJavaParser.TRY, 0); }
		public FinallyBlockContext finallyBlock() {
			return getRuleContext(FinallyBlockContext.class,0);
		}
		public List<CatchClauseContext> catchClause() {
			return getRuleContexts(CatchClauseContext.class);
		}
		public CatchClauseContext catchClause(int i) {
			return getRuleContext(CatchClauseContext.class,i);
		}
		public ResourceSpecificationContext resourceSpecification() {
			return getRuleContext(ResourceSpecificationContext.class,0);
		}
		public TerminalNode SWITCH() { return getToken(TomJavaParser.SWITCH, 0); }
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<SwitchBlockStatementGroupContext> switchBlockStatementGroup() {
			return getRuleContexts(SwitchBlockStatementGroupContext.class);
		}
		public SwitchBlockStatementGroupContext switchBlockStatementGroup(int i) {
			return getRuleContext(SwitchBlockStatementGroupContext.class,i);
		}
		public List<SwitchLabelContext> switchLabel() {
			return getRuleContexts(SwitchLabelContext.class);
		}
		public SwitchLabelContext switchLabel(int i) {
			return getRuleContext(SwitchLabelContext.class,i);
		}
		public TerminalNode SYNCHRONIZED() { return getToken(TomJavaParser.SYNCHRONIZED, 0); }
		public TerminalNode RETURN() { return getToken(TomJavaParser.RETURN, 0); }
		public TerminalNode THROW() { return getToken(TomJavaParser.THROW, 0); }
		public TerminalNode BREAK() { return getToken(TomJavaParser.BREAK, 0); }
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public TerminalNode CONTINUE() { return getToken(TomJavaParser.CONTINUE, 0); }
		public TerminalNode SEMI() { return getToken(TomJavaParser.SEMI, 0); }
		public TomStatementContext tomStatement() {
			return getRuleContext(TomStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_statement);
		int _la;
		try {
			int _alt;
			setState(1056);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(950);
				((StatementContext)_localctx).blockLabel = block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(951);
				match(ASSERT);
				setState(952);
				expression(0);
				setState(955);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(953);
					match(COLON);
					setState(954);
					expression(0);
					}
				}

				setState(957);
				match(SEMI);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(959);
				match(IF);
				setState(960);
				parExpression();
				setState(961);
				statement();
				setState(964);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
				case 1:
					{
					setState(962);
					match(ELSE);
					setState(963);
					statement();
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(966);
				match(FOR);
				setState(967);
				match(LPAREN);
				setState(968);
				forControl();
				setState(969);
				match(RPAREN);
				setState(970);
				statement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(972);
				match(WHILE);
				setState(973);
				parExpression();
				setState(974);
				statement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(976);
				match(DO);
				setState(977);
				statement();
				setState(978);
				match(WHILE);
				setState(979);
				parExpression();
				setState(980);
				match(SEMI);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(982);
				match(TRY);
				setState(983);
				block();
				setState(993);
				switch (_input.LA(1)) {
				case CATCH:
					{
					setState(985); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(984);
						catchClause();
						}
						}
						setState(987); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==CATCH );
					setState(990);
					_la = _input.LA(1);
					if (_la==FINALLY) {
						{
						setState(989);
						finallyBlock();
						}
					}

					}
					break;
				case FINALLY:
					{
					setState(992);
					finallyBlock();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(995);
				match(TRY);
				setState(996);
				resourceSpecification();
				setState(997);
				block();
				setState(1001);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CATCH) {
					{
					{
					setState(998);
					catchClause();
					}
					}
					setState(1003);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1005);
				_la = _input.LA(1);
				if (_la==FINALLY) {
					{
					setState(1004);
					finallyBlock();
					}
				}

				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1007);
				match(SWITCH);
				setState(1008);
				parExpression();
				setState(1009);
				match(LBRACE);
				setState(1013);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1010);
						switchBlockStatementGroup();
						}
						} 
					}
					setState(1015);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
				}
				setState(1019);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CASE || _la==DEFAULT) {
					{
					{
					setState(1016);
					switchLabel();
					}
					}
					setState(1021);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1022);
				match(RBRACE);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1024);
				match(SYNCHRONIZED);
				setState(1025);
				parExpression();
				setState(1026);
				block();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1028);
				match(RETURN);
				setState(1030);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
					{
					setState(1029);
					expression(0);
					}
				}

				setState(1032);
				match(SEMI);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1033);
				match(THROW);
				setState(1034);
				expression(0);
				setState(1035);
				match(SEMI);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1037);
				match(BREAK);
				setState(1039);
				_la = _input.LA(1);
				if (((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (VISIT - 60)) | (1L << (IS_FSYM - 60)) | (1L << (IS_SORT - 60)) | (1L << (MAKE - 60)) | (1L << (MAKE_EMPTY - 60)) | (1L << (MAKE_APPEND - 60)) | (1L << (MAKE_INSERT - 60)) | (1L << (GET_SLOT - 60)) | (1L << (GET_DEFAULT - 60)) | (1L << (GET_ELEMENT - 60)) | (1L << (GET_HEAD - 60)) | (1L << (GET_TAIL - 60)) | (1L << (GET_SIZE - 60)) | (1L << (IS_EMPTY - 60)) | (1L << (IMPLEMENT - 60)) | (1L << (EQUALS - 60)) | (1L << (WHEN - 60)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1038);
					javaIdentifier();
					}
				}

				setState(1041);
				match(SEMI);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1042);
				match(CONTINUE);
				setState(1044);
				_la = _input.LA(1);
				if (((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (VISIT - 60)) | (1L << (IS_FSYM - 60)) | (1L << (IS_SORT - 60)) | (1L << (MAKE - 60)) | (1L << (MAKE_EMPTY - 60)) | (1L << (MAKE_APPEND - 60)) | (1L << (MAKE_INSERT - 60)) | (1L << (GET_SLOT - 60)) | (1L << (GET_DEFAULT - 60)) | (1L << (GET_ELEMENT - 60)) | (1L << (GET_HEAD - 60)) | (1L << (GET_TAIL - 60)) | (1L << (GET_SIZE - 60)) | (1L << (IS_EMPTY - 60)) | (1L << (IMPLEMENT - 60)) | (1L << (EQUALS - 60)) | (1L << (WHEN - 60)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1043);
					javaIdentifier();
					}
				}

				setState(1046);
				match(SEMI);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1047);
				match(SEMI);
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(1048);
				((StatementContext)_localctx).statementExpression = expression(0);
				setState(1049);
				match(SEMI);
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(1051);
				((StatementContext)_localctx).identifierLabel = javaIdentifier();
				setState(1052);
				match(COLON);
				setState(1053);
				statement();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(1055);
				tomStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatchClauseContext extends ParserRuleContext {
		public TerminalNode CATCH() { return getToken(TomJavaParser.CATCH, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public CatchTypeContext catchType() {
			return getRuleContext(CatchTypeContext.class,0);
		}
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public CatchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterCatchClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitCatchClause(this);
		}
	}

	public final CatchClauseContext catchClause() throws RecognitionException {
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1058);
			match(CATCH);
			setState(1059);
			match(LPAREN);
			setState(1063);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(1060);
				variableModifier();
				}
				}
				setState(1065);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1066);
			catchType();
			setState(1067);
			javaIdentifier();
			setState(1068);
			match(RPAREN);
			setState(1069);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatchTypeContext extends ParserRuleContext {
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public CatchTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterCatchType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitCatchType(this);
		}
	}

	public final CatchTypeContext catchType() throws RecognitionException {
		CatchTypeContext _localctx = new CatchTypeContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_catchType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1071);
			qualifiedName();
			setState(1076);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PIPE) {
				{
				{
				setState(1072);
				match(PIPE);
				setState(1073);
				qualifiedName();
				}
				}
				setState(1078);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FinallyBlockContext extends ParserRuleContext {
		public TerminalNode FINALLY() { return getToken(TomJavaParser.FINALLY, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public FinallyBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_finallyBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterFinallyBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitFinallyBlock(this);
		}
	}

	public final FinallyBlockContext finallyBlock() throws RecognitionException {
		FinallyBlockContext _localctx = new FinallyBlockContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_finallyBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1079);
			match(FINALLY);
			setState(1080);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResourceSpecificationContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public ResourcesContext resources() {
			return getRuleContext(ResourcesContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public ResourceSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resourceSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterResourceSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitResourceSpecification(this);
		}
	}

	public final ResourceSpecificationContext resourceSpecification() throws RecognitionException {
		ResourceSpecificationContext _localctx = new ResourceSpecificationContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_resourceSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1082);
			match(LPAREN);
			setState(1083);
			resources();
			setState(1085);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(1084);
				match(SEMI);
				}
			}

			setState(1087);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResourcesContext extends ParserRuleContext {
		public List<ResourceContext> resource() {
			return getRuleContexts(ResourceContext.class);
		}
		public ResourceContext resource(int i) {
			return getRuleContext(ResourceContext.class,i);
		}
		public ResourcesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resources; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterResources(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitResources(this);
		}
	}

	public final ResourcesContext resources() throws RecognitionException {
		ResourcesContext _localctx = new ResourcesContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_resources);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1089);
			resource();
			setState(1094);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,113,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1090);
					match(SEMI);
					setState(1091);
					resource();
					}
					} 
				}
				setState(1096);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,113,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResourceContext extends ParserRuleContext {
		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return getRuleContext(ClassOrInterfaceTypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public ResourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterResource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitResource(this);
		}
	}

	public final ResourceContext resource() throws RecognitionException {
		ResourceContext _localctx = new ResourceContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_resource);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(1097);
				variableModifier();
				}
				}
				setState(1102);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1103);
			classOrInterfaceType();
			setState(1104);
			variableDeclaratorId();
			setState(1105);
			match(ASSIGN);
			setState(1106);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SwitchBlockStatementGroupContext extends ParserRuleContext {
		public List<SwitchLabelContext> switchLabel() {
			return getRuleContexts(SwitchLabelContext.class);
		}
		public SwitchLabelContext switchLabel(int i) {
			return getRuleContext(SwitchLabelContext.class,i);
		}
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public SwitchBlockStatementGroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchBlockStatementGroup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterSwitchBlockStatementGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitSwitchBlockStatementGroup(this);
		}
	}

	public final SwitchBlockStatementGroupContext switchBlockStatementGroup() throws RecognitionException {
		SwitchBlockStatementGroupContext _localctx = new SwitchBlockStatementGroupContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_switchBlockStatementGroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1109); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1108);
				switchLabel();
				}
				}
				setState(1111); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CASE || _la==DEFAULT );
			setState(1114); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1113);
				blockStatement();
				}
				}
				setState(1116); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << CONTINUE) | (1L << DO) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NEW) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << TRY) | (1L << VOID) | (1L << WHILE) | (1L << MATCH) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SwitchLabelContext extends ParserRuleContext {
		public ExpressionContext constantExpression;
		public JavaIdentifierContext enumConstantName;
		public TerminalNode CASE() { return getToken(TomJavaParser.CASE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public TerminalNode DEFAULT() { return getToken(TomJavaParser.DEFAULT, 0); }
		public SwitchLabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchLabel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterSwitchLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitSwitchLabel(this);
		}
	}

	public final SwitchLabelContext switchLabel() throws RecognitionException {
		SwitchLabelContext _localctx = new SwitchLabelContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_switchLabel);
		try {
			setState(1127);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1118);
				match(CASE);
				setState(1121);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
				case 1:
					{
					setState(1119);
					((SwitchLabelContext)_localctx).constantExpression = expression(0);
					}
					break;
				case 2:
					{
					setState(1120);
					((SwitchLabelContext)_localctx).enumConstantName = javaIdentifier();
					}
					break;
				}
				setState(1123);
				match(COLON);
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1125);
				match(DEFAULT);
				setState(1126);
				match(COLON);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForControlContext extends ParserRuleContext {
		public ExpressionListContext forUpdate;
		public EnhancedForControlContext enhancedForControl() {
			return getRuleContext(EnhancedForControlContext.class,0);
		}
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forControl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterForControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitForControl(this);
		}
	}

	public final ForControlContext forControl() throws RecognitionException {
		ForControlContext _localctx = new ForControlContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_forControl);
		int _la;
		try {
			setState(1141);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1129);
				enhancedForControl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1131);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
					{
					setState(1130);
					forInit();
					}
				}

				setState(1133);
				match(SEMI);
				setState(1135);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
					{
					setState(1134);
					expression(0);
					}
				}

				setState(1137);
				match(SEMI);
				setState(1139);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
					{
					setState(1138);
					((ForControlContext)_localctx).forUpdate = expressionList();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForInitContext extends ParserRuleContext {
		public LocalVariableDeclarationContext localVariableDeclaration() {
			return getRuleContext(LocalVariableDeclarationContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterForInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitForInit(this);
		}
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx = new ForInitContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_forInit);
		try {
			setState(1145);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1143);
				localVariableDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1144);
				expressionList();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnhancedForControlContext extends ParserRuleContext {
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public EnhancedForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enhancedForControl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterEnhancedForControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitEnhancedForControl(this);
		}
	}

	public final EnhancedForControlContext enhancedForControl() throws RecognitionException {
		EnhancedForControlContext _localctx = new EnhancedForControlContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_enhancedForControl);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1150);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,124,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1147);
					variableModifier();
					}
					} 
				}
				setState(1152);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,124,_ctx);
			}
			setState(1153);
			typeType();
			setState(1154);
			variableDeclaratorId();
			setState(1155);
			match(COLON);
			setState(1156);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParExpressionContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public ParExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterParExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitParExpression(this);
		}
	}

	public final ParExpressionContext parExpression() throws RecognitionException {
		ParExpressionContext _localctx = new ParExpressionContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_parExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1158);
			match(LPAREN);
			setState(1159);
			expression(0);
			setState(1160);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitExpressionList(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1162);
			expression(0);
			setState(1167);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1163);
				match(COMMA);
				setState(1164);
				expression(0);
				}
				}
				setState(1169);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public Token prefix;
		public Token bop;
		public Token postfix;
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public FunTermContext funTerm() {
			return getRuleContext(FunTermContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode NEW() { return getToken(TomJavaParser.NEW, 0); }
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public LambdaExpressionContext lambdaExpression() {
			return getRuleContext(LambdaExpressionContext.class,0);
		}
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public ClassTypeContext classType() {
			return getRuleContext(ClassTypeContext.class,0);
		}
		public TomTermContext tomTerm() {
			return getRuleContext(TomTermContext.class,0);
		}
		public TerminalNode THIS() { return getToken(TomJavaParser.THIS, 0); }
		public InnerCreatorContext innerCreator() {
			return getRuleContext(InnerCreatorContext.class,0);
		}
		public TerminalNode SUPER() { return getToken(TomJavaParser.SUPER, 0); }
		public SuperSuffixContext superSuffix() {
			return getRuleContext(SuperSuffixContext.class,0);
		}
		public ExplicitGenericInvocationContext explicitGenericInvocation() {
			return getRuleContext(ExplicitGenericInvocationContext.class,0);
		}
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public TerminalNode INSTANCEOF() { return getToken(TomJavaParser.INSTANCEOF, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 166;
		enterRecursionRule(_localctx, 166, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1208);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1171);
				primary();
				}
				break;
			case 2:
				{
				setState(1172);
				funTerm(0);
				setState(1173);
				match(LPAREN);
				setState(1175);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
					{
					setState(1174);
					expressionList();
					}
				}

				setState(1177);
				match(RPAREN);
				}
				break;
			case 3:
				{
				setState(1179);
				match(NEW);
				setState(1180);
				creator();
				}
				break;
			case 4:
				{
				setState(1181);
				match(LPAREN);
				setState(1182);
				typeType();
				setState(1183);
				match(RPAREN);
				setState(1184);
				expression(22);
				}
				break;
			case 5:
				{
				setState(1186);
				((ExpressionContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 108)) & ~0x3f) == 0 && ((1L << (_la - 108)) & ((1L << (INC - 108)) | (1L << (DEC - 108)) | (1L << (ADD - 108)) | (1L << (SUB - 108)))) != 0)) ) {
					((ExpressionContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1187);
				expression(20);
				}
				break;
			case 6:
				{
				setState(1188);
				((ExpressionContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TILDE || _la==ANTI) ) {
					((ExpressionContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1189);
				expression(19);
				}
				break;
			case 7:
				{
				setState(1190);
				lambdaExpression();
				}
				break;
			case 8:
				{
				setState(1191);
				typeType();
				setState(1192);
				match(COLONCOLON);
				setState(1198);
				switch (_input.LA(1)) {
				case VISIT:
				case IS_FSYM:
				case IS_SORT:
				case MAKE:
				case MAKE_EMPTY:
				case MAKE_APPEND:
				case MAKE_INSERT:
				case GET_SLOT:
				case GET_DEFAULT:
				case GET_ELEMENT:
				case GET_HEAD:
				case GET_TAIL:
				case GET_SIZE:
				case IS_EMPTY:
				case IMPLEMENT:
				case EQUALS:
				case WHEN:
				case LT:
				case IDENTIFIER:
					{
					setState(1194);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(1193);
						typeArguments();
						}
					}

					setState(1196);
					javaIdentifier();
					}
					break;
				case NEW:
					{
					setState(1197);
					match(NEW);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 9:
				{
				setState(1200);
				classType();
				setState(1201);
				match(COLONCOLON);
				setState(1203);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1202);
					typeArguments();
					}
				}

				setState(1205);
				match(NEW);
				}
				break;
			case 10:
				{
				setState(1207);
				tomTerm();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1303);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,139,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1301);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1210);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(1211);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & ((1L << (MOD - 114)) | (1L << (STAR - 114)) | (1L << (SLASH - 114)))) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1212);
						expression(19);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1213);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(1214);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1215);
						expression(18);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1216);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(1224);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
						case 1:
							{
							setState(1217);
							match(LT);
							setState(1218);
							match(LT);
							}
							break;
						case 2:
							{
							setState(1219);
							match(GT);
							setState(1220);
							match(GT);
							setState(1221);
							match(GT);
							}
							break;
						case 3:
							{
							setState(1222);
							match(GT);
							setState(1223);
							match(GT);
							}
							break;
						}
						setState(1226);
						expression(17);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1227);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(1228);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 98)) & ~0x3f) == 0 && ((1L << (_la - 98)) & ((1L << (GT - 98)) | (1L << (LT - 98)) | (1L << (LE - 98)) | (1L << (GE - 98)))) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1229);
						expression(16);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1230);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(1231);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQUAL || _la==NOTEQUAL) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1232);
						expression(14);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1233);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(1234);
						((ExpressionContext)_localctx).bop = match(BITAND);
						setState(1235);
						expression(13);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1236);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(1237);
						((ExpressionContext)_localctx).bop = match(CARET);
						setState(1238);
						expression(12);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1239);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1240);
						((ExpressionContext)_localctx).bop = match(PIPE);
						setState(1241);
						expression(11);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1242);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1243);
						((ExpressionContext)_localctx).bop = match(AND);
						setState(1244);
						expression(10);
						}
						break;
					case 10:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1245);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1246);
						((ExpressionContext)_localctx).bop = match(OR);
						setState(1247);
						expression(9);
						}
						break;
					case 11:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1248);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1249);
						((ExpressionContext)_localctx).bop = match(QMARK);
						setState(1250);
						expression(0);
						setState(1251);
						match(COLON);
						setState(1252);
						expression(8);
						}
						break;
					case 12:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1254);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1255);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (ASSIGN - 97)) | (1L << (ADD_ASSIGN - 97)) | (1L << (SUB_ASSIGN - 97)) | (1L << (MUL_ASSIGN - 97)) | (1L << (DIV_ASSIGN - 97)) | (1L << (AND_ASSIGN - 97)) | (1L << (OR_ASSIGN - 97)) | (1L << (XOR_ASSIGN - 97)) | (1L << (MOD_ASSIGN - 97)) | (1L << (LSHIFT_ASSIGN - 97)) | (1L << (RSHIFT_ASSIGN - 97)) | (1L << (URSHIFT_ASSIGN - 97)))) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1256);
						expression(6);
						}
						break;
					case 13:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1257);
						if (!(precpred(_ctx, 26))) throw new FailedPredicateException(this, "precpred(_ctx, 26)");
						setState(1258);
						((ExpressionContext)_localctx).bop = match(DOT);
						setState(1276);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
						case 1:
							{
							setState(1259);
							javaIdentifier();
							}
							break;
						case 2:
							{
							setState(1260);
							match(THIS);
							}
							break;
						case 3:
							{
							setState(1261);
							match(NEW);
							setState(1263);
							_la = _input.LA(1);
							if (_la==LT) {
								{
								setState(1262);
								nonWildcardTypeArguments();
								}
							}

							setState(1265);
							innerCreator();
							}
							break;
						case 4:
							{
							setState(1266);
							match(SUPER);
							setState(1267);
							superSuffix();
							}
							break;
						case 5:
							{
							setState(1268);
							explicitGenericInvocation();
							}
							break;
						case 6:
							{
							setState(1269);
							funTerm(0);
							setState(1270);
							match(LPAREN);
							setState(1272);
							_la = _input.LA(1);
							if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
								{
								setState(1271);
								expressionList();
								}
							}

							setState(1274);
							match(RPAREN);
							}
							break;
						}
						}
						break;
					case 14:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1278);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(1279);
						match(LBRACK);
						setState(1280);
						expression(0);
						setState(1281);
						match(RBRACK);
						}
						break;
					case 15:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1283);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(1284);
						((ExpressionContext)_localctx).postfix = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
							((ExpressionContext)_localctx).postfix = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						break;
					case 16:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1285);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(1286);
						((ExpressionContext)_localctx).bop = match(INSTANCEOF);
						setState(1287);
						typeType();
						}
						break;
					case 17:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1288);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1289);
						match(COLONCOLON);
						setState(1291);
						_la = _input.LA(1);
						if (_la==LT) {
							{
							setState(1290);
							typeArguments();
							}
						}

						setState(1293);
						javaIdentifier();
						setState(1299);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
						case 1:
							{
							setState(1294);
							match(LPAREN);
							setState(1296);
							_la = _input.LA(1);
							if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
								{
								setState(1295);
								expressionList();
								}
							}

							setState(1298);
							match(RPAREN);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(1305);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,139,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FunTermContext extends ParserRuleContext {
		public Token postfix;
		public TerminalNode THIS() { return getToken(TomJavaParser.THIS, 0); }
		public TerminalNode SUPER() { return getToken(TomJavaParser.SUPER, 0); }
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
			return getRuleContext(ExplicitGenericInvocationSuffixContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public TerminalNode NEW() { return getToken(TomJavaParser.NEW, 0); }
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public LambdaExpressionContext lambdaExpression() {
			return getRuleContext(LambdaExpressionContext.class,0);
		}
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public ClassTypeContext classType() {
			return getRuleContext(ClassTypeContext.class,0);
		}
		public FunTermContext funTerm() {
			return getRuleContext(FunTermContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public FunTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterFunTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitFunTerm(this);
		}
	}

	public final FunTermContext funTerm() throws RecognitionException {
		return funTerm(0);
	}

	private FunTermContext funTerm(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FunTermContext _localctx = new FunTermContext(_ctx, _parentState);
		FunTermContext _prevctx = _localctx;
		int _startState = 168;
		enterRecursionRule(_localctx, 168, RULE_funTerm, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1335);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
			case 1:
				{
				setState(1307);
				match(THIS);
				}
				break;
			case 2:
				{
				setState(1308);
				match(SUPER);
				}
				break;
			case 3:
				{
				setState(1309);
				javaIdentifier();
				}
				break;
			case 4:
				{
				setState(1310);
				nonWildcardTypeArguments();
				setState(1314);
				switch (_input.LA(1)) {
				case SUPER:
				case VISIT:
				case IS_FSYM:
				case IS_SORT:
				case MAKE:
				case MAKE_EMPTY:
				case MAKE_APPEND:
				case MAKE_INSERT:
				case GET_SLOT:
				case GET_DEFAULT:
				case GET_ELEMENT:
				case GET_HEAD:
				case GET_TAIL:
				case GET_SIZE:
				case IS_EMPTY:
				case IMPLEMENT:
				case EQUALS:
				case WHEN:
				case IDENTIFIER:
					{
					setState(1311);
					explicitGenericInvocationSuffix();
					}
					break;
				case THIS:
					{
					setState(1312);
					match(THIS);
					setState(1313);
					arguments();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 5:
				{
				setState(1316);
				match(NEW);
				setState(1317);
				creator();
				}
				break;
			case 6:
				{
				setState(1318);
				lambdaExpression();
				}
				break;
			case 7:
				{
				setState(1319);
				typeType();
				setState(1320);
				match(COLONCOLON);
				setState(1326);
				switch (_input.LA(1)) {
				case VISIT:
				case IS_FSYM:
				case IS_SORT:
				case MAKE:
				case MAKE_EMPTY:
				case MAKE_APPEND:
				case MAKE_INSERT:
				case GET_SLOT:
				case GET_DEFAULT:
				case GET_ELEMENT:
				case GET_HEAD:
				case GET_TAIL:
				case GET_SIZE:
				case IS_EMPTY:
				case IMPLEMENT:
				case EQUALS:
				case WHEN:
				case LT:
				case IDENTIFIER:
					{
					setState(1322);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(1321);
						typeArguments();
						}
					}

					setState(1324);
					javaIdentifier();
					}
					break;
				case NEW:
					{
					setState(1325);
					match(NEW);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 8:
				{
				setState(1328);
				classType();
				setState(1329);
				match(COLONCOLON);
				setState(1331);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1330);
					typeArguments();
					}
				}

				setState(1333);
				match(NEW);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1352);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,147,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1350);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,146,_ctx) ) {
					case 1:
						{
						_localctx = new FunTermContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_funTerm);
						setState(1337);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1338);
						match(LBRACK);
						setState(1339);
						expression(0);
						setState(1340);
						match(RBRACK);
						}
						break;
					case 2:
						{
						_localctx = new FunTermContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_funTerm);
						setState(1342);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1343);
						match(LPAREN);
						setState(1345);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
							{
							setState(1344);
							expressionList();
							}
						}

						setState(1347);
						match(RPAREN);
						}
						break;
					case 3:
						{
						_localctx = new FunTermContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_funTerm);
						setState(1348);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1349);
						((FunTermContext)_localctx).postfix = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
							((FunTermContext)_localctx).postfix = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(1354);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,147,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class LambdaExpressionContext extends ParserRuleContext {
		public LambdaParametersContext lambdaParameters() {
			return getRuleContext(LambdaParametersContext.class,0);
		}
		public LambdaBodyContext lambdaBody() {
			return getRuleContext(LambdaBodyContext.class,0);
		}
		public LambdaExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterLambdaExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitLambdaExpression(this);
		}
	}

	public final LambdaExpressionContext lambdaExpression() throws RecognitionException {
		LambdaExpressionContext _localctx = new LambdaExpressionContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_lambdaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1355);
			lambdaParameters();
			setState(1356);
			match(ARROW);
			setState(1357);
			lambdaBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LambdaParametersContext extends ParserRuleContext {
		public List<JavaIdentifierContext> javaIdentifier() {
			return getRuleContexts(JavaIdentifierContext.class);
		}
		public JavaIdentifierContext javaIdentifier(int i) {
			return getRuleContext(JavaIdentifierContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public FormalParameterListContext formalParameterList() {
			return getRuleContext(FormalParameterListContext.class,0);
		}
		public LambdaParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterLambdaParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitLambdaParameters(this);
		}
	}

	public final LambdaParametersContext lambdaParameters() throws RecognitionException {
		LambdaParametersContext _localctx = new LambdaParametersContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_lambdaParameters);
		int _la;
		try {
			setState(1376);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1359);
				javaIdentifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1360);
				match(LPAREN);
				setState(1362);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << SHORT) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
					{
					setState(1361);
					formalParameterList();
					}
				}

				setState(1364);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1365);
				match(LPAREN);
				setState(1366);
				javaIdentifier();
				setState(1371);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1367);
					match(COMMA);
					setState(1368);
					javaIdentifier();
					}
					}
					setState(1373);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1374);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LambdaBodyContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public LambdaBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterLambdaBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitLambdaBody(this);
		}
	}

	public final LambdaBodyContext lambdaBody() throws RecognitionException {
		LambdaBodyContext _localctx = new LambdaBodyContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_lambdaBody);
		try {
			setState(1380);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case NEW:
			case SHORT:
			case SUPER:
			case THIS:
			case VOID:
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case LPAREN:
			case LT:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case ANTI:
			case BQUOTE:
			case METAQUOTE:
			case AT:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1378);
				expression(0);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1379);
				block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimaryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TerminalNode THIS() { return getToken(TomJavaParser.THIS, 0); }
		public TerminalNode SUPER() { return getToken(TomJavaParser.SUPER, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public TypeTypeOrVoidContext typeTypeOrVoid() {
			return getRuleContext(TypeTypeOrVoidContext.class,0);
		}
		public TerminalNode CLASS() { return getToken(TomJavaParser.CLASS, 0); }
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
			return getRuleContext(ExplicitGenericInvocationSuffixContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPrimary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPrimary(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_primary);
		try {
			setState(1400);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1382);
				match(LPAREN);
				setState(1383);
				expression(0);
				setState(1384);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1386);
				match(THIS);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1387);
				match(SUPER);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1388);
				literal();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1389);
				javaIdentifier();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1390);
				typeTypeOrVoid();
				setState(1391);
				match(DOT);
				setState(1392);
				match(CLASS);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1394);
				nonWildcardTypeArguments();
				setState(1398);
				switch (_input.LA(1)) {
				case SUPER:
				case VISIT:
				case IS_FSYM:
				case IS_SORT:
				case MAKE:
				case MAKE_EMPTY:
				case MAKE_APPEND:
				case MAKE_INSERT:
				case GET_SLOT:
				case GET_DEFAULT:
				case GET_ELEMENT:
				case GET_HEAD:
				case GET_TAIL:
				case GET_SIZE:
				case IS_EMPTY:
				case IMPLEMENT:
				case EQUALS:
				case WHEN:
				case IDENTIFIER:
					{
					setState(1395);
					explicitGenericInvocationSuffix();
					}
					break;
				case THIS:
					{
					setState(1396);
					match(THIS);
					setState(1397);
					arguments();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassTypeContext extends ParserRuleContext {
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return getRuleContext(ClassOrInterfaceTypeContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public ClassTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterClassType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitClassType(this);
		}
	}

	public final ClassTypeContext classType() throws RecognitionException {
		ClassTypeContext _localctx = new ClassTypeContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_classType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1405);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
			case 1:
				{
				setState(1402);
				classOrInterfaceType();
				setState(1403);
				match(DOT);
				}
				break;
			}
			setState(1410);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(1407);
				annotation();
				}
				}
				setState(1412);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1413);
			javaIdentifier();
			setState(1415);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(1414);
				typeArguments();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreatorContext extends ParserRuleContext {
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public CreatedNameContext createdName() {
			return getRuleContext(CreatedNameContext.class,0);
		}
		public ClassCreatorRestContext classCreatorRest() {
			return getRuleContext(ClassCreatorRestContext.class,0);
		}
		public ArrayCreatorRestContext arrayCreatorRest() {
			return getRuleContext(ArrayCreatorRestContext.class,0);
		}
		public CreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterCreator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitCreator(this);
		}
	}

	public final CreatorContext creator() throws RecognitionException {
		CreatorContext _localctx = new CreatorContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_creator);
		try {
			setState(1426);
			switch (_input.LA(1)) {
			case LT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1417);
				nonWildcardTypeArguments();
				setState(1418);
				createdName();
				setState(1419);
				classCreatorRest();
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(1421);
				createdName();
				setState(1424);
				switch (_input.LA(1)) {
				case LBRACK:
					{
					setState(1422);
					arrayCreatorRest();
					}
					break;
				case LPAREN:
					{
					setState(1423);
					classCreatorRest();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreatedNameContext extends ParserRuleContext {
		public List<JavaIdentifierContext> javaIdentifier() {
			return getRuleContexts(JavaIdentifierContext.class);
		}
		public JavaIdentifierContext javaIdentifier(int i) {
			return getRuleContext(JavaIdentifierContext.class,i);
		}
		public List<TypeArgumentsOrDiamondContext> typeArgumentsOrDiamond() {
			return getRuleContexts(TypeArgumentsOrDiamondContext.class);
		}
		public TypeArgumentsOrDiamondContext typeArgumentsOrDiamond(int i) {
			return getRuleContext(TypeArgumentsOrDiamondContext.class,i);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public CreatedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createdName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterCreatedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitCreatedName(this);
		}
	}

	public final CreatedNameContext createdName() throws RecognitionException {
		CreatedNameContext _localctx = new CreatedNameContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_createdName);
		int _la;
		try {
			setState(1443);
			switch (_input.LA(1)) {
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1428);
				javaIdentifier();
				setState(1430);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1429);
					typeArgumentsOrDiamond();
					}
				}

				setState(1439);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DOT) {
					{
					{
					setState(1432);
					match(DOT);
					setState(1433);
					javaIdentifier();
					setState(1435);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(1434);
						typeArgumentsOrDiamond();
						}
					}

					}
					}
					setState(1441);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1442);
				primitiveType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InnerCreatorContext extends ParserRuleContext {
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public ClassCreatorRestContext classCreatorRest() {
			return getRuleContext(ClassCreatorRestContext.class,0);
		}
		public NonWildcardTypeArgumentsOrDiamondContext nonWildcardTypeArgumentsOrDiamond() {
			return getRuleContext(NonWildcardTypeArgumentsOrDiamondContext.class,0);
		}
		public InnerCreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_innerCreator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterInnerCreator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitInnerCreator(this);
		}
	}

	public final InnerCreatorContext innerCreator() throws RecognitionException {
		InnerCreatorContext _localctx = new InnerCreatorContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_innerCreator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1445);
			javaIdentifier();
			setState(1447);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(1446);
				nonWildcardTypeArgumentsOrDiamond();
				}
			}

			setState(1449);
			classCreatorRest();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayCreatorRestContext extends ParserRuleContext {
		public ArrayInitializerContext arrayInitializer() {
			return getRuleContext(ArrayInitializerContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreatorRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterArrayCreatorRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitArrayCreatorRest(this);
		}
	}

	public final ArrayCreatorRestContext arrayCreatorRest() throws RecognitionException {
		ArrayCreatorRestContext _localctx = new ArrayCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_arrayCreatorRest);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1451);
			match(LBRACK);
			setState(1479);
			switch (_input.LA(1)) {
			case RBRACK:
				{
				setState(1452);
				match(RBRACK);
				setState(1457);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LBRACK) {
					{
					{
					setState(1453);
					match(LBRACK);
					setState(1454);
					match(RBRACK);
					}
					}
					setState(1459);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1460);
				arrayInitializer();
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case NEW:
			case SHORT:
			case SUPER:
			case THIS:
			case VOID:
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case LPAREN:
			case LT:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case ANTI:
			case BQUOTE:
			case METAQUOTE:
			case AT:
			case IDENTIFIER:
				{
				setState(1461);
				expression(0);
				setState(1462);
				match(RBRACK);
				setState(1469);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,165,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1463);
						match(LBRACK);
						setState(1464);
						expression(0);
						setState(1465);
						match(RBRACK);
						}
						} 
					}
					setState(1471);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,165,_ctx);
				}
				setState(1476);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,166,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1472);
						match(LBRACK);
						setState(1473);
						match(RBRACK);
						}
						} 
					}
					setState(1478);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,166,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassCreatorRestContext extends ParserRuleContext {
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public ClassCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classCreatorRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterClassCreatorRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitClassCreatorRest(this);
		}
	}

	public final ClassCreatorRestContext classCreatorRest() throws RecognitionException {
		ClassCreatorRestContext _localctx = new ClassCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_classCreatorRest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1481);
			arguments();
			setState(1483);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,168,_ctx) ) {
			case 1:
				{
				setState(1482);
				classBody();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplicitGenericInvocationContext extends ParserRuleContext {
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
			return getRuleContext(ExplicitGenericInvocationSuffixContext.class,0);
		}
		public ExplicitGenericInvocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitGenericInvocation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterExplicitGenericInvocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitExplicitGenericInvocation(this);
		}
	}

	public final ExplicitGenericInvocationContext explicitGenericInvocation() throws RecognitionException {
		ExplicitGenericInvocationContext _localctx = new ExplicitGenericInvocationContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_explicitGenericInvocation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1485);
			nonWildcardTypeArguments();
			setState(1486);
			explicitGenericInvocationSuffix();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeArgumentsOrDiamondContext extends ParserRuleContext {
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public TypeArgumentsOrDiamondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArgumentsOrDiamond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeArgumentsOrDiamond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeArgumentsOrDiamond(this);
		}
	}

	public final TypeArgumentsOrDiamondContext typeArgumentsOrDiamond() throws RecognitionException {
		TypeArgumentsOrDiamondContext _localctx = new TypeArgumentsOrDiamondContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_typeArgumentsOrDiamond);
		try {
			setState(1491);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,169,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1488);
				match(LT);
				setState(1489);
				match(GT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1490);
				typeArguments();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonWildcardTypeArgumentsOrDiamondContext extends ParserRuleContext {
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public NonWildcardTypeArgumentsOrDiamondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonWildcardTypeArgumentsOrDiamond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterNonWildcardTypeArgumentsOrDiamond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitNonWildcardTypeArgumentsOrDiamond(this);
		}
	}

	public final NonWildcardTypeArgumentsOrDiamondContext nonWildcardTypeArgumentsOrDiamond() throws RecognitionException {
		NonWildcardTypeArgumentsOrDiamondContext _localctx = new NonWildcardTypeArgumentsOrDiamondContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_nonWildcardTypeArgumentsOrDiamond);
		try {
			setState(1496);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,170,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1493);
				match(LT);
				setState(1494);
				match(GT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1495);
				nonWildcardTypeArguments();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonWildcardTypeArgumentsContext extends ParserRuleContext {
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public NonWildcardTypeArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonWildcardTypeArguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterNonWildcardTypeArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitNonWildcardTypeArguments(this);
		}
	}

	public final NonWildcardTypeArgumentsContext nonWildcardTypeArguments() throws RecognitionException {
		NonWildcardTypeArgumentsContext _localctx = new NonWildcardTypeArgumentsContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_nonWildcardTypeArguments);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1498);
			match(LT);
			setState(1499);
			typeList();
			setState(1500);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeListContext extends ParserRuleContext {
		public List<TypeTypeContext> typeType() {
			return getRuleContexts(TypeTypeContext.class);
		}
		public TypeTypeContext typeType(int i) {
			return getRuleContext(TypeTypeContext.class,i);
		}
		public TypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeList(this);
		}
	}

	public final TypeListContext typeList() throws RecognitionException {
		TypeListContext _localctx = new TypeListContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1502);
			typeType();
			setState(1507);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1503);
				match(COMMA);
				setState(1504);
				typeType();
				}
				}
				setState(1509);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeTypeContext extends ParserRuleContext {
		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return getRuleContext(ClassOrInterfaceTypeContext.class,0);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public TypeTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeType(this);
		}
	}

	public final TypeTypeContext typeType() throws RecognitionException {
		TypeTypeContext _localctx = new TypeTypeContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_typeType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1511);
			_la = _input.LA(1);
			if (_la==AT) {
				{
				setState(1510);
				annotation();
				}
			}

			setState(1515);
			switch (_input.LA(1)) {
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case IDENTIFIER:
				{
				setState(1513);
				classOrInterfaceType();
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
				{
				setState(1514);
				primitiveType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1521);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,174,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1517);
					match(LBRACK);
					setState(1518);
					match(RBRACK);
					}
					} 
				}
				setState(1523);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,174,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimitiveTypeContext extends ParserRuleContext {
		public TerminalNode BOOLEAN() { return getToken(TomJavaParser.BOOLEAN, 0); }
		public TerminalNode CHAR() { return getToken(TomJavaParser.CHAR, 0); }
		public TerminalNode BYTE() { return getToken(TomJavaParser.BYTE, 0); }
		public TerminalNode SHORT() { return getToken(TomJavaParser.SHORT, 0); }
		public TerminalNode INT() { return getToken(TomJavaParser.INT, 0); }
		public TerminalNode LONG() { return getToken(TomJavaParser.LONG, 0); }
		public TerminalNode FLOAT() { return getToken(TomJavaParser.FLOAT, 0); }
		public TerminalNode DOUBLE() { return getToken(TomJavaParser.DOUBLE, 0); }
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPrimitiveType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPrimitiveType(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1524);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << SHORT))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeArgumentsContext extends ParserRuleContext {
		public List<TypeArgumentContext> typeArgument() {
			return getRuleContexts(TypeArgumentContext.class);
		}
		public TypeArgumentContext typeArgument(int i) {
			return getRuleContext(TypeArgumentContext.class,i);
		}
		public TypeArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeArguments(this);
		}
	}

	public final TypeArgumentsContext typeArguments() throws RecognitionException {
		TypeArgumentsContext _localctx = new TypeArgumentsContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_typeArguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1526);
			match(LT);
			setState(1527);
			typeArgument();
			setState(1532);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1528);
				match(COMMA);
				setState(1529);
				typeArgument();
				}
				}
				setState(1534);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1535);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SuperSuffixContext extends ParserRuleContext {
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public SuperSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_superSuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterSuperSuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitSuperSuffix(this);
		}
	}

	public final SuperSuffixContext superSuffix() throws RecognitionException {
		SuperSuffixContext _localctx = new SuperSuffixContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_superSuffix);
		try {
			setState(1543);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(1537);
				arguments();
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1538);
				match(DOT);
				setState(1539);
				javaIdentifier();
				setState(1541);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,176,_ctx) ) {
				case 1:
					{
					setState(1540);
					arguments();
					}
					break;
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplicitGenericInvocationSuffixContext extends ParserRuleContext {
		public TerminalNode SUPER() { return getToken(TomJavaParser.SUPER, 0); }
		public SuperSuffixContext superSuffix() {
			return getRuleContext(SuperSuffixContext.class,0);
		}
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitGenericInvocationSuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterExplicitGenericInvocationSuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitExplicitGenericInvocationSuffix(this);
		}
	}

	public final ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() throws RecognitionException {
		ExplicitGenericInvocationSuffixContext _localctx = new ExplicitGenericInvocationSuffixContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_explicitGenericInvocationSuffix);
		try {
			setState(1550);
			switch (_input.LA(1)) {
			case SUPER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1545);
				match(SUPER);
				setState(1546);
				superSuffix();
				}
				break;
			case VISIT:
			case IS_FSYM:
			case IS_SORT:
			case MAKE:
			case MAKE_EMPTY:
			case MAKE_APPEND:
			case MAKE_INSERT:
			case GET_SLOT:
			case GET_DEFAULT:
			case GET_ELEMENT:
			case GET_HEAD:
			case GET_TAIL:
			case GET_SIZE:
			case IS_EMPTY:
			case IMPLEMENT:
			case EQUALS:
			case WHEN:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(1547);
				javaIdentifier();
				setState(1548);
				arguments();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitArguments(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1552);
			match(LPAREN);
			setState(1554);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (BQUOTE - 128)) | (1L << (METAQUOTE - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
				{
				setState(1553);
				expressionList();
				}
			}

			setState(1556);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JavaIdentifierContext extends ParserRuleContext {
		public TerminalNode IS_FSYM() { return getToken(TomJavaParser.IS_FSYM, 0); }
		public TerminalNode IS_SORT() { return getToken(TomJavaParser.IS_SORT, 0); }
		public TerminalNode MAKE() { return getToken(TomJavaParser.MAKE, 0); }
		public TerminalNode MAKE_EMPTY() { return getToken(TomJavaParser.MAKE_EMPTY, 0); }
		public TerminalNode MAKE_APPEND() { return getToken(TomJavaParser.MAKE_APPEND, 0); }
		public TerminalNode MAKE_INSERT() { return getToken(TomJavaParser.MAKE_INSERT, 0); }
		public TerminalNode GET_SLOT() { return getToken(TomJavaParser.GET_SLOT, 0); }
		public TerminalNode GET_DEFAULT() { return getToken(TomJavaParser.GET_DEFAULT, 0); }
		public TerminalNode GET_ELEMENT() { return getToken(TomJavaParser.GET_ELEMENT, 0); }
		public TerminalNode GET_HEAD() { return getToken(TomJavaParser.GET_HEAD, 0); }
		public TerminalNode GET_TAIL() { return getToken(TomJavaParser.GET_TAIL, 0); }
		public TerminalNode GET_SIZE() { return getToken(TomJavaParser.GET_SIZE, 0); }
		public TerminalNode IS_EMPTY() { return getToken(TomJavaParser.IS_EMPTY, 0); }
		public TerminalNode IMPLEMENT() { return getToken(TomJavaParser.IMPLEMENT, 0); }
		public TerminalNode EQUALS() { return getToken(TomJavaParser.EQUALS, 0); }
		public TerminalNode VISIT() { return getToken(TomJavaParser.VISIT, 0); }
		public TerminalNode WHEN() { return getToken(TomJavaParser.WHEN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(TomJavaParser.IDENTIFIER, 0); }
		public JavaIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_javaIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterJavaIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitJavaIdentifier(this);
		}
	}

	public final JavaIdentifierContext javaIdentifier() throws RecognitionException {
		JavaIdentifierContext _localctx = new JavaIdentifierContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_javaIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1558);
			_la = _input.LA(1);
			if ( !(((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (VISIT - 60)) | (1L << (IS_FSYM - 60)) | (1L << (IS_SORT - 60)) | (1L << (MAKE - 60)) | (1L << (MAKE_EMPTY - 60)) | (1L << (MAKE_APPEND - 60)) | (1L << (MAKE_INSERT - 60)) | (1L << (GET_SLOT - 60)) | (1L << (GET_DEFAULT - 60)) | (1L << (GET_ELEMENT - 60)) | (1L << (GET_HEAD - 60)) | (1L << (GET_TAIL - 60)) | (1L << (GET_SIZE - 60)) | (1L << (IS_EMPTY - 60)) | (1L << (IMPLEMENT - 60)) | (1L << (EQUALS - 60)) | (1L << (WHEN - 60)))) != 0) || _la==IDENTIFIER) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TomDeclarationContext extends ParserRuleContext {
		public StrategyStatementContext strategyStatement() {
			return getRuleContext(StrategyStatementContext.class,0);
		}
		public IncludeStatementContext includeStatement() {
			return getRuleContext(IncludeStatementContext.class,0);
		}
		public GomStatementContext gomStatement() {
			return getRuleContext(GomStatementContext.class,0);
		}
		public RuleStatementContext ruleStatement() {
			return getRuleContext(RuleStatementContext.class,0);
		}
		public TypetermContext typeterm() {
			return getRuleContext(TypetermContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public OplistContext oplist() {
			return getRuleContext(OplistContext.class,0);
		}
		public OparrayContext oparray() {
			return getRuleContext(OparrayContext.class,0);
		}
		public TomDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tomDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTomDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTomDeclaration(this);
		}
	}

	public final TomDeclarationContext tomDeclaration() throws RecognitionException {
		TomDeclarationContext _localctx = new TomDeclarationContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_tomDeclaration);
		try {
			setState(1568);
			switch (_input.LA(1)) {
			case STRATEGY:
				enterOuterAlt(_localctx, 1);
				{
				setState(1560);
				strategyStatement();
				}
				break;
			case INCLUDE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1561);
				includeStatement();
				}
				break;
			case GOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(1562);
				gomStatement();
				}
				break;
			case RULE:
				enterOuterAlt(_localctx, 4);
				{
				setState(1563);
				ruleStatement();
				}
				break;
			case TYPETERM:
				enterOuterAlt(_localctx, 5);
				{
				setState(1564);
				typeterm();
				}
				break;
			case OP:
				enterOuterAlt(_localctx, 6);
				{
				setState(1565);
				operator();
				}
				break;
			case OPLIST:
				enterOuterAlt(_localctx, 7);
				{
				setState(1566);
				oplist();
				}
				break;
			case OPARRAY:
				enterOuterAlt(_localctx, 8);
				{
				setState(1567);
				oparray();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TomStatementContext extends ParserRuleContext {
		public MatchStatementContext matchStatement() {
			return getRuleContext(MatchStatementContext.class,0);
		}
		public TomStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tomStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTomStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTomStatement(this);
		}
	}

	public final TomStatementContext tomStatement() throws RecognitionException {
		TomStatementContext _localctx = new TomStatementContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_tomStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1570);
			matchStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TomTermContext extends ParserRuleContext {
		public BqcompositeContext bqcomposite() {
			return getRuleContext(BqcompositeContext.class,0);
		}
		public MetaquoteContext metaquote() {
			return getRuleContext(MetaquoteContext.class,0);
		}
		public TomTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tomTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTomTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTomTerm(this);
		}
	}

	public final TomTermContext tomTerm() throws RecognitionException {
		TomTermContext _localctx = new TomTermContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_tomTerm);
		try {
			setState(1574);
			switch (_input.LA(1)) {
			case BQUOTE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1572);
				bqcomposite();
				}
				break;
			case METAQUOTE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1573);
				metaquote();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MetaquoteContext extends ParserRuleContext {
		public TerminalNode METAQUOTE() { return getToken(TomJavaParser.METAQUOTE, 0); }
		public MetaquoteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metaquote; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMetaquote(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMetaquote(this);
		}
	}

	public final MetaquoteContext metaquote() throws RecognitionException {
		MetaquoteContext _localctx = new MetaquoteContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_metaquote);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1576);
			match(METAQUOTE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchStatementContext extends ParserRuleContext {
		public TerminalNode MATCH() { return getToken(TomJavaParser.MATCH, 0); }
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public List<ActionRuleContext> actionRule() {
			return getRuleContexts(ActionRuleContext.class);
		}
		public ActionRuleContext actionRule(int i) {
			return getRuleContext(ActionRuleContext.class,i);
		}
		public List<BqtermContext> bqterm() {
			return getRuleContexts(BqtermContext.class);
		}
		public BqtermContext bqterm(int i) {
			return getRuleContext(BqtermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public MatchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMatchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMatchStatement(this);
		}
	}

	public final MatchStatementContext matchStatement() throws RecognitionException {
		MatchStatementContext _localctx = new MatchStatementContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_matchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1578);
			match(MATCH);
			setState(1591);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(1579);
				match(LPAREN);
				setState(1588);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (BQUOTE - 77)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1580);
					bqterm();
					setState(1585);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1581);
						match(COMMA);
						setState(1582);
						bqterm();
						}
						}
						setState(1587);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1590);
				match(RPAREN);
				}
			}

			setState(1593);
			match(LBRACE);
			setState(1597);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (LPAREN - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (ANTI - 77)))) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(1594);
				actionRule();
				}
				}
				setState(1599);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1600);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StrategyStatementContext extends ParserRuleContext {
		public TerminalNode STRATEGY() { return getToken(TomJavaParser.STRATEGY, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TerminalNode EXTENDS() { return getToken(TomJavaParser.EXTENDS, 0); }
		public BqtermContext bqterm() {
			return getRuleContext(BqtermContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public SlotListContext slotList() {
			return getRuleContext(SlotListContext.class,0);
		}
		public List<VisitContext> visit() {
			return getRuleContexts(VisitContext.class);
		}
		public VisitContext visit(int i) {
			return getRuleContext(VisitContext.class,i);
		}
		public StrategyStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_strategyStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterStrategyStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitStrategyStatement(this);
		}
	}

	public final StrategyStatementContext strategyStatement() throws RecognitionException {
		StrategyStatementContext _localctx = new StrategyStatementContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_strategyStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1602);
			match(STRATEGY);
			setState(1603);
			tomIdentifier();
			setState(1604);
			match(LPAREN);
			setState(1606);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
				{
				setState(1605);
				slotList();
				}
			}

			setState(1608);
			match(RPAREN);
			setState(1609);
			match(EXTENDS);
			setState(1610);
			bqterm();
			setState(1611);
			match(LBRACE);
			setState(1615);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VISIT) {
				{
				{
				setState(1612);
				visit();
				}
				}
				setState(1617);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1618);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IncludeStatementContext extends ParserRuleContext {
		public TerminalNode INCLUDE() { return getToken(TomJavaParser.INCLUDE, 0); }
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<TerminalNode> SLASH() { return getTokens(TomJavaParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(TomJavaParser.SLASH, i);
		}
		public List<TerminalNode> DOT() { return getTokens(TomJavaParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(TomJavaParser.DOT, i);
		}
		public List<TerminalNode> BACKSLASH() { return getTokens(TomJavaParser.BACKSLASH); }
		public TerminalNode BACKSLASH(int i) {
			return getToken(TomJavaParser.BACKSLASH, i);
		}
		public IncludeStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_includeStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterIncludeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitIncludeStatement(this);
		}
	}

	public final IncludeStatementContext includeStatement() throws RecognitionException {
		IncludeStatementContext _localctx = new IncludeStatementContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_includeStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1620);
			match(INCLUDE);
			setState(1621);
			match(LBRACE);
			setState(1631);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT || _la==SLASH) {
				{
				{
				setState(1625);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DOT) {
					{
					{
					setState(1622);
					match(DOT);
					}
					}
					setState(1627);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1628);
				match(SLASH);
				}
				}
				setState(1633);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1634);
			tomIdentifier();
			setState(1639);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (DOT - 96)) | (1L << (SLASH - 96)) | (1L << (BACKSLASH - 96)))) != 0)) {
				{
				{
				setState(1635);
				_la = _input.LA(1);
				if ( !(((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (DOT - 96)) | (1L << (SLASH - 96)) | (1L << (BACKSLASH - 96)))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1636);
				tomIdentifier();
				}
				}
				setState(1641);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1642);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GomStatementContext extends ParserRuleContext {
		public TerminalNode GOM() { return getToken(TomJavaParser.GOM, 0); }
		public UnknownBlockContext unknownBlock() {
			return getRuleContext(UnknownBlockContext.class,0);
		}
		public GomOptionsContext gomOptions() {
			return getRuleContext(GomOptionsContext.class,0);
		}
		public GomStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gomStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGomStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGomStatement(this);
		}
	}

	public final GomStatementContext gomStatement() throws RecognitionException {
		GomStatementContext _localctx = new GomStatementContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_gomStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1644);
			match(GOM);
			setState(1646);
			_la = _input.LA(1);
			if (_la==OPTIONSTART) {
				{
				setState(1645);
				gomOptions();
				}
			}

			setState(1648);
			unknownBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleStatementContext extends ParserRuleContext {
		public TerminalNode RULE() { return getToken(TomJavaParser.RULE, 0); }
		public UnknownBlockContext unknownBlock() {
			return getRuleContext(UnknownBlockContext.class,0);
		}
		public RuleStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterRuleStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitRuleStatement(this);
		}
	}

	public final RuleStatementContext ruleStatement() throws RecognitionException {
		RuleStatementContext _localctx = new RuleStatementContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_ruleStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1650);
			match(RULE);
			setState(1651);
			unknownBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnknownBlockContext extends ParserRuleContext {
		public TerminalNode BLOCKSTART() { return getToken(TomJavaParser.BLOCKSTART, 0); }
		public TerminalNode BLOCKEND() { return getToken(TomJavaParser.BLOCKEND, 0); }
		public List<UnknownBlockContext> unknownBlock() {
			return getRuleContexts(UnknownBlockContext.class);
		}
		public UnknownBlockContext unknownBlock(int i) {
			return getRuleContext(UnknownBlockContext.class,i);
		}
		public List<TerminalNode> ANY() { return getTokens(TomJavaParser.ANY); }
		public TerminalNode ANY(int i) {
			return getToken(TomJavaParser.ANY, i);
		}
		public TerminalNode SUBBLOCKEND() { return getToken(TomJavaParser.SUBBLOCKEND, 0); }
		public TerminalNode SUBBLOCKSTART() { return getToken(TomJavaParser.SUBBLOCKSTART, 0); }
		public TerminalNode SUBSUBBLOCKSTART() { return getToken(TomJavaParser.SUBSUBBLOCKSTART, 0); }
		public List<TerminalNode> SUB_ANY() { return getTokens(TomJavaParser.SUB_ANY); }
		public TerminalNode SUB_ANY(int i) {
			return getToken(TomJavaParser.SUB_ANY, i);
		}
		public UnknownBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unknownBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterUnknownBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitUnknownBlock(this);
		}
	}

	public final UnknownBlockContext unknownBlock() throws RecognitionException {
		UnknownBlockContext _localctx = new UnknownBlockContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_unknownBlock);
		int _la;
		try {
			int _alt;
			setState(1671);
			switch (_input.LA(1)) {
			case BLOCKSTART:
				enterOuterAlt(_localctx, 1);
				{
				setState(1653);
				match(BLOCKSTART);
				setState(1658);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,193,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						setState(1656);
						switch (_input.LA(1)) {
						case BLOCKSTART:
						case SUBBLOCKSTART:
						case SUBSUBBLOCKSTART:
							{
							setState(1654);
							unknownBlock();
							}
							break;
						case ANY:
							{
							setState(1655);
							match(ANY);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						} 
					}
					setState(1660);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,193,_ctx);
				}
				setState(1661);
				match(BLOCKEND);
				}
				break;
			case SUBBLOCKSTART:
			case SUBSUBBLOCKSTART:
				enterOuterAlt(_localctx, 2);
				{
				setState(1662);
				_la = _input.LA(1);
				if ( !(_la==SUBBLOCKSTART || _la==SUBSUBBLOCKSTART) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1667);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,195,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						setState(1665);
						switch (_input.LA(1)) {
						case BLOCKSTART:
						case SUBBLOCKSTART:
						case SUBSUBBLOCKSTART:
							{
							setState(1663);
							unknownBlock();
							}
							break;
						case SUB_ANY:
							{
							setState(1664);
							match(SUB_ANY);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						} 
					}
					setState(1669);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,195,_ctx);
				}
				setState(1670);
				match(SUBBLOCKEND);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GomOptionsContext extends ParserRuleContext {
		public TerminalNode OPTIONSTART() { return getToken(TomJavaParser.OPTIONSTART, 0); }
		public List<TerminalNode> DMINUSID() { return getTokens(TomJavaParser.DMINUSID); }
		public TerminalNode DMINUSID(int i) {
			return getToken(TomJavaParser.DMINUSID, i);
		}
		public TerminalNode OPTIONEND() { return getToken(TomJavaParser.OPTIONEND, 0); }
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public GomOptionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gomOptions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGomOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGomOptions(this);
		}
	}

	public final GomOptionsContext gomOptions() throws RecognitionException {
		GomOptionsContext _localctx = new GomOptionsContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_gomOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1673);
			match(OPTIONSTART);
			setState(1674);
			match(DMINUSID);
			setState(1679);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1675);
				match(COMMA);
				setState(1676);
				match(DMINUSID);
				}
				}
				setState(1681);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1682);
			match(OPTIONEND);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VisitContext extends ParserRuleContext {
		public TerminalNode VISIT() { return getToken(TomJavaParser.VISIT, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<ActionRuleContext> actionRule() {
			return getRuleContexts(ActionRuleContext.class);
		}
		public ActionRuleContext actionRule(int i) {
			return getRuleContext(ActionRuleContext.class,i);
		}
		public VisitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_visit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterVisit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitVisit(this);
		}
	}

	public final VisitContext visit() throws RecognitionException {
		VisitContext _localctx = new VisitContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_visit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1684);
			match(VISIT);
			setState(1685);
			tomIdentifier();
			setState(1686);
			match(LBRACE);
			setState(1690);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (LPAREN - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (ANTI - 77)))) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(1687);
				actionRule();
				}
				}
				setState(1692);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1693);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionRuleContext extends ParserRuleContext {
		public ConstraintContext c;
		public PatternlistContext patternlist() {
			return getRuleContext(PatternlistContext.class,0);
		}
		public TerminalNode ARROW() { return getToken(TomJavaParser.ARROW, 0); }
		public TomBlockContext tomBlock() {
			return getRuleContext(TomBlockContext.class,0);
		}
		public ConstraintContext constraint() {
			return getRuleContext(ConstraintContext.class,0);
		}
		public TerminalNode AND() { return getToken(TomJavaParser.AND, 0); }
		public TerminalNode OR() { return getToken(TomJavaParser.OR, 0); }
		public BqtermContext bqterm() {
			return getRuleContext(BqtermContext.class,0);
		}
		public ActionRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterActionRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitActionRule(this);
		}
	}

	public final ActionRuleContext actionRule() throws RecognitionException {
		ActionRuleContext _localctx = new ActionRuleContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_actionRule);
		int _la;
		try {
			setState(1719);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,201,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1695);
				patternlist();
				setState(1698);
				_la = _input.LA(1);
				if (_la==AND || _la==OR) {
					{
					setState(1696);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(1697);
					constraint(0);
					}
				}

				setState(1700);
				match(ARROW);
				setState(1701);
				tomBlock();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1703);
				patternlist();
				setState(1706);
				_la = _input.LA(1);
				if (_la==AND || _la==OR) {
					{
					setState(1704);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(1705);
					constraint(0);
					}
				}

				setState(1708);
				match(ARROW);
				setState(1709);
				bqterm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1711);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(1712);
				match(ARROW);
				setState(1713);
				tomBlock();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1715);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(1716);
				match(ARROW);
				setState(1717);
				bqterm();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TomBlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<TomBlockContext> tomBlock() {
			return getRuleContexts(TomBlockContext.class);
		}
		public TomBlockContext tomBlock(int i) {
			return getRuleContext(TomBlockContext.class,i);
		}
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public TomBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tomBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTomBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTomBlock(this);
		}
	}

	public final TomBlockContext tomBlock() throws RecognitionException {
		TomBlockContext _localctx = new TomBlockContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_tomBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1721);
			match(LBRACE);
			setState(1726);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,203,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(1724);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,202,_ctx) ) {
					case 1:
						{
						setState(1722);
						tomBlock();
						}
						break;
					case 2:
						{
						setState(1723);
						blockStatement();
						}
						break;
					}
					} 
				}
				setState(1728);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,203,_ctx);
			}
			setState(1729);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SlotListContext extends ParserRuleContext {
		public List<SlotContext> slot() {
			return getRuleContexts(SlotContext.class);
		}
		public SlotContext slot(int i) {
			return getRuleContext(SlotContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public SlotListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slotList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterSlotList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitSlotList(this);
		}
	}

	public final SlotListContext slotList() throws RecognitionException {
		SlotListContext _localctx = new SlotListContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_slotList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1731);
			slot();
			setState(1736);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1732);
				match(COMMA);
				setState(1733);
				slot();
				}
				}
				setState(1738);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SlotContext extends ParserRuleContext {
		public TomIdentifierContext id1;
		public TomIdentifierContext id2;
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public TerminalNode COLON() { return getToken(TomJavaParser.COLON, 0); }
		public SlotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterSlot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitSlot(this);
		}
	}

	public final SlotContext slot() throws RecognitionException {
		SlotContext _localctx = new SlotContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_slot);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1739);
			((SlotContext)_localctx).id1 = tomIdentifier();
			setState(1741);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(1740);
				match(COLON);
				}
			}

			setState(1743);
			((SlotContext)_localctx).id2 = tomIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PatternlistContext extends ParserRuleContext {
		public List<PatternContext> pattern() {
			return getRuleContexts(PatternContext.class);
		}
		public PatternContext pattern(int i) {
			return getRuleContext(PatternContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public TerminalNode WHEN() { return getToken(TomJavaParser.WHEN, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> LPAREN() { return getTokens(TomJavaParser.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(TomJavaParser.LPAREN, i);
		}
		public List<TerminalNode> RPAREN() { return getTokens(TomJavaParser.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(TomJavaParser.RPAREN, i);
		}
		public PatternlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_patternlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPatternlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPatternlist(this);
		}
	}

	public final PatternlistContext patternlist() throws RecognitionException {
		PatternlistContext _localctx = new PatternlistContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_patternlist);
		int _la;
		try {
			setState(1788);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,212,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1745);
				pattern();
				setState(1750);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1746);
					match(COMMA);
					setState(1747);
					pattern();
					}
					}
					setState(1752);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1762);
				_la = _input.LA(1);
				if (_la==WHEN) {
					{
					setState(1753);
					match(WHEN);
					setState(1754);
					term();
					setState(1759);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1755);
						match(COMMA);
						setState(1756);
						term();
						}
						}
						setState(1761);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1764);
				match(LPAREN);
				setState(1765);
				pattern();
				setState(1770);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1766);
					match(COMMA);
					setState(1767);
					pattern();
					}
					}
					setState(1772);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1773);
				match(RPAREN);
				setState(1786);
				_la = _input.LA(1);
				if (_la==WHEN) {
					{
					setState(1774);
					match(WHEN);
					setState(1775);
					match(LPAREN);
					setState(1776);
					term();
					setState(1781);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1777);
						match(COMMA);
						setState(1778);
						term();
						}
						}
						setState(1783);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(1784);
					match(RPAREN);
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstraintContext extends ParserRuleContext {
		public Token match_symbol;
		public ConstraintContext c;
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public BqtermContext bqterm() {
			return getRuleContext(BqtermContext.class,0);
		}
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public TerminalNode GT() { return getToken(TomJavaParser.GT, 0); }
		public TerminalNode GE() { return getToken(TomJavaParser.GE, 0); }
		public TerminalNode LT() { return getToken(TomJavaParser.LT, 0); }
		public TerminalNode LE() { return getToken(TomJavaParser.LE, 0); }
		public TerminalNode EQUAL() { return getToken(TomJavaParser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(TomJavaParser.NOTEQUAL, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public List<ConstraintContext> constraint() {
			return getRuleContexts(ConstraintContext.class);
		}
		public ConstraintContext constraint(int i) {
			return getRuleContext(ConstraintContext.class,i);
		}
		public TerminalNode AND() { return getToken(TomJavaParser.AND, 0); }
		public TerminalNode OR() { return getToken(TomJavaParser.OR, 0); }
		public ConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitConstraint(this);
		}
	}

	public final ConstraintContext constraint() throws RecognitionException {
		return constraint(0);
	}

	private ConstraintContext constraint(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ConstraintContext _localctx = new ConstraintContext(_ctx, _parentState);
		ConstraintContext _prevctx = _localctx;
		int _startState = 248;
		enterRecursionRule(_localctx, 248, RULE_constraint, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1824);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,213,_ctx) ) {
			case 1:
				{
				setState(1791);
				pattern();
				setState(1792);
				((ConstraintContext)_localctx).match_symbol = match(LT);
				setState(1793);
				match(LT);
				setState(1794);
				bqterm();
				}
				break;
			case 2:
				{
				setState(1796);
				term();
				setState(1797);
				match(GT);
				setState(1798);
				term();
				}
				break;
			case 3:
				{
				setState(1800);
				term();
				setState(1801);
				match(GE);
				setState(1802);
				term();
				}
				break;
			case 4:
				{
				setState(1804);
				term();
				setState(1805);
				match(LT);
				setState(1806);
				term();
				}
				break;
			case 5:
				{
				setState(1808);
				term();
				setState(1809);
				match(LE);
				setState(1810);
				term();
				}
				break;
			case 6:
				{
				setState(1812);
				term();
				setState(1813);
				match(EQUAL);
				setState(1814);
				term();
				}
				break;
			case 7:
				{
				setState(1816);
				term();
				setState(1817);
				match(NOTEQUAL);
				setState(1818);
				term();
				}
				break;
			case 8:
				{
				setState(1820);
				match(LPAREN);
				setState(1821);
				((ConstraintContext)_localctx).c = constraint(0);
				setState(1822);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1834);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,215,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1832);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,214,_ctx) ) {
					case 1:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(1826);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1827);
						match(AND);
						setState(1828);
						constraint(11);
						}
						break;
					case 2:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(1829);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1830);
						match(OR);
						setState(1831);
						constraint(10);
						}
						break;
					}
					} 
				}
				setState(1836);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,215,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public TomIdentifierContext var;
		public TomIdentifierContext fsym;
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode STAR() { return getToken(TomJavaParser.STAR, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_term);
		int _la;
		try {
			setState(1856);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,219,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1837);
				((TermContext)_localctx).var = tomIdentifier();
				setState(1839);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,216,_ctx) ) {
				case 1:
					{
					setState(1838);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1841);
				((TermContext)_localctx).fsym = tomIdentifier();
				setState(1842);
				match(LPAREN);
				setState(1851);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (SUB - 77)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1843);
					term();
					setState(1848);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1844);
						match(COMMA);
						setState(1845);
						term();
						}
						}
						setState(1850);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1853);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1855);
				constant();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BqtermContext extends ParserRuleContext {
		public TomIdentifierContext codomain;
		public TomIdentifierContext fsym;
		public TomIdentifierContext var;
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public TerminalNode BQUOTE() { return getToken(TomJavaParser.BQUOTE, 0); }
		public List<BqtermContext> bqterm() {
			return getRuleContexts(BqtermContext.class);
		}
		public BqtermContext bqterm(int i) {
			return getRuleContext(BqtermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public TerminalNode LBRACK() { return getToken(TomJavaParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(TomJavaParser.RBRACK, 0); }
		public List<PairSlotBqtermContext> pairSlotBqterm() {
			return getRuleContexts(PairSlotBqtermContext.class);
		}
		public PairSlotBqtermContext pairSlotBqterm(int i) {
			return getRuleContext(PairSlotBqtermContext.class,i);
		}
		public TerminalNode STAR() { return getToken(TomJavaParser.STAR, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public TerminalNode UNDERSCORE() { return getToken(TomJavaParser.UNDERSCORE, 0); }
		public BqtermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bqterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterBqterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitBqterm(this);
		}
	}

	public final BqtermContext bqterm() throws RecognitionException {
		BqtermContext _localctx = new BqtermContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_bqterm);
		int _la;
		try {
			setState(1913);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,232,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1859);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,220,_ctx) ) {
				case 1:
					{
					setState(1858);
					((BqtermContext)_localctx).codomain = tomIdentifier();
					}
					break;
				}
				setState(1862);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(1861);
					match(BQUOTE);
					}
				}

				setState(1864);
				((BqtermContext)_localctx).fsym = tomIdentifier();
				setState(1865);
				match(LPAREN);
				setState(1874);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (BQUOTE - 77)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1866);
					bqterm();
					setState(1871);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1867);
						match(COMMA);
						setState(1868);
						bqterm();
						}
						}
						setState(1873);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1876);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1879);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,224,_ctx) ) {
				case 1:
					{
					setState(1878);
					((BqtermContext)_localctx).codomain = tomIdentifier();
					}
					break;
				}
				setState(1882);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(1881);
					match(BQUOTE);
					}
				}

				setState(1884);
				((BqtermContext)_localctx).fsym = tomIdentifier();
				setState(1885);
				match(LBRACK);
				setState(1894);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
					{
					setState(1886);
					pairSlotBqterm();
					setState(1891);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1887);
						match(COMMA);
						setState(1888);
						pairSlotBqterm();
						}
						}
						setState(1893);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1896);
				match(RBRACK);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1899);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,228,_ctx) ) {
				case 1:
					{
					setState(1898);
					((BqtermContext)_localctx).codomain = tomIdentifier();
					}
					break;
				}
				setState(1902);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(1901);
					match(BQUOTE);
					}
				}

				setState(1904);
				((BqtermContext)_localctx).var = tomIdentifier();
				setState(1906);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,230,_ctx) ) {
				case 1:
					{
					setState(1905);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1909);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
					{
					setState(1908);
					((BqtermContext)_localctx).codomain = tomIdentifier();
					}
				}

				setState(1911);
				constant();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1912);
				match(UNDERSCORE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairSlotBqtermContext extends ParserRuleContext {
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(TomJavaParser.ASSIGN, 0); }
		public BqtermContext bqterm() {
			return getRuleContext(BqtermContext.class,0);
		}
		public PairSlotBqtermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pairSlotBqterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPairSlotBqterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPairSlotBqterm(this);
		}
	}

	public final PairSlotBqtermContext pairSlotBqterm() throws RecognitionException {
		PairSlotBqtermContext _localctx = new PairSlotBqtermContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_pairSlotBqterm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1915);
			tomIdentifier();
			setState(1916);
			match(ASSIGN);
			setState(1917);
			bqterm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BqcompositeContext extends ParserRuleContext {
		public TomIdentifierContext fsym;
		public CompositeContext sub;
		public TomIdentifierContext var;
		public TerminalNode BQUOTE() { return getToken(TomJavaParser.BQUOTE, 0); }
		public TerminalNode LBRACK() { return getToken(TomJavaParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(TomJavaParser.RBRACK, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public List<PairSlotBqtermContext> pairSlotBqterm() {
			return getRuleContexts(PairSlotBqtermContext.class);
		}
		public PairSlotBqtermContext pairSlotBqterm(int i) {
			return getRuleContext(PairSlotBqtermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public List<CompositeContext> composite() {
			return getRuleContexts(CompositeContext.class);
		}
		public CompositeContext composite(int i) {
			return getRuleContext(CompositeContext.class,i);
		}
		public TerminalNode STAR() { return getToken(TomJavaParser.STAR, 0); }
		public BqcompositeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bqcomposite; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterBqcomposite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitBqcomposite(this);
		}
	}

	public final BqcompositeContext bqcomposite() throws RecognitionException {
		BqcompositeContext _localctx = new BqcompositeContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_bqcomposite);
		int _la;
		try {
			setState(1959);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,238,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1919);
				match(BQUOTE);
				setState(1920);
				((BqcompositeContext)_localctx).fsym = tomIdentifier();
				setState(1921);
				match(LBRACK);
				setState(1930);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
					{
					setState(1922);
					pairSlotBqterm();
					setState(1927);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1923);
						match(COMMA);
						setState(1924);
						pairSlotBqterm();
						}
						}
						setState(1929);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1932);
				match(RBRACK);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1934);
				match(BQUOTE);
				setState(1935);
				((BqcompositeContext)_localctx).fsym = tomIdentifier();
				setState(1936);
				match(LPAREN);
				setState(1945);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (EXTENDED_CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)) | (1L << (UNDERSCORE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
					{
					setState(1937);
					composite(0);
					setState(1942);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1938);
						match(COMMA);
						setState(1939);
						composite(0);
						}
						}
						setState(1944);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1947);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1949);
				match(BQUOTE);
				setState(1950);
				match(LPAREN);
				setState(1951);
				((BqcompositeContext)_localctx).sub = composite(0);
				setState(1952);
				match(RPAREN);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1954);
				match(BQUOTE);
				setState(1955);
				((BqcompositeContext)_localctx).var = tomIdentifier();
				setState(1957);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,237,_ctx) ) {
				case 1:
					{
					setState(1956);
					match(STAR);
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CompositeContext extends ParserRuleContext {
		public TomIdentifierContext fsym;
		public CompositeContext sub;
		public TomIdentifierContext var;
		public Token prefix;
		public Token bop;
		public Token postfix;
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public List<CompositeContext> composite() {
			return getRuleContexts(CompositeContext.class);
		}
		public CompositeContext composite(int i) {
			return getRuleContext(CompositeContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public TerminalNode STAR() { return getToken(TomJavaParser.STAR, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public TerminalNode UNDERSCORE() { return getToken(TomJavaParser.UNDERSCORE, 0); }
		public JavaIdentifierContext javaIdentifier() {
			return getRuleContext(JavaIdentifierContext.class,0);
		}
		public TerminalNode NEW() { return getToken(TomJavaParser.NEW, 0); }
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public LambdaExpressionContext lambdaExpression() {
			return getRuleContext(LambdaExpressionContext.class,0);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public ClassTypeContext classType() {
			return getRuleContext(ClassTypeContext.class,0);
		}
		public TerminalNode THIS() { return getToken(TomJavaParser.THIS, 0); }
		public InnerCreatorContext innerCreator() {
			return getRuleContext(InnerCreatorContext.class,0);
		}
		public TerminalNode SUPER() { return getToken(TomJavaParser.SUPER, 0); }
		public SuperSuffixContext superSuffix() {
			return getRuleContext(SuperSuffixContext.class,0);
		}
		public ExplicitGenericInvocationContext explicitGenericInvocation() {
			return getRuleContext(ExplicitGenericInvocationContext.class,0);
		}
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public TerminalNode INSTANCEOF() { return getToken(TomJavaParser.INSTANCEOF, 0); }
		public CompositeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_composite; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterComposite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitComposite(this);
		}
	}

	public final CompositeContext composite() throws RecognitionException {
		return composite(0);
	}

	private CompositeContext composite(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		CompositeContext _localctx = new CompositeContext(_ctx, _parentState);
		CompositeContext _prevctx = _localctx;
		int _startState = 258;
		enterRecursionRule(_localctx, 258, RULE_composite, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2015);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,245,_ctx) ) {
			case 1:
				{
				setState(1962);
				((CompositeContext)_localctx).fsym = tomIdentifier();
				setState(1963);
				match(LPAREN);
				setState(1972);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (EXTENDED_CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)) | (1L << (UNDERSCORE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
					{
					setState(1964);
					composite(0);
					setState(1969);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1965);
						match(COMMA);
						setState(1966);
						composite(0);
						}
						}
						setState(1971);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1974);
				match(RPAREN);
				}
				break;
			case 2:
				{
				setState(1976);
				match(LPAREN);
				setState(1977);
				((CompositeContext)_localctx).sub = composite(0);
				setState(1978);
				match(RPAREN);
				}
				break;
			case 3:
				{
				setState(1980);
				((CompositeContext)_localctx).var = tomIdentifier();
				setState(1982);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,241,_ctx) ) {
				case 1:
					{
					setState(1981);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				{
				setState(1984);
				constant();
				}
				break;
			case 5:
				{
				setState(1985);
				match(UNDERSCORE);
				}
				break;
			case 6:
				{
				setState(1986);
				javaIdentifier();
				}
				break;
			case 7:
				{
				setState(1987);
				match(NEW);
				setState(1988);
				creator();
				}
				break;
			case 8:
				{
				setState(1989);
				match(LPAREN);
				setState(1990);
				typeType();
				setState(1991);
				match(RPAREN);
				setState(1992);
				composite(21);
				}
				break;
			case 9:
				{
				setState(1994);
				((CompositeContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 108)) & ~0x3f) == 0 && ((1L << (_la - 108)) & ((1L << (INC - 108)) | (1L << (DEC - 108)) | (1L << (ADD - 108)) | (1L << (SUB - 108)))) != 0)) ) {
					((CompositeContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1995);
				composite(19);
				}
				break;
			case 10:
				{
				setState(1996);
				((CompositeContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TILDE || _la==ANTI) ) {
					((CompositeContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1997);
				composite(18);
				}
				break;
			case 11:
				{
				setState(1998);
				lambdaExpression();
				}
				break;
			case 12:
				{
				setState(1999);
				typeType();
				setState(2000);
				match(COLONCOLON);
				setState(2006);
				switch (_input.LA(1)) {
				case VISIT:
				case IS_FSYM:
				case IS_SORT:
				case MAKE:
				case MAKE_EMPTY:
				case MAKE_APPEND:
				case MAKE_INSERT:
				case GET_SLOT:
				case GET_DEFAULT:
				case GET_ELEMENT:
				case GET_HEAD:
				case GET_TAIL:
				case GET_SIZE:
				case IS_EMPTY:
				case IMPLEMENT:
				case EQUALS:
				case WHEN:
				case LT:
				case IDENTIFIER:
					{
					setState(2002);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(2001);
						typeArguments();
						}
					}

					setState(2004);
					javaIdentifier();
					}
					break;
				case NEW:
					{
					setState(2005);
					match(NEW);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 13:
				{
				setState(2008);
				classType();
				setState(2009);
				match(COLONCOLON);
				setState(2011);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(2010);
					typeArguments();
					}
				}

				setState(2013);
				match(NEW);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(2109);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,253,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(2107);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,252,_ctx) ) {
					case 1:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2017);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(2018);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & ((1L << (MOD - 114)) | (1L << (STAR - 114)) | (1L << (SLASH - 114)))) != 0)) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2019);
						composite(18);
						}
						break;
					case 2:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2020);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(2021);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2022);
						composite(17);
						}
						break;
					case 3:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2023);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(2031);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,246,_ctx) ) {
						case 1:
							{
							setState(2024);
							match(LT);
							setState(2025);
							match(LT);
							}
							break;
						case 2:
							{
							setState(2026);
							match(GT);
							setState(2027);
							match(GT);
							setState(2028);
							match(GT);
							}
							break;
						case 3:
							{
							setState(2029);
							match(GT);
							setState(2030);
							match(GT);
							}
							break;
						}
						setState(2033);
						composite(16);
						}
						break;
					case 4:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2034);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(2035);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 98)) & ~0x3f) == 0 && ((1L << (_la - 98)) & ((1L << (GT - 98)) | (1L << (LT - 98)) | (1L << (LE - 98)) | (1L << (GE - 98)))) != 0)) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2036);
						composite(15);
						}
						break;
					case 5:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2037);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(2038);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQUAL || _la==NOTEQUAL) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2039);
						composite(13);
						}
						break;
					case 6:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2040);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(2041);
						((CompositeContext)_localctx).bop = match(BITAND);
						setState(2042);
						composite(12);
						}
						break;
					case 7:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2043);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(2044);
						((CompositeContext)_localctx).bop = match(CARET);
						setState(2045);
						composite(11);
						}
						break;
					case 8:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2046);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(2047);
						((CompositeContext)_localctx).bop = match(PIPE);
						setState(2048);
						composite(10);
						}
						break;
					case 9:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2049);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(2050);
						((CompositeContext)_localctx).bop = match(AND);
						setState(2051);
						composite(9);
						}
						break;
					case 10:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2052);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(2053);
						((CompositeContext)_localctx).bop = match(OR);
						setState(2054);
						composite(8);
						}
						break;
					case 11:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2055);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(2056);
						((CompositeContext)_localctx).bop = match(QMARK);
						setState(2057);
						composite(0);
						setState(2058);
						match(COLON);
						setState(2059);
						composite(7);
						}
						break;
					case 12:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2061);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(2062);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (ASSIGN - 97)) | (1L << (ADD_ASSIGN - 97)) | (1L << (SUB_ASSIGN - 97)) | (1L << (MUL_ASSIGN - 97)) | (1L << (DIV_ASSIGN - 97)) | (1L << (AND_ASSIGN - 97)) | (1L << (OR_ASSIGN - 97)) | (1L << (XOR_ASSIGN - 97)) | (1L << (MOD_ASSIGN - 97)) | (1L << (LSHIFT_ASSIGN - 97)) | (1L << (RSHIFT_ASSIGN - 97)) | (1L << (URSHIFT_ASSIGN - 97)))) != 0)) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2063);
						composite(5);
						}
						break;
					case 13:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2064);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(2065);
						match(LPAREN);
						setState(2074);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (EXTENDED_CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)) | (1L << (UNDERSCORE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ANTI - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
							{
							setState(2066);
							composite(0);
							setState(2071);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==COMMA) {
								{
								{
								setState(2067);
								match(COMMA);
								setState(2068);
								composite(0);
								}
								}
								setState(2073);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(2076);
						match(RPAREN);
						}
						break;
					case 14:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2077);
						if (!(precpred(_ctx, 24))) throw new FailedPredicateException(this, "precpred(_ctx, 24)");
						setState(2078);
						((CompositeContext)_localctx).bop = match(DOT);
						setState(2089);
						switch (_input.LA(1)) {
						case VISIT:
						case IS_FSYM:
						case IS_SORT:
						case MAKE:
						case MAKE_EMPTY:
						case MAKE_APPEND:
						case MAKE_INSERT:
						case GET_SLOT:
						case GET_DEFAULT:
						case GET_ELEMENT:
						case GET_HEAD:
						case GET_TAIL:
						case GET_SIZE:
						case IS_EMPTY:
						case IMPLEMENT:
						case EQUALS:
						case WHEN:
						case IDENTIFIER:
							{
							setState(2079);
							javaIdentifier();
							}
							break;
						case THIS:
							{
							setState(2080);
							match(THIS);
							}
							break;
						case NEW:
							{
							setState(2081);
							match(NEW);
							setState(2083);
							_la = _input.LA(1);
							if (_la==LT) {
								{
								setState(2082);
								nonWildcardTypeArguments();
								}
							}

							setState(2085);
							innerCreator();
							}
							break;
						case SUPER:
							{
							setState(2086);
							match(SUPER);
							setState(2087);
							superSuffix();
							}
							break;
						case LT:
							{
							setState(2088);
							explicitGenericInvocation();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						}
						break;
					case 15:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2091);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(2092);
						match(LBRACK);
						setState(2093);
						composite(0);
						setState(2094);
						match(RBRACK);
						}
						break;
					case 16:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2096);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(2097);
						((CompositeContext)_localctx).postfix = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
							((CompositeContext)_localctx).postfix = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						break;
					case 17:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2098);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(2099);
						((CompositeContext)_localctx).bop = match(INSTANCEOF);
						setState(2100);
						typeType();
						}
						break;
					case 18:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2101);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(2102);
						match(COLONCOLON);
						setState(2104);
						_la = _input.LA(1);
						if (_la==LT) {
							{
							setState(2103);
							typeArguments();
							}
						}

						setState(2106);
						javaIdentifier();
						}
						break;
					}
					} 
				}
				setState(2111);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,253,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class PatternContext extends ParserRuleContext {
		public TomIdentifierContext var;
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode AT() { return getToken(TomJavaParser.AT, 0); }
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public TerminalNode ANTI() { return getToken(TomJavaParser.ANTI, 0); }
		public FsymbolContext fsymbol() {
			return getRuleContext(FsymbolContext.class,0);
		}
		public ExplicitArgsContext explicitArgs() {
			return getRuleContext(ExplicitArgsContext.class,0);
		}
		public ImplicitArgsContext implicitArgs() {
			return getRuleContext(ImplicitArgsContext.class,0);
		}
		public TerminalNode STAR() { return getToken(TomJavaParser.STAR, 0); }
		public TerminalNode UNDERSCORE() { return getToken(TomJavaParser.UNDERSCORE, 0); }
		public List<ConstantContext> constant() {
			return getRuleContexts(ConstantContext.class);
		}
		public ConstantContext constant(int i) {
			return getRuleContext(ConstantContext.class,i);
		}
		public List<TerminalNode> PIPE() { return getTokens(TomJavaParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(TomJavaParser.PIPE, i);
		}
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPattern(this);
		}
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_pattern);
		int _la;
		try {
			setState(2140);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,257,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2112);
				tomIdentifier();
				setState(2113);
				match(AT);
				setState(2114);
				pattern();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2116);
				match(ANTI);
				setState(2117);
				pattern();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2118);
				fsymbol();
				setState(2119);
				explicitArgs();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2121);
				fsymbol();
				setState(2122);
				implicitArgs();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2124);
				((PatternContext)_localctx).var = tomIdentifier();
				setState(2126);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(2125);
					match(STAR);
					}
				}

				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2128);
				match(UNDERSCORE);
				setState(2130);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(2129);
					match(STAR);
					}
				}

				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2132);
				constant();
				setState(2137);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PIPE) {
					{
					{
					setState(2133);
					match(PIPE);
					setState(2134);
					constant();
					}
					}
					setState(2139);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FsymbolContext extends ParserRuleContext {
		public List<HeadSymbolContext> headSymbol() {
			return getRuleContexts(HeadSymbolContext.class);
		}
		public HeadSymbolContext headSymbol(int i) {
			return getRuleContext(HeadSymbolContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public List<TerminalNode> PIPE() { return getTokens(TomJavaParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(TomJavaParser.PIPE, i);
		}
		public FsymbolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fsymbol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterFsymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitFsymbol(this);
		}
	}

	public final FsymbolContext fsymbol() throws RecognitionException {
		FsymbolContext _localctx = new FsymbolContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_fsymbol);
		int _la;
		try {
			setState(2153);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case ASSERT:
			case BOOLEAN:
			case BREAK:
			case BYTE:
			case CASE:
			case CATCH:
			case CHAR:
			case CLASS:
			case CONST:
			case CONTINUE:
			case DEFAULT:
			case DO:
			case DOUBLE:
			case ELSE:
			case ENUM:
			case FINAL:
			case FINALLY:
			case FLOAT:
			case FOR:
			case IF:
			case GOTO:
			case IMPLEMENTS:
			case IMPORT:
			case INSTANCEOF:
			case INT:
			case INTERFACE:
			case LONG:
			case NATIVE:
			case NEW:
			case PACKAGE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case RETURN:
			case SHORT:
			case STATIC:
			case STRICTFP:
			case SUPER:
			case SWITCH:
			case SYNCHRONIZED:
			case THIS:
			case THROW:
			case THROWS:
			case TRANSIENT:
			case TRY:
			case VOID:
			case VOLATILE:
			case WHILE:
			case DECIMAL_LITERAL:
			case FLOAT_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case EXTENDED_CHAR_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case SUB:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(2142);
				headSymbol();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(2143);
				match(LPAREN);
				setState(2144);
				headSymbol();
				setState(2147); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(2145);
					match(PIPE);
					setState(2146);
					headSymbol();
					}
					}
					setState(2149); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==PIPE );
				setState(2151);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeadSymbolContext extends ParserRuleContext {
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode QMARK() { return getToken(TomJavaParser.QMARK, 0); }
		public TerminalNode DQMARK() { return getToken(TomJavaParser.DQMARK, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public HeadSymbolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headSymbol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterHeadSymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitHeadSymbol(this);
		}
	}

	public final HeadSymbolContext headSymbol() throws RecognitionException {
		HeadSymbolContext _localctx = new HeadSymbolContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_headSymbol);
		int _la;
		try {
			setState(2164);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,262,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2155);
				tomIdentifier();
				setState(2157);
				_la = _input.LA(1);
				if (_la==QMARK) {
					{
					setState(2156);
					match(QMARK);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2159);
				tomIdentifier();
				setState(2161);
				_la = _input.LA(1);
				if (_la==DQMARK) {
					{
					setState(2160);
					match(DQMARK);
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2163);
				constant();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode DECIMAL_LITERAL() { return getToken(TomJavaParser.DECIMAL_LITERAL, 0); }
		public TerminalNode SUB() { return getToken(TomJavaParser.SUB, 0); }
		public TerminalNode FLOAT_LITERAL() { return getToken(TomJavaParser.FLOAT_LITERAL, 0); }
		public TerminalNode CHAR_LITERAL() { return getToken(TomJavaParser.CHAR_LITERAL, 0); }
		public TerminalNode EXTENDED_CHAR_LITERAL() { return getToken(TomJavaParser.EXTENDED_CHAR_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(TomJavaParser.STRING_LITERAL, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitConstant(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_constant);
		int _la;
		try {
			setState(2177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,265,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2167);
				_la = _input.LA(1);
				if (_la==SUB) {
					{
					setState(2166);
					match(SUB);
					}
				}

				setState(2169);
				match(DECIMAL_LITERAL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2171);
				_la = _input.LA(1);
				if (_la==SUB) {
					{
					setState(2170);
					match(SUB);
					}
				}

				setState(2173);
				match(FLOAT_LITERAL);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2174);
				match(CHAR_LITERAL);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2175);
				match(EXTENDED_CHAR_LITERAL);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2176);
				match(STRING_LITERAL);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplicitArgsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public List<PatternContext> pattern() {
			return getRuleContexts(PatternContext.class);
		}
		public PatternContext pattern(int i) {
			return getRuleContext(PatternContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public ExplicitArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterExplicitArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitExplicitArgs(this);
		}
	}

	public final ExplicitArgsContext explicitArgs() throws RecognitionException {
		ExplicitArgsContext _localctx = new ExplicitArgsContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_explicitArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2179);
			match(LPAREN);
			setState(2188);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (LPAREN - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (ANTI - 77)))) != 0) || _la==IDENTIFIER) {
				{
				setState(2180);
				pattern();
				setState(2185);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2181);
					match(COMMA);
					setState(2182);
					pattern();
					}
					}
					setState(2187);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2190);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImplicitArgsContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(TomJavaParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(TomJavaParser.RBRACK, 0); }
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public List<TerminalNode> ASSIGN() { return getTokens(TomJavaParser.ASSIGN); }
		public TerminalNode ASSIGN(int i) {
			return getToken(TomJavaParser.ASSIGN, i);
		}
		public List<PatternContext> pattern() {
			return getRuleContexts(PatternContext.class);
		}
		public PatternContext pattern(int i) {
			return getRuleContext(PatternContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public ImplicitArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implicitArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterImplicitArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitImplicitArgs(this);
		}
	}

	public final ImplicitArgsContext implicitArgs() throws RecognitionException {
		ImplicitArgsContext _localctx = new ImplicitArgsContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_implicitArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2192);
			match(LBRACK);
			setState(2206);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
				{
				setState(2193);
				tomIdentifier();
				setState(2194);
				match(ASSIGN);
				setState(2195);
				pattern();
				setState(2203);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2196);
					match(COMMA);
					setState(2197);
					tomIdentifier();
					setState(2198);
					match(ASSIGN);
					setState(2199);
					pattern();
					}
					}
					setState(2205);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2208);
			match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypetermContext extends ParserRuleContext {
		public TomIdentifierContext type;
		public TomIdentifierContext supertype;
		public TerminalNode TYPETERM() { return getToken(TomJavaParser.TYPETERM, 0); }
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public ImplementContext implement() {
			return getRuleContext(ImplementContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public TerminalNode EXTENDS() { return getToken(TomJavaParser.EXTENDS, 0); }
		public IsSortContext isSort() {
			return getRuleContext(IsSortContext.class,0);
		}
		public EqualsTermContext equalsTerm() {
			return getRuleContext(EqualsTermContext.class,0);
		}
		public TypetermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypeterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypeterm(this);
		}
	}

	public final TypetermContext typeterm() throws RecognitionException {
		TypetermContext _localctx = new TypetermContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_typeterm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2210);
			match(TYPETERM);
			setState(2211);
			((TypetermContext)_localctx).type = tomIdentifier();
			setState(2214);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(2212);
				match(EXTENDS);
				setState(2213);
				((TypetermContext)_localctx).supertype = tomIdentifier();
				}
			}

			setState(2216);
			match(LBRACE);
			setState(2217);
			implement();
			setState(2219);
			_la = _input.LA(1);
			if (_la==IS_SORT) {
				{
				setState(2218);
				isSort();
				}
			}

			setState(2222);
			_la = _input.LA(1);
			if (_la==EQUALS) {
				{
				setState(2221);
				equalsTerm();
				}
			}

			setState(2224);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorContext extends ParserRuleContext {
		public TomIdentifierContext codomain;
		public TomIdentifierContext opname;
		public TerminalNode OP() { return getToken(TomJavaParser.OP, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public SlotListContext slotList() {
			return getRuleContext(SlotListContext.class,0);
		}
		public List<IsFsymContext> isFsym() {
			return getRuleContexts(IsFsymContext.class);
		}
		public IsFsymContext isFsym(int i) {
			return getRuleContext(IsFsymContext.class,i);
		}
		public List<MakeContext> make() {
			return getRuleContexts(MakeContext.class);
		}
		public MakeContext make(int i) {
			return getRuleContext(MakeContext.class,i);
		}
		public List<GetSlotContext> getSlot() {
			return getRuleContexts(GetSlotContext.class);
		}
		public GetSlotContext getSlot(int i) {
			return getRuleContext(GetSlotContext.class,i);
		}
		public List<GetDefaultContext> getDefault() {
			return getRuleContexts(GetDefaultContext.class);
		}
		public GetDefaultContext getDefault(int i) {
			return getRuleContext(GetDefaultContext.class,i);
		}
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitOperator(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2226);
			match(OP);
			setState(2227);
			((OperatorContext)_localctx).codomain = tomIdentifier();
			setState(2228);
			((OperatorContext)_localctx).opname = tomIdentifier();
			setState(2229);
			match(LPAREN);
			setState(2231);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
				{
				setState(2230);
				slotList();
				}
			}

			setState(2233);
			match(RPAREN);
			setState(2234);
			match(LBRACE);
			setState(2241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (IS_FSYM - 61)) | (1L << (MAKE - 61)) | (1L << (GET_SLOT - 61)) | (1L << (GET_DEFAULT - 61)))) != 0)) {
				{
				setState(2239);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(2235);
					isFsym();
					}
					break;
				case MAKE:
					{
					setState(2236);
					make();
					}
					break;
				case GET_SLOT:
					{
					setState(2237);
					getSlot();
					}
					break;
				case GET_DEFAULT:
					{
					setState(2238);
					getDefault();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2243);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2244);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OplistContext extends ParserRuleContext {
		public TomIdentifierContext codomain;
		public TomIdentifierContext opname;
		public TomIdentifierContext domain;
		public TerminalNode OPLIST() { return getToken(TomJavaParser.OPLIST, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(TomJavaParser.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public List<IsFsymContext> isFsym() {
			return getRuleContexts(IsFsymContext.class);
		}
		public IsFsymContext isFsym(int i) {
			return getRuleContext(IsFsymContext.class,i);
		}
		public List<MakeEmptyListContext> makeEmptyList() {
			return getRuleContexts(MakeEmptyListContext.class);
		}
		public MakeEmptyListContext makeEmptyList(int i) {
			return getRuleContext(MakeEmptyListContext.class,i);
		}
		public List<MakeInsertListContext> makeInsertList() {
			return getRuleContexts(MakeInsertListContext.class);
		}
		public MakeInsertListContext makeInsertList(int i) {
			return getRuleContext(MakeInsertListContext.class,i);
		}
		public List<GetHeadContext> getHead() {
			return getRuleContexts(GetHeadContext.class);
		}
		public GetHeadContext getHead(int i) {
			return getRuleContext(GetHeadContext.class,i);
		}
		public List<GetTailContext> getTail() {
			return getRuleContexts(GetTailContext.class);
		}
		public GetTailContext getTail(int i) {
			return getRuleContext(GetTailContext.class,i);
		}
		public List<IsEmptyListContext> isEmptyList() {
			return getRuleContexts(IsEmptyListContext.class);
		}
		public IsEmptyListContext isEmptyList(int i) {
			return getRuleContext(IsEmptyListContext.class,i);
		}
		public OplistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oplist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterOplist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitOplist(this);
		}
	}

	public final OplistContext oplist() throws RecognitionException {
		OplistContext _localctx = new OplistContext(_ctx, getState());
		enterRule(_localctx, 276, RULE_oplist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2246);
			match(OPLIST);
			setState(2247);
			((OplistContext)_localctx).codomain = tomIdentifier();
			setState(2248);
			((OplistContext)_localctx).opname = tomIdentifier();
			setState(2249);
			match(LPAREN);
			setState(2250);
			((OplistContext)_localctx).domain = tomIdentifier();
			setState(2251);
			match(STAR);
			setState(2252);
			match(RPAREN);
			setState(2253);
			match(LBRACE);
			setState(2262);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (IS_FSYM - 61)) | (1L << (MAKE_EMPTY - 61)) | (1L << (MAKE_INSERT - 61)) | (1L << (GET_HEAD - 61)) | (1L << (GET_TAIL - 61)) | (1L << (IS_EMPTY - 61)))) != 0)) {
				{
				setState(2260);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(2254);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(2255);
					makeEmptyList();
					}
					break;
				case MAKE_INSERT:
					{
					setState(2256);
					makeInsertList();
					}
					break;
				case GET_HEAD:
					{
					setState(2257);
					getHead();
					}
					break;
				case GET_TAIL:
					{
					setState(2258);
					getTail();
					}
					break;
				case IS_EMPTY:
					{
					setState(2259);
					isEmptyList();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2264);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2265);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OparrayContext extends ParserRuleContext {
		public TomIdentifierContext codomain;
		public TomIdentifierContext opname;
		public TomIdentifierContext domain;
		public TerminalNode OPARRAY() { return getToken(TomJavaParser.OPARRAY, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(TomJavaParser.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public List<IsFsymContext> isFsym() {
			return getRuleContexts(IsFsymContext.class);
		}
		public IsFsymContext isFsym(int i) {
			return getRuleContext(IsFsymContext.class,i);
		}
		public List<MakeEmptyArrayContext> makeEmptyArray() {
			return getRuleContexts(MakeEmptyArrayContext.class);
		}
		public MakeEmptyArrayContext makeEmptyArray(int i) {
			return getRuleContext(MakeEmptyArrayContext.class,i);
		}
		public List<MakeAppendArrayContext> makeAppendArray() {
			return getRuleContexts(MakeAppendArrayContext.class);
		}
		public MakeAppendArrayContext makeAppendArray(int i) {
			return getRuleContext(MakeAppendArrayContext.class,i);
		}
		public List<GetElementContext> getElement() {
			return getRuleContexts(GetElementContext.class);
		}
		public GetElementContext getElement(int i) {
			return getRuleContext(GetElementContext.class,i);
		}
		public List<GetSizeContext> getSize() {
			return getRuleContexts(GetSizeContext.class);
		}
		public GetSizeContext getSize(int i) {
			return getRuleContext(GetSizeContext.class,i);
		}
		public OparrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oparray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterOparray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitOparray(this);
		}
	}

	public final OparrayContext oparray() throws RecognitionException {
		OparrayContext _localctx = new OparrayContext(_ctx, getState());
		enterRule(_localctx, 278, RULE_oparray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2267);
			match(OPARRAY);
			setState(2268);
			((OparrayContext)_localctx).codomain = tomIdentifier();
			setState(2269);
			((OparrayContext)_localctx).opname = tomIdentifier();
			setState(2270);
			match(LPAREN);
			setState(2271);
			((OparrayContext)_localctx).domain = tomIdentifier();
			setState(2272);
			match(STAR);
			setState(2273);
			match(RPAREN);
			setState(2274);
			match(LBRACE);
			setState(2282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (IS_FSYM - 61)) | (1L << (MAKE_EMPTY - 61)) | (1L << (MAKE_APPEND - 61)) | (1L << (GET_ELEMENT - 61)) | (1L << (GET_SIZE - 61)))) != 0)) {
				{
				setState(2280);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(2275);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(2276);
					makeEmptyArray();
					}
					break;
				case MAKE_APPEND:
					{
					setState(2277);
					makeAppendArray();
					}
					break;
				case GET_ELEMENT:
					{
					setState(2278);
					getElement();
					}
					break;
				case GET_SIZE:
					{
					setState(2279);
					getSize();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2284);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2285);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermBlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public TermBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTermBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTermBlock(this);
		}
	}

	public final TermBlockContext termBlock() throws RecognitionException {
		TermBlockContext _localctx = new TermBlockContext(_ctx, getState());
		enterRule(_localctx, 280, RULE_termBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2287);
			match(LBRACE);
			setState(2288);
			expression(0);
			setState(2289);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImplementContext extends ParserRuleContext {
		public TerminalNode IMPLEMENT() { return getToken(TomJavaParser.IMPLEMENT, 0); }
		public TerminalNode LBRACE() { return getToken(TomJavaParser.LBRACE, 0); }
		public TypeTypeContext typeType() {
			return getRuleContext(TypeTypeContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(TomJavaParser.RBRACE, 0); }
		public ImplementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterImplement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitImplement(this);
		}
	}

	public final ImplementContext implement() throws RecognitionException {
		ImplementContext _localctx = new ImplementContext(_ctx, getState());
		enterRule(_localctx, 282, RULE_implement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2291);
			match(IMPLEMENT);
			setState(2292);
			match(LBRACE);
			setState(2293);
			typeType();
			setState(2294);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EqualsTermContext extends ParserRuleContext {
		public TomIdentifierContext id1;
		public TomIdentifierContext id2;
		public TerminalNode EQUALS() { return getToken(TomJavaParser.EQUALS, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomJavaParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public EqualsTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalsTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterEqualsTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitEqualsTerm(this);
		}
	}

	public final EqualsTermContext equalsTerm() throws RecognitionException {
		EqualsTermContext _localctx = new EqualsTermContext(_ctx, getState());
		enterRule(_localctx, 284, RULE_equalsTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2296);
			match(EQUALS);
			setState(2297);
			match(LPAREN);
			setState(2298);
			((EqualsTermContext)_localctx).id1 = tomIdentifier();
			setState(2299);
			match(COMMA);
			setState(2300);
			((EqualsTermContext)_localctx).id2 = tomIdentifier();
			setState(2301);
			match(RPAREN);
			setState(2302);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsSortContext extends ParserRuleContext {
		public TerminalNode IS_SORT() { return getToken(TomJavaParser.IS_SORT, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public IsSortContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isSort; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterIsSort(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitIsSort(this);
		}
	}

	public final IsSortContext isSort() throws RecognitionException {
		IsSortContext _localctx = new IsSortContext(_ctx, getState());
		enterRule(_localctx, 286, RULE_isSort);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2304);
			match(IS_SORT);
			setState(2305);
			match(LPAREN);
			setState(2306);
			tomIdentifier();
			setState(2307);
			match(RPAREN);
			setState(2308);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsFsymContext extends ParserRuleContext {
		public TerminalNode IS_FSYM() { return getToken(TomJavaParser.IS_FSYM, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public IsFsymContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isFsym; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterIsFsym(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitIsFsym(this);
		}
	}

	public final IsFsymContext isFsym() throws RecognitionException {
		IsFsymContext _localctx = new IsFsymContext(_ctx, getState());
		enterRule(_localctx, 288, RULE_isFsym);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2310);
			match(IS_FSYM);
			setState(2311);
			match(LPAREN);
			setState(2312);
			tomIdentifier();
			setState(2313);
			match(RPAREN);
			setState(2314);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeContext extends ParserRuleContext {
		public TerminalNode MAKE() { return getToken(TomJavaParser.MAKE, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
		}
		public MakeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_make; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMake(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMake(this);
		}
	}

	public final MakeContext make() throws RecognitionException {
		MakeContext _localctx = new MakeContext(_ctx, getState());
		enterRule(_localctx, 290, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2316);
			match(MAKE);
			setState(2317);
			match(LPAREN);
			setState(2326);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
				{
				setState(2318);
				tomIdentifier();
				setState(2323);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2319);
					match(COMMA);
					setState(2320);
					tomIdentifier();
					}
					}
					setState(2325);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2328);
			match(RPAREN);
			setState(2329);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeEmptyListContext extends ParserRuleContext {
		public TerminalNode MAKE_EMPTY() { return getToken(TomJavaParser.MAKE_EMPTY, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public MakeEmptyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeEmptyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMakeEmptyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMakeEmptyList(this);
		}
	}

	public final MakeEmptyListContext makeEmptyList() throws RecognitionException {
		MakeEmptyListContext _localctx = new MakeEmptyListContext(_ctx, getState());
		enterRule(_localctx, 292, RULE_makeEmptyList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2331);
			match(MAKE_EMPTY);
			setState(2332);
			match(LPAREN);
			setState(2333);
			match(RPAREN);
			setState(2334);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeEmptyArrayContext extends ParserRuleContext {
		public TerminalNode MAKE_EMPTY() { return getToken(TomJavaParser.MAKE_EMPTY, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public MakeEmptyArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeEmptyArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMakeEmptyArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMakeEmptyArray(this);
		}
	}

	public final MakeEmptyArrayContext makeEmptyArray() throws RecognitionException {
		MakeEmptyArrayContext _localctx = new MakeEmptyArrayContext(_ctx, getState());
		enterRule(_localctx, 294, RULE_makeEmptyArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2336);
			match(MAKE_EMPTY);
			setState(2337);
			match(LPAREN);
			setState(2338);
			tomIdentifier();
			setState(2339);
			match(RPAREN);
			setState(2340);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeAppendArrayContext extends ParserRuleContext {
		public TomIdentifierContext id1;
		public TomIdentifierContext id2;
		public TerminalNode MAKE_APPEND() { return getToken(TomJavaParser.MAKE_APPEND, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomJavaParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public MakeAppendArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeAppendArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMakeAppendArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMakeAppendArray(this);
		}
	}

	public final MakeAppendArrayContext makeAppendArray() throws RecognitionException {
		MakeAppendArrayContext _localctx = new MakeAppendArrayContext(_ctx, getState());
		enterRule(_localctx, 296, RULE_makeAppendArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2342);
			match(MAKE_APPEND);
			setState(2343);
			match(LPAREN);
			setState(2344);
			((MakeAppendArrayContext)_localctx).id1 = tomIdentifier();
			setState(2345);
			match(COMMA);
			setState(2346);
			((MakeAppendArrayContext)_localctx).id2 = tomIdentifier();
			setState(2347);
			match(RPAREN);
			setState(2348);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeInsertListContext extends ParserRuleContext {
		public TomIdentifierContext id1;
		public TomIdentifierContext id2;
		public TerminalNode MAKE_INSERT() { return getToken(TomJavaParser.MAKE_INSERT, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomJavaParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public MakeInsertListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeInsertList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterMakeInsertList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitMakeInsertList(this);
		}
	}

	public final MakeInsertListContext makeInsertList() throws RecognitionException {
		MakeInsertListContext _localctx = new MakeInsertListContext(_ctx, getState());
		enterRule(_localctx, 298, RULE_makeInsertList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2350);
			match(MAKE_INSERT);
			setState(2351);
			match(LPAREN);
			setState(2352);
			((MakeInsertListContext)_localctx).id1 = tomIdentifier();
			setState(2353);
			match(COMMA);
			setState(2354);
			((MakeInsertListContext)_localctx).id2 = tomIdentifier();
			setState(2355);
			match(RPAREN);
			setState(2356);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetSlotContext extends ParserRuleContext {
		public TomIdentifierContext id1;
		public TomIdentifierContext id2;
		public TerminalNode GET_SLOT() { return getToken(TomJavaParser.GET_SLOT, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomJavaParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public GetSlotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getSlot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGetSlot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGetSlot(this);
		}
	}

	public final GetSlotContext getSlot() throws RecognitionException {
		GetSlotContext _localctx = new GetSlotContext(_ctx, getState());
		enterRule(_localctx, 300, RULE_getSlot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2358);
			match(GET_SLOT);
			setState(2359);
			match(LPAREN);
			setState(2360);
			((GetSlotContext)_localctx).id1 = tomIdentifier();
			setState(2361);
			match(COMMA);
			setState(2362);
			((GetSlotContext)_localctx).id2 = tomIdentifier();
			setState(2363);
			match(RPAREN);
			setState(2364);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetHeadContext extends ParserRuleContext {
		public TerminalNode GET_HEAD() { return getToken(TomJavaParser.GET_HEAD, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public GetHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGetHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGetHead(this);
		}
	}

	public final GetHeadContext getHead() throws RecognitionException {
		GetHeadContext _localctx = new GetHeadContext(_ctx, getState());
		enterRule(_localctx, 302, RULE_getHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2366);
			match(GET_HEAD);
			setState(2367);
			match(LPAREN);
			setState(2368);
			tomIdentifier();
			setState(2369);
			match(RPAREN);
			setState(2370);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetTailContext extends ParserRuleContext {
		public TerminalNode GET_TAIL() { return getToken(TomJavaParser.GET_TAIL, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public GetTailContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getTail; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGetTail(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGetTail(this);
		}
	}

	public final GetTailContext getTail() throws RecognitionException {
		GetTailContext _localctx = new GetTailContext(_ctx, getState());
		enterRule(_localctx, 304, RULE_getTail);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2372);
			match(GET_TAIL);
			setState(2373);
			match(LPAREN);
			setState(2374);
			tomIdentifier();
			setState(2375);
			match(RPAREN);
			setState(2376);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetElementContext extends ParserRuleContext {
		public TomIdentifierContext id1;
		public TomIdentifierContext id2;
		public TerminalNode GET_ELEMENT() { return getToken(TomJavaParser.GET_ELEMENT, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomJavaParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public List<TomIdentifierContext> tomIdentifier() {
			return getRuleContexts(TomIdentifierContext.class);
		}
		public TomIdentifierContext tomIdentifier(int i) {
			return getRuleContext(TomIdentifierContext.class,i);
		}
		public GetElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGetElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGetElement(this);
		}
	}

	public final GetElementContext getElement() throws RecognitionException {
		GetElementContext _localctx = new GetElementContext(_ctx, getState());
		enterRule(_localctx, 306, RULE_getElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2378);
			match(GET_ELEMENT);
			setState(2379);
			match(LPAREN);
			setState(2380);
			((GetElementContext)_localctx).id1 = tomIdentifier();
			setState(2381);
			match(COMMA);
			setState(2382);
			((GetElementContext)_localctx).id2 = tomIdentifier();
			setState(2383);
			match(RPAREN);
			setState(2384);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsEmptyListContext extends ParserRuleContext {
		public TerminalNode IS_EMPTY() { return getToken(TomJavaParser.IS_EMPTY, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public IsEmptyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isEmptyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterIsEmptyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitIsEmptyList(this);
		}
	}

	public final IsEmptyListContext isEmptyList() throws RecognitionException {
		IsEmptyListContext _localctx = new IsEmptyListContext(_ctx, getState());
		enterRule(_localctx, 308, RULE_isEmptyList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2386);
			match(IS_EMPTY);
			setState(2387);
			match(LPAREN);
			setState(2388);
			tomIdentifier();
			setState(2389);
			match(RPAREN);
			setState(2390);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetSizeContext extends ParserRuleContext {
		public TerminalNode GET_SIZE() { return getToken(TomJavaParser.GET_SIZE, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public GetSizeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getSize; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGetSize(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGetSize(this);
		}
	}

	public final GetSizeContext getSize() throws RecognitionException {
		GetSizeContext _localctx = new GetSizeContext(_ctx, getState());
		enterRule(_localctx, 310, RULE_getSize);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2392);
			match(GET_SIZE);
			setState(2393);
			match(LPAREN);
			setState(2394);
			tomIdentifier();
			setState(2395);
			match(RPAREN);
			setState(2396);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetDefaultContext extends ParserRuleContext {
		public TerminalNode GET_DEFAULT() { return getToken(TomJavaParser.GET_DEFAULT, 0); }
		public TerminalNode LPAREN() { return getToken(TomJavaParser.LPAREN, 0); }
		public TomIdentifierContext tomIdentifier() {
			return getRuleContext(TomIdentifierContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(TomJavaParser.RPAREN, 0); }
		public TermBlockContext termBlock() {
			return getRuleContext(TermBlockContext.class,0);
		}
		public GetDefaultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getDefault; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGetDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGetDefault(this);
		}
	}

	public final GetDefaultContext getDefault() throws RecognitionException {
		GetDefaultContext _localctx = new GetDefaultContext(_ctx, getState());
		enterRule(_localctx, 312, RULE_getDefault);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2398);
			match(GET_DEFAULT);
			setState(2399);
			match(LPAREN);
			setState(2400);
			tomIdentifier();
			setState(2401);
			match(RPAREN);
			setState(2402);
			termBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TomIdentifierContext extends ParserRuleContext {
		public TerminalNode ABSTRACT() { return getToken(TomJavaParser.ABSTRACT, 0); }
		public TerminalNode ASSERT() { return getToken(TomJavaParser.ASSERT, 0); }
		public TerminalNode BOOLEAN() { return getToken(TomJavaParser.BOOLEAN, 0); }
		public TerminalNode BREAK() { return getToken(TomJavaParser.BREAK, 0); }
		public TerminalNode BYTE() { return getToken(TomJavaParser.BYTE, 0); }
		public TerminalNode CASE() { return getToken(TomJavaParser.CASE, 0); }
		public TerminalNode CATCH() { return getToken(TomJavaParser.CATCH, 0); }
		public TerminalNode CHAR() { return getToken(TomJavaParser.CHAR, 0); }
		public TerminalNode CLASS() { return getToken(TomJavaParser.CLASS, 0); }
		public TerminalNode CONST() { return getToken(TomJavaParser.CONST, 0); }
		public TerminalNode CONTINUE() { return getToken(TomJavaParser.CONTINUE, 0); }
		public TerminalNode DEFAULT() { return getToken(TomJavaParser.DEFAULT, 0); }
		public TerminalNode DO() { return getToken(TomJavaParser.DO, 0); }
		public TerminalNode DOUBLE() { return getToken(TomJavaParser.DOUBLE, 0); }
		public TerminalNode ELSE() { return getToken(TomJavaParser.ELSE, 0); }
		public TerminalNode ENUM() { return getToken(TomJavaParser.ENUM, 0); }
		public TerminalNode FINAL() { return getToken(TomJavaParser.FINAL, 0); }
		public TerminalNode FINALLY() { return getToken(TomJavaParser.FINALLY, 0); }
		public TerminalNode FLOAT() { return getToken(TomJavaParser.FLOAT, 0); }
		public TerminalNode FOR() { return getToken(TomJavaParser.FOR, 0); }
		public TerminalNode IF() { return getToken(TomJavaParser.IF, 0); }
		public TerminalNode GOTO() { return getToken(TomJavaParser.GOTO, 0); }
		public TerminalNode IMPLEMENTS() { return getToken(TomJavaParser.IMPLEMENTS, 0); }
		public TerminalNode IMPORT() { return getToken(TomJavaParser.IMPORT, 0); }
		public TerminalNode INSTANCEOF() { return getToken(TomJavaParser.INSTANCEOF, 0); }
		public TerminalNode INT() { return getToken(TomJavaParser.INT, 0); }
		public TerminalNode INTERFACE() { return getToken(TomJavaParser.INTERFACE, 0); }
		public TerminalNode LONG() { return getToken(TomJavaParser.LONG, 0); }
		public TerminalNode NATIVE() { return getToken(TomJavaParser.NATIVE, 0); }
		public TerminalNode NEW() { return getToken(TomJavaParser.NEW, 0); }
		public TerminalNode PACKAGE() { return getToken(TomJavaParser.PACKAGE, 0); }
		public TerminalNode PRIVATE() { return getToken(TomJavaParser.PRIVATE, 0); }
		public TerminalNode PROTECTED() { return getToken(TomJavaParser.PROTECTED, 0); }
		public TerminalNode PUBLIC() { return getToken(TomJavaParser.PUBLIC, 0); }
		public TerminalNode RETURN() { return getToken(TomJavaParser.RETURN, 0); }
		public TerminalNode SHORT() { return getToken(TomJavaParser.SHORT, 0); }
		public TerminalNode STATIC() { return getToken(TomJavaParser.STATIC, 0); }
		public TerminalNode STRICTFP() { return getToken(TomJavaParser.STRICTFP, 0); }
		public TerminalNode SUPER() { return getToken(TomJavaParser.SUPER, 0); }
		public TerminalNode SWITCH() { return getToken(TomJavaParser.SWITCH, 0); }
		public TerminalNode SYNCHRONIZED() { return getToken(TomJavaParser.SYNCHRONIZED, 0); }
		public TerminalNode THIS() { return getToken(TomJavaParser.THIS, 0); }
		public TerminalNode THROW() { return getToken(TomJavaParser.THROW, 0); }
		public TerminalNode THROWS() { return getToken(TomJavaParser.THROWS, 0); }
		public TerminalNode TRANSIENT() { return getToken(TomJavaParser.TRANSIENT, 0); }
		public TerminalNode TRY() { return getToken(TomJavaParser.TRY, 0); }
		public TerminalNode VOID() { return getToken(TomJavaParser.VOID, 0); }
		public TerminalNode VOLATILE() { return getToken(TomJavaParser.VOLATILE, 0); }
		public TerminalNode WHILE() { return getToken(TomJavaParser.WHILE, 0); }
		public TerminalNode BOOL_LITERAL() { return getToken(TomJavaParser.BOOL_LITERAL, 0); }
		public TerminalNode NULL_LITERAL() { return getToken(TomJavaParser.NULL_LITERAL, 0); }
		public TerminalNode IDENTIFIER() { return getToken(TomJavaParser.IDENTIFIER, 0); }
		public TomIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tomIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTomIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTomIdentifier(this);
		}
	}

	public final TomIdentifierContext tomIdentifier() throws RecognitionException {
		TomIdentifierContext _localctx = new TomIdentifierContext(_ctx, getState());
		enterRule(_localctx, 314, RULE_tomIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2404);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 83:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 84:
			return funTerm_sempred((FunTermContext)_localctx, predIndex);
		case 124:
			return constraint_sempred((ConstraintContext)_localctx, predIndex);
		case 129:
			return composite_sempred((CompositeContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 18);
		case 1:
			return precpred(_ctx, 17);
		case 2:
			return precpred(_ctx, 16);
		case 3:
			return precpred(_ctx, 15);
		case 4:
			return precpred(_ctx, 13);
		case 5:
			return precpred(_ctx, 12);
		case 6:
			return precpred(_ctx, 11);
		case 7:
			return precpred(_ctx, 10);
		case 8:
			return precpred(_ctx, 9);
		case 9:
			return precpred(_ctx, 8);
		case 10:
			return precpred(_ctx, 7);
		case 11:
			return precpred(_ctx, 6);
		case 12:
			return precpred(_ctx, 26);
		case 13:
			return precpred(_ctx, 25);
		case 14:
			return precpred(_ctx, 21);
		case 15:
			return precpred(_ctx, 14);
		case 16:
			return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean funTerm_sempred(FunTermContext _localctx, int predIndex) {
		switch (predIndex) {
		case 17:
			return precpred(_ctx, 7);
		case 18:
			return precpred(_ctx, 6);
		case 19:
			return precpred(_ctx, 5);
		}
		return true;
	}
	private boolean constraint_sempred(ConstraintContext _localctx, int predIndex) {
		switch (predIndex) {
		case 20:
			return precpred(_ctx, 10);
		case 21:
			return precpred(_ctx, 9);
		}
		return true;
	}
	private boolean composite_sempred(CompositeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 22:
			return precpred(_ctx, 17);
		case 23:
			return precpred(_ctx, 16);
		case 24:
			return precpred(_ctx, 15);
		case 25:
			return precpred(_ctx, 14);
		case 26:
			return precpred(_ctx, 12);
		case 27:
			return precpred(_ctx, 11);
		case 28:
			return precpred(_ctx, 10);
		case 29:
			return precpred(_ctx, 9);
		case 30:
			return precpred(_ctx, 8);
		case 31:
			return precpred(_ctx, 7);
		case 32:
			return precpred(_ctx, 6);
		case 33:
			return precpred(_ctx, 5);
		case 34:
			return precpred(_ctx, 25);
		case 35:
			return precpred(_ctx, 24);
		case 36:
			return precpred(_ctx, 23);
		case 37:
			return precpred(_ctx, 20);
		case 38:
			return precpred(_ctx, 13);
		case 39:
			return precpred(_ctx, 3);
		}
		return true;
	}

	private static final int _serializedATNSegments = 2;
	private static final String _serializedATNSegment0 =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\u00a4\u0969\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"+
		"\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"+
		"\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089\t\u0089"+
		"\4\u008a\t\u008a\4\u008b\t\u008b\4\u008c\t\u008c\4\u008d\t\u008d\4\u008e"+
		"\t\u008e\4\u008f\t\u008f\4\u0090\t\u0090\4\u0091\t\u0091\4\u0092\t\u0092"+
		"\4\u0093\t\u0093\4\u0094\t\u0094\4\u0095\t\u0095\4\u0096\t\u0096\4\u0097"+
		"\t\u0097\4\u0098\t\u0098\4\u0099\t\u0099\4\u009a\t\u009a\4\u009b\t\u009b"+
		"\4\u009c\t\u009c\4\u009d\t\u009d\4\u009e\t\u009e\4\u009f\t\u009f\3\2\5"+
		"\2\u0140\n\2\3\2\7\2\u0143\n\2\f\2\16\2\u0146\13\2\3\2\7\2\u0149\n\2\f"+
		"\2\16\2\u014c\13\2\3\2\3\2\3\3\7\3\u0151\n\3\f\3\16\3\u0154\13\3\3\3\3"+
		"\3\3\4\3\4\3\4\3\5\7\5\u015c\n\5\f\5\16\5\u015f\13\5\3\5\3\5\3\5\3\5\3"+
		"\6\3\6\5\6\u0167\n\6\3\6\3\6\3\6\5\6\u016c\n\6\3\6\3\6\3\7\7\7\u0171\n"+
		"\7\f\7\16\7\u0174\13\7\3\7\3\7\3\7\3\7\5\7\u017a\n\7\3\7\5\7\u017d\n\7"+
		"\3\b\3\b\3\b\3\b\3\b\5\b\u0184\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t"+
		"\u018e\n\t\3\n\3\n\5\n\u0192\n\n\3\13\3\13\3\13\5\13\u0197\n\13\3\13\3"+
		"\13\5\13\u019b\n\13\3\13\3\13\5\13\u019f\n\13\3\13\3\13\3\f\3\f\3\f\3"+
		"\f\7\f\u01a7\n\f\f\f\16\f\u01aa\13\f\3\f\3\f\3\r\7\r\u01af\n\r\f\r\16"+
		"\r\u01b2\13\r\3\r\3\r\3\r\5\r\u01b7\n\r\3\16\3\16\3\16\7\16\u01bc\n\16"+
		"\f\16\16\16\u01bf\13\16\3\17\3\17\3\17\3\17\5\17\u01c5\n\17\3\17\3\17"+
		"\5\17\u01c9\n\17\3\17\5\17\u01cc\n\17\3\17\5\17\u01cf\n\17\3\17\3\17\3"+
		"\20\3\20\3\20\7\20\u01d6\n\20\f\20\16\20\u01d9\13\20\3\21\7\21\u01dc\n"+
		"\21\f\21\16\21\u01df\13\21\3\21\3\21\5\21\u01e3\n\21\3\21\5\21\u01e6\n"+
		"\21\3\22\3\22\7\22\u01ea\n\22\f\22\16\22\u01ed\13\22\3\23\3\23\3\23\5"+
		"\23\u01f2\n\23\3\23\3\23\5\23\u01f6\n\23\3\23\3\23\3\24\3\24\7\24\u01fc"+
		"\n\24\f\24\16\24\u01ff\13\24\3\24\3\24\3\25\3\25\7\25\u0205\n\25\f\25"+
		"\16\25\u0208\13\25\3\25\3\25\3\26\3\26\5\26\u020e\n\26\3\26\3\26\7\26"+
		"\u0212\n\26\f\26\16\26\u0215\13\26\3\26\3\26\5\26\u0219\n\26\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0224\n\27\3\30\3\30\3\30\3\30"+
		"\3\30\7\30\u022b\n\30\f\30\16\30\u022e\13\30\3\30\3\30\5\30\u0232\n\30"+
		"\3\30\3\30\3\31\3\31\5\31\u0238\n\31\3\32\3\32\5\32\u023c\n\32\3\33\3"+
		"\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\5\35\u0248\n\35\3\35\3\35"+
		"\3\36\3\36\3\36\3\36\3\37\7\37\u0251\n\37\f\37\16\37\u0254\13\37\3\37"+
		"\3\37\3\37\5\37\u0259\n\37\3 \3 \3 \3 \3 \3 \3 \5 \u0262\n \3!\3!\3!\3"+
		"!\7!\u0268\n!\f!\16!\u026b\13!\3!\3!\3\"\3\"\3\"\7\"\u0272\n\"\f\"\16"+
		"\"\u0275\13\"\3\"\3\"\3\"\3#\7#\u027b\n#\f#\16#\u027e\13#\3#\3#\3#\7#"+
		"\u0283\n#\f#\16#\u0286\13#\3#\3#\5#\u028a\n#\3#\3#\3#\3#\7#\u0290\n#\f"+
		"#\16#\u0293\13#\3#\3#\5#\u0297\n#\3#\3#\3$\3$\3$\3$\3$\3$\5$\u02a1\n$"+
		"\3%\3%\3%\3&\3&\3&\7&\u02a9\n&\f&\16&\u02ac\13&\3\'\3\'\3\'\5\'\u02b1"+
		"\n\'\3(\3(\3(\7(\u02b6\n(\f(\16(\u02b9\13(\3)\3)\5)\u02bd\n)\3*\3*\3*"+
		"\3*\7*\u02c3\n*\f*\16*\u02c6\13*\3*\5*\u02c9\n*\5*\u02cb\n*\3*\3*\3+\3"+
		"+\5+\u02d1\n+\3+\3+\3+\5+\u02d6\n+\7+\u02d8\n+\f+\16+\u02db\13+\3,\3,"+
		"\3,\3,\5,\u02e1\n,\5,\u02e3\n,\3-\3-\3-\7-\u02e8\n-\f-\16-\u02eb\13-\3"+
		".\3.\5.\u02ef\n.\3.\3.\3/\3/\3/\7/\u02f6\n/\f/\16/\u02f9\13/\3/\3/\5/"+
		"\u02fd\n/\3/\5/\u0300\n/\3\60\7\60\u0303\n\60\f\60\16\60\u0306\13\60\3"+
		"\60\3\60\3\60\3\61\7\61\u030c\n\61\f\61\16\61\u030f\13\61\3\61\3\61\3"+
		"\61\3\61\3\62\3\62\3\62\7\62\u0318\n\62\f\62\16\62\u031b\13\62\3\63\3"+
		"\63\3\63\3\63\3\63\3\63\5\63\u0323\n\63\3\64\3\64\3\65\3\65\3\66\3\66"+
		"\3\66\3\66\3\66\5\66\u032e\n\66\3\66\5\66\u0331\n\66\3\67\3\67\3\67\7"+
		"\67\u0336\n\67\f\67\16\67\u0339\13\67\38\38\38\38\39\39\39\59\u0342\n"+
		"9\3:\3:\3:\3:\7:\u0348\n:\f:\16:\u034b\13:\5:\u034d\n:\3:\5:\u0350\n:"+
		"\3:\3:\3;\3;\3;\3;\3;\3<\3<\7<\u035b\n<\f<\16<\u035e\13<\3<\3<\3=\7=\u0363"+
		"\n=\f=\16=\u0366\13=\3=\3=\3=\5=\u036b\n=\3>\3>\3>\3>\3>\3>\5>\u0373\n"+
		">\3>\3>\5>\u0377\n>\3>\3>\5>\u037b\n>\3>\3>\5>\u037f\n>\5>\u0381\n>\3"+
		"?\3?\5?\u0385\n?\3@\3@\3@\3@\5@\u038b\n@\3A\3A\3B\3B\3B\3C\3C\7C\u0394"+
		"\nC\fC\16C\u0397\13C\3C\3C\3D\3D\3D\3D\3D\3D\5D\u03a1\nD\3E\7E\u03a4\n"+
		"E\fE\16E\u03a7\13E\3E\3E\3E\3F\7F\u03ad\nF\fF\16F\u03b0\13F\3F\3F\5F\u03b4"+
		"\nF\3F\5F\u03b7\nF\3G\3G\3G\3G\3G\5G\u03be\nG\3G\3G\3G\3G\3G\3G\3G\5G"+
		"\u03c7\nG\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\6G"+
		"\u03dc\nG\rG\16G\u03dd\3G\5G\u03e1\nG\3G\5G\u03e4\nG\3G\3G\3G\3G\7G\u03ea"+
		"\nG\fG\16G\u03ed\13G\3G\5G\u03f0\nG\3G\3G\3G\3G\7G\u03f6\nG\fG\16G\u03f9"+
		"\13G\3G\7G\u03fc\nG\fG\16G\u03ff\13G\3G\3G\3G\3G\3G\3G\3G\3G\5G\u0409"+
		"\nG\3G\3G\3G\3G\3G\3G\3G\5G\u0412\nG\3G\3G\3G\5G\u0417\nG\3G\3G\3G\3G"+
		"\3G\3G\3G\3G\3G\3G\5G\u0423\nG\3H\3H\3H\7H\u0428\nH\fH\16H\u042b\13H\3"+
		"H\3H\3H\3H\3H\3I\3I\3I\7I\u0435\nI\fI\16I\u0438\13I\3J\3J\3J\3K\3K\3K"+
		"\5K\u0440\nK\3K\3K\3L\3L\3L\7L\u0447\nL\fL\16L\u044a\13L\3M\7M\u044d\n"+
		"M\fM\16M\u0450\13M\3M\3M\3M\3M\3M\3N\6N\u0458\nN\rN\16N\u0459\3N\6N\u045d"+
		"\nN\rN\16N\u045e\3O\3O\3O\5O\u0464\nO\3O\3O\3O\3O\5O\u046a\nO\3P\3P\5"+
		"P\u046e\nP\3P\3P\5P\u0472\nP\3P\3P\5P\u0476\nP\5P\u0478\nP\3Q\3Q\5Q\u047c"+
		"\nQ\3R\7R\u047f\nR\fR\16R\u0482\13R\3R\3R\3R\3R\3R\3S\3S\3S\3S\3T\3T\3"+
		"T\7T\u0490\nT\fT\16T\u0493\13T\3U\3U\3U\3U\3U\5U\u049a\nU\3U\3U\3U\3U"+
		"\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\5U\u04ad\nU\3U\3U\5U\u04b1\nU"+
		"\3U\3U\3U\5U\u04b6\nU\3U\3U\3U\5U\u04bb\nU\3U\3U\3U\3U\3U\3U\3U\3U\3U"+
		"\3U\3U\3U\3U\3U\5U\u04cb\nU\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U"+
		"\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U"+
		"\5U\u04f2\nU\3U\3U\3U\3U\3U\3U\3U\5U\u04fb\nU\3U\3U\5U\u04ff\nU\3U\3U"+
		"\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\3U\5U\u050e\nU\3U\3U\3U\5U\u0513\nU\3U"+
		"\5U\u0516\nU\7U\u0518\nU\fU\16U\u051b\13U\3V\3V\3V\3V\3V\3V\3V\3V\5V\u0525"+
		"\nV\3V\3V\3V\3V\3V\3V\5V\u052d\nV\3V\3V\5V\u0531\nV\3V\3V\3V\5V\u0536"+
		"\nV\3V\3V\5V\u053a\nV\3V\3V\3V\3V\3V\3V\3V\3V\5V\u0544\nV\3V\3V\3V\7V"+
		"\u0549\nV\fV\16V\u054c\13V\3W\3W\3W\3W\3X\3X\3X\5X\u0555\nX\3X\3X\3X\3"+
		"X\3X\7X\u055c\nX\fX\16X\u055f\13X\3X\3X\5X\u0563\nX\3Y\3Y\5Y\u0567\nY"+
		"\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\5Z\u0579\nZ\5Z\u057b"+
		"\nZ\3[\3[\3[\5[\u0580\n[\3[\7[\u0583\n[\f[\16[\u0586\13[\3[\3[\5[\u058a"+
		"\n[\3\\\3\\\3\\\3\\\3\\\3\\\3\\\5\\\u0593\n\\\5\\\u0595\n\\\3]\3]\5]\u0599"+
		"\n]\3]\3]\3]\5]\u059e\n]\7]\u05a0\n]\f]\16]\u05a3\13]\3]\5]\u05a6\n]\3"+
		"^\3^\5^\u05aa\n^\3^\3^\3_\3_\3_\3_\7_\u05b2\n_\f_\16_\u05b5\13_\3_\3_"+
		"\3_\3_\3_\3_\3_\7_\u05be\n_\f_\16_\u05c1\13_\3_\3_\7_\u05c5\n_\f_\16_"+
		"\u05c8\13_\5_\u05ca\n_\3`\3`\5`\u05ce\n`\3a\3a\3a\3b\3b\3b\5b\u05d6\n"+
		"b\3c\3c\3c\5c\u05db\nc\3d\3d\3d\3d\3e\3e\3e\7e\u05e4\ne\fe\16e\u05e7\13"+
		"e\3f\5f\u05ea\nf\3f\3f\5f\u05ee\nf\3f\3f\7f\u05f2\nf\ff\16f\u05f5\13f"+
		"\3g\3g\3h\3h\3h\3h\7h\u05fd\nh\fh\16h\u0600\13h\3h\3h\3i\3i\3i\3i\5i\u0608"+
		"\ni\5i\u060a\ni\3j\3j\3j\3j\3j\5j\u0611\nj\3k\3k\5k\u0615\nk\3k\3k\3l"+
		"\3l\3m\3m\3m\3m\3m\3m\3m\3m\5m\u0623\nm\3n\3n\3o\3o\5o\u0629\no\3p\3p"+
		"\3q\3q\3q\3q\3q\7q\u0632\nq\fq\16q\u0635\13q\5q\u0637\nq\3q\5q\u063a\n"+
		"q\3q\3q\7q\u063e\nq\fq\16q\u0641\13q\3q\3q\3r\3r\3r\3r\5r\u0649\nr\3r"+
		"\3r\3r\3r\3r\7r\u0650\nr\fr\16r\u0653\13r\3r\3r\3s\3s\3s\7s\u065a\ns\f"+
		"s\16s\u065d\13s\3s\7s\u0660\ns\fs\16s\u0663\13s\3s\3s\3s\7s\u0668\ns\f"+
		"s\16s\u066b\13s\3s\3s\3t\3t\5t\u0671\nt\3t\3t\3u\3u\3u\3v\3v\3v\7v\u067b"+
		"\nv\fv\16v\u067e\13v\3v\3v\3v\3v\7v\u0684\nv\fv\16v\u0687\13v\3v\5v\u068a"+
		"\nv\3w\3w\3w\3w\7w\u0690\nw\fw\16w\u0693\13w\3w\3w\3x\3x\3x\3x\7x\u069b"+
		"\nx\fx\16x\u069e\13x\3x\3x\3y\3y\3y\5y\u06a5\ny\3y\3y\3y\3y\3y\3y\5y\u06ad"+
		"\ny\3y\3y\3y\3y\3y\3y\3y\3y\3y\3y\3y\5y\u06ba\ny\3z\3z\3z\7z\u06bf\nz"+
		"\fz\16z\u06c2\13z\3z\3z\3{\3{\3{\7{\u06c9\n{\f{\16{\u06cc\13{\3|\3|\5"+
		"|\u06d0\n|\3|\3|\3}\3}\3}\7}\u06d7\n}\f}\16}\u06da\13}\3}\3}\3}\3}\7}"+
		"\u06e0\n}\f}\16}\u06e3\13}\5}\u06e5\n}\3}\3}\3}\3}\7}\u06eb\n}\f}\16}"+
		"\u06ee\13}\3}\3}\3}\3}\3}\3}\7}\u06f6\n}\f}\16}\u06f9\13}\3}\3}\5}\u06fd"+
		"\n}\5}\u06ff\n}\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~"+
		"\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\3~\5~\u0723\n~\3~\3~\3~"+
		"\3~\3~\3~\7~\u072b\n~\f~\16~\u072e\13~\3\177\3\177\5\177\u0732\n\177\3"+
		"\177\3\177\3\177\3\177\3\177\7\177\u0739\n\177\f\177\16\177\u073c\13\177"+
		"\5\177\u073e\n\177\3\177\3\177\3\177\5\177\u0743\n\177\3\u0080\5\u0080"+
		"\u0746\n\u0080\3\u0080\5\u0080\u0749\n\u0080\3\u0080\3\u0080\3\u0080\3"+
		"\u0080\3\u0080\7\u0080\u0750\n\u0080\f\u0080\16\u0080\u0753\13\u0080\5"+
		"\u0080\u0755\n\u0080\3\u0080\3\u0080\3\u0080\5\u0080\u075a\n\u0080\3\u0080"+
		"\5\u0080\u075d\n\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\7\u0080"+
		"\u0764\n\u0080\f\u0080\16\u0080\u0767\13\u0080\5\u0080\u0769\n\u0080\3"+
		"\u0080\3\u0080\3\u0080\5\u0080\u076e\n\u0080\3\u0080\5\u0080\u0771\n\u0080"+
		"\3\u0080\3\u0080\5\u0080\u0775\n\u0080\3\u0080\5\u0080\u0778\n\u0080\3"+
		"\u0080\3\u0080\5\u0080\u077c\n\u0080\3\u0081\3\u0081\3\u0081\3\u0081\3"+
		"\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\7\u0082\u0788\n\u0082\f"+
		"\u0082\16\u0082\u078b\13\u0082\5\u0082\u078d\n\u0082\3\u0082\3\u0082\3"+
		"\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\7\u0082\u0797\n\u0082\f"+
		"\u0082\16\u0082\u079a\13\u0082\5\u0082\u079c\n\u0082\3\u0082\3\u0082\3"+
		"\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\5\u0082"+
		"\u07a8\n\u0082\5\u0082\u07aa\n\u0082\3\u0083\3\u0083\3\u0083\3\u0083\3"+
		"\u0083\3\u0083\7\u0083\u07b2\n\u0083\f\u0083\16\u0083\u07b5\13\u0083\5"+
		"\u0083\u07b7\n\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3"+
		"\u0083\3\u0083\5\u0083\u07c1\n\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3"+
		"\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\5\u0083\u07d5\n\u0083\3\u0083"+
		"\3\u0083\5\u0083\u07d9\n\u0083\3\u0083\3\u0083\3\u0083\5\u0083\u07de\n"+
		"\u0083\3\u0083\3\u0083\5\u0083\u07e2\n\u0083\3\u0083\3\u0083\3\u0083\3"+
		"\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\5\u0083\u07f2\n\u0083\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\7\u0083\u0818\n\u0083\f\u0083"+
		"\16\u0083\u081b\13\u0083\5\u0083\u081d\n\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0083\3\u0083\5\u0083\u0826\n\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\5\u0083\u082c\n\u0083\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\5\u0083\u083b\n\u0083\3\u0083\7\u0083\u083e\n\u0083\f\u0083\16\u0083"+
		"\u0841\13\u0083\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084"+
		"\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\5\u0084\u0851"+
		"\n\u0084\3\u0084\3\u0084\5\u0084\u0855\n\u0084\3\u0084\3\u0084\3\u0084"+
		"\7\u0084\u085a\n\u0084\f\u0084\16\u0084\u085d\13\u0084\5\u0084\u085f\n"+
		"\u0084\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\6\u0085\u0866\n\u0085\r"+
		"\u0085\16\u0085\u0867\3\u0085\3\u0085\5\u0085\u086c\n\u0085\3\u0086\3"+
		"\u0086\5\u0086\u0870\n\u0086\3\u0086\3\u0086\5\u0086\u0874\n\u0086\3\u0086"+
		"\5\u0086\u0877\n\u0086\3\u0087\5\u0087\u087a\n\u0087\3\u0087\3\u0087\5"+
		"\u0087\u087e\n\u0087\3\u0087\3\u0087\3\u0087\3\u0087\5\u0087\u0884\n\u0087"+
		"\3\u0088\3\u0088\3\u0088\3\u0088\7\u0088\u088a\n\u0088\f\u0088\16\u0088"+
		"\u088d\13\u0088\5\u0088\u088f\n\u0088\3\u0088\3\u0088\3\u0089\3\u0089"+
		"\3\u0089\3\u0089\3\u0089\3\u0089\3\u0089\3\u0089\3\u0089\7\u0089\u089c"+
		"\n\u0089\f\u0089\16\u0089\u089f\13\u0089\5\u0089\u08a1\n\u0089\3\u0089"+
		"\3\u0089\3\u008a\3\u008a\3\u008a\3\u008a\5\u008a\u08a9\n\u008a\3\u008a"+
		"\3\u008a\3\u008a\5\u008a\u08ae\n\u008a\3\u008a\5\u008a\u08b1\n\u008a\3"+
		"\u008a\3\u008a\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\5\u008b\u08ba\n"+
		"\u008b\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\7\u008b\u08c2\n"+
		"\u008b\f\u008b\16\u008b\u08c5\13\u008b\3\u008b\3\u008b\3\u008c\3\u008c"+
		"\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c"+
		"\3\u008c\3\u008c\3\u008c\7\u008c\u08d7\n\u008c\f\u008c\16\u008c\u08da"+
		"\13\u008c\3\u008c\3\u008c\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d"+
		"\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d\7\u008d\u08eb"+
		"\n\u008d\f\u008d\16\u008d\u08ee\13\u008d\3\u008d\3\u008d\3\u008e\3\u008e"+
		"\3\u008e\3\u008e\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f\3\u0090\3\u0090"+
		"\3\u0090\3\u0090\3\u0090\3\u0090\3\u0090\3\u0090\3\u0091\3\u0091\3\u0091"+
		"\3\u0091\3\u0091\3\u0091\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092"+
		"\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\7\u0093\u0914\n\u0093\f\u0093"+
		"\16\u0093\u0917\13\u0093\5\u0093\u0919\n\u0093\3\u0093\3\u0093\3\u0093"+
		"\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\3\u0095\3\u0095\3\u0095\3\u0095"+
		"\3\u0095\3\u0095\3\u0096\3\u0096\3\u0096\3\u0096\3\u0096\3\u0096\3\u0096"+
		"\3\u0096\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097"+
		"\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098\3\u0099"+
		"\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099\3\u009a\3\u009a\3\u009a\3\u009a"+
		"\3\u009a\3\u009a\3\u009b\3\u009b\3\u009b\3\u009b\3\u009b\3\u009b\3\u009b"+
		"\3\u009b\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009d\3\u009d"+
		"\3\u009d\3\u009d\3\u009d\3\u009d\3\u009e\3\u009e\3\u009e\3\u009e\3\u009e"+
		"\3\u009e\3\u009f\3\u009f\3\u009f\5\u067c\u0685\u06c0\6\u00a8\u00aa\u00fa"+
		"\u0104\u00a0\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64"+
		"\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088"+
		"\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0"+
		"\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8"+
		"\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0"+
		"\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8"+
		"\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100"+
		"\u0102\u0104\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118"+
		"\u011a\u011c\u011e\u0120\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130"+
		"\u0132\u0134\u0136\u0138\u013a\u013c\2\23\4\2\23\23**\3\2OR\3\2ST\3\2"+
		"nq\4\2ff\u0082\u0082\5\2tt\u0080\u0080\u0086\u0086\3\2pq\4\2deij\4\2h"+
		"hkk\4\2ccu\177\3\2no\n\2\5\5\7\7\n\n\20\20\26\26\35\35\37\37\'\'\4\2>"+
		"N\u0091\u0091\4\2bb\u0086\u0087\4\2\u0099\u0099\u009f\u009f\3\2lm\7\2"+
		"\3\22\24\64UUYY\u0091\u0091\u0a8c\2\u013f\3\2\2\2\4\u0152\3\2\2\2\6\u0157"+
		"\3\2\2\2\b\u015d\3\2\2\2\n\u0164\3\2\2\2\f\u017c\3\2\2\2\16\u0183\3\2"+
		"\2\2\20\u018d\3\2\2\2\22\u0191\3\2\2\2\24\u0193\3\2\2\2\26\u01a2\3\2\2"+
		"\2\30\u01b0\3\2\2\2\32\u01b8\3\2\2\2\34\u01c0\3\2\2\2\36\u01d2\3\2\2\2"+
		" \u01dd\3\2\2\2\"\u01e7\3\2\2\2$\u01ee\3\2\2\2&\u01f9\3\2\2\2(\u0202\3"+
		"\2\2\2*\u0218\3\2\2\2,\u0223\3\2\2\2.\u0225\3\2\2\2\60\u0237\3\2\2\2\62"+
		"\u023b\3\2\2\2\64\u023d\3\2\2\2\66\u0240\3\2\2\28\u0243\3\2\2\2:\u024b"+
		"\3\2\2\2<\u0258\3\2\2\2>\u0261\3\2\2\2@\u0263\3\2\2\2B\u026e\3\2\2\2D"+
		"\u027c\3\2\2\2F\u02a0\3\2\2\2H\u02a2\3\2\2\2J\u02a5\3\2\2\2L\u02ad\3\2"+
		"\2\2N\u02b2\3\2\2\2P\u02bc\3\2\2\2R\u02be\3\2\2\2T\u02ce\3\2\2\2V\u02e2"+
		"\3\2\2\2X\u02e4\3\2\2\2Z\u02ec\3\2\2\2\\\u02ff\3\2\2\2^\u0304\3\2\2\2"+
		"`\u030d\3\2\2\2b\u0314\3\2\2\2d\u0322\3\2\2\2f\u0324\3\2\2\2h\u0326\3"+
		"\2\2\2j\u0328\3\2\2\2l\u0332\3\2\2\2n\u033a\3\2\2\2p\u0341\3\2\2\2r\u0343"+
		"\3\2\2\2t\u0353\3\2\2\2v\u0358\3\2\2\2x\u036a\3\2\2\2z\u0380\3\2\2\2|"+
		"\u0384\3\2\2\2~\u0386\3\2\2\2\u0080\u038c\3\2\2\2\u0082\u038e\3\2\2\2"+
		"\u0084\u0391\3\2\2\2\u0086\u03a0\3\2\2\2\u0088\u03a5\3\2\2\2\u008a\u03b6"+
		"\3\2\2\2\u008c\u0422\3\2\2\2\u008e\u0424\3\2\2\2\u0090\u0431\3\2\2\2\u0092"+
		"\u0439\3\2\2\2\u0094\u043c\3\2\2\2\u0096\u0443\3\2\2\2\u0098\u044e\3\2"+
		"\2\2\u009a\u0457\3\2\2\2\u009c\u0469\3\2\2\2\u009e\u0477\3\2\2\2\u00a0"+
		"\u047b\3\2\2\2\u00a2\u0480\3\2\2\2\u00a4\u0488\3\2\2\2\u00a6\u048c\3\2"+
		"\2\2\u00a8\u04ba\3\2\2\2\u00aa\u0539\3\2\2\2\u00ac\u054d\3\2\2\2\u00ae"+
		"\u0562\3\2\2\2\u00b0\u0566\3\2\2\2\u00b2\u057a\3\2\2\2\u00b4\u057f\3\2"+
		"\2\2\u00b6\u0594\3\2\2\2\u00b8\u05a5\3\2\2\2\u00ba\u05a7\3\2\2\2\u00bc"+
		"\u05ad\3\2\2\2\u00be\u05cb\3\2\2\2\u00c0\u05cf\3\2\2\2\u00c2\u05d5\3\2"+
		"\2\2\u00c4\u05da\3\2\2\2\u00c6\u05dc\3\2\2\2\u00c8\u05e0\3\2\2\2\u00ca"+
		"\u05e9\3\2\2\2\u00cc\u05f6\3\2\2\2\u00ce\u05f8\3\2\2\2\u00d0\u0609\3\2"+
		"\2\2\u00d2\u0610\3\2\2\2\u00d4\u0612\3\2\2\2\u00d6\u0618\3\2\2\2\u00d8"+
		"\u0622\3\2\2\2\u00da\u0624\3\2\2\2\u00dc\u0628\3\2\2\2\u00de\u062a\3\2"+
		"\2\2\u00e0\u062c\3\2\2\2\u00e2\u0644\3\2\2\2\u00e4\u0656\3\2\2\2\u00e6"+
		"\u066e\3\2\2\2\u00e8\u0674\3\2\2\2\u00ea\u0689\3\2\2\2\u00ec\u068b\3\2"+
		"\2\2\u00ee\u0696\3\2\2\2\u00f0\u06b9\3\2\2\2\u00f2\u06bb\3\2\2\2\u00f4"+
		"\u06c5\3\2\2\2\u00f6\u06cd\3\2\2\2\u00f8\u06fe\3\2\2\2\u00fa\u0722\3\2"+
		"\2\2\u00fc\u0742\3\2\2\2\u00fe\u077b\3\2\2\2\u0100\u077d\3\2\2\2\u0102"+
		"\u07a9\3\2\2\2\u0104\u07e1\3\2\2\2\u0106\u085e\3\2\2\2\u0108\u086b\3\2"+
		"\2\2\u010a\u0876\3\2\2\2\u010c\u0883\3\2\2\2\u010e\u0885\3\2\2\2\u0110"+
		"\u0892\3\2\2\2\u0112\u08a4\3\2\2\2\u0114\u08b4\3\2\2\2\u0116\u08c8\3\2"+
		"\2\2\u0118\u08dd\3\2\2\2\u011a\u08f1\3\2\2\2\u011c\u08f5\3\2\2\2\u011e"+
		"\u08fa\3\2\2\2\u0120\u0902\3\2\2\2\u0122\u0908\3\2\2\2\u0124\u090e\3\2"+
		"\2\2\u0126\u091d\3\2\2\2\u0128\u0922\3\2\2\2\u012a\u0928\3\2\2\2\u012c"+
		"\u0930\3\2\2\2\u012e\u0938\3\2\2\2\u0130\u0940\3\2\2\2\u0132\u0946\3\2"+
		"\2\2\u0134\u094c\3\2\2\2\u0136\u0954\3\2\2\2\u0138\u095a\3\2\2\2\u013a"+
		"\u0960\3\2\2\2\u013c\u0966\3\2\2\2\u013e\u0140\5\b\5\2\u013f\u013e\3\2"+
		"\2\2\u013f\u0140\3\2\2\2\u0140\u0144\3\2\2\2\u0141\u0143\5\n\6\2\u0142"+
		"\u0141\3\2\2\2\u0143\u0146\3\2\2\2\u0144\u0142\3\2\2\2\u0144\u0145\3\2"+
		"\2\2\u0145\u014a\3\2\2\2\u0146\u0144\3\2\2\2\u0147\u0149\5\f\7\2\u0148"+
		"\u0147\3\2\2\2\u0149\u014c\3\2\2\2\u014a\u0148\3\2\2\2\u014a\u014b\3\2"+
		"\2\2\u014b\u014d\3\2\2\2\u014c\u014a\3\2\2\2\u014d\u014e\7\2\2\3\u014e"+
		"\3\3\2\2\2\u014f\u0151\5*\26\2\u0150\u014f\3\2\2\2\u0151\u0154\3\2\2\2"+
		"\u0152\u0150\3\2\2\2\u0152\u0153\3\2\2\2\u0153\u0155\3\2\2\2\u0154\u0152"+
		"\3\2\2\2\u0155\u0156\7\2\2\3\u0156\5\3\2\2\2\u0157\u0158\5\u00a8U\2\u0158"+
		"\u0159\7\2\2\3\u0159\7\3\2\2\2\u015a\u015c\5j\66\2\u015b\u015a\3\2\2\2"+
		"\u015c\u015f\3\2\2\2\u015d\u015b\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u0160"+
		"\3\2\2\2\u015f\u015d\3\2\2\2\u0160\u0161\7\"\2\2\u0161\u0162\5b\62\2\u0162"+
		"\u0163\7`\2\2\u0163\t\3\2\2\2\u0164\u0166\7\33\2\2\u0165\u0167\7(\2\2"+
		"\u0166\u0165\3\2\2\2\u0166\u0167\3\2\2\2\u0167\u0168\3\2\2\2\u0168\u016b"+
		"\5b\62\2\u0169\u016a\7b\2\2\u016a\u016c\7\u0080\2\2\u016b\u0169\3\2\2"+
		"\2\u016b\u016c\3\2\2\2\u016c\u016d\3\2\2\2\u016d\u016e\7`\2\2\u016e\13"+
		"\3\2\2\2\u016f\u0171\5\20\t\2\u0170\u016f\3\2\2\2\u0171\u0174\3\2\2\2"+
		"\u0172\u0170\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0179\3\2\2\2\u0174\u0172"+
		"\3\2\2\2\u0175\u017a\5\24\13\2\u0176\u017a\5\34\17\2\u0177\u017a\5$\23"+
		"\2\u0178\u017a\5t;\2\u0179\u0175\3\2\2\2\u0179\u0176\3\2\2\2\u0179\u0177"+
		"\3\2\2\2\u0179\u0178\3\2\2\2\u017a\u017d\3\2\2\2\u017b\u017d\7`\2\2\u017c"+
		"\u0172\3\2\2\2\u017c\u017b\3\2\2\2\u017d\r\3\2\2\2\u017e\u0184\5\20\t"+
		"\2\u017f\u0184\7 \2\2\u0180\u0184\7,\2\2\u0181\u0184\7\60\2\2\u0182\u0184"+
		"\7\63\2\2\u0183\u017e\3\2\2\2\u0183\u017f\3\2\2\2\u0183\u0180\3\2\2\2"+
		"\u0183\u0181\3\2\2\2\u0183\u0182\3\2\2\2\u0184\17\3\2\2\2\u0185\u018e"+
		"\5j\66\2\u0186\u018e\7%\2\2\u0187\u018e\7$\2\2\u0188\u018e\7#\2\2\u0189"+
		"\u018e\7(\2\2\u018a\u018e\7\3\2\2\u018b\u018e\7\24\2\2\u018c\u018e\7)"+
		"\2\2\u018d\u0185\3\2\2\2\u018d\u0186\3\2\2\2\u018d\u0187\3\2\2\2\u018d"+
		"\u0188\3\2\2\2\u018d\u0189\3\2\2\2\u018d\u018a\3\2\2\2\u018d\u018b\3\2"+
		"\2\2\u018d\u018c\3\2\2\2\u018e\21\3\2\2\2\u018f\u0192\7\24\2\2\u0190\u0192"+
		"\5j\66\2\u0191\u018f\3\2\2\2\u0191\u0190\3\2\2\2\u0192\23\3\2\2\2\u0193"+
		"\u0194\7\13\2\2\u0194\u0196\5\u00d6l\2\u0195\u0197\5\26\f\2\u0196\u0195"+
		"\3\2\2\2\u0196\u0197\3\2\2\2\u0197\u019a\3\2\2\2\u0198\u0199\7\23\2\2"+
		"\u0199\u019b\5\u00caf\2\u019a\u0198\3\2\2\2\u019a\u019b\3\2\2\2\u019b"+
		"\u019e\3\2\2\2\u019c\u019d\7\32\2\2\u019d\u019f\5\u00c8e\2\u019e\u019c"+
		"\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0\u01a1\5&\24\2\u01a1"+
		"\25\3\2\2\2\u01a2\u01a3\7e\2\2\u01a3\u01a8\5\30\r\2\u01a4\u01a5\7a\2\2"+
		"\u01a5\u01a7\5\30\r\2\u01a6\u01a4\3\2\2\2\u01a7\u01aa\3\2\2\2\u01a8\u01a6"+
		"\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01ab\3\2\2\2\u01aa\u01a8\3\2\2\2\u01ab"+
		"\u01ac\7d\2\2\u01ac\27\3\2\2\2\u01ad\u01af\5j\66\2\u01ae\u01ad\3\2\2\2"+
		"\u01af\u01b2\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b3"+
		"\3\2\2\2\u01b2\u01b0\3\2\2\2\u01b3\u01b6\5\u00d6l\2\u01b4\u01b5\7\23\2"+
		"\2\u01b5\u01b7\5\32\16\2\u01b6\u01b4\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7"+
		"\31\3\2\2\2\u01b8\u01bd\5\u00caf\2\u01b9\u01ba\7r\2\2\u01ba\u01bc\5\u00ca"+
		"f\2\u01bb\u01b9\3\2\2\2\u01bc\u01bf\3\2\2\2\u01bd\u01bb\3\2\2\2\u01bd"+
		"\u01be\3\2\2\2\u01be\33\3\2\2\2\u01bf\u01bd\3\2\2\2\u01c0\u01c1\7\22\2"+
		"\2\u01c1\u01c4\5\u00d6l\2\u01c2\u01c3\7\32\2\2\u01c3\u01c5\5\u00c8e\2"+
		"\u01c4\u01c2\3\2\2\2\u01c4\u01c5\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01c8"+
		"\7\\\2\2\u01c7\u01c9\5\36\20\2\u01c8\u01c7\3\2\2\2\u01c8\u01c9\3\2\2\2"+
		"\u01c9\u01cb\3\2\2\2\u01ca\u01cc\7a\2\2\u01cb\u01ca\3\2\2\2\u01cb\u01cc"+
		"\3\2\2\2\u01cc\u01ce\3\2\2\2\u01cd\u01cf\5\"\22\2\u01ce\u01cd\3\2\2\2"+
		"\u01ce\u01cf\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01d1\7]\2\2\u01d1\35\3"+
		"\2\2\2\u01d2\u01d7\5 \21\2\u01d3\u01d4\7a\2\2\u01d4\u01d6\5 \21\2\u01d5"+
		"\u01d3\3\2\2\2\u01d6\u01d9\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d8\3\2"+
		"\2\2\u01d8\37\3\2\2\2\u01d9\u01d7\3\2\2\2\u01da\u01dc\5j\66\2\u01db\u01da"+
		"\3\2\2\2\u01dc\u01df\3\2\2\2\u01dd\u01db\3\2\2\2\u01dd\u01de\3\2\2\2\u01de"+
		"\u01e0\3\2\2\2\u01df\u01dd\3\2\2\2\u01e0\u01e2\5\u00d6l\2\u01e1\u01e3"+
		"\5\u00d4k\2\u01e2\u01e1\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3\u01e5\3\2\2"+
		"\2\u01e4\u01e6\5&\24\2\u01e5\u01e4\3\2\2\2\u01e5\u01e6\3\2\2\2\u01e6!"+
		"\3\2\2\2\u01e7\u01eb\7`\2\2\u01e8\u01ea\5*\26\2\u01e9\u01e8\3\2\2\2\u01ea"+
		"\u01ed\3\2\2\2\u01eb\u01e9\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec#\3\2\2\2"+
		"\u01ed\u01eb\3\2\2\2\u01ee\u01ef\7\36\2\2\u01ef\u01f1\5\u00d6l\2\u01f0"+
		"\u01f2\5\26\f\2\u01f1\u01f0\3\2\2\2\u01f1\u01f2\3\2\2\2\u01f2\u01f5\3"+
		"\2\2\2\u01f3\u01f4\7\23\2\2\u01f4\u01f6\5\u00c8e\2\u01f5\u01f3\3\2\2\2"+
		"\u01f5\u01f6\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7\u01f8\5(\25\2\u01f8%\3"+
		"\2\2\2\u01f9\u01fd\7\\\2\2\u01fa\u01fc\5*\26\2\u01fb\u01fa\3\2\2\2\u01fc"+
		"\u01ff\3\2\2\2\u01fd\u01fb\3\2\2\2\u01fd\u01fe\3\2\2\2\u01fe\u0200\3\2"+
		"\2\2\u01ff\u01fd\3\2\2\2\u0200\u0201\7]\2\2\u0201\'\3\2\2\2\u0202\u0206"+
		"\7\\\2\2\u0203\u0205\5<\37\2\u0204\u0203\3\2\2\2\u0205\u0208\3\2\2\2\u0206"+
		"\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207\u0209\3\2\2\2\u0208\u0206\3\2"+
		"\2\2\u0209\u020a\7]\2\2\u020a)\3\2\2\2\u020b\u0219\7`\2\2\u020c\u020e"+
		"\7(\2\2\u020d\u020c\3\2\2\2\u020d\u020e\3\2\2\2\u020e\u020f\3\2\2\2\u020f"+
		"\u0219\5\u0084C\2\u0210\u0212\5\16\b\2\u0211\u0210\3\2\2\2\u0212\u0215"+
		"\3\2\2\2\u0213\u0211\3\2\2\2\u0213\u0214\3\2\2\2\u0214\u0216\3\2\2\2\u0215"+
		"\u0213\3\2\2\2\u0216\u0219\5,\27\2\u0217\u0219\5\u00d8m\2\u0218\u020b"+
		"\3\2\2\2\u0218\u020d\3\2\2\2\u0218\u0213\3\2\2\2\u0218\u0217\3\2\2\2\u0219"+
		"+\3\2\2\2\u021a\u0224\5.\30\2\u021b\u0224\5\64\33\2\u021c\u0224\5:\36"+
		"\2\u021d\u0224\58\35\2\u021e\u0224\5\66\34\2\u021f\u0224\5$\23\2\u0220"+
		"\u0224\5t;\2\u0221\u0224\5\24\13\2\u0222\u0224\5\34\17\2\u0223\u021a\3"+
		"\2\2\2\u0223\u021b\3\2\2\2\u0223\u021c\3\2\2\2\u0223\u021d\3\2\2\2\u0223"+
		"\u021e\3\2\2\2\u0223\u021f\3\2\2\2\u0223\u0220\3\2\2\2\u0223\u0221\3\2"+
		"\2\2\u0223\u0222\3\2\2\2\u0224-\3\2\2\2\u0225\u0226\5\62\32\2\u0226\u0227"+
		"\5\u00d6l\2\u0227\u022c\5Z.\2\u0228\u0229\7^\2\2\u0229\u022b\7_\2\2\u022a"+
		"\u0228\3\2\2\2\u022b\u022e\3\2\2\2\u022c\u022a\3\2\2\2\u022c\u022d\3\2"+
		"\2\2\u022d\u0231\3\2\2\2\u022e\u022c\3\2\2\2\u022f\u0230\7/\2\2\u0230"+
		"\u0232\5X-\2\u0231\u022f\3\2\2\2\u0231\u0232\3\2\2\2\u0232\u0233\3\2\2"+
		"\2\u0233\u0234\5\60\31\2\u0234/\3\2\2\2\u0235\u0238\5\u0084C\2\u0236\u0238"+
		"\7`\2\2\u0237\u0235\3\2\2\2\u0237\u0236\3\2\2\2\u0238\61\3\2\2\2\u0239"+
		"\u023c\5\u00caf\2\u023a\u023c\7\62\2\2\u023b\u0239\3\2\2\2\u023b\u023a"+
		"\3\2\2\2\u023c\63\3\2\2\2\u023d\u023e\5\26\f\2\u023e\u023f\5.\30\2\u023f"+
		"\65\3\2\2\2\u0240\u0241\5\26\f\2\u0241\u0242\58\35\2\u0242\67\3\2\2\2"+
		"\u0243\u0244\5\u00d6l\2\u0244\u0247\5Z.\2\u0245\u0246\7/\2\2\u0246\u0248"+
		"\5X-\2\u0247\u0245\3\2\2\2\u0247\u0248\3\2\2\2\u0248\u0249\3\2\2\2\u0249"+
		"\u024a\5\u0084C\2\u024a9\3\2\2\2\u024b\u024c\5\u00caf\2\u024c\u024d\5"+
		"J&\2\u024d\u024e\7`\2\2\u024e;\3\2\2\2\u024f\u0251\5\16\b\2\u0250\u024f"+
		"\3\2\2\2\u0251\u0254\3\2\2\2\u0252\u0250\3\2\2\2\u0252\u0253\3\2\2\2\u0253"+
		"\u0255\3\2\2\2\u0254\u0252\3\2\2\2\u0255\u0259\5> \2\u0256\u0259\5\u00d8"+
		"m\2\u0257\u0259\7`\2\2\u0258\u0252\3\2\2\2\u0258\u0256\3\2\2\2\u0258\u0257"+
		"\3\2\2\2\u0259=\3\2\2\2\u025a\u0262\5@!\2\u025b\u0262\5D#\2\u025c\u0262"+
		"\5H%\2\u025d\u0262\5$\23\2\u025e\u0262\5t;\2\u025f\u0262\5\24\13\2\u0260"+
		"\u0262\5\34\17\2\u0261\u025a\3\2\2\2\u0261\u025b\3\2\2\2\u0261\u025c\3"+
		"\2\2\2\u0261\u025d\3\2\2\2\u0261\u025e\3\2\2\2\u0261\u025f\3\2\2\2\u0261"+
		"\u0260\3\2\2\2\u0262?\3\2\2\2\u0263\u0264\5\u00caf\2\u0264\u0269\5B\""+
		"\2\u0265\u0266\7a\2\2\u0266\u0268\5B\"\2\u0267\u0265\3\2\2\2\u0268\u026b"+
		"\3\2\2\2\u0269\u0267\3\2\2\2\u0269\u026a\3\2\2\2\u026a\u026c\3\2\2\2\u026b"+
		"\u0269\3\2\2\2\u026c\u026d\7`\2\2\u026dA\3\2\2\2\u026e\u0273\5\u00d6l"+
		"\2\u026f\u0270\7^\2\2\u0270\u0272\7_\2\2\u0271\u026f\3\2\2\2\u0272\u0275"+
		"\3\2\2\2\u0273\u0271\3\2\2\2\u0273\u0274\3\2\2\2\u0274\u0276\3\2\2\2\u0275"+
		"\u0273\3\2\2\2\u0276\u0277\7c\2\2\u0277\u0278\5P)\2\u0278C\3\2\2\2\u0279"+
		"\u027b\5F$\2\u027a\u0279\3\2\2\2\u027b\u027e\3\2\2\2\u027c\u027a\3\2\2"+
		"\2\u027c\u027d\3\2\2\2\u027d\u0289\3\2\2\2\u027e\u027c\3\2\2\2\u027f\u028a"+
		"\5\62\32\2\u0280\u0284\5\26\f\2\u0281\u0283\5j\66\2\u0282\u0281\3\2\2"+
		"\2\u0283\u0286\3\2\2\2\u0284\u0282\3\2\2\2\u0284\u0285\3\2\2\2\u0285\u0287"+
		"\3\2\2\2\u0286\u0284\3\2\2\2\u0287\u0288\5\62\32\2\u0288\u028a\3\2\2\2"+
		"\u0289\u027f\3\2\2\2\u0289\u0280\3\2\2\2\u028a\u028b\3\2\2\2\u028b\u028c"+
		"\5\u00d6l\2\u028c\u0291\5Z.\2\u028d\u028e\7^\2\2\u028e\u0290\7_\2\2\u028f"+
		"\u028d\3\2\2\2\u0290\u0293\3\2\2\2\u0291\u028f\3\2\2\2\u0291\u0292\3\2"+
		"\2\2\u0292\u0296\3\2\2\2\u0293\u0291\3\2\2\2\u0294\u0295\7/\2\2\u0295"+
		"\u0297\5X-\2\u0296\u0294\3\2\2\2\u0296\u0297\3\2\2\2\u0297\u0298\3\2\2"+
		"\2\u0298\u0299\5\60\31\2\u0299E\3\2\2\2\u029a\u02a1\5j\66\2\u029b\u02a1"+
		"\7%\2\2\u029c\u02a1\7\3\2\2\u029d\u02a1\7\16\2\2\u029e\u02a1\7(\2\2\u029f"+
		"\u02a1\7)\2\2\u02a0\u029a\3\2\2\2\u02a0\u029b\3\2\2\2\u02a0\u029c\3\2"+
		"\2\2\u02a0\u029d\3\2\2\2\u02a0\u029e\3\2\2\2\u02a0\u029f\3\2\2\2\u02a1"+
		"G\3\2\2\2\u02a2\u02a3\5\26\f\2\u02a3\u02a4\5D#\2\u02a4I\3\2\2\2\u02a5"+
		"\u02aa\5L\'\2\u02a6\u02a7\7a\2\2\u02a7\u02a9\5L\'\2\u02a8\u02a6\3\2\2"+
		"\2\u02a9\u02ac\3\2\2\2\u02aa\u02a8\3\2\2\2\u02aa\u02ab\3\2\2\2\u02abK"+
		"\3\2\2\2\u02ac\u02aa\3\2\2\2\u02ad\u02b0\5N(\2\u02ae\u02af\7c\2\2\u02af"+
		"\u02b1\5P)\2\u02b0\u02ae\3\2\2\2\u02b0\u02b1\3\2\2\2\u02b1M\3\2\2\2\u02b2"+
		"\u02b7\5\u00d6l\2\u02b3\u02b4\7^\2\2\u02b4\u02b6\7_\2\2\u02b5\u02b3\3"+
		"\2\2\2\u02b6\u02b9\3\2\2\2\u02b7\u02b5\3\2\2\2\u02b7\u02b8\3\2\2\2\u02b8"+
		"O\3\2\2\2\u02b9\u02b7\3\2\2\2\u02ba\u02bd\5R*\2\u02bb\u02bd\5\u00a8U\2"+
		"\u02bc\u02ba\3\2\2\2\u02bc\u02bb\3\2\2\2\u02bdQ\3\2\2\2\u02be\u02ca\7"+
		"\\\2\2\u02bf\u02c4\5P)\2\u02c0\u02c1\7a\2\2\u02c1\u02c3\5P)\2\u02c2\u02c0"+
		"\3\2\2\2\u02c3\u02c6\3\2\2\2\u02c4\u02c2\3\2\2\2\u02c4\u02c5\3\2\2\2\u02c5"+
		"\u02c8\3\2\2\2\u02c6\u02c4\3\2\2\2\u02c7\u02c9\7a\2\2\u02c8\u02c7\3\2"+
		"\2\2\u02c8\u02c9\3\2\2\2\u02c9\u02cb\3\2\2\2\u02ca\u02bf\3\2\2\2\u02ca"+
		"\u02cb\3\2\2\2\u02cb\u02cc\3\2\2\2\u02cc\u02cd\7]\2\2\u02cdS\3\2\2\2\u02ce"+
		"\u02d0\5\u00d6l\2\u02cf\u02d1\5\u00ceh\2\u02d0\u02cf\3\2\2\2\u02d0\u02d1"+
		"\3\2\2\2\u02d1\u02d9\3\2\2\2\u02d2\u02d3\7b\2\2\u02d3\u02d5\5\u00d6l\2"+
		"\u02d4\u02d6\5\u00ceh\2\u02d5\u02d4\3\2\2\2\u02d5\u02d6\3\2\2\2\u02d6"+
		"\u02d8\3\2\2\2\u02d7\u02d2\3\2\2\2\u02d8\u02db\3\2\2\2\u02d9\u02d7\3\2"+
		"\2\2\u02d9\u02da\3\2\2\2\u02daU\3\2\2\2\u02db\u02d9\3\2\2\2\u02dc\u02e3"+
		"\5\u00caf\2\u02dd\u02e0\7\u0084\2\2\u02de\u02df\t\2\2\2\u02df\u02e1\5"+
		"\u00caf\2\u02e0\u02de\3\2\2\2\u02e0\u02e1\3\2\2\2\u02e1\u02e3\3\2\2\2"+
		"\u02e2\u02dc\3\2\2\2\u02e2\u02dd\3\2\2\2\u02e3W\3\2\2\2\u02e4\u02e9\5"+
		"b\62\2\u02e5\u02e6\7a\2\2\u02e6\u02e8\5b\62\2\u02e7\u02e5\3\2\2\2\u02e8"+
		"\u02eb\3\2\2\2\u02e9\u02e7\3\2\2\2\u02e9\u02ea\3\2\2\2\u02eaY\3\2\2\2"+
		"\u02eb\u02e9\3\2\2\2\u02ec\u02ee\7Z\2\2\u02ed\u02ef\5\\/\2\u02ee\u02ed"+
		"\3\2\2\2\u02ee\u02ef\3\2\2\2\u02ef\u02f0\3\2\2\2\u02f0\u02f1\7[\2\2\u02f1"+
		"[\3\2\2\2\u02f2\u02f7\5^\60\2\u02f3\u02f4\7a\2\2\u02f4\u02f6\5^\60\2\u02f5"+
		"\u02f3\3\2\2\2\u02f6\u02f9\3\2\2\2\u02f7\u02f5\3\2\2\2\u02f7\u02f8\3\2"+
		"\2\2\u02f8\u02fc\3\2\2\2\u02f9\u02f7\3\2\2\2\u02fa\u02fb\7a\2\2\u02fb"+
		"\u02fd\5`\61\2\u02fc\u02fa\3\2\2\2\u02fc\u02fd\3\2\2\2\u02fd\u0300\3\2"+
		"\2\2\u02fe\u0300\5`\61\2\u02ff\u02f2\3\2\2\2\u02ff\u02fe\3\2\2\2\u0300"+
		"]\3\2\2\2\u0301\u0303\5\22\n\2\u0302\u0301\3\2\2\2\u0303\u0306\3\2\2\2"+
		"\u0304\u0302\3\2\2\2\u0304\u0305\3\2\2\2\u0305\u0307\3\2\2\2\u0306\u0304"+
		"\3\2\2\2\u0307\u0308\5\u00caf\2\u0308\u0309\5N(\2\u0309_\3\2\2\2\u030a"+
		"\u030c\5\22\n\2\u030b\u030a\3\2\2\2\u030c\u030f\3\2\2\2\u030d\u030b\3"+
		"\2\2\2\u030d\u030e\3\2\2\2\u030e\u0310\3\2\2\2\u030f\u030d\3\2\2\2\u0310"+
		"\u0311\5\u00caf\2\u0311\u0312\7\u008d\2\2\u0312\u0313\5N(\2\u0313a\3\2"+
		"\2\2\u0314\u0319\5\u00d6l\2\u0315\u0316\7b\2\2\u0316\u0318\5\u00d6l\2"+
		"\u0317\u0315\3\2\2\2\u0318\u031b\3\2\2\2\u0319\u0317\3\2\2\2\u0319\u031a"+
		"\3\2\2\2\u031ac\3\2\2\2\u031b\u0319\3\2\2\2\u031c\u0323\5f\64\2\u031d"+
		"\u0323\5h\65\2\u031e\u0323\7V\2\2\u031f\u0323\7X\2\2\u0320\u0323\7U\2"+
		"\2\u0321\u0323\7Y\2\2\u0322\u031c\3\2\2\2\u0322\u031d\3\2\2\2\u0322\u031e"+
		"\3\2\2\2\u0322\u031f\3\2\2\2\u0322\u0320\3\2\2\2\u0322\u0321\3\2\2\2\u0323"+
		"e\3\2\2\2\u0324\u0325\t\3\2\2\u0325g\3\2\2\2\u0326\u0327\t\4\2\2\u0327"+
		"i\3\2\2\2\u0328\u0329\7\u008c\2\2\u0329\u0330\5b\62\2\u032a\u032d\7Z\2"+
		"\2\u032b\u032e\5l\67\2\u032c\u032e\5p9\2\u032d\u032b\3\2\2\2\u032d\u032c"+
		"\3\2\2\2\u032d\u032e\3\2\2\2\u032e\u032f\3\2\2\2\u032f\u0331\7[\2\2\u0330"+
		"\u032a\3\2\2\2\u0330\u0331\3\2\2\2\u0331k\3\2\2\2\u0332\u0337\5n8\2\u0333"+
		"\u0334\7a\2\2\u0334\u0336\5n8\2\u0335\u0333\3\2\2\2\u0336\u0339\3\2\2"+
		"\2\u0337\u0335\3\2\2\2\u0337\u0338\3\2\2\2\u0338m\3\2\2\2\u0339\u0337"+
		"\3\2\2\2\u033a\u033b\5\u00d6l\2\u033b\u033c\7c\2\2\u033c\u033d\5p9\2\u033d"+
		"o\3\2\2\2\u033e\u0342\5\u00a8U\2\u033f\u0342\5j\66\2\u0340\u0342\5r:\2"+
		"\u0341\u033e\3\2\2\2\u0341\u033f\3\2\2\2\u0341\u0340\3\2\2\2\u0342q\3"+
		"\2\2\2\u0343\u034c\7\\\2\2\u0344\u0349\5p9\2\u0345\u0346\7a\2\2\u0346"+
		"\u0348\5p9\2\u0347\u0345\3\2\2\2\u0348\u034b\3\2\2\2\u0349\u0347\3\2\2"+
		"\2\u0349\u034a\3\2\2\2\u034a\u034d\3\2\2\2\u034b\u0349\3\2\2\2\u034c\u0344"+
		"\3\2\2\2\u034c\u034d\3\2\2\2\u034d\u034f\3\2\2\2\u034e\u0350\7a\2\2\u034f"+
		"\u034e\3\2\2\2\u034f\u0350\3\2\2\2\u0350\u0351\3\2\2\2\u0351\u0352\7]"+
		"\2\2\u0352s\3\2\2\2\u0353\u0354\7\u008c\2\2\u0354\u0355\7\36\2\2\u0355"+
		"\u0356\5\u00d6l\2\u0356\u0357\5v<\2\u0357u\3\2\2\2\u0358\u035c\7\\\2\2"+
		"\u0359\u035b\5x=\2\u035a\u0359\3\2\2\2\u035b\u035e\3\2\2\2\u035c\u035a"+
		"\3\2\2\2\u035c\u035d\3\2\2\2\u035d\u035f\3\2\2\2\u035e\u035c\3\2\2\2\u035f"+
		"\u0360\7]\2\2\u0360w\3\2\2\2\u0361\u0363\5\16\b\2\u0362\u0361\3\2\2\2"+
		"\u0363\u0366\3\2\2\2\u0364\u0362\3\2\2\2\u0364\u0365\3\2\2\2\u0365\u0367"+
		"\3\2\2\2\u0366\u0364\3\2\2\2\u0367\u036b\5z>\2\u0368\u036b\5\u00d8m\2"+
		"\u0369\u036b\7`\2\2\u036a\u0364\3\2\2\2\u036a\u0368\3\2\2\2\u036a\u0369"+
		"\3\2\2\2\u036by\3\2\2\2\u036c\u036d\5\u00caf\2\u036d\u036e\5|?\2\u036e"+
		"\u036f\7`\2\2\u036f\u0381\3\2\2\2\u0370\u0372\5\24\13\2\u0371\u0373\7"+
		"`\2\2\u0372\u0371\3\2\2\2\u0372\u0373\3\2\2\2\u0373\u0381\3\2\2\2\u0374"+
		"\u0376\5$\23\2\u0375\u0377\7`\2\2\u0376\u0375\3\2\2\2\u0376\u0377\3\2"+
		"\2\2\u0377\u0381\3\2\2\2\u0378\u037a\5\34\17\2\u0379\u037b\7`\2\2\u037a"+
		"\u0379\3\2\2\2\u037a\u037b\3\2\2\2\u037b\u0381\3\2\2\2\u037c\u037e\5t"+
		";\2\u037d\u037f\7`\2\2\u037e\u037d\3\2\2\2\u037e\u037f\3\2\2\2\u037f\u0381"+
		"\3\2\2\2\u0380\u036c\3\2\2\2\u0380\u0370\3\2\2\2\u0380\u0374\3\2\2\2\u0380"+
		"\u0378\3\2\2\2\u0380\u037c\3\2\2\2\u0381{\3\2\2\2\u0382\u0385\5~@\2\u0383"+
		"\u0385\5\u0080A\2\u0384\u0382\3\2\2\2\u0384\u0383\3\2\2\2\u0385}\3\2\2"+
		"\2\u0386\u0387\5\u00d6l\2\u0387\u0388\7Z\2\2\u0388\u038a\7[\2\2\u0389"+
		"\u038b\5\u0082B\2\u038a\u0389\3\2\2\2\u038a\u038b\3\2\2\2\u038b\177\3"+
		"\2\2\2\u038c\u038d\5J&\2\u038d\u0081\3\2\2\2\u038e\u038f\7\16\2\2\u038f"+
		"\u0390\5p9\2\u0390\u0083\3\2\2\2\u0391\u0395\7\\\2\2\u0392\u0394\5\u0086"+
		"D\2\u0393\u0392\3\2\2\2\u0394\u0397\3\2\2\2\u0395\u0393\3\2\2\2\u0395"+
		"\u0396\3\2\2\2\u0396\u0398\3\2\2\2\u0397\u0395\3\2\2\2\u0398\u0399\7]"+
		"\2\2\u0399\u0085\3\2\2\2\u039a\u039b\5\u0088E\2\u039b\u039c\7`\2\2\u039c"+
		"\u03a1\3\2\2\2\u039d\u03a1\5\u008cG\2\u039e\u03a1\5\u008aF\2\u039f\u03a1"+
		"\5\u00d8m\2\u03a0\u039a\3\2\2\2\u03a0\u039d\3\2\2\2\u03a0\u039e\3\2\2"+
		"\2\u03a0\u039f\3\2\2\2\u03a1\u0087\3\2\2\2\u03a2\u03a4\5\22\n\2\u03a3"+
		"\u03a2\3\2\2\2\u03a4\u03a7\3\2\2\2\u03a5\u03a3\3\2\2\2\u03a5\u03a6\3\2"+
		"\2\2\u03a6\u03a8\3\2\2\2\u03a7\u03a5\3\2\2\2\u03a8\u03a9\5\u00caf\2\u03a9"+
		"\u03aa\5J&\2\u03aa\u0089\3\2\2\2\u03ab\u03ad\5\20\t\2\u03ac\u03ab\3\2"+
		"\2\2\u03ad\u03b0\3\2\2\2\u03ae\u03ac\3\2\2\2\u03ae\u03af\3\2\2\2\u03af"+
		"\u03b3\3\2\2\2\u03b0\u03ae\3\2\2\2\u03b1\u03b4\5\24\13\2\u03b2\u03b4\5"+
		"$\23\2\u03b3\u03b1\3\2\2\2\u03b3\u03b2\3\2\2\2\u03b4\u03b7\3\2\2\2\u03b5"+
		"\u03b7\7`\2\2\u03b6\u03ae\3\2\2\2\u03b6\u03b5\3\2\2\2\u03b7\u008b\3\2"+
		"\2\2\u03b8\u0423\5\u0084C\2\u03b9\u03ba\7\4\2\2\u03ba\u03bd\5\u00a8U\2"+
		"\u03bb\u03bc\7g\2\2\u03bc\u03be\5\u00a8U\2\u03bd\u03bb\3\2\2\2\u03bd\u03be"+
		"\3\2\2\2\u03be\u03bf\3\2\2\2\u03bf\u03c0\7`\2\2\u03c0\u0423\3\2\2\2\u03c1"+
		"\u03c2\7\30\2\2\u03c2\u03c3\5\u00a4S\2\u03c3\u03c6\5\u008cG\2\u03c4\u03c5"+
		"\7\21\2\2\u03c5\u03c7\5\u008cG\2\u03c6\u03c4\3\2\2\2\u03c6\u03c7\3\2\2"+
		"\2\u03c7\u0423\3\2\2\2\u03c8\u03c9\7\27\2\2\u03c9\u03ca\7Z\2\2\u03ca\u03cb"+
		"\5\u009eP\2\u03cb\u03cc\7[\2\2\u03cc\u03cd\5\u008cG\2\u03cd\u0423\3\2"+
		"\2\2\u03ce\u03cf\7\64\2\2\u03cf\u03d0\5\u00a4S\2\u03d0\u03d1\5\u008cG"+
		"\2\u03d1\u0423\3\2\2\2\u03d2\u03d3\7\17\2\2\u03d3\u03d4\5\u008cG\2\u03d4"+
		"\u03d5\7\64\2\2\u03d5\u03d6\5\u00a4S\2\u03d6\u03d7\7`\2\2\u03d7\u0423"+
		"\3\2\2\2\u03d8\u03d9\7\61\2\2\u03d9\u03e3\5\u0084C\2\u03da\u03dc\5\u008e"+
		"H\2\u03db\u03da\3\2\2\2\u03dc\u03dd\3\2\2\2\u03dd\u03db\3\2\2\2\u03dd"+
		"\u03de\3\2\2\2\u03de\u03e0\3\2\2\2\u03df\u03e1\5\u0092J\2\u03e0\u03df"+
		"\3\2\2\2\u03e0\u03e1\3\2\2\2\u03e1\u03e4\3\2\2\2\u03e2\u03e4\5\u0092J"+
		"\2\u03e3\u03db\3\2\2\2\u03e3\u03e2\3\2\2\2\u03e4\u0423\3\2\2\2\u03e5\u03e6"+
		"\7\61\2\2\u03e6\u03e7\5\u0094K\2\u03e7\u03eb\5\u0084C\2\u03e8\u03ea\5"+
		"\u008eH\2\u03e9\u03e8\3\2\2\2\u03ea\u03ed\3\2\2\2\u03eb\u03e9\3\2\2\2"+
		"\u03eb\u03ec\3\2\2\2\u03ec\u03ef\3\2\2\2\u03ed\u03eb\3\2\2\2\u03ee\u03f0"+
		"\5\u0092J\2\u03ef\u03ee\3\2\2\2\u03ef\u03f0\3\2\2\2\u03f0\u0423\3\2\2"+
		"\2\u03f1\u03f2\7+\2\2\u03f2\u03f3\5\u00a4S\2\u03f3\u03f7\7\\\2\2\u03f4"+
		"\u03f6\5\u009aN\2\u03f5\u03f4\3\2\2\2\u03f6\u03f9\3\2\2\2\u03f7\u03f5"+
		"\3\2\2\2\u03f7\u03f8\3\2\2\2\u03f8\u03fd\3\2\2\2\u03f9\u03f7\3\2\2\2\u03fa"+
		"\u03fc\5\u009cO\2\u03fb\u03fa\3\2\2\2\u03fc\u03ff\3\2\2\2\u03fd\u03fb"+
		"\3\2\2\2\u03fd\u03fe\3\2\2\2\u03fe\u0400\3\2\2\2\u03ff\u03fd\3\2\2\2\u0400"+
		"\u0401\7]\2\2\u0401\u0423\3\2\2\2\u0402\u0403\7,\2\2\u0403\u0404\5\u00a4"+
		"S\2\u0404\u0405\5\u0084C\2\u0405\u0423\3\2\2\2\u0406\u0408\7&\2\2\u0407"+
		"\u0409\5\u00a8U\2\u0408\u0407\3\2\2\2\u0408\u0409\3\2\2\2\u0409\u040a"+
		"\3\2\2\2\u040a\u0423\7`\2\2\u040b\u040c\7.\2\2\u040c\u040d\5\u00a8U\2"+
		"\u040d\u040e\7`\2\2\u040e\u0423\3\2\2\2\u040f\u0411\7\6\2\2\u0410\u0412"+
		"\5\u00d6l\2\u0411\u0410\3\2\2\2\u0411\u0412\3\2\2\2\u0412\u0413\3\2\2"+
		"\2\u0413\u0423\7`\2\2\u0414\u0416\7\r\2\2\u0415\u0417\5\u00d6l\2\u0416"+
		"\u0415\3\2\2\2\u0416\u0417\3\2\2\2\u0417\u0418\3\2\2\2\u0418\u0423\7`"+
		"\2\2\u0419\u0423\7`\2\2\u041a\u041b\5\u00a8U\2\u041b\u041c\7`\2\2\u041c"+
		"\u0423\3\2\2\2\u041d\u041e\5\u00d6l\2\u041e\u041f\7g\2\2\u041f\u0420\5"+
		"\u008cG\2\u0420\u0423\3\2\2\2\u0421\u0423\5\u00dan\2\u0422\u03b8\3\2\2"+
		"\2\u0422\u03b9\3\2\2\2\u0422\u03c1\3\2\2\2\u0422\u03c8\3\2\2\2\u0422\u03ce"+
		"\3\2\2\2\u0422\u03d2\3\2\2\2\u0422\u03d8\3\2\2\2\u0422\u03e5\3\2\2\2\u0422"+
		"\u03f1\3\2\2\2\u0422\u0402\3\2\2\2\u0422\u0406\3\2\2\2\u0422\u040b\3\2"+
		"\2\2\u0422\u040f\3\2\2\2\u0422\u0414\3\2\2\2\u0422\u0419\3\2\2\2\u0422"+
		"\u041a\3\2\2\2\u0422\u041d\3\2\2\2\u0422\u0421\3\2\2\2\u0423\u008d\3\2"+
		"\2\2\u0424\u0425\7\t\2\2\u0425\u0429\7Z\2\2\u0426\u0428\5\22\n\2\u0427"+
		"\u0426\3\2\2\2\u0428\u042b\3\2\2\2\u0429\u0427\3\2\2\2\u0429\u042a\3\2"+
		"\2\2\u042a\u042c\3\2\2\2\u042b\u0429\3\2\2\2\u042c\u042d\5\u0090I\2\u042d"+
		"\u042e\5\u00d6l\2\u042e\u042f\7[\2\2\u042f\u0430\5\u0084C\2\u0430\u008f"+
		"\3\2\2\2\u0431\u0436\5b\62\2\u0432\u0433\7\u0083\2\2\u0433\u0435\5b\62"+
		"\2\u0434\u0432\3\2\2\2\u0435\u0438\3\2\2\2\u0436\u0434\3\2\2\2\u0436\u0437"+
		"\3\2\2\2\u0437\u0091\3\2\2\2\u0438\u0436\3\2\2\2\u0439\u043a\7\25\2\2"+
		"\u043a\u043b\5\u0084C\2\u043b\u0093\3\2\2\2\u043c\u043d\7Z\2\2\u043d\u043f"+
		"\5\u0096L\2\u043e\u0440\7`\2\2\u043f\u043e\3\2\2\2\u043f\u0440\3\2\2\2"+
		"\u0440\u0441\3\2\2\2\u0441\u0442\7[\2\2\u0442\u0095\3\2\2\2\u0443\u0448"+
		"\5\u0098M\2\u0444\u0445\7`\2\2\u0445\u0447\5\u0098M\2\u0446\u0444\3\2"+
		"\2\2\u0447\u044a\3\2\2\2\u0448\u0446\3\2\2\2\u0448\u0449\3\2\2\2\u0449"+
		"\u0097\3\2\2\2\u044a\u0448\3\2\2\2\u044b\u044d\5\22\n\2\u044c\u044b\3"+
		"\2\2\2\u044d\u0450\3\2\2\2\u044e\u044c\3\2\2\2\u044e\u044f\3\2\2\2\u044f"+
		"\u0451\3\2\2\2\u0450\u044e\3\2\2\2\u0451\u0452\5T+\2\u0452\u0453\5N(\2"+
		"\u0453\u0454\7c\2\2\u0454\u0455\5\u00a8U\2\u0455\u0099\3\2\2\2\u0456\u0458"+
		"\5\u009cO\2\u0457\u0456\3\2\2\2\u0458\u0459\3\2\2\2\u0459\u0457\3\2\2"+
		"\2\u0459\u045a\3\2\2\2\u045a\u045c\3\2\2\2\u045b\u045d\5\u0086D\2\u045c"+
		"\u045b\3\2\2\2\u045d\u045e\3\2\2\2\u045e\u045c\3\2\2\2\u045e\u045f\3\2"+
		"\2\2\u045f\u009b\3\2\2\2\u0460\u0463\7\b\2\2\u0461\u0464\5\u00a8U\2\u0462"+
		"\u0464\5\u00d6l\2\u0463\u0461\3\2\2\2\u0463\u0462\3\2\2\2\u0464\u0465"+
		"\3\2\2\2\u0465\u0466\7g\2\2\u0466\u046a\3\2\2\2\u0467\u0468\7\16\2\2\u0468"+
		"\u046a\7g\2\2\u0469\u0460\3\2\2\2\u0469\u0467\3\2\2\2\u046a\u009d\3\2"+
		"\2\2\u046b\u0478\5\u00a2R\2\u046c\u046e\5\u00a0Q\2\u046d\u046c\3\2\2\2"+
		"\u046d\u046e\3\2\2\2\u046e\u046f\3\2\2\2\u046f\u0471\7`\2\2\u0470\u0472"+
		"\5\u00a8U\2\u0471\u0470\3\2\2\2\u0471\u0472\3\2\2\2\u0472\u0473\3\2\2"+
		"\2\u0473\u0475\7`\2\2\u0474\u0476\5\u00a6T\2\u0475\u0474\3\2\2\2\u0475"+
		"\u0476\3\2\2\2\u0476\u0478\3\2\2\2\u0477\u046b\3\2\2\2\u0477\u046d\3\2"+
		"\2\2\u0478\u009f\3\2\2\2\u0479\u047c\5\u0088E\2\u047a\u047c\5\u00a6T\2"+
		"\u047b\u0479\3\2\2\2\u047b\u047a\3\2\2\2\u047c\u00a1\3\2\2\2\u047d\u047f"+
		"\5\22\n\2\u047e\u047d\3\2\2\2\u047f\u0482\3\2\2\2\u0480\u047e\3\2\2\2"+
		"\u0480\u0481\3\2\2\2\u0481\u0483\3\2\2\2\u0482\u0480\3\2\2\2\u0483\u0484"+
		"\5\u00caf\2\u0484\u0485\5N(\2\u0485\u0486\7g\2\2\u0486\u0487\5\u00a8U"+
		"\2\u0487\u00a3\3\2\2\2\u0488\u0489\7Z\2\2\u0489\u048a\5\u00a8U\2\u048a"+
		"\u048b\7[\2\2\u048b\u00a5\3\2\2\2\u048c\u0491\5\u00a8U\2\u048d\u048e\7"+
		"a\2\2\u048e\u0490\5\u00a8U\2\u048f\u048d\3\2\2\2\u0490\u0493\3\2\2\2\u0491"+
		"\u048f\3\2\2\2\u0491\u0492\3\2\2\2\u0492\u00a7\3\2\2\2\u0493\u0491\3\2"+
		"\2\2\u0494\u0495\bU\1\2\u0495\u04bb\5\u00b2Z\2\u0496\u0497\5\u00aaV\2"+
		"\u0497\u0499\7Z\2\2\u0498\u049a\5\u00a6T\2\u0499\u0498\3\2\2\2\u0499\u049a"+
		"\3\2\2\2\u049a\u049b\3\2\2\2\u049b\u049c\7[\2\2\u049c\u04bb\3\2\2\2\u049d"+
		"\u049e\7!\2\2\u049e\u04bb\5\u00b6\\\2\u049f\u04a0\7Z\2\2\u04a0\u04a1\5"+
		"\u00caf\2\u04a1\u04a2\7[\2\2\u04a2\u04a3\5\u00a8U\30\u04a3\u04bb\3\2\2"+
		"\2\u04a4\u04a5\t\5\2\2\u04a5\u04bb\5\u00a8U\26\u04a6\u04a7\t\6\2\2\u04a7"+
		"\u04bb\5\u00a8U\25\u04a8\u04bb\5\u00acW\2\u04a9\u04aa\5\u00caf\2\u04aa"+
		"\u04b0\7\u008b\2\2\u04ab\u04ad\5\u00ceh\2\u04ac\u04ab\3\2\2\2\u04ac\u04ad"+
		"\3\2\2\2\u04ad\u04ae\3\2\2\2\u04ae\u04b1\5\u00d6l\2\u04af\u04b1\7!\2\2"+
		"\u04b0\u04ac\3\2\2\2\u04b0\u04af\3\2\2\2\u04b1\u04bb\3\2\2\2\u04b2\u04b3"+
		"\5\u00b4[\2\u04b3\u04b5\7\u008b\2\2\u04b4\u04b6\5\u00ceh\2\u04b5\u04b4"+
		"\3\2\2\2\u04b5\u04b6\3\2\2\2\u04b6\u04b7\3\2\2\2\u04b7\u04b8\7!\2\2\u04b8"+
		"\u04bb\3\2\2\2\u04b9\u04bb\5\u00dco\2\u04ba\u0494\3\2\2\2\u04ba\u0496"+
		"\3\2\2\2\u04ba\u049d\3\2\2\2\u04ba\u049f\3\2\2\2\u04ba\u04a4\3\2\2\2\u04ba"+
		"\u04a6\3\2\2\2\u04ba\u04a8\3\2\2\2\u04ba\u04a9\3\2\2\2\u04ba\u04b2\3\2"+
		"\2\2\u04ba\u04b9\3\2\2\2\u04bb\u0519\3\2\2\2\u04bc\u04bd\f\24\2\2\u04bd"+
		"\u04be\t\7\2\2\u04be\u0518\5\u00a8U\25\u04bf\u04c0\f\23\2\2\u04c0\u04c1"+
		"\t\b\2\2\u04c1\u0518\5\u00a8U\24\u04c2\u04ca\f\22\2\2\u04c3\u04c4\7e\2"+
		"\2\u04c4\u04cb\7e\2\2\u04c5\u04c6\7d\2\2\u04c6\u04c7\7d\2\2\u04c7\u04cb"+
		"\7d\2\2\u04c8\u04c9\7d\2\2\u04c9\u04cb\7d\2\2\u04ca\u04c3\3\2\2\2\u04ca"+
		"\u04c5\3\2\2\2\u04ca\u04c8\3\2\2\2\u04cb\u04cc\3\2\2\2\u04cc\u0518\5\u00a8"+
		"U\23\u04cd\u04ce\f\21\2\2\u04ce\u04cf\t\t\2\2\u04cf\u0518\5\u00a8U\22"+
		"\u04d0\u04d1\f\17\2\2\u04d1\u04d2\t\n\2\2\u04d2\u0518\5\u00a8U\20\u04d3"+
		"\u04d4\f\16\2\2\u04d4\u04d5\7r\2\2\u04d5\u0518\5\u00a8U\17\u04d6\u04d7"+
		"\f\r\2\2\u04d7\u04d8\7s\2\2\u04d8\u0518\5\u00a8U\16\u04d9\u04da\f\f\2"+
		"\2\u04da\u04db\7\u0083\2\2\u04db\u0518\5\u00a8U\r\u04dc\u04dd\f\13\2\2"+
		"\u04dd\u04de\7l\2\2\u04de\u0518\5\u00a8U\f\u04df\u04e0\f\n\2\2\u04e0\u04e1"+
		"\7m\2\2\u04e1\u0518\5\u00a8U\13\u04e2\u04e3\f\t\2\2\u04e3\u04e4\7\u0084"+
		"\2\2\u04e4\u04e5\5\u00a8U\2\u04e5\u04e6\7g\2\2\u04e6\u04e7\5\u00a8U\n"+
		"\u04e7\u0518\3\2\2\2\u04e8\u04e9\f\b\2\2\u04e9\u04ea\t\13\2\2\u04ea\u0518"+
		"\5\u00a8U\b\u04eb\u04ec\f\34\2\2\u04ec\u04fe\7b\2\2\u04ed\u04ff\5\u00d6"+
		"l\2\u04ee\u04ff\7-\2\2\u04ef\u04f1\7!\2\2\u04f0\u04f2\5\u00c6d\2\u04f1"+
		"\u04f0\3\2\2\2\u04f1\u04f2\3\2\2\2\u04f2\u04f3\3\2\2\2\u04f3\u04ff\5\u00ba"+
		"^\2\u04f4\u04f5\7*\2\2\u04f5\u04ff\5\u00d0i\2\u04f6\u04ff\5\u00c0a\2\u04f7"+
		"\u04f8\5\u00aaV\2\u04f8\u04fa\7Z\2\2\u04f9\u04fb\5\u00a6T\2\u04fa\u04f9"+
		"\3\2\2\2\u04fa\u04fb\3\2\2\2\u04fb\u04fc\3\2\2\2\u04fc\u04fd\7[\2\2\u04fd"+
		"\u04ff\3\2\2\2\u04fe\u04ed\3\2\2\2\u04fe\u04ee\3\2\2\2\u04fe\u04ef\3\2"+
		"\2\2\u04fe\u04f4\3\2\2\2\u04fe\u04f6\3\2\2\2\u04fe\u04f7\3\2\2\2\u04ff"+
		"\u0518\3\2\2\2\u0500\u0501\f\33\2\2\u0501\u0502\7^\2\2\u0502\u0503\5\u00a8"+
		"U\2\u0503\u0504\7_\2\2\u0504\u0518\3\2\2\2\u0505\u0506\f\27\2\2\u0506"+
		"\u0518\t\f\2\2\u0507\u0508\f\20\2\2\u0508\u0509\7\34\2\2\u0509\u0518\5"+
		"\u00caf\2\u050a\u050b\f\6\2\2\u050b\u050d\7\u008b\2\2\u050c\u050e\5\u00ce"+
		"h\2\u050d\u050c\3\2\2\2\u050d\u050e\3\2\2\2\u050e\u050f\3\2\2\2\u050f"+
		"\u0515\5\u00d6l\2\u0510\u0512\7Z\2\2\u0511\u0513\5\u00a6T\2\u0512\u0511"+
		"\3\2\2\2\u0512\u0513\3\2\2\2\u0513\u0514\3\2\2\2\u0514\u0516\7[\2\2\u0515"+
		"\u0510\3\2\2\2\u0515\u0516\3\2\2\2\u0516\u0518\3\2\2\2\u0517\u04bc\3\2"+
		"\2\2\u0517\u04bf\3\2\2\2\u0517\u04c2\3\2\2\2\u0517\u04cd\3\2\2\2\u0517"+
		"\u04d0\3\2\2\2\u0517\u04d3\3\2\2\2\u0517\u04d6\3\2\2\2\u0517\u04d9\3\2"+
		"\2\2\u0517\u04dc\3\2\2\2\u0517\u04df\3\2\2\2\u0517\u04e2\3\2\2\2\u0517"+
		"\u04e8\3\2\2\2\u0517\u04eb\3\2\2\2\u0517\u0500\3\2\2\2\u0517\u0505\3\2"+
		"\2\2\u0517\u0507\3\2\2\2\u0517\u050a\3\2\2\2\u0518\u051b\3\2\2\2\u0519"+
		"\u0517\3\2\2\2\u0519\u051a\3\2\2\2\u051a\u00a9\3\2\2\2\u051b\u0519\3\2"+
		"\2\2\u051c\u051d\bV\1\2\u051d\u053a\7-\2\2\u051e\u053a\7*\2\2\u051f\u053a"+
		"\5\u00d6l\2\u0520\u0524\5\u00c6d\2\u0521\u0525\5\u00d2j\2\u0522\u0523"+
		"\7-\2\2\u0523\u0525\5\u00d4k\2\u0524\u0521\3\2\2\2\u0524\u0522\3\2\2\2"+
		"\u0525\u053a\3\2\2\2\u0526\u0527\7!\2\2\u0527\u053a\5\u00b6\\\2\u0528"+
		"\u053a\5\u00acW\2\u0529\u052a\5\u00caf\2\u052a\u0530\7\u008b\2\2\u052b"+
		"\u052d\5\u00ceh\2\u052c\u052b\3\2\2\2\u052c\u052d\3\2\2\2\u052d\u052e"+
		"\3\2\2\2\u052e\u0531\5\u00d6l\2\u052f\u0531\7!\2\2\u0530\u052c\3\2\2\2"+
		"\u0530\u052f\3\2\2\2\u0531\u053a\3\2\2\2\u0532\u0533\5\u00b4[\2\u0533"+
		"\u0535\7\u008b\2\2\u0534\u0536\5\u00ceh\2\u0535\u0534\3\2\2\2\u0535\u0536"+
		"\3\2\2\2\u0536\u0537\3\2\2\2\u0537\u0538\7!\2\2\u0538\u053a\3\2\2\2\u0539"+
		"\u051c\3\2\2\2\u0539\u051e\3\2\2\2\u0539\u051f\3\2\2\2\u0539\u0520\3\2"+
		"\2\2\u0539\u0526\3\2\2\2\u0539\u0528\3\2\2\2\u0539\u0529\3\2\2\2\u0539"+
		"\u0532\3\2\2\2\u053a\u054a\3\2\2\2\u053b\u053c\f\t\2\2\u053c\u053d\7^"+
		"\2\2\u053d\u053e\5\u00a8U\2\u053e\u053f\7_\2\2\u053f\u0549\3\2\2\2\u0540"+
		"\u0541\f\b\2\2\u0541\u0543\7Z\2\2\u0542\u0544\5\u00a6T\2\u0543\u0542\3"+
		"\2\2\2\u0543\u0544\3\2\2\2\u0544\u0545\3\2\2\2\u0545\u0549\7[\2\2\u0546"+
		"\u0547\f\7\2\2\u0547\u0549\t\f\2\2\u0548\u053b\3\2\2\2\u0548\u0540\3\2"+
		"\2\2\u0548\u0546\3\2\2\2\u0549\u054c\3\2\2\2\u054a\u0548\3\2\2\2\u054a"+
		"\u054b\3\2\2\2\u054b\u00ab\3\2\2\2\u054c\u054a\3\2\2\2\u054d\u054e\5\u00ae"+
		"X\2\u054e\u054f\7\u008a\2\2\u054f\u0550\5\u00b0Y\2\u0550\u00ad\3\2\2\2"+
		"\u0551\u0563\5\u00d6l\2\u0552\u0554\7Z\2\2\u0553\u0555\5\\/\2\u0554\u0553"+
		"\3\2\2\2\u0554\u0555\3\2\2\2\u0555\u0556\3\2\2\2\u0556\u0563\7[\2\2\u0557"+
		"\u0558\7Z\2\2\u0558\u055d\5\u00d6l\2\u0559\u055a\7a\2\2\u055a\u055c\5"+
		"\u00d6l\2\u055b\u0559\3\2\2\2\u055c\u055f\3\2\2\2\u055d\u055b\3\2\2\2"+
		"\u055d\u055e\3\2\2\2\u055e\u0560\3\2\2\2\u055f\u055d\3\2\2\2\u0560\u0561"+
		"\7[\2\2\u0561\u0563\3\2\2\2\u0562\u0551\3\2\2\2\u0562\u0552\3\2\2\2\u0562"+
		"\u0557\3\2\2\2\u0563\u00af\3\2\2\2\u0564\u0567\5\u00a8U\2\u0565\u0567"+
		"\5\u0084C\2\u0566\u0564\3\2\2\2\u0566\u0565\3\2\2\2\u0567\u00b1\3\2\2"+
		"\2\u0568\u0569\7Z\2\2\u0569\u056a\5\u00a8U\2\u056a\u056b\7[\2\2\u056b"+
		"\u057b\3\2\2\2\u056c\u057b\7-\2\2\u056d\u057b\7*\2\2\u056e\u057b\5d\63"+
		"\2\u056f\u057b\5\u00d6l\2\u0570\u0571\5\62\32\2\u0571\u0572\7b\2\2\u0572"+
		"\u0573\7\13\2\2\u0573\u057b\3\2\2\2\u0574\u0578\5\u00c6d\2\u0575\u0579"+
		"\5\u00d2j\2\u0576\u0577\7-\2\2\u0577\u0579\5\u00d4k\2\u0578\u0575\3\2"+
		"\2\2\u0578\u0576\3\2\2\2\u0579\u057b\3\2\2\2\u057a\u0568\3\2\2\2\u057a"+
		"\u056c\3\2\2\2\u057a\u056d\3\2\2\2\u057a\u056e\3\2\2\2\u057a\u056f\3\2"+
		"\2\2\u057a\u0570\3\2\2\2\u057a\u0574\3\2\2\2\u057b\u00b3\3\2\2\2\u057c"+
		"\u057d\5T+\2\u057d\u057e\7b\2\2\u057e\u0580\3\2\2\2\u057f\u057c\3\2\2"+
		"\2\u057f\u0580\3\2\2\2\u0580\u0584\3\2\2\2\u0581\u0583\5j\66\2\u0582\u0581"+
		"\3\2\2\2\u0583\u0586\3\2\2\2\u0584\u0582\3\2\2\2\u0584\u0585\3\2\2\2\u0585"+
		"\u0587\3\2\2\2\u0586\u0584\3\2\2\2\u0587\u0589\5\u00d6l\2\u0588\u058a"+
		"\5\u00ceh\2\u0589\u0588\3\2\2\2\u0589\u058a\3\2\2\2\u058a\u00b5\3\2\2"+
		"\2\u058b\u058c\5\u00c6d\2\u058c\u058d\5\u00b8]\2\u058d\u058e\5\u00be`"+
		"\2\u058e\u0595\3\2\2\2\u058f\u0592\5\u00b8]\2\u0590\u0593\5\u00bc_\2\u0591"+
		"\u0593\5\u00be`\2\u0592\u0590\3\2\2\2\u0592\u0591\3\2\2\2\u0593\u0595"+
		"\3\2\2\2\u0594\u058b\3\2\2\2\u0594\u058f\3\2\2\2\u0595\u00b7\3\2\2\2\u0596"+
		"\u0598\5\u00d6l\2\u0597\u0599\5\u00c2b\2\u0598\u0597\3\2\2\2\u0598\u0599"+
		"\3\2\2\2\u0599\u05a1\3\2\2\2\u059a\u059b\7b\2\2\u059b\u059d\5\u00d6l\2"+
		"\u059c\u059e\5\u00c2b\2\u059d\u059c\3\2\2\2\u059d\u059e\3\2\2\2\u059e"+
		"\u05a0\3\2\2\2\u059f\u059a\3\2\2\2\u05a0\u05a3\3\2\2\2\u05a1\u059f\3\2"+
		"\2\2\u05a1\u05a2\3\2\2\2\u05a2\u05a6\3\2\2\2\u05a3\u05a1\3\2\2\2\u05a4"+
		"\u05a6\5\u00ccg\2\u05a5\u0596\3\2\2\2\u05a5\u05a4\3\2\2\2\u05a6\u00b9"+
		"\3\2\2\2\u05a7\u05a9\5\u00d6l\2\u05a8\u05aa\5\u00c4c\2\u05a9\u05a8\3\2"+
		"\2\2\u05a9\u05aa\3\2\2\2\u05aa\u05ab\3\2\2\2\u05ab\u05ac\5\u00be`\2\u05ac"+
		"\u00bb\3\2\2\2\u05ad\u05c9\7^\2\2\u05ae\u05b3\7_\2\2\u05af\u05b0\7^\2"+
		"\2\u05b0\u05b2\7_\2\2\u05b1\u05af\3\2\2\2\u05b2\u05b5\3\2\2\2\u05b3\u05b1"+
		"\3\2\2\2\u05b3\u05b4\3\2\2\2\u05b4\u05b6\3\2\2\2\u05b5\u05b3\3\2\2\2\u05b6"+
		"\u05ca\5R*\2\u05b7\u05b8\5\u00a8U\2\u05b8\u05bf\7_\2\2\u05b9\u05ba\7^"+
		"\2\2\u05ba\u05bb\5\u00a8U\2\u05bb\u05bc\7_\2\2\u05bc\u05be\3\2\2\2\u05bd"+
		"\u05b9\3\2\2\2\u05be\u05c1\3\2\2\2\u05bf\u05bd\3\2\2\2\u05bf\u05c0\3\2"+
		"\2\2\u05c0\u05c6\3\2\2\2\u05c1\u05bf\3\2\2\2\u05c2\u05c3\7^\2\2\u05c3"+
		"\u05c5\7_\2\2\u05c4\u05c2\3\2\2\2\u05c5\u05c8\3\2\2\2\u05c6\u05c4\3\2"+
		"\2\2\u05c6\u05c7\3\2\2\2\u05c7\u05ca\3\2\2\2\u05c8\u05c6\3\2\2\2\u05c9"+
		"\u05ae\3\2\2\2\u05c9\u05b7\3\2\2\2\u05ca\u00bd\3\2\2\2\u05cb\u05cd\5\u00d4"+
		"k\2\u05cc\u05ce\5&\24\2\u05cd\u05cc\3\2\2\2\u05cd\u05ce\3\2\2\2\u05ce"+
		"\u00bf\3\2\2\2\u05cf\u05d0\5\u00c6d\2\u05d0\u05d1\5\u00d2j\2\u05d1\u00c1"+
		"\3\2\2\2\u05d2\u05d3\7e\2\2\u05d3\u05d6\7d\2\2\u05d4\u05d6\5\u00ceh\2"+
		"\u05d5\u05d2\3\2\2\2\u05d5\u05d4\3\2\2\2\u05d6\u00c3\3\2\2\2\u05d7\u05d8"+
		"\7e\2\2\u05d8\u05db\7d\2\2\u05d9\u05db\5\u00c6d\2\u05da\u05d7\3\2\2\2"+
		"\u05da\u05d9\3\2\2\2\u05db\u00c5\3\2\2\2\u05dc\u05dd\7e\2\2\u05dd\u05de"+
		"\5\u00c8e\2\u05de\u05df\7d\2\2\u05df\u00c7\3\2\2\2\u05e0\u05e5\5\u00ca"+
		"f\2\u05e1\u05e2\7a\2\2\u05e2\u05e4\5\u00caf\2\u05e3\u05e1\3\2\2\2\u05e4"+
		"\u05e7\3\2\2\2\u05e5\u05e3\3\2\2\2\u05e5\u05e6\3\2\2\2\u05e6\u00c9\3\2"+
		"\2\2\u05e7\u05e5\3\2\2\2\u05e8\u05ea\5j\66\2\u05e9\u05e8\3\2\2\2\u05e9"+
		"\u05ea\3\2\2\2\u05ea\u05ed\3\2\2\2\u05eb\u05ee\5T+\2\u05ec\u05ee\5\u00cc"+
		"g\2\u05ed\u05eb\3\2\2\2\u05ed\u05ec\3\2\2\2\u05ee\u05f3\3\2\2\2\u05ef"+
		"\u05f0\7^\2\2\u05f0\u05f2\7_\2\2\u05f1\u05ef\3\2\2\2\u05f2\u05f5\3\2\2"+
		"\2\u05f3\u05f1\3\2\2\2\u05f3\u05f4\3\2\2\2\u05f4\u00cb\3\2\2\2\u05f5\u05f3"+
		"\3\2\2\2\u05f6\u05f7\t\r\2\2\u05f7\u00cd\3\2\2\2\u05f8\u05f9\7e\2\2\u05f9"+
		"\u05fe\5V,\2\u05fa\u05fb\7a\2\2\u05fb\u05fd\5V,\2\u05fc\u05fa\3\2\2\2"+
		"\u05fd\u0600\3\2\2\2\u05fe\u05fc\3\2\2\2\u05fe\u05ff\3\2\2\2\u05ff\u0601"+
		"\3\2\2\2\u0600\u05fe\3\2\2\2\u0601\u0602\7d\2\2\u0602\u00cf\3\2\2\2\u0603"+
		"\u060a\5\u00d4k\2\u0604\u0605\7b\2\2\u0605\u0607\5\u00d6l\2\u0606\u0608"+
		"\5\u00d4k\2\u0607\u0606\3\2\2\2\u0607\u0608\3\2\2\2\u0608\u060a\3\2\2"+
		"\2\u0609\u0603\3\2\2\2\u0609\u0604\3\2\2\2\u060a\u00d1\3\2\2\2\u060b\u060c"+
		"\7*\2\2\u060c\u0611\5\u00d0i\2\u060d\u060e\5\u00d6l\2\u060e\u060f\5\u00d4"+
		"k\2\u060f\u0611\3\2\2\2\u0610\u060b\3\2\2\2\u0610\u060d\3\2\2\2\u0611"+
		"\u00d3\3\2\2\2\u0612\u0614\7Z\2\2\u0613\u0615\5\u00a6T\2\u0614\u0613\3"+
		"\2\2\2\u0614\u0615\3\2\2\2\u0615\u0616\3\2\2\2\u0616\u0617\7[\2\2\u0617"+
		"\u00d5\3\2\2\2\u0618\u0619\t\16\2\2\u0619\u00d7\3\2\2\2\u061a\u0623\5"+
		"\u00e2r\2\u061b\u0623\5\u00e4s\2\u061c\u0623\5\u00e6t\2\u061d\u0623\5"+
		"\u00e8u\2\u061e\u0623\5\u0112\u008a\2\u061f\u0623\5\u0114\u008b\2\u0620"+
		"\u0623\5\u0116\u008c\2\u0621\u0623\5\u0118\u008d\2\u0622\u061a\3\2\2\2"+
		"\u0622\u061b\3\2\2\2\u0622\u061c\3\2\2\2\u0622\u061d\3\2\2\2\u0622\u061e"+
		"\3\2\2\2\u0622\u061f\3\2\2\2\u0622\u0620\3\2\2\2\u0622\u0621\3\2\2\2\u0623"+
		"\u00d9\3\2\2\2\u0624\u0625\5\u00e0q\2\u0625\u00db\3\2\2\2\u0626\u0629"+
		"\5\u0102\u0082\2\u0627\u0629\5\u00dep\2\u0628\u0626\3\2\2\2\u0628\u0627"+
		"\3\2\2\2\u0629\u00dd\3\2\2\2\u062a\u062b\7\u0089\2\2\u062b\u00df\3\2\2"+
		"\2\u062c\u0639\7\65\2\2\u062d\u0636\7Z\2\2\u062e\u0633\5\u00fe\u0080\2"+
		"\u062f\u0630\7a\2\2\u0630\u0632\5\u00fe\u0080\2\u0631\u062f\3\2\2\2\u0632"+
		"\u0635\3\2\2\2\u0633\u0631\3\2\2\2\u0633\u0634\3\2\2\2\u0634\u0637\3\2"+
		"\2\2\u0635\u0633\3\2\2\2\u0636\u062e\3\2\2\2\u0636\u0637\3\2\2\2\u0637"+
		"\u0638\3\2\2\2\u0638\u063a\7[\2\2\u0639\u062d\3\2\2\2\u0639\u063a\3\2"+
		"\2\2\u063a\u063b\3\2\2\2\u063b\u063f\7\\\2\2\u063c\u063e\5\u00f0y\2\u063d"+
		"\u063c\3\2\2\2\u063e\u0641\3\2\2\2\u063f\u063d\3\2\2\2\u063f\u0640\3\2"+
		"\2\2\u0640\u0642\3\2\2\2\u0641\u063f\3\2\2\2\u0642\u0643\7]\2\2\u0643"+
		"\u00e1\3\2\2\2\u0644\u0645\7\66\2\2\u0645\u0646\5\u013c\u009f\2\u0646"+
		"\u0648\7Z\2\2\u0647\u0649\5\u00f4{\2\u0648\u0647\3\2\2\2\u0648\u0649\3"+
		"\2\2\2\u0649\u064a\3\2\2\2\u064a\u064b\7[\2\2\u064b\u064c\7\23\2\2\u064c"+
		"\u064d\5\u00fe\u0080\2\u064d\u0651\7\\\2\2\u064e\u0650\5\u00eex\2\u064f"+
		"\u064e\3\2\2\2\u0650\u0653\3\2\2\2\u0651\u064f\3\2\2\2\u0651\u0652\3\2"+
		"\2\2\u0652\u0654\3\2\2\2\u0653\u0651\3\2\2\2\u0654\u0655\7]\2\2\u0655"+
		"\u00e3\3\2\2\2\u0656\u0657\7\67\2\2\u0657\u0661\7\\\2\2\u0658\u065a\7"+
		"b\2\2\u0659\u0658\3\2\2\2\u065a\u065d\3\2\2\2\u065b\u0659\3\2\2\2\u065b"+
		"\u065c\3\2\2\2\u065c\u065e\3\2\2\2\u065d\u065b\3\2\2\2\u065e\u0660\7\u0086"+
		"\2\2\u065f\u065b\3\2\2\2\u0660\u0663\3\2\2\2\u0661\u065f\3\2\2\2\u0661"+
		"\u0662\3\2\2\2\u0662\u0664\3\2\2\2\u0663\u0661\3\2\2\2\u0664\u0669\5\u013c"+
		"\u009f\2\u0665\u0666\t\17\2\2\u0666\u0668\5\u013c\u009f\2\u0667\u0665"+
		"\3\2\2\2\u0668\u066b\3\2\2\2\u0669\u0667\3\2\2\2\u0669\u066a\3\2\2\2\u066a"+
		"\u066c\3\2\2\2\u066b\u0669\3\2\2\2\u066c\u066d\7]\2\2\u066d\u00e5\3\2"+
		"\2\2\u066e\u0670\78\2\2\u066f\u0671\5\u00ecw\2\u0670\u066f\3\2\2\2\u0670"+
		"\u0671\3\2\2\2\u0671\u0672\3\2\2\2\u0672\u0673\5\u00eav\2\u0673\u00e7"+
		"\3\2\2\2\u0674\u0675\7=\2\2\u0675\u0676\5\u00eav\2\u0676\u00e9\3\2\2\2"+
		"\u0677\u067c\7\u0092\2\2\u0678\u067b\5\u00eav\2\u0679\u067b\7\u009e\2"+
		"\2\u067a\u0678\3\2\2\2\u067a\u0679\3\2\2\2\u067b\u067e\3\2\2\2\u067c\u067d"+
		"\3\2\2\2\u067c\u067a\3\2\2\2\u067d\u067f\3\2\2\2\u067e\u067c\3\2\2\2\u067f"+
		"\u068a\7\u009a\2\2\u0680\u0685\t\20\2\2\u0681\u0684\5\u00eav\2\u0682\u0684"+
		"\7\u00a4\2\2\u0683\u0681\3\2\2\2\u0683\u0682\3\2\2\2\u0684\u0687\3\2\2"+
		"\2\u0685\u0686\3\2\2\2\u0685\u0683\3\2\2\2\u0686\u0688\3\2\2\2\u0687\u0685"+
		"\3\2\2\2\u0688\u068a\7\u00a0\2\2\u0689\u0677\3\2\2\2\u0689\u0680\3\2\2"+
		"\2\u068a\u00eb\3\2\2\2\u068b\u068c\7\u0093\2\2\u068c\u0691\7\u0095\2\2"+
		"\u068d\u068e\7a\2\2\u068e\u0690\7\u0095\2\2\u068f\u068d\3\2\2\2\u0690"+
		"\u0693\3\2\2\2\u0691\u068f\3\2\2\2\u0691\u0692\3\2\2\2\u0692\u0694\3\2"+
		"\2\2\u0693\u0691\3\2\2\2\u0694\u0695\7\u0094\2\2\u0695\u00ed\3\2\2\2\u0696"+
		"\u0697\7>\2\2\u0697\u0698\5\u013c\u009f\2\u0698\u069c\7\\\2\2\u0699\u069b"+
		"\5\u00f0y\2\u069a\u0699\3\2\2\2\u069b\u069e\3\2\2\2\u069c\u069a\3\2\2"+
		"\2\u069c\u069d\3\2\2\2\u069d\u069f\3\2\2\2\u069e\u069c\3\2\2\2\u069f\u06a0"+
		"\7]\2\2\u06a0\u00ef\3\2\2\2\u06a1\u06a4\5\u00f8}\2\u06a2\u06a3\t\21\2"+
		"\2\u06a3\u06a5\5\u00fa~\2\u06a4\u06a2\3\2\2\2\u06a4\u06a5\3\2\2\2\u06a5"+
		"\u06a6\3\2\2\2\u06a6\u06a7\7\u008a\2\2\u06a7\u06a8\5\u00f2z\2\u06a8\u06ba"+
		"\3\2\2\2\u06a9\u06ac\5\u00f8}\2\u06aa\u06ab\t\21\2\2\u06ab\u06ad\5\u00fa"+
		"~\2\u06ac\u06aa\3\2\2\2\u06ac\u06ad\3\2\2\2\u06ad\u06ae\3\2\2\2\u06ae"+
		"\u06af\7\u008a\2\2\u06af\u06b0\5\u00fe\u0080\2\u06b0\u06ba\3\2\2\2\u06b1"+
		"\u06b2\5\u00fa~\2\u06b2\u06b3\7\u008a\2\2\u06b3\u06b4\5\u00f2z\2\u06b4"+
		"\u06ba\3\2\2\2\u06b5\u06b6\5\u00fa~\2\u06b6\u06b7\7\u008a\2\2\u06b7\u06b8"+
		"\5\u00fe\u0080\2\u06b8\u06ba\3\2\2\2\u06b9\u06a1\3\2\2\2\u06b9\u06a9\3"+
		"\2\2\2\u06b9\u06b1\3\2\2\2\u06b9\u06b5\3\2\2\2\u06ba\u00f1\3\2\2\2\u06bb"+
		"\u06c0\7\\\2\2\u06bc\u06bf\5\u00f2z\2\u06bd\u06bf\5\u0086D\2\u06be\u06bc"+
		"\3\2\2\2\u06be\u06bd\3\2\2\2\u06bf\u06c2\3\2\2\2\u06c0\u06c1\3\2\2\2\u06c0"+
		"\u06be\3\2\2\2\u06c1\u06c3\3\2\2\2\u06c2\u06c0\3\2\2\2\u06c3\u06c4\7]"+
		"\2\2\u06c4\u00f3\3\2\2\2\u06c5\u06ca\5\u00f6|\2\u06c6\u06c7\7a\2\2\u06c7"+
		"\u06c9\5\u00f6|\2\u06c8\u06c6\3\2\2\2\u06c9\u06cc\3\2\2\2\u06ca\u06c8"+
		"\3\2\2\2\u06ca\u06cb\3\2\2\2\u06cb\u00f5\3\2\2\2\u06cc\u06ca\3\2\2\2\u06cd"+
		"\u06cf\5\u013c\u009f\2\u06ce\u06d0\7g\2\2\u06cf\u06ce\3\2\2\2\u06cf\u06d0"+
		"\3\2\2\2\u06d0\u06d1\3\2\2\2\u06d1\u06d2\5\u013c\u009f\2\u06d2\u00f7\3"+
		"\2\2\2\u06d3\u06d8\5\u0106\u0084\2\u06d4\u06d5\7a\2\2\u06d5\u06d7\5\u0106"+
		"\u0084\2\u06d6\u06d4\3\2\2\2\u06d7\u06da\3\2\2\2\u06d8\u06d6\3\2\2\2\u06d8"+
		"\u06d9\3\2\2\2\u06d9\u06e4\3\2\2\2\u06da\u06d8\3\2\2\2\u06db\u06dc\7N"+
		"\2\2\u06dc\u06e1\5\u00fc\177\2\u06dd\u06de\7a\2\2\u06de\u06e0\5\u00fc"+
		"\177\2\u06df\u06dd\3\2\2\2\u06e0\u06e3\3\2\2\2\u06e1\u06df\3\2\2\2\u06e1"+
		"\u06e2\3\2\2\2\u06e2\u06e5\3\2\2\2\u06e3\u06e1\3\2\2\2\u06e4\u06db\3\2"+
		"\2\2\u06e4\u06e5\3\2\2\2\u06e5\u06ff\3\2\2\2\u06e6\u06e7\7Z\2\2\u06e7"+
		"\u06ec\5\u0106\u0084\2\u06e8\u06e9\7a\2\2\u06e9\u06eb\5\u0106\u0084\2"+
		"\u06ea\u06e8\3\2\2\2\u06eb\u06ee\3\2\2\2\u06ec\u06ea\3\2\2\2\u06ec\u06ed"+
		"\3\2\2\2\u06ed\u06ef\3\2\2\2\u06ee\u06ec\3\2\2\2\u06ef\u06fc\7[\2\2\u06f0"+
		"\u06f1\7N\2\2\u06f1\u06f2\7Z\2\2\u06f2\u06f7\5\u00fc\177\2\u06f3\u06f4"+
		"\7a\2\2\u06f4\u06f6\5\u00fc\177\2\u06f5\u06f3\3\2\2\2\u06f6\u06f9\3\2"+
		"\2\2\u06f7\u06f5\3\2\2\2\u06f7\u06f8\3\2\2\2\u06f8\u06fa\3\2\2\2\u06f9"+
		"\u06f7\3\2\2\2\u06fa\u06fb\7[\2\2\u06fb\u06fd\3\2\2\2\u06fc\u06f0\3\2"+
		"\2\2\u06fc\u06fd\3\2\2\2\u06fd\u06ff\3\2\2\2\u06fe\u06d3\3\2\2\2\u06fe"+
		"\u06e6\3\2\2\2\u06ff\u00f9\3\2\2\2\u0700\u0701\b~\1\2\u0701\u0702\5\u0106"+
		"\u0084\2\u0702\u0703\7e\2\2\u0703\u0704\7e\2\2\u0704\u0705\5\u00fe\u0080"+
		"\2\u0705\u0723\3\2\2\2\u0706\u0707\5\u00fc\177\2\u0707\u0708\7d\2\2\u0708"+
		"\u0709\5\u00fc\177\2\u0709\u0723\3\2\2\2\u070a\u070b\5\u00fc\177\2\u070b"+
		"\u070c\7j\2\2\u070c\u070d\5\u00fc\177\2\u070d\u0723\3\2\2\2\u070e\u070f"+
		"\5\u00fc\177\2\u070f\u0710\7e\2\2\u0710\u0711\5\u00fc\177\2\u0711\u0723"+
		"\3\2\2\2\u0712\u0713\5\u00fc\177\2\u0713\u0714\7i\2\2\u0714\u0715\5\u00fc"+
		"\177\2\u0715\u0723\3\2\2\2\u0716\u0717\5\u00fc\177\2\u0717\u0718\7h\2"+
		"\2\u0718\u0719\5\u00fc\177\2\u0719\u0723\3\2\2\2\u071a\u071b\5\u00fc\177"+
		"\2\u071b\u071c\7k\2\2\u071c\u071d\5\u00fc\177\2\u071d\u0723\3\2\2\2\u071e"+
		"\u071f\7Z\2\2\u071f\u0720\5\u00fa~\2\u0720\u0721\7[\2\2\u0721\u0723\3"+
		"\2\2\2\u0722\u0700\3\2\2\2\u0722\u0706\3\2\2\2\u0722\u070a\3\2\2\2\u0722"+
		"\u070e\3\2\2\2\u0722\u0712\3\2\2\2\u0722\u0716\3\2\2\2\u0722\u071a\3\2"+
		"\2\2\u0722\u071e\3\2\2\2\u0723\u072c\3\2\2\2\u0724\u0725\f\f\2\2\u0725"+
		"\u0726\7l\2\2\u0726\u072b\5\u00fa~\r\u0727\u0728\f\13\2\2\u0728\u0729"+
		"\7m\2\2\u0729\u072b\5\u00fa~\f\u072a\u0724\3\2\2\2\u072a\u0727\3\2\2\2"+
		"\u072b\u072e\3\2\2\2\u072c\u072a\3\2\2\2\u072c\u072d\3\2\2\2\u072d\u00fb"+
		"\3\2\2\2\u072e\u072c\3\2\2\2\u072f\u0731\5\u013c\u009f\2\u0730\u0732\7"+
		"\u0080\2\2\u0731\u0730\3\2\2\2\u0731\u0732\3\2\2\2\u0732\u0743\3\2\2\2"+
		"\u0733\u0734\5\u013c\u009f\2\u0734\u073d\7Z\2\2\u0735\u073a\5\u00fc\177"+
		"\2\u0736\u0737\7a\2\2\u0737\u0739\5\u00fc\177\2\u0738\u0736\3\2\2\2\u0739"+
		"\u073c\3\2\2\2\u073a\u0738\3\2\2\2\u073a\u073b\3\2\2\2\u073b\u073e\3\2"+
		"\2\2\u073c\u073a\3\2\2\2\u073d\u0735\3\2\2\2\u073d\u073e\3\2\2\2\u073e"+
		"\u073f\3\2\2\2\u073f\u0740\7[\2\2\u0740\u0743\3\2\2\2\u0741\u0743\5\u010c"+
		"\u0087\2\u0742\u072f\3\2\2\2\u0742\u0733\3\2\2\2\u0742\u0741\3\2\2\2\u0743"+
		"\u00fd\3\2\2\2\u0744\u0746\5\u013c\u009f\2\u0745\u0744\3\2\2\2\u0745\u0746"+
		"\3\2\2\2\u0746\u0748\3\2\2\2\u0747\u0749\7\u0088\2\2\u0748\u0747\3\2\2"+
		"\2\u0748\u0749\3\2\2\2\u0749\u074a\3\2\2\2\u074a\u074b\5\u013c\u009f\2"+
		"\u074b\u0754\7Z\2\2\u074c\u0751\5\u00fe\u0080\2\u074d\u074e\7a\2\2\u074e"+
		"\u0750\5\u00fe\u0080\2\u074f\u074d\3\2\2\2\u0750\u0753\3\2\2\2\u0751\u074f"+
		"\3\2\2\2\u0751\u0752\3\2\2\2\u0752\u0755\3\2\2\2\u0753\u0751\3\2\2\2\u0754"+
		"\u074c\3\2\2\2\u0754\u0755\3\2\2\2\u0755\u0756\3\2\2\2\u0756\u0757\7["+
		"\2\2\u0757\u077c\3\2\2\2\u0758\u075a\5\u013c\u009f\2\u0759\u0758\3\2\2"+
		"\2\u0759\u075a\3\2\2\2\u075a\u075c\3\2\2\2\u075b\u075d\7\u0088\2\2\u075c"+
		"\u075b\3\2\2\2\u075c\u075d\3\2\2\2\u075d\u075e\3\2\2\2\u075e\u075f\5\u013c"+
		"\u009f\2\u075f\u0768\7^\2\2\u0760\u0765\5\u0100\u0081\2\u0761\u0762\7"+
		"a\2\2\u0762\u0764\5\u0100\u0081\2\u0763\u0761\3\2\2\2\u0764\u0767\3\2"+
		"\2\2\u0765\u0763\3\2\2\2\u0765\u0766\3\2\2\2\u0766\u0769\3\2\2\2\u0767"+
		"\u0765\3\2\2\2\u0768\u0760\3\2\2\2\u0768\u0769\3\2\2\2\u0769\u076a\3\2"+
		"\2\2\u076a\u076b\7_\2\2\u076b\u077c\3\2\2\2\u076c\u076e\5\u013c\u009f"+
		"\2\u076d\u076c\3\2\2\2\u076d\u076e\3\2\2\2\u076e\u0770\3\2\2\2\u076f\u0771"+
		"\7\u0088\2\2\u0770\u076f\3\2\2\2\u0770\u0771\3\2\2\2\u0771\u0772\3\2\2"+
		"\2\u0772\u0774\5\u013c\u009f\2\u0773\u0775\7\u0080\2\2\u0774\u0773\3\2"+
		"\2\2\u0774\u0775\3\2\2\2\u0775\u077c\3\2\2\2\u0776\u0778\5\u013c\u009f"+
		"\2\u0777\u0776\3\2\2\2\u0777\u0778\3\2\2\2\u0778\u0779\3\2\2\2\u0779\u077c"+
		"\5\u010c\u0087\2\u077a\u077c\7\u0081\2\2\u077b\u0745\3\2\2\2\u077b\u0759"+
		"\3\2\2\2\u077b\u076d\3\2\2\2\u077b\u0777\3\2\2\2\u077b\u077a\3\2\2\2\u077c"+
		"\u00ff\3\2\2\2\u077d\u077e\5\u013c\u009f\2\u077e\u077f\7c\2\2\u077f\u0780"+
		"\5\u00fe\u0080\2\u0780\u0101\3\2\2\2\u0781\u0782\7\u0088\2\2\u0782\u0783"+
		"\5\u013c\u009f\2\u0783\u078c\7^\2\2\u0784\u0789\5\u0100\u0081\2\u0785"+
		"\u0786\7a\2\2\u0786\u0788\5\u0100\u0081\2\u0787\u0785\3\2\2\2\u0788\u078b"+
		"\3\2\2\2\u0789\u0787\3\2\2\2\u0789\u078a\3\2\2\2\u078a\u078d\3\2\2\2\u078b"+
		"\u0789\3\2\2\2\u078c\u0784\3\2\2\2\u078c\u078d\3\2\2\2\u078d\u078e\3\2"+
		"\2\2\u078e\u078f\7_\2\2\u078f\u07aa\3\2\2\2\u0790\u0791\7\u0088\2\2\u0791"+
		"\u0792\5\u013c\u009f\2\u0792\u079b\7Z\2\2\u0793\u0798\5\u0104\u0083\2"+
		"\u0794\u0795\7a\2\2\u0795\u0797\5\u0104\u0083\2\u0796\u0794\3\2\2\2\u0797"+
		"\u079a\3\2\2\2\u0798\u0796\3\2\2\2\u0798\u0799\3\2\2\2\u0799\u079c\3\2"+
		"\2\2\u079a\u0798\3\2\2\2\u079b\u0793\3\2\2\2\u079b\u079c\3\2\2\2\u079c"+
		"\u079d\3\2\2\2\u079d\u079e\7[\2\2\u079e\u07aa\3\2\2\2\u079f\u07a0\7\u0088"+
		"\2\2\u07a0\u07a1\7Z\2\2\u07a1\u07a2\5\u0104\u0083\2\u07a2\u07a3\7[\2\2"+
		"\u07a3\u07aa\3\2\2\2\u07a4\u07a5\7\u0088\2\2\u07a5\u07a7\5\u013c\u009f"+
		"\2\u07a6\u07a8\7\u0080\2\2\u07a7\u07a6\3\2\2\2\u07a7\u07a8\3\2\2\2\u07a8"+
		"\u07aa\3\2\2\2\u07a9\u0781\3\2\2\2\u07a9\u0790\3\2\2\2\u07a9\u079f\3\2"+
		"\2\2\u07a9\u07a4\3\2\2\2\u07aa\u0103\3\2\2\2\u07ab\u07ac\b\u0083\1\2\u07ac"+
		"\u07ad\5\u013c\u009f\2\u07ad\u07b6\7Z\2\2\u07ae\u07b3\5\u0104\u0083\2"+
		"\u07af\u07b0\7a\2\2\u07b0\u07b2\5\u0104\u0083\2\u07b1\u07af\3\2\2\2\u07b2"+
		"\u07b5\3\2\2\2\u07b3\u07b1\3\2\2\2\u07b3\u07b4\3\2\2\2\u07b4\u07b7\3\2"+
		"\2\2\u07b5\u07b3\3\2\2\2\u07b6\u07ae\3\2\2\2\u07b6\u07b7\3\2\2\2\u07b7"+
		"\u07b8\3\2\2\2\u07b8\u07b9\7[\2\2\u07b9\u07e2\3\2\2\2\u07ba\u07bb\7Z\2"+
		"\2\u07bb\u07bc\5\u0104\u0083\2\u07bc\u07bd\7[\2\2\u07bd\u07e2\3\2\2\2"+
		"\u07be\u07c0\5\u013c\u009f\2\u07bf\u07c1\7\u0080\2\2\u07c0\u07bf\3\2\2"+
		"\2\u07c0\u07c1\3\2\2\2\u07c1\u07e2\3\2\2\2\u07c2\u07e2\5\u010c\u0087\2"+
		"\u07c3\u07e2\7\u0081\2\2\u07c4\u07e2\5\u00d6l\2\u07c5\u07c6\7!\2\2\u07c6"+
		"\u07e2\5\u00b6\\\2\u07c7\u07c8\7Z\2\2\u07c8\u07c9\5\u00caf\2\u07c9\u07ca"+
		"\7[\2\2\u07ca\u07cb\5\u0104\u0083\27\u07cb\u07e2\3\2\2\2\u07cc\u07cd\t"+
		"\5\2\2\u07cd\u07e2\5\u0104\u0083\25\u07ce\u07cf\t\6\2\2\u07cf\u07e2\5"+
		"\u0104\u0083\24\u07d0\u07e2\5\u00acW\2\u07d1\u07d2\5\u00caf\2\u07d2\u07d8"+
		"\7\u008b\2\2\u07d3\u07d5\5\u00ceh\2\u07d4\u07d3\3\2\2\2\u07d4\u07d5\3"+
		"\2\2\2\u07d5\u07d6\3\2\2\2\u07d6\u07d9\5\u00d6l\2\u07d7\u07d9\7!\2\2\u07d8"+
		"\u07d4\3\2\2\2\u07d8\u07d7\3\2\2\2\u07d9\u07e2\3\2\2\2\u07da\u07db\5\u00b4"+
		"[\2\u07db\u07dd\7\u008b\2\2\u07dc\u07de\5\u00ceh\2\u07dd\u07dc\3\2\2\2"+
		"\u07dd\u07de\3\2\2\2\u07de\u07df\3\2\2\2\u07df\u07e0\7!\2\2\u07e0\u07e2"+
		"\3\2\2\2\u07e1\u07ab\3\2\2\2\u07e1\u07ba\3\2\2\2\u07e1\u07be\3\2\2\2\u07e1"+
		"\u07c2\3\2\2\2\u07e1\u07c3\3\2\2\2\u07e1\u07c4\3\2\2\2\u07e1\u07c5\3\2"+
		"\2\2\u07e1\u07c7\3\2\2\2\u07e1\u07cc\3\2\2\2\u07e1\u07ce\3\2\2\2\u07e1"+
		"\u07d0\3\2\2\2\u07e1\u07d1\3\2\2\2\u07e1\u07da\3\2\2\2\u07e2\u083f\3\2"+
		"\2\2\u07e3\u07e4\f\23\2\2\u07e4\u07e5\t\7\2\2\u07e5\u083e\5\u0104\u0083"+
		"\24\u07e6\u07e7\f\22\2\2\u07e7\u07e8\t\b\2\2\u07e8\u083e\5\u0104\u0083"+
		"\23\u07e9\u07f1\f\21\2\2\u07ea\u07eb\7e\2\2\u07eb\u07f2\7e\2\2\u07ec\u07ed"+
		"\7d\2\2\u07ed\u07ee\7d\2\2\u07ee\u07f2\7d\2\2\u07ef\u07f0\7d\2\2\u07f0"+
		"\u07f2\7d\2\2\u07f1\u07ea\3\2\2\2\u07f1\u07ec\3\2\2\2\u07f1\u07ef\3\2"+
		"\2\2\u07f2\u07f3\3\2\2\2\u07f3\u083e\5\u0104\u0083\22\u07f4\u07f5\f\20"+
		"\2\2\u07f5\u07f6\t\t\2\2\u07f6\u083e\5\u0104\u0083\21\u07f7\u07f8\f\16"+
		"\2\2\u07f8\u07f9\t\n\2\2\u07f9\u083e\5\u0104\u0083\17\u07fa\u07fb\f\r"+
		"\2\2\u07fb\u07fc\7r\2\2\u07fc\u083e\5\u0104\u0083\16\u07fd\u07fe\f\f\2"+
		"\2\u07fe\u07ff\7s\2\2\u07ff\u083e\5\u0104\u0083\r\u0800\u0801\f\13\2\2"+
		"\u0801\u0802\7\u0083\2\2\u0802\u083e\5\u0104\u0083\f\u0803\u0804\f\n\2"+
		"\2\u0804\u0805\7l\2\2\u0805\u083e\5\u0104\u0083\13\u0806\u0807\f\t\2\2"+
		"\u0807\u0808\7m\2\2\u0808\u083e\5\u0104\u0083\n\u0809\u080a\f\b\2\2\u080a"+
		"\u080b\7\u0084\2\2\u080b\u080c\5\u0104\u0083\2\u080c\u080d\7g\2\2\u080d"+
		"\u080e\5\u0104\u0083\t\u080e\u083e\3\2\2\2\u080f\u0810\f\7\2\2\u0810\u0811"+
		"\t\13\2\2\u0811\u083e\5\u0104\u0083\7\u0812\u0813\f\33\2\2\u0813\u081c"+
		"\7Z\2\2\u0814\u0819\5\u0104\u0083\2\u0815\u0816\7a\2\2\u0816\u0818\5\u0104"+
		"\u0083\2\u0817\u0815\3\2\2\2\u0818\u081b\3\2\2\2\u0819\u0817\3\2\2\2\u0819"+
		"\u081a\3\2\2\2\u081a\u081d\3\2\2\2\u081b\u0819\3\2\2\2\u081c\u0814\3\2"+
		"\2\2\u081c\u081d\3\2\2\2\u081d\u081e\3\2\2\2\u081e\u083e\7[\2\2\u081f"+
		"\u0820\f\32\2\2\u0820\u082b\7b\2\2\u0821\u082c\5\u00d6l\2\u0822\u082c"+
		"\7-\2\2\u0823\u0825\7!\2\2\u0824\u0826\5\u00c6d\2\u0825\u0824\3\2\2\2"+
		"\u0825\u0826\3\2\2\2\u0826\u0827\3\2\2\2\u0827\u082c\5\u00ba^\2\u0828"+
		"\u0829\7*\2\2\u0829\u082c\5\u00d0i\2\u082a\u082c\5\u00c0a\2\u082b\u0821"+
		"\3\2\2\2\u082b\u0822\3\2\2\2\u082b\u0823\3\2\2\2\u082b\u0828\3\2\2\2\u082b"+
		"\u082a\3\2\2\2\u082c\u083e\3\2\2\2\u082d\u082e\f\31\2\2\u082e\u082f\7"+
		"^\2\2\u082f\u0830\5\u0104\u0083\2\u0830\u0831\7_\2\2\u0831\u083e\3\2\2"+
		"\2\u0832\u0833\f\26\2\2\u0833\u083e\t\f\2\2\u0834\u0835\f\17\2\2\u0835"+
		"\u0836\7\34\2\2\u0836\u083e\5\u00caf\2\u0837\u0838\f\5\2\2\u0838\u083a"+
		"\7\u008b\2\2\u0839\u083b\5\u00ceh\2\u083a\u0839\3\2\2\2\u083a\u083b\3"+
		"\2\2\2\u083b\u083c\3\2\2\2\u083c\u083e\5\u00d6l\2\u083d\u07e3\3\2\2\2"+
		"\u083d\u07e6\3\2\2\2\u083d\u07e9\3\2\2\2\u083d\u07f4\3\2\2\2\u083d\u07f7"+
		"\3\2\2\2\u083d\u07fa\3\2\2\2\u083d\u07fd\3\2\2\2\u083d\u0800\3\2\2\2\u083d"+
		"\u0803\3\2\2\2\u083d\u0806\3\2\2\2\u083d\u0809\3\2\2\2\u083d\u080f\3\2"+
		"\2\2\u083d\u0812\3\2\2\2\u083d\u081f\3\2\2\2\u083d\u082d\3\2\2\2\u083d"+
		"\u0832\3\2\2\2\u083d\u0834\3\2\2\2\u083d\u0837\3\2\2\2\u083e\u0841\3\2"+
		"\2\2\u083f\u083d\3\2\2\2\u083f\u0840\3\2\2\2\u0840\u0105\3\2\2\2\u0841"+
		"\u083f\3\2\2\2\u0842\u0843\5\u013c\u009f\2\u0843\u0844\7\u008c\2\2\u0844"+
		"\u0845\5\u0106\u0084\2\u0845\u085f\3\2\2\2\u0846\u0847\7\u0082\2\2\u0847"+
		"\u085f\5\u0106\u0084\2\u0848\u0849\5\u0108\u0085\2\u0849\u084a\5\u010e"+
		"\u0088\2\u084a\u085f\3\2\2\2\u084b\u084c\5\u0108\u0085\2\u084c\u084d\5"+
		"\u0110\u0089\2\u084d\u085f\3\2\2\2\u084e\u0850\5\u013c\u009f\2\u084f\u0851"+
		"\7\u0080\2\2\u0850\u084f\3\2\2\2\u0850\u0851\3\2\2\2\u0851\u085f\3\2\2"+
		"\2\u0852\u0854\7\u0081\2\2\u0853\u0855\7\u0080\2\2\u0854\u0853\3\2\2\2"+
		"\u0854\u0855\3\2\2\2\u0855\u085f\3\2\2\2\u0856\u085b\5\u010c\u0087\2\u0857"+
		"\u0858\7\u0083\2\2\u0858\u085a\5\u010c\u0087\2\u0859\u0857\3\2\2\2\u085a"+
		"\u085d\3\2\2\2\u085b\u0859\3\2\2\2\u085b\u085c\3\2\2\2\u085c\u085f\3\2"+
		"\2\2\u085d\u085b\3\2\2\2\u085e\u0842\3\2\2\2\u085e\u0846\3\2\2\2\u085e"+
		"\u0848\3\2\2\2\u085e\u084b\3\2\2\2\u085e\u084e\3\2\2\2\u085e\u0852\3\2"+
		"\2\2\u085e\u0856\3\2\2\2\u085f\u0107\3\2\2\2\u0860\u086c\5\u010a\u0086"+
		"\2\u0861\u0862\7Z\2\2\u0862\u0865\5\u010a\u0086\2\u0863\u0864\7\u0083"+
		"\2\2\u0864\u0866\5\u010a\u0086\2\u0865\u0863\3\2\2\2\u0866\u0867\3\2\2"+
		"\2\u0867\u0865\3\2\2\2\u0867\u0868\3\2\2\2\u0868\u0869\3\2\2\2\u0869\u086a"+
		"\7[\2\2\u086a\u086c\3\2\2\2\u086b\u0860\3\2\2\2\u086b\u0861\3\2\2\2\u086c"+
		"\u0109\3\2\2\2\u086d\u086f\5\u013c\u009f\2\u086e\u0870\7\u0084\2\2\u086f"+
		"\u086e\3\2\2\2\u086f\u0870\3\2\2\2\u0870\u0877\3\2\2\2\u0871\u0873\5\u013c"+
		"\u009f\2\u0872\u0874\7\u0085\2\2\u0873\u0872\3\2\2\2\u0873\u0874\3\2\2"+
		"\2\u0874\u0877\3\2\2\2\u0875\u0877\5\u010c\u0087\2\u0876\u086d\3\2\2\2"+
		"\u0876\u0871\3\2\2\2\u0876\u0875\3\2\2\2\u0877\u010b\3\2\2\2\u0878\u087a"+
		"\7q\2\2\u0879\u0878\3\2\2\2\u0879\u087a\3\2\2\2\u087a\u087b\3\2\2\2\u087b"+
		"\u0884\7O\2\2\u087c\u087e\7q\2\2\u087d\u087c\3\2\2\2\u087d\u087e\3\2\2"+
		"\2\u087e\u087f\3\2\2\2\u087f\u0884\7S\2\2\u0880\u0884\7V\2\2\u0881\u0884"+
		"\7W\2\2\u0882\u0884\7X\2\2\u0883\u0879\3\2\2\2\u0883\u087d\3\2\2\2\u0883"+
		"\u0880\3\2\2\2\u0883\u0881\3\2\2\2\u0883\u0882\3\2\2\2\u0884\u010d\3\2"+
		"\2\2\u0885\u088e\7Z\2\2\u0886\u088b\5\u0106\u0084\2\u0887\u0888\7a\2\2"+
		"\u0888\u088a\5\u0106\u0084\2\u0889\u0887\3\2\2\2\u088a\u088d\3\2\2\2\u088b"+
		"\u0889\3\2\2\2\u088b\u088c\3\2\2\2\u088c\u088f\3\2\2\2\u088d\u088b\3\2"+
		"\2\2\u088e\u0886\3\2\2\2\u088e\u088f\3\2\2\2\u088f\u0890\3\2\2\2\u0890"+
		"\u0891\7[\2\2\u0891\u010f\3\2\2\2\u0892\u08a0\7^\2\2\u0893\u0894\5\u013c"+
		"\u009f\2\u0894\u0895\7c\2\2\u0895\u089d\5\u0106\u0084\2\u0896\u0897\7"+
		"a\2\2\u0897\u0898\5\u013c\u009f\2\u0898\u0899\7c\2\2\u0899\u089a\5\u0106"+
		"\u0084\2\u089a\u089c\3\2\2\2\u089b\u0896\3\2\2\2\u089c\u089f\3\2\2\2\u089d"+
		"\u089b\3\2\2\2\u089d\u089e\3\2\2\2\u089e\u08a1\3\2\2\2\u089f\u089d\3\2"+
		"\2\2\u08a0\u0893\3\2\2\2\u08a0\u08a1\3\2\2\2\u08a1\u08a2\3\2\2\2\u08a2"+
		"\u08a3\7_\2\2\u08a3\u0111\3\2\2\2\u08a4\u08a5\7<\2\2\u08a5\u08a8\5\u013c"+
		"\u009f\2\u08a6\u08a7\7\23\2\2\u08a7\u08a9\5\u013c\u009f\2\u08a8\u08a6"+
		"\3\2\2\2\u08a8\u08a9\3\2\2\2\u08a9\u08aa\3\2\2\2\u08aa\u08ab\7\\\2\2\u08ab"+
		"\u08ad\5\u011c\u008f\2\u08ac\u08ae\5\u0120\u0091\2\u08ad\u08ac\3\2\2\2"+
		"\u08ad\u08ae\3\2\2\2\u08ae\u08b0\3\2\2\2\u08af\u08b1\5\u011e\u0090\2\u08b0"+
		"\u08af\3\2\2\2\u08b0\u08b1\3\2\2\2\u08b1\u08b2\3\2\2\2\u08b2\u08b3\7]"+
		"\2\2\u08b3\u0113\3\2\2\2\u08b4\u08b5\79\2\2\u08b5\u08b6\5\u013c\u009f"+
		"\2\u08b6\u08b7\5\u013c\u009f\2\u08b7\u08b9\7Z\2\2\u08b8\u08ba\5\u00f4"+
		"{\2\u08b9\u08b8\3\2\2\2\u08b9\u08ba\3\2\2\2\u08ba\u08bb\3\2\2\2\u08bb"+
		"\u08bc\7[\2\2\u08bc\u08c3\7\\\2\2\u08bd\u08c2\5\u0122\u0092\2\u08be\u08c2"+
		"\5\u0124\u0093\2\u08bf\u08c2\5\u012e\u0098\2\u08c0\u08c2\5\u013a\u009e"+
		"\2\u08c1\u08bd\3\2\2\2\u08c1\u08be\3\2\2\2\u08c1\u08bf\3\2\2\2\u08c1\u08c0"+
		"\3\2\2\2\u08c2\u08c5\3\2\2\2\u08c3\u08c1\3\2\2\2\u08c3\u08c4\3\2\2\2\u08c4"+
		"\u08c6\3\2\2\2\u08c5\u08c3\3\2\2\2\u08c6\u08c7\7]\2\2\u08c7\u0115\3\2"+
		"\2\2\u08c8\u08c9\7;\2\2\u08c9\u08ca\5\u013c\u009f\2\u08ca\u08cb\5\u013c"+
		"\u009f\2\u08cb\u08cc\7Z\2\2\u08cc\u08cd\5\u013c\u009f\2\u08cd\u08ce\7"+
		"\u0080\2\2\u08ce\u08cf\7[\2\2\u08cf\u08d8\7\\\2\2\u08d0\u08d7\5\u0122"+
		"\u0092\2\u08d1\u08d7\5\u0126\u0094\2\u08d2\u08d7\5\u012c\u0097\2\u08d3"+
		"\u08d7\5\u0130\u0099\2\u08d4\u08d7\5\u0132\u009a\2\u08d5\u08d7\5\u0136"+
		"\u009c\2\u08d6\u08d0\3\2\2\2\u08d6\u08d1\3\2\2\2\u08d6\u08d2\3\2\2\2\u08d6"+
		"\u08d3\3\2\2\2\u08d6\u08d4\3\2\2\2\u08d6\u08d5\3\2\2\2\u08d7\u08da\3\2"+
		"\2\2\u08d8\u08d6\3\2\2\2\u08d8\u08d9\3\2\2\2\u08d9\u08db\3\2\2\2\u08da"+
		"\u08d8\3\2\2\2\u08db\u08dc\7]\2\2\u08dc\u0117\3\2\2\2\u08dd\u08de\7:\2"+
		"\2\u08de\u08df\5\u013c\u009f\2\u08df\u08e0\5\u013c\u009f\2\u08e0\u08e1"+
		"\7Z\2\2\u08e1\u08e2\5\u013c\u009f\2\u08e2\u08e3\7\u0080\2\2\u08e3\u08e4"+
		"\7[\2\2\u08e4\u08ec\7\\\2\2\u08e5\u08eb\5\u0122\u0092\2\u08e6\u08eb\5"+
		"\u0128\u0095\2\u08e7\u08eb\5\u012a\u0096\2\u08e8\u08eb\5\u0134\u009b\2"+
		"\u08e9\u08eb\5\u0138\u009d\2\u08ea\u08e5\3\2\2\2\u08ea\u08e6\3\2\2\2\u08ea"+
		"\u08e7\3\2\2\2\u08ea\u08e8\3\2\2\2\u08ea\u08e9\3\2\2\2\u08eb\u08ee\3\2"+
		"\2\2\u08ec\u08ea\3\2\2\2\u08ec\u08ed\3\2\2\2\u08ed\u08ef\3\2\2\2\u08ee"+
		"\u08ec\3\2\2\2\u08ef\u08f0\7]\2\2\u08f0\u0119\3\2\2\2\u08f1\u08f2\7\\"+
		"\2\2\u08f2\u08f3\5\u00a8U\2\u08f3\u08f4\7]\2\2\u08f4\u011b\3\2\2\2\u08f5"+
		"\u08f6\7L\2\2\u08f6\u08f7\7\\\2\2\u08f7\u08f8\5\u00caf\2\u08f8\u08f9\7"+
		"]\2\2\u08f9\u011d\3\2\2\2\u08fa\u08fb\7M\2\2\u08fb\u08fc\7Z\2\2\u08fc"+
		"\u08fd\5\u013c\u009f\2\u08fd\u08fe\7a\2\2\u08fe\u08ff\5\u013c\u009f\2"+
		"\u08ff\u0900\7[\2\2\u0900\u0901\5\u011a\u008e\2\u0901\u011f\3\2\2\2\u0902"+
		"\u0903\7@\2\2\u0903\u0904\7Z\2\2\u0904\u0905\5\u013c\u009f\2\u0905\u0906"+
		"\7[\2\2\u0906\u0907\5\u011a\u008e\2\u0907\u0121\3\2\2\2\u0908\u0909\7"+
		"?\2\2\u0909\u090a\7Z\2\2\u090a\u090b\5\u013c\u009f\2\u090b\u090c\7[\2"+
		"\2\u090c\u090d\5\u011a\u008e\2\u090d\u0123\3\2\2\2\u090e\u090f\7A\2\2"+
		"\u090f\u0918\7Z\2\2\u0910\u0915\5\u013c\u009f\2\u0911\u0912\7a\2\2\u0912"+
		"\u0914\5\u013c\u009f\2\u0913\u0911\3\2\2\2\u0914\u0917\3\2\2\2\u0915\u0913"+
		"\3\2\2\2\u0915\u0916\3\2\2\2\u0916\u0919\3\2\2\2\u0917\u0915\3\2\2\2\u0918"+
		"\u0910\3\2\2\2\u0918\u0919\3\2\2\2\u0919\u091a\3\2\2\2\u091a\u091b\7["+
		"\2\2\u091b\u091c\5\u011a\u008e\2\u091c\u0125\3\2\2\2\u091d\u091e\7B\2"+
		"\2\u091e\u091f\7Z\2\2\u091f\u0920\7[\2\2\u0920\u0921\5\u011a\u008e\2\u0921"+
		"\u0127\3\2\2\2\u0922\u0923\7B\2\2\u0923\u0924\7Z\2\2\u0924\u0925\5\u013c"+
		"\u009f\2\u0925\u0926\7[\2\2\u0926\u0927\5\u011a\u008e\2\u0927\u0129\3"+
		"\2\2\2\u0928\u0929\7C\2\2\u0929\u092a\7Z\2\2\u092a\u092b\5\u013c\u009f"+
		"\2\u092b\u092c\7a\2\2\u092c\u092d\5\u013c\u009f\2\u092d\u092e\7[\2\2\u092e"+
		"\u092f\5\u011a\u008e\2\u092f\u012b\3\2\2\2\u0930\u0931\7D\2\2\u0931\u0932"+
		"\7Z\2\2\u0932\u0933\5\u013c\u009f\2\u0933\u0934\7a\2\2\u0934\u0935\5\u013c"+
		"\u009f\2\u0935\u0936\7[\2\2\u0936\u0937\5\u011a\u008e\2\u0937\u012d\3"+
		"\2\2\2\u0938\u0939\7E\2\2\u0939\u093a\7Z\2\2\u093a\u093b\5\u013c\u009f"+
		"\2\u093b\u093c\7a\2\2\u093c\u093d\5\u013c\u009f\2\u093d\u093e\7[\2\2\u093e"+
		"\u093f\5\u011a\u008e\2\u093f\u012f\3\2\2\2\u0940\u0941\7H\2\2\u0941\u0942"+
		"\7Z\2\2\u0942\u0943\5\u013c\u009f\2\u0943\u0944\7[\2\2\u0944\u0945\5\u011a"+
		"\u008e\2\u0945\u0131\3\2\2\2\u0946\u0947\7I\2\2\u0947\u0948\7Z\2\2\u0948"+
		"\u0949\5\u013c\u009f\2\u0949\u094a\7[\2\2\u094a\u094b\5\u011a\u008e\2"+
		"\u094b\u0133\3\2\2\2\u094c\u094d\7G\2\2\u094d\u094e\7Z\2\2\u094e\u094f"+
		"\5\u013c\u009f\2\u094f\u0950\7a\2\2\u0950\u0951\5\u013c\u009f\2\u0951"+
		"\u0952\7[\2\2\u0952\u0953\5\u011a\u008e\2\u0953\u0135\3\2\2\2\u0954\u0955"+
		"\7K\2\2\u0955\u0956\7Z\2\2\u0956\u0957\5\u013c\u009f\2\u0957\u0958\7["+
		"\2\2\u0958\u0959\5\u011a\u008e\2\u0959\u0137\3\2\2\2\u095a\u095b\7J\2"+
		"\2\u095b\u095c\7Z\2\2\u095c\u095d\5\u013c\u009f\2\u095d\u095e\7[\2\2\u095e"+
		"\u095f\5\u011a\u008e\2\u095f\u0139\3\2\2\2\u0960\u0961\7F\2\2\u0961\u0962"+
		"\7Z\2\2\u0962\u0963\5\u013c\u009f\2\u0963\u0964\7[\2\2\u0964\u0965\5\u011a"+
		"\u008e\2\u0965\u013b\3\2\2\2\u0966\u0967\t\22\2\2\u0967\u013d\3\2\2\2"+
		"\u011c\u013f\u0144\u014a\u0152\u015d\u0166\u016b\u0172\u0179\u017c\u0183"+
		"\u018d\u0191\u0196\u019a\u019e\u01a8\u01b0\u01b6\u01bd\u01c4\u01c8\u01cb"+
		"\u01ce\u01d7\u01dd\u01e2\u01e5\u01eb\u01f1\u01f5\u01fd\u0206\u020d\u0213"+
		"\u0218\u0223\u022c\u0231\u0237\u023b\u0247\u0252\u0258\u0261\u0269\u0273"+
		"\u027c\u0284\u0289\u0291\u0296\u02a0\u02aa\u02b0\u02b7\u02bc\u02c4\u02c8"+
		"\u02ca\u02d0\u02d5\u02d9\u02e0\u02e2\u02e9\u02ee\u02f7\u02fc\u02ff\u0304"+
		"\u030d\u0319\u0322\u032d\u0330\u0337\u0341\u0349\u034c\u034f\u035c\u0364"+
		"\u036a\u0372\u0376\u037a\u037e\u0380\u0384\u038a\u0395\u03a0\u03a5\u03ae"+
		"\u03b3\u03b6\u03bd\u03c6\u03dd\u03e0\u03e3\u03eb\u03ef\u03f7\u03fd\u0408"+
		"\u0411\u0416\u0422\u0429\u0436\u043f\u0448\u044e\u0459\u045e\u0463\u0469"+
		"\u046d\u0471\u0475\u0477\u047b\u0480\u0491\u0499\u04ac\u04b0\u04b5\u04ba"+
		"\u04ca\u04f1\u04fa\u04fe\u050d\u0512\u0515\u0517\u0519\u0524\u052c\u0530"+
		"\u0535\u0539\u0543\u0548\u054a\u0554\u055d\u0562\u0566\u0578\u057a\u057f"+
		"\u0584\u0589\u0592\u0594\u0598\u059d\u05a1\u05a5\u05a9\u05b3\u05bf\u05c6"+
		"\u05c9\u05cd\u05d5\u05da\u05e5\u05e9\u05ed\u05f3\u05fe\u0607\u0609\u0610"+
		"\u0614\u0622\u0628\u0633\u0636";
	private static final String _serializedATNSegment1 =
		"\u0639\u063f\u0648\u0651\u065b\u0661\u0669\u0670\u067a\u067c\u0683\u0685"+
		"\u0689\u0691\u069c\u06a4\u06ac\u06b9\u06be\u06c0\u06ca\u06cf\u06d8\u06e1"+
		"\u06e4\u06ec\u06f7\u06fc\u06fe\u0722\u072a\u072c\u0731\u073a\u073d\u0742"+
		"\u0745\u0748\u0751\u0754\u0759\u075c\u0765\u0768\u076d\u0770\u0774\u0777"+
		"\u077b\u0789\u078c\u0798\u079b\u07a7\u07a9\u07b3\u07b6\u07c0\u07d4\u07d8"+
		"\u07dd\u07e1\u07f1\u0819\u081c\u0825\u082b\u083a\u083d\u083f\u0850\u0854"+
		"\u085b\u085e\u0867\u086b\u086f\u0873\u0876\u0879\u087d\u0883\u088b\u088e"+
		"\u089d\u08a0\u08a8\u08ad\u08b0\u08b9\u08c1\u08c3\u08d6\u08d8\u08ea\u08ec"+
		"\u0915\u0918";
	public static final String _serializedATN = Utils.join(
		new String[] {
			_serializedATNSegment0,
			_serializedATNSegment1
		},
		""
	);
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}