// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g 2016-03-01 15:43:31

package tom.engine.parser.antlr3;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.Tree;

import static tom.engine.parser.antlr3.TreeFactory.*;
import static tom.engine.parser.antlr3.miniTomParser.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class BQTermParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Code", "IsSort", "DoWhileExpression", "ACMatchLoop", "ConstraintToExpression", "GetSliceArray", "GetSliceList", "GetElement", "GetSize", "Substract", "SubstractOne", "AddOne", "IsEmptyArray", "IsEmptyList", "GetTail", "GetHead", "IsFsym", "GetSlot", "Cast", "EqualBQTerm", "EqualTerm", "Integer", "FalseTL", "TrueTL", "Conditional", "AntiMatchExpression", "LessOrEqualThan", "LessThan", "GreaterOrEqualThan", "GreaterThan", "OrConnector", "OrExpressionDisjunction", "Or", "And", "Negation", "Bottom", "TomInstructionToExpression", "BQTermToExpression", "Cst_ConstantString", "Cst_ConstantDouble", "Cst_ConstantChar", "Cst_ConstantLong", "Cst_ConstantInt", "Cst_Symbol", "Cst_TheoryDEFAULT", "Cst_TheoryAC", "Cst_TheoryAU", "ConcCstSymbol", "Cst_Slot", "Cst_StrategyConstruct", "Cst_BQTermToBlock", "Cst_IncludeConstruct", "Cst_MetaQuoteConstruct", "Cst_TypetermConstruct", "Cst_OpListConstruct", "Cst_OpArrayConstruct", "Cst_OpConstruct", "Cst_MatchConstruct", "HOSTBLOCK", "Cst_PairPattern", "ConcCstPairPattern", "Cst_TermAppl", "Cst_TermVariableStar", "Cst_TermVariable", "ConcCstConstraint", "Cst_ConstraintAction", "Cst_OriginTracking", "Cst_Label", "NodeString", "ConcCstVisit", "ConcCstConstraintAction", "Cst_RecordAppl", "Cst_Appl", "Cst_UnamedVariableStar", "Cst_UnamedVariable", "Cst_ConstantStar", "Cst_Constant", "Cst_VariableStar", "Cst_Variable", "Cst_AnnotatedPattern", "Cst_Anti", "ConcCstBQTerm", "ConcCstTerm", "ConcCstOperator", "ConcCstName", "ConcCstPattern", "ConcCstOption", "ConcCstPairSlotBQTerm", "Cst_Program", "Cst_PairSlotBQTerm", "Cst_TypeUnknown", "Cst_Type", "Cst_VisitTerm", "Cst_Equals", "Cst_IsSort", "Cst_Implement", "Cst_MakeEmptyList", "Cst_Make", "Cst_MakeAppend", "Cst_GetElement", "Cst_MakeInsert", "Cst_GetSlot", "Cst_GetDefault", "Cst_GetTail", "Cst_GetSize", "Cst_MakeEmptyArray", "Cst_IsEmpty", "Cst_GetHead", "Cst_IsFsym", "NodeInt", "ConcCstBlock", "Cst_EmptyName", "Cst_Name", "ConcCstSlot", "Cst_NumDifferent", "Cst_NumEqualTo", "Cst_NumGreaterOrEqualTo", "Cst_NumGreaterThan", "Cst_NumLessOrEqualTo", "Cst_NumLessThan", "Cst_OrConstraint", "Cst_AndConstraint", "Cst_MatchArgumentConstraint", "Cst_MatchTermConstraint", "Cst_BQConstant", "Cst_ITL", "Cst_BQComposite", "Cst_BQDefault", "Cst_BQVarStar", "Cst_BQVar", "Cst_BQRecordAppl", "Cst_BQAppl", "AntiName", "EmptyName", "PositionName", "Name", "concTomNumber", "concTomName", "NameNumber", "RenamedVar", "AbsVar", "Position", "Save", "End", "Begin", "IndexNumber", "ListNumber", "PatternNumber", "MatchNumber", "UsedType", "UsedSymbolAC", "UsedSymbolDestructor", "UsedSymbolConstructor", "ResolveStratBlock", "VisitTerm", "concResolveStratElement", "concTomSymbol", "concTomVisit", "Entry", "concTomEntry", "TransfoStratInfo", "ResolveStratElement", "concElementaryTransformation", "concResolveStratBlock", "TextPosition", "StructTable", "ElementaryTransformation", "Symbol", "EmptySymbol", "Table", "TypeForVariable", "DefinedSymbol", "noOption", "ACSymbol", "ImplicitXMLChild", "ImplicitXMLAttribut", "ModuleName", "Debug", "Label", "MatchingTheory", "OriginalText", "OriginTracking", "GeneratedMatch", "TomTermToOption", "TomNameToOption", "DeclarationToOption", "concOption", "concConstraintInstruction", "concInstruction", "Resolve", "TracelinkPopulateResolve", "Tracelink", "RawAction", "CompiledPattern", "CompiledMatch", "Match", "NamedBlock", "UnamedBlock", "AbstractBlock", "Nop", "Return", "AssignArray", "Assign", "LetRef", "Let", "WhileDo", "DoWhile", "If", "CodeToInstruction", "ExpressionToInstruction", "BQTermToInstruction", "concRuleInstruction", "concRefClassTracelinkInstruction", "RefClassTracelinkInstruction", "ConstraintInstruction", "RuleInstruction", "EmptyTargetLanguageType", "TLType", "concTypeOption", "concTomType", "TypeVar", "EmptyType", "TypesToType", "Codomain", "Type", "SubtypeDecl", "WithSymbol", "IntegerPattern", "FalsePattern", "TruePattern", "TestVar", "Automata", "AntiTerm", "TomSymbolToTomTerm", "VariableStar", "Variable", "XMLAppl", "RecordAppl", "TermAppl", "concTomTerm", "concConstraint", "EmptyArrayConstraint", "EmptyListConstraint", "NumericConstraint", "AntiMatchConstraint", "MatchConstraint", "OrConstraintDisjunction", "OrConstraint", "AndConstraint", "IsSortConstraint", "Negate", "FalseConstraint", "TrueConstraint", "AssignPositionTo", "AliasTo", "NumEqual", "NumDifferent", "NumGreaterOrEqualThan", "NumGreaterThan", "NumLessOrEqualThan", "NumLessThan", "TomInclude", "Tom", "BQTermToCode", "DeclarationToCode", "InstructionToCode", "TargetLanguageToCode", "noTL", "Comment", "ITL", "TL", "ListTail", "ListHead", "VariableHeadArray", "VariableHeadList", "Subterm", "SymbolOf", "ExpressionToBQTerm", "BuildAppendArray", "BuildConsArray", "BuildEmptyArray", "BuildAppendList", "BuildConsList", "BuildEmptyList", "BuildTerm", "BuildConstant", "FunctionCall", "ReferencerBQTerm", "Composite", "BQDefault", "BQVariableStar", "BQVariable", "BQRecordAppl", "BQAppl", "concBQTerm", "CompositeTL", "CompositeBQTerm", "concCode", "AU", "AC", "Unitary", "Associative", "Syntactic", "concElementaryTheory", "PairNameOptions", "concTypeConstraint", "FalseTypeConstraint", "Subtype", "Equation", "concDeclaration", "BQTermToDeclaration", "ACSymbolDecl", "AbstractDecl", "EmptyDeclaration", "ArraySymbolDecl", "ListSymbolDecl", "SymbolDecl", "IntrospectorClass", "Class", "MethodDef", "FunctionDef", "ReferenceClass", "ResolveMakeDecl", "ResolveGetSlotDecl", "ResolveIsFsymDecl", "ResolveClassDecl", "ResolveInverseLinksDecl", "ResolveStratDecl", "Transformation", "Strategy", "MakeDecl", "MakeAddArray", "MakeEmptyArray", "GetSizeDecl", "GetElementDecl", "MakeAddList", "MakeEmptyList", "IsEmptyDecl", "GetTailDecl", "GetHeadDecl", "IsSortDecl", "EqualTermDecl", "ImplementDecl", "GetDefaultDecl", "GetSlotDecl", "IsFsymDecl", "TypeTermDecl", "concPairNameDecl", "PairNameDecl", "PairSlotBQTerm", "concSlot", "concBQSlot", "PairSlotAppl", "BQ", "FragWS", "FragID", "LPAR", "BQIDPAR", "BQIDBR", "BQIDSTAR", "BQID", "BQPAR", "IDPAR", "IDBR", "IDSTAR", "ID", "UNDERSCORE", "COMMA", "RPAR", "RBR", "EQUAL", "MINUS", "LETTER", "DIGIT", "BQESC", "BQSTRING", "BQCHAR", "INTEGER", "BQDOT", "NUM", "WS", "SL_COMMENT", "ML_COMMENT", "ANY"
    };
    public static final int concInstruction=198;
    public static final int EmptyTargetLanguageType=226;
    public static final int Cst_MatchTermConstraint=133;
    public static final int MakeEmptyList=346;
    public static final int Cst_Label=77;
    public static final int NumLessOrEqualThan=269;
    public static final int GetHead=25;
    public static final int ResolveGetSlotDecl=333;
    public static final int UsedSymbolConstructor=162;
    public static final int ResolveStratElement=171;
    public static final int VariableStar=244;
    public static final int Cst_RecordAppl=81;
    public static final int GetSlot=27;
    public static final int Cst_MakeEmptyArray=115;
    public static final int Tracelink=201;
    public static final int EmptySymbol=178;
    public static final int BQTermToDeclaration=320;
    public static final int GetDefaultDecl=353;
    public static final int Cst_Symbol=53;
    public static final int BQRecordAppl=302;
    public static final int Automata=241;
    public static final int Conditional=34;
    public static final int Cst_OriginTracking=76;
    public static final int Cst_NumGreaterOrEqualTo=126;
    public static final int ACSymbolDecl=321;
    public static final int Cst_UnamedVariableStar=83;
    public static final int FalseTL=32;
    public static final int UNDERSCORE=376;
    public static final int Cst_Name=122;
    public static final int AndConstraint=258;
    public static final int NamedBlock=206;
    public static final int CompiledPattern=203;
    public static final int concResolveStratBlock=173;
    public static final int Strategy=339;
    public static final int concResolveStratElement=165;
    public static final int Cst_TypetermConstruct=63;
    public static final int IDSTAR=374;
    public static final int MakeAddArray=341;
    public static final int SL_COMMENT=391;
    public static final int BQESC=384;
    public static final int ConcCstSymbol=57;
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
    public static final int TextPosition=174;
    public static final int DeclarationToOption=195;
    public static final int Cst_IsEmpty=116;
    public static final int ListTail=281;
    public static final int Cst_Variable=88;
    public static final int ArraySymbolDecl=324;
    public static final int MINUS=381;
    public static final int concTypeOption=228;
    public static final int FragWS=364;
    public static final int BQ=363;
    public static final int Cst_BQAppl=141;
    public static final int Cst_AnnotatedPattern=89;
    public static final int GetSizeDecl=343;
    public static final int Match=205;
    public static final int TomInclude=271;
    public static final int concTomVisit=167;
    public static final int Cst_MakeEmptyList=106;
    public static final int BQTermToExpression=47;
    public static final int GetHeadDecl=349;
    public static final int AbstractBlock=208;
    public static final int BQDOT=388;
    public static final int Position=151;
    public static final int Transformation=338;
    public static final int BQIDSTAR=369;
    public static final int LessThan=37;
    public static final int Cst_Implement=105;
    public static final int Cst_TermVariable=73;
    public static final int concCode=307;
    public static final int Cst_GetHead=117;
    public static final int Cst_GetSlot=111;
    public static final int Cst_BQConstant=134;
    public static final int Cst_Anti=90;
    public static final int noTL=277;
    public static final int FalseConstraint=261;
    public static final int Entry=168;
    public static final int Let=214;
    public static final int LPAR=366;
    public static final int Cst_ITL=135;
    public static final int BQTermToInstruction=220;
    public static final int NodeString=78;
    public static final int Cst_OrConstraint=130;
    public static final int XMLAppl=246;
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
    public static final int Cst_Make=107;
    public static final int TransfoStratInfo=170;
    public static final int ConcCstBlock=120;
    public static final int Cst_Type=101;
    public static final int Cst_Appl=82;
    public static final int NumGreaterThan=268;
    public static final int BQVariableStar=300;
    public static final int IsSort=11;
    public static final int AssignArray=211;
    public static final int Cst_ConstraintAction=75;
    public static final int Unitary=310;
    public static final int BuildConstant=295;
    public static final int RBR=379;
    public static final int OrConstraint=257;
    public static final int AntiMatchConstraint=254;
    public static final int EqualBQTerm=29;
    public static final int VariableHeadList=284;
    public static final int ID=375;
    public static final int ResolveInverseLinksDecl=336;
    public static final int Cst_PairPattern=69;
    public static final int UsedType=159;
    public static final int Begin=154;
    public static final int TomNameToOption=194;
    public static final int PositionName=144;
    public static final int RawAction=202;
    public static final int ResolveStratBlock=163;
    public static final int ImplicitXMLAttribut=185;
    public static final int Cst_TermAppl=71;
    public static final int WithSymbol=236;
    public static final int COMMA=377;
    public static final int EQUAL=380;
    public static final int BQID=370;
    public static final int Cst_BQTermToBlock=60;
    public static final int ImplicitXMLChild=184;
    public static final int ResolveIsFsymDecl=334;
    public static final int SubtypeDecl=235;
    public static final int Cst_BQRecordAppl=140;
    public static final int DIGIT=383;
    public static final int Cst_NumGreaterThan=127;
    public static final int ACMatchLoop=13;
    public static final int ExpressionToInstruction=219;
    public static final int Cst_EmptyName=121;
    public static final int concTomTerm=249;
    public static final int Negation=44;
    public static final int If=217;
    public static final int BQIDBR=368;
    public static final int RecordAppl=247;
    public static final int Cst_Equals=103;
    public static final int Type=234;
    public static final int HOSTBLOCK=68;
    public static final int Cst_ConstantStar=85;
    public static final int IsEmptyList=23;
    public static final int MakeEmptyArray=342;
    public static final int AliasTo=264;
    public static final int BQAppl=303;
    public static final int Cst_NumLessOrEqualTo=128;
    public static final int Cst_Slot=58;
    public static final int BuildConsList=292;
    public static final int ConcCstBQTerm=91;
    public static final int Cst_Constant=86;
    public static final int OriginalText=190;
    public static final int DeclarationToCode=274;
    public static final int Cst_OpConstruct=66;
    public static final int FalsePattern=238;
    public static final int ModuleName=186;
    public static final int ConcCstPairPattern=70;
    public static final int Cst_BQComposite=136;
    public static final int DoWhileExpression=12;
    public static final int ConcCstVisit=79;
    public static final int AssignPositionTo=263;
    public static final int TypeVar=230;
    public static final int FunctionDef=330;
    public static final int concDeclaration=319;
    public static final int LETTER=382;
    public static final int BQDefault=299;
    public static final int ListSymbolDecl=325;
    public static final int noOption=182;
    public static final int BQSTRING=385;
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
    public static final int CompiledMatch=204;
    public static final int Cst_StrategyConstruct=59;
    public static final int Class=328;
    public static final int MethodDef=329;
    public static final int Cst_IsSort=104;
    public static final int MakeAddList=345;
    public static final int Code=10;
    public static final int AntiMatchExpression=35;
    public static final int RefClassTracelinkInstruction=223;
    public static final int concTypeConstraint=315;
    public static final int ListHead=282;
    public static final int SubstractOne=20;
    public static final int WS=390;
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
    public static final int Subtype=317;
    public static final int WhileDo=215;
    public static final int Cst_MatchConstruct=67;
    public static final int EqualTerm=30;
    public static final int GetSliceArray=15;
    public static final int Or=42;
    public static final int ConcCstName=94;
    public static final int Resolve=199;
    public static final int Composite=298;
    public static final int BuildEmptyList=293;
    public static final int INTEGER=387;
    public static final int concPairNameDecl=357;
    public static final int TL=280;
    public static final int BQIDPAR=367;
    public static final int Cst_BQVar=139;
    public static final int UsedSymbolAC=160;
    public static final int FragID=365;
    public static final int Cst_VisitTerm=102;
    public static final int OriginTracking=191;
    public static final int OrConstraintDisjunction=256;
    public static final int EmptyArrayConstraint=251;
    public static final int Integer=31;
    public static final int concRuleInstruction=221;
    public static final int Save=152;
    public static final int ConcCstPattern=95;
    public static final int concElementaryTheory=313;
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
    public static final int BQCHAR=386;
    public static final int Cst_ConstantDouble=49;
    public static final int ConcCstOperator=93;
    public static final int TargetLanguageToCode=276;
    public static final int Cst_GetElement=109;
    public static final int AntiTerm=242;
    public static final int concConstraintInstruction=197;
    public static final int Cst_TypeUnknown=100;
    public static final int GetElementDecl=344;
    public static final int concTomEntry=169;
    public static final int ConcCstTerm=92;
    public static final int BuildTerm=294;
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
    public static final int SymbolDecl=326;
    public static final int Syntactic=312;
    public static final int End=153;
    public static final int PairNameOptions=314;
    public static final int Cst_BQDefault=137;
    public static final int ResolveMakeDecl=332;
    public static final int IntegerPattern=237;
    public static final int ExpressionToBQTerm=287;
    public static final int BuildAppendArray=288;
    public static final int IDPAR=372;
    public static final int Cast=28;
    public static final int PatternNumber=157;
    public static final int ML_COMMENT=392;
    public static final int NumGreaterOrEqualThan=267;
    public static final int Cst_PairSlotBQTerm=99;
    public static final int Cst_IsFsym=118;
    public static final int Cst_NumEqualTo=125;
    public static final int IndexNumber=155;
    public static final int NumEqual=265;
    public static final int BQPAR=371;
    public static final int IDBR=373;
    public static final int FunctionCall=296;
    public static final int Cst_MatchArgumentConstraint=132;
    public static final int TomTermToOption=193;
    public static final int IsFsymDecl=355;
    public static final int TestVar=240;
    public static final int Cst_VariableStar=87;
    public static final int Symbol=177;
    public static final int EmptyType=231;
    public static final int And=43;
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
    public static final int NUM=389;
    public static final int TrueTL=33;
    public static final int Cst_TheoryAU=56;
    public static final int GreaterOrEqualThan=38;
    public static final int TypesToType=232;
    public static final int ACSymbol=183;
    public static final int ANY=393;
    public static final int NumLessThan=270;
    public static final int Cst_ConstantInt=52;
    public static final int concTomType=229;
    public static final int Cst_TermVariableStar=72;
    public static final int Subterm=285;
    public static final int InstructionToCode=275;
    public static final int Name=145;
    public static final int Nop=209;
    public static final int RPAR=378;
    public static final int EmptyListConstraint=252;
    public static final int NumericConstraint=253;
    public static final int Cst_MetaQuoteConstruct=62;

    // delegates
    // delegators


        public BQTermParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public BQTermParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return BQTermParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g"; }



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
    //FIXME
        return
          makeOptions(t.getInputStream()!=null?t.getInputStream().getSourceName():"unknown",
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

    //FIXME
        return
          makeOptions(start.getInputStream()!=null?start.getInputStream().getSourceName():"unknown",
          start.getLine(), start.getCharPositionInLine(), lastCharLine, lastCharColumn);
      }


    public static class csBQTerm_return extends ParserRuleReturnScope {
        public int marker;
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csBQTerm"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:65:1: csBQTerm returns [int marker] : ( BQID -> ^( Cst_BQVar BQID ^( Cst_TypeUnknown ) ) | BQPAR ( csCompositePart )* RPAR -> ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_ITL BQPAR ) ^( Cst_BQComposite ^( ConcCstBQTerm ( csCompositePart )* ) ) ^( Cst_ITL RPAR ) ) ) | BQIDSTAR -> ^( Cst_BQVarStar BQIDSTAR ^( Cst_TypeUnknown ) ) | BQIDPAR ( csBQTermArgsComposite ( COMMA csBQTermArgsComposite )* )? RPAR -> ^( Cst_BQAppl BQIDPAR ^( ConcCstBQTerm ( csBQTermArgsComposite )* ) ) | BQIDBR ( csPairSlotBQTerm ( COMMA csPairSlotBQTerm )* )? RBR -> ^( Cst_BQRecordAppl BQIDBR ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* ) ) );
    public final BQTermParser.csBQTerm_return csBQTerm() throws RecognitionException {
        BQTermParser.csBQTerm_return retval = new BQTermParser.csBQTerm_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token BQID1=null;
        Token BQPAR2=null;
        Token RPAR4=null;
        Token BQIDSTAR5=null;
        Token BQIDPAR6=null;
        Token COMMA8=null;
        Token RPAR10=null;
        Token BQIDBR11=null;
        Token COMMA13=null;
        Token RBR15=null;
        BQTermParser.csCompositePart_return csCompositePart3 = null;

        BQTermParser.csBQTermArgsComposite_return csBQTermArgsComposite7 = null;

        BQTermParser.csBQTermArgsComposite_return csBQTermArgsComposite9 = null;

        BQTermParser.csPairSlotBQTerm_return csPairSlotBQTerm12 = null;

        BQTermParser.csPairSlotBQTerm_return csPairSlotBQTerm14 = null;


        Tree BQID1_tree=null;
        Tree BQPAR2_tree=null;
        Tree RPAR4_tree=null;
        Tree BQIDSTAR5_tree=null;
        Tree BQIDPAR6_tree=null;
        Tree COMMA8_tree=null;
        Tree RPAR10_tree=null;
        Tree BQIDBR11_tree=null;
        Tree COMMA13_tree=null;
        Tree RBR15_tree=null;
        RewriteRuleTokenStream stream_BQIDPAR=new RewriteRuleTokenStream(adaptor,"token BQIDPAR");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_BQIDSTAR=new RewriteRuleTokenStream(adaptor,"token BQIDSTAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_BQPAR=new RewriteRuleTokenStream(adaptor,"token BQPAR");
        RewriteRuleTokenStream stream_BQIDBR=new RewriteRuleTokenStream(adaptor,"token BQIDBR");
        RewriteRuleTokenStream stream_BQID=new RewriteRuleTokenStream(adaptor,"token BQID");
        RewriteRuleSubtreeStream stream_csPairSlotBQTerm=new RewriteRuleSubtreeStream(adaptor,"rule csPairSlotBQTerm");
        RewriteRuleSubtreeStream stream_csBQTermArgsComposite=new RewriteRuleSubtreeStream(adaptor,"rule csBQTermArgsComposite");
        RewriteRuleSubtreeStream stream_csCompositePart=new RewriteRuleSubtreeStream(adaptor,"rule csCompositePart");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:66:22: ( BQID -> ^( Cst_BQVar BQID ^( Cst_TypeUnknown ) ) | BQPAR ( csCompositePart )* RPAR -> ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_ITL BQPAR ) ^( Cst_BQComposite ^( ConcCstBQTerm ( csCompositePart )* ) ) ^( Cst_ITL RPAR ) ) ) | BQIDSTAR -> ^( Cst_BQVarStar BQIDSTAR ^( Cst_TypeUnknown ) ) | BQIDPAR ( csBQTermArgsComposite ( COMMA csBQTermArgsComposite )* )? RPAR -> ^( Cst_BQAppl BQIDPAR ^( ConcCstBQTerm ( csBQTermArgsComposite )* ) ) | BQIDBR ( csPairSlotBQTerm ( COMMA csPairSlotBQTerm )* )? RBR -> ^( Cst_BQRecordAppl BQIDBR ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* ) ) )
            int alt6=5;
            switch ( input.LA(1) ) {
            case BQID:
                {
                alt6=1;
                }
                break;
            case BQPAR:
                {
                alt6=2;
                }
                break;
            case BQIDSTAR:
                {
                alt6=3;
                }
                break;
            case BQIDPAR:
                {
                alt6=4;
                }
                break;
            case BQIDBR:
                {
                alt6=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:67:2: BQID
                    {
                    BQID1=(Token)match(input,BQID,FOLLOW_BQID_in_csBQTerm68); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQID.add(BQID1);

                    if ( state.backtracking==0 ) {
                      retval.marker = ((CustomToken)BQID1).getPayload(Integer.class);
                    }


                    // AST REWRITE
                    // elements: BQID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 69:3: -> ^( Cst_BQVar BQID ^( Cst_TypeUnknown ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:69:6: ^( Cst_BQVar BQID ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVar, "Cst_BQVar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)BQID1));
                        adaptor.addChild(root_1, stream_BQID.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:69:60: ^( Cst_TypeUnknown )
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
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:71:3: BQPAR ( csCompositePart )* RPAR
                    {
                    BQPAR2=(Token)match(input,BQPAR,FOLLOW_BQPAR_in_csBQTerm94); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQPAR.add(BQPAR2);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:71:9: ( csCompositePart )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0==LPAR||(LA1_0>=IDPAR && LA1_0<=UNDERSCORE)||LA1_0==EQUAL||(LA1_0>=BQSTRING && LA1_0<=BQCHAR)||(LA1_0>=BQDOT && LA1_0<=NUM)||LA1_0==ANY) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:0:0: csCompositePart
                    	    {
                    	    pushFollow(FOLLOW_csCompositePart_in_csBQTerm96);
                    	    csCompositePart3=csCompositePart();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csCompositePart.add(csCompositePart3.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop1;
                        }
                    } while (true);

                    RPAR4=(Token)match(input,RPAR,FOLLOW_RPAR_in_csBQTerm99); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR4);

                    if ( state.backtracking==0 ) {
                      retval.marker = ((CustomToken)RPAR4).getPayload(Integer.class);
                    }


                    // AST REWRITE
                    // elements: RPAR, csCompositePart, BQPAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 74:3: -> ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_ITL BQPAR ) ^( Cst_BQComposite ^( ConcCstBQTerm ( csCompositePart )* ) ) ^( Cst_ITL RPAR ) ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:74:6: ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_ITL BQPAR ) ^( Cst_BQComposite ^( ConcCstBQTerm ( csCompositePart )* ) ) ^( Cst_ITL RPAR ) ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQComposite, "Cst_BQComposite"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)BQPAR2, (CommonToken)RPAR4));
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:76:8: ^( ConcCstBQTerm ^( Cst_ITL BQPAR ) ^( Cst_BQComposite ^( ConcCstBQTerm ( csCompositePart )* ) ) ^( Cst_ITL RPAR ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:77:10: ^( Cst_ITL BQPAR )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_3);

                        adaptor.addChild(root_3, extractOptions((CommonToken)BQPAR2));
                        adaptor.addChild(root_3, stream_BQPAR.nextNode());

                        adaptor.addChild(root_2, root_3);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:78:10: ^( Cst_BQComposite ^( ConcCstBQTerm ( csCompositePart )* ) )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQComposite, "Cst_BQComposite"), root_3);

                        adaptor.addChild(root_3, extractOptions((CommonToken)(csCompositePart3!=null?((Token)csCompositePart3.stop):null)));
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:80:12: ^( ConcCstBQTerm ( csCompositePart )* )
                        {
                        Tree root_4 = (Tree)adaptor.nil();
                        root_4 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_4);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:80:28: ( csCompositePart )*
                        while ( stream_csCompositePart.hasNext() ) {
                            adaptor.addChild(root_4, stream_csCompositePart.nextTree());

                        }
                        stream_csCompositePart.reset();

                        adaptor.addChild(root_3, root_4);
                        }

                        adaptor.addChild(root_2, root_3);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:81:10: ^( Cst_ITL RPAR )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_3);

                        adaptor.addChild(root_3, extractOptions((CommonToken)RPAR4));
                        adaptor.addChild(root_3, stream_RPAR.nextNode());

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:85:3: BQIDSTAR
                    {
                    BQIDSTAR5=(Token)match(input,BQIDSTAR,FOLLOW_BQIDSTAR_in_csBQTerm232); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQIDSTAR.add(BQIDSTAR5);

                    if ( state.backtracking==0 ) {
                      retval.marker = ((CustomToken)BQIDSTAR5).getPayload(Integer.class);
                    }


                    // AST REWRITE
                    // elements: BQIDSTAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 88:3: -> ^( Cst_BQVarStar BQIDSTAR ^( Cst_TypeUnknown ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:88:6: ^( Cst_BQVarStar BQIDSTAR ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVarStar, "Cst_BQVarStar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)BQIDSTAR5));
                        adaptor.addChild(root_1, stream_BQIDSTAR.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:88:72: ^( Cst_TypeUnknown )
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
                case 4 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:90:3: BQIDPAR ( csBQTermArgsComposite ( COMMA csBQTermArgsComposite )* )? RPAR
                    {
                    BQIDPAR6=(Token)match(input,BQIDPAR,FOLLOW_BQIDPAR_in_csBQTerm259); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQIDPAR.add(BQIDPAR6);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:90:11: ( csBQTermArgsComposite ( COMMA csBQTermArgsComposite )* )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==LPAR||(LA3_0>=IDPAR && LA3_0<=UNDERSCORE)||(LA3_0>=BQSTRING && LA3_0<=BQCHAR)||(LA3_0>=BQDOT && LA3_0<=NUM)||LA3_0==ANY) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:90:12: csBQTermArgsComposite ( COMMA csBQTermArgsComposite )*
                            {
                            pushFollow(FOLLOW_csBQTermArgsComposite_in_csBQTerm262);
                            csBQTermArgsComposite7=csBQTermArgsComposite();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_csBQTermArgsComposite.add(csBQTermArgsComposite7.getTree());
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:90:34: ( COMMA csBQTermArgsComposite )*
                            loop2:
                            do {
                                int alt2=2;
                                int LA2_0 = input.LA(1);

                                if ( (LA2_0==COMMA) ) {
                                    alt2=1;
                                }


                                switch (alt2) {
                            	case 1 :
                            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:90:35: COMMA csBQTermArgsComposite
                            	    {
                            	    COMMA8=(Token)match(input,COMMA,FOLLOW_COMMA_in_csBQTerm265); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA8);

                            	    pushFollow(FOLLOW_csBQTermArgsComposite_in_csBQTerm267);
                            	    csBQTermArgsComposite9=csBQTermArgsComposite();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_csBQTermArgsComposite.add(csBQTermArgsComposite9.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop2;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR10=(Token)match(input,RPAR,FOLLOW_RPAR_in_csBQTerm273); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR10);

                    if ( state.backtracking==0 ) {
                      retval.marker = ((CustomToken)RPAR10).getPayload(Integer.class);
                    }


                    // AST REWRITE
                    // elements: BQIDPAR, csBQTermArgsComposite
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 93:3: -> ^( Cst_BQAppl BQIDPAR ^( ConcCstBQTerm ( csBQTermArgsComposite )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:93:6: ^( Cst_BQAppl BQIDPAR ^( ConcCstBQTerm ( csBQTermArgsComposite )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQAppl, "Cst_BQAppl"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)BQIDPAR6, (CommonToken)RPAR10));
                        adaptor.addChild(root_1, stream_BQIDPAR.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:94:15: ^( ConcCstBQTerm ( csBQTermArgsComposite )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:94:31: ( csBQTermArgsComposite )*
                        while ( stream_csBQTermArgsComposite.hasNext() ) {
                            adaptor.addChild(root_2, stream_csBQTermArgsComposite.nextTree());

                        }
                        stream_csBQTermArgsComposite.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:96:3: BQIDBR ( csPairSlotBQTerm ( COMMA csPairSlotBQTerm )* )? RBR
                    {
                    BQIDBR11=(Token)match(input,BQIDBR,FOLLOW_BQIDBR_in_csBQTerm309); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQIDBR.add(BQIDBR11);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:96:10: ( csPairSlotBQTerm ( COMMA csPairSlotBQTerm )* )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==ID) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:96:11: csPairSlotBQTerm ( COMMA csPairSlotBQTerm )*
                            {
                            pushFollow(FOLLOW_csPairSlotBQTerm_in_csBQTerm312);
                            csPairSlotBQTerm12=csPairSlotBQTerm();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_csPairSlotBQTerm.add(csPairSlotBQTerm12.getTree());
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:96:28: ( COMMA csPairSlotBQTerm )*
                            loop4:
                            do {
                                int alt4=2;
                                int LA4_0 = input.LA(1);

                                if ( (LA4_0==COMMA) ) {
                                    alt4=1;
                                }


                                switch (alt4) {
                            	case 1 :
                            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:96:29: COMMA csPairSlotBQTerm
                            	    {
                            	    COMMA13=(Token)match(input,COMMA,FOLLOW_COMMA_in_csBQTerm315); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA13);

                            	    pushFollow(FOLLOW_csPairSlotBQTerm_in_csBQTerm317);
                            	    csPairSlotBQTerm14=csPairSlotBQTerm();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_csPairSlotBQTerm.add(csPairSlotBQTerm14.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop4;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RBR15=(Token)match(input,RBR,FOLLOW_RBR_in_csBQTerm323); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBR.add(RBR15);

                    if ( state.backtracking==0 ) {
                      retval.marker = ((CustomToken)RBR15).getPayload(Integer.class);
                    }


                    // AST REWRITE
                    // elements: csPairSlotBQTerm, BQIDBR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 99:3: -> ^( Cst_BQRecordAppl BQIDBR ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:99:6: ^( Cst_BQRecordAppl BQIDBR ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQRecordAppl, "Cst_BQRecordAppl"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)BQIDBR11, (CommonToken)RBR15));
                        adaptor.addChild(root_1, stream_BQIDBR.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:100:14: ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstPairSlotBQTerm, "ConcCstPairSlotBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:100:38: ( csPairSlotBQTerm )*
                        while ( stream_csPairSlotBQTerm.hasNext() ) {
                            adaptor.addChild(root_2, stream_csPairSlotBQTerm.nextTree());

                        }
                        stream_csPairSlotBQTerm.reset();

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
    // $ANTLR end "csBQTerm"

    public static class csBQTermArgs_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csBQTermArgs"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:103:1: csBQTermArgs[ boolean compositeAllowed] : ( UNDERSCORE -> ^( Cst_BQDefault ) | IDSTAR -> ^( Cst_BQVarStar IDSTAR ^( Cst_TypeUnknown ) ) | ID {...}? => (c= csCompositePart )* -> {c==null}? ^( Cst_BQVar ID ^( Cst_TypeUnknown ) ) -> ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_BQVar ID ^( Cst_TypeUnknown ) ) ( csCompositePart )* ) ) | LPAR csBQTermArgs[true] RPAR {...}? => (c= csCompositePart )* -> {c==null}? csBQTermArgs -> ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_ITL LPAR ) csBQTermArgs ^( Cst_ITL RPAR ) ( csCompositePart )* ) ) | IDPAR ( csBQTermArgsComposite ( COMMA csBQTermArgsComposite )* )? RPAR -> ^( Cst_BQAppl IDPAR ^( ConcCstBQTerm ( csBQTermArgsComposite )* ) ) | IDBR ( csPairSlotBQTerm ( COMMA csPairSlotBQTerm )* )? RBR -> ^( Cst_BQRecordAppl IDBR ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* ) ) | csTL );
    public final BQTermParser.csBQTermArgs_return csBQTermArgs(boolean compositeAllowed) throws RecognitionException {
        BQTermParser.csBQTermArgs_return retval = new BQTermParser.csBQTermArgs_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token UNDERSCORE16=null;
        Token IDSTAR17=null;
        Token ID18=null;
        Token LPAR19=null;
        Token RPAR21=null;
        Token IDPAR22=null;
        Token COMMA24=null;
        Token RPAR26=null;
        Token IDBR27=null;
        Token COMMA29=null;
        Token RBR31=null;
        BQTermParser.csCompositePart_return c = null;

        BQTermParser.csBQTermArgs_return csBQTermArgs20 = null;

        BQTermParser.csBQTermArgsComposite_return csBQTermArgsComposite23 = null;

        BQTermParser.csBQTermArgsComposite_return csBQTermArgsComposite25 = null;

        BQTermParser.csPairSlotBQTerm_return csPairSlotBQTerm28 = null;

        BQTermParser.csPairSlotBQTerm_return csPairSlotBQTerm30 = null;

        BQTermParser.csTL_return csTL32 = null;


        Tree UNDERSCORE16_tree=null;
        Tree IDSTAR17_tree=null;
        Tree ID18_tree=null;
        Tree LPAR19_tree=null;
        Tree RPAR21_tree=null;
        Tree IDPAR22_tree=null;
        Tree COMMA24_tree=null;
        Tree RPAR26_tree=null;
        Tree IDBR27_tree=null;
        Tree COMMA29_tree=null;
        Tree RBR31_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_UNDERSCORE=new RewriteRuleTokenStream(adaptor,"token UNDERSCORE");
        RewriteRuleTokenStream stream_IDPAR=new RewriteRuleTokenStream(adaptor,"token IDPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RBR=new RewriteRuleTokenStream(adaptor,"token RBR");
        RewriteRuleTokenStream stream_IDBR=new RewriteRuleTokenStream(adaptor,"token IDBR");
        RewriteRuleTokenStream stream_IDSTAR=new RewriteRuleTokenStream(adaptor,"token IDSTAR");
        RewriteRuleSubtreeStream stream_csPairSlotBQTerm=new RewriteRuleSubtreeStream(adaptor,"rule csPairSlotBQTerm");
        RewriteRuleSubtreeStream stream_csBQTermArgsComposite=new RewriteRuleSubtreeStream(adaptor,"rule csBQTermArgsComposite");
        RewriteRuleSubtreeStream stream_csBQTermArgs=new RewriteRuleSubtreeStream(adaptor,"rule csBQTermArgs");
        RewriteRuleSubtreeStream stream_csCompositePart=new RewriteRuleSubtreeStream(adaptor,"rule csCompositePart");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:103:42: ( UNDERSCORE -> ^( Cst_BQDefault ) | IDSTAR -> ^( Cst_BQVarStar IDSTAR ^( Cst_TypeUnknown ) ) | ID {...}? => (c= csCompositePart )* -> {c==null}? ^( Cst_BQVar ID ^( Cst_TypeUnknown ) ) -> ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_BQVar ID ^( Cst_TypeUnknown ) ) ( csCompositePart )* ) ) | LPAR csBQTermArgs[true] RPAR {...}? => (c= csCompositePart )* -> {c==null}? csBQTermArgs -> ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_ITL LPAR ) csBQTermArgs ^( Cst_ITL RPAR ) ( csCompositePart )* ) ) | IDPAR ( csBQTermArgsComposite ( COMMA csBQTermArgsComposite )* )? RPAR -> ^( Cst_BQAppl IDPAR ^( ConcCstBQTerm ( csBQTermArgsComposite )* ) ) | IDBR ( csPairSlotBQTerm ( COMMA csPairSlotBQTerm )* )? RBR -> ^( Cst_BQRecordAppl IDBR ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* ) ) | csTL )
            int alt13=7;
            switch ( input.LA(1) ) {
            case UNDERSCORE:
                {
                alt13=1;
                }
                break;
            case IDSTAR:
                {
                alt13=2;
                }
                break;
            case ID:
                {
                alt13=3;
                }
                break;
            case LPAR:
                {
                alt13=4;
                }
                break;
            case IDPAR:
                {
                alt13=5;
                }
                break;
            case IDBR:
                {
                alt13=6;
                }
                break;
            case BQSTRING:
            case BQCHAR:
            case BQDOT:
            case NUM:
            case ANY:
                {
                alt13=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:104:3: UNDERSCORE
                    {
                    UNDERSCORE16=(Token)match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_csBQTermArgs370); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_UNDERSCORE.add(UNDERSCORE16);



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
                    // 104:14: -> ^( Cst_BQDefault )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:104:17: ^( Cst_BQDefault )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQDefault, "Cst_BQDefault"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)UNDERSCORE16));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:105:5: IDSTAR
                    {
                    IDSTAR17=(Token)match(input,IDSTAR,FOLLOW_IDSTAR_in_csBQTermArgs384); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDSTAR.add(IDSTAR17);



                    // AST REWRITE
                    // elements: IDSTAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 105:12: -> ^( Cst_BQVarStar IDSTAR ^( Cst_TypeUnknown ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:105:15: ^( Cst_BQVarStar IDSTAR ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVarStar, "Cst_BQVarStar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)IDSTAR17));
                        adaptor.addChild(root_1, stream_IDSTAR.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:105:77: ^( Cst_TypeUnknown )
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
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:106:5: ID {...}? => (c= csCompositePart )*
                    {
                    ID18=(Token)match(input,ID,FOLLOW_ID_in_csBQTermArgs405); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID18);

                    if ( !((compositeAllowed)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "csBQTermArgs", "$compositeAllowed");
                    }
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:106:32: (c= csCompositePart )*
                    loop7:
                    do {
                        int alt7=2;
                        alt7 = dfa7.predict(input);
                        switch (alt7) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:0:0: c= csCompositePart
                    	    {
                    	    pushFollow(FOLLOW_csCompositePart_in_csBQTermArgs412);
                    	    c=csCompositePart();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csCompositePart.add(c.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);



                    // AST REWRITE
                    // elements: csCompositePart, ID, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 107:5: -> {c==null}? ^( Cst_BQVar ID ^( Cst_TypeUnknown ) )
                    if (c==null) {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:107:19: ^( Cst_BQVar ID ^( Cst_TypeUnknown ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVar, "Cst_BQVar"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)ID18));
                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:108:12: ^( Cst_TypeUnknown )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 109:5: -> ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_BQVar ID ^( Cst_TypeUnknown ) ) ( csCompositePart )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:109:8: ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_BQVar ID ^( Cst_TypeUnknown ) ) ( csCompositePart )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQComposite, "Cst_BQComposite"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)ID18));
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:111:10: ^( ConcCstBQTerm ^( Cst_BQVar ID ^( Cst_TypeUnknown ) ) ( csCompositePart )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:112:12: ^( Cst_BQVar ID ^( Cst_TypeUnknown ) )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQVar, "Cst_BQVar"), root_3);

                        adaptor.addChild(root_3, extractOptions((CommonToken)ID18));
                        adaptor.addChild(root_3, stream_ID.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:113:17: ^( Cst_TypeUnknown )
                        {
                        Tree root_4 = (Tree)adaptor.nil();
                        root_4 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_TypeUnknown, "Cst_TypeUnknown"), root_4);

                        adaptor.addChild(root_3, root_4);
                        }

                        adaptor.addChild(root_2, root_3);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:113:38: ( csCompositePart )*
                        while ( stream_csCompositePart.hasNext() ) {
                            adaptor.addChild(root_2, stream_csCompositePart.nextTree());

                        }
                        stream_csCompositePart.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:116:5: LPAR csBQTermArgs[true] RPAR {...}? => (c= csCompositePart )*
                    {
                    LPAR19=(Token)match(input,LPAR,FOLLOW_LPAR_in_csBQTermArgs543); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR19);

                    pushFollow(FOLLOW_csBQTermArgs_in_csBQTermArgs545);
                    csBQTermArgs20=csBQTermArgs(true);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csBQTermArgs.add(csBQTermArgs20.getTree());
                    RPAR21=(Token)match(input,RPAR,FOLLOW_RPAR_in_csBQTermArgs548); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR21);

                    if ( !((compositeAllowed)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "csBQTermArgs", "$compositeAllowed");
                    }
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:116:58: (c= csCompositePart )*
                    loop8:
                    do {
                        int alt8=2;
                        alt8 = dfa8.predict(input);
                        switch (alt8) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:0:0: c= csCompositePart
                    	    {
                    	    pushFollow(FOLLOW_csCompositePart_in_csBQTermArgs555);
                    	    c=csCompositePart();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_csCompositePart.add(c.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);



                    // AST REWRITE
                    // elements: csBQTermArgs, RPAR, csBQTermArgs, csCompositePart, LPAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 117:5: -> {c==null}? csBQTermArgs
                    if (c==null) {
                        adaptor.addChild(root_0, stream_csBQTermArgs.nextTree());

                    }
                    else // 118:5: -> ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_ITL LPAR ) csBQTermArgs ^( Cst_ITL RPAR ) ( csCompositePart )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:118:8: ^( Cst_BQComposite ^( ConcCstBQTerm ^( Cst_ITL LPAR ) csBQTermArgs ^( Cst_ITL RPAR ) ( csCompositePart )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQComposite, "Cst_BQComposite"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)LPAR19, (CommonToken)RPAR21));
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:120:10: ^( ConcCstBQTerm ^( Cst_ITL LPAR ) csBQTermArgs ^( Cst_ITL RPAR ) ( csCompositePart )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:121:12: ^( Cst_ITL LPAR )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_3);

                        adaptor.addChild(root_3, extractOptions((CommonToken)LPAR19));
                        adaptor.addChild(root_3, stream_LPAR.nextNode());

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_csBQTermArgs.nextTree());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:123:12: ^( Cst_ITL RPAR )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_3);

                        adaptor.addChild(root_3, extractOptions((CommonToken)RPAR21));
                        adaptor.addChild(root_3, stream_RPAR.nextNode());

                        adaptor.addChild(root_2, root_3);
                        }
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:124:12: ( csCompositePart )*
                        while ( stream_csCompositePart.hasNext() ) {
                            adaptor.addChild(root_2, stream_csCompositePart.nextTree());

                        }
                        stream_csCompositePart.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:128:5: IDPAR ( csBQTermArgsComposite ( COMMA csBQTermArgsComposite )* )? RPAR
                    {
                    IDPAR22=(Token)match(input,IDPAR,FOLLOW_IDPAR_in_csBQTermArgs692); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDPAR.add(IDPAR22);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:128:11: ( csBQTermArgsComposite ( COMMA csBQTermArgsComposite )* )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==LPAR||(LA10_0>=IDPAR && LA10_0<=UNDERSCORE)||(LA10_0>=BQSTRING && LA10_0<=BQCHAR)||(LA10_0>=BQDOT && LA10_0<=NUM)||LA10_0==ANY) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:128:12: csBQTermArgsComposite ( COMMA csBQTermArgsComposite )*
                            {
                            pushFollow(FOLLOW_csBQTermArgsComposite_in_csBQTermArgs695);
                            csBQTermArgsComposite23=csBQTermArgsComposite();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_csBQTermArgsComposite.add(csBQTermArgsComposite23.getTree());
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:128:34: ( COMMA csBQTermArgsComposite )*
                            loop9:
                            do {
                                int alt9=2;
                                int LA9_0 = input.LA(1);

                                if ( (LA9_0==COMMA) ) {
                                    alt9=1;
                                }


                                switch (alt9) {
                            	case 1 :
                            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:128:35: COMMA csBQTermArgsComposite
                            	    {
                            	    COMMA24=(Token)match(input,COMMA,FOLLOW_COMMA_in_csBQTermArgs698); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA24);

                            	    pushFollow(FOLLOW_csBQTermArgsComposite_in_csBQTermArgs700);
                            	    csBQTermArgsComposite25=csBQTermArgsComposite();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_csBQTermArgsComposite.add(csBQTermArgsComposite25.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop9;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR26=(Token)match(input,RPAR,FOLLOW_RPAR_in_csBQTermArgs706); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR26);



                    // AST REWRITE
                    // elements: csBQTermArgsComposite, IDPAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 129:5: -> ^( Cst_BQAppl IDPAR ^( ConcCstBQTerm ( csBQTermArgsComposite )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:129:8: ^( Cst_BQAppl IDPAR ^( ConcCstBQTerm ( csBQTermArgsComposite )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQAppl, "Cst_BQAppl"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)IDPAR22, (CommonToken)RPAR26));
                        adaptor.addChild(root_1, stream_IDPAR.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:130:15: ^( ConcCstBQTerm ( csBQTermArgsComposite )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:130:31: ( csBQTermArgsComposite )*
                        while ( stream_csBQTermArgsComposite.hasNext() ) {
                            adaptor.addChild(root_2, stream_csBQTermArgsComposite.nextTree());

                        }
                        stream_csBQTermArgsComposite.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:132:5: IDBR ( csPairSlotBQTerm ( COMMA csPairSlotBQTerm )* )? RBR
                    {
                    IDBR27=(Token)match(input,IDBR,FOLLOW_IDBR_in_csBQTermArgs743); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDBR.add(IDBR27);

                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:132:10: ( csPairSlotBQTerm ( COMMA csPairSlotBQTerm )* )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==ID) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:132:11: csPairSlotBQTerm ( COMMA csPairSlotBQTerm )*
                            {
                            pushFollow(FOLLOW_csPairSlotBQTerm_in_csBQTermArgs746);
                            csPairSlotBQTerm28=csPairSlotBQTerm();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_csPairSlotBQTerm.add(csPairSlotBQTerm28.getTree());
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:132:28: ( COMMA csPairSlotBQTerm )*
                            loop11:
                            do {
                                int alt11=2;
                                int LA11_0 = input.LA(1);

                                if ( (LA11_0==COMMA) ) {
                                    alt11=1;
                                }


                                switch (alt11) {
                            	case 1 :
                            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:132:29: COMMA csPairSlotBQTerm
                            	    {
                            	    COMMA29=(Token)match(input,COMMA,FOLLOW_COMMA_in_csBQTermArgs749); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA29);

                            	    pushFollow(FOLLOW_csPairSlotBQTerm_in_csBQTermArgs751);
                            	    csPairSlotBQTerm30=csPairSlotBQTerm();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_csPairSlotBQTerm.add(csPairSlotBQTerm30.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop11;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RBR31=(Token)match(input,RBR,FOLLOW_RBR_in_csBQTermArgs757); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBR.add(RBR31);



                    // AST REWRITE
                    // elements: IDBR, csPairSlotBQTerm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 133:5: -> ^( Cst_BQRecordAppl IDBR ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:133:8: ^( Cst_BQRecordAppl IDBR ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQRecordAppl, "Cst_BQRecordAppl"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)IDBR27, (CommonToken)RBR31));
                        adaptor.addChild(root_1, stream_IDBR.nextNode());
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:134:14: ^( ConcCstPairSlotBQTerm ( csPairSlotBQTerm )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstPairSlotBQTerm, "ConcCstPairSlotBQTerm"), root_2);

                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:134:38: ( csPairSlotBQTerm )*
                        while ( stream_csPairSlotBQTerm.hasNext() ) {
                            adaptor.addChild(root_2, stream_csPairSlotBQTerm.nextTree());

                        }
                        stream_csPairSlotBQTerm.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 7 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:135:5: csTL
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_csTL_in_csBQTermArgs792);
                    csTL32=csTL();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, csTL32.getTree());

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
    // $ANTLR end "csBQTermArgs"

    public static class csBQTermArgsComposite_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csBQTermArgsComposite"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:138:1: csBQTermArgsComposite : csBQTermArgs[true] (csCP= csCompositePart )* -> {csCP==null}? csBQTermArgs -> ^( Cst_BQComposite ^( ConcCstBQTerm csBQTermArgs ( csCompositePart )* ) ) ;
    public final BQTermParser.csBQTermArgsComposite_return csBQTermArgsComposite() throws RecognitionException {
        BQTermParser.csBQTermArgsComposite_return retval = new BQTermParser.csBQTermArgsComposite_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        BQTermParser.csCompositePart_return csCP = null;

        BQTermParser.csBQTermArgs_return csBQTermArgs33 = null;


        RewriteRuleSubtreeStream stream_csBQTermArgs=new RewriteRuleSubtreeStream(adaptor,"rule csBQTermArgs");
        RewriteRuleSubtreeStream stream_csCompositePart=new RewriteRuleSubtreeStream(adaptor,"rule csCompositePart");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:138:23: ( csBQTermArgs[true] (csCP= csCompositePart )* -> {csCP==null}? csBQTermArgs -> ^( Cst_BQComposite ^( ConcCstBQTerm csBQTermArgs ( csCompositePart )* ) ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:139:3: csBQTermArgs[true] (csCP= csCompositePart )*
            {
            pushFollow(FOLLOW_csBQTermArgs_in_csBQTermArgsComposite805);
            csBQTermArgs33=csBQTermArgs(true);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csBQTermArgs.add(csBQTermArgs33.getTree());
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:139:26: (csCP= csCompositePart )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==LPAR||(LA14_0>=IDPAR && LA14_0<=UNDERSCORE)||LA14_0==EQUAL||(LA14_0>=BQSTRING && LA14_0<=BQCHAR)||(LA14_0>=BQDOT && LA14_0<=NUM)||LA14_0==ANY) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:0:0: csCP= csCompositePart
            	    {
            	    pushFollow(FOLLOW_csCompositePart_in_csBQTermArgsComposite810);
            	    csCP=csCompositePart();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_csCompositePart.add(csCP.getTree());

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);



            // AST REWRITE
            // elements: csBQTermArgs, csCompositePart, csBQTermArgs
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 140:3: -> {csCP==null}? csBQTermArgs
            if (csCP==null) {
                adaptor.addChild(root_0, stream_csBQTermArgs.nextTree());

            }
            else // 141:3: -> ^( Cst_BQComposite ^( ConcCstBQTerm csBQTermArgs ( csCompositePart )* ) )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:141:6: ^( Cst_BQComposite ^( ConcCstBQTerm csBQTermArgs ( csCompositePart )* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_BQComposite, "Cst_BQComposite"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)(csBQTermArgs33!=null?((Token)csBQTermArgs33.start):null), (CommonToken)(csBQTermArgs33!=null?((Token)csBQTermArgs33.stop):null)));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:143:8: ^( ConcCstBQTerm csBQTermArgs ( csCompositePart )* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcCstBQTerm, "ConcCstBQTerm"), root_2);

                adaptor.addChild(root_2, stream_csBQTermArgs.nextTree());
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:143:37: ( csCompositePart )*
                while ( stream_csCompositePart.hasNext() ) {
                    adaptor.addChild(root_2, stream_csCompositePart.nextTree());

                }
                stream_csCompositePart.reset();

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
    // $ANTLR end "csBQTermArgsComposite"

    public static class csPairSlotBQTerm_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csPairSlotBQTerm"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:147:1: csPairSlotBQTerm : ID EQUAL csBQTermArgs[false] -> ^( Cst_PairSlotBQTerm ^( Cst_Name ID ) csBQTermArgs ) ;
    public final BQTermParser.csPairSlotBQTerm_return csPairSlotBQTerm() throws RecognitionException {
        BQTermParser.csPairSlotBQTerm_return retval = new BQTermParser.csPairSlotBQTerm_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID34=null;
        Token EQUAL35=null;
        BQTermParser.csBQTermArgs_return csBQTermArgs36 = null;


        Tree ID34_tree=null;
        Tree EQUAL35_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_EQUAL=new RewriteRuleTokenStream(adaptor,"token EQUAL");
        RewriteRuleSubtreeStream stream_csBQTermArgs=new RewriteRuleSubtreeStream(adaptor,"rule csBQTermArgs");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:147:18: ( ID EQUAL csBQTermArgs[false] -> ^( Cst_PairSlotBQTerm ^( Cst_Name ID ) csBQTermArgs ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:148:3: ID EQUAL csBQTermArgs[false]
            {
            ID34=(Token)match(input,ID,FOLLOW_ID_in_csPairSlotBQTerm872); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID34);

            EQUAL35=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_csPairSlotBQTerm874); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EQUAL.add(EQUAL35);

            pushFollow(FOLLOW_csBQTermArgs_in_csPairSlotBQTerm876);
            csBQTermArgs36=csBQTermArgs(false);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_csBQTermArgs.add(csBQTermArgs36.getTree());


            // AST REWRITE
            // elements: ID, csBQTermArgs
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 149:3: -> ^( Cst_PairSlotBQTerm ^( Cst_Name ID ) csBQTermArgs )
            {
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:149:6: ^( Cst_PairSlotBQTerm ^( Cst_Name ID ) csBQTermArgs )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_PairSlotBQTerm, "Cst_PairSlotBQTerm"), root_1);

                adaptor.addChild(root_1, extractOptions((CommonToken)ID34));
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:149:62: ^( Cst_Name ID )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_Name, "Cst_Name"), root_2);

                adaptor.addChild(root_2, stream_ID.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_csBQTermArgs.nextTree());

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
    // $ANTLR end "csPairSlotBQTerm"

    public static class csCompositePart_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csCompositePart"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:152:1: csCompositePart : ( csTL | EQUAL -> ^( Cst_ITL EQUAL ) | csBQTermArgs[true] -> csBQTermArgs );
    public final BQTermParser.csCompositePart_return csCompositePart() throws RecognitionException {
        BQTermParser.csCompositePart_return retval = new BQTermParser.csCompositePart_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token EQUAL38=null;
        BQTermParser.csTL_return csTL37 = null;

        BQTermParser.csBQTermArgs_return csBQTermArgs39 = null;


        Tree EQUAL38_tree=null;
        RewriteRuleTokenStream stream_EQUAL=new RewriteRuleTokenStream(adaptor,"token EQUAL");
        RewriteRuleSubtreeStream stream_csBQTermArgs=new RewriteRuleSubtreeStream(adaptor,"rule csBQTermArgs");
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:152:17: ( csTL | EQUAL -> ^( Cst_ITL EQUAL ) | csBQTermArgs[true] -> csBQTermArgs )
            int alt15=3;
            switch ( input.LA(1) ) {
            case BQSTRING:
                {
                int LA15_1 = input.LA(2);

                if ( (synpred23_BQTerm()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }
                }
                break;
            case NUM:
                {
                int LA15_2 = input.LA(2);

                if ( (synpred23_BQTerm()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 2, input);

                    throw nvae;
                }
                }
                break;
            case BQDOT:
                {
                int LA15_3 = input.LA(2);

                if ( (synpred23_BQTerm()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 3, input);

                    throw nvae;
                }
                }
                break;
            case BQCHAR:
                {
                int LA15_4 = input.LA(2);

                if ( (synpred23_BQTerm()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 4, input);

                    throw nvae;
                }
                }
                break;
            case ANY:
                {
                int LA15_5 = input.LA(2);

                if ( (synpred23_BQTerm()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 5, input);

                    throw nvae;
                }
                }
                break;
            case EQUAL:
                {
                alt15=2;
                }
                break;
            case LPAR:
            case IDPAR:
            case IDBR:
            case IDSTAR:
            case ID:
            case UNDERSCORE:
                {
                alt15=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }

            switch (alt15) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:153:4: csTL
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_csTL_in_csCompositePart909);
                    csTL37=csTL();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, csTL37.getTree());

                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:154:5: EQUAL
                    {
                    EQUAL38=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_csCompositePart915); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EQUAL.add(EQUAL38);



                    // AST REWRITE
                    // elements: EQUAL
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 154:11: -> ^( Cst_ITL EQUAL )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:154:14: ^( Cst_ITL EQUAL )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)EQUAL38));
                        adaptor.addChild(root_1, stream_EQUAL.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:155:5: csBQTermArgs[true]
                    {
                    pushFollow(FOLLOW_csBQTermArgs_in_csCompositePart931);
                    csBQTermArgs39=csBQTermArgs(true);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_csBQTermArgs.add(csBQTermArgs39.getTree());


                    // AST REWRITE
                    // elements: csBQTermArgs
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 155:24: -> csBQTermArgs
                    {
                        adaptor.addChild(root_0, stream_csBQTermArgs.nextTree());

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
    // $ANTLR end "csCompositePart"

    public static class csTL_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "csTL"
    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:158:1: csTL : ( BQSTRING -> ^( Cst_ITL BQSTRING ) | NUM -> ^( Cst_ITL NUM ) | BQDOT -> ^( Cst_ITL BQDOT ) | BQCHAR -> ^( Cst_ITL BQCHAR ) | ANY -> ^( Cst_ITL ANY ) );
    public final BQTermParser.csTL_return csTL() throws RecognitionException {
        BQTermParser.csTL_return retval = new BQTermParser.csTL_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token BQSTRING40=null;
        Token NUM41=null;
        Token BQDOT42=null;
        Token BQCHAR43=null;
        Token ANY44=null;

        Tree BQSTRING40_tree=null;
        Tree NUM41_tree=null;
        Tree BQDOT42_tree=null;
        Tree BQCHAR43_tree=null;
        Tree ANY44_tree=null;
        RewriteRuleTokenStream stream_ANY=new RewriteRuleTokenStream(adaptor,"token ANY");
        RewriteRuleTokenStream stream_BQSTRING=new RewriteRuleTokenStream(adaptor,"token BQSTRING");
        RewriteRuleTokenStream stream_BQDOT=new RewriteRuleTokenStream(adaptor,"token BQDOT");
        RewriteRuleTokenStream stream_BQCHAR=new RewriteRuleTokenStream(adaptor,"token BQCHAR");
        RewriteRuleTokenStream stream_NUM=new RewriteRuleTokenStream(adaptor,"token NUM");

        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:158:6: ( BQSTRING -> ^( Cst_ITL BQSTRING ) | NUM -> ^( Cst_ITL NUM ) | BQDOT -> ^( Cst_ITL BQDOT ) | BQCHAR -> ^( Cst_ITL BQCHAR ) | ANY -> ^( Cst_ITL ANY ) )
            int alt16=5;
            switch ( input.LA(1) ) {
            case BQSTRING:
                {
                alt16=1;
                }
                break;
            case NUM:
                {
                alt16=2;
                }
                break;
            case BQDOT:
                {
                alt16=3;
                }
                break;
            case BQCHAR:
                {
                alt16=4;
                }
                break;
            case ANY:
                {
                alt16=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:159:3: BQSTRING
                    {
                    BQSTRING40=(Token)match(input,BQSTRING,FOLLOW_BQSTRING_in_csTL949); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQSTRING.add(BQSTRING40);



                    // AST REWRITE
                    // elements: BQSTRING
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 159:12: -> ^( Cst_ITL BQSTRING )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:159:15: ^( Cst_ITL BQSTRING )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)BQSTRING40));
                        adaptor.addChild(root_1, stream_BQSTRING.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:160:5: NUM
                    {
                    NUM41=(Token)match(input,NUM,FOLLOW_NUM_in_csTL965); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUM.add(NUM41);



                    // AST REWRITE
                    // elements: NUM
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 160:9: -> ^( Cst_ITL NUM )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:160:12: ^( Cst_ITL NUM )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)NUM41));
                        adaptor.addChild(root_1, stream_NUM.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:161:5: BQDOT
                    {
                    BQDOT42=(Token)match(input,BQDOT,FOLLOW_BQDOT_in_csTL981); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQDOT.add(BQDOT42);



                    // AST REWRITE
                    // elements: BQDOT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 161:11: -> ^( Cst_ITL BQDOT )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:161:14: ^( Cst_ITL BQDOT )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)BQDOT42));
                        adaptor.addChild(root_1, stream_BQDOT.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:162:5: BQCHAR
                    {
                    BQCHAR43=(Token)match(input,BQCHAR,FOLLOW_BQCHAR_in_csTL997); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BQCHAR.add(BQCHAR43);



                    // AST REWRITE
                    // elements: BQCHAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 162:12: -> ^( Cst_ITL BQCHAR )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:162:15: ^( Cst_ITL BQCHAR )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)BQCHAR43));
                        adaptor.addChild(root_1, stream_BQCHAR.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:163:5: ANY
                    {
                    ANY44=(Token)match(input,ANY,FOLLOW_ANY_in_csTL1013); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ANY.add(ANY44);



                    // AST REWRITE
                    // elements: ANY
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 163:9: -> ^( Cst_ITL ANY )
                    {
                        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:163:12: ^( Cst_ITL ANY )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Cst_ITL, "Cst_ITL"), root_1);

                        adaptor.addChild(root_1, extractOptions((CommonToken)ANY44));
                        adaptor.addChild(root_1, stream_ANY.nextNode());

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
    // $ANTLR end "csTL"

    // $ANTLR start synpred12_BQTerm
    public final void synpred12_BQTerm_fragment() throws RecognitionException {   
        BQTermParser.csCompositePart_return c = null;


        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:106:32: (c= csCompositePart )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:106:32: c= csCompositePart
        {
        pushFollow(FOLLOW_csCompositePart_in_synpred12_BQTerm412);
        c=csCompositePart();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_BQTerm

    // $ANTLR start synpred14_BQTerm
    public final void synpred14_BQTerm_fragment() throws RecognitionException {   
        BQTermParser.csCompositePart_return c = null;


        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:116:58: (c= csCompositePart )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:116:58: c= csCompositePart
        {
        pushFollow(FOLLOW_csCompositePart_in_synpred14_BQTerm555);
        c=csCompositePart();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred14_BQTerm

    // $ANTLR start synpred23_BQTerm
    public final void synpred23_BQTerm_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:153:4: ( csTL )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTerm.g:153:4: csTL
        {
        pushFollow(FOLLOW_csTL_in_synpred23_BQTerm909);
        csTL();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred23_BQTerm

    // Delegated rules

    public final boolean synpred12_BQTerm() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_BQTerm_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred23_BQTerm() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred23_BQTerm_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred14_BQTerm() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred14_BQTerm_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA7 dfa7 = new DFA7(this);
    protected DFA8 dfa8 = new DFA8(this);
    static final String DFA7_eotS =
        "\22\uffff";
    static final String DFA7_eofS =
        "\1\1\21\uffff";
    static final String DFA7_minS =
        "\1\u016e\1\uffff\14\0\4\uffff";
    static final String DFA7_maxS =
        "\1\u0189\1\uffff\14\0\4\uffff";
    static final String DFA7_acceptS =
        "\1\uffff\1\2\17\uffff\1\1";
    static final String DFA7_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\4\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\13\5\uffff\1\14\1\15\1\11\1\12\1\10\3\1\1\7\4\uffff\1\2\1"+
            "\5\1\uffff\1\4\1\3\3\uffff\1\6",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
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
            "",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "()* loopback of 106:32: (c= csCompositePart )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA7_2 = input.LA(1);

                         
                        int index7_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA7_3 = input.LA(1);

                         
                        int index7_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA7_4 = input.LA(1);

                         
                        int index7_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA7_5 = input.LA(1);

                         
                        int index7_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA7_6 = input.LA(1);

                         
                        int index7_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA7_7 = input.LA(1);

                         
                        int index7_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA7_8 = input.LA(1);

                         
                        int index7_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA7_9 = input.LA(1);

                         
                        int index7_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_9);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA7_10 = input.LA(1);

                         
                        int index7_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_10);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA7_11 = input.LA(1);

                         
                        int index7_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_11);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA7_12 = input.LA(1);

                         
                        int index7_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_12);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA7_13 = input.LA(1);

                         
                        int index7_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_13);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 7, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA8_eotS =
        "\22\uffff";
    static final String DFA8_eofS =
        "\1\1\21\uffff";
    static final String DFA8_minS =
        "\1\u016e\1\uffff\14\0\4\uffff";
    static final String DFA8_maxS =
        "\1\u0189\1\uffff\14\0\4\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\2\17\uffff\1\1";
    static final String DFA8_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\4\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\13\5\uffff\1\14\1\15\1\11\1\12\1\10\3\1\1\7\4\uffff\1\2\1"+
            "\5\1\uffff\1\4\1\3\3\uffff\1\6",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
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
            "",
            "",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "()* loopback of 116:58: (c= csCompositePart )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA8_2 = input.LA(1);

                         
                        int index8_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA8_3 = input.LA(1);

                         
                        int index8_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA8_4 = input.LA(1);

                         
                        int index8_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA8_5 = input.LA(1);

                         
                        int index8_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA8_6 = input.LA(1);

                         
                        int index8_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA8_7 = input.LA(1);

                         
                        int index8_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA8_8 = input.LA(1);

                         
                        int index8_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA8_9 = input.LA(1);

                         
                        int index8_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_9);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA8_10 = input.LA(1);

                         
                        int index8_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_10);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA8_11 = input.LA(1);

                         
                        int index8_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_11);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA8_12 = input.LA(1);

                         
                        int index8_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_12);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA8_13 = input.LA(1);

                         
                        int index8_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTerm()) ) {s = 17;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_13);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 8, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_BQID_in_csBQTerm68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BQPAR_in_csBQTerm94 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x15F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csCompositePart_in_csBQTerm96 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x15F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_RPAR_in_csBQTerm99 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BQIDSTAR_in_csBQTerm232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BQIDPAR_in_csBQTerm259 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x15F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csBQTermArgsComposite_in_csBQTerm262 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csBQTerm265 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csBQTermArgsComposite_in_csBQTerm267 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_RPAR_in_csBQTerm273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BQIDBR_in_csBQTerm309 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0880000000000000L});
    public static final BitSet FOLLOW_csPairSlotBQTerm_in_csBQTerm312 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0A00000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csBQTerm315 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_csPairSlotBQTerm_in_csBQTerm317 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0A00000000000000L});
    public static final BitSet FOLLOW_RBR_in_csBQTerm323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_csBQTermArgs370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDSTAR_in_csBQTermArgs384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_csBQTermArgs405 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csCompositePart_in_csBQTermArgs412 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_LPAR_in_csBQTermArgs543 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csBQTermArgs_in_csBQTermArgs545 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_RPAR_in_csBQTermArgs548 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csCompositePart_in_csBQTermArgs555 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_IDPAR_in_csBQTermArgs692 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x15F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csBQTermArgsComposite_in_csBQTermArgs695 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csBQTermArgs698 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csBQTermArgsComposite_in_csBQTermArgs700 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_RPAR_in_csBQTermArgs706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDBR_in_csBQTermArgs743 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0880000000000000L});
    public static final BitSet FOLLOW_csPairSlotBQTerm_in_csBQTermArgs746 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0A00000000000000L});
    public static final BitSet FOLLOW_COMMA_in_csBQTermArgs749 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_csPairSlotBQTerm_in_csBQTermArgs751 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0A00000000000000L});
    public static final BitSet FOLLOW_RBR_in_csBQTermArgs757 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csTL_in_csBQTermArgs792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csBQTermArgs_in_csBQTermArgsComposite805 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csCompositePart_in_csBQTermArgsComposite810 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_ID_in_csPairSlotBQTerm872 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_EQUAL_in_csPairSlotBQTerm874 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x11F0400000000000L,0x0000000000000236L});
    public static final BitSet FOLLOW_csBQTermArgs_in_csPairSlotBQTerm876 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csTL_in_csCompositePart909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_in_csCompositePart915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csBQTermArgs_in_csCompositePart931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BQSTRING_in_csTL949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUM_in_csTL965 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BQDOT_in_csTL981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BQCHAR_in_csTL997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ANY_in_csTL1013 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csCompositePart_in_synpred12_BQTerm412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csCompositePart_in_synpred14_BQTerm555 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_csTL_in_synpred23_BQTerm909 = new BitSet(new long[]{0x0000000000000002L});

}