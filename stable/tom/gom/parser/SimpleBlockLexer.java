// $ANTLR 3.1 /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g 2009-01-19 16:55:32

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
    public String getGrammarFileName() { return "/home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g"; }

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:50:8: ( '{' )
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:50:10: '{'
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
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:51:8: ( '}' )
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:51:10: '}'
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
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:8: ( '\"' ( ESC | ~ ( '\\\\' | '\"' ) )* '\"' )
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:10: '\"' ( ESC | ~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:14: ( ESC | ~ ( '\\\\' | '\"' ) )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\\') ) {
                    alt1=1;
                }
                else if ( ((LA1_0>='\u0000' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='[')||(LA1_0>=']' && LA1_0<='\uFFFE')) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:21: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
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
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:66:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | OctalESC )
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
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:66:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
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
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:67:7: OctalESC
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
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:3: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
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
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:7: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:12: ( '0' .. '3' )
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:13: '0' .. '3'
                    {
                    matchRange('0','3'); 

                    }

                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:23: ( '0' .. '7' )
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:24: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:34: ( '0' .. '7' )
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:35: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 2 :
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:7: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:12: ( '0' .. '7' )
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:13: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:23: ( '0' .. '7' )
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:24: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 3 :
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:74:7: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:74:12: ( '0' .. '7' )
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:74:13: '0' .. '7'
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
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:81:12: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:3: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); 

            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:8: (~ ( '\\n' | '\\r' ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\u0000' && LA4_0<='\t')||(LA4_0>='\u000B' && LA4_0<='\f')||(LA4_0>='\u000E' && LA4_0<='\uFFFE')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:9: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
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

            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:24: ( '\\r' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\r') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:24: '\\r'
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
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:87:3: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:87:5: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:87:10: ( options {greedy=false; } : . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='*') ) {
                    int LA6_1 = input.LA(2);

                    if ( (LA6_1=='/') ) {
                        alt6=2;
                    }
                    else if ( ((LA6_1>='\u0000' && LA6_1<='.')||(LA6_1>='0' && LA6_1<='\uFFFE')) ) {
                        alt6=1;
                    }


                }
                else if ( ((LA6_0>='\u0000' && LA6_0<=')')||(LA6_0>='+' && LA6_0<='\uFFFE')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:87:38: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
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

    // $ANTLR start "TARGET"
    public final void mTARGET() throws RecognitionException {
        try {
            int _type = TARGET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int text;

            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:91:8: ( (text=~ ( '/' | '{' | '}' | '\"' ) )+ )
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:91:10: (text=~ ( '/' | '{' | '}' | '\"' ) )+
            {
            // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:91:10: (text=~ ( '/' | '{' | '}' | '\"' ) )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\u0000' && LA7_0<='!')||(LA7_0>='#' && LA7_0<='.')||(LA7_0>='0' && LA7_0<='z')||LA7_0=='|'||(LA7_0>='~' && LA7_0<='\uFFFE')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:91:12: text=~ ( '/' | '{' | '}' | '\"' )
            	    {
            	    text= input.LA(1);
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='.')||(input.LA(1)>='0' && input.LA(1)<='z')||input.LA(1)=='|'||(input.LA(1)>='~' && input.LA(1)<='\uFFFE') ) {
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
        // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:8: ( LBRACE | RBRACE | STRING | SL_COMMENT | ML_COMMENT | TARGET )
        int alt8=6;
        int LA8_0 = input.LA(1);

        if ( (LA8_0=='{') ) {
            alt8=1;
        }
        else if ( (LA8_0=='}') ) {
            alt8=2;
        }
        else if ( (LA8_0=='\"') ) {
            alt8=3;
        }
        else if ( (LA8_0=='/') ) {
            int LA8_4 = input.LA(2);

            if ( (LA8_4=='/') ) {
                alt8=4;
            }
            else if ( (LA8_4=='*') ) {
                alt8=5;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 4, input);

                throw nvae;
            }
        }
        else if ( ((LA8_0>='\u0000' && LA8_0<='!')||(LA8_0>='#' && LA8_0<='.')||(LA8_0>='0' && LA8_0<='z')||LA8_0=='|'||(LA8_0>='~' && LA8_0<='\uFFFE')) ) {
            alt8=6;
        }
        else {
            NoViableAltException nvae =
                new NoViableAltException("", 8, 0, input);

            throw nvae;
        }
        switch (alt8) {
            case 1 :
                // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:10: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 2 :
                // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:17: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 3 :
                // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:24: STRING
                {
                mSTRING(); 

                }
                break;
            case 4 :
                // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:31: SL_COMMENT
                {
                mSL_COMMENT(); 

                }
                break;
            case 5 :
                // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:42: ML_COMMENT
                {
                mML_COMMENT(); 

                }
                break;
            case 6 :
                // /home/jcb/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:53: TARGET
                {
                mTARGET(); 

                }
                break;

        }

    }


 

}