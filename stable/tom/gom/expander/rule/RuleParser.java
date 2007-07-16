// $ANTLR 3.0 ../src/tom/gom/expander/rule/Rule.g 2007-07-16 10:32:47

  package tom.gom.expander.rule;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULELIST", "RULE", "CONDRULE", "APPL", "LAB", "CONDTERM", "CONDEQUALS", "CONDNOTEQUALS", "CONDLEQ", "CONDLT", "CONDGEQ", "CONDGT", "CONDMETHOD", "ARROW", "IF", "EQUALS", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "DOT", "ID", "LPAR", "RPAR", "COMA", "INT", "STRING", "COLON", "ESC", "WS", "SLCOMMENT"
    };
    public static final int CONDGEQ=14;
    public static final int NOTEQUALS=20;
    public static final int RPAR=28;
    public static final int CONDLT=13;
    public static final int WS=34;
    public static final int LPAR=27;
    public static final int ARROW=17;
    public static final int STRING=31;
    public static final int LT=22;
    public static final int LEQ=21;
    public static final int GT=24;
    public static final int CONDGT=15;
    public static final int RULE=5;
    public static final int RULELIST=4;
    public static final int GEQ=23;
    public static final int CONDEQUALS=10;
    public static final int ESC=33;
    public static final int LAB=8;
    public static final int DOT=25;
    public static final int EQUALS=19;
    public static final int CONDTERM=9;
    public static final int SLCOMMENT=35;
    public static final int CONDNOTEQUALS=11;
    public static final int CONDMETHOD=16;
    public static final int INT=30;
    public static final int IF=18;
    public static final int CONDLEQ=12;
    public static final int EOF=-1;
    public static final int COLON=32;
    public static final int CONDRULE=6;
    public static final int APPL=7;
    public static final int COMA=29;
    public static final int ID=26;

        public RuleParser(TokenStream input) {
            super(input);
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "../src/tom/gom/expander/rule/Rule.g"; }


    public static class ruleset_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ruleset
    // ../src/tom/gom/expander/rule/Rule.g:30:1: ruleset : ( rule )* EOF -> ^( RULELIST ( rule )* ) ;
    public final ruleset_return ruleset() throws RecognitionException {
        ruleset_return retval = new ruleset_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EOF2=null;
        rule_return rule1 = null;


        CommonTree EOF2_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        try {
            // ../src/tom/gom/expander/rule/Rule.g:31:3: ( ( rule )* EOF -> ^( RULELIST ( rule )* ) )
            // ../src/tom/gom/expander/rule/Rule.g:31:3: ( rule )* EOF
            {
            // ../src/tom/gom/expander/rule/Rule.g:31:3: ( rule )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../src/tom/gom/expander/rule/Rule.g:31:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset116);
            	    rule1=rule();
            	    _fsp--;

            	    stream_rule.add(rule1.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            EOF2=(Token)input.LT(1);
            match(input,EOF,FOLLOW_EOF_in_ruleset120); 
            stream_EOF.add(EOF2);


            // AST REWRITE
            // elements: rule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 31:15: -> ^( RULELIST ( rule )* )
            {
                // ../src/tom/gom/expander/rule/Rule.g:31:18: ^( RULELIST ( rule )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(RULELIST, "RULELIST"), root_1);

                // ../src/tom/gom/expander/rule/Rule.g:31:29: ( rule )*
                while ( stream_rule.hasNext() ) {
                    adaptor.addChild(root_1, stream_rule.next());

                }
                stream_rule.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end ruleset

    public static class rule_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start rule
    // ../src/tom/gom/expander/rule/Rule.g:33:1: rule : pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE pattern term ) -> ^( CONDRULE pattern term $cond) ;
    public final rule_return rule() throws RecognitionException {
        rule_return retval = new rule_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ARROW4=null;
        Token IF6=null;
        condition_return cond = null;

        pattern_return pattern3 = null;

        term_return term5 = null;


        CommonTree ARROW4_tree=null;
        CommonTree IF6_tree=null;
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_pattern=new RewriteRuleSubtreeStream(adaptor,"rule pattern");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // ../src/tom/gom/expander/rule/Rule.g:34:3: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE pattern term ) -> ^( CONDRULE pattern term $cond) )
            // ../src/tom/gom/expander/rule/Rule.g:34:3: pattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_pattern_in_rule143);
            pattern3=pattern();
            _fsp--;

            stream_pattern.add(pattern3.getTree());
            ARROW4=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_rule145); 
            stream_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule147);
            term5=term();
            _fsp--;

            stream_term.add(term5.getTree());
            // ../src/tom/gom/expander/rule/Rule.g:34:22: ( IF cond= condition )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==IF) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ../src/tom/gom/expander/rule/Rule.g:34:23: IF cond= condition
                    {
                    IF6=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_rule150); 
                    stream_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule154);
                    cond=condition();
                    _fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: term, term, cond, pattern, pattern
            // token labels: 
            // rule labels: cond, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 35:5: -> { cond == null }? ^( RULE pattern term )
            if ( cond == null ) {
                // ../src/tom/gom/expander/rule/Rule.g:35:26: ^( RULE pattern term )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(RULE, "RULE"), root_1);

                adaptor.addChild(root_1, stream_pattern.next());
                adaptor.addChild(root_1, stream_term.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 36:5: -> ^( CONDRULE pattern term $cond)
            {
                // ../src/tom/gom/expander/rule/Rule.g:36:8: ^( CONDRULE pattern term $cond)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDRULE, "CONDRULE"), root_1);

                adaptor.addChild(root_1, stream_pattern.next());
                adaptor.addChild(root_1, stream_term.next());
                adaptor.addChild(root_1, stream_cond.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end rule

    public static class graphruleset_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start graphruleset
    // ../src/tom/gom/expander/rule/Rule.g:38:1: graphruleset : ( graphrule )* EOF -> ^( RULELIST ( graphrule )* ) ;
    public final graphruleset_return graphruleset() throws RecognitionException {
        graphruleset_return retval = new graphruleset_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EOF8=null;
        graphrule_return graphrule7 = null;


        CommonTree EOF8_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_graphrule=new RewriteRuleSubtreeStream(adaptor,"rule graphrule");
        try {
            // ../src/tom/gom/expander/rule/Rule.g:39:3: ( ( graphrule )* EOF -> ^( RULELIST ( graphrule )* ) )
            // ../src/tom/gom/expander/rule/Rule.g:39:3: ( graphrule )* EOF
            {
            // ../src/tom/gom/expander/rule/Rule.g:39:3: ( graphrule )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID||(LA3_0>=INT && LA3_0<=STRING)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../src/tom/gom/expander/rule/Rule.g:39:4: graphrule
            	    {
            	    pushFollow(FOLLOW_graphrule_in_graphruleset202);
            	    graphrule7=graphrule();
            	    _fsp--;

            	    stream_graphrule.add(graphrule7.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            EOF8=(Token)input.LT(1);
            match(input,EOF,FOLLOW_EOF_in_graphruleset206); 
            stream_EOF.add(EOF8);


            // AST REWRITE
            // elements: graphrule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 39:20: -> ^( RULELIST ( graphrule )* )
            {
                // ../src/tom/gom/expander/rule/Rule.g:39:23: ^( RULELIST ( graphrule )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(RULELIST, "RULELIST"), root_1);

                // ../src/tom/gom/expander/rule/Rule.g:39:34: ( graphrule )*
                while ( stream_graphrule.hasNext() ) {
                    adaptor.addChild(root_1, stream_graphrule.next());

                }
                stream_graphrule.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end graphruleset

    public static class graphrule_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start graphrule
    // ../src/tom/gom/expander/rule/Rule.g:41:1: graphrule : labelledpattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE labelledpattern term ) -> ^( CONDRULE labelledpattern term $cond) ;
    public final graphrule_return graphrule() throws RecognitionException {
        graphrule_return retval = new graphrule_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ARROW10=null;
        Token IF12=null;
        condition_return cond = null;

        labelledpattern_return labelledpattern9 = null;

        term_return term11 = null;


        CommonTree ARROW10_tree=null;
        CommonTree IF12_tree=null;
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // ../src/tom/gom/expander/rule/Rule.g:42:3: ( labelledpattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE labelledpattern term ) -> ^( CONDRULE labelledpattern term $cond) )
            // ../src/tom/gom/expander/rule/Rule.g:42:3: labelledpattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_labelledpattern_in_graphrule229);
            labelledpattern9=labelledpattern();
            _fsp--;

            stream_labelledpattern.add(labelledpattern9.getTree());
            ARROW10=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_graphrule231); 
            stream_ARROW.add(ARROW10);

            pushFollow(FOLLOW_term_in_graphrule233);
            term11=term();
            _fsp--;

            stream_term.add(term11.getTree());
            // ../src/tom/gom/expander/rule/Rule.g:42:30: ( IF cond= condition )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==IF) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // ../src/tom/gom/expander/rule/Rule.g:42:31: IF cond= condition
                    {
                    IF12=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_graphrule236); 
                    stream_IF.add(IF12);

                    pushFollow(FOLLOW_condition_in_graphrule240);
                    cond=condition();
                    _fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: labelledpattern, term, term, cond, labelledpattern
            // token labels: 
            // rule labels: cond, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 43:5: -> { cond == null }? ^( RULE labelledpattern term )
            if ( cond == null ) {
                // ../src/tom/gom/expander/rule/Rule.g:43:26: ^( RULE labelledpattern term )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(RULE, "RULE"), root_1);

                adaptor.addChild(root_1, stream_labelledpattern.next());
                adaptor.addChild(root_1, stream_term.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 44:5: -> ^( CONDRULE labelledpattern term $cond)
            {
                // ../src/tom/gom/expander/rule/Rule.g:44:8: ^( CONDRULE labelledpattern term $cond)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDRULE, "CONDRULE"), root_1);

                adaptor.addChild(root_1, stream_labelledpattern.next());
                adaptor.addChild(root_1, stream_term.next());
                adaptor.addChild(root_1, stream_cond.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end graphrule

    public static class condition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start condition
    // ../src/tom/gom/expander/rule/Rule.g:46:1: condition : p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CONDEQUALS $p1 $p2) -> {p3!=null}? ^( CONDNOTEQUALS $p1 $p3) -> {p4!=null}? ^( CONDLEQ $p1 $p4) -> {p5!=null}? ^( CONDLT $p1 $p5) -> {p6!=null}? ^( CONDGEQ $p1 $p6) -> {p7!=null}? ^( CONDGT $p1 $p7) -> {p8!=null}? ^( CONDMETHOD $p1 ID $p8) -> ^( CONDTERM $p1) ;
    public final condition_return condition() throws RecognitionException {
        condition_return retval = new condition_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EQUALS13=null;
        Token NOTEQUALS14=null;
        Token LEQ15=null;
        Token LT16=null;
        Token GEQ17=null;
        Token GT18=null;
        Token DOT19=null;
        Token ID20=null;
        Token LPAR21=null;
        Token RPAR22=null;
        term_return p1 = null;

        term_return p2 = null;

        term_return p3 = null;

        term_return p4 = null;

        term_return p5 = null;

        term_return p6 = null;

        term_return p7 = null;

        term_return p8 = null;


        CommonTree EQUALS13_tree=null;
        CommonTree NOTEQUALS14_tree=null;
        CommonTree LEQ15_tree=null;
        CommonTree LT16_tree=null;
        CommonTree GEQ17_tree=null;
        CommonTree GT18_tree=null;
        CommonTree DOT19_tree=null;
        CommonTree ID20_tree=null;
        CommonTree LPAR21_tree=null;
        CommonTree RPAR22_tree=null;
        RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
        RewriteRuleTokenStream stream_LEQ=new RewriteRuleTokenStream(adaptor,"token LEQ");
        RewriteRuleTokenStream stream_LT=new RewriteRuleTokenStream(adaptor,"token LT");
        RewriteRuleTokenStream stream_GT=new RewriteRuleTokenStream(adaptor,"token GT");
        RewriteRuleTokenStream stream_NOTEQUALS=new RewriteRuleTokenStream(adaptor,"token NOTEQUALS");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_GEQ=new RewriteRuleTokenStream(adaptor,"token GEQ");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // ../src/tom/gom/expander/rule/Rule.g:47:3: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CONDEQUALS $p1 $p2) -> {p3!=null}? ^( CONDNOTEQUALS $p1 $p3) -> {p4!=null}? ^( CONDLEQ $p1 $p4) -> {p5!=null}? ^( CONDLT $p1 $p5) -> {p6!=null}? ^( CONDGEQ $p1 $p6) -> {p7!=null}? ^( CONDGT $p1 $p7) -> {p8!=null}? ^( CONDMETHOD $p1 ID $p8) -> ^( CONDTERM $p1) )
            // ../src/tom/gom/expander/rule/Rule.g:47:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
            {
            pushFollow(FOLLOW_term_in_condition289);
            p1=term();
            _fsp--;

            stream_term.add(p1.getTree());
            // ../src/tom/gom/expander/rule/Rule.g:47:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
            int alt5=8;
            switch ( input.LA(1) ) {
                case EQUALS:
                    {
                    alt5=1;
                    }
                    break;
                case NOTEQUALS:
                    {
                    alt5=2;
                    }
                    break;
                case LEQ:
                    {
                    alt5=3;
                    }
                    break;
                case LT:
                    {
                    alt5=4;
                    }
                    break;
                case GEQ:
                    {
                    alt5=5;
                    }
                    break;
                case GT:
                    {
                    alt5=6;
                    }
                    break;
                case DOT:
                    {
                    alt5=7;
                    }
                    break;
            }

            switch (alt5) {
                case 1 :
                    // ../src/tom/gom/expander/rule/Rule.g:47:12: EQUALS p2= term
                    {
                    EQUALS13=(Token)input.LT(1);
                    match(input,EQUALS,FOLLOW_EQUALS_in_condition292); 
                    stream_EQUALS.add(EQUALS13);

                    pushFollow(FOLLOW_term_in_condition296);
                    p2=term();
                    _fsp--;

                    stream_term.add(p2.getTree());

                    }
                    break;
                case 2 :
                    // ../src/tom/gom/expander/rule/Rule.g:48:7: NOTEQUALS p3= term
                    {
                    NOTEQUALS14=(Token)input.LT(1);
                    match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_condition304); 
                    stream_NOTEQUALS.add(NOTEQUALS14);

                    pushFollow(FOLLOW_term_in_condition308);
                    p3=term();
                    _fsp--;

                    stream_term.add(p3.getTree());

                    }
                    break;
                case 3 :
                    // ../src/tom/gom/expander/rule/Rule.g:49:7: LEQ p4= term
                    {
                    LEQ15=(Token)input.LT(1);
                    match(input,LEQ,FOLLOW_LEQ_in_condition316); 
                    stream_LEQ.add(LEQ15);

                    pushFollow(FOLLOW_term_in_condition320);
                    p4=term();
                    _fsp--;

                    stream_term.add(p4.getTree());

                    }
                    break;
                case 4 :
                    // ../src/tom/gom/expander/rule/Rule.g:50:7: LT p5= term
                    {
                    LT16=(Token)input.LT(1);
                    match(input,LT,FOLLOW_LT_in_condition328); 
                    stream_LT.add(LT16);

                    pushFollow(FOLLOW_term_in_condition332);
                    p5=term();
                    _fsp--;

                    stream_term.add(p5.getTree());

                    }
                    break;
                case 5 :
                    // ../src/tom/gom/expander/rule/Rule.g:51:7: GEQ p6= term
                    {
                    GEQ17=(Token)input.LT(1);
                    match(input,GEQ,FOLLOW_GEQ_in_condition340); 
                    stream_GEQ.add(GEQ17);

                    pushFollow(FOLLOW_term_in_condition344);
                    p6=term();
                    _fsp--;

                    stream_term.add(p6.getTree());

                    }
                    break;
                case 6 :
                    // ../src/tom/gom/expander/rule/Rule.g:52:7: GT p7= term
                    {
                    GT18=(Token)input.LT(1);
                    match(input,GT,FOLLOW_GT_in_condition352); 
                    stream_GT.add(GT18);

                    pushFollow(FOLLOW_term_in_condition356);
                    p7=term();
                    _fsp--;

                    stream_term.add(p7.getTree());

                    }
                    break;
                case 7 :
                    // ../src/tom/gom/expander/rule/Rule.g:53:7: DOT ID LPAR p8= term RPAR
                    {
                    DOT19=(Token)input.LT(1);
                    match(input,DOT,FOLLOW_DOT_in_condition364); 
                    stream_DOT.add(DOT19);

                    ID20=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_condition366); 
                    stream_ID.add(ID20);

                    LPAR21=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_condition368); 
                    stream_LPAR.add(LPAR21);

                    pushFollow(FOLLOW_term_in_condition372);
                    p8=term();
                    _fsp--;

                    stream_term.add(p8.getTree());
                    RPAR22=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_condition374); 
                    stream_RPAR.add(RPAR22);


                    }
                    break;

            }


            // AST REWRITE
            // elements: p8, p6, p1, p1, p1, p1, p1, p1, p3, p5, p2, p1, p4, ID, p1, p7
            // token labels: 
            // rule labels: p3, p6, p8, p1, p7, p4, p5, p2, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_p3=new RewriteRuleSubtreeStream(adaptor,"token p3",p3!=null?p3.tree:null);
            RewriteRuleSubtreeStream stream_p6=new RewriteRuleSubtreeStream(adaptor,"token p6",p6!=null?p6.tree:null);
            RewriteRuleSubtreeStream stream_p8=new RewriteRuleSubtreeStream(adaptor,"token p8",p8!=null?p8.tree:null);
            RewriteRuleSubtreeStream stream_p1=new RewriteRuleSubtreeStream(adaptor,"token p1",p1!=null?p1.tree:null);
            RewriteRuleSubtreeStream stream_p7=new RewriteRuleSubtreeStream(adaptor,"token p7",p7!=null?p7.tree:null);
            RewriteRuleSubtreeStream stream_p4=new RewriteRuleSubtreeStream(adaptor,"token p4",p4!=null?p4.tree:null);
            RewriteRuleSubtreeStream stream_p5=new RewriteRuleSubtreeStream(adaptor,"token p5",p5!=null?p5.tree:null);
            RewriteRuleSubtreeStream stream_p2=new RewriteRuleSubtreeStream(adaptor,"token p2",p2!=null?p2.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 55:5: -> {p2!=null}? ^( CONDEQUALS $p1 $p2)
            if (p2!=null) {
                // ../src/tom/gom/expander/rule/Rule.g:55:20: ^( CONDEQUALS $p1 $p2)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDEQUALS, "CONDEQUALS"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p2.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 56:5: -> {p3!=null}? ^( CONDNOTEQUALS $p1 $p3)
            if (p3!=null) {
                // ../src/tom/gom/expander/rule/Rule.g:56:20: ^( CONDNOTEQUALS $p1 $p3)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDNOTEQUALS, "CONDNOTEQUALS"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p3.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 57:5: -> {p4!=null}? ^( CONDLEQ $p1 $p4)
            if (p4!=null) {
                // ../src/tom/gom/expander/rule/Rule.g:57:20: ^( CONDLEQ $p1 $p4)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDLEQ, "CONDLEQ"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p4.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 58:5: -> {p5!=null}? ^( CONDLT $p1 $p5)
            if (p5!=null) {
                // ../src/tom/gom/expander/rule/Rule.g:58:20: ^( CONDLT $p1 $p5)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDLT, "CONDLT"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p5.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 59:5: -> {p6!=null}? ^( CONDGEQ $p1 $p6)
            if (p6!=null) {
                // ../src/tom/gom/expander/rule/Rule.g:59:20: ^( CONDGEQ $p1 $p6)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDGEQ, "CONDGEQ"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p6.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 60:5: -> {p7!=null}? ^( CONDGT $p1 $p7)
            if (p7!=null) {
                // ../src/tom/gom/expander/rule/Rule.g:60:20: ^( CONDGT $p1 $p7)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDGT, "CONDGT"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p7.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 61:5: -> {p8!=null}? ^( CONDMETHOD $p1 ID $p8)
            if (p8!=null) {
                // ../src/tom/gom/expander/rule/Rule.g:61:20: ^( CONDMETHOD $p1 ID $p8)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDMETHOD, "CONDMETHOD"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_ID.next());
                adaptor.addChild(root_1, stream_p8.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 62:5: -> ^( CONDTERM $p1)
            {
                // ../src/tom/gom/expander/rule/Rule.g:62:8: ^( CONDTERM $p1)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CONDTERM, "CONDTERM"), root_1);

                adaptor.addChild(root_1, stream_p1.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end condition

    public static class pattern_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start pattern
    // ../src/tom/gom/expander/rule/Rule.g:64:1: pattern : ID LPAR ( term ( COMA term )* )? RPAR -> ^( APPL ID ( term )* ) ;
    public final pattern_return pattern() throws RecognitionException {
        pattern_return retval = new pattern_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID23=null;
        Token LPAR24=null;
        Token COMA26=null;
        Token RPAR28=null;
        term_return term25 = null;

        term_return term27 = null;


        CommonTree ID23_tree=null;
        CommonTree LPAR24_tree=null;
        CommonTree COMA26_tree=null;
        CommonTree RPAR28_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // ../src/tom/gom/expander/rule/Rule.g:65:3: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( APPL ID ( term )* ) )
            // ../src/tom/gom/expander/rule/Rule.g:65:3: ID LPAR ( term ( COMA term )* )? RPAR
            {
            ID23=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_pattern533); 
            stream_ID.add(ID23);

            LPAR24=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_pattern535); 
            stream_LPAR.add(LPAR24);

            // ../src/tom/gom/expander/rule/Rule.g:65:11: ( term ( COMA term )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==ID||(LA7_0>=INT && LA7_0<=STRING)) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../src/tom/gom/expander/rule/Rule.g:65:12: term ( COMA term )*
                    {
                    pushFollow(FOLLOW_term_in_pattern538);
                    term25=term();
                    _fsp--;

                    stream_term.add(term25.getTree());
                    // ../src/tom/gom/expander/rule/Rule.g:65:17: ( COMA term )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // ../src/tom/gom/expander/rule/Rule.g:65:18: COMA term
                    	    {
                    	    COMA26=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_pattern541); 
                    	    stream_COMA.add(COMA26);

                    	    pushFollow(FOLLOW_term_in_pattern543);
                    	    term27=term();
                    	    _fsp--;

                    	    stream_term.add(term27.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR28=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_pattern549); 
            stream_RPAR.add(RPAR28);


            // AST REWRITE
            // elements: term, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 65:37: -> ^( APPL ID ( term )* )
            {
                // ../src/tom/gom/expander/rule/Rule.g:65:40: ^( APPL ID ( term )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(APPL, "APPL"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                // ../src/tom/gom/expander/rule/Rule.g:65:50: ( term )*
                while ( stream_term.hasNext() ) {
                    adaptor.addChild(root_1, stream_term.next());

                }
                stream_term.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end pattern

    public static class term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start term
    // ../src/tom/gom/expander/rule/Rule.g:67:1: term : ( pattern | ID | builtin );
    public final term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID30=null;
        pattern_return pattern29 = null;

        builtin_return builtin31 = null;


        CommonTree ID30_tree=null;

        try {
            // ../src/tom/gom/expander/rule/Rule.g:68:3: ( pattern | ID | builtin )
            int alt8=3;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ID) ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==LPAR) ) {
                    alt8=1;
                }
                else if ( (LA8_1==EOF||(LA8_1>=IF && LA8_1<=ID)||(LA8_1>=RPAR && LA8_1<=STRING)) ) {
                    alt8=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("67:1: term : ( pattern | ID | builtin );", 8, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA8_0>=INT && LA8_0<=STRING)) ) {
                alt8=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("67:1: term : ( pattern | ID | builtin );", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // ../src/tom/gom/expander/rule/Rule.g:68:3: pattern
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term570);
                    pattern29=pattern();
                    _fsp--;

                    adaptor.addChild(root_0, pattern29.getTree());

                    }
                    break;
                case 2 :
                    // ../src/tom/gom/expander/rule/Rule.g:68:14: ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ID30=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_term575); 
                    ID30_tree = (CommonTree)adaptor.create(ID30);
                    adaptor.addChild(root_0, ID30_tree);


                    }
                    break;
                case 3 :
                    // ../src/tom/gom/expander/rule/Rule.g:68:19: builtin
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term579);
                    builtin31=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin31.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end term

    public static class builtin_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start builtin
    // ../src/tom/gom/expander/rule/Rule.g:70:1: builtin : ( INT | STRING );
    public final builtin_return builtin() throws RecognitionException {
        builtin_return retval = new builtin_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set32=null;

        CommonTree set32_tree=null;

        try {
            // ../src/tom/gom/expander/rule/Rule.g:71:1: ( INT | STRING )
            // ../src/tom/gom/expander/rule/Rule.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set32=(Token)input.LT(1);
            if ( (input.LA(1)>=INT && input.LA(1)<=STRING) ) {
                input.consume();
                adaptor.addChild(root_0, adaptor.create(set32));
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_builtin0);    throw mse;
            }


            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end builtin

    public static class labelledpattern_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start labelledpattern
    // ../src/tom/gom/expander/rule/Rule.g:74:1: labelledpattern : (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LAB $namelabel $p) -> ^( $p) ;
    public final labelledpattern_return labelledpattern() throws RecognitionException {
        labelledpattern_return retval = new labelledpattern_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token namelabel=null;
        Token COLON33=null;
        graphpattern_return p = null;


        CommonTree namelabel_tree=null;
        CommonTree COLON33_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_graphpattern=new RewriteRuleSubtreeStream(adaptor,"rule graphpattern");
        try {
            // ../src/tom/gom/expander/rule/Rule.g:75:3: ( (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LAB $namelabel $p) -> ^( $p) )
            // ../src/tom/gom/expander/rule/Rule.g:75:3: (namelabel= ID COLON )? p= graphpattern
            {
            // ../src/tom/gom/expander/rule/Rule.g:75:3: (namelabel= ID COLON )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==ID) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==COLON) ) {
                    alt9=1;
                }
            }
            switch (alt9) {
                case 1 :
                    // ../src/tom/gom/expander/rule/Rule.g:75:4: namelabel= ID COLON
                    {
                    namelabel=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_labelledpattern606); 
                    stream_ID.add(namelabel);

                    COLON33=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_labelledpattern608); 
                    stream_COLON.add(COLON33);


                    }
                    break;

            }

            pushFollow(FOLLOW_graphpattern_in_labelledpattern614);
            p=graphpattern();
            _fsp--;

            stream_graphpattern.add(p.getTree());

            // AST REWRITE
            // elements: p, namelabel, p
            // token labels: namelabel
            // rule labels: p, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_namelabel=new RewriteRuleTokenStream(adaptor,"token namelabel",namelabel);
            RewriteRuleSubtreeStream stream_p=new RewriteRuleSubtreeStream(adaptor,"token p",p!=null?p.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 76:2: -> {$namelabel!=null}? ^( LAB $namelabel $p)
            if (namelabel!=null) {
                // ../src/tom/gom/expander/rule/Rule.g:76:25: ^( LAB $namelabel $p)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(LAB, "LAB"), root_1);

                adaptor.addChild(root_1, stream_namelabel.next());
                adaptor.addChild(root_1, stream_p.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 77:2: -> ^( $p)
            {
                // ../src/tom/gom/expander/rule/Rule.g:77:5: ^( $p)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_p.nextNode(), root_1);

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end labelledpattern

    public static class graphpattern_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start graphpattern
    // ../src/tom/gom/expander/rule/Rule.g:79:1: graphpattern : ( constructor | ID | builtin );
    public final graphpattern_return graphpattern() throws RecognitionException {
        graphpattern_return retval = new graphpattern_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID35=null;
        constructor_return constructor34 = null;

        builtin_return builtin36 = null;


        CommonTree ID35_tree=null;

        try {
            // ../src/tom/gom/expander/rule/Rule.g:80:3: ( constructor | ID | builtin )
            int alt10=3;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==ID) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==LPAR) ) {
                    alt10=1;
                }
                else if ( (LA10_1==ARROW||(LA10_1>=RPAR && LA10_1<=COMA)) ) {
                    alt10=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("79:1: graphpattern : ( constructor | ID | builtin );", 10, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA10_0>=INT && LA10_0<=STRING)) ) {
                alt10=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("79:1: graphpattern : ( constructor | ID | builtin );", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // ../src/tom/gom/expander/rule/Rule.g:80:3: constructor
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_constructor_in_graphpattern647);
                    constructor34=constructor();
                    _fsp--;

                    adaptor.addChild(root_0, constructor34.getTree());

                    }
                    break;
                case 2 :
                    // ../src/tom/gom/expander/rule/Rule.g:80:17: ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ID35=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_graphpattern651); 
                    ID35_tree = (CommonTree)adaptor.create(ID35);
                    adaptor.addChild(root_0, ID35_tree);


                    }
                    break;
                case 3 :
                    // ../src/tom/gom/expander/rule/Rule.g:80:22: builtin
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_graphpattern655);
                    builtin36=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin36.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end graphpattern

    public static class constructor_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start constructor
    // ../src/tom/gom/expander/rule/Rule.g:82:1: constructor : ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( APPL ID ( labelledpattern )* ) ;
    public final constructor_return constructor() throws RecognitionException {
        constructor_return retval = new constructor_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID37=null;
        Token LPAR38=null;
        Token COMA40=null;
        Token RPAR42=null;
        labelledpattern_return labelledpattern39 = null;

        labelledpattern_return labelledpattern41 = null;


        CommonTree ID37_tree=null;
        CommonTree LPAR38_tree=null;
        CommonTree COMA40_tree=null;
        CommonTree RPAR42_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        try {
            // ../src/tom/gom/expander/rule/Rule.g:83:1: ( ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( APPL ID ( labelledpattern )* ) )
            // ../src/tom/gom/expander/rule/Rule.g:83:1: ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR
            {
            ID37=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_constructor662); 
            stream_ID.add(ID37);

            LPAR38=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_constructor664); 
            stream_LPAR.add(LPAR38);

            // ../src/tom/gom/expander/rule/Rule.g:83:9: ( labelledpattern ( COMA labelledpattern )* )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ID||(LA12_0>=INT && LA12_0<=STRING)) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../src/tom/gom/expander/rule/Rule.g:83:10: labelledpattern ( COMA labelledpattern )*
                    {
                    pushFollow(FOLLOW_labelledpattern_in_constructor667);
                    labelledpattern39=labelledpattern();
                    _fsp--;

                    stream_labelledpattern.add(labelledpattern39.getTree());
                    // ../src/tom/gom/expander/rule/Rule.g:83:26: ( COMA labelledpattern )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==COMA) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // ../src/tom/gom/expander/rule/Rule.g:83:27: COMA labelledpattern
                    	    {
                    	    COMA40=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_constructor670); 
                    	    stream_COMA.add(COMA40);

                    	    pushFollow(FOLLOW_labelledpattern_in_constructor672);
                    	    labelledpattern41=labelledpattern();
                    	    _fsp--;

                    	    stream_labelledpattern.add(labelledpattern41.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR42=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_constructor678); 
            stream_RPAR.add(RPAR42);


            // AST REWRITE
            // elements: labelledpattern, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 84:1: -> ^( APPL ID ( labelledpattern )* )
            {
                // ../src/tom/gom/expander/rule/Rule.g:84:4: ^( APPL ID ( labelledpattern )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(APPL, "APPL"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                // ../src/tom/gom/expander/rule/Rule.g:84:14: ( labelledpattern )*
                while ( stream_labelledpattern.hasNext() ) {
                    adaptor.addChild(root_1, stream_labelledpattern.next());

                }
                stream_labelledpattern.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end constructor


 

    public static final BitSet FOLLOW_rule_in_ruleset116 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_EOF_in_ruleset120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule143 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ARROW_in_rule145 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_rule147 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_IF_in_rule150 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_condition_in_rule154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_graphrule_in_graphruleset202 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_EOF_in_graphruleset206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule229 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ARROW_in_graphrule231 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_graphrule233 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_IF_in_graphrule236 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_condition_in_graphrule240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_condition289 = new BitSet(new long[]{0x0000000003F80002L});
    public static final BitSet FOLLOW_EQUALS_in_condition292 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_condition296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_condition304 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_condition308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEQ_in_condition316 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_condition320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_condition328 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_condition332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GEQ_in_condition340 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_condition344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_condition352 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_condition356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_condition364 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_ID_in_condition366 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_LPAR_in_condition368 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_condition372 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_RPAR_in_condition374 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern533 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern535 = new BitSet(new long[]{0x00000000D4000000L});
    public static final BitSet FOLLOW_term_in_pattern538 = new BitSet(new long[]{0x0000000030000000L});
    public static final BitSet FOLLOW_COMA_in_pattern541 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_term_in_pattern543 = new BitSet(new long[]{0x0000000030000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern549 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_builtin0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labelledpattern606 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_COLON_in_labelledpattern608 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_graphpattern_in_labelledpattern614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_in_graphpattern647 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_graphpattern651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_graphpattern655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constructor662 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_LPAR_in_constructor664 = new BitSet(new long[]{0x00000000D4000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor667 = new BitSet(new long[]{0x0000000030000000L});
    public static final BitSet FOLLOW_COMA_in_constructor670 = new BitSet(new long[]{0x00000000C4000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor672 = new BitSet(new long[]{0x0000000030000000L});
    public static final BitSet FOLLOW_RPAR_in_constructor678 = new BitSet(new long[]{0x0000000000000002L});

}