// $ANTLR 3.2 Sep 23, 2009 12:02:23 ./src/miniTom.g 2010-12-13 17:43:07

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class miniTomParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "LEFTPAR", "RIGHTPAR", "LEFTBR", "RIGHTBR", "CODE", "A", "B", "BLANK", "';'", "'a'"
    };
    public static final int RIGHTBR=7;
    public static final int A=9;
    public static final int B=10;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int RIGHTPAR=5;
    public static final int BLANK=11;
    public static final int LEFTPAR=4;
    public static final int LEFTBR=6;
    public static final int EOF=-1;
    public static final int CODE=8;

    // delegates
    // delegators


        public miniTomParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public miniTomParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return miniTomParser.tokenNames; }
    public String getGrammarFileName() { return "./src/miniTom.g"; }


    public static class program_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // ./src/miniTom.g:17:1: program : LEFTPAR RIGHTPAR LEFTBR code RIGHTBR ;
    public final miniTomParser.program_return program() throws RecognitionException {
        miniTomParser.program_return retval = new miniTomParser.program_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFTPAR1=null;
        Token RIGHTPAR2=null;
        Token LEFTBR3=null;
        Token RIGHTBR5=null;
        miniTomParser.code_return code4 = null;


        CommonTree LEFTPAR1_tree=null;
        CommonTree RIGHTPAR2_tree=null;
        CommonTree LEFTBR3_tree=null;
        CommonTree RIGHTBR5_tree=null;

        try {
            // ./src/miniTom.g:18:2: ( LEFTPAR RIGHTPAR LEFTBR code RIGHTBR )
            // ./src/miniTom.g:18:4: LEFTPAR RIGHTPAR LEFTBR code RIGHTBR
            {
            root_0 = (CommonTree)adaptor.nil();

            LEFTPAR1=(Token)match(input,LEFTPAR,FOLLOW_LEFTPAR_in_program72); 
            LEFTPAR1_tree = (CommonTree)adaptor.create(LEFTPAR1);
            adaptor.addChild(root_0, LEFTPAR1_tree);

            RIGHTPAR2=(Token)match(input,RIGHTPAR,FOLLOW_RIGHTPAR_in_program74); 
            RIGHTPAR2_tree = (CommonTree)adaptor.create(RIGHTPAR2);
            adaptor.addChild(root_0, RIGHTPAR2_tree);

            LEFTBR3=(Token)match(input,LEFTBR,FOLLOW_LEFTBR_in_program76); 
            LEFTBR3_tree = (CommonTree)adaptor.create(LEFTBR3);
            adaptor.addChild(root_0, LEFTBR3_tree);

            pushFollow(FOLLOW_code_in_program78);
            code4=code();

            state._fsp--;

            adaptor.addChild(root_0, code4.getTree());
            RIGHTBR5=(Token)match(input,RIGHTBR,FOLLOW_RIGHTBR_in_program80); 
            RIGHTBR5_tree = (CommonTree)adaptor.create(RIGHTBR5);
            adaptor.addChild(root_0, RIGHTBR5_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "program"

    public static class code_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "code"
    // ./src/miniTom.g:21:1: code : (s= statement ';' ) -> ^( 'CODE' $s) ;
    public final miniTomParser.code_return code() throws RecognitionException {
        miniTomParser.code_return retval = new miniTomParser.code_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal6=null;
        miniTomParser.statement_return s = null;


        CommonTree char_literal6_tree=null;
        RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");
        RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
        try {
            // ./src/miniTom.g:22:2: ( (s= statement ';' ) -> ^( 'CODE' $s) )
            // ./src/miniTom.g:22:4: (s= statement ';' )
            {
            // ./src/miniTom.g:22:4: (s= statement ';' )
            // ./src/miniTom.g:22:5: s= statement ';'
            {
            pushFollow(FOLLOW_statement_in_code95);
            s=statement();

            state._fsp--;

            stream_statement.add(s.getTree());
            System.out.println((s!=null?input.toString(s.start,s.stop):null));
            char_literal6=(Token)match(input,12,FOLLOW_12_in_code99);  
            stream_12.add(char_literal6);


            }



            // AST REWRITE
            // elements: <INVALID>, s
            // token labels: 
            // rule labels: retval, s
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 22:53: -> ^( 'CODE' $s)
            {
                // ./src/miniTom.g:22:56: ^( 'CODE' $s)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                adaptor.addChild(root_1, stream_s.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "code"

    public static class statement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "statement"
    // ./src/miniTom.g:25:1: statement : ( 'a' )+ ;
    public final miniTomParser.statement_return statement() throws RecognitionException {
        miniTomParser.statement_return retval = new miniTomParser.statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal7=null;

        CommonTree char_literal7_tree=null;

        try {
            // ./src/miniTom.g:26:2: ( ( 'a' )+ )
            // ./src/miniTom.g:26:4: ( 'a' )+
            {
            root_0 = (CommonTree)adaptor.nil();

            // ./src/miniTom.g:26:4: ( 'a' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==13) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ./src/miniTom.g:26:4: 'a'
            	    {
            	    char_literal7=(Token)match(input,13,FOLLOW_13_in_statement120); 
            	    char_literal7_tree = (CommonTree)adaptor.create(char_literal7);
            	    adaptor.addChild(root_0, char_literal7_tree);


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "statement"

    // Delegated rules


 

    public static final BitSet FOLLOW_LEFTPAR_in_program72 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RIGHTPAR_in_program74 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_LEFTBR_in_program76 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_code_in_program78 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RIGHTBR_in_program80 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_code95 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_code99 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_statement120 = new BitSet(new long[]{0x0000000000002002L});

}