// $ANTLR 3.0 /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g 2008-03-22 14:30:58

package tom.gom.expander.rule;
import tom.gom.adt.rule.RuleTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RuleLexer extends Lexer {
    public static final int CondEquals=4;
    public static final int RefTerm=8;
    public static final int LPAR=43;
    public static final int ARROW=30;
    public static final int LEQ=37;
    public static final int GEQ=39;
    public static final int RuleList=14;
    public static final int OR=32;
    public static final int DOT=41;
    public static final int BuiltinInt=16;
    public static final int AND=33;
    public static final int BuiltinString=17;
    public static final int SLCOMMENT=55;
    public static final int CondLessEquals=18;
    public static final int INT=49;
    public static final int CondLessThan=25;
    public static final int UnnamedVar=26;
    public static final int CondGreaterEquals=27;
    public static final int Var=28;
    public static final int AT=46;
    public static final int At=29;
    public static final int ID=42;
    public static final int NOTEQUALS=36;
    public static final int UnnamedVarStar=5;
    public static final int CondMethod=6;
    public static final int RPAR=44;
    public static final int LabTerm=7;
    public static final int WS=54;
    public static final int STRING=50;
    public static final int CondOr=9;
    public static final int LT=38;
    public static final int ConditionalRule=10;
    public static final int GT=40;
    public static final int Rule=11;
    public static final int CondAnd=12;
    public static final int CondNot=13;
    public static final int ESC=53;
    public static final int Appl=15;
    public static final int EQUALS=35;
    public static final int AMPERCENT=52;
    public static final int VarStar=19;
    public static final int IF=31;
    public static final int CondTerm=20;
    public static final int EOF=-1;
    public static final int Tokens=56;
    public static final int COLON=51;
    public static final int TermList=21;
    public static final int CondNotEquals=22;
    public static final int CondGreaterThan=24;
    public static final int PathTerm=23;
    public static final int COMA=45;
    public static final int STAR=48;
    public static final int NOT=34;
    public static final int UNDERSCORE=47;
    public RuleLexer() {;} 
    public RuleLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g"; }

    // $ANTLR start ARROW
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:108:9: ( '->' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:108:9: '->'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:109:13: ( '&' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:109:13: '&'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:110:14: ( '_' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:110:14: '_'
            {
            match('_'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end UNDERSCORE

    // $ANTLR start STAR
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:111:8: ( '*' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:111:8: '*'
            {
            match('*'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STAR

    // $ANTLR start AT
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:112:6: ( '@' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:112:6: '@'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:113:9: ( ':' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:113:9: ':'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:114:8: ( '(' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:114:8: '('
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:115:8: ( ')' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:115:8: ')'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:116:8: ( ',' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:116:8: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMA

    // $ANTLR start AND
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:117:7: ( '&&' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:117:7: '&&'
            {
            match("&&"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end AND

    // $ANTLR start OR
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:118:6: ( '||' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:118:6: '||'
            {
            match("||"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OR

    // $ANTLR start NOT
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:119:7: ( '!' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:119:7: '!'
            {
            match('!'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NOT

    // $ANTLR start EQUALS
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:120:10: ( '==' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:120:10: '=='
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:121:13: ( '!=' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:121:13: '!='
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:122:7: ( '.' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:122:7: '.'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:123:7: ( '<=' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:123:7: '<='
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:124:6: ( '<' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:124:6: '<'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:125:7: ( '>=' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:125:7: '>='
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:126:6: ( '>' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:126:6: '>'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:127:6: ( 'if' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:127:6: 'if'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:128:7: ( ( '0' .. '9' )+ )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:128:7: ( '0' .. '9' )+
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:128:7: ( '0' .. '9' )+
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
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:128:8: '0' .. '9'
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:129:7: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:129:7: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' )
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:130:10: ( '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:130:10: '\"' ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); 
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:130:14: ( ESC | ~ ( '\"' | '\\\\' | '\\n' | '\\r' ) )*
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
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:130:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:130:19: ~ ( '\"' | '\\\\' | '\\n' | '\\r' )
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:131:6: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:131:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:131:30: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:132:6: ( ( ' ' | '\\t' | '\\n' )+ )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:132:6: ( ' ' | '\\t' | '\\n' )+
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:132:6: ( ' ' | '\\t' | '\\n' )+
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
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:
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
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:134:13: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:134:13: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:134:18: (~ ( '\\n' | '\\r' ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\u0000' && LA5_0<='\t')||(LA5_0>='\u000B' && LA5_0<='\f')||(LA5_0>='\u000E' && LA5_0<='\uFFFE')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:134:19: ~ ( '\\n' | '\\r' )
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

            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:134:34: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:134:35: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:134:40: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:134:44: ( '\\n' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0=='\n') ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:134:45: '\\n'
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
        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:10: ( ARROW | AMPERCENT | UNDERSCORE | STAR | AT | COLON | LPAR | RPAR | COMA | AND | OR | NOT | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT )
        int alt8=26;
        switch ( input.LA(1) ) {
        case '-':
            {
            alt8=1;
            }
            break;
        case '&':
            {
            int LA8_2 = input.LA(2);

            if ( (LA8_2=='&') ) {
                alt8=10;
            }
            else {
                alt8=2;}
            }
            break;
        case '_':
            {
            int LA8_3 = input.LA(2);

            if ( ((LA8_3>='0' && LA8_3<='9')||(LA8_3>='A' && LA8_3<='Z')||LA8_3=='_'||(LA8_3>='a' && LA8_3<='z')) ) {
                alt8=24;
            }
            else {
                alt8=3;}
            }
            break;
        case '*':
            {
            alt8=4;
            }
            break;
        case '@':
            {
            alt8=5;
            }
            break;
        case ':':
            {
            alt8=6;
            }
            break;
        case '(':
            {
            alt8=7;
            }
            break;
        case ')':
            {
            alt8=8;
            }
            break;
        case ',':
            {
            alt8=9;
            }
            break;
        case '|':
            {
            alt8=11;
            }
            break;
        case '!':
            {
            int LA8_11 = input.LA(2);

            if ( (LA8_11=='=') ) {
                alt8=14;
            }
            else {
                alt8=12;}
            }
            break;
        case '=':
            {
            alt8=13;
            }
            break;
        case '.':
            {
            alt8=15;
            }
            break;
        case '<':
            {
            int LA8_14 = input.LA(2);

            if ( (LA8_14=='=') ) {
                alt8=16;
            }
            else {
                alt8=17;}
            }
            break;
        case '>':
            {
            int LA8_15 = input.LA(2);

            if ( (LA8_15=='=') ) {
                alt8=18;
            }
            else {
                alt8=19;}
            }
            break;
        case 'i':
            {
            int LA8_16 = input.LA(2);

            if ( (LA8_16=='f') ) {
                int LA8_32 = input.LA(3);

                if ( ((LA8_32>='0' && LA8_32<='9')||(LA8_32>='A' && LA8_32<='Z')||LA8_32=='_'||(LA8_32>='a' && LA8_32<='z')) ) {
                    alt8=24;
                }
                else {
                    alt8=20;}
            }
            else {
                alt8=24;}
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
            alt8=21;
            }
            break;
        case '\\':
            {
            alt8=22;
            }
            break;
        case '\"':
            {
            alt8=23;
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
            alt8=24;
            }
            break;
        case '\t':
        case '\n':
        case ' ':
            {
            alt8=25;
            }
            break;
        case '/':
            {
            alt8=26;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( ARROW | AMPERCENT | UNDERSCORE | STAR | AT | COLON | LPAR | RPAR | COMA | AND | OR | NOT | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT );", 8, 0, input);

            throw nvae;
        }

        switch (alt8) {
            case 1 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:10: ARROW
                {
                mARROW(); 

                }
                break;
            case 2 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:16: AMPERCENT
                {
                mAMPERCENT(); 

                }
                break;
            case 3 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:26: UNDERSCORE
                {
                mUNDERSCORE(); 

                }
                break;
            case 4 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:37: STAR
                {
                mSTAR(); 

                }
                break;
            case 5 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:42: AT
                {
                mAT(); 

                }
                break;
            case 6 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:45: COLON
                {
                mCOLON(); 

                }
                break;
            case 7 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:51: LPAR
                {
                mLPAR(); 

                }
                break;
            case 8 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:56: RPAR
                {
                mRPAR(); 

                }
                break;
            case 9 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:61: COMA
                {
                mCOMA(); 

                }
                break;
            case 10 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:66: AND
                {
                mAND(); 

                }
                break;
            case 11 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:70: OR
                {
                mOR(); 

                }
                break;
            case 12 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:73: NOT
                {
                mNOT(); 

                }
                break;
            case 13 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:77: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 14 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:84: NOTEQUALS
                {
                mNOTEQUALS(); 

                }
                break;
            case 15 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:94: DOT
                {
                mDOT(); 

                }
                break;
            case 16 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:98: LEQ
                {
                mLEQ(); 

                }
                break;
            case 17 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:102: LT
                {
                mLT(); 

                }
                break;
            case 18 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:105: GEQ
                {
                mGEQ(); 

                }
                break;
            case 19 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:109: GT
                {
                mGT(); 

                }
                break;
            case 20 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:112: IF
                {
                mIF(); 

                }
                break;
            case 21 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:115: INT
                {
                mINT(); 

                }
                break;
            case 22 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:119: ESC
                {
                mESC(); 

                }
                break;
            case 23 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:123: STRING
                {
                mSTRING(); 

                }
                break;
            case 24 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:130: ID
                {
                mID(); 

                }
                break;
            case 25 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:133: WS
                {
                mWS(); 

                }
                break;
            case 26 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:1:136: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;

        }

    }


 

}