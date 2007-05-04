// $ANTLR 3.0b6 /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g 2007-05-03 14:52:15

  package tom.gom.expander.rule;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULELIST", "RULE", "CONDRULE", "APPL", "CONDTERM", "CONDEQUALS", "CONDNOTEQUALS", "ARROW", "IF", "EQUALS", "NOTEQUALS", "ID", "LPAR", "COMA", "RPAR", "INT", "STRING", "ESC", "WS", "SLCOMMENT"
    };
    public static final int NOTEQUALS=14;
    public static final int RPAR=18;
    public static final int WS=22;
    public static final int LPAR=16;
    public static final int ARROW=11;
    public static final int STRING=20;
    public static final int RULE=5;
    public static final int RULELIST=4;
    public static final int CONDEQUALS=9;
    public static final int ESC=21;
    public static final int EQUALS=13;
    public static final int CONDTERM=8;
    public static final int SLCOMMENT=23;
    public static final int CONDNOTEQUALS=10;
    public static final int IF=12;
    public static final int INT=19;
    public static final int EOF=-1;
    public static final int CONDRULE=6;
    public static final int APPL=7;
    public static final int COMA=17;
    public static final int ID=15;

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
    public String getGrammarFileName() { return "/home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g"; }


    public static class ruleset_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ruleset
    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:24:1: ruleset : ( rule )* EOF -> ^( RULELIST ( rule )* ) ;
    public ruleset_return ruleset() throws RecognitionException {
        ruleset_return retval = new ruleset_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token EOF2=null;
        rule_return rule1 = null;

        List list_rule=new ArrayList();
        List list_EOF=new ArrayList();
        ASTTree EOF2_tree=null;

        try {
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:25:3: ( ( rule )* EOF -> ^( RULELIST ( rule )* ) )
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:25:3: ( rule )* EOF
            {
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:25:3: ( rule )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);
                if ( (LA1_0==ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:25:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset86);
            	    rule1=rule();
            	    _fsp--;

            	    list_rule.add(rule1.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            EOF2=(Token)input.LT(1);
            match(input,EOF,FOLLOW_EOF_in_ruleset90); 
            list_EOF.add(EOF2);


            // AST REWRITE
            int i_0 = 0;
            retval.tree = root_0;
            root_0 = (ASTTree)adaptor.nil();
            // 25:15: -> ^( RULELIST ( rule )* )
            {
                // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:25:18: ^( RULELIST ( rule )* )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(RULELIST, "RULELIST"), root_1);

                // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:25:29: ( rule )*
                {
                int n_1 = list_rule == null ? 0 : list_rule.size();
                 


                for (int i_1=0; i_1<n_1; i_1++) {
                    adaptor.addChild(root_1, list_rule.get(i_1));

                }
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        return retval;
    }
    // $ANTLR end ruleset

    public static class rule_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start rule
    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:27:1: rule : pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE pattern term ) -> ^( CONDRULE pattern term $cond) ;
    public rule_return rule() throws RecognitionException {
        rule_return retval = new rule_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ARROW4=null;
        Token IF6=null;
        condition_return cond = null;

        pattern_return pattern3 = null;

        term_return term5 = null;

        List list_term=new ArrayList();
        List list_pattern=new ArrayList();
        List list_condition=new ArrayList();
        List list_IF=new ArrayList();
        List list_ARROW=new ArrayList();
        ASTTree ARROW4_tree=null;
        ASTTree IF6_tree=null;

        try {
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:28:3: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE pattern term ) -> ^( CONDRULE pattern term $cond) )
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:28:3: pattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_pattern_in_rule113);
            pattern3=pattern();
            _fsp--;

            list_pattern.add(pattern3.getTree());
            ARROW4=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_rule115); 
            list_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule117);
            term5=term();
            _fsp--;

            list_term.add(term5.getTree());
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:28:22: ( IF cond= condition )?
            int alt2=2;
            int LA2_0 = input.LA(1);
            if ( (LA2_0==IF) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:28:23: IF cond= condition
                    {
                    IF6=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_rule120); 
                    list_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule124);
                    cond=condition();
                    _fsp--;

                    list_condition.add(cond.getTree());

                    }
                    break;

            }


            // AST REWRITE
            int i_0 = 0;
            retval.tree = root_0;
            root_0 = (ASTTree)adaptor.nil();
            // 29:5: -> { cond == null }? ^( RULE pattern term )
            if ( cond == null ) {
                // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:29:26: ^( RULE pattern term )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(RULE, "RULE"), root_1);

                adaptor.addChild(root_1, list_pattern.get(i_0));
                adaptor.addChild(root_1, list_term.get(i_0));

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 30:5: -> ^( CONDRULE pattern term $cond)
            {
                // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:30:8: ^( CONDRULE pattern term $cond)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDRULE, "CONDRULE"), root_1);

                adaptor.addChild(root_1, list_pattern.get(i_0));
                adaptor.addChild(root_1, list_term.get(i_0));
                adaptor.addChild(root_1, cond.tree);

                adaptor.addChild(root_0, root_1);
                }

            }



            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        return retval;
    }
    // $ANTLR end rule

    public static class condition_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start condition
    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:32:1: condition : p1= term ( EQUALS p2= term | NOTEQUALS p3= term )? -> {p2!=null && p3==null}? ^( CONDEQUALS $p1 $p2) -> {p2==null && p3!=null}? ^( CONDNOTEQUALS $p1 $p3) -> ^( CONDTERM $p1) ;
    public condition_return condition() throws RecognitionException {
        condition_return retval = new condition_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token EQUALS7=null;
        Token NOTEQUALS8=null;
        term_return p1 = null;

        term_return p2 = null;

        term_return p3 = null;

        List list_term=new ArrayList();
        List list_EQUALS=new ArrayList();
        List list_NOTEQUALS=new ArrayList();
        ASTTree EQUALS7_tree=null;
        ASTTree NOTEQUALS8_tree=null;

        try {
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:33:3: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term )? -> {p2!=null && p3==null}? ^( CONDEQUALS $p1 $p2) -> {p2==null && p3!=null}? ^( CONDNOTEQUALS $p1 $p3) -> ^( CONDTERM $p1) )
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:33:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term )?
            {
            pushFollow(FOLLOW_term_in_condition173);
            p1=term();
            _fsp--;

            list_term.add(p1.getTree());
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:33:11: ( EQUALS p2= term | NOTEQUALS p3= term )?
            int alt3=3;
            int LA3_0 = input.LA(1);
            if ( (LA3_0==EQUALS) ) {
                alt3=1;
            }
            else if ( (LA3_0==NOTEQUALS) ) {
                alt3=2;
            }
            switch (alt3) {
                case 1 :
                    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:33:12: EQUALS p2= term
                    {
                    EQUALS7=(Token)input.LT(1);
                    match(input,EQUALS,FOLLOW_EQUALS_in_condition176); 
                    list_EQUALS.add(EQUALS7);

                    pushFollow(FOLLOW_term_in_condition180);
                    p2=term();
                    _fsp--;

                    list_term.add(p2.getTree());

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:33:29: NOTEQUALS p3= term
                    {
                    NOTEQUALS8=(Token)input.LT(1);
                    match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_condition184); 
                    list_NOTEQUALS.add(NOTEQUALS8);

                    pushFollow(FOLLOW_term_in_condition188);
                    p3=term();
                    _fsp--;

                    list_term.add(p3.getTree());

                    }
                    break;

            }


            // AST REWRITE
            int i_0 = 0;
            retval.tree = root_0;
            root_0 = (ASTTree)adaptor.nil();
            // 34:5: -> {p2!=null && p3==null}? ^( CONDEQUALS $p1 $p2)
            if (p2!=null && p3==null) {
                // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:34:32: ^( CONDEQUALS $p1 $p2)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDEQUALS, "CONDEQUALS"), root_1);

                adaptor.addChild(root_1, p1.tree);
                adaptor.addChild(root_1, p2.tree);

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 35:5: -> {p2==null && p3!=null}? ^( CONDNOTEQUALS $p1 $p3)
            if (p2==null && p3!=null) {
                // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:35:32: ^( CONDNOTEQUALS $p1 $p3)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDNOTEQUALS, "CONDNOTEQUALS"), root_1);

                adaptor.addChild(root_1, p1.tree);
                adaptor.addChild(root_1, p3.tree);

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 36:5: -> ^( CONDTERM $p1)
            {
                // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:36:8: ^( CONDTERM $p1)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDTERM, "CONDTERM"), root_1);

                adaptor.addChild(root_1, p1.tree);

                adaptor.addChild(root_0, root_1);
                }

            }



            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        return retval;
    }
    // $ANTLR end condition

    public static class pattern_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start pattern
    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:38:1: pattern : ID LPAR ( term ( COMA term )* )? RPAR -> ^( APPL ID ( term )* ) ;
    public pattern_return pattern() throws RecognitionException {
        pattern_return retval = new pattern_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ID9=null;
        Token LPAR10=null;
        Token COMA12=null;
        Token RPAR14=null;
        term_return term11 = null;

        term_return term13 = null;

        List list_term=new ArrayList();
        List list_COMA=new ArrayList();
        List list_RPAR=new ArrayList();
        List list_LPAR=new ArrayList();
        List list_ID=new ArrayList();
        ASTTree ID9_tree=null;
        ASTTree LPAR10_tree=null;
        ASTTree COMA12_tree=null;
        ASTTree RPAR14_tree=null;

        try {
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:3: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( APPL ID ( term )* ) )
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:3: ID LPAR ( term ( COMA term )* )? RPAR
            {
            ID9=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_pattern251); 
            list_ID.add(ID9);

            LPAR10=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_pattern253); 
            list_LPAR.add(LPAR10);

            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:11: ( term ( COMA term )* )?
            int alt5=2;
            int LA5_0 = input.LA(1);
            if ( (LA5_0==ID||(LA5_0>=INT && LA5_0<=STRING)) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:12: term ( COMA term )*
                    {
                    pushFollow(FOLLOW_term_in_pattern256);
                    term11=term();
                    _fsp--;

                    list_term.add(term11.getTree());
                    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:17: ( COMA term )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);
                        if ( (LA4_0==COMA) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:18: COMA term
                    	    {
                    	    COMA12=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_pattern259); 
                    	    list_COMA.add(COMA12);

                    	    pushFollow(FOLLOW_term_in_pattern261);
                    	    term13=term();
                    	    _fsp--;

                    	    list_term.add(term13.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR14=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_pattern267); 
            list_RPAR.add(RPAR14);


            // AST REWRITE
            int i_0 = 0;
            retval.tree = root_0;
            root_0 = (ASTTree)adaptor.nil();
            // 39:37: -> ^( APPL ID ( term )* )
            {
                // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:40: ^( APPL ID ( term )* )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(APPL, "APPL"), root_1);

                adaptor.addChild(root_1, (Token)list_ID.get(i_0));
                // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:50: ( term )*
                {
                int n_1 = list_term == null ? 0 : list_term.size();
                 


                for (int i_1=0; i_1<n_1; i_1++) {
                    adaptor.addChild(root_1, list_term.get(i_1));

                }
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        return retval;
    }
    // $ANTLR end pattern

    public static class term_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start term
    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:41:1: term : ( pattern | ID | builtin );
    public term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ID16=null;
        pattern_return pattern15 = null;

        builtin_return builtin17 = null;


        ASTTree ID16_tree=null;

        try {
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:42:3: ( pattern | ID | builtin )
            int alt6=3;
            int LA6_0 = input.LA(1);
            if ( (LA6_0==ID) ) {
                int LA6_1 = input.LA(2);
                if ( (LA6_1==LPAR) ) {
                    alt6=1;
                }
                else if ( (LA6_1==EOF||(LA6_1>=IF && LA6_1<=ID)||(LA6_1>=COMA && LA6_1<=RPAR)) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("41:1: term : ( pattern | ID | builtin );", 6, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA6_0>=INT && LA6_0<=STRING)) ) {
                alt6=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("41:1: term : ( pattern | ID | builtin );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:42:3: pattern
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term290);
                    pattern15=pattern();
                    _fsp--;

                    adaptor.addChild(root_0, pattern15.getTree());

                    }
                    break;
                case 2 :
                    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:42:13: ID
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    ID16=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_term294); 
                    ID16_tree = (ASTTree)adaptor.create(ID16);
                    adaptor.addChild(root_0, ID16_tree);


                    }
                    break;
                case 3 :
                    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:42:18: builtin
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term298);
                    builtin17=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin17.getTree());

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        return retval;
    }
    // $ANTLR end term

    public static class builtin_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start builtin
    // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:44:1: builtin : (INT|STRING);
    public builtin_return builtin() throws RecognitionException {
        builtin_return retval = new builtin_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token set18=null;

        ASTTree set18_tree=null;

        try {
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:44:9: ( (INT|STRING))
            // /home/balland/workspace/jtom/src/tom/gom/expander/rule/Rule.g:45:3: (INT|STRING)
            {
            root_0 = (ASTTree)adaptor.nil();

            set18=(Token)input.LT(1);
            if ( (input.LA(1)>=INT && input.LA(1)<=STRING) ) {
                adaptor.addChild(root_0, adaptor.create(set18));
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_builtin310);    throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            retval.stop = input.LT(-1);

                retval.tree = (ASTTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        return retval;
    }
    // $ANTLR end builtin


 

    public static final BitSet FOLLOW_rule_in_ruleset86 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_EOF_in_ruleset90 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule113 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_ARROW_in_rule115 = new BitSet(new long[]{0x0000000000188000L});
    public static final BitSet FOLLOW_term_in_rule117 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_IF_in_rule120 = new BitSet(new long[]{0x0000000000188000L});
    public static final BitSet FOLLOW_condition_in_rule124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_condition173 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_EQUALS_in_condition176 = new BitSet(new long[]{0x0000000000188000L});
    public static final BitSet FOLLOW_term_in_condition180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_condition184 = new BitSet(new long[]{0x0000000000188000L});
    public static final BitSet FOLLOW_term_in_condition188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern251 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_LPAR_in_pattern253 = new BitSet(new long[]{0x00000000001C8000L});
    public static final BitSet FOLLOW_term_in_pattern256 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_COMA_in_pattern259 = new BitSet(new long[]{0x0000000000188000L});
    public static final BitSet FOLLOW_term_in_pattern261 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_RPAR_in_pattern267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_builtin310 = new BitSet(new long[]{0x0000000000000002L});

}