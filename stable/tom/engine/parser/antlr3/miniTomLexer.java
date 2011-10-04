// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g 2011-10-04 18:06:53

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
    public static final int KEYWORD_GET_TAIL=378;
    public static final int EmptySymbol=171;
    public static final int GetDefaultDecl=328;
    public static final int Cst_Symbol=53;
    public static final int BQRecordAppl=287;
    public static final int Automata=227;
    public static final int Conditional=34;
    public static final int Cst_OriginTracking=76;
    public static final int Cst_NumGreaterOrEqualTo=126;
    public static final int ACSymbolDecl=305;
    public static final int Cst_UnamedVariableStar=83;
    public static final int FalseTL=32;
    public static final int UNDERSCORE=361;
    public static final int ANTI=360;
    public static final int Cst_Name=122;
    public static final int AndConstraint=244;
    public static final int KEYWORD_MAKE_INSERT=381;
    public static final int NamedBlock=196;
    public static final int CompiledPattern=193;
    public static final int Strategy=315;
    public static final int Cst_TypetermConstruct=63;
    public static final int LOWERTHAN=354;
    public static final int MakeAddArray=317;
    public static final int SL_COMMENT=400;
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
    public static final int KEYWORD_GET_DEFAULT=376;
    public static final int ARROW=347;
    public static final int GetHeadDecl=325;
    public static final int AbstractBlock=198;
    public static final int LONG=366;
    public static final int Position=151;
    public static final int LessThan=37;
    public static final int Cst_Implement=105;
    public static final int Cst_TermVariable=73;
    public static final int CHAR=367;
    public static final int DIFFERENT=357;
    public static final int concCode=292;
    public static final int Cst_GetHead=117;
    public static final int Cst_GetSlot=111;
    public static final int Cst_BQConstant=134;
    public static final int Cst_Anti=90;
    public static final int noTL=263;
    public static final int FalseConstraint=247;
    public static final int Entry=167;
    public static final int Let=204;
    public static final int LPAR=341;
    public static final int Cst_ITL=135;
    public static final int DOUBLE=368;
    public static final int BQTermToInstruction=210;
    public static final int NodeString=78;
    public static final int Cst_OrConstraint=130;
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
    public static final int Cst_Appl=82;
    public static final int Cst_Type=100;
    public static final int NumGreaterThan=254;
    public static final int BQVariableStar=285;
    public static final int IsSort=11;
    public static final int AssignArray=201;
    public static final int Cst_ConstraintAction=75;
    public static final int Unitary=295;
    public static final int BuildConstant=281;
    public static final int RBR=340;
    public static final int OrConstraint=243;
    public static final int KEYWORD_GET_SIZE=383;
    public static final int AntiMatchConstraint=240;
    public static final int LARROW=358;
    public static final int EqualBQTerm=29;
    public static final int VariableHeadList=270;
    public static final int Cst_PairPattern=69;
    public static final int UsedType=159;
    public static final int LBR=338;
    public static final int Begin=154;
    public static final int TomNameToOption=187;
    public static final int PositionName=144;
    public static final int RawAction=192;
    public static final int ImplicitXMLAttribut=178;
    public static final int Cst_TermAppl=71;
    public static final int WithSymbol=222;
    public static final int COMMA=348;
    public static final int EQUAL=372;
    public static final int ImplicitXMLChild=177;
    public static final int Cst_BQTermToBlock=60;
    public static final int SubtypeDecl=221;
    public static final int Cst_BQRecordAppl=140;
    public static final int Cst_NumGreaterThan=127;
    public static final int DIGIT=391;
    public static final int ACMatchLoop=13;
    public static final int ExpressionToInstruction=209;
    public static final int Cst_EmptyName=121;
    public static final int concTomTerm=235;
    public static final int Negation=44;
    public static final int If=207;
    public static final int RecordAppl=233;
    public static final int Cst_Equals=103;
    public static final int Type=220;
    public static final int HOSTBLOCK=68;
    public static final int Cst_ConstantStar=85;
    public static final int DEFAULT=402;
    public static final int IsEmptyList=23;
    public static final int MakeEmptyArray=318;
    public static final int AliasTo=250;
    public static final int BQAppl=288;
    public static final int BuildConsList=278;
    public static final int Cst_NumLessOrEqualTo=128;
    public static final int Cst_Slot=58;
    public static final int ConcCstBQTerm=91;
    public static final int Cst_Constant=86;
    public static final int OriginalText=183;
    public static final int DeclarationToCode=260;
    public static final int Cst_OpConstruct=66;
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
    public static final int GreaterOrEqualThan=38;
    public static final int Cst_TheoryAU=56;
    public static final int ACSymbol=176;
    public static final int TypesToType=218;
    public static final int NumLessThan=256;
    public static final int Cst_ConstantInt=52;
    public static final int concTomType=215;
    public static final int Cst_TermVariableStar=72;
    public static final int Subterm=271;
    public static final int InstructionToCode=261;
    public static final int Name=145;
    public static final int BQUOTE=344;
    public static final int Nop=199;
    public static final int RPAR=342;
    public static final int EmptyListConstraint=238;
    public static final int EXTENDS=343;
    public static final int NumericConstraint=239;
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
    public String getGrammarFileName() { return "/Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g"; }

    // $ANTLR start "KEYWORD_IS_FSYM"
    public final void mKEYWORD_IS_FSYM() throws RecognitionException {
        try {
            int _type = KEYWORD_IS_FSYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:732:21: ( 'is_fsym' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:732:23: 'is_fsym'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:733:21: ( 'get_slot' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:733:23: 'get_slot'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:734:21: ( 'get_default' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:734:23: 'get_default'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:735:21: ( 'make' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:735:23: 'make'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:736:21: ( 'get_head' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:736:23: 'get_head'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:737:21: ( 'get_tail' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:737:23: 'get_tail'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:738:21: ( 'is_empty' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:738:23: 'is_empty'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:739:21: ( 'make_empty' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:739:23: 'make_empty'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:740:21: ( 'make_insert' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:740:23: 'make_insert'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:741:21: ( 'get_element' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:741:23: 'get_element'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:742:21: ( 'get_size' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:742:23: 'get_size'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:743:21: ( 'make_append' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:743:23: 'make_append'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:744:21: ( 'implement' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:744:23: 'implement'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:745:21: ( 'is_sort' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:745:23: 'is_sort'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:746:20: ( 'equals' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:746:22: 'equals'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:748:7: ( '->' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:748:9: '->'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:750:9: ( '{' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:750:12: '{'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:779:5: ( '}' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:779:7: '}'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:790:9: ( 'extends' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:790:11: 'extends'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:791:9: ( 'visit' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:791:11: 'visit'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:793:8: ( '<<' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:793:10: '<<'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:794:14: ( '>=' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:794:16: '>='
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:795:12: ( '<=' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:795:14: '<='
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:796:13: ( '>' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:796:15: '>'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:797:11: ( '<' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:797:13: '<'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:798:13: ( '==' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:798:15: '=='
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:799:11: ( '!=' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:799:13: '!='
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:801:5: ( '&&' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:801:7: '&&'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:802:4: ( '||' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:802:6: '||'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:803:7: ( '|' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:803:9: '|'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:804:7: ( '?' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:804:8: '?'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:805:8: ( '??' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:805:9: '??'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:806:7: ( '=' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:806:9: '='
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:807:11: ( '[' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:807:13: '['
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:808:11: ( ']' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:808:13: ']'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:809:7: ( ')' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:809:9: ')'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:810:6: ( '(' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:810:8: '('
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:811:7: ( ',' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:811:9: ','
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:812:6: ( '*' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:812:8: '*'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:813:11: ( '_' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:813:12: '_'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:814:4: ( '@' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:814:6: '@'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:815:6: ( '!' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:815:8: '!'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:816:9: ( '\"' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:816:11: '\"'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:817:9: ( '\\'' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:817:11: '\\''
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:818:10: ( '`' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:818:12: '`'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:819:9: ( ':' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:819:11: ':'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:821:13: ( ( '_' )? LETTER ( LETTER | DIGIT | '_' | '.' )* )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:821:15: ( '_' )? LETTER ( LETTER | DIGIT | '_' | '.' )*
            {
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:821:15: ( '_' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='_') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:821:16: '_'
                    {
                    match('_'); 

                    }
                    break;

            }

            mLETTER(); 
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:821:29: ( LETTER | DIGIT | '_' | '.' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='.'||(LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:824:7: ( '-' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:824:9: '-'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:826:9: ( ( MINUS )? ( DIGIT )+ )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:826:11: ( MINUS )? ( DIGIT )+
            {
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:826:11: ( MINUS )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:826:12: MINUS
                    {
                    mMINUS(); 

                    }
                    break;

            }

            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:826:20: ( DIGIT )+
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
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:826:21: DIGIT
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:828:17: ( ( DIGIT )+ '.' ( DIGIT )* | '.' ( DIGIT )+ )
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
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:828:19: ( DIGIT )+ '.' ( DIGIT )*
                    {
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:828:19: ( DIGIT )+
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
                    	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:828:20: DIGIT
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
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:828:30: ( DIGIT )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:828:31: DIGIT
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
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:828:41: '.' ( DIGIT )+
                    {
                    match('.'); 
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:828:45: ( DIGIT )+
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
                    	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:828:46: DIGIT
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:829:9: ( ( MINUS )? UNSIGNED_DOUBLE )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:829:11: ( MINUS )? UNSIGNED_DOUBLE
            {
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:829:11: ( MINUS )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='-') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:829:12: MINUS
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:830:9: ( ( MINUS )? ( DIGIT )+ LONG_SUFFIX )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:830:11: ( MINUS )? ( DIGIT )+ LONG_SUFFIX
            {
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:830:11: ( MINUS )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='-') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:830:12: MINUS
                    {
                    mMINUS(); 

                    }
                    break;

            }

            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:830:20: ( DIGIT )+
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
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:830:21: DIGIT
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

    // $ANTLR start "ID_MINUS"
    public final void mID_MINUS() throws RecognitionException {
        try {
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:850:9: ( IDENTIFIER MINUS ( 'a' .. 'z' | 'A' .. 'Z' | ( DIGIT )+ | UNSIGNED_DOUBLE ) ( MINUS ( 'a' .. 'z' | 'A' .. 'Z' ) | IDENTIFIER )* )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:851:3: IDENTIFIER MINUS ( 'a' .. 'z' | 'A' .. 'Z' | ( DIGIT )+ | UNSIGNED_DOUBLE ) ( MINUS ( 'a' .. 'z' | 'A' .. 'Z' ) | IDENTIFIER )*
            {
            mIDENTIFIER(); 
            mMINUS(); 
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:851:20: ( 'a' .. 'z' | 'A' .. 'Z' | ( DIGIT )+ | UNSIGNED_DOUBLE )
            int alt13=4;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:851:21: 'a' .. 'z'
                    {
                    matchRange('a','z'); 

                    }
                    break;
                case 2 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:851:30: 'A' .. 'Z'
                    {
                    matchRange('A','Z'); 

                    }
                    break;
                case 3 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:851:39: ( DIGIT )+
                    {
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:851:39: ( DIGIT )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0>='0' && LA12_0<='9')) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:851:40: DIGIT
                    	    {
                    	    mDIGIT(); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt12 >= 1 ) break loop12;
                                EarlyExitException eee =
                                    new EarlyExitException(12, input);
                                throw eee;
                        }
                        cnt12++;
                    } while (true);


                    }
                    break;
                case 4 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:851:48: UNSIGNED_DOUBLE
                    {
                    mUNSIGNED_DOUBLE(); 

                    }
                    break;

            }

            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:851:65: ( MINUS ( 'a' .. 'z' | 'A' .. 'Z' ) | IDENTIFIER )*
            loop14:
            do {
                int alt14=3;
                int LA14_0 = input.LA(1);

                if ( (LA14_0=='-') ) {
                    alt14=1;
                }
                else if ( ((LA14_0>='A' && LA14_0<='Z')||LA14_0=='_'||(LA14_0>='a' && LA14_0<='z')) ) {
                    alt14=2;
                }


                switch (alt14) {
            	case 1 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:852:9: MINUS ( 'a' .. 'z' | 'A' .. 'Z' )
            	    {
            	    mMINUS(); 
            	    if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;
            	case 2 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:853:9: IDENTIFIER
            	    {
            	    mIDENTIFIER(); 

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "ID_MINUS"

    // $ANTLR start "ALL_ID"
    public final void mALL_ID() throws RecognitionException {
        try {
            int _type = ALL_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:857:8: ( IDENTIFIER | ID_MINUS )
            int alt15=2;
            alt15 = dfa15.predict(input);
            switch (alt15) {
                case 1 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:857:10: IDENTIFIER
                    {
                    mIDENTIFIER(); 

                    }
                    break;
                case 2 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:857:23: ID_MINUS
                    {
                    mID_MINUS(); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALL_ID"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:860:11: ( ( '0' .. '9' | 'A' .. 'F' | 'a' .. 'f' ) )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:860:13: ( '0' .. '9' | 'A' .. 'F' | 'a' .. 'f' )
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:866:13: ( 'l' | 'L' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:870:9: ( '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:870:11: '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); 
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:870:15: ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )*
            loop16:
            do {
                int alt16=3;
                int LA16_0 = input.LA(1);

                if ( (LA16_0=='\\') ) {
                    alt16=1;
                }
                else if ( ((LA16_0>='\u0000' && LA16_0<='\t')||(LA16_0>='\u000B' && LA16_0<='\f')||(LA16_0>='\u000E' && LA16_0<='!')||(LA16_0>='#' && LA16_0<='[')||(LA16_0>=']' && LA16_0<='\uFFFF')) ) {
                    alt16=2;
                }


                switch (alt16) {
            	case 1 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:870:16: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:870:20: ~ ( '\"' | '\\\\' | '\\n' | '\\r' )
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
            	    break loop16;
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:871:9: ( '\\'' ( ESC | ~ ( '\\'' | '\\n' | '\\r' | '\\\\' ) )+ '\\'' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:871:11: '\\'' ( ESC | ~ ( '\\'' | '\\n' | '\\r' | '\\\\' ) )+ '\\''
            {
            match('\''); 
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:871:16: ( ESC | ~ ( '\\'' | '\\n' | '\\r' | '\\\\' ) )+
            int cnt17=0;
            loop17:
            do {
                int alt17=3;
                int LA17_0 = input.LA(1);

                if ( (LA17_0=='\\') ) {
                    alt17=1;
                }
                else if ( ((LA17_0>='\u0000' && LA17_0<='\t')||(LA17_0>='\u000B' && LA17_0<='\f')||(LA17_0>='\u000E' && LA17_0<='&')||(LA17_0>='(' && LA17_0<='[')||(LA17_0>=']' && LA17_0<='\uFFFF')) ) {
                    alt17=2;
                }


                switch (alt17) {
            	case 1 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:871:18: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:871:24: ~ ( '\\'' | '\\n' | '\\r' | '\\\\' )
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
            	    if ( cnt17 >= 1 ) break loop17;
                        EarlyExitException eee =
                            new EarlyExitException(17, input);
                        throw eee;
                }
                cnt17++;
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:874:8: ( 'A' .. 'Z' | 'a' .. 'z' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:876:7: ( '0' .. '9' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:876:9: '0' .. '9'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:878:4: ( ( '\\r' | '\\n' | '\\t' | ' ' )* )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:878:6: ( '\\r' | '\\n' | '\\t' | ' ' )*
            {
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:878:6: ( '\\r' | '\\n' | '\\t' | ' ' )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>='\t' && LA18_0<='\n')||LA18_0=='\r'||LA18_0==' ') ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:
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
            	    break loop18;
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:880:12: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:880:14: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:880:19: (~ ( '\\n' | '\\r' ) )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>='\u0000' && LA19_0<='\t')||(LA19_0>='\u000B' && LA19_0<='\f')||(LA19_0>='\u000E' && LA19_0<='\uFFFF')) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:880:20: ~ ( '\\n' | '\\r' )
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
            	    break loop19;
                }
            } while (true);

            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:880:35: ( '\\n' | '\\r' ( '\\n' )? )?
            int alt21=3;
            int LA21_0 = input.LA(1);

            if ( (LA21_0=='\n') ) {
                alt21=1;
            }
            else if ( (LA21_0=='\r') ) {
                alt21=2;
            }
            switch (alt21) {
                case 1 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:880:36: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:880:41: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:880:45: ( '\\n' )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0=='\n') ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:880:46: '\\n'
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:881:12: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:881:14: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:881:19: ( options {greedy=false; } : . )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0=='*') ) {
                    int LA22_1 = input.LA(2);

                    if ( (LA22_1=='/') ) {
                        alt22=2;
                    }
                    else if ( ((LA22_1>='\u0000' && LA22_1<='.')||(LA22_1>='0' && LA22_1<='\uFFFF')) ) {
                        alt22=1;
                    }


                }
                else if ( ((LA22_0>='\u0000' && LA22_0<=')')||(LA22_0>='+' && LA22_0<='\uFFFF')) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:881:47: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop22;
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:885:9: ( . )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:885:11: .
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
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:889:3: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' | ( 'u' )+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT | '0' .. '3' ( '0' .. '7' ( '0' .. '7' )? )? | '4' .. '7' ( '0' .. '7' )? ) )
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:889:5: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' | ( 'u' )+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT | '0' .. '3' ( '0' .. '7' ( '0' .. '7' )? )? | '4' .. '7' ( '0' .. '7' )? )
            {
            match('\\'); 
            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:890:5: ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' | ( 'u' )+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT | '0' .. '3' ( '0' .. '7' ( '0' .. '7' )? )? | '4' .. '7' ( '0' .. '7' )? )
            int alt27=11;
            switch ( input.LA(1) ) {
            case 'n':
                {
                alt27=1;
                }
                break;
            case 'r':
                {
                alt27=2;
                }
                break;
            case 't':
                {
                alt27=3;
                }
                break;
            case 'b':
                {
                alt27=4;
                }
                break;
            case 'f':
                {
                alt27=5;
                }
                break;
            case '\"':
                {
                alt27=6;
                }
                break;
            case '\'':
                {
                alt27=7;
                }
                break;
            case '\\':
                {
                alt27=8;
                }
                break;
            case 'u':
                {
                alt27=9;
                }
                break;
            case '0':
            case '1':
            case '2':
            case '3':
                {
                alt27=10;
                }
                break;
            case '4':
            case '5':
            case '6':
            case '7':
                {
                alt27=11;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:890:7: 'n'
                    {
                    match('n'); 

                    }
                    break;
                case 2 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:891:7: 'r'
                    {
                    match('r'); 

                    }
                    break;
                case 3 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:892:7: 't'
                    {
                    match('t'); 

                    }
                    break;
                case 4 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:893:7: 'b'
                    {
                    match('b'); 

                    }
                    break;
                case 5 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:894:7: 'f'
                    {
                    match('f'); 

                    }
                    break;
                case 6 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:895:7: '\"'
                    {
                    match('\"'); 

                    }
                    break;
                case 7 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:896:7: '\\''
                    {
                    match('\''); 

                    }
                    break;
                case 8 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:897:7: '\\\\'
                    {
                    match('\\'); 

                    }
                    break;
                case 9 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:898:7: ( 'u' )+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
                    {
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:898:7: ( 'u' )+
                    int cnt23=0;
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( (LA23_0=='u') ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:898:8: 'u'
                    	    {
                    	    match('u'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt23 >= 1 ) break loop23;
                                EarlyExitException eee =
                                    new EarlyExitException(23, input);
                                throw eee;
                        }
                        cnt23++;
                    } while (true);

                    mHEX_DIGIT(); 
                    mHEX_DIGIT(); 
                    mHEX_DIGIT(); 
                    mHEX_DIGIT(); 

                    }
                    break;
                case 10 :
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:899:7: '0' .. '3' ( '0' .. '7' ( '0' .. '7' )? )?
                    {
                    matchRange('0','3'); 
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:900:7: ( '0' .. '7' ( '0' .. '7' )? )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( ((LA25_0>='0' && LA25_0<='7')) ) {
                        alt25=1;
                    }
                    switch (alt25) {
                        case 1 :
                            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:901:9: '0' .. '7' ( '0' .. '7' )?
                            {
                            matchRange('0','7'); 
                            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:902:9: ( '0' .. '7' )?
                            int alt24=2;
                            int LA24_0 = input.LA(1);

                            if ( ((LA24_0>='0' && LA24_0<='7')) ) {
                                alt24=1;
                            }
                            switch (alt24) {
                                case 1 :
                                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:903:11: '0' .. '7'
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
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:906:7: '4' .. '7' ( '0' .. '7' )?
                    {
                    matchRange('4','7'); 
                    // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:907:7: ( '0' .. '7' )?
                    int alt26=2;
                    int LA26_0 = input.LA(1);

                    if ( ((LA26_0>='0' && LA26_0<='7')) ) {
                        alt26=1;
                    }
                    switch (alt26) {
                        case 1 :
                            // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:908:7: '0' .. '7'
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
        // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:8: ( KEYWORD_IS_FSYM | KEYWORD_GET_SLOT | KEYWORD_GET_DEFAULT | KEYWORD_MAKE | KEYWORD_GET_HEAD | KEYWORD_GET_TAIL | KEYWORD_IS_EMPTY | KEYWORD_MAKE_EMPTY | KEYWORD_MAKE_INSERT | KEYWORD_GET_ELEMENT | KEYWORD_GET_SIZE | KEYWORD_MAKE_APPEND | KEYWORD_IMPLEMENT | KEYWORD_IS_SORT | KEYWORD_EQUALS | ARROW | LBR | RBR | EXTENDS | VISIT | LARROW | GREATEROREQU | LOWEROREQU | GREATERTHAN | LOWERTHAN | DOUBLEEQUAL | DIFFERENT | AND | OR | PIPE | QMARK | DQMARK | EQUAL | LSQUAREBR | RSQUAREBR | RPAR | LPAR | COMMA | STAR | UNDERSCORE | AT | ANTI | DQUOTE | SQUOTE | BQUOTE | COLON | IDENTIFIER | INTEGER | DOUBLE | LONG | ALL_ID | STRING | CHAR | WS | SL_COMMENT | ML_COMMENT | DEFAULT )
        int alt28=57;
        alt28 = dfa28.predict(input);
        switch (alt28) {
            case 1 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:10: KEYWORD_IS_FSYM
                {
                mKEYWORD_IS_FSYM(); 

                }
                break;
            case 2 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:26: KEYWORD_GET_SLOT
                {
                mKEYWORD_GET_SLOT(); 

                }
                break;
            case 3 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:43: KEYWORD_GET_DEFAULT
                {
                mKEYWORD_GET_DEFAULT(); 

                }
                break;
            case 4 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:63: KEYWORD_MAKE
                {
                mKEYWORD_MAKE(); 

                }
                break;
            case 5 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:76: KEYWORD_GET_HEAD
                {
                mKEYWORD_GET_HEAD(); 

                }
                break;
            case 6 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:93: KEYWORD_GET_TAIL
                {
                mKEYWORD_GET_TAIL(); 

                }
                break;
            case 7 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:110: KEYWORD_IS_EMPTY
                {
                mKEYWORD_IS_EMPTY(); 

                }
                break;
            case 8 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:127: KEYWORD_MAKE_EMPTY
                {
                mKEYWORD_MAKE_EMPTY(); 

                }
                break;
            case 9 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:146: KEYWORD_MAKE_INSERT
                {
                mKEYWORD_MAKE_INSERT(); 

                }
                break;
            case 10 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:166: KEYWORD_GET_ELEMENT
                {
                mKEYWORD_GET_ELEMENT(); 

                }
                break;
            case 11 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:186: KEYWORD_GET_SIZE
                {
                mKEYWORD_GET_SIZE(); 

                }
                break;
            case 12 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:203: KEYWORD_MAKE_APPEND
                {
                mKEYWORD_MAKE_APPEND(); 

                }
                break;
            case 13 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:223: KEYWORD_IMPLEMENT
                {
                mKEYWORD_IMPLEMENT(); 

                }
                break;
            case 14 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:241: KEYWORD_IS_SORT
                {
                mKEYWORD_IS_SORT(); 

                }
                break;
            case 15 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:257: KEYWORD_EQUALS
                {
                mKEYWORD_EQUALS(); 

                }
                break;
            case 16 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:272: ARROW
                {
                mARROW(); 

                }
                break;
            case 17 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:278: LBR
                {
                mLBR(); 

                }
                break;
            case 18 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:282: RBR
                {
                mRBR(); 

                }
                break;
            case 19 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:286: EXTENDS
                {
                mEXTENDS(); 

                }
                break;
            case 20 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:294: VISIT
                {
                mVISIT(); 

                }
                break;
            case 21 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:300: LARROW
                {
                mLARROW(); 

                }
                break;
            case 22 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:307: GREATEROREQU
                {
                mGREATEROREQU(); 

                }
                break;
            case 23 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:320: LOWEROREQU
                {
                mLOWEROREQU(); 

                }
                break;
            case 24 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:331: GREATERTHAN
                {
                mGREATERTHAN(); 

                }
                break;
            case 25 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:343: LOWERTHAN
                {
                mLOWERTHAN(); 

                }
                break;
            case 26 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:353: DOUBLEEQUAL
                {
                mDOUBLEEQUAL(); 

                }
                break;
            case 27 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:365: DIFFERENT
                {
                mDIFFERENT(); 

                }
                break;
            case 28 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:375: AND
                {
                mAND(); 

                }
                break;
            case 29 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:379: OR
                {
                mOR(); 

                }
                break;
            case 30 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:382: PIPE
                {
                mPIPE(); 

                }
                break;
            case 31 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:387: QMARK
                {
                mQMARK(); 

                }
                break;
            case 32 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:393: DQMARK
                {
                mDQMARK(); 

                }
                break;
            case 33 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:400: EQUAL
                {
                mEQUAL(); 

                }
                break;
            case 34 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:406: LSQUAREBR
                {
                mLSQUAREBR(); 

                }
                break;
            case 35 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:416: RSQUAREBR
                {
                mRSQUAREBR(); 

                }
                break;
            case 36 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:426: RPAR
                {
                mRPAR(); 

                }
                break;
            case 37 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:431: LPAR
                {
                mLPAR(); 

                }
                break;
            case 38 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:436: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 39 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:442: STAR
                {
                mSTAR(); 

                }
                break;
            case 40 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:447: UNDERSCORE
                {
                mUNDERSCORE(); 

                }
                break;
            case 41 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:458: AT
                {
                mAT(); 

                }
                break;
            case 42 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:461: ANTI
                {
                mANTI(); 

                }
                break;
            case 43 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:466: DQUOTE
                {
                mDQUOTE(); 

                }
                break;
            case 44 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:473: SQUOTE
                {
                mSQUOTE(); 

                }
                break;
            case 45 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:480: BQUOTE
                {
                mBQUOTE(); 

                }
                break;
            case 46 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:487: COLON
                {
                mCOLON(); 

                }
                break;
            case 47 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:493: IDENTIFIER
                {
                mIDENTIFIER(); 

                }
                break;
            case 48 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:504: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 49 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:512: DOUBLE
                {
                mDOUBLE(); 

                }
                break;
            case 50 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:519: LONG
                {
                mLONG(); 

                }
                break;
            case 51 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:524: ALL_ID
                {
                mALL_ID(); 

                }
                break;
            case 52 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:531: STRING
                {
                mSTRING(); 

                }
                break;
            case 53 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:538: CHAR
                {
                mCHAR(); 

                }
                break;
            case 54 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:543: WS
                {
                mWS(); 

                }
                break;
            case 55 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:546: SL_COMMENT
                {
                mSL_COMMENT(); 

                }
                break;
            case 56 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:557: ML_COMMENT
                {
                mML_COMMENT(); 

                }
                break;
            case 57 :
                // /Users/claudia/Doctorat/tom/workspace/jtom29/src/tom/engine/parser/antlr3/miniTom.g:1:568: DEFAULT
                {
                mDEFAULT(); 

                }
                break;

        }

    }


    protected DFA13 dfa13 = new DFA13(this);
    protected DFA15 dfa15 = new DFA15(this);
    protected DFA28 dfa28 = new DFA28(this);
    static final String DFA13_eotS =
        "\3\uffff\1\5\2\uffff";
    static final String DFA13_eofS =
        "\6\uffff";
    static final String DFA13_minS =
        "\1\56\2\uffff\1\56\2\uffff";
    static final String DFA13_maxS =
        "\1\172\2\uffff\1\71\2\uffff";
    static final String DFA13_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\4\1\3";
    static final String DFA13_specialS =
        "\6\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\4\1\uffff\12\3\7\uffff\32\2\6\uffff\32\1",
            "",
            "",
            "\1\4\1\uffff\12\3",
            "",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "851:20: ( 'a' .. 'z' | 'A' .. 'Z' | ( DIGIT )+ | UNSIGNED_DOUBLE )";
        }
    }
    static final String DFA15_eotS =
        "\2\uffff\1\3\1\uffff\1\3\1\uffff";
    static final String DFA15_eofS =
        "\6\uffff";
    static final String DFA15_minS =
        "\2\101\1\55\1\uffff\1\55\1\uffff";
    static final String DFA15_maxS =
        "\3\172\1\uffff\1\172\1\uffff";
    static final String DFA15_acceptS =
        "\3\uffff\1\1\1\uffff\1\2";
    static final String DFA15_specialS =
        "\6\uffff}>";
    static final String[] DFA15_transitionS = {
            "\32\2\4\uffff\1\1\1\uffff\32\2",
            "\32\2\6\uffff\32\2",
            "\1\5\1\4\1\uffff\12\4\7\uffff\32\4\4\uffff\1\4\1\uffff\32\4",
            "",
            "\1\5\1\4\1\uffff\12\4\7\uffff\32\4\4\uffff\1\4\1\uffff\32\4",
            ""
    };

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "857:1: ALL_ID : ( IDENTIFIER | ID_MINUS );";
        }
    }
    static final String DFA28_eotS =
        "\1\40\4\45\1\42\2\uffff\1\45\1\64\1\66\1\70\1\72\1\42\1\75\1\77"+
        "\6\uffff\1\107\1\uffff\1\111\1\114\2\uffff\1\45\1\117\1\42\2\uffff"+
        "\1\42\1\uffff\2\45\1\uffff\1\45\1\uffff\4\45\1\uffff\1\117\3\uffff"+
        "\1\45\24\uffff\1\45\14\uffff\14\45\1\155\15\45\1\uffff\2\45\1\u0080"+
        "\15\45\1\u008e\1\45\1\uffff\1\u0090\1\45\1\u0092\12\45\1\uffff\1"+
        "\u009d\1\uffff\1\u009e\1\uffff\1\45\1\u00a0\1\u00a1\1\45\1\u00a3"+
        "\1\u00a4\4\45\2\uffff\1\u00a9\2\uffff\1\45\2\uffff\4\45\1\uffff"+
        "\2\45\1\u00b1\2\45\1\u00b4\1\u00b5\1\uffff\1\u00b6\1\u00b7\4\uffff";
    static final String DFA28_eofS =
        "\u00b8\uffff";
    static final String DFA28_minS =
        "\1\0\4\55\1\56\2\uffff\1\55\1\74\3\75\1\46\1\174\1\77\6\uffff\1"+
        "\101\1\uffff\2\0\2\uffff\1\55\1\56\1\60\2\uffff\1\52\1\uffff\2\55"+
        "\1\uffff\1\55\1\uffff\4\55\1\uffff\1\56\3\uffff\1\55\24\uffff\1"+
        "\55\14\uffff\32\55\1\uffff\22\55\1\uffff\15\55\1\uffff\1\55\1\uffff"+
        "\1\55\1\uffff\12\55\2\uffff\1\55\2\uffff\1\55\2\uffff\4\55\1\uffff"+
        "\7\55\1\uffff\2\55\4\uffff";
    static final String DFA28_maxS =
        "\1\uffff\4\172\1\76\2\uffff\1\172\4\75\1\46\1\174\1\77\6\uffff\1"+
        "\172\1\uffff\2\uffff\2\uffff\1\172\1\154\1\71\2\uffff\1\57\1\uffff"+
        "\2\172\1\uffff\1\172\1\uffff\4\172\1\uffff\1\154\3\uffff\1\172\24"+
        "\uffff\1\172\14\uffff\32\172\1\uffff\22\172\1\uffff\15\172\1\uffff"+
        "\1\172\1\uffff\1\172\1\uffff\12\172\2\uffff\1\172\2\uffff\1\172"+
        "\2\uffff\4\172\1\uffff\7\172\1\uffff\2\172\4\uffff";
    static final String DFA28_acceptS =
        "\6\uffff\1\21\1\22\10\uffff\1\42\1\43\1\44\1\45\1\46\1\47\1\uffff"+
        "\1\51\2\uffff\1\55\1\56\3\uffff\2\66\1\uffff\1\71\2\uffff\1\57\1"+
        "\uffff\1\63\4\uffff\1\20\1\uffff\1\61\1\21\1\22\1\uffff\1\25\1\27"+
        "\1\31\1\26\1\30\1\32\1\41\1\33\1\52\1\34\1\35\1\36\1\40\1\37\1\42"+
        "\1\43\1\44\1\45\1\46\1\47\1\uffff\1\50\1\51\1\53\1\64\1\65\1\54"+
        "\1\55\1\56\1\60\1\62\1\67\1\70\32\uffff\1\4\22\uffff\1\24\15\uffff"+
        "\1\17\1\uffff\1\1\1\uffff\1\16\12\uffff\1\23\1\7\1\uffff\1\2\1\13"+
        "\1\uffff\1\5\1\6\4\uffff\1\15\7\uffff\1\10\2\uffff\1\3\1\12\1\11"+
        "\1\14";
    static final String DFA28_specialS =
        "\1\1\27\uffff\1\0\1\2\u009e\uffff}>";
    static final String[] DFA28_transitionS = {
            "\11\42\2\37\2\42\1\37\22\42\1\37\1\14\1\30\3\42\1\15\1\31\1"+
            "\23\1\22\1\25\1\42\1\24\1\5\1\36\1\41\12\35\1\33\1\42\1\11\1"+
            "\13\1\12\1\17\1\27\32\34\1\20\1\42\1\21\1\42\1\26\1\32\4\34"+
            "\1\4\1\34\1\2\1\34\1\1\3\34\1\3\10\34\1\10\4\34\1\6\1\16\1\7"+
            "\uff82\42",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\14\46\1\44\5\46\1\43\7\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\50\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\1\51\31\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\20\46\1\52\6\46\1\53\2\46",
            "\1\56\1\uffff\12\55\4\uffff\1\54",
            "",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\10\46\1\61\21\46",
            "\1\62\1\63",
            "\1\65",
            "\1\67",
            "\1\71",
            "\1\73",
            "\1\74",
            "\1\76",
            "",
            "",
            "",
            "",
            "",
            "",
            "\32\106\6\uffff\32\106",
            "",
            "\12\112\1\uffff\2\112\1\uffff\ufff2\112",
            "\12\113\1\uffff\2\113\1\uffff\31\113\1\uffff\uffd8\113",
            "",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\56\1\uffff\12\55\22\uffff\1\120\37\uffff\1\120",
            "\12\56",
            "",
            "",
            "\1\122\4\uffff\1\121",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\123\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\17\46\1\124\12\46",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\125\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\12\46\1\126\17\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\24\46\1\127\5\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\130\6\46",
            "",
            "\1\56\1\uffff\12\55\22\uffff\1\120\37\uffff\1\120",
            "",
            "",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\22\46\1\131\7\46",
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
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
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
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\133\1\132\14\46\1\134\7\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\13\46\1\135\16\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\136\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\137\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\1\140\31\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\141\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\10\46\1\142\21\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\22\46\1\143\7\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\14\46\1\144\15\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\16\46\1\145\13\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\146\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\3\46\1\150\1\153\2\46\1\151\12\46\1\147\1\152\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\154\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\13\46\1\156\16\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\15\46\1\157\14\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\160\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\30\46\1\161\1\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\17\46\1\162\12\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\21\46\1\163\10\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\14\46\1\164\15\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\10\46\1\166\2\46\1\165\16\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\167\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\170\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\1\171\31\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\13\46\1\172\16\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\1\175\3\46\1\173\3\46\1\174\21\46",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\22\46\1\176\7\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\3\46\1\177\26\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\14\46\1\u0081\15\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\u0082\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\u0083\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\u0084\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\16\46\1\u0085\13\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\31\46\1\u0086",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\5\46\1\u0087\24\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\1\u0088\31\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\10\46\1\u0089\21\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\u008a\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\14\46\1\u008b\15\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\15\46\1\u008c\14\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\17\46\1\u008d\12\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\22\46\1\u008f\7\46",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\30\46\1\u0091\1\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\15\46\1\u0093\14\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\u0094\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\u0095\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\1\u0096\31\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\3\46\1\u0097\26\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\13\46\1\u0098\16\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\14\46\1\u0099\15\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\17\46\1\u009a\12\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\22\46\1\u009b\7\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\17\46\1\u009c\12\46",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\u009f\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\24\46\1\u00a2\5\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\u00a5\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\u00a6\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\u00a7\25\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\4\46\1\u00a8\25\46",
            "",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\13\46\1\u00aa\16\46",
            "",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\15\46\1\u00ab\14\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\30\46\1\u00ac\1\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\21\46\1\u00ad\10\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\15\46\1\u00ae\14\46",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\u00af\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\u00b0\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\23\46\1\u00b2\6\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\3\46\1\u00b3\26\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "\1\47\1\46\1\uffff\12\46\7\uffff\32\46\4\uffff\1\46\1\uffff"+
            "\32\46",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA28_eot = DFA.unpackEncodedString(DFA28_eotS);
    static final short[] DFA28_eof = DFA.unpackEncodedString(DFA28_eofS);
    static final char[] DFA28_min = DFA.unpackEncodedStringToUnsignedChars(DFA28_minS);
    static final char[] DFA28_max = DFA.unpackEncodedStringToUnsignedChars(DFA28_maxS);
    static final short[] DFA28_accept = DFA.unpackEncodedString(DFA28_acceptS);
    static final short[] DFA28_special = DFA.unpackEncodedString(DFA28_specialS);
    static final short[][] DFA28_transition;

    static {
        int numStates = DFA28_transitionS.length;
        DFA28_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA28_transition[i] = DFA.unpackEncodedString(DFA28_transitionS[i]);
        }
    }

    class DFA28 extends DFA {

        public DFA28(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 28;
            this.eot = DFA28_eot;
            this.eof = DFA28_eof;
            this.min = DFA28_min;
            this.max = DFA28_max;
            this.accept = DFA28_accept;
            this.special = DFA28_special;
            this.transition = DFA28_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( KEYWORD_IS_FSYM | KEYWORD_GET_SLOT | KEYWORD_GET_DEFAULT | KEYWORD_MAKE | KEYWORD_GET_HEAD | KEYWORD_GET_TAIL | KEYWORD_IS_EMPTY | KEYWORD_MAKE_EMPTY | KEYWORD_MAKE_INSERT | KEYWORD_GET_ELEMENT | KEYWORD_GET_SIZE | KEYWORD_MAKE_APPEND | KEYWORD_IMPLEMENT | KEYWORD_IS_SORT | KEYWORD_EQUALS | ARROW | LBR | RBR | EXTENDS | VISIT | LARROW | GREATEROREQU | LOWEROREQU | GREATERTHAN | LOWERTHAN | DOUBLEEQUAL | DIFFERENT | AND | OR | PIPE | QMARK | DQMARK | EQUAL | LSQUAREBR | RSQUAREBR | RPAR | LPAR | COMMA | STAR | UNDERSCORE | AT | ANTI | DQUOTE | SQUOTE | BQUOTE | COLON | IDENTIFIER | INTEGER | DOUBLE | LONG | ALL_ID | STRING | CHAR | WS | SL_COMMENT | ML_COMMENT | DEFAULT );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA28_24 = input.LA(1);

                        s = -1;
                        if ( ((LA28_24>='\u0000' && LA28_24<='\t')||(LA28_24>='\u000B' && LA28_24<='\f')||(LA28_24>='\u000E' && LA28_24<='\uFFFF')) ) {s = 74;}

                        else s = 73;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA28_0 = input.LA(1);

                        s = -1;
                        if ( (LA28_0=='i') ) {s = 1;}

                        else if ( (LA28_0=='g') ) {s = 2;}

                        else if ( (LA28_0=='m') ) {s = 3;}

                        else if ( (LA28_0=='e') ) {s = 4;}

                        else if ( (LA28_0=='-') ) {s = 5;}

                        else if ( (LA28_0=='{') ) {s = 6;}

                        else if ( (LA28_0=='}') ) {s = 7;}

                        else if ( (LA28_0=='v') ) {s = 8;}

                        else if ( (LA28_0=='<') ) {s = 9;}

                        else if ( (LA28_0=='>') ) {s = 10;}

                        else if ( (LA28_0=='=') ) {s = 11;}

                        else if ( (LA28_0=='!') ) {s = 12;}

                        else if ( (LA28_0=='&') ) {s = 13;}

                        else if ( (LA28_0=='|') ) {s = 14;}

                        else if ( (LA28_0=='?') ) {s = 15;}

                        else if ( (LA28_0=='[') ) {s = 16;}

                        else if ( (LA28_0==']') ) {s = 17;}

                        else if ( (LA28_0==')') ) {s = 18;}

                        else if ( (LA28_0=='(') ) {s = 19;}

                        else if ( (LA28_0==',') ) {s = 20;}

                        else if ( (LA28_0=='*') ) {s = 21;}

                        else if ( (LA28_0=='_') ) {s = 22;}

                        else if ( (LA28_0=='@') ) {s = 23;}

                        else if ( (LA28_0=='\"') ) {s = 24;}

                        else if ( (LA28_0=='\'') ) {s = 25;}

                        else if ( (LA28_0=='`') ) {s = 26;}

                        else if ( (LA28_0==':') ) {s = 27;}

                        else if ( ((LA28_0>='A' && LA28_0<='Z')||(LA28_0>='a' && LA28_0<='d')||LA28_0=='f'||LA28_0=='h'||(LA28_0>='j' && LA28_0<='l')||(LA28_0>='n' && LA28_0<='u')||(LA28_0>='w' && LA28_0<='z')) ) {s = 28;}

                        else if ( ((LA28_0>='0' && LA28_0<='9')) ) {s = 29;}

                        else if ( (LA28_0=='.') ) {s = 30;}

                        else if ( ((LA28_0>='\t' && LA28_0<='\n')||LA28_0=='\r'||LA28_0==' ') ) {s = 31;}

                        else if ( (LA28_0=='/') ) {s = 33;}

                        else if ( ((LA28_0>='\u0000' && LA28_0<='\b')||(LA28_0>='\u000B' && LA28_0<='\f')||(LA28_0>='\u000E' && LA28_0<='\u001F')||(LA28_0>='#' && LA28_0<='%')||LA28_0=='+'||LA28_0==';'||LA28_0=='\\'||LA28_0=='^'||(LA28_0>='~' && LA28_0<='\uFFFF')) ) {s = 34;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA28_25 = input.LA(1);

                        s = -1;
                        if ( ((LA28_25>='\u0000' && LA28_25<='\t')||(LA28_25>='\u000B' && LA28_25<='\f')||(LA28_25>='\u000E' && LA28_25<='&')||(LA28_25>='(' && LA28_25<='\uFFFF')) ) {s = 75;}

                        else s = 76;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 28, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}