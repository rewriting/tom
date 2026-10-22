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
		LSHIFT=126, STAR=127, UNDERSCORE=128, ANTI=129, PIPE=130, QMARK=131, DQMARK=132, 
		SLASH=133, BACKSLASH=134, BQUOTE=135, METAQUOTE=136, ARROW=137, COLONCOLON=138, 
		AT=139, ELLIPSIS=140, WS=141, COMMENT=142, LINE_COMMENT=143, IDENTIFIER=144, 
		GOMSTART=145, GOM_WS=146, GOM_COMMENT=147, GOM_LINE_COMMENT=148, HOOKSTART=149, 
		GOMEND=150, MODULE=151, IMPORTS=152, GOM_PUBLIC=153, GOM_ABSTRACT=154, 
		SYNTAX=155, SORT=156, OPERATOR=157, ATOM=158, INNER=159, OUTER=160, NEUTRAL=161, 
		BINDS=162, GOM_COLON=163, GOM_COMMA=164, GOM_DOT=165, GOM_LPAREN=166, 
		GOM_RPAREN=167, GOM_STAR=168, GOM_EQUAL=169, ALT=170, GOM_SEMI=171, LDIPLE=172, 
		RDIPLE=173, JAVADOC=174, RULES=175, GRAPHRULES=176, AC=177, ACU=178, AU=179, 
		FL=180, FREE=181, HOOK_MAKE=182, HOOK_MAKE_INSERT=183, HOOK_MAKE_EMPTY=184, 
		HOOK_IMPORT=185, HOOK_INTERFACE=186, BLOCK=187, MAPPING=188, ID=189, GOM_INSIDE_WS=190, 
		GOM_INSIDE_COMMENT=191, GOM_INSIDE_LINE_COMMENT=192, IDENTITY=193, FAIL=194, 
		RULE_ARG=195, ARG_COMMA=196, RULE_LPAREN=197, RULE_RPAREN=198, RULESTART=199, 
		RULE_WS=200, RULE_COMMENT=201, RULE_LINE_COMMENT=202, RULEEND=203, RULE_ARROW=204, 
		AMPERSAND=205, RULE_UNDERSCORE=206, RULE_STAR=207, RULE_AT=208, RULE_COLON=209, 
		LPAR=210, RPAR=211, RULE_COMMA=212, RULE_AND=213, RULE_OR=214, NOT=215, 
		RULE_EQUALS=216, NOTEQUALS=217, LEQ=218, MATCH_SYMBOL=219, RULE_LT=220, 
		GEQ=221, RULE_GT=222, RULE_IF=223, INTEGER=224, STRING=225, RULE_ID=226, 
		RULE_INSIDE_WS=227, RULE_INSIDE_COMMENT=228, RULE_INSIDE_LINE_COMMENT=229, 
		BLOCKSTART=230, OPTIONSTART=231, OPTIONEND=232, DMINUSID=233, UNKNOWNBLOCK_WS=234, 
		UNKNOWNBLOCK_COMMENT=235, UNKNOWNBLOCK_LINE_COMMENT=236, SUBBLOCKSTART=237, 
		BLOCKEND=238, ANY=239, SUBSUBBLOCKSTART=240, SUBBLOCKEND=241, SUB_ANY=242;
	public static final int
		RULE_compilationUnit = 0, RULE_declarationsUnit = 1, RULE_expressionUnit = 2, 
		RULE_gomUnit = 3, RULE_packageDeclaration = 4, RULE_importDeclaration = 5, 
		RULE_typeDeclaration = 6, RULE_modifier = 7, RULE_classOrInterfaceModifier = 8, 
		RULE_variableModifier = 9, RULE_classDeclaration = 10, RULE_typeParameters = 11, 
		RULE_typeParameter = 12, RULE_typeBound = 13, RULE_enumDeclaration = 14, 
		RULE_enumConstants = 15, RULE_enumConstant = 16, RULE_enumBodyDeclarations = 17, 
		RULE_interfaceDeclaration = 18, RULE_classBody = 19, RULE_interfaceBody = 20, 
		RULE_classBodyDeclaration = 21, RULE_memberDeclaration = 22, RULE_methodDeclaration = 23, 
		RULE_methodBody = 24, RULE_typeTypeOrVoid = 25, RULE_genericMethodDeclaration = 26, 
		RULE_genericConstructorDeclaration = 27, RULE_constructorDeclaration = 28, 
		RULE_fieldDeclaration = 29, RULE_interfaceBodyDeclaration = 30, RULE_interfaceMemberDeclaration = 31, 
		RULE_constDeclaration = 32, RULE_constantDeclarator = 33, RULE_interfaceMethodDeclaration = 34, 
		RULE_interfaceMethodModifier = 35, RULE_genericInterfaceMethodDeclaration = 36, 
		RULE_variableDeclarators = 37, RULE_variableDeclarator = 38, RULE_variableDeclaratorId = 39, 
		RULE_variableInitializer = 40, RULE_arrayInitializer = 41, RULE_classOrInterfaceType = 42, 
		RULE_typeArgument = 43, RULE_qualifiedNameList = 44, RULE_formalParameters = 45, 
		RULE_formalParameterList = 46, RULE_formalParameter = 47, RULE_lastFormalParameter = 48, 
		RULE_qualifiedName = 49, RULE_literal = 50, RULE_integerLiteral = 51, 
		RULE_floatLiteral = 52, RULE_annotation = 53, RULE_elementValuePairs = 54, 
		RULE_elementValuePair = 55, RULE_elementValue = 56, RULE_elementValueArrayInitializer = 57, 
		RULE_annotationTypeDeclaration = 58, RULE_annotationTypeBody = 59, RULE_annotationTypeElementDeclaration = 60, 
		RULE_annotationTypeElementRest = 61, RULE_annotationMethodOrConstantRest = 62, 
		RULE_annotationMethodRest = 63, RULE_annotationConstantRest = 64, RULE_defaultValue = 65, 
		RULE_block = 66, RULE_blockStatement = 67, RULE_localVariableDeclaration = 68, 
		RULE_localTypeDeclaration = 69, RULE_statement = 70, RULE_catchClause = 71, 
		RULE_catchType = 72, RULE_finallyBlock = 73, RULE_resourceSpecification = 74, 
		RULE_resources = 75, RULE_resource = 76, RULE_switchBlockStatementGroup = 77, 
		RULE_switchLabel = 78, RULE_forControl = 79, RULE_forInit = 80, RULE_enhancedForControl = 81, 
		RULE_parExpression = 82, RULE_expressionList = 83, RULE_expression = 84, 
		RULE_funTerm = 85, RULE_lambdaExpression = 86, RULE_lambdaParameters = 87, 
		RULE_lambdaBody = 88, RULE_primary = 89, RULE_classType = 90, RULE_creator = 91, 
		RULE_createdName = 92, RULE_innerCreator = 93, RULE_arrayCreatorRest = 94, 
		RULE_classCreatorRest = 95, RULE_explicitGenericInvocation = 96, RULE_typeArgumentsOrDiamond = 97, 
		RULE_nonWildcardTypeArgumentsOrDiamond = 98, RULE_nonWildcardTypeArguments = 99, 
		RULE_typeList = 100, RULE_typeType = 101, RULE_primitiveType = 102, RULE_typeArguments = 103, 
		RULE_superSuffix = 104, RULE_explicitGenericInvocationSuffix = 105, RULE_arguments = 106, 
		RULE_javaIdentifier = 107, RULE_tomDeclaration = 108, RULE_tomStatement = 109, 
		RULE_tomTerm = 110, RULE_metaquote = 111, RULE_matchStatement = 112, RULE_strategyStatement = 113, 
		RULE_includeStatement = 114, RULE_gomStatement = 115, RULE_gomOptions = 116, 
		RULE_gomBlock = 117, RULE_ruleStatement = 118, RULE_unknownBlock = 119, 
		RULE_visit = 120, RULE_actionRule = 121, RULE_tomBlock = 122, RULE_slotList = 123, 
		RULE_slot = 124, RULE_patternlist = 125, RULE_constraint = 126, RULE_term = 127, 
		RULE_bqterm = 128, RULE_pairSlotBqterm = 129, RULE_bqcomposite = 130, 
		RULE_composite = 131, RULE_pattern = 132, RULE_fsymbol = 133, RULE_headSymbol = 134, 
		RULE_constant = 135, RULE_explicitArgs = 136, RULE_implicitArgs = 137, 
		RULE_typeterm = 138, RULE_operator = 139, RULE_oplist = 140, RULE_oparray = 141, 
		RULE_termBlock = 142, RULE_implement = 143, RULE_equalsTerm = 144, RULE_isSort = 145, 
		RULE_isFsym = 146, RULE_make = 147, RULE_makeEmptyList = 148, RULE_makeEmptyArray = 149, 
		RULE_makeAppendArray = 150, RULE_makeInsertList = 151, RULE_getSlot = 152, 
		RULE_getHead = 153, RULE_getTail = 154, RULE_getElement = 155, RULE_isEmptyList = 156, 
		RULE_getSize = 157, RULE_getDefault = 158, RULE_tomIdentifier = 159, RULE_module = 160, 
		RULE_modulename = 161, RULE_imports = 162, RULE_section = 163, RULE_adtgrammar = 164, 
		RULE_syntax = 165, RULE_atomdecl = 166, RULE_typedecl = 167, RULE_alternatives = 168, 
		RULE_pattern_alternatives = 169, RULE_opdecl = 170, RULE_pattern_opdecl = 171, 
		RULE_fieldlist = 172, RULE_pattern_fieldlist = 173, RULE_field = 174, 
		RULE_pattern_field = 175, RULE_arglist = 176, RULE_hookConstruct = 177, 
		RULE_hookBlock = 178, RULE_ruleBlock = 179, RULE_hookScope = 180, RULE_gomIdentifier = 181, 
		RULE_ruleset = 182, RULE_termrule = 183, RULE_graphruleset = 184, RULE_graphrule = 185, 
		RULE_condition = 186, RULE_andcondition = 187, RULE_simplecondition = 188, 
		RULE_rule_pattern = 189, RULE_rule_term = 190, RULE_builtin = 191, RULE_labelledpattern = 192, 
		RULE_graphpattern = 193, RULE_variable = 194, RULE_ref = 195, RULE_constructor = 196;
	public static final String[] ruleNames = {
		"compilationUnit", "declarationsUnit", "expressionUnit", "gomUnit", "packageDeclaration", 
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
		"gomStatement", "gomOptions", "gomBlock", "ruleStatement", "unknownBlock", 
		"visit", "actionRule", "tomBlock", "slotList", "slot", "patternlist", 
		"constraint", "term", "bqterm", "pairSlotBqterm", "bqcomposite", "composite", 
		"pattern", "fsymbol", "headSymbol", "constant", "explicitArgs", "implicitArgs", 
		"typeterm", "operator", "oplist", "oparray", "termBlock", "implement", 
		"equalsTerm", "isSort", "isFsym", "make", "makeEmptyList", "makeEmptyArray", 
		"makeAppendArray", "makeInsertList", "getSlot", "getHead", "getTail", 
		"getElement", "isEmptyList", "getSize", "getDefault", "tomIdentifier", 
		"module", "modulename", "imports", "section", "adtgrammar", "syntax", 
		"atomdecl", "typedecl", "alternatives", "pattern_alternatives", "opdecl", 
		"pattern_opdecl", "fieldlist", "pattern_fieldlist", "field", "pattern_field", 
		"arglist", "hookConstruct", "hookBlock", "ruleBlock", "hookScope", "gomIdentifier", 
		"ruleset", "termrule", "graphruleset", "graphrule", "condition", "andcondition", 
		"simplecondition", "rule_pattern", "rule_term", "builtin", "labelledpattern", 
		"graphpattern", "variable", "ref", "constructor"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'assert'", "'boolean'", "'break'", "'byte'", "'case'", "'catch'", 
		"'char'", "'class'", "'const'", "'continue'", "'default'", "'do'", "'double'", 
		"'else'", "'enum'", "'extends'", "'final'", "'finally'", "'float'", "'for'", 
		null, "'goto'", "'implements'", null, "'instanceof'", "'int'", null, "'long'", 
		"'native'", "'new'", "'package'", "'private'", "'protected'", null, "'return'", 
		"'short'", "'static'", "'strictfp'", "'super'", "'switch'", "'synchronized'", 
		"'this'", "'throw'", "'throws'", "'transient'", "'try'", "'void'", "'volatile'", 
		"'while'", "'%match'", "'%strategy'", "'%include'", "'%gom'", "'%op'", 
		"'%oparray'", "'%oplist'", "'%typeterm'", "'%rule'", "'visit'", "'is_fsym'", 
		"'is_sort'", null, null, "'make_append'", null, "'get_slot'", "'get_default'", 
		"'get_element'", "'get_head'", "'get_tail'", "'get_size'", "'is_empty'", 
		"'implement'", "'equals'", "'when'", null, null, null, null, null, null, 
		null, null, null, null, "'null'", null, null, null, null, "'['", "']'", 
		"';'", null, null, null, null, null, "'~'", null, null, null, null, null, 
		null, null, "'++'", "'--'", "'+'", "'-'", null, "'^'", "'%'", "'+='", 
		"'-='", "'*='", "'/='", "'&='", "'|='", "'^='", "'%='", "'<<='", "'>>='", 
		"'>>>='", null, null, null, null, null, "'?'", "'??'", "'/'", "'\\'", 
		"'`'", null, null, "'::'", null, "'...'", null, null, null, null, null, 
		null, null, null, null, null, "'module'", "'imports'", null, null, "'syntax'", 
		"'sort'", "'operator'", "'atom'", "'inner'", "'outer'", "'neutral'", "'binds'", 
		null, null, null, null, null, null, null, null, "';;'", null, null, null, 
		"'rules'", "'graphrules'", "'AC'", "'ACU'", "'AU'", "'FL'", "'Free'", 
		null, null, null, null, null, "'block'", "'mapping'", null, null, null, 
		null, "'Identity'", "'Fail'"
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
		"MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "LSHIFT", 
		"STAR", "UNDERSCORE", "ANTI", "PIPE", "QMARK", "DQMARK", "SLASH", "BACKSLASH", 
		"BQUOTE", "METAQUOTE", "ARROW", "COLONCOLON", "AT", "ELLIPSIS", "WS", 
		"COMMENT", "LINE_COMMENT", "IDENTIFIER", "GOMSTART", "GOM_WS", "GOM_COMMENT", 
		"GOM_LINE_COMMENT", "HOOKSTART", "GOMEND", "MODULE", "IMPORTS", "GOM_PUBLIC", 
		"GOM_ABSTRACT", "SYNTAX", "SORT", "OPERATOR", "ATOM", "INNER", "OUTER", 
		"NEUTRAL", "BINDS", "GOM_COLON", "GOM_COMMA", "GOM_DOT", "GOM_LPAREN", 
		"GOM_RPAREN", "GOM_STAR", "GOM_EQUAL", "ALT", "GOM_SEMI", "LDIPLE", "RDIPLE", 
		"JAVADOC", "RULES", "GRAPHRULES", "AC", "ACU", "AU", "FL", "FREE", "HOOK_MAKE", 
		"HOOK_MAKE_INSERT", "HOOK_MAKE_EMPTY", "HOOK_IMPORT", "HOOK_INTERFACE", 
		"BLOCK", "MAPPING", "ID", "GOM_INSIDE_WS", "GOM_INSIDE_COMMENT", "GOM_INSIDE_LINE_COMMENT", 
		"IDENTITY", "FAIL", "RULE_ARG", "ARG_COMMA", "RULE_LPAREN", "RULE_RPAREN", 
		"RULESTART", "RULE_WS", "RULE_COMMENT", "RULE_LINE_COMMENT", "RULEEND", 
		"RULE_ARROW", "AMPERSAND", "RULE_UNDERSCORE", "RULE_STAR", "RULE_AT", 
		"RULE_COLON", "LPAR", "RPAR", "RULE_COMMA", "RULE_AND", "RULE_OR", "NOT", 
		"RULE_EQUALS", "NOTEQUALS", "LEQ", "MATCH_SYMBOL", "RULE_LT", "GEQ", "RULE_GT", 
		"RULE_IF", "INTEGER", "STRING", "RULE_ID", "RULE_INSIDE_WS", "RULE_INSIDE_COMMENT", 
		"RULE_INSIDE_LINE_COMMENT", "BLOCKSTART", "OPTIONSTART", "OPTIONEND", 
		"DMINUSID", "UNKNOWNBLOCK_WS", "UNKNOWNBLOCK_COMMENT", "UNKNOWNBLOCK_LINE_COMMENT", 
		"SUBBLOCKSTART", "BLOCKEND", "ANY", "SUBSUBBLOCKSTART", "SUBBLOCKEND", 
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
			setState(395);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(394);
				packageDeclaration();
				}
				break;
			}
			setState(400);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(397);
				importDeclaration();
				}
				}
				setState(402);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(406);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << CLASS) | (1L << ENUM) | (1L << FINAL) | (1L << INTERFACE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << STATIC) | (1L << STRICTFP))) != 0) || _la==SEMI || _la==AT) {
				{
				{
				setState(403);
				typeDeclaration();
				}
				}
				setState(408);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(409);
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
			setState(414);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(411);
				classBodyDeclaration();
				}
				}
				setState(416);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(417);
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
			setState(419);
			expression(0);
			setState(420);
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

	public static class GomUnitContext extends ParserRuleContext {
		public ModuleContext module() {
			return getRuleContext(ModuleContext.class,0);
		}
		public TerminalNode EOF() { return getToken(TomJavaParser.EOF, 0); }
		public GomUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gomUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGomUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGomUnit(this);
		}
	}

	public final GomUnitContext gomUnit() throws RecognitionException {
		GomUnitContext _localctx = new GomUnitContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_gomUnit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(422);
			module();
			setState(423);
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
		enterRule(_localctx, 8, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(425);
				annotation();
				}
				}
				setState(430);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(431);
			match(PACKAGE);
			setState(432);
			qualifiedName();
			setState(433);
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
		public TerminalNode DOT() { return getToken(TomJavaParser.DOT, 0); }
		public TerminalNode STAR() { return getToken(TomJavaParser.STAR, 0); }
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
		enterRule(_localctx, 10, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435);
			match(IMPORT);
			setState(437);
			_la = _input.LA(1);
			if (_la==STATIC) {
				{
				setState(436);
				match(STATIC);
				}
			}

			setState(439);
			qualifiedName();
			setState(442);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(440);
				match(DOT);
				setState(441);
				match(STAR);
				}
			}

			setState(444);
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
		enterRule(_localctx, 12, RULE_typeDeclaration);
		try {
			int _alt;
			setState(459);
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
				setState(449);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(446);
						classOrInterfaceModifier();
						}
						} 
					}
					setState(451);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				}
				setState(456);
				switch (_input.LA(1)) {
				case CLASS:
					{
					setState(452);
					classDeclaration();
					}
					break;
				case ENUM:
					{
					setState(453);
					enumDeclaration();
					}
					break;
				case INTERFACE:
					{
					setState(454);
					interfaceDeclaration();
					}
					break;
				case AT:
					{
					setState(455);
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
				setState(458);
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
		enterRule(_localctx, 14, RULE_modifier);
		try {
			setState(466);
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
				setState(461);
				classOrInterfaceModifier();
				}
				break;
			case NATIVE:
				enterOuterAlt(_localctx, 2);
				{
				setState(462);
				match(NATIVE);
				}
				break;
			case SYNCHRONIZED:
				enterOuterAlt(_localctx, 3);
				{
				setState(463);
				match(SYNCHRONIZED);
				}
				break;
			case TRANSIENT:
				enterOuterAlt(_localctx, 4);
				{
				setState(464);
				match(TRANSIENT);
				}
				break;
			case VOLATILE:
				enterOuterAlt(_localctx, 5);
				{
				setState(465);
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
		enterRule(_localctx, 16, RULE_classOrInterfaceModifier);
		try {
			setState(476);
			switch (_input.LA(1)) {
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(468);
				annotation();
				}
				break;
			case PUBLIC:
				enterOuterAlt(_localctx, 2);
				{
				setState(469);
				match(PUBLIC);
				}
				break;
			case PROTECTED:
				enterOuterAlt(_localctx, 3);
				{
				setState(470);
				match(PROTECTED);
				}
				break;
			case PRIVATE:
				enterOuterAlt(_localctx, 4);
				{
				setState(471);
				match(PRIVATE);
				}
				break;
			case STATIC:
				enterOuterAlt(_localctx, 5);
				{
				setState(472);
				match(STATIC);
				}
				break;
			case ABSTRACT:
				enterOuterAlt(_localctx, 6);
				{
				setState(473);
				match(ABSTRACT);
				}
				break;
			case FINAL:
				enterOuterAlt(_localctx, 7);
				{
				setState(474);
				match(FINAL);
				}
				break;
			case STRICTFP:
				enterOuterAlt(_localctx, 8);
				{
				setState(475);
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
		enterRule(_localctx, 18, RULE_variableModifier);
		try {
			setState(480);
			switch (_input.LA(1)) {
			case FINAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(478);
				match(FINAL);
				}
				break;
			case AT:
				enterOuterAlt(_localctx, 2);
				{
				setState(479);
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
		enterRule(_localctx, 20, RULE_classDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			match(CLASS);
			setState(483);
			javaIdentifier();
			setState(485);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(484);
				typeParameters();
				}
			}

			setState(489);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(487);
				match(EXTENDS);
				setState(488);
				typeType();
				}
			}

			setState(493);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(491);
				match(IMPLEMENTS);
				setState(492);
				typeList();
				}
			}

			setState(495);
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
		public TerminalNode LT() { return getToken(TomJavaParser.LT, 0); }
		public List<TypeParameterContext> typeParameter() {
			return getRuleContexts(TypeParameterContext.class);
		}
		public TypeParameterContext typeParameter(int i) {
			return getRuleContext(TypeParameterContext.class,i);
		}
		public TerminalNode GT() { return getToken(TomJavaParser.GT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 22, RULE_typeParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
			match(LT);
			setState(498);
			typeParameter();
			setState(503);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(499);
				match(COMMA);
				setState(500);
				typeParameter();
				}
				}
				setState(505);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(506);
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
		enterRule(_localctx, 24, RULE_typeParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(511);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(508);
				annotation();
				}
				}
				setState(513);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(514);
			javaIdentifier();
			setState(517);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(515);
				match(EXTENDS);
				setState(516);
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
		public List<TerminalNode> BITAND() { return getTokens(TomJavaParser.BITAND); }
		public TerminalNode BITAND(int i) {
			return getToken(TomJavaParser.BITAND, i);
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
		enterRule(_localctx, 26, RULE_typeBound);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(519);
			typeType();
			setState(524);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BITAND) {
				{
				{
				setState(520);
				match(BITAND);
				setState(521);
				typeType();
				}
				}
				setState(526);
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
		public TerminalNode COMMA() { return getToken(TomJavaParser.COMMA, 0); }
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
		enterRule(_localctx, 28, RULE_enumDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(527);
			match(ENUM);
			setState(528);
			javaIdentifier();
			setState(531);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(529);
				match(IMPLEMENTS);
				setState(530);
				typeList();
				}
			}

			setState(533);
			match(LBRACE);
			setState(535);
			_la = _input.LA(1);
			if (((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (VISIT - 60)) | (1L << (IS_FSYM - 60)) | (1L << (IS_SORT - 60)) | (1L << (MAKE - 60)) | (1L << (MAKE_EMPTY - 60)) | (1L << (MAKE_APPEND - 60)) | (1L << (MAKE_INSERT - 60)) | (1L << (GET_SLOT - 60)) | (1L << (GET_DEFAULT - 60)) | (1L << (GET_ELEMENT - 60)) | (1L << (GET_HEAD - 60)) | (1L << (GET_TAIL - 60)) | (1L << (GET_SIZE - 60)) | (1L << (IS_EMPTY - 60)) | (1L << (IMPLEMENT - 60)) | (1L << (EQUALS - 60)) | (1L << (WHEN - 60)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				setState(534);
				enumConstants();
				}
			}

			setState(538);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(537);
				match(COMMA);
				}
			}

			setState(541);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(540);
				enumBodyDeclarations();
				}
			}

			setState(543);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 30, RULE_enumConstants);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			enumConstant();
			setState(550);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(546);
					match(COMMA);
					setState(547);
					enumConstant();
					}
					} 
				}
				setState(552);
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
		enterRule(_localctx, 32, RULE_enumConstant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(556);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(553);
				annotation();
				}
				}
				setState(558);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(559);
			javaIdentifier();
			setState(561);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(560);
				arguments();
				}
			}

			setState(564);
			_la = _input.LA(1);
			if (_la==LBRACE) {
				{
				setState(563);
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
		enterRule(_localctx, 34, RULE_enumBodyDeclarations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(566);
			match(SEMI);
			setState(570);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(567);
				classBodyDeclaration();
				}
				}
				setState(572);
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
		enterRule(_localctx, 36, RULE_interfaceDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(573);
			match(INTERFACE);
			setState(574);
			javaIdentifier();
			setState(576);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(575);
				typeParameters();
				}
			}

			setState(580);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(578);
				match(EXTENDS);
				setState(579);
				typeList();
				}
			}

			setState(582);
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
		enterRule(_localctx, 38, RULE_classBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(584);
			match(LBRACE);
			setState(588);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(585);
				classBodyDeclaration();
				}
				}
				setState(590);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(591);
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
		enterRule(_localctx, 40, RULE_interfaceBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(593);
			match(LBRACE);
			setState(597);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DEFAULT) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOID) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(594);
				interfaceBodyDeclaration();
				}
				}
				setState(599);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(600);
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
		enterRule(_localctx, 42, RULE_classBodyDeclaration);
		int _la;
		try {
			int _alt;
			setState(615);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(602);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(604);
				_la = _input.LA(1);
				if (_la==STATIC) {
					{
					setState(603);
					match(STATIC);
					}
				}

				setState(606);
				block();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(610);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(607);
						modifier();
						}
						} 
					}
					setState(612);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
				}
				setState(613);
				memberDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(614);
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
		enterRule(_localctx, 44, RULE_memberDeclaration);
		try {
			setState(626);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(617);
				methodDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(618);
				genericMethodDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(619);
				fieldDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(620);
				constructorDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(621);
				genericConstructorDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(622);
				interfaceDeclaration();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(623);
				annotationTypeDeclaration();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(624);
				classDeclaration();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(625);
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
		enterRule(_localctx, 46, RULE_methodDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(628);
			typeTypeOrVoid();
			setState(629);
			javaIdentifier();
			setState(630);
			formalParameters();
			setState(635);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(631);
				match(LBRACK);
				setState(632);
				match(RBRACK);
				}
				}
				setState(637);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(640);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(638);
				match(THROWS);
				setState(639);
				qualifiedNameList();
				}
			}

			setState(642);
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
		enterRule(_localctx, 48, RULE_methodBody);
		try {
			setState(646);
			switch (_input.LA(1)) {
			case LBRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(644);
				block();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(645);
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
		enterRule(_localctx, 50, RULE_typeTypeOrVoid);
		try {
			setState(650);
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
				setState(648);
				typeType();
				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 2);
				{
				setState(649);
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
		enterRule(_localctx, 52, RULE_genericMethodDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(652);
			typeParameters();
			setState(653);
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
		enterRule(_localctx, 54, RULE_genericConstructorDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(655);
			typeParameters();
			setState(656);
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
		enterRule(_localctx, 56, RULE_constructorDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(658);
			javaIdentifier();
			setState(659);
			formalParameters();
			setState(662);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(660);
				match(THROWS);
				setState(661);
				qualifiedNameList();
				}
			}

			setState(664);
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
		enterRule(_localctx, 58, RULE_fieldDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(666);
			typeType();
			setState(667);
			variableDeclarators();
			setState(668);
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
		enterRule(_localctx, 60, RULE_interfaceBodyDeclaration);
		try {
			int _alt;
			setState(679);
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
				setState(673);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(670);
						modifier();
						}
						} 
					}
					setState(675);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
				}
				setState(676);
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
				setState(677);
				tomDeclaration();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 3);
				{
				setState(678);
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
		enterRule(_localctx, 62, RULE_interfaceMemberDeclaration);
		try {
			setState(688);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(681);
				constDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(682);
				interfaceMethodDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(683);
				genericInterfaceMethodDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(684);
				interfaceDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(685);
				annotationTypeDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(686);
				classDeclaration();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(687);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 64, RULE_constDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(690);
			typeType();
			setState(691);
			constantDeclarator();
			setState(696);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(692);
				match(COMMA);
				setState(693);
				constantDeclarator();
				}
				}
				setState(698);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(699);
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
		public TerminalNode ASSIGN() { return getToken(TomJavaParser.ASSIGN, 0); }
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
		enterRule(_localctx, 66, RULE_constantDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(701);
			javaIdentifier();
			setState(706);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(702);
				match(LBRACK);
				setState(703);
				match(RBRACK);
				}
				}
				setState(708);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(709);
			match(ASSIGN);
			setState(710);
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
		enterRule(_localctx, 68, RULE_interfaceMethodDeclaration);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(715);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(712);
					interfaceMethodModifier();
					}
					} 
				}
				setState(717);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			}
			setState(728);
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
				setState(718);
				typeTypeOrVoid();
				}
				break;
			case LT:
				{
				setState(719);
				typeParameters();
				setState(723);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(720);
						annotation();
						}
						} 
					}
					setState(725);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				}
				setState(726);
				typeTypeOrVoid();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(730);
			javaIdentifier();
			setState(731);
			formalParameters();
			setState(736);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(732);
				match(LBRACK);
				setState(733);
				match(RBRACK);
				}
				}
				setState(738);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(741);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(739);
				match(THROWS);
				setState(740);
				qualifiedNameList();
				}
			}

			setState(743);
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
		enterRule(_localctx, 70, RULE_interfaceMethodModifier);
		try {
			setState(751);
			switch (_input.LA(1)) {
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(745);
				annotation();
				}
				break;
			case PUBLIC:
				enterOuterAlt(_localctx, 2);
				{
				setState(746);
				match(PUBLIC);
				}
				break;
			case ABSTRACT:
				enterOuterAlt(_localctx, 3);
				{
				setState(747);
				match(ABSTRACT);
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 4);
				{
				setState(748);
				match(DEFAULT);
				}
				break;
			case STATIC:
				enterOuterAlt(_localctx, 5);
				{
				setState(749);
				match(STATIC);
				}
				break;
			case STRICTFP:
				enterOuterAlt(_localctx, 6);
				{
				setState(750);
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
		enterRule(_localctx, 72, RULE_genericInterfaceMethodDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(753);
			typeParameters();
			setState(754);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 74, RULE_variableDeclarators);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(756);
			variableDeclarator();
			setState(761);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(757);
				match(COMMA);
				setState(758);
				variableDeclarator();
				}
				}
				setState(763);
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
		public TerminalNode ASSIGN() { return getToken(TomJavaParser.ASSIGN, 0); }
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
		enterRule(_localctx, 76, RULE_variableDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(764);
			variableDeclaratorId();
			setState(767);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(765);
				match(ASSIGN);
				setState(766);
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
		enterRule(_localctx, 78, RULE_variableDeclaratorId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(769);
			javaIdentifier();
			setState(774);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(770);
				match(LBRACK);
				setState(771);
				match(RBRACK);
				}
				}
				setState(776);
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
		enterRule(_localctx, 80, RULE_variableInitializer);
		try {
			setState(779);
			switch (_input.LA(1)) {
			case LBRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(777);
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
				setState(778);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 82, RULE_arrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(781);
			match(LBRACE);
			setState(793);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LBRACE - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
				{
				setState(782);
				variableInitializer();
				setState(787);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(783);
						match(COMMA);
						setState(784);
						variableInitializer();
						}
						} 
					}
					setState(789);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
				}
				setState(791);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(790);
					match(COMMA);
					}
				}

				}
			}

			setState(795);
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
		public List<TerminalNode> DOT() { return getTokens(TomJavaParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(TomJavaParser.DOT, i);
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
		enterRule(_localctx, 84, RULE_classOrInterfaceType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(797);
			javaIdentifier();
			setState(799);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(798);
				typeArguments();
				}
				break;
			}
			setState(808);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(801);
					match(DOT);
					setState(802);
					javaIdentifier();
					setState(804);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
					case 1:
						{
						setState(803);
						typeArguments();
						}
						break;
					}
					}
					} 
				}
				setState(810);
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
		enterRule(_localctx, 86, RULE_typeArgument);
		int _la;
		try {
			setState(817);
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
				setState(811);
				typeType();
				}
				break;
			case QMARK:
				enterOuterAlt(_localctx, 2);
				{
				setState(812);
				match(QMARK);
				setState(815);
				_la = _input.LA(1);
				if (_la==EXTENDS || _la==SUPER) {
					{
					setState(813);
					_la = _input.LA(1);
					if ( !(_la==EXTENDS || _la==SUPER) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(814);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 88, RULE_qualifiedNameList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(819);
			qualifiedName();
			setState(824);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(820);
				match(COMMA);
				setState(821);
				qualifiedName();
				}
				}
				setState(826);
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
		enterRule(_localctx, 90, RULE_formalParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(827);
			match(LPAREN);
			setState(829);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << SHORT) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				setState(828);
				formalParameterList();
				}
			}

			setState(831);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 92, RULE_formalParameterList);
		int _la;
		try {
			int _alt;
			setState(846);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(833);
				formalParameter();
				setState(838);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(834);
						match(COMMA);
						setState(835);
						formalParameter();
						}
						} 
					}
					setState(840);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
				}
				setState(843);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(841);
					match(COMMA);
					setState(842);
					lastFormalParameter();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(845);
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
		enterRule(_localctx, 94, RULE_formalParameter);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(851);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(848);
					variableModifier();
					}
					} 
				}
				setState(853);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,70,_ctx);
			}
			setState(854);
			typeType();
			setState(855);
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
		enterRule(_localctx, 96, RULE_lastFormalParameter);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(860);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(857);
					variableModifier();
					}
					} 
				}
				setState(862);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			}
			setState(863);
			typeType();
			setState(864);
			match(ELLIPSIS);
			setState(865);
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
		public List<TerminalNode> DOT() { return getTokens(TomJavaParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(TomJavaParser.DOT, i);
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
		enterRule(_localctx, 98, RULE_qualifiedName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(867);
			javaIdentifier();
			setState(872);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(868);
					match(DOT);
					setState(869);
					javaIdentifier();
					}
					} 
				}
				setState(874);
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
		enterRule(_localctx, 100, RULE_literal);
		try {
			setState(881);
			switch (_input.LA(1)) {
			case DECIMAL_LITERAL:
			case HEX_LITERAL:
			case OCT_LITERAL:
			case BINARY_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(875);
				integerLiteral();
				}
				break;
			case FLOAT_LITERAL:
			case HEX_FLOAT_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(876);
				floatLiteral();
				}
				break;
			case CHAR_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(877);
				match(CHAR_LITERAL);
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 4);
				{
				setState(878);
				match(STRING_LITERAL);
				}
				break;
			case BOOL_LITERAL:
				enterOuterAlt(_localctx, 5);
				{
				setState(879);
				match(BOOL_LITERAL);
				}
				break;
			case NULL_LITERAL:
				enterOuterAlt(_localctx, 6);
				{
				setState(880);
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
		enterRule(_localctx, 102, RULE_integerLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(883);
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
		enterRule(_localctx, 104, RULE_floatLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(885);
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
		public TerminalNode AT() { return getToken(TomJavaParser.AT, 0); }
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
		enterRule(_localctx, 106, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(887);
			match(AT);
			setState(888);
			qualifiedName();
			setState(895);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(889);
				match(LPAREN);
				setState(892);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
				case 1:
					{
					setState(890);
					elementValuePairs();
					}
					break;
				case 2:
					{
					setState(891);
					elementValue();
					}
					break;
				}
				setState(894);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 108, RULE_elementValuePairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(897);
			elementValuePair();
			setState(902);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(898);
				match(COMMA);
				setState(899);
				elementValuePair();
				}
				}
				setState(904);
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
		public TerminalNode ASSIGN() { return getToken(TomJavaParser.ASSIGN, 0); }
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
		enterRule(_localctx, 110, RULE_elementValuePair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(905);
			javaIdentifier();
			setState(906);
			match(ASSIGN);
			setState(907);
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
		enterRule(_localctx, 112, RULE_elementValue);
		try {
			setState(912);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(909);
				expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(910);
				annotation();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(911);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 114, RULE_elementValueArrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(914);
			match(LBRACE);
			setState(923);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LBRACE - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
				{
				setState(915);
				elementValue();
				setState(920);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(916);
						match(COMMA);
						setState(917);
						elementValue();
						}
						} 
					}
					setState(922);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
				}
				}
			}

			setState(926);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(925);
				match(COMMA);
				}
			}

			setState(928);
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
		public TerminalNode AT() { return getToken(TomJavaParser.AT, 0); }
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
		enterRule(_localctx, 116, RULE_annotationTypeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(930);
			match(AT);
			setState(931);
			match(INTERFACE);
			setState(932);
			javaIdentifier();
			setState(933);
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
		enterRule(_localctx, 118, RULE_annotationTypeBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(935);
			match(LBRACE);
			setState(939);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << DOUBLE) | (1L << ENUM) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SYNCHRONIZED) | (1L << TRANSIENT) | (1L << VOLATILE) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (SEMI - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
				{
				{
				setState(936);
				annotationTypeElementDeclaration();
				}
				}
				setState(941);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(942);
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
		enterRule(_localctx, 120, RULE_annotationTypeElementDeclaration);
		try {
			int _alt;
			setState(953);
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
				setState(947);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(944);
						modifier();
						}
						} 
					}
					setState(949);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
				}
				setState(950);
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
				setState(951);
				tomDeclaration();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 3);
				{
				setState(952);
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
		enterRule(_localctx, 122, RULE_annotationTypeElementRest);
		try {
			setState(975);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(955);
				typeType();
				setState(956);
				annotationMethodOrConstantRest();
				setState(957);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(959);
				classDeclaration();
				setState(961);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
				case 1:
					{
					setState(960);
					match(SEMI);
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(963);
				interfaceDeclaration();
				setState(965);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
				case 1:
					{
					setState(964);
					match(SEMI);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(967);
				enumDeclaration();
				setState(969);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
				case 1:
					{
					setState(968);
					match(SEMI);
					}
					break;
				}
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(971);
				annotationTypeDeclaration();
				setState(973);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
				case 1:
					{
					setState(972);
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
		enterRule(_localctx, 124, RULE_annotationMethodOrConstantRest);
		try {
			setState(979);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(977);
				annotationMethodRest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(978);
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
		enterRule(_localctx, 126, RULE_annotationMethodRest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(981);
			javaIdentifier();
			setState(982);
			match(LPAREN);
			setState(983);
			match(RPAREN);
			setState(985);
			_la = _input.LA(1);
			if (_la==DEFAULT) {
				{
				setState(984);
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
		enterRule(_localctx, 128, RULE_annotationConstantRest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(987);
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
		enterRule(_localctx, 130, RULE_defaultValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(989);
			match(DEFAULT);
			setState(990);
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
		enterRule(_localctx, 132, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(992);
			match(LBRACE);
			setState(996);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << CONTINUE) | (1L << DO) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NEW) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << TRY) | (1L << VOID) | (1L << WHILE) | (1L << MATCH) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
				{
				{
				setState(993);
				blockStatement();
				}
				}
				setState(998);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(999);
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
		enterRule(_localctx, 134, RULE_blockStatement);
		try {
			setState(1007);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1001);
				localVariableDeclaration();
				setState(1002);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1004);
				statement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1005);
				localTypeDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1006);
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
		enterRule(_localctx, 136, RULE_localVariableDeclaration);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1012);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1009);
					variableModifier();
					}
					} 
				}
				setState(1014);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
			}
			setState(1015);
			typeType();
			setState(1016);
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
		enterRule(_localctx, 138, RULE_localTypeDeclaration);
		int _la;
		try {
			setState(1029);
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
				setState(1021);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << FINAL) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << STATIC) | (1L << STRICTFP))) != 0) || _la==AT) {
					{
					{
					setState(1018);
					classOrInterfaceModifier();
					}
					}
					setState(1023);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1026);
				switch (_input.LA(1)) {
				case CLASS:
					{
					setState(1024);
					classDeclaration();
					}
					break;
				case INTERFACE:
					{
					setState(1025);
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
				setState(1028);
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
		public TerminalNode COLON() { return getToken(TomJavaParser.COLON, 0); }
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
		enterRule(_localctx, 140, RULE_statement);
		int _la;
		try {
			int _alt;
			setState(1137);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1031);
				((StatementContext)_localctx).blockLabel = block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1032);
				match(ASSERT);
				setState(1033);
				expression(0);
				setState(1036);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(1034);
					match(COLON);
					setState(1035);
					expression(0);
					}
				}

				setState(1038);
				match(SEMI);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1040);
				match(IF);
				setState(1041);
				parExpression();
				setState(1042);
				statement();
				setState(1045);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
				case 1:
					{
					setState(1043);
					match(ELSE);
					setState(1044);
					statement();
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1047);
				match(FOR);
				setState(1048);
				match(LPAREN);
				setState(1049);
				forControl();
				setState(1050);
				match(RPAREN);
				setState(1051);
				statement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1053);
				match(WHILE);
				setState(1054);
				parExpression();
				setState(1055);
				statement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1057);
				match(DO);
				setState(1058);
				statement();
				setState(1059);
				match(WHILE);
				setState(1060);
				parExpression();
				setState(1061);
				match(SEMI);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1063);
				match(TRY);
				setState(1064);
				block();
				setState(1074);
				switch (_input.LA(1)) {
				case CATCH:
					{
					setState(1066); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(1065);
						catchClause();
						}
						}
						setState(1068); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==CATCH );
					setState(1071);
					_la = _input.LA(1);
					if (_la==FINALLY) {
						{
						setState(1070);
						finallyBlock();
						}
					}

					}
					break;
				case FINALLY:
					{
					setState(1073);
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
				setState(1076);
				match(TRY);
				setState(1077);
				resourceSpecification();
				setState(1078);
				block();
				setState(1082);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CATCH) {
					{
					{
					setState(1079);
					catchClause();
					}
					}
					setState(1084);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1086);
				_la = _input.LA(1);
				if (_la==FINALLY) {
					{
					setState(1085);
					finallyBlock();
					}
				}

				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1088);
				match(SWITCH);
				setState(1089);
				parExpression();
				setState(1090);
				match(LBRACE);
				setState(1094);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1091);
						switchBlockStatementGroup();
						}
						} 
					}
					setState(1096);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
				}
				setState(1100);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CASE || _la==DEFAULT) {
					{
					{
					setState(1097);
					switchLabel();
					}
					}
					setState(1102);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1103);
				match(RBRACE);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1105);
				match(SYNCHRONIZED);
				setState(1106);
				parExpression();
				setState(1107);
				block();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1109);
				match(RETURN);
				setState(1111);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
					{
					setState(1110);
					expression(0);
					}
				}

				setState(1113);
				match(SEMI);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1114);
				match(THROW);
				setState(1115);
				expression(0);
				setState(1116);
				match(SEMI);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1118);
				match(BREAK);
				setState(1120);
				_la = _input.LA(1);
				if (((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (VISIT - 60)) | (1L << (IS_FSYM - 60)) | (1L << (IS_SORT - 60)) | (1L << (MAKE - 60)) | (1L << (MAKE_EMPTY - 60)) | (1L << (MAKE_APPEND - 60)) | (1L << (MAKE_INSERT - 60)) | (1L << (GET_SLOT - 60)) | (1L << (GET_DEFAULT - 60)) | (1L << (GET_ELEMENT - 60)) | (1L << (GET_HEAD - 60)) | (1L << (GET_TAIL - 60)) | (1L << (GET_SIZE - 60)) | (1L << (IS_EMPTY - 60)) | (1L << (IMPLEMENT - 60)) | (1L << (EQUALS - 60)) | (1L << (WHEN - 60)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1119);
					javaIdentifier();
					}
				}

				setState(1122);
				match(SEMI);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1123);
				match(CONTINUE);
				setState(1125);
				_la = _input.LA(1);
				if (((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (VISIT - 60)) | (1L << (IS_FSYM - 60)) | (1L << (IS_SORT - 60)) | (1L << (MAKE - 60)) | (1L << (MAKE_EMPTY - 60)) | (1L << (MAKE_APPEND - 60)) | (1L << (MAKE_INSERT - 60)) | (1L << (GET_SLOT - 60)) | (1L << (GET_DEFAULT - 60)) | (1L << (GET_ELEMENT - 60)) | (1L << (GET_HEAD - 60)) | (1L << (GET_TAIL - 60)) | (1L << (GET_SIZE - 60)) | (1L << (IS_EMPTY - 60)) | (1L << (IMPLEMENT - 60)) | (1L << (EQUALS - 60)) | (1L << (WHEN - 60)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1124);
					javaIdentifier();
					}
				}

				setState(1127);
				match(SEMI);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1128);
				match(SEMI);
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(1129);
				((StatementContext)_localctx).statementExpression = expression(0);
				setState(1130);
				match(SEMI);
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(1132);
				((StatementContext)_localctx).identifierLabel = javaIdentifier();
				setState(1133);
				match(COLON);
				setState(1134);
				statement();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(1136);
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
		enterRule(_localctx, 142, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1139);
			match(CATCH);
			setState(1140);
			match(LPAREN);
			setState(1144);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(1141);
				variableModifier();
				}
				}
				setState(1146);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1147);
			catchType();
			setState(1148);
			javaIdentifier();
			setState(1149);
			match(RPAREN);
			setState(1150);
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
		public List<TerminalNode> PIPE() { return getTokens(TomJavaParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(TomJavaParser.PIPE, i);
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
		enterRule(_localctx, 144, RULE_catchType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1152);
			qualifiedName();
			setState(1157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PIPE) {
				{
				{
				setState(1153);
				match(PIPE);
				setState(1154);
				qualifiedName();
				}
				}
				setState(1159);
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
		enterRule(_localctx, 146, RULE_finallyBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1160);
			match(FINALLY);
			setState(1161);
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
		enterRule(_localctx, 148, RULE_resourceSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1163);
			match(LPAREN);
			setState(1164);
			resources();
			setState(1166);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(1165);
				match(SEMI);
				}
			}

			setState(1168);
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
		enterRule(_localctx, 150, RULE_resources);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1170);
			resource();
			setState(1175);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,113,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1171);
					match(SEMI);
					setState(1172);
					resource();
					}
					} 
				}
				setState(1177);
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
		public TerminalNode ASSIGN() { return getToken(TomJavaParser.ASSIGN, 0); }
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
		enterRule(_localctx, 152, RULE_resource);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1181);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(1178);
				variableModifier();
				}
				}
				setState(1183);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1184);
			classOrInterfaceType();
			setState(1185);
			variableDeclaratorId();
			setState(1186);
			match(ASSIGN);
			setState(1187);
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
		enterRule(_localctx, 154, RULE_switchBlockStatementGroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1190); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1189);
				switchLabel();
				}
				}
				setState(1192); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CASE || _la==DEFAULT );
			setState(1195); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1194);
				blockStatement();
				}
				}
				setState(1197); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CHAR) | (1L << CLASS) | (1L << CONTINUE) | (1L << DO) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NEW) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << TRY) | (1L << VOID) | (1L << WHILE) | (1L << MATCH) | (1L << STRATEGY) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << RULE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LBRACE - 64)) | (1L << (SEMI - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0) );
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
		public TerminalNode COLON() { return getToken(TomJavaParser.COLON, 0); }
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
		enterRule(_localctx, 156, RULE_switchLabel);
		try {
			setState(1208);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1199);
				match(CASE);
				setState(1202);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
				case 1:
					{
					setState(1200);
					((SwitchLabelContext)_localctx).constantExpression = expression(0);
					}
					break;
				case 2:
					{
					setState(1201);
					((SwitchLabelContext)_localctx).enumConstantName = javaIdentifier();
					}
					break;
				}
				setState(1204);
				match(COLON);
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1206);
				match(DEFAULT);
				setState(1207);
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
		enterRule(_localctx, 158, RULE_forControl);
		int _la;
		try {
			setState(1222);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1210);
				enhancedForControl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1212);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
					{
					setState(1211);
					forInit();
					}
				}

				setState(1214);
				match(SEMI);
				setState(1216);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
					{
					setState(1215);
					expression(0);
					}
				}

				setState(1218);
				match(SEMI);
				setState(1220);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
					{
					setState(1219);
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
		enterRule(_localctx, 160, RULE_forInit);
		try {
			setState(1226);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1224);
				localVariableDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1225);
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
		public TerminalNode COLON() { return getToken(TomJavaParser.COLON, 0); }
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
		enterRule(_localctx, 162, RULE_enhancedForControl);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1231);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,124,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1228);
					variableModifier();
					}
					} 
				}
				setState(1233);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,124,_ctx);
			}
			setState(1234);
			typeType();
			setState(1235);
			variableDeclaratorId();
			setState(1236);
			match(COLON);
			setState(1237);
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
		enterRule(_localctx, 164, RULE_parExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1239);
			match(LPAREN);
			setState(1240);
			expression(0);
			setState(1241);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 166, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1243);
			expression(0);
			setState(1248);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1244);
				match(COMMA);
				setState(1245);
				expression(0);
				}
				}
				setState(1250);
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
		public TerminalNode ANTI() { return getToken(TomJavaParser.ANTI, 0); }
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
		public TerminalNode STAR() { return getToken(TomJavaParser.STAR, 0); }
		public TerminalNode LSHIFT() { return getToken(TomJavaParser.LSHIFT, 0); }
		public List<TerminalNode> GT() { return getTokens(TomJavaParser.GT); }
		public TerminalNode GT(int i) {
			return getToken(TomJavaParser.GT, i);
		}
		public TerminalNode LE() { return getToken(TomJavaParser.LE, 0); }
		public TerminalNode GE() { return getToken(TomJavaParser.GE, 0); }
		public TerminalNode LT() { return getToken(TomJavaParser.LT, 0); }
		public TerminalNode EQUAL() { return getToken(TomJavaParser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(TomJavaParser.NOTEQUAL, 0); }
		public TerminalNode BITAND() { return getToken(TomJavaParser.BITAND, 0); }
		public TerminalNode PIPE() { return getToken(TomJavaParser.PIPE, 0); }
		public TerminalNode AND() { return getToken(TomJavaParser.AND, 0); }
		public TerminalNode OR() { return getToken(TomJavaParser.OR, 0); }
		public TerminalNode COLON() { return getToken(TomJavaParser.COLON, 0); }
		public TerminalNode ASSIGN() { return getToken(TomJavaParser.ASSIGN, 0); }
		public TerminalNode DOT() { return getToken(TomJavaParser.DOT, 0); }
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
		int _startState = 168;
		enterRecursionRule(_localctx, 168, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1289);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1252);
				primary();
				}
				break;
			case 2:
				{
				setState(1253);
				funTerm(0);
				setState(1254);
				match(LPAREN);
				setState(1256);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
					{
					setState(1255);
					expressionList();
					}
				}

				setState(1258);
				match(RPAREN);
				}
				break;
			case 3:
				{
				setState(1260);
				match(NEW);
				setState(1261);
				creator();
				}
				break;
			case 4:
				{
				setState(1262);
				match(LPAREN);
				setState(1263);
				typeType();
				setState(1264);
				match(RPAREN);
				setState(1265);
				expression(22);
				}
				break;
			case 5:
				{
				setState(1267);
				((ExpressionContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 108)) & ~0x3f) == 0 && ((1L << (_la - 108)) & ((1L << (INC - 108)) | (1L << (DEC - 108)) | (1L << (ADD - 108)) | (1L << (SUB - 108)))) != 0)) ) {
					((ExpressionContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1268);
				expression(20);
				}
				break;
			case 6:
				{
				setState(1269);
				((ExpressionContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TILDE || _la==ANTI) ) {
					((ExpressionContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1270);
				expression(19);
				}
				break;
			case 7:
				{
				setState(1271);
				lambdaExpression();
				}
				break;
			case 8:
				{
				setState(1272);
				typeType();
				setState(1273);
				match(COLONCOLON);
				setState(1279);
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
					setState(1275);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(1274);
						typeArguments();
						}
					}

					setState(1277);
					javaIdentifier();
					}
					break;
				case NEW:
					{
					setState(1278);
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
				setState(1281);
				classType();
				setState(1282);
				match(COLONCOLON);
				setState(1284);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1283);
					typeArguments();
					}
				}

				setState(1286);
				match(NEW);
				}
				break;
			case 10:
				{
				setState(1288);
				tomTerm();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1383);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,139,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1381);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1291);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(1292);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & ((1L << (MOD - 114)) | (1L << (STAR - 114)) | (1L << (SLASH - 114)))) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1293);
						expression(19);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1294);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(1295);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1296);
						expression(18);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1297);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(1304);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
						case 1:
							{
							setState(1298);
							match(LSHIFT);
							}
							break;
						case 2:
							{
							setState(1299);
							match(GT);
							setState(1300);
							match(GT);
							setState(1301);
							match(GT);
							}
							break;
						case 3:
							{
							setState(1302);
							match(GT);
							setState(1303);
							match(GT);
							}
							break;
						}
						setState(1306);
						expression(17);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1307);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(1308);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 98)) & ~0x3f) == 0 && ((1L << (_la - 98)) & ((1L << (GT - 98)) | (1L << (LT - 98)) | (1L << (LE - 98)) | (1L << (GE - 98)))) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1309);
						expression(16);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1310);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(1311);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQUAL || _la==NOTEQUAL) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1312);
						expression(14);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1313);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(1314);
						((ExpressionContext)_localctx).bop = match(BITAND);
						setState(1315);
						expression(13);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1316);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(1317);
						((ExpressionContext)_localctx).bop = match(CARET);
						setState(1318);
						expression(12);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1319);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1320);
						((ExpressionContext)_localctx).bop = match(PIPE);
						setState(1321);
						expression(11);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1322);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1323);
						((ExpressionContext)_localctx).bop = match(AND);
						setState(1324);
						expression(10);
						}
						break;
					case 10:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1325);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1326);
						((ExpressionContext)_localctx).bop = match(OR);
						setState(1327);
						expression(9);
						}
						break;
					case 11:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1328);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1329);
						((ExpressionContext)_localctx).bop = match(QMARK);
						setState(1330);
						expression(0);
						setState(1331);
						match(COLON);
						setState(1332);
						expression(8);
						}
						break;
					case 12:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1334);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1335);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (ASSIGN - 97)) | (1L << (ADD_ASSIGN - 97)) | (1L << (SUB_ASSIGN - 97)) | (1L << (MUL_ASSIGN - 97)) | (1L << (DIV_ASSIGN - 97)) | (1L << (AND_ASSIGN - 97)) | (1L << (OR_ASSIGN - 97)) | (1L << (XOR_ASSIGN - 97)) | (1L << (MOD_ASSIGN - 97)) | (1L << (LSHIFT_ASSIGN - 97)) | (1L << (RSHIFT_ASSIGN - 97)) | (1L << (URSHIFT_ASSIGN - 97)))) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(1336);
						expression(6);
						}
						break;
					case 13:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1337);
						if (!(precpred(_ctx, 26))) throw new FailedPredicateException(this, "precpred(_ctx, 26)");
						setState(1338);
						((ExpressionContext)_localctx).bop = match(DOT);
						setState(1356);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
						case 1:
							{
							setState(1339);
							javaIdentifier();
							}
							break;
						case 2:
							{
							setState(1340);
							match(THIS);
							}
							break;
						case 3:
							{
							setState(1341);
							match(NEW);
							setState(1343);
							_la = _input.LA(1);
							if (_la==LT) {
								{
								setState(1342);
								nonWildcardTypeArguments();
								}
							}

							setState(1345);
							innerCreator();
							}
							break;
						case 4:
							{
							setState(1346);
							match(SUPER);
							setState(1347);
							superSuffix();
							}
							break;
						case 5:
							{
							setState(1348);
							explicitGenericInvocation();
							}
							break;
						case 6:
							{
							setState(1349);
							funTerm(0);
							setState(1350);
							match(LPAREN);
							setState(1352);
							_la = _input.LA(1);
							if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
								{
								setState(1351);
								expressionList();
								}
							}

							setState(1354);
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
						setState(1358);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(1359);
						match(LBRACK);
						setState(1360);
						expression(0);
						setState(1361);
						match(RBRACK);
						}
						break;
					case 15:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1363);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(1364);
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
						setState(1365);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(1366);
						((ExpressionContext)_localctx).bop = match(INSTANCEOF);
						setState(1367);
						typeType();
						}
						break;
					case 17:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1368);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1369);
						match(COLONCOLON);
						setState(1371);
						_la = _input.LA(1);
						if (_la==LT) {
							{
							setState(1370);
							typeArguments();
							}
						}

						setState(1373);
						javaIdentifier();
						setState(1379);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
						case 1:
							{
							setState(1374);
							match(LPAREN);
							setState(1376);
							_la = _input.LA(1);
							if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
								{
								setState(1375);
								expressionList();
								}
							}

							setState(1378);
							match(RPAREN);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(1385);
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
		int _startState = 170;
		enterRecursionRule(_localctx, 170, RULE_funTerm, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1415);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
			case 1:
				{
				setState(1387);
				match(THIS);
				}
				break;
			case 2:
				{
				setState(1388);
				match(SUPER);
				}
				break;
			case 3:
				{
				setState(1389);
				javaIdentifier();
				}
				break;
			case 4:
				{
				setState(1390);
				nonWildcardTypeArguments();
				setState(1394);
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
					setState(1391);
					explicitGenericInvocationSuffix();
					}
					break;
				case THIS:
					{
					setState(1392);
					match(THIS);
					setState(1393);
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
				setState(1396);
				match(NEW);
				setState(1397);
				creator();
				}
				break;
			case 6:
				{
				setState(1398);
				lambdaExpression();
				}
				break;
			case 7:
				{
				setState(1399);
				typeType();
				setState(1400);
				match(COLONCOLON);
				setState(1406);
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
					setState(1402);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(1401);
						typeArguments();
						}
					}

					setState(1404);
					javaIdentifier();
					}
					break;
				case NEW:
					{
					setState(1405);
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
				setState(1408);
				classType();
				setState(1409);
				match(COLONCOLON);
				setState(1411);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1410);
					typeArguments();
					}
				}

				setState(1413);
				match(NEW);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1432);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,147,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1430);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,146,_ctx) ) {
					case 1:
						{
						_localctx = new FunTermContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_funTerm);
						setState(1417);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1418);
						match(LBRACK);
						setState(1419);
						expression(0);
						setState(1420);
						match(RBRACK);
						}
						break;
					case 2:
						{
						_localctx = new FunTermContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_funTerm);
						setState(1422);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1423);
						match(LPAREN);
						setState(1425);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
							{
							setState(1424);
							expressionList();
							}
						}

						setState(1427);
						match(RPAREN);
						}
						break;
					case 3:
						{
						_localctx = new FunTermContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_funTerm);
						setState(1428);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1429);
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
				setState(1434);
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
		public TerminalNode ARROW() { return getToken(TomJavaParser.ARROW, 0); }
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
		enterRule(_localctx, 172, RULE_lambdaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1435);
			lambdaParameters();
			setState(1436);
			match(ARROW);
			setState(1437);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 174, RULE_lambdaParameters);
		int _la;
		try {
			setState(1456);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1439);
				javaIdentifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1440);
				match(LPAREN);
				setState(1442);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << SHORT) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)))) != 0) || _la==AT || _la==IDENTIFIER) {
					{
					setState(1441);
					formalParameterList();
					}
				}

				setState(1444);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1445);
				match(LPAREN);
				setState(1446);
				javaIdentifier();
				setState(1451);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1447);
					match(COMMA);
					setState(1448);
					javaIdentifier();
					}
					}
					setState(1453);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1454);
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
		enterRule(_localctx, 176, RULE_lambdaBody);
		try {
			setState(1460);
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
				setState(1458);
				expression(0);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1459);
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
		public TerminalNode DOT() { return getToken(TomJavaParser.DOT, 0); }
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
		enterRule(_localctx, 178, RULE_primary);
		try {
			setState(1480);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1462);
				match(LPAREN);
				setState(1463);
				expression(0);
				setState(1464);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1466);
				match(THIS);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1467);
				match(SUPER);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1468);
				literal();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1469);
				javaIdentifier();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1470);
				typeTypeOrVoid();
				setState(1471);
				match(DOT);
				setState(1472);
				match(CLASS);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1474);
				nonWildcardTypeArguments();
				setState(1478);
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
					setState(1475);
					explicitGenericInvocationSuffix();
					}
					break;
				case THIS:
					{
					setState(1476);
					match(THIS);
					setState(1477);
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
		public TerminalNode DOT() { return getToken(TomJavaParser.DOT, 0); }
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
		enterRule(_localctx, 180, RULE_classType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1485);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
			case 1:
				{
				setState(1482);
				classOrInterfaceType();
				setState(1483);
				match(DOT);
				}
				break;
			}
			setState(1490);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(1487);
				annotation();
				}
				}
				setState(1492);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1493);
			javaIdentifier();
			setState(1495);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(1494);
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
		enterRule(_localctx, 182, RULE_creator);
		try {
			setState(1506);
			switch (_input.LA(1)) {
			case LT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1497);
				nonWildcardTypeArguments();
				setState(1498);
				createdName();
				setState(1499);
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
				setState(1501);
				createdName();
				setState(1504);
				switch (_input.LA(1)) {
				case LBRACK:
					{
					setState(1502);
					arrayCreatorRest();
					}
					break;
				case LPAREN:
					{
					setState(1503);
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
		public List<TerminalNode> DOT() { return getTokens(TomJavaParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(TomJavaParser.DOT, i);
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
		enterRule(_localctx, 184, RULE_createdName);
		int _la;
		try {
			setState(1523);
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
				setState(1508);
				javaIdentifier();
				setState(1510);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1509);
					typeArgumentsOrDiamond();
					}
				}

				setState(1519);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DOT) {
					{
					{
					setState(1512);
					match(DOT);
					setState(1513);
					javaIdentifier();
					setState(1515);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(1514);
						typeArgumentsOrDiamond();
						}
					}

					}
					}
					setState(1521);
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
				setState(1522);
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
		enterRule(_localctx, 186, RULE_innerCreator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1525);
			javaIdentifier();
			setState(1527);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(1526);
				nonWildcardTypeArgumentsOrDiamond();
				}
			}

			setState(1529);
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
		enterRule(_localctx, 188, RULE_arrayCreatorRest);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1531);
			match(LBRACK);
			setState(1559);
			switch (_input.LA(1)) {
			case RBRACK:
				{
				setState(1532);
				match(RBRACK);
				setState(1537);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LBRACK) {
					{
					{
					setState(1533);
					match(LBRACK);
					setState(1534);
					match(RBRACK);
					}
					}
					setState(1539);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1540);
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
				setState(1541);
				expression(0);
				setState(1542);
				match(RBRACK);
				setState(1549);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,165,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1543);
						match(LBRACK);
						setState(1544);
						expression(0);
						setState(1545);
						match(RBRACK);
						}
						} 
					}
					setState(1551);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,165,_ctx);
				}
				setState(1556);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,166,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1552);
						match(LBRACK);
						setState(1553);
						match(RBRACK);
						}
						} 
					}
					setState(1558);
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
		enterRule(_localctx, 190, RULE_classCreatorRest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1561);
			arguments();
			setState(1563);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,168,_ctx) ) {
			case 1:
				{
				setState(1562);
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
		enterRule(_localctx, 192, RULE_explicitGenericInvocation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1565);
			nonWildcardTypeArguments();
			setState(1566);
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
		public TerminalNode LT() { return getToken(TomJavaParser.LT, 0); }
		public TerminalNode GT() { return getToken(TomJavaParser.GT, 0); }
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
		enterRule(_localctx, 194, RULE_typeArgumentsOrDiamond);
		try {
			setState(1571);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,169,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1568);
				match(LT);
				setState(1569);
				match(GT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1570);
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
		public TerminalNode LT() { return getToken(TomJavaParser.LT, 0); }
		public TerminalNode GT() { return getToken(TomJavaParser.GT, 0); }
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
		enterRule(_localctx, 196, RULE_nonWildcardTypeArgumentsOrDiamond);
		try {
			setState(1576);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,170,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1573);
				match(LT);
				setState(1574);
				match(GT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1575);
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
		public TerminalNode LT() { return getToken(TomJavaParser.LT, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public TerminalNode GT() { return getToken(TomJavaParser.GT, 0); }
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
		enterRule(_localctx, 198, RULE_nonWildcardTypeArguments);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1578);
			match(LT);
			setState(1579);
			typeList();
			setState(1580);
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
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 200, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1582);
			typeType();
			setState(1587);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1583);
				match(COMMA);
				setState(1584);
				typeType();
				}
				}
				setState(1589);
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
		enterRule(_localctx, 202, RULE_typeType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1591);
			_la = _input.LA(1);
			if (_la==AT) {
				{
				setState(1590);
				annotation();
				}
			}

			setState(1595);
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
				setState(1593);
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
				setState(1594);
				primitiveType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1601);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,174,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1597);
					match(LBRACK);
					setState(1598);
					match(RBRACK);
					}
					} 
				}
				setState(1603);
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
		enterRule(_localctx, 204, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1604);
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
		public TerminalNode LT() { return getToken(TomJavaParser.LT, 0); }
		public List<TypeArgumentContext> typeArgument() {
			return getRuleContexts(TypeArgumentContext.class);
		}
		public TypeArgumentContext typeArgument(int i) {
			return getRuleContext(TypeArgumentContext.class,i);
		}
		public TerminalNode GT() { return getToken(TomJavaParser.GT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(TomJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomJavaParser.COMMA, i);
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
		enterRule(_localctx, 206, RULE_typeArguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1606);
			match(LT);
			setState(1607);
			typeArgument();
			setState(1612);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1608);
				match(COMMA);
				setState(1609);
				typeArgument();
				}
				}
				setState(1614);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1615);
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
		public TerminalNode DOT() { return getToken(TomJavaParser.DOT, 0); }
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
		enterRule(_localctx, 208, RULE_superSuffix);
		try {
			setState(1623);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(1617);
				arguments();
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1618);
				match(DOT);
				setState(1619);
				javaIdentifier();
				setState(1621);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,176,_ctx) ) {
				case 1:
					{
					setState(1620);
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
		enterRule(_localctx, 210, RULE_explicitGenericInvocationSuffix);
		try {
			setState(1630);
			switch (_input.LA(1)) {
			case SUPER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1625);
				match(SUPER);
				setState(1626);
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
				setState(1627);
				javaIdentifier();
				setState(1628);
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
		enterRule(_localctx, 212, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1632);
			match(LPAREN);
			setState(1634);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW) | (1L << SHORT) | (1L << SUPER) | (1L << THIS) | (1L << VOID) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (HEX_LITERAL - 64)) | (1L << (OCT_LITERAL - 64)) | (1L << (BINARY_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (HEX_FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (LT - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (ANTI - 129)) | (1L << (BQUOTE - 129)) | (1L << (METAQUOTE - 129)) | (1L << (AT - 129)) | (1L << (IDENTIFIER - 129)))) != 0)) {
				{
				setState(1633);
				expressionList();
				}
			}

			setState(1636);
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
		enterRule(_localctx, 214, RULE_javaIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1638);
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
		enterRule(_localctx, 216, RULE_tomDeclaration);
		try {
			setState(1648);
			switch (_input.LA(1)) {
			case STRATEGY:
				enterOuterAlt(_localctx, 1);
				{
				setState(1640);
				strategyStatement();
				}
				break;
			case INCLUDE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1641);
				includeStatement();
				}
				break;
			case GOM:
				enterOuterAlt(_localctx, 3);
				{
				setState(1642);
				gomStatement();
				}
				break;
			case RULE:
				enterOuterAlt(_localctx, 4);
				{
				setState(1643);
				ruleStatement();
				}
				break;
			case TYPETERM:
				enterOuterAlt(_localctx, 5);
				{
				setState(1644);
				typeterm();
				}
				break;
			case OP:
				enterOuterAlt(_localctx, 6);
				{
				setState(1645);
				operator();
				}
				break;
			case OPLIST:
				enterOuterAlt(_localctx, 7);
				{
				setState(1646);
				oplist();
				}
				break;
			case OPARRAY:
				enterOuterAlt(_localctx, 8);
				{
				setState(1647);
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
		enterRule(_localctx, 218, RULE_tomStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1650);
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
		enterRule(_localctx, 220, RULE_tomTerm);
		try {
			setState(1654);
			switch (_input.LA(1)) {
			case BQUOTE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1652);
				bqcomposite();
				}
				break;
			case METAQUOTE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1653);
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
		enterRule(_localctx, 222, RULE_metaquote);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1656);
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
		enterRule(_localctx, 224, RULE_matchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1658);
			match(MATCH);
			setState(1671);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(1659);
				match(LPAREN);
				setState(1668);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (BQUOTE - 77)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1660);
					bqterm();
					setState(1665);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1661);
						match(COMMA);
						setState(1662);
						bqterm();
						}
						}
						setState(1667);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1670);
				match(RPAREN);
				}
			}

			setState(1673);
			match(LBRACE);
			setState(1677);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (LPAREN - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (ANTI - 77)))) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(1674);
				actionRule();
				}
				}
				setState(1679);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1680);
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
		enterRule(_localctx, 226, RULE_strategyStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1682);
			match(STRATEGY);
			setState(1683);
			tomIdentifier();
			setState(1684);
			match(LPAREN);
			setState(1686);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
				{
				setState(1685);
				slotList();
				}
			}

			setState(1688);
			match(RPAREN);
			setState(1689);
			match(EXTENDS);
			setState(1690);
			bqterm();
			setState(1691);
			match(LBRACE);
			setState(1695);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VISIT) {
				{
				{
				setState(1692);
				visit();
				}
				}
				setState(1697);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1698);
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
		enterRule(_localctx, 228, RULE_includeStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1700);
			match(INCLUDE);
			setState(1701);
			match(LBRACE);
			setState(1711);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT || _la==SLASH) {
				{
				{
				setState(1705);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DOT) {
					{
					{
					setState(1702);
					match(DOT);
					}
					}
					setState(1707);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1708);
				match(SLASH);
				}
				}
				setState(1713);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1714);
			tomIdentifier();
			setState(1719);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (DOT - 96)) | (1L << (SLASH - 96)) | (1L << (BACKSLASH - 96)))) != 0)) {
				{
				{
				setState(1715);
				_la = _input.LA(1);
				if ( !(((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (DOT - 96)) | (1L << (SLASH - 96)) | (1L << (BACKSLASH - 96)))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1716);
				tomIdentifier();
				}
				}
				setState(1721);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1722);
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
		enterRule(_localctx, 230, RULE_gomStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1724);
			match(GOM);
			setState(1726);
			_la = _input.LA(1);
			if (_la==OPTIONSTART) {
				{
				setState(1725);
				gomOptions();
				}
			}

			setState(1728);
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
		enterRule(_localctx, 232, RULE_gomOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1730);
			match(OPTIONSTART);
			setState(1731);
			match(DMINUSID);
			setState(1736);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1732);
				match(COMMA);
				setState(1733);
				match(DMINUSID);
				}
				}
				setState(1738);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1739);
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

	public static class GomBlockContext extends ParserRuleContext {
		public TerminalNode GOMSTART() { return getToken(TomJavaParser.GOMSTART, 0); }
		public ModuleContext module() {
			return getRuleContext(ModuleContext.class,0);
		}
		public TerminalNode GOMEND() { return getToken(TomJavaParser.GOMEND, 0); }
		public GomBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gomBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGomBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGomBlock(this);
		}
	}

	public final GomBlockContext gomBlock() throws RecognitionException {
		GomBlockContext _localctx = new GomBlockContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_gomBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1741);
			match(GOMSTART);
			setState(1742);
			module();
			setState(1743);
			match(GOMEND);
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
		enterRule(_localctx, 236, RULE_ruleStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1745);
			match(RULE);
			setState(1746);
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
		enterRule(_localctx, 238, RULE_unknownBlock);
		int _la;
		try {
			int _alt;
			setState(1766);
			switch (_input.LA(1)) {
			case BLOCKSTART:
				enterOuterAlt(_localctx, 1);
				{
				setState(1748);
				match(BLOCKSTART);
				setState(1753);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,194,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						setState(1751);
						switch (_input.LA(1)) {
						case BLOCKSTART:
						case SUBBLOCKSTART:
						case SUBSUBBLOCKSTART:
							{
							setState(1749);
							unknownBlock();
							}
							break;
						case ANY:
							{
							setState(1750);
							match(ANY);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						} 
					}
					setState(1755);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,194,_ctx);
				}
				setState(1756);
				match(BLOCKEND);
				}
				break;
			case SUBBLOCKSTART:
			case SUBSUBBLOCKSTART:
				enterOuterAlt(_localctx, 2);
				{
				setState(1757);
				_la = _input.LA(1);
				if ( !(_la==SUBBLOCKSTART || _la==SUBSUBBLOCKSTART) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1762);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,196,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						setState(1760);
						switch (_input.LA(1)) {
						case BLOCKSTART:
						case SUBBLOCKSTART:
						case SUBSUBBLOCKSTART:
							{
							setState(1758);
							unknownBlock();
							}
							break;
						case SUB_ANY:
							{
							setState(1759);
							match(SUB_ANY);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						} 
					}
					setState(1764);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,196,_ctx);
				}
				setState(1765);
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
		enterRule(_localctx, 240, RULE_visit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1768);
			match(VISIT);
			setState(1769);
			tomIdentifier();
			setState(1770);
			match(LBRACE);
			setState(1774);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (LPAREN - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (ANTI - 77)))) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(1771);
				actionRule();
				}
				}
				setState(1776);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1777);
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
		enterRule(_localctx, 242, RULE_actionRule);
		int _la;
		try {
			setState(1803);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,201,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1779);
				patternlist();
				setState(1782);
				_la = _input.LA(1);
				if (_la==AND || _la==OR) {
					{
					setState(1780);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(1781);
					constraint(0);
					}
				}

				setState(1784);
				match(ARROW);
				setState(1785);
				tomBlock();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1787);
				patternlist();
				setState(1790);
				_la = _input.LA(1);
				if (_la==AND || _la==OR) {
					{
					setState(1788);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(1789);
					constraint(0);
					}
				}

				setState(1792);
				match(ARROW);
				setState(1793);
				bqterm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1795);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(1796);
				match(ARROW);
				setState(1797);
				tomBlock();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1799);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(1800);
				match(ARROW);
				setState(1801);
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
		enterRule(_localctx, 244, RULE_tomBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1805);
			match(LBRACE);
			setState(1810);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,203,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(1808);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,202,_ctx) ) {
					case 1:
						{
						setState(1806);
						tomBlock();
						}
						break;
					case 2:
						{
						setState(1807);
						blockStatement();
						}
						break;
					}
					} 
				}
				setState(1812);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,203,_ctx);
			}
			setState(1813);
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
		enterRule(_localctx, 246, RULE_slotList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1815);
			slot();
			setState(1820);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1816);
				match(COMMA);
				setState(1817);
				slot();
				}
				}
				setState(1822);
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
		enterRule(_localctx, 248, RULE_slot);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1823);
			((SlotContext)_localctx).id1 = tomIdentifier();
			setState(1825);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(1824);
				match(COLON);
				}
			}

			setState(1827);
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
		enterRule(_localctx, 250, RULE_patternlist);
		int _la;
		try {
			setState(1872);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,212,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1829);
				pattern();
				setState(1834);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1830);
					match(COMMA);
					setState(1831);
					pattern();
					}
					}
					setState(1836);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1846);
				_la = _input.LA(1);
				if (_la==WHEN) {
					{
					setState(1837);
					match(WHEN);
					setState(1838);
					term();
					setState(1843);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1839);
						match(COMMA);
						setState(1840);
						term();
						}
						}
						setState(1845);
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
				setState(1848);
				match(LPAREN);
				setState(1849);
				pattern();
				setState(1854);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1850);
					match(COMMA);
					setState(1851);
					pattern();
					}
					}
					setState(1856);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1857);
				match(RPAREN);
				setState(1870);
				_la = _input.LA(1);
				if (_la==WHEN) {
					{
					setState(1858);
					match(WHEN);
					setState(1859);
					match(LPAREN);
					setState(1860);
					term();
					setState(1865);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1861);
						match(COMMA);
						setState(1862);
						term();
						}
						}
						setState(1867);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(1868);
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
		public TerminalNode LSHIFT() { return getToken(TomJavaParser.LSHIFT, 0); }
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
		int _startState = 252;
		enterRecursionRule(_localctx, 252, RULE_constraint, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1907);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,213,_ctx) ) {
			case 1:
				{
				setState(1875);
				pattern();
				setState(1876);
				((ConstraintContext)_localctx).match_symbol = match(LSHIFT);
				setState(1877);
				bqterm();
				}
				break;
			case 2:
				{
				setState(1879);
				term();
				setState(1880);
				match(GT);
				setState(1881);
				term();
				}
				break;
			case 3:
				{
				setState(1883);
				term();
				setState(1884);
				match(GE);
				setState(1885);
				term();
				}
				break;
			case 4:
				{
				setState(1887);
				term();
				setState(1888);
				match(LT);
				setState(1889);
				term();
				}
				break;
			case 5:
				{
				setState(1891);
				term();
				setState(1892);
				match(LE);
				setState(1893);
				term();
				}
				break;
			case 6:
				{
				setState(1895);
				term();
				setState(1896);
				match(EQUAL);
				setState(1897);
				term();
				}
				break;
			case 7:
				{
				setState(1899);
				term();
				setState(1900);
				match(NOTEQUAL);
				setState(1901);
				term();
				}
				break;
			case 8:
				{
				setState(1903);
				match(LPAREN);
				setState(1904);
				((ConstraintContext)_localctx).c = constraint(0);
				setState(1905);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1917);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,215,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1915);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,214,_ctx) ) {
					case 1:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(1909);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1910);
						match(AND);
						setState(1911);
						constraint(11);
						}
						break;
					case 2:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(1912);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1913);
						match(OR);
						setState(1914);
						constraint(10);
						}
						break;
					}
					} 
				}
				setState(1919);
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
		enterRule(_localctx, 254, RULE_term);
		int _la;
		try {
			setState(1939);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,219,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1920);
				((TermContext)_localctx).var = tomIdentifier();
				setState(1922);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,216,_ctx) ) {
				case 1:
					{
					setState(1921);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1924);
				((TermContext)_localctx).fsym = tomIdentifier();
				setState(1925);
				match(LPAREN);
				setState(1934);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (SUB - 77)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1926);
					term();
					setState(1931);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1927);
						match(COMMA);
						setState(1928);
						term();
						}
						}
						setState(1933);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1936);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1938);
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
		enterRule(_localctx, 256, RULE_bqterm);
		int _la;
		try {
			setState(1996);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,232,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1942);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,220,_ctx) ) {
				case 1:
					{
					setState(1941);
					((BqtermContext)_localctx).codomain = tomIdentifier();
					}
					break;
				}
				setState(1945);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(1944);
					match(BQUOTE);
					}
				}

				setState(1947);
				((BqtermContext)_localctx).fsym = tomIdentifier();
				setState(1948);
				match(LPAREN);
				setState(1957);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (BQUOTE - 77)))) != 0) || _la==IDENTIFIER) {
					{
					setState(1949);
					bqterm();
					setState(1954);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1950);
						match(COMMA);
						setState(1951);
						bqterm();
						}
						}
						setState(1956);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1959);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1962);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,224,_ctx) ) {
				case 1:
					{
					setState(1961);
					((BqtermContext)_localctx).codomain = tomIdentifier();
					}
					break;
				}
				setState(1965);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(1964);
					match(BQUOTE);
					}
				}

				setState(1967);
				((BqtermContext)_localctx).fsym = tomIdentifier();
				setState(1968);
				match(LBRACK);
				setState(1977);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
					{
					setState(1969);
					pairSlotBqterm();
					setState(1974);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1970);
						match(COMMA);
						setState(1971);
						pairSlotBqterm();
						}
						}
						setState(1976);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1979);
				match(RBRACK);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1982);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,228,_ctx) ) {
				case 1:
					{
					setState(1981);
					((BqtermContext)_localctx).codomain = tomIdentifier();
					}
					break;
				}
				setState(1985);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(1984);
					match(BQUOTE);
					}
				}

				setState(1987);
				((BqtermContext)_localctx).var = tomIdentifier();
				setState(1989);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,230,_ctx) ) {
				case 1:
					{
					setState(1988);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1992);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
					{
					setState(1991);
					((BqtermContext)_localctx).codomain = tomIdentifier();
					}
				}

				setState(1994);
				constant();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1995);
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
		enterRule(_localctx, 258, RULE_pairSlotBqterm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1998);
			tomIdentifier();
			setState(1999);
			match(ASSIGN);
			setState(2000);
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
		enterRule(_localctx, 260, RULE_bqcomposite);
		int _la;
		try {
			setState(2042);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,238,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2002);
				match(BQUOTE);
				setState(2003);
				((BqcompositeContext)_localctx).fsym = tomIdentifier();
				setState(2004);
				match(LBRACK);
				setState(2013);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
					{
					setState(2005);
					pairSlotBqterm();
					setState(2010);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(2006);
						match(COMMA);
						setState(2007);
						pairSlotBqterm();
						}
						}
						setState(2012);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(2015);
				match(RBRACK);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2017);
				match(BQUOTE);
				setState(2018);
				((BqcompositeContext)_localctx).fsym = tomIdentifier();
				setState(2019);
				match(LPAREN);
				setState(2028);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (EXTENDED_CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (UNDERSCORE - 128)) | (1L << (ANTI - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
					{
					setState(2020);
					composite(0);
					setState(2025);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(2021);
						match(COMMA);
						setState(2022);
						composite(0);
						}
						}
						setState(2027);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(2030);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2032);
				match(BQUOTE);
				setState(2033);
				match(LPAREN);
				setState(2034);
				((BqcompositeContext)_localctx).sub = composite(0);
				setState(2035);
				match(RPAREN);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2037);
				match(BQUOTE);
				setState(2038);
				((BqcompositeContext)_localctx).var = tomIdentifier();
				setState(2040);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,237,_ctx) ) {
				case 1:
					{
					setState(2039);
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
		public TerminalNode ANTI() { return getToken(TomJavaParser.ANTI, 0); }
		public LambdaExpressionContext lambdaExpression() {
			return getRuleContext(LambdaExpressionContext.class,0);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public ClassTypeContext classType() {
			return getRuleContext(ClassTypeContext.class,0);
		}
		public TerminalNode LSHIFT() { return getToken(TomJavaParser.LSHIFT, 0); }
		public List<TerminalNode> GT() { return getTokens(TomJavaParser.GT); }
		public TerminalNode GT(int i) {
			return getToken(TomJavaParser.GT, i);
		}
		public TerminalNode LE() { return getToken(TomJavaParser.LE, 0); }
		public TerminalNode GE() { return getToken(TomJavaParser.GE, 0); }
		public TerminalNode LT() { return getToken(TomJavaParser.LT, 0); }
		public TerminalNode EQUAL() { return getToken(TomJavaParser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(TomJavaParser.NOTEQUAL, 0); }
		public TerminalNode BITAND() { return getToken(TomJavaParser.BITAND, 0); }
		public TerminalNode PIPE() { return getToken(TomJavaParser.PIPE, 0); }
		public TerminalNode AND() { return getToken(TomJavaParser.AND, 0); }
		public TerminalNode OR() { return getToken(TomJavaParser.OR, 0); }
		public TerminalNode COLON() { return getToken(TomJavaParser.COLON, 0); }
		public TerminalNode ASSIGN() { return getToken(TomJavaParser.ASSIGN, 0); }
		public TerminalNode DOT() { return getToken(TomJavaParser.DOT, 0); }
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
		int _startState = 262;
		enterRecursionRule(_localctx, 262, RULE_composite, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2098);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,245,_ctx) ) {
			case 1:
				{
				setState(2045);
				((CompositeContext)_localctx).fsym = tomIdentifier();
				setState(2046);
				match(LPAREN);
				setState(2055);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (EXTENDED_CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (UNDERSCORE - 128)) | (1L << (ANTI - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
					{
					setState(2047);
					composite(0);
					setState(2052);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(2048);
						match(COMMA);
						setState(2049);
						composite(0);
						}
						}
						setState(2054);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(2057);
				match(RPAREN);
				}
				break;
			case 2:
				{
				setState(2059);
				match(LPAREN);
				setState(2060);
				((CompositeContext)_localctx).sub = composite(0);
				setState(2061);
				match(RPAREN);
				}
				break;
			case 3:
				{
				setState(2063);
				((CompositeContext)_localctx).var = tomIdentifier();
				setState(2065);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,241,_ctx) ) {
				case 1:
					{
					setState(2064);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				{
				setState(2067);
				constant();
				}
				break;
			case 5:
				{
				setState(2068);
				match(UNDERSCORE);
				}
				break;
			case 6:
				{
				setState(2069);
				javaIdentifier();
				}
				break;
			case 7:
				{
				setState(2070);
				match(NEW);
				setState(2071);
				creator();
				}
				break;
			case 8:
				{
				setState(2072);
				match(LPAREN);
				setState(2073);
				typeType();
				setState(2074);
				match(RPAREN);
				setState(2075);
				composite(21);
				}
				break;
			case 9:
				{
				setState(2077);
				((CompositeContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 108)) & ~0x3f) == 0 && ((1L << (_la - 108)) & ((1L << (INC - 108)) | (1L << (DEC - 108)) | (1L << (ADD - 108)) | (1L << (SUB - 108)))) != 0)) ) {
					((CompositeContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(2078);
				composite(19);
				}
				break;
			case 10:
				{
				setState(2079);
				((CompositeContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TILDE || _la==ANTI) ) {
					((CompositeContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(2080);
				composite(18);
				}
				break;
			case 11:
				{
				setState(2081);
				lambdaExpression();
				}
				break;
			case 12:
				{
				setState(2082);
				typeType();
				setState(2083);
				match(COLONCOLON);
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
				case LT:
				case IDENTIFIER:
					{
					setState(2085);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(2084);
						typeArguments();
						}
					}

					setState(2087);
					javaIdentifier();
					}
					break;
				case NEW:
					{
					setState(2088);
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
				setState(2091);
				classType();
				setState(2092);
				match(COLONCOLON);
				setState(2094);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(2093);
					typeArguments();
					}
				}

				setState(2096);
				match(NEW);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(2191);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,253,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(2189);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,252,_ctx) ) {
					case 1:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2100);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(2101);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & ((1L << (MOD - 114)) | (1L << (STAR - 114)) | (1L << (SLASH - 114)))) != 0)) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2102);
						composite(18);
						}
						break;
					case 2:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2103);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(2104);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2105);
						composite(17);
						}
						break;
					case 3:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2106);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(2113);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,246,_ctx) ) {
						case 1:
							{
							setState(2107);
							match(LSHIFT);
							}
							break;
						case 2:
							{
							setState(2108);
							match(GT);
							setState(2109);
							match(GT);
							setState(2110);
							match(GT);
							}
							break;
						case 3:
							{
							setState(2111);
							match(GT);
							setState(2112);
							match(GT);
							}
							break;
						}
						setState(2115);
						composite(16);
						}
						break;
					case 4:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2116);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(2117);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 98)) & ~0x3f) == 0 && ((1L << (_la - 98)) & ((1L << (GT - 98)) | (1L << (LT - 98)) | (1L << (LE - 98)) | (1L << (GE - 98)))) != 0)) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2118);
						composite(15);
						}
						break;
					case 5:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2119);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(2120);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQUAL || _la==NOTEQUAL) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2121);
						composite(13);
						}
						break;
					case 6:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2122);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(2123);
						((CompositeContext)_localctx).bop = match(BITAND);
						setState(2124);
						composite(12);
						}
						break;
					case 7:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2125);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(2126);
						((CompositeContext)_localctx).bop = match(CARET);
						setState(2127);
						composite(11);
						}
						break;
					case 8:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2128);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(2129);
						((CompositeContext)_localctx).bop = match(PIPE);
						setState(2130);
						composite(10);
						}
						break;
					case 9:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2131);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(2132);
						((CompositeContext)_localctx).bop = match(AND);
						setState(2133);
						composite(9);
						}
						break;
					case 10:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2134);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(2135);
						((CompositeContext)_localctx).bop = match(OR);
						setState(2136);
						composite(8);
						}
						break;
					case 11:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2137);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(2138);
						((CompositeContext)_localctx).bop = match(QMARK);
						setState(2139);
						composite(0);
						setState(2140);
						match(COLON);
						setState(2141);
						composite(7);
						}
						break;
					case 12:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2143);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(2144);
						((CompositeContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 97)) & ~0x3f) == 0 && ((1L << (_la - 97)) & ((1L << (ASSIGN - 97)) | (1L << (ADD_ASSIGN - 97)) | (1L << (SUB_ASSIGN - 97)) | (1L << (MUL_ASSIGN - 97)) | (1L << (DIV_ASSIGN - 97)) | (1L << (AND_ASSIGN - 97)) | (1L << (OR_ASSIGN - 97)) | (1L << (XOR_ASSIGN - 97)) | (1L << (MOD_ASSIGN - 97)) | (1L << (LSHIFT_ASSIGN - 97)) | (1L << (RSHIFT_ASSIGN - 97)) | (1L << (URSHIFT_ASSIGN - 97)))) != 0)) ) {
							((CompositeContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(2145);
						composite(5);
						}
						break;
					case 13:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2146);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(2147);
						match(LPAREN);
						setState(2156);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE) | (1L << VISIT) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (MAKE_EMPTY - 64)) | (1L << (MAKE_APPEND - 64)) | (1L << (MAKE_INSERT - 64)) | (1L << (GET_SLOT - 64)) | (1L << (GET_DEFAULT - 64)) | (1L << (GET_ELEMENT - 64)) | (1L << (GET_HEAD - 64)) | (1L << (GET_TAIL - 64)) | (1L << (GET_SIZE - 64)) | (1L << (IS_EMPTY - 64)) | (1L << (IMPLEMENT - 64)) | (1L << (EQUALS - 64)) | (1L << (WHEN - 64)) | (1L << (DECIMAL_LITERAL - 64)) | (1L << (FLOAT_LITERAL - 64)) | (1L << (BOOL_LITERAL - 64)) | (1L << (CHAR_LITERAL - 64)) | (1L << (EXTENDED_CHAR_LITERAL - 64)) | (1L << (STRING_LITERAL - 64)) | (1L << (NULL_LITERAL - 64)) | (1L << (LPAREN - 64)) | (1L << (TILDE - 64)) | (1L << (INC - 64)) | (1L << (DEC - 64)) | (1L << (ADD - 64)) | (1L << (SUB - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (UNDERSCORE - 128)) | (1L << (ANTI - 128)) | (1L << (AT - 128)) | (1L << (IDENTIFIER - 128)))) != 0)) {
							{
							setState(2148);
							composite(0);
							setState(2153);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==COMMA) {
								{
								{
								setState(2149);
								match(COMMA);
								setState(2150);
								composite(0);
								}
								}
								setState(2155);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(2158);
						match(RPAREN);
						}
						break;
					case 14:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2159);
						if (!(precpred(_ctx, 24))) throw new FailedPredicateException(this, "precpred(_ctx, 24)");
						setState(2160);
						((CompositeContext)_localctx).bop = match(DOT);
						setState(2171);
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
							setState(2161);
							javaIdentifier();
							}
							break;
						case THIS:
							{
							setState(2162);
							match(THIS);
							}
							break;
						case NEW:
							{
							setState(2163);
							match(NEW);
							setState(2165);
							_la = _input.LA(1);
							if (_la==LT) {
								{
								setState(2164);
								nonWildcardTypeArguments();
								}
							}

							setState(2167);
							innerCreator();
							}
							break;
						case SUPER:
							{
							setState(2168);
							match(SUPER);
							setState(2169);
							superSuffix();
							}
							break;
						case LT:
							{
							setState(2170);
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
						setState(2173);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(2174);
						match(LBRACK);
						setState(2175);
						composite(0);
						setState(2176);
						match(RBRACK);
						}
						break;
					case 16:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2178);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(2179);
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
						setState(2180);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(2181);
						((CompositeContext)_localctx).bop = match(INSTANCEOF);
						setState(2182);
						typeType();
						}
						break;
					case 18:
						{
						_localctx = new CompositeContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_composite);
						setState(2183);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(2184);
						match(COLONCOLON);
						setState(2186);
						_la = _input.LA(1);
						if (_la==LT) {
							{
							setState(2185);
							typeArguments();
							}
						}

						setState(2188);
						javaIdentifier();
						}
						break;
					}
					} 
				}
				setState(2193);
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
		enterRule(_localctx, 264, RULE_pattern);
		int _la;
		try {
			setState(2222);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,257,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2194);
				tomIdentifier();
				setState(2195);
				match(AT);
				setState(2196);
				pattern();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2198);
				match(ANTI);
				setState(2199);
				pattern();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2200);
				fsymbol();
				setState(2201);
				explicitArgs();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2203);
				fsymbol();
				setState(2204);
				implicitArgs();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2206);
				((PatternContext)_localctx).var = tomIdentifier();
				setState(2208);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(2207);
					match(STAR);
					}
				}

				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2210);
				match(UNDERSCORE);
				setState(2212);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(2211);
					match(STAR);
					}
				}

				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2214);
				constant();
				setState(2219);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PIPE) {
					{
					{
					setState(2215);
					match(PIPE);
					setState(2216);
					constant();
					}
					}
					setState(2221);
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
		enterRule(_localctx, 266, RULE_fsymbol);
		int _la;
		try {
			setState(2235);
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
				setState(2224);
				headSymbol();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(2225);
				match(LPAREN);
				setState(2226);
				headSymbol();
				setState(2229); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(2227);
					match(PIPE);
					setState(2228);
					headSymbol();
					}
					}
					setState(2231); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==PIPE );
				setState(2233);
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
		enterRule(_localctx, 268, RULE_headSymbol);
		int _la;
		try {
			setState(2246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,262,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2237);
				tomIdentifier();
				setState(2239);
				_la = _input.LA(1);
				if (_la==QMARK) {
					{
					setState(2238);
					match(QMARK);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2241);
				tomIdentifier();
				setState(2243);
				_la = _input.LA(1);
				if (_la==DQMARK) {
					{
					setState(2242);
					match(DQMARK);
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2245);
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
		enterRule(_localctx, 270, RULE_constant);
		int _la;
		try {
			setState(2259);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,265,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2249);
				_la = _input.LA(1);
				if (_la==SUB) {
					{
					setState(2248);
					match(SUB);
					}
				}

				setState(2251);
				match(DECIMAL_LITERAL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2253);
				_la = _input.LA(1);
				if (_la==SUB) {
					{
					setState(2252);
					match(SUB);
					}
				}

				setState(2255);
				match(FLOAT_LITERAL);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2256);
				match(CHAR_LITERAL);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2257);
				match(EXTENDED_CHAR_LITERAL);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2258);
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
		enterRule(_localctx, 272, RULE_explicitArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2261);
			match(LPAREN);
			setState(2270);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (DECIMAL_LITERAL - 77)) | (1L << (FLOAT_LITERAL - 77)) | (1L << (BOOL_LITERAL - 77)) | (1L << (CHAR_LITERAL - 77)) | (1L << (EXTENDED_CHAR_LITERAL - 77)) | (1L << (STRING_LITERAL - 77)) | (1L << (NULL_LITERAL - 77)) | (1L << (LPAREN - 77)) | (1L << (SUB - 77)) | (1L << (UNDERSCORE - 77)) | (1L << (ANTI - 77)))) != 0) || _la==IDENTIFIER) {
				{
				setState(2262);
				pattern();
				setState(2267);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2263);
					match(COMMA);
					setState(2264);
					pattern();
					}
					}
					setState(2269);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2272);
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
		enterRule(_localctx, 274, RULE_implicitArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2274);
			match(LBRACK);
			setState(2288);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
				{
				setState(2275);
				tomIdentifier();
				setState(2276);
				match(ASSIGN);
				setState(2277);
				pattern();
				setState(2285);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2278);
					match(COMMA);
					setState(2279);
					tomIdentifier();
					setState(2280);
					match(ASSIGN);
					setState(2281);
					pattern();
					}
					}
					setState(2287);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2290);
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
		enterRule(_localctx, 276, RULE_typeterm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2292);
			match(TYPETERM);
			setState(2293);
			((TypetermContext)_localctx).type = tomIdentifier();
			setState(2296);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(2294);
				match(EXTENDS);
				setState(2295);
				((TypetermContext)_localctx).supertype = tomIdentifier();
				}
			}

			setState(2298);
			match(LBRACE);
			setState(2299);
			implement();
			setState(2301);
			_la = _input.LA(1);
			if (_la==IS_SORT) {
				{
				setState(2300);
				isSort();
				}
			}

			setState(2304);
			_la = _input.LA(1);
			if (_la==EQUALS) {
				{
				setState(2303);
				equalsTerm();
				}
			}

			setState(2306);
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
		enterRule(_localctx, 278, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2308);
			match(OP);
			setState(2309);
			((OperatorContext)_localctx).codomain = tomIdentifier();
			setState(2310);
			((OperatorContext)_localctx).opname = tomIdentifier();
			setState(2311);
			match(LPAREN);
			setState(2313);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
				{
				setState(2312);
				slotList();
				}
			}

			setState(2315);
			match(RPAREN);
			setState(2316);
			match(LBRACE);
			setState(2323);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (IS_FSYM - 61)) | (1L << (MAKE - 61)) | (1L << (GET_SLOT - 61)) | (1L << (GET_DEFAULT - 61)))) != 0)) {
				{
				setState(2321);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(2317);
					isFsym();
					}
					break;
				case MAKE:
					{
					setState(2318);
					make();
					}
					break;
				case GET_SLOT:
					{
					setState(2319);
					getSlot();
					}
					break;
				case GET_DEFAULT:
					{
					setState(2320);
					getDefault();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2325);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2326);
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
		enterRule(_localctx, 280, RULE_oplist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2328);
			match(OPLIST);
			setState(2329);
			((OplistContext)_localctx).codomain = tomIdentifier();
			setState(2330);
			((OplistContext)_localctx).opname = tomIdentifier();
			setState(2331);
			match(LPAREN);
			setState(2332);
			((OplistContext)_localctx).domain = tomIdentifier();
			setState(2333);
			match(STAR);
			setState(2334);
			match(RPAREN);
			setState(2335);
			match(LBRACE);
			setState(2344);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (IS_FSYM - 61)) | (1L << (MAKE_EMPTY - 61)) | (1L << (MAKE_INSERT - 61)) | (1L << (GET_HEAD - 61)) | (1L << (GET_TAIL - 61)) | (1L << (IS_EMPTY - 61)))) != 0)) {
				{
				setState(2342);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(2336);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(2337);
					makeEmptyList();
					}
					break;
				case MAKE_INSERT:
					{
					setState(2338);
					makeInsertList();
					}
					break;
				case GET_HEAD:
					{
					setState(2339);
					getHead();
					}
					break;
				case GET_TAIL:
					{
					setState(2340);
					getTail();
					}
					break;
				case IS_EMPTY:
					{
					setState(2341);
					isEmptyList();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2346);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2347);
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
		enterRule(_localctx, 282, RULE_oparray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2349);
			match(OPARRAY);
			setState(2350);
			((OparrayContext)_localctx).codomain = tomIdentifier();
			setState(2351);
			((OparrayContext)_localctx).opname = tomIdentifier();
			setState(2352);
			match(LPAREN);
			setState(2353);
			((OparrayContext)_localctx).domain = tomIdentifier();
			setState(2354);
			match(STAR);
			setState(2355);
			match(RPAREN);
			setState(2356);
			match(LBRACE);
			setState(2364);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (IS_FSYM - 61)) | (1L << (MAKE_EMPTY - 61)) | (1L << (MAKE_APPEND - 61)) | (1L << (GET_ELEMENT - 61)) | (1L << (GET_SIZE - 61)))) != 0)) {
				{
				setState(2362);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(2357);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(2358);
					makeEmptyArray();
					}
					break;
				case MAKE_APPEND:
					{
					setState(2359);
					makeAppendArray();
					}
					break;
				case GET_ELEMENT:
					{
					setState(2360);
					getElement();
					}
					break;
				case GET_SIZE:
					{
					setState(2361);
					getSize();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2366);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2367);
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
		enterRule(_localctx, 284, RULE_termBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2369);
			match(LBRACE);
			setState(2370);
			expression(0);
			setState(2371);
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
		enterRule(_localctx, 286, RULE_implement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2373);
			match(IMPLEMENT);
			setState(2374);
			match(LBRACE);
			setState(2375);
			typeType();
			setState(2376);
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
		enterRule(_localctx, 288, RULE_equalsTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2378);
			match(EQUALS);
			setState(2379);
			match(LPAREN);
			setState(2380);
			((EqualsTermContext)_localctx).id1 = tomIdentifier();
			setState(2381);
			match(COMMA);
			setState(2382);
			((EqualsTermContext)_localctx).id2 = tomIdentifier();
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
		enterRule(_localctx, 290, RULE_isSort);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2386);
			match(IS_SORT);
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
		enterRule(_localctx, 292, RULE_isFsym);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2392);
			match(IS_FSYM);
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
		enterRule(_localctx, 294, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2398);
			match(MAKE);
			setState(2399);
			match(LPAREN);
			setState(2408);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ABSTRACT) | (1L << ASSERT) | (1L << BOOLEAN) | (1L << BREAK) | (1L << BYTE) | (1L << CASE) | (1L << CATCH) | (1L << CHAR) | (1L << CLASS) | (1L << CONST) | (1L << CONTINUE) | (1L << DEFAULT) | (1L << DO) | (1L << DOUBLE) | (1L << ELSE) | (1L << ENUM) | (1L << FINAL) | (1L << FINALLY) | (1L << FLOAT) | (1L << FOR) | (1L << IF) | (1L << GOTO) | (1L << IMPLEMENTS) | (1L << IMPORT) | (1L << INSTANCEOF) | (1L << INT) | (1L << INTERFACE) | (1L << LONG) | (1L << NATIVE) | (1L << NEW) | (1L << PACKAGE) | (1L << PRIVATE) | (1L << PROTECTED) | (1L << PUBLIC) | (1L << RETURN) | (1L << SHORT) | (1L << STATIC) | (1L << STRICTFP) | (1L << SUPER) | (1L << SWITCH) | (1L << SYNCHRONIZED) | (1L << THIS) | (1L << THROW) | (1L << THROWS) | (1L << TRANSIENT) | (1L << TRY) | (1L << VOID) | (1L << VOLATILE) | (1L << WHILE))) != 0) || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (BOOL_LITERAL - 83)) | (1L << (NULL_LITERAL - 83)) | (1L << (IDENTIFIER - 83)))) != 0)) {
				{
				setState(2400);
				tomIdentifier();
				setState(2405);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2401);
					match(COMMA);
					setState(2402);
					tomIdentifier();
					}
					}
					setState(2407);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2410);
			match(RPAREN);
			setState(2411);
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
		enterRule(_localctx, 296, RULE_makeEmptyList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2413);
			match(MAKE_EMPTY);
			setState(2414);
			match(LPAREN);
			setState(2415);
			match(RPAREN);
			setState(2416);
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
		enterRule(_localctx, 298, RULE_makeEmptyArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2418);
			match(MAKE_EMPTY);
			setState(2419);
			match(LPAREN);
			setState(2420);
			tomIdentifier();
			setState(2421);
			match(RPAREN);
			setState(2422);
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
		enterRule(_localctx, 300, RULE_makeAppendArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2424);
			match(MAKE_APPEND);
			setState(2425);
			match(LPAREN);
			setState(2426);
			((MakeAppendArrayContext)_localctx).id1 = tomIdentifier();
			setState(2427);
			match(COMMA);
			setState(2428);
			((MakeAppendArrayContext)_localctx).id2 = tomIdentifier();
			setState(2429);
			match(RPAREN);
			setState(2430);
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
		enterRule(_localctx, 302, RULE_makeInsertList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2432);
			match(MAKE_INSERT);
			setState(2433);
			match(LPAREN);
			setState(2434);
			((MakeInsertListContext)_localctx).id1 = tomIdentifier();
			setState(2435);
			match(COMMA);
			setState(2436);
			((MakeInsertListContext)_localctx).id2 = tomIdentifier();
			setState(2437);
			match(RPAREN);
			setState(2438);
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
		enterRule(_localctx, 304, RULE_getSlot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2440);
			match(GET_SLOT);
			setState(2441);
			match(LPAREN);
			setState(2442);
			((GetSlotContext)_localctx).id1 = tomIdentifier();
			setState(2443);
			match(COMMA);
			setState(2444);
			((GetSlotContext)_localctx).id2 = tomIdentifier();
			setState(2445);
			match(RPAREN);
			setState(2446);
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
		enterRule(_localctx, 306, RULE_getHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2448);
			match(GET_HEAD);
			setState(2449);
			match(LPAREN);
			setState(2450);
			tomIdentifier();
			setState(2451);
			match(RPAREN);
			setState(2452);
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
		enterRule(_localctx, 308, RULE_getTail);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2454);
			match(GET_TAIL);
			setState(2455);
			match(LPAREN);
			setState(2456);
			tomIdentifier();
			setState(2457);
			match(RPAREN);
			setState(2458);
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
		enterRule(_localctx, 310, RULE_getElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2460);
			match(GET_ELEMENT);
			setState(2461);
			match(LPAREN);
			setState(2462);
			((GetElementContext)_localctx).id1 = tomIdentifier();
			setState(2463);
			match(COMMA);
			setState(2464);
			((GetElementContext)_localctx).id2 = tomIdentifier();
			setState(2465);
			match(RPAREN);
			setState(2466);
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
		enterRule(_localctx, 312, RULE_isEmptyList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2468);
			match(IS_EMPTY);
			setState(2469);
			match(LPAREN);
			setState(2470);
			tomIdentifier();
			setState(2471);
			match(RPAREN);
			setState(2472);
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
		enterRule(_localctx, 314, RULE_getSize);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2474);
			match(GET_SIZE);
			setState(2475);
			match(LPAREN);
			setState(2476);
			tomIdentifier();
			setState(2477);
			match(RPAREN);
			setState(2478);
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
		enterRule(_localctx, 316, RULE_getDefault);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2480);
			match(GET_DEFAULT);
			setState(2481);
			match(LPAREN);
			setState(2482);
			tomIdentifier();
			setState(2483);
			match(RPAREN);
			setState(2484);
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
		enterRule(_localctx, 318, RULE_tomIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2486);
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

	public static class ModuleContext extends ParserRuleContext {
		public TerminalNode MODULE() { return getToken(TomJavaParser.MODULE, 0); }
		public ModulenameContext modulename() {
			return getRuleContext(ModulenameContext.class,0);
		}
		public SectionContext section() {
			return getRuleContext(SectionContext.class,0);
		}
		public ImportsContext imports() {
			return getRuleContext(ImportsContext.class,0);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitModule(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 320, RULE_module);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2488);
			match(MODULE);
			setState(2489);
			modulename();
			setState(2491);
			_la = _input.LA(1);
			if (_la==IMPORTS) {
				{
				setState(2490);
				imports();
				}
			}

			setState(2493);
			section();
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

	public static class ModulenameContext extends ParserRuleContext {
		public GomIdentifierContext gomIdentifier;
		public List<GomIdentifierContext> prefixes = new ArrayList<GomIdentifierContext>();
		public GomIdentifierContext moduleName;
		public List<GomIdentifierContext> gomIdentifier() {
			return getRuleContexts(GomIdentifierContext.class);
		}
		public GomIdentifierContext gomIdentifier(int i) {
			return getRuleContext(GomIdentifierContext.class,i);
		}
		public List<TerminalNode> GOM_DOT() { return getTokens(TomJavaParser.GOM_DOT); }
		public TerminalNode GOM_DOT(int i) {
			return getToken(TomJavaParser.GOM_DOT, i);
		}
		public ModulenameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modulename; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterModulename(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitModulename(this);
		}
	}

	public final ModulenameContext modulename() throws RecognitionException {
		ModulenameContext _localctx = new ModulenameContext(_ctx, getState());
		enterRule(_localctx, 322, RULE_modulename);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2500);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,283,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(2495);
					((ModulenameContext)_localctx).gomIdentifier = gomIdentifier();
					((ModulenameContext)_localctx).prefixes.add(((ModulenameContext)_localctx).gomIdentifier);
					setState(2496);
					match(GOM_DOT);
					}
					} 
				}
				setState(2502);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,283,_ctx);
			}
			setState(2503);
			((ModulenameContext)_localctx).moduleName = gomIdentifier();
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

	public static class ImportsContext extends ParserRuleContext {
		public TerminalNode IMPORTS() { return getToken(TomJavaParser.IMPORTS, 0); }
		public List<GomIdentifierContext> gomIdentifier() {
			return getRuleContexts(GomIdentifierContext.class);
		}
		public GomIdentifierContext gomIdentifier(int i) {
			return getRuleContext(GomIdentifierContext.class,i);
		}
		public ImportsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_imports; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterImports(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitImports(this);
		}
	}

	public final ImportsContext imports() throws RecognitionException {
		ImportsContext _localctx = new ImportsContext(_ctx, getState());
		enterRule(_localctx, 324, RULE_imports);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2505);
			match(IMPORTS);
			setState(2509);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 177)) & ~0x3f) == 0 && ((1L << (_la - 177)) & ((1L << (AC - 177)) | (1L << (ACU - 177)) | (1L << (AU - 177)) | (1L << (FL - 177)) | (1L << (FREE - 177)) | (1L << (HOOK_MAKE - 177)) | (1L << (HOOK_MAKE_INSERT - 177)) | (1L << (HOOK_MAKE_EMPTY - 177)) | (1L << (HOOK_IMPORT - 177)) | (1L << (HOOK_INTERFACE - 177)) | (1L << (BLOCK - 177)) | (1L << (MAPPING - 177)) | (1L << (ID - 177)))) != 0)) {
				{
				{
				setState(2506);
				gomIdentifier();
				}
				}
				setState(2511);
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

	public static class SectionContext extends ParserRuleContext {
		public AdtgrammarContext adtgrammar() {
			return getRuleContext(AdtgrammarContext.class,0);
		}
		public TerminalNode GOM_PUBLIC() { return getToken(TomJavaParser.GOM_PUBLIC, 0); }
		public SectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_section; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterSection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitSection(this);
		}
	}

	public final SectionContext section() throws RecognitionException {
		SectionContext _localctx = new SectionContext(_ctx, getState());
		enterRule(_localctx, 326, RULE_section);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2513);
			_la = _input.LA(1);
			if (_la==GOM_PUBLIC) {
				{
				setState(2512);
				match(GOM_PUBLIC);
				}
			}

			setState(2515);
			adtgrammar();
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

	public static class AdtgrammarContext extends ParserRuleContext {
		public List<SyntaxContext> syntax() {
			return getRuleContexts(SyntaxContext.class);
		}
		public SyntaxContext syntax(int i) {
			return getRuleContext(SyntaxContext.class,i);
		}
		public AdtgrammarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_adtgrammar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAdtgrammar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAdtgrammar(this);
		}
	}

	public final AdtgrammarContext adtgrammar() throws RecognitionException {
		AdtgrammarContext _localctx = new AdtgrammarContext(_ctx, getState());
		enterRule(_localctx, 328, RULE_adtgrammar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2520);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==GOM_ABSTRACT) {
				{
				{
				setState(2517);
				syntax();
				}
				}
				setState(2522);
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

	public static class SyntaxContext extends ParserRuleContext {
		public TerminalNode GOM_ABSTRACT() { return getToken(TomJavaParser.GOM_ABSTRACT, 0); }
		public TerminalNode SYNTAX() { return getToken(TomJavaParser.SYNTAX, 0); }
		public List<HookConstructContext> hookConstruct() {
			return getRuleContexts(HookConstructContext.class);
		}
		public HookConstructContext hookConstruct(int i) {
			return getRuleContext(HookConstructContext.class,i);
		}
		public List<TypedeclContext> typedecl() {
			return getRuleContexts(TypedeclContext.class);
		}
		public TypedeclContext typedecl(int i) {
			return getRuleContext(TypedeclContext.class,i);
		}
		public List<AtomdeclContext> atomdecl() {
			return getRuleContexts(AtomdeclContext.class);
		}
		public AtomdeclContext atomdecl(int i) {
			return getRuleContext(AtomdeclContext.class,i);
		}
		public SyntaxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_syntax; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterSyntax(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitSyntax(this);
		}
	}

	public final SyntaxContext syntax() throws RecognitionException {
		SyntaxContext _localctx = new SyntaxContext(_ctx, getState());
		enterRule(_localctx, 330, RULE_syntax);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(2523);
			match(GOM_ABSTRACT);
			setState(2524);
			match(SYNTAX);
			}
			setState(2531);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 151)) & ~0x3f) == 0 && ((1L << (_la - 151)) & ((1L << (MODULE - 151)) | (1L << (SORT - 151)) | (1L << (OPERATOR - 151)) | (1L << (ATOM - 151)) | (1L << (AC - 151)) | (1L << (ACU - 151)) | (1L << (AU - 151)) | (1L << (FL - 151)) | (1L << (FREE - 151)) | (1L << (HOOK_MAKE - 151)) | (1L << (HOOK_MAKE_INSERT - 151)) | (1L << (HOOK_MAKE_EMPTY - 151)) | (1L << (HOOK_IMPORT - 151)) | (1L << (HOOK_INTERFACE - 151)) | (1L << (BLOCK - 151)) | (1L << (MAPPING - 151)) | (1L << (ID - 151)))) != 0)) {
				{
				setState(2529);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,287,_ctx) ) {
				case 1:
					{
					setState(2526);
					hookConstruct();
					}
					break;
				case 2:
					{
					setState(2527);
					typedecl();
					}
					break;
				case 3:
					{
					setState(2528);
					atomdecl();
					}
					break;
				}
				}
				setState(2533);
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

	public static class AtomdeclContext extends ParserRuleContext {
		public TerminalNode ATOM() { return getToken(TomJavaParser.ATOM, 0); }
		public GomIdentifierContext gomIdentifier() {
			return getRuleContext(GomIdentifierContext.class,0);
		}
		public AtomdeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomdecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAtomdecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAtomdecl(this);
		}
	}

	public final AtomdeclContext atomdecl() throws RecognitionException {
		AtomdeclContext _localctx = new AtomdeclContext(_ctx, getState());
		enterRule(_localctx, 332, RULE_atomdecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2534);
			match(ATOM);
			setState(2535);
			gomIdentifier();
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

	public static class TypedeclContext extends ParserRuleContext {
		public GomIdentifierContext typename;
		public GomIdentifierContext ptypename;
		public GomIdentifierContext gomIdentifier;
		public List<GomIdentifierContext> atoms = new ArrayList<GomIdentifierContext>();
		public TerminalNode GOM_EQUAL() { return getToken(TomJavaParser.GOM_EQUAL, 0); }
		public AlternativesContext alternatives() {
			return getRuleContext(AlternativesContext.class,0);
		}
		public List<GomIdentifierContext> gomIdentifier() {
			return getRuleContexts(GomIdentifierContext.class);
		}
		public GomIdentifierContext gomIdentifier(int i) {
			return getRuleContext(GomIdentifierContext.class,i);
		}
		public TerminalNode BINDS() { return getToken(TomJavaParser.BINDS, 0); }
		public Pattern_alternativesContext pattern_alternatives() {
			return getRuleContext(Pattern_alternativesContext.class,0);
		}
		public TypedeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTypedecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTypedecl(this);
		}
	}

	public final TypedeclContext typedecl() throws RecognitionException {
		TypedeclContext _localctx = new TypedeclContext(_ctx, getState());
		enterRule(_localctx, 334, RULE_typedecl);
		try {
			setState(2547);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,289,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2537);
				((TypedeclContext)_localctx).typename = gomIdentifier();
				setState(2538);
				match(GOM_EQUAL);
				setState(2539);
				alternatives();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2541);
				((TypedeclContext)_localctx).ptypename = gomIdentifier();
				setState(2542);
				match(BINDS);
				{
				setState(2543);
				((TypedeclContext)_localctx).gomIdentifier = gomIdentifier();
				((TypedeclContext)_localctx).atoms.add(((TypedeclContext)_localctx).gomIdentifier);
				}
				setState(2544);
				match(GOM_EQUAL);
				setState(2545);
				pattern_alternatives();
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

	public static class AlternativesContext extends ParserRuleContext {
		public Token JAVADOC;
		public List<Token> jd = new ArrayList<Token>();
		public List<OpdeclContext> opdecl() {
			return getRuleContexts(OpdeclContext.class);
		}
		public OpdeclContext opdecl(int i) {
			return getRuleContext(OpdeclContext.class,i);
		}
		public List<TerminalNode> JAVADOC() { return getTokens(TomJavaParser.JAVADOC); }
		public TerminalNode JAVADOC(int i) {
			return getToken(TomJavaParser.JAVADOC, i);
		}
		public TerminalNode GOM_SEMI() { return getToken(TomJavaParser.GOM_SEMI, 0); }
		public List<TerminalNode> ALT() { return getTokens(TomJavaParser.ALT); }
		public TerminalNode ALT(int i) {
			return getToken(TomJavaParser.ALT, i);
		}
		public AlternativesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alternatives; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAlternatives(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAlternatives(this);
		}
	}

	public final AlternativesContext alternatives() throws RecognitionException {
		AlternativesContext _localctx = new AlternativesContext(_ctx, getState());
		enterRule(_localctx, 336, RULE_alternatives);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2558);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,291,_ctx) ) {
			case 1:
				{
				{
				setState(2549);
				((AlternativesContext)_localctx).JAVADOC = match(JAVADOC);
				((AlternativesContext)_localctx).jd.add(((AlternativesContext)_localctx).JAVADOC);
				setState(2550);
				match(ALT);
				}
				}
				break;
			case 2:
				{
				{
				setState(2551);
				match(ALT);
				setState(2552);
				((AlternativesContext)_localctx).JAVADOC = match(JAVADOC);
				((AlternativesContext)_localctx).jd.add(((AlternativesContext)_localctx).JAVADOC);
				}
				}
				break;
			case 3:
				{
				setState(2553);
				((AlternativesContext)_localctx).JAVADOC = match(JAVADOC);
				((AlternativesContext)_localctx).jd.add(((AlternativesContext)_localctx).JAVADOC);
				}
				break;
			case 4:
				{
				((AlternativesContext)_localctx).jd.add(null);
				setState(2556);
				_la = _input.LA(1);
				if (_la==ALT) {
					{
					setState(2555);
					match(ALT);
					}
				}

				}
				break;
			}
			setState(2560);
			opdecl();
			setState(2572);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ALT || _la==JAVADOC) {
				{
				{
				setState(2567);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,292,_ctx) ) {
				case 1:
					{
					{
					setState(2561);
					((AlternativesContext)_localctx).JAVADOC = match(JAVADOC);
					((AlternativesContext)_localctx).jd.add(((AlternativesContext)_localctx).JAVADOC);
					setState(2562);
					match(ALT);
					}
					}
					break;
				case 2:
					{
					{
					setState(2563);
					match(ALT);
					setState(2564);
					((AlternativesContext)_localctx).JAVADOC = match(JAVADOC);
					((AlternativesContext)_localctx).jd.add(((AlternativesContext)_localctx).JAVADOC);
					}
					}
					break;
				case 3:
					{
					((AlternativesContext)_localctx).jd.add(null);
					setState(2566);
					match(ALT);
					}
					break;
				}
				setState(2569);
				opdecl();
				}
				}
				setState(2574);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2576);
			_la = _input.LA(1);
			if (_la==GOM_SEMI) {
				{
				setState(2575);
				match(GOM_SEMI);
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

	public static class Pattern_alternativesContext extends ParserRuleContext {
		public List<Pattern_opdeclContext> pattern_opdecl() {
			return getRuleContexts(Pattern_opdeclContext.class);
		}
		public Pattern_opdeclContext pattern_opdecl(int i) {
			return getRuleContext(Pattern_opdeclContext.class,i);
		}
		public List<TerminalNode> ALT() { return getTokens(TomJavaParser.ALT); }
		public TerminalNode ALT(int i) {
			return getToken(TomJavaParser.ALT, i);
		}
		public TerminalNode GOM_SEMI() { return getToken(TomJavaParser.GOM_SEMI, 0); }
		public Pattern_alternativesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern_alternatives; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPattern_alternatives(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPattern_alternatives(this);
		}
	}

	public final Pattern_alternativesContext pattern_alternatives() throws RecognitionException {
		Pattern_alternativesContext _localctx = new Pattern_alternativesContext(_ctx, getState());
		enterRule(_localctx, 338, RULE_pattern_alternatives);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2579);
			_la = _input.LA(1);
			if (_la==ALT) {
				{
				setState(2578);
				match(ALT);
				}
			}

			setState(2581);
			pattern_opdecl();
			setState(2586);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ALT) {
				{
				{
				setState(2582);
				match(ALT);
				setState(2583);
				pattern_opdecl();
				}
				}
				setState(2588);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2590);
			_la = _input.LA(1);
			if (_la==GOM_SEMI) {
				{
				setState(2589);
				match(GOM_SEMI);
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

	public static class OpdeclContext extends ParserRuleContext {
		public GomIdentifierContext gomIdentifier() {
			return getRuleContext(GomIdentifierContext.class,0);
		}
		public FieldlistContext fieldlist() {
			return getRuleContext(FieldlistContext.class,0);
		}
		public OpdeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_opdecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterOpdecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitOpdecl(this);
		}
	}

	public final OpdeclContext opdecl() throws RecognitionException {
		OpdeclContext _localctx = new OpdeclContext(_ctx, getState());
		enterRule(_localctx, 340, RULE_opdecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2592);
			gomIdentifier();
			setState(2593);
			fieldlist();
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

	public static class Pattern_opdeclContext extends ParserRuleContext {
		public GomIdentifierContext gomIdentifier() {
			return getRuleContext(GomIdentifierContext.class,0);
		}
		public Pattern_fieldlistContext pattern_fieldlist() {
			return getRuleContext(Pattern_fieldlistContext.class,0);
		}
		public Pattern_opdeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern_opdecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPattern_opdecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPattern_opdecl(this);
		}
	}

	public final Pattern_opdeclContext pattern_opdecl() throws RecognitionException {
		Pattern_opdeclContext _localctx = new Pattern_opdeclContext(_ctx, getState());
		enterRule(_localctx, 342, RULE_pattern_opdecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2595);
			gomIdentifier();
			setState(2596);
			pattern_fieldlist();
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

	public static class FieldlistContext extends ParserRuleContext {
		public TerminalNode GOM_LPAREN() { return getToken(TomJavaParser.GOM_LPAREN, 0); }
		public TerminalNode GOM_RPAREN() { return getToken(TomJavaParser.GOM_RPAREN, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public List<TerminalNode> GOM_COMMA() { return getTokens(TomJavaParser.GOM_COMMA); }
		public TerminalNode GOM_COMMA(int i) {
			return getToken(TomJavaParser.GOM_COMMA, i);
		}
		public FieldlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterFieldlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitFieldlist(this);
		}
	}

	public final FieldlistContext fieldlist() throws RecognitionException {
		FieldlistContext _localctx = new FieldlistContext(_ctx, getState());
		enterRule(_localctx, 344, RULE_fieldlist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2598);
			match(GOM_LPAREN);
			setState(2607);
			_la = _input.LA(1);
			if (((((_la - 172)) & ~0x3f) == 0 && ((1L << (_la - 172)) & ((1L << (LDIPLE - 172)) | (1L << (AC - 172)) | (1L << (ACU - 172)) | (1L << (AU - 172)) | (1L << (FL - 172)) | (1L << (FREE - 172)) | (1L << (HOOK_MAKE - 172)) | (1L << (HOOK_MAKE_INSERT - 172)) | (1L << (HOOK_MAKE_EMPTY - 172)) | (1L << (HOOK_IMPORT - 172)) | (1L << (HOOK_INTERFACE - 172)) | (1L << (BLOCK - 172)) | (1L << (MAPPING - 172)) | (1L << (ID - 172)))) != 0)) {
				{
				setState(2599);
				field();
				setState(2604);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==GOM_COMMA) {
					{
					{
					setState(2600);
					match(GOM_COMMA);
					setState(2601);
					field();
					}
					}
					setState(2606);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2609);
			match(GOM_RPAREN);
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

	public static class Pattern_fieldlistContext extends ParserRuleContext {
		public TerminalNode GOM_LPAREN() { return getToken(TomJavaParser.GOM_LPAREN, 0); }
		public TerminalNode GOM_RPAREN() { return getToken(TomJavaParser.GOM_RPAREN, 0); }
		public List<Pattern_fieldContext> pattern_field() {
			return getRuleContexts(Pattern_fieldContext.class);
		}
		public Pattern_fieldContext pattern_field(int i) {
			return getRuleContext(Pattern_fieldContext.class,i);
		}
		public List<TerminalNode> GOM_COMMA() { return getTokens(TomJavaParser.GOM_COMMA); }
		public TerminalNode GOM_COMMA(int i) {
			return getToken(TomJavaParser.GOM_COMMA, i);
		}
		public Pattern_fieldlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern_fieldlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPattern_fieldlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPattern_fieldlist(this);
		}
	}

	public final Pattern_fieldlistContext pattern_fieldlist() throws RecognitionException {
		Pattern_fieldlistContext _localctx = new Pattern_fieldlistContext(_ctx, getState());
		enterRule(_localctx, 346, RULE_pattern_fieldlist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2611);
			match(GOM_LPAREN);
			setState(2620);
			_la = _input.LA(1);
			if (((((_la - 159)) & ~0x3f) == 0 && ((1L << (_la - 159)) & ((1L << (INNER - 159)) | (1L << (OUTER - 159)) | (1L << (NEUTRAL - 159)) | (1L << (AC - 159)) | (1L << (ACU - 159)) | (1L << (AU - 159)) | (1L << (FL - 159)) | (1L << (FREE - 159)) | (1L << (HOOK_MAKE - 159)) | (1L << (HOOK_MAKE_INSERT - 159)) | (1L << (HOOK_MAKE_EMPTY - 159)) | (1L << (HOOK_IMPORT - 159)) | (1L << (HOOK_INTERFACE - 159)) | (1L << (BLOCK - 159)) | (1L << (MAPPING - 159)) | (1L << (ID - 159)))) != 0)) {
				{
				setState(2612);
				pattern_field();
				setState(2617);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==GOM_COMMA) {
					{
					{
					setState(2613);
					match(GOM_COMMA);
					setState(2614);
					pattern_field();
					}
					}
					setState(2619);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2622);
			match(GOM_RPAREN);
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

	public static class FieldContext extends ParserRuleContext {
		public GomIdentifierContext type;
		public GomIdentifierContext name;
		public TerminalNode GOM_STAR() { return getToken(TomJavaParser.GOM_STAR, 0); }
		public List<GomIdentifierContext> gomIdentifier() {
			return getRuleContexts(GomIdentifierContext.class);
		}
		public GomIdentifierContext gomIdentifier(int i) {
			return getRuleContext(GomIdentifierContext.class,i);
		}
		public TerminalNode LDIPLE() { return getToken(TomJavaParser.LDIPLE, 0); }
		public TerminalNode RDIPLE() { return getToken(TomJavaParser.RDIPLE, 0); }
		public TerminalNode GOM_COLON() { return getToken(TomJavaParser.GOM_COLON, 0); }
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitField(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 348, RULE_field);
		try {
			setState(2642);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,302,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2624);
				((FieldContext)_localctx).type = gomIdentifier();
				setState(2625);
				match(GOM_STAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2627);
				match(LDIPLE);
				setState(2628);
				((FieldContext)_localctx).type = gomIdentifier();
				setState(2629);
				match(RDIPLE);
				setState(2630);
				match(GOM_STAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2632);
				((FieldContext)_localctx).name = gomIdentifier();
				setState(2633);
				match(GOM_COLON);
				setState(2634);
				((FieldContext)_localctx).type = gomIdentifier();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2636);
				((FieldContext)_localctx).name = gomIdentifier();
				setState(2637);
				match(GOM_COLON);
				setState(2638);
				match(LDIPLE);
				setState(2639);
				((FieldContext)_localctx).type = gomIdentifier();
				setState(2640);
				match(RDIPLE);
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

	public static class Pattern_fieldContext extends ParserRuleContext {
		public GomIdentifierContext type;
		public GomIdentifierContext name;
		public TerminalNode GOM_STAR() { return getToken(TomJavaParser.GOM_STAR, 0); }
		public List<GomIdentifierContext> gomIdentifier() {
			return getRuleContexts(GomIdentifierContext.class);
		}
		public GomIdentifierContext gomIdentifier(int i) {
			return getRuleContext(GomIdentifierContext.class,i);
		}
		public TerminalNode INNER() { return getToken(TomJavaParser.INNER, 0); }
		public TerminalNode GOM_COLON() { return getToken(TomJavaParser.GOM_COLON, 0); }
		public TerminalNode OUTER() { return getToken(TomJavaParser.OUTER, 0); }
		public TerminalNode NEUTRAL() { return getToken(TomJavaParser.NEUTRAL, 0); }
		public Pattern_fieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterPattern_field(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitPattern_field(this);
		}
	}

	public final Pattern_fieldContext pattern_field() throws RecognitionException {
		Pattern_fieldContext _localctx = new Pattern_fieldContext(_ctx, getState());
		enterRule(_localctx, 350, RULE_pattern_field);
		try {
			setState(2666);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,303,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2644);
				((Pattern_fieldContext)_localctx).type = gomIdentifier();
				setState(2645);
				match(GOM_STAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2647);
				match(INNER);
				setState(2648);
				((Pattern_fieldContext)_localctx).name = gomIdentifier();
				setState(2649);
				match(GOM_COLON);
				setState(2650);
				((Pattern_fieldContext)_localctx).type = gomIdentifier();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2652);
				match(OUTER);
				setState(2653);
				((Pattern_fieldContext)_localctx).name = gomIdentifier();
				setState(2654);
				match(GOM_COLON);
				setState(2655);
				((Pattern_fieldContext)_localctx).type = gomIdentifier();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2657);
				match(NEUTRAL);
				setState(2658);
				((Pattern_fieldContext)_localctx).name = gomIdentifier();
				setState(2659);
				match(GOM_COLON);
				setState(2660);
				((Pattern_fieldContext)_localctx).type = gomIdentifier();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2662);
				((Pattern_fieldContext)_localctx).name = gomIdentifier();
				setState(2663);
				match(GOM_COLON);
				setState(2664);
				((Pattern_fieldContext)_localctx).type = gomIdentifier();
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

	public static class ArglistContext extends ParserRuleContext {
		public TerminalNode GOM_LPAREN() { return getToken(TomJavaParser.GOM_LPAREN, 0); }
		public TerminalNode GOM_RPAREN() { return getToken(TomJavaParser.GOM_RPAREN, 0); }
		public List<GomIdentifierContext> gomIdentifier() {
			return getRuleContexts(GomIdentifierContext.class);
		}
		public GomIdentifierContext gomIdentifier(int i) {
			return getRuleContext(GomIdentifierContext.class,i);
		}
		public List<TerminalNode> GOM_COMMA() { return getTokens(TomJavaParser.GOM_COMMA); }
		public TerminalNode GOM_COMMA(int i) {
			return getToken(TomJavaParser.GOM_COMMA, i);
		}
		public ArglistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arglist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterArglist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitArglist(this);
		}
	}

	public final ArglistContext arglist() throws RecognitionException {
		ArglistContext _localctx = new ArglistContext(_ctx, getState());
		enterRule(_localctx, 352, RULE_arglist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2680);
			_la = _input.LA(1);
			if (_la==GOM_LPAREN) {
				{
				setState(2668);
				match(GOM_LPAREN);
				setState(2677);
				_la = _input.LA(1);
				if (((((_la - 177)) & ~0x3f) == 0 && ((1L << (_la - 177)) & ((1L << (AC - 177)) | (1L << (ACU - 177)) | (1L << (AU - 177)) | (1L << (FL - 177)) | (1L << (FREE - 177)) | (1L << (HOOK_MAKE - 177)) | (1L << (HOOK_MAKE_INSERT - 177)) | (1L << (HOOK_MAKE_EMPTY - 177)) | (1L << (HOOK_IMPORT - 177)) | (1L << (HOOK_INTERFACE - 177)) | (1L << (BLOCK - 177)) | (1L << (MAPPING - 177)) | (1L << (ID - 177)))) != 0)) {
					{
					setState(2669);
					gomIdentifier();
					setState(2674);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==GOM_COMMA) {
						{
						{
						setState(2670);
						match(GOM_COMMA);
						setState(2671);
						gomIdentifier();
						}
						}
						setState(2676);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(2679);
				match(GOM_RPAREN);
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

	public static class HookConstructContext extends ParserRuleContext {
		public GomIdentifierContext pointCut;
		public Token hookType;
		public TerminalNode GOM_COLON() { return getToken(TomJavaParser.GOM_COLON, 0); }
		public GomIdentifierContext gomIdentifier() {
			return getRuleContext(GomIdentifierContext.class,0);
		}
		public TerminalNode RULESTART() { return getToken(TomJavaParser.RULESTART, 0); }
		public RulesetContext ruleset() {
			return getRuleContext(RulesetContext.class,0);
		}
		public TerminalNode RULEEND() { return getToken(TomJavaParser.RULEEND, 0); }
		public TerminalNode RULE_LPAREN() { return getToken(TomJavaParser.RULE_LPAREN, 0); }
		public TerminalNode RULE_ARG() { return getToken(TomJavaParser.RULE_ARG, 0); }
		public TerminalNode ARG_COMMA() { return getToken(TomJavaParser.ARG_COMMA, 0); }
		public TerminalNode RULE_RPAREN() { return getToken(TomJavaParser.RULE_RPAREN, 0); }
		public GraphrulesetContext graphruleset() {
			return getRuleContext(GraphrulesetContext.class,0);
		}
		public HookBlockContext hookBlock() {
			return getRuleContext(HookBlockContext.class,0);
		}
		public ArglistContext arglist() {
			return getRuleContext(ArglistContext.class,0);
		}
		public HookScopeContext hookScope() {
			return getRuleContext(HookScopeContext.class,0);
		}
		public TerminalNode RULES() { return getToken(TomJavaParser.RULES, 0); }
		public TerminalNode GRAPHRULES() { return getToken(TomJavaParser.GRAPHRULES, 0); }
		public TerminalNode IDENTITY() { return getToken(TomJavaParser.IDENTITY, 0); }
		public TerminalNode FAIL() { return getToken(TomJavaParser.FAIL, 0); }
		public TerminalNode HOOK_IMPORT() { return getToken(TomJavaParser.HOOK_IMPORT, 0); }
		public TerminalNode HOOK_INTERFACE() { return getToken(TomJavaParser.HOOK_INTERFACE, 0); }
		public TerminalNode AC() { return getToken(TomJavaParser.AC, 0); }
		public TerminalNode FL() { return getToken(TomJavaParser.FL, 0); }
		public TerminalNode FREE() { return getToken(TomJavaParser.FREE, 0); }
		public TerminalNode ACU() { return getToken(TomJavaParser.ACU, 0); }
		public TerminalNode AU() { return getToken(TomJavaParser.AU, 0); }
		public TerminalNode HOOK_MAKE() { return getToken(TomJavaParser.HOOK_MAKE, 0); }
		public TerminalNode HOOK_MAKE_INSERT() { return getToken(TomJavaParser.HOOK_MAKE_INSERT, 0); }
		public TerminalNode HOOK_MAKE_EMPTY() { return getToken(TomJavaParser.HOOK_MAKE_EMPTY, 0); }
		public TerminalNode BLOCK() { return getToken(TomJavaParser.BLOCK, 0); }
		public TerminalNode MAPPING() { return getToken(TomJavaParser.MAPPING, 0); }
		public TerminalNode GOM_LPAREN() { return getToken(TomJavaParser.GOM_LPAREN, 0); }
		public TerminalNode GOM_RPAREN() { return getToken(TomJavaParser.GOM_RPAREN, 0); }
		public HookConstructContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hookConstruct; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterHookConstruct(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitHookConstruct(this);
		}
	}

	public final HookConstructContext hookConstruct() throws RecognitionException {
		HookConstructContext _localctx = new HookConstructContext(_ctx, getState());
		enterRule(_localctx, 354, RULE_hookConstruct);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2683);
			_la = _input.LA(1);
			if (((((_la - 151)) & ~0x3f) == 0 && ((1L << (_la - 151)) & ((1L << (MODULE - 151)) | (1L << (SORT - 151)) | (1L << (OPERATOR - 151)))) != 0)) {
				{
				setState(2682);
				hookScope();
				}
			}

			setState(2685);
			((HookConstructContext)_localctx).pointCut = gomIdentifier();
			setState(2686);
			match(GOM_COLON);
			setState(2740);
			switch (_input.LA(1)) {
			case RULES:
				{
				setState(2687);
				((HookConstructContext)_localctx).hookType = match(RULES);
				setState(2690);
				_la = _input.LA(1);
				if (_la==RULE_LPAREN) {
					{
					setState(2688);
					match(RULE_LPAREN);
					setState(2689);
					match(RULE_RPAREN);
					}
				}

				setState(2692);
				match(RULESTART);
				setState(2693);
				ruleset();
				setState(2694);
				match(RULEEND);
				}
				break;
			case GRAPHRULES:
				{
				setState(2696);
				((HookConstructContext)_localctx).hookType = match(GRAPHRULES);
				setState(2697);
				match(RULE_LPAREN);
				setState(2698);
				match(RULE_ARG);
				setState(2699);
				match(ARG_COMMA);
				setState(2700);
				_la = _input.LA(1);
				if ( !(_la==IDENTITY || _la==FAIL) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(2701);
				match(RULE_RPAREN);
				setState(2702);
				match(RULESTART);
				setState(2703);
				graphruleset();
				setState(2704);
				match(RULEEND);
				}
				break;
			case AC:
			case FL:
			case FREE:
				{
				setState(2706);
				((HookConstructContext)_localctx).hookType = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 177)) & ~0x3f) == 0 && ((1L << (_la - 177)) & ((1L << (AC - 177)) | (1L << (FL - 177)) | (1L << (FREE - 177)))) != 0)) ) {
					((HookConstructContext)_localctx).hookType = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(2709);
				_la = _input.LA(1);
				if (_la==GOM_LPAREN) {
					{
					setState(2707);
					match(GOM_LPAREN);
					setState(2708);
					match(GOM_RPAREN);
					}
				}

				setState(2711);
				hookBlock();
				}
				break;
			case ACU:
			case AU:
				{
				setState(2712);
				((HookConstructContext)_localctx).hookType = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ACU || _la==AU) ) {
					((HookConstructContext)_localctx).hookType = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(2715);
				_la = _input.LA(1);
				if (_la==GOM_LPAREN) {
					{
					setState(2713);
					match(GOM_LPAREN);
					setState(2714);
					match(GOM_RPAREN);
					}
				}

				setState(2717);
				hookBlock();
				}
				break;
			case HOOK_MAKE:
			case HOOK_MAKE_INSERT:
			case HOOK_MAKE_EMPTY:
				{
				setState(2718);
				((HookConstructContext)_localctx).hookType = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 182)) & ~0x3f) == 0 && ((1L << (_la - 182)) & ((1L << (HOOK_MAKE - 182)) | (1L << (HOOK_MAKE_INSERT - 182)) | (1L << (HOOK_MAKE_EMPTY - 182)))) != 0)) ) {
					((HookConstructContext)_localctx).hookType = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(2719);
				arglist();
				setState(2720);
				hookBlock();
				}
				break;
			case HOOK_IMPORT:
				{
				setState(2722);
				((HookConstructContext)_localctx).hookType = match(HOOK_IMPORT);
				setState(2725);
				_la = _input.LA(1);
				if (_la==GOM_LPAREN) {
					{
					setState(2723);
					match(GOM_LPAREN);
					setState(2724);
					match(GOM_RPAREN);
					}
				}

				setState(2727);
				hookBlock();
				}
				break;
			case HOOK_INTERFACE:
				{
				setState(2728);
				((HookConstructContext)_localctx).hookType = match(HOOK_INTERFACE);
				setState(2731);
				_la = _input.LA(1);
				if (_la==GOM_LPAREN) {
					{
					setState(2729);
					match(GOM_LPAREN);
					setState(2730);
					match(GOM_RPAREN);
					}
				}

				setState(2733);
				hookBlock();
				}
				break;
			case BLOCK:
			case MAPPING:
				{
				setState(2734);
				((HookConstructContext)_localctx).hookType = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==BLOCK || _la==MAPPING) ) {
					((HookConstructContext)_localctx).hookType = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(2737);
				_la = _input.LA(1);
				if (_la==GOM_LPAREN) {
					{
					setState(2735);
					match(GOM_LPAREN);
					setState(2736);
					match(GOM_RPAREN);
					}
				}

				setState(2739);
				hookBlock();
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

	public static class HookBlockContext extends ParserRuleContext {
		public TerminalNode HOOKSTART() { return getToken(TomJavaParser.HOOKSTART, 0); }
		public TerminalNode SUBBLOCKEND() { return getToken(TomJavaParser.SUBBLOCKEND, 0); }
		public List<UnknownBlockContext> unknownBlock() {
			return getRuleContexts(UnknownBlockContext.class);
		}
		public UnknownBlockContext unknownBlock(int i) {
			return getRuleContext(UnknownBlockContext.class,i);
		}
		public List<TerminalNode> SUB_ANY() { return getTokens(TomJavaParser.SUB_ANY); }
		public TerminalNode SUB_ANY(int i) {
			return getToken(TomJavaParser.SUB_ANY, i);
		}
		public HookBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hookBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterHookBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitHookBlock(this);
		}
	}

	public final HookBlockContext hookBlock() throws RecognitionException {
		HookBlockContext _localctx = new HookBlockContext(_ctx, getState());
		enterRule(_localctx, 356, RULE_hookBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2742);
			match(HOOKSTART);
			setState(2747);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,316,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(2745);
					switch (_input.LA(1)) {
					case BLOCKSTART:
					case SUBBLOCKSTART:
					case SUBSUBBLOCKSTART:
						{
						setState(2743);
						unknownBlock();
						}
						break;
					case SUB_ANY:
						{
						setState(2744);
						match(SUB_ANY);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(2749);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,316,_ctx);
			}
			setState(2750);
			match(SUBBLOCKEND);
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

	public static class RuleBlockContext extends ParserRuleContext {
		public RuleBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterRuleBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitRuleBlock(this);
		}
	}

	public final RuleBlockContext ruleBlock() throws RecognitionException {
		RuleBlockContext _localctx = new RuleBlockContext(_ctx, getState());
		enterRule(_localctx, 358, RULE_ruleBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
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

	public static class HookScopeContext extends ParserRuleContext {
		public TerminalNode SORT() { return getToken(TomJavaParser.SORT, 0); }
		public TerminalNode MODULE() { return getToken(TomJavaParser.MODULE, 0); }
		public TerminalNode OPERATOR() { return getToken(TomJavaParser.OPERATOR, 0); }
		public HookScopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hookScope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterHookScope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitHookScope(this);
		}
	}

	public final HookScopeContext hookScope() throws RecognitionException {
		HookScopeContext _localctx = new HookScopeContext(_ctx, getState());
		enterRule(_localctx, 360, RULE_hookScope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2754);
			_la = _input.LA(1);
			if ( !(((((_la - 151)) & ~0x3f) == 0 && ((1L << (_la - 151)) & ((1L << (MODULE - 151)) | (1L << (SORT - 151)) | (1L << (OPERATOR - 151)))) != 0)) ) {
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

	public static class GomIdentifierContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TomJavaParser.ID, 0); }
		public TerminalNode AC() { return getToken(TomJavaParser.AC, 0); }
		public TerminalNode FL() { return getToken(TomJavaParser.FL, 0); }
		public TerminalNode FREE() { return getToken(TomJavaParser.FREE, 0); }
		public TerminalNode ACU() { return getToken(TomJavaParser.ACU, 0); }
		public TerminalNode AU() { return getToken(TomJavaParser.AU, 0); }
		public TerminalNode HOOK_MAKE() { return getToken(TomJavaParser.HOOK_MAKE, 0); }
		public TerminalNode HOOK_MAKE_INSERT() { return getToken(TomJavaParser.HOOK_MAKE_INSERT, 0); }
		public TerminalNode HOOK_MAKE_EMPTY() { return getToken(TomJavaParser.HOOK_MAKE_EMPTY, 0); }
		public TerminalNode HOOK_IMPORT() { return getToken(TomJavaParser.HOOK_IMPORT, 0); }
		public TerminalNode HOOK_INTERFACE() { return getToken(TomJavaParser.HOOK_INTERFACE, 0); }
		public TerminalNode BLOCK() { return getToken(TomJavaParser.BLOCK, 0); }
		public TerminalNode MAPPING() { return getToken(TomJavaParser.MAPPING, 0); }
		public GomIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gomIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGomIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGomIdentifier(this);
		}
	}

	public final GomIdentifierContext gomIdentifier() throws RecognitionException {
		GomIdentifierContext _localctx = new GomIdentifierContext(_ctx, getState());
		enterRule(_localctx, 362, RULE_gomIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2756);
			_la = _input.LA(1);
			if ( !(((((_la - 177)) & ~0x3f) == 0 && ((1L << (_la - 177)) & ((1L << (AC - 177)) | (1L << (ACU - 177)) | (1L << (AU - 177)) | (1L << (FL - 177)) | (1L << (FREE - 177)) | (1L << (HOOK_MAKE - 177)) | (1L << (HOOK_MAKE_INSERT - 177)) | (1L << (HOOK_MAKE_EMPTY - 177)) | (1L << (HOOK_IMPORT - 177)) | (1L << (HOOK_INTERFACE - 177)) | (1L << (BLOCK - 177)) | (1L << (MAPPING - 177)) | (1L << (ID - 177)))) != 0)) ) {
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

	public static class RulesetContext extends ParserRuleContext {
		public List<TermruleContext> termrule() {
			return getRuleContexts(TermruleContext.class);
		}
		public TermruleContext termrule(int i) {
			return getRuleContext(TermruleContext.class,i);
		}
		public RulesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterRuleset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitRuleset(this);
		}
	}

	public final RulesetContext ruleset() throws RecognitionException {
		RulesetContext _localctx = new RulesetContext(_ctx, getState());
		enterRule(_localctx, 364, RULE_ruleset);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2761);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 206)) & ~0x3f) == 0 && ((1L << (_la - 206)) & ((1L << (RULE_UNDERSCORE - 206)) | (1L << (NOT - 206)) | (1L << (RULE_ID - 206)))) != 0)) {
				{
				{
				setState(2758);
				termrule();
				}
				}
				setState(2763);
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

	public static class TermruleContext extends ParserRuleContext {
		public Rule_patternContext rule_pattern() {
			return getRuleContext(Rule_patternContext.class,0);
		}
		public TerminalNode RULE_ARROW() { return getToken(TomJavaParser.RULE_ARROW, 0); }
		public Rule_termContext rule_term() {
			return getRuleContext(Rule_termContext.class,0);
		}
		public TerminalNode RULE_IF() { return getToken(TomJavaParser.RULE_IF, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TermruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termrule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterTermrule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitTermrule(this);
		}
	}

	public final TermruleContext termrule() throws RecognitionException {
		TermruleContext _localctx = new TermruleContext(_ctx, getState());
		enterRule(_localctx, 366, RULE_termrule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2764);
			rule_pattern();
			setState(2765);
			match(RULE_ARROW);
			setState(2766);
			rule_term();
			setState(2769);
			_la = _input.LA(1);
			if (_la==RULE_IF) {
				{
				setState(2767);
				match(RULE_IF);
				setState(2768);
				condition();
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

	public static class GraphrulesetContext extends ParserRuleContext {
		public List<GraphruleContext> graphrule() {
			return getRuleContexts(GraphruleContext.class);
		}
		public GraphruleContext graphrule(int i) {
			return getRuleContext(GraphruleContext.class,i);
		}
		public GraphrulesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_graphruleset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGraphruleset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGraphruleset(this);
		}
	}

	public final GraphrulesetContext graphruleset() throws RecognitionException {
		GraphrulesetContext _localctx = new GraphrulesetContext(_ctx, getState());
		enterRule(_localctx, 368, RULE_graphruleset);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2774);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 205)) & ~0x3f) == 0 && ((1L << (_la - 205)) & ((1L << (AMPERSAND - 205)) | (1L << (INTEGER - 205)) | (1L << (STRING - 205)) | (1L << (RULE_ID - 205)))) != 0)) {
				{
				{
				setState(2771);
				graphrule();
				}
				}
				setState(2776);
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

	public static class GraphruleContext extends ParserRuleContext {
		public LabelledpatternContext lhs;
		public LabelledpatternContext rhs;
		public TerminalNode RULE_ARROW() { return getToken(TomJavaParser.RULE_ARROW, 0); }
		public List<LabelledpatternContext> labelledpattern() {
			return getRuleContexts(LabelledpatternContext.class);
		}
		public LabelledpatternContext labelledpattern(int i) {
			return getRuleContext(LabelledpatternContext.class,i);
		}
		public TerminalNode RULE_IF() { return getToken(TomJavaParser.RULE_IF, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public GraphruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_graphrule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGraphrule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGraphrule(this);
		}
	}

	public final GraphruleContext graphrule() throws RecognitionException {
		GraphruleContext _localctx = new GraphruleContext(_ctx, getState());
		enterRule(_localctx, 370, RULE_graphrule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2777);
			((GraphruleContext)_localctx).lhs = labelledpattern();
			setState(2778);
			match(RULE_ARROW);
			setState(2779);
			((GraphruleContext)_localctx).rhs = labelledpattern();
			setState(2782);
			_la = _input.LA(1);
			if (_la==RULE_IF) {
				{
				setState(2780);
				match(RULE_IF);
				setState(2781);
				condition();
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

	public static class ConditionContext extends ParserRuleContext {
		public AndconditionContext cond;
		public List<AndconditionContext> andcondition() {
			return getRuleContexts(AndconditionContext.class);
		}
		public AndconditionContext andcondition(int i) {
			return getRuleContext(AndconditionContext.class,i);
		}
		public List<TerminalNode> RULE_OR() { return getTokens(TomJavaParser.RULE_OR); }
		public TerminalNode RULE_OR(int i) {
			return getToken(TomJavaParser.RULE_OR, i);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitCondition(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 372, RULE_condition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2784);
			((ConditionContext)_localctx).cond = andcondition();
			setState(2789);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==RULE_OR) {
				{
				{
				setState(2785);
				match(RULE_OR);
				setState(2786);
				andcondition();
				}
				}
				setState(2791);
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

	public static class AndconditionContext extends ParserRuleContext {
		public SimpleconditionContext cond;
		public List<SimpleconditionContext> simplecondition() {
			return getRuleContexts(SimpleconditionContext.class);
		}
		public SimpleconditionContext simplecondition(int i) {
			return getRuleContext(SimpleconditionContext.class,i);
		}
		public List<TerminalNode> RULE_AND() { return getTokens(TomJavaParser.RULE_AND); }
		public TerminalNode RULE_AND(int i) {
			return getToken(TomJavaParser.RULE_AND, i);
		}
		public AndconditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andcondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterAndcondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitAndcondition(this);
		}
	}

	public final AndconditionContext andcondition() throws RecognitionException {
		AndconditionContext _localctx = new AndconditionContext(_ctx, getState());
		enterRule(_localctx, 374, RULE_andcondition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2792);
			((AndconditionContext)_localctx).cond = simplecondition();
			setState(2797);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==RULE_AND) {
				{
				{
				setState(2793);
				match(RULE_AND);
				setState(2794);
				simplecondition();
				}
				}
				setState(2799);
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

	public static class SimpleconditionContext extends ParserRuleContext {
		public Rule_termContext lterm;
		public Rule_termContext rterm;
		public List<Rule_termContext> rule_term() {
			return getRuleContexts(Rule_termContext.class);
		}
		public Rule_termContext rule_term(int i) {
			return getRuleContext(Rule_termContext.class,i);
		}
		public TerminalNode RULE_EQUALS() { return getToken(TomJavaParser.RULE_EQUALS, 0); }
		public TerminalNode NOTEQUALS() { return getToken(TomJavaParser.NOTEQUALS, 0); }
		public TerminalNode LEQ() { return getToken(TomJavaParser.LEQ, 0); }
		public TerminalNode RULE_LT() { return getToken(TomJavaParser.RULE_LT, 0); }
		public TerminalNode GEQ() { return getToken(TomJavaParser.GEQ, 0); }
		public TerminalNode RULE_GT() { return getToken(TomJavaParser.RULE_GT, 0); }
		public TerminalNode MATCH_SYMBOL() { return getToken(TomJavaParser.MATCH_SYMBOL, 0); }
		public TerminalNode LPAR() { return getToken(TomJavaParser.LPAR, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(TomJavaParser.RPAR, 0); }
		public SimpleconditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simplecondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterSimplecondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitSimplecondition(this);
		}
	}

	public final SimpleconditionContext simplecondition() throws RecognitionException {
		SimpleconditionContext _localctx = new SimpleconditionContext(_ctx, getState());
		enterRule(_localctx, 376, RULE_simplecondition);
		int _la;
		try {
			setState(2808);
			switch (_input.LA(1)) {
			case RULE_UNDERSCORE:
			case NOT:
			case INTEGER:
			case STRING:
			case RULE_ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(2800);
				((SimpleconditionContext)_localctx).lterm = rule_term();
				setState(2801);
				_la = _input.LA(1);
				if ( !(((((_la - 216)) & ~0x3f) == 0 && ((1L << (_la - 216)) & ((1L << (RULE_EQUALS - 216)) | (1L << (NOTEQUALS - 216)) | (1L << (LEQ - 216)) | (1L << (MATCH_SYMBOL - 216)) | (1L << (RULE_LT - 216)) | (1L << (GEQ - 216)) | (1L << (RULE_GT - 216)))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(2802);
				((SimpleconditionContext)_localctx).rterm = rule_term();
				}
				break;
			case LPAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(2804);
				match(LPAR);
				setState(2805);
				condition();
				setState(2806);
				match(RPAR);
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

	public static class Rule_patternContext extends ParserRuleContext {
		public Token funname;
		public Token varname;
		public TerminalNode LPAR() { return getToken(TomJavaParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(TomJavaParser.RPAR, 0); }
		public List<TerminalNode> RULE_ID() { return getTokens(TomJavaParser.RULE_ID); }
		public TerminalNode RULE_ID(int i) {
			return getToken(TomJavaParser.RULE_ID, i);
		}
		public List<Rule_termContext> rule_term() {
			return getRuleContexts(Rule_termContext.class);
		}
		public Rule_termContext rule_term(int i) {
			return getRuleContext(Rule_termContext.class,i);
		}
		public List<TerminalNode> RULE_COMMA() { return getTokens(TomJavaParser.RULE_COMMA); }
		public TerminalNode RULE_COMMA(int i) {
			return getToken(TomJavaParser.RULE_COMMA, i);
		}
		public TerminalNode RULE_AT() { return getToken(TomJavaParser.RULE_AT, 0); }
		public TerminalNode RULE_UNDERSCORE() { return getToken(TomJavaParser.RULE_UNDERSCORE, 0); }
		public TerminalNode RULE_STAR() { return getToken(TomJavaParser.RULE_STAR, 0); }
		public TerminalNode NOT() { return getToken(TomJavaParser.NOT, 0); }
		public Rule_patternContext rule_pattern() {
			return getRuleContext(Rule_patternContext.class,0);
		}
		public Rule_patternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rule_pattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterRule_pattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitRule_pattern(this);
		}
	}

	public final Rule_patternContext rule_pattern() throws RecognitionException {
		Rule_patternContext _localctx = new Rule_patternContext(_ctx, getState());
		enterRule(_localctx, 378, RULE_rule_pattern);
		int _la;
		try {
			setState(2844);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,329,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2810);
				((Rule_patternContext)_localctx).funname = match(RULE_ID);
				setState(2811);
				match(LPAR);
				setState(2820);
				_la = _input.LA(1);
				if (((((_la - 206)) & ~0x3f) == 0 && ((1L << (_la - 206)) & ((1L << (RULE_UNDERSCORE - 206)) | (1L << (NOT - 206)) | (1L << (INTEGER - 206)) | (1L << (STRING - 206)) | (1L << (RULE_ID - 206)))) != 0)) {
					{
					setState(2812);
					rule_term();
					setState(2817);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==RULE_COMMA) {
						{
						{
						setState(2813);
						match(RULE_COMMA);
						setState(2814);
						rule_term();
						}
						}
						setState(2819);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(2822);
				match(RPAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(2823);
				((Rule_patternContext)_localctx).varname = match(RULE_ID);
				}
				setState(2824);
				match(RULE_AT);
				{
				setState(2825);
				((Rule_patternContext)_localctx).funname = match(RULE_ID);
				}
				setState(2826);
				match(LPAR);
				setState(2835);
				_la = _input.LA(1);
				if (((((_la - 206)) & ~0x3f) == 0 && ((1L << (_la - 206)) & ((1L << (RULE_UNDERSCORE - 206)) | (1L << (NOT - 206)) | (1L << (INTEGER - 206)) | (1L << (STRING - 206)) | (1L << (RULE_ID - 206)))) != 0)) {
					{
					setState(2827);
					rule_term();
					setState(2832);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==RULE_COMMA) {
						{
						{
						setState(2828);
						match(RULE_COMMA);
						setState(2829);
						rule_term();
						}
						}
						setState(2834);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(2837);
				match(RPAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2838);
				match(RULE_UNDERSCORE);
				setState(2840);
				_la = _input.LA(1);
				if (_la==RULE_STAR) {
					{
					setState(2839);
					match(RULE_STAR);
					}
				}

				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2842);
				match(NOT);
				setState(2843);
				rule_pattern();
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

	public static class Rule_termContext extends ParserRuleContext {
		public Rule_patternContext rule_pattern() {
			return getRuleContext(Rule_patternContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public BuiltinContext builtin() {
			return getRuleContext(BuiltinContext.class,0);
		}
		public Rule_termContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rule_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterRule_term(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitRule_term(this);
		}
	}

	public final Rule_termContext rule_term() throws RecognitionException {
		Rule_termContext _localctx = new Rule_termContext(_ctx, getState());
		enterRule(_localctx, 380, RULE_rule_term);
		try {
			setState(2849);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,330,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2846);
				rule_pattern();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2847);
				variable();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2848);
				builtin();
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

	public static class BuiltinContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(TomJavaParser.INTEGER, 0); }
		public TerminalNode STRING() { return getToken(TomJavaParser.STRING, 0); }
		public BuiltinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_builtin; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterBuiltin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitBuiltin(this);
		}
	}

	public final BuiltinContext builtin() throws RecognitionException {
		BuiltinContext _localctx = new BuiltinContext(_ctx, getState());
		enterRule(_localctx, 382, RULE_builtin);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2851);
			_la = _input.LA(1);
			if ( !(_la==INTEGER || _la==STRING) ) {
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

	public static class LabelledpatternContext extends ParserRuleContext {
		public GraphpatternContext graphpattern() {
			return getRuleContext(GraphpatternContext.class,0);
		}
		public TerminalNode RULE_ID() { return getToken(TomJavaParser.RULE_ID, 0); }
		public TerminalNode RULE_COLON() { return getToken(TomJavaParser.RULE_COLON, 0); }
		public LabelledpatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labelledpattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterLabelledpattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitLabelledpattern(this);
		}
	}

	public final LabelledpatternContext labelledpattern() throws RecognitionException {
		LabelledpatternContext _localctx = new LabelledpatternContext(_ctx, getState());
		enterRule(_localctx, 384, RULE_labelledpattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2855);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,331,_ctx) ) {
			case 1:
				{
				setState(2853);
				match(RULE_ID);
				setState(2854);
				match(RULE_COLON);
				}
				break;
			}
			setState(2857);
			graphpattern();
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

	public static class GraphpatternContext extends ParserRuleContext {
		public ConstructorContext constructor() {
			return getRuleContext(ConstructorContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public BuiltinContext builtin() {
			return getRuleContext(BuiltinContext.class,0);
		}
		public RefContext ref() {
			return getRuleContext(RefContext.class,0);
		}
		public GraphpatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_graphpattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterGraphpattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitGraphpattern(this);
		}
	}

	public final GraphpatternContext graphpattern() throws RecognitionException {
		GraphpatternContext _localctx = new GraphpatternContext(_ctx, getState());
		enterRule(_localctx, 386, RULE_graphpattern);
		try {
			setState(2863);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,332,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2859);
				constructor();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2860);
				variable();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2861);
				builtin();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2862);
				ref();
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

	public static class VariableContext extends ParserRuleContext {
		public TerminalNode RULE_ID() { return getToken(TomJavaParser.RULE_ID, 0); }
		public TerminalNode RULE_STAR() { return getToken(TomJavaParser.RULE_STAR, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitVariable(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 388, RULE_variable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2865);
			match(RULE_ID);
			setState(2867);
			_la = _input.LA(1);
			if (_la==RULE_STAR) {
				{
				setState(2866);
				match(RULE_STAR);
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

	public static class RefContext extends ParserRuleContext {
		public TerminalNode AMPERSAND() { return getToken(TomJavaParser.AMPERSAND, 0); }
		public TerminalNode RULE_ID() { return getToken(TomJavaParser.RULE_ID, 0); }
		public RefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ref; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitRef(this);
		}
	}

	public final RefContext ref() throws RecognitionException {
		RefContext _localctx = new RefContext(_ctx, getState());
		enterRule(_localctx, 390, RULE_ref);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2869);
			match(AMPERSAND);
			setState(2870);
			match(RULE_ID);
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

	public static class ConstructorContext extends ParserRuleContext {
		public TerminalNode RULE_ID() { return getToken(TomJavaParser.RULE_ID, 0); }
		public TerminalNode LPAR() { return getToken(TomJavaParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(TomJavaParser.RPAR, 0); }
		public List<LabelledpatternContext> labelledpattern() {
			return getRuleContexts(LabelledpatternContext.class);
		}
		public LabelledpatternContext labelledpattern(int i) {
			return getRuleContext(LabelledpatternContext.class,i);
		}
		public List<TerminalNode> RULE_COMMA() { return getTokens(TomJavaParser.RULE_COMMA); }
		public TerminalNode RULE_COMMA(int i) {
			return getToken(TomJavaParser.RULE_COMMA, i);
		}
		public ConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).enterConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomJavaParserListener ) ((TomJavaParserListener)listener).exitConstructor(this);
		}
	}

	public final ConstructorContext constructor() throws RecognitionException {
		ConstructorContext _localctx = new ConstructorContext(_ctx, getState());
		enterRule(_localctx, 392, RULE_constructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2872);
			match(RULE_ID);
			setState(2873);
			match(LPAR);
			setState(2882);
			_la = _input.LA(1);
			if (((((_la - 205)) & ~0x3f) == 0 && ((1L << (_la - 205)) & ((1L << (AMPERSAND - 205)) | (1L << (INTEGER - 205)) | (1L << (STRING - 205)) | (1L << (RULE_ID - 205)))) != 0)) {
				{
				setState(2874);
				labelledpattern();
				setState(2879);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==RULE_COMMA) {
					{
					{
					setState(2875);
					match(RULE_COMMA);
					setState(2876);
					labelledpattern();
					}
					}
					setState(2881);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2884);
			match(RPAR);
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
		case 84:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 85:
			return funTerm_sempred((FunTermContext)_localctx, predIndex);
		case 126:
			return constraint_sempred((ConstraintContext)_localctx, predIndex);
		case 131:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\u00f4\u0b49\4\2\t"+
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
		"\4\u009c\t\u009c\4\u009d\t\u009d\4\u009e\t\u009e\4\u009f\t\u009f\4\u00a0"+
		"\t\u00a0\4\u00a1\t\u00a1\4\u00a2\t\u00a2\4\u00a3\t\u00a3\4\u00a4\t\u00a4"+
		"\4\u00a5\t\u00a5\4\u00a6\t\u00a6\4\u00a7\t\u00a7\4\u00a8\t\u00a8\4\u00a9"+
		"\t\u00a9\4\u00aa\t\u00aa\4\u00ab\t\u00ab\4\u00ac\t\u00ac\4\u00ad\t\u00ad"+
		"\4\u00ae\t\u00ae\4\u00af\t\u00af\4\u00b0\t\u00b0\4\u00b1\t\u00b1\4\u00b2"+
		"\t\u00b2\4\u00b3\t\u00b3\4\u00b4\t\u00b4\4\u00b5\t\u00b5\4\u00b6\t\u00b6"+
		"\4\u00b7\t\u00b7\4\u00b8\t\u00b8\4\u00b9\t\u00b9\4\u00ba\t\u00ba\4\u00bb"+
		"\t\u00bb\4\u00bc\t\u00bc\4\u00bd\t\u00bd\4\u00be\t\u00be\4\u00bf\t\u00bf"+
		"\4\u00c0\t\u00c0\4\u00c1\t\u00c1\4\u00c2\t\u00c2\4\u00c3\t\u00c3\4\u00c4"+
		"\t\u00c4\4\u00c5\t\u00c5\4\u00c6\t\u00c6\3\2\5\2\u018e\n\2\3\2\7\2\u0191"+
		"\n\2\f\2\16\2\u0194\13\2\3\2\7\2\u0197\n\2\f\2\16\2\u019a\13\2\3\2\3\2"+
		"\3\3\7\3\u019f\n\3\f\3\16\3\u01a2\13\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\6\7\6\u01ad\n\6\f\6\16\6\u01b0\13\6\3\6\3\6\3\6\3\6\3\7\3\7\5\7\u01b8"+
		"\n\7\3\7\3\7\3\7\5\7\u01bd\n\7\3\7\3\7\3\b\7\b\u01c2\n\b\f\b\16\b\u01c5"+
		"\13\b\3\b\3\b\3\b\3\b\5\b\u01cb\n\b\3\b\5\b\u01ce\n\b\3\t\3\t\3\t\3\t"+
		"\3\t\5\t\u01d5\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u01df\n\n\3\13"+
		"\3\13\5\13\u01e3\n\13\3\f\3\f\3\f\5\f\u01e8\n\f\3\f\3\f\5\f\u01ec\n\f"+
		"\3\f\3\f\5\f\u01f0\n\f\3\f\3\f\3\r\3\r\3\r\3\r\7\r\u01f8\n\r\f\r\16\r"+
		"\u01fb\13\r\3\r\3\r\3\16\7\16\u0200\n\16\f\16\16\16\u0203\13\16\3\16\3"+
		"\16\3\16\5\16\u0208\n\16\3\17\3\17\3\17\7\17\u020d\n\17\f\17\16\17\u0210"+
		"\13\17\3\20\3\20\3\20\3\20\5\20\u0216\n\20\3\20\3\20\5\20\u021a\n\20\3"+
		"\20\5\20\u021d\n\20\3\20\5\20\u0220\n\20\3\20\3\20\3\21\3\21\3\21\7\21"+
		"\u0227\n\21\f\21\16\21\u022a\13\21\3\22\7\22\u022d\n\22\f\22\16\22\u0230"+
		"\13\22\3\22\3\22\5\22\u0234\n\22\3\22\5\22\u0237\n\22\3\23\3\23\7\23\u023b"+
		"\n\23\f\23\16\23\u023e\13\23\3\24\3\24\3\24\5\24\u0243\n\24\3\24\3\24"+
		"\5\24\u0247\n\24\3\24\3\24\3\25\3\25\7\25\u024d\n\25\f\25\16\25\u0250"+
		"\13\25\3\25\3\25\3\26\3\26\7\26\u0256\n\26\f\26\16\26\u0259\13\26\3\26"+
		"\3\26\3\27\3\27\5\27\u025f\n\27\3\27\3\27\7\27\u0263\n\27\f\27\16\27\u0266"+
		"\13\27\3\27\3\27\5\27\u026a\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3"+
		"\30\3\30\5\30\u0275\n\30\3\31\3\31\3\31\3\31\3\31\7\31\u027c\n\31\f\31"+
		"\16\31\u027f\13\31\3\31\3\31\5\31\u0283\n\31\3\31\3\31\3\32\3\32\5\32"+
		"\u0289\n\32\3\33\3\33\5\33\u028d\n\33\3\34\3\34\3\34\3\35\3\35\3\35\3"+
		"\36\3\36\3\36\3\36\5\36\u0299\n\36\3\36\3\36\3\37\3\37\3\37\3\37\3 \7"+
		" \u02a2\n \f \16 \u02a5\13 \3 \3 \3 \5 \u02aa\n \3!\3!\3!\3!\3!\3!\3!"+
		"\5!\u02b3\n!\3\"\3\"\3\"\3\"\7\"\u02b9\n\"\f\"\16\"\u02bc\13\"\3\"\3\""+
		"\3#\3#\3#\7#\u02c3\n#\f#\16#\u02c6\13#\3#\3#\3#\3$\7$\u02cc\n$\f$\16$"+
		"\u02cf\13$\3$\3$\3$\7$\u02d4\n$\f$\16$\u02d7\13$\3$\3$\5$\u02db\n$\3$"+
		"\3$\3$\3$\7$\u02e1\n$\f$\16$\u02e4\13$\3$\3$\5$\u02e8\n$\3$\3$\3%\3%\3"+
		"%\3%\3%\3%\5%\u02f2\n%\3&\3&\3&\3\'\3\'\3\'\7\'\u02fa\n\'\f\'\16\'\u02fd"+
		"\13\'\3(\3(\3(\5(\u0302\n(\3)\3)\3)\7)\u0307\n)\f)\16)\u030a\13)\3*\3"+
		"*\5*\u030e\n*\3+\3+\3+\3+\7+\u0314\n+\f+\16+\u0317\13+\3+\5+\u031a\n+"+
		"\5+\u031c\n+\3+\3+\3,\3,\5,\u0322\n,\3,\3,\3,\5,\u0327\n,\7,\u0329\n,"+
		"\f,\16,\u032c\13,\3-\3-\3-\3-\5-\u0332\n-\5-\u0334\n-\3.\3.\3.\7.\u0339"+
		"\n.\f.\16.\u033c\13.\3/\3/\5/\u0340\n/\3/\3/\3\60\3\60\3\60\7\60\u0347"+
		"\n\60\f\60\16\60\u034a\13\60\3\60\3\60\5\60\u034e\n\60\3\60\5\60\u0351"+
		"\n\60\3\61\7\61\u0354\n\61\f\61\16\61\u0357\13\61\3\61\3\61\3\61\3\62"+
		"\7\62\u035d\n\62\f\62\16\62\u0360\13\62\3\62\3\62\3\62\3\62\3\63\3\63"+
		"\3\63\7\63\u0369\n\63\f\63\16\63\u036c\13\63\3\64\3\64\3\64\3\64\3\64"+
		"\3\64\5\64\u0374\n\64\3\65\3\65\3\66\3\66\3\67\3\67\3\67\3\67\3\67\5\67"+
		"\u037f\n\67\3\67\5\67\u0382\n\67\38\38\38\78\u0387\n8\f8\168\u038a\13"+
		"8\39\39\39\39\3:\3:\3:\5:\u0393\n:\3;\3;\3;\3;\7;\u0399\n;\f;\16;\u039c"+
		"\13;\5;\u039e\n;\3;\5;\u03a1\n;\3;\3;\3<\3<\3<\3<\3<\3=\3=\7=\u03ac\n"+
		"=\f=\16=\u03af\13=\3=\3=\3>\7>\u03b4\n>\f>\16>\u03b7\13>\3>\3>\3>\5>\u03bc"+
		"\n>\3?\3?\3?\3?\3?\3?\5?\u03c4\n?\3?\3?\5?\u03c8\n?\3?\3?\5?\u03cc\n?"+
		"\3?\3?\5?\u03d0\n?\5?\u03d2\n?\3@\3@\5@\u03d6\n@\3A\3A\3A\3A\5A\u03dc"+
		"\nA\3B\3B\3C\3C\3C\3D\3D\7D\u03e5\nD\fD\16D\u03e8\13D\3D\3D\3E\3E\3E\3"+
		"E\3E\3E\5E\u03f2\nE\3F\7F\u03f5\nF\fF\16F\u03f8\13F\3F\3F\3F\3G\7G\u03fe"+
		"\nG\fG\16G\u0401\13G\3G\3G\5G\u0405\nG\3G\5G\u0408\nG\3H\3H\3H\3H\3H\5"+
		"H\u040f\nH\3H\3H\3H\3H\3H\3H\3H\5H\u0418\nH\3H\3H\3H\3H\3H\3H\3H\3H\3"+
		"H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\6H\u042d\nH\rH\16H\u042e\3H\5H\u0432\n"+
		"H\3H\5H\u0435\nH\3H\3H\3H\3H\7H\u043b\nH\fH\16H\u043e\13H\3H\5H\u0441"+
		"\nH\3H\3H\3H\3H\7H\u0447\nH\fH\16H\u044a\13H\3H\7H\u044d\nH\fH\16H\u0450"+
		"\13H\3H\3H\3H\3H\3H\3H\3H\3H\5H\u045a\nH\3H\3H\3H\3H\3H\3H\3H\5H\u0463"+
		"\nH\3H\3H\3H\5H\u0468\nH\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\5H\u0474\nH\3I"+
		"\3I\3I\7I\u0479\nI\fI\16I\u047c\13I\3I\3I\3I\3I\3I\3J\3J\3J\7J\u0486\n"+
		"J\fJ\16J\u0489\13J\3K\3K\3K\3L\3L\3L\5L\u0491\nL\3L\3L\3M\3M\3M\7M\u0498"+
		"\nM\fM\16M\u049b\13M\3N\7N\u049e\nN\fN\16N\u04a1\13N\3N\3N\3N\3N\3N\3"+
		"O\6O\u04a9\nO\rO\16O\u04aa\3O\6O\u04ae\nO\rO\16O\u04af\3P\3P\3P\5P\u04b5"+
		"\nP\3P\3P\3P\3P\5P\u04bb\nP\3Q\3Q\5Q\u04bf\nQ\3Q\3Q\5Q\u04c3\nQ\3Q\3Q"+
		"\5Q\u04c7\nQ\5Q\u04c9\nQ\3R\3R\5R\u04cd\nR\3S\7S\u04d0\nS\fS\16S\u04d3"+
		"\13S\3S\3S\3S\3S\3S\3T\3T\3T\3T\3U\3U\3U\7U\u04e1\nU\fU\16U\u04e4\13U"+
		"\3V\3V\3V\3V\3V\5V\u04eb\nV\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V"+
		"\3V\3V\3V\5V\u04fe\nV\3V\3V\5V\u0502\nV\3V\3V\3V\5V\u0507\nV\3V\3V\3V"+
		"\5V\u050c\nV\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\5V\u051b\nV\3V\3V"+
		"\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V"+
		"\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\5V\u0542\nV\3V\3V\3V\3V\3V\3V\3V"+
		"\5V\u054b\nV\3V\3V\5V\u054f\nV\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V\3V"+
		"\5V\u055e\nV\3V\3V\3V\5V\u0563\nV\3V\5V\u0566\nV\7V\u0568\nV\fV\16V\u056b"+
		"\13V\3W\3W\3W\3W\3W\3W\3W\3W\5W\u0575\nW\3W\3W\3W\3W\3W\3W\5W\u057d\n"+
		"W\3W\3W\5W\u0581\nW\3W\3W\3W\5W\u0586\nW\3W\3W\5W\u058a\nW\3W\3W\3W\3"+
		"W\3W\3W\3W\3W\5W\u0594\nW\3W\3W\3W\7W\u0599\nW\fW\16W\u059c\13W\3X\3X"+
		"\3X\3X\3Y\3Y\3Y\5Y\u05a5\nY\3Y\3Y\3Y\3Y\3Y\7Y\u05ac\nY\fY\16Y\u05af\13"+
		"Y\3Y\3Y\5Y\u05b3\nY\3Z\3Z\5Z\u05b7\nZ\3[\3[\3[\3[\3[\3[\3[\3[\3[\3[\3"+
		"[\3[\3[\3[\3[\3[\5[\u05c9\n[\5[\u05cb\n[\3\\\3\\\3\\\5\\\u05d0\n\\\3\\"+
		"\7\\\u05d3\n\\\f\\\16\\\u05d6\13\\\3\\\3\\\5\\\u05da\n\\\3]\3]\3]\3]\3"+
		"]\3]\3]\5]\u05e3\n]\5]\u05e5\n]\3^\3^\5^\u05e9\n^\3^\3^\3^\5^\u05ee\n"+
		"^\7^\u05f0\n^\f^\16^\u05f3\13^\3^\5^\u05f6\n^\3_\3_\5_\u05fa\n_\3_\3_"+
		"\3`\3`\3`\3`\7`\u0602\n`\f`\16`\u0605\13`\3`\3`\3`\3`\3`\3`\3`\7`\u060e"+
		"\n`\f`\16`\u0611\13`\3`\3`\7`\u0615\n`\f`\16`\u0618\13`\5`\u061a\n`\3"+
		"a\3a\5a\u061e\na\3b\3b\3b\3c\3c\3c\5c\u0626\nc\3d\3d\3d\5d\u062b\nd\3"+
		"e\3e\3e\3e\3f\3f\3f\7f\u0634\nf\ff\16f\u0637\13f\3g\5g\u063a\ng\3g\3g"+
		"\5g\u063e\ng\3g\3g\7g\u0642\ng\fg\16g\u0645\13g\3h\3h\3i\3i\3i\3i\7i\u064d"+
		"\ni\fi\16i\u0650\13i\3i\3i\3j\3j\3j\3j\5j\u0658\nj\5j\u065a\nj\3k\3k\3"+
		"k\3k\3k\5k\u0661\nk\3l\3l\5l\u0665\nl\3l\3l\3m\3m\3n\3n\3n\3n\3n\3n\3"+
		"n\3n\5n\u0673\nn\3o\3o\3p\3p\5p\u0679\np\3q\3q\3r\3r\3r\3r\3r\7r\u0682"+
		"\nr\fr\16r\u0685\13r\5r\u0687\nr\3r\5r\u068a\nr\3r\3r\7r\u068e\nr\fr\16"+
		"r\u0691\13r\3r\3r\3s\3s\3s\3s\5s\u0699\ns\3s\3s\3s\3s\3s\7s\u06a0\ns\f"+
		"s\16s\u06a3\13s\3s\3s\3t\3t\3t\7t\u06aa\nt\ft\16t\u06ad\13t\3t\7t\u06b0"+
		"\nt\ft\16t\u06b3\13t\3t\3t\3t\7t\u06b8\nt\ft\16t\u06bb\13t\3t\3t\3u\3"+
		"u\5u\u06c1\nu\3u\3u\3v\3v\3v\3v\7v\u06c9\nv\fv\16v\u06cc\13v\3v\3v\3w"+
		"\3w\3w\3w\3x\3x\3x\3y\3y\3y\7y\u06da\ny\fy\16y\u06dd\13y\3y\3y\3y\3y\7"+
		"y\u06e3\ny\fy\16y\u06e6\13y\3y\5y\u06e9\ny\3z\3z\3z\3z\7z\u06ef\nz\fz"+
		"\16z\u06f2\13z\3z\3z\3{\3{\3{\5{\u06f9\n{\3{\3{\3{\3{\3{\3{\5{\u0701\n"+
		"{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\5{\u070e\n{\3|\3|\3|\7|\u0713\n|\f"+
		"|\16|\u0716\13|\3|\3|\3}\3}\3}\7}\u071d\n}\f}\16}\u0720\13}\3~\3~\5~\u0724"+
		"\n~\3~\3~\3\177\3\177\3\177\7\177\u072b\n\177\f\177\16\177\u072e\13\177"+
		"\3\177\3\177\3\177\3\177\7\177\u0734\n\177\f\177\16\177\u0737\13\177\5"+
		"\177\u0739\n\177\3\177\3\177\3\177\3\177\7\177\u073f\n\177\f\177\16\177"+
		"\u0742\13\177\3\177\3\177\3\177\3\177\3\177\3\177\7\177\u074a\n\177\f"+
		"\177\16\177\u074d\13\177\3\177\3\177\5\177\u0751\n\177\5\177\u0753\n\177"+
		"\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080"+
		"\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080"+
		"\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080"+
		"\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\5\u0080\u0776\n\u0080"+
		"\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\7\u0080\u077e\n\u0080"+
		"\f\u0080\16\u0080\u0781\13\u0080\3\u0081\3\u0081\5\u0081\u0785\n\u0081"+
		"\3\u0081\3\u0081\3\u0081\3\u0081\3\u0081\7\u0081\u078c\n\u0081\f\u0081"+
		"\16\u0081\u078f\13\u0081\5\u0081\u0791\n\u0081\3\u0081\3\u0081\3\u0081"+
		"\5\u0081\u0796\n\u0081\3\u0082\5\u0082\u0799\n\u0082\3\u0082\5\u0082\u079c"+
		"\n\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\7\u0082\u07a3\n\u0082"+
		"\f\u0082\16\u0082\u07a6\13\u0082\5\u0082\u07a8\n\u0082\3\u0082\3\u0082"+
		"\3\u0082\5\u0082\u07ad\n\u0082\3\u0082\5\u0082\u07b0\n\u0082\3\u0082\3"+
		"\u0082\3\u0082\3\u0082\3\u0082\7\u0082\u07b7\n\u0082\f\u0082\16\u0082"+
		"\u07ba\13\u0082\5\u0082\u07bc\n\u0082\3\u0082\3\u0082\3\u0082\5\u0082"+
		"\u07c1\n\u0082\3\u0082\5\u0082\u07c4\n\u0082\3\u0082\3\u0082\5\u0082\u07c8"+
		"\n\u0082\3\u0082\5\u0082\u07cb\n\u0082\3\u0082\3\u0082\5\u0082\u07cf\n"+
		"\u0082\3\u0083\3\u0083\3\u0083\3\u0083\3\u0084\3\u0084\3\u0084\3\u0084"+
		"\3\u0084\3\u0084\7\u0084\u07db\n\u0084\f\u0084\16\u0084\u07de\13\u0084"+
		"\5\u0084\u07e0\n\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084"+
		"\3\u0084\3\u0084\7\u0084\u07ea\n\u0084\f\u0084\16\u0084\u07ed\13\u0084"+
		"\5\u0084\u07ef\n\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084"+
		"\3\u0084\3\u0084\3\u0084\3\u0084\5\u0084\u07fb\n\u0084\5\u0084\u07fd\n"+
		"\u0084\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\7\u0085\u0805\n"+
		"\u0085\f\u0085\16\u0085\u0808\13\u0085\5\u0085\u080a\n\u0085\3\u0085\3"+
		"\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\5\u0085\u0814\n"+
		"\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\5\u0085\u0828\n\u0085\3\u0085\3\u0085\5\u0085\u082c\n\u0085\3"+
		"\u0085\3\u0085\3\u0085\5\u0085\u0831\n\u0085\3\u0085\3\u0085\5\u0085\u0835"+
		"\n\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\5\u0085\u0844\n\u0085\3\u0085"+
		"\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\7\u0085"+
		"\u086a\n\u0085\f\u0085\16\u0085\u086d\13\u0085\5\u0085\u086f\n\u0085\3"+
		"\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\5\u0085\u0878\n"+
		"\u0085\3\u0085\3\u0085\3\u0085\3\u0085\5\u0085\u087e\n\u0085\3\u0085\3"+
		"\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\3\u0085\3\u0085\3\u0085\5\u0085\u088d\n\u0085\3\u0085\7\u0085\u0890\n"+
		"\u0085\f\u0085\16\u0085\u0893\13\u0085\3\u0086\3\u0086\3\u0086\3\u0086"+
		"\3\u0086\3\u0086\3\u0086\3\u0086\3\u0086\3\u0086\3\u0086\3\u0086\3\u0086"+
		"\3\u0086\5\u0086\u08a3\n\u0086\3\u0086\3\u0086\5\u0086\u08a7\n\u0086\3"+
		"\u0086\3\u0086\3\u0086\7\u0086\u08ac\n\u0086\f\u0086\16\u0086\u08af\13"+
		"\u0086\5\u0086\u08b1\n\u0086\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\6"+
		"\u0087\u08b8\n\u0087\r\u0087\16\u0087\u08b9\3\u0087\3\u0087\5\u0087\u08be"+
		"\n\u0087\3\u0088\3\u0088\5\u0088\u08c2\n\u0088\3\u0088\3\u0088\5\u0088"+
		"\u08c6\n\u0088\3\u0088\5\u0088\u08c9\n\u0088\3\u0089\5\u0089\u08cc\n\u0089"+
		"\3\u0089\3\u0089\5\u0089\u08d0\n\u0089\3\u0089\3\u0089\3\u0089\3\u0089"+
		"\5\u0089\u08d6\n\u0089\3\u008a\3\u008a\3\u008a\3\u008a\7\u008a\u08dc\n"+
		"\u008a\f\u008a\16\u008a\u08df\13\u008a\5\u008a\u08e1\n\u008a\3\u008a\3"+
		"\u008a\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b"+
		"\3\u008b\7\u008b\u08ee\n\u008b\f\u008b\16\u008b\u08f1\13\u008b\5\u008b"+
		"\u08f3\n\u008b\3\u008b\3\u008b\3\u008c\3\u008c\3\u008c\3\u008c\5\u008c"+
		"\u08fb\n\u008c\3\u008c\3\u008c\3\u008c\5\u008c\u0900\n\u008c\3\u008c\5"+
		"\u008c\u0903\n\u008c\3\u008c\3\u008c\3\u008d\3\u008d\3\u008d\3\u008d\3"+
		"\u008d\5\u008d\u090c\n\u008d\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d\3"+
		"\u008d\7\u008d\u0914\n\u008d\f\u008d\16\u008d\u0917\13\u008d\3\u008d\3"+
		"\u008d\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e"+
		"\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e\7\u008e\u0929\n\u008e"+
		"\f\u008e\16\u008e\u092c\13\u008e\3\u008e\3\u008e\3\u008f\3\u008f\3\u008f"+
		"\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f"+
		"\3\u008f\7\u008f\u093d\n\u008f\f\u008f\16\u008f\u0940\13\u008f\3\u008f"+
		"\3\u008f\3\u0090\3\u0090\3\u0090\3\u0090\3\u0091\3\u0091\3\u0091\3\u0091"+
		"\3\u0091\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092"+
		"\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\3\u0094\3\u0094\3\u0094"+
		"\3\u0094\3\u0094\3\u0094\3\u0095\3\u0095\3\u0095\3\u0095\3\u0095\7\u0095"+
		"\u0966\n\u0095\f\u0095\16\u0095\u0969\13\u0095\5\u0095\u096b\n\u0095\3"+
		"\u0095\3\u0095\3\u0095\3\u0096\3\u0096\3\u0096\3\u0096\3\u0096\3\u0097"+
		"\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097\3\u0098\3\u0098\3\u0098\3\u0098"+
		"\3\u0098\3\u0098\3\u0098\3\u0098\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099"+
		"\3\u0099\3\u0099\3\u0099\3\u009a\3\u009a\3\u009a\3\u009a\3\u009a\3\u009a"+
		"\3\u009a\3\u009a\3\u009b\3\u009b\3\u009b\3\u009b\3\u009b\3\u009b\3\u009c"+
		"\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009d\3\u009d\3\u009d\3\u009d"+
		"\3\u009d\3\u009d\3\u009d\3\u009d\3\u009e\3\u009e\3\u009e\3\u009e\3\u009e"+
		"\3\u009e\3\u009f\3\u009f\3\u009f\3\u009f\3\u009f\3\u009f\3\u00a0\3\u00a0"+
		"\3\u00a0\3\u00a0\3\u00a0\3\u00a0\3\u00a1\3\u00a1\3\u00a2\3\u00a2\3\u00a2"+
		"\5\u00a2\u09be\n\u00a2\3\u00a2\3\u00a2\3\u00a3\3\u00a3\3\u00a3\7\u00a3"+
		"\u09c5\n\u00a3\f\u00a3\16\u00a3\u09c8\13\u00a3\3\u00a3\3\u00a3\3\u00a4"+
		"\3\u00a4\7\u00a4\u09ce\n\u00a4\f\u00a4\16\u00a4\u09d1\13\u00a4\3\u00a5"+
		"\5\u00a5\u09d4\n\u00a5\3\u00a5\3\u00a5\3\u00a6\7\u00a6\u09d9\n\u00a6\f"+
		"\u00a6\16\u00a6\u09dc\13\u00a6\3\u00a7\3\u00a7\3\u00a7\3\u00a7\3\u00a7"+
		"\3\u00a7\7\u00a7\u09e4\n\u00a7\f\u00a7\16\u00a7\u09e7\13\u00a7\3\u00a8"+
		"\3\u00a8\3\u00a8\3\u00a9\3\u00a9\3\u00a9\3\u00a9\3\u00a9\3\u00a9\3\u00a9"+
		"\3\u00a9\3\u00a9\3\u00a9\5\u00a9\u09f6\n\u00a9\3\u00aa\3\u00aa\3\u00aa"+
		"\3\u00aa\3\u00aa\3\u00aa\3\u00aa\5\u00aa\u09ff\n\u00aa\5\u00aa\u0a01\n"+
		"\u00aa\3\u00aa\3\u00aa\3\u00aa\3\u00aa\3\u00aa\3\u00aa\3\u00aa\5\u00aa"+
		"\u0a0a\n\u00aa\3\u00aa\7\u00aa\u0a0d\n\u00aa\f\u00aa\16\u00aa\u0a10\13"+
		"\u00aa\3\u00aa\5\u00aa\u0a13\n\u00aa\3\u00ab\5\u00ab\u0a16\n\u00ab\3\u00ab"+
		"\3\u00ab\3\u00ab\7\u00ab\u0a1b\n\u00ab\f\u00ab\16\u00ab\u0a1e\13\u00ab"+
		"\3\u00ab\5\u00ab\u0a21\n\u00ab\3\u00ac\3\u00ac\3\u00ac\3\u00ad\3\u00ad"+
		"\3\u00ad\3\u00ae\3\u00ae\3\u00ae\3\u00ae\7\u00ae\u0a2d\n\u00ae\f\u00ae"+
		"\16\u00ae\u0a30\13\u00ae\5\u00ae\u0a32\n\u00ae\3\u00ae\3\u00ae\3\u00af"+
		"\3\u00af\3\u00af\3\u00af\7\u00af\u0a3a\n\u00af\f\u00af\16\u00af\u0a3d"+
		"\13\u00af\5\u00af\u0a3f\n\u00af\3\u00af\3\u00af\3\u00b0\3\u00b0\3\u00b0"+
		"\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0"+
		"\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0\5\u00b0\u0a55\n\u00b0"+
		"\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1"+
		"\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1"+
		"\3\u00b1\3\u00b1\3\u00b1\3\u00b1\5\u00b1\u0a6d\n\u00b1\3\u00b2\3\u00b2"+
		"\3\u00b2\3\u00b2\7\u00b2\u0a73\n\u00b2\f\u00b2\16\u00b2\u0a76\13\u00b2"+
		"\5\u00b2\u0a78\n\u00b2\3\u00b2\5\u00b2\u0a7b\n\u00b2\3\u00b3\5\u00b3\u0a7e"+
		"\n\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\5\u00b3\u0a85\n\u00b3"+
		"\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3"+
		"\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\5\u00b3"+
		"\u0a98\n\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\5\u00b3\u0a9e\n\u00b3\3"+
		"\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\5\u00b3"+
		"\u0aa8\n\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\5\u00b3\u0aae\n\u00b3\3"+
		"\u00b3\3\u00b3\3\u00b3\3\u00b3\5\u00b3\u0ab4\n\u00b3\3\u00b3\5\u00b3\u0ab7"+
		"\n\u00b3\3\u00b4\3\u00b4\3\u00b4\7\u00b4\u0abc\n\u00b4\f\u00b4\16\u00b4"+
		"\u0abf\13\u00b4\3\u00b4\3\u00b4\3\u00b5\3\u00b5\3\u00b6\3\u00b6\3\u00b7"+
		"\3\u00b7\3\u00b8\7\u00b8\u0aca\n\u00b8\f\u00b8\16\u00b8\u0acd\13\u00b8"+
		"\3\u00b9\3\u00b9\3\u00b9\3\u00b9\3\u00b9\5\u00b9\u0ad4\n\u00b9\3\u00ba"+
		"\7\u00ba\u0ad7\n\u00ba\f\u00ba\16\u00ba\u0ada\13\u00ba\3\u00bb\3\u00bb"+
		"\3\u00bb\3\u00bb\3\u00bb\5\u00bb\u0ae1\n\u00bb\3\u00bc\3\u00bc\3\u00bc"+
		"\7\u00bc\u0ae6\n\u00bc\f\u00bc\16\u00bc\u0ae9\13\u00bc\3\u00bd\3\u00bd"+
		"\3\u00bd\7\u00bd\u0aee\n\u00bd\f\u00bd\16\u00bd\u0af1\13\u00bd\3\u00be"+
		"\3\u00be\3\u00be\3\u00be\3\u00be\3\u00be\3\u00be\3\u00be\5\u00be\u0afb"+
		"\n\u00be\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\7\u00bf\u0b02\n\u00bf"+
		"\f\u00bf\16\u00bf\u0b05\13\u00bf\5\u00bf\u0b07\n\u00bf\3\u00bf\3\u00bf"+
		"\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\7\u00bf\u0b11\n\u00bf"+
		"\f\u00bf\16\u00bf\u0b14\13\u00bf\5\u00bf\u0b16\n\u00bf\3\u00bf\3\u00bf"+
		"\3\u00bf\5\u00bf\u0b1b\n\u00bf\3\u00bf\3\u00bf\5\u00bf\u0b1f\n\u00bf\3"+
		"\u00c0\3\u00c0\3\u00c0\5\u00c0\u0b24\n\u00c0\3\u00c1\3\u00c1\3\u00c2\3"+
		"\u00c2\5\u00c2\u0b2a\n\u00c2\3\u00c2\3\u00c2\3\u00c3\3\u00c3\3\u00c3\3"+
		"\u00c3\5\u00c3\u0b32\n\u00c3\3\u00c4\3\u00c4\5\u00c4\u0b36\n\u00c4\3\u00c5"+
		"\3\u00c5\3\u00c5\3\u00c6\3\u00c6\3\u00c6\3\u00c6\3\u00c6\7\u00c6\u0b40"+
		"\n\u00c6\f\u00c6\16\u00c6\u0b43\13\u00c6\5\u00c6\u0b45\n\u00c6\3\u00c6"+
		"\3\u00c6\3\u00c6\6\u06db\u06e4\u0714\u0abd\6\u00aa\u00ac\u00fe\u0108\u00c7"+
		"\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFH"+
		"JLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"+
		"\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4"+
		"\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc"+
		"\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4"+
		"\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec"+
		"\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104"+
		"\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c"+
		"\u011e\u0120\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134"+
		"\u0136\u0138\u013a\u013c\u013e\u0140\u0142\u0144\u0146\u0148\u014a\u014c"+
		"\u014e\u0150\u0152\u0154\u0156\u0158\u015a\u015c\u015e\u0160\u0162\u0164"+
		"\u0166\u0168\u016a\u016c\u016e\u0170\u0172\u0174\u0176\u0178\u017a\u017c"+
		"\u017e\u0180\u0182\u0184\u0186\u0188\u018a\2\34\4\2\23\23**\3\2OR\3\2"+
		"ST\3\2nq\4\2ff\u0083\u0083\5\2tt\u0081\u0081\u0087\u0087\3\2pq\4\2dei"+
		"j\4\2hhkk\4\2ccu\177\3\2no\n\2\5\5\7\7\n\n\20\20\26\26\35\35\37\37\'\'"+
		"\4\2>N\u0092\u0092\4\2bb\u0087\u0088\4\2\u00ef\u00ef\u00f2\u00f2\3\2l"+
		"m\7\2\3\22\24\64UUYY\u0092\u0092\3\2\u00c3\u00c4\4\2\u00b3\u00b3\u00b6"+
		"\u00b7\3\2\u00b4\u00b5\3\2\u00b8\u00ba\3\2\u00bd\u00be\4\2\u0099\u0099"+
		"\u009e\u009f\3\2\u00b3\u00bf\3\2\u00da\u00e0\3\2\u00e2\u00e3\u0c8f\2\u018d"+
		"\3\2\2\2\4\u01a0\3\2\2\2\6\u01a5\3\2\2\2\b\u01a8\3\2\2\2\n\u01ae\3\2\2"+
		"\2\f\u01b5\3\2\2\2\16\u01cd\3\2\2\2\20\u01d4\3\2\2\2\22\u01de\3\2\2\2"+
		"\24\u01e2\3\2\2\2\26\u01e4\3\2\2\2\30\u01f3\3\2\2\2\32\u0201\3\2\2\2\34"+
		"\u0209\3\2\2\2\36\u0211\3\2\2\2 \u0223\3\2\2\2\"\u022e\3\2\2\2$\u0238"+
		"\3\2\2\2&\u023f\3\2\2\2(\u024a\3\2\2\2*\u0253\3\2\2\2,\u0269\3\2\2\2."+
		"\u0274\3\2\2\2\60\u0276\3\2\2\2\62\u0288\3\2\2\2\64\u028c\3\2\2\2\66\u028e"+
		"\3\2\2\28\u0291\3\2\2\2:\u0294\3\2\2\2<\u029c\3\2\2\2>\u02a9\3\2\2\2@"+
		"\u02b2\3\2\2\2B\u02b4\3\2\2\2D\u02bf\3\2\2\2F\u02cd\3\2\2\2H\u02f1\3\2"+
		"\2\2J\u02f3\3\2\2\2L\u02f6\3\2\2\2N\u02fe\3\2\2\2P\u0303\3\2\2\2R\u030d"+
		"\3\2\2\2T\u030f\3\2\2\2V\u031f\3\2\2\2X\u0333\3\2\2\2Z\u0335\3\2\2\2\\"+
		"\u033d\3\2\2\2^\u0350\3\2\2\2`\u0355\3\2\2\2b\u035e\3\2\2\2d\u0365\3\2"+
		"\2\2f\u0373\3\2\2\2h\u0375\3\2\2\2j\u0377\3\2\2\2l\u0379\3\2\2\2n\u0383"+
		"\3\2\2\2p\u038b\3\2\2\2r\u0392\3\2\2\2t\u0394\3\2\2\2v\u03a4\3\2\2\2x"+
		"\u03a9\3\2\2\2z\u03bb\3\2\2\2|\u03d1\3\2\2\2~\u03d5\3\2\2\2\u0080\u03d7"+
		"\3\2\2\2\u0082\u03dd\3\2\2\2\u0084\u03df\3\2\2\2\u0086\u03e2\3\2\2\2\u0088"+
		"\u03f1\3\2\2\2\u008a\u03f6\3\2\2\2\u008c\u0407\3\2\2\2\u008e\u0473\3\2"+
		"\2\2\u0090\u0475\3\2\2\2\u0092\u0482\3\2\2\2\u0094\u048a\3\2\2\2\u0096"+
		"\u048d\3\2\2\2\u0098\u0494\3\2\2\2\u009a\u049f\3\2\2\2\u009c\u04a8\3\2"+
		"\2\2\u009e\u04ba\3\2\2\2\u00a0\u04c8\3\2\2\2\u00a2\u04cc\3\2\2\2\u00a4"+
		"\u04d1\3\2\2\2\u00a6\u04d9\3\2\2\2\u00a8\u04dd\3\2\2\2\u00aa\u050b\3\2"+
		"\2\2\u00ac\u0589\3\2\2\2\u00ae\u059d\3\2\2\2\u00b0\u05b2\3\2\2\2\u00b2"+
		"\u05b6\3\2\2\2\u00b4\u05ca\3\2\2\2\u00b6\u05cf\3\2\2\2\u00b8\u05e4\3\2"+
		"\2\2\u00ba\u05f5\3\2\2\2\u00bc\u05f7\3\2\2\2\u00be\u05fd\3\2\2\2\u00c0"+
		"\u061b\3\2\2\2\u00c2\u061f\3\2\2\2\u00c4\u0625\3\2\2\2\u00c6\u062a\3\2"+
		"\2\2\u00c8\u062c\3\2\2\2\u00ca\u0630\3\2\2\2\u00cc\u0639\3\2\2\2\u00ce"+
		"\u0646\3\2\2\2\u00d0\u0648\3\2\2\2\u00d2\u0659\3\2\2\2\u00d4\u0660\3\2"+
		"\2\2\u00d6\u0662\3\2\2\2\u00d8\u0668\3\2\2\2\u00da\u0672\3\2\2\2\u00dc"+
		"\u0674\3\2\2\2\u00de\u0678\3\2\2\2\u00e0\u067a\3\2\2\2\u00e2\u067c\3\2"+
		"\2\2\u00e4\u0694\3\2\2\2\u00e6\u06a6\3\2\2\2\u00e8\u06be\3\2\2\2\u00ea"+
		"\u06c4\3\2\2\2\u00ec\u06cf\3\2\2\2\u00ee\u06d3\3\2\2\2\u00f0\u06e8\3\2"+
		"\2\2\u00f2\u06ea\3\2\2\2\u00f4\u070d\3\2\2\2\u00f6\u070f\3\2\2\2\u00f8"+
		"\u0719\3\2\2\2\u00fa\u0721\3\2\2\2\u00fc\u0752\3\2\2\2\u00fe\u0775\3\2"+
		"\2\2\u0100\u0795\3\2\2\2\u0102\u07ce\3\2\2\2\u0104\u07d0\3\2\2\2\u0106"+
		"\u07fc\3\2\2\2\u0108\u0834\3\2\2\2\u010a\u08b0\3\2\2\2\u010c\u08bd\3\2"+
		"\2\2\u010e\u08c8\3\2\2\2\u0110\u08d5\3\2\2\2\u0112\u08d7\3\2\2\2\u0114"+
		"\u08e4\3\2\2\2\u0116\u08f6\3\2\2\2\u0118\u0906\3\2\2\2\u011a\u091a\3\2"+
		"\2\2\u011c\u092f\3\2\2\2\u011e\u0943\3\2\2\2\u0120\u0947\3\2\2\2\u0122"+
		"\u094c\3\2\2\2\u0124\u0954\3\2\2\2\u0126\u095a\3\2\2\2\u0128\u0960\3\2"+
		"\2\2\u012a\u096f\3\2\2\2\u012c\u0974\3\2\2\2\u012e\u097a\3\2\2\2\u0130"+
		"\u0982\3\2\2\2\u0132\u098a\3\2\2\2\u0134\u0992\3\2\2\2\u0136\u0998\3\2"+
		"\2\2\u0138\u099e\3\2\2\2\u013a\u09a6\3\2\2\2\u013c\u09ac\3\2\2\2\u013e"+
		"\u09b2\3\2\2\2\u0140\u09b8\3\2\2\2\u0142\u09ba\3\2\2\2\u0144\u09c6\3\2"+
		"\2\2\u0146\u09cb\3\2\2\2\u0148\u09d3\3\2\2\2\u014a\u09da\3\2\2\2\u014c"+
		"\u09dd\3\2\2\2\u014e\u09e8\3\2\2\2\u0150\u09f5\3\2\2\2\u0152\u0a00\3\2"+
		"\2\2\u0154\u0a15\3\2\2\2\u0156\u0a22\3\2\2\2\u0158\u0a25\3\2\2\2\u015a"+
		"\u0a28\3\2\2\2\u015c\u0a35\3\2\2\2\u015e\u0a54\3\2\2\2\u0160\u0a6c\3\2"+
		"\2\2\u0162\u0a7a\3\2\2\2\u0164\u0a7d\3\2\2\2\u0166\u0ab8\3\2\2\2\u0168"+
		"\u0ac2\3\2\2\2\u016a\u0ac4\3\2\2\2\u016c\u0ac6\3\2\2\2\u016e\u0acb\3\2"+
		"\2\2\u0170\u0ace\3\2\2\2\u0172\u0ad8\3\2\2\2\u0174\u0adb\3\2\2\2\u0176"+
		"\u0ae2\3\2\2\2\u0178\u0aea\3\2\2\2\u017a\u0afa\3\2\2\2\u017c\u0b1e\3\2"+
		"\2\2\u017e\u0b23\3\2\2\2\u0180\u0b25\3\2\2\2\u0182\u0b29\3\2\2\2\u0184"+
		"\u0b31\3\2\2\2\u0186\u0b33\3\2\2\2\u0188\u0b37\3\2\2\2\u018a\u0b3a\3\2"+
		"\2\2\u018c\u018e\5\n\6\2\u018d\u018c\3\2\2\2\u018d\u018e\3\2\2\2\u018e"+
		"\u0192\3\2\2\2\u018f\u0191\5\f\7\2\u0190\u018f\3\2\2\2\u0191\u0194\3\2"+
		"\2\2\u0192\u0190\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0198\3\2\2\2\u0194"+
		"\u0192\3\2\2\2\u0195\u0197\5\16\b\2\u0196\u0195\3\2\2\2\u0197\u019a\3"+
		"\2\2\2\u0198\u0196\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u019b\3\2\2\2\u019a"+
		"\u0198\3\2\2\2\u019b\u019c\7\2\2\3\u019c\3\3\2\2\2\u019d\u019f\5,\27\2"+
		"\u019e\u019d\3\2\2\2\u019f\u01a2\3\2\2\2\u01a0\u019e\3\2\2\2\u01a0\u01a1"+
		"\3\2\2\2\u01a1\u01a3\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a3\u01a4\7\2\2\3\u01a4"+
		"\5\3\2\2\2\u01a5\u01a6\5\u00aaV\2\u01a6\u01a7\7\2\2\3\u01a7\7\3\2\2\2"+
		"\u01a8\u01a9\5\u0142\u00a2\2\u01a9\u01aa\7\2\2\3\u01aa\t\3\2\2\2\u01ab"+
		"\u01ad\5l\67\2\u01ac\u01ab\3\2\2\2\u01ad\u01b0\3\2\2\2\u01ae\u01ac\3\2"+
		"\2\2\u01ae\u01af\3\2\2\2\u01af\u01b1\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b1"+
		"\u01b2\7\"\2\2\u01b2\u01b3\5d\63\2\u01b3\u01b4\7`\2\2\u01b4\13\3\2\2\2"+
		"\u01b5\u01b7\7\33\2\2\u01b6\u01b8\7(\2\2\u01b7\u01b6\3\2\2\2\u01b7\u01b8"+
		"\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9\u01bc\5d\63\2\u01ba\u01bb\7b\2\2\u01bb"+
		"\u01bd\7\u0081\2\2\u01bc\u01ba\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01be"+
		"\3\2\2\2\u01be\u01bf\7`\2\2\u01bf\r\3\2\2\2\u01c0\u01c2\5\22\n\2\u01c1"+
		"\u01c0\3\2\2\2\u01c2\u01c5\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c3\u01c4\3\2"+
		"\2\2\u01c4\u01ca\3\2\2\2\u01c5\u01c3\3\2\2\2\u01c6\u01cb\5\26\f\2\u01c7"+
		"\u01cb\5\36\20\2\u01c8\u01cb\5&\24\2\u01c9\u01cb\5v<\2\u01ca\u01c6\3\2"+
		"\2\2\u01ca\u01c7\3\2\2\2\u01ca\u01c8\3\2\2\2\u01ca\u01c9\3\2\2\2\u01cb"+
		"\u01ce\3\2\2\2\u01cc\u01ce\7`\2\2\u01cd\u01c3\3\2\2\2\u01cd\u01cc\3\2"+
		"\2\2\u01ce\17\3\2\2\2\u01cf\u01d5\5\22\n\2\u01d0\u01d5\7 \2\2\u01d1\u01d5"+
		"\7,\2\2\u01d2\u01d5\7\60\2\2\u01d3\u01d5\7\63\2\2\u01d4\u01cf\3\2\2\2"+
		"\u01d4\u01d0\3\2\2\2\u01d4\u01d1\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d4\u01d3"+
		"\3\2\2\2\u01d5\21\3\2\2\2\u01d6\u01df\5l\67\2\u01d7\u01df\7%\2\2\u01d8"+
		"\u01df\7$\2\2\u01d9\u01df\7#\2\2\u01da\u01df\7(\2\2\u01db\u01df\7\3\2"+
		"\2\u01dc\u01df\7\24\2\2\u01dd\u01df\7)\2\2\u01de\u01d6\3\2\2\2\u01de\u01d7"+
		"\3\2\2\2\u01de\u01d8\3\2\2\2\u01de\u01d9\3\2\2\2\u01de\u01da\3\2\2\2\u01de"+
		"\u01db\3\2\2\2\u01de\u01dc\3\2\2\2\u01de\u01dd\3\2\2\2\u01df\23\3\2\2"+
		"\2\u01e0\u01e3\7\24\2\2\u01e1\u01e3\5l\67\2\u01e2\u01e0\3\2\2\2\u01e2"+
		"\u01e1\3\2\2\2\u01e3\25\3\2\2\2\u01e4\u01e5\7\13\2\2\u01e5\u01e7\5\u00d8"+
		"m\2\u01e6\u01e8\5\30\r\2\u01e7\u01e6\3\2\2\2\u01e7\u01e8\3\2\2\2\u01e8"+
		"\u01eb\3\2\2\2\u01e9\u01ea\7\23\2\2\u01ea\u01ec\5\u00ccg\2\u01eb\u01e9"+
		"\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01ef\3\2\2\2\u01ed\u01ee\7\32\2\2"+
		"\u01ee\u01f0\5\u00caf\2\u01ef\u01ed\3\2\2\2\u01ef\u01f0\3\2\2\2\u01f0"+
		"\u01f1\3\2\2\2\u01f1\u01f2\5(\25\2\u01f2\27\3\2\2\2\u01f3\u01f4\7e\2\2"+
		"\u01f4\u01f9\5\32\16\2\u01f5\u01f6\7a\2\2\u01f6\u01f8\5\32\16\2\u01f7"+
		"\u01f5\3\2\2\2\u01f8\u01fb\3\2\2\2\u01f9\u01f7\3\2\2\2\u01f9\u01fa\3\2"+
		"\2\2\u01fa\u01fc\3\2\2\2\u01fb\u01f9\3\2\2\2\u01fc\u01fd\7d\2\2\u01fd"+
		"\31\3\2\2\2\u01fe\u0200\5l\67\2\u01ff\u01fe\3\2\2\2\u0200\u0203\3\2\2"+
		"\2\u0201\u01ff\3\2\2\2\u0201\u0202\3\2\2\2\u0202\u0204\3\2\2\2\u0203\u0201"+
		"\3\2\2\2\u0204\u0207\5\u00d8m\2\u0205\u0206\7\23\2\2\u0206\u0208\5\34"+
		"\17\2\u0207\u0205\3\2\2\2\u0207\u0208\3\2\2\2\u0208\33\3\2\2\2\u0209\u020e"+
		"\5\u00ccg\2\u020a\u020b\7r\2\2\u020b\u020d\5\u00ccg\2\u020c\u020a\3\2"+
		"\2\2\u020d\u0210\3\2\2\2\u020e\u020c\3\2\2\2\u020e\u020f\3\2\2\2\u020f"+
		"\35\3\2\2\2\u0210\u020e\3\2\2\2\u0211\u0212\7\22\2\2\u0212\u0215\5\u00d8"+
		"m\2\u0213\u0214\7\32\2\2\u0214\u0216\5\u00caf\2\u0215\u0213\3\2\2\2\u0215"+
		"\u0216\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u0219\7\\\2\2\u0218\u021a\5 "+
		"\21\2\u0219\u0218\3\2\2\2\u0219\u021a\3\2\2\2\u021a\u021c\3\2\2\2\u021b"+
		"\u021d\7a\2\2\u021c\u021b\3\2\2\2\u021c\u021d\3\2\2\2\u021d\u021f\3\2"+
		"\2\2\u021e\u0220\5$\23\2\u021f\u021e\3\2\2\2\u021f\u0220\3\2\2\2\u0220"+
		"\u0221\3\2\2\2\u0221\u0222\7]\2\2\u0222\37\3\2\2\2\u0223\u0228\5\"\22"+
		"\2\u0224\u0225\7a\2\2\u0225\u0227\5\"\22\2\u0226\u0224\3\2\2\2\u0227\u022a"+
		"\3\2\2\2\u0228\u0226\3\2\2\2\u0228\u0229\3\2\2\2\u0229!\3\2\2\2\u022a"+
		"\u0228\3\2\2\2\u022b\u022d\5l\67\2\u022c\u022b\3\2\2\2\u022d\u0230\3\2"+
		"\2\2\u022e\u022c\3\2\2\2\u022e\u022f\3\2\2\2\u022f\u0231\3\2\2\2\u0230"+
		"\u022e\3\2\2\2\u0231\u0233\5\u00d8m\2\u0232\u0234\5\u00d6l\2\u0233\u0232"+
		"\3\2\2\2\u0233\u0234\3\2\2\2\u0234\u0236\3\2\2\2\u0235\u0237\5(\25\2\u0236"+
		"\u0235\3\2\2\2\u0236\u0237\3\2\2\2\u0237#\3\2\2\2\u0238\u023c\7`\2\2\u0239"+
		"\u023b\5,\27\2\u023a\u0239\3\2\2\2\u023b\u023e\3\2\2\2\u023c\u023a\3\2"+
		"\2\2\u023c\u023d\3\2\2\2\u023d%\3\2\2\2\u023e\u023c\3\2\2\2\u023f\u0240"+
		"\7\36\2\2\u0240\u0242\5\u00d8m\2\u0241\u0243\5\30\r\2\u0242\u0241\3\2"+
		"\2\2\u0242\u0243\3\2\2\2\u0243\u0246\3\2\2\2\u0244\u0245\7\23\2\2\u0245"+
		"\u0247\5\u00caf\2\u0246\u0244\3\2\2\2\u0246\u0247\3\2\2\2\u0247\u0248"+
		"\3\2\2\2\u0248\u0249\5*\26\2\u0249\'\3\2\2\2\u024a\u024e\7\\\2\2\u024b"+
		"\u024d\5,\27\2\u024c\u024b\3\2\2\2\u024d\u0250\3\2\2\2\u024e\u024c\3\2"+
		"\2\2\u024e\u024f\3\2\2\2\u024f\u0251\3\2\2\2\u0250\u024e\3\2\2\2\u0251"+
		"\u0252\7]\2\2\u0252)\3\2\2\2\u0253\u0257\7\\\2\2\u0254\u0256\5> \2\u0255"+
		"\u0254\3\2\2\2\u0256\u0259\3\2\2\2\u0257\u0255\3\2\2\2\u0257\u0258\3\2"+
		"\2\2\u0258\u025a\3\2\2\2\u0259\u0257\3\2\2\2\u025a\u025b\7]\2\2\u025b"+
		"+\3\2\2\2\u025c\u026a\7`\2\2\u025d\u025f\7(\2\2\u025e\u025d\3\2\2\2\u025e"+
		"\u025f\3\2\2\2\u025f\u0260\3\2\2\2\u0260\u026a\5\u0086D\2\u0261\u0263"+
		"\5\20\t\2\u0262\u0261\3\2\2\2\u0263\u0266\3\2\2\2\u0264\u0262\3\2\2\2"+
		"\u0264\u0265\3\2\2\2\u0265\u0267\3\2\2\2\u0266\u0264\3\2\2\2\u0267\u026a"+
		"\5.\30\2\u0268\u026a\5\u00dan\2\u0269\u025c\3\2\2\2\u0269\u025e\3\2\2"+
		"\2\u0269\u0264\3\2\2\2\u0269\u0268\3\2\2\2\u026a-\3\2\2\2\u026b\u0275"+
		"\5\60\31\2\u026c\u0275\5\66\34\2\u026d\u0275\5<\37\2\u026e\u0275\5:\36"+
		"\2\u026f\u0275\58\35\2\u0270\u0275\5&\24\2\u0271\u0275\5v<\2\u0272\u0275"+
		"\5\26\f\2\u0273\u0275\5\36\20\2\u0274\u026b\3\2\2\2\u0274\u026c\3\2\2"+
		"\2\u0274\u026d\3\2\2\2\u0274\u026e\3\2\2\2\u0274\u026f\3\2\2\2\u0274\u0270"+
		"\3\2\2\2\u0274\u0271\3\2\2\2\u0274\u0272\3\2\2\2\u0274\u0273\3\2\2\2\u0275"+
		"/\3\2\2\2\u0276\u0277\5\64\33\2\u0277\u0278\5\u00d8m\2\u0278\u027d\5\\"+
		"/\2\u0279\u027a\7^\2\2\u027a\u027c\7_\2\2\u027b\u0279\3\2\2\2\u027c\u027f"+
		"\3\2\2\2\u027d\u027b\3\2\2\2\u027d\u027e\3\2\2\2\u027e\u0282\3\2\2\2\u027f"+
		"\u027d\3\2\2\2\u0280\u0281\7/\2\2\u0281\u0283\5Z.\2\u0282\u0280\3\2\2"+
		"\2\u0282\u0283\3\2\2\2\u0283\u0284\3\2\2\2\u0284\u0285\5\62\32\2\u0285"+
		"\61\3\2\2\2\u0286\u0289\5\u0086D\2\u0287\u0289\7`\2\2\u0288\u0286\3\2"+
		"\2\2\u0288\u0287\3\2\2\2\u0289\63\3\2\2\2\u028a\u028d\5\u00ccg\2\u028b"+
		"\u028d\7\62\2\2\u028c\u028a\3\2\2\2\u028c\u028b\3\2\2\2\u028d\65\3\2\2"+
		"\2\u028e\u028f\5\30\r\2\u028f\u0290\5\60\31\2\u0290\67\3\2\2\2\u0291\u0292"+
		"\5\30\r\2\u0292\u0293\5:\36\2\u02939\3\2\2\2\u0294\u0295\5\u00d8m\2\u0295"+
		"\u0298\5\\/\2\u0296\u0297\7/\2\2\u0297\u0299\5Z.\2\u0298\u0296\3\2\2\2"+
		"\u0298\u0299\3\2\2\2\u0299\u029a\3\2\2\2\u029a\u029b\5\u0086D\2\u029b"+
		";\3\2\2\2\u029c\u029d\5\u00ccg\2\u029d\u029e\5L\'\2\u029e\u029f\7`\2\2"+
		"\u029f=\3\2\2\2\u02a0\u02a2\5\20\t\2\u02a1\u02a0\3\2\2\2\u02a2\u02a5\3"+
		"\2\2\2\u02a3\u02a1\3\2\2\2\u02a3\u02a4\3\2\2\2\u02a4\u02a6\3\2\2\2\u02a5"+
		"\u02a3\3\2\2\2\u02a6\u02aa\5@!\2\u02a7\u02aa\5\u00dan\2\u02a8\u02aa\7"+
		"`\2\2\u02a9\u02a3\3\2\2\2\u02a9\u02a7\3\2\2\2\u02a9\u02a8\3\2\2\2\u02aa"+
		"?\3\2\2\2\u02ab\u02b3\5B\"\2\u02ac\u02b3\5F$\2\u02ad\u02b3\5J&\2\u02ae"+
		"\u02b3\5&\24\2\u02af\u02b3\5v<\2\u02b0\u02b3\5\26\f\2\u02b1\u02b3\5\36"+
		"\20\2\u02b2\u02ab\3\2\2\2\u02b2\u02ac\3\2\2\2\u02b2\u02ad\3\2\2\2\u02b2"+
		"\u02ae\3\2\2\2\u02b2\u02af\3\2\2\2\u02b2\u02b0\3\2\2\2\u02b2\u02b1\3\2"+
		"\2\2\u02b3A\3\2\2\2\u02b4\u02b5\5\u00ccg\2\u02b5\u02ba\5D#\2\u02b6\u02b7"+
		"\7a\2\2\u02b7\u02b9\5D#\2\u02b8\u02b6\3\2\2\2\u02b9\u02bc\3\2\2\2\u02ba"+
		"\u02b8\3\2\2\2\u02ba\u02bb\3\2\2\2\u02bb\u02bd\3\2\2\2\u02bc\u02ba\3\2"+
		"\2\2\u02bd\u02be\7`\2\2\u02beC\3\2\2\2\u02bf\u02c4\5\u00d8m\2\u02c0\u02c1"+
		"\7^\2\2\u02c1\u02c3\7_\2\2\u02c2\u02c0\3\2\2\2\u02c3\u02c6\3\2\2\2\u02c4"+
		"\u02c2\3\2\2\2\u02c4\u02c5\3\2\2\2\u02c5\u02c7\3\2\2\2\u02c6\u02c4\3\2"+
		"\2\2\u02c7\u02c8\7c\2\2\u02c8\u02c9\5R*\2\u02c9E\3\2\2\2\u02ca\u02cc\5"+
		"H%\2\u02cb\u02ca\3\2\2\2\u02cc\u02cf\3\2\2\2\u02cd\u02cb\3\2\2\2\u02cd"+
		"\u02ce\3\2\2\2\u02ce\u02da\3\2\2\2\u02cf\u02cd\3\2\2\2\u02d0\u02db\5\64"+
		"\33\2\u02d1\u02d5\5\30\r\2\u02d2\u02d4\5l\67\2\u02d3\u02d2\3\2\2\2\u02d4"+
		"\u02d7\3\2\2\2\u02d5\u02d3\3\2\2\2\u02d5\u02d6\3\2\2\2\u02d6\u02d8\3\2"+
		"\2\2\u02d7\u02d5\3\2\2\2\u02d8\u02d9\5\64\33\2\u02d9\u02db\3\2\2\2\u02da"+
		"\u02d0\3\2\2\2\u02da\u02d1\3\2\2\2\u02db\u02dc\3\2\2\2\u02dc\u02dd\5\u00d8"+
		"m\2\u02dd\u02e2\5\\/\2\u02de\u02df\7^\2\2\u02df\u02e1\7_\2\2\u02e0\u02de"+
		"\3\2\2\2\u02e1\u02e4\3\2\2\2\u02e2\u02e0\3\2\2\2\u02e2\u02e3\3\2\2\2\u02e3"+
		"\u02e7\3\2\2\2\u02e4\u02e2\3\2\2\2\u02e5\u02e6\7/\2\2\u02e6\u02e8\5Z."+
		"\2\u02e7\u02e5\3\2\2\2\u02e7\u02e8\3\2\2\2\u02e8\u02e9\3\2\2\2\u02e9\u02ea"+
		"\5\62\32\2\u02eaG\3\2\2\2\u02eb\u02f2\5l\67\2\u02ec\u02f2\7%\2\2\u02ed"+
		"\u02f2\7\3\2\2\u02ee\u02f2\7\16\2\2\u02ef\u02f2\7(\2\2\u02f0\u02f2\7)"+
		"\2\2\u02f1\u02eb\3\2\2\2\u02f1\u02ec\3\2\2\2\u02f1\u02ed\3\2\2\2\u02f1"+
		"\u02ee\3\2\2\2\u02f1\u02ef\3\2\2\2\u02f1\u02f0\3\2\2\2\u02f2I\3\2\2\2"+
		"\u02f3\u02f4\5\30\r\2\u02f4\u02f5\5F$\2\u02f5K\3\2\2\2\u02f6\u02fb\5N"+
		"(\2\u02f7\u02f8\7a\2\2\u02f8\u02fa\5N(\2\u02f9\u02f7\3\2\2\2\u02fa\u02fd"+
		"\3\2\2\2\u02fb\u02f9\3\2\2\2\u02fb\u02fc\3\2\2\2\u02fcM\3\2\2\2\u02fd"+
		"\u02fb\3\2\2\2\u02fe\u0301\5P)\2\u02ff\u0300\7c\2\2\u0300\u0302\5R*\2"+
		"\u0301\u02ff\3\2\2\2\u0301\u0302\3\2\2\2\u0302O\3\2\2\2\u0303\u0308\5"+
		"\u00d8m\2\u0304\u0305\7^\2\2\u0305\u0307\7_\2\2\u0306\u0304\3\2\2\2\u0307"+
		"\u030a\3\2\2\2\u0308\u0306\3\2\2\2\u0308\u0309\3\2\2\2\u0309Q\3\2\2\2"+
		"\u030a\u0308\3\2\2\2\u030b\u030e\5T+\2\u030c\u030e\5\u00aaV\2\u030d\u030b"+
		"\3\2\2\2\u030d\u030c\3\2\2\2\u030eS\3\2\2\2\u030f\u031b\7\\\2\2\u0310"+
		"\u0315\5R*\2\u0311\u0312\7a\2\2\u0312\u0314\5R*\2\u0313\u0311\3\2\2\2"+
		"\u0314\u0317\3\2\2\2\u0315\u0313\3\2\2\2\u0315\u0316\3\2\2\2\u0316\u0319"+
		"\3\2\2\2\u0317\u0315\3\2\2\2\u0318\u031a\7a\2\2\u0319\u0318\3\2\2\2\u0319"+
		"\u031a\3\2\2\2\u031a\u031c\3\2\2\2\u031b\u0310\3\2\2\2\u031b\u031c\3\2"+
		"\2\2\u031c\u031d\3\2\2\2\u031d\u031e\7]\2\2\u031eU\3\2\2\2\u031f\u0321"+
		"\5\u00d8m\2\u0320\u0322\5\u00d0i\2\u0321\u0320\3\2\2\2\u0321\u0322\3\2"+
		"\2\2\u0322\u032a\3\2\2\2\u0323\u0324\7b\2\2\u0324\u0326\5\u00d8m\2\u0325"+
		"\u0327\5\u00d0i\2\u0326\u0325\3\2\2\2\u0326\u0327\3\2\2\2\u0327\u0329"+
		"\3\2\2\2\u0328\u0323\3\2\2\2\u0329\u032c\3\2\2\2\u032a\u0328\3\2\2\2\u032a"+
		"\u032b\3\2\2\2\u032bW\3\2\2\2\u032c\u032a\3\2\2\2\u032d\u0334\5\u00cc"+
		"g\2\u032e\u0331\7\u0085\2\2\u032f\u0330\t\2\2\2\u0330\u0332\5\u00ccg\2"+
		"\u0331\u032f\3\2\2\2\u0331\u0332\3\2\2\2\u0332\u0334\3\2\2\2\u0333\u032d"+
		"\3\2\2\2\u0333\u032e\3\2\2\2\u0334Y\3\2\2\2\u0335\u033a\5d\63\2\u0336"+
		"\u0337\7a\2\2\u0337\u0339\5d\63\2\u0338\u0336\3\2\2\2\u0339\u033c\3\2"+
		"\2\2\u033a\u0338\3\2\2\2\u033a\u033b\3\2\2\2\u033b[\3\2\2\2\u033c\u033a"+
		"\3\2\2\2\u033d\u033f\7Z\2\2\u033e\u0340\5^\60\2\u033f\u033e\3\2\2\2\u033f"+
		"\u0340\3\2\2\2\u0340\u0341\3\2\2\2\u0341\u0342\7[\2\2\u0342]\3\2\2\2\u0343"+
		"\u0348\5`\61\2\u0344\u0345\7a\2\2\u0345\u0347\5`\61\2\u0346\u0344\3\2"+
		"\2\2\u0347\u034a\3\2\2\2\u0348\u0346\3\2\2\2\u0348\u0349\3\2\2\2\u0349"+
		"\u034d\3\2\2\2\u034a\u0348\3\2\2\2\u034b\u034c\7a\2\2\u034c\u034e\5b\62"+
		"\2\u034d\u034b\3\2\2\2\u034d\u034e\3\2\2\2\u034e\u0351\3\2\2\2\u034f\u0351"+
		"\5b\62\2\u0350\u0343\3\2\2\2\u0350\u034f\3\2\2\2\u0351_\3\2\2\2\u0352"+
		"\u0354\5\24\13\2\u0353\u0352\3\2\2\2\u0354\u0357\3\2\2\2\u0355\u0353\3"+
		"\2\2\2\u0355\u0356\3\2\2\2\u0356\u0358\3\2\2\2\u0357\u0355\3\2\2\2\u0358"+
		"\u0359\5\u00ccg\2\u0359\u035a\5P)\2\u035aa\3\2\2\2\u035b\u035d\5\24\13"+
		"\2\u035c\u035b\3\2\2\2\u035d\u0360\3\2\2\2\u035e\u035c\3\2\2\2\u035e\u035f"+
		"\3\2\2\2\u035f\u0361\3\2\2\2\u0360\u035e\3\2\2\2\u0361\u0362\5\u00ccg"+
		"\2\u0362\u0363\7\u008e\2\2\u0363\u0364\5P)\2\u0364c\3\2\2\2\u0365\u036a"+
		"\5\u00d8m\2\u0366\u0367\7b\2\2\u0367\u0369\5\u00d8m\2\u0368\u0366\3\2"+
		"\2\2\u0369\u036c\3\2\2\2\u036a\u0368\3\2\2\2\u036a\u036b\3\2\2\2\u036b"+
		"e\3\2\2\2\u036c\u036a\3\2\2\2\u036d\u0374\5h\65\2\u036e\u0374\5j\66\2"+
		"\u036f\u0374\7V\2\2\u0370\u0374\7X\2\2\u0371\u0374\7U\2\2\u0372\u0374"+
		"\7Y\2\2\u0373\u036d\3\2\2\2\u0373\u036e\3\2\2\2\u0373\u036f\3\2\2\2\u0373"+
		"\u0370\3\2\2\2\u0373\u0371\3\2\2\2\u0373\u0372\3\2\2\2\u0374g\3\2\2\2"+
		"\u0375\u0376\t\3\2\2\u0376i\3\2\2\2\u0377\u0378\t\4\2\2\u0378k\3\2\2\2"+
		"\u0379\u037a\7\u008d\2\2\u037a\u0381\5d\63\2\u037b\u037e\7Z\2\2\u037c"+
		"\u037f\5n8\2\u037d\u037f\5r:\2\u037e\u037c\3\2\2\2\u037e\u037d\3\2\2\2"+
		"\u037e\u037f\3\2\2\2\u037f\u0380\3\2\2\2\u0380\u0382\7[\2\2\u0381\u037b"+
		"\3\2\2\2\u0381\u0382\3\2\2\2\u0382m\3\2\2\2\u0383\u0388\5p9\2\u0384\u0385"+
		"\7a\2\2\u0385\u0387\5p9\2\u0386\u0384\3\2\2\2\u0387\u038a\3\2\2\2\u0388"+
		"\u0386\3\2\2\2\u0388\u0389\3\2\2\2\u0389o\3\2\2\2\u038a\u0388\3\2\2\2"+
		"\u038b\u038c\5\u00d8m\2\u038c\u038d\7c\2\2\u038d\u038e\5r:\2\u038eq\3"+
		"\2\2\2\u038f\u0393\5\u00aaV\2\u0390\u0393\5l\67\2\u0391\u0393\5t;\2\u0392"+
		"\u038f\3\2\2\2\u0392\u0390\3\2\2\2\u0392\u0391\3\2\2\2\u0393s\3\2\2\2"+
		"\u0394\u039d\7\\\2\2\u0395\u039a\5r:\2\u0396\u0397\7a\2\2\u0397\u0399"+
		"\5r:\2\u0398\u0396\3\2\2\2\u0399\u039c\3\2\2\2\u039a\u0398\3\2\2\2\u039a"+
		"\u039b\3\2\2\2\u039b\u039e\3\2\2\2\u039c\u039a\3\2\2\2\u039d\u0395\3\2"+
		"\2\2\u039d\u039e\3\2\2\2\u039e\u03a0\3\2\2\2\u039f\u03a1\7a\2\2\u03a0"+
		"\u039f\3\2\2\2\u03a0\u03a1\3\2\2\2\u03a1\u03a2\3\2\2\2\u03a2\u03a3\7]"+
		"\2\2\u03a3u\3\2\2\2\u03a4\u03a5\7\u008d\2\2\u03a5\u03a6\7\36\2\2\u03a6"+
		"\u03a7\5\u00d8m\2\u03a7\u03a8\5x=\2\u03a8w\3\2\2\2\u03a9\u03ad\7\\\2\2"+
		"\u03aa\u03ac\5z>\2\u03ab\u03aa\3\2\2\2\u03ac\u03af\3\2\2\2\u03ad\u03ab"+
		"\3\2\2\2\u03ad\u03ae\3\2\2\2\u03ae\u03b0\3\2\2\2\u03af\u03ad\3\2\2\2\u03b0"+
		"\u03b1\7]\2\2\u03b1y\3\2\2\2\u03b2\u03b4\5\20\t\2\u03b3\u03b2\3\2\2\2"+
		"\u03b4\u03b7\3\2\2\2\u03b5\u03b3\3\2\2\2\u03b5\u03b6\3\2\2\2\u03b6\u03b8"+
		"\3\2\2\2\u03b7\u03b5\3\2\2\2\u03b8\u03bc\5|?\2\u03b9\u03bc\5\u00dan\2"+
		"\u03ba\u03bc\7`\2\2\u03bb\u03b5\3\2\2\2\u03bb\u03b9\3\2\2\2\u03bb\u03ba"+
		"\3\2\2\2\u03bc{\3\2\2\2\u03bd\u03be\5\u00ccg\2\u03be\u03bf\5~@\2\u03bf"+
		"\u03c0\7`\2\2\u03c0\u03d2\3\2\2\2\u03c1\u03c3\5\26\f\2\u03c2\u03c4\7`"+
		"\2\2\u03c3\u03c2\3\2\2\2\u03c3\u03c4\3\2\2\2\u03c4\u03d2\3\2\2\2\u03c5"+
		"\u03c7\5&\24\2\u03c6\u03c8\7`\2\2\u03c7\u03c6\3\2\2\2\u03c7\u03c8\3\2"+
		"\2\2\u03c8\u03d2\3\2\2\2\u03c9\u03cb\5\36\20\2\u03ca\u03cc\7`\2\2\u03cb"+
		"\u03ca\3\2\2\2\u03cb\u03cc\3\2\2\2\u03cc\u03d2\3\2\2\2\u03cd\u03cf\5v"+
		"<\2\u03ce\u03d0\7`\2\2\u03cf\u03ce\3\2\2\2\u03cf\u03d0\3\2\2\2\u03d0\u03d2"+
		"\3\2\2\2\u03d1\u03bd\3\2\2\2\u03d1\u03c1\3\2\2\2\u03d1\u03c5\3\2\2\2\u03d1"+
		"\u03c9\3\2\2\2\u03d1\u03cd\3\2\2\2\u03d2}\3\2\2\2\u03d3\u03d6\5\u0080"+
		"A\2\u03d4\u03d6\5\u0082B\2\u03d5\u03d3\3\2\2\2\u03d5\u03d4\3\2\2\2\u03d6"+
		"\177\3\2\2\2\u03d7\u03d8\5\u00d8m\2\u03d8\u03d9\7Z\2\2\u03d9\u03db\7["+
		"\2\2\u03da\u03dc\5\u0084C\2\u03db\u03da\3\2\2\2\u03db\u03dc\3\2\2\2\u03dc"+
		"\u0081\3\2\2\2\u03dd\u03de\5L\'\2\u03de\u0083\3\2\2\2\u03df\u03e0\7\16"+
		"\2\2\u03e0\u03e1\5r:\2\u03e1\u0085\3\2\2\2\u03e2\u03e6\7\\\2\2\u03e3\u03e5"+
		"\5\u0088E\2\u03e4\u03e3\3\2\2\2\u03e5\u03e8\3\2\2\2\u03e6\u03e4\3\2\2"+
		"\2\u03e6\u03e7\3\2\2\2\u03e7\u03e9\3\2\2\2\u03e8\u03e6\3\2\2\2\u03e9\u03ea"+
		"\7]\2\2\u03ea\u0087\3\2\2\2\u03eb\u03ec\5\u008aF\2\u03ec\u03ed\7`\2\2"+
		"\u03ed\u03f2\3\2\2\2\u03ee\u03f2\5\u008eH\2\u03ef\u03f2\5\u008cG\2\u03f0"+
		"\u03f2\5\u00dan\2\u03f1\u03eb\3\2\2\2\u03f1\u03ee\3\2\2\2\u03f1\u03ef"+
		"\3\2\2\2\u03f1\u03f0\3\2\2\2\u03f2\u0089\3\2\2\2\u03f3\u03f5\5\24\13\2"+
		"\u03f4\u03f3\3\2\2\2\u03f5\u03f8\3\2\2\2\u03f6\u03f4\3\2\2\2\u03f6\u03f7"+
		"\3\2\2\2\u03f7\u03f9\3\2\2\2\u03f8\u03f6\3\2\2\2\u03f9\u03fa\5\u00ccg"+
		"\2\u03fa\u03fb\5L\'\2\u03fb\u008b\3\2\2\2\u03fc\u03fe\5\22\n\2\u03fd\u03fc"+
		"\3\2\2\2\u03fe\u0401\3\2\2\2\u03ff\u03fd\3\2\2\2\u03ff\u0400\3\2\2\2\u0400"+
		"\u0404\3\2\2\2\u0401\u03ff\3\2\2\2\u0402\u0405\5\26\f\2\u0403\u0405\5"+
		"&\24\2\u0404\u0402\3\2\2\2\u0404\u0403\3\2\2\2\u0405\u0408\3\2\2\2\u0406"+
		"\u0408\7`\2\2\u0407\u03ff\3\2\2\2\u0407\u0406\3\2\2\2\u0408\u008d\3\2"+
		"\2\2\u0409\u0474\5\u0086D\2\u040a\u040b\7\4\2\2\u040b\u040e\5\u00aaV\2"+
		"\u040c\u040d\7g\2\2\u040d\u040f\5\u00aaV\2\u040e\u040c\3\2\2\2\u040e\u040f"+
		"\3\2\2\2\u040f\u0410\3\2\2\2\u0410\u0411\7`\2\2\u0411\u0474\3\2\2\2\u0412"+
		"\u0413\7\30\2\2\u0413\u0414\5\u00a6T\2\u0414\u0417\5\u008eH\2\u0415\u0416"+
		"\7\21\2\2\u0416\u0418\5\u008eH\2\u0417\u0415\3\2\2\2\u0417\u0418\3\2\2"+
		"\2\u0418\u0474\3\2\2\2\u0419\u041a\7\27\2\2\u041a\u041b\7Z\2\2\u041b\u041c"+
		"\5\u00a0Q\2\u041c\u041d\7[\2\2\u041d\u041e\5\u008eH\2\u041e\u0474\3\2"+
		"\2\2\u041f\u0420\7\64\2\2\u0420\u0421\5\u00a6T\2\u0421\u0422\5\u008eH"+
		"\2\u0422\u0474\3\2\2\2\u0423\u0424\7\17\2\2\u0424\u0425\5\u008eH\2\u0425"+
		"\u0426\7\64\2\2\u0426\u0427\5\u00a6T\2\u0427\u0428\7`\2\2\u0428\u0474"+
		"\3\2\2\2\u0429\u042a\7\61\2\2\u042a\u0434\5\u0086D\2\u042b\u042d\5\u0090"+
		"I\2\u042c\u042b\3\2\2\2\u042d\u042e\3\2\2\2\u042e\u042c\3\2\2\2\u042e"+
		"\u042f\3\2\2\2\u042f\u0431\3\2\2\2\u0430\u0432\5\u0094K\2\u0431\u0430"+
		"\3\2\2\2\u0431\u0432\3\2\2\2\u0432\u0435\3\2\2\2\u0433\u0435\5\u0094K"+
		"\2\u0434\u042c\3\2\2\2\u0434\u0433\3\2\2\2\u0435\u0474\3\2\2\2\u0436\u0437"+
		"\7\61\2\2\u0437\u0438\5\u0096L\2\u0438\u043c\5\u0086D\2\u0439\u043b\5"+
		"\u0090I\2\u043a\u0439\3\2\2\2\u043b\u043e\3\2\2\2\u043c\u043a\3\2\2\2"+
		"\u043c\u043d\3\2\2\2\u043d\u0440\3\2\2\2\u043e\u043c\3\2\2\2\u043f\u0441"+
		"\5\u0094K\2\u0440\u043f\3\2\2\2\u0440\u0441\3\2\2\2\u0441\u0474\3\2\2"+
		"\2\u0442\u0443\7+\2\2\u0443\u0444\5\u00a6T\2\u0444\u0448\7\\\2\2\u0445"+
		"\u0447\5\u009cO\2\u0446\u0445\3\2\2\2\u0447\u044a\3\2\2\2\u0448\u0446"+
		"\3\2\2\2\u0448\u0449\3\2\2\2\u0449\u044e\3\2\2\2\u044a\u0448\3\2\2\2\u044b"+
		"\u044d\5\u009eP\2\u044c\u044b\3\2\2\2\u044d\u0450\3\2\2\2\u044e\u044c"+
		"\3\2\2\2\u044e\u044f\3\2\2\2\u044f\u0451\3\2\2\2\u0450\u044e\3\2\2\2\u0451"+
		"\u0452\7]\2\2\u0452\u0474\3\2\2\2\u0453\u0454\7,\2\2\u0454\u0455\5\u00a6"+
		"T\2\u0455\u0456\5\u0086D\2\u0456\u0474\3\2\2\2\u0457\u0459\7&\2\2\u0458"+
		"\u045a\5\u00aaV\2\u0459\u0458\3\2\2\2\u0459\u045a\3\2\2\2\u045a\u045b"+
		"\3\2\2\2\u045b\u0474\7`\2\2\u045c\u045d\7.\2\2\u045d\u045e\5\u00aaV\2"+
		"\u045e\u045f\7`\2\2\u045f\u0474\3\2\2\2\u0460\u0462\7\6\2\2\u0461\u0463"+
		"\5\u00d8m\2\u0462\u0461\3\2\2\2\u0462\u0463\3\2\2\2\u0463\u0464\3\2\2"+
		"\2\u0464\u0474\7`\2\2\u0465\u0467\7\r\2\2\u0466\u0468\5\u00d8m\2\u0467"+
		"\u0466\3\2\2\2\u0467\u0468\3\2\2\2\u0468\u0469\3\2\2\2\u0469\u0474\7`"+
		"\2\2\u046a\u0474\7`\2\2\u046b\u046c\5\u00aaV\2\u046c\u046d\7`\2\2\u046d"+
		"\u0474\3\2\2\2\u046e\u046f\5\u00d8m\2\u046f\u0470\7g\2\2\u0470\u0471\5"+
		"\u008eH\2\u0471\u0474\3\2\2\2\u0472\u0474\5\u00dco\2\u0473\u0409\3\2\2"+
		"\2\u0473\u040a\3\2\2\2\u0473\u0412\3\2\2\2\u0473\u0419\3\2\2\2\u0473\u041f"+
		"\3\2\2\2\u0473\u0423\3\2\2\2\u0473\u0429\3\2\2\2\u0473\u0436\3\2\2\2\u0473"+
		"\u0442\3\2\2\2\u0473\u0453\3\2\2\2\u0473\u0457\3\2\2\2\u0473\u045c\3\2"+
		"\2\2\u0473\u0460\3\2\2\2\u0473\u0465\3\2\2\2\u0473\u046a\3\2\2\2\u0473"+
		"\u046b\3\2\2\2\u0473\u046e\3\2\2\2\u0473\u0472\3\2\2\2\u0474\u008f\3\2"+
		"\2\2\u0475\u0476\7\t\2\2\u0476\u047a\7Z\2\2\u0477\u0479\5\24\13\2\u0478"+
		"\u0477\3\2\2\2\u0479\u047c\3\2\2\2\u047a\u0478\3\2\2\2\u047a\u047b\3\2"+
		"\2\2\u047b\u047d\3\2\2\2\u047c\u047a\3\2\2\2\u047d\u047e\5\u0092J\2\u047e"+
		"\u047f\5\u00d8m\2\u047f\u0480\7[\2\2\u0480\u0481\5\u0086D\2\u0481\u0091"+
		"\3\2\2\2\u0482\u0487\5d\63\2\u0483\u0484\7\u0084\2\2\u0484\u0486\5d\63"+
		"\2\u0485\u0483\3\2\2\2\u0486\u0489\3\2\2\2\u0487\u0485\3\2\2\2\u0487\u0488"+
		"\3\2\2\2\u0488\u0093\3\2\2\2\u0489\u0487\3\2\2\2\u048a\u048b\7\25\2\2"+
		"\u048b\u048c\5\u0086D\2\u048c\u0095\3\2\2\2\u048d\u048e\7Z\2\2\u048e\u0490"+
		"\5\u0098M\2\u048f\u0491\7`\2\2\u0490\u048f\3\2\2\2\u0490\u0491\3\2\2\2"+
		"\u0491\u0492\3\2\2\2\u0492\u0493\7[\2\2\u0493\u0097\3\2\2\2\u0494\u0499"+
		"\5\u009aN\2\u0495\u0496\7`\2\2\u0496\u0498\5\u009aN\2\u0497\u0495\3\2"+
		"\2\2\u0498\u049b\3\2\2\2\u0499\u0497\3\2\2\2\u0499\u049a\3\2\2\2\u049a"+
		"\u0099\3\2\2\2\u049b\u0499\3\2\2\2\u049c\u049e\5\24\13\2\u049d\u049c\3"+
		"\2\2\2\u049e\u04a1\3\2\2\2\u049f\u049d\3\2\2\2\u049f\u04a0\3\2\2\2\u04a0"+
		"\u04a2\3\2\2\2\u04a1\u049f\3\2\2\2\u04a2\u04a3\5V,\2\u04a3\u04a4\5P)\2"+
		"\u04a4\u04a5\7c\2\2\u04a5\u04a6\5\u00aaV\2\u04a6\u009b\3\2\2\2\u04a7\u04a9"+
		"\5\u009eP\2\u04a8\u04a7\3\2\2\2\u04a9\u04aa\3\2\2\2\u04aa\u04a8\3\2\2"+
		"\2\u04aa\u04ab\3\2\2\2\u04ab\u04ad\3\2\2\2\u04ac\u04ae\5\u0088E\2\u04ad"+
		"\u04ac\3\2\2\2\u04ae\u04af\3\2\2\2\u04af\u04ad\3\2\2\2\u04af\u04b0\3\2"+
		"\2\2\u04b0\u009d\3\2\2\2\u04b1\u04b4\7\b\2\2\u04b2\u04b5\5\u00aaV\2\u04b3"+
		"\u04b5\5\u00d8m\2\u04b4\u04b2\3\2\2\2\u04b4\u04b3\3\2\2\2\u04b5\u04b6"+
		"\3\2\2\2\u04b6\u04b7\7g\2\2\u04b7\u04bb\3\2\2\2\u04b8\u04b9\7\16\2\2\u04b9"+
		"\u04bb\7g\2\2\u04ba\u04b1\3\2\2\2\u04ba\u04b8\3\2\2\2\u04bb\u009f\3\2"+
		"\2\2\u04bc\u04c9\5\u00a4S\2\u04bd\u04bf\5\u00a2R\2\u04be\u04bd\3\2\2\2"+
		"\u04be\u04bf\3\2\2\2\u04bf\u04c0\3\2\2\2\u04c0\u04c2\7`\2\2\u04c1\u04c3"+
		"\5\u00aaV\2\u04c2\u04c1\3\2\2\2\u04c2\u04c3\3\2\2\2\u04c3\u04c4\3\2\2"+
		"\2\u04c4\u04c6\7`\2\2\u04c5\u04c7\5\u00a8U\2\u04c6\u04c5\3\2\2\2\u04c6"+
		"\u04c7\3\2\2\2\u04c7\u04c9\3\2\2\2\u04c8\u04bc\3\2\2\2\u04c8\u04be\3\2"+
		"\2\2\u04c9\u00a1\3\2\2\2\u04ca\u04cd\5\u008aF\2\u04cb\u04cd\5\u00a8U\2"+
		"\u04cc\u04ca\3\2\2\2\u04cc\u04cb\3\2\2\2\u04cd\u00a3\3\2\2\2\u04ce\u04d0"+
		"\5\24\13\2\u04cf\u04ce\3\2\2\2\u04d0\u04d3\3\2\2\2\u04d1\u04cf\3\2\2\2"+
		"\u04d1\u04d2\3\2\2\2\u04d2\u04d4\3\2\2\2\u04d3\u04d1\3\2\2\2\u04d4\u04d5"+
		"\5\u00ccg\2\u04d5\u04d6\5P)\2\u04d6\u04d7\7g\2\2\u04d7\u04d8\5\u00aaV"+
		"\2\u04d8\u00a5\3\2\2\2\u04d9\u04da\7Z\2\2\u04da\u04db\5\u00aaV\2\u04db"+
		"\u04dc\7[\2\2\u04dc\u00a7\3\2\2\2\u04dd\u04e2\5\u00aaV\2\u04de\u04df\7"+
		"a\2\2\u04df\u04e1\5\u00aaV\2\u04e0\u04de\3\2\2\2\u04e1\u04e4\3\2\2\2\u04e2"+
		"\u04e0\3\2\2\2\u04e2\u04e3\3\2\2\2\u04e3\u00a9\3\2\2\2\u04e4\u04e2\3\2"+
		"\2\2\u04e5\u04e6\bV\1\2\u04e6\u050c\5\u00b4[\2\u04e7\u04e8\5\u00acW\2"+
		"\u04e8\u04ea\7Z\2\2\u04e9\u04eb\5\u00a8U\2\u04ea\u04e9\3\2\2\2\u04ea\u04eb"+
		"\3\2\2\2\u04eb\u04ec\3\2\2\2\u04ec\u04ed\7[\2\2\u04ed\u050c\3\2\2\2\u04ee"+
		"\u04ef\7!\2\2\u04ef\u050c\5\u00b8]\2\u04f0\u04f1\7Z\2\2\u04f1\u04f2\5"+
		"\u00ccg\2\u04f2\u04f3\7[\2\2\u04f3\u04f4\5\u00aaV\30\u04f4\u050c\3\2\2"+
		"\2\u04f5\u04f6\t\5\2\2\u04f6\u050c\5\u00aaV\26\u04f7\u04f8\t\6\2\2\u04f8"+
		"\u050c\5\u00aaV\25\u04f9\u050c\5\u00aeX\2\u04fa\u04fb\5\u00ccg\2\u04fb"+
		"\u0501\7\u008c\2\2\u04fc\u04fe\5\u00d0i\2\u04fd\u04fc\3\2\2\2\u04fd\u04fe"+
		"\3\2\2\2\u04fe\u04ff\3\2\2\2\u04ff\u0502\5\u00d8m\2\u0500\u0502\7!\2\2"+
		"\u0501\u04fd\3\2\2\2\u0501\u0500\3\2\2\2\u0502\u050c\3\2\2\2\u0503\u0504"+
		"\5\u00b6\\\2\u0504\u0506\7\u008c\2\2\u0505\u0507\5\u00d0i\2\u0506\u0505"+
		"\3\2\2\2\u0506\u0507\3\2\2\2\u0507\u0508\3\2\2\2\u0508\u0509\7!\2\2\u0509"+
		"\u050c\3\2\2\2\u050a\u050c\5\u00dep\2\u050b\u04e5\3\2\2\2\u050b\u04e7"+
		"\3\2\2\2\u050b\u04ee\3\2\2\2\u050b\u04f0\3\2\2\2\u050b\u04f5\3\2\2\2\u050b"+
		"\u04f7\3\2\2\2\u050b\u04f9\3\2\2\2\u050b\u04fa\3\2\2\2\u050b\u0503\3\2"+
		"\2\2\u050b\u050a\3\2\2\2\u050c\u0569\3\2\2\2\u050d\u050e\f\24\2\2\u050e"+
		"\u050f\t\7\2\2\u050f\u0568\5\u00aaV\25\u0510\u0511\f\23\2\2\u0511\u0512"+
		"\t\b\2\2\u0512\u0568\5\u00aaV\24\u0513\u051a\f\22\2\2\u0514\u051b\7\u0080"+
		"\2\2\u0515\u0516\7d\2\2\u0516\u0517\7d\2\2\u0517\u051b\7d\2\2\u0518\u0519"+
		"\7d\2\2\u0519\u051b\7d\2\2\u051a\u0514\3\2\2\2\u051a\u0515\3\2\2\2\u051a"+
		"\u0518\3\2\2\2\u051b\u051c\3\2\2\2\u051c\u0568\5\u00aaV\23\u051d\u051e"+
		"\f\21\2\2\u051e\u051f\t\t\2\2\u051f\u0568\5\u00aaV\22\u0520\u0521\f\17"+
		"\2\2\u0521\u0522\t\n\2\2\u0522\u0568\5\u00aaV\20\u0523\u0524\f\16\2\2"+
		"\u0524\u0525\7r\2\2\u0525\u0568\5\u00aaV\17\u0526\u0527\f\r\2\2\u0527"+
		"\u0528\7s\2\2\u0528\u0568\5\u00aaV\16\u0529\u052a\f\f\2\2\u052a\u052b"+
		"\7\u0084\2\2\u052b\u0568\5\u00aaV\r\u052c\u052d\f\13\2\2\u052d\u052e\7"+
		"l\2\2\u052e\u0568\5\u00aaV\f\u052f\u0530\f\n\2\2\u0530\u0531\7m\2\2\u0531"+
		"\u0568\5\u00aaV\13\u0532\u0533\f\t\2\2\u0533\u0534\7\u0085\2\2\u0534\u0535"+
		"\5\u00aaV\2\u0535\u0536\7g\2\2\u0536\u0537\5\u00aaV\n\u0537\u0568\3\2"+
		"\2\2\u0538\u0539\f\b\2\2\u0539\u053a\t\13\2\2\u053a\u0568\5\u00aaV\b\u053b"+
		"\u053c\f\34\2\2\u053c\u054e\7b\2\2\u053d\u054f\5\u00d8m\2\u053e\u054f"+
		"\7-\2\2\u053f\u0541\7!\2\2\u0540\u0542\5\u00c8e\2\u0541\u0540\3\2\2\2"+
		"\u0541\u0542\3\2\2\2\u0542\u0543\3\2\2\2\u0543\u054f\5\u00bc_\2\u0544"+
		"\u0545\7*\2\2\u0545\u054f\5\u00d2j\2\u0546\u054f\5\u00c2b\2\u0547\u0548"+
		"\5\u00acW\2\u0548\u054a\7Z\2\2\u0549\u054b\5\u00a8U\2\u054a\u0549\3\2"+
		"\2\2\u054a\u054b\3\2\2\2\u054b\u054c\3\2\2\2\u054c\u054d\7[\2\2\u054d"+
		"\u054f\3\2\2\2\u054e\u053d\3\2\2\2\u054e\u053e\3\2\2\2\u054e\u053f\3\2"+
		"\2\2\u054e\u0544\3\2\2\2\u054e\u0546\3\2\2\2\u054e\u0547\3\2\2\2\u054f"+
		"\u0568\3\2\2\2\u0550\u0551\f\33\2\2\u0551\u0552\7^\2\2\u0552\u0553\5\u00aa"+
		"V\2\u0553\u0554\7_\2\2\u0554\u0568\3\2\2\2\u0555\u0556\f\27\2\2\u0556"+
		"\u0568\t\f\2\2\u0557\u0558\f\20\2\2\u0558\u0559\7\34\2\2\u0559\u0568\5"+
		"\u00ccg\2\u055a\u055b\f\6\2\2\u055b\u055d\7\u008c\2\2\u055c\u055e\5\u00d0"+
		"i\2\u055d\u055c\3\2\2\2\u055d\u055e\3\2\2\2\u055e\u055f\3\2\2\2\u055f"+
		"\u0565\5\u00d8m\2\u0560\u0562\7Z\2\2\u0561\u0563\5\u00a8U\2\u0562\u0561"+
		"\3\2\2\2\u0562\u0563\3\2\2\2\u0563\u0564\3\2\2\2\u0564\u0566\7[\2\2\u0565"+
		"\u0560\3\2\2\2\u0565\u0566\3\2\2\2\u0566\u0568\3\2\2\2\u0567\u050d\3\2"+
		"\2\2\u0567\u0510\3\2\2\2\u0567\u0513\3\2\2\2\u0567\u051d\3\2\2\2\u0567"+
		"\u0520\3\2\2\2\u0567\u0523\3\2\2\2\u0567\u0526\3\2\2\2\u0567\u0529\3\2"+
		"\2\2\u0567\u052c\3\2\2\2\u0567\u052f\3\2\2\2\u0567\u0532\3\2\2\2\u0567"+
		"\u0538\3\2\2\2\u0567\u053b\3\2\2\2\u0567\u0550\3\2\2\2\u0567\u0555\3\2"+
		"\2\2\u0567\u0557\3\2\2\2\u0567\u055a\3\2\2\2\u0568\u056b\3\2\2\2\u0569"+
		"\u0567\3\2\2\2\u0569\u056a\3\2\2\2\u056a\u00ab\3\2\2\2\u056b\u0569\3\2"+
		"\2\2\u056c\u056d\bW\1\2\u056d\u058a\7-\2\2\u056e\u058a\7*\2\2\u056f\u058a"+
		"\5\u00d8m\2\u0570\u0574\5\u00c8e\2\u0571\u0575\5\u00d4k\2\u0572\u0573"+
		"\7-\2\2\u0573\u0575\5\u00d6l\2\u0574\u0571\3\2\2\2\u0574\u0572\3\2\2\2"+
		"\u0575\u058a\3\2\2\2\u0576\u0577\7!\2\2\u0577\u058a\5\u00b8]\2\u0578\u058a"+
		"\5\u00aeX\2\u0579\u057a\5\u00ccg\2\u057a\u0580\7\u008c\2\2\u057b\u057d"+
		"\5\u00d0i\2\u057c\u057b\3\2\2\2\u057c\u057d\3\2\2\2\u057d\u057e\3\2\2"+
		"\2\u057e\u0581\5\u00d8m\2\u057f\u0581\7!\2\2\u0580\u057c\3\2\2\2\u0580"+
		"\u057f\3\2\2\2\u0581\u058a\3\2\2\2\u0582\u0583\5\u00b6\\\2\u0583\u0585"+
		"\7\u008c\2\2\u0584\u0586\5\u00d0i\2\u0585\u0584\3\2\2\2\u0585\u0586\3"+
		"\2\2\2\u0586\u0587\3\2\2\2\u0587\u0588\7!\2\2\u0588\u058a\3\2\2\2\u0589"+
		"\u056c\3\2\2\2\u0589\u056e\3\2\2\2\u0589\u056f\3\2\2\2\u0589\u0570\3\2"+
		"\2\2\u0589\u0576\3\2\2\2\u0589\u0578\3\2\2\2\u0589\u0579\3\2\2\2\u0589"+
		"\u0582\3\2\2\2\u058a\u059a\3\2\2\2\u058b\u058c\f\t\2\2\u058c\u058d\7^"+
		"\2\2\u058d\u058e\5\u00aaV\2\u058e\u058f\7_\2\2\u058f\u0599\3\2\2\2\u0590"+
		"\u0591\f\b\2\2\u0591\u0593\7Z\2\2\u0592\u0594\5\u00a8U\2\u0593\u0592\3"+
		"\2\2\2\u0593\u0594\3\2\2\2\u0594\u0595\3\2\2\2\u0595\u0599\7[\2\2\u0596"+
		"\u0597\f\7\2\2\u0597\u0599\t\f\2\2\u0598\u058b\3\2\2\2\u0598\u0590\3\2"+
		"\2\2\u0598\u0596\3\2\2\2\u0599\u059c\3\2\2\2\u059a\u0598\3\2\2\2\u059a"+
		"\u059b\3\2\2\2\u059b\u00ad\3\2\2\2\u059c\u059a\3\2\2\2\u059d\u059e\5\u00b0"+
		"Y\2\u059e\u059f\7\u008b\2\2\u059f\u05a0\5\u00b2Z\2\u05a0\u00af\3\2\2\2"+
		"\u05a1\u05b3\5\u00d8m\2\u05a2\u05a4\7Z\2\2\u05a3\u05a5\5^\60\2\u05a4\u05a3"+
		"\3\2\2\2\u05a4\u05a5\3\2\2\2\u05a5\u05a6\3\2\2\2\u05a6\u05b3\7[\2\2\u05a7"+
		"\u05a8\7Z\2\2\u05a8\u05ad\5\u00d8m\2\u05a9\u05aa\7a\2\2\u05aa\u05ac\5"+
		"\u00d8m\2\u05ab\u05a9\3\2\2\2\u05ac\u05af\3\2\2\2\u05ad\u05ab\3\2\2\2"+
		"\u05ad\u05ae\3\2\2\2\u05ae\u05b0\3\2\2\2\u05af\u05ad\3\2\2\2\u05b0\u05b1"+
		"\7[\2\2\u05b1\u05b3\3\2\2\2\u05b2\u05a1\3\2\2\2\u05b2\u05a2\3\2\2\2\u05b2"+
		"\u05a7\3\2\2\2\u05b3\u00b1\3\2\2\2\u05b4\u05b7\5\u00aaV\2\u05b5\u05b7"+
		"\5\u0086D\2\u05b6\u05b4\3\2\2\2\u05b6\u05b5\3\2\2\2\u05b7\u00b3\3\2\2"+
		"\2\u05b8\u05b9\7Z\2\2\u05b9\u05ba\5\u00aaV\2\u05ba\u05bb\7[\2\2\u05bb"+
		"\u05cb\3\2\2\2\u05bc\u05cb\7-\2\2\u05bd\u05cb\7*\2\2\u05be\u05cb\5f\64"+
		"\2\u05bf\u05cb\5\u00d8m\2\u05c0\u05c1\5\64\33\2\u05c1\u05c2\7b\2\2\u05c2"+
		"\u05c3\7\13\2\2\u05c3\u05cb\3\2\2\2\u05c4\u05c8\5\u00c8e\2\u05c5\u05c9"+
		"\5\u00d4k\2\u05c6\u05c7\7-\2\2\u05c7\u05c9\5\u00d6l\2\u05c8\u05c5\3\2"+
		"\2\2\u05c8\u05c6\3\2\2\2\u05c9\u05cb\3\2\2\2\u05ca\u05b8\3\2\2\2\u05ca"+
		"\u05bc\3\2\2\2\u05ca\u05bd\3\2\2\2\u05ca\u05be\3\2\2\2\u05ca\u05bf\3\2"+
		"\2\2\u05ca\u05c0\3\2\2\2\u05ca\u05c4\3\2\2\2\u05cb\u00b5\3\2\2\2\u05cc"+
		"\u05cd\5V,\2\u05cd\u05ce\7b\2\2\u05ce\u05d0\3\2\2\2\u05cf\u05cc\3\2\2"+
		"\2\u05cf\u05d0\3\2\2\2\u05d0\u05d4\3\2\2\2\u05d1\u05d3\5l\67\2\u05d2\u05d1"+
		"\3\2\2\2\u05d3\u05d6\3\2\2\2\u05d4\u05d2\3\2\2\2\u05d4\u05d5\3\2\2\2\u05d5"+
		"\u05d7\3\2\2\2\u05d6\u05d4\3\2\2\2\u05d7\u05d9\5\u00d8m\2\u05d8\u05da"+
		"\5\u00d0i\2\u05d9\u05d8\3\2\2\2\u05d9\u05da\3\2\2\2\u05da\u00b7\3\2\2"+
		"\2\u05db\u05dc\5\u00c8e\2\u05dc\u05dd\5\u00ba^\2\u05dd\u05de\5\u00c0a"+
		"\2\u05de\u05e5\3\2\2\2\u05df\u05e2\5\u00ba^\2\u05e0\u05e3\5\u00be`\2\u05e1"+
		"\u05e3\5\u00c0a\2\u05e2\u05e0\3\2\2\2\u05e2\u05e1\3\2\2\2\u05e3\u05e5"+
		"\3\2\2\2\u05e4\u05db\3\2\2\2\u05e4\u05df\3\2\2\2\u05e5\u00b9\3\2\2\2\u05e6"+
		"\u05e8\5\u00d8m\2\u05e7\u05e9\5\u00c4c\2\u05e8\u05e7\3\2\2\2\u05e8\u05e9"+
		"\3\2\2\2\u05e9\u05f1\3\2\2\2\u05ea\u05eb\7b\2\2\u05eb\u05ed\5\u00d8m\2"+
		"\u05ec\u05ee\5\u00c4c\2\u05ed\u05ec\3\2\2\2\u05ed\u05ee\3\2\2\2\u05ee"+
		"\u05f0\3\2\2\2\u05ef\u05ea\3\2\2\2\u05f0\u05f3\3\2\2\2\u05f1\u05ef\3\2"+
		"\2\2\u05f1\u05f2\3\2\2\2\u05f2\u05f6\3\2\2\2\u05f3\u05f1\3\2\2\2\u05f4"+
		"\u05f6\5\u00ceh\2\u05f5\u05e6\3\2\2\2\u05f5\u05f4\3\2\2\2\u05f6\u00bb"+
		"\3\2\2\2\u05f7\u05f9\5\u00d8m\2\u05f8\u05fa\5\u00c6d\2\u05f9\u05f8\3\2"+
		"\2\2\u05f9\u05fa\3\2\2\2\u05fa\u05fb\3\2\2\2\u05fb\u05fc\5\u00c0a\2\u05fc"+
		"\u00bd\3\2\2\2\u05fd\u0619\7^\2\2\u05fe\u0603\7_\2\2\u05ff\u0600\7^\2"+
		"\2\u0600\u0602\7_\2\2\u0601\u05ff\3\2\2\2\u0602\u0605\3\2\2\2\u0603\u0601"+
		"\3\2\2\2\u0603\u0604\3\2\2\2\u0604\u0606\3\2\2\2\u0605\u0603\3\2\2\2\u0606"+
		"\u061a\5T+\2\u0607\u0608\5\u00aaV\2\u0608\u060f\7_\2\2\u0609\u060a\7^"+
		"\2\2\u060a\u060b\5\u00aaV\2\u060b\u060c\7_\2\2\u060c\u060e\3\2\2\2\u060d"+
		"\u0609\3\2\2\2\u060e\u0611\3\2\2\2\u060f\u060d\3\2\2\2\u060f\u0610\3\2"+
		"\2\2\u0610\u0616\3\2\2\2\u0611\u060f\3\2\2\2\u0612\u0613\7^\2\2\u0613"+
		"\u0615\7_\2\2\u0614\u0612\3\2\2\2\u0615\u0618\3\2\2\2\u0616\u0614\3\2"+
		"\2\2\u0616\u0617\3\2\2\2\u0617\u061a\3\2\2\2\u0618\u0616\3\2\2\2\u0619"+
		"\u05fe\3\2\2\2\u0619\u0607\3\2\2\2\u061a\u00bf\3\2\2\2\u061b\u061d\5\u00d6"+
		"l\2\u061c\u061e\5(\25\2\u061d\u061c\3\2\2\2\u061d\u061e\3\2\2\2\u061e"+
		"\u00c1\3\2\2\2\u061f\u0620\5\u00c8e\2\u0620\u0621\5\u00d4k\2\u0621\u00c3"+
		"\3\2\2\2\u0622\u0623\7e\2\2\u0623\u0626\7d\2\2\u0624\u0626\5\u00d0i\2"+
		"\u0625\u0622\3\2\2\2\u0625\u0624\3\2\2\2\u0626\u00c5\3\2\2\2\u0627\u0628"+
		"\7e\2\2\u0628\u062b\7d\2\2\u0629\u062b\5\u00c8e\2\u062a\u0627\3\2\2\2"+
		"\u062a\u0629\3\2\2\2\u062b\u00c7\3\2\2\2\u062c\u062d\7e\2\2\u062d\u062e"+
		"\5\u00caf\2\u062e\u062f\7d\2\2\u062f\u00c9\3\2\2\2\u0630\u0635\5\u00cc"+
		"g\2\u0631\u0632\7a\2\2\u0632\u0634\5\u00ccg\2\u0633\u0631\3\2\2\2\u0634"+
		"\u0637\3\2\2\2\u0635\u0633\3\2\2\2\u0635\u0636\3\2\2\2\u0636\u00cb\3\2"+
		"\2\2\u0637\u0635\3\2\2\2\u0638\u063a\5l\67\2\u0639\u0638\3\2\2\2\u0639"+
		"\u063a\3\2\2\2\u063a\u063d\3\2\2\2\u063b\u063e\5V,\2\u063c\u063e\5\u00ce"+
		"h\2\u063d\u063b\3\2\2\2\u063d\u063c\3\2\2\2\u063e\u0643\3\2\2\2\u063f"+
		"\u0640\7^\2\2\u0640\u0642\7_\2\2\u0641\u063f\3\2\2\2\u0642\u0645\3\2\2"+
		"\2\u0643\u0641\3\2\2\2\u0643\u0644\3\2\2\2\u0644\u00cd\3\2\2\2\u0645\u0643"+
		"\3\2\2\2\u0646\u0647\t\r\2\2\u0647\u00cf\3\2\2\2\u0648\u0649\7e\2\2\u0649"+
		"\u064e\5X-\2\u064a\u064b\7a\2\2\u064b\u064d\5X-\2\u064c\u064a\3\2\2\2"+
		"\u064d\u0650\3\2\2\2\u064e\u064c\3\2\2\2\u064e\u064f\3\2\2\2\u064f\u0651"+
		"\3\2\2\2\u0650\u064e\3\2\2\2\u0651\u0652\7d\2\2\u0652\u00d1\3\2\2\2\u0653"+
		"\u065a\5\u00d6l\2\u0654\u0655\7b\2\2\u0655\u0657\5\u00d8m\2\u0656\u0658"+
		"\5\u00d6l\2\u0657\u0656\3\2\2\2\u0657\u0658\3\2\2\2\u0658\u065a\3\2\2"+
		"\2\u0659\u0653\3\2\2\2\u0659\u0654\3\2\2\2\u065a\u00d3\3\2\2\2\u065b\u065c"+
		"\7*\2\2\u065c\u0661\5\u00d2j\2\u065d\u065e\5\u00d8m\2\u065e\u065f\5\u00d6"+
		"l\2\u065f\u0661\3\2\2\2\u0660\u065b\3\2\2\2\u0660\u065d\3\2\2\2\u0661"+
		"\u00d5\3\2\2\2\u0662\u0664\7Z\2\2\u0663\u0665\5\u00a8U\2\u0664\u0663\3"+
		"\2\2\2\u0664\u0665\3\2\2\2\u0665\u0666\3\2\2\2\u0666\u0667\7[\2\2\u0667"+
		"\u00d7\3\2\2\2\u0668\u0669\t\16\2\2\u0669\u00d9\3\2\2\2\u066a\u0673\5"+
		"\u00e4s\2\u066b\u0673\5\u00e6t\2\u066c\u0673\5\u00e8u\2\u066d\u0673\5"+
		"\u00eex\2\u066e\u0673\5\u0116\u008c\2\u066f\u0673\5\u0118\u008d\2\u0670"+
		"\u0673\5\u011a\u008e\2\u0671\u0673\5\u011c\u008f\2\u0672\u066a\3\2\2\2"+
		"\u0672\u066b\3\2\2\2\u0672\u066c\3\2\2\2\u0672\u066d\3\2\2\2\u0672\u066e"+
		"\3\2\2\2\u0672\u066f\3\2\2\2\u0672\u0670\3\2\2\2\u0672\u0671\3\2\2\2\u0673"+
		"\u00db\3\2\2\2\u0674\u0675\5\u00e2r\2\u0675\u00dd\3\2\2\2\u0676\u0679"+
		"\5\u0106\u0084\2\u0677\u0679\5\u00e0q\2\u0678\u0676\3\2\2\2\u0678\u0677"+
		"\3\2\2\2\u0679\u00df\3\2\2\2\u067a\u067b\7\u008a\2\2\u067b\u00e1\3\2\2"+
		"\2\u067c\u0689\7\65\2\2\u067d\u0686\7Z\2\2\u067e\u0683\5\u0102\u0082\2"+
		"\u067f\u0680\7a\2\2\u0680\u0682\5\u0102\u0082\2\u0681\u067f\3\2\2\2\u0682"+
		"\u0685\3\2\2\2\u0683\u0681\3\2\2\2\u0683\u0684\3\2\2\2\u0684\u0687\3\2"+
		"\2\2\u0685\u0683\3\2\2\2\u0686\u067e\3\2\2\2\u0686\u0687\3\2\2\2\u0687"+
		"\u0688\3\2\2\2\u0688\u068a\7[\2\2\u0689\u067d\3\2\2\2\u0689\u068a\3\2"+
		"\2\2\u068a\u068b\3\2\2\2\u068b\u068f\7\\\2\2\u068c\u068e\5\u00f4{\2\u068d"+
		"\u068c\3\2\2\2\u068e\u0691\3\2\2\2\u068f\u068d\3\2\2\2\u068f\u0690\3\2"+
		"\2\2\u0690\u0692\3\2\2\2\u0691\u068f\3\2\2\2\u0692\u0693\7]\2\2\u0693"+
		"\u00e3\3\2\2\2\u0694\u0695\7\66\2\2\u0695\u0696\5\u0140\u00a1\2\u0696"+
		"\u0698\7Z\2\2\u0697\u0699\5\u00f8}\2\u0698\u0697\3\2\2\2\u0698\u0699\3"+
		"\2\2\2\u0699\u069a\3\2\2\2\u069a\u069b\7[\2\2\u069b\u069c\7\23\2\2\u069c"+
		"\u069d\5\u0102\u0082\2\u069d\u06a1\7\\\2\2\u069e\u06a0\5\u00f2z\2\u069f"+
		"\u069e\3\2\2\2\u06a0\u06a3\3\2\2\2\u06a1\u069f\3\2\2\2\u06a1\u06a2\3\2"+
		"\2\2\u06a2\u06a4\3\2\2\2\u06a3\u06a1\3\2\2\2\u06a4\u06a5\7]\2\2\u06a5"+
		"\u00e5\3\2\2\2\u06a6\u06a7\7\67\2\2\u06a7\u06b1\7\\\2\2\u06a8\u06aa\7"+
		"b\2\2\u06a9\u06a8\3\2\2\2\u06aa\u06ad\3\2\2\2\u06ab\u06a9\3\2\2\2\u06ab"+
		"\u06ac\3\2\2\2\u06ac\u06ae\3\2\2\2\u06ad\u06ab\3\2\2\2\u06ae\u06b0\7\u0087"+
		"\2\2\u06af\u06ab\3\2\2\2\u06b0\u06b3\3\2\2\2\u06b1\u06af\3\2\2\2\u06b1"+
		"\u06b2\3\2\2\2\u06b2\u06b4\3\2\2\2\u06b3\u06b1\3\2\2\2\u06b4\u06b9\5\u0140"+
		"\u00a1\2\u06b5\u06b6\t\17\2\2\u06b6\u06b8\5\u0140\u00a1\2\u06b7\u06b5"+
		"\3\2\2\2\u06b8\u06bb\3\2\2\2\u06b9\u06b7\3\2\2\2\u06b9\u06ba\3\2\2\2\u06ba"+
		"\u06bc\3\2\2\2\u06bb\u06b9\3\2\2\2\u06bc\u06bd\7]\2\2\u06bd\u00e7\3\2"+
		"\2\2\u06be\u06c0\78\2\2\u06bf\u06c1\5\u00eav\2\u06c0\u06bf\3\2\2\2\u06c0"+
		"\u06c1\3\2\2\2\u06c1\u06c2\3\2\2\2\u06c2\u06c3\5\u00f0y\2\u06c3\u00e9"+
		"\3\2\2\2\u06c4\u06c5\7\u00e9\2\2\u06c5\u06ca\7\u00eb\2\2\u06c6\u06c7\7"+
		"a\2\2\u06c7\u06c9\7\u00eb\2\2\u06c8\u06c6\3\2\2\2\u06c9\u06cc\3\2\2\2"+
		"\u06ca\u06c8\3\2\2\2\u06ca\u06cb\3\2\2\2\u06cb\u06cd\3\2\2\2\u06cc\u06ca"+
		"\3\2\2\2\u06cd\u06ce\7\u00ea\2\2\u06ce\u00eb\3\2\2\2\u06cf\u06d0\7\u0093"+
		"\2\2\u06d0\u06d1\5\u0142\u00a2\2\u06d1\u06d2\7\u0098\2\2\u06d2\u00ed\3"+
		"\2\2\2\u06d3\u06d4\7=\2\2\u06d4\u06d5\5\u00f0y\2\u06d5\u00ef\3\2\2\2\u06d6"+
		"\u06db\7\u00e8\2\2\u06d7\u06da\5\u00f0y\2\u06d8\u06da\7\u00f1\2\2\u06d9"+
		"\u06d7\3\2\2\2\u06d9\u06d8\3\2\2\2\u06da\u06dd\3\2\2\2\u06db\u06dc\3\2"+
		"\2\2\u06db\u06d9\3\2\2\2\u06dc\u06de\3\2\2\2\u06dd\u06db\3\2\2\2\u06de"+
		"\u06e9\7\u00f0\2\2\u06df\u06e4\t\20\2\2\u06e0\u06e3\5\u00f0y\2\u06e1\u06e3"+
		"\7\u00f4\2\2\u06e2\u06e0\3\2\2\2\u06e2\u06e1\3\2\2\2\u06e3\u06e6\3\2\2"+
		"\2\u06e4\u06e5\3\2\2\2\u06e4\u06e2\3\2\2\2\u06e5\u06e7\3\2\2\2\u06e6\u06e4"+
		"\3\2\2\2\u06e7\u06e9\7\u00f3\2\2\u06e8\u06d6\3\2\2\2\u06e8\u06df\3\2\2"+
		"\2\u06e9\u00f1\3\2\2\2\u06ea\u06eb\7>\2\2\u06eb\u06ec\5\u0140\u00a1\2"+
		"\u06ec\u06f0\7\\\2\2\u06ed\u06ef\5\u00f4{\2\u06ee\u06ed\3\2\2\2\u06ef"+
		"\u06f2\3\2\2\2\u06f0\u06ee\3\2\2\2\u06f0\u06f1\3\2\2\2\u06f1\u06f3\3\2"+
		"\2\2\u06f2\u06f0\3\2\2\2\u06f3\u06f4\7]\2\2\u06f4\u00f3\3\2\2\2\u06f5"+
		"\u06f8\5\u00fc\177\2\u06f6\u06f7\t\21\2\2\u06f7\u06f9\5\u00fe\u0080\2"+
		"\u06f8\u06f6\3\2\2\2\u06f8\u06f9\3\2\2\2\u06f9\u06fa\3\2\2\2\u06fa\u06fb"+
		"\7\u008b\2\2\u06fb\u06fc\5\u00f6|\2\u06fc\u070e\3\2\2\2\u06fd\u0700\5"+
		"\u00fc\177\2\u06fe\u06ff\t\21\2\2\u06ff\u0701\5\u00fe\u0080\2\u0700\u06fe"+
		"\3\2\2\2\u0700\u0701\3\2\2\2\u0701\u0702\3\2\2\2\u0702\u0703\7\u008b\2"+
		"\2\u0703\u0704\5\u0102\u0082\2\u0704\u070e\3\2\2\2\u0705\u0706\5\u00fe"+
		"\u0080\2\u0706\u0707\7\u008b\2\2\u0707\u0708\5\u00f6|\2\u0708\u070e\3"+
		"\2\2\2\u0709\u070a\5\u00fe\u0080\2\u070a\u070b\7\u008b\2\2\u070b\u070c"+
		"\5\u0102\u0082\2\u070c\u070e\3\2\2\2\u070d\u06f5\3\2\2\2\u070d\u06fd\3"+
		"\2\2\2\u070d\u0705\3\2\2\2\u070d\u0709\3\2\2\2\u070e\u00f5\3\2\2\2\u070f"+
		"\u0714\7\\\2\2\u0710\u0713\5\u00f6|\2\u0711\u0713\5\u0088E\2\u0712\u0710"+
		"\3\2\2\2\u0712\u0711\3\2\2\2\u0713\u0716\3\2\2\2\u0714\u0715\3\2\2\2\u0714"+
		"\u0712\3\2\2\2\u0715\u0717\3\2\2\2\u0716\u0714\3\2\2\2\u0717\u0718\7]"+
		"\2\2\u0718\u00f7\3\2\2\2\u0719\u071e\5\u00fa~\2\u071a\u071b\7a\2\2\u071b"+
		"\u071d\5\u00fa~\2\u071c\u071a\3\2\2\2\u071d\u0720\3\2\2\2\u071e\u071c"+
		"\3\2\2\2\u071e\u071f\3\2\2\2\u071f\u00f9\3\2\2\2\u0720\u071e\3\2\2\2\u0721"+
		"\u0723\5\u0140\u00a1\2\u0722\u0724\7g\2\2\u0723\u0722\3\2\2\2\u0723\u0724"+
		"\3\2\2\2\u0724\u0725\3\2\2\2\u0725\u0726\5\u0140\u00a1\2\u0726\u00fb\3"+
		"\2\2\2\u0727\u072c\5\u010a\u0086\2\u0728\u0729\7a\2\2\u0729\u072b\5\u010a"+
		"\u0086\2\u072a\u0728\3\2\2\2\u072b\u072e\3\2\2\2\u072c\u072a\3\2\2\2\u072c"+
		"\u072d\3\2\2\2\u072d\u0738\3\2\2\2\u072e\u072c\3\2\2\2\u072f\u0730\7N"+
		"\2\2\u0730\u0735\5\u0100\u0081\2\u0731\u0732\7a\2\2\u0732\u0734\5\u0100"+
		"\u0081\2\u0733\u0731\3\2\2\2\u0734\u0737\3\2\2\2\u0735\u0733\3\2\2\2\u0735"+
		"\u0736\3\2\2\2\u0736\u0739\3\2\2\2\u0737\u0735\3\2\2\2\u0738\u072f\3\2"+
		"\2\2\u0738\u0739\3\2\2\2\u0739\u0753\3\2\2\2\u073a\u073b\7Z\2\2\u073b"+
		"\u0740\5\u010a\u0086\2\u073c\u073d\7a\2\2\u073d\u073f\5\u010a\u0086\2"+
		"\u073e\u073c\3\2\2\2\u073f\u0742\3\2\2\2\u0740\u073e\3\2\2\2\u0740\u0741"+
		"\3\2\2\2\u0741\u0743\3\2\2\2\u0742\u0740\3\2\2\2\u0743\u0750\7[\2\2\u0744"+
		"\u0745\7N\2\2\u0745\u0746\7Z\2\2\u0746\u074b\5\u0100\u0081\2\u0747\u0748"+
		"\7a\2\2\u0748\u074a\5\u0100\u0081\2\u0749\u0747\3\2\2\2\u074a\u074d\3"+
		"\2\2\2\u074b\u0749\3\2\2\2\u074b\u074c\3\2\2\2\u074c\u074e\3\2\2\2\u074d"+
		"\u074b\3\2\2\2\u074e\u074f\7[\2\2\u074f\u0751\3\2\2\2\u0750\u0744\3\2"+
		"\2\2\u0750\u0751\3\2\2\2\u0751\u0753\3\2\2\2\u0752\u0727\3\2\2\2\u0752"+
		"\u073a\3\2\2\2\u0753\u00fd\3\2\2\2\u0754\u0755\b\u0080\1\2\u0755\u0756"+
		"\5\u010a\u0086\2\u0756\u0757\7\u0080\2\2\u0757\u0758\5\u0102\u0082\2\u0758"+
		"\u0776\3\2\2\2\u0759\u075a\5\u0100\u0081\2\u075a\u075b\7d\2\2\u075b\u075c"+
		"\5\u0100\u0081\2\u075c\u0776\3\2\2\2\u075d\u075e\5\u0100\u0081\2\u075e"+
		"\u075f\7j\2\2\u075f\u0760\5\u0100\u0081\2\u0760\u0776\3\2\2\2\u0761\u0762"+
		"\5\u0100\u0081\2\u0762\u0763\7e\2\2\u0763\u0764\5\u0100\u0081\2\u0764"+
		"\u0776\3\2\2\2\u0765\u0766\5\u0100\u0081\2\u0766\u0767\7i\2\2\u0767\u0768"+
		"\5\u0100\u0081\2\u0768\u0776\3\2\2\2\u0769\u076a\5\u0100\u0081\2\u076a"+
		"\u076b\7h\2\2\u076b\u076c\5\u0100\u0081\2\u076c\u0776\3\2\2\2\u076d\u076e"+
		"\5\u0100\u0081\2\u076e\u076f\7k\2\2\u076f\u0770\5\u0100\u0081\2\u0770"+
		"\u0776\3\2\2\2\u0771\u0772\7Z\2\2\u0772\u0773\5\u00fe\u0080\2\u0773\u0774"+
		"\7[\2\2\u0774\u0776\3\2\2\2\u0775\u0754\3\2\2\2\u0775\u0759\3\2\2\2\u0775"+
		"\u075d\3\2\2\2\u0775\u0761\3\2\2\2\u0775\u0765\3\2\2\2\u0775\u0769\3\2"+
		"\2\2\u0775\u076d\3\2\2\2\u0775\u0771\3\2\2\2\u0776\u077f\3\2\2\2\u0777"+
		"\u0778\f\f\2\2\u0778\u0779\7l\2\2\u0779\u077e\5\u00fe\u0080\r\u077a\u077b"+
		"\f\13\2\2\u077b\u077c\7m\2\2\u077c\u077e\5\u00fe\u0080\f\u077d\u0777\3"+
		"\2\2\2\u077d\u077a\3\2\2\2\u077e\u0781\3\2\2\2\u077f\u077d\3\2\2\2\u077f"+
		"\u0780\3\2\2\2\u0780\u00ff\3\2\2\2\u0781\u077f\3\2\2\2\u0782\u0784\5\u0140"+
		"\u00a1\2\u0783\u0785\7\u0081\2\2\u0784\u0783\3\2\2\2\u0784\u0785\3\2\2"+
		"\2\u0785\u0796\3\2\2\2\u0786\u0787\5\u0140\u00a1\2\u0787\u0790\7Z\2\2"+
		"\u0788\u078d\5\u0100\u0081\2\u0789\u078a\7a\2\2\u078a\u078c\5\u0100\u0081"+
		"\2\u078b\u0789\3\2\2\2\u078c\u078f\3\2\2\2\u078d\u078b\3\2\2\2\u078d\u078e"+
		"\3\2\2\2\u078e\u0791\3\2\2\2\u078f\u078d\3\2\2\2\u0790\u0788\3\2\2\2\u0790"+
		"\u0791\3\2\2\2\u0791\u0792\3\2\2\2\u0792\u0793\7[\2\2\u0793\u0796\3\2"+
		"\2\2\u0794\u0796\5\u0110\u0089\2\u0795\u0782\3\2\2\2\u0795\u0786\3\2\2"+
		"\2\u0795\u0794\3\2\2\2\u0796\u0101\3\2\2\2\u0797\u0799\5\u0140\u00a1\2"+
		"\u0798\u0797\3\2\2\2\u0798\u0799\3\2\2\2\u0799\u079b\3\2\2\2\u079a\u079c"+
		"\7\u0089\2\2\u079b\u079a\3\2\2\2\u079b\u079c\3\2\2\2\u079c\u079d\3\2\2"+
		"\2\u079d\u079e\5\u0140\u00a1\2\u079e\u07a7\7Z\2\2\u079f\u07a4\5\u0102"+
		"\u0082\2\u07a0\u07a1\7a\2\2\u07a1\u07a3\5\u0102\u0082\2\u07a2\u07a0\3"+
		"\2\2\2\u07a3\u07a6\3\2\2\2\u07a4\u07a2\3\2\2\2\u07a4\u07a5\3\2\2\2\u07a5"+
		"\u07a8\3\2\2\2\u07a6\u07a4\3\2\2\2\u07a7\u079f\3\2\2\2\u07a7\u07a8\3\2"+
		"\2\2\u07a8\u07a9\3\2\2\2\u07a9\u07aa\7[\2\2\u07aa\u07cf\3\2\2\2\u07ab"+
		"\u07ad\5\u0140\u00a1\2\u07ac\u07ab\3\2\2\2\u07ac\u07ad\3\2\2\2\u07ad\u07af"+
		"\3\2\2\2\u07ae\u07b0\7\u0089\2\2\u07af\u07ae\3\2\2\2\u07af\u07b0\3\2\2"+
		"\2\u07b0\u07b1\3\2\2\2\u07b1\u07b2\5\u0140\u00a1\2\u07b2\u07bb\7^\2\2"+
		"\u07b3\u07b8\5\u0104\u0083\2\u07b4\u07b5\7a\2\2\u07b5\u07b7\5\u0104\u0083"+
		"\2\u07b6\u07b4\3\2\2\2\u07b7\u07ba\3\2\2\2\u07b8\u07b6\3\2\2\2\u07b8\u07b9"+
		"\3\2\2\2\u07b9\u07bc\3\2\2\2\u07ba\u07b8\3\2\2\2\u07bb\u07b3\3\2\2\2\u07bb"+
		"\u07bc\3\2\2\2\u07bc\u07bd\3\2\2\2\u07bd\u07be\7_\2\2\u07be\u07cf\3\2"+
		"\2\2\u07bf\u07c1\5\u0140\u00a1\2\u07c0\u07bf\3\2\2\2\u07c0\u07c1\3\2\2"+
		"\2\u07c1\u07c3\3\2\2\2\u07c2\u07c4\7\u0089\2\2\u07c3\u07c2\3\2\2\2\u07c3"+
		"\u07c4\3\2\2\2\u07c4\u07c5\3\2\2\2\u07c5\u07c7\5\u0140\u00a1\2\u07c6\u07c8"+
		"\7\u0081\2\2\u07c7\u07c6\3\2\2\2\u07c7\u07c8\3\2\2\2\u07c8\u07cf\3\2\2"+
		"\2\u07c9\u07cb\5\u0140\u00a1\2\u07ca\u07c9\3\2\2\2\u07ca\u07cb\3\2\2\2"+
		"\u07cb\u07cc\3\2\2\2\u07cc\u07cf\5\u0110\u0089\2\u07cd\u07cf\7\u0082\2"+
		"\2\u07ce\u0798\3\2\2\2\u07ce\u07ac\3\2\2\2\u07ce\u07c0\3\2\2\2\u07ce\u07ca"+
		"\3\2\2\2\u07ce\u07cd\3\2\2\2\u07cf\u0103\3\2\2\2\u07d0\u07d1\5\u0140\u00a1"+
		"\2\u07d1\u07d2\7c\2\2\u07d2\u07d3\5\u0102\u0082\2\u07d3\u0105\3\2\2\2"+
		"\u07d4\u07d5\7\u0089\2\2\u07d5\u07d6\5\u0140\u00a1\2\u07d6\u07df\7^\2"+
		"\2\u07d7\u07dc\5\u0104\u0083\2\u07d8\u07d9\7a\2\2\u07d9\u07db\5\u0104"+
		"\u0083\2\u07da\u07d8\3\2\2\2\u07db\u07de\3\2\2\2\u07dc\u07da\3\2\2\2\u07dc"+
		"\u07dd\3\2\2\2\u07dd\u07e0\3\2\2\2\u07de\u07dc\3\2\2\2\u07df\u07d7\3\2"+
		"\2\2\u07df\u07e0\3\2\2\2\u07e0\u07e1\3\2\2\2\u07e1\u07e2\7_\2\2\u07e2"+
		"\u07fd\3\2\2\2\u07e3\u07e4\7\u0089\2\2\u07e4\u07e5\5\u0140\u00a1\2\u07e5"+
		"\u07ee\7Z\2\2\u07e6\u07eb\5\u0108\u0085\2\u07e7\u07e8\7a\2\2\u07e8\u07ea"+
		"\5\u0108\u0085\2\u07e9\u07e7\3\2\2\2\u07ea\u07ed\3\2\2\2\u07eb\u07e9\3"+
		"\2\2\2\u07eb\u07ec\3\2\2\2\u07ec\u07ef\3\2\2\2\u07ed\u07eb\3\2\2\2\u07ee"+
		"\u07e6\3\2\2\2\u07ee\u07ef\3\2\2\2\u07ef\u07f0\3\2\2\2\u07f0\u07f1\7["+
		"\2\2\u07f1\u07fd\3\2\2\2\u07f2\u07f3\7\u0089\2\2\u07f3\u07f4\7Z\2\2\u07f4"+
		"\u07f5\5\u0108\u0085\2\u07f5\u07f6\7[\2\2\u07f6\u07fd\3\2\2\2\u07f7\u07f8"+
		"\7\u0089\2\2\u07f8\u07fa\5\u0140\u00a1\2\u07f9\u07fb\7\u0081\2\2\u07fa"+
		"\u07f9\3\2\2\2\u07fa\u07fb\3\2\2\2\u07fb\u07fd\3\2\2\2\u07fc\u07d4\3\2"+
		"\2\2\u07fc\u07e3\3\2\2\2\u07fc\u07f2\3\2\2\2\u07fc\u07f7\3\2\2\2\u07fd"+
		"\u0107\3\2\2\2\u07fe\u07ff\b\u0085\1\2\u07ff\u0800\5\u0140\u00a1\2\u0800"+
		"\u0809\7Z\2\2\u0801\u0806\5\u0108\u0085\2\u0802\u0803\7a\2\2\u0803\u0805"+
		"\5\u0108\u0085\2\u0804\u0802\3\2\2\2\u0805\u0808\3\2\2\2\u0806\u0804\3"+
		"\2\2\2\u0806\u0807\3\2\2\2\u0807\u080a\3\2\2\2\u0808\u0806\3\2\2\2\u0809"+
		"\u0801\3\2\2\2\u0809\u080a\3\2\2\2\u080a\u080b\3\2\2\2\u080b\u080c\7["+
		"\2\2\u080c\u0835\3\2\2\2\u080d\u080e\7Z\2\2\u080e\u080f\5\u0108\u0085"+
		"\2\u080f\u0810\7[\2\2\u0810\u0835\3\2\2\2\u0811\u0813\5\u0140\u00a1\2"+
		"\u0812\u0814\7\u0081\2\2\u0813\u0812\3\2\2\2\u0813\u0814\3\2\2\2\u0814"+
		"\u0835\3\2\2\2\u0815\u0835\5\u0110\u0089\2\u0816\u0835\7\u0082\2\2\u0817"+
		"\u0835\5\u00d8m\2\u0818\u0819\7!\2\2\u0819\u0835\5\u00b8]\2\u081a\u081b"+
		"\7Z\2\2\u081b\u081c\5\u00ccg\2\u081c\u081d\7[\2\2\u081d\u081e\5\u0108"+
		"\u0085\27\u081e\u0835\3\2\2\2\u081f\u0820\t\5\2\2\u0820\u0835\5\u0108"+
		"\u0085\25\u0821\u0822\t\6\2\2\u0822\u0835\5\u0108\u0085\24\u0823\u0835"+
		"\5\u00aeX\2\u0824\u0825\5\u00ccg\2\u0825\u082b\7\u008c\2\2\u0826\u0828"+
		"\5\u00d0i\2\u0827\u0826\3\2\2\2\u0827\u0828\3\2\2\2\u0828\u0829\3\2\2"+
		"\2\u0829\u082c\5\u00d8m\2\u082a\u082c\7!\2\2\u082b\u0827\3\2\2\2\u082b"+
		"\u082a\3\2\2\2\u082c\u0835\3\2\2\2\u082d\u082e\5\u00b6\\\2\u082e\u0830"+
		"\7\u008c\2\2\u082f\u0831\5\u00d0i\2\u0830\u082f\3\2\2\2\u0830\u0831\3"+
		"\2\2\2\u0831\u0832\3\2\2\2\u0832\u0833\7!\2\2\u0833\u0835\3\2\2\2\u0834"+
		"\u07fe\3\2\2\2\u0834\u080d\3\2\2\2\u0834\u0811\3\2\2\2\u0834\u0815\3\2"+
		"\2\2\u0834\u0816\3\2\2\2\u0834\u0817\3\2\2\2\u0834\u0818\3\2\2\2\u0834"+
		"\u081a\3\2\2\2\u0834\u081f\3\2\2\2\u0834\u0821\3\2\2\2\u0834\u0823\3\2"+
		"\2\2\u0834\u0824\3\2\2\2\u0834\u082d\3\2\2\2\u0835\u0891\3\2\2\2\u0836"+
		"\u0837\f\23\2\2\u0837\u0838\t\7\2\2\u0838\u0890\5\u0108\u0085\24\u0839"+
		"\u083a\f\22\2\2\u083a\u083b\t\b\2\2\u083b\u0890\5\u0108\u0085\23\u083c"+
		"\u0843\f\21\2\2\u083d\u0844\7\u0080\2\2\u083e\u083f\7d\2\2\u083f\u0840"+
		"\7d\2\2\u0840\u0844\7d\2\2\u0841\u0842\7d\2\2\u0842\u0844\7d\2\2\u0843"+
		"\u083d\3\2\2\2\u0843\u083e\3\2\2\2\u0843\u0841\3\2\2\2\u0844\u0845\3\2"+
		"\2\2\u0845\u0890\5\u0108\u0085\22\u0846\u0847\f\20\2\2\u0847\u0848\t\t"+
		"\2\2\u0848\u0890\5\u0108\u0085\21\u0849\u084a\f\16\2\2\u084a\u084b\t\n"+
		"\2\2\u084b\u0890\5\u0108\u0085\17\u084c\u084d\f\r\2\2\u084d\u084e\7r\2"+
		"\2\u084e\u0890\5\u0108\u0085\16\u084f\u0850\f\f\2\2\u0850\u0851\7s\2\2"+
		"\u0851\u0890\5\u0108\u0085\r\u0852\u0853\f\13\2\2\u0853\u0854\7\u0084"+
		"\2\2\u0854\u0890\5\u0108\u0085\f\u0855\u0856\f\n\2\2\u0856\u0857\7l\2"+
		"\2\u0857\u0890\5\u0108\u0085\13\u0858\u0859\f\t\2\2\u0859\u085a\7m\2\2"+
		"\u085a\u0890\5\u0108\u0085\n\u085b\u085c\f\b\2\2\u085c\u085d\7\u0085\2"+
		"\2\u085d\u085e\5\u0108\u0085\2\u085e\u085f\7g\2\2\u085f\u0860\5\u0108"+
		"\u0085\t\u0860\u0890\3\2\2\2\u0861\u0862\f\7\2\2\u0862\u0863\t\13\2\2"+
		"\u0863\u0890\5\u0108\u0085\7\u0864\u0865\f\33\2\2\u0865\u086e\7Z\2\2\u0866"+
		"\u086b\5\u0108\u0085\2\u0867\u0868\7a\2\2\u0868\u086a\5\u0108\u0085\2"+
		"\u0869\u0867\3\2\2\2\u086a\u086d\3\2\2\2\u086b\u0869\3\2\2\2\u086b\u086c"+
		"\3\2\2\2\u086c\u086f\3\2\2\2\u086d\u086b\3\2\2\2\u086e\u0866\3\2\2\2\u086e"+
		"\u086f\3\2\2\2\u086f\u0870\3\2\2\2\u0870\u0890\7[\2\2\u0871\u0872\f\32"+
		"\2\2\u0872\u087d\7b\2\2\u0873\u087e\5\u00d8m\2\u0874\u087e\7-\2\2\u0875"+
		"\u0877\7!\2\2\u0876\u0878\5\u00c8e\2\u0877\u0876\3\2\2\2\u0877\u0878\3"+
		"\2\2\2\u0878\u0879\3\2\2\2\u0879\u087e\5\u00bc_\2\u087a\u087b\7*\2\2\u087b"+
		"\u087e\5\u00d2j\2\u087c\u087e\5\u00c2b\2\u087d\u0873\3\2\2\2\u087d\u0874"+
		"\3\2\2\2\u087d\u0875\3\2\2\2\u087d\u087a\3\2\2\2\u087d\u087c\3\2\2\2\u087e"+
		"\u0890\3\2\2\2\u087f\u0880\f\31\2\2\u0880\u0881\7^\2\2\u0881\u0882\5\u0108"+
		"\u0085\2\u0882\u0883\7_\2\2\u0883\u0890\3\2\2\2\u0884\u0885\f\26\2\2\u0885"+
		"\u0890\t\f\2\2\u0886\u0887\f\17\2\2\u0887\u0888\7\34\2\2\u0888\u0890\5"+
		"\u00ccg\2\u0889\u088a\f\5\2\2\u088a\u088c\7\u008c\2\2\u088b\u088d\5\u00d0"+
		"i\2\u088c\u088b\3\2\2\2\u088c\u088d\3\2\2\2\u088d\u088e\3\2\2\2\u088e"+
		"\u0890\5\u00d8m\2\u088f\u0836\3\2\2\2\u088f\u0839\3\2\2\2\u088f\u083c"+
		"\3\2\2\2\u088f\u0846\3\2\2\2\u088f\u0849\3\2\2\2\u088f\u084c\3\2\2\2\u088f"+
		"\u084f\3\2\2\2\u088f\u0852\3\2\2\2\u088f\u0855\3\2\2\2\u088f\u0858\3\2"+
		"\2\2\u088f\u085b\3\2\2\2\u088f\u0861\3\2\2\2\u088f\u0864\3\2\2\2\u088f"+
		"\u0871\3\2\2\2\u088f\u087f\3\2\2\2\u088f\u0884\3\2\2\2\u088f\u0886\3\2"+
		"\2\2\u088f\u0889\3\2\2\2\u0890\u0893\3\2\2\2\u0891\u088f\3\2\2\2\u0891"+
		"\u0892\3\2\2\2\u0892\u0109\3\2\2\2\u0893\u0891\3\2\2\2\u0894\u0895\5\u0140"+
		"\u00a1\2\u0895\u0896\7\u008d\2\2\u0896\u0897\5\u010a\u0086\2\u0897\u08b1"+
		"\3\2\2\2\u0898\u0899\7\u0083\2\2\u0899\u08b1\5\u010a\u0086\2\u089a\u089b"+
		"\5\u010c\u0087\2\u089b\u089c\5\u0112\u008a\2\u089c\u08b1\3\2\2\2\u089d"+
		"\u089e\5\u010c\u0087\2\u089e\u089f\5\u0114\u008b\2\u089f\u08b1\3\2\2\2"+
		"\u08a0\u08a2\5\u0140\u00a1\2\u08a1\u08a3\7\u0081\2\2\u08a2\u08a1\3\2\2"+
		"\2\u08a2\u08a3\3\2\2\2\u08a3\u08b1\3\2\2\2\u08a4\u08a6\7\u0082\2\2\u08a5"+
		"\u08a7\7\u0081\2\2\u08a6\u08a5\3\2\2\2\u08a6\u08a7\3\2\2\2\u08a7\u08b1"+
		"\3\2\2\2\u08a8\u08ad\5\u0110\u0089\2\u08a9\u08aa\7\u0084\2\2\u08aa\u08ac"+
		"\5\u0110\u0089\2\u08ab\u08a9\3\2\2\2\u08ac\u08af\3\2\2\2\u08ad\u08ab\3"+
		"\2\2\2\u08ad\u08ae\3\2\2\2\u08ae\u08b1\3\2\2\2\u08af\u08ad\3\2\2\2\u08b0"+
		"\u0894\3\2\2\2\u08b0\u0898\3\2\2\2\u08b0\u089a\3\2\2\2\u08b0\u089d\3\2"+
		"\2\2\u08b0\u08a0\3\2\2\2\u08b0\u08a4\3\2\2\2\u08b0\u08a8\3\2\2\2\u08b1"+
		"\u010b\3\2\2\2\u08b2\u08be\5\u010e\u0088\2\u08b3\u08b4\7Z\2\2\u08b4\u08b7"+
		"\5\u010e\u0088\2\u08b5\u08b6\7\u0084\2\2\u08b6\u08b8\5\u010e\u0088\2\u08b7"+
		"\u08b5\3\2\2\2\u08b8\u08b9\3\2\2\2\u08b9\u08b7\3\2\2\2\u08b9\u08ba\3\2"+
		"\2\2\u08ba\u08bb\3\2\2\2\u08bb\u08bc\7[\2\2\u08bc\u08be\3\2\2\2\u08bd"+
		"\u08b2\3\2\2\2\u08bd\u08b3\3\2\2\2\u08be\u010d\3\2\2\2\u08bf\u08c1\5\u0140"+
		"\u00a1\2\u08c0\u08c2\7\u0085\2\2\u08c1\u08c0\3\2\2\2\u08c1\u08c2\3\2\2"+
		"\2\u08c2\u08c9\3\2\2\2\u08c3\u08c5\5\u0140\u00a1\2\u08c4\u08c6\7\u0086"+
		"\2\2\u08c5\u08c4\3\2\2\2\u08c5\u08c6\3\2\2\2\u08c6\u08c9\3\2\2\2\u08c7"+
		"\u08c9\5\u0110\u0089\2\u08c8\u08bf\3\2\2\2\u08c8\u08c3\3\2\2\2\u08c8\u08c7"+
		"\3\2\2\2\u08c9\u010f\3\2\2\2\u08ca\u08cc\7q\2\2\u08cb\u08ca\3\2\2\2\u08cb"+
		"\u08cc\3\2\2\2\u08cc\u08cd\3\2\2\2\u08cd\u08d6\7O\2\2\u08ce\u08d0\7q\2"+
		"\2\u08cf\u08ce\3\2\2\2\u08cf\u08d0\3\2\2\2\u08d0\u08d1\3\2\2\2\u08d1\u08d6"+
		"\7S\2\2\u08d2\u08d6\7V\2\2\u08d3\u08d6\7W\2\2\u08d4\u08d6\7X\2\2\u08d5"+
		"\u08cb\3\2\2\2\u08d5\u08cf\3\2\2\2\u08d5\u08d2\3\2\2\2\u08d5\u08d3\3\2"+
		"\2\2\u08d5\u08d4\3\2\2\2\u08d6\u0111\3\2\2\2\u08d7\u08e0\7Z\2\2\u08d8"+
		"\u08dd\5\u010a\u0086\2\u08d9\u08da\7a\2\2\u08da\u08dc\5\u010a\u0086\2"+
		"\u08db\u08d9\3\2\2\2\u08dc\u08df\3\2\2\2\u08dd\u08db\3\2\2\2\u08dd\u08de"+
		"\3\2\2\2\u08de\u08e1\3\2\2\2\u08df\u08dd\3\2\2\2\u08e0\u08d8\3\2\2\2\u08e0"+
		"\u08e1\3\2\2\2\u08e1\u08e2\3\2\2\2\u08e2\u08e3\7[\2\2\u08e3\u0113\3\2"+
		"\2\2\u08e4\u08f2\7^\2\2\u08e5\u08e6\5\u0140\u00a1\2\u08e6\u08e7\7c\2\2"+
		"\u08e7\u08ef\5\u010a\u0086\2\u08e8\u08e9\7a\2\2\u08e9\u08ea\5\u0140\u00a1"+
		"\2\u08ea\u08eb\7c\2\2\u08eb\u08ec\5\u010a\u0086\2\u08ec\u08ee\3\2\2\2"+
		"\u08ed\u08e8\3\2\2\2\u08ee\u08f1\3\2\2\2\u08ef\u08ed\3\2\2\2\u08ef\u08f0"+
		"\3\2\2\2\u08f0\u08f3\3\2\2\2\u08f1\u08ef\3\2\2\2\u08f2\u08e5\3\2\2\2\u08f2"+
		"\u08f3\3\2\2\2\u08f3\u08f4\3\2\2\2\u08f4\u08f5\7_\2\2\u08f5\u0115\3\2"+
		"\2\2\u08f6\u08f7\7<\2\2\u08f7\u08fa\5\u0140\u00a1\2\u08f8\u08f9\7\23\2"+
		"\2\u08f9\u08fb\5\u0140\u00a1\2\u08fa\u08f8\3\2\2\2\u08fa\u08fb\3\2\2\2"+
		"\u08fb\u08fc\3\2\2\2\u08fc\u08fd\7\\\2\2\u08fd\u08ff\5\u0120\u0091\2\u08fe"+
		"\u0900\5\u0124\u0093\2\u08ff\u08fe\3\2\2\2\u08ff\u0900\3\2\2\2\u0900\u0902"+
		"\3\2\2\2\u0901\u0903\5\u0122\u0092\2\u0902\u0901\3\2\2\2\u0902\u0903\3"+
		"\2\2\2\u0903\u0904\3\2\2\2\u0904\u0905\7]\2\2\u0905\u0117\3\2\2\2\u0906"+
		"\u0907\79\2\2";
	private static final String _serializedATNSegment1 =
		"\u0907\u0908\5\u0140\u00a1\2\u0908\u0909\5\u0140\u00a1\2\u0909\u090b\7"+
		"Z\2\2\u090a\u090c\5\u00f8}\2\u090b\u090a\3\2\2\2\u090b\u090c\3\2\2\2\u090c"+
		"\u090d\3\2\2\2\u090d\u090e\7[\2\2\u090e\u0915\7\\\2\2\u090f\u0914\5\u0126"+
		"\u0094\2\u0910\u0914\5\u0128\u0095\2\u0911\u0914\5\u0132\u009a\2\u0912"+
		"\u0914\5\u013e\u00a0\2\u0913\u090f\3\2\2\2\u0913\u0910\3\2\2\2\u0913\u0911"+
		"\3\2\2\2\u0913\u0912\3\2\2\2\u0914\u0917\3\2\2\2\u0915\u0913\3\2\2\2\u0915"+
		"\u0916\3\2\2\2\u0916\u0918\3\2\2\2\u0917\u0915\3\2\2\2\u0918\u0919\7]"+
		"\2\2\u0919\u0119\3\2\2\2\u091a\u091b\7;\2\2\u091b\u091c\5\u0140\u00a1"+
		"\2\u091c\u091d\5\u0140\u00a1\2\u091d\u091e\7Z\2\2\u091e\u091f\5\u0140"+
		"\u00a1\2\u091f\u0920\7\u0081\2\2\u0920\u0921\7[\2\2\u0921\u092a\7\\\2"+
		"\2\u0922\u0929\5\u0126\u0094\2\u0923\u0929\5\u012a\u0096\2\u0924\u0929"+
		"\5\u0130\u0099\2\u0925\u0929\5\u0134\u009b\2\u0926\u0929\5\u0136\u009c"+
		"\2\u0927\u0929\5\u013a\u009e\2\u0928\u0922\3\2\2\2\u0928\u0923\3\2\2\2"+
		"\u0928\u0924\3\2\2\2\u0928\u0925\3\2\2\2\u0928\u0926\3\2\2\2\u0928\u0927"+
		"\3\2\2\2\u0929\u092c\3\2\2\2\u092a\u0928\3\2\2\2\u092a\u092b\3\2\2\2\u092b"+
		"\u092d\3\2\2\2\u092c\u092a\3\2\2\2\u092d\u092e\7]\2\2\u092e\u011b\3\2"+
		"\2\2\u092f\u0930\7:\2\2\u0930\u0931\5\u0140\u00a1\2\u0931\u0932\5\u0140"+
		"\u00a1\2\u0932\u0933\7Z\2\2\u0933\u0934\5\u0140\u00a1\2\u0934\u0935\7"+
		"\u0081\2\2\u0935\u0936\7[\2\2\u0936\u093e\7\\\2\2\u0937\u093d\5\u0126"+
		"\u0094\2\u0938\u093d\5\u012c\u0097\2\u0939\u093d\5\u012e\u0098\2\u093a"+
		"\u093d\5\u0138\u009d\2\u093b\u093d\5\u013c\u009f\2\u093c\u0937\3\2\2\2"+
		"\u093c\u0938\3\2\2\2\u093c\u0939\3\2\2\2\u093c\u093a\3\2\2\2\u093c\u093b"+
		"\3\2\2\2\u093d\u0940\3\2\2\2\u093e\u093c\3\2\2\2\u093e\u093f\3\2\2\2\u093f"+
		"\u0941\3\2\2\2\u0940\u093e\3\2\2\2\u0941\u0942\7]\2\2\u0942\u011d\3\2"+
		"\2\2\u0943\u0944\7\\\2\2\u0944\u0945\5\u00aaV\2\u0945\u0946\7]\2\2\u0946"+
		"\u011f\3\2\2\2\u0947\u0948\7L\2\2\u0948\u0949\7\\\2\2\u0949\u094a\5\u00cc"+
		"g\2\u094a\u094b\7]\2\2\u094b\u0121\3\2\2\2\u094c\u094d\7M\2\2\u094d\u094e"+
		"\7Z\2\2\u094e\u094f\5\u0140\u00a1\2\u094f\u0950\7a\2\2\u0950\u0951\5\u0140"+
		"\u00a1\2\u0951\u0952\7[\2\2\u0952\u0953\5\u011e\u0090\2\u0953\u0123\3"+
		"\2\2\2\u0954\u0955\7@\2\2\u0955\u0956\7Z\2\2\u0956\u0957\5\u0140\u00a1"+
		"\2\u0957\u0958\7[\2\2\u0958\u0959\5\u011e\u0090\2\u0959\u0125\3\2\2\2"+
		"\u095a\u095b\7?\2\2\u095b\u095c\7Z\2\2\u095c\u095d\5\u0140\u00a1\2\u095d"+
		"\u095e\7[\2\2\u095e\u095f\5\u011e\u0090\2\u095f\u0127\3\2\2\2\u0960\u0961"+
		"\7A\2\2\u0961\u096a\7Z\2\2\u0962\u0967\5\u0140\u00a1\2\u0963\u0964\7a"+
		"\2\2\u0964\u0966\5\u0140\u00a1\2\u0965\u0963\3\2\2\2\u0966\u0969\3\2\2"+
		"\2\u0967\u0965\3\2\2\2\u0967\u0968\3\2\2\2\u0968\u096b\3\2\2\2\u0969\u0967"+
		"\3\2\2\2\u096a\u0962\3\2\2\2\u096a\u096b\3\2\2\2\u096b\u096c\3\2\2\2\u096c"+
		"\u096d\7[\2\2\u096d\u096e\5\u011e\u0090\2\u096e\u0129\3\2\2\2\u096f\u0970"+
		"\7B\2\2\u0970\u0971\7Z\2\2\u0971\u0972\7[\2\2\u0972\u0973\5\u011e\u0090"+
		"\2\u0973\u012b\3\2\2\2\u0974\u0975\7B\2\2\u0975\u0976\7Z\2\2\u0976\u0977"+
		"\5\u0140\u00a1\2\u0977\u0978\7[\2\2\u0978\u0979\5\u011e\u0090\2\u0979"+
		"\u012d\3\2\2\2\u097a\u097b\7C\2\2\u097b\u097c\7Z\2\2\u097c\u097d\5\u0140"+
		"\u00a1\2\u097d\u097e\7a\2\2\u097e\u097f\5\u0140\u00a1\2\u097f\u0980\7"+
		"[\2\2\u0980\u0981\5\u011e\u0090\2\u0981\u012f\3\2\2\2\u0982\u0983\7D\2"+
		"\2\u0983\u0984\7Z\2\2\u0984\u0985\5\u0140\u00a1\2\u0985\u0986\7a\2\2\u0986"+
		"\u0987\5\u0140\u00a1\2\u0987\u0988\7[\2\2\u0988\u0989\5\u011e\u0090\2"+
		"\u0989\u0131\3\2\2\2\u098a\u098b\7E\2\2\u098b\u098c\7Z\2\2\u098c\u098d"+
		"\5\u0140\u00a1\2\u098d\u098e\7a\2\2\u098e\u098f\5\u0140\u00a1\2\u098f"+
		"\u0990\7[\2\2\u0990\u0991\5\u011e\u0090\2\u0991\u0133\3\2\2\2\u0992\u0993"+
		"\7H\2\2\u0993\u0994\7Z\2\2\u0994\u0995\5\u0140\u00a1\2\u0995\u0996\7["+
		"\2\2\u0996\u0997\5\u011e\u0090\2\u0997\u0135\3\2\2\2\u0998\u0999\7I\2"+
		"\2\u0999\u099a\7Z\2\2\u099a\u099b\5\u0140\u00a1\2\u099b\u099c\7[\2\2\u099c"+
		"\u099d\5\u011e\u0090\2\u099d\u0137\3\2\2\2\u099e\u099f\7G\2\2\u099f\u09a0"+
		"\7Z\2\2\u09a0\u09a1\5\u0140\u00a1\2\u09a1\u09a2\7a\2\2\u09a2\u09a3\5\u0140"+
		"\u00a1\2\u09a3\u09a4\7[\2\2\u09a4\u09a5\5\u011e\u0090\2\u09a5\u0139\3"+
		"\2\2\2\u09a6\u09a7\7K\2\2\u09a7\u09a8\7Z\2\2\u09a8\u09a9\5\u0140\u00a1"+
		"\2\u09a9\u09aa\7[\2\2\u09aa\u09ab\5\u011e\u0090\2\u09ab\u013b\3\2\2\2"+
		"\u09ac\u09ad\7J\2\2\u09ad\u09ae\7Z\2\2\u09ae\u09af\5\u0140\u00a1\2\u09af"+
		"\u09b0\7[\2\2\u09b0\u09b1\5\u011e\u0090\2\u09b1\u013d\3\2\2\2\u09b2\u09b3"+
		"\7F\2\2\u09b3\u09b4\7Z\2\2\u09b4\u09b5\5\u0140\u00a1\2\u09b5\u09b6\7["+
		"\2\2\u09b6\u09b7\5\u011e\u0090\2\u09b7\u013f\3\2\2\2\u09b8\u09b9\t\22"+
		"\2\2\u09b9\u0141\3\2\2\2\u09ba\u09bb\7\u0099\2\2\u09bb\u09bd\5\u0144\u00a3"+
		"\2\u09bc\u09be\5\u0146\u00a4\2\u09bd\u09bc\3\2\2\2\u09bd\u09be\3\2\2\2"+
		"\u09be\u09bf\3\2\2\2\u09bf\u09c0\5\u0148\u00a5\2\u09c0\u0143\3\2\2\2\u09c1"+
		"\u09c2\5\u016c\u00b7\2\u09c2\u09c3\7\u00a7\2\2\u09c3\u09c5\3\2\2\2\u09c4"+
		"\u09c1\3\2\2\2\u09c5\u09c8\3\2\2\2\u09c6\u09c4\3\2\2\2\u09c6\u09c7\3\2"+
		"\2\2\u09c7\u09c9\3\2\2\2\u09c8\u09c6\3\2\2\2\u09c9\u09ca\5\u016c\u00b7"+
		"\2\u09ca\u0145\3\2\2\2\u09cb\u09cf\7\u009a\2\2\u09cc\u09ce\5\u016c\u00b7"+
		"\2\u09cd\u09cc\3\2\2\2\u09ce\u09d1\3\2\2\2\u09cf\u09cd\3\2\2\2\u09cf\u09d0"+
		"\3\2\2\2\u09d0\u0147\3\2\2\2\u09d1\u09cf\3\2\2\2\u09d2\u09d4\7\u009b\2"+
		"\2\u09d3\u09d2\3\2\2\2\u09d3\u09d4\3\2\2\2\u09d4\u09d5\3\2\2\2\u09d5\u09d6"+
		"\5\u014a\u00a6\2\u09d6\u0149\3\2\2\2\u09d7\u09d9\5\u014c\u00a7\2\u09d8"+
		"\u09d7\3\2\2\2\u09d9\u09dc\3\2\2\2\u09da\u09d8\3\2\2\2\u09da\u09db\3\2"+
		"\2\2\u09db\u014b\3\2\2\2\u09dc\u09da\3\2\2\2\u09dd\u09de\7\u009c\2\2\u09de"+
		"\u09df\7\u009d\2\2\u09df\u09e5\3\2\2\2\u09e0\u09e4\5\u0164\u00b3\2\u09e1"+
		"\u09e4\5\u0150\u00a9\2\u09e2\u09e4\5\u014e\u00a8\2\u09e3\u09e0\3\2\2\2"+
		"\u09e3\u09e1\3\2\2\2\u09e3\u09e2\3\2\2\2\u09e4\u09e7\3\2\2\2\u09e5\u09e3"+
		"\3\2\2\2\u09e5\u09e6\3\2\2\2\u09e6\u014d\3\2\2\2\u09e7\u09e5\3\2\2\2\u09e8"+
		"\u09e9\7\u00a0\2\2\u09e9\u09ea\5\u016c\u00b7\2\u09ea\u014f\3\2\2\2\u09eb"+
		"\u09ec\5\u016c\u00b7\2\u09ec\u09ed\7\u00ab\2\2\u09ed\u09ee\5\u0152\u00aa"+
		"\2\u09ee\u09f6\3\2\2\2\u09ef\u09f0\5\u016c\u00b7\2\u09f0\u09f1\7\u00a4"+
		"\2\2\u09f1\u09f2\5\u016c\u00b7\2\u09f2\u09f3\7\u00ab\2\2\u09f3\u09f4\5"+
		"\u0154\u00ab\2\u09f4\u09f6\3\2\2\2\u09f5\u09eb\3\2\2\2\u09f5\u09ef\3\2"+
		"\2\2\u09f6\u0151\3\2\2\2\u09f7\u09f8\7\u00b0\2\2\u09f8\u0a01\7\u00ac\2"+
		"\2\u09f9\u09fa\7\u00ac\2\2\u09fa\u0a01\7\u00b0\2\2\u09fb\u0a01\7\u00b0"+
		"\2\2\u09fc\u09fe\b\u00aa\1\2\u09fd\u09ff\7\u00ac\2\2\u09fe\u09fd\3\2\2"+
		"\2\u09fe\u09ff\3\2\2\2\u09ff\u0a01\3\2\2\2\u0a00\u09f7\3\2\2\2\u0a00\u09f9"+
		"\3\2\2\2\u0a00\u09fb\3\2\2\2\u0a00\u09fc\3\2\2\2\u0a01\u0a02\3\2\2\2\u0a02"+
		"\u0a0e\5\u0156\u00ac\2\u0a03\u0a04\7\u00b0\2\2\u0a04\u0a0a\7\u00ac\2\2"+
		"\u0a05\u0a06\7\u00ac\2\2\u0a06\u0a0a\7\u00b0\2\2\u0a07\u0a08\b\u00aa\1"+
		"\2\u0a08\u0a0a\7\u00ac\2\2\u0a09\u0a03\3\2\2\2\u0a09\u0a05\3\2\2\2\u0a09"+
		"\u0a07\3\2\2\2\u0a0a\u0a0b\3\2\2\2\u0a0b\u0a0d\5\u0156\u00ac\2\u0a0c\u0a09"+
		"\3\2\2\2\u0a0d\u0a10\3\2\2\2\u0a0e\u0a0c\3\2\2\2\u0a0e\u0a0f\3\2\2\2\u0a0f"+
		"\u0a12\3\2\2\2\u0a10\u0a0e\3\2\2\2\u0a11\u0a13\7\u00ad\2\2\u0a12\u0a11"+
		"\3\2\2\2\u0a12\u0a13\3\2\2\2\u0a13\u0153\3\2\2\2\u0a14\u0a16\7\u00ac\2"+
		"\2\u0a15\u0a14\3\2\2\2\u0a15\u0a16\3\2\2\2\u0a16\u0a17\3\2\2\2\u0a17\u0a1c"+
		"\5\u0158\u00ad\2\u0a18\u0a19\7\u00ac\2\2\u0a19\u0a1b\5\u0158\u00ad\2\u0a1a"+
		"\u0a18\3\2\2\2\u0a1b\u0a1e\3\2\2\2\u0a1c\u0a1a\3\2\2\2\u0a1c\u0a1d\3\2"+
		"\2\2\u0a1d\u0a20\3\2\2\2\u0a1e\u0a1c\3\2\2\2\u0a1f\u0a21\7\u00ad\2\2\u0a20"+
		"\u0a1f\3\2\2\2\u0a20\u0a21\3\2\2\2\u0a21\u0155\3\2\2\2\u0a22\u0a23\5\u016c"+
		"\u00b7\2\u0a23\u0a24\5\u015a\u00ae\2\u0a24\u0157\3\2\2\2\u0a25\u0a26\5"+
		"\u016c\u00b7\2\u0a26\u0a27\5\u015c\u00af\2\u0a27\u0159\3\2\2\2\u0a28\u0a31"+
		"\7\u00a8\2\2\u0a29\u0a2e\5\u015e\u00b0\2\u0a2a\u0a2b\7\u00a6\2\2\u0a2b"+
		"\u0a2d\5\u015e\u00b0\2\u0a2c\u0a2a\3\2\2\2\u0a2d\u0a30\3\2\2\2\u0a2e\u0a2c"+
		"\3\2\2\2\u0a2e\u0a2f\3\2\2\2\u0a2f\u0a32\3\2\2\2\u0a30\u0a2e\3\2\2\2\u0a31"+
		"\u0a29\3\2\2\2\u0a31\u0a32\3\2\2\2\u0a32\u0a33\3\2\2\2\u0a33\u0a34\7\u00a9"+
		"\2\2\u0a34\u015b\3\2\2\2\u0a35\u0a3e\7\u00a8\2\2\u0a36\u0a3b\5\u0160\u00b1"+
		"\2\u0a37\u0a38\7\u00a6\2\2\u0a38\u0a3a\5\u0160\u00b1\2\u0a39\u0a37\3\2"+
		"\2\2\u0a3a\u0a3d\3\2\2\2\u0a3b\u0a39\3\2\2\2\u0a3b\u0a3c\3\2\2\2\u0a3c"+
		"\u0a3f\3\2\2\2\u0a3d\u0a3b\3\2\2\2\u0a3e\u0a36\3\2\2\2\u0a3e\u0a3f\3\2"+
		"\2\2\u0a3f\u0a40\3\2\2\2\u0a40\u0a41\7\u00a9\2\2\u0a41\u015d\3\2\2\2\u0a42"+
		"\u0a43\5\u016c\u00b7\2\u0a43\u0a44\7\u00aa\2\2\u0a44\u0a55\3\2\2\2\u0a45"+
		"\u0a46\7\u00ae\2\2\u0a46\u0a47\5\u016c\u00b7\2\u0a47\u0a48\7\u00af\2\2"+
		"\u0a48\u0a49\7\u00aa\2\2\u0a49\u0a55\3\2\2\2\u0a4a\u0a4b\5\u016c\u00b7"+
		"\2\u0a4b\u0a4c\7\u00a5\2\2\u0a4c\u0a4d\5\u016c\u00b7\2\u0a4d\u0a55\3\2"+
		"\2\2\u0a4e\u0a4f\5\u016c\u00b7\2\u0a4f\u0a50\7\u00a5\2\2\u0a50\u0a51\7"+
		"\u00ae\2\2\u0a51\u0a52\5\u016c\u00b7\2\u0a52\u0a53\7\u00af\2\2\u0a53\u0a55"+
		"\3\2\2\2\u0a54\u0a42\3\2\2\2\u0a54\u0a45\3\2\2\2\u0a54\u0a4a\3\2\2\2\u0a54"+
		"\u0a4e\3\2\2\2\u0a55\u015f\3\2\2\2\u0a56\u0a57\5\u016c\u00b7\2\u0a57\u0a58"+
		"\7\u00aa\2\2\u0a58\u0a6d\3\2\2\2\u0a59\u0a5a\7\u00a1\2\2\u0a5a\u0a5b\5"+
		"\u016c\u00b7\2\u0a5b\u0a5c\7\u00a5\2\2\u0a5c\u0a5d\5\u016c\u00b7\2\u0a5d"+
		"\u0a6d\3\2\2\2\u0a5e\u0a5f\7\u00a2\2\2\u0a5f\u0a60\5\u016c\u00b7\2\u0a60"+
		"\u0a61\7\u00a5\2\2\u0a61\u0a62\5\u016c\u00b7\2\u0a62\u0a6d\3\2\2\2\u0a63"+
		"\u0a64\7\u00a3\2\2\u0a64\u0a65\5\u016c\u00b7\2\u0a65\u0a66\7\u00a5\2\2"+
		"\u0a66\u0a67\5\u016c\u00b7\2\u0a67\u0a6d\3\2\2\2\u0a68\u0a69\5\u016c\u00b7"+
		"\2\u0a69\u0a6a\7\u00a5\2\2\u0a6a\u0a6b\5\u016c\u00b7\2\u0a6b\u0a6d\3\2"+
		"\2\2\u0a6c\u0a56\3\2\2\2\u0a6c\u0a59\3\2\2\2\u0a6c\u0a5e\3\2\2\2\u0a6c"+
		"\u0a63\3\2\2\2\u0a6c\u0a68\3\2\2\2\u0a6d\u0161\3\2\2\2\u0a6e\u0a77\7\u00a8"+
		"\2\2\u0a6f\u0a74\5\u016c\u00b7\2\u0a70\u0a71\7\u00a6\2\2\u0a71\u0a73\5"+
		"\u016c\u00b7\2\u0a72\u0a70\3\2\2\2\u0a73\u0a76\3\2\2\2\u0a74\u0a72\3\2"+
		"\2\2\u0a74\u0a75\3\2\2\2\u0a75\u0a78\3\2\2\2\u0a76\u0a74\3\2\2\2\u0a77"+
		"\u0a6f\3\2\2\2\u0a77\u0a78\3\2\2\2\u0a78\u0a79\3\2\2\2\u0a79\u0a7b\7\u00a9"+
		"\2\2\u0a7a\u0a6e\3\2\2\2\u0a7a\u0a7b\3\2\2\2\u0a7b\u0163\3\2\2\2\u0a7c"+
		"\u0a7e\5\u016a\u00b6\2\u0a7d\u0a7c\3\2\2\2\u0a7d\u0a7e\3\2\2\2\u0a7e\u0a7f"+
		"\3\2\2\2\u0a7f\u0a80\5\u016c\u00b7\2\u0a80\u0ab6\7\u00a5\2\2\u0a81\u0a84"+
		"\7\u00b1\2\2\u0a82\u0a83\7\u00c7\2\2\u0a83\u0a85\7\u00c8\2\2\u0a84\u0a82"+
		"\3\2\2\2\u0a84\u0a85\3\2\2\2\u0a85\u0a86\3\2\2\2\u0a86\u0a87\7\u00c9\2"+
		"\2\u0a87\u0a88\5\u016e\u00b8\2\u0a88\u0a89\7\u00cd\2\2\u0a89\u0ab7\3\2"+
		"\2\2\u0a8a\u0a8b\7\u00b2\2\2\u0a8b\u0a8c\7\u00c7\2\2\u0a8c\u0a8d\7\u00c5"+
		"\2\2\u0a8d\u0a8e\7\u00c6\2\2\u0a8e\u0a8f\t\23\2\2\u0a8f\u0a90\7\u00c8"+
		"\2\2\u0a90\u0a91\7\u00c9\2\2\u0a91\u0a92\5\u0172\u00ba\2\u0a92\u0a93\7"+
		"\u00cd\2\2\u0a93\u0ab7\3\2\2\2\u0a94\u0a97\t\24\2\2\u0a95\u0a96\7\u00a8"+
		"\2\2\u0a96\u0a98\7\u00a9\2\2\u0a97\u0a95\3\2\2\2\u0a97\u0a98\3\2\2\2\u0a98"+
		"\u0a99\3\2\2\2\u0a99\u0ab7\5\u0166\u00b4\2\u0a9a\u0a9d\t\25\2\2\u0a9b"+
		"\u0a9c\7\u00a8\2\2\u0a9c\u0a9e\7\u00a9\2\2\u0a9d\u0a9b\3\2\2\2\u0a9d\u0a9e"+
		"\3\2\2\2\u0a9e\u0a9f\3\2\2\2\u0a9f\u0ab7\5\u0166\u00b4\2\u0aa0\u0aa1\t"+
		"\26\2\2\u0aa1\u0aa2\5\u0162\u00b2\2\u0aa2\u0aa3\5\u0166\u00b4\2\u0aa3"+
		"\u0ab7\3\2\2\2\u0aa4\u0aa7\7\u00bb\2\2\u0aa5\u0aa6\7\u00a8\2\2\u0aa6\u0aa8"+
		"\7\u00a9\2\2\u0aa7\u0aa5\3\2\2\2\u0aa7\u0aa8\3\2\2\2\u0aa8\u0aa9\3\2\2"+
		"\2\u0aa9\u0ab7\5\u0166\u00b4\2\u0aaa\u0aad\7\u00bc\2\2\u0aab\u0aac\7\u00a8"+
		"\2\2\u0aac\u0aae\7\u00a9\2\2\u0aad\u0aab\3\2\2\2\u0aad\u0aae\3\2\2\2\u0aae"+
		"\u0aaf\3\2\2\2\u0aaf\u0ab7\5\u0166\u00b4\2\u0ab0\u0ab3\t\27\2\2\u0ab1"+
		"\u0ab2\7\u00a8\2\2\u0ab2\u0ab4\7\u00a9\2\2\u0ab3\u0ab1\3\2\2\2\u0ab3\u0ab4"+
		"\3\2\2\2\u0ab4\u0ab5\3\2\2\2\u0ab5\u0ab7\5\u0166\u00b4\2\u0ab6\u0a81\3"+
		"\2\2\2\u0ab6\u0a8a\3\2\2\2\u0ab6\u0a94\3\2\2\2\u0ab6\u0a9a\3\2\2\2\u0ab6"+
		"\u0aa0\3\2\2\2\u0ab6\u0aa4\3\2\2\2\u0ab6\u0aaa\3\2\2\2\u0ab6\u0ab0\3\2"+
		"\2\2\u0ab7\u0165\3\2\2\2\u0ab8\u0abd\7\u0097\2\2\u0ab9\u0abc\5\u00f0y"+
		"\2\u0aba\u0abc\7\u00f4\2\2\u0abb\u0ab9\3\2\2\2\u0abb\u0aba\3\2\2\2\u0abc"+
		"\u0abf\3\2\2\2\u0abd\u0abe\3\2\2\2\u0abd\u0abb\3\2\2\2\u0abe\u0ac0\3\2"+
		"\2\2\u0abf\u0abd\3\2\2\2\u0ac0\u0ac1\7\u00f3\2\2\u0ac1\u0167\3\2\2\2\u0ac2"+
		"\u0ac3\3\2\2\2\u0ac3\u0169\3\2\2\2\u0ac4\u0ac5\t\30\2\2\u0ac5\u016b\3"+
		"\2\2\2\u0ac6\u0ac7\t\31\2\2\u0ac7\u016d\3\2\2\2\u0ac8\u0aca\5\u0170\u00b9"+
		"\2\u0ac9\u0ac8\3\2\2\2\u0aca\u0acd\3\2\2\2\u0acb\u0ac9\3\2\2\2\u0acb\u0acc"+
		"\3\2\2\2\u0acc\u016f\3\2\2\2\u0acd\u0acb\3\2\2\2\u0ace\u0acf\5\u017c\u00bf"+
		"\2\u0acf\u0ad0\7\u00ce\2\2\u0ad0\u0ad3\5\u017e\u00c0\2\u0ad1\u0ad2\7\u00e1"+
		"\2\2\u0ad2\u0ad4\5\u0176\u00bc\2\u0ad3\u0ad1\3\2\2\2\u0ad3\u0ad4\3\2\2"+
		"\2\u0ad4\u0171\3\2\2\2\u0ad5\u0ad7\5\u0174\u00bb\2\u0ad6\u0ad5\3\2\2\2"+
		"\u0ad7\u0ada\3\2\2\2\u0ad8\u0ad6\3\2\2\2\u0ad8\u0ad9\3\2\2\2\u0ad9\u0173"+
		"\3\2\2\2\u0ada\u0ad8\3\2\2\2\u0adb\u0adc\5\u0182\u00c2\2\u0adc\u0add\7"+
		"\u00ce\2\2\u0add\u0ae0\5\u0182\u00c2\2\u0ade\u0adf\7\u00e1\2\2\u0adf\u0ae1"+
		"\5\u0176\u00bc\2\u0ae0\u0ade\3\2\2\2\u0ae0\u0ae1\3\2\2\2\u0ae1\u0175\3"+
		"\2\2\2\u0ae2\u0ae7\5\u0178\u00bd\2\u0ae3\u0ae4\7\u00d8\2\2\u0ae4\u0ae6"+
		"\5\u0178\u00bd\2\u0ae5\u0ae3\3\2\2\2\u0ae6\u0ae9\3\2\2\2\u0ae7\u0ae5\3"+
		"\2\2\2\u0ae7\u0ae8\3\2\2\2\u0ae8\u0177\3\2\2\2\u0ae9\u0ae7\3\2\2\2\u0aea"+
		"\u0aef\5\u017a\u00be\2\u0aeb\u0aec\7\u00d7\2\2\u0aec\u0aee\5\u017a\u00be"+
		"\2\u0aed\u0aeb\3\2\2\2\u0aee\u0af1\3\2\2\2\u0aef\u0aed\3\2\2\2\u0aef\u0af0"+
		"\3\2\2\2\u0af0\u0179\3\2\2\2\u0af1\u0aef\3\2\2\2\u0af2\u0af3\5\u017e\u00c0"+
		"\2\u0af3\u0af4\t\32\2\2\u0af4\u0af5\5\u017e\u00c0\2\u0af5\u0afb\3\2\2"+
		"\2\u0af6\u0af7\7\u00d4\2\2\u0af7\u0af8\5\u0176\u00bc\2\u0af8\u0af9\7\u00d5"+
		"\2\2\u0af9\u0afb\3\2\2\2\u0afa\u0af2\3\2\2\2\u0afa\u0af6\3\2\2\2\u0afb"+
		"\u017b\3\2\2\2\u0afc\u0afd\7\u00e4\2\2\u0afd\u0b06\7\u00d4\2\2\u0afe\u0b03"+
		"\5\u017e\u00c0\2\u0aff\u0b00\7\u00d6\2\2\u0b00\u0b02\5\u017e\u00c0\2\u0b01"+
		"\u0aff\3\2\2\2\u0b02\u0b05\3\2\2\2\u0b03\u0b01\3\2\2\2\u0b03\u0b04\3\2"+
		"\2\2\u0b04\u0b07\3\2\2\2\u0b05\u0b03\3\2\2\2\u0b06\u0afe\3\2\2\2\u0b06"+
		"\u0b07\3\2\2\2\u0b07\u0b08\3\2\2\2\u0b08\u0b1f\7\u00d5\2\2\u0b09\u0b0a"+
		"\7\u00e4\2\2\u0b0a\u0b0b\7\u00d2\2\2\u0b0b\u0b0c\7\u00e4\2\2\u0b0c\u0b15"+
		"\7\u00d4\2\2\u0b0d\u0b12\5\u017e\u00c0\2\u0b0e\u0b0f\7\u00d6\2\2\u0b0f"+
		"\u0b11\5\u017e\u00c0\2\u0b10\u0b0e\3\2\2\2\u0b11\u0b14\3\2\2\2\u0b12\u0b10"+
		"\3\2\2\2\u0b12\u0b13\3\2\2\2\u0b13\u0b16\3\2\2\2\u0b14\u0b12\3\2\2\2\u0b15"+
		"\u0b0d\3\2\2\2\u0b15\u0b16\3\2\2\2\u0b16\u0b17\3\2\2\2\u0b17\u0b1f\7\u00d5"+
		"\2\2\u0b18\u0b1a\7\u00d0\2\2\u0b19\u0b1b\7\u00d1\2\2\u0b1a\u0b19\3\2\2"+
		"\2\u0b1a\u0b1b\3\2\2\2\u0b1b\u0b1f\3\2\2\2\u0b1c\u0b1d\7\u00d9\2\2\u0b1d"+
		"\u0b1f\5\u017c\u00bf\2\u0b1e\u0afc\3\2\2\2\u0b1e\u0b09\3\2\2\2\u0b1e\u0b18"+
		"\3\2\2\2\u0b1e\u0b1c\3\2\2\2\u0b1f\u017d\3\2\2\2\u0b20\u0b24\5\u017c\u00bf"+
		"\2\u0b21\u0b24\5\u0186\u00c4\2\u0b22\u0b24\5\u0180\u00c1\2\u0b23\u0b20"+
		"\3\2\2\2\u0b23\u0b21\3\2\2\2\u0b23\u0b22\3\2\2\2\u0b24\u017f\3\2\2\2\u0b25"+
		"\u0b26\t\33\2\2\u0b26\u0181\3\2\2\2\u0b27\u0b28\7\u00e4\2\2\u0b28\u0b2a"+
		"\7\u00d3\2\2\u0b29\u0b27\3\2\2\2\u0b29\u0b2a\3\2\2\2\u0b2a\u0b2b\3\2\2"+
		"\2\u0b2b\u0b2c\5\u0184\u00c3\2\u0b2c\u0183\3\2\2\2\u0b2d\u0b32\5\u018a"+
		"\u00c6\2\u0b2e\u0b32\5\u0186\u00c4\2\u0b2f\u0b32\5\u0180\u00c1\2\u0b30"+
		"\u0b32\5\u0188\u00c5\2\u0b31\u0b2d\3\2\2\2\u0b31\u0b2e\3\2\2\2\u0b31\u0b2f"+
		"\3\2\2\2\u0b31\u0b30\3\2\2\2\u0b32\u0185\3\2\2\2\u0b33\u0b35\7\u00e4\2"+
		"\2\u0b34\u0b36\7\u00d1\2\2\u0b35\u0b34\3\2\2\2\u0b35\u0b36\3\2\2\2\u0b36"+
		"\u0187\3\2\2\2\u0b37\u0b38\7\u00cf\2\2\u0b38\u0b39\7\u00e4\2\2\u0b39\u0189"+
		"\3\2\2\2\u0b3a\u0b3b\7\u00e4\2\2\u0b3b\u0b44\7\u00d4\2\2\u0b3c\u0b41\5"+
		"\u0182\u00c2\2\u0b3d\u0b3e\7\u00d6\2\2\u0b3e\u0b40\5\u0182\u00c2\2\u0b3f"+
		"\u0b3d\3\2\2\2\u0b40\u0b43\3\2\2\2\u0b41\u0b3f\3\2\2\2\u0b41\u0b42\3\2"+
		"\2\2\u0b42\u0b45\3\2\2\2\u0b43\u0b41\3\2\2\2\u0b44\u0b3c\3\2\2\2\u0b44"+
		"\u0b45\3\2\2\2\u0b45\u0b46\3\2\2\2\u0b46\u0b47\7\u00d5\2\2\u0b47\u018b"+
		"\3\2\2\2\u0152\u018d\u0192\u0198\u01a0\u01ae\u01b7\u01bc\u01c3\u01ca\u01cd"+
		"\u01d4\u01de\u01e2\u01e7\u01eb\u01ef\u01f9\u0201\u0207\u020e\u0215\u0219"+
		"\u021c\u021f\u0228\u022e\u0233\u0236\u023c\u0242\u0246\u024e\u0257\u025e"+
		"\u0264\u0269\u0274\u027d\u0282\u0288\u028c\u0298\u02a3\u02a9\u02b2\u02ba"+
		"\u02c4\u02cd\u02d5\u02da\u02e2\u02e7\u02f1\u02fb\u0301\u0308\u030d\u0315"+
		"\u0319\u031b\u0321\u0326\u032a\u0331\u0333\u033a\u033f\u0348\u034d\u0350"+
		"\u0355\u035e\u036a\u0373\u037e\u0381\u0388\u0392\u039a\u039d\u03a0\u03ad"+
		"\u03b5\u03bb\u03c3\u03c7\u03cb\u03cf\u03d1\u03d5\u03db\u03e6\u03f1\u03f6"+
		"\u03ff\u0404\u0407\u040e\u0417\u042e\u0431\u0434\u043c\u0440\u0448\u044e"+
		"\u0459\u0462\u0467\u0473\u047a\u0487\u0490\u0499\u049f\u04aa\u04af\u04b4"+
		"\u04ba\u04be\u04c2\u04c6\u04c8\u04cc\u04d1\u04e2\u04ea\u04fd\u0501\u0506"+
		"\u050b\u051a\u0541\u054a\u054e\u055d\u0562\u0565\u0567\u0569\u0574\u057c"+
		"\u0580\u0585\u0589\u0593\u0598\u059a\u05a4\u05ad\u05b2\u05b6\u05c8\u05ca"+
		"\u05cf\u05d4\u05d9\u05e2\u05e4\u05e8\u05ed\u05f1\u05f5\u05f9\u0603\u060f"+
		"\u0616\u0619\u061d\u0625\u062a\u0635\u0639\u063d\u0643\u064e\u0657\u0659"+
		"\u0660\u0664\u0672\u0678\u0683\u0686\u0689\u068f\u0698\u06a1\u06ab\u06b1"+
		"\u06b9\u06c0\u06ca\u06d9\u06db\u06e2\u06e4\u06e8\u06f0\u06f8\u0700\u070d"+
		"\u0712\u0714\u071e\u0723\u072c\u0735\u0738\u0740\u074b\u0750\u0752\u0775"+
		"\u077d\u077f\u0784\u078d\u0790\u0795\u0798\u079b\u07a4\u07a7\u07ac\u07af"+
		"\u07b8\u07bb\u07c0\u07c3\u07c7\u07ca\u07ce\u07dc\u07df\u07eb\u07ee\u07fa"+
		"\u07fc\u0806\u0809\u0813\u0827\u082b\u0830\u0834\u0843\u086b\u086e\u0877"+
		"\u087d\u088c\u088f\u0891\u08a2\u08a6\u08ad\u08b0\u08b9\u08bd\u08c1\u08c5"+
		"\u08c8\u08cb\u08cf\u08d5\u08dd\u08e0\u08ef\u08f2\u08fa\u08ff\u0902\u090b"+
		"\u0913\u0915\u0928\u092a\u093c\u093e\u0967\u096a\u09bd\u09c6\u09cf\u09d3"+
		"\u09da\u09e3\u09e5\u09f5\u09fe\u0a00\u0a09\u0a0e\u0a12\u0a15\u0a1c\u0a20"+
		"\u0a2e\u0a31\u0a3b\u0a3e\u0a54\u0a6c\u0a74\u0a77\u0a7a\u0a7d\u0a84\u0a97"+
		"\u0a9d\u0aa7\u0aad\u0ab3\u0ab6\u0abb\u0abd\u0acb\u0ad3\u0ad8\u0ae0\u0ae7"+
		"\u0aef\u0afa\u0b03\u0b06\u0b12\u0b15\u0b1a\u0b1e\u0b23\u0b29\u0b31\u0b35"+
		"\u0b41\u0b44";
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