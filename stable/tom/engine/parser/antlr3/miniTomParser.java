// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g 2016-03-01 15:43:27

package tom.engine.parser.antlr3;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.CommonToken;
import static tom.engine.parser.antlr3.TreeFactory.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class miniTomParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Code", "IsSort", "DoWhileExpression", "ACMatchLoop", "ConstraintToExpression", "GetSliceArray", "GetSliceList", "GetElement", "GetSize", "Substract", "SubstractOne", "AddOne", "IsEmptyArray", "IsEmptyList", "GetTail", "GetHead", "IsFsym", "GetSlot", "Cast", "EqualBQTerm", "EqualTerm", "Integer", "FalseTL", "TrueTL", "Conditional", "AntiMatchExpression", "LessOrEqualThan", "LessThan", "GreaterOrEqualThan", "GreaterThan", "OrConnector", "OrExpressionDisjunction", "Or", "And", "Negation", "Bottom", "TomInstructionToExpression", "BQTermToExpression", "Cst_ConstantString", "Cst_ConstantDouble", "Cst_ConstantChar", "Cst_ConstantLong", "Cst_ConstantInt", "Cst_Symbol", "Cst_TheoryDEFAULT", "Cst_TheoryAC", "Cst_TheoryAU", "ConcCstSymbol", "Cst_Slot", "Cst_StrategyConstruct", "Cst_BQTermToBlock", "Cst_IncludeConstruct", "Cst_MetaQuoteConstruct", "Cst_TypetermConstruct", "Cst_OpListConstruct", "Cst_OpArrayConstruct", "Cst_OpConstruct", "Cst_MatchConstruct", "HOSTBLOCK", "Cst_PairPattern", "ConcCstPairPattern", "Cst_TermAppl", "Cst_TermVariableStar", "Cst_TermVariable", "ConcCstConstraint", "Cst_ConstraintAction", "Cst_OriginTracking", "Cst_Label", "NodeString", "ConcCstVisit", "ConcCstConstraintAction", "Cst_RecordAppl", "Cst_Appl", "Cst_UnamedVariableStar", "Cst_UnamedVariable", "Cst_ConstantStar", "Cst_Constant", "Cst_VariableStar", "Cst_Variable", "Cst_AnnotatedPattern", "Cst_Anti", "ConcCstBQTerm", "ConcCstTerm", "ConcCstOperator", "ConcCstName", "ConcCstPattern", "ConcCstOption", "ConcCstPairSlotBQTerm", "Cst_Program", "Cst_PairSlotBQTerm", "Cst_TypeUnknown", "Cst_Type", "Cst_VisitTerm", "Cst_Equals", "Cst_IsSort", "Cst_Implement", "Cst_MakeEmptyList", "Cst_Make", "Cst_MakeAppend", "Cst_GetElement", "Cst_MakeInsert", "Cst_GetSlot", "Cst_GetDefault", "Cst_GetTail", "Cst_GetSize", "Cst_MakeEmptyArray", "Cst_IsEmpty", "Cst_GetHead", "Cst_IsFsym", "NodeInt", "ConcCstBlock", "Cst_EmptyName", "Cst_Name", "ConcCstSlot", "Cst_NumDifferent", "Cst_NumEqualTo", "Cst_NumGreaterOrEqualTo", "Cst_NumGreaterThan", "Cst_NumLessOrEqualTo", "Cst_NumLessThan", "Cst_OrConstraint", "Cst_AndConstraint", "Cst_MatchArgumentConstraint", "Cst_MatchTermConstraint", "Cst_BQConstant", "Cst_ITL", "Cst_BQComposite", "Cst_BQDefault", "Cst_BQVarStar", "Cst_BQVar", "Cst_BQRecordAppl", "Cst_BQAppl", "AntiName", "EmptyName", "PositionName", "Name", "concTomNumber", "concTomName", "NameNumber", "RenamedVar", "AbsVar", "Position", "Save", "End", "Begin", "IndexNumber", "ListNumber", "PatternNumber", "MatchNumber", "UsedType", "UsedSymbolAC", "UsedSymbolDestructor", "UsedSymbolConstructor", "ResolveStratBlock", "VisitTerm", "concResolveStratElement", "concTomSymbol", "concTomVisit", "Entry", "concTomEntry", "TransfoStratInfo", "ResolveStratElement", "concElementaryTransformation", "concResolveStratBlock", "TextPosition", "StructTable", "ElementaryTransformation", "Symbol", "EmptySymbol", "Table", "TypeForVariable", "DefinedSymbol", "noOption", "ACSymbol", "ImplicitXMLChild", "ImplicitXMLAttribut", "ModuleName", "Debug", "Label", "MatchingTheory", "OriginalText", "OriginTracking", "GeneratedMatch", "TomTermToOption", "TomNameToOption", "DeclarationToOption", "concOption", "concConstraintInstruction", "concInstruction", "Resolve", "TracelinkPopulateResolve", "Tracelink", "RawAction", "CompiledPattern", "CompiledMatch", "Match", "NamedBlock", "UnamedBlock", "AbstractBlock", "Nop", "Return", "AssignArray", "Assign", "LetRef", "Let", "WhileDo", "DoWhile", "If", "CodeToInstruction", "ExpressionToInstruction", "BQTermToInstruction", "concRuleInstruction", "concRefClassTracelinkInstruction", "RefClassTracelinkInstruction", "ConstraintInstruction", "RuleInstruction", "EmptyTargetLanguageType", "TLType", "concTypeOption", "concTomType", "TypeVar", "EmptyType", "TypesToType", "Codomain", "Type", "SubtypeDecl", "WithSymbol", "IntegerPattern", "FalsePattern", "TruePattern", "TestVar", "Automata", "AntiTerm", "TomSymbolToTomTerm", "VariableStar", "Variable", "XMLAppl", "RecordAppl", "TermAppl", "concTomTerm", "concConstraint", "EmptyArrayConstraint", "EmptyListConstraint", "NumericConstraint", "AntiMatchConstraint", "MatchConstraint", "OrConstraintDisjunction", "OrConstraint", "AndConstraint", "IsSortConstraint", "Negate", "FalseConstraint", "TrueConstraint", "AssignPositionTo", "AliasTo", "NumEqual", "NumDifferent", "NumGreaterOrEqualThan", "NumGreaterThan", "NumLessOrEqualThan", "NumLessThan", "TomInclude", "Tom", "BQTermToCode", "DeclarationToCode", "InstructionToCode", "TargetLanguageToCode", "noTL", "Comment", "ITL", "TL", "ListTail", "ListHead", "VariableHeadArray", "VariableHeadList", "Subterm", "SymbolOf", "ExpressionToBQTerm", "BuildAppendArray", "BuildConsArray", "BuildEmptyArray", "BuildAppendList", "BuildConsList", "BuildEmptyList", "BuildTerm", "BuildConstant", "FunctionCall", "ReferencerBQTerm", "Composite", "BQDefault", "BQVariableStar", "BQVariable", "BQRecordAppl", "BQAppl", "concBQTerm", "CompositeTL", "CompositeBQTerm", "concCode", "AU", "AC", "Unitary", "Associative", "Syntactic", "concElementaryTheory", "PairNameOptions", "concTypeConstraint", "FalseTypeConstraint", "Subtype", "Equation", "concDeclaration", "BQTermToDeclaration", "ACSymbolDecl", "AbstractDecl", "EmptyDeclaration", "ArraySymbolDecl", "ListSymbolDecl", "SymbolDecl", "IntrospectorClass", "Class", "MethodDef", "FunctionDef", "ReferenceClass", "ResolveMakeDecl", "ResolveGetSlotDecl", "ResolveIsFsymDecl", "ResolveClassDecl", "ResolveInverseLinksDecl", "ResolveStratDecl", "Transformation", "Strategy", "MakeDecl", "MakeAddArray", "MakeEmptyArray", "GetSizeDecl", "GetElementDecl", "MakeAddList", "MakeEmptyList", "IsEmptyDecl", "GetTailDecl", "GetHeadDecl", "IsSortDecl", "EqualTermDecl", "ImplementDecl", "GetDefaultDecl", "GetSlotDecl", "IsFsymDecl", "TypeTermDecl", "concPairNameDecl", "PairNameDecl", "PairSlotBQTerm", "concSlot", "concBQSlot", "PairSlotAppl", "LBR", "IDENTIFIER", "RBR", "LPAR", "RPAR", "EXTENDS", "BQUOTE", "VISIT", "COLON", "ARROW", "COMMA", "STAR", "AND", "OR", "LARROW", "GREATERTHAN", "GREATEROREQU", "LOWERTHAN", "LOWEROREQU", "DOUBLEEQUAL", "DIFFERENT", "AT", "ANTI", "UNDERSCORE", "PIPE", "QMARK", "DQMARK", "INTEGER", "LONG", "CHAR", "DOUBLE", "STRING", "LSQUAREBR", "RSQUAREBR", "EQUAL", "KEYWORD_IS_FSYM", "KEYWORD_GET_SLOT", "KEYWORD_MAKE", "KEYWORD_GET_DEFAULT", "KEYWORD_IMPLEMENT", "KEYWORD_GET_HEAD", "KEYWORD_GET_TAIL", "KEYWORD_IS_EMPTY", "KEYWORD_MAKE_EMPTY", "KEYWORD_MAKE_INSERT", "KEYWORD_GET_ELEMENT", "KEYWORD_GET_SIZE", "KEYWORD_MAKE_APPEND", "KEYWORD_IS_SORT", "KEYWORD_EQUALS", "DQUOTE", "SQUOTE", "LETTER", "DIGIT", "MINUS", "UNSIGNED_DOUBLE", "LONG_SUFFIX", "HEX_DIGIT", "ESC", "WS", "SL_COMMENT", "ML_COMMENT", "DEFAULT"
    };
    public static final int concInstruction=198;
    public static final int EmptyTargetLanguageType=226;
    public static final int Cst_MatchTermConstraint=133;
    public static final int MakeEmptyList=346;
    public static final int Cst_Label=77;
    public static final int RSQUAREBR=396;
    public static final int NumLessOrEqualThan=269;
    public static final int GetHead=25;
    public static final int ResolveGetSlotDecl=333;
    public static final int KEYWORD_MAKE_EMPTY=406;
    public static final int GREATEROREQU=379;
    public static final int KEYWORD_GET_ELEMENT=408;
    public static final int UsedSymbolConstructor=162;
    public static final int ResolveStratElement=171;
    public static final int VariableStar=244;
    public static final int Cst_RecordAppl=81;
    public static final int GetSlot=27;
    public static final int Cst_MakeEmptyArray=115;
    public static final int Tracelink=201;
    public static final int KEYWORD_GET_TAIL=404;
    public static final int EmptySymbol=178;
    public static final int BQTermToDeclaration=320;
    public static final int GetDefaultDecl=353;
    public static final int Cst_Symbol=53;
    public static final int Automata=241;
    public static final int BQRecordAppl=302;
    public static final int Conditional=34;
    public static final int Cst_OriginTracking=76;
    public static final int Cst_NumGreaterOrEqualTo=126;
    public static final int FalseTL=32;
    public static final int Cst_UnamedVariableStar=83;
    public static final int ACSymbolDecl=321;
    public static final int UNDERSCORE=386;
    public static final int ANTI=385;
    public static final int Cst_Name=122;
    public static final int AndConstraint=258;
    public static final int KEYWORD_MAKE_INSERT=407;
    public static final int NamedBlock=206;
    public static final int CompiledPattern=203;
    public static final int concResolveStratBlock=173;
    public static final int concResolveStratElement=165;
    public static final int Strategy=339;
    public static final int Cst_TypetermConstruct=63;
    public static final int LOWERTHAN=380;
    public static final int SL_COMMENT=423;
    public static final int MakeAddArray=341;
    public static final int ConcCstSymbol=57;
    public static final int KEYWORD_MAKE_APPEND=410;
    public static final int EmptyDeclaration=323;
    public static final int BuildConsArray=289;
    public static final int CodeToInstruction=218;
    public static final int Cst_NumLessThan=129;
    public static final int ConcCstPairSlotBQTerm=97;
    public static final int FalseTypeConstraint=316;
    public static final int Codomain=233;
    public static final int GreaterThan=39;
    public static final int CompositeBQTerm=306;
    public static final int Cst_UnamedVariable=84;
    public static final int AT=384;
    public static final int AU=308;
    public static final int Cst_TheoryDEFAULT=54;
    public static final int AC=309;
    public static final int BuildEmptyArray=290;
    public static final int ConstraintToExpression=14;
    public static final int GetTail=24;
    public static final int OrExpressionDisjunction=41;
    public static final int Return=210;
    public static final int Cst_GetTail=113;
    public static final int VariableHeadArray=283;
    public static final int MatchNumber=158;
    public static final int Cst_BQVarStar=138;
    public static final int TrueConstraint=262;
    public static final int KEYWORD_EQUALS=412;
    public static final int DQMARK=389;
    public static final int TextPosition=174;
    public static final int DeclarationToOption=195;
    public static final int Cst_IsEmpty=116;
    public static final int HEX_DIGIT=420;
    public static final int ListTail=281;
    public static final int Cst_Variable=88;
    public static final int MINUS=417;
    public static final int SQUOTE=414;
    public static final int ArraySymbolDecl=324;
    public static final int concTypeOption=228;
    public static final int Cst_BQAppl=141;
    public static final int Cst_AnnotatedPattern=89;
    public static final int COLON=371;
    public static final int GetSizeDecl=343;
    public static final int Match=205;
    public static final int TomInclude=271;
    public static final int concTomVisit=167;
    public static final int Cst_MakeEmptyList=106;
    public static final int BQTermToExpression=47;
    public static final int KEYWORD_GET_DEFAULT=401;
    public static final int ARROW=372;
    public static final int GetHeadDecl=349;
    public static final int AbstractBlock=208;
    public static final int LONG=391;
    public static final int Position=151;
    public static final int Transformation=338;
    public static final int LessThan=37;
    public static final int Cst_Implement=105;
    public static final int Cst_TermVariable=73;
    public static final int CHAR=392;
    public static final int DIFFERENT=383;
    public static final int concCode=307;
    public static final int Cst_GetSlot=111;
    public static final int Cst_GetHead=117;
    public static final int Cst_BQConstant=134;
    public static final int Cst_Anti=90;
    public static final int noTL=277;
    public static final int FalseConstraint=261;
    public static final int Entry=168;
    public static final int Let=214;
    public static final int LPAR=366;
    public static final int Cst_ITL=135;
    public static final int DOUBLE=393;
    public static final int Cst_OrConstraint=130;
    public static final int NodeString=78;
    public static final int BQTermToInstruction=220;
    public static final int XMLAppl=246;
    public static final int VISIT=370;
    public static final int CompositeTL=305;
    public static final int Cst_Program=98;
    public static final int Table=179;
    public static final int concBQTerm=304;
    public static final int RuleInstruction=225;
    public static final int TruePattern=239;
    public static final int concElementaryTransformation=172;
    public static final int GetSlotDecl=354;
    public static final int TypeForVariable=180;
    public static final int concConstraint=250;
    public static final int OR=376;
    public static final int Cst_Make=107;
    public static final int TransfoStratInfo=170;
    public static final int ConcCstBlock=120;
    public static final int Cst_Appl=82;
    public static final int Cst_Type=101;
    public static final int NumGreaterThan=268;
    public static final int BQVariableStar=300;
    public static final int IsSort=11;
    public static final int AssignArray=211;
    public static final int Unitary=310;
    public static final int Cst_ConstraintAction=75;
    public static final int BuildConstant=295;
    public static final int RBR=365;
    public static final int OrConstraint=257;
    public static final int KEYWORD_GET_SIZE=409;
    public static final int AntiMatchConstraint=254;
    public static final int LARROW=377;
    public static final int EqualBQTerm=29;
    public static final int VariableHeadList=284;
    public static final int ResolveInverseLinksDecl=336;
    public static final int Cst_PairPattern=69;
    public static final int UsedType=159;
    public static final int Begin=154;
    public static final int LBR=363;
    public static final int TomNameToOption=194;
    public static final int PositionName=144;
    public static final int RawAction=202;
    public static final int ImplicitXMLAttribut=185;
    public static final int ResolveStratBlock=163;
    public static final int WithSymbol=236;
    public static final int Cst_TermAppl=71;
    public static final int COMMA=373;
    public static final int EQUAL=397;
    public static final int ImplicitXMLChild=184;
    public static final int Cst_BQTermToBlock=60;
    public static final int ResolveIsFsymDecl=334;
    public static final int SubtypeDecl=235;
    public static final int Cst_BQRecordAppl=140;
    public static final int DIGIT=416;
    public static final int Cst_NumGreaterThan=127;
    public static final int ACMatchLoop=13;
    public static final int ExpressionToInstruction=219;
    public static final int Cst_EmptyName=121;
    public static final int concTomTerm=249;
    public static final int If=217;
    public static final int Negation=44;
    public static final int RecordAppl=247;
    public static final int Cst_Equals=103;
    public static final int HOSTBLOCK=68;
    public static final int Type=234;
    public static final int Cst_ConstantStar=85;
    public static final int DEFAULT=425;
    public static final int IsEmptyList=23;
    public static final int AliasTo=264;
    public static final int MakeEmptyArray=342;
    public static final int BQAppl=303;
    public static final int BuildConsList=292;
    public static final int Cst_Slot=58;
    public static final int Cst_NumLessOrEqualTo=128;
    public static final int ConcCstBQTerm=91;
    public static final int Cst_Constant=86;
    public static final int OriginalText=190;
    public static final int Cst_OpConstruct=66;
    public static final int DeclarationToCode=274;
    public static final int FalsePattern=238;
    public static final int ModuleName=186;
    public static final int ConcCstPairPattern=70;
    public static final int Cst_BQComposite=136;
    public static final int DoWhileExpression=12;
    public static final int UNSIGNED_DOUBLE=418;
    public static final int ConcCstVisit=79;
    public static final int AssignPositionTo=263;
    public static final int TypeVar=230;
    public static final int STAR=374;
    public static final int FunctionDef=330;
    public static final int KEYWORD_IS_EMPTY=405;
    public static final int concDeclaration=319;
    public static final int LETTER=415;
    public static final int BQDefault=299;
    public static final int ListSymbolDecl=325;
    public static final int noOption=182;
    public static final int DQUOTE=413;
    public static final int Cst_AndConstraint=131;
    public static final int Cst_OpListConstruct=64;
    public static final int EOF=-1;
    public static final int concSlot=360;
    public static final int SymbolOf=286;
    public static final int ResolveClassDecl=335;
    public static final int PairNameDecl=358;
    public static final int UnamedBlock=207;
    public static final int IsEmptyDecl=347;
    public static final int concTomNumber=146;
    public static final int KEYWORD_GET_HEAD=403;
    public static final int CompiledMatch=204;
    public static final int Cst_StrategyConstruct=59;
    public static final int Class=328;
    public static final int MethodDef=329;
    public static final int KEYWORD_GET_SLOT=399;
    public static final int Cst_IsSort=104;
    public static final int MakeAddList=345;
    public static final int Code=10;
    public static final int AntiMatchExpression=35;
    public static final int RefClassTracelinkInstruction=223;
    public static final int concTypeConstraint=315;
    public static final int ListHead=282;
    public static final int SubstractOne=20;
    public static final int WS=422;
    public static final int Equation=318;
    public static final int Debug=187;
    public static final int TomInstructionToExpression=46;
    public static final int TLType=227;
    public static final int AntiName=142;
    public static final int concRefClassTracelinkInstruction=222;
    public static final int ReferencerBQTerm=297;
    public static final int AbsVar=150;
    public static final int GetSliceList=16;
    public static final int Label=188;
    public static final int concBQSlot=361;
    public static final int ImplementDecl=352;
    public static final int IsSortConstraint=259;
    public static final int DoWhile=216;
    public static final int Cst_GetSize=114;
    public static final int PairSlotBQTerm=359;
    public static final int Cst_ConstantChar=50;
    public static final int KEYWORD_MAKE=400;
    public static final int LetRef=213;
    public static final int GetSize=18;
    public static final int IsSortDecl=350;
    public static final int IsEmptyArray=22;
    public static final int Assign=212;
    public static final int AddOne=21;
    public static final int concTomName=147;
    public static final int StructTable=175;
    public static final int BQVariable=301;
    public static final int TypeTermDecl=356;
    public static final int NumDifferent=266;
    public static final int concOption=196;
    public static final int MatchConstraint=255;
    public static final int Cst_OpArrayConstruct=65;
    public static final int NodeInt=119;
    public static final int LOWEROREQU=381;
    public static final int Subtype=317;
    public static final int WhileDo=215;
    public static final int PIPE=387;
    public static final int Cst_MatchConstruct=67;
    public static final int EqualTerm=30;
    public static final int GetSliceArray=15;
    public static final int Or=42;
    public static final int ConcCstName=94;
    public static final int Resolve=199;
    public static final int Composite=298;
    public static final int BuildEmptyList=293;
    public static final int INTEGER=390;
    public static final int concPairNameDecl=357;
    public static final int TL=280;
    public static final int Cst_BQVar=139;
    public static final int UsedSymbolAC=160;
    public static final int Cst_VisitTerm=102;
    public static final int OriginTracking=191;
    public static final int OrConstraintDisjunction=256;
    public static final int EmptyArrayConstraint=251;
    public static final int Integer=31;
    public static final int concRuleInstruction=221;
    public static final int Save=152;
    public static final int STRING=394;
    public static final int ConcCstPattern=95;
    public static final int concElementaryTheory=313;
    public static final int KEYWORD_IS_SORT=411;
    public static final int LessOrEqualThan=36;
    public static final int Associative=311;
    public static final int ConcCstConstraint=74;
    public static final int ConcCstOption=96;
    public static final int Cst_ConstantString=48;
    public static final int Cst_NumDifferent=124;
    public static final int GeneratedMatch=192;
    public static final int Cst_GetDefault=112;
    public static final int ConcCstSlot=123;
    public static final int ListNumber=156;
    public static final int EmptyName=143;
    public static final int ConcCstConstraintAction=80;
    public static final int Negate=260;
    public static final int IntrospectorClass=327;
    public static final int RenamedVar=149;
    public static final int ConstraintInstruction=224;
    public static final int BuildAppendList=291;
    public static final int MatchingTheory=189;
    public static final int Variable=245;
    public static final int Tom=272;
    public static final int AbstractDecl=322;
    public static final int Comment=278;
    public static final int Cst_MakeInsert=110;
    public static final int IsFsym=26;
    public static final int EqualTermDecl=351;
    public static final int Bottom=45;
    public static final int GetElement=17;
    public static final int Cst_ConstantDouble=49;
    public static final int ConcCstOperator=93;
    public static final int TargetLanguageToCode=276;
    public static final int GREATERTHAN=378;
    public static final int Cst_GetElement=109;
    public static final int AntiTerm=242;
    public static final int concConstraintInstruction=197;
    public static final int Cst_TypeUnknown=100;
    public static final int GetElementDecl=344;
    public static final int concTomEntry=169;
    public static final int LONG_SUFFIX=419;
    public static final int ConcCstTerm=92;
    public static final int BuildTerm=294;
    public static final int LSQUAREBR=395;
    public static final int concTomSymbol=166;
    public static final int TracelinkPopulateResolve=200;
    public static final int ITL=279;
    public static final int Cst_MakeAppend=108;
    public static final int NameNumber=148;
    public static final int ReferenceClass=331;
    public static final int GetTailDecl=348;
    public static final int UsedSymbolDestructor=161;
    public static final int VisitTerm=164;
    public static final int PairSlotAppl=362;
    public static final int OrConnector=40;
    public static final int Cst_ConstantLong=51;
    public static final int QMARK=388;
    public static final int SymbolDecl=326;
    public static final int Syntactic=312;
    public static final int End=153;
    public static final int PairNameOptions=314;
    public static final int ESC=421;
    public static final int Cst_BQDefault=137;
    public static final int ResolveMakeDecl=332;
    public static final int IntegerPattern=237;
    public static final int ExpressionToBQTerm=287;
    public static final int BuildAppendArray=288;
    public static final int Cast=28;
    public static final int AND=375;
    public static final int PatternNumber=157;
    public static final int ML_COMMENT=424;
    public static final int NumGreaterOrEqualThan=267;
    public static final int Cst_PairSlotBQTerm=99;
    public static final int Cst_IsFsym=118;
    public static final int Cst_NumEqualTo=125;
    public static final int IndexNumber=155;
    public static final int NumEqual=265;
    public static final int IDENTIFIER=364;
    public static final int FunctionCall=296;
    public static final int Cst_MatchArgumentConstraint=132;
    public static final int TomTermToOption=193;
    public static final int KEYWORD_IS_FSYM=398;
    public static final int IsFsymDecl=355;
    public static final int TestVar=240;
    public static final int Cst_VariableStar=87;
    public static final int Symbol=177;
    public static final int DOUBLEEQUAL=382;
    public static final int EmptyType=231;
    public static final int And=43;
    public static final int KEYWORD_IMPLEMENT=402;
    public static final int Substract=19;
    public static final int Cst_IncludeConstruct=61;
    public static final int Cst_TheoryAC=55;
    public static final int MakeDecl=340;
    public static final int ResolveStratDecl=337;
    public static final int TermAppl=248;
    public static final int TomSymbolToTomTerm=243;
    public static final int BQTermToCode=273;
    public static final int DefinedSymbol=181;
    public static final int ElementaryTransformation=176;
    public static final int TrueTL=33;
    public static final int Cst_TheoryAU=56;
    public static final int GreaterOrEqualThan=38;
    public static final int TypesToType=232;
    public static final int ACSymbol=183;
    public static final int NumLessThan=270;
    public static final int Cst_ConstantInt=52;
    public static final int concTomType=229;
    public static final int Cst_TermVariableStar=72;
    public static final int Subterm=285;
    public static final int InstructionToCode=275;
    public static final int Name=145;
    public static final int Nop=209;
    public static final int BQUOTE=369;
    public static final int EmptyListConstraint=252;
    public static final int RPAR=367;
    public static final int EXTENDS=368;
    public static final int NumericConstraint=253;
    public static final int Cst_MetaQuoteConstruct=62;

    // delegates
    // delegators


        public miniTomParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public miniTomParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return miniTomParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g"; }



      public static CommonTree extractOptions(CommonToken t) {
        String newline = System.getProperty("line.separator");
        String lines[] = t.getText().split(newline);
        
        int firstCharLine = t.getLine();
        int firstCharColumn = t.getCharPositionInLine()+1;
        int lastCharLine = firstCharLine+lines.length-1;
        int lastCharColumn;
        if(lines.length==1) {
          lastCharColumn = firstCharColumn + lines[0].length();
        } else {
          lastCharColumn = lines[lines.length-1].length();
        }
      
        return makeOptions(t.getInputStream().getSourceName(),
          firstCharLine, firstCharColumn, lastCharLine, lastCharColumn);  
      }

      public static CommonTree extractOptions(CommonToken start, CommonToken end) {
        String newline = System.getProperty("line.separator");
        String lines[] = end.getText().split(newline);

        int lastCharLine = end.getLine()+lines.length;
        int lastCharColumn;
        if(lines.length==1) {
          lastCharColumn = end.getCharPositionInLine() + lines[0].length();
        } else {
          lastCharColumn = lines[lines.length-1].length();
        }

        return makeOptions(start.getInputStream().getSourceName(),
          start.getLine(), start.getCharPositionInLine(), lastCharLine,
          lastCharColumn);
      }


    public static class csIncludeConstruct_return extends ParserRuleReturnScope {
        public int marker;
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csIncludeConstruct"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:88:1: csIncludeConstruct returns [int marker] : LBR filename= IDENTIFIER RBR -> ^( Cst_IncludeConstruct $filename) ;
    public final miniTomParser.csIncludeConstruct_return csIncludeConstruct() throws RecognitionException {
        miniTomParser.csIncludeConstruct_return retval = new miniTomParser.csIncludeConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token filename=null;
        Token LBR1=null;
        Token RBR2=null;

        Tree filename_tree=null;
        Tree LBR1_tree=null;
        Tree RBR2_tree=null;
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");

        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:89:22: ( LBR filename= IDENTIFIER RBR -> ^( Cst_IncludeConstruct $filename) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:91:20: LBR filename= IDENTIFIER RBR
            {
            LBR1=(Token)match(input,LBR,FOLLOW_LBR_in_csIncludeConstruct90); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR1);

            filename=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csIncludeConstruct94); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(filename);

            RBR2=(Token)match(input,RBR,FOLLOW_RBR_in_csIncludeConstruct96); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR2);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR2).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: filename
            // token labels: filename
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_filename=new RewriteRuleTokenStream(adaptor,"token filename",filename);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 96:3: -> ^( Cst_IncludeConstruct $filename)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:96:6: ^( Cst_IncludeConstruct $filename)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_IncludeConstruct, "Cst_IncludeConstruct"), root_1);

                adaptor.addChild(root_1, stream_filename.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csIncludeConstruct"

    public static class csStrategyConstruct_return extends ParserRuleReturnScope {
        public int marker;
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csStrategyConstruct"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:100:1: csStrategyConstruct returns [int marker] : csName LPAR csSlotList RPAR EXTENDS ( BQUOTE )? csBQTerm[false] LBR csStrategyVisitList RBR -> ^( Cst_StrategyConstruct csName csSlotList csBQTerm csStrategyVisitList ) ;
    public final miniTomParser.csStrategyConstruct_return csStrategyConstruct() throws RecognitionException {
        miniTomParser.csStrategyConstruct_return retval = new miniTomParser.csStrategyConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAR4=null;
        Token RPAR6=null;
        Token EXTENDS7=null;
        Token BQUOTE8=null;
        Token LBR10=null;
        Token RBR12=null;
        miniTomParser.csName_return csName3 = null;

        miniTomParser.csSlotList_return csSlotList5 = null;

        miniTomParser.csBQTerm_return csBQTerm9 = null;

        miniTomParser.csStrategyVisitList_return csStrategyVisitList11 = null;


        Tree LPAR4_tree=null;
        Tree RPAR6_tree=null;
        Tree EXTENDS7_tree=null;
        Tree BQUOTE8_tree=null;
        Tree LBR10_tree=null;
        Tree RBR12_tree=null;
        RewriteRuleTokenStream stream_BQUOTE=new RewriteRuleTokenStream(adaptor,"token BQUOTE");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_EXTENDS=new RewriteRuleTokenStream(adaptor,"token EXTENDS");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        RewriteRuleSubtreeStream stream_csBQTerm=new RewriteRuleSubtreeStream(adaptor,"rule csBQTerm");
        RewriteRuleSubtreeStream stream_csSlotList=new RewriteRuleSubtreeStream(adaptor,"rule csSlotList");
        RewriteRuleSubtreeStream stream_csStrategyVisitList=new RewriteRuleSubtreeStream(adaptor,"rule csStrategyVisitList");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:101:21: ( csName LPAR csSlotList RPAR EXTENDS ( BQUOTE )? csBQTerm[false] LBR csStrategyVisitList RBR -> ^( Cst_StrategyConstruct csName csSlotList csBQTerm csStrategyVisitList ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:102:1: csName LPAR csSlotList RPAR EXTENDS ( BQUOTE )? csBQTerm[false] LBR csStrategyVisitList RBR
            {
            pushFollow(FOLLOW_csName_in_csStrategyConstruct129);
            csName3=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(csName3.getTree());
            LPAR4=(Token)match(input,LPAR,FOLLOW_LPAR_in_csStrategyConstruct131); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR4);

            pushFollow(FOLLOW_csSlotList_in_csStrategyConstruct133);
            csSlotList5=csSlotList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csSlotList.add(csSlotList5.getTree());
            RPAR6=(Token)match(input,RPAR,FOLLOW_RPAR_in_csStrategyConstruct135); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR6);

            EXTENDS7=(Token)match(input,EXTENDS,FOLLOW_EXTENDS_in_csStrategyConstruct137); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EXTENDS.add(EXTENDS7);

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:102:37: ( BQUOTE )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==BQUOTE) ) {
                int LA1_1 = input.LA(2);

                if ( (synpred1_miniTom()) ) {
                    alt1=1;
                }
            }
            switch (alt1) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:0:0: BQUOTE
                    {
                    BQUOTE8=(Token)match(input,BQUOTE,FOLLOW_BQUOTE_in_csStrategyConstruct139); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQUOTE.add(BQUOTE8);


                    }
                    break;

            }

            pushFollow(FOLLOW_csBQTerm_in_csStrategyConstruct142);
            csBQTerm9=csBQTerm(false);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csBQTerm.add(csBQTerm9.getTree());
            LBR10=(Token)match(input,LBR,FOLLOW_LBR_in_csStrategyConstruct145); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR10);

            pushFollow(FOLLOW_csStrategyVisitList_in_csStrategyConstruct147);
            csStrategyVisitList11=csStrategyVisitList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csStrategyVisitList.add(csStrategyVisitList11.getTree());
            RBR12=(Token)match(input,RBR,FOLLOW_RBR_in_csStrategyConstruct149); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR12);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR12).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: csName, csBQTerm, csSlotList, csStrategyVisitList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 106:3: -> ^( Cst_StrategyConstruct csName csSlotList csBQTerm csStrategyVisitList )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:106:6: ^( Cst_StrategyConstruct csName csSlotList csBQTerm csStrategyVisitList )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_StrategyConstruct, "Cst_StrategyConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)LPAR4, (CommonToken)RBR12));
                adaptor.addChild(root_1, stream_csName.nextTree());
                adaptor.addChild(root_1, stream_csSlotList.nextTree());
                adaptor.addChild(root_1, stream_csBQTerm.nextTree());
                adaptor.addChild(root_1, stream_csStrategyVisitList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csStrategyConstruct"

    public static class csStrategyVisitList_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csStrategyVisitList"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:115:1: csStrategyVisitList : ( csStrategyVisit )* -> ^( ConcCstVisit ( csStrategyVisit )* ) ;
    public final miniTomParser.csStrategyVisitList_return csStrategyVisitList() throws RecognitionException {
        miniTomParser.csStrategyVisitList_return retval = new miniTomParser.csStrategyVisitList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        miniTomParser.csStrategyVisit_return csStrategyVisit13 = null;


        RewriteRuleSubtreeStream stream_csStrategyVisit=new RewriteRuleSubtreeStream(adaptor,"rule csStrategyVisit");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:115:21: ( ( csStrategyVisit )* -> ^( ConcCstVisit ( csStrategyVisit )* ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:116:3: ( csStrategyVisit )*
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:116:3: ( csStrategyVisit )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==VISIT) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:0:0: csStrategyVisit
            	    {
            	    pushFollow(FOLLOW_csStrategyVisit_in_csStrategyVisitList251);
            	    csStrategyVisit13=csStrategyVisit();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csStrategyVisit.add(csStrategyVisit13.getTree());

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);



            // AST REWRITE
            // elements: csStrategyVisit
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 116:20: -> ^( ConcCstVisit ( csStrategyVisit )* )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:116:23: ^( ConcCstVisit ( csStrategyVisit )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstVisit, "ConcCstVisit"), root_1);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:116:38: ( csStrategyVisit )*
                while ( stream_csStrategyVisit.hasNext() ) {
                    adaptor.addChild(root_1, stream_csStrategyVisit.nextTree());

                }
                stream_csStrategyVisit.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csStrategyVisitList"

    public static class csStrategyVisit_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csStrategyVisit"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:119:1: csStrategyVisit : VISIT IDENTIFIER LBR ( csVisitAction )* RBR -> ^( Cst_VisitTerm ^( Cst_Type IDENTIFIER ) ^( ConcCstConstraintAction ( csVisitAction )* ) ) ;
    public final miniTomParser.csStrategyVisit_return csStrategyVisit() throws RecognitionException {
        miniTomParser.csStrategyVisit_return retval = new miniTomParser.csStrategyVisit_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token VISIT14=null;
        Token IDENTIFIER15=null;
        Token LBR16=null;
        Token RBR18=null;
        miniTomParser.csVisitAction_return csVisitAction17 = null;


        Tree VISIT14_tree=null;
        Tree IDENTIFIER15_tree=null;
        Tree LBR16_tree=null;
        Tree RBR18_tree=null;
        RewriteRuleTokenStream stream_VISIT=new RewriteRuleTokenStream(adaptor,"token VISIT");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csVisitAction=new RewriteRuleSubtreeStream(adaptor,"rule csVisitAction");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:119:17: ( VISIT IDENTIFIER LBR ( csVisitAction )* RBR -> ^( Cst_VisitTerm ^( Cst_Type IDENTIFIER ) ^( ConcCstConstraintAction ( csVisitAction )* ) ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:120:3: VISIT IDENTIFIER LBR ( csVisitAction )* RBR
            {
            VISIT14=(Token)match(input,VISIT,FOLLOW_VISIT_in_csStrategyVisit274); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_VISIT.add(VISIT14);

            IDENTIFIER15=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csStrategyVisit276); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER15);

            LBR16=(Token)match(input,LBR,FOLLOW_LBR_in_csStrategyVisit278); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR16);

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:120:24: ( csVisitAction )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==IDENTIFIER||LA3_0==LPAR||(LA3_0>=ANTI && LA3_0<=UNDERSCORE)||(LA3_0>=INTEGER && LA3_0<=STRING)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:120:25: csVisitAction
            	    {
            	    pushFollow(FOLLOW_csVisitAction_in_csStrategyVisit281);
            	    csVisitAction17=csVisitAction();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csVisitAction.add(csVisitAction17.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            RBR18=(Token)match(input,RBR,FOLLOW_RBR_in_csStrategyVisit285); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR18);



            // AST REWRITE
            // elements: csVisitAction, IDENTIFIER
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 121:5: -> ^( Cst_VisitTerm ^( Cst_Type IDENTIFIER ) ^( ConcCstConstraintAction ( csVisitAction )* ) )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:121:8: ^( Cst_VisitTerm ^( Cst_Type IDENTIFIER ) ^( ConcCstConstraintAction ( csVisitAction )* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_VisitTerm, "Cst_VisitTerm"), root_1);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:122:11: ^( Cst_Type IDENTIFIER )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_IDENTIFIER.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:123:11: ^( ConcCstConstraintAction ( csVisitAction )* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstConstraintAction, "ConcCstConstraintAction"), root_2);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:123:37: ( csVisitAction )*
                while ( stream_csVisitAction.hasNext() ) {
                    adaptor.addChild(root_2, stream_csVisitAction.nextTree());

                }
                stream_csVisitAction.reset();

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, extractOptions((CommonToken)VISIT14, (CommonToken)RBR18));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csStrategyVisit"

    public static class csVisitAction_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csVisitAction"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:128:1: csVisitAction : ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) | ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW csBQTerm[false] -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ) ) );
    public final miniTomParser.csVisitAction_return csVisitAction() throws RecognitionException {
        miniTomParser.csVisitAction_return retval = new miniTomParser.csVisitAction_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token l=null;
        Token IDENTIFIER19=null;
        Token ARROW21=null;
        Token LBR22=null;
        Token RBR23=null;
        Token IDENTIFIER24=null;
        Token ARROW26=null;
        miniTomParser.csExtendedConstraint_return csExtendedConstraint20 = null;

        miniTomParser.csExtendedConstraint_return csExtendedConstraint25 = null;

        miniTomParser.csBQTerm_return csBQTerm27 = null;


        Tree l_tree=null;
        Tree IDENTIFIER19_tree=null;
        Tree ARROW21_tree=null;
        Tree LBR22_tree=null;
        Tree RBR23_tree=null;
        Tree IDENTIFIER24_tree=null;
        Tree ARROW26_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csBQTerm=new RewriteRuleSubtreeStream(adaptor,"rule csBQTerm");
        RewriteRuleSubtreeStream stream_csExtendedConstraint=new RewriteRuleSubtreeStream(adaptor,"rule csExtendedConstraint");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:128:15: ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) | ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW csBQTerm[false] -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ) ) )
            int alt6=2;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR
                    {
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( IDENTIFIER l= COLON )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==IDENTIFIER) ) {
                        int LA4_1 = input.LA(2);

                        if ( (LA4_1==COLON) ) {
                            alt4=1;
                        }
                    }
                    switch (alt4) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:129:4: IDENTIFIER l= COLON
                            {
                            IDENTIFIER19=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csVisitAction367); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER19);

                            l=(Token)match(input,COLON,FOLLOW_COLON_in_csVisitAction371); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COLON.add(l);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_csExtendedConstraint_in_csVisitAction375);
                    csExtendedConstraint20=csExtendedConstraint();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csExtendedConstraint.add(csExtendedConstraint20.getTree());
                    ARROW21=(Token)match(input,ARROW,FOLLOW_ARROW_in_csVisitAction377); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARROW.add(ARROW21);

                    LBR22=(Token)match(input,LBR,FOLLOW_LBR_in_csVisitAction379); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBR.add(LBR22);

                    RBR23=(Token)match(input,RBR,FOLLOW_RBR_in_csVisitAction381); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBR.add(RBR23);



                    // AST REWRITE
                    // elements: csExtendedConstraint, csExtendedConstraint, IDENTIFIER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 130:5: -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                    if (l!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:130:20: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                        adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                        adaptor.addChild(root_1, ((CustomToken)LBR22).getPayload(Tree.class));
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:132:28: ^( ConcCstOption ^( Cst_Label IDENTIFIER ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:132:44: ^( Cst_Label IDENTIFIER )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Label, "Cst_Label"), root_3);

                        adaptor.addChild(root_3, stream_IDENTIFIER.nextNode());

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 134:5: -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:134:19: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                        adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                        adaptor.addChild(root_1, ((CustomToken)LBR22).getPayload(Tree.class));
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:136:28: ^( ConcCstOption )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:138:5: ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW csBQTerm[false]
                    {
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:138:5: ( IDENTIFIER l= COLON )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==IDENTIFIER) ) {
                        int LA5_1 = input.LA(2);

                        if ( (LA5_1==COLON) ) {
                            alt5=1;
                        }
                    }
                    switch (alt5) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:138:6: IDENTIFIER l= COLON
                            {
                            IDENTIFIER24=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csVisitAction602); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER24);

                            l=(Token)match(input,COLON,FOLLOW_COLON_in_csVisitAction606); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COLON.add(l);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_csExtendedConstraint_in_csVisitAction610);
                    csExtendedConstraint25=csExtendedConstraint();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csExtendedConstraint.add(csExtendedConstraint25.getTree());
                    ARROW26=(Token)match(input,ARROW,FOLLOW_ARROW_in_csVisitAction612); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARROW.add(ARROW26);

                    pushFollow(FOLLOW_csBQTerm_in_csVisitAction614);
                    csBQTerm27=csBQTerm(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csBQTerm.add(csBQTerm27.getTree());


                    // AST REWRITE
                    // elements: csExtendedConstraint, csBQTerm, IDENTIFIER, csBQTerm, csExtendedConstraint
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 139:5: -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                    if (l!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:139:20: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                        adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:142:28: ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBlock, "ConcCstBlock"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:142:43: ^( Cst_BQTermToBlock csBQTerm )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQTermToBlock, "Cst_BQTermToBlock"), root_3);

                        adaptor.addChild(root_3, stream_csBQTerm.nextTree());

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:143:28: ^( ConcCstOption ^( Cst_Label IDENTIFIER ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:143:44: ^( Cst_Label IDENTIFIER )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Label, "Cst_Label"), root_3);

                        adaptor.addChild(root_3, stream_IDENTIFIER.nextNode());

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 145:5: -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:145:19: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                        adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:148:28: ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBlock, "ConcCstBlock"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:148:43: ^( Cst_BQTermToBlock csBQTerm )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQTermToBlock, "Cst_BQTermToBlock"), root_3);

                        adaptor.addChild(root_3, stream_csBQTerm.nextTree());

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:149:28: ^( ConcCstOption )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csVisitAction"

    public static class csMatchConstruct_return extends ParserRuleReturnScope {
        public int marker;
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csMatchConstruct"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:159:1: csMatchConstruct returns [int marker] : ( LPAR csBQTerm[false] ( COMMA csBQTerm[false] )* RPAR LBR ( csExtendedConstraintAction )* RBR -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ( csBQTerm )* ) ^( ConcCstConstraintAction ( csExtendedConstraintAction )* ) ) | ( LPAR RPAR )? LBR ( csConstraintAction )* RBR -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ) ^( ConcCstConstraintAction ( csConstraintAction )* ) ) );
    public final miniTomParser.csMatchConstruct_return csMatchConstruct() throws RecognitionException {
        miniTomParser.csMatchConstruct_return retval = new miniTomParser.csMatchConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAR28=null;
        Token COMMA30=null;
        Token RPAR32=null;
        Token LBR33=null;
        Token RBR35=null;
        Token LPAR36=null;
        Token RPAR37=null;
        Token LBR38=null;
        Token RBR40=null;
        miniTomParser.csBQTerm_return csBQTerm29 = null;

        miniTomParser.csBQTerm_return csBQTerm31 = null;

        miniTomParser.csExtendedConstraintAction_return csExtendedConstraintAction34 = null;

        miniTomParser.csConstraintAction_return csConstraintAction39 = null;


        Tree LPAR28_tree=null;
        Tree COMMA30_tree=null;
        Tree RPAR32_tree=null;
        Tree LBR33_tree=null;
        Tree RBR35_tree=null;
        Tree LPAR36_tree=null;
        Tree RPAR37_tree=null;
        Tree LBR38_tree=null;
        Tree RBR40_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csBQTerm=new RewriteRuleSubtreeStream(adaptor,"rule csBQTerm");
        RewriteRuleSubtreeStream stream_csConstraintAction=new RewriteRuleSubtreeStream(adaptor,"rule csConstraintAction");
        RewriteRuleSubtreeStream stream_csExtendedConstraintAction=new RewriteRuleSubtreeStream(adaptor,"rule csExtendedConstraintAction");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:160:21: ( LPAR csBQTerm[false] ( COMMA csBQTerm[false] )* RPAR LBR ( csExtendedConstraintAction )* RBR -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ( csBQTerm )* ) ^( ConcCstConstraintAction ( csExtendedConstraintAction )* ) ) | ( LPAR RPAR )? LBR ( csConstraintAction )* RBR -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ) ^( ConcCstConstraintAction ( csConstraintAction )* ) ) )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==LPAR) ) {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==RPAR) ) {
                    alt11=2;
                }
                else if ( (LA11_1==IDENTIFIER||LA11_1==BQUOTE||(LA11_1>=INTEGER && LA11_1<=STRING)) ) {
                    alt11=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA11_0==LBR) ) {
                alt11=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:165:1: LPAR csBQTerm[false] ( COMMA csBQTerm[false] )* RPAR LBR ( csExtendedConstraintAction )* RBR
                    {
                    LPAR28=(Token)match(input,LPAR,FOLLOW_LPAR_in_csMatchConstruct972); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR28);

                    pushFollow(FOLLOW_csBQTerm_in_csMatchConstruct974);
                    csBQTerm29=csBQTerm(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csBQTerm.add(csBQTerm29.getTree());
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:165:42: ( COMMA csBQTerm[false] )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==COMMA) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:165:43: COMMA csBQTerm[false]
                    	    {
                    	    COMMA30=(Token)match(input,COMMA,FOLLOW_COMMA_in_csMatchConstruct980); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA30);

                    	    pushFollow(FOLLOW_csBQTerm_in_csMatchConstruct982);
                    	    csBQTerm31=csBQTerm(false);

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csBQTerm.add(csBQTerm31.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);

                    RPAR32=(Token)match(input,RPAR,FOLLOW_RPAR_in_csMatchConstruct989); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR32);

                    LBR33=(Token)match(input,LBR,FOLLOW_LBR_in_csMatchConstruct991); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBR.add(LBR33);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:167:1: ( csExtendedConstraintAction )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==IDENTIFIER||LA8_0==LPAR||(LA8_0>=ANTI && LA8_0<=UNDERSCORE)||(LA8_0>=INTEGER && LA8_0<=STRING)) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:0:0: csExtendedConstraintAction
                    	    {
                    	    pushFollow(FOLLOW_csExtendedConstraintAction_in_csMatchConstruct993);
                    	    csExtendedConstraintAction34=csExtendedConstraintAction();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csExtendedConstraintAction.add(csExtendedConstraintAction34.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);

                    RBR35=(Token)match(input,RBR,FOLLOW_RBR_in_csMatchConstruct996); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBR.add(RBR35);

                    if ( state.backtracking==0 ) {
                      retval.marker = ((CustomToken)RBR35).getPayload(Integer.class);
                    }


                    // AST REWRITE
                    // elements: csExtendedConstraintAction, csBQTerm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 173:1: -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ( csBQTerm )* ) ^( ConcCstConstraintAction ( csExtendedConstraintAction )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:173:4: ^( Cst_MatchConstruct ^( ConcCstBQTerm ( csBQTerm )* ) ^( ConcCstConstraintAction ( csExtendedConstraintAction )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MatchConstruct, "Cst_MatchConstruct"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)LPAR28, (CommonToken)RBR35));
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:175:5: ^( ConcCstBQTerm ( csBQTerm )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:175:22: ( csBQTerm )*
                        while ( stream_csBQTerm.hasNext() ) {
                            adaptor.addChild(root_2, stream_csBQTerm.nextTree());

                        }
                        stream_csBQTerm.reset();

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:176:5: ^( ConcCstConstraintAction ( csExtendedConstraintAction )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstConstraintAction, "ConcCstConstraintAction"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:176:32: ( csExtendedConstraintAction )*
                        while ( stream_csExtendedConstraintAction.hasNext() ) {
                            adaptor.addChild(root_2, stream_csExtendedConstraintAction.nextTree());

                        }
                        stream_csExtendedConstraintAction.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:181:1: ( LPAR RPAR )? LBR ( csConstraintAction )* RBR
                    {
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:181:1: ( LPAR RPAR )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==LPAR) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:181:2: LPAR RPAR
                            {
                            LPAR36=(Token)match(input,LPAR,FOLLOW_LPAR_in_csMatchConstruct1051); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_LPAR.add(LPAR36);

                            RPAR37=(Token)match(input,RPAR,FOLLOW_RPAR_in_csMatchConstruct1053); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_RPAR.add(RPAR37);


                            }
                            break;

                    }

                    LBR38=(Token)match(input,LBR,FOLLOW_LBR_in_csMatchConstruct1057); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBR.add(LBR38);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:183:1: ( csConstraintAction )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==IDENTIFIER||LA10_0==LPAR||(LA10_0>=ANTI && LA10_0<=UNDERSCORE)||(LA10_0>=INTEGER && LA10_0<=STRING)) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:0:0: csConstraintAction
                    	    {
                    	    pushFollow(FOLLOW_csConstraintAction_in_csMatchConstruct1059);
                    	    csConstraintAction39=csConstraintAction();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csConstraintAction.add(csConstraintAction39.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);

                    RBR40=(Token)match(input,RBR,FOLLOW_RBR_in_csMatchConstruct1062); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBR.add(RBR40);

                    if ( state.backtracking==0 ) {
                      retval.marker = ((CustomToken)RBR40).getPayload(Integer.class);
                    }


                    // AST REWRITE
                    // elements: csConstraintAction
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 189:1: -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ) ^( ConcCstConstraintAction ( csConstraintAction )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:189:4: ^( Cst_MatchConstruct ^( ConcCstBQTerm ) ^( ConcCstConstraintAction ( csConstraintAction )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MatchConstruct, "Cst_MatchConstruct"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)LBR38, (CommonToken)RBR40));
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:191:5: ^( ConcCstBQTerm )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:192:5: ^( ConcCstConstraintAction ( csConstraintAction )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstConstraintAction, "ConcCstConstraintAction"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:192:32: ( csConstraintAction )*
                        while ( stream_csConstraintAction.hasNext() ) {
                            adaptor.addChild(root_2, stream_csConstraintAction.nextTree());

                        }
                        stream_csConstraintAction.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csMatchConstruct"

    public static class csConstraintAction_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csConstraintAction"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:196:1: csConstraintAction : ( IDENTIFIER l= COLON )? csConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ) ) ;
    public final miniTomParser.csConstraintAction_return csConstraintAction() throws RecognitionException {
        miniTomParser.csConstraintAction_return retval = new miniTomParser.csConstraintAction_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token l=null;
        Token IDENTIFIER41=null;
        Token ARROW43=null;
        Token LBR44=null;
        Token RBR45=null;
        miniTomParser.csConstraint_return csConstraint42 = null;


        Tree l_tree=null;
        Tree IDENTIFIER41_tree=null;
        Tree ARROW43_tree=null;
        Tree LBR44_tree=null;
        Tree RBR45_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csConstraint=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:196:20: ( ( IDENTIFIER l= COLON )? csConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ) ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:197:1: ( IDENTIFIER l= COLON )? csConstraint ARROW LBR RBR
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:197:1: ( IDENTIFIER l= COLON )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==IDENTIFIER) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==COLON) ) {
                    alt12=1;
                }
            }
            switch (alt12) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:197:2: IDENTIFIER l= COLON
                    {
                    IDENTIFIER41=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csConstraintAction1118); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER41);

                    l=(Token)match(input,COLON,FOLLOW_COLON_in_csConstraintAction1122); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(l);


                    }
                    break;

            }

            pushFollow(FOLLOW_csConstraint_in_csConstraintAction1126);
            csConstraint42=csConstraint();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csConstraint.add(csConstraint42.getTree());
            ARROW43=(Token)match(input,ARROW,FOLLOW_ARROW_in_csConstraintAction1128); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ARROW.add(ARROW43);

            LBR44=(Token)match(input,LBR,FOLLOW_LBR_in_csConstraintAction1130); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR44);

            RBR45=(Token)match(input,RBR,FOLLOW_RBR_in_csConstraintAction1132); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR45);



            // AST REWRITE
            // elements: csConstraint, csConstraint, IDENTIFIER
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 198:3: -> {$l!=null}? ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
            if (l!=null) {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:198:18: ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                adaptor.addChild(root_1, stream_csConstraint.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR44).getPayload(Tree.class));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:200:19: ^( ConcCstOption ^( Cst_Label IDENTIFIER ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:200:35: ^( Cst_Label IDENTIFIER )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Label, "Cst_Label"), root_3);

                adaptor.addChild(root_3, stream_IDENTIFIER.nextNode());

                adaptor.addChild(root_2, root_3);
                }

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 202:3: -> ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ) )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:202:18: ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                adaptor.addChild(root_1, stream_csConstraint.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR44).getPayload(Tree.class));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:204:19: ^( ConcCstOption )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csConstraintAction"

    public static class csExtendedConstraintAction_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csExtendedConstraintAction"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:208:1: csExtendedConstraintAction : ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) ;
    public final miniTomParser.csExtendedConstraintAction_return csExtendedConstraintAction() throws RecognitionException {
        miniTomParser.csExtendedConstraintAction_return retval = new miniTomParser.csExtendedConstraintAction_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token l=null;
        Token IDENTIFIER46=null;
        Token ARROW48=null;
        Token LBR49=null;
        Token RBR50=null;
        miniTomParser.csExtendedConstraint_return csExtendedConstraint47 = null;


        Tree l_tree=null;
        Tree IDENTIFIER46_tree=null;
        Tree ARROW48_tree=null;
        Tree LBR49_tree=null;
        Tree RBR50_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csExtendedConstraint=new RewriteRuleSubtreeStream(adaptor,"rule csExtendedConstraint");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:208:28: ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:209:1: ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:209:1: ( IDENTIFIER l= COLON )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==IDENTIFIER) ) {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==COLON) ) {
                    alt13=1;
                }
            }
            switch (alt13) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:209:2: IDENTIFIER l= COLON
                    {
                    IDENTIFIER46=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csExtendedConstraintAction1305); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER46);

                    l=(Token)match(input,COLON,FOLLOW_COLON_in_csExtendedConstraintAction1309); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(l);


                    }
                    break;

            }

            pushFollow(FOLLOW_csExtendedConstraint_in_csExtendedConstraintAction1313);
            csExtendedConstraint47=csExtendedConstraint();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csExtendedConstraint.add(csExtendedConstraint47.getTree());
            ARROW48=(Token)match(input,ARROW,FOLLOW_ARROW_in_csExtendedConstraintAction1315); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ARROW.add(ARROW48);

            LBR49=(Token)match(input,LBR,FOLLOW_LBR_in_csExtendedConstraintAction1317); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR49);

            RBR50=(Token)match(input,RBR,FOLLOW_RBR_in_csExtendedConstraintAction1319); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR50);



            // AST REWRITE
            // elements: IDENTIFIER, csExtendedConstraint, csExtendedConstraint
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 210:3: -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
            if (l!=null) {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:210:18: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR49).getPayload(Tree.class));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:212:19: ^( ConcCstOption ^( Cst_Label IDENTIFIER ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:212:35: ^( Cst_Label IDENTIFIER )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Label, "Cst_Label"), root_3);

                adaptor.addChild(root_3, stream_IDENTIFIER.nextNode());

                adaptor.addChild(root_2, root_3);
                }

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 214:3: -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:214:18: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR49).getPayload(Tree.class));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:216:19: ^( ConcCstOption )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csExtendedConstraintAction"

    public static class csBQTerm_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csBQTerm"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:220:1: csBQTerm[ boolean typedAppl ] : ( (type= IDENTIFIER )? ( BQUOTE )? bqname= IDENTIFIER LPAR (a+= csBQTerm[false] ( COMMA a+= csBQTerm[false] )* )? RPAR -> {$typedAppl && type==null}? ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) ^( Cst_TypeUnknown ) -> {$typedAppl && type!=null}? ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) ^( Cst_Type $type) -> ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) | (type= IDENTIFIER )? ( BQUOTE )? name= IDENTIFIER (s= STAR )? -> {s!=null && type!=null && $typedAppl}? ^( Cst_BQVarStar $name ^( Cst_Type $type) ) ^( Cst_Type $type) -> {s!=null && type!=null && !$typedAppl}? ^( Cst_BQVarStar $name ^( Cst_Type $type) ) -> {s!=null && type==null && $typedAppl}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) ) ^( Cst_TypeUnknown ) -> {s!=null && type==null && !$typedAppl}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) ) -> {s==null && type!=null && $typedAppl}? ^( Cst_BQVar $name ^( Cst_Type $type) ) ^( Cst_Type $type) -> {s==null && type!=null && !$typedAppl}? ^( Cst_BQVar $name ^( Cst_Type $type) ) -> {s==null && type==null && $typedAppl}? ^( Cst_BQVar $name ^( Cst_TypeUnknown ) ) ^( Cst_TypeUnknown ) -> ^( Cst_BQVar $name ^( Cst_TypeUnknown ) ) | csConstantValue -> ^( Cst_BQConstant csConstantValue ) );
    public final miniTomParser.csBQTerm_return csBQTerm(boolean typedAppl) throws RecognitionException {
        miniTomParser.csBQTerm_return retval = new miniTomParser.csBQTerm_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token type=null;
        Token bqname=null;
        Token name=null;
        Token s=null;
        Token BQUOTE51=null;
        Token LPAR52=null;
        Token COMMA53=null;
        Token RPAR54=null;
        Token BQUOTE55=null;
        List list_a=null;
        miniTomParser.csConstantValue_return csConstantValue56 = null;

        RuleReturnScope a = null;
        Tree type_tree=null;
        Tree bqname_tree=null;
        Tree name_tree=null;
        Tree s_tree=null;
        Tree BQUOTE51_tree=null;
        Tree LPAR52_tree=null;
        Tree COMMA53_tree=null;
        Tree RPAR54_tree=null;
        Tree BQUOTE55_tree=null;
        RewriteRuleTokenStream stream_BQUOTE=new RewriteRuleTokenStream(adaptor,"token BQUOTE");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleSubtreeStream stream_csBQTerm=new RewriteRuleSubtreeStream(adaptor,"rule csBQTerm");
        RewriteRuleSubtreeStream stream_csConstantValue=new RewriteRuleSubtreeStream(adaptor,"rule csConstantValue");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:220:32: ( (type= IDENTIFIER )? ( BQUOTE )? bqname= IDENTIFIER LPAR (a+= csBQTerm[false] ( COMMA a+= csBQTerm[false] )* )? RPAR -> {$typedAppl && type==null}? ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) ^( Cst_TypeUnknown ) -> {$typedAppl && type!=null}? ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) ^( Cst_Type $type) -> ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) | (type= IDENTIFIER )? ( BQUOTE )? name= IDENTIFIER (s= STAR )? -> {s!=null && type!=null && $typedAppl}? ^( Cst_BQVarStar $name ^( Cst_Type $type) ) ^( Cst_Type $type) -> {s!=null && type!=null && !$typedAppl}? ^( Cst_BQVarStar $name ^( Cst_Type $type) ) -> {s!=null && type==null && $typedAppl}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) ) ^( Cst_TypeUnknown ) -> {s!=null && type==null && !$typedAppl}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) ) -> {s==null && type!=null && $typedAppl}? ^( Cst_BQVar $name ^( Cst_Type $type) ) ^( Cst_Type $type) -> {s==null && type!=null && !$typedAppl}? ^( Cst_BQVar $name ^( Cst_Type $type) ) -> {s==null && type==null && $typedAppl}? ^( Cst_BQVar $name ^( Cst_TypeUnknown ) ) ^( Cst_TypeUnknown ) -> ^( Cst_BQVar $name ^( Cst_TypeUnknown ) ) | csConstantValue -> ^( Cst_BQConstant csConstantValue ) )
            int alt21=3;
            switch ( input.LA(1) ) {
            case IDENTIFIER:
                {
                int LA21_1 = input.LA(2);

                if ( (synpred18_miniTom()) ) {
                    alt21=1;
                }
                else if ( (synpred22_miniTom()) ) {
                    alt21=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;
                }
                }
                break;
            case BQUOTE:
                {
                int LA21_2 = input.LA(2);

                if ( (synpred18_miniTom()) ) {
                    alt21=1;
                }
                else if ( (synpred22_miniTom()) ) {
                    alt21=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 2, input);

                    throw nvae;
                }
                }
                break;
            case INTEGER:
            case LONG:
            case CHAR:
            case DOUBLE:
            case STRING:
                {
                alt21=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:3: (type= IDENTIFIER )? ( BQUOTE )? bqname= IDENTIFIER LPAR (a+= csBQTerm[false] ( COMMA a+= csBQTerm[false] )* )? RPAR
                    {
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:3: (type= IDENTIFIER )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==IDENTIFIER) ) {
                        int LA14_1 = input.LA(2);

                        if ( (LA14_1==IDENTIFIER||LA14_1==BQUOTE) ) {
                            alt14=1;
                        }
                    }
                    switch (alt14) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:4: type= IDENTIFIER
                            {
                            type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csBQTerm1498); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IDENTIFIER.add(type);


                            }
                            break;

                    }

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:22: ( BQUOTE )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==BQUOTE) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:0:0: BQUOTE
                            {
                            BQUOTE51=(Token)match(input,BQUOTE,FOLLOW_BQUOTE_in_csBQTerm1502); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_BQUOTE.add(BQUOTE51);


                            }
                            break;

                    }

                    bqname=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csBQTerm1507); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(bqname);

                    LPAR52=(Token)match(input,LPAR,FOLLOW_LPAR_in_csBQTerm1509); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR52);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:53: (a+= csBQTerm[false] ( COMMA a+= csBQTerm[false] )* )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==IDENTIFIER||LA17_0==BQUOTE||(LA17_0>=INTEGER && LA17_0<=STRING)) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:54: a+= csBQTerm[false] ( COMMA a+= csBQTerm[false] )*
                            {
                            pushFollow(FOLLOW_csBQTerm_in_csBQTerm1514);
                            a=csBQTerm(false);

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_csBQTerm.add(a.getTree());
                            if (list_a==null) list_a=new ArrayList();
                            list_a.add(a.getTree());

                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:73: ( COMMA a+= csBQTerm[false] )*
                            loop16:
                            do {
                                int alt16=2;
                                int LA16_0 = input.LA(1);

                                if ( (LA16_0==COMMA) ) {
                                    alt16=1;
                                }


                                switch (alt16) {
                            	case 1 :
                            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:74: COMMA a+= csBQTerm[false]
                            	    {
                            	    COMMA53=(Token)match(input,COMMA,FOLLOW_COMMA_in_csBQTerm1518); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA53);

                            	    pushFollow(FOLLOW_csBQTerm_in_csBQTerm1522);
                            	    a=csBQTerm(false);

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_csBQTerm.add(a.getTree());
                            	    if (list_a==null) list_a=new ArrayList();
                            	    list_a.add(a.getTree());


                            	    }
                            	    break;

                            	default :
                            	    break loop16;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR54=(Token)match(input,RPAR,FOLLOW_RPAR_in_csBQTerm1529); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR54);



                    // AST REWRITE
                    // elements: bqname, bqname, a, a, type, bqname, a
                    // token labels: type, bqname
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: a
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
                    RewriteRuleTokenStream stream_bqname=new RewriteRuleTokenStream(adaptor,"token bqname",bqname);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"token a",list_a);
                    root_0 = (Tree)adaptor.nil();
                    // 222:4: -> {$typedAppl && type==null}? ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) ^( Cst_TypeUnknown )
                    if (typedAppl && type==null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:222:35: ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQAppl, "Cst_BQAppl"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)LPAR52, (CommonToken)RPAR54));
                        adaptor.addChild(root_1, stream_bqname.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:223:16: ^( ConcCstBQTerm ( $a)* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:223:32: ( $a)*
                        while ( stream_a.hasNext() ) {
                            adaptor.addChild(root_2, stream_a.nextTree());

                        }
                        stream_a.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:223:38: ^( Cst_TypeUnknown )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 224:4: -> {$typedAppl && type!=null}? ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) ^( Cst_Type $type)
                    if (typedAppl && type!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:224:35: ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQAppl, "Cst_BQAppl"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)LPAR52, (CommonToken)RPAR54));
                        adaptor.addChild(root_1, stream_bqname.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:225:16: ^( ConcCstBQTerm ( $a)* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:225:32: ( $a)*
                        while ( stream_a.hasNext() ) {
                            adaptor.addChild(root_2, stream_a.nextTree());

                        }
                        stream_a.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:225:38: ^( Cst_Type $type)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_1);

                        adaptor.addChild(root_1, stream_type.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 226:4: -> ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:226:35: ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQAppl, "Cst_BQAppl"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)LPAR52, (CommonToken)RPAR54));
                        adaptor.addChild(root_1, stream_bqname.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:227:16: ^( ConcCstBQTerm ( $a)* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:227:32: ( $a)*
                        while ( stream_a.hasNext() ) {
                            adaptor.addChild(root_2, stream_a.nextTree());

                        }
                        stream_a.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:4: (type= IDENTIFIER )? ( BQUOTE )? name= IDENTIFIER (s= STAR )?
                    {
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:4: (type= IDENTIFIER )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==IDENTIFIER) ) {
                        int LA18_1 = input.LA(2);

                        if ( (LA18_1==BQUOTE) ) {
                            alt18=1;
                        }
                        else if ( (LA18_1==IDENTIFIER) ) {
                            int LA18_4 = input.LA(3);

                            if ( (synpred19_miniTom()) ) {
                                alt18=1;
                            }
                        }
                    }
                    switch (alt18) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:5: type= IDENTIFIER
                            {
                            type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csBQTerm1670); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IDENTIFIER.add(type);


                            }
                            break;

                    }

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:23: ( BQUOTE )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==BQUOTE) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:0:0: BQUOTE
                            {
                            BQUOTE55=(Token)match(input,BQUOTE,FOLLOW_BQUOTE_in_csBQTerm1674); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_BQUOTE.add(BQUOTE55);


                            }
                            break;

                    }

                    name=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csBQTerm1679); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(name);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:47: (s= STAR )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==STAR) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:48: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csBQTerm1684); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(s);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: name, type, type, name, name, name, name, type, name, type, name, type, name, type
                    // token labels: name, type
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
                    RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 230:4: -> {s!=null && type!=null && $typedAppl}? ^( Cst_BQVarStar $name ^( Cst_Type $type) ) ^( Cst_Type $type)
                    if (s!=null && type!=null && typedAppl) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:230:45: ^( Cst_BQVarStar $name ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVarStar, "Cst_BQVarStar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:230:104: ^( Cst_Type $type)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                        adaptor.addChild(root_2, stream_type.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:230:123: ^( Cst_Type $type)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_1);

                        adaptor.addChild(root_1, stream_type.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 231:4: -> {s!=null && type!=null && !$typedAppl}? ^( Cst_BQVarStar $name ^( Cst_Type $type) )
                    if (s!=null && type!=null && !typedAppl) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:231:46: ^( Cst_BQVarStar $name ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVarStar, "Cst_BQVarStar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:231:105: ^( Cst_Type $type)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                        adaptor.addChild(root_2, stream_type.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 233:4: -> {s!=null && type==null && $typedAppl}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) ) ^( Cst_TypeUnknown )
                    if (s!=null && type==null && typedAppl) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:233:45: ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVarStar, "Cst_BQVarStar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:233:104: ^( Cst_TypeUnknown )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:233:125: ^( Cst_TypeUnknown )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 234:4: -> {s!=null && type==null && !$typedAppl}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) )
                    if (s!=null && type==null && !typedAppl) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:234:46: ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVarStar, "Cst_BQVarStar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:234:105: ^( Cst_TypeUnknown )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 236:4: -> {s==null && type!=null && $typedAppl}? ^( Cst_BQVar $name ^( Cst_Type $type) ) ^( Cst_Type $type)
                    if (s==null && type!=null && typedAppl) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:236:45: ^( Cst_BQVar $name ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVar, "Cst_BQVar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:236:100: ^( Cst_Type $type)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                        adaptor.addChild(root_2, stream_type.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:236:119: ^( Cst_Type $type)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_1);

                        adaptor.addChild(root_1, stream_type.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 237:4: -> {s==null && type!=null && !$typedAppl}? ^( Cst_BQVar $name ^( Cst_Type $type) )
                    if (s==null && type!=null && !typedAppl) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:237:46: ^( Cst_BQVar $name ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVar, "Cst_BQVar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:237:101: ^( Cst_Type $type)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                        adaptor.addChild(root_2, stream_type.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 239:4: -> {s==null && type==null && $typedAppl}? ^( Cst_BQVar $name ^( Cst_TypeUnknown ) ) ^( Cst_TypeUnknown )
                    if (s==null && type==null && typedAppl) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:239:45: ^( Cst_BQVar $name ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVar, "Cst_BQVar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:239:100: ^( Cst_TypeUnknown )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:239:121: ^( Cst_TypeUnknown )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 240:4: -> ^( Cst_BQVar $name ^( Cst_TypeUnknown ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:240:45: ^( Cst_BQVar $name ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVar, "Cst_BQVar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:240:100: ^( Cst_TypeUnknown )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:242:5: csConstantValue
                    {
                    pushFollow(FOLLOW_csConstantValue_in_csBQTerm1934);
                    csConstantValue56=csConstantValue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csConstantValue.add(csConstantValue56.getTree());


                    // AST REWRITE
                    // elements: csConstantValue
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 242:21: -> ^( Cst_BQConstant csConstantValue )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:242:24: ^( Cst_BQConstant csConstantValue )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQConstant, "Cst_BQConstant"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)(csConstantValue56!=null?((Token)csConstantValue56.start):null), (CommonToken)(csConstantValue56!=null?((Token)csConstantValue56.stop):null)));
                        adaptor.addChild(root_1, stream_csConstantValue.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csBQTerm"

    public static class csExtendedConstraint_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csExtendedConstraint"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:247:1: csExtendedConstraint : csMatchArgumentConstraintList ( (a= AND | o= OR ) csConstraint )? -> {a!=null}? ^( Cst_AndConstraint csMatchArgumentConstraintList csConstraint ) -> {o!=null}? ^( Cst_OrConstraint csMatchArgumentConstraintList csConstraint ) -> csMatchArgumentConstraintList ;
    public final miniTomParser.csExtendedConstraint_return csExtendedConstraint() throws RecognitionException {
        miniTomParser.csExtendedConstraint_return retval = new miniTomParser.csExtendedConstraint_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token a=null;
        Token o=null;
        miniTomParser.csMatchArgumentConstraintList_return csMatchArgumentConstraintList57 = null;

        miniTomParser.csConstraint_return csConstraint58 = null;


        Tree a_tree=null;
        Tree o_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
        RewriteRuleSubtreeStream stream_csMatchArgumentConstraintList=new RewriteRuleSubtreeStream(adaptor,"rule csMatchArgumentConstraintList");
        RewriteRuleSubtreeStream stream_csConstraint=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:255:22: ( csMatchArgumentConstraintList ( (a= AND | o= OR ) csConstraint )? -> {a!=null}? ^( Cst_AndConstraint csMatchArgumentConstraintList csConstraint ) -> {o!=null}? ^( Cst_OrConstraint csMatchArgumentConstraintList csConstraint ) -> csMatchArgumentConstraintList )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:256:2: csMatchArgumentConstraintList ( (a= AND | o= OR ) csConstraint )?
            {
            pushFollow(FOLLOW_csMatchArgumentConstraintList_in_csExtendedConstraint1965);
            csMatchArgumentConstraintList57=csMatchArgumentConstraintList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csMatchArgumentConstraintList.add(csMatchArgumentConstraintList57.getTree());
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:256:32: ( (a= AND | o= OR ) csConstraint )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( ((LA23_0>=AND && LA23_0<=OR)) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:256:33: (a= AND | o= OR ) csConstraint
                    {
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:256:33: (a= AND | o= OR )
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==AND) ) {
                        alt22=1;
                    }
                    else if ( (LA22_0==OR) ) {
                        alt22=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 22, 0, input);

                        throw nvae;
                    }
                    switch (alt22) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:256:34: a= AND
                            {
                            a=(Token)match(input,AND,FOLLOW_AND_in_csExtendedConstraint1971); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_AND.add(a);


                            }
                            break;
                        case 2 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:256:40: o= OR
                            {
                            o=(Token)match(input,OR,FOLLOW_OR_in_csExtendedConstraint1975); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_OR.add(o);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_csConstraint_in_csExtendedConstraint1978);
                    csConstraint58=csConstraint();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csConstraint.add(csConstraint58.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: csMatchArgumentConstraintList, csConstraint, csMatchArgumentConstraintList, csConstraint, csMatchArgumentConstraintList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 257:2: -> {a!=null}? ^( Cst_AndConstraint csMatchArgumentConstraintList csConstraint )
            if (a!=null) {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:257:15: ^( Cst_AndConstraint csMatchArgumentConstraintList csConstraint )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_AndConstraint, "Cst_AndConstraint"), root_1);

                adaptor.addChild(root_1, stream_csMatchArgumentConstraintList.nextTree());
                adaptor.addChild(root_1, stream_csConstraint.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 258:2: -> {o!=null}? ^( Cst_OrConstraint csMatchArgumentConstraintList csConstraint )
            if (o!=null) {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:258:15: ^( Cst_OrConstraint csMatchArgumentConstraintList csConstraint )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OrConstraint, "Cst_OrConstraint"), root_1);

                adaptor.addChild(root_1, stream_csMatchArgumentConstraintList.nextTree());
                adaptor.addChild(root_1, stream_csConstraint.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 259:2: -> csMatchArgumentConstraintList
            {
                adaptor.addChild(root_0, stream_csMatchArgumentConstraintList.nextTree());

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csExtendedConstraint"

    public static class csMatchArgumentConstraintList_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csMatchArgumentConstraintList"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:262:1: csMatchArgumentConstraintList : csMatchArgumentConstraint ( COMMA c2= csMatchArgumentConstraint )* ( COMMA )? -> {c2!=null}? ^( Cst_AndConstraint ( csMatchArgumentConstraint )* ) -> csMatchArgumentConstraint ;
    public final miniTomParser.csMatchArgumentConstraintList_return csMatchArgumentConstraintList() throws RecognitionException {
        miniTomParser.csMatchArgumentConstraintList_return retval = new miniTomParser.csMatchArgumentConstraintList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token COMMA60=null;
        Token COMMA61=null;
        miniTomParser.csMatchArgumentConstraint_return c2 = null;

        miniTomParser.csMatchArgumentConstraint_return csMatchArgumentConstraint59 = null;


        Tree COMMA60_tree=null;
        Tree COMMA61_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_csMatchArgumentConstraint=new RewriteRuleSubtreeStream(adaptor,"rule csMatchArgumentConstraint");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:262:31: ( csMatchArgumentConstraint ( COMMA c2= csMatchArgumentConstraint )* ( COMMA )? -> {c2!=null}? ^( Cst_AndConstraint ( csMatchArgumentConstraint )* ) -> csMatchArgumentConstraint )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:263:3: csMatchArgumentConstraint ( COMMA c2= csMatchArgumentConstraint )* ( COMMA )?
            {
            pushFollow(FOLLOW_csMatchArgumentConstraint_in_csMatchArgumentConstraintList2031);
            csMatchArgumentConstraint59=csMatchArgumentConstraint();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csMatchArgumentConstraint.add(csMatchArgumentConstraint59.getTree());
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:263:29: ( COMMA c2= csMatchArgumentConstraint )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==COMMA) ) {
                    int LA24_1 = input.LA(2);

                    if ( (LA24_1==IDENTIFIER||LA24_1==LPAR||(LA24_1>=ANTI && LA24_1<=UNDERSCORE)||(LA24_1>=INTEGER && LA24_1<=STRING)) ) {
                        alt24=1;
                    }


                }


                switch (alt24) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:263:30: COMMA c2= csMatchArgumentConstraint
            	    {
            	    COMMA60=(Token)match(input,COMMA,FOLLOW_COMMA_in_csMatchArgumentConstraintList2034); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA60);

            	    pushFollow(FOLLOW_csMatchArgumentConstraint_in_csMatchArgumentConstraintList2038);
            	    c2=csMatchArgumentConstraint();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csMatchArgumentConstraint.add(c2.getTree());

            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:263:67: ( COMMA )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==COMMA) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:263:68: COMMA
                    {
                    COMMA61=(Token)match(input,COMMA,FOLLOW_COMMA_in_csMatchArgumentConstraintList2043); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COMMA.add(COMMA61);


                    }
                    break;

            }



            // AST REWRITE
            // elements: csMatchArgumentConstraint, csMatchArgumentConstraint
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 264:3: -> {c2!=null}? ^( Cst_AndConstraint ( csMatchArgumentConstraint )* )
            if (c2!=null) {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:264:17: ^( Cst_AndConstraint ( csMatchArgumentConstraint )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_AndConstraint, "Cst_AndConstraint"), root_1);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:264:37: ( csMatchArgumentConstraint )*
                while ( stream_csMatchArgumentConstraint.hasNext() ) {
                    adaptor.addChild(root_1, stream_csMatchArgumentConstraint.nextTree());

                }
                stream_csMatchArgumentConstraint.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 265:3: -> csMatchArgumentConstraint
            {
                adaptor.addChild(root_0, stream_csMatchArgumentConstraint.nextTree());

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csMatchArgumentConstraintList"

    public static class csMatchArgumentConstraint_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csMatchArgumentConstraint"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:268:1: csMatchArgumentConstraint : csPattern -> ^( Cst_MatchArgumentConstraint csPattern ) ;
    public final miniTomParser.csMatchArgumentConstraint_return csMatchArgumentConstraint() throws RecognitionException {
        miniTomParser.csMatchArgumentConstraint_return retval = new miniTomParser.csMatchArgumentConstraint_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        miniTomParser.csPattern_return csPattern62 = null;


        RewriteRuleSubtreeStream stream_csPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPattern");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:268:27: ( csPattern -> ^( Cst_MatchArgumentConstraint csPattern ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:269:3: csPattern
            {
            pushFollow(FOLLOW_csPattern_in_csMatchArgumentConstraint2085);
            csPattern62=csPattern();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csPattern.add(csPattern62.getTree());


            // AST REWRITE
            // elements: csPattern
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 270:3: -> ^( Cst_MatchArgumentConstraint csPattern )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:270:6: ^( Cst_MatchArgumentConstraint csPattern )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MatchArgumentConstraint, "Cst_MatchArgumentConstraint"), root_1);

                adaptor.addChild(root_1, stream_csPattern.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csMatchArgumentConstraint"

    public static class csConstraint_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csConstraint"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:273:1: csConstraint : csConstraint_priority1 ;
    public final miniTomParser.csConstraint_return csConstraint() throws RecognitionException {
        miniTomParser.csConstraint_return retval = new miniTomParser.csConstraint_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        miniTomParser.csConstraint_priority1_return csConstraint_priority163 = null;



        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:285:14: ( csConstraint_priority1 )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:286:2: csConstraint_priority1
            {
            root_0 = (Tree)adaptor.nil();

            pushFollow(FOLLOW_csConstraint_priority1_in_csConstraint2108);
            csConstraint_priority163=csConstraint_priority1();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, csConstraint_priority163.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csConstraint"

    public static class csConstraint_priority1_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csConstraint_priority1"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:289:1: csConstraint_priority1 : csConstraint_priority2 (or= OR csConstraint_priority2 )* -> {or!=null}? ^( Cst_OrConstraint ( csConstraint_priority2 )* ) -> csConstraint_priority2 ;
    public final miniTomParser.csConstraint_priority1_return csConstraint_priority1() throws RecognitionException {
        miniTomParser.csConstraint_priority1_return retval = new miniTomParser.csConstraint_priority1_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token or=null;
        miniTomParser.csConstraint_priority2_return csConstraint_priority264 = null;

        miniTomParser.csConstraint_priority2_return csConstraint_priority265 = null;


        Tree or_tree=null;
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
        RewriteRuleSubtreeStream stream_csConstraint_priority2=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint_priority2");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:289:24: ( csConstraint_priority2 (or= OR csConstraint_priority2 )* -> {or!=null}? ^( Cst_OrConstraint ( csConstraint_priority2 )* ) -> csConstraint_priority2 )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:290:3: csConstraint_priority2 (or= OR csConstraint_priority2 )*
            {
            pushFollow(FOLLOW_csConstraint_priority2_in_csConstraint_priority12119);
            csConstraint_priority264=csConstraint_priority2();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csConstraint_priority2.add(csConstraint_priority264.getTree());
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:290:26: (or= OR csConstraint_priority2 )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==OR) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:290:27: or= OR csConstraint_priority2
            	    {
            	    or=(Token)match(input,OR,FOLLOW_OR_in_csConstraint_priority12124); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_OR.add(or);

            	    pushFollow(FOLLOW_csConstraint_priority2_in_csConstraint_priority12126);
            	    csConstraint_priority265=csConstraint_priority2();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csConstraint_priority2.add(csConstraint_priority265.getTree());

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);



            // AST REWRITE
            // elements: csConstraint_priority2, csConstraint_priority2
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 291:3: -> {or!=null}? ^( Cst_OrConstraint ( csConstraint_priority2 )* )
            if (or!=null) {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:291:17: ^( Cst_OrConstraint ( csConstraint_priority2 )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OrConstraint, "Cst_OrConstraint"), root_1);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:291:36: ( csConstraint_priority2 )*
                while ( stream_csConstraint_priority2.hasNext() ) {
                    adaptor.addChild(root_1, stream_csConstraint_priority2.nextTree());

                }
                stream_csConstraint_priority2.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 292:3: -> csConstraint_priority2
            {
                adaptor.addChild(root_0, stream_csConstraint_priority2.nextTree());

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csConstraint_priority1"

    public static class csConstraint_priority2_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csConstraint_priority2"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:295:1: csConstraint_priority2 : csConstraint_priority3 (and= AND csConstraint_priority3 )* -> {and!=null}? ^( Cst_AndConstraint ( csConstraint_priority3 )* ) -> csConstraint_priority3 ;
    public final miniTomParser.csConstraint_priority2_return csConstraint_priority2() throws RecognitionException {
        miniTomParser.csConstraint_priority2_return retval = new miniTomParser.csConstraint_priority2_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token and=null;
        miniTomParser.csConstraint_priority3_return csConstraint_priority366 = null;

        miniTomParser.csConstraint_priority3_return csConstraint_priority367 = null;


        Tree and_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_csConstraint_priority3=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint_priority3");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:295:24: ( csConstraint_priority3 (and= AND csConstraint_priority3 )* -> {and!=null}? ^( Cst_AndConstraint ( csConstraint_priority3 )* ) -> csConstraint_priority3 )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:296:3: csConstraint_priority3 (and= AND csConstraint_priority3 )*
            {
            pushFollow(FOLLOW_csConstraint_priority3_in_csConstraint_priority22157);
            csConstraint_priority366=csConstraint_priority3();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csConstraint_priority3.add(csConstraint_priority366.getTree());
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:296:26: (and= AND csConstraint_priority3 )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==AND) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:296:27: and= AND csConstraint_priority3
            	    {
            	    and=(Token)match(input,AND,FOLLOW_AND_in_csConstraint_priority22162); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_AND.add(and);

            	    pushFollow(FOLLOW_csConstraint_priority3_in_csConstraint_priority22164);
            	    csConstraint_priority367=csConstraint_priority3();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csConstraint_priority3.add(csConstraint_priority367.getTree());

            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);



            // AST REWRITE
            // elements: csConstraint_priority3, csConstraint_priority3
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 297:3: -> {and!=null}? ^( Cst_AndConstraint ( csConstraint_priority3 )* )
            if (and!=null) {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:297:18: ^( Cst_AndConstraint ( csConstraint_priority3 )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_AndConstraint, "Cst_AndConstraint"), root_1);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:297:38: ( csConstraint_priority3 )*
                while ( stream_csConstraint_priority3.hasNext() ) {
                    adaptor.addChild(root_1, stream_csConstraint_priority3.nextTree());

                }
                stream_csConstraint_priority3.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 298:3: -> csConstraint_priority3
            {
                adaptor.addChild(root_0, stream_csConstraint_priority3.nextTree());

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csConstraint_priority2"

    public static class csConstraint_priority3_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csConstraint_priority3"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:301:1: csConstraint_priority3 : ( csPattern LARROW csBQTerm[true] -> ^( Cst_MatchTermConstraint csPattern csBQTerm ) | l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm -> {gt!=null}? ^( Cst_NumGreaterThan $l $r) -> {ge!=null}? ^( Cst_NumGreaterOrEqualTo $l $r) -> {lt!=null}? ^( Cst_NumLessThan $l $r) -> {le!=null}? ^( Cst_NumLessOrEqualTo $l $r) -> {eq!=null}? ^( Cst_NumEqualTo $l $r) -> ^( Cst_NumDifferent $l $r) | LPAR csConstraint RPAR -> csConstraint );
    public final miniTomParser.csConstraint_priority3_return csConstraint_priority3() throws RecognitionException {
        miniTomParser.csConstraint_priority3_return retval = new miniTomParser.csConstraint_priority3_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token gt=null;
        Token ge=null;
        Token lt=null;
        Token le=null;
        Token eq=null;
        Token ne=null;
        Token LARROW69=null;
        Token LPAR71=null;
        Token RPAR73=null;
        miniTomParser.csTerm_return l = null;

        miniTomParser.csTerm_return r = null;

        miniTomParser.csPattern_return csPattern68 = null;

        miniTomParser.csBQTerm_return csBQTerm70 = null;

        miniTomParser.csConstraint_return csConstraint72 = null;


        Tree gt_tree=null;
        Tree ge_tree=null;
        Tree lt_tree=null;
        Tree le_tree=null;
        Tree eq_tree=null;
        Tree ne_tree=null;
        Tree LARROW69_tree=null;
        Tree LPAR71_tree=null;
        Tree RPAR73_tree=null;
        RewriteRuleTokenStream stream_DIFFERENT=new RewriteRuleTokenStream(adaptor,"token DIFFERENT");
        RewriteRuleTokenStream stream_LARROW=new RewriteRuleTokenStream(adaptor,"token LARROW");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_DOUBLEEQUAL=new RewriteRuleTokenStream(adaptor,"token DOUBLEEQUAL");
        RewriteRuleTokenStream stream_LOWERTHAN=new RewriteRuleTokenStream(adaptor,"token LOWERTHAN");
        RewriteRuleTokenStream stream_GREATEROREQU=new RewriteRuleTokenStream(adaptor,"token GREATEROREQU");
        RewriteRuleTokenStream stream_GREATERTHAN=new RewriteRuleTokenStream(adaptor,"token GREATERTHAN");
        RewriteRuleTokenStream stream_LOWEROREQU=new RewriteRuleTokenStream(adaptor,"token LOWEROREQU");
        RewriteRuleSubtreeStream stream_csBQTerm=new RewriteRuleSubtreeStream(adaptor,"rule csBQTerm");
        RewriteRuleSubtreeStream stream_csTerm=new RewriteRuleSubtreeStream(adaptor,"rule csTerm");
        RewriteRuleSubtreeStream stream_csConstraint=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint");
        RewriteRuleSubtreeStream stream_csPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPattern");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:301:24: ( csPattern LARROW csBQTerm[true] -> ^( Cst_MatchTermConstraint csPattern csBQTerm ) | l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm -> {gt!=null}? ^( Cst_NumGreaterThan $l $r) -> {ge!=null}? ^( Cst_NumGreaterOrEqualTo $l $r) -> {lt!=null}? ^( Cst_NumLessThan $l $r) -> {le!=null}? ^( Cst_NumLessOrEqualTo $l $r) -> {eq!=null}? ^( Cst_NumEqualTo $l $r) -> ^( Cst_NumDifferent $l $r) | LPAR csConstraint RPAR -> csConstraint )
            int alt29=3;
            alt29 = dfa29.predict(input);
            switch (alt29) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:303:3: csPattern LARROW csBQTerm[true]
                    {
                    pushFollow(FOLLOW_csPattern_in_csConstraint_priority32197);
                    csPattern68=csPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPattern.add(csPattern68.getTree());
                    LARROW69=(Token)match(input,LARROW,FOLLOW_LARROW_in_csConstraint_priority32199); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LARROW.add(LARROW69);

                    pushFollow(FOLLOW_csBQTerm_in_csConstraint_priority32201);
                    csBQTerm70=csBQTerm(true);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csBQTerm.add(csBQTerm70.getTree());


                    // AST REWRITE
                    // elements: csPattern, csBQTerm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 304:3: -> ^( Cst_MatchTermConstraint csPattern csBQTerm )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:304:6: ^( Cst_MatchTermConstraint csPattern csBQTerm )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MatchTermConstraint, "Cst_MatchTermConstraint"), root_1);

                        adaptor.addChild(root_1, stream_csPattern.nextTree());
                        adaptor.addChild(root_1, stream_csBQTerm.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:306:5: l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm
                    {
                    pushFollow(FOLLOW_csTerm_in_csConstraint_priority32224);
                    l=csTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csTerm.add(l.getTree());
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:307:5: (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT )
                    int alt28=6;
                    switch ( input.LA(1) ) {
                    case GREATERTHAN:
                        {
                        alt28=1;
                        }
                        break;
                    case GREATEROREQU:
                        {
                        alt28=2;
                        }
                        break;
                    case LOWERTHAN:
                        {
                        alt28=3;
                        }
                        break;
                    case LOWEROREQU:
                        {
                        alt28=4;
                        }
                        break;
                    case DOUBLEEQUAL:
                        {
                        alt28=5;
                        }
                        break;
                    case DIFFERENT:
                        {
                        alt28=6;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 28, 0, input);

                        throw nvae;
                    }

                    switch (alt28) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:307:6: gt= GREATERTHAN
                            {
                            gt=(Token)match(input,GREATERTHAN,FOLLOW_GREATERTHAN_in_csConstraint_priority32234); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_GREATERTHAN.add(gt);


                            }
                            break;
                        case 2 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:307:21: ge= GREATEROREQU
                            {
                            ge=(Token)match(input,GREATEROREQU,FOLLOW_GREATEROREQU_in_csConstraint_priority32238); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_GREATEROREQU.add(ge);


                            }
                            break;
                        case 3 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:307:37: lt= LOWERTHAN
                            {
                            lt=(Token)match(input,LOWERTHAN,FOLLOW_LOWERTHAN_in_csConstraint_priority32242); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_LOWERTHAN.add(lt);


                            }
                            break;
                        case 4 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:308:6: le= LOWEROREQU
                            {
                            le=(Token)match(input,LOWEROREQU,FOLLOW_LOWEROREQU_in_csConstraint_priority32251); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_LOWEROREQU.add(le);


                            }
                            break;
                        case 5 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:308:21: eq= DOUBLEEQUAL
                            {
                            eq=(Token)match(input,DOUBLEEQUAL,FOLLOW_DOUBLEEQUAL_in_csConstraint_priority32256); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DOUBLEEQUAL.add(eq);


                            }
                            break;
                        case 6 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:308:37: ne= DIFFERENT
                            {
                            ne=(Token)match(input,DIFFERENT,FOLLOW_DIFFERENT_in_csConstraint_priority32261); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DIFFERENT.add(ne);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_csTerm_in_csConstraint_priority32270);
                    r=csTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csTerm.add(r.getTree());


                    // AST REWRITE
                    // elements: l, r, l, l, l, l, r, r, r, l, r, r
                    // token labels: 
                    // rule labels: retval, r, l
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"rule r",r!=null?r.tree:null);
                    RewriteRuleSubtreeStream stream_l=new RewriteRuleSubtreeStream(adaptor,"rule l",l!=null?l.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 311:3: -> {gt!=null}? ^( Cst_NumGreaterThan $l $r)
                    if (gt!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:311:17: ^( Cst_NumGreaterThan $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumGreaterThan, "Cst_NumGreaterThan"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 312:3: -> {ge!=null}? ^( Cst_NumGreaterOrEqualTo $l $r)
                    if (ge!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:312:17: ^( Cst_NumGreaterOrEqualTo $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumGreaterOrEqualTo, "Cst_NumGreaterOrEqualTo"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 313:3: -> {lt!=null}? ^( Cst_NumLessThan $l $r)
                    if (lt!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:313:17: ^( Cst_NumLessThan $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumLessThan, "Cst_NumLessThan"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 314:3: -> {le!=null}? ^( Cst_NumLessOrEqualTo $l $r)
                    if (le!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:314:17: ^( Cst_NumLessOrEqualTo $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumLessOrEqualTo, "Cst_NumLessOrEqualTo"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 315:3: -> {eq!=null}? ^( Cst_NumEqualTo $l $r)
                    if (eq!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:315:17: ^( Cst_NumEqualTo $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumEqualTo, "Cst_NumEqualTo"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 316:3: -> ^( Cst_NumDifferent $l $r)
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:316:17: ^( Cst_NumDifferent $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumDifferent, "Cst_NumDifferent"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:318:5: LPAR csConstraint RPAR
                    {
                    LPAR71=(Token)match(input,LPAR,FOLLOW_LPAR_in_csConstraint_priority32400); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR71);

                    pushFollow(FOLLOW_csConstraint_in_csConstraint_priority32402);
                    csConstraint72=csConstraint();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csConstraint.add(csConstraint72.getTree());
                    RPAR73=(Token)match(input,RPAR,FOLLOW_RPAR_in_csConstraint_priority32404); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR73);



                    // AST REWRITE
                    // elements: csConstraint
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 319:3: -> csConstraint
                    {
                        adaptor.addChild(root_0, stream_csConstraint.nextTree());

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csConstraint_priority3"

    public static class csTerm_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csTerm"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:324:1: csTerm : ( IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_TermVariableStar IDENTIFIER ) -> ^( Cst_TermVariable IDENTIFIER ) | IDENTIFIER LPAR ( csTerm ( COMMA csTerm )* )? RPAR -> ^( Cst_TermAppl IDENTIFIER ^( ConcCstTerm ( csTerm )* ) ) );
    public final miniTomParser.csTerm_return csTerm() throws RecognitionException {
        miniTomParser.csTerm_return retval = new miniTomParser.csTerm_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token s=null;
        Token IDENTIFIER74=null;
        Token IDENTIFIER75=null;
        Token LPAR76=null;
        Token COMMA78=null;
        Token RPAR80=null;
        miniTomParser.csTerm_return csTerm77 = null;

        miniTomParser.csTerm_return csTerm79 = null;


        Tree s_tree=null;
        Tree IDENTIFIER74_tree=null;
        Tree IDENTIFIER75_tree=null;
        Tree LPAR76_tree=null;
        Tree COMMA78_tree=null;
        Tree RPAR80_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleSubtreeStream stream_csTerm=new RewriteRuleSubtreeStream(adaptor,"rule csTerm");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:324:8: ( IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_TermVariableStar IDENTIFIER ) -> ^( Cst_TermVariable IDENTIFIER ) | IDENTIFIER LPAR ( csTerm ( COMMA csTerm )* )? RPAR -> ^( Cst_TermAppl IDENTIFIER ^( ConcCstTerm ( csTerm )* ) ) )
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==IDENTIFIER) ) {
                int LA33_1 = input.LA(2);

                if ( (LA33_1==LPAR) ) {
                    alt33=2;
                }
                else if ( (LA33_1==EOF||LA33_1==RPAR||(LA33_1>=ARROW && LA33_1<=OR)||(LA33_1>=GREATERTHAN && LA33_1<=DIFFERENT)) ) {
                    alt33=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 33, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;
            }
            switch (alt33) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:325:3: IDENTIFIER (s= STAR )?
                    {
                    IDENTIFIER74=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csTerm2423); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER74);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:325:14: (s= STAR )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==STAR) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:325:15: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csTerm2428); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(s);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: IDENTIFIER, IDENTIFIER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 326:3: -> {s!=null}? ^( Cst_TermVariableStar IDENTIFIER )
                    if (s!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:326:16: ^( Cst_TermVariableStar IDENTIFIER )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TermVariableStar, "Cst_TermVariableStar"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 327:3: -> ^( Cst_TermVariable IDENTIFIER )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:327:16: ^( Cst_TermVariable IDENTIFIER )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TermVariable, "Cst_TermVariable"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:329:3: IDENTIFIER LPAR ( csTerm ( COMMA csTerm )* )? RPAR
                    {
                    IDENTIFIER75=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csTerm2468); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER75);

                    LPAR76=(Token)match(input,LPAR,FOLLOW_LPAR_in_csTerm2470); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR76);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:329:19: ( csTerm ( COMMA csTerm )* )?
                    int alt32=2;
                    int LA32_0 = input.LA(1);

                    if ( (LA32_0==IDENTIFIER) ) {
                        alt32=1;
                    }
                    switch (alt32) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:329:20: csTerm ( COMMA csTerm )*
                            {
                            pushFollow(FOLLOW_csTerm_in_csTerm2473);
                            csTerm77=csTerm();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_csTerm.add(csTerm77.getTree());
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:329:27: ( COMMA csTerm )*
                            loop31:
                            do {
                                int alt31=2;
                                int LA31_0 = input.LA(1);

                                if ( (LA31_0==COMMA) ) {
                                    alt31=1;
                                }


                                switch (alt31) {
                            	case 1 :
                            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:329:28: COMMA csTerm
                            	    {
                            	    COMMA78=(Token)match(input,COMMA,FOLLOW_COMMA_in_csTerm2476); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA78);

                            	    pushFollow(FOLLOW_csTerm_in_csTerm2478);
                            	    csTerm79=csTerm();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_csTerm.add(csTerm79.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop31;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR80=(Token)match(input,RPAR,FOLLOW_RPAR_in_csTerm2484); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR80);



                    // AST REWRITE
                    // elements: csTerm, IDENTIFIER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 330:3: -> ^( Cst_TermAppl IDENTIFIER ^( ConcCstTerm ( csTerm )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:330:6: ^( Cst_TermAppl IDENTIFIER ^( ConcCstTerm ( csTerm )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TermAppl, "Cst_TermAppl"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:330:32: ^( ConcCstTerm ( csTerm )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstTerm, "ConcCstTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:330:46: ( csTerm )*
                        while ( stream_csTerm.hasNext() ) {
                            adaptor.addChild(root_2, stream_csTerm.nextTree());

                        }
                        stream_csTerm.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csTerm"

    public static class csPattern_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csPattern"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:333:1: csPattern : ( IDENTIFIER AT csPattern -> ^( Cst_AnnotatedPattern csPattern IDENTIFIER ) | ANTI csPattern -> ^( Cst_Anti csPattern ) | csHeadSymbolList csExplicitTermList -> ^( Cst_Appl csHeadSymbolList csExplicitTermList ) | csHeadSymbolList csImplicitPairList -> ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList ) | IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_VariableStar IDENTIFIER ) -> ^( Cst_Variable IDENTIFIER ) | UNDERSCORE (s= STAR )? -> {s!=null}? ^( Cst_UnamedVariableStar ) -> ^( Cst_UnamedVariable ) | csConstantValue (s= STAR )? -> {s!=null}? ^( Cst_ConstantStar csConstantValue ) -> ^( Cst_Constant csConstantValue ) );
    public final miniTomParser.csPattern_return csPattern() throws RecognitionException {
        miniTomParser.csPattern_return retval = new miniTomParser.csPattern_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token s=null;
        Token IDENTIFIER81=null;
        Token AT82=null;
        Token ANTI84=null;
        Token IDENTIFIER90=null;
        Token UNDERSCORE91=null;
        miniTomParser.csPattern_return csPattern83 = null;

        miniTomParser.csPattern_return csPattern85 = null;

        miniTomParser.csHeadSymbolList_return csHeadSymbolList86 = null;

        miniTomParser.csExplicitTermList_return csExplicitTermList87 = null;

        miniTomParser.csHeadSymbolList_return csHeadSymbolList88 = null;

        miniTomParser.csImplicitPairList_return csImplicitPairList89 = null;

        miniTomParser.csConstantValue_return csConstantValue92 = null;


        Tree s_tree=null;
        Tree IDENTIFIER81_tree=null;
        Tree AT82_tree=null;
        Tree ANTI84_tree=null;
        Tree IDENTIFIER90_tree=null;
        Tree UNDERSCORE91_tree=null;
        RewriteRuleTokenStream stream_AT=new RewriteRuleTokenStream(adaptor,"token AT");
        RewriteRuleTokenStream stream_UNDERSCORE=new RewriteRuleTokenStream(adaptor,"token UNDERSCORE");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ANTI=new RewriteRuleTokenStream(adaptor,"token ANTI");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleSubtreeStream stream_csImplicitPairList=new RewriteRuleSubtreeStream(adaptor,"rule csImplicitPairList");
        RewriteRuleSubtreeStream stream_csExplicitTermList=new RewriteRuleSubtreeStream(adaptor,"rule csExplicitTermList");
        RewriteRuleSubtreeStream stream_csHeadSymbolList=new RewriteRuleSubtreeStream(adaptor,"rule csHeadSymbolList");
        RewriteRuleSubtreeStream stream_csPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPattern");
        RewriteRuleSubtreeStream stream_csConstantValue=new RewriteRuleSubtreeStream(adaptor,"rule csConstantValue");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:333:11: ( IDENTIFIER AT csPattern -> ^( Cst_AnnotatedPattern csPattern IDENTIFIER ) | ANTI csPattern -> ^( Cst_Anti csPattern ) | csHeadSymbolList csExplicitTermList -> ^( Cst_Appl csHeadSymbolList csExplicitTermList ) | csHeadSymbolList csImplicitPairList -> ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList ) | IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_VariableStar IDENTIFIER ) -> ^( Cst_Variable IDENTIFIER ) | UNDERSCORE (s= STAR )? -> {s!=null}? ^( Cst_UnamedVariableStar ) -> ^( Cst_UnamedVariable ) | csConstantValue (s= STAR )? -> {s!=null}? ^( Cst_ConstantStar csConstantValue ) -> ^( Cst_Constant csConstantValue ) )
            int alt37=7;
            alt37 = dfa37.predict(input);
            switch (alt37) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:335:3: IDENTIFIER AT csPattern
                    {
                    IDENTIFIER81=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csPattern2515); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER81);

                    AT82=(Token)match(input,AT,FOLLOW_AT_in_csPattern2517); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AT.add(AT82);

                    pushFollow(FOLLOW_csPattern_in_csPattern2519);
                    csPattern83=csPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPattern.add(csPattern83.getTree());


                    // AST REWRITE
                    // elements: csPattern, IDENTIFIER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 336:3: -> ^( Cst_AnnotatedPattern csPattern IDENTIFIER )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:336:6: ^( Cst_AnnotatedPattern csPattern IDENTIFIER )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_AnnotatedPattern, "Cst_AnnotatedPattern"), root_1);

                        adaptor.addChild(root_1, stream_csPattern.nextTree());
                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:339:3: ANTI csPattern
                    {
                    ANTI84=(Token)match(input,ANTI,FOLLOW_ANTI_in_csPattern2539); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ANTI.add(ANTI84);

                    pushFollow(FOLLOW_csPattern_in_csPattern2541);
                    csPattern85=csPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPattern.add(csPattern85.getTree());


                    // AST REWRITE
                    // elements: csPattern
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 340:3: -> ^( Cst_Anti csPattern )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:340:6: ^( Cst_Anti csPattern )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Anti, "Cst_Anti"), root_1);

                        adaptor.addChild(root_1, stream_csPattern.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:343:3: csHeadSymbolList csExplicitTermList
                    {
                    pushFollow(FOLLOW_csHeadSymbolList_in_csPattern2559);
                    csHeadSymbolList86=csHeadSymbolList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csHeadSymbolList.add(csHeadSymbolList86.getTree());
                    pushFollow(FOLLOW_csExplicitTermList_in_csPattern2561);
                    csExplicitTermList87=csExplicitTermList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csExplicitTermList.add(csExplicitTermList87.getTree());


                    // AST REWRITE
                    // elements: csHeadSymbolList, csExplicitTermList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 344:3: -> ^( Cst_Appl csHeadSymbolList csExplicitTermList )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:344:6: ^( Cst_Appl csHeadSymbolList csExplicitTermList )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Appl, "Cst_Appl"), root_1);

                        adaptor.addChild(root_1, stream_csHeadSymbolList.nextTree());
                        adaptor.addChild(root_1, stream_csExplicitTermList.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:347:3: csHeadSymbolList csImplicitPairList
                    {
                    pushFollow(FOLLOW_csHeadSymbolList_in_csPattern2582);
                    csHeadSymbolList88=csHeadSymbolList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csHeadSymbolList.add(csHeadSymbolList88.getTree());
                    pushFollow(FOLLOW_csImplicitPairList_in_csPattern2584);
                    csImplicitPairList89=csImplicitPairList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csImplicitPairList.add(csImplicitPairList89.getTree());


                    // AST REWRITE
                    // elements: csImplicitPairList, csHeadSymbolList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 348:3: -> ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:348:6: ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_RecordAppl, "Cst_RecordAppl"), root_1);

                        adaptor.addChild(root_1, stream_csHeadSymbolList.nextTree());
                        adaptor.addChild(root_1, stream_csImplicitPairList.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:352:3: IDENTIFIER (s= STAR )?
                    {
                    IDENTIFIER90=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csPattern2607); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER90);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:352:14: (s= STAR )?
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==STAR) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:352:15: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csPattern2612); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(s);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: IDENTIFIER, IDENTIFIER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 353:3: -> {s!=null}? ^( Cst_VariableStar IDENTIFIER )
                    if (s!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:353:17: ^( Cst_VariableStar IDENTIFIER )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_VariableStar, "Cst_VariableStar"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 354:3: -> ^( Cst_Variable IDENTIFIER )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:354:17: ^( Cst_Variable IDENTIFIER )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Variable, "Cst_Variable"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:358:3: UNDERSCORE (s= STAR )?
                    {
                    UNDERSCORE91=(Token)match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_csPattern2650); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_UNDERSCORE.add(UNDERSCORE91);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:358:14: (s= STAR )?
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==STAR) ) {
                        alt35=1;
                    }
                    switch (alt35) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:358:15: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csPattern2655); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(s);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 359:3: -> {s!=null}? ^( Cst_UnamedVariableStar )
                    if (s!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:359:17: ^( Cst_UnamedVariableStar )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_UnamedVariableStar, "Cst_UnamedVariableStar"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 360:3: -> ^( Cst_UnamedVariable )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:360:17: ^( Cst_UnamedVariable )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_UnamedVariable, "Cst_UnamedVariable"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 7 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:364:3: csConstantValue (s= STAR )?
                    {
                    pushFollow(FOLLOW_csConstantValue_in_csPattern2690);
                    csConstantValue92=csConstantValue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csConstantValue.add(csConstantValue92.getTree());
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:364:19: (s= STAR )?
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==STAR) ) {
                        alt36=1;
                    }
                    switch (alt36) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:364:20: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csPattern2695); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(s);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: csConstantValue, csConstantValue
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 365:3: -> {s!=null}? ^( Cst_ConstantStar csConstantValue )
                    if (s!=null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:365:17: ^( Cst_ConstantStar csConstantValue )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstantStar, "Cst_ConstantStar"), root_1);

                        adaptor.addChild(root_1, stream_csConstantValue.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 366:3: -> ^( Cst_Constant csConstantValue )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:366:17: ^( Cst_Constant csConstantValue )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Constant, "Cst_Constant"), root_1);

                        adaptor.addChild(root_1, stream_csConstantValue.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csPattern"

    public static class csHeadSymbolList_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csHeadSymbolList"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:373:1: csHeadSymbolList : ( csHeadSymbol -> ^( ConcCstSymbol csHeadSymbol ) | LPAR csHeadSymbol ( PIPE csHeadSymbol )* RPAR -> ^( ConcCstSymbol ( csHeadSymbol )* ) );
    public final miniTomParser.csHeadSymbolList_return csHeadSymbolList() throws RecognitionException {
        miniTomParser.csHeadSymbolList_return retval = new miniTomParser.csHeadSymbolList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAR94=null;
        Token PIPE96=null;
        Token RPAR98=null;
        miniTomParser.csHeadSymbol_return csHeadSymbol93 = null;

        miniTomParser.csHeadSymbol_return csHeadSymbol95 = null;

        miniTomParser.csHeadSymbol_return csHeadSymbol97 = null;


        Tree LPAR94_tree=null;
        Tree PIPE96_tree=null;
        Tree RPAR98_tree=null;
        RewriteRuleTokenStream stream_PIPE=new RewriteRuleTokenStream(adaptor,"token PIPE");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleSubtreeStream stream_csHeadSymbol=new RewriteRuleSubtreeStream(adaptor,"rule csHeadSymbol");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:373:18: ( csHeadSymbol -> ^( ConcCstSymbol csHeadSymbol ) | LPAR csHeadSymbol ( PIPE csHeadSymbol )* RPAR -> ^( ConcCstSymbol ( csHeadSymbol )* ) )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==IDENTIFIER||(LA39_0>=INTEGER && LA39_0<=STRING)) ) {
                alt39=1;
            }
            else if ( (LA39_0==LPAR) ) {
                alt39=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:374:3: csHeadSymbol
                    {
                    pushFollow(FOLLOW_csHeadSymbol_in_csHeadSymbolList2745);
                    csHeadSymbol93=csHeadSymbol();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csHeadSymbol.add(csHeadSymbol93.getTree());


                    // AST REWRITE
                    // elements: csHeadSymbol
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 375:3: -> ^( ConcCstSymbol csHeadSymbol )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:375:6: ^( ConcCstSymbol csHeadSymbol )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstSymbol, "ConcCstSymbol"), root_1);

                        adaptor.addChild(root_1, stream_csHeadSymbol.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:376:4: LPAR csHeadSymbol ( PIPE csHeadSymbol )* RPAR
                    {
                    LPAR94=(Token)match(input,LPAR,FOLLOW_LPAR_in_csHeadSymbolList2760); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR94);

                    pushFollow(FOLLOW_csHeadSymbol_in_csHeadSymbolList2762);
                    csHeadSymbol95=csHeadSymbol();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csHeadSymbol.add(csHeadSymbol95.getTree());
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:376:22: ( PIPE csHeadSymbol )*
                    loop38:
                    do {
                        int alt38=2;
                        int LA38_0 = input.LA(1);

                        if ( (LA38_0==PIPE) ) {
                            alt38=1;
                        }


                        switch (alt38) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:376:23: PIPE csHeadSymbol
                    	    {
                    	    PIPE96=(Token)match(input,PIPE,FOLLOW_PIPE_in_csHeadSymbolList2765); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_PIPE.add(PIPE96);

                    	    pushFollow(FOLLOW_csHeadSymbol_in_csHeadSymbolList2767);
                    	    csHeadSymbol97=csHeadSymbol();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csHeadSymbol.add(csHeadSymbol97.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop38;
                        }
                    } while (true);

                    RPAR98=(Token)match(input,RPAR,FOLLOW_RPAR_in_csHeadSymbolList2771); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR98);



                    // AST REWRITE
                    // elements: csHeadSymbol
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 377:3: -> ^( ConcCstSymbol ( csHeadSymbol )* )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:377:6: ^( ConcCstSymbol ( csHeadSymbol )* )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstSymbol, "ConcCstSymbol"), root_1);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:377:23: ( csHeadSymbol )*
                        while ( stream_csHeadSymbol.hasNext() ) {
                            adaptor.addChild(root_1, stream_csHeadSymbol.nextTree());

                        }
                        stream_csHeadSymbol.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csHeadSymbolList"

    public static class csHeadSymbol_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csHeadSymbol"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:380:1: csHeadSymbol : ( IDENTIFIER -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) ) | IDENTIFIER QMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) ) | IDENTIFIER DQMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) ) | INTEGER -> ^( Cst_ConstantInt INTEGER ) | LONG -> ^( Cst_ConstantLong LONG ) | CHAR -> ^( Cst_ConstantChar CHAR ) | DOUBLE -> ^( Cst_ConstantDouble DOUBLE ) | STRING -> ^( Cst_ConstantString STRING ) );
    public final miniTomParser.csHeadSymbol_return csHeadSymbol() throws RecognitionException {
        miniTomParser.csHeadSymbol_return retval = new miniTomParser.csHeadSymbol_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token IDENTIFIER99=null;
        Token IDENTIFIER100=null;
        Token QMARK101=null;
        Token IDENTIFIER102=null;
        Token DQMARK103=null;
        Token INTEGER104=null;
        Token LONG105=null;
        Token CHAR106=null;
        Token DOUBLE107=null;
        Token STRING108=null;

        Tree IDENTIFIER99_tree=null;
        Tree IDENTIFIER100_tree=null;
        Tree QMARK101_tree=null;
        Tree IDENTIFIER102_tree=null;
        Tree DQMARK103_tree=null;
        Tree INTEGER104_tree=null;
        Tree LONG105_tree=null;
        Tree CHAR106_tree=null;
        Tree DOUBLE107_tree=null;
        Tree STRING108_tree=null;
        RewriteRuleTokenStream stream_CHAR=new RewriteRuleTokenStream(adaptor,"token CHAR");
        RewriteRuleTokenStream stream_INTEGER=new RewriteRuleTokenStream(adaptor,"token INTEGER");
        RewriteRuleTokenStream stream_DOUBLE=new RewriteRuleTokenStream(adaptor,"token DOUBLE");
        RewriteRuleTokenStream stream_DQMARK=new RewriteRuleTokenStream(adaptor,"token DQMARK");
        RewriteRuleTokenStream stream_LONG=new RewriteRuleTokenStream(adaptor,"token LONG");
        RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:380:14: ( IDENTIFIER -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) ) | IDENTIFIER QMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) ) | IDENTIFIER DQMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) ) | INTEGER -> ^( Cst_ConstantInt INTEGER ) | LONG -> ^( Cst_ConstantLong LONG ) | CHAR -> ^( Cst_ConstantChar CHAR ) | DOUBLE -> ^( Cst_ConstantDouble DOUBLE ) | STRING -> ^( Cst_ConstantString STRING ) )
            int alt40=8;
            alt40 = dfa40.predict(input);
            switch (alt40) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:381:3: IDENTIFIER
                    {
                    IDENTIFIER99=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csHeadSymbol2796); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER99);



                    // AST REWRITE
                    // elements: IDENTIFIER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 382:3: -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:382:6: ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Symbol, "Cst_Symbol"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:382:30: ^( Cst_TheoryDEFAULT )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TheoryDEFAULT, "Cst_TheoryDEFAULT"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:383:3: IDENTIFIER QMARK
                    {
                    IDENTIFIER100=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csHeadSymbol2814); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER100);

                    QMARK101=(Token)match(input,QMARK,FOLLOW_QMARK_in_csHeadSymbol2816); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QMARK.add(QMARK101);



                    // AST REWRITE
                    // elements: IDENTIFIER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 384:3: -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:384:6: ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Symbol, "Cst_Symbol"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:384:30: ^( Cst_TheoryAU )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TheoryAU, "Cst_TheoryAU"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:385:3: IDENTIFIER DQMARK
                    {
                    IDENTIFIER102=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csHeadSymbol2834); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER102);

                    DQMARK103=(Token)match(input,DQMARK,FOLLOW_DQMARK_in_csHeadSymbol2836); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DQMARK.add(DQMARK103);



                    // AST REWRITE
                    // elements: IDENTIFIER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 386:3: -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:386:6: ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Symbol, "Cst_Symbol"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:386:30: ^( Cst_TheoryAC )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TheoryAC, "Cst_TheoryAC"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:387:3: INTEGER
                    {
                    INTEGER104=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_csHeadSymbol2854); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INTEGER.add(INTEGER104);



                    // AST REWRITE
                    // elements: INTEGER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 388:3: -> ^( Cst_ConstantInt INTEGER )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:388:6: ^( Cst_ConstantInt INTEGER )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstantInt, "Cst_ConstantInt"), root_1);

                        adaptor.addChild(root_1, stream_INTEGER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:389:3: LONG
                    {
                    LONG105=(Token)match(input,LONG,FOLLOW_LONG_in_csHeadSymbol2868); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LONG.add(LONG105);



                    // AST REWRITE
                    // elements: LONG
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 390:3: -> ^( Cst_ConstantLong LONG )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:390:6: ^( Cst_ConstantLong LONG )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstantLong, "Cst_ConstantLong"), root_1);

                        adaptor.addChild(root_1, stream_LONG.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:391:3: CHAR
                    {
                    CHAR106=(Token)match(input,CHAR,FOLLOW_CHAR_in_csHeadSymbol2882); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CHAR.add(CHAR106);



                    // AST REWRITE
                    // elements: CHAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 392:3: -> ^( Cst_ConstantChar CHAR )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:392:6: ^( Cst_ConstantChar CHAR )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstantChar, "Cst_ConstantChar"), root_1);

                        adaptor.addChild(root_1, stream_CHAR.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 7 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:393:3: DOUBLE
                    {
                    DOUBLE107=(Token)match(input,DOUBLE,FOLLOW_DOUBLE_in_csHeadSymbol2896); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DOUBLE.add(DOUBLE107);



                    // AST REWRITE
                    // elements: DOUBLE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 394:3: -> ^( Cst_ConstantDouble DOUBLE )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:394:6: ^( Cst_ConstantDouble DOUBLE )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstantDouble, "Cst_ConstantDouble"), root_1);

                        adaptor.addChild(root_1, stream_DOUBLE.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 8 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:395:3: STRING
                    {
                    STRING108=(Token)match(input,STRING,FOLLOW_STRING_in_csHeadSymbol2910); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STRING.add(STRING108);



                    // AST REWRITE
                    // elements: STRING
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 396:3: -> ^( Cst_ConstantString STRING )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:396:6: ^( Cst_ConstantString STRING )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstantString, "Cst_ConstantString"), root_1);

                        adaptor.addChild(root_1, stream_STRING.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csHeadSymbol"

    public static class csConstantValue_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csConstantValue"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:399:1: csConstantValue : ( INTEGER | LONG | CHAR | DOUBLE | STRING );
    public final miniTomParser.csConstantValue_return csConstantValue() throws RecognitionException {
        miniTomParser.csConstantValue_return retval = new miniTomParser.csConstantValue_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token set109=null;

        Tree set109_tree=null;

        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:399:17: ( INTEGER | LONG | CHAR | DOUBLE | STRING )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:
            {
            root_0 = (Tree)adaptor.nil();

            set109=(Token)input.LT(1);
            if ( (input.LA(1)>=INTEGER && input.LA(1)<=STRING) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Tree)adaptor.create(set109));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csConstantValue"

    public static class csExplicitTermList_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csExplicitTermList"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:403:1: csExplicitTermList : LPAR ( csPattern ( COMMA csPattern )* )? RPAR -> ^( ConcCstPattern ( csPattern )* ) ;
    public final miniTomParser.csExplicitTermList_return csExplicitTermList() throws RecognitionException {
        miniTomParser.csExplicitTermList_return retval = new miniTomParser.csExplicitTermList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAR110=null;
        Token COMMA112=null;
        Token RPAR114=null;
        miniTomParser.csPattern_return csPattern111 = null;

        miniTomParser.csPattern_return csPattern113 = null;


        Tree LPAR110_tree=null;
        Tree COMMA112_tree=null;
        Tree RPAR114_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_csPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPattern");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:403:20: ( LPAR ( csPattern ( COMMA csPattern )* )? RPAR -> ^( ConcCstPattern ( csPattern )* ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:404:4: LPAR ( csPattern ( COMMA csPattern )* )? RPAR
            {
            LPAR110=(Token)match(input,LPAR,FOLLOW_LPAR_in_csExplicitTermList2953); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR110);

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:404:9: ( csPattern ( COMMA csPattern )* )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==IDENTIFIER||LA42_0==LPAR||(LA42_0>=ANTI && LA42_0<=UNDERSCORE)||(LA42_0>=INTEGER && LA42_0<=STRING)) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:404:10: csPattern ( COMMA csPattern )*
                    {
                    pushFollow(FOLLOW_csPattern_in_csExplicitTermList2956);
                    csPattern111=csPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPattern.add(csPattern111.getTree());
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:404:20: ( COMMA csPattern )*
                    loop41:
                    do {
                        int alt41=2;
                        int LA41_0 = input.LA(1);

                        if ( (LA41_0==COMMA) ) {
                            alt41=1;
                        }


                        switch (alt41) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:404:21: COMMA csPattern
                    	    {
                    	    COMMA112=(Token)match(input,COMMA,FOLLOW_COMMA_in_csExplicitTermList2959); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA112);

                    	    pushFollow(FOLLOW_csPattern_in_csExplicitTermList2961);
                    	    csPattern113=csPattern();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csPattern.add(csPattern113.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop41;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR114=(Token)match(input,RPAR,FOLLOW_RPAR_in_csExplicitTermList2967); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR114);



            // AST REWRITE
            // elements: csPattern
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 406:2: -> ^( ConcCstPattern ( csPattern )* )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:406:5: ^( ConcCstPattern ( csPattern )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstPattern, "ConcCstPattern"), root_1);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:406:22: ( csPattern )*
                while ( stream_csPattern.hasNext() ) {
                    adaptor.addChild(root_1, stream_csPattern.nextTree());

                }
                stream_csPattern.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csExplicitTermList"

    public static class csImplicitPairList_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csImplicitPairList"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:409:1: csImplicitPairList : LSQUAREBR ( csPairPattern ( COMMA csPairPattern )* )? RSQUAREBR -> ^( ConcCstPairPattern ( csPairPattern )* ) ;
    public final miniTomParser.csImplicitPairList_return csImplicitPairList() throws RecognitionException {
        miniTomParser.csImplicitPairList_return retval = new miniTomParser.csImplicitPairList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LSQUAREBR115=null;
        Token COMMA117=null;
        Token RSQUAREBR119=null;
        miniTomParser.csPairPattern_return csPairPattern116 = null;

        miniTomParser.csPairPattern_return csPairPattern118 = null;


        Tree LSQUAREBR115_tree=null;
        Tree COMMA117_tree=null;
        Tree RSQUAREBR119_tree=null;
        RewriteRuleTokenStream stream_LSQUAREBR=new RewriteRuleTokenStream(adaptor,"token LSQUAREBR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RSQUAREBR=new RewriteRuleTokenStream(adaptor,"token RSQUAREBR");
        RewriteRuleSubtreeStream stream_csPairPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPairPattern");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:409:20: ( LSQUAREBR ( csPairPattern ( COMMA csPairPattern )* )? RSQUAREBR -> ^( ConcCstPairPattern ( csPairPattern )* ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:410:3: LSQUAREBR ( csPairPattern ( COMMA csPairPattern )* )? RSQUAREBR
            {
            LSQUAREBR115=(Token)match(input,LSQUAREBR,FOLLOW_LSQUAREBR_in_csImplicitPairList2989); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LSQUAREBR.add(LSQUAREBR115);

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:410:13: ( csPairPattern ( COMMA csPairPattern )* )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==IDENTIFIER) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:410:14: csPairPattern ( COMMA csPairPattern )*
                    {
                    pushFollow(FOLLOW_csPairPattern_in_csImplicitPairList2992);
                    csPairPattern116=csPairPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPairPattern.add(csPairPattern116.getTree());
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:410:28: ( COMMA csPairPattern )*
                    loop43:
                    do {
                        int alt43=2;
                        int LA43_0 = input.LA(1);

                        if ( (LA43_0==COMMA) ) {
                            alt43=1;
                        }


                        switch (alt43) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:410:29: COMMA csPairPattern
                    	    {
                    	    COMMA117=(Token)match(input,COMMA,FOLLOW_COMMA_in_csImplicitPairList2995); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA117);

                    	    pushFollow(FOLLOW_csPairPattern_in_csImplicitPairList2997);
                    	    csPairPattern118=csPairPattern();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csPairPattern.add(csPairPattern118.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop43;
                        }
                    } while (true);


                    }
                    break;

            }

            RSQUAREBR119=(Token)match(input,RSQUAREBR,FOLLOW_RSQUAREBR_in_csImplicitPairList3004); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RSQUAREBR.add(RSQUAREBR119);



            // AST REWRITE
            // elements: csPairPattern
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 412:3: -> ^( ConcCstPairPattern ( csPairPattern )* )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:412:6: ^( ConcCstPairPattern ( csPairPattern )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstPairPattern, "ConcCstPairPattern"), root_1);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:412:27: ( csPairPattern )*
                while ( stream_csPairPattern.hasNext() ) {
                    adaptor.addChild(root_1, stream_csPairPattern.nextTree());

                }
                stream_csPairPattern.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csImplicitPairList"

    public static class csPairPattern_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csPairPattern"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:415:1: csPairPattern : IDENTIFIER EQUAL csPattern -> ^( Cst_PairPattern IDENTIFIER csPattern ) ;
    public final miniTomParser.csPairPattern_return csPairPattern() throws RecognitionException {
        miniTomParser.csPairPattern_return retval = new miniTomParser.csPairPattern_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token IDENTIFIER120=null;
        Token EQUAL121=null;
        miniTomParser.csPattern_return csPattern122 = null;


        Tree IDENTIFIER120_tree=null;
        Tree EQUAL121_tree=null;
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_EQUAL=new RewriteRuleTokenStream(adaptor,"token EQUAL");
        RewriteRuleSubtreeStream stream_csPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPattern");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:415:15: ( IDENTIFIER EQUAL csPattern -> ^( Cst_PairPattern IDENTIFIER csPattern ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:416:2: IDENTIFIER EQUAL csPattern
            {
            IDENTIFIER120=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csPairPattern3026); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER120);

            EQUAL121=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_csPairPattern3028); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EQUAL.add(EQUAL121);

            pushFollow(FOLLOW_csPattern_in_csPairPattern3030);
            csPattern122=csPattern();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csPattern.add(csPattern122.getTree());


            // AST REWRITE
            // elements: IDENTIFIER, csPattern
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 418:2: -> ^( Cst_PairPattern IDENTIFIER csPattern )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:418:5: ^( Cst_PairPattern IDENTIFIER csPattern )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_PairPattern, "Cst_PairPattern"), root_1);

                adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                adaptor.addChild(root_1, stream_csPattern.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csPairPattern"

    public static class csOperatorConstruct_return extends ParserRuleReturnScope {
        public int marker;
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csOperatorConstruct"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:422:1: csOperatorConstruct returns [int marker] : codomain= IDENTIFIER ctorName= csName LPAR csSlotList RPAR LBR (ks+= csKeywordIsFsym | ks+= csKeywordMake | ks+= csKeywordGetSlot | ks+= csKeywordGetDefault | ks+= csKeywordOpImplement )* RBR -> ^( Cst_OpConstruct ^( Cst_Type $codomain) $ctorName csSlotList ^( ConcCstOperator ( $ks)* ) ) ;
    public final miniTomParser.csOperatorConstruct_return csOperatorConstruct() throws RecognitionException {
        miniTomParser.csOperatorConstruct_return retval = new miniTomParser.csOperatorConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token codomain=null;
        Token LPAR123=null;
        Token RPAR125=null;
        Token LBR126=null;
        Token RBR127=null;
        List list_ks=null;
        miniTomParser.csName_return ctorName = null;

        miniTomParser.csSlotList_return csSlotList124 = null;

        RuleReturnScope ks = null;
        Tree codomain_tree=null;
        Tree LPAR123_tree=null;
        Tree RPAR125_tree=null;
        Tree LBR126_tree=null;
        Tree RBR127_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        RewriteRuleSubtreeStream stream_csSlotList=new RewriteRuleSubtreeStream(adaptor,"rule csSlotList");
        RewriteRuleSubtreeStream stream_csKeywordMake=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordMake");
        RewriteRuleSubtreeStream stream_csKeywordIsFsym=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordIsFsym");
        RewriteRuleSubtreeStream stream_csKeywordGetDefault=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordGetDefault");
        RewriteRuleSubtreeStream stream_csKeywordGetSlot=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordGetSlot");
        RewriteRuleSubtreeStream stream_csKeywordOpImplement=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordOpImplement");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:423:22: (codomain= IDENTIFIER ctorName= csName LPAR csSlotList RPAR LBR (ks+= csKeywordIsFsym | ks+= csKeywordMake | ks+= csKeywordGetSlot | ks+= csKeywordGetDefault | ks+= csKeywordOpImplement )* RBR -> ^( Cst_OpConstruct ^( Cst_Type $codomain) $ctorName csSlotList ^( ConcCstOperator ( $ks)* ) ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:426:3: codomain= IDENTIFIER ctorName= csName LPAR csSlotList RPAR LBR (ks+= csKeywordIsFsym | ks+= csKeywordMake | ks+= csKeywordGetSlot | ks+= csKeywordGetDefault | ks+= csKeywordOpImplement )* RBR
            {
            codomain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorConstruct3065); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(codomain);

            pushFollow(FOLLOW_csName_in_csOperatorConstruct3069);
            ctorName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(ctorName.getTree());
            LPAR123=(Token)match(input,LPAR,FOLLOW_LPAR_in_csOperatorConstruct3071); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR123);

            pushFollow(FOLLOW_csSlotList_in_csOperatorConstruct3073);
            csSlotList124=csSlotList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csSlotList.add(csSlotList124.getTree());
            RPAR125=(Token)match(input,RPAR,FOLLOW_RPAR_in_csOperatorConstruct3075); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR125);

            LBR126=(Token)match(input,LBR,FOLLOW_LBR_in_csOperatorConstruct3079); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR126);

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:428:5: (ks+= csKeywordIsFsym | ks+= csKeywordMake | ks+= csKeywordGetSlot | ks+= csKeywordGetDefault | ks+= csKeywordOpImplement )*
            loop45:
            do {
                int alt45=6;
                switch ( input.LA(1) ) {
                case KEYWORD_IS_FSYM:
                    {
                    alt45=1;
                    }
                    break;
                case KEYWORD_MAKE:
                    {
                    alt45=2;
                    }
                    break;
                case KEYWORD_GET_SLOT:
                    {
                    alt45=3;
                    }
                    break;
                case KEYWORD_GET_DEFAULT:
                    {
                    alt45=4;
                    }
                    break;
                case KEYWORD_IMPLEMENT:
                    {
                    alt45=5;
                    }
                    break;

                }

                switch (alt45) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:428:8: ks+= csKeywordIsFsym
            	    {
            	    pushFollow(FOLLOW_csKeywordIsFsym_in_csOperatorConstruct3091);
            	    ks=csKeywordIsFsym();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordIsFsym.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:429:8: ks+= csKeywordMake
            	    {
            	    pushFollow(FOLLOW_csKeywordMake_in_csOperatorConstruct3102);
            	    ks=csKeywordMake();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMake.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:430:8: ks+= csKeywordGetSlot
            	    {
            	    pushFollow(FOLLOW_csKeywordGetSlot_in_csOperatorConstruct3113);
            	    ks=csKeywordGetSlot();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetSlot.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 4 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:431:8: ks+= csKeywordGetDefault
            	    {
            	    pushFollow(FOLLOW_csKeywordGetDefault_in_csOperatorConstruct3124);
            	    ks=csKeywordGetDefault();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetDefault.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 5 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:432:8: ks+= csKeywordOpImplement
            	    {
            	    pushFollow(FOLLOW_csKeywordOpImplement_in_csOperatorConstruct3135);
            	    ks=csKeywordOpImplement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordOpImplement.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;

            	default :
            	    break loop45;
                }
            } while (true);

            RBR127=(Token)match(input,RBR,FOLLOW_RBR_in_csOperatorConstruct3147); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR127);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR127).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: csSlotList, ctorName, codomain, ks
            // token labels: codomain
            // rule labels: retval, ctorName
            // token list labels: 
            // rule list labels: ks
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_codomain=new RewriteRuleTokenStream(adaptor,"token codomain",codomain);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ctorName=new RewriteRuleSubtreeStream(adaptor,"rule ctorName",ctorName!=null?ctorName.tree:null);
            RewriteRuleSubtreeStream stream_ks=new RewriteRuleSubtreeStream(adaptor,"token ks",list_ks);
            root_0 = (Tree)adaptor.nil();
            // 438:3: -> ^( Cst_OpConstruct ^( Cst_Type $codomain) $ctorName csSlotList ^( ConcCstOperator ( $ks)* ) )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:438:6: ^( Cst_OpConstruct ^( Cst_Type $codomain) $ctorName csSlotList ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OpConstruct, "Cst_OpConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)codomain, (CommonToken)RBR127));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:440:9: ^( Cst_Type $codomain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_codomain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_ctorName.nextTree());
                adaptor.addChild(root_1, stream_csSlotList.nextTree());
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:440:52: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:440:70: ( $ks)*
                while ( stream_ks.hasNext() ) {
                    adaptor.addChild(root_2, stream_ks.nextTree());

                }
                stream_ks.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csOperatorConstruct"

    public static class csOperatorArrayConstruct_return extends ParserRuleReturnScope {
        public int marker;
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csOperatorArrayConstruct"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:444:1: csOperatorArrayConstruct returns [int marker] : codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_Array | ks+= csKeywordMakeAppend | ks+= csKeywordGetElement | ks+= csKeywordGetSize )* RBR -> ^( Cst_OpArrayConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) ) ;
    public final miniTomParser.csOperatorArrayConstruct_return csOperatorArrayConstruct() throws RecognitionException {
        miniTomParser.csOperatorArrayConstruct_return retval = new miniTomParser.csOperatorArrayConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token codomain=null;
        Token domain=null;
        Token LPAR128=null;
        Token STAR129=null;
        Token RPAR130=null;
        Token LBR131=null;
        Token RBR132=null;
        List list_ks=null;
        miniTomParser.csName_return ctorName = null;

        RuleReturnScope ks = null;
        Tree codomain_tree=null;
        Tree domain_tree=null;
        Tree LPAR128_tree=null;
        Tree STAR129_tree=null;
        Tree RPAR130_tree=null;
        Tree LBR131_tree=null;
        Tree RBR132_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        RewriteRuleSubtreeStream stream_csKeywordGetElement=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordGetElement");
        RewriteRuleSubtreeStream stream_csKeywordMakeEmpty_Array=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordMakeEmpty_Array");
        RewriteRuleSubtreeStream stream_csKeywordGetSize=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordGetSize");
        RewriteRuleSubtreeStream stream_csKeywordIsFsym=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordIsFsym");
        RewriteRuleSubtreeStream stream_csKeywordMakeAppend=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordMakeAppend");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:445:22: (codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_Array | ks+= csKeywordMakeAppend | ks+= csKeywordGetElement | ks+= csKeywordGetSize )* RBR -> ^( Cst_OpArrayConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:448:3: codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_Array | ks+= csKeywordMakeAppend | ks+= csKeywordGetElement | ks+= csKeywordGetSize )* RBR
            {
            codomain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorArrayConstruct3228); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(codomain);

            pushFollow(FOLLOW_csName_in_csOperatorArrayConstruct3232);
            ctorName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(ctorName.getTree());
            LPAR128=(Token)match(input,LPAR,FOLLOW_LPAR_in_csOperatorArrayConstruct3234); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR128);

            domain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorArrayConstruct3238); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(domain);

            STAR129=(Token)match(input,STAR,FOLLOW_STAR_in_csOperatorArrayConstruct3240); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STAR.add(STAR129);

            RPAR130=(Token)match(input,RPAR,FOLLOW_RPAR_in_csOperatorArrayConstruct3242); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR130);

            LBR131=(Token)match(input,LBR,FOLLOW_LBR_in_csOperatorArrayConstruct3246); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR131);

            pushFollow(FOLLOW_csKeywordIsFsym_in_csOperatorArrayConstruct3257);
            ks=csKeywordIsFsym();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csKeywordIsFsym.add(ks.getTree());
            if (list_ks==null) list_ks=new ArrayList();
            list_ks.add(ks.getTree());

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:451:4: (ks+= csKeywordMakeEmpty_Array | ks+= csKeywordMakeAppend | ks+= csKeywordGetElement | ks+= csKeywordGetSize )*
            loop46:
            do {
                int alt46=5;
                switch ( input.LA(1) ) {
                case KEYWORD_MAKE_EMPTY:
                    {
                    alt46=1;
                    }
                    break;
                case KEYWORD_MAKE_APPEND:
                    {
                    alt46=2;
                    }
                    break;
                case KEYWORD_GET_ELEMENT:
                    {
                    alt46=3;
                    }
                    break;
                case KEYWORD_GET_SIZE:
                    {
                    alt46=4;
                    }
                    break;

                }

                switch (alt46) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:451:6: ks+= csKeywordMakeEmpty_Array
            	    {
            	    pushFollow(FOLLOW_csKeywordMakeEmpty_Array_in_csOperatorArrayConstruct3266);
            	    ks=csKeywordMakeEmpty_Array();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMakeEmpty_Array.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:452:6: ks+= csKeywordMakeAppend
            	    {
            	    pushFollow(FOLLOW_csKeywordMakeAppend_in_csOperatorArrayConstruct3275);
            	    ks=csKeywordMakeAppend();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMakeAppend.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:453:6: ks+= csKeywordGetElement
            	    {
            	    pushFollow(FOLLOW_csKeywordGetElement_in_csOperatorArrayConstruct3284);
            	    ks=csKeywordGetElement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetElement.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 4 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:454:6: ks+= csKeywordGetSize
            	    {
            	    pushFollow(FOLLOW_csKeywordGetSize_in_csOperatorArrayConstruct3293);
            	    ks=csKeywordGetSize();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetSize.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;

            	default :
            	    break loop46;
                }
            } while (true);

            RBR132=(Token)match(input,RBR,FOLLOW_RBR_in_csOperatorArrayConstruct3303); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR132);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR132).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: domain, ctorName, ks, codomain
            // token labels: codomain, domain
            // rule labels: retval, ctorName
            // token list labels: 
            // rule list labels: ks
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_codomain=new RewriteRuleTokenStream(adaptor,"token codomain",codomain);
            RewriteRuleTokenStream stream_domain=new RewriteRuleTokenStream(adaptor,"token domain",domain);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ctorName=new RewriteRuleSubtreeStream(adaptor,"rule ctorName",ctorName!=null?ctorName.tree:null);
            RewriteRuleSubtreeStream stream_ks=new RewriteRuleSubtreeStream(adaptor,"token ks",list_ks);
            root_0 = (Tree)adaptor.nil();
            // 460:3: -> ^( Cst_OpArrayConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:460:6: ^( Cst_OpArrayConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OpArrayConstruct, "Cst_OpArrayConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)codomain, (CommonToken)RBR132));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:462:9: ^( Cst_Type $codomain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_codomain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_ctorName.nextTree());
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:462:41: ^( Cst_Type $domain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_domain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:462:61: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:462:79: ( $ks)*
                while ( stream_ks.hasNext() ) {
                    adaptor.addChild(root_2, stream_ks.nextTree());

                }
                stream_ks.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csOperatorArrayConstruct"

    public static class csOperatorListConstruct_return extends ParserRuleReturnScope {
        public int marker;
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csOperatorListConstruct"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:466:1: csOperatorListConstruct returns [int marker] : codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_List | ks+= csKeywordMakeInsert | ks+= csKeywordGetHead | ks+= csKeywordGetTail | ks+= csKeywordIsEmpty )* RBR -> ^( Cst_OpListConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) ) ;
    public final miniTomParser.csOperatorListConstruct_return csOperatorListConstruct() throws RecognitionException {
        miniTomParser.csOperatorListConstruct_return retval = new miniTomParser.csOperatorListConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token codomain=null;
        Token domain=null;
        Token LPAR133=null;
        Token STAR134=null;
        Token RPAR135=null;
        Token LBR136=null;
        Token RBR137=null;
        List list_ks=null;
        miniTomParser.csName_return ctorName = null;

        RuleReturnScope ks = null;
        Tree codomain_tree=null;
        Tree domain_tree=null;
        Tree LPAR133_tree=null;
        Tree STAR134_tree=null;
        Tree RPAR135_tree=null;
        Tree LBR136_tree=null;
        Tree RBR137_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        RewriteRuleSubtreeStream stream_csKeywordIsEmpty=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordIsEmpty");
        RewriteRuleSubtreeStream stream_csKeywordIsFsym=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordIsFsym");
        RewriteRuleSubtreeStream stream_csKeywordMakeInsert=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordMakeInsert");
        RewriteRuleSubtreeStream stream_csKeywordGetHead=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordGetHead");
        RewriteRuleSubtreeStream stream_csKeywordMakeEmpty_List=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordMakeEmpty_List");
        RewriteRuleSubtreeStream stream_csKeywordGetTail=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordGetTail");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:467:22: (codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_List | ks+= csKeywordMakeInsert | ks+= csKeywordGetHead | ks+= csKeywordGetTail | ks+= csKeywordIsEmpty )* RBR -> ^( Cst_OpListConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:470:3: codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_List | ks+= csKeywordMakeInsert | ks+= csKeywordGetHead | ks+= csKeywordGetTail | ks+= csKeywordIsEmpty )* RBR
            {
            codomain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorListConstruct3389); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(codomain);

            pushFollow(FOLLOW_csName_in_csOperatorListConstruct3393);
            ctorName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(ctorName.getTree());
            LPAR133=(Token)match(input,LPAR,FOLLOW_LPAR_in_csOperatorListConstruct3395); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR133);

            domain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorListConstruct3399); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(domain);

            STAR134=(Token)match(input,STAR,FOLLOW_STAR_in_csOperatorListConstruct3401); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STAR.add(STAR134);

            RPAR135=(Token)match(input,RPAR,FOLLOW_RPAR_in_csOperatorListConstruct3403); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR135);

            LBR136=(Token)match(input,LBR,FOLLOW_LBR_in_csOperatorListConstruct3407); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR136);

            pushFollow(FOLLOW_csKeywordIsFsym_in_csOperatorListConstruct3416);
            ks=csKeywordIsFsym();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csKeywordIsFsym.add(ks.getTree());
            if (list_ks==null) list_ks=new ArrayList();
            list_ks.add(ks.getTree());

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:473:4: (ks+= csKeywordMakeEmpty_List | ks+= csKeywordMakeInsert | ks+= csKeywordGetHead | ks+= csKeywordGetTail | ks+= csKeywordIsEmpty )*
            loop47:
            do {
                int alt47=6;
                switch ( input.LA(1) ) {
                case KEYWORD_MAKE_EMPTY:
                    {
                    alt47=1;
                    }
                    break;
                case KEYWORD_MAKE_INSERT:
                    {
                    alt47=2;
                    }
                    break;
                case KEYWORD_GET_HEAD:
                    {
                    alt47=3;
                    }
                    break;
                case KEYWORD_GET_TAIL:
                    {
                    alt47=4;
                    }
                    break;
                case KEYWORD_IS_EMPTY:
                    {
                    alt47=5;
                    }
                    break;

                }

                switch (alt47) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:473:6: ks+= csKeywordMakeEmpty_List
            	    {
            	    pushFollow(FOLLOW_csKeywordMakeEmpty_List_in_csOperatorListConstruct3425);
            	    ks=csKeywordMakeEmpty_List();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMakeEmpty_List.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:474:6: ks+= csKeywordMakeInsert
            	    {
            	    pushFollow(FOLLOW_csKeywordMakeInsert_in_csOperatorListConstruct3434);
            	    ks=csKeywordMakeInsert();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMakeInsert.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:475:6: ks+= csKeywordGetHead
            	    {
            	    pushFollow(FOLLOW_csKeywordGetHead_in_csOperatorListConstruct3443);
            	    ks=csKeywordGetHead();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetHead.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 4 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:476:6: ks+= csKeywordGetTail
            	    {
            	    pushFollow(FOLLOW_csKeywordGetTail_in_csOperatorListConstruct3452);
            	    ks=csKeywordGetTail();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetTail.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 5 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:477:6: ks+= csKeywordIsEmpty
            	    {
            	    pushFollow(FOLLOW_csKeywordIsEmpty_in_csOperatorListConstruct3461);
            	    ks=csKeywordIsEmpty();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordIsEmpty.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);

            RBR137=(Token)match(input,RBR,FOLLOW_RBR_in_csOperatorListConstruct3471); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR137);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR137).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: ks, domain, codomain, ctorName
            // token labels: codomain, domain
            // rule labels: retval, ctorName
            // token list labels: 
            // rule list labels: ks
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_codomain=new RewriteRuleTokenStream(adaptor,"token codomain",codomain);
            RewriteRuleTokenStream stream_domain=new RewriteRuleTokenStream(adaptor,"token domain",domain);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ctorName=new RewriteRuleSubtreeStream(adaptor,"rule ctorName",ctorName!=null?ctorName.tree:null);
            RewriteRuleSubtreeStream stream_ks=new RewriteRuleSubtreeStream(adaptor,"token ks",list_ks);
            root_0 = (Tree)adaptor.nil();
            // 483:3: -> ^( Cst_OpListConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:483:6: ^( Cst_OpListConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OpListConstruct, "Cst_OpListConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)codomain, (CommonToken)RBR137));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:485:9: ^( Cst_Type $codomain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_codomain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_ctorName.nextTree());
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:485:41: ^( Cst_Type $domain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_domain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:485:61: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:485:79: ( $ks)*
                while ( stream_ks.hasNext() ) {
                    adaptor.addChild(root_2, stream_ks.nextTree());

                }
                stream_ks.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csOperatorListConstruct"

    public static class csTypetermConstruct_return extends ParserRuleReturnScope {
        public int marker;
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csTypetermConstruct"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:490:1: csTypetermConstruct returns [int marker] : typeName= IDENTIFIER ( EXTENDS extend= IDENTIFIER )? LBR ks+= csKeywordImplement (ks+= csKeywordIsSort )? (ks+= csKeywordEquals )? RBR -> {extend==null}? ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_TypeUnknown ) ^( ConcCstOperator ( $ks)* ) ) -> ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_Type $extend) ^( ConcCstOperator ( $ks)* ) ) ;
    public final miniTomParser.csTypetermConstruct_return csTypetermConstruct() throws RecognitionException {
        miniTomParser.csTypetermConstruct_return retval = new miniTomParser.csTypetermConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token typeName=null;
        Token extend=null;
        Token EXTENDS138=null;
        Token LBR139=null;
        Token RBR140=null;
        List list_ks=null;
        RuleReturnScope ks = null;
        Tree typeName_tree=null;
        Tree extend_tree=null;
        Tree EXTENDS138_tree=null;
        Tree LBR139_tree=null;
        Tree RBR140_tree=null;
        RewriteRuleTokenStream stream_EXTENDS=new RewriteRuleTokenStream(adaptor,"token EXTENDS");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csKeywordImplement=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordImplement");
        RewriteRuleSubtreeStream stream_csKeywordIsSort=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordIsSort");
        RewriteRuleSubtreeStream stream_csKeywordEquals=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordEquals");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:491:22: (typeName= IDENTIFIER ( EXTENDS extend= IDENTIFIER )? LBR ks+= csKeywordImplement (ks+= csKeywordIsSort )? (ks+= csKeywordEquals )? RBR -> {extend==null}? ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_TypeUnknown ) ^( ConcCstOperator ( $ks)* ) ) -> ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_Type $extend) ^( ConcCstOperator ( $ks)* ) ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:494:3: typeName= IDENTIFIER ( EXTENDS extend= IDENTIFIER )? LBR ks+= csKeywordImplement (ks+= csKeywordIsSort )? (ks+= csKeywordEquals )? RBR
            {
            typeName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csTypetermConstruct3558); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(typeName);

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:494:23: ( EXTENDS extend= IDENTIFIER )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==EXTENDS) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:494:24: EXTENDS extend= IDENTIFIER
                    {
                    EXTENDS138=(Token)match(input,EXTENDS,FOLLOW_EXTENDS_in_csTypetermConstruct3561); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EXTENDS.add(EXTENDS138);

                    extend=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csTypetermConstruct3565); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(extend);


                    }
                    break;

            }

            LBR139=(Token)match(input,LBR,FOLLOW_LBR_in_csTypetermConstruct3571); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR139);

            pushFollow(FOLLOW_csKeywordImplement_in_csTypetermConstruct3579);
            ks=csKeywordImplement();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csKeywordImplement.add(ks.getTree());
            if (list_ks==null) list_ks=new ArrayList();
            list_ks.add(ks.getTree());

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:496:28: (ks+= csKeywordIsSort )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==KEYWORD_IS_SORT) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:496:29: ks+= csKeywordIsSort
                    {
                    pushFollow(FOLLOW_csKeywordIsSort_in_csTypetermConstruct3584);
                    ks=csKeywordIsSort();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csKeywordIsSort.add(ks.getTree());
                    if (list_ks==null) list_ks=new ArrayList();
                    list_ks.add(ks.getTree());


                    }
                    break;

            }

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:496:51: (ks+= csKeywordEquals )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==KEYWORD_EQUALS) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:496:52: ks+= csKeywordEquals
                    {
                    pushFollow(FOLLOW_csKeywordEquals_in_csTypetermConstruct3591);
                    ks=csKeywordEquals();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csKeywordEquals.add(ks.getTree());
                    if (list_ks==null) list_ks=new ArrayList();
                    list_ks.add(ks.getTree());


                    }
                    break;

            }

            RBR140=(Token)match(input,RBR,FOLLOW_RBR_in_csTypetermConstruct3597); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR140);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR140).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: typeName, extend, ks, ks, typeName
            // token labels: typeName, extend
            // rule labels: retval
            // token list labels: 
            // rule list labels: ks
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_typeName=new RewriteRuleTokenStream(adaptor,"token typeName",typeName);
            RewriteRuleTokenStream stream_extend=new RewriteRuleTokenStream(adaptor,"token extend",extend);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ks=new RewriteRuleSubtreeStream(adaptor,"token ks",list_ks);
            root_0 = (Tree)adaptor.nil();
            // 501:3: -> {extend==null}? ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_TypeUnknown ) ^( ConcCstOperator ( $ks)* ) )
            if (extend==null) {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:502:4: ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_TypeUnknown ) ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypetermConstruct, "Cst_TypetermConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)typeName, (CommonToken)RBR140));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:504:9: ^( Cst_Type $typeName)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_typeName.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:504:31: ^( Cst_TypeUnknown )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:504:50: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:504:68: ( $ks)*
                while ( stream_ks.hasNext() ) {
                    adaptor.addChild(root_2, stream_ks.nextTree());

                }
                stream_ks.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 507:3: -> ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_Type $extend) ^( ConcCstOperator ( $ks)* ) )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:508:4: ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_Type $extend) ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypetermConstruct, "Cst_TypetermConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)typeName, (CommonToken)RBR140));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:510:9: ^( Cst_Type $typeName)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_typeName.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:510:31: ^( Cst_Type $extend)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_extend.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:510:51: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:510:69: ( $ks)*
                while ( stream_ks.hasNext() ) {
                    adaptor.addChild(root_2, stream_ks.nextTree());

                }
                stream_ks.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csTypetermConstruct"

    public static class csSlotList_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csSlotList"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:514:1: csSlotList : ( csSlot ( COMMA csSlot )* )? -> ^( ConcCstSlot ( csSlot )* ) ;
    public final miniTomParser.csSlotList_return csSlotList() throws RecognitionException {
        miniTomParser.csSlotList_return retval = new miniTomParser.csSlotList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token COMMA142=null;
        miniTomParser.csSlot_return csSlot141 = null;

        miniTomParser.csSlot_return csSlot143 = null;


        Tree COMMA142_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_csSlot=new RewriteRuleSubtreeStream(adaptor,"rule csSlot");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:514:12: ( ( csSlot ( COMMA csSlot )* )? -> ^( ConcCstSlot ( csSlot )* ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:515:3: ( csSlot ( COMMA csSlot )* )?
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:515:3: ( csSlot ( COMMA csSlot )* )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==IDENTIFIER) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:515:4: csSlot ( COMMA csSlot )*
                    {
                    pushFollow(FOLLOW_csSlot_in_csSlotList3729);
                    csSlot141=csSlot();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csSlot.add(csSlot141.getTree());
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:515:11: ( COMMA csSlot )*
                    loop51:
                    do {
                        int alt51=2;
                        int LA51_0 = input.LA(1);

                        if ( (LA51_0==COMMA) ) {
                            alt51=1;
                        }


                        switch (alt51) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:515:12: COMMA csSlot
                    	    {
                    	    COMMA142=(Token)match(input,COMMA,FOLLOW_COMMA_in_csSlotList3732); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA142);

                    	    pushFollow(FOLLOW_csSlot_in_csSlotList3734);
                    	    csSlot143=csSlot();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csSlot.add(csSlot143.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop51;
                        }
                    } while (true);


                    }
                    break;

            }



            // AST REWRITE
            // elements: csSlot
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 515:30: -> ^( ConcCstSlot ( csSlot )* )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:515:33: ^( ConcCstSlot ( csSlot )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstSlot, "ConcCstSlot"), root_1);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:515:47: ( csSlot )*
                while ( stream_csSlot.hasNext() ) {
                    adaptor.addChild(root_1, stream_csSlot.nextTree());

                }
                stream_csSlot.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csSlotList"

    public static class csSlot_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csSlot"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:518:1: csSlot : (name= IDENTIFIER COLON type= IDENTIFIER -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) ) | type= IDENTIFIER name= IDENTIFIER -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) ) );
    public final miniTomParser.csSlot_return csSlot() throws RecognitionException {
        miniTomParser.csSlot_return retval = new miniTomParser.csSlot_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token name=null;
        Token type=null;
        Token COLON144=null;

        Tree name_tree=null;
        Tree type_tree=null;
        Tree COLON144_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");

        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:518:8: (name= IDENTIFIER COLON type= IDENTIFIER -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) ) | type= IDENTIFIER name= IDENTIFIER -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) ) )
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==IDENTIFIER) ) {
                int LA53_1 = input.LA(2);

                if ( (LA53_1==COLON) ) {
                    alt53=1;
                }
                else if ( (LA53_1==IDENTIFIER) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 53, 0, input);

                throw nvae;
            }
            switch (alt53) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:519:5: name= IDENTIFIER COLON type= IDENTIFIER
                    {
                    name=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csSlot3764); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(name);

                    COLON144=(Token)match(input,COLON,FOLLOW_COLON_in_csSlot3766); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(COLON144);

                    type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csSlot3770); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(type);



                    // AST REWRITE
                    // elements: name, type
                    // token labels: name, type
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
                    RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 519:43: -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:519:46: ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Slot, "Cst_Slot"), root_1);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:519:57: ^( Cst_Name $name)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Name, "Cst_Name"), root_2);

                        adaptor.addChild(root_2, stream_name.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:519:75: ^( Cst_Type $type)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                        adaptor.addChild(root_2, stream_type.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:520:5: type= IDENTIFIER name= IDENTIFIER
                    {
                    type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csSlot3798); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(type);

                    name=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csSlot3802); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(name);



                    // AST REWRITE
                    // elements: name, type
                    // token labels: name, type
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
                    RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 520:37: -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:520:41: ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Slot, "Cst_Slot"), root_1);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:520:52: ^( Cst_Name $name)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Name, "Cst_Name"), root_2);

                        adaptor.addChild(root_2, stream_name.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:520:70: ^( Cst_Type $type)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                        adaptor.addChild(root_2, stream_type.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csSlot"

    public static class csKeywordIsFsym_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordIsFsym"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:525:1: csKeywordIsFsym : KEYWORD_IS_FSYM LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsFsym $argName) ;
    public final miniTomParser.csKeywordIsFsym_return csKeywordIsFsym() throws RecognitionException {
        miniTomParser.csKeywordIsFsym_return retval = new miniTomParser.csKeywordIsFsym_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_IS_FSYM145=null;
        Token LPAR146=null;
        Token RPAR147=null;
        Token LBR148=null;
        Token RBR149=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_IS_FSYM145_tree=null;
        Tree LPAR146_tree=null;
        Tree RPAR147_tree=null;
        Tree LBR148_tree=null;
        Tree RBR149_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_IS_FSYM=new RewriteRuleTokenStream(adaptor,"token KEYWORD_IS_FSYM");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:525:17: ( KEYWORD_IS_FSYM LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsFsym $argName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:526:3: KEYWORD_IS_FSYM LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_IS_FSYM145=(Token)match(input,KEYWORD_IS_FSYM,FOLLOW_KEYWORD_IS_FSYM_in_csKeywordIsFsym3836); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_IS_FSYM.add(KEYWORD_IS_FSYM145);

            LPAR146=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordIsFsym3838); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR146);

            pushFollow(FOLLOW_csName_in_csKeywordIsFsym3842);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR147=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordIsFsym3844); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR147);

            LBR148=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordIsFsym3848); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR148);

            RBR149=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordIsFsym3852); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR149);



            // AST REWRITE
            // elements: argName
            // token labels: 
            // rule labels: retval, argName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_argName=new RewriteRuleSubtreeStream(adaptor,"rule argName",argName!=null?argName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 529:3: -> ^( Cst_IsFsym $argName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:529:6: ^( Cst_IsFsym $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_IsFsym, "Cst_IsFsym"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR148).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordIsFsym"

    public static class csKeywordGetSlot_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordGetSlot"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:534:1: csKeywordGetSlot : KEYWORD_GET_SLOT LPAR slotName= csName COMMA termName= csName RPAR LBR RBR -> ^( Cst_GetSlot $slotName $termName) ;
    public final miniTomParser.csKeywordGetSlot_return csKeywordGetSlot() throws RecognitionException {
        miniTomParser.csKeywordGetSlot_return retval = new miniTomParser.csKeywordGetSlot_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_SLOT150=null;
        Token LPAR151=null;
        Token COMMA152=null;
        Token RPAR153=null;
        Token LBR154=null;
        Token RBR155=null;
        miniTomParser.csName_return slotName = null;

        miniTomParser.csName_return termName = null;


        Tree KEYWORD_GET_SLOT150_tree=null;
        Tree LPAR151_tree=null;
        Tree COMMA152_tree=null;
        Tree RPAR153_tree=null;
        Tree LBR154_tree=null;
        Tree RBR155_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_KEYWORD_GET_SLOT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_SLOT");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:534:18: ( KEYWORD_GET_SLOT LPAR slotName= csName COMMA termName= csName RPAR LBR RBR -> ^( Cst_GetSlot $slotName $termName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:535:3: KEYWORD_GET_SLOT LPAR slotName= csName COMMA termName= csName RPAR LBR RBR
            {
            KEYWORD_GET_SLOT150=(Token)match(input,KEYWORD_GET_SLOT,FOLLOW_KEYWORD_GET_SLOT_in_csKeywordGetSlot3892); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_SLOT.add(KEYWORD_GET_SLOT150);

            LPAR151=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetSlot3894); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR151);

            pushFollow(FOLLOW_csName_in_csKeywordGetSlot3898);
            slotName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(slotName.getTree());
            COMMA152=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordGetSlot3900); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA152);

            pushFollow(FOLLOW_csName_in_csKeywordGetSlot3904);
            termName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termName.getTree());
            RPAR153=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetSlot3906); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR153);

            LBR154=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetSlot3910); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR154);

            RBR155=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetSlot3914); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR155);



            // AST REWRITE
            // elements: slotName, termName
            // token labels: 
            // rule labels: termName, retval, slotName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_termName=new RewriteRuleSubtreeStream(adaptor,"rule termName",termName!=null?termName.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_slotName=new RewriteRuleSubtreeStream(adaptor,"rule slotName",slotName!=null?slotName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 538:3: -> ^( Cst_GetSlot $slotName $termName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:538:6: ^( Cst_GetSlot $slotName $termName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetSlot, "Cst_GetSlot"), root_1);

                adaptor.addChild(root_1, stream_slotName.nextTree());
                adaptor.addChild(root_1, stream_termName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR154).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordGetSlot"

    public static class csKeywordMake_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordMake"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:543:1: csKeywordMake : KEYWORD_MAKE LPAR argList= csNameList RPAR LBR RBR -> ^( Cst_Make $argList) ;
    public final miniTomParser.csKeywordMake_return csKeywordMake() throws RecognitionException {
        miniTomParser.csKeywordMake_return retval = new miniTomParser.csKeywordMake_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE156=null;
        Token LPAR157=null;
        Token RPAR158=null;
        Token LBR159=null;
        Token RBR160=null;
        miniTomParser.csNameList_return argList = null;


        Tree KEYWORD_MAKE156_tree=null;
        Tree LPAR157_tree=null;
        Tree RPAR158_tree=null;
        Tree LBR159_tree=null;
        Tree RBR160_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_MAKE=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csNameList=new RewriteRuleSubtreeStream(adaptor,"rule csNameList");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:543:15: ( KEYWORD_MAKE LPAR argList= csNameList RPAR LBR RBR -> ^( Cst_Make $argList) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:544:3: KEYWORD_MAKE LPAR argList= csNameList RPAR LBR RBR
            {
            KEYWORD_MAKE156=(Token)match(input,KEYWORD_MAKE,FOLLOW_KEYWORD_MAKE_in_csKeywordMake3957); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE.add(KEYWORD_MAKE156);

            LPAR157=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMake3959); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR157);

            pushFollow(FOLLOW_csNameList_in_csKeywordMake3963);
            argList=csNameList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csNameList.add(argList.getTree());
            RPAR158=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMake3965); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR158);

            LBR159=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMake3969); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR159);

            RBR160=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMake3973); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR160);



            // AST REWRITE
            // elements: argList
            // token labels: 
            // rule labels: retval, argList
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_argList=new RewriteRuleSubtreeStream(adaptor,"rule argList",argList!=null?argList.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 547:3: -> ^( Cst_Make $argList)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:547:6: ^( Cst_Make $argList)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Make, "Cst_Make"), root_1);

                adaptor.addChild(root_1, stream_argList.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR159).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordMake"

    public static class csKeywordGetDefault_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordGetDefault"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:552:1: csKeywordGetDefault : KEYWORD_GET_DEFAULT LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetDefault $argName) ;
    public final miniTomParser.csKeywordGetDefault_return csKeywordGetDefault() throws RecognitionException {
        miniTomParser.csKeywordGetDefault_return retval = new miniTomParser.csKeywordGetDefault_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_DEFAULT161=null;
        Token LPAR162=null;
        Token RPAR163=null;
        Token LBR164=null;
        Token RBR165=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_GET_DEFAULT161_tree=null;
        Tree LPAR162_tree=null;
        Tree RPAR163_tree=null;
        Tree LBR164_tree=null;
        Tree RBR165_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_GET_DEFAULT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_DEFAULT");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:552:21: ( KEYWORD_GET_DEFAULT LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetDefault $argName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:553:3: KEYWORD_GET_DEFAULT LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_GET_DEFAULT161=(Token)match(input,KEYWORD_GET_DEFAULT,FOLLOW_KEYWORD_GET_DEFAULT_in_csKeywordGetDefault4013); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_DEFAULT.add(KEYWORD_GET_DEFAULT161);

            LPAR162=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetDefault4015); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR162);

            pushFollow(FOLLOW_csName_in_csKeywordGetDefault4019);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR163=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetDefault4021); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR163);

            LBR164=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetDefault4025); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR164);

            RBR165=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetDefault4029); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR165);



            // AST REWRITE
            // elements: argName
            // token labels: 
            // rule labels: retval, argName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_argName=new RewriteRuleSubtreeStream(adaptor,"rule argName",argName!=null?argName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 556:3: -> ^( Cst_GetDefault $argName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:556:6: ^( Cst_GetDefault $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetDefault, "Cst_GetDefault"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR164).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordGetDefault"

    public static class csKeywordOpImplement_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordOpImplement"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:561:1: csKeywordOpImplement : KEYWORD_IMPLEMENT LPAR RPAR LBR RBR -> ^( Cst_Implement ) ;
    public final miniTomParser.csKeywordOpImplement_return csKeywordOpImplement() throws RecognitionException {
        miniTomParser.csKeywordOpImplement_return retval = new miniTomParser.csKeywordOpImplement_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_IMPLEMENT166=null;
        Token LPAR167=null;
        Token RPAR168=null;
        Token LBR169=null;
        Token RBR170=null;

        Tree KEYWORD_IMPLEMENT166_tree=null;
        Tree LPAR167_tree=null;
        Tree RPAR168_tree=null;
        Tree LBR169_tree=null;
        Tree RBR170_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_IMPLEMENT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_IMPLEMENT");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");

        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:561:22: ( KEYWORD_IMPLEMENT LPAR RPAR LBR RBR -> ^( Cst_Implement ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:562:3: KEYWORD_IMPLEMENT LPAR RPAR LBR RBR
            {
            KEYWORD_IMPLEMENT166=(Token)match(input,KEYWORD_IMPLEMENT,FOLLOW_KEYWORD_IMPLEMENT_in_csKeywordOpImplement4069); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_IMPLEMENT.add(KEYWORD_IMPLEMENT166);

            LPAR167=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordOpImplement4071); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR167);

            RPAR168=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordOpImplement4073); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR168);

            LBR169=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordOpImplement4077); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR169);

            RBR170=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordOpImplement4081); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR170);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 564:3: -> ^( Cst_Implement )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:564:6: ^( Cst_Implement )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Implement, "Cst_Implement"), root_1);

                adaptor.addChild(root_1, ((CustomToken)LBR169).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordOpImplement"

    public static class csKeywordGetHead_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordGetHead"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:568:1: csKeywordGetHead : KEYWORD_GET_HEAD LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetHead $argName) ;
    public final miniTomParser.csKeywordGetHead_return csKeywordGetHead() throws RecognitionException {
        miniTomParser.csKeywordGetHead_return retval = new miniTomParser.csKeywordGetHead_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_HEAD171=null;
        Token LPAR172=null;
        Token RPAR173=null;
        Token LBR174=null;
        Token RBR175=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_GET_HEAD171_tree=null;
        Tree LPAR172_tree=null;
        Tree RPAR173_tree=null;
        Tree LBR174_tree=null;
        Tree RBR175_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_KEYWORD_GET_HEAD=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_HEAD");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:568:18: ( KEYWORD_GET_HEAD LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetHead $argName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:569:3: KEYWORD_GET_HEAD LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_GET_HEAD171=(Token)match(input,KEYWORD_GET_HEAD,FOLLOW_KEYWORD_GET_HEAD_in_csKeywordGetHead4103); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_HEAD.add(KEYWORD_GET_HEAD171);

            LPAR172=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetHead4105); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR172);

            pushFollow(FOLLOW_csName_in_csKeywordGetHead4109);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR173=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetHead4111); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR173);

            LBR174=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetHead4116); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR174);

            RBR175=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetHead4120); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR175);



            // AST REWRITE
            // elements: argName
            // token labels: 
            // rule labels: retval, argName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_argName=new RewriteRuleSubtreeStream(adaptor,"rule argName",argName!=null?argName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 572:3: -> ^( Cst_GetHead $argName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:572:6: ^( Cst_GetHead $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetHead, "Cst_GetHead"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR174).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordGetHead"

    public static class csKeywordGetTail_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordGetTail"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:577:1: csKeywordGetTail : KEYWORD_GET_TAIL LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetTail $argName) ;
    public final miniTomParser.csKeywordGetTail_return csKeywordGetTail() throws RecognitionException {
        miniTomParser.csKeywordGetTail_return retval = new miniTomParser.csKeywordGetTail_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_TAIL176=null;
        Token LPAR177=null;
        Token RPAR178=null;
        Token LBR179=null;
        Token RBR180=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_GET_TAIL176_tree=null;
        Tree LPAR177_tree=null;
        Tree RPAR178_tree=null;
        Tree LBR179_tree=null;
        Tree RBR180_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_GET_TAIL=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_TAIL");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:577:18: ( KEYWORD_GET_TAIL LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetTail $argName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:578:3: KEYWORD_GET_TAIL LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_GET_TAIL176=(Token)match(input,KEYWORD_GET_TAIL,FOLLOW_KEYWORD_GET_TAIL_in_csKeywordGetTail4160); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_TAIL.add(KEYWORD_GET_TAIL176);

            LPAR177=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetTail4162); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR177);

            pushFollow(FOLLOW_csName_in_csKeywordGetTail4166);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR178=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetTail4168); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR178);

            LBR179=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetTail4172); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR179);

            RBR180=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetTail4176); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR180);



            // AST REWRITE
            // elements: argName
            // token labels: 
            // rule labels: retval, argName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_argName=new RewriteRuleSubtreeStream(adaptor,"rule argName",argName!=null?argName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 581:3: -> ^( Cst_GetTail $argName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:581:6: ^( Cst_GetTail $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetTail, "Cst_GetTail"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR179).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordGetTail"

    public static class csKeywordIsEmpty_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordIsEmpty"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:586:1: csKeywordIsEmpty : KEYWORD_IS_EMPTY LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsEmpty $argName) ;
    public final miniTomParser.csKeywordIsEmpty_return csKeywordIsEmpty() throws RecognitionException {
        miniTomParser.csKeywordIsEmpty_return retval = new miniTomParser.csKeywordIsEmpty_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_IS_EMPTY181=null;
        Token LPAR182=null;
        Token RPAR183=null;
        Token LBR184=null;
        Token RBR185=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_IS_EMPTY181_tree=null;
        Tree LPAR182_tree=null;
        Tree RPAR183_tree=null;
        Tree LBR184_tree=null;
        Tree RBR185_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_IS_EMPTY=new RewriteRuleTokenStream(adaptor,"token KEYWORD_IS_EMPTY");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:586:18: ( KEYWORD_IS_EMPTY LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsEmpty $argName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:587:3: KEYWORD_IS_EMPTY LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_IS_EMPTY181=(Token)match(input,KEYWORD_IS_EMPTY,FOLLOW_KEYWORD_IS_EMPTY_in_csKeywordIsEmpty4216); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_IS_EMPTY.add(KEYWORD_IS_EMPTY181);

            LPAR182=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordIsEmpty4218); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR182);

            pushFollow(FOLLOW_csName_in_csKeywordIsEmpty4222);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR183=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordIsEmpty4224); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR183);

            LBR184=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordIsEmpty4228); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR184);

            RBR185=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordIsEmpty4232); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR185);



            // AST REWRITE
            // elements: argName
            // token labels: 
            // rule labels: retval, argName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_argName=new RewriteRuleSubtreeStream(adaptor,"rule argName",argName!=null?argName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 590:3: -> ^( Cst_IsEmpty $argName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:590:6: ^( Cst_IsEmpty $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_IsEmpty, "Cst_IsEmpty"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR184).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordIsEmpty"

    public static class csKeywordMakeEmpty_List_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordMakeEmpty_List"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:595:1: csKeywordMakeEmpty_List : KEYWORD_MAKE_EMPTY LPAR RPAR LBR RBR -> ^( Cst_MakeEmptyList ) ;
    public final miniTomParser.csKeywordMakeEmpty_List_return csKeywordMakeEmpty_List() throws RecognitionException {
        miniTomParser.csKeywordMakeEmpty_List_return retval = new miniTomParser.csKeywordMakeEmpty_List_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE_EMPTY186=null;
        Token LPAR187=null;
        Token RPAR188=null;
        Token LBR189=null;
        Token RBR190=null;

        Tree KEYWORD_MAKE_EMPTY186_tree=null;
        Tree LPAR187_tree=null;
        Tree RPAR188_tree=null;
        Tree LBR189_tree=null;
        Tree RBR190_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_MAKE_EMPTY=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE_EMPTY");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");

        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:595:25: ( KEYWORD_MAKE_EMPTY LPAR RPAR LBR RBR -> ^( Cst_MakeEmptyList ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:596:3: KEYWORD_MAKE_EMPTY LPAR RPAR LBR RBR
            {
            KEYWORD_MAKE_EMPTY186=(Token)match(input,KEYWORD_MAKE_EMPTY,FOLLOW_KEYWORD_MAKE_EMPTY_in_csKeywordMakeEmpty_List4272); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE_EMPTY.add(KEYWORD_MAKE_EMPTY186);

            LPAR187=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMakeEmpty_List4274); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR187);

            RPAR188=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMakeEmpty_List4276); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR188);

            LBR189=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMakeEmpty_List4280); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR189);

            RBR190=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMakeEmpty_List4284); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR190);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 599:3: -> ^( Cst_MakeEmptyList )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:599:6: ^( Cst_MakeEmptyList )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MakeEmptyList, "Cst_MakeEmptyList"), root_1);

                adaptor.addChild(root_1, ((CustomToken)LBR189).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordMakeEmpty_List"

    public static class csKeywordMakeEmpty_Array_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordMakeEmpty_Array"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:604:1: csKeywordMakeEmpty_Array : KEYWORD_MAKE_EMPTY LPAR argName= csName RPAR LBR RBR -> ^( Cst_MakeEmptyArray $argName) ;
    public final miniTomParser.csKeywordMakeEmpty_Array_return csKeywordMakeEmpty_Array() throws RecognitionException {
        miniTomParser.csKeywordMakeEmpty_Array_return retval = new miniTomParser.csKeywordMakeEmpty_Array_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE_EMPTY191=null;
        Token LPAR192=null;
        Token RPAR193=null;
        Token LBR194=null;
        Token RBR195=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_MAKE_EMPTY191_tree=null;
        Tree LPAR192_tree=null;
        Tree RPAR193_tree=null;
        Tree LBR194_tree=null;
        Tree RBR195_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_MAKE_EMPTY=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE_EMPTY");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:604:26: ( KEYWORD_MAKE_EMPTY LPAR argName= csName RPAR LBR RBR -> ^( Cst_MakeEmptyArray $argName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:605:3: KEYWORD_MAKE_EMPTY LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_MAKE_EMPTY191=(Token)match(input,KEYWORD_MAKE_EMPTY,FOLLOW_KEYWORD_MAKE_EMPTY_in_csKeywordMakeEmpty_Array4321); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE_EMPTY.add(KEYWORD_MAKE_EMPTY191);

            LPAR192=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMakeEmpty_Array4323); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR192);

            pushFollow(FOLLOW_csName_in_csKeywordMakeEmpty_Array4327);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR193=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMakeEmpty_Array4329); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR193);

            LBR194=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMakeEmpty_Array4333); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR194);

            RBR195=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMakeEmpty_Array4337); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR195);



            // AST REWRITE
            // elements: argName
            // token labels: 
            // rule labels: retval, argName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_argName=new RewriteRuleSubtreeStream(adaptor,"rule argName",argName!=null?argName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 608:3: -> ^( Cst_MakeEmptyArray $argName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:608:6: ^( Cst_MakeEmptyArray $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MakeEmptyArray, "Cst_MakeEmptyArray"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR194).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordMakeEmpty_Array"

    public static class csKeywordMakeInsert_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordMakeInsert"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:613:1: csKeywordMakeInsert : KEYWORD_MAKE_INSERT LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR -> ^( Cst_MakeInsert $elementArgName $termArgName) ;
    public final miniTomParser.csKeywordMakeInsert_return csKeywordMakeInsert() throws RecognitionException {
        miniTomParser.csKeywordMakeInsert_return retval = new miniTomParser.csKeywordMakeInsert_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE_INSERT196=null;
        Token LPAR197=null;
        Token COMMA198=null;
        Token RPAR199=null;
        Token LBR200=null;
        Token RBR201=null;
        miniTomParser.csName_return elementArgName = null;

        miniTomParser.csName_return termArgName = null;


        Tree KEYWORD_MAKE_INSERT196_tree=null;
        Tree LPAR197_tree=null;
        Tree COMMA198_tree=null;
        Tree RPAR199_tree=null;
        Tree LBR200_tree=null;
        Tree RBR201_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_MAKE_INSERT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE_INSERT");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:613:21: ( KEYWORD_MAKE_INSERT LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR -> ^( Cst_MakeInsert $elementArgName $termArgName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:614:3: KEYWORD_MAKE_INSERT LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR
            {
            KEYWORD_MAKE_INSERT196=(Token)match(input,KEYWORD_MAKE_INSERT,FOLLOW_KEYWORD_MAKE_INSERT_in_csKeywordMakeInsert4377); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE_INSERT.add(KEYWORD_MAKE_INSERT196);

            LPAR197=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMakeInsert4383); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR197);

            pushFollow(FOLLOW_csName_in_csKeywordMakeInsert4387);
            elementArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(elementArgName.getTree());
            COMMA198=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordMakeInsert4389); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA198);

            pushFollow(FOLLOW_csName_in_csKeywordMakeInsert4393);
            termArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termArgName.getTree());
            RPAR199=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMakeInsert4395); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR199);

            LBR200=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMakeInsert4399); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR200);

            RBR201=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMakeInsert4403); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR201);



            // AST REWRITE
            // elements: elementArgName, termArgName
            // token labels: 
            // rule labels: retval, elementArgName, termArgName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_elementArgName=new RewriteRuleSubtreeStream(adaptor,"rule elementArgName",elementArgName!=null?elementArgName.tree:null);
            RewriteRuleSubtreeStream stream_termArgName=new RewriteRuleSubtreeStream(adaptor,"rule termArgName",termArgName!=null?termArgName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 618:3: -> ^( Cst_MakeInsert $elementArgName $termArgName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:618:6: ^( Cst_MakeInsert $elementArgName $termArgName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MakeInsert, "Cst_MakeInsert"), root_1);

                adaptor.addChild(root_1, stream_elementArgName.nextTree());
                adaptor.addChild(root_1, stream_termArgName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR200).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordMakeInsert"

    public static class csKeywordGetElement_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordGetElement"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:623:1: csKeywordGetElement : KEYWORD_GET_ELEMENT LPAR termArgName= csName COMMA elementIndexArgName= csName RPAR LBR RBR -> ^( Cst_GetElement $termArgName $elementIndexArgName) ;
    public final miniTomParser.csKeywordGetElement_return csKeywordGetElement() throws RecognitionException {
        miniTomParser.csKeywordGetElement_return retval = new miniTomParser.csKeywordGetElement_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_ELEMENT202=null;
        Token LPAR203=null;
        Token COMMA204=null;
        Token RPAR205=null;
        Token LBR206=null;
        Token RBR207=null;
        miniTomParser.csName_return termArgName = null;

        miniTomParser.csName_return elementIndexArgName = null;


        Tree KEYWORD_GET_ELEMENT202_tree=null;
        Tree LPAR203_tree=null;
        Tree COMMA204_tree=null;
        Tree RPAR205_tree=null;
        Tree LBR206_tree=null;
        Tree RBR207_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleTokenStream stream_KEYWORD_GET_ELEMENT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_ELEMENT");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:623:21: ( KEYWORD_GET_ELEMENT LPAR termArgName= csName COMMA elementIndexArgName= csName RPAR LBR RBR -> ^( Cst_GetElement $termArgName $elementIndexArgName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:624:3: KEYWORD_GET_ELEMENT LPAR termArgName= csName COMMA elementIndexArgName= csName RPAR LBR RBR
            {
            KEYWORD_GET_ELEMENT202=(Token)match(input,KEYWORD_GET_ELEMENT,FOLLOW_KEYWORD_GET_ELEMENT_in_csKeywordGetElement4446); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_ELEMENT.add(KEYWORD_GET_ELEMENT202);

            LPAR203=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetElement4452); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR203);

            pushFollow(FOLLOW_csName_in_csKeywordGetElement4456);
            termArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termArgName.getTree());
            COMMA204=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordGetElement4458); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA204);

            pushFollow(FOLLOW_csName_in_csKeywordGetElement4462);
            elementIndexArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(elementIndexArgName.getTree());
            RPAR205=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetElement4464); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR205);

            LBR206=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetElement4468); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR206);

            RBR207=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetElement4472); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR207);



            // AST REWRITE
            // elements: elementIndexArgName, termArgName
            // token labels: 
            // rule labels: retval, elementIndexArgName, termArgName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_elementIndexArgName=new RewriteRuleSubtreeStream(adaptor,"rule elementIndexArgName",elementIndexArgName!=null?elementIndexArgName.tree:null);
            RewriteRuleSubtreeStream stream_termArgName=new RewriteRuleSubtreeStream(adaptor,"rule termArgName",termArgName!=null?termArgName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 628:3: -> ^( Cst_GetElement $termArgName $elementIndexArgName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:628:6: ^( Cst_GetElement $termArgName $elementIndexArgName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetElement, "Cst_GetElement"), root_1);

                adaptor.addChild(root_1, stream_termArgName.nextTree());
                adaptor.addChild(root_1, stream_elementIndexArgName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR206).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordGetElement"

    public static class csKeywordGetSize_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordGetSize"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:633:1: csKeywordGetSize : KEYWORD_GET_SIZE LPAR termArgName= csName RPAR LBR RBR -> ^( Cst_GetSize $termArgName) ;
    public final miniTomParser.csKeywordGetSize_return csKeywordGetSize() throws RecognitionException {
        miniTomParser.csKeywordGetSize_return retval = new miniTomParser.csKeywordGetSize_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_SIZE208=null;
        Token LPAR209=null;
        Token RPAR210=null;
        Token LBR211=null;
        Token RBR212=null;
        miniTomParser.csName_return termArgName = null;


        Tree KEYWORD_GET_SIZE208_tree=null;
        Tree LPAR209_tree=null;
        Tree RPAR210_tree=null;
        Tree LBR211_tree=null;
        Tree RBR212_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_GET_SIZE=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_SIZE");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:633:18: ( KEYWORD_GET_SIZE LPAR termArgName= csName RPAR LBR RBR -> ^( Cst_GetSize $termArgName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:634:3: KEYWORD_GET_SIZE LPAR termArgName= csName RPAR LBR RBR
            {
            KEYWORD_GET_SIZE208=(Token)match(input,KEYWORD_GET_SIZE,FOLLOW_KEYWORD_GET_SIZE_in_csKeywordGetSize4515); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_SIZE.add(KEYWORD_GET_SIZE208);

            LPAR209=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetSize4517); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR209);

            pushFollow(FOLLOW_csName_in_csKeywordGetSize4521);
            termArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termArgName.getTree());
            RPAR210=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetSize4523); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR210);

            LBR211=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetSize4527); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR211);

            RBR212=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetSize4531); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR212);



            // AST REWRITE
            // elements: termArgName
            // token labels: 
            // rule labels: retval, termArgName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_termArgName=new RewriteRuleSubtreeStream(adaptor,"rule termArgName",termArgName!=null?termArgName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 637:3: -> ^( Cst_GetSize $termArgName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:637:6: ^( Cst_GetSize $termArgName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetSize, "Cst_GetSize"), root_1);

                adaptor.addChild(root_1, stream_termArgName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR211).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordGetSize"

    public static class csKeywordMakeAppend_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordMakeAppend"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:642:1: csKeywordMakeAppend : KEYWORD_MAKE_APPEND LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR -> ^( Cst_MakeAppend $elementArgName $termArgName) ;
    public final miniTomParser.csKeywordMakeAppend_return csKeywordMakeAppend() throws RecognitionException {
        miniTomParser.csKeywordMakeAppend_return retval = new miniTomParser.csKeywordMakeAppend_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE_APPEND213=null;
        Token LPAR214=null;
        Token COMMA215=null;
        Token RPAR216=null;
        Token LBR217=null;
        Token RBR218=null;
        miniTomParser.csName_return elementArgName = null;

        miniTomParser.csName_return termArgName = null;


        Tree KEYWORD_MAKE_APPEND213_tree=null;
        Tree LPAR214_tree=null;
        Tree COMMA215_tree=null;
        Tree RPAR216_tree=null;
        Tree LBR217_tree=null;
        Tree RBR218_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_MAKE_APPEND=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE_APPEND");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:642:21: ( KEYWORD_MAKE_APPEND LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR -> ^( Cst_MakeAppend $elementArgName $termArgName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:643:3: KEYWORD_MAKE_APPEND LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR
            {
            KEYWORD_MAKE_APPEND213=(Token)match(input,KEYWORD_MAKE_APPEND,FOLLOW_KEYWORD_MAKE_APPEND_in_csKeywordMakeAppend4571); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE_APPEND.add(KEYWORD_MAKE_APPEND213);

            LPAR214=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMakeAppend4578); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR214);

            pushFollow(FOLLOW_csName_in_csKeywordMakeAppend4582);
            elementArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(elementArgName.getTree());
            COMMA215=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordMakeAppend4584); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA215);

            pushFollow(FOLLOW_csName_in_csKeywordMakeAppend4588);
            termArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termArgName.getTree());
            RPAR216=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMakeAppend4590); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR216);

            LBR217=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMakeAppend4594); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR217);

            RBR218=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMakeAppend4598); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR218);



            // AST REWRITE
            // elements: elementArgName, termArgName
            // token labels: 
            // rule labels: retval, elementArgName, termArgName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_elementArgName=new RewriteRuleSubtreeStream(adaptor,"rule elementArgName",elementArgName!=null?elementArgName.tree:null);
            RewriteRuleSubtreeStream stream_termArgName=new RewriteRuleSubtreeStream(adaptor,"rule termArgName",termArgName!=null?termArgName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 647:3: -> ^( Cst_MakeAppend $elementArgName $termArgName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:647:6: ^( Cst_MakeAppend $elementArgName $termArgName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MakeAppend, "Cst_MakeAppend"), root_1);

                adaptor.addChild(root_1, stream_elementArgName.nextTree());
                adaptor.addChild(root_1, stream_termArgName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR217).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordMakeAppend"

    public static class csKeywordImplement_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordImplement"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:652:1: csKeywordImplement : KEYWORD_IMPLEMENT LBR RBR -> ^( Cst_Implement ) ;
    public final miniTomParser.csKeywordImplement_return csKeywordImplement() throws RecognitionException {
        miniTomParser.csKeywordImplement_return retval = new miniTomParser.csKeywordImplement_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_IMPLEMENT219=null;
        Token LBR220=null;
        Token RBR221=null;

        Tree KEYWORD_IMPLEMENT219_tree=null;
        Tree LBR220_tree=null;
        Tree RBR221_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_IMPLEMENT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_IMPLEMENT");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");

        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:652:20: ( KEYWORD_IMPLEMENT LBR RBR -> ^( Cst_Implement ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:653:3: KEYWORD_IMPLEMENT LBR RBR
            {
            KEYWORD_IMPLEMENT219=(Token)match(input,KEYWORD_IMPLEMENT,FOLLOW_KEYWORD_IMPLEMENT_in_csKeywordImplement4641); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_IMPLEMENT.add(KEYWORD_IMPLEMENT219);

            LBR220=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordImplement4643); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR220);

            RBR221=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordImplement4647); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR221);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 655:3: -> ^( Cst_Implement )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:655:6: ^( Cst_Implement )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Implement, "Cst_Implement"), root_1);

                adaptor.addChild(root_1, ((CustomToken)LBR220).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordImplement"

    public static class csKeywordIsSort_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordIsSort"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:658:1: csKeywordIsSort : KEYWORD_IS_SORT LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsSort $argName) ;
    public final miniTomParser.csKeywordIsSort_return csKeywordIsSort() throws RecognitionException {
        miniTomParser.csKeywordIsSort_return retval = new miniTomParser.csKeywordIsSort_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_IS_SORT222=null;
        Token LPAR223=null;
        Token RPAR224=null;
        Token LBR225=null;
        Token RBR226=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_IS_SORT222_tree=null;
        Tree LPAR223_tree=null;
        Tree RPAR224_tree=null;
        Tree LBR225_tree=null;
        Tree RBR226_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_IS_SORT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_IS_SORT");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:658:17: ( KEYWORD_IS_SORT LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsSort $argName) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:659:3: KEYWORD_IS_SORT LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_IS_SORT222=(Token)match(input,KEYWORD_IS_SORT,FOLLOW_KEYWORD_IS_SORT_in_csKeywordIsSort4669); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_IS_SORT.add(KEYWORD_IS_SORT222);

            LPAR223=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordIsSort4671); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR223);

            pushFollow(FOLLOW_csName_in_csKeywordIsSort4675);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR224=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordIsSort4677); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR224);

            LBR225=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordIsSort4683); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR225);

            RBR226=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordIsSort4687); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR226);



            // AST REWRITE
            // elements: argName
            // token labels: 
            // rule labels: retval, argName
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_argName=new RewriteRuleSubtreeStream(adaptor,"rule argName",argName!=null?argName.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 662:3: -> ^( Cst_IsSort $argName)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:662:6: ^( Cst_IsSort $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_IsSort, "Cst_IsSort"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR225).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordIsSort"

    public static class csKeywordEquals_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordEquals"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:665:1: csKeywordEquals : KEYWORD_EQUALS LPAR arg1= csName COMMA arg2= csName RPAR LBR RBR -> ^( Cst_Equals $arg1 $arg2) ;
    public final miniTomParser.csKeywordEquals_return csKeywordEquals() throws RecognitionException {
        miniTomParser.csKeywordEquals_return retval = new miniTomParser.csKeywordEquals_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_EQUALS227=null;
        Token LPAR228=null;
        Token COMMA229=null;
        Token RPAR230=null;
        Token LBR231=null;
        Token RBR232=null;
        miniTomParser.csName_return arg1 = null;

        miniTomParser.csName_return arg2 = null;


        Tree KEYWORD_EQUALS227_tree=null;
        Tree LPAR228_tree=null;
        Tree COMMA229_tree=null;
        Tree RPAR230_tree=null;
        Tree LBR231_tree=null;
        Tree RBR232_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_EQUALS=new RewriteRuleTokenStream(adaptor,"token KEYWORD_EQUALS");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:665:17: ( KEYWORD_EQUALS LPAR arg1= csName COMMA arg2= csName RPAR LBR RBR -> ^( Cst_Equals $arg1 $arg2) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:666:3: KEYWORD_EQUALS LPAR arg1= csName COMMA arg2= csName RPAR LBR RBR
            {
            KEYWORD_EQUALS227=(Token)match(input,KEYWORD_EQUALS,FOLLOW_KEYWORD_EQUALS_in_csKeywordEquals4712); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_EQUALS.add(KEYWORD_EQUALS227);

            LPAR228=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordEquals4714); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR228);

            pushFollow(FOLLOW_csName_in_csKeywordEquals4718);
            arg1=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(arg1.getTree());
            COMMA229=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordEquals4720); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA229);

            pushFollow(FOLLOW_csName_in_csKeywordEquals4724);
            arg2=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(arg2.getTree());
            RPAR230=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordEquals4726); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR230);

            LBR231=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordEquals4732); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR231);

            RBR232=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordEquals4736); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR232);



            // AST REWRITE
            // elements: arg2, arg1
            // token labels: 
            // rule labels: retval, arg2, arg1
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_arg2=new RewriteRuleSubtreeStream(adaptor,"rule arg2",arg2!=null?arg2.tree:null);
            RewriteRuleSubtreeStream stream_arg1=new RewriteRuleSubtreeStream(adaptor,"rule arg1",arg1!=null?arg1.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 669:3: -> ^( Cst_Equals $arg1 $arg2)
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:669:6: ^( Cst_Equals $arg1 $arg2)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Equals, "Cst_Equals"), root_1);

                adaptor.addChild(root_1, stream_arg1.nextTree());
                adaptor.addChild(root_1, stream_arg2.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR231).getPayload(Tree.class));

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csKeywordEquals"

    public static class csNameList_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csNameList"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:674:1: csNameList : ( csName ( COMMA csName )* )? -> ^( ConcCstName ( csName )* ) ;
    public final miniTomParser.csNameList_return csNameList() throws RecognitionException {
        miniTomParser.csNameList_return retval = new miniTomParser.csNameList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token COMMA234=null;
        miniTomParser.csName_return csName233 = null;

        miniTomParser.csName_return csName235 = null;


        Tree COMMA234_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:674:12: ( ( csName ( COMMA csName )* )? -> ^( ConcCstName ( csName )* ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:675:3: ( csName ( COMMA csName )* )?
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:675:3: ( csName ( COMMA csName )* )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==IDENTIFIER) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:675:4: csName ( COMMA csName )*
                    {
                    pushFollow(FOLLOW_csName_in_csNameList4781);
                    csName233=csName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csName.add(csName233.getTree());
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:675:11: ( COMMA csName )*
                    loop54:
                    do {
                        int alt54=2;
                        int LA54_0 = input.LA(1);

                        if ( (LA54_0==COMMA) ) {
                            alt54=1;
                        }


                        switch (alt54) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:675:12: COMMA csName
                    	    {
                    	    COMMA234=(Token)match(input,COMMA,FOLLOW_COMMA_in_csNameList4784); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA234);

                    	    pushFollow(FOLLOW_csName_in_csNameList4786);
                    	    csName235=csName();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csName.add(csName235.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop54;
                        }
                    } while (true);


                    }
                    break;

            }



            // AST REWRITE
            // elements: csName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 676:3: -> ^( ConcCstName ( csName )* )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:676:6: ^( ConcCstName ( csName )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstName, "ConcCstName"), root_1);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:676:20: ( csName )*
                while ( stream_csName.hasNext() ) {
                    adaptor.addChild(root_1, stream_csName.nextTree());

                }
                stream_csName.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csNameList"

    public static class csName_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csName"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:679:1: csName : IDENTIFIER -> ^( Cst_Name IDENTIFIER ) ;
    public final miniTomParser.csName_return csName() throws RecognitionException {
        miniTomParser.csName_return retval = new miniTomParser.csName_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token IDENTIFIER236=null;

        Tree IDENTIFIER236_tree=null;
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");

        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:679:8: ( IDENTIFIER -> ^( Cst_Name IDENTIFIER ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:680:3: IDENTIFIER
            {
            IDENTIFIER236=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csName4812); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER236);



            // AST REWRITE
            // elements: IDENTIFIER
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 681:3: -> ^( Cst_Name IDENTIFIER )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:681:6: ^( Cst_Name IDENTIFIER )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Name, "Cst_Name"), root_1);

                adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "csName"

    // $ANTLR start synpred1_miniTom
    public final void synpred1_miniTom_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:102:37: ( BQUOTE )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:102:37: BQUOTE
        {
        match(input,BQUOTE,FOLLOW_BQUOTE_in_synpred1_miniTom139); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_miniTom

    // $ANTLR start synpred5_miniTom
    public final void synpred5_miniTom_fragment() throws RecognitionException {   
        Token l=null;

        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR
        {
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( IDENTIFIER l= COLON )?
        int alt56=2;
        int LA56_0 = input.LA(1);

        if ( (LA56_0==IDENTIFIER) ) {
            int LA56_1 = input.LA(2);

            if ( (LA56_1==COLON) ) {
                alt56=1;
            }
        }
        switch (alt56) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:129:4: IDENTIFIER l= COLON
                {
                match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred5_miniTom367); if (state.failed) return ;
                l=(Token)match(input,COLON,FOLLOW_COLON_in_synpred5_miniTom371); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_csExtendedConstraint_in_synpred5_miniTom375);
        csExtendedConstraint();

        state._fsp--;
        if (state.failed) return ;
        match(input,ARROW,FOLLOW_ARROW_in_synpred5_miniTom377); if (state.failed) return ;
        match(input,LBR,FOLLOW_LBR_in_synpred5_miniTom379); if (state.failed) return ;
        match(input,RBR,FOLLOW_RBR_in_synpred5_miniTom381); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred5_miniTom

    // $ANTLR start synpred18_miniTom
    public final void synpred18_miniTom_fragment() throws RecognitionException {   
        Token type=null;
        Token bqname=null;
        List list_a=null;
        RuleReturnScope a = null;
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:3: ( (type= IDENTIFIER )? ( BQUOTE )? bqname= IDENTIFIER LPAR (a+= csBQTerm[false] ( COMMA a+= csBQTerm[false] )* )? RPAR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:3: (type= IDENTIFIER )? ( BQUOTE )? bqname= IDENTIFIER LPAR (a+= csBQTerm[false] ( COMMA a+= csBQTerm[false] )* )? RPAR
        {
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:3: (type= IDENTIFIER )?
        int alt60=2;
        int LA60_0 = input.LA(1);

        if ( (LA60_0==IDENTIFIER) ) {
            int LA60_1 = input.LA(2);

            if ( (LA60_1==IDENTIFIER||LA60_1==BQUOTE) ) {
                alt60=1;
            }
        }
        switch (alt60) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:4: type= IDENTIFIER
                {
                type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred18_miniTom1498); if (state.failed) return ;

                }
                break;

        }

        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:22: ( BQUOTE )?
        int alt61=2;
        int LA61_0 = input.LA(1);

        if ( (LA61_0==BQUOTE) ) {
            alt61=1;
        }
        switch (alt61) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:0:0: BQUOTE
                {
                match(input,BQUOTE,FOLLOW_BQUOTE_in_synpred18_miniTom1502); if (state.failed) return ;

                }
                break;

        }

        bqname=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred18_miniTom1507); if (state.failed) return ;
        match(input,LPAR,FOLLOW_LPAR_in_synpred18_miniTom1509); if (state.failed) return ;
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:53: (a+= csBQTerm[false] ( COMMA a+= csBQTerm[false] )* )?
        int alt63=2;
        int LA63_0 = input.LA(1);

        if ( (LA63_0==IDENTIFIER||LA63_0==BQUOTE||(LA63_0>=INTEGER && LA63_0<=STRING)) ) {
            alt63=1;
        }
        switch (alt63) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:54: a+= csBQTerm[false] ( COMMA a+= csBQTerm[false] )*
                {
                pushFollow(FOLLOW_csBQTerm_in_synpred18_miniTom1514);
                a=csBQTerm(false);

                state._fsp--;
                if (state.failed) return ;
                if (list_a==null) list_a=new ArrayList();
                list_a.add(a);

                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:73: ( COMMA a+= csBQTerm[false] )*
                loop62:
                do {
                    int alt62=2;
                    int LA62_0 = input.LA(1);

                    if ( (LA62_0==COMMA) ) {
                        alt62=1;
                    }


                    switch (alt62) {
                	case 1 :
                	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:221:74: COMMA a+= csBQTerm[false]
                	    {
                	    match(input,COMMA,FOLLOW_COMMA_in_synpred18_miniTom1518); if (state.failed) return ;
                	    pushFollow(FOLLOW_csBQTerm_in_synpred18_miniTom1522);
                	    a=csBQTerm(false);

                	    state._fsp--;
                	    if (state.failed) return ;
                	    if (list_a==null) list_a=new ArrayList();
                	    list_a.add(a);


                	    }
                	    break;

                	default :
                	    break loop62;
                    }
                } while (true);


                }
                break;

        }

        match(input,RPAR,FOLLOW_RPAR_in_synpred18_miniTom1529); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred18_miniTom

    // $ANTLR start synpred19_miniTom
    public final void synpred19_miniTom_fragment() throws RecognitionException {   
        Token type=null;

        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:5: (type= IDENTIFIER )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:5: type= IDENTIFIER
        {
        type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred19_miniTom1670); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred19_miniTom

    // $ANTLR start synpred22_miniTom
    public final void synpred22_miniTom_fragment() throws RecognitionException {   
        Token type=null;
        Token name=null;
        Token s=null;

        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:4: ( (type= IDENTIFIER )? ( BQUOTE )? name= IDENTIFIER (s= STAR )? )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:4: (type= IDENTIFIER )? ( BQUOTE )? name= IDENTIFIER (s= STAR )?
        {
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:4: (type= IDENTIFIER )?
        int alt64=2;
        int LA64_0 = input.LA(1);

        if ( (LA64_0==IDENTIFIER) ) {
            int LA64_1 = input.LA(2);

            if ( (LA64_1==IDENTIFIER||LA64_1==BQUOTE) ) {
                alt64=1;
            }
        }
        switch (alt64) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:5: type= IDENTIFIER
                {
                type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred22_miniTom1670); if (state.failed) return ;

                }
                break;

        }

        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:23: ( BQUOTE )?
        int alt65=2;
        int LA65_0 = input.LA(1);

        if ( (LA65_0==BQUOTE) ) {
            alt65=1;
        }
        switch (alt65) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:0:0: BQUOTE
                {
                match(input,BQUOTE,FOLLOW_BQUOTE_in_synpred22_miniTom1674); if (state.failed) return ;

                }
                break;

        }

        name=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred22_miniTom1679); if (state.failed) return ;
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:47: (s= STAR )?
        int alt66=2;
        int LA66_0 = input.LA(1);

        if ( (LA66_0==STAR) ) {
            alt66=1;
        }
        switch (alt66) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:229:48: s= STAR
                {
                s=(Token)match(input,STAR,FOLLOW_STAR_in_synpred22_miniTom1684); if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred22_miniTom

    // $ANTLR start synpred29_miniTom
    public final void synpred29_miniTom_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:303:3: ( csPattern LARROW csBQTerm[true] )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:303:3: csPattern LARROW csBQTerm[true]
        {
        pushFollow(FOLLOW_csPattern_in_synpred29_miniTom2197);
        csPattern();

        state._fsp--;
        if (state.failed) return ;
        match(input,LARROW,FOLLOW_LARROW_in_synpred29_miniTom2199); if (state.failed) return ;
        pushFollow(FOLLOW_csBQTerm_in_synpred29_miniTom2201);
        csBQTerm(true);

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred29_miniTom

    // $ANTLR start synpred35_miniTom
    public final void synpred35_miniTom_fragment() throws RecognitionException {   
        Token gt=null;
        Token ge=null;
        Token lt=null;
        Token le=null;
        Token eq=null;
        Token ne=null;
        miniTomParser.csTerm_return l = null;

        miniTomParser.csTerm_return r = null;


        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:306:5: (l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:306:5: l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm
        {
        pushFollow(FOLLOW_csTerm_in_synpred35_miniTom2224);
        l=csTerm();

        state._fsp--;
        if (state.failed) return ;
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:307:5: (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT )
        int alt68=6;
        switch ( input.LA(1) ) {
        case GREATERTHAN:
            {
            alt68=1;
            }
            break;
        case GREATEROREQU:
            {
            alt68=2;
            }
            break;
        case LOWERTHAN:
            {
            alt68=3;
            }
            break;
        case LOWEROREQU:
            {
            alt68=4;
            }
            break;
        case DOUBLEEQUAL:
            {
            alt68=5;
            }
            break;
        case DIFFERENT:
            {
            alt68=6;
            }
            break;
        default:
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 68, 0, input);

            throw nvae;
        }

        switch (alt68) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:307:6: gt= GREATERTHAN
                {
                gt=(Token)match(input,GREATERTHAN,FOLLOW_GREATERTHAN_in_synpred35_miniTom2234); if (state.failed) return ;

                }
                break;
            case 2 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:307:21: ge= GREATEROREQU
                {
                ge=(Token)match(input,GREATEROREQU,FOLLOW_GREATEROREQU_in_synpred35_miniTom2238); if (state.failed) return ;

                }
                break;
            case 3 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:307:37: lt= LOWERTHAN
                {
                lt=(Token)match(input,LOWERTHAN,FOLLOW_LOWERTHAN_in_synpred35_miniTom2242); if (state.failed) return ;

                }
                break;
            case 4 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:308:6: le= LOWEROREQU
                {
                le=(Token)match(input,LOWEROREQU,FOLLOW_LOWEROREQU_in_synpred35_miniTom2251); if (state.failed) return ;

                }
                break;
            case 5 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:308:21: eq= DOUBLEEQUAL
                {
                eq=(Token)match(input,DOUBLEEQUAL,FOLLOW_DOUBLEEQUAL_in_synpred35_miniTom2256); if (state.failed) return ;

                }
                break;
            case 6 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:308:37: ne= DIFFERENT
                {
                ne=(Token)match(input,DIFFERENT,FOLLOW_DIFFERENT_in_synpred35_miniTom2261); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_csTerm_in_synpred35_miniTom2270);
        r=csTerm();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred35_miniTom

    // Delegated rules

    public final boolean synpred29_miniTom() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred29_miniTom_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_miniTom() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_miniTom_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred35_miniTom() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred35_miniTom_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred19_miniTom() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred19_miniTom_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred18_miniTom() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred18_miniTom_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred22_miniTom() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred22_miniTom_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_miniTom() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_miniTom_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA6 dfa6 = new DFA6(this);
    protected DFA29 dfa29 = new DFA29(this);
    protected DFA37 dfa37 = new DFA37(this);
    protected DFA40 dfa40 = new DFA40(this);
    static final String DFA6_eotS =
        "\14\uffff";
    static final String DFA6_eofS =
        "\14\uffff";
    static final String DFA6_minS =
        "\1\u016c\11\0\2\uffff";
    static final String DFA6_maxS =
        "\1\u018a\11\0\2\uffff";
    static final String DFA6_acceptS =
        "\12\uffff\1\1\1\2";
    static final String DFA6_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff}>";
    static final String[] DFA6_transitionS = {
            "\1\1\1\uffff\1\10\22\uffff\1\2\1\11\3\uffff\1\3\1\4\1\5\1\6"+
            "\1\7",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "128:1: csVisitAction : ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) | ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW csBQTerm[false] -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ) ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA6_1 = input.LA(1);

                         
                        int index6_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index6_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA6_2 = input.LA(1);

                         
                        int index6_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index6_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA6_3 = input.LA(1);

                         
                        int index6_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index6_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA6_4 = input.LA(1);

                         
                        int index6_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index6_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA6_5 = input.LA(1);

                         
                        int index6_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index6_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA6_6 = input.LA(1);

                         
                        int index6_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index6_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA6_7 = input.LA(1);

                         
                        int index6_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index6_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA6_8 = input.LA(1);

                         
                        int index6_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index6_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA6_9 = input.LA(1);

                         
                        int index6_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index6_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 6, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA29_eotS =
        "\14\uffff";
    static final String DFA29_eofS =
        "\14\uffff";
    static final String DFA29_minS =
        "\1\u016c\1\0\6\uffff\1\0\3\uffff";
    static final String DFA29_maxS =
        "\1\u018a\1\0\6\uffff\1\0\3\uffff";
    static final String DFA29_acceptS =
        "\2\uffff\1\1\7\uffff\1\2\1\3";
    static final String DFA29_specialS =
        "\1\uffff\1\0\6\uffff\1\1\3\uffff}>";
    static final String[] DFA29_transitionS = {
            "\1\1\1\uffff\1\10\22\uffff\2\2\3\uffff\5\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA29_eot = DFA.unpackEncodedString(DFA29_eotS);
    static final short[] DFA29_eof = DFA.unpackEncodedString(DFA29_eofS);
    static final char[] DFA29_min = DFA.unpackEncodedStringToUnsignedChars(DFA29_minS);
    static final char[] DFA29_max = DFA.unpackEncodedStringToUnsignedChars(DFA29_maxS);
    static final short[] DFA29_accept = DFA.unpackEncodedString(DFA29_acceptS);
    static final short[] DFA29_special = DFA.unpackEncodedString(DFA29_specialS);
    static final short[][] DFA29_transition;

    static {
        int numStates = DFA29_transitionS.length;
        DFA29_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA29_transition[i] = DFA.unpackEncodedString(DFA29_transitionS[i]);
        }
    }

    class DFA29 extends DFA {

        public DFA29(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 29;
            this.eot = DFA29_eot;
            this.eof = DFA29_eof;
            this.min = DFA29_min;
            this.max = DFA29_max;
            this.accept = DFA29_accept;
            this.special = DFA29_special;
            this.transition = DFA29_transition;
        }
        public String getDescription() {
            return "301:1: csConstraint_priority3 : ( csPattern LARROW csBQTerm[true] -> ^( Cst_MatchTermConstraint csPattern csBQTerm ) | l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm -> {gt!=null}? ^( Cst_NumGreaterThan $l $r) -> {ge!=null}? ^( Cst_NumGreaterOrEqualTo $l $r) -> {lt!=null}? ^( Cst_NumLessThan $l $r) -> {le!=null}? ^( Cst_NumLessOrEqualTo $l $r) -> {eq!=null}? ^( Cst_NumEqualTo $l $r) -> ^( Cst_NumDifferent $l $r) | LPAR csConstraint RPAR -> csConstraint );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA29_1 = input.LA(1);

                         
                        int index29_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred29_miniTom()) ) {s = 2;}

                        else if ( (synpred35_miniTom()) ) {s = 10;}

                         
                        input.seek(index29_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA29_8 = input.LA(1);

                         
                        int index29_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred29_miniTom()) ) {s = 2;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index29_8);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 29, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA37_eotS =
        "\43\uffff";
    static final String DFA37_eofS =
        "\1\uffff\1\16\1\uffff\5\20\33\uffff";
    static final String DFA37_minS =
        "\1\u016c\1\u016e\1\uffff\5\u016e\1\u016c\2\uffff\2\u016e\4\uffff"+
        "\10\u016f\1\u016c\1\u016e\10\u016f";
    static final String DFA37_maxS =
        "\1\u018a\1\u018c\1\uffff\5\u018c\1\u018a\2\uffff\2\u018b\4\uffff"+
        "\1\u0185\7\u0183\1\u018a\1\u018b\1\u0185\7\u0183";
    static final String DFA37_acceptS =
        "\2\uffff\1\2\6\uffff\1\6\1\1\2\uffff\1\4\1\5\1\3\1\7\22\uffff";
    static final String DFA37_specialS =
        "\43\uffff}>";
    static final String[] DFA37_transitionS = {
            "\1\1\1\uffff\1\10\22\uffff\1\2\1\11\3\uffff\1\3\1\4\1\5\1\6"+
            "\1\7",
            "\1\17\1\16\4\uffff\6\16\6\uffff\1\12\3\uffff\1\13\1\14\5\uffff"+
            "\1\15\1\16",
            "",
            "\1\17\1\20\4\uffff\6\20\21\uffff\1\15\1\20",
            "\1\17\1\20\4\uffff\6\20\21\uffff\1\15\1\20",
            "\1\17\1\20\4\uffff\6\20\21\uffff\1\15\1\20",
            "\1\17\1\20\4\uffff\6\20\21\uffff\1\15\1\20",
            "\1\17\1\20\4\uffff\6\20\21\uffff\1\15\1\20",
            "\1\21\31\uffff\1\22\1\23\1\24\1\25\1\26",
            "",
            "",
            "\1\17\34\uffff\1\15",
            "\1\17\34\uffff\1\15",
            "",
            "",
            "",
            "",
            "\1\32\23\uffff\1\31\1\27\1\30",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\33\31\uffff\1\34\1\35\1\36\1\37\1\40",
            "\1\17\34\uffff\1\15",
            "\1\32\23\uffff\1\31\1\41\1\42",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31"
    };

    static final short[] DFA37_eot = DFA.unpackEncodedString(DFA37_eotS);
    static final short[] DFA37_eof = DFA.unpackEncodedString(DFA37_eofS);
    static final char[] DFA37_min = DFA.unpackEncodedStringToUnsignedChars(DFA37_minS);
    static final char[] DFA37_max = DFA.unpackEncodedStringToUnsignedChars(DFA37_maxS);
    static final short[] DFA37_accept = DFA.unpackEncodedString(DFA37_acceptS);
    static final short[] DFA37_special = DFA.unpackEncodedString(DFA37_specialS);
    static final short[][] DFA37_transition;

    static {
        int numStates = DFA37_transitionS.length;
        DFA37_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA37_transition[i] = DFA.unpackEncodedString(DFA37_transitionS[i]);
        }
    }

    class DFA37 extends DFA {

        public DFA37(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 37;
            this.eot = DFA37_eot;
            this.eof = DFA37_eof;
            this.min = DFA37_min;
            this.max = DFA37_max;
            this.accept = DFA37_accept;
            this.special = DFA37_special;
            this.transition = DFA37_transition;
        }
        public String getDescription() {
            return "333:1: csPattern : ( IDENTIFIER AT csPattern -> ^( Cst_AnnotatedPattern csPattern IDENTIFIER ) | ANTI csPattern -> ^( Cst_Anti csPattern ) | csHeadSymbolList csExplicitTermList -> ^( Cst_Appl csHeadSymbolList csExplicitTermList ) | csHeadSymbolList csImplicitPairList -> ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList ) | IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_VariableStar IDENTIFIER ) -> ^( Cst_Variable IDENTIFIER ) | UNDERSCORE (s= STAR )? -> {s!=null}? ^( Cst_UnamedVariableStar ) -> ^( Cst_UnamedVariable ) | csConstantValue (s= STAR )? -> {s!=null}? ^( Cst_ConstantStar csConstantValue ) -> ^( Cst_Constant csConstantValue ) );";
        }
    }
    static final String DFA40_eotS =
        "\12\uffff";
    static final String DFA40_eofS =
        "\1\uffff\1\11\10\uffff";
    static final String DFA40_minS =
        "\1\u016c\1\u016e\10\uffff";
    static final String DFA40_maxS =
        "\1\u018a\1\u018b\10\uffff";
    static final String DFA40_acceptS =
        "\2\uffff\1\4\1\5\1\6\1\7\1\10\1\2\1\3\1\1";
    static final String DFA40_specialS =
        "\12\uffff}>";
    static final String[] DFA40_transitionS = {
            "\1\1\31\uffff\1\2\1\3\1\4\1\5\1\6",
            "\2\11\23\uffff\1\11\1\7\1\10\5\uffff\1\11",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA40_eot = DFA.unpackEncodedString(DFA40_eotS);
    static final short[] DFA40_eof = DFA.unpackEncodedString(DFA40_eofS);
    static final char[] DFA40_min = DFA.unpackEncodedStringToUnsignedChars(DFA40_minS);
    static final char[] DFA40_max = DFA.unpackEncodedStringToUnsignedChars(DFA40_maxS);
    static final short[] DFA40_accept = DFA.unpackEncodedString(DFA40_acceptS);
    static final short[] DFA40_special = DFA.unpackEncodedString(DFA40_specialS);
    static final short[][] DFA40_transition;

    static {
        int numStates = DFA40_transitionS.length;
        DFA40_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA40_transition[i] = DFA.unpackEncodedString(DFA40_transitionS[i]);
        }
    }

    class DFA40 extends DFA {

        public DFA40(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 40;
            this.eot = DFA40_eot;
            this.eof = DFA40_eof;
            this.min = DFA40_min;
            this.max = DFA40_max;
            this.accept = DFA40_accept;
            this.special = DFA40_special;
            this.transition = DFA40_transition;
        }
        public String getDescription() {
            return "380:1: csHeadSymbol : ( IDENTIFIER -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) ) | IDENTIFIER QMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) ) | IDENTIFIER DQMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) ) | INTEGER -> ^( Cst_ConstantInt INTEGER ) | LONG -> ^( Cst_ConstantLong LONG ) | CHAR -> ^( Cst_ConstantChar CHAR ) | DOUBLE -> ^( Cst_ConstantDouble DOUBLE ) | STRING -> ^( Cst_ConstantString STRING ) );";
        }
    }
 

    public static final BitSet FOLLOW_LBR_in_csIncludeConstruct90 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csIncludeConstruct94 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csIncludeConstruct96 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csName_in_csStrategyConstruct129 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csStrategyConstruct131 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000900000000000L});
    public static final BitSet FOLLOW_csSlotList_in_csStrategyConstruct133 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csStrategyConstruct135 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_EXTENDS_in_csStrategyConstruct137 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_BQUOTE_in_csStrategyConstruct139 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_csStrategyConstruct142 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csStrategyConstruct145 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0004200000000000L});
    public static final BitSet FOLLOW_csStrategyVisitList_in_csStrategyConstruct147 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csStrategyConstruct149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csStrategyVisit_in_csStrategyVisitList251 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_VISIT_in_csStrategyVisit274 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csStrategyVisit276 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csStrategyVisit278 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002700000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csVisitAction_in_csStrategyVisit281 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002700000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_RBR_in_csStrategyVisit285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csVisitAction367 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_COLON_in_csVisitAction371 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csExtendedConstraint_in_csVisitAction375 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_ARROW_in_csVisitAction377 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csVisitAction379 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csVisitAction381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csVisitAction602 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_COLON_in_csVisitAction606 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csExtendedConstraint_in_csVisitAction610 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_ARROW_in_csVisitAction612 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_csVisitAction614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csMatchConstruct972 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_csMatchConstruct974 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_COMMA_in_csMatchConstruct980 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_csMatchConstruct982 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csMatchConstruct989 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csMatchConstruct991 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002700000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csExtendedConstraintAction_in_csMatchConstruct993 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002700000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_RBR_in_csMatchConstruct996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csMatchConstruct1051 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csMatchConstruct1053 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csMatchConstruct1057 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002700000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csConstraintAction_in_csMatchConstruct1059 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002700000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_RBR_in_csMatchConstruct1062 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csConstraintAction1118 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_COLON_in_csConstraintAction1122 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csConstraint_in_csConstraintAction1126 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_ARROW_in_csConstraintAction1128 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csConstraintAction1130 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csConstraintAction1132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csExtendedConstraintAction1305 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_COLON_in_csExtendedConstraintAction1309 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csExtendedConstraint_in_csExtendedConstraintAction1313 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_ARROW_in_csExtendedConstraintAction1315 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csExtendedConstraintAction1317 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csExtendedConstraintAction1319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csBQTerm1498 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L});
    public static final BitSet FOLLOW_BQUOTE_in_csBQTerm1502 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csBQTerm1507 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csBQTerm1509 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002900000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_csBQTerm1514 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_COMMA_in_csBQTerm1518 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_csBQTerm1522 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csBQTerm1529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csBQTerm1670 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L});
    public static final BitSet FOLLOW_BQUOTE_in_csBQTerm1674 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csBQTerm1679 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_STAR_in_csBQTerm1684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csConstantValue_in_csBQTerm1934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csMatchArgumentConstraintList_in_csExtendedConstraint1965 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0180000000000000L});
    public static final BitSet FOLLOW_AND_in_csExtendedConstraint1971 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_OR_in_csExtendedConstraint1975 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csConstraint_in_csExtendedConstraint1978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csMatchArgumentConstraint_in_csMatchArgumentConstraintList2031 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csMatchArgumentConstraintList2034 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csMatchArgumentConstraint_in_csMatchArgumentConstraintList2038 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csMatchArgumentConstraintList2043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csPattern_in_csMatchArgumentConstraint2085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csConstraint_priority1_in_csConstraint2108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csConstraint_priority2_in_csConstraint_priority12119 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_OR_in_csConstraint_priority12124 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csConstraint_priority2_in_csConstraint_priority12126 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_csConstraint_priority3_in_csConstraint_priority22157 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_AND_in_csConstraint_priority22162 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csConstraint_priority3_in_csConstraint_priority22164 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_csPattern_in_csConstraint_priority32197 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_LARROW_in_csConstraint_priority32199 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_csConstraint_priority32201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csTerm_in_csConstraint_priority32224 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0xFC00000000000000L});
    public static final BitSet FOLLOW_GREATERTHAN_in_csConstraint_priority32234 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_GREATEROREQU_in_csConstraint_priority32238 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_LOWERTHAN_in_csConstraint_priority32242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_LOWEROREQU_in_csConstraint_priority32251 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_DOUBLEEQUAL_in_csConstraint_priority32256 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_DIFFERENT_in_csConstraint_priority32261 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csTerm_in_csConstraint_priority32270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csConstraint_priority32400 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csConstraint_in_csConstraint_priority32402 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csConstraint_priority32404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csTerm2423 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_STAR_in_csTerm2428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csTerm2468 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csTerm2470 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000900000000000L});
    public static final BitSet FOLLOW_csTerm_in_csTerm2473 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_COMMA_in_csTerm2476 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csTerm_in_csTerm2478 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csTerm2484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csPattern2515 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_AT_in_csPattern2517 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csPattern_in_csPattern2519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ANTI_in_csPattern2539 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csPattern_in_csPattern2541 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csHeadSymbolList_in_csPattern2559 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_csExplicitTermList_in_csPattern2561 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csHeadSymbolList_in_csPattern2582 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_csImplicitPairList_in_csPattern2584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csPattern2607 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_STAR_in_csPattern2612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_csPattern2650 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_STAR_in_csPattern2655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csConstantValue_in_csPattern2690 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_STAR_in_csPattern2695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csHeadSymbol_in_csHeadSymbolList2745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csHeadSymbolList2760 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csHeadSymbol_in_csHeadSymbolList2762 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_PIPE_in_csHeadSymbolList2765 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csHeadSymbol_in_csHeadSymbolList2767 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_RPAR_in_csHeadSymbolList2771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csHeadSymbol2796 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csHeadSymbol2814 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_QMARK_in_csHeadSymbol2816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csHeadSymbol2834 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_DQMARK_in_csHeadSymbol2836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_csHeadSymbol2854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LONG_in_csHeadSymbol2868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_in_csHeadSymbol2882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_in_csHeadSymbol2896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_csHeadSymbol2910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_csConstantValue0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csExplicitTermList2953 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002D00000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csPattern_in_csExplicitTermList2956 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_COMMA_in_csExplicitTermList2959 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csPattern_in_csExplicitTermList2961 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csExplicitTermList2967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LSQUAREBR_in_csImplicitPairList2989 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_csPairPattern_in_csImplicitPairList2992 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_COMMA_in_csImplicitPairList2995 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csPairPattern_in_csImplicitPairList2997 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_RSQUAREBR_in_csImplicitPairList3004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csPairPattern3026 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_EQUAL_in_csPairPattern3028 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csPattern_in_csPairPattern3030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorConstruct3065 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csOperatorConstruct3069 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csOperatorConstruct3071 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000900000000000L});
    public static final BitSet FOLLOW_csSlotList_in_csOperatorConstruct3073 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csOperatorConstruct3075 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csOperatorConstruct3079 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x000000000007C000L});
    public static final BitSet FOLLOW_csKeywordIsFsym_in_csOperatorConstruct3091 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x000000000007C000L});
    public static final BitSet FOLLOW_csKeywordMake_in_csOperatorConstruct3102 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x000000000007C000L});
    public static final BitSet FOLLOW_csKeywordGetSlot_in_csOperatorConstruct3113 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x000000000007C000L});
    public static final BitSet FOLLOW_csKeywordGetDefault_in_csOperatorConstruct3124 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x000000000007C000L});
    public static final BitSet FOLLOW_csKeywordOpImplement_in_csOperatorConstruct3135 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x000000000007C000L});
    public static final BitSet FOLLOW_RBR_in_csOperatorConstruct3147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorArrayConstruct3228 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csOperatorArrayConstruct3232 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csOperatorArrayConstruct3234 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorArrayConstruct3238 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_STAR_in_csOperatorArrayConstruct3240 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csOperatorArrayConstruct3242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csOperatorArrayConstruct3246 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_csKeywordIsFsym_in_csOperatorArrayConstruct3257 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000007400000L});
    public static final BitSet FOLLOW_csKeywordMakeEmpty_Array_in_csOperatorArrayConstruct3266 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000007400000L});
    public static final BitSet FOLLOW_csKeywordMakeAppend_in_csOperatorArrayConstruct3275 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000007400000L});
    public static final BitSet FOLLOW_csKeywordGetElement_in_csOperatorArrayConstruct3284 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000007400000L});
    public static final BitSet FOLLOW_csKeywordGetSize_in_csOperatorArrayConstruct3293 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000007400000L});
    public static final BitSet FOLLOW_RBR_in_csOperatorArrayConstruct3303 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorListConstruct3389 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csOperatorListConstruct3393 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csOperatorListConstruct3395 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorListConstruct3399 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_STAR_in_csOperatorListConstruct3401 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csOperatorListConstruct3403 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csOperatorListConstruct3407 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_csKeywordIsFsym_in_csOperatorListConstruct3416 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000000F80000L});
    public static final BitSet FOLLOW_csKeywordMakeEmpty_List_in_csOperatorListConstruct3425 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000000F80000L});
    public static final BitSet FOLLOW_csKeywordMakeInsert_in_csOperatorListConstruct3434 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000000F80000L});
    public static final BitSet FOLLOW_csKeywordGetHead_in_csOperatorListConstruct3443 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000000F80000L});
    public static final BitSet FOLLOW_csKeywordGetTail_in_csOperatorListConstruct3452 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000000F80000L});
    public static final BitSet FOLLOW_csKeywordIsEmpty_in_csOperatorListConstruct3461 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000000F80000L});
    public static final BitSet FOLLOW_RBR_in_csOperatorListConstruct3471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csTypetermConstruct3558 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0001080000000000L});
    public static final BitSet FOLLOW_EXTENDS_in_csTypetermConstruct3561 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csTypetermConstruct3565 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csTypetermConstruct3571 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_csKeywordImplement_in_csTypetermConstruct3579 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000018000000L});
    public static final BitSet FOLLOW_csKeywordIsSort_in_csTypetermConstruct3584 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_csKeywordEquals_in_csTypetermConstruct3591 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csTypetermConstruct3597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csSlot_in_csSlotList3729 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csSlotList3732 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csSlot_in_csSlotList3734 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csSlot3764 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_COLON_in_csSlot3766 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csSlot3770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csSlot3798 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csSlot3802 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_IS_FSYM_in_csKeywordIsFsym3836 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordIsFsym3838 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordIsFsym3842 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordIsFsym3844 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordIsFsym3848 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordIsFsym3852 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_SLOT_in_csKeywordGetSlot3892 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetSlot3894 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetSlot3898 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordGetSlot3900 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetSlot3904 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetSlot3906 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetSlot3910 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetSlot3914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_in_csKeywordMake3957 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMake3959 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000900000000000L});
    public static final BitSet FOLLOW_csNameList_in_csKeywordMake3963 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMake3965 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMake3969 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMake3973 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_DEFAULT_in_csKeywordGetDefault4013 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetDefault4015 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetDefault4019 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetDefault4021 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetDefault4025 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetDefault4029 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_IMPLEMENT_in_csKeywordOpImplement4069 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordOpImplement4071 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordOpImplement4073 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordOpImplement4077 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordOpImplement4081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_HEAD_in_csKeywordGetHead4103 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetHead4105 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetHead4109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetHead4111 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetHead4116 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetHead4120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_TAIL_in_csKeywordGetTail4160 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetTail4162 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetTail4166 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetTail4168 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetTail4172 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetTail4176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_IS_EMPTY_in_csKeywordIsEmpty4216 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordIsEmpty4218 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordIsEmpty4222 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordIsEmpty4224 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordIsEmpty4228 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordIsEmpty4232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_EMPTY_in_csKeywordMakeEmpty_List4272 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMakeEmpty_List4274 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMakeEmpty_List4276 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMakeEmpty_List4280 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMakeEmpty_List4284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_EMPTY_in_csKeywordMakeEmpty_Array4321 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMakeEmpty_Array4323 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeEmpty_Array4327 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMakeEmpty_Array4329 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMakeEmpty_Array4333 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMakeEmpty_Array4337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_INSERT_in_csKeywordMakeInsert4377 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMakeInsert4383 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeInsert4387 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordMakeInsert4389 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeInsert4393 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMakeInsert4395 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMakeInsert4399 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMakeInsert4403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_ELEMENT_in_csKeywordGetElement4446 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetElement4452 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetElement4456 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordGetElement4458 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetElement4462 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetElement4464 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetElement4468 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetElement4472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_SIZE_in_csKeywordGetSize4515 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetSize4517 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetSize4521 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetSize4523 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetSize4527 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetSize4531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_APPEND_in_csKeywordMakeAppend4571 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMakeAppend4578 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeAppend4582 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordMakeAppend4584 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeAppend4588 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMakeAppend4590 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMakeAppend4594 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMakeAppend4598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_IMPLEMENT_in_csKeywordImplement4641 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordImplement4643 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordImplement4647 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_IS_SORT_in_csKeywordIsSort4669 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordIsSort4671 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordIsSort4675 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordIsSort4677 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordIsSort4683 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordIsSort4687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_EQUALS_in_csKeywordEquals4712 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordEquals4714 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordEquals4718 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordEquals4720 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csKeywordEquals4724 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordEquals4726 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordEquals4732 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordEquals4736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csName_in_csNameList4781 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csNameList4784 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csName_in_csNameList4786 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csName4812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BQUOTE_in_synpred1_miniTom139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred5_miniTom367 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_COLON_in_synpred5_miniTom371 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002500000000000L,0x00000000000007C6L});
    public static final BitSet FOLLOW_csExtendedConstraint_in_synpred5_miniTom375 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_ARROW_in_synpred5_miniTom377 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_LBR_in_synpred5_miniTom379 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_RBR_in_synpred5_miniTom381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred18_miniTom1498 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L});
    public static final BitSet FOLLOW_BQUOTE_in_synpred18_miniTom1502 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred18_miniTom1507 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_synpred18_miniTom1509 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002900000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_synpred18_miniTom1514 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_COMMA_in_synpred18_miniTom1518 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_synpred18_miniTom1522 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020800000000000L});
    public static final BitSet FOLLOW_RPAR_in_synpred18_miniTom1529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred19_miniTom1670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred22_miniTom1670 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L});
    public static final BitSet FOLLOW_BQUOTE_in_synpred22_miniTom1674 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred22_miniTom1679 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_STAR_in_synpred22_miniTom1684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csPattern_in_synpred29_miniTom2197 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_LARROW_in_synpred29_miniTom2199 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0002100000000000L,0x00000000000007C0L});
    public static final BitSet FOLLOW_csBQTerm_in_synpred29_miniTom2201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csTerm_in_synpred35_miniTom2224 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0xFC00000000000000L});
    public static final BitSet FOLLOW_GREATERTHAN_in_synpred35_miniTom2234 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_GREATEROREQU_in_synpred35_miniTom2238 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_LOWERTHAN_in_synpred35_miniTom2242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_LOWEROREQU_in_synpred35_miniTom2251 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_DOUBLEEQUAL_in_synpred35_miniTom2256 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_DIFFERENT_in_synpred35_miniTom2261 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_csTerm_in_synpred35_miniTom2270 = new BitSet(new long[]{0x0000000000000002L});

}