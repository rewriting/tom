// $ANTLR 3.1 /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g 2008-10-26 15:19:32

package tom.gom.parser;
import tom.gom.GomStreamManager;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class GomLanguageParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Grammar", "ExpressionType", "GomType", "Sort", "ConcSlotField", "CutSort", "ImportHookDecl", "ImportHook", "FullSortClass", "MakeBeforeHook", "Slots", "OperatorClass", "SortType", "KindSort", "CutFutureOperator", "ShortSortClass", "Origin", "ConcOperator", "InterfaceHookDecl", "KindModule", "ConcHook", "Arg", "ModHookPair", "ConcModuleDecl", "ConcModule", "ConcField", "ConcGomModule", "Module", "FullOperatorClass", "Compare", "HookKind", "Slot", "AtomType", "CutOperator", "SlotField", "ConcGrammar", "ModuleDecl", "Variadic", "ConcSlot", "ConcClassName", "PatternType", "Cons", "KindFutureOperator", "MappingHook", "ConcProduction", "BlockHookDecl", "SortClass", "MappingHookDecl", "ConcGomClass", "ConcSort", "ConcImportedModule", "ConcArg", "Production", "FutureCons", "IsCons", "None", "SortDecl", "IsEmpty", "Import", "ConcHookDecl", "InterfaceHook", "FutureNil", "Outer", "AtomDecl", "Hook", "Imports", "Public", "ConcAtom", "Code", "TomMapping", "ConcGomType", "CodeList", "MakeHookDecl", "AbstractTypeClass", "NamedField", "CutModule", "GomModule", "MakeHook", "Inner", "Empty", "GomModuleName", "BuiltinSortDecl", "KindOperator", "BlockHook", "StarredField", "ConcSection", "VariadicOperatorClass", "Neutral", "OptionList", "Sorts", "OperatorDecl", "ClassName", "ConcSortDecl", "Refresh", "MODULE", "ID", "DOT", "IMPORTS", "PUBLIC", "ABSTRACT", "SYNTAX", "ARROW", "ATOM", "EQUALS", "BINDS", "ALT", "SEMI", "LPAREN", "COMMA", "RPAREN", "STAR", "LDIPLE", "RDIPLE", "COLON", "INNER", "OUTER", "NEUTRAL", "LBRACE", "SORT", "OPERATOR", "PRIVATE", "RBRACE", "WS", "SLCOMMENT", "MLCOMMENT"
    };
    public static final int COMMA=112;
    public static final int GomType=6;
    public static final int Sort=7;
    public static final int CutSort=9;
    public static final int ImportHook=11;
    public static final int BINDS=108;
    public static final int Slots=14;
    public static final int ABSTRACT=103;
    public static final int KindSort=17;
    public static final int CutFutureOperator=18;
    public static final int ShortSortClass=19;
    public static final int InterfaceHookDecl=22;
    public static final int KindModule=23;
    public static final int ConcModuleDecl=27;
    public static final int ConcField=29;
    public static final int LDIPLE=115;
    public static final int DOT=100;
    public static final int PRIVATE=124;
    public static final int FullOperatorClass=32;
    public static final int SLCOMMENT=127;
    public static final int Compare=33;
    public static final int HookKind=34;
    public static final int AtomType=36;
    public static final int Slot=35;
    public static final int SlotField=38;
    public static final int CutOperator=37;
    public static final int ConcGrammar=39;
    public static final int ConcSlot=42;
    public static final int MODULE=98;
    public static final int ConcClassName=43;
    public static final int RPAREN=113;
    public static final int PatternType=44;
    public static final int MappingHook=47;
    public static final int ConcProduction=48;
    public static final int BlockHookDecl=49;
    public static final int ATOM=106;
    public static final int NEUTRAL=120;
    public static final int SortClass=50;
    public static final int MappingHookDecl=51;
    public static final int ConcGomClass=52;
    public static final int ConcImportedModule=54;
    public static final int ConcArg=55;
    public static final int Production=56;
    public static final int None=59;
    public static final int IsEmpty=61;
    public static final int Import=62;
    public static final int ConcHookDecl=63;
    public static final int InterfaceHook=64;
    public static final int FutureNil=65;
    public static final int WS=126;
    public static final int Outer=66;
    public static final int Imports=69;
    public static final int Code=72;
    public static final int TomMapping=73;
    public static final int SORT=122;
    public static final int ConcGomType=74;
    public static final int CodeList=75;
    public static final int SEMI=110;
    public static final int EQUALS=107;
    public static final int AbstractTypeClass=77;
    public static final int BuiltinSortDecl=85;
    public static final int COLON=117;
    public static final int SYNTAX=104;
    public static final int VariadicOperatorClass=90;
    public static final int OptionList=92;
    public static final int ClassName=95;
    public static final int ConcSortDecl=96;
    public static final int Grammar=4;
    public static final int ExpressionType=5;
    public static final int PUBLIC=102;
    public static final int ConcSlotField=8;
    public static final int ImportHookDecl=10;
    public static final int ARROW=105;
    public static final int FullSortClass=12;
    public static final int MakeBeforeHook=13;
    public static final int OperatorClass=15;
    public static final int SortType=16;
    public static final int INNER=118;
    public static final int Origin=20;
    public static final int ConcOperator=21;
    public static final int Arg=25;
    public static final int ConcHook=24;
    public static final int ModHookPair=26;
    public static final int ConcModule=28;
    public static final int ConcGomModule=30;
    public static final int LBRACE=121;
    public static final int RBRACE=125;
    public static final int Module=31;
    public static final int MLCOMMENT=128;
    public static final int ALT=109;
    public static final int ModuleDecl=40;
    public static final int Variadic=41;
    public static final int Cons=45;
    public static final int KindFutureOperator=46;
    public static final int LPAREN=111;
    public static final int RDIPLE=116;
    public static final int IMPORTS=101;
    public static final int OPERATOR=123;
    public static final int OUTER=119;
    public static final int ID=99;
    public static final int ConcSort=53;
    public static final int FutureCons=57;
    public static final int IsCons=58;
    public static final int SortDecl=60;
    public static final int AtomDecl=67;
    public static final int Hook=68;
    public static final int Public=70;
    public static final int ConcAtom=71;
    public static final int MakeHookDecl=76;
    public static final int NamedField=78;
    public static final int CutModule=79;
    public static final int GomModule=80;
    public static final int MakeHook=81;
    public static final int Inner=82;
    public static final int EOF=-1;
    public static final int Empty=83;
    public static final int GomModuleName=84;
    public static final int KindOperator=86;
    public static final int BlockHook=87;
    public static final int StarredField=88;
    public static final int STAR=114;
    public static final int ConcSection=89;
    public static final int Sorts=93;
    public static final int Neutral=91;
    public static final int OperatorDecl=94;
    public static final int Refresh=97;

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
    public String getGrammarFileName() { return "/Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g"; }


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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:52:1: module : MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) ;
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
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");
        RewriteRuleSubtreeStream stream_modulename=new RewriteRuleSubtreeStream(adaptor,"rule modulename");
        RewriteRuleSubtreeStream stream_imports=new RewriteRuleSubtreeStream(adaptor,"rule imports");
        RewriteRuleSubtreeStream stream_section=new RewriteRuleSubtreeStream(adaptor,"rule section");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:52:8: ( MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:53:3: MODULE modulename (imps= imports )? section EOF
            {
            MODULE1=(Token)match(input,MODULE,FOLLOW_MODULE_in_module346);  
            stream_MODULE.add(MODULE1);

            pushFollow(FOLLOW_modulename_in_module348);
            modulename2=modulename();

            state._fsp--;

            stream_modulename.add(modulename2.getTree());
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:53:21: (imps= imports )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==IMPORTS) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:53:22: imps= imports
                    {
                    pushFollow(FOLLOW_imports_in_module353);
                    imps=imports();

                    state._fsp--;

                    stream_imports.add(imps.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_section_in_module357);
            section3=section();

            state._fsp--;

            stream_section.add(section3.getTree());
            EOF4=(Token)match(input,EOF,FOLLOW_EOF_in_module359);  
            stream_EOF.add(EOF4);



            // AST REWRITE
            // elements: modulename, section, section, modulename, imports
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 54:3: -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) )
            if (imps!=null) {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:54:20: ^( GomModule modulename ^( ConcSection imports section ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:54:43: ^( ConcSection imports section )
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
            else // 55:3: -> ^( GomModule modulename ^( ConcSection section ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:55:6: ^( GomModule modulename ^( ConcSection section ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:55:29: ^( ConcSection section )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:58:1: modulename : (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) ;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:61:3: ( (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:62:3: (mod= ID DOT )* moduleName= ID
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:62:3: (mod= ID DOT )*
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
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:62:4: mod= ID DOT
            	    {
            	    mod=(Token)match(input,ID,FOLLOW_ID_in_modulename416);  
            	    stream_ID.add(mod);

            	    DOT5=(Token)match(input,DOT,FOLLOW_DOT_in_modulename418);  
            	    stream_DOT.add(DOT5);

            	     packagePrefix.append((mod!=null?mod.getText():null)+"."); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            moduleName=(Token)match(input,ID,FOLLOW_ID_in_modulename428);  
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
            retval.tree = root_0;
            RewriteRuleTokenStream stream_moduleName=new RewriteRuleTokenStream(adaptor,"token moduleName",moduleName);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 72:3: -> ^( GomModuleName $moduleName)
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:72:6: ^( GomModuleName $moduleName)
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:75:1: imports : IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) ;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:75:9: ( IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:76:3: IMPORTS ( importedModuleName )*
            {
            IMPORTS6=(Token)match(input,IMPORTS,FOLLOW_IMPORTS_in_imports456);  
            stream_IMPORTS.add(IMPORTS6);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:76:11: ( importedModuleName )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:76:12: importedModuleName
            	    {
            	    pushFollow(FOLLOW_importedModuleName_in_imports459);
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
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 76:33: -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:76:36: ^( Imports ^( ConcImportedModule ( importedModuleName )* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Imports, "Imports"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:76:46: ^( ConcImportedModule ( importedModuleName )* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcImportedModule, "ConcImportedModule"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:76:67: ( importedModuleName )*
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:1: importedModuleName : ID -> ^( Import ^( GomModuleName ID ) ) ;
    public final GomLanguageParser.importedModuleName_return importedModuleName() throws RecognitionException {
        GomLanguageParser.importedModuleName_return retval = new GomLanguageParser.importedModuleName_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID8=null;

        Tree ID8_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:20: ( ID -> ^( Import ^( GomModuleName ID ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:79:3: ID
            {
            ID8=(Token)match(input,ID,FOLLOW_ID_in_importedModuleName488);  
            stream_ID.add(ID8);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 79:6: -> ^( Import ^( GomModuleName ID ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:79:9: ^( Import ^( GomModuleName ID ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Import, "Import"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:79:18: ^( GomModuleName ID )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModuleName, "GomModuleName"), root_2);

                adaptor.addChild(root_2, stream_ID.nextNode());

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
    // $ANTLR end "importedModuleName"

    public static class section_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "section"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:82:1: section : ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) ;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:82:9: ( ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:83:3: ( PUBLIC )? adtgrammar
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:83:3: ( PUBLIC )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==PUBLIC) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:83:4: PUBLIC
                    {
                    PUBLIC9=(Token)match(input,PUBLIC,FOLLOW_PUBLIC_in_section514);  
                    stream_PUBLIC.add(PUBLIC9);


                    }
                    break;

            }

            pushFollow(FOLLOW_adtgrammar_in_section518);
            adtgrammar10=adtgrammar();

            state._fsp--;

            stream_adtgrammar.add(adtgrammar10.getTree());


            // AST REWRITE
            // elements: adtgrammar
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 83:24: -> ^( Public adtgrammar )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:83:27: ^( Public adtgrammar )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:86:1: adtgrammar : (gr+= syntax )+ -> $gr;
    public final GomLanguageParser.adtgrammar_return adtgrammar() throws RecognitionException {
        GomLanguageParser.adtgrammar_return retval = new GomLanguageParser.adtgrammar_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        List list_gr=null;
        GomLanguageParser.syntax_return gr = null;
        RewriteRuleSubtreeStream stream_syntax=new RewriteRuleSubtreeStream(adaptor,"rule syntax");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:86:12: ( (gr+= syntax )+ -> $gr)
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:87:3: (gr+= syntax )+
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:87:3: (gr+= syntax )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==ABSTRACT) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:87:4: gr+= syntax
            	    {
            	    pushFollow(FOLLOW_syntax_in_adtgrammar542);
            	    gr=syntax();

            	    state._fsp--;

            	    stream_syntax.add(gr.getTree());
            	    if (list_gr==null) list_gr=new ArrayList();
            	    list_gr.add(gr.getTree());


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);



            // AST REWRITE
            // elements: gr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: gr
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_gr=new RewriteRuleSubtreeStream(adaptor,"token gr",list_gr);
            root_0 = (Tree)adaptor.nil();
            // 87:17: -> $gr
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:90:1: syntax : ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) ) ;
    public final GomLanguageParser.syntax_return syntax() throws RecognitionException {
        GomLanguageParser.syntax_return retval = new GomLanguageParser.syntax_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ABSTRACT11=null;
        Token SYNTAX12=null;
        List list_gr1=null;
        List list_gr2=null;
        List list_gr3=null;
        List list_gr4=null;
        GomLanguageParser.production_return gr1 = null;
        GomLanguageParser.hookConstruct_return gr2 = null;
        GomLanguageParser.typedecl_return gr3 = null;
        GomLanguageParser.atomdecl_return gr4 = null;
        Tree ABSTRACT11_tree=null;
        Tree SYNTAX12_tree=null;
        RewriteRuleTokenStream stream_ABSTRACT=new RewriteRuleTokenStream(adaptor,"token ABSTRACT");
        RewriteRuleTokenStream stream_SYNTAX=new RewriteRuleTokenStream(adaptor,"token SYNTAX");
        RewriteRuleSubtreeStream stream_atomdecl=new RewriteRuleSubtreeStream(adaptor,"rule atomdecl");
        RewriteRuleSubtreeStream stream_hookConstruct=new RewriteRuleSubtreeStream(adaptor,"rule hookConstruct");
        RewriteRuleSubtreeStream stream_typedecl=new RewriteRuleSubtreeStream(adaptor,"rule typedecl");
        RewriteRuleSubtreeStream stream_production=new RewriteRuleSubtreeStream(adaptor,"rule production");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:90:8: ( ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:91:3: ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )*
            {
            ABSTRACT11=(Token)match(input,ABSTRACT,FOLLOW_ABSTRACT_in_syntax562);  
            stream_ABSTRACT.add(ABSTRACT11);

            SYNTAX12=(Token)match(input,SYNTAX,FOLLOW_SYNTAX_in_syntax564);  
            stream_SYNTAX.add(SYNTAX12);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:91:19: (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )*
            loop6:
            do {
                int alt6=5;
                switch ( input.LA(1) ) {
                case ID:
                    {
                    switch ( input.LA(2) ) {
                    case COLON:
                        {
                        alt6=2;
                        }
                        break;
                    case EQUALS:
                    case BINDS:
                        {
                        alt6=3;
                        }
                        break;
                    case LPAREN:
                        {
                        alt6=1;
                        }
                        break;

                    }

                    }
                    break;
                case MODULE:
                case SORT:
                case OPERATOR:
                    {
                    alt6=2;
                    }
                    break;
                case ATOM:
                    {
                    alt6=4;
                    }
                    break;

                }

                switch (alt6) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:91:20: gr1+= production
            	    {
            	    pushFollow(FOLLOW_production_in_syntax569);
            	    gr1=production();

            	    state._fsp--;

            	    stream_production.add(gr1.getTree());
            	    if (list_gr1==null) list_gr1=new ArrayList();
            	    list_gr1.add(gr1.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:91:38: gr2+= hookConstruct
            	    {
            	    pushFollow(FOLLOW_hookConstruct_in_syntax575);
            	    gr2=hookConstruct();

            	    state._fsp--;

            	    stream_hookConstruct.add(gr2.getTree());
            	    if (list_gr2==null) list_gr2=new ArrayList();
            	    list_gr2.add(gr2.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:91:59: gr3+= typedecl
            	    {
            	    pushFollow(FOLLOW_typedecl_in_syntax581);
            	    gr3=typedecl();

            	    state._fsp--;

            	    stream_typedecl.add(gr3.getTree());
            	    if (list_gr3==null) list_gr3=new ArrayList();
            	    list_gr3.add(gr3.getTree());


            	    }
            	    break;
            	case 4 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:91:75: gr4+= atomdecl
            	    {
            	    pushFollow(FOLLOW_atomdecl_in_syntax587);
            	    gr4=atomdecl();

            	    state._fsp--;

            	    stream_atomdecl.add(gr4.getTree());
            	    if (list_gr4==null) list_gr4=new ArrayList();
            	    list_gr4.add(gr4.getTree());


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);



            // AST REWRITE
            // elements: gr1, gr4, gr3, gr2
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: gr3, gr4, gr1, gr2
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_gr3=new RewriteRuleSubtreeStream(adaptor,"token gr3",list_gr3);
            RewriteRuleSubtreeStream stream_gr4=new RewriteRuleSubtreeStream(adaptor,"token gr4",list_gr4);
            RewriteRuleSubtreeStream stream_gr1=new RewriteRuleSubtreeStream(adaptor,"token gr1",list_gr1);
            RewriteRuleSubtreeStream stream_gr2=new RewriteRuleSubtreeStream(adaptor,"token gr2",list_gr2);
            root_0 = (Tree)adaptor.nil();
            // 92:5: -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:8: ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcGrammar, "ConcGrammar"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:22: ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Grammar, "Grammar"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:32: ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_3);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:49: ( $gr4)*
                while ( stream_gr4.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr4.nextTree());

                }
                stream_gr4.reset();
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:57: ( $gr1)*
                while ( stream_gr1.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr1.nextTree());

                }
                stream_gr1.reset();
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:65: ( $gr2)*
                while ( stream_gr2.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr2.nextTree());

                }
                stream_gr2.reset();
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:73: ( $gr3)*
                while ( stream_gr3.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr3.nextTree());

                }
                stream_gr3.reset();

                adaptor.addChild(root_2, root_3);
                }

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
    // $ANTLR end "syntax"

    public static class production_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "production"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:95:1: production : ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) ;
    public final GomLanguageParser.production_return production() throws RecognitionException {
        GomLanguageParser.production_return retval = new GomLanguageParser.production_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID13=null;
        Token ARROW15=null;
        GomLanguageParser.fieldlist_return fieldlist14 = null;

        GomLanguageParser.type_return type16 = null;


        Tree ID13_tree=null;
        Tree ARROW15_tree=null;
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule fieldlist");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");

        String startLine = ""+input.LT(1).getLine();

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:98:3: ( ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:99:3: ID fieldlist ARROW type
            {
            ID13=(Token)match(input,ID,FOLLOW_ID_in_production649);  
            stream_ID.add(ID13);

            pushFollow(FOLLOW_fieldlist_in_production651);
            fieldlist14=fieldlist();

            state._fsp--;

            stream_fieldlist.add(fieldlist14.getTree());
            ARROW15=(Token)match(input,ARROW,FOLLOW_ARROW_in_production653);  
            stream_ARROW.add(ARROW15);

            pushFollow(FOLLOW_type_in_production655);
            type16=type();

            state._fsp--;

            stream_type.add(type16.getTree());


            // AST REWRITE
            // elements: type, fieldlist, ID, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 99:27: -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:99:30: ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                adaptor.addChild(root_1, stream_type.nextTree());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:99:61: ^( Origin ID[startLine] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (Tree)adaptor.create(ID, startLine));

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
    // $ANTLR end "production"

    public static class atomdecl_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atomdecl"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:102:1: atomdecl : ATOM atom= ID -> ^( AtomDecl ID[atom] ) ;
    public final GomLanguageParser.atomdecl_return atomdecl() throws RecognitionException {
        GomLanguageParser.atomdecl_return retval = new GomLanguageParser.atomdecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token atom=null;
        Token ATOM17=null;

        Tree atom_tree=null;
        Tree ATOM17_tree=null;
        RewriteRuleTokenStream stream_ATOM=new RewriteRuleTokenStream(adaptor,"token ATOM");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:102:10: ( ATOM atom= ID -> ^( AtomDecl ID[atom] ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:103:3: ATOM atom= ID
            {
            ATOM17=(Token)match(input,ATOM,FOLLOW_ATOM_in_atomdecl688);  
            stream_ATOM.add(ATOM17);

            atom=(Token)match(input,ID,FOLLOW_ID_in_atomdecl692);  
            stream_ID.add(atom);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 103:16: -> ^( AtomDecl ID[atom] )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:103:19: ^( AtomDecl ID[atom] )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:106:1: typedecl : (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts) | ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename] -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts) );
    public final GomLanguageParser.typedecl_return typedecl() throws RecognitionException {
        GomLanguageParser.typedecl_return retval = new GomLanguageParser.typedecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token typename=null;
        Token ptypename=null;
        Token EQUALS18=null;
        Token BINDS19=null;
        Token EQUALS20=null;
        GomLanguageParser.alternatives_return alts = null;

        GomLanguageParser.atoms_return b = null;

        GomLanguageParser.pattern_alternatives_return palts = null;


        Tree typename_tree=null;
        Tree ptypename_tree=null;
        Tree EQUALS18_tree=null;
        Tree BINDS19_tree=null;
        Tree EQUALS20_tree=null;
        RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
        RewriteRuleTokenStream stream_BINDS=new RewriteRuleTokenStream(adaptor,"token BINDS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_atoms=new RewriteRuleSubtreeStream(adaptor,"rule atoms");
        RewriteRuleSubtreeStream stream_pattern_alternatives=new RewriteRuleSubtreeStream(adaptor,"rule pattern_alternatives");
        RewriteRuleSubtreeStream stream_alternatives=new RewriteRuleSubtreeStream(adaptor,"rule alternatives");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:106:10: (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts) | ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename] -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts) )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:107:5: typename= ID EQUALS alts= alternatives[typename]
                    {
                    typename=(Token)match(input,ID,FOLLOW_ID_in_typedecl718);  
                    stream_ID.add(typename);

                    EQUALS18=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl721);  
                    stream_EQUALS.add(EQUALS18);

                    pushFollow(FOLLOW_alternatives_in_typedecl725);
                    alts=alternatives(typename);

                    state._fsp--;

                    stream_alternatives.add(alts.getTree());


                    // AST REWRITE
                    // elements: typename, alts
                    // token labels: typename
                    // rule labels: alts, retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_typename=new RewriteRuleTokenStream(adaptor,"token typename",typename);
                    RewriteRuleSubtreeStream stream_alts=new RewriteRuleSubtreeStream(adaptor,"token alts",alts!=null?alts.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 108:7: -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts)
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:108:10: ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SortType, "SortType"), root_1);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:108:21: ^( GomType ^( ExpressionType ) $typename)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:108:31: ^( ExpressionType )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_typename.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:108:60: ^( ConcAtom )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:109:6: ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename]
                    {
                    ptypename=(Token)match(input,ID,FOLLOW_ID_in_typedecl765);  
                    stream_ID.add(ptypename);

                    BINDS19=(Token)match(input,BINDS,FOLLOW_BINDS_in_typedecl767);  
                    stream_BINDS.add(BINDS19);

                    pushFollow(FOLLOW_atoms_in_typedecl771);
                    b=atoms();

                    state._fsp--;

                    stream_atoms.add(b.getTree());
                    EQUALS20=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl773);  
                    stream_EQUALS.add(EQUALS20);

                    pushFollow(FOLLOW_pattern_alternatives_in_typedecl777);
                    palts=pattern_alternatives(ptypename);

                    state._fsp--;

                    stream_pattern_alternatives.add(palts.getTree());


                    // AST REWRITE
                    // elements: b, ptypename, palts
                    // token labels: ptypename
                    // rule labels: retval, palts, b
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_ptypename=new RewriteRuleTokenStream(adaptor,"token ptypename",ptypename);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_palts=new RewriteRuleSubtreeStream(adaptor,"token palts",palts!=null?palts.tree:null);
                    RewriteRuleSubtreeStream stream_b=new RewriteRuleSubtreeStream(adaptor,"token b",b!=null?b.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 110:7: -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts)
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:110:10: ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SortType, "SortType"), root_1);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:110:21: ^( GomType ^( PatternType ) $ptypename)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:110:31: ^( PatternType )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:1: atoms : (atom+= ID )+ -> ^( ConcAtom ( $atom)+ ) ;
    public final GomLanguageParser.atoms_return atoms() throws RecognitionException {
        GomLanguageParser.atoms_return retval = new GomLanguageParser.atoms_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token atom=null;
        List list_atom=null;

        Tree atom_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:7: ( (atom+= ID )+ -> ^( ConcAtom ( $atom)+ ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:114:3: (atom+= ID )+
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:114:3: (atom+= ID )+
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
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:114:4: atom+= ID
            	    {
            	    atom=(Token)match(input,ID,FOLLOW_ID_in_atoms824);  
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
            retval.tree = root_0;
            RewriteRuleTokenStream stream_atom=new RewriteRuleTokenStream(adaptor,"token atom", list_atom);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 114:15: -> ^( ConcAtom ( $atom)+ )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:114:18: ^( ConcAtom ( $atom)+ )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:1: alternatives[Token typename] : ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) ;
    public final GomLanguageParser.alternatives_return alternatives(Token typename) throws RecognitionException {
        GomLanguageParser.alternatives_return retval = new GomLanguageParser.alternatives_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ALT21=null;
        Token ALT23=null;
        Token SEMI25=null;
        GomLanguageParser.opdecl_return opdecl22 = null;

        GomLanguageParser.opdecl_return opdecl24 = null;


        Tree ALT21_tree=null;
        Tree ALT23_tree=null;
        Tree SEMI25_tree=null;
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleSubtreeStream stream_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule opdecl");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:30: ( ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:118:3: ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* ( SEMI )?
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:118:3: ( ALT )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==ALT) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:118:4: ALT
                    {
                    ALT21=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives853);  
                    stream_ALT.add(ALT21);


                    }
                    break;

            }

            pushFollow(FOLLOW_opdecl_in_alternatives857);
            opdecl22=opdecl(typename);

            state._fsp--;

            stream_opdecl.add(opdecl22.getTree());
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:118:27: ( ALT opdecl[typename] )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==ALT) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:118:28: ALT opdecl[typename]
            	    {
            	    ALT23=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives861);  
            	    stream_ALT.add(ALT23);

            	    pushFollow(FOLLOW_opdecl_in_alternatives863);
            	    opdecl24=opdecl(typename);

            	    state._fsp--;

            	    stream_opdecl.add(opdecl24.getTree());

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:118:51: ( SEMI )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==SEMI) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:118:52: SEMI
                    {
                    SEMI25=(Token)match(input,SEMI,FOLLOW_SEMI_in_alternatives869);  
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
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 119:3: -> ^( ConcProduction ( opdecl )+ )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:119:6: ^( ConcProduction ( opdecl )+ )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_1);

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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:122:1: pattern_alternatives[Token typename] : ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( pattern_opdecl )+ ) ;
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
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleSubtreeStream stream_pattern_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule pattern_opdecl");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:122:38: ( ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( pattern_opdecl )+ ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:3: ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )?
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:3: ( ALT )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ALT) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:4: ALT
                    {
                    ALT26=(Token)match(input,ALT,FOLLOW_ALT_in_pattern_alternatives899);  
                    stream_ALT.add(ALT26);


                    }
                    break;

            }

            pushFollow(FOLLOW_pattern_opdecl_in_pattern_alternatives903);
            pattern_opdecl27=pattern_opdecl(typename);

            state._fsp--;

            stream_pattern_opdecl.add(pattern_opdecl27.getTree());
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:35: ( ALT pattern_opdecl[typename] )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==ALT) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:36: ALT pattern_opdecl[typename]
            	    {
            	    ALT28=(Token)match(input,ALT,FOLLOW_ALT_in_pattern_alternatives907);  
            	    stream_ALT.add(ALT28);

            	    pushFollow(FOLLOW_pattern_opdecl_in_pattern_alternatives909);
            	    pattern_opdecl29=pattern_opdecl(typename);

            	    state._fsp--;

            	    stream_pattern_opdecl.add(pattern_opdecl29.getTree());

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:67: ( SEMI )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==SEMI) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:68: SEMI
                    {
                    SEMI30=(Token)match(input,SEMI,FOLLOW_SEMI_in_pattern_alternatives915);  
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
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 124:3: -> ^( ConcProduction ( pattern_opdecl )+ )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:124:6: ^( ConcProduction ( pattern_opdecl )+ )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_1);

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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:1: opdecl[Token type] : ID fieldlist -> ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final GomLanguageParser.opdecl_return opdecl(Token type) throws RecognitionException {
        GomLanguageParser.opdecl_return retval = new GomLanguageParser.opdecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID31=null;
        GomLanguageParser.fieldlist_return fieldlist32 = null;


        Tree ID31_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule fieldlist");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:20: ( ID fieldlist -> ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:3: ID fieldlist
            {
            ID31=(Token)match(input,ID,FOLLOW_ID_in_opdecl944);  
            stream_ID.add(ID31);

            pushFollow(FOLLOW_fieldlist_in_opdecl946);
            fieldlist32=fieldlist();

            state._fsp--;

            stream_fieldlist.add(fieldlist32.getTree());


            // AST REWRITE
            // elements: ID, fieldlist, ID, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 129:3: -> ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:129:6: ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:129:32: ^( GomType ^( ExpressionType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:129:42: ^( ExpressionType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:130:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:133:1: pattern_opdecl[Token type] : ID pattern_fieldlist -> ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
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
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:133:28: ( ID pattern_fieldlist -> ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:134:3: ID pattern_fieldlist
            {
            ID33=(Token)match(input,ID,FOLLOW_ID_in_pattern_opdecl996);  
            stream_ID.add(ID33);

            pushFollow(FOLLOW_pattern_fieldlist_in_pattern_opdecl998);
            pattern_fieldlist34=pattern_fieldlist();

            state._fsp--;

            stream_pattern_fieldlist.add(pattern_fieldlist34.getTree());


            // AST REWRITE
            // elements: ID, pattern_fieldlist, ID, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 135:3: -> ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:135:6: ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_pattern_fieldlist.nextTree());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:135:40: ^( GomType ^( PatternType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:135:50: ^( PatternType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(PatternType, "PatternType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:136:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:139:1: fieldlist : LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) ;
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
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:139:11: ( LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:140:3: LPAREN ( field ( COMMA field )* )? RPAREN
            {
            LPAREN35=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_fieldlist1047);  
            stream_LPAREN.add(LPAREN35);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:140:10: ( field ( COMMA field )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==ID||LA16_0==LDIPLE) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:140:11: field ( COMMA field )*
                    {
                    pushFollow(FOLLOW_field_in_fieldlist1050);
                    field36=field();

                    state._fsp--;

                    stream_field.add(field36.getTree());
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:140:17: ( COMMA field )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==COMMA) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:140:18: COMMA field
                    	    {
                    	    COMMA37=(Token)match(input,COMMA,FOLLOW_COMMA_in_fieldlist1053);  
                    	    stream_COMMA.add(COMMA37);

                    	    pushFollow(FOLLOW_field_in_fieldlist1055);
                    	    field38=field();

                    	    state._fsp--;

                    	    stream_field.add(field38.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAREN39=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_fieldlist1062);  
            stream_RPAREN.add(RPAREN39);



            // AST REWRITE
            // elements: field
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 140:42: -> ^( ConcField ( field )* )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:140:45: ^( ConcField ( field )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:140:57: ( field )*
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:142:1: pattern_fieldlist : LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN -> ^( ConcField ( pattern_field )* ) ;
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
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_pattern_field=new RewriteRuleSubtreeStream(adaptor,"rule pattern_field");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:142:19: ( LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN -> ^( ConcField ( pattern_field )* ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:143:3: LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN
            {
            LPAREN40=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_pattern_fieldlist1084);  
            stream_LPAREN.add(LPAREN40);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:143:10: ( pattern_field ( COMMA pattern_field )* )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==ID||(LA18_0>=INNER && LA18_0<=NEUTRAL)) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:143:11: pattern_field ( COMMA pattern_field )*
                    {
                    pushFollow(FOLLOW_pattern_field_in_pattern_fieldlist1087);
                    pattern_field41=pattern_field();

                    state._fsp--;

                    stream_pattern_field.add(pattern_field41.getTree());
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:143:25: ( COMMA pattern_field )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0==COMMA) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:143:26: COMMA pattern_field
                    	    {
                    	    COMMA42=(Token)match(input,COMMA,FOLLOW_COMMA_in_pattern_fieldlist1090);  
                    	    stream_COMMA.add(COMMA42);

                    	    pushFollow(FOLLOW_pattern_field_in_pattern_fieldlist1092);
                    	    pattern_field43=pattern_field();

                    	    state._fsp--;

                    	    stream_pattern_field.add(pattern_field43.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop17;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAREN44=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_pattern_fieldlist1099);  
            stream_RPAREN.add(RPAREN44);



            // AST REWRITE
            // elements: pattern_field
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 143:58: -> ^( ConcField ( pattern_field )* )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:143:61: ^( ConcField ( pattern_field )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:143:73: ( pattern_field )*
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:145:1: type : ID -> ^( GomType ^( ExpressionType ) ID ) ;
    public final GomLanguageParser.type_return type() throws RecognitionException {
        GomLanguageParser.type_return retval = new GomLanguageParser.type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID45=null;

        Tree ID45_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:145:5: ( ID -> ^( GomType ^( ExpressionType ) ID ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:146:3: ID
            {
            ID45=(Token)match(input,ID,FOLLOW_ID_in_type1120);  
            stream_ID.add(ID45);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 146:6: -> ^( GomType ^( ExpressionType ) ID )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:146:9: ^( GomType ^( ExpressionType ) ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:146:19: ^( ExpressionType )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:149:1: pattern_type : ID -> ^( GomType ^( PatternType ) ID ) ;
    public final GomLanguageParser.pattern_type_return pattern_type() throws RecognitionException {
        GomLanguageParser.pattern_type_return retval = new GomLanguageParser.pattern_type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID46=null;

        Tree ID46_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:149:13: ( ID -> ^( GomType ^( PatternType ) ID ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:150:3: ID
            {
            ID46=(Token)match(input,ID,FOLLOW_ID_in_pattern_type1144);  
            stream_ID.add(ID46);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 150:6: -> ^( GomType ^( PatternType ) ID )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:150:9: ^( GomType ^( PatternType ) ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:150:19: ^( PatternType )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:153:1: field : ( type STAR -> ^( StarredField type ^( None ) ) | LDIPLE pattern_type RDIPLE STAR -> ^( StarredField pattern_type ^( Refresh ) ) | ID COLON type -> ^( NamedField ^( None ) ID type ) | ID COLON LDIPLE pattern_type RDIPLE -> ^( NamedField ^( Refresh ) ID pattern_type ) );
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
        RewriteRuleTokenStream stream_RDIPLE=new RewriteRuleTokenStream(adaptor,"token RDIPLE");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_LDIPLE=new RewriteRuleTokenStream(adaptor,"token LDIPLE");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_pattern_type=new RewriteRuleSubtreeStream(adaptor,"rule pattern_type");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:153:6: ( type STAR -> ^( StarredField type ^( None ) ) | LDIPLE pattern_type RDIPLE STAR -> ^( StarredField pattern_type ^( Refresh ) ) | ID COLON type -> ^( NamedField ^( None ) ID type ) | ID COLON LDIPLE pattern_type RDIPLE -> ^( NamedField ^( Refresh ) ID pattern_type ) )
            int alt19=4;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==ID) ) {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==COLON) ) {
                    int LA19_3 = input.LA(3);

                    if ( (LA19_3==LDIPLE) ) {
                        alt19=4;
                    }
                    else if ( (LA19_3==ID) ) {
                        alt19=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 3, input);

                        throw nvae;
                    }
                }
                else if ( (LA19_1==STAR) ) {
                    alt19=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA19_0==LDIPLE) ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:154:5: type STAR
                    {
                    pushFollow(FOLLOW_type_in_field1170);
                    type47=type();

                    state._fsp--;

                    stream_type.add(type47.getTree());
                    STAR48=(Token)match(input,STAR,FOLLOW_STAR_in_field1172);  
                    stream_STAR.add(STAR48);



                    // AST REWRITE
                    // elements: type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 154:15: -> ^( StarredField type ^( None ) )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:154:18: ^( StarredField type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_type.nextTree());
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:154:38: ^( None )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:155:5: LDIPLE pattern_type RDIPLE STAR
                    {
                    LDIPLE49=(Token)match(input,LDIPLE,FOLLOW_LDIPLE_in_field1190);  
                    stream_LDIPLE.add(LDIPLE49);

                    pushFollow(FOLLOW_pattern_type_in_field1192);
                    pattern_type50=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type50.getTree());
                    RDIPLE51=(Token)match(input,RDIPLE,FOLLOW_RDIPLE_in_field1194);  
                    stream_RDIPLE.add(RDIPLE51);

                    STAR52=(Token)match(input,STAR,FOLLOW_STAR_in_field1196);  
                    stream_STAR.add(STAR52);



                    // AST REWRITE
                    // elements: pattern_type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 155:37: -> ^( StarredField pattern_type ^( Refresh ) )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:155:40: ^( StarredField pattern_type ^( Refresh ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:155:68: ^( Refresh )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:156:5: ID COLON type
                    {
                    ID53=(Token)match(input,ID,FOLLOW_ID_in_field1214);  
                    stream_ID.add(ID53);

                    COLON54=(Token)match(input,COLON,FOLLOW_COLON_in_field1216);  
                    stream_COLON.add(COLON54);

                    pushFollow(FOLLOW_type_in_field1218);
                    type55=type();

                    state._fsp--;

                    stream_type.add(type55.getTree());


                    // AST REWRITE
                    // elements: type, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 156:19: -> ^( NamedField ^( None ) ID type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:156:22: ^( NamedField ^( None ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:156:35: ^( None )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(None, "None"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_type.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:157:5: ID COLON LDIPLE pattern_type RDIPLE
                    {
                    ID56=(Token)match(input,ID,FOLLOW_ID_in_field1238);  
                    stream_ID.add(ID56);

                    COLON57=(Token)match(input,COLON,FOLLOW_COLON_in_field1240);  
                    stream_COLON.add(COLON57);

                    LDIPLE58=(Token)match(input,LDIPLE,FOLLOW_LDIPLE_in_field1242);  
                    stream_LDIPLE.add(LDIPLE58);

                    pushFollow(FOLLOW_pattern_type_in_field1244);
                    pattern_type59=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type59.getTree());
                    RDIPLE60=(Token)match(input,RDIPLE,FOLLOW_RDIPLE_in_field1246);  
                    stream_RDIPLE.add(RDIPLE60);



                    // AST REWRITE
                    // elements: ID, pattern_type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 157:41: -> ^( NamedField ^( Refresh ) ID pattern_type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:157:44: ^( NamedField ^( Refresh ) ID pattern_type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:157:57: ^( Refresh )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Refresh, "Refresh"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_pattern_type.nextTree());

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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:160:1: pattern_field : ( pattern_type STAR -> ^( StarredField pattern_type ^( None ) ) | INNER ID COLON type -> ^( NamedField ^( Inner ) ID type ) | OUTER ID COLON type -> ^( NamedField ^( Outer ) ID type ) | NEUTRAL ID COLON type -> ^( NamedField ^( Neutral ) ID type ) | ID COLON pattern_type -> ^( NamedField ^( None ) ID pattern_type ) );
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
        RewriteRuleTokenStream stream_INNER=new RewriteRuleTokenStream(adaptor,"token INNER");
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_NEUTRAL=new RewriteRuleTokenStream(adaptor,"token NEUTRAL");
        RewriteRuleTokenStream stream_OUTER=new RewriteRuleTokenStream(adaptor,"token OUTER");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_pattern_type=new RewriteRuleSubtreeStream(adaptor,"rule pattern_type");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:160:14: ( pattern_type STAR -> ^( StarredField pattern_type ^( None ) ) | INNER ID COLON type -> ^( NamedField ^( Inner ) ID type ) | OUTER ID COLON type -> ^( NamedField ^( Outer ) ID type ) | NEUTRAL ID COLON type -> ^( NamedField ^( Neutral ) ID type ) | ID COLON pattern_type -> ^( NamedField ^( None ) ID pattern_type ) )
            int alt20=5;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA20_1 = input.LA(2);

                if ( (LA20_1==COLON) ) {
                    alt20=5;
                }
                else if ( (LA20_1==STAR) ) {
                    alt20=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 1, input);

                    throw nvae;
                }
                }
                break;
            case INNER:
                {
                alt20=2;
                }
                break;
            case OUTER:
                {
                alt20=3;
                }
                break;
            case NEUTRAL:
                {
                alt20=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:161:5: pattern_type STAR
                    {
                    pushFollow(FOLLOW_pattern_type_in_pattern_field1274);
                    pattern_type61=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type61.getTree());
                    STAR62=(Token)match(input,STAR,FOLLOW_STAR_in_pattern_field1276);  
                    stream_STAR.add(STAR62);



                    // AST REWRITE
                    // elements: pattern_type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 161:23: -> ^( StarredField pattern_type ^( None ) )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:161:26: ^( StarredField pattern_type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:161:54: ^( None )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:162:5: INNER ID COLON type
                    {
                    INNER63=(Token)match(input,INNER,FOLLOW_INNER_in_pattern_field1294);  
                    stream_INNER.add(INNER63);

                    ID64=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1296);  
                    stream_ID.add(ID64);

                    COLON65=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1298);  
                    stream_COLON.add(COLON65);

                    pushFollow(FOLLOW_type_in_pattern_field1300);
                    type66=type();

                    state._fsp--;

                    stream_type.add(type66.getTree());


                    // AST REWRITE
                    // elements: type, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 162:25: -> ^( NamedField ^( Inner ) ID type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:162:28: ^( NamedField ^( Inner ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:162:41: ^( Inner )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Inner, "Inner"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_type.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:163:5: OUTER ID COLON type
                    {
                    OUTER67=(Token)match(input,OUTER,FOLLOW_OUTER_in_pattern_field1320);  
                    stream_OUTER.add(OUTER67);

                    ID68=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1322);  
                    stream_ID.add(ID68);

                    COLON69=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1324);  
                    stream_COLON.add(COLON69);

                    pushFollow(FOLLOW_type_in_pattern_field1326);
                    type70=type();

                    state._fsp--;

                    stream_type.add(type70.getTree());


                    // AST REWRITE
                    // elements: type, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 163:25: -> ^( NamedField ^( Outer ) ID type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:163:28: ^( NamedField ^( Outer ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:163:41: ^( Outer )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Outer, "Outer"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_type.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:164:5: NEUTRAL ID COLON type
                    {
                    NEUTRAL71=(Token)match(input,NEUTRAL,FOLLOW_NEUTRAL_in_pattern_field1346);  
                    stream_NEUTRAL.add(NEUTRAL71);

                    ID72=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1348);  
                    stream_ID.add(ID72);

                    COLON73=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1350);  
                    stream_COLON.add(COLON73);

                    pushFollow(FOLLOW_type_in_pattern_field1352);
                    type74=type();

                    state._fsp--;

                    stream_type.add(type74.getTree());


                    // AST REWRITE
                    // elements: type, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 164:27: -> ^( NamedField ^( Neutral ) ID type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:164:30: ^( NamedField ^( Neutral ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:164:43: ^( Neutral )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Neutral, "Neutral"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_type.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 5 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:165:5: ID COLON pattern_type
                    {
                    ID75=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1372);  
                    stream_ID.add(ID75);

                    COLON76=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1374);  
                    stream_COLON.add(COLON76);

                    pushFollow(FOLLOW_pattern_type_in_pattern_field1376);
                    pattern_type77=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type77.getTree());


                    // AST REWRITE
                    // elements: pattern_type, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 165:27: -> ^( NamedField ^( None ) ID pattern_type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:165:30: ^( NamedField ^( None ) ID pattern_type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:165:43: ^( None )
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(None, "None"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_pattern_type.nextTree());

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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:168:1: arglist : ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) ;
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
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:168:8: ( ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:169:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:169:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==LPAREN) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:169:4: LPAREN ( arg ( COMMA arg )* )? RPAREN
                    {
                    LPAREN78=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arglist1403);  
                    stream_LPAREN.add(LPAREN78);

                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:169:11: ( arg ( COMMA arg )* )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==ID) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:169:12: arg ( COMMA arg )*
                            {
                            pushFollow(FOLLOW_arg_in_arglist1406);
                            arg79=arg();

                            state._fsp--;

                            stream_arg.add(arg79.getTree());
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:169:16: ( COMMA arg )*
                            loop21:
                            do {
                                int alt21=2;
                                int LA21_0 = input.LA(1);

                                if ( (LA21_0==COMMA) ) {
                                    alt21=1;
                                }


                                switch (alt21) {
                            	case 1 :
                            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:169:17: COMMA arg
                            	    {
                            	    COMMA80=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist1409);  
                            	    stream_COMMA.add(COMMA80);

                            	    pushFollow(FOLLOW_arg_in_arglist1411);
                            	    arg81=arg();

                            	    state._fsp--;

                            	    stream_arg.add(arg81.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop21;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAREN82=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arglist1418);  
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
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 170:3: -> ^( ConcArg ( arg )* )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:170:6: ^( ConcArg ( arg )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcArg, "ConcArg"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:170:16: ( arg )*
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:173:1: arg : ID -> ^( Arg ID ) ;
    public final GomLanguageParser.arg_return arg() throws RecognitionException {
        GomLanguageParser.arg_return retval = new GomLanguageParser.arg_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID83=null;

        Tree ID83_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:173:5: ( ID -> ^( Arg ID ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:173:7: ID
            {
            ID83=(Token)match(input,ID,FOLLOW_ID_in_arg1445);  
            stream_ID.add(ID83);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 173:10: -> ^( Arg ID )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:173:13: ^( Arg ID )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:175:1: hookConstruct : (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
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
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_hookScope=new RewriteRuleSubtreeStream(adaptor,"rule hookScope");
        RewriteRuleSubtreeStream stream_arglist=new RewriteRuleSubtreeStream(adaptor,"rule arglist");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:175:15: ( (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:176:3: (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:176:3: (hscope= hookScope )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==MODULE||(LA24_0>=SORT && LA24_0<=OPERATOR)) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:176:4: hscope= hookScope
                    {
                    pushFollow(FOLLOW_hookScope_in_hookConstruct1466);
                    hscope=hookScope();

                    state._fsp--;

                    stream_hookScope.add(hscope.getTree());

                    }
                    break;

            }

            pointCut=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct1472);  
            stream_ID.add(pointCut);

            COLON84=(Token)match(input,COLON,FOLLOW_COLON_in_hookConstruct1474);  
            stream_COLON.add(COLON84);

            hookType=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct1478);  
            stream_ID.add(hookType);

            pushFollow(FOLLOW_arglist_in_hookConstruct1480);
            arglist85=arglist();

            state._fsp--;

            stream_arglist.add(arglist85.getTree());
            LBRACE86=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_hookConstruct1482);  
            stream_LBRACE.add(LBRACE86);



            // AST REWRITE
            // elements: LBRACE, hookType, arglist, ID, pointCut, ID, hscope, pointCut, arglist, hookType, LBRACE
            // token labels: pointCut, hookType
            // rule labels: hscope, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_pointCut=new RewriteRuleTokenStream(adaptor,"token pointCut",pointCut);
            RewriteRuleTokenStream stream_hookType=new RewriteRuleTokenStream(adaptor,"token hookType",hookType);
            RewriteRuleSubtreeStream stream_hscope=new RewriteRuleSubtreeStream(adaptor,"token hscope",hscope!=null?hscope.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 177:3: -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            if (hscope!=null) {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:177:22: ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Hook, "Hook"), root_1);

                adaptor.addChild(root_1, stream_hscope.nextTree());
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:177:47: ^( HookKind $hookType)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:178:24: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 179:3: -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:179:6: ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Hook, "Hook"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:179:13: ^( KindOperator )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(KindOperator, "KindOperator"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:179:39: ^( HookKind $hookType)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:180:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
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
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:184:1: hookScope : ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) );
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
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");
        RewriteRuleTokenStream stream_SORT=new RewriteRuleTokenStream(adaptor,"token SORT");
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:184:11: ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) )
            int alt25=3;
            switch ( input.LA(1) ) {
            case SORT:
                {
                alt25=1;
                }
                break;
            case MODULE:
                {
                alt25=2;
                }
                break;
            case OPERATOR:
                {
                alt25=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }

            switch (alt25) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:185:3: SORT
                    {
                    SORT87=(Token)match(input,SORT,FOLLOW_SORT_in_hookScope1595);  
                    stream_SORT.add(SORT87);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 185:8: -> ^( KindSort )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:185:11: ^( KindSort )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:186:5: MODULE
                    {
                    MODULE88=(Token)match(input,MODULE,FOLLOW_MODULE_in_hookScope1607);  
                    stream_MODULE.add(MODULE88);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 186:12: -> ^( KindModule )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:186:15: ^( KindModule )
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
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:187:5: OPERATOR
                    {
                    OPERATOR89=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_hookScope1619);  
                    stream_OPERATOR.add(OPERATOR89);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 187:14: -> ^( KindOperator )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:187:17: ^( KindOperator )
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


 

    public static final BitSet FOLLOW_MODULE_in_module346 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_modulename_in_module348 = new BitSet(new long[]{0x0000000000000000L,0x000000E000000000L});
    public static final BitSet FOLLOW_imports_in_module353 = new BitSet(new long[]{0x0000000000000000L,0x000000E000000000L});
    public static final BitSet FOLLOW_section_in_module357 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_modulename416 = new BitSet(new long[]{0x0000000000000000L,0x0000001000000000L});
    public static final BitSet FOLLOW_DOT_in_modulename418 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_modulename428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORTS_in_imports456 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000000L});
    public static final BitSet FOLLOW_importedModuleName_in_imports459 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_importedModuleName488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_section514 = new BitSet(new long[]{0x0000000000000000L,0x000000E000000000L});
    public static final BitSet FOLLOW_adtgrammar_in_section518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_syntax_in_adtgrammar542 = new BitSet(new long[]{0x0000000000000002L,0x000000E000000000L});
    public static final BitSet FOLLOW_ABSTRACT_in_syntax562 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
    public static final BitSet FOLLOW_SYNTAX_in_syntax564 = new BitSet(new long[]{0x0000000000000002L,0x0C00040C00000000L});
    public static final BitSet FOLLOW_production_in_syntax569 = new BitSet(new long[]{0x0000000000000002L,0x0C00040C00000000L});
    public static final BitSet FOLLOW_hookConstruct_in_syntax575 = new BitSet(new long[]{0x0000000000000002L,0x0C00040C00000000L});
    public static final BitSet FOLLOW_typedecl_in_syntax581 = new BitSet(new long[]{0x0000000000000002L,0x0C00040C00000000L});
    public static final BitSet FOLLOW_atomdecl_in_syntax587 = new BitSet(new long[]{0x0000000000000002L,0x0C00040C00000000L});
    public static final BitSet FOLLOW_ID_in_production649 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_fieldlist_in_production651 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
    public static final BitSet FOLLOW_ARROW_in_production653 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_type_in_production655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATOM_in_atomdecl688 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_atomdecl692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl718 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl721 = new BitSet(new long[]{0x0000000000000000L,0x0000200800000000L});
    public static final BitSet FOLLOW_alternatives_in_typedecl725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl765 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_BINDS_in_typedecl767 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_atoms_in_typedecl771 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl773 = new BitSet(new long[]{0x0000000000000000L,0x0000200800000000L});
    public static final BitSet FOLLOW_pattern_alternatives_in_typedecl777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atoms824 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives853 = new BitSet(new long[]{0x0000000000000000L,0x0000200800000000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives857 = new BitSet(new long[]{0x0000000000000002L,0x0000600000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives861 = new BitSet(new long[]{0x0000000000000000L,0x0000200800000000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives863 = new BitSet(new long[]{0x0000000000000002L,0x0000600000000000L});
    public static final BitSet FOLLOW_SEMI_in_alternatives869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALT_in_pattern_alternatives899 = new BitSet(new long[]{0x0000000000000000L,0x0000200800000000L});
    public static final BitSet FOLLOW_pattern_opdecl_in_pattern_alternatives903 = new BitSet(new long[]{0x0000000000000002L,0x0000600000000000L});
    public static final BitSet FOLLOW_ALT_in_pattern_alternatives907 = new BitSet(new long[]{0x0000000000000000L,0x0000200800000000L});
    public static final BitSet FOLLOW_pattern_opdecl_in_pattern_alternatives909 = new BitSet(new long[]{0x0000000000000002L,0x0000600000000000L});
    public static final BitSet FOLLOW_SEMI_in_pattern_alternatives915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_opdecl944 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_fieldlist_in_opdecl946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_opdecl996 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_pattern_fieldlist_in_pattern_opdecl998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_fieldlist1047 = new BitSet(new long[]{0x0000000000000000L,0x000A000800000000L});
    public static final BitSet FOLLOW_field_in_fieldlist1050 = new BitSet(new long[]{0x0000000000000000L,0x0003000000000000L});
    public static final BitSet FOLLOW_COMMA_in_fieldlist1053 = new BitSet(new long[]{0x0000000000000000L,0x0008000800000000L});
    public static final BitSet FOLLOW_field_in_fieldlist1055 = new BitSet(new long[]{0x0000000000000000L,0x0003000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_fieldlist1062 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_pattern_fieldlist1084 = new BitSet(new long[]{0x0000000000000000L,0x01C2000800000000L});
    public static final BitSet FOLLOW_pattern_field_in_pattern_fieldlist1087 = new BitSet(new long[]{0x0000000000000000L,0x0003000000000000L});
    public static final BitSet FOLLOW_COMMA_in_pattern_fieldlist1090 = new BitSet(new long[]{0x0000000000000000L,0x01C0000800000000L});
    public static final BitSet FOLLOW_pattern_field_in_pattern_fieldlist1092 = new BitSet(new long[]{0x0000000000000000L,0x0003000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_pattern_fieldlist1099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_type1120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_type1144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_field1170 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_STAR_in_field1172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LDIPLE_in_field1190 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_pattern_type_in_field1192 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_RDIPLE_in_field1194 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_STAR_in_field1196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field1214 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COLON_in_field1216 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_type_in_field1218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field1238 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COLON_in_field1240 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_LDIPLE_in_field1242 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_pattern_type_in_field1244 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_RDIPLE_in_field1246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_type_in_pattern_field1274 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_STAR_in_pattern_field1276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INNER_in_pattern_field1294 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1296 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1298 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OUTER_in_pattern_field1320 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1322 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1324 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEUTRAL_in_pattern_field1346 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1348 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1350 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_field1372 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1374 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_pattern_type_in_pattern_field1376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arglist1403 = new BitSet(new long[]{0x0000000000000000L,0x0002000800000000L});
    public static final BitSet FOLLOW_arg_in_arglist1406 = new BitSet(new long[]{0x0000000000000000L,0x0003000000000000L});
    public static final BitSet FOLLOW_COMMA_in_arglist1409 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_arg_in_arglist1411 = new BitSet(new long[]{0x0000000000000000L,0x0003000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_arglist1418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_arg1445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_hookScope_in_hookConstruct1466 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct1472 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_COLON_in_hookConstruct1474 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct1478 = new BitSet(new long[]{0x0000000000000000L,0x0200800000000000L});
    public static final BitSet FOLLOW_arglist_in_hookConstruct1480 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_hookConstruct1482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SORT_in_hookScope1595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MODULE_in_hookScope1607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATOR_in_hookScope1619 = new BitSet(new long[]{0x0000000000000002L});

}