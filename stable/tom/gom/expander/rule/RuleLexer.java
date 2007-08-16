// $ANTLR 3.0 /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g 2007-08-15 01:28:39

  package tom.gom.expander.rule;
  import tom.gom.adt.rule.RuleTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RuleLexer extends Lexer {
    public static final int CondEquals=5;
    public static final int RefTerm=9;
    public static final int LPAR=33;
    public static final int ARROW=23;
    public static final int LEQ=27;
    public static final int GEQ=29;
    public static final int RuleList=19;
    public static final int DOT=31;
    public static final int BuiltinInt=4;
    public static final int BuiltinString=6;
    public static final int SLCOMMENT=42;
    public static final int CondLessEquals=8;
    public static final int INT=36;
    public static final int CondLessThan=18;
    public static final int CondGreaterEquals=20;
    public static final int Var=21;
    public static final int ID=32;
    public static final int NOTEQUALS=26;
    public static final int CondMethod=7;
    public static final int RPAR=34;
    public static final int LabTerm=10;
    public static final int WS=41;
    public static final int STRING=37;
    public static final int LT=28;
    public static final int ConditionalRule=12;
    public static final int GT=30;
    public static final int Rule=13;
    public static final int ESC=40;
    public static final int Appl=22;
    public static final int EQUALS=25;
    public static final int AMPERCENT=39;
    public static final int IF=24;
    public static final int CondTerm=11;
    public static final int EOF=-1;
    public static final int Tokens=43;
    public static final int COLON=38;
    public static final int TermList=14;
    public static final int CondNotEquals=15;
    public static final int COMA=35;
    public static final int PathTerm=16;
    public static final int CondGreaterThan=17;
    public RuleLexer() {;} 
    public RuleLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g"; }

    // $ANTLR start ARROW
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:86:9: ( '->' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:86:9: '->'
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:87:13: ( '&' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:87:13: '&'
            {
            match('&'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end AMPERCENT

    // $ANTLR start COLON
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:88:9: ( ':' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:88:9: ':'
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:8: ( '(' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:8: '('
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:90:8: ( ')' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:90:8: ')'
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:91:8: ( ',' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:91:8: ','
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:92:10: ( '==' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:92:10: '=='
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:93:13: ( '!=' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:93:13: '!='
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:94:7: ( '.' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:94:7: '.'
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:95:7: ( '<=' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:95:7: '<='
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:6: ( '<' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:6: '<'
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:97:7: ( '>=' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:97:7: '>='
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:98:6: ( '>' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:98:6: '>'
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:99:6: ( 'if' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:99:6: 'if'
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:100:7: ( ( '0' .. '9' )+ )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:100:7: ( '0' .. '9' )+
            {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:100:7: ( '0' .. '9' )+
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
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:100:8: '0' .. '9'
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:101:7: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' ) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:101:7: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' )
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:102:10: ( '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"' )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:102:10: '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); 
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:102:14: ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )*
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
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:102:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:102:19: ~ ( '\"' | '\\\\' | '\\n' | '\\r' )
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:103:6: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* ( '*' )? )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:103:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* ( '*' )?
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:103:30: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:
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

            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:103:64: ( '*' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='*') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:103:65: '*'
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:104:6: ( ( ' ' | '\\t' | '\\n' )+ )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:104:6: ( ' ' | '\\t' | '\\n' )+
            {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:104:6: ( ' ' | '\\t' | '\\n' )+
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
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:106:13: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:106:13: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:106:18: (~ ( '\\n' | '\\r' ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFE')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:106:19: ~ ( '\\n' | '\\r' )
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

            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:106:34: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:106:35: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:106:40: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:106:44: ( '\\n' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0=='\n') ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:106:45: '\\n'
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
        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:10: ( ARROW | AMPERCENT | COLON | LPAR | RPAR | COMA | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT )
        int alt9=20;
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
        case ':':
            {
            alt9=3;
            }
            break;
        case '(':
            {
            alt9=4;
            }
            break;
        case ')':
            {
            alt9=5;
            }
            break;
        case ',':
            {
            alt9=6;
            }
            break;
        case '=':
            {
            alt9=7;
            }
            break;
        case '!':
            {
            alt9=8;
            }
            break;
        case '.':
            {
            alt9=9;
            }
            break;
        case '<':
            {
            int LA9_10 = input.LA(2);

            if ( (LA9_10=='=') ) {
                alt9=10;
            }
            else {
                alt9=11;}
            }
            break;
        case '>':
            {
            int LA9_11 = input.LA(2);

            if ( (LA9_11=='=') ) {
                alt9=12;
            }
            else {
                alt9=13;}
            }
            break;
        case 'i':
            {
            int LA9_12 = input.LA(2);

            if ( (LA9_12=='f') ) {
                int LA9_23 = input.LA(3);

                if ( (LA9_23=='*'||(LA9_23>='0' && LA9_23<='9')||(LA9_23>='A' && LA9_23<='Z')||LA9_23=='_'||(LA9_23>='a' && LA9_23<='z')) ) {
                    alt9=18;
                }
                else {
                    alt9=14;}
            }
            else {
                alt9=18;}
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
            alt9=15;
            }
            break;
        case '\\':
            {
            alt9=16;
            }
            break;
        case '\"':
            {
            alt9=17;
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
            alt9=18;
            }
            break;
        case '\t':
        case '\n':
        case ' ':
            {
            alt9=19;
            }
            break;
        case '/':
            {
            alt9=20;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( ARROW | AMPERCENT | COLON | LPAR | RPAR | COMA | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT );", 9, 0, input);

            throw nvae;
        }

        switch (alt9) {
            case 1 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:10: ARROW
                {
                mARROW(); 

                }
                break;
            case 2 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:16: AMPERCENT
                {
                mAMPERCENT(); 

                }
                break;
            case 3 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:26: COLON
                {
                mCOLON(); 

                }
                break;
            case 4 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:32: LPAR
                {
                mLPAR(); 

                }
                break;
            case 5 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:37: RPAR
                {
                mRPAR(); 

                }
                break;
            case 6 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:42: COMA
                {
                mCOMA(); 

                }
                break;
            case 7 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:47: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 8 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:54: NOTEQUALS
                {
                mNOTEQUALS(); 

                }
                break;
            case 9 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:64: DOT
                {
                mDOT(); 

                }
                break;
            case 10 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:68: LEQ
                {
                mLEQ(); 

                }
                break;
            case 11 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:72: LT
                {
                mLT(); 

                }
                break;
            case 12 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:75: GEQ
                {
                mGEQ(); 

                }
                break;
            case 13 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:79: GT
                {
                mGT(); 

                }
                break;
            case 14 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:82: IF
                {
                mIF(); 

                }
                break;
            case 15 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:85: INT
                {
                mINT(); 

                }
                break;
            case 16 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:89: ESC
                {
                mESC(); 

                }
                break;
            case 17 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:93: STRING
                {
                mSTRING(); 

                }
                break;
            case 18 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:100: ID
                {
                mID(); 

                }
                break;
            case 19 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:103: WS
                {
                mWS(); 

                }
                break;
            case 20 :
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:106: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;

        }

    }


 

}