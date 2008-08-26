// $ANTLR 3.1 /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g 2008-08-26 17:23:29

package tom.gom.parser;
import tom.gom.adt.gom.GomTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GomLanguageLexer extends Lexer {
    public static final int COMMA=96;
    public static final int GomType=5;
    public static final int Sort=6;
    public static final int CutSort=8;
    public static final int ImportHook=10;
    public static final int Slots=13;
    public static final int KindSort=16;
    public static final int ABSTRACT=89;
    public static final int ShortSortClass=17;
    public static final int InterfaceHookDecl=20;
    public static final int KindModule=21;
    public static final int ConcModuleDecl=25;
    public static final int ConcField=27;
    public static final int DOT=86;
    public static final int PRIVATE=103;
    public static final int FullOperatorClass=29;
    public static final int Compare=31;
    public static final int SLCOMMENT=106;
    public static final int HookKind=32;
    public static final int Slot=33;
    public static final int CutOperator=34;
    public static final int SlotField=35;
    public static final int ConcGrammar=36;
    public static final int ConcSlot=39;
    public static final int MODULE=84;
    public static final int ConcClassName=40;
    public static final int RPAREN=97;
    public static final int MappingHook=42;
    public static final int BlockHookDecl=44;
    public static final int ConcProduction=43;
    public static final int SortClass=45;
    public static final int MappingHookDecl=47;
    public static final int ConcGomClass=46;
    public static final int ConcImportedModule=49;
    public static final int Production=51;
    public static final int ConcArg=50;
    public static final int IsEmpty=54;
    public static final int Import=55;
    public static final int InterfaceHook=57;
    public static final int ConcHookDecl=56;
    public static final int WS=105;
    public static final int Imports=59;
    public static final int Code=61;
    public static final int TomMapping=62;
    public static final int SORT=100;
    public static final int ConcGomType=63;
    public static final int CodeList=64;
    public static final int SEMI=94;
    public static final int EQUALS=92;
    public static final int AbstractTypeClass=66;
    public static final int BuiltinSortDecl=73;
    public static final int COLON=98;
    public static final int SYNTAX=90;
    public static final int VariadicOperatorClass=78;
    public static final int OptionList=79;
    public static final int ClassName=82;
    public static final int ConcSortDecl=83;
    public static final int Grammar=4;
    public static final int PUBLIC=88;
    public static final int ConcSlotField=7;
    public static final int ImportHookDecl=9;
    public static final int FullSortClass=11;
    public static final int ARROW=91;
    public static final int MakeBeforeHook=12;
    public static final int OperatorClass=14;
    public static final int SortType=15;
    public static final int Origin=18;
    public static final int ConcOperator=19;
    public static final int Arg=23;
    public static final int ConcHook=22;
    public static final int ModHookPair=24;
    public static final int ConcModule=26;
    public static final int ConcGomModule=28;
    public static final int LBRACE=99;
    public static final int RBRACE=104;
    public static final int Module=30;
    public static final int MLCOMMENT=107;
    public static final int ALT=93;
    public static final int ModuleDecl=37;
    public static final int Variadic=38;
    public static final int Cons=41;
    public static final int LPAREN=95;
    public static final int IMPORTS=87;
    public static final int OPERATOR=101;
    public static final int ID=85;
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
    public static final int GomModuleName=72;
    public static final int StarredField=76;
    public static final int BlockHook=75;
    public static final int KindOperator=74;
    public static final int ConcSection=77;
    public static final int STAR=102;
    public static final int Sorts=80;
    public static final int OperatorDecl=81;

    // delegates
    // delegators

    public GomLanguageLexer() {;} 
    public GomLanguageLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public GomLanguageLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g"; }

    // $ANTLR start "MODULE"
    public final void mMODULE() throws RecognitionException {
        try {
            int _type = MODULE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:153:10: ( 'module' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:153:12: 'module'
            {
            match("module"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MODULE"

    // $ANTLR start "IMPORTS"
    public final void mIMPORTS() throws RecognitionException {
        try {
            int _type = IMPORTS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:154:10: ( 'imports' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:154:12: 'imports'
            {
            match("imports"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IMPORTS"

    // $ANTLR start "PUBLIC"
    public final void mPUBLIC() throws RecognitionException {
        try {
            int _type = PUBLIC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:155:10: ( 'public' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:155:12: 'public'
            {
            match("public"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PUBLIC"

    // $ANTLR start "PRIVATE"
    public final void mPRIVATE() throws RecognitionException {
        try {
            int _type = PRIVATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:156:10: ( 'private' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:156:12: 'private'
            {
            match("private"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PRIVATE"

    // $ANTLR start "ABSTRACT"
    public final void mABSTRACT() throws RecognitionException {
        try {
            int _type = ABSTRACT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:157:10: ( 'abstract' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:157:12: 'abstract'
            {
            match("abstract"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ABSTRACT"

    // $ANTLR start "SYNTAX"
    public final void mSYNTAX() throws RecognitionException {
        try {
            int _type = SYNTAX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:158:10: ( 'syntax' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:158:12: 'syntax'
            {
            match("syntax"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SYNTAX"

    // $ANTLR start "SORT"
    public final void mSORT() throws RecognitionException {
        try {
            int _type = SORT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:159:10: ( 'sort' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:159:12: 'sort'
            {
            match("sort"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SORT"

    // $ANTLR start "OPERATOR"
    public final void mOPERATOR() throws RecognitionException {
        try {
            int _type = OPERATOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:160:10: ( 'operator' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:160:12: 'operator'
            {
            match("operator"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OPERATOR"

    // $ANTLR start "ARROW"
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:162:10: ( '->' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:162:12: '->'
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

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:163:10: ( ':' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:163:12: ':'
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

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:164:10: ( ',' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:164:12: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:165:10: ( '.' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:165:12: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:166:10: ( '(' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:166:12: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LPAREN"

    // $ANTLR start "RPAREN"
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:167:10: ( ')' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:167:12: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RPAREN"

    // $ANTLR start "STAR"
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:168:10: ( '*' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:168:12: '*'
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

    // $ANTLR start "EQUALS"
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:169:10: ( '=' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:169:12: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUALS"

    // $ANTLR start "ALT"
    public final void mALT() throws RecognitionException {
        try {
            int _type = ALT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:170:10: ( '|' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:170:12: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALT"

    // $ANTLR start "SEMI"
    public final void mSEMI() throws RecognitionException {
        try {
            int _type = SEMI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:171:10: ( ';;' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:171:12: ';;'
            {
            match(";;"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMI"

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:173:7: ( '{' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:173:9: '{'
            {
            match('{'); 

                SimpleBlockLexer lex = new SimpleBlockLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lex);
                SimpleBlockParser parser = new SimpleBlockParser(tokens);
                parser.block();
              

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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:182:7: ( '}' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:182:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:184:4: ( ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:184:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:184:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
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
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:184:8: ' '
                    {
                    match(' '); 

                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:185:10: '\\t'
                    {
                    match('\t'); 

                    }
                    break;
                case 3 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:186:10: ( '\\r\\n' | '\\n' | '\\r' )
                    {
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:186:10: ( '\\r\\n' | '\\n' | '\\r' )
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
                            new NoViableAltException("", 1, 0, input);

                        throw nvae;
                    }
                    switch (alt1) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:186:12: '\\r\\n'
                            {
                            match("\r\n"); 


                            }
                            break;
                        case 2 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:187:14: '\\n'
                            {
                            match('\n'); 

                            }
                            break;
                        case 3 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:188:14: '\\r'
                            {
                            match('\r'); 

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
    // $ANTLR end "WS"

    // $ANTLR start "SLCOMMENT"
    public final void mSLCOMMENT() throws RecognitionException {
        try {
            int _type = SLCOMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:194:11: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:195:3: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:195:8: (~ ( '\\n' | '\\r' ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\u0000' && LA3_0<='\t')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='\uFFFE')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:195:9: ~ ( '\\n' | '\\r' )
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
            	    break loop3;
                }
            } while (true);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:195:24: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:195:25: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:195:30: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:195:34: ( '\\n' )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='\n') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:195:35: '\\n'
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

    // $ANTLR start "MLCOMMENT"
    public final void mMLCOMMENT() throws RecognitionException {
        try {
            int _type = MLCOMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:199:11: ( '/*' ( . )* '*/' )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:200:3: '/*' ( . )* '*/'
            {
            match("/*"); 

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:200:8: ( . )*
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
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:200:8: .
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
    // $ANTLR end "MLCOMMENT"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:203:4: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )* )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:203:6: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:204:6: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='-'||(LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='Z')||LA7_0=='_'||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop7;
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

    public void mTokens() throws RecognitionException {
        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:8: ( MODULE | IMPORTS | PUBLIC | PRIVATE | ABSTRACT | SYNTAX | SORT | OPERATOR | ARROW | COLON | COMMA | DOT | LPAREN | RPAREN | STAR | EQUALS | ALT | SEMI | LBRACE | RBRACE | WS | SLCOMMENT | MLCOMMENT | ID )
        int alt8=24;
        alt8 = dfa8.predict(input);
        switch (alt8) {
            case 1 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:10: MODULE
                {
                mMODULE(); 

                }
                break;
            case 2 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:17: IMPORTS
                {
                mIMPORTS(); 

                }
                break;
            case 3 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:25: PUBLIC
                {
                mPUBLIC(); 

                }
                break;
            case 4 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:32: PRIVATE
                {
                mPRIVATE(); 

                }
                break;
            case 5 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:40: ABSTRACT
                {
                mABSTRACT(); 

                }
                break;
            case 6 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:49: SYNTAX
                {
                mSYNTAX(); 

                }
                break;
            case 7 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:56: SORT
                {
                mSORT(); 

                }
                break;
            case 8 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:61: OPERATOR
                {
                mOPERATOR(); 

                }
                break;
            case 9 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:70: ARROW
                {
                mARROW(); 

                }
                break;
            case 10 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:76: COLON
                {
                mCOLON(); 

                }
                break;
            case 11 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:82: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 12 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:88: DOT
                {
                mDOT(); 

                }
                break;
            case 13 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:92: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 14 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:99: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 15 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:106: STAR
                {
                mSTAR(); 

                }
                break;
            case 16 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:111: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 17 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:118: ALT
                {
                mALT(); 

                }
                break;
            case 18 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:122: SEMI
                {
                mSEMI(); 

                }
                break;
            case 19 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:127: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 20 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:134: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 21 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:141: WS
                {
                mWS(); 

                }
                break;
            case 22 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:144: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;
            case 23 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:154: MLCOMMENT
                {
                mMLCOMMENT(); 

                }
                break;
            case 24 :
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:1:164: ID
                {
                mID(); 

                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    static final String DFA8_eotS =
        "\1\uffff\6\25\17\uffff\10\25\2\uffff\16\25\1\66\7\25\1\uffff\1\25"+
        "\1\77\1\25\1\101\2\25\1\104\1\25\1\uffff\1\106\1\uffff\1\107\1\25"+
        "\1\uffff\1\25\2\uffff\1\112\1\113\2\uffff";
    static final String DFA8_eofS =
        "\114\uffff";
    static final String DFA8_minS =
        "\1\11\1\157\1\155\1\162\1\142\1\157\1\160\15\uffff\1\52\1\uffff"+
        "\1\144\1\160\1\142\1\151\1\163\1\156\1\162\1\145\2\uffff\1\165\1"+
        "\157\1\154\1\166\3\164\1\162\1\154\1\162\1\151\1\141\1\162\1\141"+
        "\1\55\1\141\1\145\1\164\1\143\1\164\1\141\1\170\1\uffff\1\164\1"+
        "\55\1\163\1\55\1\145\1\143\1\55\1\157\1\uffff\1\55\1\uffff\1\55"+
        "\1\164\1\uffff\1\162\2\uffff\2\55\2\uffff";
    static final String DFA8_maxS =
        "\1\175\1\157\1\155\1\165\1\142\1\171\1\160\15\uffff\1\57\1\uffff"+
        "\1\144\1\160\1\142\1\151\1\163\1\156\1\162\1\145\2\uffff\1\165\1"+
        "\157\1\154\1\166\3\164\1\162\1\154\1\162\1\151\1\141\1\162\1\141"+
        "\1\172\1\141\1\145\1\164\1\143\1\164\1\141\1\170\1\uffff\1\164\1"+
        "\172\1\163\1\172\1\145\1\143\1\172\1\157\1\uffff\1\172\1\uffff\1"+
        "\172\1\164\1\uffff\1\162\2\uffff\2\172\2\uffff";
    static final String DFA8_acceptS =
        "\7\uffff\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23"+
        "\1\24\1\25\1\uffff\1\30\10\uffff\1\26\1\27\26\uffff\1\7\10\uffff"+
        "\1\1\1\uffff\1\3\2\uffff\1\6\1\uffff\1\2\1\4\2\uffff\1\5\1\10";
    static final String DFA8_specialS =
        "\114\uffff}>";
    static final String[] DFA8_transitionS = {
            "\2\23\2\uffff\1\23\22\uffff\1\23\7\uffff\1\13\1\14\1\15\1\uffff"+
            "\1\11\1\7\1\12\1\24\12\uffff\1\10\1\20\1\uffff\1\16\3\uffff"+
            "\32\25\6\uffff\1\4\7\25\1\2\3\25\1\1\1\25\1\6\1\3\2\25\1\5\7"+
            "\25\1\21\1\17\1\22",
            "\1\26",
            "\1\27",
            "\1\31\2\uffff\1\30",
            "\1\32",
            "\1\34\11\uffff\1\33",
            "\1\35",
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
            "\1\37\4\uffff\1\36",
            "",
            "\1\40",
            "\1\41",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "\1\46",
            "\1\47",
            "",
            "",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\25\2\uffff\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "",
            "\1\76",
            "\1\25\2\uffff\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
            "\1\100",
            "\1\25\2\uffff\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
            "\1\102",
            "\1\103",
            "\1\25\2\uffff\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
            "\1\105",
            "",
            "\1\25\2\uffff\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
            "",
            "\1\25\2\uffff\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
            "\1\110",
            "",
            "\1\111",
            "",
            "",
            "\1\25\2\uffff\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
            "\1\25\2\uffff\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
            "",
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
            return "1:1: Tokens : ( MODULE | IMPORTS | PUBLIC | PRIVATE | ABSTRACT | SYNTAX | SORT | OPERATOR | ARROW | COLON | COMMA | DOT | LPAREN | RPAREN | STAR | EQUALS | ALT | SEMI | LBRACE | RBRACE | WS | SLCOMMENT | MLCOMMENT | ID );";
        }
    }
 

}