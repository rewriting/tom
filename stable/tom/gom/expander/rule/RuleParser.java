// $ANTLR 3.0 /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g 2008-03-25 23:12:10

package tom.gom.expander.rule;
import tom.gom.adt.rule.RuleTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CondEquals", "UnnamedVarStar", "LabTerm", "RefTerm", "Anti", "CondOr", "ConditionalRule", "Rule", "CondAnd", "RuleList", "Appl", "BuiltinInt", "BuiltinString", "CondMatch", "CondLessEquals", "VarStar", "TermList", "CondNotEquals", "PathTerm", "CondGreaterThan", "CondLessThan", "UnnamedVar", "CondGreaterEquals", "Var", "At", "ARROW", "IF", "OR", "AND", "EQUALS", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "MATCH", "LPAR", "RPAR", "ID", "COMA", "AT", "UNDERSCORE", "STAR", "NOT", "INT", "STRING", "COLON", "AMPERCENT", "ESC", "WS", "SLCOMMENT"
    };
    public static final int CondEquals=4;
    public static final int RefTerm=7;
    public static final int Anti=8;
    public static final int LPAR=40;
    public static final int ARROW=29;
    public static final int LEQ=35;
    public static final int GEQ=37;
    public static final int RuleList=13;
    public static final int OR=31;
    public static final int BuiltinInt=15;
    public static final int BuiltinString=16;
    public static final int AND=32;
    public static final int SLCOMMENT=54;
    public static final int CondLessEquals=18;
    public static final int INT=48;
    public static final int CondLessThan=24;
    public static final int UnnamedVar=25;
    public static final int CondGreaterEquals=26;
    public static final int Var=27;
    public static final int AT=44;
    public static final int At=28;
    public static final int ID=42;
    public static final int NOTEQUALS=34;
    public static final int UnnamedVarStar=5;
    public static final int RPAR=41;
    public static final int LabTerm=6;
    public static final int WS=53;
    public static final int STRING=49;
    public static final int CondOr=9;
    public static final int LT=36;
    public static final int ConditionalRule=10;
    public static final int GT=38;
    public static final int Rule=11;
    public static final int CondAnd=12;
    public static final int MATCH=39;
    public static final int ESC=52;
    public static final int Appl=14;
    public static final int EQUALS=33;
    public static final int AMPERCENT=51;
    public static final int CondMatch=17;
    public static final int VarStar=19;
    public static final int IF=30;
    public static final int EOF=-1;
    public static final int COLON=50;
    public static final int TermList=20;
    public static final int CondNotEquals=21;
    public static final int PathTerm=22;
    public static final int CondGreaterThan=23;
    public static final int COMA=43;
    public static final int STAR=46;
    public static final int NOT=47;
    public static final int UNDERSCORE=45;

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

                if ( (LA1_0==ID||LA1_0==UNDERSCORE||LA1_0==NOT) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset128);
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
            match(input,EOF,FOLLOW_EOF_in_ruleset132); 
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
            pushFollow(FOLLOW_pattern_in_rule152);
            pattern3=pattern();
            _fsp--;

            stream_pattern.add(pattern3.getTree());
            ARROW4=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_rule154); 
            stream_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule156);
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
                    match(input,IF,FOLLOW_IF_in_rule159); 
                    stream_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule163);
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
            pushFollow(FOLLOW_labelledpattern_in_graphrule234);
            lhs=labelledpattern();
            _fsp--;

            stream_labelledpattern.add(lhs.getTree());
            ARROW9=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_graphrule236); 
            stream_ARROW.add(ARROW9);

            pushFollow(FOLLOW_labelledpattern_in_graphrule240);
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
                    match(input,IF,FOLLOW_IF_in_graphrule243); 
                    stream_IF.add(IF10);

                    pushFollow(FOLLOW_condition_in_graphrule247);
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
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:36:1: condition : cond= andcondition (or= OR andcondition )* -> {or!=null}? ^( CondOr ( andcondition )* ) -> $cond;
    public final condition_return condition() throws RecognitionException {
        condition_return retval = new condition_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token or=null;
        andcondition_return cond = null;

        andcondition_return andcondition11 = null;


        RuleTree or_tree=null;
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
        RewriteRuleSubtreeStream stream_andcondition=new RewriteRuleSubtreeStream(adaptor,"rule andcondition");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:3: (cond= andcondition (or= OR andcondition )* -> {or!=null}? ^( CondOr ( andcondition )* ) -> $cond)
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:3: cond= andcondition (or= OR andcondition )*
            {
            pushFollow(FOLLOW_andcondition_in_condition297);
            cond=andcondition();
            _fsp--;

            stream_andcondition.add(cond.getTree());
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:21: (or= OR andcondition )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==OR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:22: or= OR andcondition
            	    {
            	    or=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_condition302); 
            	    stream_OR.add(or);

            	    pushFollow(FOLLOW_andcondition_in_condition304);
            	    andcondition11=andcondition();
            	    _fsp--;

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
            // rule labels: cond, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 38:3: -> {or!=null}? ^( CondOr ( andcondition )* )
            if (or!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:38:18: ^( CondOr ( andcondition )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondOr, "CondOr"), root_1);

                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:38:27: ( andcondition )*
                while ( stream_andcondition.hasNext() ) {
                    adaptor.addChild(root_1, stream_andcondition.next());

                }
                stream_andcondition.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 39:3: -> $cond
            {
                adaptor.addChild(root_0, stream_cond.next());

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

    public static class andcondition_return extends ParserRuleReturnScope {
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start andcondition
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:41:1: andcondition : cond= simplecondition (and= AND simplecondition )* -> {and!=null}? ^( CondAnd ( simplecondition )* ) -> $cond;
    public final andcondition_return andcondition() throws RecognitionException {
        andcondition_return retval = new andcondition_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token and=null;
        simplecondition_return cond = null;

        simplecondition_return simplecondition12 = null;


        RuleTree and_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_simplecondition=new RewriteRuleSubtreeStream(adaptor,"rule simplecondition");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:3: (cond= simplecondition (and= AND simplecondition )* -> {and!=null}? ^( CondAnd ( simplecondition )* ) -> $cond)
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:3: cond= simplecondition (and= AND simplecondition )*
            {
            pushFollow(FOLLOW_simplecondition_in_andcondition337);
            cond=simplecondition();
            _fsp--;

            stream_simplecondition.add(cond.getTree());
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:24: (and= AND simplecondition )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==AND) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:25: and= AND simplecondition
            	    {
            	    and=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andcondition342); 
            	    stream_AND.add(and);

            	    pushFollow(FOLLOW_simplecondition_in_andcondition344);
            	    simplecondition12=simplecondition();
            	    _fsp--;

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
            // rule labels: cond, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 43:3: -> {and!=null}? ^( CondAnd ( simplecondition )* )
            if (and!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:43:19: ^( CondAnd ( simplecondition )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondAnd, "CondAnd"), root_1);

                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:43:29: ( simplecondition )*
                while ( stream_simplecondition.hasNext() ) {
                    adaptor.addChild(root_1, stream_simplecondition.next());

                }
                stream_simplecondition.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 44:3: -> $cond
            {
                adaptor.addChild(root_0, stream_cond.next());

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
    // $ANTLR end andcondition

    public static class simplecondition_return extends ParserRuleReturnScope {
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start simplecondition
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:46:1: simplecondition : (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term ) -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> ^( CondMatch $p1 $p8) | LPAR cond= condition RPAR -> $cond);
    public final simplecondition_return simplecondition() throws RecognitionException {
        simplecondition_return retval = new simplecondition_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token EQUALS13=null;
        Token NOTEQUALS14=null;
        Token LEQ15=null;
        Token LT16=null;
        Token GEQ17=null;
        Token GT18=null;
        Token MATCH19=null;
        Token LPAR20=null;
        Token RPAR21=null;
        term_return p1 = null;

        term_return p2 = null;

        term_return p3 = null;

        term_return p4 = null;

        term_return p5 = null;

        term_return p6 = null;

        term_return p7 = null;

        term_return p8 = null;

        condition_return cond = null;


        RuleTree EQUALS13_tree=null;
        RuleTree NOTEQUALS14_tree=null;
        RuleTree LEQ15_tree=null;
        RuleTree LT16_tree=null;
        RuleTree GEQ17_tree=null;
        RuleTree GT18_tree=null;
        RuleTree MATCH19_tree=null;
        RuleTree LPAR20_tree=null;
        RuleTree RPAR21_tree=null;
        RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
        RewriteRuleTokenStream stream_LEQ=new RewriteRuleTokenStream(adaptor,"token LEQ");
        RewriteRuleTokenStream stream_LT=new RewriteRuleTokenStream(adaptor,"token LT");
        RewriteRuleTokenStream stream_GT=new RewriteRuleTokenStream(adaptor,"token GT");
        RewriteRuleTokenStream stream_NOTEQUALS=new RewriteRuleTokenStream(adaptor,"token NOTEQUALS");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_GEQ=new RewriteRuleTokenStream(adaptor,"token GEQ");
        RewriteRuleTokenStream stream_MATCH=new RewriteRuleTokenStream(adaptor,"token MATCH");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:3: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term ) -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> ^( CondMatch $p1 $p8) | LPAR cond= condition RPAR -> $cond)
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
                    new NoViableAltException("46:1: simplecondition : (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term ) -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> ^( CondMatch $p1 $p8) | LPAR cond= condition RPAR -> $cond);", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term )
                    {
                    pushFollow(FOLLOW_term_in_simplecondition377);
                    p1=term();
                    _fsp--;

                    stream_term.add(p1.getTree());
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term )
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
                            new NoViableAltException("47:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | MATCH p8= term )", 7, 0, input);

                        throw nvae;
                    }

                    switch (alt7) {
                        case 1 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:12: EQUALS p2= term
                            {
                            EQUALS13=(Token)input.LT(1);
                            match(input,EQUALS,FOLLOW_EQUALS_in_simplecondition380); 
                            stream_EQUALS.add(EQUALS13);

                            pushFollow(FOLLOW_term_in_simplecondition384);
                            p2=term();
                            _fsp--;

                            stream_term.add(p2.getTree());

                            }
                            break;
                        case 2 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:48:13: NOTEQUALS p3= term
                            {
                            NOTEQUALS14=(Token)input.LT(1);
                            match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_simplecondition398); 
                            stream_NOTEQUALS.add(NOTEQUALS14);

                            pushFollow(FOLLOW_term_in_simplecondition402);
                            p3=term();
                            _fsp--;

                            stream_term.add(p3.getTree());

                            }
                            break;
                        case 3 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:49:13: LEQ p4= term
                            {
                            LEQ15=(Token)input.LT(1);
                            match(input,LEQ,FOLLOW_LEQ_in_simplecondition416); 
                            stream_LEQ.add(LEQ15);

                            pushFollow(FOLLOW_term_in_simplecondition420);
                            p4=term();
                            _fsp--;

                            stream_term.add(p4.getTree());

                            }
                            break;
                        case 4 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:50:13: LT p5= term
                            {
                            LT16=(Token)input.LT(1);
                            match(input,LT,FOLLOW_LT_in_simplecondition434); 
                            stream_LT.add(LT16);

                            pushFollow(FOLLOW_term_in_simplecondition438);
                            p5=term();
                            _fsp--;

                            stream_term.add(p5.getTree());

                            }
                            break;
                        case 5 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:51:13: GEQ p6= term
                            {
                            GEQ17=(Token)input.LT(1);
                            match(input,GEQ,FOLLOW_GEQ_in_simplecondition452); 
                            stream_GEQ.add(GEQ17);

                            pushFollow(FOLLOW_term_in_simplecondition456);
                            p6=term();
                            _fsp--;

                            stream_term.add(p6.getTree());

                            }
                            break;
                        case 6 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:52:13: GT p7= term
                            {
                            GT18=(Token)input.LT(1);
                            match(input,GT,FOLLOW_GT_in_simplecondition470); 
                            stream_GT.add(GT18);

                            pushFollow(FOLLOW_term_in_simplecondition474);
                            p7=term();
                            _fsp--;

                            stream_term.add(p7.getTree());

                            }
                            break;
                        case 7 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:53:13: MATCH p8= term
                            {
                            MATCH19=(Token)input.LT(1);
                            match(input,MATCH,FOLLOW_MATCH_in_simplecondition488); 
                            stream_MATCH.add(MATCH19);

                            pushFollow(FOLLOW_term_in_simplecondition492);
                            p8=term();
                            _fsp--;

                            stream_term.add(p8.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: p1, p4, p1, p1, p1, p1, p3, p1, p8, p2, p7, p6, p1, p5
                    // token labels: 
                    // rule labels: p6, p8, p3, p1, p5, p7, p4, p2, retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_p6=new RewriteRuleSubtreeStream(adaptor,"token p6",p6!=null?p6.tree:null);
                    RewriteRuleSubtreeStream stream_p8=new RewriteRuleSubtreeStream(adaptor,"token p8",p8!=null?p8.tree:null);
                    RewriteRuleSubtreeStream stream_p3=new RewriteRuleSubtreeStream(adaptor,"token p3",p3!=null?p3.tree:null);
                    RewriteRuleSubtreeStream stream_p1=new RewriteRuleSubtreeStream(adaptor,"token p1",p1!=null?p1.tree:null);
                    RewriteRuleSubtreeStream stream_p5=new RewriteRuleSubtreeStream(adaptor,"token p5",p5!=null?p5.tree:null);
                    RewriteRuleSubtreeStream stream_p7=new RewriteRuleSubtreeStream(adaptor,"token p7",p7!=null?p7.tree:null);
                    RewriteRuleSubtreeStream stream_p4=new RewriteRuleSubtreeStream(adaptor,"token p4",p4!=null?p4.tree:null);
                    RewriteRuleSubtreeStream stream_p2=new RewriteRuleSubtreeStream(adaptor,"token p2",p2!=null?p2.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 55:5: -> {p2!=null}? ^( CondEquals $p1 $p2)
                    if (p2!=null) {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:20: ^( CondEquals $p1 $p2)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondEquals, "CondEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p2.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 56:5: -> {p3!=null}? ^( CondNotEquals $p1 $p3)
                    if (p3!=null) {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:20: ^( CondNotEquals $p1 $p3)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondNotEquals, "CondNotEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p3.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 57:5: -> {p4!=null}? ^( CondLessEquals $p1 $p4)
                    if (p4!=null) {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:57:20: ^( CondLessEquals $p1 $p4)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondLessEquals, "CondLessEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p4.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 58:5: -> {p5!=null}? ^( CondLessThan $p1 $p5)
                    if (p5!=null) {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:58:20: ^( CondLessThan $p1 $p5)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondLessThan, "CondLessThan"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p5.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 59:5: -> {p6!=null}? ^( CondGreaterEquals $p1 $p6)
                    if (p6!=null) {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:59:20: ^( CondGreaterEquals $p1 $p6)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondGreaterEquals, "CondGreaterEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p6.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 60:5: -> {p7!=null}? ^( CondGreaterThan $p1 $p7)
                    if (p7!=null) {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:60:20: ^( CondGreaterThan $p1 $p7)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondGreaterThan, "CondGreaterThan"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p7.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 61:5: -> ^( CondMatch $p1 $p8)
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:61:8: ^( CondMatch $p1 $p8)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondMatch, "CondMatch"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p8.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:62:5: LPAR cond= condition RPAR
                    {
                    LPAR20=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_simplecondition634); 
                    stream_LPAR.add(LPAR20);

                    pushFollow(FOLLOW_condition_in_simplecondition638);
                    cond=condition();
                    _fsp--;

                    stream_condition.add(cond.getTree());
                    RPAR21=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_simplecondition640); 
                    stream_RPAR.add(RPAR21);


                    // AST REWRITE
                    // elements: cond
                    // token labels: 
                    // rule labels: cond, retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 63:3: -> $cond
                    {
                        adaptor.addChild(root_0, stream_cond.next());

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
    // $ANTLR end simplecondition

    public static class pattern_return extends ParserRuleReturnScope {
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start pattern
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) | NOT pattern -> ^( Anti pattern ) );
    public final pattern_return pattern() throws RecognitionException {
        pattern_return retval = new pattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

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
        term_return term24 = null;

        term_return term26 = null;

        term_return term30 = null;

        term_return term32 = null;

        pattern_return pattern38 = null;


        RuleTree varname_tree=null;
        RuleTree funname_tree=null;
        RuleTree ID22_tree=null;
        RuleTree LPAR23_tree=null;
        RuleTree COMA25_tree=null;
        RuleTree RPAR27_tree=null;
        RuleTree AT28_tree=null;
        RuleTree LPAR29_tree=null;
        RuleTree COMA31_tree=null;
        RuleTree RPAR33_tree=null;
        RuleTree UNDERSCORE34_tree=null;
        RuleTree UNDERSCORE35_tree=null;
        RuleTree STAR36_tree=null;
        RuleTree NOT37_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_UNDERSCORE=new RewriteRuleTokenStream(adaptor,"token UNDERSCORE");
        RewriteRuleTokenStream stream_AT=new RewriteRuleTokenStream(adaptor,"token AT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_pattern=new RewriteRuleSubtreeStream(adaptor,"rule pattern");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:3: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) | NOT pattern -> ^( Anti pattern ) )
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
                        new NoViableAltException("65:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) | NOT pattern -> ^( Anti pattern ) );", 13, 1, input);

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
                        new NoViableAltException("65:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) | NOT pattern -> ^( Anti pattern ) );", 13, 2, input);

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
                    new NoViableAltException("65:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) | NOT pattern -> ^( Anti pattern ) );", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:3: ID LPAR ( term ( COMA term )* )? RPAR
                    {
                    ID22=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_pattern656); 
                    stream_ID.add(ID22);

                    LPAR23=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_pattern658); 
                    stream_LPAR.add(LPAR23);

                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:11: ( term ( COMA term )* )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==ID||LA10_0==UNDERSCORE||(LA10_0>=NOT && LA10_0<=STRING)) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:12: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern661);
                            term24=term();
                            _fsp--;

                            stream_term.add(term24.getTree());
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:17: ( COMA term )*
                            loop9:
                            do {
                                int alt9=2;
                                int LA9_0 = input.LA(1);

                                if ( (LA9_0==COMA) ) {
                                    alt9=1;
                                }


                                switch (alt9) {
                            	case 1 :
                            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:18: COMA term
                            	    {
                            	    COMA25=(Token)input.LT(1);
                            	    match(input,COMA,FOLLOW_COMA_in_pattern664); 
                            	    stream_COMA.add(COMA25);

                            	    pushFollow(FOLLOW_term_in_pattern666);
                            	    term26=term();
                            	    _fsp--;

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

                    RPAR27=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_pattern672); 
                    stream_RPAR.add(RPAR27);


                    // AST REWRITE
                    // elements: ID, term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 66:37: -> ^( Appl ID ^( TermList ( term )* ) )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:40: ^( Appl ID ^( TermList ( term )* ) )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:50: ^( TermList ( term )* )
                        {
                        RuleTree root_2 = (RuleTree)adaptor.nil();
                        root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_2);

                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:61: ( term )*
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
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:5: (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR
                    {
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:5: (varname= ID )
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:6: varname= ID
                    {
                    varname=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_pattern696); 
                    stream_ID.add(varname);


                    }

                    AT28=(Token)input.LT(1);
                    match(input,AT,FOLLOW_AT_in_pattern699); 
                    stream_AT.add(AT28);

                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:21: (funname= ID )
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:22: funname= ID
                    {
                    funname=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_pattern704); 
                    stream_ID.add(funname);


                    }

                    LPAR29=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_pattern707); 
                    stream_LPAR.add(LPAR29);

                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:39: ( term ( COMA term )* )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==ID||LA12_0==UNDERSCORE||(LA12_0>=NOT && LA12_0<=STRING)) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:40: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern710);
                            term30=term();
                            _fsp--;

                            stream_term.add(term30.getTree());
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:45: ( COMA term )*
                            loop11:
                            do {
                                int alt11=2;
                                int LA11_0 = input.LA(1);

                                if ( (LA11_0==COMA) ) {
                                    alt11=1;
                                }


                                switch (alt11) {
                            	case 1 :
                            	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:46: COMA term
                            	    {
                            	    COMA31=(Token)input.LT(1);
                            	    match(input,COMA,FOLLOW_COMA_in_pattern713); 
                            	    stream_COMA.add(COMA31);

                            	    pushFollow(FOLLOW_term_in_pattern715);
                            	    term32=term();
                            	    _fsp--;

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

                    RPAR33=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_pattern721); 
                    stream_RPAR.add(RPAR33);


                    // AST REWRITE
                    // elements: varname, term, funname
                    // token labels: varname, funname
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_varname=new RewriteRuleTokenStream(adaptor,"token varname",varname);
                    RewriteRuleTokenStream stream_funname=new RewriteRuleTokenStream(adaptor,"token funname",funname);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 67:65: -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:68: ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(At, "At"), root_1);

                        adaptor.addChild(root_1, stream_varname.next());
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:82: ^( Appl $funname ^( TermList ( term )* ) )
                        {
                        RuleTree root_2 = (RuleTree)adaptor.nil();
                        root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_2);

                        adaptor.addChild(root_2, stream_funname.next());
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:98: ^( TermList ( term )* )
                        {
                        RuleTree root_3 = (RuleTree)adaptor.nil();
                        root_3 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_3);

                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:109: ( term )*
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
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:68:5: UNDERSCORE
                    {
                    UNDERSCORE34=(Token)input.LT(1);
                    match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_pattern750); 
                    stream_UNDERSCORE.add(UNDERSCORE34);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 68:16: -> ^( UnnamedVar )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:68:19: ^( UnnamedVar )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(UnnamedVar, "UnnamedVar"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 4 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:69:5: UNDERSCORE STAR
                    {
                    UNDERSCORE35=(Token)input.LT(1);
                    match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_pattern762); 
                    stream_UNDERSCORE.add(UNDERSCORE35);

                    STAR36=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_pattern764); 
                    stream_STAR.add(STAR36);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 69:21: -> ^( UnnamedVarStar )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:69:24: ^( UnnamedVarStar )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(UnnamedVarStar, "UnnamedVarStar"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 5 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:70:5: NOT pattern
                    {
                    NOT37=(Token)input.LT(1);
                    match(input,NOT,FOLLOW_NOT_in_pattern776); 
                    stream_NOT.add(NOT37);

                    pushFollow(FOLLOW_pattern_in_pattern778);
                    pattern38=pattern();
                    _fsp--;

                    stream_pattern.add(pattern38.getTree());

                    // AST REWRITE
                    // elements: pattern
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 70:17: -> ^( Anti pattern )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:70:20: ^( Anti pattern )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Anti, "Anti"), root_1);

                        adaptor.addChild(root_1, stream_pattern.next());

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
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:1: term : ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin );
    public final term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token s=null;
        Token ID40=null;
        pattern_return pattern39 = null;

        builtin_return builtin41 = null;


        RuleTree s_tree=null;
        RuleTree ID40_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:3: ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin )
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
                        new NoViableAltException("72:1: term : ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin );", 15, 1, input);

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
                    new NoViableAltException("72:1: term : ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin );", 15, 0, input);

                throw nvae;
            }

            switch (alt15) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:3: pattern
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term795);
                    pattern39=pattern();
                    _fsp--;

                    adaptor.addChild(root_0, pattern39.getTree());

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:75:5: ID (s= STAR )?
                    {
                    ID40=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_term803); 
                    stream_ID.add(ID40);

                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:75:8: (s= STAR )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==STAR) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:75:9: s= STAR
                            {
                            s=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_term808); 
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
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 75:18: -> {null==s}? ^( Var ID )
                    if (null==s) {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:75:32: ^( Var ID )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Var, "Var"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 75:42: -> ^( VarStar ID )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:75:44: ^( VarStar ID )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(VarStar, "VarStar"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 3 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:76:5: builtin
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term833);
                    builtin41=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin41.getTree());

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
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:78:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );
    public final builtin_return builtin() throws RecognitionException {
        builtin_return retval = new builtin_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token INT42=null;
        Token STRING43=null;

        RuleTree INT42_tree=null;
        RuleTree STRING43_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:79:3: ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) )
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
                    new NoViableAltException("78:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:79:3: INT
                    {
                    INT42=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_builtin842); 
                    stream_INT.add(INT42);


                    // AST REWRITE
                    // elements: INT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 79:7: -> ^( BuiltinInt INT )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:79:10: ^( BuiltinInt INT )
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
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:80:5: STRING
                    {
                    STRING43=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_builtin856); 
                    stream_STRING.add(STRING43);


                    // AST REWRITE
                    // elements: STRING
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 80:12: -> ^( BuiltinString STRING )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:80:15: ^( BuiltinString STRING )
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
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:83:1: labelledpattern : (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p;
    public final labelledpattern_return labelledpattern() throws RecognitionException {
        labelledpattern_return retval = new labelledpattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token namelabel=null;
        Token COLON44=null;
        graphpattern_return p = null;


        RuleTree namelabel_tree=null;
        RuleTree COLON44_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_graphpattern=new RewriteRuleSubtreeStream(adaptor,"rule graphpattern");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:84:3: ( (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p)
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:84:3: (namelabel= ID COLON )? p= graphpattern
            {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:84:3: (namelabel= ID COLON )?
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
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:84:4: namelabel= ID COLON
                    {
                    namelabel=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_labelledpattern877); 
                    stream_ID.add(namelabel);

                    COLON44=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_labelledpattern879); 
                    stream_COLON.add(COLON44);


                    }
                    break;

            }

            pushFollow(FOLLOW_graphpattern_in_labelledpattern885);
            p=graphpattern();
            _fsp--;

            stream_graphpattern.add(p.getTree());

            // AST REWRITE
            // elements: p, p, namelabel
            // token labels: namelabel
            // rule labels: p, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_namelabel=new RewriteRuleTokenStream(adaptor,"token namelabel",namelabel);
            RewriteRuleSubtreeStream stream_p=new RewriteRuleSubtreeStream(adaptor,"token p",p!=null?p.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 85:3: -> {$namelabel!=null}? ^( LabTerm $namelabel $p)
            if (namelabel!=null) {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:85:26: ^( LabTerm $namelabel $p)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(LabTerm, "LabTerm"), root_1);

                adaptor.addChild(root_1, stream_namelabel.next());
                adaptor.addChild(root_1, stream_p.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 86:3: -> $p
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
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:88:1: graphpattern : ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref );
    public final graphpattern_return graphpattern() throws RecognitionException {
        graphpattern_return retval = new graphpattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token s=null;
        Token ID46=null;
        constructor_return constructor45 = null;

        builtin_return builtin47 = null;

        ref_return ref48 = null;


        RuleTree s_tree=null;
        RuleTree ID46_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:3: ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref )
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
                        new NoViableAltException("88:1: graphpattern : ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref );", 19, 1, input);

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
                    new NoViableAltException("88:1: graphpattern : ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref );", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:3: constructor
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_constructor_in_graphpattern917);
                    constructor45=constructor();
                    _fsp--;

                    adaptor.addChild(root_0, constructor45.getTree());

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:91:5: ID (s= STAR )?
                    {
                    ID46=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_graphpattern925); 
                    stream_ID.add(ID46);

                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:91:8: (s= STAR )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==STAR) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:91:9: s= STAR
                            {
                            s=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_graphpattern930); 
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
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 91:18: -> {null==s}? ^( Var ID )
                    if (null==s) {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:91:32: ^( Var ID )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Var, "Var"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 91:42: -> ^( VarStar ID )
                    {
                        // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:91:44: ^( VarStar ID )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(VarStar, "VarStar"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 3 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:92:5: builtin
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_graphpattern955);
                    builtin47=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin47.getTree());

                    }
                    break;
                case 4 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:93:5: ref
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_ref_in_graphpattern961);
                    ref48=ref();
                    _fsp--;

                    adaptor.addChild(root_0, ref48.getTree());

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
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:95:1: ref : AMPERCENT ID -> ^( RefTerm ID ) ;
    public final ref_return ref() throws RecognitionException {
        ref_return retval = new ref_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token AMPERCENT49=null;
        Token ID50=null;

        RuleTree AMPERCENT49_tree=null;
        RuleTree ID50_tree=null;
        RewriteRuleTokenStream stream_AMPERCENT=new RewriteRuleTokenStream(adaptor,"token AMPERCENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:3: ( AMPERCENT ID -> ^( RefTerm ID ) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:3: AMPERCENT ID
            {
            AMPERCENT49=(Token)input.LT(1);
            match(input,AMPERCENT,FOLLOW_AMPERCENT_in_ref970); 
            stream_AMPERCENT.add(AMPERCENT49);

            ID50=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_ref972); 
            stream_ID.add(ID50);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 96:16: -> ^( RefTerm ID )
            {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:19: ^( RefTerm ID )
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
    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:98:1: constructor : ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) ;
    public final constructor_return constructor() throws RecognitionException {
        constructor_return retval = new constructor_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ID51=null;
        Token LPAR52=null;
        Token COMA54=null;
        Token RPAR56=null;
        labelledpattern_return labelledpattern53 = null;

        labelledpattern_return labelledpattern55 = null;


        RuleTree ID51_tree=null;
        RuleTree LPAR52_tree=null;
        RuleTree COMA54_tree=null;
        RuleTree RPAR56_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        try {
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:99:3: ( ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) )
            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:99:3: ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR
            {
            ID51=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_constructor989); 
            stream_ID.add(ID51);

            LPAR52=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_constructor991); 
            stream_LPAR.add(LPAR52);

            // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:99:11: ( labelledpattern ( COMA labelledpattern )* )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==ID||(LA21_0>=INT && LA21_0<=STRING)||LA21_0==AMPERCENT) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:99:12: labelledpattern ( COMA labelledpattern )*
                    {
                    pushFollow(FOLLOW_labelledpattern_in_constructor994);
                    labelledpattern53=labelledpattern();
                    _fsp--;

                    stream_labelledpattern.add(labelledpattern53.getTree());
                    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:99:28: ( COMA labelledpattern )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==COMA) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:99:29: COMA labelledpattern
                    	    {
                    	    COMA54=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_constructor997); 
                    	    stream_COMA.add(COMA54);

                    	    pushFollow(FOLLOW_labelledpattern_in_constructor999);
                    	    labelledpattern55=labelledpattern();
                    	    _fsp--;

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

            RPAR56=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_constructor1005); 
            stream_RPAR.add(RPAR56);


            // AST REWRITE
            // elements: labelledpattern, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 100:3: -> ^( Appl ID ^( TermList ( labelledpattern )* ) )
            {
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:100:6: ^( Appl ID ^( TermList ( labelledpattern )* ) )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:100:16: ^( TermList ( labelledpattern )* )
                {
                RuleTree root_2 = (RuleTree)adaptor.nil();
                root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_2);

                // /home/balland/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:100:27: ( labelledpattern )*
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


 

    public static final BitSet FOLLOW_rule_in_ruleset128 = new BitSet(new long[]{0x0000A40000000000L});
    public static final BitSet FOLLOW_EOF_in_ruleset132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule152 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_ARROW_in_rule154 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_rule156 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_IF_in_rule159 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_condition_in_rule163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_graphrule_in_graphruleset208 = new BitSet(new long[]{0x000B040000000000L});
    public static final BitSet FOLLOW_EOF_in_graphruleset212 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule234 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_ARROW_in_graphrule236 = new BitSet(new long[]{0x000B040000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule240 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_IF_in_graphrule243 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_condition_in_graphrule247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andcondition_in_condition297 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_OR_in_condition302 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_andcondition_in_condition304 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_simplecondition_in_andcondition337 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_AND_in_andcondition342 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_simplecondition_in_andcondition344 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_term_in_simplecondition377 = new BitSet(new long[]{0x000000FE00000000L});
    public static final BitSet FOLLOW_EQUALS_in_simplecondition380 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_simplecondition398 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEQ_in_simplecondition416 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_simplecondition434 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GEQ_in_simplecondition452 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_simplecondition470 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MATCH_in_simplecondition488 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_simplecondition634 = new BitSet(new long[]{0x0003A50000000000L});
    public static final BitSet FOLLOW_condition_in_simplecondition638 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RPAR_in_simplecondition640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern656 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern658 = new BitSet(new long[]{0x0003A60000000000L});
    public static final BitSet FOLLOW_term_in_pattern661 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern664 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_pattern666 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern696 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_AT_in_pattern699 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_ID_in_pattern704 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern707 = new BitSet(new long[]{0x0003A60000000000L});
    public static final BitSet FOLLOW_term_in_pattern710 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern713 = new BitSet(new long[]{0x0003A40000000000L});
    public static final BitSet FOLLOW_term_in_pattern715 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_pattern750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_pattern762 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_STAR_in_pattern764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_pattern776 = new BitSet(new long[]{0x0000A40000000000L});
    public static final BitSet FOLLOW_pattern_in_pattern778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term803 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_STAR_in_term808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_builtin842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_builtin856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labelledpattern877 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_COLON_in_labelledpattern879 = new BitSet(new long[]{0x000B040000000000L});
    public static final BitSet FOLLOW_graphpattern_in_labelledpattern885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_in_graphpattern917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_graphpattern925 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_STAR_in_graphpattern930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_graphpattern955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ref_in_graphpattern961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPERCENT_in_ref970 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_ID_in_ref972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constructor989 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_LPAR_in_constructor991 = new BitSet(new long[]{0x000B060000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor994 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_COMA_in_constructor997 = new BitSet(new long[]{0x000B040000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor999 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_RPAR_in_constructor1005 = new BitSet(new long[]{0x0000000000000002L});

}