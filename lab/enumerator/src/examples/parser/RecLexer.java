// $ANTLR 3.2 Sep 23, 2009 12:02:23 parser/Rec.g 2014-03-27 10:03:53

  package examples.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RecLexer extends Lexer {
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int Pair=25;
    public static final int T__45=45;
    public static final int ExpList=10;
    public static final int SeqExp=14;
    public static final int Table=24;
    public static final int False=12;
    public static final int SLCOMMENT=36;
    public static final int INT=34;
    public static final int While=18;
    public static final int ID=33;
    public static final int Equal=26;
    public static final int EOF=-1;
    public static final int Seq=22;
    public static final int Plus=32;
    public static final int OpExp=15;
    public static final int Minus=31;
    public static final int Assign=21;
    public static final int Num=16;
    public static final int WS=35;
    public static final int NotExp=11;
    public static final int Times=30;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int Print=20;
    public static final int Or=27;
    public static final int True=13;
    public static final int Id=17;
    public static final int MLCOMMENT=37;
    public static final int EmptyTable=23;
    public static final int If=19;
    public static final int And=28;
    public static final int Div=29;

    // delegates
    // delegators

    public RecLexer() {;} 
    public RecLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public RecLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "parser/Rec.g"; }

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:11:7: ( ';' )
            // parser/Rec.g:11:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:12:7: ( ':=' )
            // parser/Rec.g:12:9: ':='
            {
            match(":="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:13:7: ( 'print' )
            // parser/Rec.g:13:9: 'print'
            {
            match("print"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:14:7: ( '(' )
            // parser/Rec.g:14:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:15:7: ( ')' )
            // parser/Rec.g:15:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:16:7: ( '+' )
            // parser/Rec.g:16:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:17:7: ( '-' )
            // parser/Rec.g:17:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:18:7: ( '*' )
            // parser/Rec.g:18:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:19:7: ( '/' )
            // parser/Rec.g:19:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:20:7: ( ',' )
            // parser/Rec.g:20:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:49:5: ( ( '0' .. '9' ) ( '0' .. '9' )* )
            // parser/Rec.g:49:7: ( '0' .. '9' ) ( '0' .. '9' )*
            {
            // parser/Rec.g:49:7: ( '0' .. '9' )
            // parser/Rec.g:49:8: '0' .. '9'
            {
            matchRange('0','9'); 

            }

            // parser/Rec.g:49:17: ( '0' .. '9' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // parser/Rec.g:49:18: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:50:4: ( ( 'a' .. 'z' | 'A' .. 'Z' )+ )
            // parser/Rec.g:50:6: ( 'a' .. 'z' | 'A' .. 'Z' )+
            {
            // parser/Rec.g:50:6: ( 'a' .. 'z' | 'A' .. 'Z' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='A' && LA2_0<='Z')||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // parser/Rec.g:
            	    {
            	    if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


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
            // parser/Rec.g:51:4: ( ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) ) )
            // parser/Rec.g:51:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
            {
            // parser/Rec.g:51:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
            int alt4=3;
            switch ( input.LA(1) ) {
            case ' ':
                {
                alt4=1;
                }
                break;
            case '\t':
                {
                alt4=2;
                }
                break;
            case '\n':
            case '\r':
                {
                alt4=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // parser/Rec.g:51:7: ' '
                    {
                    match(' '); 

                    }
                    break;
                case 2 :
                    // parser/Rec.g:51:13: '\\t'
                    {
                    match('\t'); 

                    }
                    break;
                case 3 :
                    // parser/Rec.g:51:20: ( '\\r\\n' | '\\n' | '\\r' )
                    {
                    // parser/Rec.g:51:20: ( '\\r\\n' | '\\n' | '\\r' )
                    int alt3=3;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0=='\r') ) {
                        int LA3_1 = input.LA(2);

                        if ( (LA3_1=='\n') ) {
                            alt3=1;
                        }
                        else {
                            alt3=3;}
                    }
                    else if ( (LA3_0=='\n') ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 0, input);

                        throw nvae;
                    }
                    switch (alt3) {
                        case 1 :
                            // parser/Rec.g:51:22: '\\r\\n'
                            {
                            match("\r\n"); 


                            }
                            break;
                        case 2 :
                            // parser/Rec.g:51:31: '\\n'
                            {
                            match('\n'); 

                            }
                            break;
                        case 3 :
                            // parser/Rec.g:51:38: '\\r'
                            {
                            match('\r'); 

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
    // $ANTLR end "WS"

    // $ANTLR start "SLCOMMENT"
    public final void mSLCOMMENT() throws RecognitionException {
        try {
            int _type = SLCOMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // parser/Rec.g:52:11: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // parser/Rec.g:52:13: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // parser/Rec.g:52:18: (~ ( '\\n' | '\\r' ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\u0000' && LA5_0<='\t')||(LA5_0>='\u000B' && LA5_0<='\f')||(LA5_0>='\u000E' && LA5_0<='\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // parser/Rec.g:52:19: ~ ( '\\n' | '\\r' )
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
            	    break loop5;
                }
            } while (true);

            // parser/Rec.g:52:34: ( '\\n' | '\\r' ( '\\n' )? )?
            int alt7=3;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='\n') ) {
                alt7=1;
            }
            else if ( (LA7_0=='\r') ) {
                alt7=2;
            }
            switch (alt7) {
                case 1 :
                    // parser/Rec.g:52:35: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // parser/Rec.g:52:40: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // parser/Rec.g:52:44: ( '\\n' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0=='\n') ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // parser/Rec.g:52:45: '\\n'
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
            // parser/Rec.g:53:11: ( '/*' ( . )* '*/' )
            // parser/Rec.g:53:13: '/*' ( . )* '*/'
            {
            match("/*"); 

            // parser/Rec.g:53:18: ( . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='*') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='/') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1>='\u0000' && LA8_1<='.')||(LA8_1>='0' && LA8_1<='\uFFFF')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0>='\u0000' && LA8_0<=')')||(LA8_0>='+' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // parser/Rec.g:53:18: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
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
        // parser/Rec.g:1:8: ( T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | INT | ID | WS | SLCOMMENT | MLCOMMENT )
        int alt9=15;
        alt9 = dfa9.predict(input);
        switch (alt9) {
            case 1 :
                // parser/Rec.g:1:10: T__38
                {
                mT__38(); 

                }
                break;
            case 2 :
                // parser/Rec.g:1:16: T__39
                {
                mT__39(); 

                }
                break;
            case 3 :
                // parser/Rec.g:1:22: T__40
                {
                mT__40(); 

                }
                break;
            case 4 :
                // parser/Rec.g:1:28: T__41
                {
                mT__41(); 

                }
                break;
            case 5 :
                // parser/Rec.g:1:34: T__42
                {
                mT__42(); 

                }
                break;
            case 6 :
                // parser/Rec.g:1:40: T__43
                {
                mT__43(); 

                }
                break;
            case 7 :
                // parser/Rec.g:1:46: T__44
                {
                mT__44(); 

                }
                break;
            case 8 :
                // parser/Rec.g:1:52: T__45
                {
                mT__45(); 

                }
                break;
            case 9 :
                // parser/Rec.g:1:58: T__46
                {
                mT__46(); 

                }
                break;
            case 10 :
                // parser/Rec.g:1:64: T__47
                {
                mT__47(); 

                }
                break;
            case 11 :
                // parser/Rec.g:1:70: INT
                {
                mINT(); 

                }
                break;
            case 12 :
                // parser/Rec.g:1:74: ID
                {
                mID(); 

                }
                break;
            case 13 :
                // parser/Rec.g:1:77: WS
                {
                mWS(); 

                }
                break;
            case 14 :
                // parser/Rec.g:1:80: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;
            case 15 :
                // parser/Rec.g:1:90: MLCOMMENT
                {
                mMLCOMMENT(); 

                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA9_eotS =
        "\3\uffff\1\14\5\uffff\1\21\4\uffff\1\14\3\uffff\2\14\1\25\1\uffff";
    static final String DFA9_eofS =
        "\26\uffff";
    static final String DFA9_minS =
        "\1\11\2\uffff\1\162\5\uffff\1\52\4\uffff\1\151\3\uffff\1\156\1\164"+
        "\1\101\1\uffff";
    static final String DFA9_maxS =
        "\1\172\2\uffff\1\162\5\uffff\1\57\4\uffff\1\151\3\uffff\1\156\1"+
        "\164\1\172\1\uffff";
    static final String DFA9_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\4\1\5\1\6\1\7\1\10\1\uffff\1\12\1\13"+
        "\1\14\1\15\1\uffff\1\16\1\17\1\11\3\uffff\1\3";
    static final String DFA9_specialS =
        "\26\uffff}>";
    static final String[] DFA9_transitionS = {
            "\2\15\2\uffff\1\15\22\uffff\1\15\7\uffff\1\4\1\5\1\10\1\6\1"+
            "\12\1\7\1\uffff\1\11\12\13\1\2\1\1\5\uffff\32\14\6\uffff\17"+
            "\14\1\3\12\14",
            "",
            "",
            "\1\16",
            "",
            "",
            "",
            "",
            "",
            "\1\20\4\uffff\1\17",
            "",
            "",
            "",
            "",
            "\1\22",
            "",
            "",
            "",
            "\1\23",
            "\1\24",
            "\32\14\6\uffff\32\14",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | INT | ID | WS | SLCOMMENT | MLCOMMENT );";
        }
    }
 

}