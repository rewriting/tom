package tom.ui.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTomDslLexer extends Lexer {
    public static final int RULE_ID=5;
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int RULE_JAVAMETHOD=4;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int RULE_ANY_OTHER=12;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int EOF=-1;
    public static final int RULE_SL_COMMENT=10;
    public static final int RULE_ML_COMMENT=9;
    public static final int RULE_BRCKTSTMT=8;
    public static final int T__19=19;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__16=16;
    public static final int T__33=33;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__14=14;
    public static final int RULE_FIRST_LEVEL_LBRACKET=6;
    public static final int T__13=13;
    public static final int RULE_FIRST_LEVEL_RBRACKET=7;
    public static final int RULE_WS=11;

    // delegates
    // delegators

    public InternalTomDslLexer() {;} 
    public InternalTomDslLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalTomDslLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:11:7: ( '/' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:11:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:12:7: ( '.' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:12:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:13:7: ( '%include' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:13:9: '%include'
            {
            match("%include"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:14:7: ( '%op' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:14:9: '%op'
            {
            match("%op"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:15:7: ( '(' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:15:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:16:7: ( ')' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:16:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:17:7: ( 'is_fsym' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:17:9: 'is_fsym'
            {
            match("is_fsym"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:18:7: ( 'make' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:18:9: 'make'
            {
            match("make"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:19:7: ( ',' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:19:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:20:7: ( 'get_slot' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:20:9: 'get_slot'
            {
            match("get_slot"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:21:7: ( '%oparray' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:21:9: '%oparray'
            {
            match("%oparray"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:22:7: ( '*' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:22:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:23:7: ( 'get_size' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:23:9: 'get_size'
            {
            match("get_size"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:24:7: ( 'get_element' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:24:9: 'get_element'
            {
            match("get_element"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:25:7: ( 'make_empty' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:25:9: 'make_empty'
            {
            match("make_empty"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:26:7: ( 'make_append' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:26:9: 'make_append'
            {
            match("make_append"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:27:7: ( ':' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:27:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:28:7: ( '%typeterm' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:28:9: '%typeterm'
            {
            match("%typeterm"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:29:7: ( 'implement' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:29:9: 'implement'
            {
            match("implement"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:30:7: ( 'is_sort' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:30:9: 'is_sort'
            {
            match("is_sort"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:31:7: ( 'equals' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:31:9: 'equals'
            {
            match("equals"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "RULE_FIRST_LEVEL_LBRACKET"
    public final void mRULE_FIRST_LEVEL_LBRACKET() throws RecognitionException {
        try {
            int _type = RULE_FIRST_LEVEL_LBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3458:27: ( '{' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3458:29: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_FIRST_LEVEL_LBRACKET"

    // $ANTLR start "RULE_FIRST_LEVEL_RBRACKET"
    public final void mRULE_FIRST_LEVEL_RBRACKET() throws RecognitionException {
        try {
            int _type = RULE_FIRST_LEVEL_RBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3460:27: ( '}' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3460:29: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_FIRST_LEVEL_RBRACKET"

    // $ANTLR start "RULE_BRCKTSTMT"
    public final void mRULE_BRCKTSTMT() throws RecognitionException {
        try {
            int _type = RULE_BRCKTSTMT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3462:16: ( '{' ( options {greedy=false; } : . )* '}' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3462:18: '{' ( options {greedy=false; } : . )* '}'
            {
            match('{'); 
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3462:22: ( options {greedy=false; } : . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='}') ) {
                    alt1=2;
                }
                else if ( ((LA1_0>='\u0000' && LA1_0<='|')||(LA1_0>='~' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3462:50: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_BRCKTSTMT"

    // $ANTLR start "RULE_JAVAMETHOD"
    public final void mRULE_JAVAMETHOD() throws RecognitionException {
        try {
            int _type = RULE_JAVAMETHOD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:17: ( 'private' ( options {greedy=false; } : . )* '(' ( options {greedy=false; } : . )* ')' ( options {greedy=false; } : . )* '{' ( options {greedy=false; } : . )* '}' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:19: 'private' ( options {greedy=false; } : . )* '(' ( options {greedy=false; } : . )* ')' ( options {greedy=false; } : . )* '{' ( options {greedy=false; } : . )* '}'
            {
            match("private"); 

            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:29: ( options {greedy=false; } : . )*
            loop2:
            do {
                int alt2=2;
                alt2 = dfa2.predict(input);
                switch (alt2) {
            	case 1 :
            	    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:57: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('('); 
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:65: ( options {greedy=false; } : . )*
            loop3:
            do {
                int alt3=2;
                alt3 = dfa3.predict(input);
                switch (alt3) {
            	case 1 :
            	    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:93: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match(')'); 
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:101: ( options {greedy=false; } : . )*
            loop4:
            do {
                int alt4=2;
                alt4 = dfa4.predict(input);
                switch (alt4) {
            	case 1 :
            	    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:129: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            match('{'); 
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:137: ( options {greedy=false; } : . )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='}') ) {
                    alt5=2;
                }
                else if ( ((LA5_0>='\u0000' && LA5_0<='|')||(LA5_0>='~' && LA5_0<='\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3464:165: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_JAVAMETHOD"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3466:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3466:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3466:11: ( '^' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='^') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3466:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3466:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='Z')||LA7_0=='_'||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:
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
    // $ANTLR end "RULE_ID"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3468:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3468:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3468:24: ( options {greedy=false; } : . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='*') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='/') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1>='\u0000' && LA8_1<='.')||(LA8_1>='0' && LA8_1<='\uFFFF')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0>='\u0000' && LA8_0<=')')||(LA8_0>='+' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3468:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
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
    // $ANTLR end "RULE_ML_COMMENT"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3470:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3470:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3470:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='\u0000' && LA9_0<='\t')||(LA9_0>='\u000B' && LA9_0<='\f')||(LA9_0>='\u000E' && LA9_0<='\uFFFF')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3470:24: ~ ( ( '\\n' | '\\r' ) )
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
            	    break loop9;
                }
            } while (true);

            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3470:40: ( ( '\\r' )? '\\n' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\n'||LA11_0=='\r') ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3470:41: ( '\\r' )? '\\n'
                    {
                    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3470:41: ( '\\r' )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0=='\r') ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3470:41: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3472:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3472:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3472:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>='\t' && LA12_0<='\n')||LA12_0=='\r'||LA12_0==' ') ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    // $ANTLR start "RULE_ANY_OTHER"
    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3474:16: ( . )
            // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:3474:18: .
            {
            matchAny(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ANY_OTHER"

    public void mTokens() throws RecognitionException {
        // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:8: ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | RULE_FIRST_LEVEL_LBRACKET | RULE_FIRST_LEVEL_RBRACKET | RULE_BRCKTSTMT | RULE_JAVAMETHOD | RULE_ID | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt13=30;
        alt13 = dfa13.predict(input);
        switch (alt13) {
            case 1 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:10: T__13
                {
                mT__13(); 

                }
                break;
            case 2 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:16: T__14
                {
                mT__14(); 

                }
                break;
            case 3 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:22: T__15
                {
                mT__15(); 

                }
                break;
            case 4 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:28: T__16
                {
                mT__16(); 

                }
                break;
            case 5 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:34: T__17
                {
                mT__17(); 

                }
                break;
            case 6 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:40: T__18
                {
                mT__18(); 

                }
                break;
            case 7 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:46: T__19
                {
                mT__19(); 

                }
                break;
            case 8 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:52: T__20
                {
                mT__20(); 

                }
                break;
            case 9 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:58: T__21
                {
                mT__21(); 

                }
                break;
            case 10 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:64: T__22
                {
                mT__22(); 

                }
                break;
            case 11 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:70: T__23
                {
                mT__23(); 

                }
                break;
            case 12 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:76: T__24
                {
                mT__24(); 

                }
                break;
            case 13 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:82: T__25
                {
                mT__25(); 

                }
                break;
            case 14 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:88: T__26
                {
                mT__26(); 

                }
                break;
            case 15 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:94: T__27
                {
                mT__27(); 

                }
                break;
            case 16 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:100: T__28
                {
                mT__28(); 

                }
                break;
            case 17 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:106: T__29
                {
                mT__29(); 

                }
                break;
            case 18 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:112: T__30
                {
                mT__30(); 

                }
                break;
            case 19 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:118: T__31
                {
                mT__31(); 

                }
                break;
            case 20 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:124: T__32
                {
                mT__32(); 

                }
                break;
            case 21 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:130: T__33
                {
                mT__33(); 

                }
                break;
            case 22 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:136: RULE_FIRST_LEVEL_LBRACKET
                {
                mRULE_FIRST_LEVEL_LBRACKET(); 

                }
                break;
            case 23 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:162: RULE_FIRST_LEVEL_RBRACKET
                {
                mRULE_FIRST_LEVEL_RBRACKET(); 

                }
                break;
            case 24 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:188: RULE_BRCKTSTMT
                {
                mRULE_BRCKTSTMT(); 

                }
                break;
            case 25 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:203: RULE_JAVAMETHOD
                {
                mRULE_JAVAMETHOD(); 

                }
                break;
            case 26 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:219: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 27 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:227: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 28 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:243: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 29 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:259: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 30 :
                // ../tom.dsl.ui/src-gen/tom/ui/contentassist/antlr/internal/InternalTomDsl.g:1:267: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    protected DFA3 dfa3 = new DFA3(this);
    protected DFA4 dfa4 = new DFA4(this);
    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA2_eotS =
        "\20\uffff";
    static final String DFA2_eofS =
        "\20\uffff";
    static final String DFA2_minS =
        "\2\0\1\uffff\1\0\1\uffff\3\0\1\uffff\2\0\1\uffff\1\0\1\uffff\1\0"+
        "\1\uffff";
    static final String DFA2_maxS =
        "\2\uffff\1\uffff\1\uffff\1\uffff\3\uffff\1\uffff\2\uffff\1\uffff"+
        "\1\uffff\1\uffff\1\uffff\1\uffff";
    static final String DFA2_acceptS =
        "\2\uffff\1\1\1\uffff\1\2\3\uffff\1\2\2\uffff\1\2\1\uffff\1\2\1\uffff"+
        "\1\2";
    static final String DFA2_specialS =
        "\1\4\1\2\1\uffff\1\0\1\uffff\1\10\1\5\1\3\1\uffff\1\7\1\11\1\uffff"+
        "\1\6\1\uffff\1\1\1\uffff}>";
    static final String[] DFA2_transitionS = {
            "\50\2\1\1\uffd7\2",
            "\50\5\1\4\1\3\uffd6\5",
            "",
            "\50\11\1\10\1\6\121\11\1\7\uff84\11",
            "",
            "\50\5\1\4\1\3\uffd6\5",
            "\50\11\1\10\1\6\121\11\1\7\uff84\11",
            "\50\16\1\13\1\12\121\16\1\14\1\16\1\15\uff82\16",
            "",
            "\50\11\1\10\1\6\121\11\1\7\uff84\11",
            "\50\16\1\13\1\12\121\16\1\14\1\16\1\15\uff82\16",
            "",
            "\50\16\1\13\1\12\121\16\1\14\1\16\1\15\uff82\16",
            "",
            "\50\16\1\13\1\12\121\16\1\14\1\16\1\17\uff82\16",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "()* loopback of 3464:29: ( options {greedy=false; } : . )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA2_3 = input.LA(1);

                        s = -1;
                        if ( (LA2_3==')') ) {s = 6;}

                        else if ( (LA2_3=='{') ) {s = 7;}

                        else if ( (LA2_3=='(') ) {s = 8;}

                        else if ( ((LA2_3>='\u0000' && LA2_3<='\'')||(LA2_3>='*' && LA2_3<='z')||(LA2_3>='|' && LA2_3<='\uFFFF')) ) {s = 9;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA2_14 = input.LA(1);

                        s = -1;
                        if ( (LA2_14==')') ) {s = 10;}

                        else if ( (LA2_14=='}') ) {s = 15;}

                        else if ( (LA2_14=='(') ) {s = 11;}

                        else if ( (LA2_14=='{') ) {s = 12;}

                        else if ( ((LA2_14>='\u0000' && LA2_14<='\'')||(LA2_14>='*' && LA2_14<='z')||LA2_14=='|'||(LA2_14>='~' && LA2_14<='\uFFFF')) ) {s = 14;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA2_1 = input.LA(1);

                        s = -1;
                        if ( (LA2_1==')') ) {s = 3;}

                        else if ( (LA2_1=='(') ) {s = 4;}

                        else if ( ((LA2_1>='\u0000' && LA2_1<='\'')||(LA2_1>='*' && LA2_1<='\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA2_7 = input.LA(1);

                        s = -1;
                        if ( (LA2_7==')') ) {s = 10;}

                        else if ( (LA2_7=='(') ) {s = 11;}

                        else if ( (LA2_7=='{') ) {s = 12;}

                        else if ( (LA2_7=='}') ) {s = 13;}

                        else if ( ((LA2_7>='\u0000' && LA2_7<='\'')||(LA2_7>='*' && LA2_7<='z')||LA2_7=='|'||(LA2_7>='~' && LA2_7<='\uFFFF')) ) {s = 14;}

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA2_0 = input.LA(1);

                        s = -1;
                        if ( (LA2_0=='(') ) {s = 1;}

                        else if ( ((LA2_0>='\u0000' && LA2_0<='\'')||(LA2_0>=')' && LA2_0<='\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA2_6 = input.LA(1);

                        s = -1;
                        if ( (LA2_6==')') ) {s = 6;}

                        else if ( (LA2_6=='{') ) {s = 7;}

                        else if ( (LA2_6=='(') ) {s = 8;}

                        else if ( ((LA2_6>='\u0000' && LA2_6<='\'')||(LA2_6>='*' && LA2_6<='z')||(LA2_6>='|' && LA2_6<='\uFFFF')) ) {s = 9;}

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA2_12 = input.LA(1);

                        s = -1;
                        if ( (LA2_12==')') ) {s = 10;}

                        else if ( (LA2_12=='}') ) {s = 13;}

                        else if ( (LA2_12=='(') ) {s = 11;}

                        else if ( (LA2_12=='{') ) {s = 12;}

                        else if ( ((LA2_12>='\u0000' && LA2_12<='\'')||(LA2_12>='*' && LA2_12<='z')||LA2_12=='|'||(LA2_12>='~' && LA2_12<='\uFFFF')) ) {s = 14;}

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA2_9 = input.LA(1);

                        s = -1;
                        if ( (LA2_9==')') ) {s = 6;}

                        else if ( (LA2_9=='(') ) {s = 8;}

                        else if ( (LA2_9=='{') ) {s = 7;}

                        else if ( ((LA2_9>='\u0000' && LA2_9<='\'')||(LA2_9>='*' && LA2_9<='z')||(LA2_9>='|' && LA2_9<='\uFFFF')) ) {s = 9;}

                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA2_5 = input.LA(1);

                        s = -1;
                        if ( (LA2_5==')') ) {s = 3;}

                        else if ( (LA2_5=='(') ) {s = 4;}

                        else if ( ((LA2_5>='\u0000' && LA2_5<='\'')||(LA2_5>='*' && LA2_5<='\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA2_10 = input.LA(1);

                        s = -1;
                        if ( (LA2_10==')') ) {s = 10;}

                        else if ( (LA2_10=='}') ) {s = 13;}

                        else if ( (LA2_10=='{') ) {s = 12;}

                        else if ( (LA2_10=='(') ) {s = 11;}

                        else if ( ((LA2_10>='\u0000' && LA2_10<='\'')||(LA2_10>='*' && LA2_10<='z')||LA2_10=='|'||(LA2_10>='~' && LA2_10<='\uFFFF')) ) {s = 14;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 2, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA3_eotS =
        "\13\uffff";
    static final String DFA3_eofS =
        "\13\uffff";
    static final String DFA3_minS =
        "\2\0\2\uffff\2\0\1\uffff\1\0\1\uffff\1\0\1\uffff";
    static final String DFA3_maxS =
        "\2\uffff\2\uffff\2\uffff\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff";
    static final String DFA3_acceptS =
        "\2\uffff\1\1\1\2\2\uffff\1\2\1\uffff\1\2\1\uffff\1\2";
    static final String DFA3_specialS =
        "\1\3\1\0\2\uffff\1\5\1\2\1\uffff\1\4\1\uffff\1\1\1\uffff}>";
    static final String[] DFA3_transitionS = {
            "\51\2\1\1\uffd6\2",
            "\51\5\1\3\121\5\1\4\uff84\5",
            "",
            "",
            "\51\11\1\6\121\11\1\7\1\11\1\10\uff82\11",
            "\51\5\1\3\121\5\1\4\uff84\5",
            "",
            "\51\11\1\6\121\11\1\7\1\11\1\10\uff82\11",
            "",
            "\51\11\1\6\121\11\1\7\1\11\1\12\uff82\11",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "()* loopback of 3464:65: ( options {greedy=false; } : . )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA3_1 = input.LA(1);

                        s = -1;
                        if ( (LA3_1==')') ) {s = 3;}

                        else if ( (LA3_1=='{') ) {s = 4;}

                        else if ( ((LA3_1>='\u0000' && LA3_1<='(')||(LA3_1>='*' && LA3_1<='z')||(LA3_1>='|' && LA3_1<='\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA3_9 = input.LA(1);

                        s = -1;
                        if ( (LA3_9==')') ) {s = 6;}

                        else if ( (LA3_9=='}') ) {s = 10;}

                        else if ( (LA3_9=='{') ) {s = 7;}

                        else if ( ((LA3_9>='\u0000' && LA3_9<='(')||(LA3_9>='*' && LA3_9<='z')||LA3_9=='|'||(LA3_9>='~' && LA3_9<='\uFFFF')) ) {s = 9;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA3_5 = input.LA(1);

                        s = -1;
                        if ( (LA3_5==')') ) {s = 3;}

                        else if ( (LA3_5=='{') ) {s = 4;}

                        else if ( ((LA3_5>='\u0000' && LA3_5<='(')||(LA3_5>='*' && LA3_5<='z')||(LA3_5>='|' && LA3_5<='\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA3_0 = input.LA(1);

                        s = -1;
                        if ( (LA3_0==')') ) {s = 1;}

                        else if ( ((LA3_0>='\u0000' && LA3_0<='(')||(LA3_0>='*' && LA3_0<='\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA3_7 = input.LA(1);

                        s = -1;
                        if ( (LA3_7==')') ) {s = 6;}

                        else if ( (LA3_7=='}') ) {s = 8;}

                        else if ( (LA3_7=='{') ) {s = 7;}

                        else if ( ((LA3_7>='\u0000' && LA3_7<='(')||(LA3_7>='*' && LA3_7<='z')||LA3_7=='|'||(LA3_7>='~' && LA3_7<='\uFFFF')) ) {s = 9;}

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA3_4 = input.LA(1);

                        s = -1;
                        if ( (LA3_4==')') ) {s = 6;}

                        else if ( (LA3_4=='{') ) {s = 7;}

                        else if ( (LA3_4=='}') ) {s = 8;}

                        else if ( ((LA3_4>='\u0000' && LA3_4<='(')||(LA3_4>='*' && LA3_4<='z')||LA3_4=='|'||(LA3_4>='~' && LA3_4<='\uFFFF')) ) {s = 9;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 3, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA4_eotS =
        "\7\uffff";
    static final String DFA4_eofS =
        "\7\uffff";
    static final String DFA4_minS =
        "\2\0\3\uffff\1\0\1\uffff";
    static final String DFA4_maxS =
        "\2\uffff\3\uffff\1\uffff\1\uffff";
    static final String DFA4_acceptS =
        "\2\uffff\1\1\2\2\1\uffff\1\2";
    static final String DFA4_specialS =
        "\1\1\1\2\3\uffff\1\0\1\uffff}>";
    static final String[] DFA4_transitionS = {
            "\173\2\1\1\uff84\2",
            "\173\5\1\3\1\5\1\4\uff82\5",
            "",
            "",
            "",
            "\173\5\1\3\1\5\1\6\uff82\5",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "()* loopback of 3464:101: ( options {greedy=false; } : . )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA4_5 = input.LA(1);

                        s = -1;
                        if ( (LA4_5=='}') ) {s = 6;}

                        else if ( (LA4_5=='{') ) {s = 3;}

                        else if ( ((LA4_5>='\u0000' && LA4_5<='z')||LA4_5=='|'||(LA4_5>='~' && LA4_5<='\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA4_0 = input.LA(1);

                        s = -1;
                        if ( (LA4_0=='{') ) {s = 1;}

                        else if ( ((LA4_0>='\u0000' && LA4_0<='z')||(LA4_0>='|' && LA4_0<='\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA4_1 = input.LA(1);

                        s = -1;
                        if ( (LA4_1=='{') ) {s = 3;}

                        else if ( (LA4_1=='}') ) {s = 4;}

                        else if ( ((LA4_1>='\u0000' && LA4_1<='z')||LA4_1=='|'||(LA4_1>='~' && LA4_1<='\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 4, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA13_eotS =
        "\1\uffff\1\26\1\uffff\1\23\2\uffff\2\37\1\uffff\1\37\2\uffff\1\37"+
        "\1\46\1\uffff\1\37\1\23\14\uffff\2\37\1\uffff\1\37\1\uffff\1\37"+
        "\2\uffff\1\37\3\uffff\1\37\1\uffff\1\63\6\37\2\uffff\3\37\1\77\7"+
        "\37\1\uffff\14\37\1\126\1\37\1\130\1\131\6\37\1\uffff\1\37\2\uffff"+
        "\3\37\1\145\1\146\2\37\1\uffff\1\150\2\37\2\uffff\1\37\1\uffff\1"+
        "\154\2\37\1\uffff\1\157\1\160\2\uffff";
    static final String DFA13_eofS =
        "\161\uffff";
    static final String DFA13_minS =
        "\1\0\1\52\1\uffff\1\151\2\uffff\1\155\1\141\1\uffff\1\145\2\uffff"+
        "\1\161\1\0\1\uffff\1\162\1\101\10\uffff\1\160\3\uffff\1\137\1\160"+
        "\1\uffff\1\153\1\uffff\1\164\2\uffff\1\165\3\uffff\1\151\1\uffff"+
        "\1\141\1\146\1\154\1\145\1\137\1\141\1\166\2\uffff\1\163\1\157\1"+
        "\145\1\60\1\145\1\154\1\141\1\171\1\162\1\155\1\141\1\uffff\1\151"+
        "\1\154\1\163\1\164\1\155\1\164\1\145\1\155\1\160\1\157\1\172\1\145"+
        "\1\60\1\145\2\60\1\156\2\160\1\164\1\145\1\155\1\uffff\1\0\2\uffff"+
        "\2\164\1\145\2\60\1\145\1\0\1\uffff\1\60\1\171\1\156\2\uffff\1\156"+
        "\1\uffff\1\60\1\144\1\164\1\uffff\2\60\2\uffff";
    static final String DFA13_maxS =
        "\1\uffff\1\57\1\uffff\1\164\2\uffff\1\163\1\141\1\uffff\1\145\2"+
        "\uffff\1\161\1\uffff\1\uffff\1\162\1\172\10\uffff\1\160\3\uffff"+
        "\1\137\1\160\1\uffff\1\153\1\uffff\1\164\2\uffff\1\165\3\uffff\1"+
        "\151\1\uffff\1\141\1\163\1\154\1\145\1\137\1\141\1\166\2\uffff\1"+
        "\163\1\157\1\145\1\172\1\163\1\154\1\141\1\171\1\162\1\155\1\145"+
        "\1\uffff\2\154\1\163\1\164\1\155\1\164\1\145\1\155\1\160\1\157\1"+
        "\172\1\145\1\172\1\145\2\172\1\156\2\160\1\164\1\145\1\155\1\uffff"+
        "\1\uffff\2\uffff\2\164\1\145\2\172\1\145\1\uffff\1\uffff\1\172\1"+
        "\171\1\156\2\uffff\1\156\1\uffff\1\172\1\144\1\164\1\uffff\2\172"+
        "\2\uffff";
    static final String DFA13_acceptS =
        "\2\uffff\1\2\1\uffff\1\5\1\6\2\uffff\1\11\1\uffff\1\14\1\21\2\uffff"+
        "\1\27\2\uffff\1\32\1\35\1\36\1\33\1\34\1\1\1\2\1\3\1\uffff\1\22"+
        "\1\5\1\6\2\uffff\1\32\1\uffff\1\11\1\uffff\1\14\1\21\1\uffff\1\26"+
        "\1\30\1\27\1\uffff\1\35\7\uffff\1\13\1\4\13\uffff\1\10\26\uffff"+
        "\1\25\1\uffff\1\7\1\24\7\uffff\1\31\3\uffff\1\12\1\15\1\uffff\1"+
        "\23\3\uffff\1\17\2\uffff\1\20\1\16";
    static final String DFA13_specialS =
        "\1\1\14\uffff\1\0\111\uffff\1\3\10\uffff\1\2\20\uffff}>";
    static final String[] DFA13_transitionS = {
            "\11\23\2\22\2\23\1\22\22\23\1\22\4\23\1\3\2\23\1\4\1\5\1\12"+
            "\1\23\1\10\1\23\1\2\1\1\12\23\1\13\6\23\32\21\3\23\1\20\1\21"+
            "\1\23\4\21\1\14\1\21\1\11\1\21\1\6\3\21\1\7\2\21\1\17\12\21"+
            "\1\15\1\23\1\16\uff82\23",
            "\1\24\4\uffff\1\25",
            "",
            "\1\30\5\uffff\1\31\4\uffff\1\32",
            "",
            "",
            "\1\36\5\uffff\1\35",
            "\1\40",
            "",
            "\1\42",
            "",
            "",
            "\1\45",
            "\0\47",
            "",
            "\1\51",
            "\32\37\4\uffff\1\37\1\uffff\32\37",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\53",
            "",
            "",
            "",
            "\1\54",
            "\1\55",
            "",
            "\1\56",
            "",
            "\1\57",
            "",
            "",
            "\1\60",
            "",
            "",
            "",
            "\1\61",
            "",
            "\1\62",
            "\1\64\14\uffff\1\65",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "",
            "",
            "\1\73",
            "\1\74",
            "\1\75",
            "\12\37\7\uffff\32\37\4\uffff\1\76\1\uffff\32\37",
            "\1\101\15\uffff\1\100",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\110\3\uffff\1\107",
            "",
            "\1\112\2\uffff\1\111",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "\12\37\7\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\127",
            "\12\37\7\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\12\37\7\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "",
            "\60\141\12\140\7\141\32\140\4\141\1\140\1\141\32\140\uff85"+
            "\141",
            "",
            "",
            "\1\142",
            "\1\143",
            "\1\144",
            "\12\37\7\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\12\37\7\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\147",
            "\60\141\12\140\7\141\32\140\4\141\1\140\1\141\32\140\uff85"+
            "\141",
            "",
            "\12\37\7\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\151",
            "\1\152",
            "",
            "",
            "\1\153",
            "",
            "\12\37\7\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\1\155",
            "\1\156",
            "",
            "\12\37\7\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "\12\37\7\uffff\32\37\4\uffff\1\37\1\uffff\32\37",
            "",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | RULE_FIRST_LEVEL_LBRACKET | RULE_FIRST_LEVEL_RBRACKET | RULE_BRCKTSTMT | RULE_JAVAMETHOD | RULE_ID | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA13_13 = input.LA(1);

                        s = -1;
                        if ( ((LA13_13>='\u0000' && LA13_13<='\uFFFF')) ) {s = 39;}

                        else s = 38;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA13_0 = input.LA(1);

                        s = -1;
                        if ( (LA13_0=='/') ) {s = 1;}

                        else if ( (LA13_0=='.') ) {s = 2;}

                        else if ( (LA13_0=='%') ) {s = 3;}

                        else if ( (LA13_0=='(') ) {s = 4;}

                        else if ( (LA13_0==')') ) {s = 5;}

                        else if ( (LA13_0=='i') ) {s = 6;}

                        else if ( (LA13_0=='m') ) {s = 7;}

                        else if ( (LA13_0==',') ) {s = 8;}

                        else if ( (LA13_0=='g') ) {s = 9;}

                        else if ( (LA13_0=='*') ) {s = 10;}

                        else if ( (LA13_0==':') ) {s = 11;}

                        else if ( (LA13_0=='e') ) {s = 12;}

                        else if ( (LA13_0=='{') ) {s = 13;}

                        else if ( (LA13_0=='}') ) {s = 14;}

                        else if ( (LA13_0=='p') ) {s = 15;}

                        else if ( (LA13_0=='^') ) {s = 16;}

                        else if ( ((LA13_0>='A' && LA13_0<='Z')||LA13_0=='_'||(LA13_0>='a' && LA13_0<='d')||LA13_0=='f'||LA13_0=='h'||(LA13_0>='j' && LA13_0<='l')||(LA13_0>='n' && LA13_0<='o')||(LA13_0>='q' && LA13_0<='z')) ) {s = 17;}

                        else if ( ((LA13_0>='\t' && LA13_0<='\n')||LA13_0=='\r'||LA13_0==' ') ) {s = 18;}

                        else if ( ((LA13_0>='\u0000' && LA13_0<='\b')||(LA13_0>='\u000B' && LA13_0<='\f')||(LA13_0>='\u000E' && LA13_0<='\u001F')||(LA13_0>='!' && LA13_0<='$')||(LA13_0>='&' && LA13_0<='\'')||LA13_0=='+'||LA13_0=='-'||(LA13_0>='0' && LA13_0<='9')||(LA13_0>=';' && LA13_0<='@')||(LA13_0>='[' && LA13_0<=']')||LA13_0=='`'||LA13_0=='|'||(LA13_0>='~' && LA13_0<='\uFFFF')) ) {s = 19;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA13_96 = input.LA(1);

                        s = -1;
                        if ( ((LA13_96>='0' && LA13_96<='9')||(LA13_96>='A' && LA13_96<='Z')||LA13_96=='_'||(LA13_96>='a' && LA13_96<='z')) ) {s = 96;}

                        else if ( ((LA13_96>='\u0000' && LA13_96<='/')||(LA13_96>=':' && LA13_96<='@')||(LA13_96>='[' && LA13_96<='^')||LA13_96=='`'||(LA13_96>='{' && LA13_96<='\uFFFF')) ) {s = 97;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA13_87 = input.LA(1);

                        s = -1;
                        if ( ((LA13_87>='0' && LA13_87<='9')||(LA13_87>='A' && LA13_87<='Z')||LA13_87=='_'||(LA13_87>='a' && LA13_87<='z')) ) {s = 96;}

                        else if ( ((LA13_87>='\u0000' && LA13_87<='/')||(LA13_87>=':' && LA13_87<='@')||(LA13_87>='[' && LA13_87<='^')||LA13_87=='`'||(LA13_87>='{' && LA13_87<='\uFFFF')) ) {s = 97;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 13, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}