// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g 2018-01-15 14:09:45

package tom.gom.parser;
import tom.gom.GomStreamManager;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class GomLanguageParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ConcClassName", "MappingHook", "ImportHook", "InterfaceHook", "BlockHook", "MakeBeforeHook", "MakeHook", "SlotField", "ConcSlotField", "TomMapping", "VariadicOperatorClass", "OperatorClass", "SortClass", "AbstractTypeClass", "ClassName", "ConcGomClass", "ConcHook", "Sort", "ConcSort", "Variadic", "Slots", "ConcAtom", "FutureCons", "FutureNil", "HookKind", "ConcField", "ModHookPair", "KindFutureOperator", "KindOperator", "KindSort", "KindModule", "ConcOperator", "ConcModuleDecl", "Details", "HasTomCode", "OptionList", "Origin", "NamedField", "StarredField", "Refresh", "Neutral", "Outer", "Inner", "None", "ModuleDecl", "ConcSortDecl", "Alternative", "GomModuleName", "ConcSlot", "CutFutureOperator", "CutOperator", "CutSort", "CutModule", "Arg", "ConcSection", "AtomDecl", "SortType", "Hook", "ConcGomModule", "OperatorDecl", "BuiltinSortDecl", "SortDecl", "ConcProduction", "ConcGomType", "ConcHookDecl", "MappingHookDecl", "ImportHookDecl", "InterfaceHookDecl", "BlockHookDecl", "MakeHookDecl", "Slot", "GomType", "ConcImportedModule", "Public", "Imports", "ConcModule", "GomModule", "ConcAlternative", "ConcArg", "Module", "AtomType", "ExpressionType", "PatternType", "CodeList", "Compare", "FullOperatorClass", "ShortSortClass", "FullSortClass", "Cons", "Empty", "IsCons", "IsEmpty", "Code", "MODULE", "ID", "DOT", "IMPORTS", "PUBLIC", "ABSTRACT", "SYNTAX", "ATOM", "EQUALS", "BINDS", "JAVADOC", "ALT", "SEMI", "LPAREN", "COMMA", "RPAREN", "STAR", "LDIPLE", "RDIPLE", "COLON", "INNER", "OUTER", "NEUTRAL", "LBRACE", "SORT", "OPERATOR", "PRIVATE", "ARROW", "RBRACE", "WS", "SLCOMMENT", "MLCOMMENT"
    };
    public static final int Arg=63;
    public static final int STAR=119;
    public static final int OptionList=45;
    public static final int Cons=98;
    public static final int Hook=67;
    public static final int Origin=46;
    public static final int Module=89;
    public static final int AbstractTypeClass=23;
    public static final int GomModule=86;
    public static final int Empty=99;
    public static final int ExpressionType=91;
    public static final int SLCOMMENT=133;
    public static final int EQUALS=111;
    public static final int Inner=52;
    public static final int PatternType=92;
    public static final int ATOM=110;
    public static final int Slot=80;
    public static final int EOF=-1;
    public static final int ImportHookDecl=76;
    public static final int ClassName=24;
    public static final int OperatorClass=21;
    public static final int BINDS=112;
    public static final int FullOperatorClass=95;
    public static final int ConcImportedModule=82;
    public static final int ModuleDecl=54;
    public static final int RPAREN=118;
    public static final int ImportHook=12;
    public static final int ConcArg=88;
    public static final int Neutral=50;
    public static final int SortClass=22;
    public static final int StarredField=48;
    public static final int ConcHookDecl=74;
    public static final int ConcAtom=31;
    public static final int MLCOMMENT=134;
    public static final int GomType=81;
    public static final int ConcGomModule=68;
    public static final int ConcSection=64;
    public static final int RBRACE=131;
    public static final int Details=43;
    public static final int PRIVATE=129;
    public static final int CodeList=93;
    public static final int Compare=94;
    public static final int Code=102;
    public static final int BlockHookDecl=78;
    public static final int NEUTRAL=125;
    public static final int None=53;
    public static final int SlotField=17;
    public static final int ConcHook=26;
    public static final int ConcSlot=58;
    public static final int WS=132;
    public static final int BuiltinSortDecl=70;
    public static final int BlockHook=14;
    public static final int IsEmpty=101;
    public static final int AtomType=90;
    public static final int Outer=51;
    public static final int IMPORTS=106;
    public static final int ConcField=35;
    public static final int ShortSortClass=96;
    public static final int LDIPLE=120;
    public static final int Alternative=56;
    public static final int ConcOperator=41;
    public static final int GomModuleName=57;
    public static final int INNER=123;
    public static final int ConcSortDecl=55;
    public static final int LBRACE=126;
    public static final int KindModule=40;
    public static final int NamedField=47;
    public static final int ConcModuleDecl=42;
    public static final int SortDecl=71;
    public static final int ABSTRACT=108;
    public static final int AtomDecl=65;
    public static final int CutModule=62;
    public static final int ConcModule=85;
    public static final int ID=104;
    public static final int ConcSlotField=18;
    public static final int CutOperator=60;
    public static final int LPAREN=116;
    public static final int FullSortClass=97;
    public static final int InterfaceHookDecl=77;
    public static final int TomMapping=19;
    public static final int SYNTAX=109;
    public static final int Sort=27;
    public static final int MakeHook=16;
    public static final int Variadic=29;
    public static final int ALT=114;
    public static final int MappingHook=11;
    public static final int COMMA=117;
    public static final int CutSort=61;
    public static final int IsCons=100;
    public static final int ConcGomType=73;
    public static final int KindSort=39;
    public static final int ConcGomClass=25;
    public static final int DOT=105;
    public static final int HasTomCode=44;
    public static final int ConcClassName=10;
    public static final int FutureCons=32;
    public static final int OperatorDecl=69;
    public static final int OUTER=124;
    public static final int MakeHookDecl=79;
    public static final int JAVADOC=113;
    public static final int ConcAlternative=87;
    public static final int RDIPLE=121;
    public static final int ConcProduction=72;
    public static final int OPERATOR=128;
    public static final int InterfaceHook=13;
    public static final int ConcSort=28;
    public static final int ModHookPair=36;
    public static final int SORT=127;
    public static final int CutFutureOperator=59;
    public static final int FutureNil=33;
    public static final int MODULE=103;
    public static final int Imports=84;
    public static final int SEMI=115;
    public static final int Public=83;
    public static final int COLON=122;
    public static final int MappingHookDecl=75;
    public static final int MakeBeforeHook=15;
    public static final int VariadicOperatorClass=20;
    public static final int Refresh=49;
    public static final int Slots=30;
    public static final int HookKind=34;
    public static final int SortType=66;
    public static final int ARROW=130;
    public static final int KindOperator=38;
    public static final int PUBLIC=107;
    public static final int KindFutureOperator=37;

    // delegates
    // delegators


        public GomLanguageParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public GomLanguageParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return GomLanguageParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g"; }


      private GomStreamManager streamManager;
      public GomLanguageParser(TokenStream input, GomStreamManager streamManager) {
        super(input);
        this.streamManager = streamManager;
      }


    public static class module_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "module"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:49:1: module : MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) ;
    public final GomLanguageParser.module_return module() throws RecognitionException {
        GomLanguageParser.module_return retval = new GomLanguageParser.module_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token MODULE1=null;
        Token EOF4=null;
        GomLanguageParser.imports_return imps = null;

        GomLanguageParser.modulename_return modulename2 = null;

        GomLanguageParser.section_return section3 = null;


        Tree MODULE1_tree=null;
        Tree EOF4_tree=null;
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_modulename=new RewriteRuleSubtreeStream(adaptor,"rule modulename");
        RewriteRuleSubtreeStream stream_imports=new RewriteRuleSubtreeStream(adaptor,"rule imports");
        RewriteRuleSubtreeStream stream_section=new RewriteRuleSubtreeStream(adaptor,"rule section");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:49:8: ( MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:50:2: MODULE modulename (imps= imports )? section EOF
            {
            MODULE1=(Token)match(input,MODULE,FOLLOW_MODULE_in_module60);  
            stream_MODULE.add(MODULE1);

            pushFollow(FOLLOW_modulename_in_module62);
            modulename2=modulename();

            state._fsp--;

            stream_modulename.add(modulename2.getTree());
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:50:20: (imps= imports )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==IMPORTS) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:50:21: imps= imports
                    {
                    pushFollow(FOLLOW_imports_in_module67);
                    imps=imports();

                    state._fsp--;

                    stream_imports.add(imps.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_section_in_module71);
            section3=section();

            state._fsp--;

            stream_section.add(section3.getTree());
            EOF4=(Token)match(input,EOF,FOLLOW_EOF_in_module73);  
            stream_EOF.add(EOF4);



            // AST REWRITE
            // elements: section, modulename, section, modulename, imports
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 51:3: -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) )
            if (imps!=null) {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:51:20: ^( GomModule modulename ^( ConcSection imports section ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:51:43: ^( ConcSection imports section )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcSection, "ConcSection"), root_2);

                adaptor.addChild(root_2, stream_imports.nextTree());
                adaptor.addChild(root_2, stream_section.nextTree());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 52:3: -> ^( GomModule modulename ^( ConcSection section ) )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:52:6: ^( GomModule modulename ^( ConcSection section ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:52:29: ^( ConcSection section )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcSection, "ConcSection"), root_2);

                adaptor.addChild(root_2, stream_section.nextTree());

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
    // $ANTLR end "module"

    public static class modulename_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "modulename"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:55:1: modulename : (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) ;
    public final GomLanguageParser.modulename_return modulename() throws RecognitionException {
        GomLanguageParser.modulename_return retval = new GomLanguageParser.modulename_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token mod=null;
        Token moduleName=null;
        Token DOT5=null;

        Tree mod_tree=null;
        Tree moduleName_tree=null;
        Tree DOT5_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");


          StringBuilder packagePrefix = new StringBuilder("");

        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:58:3: ( (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:59:3: (mod= ID DOT )* moduleName= ID
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:59:3: (mod= ID DOT )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==ID) ) {
                    int LA2_1 = input.LA(2);

                    if ( (LA2_1==DOT) ) {
                        alt2=1;
                    }


                }


                switch (alt2) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:59:4: mod= ID DOT
            	    {
            	    mod=(Token)match(input,ID,FOLLOW_ID_in_modulename130);  
            	    stream_ID.add(mod);

            	    DOT5=(Token)match(input,DOT,FOLLOW_DOT_in_modulename132);  
            	    stream_DOT.add(DOT5);

            	     packagePrefix.append((mod!=null?mod.getText():null)+"."); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            moduleName=(Token)match(input,ID,FOLLOW_ID_in_modulename142);  
            stream_ID.add(moduleName);


                if (packagePrefix.length() > 0) {
                  packagePrefix.deleteCharAt(packagePrefix.length()-1);
                  if (null != streamManager) {
                    streamManager.associatePackagePath((moduleName!=null?moduleName.getText():null),packagePrefix.toString());
                  }
                }
              


            // AST REWRITE
            // elements: moduleName
            // token labels: moduleName
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_moduleName=new RewriteRuleTokenStream(adaptor,"token moduleName",moduleName);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 69:3: -> ^( GomModuleName $moduleName)
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:69:6: ^( GomModuleName $moduleName)
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModuleName, "GomModuleName"), root_1);

                adaptor.addChild(root_1, stream_moduleName.nextNode());

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
    // $ANTLR end "modulename"

    public static class imports_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "imports"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:72:1: imports : IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) ;
    public final GomLanguageParser.imports_return imports() throws RecognitionException {
        GomLanguageParser.imports_return retval = new GomLanguageParser.imports_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token IMPORTS6=null;
        GomLanguageParser.importedModuleName_return importedModuleName7 = null;


        Tree IMPORTS6_tree=null;
        RewriteRuleTokenStream stream_IMPORTS=new RewriteRuleTokenStream(adaptor,"token IMPORTS");
        RewriteRuleSubtreeStream stream_importedModuleName=new RewriteRuleSubtreeStream(adaptor,"rule importedModuleName");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:72:9: ( IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:73:3: IMPORTS ( importedModuleName )*
            {
            IMPORTS6=(Token)match(input,IMPORTS,FOLLOW_IMPORTS_in_imports170);  
            stream_IMPORTS.add(IMPORTS6);

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:73:11: ( importedModuleName )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:73:12: importedModuleName
            	    {
            	    pushFollow(FOLLOW_importedModuleName_in_imports173);
            	    importedModuleName7=importedModuleName();

            	    state._fsp--;

            	    stream_importedModuleName.add(importedModuleName7.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);



            // AST REWRITE
            // elements: importedModuleName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 73:33: -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:73:36: ^( Imports ^( ConcImportedModule ( importedModuleName )* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Imports, "Imports"), root_1);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:73:46: ^( ConcImportedModule ( importedModuleName )* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcImportedModule, "ConcImportedModule"), root_2);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:73:67: ( importedModuleName )*
                while ( stream_importedModuleName.hasNext() ) {
                    adaptor.addChild(root_2, stream_importedModuleName.nextTree());

                }
                stream_importedModuleName.reset();

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
    // $ANTLR end "imports"

    public static class importedModuleName_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "importedModuleName"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:75:1: importedModuleName : ID -> ^( GomModuleName ID ) ;
    public final GomLanguageParser.importedModuleName_return importedModuleName() throws RecognitionException {
        GomLanguageParser.importedModuleName_return retval = new GomLanguageParser.importedModuleName_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID8=null;

        Tree ID8_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:75:20: ( ID -> ^( GomModuleName ID ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:76:3: ID
            {
            ID8=(Token)match(input,ID,FOLLOW_ID_in_importedModuleName202);  
            stream_ID.add(ID8);



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
            // 76:6: -> ^( GomModuleName ID )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:76:9: ^( GomModuleName ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModuleName, "GomModuleName"), root_1);

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
    // $ANTLR end "importedModuleName"

    public static class section_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "section"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:79:1: section : ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) ;
    public final GomLanguageParser.section_return section() throws RecognitionException {
        GomLanguageParser.section_return retval = new GomLanguageParser.section_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token PUBLIC9=null;
        GomLanguageParser.adtgrammar_return adtgrammar10 = null;


        Tree PUBLIC9_tree=null;
        RewriteRuleTokenStream stream_PUBLIC=new RewriteRuleTokenStream(adaptor,"token PUBLIC");
        RewriteRuleSubtreeStream stream_adtgrammar=new RewriteRuleSubtreeStream(adaptor,"rule adtgrammar");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:79:9: ( ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:80:3: ( PUBLIC )? adtgrammar
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:80:3: ( PUBLIC )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==PUBLIC) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:80:4: PUBLIC
                    {
                    PUBLIC9=(Token)match(input,PUBLIC,FOLLOW_PUBLIC_in_section224);  
                    stream_PUBLIC.add(PUBLIC9);


                    }
                    break;

            }

            pushFollow(FOLLOW_adtgrammar_in_section228);
            adtgrammar10=adtgrammar();

            state._fsp--;

            stream_adtgrammar.add(adtgrammar10.getTree());


            // AST REWRITE
            // elements: adtgrammar
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 80:24: -> ^( Public adtgrammar )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:80:27: ^( Public adtgrammar )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Public, "Public"), root_1);

                adaptor.addChild(root_1, stream_adtgrammar.nextTree());

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
    // $ANTLR end "section"

    public static class adtgrammar_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "adtgrammar"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:83:1: adtgrammar : (gr+= syntax )* -> $gr;
    public final GomLanguageParser.adtgrammar_return adtgrammar() throws RecognitionException {
        GomLanguageParser.adtgrammar_return retval = new GomLanguageParser.adtgrammar_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        List list_gr=null;
        RuleReturnScope gr = null;
        RewriteRuleSubtreeStream stream_syntax=new RewriteRuleSubtreeStream(adaptor,"rule syntax");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:83:12: ( (gr+= syntax )* -> $gr)
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:84:3: (gr+= syntax )*
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:84:3: (gr+= syntax )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==ABSTRACT) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:84:4: gr+= syntax
            	    {
            	    pushFollow(FOLLOW_syntax_in_adtgrammar252);
            	    gr=syntax();

            	    state._fsp--;

            	    stream_syntax.add(gr.getTree());
            	    if (list_gr==null) list_gr=new ArrayList();
            	    list_gr.add(gr.getTree());


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);



            // AST REWRITE
            // elements: gr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: gr
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_gr=new RewriteRuleSubtreeStream(adaptor,"token gr",list_gr);
            root_0 = (Tree)adaptor.nil();
            // 84:17: -> $gr
            {
                adaptor.addChild(root_0, stream_gr.nextTree());

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
    // $ANTLR end "adtgrammar"

    public static class syntax_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "syntax"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:87:1: syntax : ( ABSTRACT SYNTAX ) (gr1+= hookConstruct | gr2+= typedecl | gr3+= atomdecl )* -> ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* ) ;
    public final GomLanguageParser.syntax_return syntax() throws RecognitionException {
        GomLanguageParser.syntax_return retval = new GomLanguageParser.syntax_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ABSTRACT11=null;
        Token SYNTAX12=null;
        List list_gr1=null;
        List list_gr2=null;
        List list_gr3=null;
        RuleReturnScope gr1 = null;
        RuleReturnScope gr2 = null;
        RuleReturnScope gr3 = null;
        Tree ABSTRACT11_tree=null;
        Tree SYNTAX12_tree=null;
        RewriteRuleTokenStream stream_SYNTAX=new RewriteRuleTokenStream(adaptor,"token SYNTAX");
        RewriteRuleTokenStream stream_ABSTRACT=new RewriteRuleTokenStream(adaptor,"token ABSTRACT");
        RewriteRuleSubtreeStream stream_hookConstruct=new RewriteRuleSubtreeStream(adaptor,"rule hookConstruct");
        RewriteRuleSubtreeStream stream_typedecl=new RewriteRuleSubtreeStream(adaptor,"rule typedecl");
        RewriteRuleSubtreeStream stream_atomdecl=new RewriteRuleSubtreeStream(adaptor,"rule atomdecl");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:87:8: ( ( ABSTRACT SYNTAX ) (gr1+= hookConstruct | gr2+= typedecl | gr3+= atomdecl )* -> ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:88:3: ( ABSTRACT SYNTAX ) (gr1+= hookConstruct | gr2+= typedecl | gr3+= atomdecl )*
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:88:3: ( ABSTRACT SYNTAX )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:88:4: ABSTRACT SYNTAX
            {
            ABSTRACT11=(Token)match(input,ABSTRACT,FOLLOW_ABSTRACT_in_syntax273);  
            stream_ABSTRACT.add(ABSTRACT11);

            SYNTAX12=(Token)match(input,SYNTAX,FOLLOW_SYNTAX_in_syntax275);  
            stream_SYNTAX.add(SYNTAX12);


            }

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:88:21: (gr1+= hookConstruct | gr2+= typedecl | gr3+= atomdecl )*
            loop6:
            do {
                int alt6=4;
                switch ( input.LA(1) ) {
                case MODULE:
                case SORT:
                case OPERATOR:
                    {
                    alt6=1;
                    }
                    break;
                case ID:
                    {
                    int LA6_3 = input.LA(2);

                    if ( (LA6_3==COLON) ) {
                        alt6=1;
                    }
                    else if ( ((LA6_3>=EQUALS && LA6_3<=BINDS)) ) {
                        alt6=2;
                    }


                    }
                    break;
                case ATOM:
                    {
                    alt6=3;
                    }
                    break;

                }

                switch (alt6) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:88:22: gr1+= hookConstruct
            	    {
            	    pushFollow(FOLLOW_hookConstruct_in_syntax281);
            	    gr1=hookConstruct();

            	    state._fsp--;

            	    stream_hookConstruct.add(gr1.getTree());
            	    if (list_gr1==null) list_gr1=new ArrayList();
            	    list_gr1.add(gr1.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:88:43: gr2+= typedecl
            	    {
            	    pushFollow(FOLLOW_typedecl_in_syntax287);
            	    gr2=typedecl();

            	    state._fsp--;

            	    stream_typedecl.add(gr2.getTree());
            	    if (list_gr2==null) list_gr2=new ArrayList();
            	    list_gr2.add(gr2.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:88:59: gr3+= atomdecl
            	    {
            	    pushFollow(FOLLOW_atomdecl_in_syntax293);
            	    gr3=atomdecl();

            	    state._fsp--;

            	    stream_atomdecl.add(gr3.getTree());
            	    if (list_gr3==null) list_gr3=new ArrayList();
            	    list_gr3.add(gr3.getTree());


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);



            // AST REWRITE
            // elements: gr1, gr2, gr3
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: gr2, gr1, gr3
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_gr2=new RewriteRuleSubtreeStream(adaptor,"token gr2",list_gr2);
            RewriteRuleSubtreeStream stream_gr1=new RewriteRuleSubtreeStream(adaptor,"token gr1",list_gr1);
            RewriteRuleSubtreeStream stream_gr3=new RewriteRuleSubtreeStream(adaptor,"token gr3",list_gr3);
            root_0 = (Tree)adaptor.nil();
            // 89:5: -> ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:89:8: ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_1);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:89:25: ( $gr1)*
                while ( stream_gr1.hasNext() ) {
                    adaptor.addChild(root_1, stream_gr1.nextTree());

                }
                stream_gr1.reset();
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:89:33: ( $gr2)*
                while ( stream_gr2.hasNext() ) {
                    adaptor.addChild(root_1, stream_gr2.nextTree());

                }
                stream_gr2.reset();
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:89:41: ( $gr3)*
                while ( stream_gr3.hasNext() ) {
                    adaptor.addChild(root_1, stream_gr3.nextTree());

                }
                stream_gr3.reset();

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
    // $ANTLR end "syntax"

    public static class atomdecl_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atomdecl"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:92:1: atomdecl : ATOM atom= ID -> ^( AtomDecl ID[atom] ) ;
    public final GomLanguageParser.atomdecl_return atomdecl() throws RecognitionException {
        GomLanguageParser.atomdecl_return retval = new GomLanguageParser.atomdecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token atom=null;
        Token ATOM13=null;

        Tree atom_tree=null;
        Tree ATOM13_tree=null;
        RewriteRuleTokenStream stream_ATOM=new RewriteRuleTokenStream(adaptor,"token ATOM");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:92:10: ( ATOM atom= ID -> ^( AtomDecl ID[atom] ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:93:3: ATOM atom= ID
            {
            ATOM13=(Token)match(input,ATOM,FOLLOW_ATOM_in_atomdecl336);  
            stream_ATOM.add(ATOM13);

            atom=(Token)match(input,ID,FOLLOW_ID_in_atomdecl340);  
            stream_ID.add(atom);



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
            // 93:16: -> ^( AtomDecl ID[atom] )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:93:19: ^( AtomDecl ID[atom] )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(AtomDecl, "AtomDecl"), root_1);

                adaptor.addChild(root_1, (Tree)adaptor.create(ID, atom));

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
    // $ANTLR end "atomdecl"

    public static class typedecl_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typedecl"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:96:1: typedecl : (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts) | ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename] -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts) );
    public final GomLanguageParser.typedecl_return typedecl() throws RecognitionException {
        GomLanguageParser.typedecl_return retval = new GomLanguageParser.typedecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token typename=null;
        Token ptypename=null;
        Token EQUALS14=null;
        Token BINDS15=null;
        Token EQUALS16=null;
        GomLanguageParser.alternatives_return alts = null;

        GomLanguageParser.atoms_return b = null;

        GomLanguageParser.pattern_alternatives_return palts = null;


        Tree typename_tree=null;
        Tree ptypename_tree=null;
        Tree EQUALS14_tree=null;
        Tree BINDS15_tree=null;
        Tree EQUALS16_tree=null;
        RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_BINDS=new RewriteRuleTokenStream(adaptor,"token BINDS");
        RewriteRuleSubtreeStream stream_atoms=new RewriteRuleSubtreeStream(adaptor,"rule atoms");
        RewriteRuleSubtreeStream stream_alternatives=new RewriteRuleSubtreeStream(adaptor,"rule alternatives");
        RewriteRuleSubtreeStream stream_pattern_alternatives=new RewriteRuleSubtreeStream(adaptor,"rule pattern_alternatives");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:96:10: (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts) | ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename] -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==ID) ) {
                int LA7_1 = input.LA(2);

                if ( (LA7_1==EQUALS) ) {
                    alt7=1;
                }
                else if ( (LA7_1==BINDS) ) {
                    alt7=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:97:5: typename= ID EQUALS alts= alternatives[typename]
                    {
                    typename=(Token)match(input,ID,FOLLOW_ID_in_typedecl366);  
                    stream_ID.add(typename);

                    EQUALS14=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl368);  
                    stream_EQUALS.add(EQUALS14);

                    pushFollow(FOLLOW_alternatives_in_typedecl372);
                    alts=alternatives(typename);

                    state._fsp--;

                    stream_alternatives.add(alts.getTree());


                    // AST REWRITE
                    // elements: typename, alts
                    // token labels: typename
                    // rule labels: retval, alts
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_typename=new RewriteRuleTokenStream(adaptor,"token typename",typename);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_alts=new RewriteRuleSubtreeStream(adaptor,"rule alts",alts!=null?alts.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 98:7: -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts)
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:98:10: ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SortType, "SortType"), root_1);

                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:98:21: ^( GomType ^( ExpressionType ) $typename)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:98:31: ^( ExpressionType )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_typename.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:98:60: ^( ConcAtom )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcAtom, "ConcAtom"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_alts.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:99:6: ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename]
                    {
                    ptypename=(Token)match(input,ID,FOLLOW_ID_in_typedecl412);  
                    stream_ID.add(ptypename);

                    BINDS15=(Token)match(input,BINDS,FOLLOW_BINDS_in_typedecl414);  
                    stream_BINDS.add(BINDS15);

                    pushFollow(FOLLOW_atoms_in_typedecl418);
                    b=atoms();

                    state._fsp--;

                    stream_atoms.add(b.getTree());
                    EQUALS16=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl420);  
                    stream_EQUALS.add(EQUALS16);

                    pushFollow(FOLLOW_pattern_alternatives_in_typedecl424);
                    palts=pattern_alternatives(ptypename);

                    state._fsp--;

                    stream_pattern_alternatives.add(palts.getTree());


                    // AST REWRITE
                    // elements: b, palts, ptypename
                    // token labels: ptypename
                    // rule labels: retval, b, palts
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_ptypename=new RewriteRuleTokenStream(adaptor,"token ptypename",ptypename);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_b=new RewriteRuleSubtreeStream(adaptor,"rule b",b!=null?b.tree:null);
                    RewriteRuleSubtreeStream stream_palts=new RewriteRuleSubtreeStream(adaptor,"rule palts",palts!=null?palts.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 100:7: -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts)
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:100:10: ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SortType, "SortType"), root_1);

                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:100:21: ^( GomType ^( PatternType ) $ptypename)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:100:31: ^( PatternType )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(PatternType, "PatternType"), root_3);

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_ptypename.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_b.nextTree());
                        adaptor.addChild(root_1, stream_palts.nextTree());

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
    // $ANTLR end "typedecl"

    public static class atoms_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atoms"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:103:1: atoms : (atom+= ID )+ -> ^( ConcAtom ( $atom)+ ) ;
    public final GomLanguageParser.atoms_return atoms() throws RecognitionException {
        GomLanguageParser.atoms_return retval = new GomLanguageParser.atoms_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token atom=null;
        List list_atom=null;

        Tree atom_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:103:7: ( (atom+= ID )+ -> ^( ConcAtom ( $atom)+ ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:104:3: (atom+= ID )+
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:104:3: (atom+= ID )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==ID) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:104:4: atom+= ID
            	    {
            	    atom=(Token)match(input,ID,FOLLOW_ID_in_atoms470);  
            	    stream_ID.add(atom);

            	    if (list_atom==null) list_atom=new ArrayList();
            	    list_atom.add(atom);


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);



            // AST REWRITE
            // elements: atom
            // token labels: 
            // rule labels: retval
            // token list labels: atom
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_atom=new RewriteRuleTokenStream(adaptor,"token atom", list_atom);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 104:15: -> ^( ConcAtom ( $atom)+ )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:104:18: ^( ConcAtom ( $atom)+ )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcAtom, "ConcAtom"), root_1);

                if ( !(stream_atom.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_atom.hasNext() ) {
                    adaptor.addChild(root_1, stream_atom.nextNode());

                }
                stream_atom.reset();

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
    // $ANTLR end "atoms"

    public static class alternatives_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "alternatives"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:107:1: alternatives[Token typename] : ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? ) opdecl[typename,jd1] ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )* ( SEMI )? -> ^( ConcAlternative ( opdecl )+ ) ;
    public final GomLanguageParser.alternatives_return alternatives(Token typename) throws RecognitionException {
        GomLanguageParser.alternatives_return retval = new GomLanguageParser.alternatives_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token jd1=null;
        Token jd2=null;
        Token ALT17=null;
        Token ALT18=null;
        Token ALT19=null;
        Token ALT21=null;
        Token ALT22=null;
        Token ALT23=null;
        Token SEMI25=null;
        GomLanguageParser.opdecl_return opdecl20 = null;

        GomLanguageParser.opdecl_return opdecl24 = null;


        Tree jd1_tree=null;
        Tree jd2_tree=null;
        Tree ALT17_tree=null;
        Tree ALT18_tree=null;
        Tree ALT19_tree=null;
        Tree ALT21_tree=null;
        Tree ALT22_tree=null;
        Tree ALT23_tree=null;
        Tree SEMI25_tree=null;
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleTokenStream stream_JAVADOC=new RewriteRuleTokenStream(adaptor,"token JAVADOC");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleSubtreeStream stream_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule opdecl");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:107:30: ( ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? ) opdecl[typename,jd1] ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )* ( SEMI )? -> ^( ConcAlternative ( opdecl )+ ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:3: ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? ) opdecl[typename,jd1] ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )* ( SEMI )?
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:3: ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? )
            int alt10=4;
            switch ( input.LA(1) ) {
            case JAVADOC:
                {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==ALT) ) {
                    alt10=1;
                }
                else if ( (LA10_1==ID) ) {
                    alt10=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;
                }
                }
                break;
            case ALT:
                {
                int LA10_2 = input.LA(2);

                if ( (LA10_2==JAVADOC) ) {
                    alt10=2;
                }
                else if ( (LA10_2==ID) ) {
                    alt10=4;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 2, input);

                    throw nvae;
                }
                }
                break;
            case ID:
                {
                alt10=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:4: (jd1= JAVADOC ALT )
                    {
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:4: (jd1= JAVADOC ALT )
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:5: jd1= JAVADOC ALT
                    {
                    jd1=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives502);  
                    stream_JAVADOC.add(jd1);

                    ALT17=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives504);  
                    stream_ALT.add(ALT17);


                    }


                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:24: ( ALT jd1= JAVADOC )
                    {
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:24: ( ALT jd1= JAVADOC )
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:25: ALT jd1= JAVADOC
                    {
                    ALT18=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives510);  
                    stream_ALT.add(ALT18);

                    jd1=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives514);  
                    stream_JAVADOC.add(jd1);


                    }


                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:44: jd1= JAVADOC
                    {
                    jd1=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives521);  
                    stream_JAVADOC.add(jd1);


                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:58: ( ALT )?
                    {
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:58: ( ALT )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==ALT) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:108:59: ALT
                            {
                            ALT19=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives526);  
                            stream_ALT.add(ALT19);


                            }
                            break;

                    }


                    }
                    break;

            }

            pushFollow(FOLLOW_opdecl_in_alternatives533);
            opdecl20=opdecl(typename, jd1);

            state._fsp--;

            stream_opdecl.add(opdecl20.getTree());
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:110:3: ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=JAVADOC && LA12_0<=ALT)) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:111:4: ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2]
            	    {
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:111:4: ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT )
            	    int alt11=3;
            	    int LA11_0 = input.LA(1);

            	    if ( (LA11_0==JAVADOC) ) {
            	        alt11=1;
            	    }
            	    else if ( (LA11_0==ALT) ) {
            	        int LA11_2 = input.LA(2);

            	        if ( (LA11_2==JAVADOC) ) {
            	            alt11=2;
            	        }
            	        else if ( (LA11_2==ID) ) {
            	            alt11=3;
            	        }
            	        else {
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 11, 2, input);

            	            throw nvae;
            	        }
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 11, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt11) {
            	        case 1 :
            	            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:111:5: (jd2= JAVADOC ALT )
            	            {
            	            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:111:5: (jd2= JAVADOC ALT )
            	            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:111:6: jd2= JAVADOC ALT
            	            {
            	            jd2=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives547);  
            	            stream_JAVADOC.add(jd2);

            	            ALT21=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives549);  
            	            stream_ALT.add(ALT21);


            	            }


            	            }
            	            break;
            	        case 2 :
            	            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:111:25: ( ALT jd2= JAVADOC )
            	            {
            	            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:111:25: ( ALT jd2= JAVADOC )
            	            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:111:26: ALT jd2= JAVADOC
            	            {
            	            ALT22=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives555);  
            	            stream_ALT.add(ALT22);

            	            jd2=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives559);  
            	            stream_JAVADOC.add(jd2);


            	            }


            	            }
            	            break;
            	        case 3 :
            	            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:111:45: ALT
            	            {
            	            ALT23=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives564);  
            	            stream_ALT.add(ALT23);

            	            jd2=null;

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_opdecl_in_alternatives572);
            	    opdecl24=opdecl(typename, jd2);

            	    state._fsp--;

            	    stream_opdecl.add(opdecl24.getTree());

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:113:6: ( SEMI )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==SEMI) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:113:7: SEMI
                    {
                    SEMI25=(Token)match(input,SEMI,FOLLOW_SEMI_in_alternatives581);  
                    stream_SEMI.add(SEMI25);


                    }
                    break;

            }



            // AST REWRITE
            // elements: opdecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 114:3: -> ^( ConcAlternative ( opdecl )+ )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:114:6: ^( ConcAlternative ( opdecl )+ )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcAlternative, "ConcAlternative"), root_1);

                if ( !(stream_opdecl.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_opdecl.hasNext() ) {
                    adaptor.addChild(root_1, stream_opdecl.nextTree());

                }
                stream_opdecl.reset();

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
    // $ANTLR end "alternatives"

    public static class pattern_alternatives_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pattern_alternatives"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:118:1: pattern_alternatives[Token typename] : ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )? -> ^( ConcAlternative ( pattern_opdecl )+ ) ;
    public final GomLanguageParser.pattern_alternatives_return pattern_alternatives(Token typename) throws RecognitionException {
        GomLanguageParser.pattern_alternatives_return retval = new GomLanguageParser.pattern_alternatives_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ALT26=null;
        Token ALT28=null;
        Token SEMI30=null;
        GomLanguageParser.pattern_opdecl_return pattern_opdecl27 = null;

        GomLanguageParser.pattern_opdecl_return pattern_opdecl29 = null;


        Tree ALT26_tree=null;
        Tree ALT28_tree=null;
        Tree SEMI30_tree=null;
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleSubtreeStream stream_pattern_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule pattern_opdecl");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:118:38: ( ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )? -> ^( ConcAlternative ( pattern_opdecl )+ ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:119:3: ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )?
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:119:3: ( ALT )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==ALT) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:119:4: ALT
                    {
                    ALT26=(Token)match(input,ALT,FOLLOW_ALT_in_pattern_alternatives613);  
                    stream_ALT.add(ALT26);


                    }
                    break;

            }

            pushFollow(FOLLOW_pattern_opdecl_in_pattern_alternatives617);
            pattern_opdecl27=pattern_opdecl(typename);

            state._fsp--;

            stream_pattern_opdecl.add(pattern_opdecl27.getTree());
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:119:35: ( ALT pattern_opdecl[typename] )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==ALT) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:119:36: ALT pattern_opdecl[typename]
            	    {
            	    ALT28=(Token)match(input,ALT,FOLLOW_ALT_in_pattern_alternatives621);  
            	    stream_ALT.add(ALT28);

            	    pushFollow(FOLLOW_pattern_opdecl_in_pattern_alternatives623);
            	    pattern_opdecl29=pattern_opdecl(typename);

            	    state._fsp--;

            	    stream_pattern_opdecl.add(pattern_opdecl29.getTree());

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:119:67: ( SEMI )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==SEMI) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:119:68: SEMI
                    {
                    SEMI30=(Token)match(input,SEMI,FOLLOW_SEMI_in_pattern_alternatives629);  
                    stream_SEMI.add(SEMI30);


                    }
                    break;

            }



            // AST REWRITE
            // elements: pattern_opdecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 120:3: -> ^( ConcAlternative ( pattern_opdecl )+ )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:120:6: ^( ConcAlternative ( pattern_opdecl )+ )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcAlternative, "ConcAlternative"), root_1);

                if ( !(stream_pattern_opdecl.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_pattern_opdecl.hasNext() ) {
                    adaptor.addChild(root_1, stream_pattern_opdecl.nextTree());

                }
                stream_pattern_opdecl.reset();

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
    // $ANTLR end "pattern_alternatives"

    public static class opdecl_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "opdecl"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:123:1: opdecl[Token type, Token JAVADOC] : ID fieldlist -> {JAVADOC!=null}? ^( Alternative ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) ) -> ^( Alternative ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final GomLanguageParser.opdecl_return opdecl(Token type, Token JAVADOC) throws RecognitionException {
        GomLanguageParser.opdecl_return retval = new GomLanguageParser.opdecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID31=null;
        GomLanguageParser.fieldlist_return fieldlist32 = null;


        Tree ID31_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule fieldlist");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:123:35: ( ID fieldlist -> {JAVADOC!=null}? ^( Alternative ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) ) -> ^( Alternative ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:124:2: ID fieldlist
            {
            ID31=(Token)match(input,ID,FOLLOW_ID_in_opdecl657);  
            stream_ID.add(ID31);

            pushFollow(FOLLOW_fieldlist_in_opdecl659);
            fieldlist32=fieldlist();

            state._fsp--;

            stream_fieldlist.add(fieldlist32.getTree());


            // AST REWRITE
            // elements: ID, ID, ID, ID, ID, fieldlist, ID, fieldlist, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 125:3: -> {JAVADOC!=null}? ^( Alternative ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) )
            if (JAVADOC!=null) {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:125:23: ^( Alternative ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Alternative, "Alternative"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:125:50: ^( GomType ^( ExpressionType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:125:60: ^( ExpressionType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:126:7: ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(OptionList, "OptionList"), root_2);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:126:20: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_3);

                adaptor.addChild(root_3, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_2, root_3);
                }
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:126:59: ^( Details ID[JAVADOC] )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Details, "Details"), root_3);

                adaptor.addChild(root_3, (Tree)adaptor.create(ID, JAVADOC));

                adaptor.addChild(root_2, root_3);
                }

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 127:3: -> ^( Alternative ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:127:6: ^( Alternative ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Alternative, "Alternative"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:127:33: ^( GomType ^( ExpressionType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:127:43: ^( ExpressionType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:128:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

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
    // $ANTLR end "opdecl"

    public static class pattern_opdecl_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pattern_opdecl"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:132:1: pattern_opdecl[Token type] : ID pattern_fieldlist -> ^( Alternative ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final GomLanguageParser.pattern_opdecl_return pattern_opdecl(Token type) throws RecognitionException {
        GomLanguageParser.pattern_opdecl_return retval = new GomLanguageParser.pattern_opdecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID33=null;
        GomLanguageParser.pattern_fieldlist_return pattern_fieldlist34 = null;


        Tree ID33_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_pattern_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule pattern_fieldlist");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:132:28: ( ID pattern_fieldlist -> ^( Alternative ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:133:2: ID pattern_fieldlist
            {
            ID33=(Token)match(input,ID,FOLLOW_ID_in_pattern_opdecl759);  
            stream_ID.add(ID33);

            pushFollow(FOLLOW_pattern_fieldlist_in_pattern_opdecl761);
            pattern_fieldlist34=pattern_fieldlist();

            state._fsp--;

            stream_pattern_fieldlist.add(pattern_fieldlist34.getTree());


            // AST REWRITE
            // elements: ID, pattern_fieldlist, ID, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 134:3: -> ^( Alternative ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:134:6: ^( Alternative ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Alternative, "Alternative"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_pattern_fieldlist.nextTree());
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:134:41: ^( GomType ^( PatternType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:134:51: ^( PatternType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(PatternType, "PatternType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:135:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

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
    // $ANTLR end "pattern_opdecl"

    public static class fieldlist_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fieldlist"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:138:1: fieldlist : LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) ;
    public final GomLanguageParser.fieldlist_return fieldlist() throws RecognitionException {
        GomLanguageParser.fieldlist_return retval = new GomLanguageParser.fieldlist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAREN35=null;
        Token COMMA37=null;
        Token RPAREN39=null;
        GomLanguageParser.field_return field36 = null;

        GomLanguageParser.field_return field38 = null;


        Tree LPAREN35_tree=null;
        Tree COMMA37_tree=null;
        Tree RPAREN39_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:138:11: ( LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:139:3: LPAREN ( field ( COMMA field )* )? RPAREN
            {
            LPAREN35=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_fieldlist810);  
            stream_LPAREN.add(LPAREN35);

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:139:10: ( field ( COMMA field )* )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==ID||LA18_0==LDIPLE) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:139:11: field ( COMMA field )*
                    {
                    pushFollow(FOLLOW_field_in_fieldlist813);
                    field36=field();

                    state._fsp--;

                    stream_field.add(field36.getTree());
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:139:17: ( COMMA field )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0==COMMA) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:139:18: COMMA field
                    	    {
                    	    COMMA37=(Token)match(input,COMMA,FOLLOW_COMMA_in_fieldlist816);  
                    	    stream_COMMA.add(COMMA37);

                    	    pushFollow(FOLLOW_field_in_fieldlist818);
                    	    field38=field();

                    	    state._fsp--;

                    	    stream_field.add(field38.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop17;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAREN39=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_fieldlist825);  
            stream_RPAREN.add(RPAREN39);



            // AST REWRITE
            // elements: field
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 139:42: -> ^( ConcField ( field )* )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:139:45: ^( ConcField ( field )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:139:57: ( field )*
                while ( stream_field.hasNext() ) {
                    adaptor.addChild(root_1, stream_field.nextTree());

                }
                stream_field.reset();

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
    // $ANTLR end "fieldlist"

    public static class pattern_fieldlist_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pattern_fieldlist"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:142:1: pattern_fieldlist : LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN -> ^( ConcField ( pattern_field )* ) ;
    public final GomLanguageParser.pattern_fieldlist_return pattern_fieldlist() throws RecognitionException {
        GomLanguageParser.pattern_fieldlist_return retval = new GomLanguageParser.pattern_fieldlist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAREN40=null;
        Token COMMA42=null;
        Token RPAREN44=null;
        GomLanguageParser.pattern_field_return pattern_field41 = null;

        GomLanguageParser.pattern_field_return pattern_field43 = null;


        Tree LPAREN40_tree=null;
        Tree COMMA42_tree=null;
        Tree RPAREN44_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_pattern_field=new RewriteRuleSubtreeStream(adaptor,"rule pattern_field");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:142:19: ( LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN -> ^( ConcField ( pattern_field )* ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:143:3: LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN
            {
            LPAREN40=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_pattern_fieldlist849);  
            stream_LPAREN.add(LPAREN40);

            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:143:10: ( pattern_field ( COMMA pattern_field )* )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==ID||(LA20_0>=INNER && LA20_0<=NEUTRAL)) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:143:11: pattern_field ( COMMA pattern_field )*
                    {
                    pushFollow(FOLLOW_pattern_field_in_pattern_fieldlist852);
                    pattern_field41=pattern_field();

                    state._fsp--;

                    stream_pattern_field.add(pattern_field41.getTree());
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:143:25: ( COMMA pattern_field )*
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( (LA19_0==COMMA) ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:143:26: COMMA pattern_field
                    	    {
                    	    COMMA42=(Token)match(input,COMMA,FOLLOW_COMMA_in_pattern_fieldlist855);  
                    	    stream_COMMA.add(COMMA42);

                    	    pushFollow(FOLLOW_pattern_field_in_pattern_fieldlist857);
                    	    pattern_field43=pattern_field();

                    	    state._fsp--;

                    	    stream_pattern_field.add(pattern_field43.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop19;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAREN44=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_pattern_fieldlist864);  
            stream_RPAREN.add(RPAREN44);



            // AST REWRITE
            // elements: pattern_field
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 143:58: -> ^( ConcField ( pattern_field )* )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:143:61: ^( ConcField ( pattern_field )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:143:73: ( pattern_field )*
                while ( stream_pattern_field.hasNext() ) {
                    adaptor.addChild(root_1, stream_pattern_field.nextTree());

                }
                stream_pattern_field.reset();

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
    // $ANTLR end "pattern_fieldlist"

    public static class type_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "type"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:145:1: type : ID -> ^( GomType ^( ExpressionType ) ID ) ;
    public final GomLanguageParser.type_return type() throws RecognitionException {
        GomLanguageParser.type_return retval = new GomLanguageParser.type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID45=null;

        Tree ID45_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:145:5: ( ID -> ^( GomType ^( ExpressionType ) ID ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:146:3: ID
            {
            ID45=(Token)match(input,ID,FOLLOW_ID_in_type885);  
            stream_ID.add(ID45);



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
            // 146:6: -> ^( GomType ^( ExpressionType ) ID )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:146:9: ^( GomType ^( ExpressionType ) ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_1);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:146:19: ^( ExpressionType )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_2);

                adaptor.addChild(root_1, root_2);
                }
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
    // $ANTLR end "type"

    public static class pattern_type_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pattern_type"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:150:1: pattern_type : ID -> ^( GomType ^( PatternType ) ID ) ;
    public final GomLanguageParser.pattern_type_return pattern_type() throws RecognitionException {
        GomLanguageParser.pattern_type_return retval = new GomLanguageParser.pattern_type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID46=null;

        Tree ID46_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:150:13: ( ID -> ^( GomType ^( PatternType ) ID ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:151:3: ID
            {
            ID46=(Token)match(input,ID,FOLLOW_ID_in_pattern_type911);  
            stream_ID.add(ID46);



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
            // 151:6: -> ^( GomType ^( PatternType ) ID )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:151:9: ^( GomType ^( PatternType ) ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_1);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:151:19: ^( PatternType )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(PatternType, "PatternType"), root_2);

                adaptor.addChild(root_1, root_2);
                }
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
    // $ANTLR end "pattern_type"

    public static class field_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "field"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:154:1: field : ( type STAR -> ^( StarredField type ^( None ) ) | LDIPLE pattern_type RDIPLE STAR -> ^( StarredField pattern_type ^( Refresh ) ) | ID COLON type -> ^( NamedField ID type ^( None ) ) | ID COLON LDIPLE pattern_type RDIPLE -> ^( NamedField ID pattern_type ^( Refresh ) ) );
    public final GomLanguageParser.field_return field() throws RecognitionException {
        GomLanguageParser.field_return retval = new GomLanguageParser.field_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token STAR48=null;
        Token LDIPLE49=null;
        Token RDIPLE51=null;
        Token STAR52=null;
        Token ID53=null;
        Token COLON54=null;
        Token ID56=null;
        Token COLON57=null;
        Token LDIPLE58=null;
        Token RDIPLE60=null;
        GomLanguageParser.type_return type47 = null;

        GomLanguageParser.pattern_type_return pattern_type50 = null;

        GomLanguageParser.type_return type55 = null;

        GomLanguageParser.pattern_type_return pattern_type59 = null;


        Tree STAR48_tree=null;
        Tree LDIPLE49_tree=null;
        Tree RDIPLE51_tree=null;
        Tree STAR52_tree=null;
        Tree ID53_tree=null;
        Tree COLON54_tree=null;
        Tree ID56_tree=null;
        Tree COLON57_tree=null;
        Tree LDIPLE58_tree=null;
        Tree RDIPLE60_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_LDIPLE=new RewriteRuleTokenStream(adaptor,"token LDIPLE");
        RewriteRuleTokenStream stream_RDIPLE=new RewriteRuleTokenStream(adaptor,"token RDIPLE");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_pattern_type=new RewriteRuleSubtreeStream(adaptor,"rule pattern_type");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:154:6: ( type STAR -> ^( StarredField type ^( None ) ) | LDIPLE pattern_type RDIPLE STAR -> ^( StarredField pattern_type ^( Refresh ) ) | ID COLON type -> ^( NamedField ID type ^( None ) ) | ID COLON LDIPLE pattern_type RDIPLE -> ^( NamedField ID pattern_type ^( Refresh ) ) )
            int alt21=4;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==ID) ) {
                int LA21_1 = input.LA(2);

                if ( (LA21_1==COLON) ) {
                    int LA21_3 = input.LA(3);

                    if ( (LA21_3==LDIPLE) ) {
                        alt21=4;
                    }
                    else if ( (LA21_3==ID) ) {
                        alt21=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 3, input);

                        throw nvae;
                    }
                }
                else if ( (LA21_1==STAR) ) {
                    alt21=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA21_0==LDIPLE) ) {
                alt21=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }
            switch (alt21) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:155:5: type STAR
                    {
                    pushFollow(FOLLOW_type_in_field937);
                    type47=type();

                    state._fsp--;

                    stream_type.add(type47.getTree());
                    STAR48=(Token)match(input,STAR,FOLLOW_STAR_in_field939);  
                    stream_STAR.add(STAR48);



                    // AST REWRITE
                    // elements: type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 155:15: -> ^( StarredField type ^( None ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:155:18: ^( StarredField type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_type.nextTree());
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:155:38: ^( None )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(None, "None"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:156:5: LDIPLE pattern_type RDIPLE STAR
                    {
                    LDIPLE49=(Token)match(input,LDIPLE,FOLLOW_LDIPLE_in_field957);  
                    stream_LDIPLE.add(LDIPLE49);

                    pushFollow(FOLLOW_pattern_type_in_field959);
                    pattern_type50=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type50.getTree());
                    RDIPLE51=(Token)match(input,RDIPLE,FOLLOW_RDIPLE_in_field961);  
                    stream_RDIPLE.add(RDIPLE51);

                    STAR52=(Token)match(input,STAR,FOLLOW_STAR_in_field963);  
                    stream_STAR.add(STAR52);



                    // AST REWRITE
                    // elements: pattern_type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 156:37: -> ^( StarredField pattern_type ^( Refresh ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:156:40: ^( StarredField pattern_type ^( Refresh ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:156:68: ^( Refresh )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Refresh, "Refresh"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:157:5: ID COLON type
                    {
                    ID53=(Token)match(input,ID,FOLLOW_ID_in_field981);  
                    stream_ID.add(ID53);

                    COLON54=(Token)match(input,COLON,FOLLOW_COLON_in_field983);  
                    stream_COLON.add(COLON54);

                    pushFollow(FOLLOW_type_in_field985);
                    type55=type();

                    state._fsp--;

                    stream_type.add(type55.getTree());


                    // AST REWRITE
                    // elements: ID, type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 157:19: -> ^( NamedField ID type ^( None ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:157:22: ^( NamedField ID type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_type.nextTree());
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:157:43: ^( None )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(None, "None"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:158:5: ID COLON LDIPLE pattern_type RDIPLE
                    {
                    ID56=(Token)match(input,ID,FOLLOW_ID_in_field1005);  
                    stream_ID.add(ID56);

                    COLON57=(Token)match(input,COLON,FOLLOW_COLON_in_field1007);  
                    stream_COLON.add(COLON57);

                    LDIPLE58=(Token)match(input,LDIPLE,FOLLOW_LDIPLE_in_field1009);  
                    stream_LDIPLE.add(LDIPLE58);

                    pushFollow(FOLLOW_pattern_type_in_field1011);
                    pattern_type59=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type59.getTree());
                    RDIPLE60=(Token)match(input,RDIPLE,FOLLOW_RDIPLE_in_field1013);  
                    stream_RDIPLE.add(RDIPLE60);



                    // AST REWRITE
                    // elements: ID, pattern_type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 158:41: -> ^( NamedField ID pattern_type ^( Refresh ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:158:44: ^( NamedField ID pattern_type ^( Refresh ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:158:73: ^( Refresh )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Refresh, "Refresh"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

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
    // $ANTLR end "field"

    public static class pattern_field_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pattern_field"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:162:1: pattern_field : ( pattern_type STAR -> ^( StarredField pattern_type ^( None ) ) | INNER ID COLON type -> ^( NamedField ID type ^( Inner ) ) | OUTER ID COLON type -> ^( NamedField ID type ^( Outer ) ) | NEUTRAL ID COLON type -> ^( NamedField ID type ^( Neutral ) ) | ID COLON pattern_type -> ^( NamedField ID pattern_type ^( None ) ) );
    public final GomLanguageParser.pattern_field_return pattern_field() throws RecognitionException {
        GomLanguageParser.pattern_field_return retval = new GomLanguageParser.pattern_field_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token STAR62=null;
        Token INNER63=null;
        Token ID64=null;
        Token COLON65=null;
        Token OUTER67=null;
        Token ID68=null;
        Token COLON69=null;
        Token NEUTRAL71=null;
        Token ID72=null;
        Token COLON73=null;
        Token ID75=null;
        Token COLON76=null;
        GomLanguageParser.pattern_type_return pattern_type61 = null;

        GomLanguageParser.type_return type66 = null;

        GomLanguageParser.type_return type70 = null;

        GomLanguageParser.type_return type74 = null;

        GomLanguageParser.pattern_type_return pattern_type77 = null;


        Tree STAR62_tree=null;
        Tree INNER63_tree=null;
        Tree ID64_tree=null;
        Tree COLON65_tree=null;
        Tree OUTER67_tree=null;
        Tree ID68_tree=null;
        Tree COLON69_tree=null;
        Tree NEUTRAL71_tree=null;
        Tree ID72_tree=null;
        Tree COLON73_tree=null;
        Tree ID75_tree=null;
        Tree COLON76_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_OUTER=new RewriteRuleTokenStream(adaptor,"token OUTER");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_INNER=new RewriteRuleTokenStream(adaptor,"token INNER");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_NEUTRAL=new RewriteRuleTokenStream(adaptor,"token NEUTRAL");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_pattern_type=new RewriteRuleSubtreeStream(adaptor,"rule pattern_type");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:162:14: ( pattern_type STAR -> ^( StarredField pattern_type ^( None ) ) | INNER ID COLON type -> ^( NamedField ID type ^( Inner ) ) | OUTER ID COLON type -> ^( NamedField ID type ^( Outer ) ) | NEUTRAL ID COLON type -> ^( NamedField ID type ^( Neutral ) ) | ID COLON pattern_type -> ^( NamedField ID pattern_type ^( None ) ) )
            int alt22=5;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA22_1 = input.LA(2);

                if ( (LA22_1==COLON) ) {
                    alt22=5;
                }
                else if ( (LA22_1==STAR) ) {
                    alt22=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 22, 1, input);

                    throw nvae;
                }
                }
                break;
            case INNER:
                {
                alt22=2;
                }
                break;
            case OUTER:
                {
                alt22=3;
                }
                break;
            case NEUTRAL:
                {
                alt22=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }

            switch (alt22) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:163:5: pattern_type STAR
                    {
                    pushFollow(FOLLOW_pattern_type_in_pattern_field1043);
                    pattern_type61=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type61.getTree());
                    STAR62=(Token)match(input,STAR,FOLLOW_STAR_in_pattern_field1045);  
                    stream_STAR.add(STAR62);



                    // AST REWRITE
                    // elements: pattern_type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 163:23: -> ^( StarredField pattern_type ^( None ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:163:26: ^( StarredField pattern_type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:163:54: ^( None )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(None, "None"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:164:5: INNER ID COLON type
                    {
                    INNER63=(Token)match(input,INNER,FOLLOW_INNER_in_pattern_field1063);  
                    stream_INNER.add(INNER63);

                    ID64=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1065);  
                    stream_ID.add(ID64);

                    COLON65=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1067);  
                    stream_COLON.add(COLON65);

                    pushFollow(FOLLOW_type_in_pattern_field1069);
                    type66=type();

                    state._fsp--;

                    stream_type.add(type66.getTree());


                    // AST REWRITE
                    // elements: type, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 164:25: -> ^( NamedField ID type ^( Inner ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:164:28: ^( NamedField ID type ^( Inner ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_type.nextTree());
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:164:49: ^( Inner )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Inner, "Inner"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:165:5: OUTER ID COLON type
                    {
                    OUTER67=(Token)match(input,OUTER,FOLLOW_OUTER_in_pattern_field1089);  
                    stream_OUTER.add(OUTER67);

                    ID68=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1091);  
                    stream_ID.add(ID68);

                    COLON69=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1093);  
                    stream_COLON.add(COLON69);

                    pushFollow(FOLLOW_type_in_pattern_field1095);
                    type70=type();

                    state._fsp--;

                    stream_type.add(type70.getTree());


                    // AST REWRITE
                    // elements: ID, type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 165:25: -> ^( NamedField ID type ^( Outer ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:165:28: ^( NamedField ID type ^( Outer ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_type.nextTree());
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:165:49: ^( Outer )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Outer, "Outer"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:166:5: NEUTRAL ID COLON type
                    {
                    NEUTRAL71=(Token)match(input,NEUTRAL,FOLLOW_NEUTRAL_in_pattern_field1115);  
                    stream_NEUTRAL.add(NEUTRAL71);

                    ID72=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1117);  
                    stream_ID.add(ID72);

                    COLON73=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1119);  
                    stream_COLON.add(COLON73);

                    pushFollow(FOLLOW_type_in_pattern_field1121);
                    type74=type();

                    state._fsp--;

                    stream_type.add(type74.getTree());


                    // AST REWRITE
                    // elements: type, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 166:27: -> ^( NamedField ID type ^( Neutral ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:166:30: ^( NamedField ID type ^( Neutral ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_type.nextTree());
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:166:51: ^( Neutral )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Neutral, "Neutral"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 5 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:167:5: ID COLON pattern_type
                    {
                    ID75=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1141);  
                    stream_ID.add(ID75);

                    COLON76=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1143);  
                    stream_COLON.add(COLON76);

                    pushFollow(FOLLOW_pattern_type_in_pattern_field1145);
                    pattern_type77=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type77.getTree());


                    // AST REWRITE
                    // elements: pattern_type, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 167:27: -> ^( NamedField ID pattern_type ^( None ) )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:167:30: ^( NamedField ID pattern_type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:167:59: ^( None )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(None, "None"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

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
    // $ANTLR end "pattern_field"

    public static class arglist_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arglist"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:170:1: arglist : ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) ;
    public final GomLanguageParser.arglist_return arglist() throws RecognitionException {
        GomLanguageParser.arglist_return retval = new GomLanguageParser.arglist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAREN78=null;
        Token COMMA80=null;
        Token RPAREN82=null;
        GomLanguageParser.arg_return arg79 = null;

        GomLanguageParser.arg_return arg81 = null;


        Tree LPAREN78_tree=null;
        Tree COMMA80_tree=null;
        Tree RPAREN82_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:170:8: ( ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:171:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:171:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==LPAREN) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:171:4: LPAREN ( arg ( COMMA arg )* )? RPAREN
                    {
                    LPAREN78=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arglist1172);  
                    stream_LPAREN.add(LPAREN78);

                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:171:11: ( arg ( COMMA arg )* )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==ID) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:171:12: arg ( COMMA arg )*
                            {
                            pushFollow(FOLLOW_arg_in_arglist1175);
                            arg79=arg();

                            state._fsp--;

                            stream_arg.add(arg79.getTree());
                            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:171:16: ( COMMA arg )*
                            loop23:
                            do {
                                int alt23=2;
                                int LA23_0 = input.LA(1);

                                if ( (LA23_0==COMMA) ) {
                                    alt23=1;
                                }


                                switch (alt23) {
                            	case 1 :
                            	    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:171:17: COMMA arg
                            	    {
                            	    COMMA80=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist1178);  
                            	    stream_COMMA.add(COMMA80);

                            	    pushFollow(FOLLOW_arg_in_arglist1180);
                            	    arg81=arg();

                            	    state._fsp--;

                            	    stream_arg.add(arg81.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop23;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAREN82=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arglist1187);  
                    stream_RPAREN.add(RPAREN82);


                    }
                    break;

            }



            // AST REWRITE
            // elements: arg
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 172:3: -> ^( ConcArg ( arg )* )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:172:6: ^( ConcArg ( arg )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcArg, "ConcArg"), root_1);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:172:16: ( arg )*
                while ( stream_arg.hasNext() ) {
                    adaptor.addChild(root_1, stream_arg.nextTree());

                }
                stream_arg.reset();

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
    // $ANTLR end "arglist"

    public static class arg_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arg"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:175:1: arg : ID -> ^( Arg ID ) ;
    public final GomLanguageParser.arg_return arg() throws RecognitionException {
        GomLanguageParser.arg_return retval = new GomLanguageParser.arg_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID83=null;

        Tree ID83_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:175:5: ( ID -> ^( Arg ID ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:175:7: ID
            {
            ID83=(Token)match(input,ID,FOLLOW_ID_in_arg1213);  
            stream_ID.add(ID83);



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
            // 175:10: -> ^( Arg ID )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:175:13: ^( Arg ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Arg, "Arg"), root_1);

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
    // $ANTLR end "arg"

    public static class hookConstruct_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "hookConstruct"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:177:1: hookConstruct : (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final GomLanguageParser.hookConstruct_return hookConstruct() throws RecognitionException {
        GomLanguageParser.hookConstruct_return retval = new GomLanguageParser.hookConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token pointCut=null;
        Token hookType=null;
        Token COLON84=null;
        Token LBRACE86=null;
        GomLanguageParser.hookScope_return hscope = null;

        GomLanguageParser.arglist_return arglist85 = null;


        Tree pointCut_tree=null;
        Tree hookType_tree=null;
        Tree COLON84_tree=null;
        Tree LBRACE86_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_arglist=new RewriteRuleSubtreeStream(adaptor,"rule arglist");
        RewriteRuleSubtreeStream stream_hookScope=new RewriteRuleSubtreeStream(adaptor,"rule hookScope");
        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:177:15: ( (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:178:3: (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE
            {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:178:3: (hscope= hookScope )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==MODULE||(LA26_0>=SORT && LA26_0<=OPERATOR)) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:178:4: hscope= hookScope
                    {
                    pushFollow(FOLLOW_hookScope_in_hookConstruct1234);
                    hscope=hookScope();

                    state._fsp--;

                    stream_hookScope.add(hscope.getTree());

                    }
                    break;

            }

            pointCut=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct1240);  
            stream_ID.add(pointCut);

            COLON84=(Token)match(input,COLON,FOLLOW_COLON_in_hookConstruct1242);  
            stream_COLON.add(COLON84);

            hookType=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct1246);  
            stream_ID.add(hookType);

            pushFollow(FOLLOW_arglist_in_hookConstruct1248);
            arglist85=arglist();

            state._fsp--;

            stream_arglist.add(arglist85.getTree());
            LBRACE86=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_hookConstruct1250);  
            stream_LBRACE.add(LBRACE86);



            // AST REWRITE
            // elements: pointCut, LBRACE, arglist, pointCut, hookType, arglist, LBRACE, hscope, ID, ID, hookType
            // token labels: hookType, pointCut
            // rule labels: retval, hscope
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_hookType=new RewriteRuleTokenStream(adaptor,"token hookType",hookType);
            RewriteRuleTokenStream stream_pointCut=new RewriteRuleTokenStream(adaptor,"token pointCut",pointCut);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_hscope=new RewriteRuleSubtreeStream(adaptor,"rule hscope",hscope!=null?hscope.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 179:3: -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            if (hscope!=null) {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:179:22: ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Hook, "Hook"), root_1);

                adaptor.addChild(root_1, stream_hscope.nextTree());
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:179:47: ^( HookKind $hookType)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:180:24: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 181:3: -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:181:6: ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Hook, "Hook"), root_1);

                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:181:13: ^( KindOperator )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(KindOperator, "KindOperator"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:181:39: ^( HookKind $hookType)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:182:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

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
    // $ANTLR end "hookConstruct"

    public static class hookScope_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "hookScope"
    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:186:1: hookScope : ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) );
    public final GomLanguageParser.hookScope_return hookScope() throws RecognitionException {
        GomLanguageParser.hookScope_return retval = new GomLanguageParser.hookScope_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token SORT87=null;
        Token MODULE88=null;
        Token OPERATOR89=null;

        Tree SORT87_tree=null;
        Tree MODULE88_tree=null;
        Tree OPERATOR89_tree=null;
        RewriteRuleTokenStream stream_SORT=new RewriteRuleTokenStream(adaptor,"token SORT");
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");

        try {
            // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:186:11: ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) )
            int alt27=3;
            switch ( input.LA(1) ) {
            case SORT:
                {
                alt27=1;
                }
                break;
            case MODULE:
                {
                alt27=2;
                }
                break;
            case OPERATOR:
                {
                alt27=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:187:3: SORT
                    {
                    SORT87=(Token)match(input,SORT,FOLLOW_SORT_in_hookScope1363);  
                    stream_SORT.add(SORT87);



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
                    // 187:8: -> ^( KindSort )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:187:11: ^( KindSort )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(KindSort, "KindSort"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:188:5: MODULE
                    {
                    MODULE88=(Token)match(input,MODULE,FOLLOW_MODULE_in_hookScope1375);  
                    stream_MODULE.add(MODULE88);



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
                    // 188:12: -> ^( KindModule )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:188:15: ^( KindModule )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(KindModule, "KindModule"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:189:5: OPERATOR
                    {
                    OPERATOR89=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_hookScope1387);  
                    stream_OPERATOR.add(OPERATOR89);



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
                    // 189:14: -> ^( KindOperator )
                    {
                        // /Users/pem/github/tom/src/tom/gom/parser/GomLanguage.g:189:17: ^( KindOperator )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(KindOperator, "KindOperator"), root_1);

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
    // $ANTLR end "hookScope"

    // Delegated rules


 

    public static final BitSet FOLLOW_MODULE_in_module60 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_modulename_in_module62 = new BitSet(new long[]{0x0000000000000000L,0x00001C0000000000L});
    public static final BitSet FOLLOW_imports_in_module67 = new BitSet(new long[]{0x0000000000000000L,0x00001C0000000000L});
    public static final BitSet FOLLOW_section_in_module71 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module73 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_modulename130 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
    public static final BitSet FOLLOW_DOT_in_modulename132 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_ID_in_modulename142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORTS_in_imports170 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_importedModuleName_in_imports173 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_ID_in_importedModuleName202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_section224 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_adtgrammar_in_section228 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_syntax_in_adtgrammar252 = new BitSet(new long[]{0x0000000000000002L,0x0000100000000000L});
    public static final BitSet FOLLOW_ABSTRACT_in_syntax273 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_SYNTAX_in_syntax275 = new BitSet(new long[]{0x0000000000000002L,0x8000418000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_hookConstruct_in_syntax281 = new BitSet(new long[]{0x0000000000000002L,0x8000418000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_typedecl_in_syntax287 = new BitSet(new long[]{0x0000000000000002L,0x8000418000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_atomdecl_in_syntax293 = new BitSet(new long[]{0x0000000000000002L,0x8000418000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_ATOM_in_atomdecl336 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_ID_in_atomdecl340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl366 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl368 = new BitSet(new long[]{0x0000000000000000L,0x0006010000000000L});
    public static final BitSet FOLLOW_alternatives_in_typedecl372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl412 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_BINDS_in_typedecl414 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_atoms_in_typedecl418 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl420 = new BitSet(new long[]{0x0000000000000000L,0x0004010000000000L});
    public static final BitSet FOLLOW_pattern_alternatives_in_typedecl424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atoms470 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives502 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives504 = new BitSet(new long[]{0x0000000000000000L,0x0006010000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives510 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives514 = new BitSet(new long[]{0x0000000000000000L,0x0006010000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives521 = new BitSet(new long[]{0x0000000000000000L,0x0006010000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives526 = new BitSet(new long[]{0x0000000000000000L,0x0006010000000000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives533 = new BitSet(new long[]{0x0000000000000002L,0x000E000000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives547 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives549 = new BitSet(new long[]{0x0000000000000000L,0x0006010000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives555 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives559 = new BitSet(new long[]{0x0000000000000000L,0x0006010000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives564 = new BitSet(new long[]{0x0000000000000000L,0x0006010000000000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives572 = new BitSet(new long[]{0x0000000000000002L,0x000E000000000000L});
    public static final BitSet FOLLOW_SEMI_in_alternatives581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALT_in_pattern_alternatives613 = new BitSet(new long[]{0x0000000000000000L,0x0004010000000000L});
    public static final BitSet FOLLOW_pattern_opdecl_in_pattern_alternatives617 = new BitSet(new long[]{0x0000000000000002L,0x000C000000000000L});
    public static final BitSet FOLLOW_ALT_in_pattern_alternatives621 = new BitSet(new long[]{0x0000000000000000L,0x0004010000000000L});
    public static final BitSet FOLLOW_pattern_opdecl_in_pattern_alternatives623 = new BitSet(new long[]{0x0000000000000002L,0x000C000000000000L});
    public static final BitSet FOLLOW_SEMI_in_pattern_alternatives629 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_opdecl657 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_fieldlist_in_opdecl659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_opdecl759 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_pattern_fieldlist_in_pattern_opdecl761 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_fieldlist810 = new BitSet(new long[]{0x0000000000000000L,0x0140010000000000L});
    public static final BitSet FOLLOW_field_in_fieldlist813 = new BitSet(new long[]{0x0000000000000000L,0x0060000000000000L});
    public static final BitSet FOLLOW_COMMA_in_fieldlist816 = new BitSet(new long[]{0x0000000000000000L,0x0100010000000000L});
    public static final BitSet FOLLOW_field_in_fieldlist818 = new BitSet(new long[]{0x0000000000000000L,0x0060000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_fieldlist825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_pattern_fieldlist849 = new BitSet(new long[]{0x0000000000000000L,0x3840010000000000L});
    public static final BitSet FOLLOW_pattern_field_in_pattern_fieldlist852 = new BitSet(new long[]{0x0000000000000000L,0x0060000000000000L});
    public static final BitSet FOLLOW_COMMA_in_pattern_fieldlist855 = new BitSet(new long[]{0x0000000000000000L,0x3800010000000000L});
    public static final BitSet FOLLOW_pattern_field_in_pattern_fieldlist857 = new BitSet(new long[]{0x0000000000000000L,0x0060000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_pattern_fieldlist864 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_type885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_type911 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_field937 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_STAR_in_field939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LDIPLE_in_field957 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_pattern_type_in_field959 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RDIPLE_in_field961 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_STAR_in_field963 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field981 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_COLON_in_field983 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_type_in_field985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field1005 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_COLON_in_field1007 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_LDIPLE_in_field1009 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_pattern_type_in_field1011 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_RDIPLE_in_field1013 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_type_in_pattern_field1043 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_STAR_in_pattern_field1045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INNER_in_pattern_field1063 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1065 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1067 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1069 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OUTER_in_pattern_field1089 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1091 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1093 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEUTRAL_in_pattern_field1115 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1117 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1119 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_field1141 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1143 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_pattern_type_in_pattern_field1145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arglist1172 = new BitSet(new long[]{0x0000000000000000L,0x0040010000000000L});
    public static final BitSet FOLLOW_arg_in_arglist1175 = new BitSet(new long[]{0x0000000000000000L,0x0060000000000000L});
    public static final BitSet FOLLOW_COMMA_in_arglist1178 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_arg_in_arglist1180 = new BitSet(new long[]{0x0000000000000000L,0x0060000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_arglist1187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_arg1213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_hookScope_in_hookConstruct1234 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct1240 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_COLON_in_hookConstruct1242 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct1246 = new BitSet(new long[]{0x0000000000000000L,0x4010000000000000L});
    public static final BitSet FOLLOW_arglist_in_hookConstruct1248 = new BitSet(new long[]{0x0000000000000000L,0x4000000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_hookConstruct1250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SORT_in_hookScope1363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MODULE_in_hookScope1375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATOR_in_hookScope1387 = new BitSet(new long[]{0x0000000000000002L});

}