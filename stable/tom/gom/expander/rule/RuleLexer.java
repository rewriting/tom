// $ANTLR 3.0b6 /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g 2007-06-21 22:28:19

  package tom.gom.expander.rule;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RuleLexer extends Lexer {
    public static final int CONDGEQ=13;
    public static final int NOTEQUALS=19;
    public static final int RPAR=27;
    public static final int CONDLT=12;
    public static final int WS=32;
    public static final int LPAR=26;
    public static final int ARROW=16;
    public static final int STRING=30;
    public static final int LT=21;
    public static final int LEQ=20;
    public static final int GT=23;
    public static final int CONDGT=14;
    public static final int RULE=5;
    public static final int RULELIST=4;
    public static final int GEQ=22;
    public static final int CONDEQUALS=9;
    public static final int ESC=31;
    public static final int DOT=24;
    public static final int EQUALS=18;
    public static final int CONDTERM=8;
    public static final int SLCOMMENT=33;
    public static final int CONDNOTEQUALS=10;
    public static final int CONDMETHOD=15;
    public static final int IF=17;
    public static final int INT=29;
    public static final int CONDLEQ=11;
    public static final int EOF=-1;
    public static final int Tokens=34;
    public static final int CONDRULE=6;
    public static final int APPL=7;
    public static final int COMA=28;
    public static final int ID=25;
    public RuleLexer() {;} 
    public RuleLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g"; }

    // $ANTLR start ARROW
    public void mARROW() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = ARROW;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:65:9: ( '->' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:65:9: '->'
            {
            match("->"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end ARROW

    // $ANTLR start LPAR
    public void mLPAR() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = LPAR;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:8: ( '(' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:8: '('
            {
            match('('); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end LPAR

    // $ANTLR start RPAR
    public void mRPAR() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = RPAR;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:67:8: ( ')' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:67:8: ')'
            {
            match(')'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end RPAR

    // $ANTLR start COMA
    public void mCOMA() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = COMA;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:68:8: ( ',' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:68:8: ','
            {
            match(','); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end COMA

    // $ANTLR start EQUALS
    public void mEQUALS() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = EQUALS;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:69:10: ( '==' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:69:10: '=='
            {
            match("=="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end EQUALS

    // $ANTLR start NOTEQUALS
    public void mNOTEQUALS() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = NOTEQUALS;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:70:13: ( '!=' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:70:13: '!='
            {
            match("!="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end NOTEQUALS

    // $ANTLR start DOT
    public void mDOT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = DOT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:71:7: ( '.' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:71:7: '.'
            {
            match('.'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end DOT

    // $ANTLR start LEQ
    public void mLEQ() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = LEQ;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:72:7: ( '<=' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:72:7: '<='
            {
            match("<="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end LEQ

    // $ANTLR start LT
    public void mLT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = LT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:73:6: ( '<' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:73:6: '<'
            {
            match('<'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end LT

    // $ANTLR start GEQ
    public void mGEQ() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = GEQ;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:74:7: ( '>=' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:74:7: '>='
            {
            match(">="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end GEQ

    // $ANTLR start GT
    public void mGT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = GT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:75:6: ( '>' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:75:6: '>'
            {
            match('>'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end GT

    // $ANTLR start IF
    public void mIF() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = IF;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:76:6: ( 'if' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:76:6: 'if'
            {
            match("if"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end IF

    // $ANTLR start INT
    public void mINT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = INT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:77:7: ( ( '0' .. '9' )+ )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:77:7: ( '0' .. '9' )+
            {
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:77:7: ( '0' .. '9' )+
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
            	    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:77:8: '0' .. '9'
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



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end INT

    // $ANTLR start ESC
    public void mESC() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = ESC;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:78:7: ( '\\\\' ('n'|'r'|'t'|'b'|'f'|'\"'|'\\''|'\\\\'))
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:78:7: '\\\\' ('n'|'r'|'t'|'b'|'f'|'\"'|'\\''|'\\\\')
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



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end ESC

    // $ANTLR start STRING
    public void mSTRING() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = STRING;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:79:10: ( '\"' ( ESC | ~ ('\"'|'\\\\'|'\\n'|'\\r'))* '\"' )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:79:10: '\"' ( ESC | ~ ('\"'|'\\\\'|'\\n'|'\\r'))* '\"'
            {
            match('\"'); 
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:79:14: ( ESC | ~ ('\"'|'\\\\'|'\\n'|'\\r'))*
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
            	    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:79:15: ESC
            	    {
            	    mESC(); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:79:19: ~ ('\"'|'\\\\'|'\\n'|'\\r')
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



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end STRING

    // $ANTLR start ID
    public void mID() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = ID;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:80:6: ( ('a'..'z'|'A'..'Z'|'_') ( ('a'..'z'|'A'..'Z'|'_'|'0'..'9'))* ( '*' )? )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:80:6: ('a'..'z'|'A'..'Z'|'_') ( ('a'..'z'|'A'..'Z'|'_'|'0'..'9'))* ( '*' )?
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:80:30: ( ('a'..'z'|'A'..'Z'|'_'|'0'..'9'))*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);
                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:80:31: ('a'..'z'|'A'..'Z'|'_'|'0'..'9')
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

            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:80:64: ( '*' )?
            int alt4=2;
            int LA4_0 = input.LA(1);
            if ( (LA4_0=='*') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:80:65: '*'
                    {
                    match('*'); 

                    }
                    break;

            }


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end ID

    // $ANTLR start WS
    public void mWS() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = WS;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:81:6: ( ( (' '|'\\t'|'\\n'))+ )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:81:6: ( (' '|'\\t'|'\\n'))+
            {
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:81:6: ( (' '|'\\t'|'\\n'))+
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
            	    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:81:7: (' '|'\\t'|'\\n')
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

             _channel=HIDDEN; 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end WS

    // $ANTLR start SLCOMMENT
    public void mSLCOMMENT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = SLCOMMENT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:13: ( '//' (~ ('\\n'|'\\r'))* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:13: '//' (~ ('\\n'|'\\r'))* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:18: (~ ('\\n'|'\\r'))*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);
                if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFE')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:19: ~ ('\\n'|'\\r')
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

            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:34: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:35: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:40: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:44: ( '\\n' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);
                    if ( (LA7_0=='\n') ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:45: '\\n'
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



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end SLCOMMENT

    public void mTokens() throws RecognitionException {
        // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:10: ( ARROW | LPAR | RPAR | COMA | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT )
        int alt9=18;
        switch ( input.LA(1) ) {
        case '-':
            alt9=1;
            break;
        case '(':
            alt9=2;
            break;
        case ')':
            alt9=3;
            break;
        case ',':
            alt9=4;
            break;
        case '=':
            alt9=5;
            break;
        case '!':
            alt9=6;
            break;
        case '.':
            alt9=7;
            break;
        case '<':
            int LA9_8 = input.LA(2);
            if ( (LA9_8=='=') ) {
                alt9=8;
            }
            else {
                alt9=9;}
            break;
        case '>':
            int LA9_9 = input.LA(2);
            if ( (LA9_9=='=') ) {
                alt9=10;
            }
            else {
                alt9=11;}
            break;
        case 'i':
            int LA9_10 = input.LA(2);
            if ( (LA9_10=='f') ) {
                int LA9_21 = input.LA(3);
                if ( (LA9_21=='*'||(LA9_21>='0' && LA9_21<='9')||(LA9_21>='A' && LA9_21<='Z')||LA9_21=='_'||(LA9_21>='a' && LA9_21<='z')) ) {
                    alt9=16;
                }
                else {
                    alt9=12;}
            }
            else {
                alt9=16;}
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
            alt9=13;
            break;
        case '\\':
            alt9=14;
            break;
        case '\"':
            alt9=15;
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
            alt9=16;
            break;
        case '\t':
        case '\n':
        case ' ':
            alt9=17;
            break;
        case '/':
            alt9=18;
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( ARROW | LPAR | RPAR | COMA | EQUALS | NOTEQUALS | DOT | LEQ | LT | GEQ | GT | IF | INT | ESC | STRING | ID | WS | SLCOMMENT );", 9, 0, input);

            throw nvae;
        }

        switch (alt9) {
            case 1 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:10: ARROW
                {
                mARROW(); 

                }
                break;
            case 2 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:16: LPAR
                {
                mLPAR(); 

                }
                break;
            case 3 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:21: RPAR
                {
                mRPAR(); 

                }
                break;
            case 4 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:26: COMA
                {
                mCOMA(); 

                }
                break;
            case 5 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:31: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 6 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:38: NOTEQUALS
                {
                mNOTEQUALS(); 

                }
                break;
            case 7 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:48: DOT
                {
                mDOT(); 

                }
                break;
            case 8 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:52: LEQ
                {
                mLEQ(); 

                }
                break;
            case 9 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:56: LT
                {
                mLT(); 

                }
                break;
            case 10 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:59: GEQ
                {
                mGEQ(); 

                }
                break;
            case 11 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:63: GT
                {
                mGT(); 

                }
                break;
            case 12 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:66: IF
                {
                mIF(); 

                }
                break;
            case 13 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:69: INT
                {
                mINT(); 

                }
                break;
            case 14 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:73: ESC
                {
                mESC(); 

                }
                break;
            case 15 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:77: STRING
                {
                mSTRING(); 

                }
                break;
            case 16 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:84: ID
                {
                mID(); 

                }
                break;
            case 17 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:87: WS
                {
                mWS(); 

                }
                break;
            case 18 :
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:1:90: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;

        }

    }


 

}