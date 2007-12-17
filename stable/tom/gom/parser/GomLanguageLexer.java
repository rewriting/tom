// $ANTLR 3.0 /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g 2007-12-17 11:02:27

package tom.gom.parser;
import tom.gom.adt.gom.GomTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GomLanguageLexer extends Lexer {
    public static final int COMMA=98;
    public static final int GomType=5;
    public static final int Sort=6;
    public static final int CutSort=8;
    public static final int ImportHook=10;
    public static final int Slots=13;
    public static final int KindSort=16;
    public static final int ABSTRACT=91;
    public static final int InterfaceHookDecl=19;
    public static final int KindModule=20;
    public static final int ConcModuleDecl=24;
    public static final int ConcField=26;
    public static final int DOT=87;
    public static final int SORTS=90;
    public static final int PRIVATE=105;
    public static final int FullOperatorClass=28;
    public static final int Compare=30;
    public static final int SLCOMMENT=108;
    public static final int HookKind=31;
    public static final int Slot=32;
    public static final int VisitorClass=33;
    public static final int CutOperator=34;
    public static final int SlotField=35;
    public static final int ConcGrammar=36;
    public static final int ConcSlot=39;
    public static final int MODULE=85;
    public static final int ConcClassName=40;
    public static final int RPAREN=99;
    public static final int MappingHook=42;
    public static final int BlockHookDecl=44;
    public static final int ConcProduction=43;
    public static final int SortClass=45;
    public static final int ConcGomClass=47;
    public static final int MappingHookDecl=46;
    public static final int ConcImportedModule=49;
    public static final int Production=51;
    public static final int ConcArg=50;
    public static final int IsEmpty=54;
    public static final int Import=55;
    public static final int ConcHookDecl=56;
    public static final int InterfaceHook=57;
    public static final int WS=107;
    public static final int Imports=59;
    public static final int Code=61;
    public static final int TomMapping=62;
    public static final int SORT=102;
    public static final int ConcGomType=63;
    public static final int CodeList=64;
    public static final int SEMI=96;
    public static final int EQUALS=94;
    public static final int AbstractTypeClass=66;
    public static final int BuiltinSortDecl=74;
    public static final int COLON=100;
    public static final int SYNTAX=92;
    public static final int VariadicOperatorClass=79;
    public static final int OptionList=80;
    public static final int ClassName=83;
    public static final int ConcSortDecl=84;
    public static final int Grammar=4;
    public static final int PUBLIC=89;
    public static final int ConcSlotField=7;
    public static final int ImportHookDecl=9;
    public static final int ARROW=93;
    public static final int FullSortClass=11;
    public static final int MakeBeforeHook=12;
    public static final int OperatorClass=14;
    public static final int SortType=15;
    public static final int Origin=17;
    public static final int ConcOperator=18;
    public static final int Arg=22;
    public static final int ConcHook=21;
    public static final int ModHookPair=23;
    public static final int ConcModule=25;
    public static final int ConcGomModule=27;
    public static final int LBRACE=101;
    public static final int RBRACE=106;
    public static final int Module=29;
    public static final int MLCOMMENT=109;
    public static final int ALT=95;
    public static final int ModuleDecl=37;
    public static final int Variadic=38;
    public static final int Cons=41;
    public static final int LPAREN=97;
    public static final int IMPORTS=88;
    public static final int OPERATOR=103;
    public static final int ID=86;
    public static final int ConcSort=48;
    public static final int IsCons=52;
    public static final int SortDecl=53;
    public static final int Hook=58;
    public static final int Public=60;
    public static final int MakeHookDecl=65;
    public static final int NamedField=67;
    public static final int CutModule=68;
    public static final int MakeHook=70;
    public static final int GomModule=69;
    public static final int Empty=71;
    public static final int EOF=-1;
    public static final int VisitableFwdClass=72;
    public static final int GomModuleName=73;
    public static final int Tokens=110;
    public static final int StarredField=77;
    public static final int BlockHook=76;
    public static final int KindOperator=75;
    public static final int ConcSection=78;
    public static final int STAR=104;
    public static final int Sorts=81;
    public static final int OperatorDecl=82;
    public GomLanguageLexer() {;} 
    public GomLanguageLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g"; }

    // $ANTLR start MODULE
    public final void mMODULE() throws RecognitionException {
        try {
            int _type = MODULE;
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:157:12: ( 'module' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:157:12: 'module'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:158:12: ( 'imports' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:158:12: 'imports'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:159:12: ( 'public' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:159:12: 'public'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:160:12: ( 'private' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:160:12: 'private'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:161:12: ( 'sorts' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:161:12: 'sorts'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:162:12: ( 'abstract' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:162:12: 'abstract'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:163:12: ( 'syntax' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:163:12: 'syntax'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:164:12: ( 'sort' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:164:12: 'sort'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:165:12: ( 'operator' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:165:12: 'operator'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:167:12: ( '->' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:167:12: '->'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:168:12: ( ':' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:168:12: ':'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:169:12: ( ',' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:169:12: ','
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:170:12: ( '.' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:170:12: '.'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:171:12: ( '(' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:171:12: '('
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:172:12: ( ')' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:172:12: ')'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:173:12: ( '*' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:173:12: '*'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:174:12: ( '=' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:174:12: '='
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:175:12: ( '|' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:175:12: '|'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:176:12: ( ';;' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:176:12: ';;'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:178:9: ( '{' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:178:9: '{'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:187:9: ( '}' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:187:9: '}'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:189:6: ( ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) ) )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:189:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
            {
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:189:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
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
                    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:189:8: ' '
                    {
                    match(' '); 

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:190:10: '\\t'
                    {
                    match('\t'); 

                    }
                    break;
                case 3 :
                    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:191:10: ( '\\r\\n' | '\\n' | '\\r' )
                    {
                    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:191:10: ( '\\r\\n' | '\\n' | '\\r' )
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
                            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:191:12: '\\r\\n'
                            {
                            match("\r\n"); 


                            }
                            break;
                        case 2 :
                            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:192:14: '\\n'
                            {
                            match('\n'); 

                            }
                            break;
                        case 3 :
                            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:193:14: '\\r'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:200:3: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:200:3: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:200:8: (~ ( '\\n' | '\\r' ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\u0000' && LA3_0<='\t')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='\uFFFE')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:200:9: ~ ( '\\n' | '\\r' )
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

            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:200:24: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:200:25: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:200:30: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:200:34: ( '\\n' )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='\n') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:200:35: '\\n'
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:205:3: ( '/*' ( . )* '*/' )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:205:3: '/*' ( . )* '*/'
            {
            match("/*"); 

            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:205:8: ( . )*
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
            	    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:205:8: .
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
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:208:6: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )* )
            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:208:6: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:209:6: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='-'||(LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='Z')||LA7_0=='_'||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:
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
        // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:10: ( MODULE | IMPORTS | PUBLIC | PRIVATE | SORTS | ABSTRACT | SYNTAX | SORT | OPERATOR | ARROW | COLON | COMMA | DOT | LPAREN | RPAREN | STAR | EQUALS | ALT | SEMI | LBRACE | RBRACE | WS | SLCOMMENT | MLCOMMENT | ID )
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
            case 'r':
                {
                int LA8_24 = input.LA(3);

                if ( (LA8_24=='i') ) {
                    int LA8_34 = input.LA(4);

                    if ( (LA8_34=='v') ) {
                        int LA8_42 = input.LA(5);

                        if ( (LA8_42=='a') ) {
                            int LA8_50 = input.LA(6);

                            if ( (LA8_50=='t') ) {
                                int LA8_59 = input.LA(7);

                                if ( (LA8_59=='e') ) {
                                    int LA8_67 = input.LA(8);

                                    if ( (LA8_67=='-'||(LA8_67>='0' && LA8_67<='9')||(LA8_67>='A' && LA8_67<='Z')||LA8_67=='_'||(LA8_67>='a' && LA8_67<='z')) ) {
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
            case 'u':
                {
                int LA8_25 = input.LA(3);

                if ( (LA8_25=='b') ) {
                    int LA8_35 = input.LA(4);

                    if ( (LA8_35=='l') ) {
                        int LA8_43 = input.LA(5);

                        if ( (LA8_43=='i') ) {
                            int LA8_51 = input.LA(6);

                            if ( (LA8_51=='c') ) {
                                int LA8_60 = input.LA(7);

                                if ( (LA8_60=='-'||(LA8_60>='0' && LA8_60<='9')||(LA8_60>='A' && LA8_60<='Z')||LA8_60=='_'||(LA8_60>='a' && LA8_60<='z')) ) {
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
            default:
                alt8=25;}

            }
            break;
        case 's':
            {
            switch ( input.LA(2) ) {
            case 'y':
                {
                int LA8_26 = input.LA(3);

                if ( (LA8_26=='n') ) {
                    int LA8_36 = input.LA(4);

                    if ( (LA8_36=='t') ) {
                        int LA8_44 = input.LA(5);

                        if ( (LA8_44=='a') ) {
                            int LA8_52 = input.LA(6);

                            if ( (LA8_52=='x') ) {
                                int LA8_61 = input.LA(7);

                                if ( (LA8_61=='-'||(LA8_61>='0' && LA8_61<='9')||(LA8_61>='A' && LA8_61<='Z')||LA8_61=='_'||(LA8_61>='a' && LA8_61<='z')) ) {
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
            case 'o':
                {
                int LA8_27 = input.LA(3);

                if ( (LA8_27=='r') ) {
                    int LA8_37 = input.LA(4);

                    if ( (LA8_37=='t') ) {
                        switch ( input.LA(5) ) {
                        case 's':
                            {
                            int LA8_53 = input.LA(6);

                            if ( (LA8_53=='-'||(LA8_53>='0' && LA8_53<='9')||(LA8_53>='A' && LA8_53<='Z')||LA8_53=='_'||(LA8_53>='a' && LA8_53<='z')) ) {
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

            if ( (LA8_20=='/') ) {
                alt8=23;
            }
            else if ( (LA8_20=='*') ) {
                alt8=24;
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
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:10: MODULE
                {
                mMODULE(); 

                }
                break;
            case 2 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:17: IMPORTS
                {
                mIMPORTS(); 

                }
                break;
            case 3 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:25: PUBLIC
                {
                mPUBLIC(); 

                }
                break;
            case 4 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:32: PRIVATE
                {
                mPRIVATE(); 

                }
                break;
            case 5 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:40: SORTS
                {
                mSORTS(); 

                }
                break;
            case 6 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:46: ABSTRACT
                {
                mABSTRACT(); 

                }
                break;
            case 7 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:55: SYNTAX
                {
                mSYNTAX(); 

                }
                break;
            case 8 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:62: SORT
                {
                mSORT(); 

                }
                break;
            case 9 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:67: OPERATOR
                {
                mOPERATOR(); 

                }
                break;
            case 10 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:76: ARROW
                {
                mARROW(); 

                }
                break;
            case 11 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:82: COLON
                {
                mCOLON(); 

                }
                break;
            case 12 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:88: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 13 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:94: DOT
                {
                mDOT(); 

                }
                break;
            case 14 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:98: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 15 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:105: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 16 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:112: STAR
                {
                mSTAR(); 

                }
                break;
            case 17 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:117: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 18 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:124: ALT
                {
                mALT(); 

                }
                break;
            case 19 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:128: SEMI
                {
                mSEMI(); 

                }
                break;
            case 20 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:133: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 21 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:140: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 22 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:147: WS
                {
                mWS(); 

                }
                break;
            case 23 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:150: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;
            case 24 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:160: MLCOMMENT
                {
                mMLCOMMENT(); 

                }
                break;
            case 25 :
                // /home/balland/workspace/newsl/src/gen/tom/gom/parser/GomLanguage.g:1:170: ID
                {
                mID(); 

                }
                break;

        }

    }


 

}