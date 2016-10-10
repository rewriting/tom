// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g 2016-10-10 13:15:01

package tom.gom.expander.rule;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RuleLexer extends Lexer {
    public static final int LT=42;
    public static final int STAR=52;
    public static final int ESC=58;
    public static final int RuleList=25;
    public static final int SLCOMMENT=60;
    public static final int EQUALS=39;
    public static final int NOT=53;
    public static final int ID=48;
    public static final int AND=38;
    public static final int EOF=-1;
    public static final int CondMatch=26;
    public static final int IF=36;
    public static final int UnnamedVar=18;
    public static final int UnnamedVarStar=17;
    public static final int CondEquals=34;
    public static final int AT=50;
    public static final int CondLessEquals=32;
    public static final int LPAR=46;
    public static final int CondGreaterThan=29;
    public static final int TermList=22;
    public static final int BuiltinString=13;
    public static final int GEQ=43;
    public static final int CondLessThan=31;
    public static final int BuiltinInt=14;
    public static final int At=16;
    public static final int VarStar=19;
    public static final int CondOr=27;
    public static final int NOTEQUALS=40;
    public static final int UNDERSCORE=51;
    public static final int MATCH=45;
    public static final int INT=54;
    public static final int COMA=49;
    public static final int CondNotEquals=33;
    public static final int COLON=56;
    public static final int WS=59;
    public static final int CondAnd=28;
    public static final int Rule=24;
    public static final int LabTerm=12;
    public static final int OR=37;
    public static final int Appl=21;
    public static final int ConditionalRule=23;
    public static final int GT=44;
    public static final int ARROW=35;
    public static final int RefTerm=11;
    public static final int RPAR=47;
    public static final int PathTerm=10;
    public static final int AMPERCENT=57;
    public static final int CondGreaterEquals=30;
    public static final int Anti=15;
    public static final int Var=20;
    public static final int STRING=55;
    public static final int LEQ=41;

    // delegates
    // delegators

    public RuleLexer() {;} 
    public RuleLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public RuleLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g"; }

    // $ANTLR start "ARROW"
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:99:7: ( '->' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:99:9: '->'
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:100:11: ( '&' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:100:13: '&'
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

    // $ANTLR start "UNDERSCORE"
    public final void mUNDERSCORE() throws RecognitionException {
        try {
            int _type = UNDERSCORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:101:12: ( '_' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:101:14: '_'
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

    // $ANTLR start "STAR"
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:102:6: ( '*' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:102:8: '*'
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

    // $ANTLR start "AT"
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:103:4: ( '@' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:103:6: '@'
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

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:104:7: ( ':' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:104:9: ':'
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:105:6: ( '(' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:105:8: '('
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:106:6: ( ')' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:106:8: ')'
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

    // $ANTLR start "COMA"
    public final void mCOMA() throws RecognitionException {
        try {
            int _type = COMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:107:6: ( ',' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:107:8: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMA"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:108:5: ( '&&' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:108:7: '&&'
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:109:4: ( '||' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:109:6: '||'
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

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:110:5: ( '!' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:110:7: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "EQUALS"
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:111:8: ( '==' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:111:10: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUALS"

    // $ANTLR start "NOTEQUALS"
    public final void mNOTEQUALS() throws RecognitionException {
        try {
            int _type = NOTEQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:112:11: ( '!=' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:112:13: '!='
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

    // $ANTLR start "LEQ"
    public final void mLEQ() throws RecognitionException {
        try {
            int _type = LEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:113:5: ( '<=' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:113:7: '<='
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

    // $ANTLR start "MATCH"
    public final void mMATCH() throws RecognitionException {
        try {
            int _type = MATCH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:114:7: ( '<<' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:114:9: '<<'
            {
            match("<<"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MATCH"

    // $ANTLR start "LT"
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:115:4: ( '<' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:115:6: '<'
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:116:5: ( '>=' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:116:7: '>='
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:117:4: ( '>' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:117:6: '>'
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:118:4: ( 'if' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:118:6: 'if'
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:119:5: ( ( '0' .. '9' )+ )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:119:7: ( '0' .. '9' )+
            {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:119:7: ( '0' .. '9' )+
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
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:119:8: '0' .. '9'
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:120:5: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' ) )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:120:7: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' )
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:121:8: ( '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"' )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:121:10: '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); 
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:121:14: ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )*
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
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:121:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:121:19: ~ ( '\"' | '\\\\' | '\\n' | '\\r' )
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:122:4: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:122:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:122:30: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:123:4: ( ( ' ' | '\\t' | '\\n' )+ )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:123:6: ( ' ' | '\\t' | '\\n' )+
            {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:123:6: ( ' ' | '\\t' | '\\n' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:
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
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
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
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:125:11: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:125:13: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:125:18: (~ ( '\\n' | '\\r' ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\u0000' && LA5_0<='\t')||(LA5_0>='\u000B' && LA5_0<='\f')||(LA5_0>='\u000E' && LA5_0<='\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:125:19: ~ ( '\\n' | '\\r' )
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

            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:125:34: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:125:35: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:125:40: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:125:44: ( '\\n' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0=='\n') ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:125:45: '\\n'
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

    public void mTokens() throws RecognitionException {
        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:8: ( ARROW | AMPERCENT | UNDERSCORE | STAR | AT | COLON | LPAR | RPAR | COMA | AND | OR | NOT | EQUALS | NOTEQUALS | LEQ | MATCH | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT )
        int alt8=26;
        alt8 = dfa8.predict(input);
        switch (alt8) {
            case 1 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:10: ARROW
                {
                mARROW(); 

                }
                break;
            case 2 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:16: AMPERCENT
                {
                mAMPERCENT(); 

                }
                break;
            case 3 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:26: UNDERSCORE
                {
                mUNDERSCORE(); 

                }
                break;
            case 4 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:37: STAR
                {
                mSTAR(); 

                }
                break;
            case 5 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:42: AT
                {
                mAT(); 

                }
                break;
            case 6 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:45: COLON
                {
                mCOLON(); 

                }
                break;
            case 7 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:51: LPAR
                {
                mLPAR(); 

                }
                break;
            case 8 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:56: RPAR
                {
                mRPAR(); 

                }
                break;
            case 9 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:61: COMA
                {
                mCOMA(); 

                }
                break;
            case 10 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:66: AND
                {
                mAND(); 

                }
                break;
            case 11 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:70: OR
                {
                mOR(); 

                }
                break;
            case 12 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:73: NOT
                {
                mNOT(); 

                }
                break;
            case 13 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:77: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 14 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:84: NOTEQUALS
                {
                mNOTEQUALS(); 

                }
                break;
            case 15 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:94: LEQ
                {
                mLEQ(); 

                }
                break;
            case 16 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:98: MATCH
                {
                mMATCH(); 

                }
                break;
            case 17 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:104: LT
                {
                mLT(); 

                }
                break;
            case 18 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:107: GEQ
                {
                mGEQ(); 

                }
                break;
            case 19 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:111: GT
                {
                mGT(); 

                }
                break;
            case 20 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:114: IF
                {
                mIF(); 

                }
                break;
            case 21 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:117: INT
                {
                mINT(); 

                }
                break;
            case 22 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:121: ESC
                {
                mESC(); 

                }
                break;
            case 23 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:125: STRING
                {
                mSTRING(); 

                }
                break;
            case 24 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:132: ID
                {
                mID(); 

                }
                break;
            case 25 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:135: WS
                {
                mWS(); 

                }
                break;
            case 26 :
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:1:138: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    static final String DFA8_eotS =
        "\2\uffff\1\27\1\30\7\uffff\1\32\1\uffff\1\35\1\37\1\23\20\uffff"+
        "\1\41\1\uffff";
    static final String DFA8_eofS =
        "\42\uffff";
    static final String DFA8_minS =
        "\1\11\1\uffff\1\46\1\60\7\uffff\1\75\1\uffff\1\74\1\75\1\146\20"+
        "\uffff\1\60\1\uffff";
    static final String DFA8_maxS =
        "\1\174\1\uffff\1\46\1\172\7\uffff\1\75\1\uffff\2\75\1\146\20\uffff"+
        "\1\172\1\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\1\2\uffff\1\4\1\5\1\6\1\7\1\10\1\11\1\13\1\uffff\1\15"+
        "\3\uffff\1\25\1\26\1\27\1\30\1\31\1\32\1\12\1\2\1\3\1\16\1\14\1"+
        "\17\1\20\1\21\1\22\1\23\1\uffff\1\24";
    static final String DFA8_specialS =
        "\42\uffff}>";
    static final String[] DFA8_transitionS = {
            "\2\24\25\uffff\1\24\1\13\1\22\3\uffff\1\2\1\uffff\1\7\1\10\1"+
            "\4\1\uffff\1\11\1\1\1\uffff\1\25\12\20\1\6\1\uffff\1\15\1\14"+
            "\1\16\1\uffff\1\5\32\23\1\uffff\1\21\2\uffff\1\3\1\uffff\10"+
            "\23\1\17\21\23\1\uffff\1\12",
            "",
            "\1\26",
            "\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\31",
            "",
            "\1\34\1\33",
            "\1\36",
            "\1\40",
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
            "\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            ""
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
            return "1:1: Tokens : ( ARROW | AMPERCENT | UNDERSCORE | STAR | AT | COLON | LPAR | RPAR | COMA | AND | OR | NOT | EQUALS | NOTEQUALS | LEQ | MATCH | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT );";
        }
    }
 

}