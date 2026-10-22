// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g 2018-01-15 14:09:46

  package tom.gom.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class SimpleBlockParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "STRING", "LBRACE", "RBRACE", "ESC", "OctalESC", "SL_COMMENT", "ML_COMMENT", "TARGET"
    };
    public static final int ML_COMMENT=10;
    public static final int RBRACE=6;
    public static final int OctalESC=8;
    public static final int ESC=7;
    public static final int SL_COMMENT=9;
    public static final int LBRACE=5;
    public static final int TARGET=11;
    public static final int EOF=-1;
    public static final int STRING=4;

    // delegates
    // delegators


        public SimpleBlockParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public SimpleBlockParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return SimpleBlockParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g"; }


    public static class block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "block"
    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:42:1: block : rawblocklist ;
    public final SimpleBlockParser.block_return block() throws RecognitionException {
        SimpleBlockParser.block_return retval = new SimpleBlockParser.block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SimpleBlockParser.rawblocklist_return rawblocklist1 = null;



        try {
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:42:7: ( rawblocklist )
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:43:3: rawblocklist
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_rawblocklist_in_block50);
            rawblocklist1=rawblocklist();

            state._fsp--;

            adaptor.addChild(root_0, rawblocklist1.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "block"

    public static class rawblocklist_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "rawblocklist"
    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:46:1: rawblocklist : ( STRING | LBRACE rawblocklist RBRACE )* ;
    public final SimpleBlockParser.rawblocklist_return rawblocklist() throws RecognitionException {
        SimpleBlockParser.rawblocklist_return retval = new SimpleBlockParser.rawblocklist_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRING2=null;
        Token LBRACE3=null;
        Token RBRACE5=null;
        SimpleBlockParser.rawblocklist_return rawblocklist4 = null;


        Object STRING2_tree=null;
        Object LBRACE3_tree=null;
        Object RBRACE5_tree=null;

        try {
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:46:14: ( ( STRING | LBRACE rawblocklist RBRACE )* )
            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:47:3: ( STRING | LBRACE rawblocklist RBRACE )*
            {
            root_0 = (Object)adaptor.nil();

            // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:47:3: ( STRING | LBRACE rawblocklist RBRACE )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==STRING) ) {
                    alt1=1;
                }
                else if ( (LA1_0==LBRACE) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:47:4: STRING
            	    {
            	    STRING2=(Token)match(input,STRING,FOLLOW_STRING_in_rawblocklist64); 
            	    STRING2_tree = (Object)adaptor.create(STRING2);
            	    adaptor.addChild(root_0, STRING2_tree);


            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/SimpleBlock.g:47:13: LBRACE rawblocklist RBRACE
            	    {
            	    LBRACE3=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_rawblocklist68); 
            	    LBRACE3_tree = (Object)adaptor.create(LBRACE3);
            	    adaptor.addChild(root_0, LBRACE3_tree);

            	    pushFollow(FOLLOW_rawblocklist_in_rawblocklist70);
            	    rawblocklist4=rawblocklist();

            	    state._fsp--;

            	    adaptor.addChild(root_0, rawblocklist4.getTree());
            	    RBRACE5=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_rawblocklist72); 
            	    RBRACE5_tree = (Object)adaptor.create(RBRACE5);
            	    adaptor.addChild(root_0, RBRACE5_tree);


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "rawblocklist"

    // Delegated rules


 

    public static final BitSet FOLLOW_rawblocklist_in_block50 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_rawblocklist64 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_LBRACE_in_rawblocklist68 = new BitSet(new long[]{0x0000000000000070L});
    public static final BitSet FOLLOW_rawblocklist_in_rawblocklist70 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RBRACE_in_rawblocklist72 = new BitSet(new long[]{0x0000000000000032L});

}