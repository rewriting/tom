// $ANTLR 3.0 /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g 2007-07-20 23:19:37

  package tom.gom.expander.rule;
  import tom.gom.adt.rule.RuleTree;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BuiltinInt", "CondEquals", "BuiltinString", "CondMethod", "CondLessEquals", "RefTerm", "LabTerm", "CondTerm", "ConditionalRule", "Rule", "TermList", "CondNotEquals", "PathTerm", "CondGreaterThan", "CondLessThan", "RuleList", "CondGreaterEquals", "Var", "Appl", "ARROW", "IF", "EQUALS", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "DOT", "ID", "LPAR", "RPAR", "COMA", "INT", "STRING", "COLON", "AMPERCENT", "ESC", "WS", "SLCOMMENT"
    };
    public static final int CondEquals=5;
    public static final int NOTEQUALS=26;
    public static final int CondMethod=7;
    public static final int RPAR=34;
    public static final int RefTerm=9;
    public static final int LabTerm=10;
    public static final int WS=41;
    public static final int LPAR=33;
    public static final int ARROW=23;
    public static final int STRING=37;
    public static final int LT=28;
    public static final int LEQ=27;
    public static final int GT=30;
    public static final int ConditionalRule=12;
    public static final int Rule=13;
    public static final int GEQ=29;
    public static final int RuleList=19;
    public static final int ESC=40;
    public static final int Appl=22;
    public static final int DOT=31;
    public static final int BuiltinInt=4;
    public static final int EQUALS=25;
    public static final int BuiltinString=6;
    public static final int SLCOMMENT=42;
    public static final int AMPERCENT=39;
    public static final int CondLessEquals=8;
    public static final int INT=36;
    public static final int IF=24;
    public static final int CondTerm=11;
    public static final int EOF=-1;
    public static final int COLON=38;
    public static final int TermList=14;
    public static final int CondNotEquals=15;
    public static final int COMA=35;
    public static final int PathTerm=16;
    public static final int CondGreaterThan=17;
    public static final int CondLessThan=18;
    public static final int CondGreaterEquals=20;
    public static final int Var=21;
    public static final int ID=32;

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
    public String getGrammarFileName() { return "/home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g"; }


    public static class ruleset_return extends ParserRuleReturnScope {
        RuleTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ruleset
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:20:1: ruleset : ( rule )* EOF -> ^( RuleList ( rule )* ) ;
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
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:3: ( ( rule )* EOF -> ^( RuleList ( rule )* ) )
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:3: ( rule )* EOF
            {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:3: ( rule )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset113);
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
            match(input,EOF,FOLLOW_EOF_in_ruleset117); 
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:18: ^( RuleList ( rule )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(RuleList, "RuleList"), root_1);

                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:21:29: ( rule )*
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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:23:1: rule : pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) ;
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
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:3: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( Rule pattern term ) -> ^( ConditionalRule pattern term $cond) )
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:3: pattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_pattern_in_rule140);
            pattern3=pattern();
            _fsp--;

            stream_pattern.add(pattern3.getTree());
            ARROW4=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_rule142); 
            stream_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule144);
            term5=term();
            _fsp--;

            stream_term.add(term5.getTree());
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:22: ( IF cond= condition )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==IF) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:24:23: IF cond= condition
                    {
                    IF6=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_rule147); 
                    stream_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule151);
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:25:26: ^( Rule pattern term )
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:26:8: ^( ConditionalRule pattern term $cond)
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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:28:1: graphruleset : ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) ;
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
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:3: ( ( graphrule )* EOF -> ^( RuleList ( graphrule )* ) )
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:3: ( graphrule )* EOF
            {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:3: ( graphrule )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID||(LA3_0>=INT && LA3_0<=STRING)||LA3_0==AMPERCENT) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:4: graphrule
            	    {
            	    pushFollow(FOLLOW_graphrule_in_graphruleset199);
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
            match(input,EOF,FOLLOW_EOF_in_graphruleset203); 
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:23: ^( RuleList ( graphrule )* )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(RuleList, "RuleList"), root_1);

                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:29:34: ( graphrule )*
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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:31:1: graphrule : lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) ;
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
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:3: (lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )? -> { cond == null }? ^( Rule $lhs $rhs) -> ^( ConditionalRule $lhs $rhs $cond) )
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:3: lhs= labelledpattern ARROW rhs= labelledpattern ( IF cond= condition )?
            {
            pushFollow(FOLLOW_labelledpattern_in_graphrule228);
            lhs=labelledpattern();
            _fsp--;

            stream_labelledpattern.add(lhs.getTree());
            ARROW9=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_graphrule230); 
            stream_ARROW.add(ARROW9);

            pushFollow(FOLLOW_labelledpattern_in_graphrule234);
            rhs=labelledpattern();
            _fsp--;

            stream_labelledpattern.add(rhs.getTree());
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:49: ( IF cond= condition )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==IF) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:32:50: IF cond= condition
                    {
                    IF10=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_graphrule237); 
                    stream_IF.add(IF10);

                    pushFollow(FOLLOW_condition_in_graphrule241);
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:33:26: ^( Rule $lhs $rhs)
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:34:8: ^( ConditionalRule $lhs $rhs $cond)
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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:36:1: condition : p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> {p8!=null}? ^( CondMethod $p1 ID $p8) -> ^( CondTerm $p1) ;
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
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:3: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CondEquals $p1 $p2) -> {p3!=null}? ^( CondNotEquals $p1 $p3) -> {p4!=null}? ^( CondLessEquals $p1 $p4) -> {p5!=null}? ^( CondLessThan $p1 $p5) -> {p6!=null}? ^( CondGreaterEquals $p1 $p6) -> {p7!=null}? ^( CondGreaterThan $p1 $p7) -> {p8!=null}? ^( CondMethod $p1 ID $p8) -> ^( CondTerm $p1) )
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
            {
            pushFollow(FOLLOW_term_in_condition294);
            p1=term();
            _fsp--;

            stream_term.add(p1.getTree());
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
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
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:37:12: EQUALS p2= term
                    {
                    EQUALS11=(Token)input.LT(1);
                    match(input,EQUALS,FOLLOW_EQUALS_in_condition297); 
                    stream_EQUALS.add(EQUALS11);

                    pushFollow(FOLLOW_term_in_condition301);
                    p2=term();
                    _fsp--;

                    stream_term.add(p2.getTree());

                    }
                    break;
                case 2 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:38:7: NOTEQUALS p3= term
                    {
                    NOTEQUALS12=(Token)input.LT(1);
                    match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_condition309); 
                    stream_NOTEQUALS.add(NOTEQUALS12);

                    pushFollow(FOLLOW_term_in_condition313);
                    p3=term();
                    _fsp--;

                    stream_term.add(p3.getTree());

                    }
                    break;
                case 3 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:39:7: LEQ p4= term
                    {
                    LEQ13=(Token)input.LT(1);
                    match(input,LEQ,FOLLOW_LEQ_in_condition321); 
                    stream_LEQ.add(LEQ13);

                    pushFollow(FOLLOW_term_in_condition325);
                    p4=term();
                    _fsp--;

                    stream_term.add(p4.getTree());

                    }
                    break;
                case 4 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:40:7: LT p5= term
                    {
                    LT14=(Token)input.LT(1);
                    match(input,LT,FOLLOW_LT_in_condition333); 
                    stream_LT.add(LT14);

                    pushFollow(FOLLOW_term_in_condition337);
                    p5=term();
                    _fsp--;

                    stream_term.add(p5.getTree());

                    }
                    break;
                case 5 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:41:7: GEQ p6= term
                    {
                    GEQ15=(Token)input.LT(1);
                    match(input,GEQ,FOLLOW_GEQ_in_condition345); 
                    stream_GEQ.add(GEQ15);

                    pushFollow(FOLLOW_term_in_condition349);
                    p6=term();
                    _fsp--;

                    stream_term.add(p6.getTree());

                    }
                    break;
                case 6 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:42:7: GT p7= term
                    {
                    GT16=(Token)input.LT(1);
                    match(input,GT,FOLLOW_GT_in_condition357); 
                    stream_GT.add(GT16);

                    pushFollow(FOLLOW_term_in_condition361);
                    p7=term();
                    _fsp--;

                    stream_term.add(p7.getTree());

                    }
                    break;
                case 7 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:43:7: DOT ID LPAR p8= term RPAR
                    {
                    DOT17=(Token)input.LT(1);
                    match(input,DOT,FOLLOW_DOT_in_condition369); 
                    stream_DOT.add(DOT17);

                    ID18=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_condition371); 
                    stream_ID.add(ID18);

                    LPAR19=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_condition373); 
                    stream_LPAR.add(LPAR19);

                    pushFollow(FOLLOW_term_in_condition377);
                    p8=term();
                    _fsp--;

                    stream_term.add(p8.getTree());
                    RPAR20=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_condition379); 
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

            root_0 = (RuleTree)adaptor.nil();
            // 45:5: -> {p2!=null}? ^( CondEquals $p1 $p2)
            if (p2!=null) {
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:45:20: ^( CondEquals $p1 $p2)
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:46:20: ^( CondNotEquals $p1 $p3)
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:47:20: ^( CondLessEquals $p1 $p4)
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:48:20: ^( CondLessThan $p1 $p5)
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:49:20: ^( CondGreaterEquals $p1 $p6)
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:50:20: ^( CondGreaterThan $p1 $p7)
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:51:20: ^( CondMethod $p1 ID $p8)
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
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:52:8: ^( CondTerm $p1)
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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:54:1: pattern : ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) ;
    public final pattern_return pattern() throws RecognitionException {
        pattern_return retval = new pattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ID21=null;
        Token LPAR22=null;
        Token COMA24=null;
        Token RPAR26=null;
        term_return term23 = null;

        term_return term25 = null;


        RuleTree ID21_tree=null;
        RuleTree LPAR22_tree=null;
        RuleTree COMA24_tree=null;
        RuleTree RPAR26_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:3: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( Appl ID ^( TermList ( term )* ) ) )
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:3: ID LPAR ( term ( COMA term )* )? RPAR
            {
            ID21=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_pattern538); 
            stream_ID.add(ID21);

            LPAR22=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_pattern540); 
            stream_LPAR.add(LPAR22);

            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:11: ( term ( COMA term )* )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==ID||(LA7_0>=INT && LA7_0<=STRING)) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:12: term ( COMA term )*
                    {
                    pushFollow(FOLLOW_term_in_pattern543);
                    term23=term();
                    _fsp--;

                    stream_term.add(term23.getTree());
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:17: ( COMA term )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COMA) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:18: COMA term
                    	    {
                    	    COMA24=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_pattern546); 
                    	    stream_COMA.add(COMA24);

                    	    pushFollow(FOLLOW_term_in_pattern548);
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
            match(input,RPAR,FOLLOW_RPAR_in_pattern554); 
            stream_RPAR.add(RPAR26);


            // AST REWRITE
            // elements: term, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 55:37: -> ^( Appl ID ^( TermList ( term )* ) )
            {
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:40: ^( Appl ID ^( TermList ( term )* ) )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:50: ^( TermList ( term )* )
                {
                RuleTree root_2 = (RuleTree)adaptor.nil();
                root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_2);

                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:55:61: ( term )*
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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:57:1: term : ( pattern | ID -> ^( Var ID ) | builtin );
    public final term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ID28=null;
        pattern_return pattern27 = null;

        builtin_return builtin29 = null;


        RuleTree ID28_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:58:3: ( pattern | ID -> ^( Var ID ) | builtin )
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
                        new NoViableAltException("57:1: term : ( pattern | ID -> ^( Var ID ) | builtin );", 8, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA8_0>=INT && LA8_0<=STRING)) ) {
                alt8=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("57:1: term : ( pattern | ID -> ^( Var ID ) | builtin );", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:58:3: pattern
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term579);
                    pattern27=pattern();
                    _fsp--;

                    adaptor.addChild(root_0, pattern27.getTree());

                    }
                    break;
                case 2 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:59:5: ID
                    {
                    ID28=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_term585); 
                    stream_ID.add(ID28);


                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 59:8: -> ^( Var ID )
                    {
                        // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:59:11: ^( Var ID )
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
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:60:5: builtin
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term599);
                    builtin29=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin29.getTree());

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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:62:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );
    public final builtin_return builtin() throws RecognitionException {
        builtin_return retval = new builtin_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token INT30=null;
        Token STRING31=null;

        RuleTree INT30_tree=null;
        RuleTree STRING31_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:63:3: ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==INT) ) {
                alt9=1;
            }
            else if ( (LA9_0==STRING) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("62:1: builtin : ( INT -> ^( BuiltinInt INT ) | STRING -> ^( BuiltinString STRING ) );", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:63:3: INT
                    {
                    INT30=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_builtin609); 
                    stream_INT.add(INT30);


                    // AST REWRITE
                    // elements: INT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 63:7: -> ^( BuiltinInt INT )
                    {
                        // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:63:10: ^( BuiltinInt INT )
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
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:5: STRING
                    {
                    STRING31=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_builtin623); 
                    stream_STRING.add(STRING31);


                    // AST REWRITE
                    // elements: STRING
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 64:12: -> ^( BuiltinString STRING )
                    {
                        // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:64:15: ^( BuiltinString STRING )
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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:67:1: labelledpattern : (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p;
    public final labelledpattern_return labelledpattern() throws RecognitionException {
        labelledpattern_return retval = new labelledpattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token namelabel=null;
        Token COLON32=null;
        graphpattern_return p = null;


        RuleTree namelabel_tree=null;
        RuleTree COLON32_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_graphpattern=new RewriteRuleSubtreeStream(adaptor,"rule graphpattern");
        try {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:68:3: ( (namelabel= ID COLON )? p= graphpattern -> {$namelabel!=null}? ^( LabTerm $namelabel $p) -> $p)
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:68:3: (namelabel= ID COLON )? p= graphpattern
            {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:68:3: (namelabel= ID COLON )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==ID) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==COLON) ) {
                    alt10=1;
                }
            }
            switch (alt10) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:68:4: namelabel= ID COLON
                    {
                    namelabel=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_labelledpattern645); 
                    stream_ID.add(namelabel);

                    COLON32=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_labelledpattern647); 
                    stream_COLON.add(COLON32);


                    }
                    break;

            }

            pushFollow(FOLLOW_graphpattern_in_labelledpattern653);
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
            // 69:3: -> {$namelabel!=null}? ^( LabTerm $namelabel $p)
            if (namelabel!=null) {
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:69:26: ^( LabTerm $namelabel $p)
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(LabTerm, "LabTerm"), root_1);

                adaptor.addChild(root_1, stream_namelabel.next());
                adaptor.addChild(root_1, stream_p.next());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 70:3: -> $p
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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:72:1: graphpattern : ( constructor | ID -> ^( Var ID ) | builtin | ref );
    public final graphpattern_return graphpattern() throws RecognitionException {
        graphpattern_return retval = new graphpattern_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ID34=null;
        constructor_return constructor33 = null;

        builtin_return builtin35 = null;

        ref_return ref36 = null;


        RuleTree ID34_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:3: ( constructor | ID -> ^( Var ID ) | builtin | ref )
            int alt11=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==LPAR) ) {
                    alt11=1;
                }
                else if ( (LA11_1==EOF||(LA11_1>=ARROW && LA11_1<=IF)||LA11_1==ID||(LA11_1>=RPAR && LA11_1<=STRING)||LA11_1==AMPERCENT) ) {
                    alt11=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("72:1: graphpattern : ( constructor | ID -> ^( Var ID ) | builtin | ref );", 11, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT:
            case STRING:
                {
                alt11=3;
                }
                break;
            case AMPERCENT:
                {
                alt11=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("72:1: graphpattern : ( constructor | ID -> ^( Var ID ) | builtin | ref );", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:73:3: constructor
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_constructor_in_graphpattern686);
                    constructor33=constructor();
                    _fsp--;

                    adaptor.addChild(root_0, constructor33.getTree());

                    }
                    break;
                case 2 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:74:5: ID
                    {
                    ID34=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_graphpattern692); 
                    stream_ID.add(ID34);


                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (RuleTree)adaptor.nil();
                    // 74:8: -> ^( Var ID )
                    {
                        // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:74:11: ^( Var ID )
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
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:75:5: builtin
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_graphpattern706);
                    builtin35=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin35.getTree());

                    }
                    break;
                case 4 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:76:5: ref
                    {
                    root_0 = (RuleTree)adaptor.nil();

                    pushFollow(FOLLOW_ref_in_graphpattern712);
                    ref36=ref();
                    _fsp--;

                    adaptor.addChild(root_0, ref36.getTree());

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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:78:1: ref : AMPERCENT ID -> ^( RefTerm ID ) ;
    public final ref_return ref() throws RecognitionException {
        ref_return retval = new ref_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token AMPERCENT37=null;
        Token ID38=null;

        RuleTree AMPERCENT37_tree=null;
        RuleTree ID38_tree=null;
        RewriteRuleTokenStream stream_AMPERCENT=new RewriteRuleTokenStream(adaptor,"token AMPERCENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:79:3: ( AMPERCENT ID -> ^( RefTerm ID ) )
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:79:3: AMPERCENT ID
            {
            AMPERCENT37=(Token)input.LT(1);
            match(input,AMPERCENT,FOLLOW_AMPERCENT_in_ref722); 
            stream_AMPERCENT.add(AMPERCENT37);

            ID38=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_ref724); 
            stream_ID.add(ID38);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 79:16: -> ^( RefTerm ID )
            {
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:79:19: ^( RefTerm ID )
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
    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:81:1: constructor : ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) ;
    public final constructor_return constructor() throws RecognitionException {
        constructor_return retval = new constructor_return();
        retval.start = input.LT(1);

        RuleTree root_0 = null;

        Token ID39=null;
        Token LPAR40=null;
        Token COMA42=null;
        Token RPAR44=null;
        labelledpattern_return labelledpattern41 = null;

        labelledpattern_return labelledpattern43 = null;


        RuleTree ID39_tree=null;
        RuleTree LPAR40_tree=null;
        RuleTree COMA42_tree=null;
        RuleTree RPAR44_tree=null;
        RewriteRuleTokenStream stream_COMA=new RewriteRuleTokenStream(adaptor,"token COMA");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_labelledpattern=new RewriteRuleSubtreeStream(adaptor,"rule labelledpattern");
        try {
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:3: ( ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR -> ^( Appl ID ^( TermList ( labelledpattern )* ) ) )
            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:3: ID LPAR ( labelledpattern ( COMA labelledpattern )* )? RPAR
            {
            ID39=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_constructor742); 
            stream_ID.add(ID39);

            LPAR40=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_constructor744); 
            stream_LPAR.add(LPAR40);

            // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:11: ( labelledpattern ( COMA labelledpattern )* )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==ID||(LA13_0>=INT && LA13_0<=STRING)||LA13_0==AMPERCENT) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:12: labelledpattern ( COMA labelledpattern )*
                    {
                    pushFollow(FOLLOW_labelledpattern_in_constructor747);
                    labelledpattern41=labelledpattern();
                    _fsp--;

                    stream_labelledpattern.add(labelledpattern41.getTree());
                    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:28: ( COMA labelledpattern )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==COMA) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:82:29: COMA labelledpattern
                    	    {
                    	    COMA42=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_constructor750); 
                    	    stream_COMA.add(COMA42);

                    	    pushFollow(FOLLOW_labelledpattern_in_constructor752);
                    	    labelledpattern43=labelledpattern();
                    	    _fsp--;

                    	    stream_labelledpattern.add(labelledpattern43.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR44=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_constructor758); 
            stream_RPAR.add(RPAR44);


            // AST REWRITE
            // elements: ID, labelledpattern
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (RuleTree)adaptor.nil();
            // 83:3: -> ^( Appl ID ^( TermList ( labelledpattern )* ) )
            {
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:83:6: ^( Appl ID ^( TermList ( labelledpattern )* ) )
                {
                RuleTree root_1 = (RuleTree)adaptor.nil();
                root_1 = (RuleTree)adaptor.becomeRoot(adaptor.create(Appl, "Appl"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:83:16: ^( TermList ( labelledpattern )* )
                {
                RuleTree root_2 = (RuleTree)adaptor.nil();
                root_2 = (RuleTree)adaptor.becomeRoot(adaptor.create(TermList, "TermList"), root_2);

                // /home/tonio/workspace/jtom/src/gen/tom/gom/expander/rule/Rule.g:83:27: ( labelledpattern )*
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


 

    public static final BitSet FOLLOW_rule_in_ruleset113 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_EOF_in_ruleset117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule140 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_ARROW_in_rule142 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_term_in_rule144 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_IF_in_rule147 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_condition_in_rule151 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_graphrule_in_graphruleset199 = new BitSet(new long[]{0x000000B100000000L});
    public static final BitSet FOLLOW_EOF_in_graphruleset203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule228 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_ARROW_in_graphrule230 = new BitSet(new long[]{0x000000B100000000L});
    public static final BitSet FOLLOW_labelledpattern_in_graphrule234 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_IF_in_graphrule237 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_condition_in_graphrule241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_condition294 = new BitSet(new long[]{0x00000000FE000002L});
    public static final BitSet FOLLOW_EQUALS_in_condition297 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_term_in_condition301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_condition309 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_term_in_condition313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEQ_in_condition321 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_term_in_condition325 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_condition333 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_term_in_condition337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GEQ_in_condition345 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_term_in_condition349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_condition357 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_term_in_condition361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_condition369 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_ID_in_condition371 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_LPAR_in_condition373 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_term_in_condition377 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RPAR_in_condition379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern538 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern540 = new BitSet(new long[]{0x0000003500000000L});
    public static final BitSet FOLLOW_term_in_pattern543 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_COMA_in_pattern546 = new BitSet(new long[]{0x0000003100000000L});
    public static final BitSet FOLLOW_term_in_pattern548 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_builtin609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_builtin623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labelledpattern645 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_COLON_in_labelledpattern647 = new BitSet(new long[]{0x000000B100000000L});
    public static final BitSet FOLLOW_graphpattern_in_labelledpattern653 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_in_graphpattern686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_graphpattern692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_graphpattern706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ref_in_graphpattern712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPERCENT_in_ref722 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_ID_in_ref724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constructor742 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_LPAR_in_constructor744 = new BitSet(new long[]{0x000000B500000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor747 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_COMA_in_constructor750 = new BitSet(new long[]{0x000000B100000000L});
    public static final BitSet FOLLOW_labelledpattern_in_constructor752 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_RPAR_in_constructor758 = new BitSet(new long[]{0x0000000000000002L});

}