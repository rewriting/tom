// $ANTLR 3.0 ../src/tom/gom/expander/rule/Rule.g 2007-07-16 10:32:48

  package tom.gom.expander.rule;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RuleLexer extends Lexer {
    public static final int CONDGEQ=14;
    public static final int NOTEQUALS=20;
    public static final int RPAR=28;
    public static final int CONDLT=13;
    public static final int WS=34;
    public static final int LPAR=27;
    public static final int ARROW=17;
    public static final int STRING=31;
    public static final int LT=22;
    public static final int LEQ=21;
    public static final int GT=24;
    public static final int CONDGT=15;
    public static final int RULE=5;
    public static final int RULELIST=4;
    public static final int GEQ=23;
    public static final int CONDEQUALS=10;
    public static final int ESC=33;
    public static final int LAB=8;
    public static final int DOT=25;
    public static final int EQUALS=19;
    public static final int CONDTERM=9;
    public static final int SLCOMMENT=35;
    public static final int CONDNOTEQUALS=11;
    public static final int CONDMETHOD=16;
    public static final int IF=18;
    public static final int INT=30;
    public static final int CONDLEQ=12;
    public static final int EOF=-1;
    public static final int Tokens=36;
    public static final int CONDRULE=6;
    public static final int COLON=32;
    public static final int APPL=7;
    public static final int COMA=29;
    public static final int ID=26;
    public RuleLexer() {;} 
    public RuleLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "../src/tom/gom/expander/rule/Rule.g"; }

    // $ANTLR start ARROW
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            // ../src/tom/gom/expander/rule/Rule.g:87:9: ( '->' )
            // ../src/tom/gom/expander/rule/Rule.g:87:9: '->'
            {
            match("->"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ARROW

    // $ANTLR start COLON
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            // ../src/tom/gom/expander/rule/Rule.g:88:9: ( ':' )
            // ../src/tom/gom/expander/rule/Rule.g:88:9: ':'
            {
            match(':'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COLON

    // $ANTLR start LPAR
    public final void mLPAR() throws RecognitionException {
        try {
            int _type = LPAR;
            // ../src/tom/gom/expander/rule/Rule.g:89:8: ( '(' )
            // ../src/tom/gom/expander/rule/Rule.g:89:8: '('
            {
            match('('); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LPAR

    // $ANTLR start RPAR
    public final void mRPAR() throws RecognitionException {
        try {
            int _type = RPAR;
            // ../src/tom/gom/expander/rule/Rule.g:90:8: ( ')' )
            // ../src/tom/gom/expander/rule/Rule.g:90:8: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RPAR

    // $ANTLR start COMA
    public final void mCOMA() throws RecognitionException {
        try {
            int _type = COMA;
            // ../src/tom/gom/expander/rule/Rule.g:91:8: ( ',' )
            // ../src/tom/gom/expander/rule/Rule.g:91:8: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMA

    // $ANTLR start EQUALS
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            // ../src/tom/gom/expander/rule/Rule.g:92:10: ( '==' )
            // ../src/tom/gom/expander/rule/Rule.g:92:10: '=='
            {
            match("=="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EQUALS

    // $ANTLR start NOTEQUALS
    public final void mNOTEQUALS() throws RecognitionException {
        try {
            int _type = NOTEQUALS;
            // ../src/tom/gom/expander/rule/Rule.g:93:13: ( '!=' )
            // ../src/tom/gom/expander/rule/Rule.g:93:13: '!='
            {
            match("!="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NOTEQUALS

    // $ANTLR start DOT
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            // ../src/tom/gom/expander/rule/Rule.g:94:7: ( '.' )
            // ../src/tom/gom/expander/rule/Rule.g:94:7: '.'
            {
            match('.'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DOT

    // $ANTLR start LEQ
    public final void mLEQ() throws RecognitionException {
        try {
            int _type = LEQ;
            // ../src/tom/gom/expander/rule/Rule.g:95:7: ( '<=' )
            // ../src/tom/gom/expander/rule/Rule.g:95:7: '<='
            {
            match("<="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LEQ

    // $ANTLR start LT
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            // ../src/tom/gom/expander/rule/Rule.g:96:6: ( '<' )
            // ../src/tom/gom/expander/rule/Rule.g:96:6: '<'
            {
            match('<'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LT

    // $ANTLR start GEQ
    public final void mGEQ() throws RecognitionException {
        try {
            int _type = GEQ;
            // ../src/tom/gom/expander/rule/Rule.g:97:7: ( '>=' )
            // ../src/tom/gom/expander/rule/Rule.g:97:7: '>='
            {
            match(">="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end GEQ

    // $ANTLR start GT
    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            // ../src/tom/gom/expander/rule/Rule.g:98:6: ( '>' )
            // ../src/tom/gom/expander/rule/Rule.g:98:6: '>'
            {
            match('>'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end GT

    // $ANTLR start IF
    public final void mIF() throws RecognitionException {
        try {
            int _type = IF;
            // ../src/tom/gom/expander/rule/Rule.g:99:6: ( 'if' )
            // ../src/tom/gom/expander/rule/Rule.g:99:6: 'if'
            {
            match("if"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IF

    // $ANTLR start INT
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            // ../src/tom/gom/expander/rule/Rule.g:100:7: ( ( '0' .. '9' )+ )
            // ../src/tom/gom/expander/rule/Rule.g:100:7: ( '0' .. '9' )+
            {
            // ../src/tom/gom/expander/rule/Rule.g:100:7: ( '0' .. '9' )+
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
            	    // ../src/tom/gom/expander/rule/Rule.g:100:8: '0' .. '9'
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

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INT

    // $ANTLR start ESC
    public final void mESC() throws RecognitionException {
        try {
            int _type = ESC;
            // ../src/tom/gom/expander/rule/Rule.g:101:7: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' ) )
            // ../src/tom/gom/expander/rule/Rule.g:101:7: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' )
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

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ESC

    // $ANTLR start STRING
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            // ../src/tom/gom/expander/rule/Rule.g:102:10: ( '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"' )
            // ../src/tom/gom/expander/rule/Rule.g:102:10: '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); 
            // ../src/tom/gom/expander/rule/Rule.g:102:14: ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='\\') ) {
                    alt2=1;
                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='!')||(LA2_0>='#' && LA2_0<='[')||(LA2_0>=']' && LA2_0<='\uFFFE')) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // ../src/tom/gom/expander/rule/Rule.g:102:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // ../src/tom/gom/expander/rule/Rule.g:102:19: ~ ( '\"' | '\\\\' | '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
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
            	    break loop2;
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

    // $ANTLR start ID
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            // ../src/tom/gom/expander/rule/Rule.g:103:6: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* ( '*' )? )
            // ../src/tom/gom/expander/rule/Rule.g:103:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* ( '*' )?
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // ../src/tom/gom/expander/rule/Rule.g:103:30: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../src/tom/gom/expander/rule/Rule.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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
            	    break loop3;
                }
            } while (true);

            // ../src/tom/gom/expander/rule/Rule.g:103:64: ( '*' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='*') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // ../src/tom/gom/expander/rule/Rule.g:103:65: '*'
                    {
                    match('*'); 

                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ID

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // ../src/tom/gom/expander/rule/Rule.g:104:6: ( ( ' ' | '\\t' | '\\n' )+ )
            // ../src/tom/gom/expander/rule/Rule.g:104:6: ( ' ' | '\\t' | '\\n' )+
            {
            // ../src/tom/gom/expander/rule/Rule.g:104:6: ( ' ' | '\\t' | '\\n' )+
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
            	    // ../src/tom/gom/expander/rule/Rule.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)==' ' ) {
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
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

             channel=HIDDEN; 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WS

    // $ANTLR start SLCOMMENT
    public final void mSLCOMMENT() throws RecognitionException {
        try {
            int _type = SLCOMMENT;
            // ../src/tom/gom/expander/rule/Rule.g:106:13: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // ../src/tom/gom/expander/rule/Rule.g:106:13: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // ../src/tom/gom/expander/rule/Rule.g:106:18: (~ ( '\\n' | '\\r' ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFE')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // ../src/tom/gom/expander/rule/Rule.g:106:19: ~ ( '\\n' | '\\r' )
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
            	    break loop6;
                }
            } while (true);

            // ../src/tom/gom/expander/rule/Rule.g:106:34: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // ../src/tom/gom/expander/rule/Rule.g:106:35: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // ../src/tom/gom/expander/rule/Rule.g:106:40: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // ../src/tom/gom/expander/rule/Rule.g:106:44: ( '\\n' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0=='\n') ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // ../src/tom/gom/expander/rule/Rule.g:106:45: '\\n'
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

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SLCOMMENT

    public void mTokens() throws RecognitionException {
        // ../src/tom/gom/expander/rule/Rule.g:1:10: ( ARROW | COLON | LPAR | RPAR | COMA | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT )
        int alt9=19;
        switch ( input.LA(1) ) {
        case '-':
            {
            alt9=1;
            }
            break;
        case ':':
            {
            alt9=2;
            }
            break;
        case '(':
            {
            alt9=3;
            }
            break;
        case ')':
            {
            alt9=4;
            }
            break;
        case ',':
            {
            alt9=5;
            }
            break;
        case '=':
            {
            alt9=6;
            }
            break;
        case '!':
            {
            alt9=7;
            }
            break;
        case '.':
            {
            alt9=8;
            }
            break;
        case '<':
            {
            int LA9_9 = input.LA(2);

            if ( (LA9_9=='=') ) {
                alt9=9;
            }
            else {
                alt9=10;}
            }
            break;
        case '>':
            {
            int LA9_10 = input.LA(2);

            if ( (LA9_10=='=') ) {
                alt9=11;
            }
            else {
                alt9=12;}
            }
            break;
        case 'i':
            {
            int LA9_11 = input.LA(2);

            if ( (LA9_11=='f') ) {
                int LA9_22 = input.LA(3);

                if ( (LA9_22=='*'||(LA9_22>='0' && LA9_22<='9')||(LA9_22>='A' && LA9_22<='Z')||LA9_22=='_'||(LA9_22>='a' && LA9_22<='z')) ) {
                    alt9=17;
                }
                else {
                    alt9=13;}
            }
            else {
                alt9=17;}
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt9=14;
            }
            break;
        case '\\':
            {
            alt9=15;
            }
            break;
        case '\"':
            {
            alt9=16;
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '_':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt9=17;
            }
            break;
        case '\t':
        case '\n':
        case ' ':
            {
            alt9=18;
            }
            break;
        case '/':
            {
            alt9=19;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( ARROW | COLON | LPAR | RPAR | COMA | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT );", 9, 0, input);

            throw nvae;
        }

        switch (alt9) {
            case 1 :
                // ../src/tom/gom/expander/rule/Rule.g:1:10: ARROW
                {
                mARROW(); 

                }
                break;
            case 2 :
                // ../src/tom/gom/expander/rule/Rule.g:1:16: COLON
                {
                mCOLON(); 

                }
                break;
            case 3 :
                // ../src/tom/gom/expander/rule/Rule.g:1:22: LPAR
                {
                mLPAR(); 

                }
                break;
            case 4 :
                // ../src/tom/gom/expander/rule/Rule.g:1:27: RPAR
                {
                mRPAR(); 

                }
                break;
            case 5 :
                // ../src/tom/gom/expander/rule/Rule.g:1:32: COMA
                {
                mCOMA(); 

                }
                break;
            case 6 :
                // ../src/tom/gom/expander/rule/Rule.g:1:37: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 7 :
                // ../src/tom/gom/expander/rule/Rule.g:1:44: NOTEQUALS
                {
                mNOTEQUALS(); 

                }
                break;
            case 8 :
                // ../src/tom/gom/expander/rule/Rule.g:1:54: DOT
                {
                mDOT(); 

                }
                break;
            case 9 :
                // ../src/tom/gom/expander/rule/Rule.g:1:58: LEQ
                {
                mLEQ(); 

                }
                break;
            case 10 :
                // ../src/tom/gom/expander/rule/Rule.g:1:62: LT
                {
                mLT(); 

                }
                break;
            case 11 :
                // ../src/tom/gom/expander/rule/Rule.g:1:65: GEQ
                {
                mGEQ(); 

                }
                break;
            case 12 :
                // ../src/tom/gom/expander/rule/Rule.g:1:69: GT
                {
                mGT(); 

                }
                break;
            case 13 :
                // ../src/tom/gom/expander/rule/Rule.g:1:72: IF
                {
                mIF(); 

                }
                break;
            case 14 :
                // ../src/tom/gom/expander/rule/Rule.g:1:75: INT
                {
                mINT(); 

                }
                break;
            case 15 :
                // ../src/tom/gom/expander/rule/Rule.g:1:79: ESC
                {
                mESC(); 

                }
                break;
            case 16 :
                // ../src/tom/gom/expander/rule/Rule.g:1:83: STRING
                {
                mSTRING(); 

                }
                break;
            case 17 :
                // ../src/tom/gom/expander/rule/Rule.g:1:90: ID
                {
                mID(); 

                }
                break;
            case 18 :
                // ../src/tom/gom/expander/rule/Rule.g:1:93: WS
                {
                mWS(); 

                }
                break;
            case 19 :
                // ../src/tom/gom/expander/rule/Rule.g:1:96: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;

        }

    }


 

}