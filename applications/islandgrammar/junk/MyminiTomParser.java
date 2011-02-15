// $ANTLR 3.2 Sep 23, 2009 12:02:23 ./src/MyminiTom.g 2011-01-13 12:35:43

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class MyminiTomParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PROGRAM", "CODE", "LEFTPAR", "RIGHTPAR", "LEFTBR", "RIGHTBR", "SEMICOLUMN", "A", "B", "WS", "'a'"
    };
    public static final int SEMICOLUMN=10;
    public static final int RIGHTBR=9;
    public static final int WS=13;
    public static final int A=11;
    public static final int B=12;
    public static final int T__14=14;
    public static final int RIGHTPAR=7;
    public static final int LEFTPAR=6;
    public static final int LEFTBR=8;
    public static final int PROGRAM=4;
    public static final int EOF=-1;
    public static final int CODE=5;

    // delegates
    // delegators


        public MyminiTomParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public MyminiTomParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return MyminiTomParser.tokenNames; }
    public String getGrammarFileName() { return "./src/MyminiTom.g"; }


    public static class program_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // ./src/MyminiTom.g:16:1: program : LEFTPAR RIGHTPAR LEFTBR ( code )* RIGHTBR -> ^( PROGRAM ( code )* ) ;
    public final MyminiTomParser.program_return program() throws RecognitionException {
        MyminiTomParser.program_return retval = new MyminiTomParser.program_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LEFTPAR1=null;
        Token RIGHTPAR2=null;
        Token LEFTBR3=null;
        Token RIGHTBR5=null;
        MyminiTomParser.code_return code4 = null;


        Tree LEFTPAR1_tree=null;
        Tree RIGHTPAR2_tree=null;
        Tree LEFTBR3_tree=null;
        Tree RIGHTBR5_tree=null;
        RewriteRuleTokenStream stream_LEFTPAR=new RewriteRuleTokenStream(adaptor,"token LEFTPAR");
        RewriteRuleTokenStream stream_LEFTBR=new RewriteRuleTokenStream(adaptor,"token LEFTBR");
        RewriteRuleTokenStream stream_RIGHTBR=new RewriteRuleTokenStream(adaptor,"token RIGHTBR");
        RewriteRuleTokenStream stream_RIGHTPAR=new RewriteRuleTokenStream(adaptor,"token RIGHTPAR");
        RewriteRuleSubtreeStream stream_code=new RewriteRuleSubtreeStream(adaptor,"rule code");
        try {
            // ./src/MyminiTom.g:16:9: ( LEFTPAR RIGHTPAR LEFTBR ( code )* RIGHTBR -> ^( PROGRAM ( code )* ) )
            // ./src/MyminiTom.g:16:11: LEFTPAR RIGHTPAR LEFTBR ( code )* RIGHTBR
            {
            LEFTPAR1=(Token)match(input,LEFTPAR,FOLLOW_LEFTPAR_in_program49);  
            stream_LEFTPAR.add(LEFTPAR1);

            RIGHTPAR2=(Token)match(input,RIGHTPAR,FOLLOW_RIGHTPAR_in_program51);  
            stream_RIGHTPAR.add(RIGHTPAR2);

            LEFTBR3=(Token)match(input,LEFTBR,FOLLOW_LEFTBR_in_program53);  
            stream_LEFTBR.add(LEFTBR3);

            // ./src/MyminiTom.g:16:35: ( code )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==14) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ./src/MyminiTom.g:16:35: code
            	    {
            	    pushFollow(FOLLOW_code_in_program55);
            	    code4=code();

            	    state._fsp--;

            	    stream_code.add(code4.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            RIGHTBR5=(Token)match(input,RIGHTBR,FOLLOW_RIGHTBR_in_program58);  
            stream_RIGHTBR.add(RIGHTBR5);



            // AST REWRITE
            // elements: code
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 16:49: -> ^( PROGRAM ( code )* )
            {
                // ./src/MyminiTom.g:16:52: ^( PROGRAM ( code )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(PROGRAM, "PROGRAM"), root_1);

                // ./src/MyminiTom.g:16:62: ( code )*
                while ( stream_code.hasNext() ) {
                    adaptor.addChild(root_1, stream_code.nextTree());

                }
                stream_code.reset();

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
    // $ANTLR end "program"

    public static class code_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "code"
    // ./src/MyminiTom.g:18:1: code : (s= statement SEMICOLUMN ) -> ^( CODE $s) ;
    public final MyminiTomParser.code_return code() throws RecognitionException {
        MyminiTomParser.code_return retval = new MyminiTomParser.code_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token SEMICOLUMN6=null;
        MyminiTomParser.statement_return s = null;


        Tree SEMICOLUMN6_tree=null;
        RewriteRuleTokenStream stream_SEMICOLUMN=new RewriteRuleTokenStream(adaptor,"token SEMICOLUMN");
        RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
        try {
            // ./src/MyminiTom.g:18:6: ( (s= statement SEMICOLUMN ) -> ^( CODE $s) )
            // ./src/MyminiTom.g:18:8: (s= statement SEMICOLUMN )
            {
            // ./src/MyminiTom.g:18:8: (s= statement SEMICOLUMN )
            // ./src/MyminiTom.g:18:9: s= statement SEMICOLUMN
            {
            pushFollow(FOLLOW_statement_in_code79);
            s=statement();

            state._fsp--;

            stream_statement.add(s.getTree());
            System.out.println((s!=null?input.toString(s.start,s.stop):null));
            SEMICOLUMN6=(Token)match(input,SEMICOLUMN,FOLLOW_SEMICOLUMN_in_code83);  
            stream_SEMICOLUMN.add(SEMICOLUMN6);


            }



            // AST REWRITE
            // elements: s
            // token labels: 
            // rule labels: retval, s
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 18:64: -> ^( CODE $s)
            {
                // ./src/MyminiTom.g:18:67: ^( CODE $s)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(CODE, "CODE"), root_1);

                adaptor.addChild(root_1, stream_s.nextTree());

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
    // $ANTLR end "code"

    public static class statement_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "statement"
    // ./src/MyminiTom.g:20:1: statement : ( 'a' )+ ;
    public final MyminiTomParser.statement_return statement() throws RecognitionException {
        MyminiTomParser.statement_return retval = new MyminiTomParser.statement_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token char_literal7=null;

        Tree char_literal7_tree=null;

        try {
            // ./src/MyminiTom.g:20:11: ( ( 'a' )+ )
            // ./src/MyminiTom.g:20:13: ( 'a' )+
            {
            root_0 = (Tree)adaptor.nil();

            // ./src/MyminiTom.g:20:13: ( 'a' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==14) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // ./src/MyminiTom.g:20:13: 'a'
            	    {
            	    char_literal7=(Token)match(input,14,FOLLOW_14_in_statement102); 
            	    char_literal7_tree = (Tree)adaptor.create(char_literal7);
            	    adaptor.addChild(root_0, char_literal7_tree);


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


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
    // $ANTLR end "statement"

    // Delegated rules


 

    public static final BitSet FOLLOW_LEFTPAR_in_program49 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RIGHTPAR_in_program51 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LEFTBR_in_program53 = new BitSet(new long[]{0x0000000000004200L});
    public static final BitSet FOLLOW_code_in_program55 = new BitSet(new long[]{0x0000000000004200L});
    public static final BitSet FOLLOW_RIGHTBR_in_program58 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_code79 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_SEMICOLUMN_in_code83 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_statement102 = new BitSet(new long[]{0x0000000000004002L});

}