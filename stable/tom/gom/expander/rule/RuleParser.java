// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g 2009-05-06 09:28:59

package tom.gom.expander.rule;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "At", "VarStar", "CondOr", "RuleList", "CondMatch", "CondNotEquals", "UnnamedVar", "UnnamedVarStar", "CondEquals", "CondLessEquals", "Rule", "CondAnd", "LabTerm", "CondGreaterThan", "TermList", "Appl", "ConditionalRule", "RefTerm", "PathTerm", "BuiltinString", "CondGreaterEquals", "CondLessThan", "Anti", "Var", "BuiltinInt", "ARROW", "IF", "OR", "AND", "EQUALS", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "MATCH", "LPAR", "RPAR", "ID", "COMA", "AT", "UNDERSCORE", "STAR", "NOT", "INT", "STRING", "COLON", "AMPERCENT", "ESC", "WS", "SLCOMMENT"
    };
    public static final int LT=36;
    public static final int STAR=46;
    public static final int ESC=52;
    public static final int RuleList=7;
    public static final int SLCOMMENT=54;
    public static final int EQUALS=33;
    public static final int NOT=47;
    public static final int AND=32;
    public static final int ID=42;
    public static final int EOF=-1;
    public static final int CondMatch=8;
    public static final int CondEquals=12;
    public static final int UnnamedVarStar=11;
    public static final int UnnamedVar=10;
    public static final int IF=30;
    public static final int AT=44;
    public static final int CondLessEquals=13;
    public static final int LPAR=40;
    public static final int CondGreaterThan=17;
    public static final int TermList=18;
    public static final int BuiltinString=23;
    public static final int GEQ=37;
    public static final int CondLessThan=25;
    public static final int BuiltinInt=28;
    public static final int At=4;
    public static final int VarStar=5;
    public static final int CondOr=6;
    public static final int NOTEQUALS=34;
    public static final int UNDERSCORE=45;
    public static final int MATCH=39;
    public static final int INT=48;
    public static final int COMA=43;
    public static final int CondNotEquals=9;
    public static final int COLON=50;
    public static final int WS=53;
    public static final int CondAnd=15;
    public static final int Rule=14;
    public static final int LabTerm=16;
    public static final int OR=31;
    public static final int Appl=19;
    public static final int ConditionalRule=20;
    public static final int RefTerm=21;
    public static final int ARROW=29;
    public static final int GT=38;
    public static final int RPAR=41;
    public static final int AMPERCENT=51;
    public static final int PathTerm=22;
    public static final int CondGreaterEquals=24;
    public static final int Anti=26;
    public static final int Var=27;
    public static final int STRING=49;
    public static final int LEQ=35;

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
    public String getGrammarFileName() { return "/Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g"; }


    public static class ruleset_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ruleset"
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:18:1: ruleset : ( rule )* EOF -> ^( RuleList ( rule )* ) ;
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:18:8: ( ( rule )* EOF -> ^( RuleList ( rule )* ) )
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:19:3: ( rule )* EOF
            {
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:19:3: ( rule )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID||LA1_0==UNDERSCORE||LA1_0==NOT) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:19:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset126);
            	    rule1=rule();

            	    state._fsp--;

            	    stream_rule.add(rule1.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_ruleset130);  
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
            // 19:15: -> ^( RuleList ( rule )* )
            {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:19:18: ^( RuleList ( rule )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(RuleList, "RuleList"), root_1);

                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:19:29: ( rule )*
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:1: rule : pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) ;
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:5: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) )
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:22:3: pattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_pattern_in_rule150);
            pattern3=pattern();

            state._fsp--;

            stream_pattern.add(pattern3.getTree());
            ARROW4=(Token)match(input,ARROW,FOLLOW_ARROW_in_rule152);  
            stream_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule154);
            term5=term();

            state._fsp--;

            stream_term.add(term5.getTree());
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:22:22: ( IF cond= condition )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==IF) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:22:23: IF cond= condition
                    {
                    IF6=(Token)match(input,IF,FOLLOW_IF_in_rule157);  
                    stream_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule161);
                    cond=condition();

                    state._fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: term, cond, term, pattern, pattern
            // token labels: 
            // rule labels: retval, cond
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 23:5: -> { cond == null }? ^( Rule pattern term )
            if ( cond == null ) {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:23:26: ^( Rule pattern term )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Rule, "Rule"), root_1);

                adaptor.addChild(root_1, stream_pattern.nextTree());
                adaptor.addChild(root_1, stream_term.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 24:5: -> ^( ConditionalRule pattern term $cond)
            {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:8: ^( ConditionalRule pattern term $cond)
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:26:1: graphruleset : ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) ;
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:26:13: ( ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) )
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:27:3: ( graphrule )* EOF
            {
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:27:3: ( graphrule )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID||(LA3_0>=INT && LA3_0<=STRING)||LA3_0==AMPERCENT) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:27:4: graphrule
            	    {
            	    pushFollow(FOLLOW_graphrule_in_graphruleset206);
            	    graphrule7=graphrule();

            	    state._fsp--;

            	    stream_graphrule.add(graphrule7.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            EOF8=(Token)match(input,EOF,FOLLOW_EOF_in_graphruleset210);  
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
            // 27:20: -> ^( RuleList ( graphrule )* )
            {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:27:23: ^( RuleList ( graphrule )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(RuleList, "RuleList"), root_1);

                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:27:34: ( graphrule )*
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:1: graphrule : lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) ;
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:10: (lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) )
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:30:3: lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )?
            {
            pushFollow(FOLLOW_labelledpattern_in_graphrule232);
            lhs=labelledpattern();

            state._fsp--;

            stream_labelledpattern.add(lhs.getTree());
            ARROW9=(Token)match(input,ARROW,FOLLOW_ARROW_in_graphrule234);  
            stream_ARROW.add(ARROW9);

            pushFollow(FOLLOW_labelledpattern_in_graphrule238);
            rhs=labelledpattern();

            state._fsp--;

            stream_labelledpattern.add(rhs.getTree());
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:30:49: ( IF cond= condition )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==IF) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:30:50: IF cond= condition
                    {
                    IF10=(Token)match(input,IF,FOLLOW_IF_in_graphrule241);  
                    stream_IF.add(IF10);

                    pushFollow(FOLLOW_condition_in_graphrule245);
                    cond=condition();

                    state._fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: rhs, rhs, lhs, cond, lhs
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
            // 31:5: -> { cond == null }? ^( Rule $lhs $rhs)
            if ( cond == null ) {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:31:26: ^( Rule $lhs $rhs)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Rule, "Rule"), root_1);

                adaptor.addChild(root_1, stream_lhs.nextTree());
                adaptor.addChild(root_1, stream_rhs.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 32:5: -> ^( ConditionalRule $lhs $rhs $cond)
            {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:8: ^( ConditionalRule $lhs $rhs $cond)
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:34:1: condition : cond= andcondition (or= OR andcondition )* -> {or!=null}? ^( CondOr ( andcondition )* ) -> $cond;
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:34:10: (cond= andcondition (or= OR andcondition )* -> {or!=null}? ^( CondOr ( andcondition )* ) -> $cond)
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:35:3: cond= andcondition (or= OR andcondition )*
            {
            pushFollow(FOLLOW_andcondition_in_condition295);
            cond=andcondition();

            state._fsp--;

            stream_andcondition.add(cond.getTree());
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:35:21: (or= OR andcondition )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==OR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:35:22: or= OR andcondition
            	    {
            	    or=(Token)match(input,OR,FOLLOW_OR_in_condition300);  
            	    stream_OR.add(or);

            	    pushFollow(FOLLOW_andcondition_in_condition302);
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
            // elements: cond, andcondition
            // token labels: 
            // rule labels: retval, cond
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"rule cond",cond!=null?cond.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 36:3: -> {or!=null}? ^( CondOr ( andcondition )* )
            if (or!=null) {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:36:18: ^( CondOr ( andcondition )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondOr, "CondOr"), root_1);

                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:36:27: ( andcondition )*
                while ( stream_andcondition.hasNext() ) {
                    adaptor.addChild(root_1, stream_andcondition.nextTree());

                }
                stream_andcondition.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 37:3: -> $cond
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:39:1: andcondition : cond= simplecondition (and= AND simplecondition )* -> {and!=null}? ^( CondAnd ( simplecondition )* ) -> $cond;
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:39:13: (cond= simplecondition (and= AND simplecondition )* -> {and!=null}? ^( CondAnd ( simplecondition )* ) -> $cond)
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:40:3: cond= simplecondition (and= AND simplecondition )*
            {
            pushFollow(FOLLOW_simplecondition_in_andcondition335);
            cond=simplecondition();

            state._fsp--;

            stream_simplecondition.add(cond.getTree());
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:40:24: (and= AND simplecondition )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==AND) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:40:25: and= AND simplecondition
            	    {
            	    and=(Token)match(input,AND,FOLLOW_AND_in_andcondition340);  
            	    stream_AND.add(and);

            	    pushFollow(FOLLOW_simplecondition_in_andcondition342);
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
            // 41:3: -> {and!=null}? ^( CondAnd ( simplecondition )* )
            if (and!=null) {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:41:19: ^( CondAnd ( simplecondition )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondAnd, "CondAnd"), root_1);

                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:41:29: ( simplecondition )*
                while ( stream_simplecondition.hasNext() ) {
                    adaptor.addChild(root_1, stream_simplecondition.nextTree());

                }
                stream_simplecondition.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 42:3: -> $cond
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:44:1: simplecondition : (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term ) -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> ^( CondMatch $p1 $p8) | LPAR cond= condition RPAR -> $cond);
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:44:16: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term ) -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> ^( CondMatch $p1 $p8) | LPAR cond= condition RPAR -> $cond)
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
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:45:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term )
                    {
                    pushFollow(FOLLOW_term_in_simplecondition375);
                    p1=term();

                    state._fsp--;

                    stream_term.add(p1.getTree());
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:45:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term )
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
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:45:12: EQUALS p2= term
                            {
                            EQUALS13=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_simplecondition378);  
                            stream_EQUALS.add(EQUALS13);

                            pushFollow(FOLLOW_term_in_simplecondition382);
                            p2=term();

                            state._fsp--;

                            stream_term.add(p2.getTree());

                            }
                            break;
                        case 2 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:46:13: NOTEQUALS p3= term
                            {
                            NOTEQUALS14=(Token)match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_simplecondition396);  
                            stream_NOTEQUALS.add(NOTEQUALS14);

                            pushFollow(FOLLOW_term_in_simplecondition400);
                            p3=term();

                            state._fsp--;

                            stream_term.add(p3.getTree());

                            }
                            break;
                        case 3 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:13: LEQ p4= term
                            {
                            LEQ15=(Token)match(input,LEQ,FOLLOW_LEQ_in_simplecondition414);  
                            stream_LEQ.add(LEQ15);

                            pushFollow(FOLLOW_term_in_simplecondition418);
                            p4=term();

                            state._fsp--;

                            stream_term.add(p4.getTree());

                            }
                            break;
                        case 4 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:48:13: LT p5= term
                            {
                            LT16=(Token)match(input,LT,FOLLOW_LT_in_simplecondition432);  
                            stream_LT.add(LT16);

                            pushFollow(FOLLOW_term_in_simplecondition436);
                            p5=term();

                            state._fsp--;

                            stream_term.add(p5.getTree());

                            }
                            break;
                        case 5 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:49:13: GEQ p6= term
                            {
                            GEQ17=(Token)match(input,GEQ,FOLLOW_GEQ_in_simplecondition450);  
                            stream_GEQ.add(GEQ17);

                            pushFollow(FOLLOW_term_in_simplecondition454);
                            p6=term();

                            state._fsp--;

                            stream_term.add(p6.getTree());

                            }
                            break;
                        case 6 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:50:13: GT p7= term
                            {
                            GT18=(Token)match(input,GT,FOLLOW_GT_in_simplecondition468);  
                            stream_GT.add(GT18);

                            pushFollow(FOLLOW_term_in_simplecondition472);
                            p7=term();

                            state._fsp--;

                            stream_term.add(p7.getTree());

                            }
                            break;
                        case 7 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:51:13: MATCH p8= term
                            {
                            MATCH19=(Token)match(input,MATCH,FOLLOW_MATCH_in_simplecondition486);  
                            stream_MATCH.add(MATCH19);

                            pushFollow(FOLLOW_term_in_simplecondition490);
                            p8=term();

                            state._fsp--;

                            stream_term.add(p8.getTree());

                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: p6, p1, p1, p4, p2, p1, p1, p5, p1, p8, p1, p1, p7, p3
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
                    // 53:5: -> {p2!=null}? ^( CondEquals $p1 $p2)
                    if (p2!=null) {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:53:20: ^( CondEquals $p1 $p2)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondEquals, "CondEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 54:5: -> {p3!=null}? ^( CondNotEquals $p1 $p3)
                    if (p3!=null) {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:54:20: ^( CondNotEquals $p1 $p3)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondNotEquals, "CondNotEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p3.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 55:5: -> {p4!=null}? ^( CondLessEquals $p1 $p4)
                    if (p4!=null) {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:20: ^( CondLessEquals $p1 $p4)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondLessEquals, "CondLessEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p4.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 56:5: -> {p5!=null}? ^( CondLessThan $p1 $p5)
                    if (p5!=null) {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:20: ^( CondLessThan $p1 $p5)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondLessThan, "CondLessThan"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p5.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 57:5: -> {p6!=null}? ^( CondGreaterEquals $p1 $p6)
                    if (p6!=null) {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:57:20: ^( CondGreaterEquals $p1 $p6)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondGreaterEquals, "CondGreaterEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p6.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 58:5: -> {p7!=null}? ^( CondGreaterThan $p1 $p7)
                    if (p7!=null) {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:58:20: ^( CondGreaterThan $p1 $p7)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CondGreaterThan, "CondGreaterThan"), root_1);

                        adaptor.addChild(root_1, stream_p1.nextTree());
                        adaptor.addChild(root_1, stream_p7.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 59:5: -> ^( CondMatch $p1 $p8)
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:59:8: ^( CondMatch $p1 $p8)
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
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:60:5: LPAR cond= condition RPAR
                    {
                    LPAR20=(Token)match(input,LPAR,FOLLOW_LPAR_in_simplecondition632);  
                    stream_LPAR.add(LPAR20);

                    pushFollow(FOLLOW_condition_in_simplecondition636);
                    cond=condition();

                    state._fsp--;

                    stream_condition.add(cond.getTree());
                    RPAR21=(Token)match(input,RPAR,FOLLOW_RPAR_in_simplecondition638);  
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
                    // 61:3: -> $cond
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:63:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) | NOT pattern -> ^( Anti pattern ) );
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:63:8: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) | NOT pattern -> ^( Anti pattern ) )
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
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:3: ID LPAR ( term ( COMA term )* )? RPAR
                    {
                    ID22=(Token)match(input,ID,FOLLOW_ID_in_pattern654);  
                    stream_ID.add(ID22);

                    LPAR23=(Token)match(input,LPAR,FOLLOW_LPAR_in_pattern656);  
                    stream_LPAR.add(LPAR23);

                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:11: ( term ( COMA term )* )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==ID||LA10_0==UNDERSCORE||(LA10_0>=NOT && LA10_0<=STRING)) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:12: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern659);
                            term24=term();

                            state._fsp--;

                            stream_term.add(term24.getTree());
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:17: ( COMA term )*
                            loop9:
                            do {
                                int alt9=2;
                                int LA9_0 = input.LA(1);

                                if ( (LA9_0==COMA) ) {
                                    alt9=1;
                                }


                                switch (alt9) {
                            	case 1 :
                            	    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:18: COMA term
                            	    {
                            	    COMA25=(Token)match(input,COMA,FOLLOW_COMA_in_pattern662);  
                            	    stream_COMA.add(COMA25);

                            	    pushFollow(FOLLOW_term_in_pattern664);
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

                    RPAR27=(Token)match(input,RPAR,FOLLOW_RPAR_in_pattern670);  
                    stream_RPAR.add(RPAR27);



                    // AST REWRITE
                    // elements: ID, term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 64:37: -> ^( Appl ID ^( TermList ( term )* ) )
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:40: ^( Appl ID ^( TermList ( term )* ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Appl, "Appl"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:50: ^( TermList ( term )* )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(TermList, "TermList"), root_2);

                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:61: ( term )*
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
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:5: (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR
                    {
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:5: (varname= ID )
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:6: varname= ID
                    {
                    varname=(Token)match(input,ID,FOLLOW_ID_in_pattern694);  
                    stream_ID.add(varname);


                    }

                    AT28=(Token)match(input,AT,FOLLOW_AT_in_pattern697);  
                    stream_AT.add(AT28);

                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:21: (funname= ID )
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:22: funname= ID
                    {
                    funname=(Token)match(input,ID,FOLLOW_ID_in_pattern702);  
                    stream_ID.add(funname);


                    }

                    LPAR29=(Token)match(input,LPAR,FOLLOW_LPAR_in_pattern705);  
                    stream_LPAR.add(LPAR29);

                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:39: ( term ( COMA term )* )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==ID||LA12_0==UNDERSCORE||(LA12_0>=NOT && LA12_0<=STRING)) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:40: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern708);
                            term30=term();

                            state._fsp--;

                            stream_term.add(term30.getTree());
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:45: ( COMA term )*
                            loop11:
                            do {
                                int alt11=2;
                                int LA11_0 = input.LA(1);

                                if ( (LA11_0==COMA) ) {
                                    alt11=1;
                                }


                                switch (alt11) {
                            	case 1 :
                            	    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:46: COMA term
                            	    {
                            	    COMA31=(Token)match(input,COMA,FOLLOW_COMA_in_pattern711);  
                            	    stream_COMA.add(COMA31);

                            	    pushFollow(FOLLOW_term_in_pattern713);
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

                    RPAR33=(Token)match(input,RPAR,FOLLOW_RPAR_in_pattern719);  
                    stream_RPAR.add(RPAR33);



                    // AST REWRITE
                    // elements: funname, term, varname
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
                    // 65:65: -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:68: ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(At, "At"), root_1);

                        adaptor.addChild(root_1, stream_varname.nextNode());
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:82: ^( Appl $funname ^( TermList ( term )* ) )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Appl, "Appl"), root_2);

                        adaptor.addChild(root_2, stream_funname.nextNode());
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:98: ^( TermList ( term )* )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(TermList, "TermList"), root_3);

                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:109: ( term )*
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
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:5: UNDERSCORE
                    {
                    UNDERSCORE34=(Token)match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_pattern748);  
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
                    // 66:16: -> ^( UnnamedVar )
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:19: ^( UnnamedVar )
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
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:5: UNDERSCORE STAR
                    {
                    UNDERSCORE35=(Token)match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_pattern760);  
                    stream_UNDERSCORE.add(UNDERSCORE35);

                    STAR36=(Token)match(input,STAR,FOLLOW_STAR_in_pattern762);  
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
                    // 67:21: -> ^( UnnamedVarStar )
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:24: ^( UnnamedVarStar )
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
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:68:5: NOT pattern
                    {
                    NOT37=(Token)match(input,NOT,FOLLOW_NOT_in_pattern774);  
                    stream_NOT.add(NOT37);

                    pushFollow(FOLLOW_pattern_in_pattern776);
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
                    // 68:17: -> ^( Anti pattern )
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:68:20: ^( Anti pattern )
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:70:1: term : ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin );
    public final RuleParser.term_return term() throws RecognitionException {
        RuleParser.term_return retval = new RuleParser.term_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token s=null;
        Token ID40=null;
        RuleParser.pattern_return pattern39 = null;

        RuleParser.builtin_return builtin41 = null;


        Tree s_tree=null;
        Tree ID40_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:70:5: ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin )
            int alt15=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA15_1 = input.LA(2);

                if ( (LA15_1==LPAR||LA15_1==AT) ) {
                    alt15=1;
                }
                else if ( (LA15_1==EOF||(LA15_1>=IF && LA15_1<=MATCH)||(LA15_1>=RPAR && LA15_1<=COMA)||(LA15_1>=UNDERSCORE && LA15_1<=STRING)||LA15_1==AMPERCENT) ) {
                    alt15=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }
                }
                break;
            case UNDERSCORE:
            case NOT:
                {
                alt15=1;
                }
                break;
            case INT:
            case STRING:
                {
                alt15=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }

            switch (alt15) {
                case 1 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:71:3: pattern
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term793);
                    pattern39=pattern();

                    state._fsp--;

                    adaptor.addChild(root_0, pattern39.getTree());

                    }
                    break;
                case 2 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:5: ID (s= STAR )?
                    {
                    ID40=(Token)match(input,ID,FOLLOW_ID_in_term801);  
                    stream_ID.add(ID40);

                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:8: (s= STAR )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==STAR) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:9: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_term806);  
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
                    // 73:18: -> {null==s}? ^( Var ID )
                    if (null==s) {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:32: ^( Var ID )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Var, "Var"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 73:42: -> ^( VarStar ID )
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:44: ^( VarStar ID )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(VarStar, "VarStar"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:74:5: builtin
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term831);
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:76:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:76:8: ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==INT) ) {
                alt16=1;
            }
            else if ( (LA16_0==STRING) ) {
                alt16=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:77:3: INT
                    {
                    INT42=(Token)match(input,INT,FOLLOW_INT_in_builtin840);  
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
                    // 77:7: -> ^( BuiltinInt INT )
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:77:10: ^( BuiltinInt INT )
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
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:78:5: STRING
                    {
                    STRING43=(Token)match(input,STRING,FOLLOW_STRING_in_builtin854);  
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
                    // 78:12: -> ^( BuiltinString STRING )
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:78:15: ^( BuiltinString STRING )
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:81:1: labelledpattern : (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p;
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
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:81:16: ( (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p)
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:3: (namelabel= ID COLON )? p= graphpattern
            {
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:3: (namelabel= ID COLON )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==ID) ) {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==COLON) ) {
                    alt17=1;
                }
            }
            switch (alt17) {
                case 1 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:4: namelabel= ID COLON
                    {
                    namelabel=(Token)match(input,ID,FOLLOW_ID_in_labelledpattern875);  
                    stream_ID.add(namelabel);

                    COLON44=(Token)match(input,COLON,FOLLOW_COLON_in_labelledpattern877);  
                    stream_COLON.add(COLON44);


                    }
                    break;

            }

            pushFollow(FOLLOW_graphpattern_in_labelledpattern883);
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
            // 83:3: -> {$namelabel!=null}? ^( LabTerm $namelabel $p)
            if (namelabel!=null) {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:83:26: ^( LabTerm $namelabel $p)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(LabTerm, "LabTerm"), root_1);

                adaptor.addChild(root_1, stream_namelabel.nextNode());
                adaptor.addChild(root_1, stream_p.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 84:3: -> $p
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:86:1: graphpattern : ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref );
    public final RuleParser.graphpattern_return graphpattern() throws RecognitionException {
        RuleParser.graphpattern_return retval = new RuleParser.graphpattern_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token s=null;
        Token ID46=null;
        RuleParser.constructor_return constructor45 = null;

        RuleParser.builtin_return builtin47 = null;

        RuleParser.ref_return ref48 = null;


        Tree s_tree=null;
        Tree ID46_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:86:13: ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref )
            int alt19=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==LPAR) ) {
                    alt19=1;
                }
                else if ( (LA19_1==EOF||(LA19_1>=ARROW && LA19_1<=IF)||(LA19_1>=RPAR && LA19_1<=COMA)||LA19_1==STAR||(LA19_1>=INT && LA19_1<=STRING)||LA19_1==AMPERCENT) ) {
                    alt19=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT:
            case STRING:
                {
                alt19=3;
                }
                break;
            case AMPERCENT:
                {
                alt19=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:87:3: constructor
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_constructor_in_graphpattern915);
                    constructor45=constructor();

                    state._fsp--;

                    adaptor.addChild(root_0, constructor45.getTree());

                    }
                    break;
                case 2 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:5: ID (s= STAR )?
                    {
                    ID46=(Token)match(input,ID,FOLLOW_ID_in_graphpattern923);  
                    stream_ID.add(ID46);

                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:8: (s= STAR )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==STAR) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:9: s= STAR
                            {
                            s=(Token)match(input,STAR,FOLLOW_STAR_in_graphpattern928);  
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
                    // 89:18: -> {null==s}? ^( Var ID )
                    if (null==s) {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:32: ^( Var ID )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Var, "Var"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 89:42: -> ^( VarStar ID )
                    {
                        // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:44: ^( VarStar ID )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(VarStar, "VarStar"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:90:5: builtin
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_graphpattern953);
                    builtin47=builtin();

                    state._fsp--;

                    adaptor.addChild(root_0, builtin47.getTree());

                    }
                    break;
                case 4 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:91:5: ref
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_ref_in_graphpattern959);
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

    public static class ref_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ref"
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:93:1: ref : AMPERCENT ID -> ^( RefTerm ID ) ;
    public final RuleParser.ref_return ref() throws RecognitionException {
        RuleParser.ref_return retval = new RuleParser.ref_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token AMPERCENT49=null;
        Token ID50=null;

        Tree AMPERCENT49_tree=null;
        Tree ID50_tree=null;
        RewriteRuleTokenStream stream_AMPERCENT=new RewriteRuleTokenStream(adaptor,"token AMPERCENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:93:4: ( AMPERCENT ID -> ^( RefTerm ID ) )
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:94:3: AMPERCENT ID
            {
            AMPERCENT49=(Token)match(input,AMPERCENT,FOLLOW_AMPERCENT_in_ref968);  
            stream_AMPERCENT.add(AMPERCENT49);

            ID50=(Token)match(input,ID,FOLLOW_ID_in_ref970);  
            stream_ID.add(ID50);



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
            // 94:16: -> ^( RefTerm ID )
            {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:94:19: ^( RefTerm ID )
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
    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:1: constructor : ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) ;
    public final RuleParser.constructor_return constructor() throws RecognitionException {
        RuleParser.constructor_return retval = new RuleParser.constructor_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID51=null;
        Token LPAR52=null;
        Token COMA54=null;
        Token RPAR56=null;
        RuleParser.labelledpattern_return labelledpattern53 = null;

        RuleParser.labelledpattern_return labelledpattern55 = null;


        Tree ID51_tree=null;
        Tree LPAR52_tree=null;
        Tree COMA54_tree=null;
        Tree RPAR56_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        try {
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:12: ( ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) )
            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:97:3: ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR
            {
            ID51=(Token)match(input,ID,FOLLOW_ID_in_constructor987);  
            stream_ID.add(ID51);

            LPAR52=(Token)match(input,LPAR,FOLLOW_LPAR_in_constructor989);  
            stream_LPAR.add(LPAR52);

            // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:97:11: ( labelledpattern ( COMA labelledpattern )* )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==ID||(LA21_0>=INT && LA21_0<=STRING)||LA21_0==AMPERCENT) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:97:12: labelledpattern ( COMA labelledpattern )*
                    {
                    pushFollow(FOLLOW_labelledpattern_in_constructor992);
                    labelledpattern53=labelledpattern();

                    state._fsp--;

                    stream_labelledpattern.add(labelledpattern53.getTree());
                    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:97:28: ( COMA labelledpattern )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==COMA) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:97:29: COMA labelledpattern
                    	    {
                    	    COMA54=(Token)match(input,COMA,FOLLOW_COMA_in_constructor995);  
                    	    stream_COMA.add(COMA54);

                    	    pushFollow(FOLLOW_labelledpattern_in_constructor997);
                    	    labelledpattern55=labelledpattern();

                    	    state._fsp--;

                    	    stream_labelledpattern.add(labelledpattern55.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop20;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR56=(Token)match(input,RPAR,FOLLOW_RPAR_in_constructor1003);  
            stream_RPAR.add(RPAR56);



            // AST REWRITE
            // elements: labelledpattern, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 98:3: -> ^( Appl ID ^( TermList ( labelledpattern )* ) )
            {
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:98:6: ^( Appl ID ^( TermList ( labelledpattern )* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Appl, "Appl"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:98:16: ^( TermList ( labelledpattern )* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(TermList, "TermList"), root_2);

                // /Users/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:98:27: ( labelledpattern )*
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


 

    public static final BitSet FOLLOW_rule_in_ruleset126 = new BitSet(new long[]{0x0000A40000000000L});
    public static final BitSet FOLLOW_EOF_in_ruleset130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule150 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_ARROW_in_rule152 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_rule154 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_IF_in_rule157 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_condition_in_rule161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_graphrule_in_graphruleset206 = new BitSet(new long[]{0x000BA40000000000L});
    public static final BitSet FOLLOW_EOF_in_graphruleset210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule232 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_ARROW_in_graphrule234 = new BitSet(new long[]{0x000BA40000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule238 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_IF_in_graphrule241 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_condition_in_graphrule245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andcondition_in_condition295 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_OR_in_condition300 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_andcondition_in_condition302 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_simplecondition_in_andcondition335 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_AND_in_andcondition340 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_simplecondition_in_andcondition342 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_term_in_simplecondition375 = new BitSet(new long[]{0x000000FE00000000L});
    public static final BitSet FOLLOW_EQUALS_in_simplecondition378 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_simplecondition396 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEQ_in_simplecondition414 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_simplecondition432 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition436 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GEQ_in_simplecondition450 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_simplecondition468 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MATCH_in_simplecondition486 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_simplecondition632 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_condition_in_simplecondition636 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RPAR_in_simplecondition638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern654 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern656 = new BitSet(new long[]{0x0003A60000000000L});
    public static final BitSet FOLLOW_term_in_pattern659 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern662 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_pattern664 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern694 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_AT_in_pattern697 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_ID_in_pattern702 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern705 = new BitSet(new long[]{0x0003A60000000000L});
    public static final BitSet FOLLOW_term_in_pattern708 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern711 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_pattern713 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_pattern748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_pattern760 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_STAR_in_pattern762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_pattern774 = new BitSet(new long[]{0x0000A40000000000L});
    public static final BitSet FOLLOW_pattern_in_pattern776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term801 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_STAR_in_term806 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_builtin840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_builtin854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labelledpattern875 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_COLON_in_labelledpattern877 = new BitSet(new long[]{0x000BA40000000000L});
    public static final BitSet FOLLOW_graphpattern_in_labelledpattern883 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_in_graphpattern915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_graphpattern923 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_STAR_in_graphpattern928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_graphpattern953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ref_in_graphpattern959 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPERCENT_in_ref968 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_ID_in_ref970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constructor987 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_LPAR_in_constructor989 = new BitSet(new long[]{0x000BA60000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor992 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_COMA_in_constructor995 = new BitSet(new long[]{0x000BA40000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor997 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_RPAR_in_constructor1003 = new BitSet(new long[]{0x0000000000000002L});

}