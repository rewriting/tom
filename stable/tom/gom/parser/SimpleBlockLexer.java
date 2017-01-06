// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g 2016-11-24 18:10:51

  package tom.gom.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SimpleBlockLexer extends Lexer {
    public static final int ML_COMMENT=10;
    public static final int RBRACE=6;
    public static final int OctalESC=8;
    public static final int ESC=7;
    public static final int LBRACE=5;
    public static final int SL_COMMENT=9;
    public static final int TARGET=11;
    public static final int EOF=-1;
    public static final int STRING=4;

      public static int nesting = 0;


    // delegates
    // delegators

    public SimpleBlockLexer() {;} 
    public SimpleBlockLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SimpleBlockLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g"; }

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:50:8: ( '{' )
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:50:10: '{'
            {
            match('{'); 
             nesting++; 

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
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:51:8: ( '}' )
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:51:10: '}'
            {
            match('}'); 

                if ( nesting<=0 ) {
                  emit(Token.EOF_TOKEN);
                }
                else {
                  nesting--;
                }
              

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACE"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:62:8: ( '\"' ( ESC | ~ ( '\\\\' | '\"' ) )* '\"' )
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:62:10: '\"' ( ESC | ~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:62:14: ( ESC | ~ ( '\\\\' | '\"' ) )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\\') ) {
                    alt1=1;
                }
                else if ( ((LA1_0>='\u0000' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='[')||(LA1_0>=']' && LA1_0<='\uFFFF')) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:62:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:62:21: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
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

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "ESC"
    public final void mESC() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:66:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | OctalESC )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='\\') ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1=='\"'||LA2_1=='\''||LA2_1=='\\'||LA2_1=='b'||LA2_1=='f'||LA2_1=='n'||LA2_1=='r'||LA2_1=='t') ) {
                    alt2=1;
                }
                else if ( ((LA2_1>='0' && LA2_1<='7')) ) {
                    alt2=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:66:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
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
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:67:7: OctalESC
                    {
                    mOctalESC(); 

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "ESC"

    // $ANTLR start "OctalESC"
    public final void mOctalESC() throws RecognitionException {
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:72:3: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt3=3;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\\') ) {
                int LA3_1 = input.LA(2);

                if ( ((LA3_1>='0' && LA3_1<='3')) ) {
                    int LA3_2 = input.LA(3);

                    if ( ((LA3_2>='0' && LA3_2<='7')) ) {
                        int LA3_4 = input.LA(4);

                        if ( ((LA3_4>='0' && LA3_4<='7')) ) {
                            alt3=1;
                        }
                        else {
                            alt3=2;}
                    }
                    else {
                        alt3=3;}
                }
                else if ( ((LA3_1>='4' && LA3_1<='7')) ) {
                    int LA3_3 = input.LA(3);

                    if ( ((LA3_3>='0' && LA3_3<='7')) ) {
                        alt3=2;
                    }
                    else {
                        alt3=3;}
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:72:7: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:72:12: ( '0' .. '3' )
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:72:13: '0' .. '3'
                    {
                    matchRange('0','3'); 

                    }

                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:72:23: ( '0' .. '7' )
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:72:24: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:72:34: ( '0' .. '7' )
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:72:35: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:73:7: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:73:12: ( '0' .. '7' )
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:73:13: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:73:23: ( '0' .. '7' )
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:73:24: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:74:7: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:74:12: ( '0' .. '7' )
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:74:13: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "OctalESC"

    // $ANTLR start "SL_COMMENT"
    public final void mSL_COMMENT() throws RecognitionException {
        try {
            int _type = SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:81:12: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:82:3: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); 

            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:82:8: (~ ( '\\n' | '\\r' ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\u0000' && LA4_0<='\t')||(LA4_0>='\u000B' && LA4_0<='\f')||(LA4_0>='\u000E' && LA4_0<='\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:82:9: ~ ( '\\n' | '\\r' )
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
            	    break loop4;
                }
            } while (true);

            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:82:24: ( '\\r' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\r') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:82:24: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
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
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:87:3: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:87:5: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:87:10: ( options {greedy=false; } : . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='*') ) {
                    int LA6_1 = input.LA(2);

                    if ( (LA6_1=='/') ) {
                        alt6=2;
                    }
                    else if ( ((LA6_1>='\u0000' && LA6_1<='.')||(LA6_1>='0' && LA6_1<='\uFFFF')) ) {
                        alt6=1;
                    }


                }
                else if ( ((LA6_0>='\u0000' && LA6_0<=')')||(LA6_0>='+' && LA6_0<='\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:87:38: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ML_COMMENT"

    // $ANTLR start "TARGET"
    public final void mTARGET() throws RecognitionException {
        try {
            int _type = TARGET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int text;

            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:91:8: ( (text=~ ( '{' | '}' | '\"' ) )+ )
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:91:10: (text=~ ( '{' | '}' | '\"' ) )+
            {
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:91:10: (text=~ ( '{' | '}' | '\"' ) )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\u0000' && LA7_0<='!')||(LA7_0>='#' && LA7_0<='z')||LA7_0=='|'||(LA7_0>='~' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:91:12: text=~ ( '{' | '}' | '\"' )
            	    {
            	    text= input.LA(1);
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='z')||input.LA(1)=='|'||(input.LA(1)>='~' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TARGET"

    public void mTokens() throws RecognitionException {
        // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:1:8: ( LBRACE | RBRACE | STRING | SL_COMMENT | ML_COMMENT | TARGET )
        int alt8=6;
        alt8 = dfa8.predict(input);
        switch (alt8) {
            case 1 :
                // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:1:10: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 2 :
                // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:1:17: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 3 :
                // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:1:24: STRING
                {
                mSTRING(); 

                }
                break;
            case 4 :
                // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:1:31: SL_COMMENT
                {
                mSL_COMMENT(); 

                }
                break;
            case 5 :
                // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:1:42: ML_COMMENT
                {
                mML_COMMENT(); 

                }
                break;
            case 6 :
                // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:1:53: TARGET
                {
                mTARGET(); 

                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    static final String DFA8_eotS =
        "\4\uffff\1\5\1\uffff\4\5\1\uffff\1\12\1\5\1\uffff\1\5\1\15";
    static final String DFA8_eofS =
        "\20\uffff";
    static final String DFA8_minS =
        "\1\0\3\uffff\1\52\1\uffff\3\0\1\12\1\uffff\2\0\1\uffff\2\0";
    static final String DFA8_maxS =
        "\1\uffff\3\uffff\1\57\1\uffff\3\uffff\1\12\1\uffff\2\uffff\1\uffff"+
        "\2\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\uffff\1\6\4\uffff\1\4\2\uffff\1\5\2\uffff";
    static final String DFA8_specialS =
        "\1\3\5\uffff\1\5\1\1\1\2\2\uffff\1\6\1\0\1\uffff\1\4\1\7}>";
    static final String[] DFA8_transitionS = {
            "\42\5\1\3\14\5\1\4\113\5\1\1\1\5\1\2\uff82\5",
            "",
            "",
            "",
            "\1\7\4\uffff\1\6",
            "",
            "\12\10\1\13\2\10\1\11\24\10\1\12\130\10\1\12\1\10\1\12\uff82"+
            "\10",
            "\42\16\1\15\7\16\1\14\120\16\1\15\1\16\1\15\uff82\16",
            "\12\10\1\13\2\10\1\11\24\10\1\12\130\10\1\12\1\10\1\12\uff82"+
            "\10",
            "\1\13",
            "",
            "\42\5\1\uffff\130\5\1\uffff\1\5\1\uffff\uff82\5",
            "\42\16\1\15\7\16\1\14\4\16\1\17\113\16\1\15\1\16\1\15\uff82"+
            "\16",
            "",
            "\42\16\1\15\7\16\1\14\120\16\1\15\1\16\1\15\uff82\16",
            "\42\16\1\uffff\7\16\1\14\120\16\1\uffff\1\16\1\uffff\uff82"+
            "\16"
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
            return "1:1: Tokens : ( LBRACE | RBRACE | STRING | SL_COMMENT | ML_COMMENT | TARGET );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA8_12 = input.LA(1);

                        s = -1;
                        if ( (LA8_12=='/') ) {s = 15;}

                        else if ( (LA8_12=='*') ) {s = 12;}

                        else if ( ((LA8_12>='\u0000' && LA8_12<='!')||(LA8_12>='#' && LA8_12<=')')||(LA8_12>='+' && LA8_12<='.')||(LA8_12>='0' && LA8_12<='z')||LA8_12=='|'||(LA8_12>='~' && LA8_12<='\uFFFF')) ) {s = 14;}

                        else if ( (LA8_12=='\"'||LA8_12=='{'||LA8_12=='}') ) {s = 13;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA8_7 = input.LA(1);

                        s = -1;
                        if ( (LA8_7=='*') ) {s = 12;}

                        else if ( (LA8_7=='\"'||LA8_7=='{'||LA8_7=='}') ) {s = 13;}

                        else if ( ((LA8_7>='\u0000' && LA8_7<='!')||(LA8_7>='#' && LA8_7<=')')||(LA8_7>='+' && LA8_7<='z')||LA8_7=='|'||(LA8_7>='~' && LA8_7<='\uFFFF')) ) {s = 14;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA8_8 = input.LA(1);

                        s = -1;
                        if ( (LA8_8=='\r') ) {s = 9;}

                        else if ( (LA8_8=='\n') ) {s = 11;}

                        else if ( ((LA8_8>='\u0000' && LA8_8<='\t')||(LA8_8>='\u000B' && LA8_8<='\f')||(LA8_8>='\u000E' && LA8_8<='!')||(LA8_8>='#' && LA8_8<='z')||LA8_8=='|'||(LA8_8>='~' && LA8_8<='\uFFFF')) ) {s = 8;}

                        else if ( (LA8_8=='\"'||LA8_8=='{'||LA8_8=='}') ) {s = 10;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA8_0 = input.LA(1);

                        s = -1;
                        if ( (LA8_0=='{') ) {s = 1;}

                        else if ( (LA8_0=='}') ) {s = 2;}

                        else if ( (LA8_0=='\"') ) {s = 3;}

                        else if ( (LA8_0=='/') ) {s = 4;}

                        else if ( ((LA8_0>='\u0000' && LA8_0<='!')||(LA8_0>='#' && LA8_0<='.')||(LA8_0>='0' && LA8_0<='z')||LA8_0=='|'||(LA8_0>='~' && LA8_0<='\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA8_14 = input.LA(1);

                        s = -1;
                        if ( (LA8_14=='*') ) {s = 12;}

                        else if ( ((LA8_14>='\u0000' && LA8_14<='!')||(LA8_14>='#' && LA8_14<=')')||(LA8_14>='+' && LA8_14<='z')||LA8_14=='|'||(LA8_14>='~' && LA8_14<='\uFFFF')) ) {s = 14;}

                        else if ( (LA8_14=='\"'||LA8_14=='{'||LA8_14=='}') ) {s = 13;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA8_6 = input.LA(1);

                        s = -1;
                        if ( ((LA8_6>='\u0000' && LA8_6<='\t')||(LA8_6>='\u000B' && LA8_6<='\f')||(LA8_6>='\u000E' && LA8_6<='!')||(LA8_6>='#' && LA8_6<='z')||LA8_6=='|'||(LA8_6>='~' && LA8_6<='\uFFFF')) ) {s = 8;}

                        else if ( (LA8_6=='\r') ) {s = 9;}

                        else if ( (LA8_6=='\"'||LA8_6=='{'||LA8_6=='}') ) {s = 10;}

                        else if ( (LA8_6=='\n') ) {s = 11;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA8_11 = input.LA(1);

                        s = -1;
                        if ( ((LA8_11>='\u0000' && LA8_11<='!')||(LA8_11>='#' && LA8_11<='z')||LA8_11=='|'||(LA8_11>='~' && LA8_11<='\uFFFF')) ) {s = 5;}

                        else s = 10;

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA8_15 = input.LA(1);

                        s = -1;
                        if ( (LA8_15=='*') ) {s = 12;}

                        else if ( ((LA8_15>='\u0000' && LA8_15<='!')||(LA8_15>='#' && LA8_15<=')')||(LA8_15>='+' && LA8_15<='z')||LA8_15=='|'||(LA8_15>='~' && LA8_15<='\uFFFF')) ) {s = 14;}

                        else s = 13;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 8, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}