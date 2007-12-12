// $ANTLR 3.0 E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g 2007-12-11 17:14:21

package tom.gom.parser;
import tom.gom.adt.gom.GomTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GomLanguageLexer extends Lexer {
    public static final int Arg=4;
    public static final int OptionList=6;
    public static final int STAR=104;
    public static final int Cons=8;
    public static final int Hook=9;
    public static final int Origin=11;
    public static final int Module=12;
    public static final int AbstractTypeClass=13;
    public static final int Empty=15;
    public static final int GomModule=16;
    public static final int SLCOMMENT=108;
    public static final int EQUALS=94;
    public static final int Slot=21;
    public static final int EOF=-1;
    public static final int ImportHookDecl=23;
    public static final int ClassName=27;
    public static final int OperatorClass=29;
    public static final int FullOperatorClass=30;
    public static final int VisitableFwdClass=33;
    public static final int ConcImportedModule=34;
    public static final int ModuleDecl=36;
    public static final int ImportHook=37;
    public static final int RPAREN=99;
    public static final int ConcArg=38;
    public static final int SortClass=42;
    public static final int StarredField=43;
    public static final int ConcHookDecl=49;
    public static final int MLCOMMENT=109;
    public static final int ConcGomModule=52;
    public static final int GomType=51;
    public static final int ConcSection=56;
    public static final int RBRACE=106;
    public static final int Sorts=58;
    public static final int CodeList=59;
    public static final int PRIVATE=105;
    public static final int Compare=61;
    public static final int Code=64;
    public static final int BlockHookDecl=66;
    public static final int SlotField=68;
    public static final int ConcHook=69;
    public static final int ConcSlot=75;
    public static final int WS=107;
    public static final int BuiltinSortDecl=76;
    public static final int BlockHook=77;
    public static final int IsEmpty=79;
    public static final int IMPORTS=88;
    public static final int ConcField=83;
    public static final int ConcOperator=5;
    public static final int GomModuleName=7;
    public static final int ConcSortDecl=10;
    public static final int LBRACE=101;
    public static final int KindModule=14;
    public static final int ConcModuleDecl=17;
    public static final int NamedField=18;
    public static final int SortDecl=19;
    public static final int ABSTRACT=91;
    public static final int CutModule=22;
    public static final int ConcModule=20;
    public static final int ID=86;
    public static final int ConcSlotField=24;
    public static final int CutOperator=25;
    public static final int LPAREN=97;
    public static final int Grammar=28;
    public static final int FullSortClass=26;
    public static final int SORTS=90;
    public static final int TomMapping=32;
    public static final int InterfaceHookDecl=31;
    public static final int SYNTAX=92;
    public static final int Sort=35;
    public static final int MakeHook=40;
    public static final int Variadic=39;
    public static final int ALT=95;
    public static final int MappingHook=41;
    public static final int COMMA=98;
    public static final int CutSort=44;
    public static final int IsCons=45;
    public static final int ConcGomType=46;
    public static final int KindSort=47;
    public static final int ConcGomClass=48;
    public static final int DOT=87;
    public static final int ConcClassName=50;
    public static final int VisitorClass=53;
    public static final int OperatorDecl=54;
    public static final int MakeHookDecl=55;
    public static final int Import=57;
    public static final int ConcProduction=60;
    public static final int OPERATOR=103;
    public static final int ConcSort=62;
    public static final int InterfaceHook=63;
    public static final int ModHookPair=65;
    public static final int SORT=102;
    public static final int MODULE=85;
    public static final int Imports=67;
    public static final int Tokens=110;
    public static final int SEMI=96;
    public static final int ConcGrammar=71;
    public static final int Public=70;
    public static final int MakeBeforeHook=73;
    public static final int MappingHookDecl=72;
    public static final int COLON=100;
    public static final int VariadicOperatorClass=74;
    public static final int Slots=78;
    public static final int SortType=81;
    public static final int HookKind=80;
    public static final int KindOperator=82;
    public static final int ARROW=93;
    public static final int Production=84;
    public static final int PUBLIC=89;
    public GomLanguageLexer() {;} 
    public GomLanguageLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g"; }

    // $ANTLR start MODULE
    public final void mMODULE() throws RecognitionException {
        try {
            int _type = MODULE;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:157:12: ( 'module' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:157:12: 'module'
            {
            match("module"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end MODULE

    // $ANTLR start IMPORTS
    public final void mIMPORTS() throws RecognitionException {
        try {
            int _type = IMPORTS;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:158:12: ( 'imports' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:158:12: 'imports'
            {
            match("imports"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IMPORTS

    // $ANTLR start PUBLIC
    public final void mPUBLIC() throws RecognitionException {
        try {
            int _type = PUBLIC;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:159:12: ( 'public' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:159:12: 'public'
            {
            match("public"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PUBLIC

    // $ANTLR start PRIVATE
    public final void mPRIVATE() throws RecognitionException {
        try {
            int _type = PRIVATE;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:160:12: ( 'private' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:160:12: 'private'
            {
            match("private"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PRIVATE

    // $ANTLR start SORTS
    public final void mSORTS() throws RecognitionException {
        try {
            int _type = SORTS;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:161:12: ( 'sorts' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:161:12: 'sorts'
            {
            match("sorts"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SORTS

    // $ANTLR start ABSTRACT
    public final void mABSTRACT() throws RecognitionException {
        try {
            int _type = ABSTRACT;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:162:12: ( 'abstract' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:162:12: 'abstract'
            {
            match("abstract"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ABSTRACT

    // $ANTLR start SYNTAX
    public final void mSYNTAX() throws RecognitionException {
        try {
            int _type = SYNTAX;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:163:12: ( 'syntax' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:163:12: 'syntax'
            {
            match("syntax"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SYNTAX

    // $ANTLR start SORT
    public final void mSORT() throws RecognitionException {
        try {
            int _type = SORT;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:164:12: ( 'sort' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:164:12: 'sort'
            {
            match("sort"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SORT

    // $ANTLR start OPERATOR
    public final void mOPERATOR() throws RecognitionException {
        try {
            int _type = OPERATOR;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:165:12: ( 'operator' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:165:12: 'operator'
            {
            match("operator"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OPERATOR

    // $ANTLR start ARROW
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:167:12: ( '->' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:167:12: '->'
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
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:168:12: ( ':' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:168:12: ':'
            {
            match(':'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COLON

    // $ANTLR start COMMA
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:169:12: ( ',' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:169:12: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMA

    // $ANTLR start DOT
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:170:12: ( '.' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:170:12: '.'
            {
            match('.'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DOT

    // $ANTLR start LPAREN
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:171:12: ( '(' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:171:12: '('
            {
            match('('); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LPAREN

    // $ANTLR start RPAREN
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:172:12: ( ')' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:172:12: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RPAREN

    // $ANTLR start STAR
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:173:12: ( '*' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:173:12: '*'
            {
            match('*'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STAR

    // $ANTLR start EQUALS
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:174:12: ( '=' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:174:12: '='
            {
            match('='); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EQUALS

    // $ANTLR start ALT
    public final void mALT() throws RecognitionException {
        try {
            int _type = ALT;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:175:12: ( '|' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:175:12: '|'
            {
            match('|'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ALT

    // $ANTLR start SEMI
    public final void mSEMI() throws RecognitionException {
        try {
            int _type = SEMI;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:176:12: ( ';;' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:176:12: ';;'
            {
            match(";;"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SEMI

    // $ANTLR start LBRACE
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:178:9: ( '{' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:178:9: '{'
            {
            match('{'); 

                SimpleBlockLexer lex = new SimpleBlockLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lex);
                SimpleBlockParser parser = new SimpleBlockParser(tokens);
                parser.block();
              

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
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:187:9: ( '}' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:187:9: '}'
            {
            match('}'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RBRACE

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:189:6: ( ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) ) )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:189:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
            {
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:189:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
            int alt2=3;
            switch ( input.LA(1) ) {
            case ' ':
                {
                alt2=1;
                }
                break;
            case '\t':
                {
                alt2=2;
                }
                break;
            case '\n':
            case '\r':
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("189:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:189:8: ' '
                    {
                    match(' '); 

                    }
                    break;
                case 2 :
                    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:190:10: '\\t'
                    {
                    match('\t'); 

                    }
                    break;
                case 3 :
                    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:191:10: ( '\\r\\n' | '\\n' | '\\r' )
                    {
                    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:191:10: ( '\\r\\n' | '\\n' | '\\r' )
                    int alt1=3;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0=='\r') ) {
                        int LA1_1 = input.LA(2);

                        if ( (LA1_1=='\n') ) {
                            alt1=1;
                        }
                        else {
                            alt1=3;}
                    }
                    else if ( (LA1_0=='\n') ) {
                        alt1=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("191:10: ( '\\r\\n' | '\\n' | '\\r' )", 1, 0, input);

                        throw nvae;
                    }
                    switch (alt1) {
                        case 1 :
                            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:191:12: '\\r\\n'
                            {
                            match("\r\n"); 


                            }
                            break;
                        case 2 :
                            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:192:14: '\\n'
                            {
                            match('\n'); 

                            }
                            break;
                        case 3 :
                            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:193:14: '\\r'
                            {
                            match('\r'); 

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
    // $ANTLR end WS

    // $ANTLR start SLCOMMENT
    public final void mSLCOMMENT() throws RecognitionException {
        try {
            int _type = SLCOMMENT;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:200:3: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:200:3: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:200:8: (~ ( '\\n' | '\\r' ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\u0000' && LA3_0<='\t')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='\uFFFE')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:200:9: ~ ( '\\n' | '\\r' )
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
            	    break loop3;
                }
            } while (true);

            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:200:24: ( '\\n' | '\\r' ( '\\n' )? )?
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\n') ) {
                alt5=1;
            }
            else if ( (LA5_0=='\r') ) {
                alt5=2;
            }
            switch (alt5) {
                case 1 :
                    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:200:25: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:200:30: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:200:34: ( '\\n' )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='\n') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:200:35: '\\n'
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

    // $ANTLR start MLCOMMENT
    public final void mMLCOMMENT() throws RecognitionException {
        try {
            int _type = MLCOMMENT;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:205:3: ( '/*' ( . )* '*/' )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:205:3: '/*' ( . )* '*/'
            {
            match("/*"); 

            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:205:8: ( . )*
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
            	    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:205:8: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            match("*/"); 

            channel=HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end MLCOMMENT

    // $ANTLR start ID
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:208:6: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )* )
            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:208:6: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:209:6: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='-'||(LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='Z')||LA7_0=='_'||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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
            	    break loop7;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ID

    public void mTokens() throws RecognitionException {
        // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:10: ( MODULE | IMPORTS | PUBLIC | PRIVATE | SORTS | ABSTRACT | SYNTAX | SORT | OPERATOR | ARROW | COLON | COMMA | DOT | LPAREN | RPAREN | STAR | EQUALS | ALT | SEMI | LBRACE | RBRACE | WS | SLCOMMENT | MLCOMMENT | ID )
        int alt8=25;
        switch ( input.LA(1) ) {
        case 'm':
            {
            int LA8_1 = input.LA(2);

            if ( (LA8_1=='o') ) {
                int LA8_22 = input.LA(3);

                if ( (LA8_22=='d') ) {
                    int LA8_32 = input.LA(4);

                    if ( (LA8_32=='u') ) {
                        int LA8_40 = input.LA(5);

                        if ( (LA8_40=='l') ) {
                            int LA8_48 = input.LA(6);

                            if ( (LA8_48=='e') ) {
                                int LA8_57 = input.LA(7);

                                if ( (LA8_57=='-'||(LA8_57>='0' && LA8_57<='9')||(LA8_57>='A' && LA8_57<='Z')||LA8_57=='_'||(LA8_57>='a' && LA8_57<='z')) ) {
                                    alt8=25;
                                }
                                else {
                                    alt8=1;}
                            }
                            else {
                                alt8=25;}
                        }
                        else {
                            alt8=25;}
                    }
                    else {
                        alt8=25;}
                }
                else {
                    alt8=25;}
            }
            else {
                alt8=25;}
            }
            break;
        case 'i':
            {
            int LA8_2 = input.LA(2);

            if ( (LA8_2=='m') ) {
                int LA8_23 = input.LA(3);

                if ( (LA8_23=='p') ) {
                    int LA8_33 = input.LA(4);

                    if ( (LA8_33=='o') ) {
                        int LA8_41 = input.LA(5);

                        if ( (LA8_41=='r') ) {
                            int LA8_49 = input.LA(6);

                            if ( (LA8_49=='t') ) {
                                int LA8_58 = input.LA(7);

                                if ( (LA8_58=='s') ) {
                                    int LA8_66 = input.LA(8);

                                    if ( (LA8_66=='-'||(LA8_66>='0' && LA8_66<='9')||(LA8_66>='A' && LA8_66<='Z')||LA8_66=='_'||(LA8_66>='a' && LA8_66<='z')) ) {
                                        alt8=25;
                                    }
                                    else {
                                        alt8=2;}
                                }
                                else {
                                    alt8=25;}
                            }
                            else {
                                alt8=25;}
                        }
                        else {
                            alt8=25;}
                    }
                    else {
                        alt8=25;}
                }
                else {
                    alt8=25;}
            }
            else {
                alt8=25;}
            }
            break;
        case 'p':
            {
            switch ( input.LA(2) ) {
            case 'u':
                {
                int LA8_24 = input.LA(3);

                if ( (LA8_24=='b') ) {
                    int LA8_34 = input.LA(4);

                    if ( (LA8_34=='l') ) {
                        int LA8_42 = input.LA(5);

                        if ( (LA8_42=='i') ) {
                            int LA8_50 = input.LA(6);

                            if ( (LA8_50=='c') ) {
                                int LA8_59 = input.LA(7);

                                if ( (LA8_59=='-'||(LA8_59>='0' && LA8_59<='9')||(LA8_59>='A' && LA8_59<='Z')||LA8_59=='_'||(LA8_59>='a' && LA8_59<='z')) ) {
                                    alt8=25;
                                }
                                else {
                                    alt8=3;}
                            }
                            else {
                                alt8=25;}
                        }
                        else {
                            alt8=25;}
                    }
                    else {
                        alt8=25;}
                }
                else {
                    alt8=25;}
                }
                break;
            case 'r':
                {
                int LA8_25 = input.LA(3);

                if ( (LA8_25=='i') ) {
                    int LA8_35 = input.LA(4);

                    if ( (LA8_35=='v') ) {
                        int LA8_43 = input.LA(5);

                        if ( (LA8_43=='a') ) {
                            int LA8_51 = input.LA(6);

                            if ( (LA8_51=='t') ) {
                                int LA8_60 = input.LA(7);

                                if ( (LA8_60=='e') ) {
                                    int LA8_68 = input.LA(8);

                                    if ( (LA8_68=='-'||(LA8_68>='0' && LA8_68<='9')||(LA8_68>='A' && LA8_68<='Z')||LA8_68=='_'||(LA8_68>='a' && LA8_68<='z')) ) {
                                        alt8=25;
                                    }
                                    else {
                                        alt8=4;}
                                }
                                else {
                                    alt8=25;}
                            }
                            else {
                                alt8=25;}
                        }
                        else {
                            alt8=25;}
                    }
                    else {
                        alt8=25;}
                }
                else {
                    alt8=25;}
                }
                break;
            default:
                alt8=25;}

            }
            break;
        case 's':
            {
            switch ( input.LA(2) ) {
            case 'o':
                {
                int LA8_26 = input.LA(3);

                if ( (LA8_26=='r') ) {
                    int LA8_36 = input.LA(4);

                    if ( (LA8_36=='t') ) {
                        switch ( input.LA(5) ) {
                        case 's':
                            {
                            int LA8_52 = input.LA(6);

                            if ( (LA8_52=='-'||(LA8_52>='0' && LA8_52<='9')||(LA8_52>='A' && LA8_52<='Z')||LA8_52=='_'||(LA8_52>='a' && LA8_52<='z')) ) {
                                alt8=25;
                            }
                            else {
                                alt8=5;}
                            }
                            break;
                        case '-':
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
                        case 't':
                        case 'u':
                        case 'v':
                        case 'w':
                        case 'x':
                        case 'y':
                        case 'z':
                            {
                            alt8=25;
                            }
                            break;
                        default:
                            alt8=8;}

                    }
                    else {
                        alt8=25;}
                }
                else {
                    alt8=25;}
                }
                break;
            case 'y':
                {
                int LA8_27 = input.LA(3);

                if ( (LA8_27=='n') ) {
                    int LA8_37 = input.LA(4);

                    if ( (LA8_37=='t') ) {
                        int LA8_45 = input.LA(5);

                        if ( (LA8_45=='a') ) {
                            int LA8_54 = input.LA(6);

                            if ( (LA8_54=='x') ) {
                                int LA8_62 = input.LA(7);

                                if ( (LA8_62=='-'||(LA8_62>='0' && LA8_62<='9')||(LA8_62>='A' && LA8_62<='Z')||LA8_62=='_'||(LA8_62>='a' && LA8_62<='z')) ) {
                                    alt8=25;
                                }
                                else {
                                    alt8=7;}
                            }
                            else {
                                alt8=25;}
                        }
                        else {
                            alt8=25;}
                    }
                    else {
                        alt8=25;}
                }
                else {
                    alt8=25;}
                }
                break;
            default:
                alt8=25;}

            }
            break;
        case 'a':
            {
            int LA8_5 = input.LA(2);

            if ( (LA8_5=='b') ) {
                int LA8_28 = input.LA(3);

                if ( (LA8_28=='s') ) {
                    int LA8_38 = input.LA(4);

                    if ( (LA8_38=='t') ) {
                        int LA8_46 = input.LA(5);

                        if ( (LA8_46=='r') ) {
                            int LA8_55 = input.LA(6);

                            if ( (LA8_55=='a') ) {
                                int LA8_63 = input.LA(7);

                                if ( (LA8_63=='c') ) {
                                    int LA8_70 = input.LA(8);

                                    if ( (LA8_70=='t') ) {
                                        int LA8_74 = input.LA(9);

                                        if ( (LA8_74=='-'||(LA8_74>='0' && LA8_74<='9')||(LA8_74>='A' && LA8_74<='Z')||LA8_74=='_'||(LA8_74>='a' && LA8_74<='z')) ) {
                                            alt8=25;
                                        }
                                        else {
                                            alt8=6;}
                                    }
                                    else {
                                        alt8=25;}
                                }
                                else {
                                    alt8=25;}
                            }
                            else {
                                alt8=25;}
                        }
                        else {
                            alt8=25;}
                    }
                    else {
                        alt8=25;}
                }
                else {
                    alt8=25;}
            }
            else {
                alt8=25;}
            }
            break;
        case 'o':
            {
            int LA8_6 = input.LA(2);

            if ( (LA8_6=='p') ) {
                int LA8_29 = input.LA(3);

                if ( (LA8_29=='e') ) {
                    int LA8_39 = input.LA(4);

                    if ( (LA8_39=='r') ) {
                        int LA8_47 = input.LA(5);

                        if ( (LA8_47=='a') ) {
                            int LA8_56 = input.LA(6);

                            if ( (LA8_56=='t') ) {
                                int LA8_64 = input.LA(7);

                                if ( (LA8_64=='o') ) {
                                    int LA8_71 = input.LA(8);

                                    if ( (LA8_71=='r') ) {
                                        int LA8_75 = input.LA(9);

                                        if ( (LA8_75=='-'||(LA8_75>='0' && LA8_75<='9')||(LA8_75>='A' && LA8_75<='Z')||LA8_75=='_'||(LA8_75>='a' && LA8_75<='z')) ) {
                                            alt8=25;
                                        }
                                        else {
                                            alt8=9;}
                                    }
                                    else {
                                        alt8=25;}
                                }
                                else {
                                    alt8=25;}
                            }
                            else {
                                alt8=25;}
                        }
                        else {
                            alt8=25;}
                    }
                    else {
                        alt8=25;}
                }
                else {
                    alt8=25;}
            }
            else {
                alt8=25;}
            }
            break;
        case '-':
            {
            alt8=10;
            }
            break;
        case ':':
            {
            alt8=11;
            }
            break;
        case ',':
            {
            alt8=12;
            }
            break;
        case '.':
            {
            alt8=13;
            }
            break;
        case '(':
            {
            alt8=14;
            }
            break;
        case ')':
            {
            alt8=15;
            }
            break;
        case '*':
            {
            alt8=16;
            }
            break;
        case '=':
            {
            alt8=17;
            }
            break;
        case '|':
            {
            alt8=18;
            }
            break;
        case ';':
            {
            alt8=19;
            }
            break;
        case '{':
            {
            alt8=20;
            }
            break;
        case '}':
            {
            alt8=21;
            }
            break;
        case '\t':
        case '\n':
        case '\r':
        case ' ':
            {
            alt8=22;
            }
            break;
        case '/':
            {
            int LA8_20 = input.LA(2);

            if ( (LA8_20=='*') ) {
                alt8=24;
            }
            else if ( (LA8_20=='/') ) {
                alt8=23;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("1:1: Tokens : ( MODULE | IMPORTS | PUBLIC | PRIVATE | SORTS | ABSTRACT | SYNTAX | SORT | OPERATOR | ARROW | COLON | COMMA | DOT | LPAREN | RPAREN | STAR | EQUALS | ALT | SEMI | LBRACE | RBRACE | WS | SLCOMMENT | MLCOMMENT | ID );", 8, 20, input);

                throw nvae;
            }
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
        case 'n':
        case 'q':
        case 'r':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt8=25;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( MODULE | IMPORTS | PUBLIC | PRIVATE | SORTS | ABSTRACT | SYNTAX | SORT | OPERATOR | ARROW | COLON | COMMA | DOT | LPAREN | RPAREN | STAR | EQUALS | ALT | SEMI | LBRACE | RBRACE | WS | SLCOMMENT | MLCOMMENT | ID );", 8, 0, input);

            throw nvae;
        }

        switch (alt8) {
            case 1 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:10: MODULE
                {
                mMODULE(); 

                }
                break;
            case 2 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:17: IMPORTS
                {
                mIMPORTS(); 

                }
                break;
            case 3 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:25: PUBLIC
                {
                mPUBLIC(); 

                }
                break;
            case 4 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:32: PRIVATE
                {
                mPRIVATE(); 

                }
                break;
            case 5 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:40: SORTS
                {
                mSORTS(); 

                }
                break;
            case 6 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:46: ABSTRACT
                {
                mABSTRACT(); 

                }
                break;
            case 7 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:55: SYNTAX
                {
                mSYNTAX(); 

                }
                break;
            case 8 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:62: SORT
                {
                mSORT(); 

                }
                break;
            case 9 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:67: OPERATOR
                {
                mOPERATOR(); 

                }
                break;
            case 10 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:76: ARROW
                {
                mARROW(); 

                }
                break;
            case 11 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:82: COLON
                {
                mCOLON(); 

                }
                break;
            case 12 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:88: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 13 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:94: DOT
                {
                mDOT(); 

                }
                break;
            case 14 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:98: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 15 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:105: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 16 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:112: STAR
                {
                mSTAR(); 

                }
                break;
            case 17 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:117: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 18 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:124: ALT
                {
                mALT(); 

                }
                break;
            case 19 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:128: SEMI
                {
                mSEMI(); 

                }
                break;
            case 20 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:133: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 21 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:140: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 22 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:147: WS
                {
                mWS(); 

                }
                break;
            case 23 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:150: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;
            case 24 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:160: MLCOMMENT
                {
                mMLCOMMENT(); 

                }
                break;
            case 25 :
                // E:\\dev\\tom\\src\\gen\\tom\\gom\\parser\\GomLanguage.g:1:170: ID
                {
                mID(); 

                }
                break;

        }

    }


 

}