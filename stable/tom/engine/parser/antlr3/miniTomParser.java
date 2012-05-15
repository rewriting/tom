// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g 2012-04-23 18:26:10

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Code", "IsSort", "DoWhileExpression", "ACMatchLoop", "ConstraintToExpression", "GetSliceArray", "GetSliceList", "GetElement", "GetSize", "Substract", "SubstractOne", "AddOne", "IsEmptyArray", "IsEmptyList", "GetTail", "GetHead", "IsFsym", "GetSlot", "Cast", "EqualBQTerm", "EqualTerm", "Integer", "FalseTL", "TrueTL", "Conditional", "AntiMatchExpression", "LessOrEqualThan", "LessThan", "GreaterOrEqualThan", "GreaterThan", "OrConnector", "OrExpressionDisjunction", "Or", "And", "Negation", "Bottom", "TomInstructionToExpression", "BQTermToExpression", "Cst_ConstantString", "Cst_ConstantDouble", "Cst_ConstantChar", "Cst_ConstantLong", "Cst_ConstantInt", "Cst_Symbol", "Cst_TheoryDEFAULT", "Cst_TheoryAC", "Cst_TheoryAU", "ConcCstSymbol", "Cst_Slot", "Cst_StrategyConstruct", "Cst_BQTermToBlock", "Cst_IncludeConstruct", "Cst_MetaQuoteConstruct", "Cst_TypetermConstruct", "Cst_OpListConstruct", "Cst_OpArrayConstruct", "Cst_OpConstruct", "Cst_MatchConstruct", "HOSTBLOCK", "Cst_PairPattern", "ConcCstPairPattern", "Cst_TermAppl", "Cst_TermVariableStar", "Cst_TermVariable", "ConcCstConstraint", "Cst_ConstraintAction", "Cst_OriginTracking", "Cst_Label", "NodeString", "ConcCstVisit", "ConcCstConstraintAction", "Cst_RecordAppl", "Cst_Appl", "Cst_UnamedVariableStar", "Cst_UnamedVariable", "Cst_ConstantStar", "Cst_Constant", "Cst_VariableStar", "Cst_Variable", "Cst_AnnotatedPattern", "Cst_Anti", "ConcCstBQTerm", "ConcCstTerm", "ConcCstOperator", "ConcCstName", "ConcCstPattern", "ConcCstOption", "ConcCstPairSlotBQTerm", "Cst_Program", "Cst_TypeUnknown", "Cst_Type", "Cst_PairSlotBQTerm", "Cst_VisitTerm", "Cst_Equals", "Cst_IsSort", "Cst_Implement", "Cst_MakeEmptyList", "Cst_Make", "Cst_MakeAppend", "Cst_GetElement", "Cst_MakeInsert", "Cst_GetSlot", "Cst_GetDefault", "Cst_GetTail", "Cst_GetSize", "Cst_MakeEmptyArray", "Cst_IsEmpty", "Cst_GetHead", "Cst_IsFsym", "NodeInt", "ConcCstBlock", "Cst_EmptyName", "Cst_Name", "ConcCstSlot", "Cst_NumDifferent", "Cst_NumEqualTo", "Cst_NumGreaterOrEqualTo", "Cst_NumGreaterThan", "Cst_NumLessOrEqualTo", "Cst_NumLessThan", "Cst_OrConstraint", "Cst_AndConstraint", "Cst_MatchArgumentConstraint", "Cst_MatchTermConstraint", "Cst_BQConstant", "Cst_ITL", "Cst_BQComposite", "Cst_BQDefault", "Cst_BQVarStar", "Cst_BQVar", "Cst_BQRecordAppl", "Cst_BQAppl", "AntiName", "EmptyName", "PositionName", "Name", "concTomNumber", "concTomName", "NameNumber", "RenamedVar", "AbsVar", "Position", "Save", "End", "Begin", "IndexNumber", "ListNumber", "PatternNumber", "MatchNumber", "UsedType", "UsedSymbolAC", "UsedSymbolDestructor", "UsedSymbolConstructor", "VisitTerm", "TextPosition", "concTomSymbol", "concTomVisit", "Entry", "concTomEntry", "StructTable", "Symbol", "EmptySymbol", "Table", "TypeForVariable", "DefinedSymbol", "noOption", "ACSymbol", "ImplicitXMLChild", "ImplicitXMLAttribut", "ModuleName", "Debug", "Label", "MatchingTheory", "OriginalText", "OriginTracking", "GeneratedMatch", "TomTermToOption", "TomNameToOption", "DeclarationToOption", "concOption", "concConstraintInstruction", "concInstruction", "RawAction", "CompiledPattern", "CompiledMatch", "Match", "NamedBlock", "UnamedBlock", "AbstractBlock", "Nop", "Return", "AssignArray", "Assign", "LetRef", "Let", "WhileDo", "DoWhile", "If", "CodeToInstruction", "ExpressionToInstruction", "BQTermToInstruction", "ConstraintInstruction", "EmptyTargetLanguageType", "TLType", "concTypeOption", "concTomType", "TypeVar", "EmptyType", "TypesToType", "Codomain", "Type", "SubtypeDecl", "WithSymbol", "IntegerPattern", "FalsePattern", "TruePattern", "TestVar", "Automata", "AntiTerm", "TomSymbolToTomTerm", "VariableStar", "Variable", "XMLAppl", "RecordAppl", "TermAppl", "concTomTerm", "concConstraint", "EmptyArrayConstraint", "EmptyListConstraint", "NumericConstraint", "AntiMatchConstraint", "MatchConstraint", "OrConstraintDisjunction", "OrConstraint", "AndConstraint", "IsSortConstraint", "Negate", "FalseConstraint", "TrueConstraint", "AssignPositionTo", "AliasTo", "NumEqual", "NumDifferent", "NumGreaterOrEqualThan", "NumGreaterThan", "NumLessOrEqualThan", "NumLessThan", "TomInclude", "Tom", "BQTermToCode", "DeclarationToCode", "InstructionToCode", "TargetLanguageToCode", "noTL", "Comment", "ITL", "TL", "ListTail", "ListHead", "VariableHeadArray", "VariableHeadList", "Subterm", "SymbolOf", "ExpressionToBQTerm", "BuildAppendArray", "BuildConsArray", "BuildEmptyArray", "BuildAppendList", "BuildConsList", "BuildEmptyList", "BuildTerm", "BuildConstant", "FunctionCall", "Composite", "BQDefault", "BQVariableStar", "BQVariable", "BQRecordAppl", "BQAppl", "concBQTerm", "CompositeTL", "CompositeBQTerm", "concCode", "AU", "AC", "Unitary", "Associative", "Syntactic", "concElementaryTheory", "PairNameOptions", "concTypeConstraint", "FalseTypeConstraint", "Subtype", "Equation", "concDeclaration", "ACSymbolDecl", "AbstractDecl", "EmptyDeclaration", "ArraySymbolDecl", "ListSymbolDecl", "SymbolDecl", "IntrospectorClass", "Class", "MethodDef", "FunctionDef", "Strategy", "MakeDecl", "MakeAddArray", "MakeEmptyArray", "GetSizeDecl", "GetElementDecl", "MakeAddList", "MakeEmptyList", "IsEmptyDecl", "GetTailDecl", "GetHeadDecl", "IsSortDecl", "EqualTermDecl", "GetDefaultDecl", "GetSlotDecl", "IsFsymDecl", "TypeTermDecl", "concPairNameDecl", "PairNameDecl", "PairSlotBQTerm", "concSlot", "concBQSlot", "PairSlotAppl", "LBR", "IDENTIFIER", "RBR", "LPAR", "RPAR", "EXTENDS", "BQUOTE", "VISIT", "COLON", "ARROW", "COMMA", "STAR", "AND", "OR", "GREATERTHAN", "GREATEROREQU", "LOWERTHAN", "LOWEROREQU", "DOUBLEEQUAL", "DIFFERENT", "LARROW", "AT", "ANTI", "UNDERSCORE", "PIPE", "QMARK", "DQMARK", "INTEGER", "LONG", "CHAR", "DOUBLE", "STRING", "LSQUAREBR", "RSQUAREBR", "EQUAL", "KEYWORD_IS_FSYM", "KEYWORD_GET_SLOT", "KEYWORD_MAKE", "KEYWORD_GET_DEFAULT", "KEYWORD_GET_HEAD", "KEYWORD_GET_TAIL", "KEYWORD_IS_EMPTY", "KEYWORD_MAKE_EMPTY", "KEYWORD_MAKE_INSERT", "KEYWORD_GET_ELEMENT", "KEYWORD_GET_SIZE", "KEYWORD_MAKE_APPEND", "KEYWORD_IMPLEMENT", "KEYWORD_IS_SORT", "KEYWORD_EQUALS", "DQUOTE", "SQUOTE", "LETTER", "DIGIT", "MINUS", "UNSIGNED_DOUBLE", "LONG_SUFFIX", "ID_MINUS", "ALL_ID", "HEX_DIGIT", "ESC", "WS", "SL_COMMENT", "ML_COMMENT", "DEFAULT"
    };
    public static final int concInstruction=191;
    public static final int EmptyTargetLanguageType=212;
    public static final int Cst_MatchTermConstraint=133;
    public static final int MakeEmptyList=322;
    public static final int Cst_Label=77;
    public static final int RSQUAREBR=371;
    public static final int NumLessOrEqualThan=255;
    public static final int GetHead=25;
    public static final int KEYWORD_MAKE_EMPTY=380;
    public static final int GREATEROREQU=353;
    public static final int KEYWORD_GET_ELEMENT=382;
    public static final int UsedSymbolConstructor=162;
    public static final int VariableStar=230;
    public static final int Cst_RecordAppl=81;
    public static final int GetSlot=27;
    public static final int Cst_MakeEmptyArray=115;
    public static final int EmptySymbol=171;
    public static final int KEYWORD_GET_TAIL=378;
    public static final int GetDefaultDecl=328;
    public static final int Cst_Symbol=53;
    public static final int Automata=227;
    public static final int BQRecordAppl=287;
    public static final int Conditional=34;
    public static final int Cst_OriginTracking=76;
    public static final int Cst_NumGreaterOrEqualTo=126;
    public static final int FalseTL=32;
    public static final int Cst_UnamedVariableStar=83;
    public static final int ACSymbolDecl=305;
    public static final int UNDERSCORE=361;
    public static final int Cst_Name=122;
    public static final int ANTI=360;
    public static final int AndConstraint=244;
    public static final int NamedBlock=196;
    public static final int KEYWORD_MAKE_INSERT=381;
    public static final int CompiledPattern=193;
    public static final int Strategy=315;
    public static final int Cst_TypetermConstruct=63;
    public static final int LOWERTHAN=354;
    public static final int SL_COMMENT=400;
    public static final int MakeAddArray=317;
    public static final int ConcCstSymbol=57;
    public static final int KEYWORD_MAKE_APPEND=384;
    public static final int EmptyDeclaration=307;
    public static final int CodeToInstruction=208;
    public static final int Cst_NumLessThan=129;
    public static final int BuildConsArray=275;
    public static final int ConcCstPairSlotBQTerm=97;
    public static final int Codomain=219;
    public static final int FalseTypeConstraint=301;
    public static final int GreaterThan=39;
    public static final int CompositeBQTerm=291;
    public static final int Cst_UnamedVariable=84;
    public static final int AT=359;
    public static final int AU=293;
    public static final int Cst_TheoryDEFAULT=54;
    public static final int BuildEmptyArray=276;
    public static final int AC=294;
    public static final int ConstraintToExpression=14;
    public static final int GetTail=24;
    public static final int OrExpressionDisjunction=41;
    public static final int Return=200;
    public static final int Cst_GetTail=113;
    public static final int VariableHeadArray=269;
    public static final int MatchNumber=158;
    public static final int Cst_BQVarStar=138;
    public static final int TrueConstraint=248;
    public static final int KEYWORD_EQUALS=387;
    public static final int DQMARK=364;
    public static final int TextPosition=164;
    public static final int DeclarationToOption=188;
    public static final int Cst_IsEmpty=116;
    public static final int HEX_DIGIT=397;
    public static final int ListTail=267;
    public static final int Cst_Variable=88;
    public static final int MINUS=392;
    public static final int SQUOTE=389;
    public static final int ArraySymbolDecl=308;
    public static final int concTypeOption=214;
    public static final int Cst_BQAppl=141;
    public static final int Cst_AnnotatedPattern=89;
    public static final int COLON=346;
    public static final int GetSizeDecl=319;
    public static final int Match=195;
    public static final int TomInclude=257;
    public static final int concTomVisit=166;
    public static final int Cst_MakeEmptyList=106;
    public static final int BQTermToExpression=47;
    public static final int ARROW=347;
    public static final int KEYWORD_GET_DEFAULT=376;
    public static final int GetHeadDecl=325;
    public static final int AbstractBlock=198;
    public static final int Position=151;
    public static final int LONG=366;
    public static final int LessThan=37;
    public static final int Cst_Implement=105;
    public static final int Cst_TermVariable=73;
    public static final int CHAR=367;
    public static final int DIFFERENT=357;
    public static final int concCode=292;
    public static final int Cst_GetSlot=111;
    public static final int Cst_GetHead=117;
    public static final int Cst_BQConstant=134;
    public static final int Cst_Anti=90;
    public static final int noTL=263;
    public static final int FalseConstraint=247;
    public static final int Entry=167;
    public static final int Let=204;
    public static final int LPAR=341;
    public static final int Cst_ITL=135;
    public static final int Cst_OrConstraint=130;
    public static final int NodeString=78;
    public static final int BQTermToInstruction=210;
    public static final int DOUBLE=368;
    public static final int XMLAppl=232;
    public static final int VISIT=345;
    public static final int CompositeTL=290;
    public static final int Cst_Program=98;
    public static final int Table=172;
    public static final int concBQTerm=289;
    public static final int TruePattern=225;
    public static final int GetSlotDecl=329;
    public static final int TypeForVariable=173;
    public static final int concConstraint=236;
    public static final int OR=351;
    public static final int Cst_Make=107;
    public static final int ConcCstBlock=120;
    public static final int Cst_Type=100;
    public static final int Cst_Appl=82;
    public static final int NumGreaterThan=254;
    public static final int BQVariableStar=285;
    public static final int IsSort=11;
    public static final int AssignArray=201;
    public static final int Unitary=295;
    public static final int Cst_ConstraintAction=75;
    public static final int BuildConstant=281;
    public static final int RBR=340;
    public static final int OrConstraint=243;
    public static final int AntiMatchConstraint=240;
    public static final int KEYWORD_GET_SIZE=383;
    public static final int LARROW=358;
    public static final int EqualBQTerm=29;
    public static final int VariableHeadList=270;
    public static final int Cst_PairPattern=69;
    public static final int UsedType=159;
    public static final int Begin=154;
    public static final int LBR=338;
    public static final int TomNameToOption=187;
    public static final int PositionName=144;
    public static final int RawAction=192;
    public static final int ImplicitXMLAttribut=178;
    public static final int WithSymbol=222;
    public static final int Cst_TermAppl=71;
    public static final int COMMA=348;
    public static final int EQUAL=372;
    public static final int Cst_BQTermToBlock=60;
    public static final int ImplicitXMLChild=177;
    public static final int SubtypeDecl=221;
    public static final int Cst_BQRecordAppl=140;
    public static final int DIGIT=391;
    public static final int Cst_NumGreaterThan=127;
    public static final int ACMatchLoop=13;
    public static final int ExpressionToInstruction=209;
    public static final int Cst_EmptyName=121;
    public static final int concTomTerm=235;
    public static final int If=207;
    public static final int Negation=44;
    public static final int RecordAppl=233;
    public static final int Cst_Equals=103;
    public static final int HOSTBLOCK=68;
    public static final int Type=220;
    public static final int Cst_ConstantStar=85;
    public static final int DEFAULT=402;
    public static final int IsEmptyList=23;
    public static final int AliasTo=250;
    public static final int MakeEmptyArray=318;
    public static final int BQAppl=288;
    public static final int Cst_Slot=58;
    public static final int Cst_NumLessOrEqualTo=128;
    public static final int BuildConsList=278;
    public static final int ConcCstBQTerm=91;
    public static final int Cst_Constant=86;
    public static final int OriginalText=183;
    public static final int Cst_OpConstruct=66;
    public static final int DeclarationToCode=260;
    public static final int FalsePattern=224;
    public static final int ModuleName=179;
    public static final int ConcCstPairPattern=70;
    public static final int ALL_ID=396;
    public static final int Cst_BQComposite=136;
    public static final int DoWhileExpression=12;
    public static final int UNSIGNED_DOUBLE=393;
    public static final int ConcCstVisit=79;
    public static final int AssignPositionTo=249;
    public static final int TypeVar=216;
    public static final int STAR=349;
    public static final int FunctionDef=314;
    public static final int KEYWORD_IS_EMPTY=379;
    public static final int concDeclaration=304;
    public static final int LETTER=390;
    public static final int BQDefault=284;
    public static final int ListSymbolDecl=309;
    public static final int noOption=175;
    public static final int DQUOTE=388;
    public static final int Cst_AndConstraint=131;
    public static final int Cst_OpListConstruct=64;
    public static final int EOF=-1;
    public static final int concSlot=335;
    public static final int SymbolOf=272;
    public static final int PairNameDecl=333;
    public static final int UnamedBlock=197;
    public static final int IsEmptyDecl=323;
    public static final int concTomNumber=146;
    public static final int KEYWORD_GET_HEAD=377;
    public static final int CompiledMatch=194;
    public static final int Cst_StrategyConstruct=59;
    public static final int Class=312;
    public static final int MethodDef=313;
    public static final int Cst_IsSort=104;
    public static final int KEYWORD_GET_SLOT=374;
    public static final int MakeAddList=321;
    public static final int Code=10;
    public static final int AntiMatchExpression=35;
    public static final int concTypeConstraint=300;
    public static final int ListHead=268;
    public static final int SubstractOne=20;
    public static final int WS=399;
    public static final int Equation=303;
    public static final int Debug=180;
    public static final int TomInstructionToExpression=46;
    public static final int TLType=213;
    public static final int AntiName=142;
    public static final int AbsVar=150;
    public static final int GetSliceList=16;
    public static final int Label=181;
    public static final int concBQSlot=336;
    public static final int IsSortConstraint=245;
    public static final int DoWhile=206;
    public static final int Cst_GetSize=114;
    public static final int PairSlotBQTerm=334;
    public static final int Cst_ConstantChar=50;
    public static final int KEYWORD_MAKE=375;
    public static final int LetRef=203;
    public static final int GetSize=18;
    public static final int IsSortDecl=326;
    public static final int IsEmptyArray=22;
    public static final int Assign=202;
    public static final int AddOne=21;
    public static final int concTomName=147;
    public static final int StructTable=169;
    public static final int BQVariable=286;
    public static final int TypeTermDecl=331;
    public static final int NumDifferent=252;
    public static final int concOption=189;
    public static final int MatchConstraint=241;
    public static final int Cst_OpArrayConstruct=65;
    public static final int NodeInt=119;
    public static final int LOWEROREQU=355;
    public static final int Subtype=302;
    public static final int WhileDo=205;
    public static final int Cst_MatchConstruct=67;
    public static final int PIPE=362;
    public static final int EqualTerm=30;
    public static final int GetSliceArray=15;
    public static final int Or=42;
    public static final int ConcCstName=94;
    public static final int BuildEmptyList=279;
    public static final int Composite=283;
    public static final int INTEGER=365;
    public static final int concPairNameDecl=332;
    public static final int TL=266;
    public static final int Cst_BQVar=139;
    public static final int UsedSymbolAC=160;
    public static final int Cst_VisitTerm=102;
    public static final int OriginTracking=184;
    public static final int OrConstraintDisjunction=242;
    public static final int EmptyArrayConstraint=237;
    public static final int Integer=31;
    public static final int Save=152;
    public static final int STRING=369;
    public static final int ConcCstPattern=95;
    public static final int concElementaryTheory=298;
    public static final int KEYWORD_IS_SORT=386;
    public static final int LessOrEqualThan=36;
    public static final int Associative=296;
    public static final int ConcCstConstraint=74;
    public static final int ConcCstOption=96;
    public static final int Cst_ConstantString=48;
    public static final int Cst_NumDifferent=124;
    public static final int GeneratedMatch=185;
    public static final int Cst_GetDefault=112;
    public static final int ConcCstSlot=123;
    public static final int ListNumber=156;
    public static final int EmptyName=143;
    public static final int ConcCstConstraintAction=80;
    public static final int Negate=246;
    public static final int IntrospectorClass=311;
    public static final int ConstraintInstruction=211;
    public static final int RenamedVar=149;
    public static final int BuildAppendList=277;
    public static final int Variable=231;
    public static final int MatchingTheory=182;
    public static final int Tom=258;
    public static final int Comment=264;
    public static final int AbstractDecl=306;
    public static final int Cst_MakeInsert=110;
    public static final int IsFsym=26;
    public static final int EqualTermDecl=327;
    public static final int Bottom=45;
    public static final int ID_MINUS=395;
    public static final int GetElement=17;
    public static final int Cst_ConstantDouble=49;
    public static final int ConcCstOperator=93;
    public static final int TargetLanguageToCode=262;
    public static final int GREATERTHAN=352;
    public static final int Cst_GetElement=109;
    public static final int AntiTerm=228;
    public static final int Cst_TypeUnknown=99;
    public static final int concConstraintInstruction=190;
    public static final int GetElementDecl=320;
    public static final int concTomEntry=168;
    public static final int LONG_SUFFIX=394;
    public static final int ConcCstTerm=92;
    public static final int BuildTerm=280;
    public static final int LSQUAREBR=370;
    public static final int concTomSymbol=165;
    public static final int ITL=265;
    public static final int Cst_MakeAppend=108;
    public static final int NameNumber=148;
    public static final int GetTailDecl=324;
    public static final int UsedSymbolDestructor=161;
    public static final int VisitTerm=163;
    public static final int PairSlotAppl=337;
    public static final int OrConnector=40;
    public static final int Cst_ConstantLong=51;
    public static final int SymbolDecl=310;
    public static final int QMARK=363;
    public static final int Syntactic=297;
    public static final int End=153;
    public static final int PairNameOptions=299;
    public static final int ESC=398;
    public static final int Cst_BQDefault=137;
    public static final int IntegerPattern=223;
    public static final int BuildAppendArray=274;
    public static final int ExpressionToBQTerm=273;
    public static final int Cast=28;
    public static final int AND=350;
    public static final int PatternNumber=157;
    public static final int ML_COMMENT=401;
    public static final int NumGreaterOrEqualThan=253;
    public static final int Cst_PairSlotBQTerm=101;
    public static final int Cst_IsFsym=118;
    public static final int Cst_NumEqualTo=125;
    public static final int IndexNumber=155;
    public static final int NumEqual=251;
    public static final int IDENTIFIER=339;
    public static final int FunctionCall=282;
    public static final int Cst_MatchArgumentConstraint=132;
    public static final int TomTermToOption=186;
    public static final int KEYWORD_IS_FSYM=373;
    public static final int IsFsymDecl=330;
    public static final int TestVar=226;
    public static final int Cst_VariableStar=87;
    public static final int Symbol=170;
    public static final int DOUBLEEQUAL=356;
    public static final int EmptyType=217;
    public static final int And=43;
    public static final int KEYWORD_IMPLEMENT=385;
    public static final int Substract=19;
    public static final int Cst_IncludeConstruct=61;
    public static final int Cst_TheoryAC=55;
    public static final int MakeDecl=316;
    public static final int TermAppl=234;
    public static final int TomSymbolToTomTerm=229;
    public static final int DefinedSymbol=174;
    public static final int BQTermToCode=259;
    public static final int TrueTL=33;
    public static final int Cst_TheoryAU=56;
    public static final int GreaterOrEqualThan=38;
    public static final int TypesToType=218;
    public static final int ACSymbol=176;
    public static final int NumLessThan=256;
    public static final int Cst_ConstantInt=52;
    public static final int concTomType=215;
    public static final int Cst_TermVariableStar=72;
    public static final int Subterm=271;
    public static final int InstructionToCode=261;
    public static final int Name=145;
    public static final int Nop=199;
    public static final int BQUOTE=344;
    public static final int EmptyListConstraint=238;
    public static final int RPAR=342;
    public static final int EXTENDS=343;
    public static final int NumericConstraint=239;
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
    public String getGrammarFileName() { return "/Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g"; }



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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:88:1: csIncludeConstruct returns [int marker] : LBR filename= IDENTIFIER RBR -> ^( Cst_IncludeConstruct $filename) ;
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:89:22: ( LBR filename= IDENTIFIER RBR -> ^( Cst_IncludeConstruct $filename) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:91:20: LBR filename= IDENTIFIER RBR
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
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:96:6: ^( Cst_IncludeConstruct $filename)
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:100:1: csStrategyConstruct returns [int marker] : csName LPAR csSlotList RPAR EXTENDS ( BQUOTE )? csBQTerm LBR csStrategyVisitList RBR -> ^( Cst_StrategyConstruct csName csSlotList csBQTerm csStrategyVisitList ) ;
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:101:21: ( csName LPAR csSlotList RPAR EXTENDS ( BQUOTE )? csBQTerm LBR csStrategyVisitList RBR -> ^( Cst_StrategyConstruct csName csSlotList csBQTerm csStrategyVisitList ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:102:1: csName LPAR csSlotList RPAR EXTENDS ( BQUOTE )? csBQTerm LBR csStrategyVisitList RBR
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

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:102:37: ( BQUOTE )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==BQUOTE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:0:0: BQUOTE
                    {
                    BQUOTE8=(Token)match(input,BQUOTE,FOLLOW_BQUOTE_in_csStrategyConstruct139); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQUOTE.add(BQUOTE8);


                    }
                    break;

            }

            pushFollow(FOLLOW_csBQTerm_in_csStrategyConstruct142);
            csBQTerm9=csBQTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csBQTerm.add(csBQTerm9.getTree());
            LBR10=(Token)match(input,LBR,FOLLOW_LBR_in_csStrategyConstruct144); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR10);

            pushFollow(FOLLOW_csStrategyVisitList_in_csStrategyConstruct146);
            csStrategyVisitList11=csStrategyVisitList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csStrategyVisitList.add(csStrategyVisitList11.getTree());
            RBR12=(Token)match(input,RBR,FOLLOW_RBR_in_csStrategyConstruct148); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR12);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR12).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: csSlotList, csBQTerm, csName, csStrategyVisitList
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
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:106:6: ^( Cst_StrategyConstruct csName csSlotList csBQTerm csStrategyVisitList )
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:115:1: csStrategyVisitList : ( csStrategyVisit )* -> ^( ConcCstVisit ( csStrategyVisit )* ) ;
    public final miniTomParser.csStrategyVisitList_return csStrategyVisitList() throws RecognitionException {
        miniTomParser.csStrategyVisitList_return retval = new miniTomParser.csStrategyVisitList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        miniTomParser.csStrategyVisit_return csStrategyVisit13 = null;


        RewriteRuleSubtreeStream stream_csStrategyVisit=new RewriteRuleSubtreeStream(adaptor,"rule csStrategyVisit");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:115:21: ( ( csStrategyVisit )* -> ^( ConcCstVisit ( csStrategyVisit )* ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:116:3: ( csStrategyVisit )*
            {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:116:3: ( csStrategyVisit )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==VISIT) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:0:0: csStrategyVisit
            	    {
            	    pushFollow(FOLLOW_csStrategyVisit_in_csStrategyVisitList250);
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
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:116:23: ^( ConcCstVisit ( csStrategyVisit )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstVisit, "ConcCstVisit"), root_1);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:116:38: ( csStrategyVisit )*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:119:1: csStrategyVisit : VISIT IDENTIFIER LBR ( csVisitAction )* RBR -> ^( Cst_VisitTerm ^( Cst_Type IDENTIFIER ) ^( ConcCstConstraintAction ( csVisitAction )* ) ) ;
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:119:17: ( VISIT IDENTIFIER LBR ( csVisitAction )* RBR -> ^( Cst_VisitTerm ^( Cst_Type IDENTIFIER ) ^( ConcCstConstraintAction ( csVisitAction )* ) ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:120:3: VISIT IDENTIFIER LBR ( csVisitAction )* RBR
            {
            VISIT14=(Token)match(input,VISIT,FOLLOW_VISIT_in_csStrategyVisit273); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_VISIT.add(VISIT14);

            IDENTIFIER15=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csStrategyVisit277); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER15);

            LBR16=(Token)match(input,LBR,FOLLOW_LBR_in_csStrategyVisit279); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR16);

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:120:35: ( csVisitAction )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==IDENTIFIER||LA3_0==LPAR||(LA3_0>=ANTI && LA3_0<=UNDERSCORE)||(LA3_0>=INTEGER && LA3_0<=STRING)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:120:36: csVisitAction
            	    {
            	    pushFollow(FOLLOW_csVisitAction_in_csStrategyVisit282);
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

            RBR18=(Token)match(input,RBR,FOLLOW_RBR_in_csStrategyVisit286); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR18);



            // AST REWRITE
            // elements: IDENTIFIER, csVisitAction
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
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:121:8: ^( Cst_VisitTerm ^( Cst_Type IDENTIFIER ) ^( ConcCstConstraintAction ( csVisitAction )* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_VisitTerm, "Cst_VisitTerm"), root_1);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:122:11: ^( Cst_Type IDENTIFIER )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_IDENTIFIER.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:123:11: ^( ConcCstConstraintAction ( csVisitAction )* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstConstraintAction, "ConcCstConstraintAction"), root_2);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:123:37: ( csVisitAction )*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:128:1: csVisitAction : ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) | ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW csBQTerm -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ) ) );
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:128:15: ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) | ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW csBQTerm -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ) ) )
            int alt6=2;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR
                    {
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( IDENTIFIER l= COLON )?
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
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:129:4: IDENTIFIER l= COLON
                            {
                            IDENTIFIER19=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csVisitAction370); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER19);

                            l=(Token)match(input,COLON,FOLLOW_COLON_in_csVisitAction374); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COLON.add(l);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_csExtendedConstraint_in_csVisitAction378);
                    csExtendedConstraint20=csExtendedConstraint();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csExtendedConstraint.add(csExtendedConstraint20.getTree());
                    ARROW21=(Token)match(input,ARROW,FOLLOW_ARROW_in_csVisitAction380); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARROW.add(ARROW21);

                    LBR22=(Token)match(input,LBR,FOLLOW_LBR_in_csVisitAction382); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBR.add(LBR22);

                    RBR23=(Token)match(input,RBR,FOLLOW_RBR_in_csVisitAction384); if (state.failed) return retval; 
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
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:130:20: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                        adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                        adaptor.addChild(root_1, ((CustomToken)LBR22).getPayload(Tree.class));
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:132:28: ^( ConcCstOption ^( Cst_Label IDENTIFIER ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:132:44: ^( Cst_Label IDENTIFIER )
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
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:134:19: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                        adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                        adaptor.addChild(root_1, ((CustomToken)LBR22).getPayload(Tree.class));
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:136:28: ^( ConcCstOption )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:138:5: ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW csBQTerm
                    {
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:138:5: ( IDENTIFIER l= COLON )?
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
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:138:6: IDENTIFIER l= COLON
                            {
                            IDENTIFIER24=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csVisitAction605); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER24);

                            l=(Token)match(input,COLON,FOLLOW_COLON_in_csVisitAction609); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COLON.add(l);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_csExtendedConstraint_in_csVisitAction613);
                    csExtendedConstraint25=csExtendedConstraint();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csExtendedConstraint.add(csExtendedConstraint25.getTree());
                    ARROW26=(Token)match(input,ARROW,FOLLOW_ARROW_in_csVisitAction615); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARROW.add(ARROW26);

                    pushFollow(FOLLOW_csBQTerm_in_csVisitAction617);
                    csBQTerm27=csBQTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csBQTerm.add(csBQTerm27.getTree());


                    // AST REWRITE
                    // elements: csBQTerm, csBQTerm, csExtendedConstraint, IDENTIFIER, csExtendedConstraint
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
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:139:20: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                        adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:142:28: ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBlock, "ConcCstBlock"), root_2);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:142:43: ^( Cst_BQTermToBlock csBQTerm )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQTermToBlock, "Cst_BQTermToBlock"), root_3);

                        adaptor.addChild(root_3, stream_csBQTerm.nextTree());

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:143:28: ^( ConcCstOption ^( Cst_Label IDENTIFIER ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:143:44: ^( Cst_Label IDENTIFIER )
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
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:145:19: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                        adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:148:28: ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBlock, "ConcCstBlock"), root_2);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:148:43: ^( Cst_BQTermToBlock csBQTerm )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQTermToBlock, "Cst_BQTermToBlock"), root_3);

                        adaptor.addChild(root_3, stream_csBQTerm.nextTree());

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:149:28: ^( ConcCstOption )
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:159:1: csMatchConstruct returns [int marker] : ( LPAR csBQTerm ( COMMA csBQTerm )* RPAR LBR ( csExtendedConstraintAction )* RBR -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ( csBQTerm )* ) ^( ConcCstConstraintAction ( csExtendedConstraintAction )* ) ) | ( LPAR RPAR )? LBR ( csConstraintAction )* RBR -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ) ^( ConcCstConstraintAction ( csConstraintAction )* ) ) );
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:160:21: ( LPAR csBQTerm ( COMMA csBQTerm )* RPAR LBR ( csExtendedConstraintAction )* RBR -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ( csBQTerm )* ) ^( ConcCstConstraintAction ( csExtendedConstraintAction )* ) ) | ( LPAR RPAR )? LBR ( csConstraintAction )* RBR -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ) ^( ConcCstConstraintAction ( csConstraintAction )* ) ) )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==LPAR) ) {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==RPAR) ) {
                    alt11=2;
                }
                else if ( (LA11_1==IDENTIFIER||(LA11_1>=INTEGER && LA11_1<=STRING)) ) {
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:165:1: LPAR csBQTerm ( COMMA csBQTerm )* RPAR LBR ( csExtendedConstraintAction )* RBR
                    {
                    LPAR28=(Token)match(input,LPAR,FOLLOW_LPAR_in_csMatchConstruct974); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR28);

                    pushFollow(FOLLOW_csBQTerm_in_csMatchConstruct976);
                    csBQTerm29=csBQTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csBQTerm.add(csBQTerm29.getTree());
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:165:35: ( COMMA csBQTerm )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==COMMA) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:165:36: COMMA csBQTerm
                    	    {
                    	    COMMA30=(Token)match(input,COMMA,FOLLOW_COMMA_in_csMatchConstruct981); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA30);

                    	    pushFollow(FOLLOW_csBQTerm_in_csMatchConstruct983);
                    	    csBQTerm31=csBQTerm();

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

                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:167:1: ( csExtendedConstraintAction )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==IDENTIFIER||LA8_0==LPAR||(LA8_0>=ANTI && LA8_0<=UNDERSCORE)||(LA8_0>=INTEGER && LA8_0<=STRING)) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:0:0: csExtendedConstraintAction
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
                    // elements: csBQTerm, csExtendedConstraintAction
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
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:173:4: ^( Cst_MatchConstruct ^( ConcCstBQTerm ( csBQTerm )* ) ^( ConcCstConstraintAction ( csExtendedConstraintAction )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MatchConstruct, "Cst_MatchConstruct"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)LPAR28, (CommonToken)RBR35));
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:175:5: ^( ConcCstBQTerm ( csBQTerm )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:175:22: ( csBQTerm )*
                        while ( stream_csBQTerm.hasNext() ) {
                            adaptor.addChild(root_2, stream_csBQTerm.nextTree());

                        }
                        stream_csBQTerm.reset();

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:177:5: ^( ConcCstConstraintAction ( csExtendedConstraintAction )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstConstraintAction, "ConcCstConstraintAction"), root_2);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:177:32: ( csExtendedConstraintAction )*
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:182:1: ( LPAR RPAR )? LBR ( csConstraintAction )* RBR
                    {
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:182:1: ( LPAR RPAR )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==LPAR) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:182:2: LPAR RPAR
                            {
                            LPAR36=(Token)match(input,LPAR,FOLLOW_LPAR_in_csMatchConstruct1059); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_LPAR.add(LPAR36);

                            RPAR37=(Token)match(input,RPAR,FOLLOW_RPAR_in_csMatchConstruct1063); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_RPAR.add(RPAR37);


                            }
                            break;

                    }

                    LBR38=(Token)match(input,LBR,FOLLOW_LBR_in_csMatchConstruct1067); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBR.add(LBR38);

                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:184:1: ( csConstraintAction )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==IDENTIFIER||LA10_0==LPAR||(LA10_0>=ANTI && LA10_0<=UNDERSCORE)||(LA10_0>=INTEGER && LA10_0<=STRING)) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:0:0: csConstraintAction
                    	    {
                    	    pushFollow(FOLLOW_csConstraintAction_in_csMatchConstruct1069);
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

                    RBR40=(Token)match(input,RBR,FOLLOW_RBR_in_csMatchConstruct1072); if (state.failed) return retval; 
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
                    // 190:1: -> ^( Cst_MatchConstruct ^( ConcCstBQTerm ) ^( ConcCstConstraintAction ( csConstraintAction )* ) )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:190:4: ^( Cst_MatchConstruct ^( ConcCstBQTerm ) ^( ConcCstConstraintAction ( csConstraintAction )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MatchConstruct, "Cst_MatchConstruct"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)LBR38, (CommonToken)RBR40));
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:192:5: ^( ConcCstBQTerm )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:194:5: ^( ConcCstConstraintAction ( csConstraintAction )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstConstraintAction, "ConcCstConstraintAction"), root_2);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:194:32: ( csConstraintAction )*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:198:1: csConstraintAction : ( IDENTIFIER l= COLON )? csConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ) ) ;
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:198:20: ( ( IDENTIFIER l= COLON )? csConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ) ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:199:1: ( IDENTIFIER l= COLON )? csConstraint ARROW LBR RBR
            {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:199:1: ( IDENTIFIER l= COLON )?
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:199:2: IDENTIFIER l= COLON
                    {
                    IDENTIFIER41=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csConstraintAction1133); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER41);

                    l=(Token)match(input,COLON,FOLLOW_COLON_in_csConstraintAction1137); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(l);


                    }
                    break;

            }

            pushFollow(FOLLOW_csConstraint_in_csConstraintAction1141);
            csConstraint42=csConstraint();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csConstraint.add(csConstraint42.getTree());
            ARROW43=(Token)match(input,ARROW,FOLLOW_ARROW_in_csConstraintAction1143); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ARROW.add(ARROW43);

            LBR44=(Token)match(input,LBR,FOLLOW_LBR_in_csConstraintAction1145); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR44);

            RBR45=(Token)match(input,RBR,FOLLOW_RBR_in_csConstraintAction1147); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR45);



            // AST REWRITE
            // elements: csConstraint, IDENTIFIER, csConstraint
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 200:3: -> {$l!=null}? ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
            if (l!=null) {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:200:18: ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                adaptor.addChild(root_1, stream_csConstraint.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR44).getPayload(Tree.class));
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:202:19: ^( ConcCstOption ^( Cst_Label IDENTIFIER ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:202:35: ^( Cst_Label IDENTIFIER )
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
            else // 204:3: -> ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ) )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:204:18: ^( Cst_ConstraintAction csConstraint ^( ConcCstOption ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                adaptor.addChild(root_1, stream_csConstraint.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR44).getPayload(Tree.class));
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:206:19: ^( ConcCstOption )
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:210:1: csExtendedConstraintAction : ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) ;
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:210:28: ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:211:1: ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR
            {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:211:1: ( IDENTIFIER l= COLON )?
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:211:2: IDENTIFIER l= COLON
                    {
                    IDENTIFIER46=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csExtendedConstraintAction1320); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER46);

                    l=(Token)match(input,COLON,FOLLOW_COLON_in_csExtendedConstraintAction1324); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(l);


                    }
                    break;

            }

            pushFollow(FOLLOW_csExtendedConstraint_in_csExtendedConstraintAction1328);
            csExtendedConstraint47=csExtendedConstraint();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csExtendedConstraint.add(csExtendedConstraint47.getTree());
            ARROW48=(Token)match(input,ARROW,FOLLOW_ARROW_in_csExtendedConstraintAction1330); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ARROW.add(ARROW48);

            LBR49=(Token)match(input,LBR,FOLLOW_LBR_in_csExtendedConstraintAction1332); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR49);

            RBR50=(Token)match(input,RBR,FOLLOW_RBR_in_csExtendedConstraintAction1334); if (state.failed) return retval; 
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
            // 212:3: -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
            if (l!=null) {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:212:18: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR49).getPayload(Tree.class));
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:214:19: ^( ConcCstOption ^( Cst_Label IDENTIFIER ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOption, "ConcCstOption"), root_2);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:214:35: ^( Cst_Label IDENTIFIER )
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
            else // 216:3: -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:216:18: ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstraintAction, "Cst_ConstraintAction"), root_1);

                adaptor.addChild(root_1, stream_csExtendedConstraint.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR49).getPayload(Tree.class));
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:218:19: ^( ConcCstOption )
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:246:1: csBQTerm : ( (type= IDENTIFIER )? name= IDENTIFIER (s= STAR )? -> {s!=null && type!=null}? ^( Cst_BQVarStar $name ^( Cst_Type $type) ) -> {s!=null && type==null}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) ) -> {s==null && type!=null}? ^( Cst_BQVar $name ^( Cst_Type $type) ) -> ^( Cst_BQVar $name ^( Cst_TypeUnknown ) ) | bqname= IDENTIFIER LPAR (a+= csBQTerm ( COMMA a+= csBQTerm )* )? RPAR -> ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) | csConstantValue -> ^( Cst_BQConstant csConstantValue ) );
    public final miniTomParser.csBQTerm_return csBQTerm() throws RecognitionException {
        miniTomParser.csBQTerm_return retval = new miniTomParser.csBQTerm_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token type=null;
        Token name=null;
        Token s=null;
        Token bqname=null;
        Token LPAR51=null;
        Token COMMA52=null;
        Token RPAR53=null;
        List list_a=null;
        miniTomParser.csConstantValue_return csConstantValue54 = null;

        RuleReturnScope a = null;
        Tree type_tree=null;
        Tree name_tree=null;
        Tree s_tree=null;
        Tree bqname_tree=null;
        Tree LPAR51_tree=null;
        Tree COMMA52_tree=null;
        Tree RPAR53_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleSubtreeStream stream_csBQTerm=new RewriteRuleSubtreeStream(adaptor,"rule csBQTerm");
        RewriteRuleSubtreeStream stream_csConstantValue=new RewriteRuleSubtreeStream(adaptor,"rule csConstantValue");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:246:10: ( (type= IDENTIFIER )? name= IDENTIFIER (s= STAR )? -> {s!=null && type!=null}? ^( Cst_BQVarStar $name ^( Cst_Type $type) ) -> {s!=null && type==null}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) ) -> {s==null && type!=null}? ^( Cst_BQVar $name ^( Cst_Type $type) ) -> ^( Cst_BQVar $name ^( Cst_TypeUnknown ) ) | bqname= IDENTIFIER LPAR (a+= csBQTerm ( COMMA a+= csBQTerm )* )? RPAR -> ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) | csConstantValue -> ^( Cst_BQConstant csConstantValue ) )
            int alt18=3;
            alt18 = dfa18.predict(input);
            switch (alt18) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:247:3: (type= IDENTIFIER )? name= IDENTIFIER (s= STAR )?
                    {
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:247:3: (type= IDENTIFIER )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==IDENTIFIER) ) {
                        int LA14_1 = input.LA(2);

                        if ( (LA14_1==IDENTIFIER) ) {
                            int LA14_3 = input.LA(3);

                            if ( (synpred14_miniTom()) ) {
                                alt14=1;
                            }
                        }
                    }
                    switch (alt14) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:247:4: type= IDENTIFIER
                            {
                            type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csBQTerm1521); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IDENTIFIER.add(type);


                            }
                            break;

                    }

                    name=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csBQTerm1527); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(name);

                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:247:38: (s= STAR )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==STAR) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:247:39: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csBQTerm1532); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(s);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: type, name, name, name, type, name
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
                    // 248:4: -> {s!=null && type!=null}? ^( Cst_BQVarStar $name ^( Cst_Type $type) )
                    if (s!=null && type!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:248:31: ^( Cst_BQVarStar $name ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVarStar, "Cst_BQVarStar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:248:90: ^( Cst_Type $type)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                        adaptor.addChild(root_2, stream_type.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 249:4: -> {s!=null && type==null}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) )
                    if (s!=null && type==null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:249:31: ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVarStar, "Cst_BQVarStar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:249:90: ^( Cst_TypeUnknown )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 250:4: -> {s==null && type!=null}? ^( Cst_BQVar $name ^( Cst_Type $type) )
                    if (s==null && type!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:250:31: ^( Cst_BQVar $name ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVar, "Cst_BQVar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:250:86: ^( Cst_Type $type)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                        adaptor.addChild(root_2, stream_type.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 251:4: -> ^( Cst_BQVar $name ^( Cst_TypeUnknown ) )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:251:31: ^( Cst_BQVar $name ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVar, "Cst_BQVar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)name));
                        adaptor.addChild(root_1, stream_name.nextNode());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:251:86: ^( Cst_TypeUnknown )
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
                case 2 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:253:4: bqname= IDENTIFIER LPAR (a+= csBQTerm ( COMMA a+= csBQTerm )* )? RPAR
                    {
                    bqname=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csBQTerm1652); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(bqname);

                    LPAR51=(Token)match(input,LPAR,FOLLOW_LPAR_in_csBQTerm1654); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR51);

                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:253:27: (a+= csBQTerm ( COMMA a+= csBQTerm )* )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==IDENTIFIER||(LA17_0>=INTEGER && LA17_0<=STRING)) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:253:28: a+= csBQTerm ( COMMA a+= csBQTerm )*
                            {
                            pushFollow(FOLLOW_csBQTerm_in_csBQTerm1659);
                            a=csBQTerm();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_csBQTerm.add(a.getTree());
                            if (list_a==null) list_a=new ArrayList();
                            list_a.add(a.getTree());

                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:253:40: ( COMMA a+= csBQTerm )*
                            loop16:
                            do {
                                int alt16=2;
                                int LA16_0 = input.LA(1);

                                if ( (LA16_0==COMMA) ) {
                                    alt16=1;
                                }


                                switch (alt16) {
                            	case 1 :
                            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:253:41: COMMA a+= csBQTerm
                            	    {
                            	    COMMA52=(Token)match(input,COMMA,FOLLOW_COMMA_in_csBQTerm1662); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA52);

                            	    pushFollow(FOLLOW_csBQTerm_in_csBQTerm1666);
                            	    a=csBQTerm();

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

                    RPAR53=(Token)match(input,RPAR,FOLLOW_RPAR_in_csBQTerm1672); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR53);



                    // AST REWRITE
                    // elements: a, bqname
                    // token labels: bqname
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: a
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_bqname=new RewriteRuleTokenStream(adaptor,"token bqname",bqname);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"token a",list_a);
                    root_0 = (Tree)adaptor.nil();
                    // 254:4: -> ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:254:7: ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQAppl, "Cst_BQAppl"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)LPAR51, (CommonToken)RPAR53));
                        adaptor.addChild(root_1, stream_bqname.nextNode());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:255:16: ^( ConcCstBQTerm ( $a)* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:255:32: ( $a)*
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
                case 3 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:256:5: csConstantValue
                    {
                    pushFollow(FOLLOW_csConstantValue_in_csBQTerm1707);
                    csConstantValue54=csConstantValue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csConstantValue.add(csConstantValue54.getTree());


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
                    // 256:21: -> ^( Cst_BQConstant csConstantValue )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:256:24: ^( Cst_BQConstant csConstantValue )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQConstant, "Cst_BQConstant"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)(csConstantValue54!=null?((Token)csConstantValue54.start):null), (CommonToken)(csConstantValue54!=null?((Token)csConstantValue54.stop):null)));
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:261:1: csExtendedConstraint : csMatchArgumentConstraintList ( (a= AND | o= OR ) csConstraint )? -> {a!=null}? ^( Cst_AndConstraint csMatchArgumentConstraintList csConstraint ) -> {o!=null}? ^( Cst_OrConstraint csMatchArgumentConstraintList csConstraint ) -> csMatchArgumentConstraintList ;
    public final miniTomParser.csExtendedConstraint_return csExtendedConstraint() throws RecognitionException {
        miniTomParser.csExtendedConstraint_return retval = new miniTomParser.csExtendedConstraint_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token a=null;
        Token o=null;
        miniTomParser.csMatchArgumentConstraintList_return csMatchArgumentConstraintList55 = null;

        miniTomParser.csConstraint_return csConstraint56 = null;


        Tree a_tree=null;
        Tree o_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
        RewriteRuleSubtreeStream stream_csMatchArgumentConstraintList=new RewriteRuleSubtreeStream(adaptor,"rule csMatchArgumentConstraintList");
        RewriteRuleSubtreeStream stream_csConstraint=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:269:22: ( csMatchArgumentConstraintList ( (a= AND | o= OR ) csConstraint )? -> {a!=null}? ^( Cst_AndConstraint csMatchArgumentConstraintList csConstraint ) -> {o!=null}? ^( Cst_OrConstraint csMatchArgumentConstraintList csConstraint ) -> csMatchArgumentConstraintList )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:270:2: csMatchArgumentConstraintList ( (a= AND | o= OR ) csConstraint )?
            {
            pushFollow(FOLLOW_csMatchArgumentConstraintList_in_csExtendedConstraint1738);
            csMatchArgumentConstraintList55=csMatchArgumentConstraintList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csMatchArgumentConstraintList.add(csMatchArgumentConstraintList55.getTree());
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:270:32: ( (a= AND | o= OR ) csConstraint )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( ((LA20_0>=AND && LA20_0<=OR)) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:270:33: (a= AND | o= OR ) csConstraint
                    {
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:270:33: (a= AND | o= OR )
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==AND) ) {
                        alt19=1;
                    }
                    else if ( (LA19_0==OR) ) {
                        alt19=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 0, input);

                        throw nvae;
                    }
                    switch (alt19) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:270:34: a= AND
                            {
                            a=(Token)match(input,AND,FOLLOW_AND_in_csExtendedConstraint1744); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_AND.add(a);


                            }
                            break;
                        case 2 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:270:40: o= OR
                            {
                            o=(Token)match(input,OR,FOLLOW_OR_in_csExtendedConstraint1748); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_OR.add(o);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_csConstraint_in_csExtendedConstraint1751);
                    csConstraint56=csConstraint();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csConstraint.add(csConstraint56.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: csConstraint, csMatchArgumentConstraintList, csMatchArgumentConstraintList, csConstraint, csMatchArgumentConstraintList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 271:2: -> {a!=null}? ^( Cst_AndConstraint csMatchArgumentConstraintList csConstraint )
            if (a!=null) {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:271:15: ^( Cst_AndConstraint csMatchArgumentConstraintList csConstraint )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_AndConstraint, "Cst_AndConstraint"), root_1);

                adaptor.addChild(root_1, stream_csMatchArgumentConstraintList.nextTree());
                adaptor.addChild(root_1, stream_csConstraint.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 272:2: -> {o!=null}? ^( Cst_OrConstraint csMatchArgumentConstraintList csConstraint )
            if (o!=null) {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:272:15: ^( Cst_OrConstraint csMatchArgumentConstraintList csConstraint )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OrConstraint, "Cst_OrConstraint"), root_1);

                adaptor.addChild(root_1, stream_csMatchArgumentConstraintList.nextTree());
                adaptor.addChild(root_1, stream_csConstraint.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 273:2: -> csMatchArgumentConstraintList
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:276:1: csMatchArgumentConstraintList : csMatchArgumentConstraint ( COMMA c2= csMatchArgumentConstraint )* ( COMMA )? -> {c2!=null}? ^( Cst_AndConstraint ( csMatchArgumentConstraint )* ) -> csMatchArgumentConstraint ;
    public final miniTomParser.csMatchArgumentConstraintList_return csMatchArgumentConstraintList() throws RecognitionException {
        miniTomParser.csMatchArgumentConstraintList_return retval = new miniTomParser.csMatchArgumentConstraintList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token COMMA58=null;
        Token COMMA59=null;
        miniTomParser.csMatchArgumentConstraint_return c2 = null;

        miniTomParser.csMatchArgumentConstraint_return csMatchArgumentConstraint57 = null;


        Tree COMMA58_tree=null;
        Tree COMMA59_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_csMatchArgumentConstraint=new RewriteRuleSubtreeStream(adaptor,"rule csMatchArgumentConstraint");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:276:31: ( csMatchArgumentConstraint ( COMMA c2= csMatchArgumentConstraint )* ( COMMA )? -> {c2!=null}? ^( Cst_AndConstraint ( csMatchArgumentConstraint )* ) -> csMatchArgumentConstraint )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:277:3: csMatchArgumentConstraint ( COMMA c2= csMatchArgumentConstraint )* ( COMMA )?
            {
            pushFollow(FOLLOW_csMatchArgumentConstraint_in_csMatchArgumentConstraintList1804);
            csMatchArgumentConstraint57=csMatchArgumentConstraint();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csMatchArgumentConstraint.add(csMatchArgumentConstraint57.getTree());
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:277:29: ( COMMA c2= csMatchArgumentConstraint )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==COMMA) ) {
                    int LA21_1 = input.LA(2);

                    if ( (LA21_1==IDENTIFIER||LA21_1==LPAR||(LA21_1>=ANTI && LA21_1<=UNDERSCORE)||(LA21_1>=INTEGER && LA21_1<=STRING)) ) {
                        alt21=1;
                    }


                }


                switch (alt21) {
            	case 1 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:277:30: COMMA c2= csMatchArgumentConstraint
            	    {
            	    COMMA58=(Token)match(input,COMMA,FOLLOW_COMMA_in_csMatchArgumentConstraintList1807); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA58);

            	    pushFollow(FOLLOW_csMatchArgumentConstraint_in_csMatchArgumentConstraintList1811);
            	    c2=csMatchArgumentConstraint();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csMatchArgumentConstraint.add(c2.getTree());

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:277:67: ( COMMA )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==COMMA) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:277:68: COMMA
                    {
                    COMMA59=(Token)match(input,COMMA,FOLLOW_COMMA_in_csMatchArgumentConstraintList1816); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COMMA.add(COMMA59);


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
            // 278:3: -> {c2!=null}? ^( Cst_AndConstraint ( csMatchArgumentConstraint )* )
            if (c2!=null) {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:278:17: ^( Cst_AndConstraint ( csMatchArgumentConstraint )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_AndConstraint, "Cst_AndConstraint"), root_1);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:278:37: ( csMatchArgumentConstraint )*
                while ( stream_csMatchArgumentConstraint.hasNext() ) {
                    adaptor.addChild(root_1, stream_csMatchArgumentConstraint.nextTree());

                }
                stream_csMatchArgumentConstraint.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 279:3: -> csMatchArgumentConstraint
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:282:1: csMatchArgumentConstraint : csPattern -> ^( Cst_MatchArgumentConstraint csPattern ) ;
    public final miniTomParser.csMatchArgumentConstraint_return csMatchArgumentConstraint() throws RecognitionException {
        miniTomParser.csMatchArgumentConstraint_return retval = new miniTomParser.csMatchArgumentConstraint_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        miniTomParser.csPattern_return csPattern60 = null;


        RewriteRuleSubtreeStream stream_csPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPattern");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:282:27: ( csPattern -> ^( Cst_MatchArgumentConstraint csPattern ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:283:3: csPattern
            {
            pushFollow(FOLLOW_csPattern_in_csMatchArgumentConstraint1858);
            csPattern60=csPattern();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csPattern.add(csPattern60.getTree());


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
            // 284:3: -> ^( Cst_MatchArgumentConstraint csPattern )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:284:6: ^( Cst_MatchArgumentConstraint csPattern )
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:287:1: csConstraint : csConstraint_priority1 ;
    public final miniTomParser.csConstraint_return csConstraint() throws RecognitionException {
        miniTomParser.csConstraint_return retval = new miniTomParser.csConstraint_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        miniTomParser.csConstraint_priority1_return csConstraint_priority161 = null;



        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:299:14: ( csConstraint_priority1 )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:300:2: csConstraint_priority1
            {
            root_0 = (Tree)adaptor.nil();

            pushFollow(FOLLOW_csConstraint_priority1_in_csConstraint1881);
            csConstraint_priority161=csConstraint_priority1();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, csConstraint_priority161.getTree());

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:303:1: csConstraint_priority1 : csConstraint_priority2 (or= OR csConstraint_priority2 )* -> {or!=null}? ^( Cst_OrConstraint ( csConstraint_priority2 )* ) -> csConstraint_priority2 ;
    public final miniTomParser.csConstraint_priority1_return csConstraint_priority1() throws RecognitionException {
        miniTomParser.csConstraint_priority1_return retval = new miniTomParser.csConstraint_priority1_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token or=null;
        miniTomParser.csConstraint_priority2_return csConstraint_priority262 = null;

        miniTomParser.csConstraint_priority2_return csConstraint_priority263 = null;


        Tree or_tree=null;
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
        RewriteRuleSubtreeStream stream_csConstraint_priority2=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint_priority2");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:303:24: ( csConstraint_priority2 (or= OR csConstraint_priority2 )* -> {or!=null}? ^( Cst_OrConstraint ( csConstraint_priority2 )* ) -> csConstraint_priority2 )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:304:3: csConstraint_priority2 (or= OR csConstraint_priority2 )*
            {
            pushFollow(FOLLOW_csConstraint_priority2_in_csConstraint_priority11892);
            csConstraint_priority262=csConstraint_priority2();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csConstraint_priority2.add(csConstraint_priority262.getTree());
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:304:26: (or= OR csConstraint_priority2 )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==OR) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:304:27: or= OR csConstraint_priority2
            	    {
            	    or=(Token)match(input,OR,FOLLOW_OR_in_csConstraint_priority11897); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_OR.add(or);

            	    pushFollow(FOLLOW_csConstraint_priority2_in_csConstraint_priority11899);
            	    csConstraint_priority263=csConstraint_priority2();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csConstraint_priority2.add(csConstraint_priority263.getTree());

            	    }
            	    break;

            	default :
            	    break loop23;
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
            // 305:3: -> {or!=null}? ^( Cst_OrConstraint ( csConstraint_priority2 )* )
            if (or!=null) {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:305:17: ^( Cst_OrConstraint ( csConstraint_priority2 )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OrConstraint, "Cst_OrConstraint"), root_1);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:305:36: ( csConstraint_priority2 )*
                while ( stream_csConstraint_priority2.hasNext() ) {
                    adaptor.addChild(root_1, stream_csConstraint_priority2.nextTree());

                }
                stream_csConstraint_priority2.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 306:3: -> csConstraint_priority2
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:309:1: csConstraint_priority2 : csConstraint_priority3 (and= AND csConstraint_priority3 )* -> {and!=null}? ^( Cst_AndConstraint ( csConstraint_priority3 )* ) -> csConstraint_priority3 ;
    public final miniTomParser.csConstraint_priority2_return csConstraint_priority2() throws RecognitionException {
        miniTomParser.csConstraint_priority2_return retval = new miniTomParser.csConstraint_priority2_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token and=null;
        miniTomParser.csConstraint_priority3_return csConstraint_priority364 = null;

        miniTomParser.csConstraint_priority3_return csConstraint_priority365 = null;


        Tree and_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_csConstraint_priority3=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint_priority3");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:309:24: ( csConstraint_priority3 (and= AND csConstraint_priority3 )* -> {and!=null}? ^( Cst_AndConstraint ( csConstraint_priority3 )* ) -> csConstraint_priority3 )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:310:3: csConstraint_priority3 (and= AND csConstraint_priority3 )*
            {
            pushFollow(FOLLOW_csConstraint_priority3_in_csConstraint_priority21930);
            csConstraint_priority364=csConstraint_priority3();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csConstraint_priority3.add(csConstraint_priority364.getTree());
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:310:26: (and= AND csConstraint_priority3 )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==AND) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:310:27: and= AND csConstraint_priority3
            	    {
            	    and=(Token)match(input,AND,FOLLOW_AND_in_csConstraint_priority21935); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_AND.add(and);

            	    pushFollow(FOLLOW_csConstraint_priority3_in_csConstraint_priority21937);
            	    csConstraint_priority365=csConstraint_priority3();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csConstraint_priority3.add(csConstraint_priority365.getTree());

            	    }
            	    break;

            	default :
            	    break loop24;
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
            // 311:3: -> {and!=null}? ^( Cst_AndConstraint ( csConstraint_priority3 )* )
            if (and!=null) {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:311:18: ^( Cst_AndConstraint ( csConstraint_priority3 )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_AndConstraint, "Cst_AndConstraint"), root_1);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:311:38: ( csConstraint_priority3 )*
                while ( stream_csConstraint_priority3.hasNext() ) {
                    adaptor.addChild(root_1, stream_csConstraint_priority3.nextTree());

                }
                stream_csConstraint_priority3.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 312:3: -> csConstraint_priority3
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:315:1: csConstraint_priority3 : (l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm -> {gt!=null}? ^( Cst_NumGreaterThan $l $r) -> {ge!=null}? ^( Cst_NumGreaterOrEqualTo $l $r) -> {lt!=null}? ^( Cst_NumLessThan $l $r) -> {le!=null}? ^( Cst_NumLessOrEqualTo $l $r) -> {eq!=null}? ^( Cst_NumEqualTo $l $r) -> ^( Cst_NumDifferent $l $r) | csConstraint_priority4 -> csConstraint_priority4 );
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
        miniTomParser.csTerm_return l = null;

        miniTomParser.csTerm_return r = null;

        miniTomParser.csConstraint_priority4_return csConstraint_priority466 = null;


        Tree gt_tree=null;
        Tree ge_tree=null;
        Tree lt_tree=null;
        Tree le_tree=null;
        Tree eq_tree=null;
        Tree ne_tree=null;
        RewriteRuleTokenStream stream_DIFFERENT=new RewriteRuleTokenStream(adaptor,"token DIFFERENT");
        RewriteRuleTokenStream stream_DOUBLEEQUAL=new RewriteRuleTokenStream(adaptor,"token DOUBLEEQUAL");
        RewriteRuleTokenStream stream_LOWERTHAN=new RewriteRuleTokenStream(adaptor,"token LOWERTHAN");
        RewriteRuleTokenStream stream_GREATEROREQU=new RewriteRuleTokenStream(adaptor,"token GREATEROREQU");
        RewriteRuleTokenStream stream_GREATERTHAN=new RewriteRuleTokenStream(adaptor,"token GREATERTHAN");
        RewriteRuleTokenStream stream_LOWEROREQU=new RewriteRuleTokenStream(adaptor,"token LOWEROREQU");
        RewriteRuleSubtreeStream stream_csTerm=new RewriteRuleSubtreeStream(adaptor,"rule csTerm");
        RewriteRuleSubtreeStream stream_csConstraint_priority4=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint_priority4");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:315:24: (l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm -> {gt!=null}? ^( Cst_NumGreaterThan $l $r) -> {ge!=null}? ^( Cst_NumGreaterOrEqualTo $l $r) -> {lt!=null}? ^( Cst_NumLessThan $l $r) -> {le!=null}? ^( Cst_NumLessOrEqualTo $l $r) -> {eq!=null}? ^( Cst_NumEqualTo $l $r) -> ^( Cst_NumDifferent $l $r) | csConstraint_priority4 -> csConstraint_priority4 )
            int alt26=2;
            alt26 = dfa26.predict(input);
            switch (alt26) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:317:3: l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm
                    {
                    pushFollow(FOLLOW_csTerm_in_csConstraint_priority31972);
                    l=csTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csTerm.add(l.getTree());
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:318:3: (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT )
                    int alt25=6;
                    switch ( input.LA(1) ) {
                    case GREATERTHAN:
                        {
                        alt25=1;
                        }
                        break;
                    case GREATEROREQU:
                        {
                        alt25=2;
                        }
                        break;
                    case LOWERTHAN:
                        {
                        alt25=3;
                        }
                        break;
                    case LOWEROREQU:
                        {
                        alt25=4;
                        }
                        break;
                    case DOUBLEEQUAL:
                        {
                        alt25=5;
                        }
                        break;
                    case DIFFERENT:
                        {
                        alt25=6;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 25, 0, input);

                        throw nvae;
                    }

                    switch (alt25) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:318:4: gt= GREATERTHAN
                            {
                            gt=(Token)match(input,GREATERTHAN,FOLLOW_GREATERTHAN_in_csConstraint_priority31979); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_GREATERTHAN.add(gt);


                            }
                            break;
                        case 2 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:318:19: ge= GREATEROREQU
                            {
                            ge=(Token)match(input,GREATEROREQU,FOLLOW_GREATEROREQU_in_csConstraint_priority31983); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_GREATEROREQU.add(ge);


                            }
                            break;
                        case 3 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:318:35: lt= LOWERTHAN
                            {
                            lt=(Token)match(input,LOWERTHAN,FOLLOW_LOWERTHAN_in_csConstraint_priority31987); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_LOWERTHAN.add(lt);


                            }
                            break;
                        case 4 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:319:4: le= LOWEROREQU
                            {
                            le=(Token)match(input,LOWEROREQU,FOLLOW_LOWEROREQU_in_csConstraint_priority31994); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_LOWEROREQU.add(le);


                            }
                            break;
                        case 5 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:319:19: eq= DOUBLEEQUAL
                            {
                            eq=(Token)match(input,DOUBLEEQUAL,FOLLOW_DOUBLEEQUAL_in_csConstraint_priority31999); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DOUBLEEQUAL.add(eq);


                            }
                            break;
                        case 6 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:319:35: ne= DIFFERENT
                            {
                            ne=(Token)match(input,DIFFERENT,FOLLOW_DIFFERENT_in_csConstraint_priority32004); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DIFFERENT.add(ne);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_csTerm_in_csConstraint_priority32011);
                    r=csTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csTerm.add(r.getTree());


                    // AST REWRITE
                    // elements: r, r, r, l, l, l, l, r, r, r, l, l
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
                    // 322:2: -> {gt!=null}? ^( Cst_NumGreaterThan $l $r)
                    if (gt!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:322:16: ^( Cst_NumGreaterThan $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumGreaterThan, "Cst_NumGreaterThan"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 323:2: -> {ge!=null}? ^( Cst_NumGreaterOrEqualTo $l $r)
                    if (ge!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:323:16: ^( Cst_NumGreaterOrEqualTo $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumGreaterOrEqualTo, "Cst_NumGreaterOrEqualTo"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 324:2: -> {lt!=null}? ^( Cst_NumLessThan $l $r)
                    if (lt!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:324:16: ^( Cst_NumLessThan $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumLessThan, "Cst_NumLessThan"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 325:2: -> {le!=null}? ^( Cst_NumLessOrEqualTo $l $r)
                    if (le!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:325:16: ^( Cst_NumLessOrEqualTo $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumLessOrEqualTo, "Cst_NumLessOrEqualTo"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 326:2: -> {eq!=null}? ^( Cst_NumEqualTo $l $r)
                    if (eq!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:326:16: ^( Cst_NumEqualTo $l $r)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_NumEqualTo, "Cst_NumEqualTo"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());
                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 327:2: -> ^( Cst_NumDifferent $l $r)
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:327:16: ^( Cst_NumDifferent $l $r)
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
                case 2 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:330:4: csConstraint_priority4
                    {
                    pushFollow(FOLLOW_csConstraint_priority4_in_csConstraint_priority32135);
                    csConstraint_priority466=csConstraint_priority4();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csConstraint_priority4.add(csConstraint_priority466.getTree());


                    // AST REWRITE
                    // elements: csConstraint_priority4
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 331:2: -> csConstraint_priority4
                    {
                        adaptor.addChild(root_0, stream_csConstraint_priority4.nextTree());

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

    public static class csConstraint_priority4_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csConstraint_priority4"
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:334:1: csConstraint_priority4 : ( csPattern LARROW csBQTerm -> ^( Cst_MatchTermConstraint csPattern csBQTerm ) | LPAR csConstraint RPAR -> csConstraint );
    public final miniTomParser.csConstraint_priority4_return csConstraint_priority4() throws RecognitionException {
        miniTomParser.csConstraint_priority4_return retval = new miniTomParser.csConstraint_priority4_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LARROW68=null;
        Token LPAR70=null;
        Token RPAR72=null;
        miniTomParser.csPattern_return csPattern67 = null;

        miniTomParser.csBQTerm_return csBQTerm69 = null;

        miniTomParser.csConstraint_return csConstraint71 = null;


        Tree LARROW68_tree=null;
        Tree LPAR70_tree=null;
        Tree RPAR72_tree=null;
        RewriteRuleTokenStream stream_LARROW=new RewriteRuleTokenStream(adaptor,"token LARROW");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleSubtreeStream stream_csBQTerm=new RewriteRuleSubtreeStream(adaptor,"rule csBQTerm");
        RewriteRuleSubtreeStream stream_csConstraint=new RewriteRuleSubtreeStream(adaptor,"rule csConstraint");
        RewriteRuleSubtreeStream stream_csPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPattern");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:334:24: ( csPattern LARROW csBQTerm -> ^( Cst_MatchTermConstraint csPattern csBQTerm ) | LPAR csConstraint RPAR -> csConstraint )
            int alt27=2;
            alt27 = dfa27.predict(input);
            switch (alt27) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:335:2: csPattern LARROW csBQTerm
                    {
                    pushFollow(FOLLOW_csPattern_in_csConstraint_priority42150);
                    csPattern67=csPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPattern.add(csPattern67.getTree());
                    LARROW68=(Token)match(input,LARROW,FOLLOW_LARROW_in_csConstraint_priority42152); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LARROW.add(LARROW68);

                    pushFollow(FOLLOW_csBQTerm_in_csConstraint_priority42158);
                    csBQTerm69=csBQTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csBQTerm.add(csBQTerm69.getTree());


                    // AST REWRITE
                    // elements: csBQTerm, csPattern
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 336:2: -> ^( Cst_MatchTermConstraint csPattern csBQTerm )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:336:5: ^( Cst_MatchTermConstraint csPattern csBQTerm )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:338:4: LPAR csConstraint RPAR
                    {
                    LPAR70=(Token)match(input,LPAR,FOLLOW_LPAR_in_csConstraint_priority42177); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR70);

                    pushFollow(FOLLOW_csConstraint_in_csConstraint_priority42179);
                    csConstraint71=csConstraint();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csConstraint.add(csConstraint71.getTree());
                    RPAR72=(Token)match(input,RPAR,FOLLOW_RPAR_in_csConstraint_priority42181); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR72);



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
                    // 339:2: -> csConstraint
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
    // $ANTLR end "csConstraint_priority4"

    public static class csTerm_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csTerm"
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:342:1: csTerm : ( IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_TermVariableStar IDENTIFIER ) -> ^( Cst_TermVariable IDENTIFIER ) | IDENTIFIER LPAR ( csTerm ( COMMA csTerm )* )? RPAR -> ^( Cst_TermAppl IDENTIFIER ^( ConcCstTerm ( csTerm )* ) ) );
    public final miniTomParser.csTerm_return csTerm() throws RecognitionException {
        miniTomParser.csTerm_return retval = new miniTomParser.csTerm_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token s=null;
        Token IDENTIFIER73=null;
        Token IDENTIFIER74=null;
        Token LPAR75=null;
        Token COMMA77=null;
        Token RPAR79=null;
        miniTomParser.csTerm_return csTerm76 = null;

        miniTomParser.csTerm_return csTerm78 = null;


        Tree s_tree=null;
        Tree IDENTIFIER73_tree=null;
        Tree IDENTIFIER74_tree=null;
        Tree LPAR75_tree=null;
        Tree COMMA77_tree=null;
        Tree RPAR79_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleSubtreeStream stream_csTerm=new RewriteRuleSubtreeStream(adaptor,"rule csTerm");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:342:8: ( IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_TermVariableStar IDENTIFIER ) -> ^( Cst_TermVariable IDENTIFIER ) | IDENTIFIER LPAR ( csTerm ( COMMA csTerm )* )? RPAR -> ^( Cst_TermAppl IDENTIFIER ^( ConcCstTerm ( csTerm )* ) ) )
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==IDENTIFIER) ) {
                int LA31_1 = input.LA(2);

                if ( (LA31_1==LPAR) ) {
                    alt31=2;
                }
                else if ( (LA31_1==EOF||LA31_1==RPAR||(LA31_1>=ARROW && LA31_1<=DIFFERENT)) ) {
                    alt31=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;
            }
            switch (alt31) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:343:3: IDENTIFIER (s= STAR )?
                    {
                    IDENTIFIER73=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csTerm2197); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER73);

                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:343:14: (s= STAR )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==STAR) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:343:15: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csTerm2202); if (state.failed) return retval; 
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
                    // 344:3: -> {s!=null}? ^( Cst_TermVariableStar IDENTIFIER )
                    if (s!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:344:16: ^( Cst_TermVariableStar IDENTIFIER )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TermVariableStar, "Cst_TermVariableStar"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 345:3: -> ^( Cst_TermVariable IDENTIFIER )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:345:16: ^( Cst_TermVariable IDENTIFIER )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:347:3: IDENTIFIER LPAR ( csTerm ( COMMA csTerm )* )? RPAR
                    {
                    IDENTIFIER74=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csTerm2242); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER74);

                    LPAR75=(Token)match(input,LPAR,FOLLOW_LPAR_in_csTerm2244); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR75);

                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:347:19: ( csTerm ( COMMA csTerm )* )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==IDENTIFIER) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:347:20: csTerm ( COMMA csTerm )*
                            {
                            pushFollow(FOLLOW_csTerm_in_csTerm2247);
                            csTerm76=csTerm();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_csTerm.add(csTerm76.getTree());
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:347:27: ( COMMA csTerm )*
                            loop29:
                            do {
                                int alt29=2;
                                int LA29_0 = input.LA(1);

                                if ( (LA29_0==COMMA) ) {
                                    alt29=1;
                                }


                                switch (alt29) {
                            	case 1 :
                            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:347:28: COMMA csTerm
                            	    {
                            	    COMMA77=(Token)match(input,COMMA,FOLLOW_COMMA_in_csTerm2250); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA77);

                            	    pushFollow(FOLLOW_csTerm_in_csTerm2252);
                            	    csTerm78=csTerm();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_csTerm.add(csTerm78.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop29;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR79=(Token)match(input,RPAR,FOLLOW_RPAR_in_csTerm2258); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR79);



                    // AST REWRITE
                    // elements: IDENTIFIER, csTerm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 348:3: -> ^( Cst_TermAppl IDENTIFIER ^( ConcCstTerm ( csTerm )* ) )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:348:6: ^( Cst_TermAppl IDENTIFIER ^( ConcCstTerm ( csTerm )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TermAppl, "Cst_TermAppl"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:348:32: ^( ConcCstTerm ( csTerm )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstTerm, "ConcCstTerm"), root_2);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:348:46: ( csTerm )*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:351:1: csPattern : ( IDENTIFIER AT csPattern -> ^( Cst_AnnotatedPattern csPattern IDENTIFIER ) | ANTI csPattern -> ^( Cst_Anti csPattern ) | csHeadSymbolList csExplicitTermList -> ^( Cst_Appl csHeadSymbolList csExplicitTermList ) | csHeadSymbolList csImplicitPairList -> ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList ) | IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_VariableStar IDENTIFIER ) -> ^( Cst_Variable IDENTIFIER ) | UNDERSCORE (s= STAR )? -> {s!=null}? ^( Cst_UnamedVariableStar ) -> ^( Cst_UnamedVariable ) | csConstantValue (s= STAR )? -> {s!=null}? ^( Cst_ConstantStar csConstantValue ) -> ^( Cst_Constant csConstantValue ) );
    public final miniTomParser.csPattern_return csPattern() throws RecognitionException {
        miniTomParser.csPattern_return retval = new miniTomParser.csPattern_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token s=null;
        Token IDENTIFIER80=null;
        Token AT81=null;
        Token ANTI83=null;
        Token IDENTIFIER89=null;
        Token UNDERSCORE90=null;
        miniTomParser.csPattern_return csPattern82 = null;

        miniTomParser.csPattern_return csPattern84 = null;

        miniTomParser.csHeadSymbolList_return csHeadSymbolList85 = null;

        miniTomParser.csExplicitTermList_return csExplicitTermList86 = null;

        miniTomParser.csHeadSymbolList_return csHeadSymbolList87 = null;

        miniTomParser.csImplicitPairList_return csImplicitPairList88 = null;

        miniTomParser.csConstantValue_return csConstantValue91 = null;


        Tree s_tree=null;
        Tree IDENTIFIER80_tree=null;
        Tree AT81_tree=null;
        Tree ANTI83_tree=null;
        Tree IDENTIFIER89_tree=null;
        Tree UNDERSCORE90_tree=null;
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:351:11: ( IDENTIFIER AT csPattern -> ^( Cst_AnnotatedPattern csPattern IDENTIFIER ) | ANTI csPattern -> ^( Cst_Anti csPattern ) | csHeadSymbolList csExplicitTermList -> ^( Cst_Appl csHeadSymbolList csExplicitTermList ) | csHeadSymbolList csImplicitPairList -> ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList ) | IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_VariableStar IDENTIFIER ) -> ^( Cst_Variable IDENTIFIER ) | UNDERSCORE (s= STAR )? -> {s!=null}? ^( Cst_UnamedVariableStar ) -> ^( Cst_UnamedVariable ) | csConstantValue (s= STAR )? -> {s!=null}? ^( Cst_ConstantStar csConstantValue ) -> ^( Cst_Constant csConstantValue ) )
            int alt35=7;
            alt35 = dfa35.predict(input);
            switch (alt35) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:353:3: IDENTIFIER AT csPattern
                    {
                    IDENTIFIER80=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csPattern2289); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER80);

                    AT81=(Token)match(input,AT,FOLLOW_AT_in_csPattern2291); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AT.add(AT81);

                    pushFollow(FOLLOW_csPattern_in_csPattern2293);
                    csPattern82=csPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPattern.add(csPattern82.getTree());


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
                    // 354:3: -> ^( Cst_AnnotatedPattern csPattern IDENTIFIER )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:354:6: ^( Cst_AnnotatedPattern csPattern IDENTIFIER )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:357:3: ANTI csPattern
                    {
                    ANTI83=(Token)match(input,ANTI,FOLLOW_ANTI_in_csPattern2313); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ANTI.add(ANTI83);

                    pushFollow(FOLLOW_csPattern_in_csPattern2315);
                    csPattern84=csPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPattern.add(csPattern84.getTree());


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
                    // 358:3: -> ^( Cst_Anti csPattern )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:358:6: ^( Cst_Anti csPattern )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:361:3: csHeadSymbolList csExplicitTermList
                    {
                    pushFollow(FOLLOW_csHeadSymbolList_in_csPattern2333);
                    csHeadSymbolList85=csHeadSymbolList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csHeadSymbolList.add(csHeadSymbolList85.getTree());
                    pushFollow(FOLLOW_csExplicitTermList_in_csPattern2335);
                    csExplicitTermList86=csExplicitTermList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csExplicitTermList.add(csExplicitTermList86.getTree());


                    // AST REWRITE
                    // elements: csExplicitTermList, csHeadSymbolList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 362:3: -> ^( Cst_Appl csHeadSymbolList csExplicitTermList )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:362:6: ^( Cst_Appl csHeadSymbolList csExplicitTermList )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:365:3: csHeadSymbolList csImplicitPairList
                    {
                    pushFollow(FOLLOW_csHeadSymbolList_in_csPattern2356);
                    csHeadSymbolList87=csHeadSymbolList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csHeadSymbolList.add(csHeadSymbolList87.getTree());
                    pushFollow(FOLLOW_csImplicitPairList_in_csPattern2358);
                    csImplicitPairList88=csImplicitPairList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csImplicitPairList.add(csImplicitPairList88.getTree());


                    // AST REWRITE
                    // elements: csHeadSymbolList, csImplicitPairList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 366:3: -> ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:366:6: ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:370:3: IDENTIFIER (s= STAR )?
                    {
                    IDENTIFIER89=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csPattern2381); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER89);

                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:370:14: (s= STAR )?
                    int alt32=2;
                    int LA32_0 = input.LA(1);

                    if ( (LA32_0==STAR) ) {
                        alt32=1;
                    }
                    switch (alt32) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:370:15: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csPattern2386); if (state.failed) return retval; 
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
                    // 371:3: -> {s!=null}? ^( Cst_VariableStar IDENTIFIER )
                    if (s!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:371:17: ^( Cst_VariableStar IDENTIFIER )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_VariableStar, "Cst_VariableStar"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 372:3: -> ^( Cst_Variable IDENTIFIER )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:372:17: ^( Cst_Variable IDENTIFIER )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:376:3: UNDERSCORE (s= STAR )?
                    {
                    UNDERSCORE90=(Token)match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_csPattern2424); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_UNDERSCORE.add(UNDERSCORE90);

                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:376:14: (s= STAR )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==STAR) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:376:15: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csPattern2429); if (state.failed) return retval; 
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
                    // 377:3: -> {s!=null}? ^( Cst_UnamedVariableStar )
                    if (s!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:377:17: ^( Cst_UnamedVariableStar )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_UnamedVariableStar, "Cst_UnamedVariableStar"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 378:3: -> ^( Cst_UnamedVariable )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:378:17: ^( Cst_UnamedVariable )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:382:3: csConstantValue (s= STAR )?
                    {
                    pushFollow(FOLLOW_csConstantValue_in_csPattern2464);
                    csConstantValue91=csConstantValue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csConstantValue.add(csConstantValue91.getTree());
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:382:19: (s= STAR )?
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==STAR) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:382:20: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_csPattern2469); if (state.failed) return retval; 
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
                    // 383:3: -> {s!=null}? ^( Cst_ConstantStar csConstantValue )
                    if (s!=null) {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:383:17: ^( Cst_ConstantStar csConstantValue )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ConstantStar, "Cst_ConstantStar"), root_1);

                        adaptor.addChild(root_1, stream_csConstantValue.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 384:3: -> ^( Cst_Constant csConstantValue )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:384:17: ^( Cst_Constant csConstantValue )
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:391:1: csHeadSymbolList : ( csHeadSymbol -> ^( ConcCstSymbol csHeadSymbol ) | LPAR csHeadSymbol ( PIPE csHeadSymbol )* RPAR -> ^( ConcCstSymbol ( csHeadSymbol )* ) );
    public final miniTomParser.csHeadSymbolList_return csHeadSymbolList() throws RecognitionException {
        miniTomParser.csHeadSymbolList_return retval = new miniTomParser.csHeadSymbolList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAR93=null;
        Token PIPE95=null;
        Token RPAR97=null;
        miniTomParser.csHeadSymbol_return csHeadSymbol92 = null;

        miniTomParser.csHeadSymbol_return csHeadSymbol94 = null;

        miniTomParser.csHeadSymbol_return csHeadSymbol96 = null;


        Tree LPAR93_tree=null;
        Tree PIPE95_tree=null;
        Tree RPAR97_tree=null;
        RewriteRuleTokenStream stream_PIPE=new RewriteRuleTokenStream(adaptor,"token PIPE");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleSubtreeStream stream_csHeadSymbol=new RewriteRuleSubtreeStream(adaptor,"rule csHeadSymbol");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:391:18: ( csHeadSymbol -> ^( ConcCstSymbol csHeadSymbol ) | LPAR csHeadSymbol ( PIPE csHeadSymbol )* RPAR -> ^( ConcCstSymbol ( csHeadSymbol )* ) )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==IDENTIFIER||(LA37_0>=INTEGER && LA37_0<=STRING)) ) {
                alt37=1;
            }
            else if ( (LA37_0==LPAR) ) {
                alt37=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:392:3: csHeadSymbol
                    {
                    pushFollow(FOLLOW_csHeadSymbol_in_csHeadSymbolList2519);
                    csHeadSymbol92=csHeadSymbol();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csHeadSymbol.add(csHeadSymbol92.getTree());


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
                    // 393:3: -> ^( ConcCstSymbol csHeadSymbol )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:393:6: ^( ConcCstSymbol csHeadSymbol )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:394:4: LPAR csHeadSymbol ( PIPE csHeadSymbol )* RPAR
                    {
                    LPAR93=(Token)match(input,LPAR,FOLLOW_LPAR_in_csHeadSymbolList2534); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR93);

                    pushFollow(FOLLOW_csHeadSymbol_in_csHeadSymbolList2536);
                    csHeadSymbol94=csHeadSymbol();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csHeadSymbol.add(csHeadSymbol94.getTree());
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:394:22: ( PIPE csHeadSymbol )*
                    loop36:
                    do {
                        int alt36=2;
                        int LA36_0 = input.LA(1);

                        if ( (LA36_0==PIPE) ) {
                            alt36=1;
                        }


                        switch (alt36) {
                    	case 1 :
                    	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:394:23: PIPE csHeadSymbol
                    	    {
                    	    PIPE95=(Token)match(input,PIPE,FOLLOW_PIPE_in_csHeadSymbolList2539); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_PIPE.add(PIPE95);

                    	    pushFollow(FOLLOW_csHeadSymbol_in_csHeadSymbolList2541);
                    	    csHeadSymbol96=csHeadSymbol();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csHeadSymbol.add(csHeadSymbol96.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop36;
                        }
                    } while (true);

                    RPAR97=(Token)match(input,RPAR,FOLLOW_RPAR_in_csHeadSymbolList2545); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR97);



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
                    // 395:3: -> ^( ConcCstSymbol ( csHeadSymbol )* )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:395:6: ^( ConcCstSymbol ( csHeadSymbol )* )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstSymbol, "ConcCstSymbol"), root_1);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:395:23: ( csHeadSymbol )*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:398:1: csHeadSymbol : ( IDENTIFIER -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) ) | IDENTIFIER QMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) ) | IDENTIFIER DQMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) ) | INTEGER -> ^( Cst_ConstantInt INTEGER ) | LONG -> ^( Cst_ConstantLong LONG ) | CHAR -> ^( Cst_ConstantChar CHAR ) | DOUBLE -> ^( Cst_ConstantDouble DOUBLE ) | STRING -> ^( Cst_ConstantString STRING ) );
    public final miniTomParser.csHeadSymbol_return csHeadSymbol() throws RecognitionException {
        miniTomParser.csHeadSymbol_return retval = new miniTomParser.csHeadSymbol_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token IDENTIFIER98=null;
        Token IDENTIFIER99=null;
        Token QMARK100=null;
        Token IDENTIFIER101=null;
        Token DQMARK102=null;
        Token INTEGER103=null;
        Token LONG104=null;
        Token CHAR105=null;
        Token DOUBLE106=null;
        Token STRING107=null;

        Tree IDENTIFIER98_tree=null;
        Tree IDENTIFIER99_tree=null;
        Tree QMARK100_tree=null;
        Tree IDENTIFIER101_tree=null;
        Tree DQMARK102_tree=null;
        Tree INTEGER103_tree=null;
        Tree LONG104_tree=null;
        Tree CHAR105_tree=null;
        Tree DOUBLE106_tree=null;
        Tree STRING107_tree=null;
        RewriteRuleTokenStream stream_CHAR=new RewriteRuleTokenStream(adaptor,"token CHAR");
        RewriteRuleTokenStream stream_INTEGER=new RewriteRuleTokenStream(adaptor,"token INTEGER");
        RewriteRuleTokenStream stream_DOUBLE=new RewriteRuleTokenStream(adaptor,"token DOUBLE");
        RewriteRuleTokenStream stream_DQMARK=new RewriteRuleTokenStream(adaptor,"token DQMARK");
        RewriteRuleTokenStream stream_LONG=new RewriteRuleTokenStream(adaptor,"token LONG");
        RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:398:14: ( IDENTIFIER -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) ) | IDENTIFIER QMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) ) | IDENTIFIER DQMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) ) | INTEGER -> ^( Cst_ConstantInt INTEGER ) | LONG -> ^( Cst_ConstantLong LONG ) | CHAR -> ^( Cst_ConstantChar CHAR ) | DOUBLE -> ^( Cst_ConstantDouble DOUBLE ) | STRING -> ^( Cst_ConstantString STRING ) )
            int alt38=8;
            alt38 = dfa38.predict(input);
            switch (alt38) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:399:3: IDENTIFIER
                    {
                    IDENTIFIER98=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csHeadSymbol2570); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER98);



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
                    // 400:3: -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:400:6: ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Symbol, "Cst_Symbol"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:400:30: ^( Cst_TheoryDEFAULT )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:401:3: IDENTIFIER QMARK
                    {
                    IDENTIFIER99=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csHeadSymbol2588); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER99);

                    QMARK100=(Token)match(input,QMARK,FOLLOW_QMARK_in_csHeadSymbol2590); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QMARK.add(QMARK100);



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
                    // 402:3: -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:402:6: ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Symbol, "Cst_Symbol"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:402:30: ^( Cst_TheoryAU )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:403:3: IDENTIFIER DQMARK
                    {
                    IDENTIFIER101=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csHeadSymbol2608); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER101);

                    DQMARK102=(Token)match(input,DQMARK,FOLLOW_DQMARK_in_csHeadSymbol2610); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DQMARK.add(DQMARK102);



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
                    // 404:3: -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:404:6: ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Symbol, "Cst_Symbol"), root_1);

                        adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:404:30: ^( Cst_TheoryAC )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:405:3: INTEGER
                    {
                    INTEGER103=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_csHeadSymbol2628); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INTEGER.add(INTEGER103);



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
                    // 406:3: -> ^( Cst_ConstantInt INTEGER )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:406:6: ^( Cst_ConstantInt INTEGER )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:407:3: LONG
                    {
                    LONG104=(Token)match(input,LONG,FOLLOW_LONG_in_csHeadSymbol2642); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LONG.add(LONG104);



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
                    // 408:3: -> ^( Cst_ConstantLong LONG )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:408:6: ^( Cst_ConstantLong LONG )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:409:3: CHAR
                    {
                    CHAR105=(Token)match(input,CHAR,FOLLOW_CHAR_in_csHeadSymbol2656); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CHAR.add(CHAR105);



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
                    // 410:3: -> ^( Cst_ConstantChar CHAR )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:410:6: ^( Cst_ConstantChar CHAR )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:411:3: DOUBLE
                    {
                    DOUBLE106=(Token)match(input,DOUBLE,FOLLOW_DOUBLE_in_csHeadSymbol2670); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DOUBLE.add(DOUBLE106);



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
                    // 412:3: -> ^( Cst_ConstantDouble DOUBLE )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:412:6: ^( Cst_ConstantDouble DOUBLE )
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:413:3: STRING
                    {
                    STRING107=(Token)match(input,STRING,FOLLOW_STRING_in_csHeadSymbol2684); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STRING.add(STRING107);



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
                    // 414:3: -> ^( Cst_ConstantString STRING )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:414:6: ^( Cst_ConstantString STRING )
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:417:1: csConstantValue : ( INTEGER | LONG | CHAR | DOUBLE | STRING );
    public final miniTomParser.csConstantValue_return csConstantValue() throws RecognitionException {
        miniTomParser.csConstantValue_return retval = new miniTomParser.csConstantValue_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token set108=null;

        Tree set108_tree=null;

        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:417:17: ( INTEGER | LONG | CHAR | DOUBLE | STRING )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:
            {
            root_0 = (Tree)adaptor.nil();

            set108=(Token)input.LT(1);
            if ( (input.LA(1)>=INTEGER && input.LA(1)<=STRING) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Tree)adaptor.create(set108));
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:421:1: csExplicitTermList : LPAR ( csPattern ( COMMA csPattern )* )? RPAR -> ^( ConcCstPattern ( csPattern )* ) ;
    public final miniTomParser.csExplicitTermList_return csExplicitTermList() throws RecognitionException {
        miniTomParser.csExplicitTermList_return retval = new miniTomParser.csExplicitTermList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAR109=null;
        Token COMMA111=null;
        Token RPAR113=null;
        miniTomParser.csPattern_return csPattern110 = null;

        miniTomParser.csPattern_return csPattern112 = null;


        Tree LPAR109_tree=null;
        Tree COMMA111_tree=null;
        Tree RPAR113_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_csPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPattern");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:421:20: ( LPAR ( csPattern ( COMMA csPattern )* )? RPAR -> ^( ConcCstPattern ( csPattern )* ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:422:4: LPAR ( csPattern ( COMMA csPattern )* )? RPAR
            {
            LPAR109=(Token)match(input,LPAR,FOLLOW_LPAR_in_csExplicitTermList2727); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR109);

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:422:9: ( csPattern ( COMMA csPattern )* )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==IDENTIFIER||LA40_0==LPAR||(LA40_0>=ANTI && LA40_0<=UNDERSCORE)||(LA40_0>=INTEGER && LA40_0<=STRING)) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:422:10: csPattern ( COMMA csPattern )*
                    {
                    pushFollow(FOLLOW_csPattern_in_csExplicitTermList2730);
                    csPattern110=csPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPattern.add(csPattern110.getTree());
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:422:20: ( COMMA csPattern )*
                    loop39:
                    do {
                        int alt39=2;
                        int LA39_0 = input.LA(1);

                        if ( (LA39_0==COMMA) ) {
                            alt39=1;
                        }


                        switch (alt39) {
                    	case 1 :
                    	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:422:21: COMMA csPattern
                    	    {
                    	    COMMA111=(Token)match(input,COMMA,FOLLOW_COMMA_in_csExplicitTermList2733); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA111);

                    	    pushFollow(FOLLOW_csPattern_in_csExplicitTermList2735);
                    	    csPattern112=csPattern();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csPattern.add(csPattern112.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop39;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR113=(Token)match(input,RPAR,FOLLOW_RPAR_in_csExplicitTermList2741); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR113);



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
            // 424:2: -> ^( ConcCstPattern ( csPattern )* )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:424:5: ^( ConcCstPattern ( csPattern )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstPattern, "ConcCstPattern"), root_1);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:424:22: ( csPattern )*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:427:1: csImplicitPairList : LSQUAREBR ( csPairPattern ( COMMA csPairPattern )* )? RSQUAREBR -> ^( ConcCstPairPattern ( csPairPattern )* ) ;
    public final miniTomParser.csImplicitPairList_return csImplicitPairList() throws RecognitionException {
        miniTomParser.csImplicitPairList_return retval = new miniTomParser.csImplicitPairList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LSQUAREBR114=null;
        Token COMMA116=null;
        Token RSQUAREBR118=null;
        miniTomParser.csPairPattern_return csPairPattern115 = null;

        miniTomParser.csPairPattern_return csPairPattern117 = null;


        Tree LSQUAREBR114_tree=null;
        Tree COMMA116_tree=null;
        Tree RSQUAREBR118_tree=null;
        RewriteRuleTokenStream stream_LSQUAREBR=new RewriteRuleTokenStream(adaptor,"token LSQUAREBR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RSQUAREBR=new RewriteRuleTokenStream(adaptor,"token RSQUAREBR");
        RewriteRuleSubtreeStream stream_csPairPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPairPattern");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:427:20: ( LSQUAREBR ( csPairPattern ( COMMA csPairPattern )* )? RSQUAREBR -> ^( ConcCstPairPattern ( csPairPattern )* ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:428:3: LSQUAREBR ( csPairPattern ( COMMA csPairPattern )* )? RSQUAREBR
            {
            LSQUAREBR114=(Token)match(input,LSQUAREBR,FOLLOW_LSQUAREBR_in_csImplicitPairList2763); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LSQUAREBR.add(LSQUAREBR114);

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:428:13: ( csPairPattern ( COMMA csPairPattern )* )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==IDENTIFIER) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:428:14: csPairPattern ( COMMA csPairPattern )*
                    {
                    pushFollow(FOLLOW_csPairPattern_in_csImplicitPairList2766);
                    csPairPattern115=csPairPattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csPairPattern.add(csPairPattern115.getTree());
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:428:28: ( COMMA csPairPattern )*
                    loop41:
                    do {
                        int alt41=2;
                        int LA41_0 = input.LA(1);

                        if ( (LA41_0==COMMA) ) {
                            alt41=1;
                        }


                        switch (alt41) {
                    	case 1 :
                    	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:428:29: COMMA csPairPattern
                    	    {
                    	    COMMA116=(Token)match(input,COMMA,FOLLOW_COMMA_in_csImplicitPairList2769); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA116);

                    	    pushFollow(FOLLOW_csPairPattern_in_csImplicitPairList2771);
                    	    csPairPattern117=csPairPattern();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csPairPattern.add(csPairPattern117.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop41;
                        }
                    } while (true);


                    }
                    break;

            }

            RSQUAREBR118=(Token)match(input,RSQUAREBR,FOLLOW_RSQUAREBR_in_csImplicitPairList2778); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RSQUAREBR.add(RSQUAREBR118);



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
            // 430:3: -> ^( ConcCstPairPattern ( csPairPattern )* )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:430:6: ^( ConcCstPairPattern ( csPairPattern )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstPairPattern, "ConcCstPairPattern"), root_1);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:430:27: ( csPairPattern )*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:433:1: csPairPattern : IDENTIFIER EQUAL csPattern -> ^( Cst_PairPattern IDENTIFIER csPattern ) ;
    public final miniTomParser.csPairPattern_return csPairPattern() throws RecognitionException {
        miniTomParser.csPairPattern_return retval = new miniTomParser.csPairPattern_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token IDENTIFIER119=null;
        Token EQUAL120=null;
        miniTomParser.csPattern_return csPattern121 = null;


        Tree IDENTIFIER119_tree=null;
        Tree EQUAL120_tree=null;
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_EQUAL=new RewriteRuleTokenStream(adaptor,"token EQUAL");
        RewriteRuleSubtreeStream stream_csPattern=new RewriteRuleSubtreeStream(adaptor,"rule csPattern");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:433:15: ( IDENTIFIER EQUAL csPattern -> ^( Cst_PairPattern IDENTIFIER csPattern ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:434:2: IDENTIFIER EQUAL csPattern
            {
            IDENTIFIER119=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csPairPattern2800); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER119);

            EQUAL120=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_csPairPattern2802); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EQUAL.add(EQUAL120);

            pushFollow(FOLLOW_csPattern_in_csPairPattern2804);
            csPattern121=csPattern();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csPattern.add(csPattern121.getTree());


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
            // 436:2: -> ^( Cst_PairPattern IDENTIFIER csPattern )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:436:5: ^( Cst_PairPattern IDENTIFIER csPattern )
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:440:1: csOperatorConstruct returns [int marker] : codomain= IDENTIFIER ctorName= csName LPAR csSlotList RPAR LBR (ks+= csKeywordIsFsym | ks+= csKeywordMake | ks+= csKeywordGetSlot | ks+= csKeywordGetDefault )* RBR -> ^( Cst_OpConstruct ^( Cst_Type $codomain) $ctorName csSlotList ^( ConcCstOperator ( $ks)* ) ) ;
    public final miniTomParser.csOperatorConstruct_return csOperatorConstruct() throws RecognitionException {
        miniTomParser.csOperatorConstruct_return retval = new miniTomParser.csOperatorConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token codomain=null;
        Token LPAR122=null;
        Token RPAR124=null;
        Token LBR125=null;
        Token RBR126=null;
        List list_ks=null;
        miniTomParser.csName_return ctorName = null;

        miniTomParser.csSlotList_return csSlotList123 = null;

        RuleReturnScope ks = null;
        Tree codomain_tree=null;
        Tree LPAR122_tree=null;
        Tree RPAR124_tree=null;
        Tree LBR125_tree=null;
        Tree RBR126_tree=null;
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
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:441:22: (codomain= IDENTIFIER ctorName= csName LPAR csSlotList RPAR LBR (ks+= csKeywordIsFsym | ks+= csKeywordMake | ks+= csKeywordGetSlot | ks+= csKeywordGetDefault )* RBR -> ^( Cst_OpConstruct ^( Cst_Type $codomain) $ctorName csSlotList ^( ConcCstOperator ( $ks)* ) ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:444:3: codomain= IDENTIFIER ctorName= csName LPAR csSlotList RPAR LBR (ks+= csKeywordIsFsym | ks+= csKeywordMake | ks+= csKeywordGetSlot | ks+= csKeywordGetDefault )* RBR
            {
            codomain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorConstruct2839); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(codomain);

            pushFollow(FOLLOW_csName_in_csOperatorConstruct2843);
            ctorName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(ctorName.getTree());
            LPAR122=(Token)match(input,LPAR,FOLLOW_LPAR_in_csOperatorConstruct2845); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR122);

            pushFollow(FOLLOW_csSlotList_in_csOperatorConstruct2847);
            csSlotList123=csSlotList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csSlotList.add(csSlotList123.getTree());
            RPAR124=(Token)match(input,RPAR,FOLLOW_RPAR_in_csOperatorConstruct2849); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR124);

            LBR125=(Token)match(input,LBR,FOLLOW_LBR_in_csOperatorConstruct2853); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR125);

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:446:5: (ks+= csKeywordIsFsym | ks+= csKeywordMake | ks+= csKeywordGetSlot | ks+= csKeywordGetDefault )*
            loop43:
            do {
                int alt43=5;
                switch ( input.LA(1) ) {
                case KEYWORD_IS_FSYM:
                    {
                    alt43=1;
                    }
                    break;
                case KEYWORD_MAKE:
                    {
                    alt43=2;
                    }
                    break;
                case KEYWORD_GET_SLOT:
                    {
                    alt43=3;
                    }
                    break;
                case KEYWORD_GET_DEFAULT:
                    {
                    alt43=4;
                    }
                    break;

                }

                switch (alt43) {
            	case 1 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:446:8: ks+= csKeywordIsFsym
            	    {
            	    pushFollow(FOLLOW_csKeywordIsFsym_in_csOperatorConstruct2865);
            	    ks=csKeywordIsFsym();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordIsFsym.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:447:8: ks+= csKeywordMake
            	    {
            	    pushFollow(FOLLOW_csKeywordMake_in_csOperatorConstruct2876);
            	    ks=csKeywordMake();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMake.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:448:8: ks+= csKeywordGetSlot
            	    {
            	    pushFollow(FOLLOW_csKeywordGetSlot_in_csOperatorConstruct2887);
            	    ks=csKeywordGetSlot();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetSlot.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 4 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:449:8: ks+= csKeywordGetDefault
            	    {
            	    pushFollow(FOLLOW_csKeywordGetDefault_in_csOperatorConstruct2898);
            	    ks=csKeywordGetDefault();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetDefault.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);

            RBR126=(Token)match(input,RBR,FOLLOW_RBR_in_csOperatorConstruct2909); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR126);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR126).getPayload(Integer.class);
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
            // 455:3: -> ^( Cst_OpConstruct ^( Cst_Type $codomain) $ctorName csSlotList ^( ConcCstOperator ( $ks)* ) )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:455:6: ^( Cst_OpConstruct ^( Cst_Type $codomain) $ctorName csSlotList ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OpConstruct, "Cst_OpConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)codomain, (CommonToken)RBR126));
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:457:9: ^( Cst_Type $codomain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_codomain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_ctorName.nextTree());
                adaptor.addChild(root_1, stream_csSlotList.nextTree());
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:457:52: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:457:70: ( $ks)*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:461:1: csOperatorArrayConstruct returns [int marker] : codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_Array | ks+= csKeywordMakeAppend | ks+= csKeywordGetElement | ks+= csKeywordGetSize )* RBR -> ^( Cst_OpArrayConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) ) ;
    public final miniTomParser.csOperatorArrayConstruct_return csOperatorArrayConstruct() throws RecognitionException {
        miniTomParser.csOperatorArrayConstruct_return retval = new miniTomParser.csOperatorArrayConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token codomain=null;
        Token domain=null;
        Token LPAR127=null;
        Token STAR128=null;
        Token RPAR129=null;
        Token LBR130=null;
        Token RBR131=null;
        List list_ks=null;
        miniTomParser.csName_return ctorName = null;

        RuleReturnScope ks = null;
        Tree codomain_tree=null;
        Tree domain_tree=null;
        Tree LPAR127_tree=null;
        Tree STAR128_tree=null;
        Tree RPAR129_tree=null;
        Tree LBR130_tree=null;
        Tree RBR131_tree=null;
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:462:22: (codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_Array | ks+= csKeywordMakeAppend | ks+= csKeywordGetElement | ks+= csKeywordGetSize )* RBR -> ^( Cst_OpArrayConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:465:3: codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_Array | ks+= csKeywordMakeAppend | ks+= csKeywordGetElement | ks+= csKeywordGetSize )* RBR
            {
            codomain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorArrayConstruct2990); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(codomain);

            pushFollow(FOLLOW_csName_in_csOperatorArrayConstruct2994);
            ctorName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(ctorName.getTree());
            LPAR127=(Token)match(input,LPAR,FOLLOW_LPAR_in_csOperatorArrayConstruct2996); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR127);

            domain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorArrayConstruct3000); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(domain);

            STAR128=(Token)match(input,STAR,FOLLOW_STAR_in_csOperatorArrayConstruct3002); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STAR.add(STAR128);

            RPAR129=(Token)match(input,RPAR,FOLLOW_RPAR_in_csOperatorArrayConstruct3004); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR129);

            LBR130=(Token)match(input,LBR,FOLLOW_LBR_in_csOperatorArrayConstruct3008); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR130);

            pushFollow(FOLLOW_csKeywordIsFsym_in_csOperatorArrayConstruct3019);
            ks=csKeywordIsFsym();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csKeywordIsFsym.add(ks.getTree());
            if (list_ks==null) list_ks=new ArrayList();
            list_ks.add(ks.getTree());

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:468:4: (ks+= csKeywordMakeEmpty_Array | ks+= csKeywordMakeAppend | ks+= csKeywordGetElement | ks+= csKeywordGetSize )*
            loop44:
            do {
                int alt44=5;
                switch ( input.LA(1) ) {
                case KEYWORD_MAKE_EMPTY:
                    {
                    alt44=1;
                    }
                    break;
                case KEYWORD_MAKE_APPEND:
                    {
                    alt44=2;
                    }
                    break;
                case KEYWORD_GET_ELEMENT:
                    {
                    alt44=3;
                    }
                    break;
                case KEYWORD_GET_SIZE:
                    {
                    alt44=4;
                    }
                    break;

                }

                switch (alt44) {
            	case 1 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:468:6: ks+= csKeywordMakeEmpty_Array
            	    {
            	    pushFollow(FOLLOW_csKeywordMakeEmpty_Array_in_csOperatorArrayConstruct3028);
            	    ks=csKeywordMakeEmpty_Array();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMakeEmpty_Array.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:469:6: ks+= csKeywordMakeAppend
            	    {
            	    pushFollow(FOLLOW_csKeywordMakeAppend_in_csOperatorArrayConstruct3037);
            	    ks=csKeywordMakeAppend();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMakeAppend.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:470:6: ks+= csKeywordGetElement
            	    {
            	    pushFollow(FOLLOW_csKeywordGetElement_in_csOperatorArrayConstruct3046);
            	    ks=csKeywordGetElement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetElement.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 4 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:471:6: ks+= csKeywordGetSize
            	    {
            	    pushFollow(FOLLOW_csKeywordGetSize_in_csOperatorArrayConstruct3055);
            	    ks=csKeywordGetSize();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetSize.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;

            	default :
            	    break loop44;
                }
            } while (true);

            RBR131=(Token)match(input,RBR,FOLLOW_RBR_in_csOperatorArrayConstruct3065); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR131);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR131).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: ks, codomain, domain, ctorName
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
            // 477:3: -> ^( Cst_OpArrayConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:477:6: ^( Cst_OpArrayConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OpArrayConstruct, "Cst_OpArrayConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)codomain, (CommonToken)RBR131));
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:479:9: ^( Cst_Type $codomain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_codomain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_ctorName.nextTree());
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:479:41: ^( Cst_Type $domain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_domain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:479:61: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:479:79: ( $ks)*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:483:1: csOperatorListConstruct returns [int marker] : codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_List | ks+= csKeywordMakeInsert | ks+= csKeywordGetHead | ks+= csKeywordGetTail | ks+= csKeywordIsEmpty )* RBR -> ^( Cst_OpListConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) ) ;
    public final miniTomParser.csOperatorListConstruct_return csOperatorListConstruct() throws RecognitionException {
        miniTomParser.csOperatorListConstruct_return retval = new miniTomParser.csOperatorListConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token codomain=null;
        Token domain=null;
        Token LPAR132=null;
        Token STAR133=null;
        Token RPAR134=null;
        Token LBR135=null;
        Token RBR136=null;
        List list_ks=null;
        miniTomParser.csName_return ctorName = null;

        RuleReturnScope ks = null;
        Tree codomain_tree=null;
        Tree domain_tree=null;
        Tree LPAR132_tree=null;
        Tree STAR133_tree=null;
        Tree RPAR134_tree=null;
        Tree LBR135_tree=null;
        Tree RBR136_tree=null;
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
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:484:22: (codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_List | ks+= csKeywordMakeInsert | ks+= csKeywordGetHead | ks+= csKeywordGetTail | ks+= csKeywordIsEmpty )* RBR -> ^( Cst_OpListConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:487:3: codomain= IDENTIFIER ctorName= csName LPAR domain= IDENTIFIER STAR RPAR LBR ks+= csKeywordIsFsym (ks+= csKeywordMakeEmpty_List | ks+= csKeywordMakeInsert | ks+= csKeywordGetHead | ks+= csKeywordGetTail | ks+= csKeywordIsEmpty )* RBR
            {
            codomain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorListConstruct3151); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(codomain);

            pushFollow(FOLLOW_csName_in_csOperatorListConstruct3155);
            ctorName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(ctorName.getTree());
            LPAR132=(Token)match(input,LPAR,FOLLOW_LPAR_in_csOperatorListConstruct3157); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR132);

            domain=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csOperatorListConstruct3161); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(domain);

            STAR133=(Token)match(input,STAR,FOLLOW_STAR_in_csOperatorListConstruct3163); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STAR.add(STAR133);

            RPAR134=(Token)match(input,RPAR,FOLLOW_RPAR_in_csOperatorListConstruct3165); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR134);

            LBR135=(Token)match(input,LBR,FOLLOW_LBR_in_csOperatorListConstruct3169); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR135);

            pushFollow(FOLLOW_csKeywordIsFsym_in_csOperatorListConstruct3178);
            ks=csKeywordIsFsym();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csKeywordIsFsym.add(ks.getTree());
            if (list_ks==null) list_ks=new ArrayList();
            list_ks.add(ks.getTree());

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:490:4: (ks+= csKeywordMakeEmpty_List | ks+= csKeywordMakeInsert | ks+= csKeywordGetHead | ks+= csKeywordGetTail | ks+= csKeywordIsEmpty )*
            loop45:
            do {
                int alt45=6;
                switch ( input.LA(1) ) {
                case KEYWORD_MAKE_EMPTY:
                    {
                    alt45=1;
                    }
                    break;
                case KEYWORD_MAKE_INSERT:
                    {
                    alt45=2;
                    }
                    break;
                case KEYWORD_GET_HEAD:
                    {
                    alt45=3;
                    }
                    break;
                case KEYWORD_GET_TAIL:
                    {
                    alt45=4;
                    }
                    break;
                case KEYWORD_IS_EMPTY:
                    {
                    alt45=5;
                    }
                    break;

                }

                switch (alt45) {
            	case 1 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:490:6: ks+= csKeywordMakeEmpty_List
            	    {
            	    pushFollow(FOLLOW_csKeywordMakeEmpty_List_in_csOperatorListConstruct3187);
            	    ks=csKeywordMakeEmpty_List();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMakeEmpty_List.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:491:6: ks+= csKeywordMakeInsert
            	    {
            	    pushFollow(FOLLOW_csKeywordMakeInsert_in_csOperatorListConstruct3196);
            	    ks=csKeywordMakeInsert();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordMakeInsert.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:492:6: ks+= csKeywordGetHead
            	    {
            	    pushFollow(FOLLOW_csKeywordGetHead_in_csOperatorListConstruct3205);
            	    ks=csKeywordGetHead();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetHead.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 4 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:493:6: ks+= csKeywordGetTail
            	    {
            	    pushFollow(FOLLOW_csKeywordGetTail_in_csOperatorListConstruct3214);
            	    ks=csKeywordGetTail();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordGetTail.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;
            	case 5 :
            	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:494:6: ks+= csKeywordIsEmpty
            	    {
            	    pushFollow(FOLLOW_csKeywordIsEmpty_in_csOperatorListConstruct3223);
            	    ks=csKeywordIsEmpty();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csKeywordIsEmpty.add(ks.getTree());
            	    if (list_ks==null) list_ks=new ArrayList();
            	    list_ks.add(ks.getTree());


            	    }
            	    break;

            	default :
            	    break loop45;
                }
            } while (true);

            RBR136=(Token)match(input,RBR,FOLLOW_RBR_in_csOperatorListConstruct3233); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR136);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR136).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: ctorName, ks, codomain, domain
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
            // 500:3: -> ^( Cst_OpListConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:500:6: ^( Cst_OpListConstruct ^( Cst_Type $codomain) $ctorName ^( Cst_Type $domain) ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_OpListConstruct, "Cst_OpListConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)codomain, (CommonToken)RBR136));
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:502:9: ^( Cst_Type $codomain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_codomain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_ctorName.nextTree());
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:502:41: ^( Cst_Type $domain)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_domain.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:502:61: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:502:79: ( $ks)*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:507:1: csTypetermConstruct returns [int marker] : typeName= IDENTIFIER ( EXTENDS extend= IDENTIFIER )? LBR ks+= csKeywordImplement (ks+= csKeywordIsSort )? (ks+= csKeywordEquals )? RBR -> {extend==null}? ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_TypeUnknown ) ^( ConcCstOperator ( $ks)* ) ) -> ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_Type $extend) ^( ConcCstOperator ( $ks)* ) ) ;
    public final miniTomParser.csTypetermConstruct_return csTypetermConstruct() throws RecognitionException {
        miniTomParser.csTypetermConstruct_return retval = new miniTomParser.csTypetermConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token typeName=null;
        Token extend=null;
        Token EXTENDS137=null;
        Token LBR138=null;
        Token RBR139=null;
        List list_ks=null;
        RuleReturnScope ks = null;
        Tree typeName_tree=null;
        Tree extend_tree=null;
        Tree EXTENDS137_tree=null;
        Tree LBR138_tree=null;
        Tree RBR139_tree=null;
        RewriteRuleTokenStream stream_EXTENDS=new RewriteRuleTokenStream(adaptor,"token EXTENDS");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csKeywordImplement=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordImplement");
        RewriteRuleSubtreeStream stream_csKeywordIsSort=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordIsSort");
        RewriteRuleSubtreeStream stream_csKeywordEquals=new RewriteRuleSubtreeStream(adaptor,"rule csKeywordEquals");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:508:22: (typeName= IDENTIFIER ( EXTENDS extend= IDENTIFIER )? LBR ks+= csKeywordImplement (ks+= csKeywordIsSort )? (ks+= csKeywordEquals )? RBR -> {extend==null}? ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_TypeUnknown ) ^( ConcCstOperator ( $ks)* ) ) -> ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_Type $extend) ^( ConcCstOperator ( $ks)* ) ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:511:3: typeName= IDENTIFIER ( EXTENDS extend= IDENTIFIER )? LBR ks+= csKeywordImplement (ks+= csKeywordIsSort )? (ks+= csKeywordEquals )? RBR
            {
            typeName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csTypetermConstruct3320); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(typeName);

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:511:23: ( EXTENDS extend= IDENTIFIER )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==EXTENDS) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:511:24: EXTENDS extend= IDENTIFIER
                    {
                    EXTENDS137=(Token)match(input,EXTENDS,FOLLOW_EXTENDS_in_csTypetermConstruct3323); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EXTENDS.add(EXTENDS137);

                    extend=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csTypetermConstruct3327); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(extend);


                    }
                    break;

            }

            LBR138=(Token)match(input,LBR,FOLLOW_LBR_in_csTypetermConstruct3333); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR138);

            pushFollow(FOLLOW_csKeywordImplement_in_csTypetermConstruct3341);
            ks=csKeywordImplement();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csKeywordImplement.add(ks.getTree());
            if (list_ks==null) list_ks=new ArrayList();
            list_ks.add(ks.getTree());

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:513:28: (ks+= csKeywordIsSort )?
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==KEYWORD_IS_SORT) ) {
                alt47=1;
            }
            switch (alt47) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:513:29: ks+= csKeywordIsSort
                    {
                    pushFollow(FOLLOW_csKeywordIsSort_in_csTypetermConstruct3346);
                    ks=csKeywordIsSort();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csKeywordIsSort.add(ks.getTree());
                    if (list_ks==null) list_ks=new ArrayList();
                    list_ks.add(ks.getTree());


                    }
                    break;

            }

            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:513:51: (ks+= csKeywordEquals )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==KEYWORD_EQUALS) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:513:52: ks+= csKeywordEquals
                    {
                    pushFollow(FOLLOW_csKeywordEquals_in_csTypetermConstruct3353);
                    ks=csKeywordEquals();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csKeywordEquals.add(ks.getTree());
                    if (list_ks==null) list_ks=new ArrayList();
                    list_ks.add(ks.getTree());


                    }
                    break;

            }

            RBR139=(Token)match(input,RBR,FOLLOW_RBR_in_csTypetermConstruct3359); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR139);

            if ( state.backtracking==0 ) {
              retval.marker = ((CustomToken)RBR139).getPayload(Integer.class);
            }


            // AST REWRITE
            // elements: typeName, ks, extend, typeName, ks
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
            // 518:3: -> {extend==null}? ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_TypeUnknown ) ^( ConcCstOperator ( $ks)* ) )
            if (extend==null) {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:519:4: ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_TypeUnknown ) ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypetermConstruct, "Cst_TypetermConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)typeName, (CommonToken)RBR139));
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:521:9: ^( Cst_Type $typeName)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_typeName.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:521:31: ^( Cst_TypeUnknown )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:521:50: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:521:68: ( $ks)*
                while ( stream_ks.hasNext() ) {
                    adaptor.addChild(root_2, stream_ks.nextTree());

                }
                stream_ks.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 524:3: -> ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_Type $extend) ^( ConcCstOperator ( $ks)* ) )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:525:4: ^( Cst_TypetermConstruct ^( Cst_Type $typeName) ^( Cst_Type $extend) ^( ConcCstOperator ( $ks)* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypetermConstruct, "Cst_TypetermConstruct"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)typeName, (CommonToken)RBR139));
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:527:9: ^( Cst_Type $typeName)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_typeName.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:527:31: ^( Cst_Type $extend)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Type, "Cst_Type"), root_2);

                adaptor.addChild(root_2, stream_extend.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:527:51: ^( ConcCstOperator ( $ks)* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstOperator, "ConcCstOperator"), root_2);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:527:69: ( $ks)*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:531:1: csSlotList : ( csSlot ( COMMA csSlot )* )? -> ^( ConcCstSlot ( csSlot )* ) ;
    public final miniTomParser.csSlotList_return csSlotList() throws RecognitionException {
        miniTomParser.csSlotList_return retval = new miniTomParser.csSlotList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token COMMA141=null;
        miniTomParser.csSlot_return csSlot140 = null;

        miniTomParser.csSlot_return csSlot142 = null;


        Tree COMMA141_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_csSlot=new RewriteRuleSubtreeStream(adaptor,"rule csSlot");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:531:12: ( ( csSlot ( COMMA csSlot )* )? -> ^( ConcCstSlot ( csSlot )* ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:532:3: ( csSlot ( COMMA csSlot )* )?
            {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:532:3: ( csSlot ( COMMA csSlot )* )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==IDENTIFIER) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:532:4: csSlot ( COMMA csSlot )*
                    {
                    pushFollow(FOLLOW_csSlot_in_csSlotList3491);
                    csSlot140=csSlot();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csSlot.add(csSlot140.getTree());
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:532:11: ( COMMA csSlot )*
                    loop49:
                    do {
                        int alt49=2;
                        int LA49_0 = input.LA(1);

                        if ( (LA49_0==COMMA) ) {
                            alt49=1;
                        }


                        switch (alt49) {
                    	case 1 :
                    	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:532:12: COMMA csSlot
                    	    {
                    	    COMMA141=(Token)match(input,COMMA,FOLLOW_COMMA_in_csSlotList3494); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA141);

                    	    pushFollow(FOLLOW_csSlot_in_csSlotList3496);
                    	    csSlot142=csSlot();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csSlot.add(csSlot142.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop49;
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
            // 532:30: -> ^( ConcCstSlot ( csSlot )* )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:532:33: ^( ConcCstSlot ( csSlot )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstSlot, "ConcCstSlot"), root_1);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:532:47: ( csSlot )*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:535:1: csSlot : (name= IDENTIFIER COLON type= IDENTIFIER -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) ) | type= IDENTIFIER name= IDENTIFIER -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) ) );
    public final miniTomParser.csSlot_return csSlot() throws RecognitionException {
        miniTomParser.csSlot_return retval = new miniTomParser.csSlot_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token name=null;
        Token type=null;
        Token COLON143=null;

        Tree name_tree=null;
        Tree type_tree=null;
        Tree COLON143_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");

        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:535:8: (name= IDENTIFIER COLON type= IDENTIFIER -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) ) | type= IDENTIFIER name= IDENTIFIER -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) ) )
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==IDENTIFIER) ) {
                int LA51_1 = input.LA(2);

                if ( (LA51_1==COLON) ) {
                    alt51=1;
                }
                else if ( (LA51_1==IDENTIFIER) ) {
                    alt51=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 51, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                throw nvae;
            }
            switch (alt51) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:536:5: name= IDENTIFIER COLON type= IDENTIFIER
                    {
                    name=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csSlot3526); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(name);

                    COLON143=(Token)match(input,COLON,FOLLOW_COLON_in_csSlot3528); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(COLON143);

                    type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csSlot3532); if (state.failed) return retval; 
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
                    // 536:43: -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:536:46: ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Slot, "Cst_Slot"), root_1);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:536:57: ^( Cst_Name $name)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Name, "Cst_Name"), root_2);

                        adaptor.addChild(root_2, stream_name.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:536:75: ^( Cst_Type $type)
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
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:537:5: type= IDENTIFIER name= IDENTIFIER
                    {
                    type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csSlot3560); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(type);

                    name=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csSlot3564); if (state.failed) return retval; 
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
                    // 537:37: -> ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) )
                    {
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:537:41: ^( Cst_Slot ^( Cst_Name $name) ^( Cst_Type $type) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Slot, "Cst_Slot"), root_1);

                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:537:52: ^( Cst_Name $name)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Name, "Cst_Name"), root_2);

                        adaptor.addChild(root_2, stream_name.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:537:70: ^( Cst_Type $type)
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:542:1: csKeywordIsFsym : KEYWORD_IS_FSYM LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsFsym $argName) ;
    public final miniTomParser.csKeywordIsFsym_return csKeywordIsFsym() throws RecognitionException {
        miniTomParser.csKeywordIsFsym_return retval = new miniTomParser.csKeywordIsFsym_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_IS_FSYM144=null;
        Token LPAR145=null;
        Token RPAR146=null;
        Token LBR147=null;
        Token RBR148=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_IS_FSYM144_tree=null;
        Tree LPAR145_tree=null;
        Tree RPAR146_tree=null;
        Tree LBR147_tree=null;
        Tree RBR148_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_IS_FSYM=new RewriteRuleTokenStream(adaptor,"token KEYWORD_IS_FSYM");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:542:17: ( KEYWORD_IS_FSYM LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsFsym $argName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:543:3: KEYWORD_IS_FSYM LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_IS_FSYM144=(Token)match(input,KEYWORD_IS_FSYM,FOLLOW_KEYWORD_IS_FSYM_in_csKeywordIsFsym3598); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_IS_FSYM.add(KEYWORD_IS_FSYM144);

            LPAR145=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordIsFsym3600); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR145);

            pushFollow(FOLLOW_csName_in_csKeywordIsFsym3604);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR146=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordIsFsym3606); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR146);

            LBR147=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordIsFsym3610); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR147);

            RBR148=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordIsFsym3614); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR148);



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
            // 546:3: -> ^( Cst_IsFsym $argName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:546:6: ^( Cst_IsFsym $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_IsFsym, "Cst_IsFsym"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR147).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:551:1: csKeywordGetSlot : KEYWORD_GET_SLOT LPAR slotName= csName COMMA termName= csName RPAR LBR RBR -> ^( Cst_GetSlot $slotName $termName) ;
    public final miniTomParser.csKeywordGetSlot_return csKeywordGetSlot() throws RecognitionException {
        miniTomParser.csKeywordGetSlot_return retval = new miniTomParser.csKeywordGetSlot_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_SLOT149=null;
        Token LPAR150=null;
        Token COMMA151=null;
        Token RPAR152=null;
        Token LBR153=null;
        Token RBR154=null;
        miniTomParser.csName_return slotName = null;

        miniTomParser.csName_return termName = null;


        Tree KEYWORD_GET_SLOT149_tree=null;
        Tree LPAR150_tree=null;
        Tree COMMA151_tree=null;
        Tree RPAR152_tree=null;
        Tree LBR153_tree=null;
        Tree RBR154_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_KEYWORD_GET_SLOT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_SLOT");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:551:18: ( KEYWORD_GET_SLOT LPAR slotName= csName COMMA termName= csName RPAR LBR RBR -> ^( Cst_GetSlot $slotName $termName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:552:3: KEYWORD_GET_SLOT LPAR slotName= csName COMMA termName= csName RPAR LBR RBR
            {
            KEYWORD_GET_SLOT149=(Token)match(input,KEYWORD_GET_SLOT,FOLLOW_KEYWORD_GET_SLOT_in_csKeywordGetSlot3654); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_SLOT.add(KEYWORD_GET_SLOT149);

            LPAR150=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetSlot3656); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR150);

            pushFollow(FOLLOW_csName_in_csKeywordGetSlot3660);
            slotName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(slotName.getTree());
            COMMA151=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordGetSlot3662); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA151);

            pushFollow(FOLLOW_csName_in_csKeywordGetSlot3666);
            termName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termName.getTree());
            RPAR152=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetSlot3668); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR152);

            LBR153=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetSlot3672); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR153);

            RBR154=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetSlot3676); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR154);



            // AST REWRITE
            // elements: termName, slotName
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
            // 555:3: -> ^( Cst_GetSlot $slotName $termName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:555:6: ^( Cst_GetSlot $slotName $termName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetSlot, "Cst_GetSlot"), root_1);

                adaptor.addChild(root_1, stream_slotName.nextTree());
                adaptor.addChild(root_1, stream_termName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR153).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:560:1: csKeywordMake : KEYWORD_MAKE LPAR argList= csNameList RPAR LBR RBR -> ^( Cst_Make $argList) ;
    public final miniTomParser.csKeywordMake_return csKeywordMake() throws RecognitionException {
        miniTomParser.csKeywordMake_return retval = new miniTomParser.csKeywordMake_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE155=null;
        Token LPAR156=null;
        Token RPAR157=null;
        Token LBR158=null;
        Token RBR159=null;
        miniTomParser.csNameList_return argList = null;


        Tree KEYWORD_MAKE155_tree=null;
        Tree LPAR156_tree=null;
        Tree RPAR157_tree=null;
        Tree LBR158_tree=null;
        Tree RBR159_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_MAKE=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csNameList=new RewriteRuleSubtreeStream(adaptor,"rule csNameList");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:560:15: ( KEYWORD_MAKE LPAR argList= csNameList RPAR LBR RBR -> ^( Cst_Make $argList) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:561:3: KEYWORD_MAKE LPAR argList= csNameList RPAR LBR RBR
            {
            KEYWORD_MAKE155=(Token)match(input,KEYWORD_MAKE,FOLLOW_KEYWORD_MAKE_in_csKeywordMake3719); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE.add(KEYWORD_MAKE155);

            LPAR156=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMake3721); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR156);

            pushFollow(FOLLOW_csNameList_in_csKeywordMake3725);
            argList=csNameList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csNameList.add(argList.getTree());
            RPAR157=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMake3727); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR157);

            LBR158=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMake3731); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR158);

            RBR159=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMake3735); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR159);



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
            // 564:3: -> ^( Cst_Make $argList)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:564:6: ^( Cst_Make $argList)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Make, "Cst_Make"), root_1);

                adaptor.addChild(root_1, stream_argList.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR158).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:569:1: csKeywordGetDefault : KEYWORD_GET_DEFAULT LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetDefault $argName) ;
    public final miniTomParser.csKeywordGetDefault_return csKeywordGetDefault() throws RecognitionException {
        miniTomParser.csKeywordGetDefault_return retval = new miniTomParser.csKeywordGetDefault_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_DEFAULT160=null;
        Token LPAR161=null;
        Token RPAR162=null;
        Token LBR163=null;
        Token RBR164=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_GET_DEFAULT160_tree=null;
        Tree LPAR161_tree=null;
        Tree RPAR162_tree=null;
        Tree LBR163_tree=null;
        Tree RBR164_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_GET_DEFAULT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_DEFAULT");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:569:21: ( KEYWORD_GET_DEFAULT LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetDefault $argName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:570:3: KEYWORD_GET_DEFAULT LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_GET_DEFAULT160=(Token)match(input,KEYWORD_GET_DEFAULT,FOLLOW_KEYWORD_GET_DEFAULT_in_csKeywordGetDefault3775); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_DEFAULT.add(KEYWORD_GET_DEFAULT160);

            LPAR161=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetDefault3777); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR161);

            pushFollow(FOLLOW_csName_in_csKeywordGetDefault3781);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR162=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetDefault3783); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR162);

            LBR163=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetDefault3787); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR163);

            RBR164=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetDefault3791); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR164);



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
            // 573:3: -> ^( Cst_GetDefault $argName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:573:6: ^( Cst_GetDefault $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetDefault, "Cst_GetDefault"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR163).getPayload(Tree.class));

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

    public static class csKeywordGetHead_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordGetHead"
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:578:1: csKeywordGetHead : KEYWORD_GET_HEAD LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetHead $argName) ;
    public final miniTomParser.csKeywordGetHead_return csKeywordGetHead() throws RecognitionException {
        miniTomParser.csKeywordGetHead_return retval = new miniTomParser.csKeywordGetHead_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_HEAD165=null;
        Token LPAR166=null;
        Token RPAR167=null;
        Token LBR168=null;
        Token RBR169=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_GET_HEAD165_tree=null;
        Tree LPAR166_tree=null;
        Tree RPAR167_tree=null;
        Tree LBR168_tree=null;
        Tree RBR169_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_KEYWORD_GET_HEAD=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_HEAD");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:578:18: ( KEYWORD_GET_HEAD LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetHead $argName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:579:3: KEYWORD_GET_HEAD LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_GET_HEAD165=(Token)match(input,KEYWORD_GET_HEAD,FOLLOW_KEYWORD_GET_HEAD_in_csKeywordGetHead3831); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_HEAD.add(KEYWORD_GET_HEAD165);

            LPAR166=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetHead3833); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR166);

            pushFollow(FOLLOW_csName_in_csKeywordGetHead3837);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR167=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetHead3839); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR167);

            LBR168=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetHead3844); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR168);

            RBR169=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetHead3848); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR169);



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
            // 582:3: -> ^( Cst_GetHead $argName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:582:6: ^( Cst_GetHead $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetHead, "Cst_GetHead"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR168).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:587:1: csKeywordGetTail : KEYWORD_GET_TAIL LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetTail $argName) ;
    public final miniTomParser.csKeywordGetTail_return csKeywordGetTail() throws RecognitionException {
        miniTomParser.csKeywordGetTail_return retval = new miniTomParser.csKeywordGetTail_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_TAIL170=null;
        Token LPAR171=null;
        Token RPAR172=null;
        Token LBR173=null;
        Token RBR174=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_GET_TAIL170_tree=null;
        Tree LPAR171_tree=null;
        Tree RPAR172_tree=null;
        Tree LBR173_tree=null;
        Tree RBR174_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_GET_TAIL=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_TAIL");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:587:18: ( KEYWORD_GET_TAIL LPAR argName= csName RPAR LBR RBR -> ^( Cst_GetTail $argName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:588:3: KEYWORD_GET_TAIL LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_GET_TAIL170=(Token)match(input,KEYWORD_GET_TAIL,FOLLOW_KEYWORD_GET_TAIL_in_csKeywordGetTail3888); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_TAIL.add(KEYWORD_GET_TAIL170);

            LPAR171=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetTail3890); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR171);

            pushFollow(FOLLOW_csName_in_csKeywordGetTail3894);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR172=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetTail3896); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR172);

            LBR173=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetTail3900); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR173);

            RBR174=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetTail3904); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR174);



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
            // 591:3: -> ^( Cst_GetTail $argName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:591:6: ^( Cst_GetTail $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetTail, "Cst_GetTail"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR173).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:596:1: csKeywordIsEmpty : KEYWORD_IS_EMPTY LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsEmpty $argName) ;
    public final miniTomParser.csKeywordIsEmpty_return csKeywordIsEmpty() throws RecognitionException {
        miniTomParser.csKeywordIsEmpty_return retval = new miniTomParser.csKeywordIsEmpty_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_IS_EMPTY175=null;
        Token LPAR176=null;
        Token RPAR177=null;
        Token LBR178=null;
        Token RBR179=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_IS_EMPTY175_tree=null;
        Tree LPAR176_tree=null;
        Tree RPAR177_tree=null;
        Tree LBR178_tree=null;
        Tree RBR179_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_IS_EMPTY=new RewriteRuleTokenStream(adaptor,"token KEYWORD_IS_EMPTY");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:596:18: ( KEYWORD_IS_EMPTY LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsEmpty $argName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:597:3: KEYWORD_IS_EMPTY LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_IS_EMPTY175=(Token)match(input,KEYWORD_IS_EMPTY,FOLLOW_KEYWORD_IS_EMPTY_in_csKeywordIsEmpty3944); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_IS_EMPTY.add(KEYWORD_IS_EMPTY175);

            LPAR176=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordIsEmpty3946); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR176);

            pushFollow(FOLLOW_csName_in_csKeywordIsEmpty3950);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR177=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordIsEmpty3952); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR177);

            LBR178=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordIsEmpty3956); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR178);

            RBR179=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordIsEmpty3960); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR179);



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
            // 600:3: -> ^( Cst_IsEmpty $argName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:600:6: ^( Cst_IsEmpty $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_IsEmpty, "Cst_IsEmpty"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR178).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:605:1: csKeywordMakeEmpty_List : KEYWORD_MAKE_EMPTY LPAR RPAR LBR RBR -> ^( Cst_MakeEmptyList ) ;
    public final miniTomParser.csKeywordMakeEmpty_List_return csKeywordMakeEmpty_List() throws RecognitionException {
        miniTomParser.csKeywordMakeEmpty_List_return retval = new miniTomParser.csKeywordMakeEmpty_List_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE_EMPTY180=null;
        Token LPAR181=null;
        Token RPAR182=null;
        Token LBR183=null;
        Token RBR184=null;

        Tree KEYWORD_MAKE_EMPTY180_tree=null;
        Tree LPAR181_tree=null;
        Tree RPAR182_tree=null;
        Tree LBR183_tree=null;
        Tree RBR184_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_MAKE_EMPTY=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE_EMPTY");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");

        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:605:25: ( KEYWORD_MAKE_EMPTY LPAR RPAR LBR RBR -> ^( Cst_MakeEmptyList ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:606:3: KEYWORD_MAKE_EMPTY LPAR RPAR LBR RBR
            {
            KEYWORD_MAKE_EMPTY180=(Token)match(input,KEYWORD_MAKE_EMPTY,FOLLOW_KEYWORD_MAKE_EMPTY_in_csKeywordMakeEmpty_List4000); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE_EMPTY.add(KEYWORD_MAKE_EMPTY180);

            LPAR181=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMakeEmpty_List4002); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR181);

            RPAR182=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMakeEmpty_List4004); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR182);

            LBR183=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMakeEmpty_List4008); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR183);

            RBR184=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMakeEmpty_List4012); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR184);



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
            // 609:3: -> ^( Cst_MakeEmptyList )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:609:6: ^( Cst_MakeEmptyList )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MakeEmptyList, "Cst_MakeEmptyList"), root_1);

                adaptor.addChild(root_1, ((CustomToken)LBR183).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:614:1: csKeywordMakeEmpty_Array : KEYWORD_MAKE_EMPTY LPAR argName= csName RPAR LBR RBR -> ^( Cst_MakeEmptyArray $argName) ;
    public final miniTomParser.csKeywordMakeEmpty_Array_return csKeywordMakeEmpty_Array() throws RecognitionException {
        miniTomParser.csKeywordMakeEmpty_Array_return retval = new miniTomParser.csKeywordMakeEmpty_Array_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE_EMPTY185=null;
        Token LPAR186=null;
        Token RPAR187=null;
        Token LBR188=null;
        Token RBR189=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_MAKE_EMPTY185_tree=null;
        Tree LPAR186_tree=null;
        Tree RPAR187_tree=null;
        Tree LBR188_tree=null;
        Tree RBR189_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_MAKE_EMPTY=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE_EMPTY");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:614:26: ( KEYWORD_MAKE_EMPTY LPAR argName= csName RPAR LBR RBR -> ^( Cst_MakeEmptyArray $argName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:615:3: KEYWORD_MAKE_EMPTY LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_MAKE_EMPTY185=(Token)match(input,KEYWORD_MAKE_EMPTY,FOLLOW_KEYWORD_MAKE_EMPTY_in_csKeywordMakeEmpty_Array4049); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE_EMPTY.add(KEYWORD_MAKE_EMPTY185);

            LPAR186=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMakeEmpty_Array4051); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR186);

            pushFollow(FOLLOW_csName_in_csKeywordMakeEmpty_Array4055);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR187=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMakeEmpty_Array4057); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR187);

            LBR188=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMakeEmpty_Array4061); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR188);

            RBR189=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMakeEmpty_Array4065); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR189);



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
            // 618:3: -> ^( Cst_MakeEmptyArray $argName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:618:6: ^( Cst_MakeEmptyArray $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MakeEmptyArray, "Cst_MakeEmptyArray"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR188).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:623:1: csKeywordMakeInsert : KEYWORD_MAKE_INSERT LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR -> ^( Cst_MakeInsert $elementArgName $termArgName) ;
    public final miniTomParser.csKeywordMakeInsert_return csKeywordMakeInsert() throws RecognitionException {
        miniTomParser.csKeywordMakeInsert_return retval = new miniTomParser.csKeywordMakeInsert_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE_INSERT190=null;
        Token LPAR191=null;
        Token COMMA192=null;
        Token RPAR193=null;
        Token LBR194=null;
        Token RBR195=null;
        miniTomParser.csName_return elementArgName = null;

        miniTomParser.csName_return termArgName = null;


        Tree KEYWORD_MAKE_INSERT190_tree=null;
        Tree LPAR191_tree=null;
        Tree COMMA192_tree=null;
        Tree RPAR193_tree=null;
        Tree LBR194_tree=null;
        Tree RBR195_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_KEYWORD_MAKE_INSERT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE_INSERT");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:623:21: ( KEYWORD_MAKE_INSERT LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR -> ^( Cst_MakeInsert $elementArgName $termArgName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:624:3: KEYWORD_MAKE_INSERT LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR
            {
            KEYWORD_MAKE_INSERT190=(Token)match(input,KEYWORD_MAKE_INSERT,FOLLOW_KEYWORD_MAKE_INSERT_in_csKeywordMakeInsert4105); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE_INSERT.add(KEYWORD_MAKE_INSERT190);

            LPAR191=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMakeInsert4111); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR191);

            pushFollow(FOLLOW_csName_in_csKeywordMakeInsert4115);
            elementArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(elementArgName.getTree());
            COMMA192=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordMakeInsert4117); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA192);

            pushFollow(FOLLOW_csName_in_csKeywordMakeInsert4121);
            termArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termArgName.getTree());
            RPAR193=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMakeInsert4123); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR193);

            LBR194=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMakeInsert4127); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR194);

            RBR195=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMakeInsert4131); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR195);



            // AST REWRITE
            // elements: termArgName, elementArgName
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
            // 628:3: -> ^( Cst_MakeInsert $elementArgName $termArgName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:628:6: ^( Cst_MakeInsert $elementArgName $termArgName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MakeInsert, "Cst_MakeInsert"), root_1);

                adaptor.addChild(root_1, stream_elementArgName.nextTree());
                adaptor.addChild(root_1, stream_termArgName.nextTree());
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
    // $ANTLR end "csKeywordMakeInsert"

    public static class csKeywordGetElement_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordGetElement"
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:633:1: csKeywordGetElement : KEYWORD_GET_ELEMENT LPAR termArgName= csName COMMA elementIndexArgName= csName RPAR LBR RBR -> ^( Cst_GetElement $termArgName $elementIndexArgName) ;
    public final miniTomParser.csKeywordGetElement_return csKeywordGetElement() throws RecognitionException {
        miniTomParser.csKeywordGetElement_return retval = new miniTomParser.csKeywordGetElement_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_ELEMENT196=null;
        Token LPAR197=null;
        Token COMMA198=null;
        Token RPAR199=null;
        Token LBR200=null;
        Token RBR201=null;
        miniTomParser.csName_return termArgName = null;

        miniTomParser.csName_return elementIndexArgName = null;


        Tree KEYWORD_GET_ELEMENT196_tree=null;
        Tree LPAR197_tree=null;
        Tree COMMA198_tree=null;
        Tree RPAR199_tree=null;
        Tree LBR200_tree=null;
        Tree RBR201_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleTokenStream stream_KEYWORD_GET_ELEMENT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_ELEMENT");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:633:21: ( KEYWORD_GET_ELEMENT LPAR termArgName= csName COMMA elementIndexArgName= csName RPAR LBR RBR -> ^( Cst_GetElement $termArgName $elementIndexArgName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:634:3: KEYWORD_GET_ELEMENT LPAR termArgName= csName COMMA elementIndexArgName= csName RPAR LBR RBR
            {
            KEYWORD_GET_ELEMENT196=(Token)match(input,KEYWORD_GET_ELEMENT,FOLLOW_KEYWORD_GET_ELEMENT_in_csKeywordGetElement4174); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_ELEMENT.add(KEYWORD_GET_ELEMENT196);

            LPAR197=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetElement4180); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR197);

            pushFollow(FOLLOW_csName_in_csKeywordGetElement4184);
            termArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termArgName.getTree());
            COMMA198=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordGetElement4186); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA198);

            pushFollow(FOLLOW_csName_in_csKeywordGetElement4190);
            elementIndexArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(elementIndexArgName.getTree());
            RPAR199=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetElement4192); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR199);

            LBR200=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetElement4196); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR200);

            RBR201=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetElement4200); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR201);



            // AST REWRITE
            // elements: termArgName, elementIndexArgName
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
            // 638:3: -> ^( Cst_GetElement $termArgName $elementIndexArgName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:638:6: ^( Cst_GetElement $termArgName $elementIndexArgName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetElement, "Cst_GetElement"), root_1);

                adaptor.addChild(root_1, stream_termArgName.nextTree());
                adaptor.addChild(root_1, stream_elementIndexArgName.nextTree());
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
    // $ANTLR end "csKeywordGetElement"

    public static class csKeywordGetSize_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordGetSize"
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:643:1: csKeywordGetSize : KEYWORD_GET_SIZE LPAR termArgName= csName RPAR LBR RBR -> ^( Cst_GetSize $termArgName) ;
    public final miniTomParser.csKeywordGetSize_return csKeywordGetSize() throws RecognitionException {
        miniTomParser.csKeywordGetSize_return retval = new miniTomParser.csKeywordGetSize_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_GET_SIZE202=null;
        Token LPAR203=null;
        Token RPAR204=null;
        Token LBR205=null;
        Token RBR206=null;
        miniTomParser.csName_return termArgName = null;


        Tree KEYWORD_GET_SIZE202_tree=null;
        Tree LPAR203_tree=null;
        Tree RPAR204_tree=null;
        Tree LBR205_tree=null;
        Tree RBR206_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_GET_SIZE=new RewriteRuleTokenStream(adaptor,"token KEYWORD_GET_SIZE");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:643:18: ( KEYWORD_GET_SIZE LPAR termArgName= csName RPAR LBR RBR -> ^( Cst_GetSize $termArgName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:644:3: KEYWORD_GET_SIZE LPAR termArgName= csName RPAR LBR RBR
            {
            KEYWORD_GET_SIZE202=(Token)match(input,KEYWORD_GET_SIZE,FOLLOW_KEYWORD_GET_SIZE_in_csKeywordGetSize4243); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_GET_SIZE.add(KEYWORD_GET_SIZE202);

            LPAR203=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordGetSize4245); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR203);

            pushFollow(FOLLOW_csName_in_csKeywordGetSize4249);
            termArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termArgName.getTree());
            RPAR204=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordGetSize4251); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR204);

            LBR205=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordGetSize4255); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR205);

            RBR206=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordGetSize4259); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR206);



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
            // 647:3: -> ^( Cst_GetSize $termArgName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:647:6: ^( Cst_GetSize $termArgName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_GetSize, "Cst_GetSize"), root_1);

                adaptor.addChild(root_1, stream_termArgName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR205).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:652:1: csKeywordMakeAppend : KEYWORD_MAKE_APPEND LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR -> ^( Cst_MakeAppend $elementArgName $termArgName) ;
    public final miniTomParser.csKeywordMakeAppend_return csKeywordMakeAppend() throws RecognitionException {
        miniTomParser.csKeywordMakeAppend_return retval = new miniTomParser.csKeywordMakeAppend_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_MAKE_APPEND207=null;
        Token LPAR208=null;
        Token COMMA209=null;
        Token RPAR210=null;
        Token LBR211=null;
        Token RBR212=null;
        miniTomParser.csName_return elementArgName = null;

        miniTomParser.csName_return termArgName = null;


        Tree KEYWORD_MAKE_APPEND207_tree=null;
        Tree LPAR208_tree=null;
        Tree COMMA209_tree=null;
        Tree RPAR210_tree=null;
        Tree LBR211_tree=null;
        Tree RBR212_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_MAKE_APPEND=new RewriteRuleTokenStream(adaptor,"token KEYWORD_MAKE_APPEND");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:652:21: ( KEYWORD_MAKE_APPEND LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR -> ^( Cst_MakeAppend $elementArgName $termArgName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:653:3: KEYWORD_MAKE_APPEND LPAR elementArgName= csName COMMA termArgName= csName RPAR LBR RBR
            {
            KEYWORD_MAKE_APPEND207=(Token)match(input,KEYWORD_MAKE_APPEND,FOLLOW_KEYWORD_MAKE_APPEND_in_csKeywordMakeAppend4299); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_MAKE_APPEND.add(KEYWORD_MAKE_APPEND207);

            LPAR208=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordMakeAppend4306); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR208);

            pushFollow(FOLLOW_csName_in_csKeywordMakeAppend4310);
            elementArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(elementArgName.getTree());
            COMMA209=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordMakeAppend4312); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA209);

            pushFollow(FOLLOW_csName_in_csKeywordMakeAppend4316);
            termArgName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(termArgName.getTree());
            RPAR210=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordMakeAppend4318); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR210);

            LBR211=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordMakeAppend4322); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR211);

            RBR212=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordMakeAppend4326); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR212);



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
            // 657:3: -> ^( Cst_MakeAppend $elementArgName $termArgName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:657:6: ^( Cst_MakeAppend $elementArgName $termArgName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_MakeAppend, "Cst_MakeAppend"), root_1);

                adaptor.addChild(root_1, stream_elementArgName.nextTree());
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
    // $ANTLR end "csKeywordMakeAppend"

    public static class csKeywordImplement_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csKeywordImplement"
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:662:1: csKeywordImplement : KEYWORD_IMPLEMENT LBR RBR -> ^( Cst_Implement ) ;
    public final miniTomParser.csKeywordImplement_return csKeywordImplement() throws RecognitionException {
        miniTomParser.csKeywordImplement_return retval = new miniTomParser.csKeywordImplement_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_IMPLEMENT213=null;
        Token LBR214=null;
        Token RBR215=null;

        Tree KEYWORD_IMPLEMENT213_tree=null;
        Tree LBR214_tree=null;
        Tree RBR215_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_IMPLEMENT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_IMPLEMENT");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");

        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:662:20: ( KEYWORD_IMPLEMENT LBR RBR -> ^( Cst_Implement ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:663:3: KEYWORD_IMPLEMENT LBR RBR
            {
            KEYWORD_IMPLEMENT213=(Token)match(input,KEYWORD_IMPLEMENT,FOLLOW_KEYWORD_IMPLEMENT_in_csKeywordImplement4369); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_IMPLEMENT.add(KEYWORD_IMPLEMENT213);

            LBR214=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordImplement4371); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR214);

            RBR215=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordImplement4375); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR215);



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
            // 665:3: -> ^( Cst_Implement )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:665:6: ^( Cst_Implement )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Implement, "Cst_Implement"), root_1);

                adaptor.addChild(root_1, ((CustomToken)LBR214).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:668:1: csKeywordIsSort : KEYWORD_IS_SORT LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsSort $argName) ;
    public final miniTomParser.csKeywordIsSort_return csKeywordIsSort() throws RecognitionException {
        miniTomParser.csKeywordIsSort_return retval = new miniTomParser.csKeywordIsSort_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_IS_SORT216=null;
        Token LPAR217=null;
        Token RPAR218=null;
        Token LBR219=null;
        Token RBR220=null;
        miniTomParser.csName_return argName = null;


        Tree KEYWORD_IS_SORT216_tree=null;
        Tree LPAR217_tree=null;
        Tree RPAR218_tree=null;
        Tree LBR219_tree=null;
        Tree RBR220_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_IS_SORT=new RewriteRuleTokenStream(adaptor,"token KEYWORD_IS_SORT");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:668:17: ( KEYWORD_IS_SORT LPAR argName= csName RPAR LBR RBR -> ^( Cst_IsSort $argName) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:669:3: KEYWORD_IS_SORT LPAR argName= csName RPAR LBR RBR
            {
            KEYWORD_IS_SORT216=(Token)match(input,KEYWORD_IS_SORT,FOLLOW_KEYWORD_IS_SORT_in_csKeywordIsSort4397); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_IS_SORT.add(KEYWORD_IS_SORT216);

            LPAR217=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordIsSort4399); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR217);

            pushFollow(FOLLOW_csName_in_csKeywordIsSort4403);
            argName=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(argName.getTree());
            RPAR218=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordIsSort4405); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR218);

            LBR219=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordIsSort4411); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR219);

            RBR220=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordIsSort4415); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR220);



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
            // 672:3: -> ^( Cst_IsSort $argName)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:672:6: ^( Cst_IsSort $argName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_IsSort, "Cst_IsSort"), root_1);

                adaptor.addChild(root_1, stream_argName.nextTree());
                adaptor.addChild(root_1, ((CustomToken)LBR219).getPayload(Tree.class));

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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:675:1: csKeywordEquals : KEYWORD_EQUALS LPAR arg1= csName COMMA arg2= csName RPAR LBR RBR -> ^( Cst_Equals $arg1 $arg2) ;
    public final miniTomParser.csKeywordEquals_return csKeywordEquals() throws RecognitionException {
        miniTomParser.csKeywordEquals_return retval = new miniTomParser.csKeywordEquals_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token KEYWORD_EQUALS221=null;
        Token LPAR222=null;
        Token COMMA223=null;
        Token RPAR224=null;
        Token LBR225=null;
        Token RBR226=null;
        miniTomParser.csName_return arg1 = null;

        miniTomParser.csName_return arg2 = null;


        Tree KEYWORD_EQUALS221_tree=null;
        Tree LPAR222_tree=null;
        Tree COMMA223_tree=null;
        Tree RPAR224_tree=null;
        Tree LBR225_tree=null;
        Tree RBR226_tree=null;
        RewriteRuleTokenStream stream_KEYWORD_EQUALS=new RewriteRuleTokenStream(adaptor,"token KEYWORD_EQUALS");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_LBR=new RewriteRuleTokenStream(adaptor,"token LBR");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:675:17: ( KEYWORD_EQUALS LPAR arg1= csName COMMA arg2= csName RPAR LBR RBR -> ^( Cst_Equals $arg1 $arg2) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:676:3: KEYWORD_EQUALS LPAR arg1= csName COMMA arg2= csName RPAR LBR RBR
            {
            KEYWORD_EQUALS221=(Token)match(input,KEYWORD_EQUALS,FOLLOW_KEYWORD_EQUALS_in_csKeywordEquals4440); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KEYWORD_EQUALS.add(KEYWORD_EQUALS221);

            LPAR222=(Token)match(input,LPAR,FOLLOW_LPAR_in_csKeywordEquals4442); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR222);

            pushFollow(FOLLOW_csName_in_csKeywordEquals4446);
            arg1=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(arg1.getTree());
            COMMA223=(Token)match(input,COMMA,FOLLOW_COMMA_in_csKeywordEquals4448); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COMMA.add(COMMA223);

            pushFollow(FOLLOW_csName_in_csKeywordEquals4452);
            arg2=csName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csName.add(arg2.getTree());
            RPAR224=(Token)match(input,RPAR,FOLLOW_RPAR_in_csKeywordEquals4454); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR224);

            LBR225=(Token)match(input,LBR,FOLLOW_LBR_in_csKeywordEquals4460); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBR.add(LBR225);

            RBR226=(Token)match(input,RBR,FOLLOW_RBR_in_csKeywordEquals4464); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBR.add(RBR226);



            // AST REWRITE
            // elements: arg1, arg2
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
            // 679:3: -> ^( Cst_Equals $arg1 $arg2)
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:679:6: ^( Cst_Equals $arg1 $arg2)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Equals, "Cst_Equals"), root_1);

                adaptor.addChild(root_1, stream_arg1.nextTree());
                adaptor.addChild(root_1, stream_arg2.nextTree());
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
    // $ANTLR end "csKeywordEquals"

    public static class csNameList_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csNameList"
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:684:1: csNameList : ( csName ( COMMA csName )* )? -> ^( ConcCstName ( csName )* ) ;
    public final miniTomParser.csNameList_return csNameList() throws RecognitionException {
        miniTomParser.csNameList_return retval = new miniTomParser.csNameList_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token COMMA228=null;
        miniTomParser.csName_return csName227 = null;

        miniTomParser.csName_return csName229 = null;


        Tree COMMA228_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_csName=new RewriteRuleSubtreeStream(adaptor,"rule csName");
        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:684:12: ( ( csName ( COMMA csName )* )? -> ^( ConcCstName ( csName )* ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:685:3: ( csName ( COMMA csName )* )?
            {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:685:3: ( csName ( COMMA csName )* )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==IDENTIFIER) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:685:4: csName ( COMMA csName )*
                    {
                    pushFollow(FOLLOW_csName_in_csNameList4509);
                    csName227=csName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csName.add(csName227.getTree());
                    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:685:11: ( COMMA csName )*
                    loop52:
                    do {
                        int alt52=2;
                        int LA52_0 = input.LA(1);

                        if ( (LA52_0==COMMA) ) {
                            alt52=1;
                        }


                        switch (alt52) {
                    	case 1 :
                    	    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:685:12: COMMA csName
                    	    {
                    	    COMMA228=(Token)match(input,COMMA,FOLLOW_COMMA_in_csNameList4512); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA228);

                    	    pushFollow(FOLLOW_csName_in_csNameList4514);
                    	    csName229=csName();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csName.add(csName229.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop52;
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
            // 686:3: -> ^( ConcCstName ( csName )* )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:686:6: ^( ConcCstName ( csName )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstName, "ConcCstName"), root_1);

                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:686:20: ( csName )*
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
    // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:689:1: csName : IDENTIFIER -> ^( Cst_Name IDENTIFIER ) ;
    public final miniTomParser.csName_return csName() throws RecognitionException {
        miniTomParser.csName_return retval = new miniTomParser.csName_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token IDENTIFIER230=null;

        Tree IDENTIFIER230_tree=null;
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");

        try {
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:689:8: ( IDENTIFIER -> ^( Cst_Name IDENTIFIER ) )
            // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:690:3: IDENTIFIER
            {
            IDENTIFIER230=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_csName4540); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER230);



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
            // 691:3: -> ^( Cst_Name IDENTIFIER )
            {
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:691:6: ^( Cst_Name IDENTIFIER )
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

    // $ANTLR start synpred5_miniTom
    public final void synpred5_miniTom_fragment() throws RecognitionException {   
        Token l=null;

        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR )
        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR
        {
        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:129:3: ( IDENTIFIER l= COLON )?
        int alt54=2;
        int LA54_0 = input.LA(1);

        if ( (LA54_0==IDENTIFIER) ) {
            int LA54_1 = input.LA(2);

            if ( (LA54_1==COLON) ) {
                alt54=1;
            }
        }
        switch (alt54) {
            case 1 :
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:129:4: IDENTIFIER l= COLON
                {
                match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred5_miniTom370); if (state.failed) return ;
                l=(Token)match(input,COLON,FOLLOW_COLON_in_synpred5_miniTom374); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_csExtendedConstraint_in_synpred5_miniTom378);
        csExtendedConstraint();

        state._fsp--;
        if (state.failed) return ;
        match(input,ARROW,FOLLOW_ARROW_in_synpred5_miniTom380); if (state.failed) return ;
        match(input,LBR,FOLLOW_LBR_in_synpred5_miniTom382); if (state.failed) return ;
        match(input,RBR,FOLLOW_RBR_in_synpred5_miniTom384); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred5_miniTom

    // $ANTLR start synpred14_miniTom
    public final void synpred14_miniTom_fragment() throws RecognitionException {   
        Token type=null;

        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:247:4: (type= IDENTIFIER )
        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:247:4: type= IDENTIFIER
        {
        type=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred14_miniTom1521); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred14_miniTom

    // $ANTLR start synpred31_miniTom
    public final void synpred31_miniTom_fragment() throws RecognitionException {   
        Token gt=null;
        Token ge=null;
        Token lt=null;
        Token le=null;
        Token eq=null;
        Token ne=null;
        miniTomParser.csTerm_return l = null;

        miniTomParser.csTerm_return r = null;


        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:317:3: (l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm )
        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:317:3: l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm
        {
        pushFollow(FOLLOW_csTerm_in_synpred31_miniTom1972);
        l=csTerm();

        state._fsp--;
        if (state.failed) return ;
        // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:318:3: (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT )
        int alt63=6;
        switch ( input.LA(1) ) {
        case GREATERTHAN:
            {
            alt63=1;
            }
            break;
        case GREATEROREQU:
            {
            alt63=2;
            }
            break;
        case LOWERTHAN:
            {
            alt63=3;
            }
            break;
        case LOWEROREQU:
            {
            alt63=4;
            }
            break;
        case DOUBLEEQUAL:
            {
            alt63=5;
            }
            break;
        case DIFFERENT:
            {
            alt63=6;
            }
            break;
        default:
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 63, 0, input);

            throw nvae;
        }

        switch (alt63) {
            case 1 :
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:318:4: gt= GREATERTHAN
                {
                gt=(Token)match(input,GREATERTHAN,FOLLOW_GREATERTHAN_in_synpred31_miniTom1979); if (state.failed) return ;

                }
                break;
            case 2 :
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:318:19: ge= GREATEROREQU
                {
                ge=(Token)match(input,GREATEROREQU,FOLLOW_GREATEROREQU_in_synpred31_miniTom1983); if (state.failed) return ;

                }
                break;
            case 3 :
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:318:35: lt= LOWERTHAN
                {
                lt=(Token)match(input,LOWERTHAN,FOLLOW_LOWERTHAN_in_synpred31_miniTom1987); if (state.failed) return ;

                }
                break;
            case 4 :
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:319:4: le= LOWEROREQU
                {
                le=(Token)match(input,LOWEROREQU,FOLLOW_LOWEROREQU_in_synpred31_miniTom1994); if (state.failed) return ;

                }
                break;
            case 5 :
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:319:19: eq= DOUBLEEQUAL
                {
                eq=(Token)match(input,DOUBLEEQUAL,FOLLOW_DOUBLEEQUAL_in_synpred31_miniTom1999); if (state.failed) return ;

                }
                break;
            case 6 :
                // /Users/jcb/workspace/jtom2/src/tom/engine/parser/antlr3/miniTom.g:319:35: ne= DIFFERENT
                {
                ne=(Token)match(input,DIFFERENT,FOLLOW_DIFFERENT_in_synpred31_miniTom2004); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_csTerm_in_synpred31_miniTom2011);
        r=csTerm();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred31_miniTom

    // Delegated rules

    public final boolean synpred31_miniTom() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred31_miniTom_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred14_miniTom() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred14_miniTom_fragment(); // can never throw exception
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
    protected DFA18 dfa18 = new DFA18(this);
    protected DFA26 dfa26 = new DFA26(this);
    protected DFA27 dfa27 = new DFA27(this);
    protected DFA35 dfa35 = new DFA35(this);
    protected DFA38 dfa38 = new DFA38(this);
    static final String DFA6_eotS =
        "\14\uffff";
    static final String DFA6_eofS =
        "\14\uffff";
    static final String DFA6_minS =
        "\1\u0153\11\0\2\uffff";
    static final String DFA6_maxS =
        "\1\u0171\11\0\2\uffff";
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
            return "128:1: csVisitAction : ( ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW LBR RBR -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstOption ) ) | ( IDENTIFIER l= COLON )? csExtendedConstraint ARROW csBQTerm -> {$l!=null}? ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ^( Cst_Label IDENTIFIER ) ) ) -> ^( Cst_ConstraintAction csExtendedConstraint ^( ConcCstBlock ^( Cst_BQTermToBlock csBQTerm ) ) ^( ConcCstOption ) ) );";
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
    static final String DFA18_eotS =
        "\27\uffff";
    static final String DFA18_eofS =
        "\1\uffff\1\4\12\uffff\1\13\12\uffff";
    static final String DFA18_minS =
        "\1\u0153\1\u0152\1\uffff\1\u0153\1\uffff\1\u0153\5\u0156\1\uffff"+
        "\1\u0152\1\u0153\11\u0155";
    static final String DFA18_maxS =
        "\2\u0171\1\uffff\1\u0171\1\uffff\1\u016c\5\u016a\1\uffff\1\u0172"+
        "\1\u0171\11\u0172";
    static final String DFA18_acceptS =
        "\2\uffff\1\3\1\uffff\1\1\6\uffff\1\2\13\uffff";
    static final String DFA18_specialS =
        "\27\uffff}>";
    static final String[] DFA18_transitionS = {
            "\1\1\31\uffff\5\2",
            "\3\4\1\3\1\4\4\uffff\5\4\10\uffff\2\4\3\uffff\5\4",
            "",
            "\1\5\2\uffff\1\13\26\uffff\1\6\1\7\1\10\1\11\1\12",
            "",
            "\1\13\1\uffff\1\13\1\14\5\uffff\2\13\14\uffff\3\4",
            "\1\14\5\uffff\1\13\15\uffff\1\4",
            "\1\14\5\uffff\1\13\15\uffff\1\4",
            "\1\14\5\uffff\1\13\15\uffff\1\4",
            "\1\14\5\uffff\1\13\15\uffff\1\4",
            "\1\14\5\uffff\1\13\15\uffff\1\4",
            "",
            "\3\13\1\15\1\13\4\uffff\2\13\1\uffff\2\13\10\uffff\2\13\3\uffff"+
            "\5\13\1\4",
            "\1\16\1\uffff\2\4\21\uffff\2\4\3\uffff\1\17\1\20\1\21\1\22"+
            "\1\23",
            "\1\4\1\26\5\uffff\2\4\11\uffff\1\4\2\uffff\1\13\1\24\1\25\5"+
            "\uffff\1\4",
            "\1\4\1\26\5\uffff\2\4\14\uffff\1\13\7\uffff\1\4",
            "\1\4\1\26\5\uffff\2\4\14\uffff\1\13\7\uffff\1\4",
            "\1\4\1\26\5\uffff\2\4\14\uffff\1\13\7\uffff\1\4",
            "\1\4\1\26\5\uffff\2\4\14\uffff\1\13\7\uffff\1\4",
            "\1\4\1\26\5\uffff\2\4\14\uffff\1\13\7\uffff\1\4",
            "\1\4\1\13\23\uffff\1\13\7\uffff\1\4",
            "\1\4\1\13\23\uffff\1\13\7\uffff\1\4",
            "\1\13\5\uffff\2\4\1\uffff\2\4\22\uffff\1\13"
    };

    static final short[] DFA18_eot = DFA.unpackEncodedString(DFA18_eotS);
    static final short[] DFA18_eof = DFA.unpackEncodedString(DFA18_eofS);
    static final char[] DFA18_min = DFA.unpackEncodedStringToUnsignedChars(DFA18_minS);
    static final char[] DFA18_max = DFA.unpackEncodedStringToUnsignedChars(DFA18_maxS);
    static final short[] DFA18_accept = DFA.unpackEncodedString(DFA18_acceptS);
    static final short[] DFA18_special = DFA.unpackEncodedString(DFA18_specialS);
    static final short[][] DFA18_transition;

    static {
        int numStates = DFA18_transitionS.length;
        DFA18_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA18_transition[i] = DFA.unpackEncodedString(DFA18_transitionS[i]);
        }
    }

    class DFA18 extends DFA {

        public DFA18(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 18;
            this.eot = DFA18_eot;
            this.eof = DFA18_eof;
            this.min = DFA18_min;
            this.max = DFA18_max;
            this.accept = DFA18_accept;
            this.special = DFA18_special;
            this.transition = DFA18_transition;
        }
        public String getDescription() {
            return "246:1: csBQTerm : ( (type= IDENTIFIER )? name= IDENTIFIER (s= STAR )? -> {s!=null && type!=null}? ^( Cst_BQVarStar $name ^( Cst_Type $type) ) -> {s!=null && type==null}? ^( Cst_BQVarStar $name ^( Cst_TypeUnknown ) ) -> {s==null && type!=null}? ^( Cst_BQVar $name ^( Cst_Type $type) ) -> ^( Cst_BQVar $name ^( Cst_TypeUnknown ) ) | bqname= IDENTIFIER LPAR (a+= csBQTerm ( COMMA a+= csBQTerm )* )? RPAR -> ^( Cst_BQAppl $bqname ^( ConcCstBQTerm ( $a)* ) ) | csConstantValue -> ^( Cst_BQConstant csConstantValue ) );";
        }
    }
    static final String DFA26_eotS =
        "\13\uffff";
    static final String DFA26_eofS =
        "\13\uffff";
    static final String DFA26_minS =
        "\1\u0153\1\0\11\uffff";
    static final String DFA26_maxS =
        "\1\u0171\1\0\11\uffff";
    static final String DFA26_acceptS =
        "\2\uffff\1\2\7\uffff\1\1";
    static final String DFA26_specialS =
        "\1\uffff\1\0\11\uffff}>";
    static final String[] DFA26_transitionS = {
            "\1\1\1\uffff\1\2\22\uffff\2\2\3\uffff\5\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA26_eot = DFA.unpackEncodedString(DFA26_eotS);
    static final short[] DFA26_eof = DFA.unpackEncodedString(DFA26_eofS);
    static final char[] DFA26_min = DFA.unpackEncodedStringToUnsignedChars(DFA26_minS);
    static final char[] DFA26_max = DFA.unpackEncodedStringToUnsignedChars(DFA26_maxS);
    static final short[] DFA26_accept = DFA.unpackEncodedString(DFA26_acceptS);
    static final short[] DFA26_special = DFA.unpackEncodedString(DFA26_specialS);
    static final short[][] DFA26_transition;

    static {
        int numStates = DFA26_transitionS.length;
        DFA26_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA26_transition[i] = DFA.unpackEncodedString(DFA26_transitionS[i]);
        }
    }

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = DFA26_eot;
            this.eof = DFA26_eof;
            this.min = DFA26_min;
            this.max = DFA26_max;
            this.accept = DFA26_accept;
            this.special = DFA26_special;
            this.transition = DFA26_transition;
        }
        public String getDescription() {
            return "315:1: csConstraint_priority3 : (l= csTerm (gt= GREATERTHAN | ge= GREATEROREQU | lt= LOWERTHAN | le= LOWEROREQU | eq= DOUBLEEQUAL | ne= DIFFERENT ) r= csTerm -> {gt!=null}? ^( Cst_NumGreaterThan $l $r) -> {ge!=null}? ^( Cst_NumGreaterOrEqualTo $l $r) -> {lt!=null}? ^( Cst_NumLessThan $l $r) -> {le!=null}? ^( Cst_NumLessOrEqualTo $l $r) -> {eq!=null}? ^( Cst_NumEqualTo $l $r) -> ^( Cst_NumDifferent $l $r) | csConstraint_priority4 -> csConstraint_priority4 );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA26_1 = input.LA(1);

                         
                        int index26_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred31_miniTom()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index26_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 26, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA27_eotS =
        "\14\uffff";
    static final String DFA27_eofS =
        "\14\uffff";
    static final String DFA27_minS =
        "\1\u0153\1\uffff\1\u0153\6\u0155\1\uffff\2\u0155";
    static final String DFA27_maxS =
        "\1\u0171\1\uffff\1\u0171\6\u0172\1\uffff\2\u0172";
    static final String DFA27_acceptS =
        "\1\uffff\1\1\7\uffff\1\2\2\uffff";
    static final String DFA27_specialS =
        "\14\uffff}>";
    static final String[] DFA27_transitionS = {
            "\1\1\1\uffff\1\2\22\uffff\2\1\3\uffff\5\1",
            "",
            "\1\3\1\uffff\1\11\22\uffff\2\11\3\uffff\1\4\1\5\1\6\1\7\1\10",
            "\1\11\1\1\6\uffff\1\11\2\uffff\10\11\2\uffff\1\1\1\12\1\13"+
            "\5\uffff\1\11",
            "\1\11\1\1\6\uffff\1\11\10\uffff\1\11\3\uffff\1\1\7\uffff\1"+
            "\11",
            "\1\11\1\1\6\uffff\1\11\10\uffff\1\11\3\uffff\1\1\7\uffff\1"+
            "\11",
            "\1\11\1\1\6\uffff\1\11\10\uffff\1\11\3\uffff\1\1\7\uffff\1"+
            "\11",
            "\1\11\1\1\6\uffff\1\11\10\uffff\1\11\3\uffff\1\1\7\uffff\1"+
            "\11",
            "\1\11\1\1\6\uffff\1\11\10\uffff\1\11\3\uffff\1\1\7\uffff\1"+
            "\11",
            "",
            "\1\11\1\1\23\uffff\1\1\7\uffff\1\11",
            "\1\11\1\1\23\uffff\1\1\7\uffff\1\11"
    };

    static final short[] DFA27_eot = DFA.unpackEncodedString(DFA27_eotS);
    static final short[] DFA27_eof = DFA.unpackEncodedString(DFA27_eofS);
    static final char[] DFA27_min = DFA.unpackEncodedStringToUnsignedChars(DFA27_minS);
    static final char[] DFA27_max = DFA.unpackEncodedStringToUnsignedChars(DFA27_maxS);
    static final short[] DFA27_accept = DFA.unpackEncodedString(DFA27_acceptS);
    static final short[] DFA27_special = DFA.unpackEncodedString(DFA27_specialS);
    static final short[][] DFA27_transition;

    static {
        int numStates = DFA27_transitionS.length;
        DFA27_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA27_transition[i] = DFA.unpackEncodedString(DFA27_transitionS[i]);
        }
    }

    class DFA27 extends DFA {

        public DFA27(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 27;
            this.eot = DFA27_eot;
            this.eof = DFA27_eof;
            this.min = DFA27_min;
            this.max = DFA27_max;
            this.accept = DFA27_accept;
            this.special = DFA27_special;
            this.transition = DFA27_transition;
        }
        public String getDescription() {
            return "334:1: csConstraint_priority4 : ( csPattern LARROW csBQTerm -> ^( Cst_MatchTermConstraint csPattern csBQTerm ) | LPAR csConstraint RPAR -> csConstraint );";
        }
    }
    static final String DFA35_eotS =
        "\43\uffff";
    static final String DFA35_eofS =
        "\1\uffff\1\17\1\uffff\5\20\33\uffff";
    static final String DFA35_minS =
        "\1\u0153\1\u0155\1\uffff\5\u0155\1\u0153\2\uffff\2\u0155\4\uffff"+
        "\10\u0156\1\u0153\1\u0155\10\u0156";
    static final String DFA35_maxS =
        "\1\u0171\1\u0173\1\uffff\5\u0173\1\u0171\2\uffff\2\u0172\4\uffff"+
        "\1\u016c\7\u016a\1\u0171\1\u0172\1\u016c\7\u016a";
    static final String DFA35_acceptS =
        "\2\uffff\1\2\6\uffff\1\6\1\1\2\uffff\1\3\1\4\1\5\1\7\22\uffff";
    static final String DFA35_specialS =
        "\43\uffff}>";
    static final String[] DFA35_transitionS = {
            "\1\1\1\uffff\1\10\22\uffff\1\2\1\11\3\uffff\1\3\1\4\1\5\1\6"+
            "\1\7",
            "\1\15\1\17\4\uffff\5\17\6\uffff\1\17\1\12\3\uffff\1\13\1\14"+
            "\5\uffff\1\16\1\17",
            "",
            "\1\15\1\20\4\uffff\5\20\6\uffff\1\20\13\uffff\1\16\1\20",
            "\1\15\1\20\4\uffff\5\20\6\uffff\1\20\13\uffff\1\16\1\20",
            "\1\15\1\20\4\uffff\5\20\6\uffff\1\20\13\uffff\1\16\1\20",
            "\1\15\1\20\4\uffff\5\20\6\uffff\1\20\13\uffff\1\16\1\20",
            "\1\15\1\20\4\uffff\5\20\6\uffff\1\20\13\uffff\1\16\1\20",
            "\1\21\31\uffff\1\22\1\23\1\24\1\25\1\26",
            "",
            "",
            "\1\15\34\uffff\1\16",
            "\1\15\34\uffff\1\16",
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
            "\1\15\34\uffff\1\16",
            "\1\32\23\uffff\1\31\1\41\1\42",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31",
            "\1\32\23\uffff\1\31"
    };

    static final short[] DFA35_eot = DFA.unpackEncodedString(DFA35_eotS);
    static final short[] DFA35_eof = DFA.unpackEncodedString(DFA35_eofS);
    static final char[] DFA35_min = DFA.unpackEncodedStringToUnsignedChars(DFA35_minS);
    static final char[] DFA35_max = DFA.unpackEncodedStringToUnsignedChars(DFA35_maxS);
    static final short[] DFA35_accept = DFA.unpackEncodedString(DFA35_acceptS);
    static final short[] DFA35_special = DFA.unpackEncodedString(DFA35_specialS);
    static final short[][] DFA35_transition;

    static {
        int numStates = DFA35_transitionS.length;
        DFA35_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA35_transition[i] = DFA.unpackEncodedString(DFA35_transitionS[i]);
        }
    }

    class DFA35 extends DFA {

        public DFA35(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 35;
            this.eot = DFA35_eot;
            this.eof = DFA35_eof;
            this.min = DFA35_min;
            this.max = DFA35_max;
            this.accept = DFA35_accept;
            this.special = DFA35_special;
            this.transition = DFA35_transition;
        }
        public String getDescription() {
            return "351:1: csPattern : ( IDENTIFIER AT csPattern -> ^( Cst_AnnotatedPattern csPattern IDENTIFIER ) | ANTI csPattern -> ^( Cst_Anti csPattern ) | csHeadSymbolList csExplicitTermList -> ^( Cst_Appl csHeadSymbolList csExplicitTermList ) | csHeadSymbolList csImplicitPairList -> ^( Cst_RecordAppl csHeadSymbolList csImplicitPairList ) | IDENTIFIER (s= STAR )? -> {s!=null}? ^( Cst_VariableStar IDENTIFIER ) -> ^( Cst_Variable IDENTIFIER ) | UNDERSCORE (s= STAR )? -> {s!=null}? ^( Cst_UnamedVariableStar ) -> ^( Cst_UnamedVariable ) | csConstantValue (s= STAR )? -> {s!=null}? ^( Cst_ConstantStar csConstantValue ) -> ^( Cst_Constant csConstantValue ) );";
        }
    }
    static final String DFA38_eotS =
        "\12\uffff";
    static final String DFA38_eofS =
        "\1\uffff\1\11\10\uffff";
    static final String DFA38_minS =
        "\1\u0153\1\u0155\10\uffff";
    static final String DFA38_maxS =
        "\1\u0171\1\u0172\10\uffff";
    static final String DFA38_acceptS =
        "\2\uffff\1\4\1\5\1\6\1\7\1\10\1\2\1\3\1\1";
    static final String DFA38_specialS =
        "\12\uffff}>";
    static final String[] DFA38_transitionS = {
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

    static final short[] DFA38_eot = DFA.unpackEncodedString(DFA38_eotS);
    static final short[] DFA38_eof = DFA.unpackEncodedString(DFA38_eofS);
    static final char[] DFA38_min = DFA.unpackEncodedStringToUnsignedChars(DFA38_minS);
    static final char[] DFA38_max = DFA.unpackEncodedStringToUnsignedChars(DFA38_maxS);
    static final short[] DFA38_accept = DFA.unpackEncodedString(DFA38_acceptS);
    static final short[] DFA38_special = DFA.unpackEncodedString(DFA38_specialS);
    static final short[][] DFA38_transition;

    static {
        int numStates = DFA38_transitionS.length;
        DFA38_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA38_transition[i] = DFA.unpackEncodedString(DFA38_transitionS[i]);
        }
    }

    class DFA38 extends DFA {

        public DFA38(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 38;
            this.eot = DFA38_eot;
            this.eof = DFA38_eof;
            this.min = DFA38_min;
            this.max = DFA38_max;
            this.accept = DFA38_accept;
            this.special = DFA38_special;
            this.transition = DFA38_transition;
        }
        public String getDescription() {
            return "398:1: csHeadSymbol : ( IDENTIFIER -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryDEFAULT ) ) | IDENTIFIER QMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAU ) ) | IDENTIFIER DQMARK -> ^( Cst_Symbol IDENTIFIER ^( Cst_TheoryAC ) ) | INTEGER -> ^( Cst_ConstantInt INTEGER ) | LONG -> ^( Cst_ConstantLong LONG ) | CHAR -> ^( Cst_ConstantChar CHAR ) | DOUBLE -> ^( Cst_ConstantDouble DOUBLE ) | STRING -> ^( Cst_ConstantString STRING ) );";
        }
    }
 

    public static final BitSet FOLLOW_LBR_in_csIncludeConstruct90 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csIncludeConstruct94 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csIncludeConstruct96 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csName_in_csStrategyConstruct129 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csStrategyConstruct131 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000480000L});
    public static final BitSet FOLLOW_csSlotList_in_csStrategyConstruct133 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csStrategyConstruct135 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_EXTENDS_in_csStrategyConstruct137 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00001080000L});
    public static final BitSet FOLLOW_BQUOTE_in_csStrategyConstruct139 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00001080000L});
    public static final BitSet FOLLOW_csBQTerm_in_csStrategyConstruct142 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csStrategyConstruct144 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000002100000L});
    public static final BitSet FOLLOW_csStrategyVisitList_in_csStrategyConstruct146 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csStrategyConstruct148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csStrategyVisit_in_csStrategyVisitList250 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_VISIT_in_csStrategyVisit273 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csStrategyVisit277 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csStrategyVisit279 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001380000L});
    public static final BitSet FOLLOW_csVisitAction_in_csStrategyVisit282 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001380000L});
    public static final BitSet FOLLOW_RBR_in_csStrategyVisit286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csVisitAction370 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_csVisitAction374 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csExtendedConstraint_in_csVisitAction378 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_ARROW_in_csVisitAction380 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csVisitAction382 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csVisitAction384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csVisitAction605 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_csVisitAction609 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csExtendedConstraint_in_csVisitAction613 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_ARROW_in_csVisitAction615 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00001080000L});
    public static final BitSet FOLLOW_csBQTerm_in_csVisitAction617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csMatchConstruct974 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00001080000L});
    public static final BitSet FOLLOW_csBQTerm_in_csMatchConstruct976 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_csMatchConstruct981 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00001080000L});
    public static final BitSet FOLLOW_csBQTerm_in_csMatchConstruct983 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_RPAR_in_csMatchConstruct989 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csMatchConstruct991 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001380000L});
    public static final BitSet FOLLOW_csExtendedConstraintAction_in_csMatchConstruct993 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001380000L});
    public static final BitSet FOLLOW_RBR_in_csMatchConstruct996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csMatchConstruct1059 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csMatchConstruct1063 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csMatchConstruct1067 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001380000L});
    public static final BitSet FOLLOW_csConstraintAction_in_csMatchConstruct1069 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001380000L});
    public static final BitSet FOLLOW_RBR_in_csMatchConstruct1072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csConstraintAction1133 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_csConstraintAction1137 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csConstraint_in_csConstraintAction1141 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_ARROW_in_csConstraintAction1143 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csConstraintAction1145 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csConstraintAction1147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csExtendedConstraintAction1320 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_csExtendedConstraintAction1324 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csExtendedConstraint_in_csExtendedConstraintAction1328 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_ARROW_in_csExtendedConstraintAction1330 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csExtendedConstraintAction1332 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csExtendedConstraintAction1334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csBQTerm1521 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csBQTerm1527 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_STAR_in_csBQTerm1532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csBQTerm1652 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csBQTerm1654 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00001480000L});
    public static final BitSet FOLLOW_csBQTerm_in_csBQTerm1659 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_csBQTerm1662 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00001080000L});
    public static final BitSet FOLLOW_csBQTerm_in_csBQTerm1666 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_RPAR_in_csBQTerm1672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csConstantValue_in_csBQTerm1707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csMatchArgumentConstraintList_in_csExtendedConstraint1738 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x00000000C0000000L});
    public static final BitSet FOLLOW_AND_in_csExtendedConstraint1744 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_OR_in_csExtendedConstraint1748 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csConstraint_in_csExtendedConstraint1751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csMatchArgumentConstraint_in_csMatchArgumentConstraintList1804 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csMatchArgumentConstraintList1807 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csMatchArgumentConstraint_in_csMatchArgumentConstraintList1811 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csMatchArgumentConstraintList1816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csPattern_in_csMatchArgumentConstraint1858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csConstraint_priority1_in_csConstraint1881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csConstraint_priority2_in_csConstraint_priority11892 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_OR_in_csConstraint_priority11897 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csConstraint_priority2_in_csConstraint_priority11899 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_csConstraint_priority3_in_csConstraint_priority21930 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_AND_in_csConstraint_priority21935 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csConstraint_priority3_in_csConstraint_priority21937 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_csTerm_in_csConstraint_priority31972 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000003F00000000L});
    public static final BitSet FOLLOW_GREATERTHAN_in_csConstraint_priority31979 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_GREATEROREQU_in_csConstraint_priority31983 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_LOWERTHAN_in_csConstraint_priority31987 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_LOWEROREQU_in_csConstraint_priority31994 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_DOUBLEEQUAL_in_csConstraint_priority31999 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_DIFFERENT_in_csConstraint_priority32004 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csTerm_in_csConstraint_priority32011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csConstraint_priority4_in_csConstraint_priority32135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csPattern_in_csConstraint_priority42150 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_LARROW_in_csConstraint_priority42152 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00001080000L});
    public static final BitSet FOLLOW_csBQTerm_in_csConstraint_priority42158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csConstraint_priority42177 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csConstraint_in_csConstraint_priority42179 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csConstraint_priority42181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csTerm2197 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_STAR_in_csTerm2202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csTerm2242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csTerm2244 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000480000L});
    public static final BitSet FOLLOW_csTerm_in_csTerm2247 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_csTerm2250 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csTerm_in_csTerm2252 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_RPAR_in_csTerm2258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csPattern2289 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_AT_in_csPattern2291 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csPattern_in_csPattern2293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ANTI_in_csPattern2313 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csPattern_in_csPattern2315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csHeadSymbolList_in_csPattern2333 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_csExplicitTermList_in_csPattern2335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csHeadSymbolList_in_csPattern2356 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_csImplicitPairList_in_csPattern2358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csPattern2381 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_STAR_in_csPattern2386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_csPattern2424 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_STAR_in_csPattern2429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csConstantValue_in_csPattern2464 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_STAR_in_csPattern2469 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csHeadSymbol_in_csHeadSymbolList2519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csHeadSymbolList2534 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00000080000L});
    public static final BitSet FOLLOW_csHeadSymbol_in_csHeadSymbolList2536 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000040000400000L});
    public static final BitSet FOLLOW_PIPE_in_csHeadSymbolList2539 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E00000080000L});
    public static final BitSet FOLLOW_csHeadSymbol_in_csHeadSymbolList2541 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000040000400000L});
    public static final BitSet FOLLOW_RPAR_in_csHeadSymbolList2545 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csHeadSymbol2570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csHeadSymbol2588 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_QMARK_in_csHeadSymbol2590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csHeadSymbol2608 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_DQMARK_in_csHeadSymbol2610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_csHeadSymbol2628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LONG_in_csHeadSymbol2642 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_in_csHeadSymbol2656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_in_csHeadSymbol2670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_csHeadSymbol2684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_csConstantValue0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_csExplicitTermList2727 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001680000L});
    public static final BitSet FOLLOW_csPattern_in_csExplicitTermList2730 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_COMMA_in_csExplicitTermList2733 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csPattern_in_csExplicitTermList2735 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010400000L});
    public static final BitSet FOLLOW_RPAR_in_csExplicitTermList2741 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LSQUAREBR_in_csImplicitPairList2763 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0008000000080000L});
    public static final BitSet FOLLOW_csPairPattern_in_csImplicitPairList2766 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0008000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csImplicitPairList2769 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csPairPattern_in_csImplicitPairList2771 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0008000010000000L});
    public static final BitSet FOLLOW_RSQUAREBR_in_csImplicitPairList2778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csPairPattern2800 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_EQUAL_in_csPairPattern2802 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csPattern_in_csPairPattern2804 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorConstruct2839 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csOperatorConstruct2843 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csOperatorConstruct2845 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000480000L});
    public static final BitSet FOLLOW_csSlotList_in_csOperatorConstruct2847 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csOperatorConstruct2849 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csOperatorConstruct2853 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x01E0000000100000L});
    public static final BitSet FOLLOW_csKeywordIsFsym_in_csOperatorConstruct2865 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x01E0000000100000L});
    public static final BitSet FOLLOW_csKeywordMake_in_csOperatorConstruct2876 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x01E0000000100000L});
    public static final BitSet FOLLOW_csKeywordGetSlot_in_csOperatorConstruct2887 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x01E0000000100000L});
    public static final BitSet FOLLOW_csKeywordGetDefault_in_csOperatorConstruct2898 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x01E0000000100000L});
    public static final BitSet FOLLOW_RBR_in_csOperatorConstruct2909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorArrayConstruct2990 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csOperatorArrayConstruct2994 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csOperatorArrayConstruct2996 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorArrayConstruct3000 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_STAR_in_csOperatorArrayConstruct3002 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csOperatorArrayConstruct3004 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csOperatorArrayConstruct3008 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_csKeywordIsFsym_in_csOperatorArrayConstruct3019 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0xD000000000100000L,0x0000000000000001L});
    public static final BitSet FOLLOW_csKeywordMakeEmpty_Array_in_csOperatorArrayConstruct3028 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0xD000000000100000L,0x0000000000000001L});
    public static final BitSet FOLLOW_csKeywordMakeAppend_in_csOperatorArrayConstruct3037 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0xD000000000100000L,0x0000000000000001L});
    public static final BitSet FOLLOW_csKeywordGetElement_in_csOperatorArrayConstruct3046 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0xD000000000100000L,0x0000000000000001L});
    public static final BitSet FOLLOW_csKeywordGetSize_in_csOperatorArrayConstruct3055 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0xD000000000100000L,0x0000000000000001L});
    public static final BitSet FOLLOW_RBR_in_csOperatorArrayConstruct3065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorListConstruct3151 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csOperatorListConstruct3155 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csOperatorListConstruct3157 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csOperatorListConstruct3161 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000020000000L});
    public static final BitSet FOLLOW_STAR_in_csOperatorListConstruct3163 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csOperatorListConstruct3165 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csOperatorListConstruct3169 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_csKeywordIsFsym_in_csOperatorListConstruct3178 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x3E00000000100000L});
    public static final BitSet FOLLOW_csKeywordMakeEmpty_List_in_csOperatorListConstruct3187 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x3E00000000100000L});
    public static final BitSet FOLLOW_csKeywordMakeInsert_in_csOperatorListConstruct3196 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x3E00000000100000L});
    public static final BitSet FOLLOW_csKeywordGetHead_in_csOperatorListConstruct3205 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x3E00000000100000L});
    public static final BitSet FOLLOW_csKeywordGetTail_in_csOperatorListConstruct3214 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x3E00000000100000L});
    public static final BitSet FOLLOW_csKeywordIsEmpty_in_csOperatorListConstruct3223 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x3E00000000100000L});
    public static final BitSet FOLLOW_RBR_in_csOperatorListConstruct3233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csTypetermConstruct3320 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000840000L});
    public static final BitSet FOLLOW_EXTENDS_in_csTypetermConstruct3323 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csTypetermConstruct3327 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csTypetermConstruct3333 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_csKeywordImplement_in_csTypetermConstruct3341 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L,0x000000000000000CL});
    public static final BitSet FOLLOW_csKeywordIsSort_in_csTypetermConstruct3346 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L,0x0000000000000008L});
    public static final BitSet FOLLOW_csKeywordEquals_in_csTypetermConstruct3353 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csTypetermConstruct3359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csSlot_in_csSlotList3491 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csSlotList3494 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csSlot_in_csSlotList3496 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csSlot3526 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_csSlot3528 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csSlot3532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csSlot3560 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csSlot3564 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_IS_FSYM_in_csKeywordIsFsym3598 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordIsFsym3600 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordIsFsym3604 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordIsFsym3606 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordIsFsym3610 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordIsFsym3614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_SLOT_in_csKeywordGetSlot3654 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetSlot3656 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetSlot3660 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordGetSlot3662 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetSlot3666 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetSlot3668 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetSlot3672 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetSlot3676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_in_csKeywordMake3719 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMake3721 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000480000L});
    public static final BitSet FOLLOW_csNameList_in_csKeywordMake3725 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMake3727 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMake3731 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMake3735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_DEFAULT_in_csKeywordGetDefault3775 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetDefault3777 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetDefault3781 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetDefault3783 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetDefault3787 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetDefault3791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_HEAD_in_csKeywordGetHead3831 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetHead3833 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetHead3837 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetHead3839 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetHead3844 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetHead3848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_TAIL_in_csKeywordGetTail3888 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetTail3890 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetTail3894 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetTail3896 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetTail3900 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetTail3904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_IS_EMPTY_in_csKeywordIsEmpty3944 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordIsEmpty3946 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordIsEmpty3950 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordIsEmpty3952 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordIsEmpty3956 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordIsEmpty3960 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_EMPTY_in_csKeywordMakeEmpty_List4000 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMakeEmpty_List4002 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMakeEmpty_List4004 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMakeEmpty_List4008 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMakeEmpty_List4012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_EMPTY_in_csKeywordMakeEmpty_Array4049 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMakeEmpty_Array4051 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeEmpty_Array4055 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMakeEmpty_Array4057 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMakeEmpty_Array4061 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMakeEmpty_Array4065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_INSERT_in_csKeywordMakeInsert4105 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMakeInsert4111 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeInsert4115 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordMakeInsert4117 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeInsert4121 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMakeInsert4123 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMakeInsert4127 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMakeInsert4131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_ELEMENT_in_csKeywordGetElement4174 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetElement4180 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetElement4184 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordGetElement4186 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetElement4190 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetElement4192 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetElement4196 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetElement4200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_GET_SIZE_in_csKeywordGetSize4243 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordGetSize4245 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordGetSize4249 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordGetSize4251 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordGetSize4255 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordGetSize4259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_MAKE_APPEND_in_csKeywordMakeAppend4299 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordMakeAppend4306 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeAppend4310 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordMakeAppend4312 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordMakeAppend4316 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordMakeAppend4318 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordMakeAppend4322 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordMakeAppend4326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_IMPLEMENT_in_csKeywordImplement4369 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordImplement4371 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordImplement4375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_IS_SORT_in_csKeywordIsSort4397 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordIsSort4399 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordIsSort4403 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordIsSort4405 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordIsSort4411 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordIsSort4415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KEYWORD_EQUALS_in_csKeywordEquals4440 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_LPAR_in_csKeywordEquals4442 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordEquals4446 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csKeywordEquals4448 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csKeywordEquals4452 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_RPAR_in_csKeywordEquals4454 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_csKeywordEquals4460 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_csKeywordEquals4464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csName_in_csNameList4509 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_COMMA_in_csNameList4512 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csName_in_csNameList4514 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_csName4540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred5_miniTom370 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_synpred5_miniTom374 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0003E30001280000L});
    public static final BitSet FOLLOW_csExtendedConstraint_in_synpred5_miniTom378 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_ARROW_in_synpred5_miniTom380 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_LBR_in_synpred5_miniTom382 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_RBR_in_synpred5_miniTom384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred14_miniTom1521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csTerm_in_synpred31_miniTom1972 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000003F00000000L});
    public static final BitSet FOLLOW_GREATERTHAN_in_synpred31_miniTom1979 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_GREATEROREQU_in_synpred31_miniTom1983 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_LOWERTHAN_in_synpred31_miniTom1987 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_LOWEROREQU_in_synpred31_miniTom1994 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_DOUBLEEQUAL_in_synpred31_miniTom1999 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_DIFFERENT_in_synpred31_miniTom2004 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_csTerm_in_synpred31_miniTom2011 = new BitSet(new long[]{0x0000000000000002L});

}