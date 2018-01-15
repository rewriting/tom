// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g 2018-01-15 14:09:45

package tom.gom.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GomLanguageLexer extends Lexer {
    public static final int Arg=63;
    public static final int OptionList=45;
    public static final int STAR=119;
    public static final int Cons=98;
    public static final int Hook=67;
    public static final int Origin=46;
    public static final int Module=89;
    public static final int AbstractTypeClass=23;
    public static final int GomModule=86;
    public static final int Empty=99;
    public static final int ExpressionType=91;
    public static final int SLCOMMENT=133;
    public static final int EQUALS=111;
    public static final int Inner=52;
    public static final int PatternType=92;
    public static final int Slot=80;
    public static final int ATOM=110;
    public static final int EOF=-1;
    public static final int ImportHookDecl=76;
    public static final int ClassName=24;
    public static final int OperatorClass=21;
    public static final int FullOperatorClass=95;
    public static final int BINDS=112;
    public static final int ConcImportedModule=82;
    public static final int ModuleDecl=54;
    public static final int ImportHook=12;
    public static final int RPAREN=118;
    public static final int ConcArg=88;
    public static final int Neutral=50;
    public static final int SortClass=22;
    public static final int StarredField=48;
    public static final int ConcHookDecl=74;
    public static final int ConcAtom=31;
    public static final int MLCOMMENT=134;
    public static final int ConcGomModule=68;
    public static final int GomType=81;
    public static final int ConcSection=64;
    public static final int Details=43;
    public static final int RBRACE=131;
    public static final int CodeList=93;
    public static final int PRIVATE=129;
    public static final int Compare=94;
    public static final int Code=102;
    public static final int BlockHookDecl=78;
    public static final int NEUTRAL=125;
    public static final int None=53;
    public static final int SlotField=17;
    public static final int ConcHook=26;
    public static final int ConcSlot=58;
    public static final int WS=132;
    public static final int BuiltinSortDecl=70;
    public static final int BlockHook=14;
    public static final int IsEmpty=101;
    public static final int AtomType=90;
    public static final int Outer=51;
    public static final int IMPORTS=106;
    public static final int ConcField=35;
    public static final int ShortSortClass=96;
    public static final int LDIPLE=120;
    public static final int Alternative=56;
    public static final int ConcOperator=41;
    public static final int GomModuleName=57;
    public static final int INNER=123;
    public static final int ConcSortDecl=55;
    public static final int LBRACE=126;
    public static final int KindModule=40;
    public static final int NamedField=47;
    public static final int ConcModuleDecl=42;
    public static final int SortDecl=71;
    public static final int ABSTRACT=108;
    public static final int AtomDecl=65;
    public static final int CutModule=62;
    public static final int ConcModule=85;
    public static final int ID=104;
    public static final int ConcSlotField=18;
    public static final int CutOperator=60;
    public static final int LPAREN=116;
    public static final int FullSortClass=97;
    public static final int InterfaceHookDecl=77;
    public static final int TomMapping=19;
    public static final int SYNTAX=109;
    public static final int Sort=27;
    public static final int MakeHook=16;
    public static final int Variadic=29;
    public static final int ALT=114;
    public static final int MappingHook=11;
    public static final int COMMA=117;
    public static final int CutSort=61;
    public static final int IsCons=100;
    public static final int ConcGomType=73;
    public static final int KindSort=39;
    public static final int ConcGomClass=25;
    public static final int DOT=105;
    public static final int HasTomCode=44;
    public static final int ConcClassName=10;
    public static final int OperatorDecl=69;
    public static final int FutureCons=32;
    public static final int OUTER=124;
    public static final int MakeHookDecl=79;
    public static final int JAVADOC=113;
    public static final int ConcAlternative=87;
    public static final int ConcProduction=72;
    public static final int RDIPLE=121;
    public static final int OPERATOR=128;
    public static final int ConcSort=28;
    public static final int InterfaceHook=13;
    public static final int ModHookPair=36;
    public static final int SORT=127;
    public static final int CutFutureOperator=59;
    public static final int FutureNil=33;
    public static final int Imports=84;
    public static final int MODULE=103;
    public static final int SEMI=115;
    public static final int Public=83;
    public static final int MakeBeforeHook=15;
    public static final int MappingHookDecl=75;
    public static final int COLON=122;
    public static final int VariadicOperatorClass=20;
    public static final int Refresh=49;
    public static final int Slots=30;
    public static final int SortType=66;
    public static final int HookKind=34;
    public static final int KindOperator=38;
    public static final int ARROW=130;
    public static final int PUBLIC=107;
    public static final int KindFutureOperator=37;

    // delegates
    // delegators

    public GomLanguageLexer() {;} 
    public GomLanguageLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public GomLanguageLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g"; }

    // $ANTLR start "MODULE"
    public final void mMODULE() throws RecognitionException {
        try {
            int _type = MODULE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:193:10: ( 'module' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:193:12: 'module'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:194:10: ( 'imports' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:194:12: 'imports'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:195:10: ( 'public' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:195:12: 'public'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:196:10: ( 'private' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:196:12: 'private'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:197:10: ( 'abstract' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:197:12: 'abstract'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:198:10: ( 'syntax' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:198:12: 'syntax'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:199:10: ( 'sort' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:199:12: 'sort'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:200:10: ( 'operator' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:200:12: 'operator'
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

    // $ANTLR start "ATOM"
    public final void mATOM() throws RecognitionException {
        try {
            int _type = ATOM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:201:10: ( 'atom' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:201:12: 'atom'
            {
            match("atom"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ATOM"

    // $ANTLR start "INNER"
    public final void mINNER() throws RecognitionException {
        try {
            int _type = INNER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:202:10: ( 'inner' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:202:12: 'inner'
            {
            match("inner"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INNER"

    // $ANTLR start "OUTER"
    public final void mOUTER() throws RecognitionException {
        try {
            int _type = OUTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:203:10: ( 'outer' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:203:12: 'outer'
            {
            match("outer"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OUTER"

    // $ANTLR start "NEUTRAL"
    public final void mNEUTRAL() throws RecognitionException {
        try {
            int _type = NEUTRAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:204:10: ( 'neutral' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:204:12: 'neutral'
            {
            match("neutral"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEUTRAL"

    // $ANTLR start "BINDS"
    public final void mBINDS() throws RecognitionException {
        try {
            int _type = BINDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:205:10: ( 'binds' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:205:12: 'binds'
            {
            match("binds"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BINDS"

    // $ANTLR start "ARROW"
    public final void mARROW() throws RecognitionException {
        try {
            int _type = ARROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:207:10: ( '->' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:207:12: '->'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:208:10: ( ':' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:208:12: ':'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:209:10: ( ',' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:209:12: ','
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:210:10: ( '.' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:210:12: '.'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:211:10: ( '(' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:211:12: '('
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:212:10: ( ')' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:212:12: ')'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:213:10: ( '*' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:213:12: '*'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:214:10: ( '=' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:214:12: '='
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:215:10: ( '|' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:215:12: '|'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:216:10: ( ';;' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:216:12: ';;'
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

    // $ANTLR start "LDIPLE"
    public final void mLDIPLE() throws RecognitionException {
        try {
            int _type = LDIPLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:217:10: ( '<' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:217:12: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LDIPLE"

    // $ANTLR start "RDIPLE"
    public final void mRDIPLE() throws RecognitionException {
        try {
            int _type = RDIPLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:218:10: ( '>' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:218:12: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RDIPLE"

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:220:7: ( '{' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:220:9: '{'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:229:7: ( '}' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:229:9: '}'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:231:4: ( ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:231:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:231:6: ( ' ' | '\\t' | ( '\\r\\n' | '\\n' | '\\r' ) )
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
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:231:8: ' '
                    {
                    match(' '); 

                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:232:10: '\\t'
                    {
                    match('\t'); 

                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:233:10: ( '\\r\\n' | '\\n' | '\\r' )
                    {
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:233:10: ( '\\r\\n' | '\\n' | '\\r' )
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
                            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:233:12: '\\r\\n'
                            {
                            match("\r\n"); 


                            }
                            break;
                        case 2 :
                            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:234:14: '\\n'
                            {
                            match('\n'); 

                            }
                            break;
                        case 3 :
                            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:235:14: '\\r'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:241:11: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )? )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:242:3: '//' (~ ( '\\n' | '\\r' ) )* ( '\\n' | '\\r' ( '\\n' )? )?
            {
            match("//"); 

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:242:8: (~ ( '\\n' | '\\r' ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\u0000' && LA3_0<='\t')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='\uFFFF')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:242:9: ~ ( '\\n' | '\\r' )
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
            	    break loop3;
                }
            } while (true);

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:242:24: ( '\\n' | '\\r' ( '\\n' )? )?
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
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:242:25: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:242:30: '\\r' ( '\\n' )?
                    {
                    match('\r'); 
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:242:34: ( '\\n' )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='\n') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:242:35: '\\n'
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
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:246:11: ( '/*' ~ '*' ( . )* '*/' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:247:3: '/*' ~ '*' ( . )* '*/'
            {
            match("/*"); 

            if ( (input.LA(1)>='\u0000' && input.LA(1)<=')')||(input.LA(1)>='+' && input.LA(1)<='\uFFFF') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:247:12: ( . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='*') ) {
                    int LA6_1 = input.LA(2);

                    if ( (LA6_1=='/') ) {
                        alt6=2;
                    }
                    else if ( ((LA6_1>='\u0000' && LA6_1<='.')||(LA6_1>='0' && LA6_1<='\uFFFF')) ) {
                        alt6=1;
                    }


                }
                else if ( ((LA6_0>='\u0000' && LA6_0<=')')||(LA6_0>='+' && LA6_0<='\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:247:12: .
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

    // $ANTLR start "JAVADOC"
    public final void mJAVADOC() throws RecognitionException {
        try {
            int _type = JAVADOC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:251:9: ( '/**' ( . )* '*/' )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:252:3: '/**' ( . )* '*/'
            {
            match("/**"); 

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:252:9: ( . )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='*') ) {
                    int LA7_1 = input.LA(2);

                    if ( (LA7_1=='/') ) {
                        alt7=2;
                    }
                    else if ( ((LA7_1>='\u0000' && LA7_1<='.')||(LA7_1>='0' && LA7_1<='\uFFFF')) ) {
                        alt7=1;
                    }


                }
                else if ( ((LA7_0>='\u0000' && LA7_0<=')')||(LA7_0>='+' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:252:9: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JAVADOC"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:255:4: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )* )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:255:6: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:256:6: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='-'||(LA8_0>='0' && LA8_0<='9')||(LA8_0>='A' && LA8_0<='Z')||LA8_0=='_'||(LA8_0>='a' && LA8_0<='z')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:
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
            	    break loop8;
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
        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:8: ( MODULE | IMPORTS | PUBLIC | PRIVATE | ABSTRACT | SYNTAX | SORT | OPERATOR | ATOM | INNER | OUTER | NEUTRAL | BINDS | ARROW | COLON | COMMA | DOT | LPAREN | RPAREN | STAR | EQUALS | ALT | SEMI | LDIPLE | RDIPLE | LBRACE | RBRACE | WS | SLCOMMENT | MLCOMMENT | JAVADOC | ID )
        int alt9=32;
        alt9 = dfa9.predict(input);
        switch (alt9) {
            case 1 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:10: MODULE
                {
                mMODULE(); 

                }
                break;
            case 2 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:17: IMPORTS
                {
                mIMPORTS(); 

                }
                break;
            case 3 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:25: PUBLIC
                {
                mPUBLIC(); 

                }
                break;
            case 4 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:32: PRIVATE
                {
                mPRIVATE(); 

                }
                break;
            case 5 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:40: ABSTRACT
                {
                mABSTRACT(); 

                }
                break;
            case 6 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:49: SYNTAX
                {
                mSYNTAX(); 

                }
                break;
            case 7 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:56: SORT
                {
                mSORT(); 

                }
                break;
            case 8 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:61: OPERATOR
                {
                mOPERATOR(); 

                }
                break;
            case 9 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:70: ATOM
                {
                mATOM(); 

                }
                break;
            case 10 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:75: INNER
                {
                mINNER(); 

                }
                break;
            case 11 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:81: OUTER
                {
                mOUTER(); 

                }
                break;
            case 12 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:87: NEUTRAL
                {
                mNEUTRAL(); 

                }
                break;
            case 13 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:95: BINDS
                {
                mBINDS(); 

                }
                break;
            case 14 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:101: ARROW
                {
                mARROW(); 

                }
                break;
            case 15 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:107: COLON
                {
                mCOLON(); 

                }
                break;
            case 16 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:113: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 17 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:119: DOT
                {
                mDOT(); 

                }
                break;
            case 18 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:123: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 19 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:130: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 20 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:137: STAR
                {
                mSTAR(); 

                }
                break;
            case 21 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:142: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 22 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:149: ALT
                {
                mALT(); 

                }
                break;
            case 23 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:153: SEMI
                {
                mSEMI(); 

                }
                break;
            case 24 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:158: LDIPLE
                {
                mLDIPLE(); 

                }
                break;
            case 25 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:165: RDIPLE
                {
                mRDIPLE(); 

                }
                break;
            case 26 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:172: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 27 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:179: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 28 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:186: WS
                {
                mWS(); 

                }
                break;
            case 29 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:189: SLCOMMENT
                {
                mSLCOMMENT(); 

                }
                break;
            case 30 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:199: MLCOMMENT
                {
                mMLCOMMENT(); 

                }
                break;
            case 31 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:209: JAVADOC
                {
                mJAVADOC(); 

                }
                break;
            case 32 :
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:1:217: ID
                {
                mID(); 

                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA9_eotS =
        "\1\uffff\10\31\21\uffff\15\31\2\uffff\15\31\2\uffff\6\31\1\113\1"+
        "\31\1\115\6\31\1\124\3\31\1\uffff\1\31\1\uffff\1\31\1\132\1\31\1"+
        "\134\1\135\1\31\1\uffff\1\137\2\31\1\142\1\31\1\uffff\1\31\2\uffff"+
        "\1\145\1\uffff\1\146\1\31\1\uffff\1\31\1\151\2\uffff\1\152\1\153"+
        "\3\uffff";
    static final String DFA9_eofS =
        "\154\uffff";
    static final String DFA9_minS =
        "\1\11\1\157\1\155\1\162\1\142\1\157\1\160\1\145\1\151\17\uffff\1"+
        "\52\1\uffff\1\144\1\160\1\156\1\142\1\151\1\163\1\157\1\156\1\162"+
        "\1\145\1\164\1\165\1\156\1\uffff\1\0\1\165\1\157\1\145\1\154\1\166"+
        "\1\164\1\155\2\164\1\162\1\145\1\164\1\144\2\uffff\1\154\2\162\1"+
        "\151\1\141\1\162\1\55\1\141\1\55\1\141\2\162\1\163\1\145\1\164\1"+
        "\55\1\143\1\164\1\141\1\uffff\1\170\1\uffff\1\164\1\55\1\141\2\55"+
        "\1\163\1\uffff\1\55\1\145\1\143\1\55\1\157\1\uffff\1\154\2\uffff"+
        "\1\55\1\uffff\1\55\1\164\1\uffff\1\162\1\55\2\uffff\2\55\3\uffff";
    static final String DFA9_maxS =
        "\1\175\1\157\1\156\1\165\1\164\1\171\1\165\1\145\1\151\17\uffff"+
        "\1\57\1\uffff\1\144\1\160\1\156\1\142\1\151\1\163\1\157\1\156\1"+
        "\162\1\145\1\164\1\165\1\156\1\uffff\1\uffff\1\165\1\157\1\145\1"+
        "\154\1\166\1\164\1\155\2\164\1\162\1\145\1\164\1\144\2\uffff\1\154"+
        "\2\162\1\151\1\141\1\162\1\172\1\141\1\172\1\141\2\162\1\163\1\145"+
        "\1\164\1\172\1\143\1\164\1\141\1\uffff\1\170\1\uffff\1\164\1\172"+
        "\1\141\2\172\1\163\1\uffff\1\172\1\145\1\143\1\172\1\157\1\uffff"+
        "\1\154\2\uffff\1\172\1\uffff\1\172\1\164\1\uffff\1\162\1\172\2\uffff"+
        "\2\172\3\uffff";
    static final String DFA9_acceptS =
        "\11\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30"+
        "\1\31\1\32\1\33\1\34\1\uffff\1\40\15\uffff\1\35\16\uffff\1\36\1"+
        "\37\23\uffff\1\11\1\uffff\1\7\6\uffff\1\12\5\uffff\1\13\1\uffff"+
        "\1\15\1\1\1\uffff\1\3\2\uffff\1\6\2\uffff\1\2\1\4\2\uffff\1\14\1"+
        "\5\1\10";
    static final String DFA9_specialS =
        "\50\uffff\1\0\103\uffff}>";
    static final String[] DFA9_transitionS = {
            "\2\27\2\uffff\1\27\22\uffff\1\27\7\uffff\1\15\1\16\1\17\1\uffff"+
            "\1\13\1\11\1\14\1\30\12\uffff\1\12\1\22\1\23\1\20\1\24\2\uffff"+
            "\32\31\6\uffff\1\4\1\10\6\31\1\2\3\31\1\1\1\7\1\6\1\3\2\31\1"+
            "\5\7\31\1\25\1\21\1\26",
            "\1\32",
            "\1\33\1\34",
            "\1\36\2\uffff\1\35",
            "\1\37\21\uffff\1\40",
            "\1\42\11\uffff\1\41",
            "\1\43\4\uffff\1\44",
            "\1\45",
            "\1\46",
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
            "\1\50\4\uffff\1\47",
            "",
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
            "",
            "\52\66\1\67\uffd5\66",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "",
            "",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\114",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\125",
            "\1\126",
            "\1\127",
            "",
            "\1\130",
            "",
            "\1\131",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\133",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\136",
            "",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\140",
            "\1\141",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\143",
            "",
            "\1\144",
            "",
            "",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\147",
            "",
            "\1\150",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "",
            "",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\31\2\uffff\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( MODULE | IMPORTS | PUBLIC | PRIVATE | ABSTRACT | SYNTAX | SORT | OPERATOR | ATOM | INNER | OUTER | NEUTRAL | BINDS | ARROW | COLON | COMMA | DOT | LPAREN | RPAREN | STAR | EQUALS | ALT | SEMI | LDIPLE | RDIPLE | LBRACE | RBRACE | WS | SLCOMMENT | MLCOMMENT | JAVADOC | ID );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA9_40 = input.LA(1);

                        s = -1;
                        if ( ((LA9_40>='\u0000' && LA9_40<=')')||(LA9_40>='+' && LA9_40<='\uFFFF')) ) {s = 54;}

                        else if ( (LA9_40=='*') ) {s = 55;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 9, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}