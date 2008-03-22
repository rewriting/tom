// $ANTLR 3.0 /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g 2008-03-22 14:30:57

package tom.gom.expander.rule;
import tom.gom.adt.rule.RuleTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CondEquals", "UnnamedVarStar", "CondMethod", "LabTerm", "RefTerm", "CondOr", "ConditionalRule", "Rule", "CondAnd", "CondNot", "RuleList", "Appl", "BuiltinInt", "BuiltinString", "CondLessEquals", "VarStar", "CondTerm", "TermList", "CondNotEquals", "PathTerm", "CondGreaterThan", "CondLessThan", "UnnamedVar", "CondGreaterEquals", "Var", "At", "ARROW", "IF", "OR", "AND", "NOT", "EQUALS", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "DOT", "ID", "LPAR", "RPAR", "COMA", "AT", "UNDERSCORE", "STAR", "INT", "STRING", "COLON", "AMPERCENT", "ESC", "WS", "SLCOMMENT"
    };
    public static final int CondEquals=4;
    public static final int RefTerm=8;
    public static final int LPAR=43;
    public static final int ARROW=30;
    public static final int LEQ=37;
    public static final int GEQ=39;
    public static final int RuleList=14;
    public static final int OR=32;
    public static final int DOT=41;
    public static final int BuiltinInt=16;
    public static final int BuiltinString=17;
    public static final int AND=33;
    public static final int SLCOMMENT=55;
    public static final int CondLessEquals=18;
    public static final int INT=49;
    public static final int CondLessThan=25;
    public static final int UnnamedVar=26;
    public static final int CondGreaterEquals=27;
    public static final int Var=28;
    public static final int AT=46;
    public static final int At=29;
    public static final int ID=42;
    public static final int NOTEQUALS=36;
    public static final int UnnamedVarStar=5;
    public static final int CondMethod=6;
    public static final int RPAR=44;
    public static final int LabTerm=7;
    public static final int WS=54;
    public static final int STRING=50;
    public static final int CondOr=9;
    public static final int LT=38;
    public static final int ConditionalRule=10;
    public static final int GT=40;
    public static final int Rule=11;
    public static final int CondAnd=12;
    public static final int CondNot=13;
    public static final int ESC=53;
    public static final int Appl=15;
    public static final int EQUALS=35;
    public static final int AMPERCENT=52;
    public static final int VarStar=19;
    public static final int CondTerm=20;
    public static final int IF=31;
    public static final int EOF=-1;
    public static final int COLON=51;
    public static final int TermList=21;
    public static final int CondNotEquals=22;
    public static final int COMA=45;
    public static final int PathTerm=23;
    public static final int CondGreaterThan=24;
    public static final int STAR=48;
    public static final int NOT=34;
    public static final int UNDERSCORE=47;

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
    public String getGrammarFileName() { return "/Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g"; }


    public static class ruleset_return extends ParserRuleReturnScope {
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ruleset
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:20:1: ruleset : ( rule )* EOF -> ^( RuleList ( rule )* ) ;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:3: ( ( rule )* EOF -> ^( RuleList ( rule )* ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:3: ( rule )* EOF
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:3: ( rule )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID||LA1_0==UNDERSCORE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset131);
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
            match(input,EOF,FOLLOW_EOF_in_ruleset135); 
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
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:18: ^( RuleList ( rule )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(RuleList, "RuleList"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:29: ( rule )*
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:23:1: rule : pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) ;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:3: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:3: pattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_pattern_in_rule155);
            pattern3=pattern();
            _fsp--;

            stream_pattern.add(pattern3.getTree());
            ARROW4=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_rule157); 
            stream_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule159);
            term5=term();
            _fsp--;

            stream_term.add(term5.getTree());
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:22: ( IF cond= condition )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==IF) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:23: IF cond= condition
                    {
                    IF6=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_rule162); 
                    stream_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule166);
                    cond=condition();
                    _fsp--;

                    stream_condition.add(cond.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: term, pattern, cond, pattern, term
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
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:25:26: ^( Rule pattern term )
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
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:26:8: ^( ConditionalRule pattern term $cond)
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:28:1: graphruleset : ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) ;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:3: ( ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:3: ( graphrule )* EOF
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:3: ( graphrule )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID||(LA3_0>=INT && LA3_0<=STRING)||LA3_0==AMPERCENT) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:4: graphrule
            	    {
            	    pushFollow(FOLLOW_graphrule_in_graphruleset211);
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
            match(input,EOF,FOLLOW_EOF_in_graphruleset215); 
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
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:23: ^( RuleList ( graphrule )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(RuleList, "RuleList"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:34: ( graphrule )*
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:31:1: graphrule : lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) ;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:3: (lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:3: lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )?
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:49: ( IF cond= condition )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==IF) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:50: IF cond= condition
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
            // elements: rhs, lhs, lhs, cond, rhs
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
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:33:26: ^( Rule $lhs $rhs)
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
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:34:8: ^( ConditionalRule $lhs $rhs $cond)
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:36:1: condition : cond= andcondition (or= OR andcondition )* -> {or!=null}? ^( CondOr ( andcondition )* ) -> $cond;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:3: (cond= andcondition (or= OR andcondition )* -> {or!=null}? ^( CondOr ( andcondition )* ) -> $cond)
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:3: cond= andcondition (or= OR andcondition )*
            {
            pushFollow(FOLLOW_andcondition_in_condition300);
            cond=andcondition();
            _fsp--;

            stream_andcondition.add(cond.getTree());
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:21: (or= OR andcondition )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==OR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:22: or= OR andcondition
            	    {
            	    or=(Token)input.LT(1);
            	    match(input,OR,FOLLOW_OR_in_condition305); 
            	    stream_OR.add(or);

            	    pushFollow(FOLLOW_andcondition_in_condition307);
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
            // elements: andcondition, cond
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
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:38:18: ^( CondOr ( andcondition )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondOr, "CondOr"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:38:27: ( andcondition )*
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:41:1: andcondition : cond= notcondition (and= AND notcondition )* -> {and!=null}? ^( CondAnd ( notcondition )* ) -> $cond;
    public final andcondition_return andcondition() throws RecognitionException {
        andcondition_return retval = new andcondition_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token and=null;
        notcondition_return cond = null;

        notcondition_return notcondition12 = null;


        RuleTree and_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_notcondition=new RewriteRuleSubtreeStream(adaptor,"rule notcondition");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:3: (cond= notcondition (and= AND notcondition )* -> {and!=null}? ^( CondAnd ( notcondition )* ) -> $cond)
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:3: cond= notcondition (and= AND notcondition )*
            {
            pushFollow(FOLLOW_notcondition_in_andcondition340);
            cond=notcondition();
            _fsp--;

            stream_notcondition.add(cond.getTree());
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:21: (and= AND notcondition )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==AND) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:22: and= AND notcondition
            	    {
            	    and=(Token)input.LT(1);
            	    match(input,AND,FOLLOW_AND_in_andcondition345); 
            	    stream_AND.add(and);

            	    pushFollow(FOLLOW_notcondition_in_andcondition347);
            	    notcondition12=notcondition();
            	    _fsp--;

            	    stream_notcondition.add(notcondition12.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            // AST REWRITE
            // elements: cond, notcondition
            // token labels: 
            // rule labels: cond, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 43:3: -> {and!=null}? ^( CondAnd ( notcondition )* )
            if (and!=null) {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:43:19: ^( CondAnd ( notcondition )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondAnd, "CondAnd"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:43:29: ( notcondition )*
                while ( stream_notcondition.hasNext() ) {
                    adaptor.addChild(root_1, stream_notcondition.next());

                }
                stream_notcondition.reset();

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

    public static class notcondition_return extends ParserRuleReturnScope {
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start notcondition
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:46:1: notcondition : (not= NOT )? cond= simplecondition -> {not!=null}? ^( CondNot $cond) -> $cond;
    public final notcondition_return notcondition() throws RecognitionException {
        notcondition_return retval = new notcondition_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token not=null;
        simplecondition_return cond = null;


        RuleTree not_tree=null;
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleSubtreeStream stream_simplecondition=new RewriteRuleSubtreeStream(adaptor,"rule simplecondition");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:3: ( (not= NOT )? cond= simplecondition -> {not!=null}? ^( CondNot $cond) -> $cond)
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:3: (not= NOT )? cond= simplecondition
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:6: (not= NOT )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==NOT) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:6: not= NOT
                    {
                    not=(Token)input.LT(1);
                    match(input,NOT,FOLLOW_NOT_in_notcondition380); 
                    stream_NOT.add(not);


                    }
                    break;

            }

            pushFollow(FOLLOW_simplecondition_in_notcondition385);
            cond=simplecondition();
            _fsp--;

            stream_simplecondition.add(cond.getTree());

            // AST REWRITE
            // elements: cond, cond
            // token labels: 
            // rule labels: cond, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_cond=new RewriteRuleSubtreeStream(adaptor,"token cond",cond!=null?cond.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 48:3: -> {not!=null}? ^( CondNot $cond)
            if (not!=null) {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:48:19: ^( CondNot $cond)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondNot, "CondNot"), root_1);

                adaptor.addChild(root_1, stream_cond.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 49:3: -> $cond
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
    // $ANTLR end notcondition

    public static class simplecondition_return extends ParserRuleReturnScope {
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start simplecondition
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:51:1: simplecondition : (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> {p8!=null}? ^( CondMethod $p1 ID $p8) -> ^( CondTerm $p1) | LPAR cond= condition RPAR -> $cond);
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
        Token DOT19=null;
        Token ID20=null;
        Token LPAR21=null;
        Token RPAR22=null;
        Token LPAR23=null;
        Token RPAR24=null;
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
        RuleTree DOT19_tree=null;
        RuleTree ID20_tree=null;
        RuleTree LPAR21_tree=null;
        RuleTree RPAR22_tree=null;
        RuleTree LPAR23_tree=null;
        RuleTree RPAR24_tree=null;
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
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:52:3: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> {p8!=null}? ^( CondMethod $p1 ID $p8) -> ^( CondTerm $p1) | LPAR cond= condition RPAR -> $cond)
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==ID||LA9_0==UNDERSCORE||(LA9_0>=INT && LA9_0<=STRING)) ) {
                alt9=1;
            }
            else if ( (LA9_0==LPAR) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("51:1: simplecondition : (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> {p8!=null}? ^( CondMethod $p1 ID $p8) -> ^( CondTerm $p1) | LPAR cond= condition RPAR -> $cond);", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:52:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
                    {
                    pushFollow(FOLLOW_term_in_simplecondition416);
                    p1=term();
                    _fsp--;

                    stream_term.add(p1.getTree());
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:52:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
                    int alt8=8;
                    switch ( input.LA(1) ) {
                        case EQUALS:
                            {
                            alt8=1;
                            }
                            break;
                        case NOTEQUALS:
                            {
                            alt8=2;
                            }
                            break;
                        case LEQ:
                            {
                            alt8=3;
                            }
                            break;
                        case LT:
                            {
                            alt8=4;
                            }
                            break;
                        case GEQ:
                            {
                            alt8=5;
                            }
                            break;
                        case GT:
                            {
                            alt8=6;
                            }
                            break;
                        case DOT:
                            {
                            alt8=7;
                            }
                            break;
                    }

                    switch (alt8) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:52:12: EQUALS p2= term
                            {
                            EQUALS13=(Token)input.LT(1);
                            match(input,EQUALS,FOLLOW_EQUALS_in_simplecondition419); 
                            stream_EQUALS.add(EQUALS13);

                            pushFollow(FOLLOW_term_in_simplecondition423);
                            p2=term();
                            _fsp--;

                            stream_term.add(p2.getTree());

                            }
                            break;
                        case 2 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:53:13: NOTEQUALS p3= term
                            {
                            NOTEQUALS14=(Token)input.LT(1);
                            match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_simplecondition437); 
                            stream_NOTEQUALS.add(NOTEQUALS14);

                            pushFollow(FOLLOW_term_in_simplecondition441);
                            p3=term();
                            _fsp--;

                            stream_term.add(p3.getTree());

                            }
                            break;
                        case 3 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:54:13: LEQ p4= term
                            {
                            LEQ15=(Token)input.LT(1);
                            match(input,LEQ,FOLLOW_LEQ_in_simplecondition455); 
                            stream_LEQ.add(LEQ15);

                            pushFollow(FOLLOW_term_in_simplecondition459);
                            p4=term();
                            _fsp--;

                            stream_term.add(p4.getTree());

                            }
                            break;
                        case 4 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:13: LT p5= term
                            {
                            LT16=(Token)input.LT(1);
                            match(input,LT,FOLLOW_LT_in_simplecondition473); 
                            stream_LT.add(LT16);

                            pushFollow(FOLLOW_term_in_simplecondition477);
                            p5=term();
                            _fsp--;

                            stream_term.add(p5.getTree());

                            }
                            break;
                        case 5 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:56:13: GEQ p6= term
                            {
                            GEQ17=(Token)input.LT(1);
                            match(input,GEQ,FOLLOW_GEQ_in_simplecondition491); 
                            stream_GEQ.add(GEQ17);

                            pushFollow(FOLLOW_term_in_simplecondition495);
                            p6=term();
                            _fsp--;

                            stream_term.add(p6.getTree());

                            }
                            break;
                        case 6 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:57:13: GT p7= term
                            {
                            GT18=(Token)input.LT(1);
                            match(input,GT,FOLLOW_GT_in_simplecondition509); 
                            stream_GT.add(GT18);

                            pushFollow(FOLLOW_term_in_simplecondition513);
                            p7=term();
                            _fsp--;

                            stream_term.add(p7.getTree());

                            }
                            break;
                        case 7 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:58:13: DOT ID LPAR p8= term RPAR
                            {
                            DOT19=(Token)input.LT(1);
                            match(input,DOT,FOLLOW_DOT_in_simplecondition527); 
                            stream_DOT.add(DOT19);

                            ID20=(Token)input.LT(1);
                            match(input,ID,FOLLOW_ID_in_simplecondition529); 
                            stream_ID.add(ID20);

                            LPAR21=(Token)input.LT(1);
                            match(input,LPAR,FOLLOW_LPAR_in_simplecondition531); 
                            stream_LPAR.add(LPAR21);

                            pushFollow(FOLLOW_term_in_simplecondition535);
                            p8=term();
                            _fsp--;

                            stream_term.add(p8.getTree());
                            RPAR22=(Token)input.LT(1);
                            match(input,RPAR,FOLLOW_RPAR_in_simplecondition537); 
                            stream_RPAR.add(RPAR22);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: p1, p4, p1, p3, p1, p1, p7, p1, p2, p1, p1, p8, ID, p5, p6, p1
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
                    // 60:5: -> {p2!=null}? ^( CondEquals $p1 $p2)
                    if (p2!=null) {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:60:20: ^( CondEquals $p1 $p2)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondEquals, "CondEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p2.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 61:5: -> {p3!=null}? ^( CondNotEquals $p1 $p3)
                    if (p3!=null) {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:61:20: ^( CondNotEquals $p1 $p3)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondNotEquals, "CondNotEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p3.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 62:5: -> {p4!=null}? ^( CondLessEquals $p1 $p4)
                    if (p4!=null) {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:62:20: ^( CondLessEquals $p1 $p4)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondLessEquals, "CondLessEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p4.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 63:5: -> {p5!=null}? ^( CondLessThan $p1 $p5)
                    if (p5!=null) {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:63:20: ^( CondLessThan $p1 $p5)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondLessThan, "CondLessThan"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p5.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 64:5: -> {p6!=null}? ^( CondGreaterEquals $p1 $p6)
                    if (p6!=null) {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:20: ^( CondGreaterEquals $p1 $p6)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondGreaterEquals, "CondGreaterEquals"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p6.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 65:5: -> {p7!=null}? ^( CondGreaterThan $p1 $p7)
                    if (p7!=null) {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:65:20: ^( CondGreaterThan $p1 $p7)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondGreaterThan, "CondGreaterThan"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_p7.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 66:5: -> {p8!=null}? ^( CondMethod $p1 ID $p8)
                    if (p8!=null) {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:66:20: ^( CondMethod $p1 ID $p8)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondMethod, "CondMethod"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());
                        adaptor.addChild(root_1, stream_ID.next());
                        adaptor.addChild(root_1, stream_p8.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 67:5: -> ^( CondTerm $p1)
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:8: ^( CondTerm $p1)
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(CondTerm, "CondTerm"), root_1);

                        adaptor.addChild(root_1, stream_p1.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:68:5: LPAR cond= condition RPAR
                    {
                    LPAR23=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_simplecondition697); 
                    stream_LPAR.add(LPAR23);

                    pushFollow(FOLLOW_condition_in_simplecondition701);
                    cond=condition();
                    _fsp--;

                    stream_condition.add(cond.getTree());
                    RPAR24=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_simplecondition703); 
                    stream_RPAR.add(RPAR24);


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
                    // 69:3: -> $cond
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:71:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) );
    public final pattern_return pattern() throws RecognitionException {
        pattern_return retval = new pattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token varname=null;
        Token funname=null;
        Token ID25=null;
        Token LPAR26=null;
        Token COMA28=null;
        Token RPAR30=null;
        Token AT31=null;
        Token LPAR32=null;
        Token COMA34=null;
        Token RPAR36=null;
        Token UNDERSCORE37=null;
        Token UNDERSCORE38=null;
        Token STAR39=null;
        term_return term27 = null;

        term_return term29 = null;

        term_return term33 = null;

        term_return term35 = null;


        RuleTree varname_tree=null;
        RuleTree funname_tree=null;
        RuleTree ID25_tree=null;
        RuleTree LPAR26_tree=null;
        RuleTree COMA28_tree=null;
        RuleTree RPAR30_tree=null;
        RuleTree AT31_tree=null;
        RuleTree LPAR32_tree=null;
        RuleTree COMA34_tree=null;
        RuleTree RPAR36_tree=null;
        RuleTree UNDERSCORE37_tree=null;
        RuleTree UNDERSCORE38_tree=null;
        RuleTree STAR39_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_UNDERSCORE=new RewriteRuleTokenStream(adaptor,"token UNDERSCORE");
        RewriteRuleTokenStream stream_AT=new RewriteRuleTokenStream(adaptor,"token AT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:3: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) )
            int alt14=4;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==ID) ) {
                int LA14_1 = input.LA(2);

                if ( (LA14_1==AT) ) {
                    alt14=2;
                }
                else if ( (LA14_1==LPAR) ) {
                    alt14=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("71:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) );", 14, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA14_0==UNDERSCORE) ) {
                int LA14_2 = input.LA(2);

                if ( (LA14_2==STAR) ) {
                    alt14=4;
                }
                else if ( (LA14_2==EOF||(LA14_2>=ARROW && LA14_2<=AND)||(LA14_2>=EQUALS && LA14_2<=ID)||(LA14_2>=RPAR && LA14_2<=COMA)||LA14_2==UNDERSCORE||(LA14_2>=INT && LA14_2<=STRING)||LA14_2==AMPERCENT) ) {
                    alt14=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("71:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) );", 14, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("71:1: pattern : ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) | (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) ) | UNDERSCORE -> ^( UnnamedVar ) | UNDERSCORE STAR -> ^( UnnamedVarStar ) );", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:3: ID LPAR ( term ( COMA term )* )? RPAR
                    {
                    ID25=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_pattern719); 
                    stream_ID.add(ID25);

                    LPAR26=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_pattern721); 
                    stream_LPAR.add(LPAR26);

                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:11: ( term ( COMA term )* )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==ID||LA11_0==UNDERSCORE||(LA11_0>=INT && LA11_0<=STRING)) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:12: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern724);
                            term27=term();
                            _fsp--;

                            stream_term.add(term27.getTree());
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:17: ( COMA term )*
                            loop10:
                            do {
                                int alt10=2;
                                int LA10_0 = input.LA(1);

                                if ( (LA10_0==COMA) ) {
                                    alt10=1;
                                }


                                switch (alt10) {
                            	case 1 :
                            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:18: COMA term
                            	    {
                            	    COMA28=(Token)input.LT(1);
                            	    match(input,COMA,FOLLOW_COMA_in_pattern727); 
                            	    stream_COMA.add(COMA28);

                            	    pushFollow(FOLLOW_term_in_pattern729);
                            	    term29=term();
                            	    _fsp--;

                            	    stream_term.add(term29.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop10;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR30=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_pattern735); 
                    stream_RPAR.add(RPAR30);


                    // AST REWRITE
                    // elements: ID, term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 72:37: -> ^( Appl ID ^( TermList ( term )* ) )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:40: ^( Appl ID ^( TermList ( term )* ) )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:50: ^( TermList ( term )* )
                        {
                        RuleTree root_2 = (RuleTree)adaptor.nil();
                        root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_2);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:61: ( term )*
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:5: (varname= ID ) AT (funname= ID ) LPAR ( term ( COMA term )* )? RPAR
                    {
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:5: (varname= ID )
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:6: varname= ID
                    {
                    varname=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_pattern759); 
                    stream_ID.add(varname);


                    }

                    AT31=(Token)input.LT(1);
                    match(input,AT,FOLLOW_AT_in_pattern762); 
                    stream_AT.add(AT31);

                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:21: (funname= ID )
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:22: funname= ID
                    {
                    funname=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_pattern767); 
                    stream_ID.add(funname);


                    }

                    LPAR32=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_pattern770); 
                    stream_LPAR.add(LPAR32);

                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:39: ( term ( COMA term )* )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==ID||LA13_0==UNDERSCORE||(LA13_0>=INT && LA13_0<=STRING)) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:40: term ( COMA term )*
                            {
                            pushFollow(FOLLOW_term_in_pattern773);
                            term33=term();
                            _fsp--;

                            stream_term.add(term33.getTree());
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:45: ( COMA term )*
                            loop12:
                            do {
                                int alt12=2;
                                int LA12_0 = input.LA(1);

                                if ( (LA12_0==COMA) ) {
                                    alt12=1;
                                }


                                switch (alt12) {
                            	case 1 :
                            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:46: COMA term
                            	    {
                            	    COMA34=(Token)input.LT(1);
                            	    match(input,COMA,FOLLOW_COMA_in_pattern776); 
                            	    stream_COMA.add(COMA34);

                            	    pushFollow(FOLLOW_term_in_pattern778);
                            	    term35=term();
                            	    _fsp--;

                            	    stream_term.add(term35.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop12;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAR36=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_pattern784); 
                    stream_RPAR.add(RPAR36);


                    // AST REWRITE
                    // elements: term, varname, funname
                    // token labels: varname, funname
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_varname=new RewriteRuleTokenStream(adaptor,"token varname",varname);
                    RewriteRuleTokenStream stream_funname=new RewriteRuleTokenStream(adaptor,"token funname",funname);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 73:65: -> ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:68: ^( At $varname ^( Appl $funname ^( TermList ( term )* ) ) )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(At, "At"), root_1);

                        adaptor.addChild(root_1, stream_varname.next());
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:82: ^( Appl $funname ^( TermList ( term )* ) )
                        {
                        RuleTree root_2 = (RuleTree)adaptor.nil();
                        root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_2);

                        adaptor.addChild(root_2, stream_funname.next());
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:98: ^( TermList ( term )* )
                        {
                        RuleTree root_3 = (RuleTree)adaptor.nil();
                        root_3 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_3);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:109: ( term )*
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:74:5: UNDERSCORE
                    {
                    UNDERSCORE37=(Token)input.LT(1);
                    match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_pattern813); 
                    stream_UNDERSCORE.add(UNDERSCORE37);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 74:16: -> ^( UnnamedVar )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:74:19: ^( UnnamedVar )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(UnnamedVar, "UnnamedVar"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 4 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:75:5: UNDERSCORE STAR
                    {
                    UNDERSCORE38=(Token)input.LT(1);
                    match(input,UNDERSCORE,FOLLOW_UNDERSCORE_in_pattern825); 
                    stream_UNDERSCORE.add(UNDERSCORE38);

                    STAR39=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_pattern827); 
                    stream_STAR.add(STAR39);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 75:21: -> ^( UnnamedVarStar )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:75:24: ^( UnnamedVarStar )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:77:1: term : ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin );
    public final term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token s=null;
        Token ID41=null;
        pattern_return pattern40 = null;

        builtin_return builtin42 = null;


        RuleTree s_tree=null;
        RuleTree ID41_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:78:3: ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin )
            int alt16=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==LPAR||LA16_1==AT) ) {
                    alt16=1;
                }
                else if ( (LA16_1==EOF||(LA16_1>=IF && LA16_1<=AND)||(LA16_1>=EQUALS && LA16_1<=ID)||(LA16_1>=RPAR && LA16_1<=COMA)||(LA16_1>=UNDERSCORE && LA16_1<=STRING)||LA16_1==AMPERCENT) ) {
                    alt16=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("77:1: term : ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin );", 16, 1, input);

                    throw nvae;
                }
                }
                break;
            case UNDERSCORE:
                {
                alt16=1;
                }
                break;
            case INT:
            case STRING:
                {
                alt16=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("77:1: term : ( pattern | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin );", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:78:3: pattern
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term842);
                    pattern40=pattern();
                    _fsp--;

                    adaptor.addChild(root_0, pattern40.getTree());

                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:80:5: ID (s= STAR )?
                    {
                    ID41=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_term850); 
                    stream_ID.add(ID41);

                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:80:8: (s= STAR )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==STAR) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:80:9: s= STAR
                            {
                            s=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_term855); 
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
                    // 80:18: -> {null==s}? ^( Var ID )
                    if (null==s) {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:80:32: ^( Var ID )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Var, "Var"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 80:42: -> ^( VarStar ID )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:80:44: ^( VarStar ID )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:81:5: builtin
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term880);
                    builtin42=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin42.getTree());

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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:83:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );
    public final builtin_return builtin() throws RecognitionException {
        builtin_return retval = new builtin_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token INT43=null;
        Token STRING44=null;

        RuleTree INT43_tree=null;
        RuleTree STRING44_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:84:3: ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==INT) ) {
                alt17=1;
            }
            else if ( (LA17_0==STRING) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("83:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:84:3: INT
                    {
                    INT43=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_builtin889); 
                    stream_INT.add(INT43);


                    // AST REWRITE
                    // elements: INT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 84:7: -> ^( BuiltinInt INT )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:84:10: ^( BuiltinInt INT )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:85:5: STRING
                    {
                    STRING44=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_builtin903); 
                    stream_STRING.add(STRING44);


                    // AST REWRITE
                    // elements: STRING
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 85:12: -> ^( BuiltinString STRING )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:85:15: ^( BuiltinString STRING )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:88:1: labelledpattern : (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p;
    public final labelledpattern_return labelledpattern() throws RecognitionException {
        labelledpattern_return retval = new labelledpattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token namelabel=null;
        Token COLON45=null;
        graphpattern_return p = null;


        RuleTree namelabel_tree=null;
        RuleTree COLON45_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_graphpattern=new RewriteRuleSubtreeStream(adaptor,"rule graphpattern");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:3: ( (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p)
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:3: (namelabel= ID COLON )? p= graphpattern
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:3: (namelabel= ID COLON )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==ID) ) {
                int LA18_1 = input.LA(2);

                if ( (LA18_1==COLON) ) {
                    alt18=1;
                }
            }
            switch (alt18) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:89:4: namelabel= ID COLON
                    {
                    namelabel=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_labelledpattern924); 
                    stream_ID.add(namelabel);

                    COLON45=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_labelledpattern926); 
                    stream_COLON.add(COLON45);


                    }
                    break;

            }

            pushFollow(FOLLOW_graphpattern_in_labelledpattern932);
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
            // 90:3: -> {$namelabel!=null}? ^( LabTerm $namelabel $p)
            if (namelabel!=null) {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:90:26: ^( LabTerm $namelabel $p)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(LabTerm, "LabTerm"), root_1);

                adaptor.addChild(root_1, stream_namelabel.next());
                adaptor.addChild(root_1, stream_p.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 91:3: -> $p
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:93:1: graphpattern : ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref );
    public final graphpattern_return graphpattern() throws RecognitionException {
        graphpattern_return retval = new graphpattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token s=null;
        Token ID47=null;
        constructor_return constructor46 = null;

        builtin_return builtin48 = null;

        ref_return ref49 = null;


        RuleTree s_tree=null;
        RuleTree ID47_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:94:3: ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref )
            int alt20=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA20_1 = input.LA(2);

                if ( (LA20_1==LPAR) ) {
                    alt20=1;
                }
                else if ( (LA20_1==EOF||(LA20_1>=ARROW && LA20_1<=IF)||LA20_1==ID||(LA20_1>=RPAR && LA20_1<=COMA)||(LA20_1>=STAR && LA20_1<=STRING)||LA20_1==AMPERCENT) ) {
                    alt20=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("93:1: graphpattern : ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref );", 20, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT:
            case STRING:
                {
                alt20=3;
                }
                break;
            case AMPERCENT:
                {
                alt20=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("93:1: graphpattern : ( constructor | ID (s= STAR )? -> {null==s}? ^( Var ID ) -> ^( VarStar ID ) | builtin | ref );", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:94:3: constructor
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_constructor_in_graphpattern964);
                    constructor46=constructor();
                    _fsp--;

                    adaptor.addChild(root_0, constructor46.getTree());

                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:5: ID (s= STAR )?
                    {
                    ID47=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_graphpattern972); 
                    stream_ID.add(ID47);

                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:8: (s= STAR )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==STAR) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:9: s= STAR
                            {
                            s=(Token)input.LT(1);
                            match(input,STAR,FOLLOW_STAR_in_graphpattern977); 
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
                    // 96:18: -> {null==s}? ^( Var ID )
                    if (null==s) {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:32: ^( Var ID )
                        {
                        RuleTree root_1 = (RuleTree)adaptor.nil();
                        root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Var, "Var"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 96:42: -> ^( VarStar ID )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:96:44: ^( VarStar ID )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:97:5: builtin
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_graphpattern1002);
                    builtin48=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin48.getTree());

                    }
                    break;
                case 4 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:98:5: ref
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_ref_in_graphpattern1008);
                    ref49=ref();
                    _fsp--;

                    adaptor.addChild(root_0, ref49.getTree());

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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:100:1: ref : AMPERCENT ID -> ^( RefTerm ID ) ;
    public final ref_return ref() throws RecognitionException {
        ref_return retval = new ref_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token AMPERCENT50=null;
        Token ID51=null;

        RuleTree AMPERCENT50_tree=null;
        RuleTree ID51_tree=null;
        RewriteRuleTokenStream stream_AMPERCENT=new RewriteRuleTokenStream(adaptor,"token AMPERCENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:101:3: ( AMPERCENT ID -> ^( RefTerm ID ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:101:3: AMPERCENT ID
            {
            AMPERCENT50=(Token)input.LT(1);
            match(input,AMPERCENT,FOLLOW_AMPERCENT_in_ref1017); 
            stream_AMPERCENT.add(AMPERCENT50);

            ID51=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_ref1019); 
            stream_ID.add(ID51);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 101:16: -> ^( RefTerm ID )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:101:19: ^( RefTerm ID )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:103:1: constructor : ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) ;
    public final constructor_return constructor() throws RecognitionException {
        constructor_return retval = new constructor_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ID52=null;
        Token LPAR53=null;
        Token COMA55=null;
        Token RPAR57=null;
        labelledpattern_return labelledpattern54 = null;

        labelledpattern_return labelledpattern56 = null;


        RuleTree ID52_tree=null;
        RuleTree LPAR53_tree=null;
        RuleTree COMA55_tree=null;
        RuleTree RPAR57_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:104:3: ( ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:104:3: ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR
            {
            ID52=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_constructor1036); 
            stream_ID.add(ID52);

            LPAR53=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_constructor1038); 
            stream_LPAR.add(LPAR53);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:104:11: ( labelledpattern ( COMA labelledpattern )* )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==ID||(LA22_0>=INT && LA22_0<=STRING)||LA22_0==AMPERCENT) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:104:12: labelledpattern ( COMA labelledpattern )*
                    {
                    pushFollow(FOLLOW_labelledpattern_in_constructor1041);
                    labelledpattern54=labelledpattern();
                    _fsp--;

                    stream_labelledpattern.add(labelledpattern54.getTree());
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:104:28: ( COMA labelledpattern )*
                    loop21:
                    do {
                        int alt21=2;
                        int LA21_0 = input.LA(1);

                        if ( (LA21_0==COMA) ) {
                            alt21=1;
                        }


                        switch (alt21) {
                    	case 1 :
                    	    // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:104:29: COMA labelledpattern
                    	    {
                    	    COMA55=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_constructor1044); 
                    	    stream_COMA.add(COMA55);

                    	    pushFollow(FOLLOW_labelledpattern_in_constructor1046);
                    	    labelledpattern56=labelledpattern();
                    	    _fsp--;

                    	    stream_labelledpattern.add(labelledpattern56.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop21;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR57=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_constructor1052); 
            stream_RPAR.add(RPAR57);


            // AST REWRITE
            // elements: ID, labelledpattern
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 105:3: -> ^( Appl ID ^( TermList ( labelledpattern )* ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:105:6: ^( Appl ID ^( TermList ( labelledpattern )* ) )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:105:16: ^( TermList ( labelledpattern )* )
                {
                RuleTree root_2 = (RuleTree)adaptor.nil();
                root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:105:27: ( labelledpattern )*
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


 

    public static final BitSet FOLLOW_rule_in_ruleset131 = new BitSet(new long[]{0x0000840000000000L});
    public static final BitSet FOLLOW_EOF_in_ruleset135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule155 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_ARROW_in_rule157 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_rule159 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_IF_in_rule162 = new BitSet(new long[]{0x00068C0400000000L});
    public static final BitSet FOLLOW_condition_in_rule166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_graphrule_in_graphruleset211 = new BitSet(new long[]{0x0016040000000000L});
    public static final BitSet FOLLOW_EOF_in_graphruleset215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule237 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_ARROW_in_graphrule239 = new BitSet(new long[]{0x0016040000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule243 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_IF_in_graphrule246 = new BitSet(new long[]{0x00068C0400000000L});
    public static final BitSet FOLLOW_condition_in_graphrule250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andcondition_in_condition300 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_OR_in_condition305 = new BitSet(new long[]{0x00068C0400000000L});
    public static final BitSet FOLLOW_andcondition_in_condition307 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_notcondition_in_andcondition340 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_AND_in_andcondition345 = new BitSet(new long[]{0x00068C0400000000L});
    public static final BitSet FOLLOW_notcondition_in_andcondition347 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_NOT_in_notcondition380 = new BitSet(new long[]{0x00068C0000000000L});
    public static final BitSet FOLLOW_simplecondition_in_notcondition385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_simplecondition416 = new BitSet(new long[]{0x000003F800000002L});
    public static final BitSet FOLLOW_EQUALS_in_simplecondition419 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_simplecondition437 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEQ_in_simplecondition455 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_simplecondition473 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GEQ_in_simplecondition491 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_simplecondition509 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_simplecondition527 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_ID_in_simplecondition529 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_LPAR_in_simplecondition531 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_simplecondition535 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_RPAR_in_simplecondition537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_simplecondition697 = new BitSet(new long[]{0x00068C0400000000L});
    public static final BitSet FOLLOW_condition_in_simplecondition701 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_RPAR_in_simplecondition703 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern719 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern721 = new BitSet(new long[]{0x0006940000000000L});
    public static final BitSet FOLLOW_term_in_pattern724 = new BitSet(new long[]{0x0000300000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern727 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_pattern729 = new BitSet(new long[]{0x0000300000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern759 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_AT_in_pattern762 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_ID_in_pattern767 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern770 = new BitSet(new long[]{0x0006940000000000L});
    public static final BitSet FOLLOW_term_in_pattern773 = new BitSet(new long[]{0x0000300000000000L});
    public static final BitSet FOLLOW_COMA_in_pattern776 = new BitSet(new long[]{0x0006840000000000L});
    public static final BitSet FOLLOW_term_in_pattern778 = new BitSet(new long[]{0x0000300000000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_pattern813 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNDERSCORE_in_pattern825 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_STAR_in_pattern827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term850 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_STAR_in_term855 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term880 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_builtin889 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_builtin903 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labelledpattern924 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_COLON_in_labelledpattern926 = new BitSet(new long[]{0x0016040000000000L});
    public static final BitSet FOLLOW_graphpattern_in_labelledpattern932 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_in_graphpattern964 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_graphpattern972 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_STAR_in_graphpattern977 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_graphpattern1002 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ref_in_graphpattern1008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPERCENT_in_ref1017 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_ID_in_ref1019 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constructor1036 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_LPAR_in_constructor1038 = new BitSet(new long[]{0x0016140000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor1041 = new BitSet(new long[]{0x0000300000000000L});
    public static final BitSet FOLLOW_COMA_in_constructor1044 = new BitSet(new long[]{0x0016040000000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor1046 = new BitSet(new long[]{0x0000300000000000L});
    public static final BitSet FOLLOW_RPAR_in_constructor1052 = new BitSet(new long[]{0x0000000000000002L});

}