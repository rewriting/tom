// $ANTLR 3.2 Sep 23, 2009 12:02:23 ./src/MyminiTom.g 2011-01-13 12:35:44

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class MyminiTomLexer extends Lexer {
    public static final int SEMICOLUMN=10;
    public static final int RIGHTBR=9;
    public static final int WS=13;
    public static final int A=11;
    public static final int B=12;
    public static final int T__14=14;
    public static final int RIGHTPAR=7;
    public static final int LEFTPAR=6;
    public static final int LEFTBR=8;
    public static final int PROGRAM=4;
    public static final int EOF=-1;
    public static final int CODE=5;

    // delegates
    // delegators

    public MyminiTomLexer() {;} 
    public MyminiTomLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public MyminiTomLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "./src/MyminiTom.g"; }

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/MyminiTom.g:3:7: ( 'a' )
            // ./src/MyminiTom.g:3:9: 'a'
            {
            match('a'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "LEFTPAR"
    public final void mLEFTPAR() throws RecognitionException {
        try {
            int _type = LEFTPAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/MyminiTom.g:25:12: ( '(' )
            // ./src/MyminiTom.g:25:14: '('
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
            // ./src/MyminiTom.g:26:12: ( ')' )
            // ./src/MyminiTom.g:26:14: ')'
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
            // ./src/MyminiTom.g:27:12: ( '{' )
            // ./src/MyminiTom.g:27:14: '{'
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
            // ./src/MyminiTom.g:28:12: ( '}' )
            // ./src/MyminiTom.g:28:14: '}'
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

    // $ANTLR start "SEMICOLUMN"
    public final void mSEMICOLUMN() throws RecognitionException {
        try {
            int _type = SEMICOLUMN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/MyminiTom.g:29:12: ( ';' )
            // ./src/MyminiTom.g:29:14: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMICOLUMN"

    // $ANTLR start "A"
    public final void mA() throws RecognitionException {
        try {
            int _type = A;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/MyminiTom.g:30:12: ( 'alice' )
            // ./src/MyminiTom.g:30:14: 'alice'
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
            // ./src/MyminiTom.g:31:12: ( 'bob' )
            // ./src/MyminiTom.g:31:14: 'bob'
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

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ./src/MyminiTom.g:33:4: ( ( '\\r' | '\\n' | '\\t' | ' ' )* )
            // ./src/MyminiTom.g:33:6: ( '\\r' | '\\n' | '\\t' | ' ' )*
            {
            // ./src/MyminiTom.g:33:6: ( '\\r' | '\\n' | '\\t' | ' ' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='\t' && LA1_0<='\n')||LA1_0=='\r'||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ./src/MyminiTom.g:
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
            	    break loop1;
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

    public void mTokens() throws RecognitionException {
        // ./src/MyminiTom.g:1:8: ( T__14 | LEFTPAR | RIGHTPAR | LEFTBR | RIGHTBR | SEMICOLUMN | A | B | WS )
        int alt2=9;
        alt2 = dfa2.predict(input);
        switch (alt2) {
            case 1 :
                // ./src/MyminiTom.g:1:10: T__14
                {
                mT__14(); 

                }
                break;
            case 2 :
                // ./src/MyminiTom.g:1:16: LEFTPAR
                {
                mLEFTPAR(); 

                }
                break;
            case 3 :
                // ./src/MyminiTom.g:1:24: RIGHTPAR
                {
                mRIGHTPAR(); 

                }
                break;
            case 4 :
                // ./src/MyminiTom.g:1:33: LEFTBR
                {
                mLEFTBR(); 

                }
                break;
            case 5 :
                // ./src/MyminiTom.g:1:40: RIGHTBR
                {
                mRIGHTBR(); 

                }
                break;
            case 6 :
                // ./src/MyminiTom.g:1:48: SEMICOLUMN
                {
                mSEMICOLUMN(); 

                }
                break;
            case 7 :
                // ./src/MyminiTom.g:1:59: A
                {
                mA(); 

                }
                break;
            case 8 :
                // ./src/MyminiTom.g:1:61: B
                {
                mB(); 

                }
                break;
            case 9 :
                // ./src/MyminiTom.g:1:63: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\1\10\1\12\11\uffff";
    static final String DFA2_eofS =
        "\13\uffff";
    static final String DFA2_minS =
        "\1\50\1\154\11\uffff";
    static final String DFA2_maxS =
        "\1\175\1\154\11\uffff";
    static final String DFA2_acceptS =
        "\2\uffff\1\2\1\3\1\4\1\5\1\6\1\10\1\11\1\7\1\1";
    static final String DFA2_specialS =
        "\13\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\2\1\3\21\uffff\1\6\45\uffff\1\1\1\7\30\uffff\1\4\1\uffff"+
            "\1\5",
            "\1\11",
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
            return "1:1: Tokens : ( T__14 | LEFTPAR | RIGHTPAR | LEFTBR | RIGHTBR | SEMICOLUMN | A | B | WS );";
        }
    }
 

}