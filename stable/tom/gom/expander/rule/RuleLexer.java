// $ANTLR 3.0 /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g 2007-11-29 01:49:09

  package tom.gom.expander.rule;
  import tom.gom.adt.rule.RuleTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RuleLexer extends Lexer {
    public static final int CondEquals=5;
    public static final int RefTerm=10;
    public static final int LPAR=36;
    public static final int ARROW=26;
    public static final int LEQ=30;
    public static final int GEQ=32;
    public static final int RuleList=20;
    public static final int DOT=34;
    public static final int BuiltinInt=4;
    public static final int BuiltinString=6;
    public static final int SLCOMMENT=48;
    public static final int CondLessEquals=8;
    public static final int INT=42;
    public static final int CondLessThan=19;
    public static final int UnnamedVar=21;
    public static final int CondGreaterEquals=22;
    public static final int Var=23;
    public static final int AT=39;
    public static final int At=25;
    public static final int ID=35;
    public static final int NOTEQUALS=29;
    public static final int UnnamedVarStar=7;
    public static final int CondMethod=9;
    public static final int RPAR=37;
    public static final int LabTerm=11;
    public static final int WS=47;
    public static final int STRING=43;
    public static final int LT=31;
    public static final int ConditionalRule=13;
    public static final int GT=33;
    public static final int Rule=14;
    public static final int ESC=46;
    public static final int Appl=24;
    public static final int EQUALS=28;
    public static final int AMPERCENT=45;
    public static final int CondTerm=12;
    public static final int IF=27;
    public static final int EOF=-1;
    public static final int Tokens=49;
    public static final int COLON=44;
    public static final int TermList=15;
    public static final int CondNotEquals=16;
    public static final int UNDERSCORESTAR=41;
    public static final int COMA=38;
    public static final int PathTerm=17;
    public static final int CondGreaterThan=18;
    public static final int UNDERSCORE=40;
    public RuleLexer() {;} 
    public RuleLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g"; }

    // $ANTLR start ARROW
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:89:9: ( '->' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:89:9: '->'
            {
            match("->"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ARROW

    // $ANTLR start AMPERCENT
    public final void mAMPERCENT() throws RecognitionException {
        try {
            int _type = AMPERCENT;
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:90:13: ( '&' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:90:13: '&'
            {
            match('&'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end AMPERCENT

    // $ANTLR start UNDERSCORE
    public final void mUNDERSCORE() throws RecognitionException {
        try {
            int _type = UNDERSCORE;
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:91:14: ( '_' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:91:14: '_'
            {
            match('_'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end UNDERSCORE

    // $ANTLR start UNDERSCORESTAR
    public final void mUNDERSCORESTAR() throws RecognitionException {
        try {
            int _type = UNDERSCORESTAR;
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:92:18: ( '_*' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:92:18: '_*'
            {
            match("_*"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end UNDERSCORESTAR

    // $ANTLR start AT
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:93:6: ( '@' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:93:6: '@'
            {
            match('@'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end AT

    // $ANTLR start COLON
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:94:9: ( ':' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:94:9: ':'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:95:8: ( '(' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:95:8: '('
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:96:8: ( ')' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:96:8: ')'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:97:8: ( ',' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:97:8: ','
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:98:10: ( '==' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:98:10: '=='
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:99:13: ( '!=' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:99:13: '!='
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:100:7: ( '.' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:100:7: '.'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:101:7: ( '<=' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:101:7: '<='
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:102:6: ( '<' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:102:6: '<'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:103:7: ( '>=' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:103:7: '>='
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:104:6: ( '>' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:104:6: '>'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:105:6: ( 'if' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:105:6: 'if'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:106:7: ( ( '0' .. '9' )+ )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:106:7: ( '0' .. '9' )+
            {
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:106:7: ( '0' .. '9' )+
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
            	    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:106:8: '0' .. '9'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:107:7: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' ) )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:107:7: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' )
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:108:10: ( '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:108:10: '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); 
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:108:14: ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )*
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
            	    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:108:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:108:19: ~ ( '\"' | '\\\\' | '\\n' | '\\r' )
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:109:6: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* ( '*' )? )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:109:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* ( '*' )?
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:109:30: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:
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

            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:109:64: ( '*' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='*') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:109:65: '*'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:110:6: ( ( ' ' | '\\t' | '\\n' )+ )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:110:6: ( ' ' | '\\t' | '\\n' )+
            {
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:110:6: ( ' ' | '\\t' | '\\n' )+
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
            	    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:112:13: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:112:13: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:112:18: (~ ( '\\n' | '\\r' ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFE')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:112:19: ~ ( '\\n' | '\\r' )
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

            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:112:34: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:112:35: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:112:40: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:112:44: ( '\\n' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0=='\n') ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:112:45: '\\n'
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
        // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:10: ( ARROW | AMPERCENT | UNDERSCORE | UNDERSCORESTAR | AT | COLON | LPAR | RPAR | COMA | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT )
        int alt9=23;
        switch ( input.LA(1) ) {
        case '-':
            {
            alt9=1;
            }
            break;
        case '&':
            {
            alt9=2;
            }
            break;
        case '_':
            {
            switch ( input.LA(2) ) {
            case '*':
                {
                alt9=4;
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
            case 'i':
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
                alt9=21;
                }
                break;
            default:
                alt9=3;}

            }
            break;
        case '@':
            {
            alt9=5;
            }
            break;
        case ':':
            {
            alt9=6;
            }
            break;
        case '(':
            {
            alt9=7;
            }
            break;
        case ')':
            {
            alt9=8;
            }
            break;
        case ',':
            {
            alt9=9;
            }
            break;
        case '=':
            {
            alt9=10;
            }
            break;
        case '!':
            {
            alt9=11;
            }
            break;
        case '.':
            {
            alt9=12;
            }
            break;
        case '<':
            {
            int LA9_12 = input.LA(2);

            if ( (LA9_12=='=') ) {
                alt9=13;
            }
            else {
                alt9=14;}
            }
            break;
        case '>':
            {
            int LA9_13 = input.LA(2);

            if ( (LA9_13=='=') ) {
                alt9=15;
            }
            else {
                alt9=16;}
            }
            break;
        case 'i':
            {
            int LA9_14 = input.LA(2);

            if ( (LA9_14=='f') ) {
                int LA9_27 = input.LA(3);

                if ( (LA9_27=='*'||(LA9_27>='0' && LA9_27<='9')||(LA9_27>='A' && LA9_27<='Z')||LA9_27=='_'||(LA9_27>='a' && LA9_27<='z')) ) {
                    alt9=21;
                }
                else {
                    alt9=17;}
            }
            else {
                alt9=21;}
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
            alt9=18;
            }
            break;
        case '\\':
            {
            alt9=19;
            }
            break;
        case '\"':
            {
            alt9=20;
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
            alt9=21;
            }
            break;
        case '\t':
        case '\n':
        case ' ':
            {
            alt9=22;
            }
            break;
        case '/':
            {
            alt9=23;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( ARROW | AMPERCENT | UNDERSCORE | UNDERSCORESTAR | AT | COLON | LPAR | RPAR | COMA | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT );", 9, 0, input);

            throw nvae;
        }

        switch (alt9) {
            case 1 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:10: ARROW
                {
                mARROW(); 

                }
                break;
            case 2 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:16: AMPERCENT
                {
                mAMPERCENT(); 

                }
                break;
            case 3 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:26: UNDERSCORE
                {
                mUNDERSCORE(); 

                }
                break;
            case 4 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:37: UNDERSCORESTAR
                {
                mUNDERSCORESTAR(); 

                }
                break;
            case 5 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:52: AT
                {
                mAT(); 

                }
                break;
            case 6 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:55: COLON
                {
                mCOLON(); 

                }
                break;
            case 7 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:61: LPAR
                {
                mLPAR(); 

                }
                break;
            case 8 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:66: RPAR
                {
                mRPAR(); 

                }
                break;
            case 9 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:71: COMA
                {
                mCOMA(); 

                }
                break;
            case 10 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:76: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 11 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:83: NOTEQUALS
                {
                mNOTEQUALS(); 

                }
                break;
            case 12 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:93: DOT
                {
                mDOT(); 

                }
                break;
            case 13 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:97: LEQ
                {
                mLEQ(); 

                }
                break;
            case 14 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:101: LT
                {
                mLT(); 

                }
                break;
            case 15 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:104: GEQ
                {
                mGEQ(); 

                }
                break;
            case 16 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:108: GT
                {
                mGT(); 

                }
                break;
            case 17 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:111: IF
                {
                mIF(); 

                }
                break;
            case 18 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:114: INT
                {
                mINT(); 

                }
                break;
            case 19 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:118: ESC
                {
                mESC(); 

                }
                break;
            case 20 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:122: STRING
                {
                mSTRING(); 

                }
                break;
            case 21 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:129: ID
                {
                mID(); 

                }
                break;
            case 22 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:132: WS
                {
                mWS(); 

                }
                break;
            case 23 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/expander/rule/Rule.g:1:135: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;

        }

    }


 

}