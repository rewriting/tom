// $ANTLR 3.0 /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g 2007-07-20 16:35:00

  package tom.gom.expander.rule;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULELIST", "RULE", "CONDRULE", "APPL", "LAB", "REF", "CONDTERM", "CONDEQUALS", "CONDNOTEQUALS", "CONDLEQ", "CONDLT", "CONDGEQ", "CONDGT", "CONDMETHOD", "ARROW", "IF", "EQUALS", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "DOT", "ID", "LPAR", "RPAR", "COMA", "INT", "STRING", "COLON", "AMPERCENT", "ESC", "WS", "SLCOMMENT"
    };
    public static final int CONDGEQ=15;
    public static final int NOTEQUALS=21;
    public static final int RPAR=29;
    public static final int CONDLT=14;
    public static final int WS=36;
    public static final int LPAR=28;
    public static final int ARROW=18;
    public static final int STRING=32;
    public static final int LT=23;
    public static final int LEQ=22;
    public static final int GT=25;
    public static final int CONDGT=16;
    public static final int RULE=5;
    public static final int RULELIST=4;
    public static final int REF=9;
    public static final int GEQ=24;
    public static final int CONDEQUALS=11;
    public static final int ESC=35;
    public static final int LAB=8;
    public static final int DOT=26;
    public static final int EQUALS=20;
    public static final int CONDTERM=10;
    public static final int SLCOMMENT=37;
    public static final int AMPERCENT=34;
    public static final int CONDNOTEQUALS=12;
    public static final int CONDMETHOD=17;
    public static final int INT=31;
    public static final int IF=19;
    public static final int CONDLEQ=13;
    public static final int EOF=-1;
    public static final int COLON=33;
    public static final int CONDRULE=6;
    public static final int APPL=7;
    public static final int COMA=30;
    public static final int ID=27;

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
    public String getGrammarFileName() { return "/home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g"; }


    public static class ruleset_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ruleset
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:31:1: ruleset : ( rule )* EOF -> ^( RULELIST ( rule )* ) ;
    public final ruleset_return ruleset() throws RecognitionException {
        ruleset_return retval = new ruleset_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token EOF2=null;
        rule_return rule1 = null;


        ASTTree EOF2_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:32:3: ( ( rule )* EOF -> ^( RULELIST ( rule )* ) )
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:32:3: ( rule )* EOF
            {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:32:3: ( rule )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:32:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset121);
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
            match(input,EOF,FOLLOW_EOF_in_ruleset125); 
            stream_EOF.add(EOF2);


            // AST REWRITE
            // elements: rule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (ASTTree)adaptor.nil();
            // 32:15: -> ^( RULELIST ( rule )* )
            {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:32:18: ^( RULELIST ( rule )* )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(RULELIST, "RULELIST"), root_1);

                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:32:29: ( rule )*
                while ( stream_rule.hasNext() ) {
                    adaptor.addChild(root_1, stream_rule.next());

                }
                stream_rule.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start rule
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:34:1: rule : pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE pattern term ) -> ^( CONDRULE pattern term $cond) ;
    public final rule_return rule() throws RecognitionException {
        rule_return retval = new rule_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ARROW4=null;
        Token IF6=null;
        condition_return cond = null;

        pattern_return pattern3 = null;

        term_return term5 = null;


        ASTTree ARROW4_tree=null;
        ASTTree IF6_tree=null;
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_pattern=new RewriteRuleSubtreeStream(adaptor,"rule pattern");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:35:3: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE pattern term ) -> ^( CONDRULE pattern term $cond) )
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:35:3: pattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_pattern_in_rule148);
            pattern3=pattern();
            _fsp--;

            stream_pattern.add(pattern3.getTree());
            ARROW4=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_rule150); 
            stream_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule152);
            term5=term();
            _fsp--;

            stream_term.add(term5.getTree());
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:35:22: ( IF cond= condition )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==IF) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:35:23: IF cond= condition
                    {
                    IF6=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_rule155); 
                    stream_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule159);
                    cond=condition();
                    _fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: cond, pattern, pattern, term, term
            // token labels: 
            // rule labels: cond, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (ASTTree)adaptor.nil();
            // 36:5: -> { cond == null }? ^( RULE pattern term )
            if ( cond == null ) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:36:26: ^( RULE pattern term )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(RULE, "RULE"), root_1);

                adaptor.addChild(root_1, stream_pattern.next());
                adaptor.addChild(root_1, stream_term.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 37:5: -> ^( CONDRULE pattern term $cond)
            {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:37:8: ^( CONDRULE pattern term $cond)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDRULE, "CONDRULE"), root_1);

                adaptor.addChild(root_1, stream_pattern.next());
                adaptor.addChild(root_1, stream_term.next());
                adaptor.addChild(root_1, stream_cond.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start graphruleset
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:1: graphruleset : ( graphrule )* EOF -> ^( RULELIST ( graphrule )* ) ;
    public final graphruleset_return graphruleset() throws RecognitionException {
        graphruleset_return retval = new graphruleset_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token EOF8=null;
        graphrule_return graphrule7 = null;


        ASTTree EOF8_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_graphrule=new RewriteRuleSubtreeStream(adaptor,"rule graphrule");
        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:40:3: ( ( graphrule )* EOF -> ^( RULELIST ( graphrule )* ) )
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:40:3: ( graphrule )* EOF
            {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:40:3: ( graphrule )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID||(LA3_0>=INT && LA3_0<=STRING)||LA3_0==AMPERCENT) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:40:4: graphrule
            	    {
            	    pushFollow(FOLLOW_graphrule_in_graphruleset207);
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
            match(input,EOF,FOLLOW_EOF_in_graphruleset211); 
            stream_EOF.add(EOF8);


            // AST REWRITE
            // elements: graphrule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (ASTTree)adaptor.nil();
            // 40:20: -> ^( RULELIST ( graphrule )* )
            {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:40:23: ^( RULELIST ( graphrule )* )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(RULELIST, "RULELIST"), root_1);

                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:40:34: ( graphrule )*
                while ( stream_graphrule.hasNext() ) {
                    adaptor.addChild(root_1, stream_graphrule.next());

                }
                stream_graphrule.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start graphrule
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:42:1: graphrule : lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( RULE $lhs $rhs) -> ^( CONDRULE $lhs $rhs $cond) ;
    public final graphrule_return graphrule() throws RecognitionException {
        graphrule_return retval = new graphrule_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ARROW9=null;
        Token IF10=null;
        labelledpattern_return lhs = null;

        labelledpattern_return rhs = null;

        condition_return cond = null;


        ASTTree ARROW9_tree=null;
        ASTTree IF10_tree=null;
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:43:3: (lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( RULE $lhs $rhs) -> ^( CONDRULE $lhs $rhs $cond) )
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:43:3: lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )?
            {
            pushFollow(FOLLOW_labelledpattern_in_graphrule236);
            lhs=labelledpattern();
            _fsp--;

            stream_labelledpattern.add(lhs.getTree());
            ARROW9=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_graphrule238); 
            stream_ARROW.add(ARROW9);

            pushFollow(FOLLOW_labelledpattern_in_graphrule242);
            rhs=labelledpattern();
            _fsp--;

            stream_labelledpattern.add(rhs.getTree());
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:43:49: ( IF cond= condition )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==IF) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:43:50: IF cond= condition
                    {
                    IF10=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_graphrule245); 
                    stream_IF.add(IF10);

                    pushFollow(FOLLOW_condition_in_graphrule249);
                    cond=condition();
                    _fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: rhs, lhs, cond, lhs, rhs
            // token labels: 
            // rule labels: lhs, cond, retval, rhs
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_lhs=new RewriteRuleSubtreeStream(adaptor,"token lhs",lhs!=null?lhs.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_rhs=new RewriteRuleSubtreeStream(adaptor,"token rhs",rhs!=null?rhs.tree:null);

            root_0 = (ASTTree)adaptor.nil();
            // 44:5: -> { cond == null }? ^( RULE $lhs $rhs)
            if ( cond == null ) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:44:26: ^( RULE $lhs $rhs)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(RULE, "RULE"), root_1);

                adaptor.addChild(root_1, stream_lhs.next());
                adaptor.addChild(root_1, stream_rhs.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 45:5: -> ^( CONDRULE $lhs $rhs $cond)
            {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:45:8: ^( CONDRULE $lhs $rhs $cond)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDRULE, "CONDRULE"), root_1);

                adaptor.addChild(root_1, stream_lhs.next());
                adaptor.addChild(root_1, stream_rhs.next());
                adaptor.addChild(root_1, stream_cond.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start condition
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:47:1: condition : p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CONDEQUALS $p1 $p2) -> {p3!=null}? ^( CONDNOTEQUALS $p1 $p3) -> {p4!=null}? ^( CONDLEQ $p1 $p4) -> {p5!=null}? ^( CONDLT $p1 $p5) -> {p6!=null}? ^( CONDGEQ $p1 $p6) -> {p7!=null}? ^( CONDGT $p1 $p7) -> {p8!=null}? ^( CONDMETHOD $p1 ID $p8) -> ^( CONDTERM $p1) ;
    public final condition_return condition() throws RecognitionException {
        condition_return retval = new condition_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token EQUALS11=null;
        Token NOTEQUALS12=null;
        Token LEQ13=null;
        Token LT14=null;
        Token GEQ15=null;
        Token GT16=null;
        Token DOT17=null;
        Token ID18=null;
        Token LPAR19=null;
        Token RPAR20=null;
        term_return p1 = null;

        term_return p2 = null;

        term_return p3 = null;

        term_return p4 = null;

        term_return p5 = null;

        term_return p6 = null;

        term_return p7 = null;

        term_return p8 = null;


        ASTTree EQUALS11_tree=null;
        ASTTree NOTEQUALS12_tree=null;
        ASTTree LEQ13_tree=null;
        ASTTree LT14_tree=null;
        ASTTree GEQ15_tree=null;
        ASTTree GT16_tree=null;
        ASTTree DOT17_tree=null;
        ASTTree ID18_tree=null;
        ASTTree LPAR19_tree=null;
        ASTTree RPAR20_tree=null;
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
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:48:3: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CONDEQUALS $p1 $p2) -> {p3!=null}? ^( CONDNOTEQUALS $p1 $p3) -> {p4!=null}? ^( CONDLEQ $p1 $p4) -> {p5!=null}? ^( CONDLT $p1 $p5) -> {p6!=null}? ^( CONDGEQ $p1 $p6) -> {p7!=null}? ^( CONDGT $p1 $p7) -> {p8!=null}? ^( CONDMETHOD $p1 ID $p8) -> ^( CONDTERM $p1) )
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:48:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
            {
            pushFollow(FOLLOW_term_in_condition302);
            p1=term();
            _fsp--;

            stream_term.add(p1.getTree());
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:48:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
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
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:48:12: EQUALS p2= term
                    {
                    EQUALS11=(Token)input.LT(1);
                    match(input,EQUALS,FOLLOW_EQUALS_in_condition305); 
                    stream_EQUALS.add(EQUALS11);

                    pushFollow(FOLLOW_term_in_condition309);
                    p2=term();
                    _fsp--;

                    stream_term.add(p2.getTree());

                    }
                    break;
                case 2 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:49:7: NOTEQUALS p3= term
                    {
                    NOTEQUALS12=(Token)input.LT(1);
                    match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_condition317); 
                    stream_NOTEQUALS.add(NOTEQUALS12);

                    pushFollow(FOLLOW_term_in_condition321);
                    p3=term();
                    _fsp--;

                    stream_term.add(p3.getTree());

                    }
                    break;
                case 3 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:50:7: LEQ p4= term
                    {
                    LEQ13=(Token)input.LT(1);
                    match(input,LEQ,FOLLOW_LEQ_in_condition329); 
                    stream_LEQ.add(LEQ13);

                    pushFollow(FOLLOW_term_in_condition333);
                    p4=term();
                    _fsp--;

                    stream_term.add(p4.getTree());

                    }
                    break;
                case 4 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:51:7: LT p5= term
                    {
                    LT14=(Token)input.LT(1);
                    match(input,LT,FOLLOW_LT_in_condition341); 
                    stream_LT.add(LT14);

                    pushFollow(FOLLOW_term_in_condition345);
                    p5=term();
                    _fsp--;

                    stream_term.add(p5.getTree());

                    }
                    break;
                case 5 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:52:7: GEQ p6= term
                    {
                    GEQ15=(Token)input.LT(1);
                    match(input,GEQ,FOLLOW_GEQ_in_condition353); 
                    stream_GEQ.add(GEQ15);

                    pushFollow(FOLLOW_term_in_condition357);
                    p6=term();
                    _fsp--;

                    stream_term.add(p6.getTree());

                    }
                    break;
                case 6 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:53:7: GT p7= term
                    {
                    GT16=(Token)input.LT(1);
                    match(input,GT,FOLLOW_GT_in_condition365); 
                    stream_GT.add(GT16);

                    pushFollow(FOLLOW_term_in_condition369);
                    p7=term();
                    _fsp--;

                    stream_term.add(p7.getTree());

                    }
                    break;
                case 7 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:54:7: DOT ID LPAR p8= term RPAR
                    {
                    DOT17=(Token)input.LT(1);
                    match(input,DOT,FOLLOW_DOT_in_condition377); 
                    stream_DOT.add(DOT17);

                    ID18=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_condition379); 
                    stream_ID.add(ID18);

                    LPAR19=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_condition381); 
                    stream_LPAR.add(LPAR19);

                    pushFollow(FOLLOW_term_in_condition385);
                    p8=term();
                    _fsp--;

                    stream_term.add(p8.getTree());
                    RPAR20=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_condition387); 
                    stream_RPAR.add(RPAR20);


                    }
                    break;

            }


            // AST REWRITE
            // elements: p1, p6, p4, p3, p5, p7, ID, p1, p1, p1, p1, p1, p1, p2, p1, p8
            // token labels: 
            // rule labels: p8, p3, p6, p1, p7, p5, p4, p2, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_p8=new RewriteRuleSubtreeStream(adaptor,"token p8",p8!=null?p8.tree:null);
            RewriteRuleSubtreeStream stream_p3=new RewriteRuleSubtreeStream(adaptor,"token p3",p3!=null?p3.tree:null);
            RewriteRuleSubtreeStream stream_p6=new RewriteRuleSubtreeStream(adaptor,"token p6",p6!=null?p6.tree:null);
            RewriteRuleSubtreeStream stream_p1=new RewriteRuleSubtreeStream(adaptor,"token p1",p1!=null?p1.tree:null);
            RewriteRuleSubtreeStream stream_p7=new RewriteRuleSubtreeStream(adaptor,"token p7",p7!=null?p7.tree:null);
            RewriteRuleSubtreeStream stream_p5=new RewriteRuleSubtreeStream(adaptor,"token p5",p5!=null?p5.tree:null);
            RewriteRuleSubtreeStream stream_p4=new RewriteRuleSubtreeStream(adaptor,"token p4",p4!=null?p4.tree:null);
            RewriteRuleSubtreeStream stream_p2=new RewriteRuleSubtreeStream(adaptor,"token p2",p2!=null?p2.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (ASTTree)adaptor.nil();
            // 56:5: -> {p2!=null}? ^( CONDEQUALS $p1 $p2)
            if (p2!=null) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:56:20: ^( CONDEQUALS $p1 $p2)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDEQUALS, "CONDEQUALS"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p2.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 57:5: -> {p3!=null}? ^( CONDNOTEQUALS $p1 $p3)
            if (p3!=null) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:57:20: ^( CONDNOTEQUALS $p1 $p3)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDNOTEQUALS, "CONDNOTEQUALS"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p3.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 58:5: -> {p4!=null}? ^( CONDLEQ $p1 $p4)
            if (p4!=null) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:58:20: ^( CONDLEQ $p1 $p4)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDLEQ, "CONDLEQ"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p4.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 59:5: -> {p5!=null}? ^( CONDLT $p1 $p5)
            if (p5!=null) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:59:20: ^( CONDLT $p1 $p5)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDLT, "CONDLT"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p5.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 60:5: -> {p6!=null}? ^( CONDGEQ $p1 $p6)
            if (p6!=null) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:60:20: ^( CONDGEQ $p1 $p6)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDGEQ, "CONDGEQ"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p6.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 61:5: -> {p7!=null}? ^( CONDGT $p1 $p7)
            if (p7!=null) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:61:20: ^( CONDGT $p1 $p7)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDGT, "CONDGT"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p7.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 62:5: -> {p8!=null}? ^( CONDMETHOD $p1 ID $p8)
            if (p8!=null) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:62:20: ^( CONDMETHOD $p1 ID $p8)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDMETHOD, "CONDMETHOD"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_ID.next());
                adaptor.addChild(root_1, stream_p8.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 63:5: -> ^( CONDTERM $p1)
            {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:63:8: ^( CONDTERM $p1)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDTERM, "CONDTERM"), root_1);

                adaptor.addChild(root_1, stream_p1.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start pattern
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:65:1: pattern : ID LPAR ( term ( COMA term )* )? RPAR -> ^( APPL ID ( term )* ) ;
    public final pattern_return pattern() throws RecognitionException {
        pattern_return retval = new pattern_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ID21=null;
        Token LPAR22=null;
        Token COMA24=null;
        Token RPAR26=null;
        term_return term23 = null;

        term_return term25 = null;


        ASTTree ID21_tree=null;
        ASTTree LPAR22_tree=null;
        ASTTree COMA24_tree=null;
        ASTTree RPAR26_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:3: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( APPL ID ( term )* ) )
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:3: ID LPAR ( term ( COMA term )* )? RPAR
            {
            ID21=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_pattern546); 
            stream_ID.add(ID21);

            LPAR22=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_pattern548); 
            stream_LPAR.add(LPAR22);

            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:11: ( term ( COMA term )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==ID||(LA7_0>=INT && LA7_0<=STRING)) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:12: term ( COMA term )*
                    {
                    pushFollow(FOLLOW_term_in_pattern551);
                    term23=term();
                    _fsp--;

                    stream_term.add(term23.getTree());
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:17: ( COMA term )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:18: COMA term
                    	    {
                    	    COMA24=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_pattern554); 
                    	    stream_COMA.add(COMA24);

                    	    pushFollow(FOLLOW_term_in_pattern556);
                    	    term25=term();
                    	    _fsp--;

                    	    stream_term.add(term25.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR26=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_pattern562); 
            stream_RPAR.add(RPAR26);


            // AST REWRITE
            // elements: term, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (ASTTree)adaptor.nil();
            // 66:37: -> ^( APPL ID ( term )* )
            {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:40: ^( APPL ID ( term )* )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(APPL, "APPL"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:66:50: ( term )*
                while ( stream_term.hasNext() ) {
                    adaptor.addChild(root_1, stream_term.next());

                }
                stream_term.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start term
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:68:1: term : ( pattern | ID | builtin );
    public final term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ID28=null;
        pattern_return pattern27 = null;

        builtin_return builtin29 = null;


        ASTTree ID28_tree=null;

        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:69:3: ( pattern | ID | builtin )
            int alt8=3;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ID) ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==LPAR) ) {
                    alt8=1;
                }
                else if ( (LA8_1==EOF||(LA8_1>=IF && LA8_1<=ID)||(LA8_1>=RPAR && LA8_1<=STRING)||LA8_1==AMPERCENT) ) {
                    alt8=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("68:1: term : ( pattern | ID | builtin );", 8, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA8_0>=INT && LA8_0<=STRING)) ) {
                alt8=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("68:1: term : ( pattern | ID | builtin );", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:69:3: pattern
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term583);
                    pattern27=pattern();
                    _fsp--;

                    adaptor.addChild(root_0, pattern27.getTree());

                    }
                    break;
                case 2 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:69:14: ID
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    ID28=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_term588); 
                    ID28_tree = (ASTTree)adaptor.create(ID28);
                    adaptor.addChild(root_0, ID28_tree);


                    }
                    break;
                case 3 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:69:19: builtin
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term592);
                    builtin29=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin29.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start builtin
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:71:1: builtin : ( INT | STRING );
    public final builtin_return builtin() throws RecognitionException {
        builtin_return retval = new builtin_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token set30=null;

        ASTTree set30_tree=null;

        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:72:1: ( INT | STRING )
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:
            {
            root_0 = (ASTTree)adaptor.nil();

            set30=(Token)input.LT(1);
            if ( (input.LA(1)>=INT && input.LA(1)<=STRING) ) {
                input.consume();
                adaptor.addChild(root_0, adaptor.create(set30));
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_builtin0);    throw mse;
            }


            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start labelledpattern
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:75:1: labelledpattern : (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LAB $namelabel $p) -> $p;
    public final labelledpattern_return labelledpattern() throws RecognitionException {
        labelledpattern_return retval = new labelledpattern_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token namelabel=null;
        Token COLON31=null;
        graphpattern_return p = null;


        ASTTree namelabel_tree=null;
        ASTTree COLON31_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_graphpattern=new RewriteRuleSubtreeStream(adaptor,"rule graphpattern");
        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:76:3: ( (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LAB $namelabel $p) -> $p)
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:76:3: (namelabel= ID COLON )? p= graphpattern
            {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:76:3: (namelabel= ID COLON )?
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
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:76:4: namelabel= ID COLON
                    {
                    namelabel=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_labelledpattern619); 
                    stream_ID.add(namelabel);

                    COLON31=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_labelledpattern621); 
                    stream_COLON.add(COLON31);


                    }
                    break;

            }

            pushFollow(FOLLOW_graphpattern_in_labelledpattern627);
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

            root_0 = (ASTTree)adaptor.nil();
            // 77:2: -> {$namelabel!=null}? ^( LAB $namelabel $p)
            if (namelabel!=null) {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:77:25: ^( LAB $namelabel $p)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(LAB, "LAB"), root_1);

                adaptor.addChild(root_1, stream_namelabel.next());
                adaptor.addChild(root_1, stream_p.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 78:2: -> $p
            {
                adaptor.addChild(root_0, stream_p.next());

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start graphpattern
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:80:1: graphpattern : ( constructor | ID | builtin | ref );
    public final graphpattern_return graphpattern() throws RecognitionException {
        graphpattern_return retval = new graphpattern_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ID33=null;
        constructor_return constructor32 = null;

        builtin_return builtin34 = null;

        ref_return ref35 = null;


        ASTTree ID33_tree=null;

        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:81:3: ( constructor | ID | builtin | ref )
            int alt10=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==LPAR) ) {
                    alt10=1;
                }
                else if ( (LA10_1==EOF||(LA10_1>=ARROW && LA10_1<=IF)||LA10_1==ID||(LA10_1>=RPAR && LA10_1<=STRING)||LA10_1==AMPERCENT) ) {
                    alt10=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("80:1: graphpattern : ( constructor | ID | builtin | ref );", 10, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT:
            case STRING:
                {
                alt10=3;
                }
                break;
            case AMPERCENT:
                {
                alt10=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("80:1: graphpattern : ( constructor | ID | builtin | ref );", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:81:3: constructor
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    pushFollow(FOLLOW_constructor_in_graphpattern658);
                    constructor32=constructor();
                    _fsp--;

                    adaptor.addChild(root_0, constructor32.getTree());

                    }
                    break;
                case 2 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:81:17: ID
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    ID33=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_graphpattern662); 
                    ID33_tree = (ASTTree)adaptor.create(ID33);
                    adaptor.addChild(root_0, ID33_tree);


                    }
                    break;
                case 3 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:81:22: builtin
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_graphpattern666);
                    builtin34=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin34.getTree());

                    }
                    break;
                case 4 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:81:32: ref
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    pushFollow(FOLLOW_ref_in_graphpattern670);
                    ref35=ref();
                    _fsp--;

                    adaptor.addChild(root_0, ref35.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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

    public static class ref_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ref
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:83:1: ref : AMPERCENT ID -> ^( REF ID ) ;
    public final ref_return ref() throws RecognitionException {
        ref_return retval = new ref_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token AMPERCENT36=null;
        Token ID37=null;

        ASTTree AMPERCENT36_tree=null;
        ASTTree ID37_tree=null;
        RewriteRuleTokenStream stream_AMPERCENT=new RewriteRuleTokenStream(adaptor,"token AMPERCENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:84:1: ( AMPERCENT ID -> ^( REF ID ) )
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:84:1: AMPERCENT ID
            {
            AMPERCENT36=(Token)input.LT(1);
            match(input,AMPERCENT,FOLLOW_AMPERCENT_in_ref678); 
            stream_AMPERCENT.add(AMPERCENT36);

            ID37=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_ref680); 
            stream_ID.add(ID37);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (ASTTree)adaptor.nil();
            // 84:14: -> ^( REF ID )
            {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:84:17: ^( REF ID )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(REF, "REF"), root_1);

                adaptor.addChild(root_1, stream_ID.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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
    // $ANTLR end ref

    public static class constructor_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start constructor
    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:86:1: constructor : ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( APPL ID ( labelledpattern )* ) ;
    public final constructor_return constructor() throws RecognitionException {
        constructor_return retval = new constructor_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ID38=null;
        Token LPAR39=null;
        Token COMA41=null;
        Token RPAR43=null;
        labelledpattern_return labelledpattern40 = null;

        labelledpattern_return labelledpattern42 = null;


        ASTTree ID38_tree=null;
        ASTTree LPAR39_tree=null;
        ASTTree COMA41_tree=null;
        ASTTree RPAR43_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        try {
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:87:1: ( ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( APPL ID ( labelledpattern )* ) )
            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:87:1: ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR
            {
            ID38=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_constructor696); 
            stream_ID.add(ID38);

            LPAR39=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_constructor698); 
            stream_LPAR.add(LPAR39);

            // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:87:9: ( labelledpattern ( COMA labelledpattern )* )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ID||(LA12_0>=INT && LA12_0<=STRING)||LA12_0==AMPERCENT) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:87:10: labelledpattern ( COMA labelledpattern )*
                    {
                    pushFollow(FOLLOW_labelledpattern_in_constructor701);
                    labelledpattern40=labelledpattern();
                    _fsp--;

                    stream_labelledpattern.add(labelledpattern40.getTree());
                    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:87:26: ( COMA labelledpattern )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==COMA) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:87:27: COMA labelledpattern
                    	    {
                    	    COMA41=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_constructor704); 
                    	    stream_COMA.add(COMA41);

                    	    pushFollow(FOLLOW_labelledpattern_in_constructor706);
                    	    labelledpattern42=labelledpattern();
                    	    _fsp--;

                    	    stream_labelledpattern.add(labelledpattern42.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR43=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_constructor712); 
            stream_RPAR.add(RPAR43);


            // AST REWRITE
            // elements: ID, labelledpattern
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (ASTTree)adaptor.nil();
            // 88:1: -> ^( APPL ID ( labelledpattern )* )
            {
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:88:4: ^( APPL ID ( labelledpattern )* )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(APPL, "APPL"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                // /home/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:88:14: ( labelledpattern )*
                while ( stream_labelledpattern.hasNext() ) {
                    adaptor.addChild(root_1, stream_labelledpattern.next());

                }
                stream_labelledpattern.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
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


 

    public static final BitSet FOLLOW_rule_in_ruleset121 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_EOF_in_ruleset125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule148 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_ARROW_in_rule150 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_term_in_rule152 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_IF_in_rule155 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_condition_in_rule159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_graphrule_in_graphruleset207 = new BitSet(new long[]{0x0000000588000000L});
    public static final BitSet FOLLOW_EOF_in_graphruleset211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule236 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_ARROW_in_graphrule238 = new BitSet(new long[]{0x0000000588000000L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule242 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_IF_in_graphrule245 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_condition_in_graphrule249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_condition302 = new BitSet(new long[]{0x0000000007F00002L});
    public static final BitSet FOLLOW_EQUALS_in_condition305 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_term_in_condition309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_condition317 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_term_in_condition321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEQ_in_condition329 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_term_in_condition333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_condition341 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_term_in_condition345 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GEQ_in_condition353 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_term_in_condition357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_condition365 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_term_in_condition369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_condition377 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_ID_in_condition379 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LPAR_in_condition381 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_term_in_condition385 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_RPAR_in_condition387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern546 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern548 = new BitSet(new long[]{0x00000001A8000000L});
    public static final BitSet FOLLOW_term_in_pattern551 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_COMA_in_pattern554 = new BitSet(new long[]{0x0000000188000000L});
    public static final BitSet FOLLOW_term_in_pattern556 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_builtin0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labelledpattern619 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_COLON_in_labelledpattern621 = new BitSet(new long[]{0x0000000588000000L});
    public static final BitSet FOLLOW_graphpattern_in_labelledpattern627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_in_graphpattern658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_graphpattern662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_graphpattern666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ref_in_graphpattern670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPERCENT_in_ref678 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_ID_in_ref680 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constructor696 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LPAR_in_constructor698 = new BitSet(new long[]{0x00000005A8000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor701 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_COMA_in_constructor704 = new BitSet(new long[]{0x0000000588000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor706 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_RPAR_in_constructor712 = new BitSet(new long[]{0x0000000000000002L});

}