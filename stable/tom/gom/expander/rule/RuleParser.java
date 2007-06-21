// $ANTLR 3.0b6 /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g 2007-06-21 22:28:18

  package tom.gom.expander.rule;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RuleParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULELIST", "RULE", "CONDRULE", "APPL", "CONDTERM", "CONDEQUALS", "CONDNOTEQUALS", "CONDLEQ", "CONDLT", "CONDGEQ", "CONDGT", "CONDMETHOD", "ARROW", "IF", "EQUALS", "NOTEQUALS", "LEQ", "LT", "GEQ", "GT", "DOT", "ID", "LPAR", "RPAR", "COMA", "INT", "STRING", "ESC", "WS", "SLCOMMENT"
    };
    public static final int CONDGEQ=13;
    public static final int NOTEQUALS=19;
    public static final int RPAR=27;
    public static final int CONDLT=12;
    public static final int WS=32;
    public static final int LPAR=26;
    public static final int ARROW=16;
    public static final int STRING=30;
    public static final int LT=21;
    public static final int LEQ=20;
    public static final int GT=23;
    public static final int CONDGT=14;
    public static final int RULE=5;
    public static final int RULELIST=4;
    public static final int GEQ=22;
    public static final int CONDEQUALS=9;
    public static final int ESC=31;
    public static final int DOT=24;
    public static final int EQUALS=18;
    public static final int CONDTERM=8;
    public static final int SLCOMMENT=33;
    public static final int CONDNOTEQUALS=10;
    public static final int CONDMETHOD=15;
    public static final int INT=29;
    public static final int IF=17;
    public static final int CONDLEQ=11;
    public static final int EOF=-1;
    public static final int CONDRULE=6;
    public static final int APPL=7;
    public static final int COMA=28;
    public static final int ID=25;

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
    public String getGrammarFileName() { return "/Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g"; }


    public static class ruleset_return extends ParserRuleReturnScope {
        ASTTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ruleset
    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:29:1: ruleset : ( rule )* EOF -> ^( RULELIST ( rule )* ) ;
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
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:30:3: ( ( rule )* EOF -> ^( RULELIST ( rule )* ) )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:30:3: ( rule )* EOF
            {
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:30:3: ( rule )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);
                if ( (LA1_0==ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:30:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_ruleset111);
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
            match(input,EOF,FOLLOW_EOF_in_ruleset115); 
            list_EOF.add(EOF2);


            // AST REWRITE
            int i_0 = 0;
            retval.tree = root_0;
            root_0 = (ASTTree)adaptor.nil();
            // 30:15: -> ^( RULELIST ( rule )* )
            {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:30:18: ^( RULELIST ( rule )* )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(RULELIST, "RULELIST"), root_1);

                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:30:29: ( rule )*
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
    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:32:1: rule : pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE pattern term ) -> ^( CONDRULE pattern term $cond) ;
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
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:33:3: ( pattern ARROW term ( IF cond= condition )? -> { cond == null }? ^( RULE pattern term ) -> ^( CONDRULE pattern term $cond) )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:33:3: pattern ARROW term ( IF cond= condition )?
            {
            pushFollow(FOLLOW_pattern_in_rule138);
            pattern3=pattern();
            _fsp--;

            list_pattern.add(pattern3.getTree());
            ARROW4=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_rule140); 
            list_ARROW.add(ARROW4);

            pushFollow(FOLLOW_term_in_rule142);
            term5=term();
            _fsp--;

            list_term.add(term5.getTree());
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:33:22: ( IF cond= condition )?
            int alt2=2;
            int LA2_0 = input.LA(1);
            if ( (LA2_0==IF) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:33:23: IF cond= condition
                    {
                    IF6=(Token)input.LT(1);
                    match(input,IF,FOLLOW_IF_in_rule145); 
                    list_IF.add(IF6);

                    pushFollow(FOLLOW_condition_in_rule149);
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
            // 34:5: -> { cond == null }? ^( RULE pattern term )
            if ( cond == null ) {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:34:26: ^( RULE pattern term )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(RULE, "RULE"), root_1);

                adaptor.addChild(root_1, list_pattern.get(i_0));
                adaptor.addChild(root_1, list_term.get(i_0));

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 35:5: -> ^( CONDRULE pattern term $cond)
            {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:35:8: ^( CONDRULE pattern term $cond)
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
    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:37:1: condition : p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CONDEQUALS $p1 $p2) -> {p3!=null}? ^( CONDNOTEQUALS $p1 $p3) -> {p4!=null}? ^( CONDLEQ $p1 $p4) -> {p5!=null}? ^( CONDLT $p1 $p5) -> {p6!=null}? ^( CONDGEQ $p1 $p6) -> {p7!=null}? ^( CONDGT $p1 $p7) -> {p8!=null}? ^( CONDMETHOD $p1 ID $p8) -> ^( CONDTERM $p1) ;
    public condition_return condition() throws RecognitionException {
        condition_return retval = new condition_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token EQUALS7=null;
        Token NOTEQUALS8=null;
        Token LEQ9=null;
        Token LT10=null;
        Token GEQ11=null;
        Token GT12=null;
        Token DOT13=null;
        Token ID14=null;
        Token LPAR15=null;
        Token RPAR16=null;
        term_return p1 = null;

        term_return p2 = null;

        term_return p3 = null;

        term_return p4 = null;

        term_return p5 = null;

        term_return p6 = null;

        term_return p7 = null;

        term_return p8 = null;

        List list_term=new ArrayList();
        List list_EQUALS=new ArrayList();
        List list_LEQ=new ArrayList();
        List list_LT=new ArrayList();
        List list_GT=new ArrayList();
        List list_NOTEQUALS=new ArrayList();
        List list_RPAR=new ArrayList();
        List list_GEQ=new ArrayList();
        List list_LPAR=new ArrayList();
        List list_DOT=new ArrayList();
        List list_ID=new ArrayList();
        ASTTree EQUALS7_tree=null;
        ASTTree NOTEQUALS8_tree=null;
        ASTTree LEQ9_tree=null;
        ASTTree LT10_tree=null;
        ASTTree GEQ11_tree=null;
        ASTTree GT12_tree=null;
        ASTTree DOT13_tree=null;
        ASTTree ID14_tree=null;
        ASTTree LPAR15_tree=null;
        ASTTree RPAR16_tree=null;

        try {
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:38:3: (p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )? -> {p2!=null}? ^( CONDEQUALS $p1 $p2) -> {p3!=null}? ^( CONDNOTEQUALS $p1 $p3) -> {p4!=null}? ^( CONDLEQ $p1 $p4) -> {p5!=null}? ^( CONDLT $p1 $p5) -> {p6!=null}? ^( CONDGEQ $p1 $p6) -> {p7!=null}? ^( CONDGT $p1 $p7) -> {p8!=null}? ^( CONDMETHOD $p1 ID $p8) -> ^( CONDTERM $p1) )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:38:3: p1= term ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
            {
            pushFollow(FOLLOW_term_in_condition198);
            p1=term();
            _fsp--;

            list_term.add(p1.getTree());
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:38:11: ( EQUALS p2= term | NOTEQUALS p3= term | LEQ p4= term | LT p5= term | GEQ p6= term | GT p7= term | DOT ID LPAR p8= term RPAR )?
            int alt3=8;
            switch ( input.LA(1) ) {
                case EQUALS:
                    alt3=1;
                    break;
                case NOTEQUALS:
                    alt3=2;
                    break;
                case LEQ:
                    alt3=3;
                    break;
                case LT:
                    alt3=4;
                    break;
                case GEQ:
                    alt3=5;
                    break;
                case GT:
                    alt3=6;
                    break;
                case DOT:
                    alt3=7;
                    break;
            }

            switch (alt3) {
                case 1 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:38:12: EQUALS p2= term
                    {
                    EQUALS7=(Token)input.LT(1);
                    match(input,EQUALS,FOLLOW_EQUALS_in_condition201); 
                    list_EQUALS.add(EQUALS7);

                    pushFollow(FOLLOW_term_in_condition205);
                    p2=term();
                    _fsp--;

                    list_term.add(p2.getTree());

                    }
                    break;
                case 2 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:39:7: NOTEQUALS p3= term
                    {
                    NOTEQUALS8=(Token)input.LT(1);
                    match(input,NOTEQUALS,FOLLOW_NOTEQUALS_in_condition213); 
                    list_NOTEQUALS.add(NOTEQUALS8);

                    pushFollow(FOLLOW_term_in_condition217);
                    p3=term();
                    _fsp--;

                    list_term.add(p3.getTree());

                    }
                    break;
                case 3 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:40:7: LEQ p4= term
                    {
                    LEQ9=(Token)input.LT(1);
                    match(input,LEQ,FOLLOW_LEQ_in_condition225); 
                    list_LEQ.add(LEQ9);

                    pushFollow(FOLLOW_term_in_condition229);
                    p4=term();
                    _fsp--;

                    list_term.add(p4.getTree());

                    }
                    break;
                case 4 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:41:7: LT p5= term
                    {
                    LT10=(Token)input.LT(1);
                    match(input,LT,FOLLOW_LT_in_condition237); 
                    list_LT.add(LT10);

                    pushFollow(FOLLOW_term_in_condition241);
                    p5=term();
                    _fsp--;

                    list_term.add(p5.getTree());

                    }
                    break;
                case 5 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:42:7: GEQ p6= term
                    {
                    GEQ11=(Token)input.LT(1);
                    match(input,GEQ,FOLLOW_GEQ_in_condition249); 
                    list_GEQ.add(GEQ11);

                    pushFollow(FOLLOW_term_in_condition253);
                    p6=term();
                    _fsp--;

                    list_term.add(p6.getTree());

                    }
                    break;
                case 6 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:43:7: GT p7= term
                    {
                    GT12=(Token)input.LT(1);
                    match(input,GT,FOLLOW_GT_in_condition261); 
                    list_GT.add(GT12);

                    pushFollow(FOLLOW_term_in_condition265);
                    p7=term();
                    _fsp--;

                    list_term.add(p7.getTree());

                    }
                    break;
                case 7 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:44:7: DOT ID LPAR p8= term RPAR
                    {
                    DOT13=(Token)input.LT(1);
                    match(input,DOT,FOLLOW_DOT_in_condition273); 
                    list_DOT.add(DOT13);

                    ID14=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_condition275); 
                    list_ID.add(ID14);

                    LPAR15=(Token)input.LT(1);
                    match(input,LPAR,FOLLOW_LPAR_in_condition277); 
                    list_LPAR.add(LPAR15);

                    pushFollow(FOLLOW_term_in_condition281);
                    p8=term();
                    _fsp--;

                    list_term.add(p8.getTree());
                    RPAR16=(Token)input.LT(1);
                    match(input,RPAR,FOLLOW_RPAR_in_condition283); 
                    list_RPAR.add(RPAR16);


                    }
                    break;

            }


            // AST REWRITE
            int i_0 = 0;
            retval.tree = root_0;
            root_0 = (ASTTree)adaptor.nil();
            // 46:5: -> {p2!=null}? ^( CONDEQUALS $p1 $p2)
            if (p2!=null) {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:46:20: ^( CONDEQUALS $p1 $p2)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDEQUALS, "CONDEQUALS"), root_1);

                adaptor.addChild(root_1, p1.tree);
                adaptor.addChild(root_1, p2.tree);

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 47:5: -> {p3!=null}? ^( CONDNOTEQUALS $p1 $p3)
            if (p3!=null) {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:47:20: ^( CONDNOTEQUALS $p1 $p3)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDNOTEQUALS, "CONDNOTEQUALS"), root_1);

                adaptor.addChild(root_1, p1.tree);
                adaptor.addChild(root_1, p3.tree);

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 48:5: -> {p4!=null}? ^( CONDLEQ $p1 $p4)
            if (p4!=null) {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:48:20: ^( CONDLEQ $p1 $p4)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDLEQ, "CONDLEQ"), root_1);

                adaptor.addChild(root_1, p1.tree);
                adaptor.addChild(root_1, p4.tree);

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 49:5: -> {p5!=null}? ^( CONDLT $p1 $p5)
            if (p5!=null) {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:49:20: ^( CONDLT $p1 $p5)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDLT, "CONDLT"), root_1);

                adaptor.addChild(root_1, p1.tree);
                adaptor.addChild(root_1, p5.tree);

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 50:5: -> {p6!=null}? ^( CONDGEQ $p1 $p6)
            if (p6!=null) {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:50:20: ^( CONDGEQ $p1 $p6)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDGEQ, "CONDGEQ"), root_1);

                adaptor.addChild(root_1, p1.tree);
                adaptor.addChild(root_1, p6.tree);

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 51:5: -> {p7!=null}? ^( CONDGT $p1 $p7)
            if (p7!=null) {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:51:20: ^( CONDGT $p1 $p7)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDGT, "CONDGT"), root_1);

                adaptor.addChild(root_1, p1.tree);
                adaptor.addChild(root_1, p7.tree);

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 52:5: -> {p8!=null}? ^( CONDMETHOD $p1 ID $p8)
            if (p8!=null) {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:52:20: ^( CONDMETHOD $p1 ID $p8)
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(CONDMETHOD, "CONDMETHOD"), root_1);

                adaptor.addChild(root_1, p1.tree);
                adaptor.addChild(root_1, (Token)list_ID.get(i_0));
                adaptor.addChild(root_1, p8.tree);

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 53:5: -> ^( CONDTERM $p1)
            {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:53:8: ^( CONDTERM $p1)
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
    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:55:1: pattern : ID LPAR ( term ( COMA term )* )? RPAR -> ^( APPL ID ( term )* ) ;
    public pattern_return pattern() throws RecognitionException {
        pattern_return retval = new pattern_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ID17=null;
        Token LPAR18=null;
        Token COMA20=null;
        Token RPAR22=null;
        term_return term19 = null;

        term_return term21 = null;

        List list_term=new ArrayList();
        List list_COMA=new ArrayList();
        List list_RPAR=new ArrayList();
        List list_LPAR=new ArrayList();
        List list_ID=new ArrayList();
        ASTTree ID17_tree=null;
        ASTTree LPAR18_tree=null;
        ASTTree COMA20_tree=null;
        ASTTree RPAR22_tree=null;

        try {
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:56:3: ( ID LPAR ( term ( COMA term )* )? RPAR -> ^( APPL ID ( term )* ) )
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:56:3: ID LPAR ( term ( COMA term )* )? RPAR
            {
            ID17=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_pattern442); 
            list_ID.add(ID17);

            LPAR18=(Token)input.LT(1);
            match(input,LPAR,FOLLOW_LPAR_in_pattern444); 
            list_LPAR.add(LPAR18);

            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:56:11: ( term ( COMA term )* )?
            int alt5=2;
            int LA5_0 = input.LA(1);
            if ( (LA5_0==ID||(LA5_0>=INT && LA5_0<=STRING)) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:56:12: term ( COMA term )*
                    {
                    pushFollow(FOLLOW_term_in_pattern447);
                    term19=term();
                    _fsp--;

                    list_term.add(term19.getTree());
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:56:17: ( COMA term )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);
                        if ( (LA4_0==COMA) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:56:18: COMA term
                    	    {
                    	    COMA20=(Token)input.LT(1);
                    	    match(input,COMA,FOLLOW_COMA_in_pattern450); 
                    	    list_COMA.add(COMA20);

                    	    pushFollow(FOLLOW_term_in_pattern452);
                    	    term21=term();
                    	    _fsp--;

                    	    list_term.add(term21.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAR22=(Token)input.LT(1);
            match(input,RPAR,FOLLOW_RPAR_in_pattern458); 
            list_RPAR.add(RPAR22);


            // AST REWRITE
            int i_0 = 0;
            retval.tree = root_0;
            root_0 = (ASTTree)adaptor.nil();
            // 56:37: -> ^( APPL ID ( term )* )
            {
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:56:40: ^( APPL ID ( term )* )
                {
                ASTTree root_1 = (ASTTree)adaptor.nil();
                root_1 = (ASTTree)adaptor.becomeRoot(adaptor.create(APPL, "APPL"), root_1);

                adaptor.addChild(root_1, (Token)list_ID.get(i_0));
                // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:56:50: ( term )*
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
    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:58:1: term : ( pattern | ID | builtin );
    public term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token ID24=null;
        pattern_return pattern23 = null;

        builtin_return builtin25 = null;


        ASTTree ID24_tree=null;

        try {
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:59:3: ( pattern | ID | builtin )
            int alt6=3;
            int LA6_0 = input.LA(1);
            if ( (LA6_0==ID) ) {
                int LA6_1 = input.LA(2);
                if ( (LA6_1==LPAR) ) {
                    alt6=1;
                }
                else if ( (LA6_1==EOF||(LA6_1>=IF && LA6_1<=ID)||(LA6_1>=RPAR && LA6_1<=COMA)) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("58:1: term : ( pattern | ID | builtin );", 6, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA6_0>=INT && LA6_0<=STRING)) ) {
                alt6=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("58:1: term : ( pattern | ID | builtin );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:59:3: pattern
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    pushFollow(FOLLOW_pattern_in_term481);
                    pattern23=pattern();
                    _fsp--;

                    adaptor.addChild(root_0, pattern23.getTree());

                    }
                    break;
                case 2 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:59:13: ID
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    ID24=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_term485); 
                    ID24_tree = (ASTTree)adaptor.create(ID24);
                    adaptor.addChild(root_0, ID24_tree);


                    }
                    break;
                case 3 :
                    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:59:18: builtin
                    {
                    root_0 = (ASTTree)adaptor.nil();

                    pushFollow(FOLLOW_builtin_in_term489);
                    builtin25=builtin();
                    _fsp--;

                    adaptor.addChild(root_0, builtin25.getTree());

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
    // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:61:1: builtin : (INT|STRING);
    public builtin_return builtin() throws RecognitionException {
        builtin_return retval = new builtin_return();
        retval.start = input.LT(1);

        ASTTree root_0 = null;

        Token set26=null;

        ASTTree set26_tree=null;

        try {
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:61:9: ( (INT|STRING))
            // /Users/tonio/workspace/jtom/src/tom/gom/expander/rule/Rule.g:62:3: (INT|STRING)
            {
            root_0 = (ASTTree)adaptor.nil();

            set26=(Token)input.LT(1);
            if ( (input.LA(1)>=INT && input.LA(1)<=STRING) ) {
                adaptor.addChild(root_0, adaptor.create(set26));
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_builtin501);    throw mse;
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


 

    public static final BitSet FOLLOW_rule_in_ruleset111 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_EOF_in_ruleset115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_rule138 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_ARROW_in_rule140 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_term_in_rule142 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_IF_in_rule145 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_condition_in_rule149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_condition198 = new BitSet(new long[]{0x0000000001FC0002L});
    public static final BitSet FOLLOW_EQUALS_in_condition201 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_term_in_condition205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUALS_in_condition213 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_term_in_condition217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEQ_in_condition225 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_term_in_condition229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_condition237 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_term_in_condition241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GEQ_in_condition249 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_term_in_condition253 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_condition261 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_term_in_condition265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_condition273 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_ID_in_condition275 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LPAR_in_condition277 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_term_in_condition281 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RPAR_in_condition283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern442 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LPAR_in_pattern444 = new BitSet(new long[]{0x000000006A000000L});
    public static final BitSet FOLLOW_term_in_pattern447 = new BitSet(new long[]{0x0000000018000000L});
    public static final BitSet FOLLOW_COMA_in_pattern450 = new BitSet(new long[]{0x0000000062000000L});
    public static final BitSet FOLLOW_term_in_pattern452 = new BitSet(new long[]{0x0000000018000000L});
    public static final BitSet FOLLOW_RPAR_in_pattern458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_in_term481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_term485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtin_in_term489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_builtin501 = new BitSet(new long[]{0x0000000000000002L});

}