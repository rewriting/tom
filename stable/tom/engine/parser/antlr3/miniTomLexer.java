// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g 2016-03-01 15:43:28

package tom.engine.parser.antlr3;
import org.antlr.runtime.tree.Tree;

import static tom.engine.parser.antlr3.TreeFactory.*;
import static tom.engine.parser.antlr3.miniTomParser.*;
import tom.engine.parser.antlr3.streamanalysis.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class miniTomLexer extends Lexer {
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
    public static final int Cst_MakeEmptyArray=115;
    public static final int GetSlot=27;
    public static final int Tracelink=201;
    public static final int EmptySymbol=178;
    public static final int KEYWORD_GET_TAIL=404;
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
    public static final int UNDERSCORE=386;
    public static final int Cst_Name=122;
    public static final int ANTI=385;
    public static final int AndConstraint=258;
    public static final int NamedBlock=206;
    public static final int KEYWORD_MAKE_INSERT=407;
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
    public static final int TomInclude=271;
    public static final int Match=205;
    public static final int concTomVisit=167;
    public static final int Cst_MakeEmptyList=106;
    public static final int BQTermToExpression=47;
    public static final int ARROW=372;
    public static final int KEYWORD_GET_DEFAULT=401;
    public static final int GetHeadDecl=349;
    public static final int AbstractBlock=208;
    public static final int Position=151;
    public static final int LONG=391;
    public static final int Transformation=338;
    public static final int LessThan=37;
    public static final int Cst_Implement=105;
    public static final int Cst_TermVariable=73;
    public static final int CHAR=392;
    public static final int DIFFERENT=383;
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
    public static final int DOUBLE=393;
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
    public static final int Cst_Type=101;
    public static final int Cst_Appl=82;
    public static final int NumGreaterThan=268;
    public static final int BQVariableStar=300;
    public static final int IsSort=11;
    public static final int AssignArray=211;
    public static final int Cst_ConstraintAction=75;
    public static final int Unitary=310;
    public static final int BuildConstant=295;
    public static final int RBR=365;
    public static final int OrConstraint=257;
    public static final int AntiMatchConstraint=254;
    public static final int KEYWORD_GET_SIZE=409;
    public static final int LARROW=377;
    public static final int EqualBQTerm=29;
    public static final int VariableHeadList=284;
    public static final int ResolveInverseLinksDecl=336;
    public static final int Cst_PairPattern=69;
    public static final int UsedType=159;
    public static final int LBR=363;
    public static final int Begin=154;
    public static final int TomNameToOption=194;
    public static final int PositionName=144;
    public static final int RawAction=202;
    public static final int ResolveStratBlock=163;
    public static final int ImplicitXMLAttribut=185;
    public static final int Cst_TermAppl=71;
    public static final int WithSymbol=236;
    public static final int COMMA=373;
    public static final int EQUAL=397;
    public static final int Cst_BQTermToBlock=60;
    public static final int ImplicitXMLChild=184;
    public static final int ResolveIsFsymDecl=334;
    public static final int SubtypeDecl=235;
    public static final int Cst_BQRecordAppl=140;
    public static final int Cst_NumGreaterThan=127;
    public static final int DIGIT=416;
    public static final int ACMatchLoop=13;
    public static final int ExpressionToInstruction=219;
    public static final int Cst_EmptyName=121;
    public static final int concTomTerm=249;
    public static final int Negation=44;
    public static final int If=217;
    public static final int RecordAppl=247;
    public static final int Cst_Equals=103;
    public static final int Type=234;
    public static final int HOSTBLOCK=68;
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
    public static final int Cst_VariableStar=87;
    public static final int TestVar=240;
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
    public static final int DefinedSymbol=181;
    public static final int BQTermToCode=273;
    public static final int ElementaryTransformation=176;
    public static final int TrueTL=33;
    public static final int GreaterOrEqualThan=38;
    public static final int Cst_TheoryAU=56;
    public static final int ACSymbol=183;
    public static final int TypesToType=232;
    public static final int NumLessThan=270;
    public static final int Cst_ConstantInt=52;
    public static final int concTomType=229;
    public static final int Cst_TermVariableStar=72;
    public static final int Subterm=285;
    public static final int InstructionToCode=275;
    public static final int Name=145;
    public static final int BQUOTE=369;
    public static final int Nop=209;
    public static final int RPAR=367;
    public static final int EmptyListConstraint=252;
    public static final int EXTENDS=368;
    public static final int NumericConstraint=253;
    public static final int Cst_MetaQuoteConstruct=62;

      // === DEBUG ============//
      public String getClassDesc() {
        return "ANTLRParser";
         // actually this is Lexer but we are interested in
         // the way HostParser and ANTLR generated Parser
         // call each other. We don't need to make a
         // difference between ANTLR's Parser and Lexer
      }

      private boolean opensBlockLBR = false;
      
      private final TokenCustomizer tokenCustomizer = new TokenCustomizer();
     
      // add custom fields to ANTLR generated Tokens
      @Override 
      public void emit(Token t) {
        super.emit(tokenCustomizer.customize(t));
      }
      


    // delegates
    // delegators

    public miniTomLexer() {;} 
    public miniTomLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public miniTomLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g"; }

    // $ANTLR start "KEYWORD_IS_FSYM"
    public final void mKEYWORD_IS_FSYM() throws RecognitionException {
        try {
            int _type = KEYWORD_IS_FSYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:687:21: ( 'is_fsym' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:687:23: 'is_fsym'
            {
            match("is_fsym"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_IS_FSYM"

    // $ANTLR start "KEYWORD_GET_SLOT"
    public final void mKEYWORD_GET_SLOT() throws RecognitionException {
        try {
            int _type = KEYWORD_GET_SLOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:688:21: ( 'get_slot' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:688:23: 'get_slot'
            {
            match("get_slot"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_GET_SLOT"

    // $ANTLR start "KEYWORD_GET_DEFAULT"
    public final void mKEYWORD_GET_DEFAULT() throws RecognitionException {
        try {
            int _type = KEYWORD_GET_DEFAULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:689:21: ( 'get_default' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:689:23: 'get_default'
            {
            match("get_default"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_GET_DEFAULT"

    // $ANTLR start "KEYWORD_MAKE"
    public final void mKEYWORD_MAKE() throws RecognitionException {
        try {
            int _type = KEYWORD_MAKE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:690:21: ( 'make' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:690:23: 'make'
            {
            match("make"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_MAKE"

    // $ANTLR start "KEYWORD_GET_HEAD"
    public final void mKEYWORD_GET_HEAD() throws RecognitionException {
        try {
            int _type = KEYWORD_GET_HEAD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:691:21: ( 'get_head' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:691:23: 'get_head'
            {
            match("get_head"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_GET_HEAD"

    // $ANTLR start "KEYWORD_GET_TAIL"
    public final void mKEYWORD_GET_TAIL() throws RecognitionException {
        try {
            int _type = KEYWORD_GET_TAIL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:692:21: ( 'get_tail' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:692:23: 'get_tail'
            {
            match("get_tail"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_GET_TAIL"

    // $ANTLR start "KEYWORD_IS_EMPTY"
    public final void mKEYWORD_IS_EMPTY() throws RecognitionException {
        try {
            int _type = KEYWORD_IS_EMPTY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:693:21: ( 'is_empty' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:693:23: 'is_empty'
            {
            match("is_empty"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_IS_EMPTY"

    // $ANTLR start "KEYWORD_MAKE_EMPTY"
    public final void mKEYWORD_MAKE_EMPTY() throws RecognitionException {
        try {
            int _type = KEYWORD_MAKE_EMPTY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:694:21: ( 'make_empty' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:694:23: 'make_empty'
            {
            match("make_empty"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_MAKE_EMPTY"

    // $ANTLR start "KEYWORD_MAKE_INSERT"
    public final void mKEYWORD_MAKE_INSERT() throws RecognitionException {
        try {
            int _type = KEYWORD_MAKE_INSERT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:695:21: ( 'make_insert' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:695:23: 'make_insert'
            {
            match("make_insert"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_MAKE_INSERT"

    // $ANTLR start "KEYWORD_GET_ELEMENT"
    public final void mKEYWORD_GET_ELEMENT() throws RecognitionException {
        try {
            int _type = KEYWORD_GET_ELEMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:696:21: ( 'get_element' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:696:23: 'get_element'
            {
            match("get_element"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_GET_ELEMENT"

    // $ANTLR start "KEYWORD_GET_SIZE"
    public final void mKEYWORD_GET_SIZE() throws RecognitionException {
        try {
            int _type = KEYWORD_GET_SIZE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:697:21: ( 'get_size' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:697:23: 'get_size'
            {
            match("get_size"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_GET_SIZE"

    // $ANTLR start "KEYWORD_MAKE_APPEND"
    public final void mKEYWORD_MAKE_APPEND() throws RecognitionException {
        try {
            int _type = KEYWORD_MAKE_APPEND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:698:21: ( 'make_append' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:698:23: 'make_append'
            {
            match("make_append"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_MAKE_APPEND"

    // $ANTLR start "KEYWORD_IMPLEMENT"
    public final void mKEYWORD_IMPLEMENT() throws RecognitionException {
        try {
            int _type = KEYWORD_IMPLEMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:699:21: ( 'implement' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:699:23: 'implement'
            {
            match("implement"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_IMPLEMENT"

    // $ANTLR start "KEYWORD_IS_SORT"
    public final void mKEYWORD_IS_SORT() throws RecognitionException {
        try {
            int _type = KEYWORD_IS_SORT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:700:21: ( 'is_sort' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:700:23: 'is_sort'
            {
            match("is_sort"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_IS_SORT"

    // $ANTLR start "KEYWORD_EQUALS"
    public final void mKEYWORD_EQUALS() throws RecognitionException {
        try {
            int _type = KEYWORD_EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:701:20: ( 'equals' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:701:22: 'equals'
            {
            match("equals"); 

             opensBlockLBR = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_EQUALS"

    // $ANTLR start "ARROW"
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:703:7: ( '->' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:703:9: '->'
            {
            match("->"); 

             opensBlockLBR = true;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ARROW"

    // $ANTLR start "LBR"
    public final void mLBR() throws RecognitionException {
        try {
            int _type = LBR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:705:9: ( '{' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:705:12: '{'
            {
            match('{'); 

              if(opensBlockLBR) {
                 opensBlockLBR = false;

                HostParser parser = new HostParser(
                	null, null, new NegativeImbricationDetector('{', '}', 0));

                  // XXX DEBUG ===
                  //if(HostParserDebugger.isOn()) {
                  //  HostParserDebugger.getInstance()
                  //  .debugNewCall(parser.getClassDesc(), input, "");
                  //}
                  // === DEBUG ===

                Tree tree = parser.parseBlockList(input);

                  // XXX DEBUG ===
                  //if(HostParserDebugger.isOn()) {
                  //  HostParserDebugger.getInstance()
                  //  .debugReturnedCall(parser.getClassDesc(), input, "");
                 // }
                  // === DEBUG ===
                  
                tokenCustomizer.prepareNextToken(tree);
              }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBR"

    // $ANTLR start "RBR"
    public final void mRBR() throws RecognitionException {
        try {
            int _type = RBR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:734:5: ( '}' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:734:7: '}'
            {
            match('}'); 

            // every token generated by antlr pass throw our
            // tokenCustomizer.customize(Token). Next emitted
            // token, this RBR token, will be "customized" with
            // with input.mark() returned value as payload
            tokenCustomizer.prepareNextToken(input.mark());


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBR"

    // $ANTLR start "EXTENDS"
    public final void mEXTENDS() throws RecognitionException {
        try {
            int _type = EXTENDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:745:9: ( 'extends' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:745:11: 'extends'
            {
            match("extends"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXTENDS"

    // $ANTLR start "VISIT"
    public final void mVISIT() throws RecognitionException {
        try {
            int _type = VISIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:746:9: ( 'visit' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:746:11: 'visit'
            {
            match("visit"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VISIT"

    // $ANTLR start "LARROW"
    public final void mLARROW() throws RecognitionException {
        try {
            int _type = LARROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:748:8: ( '<<' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:748:10: '<<'
            {
            match("<<"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LARROW"

    // $ANTLR start "GREATEROREQU"
    public final void mGREATEROREQU() throws RecognitionException {
        try {
            int _type = GREATEROREQU;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:749:14: ( '>=' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:749:16: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GREATEROREQU"

    // $ANTLR start "LOWEROREQU"
    public final void mLOWEROREQU() throws RecognitionException {
        try {
            int _type = LOWEROREQU;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:750:12: ( '<=' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:750:14: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LOWEROREQU"

    // $ANTLR start "GREATERTHAN"
    public final void mGREATERTHAN() throws RecognitionException {
        try {
            int _type = GREATERTHAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:751:13: ( '>' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:751:15: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GREATERTHAN"

    // $ANTLR start "LOWERTHAN"
    public final void mLOWERTHAN() throws RecognitionException {
        try {
            int _type = LOWERTHAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:752:11: ( '<' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:752:13: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LOWERTHAN"

    // $ANTLR start "DOUBLEEQUAL"
    public final void mDOUBLEEQUAL() throws RecognitionException {
        try {
            int _type = DOUBLEEQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:753:13: ( '==' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:753:15: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLEEQUAL"

    // $ANTLR start "DIFFERENT"
    public final void mDIFFERENT() throws RecognitionException {
        try {
            int _type = DIFFERENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:754:11: ( '!=' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:754:13: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIFFERENT"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:756:5: ( '&&' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:756:7: '&&'
            {
            match("&&"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:757:4: ( '||' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:757:6: '||'
            {
            match("||"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "PIPE"
    public final void mPIPE() throws RecognitionException {
        try {
            int _type = PIPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:758:7: ( '|' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:758:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PIPE"

    // $ANTLR start "QMARK"
    public final void mQMARK() throws RecognitionException {
        try {
            int _type = QMARK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:759:7: ( '?' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:759:8: '?'
            {
            match('?'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QMARK"

    // $ANTLR start "DQMARK"
    public final void mDQMARK() throws RecognitionException {
        try {
            int _type = DQMARK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:760:8: ( '??' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:760:9: '??'
            {
            match("??"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DQMARK"

    // $ANTLR start "EQUAL"
    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:761:7: ( '=' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:761:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUAL"

    // $ANTLR start "LSQUAREBR"
    public final void mLSQUAREBR() throws RecognitionException {
        try {
            int _type = LSQUAREBR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:762:11: ( '[' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:762:13: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LSQUAREBR"

    // $ANTLR start "RSQUAREBR"
    public final void mRSQUAREBR() throws RecognitionException {
        try {
            int _type = RSQUAREBR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:763:11: ( ']' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:763:13: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RSQUAREBR"

    // $ANTLR start "RPAR"
    public final void mRPAR() throws RecognitionException {
        try {
            int _type = RPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:764:7: ( ')' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:764:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RPAR"

    // $ANTLR start "LPAR"
    public final void mLPAR() throws RecognitionException {
        try {
            int _type = LPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:765:6: ( '(' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:765:8: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LPAR"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:766:7: ( ',' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:766:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "STAR"
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:767:6: ( '*' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:767:8: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STAR"

    // $ANTLR start "UNDERSCORE"
    public final void mUNDERSCORE() throws RecognitionException {
        try {
            int _type = UNDERSCORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:768:11: ( '_' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:768:12: '_'
            {
            match('_'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNDERSCORE"

    // $ANTLR start "AT"
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:769:4: ( '@' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:769:6: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AT"

    // $ANTLR start "ANTI"
    public final void mANTI() throws RecognitionException {
        try {
            int _type = ANTI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:770:6: ( '!' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:770:8: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANTI"

    // $ANTLR start "DQUOTE"
    public final void mDQUOTE() throws RecognitionException {
        try {
            int _type = DQUOTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:771:9: ( '\"' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:771:11: '\"'
            {
            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DQUOTE"

    // $ANTLR start "SQUOTE"
    public final void mSQUOTE() throws RecognitionException {
        try {
            int _type = SQUOTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:772:9: ( '\\'' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:772:11: '\\''
            {
            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SQUOTE"

    // $ANTLR start "BQUOTE"
    public final void mBQUOTE() throws RecognitionException {
        try {
            int _type = BQUOTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:773:10: ( '`' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:773:12: '`'
            {
            match('`'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BQUOTE"

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:774:9: ( ':' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:774:11: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COLON"

    // $ANTLR start "IDENTIFIER"
    public final void mIDENTIFIER() throws RecognitionException {
        try {
            int _type = IDENTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:776:13: ( ( '_' )? LETTER ( LETTER | DIGIT | '_' | '.' )* )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:776:15: ( '_' )? LETTER ( LETTER | DIGIT | '_' | '.' )*
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:776:15: ( '_' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='_') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:776:16: '_'
                    {
                    match('_'); 

                    }
                    break;

            }

            mLETTER(); 
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:776:29: ( LETTER | DIGIT | '_' | '.' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='.'||(LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:
            	    {
            	    if ( input.LA(1)=='.'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENTIFIER"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:779:7: ( '-' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:779:9: '-'
            {
            match('-'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:781:9: ( ( MINUS )? ( DIGIT )+ )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:781:11: ( MINUS )? ( DIGIT )+
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:781:11: ( MINUS )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:781:12: MINUS
                    {
                    mMINUS(); 

                    }
                    break;

            }

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:781:20: ( DIGIT )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:781:21: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "UNSIGNED_DOUBLE"
    public final void mUNSIGNED_DOUBLE() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:783:17: ( ( DIGIT )+ '.' ( DIGIT )* | '.' ( DIGIT )+ )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                alt8=1;
            }
            else if ( (LA8_0=='.') ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:783:19: ( DIGIT )+ '.' ( DIGIT )*
                    {
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:783:19: ( DIGIT )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:783:20: DIGIT
                    	    {
                    	    mDIGIT(); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);

                    match('.'); 
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:783:30: ( DIGIT )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:783:31: DIGIT
                    	    {
                    	    mDIGIT(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:783:41: '.' ( DIGIT )+
                    {
                    match('.'); 
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:783:45: ( DIGIT )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:783:46: DIGIT
                    	    {
                    	    mDIGIT(); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "UNSIGNED_DOUBLE"

    // $ANTLR start "DOUBLE"
    public final void mDOUBLE() throws RecognitionException {
        try {
            int _type = DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:784:9: ( ( MINUS )? UNSIGNED_DOUBLE )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:784:11: ( MINUS )? UNSIGNED_DOUBLE
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:784:11: ( MINUS )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='-') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:784:12: MINUS
                    {
                    mMINUS(); 

                    }
                    break;

            }

            mUNSIGNED_DOUBLE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLE"

    // $ANTLR start "LONG"
    public final void mLONG() throws RecognitionException {
        try {
            int _type = LONG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:785:9: ( ( MINUS )? ( DIGIT )+ LONG_SUFFIX )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:785:11: ( MINUS )? ( DIGIT )+ LONG_SUFFIX
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:785:11: ( MINUS )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='-') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:785:12: MINUS
                    {
                    mMINUS(); 

                    }
                    break;

            }

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:785:20: ( DIGIT )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>='0' && LA11_0<='9')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:785:21: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);

            mLONG_SUFFIX(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LONG"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:813:11: ( ( '0' .. '9' | 'A' .. 'F' | 'a' .. 'f' ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:813:13: ( '0' .. '9' | 'A' .. 'F' | 'a' .. 'f' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "LONG_SUFFIX"
    public final void mLONG_SUFFIX() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:819:13: ( 'l' | 'L' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LONG_SUFFIX"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:821:9: ( '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:821:11: '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); 
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:821:15: ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )*
            loop12:
            do {
                int alt12=3;
                int LA12_0 = input.LA(1);

                if ( (LA12_0=='\\') ) {
                    alt12=1;
                }
                else if ( ((LA12_0>='\u0000' && LA12_0<='\t')||(LA12_0>='\u000B' && LA12_0<='\f')||(LA12_0>='\u000E' && LA12_0<='!')||(LA12_0>='#' && LA12_0<='[')||(LA12_0>=']' && LA12_0<='\uFFFF')) ) {
                    alt12=2;
                }


                switch (alt12) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:821:16: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:821:20: ~ ( '\"' | '\\\\' | '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "CHAR"
    public final void mCHAR() throws RecognitionException {
        try {
            int _type = CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:822:9: ( '\\'' ( ESC | ~ ( '\\'' | '\\n' | '\\r' | '\\\\' ) )+ '\\'' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:822:11: '\\'' ( ESC | ~ ( '\\'' | '\\n' | '\\r' | '\\\\' ) )+ '\\''
            {
            match('\''); 
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:822:16: ( ESC | ~ ( '\\'' | '\\n' | '\\r' | '\\\\' ) )+
            int cnt13=0;
            loop13:
            do {
                int alt13=3;
                int LA13_0 = input.LA(1);

                if ( (LA13_0=='\\') ) {
                    alt13=1;
                }
                else if ( ((LA13_0>='\u0000' && LA13_0<='\t')||(LA13_0>='\u000B' && LA13_0<='\f')||(LA13_0>='\u000E' && LA13_0<='&')||(LA13_0>='(' && LA13_0<='[')||(LA13_0>=']' && LA13_0<='\uFFFF')) ) {
                    alt13=2;
                }


                switch (alt13) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:822:18: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:822:24: ~ ( '\\'' | '\\n' | '\\r' | '\\\\' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt13 >= 1 ) break loop13;
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
            } while (true);

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CHAR"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:825:8: ( 'A' .. 'Z' | 'a' .. 'z' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:827:7: ( '0' .. '9' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:827:9: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:829:4: ( ( '\\r' | '\\n' | '\\t' | ' ' )* )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:829:6: ( '\\r' | '\\n' | '\\t' | ' ' )*
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:829:6: ( '\\r' | '\\n' | '\\t' | ' ' )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>='\t' && LA14_0<='\n')||LA14_0=='\r'||LA14_0==' ') ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

             _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "SL_COMMENT"
    public final void mSL_COMMENT() throws RecognitionException {
        try {
            int _type = SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:831:12: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:831:14: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:831:19: (~ ( '\\n' | '\\r' ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>='\u0000' && LA15_0<='\t')||(LA15_0>='\u000B' && LA15_0<='\f')||(LA15_0>='\u000E' && LA15_0<='\uFFFF')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:831:20: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:831:35: ( '\\n' | '\\r' ( '\\n' )? )?
            int alt17=3;
            int LA17_0 = input.LA(1);

            if ( (LA17_0=='\n') ) {
                alt17=1;
            }
            else if ( (LA17_0=='\r') ) {
                alt17=2;
            }
            switch (alt17) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:831:36: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:831:41: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:831:45: ( '\\n' )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0=='\n') ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:831:46: '\\n'
                            {
                            match('\n'); 

                            }
                            break;

                    }


                    }
                    break;

            }

             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SL_COMMENT"

    // $ANTLR start "ML_COMMENT"
    public final void mML_COMMENT() throws RecognitionException {
        try {
            int _type = ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:832:12: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:832:14: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:832:19: ( options {greedy=false; } : . )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0=='*') ) {
                    int LA18_1 = input.LA(2);

                    if ( (LA18_1=='/') ) {
                        alt18=2;
                    }
                    else if ( ((LA18_1>='\u0000' && LA18_1<='.')||(LA18_1>='0' && LA18_1<='\uFFFF')) ) {
                        alt18=1;
                    }


                }
                else if ( ((LA18_0>='\u0000' && LA18_0<=')')||(LA18_0>='+' && LA18_0<='\uFFFF')) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:832:47: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            match("*/"); 

             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ML_COMMENT"

    // $ANTLR start "DEFAULT"
    public final void mDEFAULT() throws RecognitionException {
        try {
            int _type = DEFAULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:836:9: ( . )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:836:11: .
            {
            matchAny(); 
             _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEFAULT"

    // $ANTLR start "ESC"
    public final void mESC() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:840:3: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' | ( 'u' )+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT | '0' .. '3' ( '0' .. '7' ( '0' .. '7' )? )? | '4' .. '7' ( '0' .. '7' )? ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:840:5: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' | ( 'u' )+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT | '0' .. '3' ( '0' .. '7' ( '0' .. '7' )? )? | '4' .. '7' ( '0' .. '7' )? )
            {
            match('\\'); 
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:841:5: ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' | ( 'u' )+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT | '0' .. '3' ( '0' .. '7' ( '0' .. '7' )? )? | '4' .. '7' ( '0' .. '7' )? )
            int alt23=11;
            switch ( input.LA(1) ) {
            case 'n':
                {
                alt23=1;
                }
                break;
            case 'r':
                {
                alt23=2;
                }
                break;
            case 't':
                {
                alt23=3;
                }
                break;
            case 'b':
                {
                alt23=4;
                }
                break;
            case 'f':
                {
                alt23=5;
                }
                break;
            case '\"':
                {
                alt23=6;
                }
                break;
            case '\'':
                {
                alt23=7;
                }
                break;
            case '\\':
                {
                alt23=8;
                }
                break;
            case 'u':
                {
                alt23=9;
                }
                break;
            case '0':
            case '1':
            case '2':
            case '3':
                {
                alt23=10;
                }
                break;
            case '4':
            case '5':
            case '6':
            case '7':
                {
                alt23=11;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:841:7: 'n'
                    {
                    match('n'); 

                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:842:7: 'r'
                    {
                    match('r'); 

                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:843:7: 't'
                    {
                    match('t'); 

                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:844:7: 'b'
                    {
                    match('b'); 

                    }
                    break;
                case 5 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:845:7: 'f'
                    {
                    match('f'); 

                    }
                    break;
                case 6 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:846:7: '\"'
                    {
                    match('\"'); 

                    }
                    break;
                case 7 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:847:7: '\\''
                    {
                    match('\''); 

                    }
                    break;
                case 8 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:848:7: '\\\\'
                    {
                    match('\\'); 

                    }
                    break;
                case 9 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:849:7: ( 'u' )+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
                    {
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:849:7: ( 'u' )+
                    int cnt19=0;
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( (LA19_0=='u') ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:849:8: 'u'
                    	    {
                    	    match('u'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt19 >= 1 ) break loop19;
                                EarlyExitException eee =
                                    new EarlyExitException(19, input);
                                throw eee;
                        }
                        cnt19++;
                    } while (true);

                    mHEX_DIGIT(); 
                    mHEX_DIGIT(); 
                    mHEX_DIGIT(); 
                    mHEX_DIGIT(); 

                    }
                    break;
                case 10 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:850:7: '0' .. '3' ( '0' .. '7' ( '0' .. '7' )? )?
                    {
                    matchRange('0','3'); 
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:851:7: ( '0' .. '7' ( '0' .. '7' )? )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( ((LA21_0>='0' && LA21_0<='7')) ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:852:9: '0' .. '7' ( '0' .. '7' )?
                            {
                            matchRange('0','7'); 
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:853:9: ( '0' .. '7' )?
                            int alt20=2;
                            int LA20_0 = input.LA(1);

                            if ( ((LA20_0>='0' && LA20_0<='7')) ) {
                                alt20=1;
                            }
                            switch (alt20) {
                                case 1 :
                                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:854:11: '0' .. '7'
                                    {
                                    matchRange('0','7'); 

                                    }
                                    break;

                            }


                            }
                            break;

                    }


                    }
                    break;
                case 11 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:857:7: '4' .. '7' ( '0' .. '7' )?
                    {
                    matchRange('4','7'); 
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:858:7: ( '0' .. '7' )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( ((LA22_0>='0' && LA22_0<='7')) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:859:7: '0' .. '7'
                            {
                            matchRange('0','7'); 

                            }
                            break;

                    }


                    }
                    break;

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "ESC"

    public void mTokens() throws RecognitionException {
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:8: ( KEYWORD_IS_FSYM | KEYWORD_GET_SLOT | KEYWORD_GET_DEFAULT | KEYWORD_MAKE | KEYWORD_GET_HEAD | KEYWORD_GET_TAIL | KEYWORD_IS_EMPTY | KEYWORD_MAKE_EMPTY | KEYWORD_MAKE_INSERT | KEYWORD_GET_ELEMENT | KEYWORD_GET_SIZE | KEYWORD_MAKE_APPEND | KEYWORD_IMPLEMENT | KEYWORD_IS_SORT | KEYWORD_EQUALS | ARROW | LBR | RBR | EXTENDS | VISIT | LARROW | GREATEROREQU | LOWEROREQU | GREATERTHAN | LOWERTHAN | DOUBLEEQUAL | DIFFERENT | AND | OR | PIPE | QMARK | DQMARK | EQUAL | LSQUAREBR | RSQUAREBR | RPAR | LPAR | COMMA | STAR | UNDERSCORE | AT | ANTI | DQUOTE | SQUOTE | BQUOTE | COLON | IDENTIFIER | INTEGER | DOUBLE | LONG | STRING | CHAR | WS | SL_COMMENT | ML_COMMENT | DEFAULT )
        int alt24=56;
        alt24 = dfa24.predict(input);
        switch (alt24) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:10: KEYWORD_IS_FSYM
                {
                mKEYWORD_IS_FSYM(); 

                }
                break;
            case 2 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:26: KEYWORD_GET_SLOT
                {
                mKEYWORD_GET_SLOT(); 

                }
                break;
            case 3 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:43: KEYWORD_GET_DEFAULT
                {
                mKEYWORD_GET_DEFAULT(); 

                }
                break;
            case 4 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:63: KEYWORD_MAKE
                {
                mKEYWORD_MAKE(); 

                }
                break;
            case 5 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:76: KEYWORD_GET_HEAD
                {
                mKEYWORD_GET_HEAD(); 

                }
                break;
            case 6 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:93: KEYWORD_GET_TAIL
                {
                mKEYWORD_GET_TAIL(); 

                }
                break;
            case 7 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:110: KEYWORD_IS_EMPTY
                {
                mKEYWORD_IS_EMPTY(); 

                }
                break;
            case 8 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:127: KEYWORD_MAKE_EMPTY
                {
                mKEYWORD_MAKE_EMPTY(); 

                }
                break;
            case 9 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:146: KEYWORD_MAKE_INSERT
                {
                mKEYWORD_MAKE_INSERT(); 

                }
                break;
            case 10 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:166: KEYWORD_GET_ELEMENT
                {
                mKEYWORD_GET_ELEMENT(); 

                }
                break;
            case 11 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:186: KEYWORD_GET_SIZE
                {
                mKEYWORD_GET_SIZE(); 

                }
                break;
            case 12 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:203: KEYWORD_MAKE_APPEND
                {
                mKEYWORD_MAKE_APPEND(); 

                }
                break;
            case 13 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:223: KEYWORD_IMPLEMENT
                {
                mKEYWORD_IMPLEMENT(); 

                }
                break;
            case 14 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:241: KEYWORD_IS_SORT
                {
                mKEYWORD_IS_SORT(); 

                }
                break;
            case 15 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:257: KEYWORD_EQUALS
                {
                mKEYWORD_EQUALS(); 

                }
                break;
            case 16 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:272: ARROW
                {
                mARROW(); 

                }
                break;
            case 17 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:278: LBR
                {
                mLBR(); 

                }
                break;
            case 18 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:282: RBR
                {
                mRBR(); 

                }
                break;
            case 19 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:286: EXTENDS
                {
                mEXTENDS(); 

                }
                break;
            case 20 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:294: VISIT
                {
                mVISIT(); 

                }
                break;
            case 21 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:300: LARROW
                {
                mLARROW(); 

                }
                break;
            case 22 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:307: GREATEROREQU
                {
                mGREATEROREQU(); 

                }
                break;
            case 23 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:320: LOWEROREQU
                {
                mLOWEROREQU(); 

                }
                break;
            case 24 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:331: GREATERTHAN
                {
                mGREATERTHAN(); 

                }
                break;
            case 25 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:343: LOWERTHAN
                {
                mLOWERTHAN(); 

                }
                break;
            case 26 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:353: DOUBLEEQUAL
                {
                mDOUBLEEQUAL(); 

                }
                break;
            case 27 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:365: DIFFERENT
                {
                mDIFFERENT(); 

                }
                break;
            case 28 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:375: AND
                {
                mAND(); 

                }
                break;
            case 29 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:379: OR
                {
                mOR(); 

                }
                break;
            case 30 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:382: PIPE
                {
                mPIPE(); 

                }
                break;
            case 31 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:387: QMARK
                {
                mQMARK(); 

                }
                break;
            case 32 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:393: DQMARK
                {
                mDQMARK(); 

                }
                break;
            case 33 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:400: EQUAL
                {
                mEQUAL(); 

                }
                break;
            case 34 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:406: LSQUAREBR
                {
                mLSQUAREBR(); 

                }
                break;
            case 35 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:416: RSQUAREBR
                {
                mRSQUAREBR(); 

                }
                break;
            case 36 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:426: RPAR
                {
                mRPAR(); 

                }
                break;
            case 37 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:431: LPAR
                {
                mLPAR(); 

                }
                break;
            case 38 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:436: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 39 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:442: STAR
                {
                mSTAR(); 

                }
                break;
            case 40 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:447: UNDERSCORE
                {
                mUNDERSCORE(); 

                }
                break;
            case 41 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:458: AT
                {
                mAT(); 

                }
                break;
            case 42 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:461: ANTI
                {
                mANTI(); 

                }
                break;
            case 43 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:466: DQUOTE
                {
                mDQUOTE(); 

                }
                break;
            case 44 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:473: SQUOTE
                {
                mSQUOTE(); 

                }
                break;
            case 45 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:480: BQUOTE
                {
                mBQUOTE(); 

                }
                break;
            case 46 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:487: COLON
                {
                mCOLON(); 

                }
                break;
            case 47 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:493: IDENTIFIER
                {
                mIDENTIFIER(); 

                }
                break;
            case 48 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:504: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 49 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:512: DOUBLE
                {
                mDOUBLE(); 

                }
                break;
            case 50 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:519: LONG
                {
                mLONG(); 

                }
                break;
            case 51 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:524: STRING
                {
                mSTRING(); 

                }
                break;
            case 52 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:531: CHAR
                {
                mCHAR(); 

                }
                break;
            case 53 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:536: WS
                {
                mWS(); 

                }
                break;
            case 54 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:539: SL_COMMENT
                {
                mSL_COMMENT(); 

                }
                break;
            case 55 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:550: ML_COMMENT
                {
                mML_COMMENT(); 

                }
                break;
            case 56 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/miniTom.g:1:561: DEFAULT
                {
                mDEFAULT(); 

                }
                break;

        }

    }


    protected DFA24 dfa24 = new DFA24(this);
    static final String DFA24_eotS =
        "\1\40\4\45\1\42\2\uffff\1\45\1\62\1\64\1\66\1\70\1\42\1\73\1\75"+
        "\6\uffff\1\104\1\uffff\1\106\1\111\3\uffff\1\114\1\42\2\uffff\1"+
        "\42\1\uffff\2\45\1\uffff\4\45\1\uffff\1\114\3\uffff\1\45\40\uffff"+
        "\14\45\1\152\15\45\1\uffff\2\45\1\175\15\45\1\u008b\1\45\1\uffff"+
        "\1\u008d\1\45\1\u008f\12\45\1\uffff\1\u009a\1\uffff\1\u009b\1\uffff"+
        "\1\45\1\u009d\1\u009e\1\45\1\u00a0\1\u00a1\4\45\2\uffff\1\u00a6"+
        "\2\uffff\1\45\2\uffff\4\45\1\uffff\2\45\1\u00ae\2\45\1\u00b1\1\u00b2"+
        "\1\uffff\1\u00b3\1\u00b4\4\uffff";
    static final String DFA24_eofS =
        "\u00b5\uffff";
    static final String DFA24_minS =
        "\1\0\1\155\1\145\1\141\1\161\1\56\2\uffff\1\151\1\74\3\75\1\46\1"+
        "\174\1\77\6\uffff\1\101\1\uffff\2\0\3\uffff\1\56\1\60\2\uffff\1"+
        "\52\1\uffff\1\137\1\160\1\uffff\1\164\1\153\1\165\1\164\1\uffff"+
        "\1\56\3\uffff\1\163\40\uffff\1\145\1\154\1\137\1\145\1\141\1\145"+
        "\1\151\1\163\1\155\1\157\1\145\1\144\1\56\1\154\1\156\1\164\1\171"+
        "\1\160\1\162\1\155\1\151\2\145\1\141\1\154\1\141\1\uffff\1\163\1"+
        "\144\1\56\1\155\2\164\1\145\1\157\1\172\1\146\1\141\1\151\1\145"+
        "\1\155\1\156\1\160\1\56\1\163\1\uffff\1\56\1\171\1\56\1\156\1\164"+
        "\1\145\1\141\1\144\1\154\1\155\1\160\1\163\1\160\1\uffff\1\56\1"+
        "\uffff\1\56\1\uffff\1\164\2\56\1\165\2\56\1\145\1\164\2\145\2\uffff"+
        "\1\56\2\uffff\1\154\2\uffff\1\156\1\171\1\162\1\156\1\uffff\2\164"+
        "\1\56\1\164\1\144\2\56\1\uffff\2\56\4\uffff";
    static final String DFA24_maxS =
        "\1\uffff\1\163\1\145\1\141\1\170\1\76\2\uffff\1\151\4\75\1\46\1"+
        "\174\1\77\6\uffff\1\172\1\uffff\2\uffff\3\uffff\1\154\1\71\2\uffff"+
        "\1\57\1\uffff\1\137\1\160\1\uffff\1\164\1\153\1\165\1\164\1\uffff"+
        "\1\154\3\uffff\1\163\40\uffff\1\163\1\154\1\137\1\145\1\141\1\145"+
        "\1\151\1\163\1\155\1\157\1\145\1\164\1\172\1\154\1\156\1\164\1\171"+
        "\1\160\1\162\1\155\1\154\2\145\1\141\1\154\1\151\1\uffff\1\163\1"+
        "\144\1\172\1\155\2\164\1\145\1\157\1\172\1\146\1\141\1\151\1\145"+
        "\1\155\1\156\1\160\1\172\1\163\1\uffff\1\172\1\171\1\172\1\156\1"+
        "\164\1\145\1\141\1\144\1\154\1\155\1\160\1\163\1\160\1\uffff\1\172"+
        "\1\uffff\1\172\1\uffff\1\164\2\172\1\165\2\172\1\145\1\164\2\145"+
        "\2\uffff\1\172\2\uffff\1\154\2\uffff\1\156\1\171\1\162\1\156\1\uffff"+
        "\2\164\1\172\1\164\1\144\2\172\1\uffff\2\172\4\uffff";
    static final String DFA24_acceptS =
        "\6\uffff\1\21\1\22\10\uffff\1\42\1\43\1\44\1\45\1\46\1\47\1\uffff"+
        "\1\51\2\uffff\1\55\1\56\1\57\2\uffff\2\65\1\uffff\1\70\2\uffff\1"+
        "\57\4\uffff\1\20\1\uffff\1\61\1\21\1\22\1\uffff\1\25\1\27\1\31\1"+
        "\26\1\30\1\32\1\41\1\33\1\52\1\34\1\35\1\36\1\40\1\37\1\42\1\43"+
        "\1\44\1\45\1\46\1\47\1\50\1\51\1\53\1\63\1\64\1\54\1\55\1\56\1\60"+
        "\1\62\1\66\1\67\32\uffff\1\4\22\uffff\1\24\15\uffff\1\17\1\uffff"+
        "\1\1\1\uffff\1\16\12\uffff\1\23\1\7\1\uffff\1\2\1\13\1\uffff\1\5"+
        "\1\6\4\uffff\1\15\7\uffff\1\10\2\uffff\1\3\1\12\1\11\1\14";
    static final String DFA24_specialS =
        "\1\0\27\uffff\1\2\1\1\u009b\uffff}>";
    static final String[] DFA24_transitionS = {
            "\11\42\2\37\2\42\1\37\22\42\1\37\1\14\1\30\3\42\1\15\1\31\1"+
            "\23\1\22\1\25\1\42\1\24\1\5\1\36\1\41\12\35\1\33\1\42\1\11\1"+
            "\13\1\12\1\17\1\27\32\34\1\20\1\42\1\21\1\42\1\26\1\32\4\34"+
            "\1\4\1\34\1\2\1\34\1\1\3\34\1\3\10\34\1\10\4\34\1\6\1\16\1\7"+
            "\uff82\42",
            "\1\44\5\uffff\1\43",
            "\1\46",
            "\1\47",
            "\1\50\6\uffff\1\51",
            "\1\54\1\uffff\12\53\4\uffff\1\52",
            "",
            "",
            "\1\57",
            "\1\60\1\61",
            "\1\63",
            "\1\65",
            "\1\67",
            "\1\71",
            "\1\72",
            "\1\74",
            "",
            "",
            "",
            "",
            "",
            "",
            "\32\45\6\uffff\32\45",
            "",
            "\12\107\1\uffff\2\107\1\uffff\ufff2\107",
            "\12\110\1\uffff\2\110\1\uffff\31\110\1\uffff\uffd8\110",
            "",
            "",
            "",
            "\1\54\1\uffff\12\53\22\uffff\1\115\37\uffff\1\115",
            "\12\54",
            "",
            "",
            "\1\117\4\uffff\1\116",
            "",
            "\1\120",
            "\1\121",
            "",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "",
            "\1\54\1\uffff\12\53\22\uffff\1\115\37\uffff\1\115",
            "",
            "",
            "",
            "\1\126",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\130\1\127\14\uffff\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "\1\145\1\150\2\uffff\1\146\12\uffff\1\144\1\147",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\151\1\uffff\32"+
            "\45",
            "\1\153",
            "\1\154",
            "\1\155",
            "\1\156",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\163\2\uffff\1\162",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\172\3\uffff\1\170\3\uffff\1\171",
            "",
            "\1\173",
            "\1\174",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\1\u008a",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u008c",
            "",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u008e",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "\1\u009c",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u009f",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "",
            "",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "",
            "\1\u00a7",
            "",
            "",
            "\1\u00a8",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "",
            "\1\u00ac",
            "\1\u00ad",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00af",
            "\1\u00b0",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\45\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA24_eot = DFA.unpackEncodedString(DFA24_eotS);
    static final short[] DFA24_eof = DFA.unpackEncodedString(DFA24_eofS);
    static final char[] DFA24_min = DFA.unpackEncodedStringToUnsignedChars(DFA24_minS);
    static final char[] DFA24_max = DFA.unpackEncodedStringToUnsignedChars(DFA24_maxS);
    static final short[] DFA24_accept = DFA.unpackEncodedString(DFA24_acceptS);
    static final short[] DFA24_special = DFA.unpackEncodedString(DFA24_specialS);
    static final short[][] DFA24_transition;

    static {
        int numStates = DFA24_transitionS.length;
        DFA24_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA24_transition[i] = DFA.unpackEncodedString(DFA24_transitionS[i]);
        }
    }

    class DFA24 extends DFA {

        public DFA24(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 24;
            this.eot = DFA24_eot;
            this.eof = DFA24_eof;
            this.min = DFA24_min;
            this.max = DFA24_max;
            this.accept = DFA24_accept;
            this.special = DFA24_special;
            this.transition = DFA24_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( KEYWORD_IS_FSYM | KEYWORD_GET_SLOT | KEYWORD_GET_DEFAULT | KEYWORD_MAKE | KEYWORD_GET_HEAD | KEYWORD_GET_TAIL | KEYWORD_IS_EMPTY | KEYWORD_MAKE_EMPTY | KEYWORD_MAKE_INSERT | KEYWORD_GET_ELEMENT | KEYWORD_GET_SIZE | KEYWORD_MAKE_APPEND | KEYWORD_IMPLEMENT | KEYWORD_IS_SORT | KEYWORD_EQUALS | ARROW | LBR | RBR | EXTENDS | VISIT | LARROW | GREATEROREQU | LOWEROREQU | GREATERTHAN | LOWERTHAN | DOUBLEEQUAL | DIFFERENT | AND | OR | PIPE | QMARK | DQMARK | EQUAL | LSQUAREBR | RSQUAREBR | RPAR | LPAR | COMMA | STAR | UNDERSCORE | AT | ANTI | DQUOTE | SQUOTE | BQUOTE | COLON | IDENTIFIER | INTEGER | DOUBLE | LONG | STRING | CHAR | WS | SL_COMMENT | ML_COMMENT | DEFAULT );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA24_0 = input.LA(1);

                        s = -1;
                        if ( (LA24_0=='i') ) {s = 1;}

                        else if ( (LA24_0=='g') ) {s = 2;}

                        else if ( (LA24_0=='m') ) {s = 3;}

                        else if ( (LA24_0=='e') ) {s = 4;}

                        else if ( (LA24_0=='-') ) {s = 5;}

                        else if ( (LA24_0=='{') ) {s = 6;}

                        else if ( (LA24_0=='}') ) {s = 7;}

                        else if ( (LA24_0=='v') ) {s = 8;}

                        else if ( (LA24_0=='<') ) {s = 9;}

                        else if ( (LA24_0=='>') ) {s = 10;}

                        else if ( (LA24_0=='=') ) {s = 11;}

                        else if ( (LA24_0=='!') ) {s = 12;}

                        else if ( (LA24_0=='&') ) {s = 13;}

                        else if ( (LA24_0=='|') ) {s = 14;}

                        else if ( (LA24_0=='?') ) {s = 15;}

                        else if ( (LA24_0=='[') ) {s = 16;}

                        else if ( (LA24_0==']') ) {s = 17;}

                        else if ( (LA24_0==')') ) {s = 18;}

                        else if ( (LA24_0=='(') ) {s = 19;}

                        else if ( (LA24_0==',') ) {s = 20;}

                        else if ( (LA24_0=='*') ) {s = 21;}

                        else if ( (LA24_0=='_') ) {s = 22;}

                        else if ( (LA24_0=='@') ) {s = 23;}

                        else if ( (LA24_0=='\"') ) {s = 24;}

                        else if ( (LA24_0=='\'') ) {s = 25;}

                        else if ( (LA24_0=='`') ) {s = 26;}

                        else if ( (LA24_0==':') ) {s = 27;}

                        else if ( ((LA24_0>='A' && LA24_0<='Z')||(LA24_0>='a' && LA24_0<='d')||LA24_0=='f'||LA24_0=='h'||(LA24_0>='j' && LA24_0<='l')||(LA24_0>='n' && LA24_0<='u')||(LA24_0>='w' && LA24_0<='z')) ) {s = 28;}

                        else if ( ((LA24_0>='0' && LA24_0<='9')) ) {s = 29;}

                        else if ( (LA24_0=='.') ) {s = 30;}

                        else if ( ((LA24_0>='\t' && LA24_0<='\n')||LA24_0=='\r'||LA24_0==' ') ) {s = 31;}

                        else if ( (LA24_0=='/') ) {s = 33;}

                        else if ( ((LA24_0>='\u0000' && LA24_0<='\b')||(LA24_0>='\u000B' && LA24_0<='\f')||(LA24_0>='\u000E' && LA24_0<='\u001F')||(LA24_0>='#' && LA24_0<='%')||LA24_0=='+'||LA24_0==';'||LA24_0=='\\'||LA24_0=='^'||(LA24_0>='~' && LA24_0<='\uFFFF')) ) {s = 34;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA24_25 = input.LA(1);

                        s = -1;
                        if ( ((LA24_25>='\u0000' && LA24_25<='\t')||(LA24_25>='\u000B' && LA24_25<='\f')||(LA24_25>='\u000E' && LA24_25<='&')||(LA24_25>='(' && LA24_25<='\uFFFF')) ) {s = 72;}

                        else s = 73;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA24_24 = input.LA(1);

                        s = -1;
                        if ( ((LA24_24>='\u0000' && LA24_24<='\t')||(LA24_24>='\u000B' && LA24_24<='\f')||(LA24_24>='\u000E' && LA24_24<='\uFFFF')) ) {s = 71;}

                        else s = 70;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 24, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}