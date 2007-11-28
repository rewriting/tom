// $ANTLR 3.0 /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g 2007-11-26 16:00:51

  package tom.gom.expander.rule;
  import tom.gom.adt.rule.RuleTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BuiltinInt", "CondEquals", "BuiltinString", "UnnamedVarStar", "CondLessEquals", "CondMethod", "RefTerm", "LabTerm", "CondTerm", "ConditionalRule", "Rule", "TermList", "CondNotEquals", "PathTerm", "CondGreaterThan", "CondLessThan", "RuleList", "UnnamedVar", "CondGreaterEquals", "Var", "Appl", "At", "ARROW", "IF", "EQUALS", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "DOT", "ID", "LPAR", "RPAR", "COMA", "AT", "UNDERSCORE", "UNDERSCORESTAR", "INT", "STRING", "COLON", "AMPERCENT", "ESC", "WS", "SLCOMMENT"
    };
    public static final int CondEquals=5;
    public static final int RefTerm=10;
    public static final int LPAR=36;
    public static final int ARROW=26;
    public static final int LEQ=30;
    public static final int GEQ=32;
    public static final int RuleList=20;
    public static final int DOT=34;
    public static final int BuiltinInt=4;
    public static final int BuiltinString=6;
    public static final int SLCOMMENT=48;
    public static final int CondLessEquals=8;
    public static final int INT=42;
    public static final int CondLessThan=19;
    public static final int UnnamedVar=21;
    public static final int CondGreaterEquals=22;
    public static final int Var=23;
    public static final int AT=39;
    public static final int At=25;
    public static final int ID=35;
    public static final int NOTEQUALS=29;
    public static final int UnnamedVarStar=7;
    public static final int CondMethod=9;
    public static final int RPAR=37;
    public static final int LabTerm=11;
    public static final int WS=47;
    public static final int STRING=43;
    public static final int LT=31;
    public static final int ConditionalRule=13;
    public static final int GT=33;
    public static final int Rule=14;
    public static final int ESC=46;
    public static final int Appl=24;
    public static final int EQUALS=28;
    public static final int AMPERCENT=45;
    public static final int CondTerm=12;
    public static final int IF=27;
    public static final int EOF=-1;
    public static final int COLON=44;
    public static final int TermList=15;
    public static final int CondNotEquals=16;
    public static final int CondGreaterThan=18;
    public static final int PathTerm=17;
    public static final int COMA=38;
    public static final int UNDERSCORESTAR=41;
    public static final int UNDERSCORE=40;

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
    public String getGrammarFileName() { return "/home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g"; }


    public static class ruleset_return extends ParserRuleReturnScope {
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ruleset
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:20:1: ruleset : ( rule )* EOF -> ^( RuleList ( rule )* ) ;
    public final ruleset_return ruleset() throws RecognitionException {
        ruleset_return retval = new ruleset_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token EOF2=null;
        rule_return rule1 = null;


        RuleTree EOF2_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:3: ( ( rule )* EOF -> ^( RuleList ( rule )* ) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:3: ( rule )* EOF
            {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:3: ( rule )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID||(LA1_0>=UNDERSCORE && LA1_0<=UNDERSCORESTAR)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset122);
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
            match(input,EOF,FOLLOW_EOF_in_ruleset126); 
            stream_EOF.add(EOF2);


            // AST REWRITE
            // elements: rule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 21:15: -> ^( RuleList ( rule )* )
            {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:18: ^( RuleList ( rule )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(RuleList, "RuleList"), root_1);

                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:29: ( rule )*
                while ( stream_rule.hasNext() ) {
                    adaptor.addChild(root_1, stream_rule.next());

                }
                stream_rule.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start rule
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:23:1: rule : pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) ;
    public final rule_return rule() throws RecognitionException {
        rule_return retval = new rule_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ARROW4=null;
        Token IF6=null;
        condition_return cond = null;

        pattern_return pattern3 = null;

        term_return term5 = null;


        RuleTree ARROW4_tree=null;
        RuleTree IF6_tree=null;
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_pattern=new RewriteRuleSubtreeStream(adaptor,"rule pattern");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:3: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:3: pattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_pattern_in_rule149);
            pattern3=pattern();
            _fsp--;

            stream_pattern.add(pattern3.getTree());
            ARROW4=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_rule151); 
            stream_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule153);
            term5=term();
            _fsp--;

            stream_term.add(term5.getTree());
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:22: ( IF cond= condition )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==IF) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:23: IF cond= condition
                    {
                    IF6=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_rule156); 
                    stream_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule160);
                    cond=condition();
                    _fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: term, pattern, term, cond, pattern
            // token labels: 
            // rule labels: cond, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 25:5: -> { cond == null }? ^( Rule pattern term )
            if ( cond == null ) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:25:26: ^( Rule pattern term )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Rule, "Rule"), root_1);

                adaptor.addChild(root_1, stream_pattern.next());
                adaptor.addChild(root_1, stream_term.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 26:5: -> ^( ConditionalRule pattern term $cond)
            {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:26:8: ^( ConditionalRule pattern term $cond)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(ConditionalRule, "ConditionalRule"), root_1);

                adaptor.addChild(root_1, stream_pattern.next());
                adaptor.addChild(root_1, stream_term.next());
                adaptor.addChild(root_1, stream_cond.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start graphruleset
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:28:1: graphruleset : ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) ;
    public final graphruleset_return graphruleset() throws RecognitionException {
        graphruleset_return retval = new graphruleset_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token EOF8=null;
        graphrule_return graphrule7 = null;


        RuleTree EOF8_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_graphrule=new RewriteRuleSubtreeStream(adaptor,"rule graphrule");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:3: ( ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:3: ( graphrule )* EOF
            {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:3: ( graphrule )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID||(LA3_0>=INT && LA3_0<=STRING)||LA3_0==AMPERCENT) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:4: graphrule
            	    {
            	    pushFollow(FOLLOW_graphrule_in_graphruleset208);
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
            match(input,EOF,FOLLOW_EOF_in_graphruleset212); 
            stream_EOF.add(EOF8);


            // AST REWRITE
            // elements: graphrule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 29:20: -> ^( RuleList ( graphrule )* )
            {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:23: ^( RuleList ( graphrule )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(RuleList, "RuleList"), root_1);

                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:34: ( graphrule )*
                while ( stream_graphrule.hasNext() ) {
                    adaptor.addChild(root_1, stream_graphrule.next());

                }
                stream_graphrule.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start graphrule
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:31:1: graphrule : lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) ;
    public final graphrule_return graphrule() throws RecognitionException {
        graphrule_return retval = new graphrule_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ARROW9=null;
        Token IF10=null;
        labelledpattern_return lhs = null;

        labelledpattern_return rhs = null;

        condition_return cond = null;


        RuleTree ARROW9_tree=null;
        RuleTree IF10_tree=null;
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:3: (lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:3: lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )?
            {
            pushFollow(FOLLOW_labelledpattern_in_graphrule237);
            lhs=labelledpattern();
            _fsp--;

            stream_labelledpattern.add(lhs.getTree());
            ARROW9=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_graphrule239); 
            stream_ARROW.add(ARROW9);

            pushFollow(FOLLOW_labelledpattern_in_graphrule243);
            rhs=labelledpattern();
            _fsp--;

            stream_labelledpattern.add(rhs.getTree());
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:49: ( IF cond= condition )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==IF) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:50: IF cond= condition
                    {
                    IF10=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_graphrule246); 
                    stream_IF.add(IF10);

                    pushFollow(FOLLOW_condition_in_graphrule250);
                    cond=condition();
                    _fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: lhs, rhs, rhs, lhs, cond
            // token labels: 
            // rule labels: lhs, cond, retval, rhs
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_lhs=new RewriteRuleSubtreeStream(adaptor,"token lhs",lhs!=null?lhs.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_rhs=new RewriteRuleSubtreeStream(adaptor,"token rhs",rhs!=null?rhs.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 33:5: -> { cond == null }? ^( Rule $lhs $rhs)
            if ( cond == null ) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:33:26: ^( Rule $lhs $rhs)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Rule, "Rule"), root_1);

                adaptor.addChild(root_1, stream_lhs.next());
                adaptor.addChild(root_1, stream_rhs.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 34:5: -> ^( ConditionalRule $lhs $rhs $cond)
            {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:34:8: ^( ConditionalRule $lhs $rhs $cond)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(ConditionalRule, "ConditionalRule"), root_1);

                adaptor.addChild(root_1, stream_lhs.next());
                adaptor.addChild(root_1, stream_rhs.next());
                adaptor.addChild(root_1, stream_cond.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start condition
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:36:1: condition : p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> {p8!=null}? ^( CondMethod $p1 ID $p8) -> ^( CondTerm $p1) ;
    public final condition_return condition() throws RecognitionException {
        condition_return retval = new condition_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

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


        RuleTree EQUALS11_tree=null;
        RuleTree NOTEQUALS12_tree=null;
        RuleTree LEQ13_tree=null;
        RuleTree LT14_tree=null;
        RuleTree GEQ15_tree=null;
        RuleTree GT16_tree=null;
        RuleTree DOT17_tree=null;
        RuleTree ID18_tree=null;
        RuleTree LPAR19_tree=null;
        RuleTree RPAR20_tree=null;
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
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:3: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> {p8!=null}? ^( CondMethod $p1 ID $p8) -> ^( CondTerm $p1) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
            {
            pushFollow(FOLLOW_term_in_condition303);
            p1=term();
            _fsp--;

            stream_term.add(p1.getTree());
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
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
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:12: EQUALS p2= term
                    {
                    EQUALS11=(Token)input.LT(1);
                    match(input,EQUALS,FOLLOW_EQUALS_in_condition306); 
                    stream_EQUALS.add(EQUALS11);

                    pushFollow(FOLLOW_term_in_condition310);
                    p2=term();
                    _fsp--;

                    stream_term.add(p2.getTree());

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:38:7: NOTEQUALS p3= term
                    {
                    NOTEQUALS12=(Token)input.LT(1);
                    match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_condition318); 
                    stream_NOTEQUALS.add(NOTEQUALS12);

                    pushFollow(FOLLOW_term_in_condition322);
                    p3=term();
                    _fsp--;

                    stream_term.add(p3.getTree());

                    }
                    break;
                case 3 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:39:7: LEQ p4= term
                    {
                    LEQ13=(Token)input.LT(1);
                    match(input,LEQ,FOLLOW_LEQ_in_condition330); 
                    stream_LEQ.add(LEQ13);

                    pushFollow(FOLLOW_term_in_condition334);
                    p4=term();
                    _fsp--;

                    stream_term.add(p4.getTree());

                    }
                    break;
                case 4 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:40:7: LT p5= term
                    {
                    LT14=(Token)input.LT(1);
                    match(input,LT,FOLLOW_LT_in_condition342); 
                    stream_LT.add(LT14);

                    pushFollow(FOLLOW_term_in_condition346);
                    p5=term();
                    _fsp--;

                    stream_term.add(p5.getTree());

                    }
                    break;
                case 5 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:41:7: GEQ p6= term
                    {
                    GEQ15=(Token)input.LT(1);
                    match(input,GEQ,FOLLOW_GEQ_in_condition354); 
                    stream_GEQ.add(GEQ15);

                    pushFollow(FOLLOW_term_in_condition358);
                    p6=term();
                    _fsp--;

                    stream_term.add(p6.getTree());

                    }
                    break;
                case 6 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:7: GT p7= term
                    {
                    GT16=(Token)input.LT(1);
                    match(input,GT,FOLLOW_GT_in_condition366); 
                    stream_GT.add(GT16);

                    pushFollow(FOLLOW_term_in_condition370);
                    p7=term();
                    _fsp--;

                    stream_term.add(p7.getTree());

                    }
                    break;
                case 7 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:43:7: DOT ID LPAR p8= term RPAR
                    {
                    DOT17=(Token)input.LT(1);
                    match(input,DOT,FOLLOW_DOT_in_condition378); 
                    stream_DOT.add(DOT17);

                    ID18=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_condition380); 
                    stream_ID.add(ID18);

                    LPAR19=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_condition382); 
                    stream_LPAR.add(LPAR19);

                    pushFollow(FOLLOW_term_in_condition386);
                    p8=term();
                    _fsp--;

                    stream_term.add(p8.getTree());
                    RPAR20=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_condition388); 
                    stream_RPAR.add(RPAR20);


                    }
                    break;

            }


            // AST REWRITE
            // elements: ID, p1, p1, p1, p1, p1, p8, p2, p5, p7, p3, p4, p6, p1, p1, p1
            // token labels: 
            // rule labels: p6, p3, p8, p1, p4, p7, p5, p2, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_p6=new RewriteRuleSubtreeStream(adaptor,"token p6",p6!=null?p6.tree:null);
            RewriteRuleSubtreeStream stream_p3=new RewriteRuleSubtreeStream(adaptor,"token p3",p3!=null?p3.tree:null);
            RewriteRuleSubtreeStream stream_p8=new RewriteRuleSubtreeStream(adaptor,"token p8",p8!=null?p8.tree:null);
            RewriteRuleSubtreeStream stream_p1=new RewriteRuleSubtreeStream(adaptor,"token p1",p1!=null?p1.tree:null);
            RewriteRuleSubtreeStream stream_p4=new RewriteRuleSubtreeStream(adaptor,"token p4",p4!=null?p4.tree:null);
            RewriteRuleSubtreeStream stream_p7=new RewriteRuleSubtreeStream(adaptor,"token p7",p7!=null?p7.tree:null);
            RewriteRuleSubtreeStream stream_p5=new RewriteRuleSubtreeStream(adaptor,"token p5",p5!=null?p5.tree:null);
            RewriteRuleSubtreeStream stream_p2=new RewriteRuleSubtreeStream(adaptor,"token p2",p2!=null?p2.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 45:5: -> {p2!=null}? ^( CondEquals $p1 $p2)
            if (p2!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:45:20: ^( CondEquals $p1 $p2)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondEquals, "CondEquals"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p2.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 46:5: -> {p3!=null}? ^( CondNotEquals $p1 $p3)
            if (p3!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:46:20: ^( CondNotEquals $p1 $p3)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondNotEquals, "CondNotEquals"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p3.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 47:5: -> {p4!=null}? ^( CondLessEquals $p1 $p4)
            if (p4!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:20: ^( CondLessEquals $p1 $p4)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondLessEquals, "CondLessEquals"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p4.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 48:5: -> {p5!=null}? ^( CondLessThan $p1 $p5)
            if (p5!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:48:20: ^( CondLessThan $p1 $p5)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondLessThan, "CondLessThan"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p5.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 49:5: -> {p6!=null}? ^( CondGreaterEquals $p1 $p6)
            if (p6!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:49:20: ^( CondGreaterEquals $p1 $p6)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondGreaterEquals, "CondGreaterEquals"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p6.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 50:5: -> {p7!=null}? ^( CondGreaterThan $p1 $p7)
            if (p7!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:50:20: ^( CondGreaterThan $p1 $p7)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondGreaterThan, "CondGreaterThan"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_p7.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 51:5: -> {p8!=null}? ^( CondMethod $p1 ID $p8)
            if (p8!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:51:20: ^( CondMethod $p1 ID $p8)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondMethod, "CondMethod"), root_1);

                adaptor.addChild(root_1, stream_p1.next());
                adaptor.addChild(root_1, stream_ID.next());
                adaptor.addChild(root_1, stream_p8.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 52:5: -> ^( CondTerm $p1)
            {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:52:8: ^( CondTerm $p1)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondTerm, "CondTerm"), root_1);

                adaptor.addChild(root_1, stream_p1.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start pattern
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:54:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORESTAR -> ^( UnnamedVarStar ) );
    public final pattern_return pattern() throws RecognitionException {
        pattern_return retval = new pattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token varname=null;
        Token funname=null;
        Token ID21=null;
        Token LPAR22=null;
        Token COMA24=null;
        Token RPAR26=null;
        Token AT27=null;
        Token LPAR28=null;
        Token COMA30=null;
        Token RPAR32=null;
        Token UNDERSCORE33=null;
        Token UNDERSCORESTAR34=null;
        term_return term23 = null;

        term_return term25 = null;

        term_return term29 = null;

        term_return term31 = null;


        RuleTree varname_tree=null;
        RuleTree funname_tree=null;
        RuleTree ID21_tree=null;
        RuleTree LPAR22_tree=null;
        RuleTree COMA24_tree=null;
        RuleTree RPAR26_tree=null;
        RuleTree AT27_tree=null;
        RuleTree LPAR28_tree=null;
        RuleTree COMA30_tree=null;
        RuleTree RPAR32_tree=null;
        RuleTree UNDERSCORE33_tree=null;
        RuleTree UNDERSCORESTAR34_tree=null;
        RewriteRuleTokenStream stream_UNDERSCORESTAR=new RewriteRuleTokenStream(adaptor,"token UNDERSCORESTAR");
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_UNDERSCORE=new RewriteRuleTokenStream(adaptor,"token UNDERSCORE");
        RewriteRuleTokenStream stream_AT=new RewriteRuleTokenStream(adaptor,"token AT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:3: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORESTAR -> ^( UnnamedVarStar ) )
            int alt10=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==AT) ) {
                    alt10=2;
                }
                else if ( (LA10_1==LPAR) ) {
                    alt10=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("54:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORESTAR -> ^( UnnamedVarStar ) );", 10, 1, input);

                    throw nvae;
                }
                }
                break;
            case UNDERSCORE:
                {
                alt10=3;
                }
                break;
            case UNDERSCORESTAR:
                {
                alt10=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("54:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORESTAR -> ^( UnnamedVarStar ) );", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:3: ID LPAR ( term ( COMA term )* )? RPAR
                    {
                    ID21=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_pattern547); 
                    stream_ID.add(ID21);

                    LPAR22=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_pattern549); 
                    stream_LPAR.add(LPAR22);

                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:11: ( term ( COMA term )* )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==ID||(LA7_0>=UNDERSCORE && LA7_0<=STRING)) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:12: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern552);
                            term23=term();
                            _fsp--;

                            stream_term.add(term23.getTree());
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:17: ( COMA term )*
                            loop6:
                            do {
                                int alt6=2;
                                int LA6_0 = input.LA(1);

                                if ( (LA6_0==COMA) ) {
                                    alt6=1;
                                }


                                switch (alt6) {
                            	case 1 :
                            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:18: COMA term
                            	    {
                            	    COMA24=(Token)input.LT(1);
                            	    match(input,COMA,FOLLOW_COMA_in_pattern555); 
                            	    stream_COMA.add(COMA24);

                            	    pushFollow(FOLLOW_term_in_pattern557);
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
                    match(input,RPAR,FOLLOW_RPAR_in_pattern563); 
                    stream_RPAR.add(RPAR26);


                    // AST REWRITE
                    // elements: ID, term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 55:37: -> ^( Appl ID ^( TermList ( term )* ) )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:40: ^( Appl ID ^( TermList ( term )* ) )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:50: ^( TermList ( term )* )
                        {
                        RuleTree root_2 = (RuleTree)adaptor.nil();
                        root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_2);

                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:61: ( term )*
                        while ( stream_term.hasNext() ) {
                            adaptor.addChild(root_2, stream_term.next());

                        }
                        stream_term.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:5: (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR
                    {
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:5: (varname= ID )
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:6: varname= ID
                    {
                    varname=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_pattern587); 
                    stream_ID.add(varname);


                    }

                    AT27=(Token)input.LT(1);
                    match(input,AT,FOLLOW_AT_in_pattern590); 
                    stream_AT.add(AT27);

                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:21: (funname= ID )
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:22: funname= ID
                    {
                    funname=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_pattern595); 
                    stream_ID.add(funname);


                    }

                    LPAR28=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_pattern598); 
                    stream_LPAR.add(LPAR28);

                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:39: ( term ( COMA term )* )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==ID||(LA9_0>=UNDERSCORE && LA9_0<=STRING)) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:40: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern601);
                            term29=term();
                            _fsp--;

                            stream_term.add(term29.getTree());
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:45: ( COMA term )*
                            loop8:
                            do {
                                int alt8=2;
                                int LA8_0 = input.LA(1);

                                if ( (LA8_0==COMA) ) {
                                    alt8=1;
                                }


                                switch (alt8) {
                            	case 1 :
                            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:46: COMA term
                            	    {
                            	    COMA30=(Token)input.LT(1);
                            	    match(input,COMA,FOLLOW_COMA_in_pattern604); 
                            	    stream_COMA.add(COMA30);

                            	    pushFollow(FOLLOW_term_in_pattern606);
                            	    term31=term();
                            	    _fsp--;

                            	    stream_term.add(term31.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop8;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR32=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_pattern612); 
                    stream_RPAR.add(RPAR32);


                    // AST REWRITE
                    // elements: varname, funname, term
                    // token labels: varname, funname
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_varname=new RewriteRuleTokenStream(adaptor,"token varname",varname);
                    RewriteRuleTokenStream stream_funname=new RewriteRuleTokenStream(adaptor,"token funname",funname);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 56:65: -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:68: ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(At, "At"), root_1);

                        adaptor.addChild(root_1, stream_varname.next());
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:82: ^( Appl $funname ^( TermList ( term )* ) )
                        {
                        RuleTree root_2 = (RuleTree)adaptor.nil();
                        root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_2);

                        adaptor.addChild(root_2, stream_funname.next());
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:98: ^( TermList ( term )* )
                        {
                        RuleTree root_3 = (RuleTree)adaptor.nil();
                        root_3 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_3);

                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:109: ( term )*
                        while ( stream_term.hasNext() ) {
                            adaptor.addChild(root_3, stream_term.next());

                        }
                        stream_term.reset();

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 3 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:57:5: UNDERSCORE
                    {
                    UNDERSCORE33=(Token)input.LT(1);
                    match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_pattern641); 
                    stream_UNDERSCORE.add(UNDERSCORE33);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 57:16: -> ^( UnnamedVar )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:57:19: ^( UnnamedVar )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(UnnamedVar, "UnnamedVar"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 4 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:58:5: UNDERSCORESTAR
                    {
                    UNDERSCORESTAR34=(Token)input.LT(1);
                    match(input,UNDERSCORESTAR,FOLLOW_UNDERSCORESTAR_in_pattern653); 
                    stream_UNDERSCORESTAR.add(UNDERSCORESTAR34);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 58:20: -> ^( UnnamedVarStar )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:58:23: ^( UnnamedVarStar )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(UnnamedVarStar, "UnnamedVarStar"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start term
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:60:1: term : ( pattern | ID -> ^( Var ID ) | builtin );
    public final term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ID36=null;
        pattern_return pattern35 = null;

        builtin_return builtin37 = null;


        RuleTree ID36_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:61:3: ( pattern | ID -> ^( Var ID ) | builtin )
            int alt11=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==LPAR||LA11_1==AT) ) {
                    alt11=1;
                }
                else if ( (LA11_1==EOF||(LA11_1>=IF && LA11_1<=ID)||(LA11_1>=RPAR && LA11_1<=COMA)||(LA11_1>=UNDERSCORE && LA11_1<=STRING)||LA11_1==AMPERCENT) ) {
                    alt11=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("60:1: term : ( pattern | ID -> ^( Var ID ) | builtin );", 11, 1, input);

                    throw nvae;
                }
                }
                break;
            case UNDERSCORE:
            case UNDERSCORESTAR:
                {
                alt11=1;
                }
                break;
            case INT:
            case STRING:
                {
                alt11=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("60:1: term : ( pattern | ID -> ^( Var ID ) | builtin );", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:61:3: pattern
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term669);
                    pattern35=pattern();
                    _fsp--;

                    adaptor.addChild(root_0, pattern35.getTree());

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:62:4: ID
                    {
                    ID36=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_term674); 
                    stream_ID.add(ID36);


                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 62:7: -> ^( Var ID )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:62:10: ^( Var ID )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Var, "Var"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 3 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:63:4: builtin
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term687);
                    builtin37=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin37.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start builtin
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );
    public final builtin_return builtin() throws RecognitionException {
        builtin_return retval = new builtin_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token INT38=null;
        Token STRING39=null;

        RuleTree INT38_tree=null;
        RuleTree STRING39_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:3: ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==INT) ) {
                alt12=1;
            }
            else if ( (LA12_0==STRING) ) {
                alt12=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("65:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:3: INT
                    {
                    INT38=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_builtin697); 
                    stream_INT.add(INT38);


                    // AST REWRITE
                    // elements: INT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 66:7: -> ^( BuiltinInt INT )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:10: ^( BuiltinInt INT )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(BuiltinInt, "BuiltinInt"), root_1);

                        adaptor.addChild(root_1, stream_INT.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:5: STRING
                    {
                    STRING39=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_builtin711); 
                    stream_STRING.add(STRING39);


                    // AST REWRITE
                    // elements: STRING
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 67:12: -> ^( BuiltinString STRING )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:15: ^( BuiltinString STRING )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(BuiltinString, "BuiltinString"), root_1);

                        adaptor.addChild(root_1, stream_STRING.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start labelledpattern
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:70:1: labelledpattern : (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p;
    public final labelledpattern_return labelledpattern() throws RecognitionException {
        labelledpattern_return retval = new labelledpattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token namelabel=null;
        Token COLON40=null;
        graphpattern_return p = null;


        RuleTree namelabel_tree=null;
        RuleTree COLON40_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_graphpattern=new RewriteRuleSubtreeStream(adaptor,"rule graphpattern");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:71:3: ( (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p)
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:71:3: (namelabel= ID COLON )? p= graphpattern
            {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:71:3: (namelabel= ID COLON )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==ID) ) {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==COLON) ) {
                    alt13=1;
                }
            }
            switch (alt13) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:71:4: namelabel= ID COLON
                    {
                    namelabel=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_labelledpattern733); 
                    stream_ID.add(namelabel);

                    COLON40=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_labelledpattern735); 
                    stream_COLON.add(COLON40);


                    }
                    break;

            }

            pushFollow(FOLLOW_graphpattern_in_labelledpattern741);
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

            root_0 = (RuleTree)adaptor.nil();
            // 72:3: -> {$namelabel!=null}? ^( LabTerm $namelabel $p)
            if (namelabel!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:26: ^( LabTerm $namelabel $p)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(LabTerm, "LabTerm"), root_1);

                adaptor.addChild(root_1, stream_namelabel.next());
                adaptor.addChild(root_1, stream_p.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 73:3: -> $p
            {
                adaptor.addChild(root_0, stream_p.next());

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start graphpattern
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:75:1: graphpattern : ( constructor | ID -> ^( Var ID ) | builtin | ref );
    public final graphpattern_return graphpattern() throws RecognitionException {
        graphpattern_return retval = new graphpattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ID42=null;
        constructor_return constructor41 = null;

        builtin_return builtin43 = null;

        ref_return ref44 = null;


        RuleTree ID42_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:76:3: ( constructor | ID -> ^( Var ID ) | builtin | ref )
            int alt14=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA14_1 = input.LA(2);

                if ( (LA14_1==LPAR) ) {
                    alt14=1;
                }
                else if ( (LA14_1==EOF||(LA14_1>=ARROW && LA14_1<=IF)||LA14_1==ID||(LA14_1>=RPAR && LA14_1<=COMA)||(LA14_1>=INT && LA14_1<=STRING)||LA14_1==AMPERCENT) ) {
                    alt14=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("75:1: graphpattern : ( constructor | ID -> ^( Var ID ) | builtin | ref );", 14, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT:
            case STRING:
                {
                alt14=3;
                }
                break;
            case AMPERCENT:
                {
                alt14=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("75:1: graphpattern : ( constructor | ID -> ^( Var ID ) | builtin | ref );", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:76:3: constructor
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_constructor_in_graphpattern774);
                    constructor41=constructor();
                    _fsp--;

                    adaptor.addChild(root_0, constructor41.getTree());

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:77:5: ID
                    {
                    ID42=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_graphpattern780); 
                    stream_ID.add(ID42);


                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 77:8: -> ^( Var ID )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:77:11: ^( Var ID )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Var, "Var"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 3 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:78:5: builtin
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_graphpattern794);
                    builtin43=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin43.getTree());

                    }
                    break;
                case 4 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:79:5: ref
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_ref_in_graphpattern800);
                    ref44=ref();
                    _fsp--;

                    adaptor.addChild(root_0, ref44.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ref
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:81:1: ref : AMPERCENT ID -> ^( RefTerm ID ) ;
    public final ref_return ref() throws RecognitionException {
        ref_return retval = new ref_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token AMPERCENT45=null;
        Token ID46=null;

        RuleTree AMPERCENT45_tree=null;
        RuleTree ID46_tree=null;
        RewriteRuleTokenStream stream_AMPERCENT=new RewriteRuleTokenStream(adaptor,"token AMPERCENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:3: ( AMPERCENT ID -> ^( RefTerm ID ) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:3: AMPERCENT ID
            {
            AMPERCENT45=(Token)input.LT(1);
            match(input,AMPERCENT,FOLLOW_AMPERCENT_in_ref810); 
            stream_AMPERCENT.add(AMPERCENT45);

            ID46=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_ref812); 
            stream_ID.add(ID46);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 82:16: -> ^( RefTerm ID )
            {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:19: ^( RefTerm ID )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(RefTerm, "RefTerm"), root_1);

                adaptor.addChild(root_1, stream_ID.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start constructor
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:84:1: constructor : ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) ;
    public final constructor_return constructor() throws RecognitionException {
        constructor_return retval = new constructor_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ID47=null;
        Token LPAR48=null;
        Token COMA50=null;
        Token RPAR52=null;
        labelledpattern_return labelledpattern49 = null;

        labelledpattern_return labelledpattern51 = null;


        RuleTree ID47_tree=null;
        RuleTree LPAR48_tree=null;
        RuleTree COMA50_tree=null;
        RuleTree RPAR52_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:85:3: ( ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:85:3: ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR
            {
            ID47=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_constructor830); 
            stream_ID.add(ID47);

            LPAR48=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_constructor832); 
            stream_LPAR.add(LPAR48);

            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:85:11: ( labelledpattern ( COMA labelledpattern )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==ID||(LA16_0>=INT && LA16_0<=STRING)||LA16_0==AMPERCENT) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:85:12: labelledpattern ( COMA labelledpattern )*
                    {
                    pushFollow(FOLLOW_labelledpattern_in_constructor835);
                    labelledpattern49=labelledpattern();
                    _fsp--;

                    stream_labelledpattern.add(labelledpattern49.getTree());
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:85:28: ( COMA labelledpattern )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==COMA) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:85:29: COMA labelledpattern
                    	    {
                    	    COMA50=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_constructor838); 
                    	    stream_COMA.add(COMA50);

                    	    pushFollow(FOLLOW_labelledpattern_in_constructor840);
                    	    labelledpattern51=labelledpattern();
                    	    _fsp--;

                    	    stream_labelledpattern.add(labelledpattern51.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR52=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_constructor846); 
            stream_RPAR.add(RPAR52);


            // AST REWRITE
            // elements: labelledpattern, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 86:3: -> ^( Appl ID ^( TermList ( labelledpattern )* ) )
            {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:86:6: ^( Appl ID ^( TermList ( labelledpattern )* ) )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:86:16: ^( TermList ( labelledpattern )* )
                {
                RuleTree root_2 = (RuleTree)adaptor.nil();
                root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_2);

                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:86:27: ( labelledpattern )*
                while ( stream_labelledpattern.hasNext() ) {
                    adaptor.addChild(root_2, stream_labelledpattern.next());

                }
                stream_labelledpattern.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (RuleTree)adaptor.rulePostProcessing(root_0);
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


 

    public static final BitSet FOLLOW_rule_in_ruleset122 = new BitSet(new long[]{0x0000030800000000L});
    public static final BitSet FOLLOW_EOF_in_ruleset126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule149 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_ARROW_in_rule151 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_rule153 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_IF_in_rule156 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_condition_in_rule160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_graphrule_in_graphruleset208 = new BitSet(new long[]{0x00002C0800000000L});
    public static final BitSet FOLLOW_EOF_in_graphruleset212 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule237 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_ARROW_in_graphrule239 = new BitSet(new long[]{0x00002C0800000000L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule243 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_IF_in_graphrule246 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_condition_in_graphrule250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_condition303 = new BitSet(new long[]{0x00000007F0000002L});
    public static final BitSet FOLLOW_EQUALS_in_condition306 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_condition310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_condition318 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_condition322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEQ_in_condition330 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_condition334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_condition342 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_condition346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GEQ_in_condition354 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_condition358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_condition366 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_condition370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_condition378 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_condition380 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_LPAR_in_condition382 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_condition386 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_RPAR_in_condition388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern547 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern549 = new BitSet(new long[]{0x00000F2800000000L});
    public static final BitSet FOLLOW_term_in_pattern552 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern555 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_pattern557 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern587 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_AT_in_pattern590 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_pattern595 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern598 = new BitSet(new long[]{0x00000F2800000000L});
    public static final BitSet FOLLOW_term_in_pattern601 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern604 = new BitSet(new long[]{0x00000F0800000000L});
    public static final BitSet FOLLOW_term_in_pattern606 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_pattern641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORESTAR_in_pattern653 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_builtin697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_builtin711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labelledpattern733 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_COLON_in_labelledpattern735 = new BitSet(new long[]{0x00002C0800000000L});
    public static final BitSet FOLLOW_graphpattern_in_labelledpattern741 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_in_graphpattern774 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_graphpattern780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_graphpattern794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ref_in_graphpattern800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPERCENT_in_ref810 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_ref812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constructor830 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_LPAR_in_constructor832 = new BitSet(new long[]{0x00002C2800000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor835 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_COMA_in_constructor838 = new BitSet(new long[]{0x00002C0800000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor840 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_RPAR_in_constructor846 = new BitSet(new long[]{0x0000000000000002L});

}