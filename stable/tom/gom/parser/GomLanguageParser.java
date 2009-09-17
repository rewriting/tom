// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g 2009-09-17 12:23:01

package tom.gom.parser;
import tom.gom.GomStreamManager;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class GomLanguageParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Arg", "ConcOperator", "OptionList", "GomModuleName", "Cons", "Hook", "ConcSortDecl", "Origin", "Module", "AbstractTypeClass", "KindModule", "Empty", "GomModule", "ExpressionType", "ConcModuleDecl", "NamedField", "SortDecl", "Inner", "AtomDecl", "PatternType", "ConcModule", "Slot", "CutModule", "ImportHookDecl", "ConcSlotField", "CutOperator", "FullSortClass", "Grammar", "ClassName", "OperatorClass", "FullOperatorClass", "InterfaceHookDecl", "TomMapping", "ConcImportedModule", "Sort", "ModuleDecl", "ImportHook", "ConcArg", "Variadic", "MakeHook", "Neutral", "MappingHook", "SortClass", "CutSort", "StarredField", "IsCons", "ConcGomType", "KindSort", "ConcGomClass", "ConcHookDecl", "ConcAtom", "HasTomCode", "ConcClassName", "GomType", "ConcGomModule", "OperatorDecl", "FutureCons", "MakeHookDecl", "ConcSection", "Import", "Details", "Sorts", "CodeList", "ConcProduction", "Compare", "ConcSort", "InterfaceHook", "Code", "ModHookPair", "BlockHookDecl", "CutFutureOperator", "FutureNil", "Imports", "SlotField", "None", "ConcHook", "Public", "ConcGrammar", "MappingHookDecl", "MakeBeforeHook", "VariadicOperatorClass", "ConcSlot", "BuiltinSortDecl", "BlockHook", "Refresh", "Slots", "IsEmpty", "AtomType", "SortType", "HookKind", "KindOperator", "Outer", "ConcField", "Production", "ShortSortClass", "KindFutureOperator", "MODULE", "ID", "DOT", "IMPORTS", "PUBLIC", "ABSTRACT", "SYNTAX", "ARROW", "ATOM", "EQUALS", "BINDS", "JAVADOC", "ALT", "SEMI", "LPAREN", "COMMA", "RPAREN", "STAR", "LDIPLE", "RDIPLE", "COLON", "INNER", "OUTER", "NEUTRAL", "LBRACE", "SORT", "OPERATOR", "PRIVATE", "RBRACE", "WS", "SLCOMMENT", "MLCOMMENT"
    };
    public static final int Arg=4;
    public static final int STAR=117;
    public static final int OptionList=6;
    public static final int Cons=8;
    public static final int Hook=9;
    public static final int Origin=11;
    public static final int Module=12;
    public static final int AbstractTypeClass=13;
    public static final int Empty=15;
    public static final int GomModule=16;
    public static final int ExpressionType=17;
    public static final int SLCOMMENT=130;
    public static final int EQUALS=109;
    public static final int Inner=21;
    public static final int PatternType=23;
    public static final int ATOM=108;
    public static final int Slot=25;
    public static final int EOF=-1;
    public static final int ImportHookDecl=27;
    public static final int ClassName=32;
    public static final int OperatorClass=33;
    public static final int BINDS=110;
    public static final int FullOperatorClass=34;
    public static final int ConcImportedModule=37;
    public static final int ModuleDecl=39;
    public static final int RPAREN=116;
    public static final int ImportHook=40;
    public static final int ConcArg=41;
    public static final int Neutral=44;
    public static final int SortClass=46;
    public static final int StarredField=48;
    public static final int ConcHookDecl=53;
    public static final int ConcAtom=54;
    public static final int MLCOMMENT=131;
    public static final int GomType=57;
    public static final int ConcGomModule=58;
    public static final int ConcSection=62;
    public static final int RBRACE=128;
    public static final int Details=64;
    public static final int Sorts=65;
    public static final int PRIVATE=127;
    public static final int CodeList=66;
    public static final int Compare=68;
    public static final int Code=71;
    public static final int BlockHookDecl=73;
    public static final int NEUTRAL=123;
    public static final int SlotField=77;
    public static final int None=78;
    public static final int ConcHook=79;
    public static final int ConcSlot=85;
    public static final int WS=129;
    public static final int BuiltinSortDecl=86;
    public static final int BlockHook=87;
    public static final int IsEmpty=90;
    public static final int AtomType=91;
    public static final int Outer=95;
    public static final int IMPORTS=103;
    public static final int ConcField=96;
    public static final int ShortSortClass=98;
    public static final int LDIPLE=118;
    public static final int ConcOperator=5;
    public static final int GomModuleName=7;
    public static final int INNER=121;
    public static final int ConcSortDecl=10;
    public static final int LBRACE=124;
    public static final int KindModule=14;
    public static final int ConcModuleDecl=18;
    public static final int NamedField=19;
    public static final int SortDecl=20;
    public static final int ABSTRACT=105;
    public static final int AtomDecl=22;
    public static final int CutModule=26;
    public static final int ConcModule=24;
    public static final int ID=101;
    public static final int ConcSlotField=28;
    public static final int CutOperator=29;
    public static final int LPAREN=114;
    public static final int Grammar=31;
    public static final int FullSortClass=30;
    public static final int TomMapping=36;
    public static final int InterfaceHookDecl=35;
    public static final int SYNTAX=106;
    public static final int Sort=38;
    public static final int MakeHook=43;
    public static final int Variadic=42;
    public static final int ALT=112;
    public static final int MappingHook=45;
    public static final int COMMA=115;
    public static final int CutSort=47;
    public static final int IsCons=49;
    public static final int ConcGomType=50;
    public static final int KindSort=51;
    public static final int ConcGomClass=52;
    public static final int DOT=102;
    public static final int HasTomCode=55;
    public static final int ConcClassName=56;
    public static final int OperatorDecl=59;
    public static final int FutureCons=60;
    public static final int OUTER=122;
    public static final int MakeHookDecl=61;
    public static final int Import=63;
    public static final int JAVADOC=111;
    public static final int RDIPLE=119;
    public static final int ConcProduction=67;
    public static final int OPERATOR=126;
    public static final int ConcSort=69;
    public static final int InterfaceHook=70;
    public static final int ModHookPair=72;
    public static final int SORT=125;
    public static final int CutFutureOperator=74;
    public static final int FutureNil=75;
    public static final int MODULE=100;
    public static final int Imports=76;
    public static final int SEMI=113;
    public static final int Public=80;
    public static final int ConcGrammar=81;
    public static final int COLON=120;
    public static final int MappingHookDecl=82;
    public static final int MakeBeforeHook=83;
    public static final int VariadicOperatorClass=84;
    public static final int Refresh=88;
    public static final int Slots=89;
    public static final int HookKind=93;
    public static final int SortType=92;
    public static final int ARROW=107;
    public static final int KindOperator=94;
    public static final int Production=97;
    public static final int PUBLIC=104;
    public static final int KindFutureOperator=99;

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
    public String getGrammarFileName() { return "/Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g"; }


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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:52:1: module : MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) ;
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
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:52:8: ( MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:53:2: MODULE modulename (imps= imports )? section EOF
            {
            MODULE1=(Token)match(input,MODULE,FOLLOW_MODULE_in_module349);  
            stream_MODULE.add(MODULE1);

            pushFollow(FOLLOW_modulename_in_module351);
            modulename2=modulename();

            state._fsp--;

            stream_modulename.add(modulename2.getTree());
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:53:20: (imps= imports )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==IMPORTS) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:53:21: imps= imports
                    {
                    pushFollow(FOLLOW_imports_in_module356);
                    imps=imports();

                    state._fsp--;

                    stream_imports.add(imps.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_section_in_module360);
            section3=section();

            state._fsp--;

            stream_section.add(section3.getTree());
            EOF4=(Token)match(input,EOF,FOLLOW_EOF_in_module362);  
            stream_EOF.add(EOF4);



            // AST REWRITE
            // elements: imports, section, modulename, section, modulename
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 54:3: -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) )
            if (imps!=null) {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:54:20: ^( GomModule modulename ^( ConcSection imports section ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:54:43: ^( ConcSection imports section )
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
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:55:6: ^( GomModule modulename ^( ConcSection section ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:55:29: ^( ConcSection section )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:58:1: modulename : (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) ;
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
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:61:3: ( (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:62:3: (mod= ID DOT )* moduleName= ID
            {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:62:3: (mod= ID DOT )*
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
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:62:4: mod= ID DOT
            	    {
            	    mod=(Token)match(input,ID,FOLLOW_ID_in_modulename419);  
            	    stream_ID.add(mod);

            	    DOT5=(Token)match(input,DOT,FOLLOW_DOT_in_modulename421);  
            	    stream_DOT.add(DOT5);

            	     packagePrefix.append((mod!=null?mod.getText():null)+"."); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            moduleName=(Token)match(input,ID,FOLLOW_ID_in_modulename431);  
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
            // 72:3: -> ^( GomModuleName $moduleName)
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:72:6: ^( GomModuleName $moduleName)
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:75:1: imports : IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) ;
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
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:75:9: ( IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:76:3: IMPORTS ( importedModuleName )*
            {
            IMPORTS6=(Token)match(input,IMPORTS,FOLLOW_IMPORTS_in_imports459);  
            stream_IMPORTS.add(IMPORTS6);

            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:76:11: ( importedModuleName )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:76:12: importedModuleName
            	    {
            	    pushFollow(FOLLOW_importedModuleName_in_imports462);
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
            // 76:33: -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:76:36: ^( Imports ^( ConcImportedModule ( importedModuleName )* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Imports, "Imports"), root_1);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:76:46: ^( ConcImportedModule ( importedModuleName )* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcImportedModule, "ConcImportedModule"), root_2);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:76:67: ( importedModuleName )*
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:78:1: importedModuleName : ID -> ^( Import ^( GomModuleName ID ) ) ;
    public final GomLanguageParser.importedModuleName_return importedModuleName() throws RecognitionException {
        GomLanguageParser.importedModuleName_return retval = new GomLanguageParser.importedModuleName_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID8=null;

        Tree ID8_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:78:20: ( ID -> ^( Import ^( GomModuleName ID ) ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:79:3: ID
            {
            ID8=(Token)match(input,ID,FOLLOW_ID_in_importedModuleName491);  
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
            // 79:6: -> ^( Import ^( GomModuleName ID ) )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:79:9: ^( Import ^( GomModuleName ID ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Import, "Import"), root_1);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:79:18: ^( GomModuleName ID )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:82:1: section : ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) ;
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
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:82:9: ( ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:83:3: ( PUBLIC )? adtgrammar
            {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:83:3: ( PUBLIC )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==PUBLIC) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:83:4: PUBLIC
                    {
                    PUBLIC9=(Token)match(input,PUBLIC,FOLLOW_PUBLIC_in_section517);  
                    stream_PUBLIC.add(PUBLIC9);


                    }
                    break;

            }

            pushFollow(FOLLOW_adtgrammar_in_section521);
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
            // 83:24: -> ^( Public adtgrammar )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:83:27: ^( Public adtgrammar )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:86:1: adtgrammar : (gr+= syntax )+ -> $gr;
    public final GomLanguageParser.adtgrammar_return adtgrammar() throws RecognitionException {
        GomLanguageParser.adtgrammar_return retval = new GomLanguageParser.adtgrammar_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        List list_gr=null;
        GomLanguageParser.syntax_return gr = null;
         gr = null;
        RewriteRuleSubtreeStream stream_syntax=new RewriteRuleSubtreeStream(adaptor,"rule syntax");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:86:12: ( (gr+= syntax )+ -> $gr)
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:87:3: (gr+= syntax )+
            {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:87:3: (gr+= syntax )+
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
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:87:4: gr+= syntax
            	    {
            	    pushFollow(FOLLOW_syntax_in_adtgrammar545);
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
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:90:1: syntax : ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) ) ;
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
         gr1 = null;
        GomLanguageParser.hookConstruct_return gr2 = null;
         gr2 = null;
        GomLanguageParser.typedecl_return gr3 = null;
         gr3 = null;
        GomLanguageParser.atomdecl_return gr4 = null;
         gr4 = null;
        Tree ABSTRACT11_tree=null;
        Tree SYNTAX12_tree=null;
        RewriteRuleTokenStream stream_SYNTAX=new RewriteRuleTokenStream(adaptor,"token SYNTAX");
        RewriteRuleTokenStream stream_ABSTRACT=new RewriteRuleTokenStream(adaptor,"token ABSTRACT");
        RewriteRuleSubtreeStream stream_hookConstruct=new RewriteRuleSubtreeStream(adaptor,"rule hookConstruct");
        RewriteRuleSubtreeStream stream_typedecl=new RewriteRuleSubtreeStream(adaptor,"rule typedecl");
        RewriteRuleSubtreeStream stream_atomdecl=new RewriteRuleSubtreeStream(adaptor,"rule atomdecl");
        RewriteRuleSubtreeStream stream_production=new RewriteRuleSubtreeStream(adaptor,"rule production");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:90:8: ( ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:91:3: ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )*
            {
            ABSTRACT11=(Token)match(input,ABSTRACT,FOLLOW_ABSTRACT_in_syntax565);  
            stream_ABSTRACT.add(ABSTRACT11);

            SYNTAX12=(Token)match(input,SYNTAX,FOLLOW_SYNTAX_in_syntax567);  
            stream_SYNTAX.add(SYNTAX12);

            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:91:19: (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )*
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
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:91:20: gr1+= production
            	    {
            	    pushFollow(FOLLOW_production_in_syntax572);
            	    gr1=production();

            	    state._fsp--;

            	    stream_production.add(gr1.getTree());
            	    if (list_gr1==null) list_gr1=new ArrayList();
            	    list_gr1.add(gr1.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:91:38: gr2+= hookConstruct
            	    {
            	    pushFollow(FOLLOW_hookConstruct_in_syntax578);
            	    gr2=hookConstruct();

            	    state._fsp--;

            	    stream_hookConstruct.add(gr2.getTree());
            	    if (list_gr2==null) list_gr2=new ArrayList();
            	    list_gr2.add(gr2.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:91:59: gr3+= typedecl
            	    {
            	    pushFollow(FOLLOW_typedecl_in_syntax584);
            	    gr3=typedecl();

            	    state._fsp--;

            	    stream_typedecl.add(gr3.getTree());
            	    if (list_gr3==null) list_gr3=new ArrayList();
            	    list_gr3.add(gr3.getTree());


            	    }
            	    break;
            	case 4 :
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:91:75: gr4+= atomdecl
            	    {
            	    pushFollow(FOLLOW_atomdecl_in_syntax590);
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
            // elements: gr3, gr1, gr2, gr4
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: gr2, gr1, gr4, gr3
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_gr2=new RewriteRuleSubtreeStream(adaptor,"token gr2",list_gr2);
            RewriteRuleSubtreeStream stream_gr1=new RewriteRuleSubtreeStream(adaptor,"token gr1",list_gr1);
            RewriteRuleSubtreeStream stream_gr4=new RewriteRuleSubtreeStream(adaptor,"token gr4",list_gr4);
            RewriteRuleSubtreeStream stream_gr3=new RewriteRuleSubtreeStream(adaptor,"token gr3",list_gr3);
            root_0 = (Tree)adaptor.nil();
            // 92:5: -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:92:8: ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcGrammar, "ConcGrammar"), root_1);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:92:22: ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Grammar, "Grammar"), root_2);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:92:32: ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_3);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:92:49: ( $gr4)*
                while ( stream_gr4.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr4.nextTree());

                }
                stream_gr4.reset();
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:92:57: ( $gr1)*
                while ( stream_gr1.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr1.nextTree());

                }
                stream_gr1.reset();
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:92:65: ( $gr2)*
                while ( stream_gr2.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr2.nextTree());

                }
                stream_gr2.reset();
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:92:73: ( $gr3)*
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:95:1: production : ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) ;
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
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:98:3: ( ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:99:3: ID fieldlist ARROW type
            {
            ID13=(Token)match(input,ID,FOLLOW_ID_in_production653);  
            stream_ID.add(ID13);

            pushFollow(FOLLOW_fieldlist_in_production655);
            fieldlist14=fieldlist();

            state._fsp--;

            stream_fieldlist.add(fieldlist14.getTree());
            ARROW15=(Token)match(input,ARROW,FOLLOW_ARROW_in_production657);  
            stream_ARROW.add(ARROW15);

            pushFollow(FOLLOW_type_in_production659);
            type16=type();

            state._fsp--;

            stream_type.add(type16.getTree());


            // AST REWRITE
            // elements: type, fieldlist, ID, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 99:27: -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:99:30: ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                adaptor.addChild(root_1, stream_type.nextTree());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:99:61: ^( Origin ID[startLine] )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:102:1: atomdecl : ATOM atom= ID -> ^( AtomDecl ID[atom] ) ;
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
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:102:10: ( ATOM atom= ID -> ^( AtomDecl ID[atom] ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:103:3: ATOM atom= ID
            {
            ATOM17=(Token)match(input,ATOM,FOLLOW_ATOM_in_atomdecl692);  
            stream_ATOM.add(ATOM17);

            atom=(Token)match(input,ID,FOLLOW_ID_in_atomdecl696);  
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
            // 103:16: -> ^( AtomDecl ID[atom] )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:103:19: ^( AtomDecl ID[atom] )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:106:1: typedecl : (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts) | ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename] -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts) );
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
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_BINDS=new RewriteRuleTokenStream(adaptor,"token BINDS");
        RewriteRuleSubtreeStream stream_atoms=new RewriteRuleSubtreeStream(adaptor,"rule atoms");
        RewriteRuleSubtreeStream stream_alternatives=new RewriteRuleSubtreeStream(adaptor,"rule alternatives");
        RewriteRuleSubtreeStream stream_pattern_alternatives=new RewriteRuleSubtreeStream(adaptor,"rule pattern_alternatives");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:106:10: (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts) | ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename] -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts) )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:107:5: typename= ID EQUALS alts= alternatives[typename]
                    {
                    typename=(Token)match(input,ID,FOLLOW_ID_in_typedecl722);  
                    stream_ID.add(typename);

                    EQUALS18=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl724);  
                    stream_EQUALS.add(EQUALS18);

                    pushFollow(FOLLOW_alternatives_in_typedecl728);
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
                    // 108:7: -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts)
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:108:10: ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SortType, "SortType"), root_1);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:108:21: ^( GomType ^( ExpressionType ) $typename)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:108:31: ^( ExpressionType )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_typename.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:108:60: ^( ConcAtom )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:109:6: ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename]
                    {
                    ptypename=(Token)match(input,ID,FOLLOW_ID_in_typedecl768);  
                    stream_ID.add(ptypename);

                    BINDS19=(Token)match(input,BINDS,FOLLOW_BINDS_in_typedecl770);  
                    stream_BINDS.add(BINDS19);

                    pushFollow(FOLLOW_atoms_in_typedecl774);
                    b=atoms();

                    state._fsp--;

                    stream_atoms.add(b.getTree());
                    EQUALS20=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl776);  
                    stream_EQUALS.add(EQUALS20);

                    pushFollow(FOLLOW_pattern_alternatives_in_typedecl780);
                    palts=pattern_alternatives(ptypename);

                    state._fsp--;

                    stream_pattern_alternatives.add(palts.getTree());


                    // AST REWRITE
                    // elements: palts, b, ptypename
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
                    // 110:7: -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts)
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:110:10: ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SortType, "SortType"), root_1);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:110:21: ^( GomType ^( PatternType ) $ptypename)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:110:31: ^( PatternType )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:113:1: atoms : (atom+= ID )+ -> ^( ConcAtom ( $atom)+ ) ;
    public final GomLanguageParser.atoms_return atoms() throws RecognitionException {
        GomLanguageParser.atoms_return retval = new GomLanguageParser.atoms_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token atom=null;
        List list_atom=null;

        Tree atom_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:113:7: ( (atom+= ID )+ -> ^( ConcAtom ( $atom)+ ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:114:3: (atom+= ID )+
            {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:114:3: (atom+= ID )+
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
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:114:4: atom+= ID
            	    {
            	    atom=(Token)match(input,ID,FOLLOW_ID_in_atoms827);  
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
            // 114:15: -> ^( ConcAtom ( $atom)+ )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:114:18: ^( ConcAtom ( $atom)+ )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:117:1: alternatives[Token typename] : ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? ) opdecl[typename,jd1] ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) ;
    public final GomLanguageParser.alternatives_return alternatives(Token typename) throws RecognitionException {
        GomLanguageParser.alternatives_return retval = new GomLanguageParser.alternatives_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token jd1=null;
        Token jd2=null;
        Token ALT21=null;
        Token ALT22=null;
        Token ALT23=null;
        Token ALT25=null;
        Token ALT26=null;
        Token ALT27=null;
        Token SEMI29=null;
        GomLanguageParser.opdecl_return opdecl24 = null;

        GomLanguageParser.opdecl_return opdecl28 = null;


        Tree jd1_tree=null;
        Tree jd2_tree=null;
        Tree ALT21_tree=null;
        Tree ALT22_tree=null;
        Tree ALT23_tree=null;
        Tree ALT25_tree=null;
        Tree ALT26_tree=null;
        Tree ALT27_tree=null;
        Tree SEMI29_tree=null;
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleTokenStream stream_JAVADOC=new RewriteRuleTokenStream(adaptor,"token JAVADOC");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleSubtreeStream stream_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule opdecl");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:117:30: ( ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? ) opdecl[typename,jd1] ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:3: ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? ) opdecl[typename,jd1] ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )* ( SEMI )?
            {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:3: ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:4: (jd1= JAVADOC ALT )
                    {
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:4: (jd1= JAVADOC ALT )
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:5: jd1= JAVADOC ALT
                    {
                    jd1=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives859);  
                    stream_JAVADOC.add(jd1);

                    ALT21=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives861);  
                    stream_ALT.add(ALT21);


                    }


                    }
                    break;
                case 2 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:24: ( ALT jd1= JAVADOC )
                    {
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:24: ( ALT jd1= JAVADOC )
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:25: ALT jd1= JAVADOC
                    {
                    ALT22=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives867);  
                    stream_ALT.add(ALT22);

                    jd1=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives871);  
                    stream_JAVADOC.add(jd1);


                    }


                    }
                    break;
                case 3 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:44: jd1= JAVADOC
                    {
                    jd1=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives878);  
                    stream_JAVADOC.add(jd1);


                    }
                    break;
                case 4 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:58: ( ALT )?
                    {
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:58: ( ALT )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==ALT) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:118:59: ALT
                            {
                            ALT23=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives883);  
                            stream_ALT.add(ALT23);


                            }
                            break;

                    }


                    }
                    break;

            }

            pushFollow(FOLLOW_opdecl_in_alternatives891);
            opdecl24=opdecl(typename, jd1);

            state._fsp--;

            stream_opdecl.add(opdecl24.getTree());
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:120:3: ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=JAVADOC && LA12_0<=ALT)) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:121:4: ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2]
            	    {
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:121:4: ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT )
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
            	            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:121:5: (jd2= JAVADOC ALT )
            	            {
            	            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:121:5: (jd2= JAVADOC ALT )
            	            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:121:6: jd2= JAVADOC ALT
            	            {
            	            jd2=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives906);  
            	            stream_JAVADOC.add(jd2);

            	            ALT25=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives908);  
            	            stream_ALT.add(ALT25);


            	            }


            	            }
            	            break;
            	        case 2 :
            	            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:121:25: ( ALT jd2= JAVADOC )
            	            {
            	            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:121:25: ( ALT jd2= JAVADOC )
            	            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:121:26: ALT jd2= JAVADOC
            	            {
            	            ALT26=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives914);  
            	            stream_ALT.add(ALT26);

            	            jd2=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives918);  
            	            stream_JAVADOC.add(jd2);


            	            }


            	            }
            	            break;
            	        case 3 :
            	            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:121:45: ALT
            	            {
            	            ALT27=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives923);  
            	            stream_ALT.add(ALT27);

            	            jd2=null;

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_opdecl_in_alternatives932);
            	    opdecl28=opdecl(typename, jd2);

            	    state._fsp--;

            	    stream_opdecl.add(opdecl28.getTree());

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:123:6: ( SEMI )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==SEMI) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:123:7: SEMI
                    {
                    SEMI29=(Token)match(input,SEMI,FOLLOW_SEMI_in_alternatives942);  
                    stream_SEMI.add(SEMI29);


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
            // 124:3: -> ^( ConcProduction ( opdecl )+ )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:124:6: ^( ConcProduction ( opdecl )+ )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:128:1: pattern_alternatives[Token typename] : ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( pattern_opdecl )+ ) ;
    public final GomLanguageParser.pattern_alternatives_return pattern_alternatives(Token typename) throws RecognitionException {
        GomLanguageParser.pattern_alternatives_return retval = new GomLanguageParser.pattern_alternatives_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ALT30=null;
        Token ALT32=null;
        Token SEMI34=null;
        GomLanguageParser.pattern_opdecl_return pattern_opdecl31 = null;

        GomLanguageParser.pattern_opdecl_return pattern_opdecl33 = null;


        Tree ALT30_tree=null;
        Tree ALT32_tree=null;
        Tree SEMI34_tree=null;
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleSubtreeStream stream_pattern_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule pattern_opdecl");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:128:38: ( ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( pattern_opdecl )+ ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:129:3: ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )?
            {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:129:3: ( ALT )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==ALT) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:129:4: ALT
                    {
                    ALT30=(Token)match(input,ALT,FOLLOW_ALT_in_pattern_alternatives974);  
                    stream_ALT.add(ALT30);


                    }
                    break;

            }

            pushFollow(FOLLOW_pattern_opdecl_in_pattern_alternatives978);
            pattern_opdecl31=pattern_opdecl(typename);

            state._fsp--;

            stream_pattern_opdecl.add(pattern_opdecl31.getTree());
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:129:35: ( ALT pattern_opdecl[typename] )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==ALT) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:129:36: ALT pattern_opdecl[typename]
            	    {
            	    ALT32=(Token)match(input,ALT,FOLLOW_ALT_in_pattern_alternatives982);  
            	    stream_ALT.add(ALT32);

            	    pushFollow(FOLLOW_pattern_opdecl_in_pattern_alternatives984);
            	    pattern_opdecl33=pattern_opdecl(typename);

            	    state._fsp--;

            	    stream_pattern_opdecl.add(pattern_opdecl33.getTree());

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:129:67: ( SEMI )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==SEMI) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:129:68: SEMI
                    {
                    SEMI34=(Token)match(input,SEMI,FOLLOW_SEMI_in_pattern_alternatives990);  
                    stream_SEMI.add(SEMI34);


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
            // 130:3: -> ^( ConcProduction ( pattern_opdecl )+ )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:130:6: ^( ConcProduction ( pattern_opdecl )+ )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:133:1: opdecl[Token type, Token JAVADOC] : ID fieldlist -> {JAVADOC!=null}? ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) ) -> ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final GomLanguageParser.opdecl_return opdecl(Token type, Token JAVADOC) throws RecognitionException {
        GomLanguageParser.opdecl_return retval = new GomLanguageParser.opdecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID35=null;
        GomLanguageParser.fieldlist_return fieldlist36 = null;


        Tree ID35_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule fieldlist");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:133:35: ( ID fieldlist -> {JAVADOC!=null}? ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) ) -> ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:134:2: ID fieldlist
            {
            ID35=(Token)match(input,ID,FOLLOW_ID_in_opdecl1018);  
            stream_ID.add(ID35);

            pushFollow(FOLLOW_fieldlist_in_opdecl1020);
            fieldlist36=fieldlist();

            state._fsp--;

            stream_fieldlist.add(fieldlist36.getTree());


            // AST REWRITE
            // elements: fieldlist, fieldlist, ID, ID, ID, ID, ID, ID, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 135:3: -> {JAVADOC!=null}? ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) )
            if (JAVADOC!=null) {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:135:23: ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:135:49: ^( GomType ^( ExpressionType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:135:59: ^( ExpressionType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:136:7: ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(OptionList, "OptionList"), root_2);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:136:20: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_3);

                adaptor.addChild(root_3, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_2, root_3);
                }
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:136:59: ^( Details ID[JAVADOC] )
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
            else // 137:3: -> ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:137:6: ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:137:32: ^( GomType ^( ExpressionType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:137:42: ^( ExpressionType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:138:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:142:1: pattern_opdecl[Token type] : ID pattern_fieldlist -> ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final GomLanguageParser.pattern_opdecl_return pattern_opdecl(Token type) throws RecognitionException {
        GomLanguageParser.pattern_opdecl_return retval = new GomLanguageParser.pattern_opdecl_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID37=null;
        GomLanguageParser.pattern_fieldlist_return pattern_fieldlist38 = null;


        Tree ID37_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_pattern_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule pattern_fieldlist");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:142:28: ( ID pattern_fieldlist -> ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:143:2: ID pattern_fieldlist
            {
            ID37=(Token)match(input,ID,FOLLOW_ID_in_pattern_opdecl1120);  
            stream_ID.add(ID37);

            pushFollow(FOLLOW_pattern_fieldlist_in_pattern_opdecl1122);
            pattern_fieldlist38=pattern_fieldlist();

            state._fsp--;

            stream_pattern_fieldlist.add(pattern_fieldlist38.getTree());


            // AST REWRITE
            // elements: ID, ID, ID, pattern_fieldlist
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 144:3: -> ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:144:6: ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_pattern_fieldlist.nextTree());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:144:40: ^( GomType ^( PatternType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:144:50: ^( PatternType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(PatternType, "PatternType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:145:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:148:1: fieldlist : LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) ;
    public final GomLanguageParser.fieldlist_return fieldlist() throws RecognitionException {
        GomLanguageParser.fieldlist_return retval = new GomLanguageParser.fieldlist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAREN39=null;
        Token COMMA41=null;
        Token RPAREN43=null;
        GomLanguageParser.field_return field40 = null;

        GomLanguageParser.field_return field42 = null;


        Tree LPAREN39_tree=null;
        Tree COMMA41_tree=null;
        Tree RPAREN43_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:148:11: ( LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:149:3: LPAREN ( field ( COMMA field )* )? RPAREN
            {
            LPAREN39=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_fieldlist1171);  
            stream_LPAREN.add(LPAREN39);

            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:149:10: ( field ( COMMA field )* )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==ID||LA18_0==LDIPLE) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:149:11: field ( COMMA field )*
                    {
                    pushFollow(FOLLOW_field_in_fieldlist1174);
                    field40=field();

                    state._fsp--;

                    stream_field.add(field40.getTree());
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:149:17: ( COMMA field )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0==COMMA) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:149:18: COMMA field
                    	    {
                    	    COMMA41=(Token)match(input,COMMA,FOLLOW_COMMA_in_fieldlist1177);  
                    	    stream_COMMA.add(COMMA41);

                    	    pushFollow(FOLLOW_field_in_fieldlist1179);
                    	    field42=field();

                    	    state._fsp--;

                    	    stream_field.add(field42.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop17;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAREN43=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_fieldlist1186);  
            stream_RPAREN.add(RPAREN43);



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
            // 149:42: -> ^( ConcField ( field )* )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:149:45: ^( ConcField ( field )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:149:57: ( field )*
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:152:1: pattern_fieldlist : LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN -> ^( ConcField ( pattern_field )* ) ;
    public final GomLanguageParser.pattern_fieldlist_return pattern_fieldlist() throws RecognitionException {
        GomLanguageParser.pattern_fieldlist_return retval = new GomLanguageParser.pattern_fieldlist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAREN44=null;
        Token COMMA46=null;
        Token RPAREN48=null;
        GomLanguageParser.pattern_field_return pattern_field45 = null;

        GomLanguageParser.pattern_field_return pattern_field47 = null;


        Tree LPAREN44_tree=null;
        Tree COMMA46_tree=null;
        Tree RPAREN48_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_pattern_field=new RewriteRuleSubtreeStream(adaptor,"rule pattern_field");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:152:19: ( LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN -> ^( ConcField ( pattern_field )* ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:153:3: LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN
            {
            LPAREN44=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_pattern_fieldlist1210);  
            stream_LPAREN.add(LPAREN44);

            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:153:10: ( pattern_field ( COMMA pattern_field )* )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==ID||(LA20_0>=INNER && LA20_0<=NEUTRAL)) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:153:11: pattern_field ( COMMA pattern_field )*
                    {
                    pushFollow(FOLLOW_pattern_field_in_pattern_fieldlist1213);
                    pattern_field45=pattern_field();

                    state._fsp--;

                    stream_pattern_field.add(pattern_field45.getTree());
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:153:25: ( COMMA pattern_field )*
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( (LA19_0==COMMA) ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:153:26: COMMA pattern_field
                    	    {
                    	    COMMA46=(Token)match(input,COMMA,FOLLOW_COMMA_in_pattern_fieldlist1216);  
                    	    stream_COMMA.add(COMMA46);

                    	    pushFollow(FOLLOW_pattern_field_in_pattern_fieldlist1218);
                    	    pattern_field47=pattern_field();

                    	    state._fsp--;

                    	    stream_pattern_field.add(pattern_field47.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop19;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAREN48=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_pattern_fieldlist1225);  
            stream_RPAREN.add(RPAREN48);



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
            // 153:58: -> ^( ConcField ( pattern_field )* )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:153:61: ^( ConcField ( pattern_field )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:153:73: ( pattern_field )*
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:155:1: type : ID -> ^( GomType ^( ExpressionType ) ID ) ;
    public final GomLanguageParser.type_return type() throws RecognitionException {
        GomLanguageParser.type_return retval = new GomLanguageParser.type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID49=null;

        Tree ID49_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:155:5: ( ID -> ^( GomType ^( ExpressionType ) ID ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:156:3: ID
            {
            ID49=(Token)match(input,ID,FOLLOW_ID_in_type1246);  
            stream_ID.add(ID49);



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
            // 156:6: -> ^( GomType ^( ExpressionType ) ID )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:156:9: ^( GomType ^( ExpressionType ) ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_1);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:156:19: ^( ExpressionType )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:160:1: pattern_type : ID -> ^( GomType ^( PatternType ) ID ) ;
    public final GomLanguageParser.pattern_type_return pattern_type() throws RecognitionException {
        GomLanguageParser.pattern_type_return retval = new GomLanguageParser.pattern_type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID50=null;

        Tree ID50_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:160:13: ( ID -> ^( GomType ^( PatternType ) ID ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:161:3: ID
            {
            ID50=(Token)match(input,ID,FOLLOW_ID_in_pattern_type1272);  
            stream_ID.add(ID50);



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
            // 161:6: -> ^( GomType ^( PatternType ) ID )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:161:9: ^( GomType ^( PatternType ) ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_1);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:161:19: ^( PatternType )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:164:1: field : ( type STAR -> ^( StarredField type ^( None ) ) | LDIPLE pattern_type RDIPLE STAR -> ^( StarredField pattern_type ^( Refresh ) ) | ID COLON type -> ^( NamedField ^( None ) ID type ) | ID COLON LDIPLE pattern_type RDIPLE -> ^( NamedField ^( Refresh ) ID pattern_type ) );
    public final GomLanguageParser.field_return field() throws RecognitionException {
        GomLanguageParser.field_return retval = new GomLanguageParser.field_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token STAR52=null;
        Token LDIPLE53=null;
        Token RDIPLE55=null;
        Token STAR56=null;
        Token ID57=null;
        Token COLON58=null;
        Token ID60=null;
        Token COLON61=null;
        Token LDIPLE62=null;
        Token RDIPLE64=null;
        GomLanguageParser.type_return type51 = null;

        GomLanguageParser.pattern_type_return pattern_type54 = null;

        GomLanguageParser.type_return type59 = null;

        GomLanguageParser.pattern_type_return pattern_type63 = null;


        Tree STAR52_tree=null;
        Tree LDIPLE53_tree=null;
        Tree RDIPLE55_tree=null;
        Tree STAR56_tree=null;
        Tree ID57_tree=null;
        Tree COLON58_tree=null;
        Tree ID60_tree=null;
        Tree COLON61_tree=null;
        Tree LDIPLE62_tree=null;
        Tree RDIPLE64_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_LDIPLE=new RewriteRuleTokenStream(adaptor,"token LDIPLE");
        RewriteRuleTokenStream stream_RDIPLE=new RewriteRuleTokenStream(adaptor,"token RDIPLE");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_pattern_type=new RewriteRuleSubtreeStream(adaptor,"rule pattern_type");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:164:6: ( type STAR -> ^( StarredField type ^( None ) ) | LDIPLE pattern_type RDIPLE STAR -> ^( StarredField pattern_type ^( Refresh ) ) | ID COLON type -> ^( NamedField ^( None ) ID type ) | ID COLON LDIPLE pattern_type RDIPLE -> ^( NamedField ^( Refresh ) ID pattern_type ) )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:165:5: type STAR
                    {
                    pushFollow(FOLLOW_type_in_field1298);
                    type51=type();

                    state._fsp--;

                    stream_type.add(type51.getTree());
                    STAR52=(Token)match(input,STAR,FOLLOW_STAR_in_field1300);  
                    stream_STAR.add(STAR52);



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
                    // 165:15: -> ^( StarredField type ^( None ) )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:165:18: ^( StarredField type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_type.nextTree());
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:165:38: ^( None )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:166:5: LDIPLE pattern_type RDIPLE STAR
                    {
                    LDIPLE53=(Token)match(input,LDIPLE,FOLLOW_LDIPLE_in_field1318);  
                    stream_LDIPLE.add(LDIPLE53);

                    pushFollow(FOLLOW_pattern_type_in_field1320);
                    pattern_type54=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type54.getTree());
                    RDIPLE55=(Token)match(input,RDIPLE,FOLLOW_RDIPLE_in_field1322);  
                    stream_RDIPLE.add(RDIPLE55);

                    STAR56=(Token)match(input,STAR,FOLLOW_STAR_in_field1324);  
                    stream_STAR.add(STAR56);



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
                    // 166:37: -> ^( StarredField pattern_type ^( Refresh ) )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:166:40: ^( StarredField pattern_type ^( Refresh ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:166:68: ^( Refresh )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:167:5: ID COLON type
                    {
                    ID57=(Token)match(input,ID,FOLLOW_ID_in_field1342);  
                    stream_ID.add(ID57);

                    COLON58=(Token)match(input,COLON,FOLLOW_COLON_in_field1344);  
                    stream_COLON.add(COLON58);

                    pushFollow(FOLLOW_type_in_field1346);
                    type59=type();

                    state._fsp--;

                    stream_type.add(type59.getTree());


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
                    // 167:19: -> ^( NamedField ^( None ) ID type )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:167:22: ^( NamedField ^( None ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:167:35: ^( None )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:168:5: ID COLON LDIPLE pattern_type RDIPLE
                    {
                    ID60=(Token)match(input,ID,FOLLOW_ID_in_field1366);  
                    stream_ID.add(ID60);

                    COLON61=(Token)match(input,COLON,FOLLOW_COLON_in_field1368);  
                    stream_COLON.add(COLON61);

                    LDIPLE62=(Token)match(input,LDIPLE,FOLLOW_LDIPLE_in_field1370);  
                    stream_LDIPLE.add(LDIPLE62);

                    pushFollow(FOLLOW_pattern_type_in_field1372);
                    pattern_type63=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type63.getTree());
                    RDIPLE64=(Token)match(input,RDIPLE,FOLLOW_RDIPLE_in_field1374);  
                    stream_RDIPLE.add(RDIPLE64);



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
                    // 168:41: -> ^( NamedField ^( Refresh ) ID pattern_type )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:168:44: ^( NamedField ^( Refresh ) ID pattern_type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:168:57: ^( Refresh )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:172:1: pattern_field : ( pattern_type STAR -> ^( StarredField pattern_type ^( None ) ) | INNER ID COLON type -> ^( NamedField ^( Inner ) ID type ) | OUTER ID COLON type -> ^( NamedField ^( Outer ) ID type ) | NEUTRAL ID COLON type -> ^( NamedField ^( Neutral ) ID type ) | ID COLON pattern_type -> ^( NamedField ^( None ) ID pattern_type ) );
    public final GomLanguageParser.pattern_field_return pattern_field() throws RecognitionException {
        GomLanguageParser.pattern_field_return retval = new GomLanguageParser.pattern_field_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token STAR66=null;
        Token INNER67=null;
        Token ID68=null;
        Token COLON69=null;
        Token OUTER71=null;
        Token ID72=null;
        Token COLON73=null;
        Token NEUTRAL75=null;
        Token ID76=null;
        Token COLON77=null;
        Token ID79=null;
        Token COLON80=null;
        GomLanguageParser.pattern_type_return pattern_type65 = null;

        GomLanguageParser.type_return type70 = null;

        GomLanguageParser.type_return type74 = null;

        GomLanguageParser.type_return type78 = null;

        GomLanguageParser.pattern_type_return pattern_type81 = null;


        Tree STAR66_tree=null;
        Tree INNER67_tree=null;
        Tree ID68_tree=null;
        Tree COLON69_tree=null;
        Tree OUTER71_tree=null;
        Tree ID72_tree=null;
        Tree COLON73_tree=null;
        Tree NEUTRAL75_tree=null;
        Tree ID76_tree=null;
        Tree COLON77_tree=null;
        Tree ID79_tree=null;
        Tree COLON80_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_OUTER=new RewriteRuleTokenStream(adaptor,"token OUTER");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_INNER=new RewriteRuleTokenStream(adaptor,"token INNER");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_NEUTRAL=new RewriteRuleTokenStream(adaptor,"token NEUTRAL");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_pattern_type=new RewriteRuleSubtreeStream(adaptor,"rule pattern_type");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:172:14: ( pattern_type STAR -> ^( StarredField pattern_type ^( None ) ) | INNER ID COLON type -> ^( NamedField ^( Inner ) ID type ) | OUTER ID COLON type -> ^( NamedField ^( Outer ) ID type ) | NEUTRAL ID COLON type -> ^( NamedField ^( Neutral ) ID type ) | ID COLON pattern_type -> ^( NamedField ^( None ) ID pattern_type ) )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:173:5: pattern_type STAR
                    {
                    pushFollow(FOLLOW_pattern_type_in_pattern_field1404);
                    pattern_type65=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type65.getTree());
                    STAR66=(Token)match(input,STAR,FOLLOW_STAR_in_pattern_field1406);  
                    stream_STAR.add(STAR66);



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
                    // 173:23: -> ^( StarredField pattern_type ^( None ) )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:173:26: ^( StarredField pattern_type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:173:54: ^( None )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:174:5: INNER ID COLON type
                    {
                    INNER67=(Token)match(input,INNER,FOLLOW_INNER_in_pattern_field1424);  
                    stream_INNER.add(INNER67);

                    ID68=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1426);  
                    stream_ID.add(ID68);

                    COLON69=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1428);  
                    stream_COLON.add(COLON69);

                    pushFollow(FOLLOW_type_in_pattern_field1430);
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
                    // 174:25: -> ^( NamedField ^( Inner ) ID type )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:174:28: ^( NamedField ^( Inner ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:174:41: ^( Inner )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:175:5: OUTER ID COLON type
                    {
                    OUTER71=(Token)match(input,OUTER,FOLLOW_OUTER_in_pattern_field1450);  
                    stream_OUTER.add(OUTER71);

                    ID72=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1452);  
                    stream_ID.add(ID72);

                    COLON73=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1454);  
                    stream_COLON.add(COLON73);

                    pushFollow(FOLLOW_type_in_pattern_field1456);
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
                    // 175:25: -> ^( NamedField ^( Outer ) ID type )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:175:28: ^( NamedField ^( Outer ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:175:41: ^( Outer )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:176:5: NEUTRAL ID COLON type
                    {
                    NEUTRAL75=(Token)match(input,NEUTRAL,FOLLOW_NEUTRAL_in_pattern_field1476);  
                    stream_NEUTRAL.add(NEUTRAL75);

                    ID76=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1478);  
                    stream_ID.add(ID76);

                    COLON77=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1480);  
                    stream_COLON.add(COLON77);

                    pushFollow(FOLLOW_type_in_pattern_field1482);
                    type78=type();

                    state._fsp--;

                    stream_type.add(type78.getTree());


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
                    // 176:27: -> ^( NamedField ^( Neutral ) ID type )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:176:30: ^( NamedField ^( Neutral ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:176:43: ^( Neutral )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:177:5: ID COLON pattern_type
                    {
                    ID79=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1502);  
                    stream_ID.add(ID79);

                    COLON80=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1504);  
                    stream_COLON.add(COLON80);

                    pushFollow(FOLLOW_pattern_type_in_pattern_field1506);
                    pattern_type81=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type81.getTree());


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
                    // 177:27: -> ^( NamedField ^( None ) ID pattern_type )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:177:30: ^( NamedField ^( None ) ID pattern_type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:177:43: ^( None )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:180:1: arglist : ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) ;
    public final GomLanguageParser.arglist_return arglist() throws RecognitionException {
        GomLanguageParser.arglist_return retval = new GomLanguageParser.arglist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LPAREN82=null;
        Token COMMA84=null;
        Token RPAREN86=null;
        GomLanguageParser.arg_return arg83 = null;

        GomLanguageParser.arg_return arg85 = null;


        Tree LPAREN82_tree=null;
        Tree COMMA84_tree=null;
        Tree RPAREN86_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:180:8: ( ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:181:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:181:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==LPAREN) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:181:4: LPAREN ( arg ( COMMA arg )* )? RPAREN
                    {
                    LPAREN82=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arglist1533);  
                    stream_LPAREN.add(LPAREN82);

                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:181:11: ( arg ( COMMA arg )* )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==ID) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:181:12: arg ( COMMA arg )*
                            {
                            pushFollow(FOLLOW_arg_in_arglist1536);
                            arg83=arg();

                            state._fsp--;

                            stream_arg.add(arg83.getTree());
                            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:181:16: ( COMMA arg )*
                            loop23:
                            do {
                                int alt23=2;
                                int LA23_0 = input.LA(1);

                                if ( (LA23_0==COMMA) ) {
                                    alt23=1;
                                }


                                switch (alt23) {
                            	case 1 :
                            	    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:181:17: COMMA arg
                            	    {
                            	    COMMA84=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist1539);  
                            	    stream_COMMA.add(COMMA84);

                            	    pushFollow(FOLLOW_arg_in_arglist1541);
                            	    arg85=arg();

                            	    state._fsp--;

                            	    stream_arg.add(arg85.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop23;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAREN86=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arglist1548);  
                    stream_RPAREN.add(RPAREN86);


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
            // 182:3: -> ^( ConcArg ( arg )* )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:182:6: ^( ConcArg ( arg )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcArg, "ConcArg"), root_1);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:182:16: ( arg )*
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:185:1: arg : ID -> ^( Arg ID ) ;
    public final GomLanguageParser.arg_return arg() throws RecognitionException {
        GomLanguageParser.arg_return retval = new GomLanguageParser.arg_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID87=null;

        Tree ID87_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:185:5: ( ID -> ^( Arg ID ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:185:7: ID
            {
            ID87=(Token)match(input,ID,FOLLOW_ID_in_arg1575);  
            stream_ID.add(ID87);



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
            // 185:10: -> ^( Arg ID )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:185:13: ^( Arg ID )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:187:1: hookConstruct : (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final GomLanguageParser.hookConstruct_return hookConstruct() throws RecognitionException {
        GomLanguageParser.hookConstruct_return retval = new GomLanguageParser.hookConstruct_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token pointCut=null;
        Token hookType=null;
        Token COLON88=null;
        Token LBRACE90=null;
        GomLanguageParser.hookScope_return hscope = null;

        GomLanguageParser.arglist_return arglist89 = null;


        Tree pointCut_tree=null;
        Tree hookType_tree=null;
        Tree COLON88_tree=null;
        Tree LBRACE90_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_arglist=new RewriteRuleSubtreeStream(adaptor,"rule arglist");
        RewriteRuleSubtreeStream stream_hookScope=new RewriteRuleSubtreeStream(adaptor,"rule hookScope");
        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:187:15: ( (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:188:3: (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE
            {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:188:3: (hscope= hookScope )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==MODULE||(LA26_0>=SORT && LA26_0<=OPERATOR)) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:188:4: hscope= hookScope
                    {
                    pushFollow(FOLLOW_hookScope_in_hookConstruct1596);
                    hscope=hookScope();

                    state._fsp--;

                    stream_hookScope.add(hscope.getTree());

                    }
                    break;

            }

            pointCut=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct1602);  
            stream_ID.add(pointCut);

            COLON88=(Token)match(input,COLON,FOLLOW_COLON_in_hookConstruct1604);  
            stream_COLON.add(COLON88);

            hookType=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct1608);  
            stream_ID.add(hookType);

            pushFollow(FOLLOW_arglist_in_hookConstruct1610);
            arglist89=arglist();

            state._fsp--;

            stream_arglist.add(arglist89.getTree());
            LBRACE90=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_hookConstruct1612);  
            stream_LBRACE.add(LBRACE90);



            // AST REWRITE
            // elements: LBRACE, hookType, pointCut, pointCut, hscope, LBRACE, arglist, ID, ID, arglist, hookType
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
            // 189:3: -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            if (hscope!=null) {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:189:22: ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Hook, "Hook"), root_1);

                adaptor.addChild(root_1, stream_hscope.nextTree());
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:189:47: ^( HookKind $hookType)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:190:24: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 191:3: -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:191:6: ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Hook, "Hook"), root_1);

                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:191:13: ^( KindOperator )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(KindOperator, "KindOperator"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:191:39: ^( HookKind $hookType)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:192:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
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
    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:196:1: hookScope : ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) );
    public final GomLanguageParser.hookScope_return hookScope() throws RecognitionException {
        GomLanguageParser.hookScope_return retval = new GomLanguageParser.hookScope_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token SORT91=null;
        Token MODULE92=null;
        Token OPERATOR93=null;

        Tree SORT91_tree=null;
        Tree MODULE92_tree=null;
        Tree OPERATOR93_tree=null;
        RewriteRuleTokenStream stream_SORT=new RewriteRuleTokenStream(adaptor,"token SORT");
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");

        try {
            // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:196:11: ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:197:3: SORT
                    {
                    SORT91=(Token)match(input,SORT,FOLLOW_SORT_in_hookScope1725);  
                    stream_SORT.add(SORT91);



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
                    // 197:8: -> ^( KindSort )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:197:11: ^( KindSort )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:198:5: MODULE
                    {
                    MODULE92=(Token)match(input,MODULE,FOLLOW_MODULE_in_hookScope1737);  
                    stream_MODULE.add(MODULE92);



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
                    // 198:12: -> ^( KindModule )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:198:15: ^( KindModule )
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
                    // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:199:5: OPERATOR
                    {
                    OPERATOR93=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_hookScope1749);  
                    stream_OPERATOR.add(OPERATOR93);



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
                    // 199:14: -> ^( KindOperator )
                    {
                        // /Users/balland/workspace/wtjtom/src/gen/tom/gom/parser/GomLanguage.g:199:17: ^( KindOperator )
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


 

    public static final BitSet FOLLOW_MODULE_in_module349 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_modulename_in_module351 = new BitSet(new long[]{0x0000000000000000L,0x0000038000000000L});
    public static final BitSet FOLLOW_imports_in_module356 = new BitSet(new long[]{0x0000000000000000L,0x0000038000000000L});
    public static final BitSet FOLLOW_section_in_module360 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_modulename419 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_DOT_in_modulename421 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_ID_in_modulename431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORTS_in_imports459 = new BitSet(new long[]{0x0000000000000002L,0x0000002000000000L});
    public static final BitSet FOLLOW_importedModuleName_in_imports462 = new BitSet(new long[]{0x0000000000000002L,0x0000002000000000L});
    public static final BitSet FOLLOW_ID_in_importedModuleName491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_section517 = new BitSet(new long[]{0x0000000000000000L,0x0000038000000000L});
    public static final BitSet FOLLOW_adtgrammar_in_section521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_syntax_in_adtgrammar545 = new BitSet(new long[]{0x0000000000000002L,0x0000038000000000L});
    public static final BitSet FOLLOW_ABSTRACT_in_syntax565 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
    public static final BitSet FOLLOW_SYNTAX_in_syntax567 = new BitSet(new long[]{0x0000000000000002L,0x6000103000000000L});
    public static final BitSet FOLLOW_production_in_syntax572 = new BitSet(new long[]{0x0000000000000002L,0x6000103000000000L});
    public static final BitSet FOLLOW_hookConstruct_in_syntax578 = new BitSet(new long[]{0x0000000000000002L,0x6000103000000000L});
    public static final BitSet FOLLOW_typedecl_in_syntax584 = new BitSet(new long[]{0x0000000000000002L,0x6000103000000000L});
    public static final BitSet FOLLOW_atomdecl_in_syntax590 = new BitSet(new long[]{0x0000000000000002L,0x6000103000000000L});
    public static final BitSet FOLLOW_ID_in_production653 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_fieldlist_in_production655 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_ARROW_in_production657 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_type_in_production659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATOM_in_atomdecl692 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_ID_in_atomdecl696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl722 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl724 = new BitSet(new long[]{0x0000000000000000L,0x0001802000000000L});
    public static final BitSet FOLLOW_alternatives_in_typedecl728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl768 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_BINDS_in_typedecl770 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_atoms_in_typedecl774 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl776 = new BitSet(new long[]{0x0000000000000000L,0x0001002000000000L});
    public static final BitSet FOLLOW_pattern_alternatives_in_typedecl780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atoms827 = new BitSet(new long[]{0x0000000000000002L,0x0000002000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives859 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives861 = new BitSet(new long[]{0x0000000000000000L,0x0001802000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives867 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives871 = new BitSet(new long[]{0x0000000000000000L,0x0001802000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives878 = new BitSet(new long[]{0x0000000000000000L,0x0001802000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives883 = new BitSet(new long[]{0x0000000000000000L,0x0001802000000000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives891 = new BitSet(new long[]{0x0000000000000002L,0x0003800000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives906 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives908 = new BitSet(new long[]{0x0000000000000000L,0x0001802000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives914 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives918 = new BitSet(new long[]{0x0000000000000000L,0x0001802000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives923 = new BitSet(new long[]{0x0000000000000000L,0x0001802000000000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives932 = new BitSet(new long[]{0x0000000000000002L,0x0003800000000000L});
    public static final BitSet FOLLOW_SEMI_in_alternatives942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALT_in_pattern_alternatives974 = new BitSet(new long[]{0x0000000000000000L,0x0001002000000000L});
    public static final BitSet FOLLOW_pattern_opdecl_in_pattern_alternatives978 = new BitSet(new long[]{0x0000000000000002L,0x0003000000000000L});
    public static final BitSet FOLLOW_ALT_in_pattern_alternatives982 = new BitSet(new long[]{0x0000000000000000L,0x0001002000000000L});
    public static final BitSet FOLLOW_pattern_opdecl_in_pattern_alternatives984 = new BitSet(new long[]{0x0000000000000002L,0x0003000000000000L});
    public static final BitSet FOLLOW_SEMI_in_pattern_alternatives990 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_opdecl1018 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_fieldlist_in_opdecl1020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_opdecl1120 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_pattern_fieldlist_in_pattern_opdecl1122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_fieldlist1171 = new BitSet(new long[]{0x0000000000000000L,0x0050002000000000L});
    public static final BitSet FOLLOW_field_in_fieldlist1174 = new BitSet(new long[]{0x0000000000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_COMMA_in_fieldlist1177 = new BitSet(new long[]{0x0000000000000000L,0x0040002000000000L});
    public static final BitSet FOLLOW_field_in_fieldlist1179 = new BitSet(new long[]{0x0000000000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_fieldlist1186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_pattern_fieldlist1210 = new BitSet(new long[]{0x0000000000000000L,0x0E10002000000000L});
    public static final BitSet FOLLOW_pattern_field_in_pattern_fieldlist1213 = new BitSet(new long[]{0x0000000000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_COMMA_in_pattern_fieldlist1216 = new BitSet(new long[]{0x0000000000000000L,0x0E00002000000000L});
    public static final BitSet FOLLOW_pattern_field_in_pattern_fieldlist1218 = new BitSet(new long[]{0x0000000000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_pattern_fieldlist1225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_type1246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_type1272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_field1298 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_STAR_in_field1300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LDIPLE_in_field1318 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_pattern_type_in_field1320 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_RDIPLE_in_field1322 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_STAR_in_field1324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field1342 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_COLON_in_field1344 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_type_in_field1346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field1366 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_COLON_in_field1368 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_LDIPLE_in_field1370 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_pattern_type_in_field1372 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_RDIPLE_in_field1374 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_type_in_pattern_field1404 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_STAR_in_pattern_field1406 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INNER_in_pattern_field1424 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1426 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1428 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OUTER_in_pattern_field1450 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1452 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1454 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEUTRAL_in_pattern_field1476 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1478 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1480 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_field1502 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1504 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_pattern_type_in_pattern_field1506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arglist1533 = new BitSet(new long[]{0x0000000000000000L,0x0010002000000000L});
    public static final BitSet FOLLOW_arg_in_arglist1536 = new BitSet(new long[]{0x0000000000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_COMMA_in_arglist1539 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_arg_in_arglist1541 = new BitSet(new long[]{0x0000000000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_arglist1548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_arg1575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_hookScope_in_hookConstruct1596 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct1602 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_COLON_in_hookConstruct1604 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct1608 = new BitSet(new long[]{0x0000000000000000L,0x1004000000000000L});
    public static final BitSet FOLLOW_arglist_in_hookConstruct1610 = new BitSet(new long[]{0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_hookConstruct1612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SORT_in_hookScope1725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MODULE_in_hookScope1737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATOR_in_hookScope1749 = new BitSet(new long[]{0x0000000000000002L});

}