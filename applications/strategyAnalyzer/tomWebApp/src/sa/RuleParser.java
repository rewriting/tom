// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g 2016-02-25 12:49:45

  package sa;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Program", "SortType", "ConcProduction", "Alternative", "Symbol", "ConcAlternative", "ConcField", "ConditionalRule", "Rule", "EmptyTrs", "Trs", "Otrs", "ConcStrat", "TermList", "CondFalse", "CondTrue", "CondNot", "CondAnd", "CondEquals", "Empty", "TrueMatch", "Match", "Inter", "Sub", "Add", "At", "Anti", "BuiltinInt", "Var", "Appl", "ConcRule", "ConcGomType", "ConcAdd", "Param", "PushEnvironment", "EmptyEnvironment", "ConcStratDecl", "StratAppl", "StratMu", "StratTrs", "StratOne", "StratAll", "StratFail", "StratIdentity", "StratChoice", "StratSequence", "StratRule", "StratName", "GomType", "ConcParam", "UnamedField", "StratDecl", "ABSTRACT", "SYNTAX", "FUNCTIONS", "STRATEGIES", "TRS", "LBRACKET", "COMMA", "RBRACKET", "ID", "EQUALS", "LPAR", "RPAR", "ALT", "SEMICOLON", "CHOICE", "LBRACE", "RBRACE", "IDENTITY", "FAIL", "ALL", "ONE", "MU", "DOT", "ARROW", "IF", "DOUBLEEQUALS", "INT", "COLON", "AMPERCENT", "LET", "IN", "SIGNATURE", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "ESC", "STRING", "WS", "SLCOMMENT", "MLCOMMENT", "'!'"
    };
    public static final int LT=96;
    public static final int EmptyTrs=19;
    public static final int FUNCTIONS=64;
    public static final int ConcRule=40;
    public static final int Empty=29;
    public static final int SLCOMMENT=102;
    public static final int EQUALS=71;
    public static final int ONE=82;
    public static final int EOF=-1;
    public static final int CondEquals=28;
    public static final int ConcParam=59;
    public static final int LBRACKET=67;
    public static final int MU=83;
    public static final int ConcStratDecl=46;
    public static final int LPAR=72;
    public static final int StratRule=56;
    public static final int StratIdentity=53;
    public static final int GEQ=97;
    public static final int StratMu=48;
    public static final int MLCOMMENT=103;
    public static final int GomType=58;
    public static final int RBRACE=78;
    public static final int StratAll=51;
    public static final int ConcStrat=22;
    public static final int StratOne=50;
    public static final int DOUBLEEQUALS=87;
    public static final int TRS=66;
    public static final int SEMICOLON=75;
    public static final int INT=88;
    public static final int SIGNATURE=93;
    public static final int FAIL=80;
    public static final int Inter=32;
    public static final int WS=101;
    public static final int TrueMatch=30;
    public static final int ConditionalRule=17;
    public static final int GT=98;
    public static final int CondNot=26;
    public static final int AMPERCENT=90;
    public static final int ConcField=16;
    public static final int Alternative=13;
    public static final int Anti=36;
    public static final int Var=38;
    public static final int EmptyEnvironment=45;
    public static final int STRATEGIES=65;
    public static final int IDENTITY=79;
    public static final int Trs=20;
    public static final int ESC=99;
    public static final int LBRACE=77;
    public static final int StratName=57;
    public static final int ABSTRACT=62;
    public static final int ID=70;
    public static final int Otrs=21;
    public static final int Add=34;
    public static final int CondTrue=25;
    public static final int Program=10;
    public static final int IF=86;
    public static final int SYNTAX=63;
    public static final int StratFail=52;
    public static final int Param=43;
    public static final int ALT=74;
    public static final int IN=92;
    public static final int COMMA=68;
    public static final int T__104=104;
    public static final int ALL=81;
    public static final int StratTrs=49;
    public static final int TermList=23;
    public static final int ConcGomType=41;
    public static final int Symbol=14;
    public static final int RBRACKET=69;
    public static final int DOT=84;
    public static final int CHOICE=76;
    public static final int BuiltinInt=37;
    public static final int UnamedField=60;
    public static final int PushEnvironment=44;
    public static final int At=35;
    public static final int ConcAlternative=15;
    public static final int ConcProduction=12;
    public static final int NOTEQUALS=94;
    public static final int Sub=33;
    public static final int StratDecl=61;
    public static final int CondFalse=24;
    public static final int COLON=89;
    public static final int StratSequence=55;
    public static final int Match=31;
    public static final int CondAnd=27;
    public static final int Rule=18;
    public static final int StratChoice=54;
    public static final int Appl=39;
    public static final int SortType=11;
    public static final int ARROW=85;
    public static final int RPAR=73;
    public static final int StratAppl=47;
    public static final int ConcAdd=42;
    public static final int LET=91;
    public static final int STRING=100;
    public static final int LEQ=95;

    // delegates
    // delegators


        public RuleParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public RuleParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return RuleParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g"; }


    public static class program_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:17:1: program : abstractsyntax (f= functions )? (s= strategies )? (t= trs )? EOF -> {f!=null && s!=null && t!= null }? ^( Program abstractsyntax $f $s $t) -> {f!=null && s!=null }? ^( Program abstractsyntax $f $s ^( EmptyTrs ) ) -> {f!=null && t!=null }? ^( Program abstractsyntax $f ^( ConcStratDecl ) $t) -> {t!=null && s!=null }? ^( Program abstractsyntax ^( ConcProduction ) $t $s) -> {f!=null}? ^( Program abstractsyntax $f ^( ConcStratDecl ) ^( EmptyTrs ) ) -> {s!=null}? ^( Program abstractsyntax ^( ConcProduction ) $s ^( EmptyTrs ) ) -> {t!=null}? ^( Program abstractsyntax ^( ConcProduction ) ^( ConcStratDecl ) $t) -> ^( Program abstractsyntax ^( ConcProduction ) ^( ConcStratDecl ) ^( EmptyTrs ) ) ;
    public final RuleParser.program_return program() throws RecognitionException {
        RuleParser.program_return retval = new RuleParser.program_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token EOF2=null;
        RuleParser.functions_return f = null;

        RuleParser.strategies_return s = null;

        RuleParser.trs_return t = null;

        RuleParser.abstractsyntax_return abstractsyntax1 = null;


        Tree EOF2_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_strategies=new RewriteRuleSubtreeStream(adaptor,"rule strategies");
        RewriteRuleSubtreeStream stream_trs=new RewriteRuleSubtreeStream(adaptor,"rule trs");
        RewriteRuleSubtreeStream stream_functions=new RewriteRuleSubtreeStream(adaptor,"rule functions");
        RewriteRuleSubtreeStream stream_abstractsyntax=new RewriteRuleSubtreeStream(adaptor,"rule abstractsyntax");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:17:9: ( abstractsyntax (f= functions )? (s= strategies )? (t= trs )? EOF -> {f!=null && s!=null && t!= null }? ^( Program abstractsyntax $f $s $t) -> {f!=null && s!=null }? ^( Program abstractsyntax $f $s ^( EmptyTrs ) ) -> {f!=null && t!=null }? ^( Program abstractsyntax $f ^( ConcStratDecl ) $t) -> {t!=null && s!=null }? ^( Program abstractsyntax ^( ConcProduction ) $t $s) -> {f!=null}? ^( Program abstractsyntax $f ^( ConcStratDecl ) ^( EmptyTrs ) ) -> {s!=null}? ^( Program abstractsyntax ^( ConcProduction ) $s ^( EmptyTrs ) ) -> {t!=null}? ^( Program abstractsyntax ^( ConcProduction ) ^( ConcStratDecl ) $t) -> ^( Program abstractsyntax ^( ConcProduction ) ^( ConcStratDecl ) ^( EmptyTrs ) ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:18:3: abstractsyntax (f= functions )? (s= strategies )? (t= trs )? EOF
            {
            pushFollow(FOLLOW_abstractsyntax_in_program54);
            abstractsyntax1=abstractsyntax();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_abstractsyntax.add(abstractsyntax1.getTree());
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:18:18: (f= functions )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==FUNCTIONS) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:18:19: f= functions
                    {
                    pushFollow(FOLLOW_functions_in_program59);
                    f=functions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_functions.add(f.getTree());

                    }
                    break;

            }

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:18:33: (s= strategies )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==STRATEGIES) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:18:34: s= strategies
                    {
                    pushFollow(FOLLOW_strategies_in_program66);
                    s=strategies();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_strategies.add(s.getTree());

                    }
                    break;

            }

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:18:49: (t= trs )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==TRS) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:18:50: t= trs
                    {
                    pushFollow(FOLLOW_trs_in_program73);
                    t=trs();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_trs.add(t.getTree());

                    }
                    break;

            }

            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_program77); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EOF.add(EOF2);



            // AST REWRITE
            // elements: abstractsyntax, abstractsyntax, t, t, s, t, abstractsyntax, abstractsyntax, f, f, abstractsyntax, s, abstractsyntax, f, s, abstractsyntax, f, t, s, abstractsyntax
            // token labels: 
            // rule labels: f, retval, t, s
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_f=new RewriteRuleSubtreeStream(adaptor,"rule f",f!=null?f.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_t=new RewriteRuleSubtreeStream(adaptor,"rule t",t!=null?t.tree:null);
            RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 19:3: -> {f!=null && s!=null && t!= null }? ^( Program abstractsyntax $f $s $t)
            if (f!=null && s!=null && t!= null ) {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:19:41: ^( Program abstractsyntax $f $s $t)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Program, "Program"), root_1);

                adaptor.addChild(root_1, stream_abstractsyntax.nextTree());
                adaptor.addChild(root_1, stream_f.nextTree());
                adaptor.addChild(root_1, stream_s.nextTree());
                adaptor.addChild(root_1, stream_t.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 20:3: -> {f!=null && s!=null }? ^( Program abstractsyntax $f $s ^( EmptyTrs ) )
            if (f!=null && s!=null ) {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:20:29: ^( Program abstractsyntax $f $s ^( EmptyTrs ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Program, "Program"), root_1);

                adaptor.addChild(root_1, stream_abstractsyntax.nextTree());
                adaptor.addChild(root_1, stream_f.nextTree());
                adaptor.addChild(root_1, stream_s.nextTree());
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:20:60: ^( EmptyTrs )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(EmptyTrs, "EmptyTrs"), root_2);

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 21:3: -> {f!=null && t!=null }? ^( Program abstractsyntax $f ^( ConcStratDecl ) $t)
            if (f!=null && t!=null ) {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:21:29: ^( Program abstractsyntax $f ^( ConcStratDecl ) $t)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Program, "Program"), root_1);

                adaptor.addChild(root_1, stream_abstractsyntax.nextTree());
                adaptor.addChild(root_1, stream_f.nextTree());
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:21:57: ^( ConcStratDecl )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcStratDecl, "ConcStratDecl"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_t.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 22:3: -> {t!=null && s!=null }? ^( Program abstractsyntax ^( ConcProduction ) $t $s)
            if (t!=null && s!=null ) {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:22:29: ^( Program abstractsyntax ^( ConcProduction ) $t $s)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Program, "Program"), root_1);

                adaptor.addChild(root_1, stream_abstractsyntax.nextTree());
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:22:54: ^( ConcProduction )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_t.nextTree());
                adaptor.addChild(root_1, stream_s.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 23:3: -> {f!=null}? ^( Program abstractsyntax $f ^( ConcStratDecl ) ^( EmptyTrs ) )
            if (f!=null) {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:23:17: ^( Program abstractsyntax $f ^( ConcStratDecl ) ^( EmptyTrs ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Program, "Program"), root_1);

                adaptor.addChild(root_1, stream_abstractsyntax.nextTree());
                adaptor.addChild(root_1, stream_f.nextTree());
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:23:45: ^( ConcStratDecl )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcStratDecl, "ConcStratDecl"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:23:62: ^( EmptyTrs )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(EmptyTrs, "EmptyTrs"), root_2);

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 24:3: -> {s!=null}? ^( Program abstractsyntax ^( ConcProduction ) $s ^( EmptyTrs ) )
            if (s!=null) {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:24:17: ^( Program abstractsyntax ^( ConcProduction ) $s ^( EmptyTrs ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Program, "Program"), root_1);

                adaptor.addChild(root_1, stream_abstractsyntax.nextTree());
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:24:42: ^( ConcProduction )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_s.nextTree());
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:24:63: ^( EmptyTrs )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(EmptyTrs, "EmptyTrs"), root_2);

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 25:3: -> {t!=null}? ^( Program abstractsyntax ^( ConcProduction ) ^( ConcStratDecl ) $t)
            if (t!=null) {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:25:17: ^( Program abstractsyntax ^( ConcProduction ) ^( ConcStratDecl ) $t)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Program, "Program"), root_1);

                adaptor.addChild(root_1, stream_abstractsyntax.nextTree());
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:25:42: ^( ConcProduction )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:25:60: ^( ConcStratDecl )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcStratDecl, "ConcStratDecl"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_t.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 26:3: -> ^( Program abstractsyntax ^( ConcProduction ) ^( ConcStratDecl ) ^( EmptyTrs ) )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:26:6: ^( Program abstractsyntax ^( ConcProduction ) ^( ConcStratDecl ) ^( EmptyTrs ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Program, "Program"), root_1);

                adaptor.addChild(root_1, stream_abstractsyntax.nextTree());
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:26:31: ^( ConcProduction )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:26:49: ^( ConcStratDecl )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcStratDecl, "ConcStratDecl"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:26:66: ^( EmptyTrs )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(EmptyTrs, "EmptyTrs"), root_2);

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "program"

    public static class abstractsyntax_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "abstractsyntax"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:29:1: abstractsyntax : ( ABSTRACT SYNTAX ) ( typedecl )* -> ^( ConcProduction ( typedecl )* ) ;
    public final RuleParser.abstractsyntax_return abstractsyntax() throws RecognitionException {
        RuleParser.abstractsyntax_return retval = new RuleParser.abstractsyntax_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ABSTRACT3=null;
        Token SYNTAX4=null;
        RuleParser.typedecl_return typedecl5 = null;


        Tree ABSTRACT3_tree=null;
        Tree SYNTAX4_tree=null;
        RewriteRuleTokenStream stream_SYNTAX=new RewriteRuleTokenStream(adaptor,"token SYNTAX");
        RewriteRuleTokenStream stream_ABSTRACT=new RewriteRuleTokenStream(adaptor,"token ABSTRACT");
        RewriteRuleSubtreeStream stream_typedecl=new RewriteRuleSubtreeStream(adaptor,"rule typedecl");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:29:16: ( ( ABSTRACT SYNTAX ) ( typedecl )* -> ^( ConcProduction ( typedecl )* ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:30:1: ( ABSTRACT SYNTAX ) ( typedecl )*
            {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:30:1: ( ABSTRACT SYNTAX )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:30:2: ABSTRACT SYNTAX
            {
            ABSTRACT3=(Token)match(input,ABSTRACT,FOLLOW_ABSTRACT_in_abstractsyntax265); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ABSTRACT.add(ABSTRACT3);

            SYNTAX4=(Token)match(input,SYNTAX,FOLLOW_SYNTAX_in_abstractsyntax267); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SYNTAX.add(SYNTAX4);


            }

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:30:19: ( typedecl )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==ID) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:30:20: typedecl
            	    {
            	    pushFollow(FOLLOW_typedecl_in_abstractsyntax271);
            	    typedecl5=typedecl();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_typedecl.add(typedecl5.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);



            // AST REWRITE
            // elements: typedecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 31:3: -> ^( ConcProduction ( typedecl )* )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:31:6: ^( ConcProduction ( typedecl )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_1);

                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:31:23: ( typedecl )*
                while ( stream_typedecl.hasNext() ) {
                    adaptor.addChild(root_1, stream_typedecl.nextTree());

                }
                stream_typedecl.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "abstractsyntax"

    public static class functions_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functions"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:34:1: functions : ( FUNCTIONS ) ( typedecl )* -> ^( ConcProduction ( typedecl )* ) ;
    public final RuleParser.functions_return functions() throws RecognitionException {
        RuleParser.functions_return retval = new RuleParser.functions_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token FUNCTIONS6=null;
        RuleParser.typedecl_return typedecl7 = null;


        Tree FUNCTIONS6_tree=null;
        RewriteRuleTokenStream stream_FUNCTIONS=new RewriteRuleTokenStream(adaptor,"token FUNCTIONS");
        RewriteRuleSubtreeStream stream_typedecl=new RewriteRuleSubtreeStream(adaptor,"rule typedecl");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:34:11: ( ( FUNCTIONS ) ( typedecl )* -> ^( ConcProduction ( typedecl )* ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:35:1: ( FUNCTIONS ) ( typedecl )*
            {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:35:1: ( FUNCTIONS )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:35:2: FUNCTIONS
            {
            FUNCTIONS6=(Token)match(input,FUNCTIONS,FOLLOW_FUNCTIONS_in_functions297); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_FUNCTIONS.add(FUNCTIONS6);


            }

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:35:13: ( typedecl )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==ID) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:35:14: typedecl
            	    {
            	    pushFollow(FOLLOW_typedecl_in_functions301);
            	    typedecl7=typedecl();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_typedecl.add(typedecl7.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);



            // AST REWRITE
            // elements: typedecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 36:3: -> ^( ConcProduction ( typedecl )* )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:36:6: ^( ConcProduction ( typedecl )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_1);

                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:36:23: ( typedecl )*
                while ( stream_typedecl.hasNext() ) {
                    adaptor.addChild(root_1, stream_typedecl.nextTree());

                }
                stream_typedecl.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "functions"

    public static class strategies_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "strategies"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:39:1: strategies : STRATEGIES ( stratdecl )* -> ^( ConcStratDecl ( stratdecl )* ) ;
    public final RuleParser.strategies_return strategies() throws RecognitionException {
        RuleParser.strategies_return retval = new RuleParser.strategies_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token STRATEGIES8=null;
        RuleParser.stratdecl_return stratdecl9 = null;


        Tree STRATEGIES8_tree=null;
        RewriteRuleTokenStream stream_STRATEGIES=new RewriteRuleTokenStream(adaptor,"token STRATEGIES");
        RewriteRuleSubtreeStream stream_stratdecl=new RewriteRuleSubtreeStream(adaptor,"rule stratdecl");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:39:12: ( STRATEGIES ( stratdecl )* -> ^( ConcStratDecl ( stratdecl )* ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:40:1: STRATEGIES ( stratdecl )*
            {
            STRATEGIES8=(Token)match(input,STRATEGIES,FOLLOW_STRATEGIES_in_strategies326); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STRATEGIES.add(STRATEGIES8);

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:40:12: ( stratdecl )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==ID) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:40:13: stratdecl
            	    {
            	    pushFollow(FOLLOW_stratdecl_in_strategies329);
            	    stratdecl9=stratdecl();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_stratdecl.add(stratdecl9.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);



            // AST REWRITE
            // elements: stratdecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 41:3: -> ^( ConcStratDecl ( stratdecl )* )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:41:6: ^( ConcStratDecl ( stratdecl )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcStratDecl, "ConcStratDecl"), root_1);

                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:41:22: ( stratdecl )*
                while ( stream_stratdecl.hasNext() ) {
                    adaptor.addChild(root_1, stream_stratdecl.nextTree());

                }
                stream_stratdecl.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "strategies"

    public static class trs_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "trs"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:44:1: trs : ( TRS LBRACKET ( rule ( ( COMMA )? rule )* ) RBRACKET -> ^( Otrs ^( ConcRule ( rule )* ) ) | TRS ( rule ( ( COMMA )? rule )* ) -> ^( Trs ^( ConcRule ( rule )* ) ) );
    public final RuleParser.trs_return trs() throws RecognitionException {
        RuleParser.trs_return retval = new RuleParser.trs_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token TRS10=null;
        Token LBRACKET11=null;
        Token COMMA13=null;
        Token RBRACKET15=null;
        Token TRS16=null;
        Token COMMA18=null;
        RuleParser.rule_return rule12 = null;

        RuleParser.rule_return rule14 = null;

        RuleParser.rule_return rule17 = null;

        RuleParser.rule_return rule19 = null;


        Tree TRS10_tree=null;
        Tree LBRACKET11_tree=null;
        Tree COMMA13_tree=null;
        Tree RBRACKET15_tree=null;
        Tree TRS16_tree=null;
        Tree COMMA18_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_TRS=new RewriteRuleTokenStream(adaptor,"token TRS");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:44:5: ( TRS LBRACKET ( rule ( ( COMMA )? rule )* ) RBRACKET -> ^( Otrs ^( ConcRule ( rule )* ) ) | TRS ( rule ( ( COMMA )? rule )* ) -> ^( Trs ^( ConcRule ( rule )* ) ) )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==TRS) ) {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==LBRACKET) ) {
                    alt11=1;
                }
                else if ( (LA11_1==ID||LA11_1==104) ) {
                    alt11=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:45:3: TRS LBRACKET ( rule ( ( COMMA )? rule )* ) RBRACKET
                    {
                    TRS10=(Token)match(input,TRS,FOLLOW_TRS_in_trs356); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TRS.add(TRS10);

                    LBRACKET11=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_trs358); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET11);

                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:45:16: ( rule ( ( COMMA )? rule )* )
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:45:17: rule ( ( COMMA )? rule )*
                    {
                    pushFollow(FOLLOW_rule_in_trs361);
                    rule12=rule();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_rule.add(rule12.getTree());
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:45:22: ( ( COMMA )? rule )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==COMMA||LA8_0==ID||LA8_0==104) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:45:23: ( COMMA )? rule
                    	    {
                    	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:45:23: ( COMMA )?
                    	    int alt7=2;
                    	    int LA7_0 = input.LA(1);

                    	    if ( (LA7_0==COMMA) ) {
                    	        alt7=1;
                    	    }
                    	    switch (alt7) {
                    	        case 1 :
                    	            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:45:23: COMMA
                    	            {
                    	            COMMA13=(Token)match(input,COMMA,FOLLOW_COMMA_in_trs364); if (state.failed) return retval; 
                    	            if ( state.backtracking==0 ) stream_COMMA.add(COMMA13);


                    	            }
                    	            break;

                    	    }

                    	    pushFollow(FOLLOW_rule_in_trs367);
                    	    rule14=rule();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_rule.add(rule14.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);


                    }

                    RBRACKET15=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_trs372); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET15);



                    // AST REWRITE
                    // elements: rule
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 46:3: -> ^( Otrs ^( ConcRule ( rule )* ) )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:46:6: ^( Otrs ^( ConcRule ( rule )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Otrs, "Otrs"), root_1);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:46:13: ^( ConcRule ( rule )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcRule, "ConcRule"), root_2);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:46:24: ( rule )*
                        while ( stream_rule.hasNext() ) {
                            adaptor.addChild(root_2, stream_rule.nextTree());

                        }
                        stream_rule.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:48:3: TRS ( rule ( ( COMMA )? rule )* )
                    {
                    TRS16=(Token)match(input,TRS,FOLLOW_TRS_in_trs394); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TRS.add(TRS16);

                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:48:7: ( rule ( ( COMMA )? rule )* )
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:48:8: rule ( ( COMMA )? rule )*
                    {
                    pushFollow(FOLLOW_rule_in_trs397);
                    rule17=rule();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_rule.add(rule17.getTree());
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:48:13: ( ( COMMA )? rule )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==COMMA||LA10_0==ID||LA10_0==104) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:48:14: ( COMMA )? rule
                    	    {
                    	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:48:14: ( COMMA )?
                    	    int alt9=2;
                    	    int LA9_0 = input.LA(1);

                    	    if ( (LA9_0==COMMA) ) {
                    	        alt9=1;
                    	    }
                    	    switch (alt9) {
                    	        case 1 :
                    	            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:48:14: COMMA
                    	            {
                    	            COMMA18=(Token)match(input,COMMA,FOLLOW_COMMA_in_trs400); if (state.failed) return retval; 
                    	            if ( state.backtracking==0 ) stream_COMMA.add(COMMA18);


                    	            }
                    	            break;

                    	    }

                    	    pushFollow(FOLLOW_rule_in_trs403);
                    	    rule19=rule();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_rule.add(rule19.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);


                    }



                    // AST REWRITE
                    // elements: rule
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 49:3: -> ^( Trs ^( ConcRule ( rule )* ) )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:49:6: ^( Trs ^( ConcRule ( rule )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Trs, "Trs"), root_1);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:49:12: ^( ConcRule ( rule )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcRule, "ConcRule"), root_2);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:49:23: ( rule )*
                        while ( stream_rule.hasNext() ) {
                            adaptor.addChild(root_2, stream_rule.nextTree());

                        }
                        stream_rule.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "trs"

    public static class stratdecl_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "stratdecl"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:52:1: stratdecl : stratname= ID paramlist EQUALS strategy -> ^( StratDecl $stratname paramlist strategy ) ;
    public final RuleParser.stratdecl_return stratdecl() throws RecognitionException {
        RuleParser.stratdecl_return retval = new RuleParser.stratdecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token stratname=null;
        Token EQUALS21=null;
        RuleParser.paramlist_return paramlist20 = null;

        RuleParser.strategy_return strategy22 = null;


        Tree stratname_tree=null;
        Tree EQUALS21_tree=null;
        RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_strategy=new RewriteRuleSubtreeStream(adaptor,"rule strategy");
        RewriteRuleSubtreeStream stream_paramlist=new RewriteRuleSubtreeStream(adaptor,"rule paramlist");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:52:11: (stratname= ID paramlist EQUALS strategy -> ^( StratDecl $stratname paramlist strategy ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:53:5: stratname= ID paramlist EQUALS strategy
            {
            stratname=(Token)match(input,ID,FOLLOW_ID_in_stratdecl439); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(stratname);

            pushFollow(FOLLOW_paramlist_in_stratdecl441);
            paramlist20=paramlist();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_paramlist.add(paramlist20.getTree());
            EQUALS21=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_stratdecl444); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EQUALS.add(EQUALS21);

            pushFollow(FOLLOW_strategy_in_stratdecl446);
            strategy22=strategy();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_strategy.add(strategy22.getTree());


            // AST REWRITE
            // elements: stratname, strategy, paramlist
            // token labels: stratname
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_stratname=new RewriteRuleTokenStream(adaptor,"token stratname",stratname);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 54:7: -> ^( StratDecl $stratname paramlist strategy )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:54:10: ^( StratDecl $stratname paramlist strategy )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratDecl, "StratDecl"), root_1);

                adaptor.addChild(root_1, stream_stratname.nextNode());
                adaptor.addChild(root_1, stream_paramlist.nextTree());
                adaptor.addChild(root_1, stream_strategy.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "stratdecl"

    public static class paramlist_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "paramlist"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:57:1: paramlist : LPAR ( param ( COMMA param )* )? RPAR -> ^( ConcParam ( param )* ) ;
    public final RuleParser.paramlist_return paramlist() throws RecognitionException {
        RuleParser.paramlist_return retval = new RuleParser.paramlist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAR23=null;
        Token COMMA25=null;
        Token RPAR27=null;
        RuleParser.param_return param24 = null;

        RuleParser.param_return param26 = null;


        Tree LPAR23_tree=null;
        Tree COMMA25_tree=null;
        Tree RPAR27_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_param=new RewriteRuleSubtreeStream(adaptor,"rule param");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:57:11: ( LPAR ( param ( COMMA param )* )? RPAR -> ^( ConcParam ( param )* ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:58:3: LPAR ( param ( COMMA param )* )? RPAR
            {
            LPAR23=(Token)match(input,LPAR,FOLLOW_LPAR_in_paramlist478); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR23);

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:58:8: ( param ( COMMA param )* )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==ID) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:58:9: param ( COMMA param )*
                    {
                    pushFollow(FOLLOW_param_in_paramlist481);
                    param24=param();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_param.add(param24.getTree());
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:58:15: ( COMMA param )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==COMMA) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:58:16: COMMA param
                    	    {
                    	    COMMA25=(Token)match(input,COMMA,FOLLOW_COMMA_in_paramlist484); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA25);

                    	    pushFollow(FOLLOW_param_in_paramlist486);
                    	    param26=param();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_param.add(param26.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR27=(Token)match(input,RPAR,FOLLOW_RPAR_in_paramlist493); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR27);



            // AST REWRITE
            // elements: param
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 59:2: -> ^( ConcParam ( param )* )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:59:5: ^( ConcParam ( param )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcParam, "ConcParam"), root_1);

                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:59:17: ( param )*
                while ( stream_param.hasNext() ) {
                    adaptor.addChild(root_1, stream_param.nextTree());

                }
                stream_param.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "paramlist"

    public static class param_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "param"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:62:1: param : ID -> ^( Param ID ) ;
    public final RuleParser.param_return param() throws RecognitionException {
        RuleParser.param_return retval = new RuleParser.param_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID28=null;

        Tree ID28_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:62:6: ( ID -> ^( Param ID ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:63:3: ID
            {
            ID28=(Token)match(input,ID,FOLLOW_ID_in_param516); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID28);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 64:2: -> ^( Param ID )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:64:5: ^( Param ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Param, "Param"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "param"

    public static class typedecl_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typedecl"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:68:1: typedecl : typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType $typename) $alts) ;
    public final RuleParser.typedecl_return typedecl() throws RecognitionException {
        RuleParser.typedecl_return retval = new RuleParser.typedecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token typename=null;
        Token EQUALS29=null;
        RuleParser.alternatives_return alts = null;


        Tree typename_tree=null;
        Tree EQUALS29_tree=null;
        RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_alternatives=new RewriteRuleSubtreeStream(adaptor,"rule alternatives");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:68:10: (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType $typename) $alts) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:69:5: typename= ID EQUALS alts= alternatives[typename]
            {
            typename=(Token)match(input,ID,FOLLOW_ID_in_typedecl543); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(typename);

            EQUALS29=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl545); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EQUALS.add(EQUALS29);

            pushFollow(FOLLOW_alternatives_in_typedecl549);
            alts=alternatives(typename);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_alternatives.add(alts.getTree());


            // AST REWRITE
            // elements: alts, typename
            // token labels: typename
            // rule labels: retval, alts
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_typename=new RewriteRuleTokenStream(adaptor,"token typename",typename);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_alts=new RewriteRuleSubtreeStream(adaptor,"rule alts",alts!=null?alts.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 70:7: -> ^( SortType ^( GomType $typename) $alts)
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:70:10: ^( SortType ^( GomType $typename) $alts)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SortType, "SortType"), root_1);

                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:70:21: ^( GomType $typename)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                adaptor.addChild(root_2, stream_typename.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_alts.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typedecl"

    public static class alternatives_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "alternatives"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:73:1: alternatives[Token typename] : ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* -> ^( ConcAlternative ( opdecl )+ ) ;
    public final RuleParser.alternatives_return alternatives(Token typename) throws RecognitionException {
        RuleParser.alternatives_return retval = new RuleParser.alternatives_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ALT30=null;
        Token ALT32=null;
        RuleParser.opdecl_return opdecl31 = null;

        RuleParser.opdecl_return opdecl33 = null;


        Tree ALT30_tree=null;
        Tree ALT32_tree=null;
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleSubtreeStream stream_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule opdecl");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:73:30: ( ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* -> ^( ConcAlternative ( opdecl )+ ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:74:3: ( ALT )? opdecl[typename] ( ALT opdecl[typename] )*
            {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:74:3: ( ALT )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==ALT) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:74:4: ALT
                    {
                    ALT30=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives587); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ALT.add(ALT30);


                    }
                    break;

            }

            pushFollow(FOLLOW_opdecl_in_alternatives591);
            opdecl31=opdecl(typename);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_opdecl.add(opdecl31.getTree());
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:74:27: ( ALT opdecl[typename] )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==ALT) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:74:28: ALT opdecl[typename]
            	    {
            	    ALT32=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives595); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_ALT.add(ALT32);

            	    pushFollow(FOLLOW_opdecl_in_alternatives597);
            	    opdecl33=opdecl(typename);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_opdecl.add(opdecl33.getTree());

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);



            // AST REWRITE
            // elements: opdecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 75:3: -> ^( ConcAlternative ( opdecl )+ )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:75:6: ^( ConcAlternative ( opdecl )+ )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcAlternative, "ConcAlternative"), root_1);

                if ( !(stream_opdecl.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_opdecl.hasNext() ) {
                    adaptor.addChild(root_1, stream_opdecl.nextTree());

                }
                stream_opdecl.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "alternatives"

    public static class opdecl_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "opdecl"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:78:1: opdecl[Token type] : ID fieldlist -> ^( Alternative ID fieldlist ^( GomType ID[type] ) ) ;
    public final RuleParser.opdecl_return opdecl(Token type) throws RecognitionException {
        RuleParser.opdecl_return retval = new RuleParser.opdecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID34=null;
        RuleParser.fieldlist_return fieldlist35 = null;


        Tree ID34_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule fieldlist");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:78:20: ( ID fieldlist -> ^( Alternative ID fieldlist ^( GomType ID[type] ) ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:79:2: ID fieldlist
            {
            ID34=(Token)match(input,ID,FOLLOW_ID_in_opdecl627); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID34);

            pushFollow(FOLLOW_fieldlist_in_opdecl629);
            fieldlist35=fieldlist();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_fieldlist.add(fieldlist35.getTree());


            // AST REWRITE
            // elements: ID, fieldlist, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 80:3: -> ^( Alternative ID fieldlist ^( GomType ID[type] ) )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:80:6: ^( Alternative ID fieldlist ^( GomType ID[type] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Alternative, "Alternative"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:80:33: ^( GomType ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "opdecl"

    public static class fieldlist_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fieldlist"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:84:1: fieldlist : LPAR ( field ( COMMA field )* )? RPAR -> ^( ConcField ( field )* ) ;
    public final RuleParser.fieldlist_return fieldlist() throws RecognitionException {
        RuleParser.fieldlist_return retval = new RuleParser.fieldlist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAR36=null;
        Token COMMA38=null;
        Token RPAR40=null;
        RuleParser.field_return field37 = null;

        RuleParser.field_return field39 = null;


        Tree LPAR36_tree=null;
        Tree COMMA38_tree=null;
        Tree RPAR40_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:84:11: ( LPAR ( field ( COMMA field )* )? RPAR -> ^( ConcField ( field )* ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:85:3: LPAR ( field ( COMMA field )* )? RPAR
            {
            LPAR36=(Token)match(input,LPAR,FOLLOW_LPAR_in_fieldlist668); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAR.add(LPAR36);

            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:85:8: ( field ( COMMA field )* )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==ID) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:85:9: field ( COMMA field )*
                    {
                    pushFollow(FOLLOW_field_in_fieldlist671);
                    field37=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field37.getTree());
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:85:15: ( COMMA field )*
                    loop16:
                    do {
                        int alt16=2;
                        int LA16_0 = input.LA(1);

                        if ( (LA16_0==COMMA) ) {
                            alt16=1;
                        }


                        switch (alt16) {
                    	case 1 :
                    	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:85:16: COMMA field
                    	    {
                    	    COMMA38=(Token)match(input,COMMA,FOLLOW_COMMA_in_fieldlist674); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA38);

                    	    pushFollow(FOLLOW_field_in_fieldlist676);
                    	    field39=field();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_field.add(field39.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop16;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR40=(Token)match(input,RPAR,FOLLOW_RPAR_in_fieldlist683); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAR.add(RPAR40);



            // AST REWRITE
            // elements: field
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 85:38: -> ^( ConcField ( field )* )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:85:41: ^( ConcField ( field )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:85:53: ( field )*
                while ( stream_field.hasNext() ) {
                    adaptor.addChild(root_1, stream_field.nextTree());

                }
                stream_field.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "fieldlist"

    public static class field_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "field"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:88:1: field : type -> ^( UnamedField type ) ;
    public final RuleParser.field_return field() throws RecognitionException {
        RuleParser.field_return retval = new RuleParser.field_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        RuleParser.type_return type41 = null;


        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:88:6: ( type -> ^( UnamedField type ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:89:3: type
            {
            pushFollow(FOLLOW_type_in_field705);
            type41=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_type.add(type41.getTree());


            // AST REWRITE
            // elements: type
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 89:8: -> ^( UnamedField type )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:89:11: ^( UnamedField type )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(UnamedField, "UnamedField"), root_1);

                adaptor.addChild(root_1, stream_type.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "field"

    public static class type_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "type"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:92:1: type : ID -> ^( GomType ID ) ;
    public final RuleParser.type_return type() throws RecognitionException {
        RuleParser.type_return retval = new RuleParser.type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID42=null;

        Tree ID42_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:92:5: ( ID -> ^( GomType ID ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:93:3: ID
            {
            ID42=(Token)match(input,ID,FOLLOW_ID_in_type725); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID42);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 93:6: -> ^( GomType ID )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:93:9: ^( GomType ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "type"

    public static class strategy_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "strategy"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:97:1: strategy : (s1= elementarystrategy ( SEMICOLON s2= strategy | CHOICE s3= strategy )? -> {s2!=null}? ^( StratSequence $s1 $s2) -> {s3!=null}? ^( StratChoice $s1 $s3) -> $s1 | LBRACE ( rule ( COMMA rule )* )? RBRACE -> ^( StratTrs ^( Trs ^( ConcRule ( rule )* ) ) ) | LBRACKET ( rule ( COMMA rule )* )? RBRACKET -> ^( StratTrs ^( Otrs ^( ConcRule ( rule )* ) ) ) );
    public final RuleParser.strategy_return strategy() throws RecognitionException {
        RuleParser.strategy_return retval = new RuleParser.strategy_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token SEMICOLON43=null;
        Token CHOICE44=null;
        Token LBRACE45=null;
        Token COMMA47=null;
        Token RBRACE49=null;
        Token LBRACKET50=null;
        Token COMMA52=null;
        Token RBRACKET54=null;
        RuleParser.elementarystrategy_return s1 = null;

        RuleParser.strategy_return s2 = null;

        RuleParser.strategy_return s3 = null;

        RuleParser.rule_return rule46 = null;

        RuleParser.rule_return rule48 = null;

        RuleParser.rule_return rule51 = null;

        RuleParser.rule_return rule53 = null;


        Tree SEMICOLON43_tree=null;
        Tree CHOICE44_tree=null;
        Tree LBRACE45_tree=null;
        Tree COMMA47_tree=null;
        Tree RBRACE49_tree=null;
        Tree LBRACKET50_tree=null;
        Tree COMMA52_tree=null;
        Tree RBRACKET54_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_SEMICOLON=new RewriteRuleTokenStream(adaptor,"token SEMICOLON");
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_CHOICE=new RewriteRuleTokenStream(adaptor,"token CHOICE");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_elementarystrategy=new RewriteRuleSubtreeStream(adaptor,"rule elementarystrategy");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        RewriteRuleSubtreeStream stream_strategy=new RewriteRuleSubtreeStream(adaptor,"rule strategy");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:97:10: (s1= elementarystrategy ( SEMICOLON s2= strategy | CHOICE s3= strategy )? -> {s2!=null}? ^( StratSequence $s1 $s2) -> {s3!=null}? ^( StratChoice $s1 $s3) -> $s1 | LBRACE ( rule ( COMMA rule )* )? RBRACE -> ^( StratTrs ^( Trs ^( ConcRule ( rule )* ) ) ) | LBRACKET ( rule ( COMMA rule )* )? RBRACKET -> ^( StratTrs ^( Otrs ^( ConcRule ( rule )* ) ) ) )
            int alt23=3;
            switch ( input.LA(1) ) {
            case ID:
            case LPAR:
            case IDENTITY:
            case FAIL:
            case ALL:
            case ONE:
            case MU:
                {
                alt23=1;
                }
                break;
            case LBRACE:
                {
                alt23=2;
                }
                break;
            case LBRACKET:
                {
                alt23=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:98:3: s1= elementarystrategy ( SEMICOLON s2= strategy | CHOICE s3= strategy )?
                    {
                    pushFollow(FOLLOW_elementarystrategy_in_strategy749);
                    s1=elementarystrategy();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_elementarystrategy.add(s1.getTree());
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:98:25: ( SEMICOLON s2= strategy | CHOICE s3= strategy )?
                    int alt18=3;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==SEMICOLON) ) {
                        alt18=1;
                    }
                    else if ( (LA18_0==CHOICE) ) {
                        alt18=2;
                    }
                    switch (alt18) {
                        case 1 :
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:99:8: SEMICOLON s2= strategy
                            {
                            SEMICOLON43=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_strategy760); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_SEMICOLON.add(SEMICOLON43);

                            pushFollow(FOLLOW_strategy_in_strategy764);
                            s2=strategy();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_strategy.add(s2.getTree());

                            }
                            break;
                        case 2 :
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:100:8: CHOICE s3= strategy
                            {
                            CHOICE44=(Token)match(input,CHOICE,FOLLOW_CHOICE_in_strategy773); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CHOICE.add(CHOICE44);

                            pushFollow(FOLLOW_strategy_in_strategy777);
                            s3=strategy();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_strategy.add(s3.getTree());

                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: s3, s1, s2, s1, s1
                    // token labels: 
                    // rule labels: retval, s2, s1, s3
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_s2=new RewriteRuleSubtreeStream(adaptor,"rule s2",s2!=null?s2.tree:null);
                    RewriteRuleSubtreeStream stream_s1=new RewriteRuleSubtreeStream(adaptor,"rule s1",s1!=null?s1.tree:null);
                    RewriteRuleSubtreeStream stream_s3=new RewriteRuleSubtreeStream(adaptor,"rule s3",s3!=null?s3.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 102:6: -> {s2!=null}? ^( StratSequence $s1 $s2)
                    if (s2!=null) {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:102:21: ^( StratSequence $s1 $s2)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratSequence, "StratSequence"), root_1);

                        adaptor.addChild(root_1, stream_s1.nextTree());
                        adaptor.addChild(root_1, stream_s2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 103:6: -> {s3!=null}? ^( StratChoice $s1 $s3)
                    if (s3!=null) {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:103:21: ^( StratChoice $s1 $s3)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratChoice, "StratChoice"), root_1);

                        adaptor.addChild(root_1, stream_s1.nextTree());
                        adaptor.addChild(root_1, stream_s3.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 104:6: -> $s1
                    {
                        adaptor.addChild(root_0, stream_s1.nextTree());

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:106:5: LBRACE ( rule ( COMMA rule )* )? RBRACE
                    {
                    LBRACE45=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_strategy840); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACE.add(LBRACE45);

                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:106:12: ( rule ( COMMA rule )* )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==ID||LA20_0==104) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:106:13: rule ( COMMA rule )*
                            {
                            pushFollow(FOLLOW_rule_in_strategy843);
                            rule46=rule();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_rule.add(rule46.getTree());
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:106:18: ( COMMA rule )*
                            loop19:
                            do {
                                int alt19=2;
                                int LA19_0 = input.LA(1);

                                if ( (LA19_0==COMMA) ) {
                                    alt19=1;
                                }


                                switch (alt19) {
                            	case 1 :
                            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:106:19: COMMA rule
                            	    {
                            	    COMMA47=(Token)match(input,COMMA,FOLLOW_COMMA_in_strategy846); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA47);

                            	    pushFollow(FOLLOW_rule_in_strategy848);
                            	    rule48=rule();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_rule.add(rule48.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop19;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RBRACE49=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_strategy854); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACE.add(RBRACE49);



                    // AST REWRITE
                    // elements: rule
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 106:41: -> ^( StratTrs ^( Trs ^( ConcRule ( rule )* ) ) )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:106:44: ^( StratTrs ^( Trs ^( ConcRule ( rule )* ) ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratTrs, "StratTrs"), root_1);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:106:55: ^( Trs ^( ConcRule ( rule )* ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Trs, "Trs"), root_2);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:106:61: ^( ConcRule ( rule )* )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcRule, "ConcRule"), root_3);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:106:72: ( rule )*
                        while ( stream_rule.hasNext() ) {
                            adaptor.addChild(root_3, stream_rule.nextTree());

                        }
                        stream_rule.reset();

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:107:5: LBRACKET ( rule ( COMMA rule )* )? RBRACKET
                    {
                    LBRACKET50=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_strategy879); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET50);

                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:107:14: ( rule ( COMMA rule )* )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==ID||LA22_0==104) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:107:15: rule ( COMMA rule )*
                            {
                            pushFollow(FOLLOW_rule_in_strategy882);
                            rule51=rule();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_rule.add(rule51.getTree());
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:107:20: ( COMMA rule )*
                            loop21:
                            do {
                                int alt21=2;
                                int LA21_0 = input.LA(1);

                                if ( (LA21_0==COMMA) ) {
                                    alt21=1;
                                }


                                switch (alt21) {
                            	case 1 :
                            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:107:21: COMMA rule
                            	    {
                            	    COMMA52=(Token)match(input,COMMA,FOLLOW_COMMA_in_strategy885); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA52);

                            	    pushFollow(FOLLOW_rule_in_strategy887);
                            	    rule53=rule();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_rule.add(rule53.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop21;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RBRACKET54=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_strategy893); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET54);



                    // AST REWRITE
                    // elements: rule
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 107:45: -> ^( StratTrs ^( Otrs ^( ConcRule ( rule )* ) ) )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:107:48: ^( StratTrs ^( Otrs ^( ConcRule ( rule )* ) ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratTrs, "StratTrs"), root_1);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:107:59: ^( Otrs ^( ConcRule ( rule )* ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Otrs, "Otrs"), root_2);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:107:66: ^( ConcRule ( rule )* )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcRule, "ConcRule"), root_3);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:107:77: ( rule )*
                        while ( stream_rule.hasNext() ) {
                            adaptor.addChild(root_3, stream_rule.nextTree());

                        }
                        stream_rule.reset();

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "strategy"

    public static class elementarystrategy_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "elementarystrategy"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:111:1: elementarystrategy options {backtrack=true; } : ( IDENTITY -> ^( StratIdentity ) | FAIL -> ^( StratFail ) | LPAR strategy RPAR -> strategy | ALL LPAR strategy RPAR -> ^( StratAll strategy ) | ONE LPAR strategy RPAR -> ^( StratOne strategy ) | MU ID DOT LPAR strategy RPAR -> ^( StratMu ID strategy ) | ID LPAR ( strategy ( COMMA strategy )* )? RPAR -> ^( StratAppl ID ^( ConcStrat ( strategy )* ) ) | ID -> ^( StratName ID ) );
    public final RuleParser.elementarystrategy_return elementarystrategy() throws RecognitionException {
        RuleParser.elementarystrategy_return retval = new RuleParser.elementarystrategy_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token IDENTITY55=null;
        Token FAIL56=null;
        Token LPAR57=null;
        Token RPAR59=null;
        Token ALL60=null;
        Token LPAR61=null;
        Token RPAR63=null;
        Token ONE64=null;
        Token LPAR65=null;
        Token RPAR67=null;
        Token MU68=null;
        Token ID69=null;
        Token DOT70=null;
        Token LPAR71=null;
        Token RPAR73=null;
        Token ID74=null;
        Token LPAR75=null;
        Token COMMA77=null;
        Token RPAR79=null;
        Token ID80=null;
        RuleParser.strategy_return strategy58 = null;

        RuleParser.strategy_return strategy62 = null;

        RuleParser.strategy_return strategy66 = null;

        RuleParser.strategy_return strategy72 = null;

        RuleParser.strategy_return strategy76 = null;

        RuleParser.strategy_return strategy78 = null;


        Tree IDENTITY55_tree=null;
        Tree FAIL56_tree=null;
        Tree LPAR57_tree=null;
        Tree RPAR59_tree=null;
        Tree ALL60_tree=null;
        Tree LPAR61_tree=null;
        Tree RPAR63_tree=null;
        Tree ONE64_tree=null;
        Tree LPAR65_tree=null;
        Tree RPAR67_tree=null;
        Tree MU68_tree=null;
        Tree ID69_tree=null;
        Tree DOT70_tree=null;
        Tree LPAR71_tree=null;
        Tree RPAR73_tree=null;
        Tree ID74_tree=null;
        Tree LPAR75_tree=null;
        Tree COMMA77_tree=null;
        Tree RPAR79_tree=null;
        Tree ID80_tree=null;
        RewriteRuleTokenStream stream_MU=new RewriteRuleTokenStream(adaptor,"token MU");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_ONE=new RewriteRuleTokenStream(adaptor,"token ONE");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_IDENTITY=new RewriteRuleTokenStream(adaptor,"token IDENTITY");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_FAIL=new RewriteRuleTokenStream(adaptor,"token FAIL");
        RewriteRuleTokenStream stream_ALL=new RewriteRuleTokenStream(adaptor,"token ALL");
        RewriteRuleSubtreeStream stream_strategy=new RewriteRuleSubtreeStream(adaptor,"rule strategy");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:111:49: ( IDENTITY -> ^( StratIdentity ) | FAIL -> ^( StratFail ) | LPAR strategy RPAR -> strategy | ALL LPAR strategy RPAR -> ^( StratAll strategy ) | ONE LPAR strategy RPAR -> ^( StratOne strategy ) | MU ID DOT LPAR strategy RPAR -> ^( StratMu ID strategy ) | ID LPAR ( strategy ( COMMA strategy )* )? RPAR -> ^( StratAppl ID ^( ConcStrat ( strategy )* ) ) | ID -> ^( StratName ID ) )
            int alt26=8;
            alt26 = dfa26.predict(input);
            switch (alt26) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:112:5: IDENTITY
                    {
                    IDENTITY55=(Token)match(input,IDENTITY,FOLLOW_IDENTITY_in_elementarystrategy938); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTITY.add(IDENTITY55);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 112:14: -> ^( StratIdentity )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:112:17: ^( StratIdentity )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratIdentity, "StratIdentity"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:113:5: FAIL
                    {
                    FAIL56=(Token)match(input,FAIL,FOLLOW_FAIL_in_elementarystrategy950); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_FAIL.add(FAIL56);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 113:10: -> ^( StratFail )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:113:13: ^( StratFail )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratFail, "StratFail"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:114:5: LPAR strategy RPAR
                    {
                    LPAR57=(Token)match(input,LPAR,FOLLOW_LPAR_in_elementarystrategy962); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR57);

                    pushFollow(FOLLOW_strategy_in_elementarystrategy964);
                    strategy58=strategy();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_strategy.add(strategy58.getTree());
                    RPAR59=(Token)match(input,RPAR,FOLLOW_RPAR_in_elementarystrategy966); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR59);



                    // AST REWRITE
                    // elements: strategy
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 114:24: -> strategy
                    {
                        adaptor.addChild(root_0, stream_strategy.nextTree());

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:115:5: ALL LPAR strategy RPAR
                    {
                    ALL60=(Token)match(input,ALL,FOLLOW_ALL_in_elementarystrategy976); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ALL.add(ALL60);

                    LPAR61=(Token)match(input,LPAR,FOLLOW_LPAR_in_elementarystrategy978); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR61);

                    pushFollow(FOLLOW_strategy_in_elementarystrategy980);
                    strategy62=strategy();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_strategy.add(strategy62.getTree());
                    RPAR63=(Token)match(input,RPAR,FOLLOW_RPAR_in_elementarystrategy982); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR63);



                    // AST REWRITE
                    // elements: strategy
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 115:28: -> ^( StratAll strategy )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:115:31: ^( StratAll strategy )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratAll, "StratAll"), root_1);

                        adaptor.addChild(root_1, stream_strategy.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:116:5: ONE LPAR strategy RPAR
                    {
                    ONE64=(Token)match(input,ONE,FOLLOW_ONE_in_elementarystrategy996); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ONE.add(ONE64);

                    LPAR65=(Token)match(input,LPAR,FOLLOW_LPAR_in_elementarystrategy998); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR65);

                    pushFollow(FOLLOW_strategy_in_elementarystrategy1000);
                    strategy66=strategy();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_strategy.add(strategy66.getTree());
                    RPAR67=(Token)match(input,RPAR,FOLLOW_RPAR_in_elementarystrategy1002); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR67);



                    // AST REWRITE
                    // elements: strategy
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 116:28: -> ^( StratOne strategy )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:116:31: ^( StratOne strategy )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratOne, "StratOne"), root_1);

                        adaptor.addChild(root_1, stream_strategy.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:117:5: MU ID DOT LPAR strategy RPAR
                    {
                    MU68=(Token)match(input,MU,FOLLOW_MU_in_elementarystrategy1016); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MU.add(MU68);

                    ID69=(Token)match(input,ID,FOLLOW_ID_in_elementarystrategy1018); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID69);

                    DOT70=(Token)match(input,DOT,FOLLOW_DOT_in_elementarystrategy1020); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DOT.add(DOT70);

                    LPAR71=(Token)match(input,LPAR,FOLLOW_LPAR_in_elementarystrategy1022); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR71);

                    pushFollow(FOLLOW_strategy_in_elementarystrategy1024);
                    strategy72=strategy();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_strategy.add(strategy72.getTree());
                    RPAR73=(Token)match(input,RPAR,FOLLOW_RPAR_in_elementarystrategy1026); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR73);



                    // AST REWRITE
                    // elements: ID, strategy
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 117:34: -> ^( StratMu ID strategy )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:117:37: ^( StratMu ID strategy )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratMu, "StratMu"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_strategy.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 7 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:118:5: ID LPAR ( strategy ( COMMA strategy )* )? RPAR
                    {
                    ID74=(Token)match(input,ID,FOLLOW_ID_in_elementarystrategy1042); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID74);

                    LPAR75=(Token)match(input,LPAR,FOLLOW_LPAR_in_elementarystrategy1044); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR75);

                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:118:13: ( strategy ( COMMA strategy )* )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==LBRACKET||LA25_0==ID||LA25_0==LPAR||LA25_0==LBRACE||(LA25_0>=IDENTITY && LA25_0<=MU)) ) {
                        alt25=1;
                    }
                    switch (alt25) {
                        case 1 :
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:118:14: strategy ( COMMA strategy )*
                            {
                            pushFollow(FOLLOW_strategy_in_elementarystrategy1047);
                            strategy76=strategy();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_strategy.add(strategy76.getTree());
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:118:23: ( COMMA strategy )*
                            loop24:
                            do {
                                int alt24=2;
                                int LA24_0 = input.LA(1);

                                if ( (LA24_0==COMMA) ) {
                                    alt24=1;
                                }


                                switch (alt24) {
                            	case 1 :
                            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:118:24: COMMA strategy
                            	    {
                            	    COMMA77=(Token)match(input,COMMA,FOLLOW_COMMA_in_elementarystrategy1050); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA77);

                            	    pushFollow(FOLLOW_strategy_in_elementarystrategy1052);
                            	    strategy78=strategy();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_strategy.add(strategy78.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop24;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR79=(Token)match(input,RPAR,FOLLOW_RPAR_in_elementarystrategy1058); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR79);



                    // AST REWRITE
                    // elements: ID, strategy
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 119:8: -> ^( StratAppl ID ^( ConcStrat ( strategy )* ) )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:119:11: ^( StratAppl ID ^( ConcStrat ( strategy )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratAppl, "StratAppl"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:119:26: ^( ConcStrat ( strategy )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcStrat, "ConcStrat"), root_2);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:119:38: ( strategy )*
                        while ( stream_strategy.hasNext() ) {
                            adaptor.addChild(root_2, stream_strategy.nextTree());

                        }
                        stream_strategy.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 8 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:120:5: ID
                    {
                    ID80=(Token)match(input,ID,FOLLOW_ID_in_elementarystrategy1088); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID80);



                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 120:8: -> ^( StratName ID )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:120:11: ^( StratName ID )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StratName, "StratName"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "elementarystrategy"

    public static class rule_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "rule"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:123:1: rule : ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) | ID ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule ^( Var ID ) term ) -> ^( ConditionalRule ^( Var ID ) term $cond) );
    public final RuleParser.rule_return rule() throws RecognitionException {
        RuleParser.rule_return retval = new RuleParser.rule_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ARROW82=null;
        Token IF84=null;
        Token ID85=null;
        Token ARROW86=null;
        Token IF88=null;
        RuleParser.condition_return cond = null;

        RuleParser.pattern_return pattern81 = null;

        RuleParser.term_return term83 = null;

        RuleParser.term_return term87 = null;


        Tree ARROW82_tree=null;
        Tree IF84_tree=null;
        Tree ID85_tree=null;
        Tree ARROW86_tree=null;
        Tree IF88_tree=null;
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleSubtreeStream stream_pattern=new RewriteRuleSubtreeStream(adaptor,"rule pattern");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:123:6: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) | ID ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule ^( Var ID ) term ) -> ^( ConditionalRule ^( Var ID ) term $cond) )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==ID) ) {
                int LA29_1 = input.LA(2);

                if ( (LA29_1==LPAR) ) {
                    alt29=1;
                }
                else if ( (LA29_1==ARROW) ) {
                    alt29=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 29, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA29_0==104) ) {
                alt29=1;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }
            switch (alt29) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:124:3: pattern ARROW term ( IF cond= condition )?
                    {
                    pushFollow(FOLLOW_pattern_in_rule1109);
                    pattern81=pattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_pattern.add(pattern81.getTree());
                    ARROW82=(Token)match(input,ARROW,FOLLOW_ARROW_in_rule1111); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARROW.add(ARROW82);

                    pushFollow(FOLLOW_term_in_rule1113);
                    term83=term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_term.add(term83.getTree());
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:124:22: ( IF cond= condition )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==IF) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:124:23: IF cond= condition
                            {
                            IF84=(Token)match(input,IF,FOLLOW_IF_in_rule1116); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IF.add(IF84);

                            pushFollow(FOLLOW_condition_in_rule1120);
                            cond=condition();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_condition.add(cond.getTree());

                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: cond, term, pattern, pattern, term
                    // token labels: 
                    // rule labels: retval, cond
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 125:5: -> { cond == null }? ^( Rule pattern term )
                    if ( cond == null ) {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:125:26: ^( Rule pattern term )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Rule, "Rule"), root_1);

                        adaptor.addChild(root_1, stream_pattern.nextTree());
                        adaptor.addChild(root_1, stream_term.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 126:5: -> ^( ConditionalRule pattern term $cond)
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:126:8: ^( ConditionalRule pattern term $cond)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConditionalRule, "ConditionalRule"), root_1);

                        adaptor.addChild(root_1, stream_pattern.nextTree());
                        adaptor.addChild(root_1, stream_term.nextTree());
                        adaptor.addChild(root_1, stream_cond.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:127:5: ID ARROW term ( IF cond= condition )?
                    {
                    ID85=(Token)match(input,ID,FOLLOW_ID_in_rule1161); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID85);

                    ARROW86=(Token)match(input,ARROW,FOLLOW_ARROW_in_rule1163); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARROW.add(ARROW86);

                    pushFollow(FOLLOW_term_in_rule1165);
                    term87=term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_term.add(term87.getTree());
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:127:19: ( IF cond= condition )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==IF) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:127:20: IF cond= condition
                            {
                            IF88=(Token)match(input,IF,FOLLOW_IF_in_rule1168); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IF.add(IF88);

                            pushFollow(FOLLOW_condition_in_rule1172);
                            cond=condition();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_condition.add(cond.getTree());

                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: ID, ID, term, cond, term
                    // token labels: 
                    // rule labels: retval, cond
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 128:5: -> { cond == null }? ^( Rule ^( Var ID ) term )
                    if ( cond == null ) {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:128:26: ^( Rule ^( Var ID ) term )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Rule, "Rule"), root_1);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:128:33: ^( Var ID )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Var, "Var"), root_2);

                        adaptor.addChild(root_2, stream_ID.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_term.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 129:5: -> ^( ConditionalRule ^( Var ID ) term $cond)
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:129:8: ^( ConditionalRule ^( Var ID ) term $cond)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConditionalRule, "ConditionalRule"), root_1);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:129:26: ^( Var ID )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Var, "Var"), root_2);

                        adaptor.addChild(root_2, stream_ID.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_term.nextTree());
                        adaptor.addChild(root_1, stream_cond.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "rule"

    public static class condition_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "condition"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:131:1: condition : p1= term DOUBLEEQUALS p2= term -> ^( CondEquals $p1 $p2) ;
    public final RuleParser.condition_return condition() throws RecognitionException {
        RuleParser.condition_return retval = new RuleParser.condition_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token DOUBLEEQUALS89=null;
        RuleParser.term_return p1 = null;

        RuleParser.term_return p2 = null;


        Tree DOUBLEEQUALS89_tree=null;
        RewriteRuleTokenStream stream_DOUBLEEQUALS=new RewriteRuleTokenStream(adaptor,"token DOUBLEEQUALS");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:131:11: (p1= term DOUBLEEQUALS p2= term -> ^( CondEquals $p1 $p2) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:132:3: p1= term DOUBLEEQUALS p2= term
            {
            pushFollow(FOLLOW_term_in_condition1228);
            p1=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_term.add(p1.getTree());
            DOUBLEEQUALS89=(Token)match(input,DOUBLEEQUALS,FOLLOW_DOUBLEEQUALS_in_condition1230); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_DOUBLEEQUALS.add(DOUBLEEQUALS89);

            pushFollow(FOLLOW_term_in_condition1234);
            p2=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_term.add(p2.getTree());


            // AST REWRITE
            // elements: p2, p1
            // token labels: 
            // rule labels: retval, p2, p1
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_p2=new RewriteRuleSubtreeStream(adaptor,"rule p2",p2!=null?p2.tree:null);
            RewriteRuleSubtreeStream stream_p1=new RewriteRuleSubtreeStream(adaptor,"rule p1",p1!=null?p1.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 133:5: -> ^( CondEquals $p1 $p2)
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:133:8: ^( CondEquals $p1 $p2)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondEquals, "CondEquals"), root_1);

                adaptor.addChild(root_1, stream_p1.nextTree());
                adaptor.addChild(root_1, stream_p2.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "condition"

    public static class pattern_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pattern"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:135:1: pattern : ( ID LPAR ( term ( COMMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | '!' term -> ^( Anti term ) );
    public final RuleParser.pattern_return pattern() throws RecognitionException {
        RuleParser.pattern_return retval = new RuleParser.pattern_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID90=null;
        Token LPAR91=null;
        Token COMMA93=null;
        Token RPAR95=null;
        Token char_literal96=null;
        RuleParser.term_return term92 = null;

        RuleParser.term_return term94 = null;

        RuleParser.term_return term97 = null;


        Tree ID90_tree=null;
        Tree LPAR91_tree=null;
        Tree COMMA93_tree=null;
        Tree RPAR95_tree=null;
        Tree char_literal96_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_104=new RewriteRuleTokenStream(adaptor,"token 104");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:135:9: ( ID LPAR ( term ( COMMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | '!' term -> ^( Anti term ) )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==ID) ) {
                alt32=1;
            }
            else if ( (LA32_0==104) ) {
                alt32=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:136:5: ID LPAR ( term ( COMMA term )* )? RPAR
                    {
                    ID90=(Token)match(input,ID,FOLLOW_ID_in_pattern1264); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID90);

                    LPAR91=(Token)match(input,LPAR,FOLLOW_LPAR_in_pattern1266); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAR.add(LPAR91);

                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:136:13: ( term ( COMMA term )* )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==ID||LA31_0==INT||LA31_0==104) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:136:14: term ( COMMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern1269);
                            term92=term();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term.add(term92.getTree());
                            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:136:19: ( COMMA term )*
                            loop30:
                            do {
                                int alt30=2;
                                int LA30_0 = input.LA(1);

                                if ( (LA30_0==COMMA) ) {
                                    alt30=1;
                                }


                                switch (alt30) {
                            	case 1 :
                            	    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:136:20: COMMA term
                            	    {
                            	    COMMA93=(Token)match(input,COMMA,FOLLOW_COMMA_in_pattern1272); if (state.failed) return retval; 
                            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA93);

                            	    pushFollow(FOLLOW_term_in_pattern1274);
                            	    term94=term();

                            	    state._fsp--;
                            	    if (state.failed) return retval;
                            	    if ( state.backtracking==0 ) stream_term.add(term94.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop30;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR95=(Token)match(input,RPAR,FOLLOW_RPAR_in_pattern1280); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAR.add(RPAR95);



                    // AST REWRITE
                    // elements: ID, term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 136:40: -> ^( Appl ID ^( TermList ( term )* ) )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:136:43: ^( Appl ID ^( TermList ( term )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Appl, "Appl"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:136:53: ^( TermList ( term )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(TermList, "TermList"), root_2);

                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:136:64: ( term )*
                        while ( stream_term.hasNext() ) {
                            adaptor.addChild(root_2, stream_term.nextTree());

                        }
                        stream_term.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:137:5: '!' term
                    {
                    char_literal96=(Token)match(input,104,FOLLOW_104_in_pattern1301); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_104.add(char_literal96);

                    pushFollow(FOLLOW_term_in_pattern1303);
                    term97=term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_term.add(term97.getTree());


                    // AST REWRITE
                    // elements: term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 137:14: -> ^( Anti term )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:137:17: ^( Anti term )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Anti, "Anti"), root_1);

                        adaptor.addChild(root_1, stream_term.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "pattern"

    public static class term_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:139:1: term : ( pattern | ID -> ^( Var ID ) | builtin );
    public final RuleParser.term_return term() throws RecognitionException {
        RuleParser.term_return retval = new RuleParser.term_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID99=null;
        RuleParser.pattern_return pattern98 = null;

        RuleParser.builtin_return builtin100 = null;


        Tree ID99_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:139:6: ( pattern | ID -> ^( Var ID ) | builtin )
            int alt33=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA33_1 = input.LA(2);

                if ( (LA33_1==LPAR) ) {
                    alt33=1;
                }
                else if ( (LA33_1==EOF||(LA33_1>=COMMA && LA33_1<=ID)||LA33_1==RPAR||LA33_1==RBRACE||(LA33_1>=ARROW && LA33_1<=DOUBLEEQUALS)||LA33_1==104) ) {
                    alt33=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 33, 1, input);

                    throw nvae;
                }
                }
                break;
            case 104:
                {
                alt33=1;
                }
                break;
            case INT:
                {
                alt33=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;
            }

            switch (alt33) {
                case 1 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:140:5: pattern
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term1323);
                    pattern98=pattern();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern98.getTree());

                    }
                    break;
                case 2 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:141:5: ID
                    {
                    ID99=(Token)match(input,ID,FOLLOW_ID_in_term1329); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID99);



                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 141:8: -> ^( Var ID )
                    {
                        // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:141:11: ^( Var ID )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Var, "Var"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:142:5: builtin
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term1343);
                    builtin100=builtin();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, builtin100.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "term"

    public static class builtin_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "builtin"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:145:1: builtin : INT -> ^( BuiltinInt INT ) ;
    public final RuleParser.builtin_return builtin() throws RecognitionException {
        RuleParser.builtin_return retval = new RuleParser.builtin_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token INT101=null;

        Tree INT101_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");

        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:145:9: ( INT -> ^( BuiltinInt INT ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:146:3: INT
            {
            INT101=(Token)match(input,INT,FOLLOW_INT_in_builtin1354); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_INT.add(INT101);



            // AST REWRITE
            // elements: INT
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 146:7: -> ^( BuiltinInt INT )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:146:10: ^( BuiltinInt INT )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(BuiltinInt, "BuiltinInt"), root_1);

                adaptor.addChild(root_1, stream_INT.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "builtin"

    public static class symbol_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "symbol"
    // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:149:1: symbol : ID COLON INT -> ^( Symbol ID INT ) ;
    public final RuleParser.symbol_return symbol() throws RecognitionException {
        RuleParser.symbol_return retval = new RuleParser.symbol_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID102=null;
        Token COLON103=null;
        Token INT104=null;

        Tree ID102_tree=null;
        Tree COLON103_tree=null;
        Tree INT104_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:149:8: ( ID COLON INT -> ^( Symbol ID INT ) )
            // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:150:3: ID COLON INT
            {
            ID102=(Token)match(input,ID,FOLLOW_ID_in_symbol1373); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID102);

            COLON103=(Token)match(input,COLON,FOLLOW_COLON_in_symbol1375); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(COLON103);

            INT104=(Token)match(input,INT,FOLLOW_INT_in_symbol1377); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_INT.add(INT104);



            // AST REWRITE
            // elements: INT, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 150:16: -> ^( Symbol ID INT )
            {
                // /Users/NicolasRoubertier/tom/applications/strategyAnalyzer/src/sa/Rule.g:150:19: ^( Symbol ID INT )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Symbol, "Symbol"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_INT.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "symbol"

    // Delegated rules


    protected DFA26 dfa26 = new DFA26(this);
    static final String DFA26_eotS =
        "\12\uffff";
    static final String DFA26_eofS =
        "\7\uffff\1\11\2\uffff";
    static final String DFA26_minS =
        "\1\106\6\uffff\1\102\2\uffff";
    static final String DFA26_maxS =
        "\1\123\6\uffff\1\114\2\uffff";
    static final String DFA26_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\uffff\1\7\1\10";
    static final String DFA26_specialS =
        "\12\uffff}>";
    static final String[] DFA26_transitionS = {
            "\1\7\1\uffff\1\3\6\uffff\1\1\1\2\1\4\1\5\1\6",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\11\1\uffff\1\11\1\uffff\1\11\1\uffff\1\10\1\11\1\uffff\2"+
            "\11",
            "",
            ""
    };

    static final short[] DFA26_eot = DFA.unpackEncodedString(DFA26_eotS);
    static final short[] DFA26_eof = DFA.unpackEncodedString(DFA26_eofS);
    static final char[] DFA26_min = DFA.unpackEncodedStringToUnsignedChars(DFA26_minS);
    static final char[] DFA26_max = DFA.unpackEncodedStringToUnsignedChars(DFA26_maxS);
    static final short[] DFA26_accept = DFA.unpackEncodedString(DFA26_acceptS);
    static final short[] DFA26_special = DFA.unpackEncodedString(DFA26_specialS);
    static final short[][] DFA26_transition;

    static {
        int numStates = DFA26_transitionS.length;
        DFA26_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA26_transition[i] = DFA.unpackEncodedString(DFA26_transitionS[i]);
        }
    }

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = DFA26_eot;
            this.eof = DFA26_eof;
            this.min = DFA26_min;
            this.max = DFA26_max;
            this.accept = DFA26_accept;
            this.special = DFA26_special;
            this.transition = DFA26_transition;
        }
        public String getDescription() {
            return "111:1: elementarystrategy options {backtrack=true; } : ( IDENTITY -> ^( StratIdentity ) | FAIL -> ^( StratFail ) | LPAR strategy RPAR -> strategy | ALL LPAR strategy RPAR -> ^( StratAll strategy ) | ONE LPAR strategy RPAR -> ^( StratOne strategy ) | MU ID DOT LPAR strategy RPAR -> ^( StratMu ID strategy ) | ID LPAR ( strategy ( COMMA strategy )* )? RPAR -> ^( StratAppl ID ^( ConcStrat ( strategy )* ) ) | ID -> ^( StratName ID ) );";
        }
    }
 

    public static final BitSet FOLLOW_abstractsyntax_in_program54 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_functions_in_program59 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_strategies_in_program66 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_trs_in_program73 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_program77 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_abstractsyntax265 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_SYNTAX_in_abstractsyntax267 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_typedecl_in_abstractsyntax271 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_FUNCTIONS_in_functions297 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_typedecl_in_functions301 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_STRATEGIES_in_strategies326 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_stratdecl_in_strategies329 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_TRS_in_trs356 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_LBRACKET_in_trs358 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000040L});
    public static final BitSet FOLLOW_rule_in_trs361 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000070L});
    public static final BitSet FOLLOW_COMMA_in_trs364 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000040L});
    public static final BitSet FOLLOW_rule_in_trs367 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000070L});
    public static final BitSet FOLLOW_RBRACKET_in_trs372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRS_in_trs394 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000040L});
    public static final BitSet FOLLOW_rule_in_trs397 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000050L});
    public static final BitSet FOLLOW_COMMA_in_trs400 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000040L});
    public static final BitSet FOLLOW_rule_in_trs403 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000050L});
    public static final BitSet FOLLOW_ID_in_stratdecl439 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_paramlist_in_stratdecl441 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_EQUALS_in_stratdecl444 = new BitSet(new long[]{0x0000000000000000L,0x00000000000FA148L});
    public static final BitSet FOLLOW_strategy_in_stratdecl446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_paramlist478 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000240L});
    public static final BitSet FOLLOW_param_in_paramlist481 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000210L});
    public static final BitSet FOLLOW_COMMA_in_paramlist484 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_param_in_paramlist486 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000210L});
    public static final BitSet FOLLOW_RPAR_in_paramlist493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_param516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl543 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl545 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000440L});
    public static final BitSet FOLLOW_alternatives_in_typedecl549 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALT_in_alternatives587 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000440L});
    public static final BitSet FOLLOW_opdecl_in_alternatives591 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_ALT_in_alternatives595 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000440L});
    public static final BitSet FOLLOW_opdecl_in_alternatives597 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_ID_in_opdecl627 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_fieldlist_in_opdecl629 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_fieldlist668 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000240L});
    public static final BitSet FOLLOW_field_in_fieldlist671 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000210L});
    public static final BitSet FOLLOW_COMMA_in_fieldlist674 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_field_in_fieldlist676 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000210L});
    public static final BitSet FOLLOW_RPAR_in_fieldlist683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_field705 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_type725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementarystrategy_in_strategy749 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001800L});
    public static final BitSet FOLLOW_SEMICOLON_in_strategy760 = new BitSet(new long[]{0x0000000000000000L,0x00000000000FA148L});
    public static final BitSet FOLLOW_strategy_in_strategy764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHOICE_in_strategy773 = new BitSet(new long[]{0x0000000000000000L,0x00000000000FA148L});
    public static final BitSet FOLLOW_strategy_in_strategy777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_strategy840 = new BitSet(new long[]{0x0000000000000000L,0x0000010000004040L});
    public static final BitSet FOLLOW_rule_in_strategy843 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004010L});
    public static final BitSet FOLLOW_COMMA_in_strategy846 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000040L});
    public static final BitSet FOLLOW_rule_in_strategy848 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004010L});
    public static final BitSet FOLLOW_RBRACE_in_strategy854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_strategy879 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000060L});
    public static final BitSet FOLLOW_rule_in_strategy882 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
    public static final BitSet FOLLOW_COMMA_in_strategy885 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000040L});
    public static final BitSet FOLLOW_rule_in_strategy887 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
    public static final BitSet FOLLOW_RBRACKET_in_strategy893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTITY_in_elementarystrategy938 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FAIL_in_elementarystrategy950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_elementarystrategy962 = new BitSet(new long[]{0x0000000000000000L,0x00000000000FA148L});
    public static final BitSet FOLLOW_strategy_in_elementarystrategy964 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_RPAR_in_elementarystrategy966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALL_in_elementarystrategy976 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_LPAR_in_elementarystrategy978 = new BitSet(new long[]{0x0000000000000000L,0x00000000000FA148L});
    public static final BitSet FOLLOW_strategy_in_elementarystrategy980 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_RPAR_in_elementarystrategy982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ONE_in_elementarystrategy996 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_LPAR_in_elementarystrategy998 = new BitSet(new long[]{0x0000000000000000L,0x00000000000FA148L});
    public static final BitSet FOLLOW_strategy_in_elementarystrategy1000 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_RPAR_in_elementarystrategy1002 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MU_in_elementarystrategy1016 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_ID_in_elementarystrategy1018 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_elementarystrategy1020 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_LPAR_in_elementarystrategy1022 = new BitSet(new long[]{0x0000000000000000L,0x00000000000FA148L});
    public static final BitSet FOLLOW_strategy_in_elementarystrategy1024 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_RPAR_in_elementarystrategy1026 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_elementarystrategy1042 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_LPAR_in_elementarystrategy1044 = new BitSet(new long[]{0x0000000000000000L,0x00000000000FA348L});
    public static final BitSet FOLLOW_strategy_in_elementarystrategy1047 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000210L});
    public static final BitSet FOLLOW_COMMA_in_elementarystrategy1050 = new BitSet(new long[]{0x0000000000000000L,0x00000000000FA148L});
    public static final BitSet FOLLOW_strategy_in_elementarystrategy1052 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000210L});
    public static final BitSet FOLLOW_RPAR_in_elementarystrategy1058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_elementarystrategy1088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule1109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_ARROW_in_rule1111 = new BitSet(new long[]{0x0000000000000000L,0x0000010001000040L});
    public static final BitSet FOLLOW_term_in_rule1113 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_IF_in_rule1116 = new BitSet(new long[]{0x0000000000000000L,0x0000010001000040L});
    public static final BitSet FOLLOW_condition_in_rule1120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_rule1161 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_ARROW_in_rule1163 = new BitSet(new long[]{0x0000000000000000L,0x0000010001000040L});
    public static final BitSet FOLLOW_term_in_rule1165 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_IF_in_rule1168 = new BitSet(new long[]{0x0000000000000000L,0x0000010001000040L});
    public static final BitSet FOLLOW_condition_in_rule1172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_condition1228 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_DOUBLEEQUALS_in_condition1230 = new BitSet(new long[]{0x0000000000000000L,0x0000010001000040L});
    public static final BitSet FOLLOW_term_in_condition1234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern1264 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_LPAR_in_pattern1266 = new BitSet(new long[]{0x0000000000000000L,0x0000010001000240L});
    public static final BitSet FOLLOW_term_in_pattern1269 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000210L});
    public static final BitSet FOLLOW_COMMA_in_pattern1272 = new BitSet(new long[]{0x0000000000000000L,0x0000010001000040L});
    public static final BitSet FOLLOW_term_in_pattern1274 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000210L});
    public static final BitSet FOLLOW_RPAR_in_pattern1280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_104_in_pattern1301 = new BitSet(new long[]{0x0000000000000000L,0x0000010001000040L});
    public static final BitSet FOLLOW_term_in_pattern1303 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term1323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term1329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term1343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_builtin1354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_symbol1373 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_COLON_in_symbol1375 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_INT_in_symbol1377 = new BitSet(new long[]{0x0000000000000002L});

}