// $ANTLR 3.2 Sep 23, 2009 12:02:23 parser/Rec.g 2014-03-27 10:03:53

  package examples.parser;


import org.antlr.runtime.BitSet;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.RewriteRuleTokenStream;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeAdaptor;

public class RecParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ExpList", "NotExp", "False", "True", "SeqExp", "OpExp", "Num", "Id", "While", "If", "Print", "Assign", "Seq", "EmptyTable", "Table", "Pair", "Equal", "Or", "And", "Div", "Times", "Minus", "Plus", "ID", "INT", "WS", "SLCOMMENT", "MLCOMMENT", "';'", "':='", "'print'", "'('", "')'", "'+'", "'-'", "'*'", "'/'", "','"
    };
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int Pair=25;
    public static final int T__45=45;
    public static final int ExpList=10;
    public static final int SeqExp=14;
    public static final int Table=24;
    public static final int False=12;
    public static final int SLCOMMENT=36;
    public static final int INT=34;
    public static final int While=18;
    public static final int ID=33;
    public static final int Equal=26;
    public static final int EOF=-1;
    public static final int Seq=22;
    public static final int OpExp=15;
    public static final int Plus=32;
    public static final int Minus=31;
    public static final int WS=35;
    public static final int Num=16;
    public static final int Assign=21;
    public static final int NotExp=11;
    public static final int T__38=38;
    public static final int Times=30;
    public static final int T__39=39;
    public static final int Print=20;
    public static final int Or=27;
    public static final int True=13;
    public static final int Id=17;
    public static final int MLCOMMENT=37;
    public static final int If=19;
    public static final int EmptyTable=23;
    public static final int And=28;
    public static final int Div=29;

    // delegates
    // delegators


        public RecParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public RecParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return RecParser.tokenNames; }
    public String getGrammarFileName() { return "parser/Rec.g"; }


    public static class program_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // parser/Rec.g:16:1: program : stm ( ';' stm )* -> ^( Seq ( stm )* ) ;
    public final RecParser.program_return program() throws RecognitionException {
        RecParser.program_return retval = new RecParser.program_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token char_literal2=null;
        RecParser.stm_return stm1 = null;

        RecParser.stm_return stm3 = null;


        Tree char_literal2_tree=null;
        RewriteRuleTokenStream stream_38=new RewriteRuleTokenStream(adaptor,"token 38");
        RewriteRuleSubtreeStream stream_stm=new RewriteRuleSubtreeStream(adaptor,"rule stm");
        try {
            // parser/Rec.g:16:8: ( stm ( ';' stm )* -> ^( Seq ( stm )* ) )
            // parser/Rec.g:17:3: stm ( ';' stm )*
            {
            pushFollow(FOLLOW_stm_in_program53);
            stm1=stm();

            state._fsp--;

            stream_stm.add(stm1.getTree());
            // parser/Rec.g:17:7: ( ';' stm )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==38) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // parser/Rec.g:17:8: ';' stm
            	    {
            	    char_literal2=(Token)match(input,38,FOLLOW_38_in_program56);  
            	    stream_38.add(char_literal2);

            	    pushFollow(FOLLOW_stm_in_program58);
            	    stm3=stm();

            	    state._fsp--;

            	    stream_stm.add(stm3.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);



            // AST REWRITE
            // elements: stm
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 17:18: -> ^( Seq ( stm )* )
            {
                // parser/Rec.g:17:21: ^( Seq ( stm )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Seq, "Seq"), root_1);

                // parser/Rec.g:17:27: ( stm )*
                while ( stream_stm.hasNext() ) {
                    adaptor.addChild(root_1, stream_stm.nextTree());

                }
                stream_stm.reset();

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

    public static class stm_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "stm"
    // parser/Rec.g:20:1: stm : ( ID ':=' exp -> ^( Assign ID exp ) | 'print' '(' explist ')' -> ^( Print explist ) ) ;
    public final RecParser.stm_return stm() throws RecognitionException {
        RecParser.stm_return retval = new RecParser.stm_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID4=null;
        Token string_literal5=null;
        Token string_literal7=null;
        Token char_literal8=null;
        Token char_literal10=null;
        RecParser.exp_return exp6 = null;

        RecParser.explist_return explist9 = null;


        Tree ID4_tree=null;
        Tree string_literal5_tree=null;
        Tree string_literal7_tree=null;
        Tree char_literal8_tree=null;
        Tree char_literal10_tree=null;
        RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
        RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
        RewriteRuleTokenStream stream_40=new RewriteRuleTokenStream(adaptor,"token 40");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_39=new RewriteRuleTokenStream(adaptor,"token 39");
        RewriteRuleSubtreeStream stream_exp=new RewriteRuleSubtreeStream(adaptor,"rule exp");
        RewriteRuleSubtreeStream stream_explist=new RewriteRuleSubtreeStream(adaptor,"rule explist");
        try {
            // parser/Rec.g:20:4: ( ( ID ':=' exp -> ^( Assign ID exp ) | 'print' '(' explist ')' -> ^( Print explist ) ) )
            // parser/Rec.g:21:3: ( ID ':=' exp -> ^( Assign ID exp ) | 'print' '(' explist ')' -> ^( Print explist ) )
            {
            // parser/Rec.g:21:3: ( ID ':=' exp -> ^( Assign ID exp ) | 'print' '(' explist ')' -> ^( Print explist ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==ID) ) {
                alt2=1;
            }
            else if ( (LA2_0==40) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // parser/Rec.g:21:5: ID ':=' exp
                    {
                    ID4=(Token)match(input,ID,FOLLOW_ID_in_stm82);  
                    stream_ID.add(ID4);

                    string_literal5=(Token)match(input,39,FOLLOW_39_in_stm84);  
                    stream_39.add(string_literal5);

                    pushFollow(FOLLOW_exp_in_stm86);
                    exp6=exp();

                    state._fsp--;

                    stream_exp.add(exp6.getTree());


                    // AST REWRITE
                    // elements: ID, exp
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 21:17: -> ^( Assign ID exp )
                    {
                        // parser/Rec.g:21:20: ^( Assign ID exp )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Assign, "Assign"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_exp.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // parser/Rec.g:22:5: 'print' '(' explist ')'
                    {
                    string_literal7=(Token)match(input,40,FOLLOW_40_in_stm102);  
                    stream_40.add(string_literal7);

                    char_literal8=(Token)match(input,41,FOLLOW_41_in_stm104);  
                    stream_41.add(char_literal8);

                    pushFollow(FOLLOW_explist_in_stm106);
                    explist9=explist();

                    state._fsp--;

                    stream_explist.add(explist9.getTree());
                    char_literal10=(Token)match(input,42,FOLLOW_42_in_stm108);  
                    stream_42.add(char_literal10);



                    // AST REWRITE
                    // elements: explist
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 22:29: -> ^( Print explist )
                    {
                        // parser/Rec.g:22:32: ^( Print explist )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Print, "Print"), root_1);

                        adaptor.addChild(root_1, stream_explist.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }


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
    // $ANTLR end "stm"

    public static class exp_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exp"
    // parser/Rec.g:26:1: exp : e1= multexp ( '+' e2= multexp -> ^( OpExp $e1 ^( Plus ) $e2) | '-' e2= multexp -> ^( OpExp $e1 ^( Minus ) $e2) | -> $e1) ;
    public final RecParser.exp_return exp() throws RecognitionException {
        RecParser.exp_return retval = new RecParser.exp_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token char_literal11=null;
        Token char_literal12=null;
        RecParser.multexp_return e1 = null;

        RecParser.multexp_return e2 = null;


        Tree char_literal11_tree=null;
        Tree char_literal12_tree=null;
        RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
        RewriteRuleTokenStream stream_44=new RewriteRuleTokenStream(adaptor,"token 44");
        RewriteRuleSubtreeStream stream_multexp=new RewriteRuleSubtreeStream(adaptor,"rule multexp");
        try {
            // parser/Rec.g:26:4: (e1= multexp ( '+' e2= multexp -> ^( OpExp $e1 ^( Plus ) $e2) | '-' e2= multexp -> ^( OpExp $e1 ^( Minus ) $e2) | -> $e1) )
            // parser/Rec.g:27:5: e1= multexp ( '+' e2= multexp -> ^( OpExp $e1 ^( Plus ) $e2) | '-' e2= multexp -> ^( OpExp $e1 ^( Minus ) $e2) | -> $e1)
            {
            pushFollow(FOLLOW_multexp_in_exp135);
            e1=multexp();

            state._fsp--;

            stream_multexp.add(e1.getTree());
            // parser/Rec.g:27:16: ( '+' e2= multexp -> ^( OpExp $e1 ^( Plus ) $e2) | '-' e2= multexp -> ^( OpExp $e1 ^( Minus ) $e2) | -> $e1)
            int alt3=3;
            switch ( input.LA(1) ) {
            case 43:
                {
                alt3=1;
                }
                break;
            case 44:
                {
                alt3=2;
                }
                break;
            case EOF:
            case 38:
            case 42:
            case 47:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // parser/Rec.g:27:17: '+' e2= multexp
                    {
                    char_literal11=(Token)match(input,43,FOLLOW_43_in_exp138);  
                    stream_43.add(char_literal11);

                    pushFollow(FOLLOW_multexp_in_exp142);
                    e2=multexp();

                    state._fsp--;

                    stream_multexp.add(e2.getTree());


                    // AST REWRITE
                    // elements: e2, e1
                    // token labels: 
                    // rule labels: retval, e1, e2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_e1=new RewriteRuleSubtreeStream(adaptor,"rule e1",e1!=null?e1.tree:null);
                    RewriteRuleSubtreeStream stream_e2=new RewriteRuleSubtreeStream(adaptor,"rule e2",e2!=null?e2.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 27:32: -> ^( OpExp $e1 ^( Plus ) $e2)
                    {
                        // parser/Rec.g:27:35: ^( OpExp $e1 ^( Plus ) $e2)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(OpExp, "OpExp"), root_1);

                        adaptor.addChild(root_1, stream_e1.nextTree());
                        // parser/Rec.g:27:47: ^( Plus )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Plus, "Plus"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_e2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // parser/Rec.g:28:17: '-' e2= multexp
                    {
                    char_literal12=(Token)match(input,44,FOLLOW_44_in_exp176);  
                    stream_44.add(char_literal12);

                    pushFollow(FOLLOW_multexp_in_exp180);
                    e2=multexp();

                    state._fsp--;

                    stream_multexp.add(e2.getTree());


                    // AST REWRITE
                    // elements: e1, e2
                    // token labels: 
                    // rule labels: retval, e1, e2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_e1=new RewriteRuleSubtreeStream(adaptor,"rule e1",e1!=null?e1.tree:null);
                    RewriteRuleSubtreeStream stream_e2=new RewriteRuleSubtreeStream(adaptor,"rule e2",e2!=null?e2.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 28:32: -> ^( OpExp $e1 ^( Minus ) $e2)
                    {
                        // parser/Rec.g:28:35: ^( OpExp $e1 ^( Minus ) $e2)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(OpExp, "OpExp"), root_1);

                        adaptor.addChild(root_1, stream_e1.nextTree());
                        // parser/Rec.g:28:47: ^( Minus )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Minus, "Minus"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_e2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // parser/Rec.g:29:32: 
                    {

                    // AST REWRITE
                    // elements: e1
                    // token labels: 
                    // rule labels: retval, e1
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_e1=new RewriteRuleSubtreeStream(adaptor,"rule e1",e1!=null?e1.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 29:32: -> $e1
                    {
                        adaptor.addChild(root_0, stream_e1.nextTree());

                    }

                    retval.tree = root_0;
                    }
                    break;

            }


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
    // $ANTLR end "exp"

    public static class multexp_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "multexp"
    // parser/Rec.g:32:1: multexp : e1= atom ( '*' e2= atom -> ^( OpExp $e1 ^( Times ) $e2) | '/' e2= atom -> ^( OpExp $e1 ^( Div ) $e2) | -> $e1) ;
    public final RecParser.multexp_return multexp() throws RecognitionException {
        RecParser.multexp_return retval = new RecParser.multexp_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token char_literal13=null;
        Token char_literal14=null;
        RecParser.atom_return e1 = null;

        RecParser.atom_return e2 = null;


        Tree char_literal13_tree=null;
        Tree char_literal14_tree=null;
        RewriteRuleTokenStream stream_45=new RewriteRuleTokenStream(adaptor,"token 45");
        RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
        RewriteRuleSubtreeStream stream_atom=new RewriteRuleSubtreeStream(adaptor,"rule atom");
        try {
            // parser/Rec.g:32:8: (e1= atom ( '*' e2= atom -> ^( OpExp $e1 ^( Times ) $e2) | '/' e2= atom -> ^( OpExp $e1 ^( Div ) $e2) | -> $e1) )
            // parser/Rec.g:33:5: e1= atom ( '*' e2= atom -> ^( OpExp $e1 ^( Times ) $e2) | '/' e2= atom -> ^( OpExp $e1 ^( Div ) $e2) | -> $e1)
            {
            pushFollow(FOLLOW_atom_in_multexp247);
            e1=atom();

            state._fsp--;

            stream_atom.add(e1.getTree());
            // parser/Rec.g:33:13: ( '*' e2= atom -> ^( OpExp $e1 ^( Times ) $e2) | '/' e2= atom -> ^( OpExp $e1 ^( Div ) $e2) | -> $e1)
            int alt4=3;
            switch ( input.LA(1) ) {
            case 45:
                {
                alt4=1;
                }
                break;
            case 46:
                {
                alt4=2;
                }
                break;
            case EOF:
            case 38:
            case 42:
            case 43:
            case 44:
            case 47:
                {
                alt4=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // parser/Rec.g:33:14: '*' e2= atom
                    {
                    char_literal13=(Token)match(input,45,FOLLOW_45_in_multexp250);  
                    stream_45.add(char_literal13);

                    pushFollow(FOLLOW_atom_in_multexp254);
                    e2=atom();

                    state._fsp--;

                    stream_atom.add(e2.getTree());


                    // AST REWRITE
                    // elements: e1, e2
                    // token labels: 
                    // rule labels: retval, e1, e2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_e1=new RewriteRuleSubtreeStream(adaptor,"rule e1",e1!=null?e1.tree:null);
                    RewriteRuleSubtreeStream stream_e2=new RewriteRuleSubtreeStream(adaptor,"rule e2",e2!=null?e2.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 33:26: -> ^( OpExp $e1 ^( Times ) $e2)
                    {
                        // parser/Rec.g:33:29: ^( OpExp $e1 ^( Times ) $e2)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(OpExp, "OpExp"), root_1);

                        adaptor.addChild(root_1, stream_e1.nextTree());
                        // parser/Rec.g:33:41: ^( Times )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Times, "Times"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_e2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // parser/Rec.g:34:14: '/' e2= atom
                    {
                    char_literal14=(Token)match(input,46,FOLLOW_46_in_multexp285);  
                    stream_46.add(char_literal14);

                    pushFollow(FOLLOW_atom_in_multexp289);
                    e2=atom();

                    state._fsp--;

                    stream_atom.add(e2.getTree());


                    // AST REWRITE
                    // elements: e1, e2
                    // token labels: 
                    // rule labels: retval, e1, e2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_e1=new RewriteRuleSubtreeStream(adaptor,"rule e1",e1!=null?e1.tree:null);
                    RewriteRuleSubtreeStream stream_e2=new RewriteRuleSubtreeStream(adaptor,"rule e2",e2!=null?e2.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 34:26: -> ^( OpExp $e1 ^( Div ) $e2)
                    {
                        // parser/Rec.g:34:29: ^( OpExp $e1 ^( Div ) $e2)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(OpExp, "OpExp"), root_1);

                        adaptor.addChild(root_1, stream_e1.nextTree());
                        // parser/Rec.g:34:41: ^( Div )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Div, "Div"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_e2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // parser/Rec.g:35:26: 
                    {

                    // AST REWRITE
                    // elements: e1
                    // token labels: 
                    // rule labels: retval, e1
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_e1=new RewriteRuleSubtreeStream(adaptor,"rule e1",e1!=null?e1.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 35:26: -> $e1
                    {
                        adaptor.addChild(root_0, stream_e1.nextTree());

                    }

                    retval.tree = root_0;
                    }
                    break;

            }


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
    // $ANTLR end "multexp"

    public static class atom_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atom"
    // parser/Rec.g:38:1: atom : ( INT -> ^( Num INT ) | ID -> ^( Id ID ) | '(' stm ',' exp ')' -> ^( SeqExp stm exp ) ) ;
    public final RecParser.atom_return atom() throws RecognitionException {
        RecParser.atom_return retval = new RecParser.atom_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token INT15=null;
        Token ID16=null;
        Token char_literal17=null;
        Token char_literal19=null;
        Token char_literal21=null;
        RecParser.stm_return stm18 = null;

        RecParser.exp_return exp20 = null;


        Tree INT15_tree=null;
        Tree ID16_tree=null;
        Tree char_literal17_tree=null;
        Tree char_literal19_tree=null;
        Tree char_literal21_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_exp=new RewriteRuleSubtreeStream(adaptor,"rule exp");
        RewriteRuleSubtreeStream stream_stm=new RewriteRuleSubtreeStream(adaptor,"rule stm");
        try {
            // parser/Rec.g:38:5: ( ( INT -> ^( Num INT ) | ID -> ^( Id ID ) | '(' stm ',' exp ')' -> ^( SeqExp stm exp ) ) )
            // parser/Rec.g:39:3: ( INT -> ^( Num INT ) | ID -> ^( Id ID ) | '(' stm ',' exp ')' -> ^( SeqExp stm exp ) )
            {
            // parser/Rec.g:39:3: ( INT -> ^( Num INT ) | ID -> ^( Id ID ) | '(' stm ',' exp ')' -> ^( SeqExp stm exp ) )
            int alt5=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt5=1;
                }
                break;
            case ID:
                {
                alt5=2;
                }
                break;
            case 41:
                {
                alt5=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // parser/Rec.g:39:5: INT
                    {
                    INT15=(Token)match(input,INT,FOLLOW_INT_in_atom348);  
                    stream_INT.add(INT15);



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
                    // 39:9: -> ^( Num INT )
                    {
                        // parser/Rec.g:39:12: ^( Num INT )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Num, "Num"), root_1);

                        adaptor.addChild(root_1, stream_INT.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // parser/Rec.g:40:5: ID
                    {
                    ID16=(Token)match(input,ID,FOLLOW_ID_in_atom362);  
                    stream_ID.add(ID16);



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
                    // 40:8: -> ^( Id ID )
                    {
                        // parser/Rec.g:40:11: ^( Id ID )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Id, "Id"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // parser/Rec.g:41:5: '(' stm ',' exp ')'
                    {
                    char_literal17=(Token)match(input,41,FOLLOW_41_in_atom376);  
                    stream_41.add(char_literal17);

                    pushFollow(FOLLOW_stm_in_atom378);
                    stm18=stm();

                    state._fsp--;

                    stream_stm.add(stm18.getTree());
                    char_literal19=(Token)match(input,47,FOLLOW_47_in_atom380);  
                    stream_47.add(char_literal19);

                    pushFollow(FOLLOW_exp_in_atom382);
                    exp20=exp();

                    state._fsp--;

                    stream_exp.add(exp20.getTree());
                    char_literal21=(Token)match(input,42,FOLLOW_42_in_atom384);  
                    stream_42.add(char_literal21);



                    // AST REWRITE
                    // elements: stm, exp
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 41:25: -> ^( SeqExp stm exp )
                    {
                        // parser/Rec.g:41:28: ^( SeqExp stm exp )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SeqExp, "SeqExp"), root_1);

                        adaptor.addChild(root_1, stream_stm.nextTree());
                        adaptor.addChild(root_1, stream_exp.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }


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
    // $ANTLR end "atom"

    public static class explist_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "explist"
    // parser/Rec.g:45:1: explist : exp ( ',' exp )* -> ^( ExpList ( exp )* ) ;
    public final RecParser.explist_return explist() throws RecognitionException {
        RecParser.explist_return retval = new RecParser.explist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token char_literal23=null;
        RecParser.exp_return exp22 = null;

        RecParser.exp_return exp24 = null;


        Tree char_literal23_tree=null;
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleSubtreeStream stream_exp=new RewriteRuleSubtreeStream(adaptor,"rule exp");
        try {
            // parser/Rec.g:45:8: ( exp ( ',' exp )* -> ^( ExpList ( exp )* ) )
            // parser/Rec.g:46:3: exp ( ',' exp )*
            {
            pushFollow(FOLLOW_exp_in_explist408);
            exp22=exp();

            state._fsp--;

            stream_exp.add(exp22.getTree());
            // parser/Rec.g:46:7: ( ',' exp )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==47) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // parser/Rec.g:46:8: ',' exp
            	    {
            	    char_literal23=(Token)match(input,47,FOLLOW_47_in_explist411);  
            	    stream_47.add(char_literal23);

            	    pushFollow(FOLLOW_exp_in_explist413);
            	    exp24=exp();

            	    state._fsp--;

            	    stream_exp.add(exp24.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);



            // AST REWRITE
            // elements: exp
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 46:18: -> ^( ExpList ( exp )* )
            {
                // parser/Rec.g:46:21: ^( ExpList ( exp )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpList, "ExpList"), root_1);

                // parser/Rec.g:46:31: ( exp )*
                while ( stream_exp.hasNext() ) {
                    adaptor.addChild(root_1, stream_exp.nextTree());

                }
                stream_exp.reset();

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
    // $ANTLR end "explist"

    // Delegated rules


 

    public static final BitSet FOLLOW_stm_in_program53 = new BitSet(new long[]{0x0000004000000002L});
    public static final BitSet FOLLOW_38_in_program56 = new BitSet(new long[]{0x0000010200000000L});
    public static final BitSet FOLLOW_stm_in_program58 = new BitSet(new long[]{0x0000004000000002L});
    public static final BitSet FOLLOW_ID_in_stm82 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_stm84 = new BitSet(new long[]{0x0000020600000000L});
    public static final BitSet FOLLOW_exp_in_stm86 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_stm102 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_stm104 = new BitSet(new long[]{0x0000020600000000L});
    public static final BitSet FOLLOW_explist_in_stm106 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_stm108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multexp_in_exp135 = new BitSet(new long[]{0x0000180000000002L});
    public static final BitSet FOLLOW_43_in_exp138 = new BitSet(new long[]{0x0000020600000000L});
    public static final BitSet FOLLOW_multexp_in_exp142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_exp176 = new BitSet(new long[]{0x0000020600000000L});
    public static final BitSet FOLLOW_multexp_in_exp180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_multexp247 = new BitSet(new long[]{0x0000600000000002L});
    public static final BitSet FOLLOW_45_in_multexp250 = new BitSet(new long[]{0x0000020600000000L});
    public static final BitSet FOLLOW_atom_in_multexp254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_multexp285 = new BitSet(new long[]{0x0000020600000000L});
    public static final BitSet FOLLOW_atom_in_multexp289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_atom348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_atom376 = new BitSet(new long[]{0x0000010200000000L});
    public static final BitSet FOLLOW_stm_in_atom378 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_atom380 = new BitSet(new long[]{0x0000020600000000L});
    public static final BitSet FOLLOW_exp_in_atom382 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_atom384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exp_in_explist408 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_47_in_explist411 = new BitSet(new long[]{0x0000020600000000L});
    public static final BitSet FOLLOW_exp_in_explist413 = new BitSet(new long[]{0x0000800000000002L});

}