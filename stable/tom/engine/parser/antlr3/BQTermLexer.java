// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g 2016-03-01 15:43:29

package tom.engine.parser.antlr3;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class BQTermLexer extends Lexer {
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
    public static final int Automata=241;
    public static final int BQRecordAppl=302;
    public static final int Conditional=34;
    public static final int Cst_OriginTracking=76;
    public static final int Cst_NumGreaterOrEqualTo=126;
    public static final int FalseTL=32;
    public static final int Cst_UnamedVariableStar=83;
    public static final int ACSymbolDecl=321;
    public static final int UNDERSCORE=376;
    public static final int Cst_Name=122;
    public static final int AndConstraint=258;
    public static final int NamedBlock=206;
    public static final int CompiledPattern=203;
    public static final int concResolveStratBlock=173;
    public static final int concResolveStratElement=165;
    public static final int Strategy=339;
    public static final int Cst_TypetermConstruct=63;
    public static final int SL_COMMENT=391;
    public static final int MakeAddArray=341;
    public static final int IDSTAR=374;
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
    public static final int Cst_OrConstraint=130;
    public static final int NodeString=78;
    public static final int BQTermToInstruction=220;
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
    public static final int Cst_Appl=82;
    public static final int Cst_Type=101;
    public static final int NumGreaterThan=268;
    public static final int BQVariableStar=300;
    public static final int IsSort=11;
    public static final int AssignArray=211;
    public static final int Unitary=310;
    public static final int Cst_ConstraintAction=75;
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
    public static final int ImplicitXMLAttribut=185;
    public static final int ResolveStratBlock=163;
    public static final int WithSymbol=236;
    public static final int Cst_TermAppl=71;
    public static final int COMMA=377;
    public static final int BQID=370;
    public static final int EQUAL=380;
    public static final int ImplicitXMLChild=184;
    public static final int Cst_BQTermToBlock=60;
    public static final int ResolveIsFsymDecl=334;
    public static final int SubtypeDecl=235;
    public static final int Cst_BQRecordAppl=140;
    public static final int Cst_NumGreaterThan=127;
    public static final int DIGIT=383;
    public static final int ACMatchLoop=13;
    public static final int ExpressionToInstruction=219;
    public static final int Cst_EmptyName=121;
    public static final int concTomTerm=249;
    public static final int If=217;
    public static final int Negation=44;
    public static final int BQIDBR=368;
    public static final int RecordAppl=247;
    public static final int Cst_Equals=103;
    public static final int HOSTBLOCK=68;
    public static final int Type=234;
    public static final int Cst_ConstantStar=85;
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
    public static final int EmptyListConstraint=252;
    public static final int RPAR=378;
    public static final int NumericConstraint=253;
    public static final int Cst_MetaQuoteConstruct=62;

      private final TokenCustomizer tokenCustomizer = new TokenCustomizer();
      
      @Override 
      public void emit(Token t){
        super.emit(tokenCustomizer.customize(t));
      }


    // delegates
    // delegators

    public BQTermLexer() {;} 
    public BQTermLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public BQTermLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g"; }

    public Token nextToken() {
        while (true) {
            if ( input.LA(1)==CharStream.EOF ) {
                return Token.EOF_TOKEN;
            }
            state.token = null;
    	state.channel = Token.DEFAULT_CHANNEL;
            state.tokenStartCharIndex = input.index();
            state.tokenStartCharPositionInLine = input.getCharPositionInLine();
            state.tokenStartLine = input.getLine();
    	state.text = null;
            try {
                int m = input.mark();
                state.backtracking=1; 
                state.failed=false;
                mTokens();
                state.backtracking=0;

                if ( state.failed ) {
                    input.rewind(m);
                    input.consume(); 
                }
                else {
                    emit();
                    return state.token;
                }
            }
            catch (RecognitionException re) {
                // shouldn't happen in backtracking mode, but...
                reportError(re);
                recover(re);
            }
        }
    }

    public void memoize(IntStream input,
    		int ruleIndex,
    		int ruleStartIndex)
    {
    if ( state.backtracking>1 ) super.memoize(input, ruleIndex, ruleStartIndex);
    }

    public boolean alreadyParsedRule(IntStream input, int ruleIndex) {
    if ( state.backtracking>1 ) return super.alreadyParsedRule(input, ruleIndex);
    return false;
    }// $ANTLR start "BQIDPAR"
    public final void mBQIDPAR() throws RecognitionException {
        try {
            int _type = BQIDPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken FragID1=null;

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:25:10: ( BQ ( FragWS )* FragID ( FragWS )* LPAR )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:25:12: BQ ( FragWS )* FragID ( FragWS )* LPAR
            {
            mBQ(); if (state.failed) return ;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:25:15: ( FragWS )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='\t' && LA1_0<='\n')||LA1_0=='\r'||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:25:16: FragWS
            	    {
            	    mFragWS(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            int FragID1Start60 = getCharIndex();
            mFragID(); if (state.failed) return ;
            FragID1 = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, FragID1Start60, getCharIndex()-1);
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:25:32: ( FragWS )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='\t' && LA2_0<='\n')||LA2_0=='\r'||LA2_0==' ') ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:25:33: FragWS
            	    {
            	    mFragWS(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            mLPAR(); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              state.text = (FragID1!=null?FragID1.getText():null);
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BQIDPAR"

    // $ANTLR start "BQIDBR"
    public final void mBQIDBR() throws RecognitionException {
        try {
            int _type = BQIDBR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken FragID2=null;

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:30:10: ( BQ ( FragWS )* FragID ( FragWS )* '[' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:30:12: BQ ( FragWS )* FragID ( FragWS )* '['
            {
            mBQ(); if (state.failed) return ;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:30:15: ( FragWS )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\t' && LA3_0<='\n')||LA3_0=='\r'||LA3_0==' ') ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:30:16: FragWS
            	    {
            	    mFragWS(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            int FragID2Start93 = getCharIndex();
            mFragID(); if (state.failed) return ;
            FragID2 = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, FragID2Start93, getCharIndex()-1);
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:30:32: ( FragWS )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||LA4_0=='\r'||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:30:33: FragWS
            	    {
            	    mFragWS(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            match('['); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              state.text = (FragID2!=null?FragID2.getText():null);
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BQIDBR"

    // $ANTLR start "BQIDSTAR"
    public final void mBQIDSTAR() throws RecognitionException {
        try {
            int _type = BQIDSTAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken FragID3=null;

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:35:10: ( BQ FragID ( FragWS )* '*' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:35:12: BQ FragID ( FragWS )* '*'
            {
            mBQ(); if (state.failed) return ;
            int FragID3Start118 = getCharIndex();
            mFragID(); if (state.failed) return ;
            FragID3 = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, FragID3Start118, getCharIndex()-1);
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:35:22: ( FragWS )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\t' && LA5_0<='\n')||LA5_0=='\r'||LA5_0==' ') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:35:23: FragWS
            	    {
            	    mFragWS(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            match('*'); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              state.text = (FragID3!=null?FragID3.getText():null);
            }
            if ( state.backtracking==1 ) {
              tokenCustomizer.prepareNextToken(input.mark());
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BQIDSTAR"

    // $ANTLR start "BQID"
    public final void mBQID() throws RecognitionException {
        try {
            int _type = BQID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken FragID4=null;

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:40:10: ( BQ FragID )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:40:12: BQ FragID
            {
            mBQ(); if (state.failed) return ;
            int FragID4Start150 = getCharIndex();
            mFragID(); if (state.failed) return ;
            FragID4 = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, FragID4Start150, getCharIndex()-1);
            if ( state.backtracking==1 ) {
              state.text = (FragID4!=null?FragID4.getText():null);
            }
            if ( state.backtracking==1 ) {
              tokenCustomizer.prepareNextToken(input.mark());
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BQID"

    // $ANTLR start "BQPAR"
    public final void mBQPAR() throws RecognitionException {
        try {
            int _type = BQPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:45:10: ( BQ LPAR )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:45:12: BQ LPAR
            {
            mBQ(); if (state.failed) return ;
            mLPAR(); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              state.text = "(";
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BQPAR"

    // $ANTLR start "IDPAR"
    public final void mIDPAR() throws RecognitionException {
        try {
            int _type = IDPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken FragID5=null;

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:50:10: ( FragID ( FragWS )* LPAR )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:50:12: FragID ( FragWS )* LPAR
            {
            int FragID5Start194 = getCharIndex();
            mFragID(); if (state.failed) return ;
            FragID5 = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, FragID5Start194, getCharIndex()-1);
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:50:19: ( FragWS )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\t' && LA6_0<='\n')||LA6_0=='\r'||LA6_0==' ') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:50:20: FragWS
            	    {
            	    mFragWS(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            mLPAR(); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              state.text = (FragID5!=null?FragID5.getText():null);
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDPAR"

    // $ANTLR start "IDBR"
    public final void mIDBR() throws RecognitionException {
        try {
            int _type = IDBR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken FragID6=null;

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:55:10: ( FragID ( FragWS )* '[' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:55:12: FragID ( FragWS )* '['
            {
            int FragID6Start222 = getCharIndex();
            mFragID(); if (state.failed) return ;
            FragID6 = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, FragID6Start222, getCharIndex()-1);
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:55:19: ( FragWS )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\t' && LA7_0<='\n')||LA7_0=='\r'||LA7_0==' ') ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:55:20: FragWS
            	    {
            	    mFragWS(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            match('['); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              state.text = (FragID6!=null?FragID6.getText():null);
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDBR"

    // $ANTLR start "IDSTAR"
    public final void mIDSTAR() throws RecognitionException {
        try {
            int _type = IDSTAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken FragID7=null;

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:60:10: ( FragID '*' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:60:12: FragID '*'
            {
            int FragID7Start247 = getCharIndex();
            mFragID(); if (state.failed) return ;
            FragID7 = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, FragID7Start247, getCharIndex()-1);
            match('*'); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              state.text = (FragID7!=null?FragID7.getText():null);
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDSTAR"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:65:10: ( FragID )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:65:12: FragID
            {
            mFragID(); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "UNDERSCORE"
    public final void mUNDERSCORE() throws RecognitionException {
        try {
            int _type = UNDERSCORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:69:12: ( '_' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:69:14: '_'
            {
            match('_'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNDERSCORE"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:71:8: ( ',' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:71:10: ','
            {
            match(','); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "LPAR"
    public final void mLPAR() throws RecognitionException {
        try {
            int _type = LPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:72:8: ( '(' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:72:10: '('
            {
            match('('); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LPAR"

    // $ANTLR start "RPAR"
    public final void mRPAR() throws RecognitionException {
        try {
            int _type = RPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:73:8: ( ')' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:73:10: ')'
            {
            match(')'); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              tokenCustomizer.prepareNextToken(input.mark());
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RPAR"

    // $ANTLR start "RBR"
    public final void mRBR() throws RecognitionException {
        try {
            int _type = RBR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:74:8: ( ']' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:74:10: ']'
            {
            match(']'); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              tokenCustomizer.prepareNextToken(input.mark());
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBR"

    // $ANTLR start "EQUAL"
    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:75:8: ( '=' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:75:10: '='
            {
            match('='); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUAL"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:77:8: ( '-' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:77:10: '-'
            {
            match('-'); if (state.failed) return ;

            }

        }
        finally {
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "BQ"
    public final void mBQ() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:84:9: ( '`' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:84:11: '`'
            {
            match('`'); if (state.failed) return ;

            }

        }
        finally {
        }
    }
    // $ANTLR end "BQ"

    // $ANTLR start "FragID"
    public final void mFragID() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:86:13: ( ( '_' )? LETTER ( LETTER | DIGIT | '_' )* )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:86:16: ( '_' )? LETTER ( LETTER | DIGIT | '_' )*
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:86:16: ( '_' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='_') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:86:17: '_'
                    {
                    match('_'); if (state.failed) return ;

                    }
                    break;

            }

            mLETTER(); if (state.failed) return ;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:86:30: ( LETTER | DIGIT | '_' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='0' && LA9_0<='9')||(LA9_0>='A' && LA9_0<='Z')||LA9_0=='_'||(LA9_0>='a' && LA9_0<='z')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();
            	    state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "FragID"

    // $ANTLR start "BQESC"
    public final void mBQESC() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:89:7: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:89:9: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' )
            {
            match('\\'); if (state.failed) return ;
            if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                input.consume();
            state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "BQESC"

    // $ANTLR start "BQSTRING"
    public final void mBQSTRING() throws RecognitionException {
        try {
            int _type = BQSTRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:114:10: ( '\"' ( BQESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:114:12: '\"' ( BQESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); if (state.failed) return ;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:114:16: ( BQESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )*
            loop10:
            do {
                int alt10=3;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='\\') ) {
                    alt10=1;
                }
                else if ( ((LA10_0>='\u0000' && LA10_0<='\t')||(LA10_0>='\u000B' && LA10_0<='\f')||(LA10_0>='\u000E' && LA10_0<='!')||(LA10_0>='#' && LA10_0<='[')||(LA10_0>=']' && LA10_0<='\uFFFF')) ) {
                    alt10=2;
                }


                switch (alt10) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:114:17: BQESC
            	    {
            	    mBQESC(); if (state.failed) return ;

            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:114:23: ~ ( '\"' | '\\\\' | '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();
            	    state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            match('\"'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BQSTRING"

    // $ANTLR start "BQCHAR"
    public final void mBQCHAR() throws RecognitionException {
        try {
            int _type = BQCHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:115:10: ( '\\'' (~ ( '\\'' | '\\n' | '\\r' | '\\\\' ) )+ '\\'' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:115:12: '\\'' (~ ( '\\'' | '\\n' | '\\r' | '\\\\' ) )+ '\\''
            {
            match('\''); if (state.failed) return ;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:115:17: (~ ( '\\'' | '\\n' | '\\r' | '\\\\' ) )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>='\u0000' && LA11_0<='\t')||(LA11_0>='\u000B' && LA11_0<='\f')||(LA11_0>='\u000E' && LA11_0<='&')||(LA11_0>='(' && LA11_0<='[')||(LA11_0>=']' && LA11_0<='\uFFFF')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:115:18: ~ ( '\\'' | '\\n' | '\\r' | '\\\\' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();
            	    state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);

            match('\''); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BQCHAR"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:117:10: ( ( MINUS )? ( DIGIT )+ )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:117:12: ( MINUS )? ( DIGIT )+
            {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:117:12: ( MINUS )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='-') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:117:14: MINUS
                    {
                    mMINUS(); if (state.failed) return ;

                    }
                    break;

            }

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:117:23: ( DIGIT )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>='0' && LA13_0<='9')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:117:25: DIGIT
            	    {
            	    mDIGIT(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt13 >= 1 ) break loop13;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "BQDOT"
    public final void mBQDOT() throws RecognitionException {
        try {
            int _type = BQDOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:118:10: ( '.' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:118:12: '.'
            {
            match('.'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BQDOT"

    // $ANTLR start "NUM"
    public final void mNUM() throws RecognitionException {
        try {
            int _type = NUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:119:5: ( INTEGER ( BQDOT ( DIGIT )* )? )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:119:7: INTEGER ( BQDOT ( DIGIT )* )?
            {
            mINTEGER(); if (state.failed) return ;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:119:15: ( BQDOT ( DIGIT )* )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0=='.') ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:119:16: BQDOT ( DIGIT )*
                    {
                    mBQDOT(); if (state.failed) return ;
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:119:22: ( DIGIT )*
                    loop14:
                    do {
                        int alt14=2;
                        int LA14_0 = input.LA(1);

                        if ( ((LA14_0>='0' && LA14_0<='9')) ) {
                            alt14=1;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:119:22: DIGIT
                    	    {
                    	    mDIGIT(); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop14;
                        }
                    } while (true);


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUM"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:122:9: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:122:11: ( 'A' .. 'Z' | 'a' .. 'z' )
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();
            state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
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
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:124:9: ( '0' .. '9' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:124:11: '0' .. '9'
            {
            matchRange('0','9'); if (state.failed) return ;

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "FragWS"
    public final void mFragWS() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:127:13: ( ( '\\r' | '\\n' | '\\t' | ' ' ) )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:127:15: ( '\\r' | '\\n' | '\\t' | ' ' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();
            state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "FragWS"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:129:4: ( FragWS )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:130:1: FragWS
            {
            mFragWS(); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              _channel=HIDDEN;
            }

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
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:132:12: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:132:14: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); if (state.failed) return ;

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:132:19: (~ ( '\\n' | '\\r' ) )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( ((LA16_0>='\u0000' && LA16_0<='\t')||(LA16_0>='\u000B' && LA16_0<='\f')||(LA16_0>='\u000E' && LA16_0<='\uFFFF')) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:132:20: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();
            	    state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:132:35: ( '\\n' | '\\r' ( '\\n' )? )?
            int alt18=3;
            int LA18_0 = input.LA(1);

            if ( (LA18_0=='\n') ) {
                alt18=1;
            }
            else if ( (LA18_0=='\r') ) {
                alt18=2;
            }
            switch (alt18) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:132:36: '\\n'
                    {
                    match('\n'); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:132:41: '\\r' ( '\\n' )?
                    {
                    match('\r'); if (state.failed) return ;
                    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:132:45: ( '\\n' )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0=='\n') ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:132:46: '\\n'
                            {
                            match('\n'); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }

            if ( state.backtracking==1 ) {
               _channel=HIDDEN; 
            }

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
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:133:12: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:133:14: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); if (state.failed) return ;

            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:133:19: ( options {greedy=false; } : . )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0=='*') ) {
                    int LA19_1 = input.LA(2);

                    if ( (LA19_1=='/') ) {
                        alt19=2;
                    }
                    else if ( ((LA19_1>='\u0000' && LA19_1<='.')||(LA19_1>='0' && LA19_1<='\uFFFF')) ) {
                        alt19=1;
                    }


                }
                else if ( ((LA19_0>='\u0000' && LA19_0<=')')||(LA19_0>='+' && LA19_0<='\uFFFF')) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:133:47: .
            	    {
            	    matchAny(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            match("*/"); if (state.failed) return ;

            if ( state.backtracking==1 ) {
               _channel=HIDDEN; 
            }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ML_COMMENT"

    // $ANTLR start "ANY"
    public final void mANY() throws RecognitionException {
        try {
            int _type = ANY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:135:5: ( . )
            // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:135:7: .
            {
            matchAny(); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANY"

    public void mTokens() throws RecognitionException {
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:39: ( BQIDPAR | BQIDBR | BQIDSTAR | BQID | BQPAR | IDPAR | IDBR | IDSTAR | ID | UNDERSCORE | COMMA | LPAR | RPAR | RBR | EQUAL | BQSTRING | BQCHAR | BQDOT | NUM | WS | SL_COMMENT | ML_COMMENT | ANY )
        int alt20=23;
        alt20 = dfa20.predict(input);
        switch (alt20) {
            case 1 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:41: BQIDPAR
                {
                mBQIDPAR(); if (state.failed) return ;

                }
                break;
            case 2 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:49: BQIDBR
                {
                mBQIDBR(); if (state.failed) return ;

                }
                break;
            case 3 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:56: BQIDSTAR
                {
                mBQIDSTAR(); if (state.failed) return ;

                }
                break;
            case 4 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:65: BQID
                {
                mBQID(); if (state.failed) return ;

                }
                break;
            case 5 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:70: BQPAR
                {
                mBQPAR(); if (state.failed) return ;

                }
                break;
            case 6 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:76: IDPAR
                {
                mIDPAR(); if (state.failed) return ;

                }
                break;
            case 7 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:82: IDBR
                {
                mIDBR(); if (state.failed) return ;

                }
                break;
            case 8 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:87: IDSTAR
                {
                mIDSTAR(); if (state.failed) return ;

                }
                break;
            case 9 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:94: ID
                {
                mID(); if (state.failed) return ;

                }
                break;
            case 10 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:97: UNDERSCORE
                {
                mUNDERSCORE(); if (state.failed) return ;

                }
                break;
            case 11 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:108: COMMA
                {
                mCOMMA(); if (state.failed) return ;

                }
                break;
            case 12 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:114: LPAR
                {
                mLPAR(); if (state.failed) return ;

                }
                break;
            case 13 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:119: RPAR
                {
                mRPAR(); if (state.failed) return ;

                }
                break;
            case 14 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:124: RBR
                {
                mRBR(); if (state.failed) return ;

                }
                break;
            case 15 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:128: EQUAL
                {
                mEQUAL(); if (state.failed) return ;

                }
                break;
            case 16 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:134: BQSTRING
                {
                mBQSTRING(); if (state.failed) return ;

                }
                break;
            case 17 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:143: BQCHAR
                {
                mBQCHAR(); if (state.failed) return ;

                }
                break;
            case 18 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:150: BQDOT
                {
                mBQDOT(); if (state.failed) return ;

                }
                break;
            case 19 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:156: NUM
                {
                mNUM(); if (state.failed) return ;

                }
                break;
            case 20 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:160: WS
                {
                mWS(); if (state.failed) return ;

                }
                break;
            case 21 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:163: SL_COMMENT
                {
                mSL_COMMENT(); if (state.failed) return ;

                }
                break;
            case 22 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:174: ML_COMMENT
                {
                mML_COMMENT(); if (state.failed) return ;

                }
                break;
            case 23 :
                // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:185: ANY
                {
                mANY(); if (state.failed) return ;

                }
                break;

        }

    }

    // $ANTLR start synpred1_BQTermLexer
    public final void synpred1_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:41: ( BQIDPAR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:41: BQIDPAR
        {
        mBQIDPAR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_BQTermLexer

    // $ANTLR start synpred2_BQTermLexer
    public final void synpred2_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:49: ( BQIDBR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:49: BQIDBR
        {
        mBQIDBR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred2_BQTermLexer

    // $ANTLR start synpred3_BQTermLexer
    public final void synpred3_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:56: ( BQIDSTAR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:56: BQIDSTAR
        {
        mBQIDSTAR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred3_BQTermLexer

    // $ANTLR start synpred4_BQTermLexer
    public final void synpred4_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:65: ( BQID )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:65: BQID
        {
        mBQID(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred4_BQTermLexer

    // $ANTLR start synpred5_BQTermLexer
    public final void synpred5_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:70: ( BQPAR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:70: BQPAR
        {
        mBQPAR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred5_BQTermLexer

    // $ANTLR start synpred6_BQTermLexer
    public final void synpred6_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:76: ( IDPAR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:76: IDPAR
        {
        mIDPAR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred6_BQTermLexer

    // $ANTLR start synpred7_BQTermLexer
    public final void synpred7_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:82: ( IDBR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:82: IDBR
        {
        mIDBR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred7_BQTermLexer

    // $ANTLR start synpred8_BQTermLexer
    public final void synpred8_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:87: ( IDSTAR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:87: IDSTAR
        {
        mIDSTAR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred8_BQTermLexer

    // $ANTLR start synpred9_BQTermLexer
    public final void synpred9_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:94: ( ID )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:94: ID
        {
        mID(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred9_BQTermLexer

    // $ANTLR start synpred10_BQTermLexer
    public final void synpred10_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:97: ( UNDERSCORE )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:97: UNDERSCORE
        {
        mUNDERSCORE(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred10_BQTermLexer

    // $ANTLR start synpred11_BQTermLexer
    public final void synpred11_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:108: ( COMMA )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:108: COMMA
        {
        mCOMMA(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred11_BQTermLexer

    // $ANTLR start synpred12_BQTermLexer
    public final void synpred12_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:114: ( LPAR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:114: LPAR
        {
        mLPAR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_BQTermLexer

    // $ANTLR start synpred13_BQTermLexer
    public final void synpred13_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:119: ( RPAR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:119: RPAR
        {
        mRPAR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred13_BQTermLexer

    // $ANTLR start synpred14_BQTermLexer
    public final void synpred14_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:124: ( RBR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:124: RBR
        {
        mRBR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred14_BQTermLexer

    // $ANTLR start synpred15_BQTermLexer
    public final void synpred15_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:128: ( EQUAL )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:128: EQUAL
        {
        mEQUAL(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred15_BQTermLexer

    // $ANTLR start synpred16_BQTermLexer
    public final void synpred16_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:134: ( BQSTRING )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:134: BQSTRING
        {
        mBQSTRING(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred16_BQTermLexer

    // $ANTLR start synpred17_BQTermLexer
    public final void synpred17_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:143: ( BQCHAR )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:143: BQCHAR
        {
        mBQCHAR(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred17_BQTermLexer

    // $ANTLR start synpred18_BQTermLexer
    public final void synpred18_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:150: ( BQDOT )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:150: BQDOT
        {
        mBQDOT(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred18_BQTermLexer

    // $ANTLR start synpred19_BQTermLexer
    public final void synpred19_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:156: ( NUM )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:156: NUM
        {
        mNUM(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred19_BQTermLexer

    // $ANTLR start synpred20_BQTermLexer
    public final void synpred20_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:160: ( WS )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:160: WS
        {
        mWS(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred20_BQTermLexer

    // $ANTLR start synpred21_BQTermLexer
    public final void synpred21_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:163: ( SL_COMMENT )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:163: SL_COMMENT
        {
        mSL_COMMENT(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred21_BQTermLexer

    // $ANTLR start synpred22_BQTermLexer
    public final void synpred22_BQTermLexer_fragment() throws RecognitionException {   
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:174: ( ML_COMMENT )
        // /Users/pem/github/tom/src/tom/engine/parser/antlr3/BQTermLexer.g:1:174: ML_COMMENT
        {
        mML_COMMENT(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred22_BQTermLexer

    public final boolean synpred6_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred13_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred13_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred16_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred16_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred15_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred15_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred17_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred17_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred10_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred10_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred22_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred22_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred11_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred11_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred18_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred18_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred19_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred19_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred20_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred20_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred21_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred21_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred9_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred14_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred14_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_BQTermLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_BQTermLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA20 dfa20 = new DFA20(this);
    static final String DFA20_eotS =
        "\47\uffff";
    static final String DFA20_eofS =
        "\47\uffff";
    static final String DFA20_minS =
        "\2\0\2\uffff\1\0\2\uffff\1\0\2\uffff\1\0\4\uffff\1\0\1\uffff\1\0"+
        "\1\uffff\1\0\1\uffff\1\0\1\uffff\1\0\5\uffff\1\0\1\uffff\1\0\1\uffff"+
        "\1\0\1\uffff\1\0\1\uffff\1\0\1\uffff";
    static final String DFA20_maxS =
        "\1\uffff\1\0\2\uffff\1\0\2\uffff\1\0\2\uffff\1\0\4\uffff\1\0\1\uffff"+
        "\1\0\1\uffff\1\0\1\uffff\1\0\1\uffff\1\0\5\uffff\1\0\1\uffff\1\0"+
        "\1\uffff\1\0\1\uffff\1\0\1\uffff\1\0\1\uffff";
    static final String DFA20_acceptS =
        "\2\uffff\1\17\1\27\1\uffff\1\23\1\27\1\uffff\1\25\1\26\1\uffff\1"+
        "\6\1\7\1\10\1\11\1\uffff\1\21\1\uffff\1\16\1\uffff\1\24\1\uffff"+
        "\1\14\1\uffff\1\1\1\2\1\3\1\4\1\5\1\uffff\1\22\1\uffff\1\13\1\uffff"+
        "\1\20\1\uffff\1\12\1\uffff\1\15";
    static final String DFA20_specialS =
        "\1\0\1\1\2\uffff\1\2\2\uffff\1\3\2\uffff\1\4\4\uffff\1\5\1\uffff"+
        "\1\6\1\uffff\1\7\1\uffff\1\10\1\uffff\1\11\5\uffff\1\12\1\uffff"+
        "\1\13\1\uffff\1\14\1\uffff\1\15\1\uffff\1\16\1\uffff}>";
    static final String[] DFA20_transitionS = {
            "\11\6\2\23\2\6\1\23\22\6\1\23\1\6\1\41\4\6\1\17\1\25\1\45\2"+
            "\6\1\37\1\4\1\35\1\7\12\4\3\6\1\1\3\6\32\12\2\6\1\21\1\6\1\43"+
            "\1\27\32\12\uff85\6",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            ""
    };

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "1:1: Tokens options {k=1; backtrack=true; } : ( BQIDPAR | BQIDBR | BQIDSTAR | BQID | BQPAR | IDPAR | IDBR | IDSTAR | ID | UNDERSCORE | COMMA | LPAR | RPAR | RBR | EQUAL | BQSTRING | BQCHAR | BQDOT | NUM | WS | SL_COMMENT | ML_COMMENT | ANY );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA20_0 = input.LA(1);

                        s = -1;
                        if ( (LA20_0=='=') ) {s = 1;}

                        else if ( (LA20_0=='-'||(LA20_0>='0' && LA20_0<='9')) ) {s = 4;}

                        else if ( ((LA20_0>='\u0000' && LA20_0<='\b')||(LA20_0>='\u000B' && LA20_0<='\f')||(LA20_0>='\u000E' && LA20_0<='\u001F')||LA20_0=='!'||(LA20_0>='#' && LA20_0<='&')||(LA20_0>='*' && LA20_0<='+')||(LA20_0>=':' && LA20_0<='<')||(LA20_0>='>' && LA20_0<='@')||(LA20_0>='[' && LA20_0<='\\')||LA20_0=='^'||(LA20_0>='{' && LA20_0<='\uFFFF')) ) {s = 6;}

                        else if ( (LA20_0=='/') ) {s = 7;}

                        else if ( ((LA20_0>='A' && LA20_0<='Z')||(LA20_0>='a' && LA20_0<='z')) ) {s = 10;}

                        else if ( (LA20_0=='\'') ) {s = 15;}

                        else if ( (LA20_0==']') ) {s = 17;}

                        else if ( ((LA20_0>='\t' && LA20_0<='\n')||LA20_0=='\r'||LA20_0==' ') ) {s = 19;}

                        else if ( (LA20_0=='(') ) {s = 21;}

                        else if ( (LA20_0=='`') ) {s = 23;}

                        else if ( (LA20_0=='.') ) {s = 29;}

                        else if ( (LA20_0==',') ) {s = 31;}

                        else if ( (LA20_0=='\"') ) {s = 33;}

                        else if ( (LA20_0=='_') ) {s = 35;}

                        else if ( (LA20_0==')') ) {s = 37;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA20_1 = input.LA(1);

                         
                        int index20_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_BQTermLexer()) ) {s = 2;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index20_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA20_4 = input.LA(1);

                         
                        int index20_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred19_BQTermLexer()) ) {s = 5;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index20_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA20_7 = input.LA(1);

                         
                        int index20_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_BQTermLexer()) ) {s = 8;}

                        else if ( (synpred22_BQTermLexer()) ) {s = 9;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_7);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA20_10 = input.LA(1);

                         
                        int index20_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred6_BQTermLexer()) ) {s = 11;}

                        else if ( (synpred7_BQTermLexer()) ) {s = 12;}

                        else if ( (synpred8_BQTermLexer()) ) {s = 13;}

                        else if ( (synpred9_BQTermLexer()) ) {s = 14;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_10);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA20_15 = input.LA(1);

                         
                        int index20_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_BQTermLexer()) ) {s = 16;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_15);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA20_17 = input.LA(1);

                         
                        int index20_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_BQTermLexer()) ) {s = 18;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_17);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA20_19 = input.LA(1);

                         
                        int index20_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred20_BQTermLexer()) ) {s = 20;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_19);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA20_21 = input.LA(1);

                         
                        int index20_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_BQTermLexer()) ) {s = 22;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_21);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA20_23 = input.LA(1);

                         
                        int index20_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_BQTermLexer()) ) {s = 24;}

                        else if ( (synpred2_BQTermLexer()) ) {s = 25;}

                        else if ( (synpred3_BQTermLexer()) ) {s = 26;}

                        else if ( (synpred4_BQTermLexer()) ) {s = 27;}

                        else if ( (synpred5_BQTermLexer()) ) {s = 28;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_23);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA20_29 = input.LA(1);

                         
                        int index20_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred18_BQTermLexer()) ) {s = 30;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_29);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA20_31 = input.LA(1);

                         
                        int index20_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_BQTermLexer()) ) {s = 32;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_31);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA20_33 = input.LA(1);

                         
                        int index20_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred16_BQTermLexer()) ) {s = 34;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_33);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA20_35 = input.LA(1);

                         
                        int index20_35 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred6_BQTermLexer()) ) {s = 11;}

                        else if ( (synpred7_BQTermLexer()) ) {s = 12;}

                        else if ( (synpred8_BQTermLexer()) ) {s = 13;}

                        else if ( (synpred9_BQTermLexer()) ) {s = 14;}

                        else if ( (synpred10_BQTermLexer()) ) {s = 36;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_35);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA20_37 = input.LA(1);

                         
                        int index20_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred13_BQTermLexer()) ) {s = 38;}

                        else if ( (true) ) {s = 6;}

                         
                        input.seek(index20_37);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 20, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}