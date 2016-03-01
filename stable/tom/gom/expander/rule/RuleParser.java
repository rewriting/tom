// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g 2016-03-01 15:43:34

package tom.gom.expander.rule;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PathTerm", "RefTerm", "LabTerm", "BuiltinString", "BuiltinInt", "Anti", "At", "UnnamedVarStar", "UnnamedVar", "VarStar", "Var", "Appl", "TermList", "ConditionalRule", "Rule", "RuleList", "CondMatch", "CondOr", "CondAnd", "CondGreaterThan", "CondGreaterEquals", "CondLessThan", "CondLessEquals", "CondNotEquals", "CondEquals", "ARROW", "IF", "OR", "AND", "EQUALS", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "MATCH", "LPAR", "RPAR", "ID", "COMA", "AT", "UNDERSCORE", "STAR", "NOT", "INT", "STRING", "COLON", "AMPERCENT", "ESC", "WS", "SLCOMMENT"
    };
    public static final int LT=42;
    public static final int STAR=52;
    public static final int ESC=58;
    public static final int RuleList=25;
    public static final int SLCOMMENT=60;
    public static final int EQUALS=39;
    public static final int NOT=53;
    public static final int AND=38;
    public static final int ID=48;
    public static final int EOF=-1;
    public static final int CondMatch=26;
    public static final int CondEquals=34;
    public static final int UnnamedVarStar=17;
    public static final int UnnamedVar=18;
    public static final int IF=36;
    public static final int AT=50;
    public static final int CondLessEquals=32;
    public static final int LPAR=46;
    public static final int CondGreaterThan=29;
    public static final int TermList=22;
    public static final int BuiltinString=13;
    public static final int GEQ=43;
    public static final int CondLessThan=31;
    public static final int BuiltinInt=14;
    public static final int At=16;
    public static final int VarStar=19;
    public static final int CondOr=27;
    public static final int NOTEQUALS=40;
    public static final int UNDERSCORE=51;
    public static final int MATCH=45;
    public static final int INT=54;
    public static final int COMA=49;
    public static final int CondNotEquals=33;
    public static final int COLON=56;
    public static final int WS=59;
    public static final int CondAnd=28;
    public static final int Rule=24;
    public static final int LabTerm=12;
    public static final int OR=37;
    public static final int Appl=21;
    public static final int ConditionalRule=23;
    public static final int RefTerm=11;
    public static final int ARROW=35;
    public static final int GT=44;
    public static final int RPAR=47;
    public static final int AMPERCENT=57;
    public static final int PathTerm=10;
    public static final int CondGreaterEquals=30;
    public static final int Anti=15;
    public static final int Var=20;
    public static final int STRING=55;
    public static final int LEQ=41;

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
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g"; }


    public static class ruleset_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ruleset"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:15:1: ruleset : ( rule )* EOF -> ^( RuleList ( rule )* ) ;
    public final RuleParser.ruleset_return ruleset() throws RecognitionException {
        RuleParser.ruleset_return retval = new RuleParser.ruleset_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token EOF2=null;
        RuleParser.rule_return rule1 = null;


        Tree EOF2_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:15:8: ( ( rule )* EOF -> ^( RuleList ( rule )* ) )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:16:3: ( rule )* EOF
            {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:16:3: ( rule )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID||LA1_0==UNDERSCORE||LA1_0==NOT) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:16:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset52);
            	    rule1=rule();

            	    state._fsp--;

            	    stream_rule.add(rule1.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_ruleset56);  
            stream_EOF.add(EOF2);



            // AST REWRITE
            // elements: rule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 16:15: -> ^( RuleList ( rule )* )
            {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:16:18: ^( RuleList ( rule )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(RuleList, "RuleList"), root_1);

                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:16:29: ( rule )*
                while ( stream_rule.hasNext() ) {
                    adaptor.addChild(root_1, stream_rule.nextTree());

                }
                stream_rule.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "ruleset"

    public static class rule_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "rule"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:18:1: rule : pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) ;
    public final RuleParser.rule_return rule() throws RecognitionException {
        RuleParser.rule_return retval = new RuleParser.rule_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ARROW4=null;
        Token IF6=null;
        RuleParser.condition_return cond = null;

        RuleParser.pattern_return pattern3 = null;

        RuleParser.term_return term5 = null;


        Tree ARROW4_tree=null;
        Tree IF6_tree=null;
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleSubtreeStream stream_pattern=new RewriteRuleSubtreeStream(adaptor,"rule pattern");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:18:5: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:19:3: pattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_pattern_in_rule76);
            pattern3=pattern();

            state._fsp--;

            stream_pattern.add(pattern3.getTree());
            ARROW4=(Token)match(input,ARROW,FOLLOW_ARROW_in_rule78);  
            stream_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule80);
            term5=term();

            state._fsp--;

            stream_term.add(term5.getTree());
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:19:22: ( IF cond= condition )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==IF) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:19:23: IF cond= condition
                    {
                    IF6=(Token)match(input,IF,FOLLOW_IF_in_rule83);  
                    stream_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule87);
                    cond=condition();

                    state._fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: cond, pattern, term, pattern, term
            // token labels: 
            // rule labels: retval, cond
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 20:5: -> { cond == null }? ^( Rule pattern term )
            if ( cond == null ) {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:20:26: ^( Rule pattern term )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Rule, "Rule"), root_1);

                adaptor.addChild(root_1, stream_pattern.nextTree());
                adaptor.addChild(root_1, stream_term.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 21:5: -> ^( ConditionalRule pattern term $cond)
            {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:21:8: ^( ConditionalRule pattern term $cond)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConditionalRule, "ConditionalRule"), root_1);

                adaptor.addChild(root_1, stream_pattern.nextTree());
                adaptor.addChild(root_1, stream_term.nextTree());
                adaptor.addChild(root_1, stream_cond.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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

    public static class graphruleset_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "graphruleset"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:23:1: graphruleset : ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) ;
    public final RuleParser.graphruleset_return graphruleset() throws RecognitionException {
        RuleParser.graphruleset_return retval = new RuleParser.graphruleset_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token EOF8=null;
        RuleParser.graphrule_return graphrule7 = null;


        Tree EOF8_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_graphrule=new RewriteRuleSubtreeStream(adaptor,"rule graphrule");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:23:13: ( ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:24:3: ( graphrule )* EOF
            {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:24:3: ( graphrule )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID||(LA3_0>=INT && LA3_0<=STRING)||LA3_0==AMPERCENT) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:24:4: graphrule
            	    {
            	    pushFollow(FOLLOW_graphrule_in_graphruleset132);
            	    graphrule7=graphrule();

            	    state._fsp--;

            	    stream_graphrule.add(graphrule7.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            EOF8=(Token)match(input,EOF,FOLLOW_EOF_in_graphruleset136);  
            stream_EOF.add(EOF8);



            // AST REWRITE
            // elements: graphrule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 24:20: -> ^( RuleList ( graphrule )* )
            {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:24:23: ^( RuleList ( graphrule )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(RuleList, "RuleList"), root_1);

                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:24:34: ( graphrule )*
                while ( stream_graphrule.hasNext() ) {
                    adaptor.addChild(root_1, stream_graphrule.nextTree());

                }
                stream_graphrule.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "graphruleset"

    public static class graphrule_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "graphrule"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:26:1: graphrule : lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) ;
    public final RuleParser.graphrule_return graphrule() throws RecognitionException {
        RuleParser.graphrule_return retval = new RuleParser.graphrule_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ARROW9=null;
        Token IF10=null;
        RuleParser.labelledpattern_return lhs = null;

        RuleParser.labelledpattern_return rhs = null;

        RuleParser.condition_return cond = null;


        Tree ARROW9_tree=null;
        Tree IF10_tree=null;
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:26:10: (lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:27:3: lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )?
            {
            pushFollow(FOLLOW_labelledpattern_in_graphrule158);
            lhs=labelledpattern();

            state._fsp--;

            stream_labelledpattern.add(lhs.getTree());
            ARROW9=(Token)match(input,ARROW,FOLLOW_ARROW_in_graphrule160);  
            stream_ARROW.add(ARROW9);

            pushFollow(FOLLOW_labelledpattern_in_graphrule164);
            rhs=labelledpattern();

            state._fsp--;

            stream_labelledpattern.add(rhs.getTree());
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:27:49: ( IF cond= condition )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==IF) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:27:50: IF cond= condition
                    {
                    IF10=(Token)match(input,IF,FOLLOW_IF_in_graphrule167);  
                    stream_IF.add(IF10);

                    pushFollow(FOLLOW_condition_in_graphrule171);
                    cond=condition();

                    state._fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: cond, rhs, lhs, lhs, rhs
            // token labels: 
            // rule labels: retval, rhs, cond, lhs
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_rhs=new RewriteRuleSubtreeStream(adaptor,"rule rhs",rhs!=null?rhs.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_lhs=new RewriteRuleSubtreeStream(adaptor,"rule lhs",lhs!=null?lhs.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 28:5: -> { cond == null }? ^( Rule $lhs $rhs)
            if ( cond == null ) {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:28:26: ^( Rule $lhs $rhs)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Rule, "Rule"), root_1);

                adaptor.addChild(root_1, stream_lhs.nextTree());
                adaptor.addChild(root_1, stream_rhs.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 29:5: -> ^( ConditionalRule $lhs $rhs $cond)
            {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:29:8: ^( ConditionalRule $lhs $rhs $cond)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConditionalRule, "ConditionalRule"), root_1);

                adaptor.addChild(root_1, stream_lhs.nextTree());
                adaptor.addChild(root_1, stream_rhs.nextTree());
                adaptor.addChild(root_1, stream_cond.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "graphrule"

    public static class condition_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "condition"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:31:1: condition : cond= andcondition (or= OR andcondition )* -> {or!=null}? ^( CondOr ( andcondition )* ) -> $cond;
    public final RuleParser.condition_return condition() throws RecognitionException {
        RuleParser.condition_return retval = new RuleParser.condition_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token or=null;
        RuleParser.andcondition_return cond = null;

        RuleParser.andcondition_return andcondition11 = null;


        Tree or_tree=null;
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
        RewriteRuleSubtreeStream stream_andcondition=new RewriteRuleSubtreeStream(adaptor,"rule andcondition");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:31:10: (cond= andcondition (or= OR andcondition )* -> {or!=null}? ^( CondOr ( andcondition )* ) -> $cond)
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:32:3: cond= andcondition (or= OR andcondition )*
            {
            pushFollow(FOLLOW_andcondition_in_condition221);
            cond=andcondition();

            state._fsp--;

            stream_andcondition.add(cond.getTree());
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:32:21: (or= OR andcondition )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==OR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:32:22: or= OR andcondition
            	    {
            	    or=(Token)match(input,OR,FOLLOW_OR_in_condition226);  
            	    stream_OR.add(or);

            	    pushFollow(FOLLOW_andcondition_in_condition228);
            	    andcondition11=andcondition();

            	    state._fsp--;

            	    stream_andcondition.add(andcondition11.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);



            // AST REWRITE
            // elements: andcondition, cond
            // token labels: 
            // rule labels: retval, cond
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 33:3: -> {or!=null}? ^( CondOr ( andcondition )* )
            if (or!=null) {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:33:18: ^( CondOr ( andcondition )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondOr, "CondOr"), root_1);

                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:33:27: ( andcondition )*
                while ( stream_andcondition.hasNext() ) {
                    adaptor.addChild(root_1, stream_andcondition.nextTree());

                }
                stream_andcondition.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 34:3: -> $cond
            {
                adaptor.addChild(root_0, stream_cond.nextTree());

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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

    public static class andcondition_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "andcondition"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:36:1: andcondition : cond= simplecondition (and= AND simplecondition )* -> {and!=null}? ^( CondAnd ( simplecondition )* ) -> $cond;
    public final RuleParser.andcondition_return andcondition() throws RecognitionException {
        RuleParser.andcondition_return retval = new RuleParser.andcondition_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token and=null;
        RuleParser.simplecondition_return cond = null;

        RuleParser.simplecondition_return simplecondition12 = null;


        Tree and_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_simplecondition=new RewriteRuleSubtreeStream(adaptor,"rule simplecondition");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:36:13: (cond= simplecondition (and= AND simplecondition )* -> {and!=null}? ^( CondAnd ( simplecondition )* ) -> $cond)
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:37:3: cond= simplecondition (and= AND simplecondition )*
            {
            pushFollow(FOLLOW_simplecondition_in_andcondition261);
            cond=simplecondition();

            state._fsp--;

            stream_simplecondition.add(cond.getTree());
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:37:24: (and= AND simplecondition )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==AND) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:37:25: and= AND simplecondition
            	    {
            	    and=(Token)match(input,AND,FOLLOW_AND_in_andcondition266);  
            	    stream_AND.add(and);

            	    pushFollow(FOLLOW_simplecondition_in_andcondition268);
            	    simplecondition12=simplecondition();

            	    state._fsp--;

            	    stream_simplecondition.add(simplecondition12.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);



            // AST REWRITE
            // elements: simplecondition, cond
            // token labels: 
            // rule labels: retval, cond
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 38:3: -> {and!=null}? ^( CondAnd ( simplecondition )* )
            if (and!=null) {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:38:19: ^( CondAnd ( simplecondition )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondAnd, "CondAnd"), root_1);

                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:38:29: ( simplecondition )*
                while ( stream_simplecondition.hasNext() ) {
                    adaptor.addChild(root_1, stream_simplecondition.nextTree());

                }
                stream_simplecondition.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 39:3: -> $cond
            {
                adaptor.addChild(root_0, stream_cond.nextTree());

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "andcondition"

    public static class simplecondition_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simplecondition"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:41:1: simplecondition : (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term ) -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> ^( CondMatch $p1 $p8) | LPAR cond= condition RPAR -> $cond);
    public final RuleParser.simplecondition_return simplecondition() throws RecognitionException {
        RuleParser.simplecondition_return retval = new RuleParser.simplecondition_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token EQUALS13=null;
        Token NOTEQUALS14=null;
        Token LEQ15=null;
        Token LT16=null;
        Token GEQ17=null;
        Token GT18=null;
        Token MATCH19=null;
        Token LPAR20=null;
        Token RPAR21=null;
        RuleParser.term_return p1 = null;

        RuleParser.term_return p2 = null;

        RuleParser.term_return p3 = null;

        RuleParser.term_return p4 = null;

        RuleParser.term_return p5 = null;

        RuleParser.term_return p6 = null;

        RuleParser.term_return p7 = null;

        RuleParser.term_return p8 = null;

        RuleParser.condition_return cond = null;


        Tree EQUALS13_tree=null;
        Tree NOTEQUALS14_tree=null;
        Tree LEQ15_tree=null;
        Tree LT16_tree=null;
        Tree GEQ17_tree=null;
        Tree GT18_tree=null;
        Tree MATCH19_tree=null;
        Tree LPAR20_tree=null;
        Tree RPAR21_tree=null;
        RewriteRuleTokenStream stream_NOTEQUALS=new RewriteRuleTokenStream(adaptor,"token NOTEQUALS");
        RewriteRuleTokenStream stream_GT=new RewriteRuleTokenStream(adaptor,"token GT");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LT=new RewriteRuleTokenStream(adaptor,"token LT");
        RewriteRuleTokenStream stream_MATCH=new RewriteRuleTokenStream(adaptor,"token MATCH");
        RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_GEQ=new RewriteRuleTokenStream(adaptor,"token GEQ");
        RewriteRuleTokenStream stream_LEQ=new RewriteRuleTokenStream(adaptor,"token LEQ");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:41:16: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term ) -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> ^( CondMatch $p1 $p8) | LPAR cond= condition RPAR -> $cond)
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ID||LA8_0==UNDERSCORE||(LA8_0>=NOT && LA8_0<=STRING)) ) {
                alt8=1;
            }
            else if ( (LA8_0==LPAR) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:42:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term )
                    {
                    pushFollow(FOLLOW_term_in_simplecondition301);
                    p1=term();

                    state._fsp--;

                    stream_term.add(p1.getTree());
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:42:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term )
                    int alt7=7;
                    switch ( input.LA(1) ) {
                    case EQUALS:
                        {
                        alt7=1;
                        }
                        break;
                    case NOTEQUALS:
                        {
                        alt7=2;
                        }
                        break;
                    case LEQ:
                        {
                        alt7=3;
                        }
                        break;
                    case LT:
                        {
                        alt7=4;
                        }
                        break;
                    case GEQ:
                        {
                        alt7=5;
                        }
                        break;
                    case GT:
                        {
                        alt7=6;
                        }
                        break;
                    case MATCH:
                        {
                        alt7=7;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 7, 0, input);

                        throw nvae;
                    }

                    switch (alt7) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:42:12: EQUALS p2= term
                            {
                            EQUALS13=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_simplecondition304);  
                            stream_EQUALS.add(EQUALS13);

                            pushFollow(FOLLOW_term_in_simplecondition308);
                            p2=term();

                            state._fsp--;

                            stream_term.add(p2.getTree());

                            }
                            break;
                        case 2 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:43:13: NOTEQUALS p3= term
                            {
                            NOTEQUALS14=(Token)match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_simplecondition322);  
                            stream_NOTEQUALS.add(NOTEQUALS14);

                            pushFollow(FOLLOW_term_in_simplecondition326);
                            p3=term();

                            state._fsp--;

                            stream_term.add(p3.getTree());

                            }
                            break;
                        case 3 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:44:13: LEQ p4= term
                            {
                            LEQ15=(Token)match(input,LEQ,FOLLOW_LEQ_in_simplecondition340);  
                            stream_LEQ.add(LEQ15);

                            pushFollow(FOLLOW_term_in_simplecondition344);
                            p4=term();

                            state._fsp--;

                            stream_term.add(p4.getTree());

                            }
                            break;
                        case 4 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:45:13: LT p5= term
                            {
                            LT16=(Token)match(input,LT,FOLLOW_LT_in_simplecondition358);  
                            stream_LT.add(LT16);

                            pushFollow(FOLLOW_term_in_simplecondition362);
                            p5=term();

                            state._fsp--;

                            stream_term.add(p5.getTree());

                            }
                            break;
                        case 5 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:46:13: GEQ p6= term
                            {
                            GEQ17=(Token)match(input,GEQ,FOLLOW_GEQ_in_simplecondition376);  
                            stream_GEQ.add(GEQ17);

                            pushFollow(FOLLOW_term_in_simplecondition380);
                            p6=term();

                            state._fsp--;

                            stream_term.add(p6.getTree());

                            }
                            break;
                        case 6 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:47:13: GT p7= term
                            {
                            GT18=(Token)match(input,GT,FOLLOW_GT_in_simplecondition394);  
                            stream_GT.add(GT18);

                            pushFollow(FOLLOW_term_in_simplecondition398);
                            p7=term();

                            state._fsp--;

                            stream_term.add(p7.getTree());

                            }
                            break;
                        case 7 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:48:13: MATCH p8= term
                            {
                            MATCH19=(Token)match(input,MATCH,FOLLOW_MATCH_in_simplecondition412);  
                            stream_MATCH.add(MATCH19);

                            pushFollow(FOLLOW_term_in_simplecondition416);
                            p8=term();

                            state._fsp--;

                            stream_term.add(p8.getTree());

                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: p1, p1, p6, p3, p5, p1, p1, p7, p1, p1, p2, p1, p4, p8
                    // token labels: 
                    // rule labels: p7, p6, retval, p5, p4, p3, p2, p1, p8
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_p7=new RewriteRuleSubtreeStream(adaptor,"rule p7",p7!=null?p7.tree:null);
                    RewriteRuleSubtreeStream stream_p6=new RewriteRuleSubtreeStream(adaptor,"rule p6",p6!=null?p6.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_p5=new RewriteRuleSubtreeStream(adaptor,"rule p5",p5!=null?p5.tree:null);
                    RewriteRuleSubtreeStream stream_p4=new RewriteRuleSubtreeStream(adaptor,"rule p4",p4!=null?p4.tree:null);
                    RewriteRuleSubtreeStream stream_p3=new RewriteRuleSubtreeStream(adaptor,"rule p3",p3!=null?p3.tree:null);
                    RewriteRuleSubtreeStream stream_p2=new RewriteRuleSubtreeStream(adaptor,"rule p2",p2!=null?p2.tree:null);
                    RewriteRuleSubtreeStream stream_p1=new RewriteRuleSubtreeStream(adaptor,"rule p1",p1!=null?p1.tree:null);
                    RewriteRuleSubtreeStream stream_p8=new RewriteRuleSubtreeStream(adaptor,"rule p8",p8!=null?p8.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 50:5: -> {p2!=null}? ^( CondEquals $p1 $p2)
                    if (p2!=null) {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:50:20: ^( CondEquals $p1 $p2)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondEquals, "CondEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 51:5: -> {p3!=null}? ^( CondNotEquals $p1 $p3)
                    if (p3!=null) {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:51:20: ^( CondNotEquals $p1 $p3)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondNotEquals, "CondNotEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p3.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 52:5: -> {p4!=null}? ^( CondLessEquals $p1 $p4)
                    if (p4!=null) {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:52:20: ^( CondLessEquals $p1 $p4)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondLessEquals, "CondLessEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p4.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 53:5: -> {p5!=null}? ^( CondLessThan $p1 $p5)
                    if (p5!=null) {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:53:20: ^( CondLessThan $p1 $p5)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondLessThan, "CondLessThan"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p5.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 54:5: -> {p6!=null}? ^( CondGreaterEquals $p1 $p6)
                    if (p6!=null) {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:54:20: ^( CondGreaterEquals $p1 $p6)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondGreaterEquals, "CondGreaterEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p6.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 55:5: -> {p7!=null}? ^( CondGreaterThan $p1 $p7)
                    if (p7!=null) {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:55:20: ^( CondGreaterThan $p1 $p7)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondGreaterThan, "CondGreaterThan"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p7.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 56:5: -> ^( CondMatch $p1 $p8)
                    {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:56:8: ^( CondMatch $p1 $p8)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondMatch, "CondMatch"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p8.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:57:5: LPAR cond= condition RPAR
                    {
                    LPAR20=(Token)match(input,LPAR,FOLLOW_LPAR_in_simplecondition558);  
                    stream_LPAR.add(LPAR20);

                    pushFollow(FOLLOW_condition_in_simplecondition562);
                    cond=condition();

                    state._fsp--;

                    stream_condition.add(cond.getTree());
                    RPAR21=(Token)match(input,RPAR,FOLLOW_RPAR_in_simplecondition564);  
                    stream_RPAR.add(RPAR21);



                    // AST REWRITE
                    // elements: cond
                    // token labels: 
                    // rule labels: retval, cond
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 58:3: -> $cond
                    {
                        adaptor.addChild(root_0, stream_cond.nextTree());

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "simplecondition"

    public static class pattern_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pattern"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:60:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) | NOT pattern -> ^( Anti pattern ) );
    public final RuleParser.pattern_return pattern() throws RecognitionException {
        RuleParser.pattern_return retval = new RuleParser.pattern_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token varname=null;
        Token funname=null;
        Token ID22=null;
        Token LPAR23=null;
        Token COMA25=null;
        Token RPAR27=null;
        Token AT28=null;
        Token LPAR29=null;
        Token COMA31=null;
        Token RPAR33=null;
        Token UNDERSCORE34=null;
        Token UNDERSCORE35=null;
        Token STAR36=null;
        Token NOT37=null;
        RuleParser.term_return term24 = null;

        RuleParser.term_return term26 = null;

        RuleParser.term_return term30 = null;

        RuleParser.term_return term32 = null;

        RuleParser.pattern_return pattern38 = null;


        Tree varname_tree=null;
        Tree funname_tree=null;
        Tree ID22_tree=null;
        Tree LPAR23_tree=null;
        Tree COMA25_tree=null;
        Tree RPAR27_tree=null;
        Tree AT28_tree=null;
        Tree LPAR29_tree=null;
        Tree COMA31_tree=null;
        Tree RPAR33_tree=null;
        Tree UNDERSCORE34_tree=null;
        Tree UNDERSCORE35_tree=null;
        Tree STAR36_tree=null;
        Tree NOT37_tree=null;
        RewriteRuleTokenStream stream_AT=new RewriteRuleTokenStream(adaptor,"token AT");
        RewriteRuleTokenStream stream_UNDERSCORE=new RewriteRuleTokenStream(adaptor,"token UNDERSCORE");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleSubtreeStream stream_pattern=new RewriteRuleSubtreeStream(adaptor,"rule pattern");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:60:8: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) | NOT pattern -> ^( Anti pattern ) )
            int alt13=5;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==LPAR) ) {
                    alt13=1;
                }
                else if ( (LA13_1==AT) ) {
                    alt13=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;
                }
                }
                break;
            case UNDERSCORE:
                {
                int LA13_2 = input.LA(2);

                if ( (LA13_2==STAR) ) {
                    alt13=4;
                }
                else if ( (LA13_2==EOF||(LA13_2>=ARROW && LA13_2<=MATCH)||(LA13_2>=RPAR && LA13_2<=COMA)||LA13_2==UNDERSCORE||(LA13_2>=NOT && LA13_2<=STRING)||LA13_2==AMPERCENT) ) {
                    alt13=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 2, input);

                    throw nvae;
                }
                }
                break;
            case NOT:
                {
                alt13=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:61:3: ID LPAR ( term ( COMA term )* )? RPAR
                    {
                    ID22=(Token)match(input,ID,FOLLOW_ID_in_pattern580);  
                    stream_ID.add(ID22);

                    LPAR23=(Token)match(input,LPAR,FOLLOW_LPAR_in_pattern582);  
                    stream_LPAR.add(LPAR23);

                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:61:11: ( term ( COMA term )* )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==ID||LA10_0==UNDERSCORE||(LA10_0>=NOT && LA10_0<=STRING)) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:61:12: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern585);
                            term24=term();

                            state._fsp--;

                            stream_term.add(term24.getTree());
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:61:17: ( COMA term )*
                            loop9:
                            do {
                                int alt9=2;
                                int LA9_0 = input.LA(1);

                                if ( (LA9_0==COMA) ) {
                                    alt9=1;
                                }


                                switch (alt9) {
                            	case 1 :
                            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:61:18: COMA term
                            	    {
                            	    COMA25=(Token)match(input,COMA,FOLLOW_COMA_in_pattern588);  
                            	    stream_COMA.add(COMA25);

                            	    pushFollow(FOLLOW_term_in_pattern590);
                            	    term26=term();

                            	    state._fsp--;

                            	    stream_term.add(term26.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop9;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR27=(Token)match(input,RPAR,FOLLOW_RPAR_in_pattern596);  
                    stream_RPAR.add(RPAR27);



                    // AST REWRITE
                    // elements: term, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 61:37: -> ^( Appl ID ^( TermList ( term )* ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:61:40: ^( Appl ID ^( TermList ( term )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Appl, "Appl"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:61:50: ^( TermList ( term )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(TermList, "TermList"), root_2);

                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:61:61: ( term )*
                        while ( stream_term.hasNext() ) {
                            adaptor.addChild(root_2, stream_term.nextTree());

                        }
                        stream_term.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:5: (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR
                    {
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:5: (varname= ID )
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:6: varname= ID
                    {
                    varname=(Token)match(input,ID,FOLLOW_ID_in_pattern620);  
                    stream_ID.add(varname);


                    }

                    AT28=(Token)match(input,AT,FOLLOW_AT_in_pattern623);  
                    stream_AT.add(AT28);

                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:21: (funname= ID )
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:22: funname= ID
                    {
                    funname=(Token)match(input,ID,FOLLOW_ID_in_pattern628);  
                    stream_ID.add(funname);


                    }

                    LPAR29=(Token)match(input,LPAR,FOLLOW_LPAR_in_pattern631);  
                    stream_LPAR.add(LPAR29);

                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:39: ( term ( COMA term )* )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==ID||LA12_0==UNDERSCORE||(LA12_0>=NOT && LA12_0<=STRING)) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:40: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern634);
                            term30=term();

                            state._fsp--;

                            stream_term.add(term30.getTree());
                            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:45: ( COMA term )*
                            loop11:
                            do {
                                int alt11=2;
                                int LA11_0 = input.LA(1);

                                if ( (LA11_0==COMA) ) {
                                    alt11=1;
                                }


                                switch (alt11) {
                            	case 1 :
                            	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:46: COMA term
                            	    {
                            	    COMA31=(Token)match(input,COMA,FOLLOW_COMA_in_pattern637);  
                            	    stream_COMA.add(COMA31);

                            	    pushFollow(FOLLOW_term_in_pattern639);
                            	    term32=term();

                            	    state._fsp--;

                            	    stream_term.add(term32.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop11;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR33=(Token)match(input,RPAR,FOLLOW_RPAR_in_pattern645);  
                    stream_RPAR.add(RPAR33);



                    // AST REWRITE
                    // elements: term, funname, varname
                    // token labels: funname, varname
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_funname=new RewriteRuleTokenStream(adaptor,"token funname",funname);
                    RewriteRuleTokenStream stream_varname=new RewriteRuleTokenStream(adaptor,"token varname",varname);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 62:65: -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:68: ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(At, "At"), root_1);

                        adaptor.addChild(root_1, stream_varname.nextNode());
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:82: ^( Appl $funname ^( TermList ( term )* ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Appl, "Appl"), root_2);

                        adaptor.addChild(root_2, stream_funname.nextNode());
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:98: ^( TermList ( term )* )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(TermList, "TermList"), root_3);

                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:62:109: ( term )*
                        while ( stream_term.hasNext() ) {
                            adaptor.addChild(root_3, stream_term.nextTree());

                        }
                        stream_term.reset();

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:63:5: UNDERSCORE
                    {
                    UNDERSCORE34=(Token)match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_pattern674);  
                    stream_UNDERSCORE.add(UNDERSCORE34);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 63:16: -> ^( UnnamedVar )
                    {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:63:19: ^( UnnamedVar )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(UnnamedVar, "UnnamedVar"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:64:5: UNDERSCORE STAR
                    {
                    UNDERSCORE35=(Token)match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_pattern686);  
                    stream_UNDERSCORE.add(UNDERSCORE35);

                    STAR36=(Token)match(input,STAR,FOLLOW_STAR_in_pattern688);  
                    stream_STAR.add(STAR36);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 64:21: -> ^( UnnamedVarStar )
                    {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:64:24: ^( UnnamedVarStar )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(UnnamedVarStar, "UnnamedVarStar"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 5 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:65:5: NOT pattern
                    {
                    NOT37=(Token)match(input,NOT,FOLLOW_NOT_in_pattern700);  
                    stream_NOT.add(NOT37);

                    pushFollow(FOLLOW_pattern_in_pattern702);
                    pattern38=pattern();

                    state._fsp--;

                    stream_pattern.add(pattern38.getTree());


                    // AST REWRITE
                    // elements: pattern
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 65:17: -> ^( Anti pattern )
                    {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:65:20: ^( Anti pattern )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Anti, "Anti"), root_1);

                        adaptor.addChild(root_1, stream_pattern.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:67:1: term : ( pattern | var | builtin );
    public final RuleParser.term_return term() throws RecognitionException {
        RuleParser.term_return retval = new RuleParser.term_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        RuleParser.pattern_return pattern39 = null;

        RuleParser.var_return var40 = null;

        RuleParser.builtin_return builtin41 = null;



        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:67:5: ( pattern | var | builtin )
            int alt14=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA14_1 = input.LA(2);

                if ( (LA14_1==LPAR||LA14_1==AT) ) {
                    alt14=1;
                }
                else if ( (LA14_1==EOF||(LA14_1>=IF && LA14_1<=MATCH)||(LA14_1>=RPAR && LA14_1<=COMA)||(LA14_1>=UNDERSCORE && LA14_1<=STRING)||LA14_1==AMPERCENT) ) {
                    alt14=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;
                }
                }
                break;
            case UNDERSCORE:
            case NOT:
                {
                alt14=1;
                }
                break;
            case INT:
            case STRING:
                {
                alt14=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:68:3: pattern
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term719);
                    pattern39=pattern();

                    state._fsp--;

                    adaptor.addChild(root_0, pattern39.getTree());

                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:69:5: var
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_var_in_term725);
                    var40=var();

                    state._fsp--;

                    adaptor.addChild(root_0, var40.getTree());

                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:70:5: builtin
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term731);
                    builtin41=builtin();

                    state._fsp--;

                    adaptor.addChild(root_0, builtin41.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:72:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );
    public final RuleParser.builtin_return builtin() throws RecognitionException {
        RuleParser.builtin_return retval = new RuleParser.builtin_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token INT42=null;
        Token STRING43=null;

        Tree INT42_tree=null;
        Tree STRING43_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:72:8: ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==INT) ) {
                alt15=1;
            }
            else if ( (LA15_0==STRING) ) {
                alt15=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:73:3: INT
                    {
                    INT42=(Token)match(input,INT,FOLLOW_INT_in_builtin740);  
                    stream_INT.add(INT42);



                    // AST REWRITE
                    // elements: INT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 73:7: -> ^( BuiltinInt INT )
                    {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:73:10: ^( BuiltinInt INT )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(BuiltinInt, "BuiltinInt"), root_1);

                        adaptor.addChild(root_1, stream_INT.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:74:5: STRING
                    {
                    STRING43=(Token)match(input,STRING,FOLLOW_STRING_in_builtin754);  
                    stream_STRING.add(STRING43);



                    // AST REWRITE
                    // elements: STRING
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 74:12: -> ^( BuiltinString STRING )
                    {
                        // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:74:15: ^( BuiltinString STRING )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(BuiltinString, "BuiltinString"), root_1);

                        adaptor.addChild(root_1, stream_STRING.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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

    public static class labelledpattern_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "labelledpattern"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:77:1: labelledpattern : (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p;
    public final RuleParser.labelledpattern_return labelledpattern() throws RecognitionException {
        RuleParser.labelledpattern_return retval = new RuleParser.labelledpattern_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token namelabel=null;
        Token COLON44=null;
        RuleParser.graphpattern_return p = null;


        Tree namelabel_tree=null;
        Tree COLON44_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_graphpattern=new RewriteRuleSubtreeStream(adaptor,"rule graphpattern");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:77:16: ( (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p)
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:78:3: (namelabel= ID COLON )? p= graphpattern
            {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:78:3: (namelabel= ID COLON )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==ID) ) {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==COLON) ) {
                    alt16=1;
                }
            }
            switch (alt16) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:78:4: namelabel= ID COLON
                    {
                    namelabel=(Token)match(input,ID,FOLLOW_ID_in_labelledpattern775);  
                    stream_ID.add(namelabel);

                    COLON44=(Token)match(input,COLON,FOLLOW_COLON_in_labelledpattern777);  
                    stream_COLON.add(COLON44);


                    }
                    break;

            }

            pushFollow(FOLLOW_graphpattern_in_labelledpattern783);
            p=graphpattern();

            state._fsp--;

            stream_graphpattern.add(p.getTree());


            // AST REWRITE
            // elements: p, p, namelabel
            // token labels: namelabel
            // rule labels: retval, p
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_namelabel=new RewriteRuleTokenStream(adaptor,"token namelabel",namelabel);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_p=new RewriteRuleSubtreeStream(adaptor,"rule p",p!=null?p.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 79:3: -> {$namelabel!=null}? ^( LabTerm $namelabel $p)
            if (namelabel!=null) {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:79:26: ^( LabTerm $namelabel $p)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(LabTerm, "LabTerm"), root_1);

                adaptor.addChild(root_1, stream_namelabel.nextNode());
                adaptor.addChild(root_1, stream_p.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 80:3: -> $p
            {
                adaptor.addChild(root_0, stream_p.nextTree());

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "labelledpattern"

    public static class graphpattern_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "graphpattern"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:82:1: graphpattern : ( constructor | var | builtin | ref );
    public final RuleParser.graphpattern_return graphpattern() throws RecognitionException {
        RuleParser.graphpattern_return retval = new RuleParser.graphpattern_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        RuleParser.constructor_return constructor45 = null;

        RuleParser.var_return var46 = null;

        RuleParser.builtin_return builtin47 = null;

        RuleParser.ref_return ref48 = null;



        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:82:13: ( constructor | var | builtin | ref )
            int alt17=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==LPAR) ) {
                    alt17=1;
                }
                else if ( (LA17_1==EOF||(LA17_1>=ARROW && LA17_1<=IF)||(LA17_1>=RPAR && LA17_1<=COMA)||LA17_1==STAR||(LA17_1>=INT && LA17_1<=STRING)||LA17_1==AMPERCENT) ) {
                    alt17=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT:
            case STRING:
                {
                alt17=3;
                }
                break;
            case AMPERCENT:
                {
                alt17=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:83:3: constructor
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_constructor_in_graphpattern815);
                    constructor45=constructor();

                    state._fsp--;

                    adaptor.addChild(root_0, constructor45.getTree());

                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:84:5: var
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_var_in_graphpattern821);
                    var46=var();

                    state._fsp--;

                    adaptor.addChild(root_0, var46.getTree());

                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:85:5: builtin
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_graphpattern827);
                    builtin47=builtin();

                    state._fsp--;

                    adaptor.addChild(root_0, builtin47.getTree());

                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:86:5: ref
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_ref_in_graphpattern833);
                    ref48=ref();

                    state._fsp--;

                    adaptor.addChild(root_0, ref48.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "graphpattern"

    public static class var_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "var"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:88:1: var : ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) ;
    public final RuleParser.var_return var() throws RecognitionException {
        RuleParser.var_return retval = new RuleParser.var_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token s=null;
        Token ID49=null;

        Tree s_tree=null;
        Tree ID49_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:88:4: ( ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:89:3: ID (s= STAR )?
            {
            ID49=(Token)match(input,ID,FOLLOW_ID_in_var842);  
            stream_ID.add(ID49);

            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:89:6: (s= STAR )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==STAR) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:89:7: s= STAR
                    {
                    s=(Token)match(input,STAR,FOLLOW_STAR_in_var847);  
                    stream_STAR.add(s);


                    }
                    break;

            }



            // AST REWRITE
            // elements: ID, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 89:16: -> {null==s}? ^( Var ID )
            if (null==s) {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:89:30: ^( Var ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Var, "Var"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 89:40: -> ^( VarStar ID )
            {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:89:42: ^( VarStar ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(VarStar, "VarStar"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "var"

    public static class ref_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ref"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:91:1: ref : AMPERCENT ID -> ^( RefTerm ID ) ;
    public final RuleParser.ref_return ref() throws RecognitionException {
        RuleParser.ref_return retval = new RuleParser.ref_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token AMPERCENT50=null;
        Token ID51=null;

        Tree AMPERCENT50_tree=null;
        Tree ID51_tree=null;
        RewriteRuleTokenStream stream_AMPERCENT=new RewriteRuleTokenStream(adaptor,"token AMPERCENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:91:4: ( AMPERCENT ID -> ^( RefTerm ID ) )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:92:3: AMPERCENT ID
            {
            AMPERCENT50=(Token)match(input,AMPERCENT,FOLLOW_AMPERCENT_in_ref875);  
            stream_AMPERCENT.add(AMPERCENT50);

            ID51=(Token)match(input,ID,FOLLOW_ID_in_ref877);  
            stream_ID.add(ID51);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 92:16: -> ^( RefTerm ID )
            {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:92:19: ^( RefTerm ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(RefTerm, "RefTerm"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "ref"

    public static class constructor_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "constructor"
    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:94:1: constructor : ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) ;
    public final RuleParser.constructor_return constructor() throws RecognitionException {
        RuleParser.constructor_return retval = new RuleParser.constructor_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID52=null;
        Token LPAR53=null;
        Token COMA55=null;
        Token RPAR57=null;
        RuleParser.labelledpattern_return labelledpattern54 = null;

        RuleParser.labelledpattern_return labelledpattern56 = null;


        Tree ID52_tree=null;
        Tree LPAR53_tree=null;
        Tree COMA55_tree=null;
        Tree RPAR57_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        try {
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:94:12: ( ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) )
            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:95:3: ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR
            {
            ID52=(Token)match(input,ID,FOLLOW_ID_in_constructor894);  
            stream_ID.add(ID52);

            LPAR53=(Token)match(input,LPAR,FOLLOW_LPAR_in_constructor896);  
            stream_LPAR.add(LPAR53);

            // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:95:11: ( labelledpattern ( COMA labelledpattern )* )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==ID||(LA20_0>=INT && LA20_0<=STRING)||LA20_0==AMPERCENT) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:95:12: labelledpattern ( COMA labelledpattern )*
                    {
                    pushFollow(FOLLOW_labelledpattern_in_constructor899);
                    labelledpattern54=labelledpattern();

                    state._fsp--;

                    stream_labelledpattern.add(labelledpattern54.getTree());
                    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:95:28: ( COMA labelledpattern )*
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( (LA19_0==COMA) ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:95:29: COMA labelledpattern
                    	    {
                    	    COMA55=(Token)match(input,COMA,FOLLOW_COMA_in_constructor902);  
                    	    stream_COMA.add(COMA55);

                    	    pushFollow(FOLLOW_labelledpattern_in_constructor904);
                    	    labelledpattern56=labelledpattern();

                    	    state._fsp--;

                    	    stream_labelledpattern.add(labelledpattern56.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop19;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR57=(Token)match(input,RPAR,FOLLOW_RPAR_in_constructor910);  
            stream_RPAR.add(RPAR57);



            // AST REWRITE
            // elements: ID, labelledpattern
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 96:3: -> ^( Appl ID ^( TermList ( labelledpattern )* ) )
            {
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:96:6: ^( Appl ID ^( TermList ( labelledpattern )* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Appl, "Appl"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:96:16: ^( TermList ( labelledpattern )* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(TermList, "TermList"), root_2);

                // /Users/pem/github/tom/src/tom/gom/expander/rule/Rule.g:96:27: ( labelledpattern )*
                while ( stream_labelledpattern.hasNext() ) {
                    adaptor.addChild(root_2, stream_labelledpattern.nextTree());

                }
                stream_labelledpattern.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

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
    // $ANTLR end "constructor"

    // Delegated rules


 

    public static final BitSet FOLLOW_rule_in_ruleset52 = new BitSet(new long[]{0x0029000000000000L});
    public static final BitSet FOLLOW_EOF_in_ruleset56 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule76 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ARROW_in_rule78 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_rule80 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_IF_in_rule83 = new BitSet(new long[]{0x00E9400000000000L});
    public static final BitSet FOLLOW_condition_in_rule87 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_graphrule_in_graphruleset132 = new BitSet(new long[]{0x02E9000000000000L});
    public static final BitSet FOLLOW_EOF_in_graphruleset136 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule158 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ARROW_in_graphrule160 = new BitSet(new long[]{0x02E9000000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule164 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_IF_in_graphrule167 = new BitSet(new long[]{0x00E9400000000000L});
    public static final BitSet FOLLOW_condition_in_graphrule171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andcondition_in_condition221 = new BitSet(new long[]{0x0000002000000002L});
    public static final BitSet FOLLOW_OR_in_condition226 = new BitSet(new long[]{0x00E9400000000000L});
    public static final BitSet FOLLOW_andcondition_in_condition228 = new BitSet(new long[]{0x0000002000000002L});
    public static final BitSet FOLLOW_simplecondition_in_andcondition261 = new BitSet(new long[]{0x0000004000000002L});
    public static final BitSet FOLLOW_AND_in_andcondition266 = new BitSet(new long[]{0x00E9400000000000L});
    public static final BitSet FOLLOW_simplecondition_in_andcondition268 = new BitSet(new long[]{0x0000004000000002L});
    public static final BitSet FOLLOW_term_in_simplecondition301 = new BitSet(new long[]{0x00003F8000000000L});
    public static final BitSet FOLLOW_EQUALS_in_simplecondition304 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_simplecondition322 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEQ_in_simplecondition340 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_simplecondition358 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GEQ_in_simplecondition376 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_simplecondition394 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MATCH_in_simplecondition412 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition416 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_simplecondition558 = new BitSet(new long[]{0x00E9400000000000L});
    public static final BitSet FOLLOW_condition_in_simplecondition562 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_RPAR_in_simplecondition564 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern580 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern582 = new BitSet(new long[]{0x00E9800000000000L});
    public static final BitSet FOLLOW_term_in_pattern585 = new BitSet(new long[]{0x0002800000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern588 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_pattern590 = new BitSet(new long[]{0x0002800000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern620 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_AT_in_pattern623 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_ID_in_pattern628 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern631 = new BitSet(new long[]{0x00E9800000000000L});
    public static final BitSet FOLLOW_term_in_pattern634 = new BitSet(new long[]{0x0002800000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern637 = new BitSet(new long[]{0x00E9000000000000L});
    public static final BitSet FOLLOW_term_in_pattern639 = new BitSet(new long[]{0x0002800000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_pattern674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_pattern686 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_STAR_in_pattern688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_pattern700 = new BitSet(new long[]{0x0029000000000000L});
    public static final BitSet FOLLOW_pattern_in_pattern702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_term725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_builtin740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_builtin754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labelledpattern775 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_COLON_in_labelledpattern777 = new BitSet(new long[]{0x02E9000000000000L});
    public static final BitSet FOLLOW_graphpattern_in_labelledpattern783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_in_graphpattern815 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_graphpattern821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_graphpattern827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ref_in_graphpattern833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_var842 = new BitSet(new long[]{0x0010000000000002L});
    public static final BitSet FOLLOW_STAR_in_var847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPERCENT_in_ref875 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_ID_in_ref877 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constructor894 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_LPAR_in_constructor896 = new BitSet(new long[]{0x02E9800000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor899 = new BitSet(new long[]{0x0002800000000000L});
    public static final BitSet FOLLOW_COMA_in_constructor902 = new BitSet(new long[]{0x02E9000000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor904 = new BitSet(new long[]{0x0002800000000000L});
    public static final BitSet FOLLOW_RPAR_in_constructor910 = new BitSet(new long[]{0x0000000000000002L});

}