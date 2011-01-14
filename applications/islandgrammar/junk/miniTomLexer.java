// $ANTLR 3.2 Sep 23, 2009 12:02:23 ./src/miniTom.g 2010-12-13 17:43:08

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class miniTomLexer extends Lexer {
    public static final int RIGHTBR=7;
    public static final int A=9;
    public static final int B=10;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int RIGHTPAR=5;
    public static final int BLANK=11;
    public static final int LEFTPAR=4;
    public static final int LEFTBR=6;
    public static final int EOF=-1;
    public static final int CODE=8;

    // delegates
    // delegators

    public miniTomLexer() {;} 
    public miniTomLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public miniTomLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "./src/miniTom.g"; }

    // $ANTLR start "LEFTPAR"
    public final void mLEFTPAR() throws RecognitionException {
        try {
            int _type = LEFTPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/miniTom.g:3:9: ( '(' )
            // ./src/miniTom.g:3:11: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTPAR"

    // $ANTLR start "RIGHTPAR"
    public final void mRIGHTPAR() throws RecognitionException {
        try {
            int _type = RIGHTPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/miniTom.g:4:10: ( ')' )
            // ./src/miniTom.g:4:12: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTPAR"

    // $ANTLR start "LEFTBR"
    public final void mLEFTBR() throws RecognitionException {
        try {
            int _type = LEFTBR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/miniTom.g:5:8: ( '{' )
            // ./src/miniTom.g:5:10: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTBR"

    // $ANTLR start "RIGHTBR"
    public final void mRIGHTBR() throws RecognitionException {
        try {
            int _type = RIGHTBR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/miniTom.g:6:9: ( '}' )
            // ./src/miniTom.g:6:11: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTBR"

    // $ANTLR start "A"
    public final void mA() throws RecognitionException {
        try {
            int _type = A;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/miniTom.g:7:3: ( 'alice' )
            // ./src/miniTom.g:7:5: 'alice'
            {
            match("alice"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "A"

    // $ANTLR start "B"
    public final void mB() throws RecognitionException {
        try {
            int _type = B;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/miniTom.g:8:3: ( 'bob' )
            // ./src/miniTom.g:8:5: 'bob'
            {
            match("bob"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "B"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/miniTom.g:9:7: ( ';' )
            // ./src/miniTom.g:9:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/miniTom.g:10:7: ( 'a' )
            // ./src/miniTom.g:10:9: 'a'
            {
            match('a'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "BLANK"
    public final void mBLANK() throws RecognitionException {
        try {
            int _type = BLANK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/miniTom.g:30:2: ( ( '\\r' | '\\n' | '\\r\\n' | '\\t' | ' ' ) )
            // ./src/miniTom.g:30:4: ( '\\r' | '\\n' | '\\r\\n' | '\\t' | ' ' )
            {
            // ./src/miniTom.g:30:4: ( '\\r' | '\\n' | '\\r\\n' | '\\t' | ' ' )
            int alt1=5;
            switch ( input.LA(1) ) {
            case '\r':
                {
                int LA1_1 = input.LA(2);

                if ( (LA1_1=='\n') ) {
                    alt1=3;
                }
                else {
                    alt1=1;}
                }
                break;
            case '\n':
                {
                alt1=2;
                }
                break;
            case '\t':
                {
                alt1=4;
                }
                break;
            case ' ':
                {
                alt1=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // ./src/miniTom.g:30:5: '\\r'
                    {
                    match('\r'); 

                    }
                    break;
                case 2 :
                    // ./src/miniTom.g:30:12: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 3 :
                    // ./src/miniTom.g:30:19: '\\r\\n'
                    {
                    match("\r\n"); 


                    }
                    break;
                case 4 :
                    // ./src/miniTom.g:30:29: '\\t'
                    {
                    match('\t'); 

                    }
                    break;
                case 5 :
                    // ./src/miniTom.g:30:36: ' '
                    {
                    match(' '); 

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
    // $ANTLR end "BLANK"

    public void mTokens() throws RecognitionException {
        // ./src/miniTom.g:1:8: ( LEFTPAR | RIGHTPAR | LEFTBR | RIGHTBR | A | B | T__12 | T__13 | BLANK )
        int alt2=9;
        alt2 = dfa2.predict(input);
        switch (alt2) {
            case 1 :
                // ./src/miniTom.g:1:10: LEFTPAR
                {
                mLEFTPAR(); 

                }
                break;
            case 2 :
                // ./src/miniTom.g:1:18: RIGHTPAR
                {
                mRIGHTPAR(); 

                }
                break;
            case 3 :
                // ./src/miniTom.g:1:27: LEFTBR
                {
                mLEFTBR(); 

                }
                break;
            case 4 :
                // ./src/miniTom.g:1:34: RIGHTBR
                {
                mRIGHTBR(); 

                }
                break;
            case 5 :
                // ./src/miniTom.g:1:42: A
                {
                mA(); 

                }
                break;
            case 6 :
                // ./src/miniTom.g:1:44: B
                {
                mB(); 

                }
                break;
            case 7 :
                // ./src/miniTom.g:1:46: T__12
                {
                mT__12(); 

                }
                break;
            case 8 :
                // ./src/miniTom.g:1:52: T__13
                {
                mT__13(); 

                }
                break;
            case 9 :
                // ./src/miniTom.g:1:58: BLANK
                {
                mBLANK(); 

                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\5\uffff\1\12\5\uffff";
    static final String DFA2_eofS =
        "\13\uffff";
    static final String DFA2_minS =
        "\1\11\4\uffff\1\154\5\uffff";
    static final String DFA2_maxS =
        "\1\175\4\uffff\1\154\5\uffff";
    static final String DFA2_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\uffff\1\6\1\7\1\11\1\5\1\10";
    static final String DFA2_specialS =
        "\13\uffff}>";
    static final String[] DFA2_transitionS = {
            "\2\10\2\uffff\1\10\22\uffff\1\10\7\uffff\1\1\1\2\21\uffff\1"+
            "\7\45\uffff\1\5\1\6\30\uffff\1\3\1\uffff\1\4",
            "",
            "",
            "",
            "",
            "\1\11",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( LEFTPAR | RIGHTPAR | LEFTBR | RIGHTBR | A | B | T__12 | T__13 | BLANK );";
        }
    }
 

}