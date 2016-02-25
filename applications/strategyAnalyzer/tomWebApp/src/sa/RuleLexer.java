// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g 2016-02-25 12:49:45

  package sa;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RuleLexer extends Lexer {
    public static final int LT=96;
    public static final int FUNCTIONS=64;
    public static final int EmptyTrs=19;
    public static final int ConcRule=40;
    public static final int Empty=29;
    public static final int SLCOMMENT=102;
    public static final int EQUALS=71;
    public static final int ONE=82;
    public static final int EOF=-1;
    public static final int CondEquals=28;
    public static final int ConcParam=59;
    public static final int LBRACKET=67;
    public static final int MU=83;
    public static final int ConcStratDecl=46;
    public static final int LPAR=72;
    public static final int StratRule=56;
    public static final int StratIdentity=53;
    public static final int GEQ=97;
    public static final int StratMu=48;
    public static final int MLCOMMENT=103;
    public static final int GomType=58;
    public static final int RBRACE=78;
    public static final int StratAll=51;
    public static final int ConcStrat=22;
    public static final int DOUBLEEQUALS=87;
    public static final int StratOne=50;
    public static final int TRS=66;
    public static final int INT=88;
    public static final int SEMICOLON=75;
    public static final int SIGNATURE=93;
    public static final int FAIL=80;
    public static final int Inter=32;
    public static final int WS=101;
    public static final int TrueMatch=30;
    public static final int ConditionalRule=17;
    public static final int CondNot=26;
    public static final int GT=98;
    public static final int AMPERCENT=90;
    public static final int ConcField=16;
    public static final int Alternative=13;
    public static final int Anti=36;
    public static final int Var=38;
    public static final int EmptyEnvironment=45;
    public static final int STRATEGIES=65;
    public static final int IDENTITY=79;
    public static final int Trs=20;
    public static final int ESC=99;
    public static final int LBRACE=77;
    public static final int StratName=57;
    public static final int ABSTRACT=62;
    public static final int ID=70;
    public static final int Otrs=21;
    public static final int Add=34;
    public static final int CondTrue=25;
    public static final int Program=10;
    public static final int IF=86;
    public static final int SYNTAX=63;
    public static final int StratFail=52;
    public static final int Param=43;
    public static final int ALT=74;
    public static final int IN=92;
    public static final int COMMA=68;
    public static final int T__104=104;
    public static final int ALL=81;
    public static final int StratTrs=49;
    public static final int TermList=23;
    public static final int ConcGomType=41;
    public static final int Symbol=14;
    public static final int RBRACKET=69;
    public static final int DOT=84;
    public static final int CHOICE=76;
    public static final int BuiltinInt=37;
    public static final int UnamedField=60;
    public static final int PushEnvironment=44;
    public static final int At=35;
    public static final int ConcAlternative=15;
    public static final int ConcProduction=12;
    public static final int NOTEQUALS=94;
    public static final int Sub=33;
    public static final int StratDecl=61;
    public static final int CondFalse=24;
    public static final int COLON=89;
    public static final int StratSequence=55;
    public static final int Match=31;
    public static final int CondAnd=27;
    public static final int Rule=18;
    public static final int StratChoice=54;
    public static final int Appl=39;
    public static final int SortType=11;
    public static final int ARROW=85;
    public static final int RPAR=73;
    public static final int StratAppl=47;
    public static final int LET=91;
    public static final int ConcAdd=42;
    public static final int STRING=100;
    public static final int LEQ=95;

    // delegates
    // delegators

    public RuleLexer() {;} 
    public RuleLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public RuleLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g"; }

    // $ANTLR start "T__104"
    public final void mT__104() throws RecognitionException {
        try {
            int _type = T__104;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:11:8: ( '!' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:11:10: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__104"

    // $ANTLR start "ABSTRACT"
    public final void mABSTRACT() throws RecognitionException {
        try {
            int _type = ABSTRACT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:153:10: ( 'abstract' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:153:12: 'abstract'
            {
            match("abstract"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ABSTRACT"

    // $ANTLR start "SYNTAX"
    public final void mSYNTAX() throws RecognitionException {
        try {
            int _type = SYNTAX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:154:10: ( 'syntax' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:154:12: 'syntax'
            {
            match("syntax"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SYNTAX"

    // $ANTLR start "STRATEGIES"
    public final void mSTRATEGIES() throws RecognitionException {
        try {
            int _type = STRATEGIES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:155:14: ( 'strategies' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:155:16: 'strategies'
            {
            match("strategies"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRATEGIES"

    // $ANTLR start "FUNCTIONS"
    public final void mFUNCTIONS() throws RecognitionException {
        try {
            int _type = FUNCTIONS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:156:11: ( 'functions' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:156:13: 'functions'
            {
            match("functions"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FUNCTIONS"

    // $ANTLR start "TRS"
    public final void mTRS() throws RecognitionException {
        try {
            int _type = TRS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:157:5: ( 'trs' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:157:7: 'trs'
            {
            match("trs"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRS"

    // $ANTLR start "ARROW"
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:160:7: ( '->' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:160:9: '->'
            {
            match("->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ARROW"

    // $ANTLR start "AMPERCENT"
    public final void mAMPERCENT() throws RecognitionException {
        try {
            int _type = AMPERCENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:161:11: ( '&' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:161:13: '&'
            {
            match('&'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AMPERCENT"

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:162:7: ( ':' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:162:9: ':'
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

    // $ANTLR start "LPAR"
    public final void mLPAR() throws RecognitionException {
        try {
            int _type = LPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:163:6: ( '(' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:163:8: '('
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

    // $ANTLR start "RPAR"
    public final void mRPAR() throws RecognitionException {
        try {
            int _type = RPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:164:6: ( ')' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:164:8: ')'
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

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:165:8: ( '{' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:165:10: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACE"

    // $ANTLR start "RBRACE"
    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:166:8: ( '}' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:166:10: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACE"

    // $ANTLR start "LBRACKET"
    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:167:10: ( '[' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:167:12: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACKET"

    // $ANTLR start "RBRACKET"
    public final void mRBRACKET() throws RecognitionException {
        try {
            int _type = RBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:168:10: ( ']' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:168:12: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACKET"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:169:7: ( ',' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:169:9: ','
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

    // $ANTLR start "SEMICOLON"
    public final void mSEMICOLON() throws RecognitionException {
        try {
            int _type = SEMICOLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:170:11: ( ';' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:170:13: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMICOLON"

    // $ANTLR start "CHOICE"
    public final void mCHOICE() throws RecognitionException {
        try {
            int _type = CHOICE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:171:8: ( '<+' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:171:10: '<+'
            {
            match("<+"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CHOICE"

    // $ANTLR start "IDENTITY"
    public final void mIDENTITY() throws RecognitionException {
        try {
            int _type = IDENTITY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:172:10: ( 'identity' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:172:12: 'identity'
            {
            match("identity"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENTITY"

    // $ANTLR start "FAIL"
    public final void mFAIL() throws RecognitionException {
        try {
            int _type = FAIL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:173:6: ( 'fail' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:173:8: 'fail'
            {
            match("fail"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FAIL"

    // $ANTLR start "ALL"
    public final void mALL() throws RecognitionException {
        try {
            int _type = ALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:174:5: ( 'all' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:174:7: 'all'
            {
            match("all"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALL"

    // $ANTLR start "ONE"
    public final void mONE() throws RecognitionException {
        try {
            int _type = ONE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:175:5: ( 'one' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:175:7: 'one'
            {
            match("one"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ONE"

    // $ANTLR start "MU"
    public final void mMU() throws RecognitionException {
        try {
            int _type = MU;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:176:4: ( 'mu' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:176:6: 'mu'
            {
            match("mu"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MU"

    // $ANTLR start "LET"
    public final void mLET() throws RecognitionException {
        try {
            int _type = LET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:177:5: ( 'let' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:177:7: 'let'
            {
            match("let"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LET"

    // $ANTLR start "IN"
    public final void mIN() throws RecognitionException {
        try {
            int _type = IN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:178:4: ( 'in' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:178:6: 'in'
            {
            match("in"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IN"

    // $ANTLR start "SIGNATURE"
    public final void mSIGNATURE() throws RecognitionException {
        try {
            int _type = SIGNATURE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:179:11: ( 'signature' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:179:13: 'signature'
            {
            match("signature"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SIGNATURE"

    // $ANTLR start "EQUALS"
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:180:8: ( '=' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:180:10: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUALS"

    // $ANTLR start "ALT"
    public final void mALT() throws RecognitionException {
        try {
            int _type = ALT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:181:5: ( '|' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:181:7: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALT"

    // $ANTLR start "DOUBLEEQUALS"
    public final void mDOUBLEEQUALS() throws RecognitionException {
        try {
            int _type = DOUBLEEQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:182:14: ( '==' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:182:16: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLEEQUALS"

    // $ANTLR start "NOTEQUALS"
    public final void mNOTEQUALS() throws RecognitionException {
        try {
            int _type = NOTEQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:183:11: ( '!=' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:183:13: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOTEQUALS"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:184:5: ( '.' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:184:7: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "LEQ"
    public final void mLEQ() throws RecognitionException {
        try {
            int _type = LEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:185:5: ( '<=' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:185:7: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEQ"

    // $ANTLR start "LT"
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:186:4: ( '<' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:186:6: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LT"

    // $ANTLR start "GEQ"
    public final void mGEQ() throws RecognitionException {
        try {
            int _type = GEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:187:5: ( '>=' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:187:7: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GEQ"

    // $ANTLR start "GT"
    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:188:4: ( '>' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:188:6: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GT"

    // $ANTLR start "IF"
    public final void mIF() throws RecognitionException {
        try {
            int _type = IF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:189:4: ( 'if' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:189:6: 'if'
            {
            match("if"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IF"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:190:5: ( ( '0' .. '9' )+ )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:190:7: ( '0' .. '9' )+
            {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:190:7: ( '0' .. '9' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:190:8: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "ESC"
    public final void mESC() throws RecognitionException {
        try {
            int _type = ESC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:191:5: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:191:7: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' )
            {
            match('\\'); 
            if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ESC"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:192:8: ( '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:192:10: '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); 
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:192:14: ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='\\') ) {
                    alt2=1;
                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='!')||(LA2_0>='#' && LA2_0<='[')||(LA2_0>=']' && LA2_0<='\uFFFF')) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:192:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:192:19: ~ ( '\"' | '\\\\' | '\\n' | '\\r' )
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
            	    break loop2;
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

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:193:4: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* ( '*' )? )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:193:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* ( '*' )?
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:193:30: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:193:64: ( '*' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='*') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:193:65: '*'
                    {
                    match('*'); 

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
    // $ANTLR end "ID"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:194:4: ( ( ' ' | '\\t' | '\\n' )+ )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:194:6: ( ' ' | '\\t' | '\\n' )+
            {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:194:6: ( ' ' | '\\t' | '\\n' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\t' && LA5_0<='\n')||LA5_0==' ') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


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

             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "SLCOMMENT"
    public final void mSLCOMMENT() throws RecognitionException {
        try {
            int _type = SLCOMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:196:11: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:196:13: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:196:18: (~ ( '\\n' | '\\r' ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:196:19: ~ ( '\\n' | '\\r' )
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
            	    break loop6;
                }
            } while (true);

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:196:34: ( '\\n' | '\\r' ( '\\n' )? )?
            int alt8=3;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='\n') ) {
                alt8=1;
            }
            else if ( (LA8_0=='\r') ) {
                alt8=2;
            }
            switch (alt8) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:196:35: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:196:40: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:196:44: ( '\\n' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0=='\n') ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:196:45: '\\n'
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
    // $ANTLR end "SLCOMMENT"

    // $ANTLR start "MLCOMMENT"
    public final void mMLCOMMENT() throws RecognitionException {
        try {
            int _type = MLCOMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:198:11: ( '/*' ~ '*' ( . )* '*/' )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:199:3: '/*' ~ '*' ( . )* '*/'
            {
            match("/*"); 

            if ( (input.LA(1)>='\u0000' && input.LA(1)<=')')||(input.LA(1)>='+' && input.LA(1)<='\uFFFF') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:199:12: ( . )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='*') ) {
                    int LA9_1 = input.LA(2);

                    if ( (LA9_1=='/') ) {
                        alt9=2;
                    }
                    else if ( ((LA9_1>='\u0000' && LA9_1<='.')||(LA9_1>='0' && LA9_1<='\uFFFF')) ) {
                        alt9=1;
                    }


                }
                else if ( ((LA9_0>='\u0000' && LA9_0<=')')||(LA9_0>='+' && LA9_0<='\uFFFF')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:199:12: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop9;
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
    // $ANTLR end "MLCOMMENT"

    public void mTokens() throws RecognitionException {
        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:8: ( T__104 | ABSTRACT | SYNTAX | STRATEGIES | FUNCTIONS | TRS | ARROW | AMPERCENT | COLON | LPAR | RPAR | LBRACE | RBRACE | LBRACKET | RBRACKET | COMMA | SEMICOLON | CHOICE | IDENTITY | FAIL | ALL | ONE | MU | LET | IN | SIGNATURE | EQUALS | ALT | DOUBLEEQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT | MLCOMMENT )
        int alt10=43;
        alt10 = dfa10.predict(input);
        switch (alt10) {
            case 1 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:10: T__104
                {
                mT__104(); 

                }
                break;
            case 2 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:17: ABSTRACT
                {
                mABSTRACT(); 

                }
                break;
            case 3 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:26: SYNTAX
                {
                mSYNTAX(); 

                }
                break;
            case 4 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:33: STRATEGIES
                {
                mSTRATEGIES(); 

                }
                break;
            case 5 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:44: FUNCTIONS
                {
                mFUNCTIONS(); 

                }
                break;
            case 6 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:54: TRS
                {
                mTRS(); 

                }
                break;
            case 7 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:58: ARROW
                {
                mARROW(); 

                }
                break;
            case 8 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:64: AMPERCENT
                {
                mAMPERCENT(); 

                }
                break;
            case 9 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:74: COLON
                {
                mCOLON(); 

                }
                break;
            case 10 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:80: LPAR
                {
                mLPAR(); 

                }
                break;
            case 11 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:85: RPAR
                {
                mRPAR(); 

                }
                break;
            case 12 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:90: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 13 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:97: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 14 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:104: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 15 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:113: RBRACKET
                {
                mRBRACKET(); 

                }
                break;
            case 16 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:122: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 17 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:128: SEMICOLON
                {
                mSEMICOLON(); 

                }
                break;
            case 18 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:138: CHOICE
                {
                mCHOICE(); 

                }
                break;
            case 19 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:145: IDENTITY
                {
                mIDENTITY(); 

                }
                break;
            case 20 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:154: FAIL
                {
                mFAIL(); 

                }
                break;
            case 21 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:159: ALL
                {
                mALL(); 

                }
                break;
            case 22 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:163: ONE
                {
                mONE(); 

                }
                break;
            case 23 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:167: MU
                {
                mMU(); 

                }
                break;
            case 24 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:170: LET
                {
                mLET(); 

                }
                break;
            case 25 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:174: IN
                {
                mIN(); 

                }
                break;
            case 26 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:177: SIGNATURE
                {
                mSIGNATURE(); 

                }
                break;
            case 27 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:187: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 28 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:194: ALT
                {
                mALT(); 

                }
                break;
            case 29 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:198: DOUBLEEQUALS
                {
                mDOUBLEEQUALS(); 

                }
                break;
            case 30 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:211: NOTEQUALS
                {
                mNOTEQUALS(); 

                }
                break;
            case 31 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:221: DOT
                {
                mDOT(); 

                }
                break;
            case 32 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:225: LEQ
                {
                mLEQ(); 

                }
                break;
            case 33 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:229: LT
                {
                mLT(); 

                }
                break;
            case 34 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:232: GEQ
                {
                mGEQ(); 

                }
                break;
            case 35 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:236: GT
                {
                mGT(); 

                }
                break;
            case 36 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:239: IF
                {
                mIF(); 

                }
                break;
            case 37 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:242: INT
                {
                mINT(); 

                }
                break;
            case 38 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:246: ESC
                {
                mESC(); 

                }
                break;
            case 39 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:250: STRING
                {
                mSTRING(); 

                }
                break;
            case 40 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:257: ID
                {
                mID(); 

                }
                break;
            case 41 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:260: WS
                {
                mWS(); 

                }
                break;
            case 42 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:263: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;
            case 43 :
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:1:273: MLCOMMENT
                {
                mMLCOMMENT(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\1\uffff\1\41\4\35\13\uffff\1\54\4\35\1\64\2\uffff\1\66\10\uffff"+
        "\10\35\3\uffff\1\35\1\102\1\103\1\35\1\105\1\35\6\uffff\1\35\1\110"+
        "\5\35\1\116\1\35\2\uffff\1\120\1\uffff\1\121\1\35\1\uffff\4\35\1"+
        "\127\1\uffff\1\35\2\uffff\5\35\1\uffff\2\35\1\140\5\35\1\uffff\4"+
        "\35\1\152\3\35\1\156\1\uffff\1\35\1\160\1\161\1\uffff\1\162\3\uffff";
    static final String DFA10_eofS =
        "\163\uffff";
    static final String DFA10_minS =
        "\1\11\1\75\1\142\1\151\1\141\1\162\13\uffff\1\53\1\144\1\156\1\165"+
        "\1\145\1\75\2\uffff\1\75\5\uffff\1\52\2\uffff\1\163\1\154\1\156"+
        "\1\162\1\147\1\156\1\151\1\163\3\uffff\1\145\2\52\1\145\1\52\1\164"+
        "\6\uffff\1\164\1\52\1\164\1\141\1\156\1\143\1\154\1\52\1\156\2\uffff"+
        "\1\52\1\uffff\1\52\1\162\1\uffff\1\141\1\164\1\141\1\164\1\52\1"+
        "\uffff\1\164\2\uffff\1\141\1\170\1\145\1\164\1\151\1\uffff\1\151"+
        "\1\143\1\52\1\147\1\165\1\157\2\164\1\uffff\1\151\1\162\1\156\1"+
        "\171\1\52\2\145\1\163\1\52\1\uffff\1\163\2\52\1\uffff\1\52\3\uffff";
    static final String DFA10_maxS =
        "\1\175\1\75\1\154\1\171\1\165\1\162\13\uffff\1\75\2\156\1\165\1"+
        "\145\1\75\2\uffff\1\75\5\uffff\1\57\2\uffff\1\163\1\154\1\156\1"+
        "\162\1\147\1\156\1\151\1\163\3\uffff\1\145\2\172\1\145\1\172\1\164"+
        "\6\uffff\1\164\1\172\1\164\1\141\1\156\1\143\1\154\1\172\1\156\2"+
        "\uffff\1\172\1\uffff\1\172\1\162\1\uffff\1\141\1\164\1\141\1\164"+
        "\1\172\1\uffff\1\164\2\uffff\1\141\1\170\1\145\1\164\1\151\1\uffff"+
        "\1\151\1\143\1\172\1\147\1\165\1\157\2\164\1\uffff\1\151\1\162\1"+
        "\156\1\171\1\172\2\145\1\163\1\172\1\uffff\1\163\2\172\1\uffff\1"+
        "\172\3\uffff";
    static final String DFA10_acceptS =
        "\6\uffff\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1\21\6"+
        "\uffff\1\34\1\37\1\uffff\1\45\1\46\1\47\1\50\1\51\1\uffff\1\36\1"+
        "\1\10\uffff\1\22\1\40\1\41\6\uffff\1\35\1\33\1\42\1\43\1\52\1\53"+
        "\11\uffff\1\31\1\44\1\uffff\1\27\2\uffff\1\25\5\uffff\1\6\1\uffff"+
        "\1\26\1\30\5\uffff\1\24\10\uffff\1\3\11\uffff\1\2\3\uffff\1\23\1"+
        "\uffff\1\32\1\5\1\4";
    static final String DFA10_specialS =
        "\163\uffff}>";
    static final String[] DFA10_transitionS = {
            "\2\36\25\uffff\1\36\1\1\1\34\3\uffff\1\7\1\uffff\1\11\1\12\2"+
            "\uffff\1\17\1\6\1\30\1\37\12\32\1\10\1\20\1\21\1\26\1\31\2\uffff"+
            "\32\35\1\15\1\33\1\16\1\uffff\1\35\1\uffff\1\2\4\35\1\4\2\35"+
            "\1\22\2\35\1\25\1\24\1\35\1\23\3\35\1\3\1\5\6\35\1\13\1\27\1"+
            "\14",
            "\1\40",
            "\1\42\11\uffff\1\43",
            "\1\46\12\uffff\1\45\4\uffff\1\44",
            "\1\50\23\uffff\1\47",
            "\1\51",
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
            "\1\52\21\uffff\1\53",
            "\1\55\1\uffff\1\57\7\uffff\1\56",
            "\1\60",
            "\1\61",
            "\1\62",
            "\1\63",
            "",
            "",
            "\1\65",
            "",
            "",
            "",
            "",
            "",
            "\1\70\4\uffff\1\67",
            "",
            "",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "",
            "",
            "",
            "\1\101",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\104",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\106",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\107",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\117",
            "",
            "",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\122",
            "",
            "\1\123",
            "\1\124",
            "\1\125",
            "\1\126",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\1\130",
            "",
            "",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "",
            "\1\136",
            "\1\137",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\141",
            "\1\142",
            "\1\143",
            "\1\144",
            "\1\145",
            "",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\151",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\153",
            "\1\154",
            "\1\155",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\1\157",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "\1\35\5\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
            "",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__104 | ABSTRACT | SYNTAX | STRATEGIES | FUNCTIONS | TRS | ARROW | AMPERCENT | COLON | LPAR | RPAR | LBRACE | RBRACE | LBRACKET | RBRACKET | COMMA | SEMICOLON | CHOICE | IDENTITY | FAIL | ALL | ONE | MU | LET | IN | SIGNATURE | EQUALS | ALT | DOUBLEEQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT | MLCOMMENT );";
        }
    }
 

}