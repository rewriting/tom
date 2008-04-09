// $ANTLR 3.0 /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g 2008-04-09 11:04:37

  package tom.gom.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SimpleBlockLexer extends Lexer {
    public static final int RBRACE=6;
    public static final int OctalESC=8;
    public static final int EOF=-1;
    public static final int TARGET=12;
    public static final int STRING=4;
    public static final int Tokens=13;
    public static final int COMMENT=11;
    public static final int ML_COMMENT=10;
    public static final int ESC=7;
    public static final int SL_COMMENT=9;
    public static final int LBRACE=5;

      public static int nesting = 0;

    public SimpleBlockLexer() {;} 
    public SimpleBlockLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g"; }

    // $ANTLR start LBRACE
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:50:10: ( '{' )
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:50:10: '{'
            {
            match('{'); 
             nesting++; 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LBRACE

    // $ANTLR start RBRACE
    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:51:10: ( '}' )
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:51:10: '}'
            {
            match('}'); 

                if ( nesting<=0 ) {
                  emit(Token.EOF_TOKEN);
                }
                else {
                  nesting--;
                }
              

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RBRACE

    // $ANTLR start STRING
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:10: ( '\"' ( ESC | ~ ( '\\\\' | '\"' ) )* '\"' )
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:10: '\"' ( ESC | ~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:14: ( ESC | ~ ( '\\\\' | '\"' ) )*
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
            	    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:62:21: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match('\"'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STRING

    // $ANTLR start ESC
    public final void mESC() throws RecognitionException {
        try {
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:66:9: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | OctalESC )
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
                        new NoViableAltException("65:1: fragment ESC : ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | OctalESC );", 2, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("65:1: fragment ESC : ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | OctalESC );", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:66:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 
                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:67:7: OctalESC
                    {
                    mOctalESC(); 

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end ESC

    // $ANTLR start OctalESC
    public final void mOctalESC() throws RecognitionException {
        try {
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:7: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
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
                        new NoViableAltException("70:1: fragment OctalESC : ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) );", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("70:1: fragment OctalESC : ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:7: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:12: ( '0' .. '3' )
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:13: '0' .. '3'
                    {
                    matchRange('0','3'); 

                    }

                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:23: ( '0' .. '7' )
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:24: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:34: ( '0' .. '7' )
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:72:35: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:7: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:12: ( '0' .. '7' )
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:13: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:23: ( '0' .. '7' )
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:73:24: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 3 :
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:74:7: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:74:12: ( '0' .. '7' )
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:74:13: '0' .. '7'
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
    // $ANTLR end OctalESC

    // $ANTLR start COMMENT
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:77:11: ( ( SL_COMMENT | ML_COMMENT ) )
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:77:11: ( SL_COMMENT | ML_COMMENT )
            {
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:77:11: ( SL_COMMENT | ML_COMMENT )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='/') ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1=='/') ) {
                    alt4=1;
                }
                else if ( (LA4_1=='*') ) {
                    alt4=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("77:11: ( SL_COMMENT | ML_COMMENT )", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("77:11: ( SL_COMMENT | ML_COMMENT )", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:77:12: SL_COMMENT
                    {
                    mSL_COMMENT(); 

                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:77:25: ML_COMMENT
                    {
                    mML_COMMENT(); 

                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMENT

    // $ANTLR start SL_COMMENT
    public final void mSL_COMMENT() throws RecognitionException {
        try {
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:3: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:3: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:8: (~ ( '\\n' | '\\r' ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\u0000' && LA5_0<='\t')||(LA5_0>='\u000B' && LA5_0<='\f')||(LA5_0>='\u000E' && LA5_0<='\uFFFE')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:9: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:24: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:25: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:30: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:34: ( '\\n' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0=='\n') ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:82:35: '\\n'
                            {
                            match('\n'); 

                            }
                            break;

                    }


                    }
                    break;

            }

            channel=HIDDEN;

            }

        }
        finally {
        }
    }
    // $ANTLR end SL_COMMENT

    // $ANTLR start ML_COMMENT
    public final void mML_COMMENT() throws RecognitionException {
        try {
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:88:5: ( '/*' ( . )* '*/' )
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:88:5: '/*' ( . )* '*/'
            {
            match("/*"); 

            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:88:10: ( . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='*') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='/') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1>='\u0000' && LA8_1<='.')||(LA8_1>='0' && LA8_1<='\uFFFE')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0>='\u0000' && LA8_0<=')')||(LA8_0>='+' && LA8_0<='\uFFFE')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:88:10: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            match("*/"); 

            channel=HIDDEN;

            }

        }
        finally {
        }
    }
    // $ANTLR end ML_COMMENT

    // $ANTLR start TARGET
    public final void mTARGET() throws RecognitionException {
        try {
            int _type = TARGET;
            int text;

            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:92:10: ( (text=~ ( '/' | '{' | '}' | '\"' ) )+ )
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:92:10: (text=~ ( '/' | '{' | '}' | '\"' ) )+
            {
            // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:92:10: (text=~ ( '/' | '{' | '}' | '\"' ) )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='\u0000' && LA9_0<='!')||(LA9_0>='#' && LA9_0<='.')||(LA9_0>='0' && LA9_0<='z')||LA9_0=='|'||(LA9_0>='~' && LA9_0<='\uFFFE')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:92:12: text=~ ( '/' | '{' | '}' | '\"' )
            	    {
            	    text= input.LA(1);
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='.')||(input.LA(1)>='0' && input.LA(1)<='z')||input.LA(1)=='|'||(input.LA(1)>='~' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TARGET

    public void mTokens() throws RecognitionException {
        // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:10: ( LBRACE | RBRACE | STRING | COMMENT | TARGET )
        int alt10=5;
        int LA10_0 = input.LA(1);

        if ( (LA10_0=='{') ) {
            alt10=1;
        }
        else if ( (LA10_0=='}') ) {
            alt10=2;
        }
        else if ( (LA10_0=='\"') ) {
            alt10=3;
        }
        else if ( (LA10_0=='/') ) {
            alt10=4;
        }
        else if ( ((LA10_0>='\u0000' && LA10_0<='!')||(LA10_0>='#' && LA10_0<='.')||(LA10_0>='0' && LA10_0<='z')||LA10_0=='|'||(LA10_0>='~' && LA10_0<='\uFFFE')) ) {
            alt10=5;
        }
        else {
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( LBRACE | RBRACE | STRING | COMMENT | TARGET );", 10, 0, input);

            throw nvae;
        }
        switch (alt10) {
            case 1 :
                // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:10: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 2 :
                // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:17: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 3 :
                // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:24: STRING
                {
                mSTRING(); 

                }
                break;
            case 4 :
                // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:31: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 5 :
                // /Users/pem/workspace/jtom/src/tom/gom/parser/SimpleBlock.g:1:39: TARGET
                {
                mTARGET(); 

                }
                break;

        }

    }


 

}