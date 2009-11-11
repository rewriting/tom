// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g 2009-11-11 11:08:13

package tom.gom.parser;
import tom.gom.GomStreamManager;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class GomLanguageParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ConcHook", "ConcClassName", "ConcGomClass", "ClassName", "TomMapping", "VariadicOperatorClass", "OperatorClass", "SortClass", "AbstractTypeClass", "ConcSlotField", "SlotField", "MappingHook", "ImportHook", "InterfaceHook", "BlockHook", "MakeBeforeHook", "MakeHook", "CodeList", "Compare", "FullOperatorClass", "ShortSortClass", "FullSortClass", "Cons", "Empty", "IsCons", "IsEmpty", "Code", "HookKind", "Details", "HasTomCode", "OptionList", "Origin", "Import", "GomType", "ConcModule", "ModuleDecl", "AtomDecl", "SortType", "Hook", "Production", "NamedField", "StarredField", "GomModuleName", "Arg", "FutureCons", "FutureNil", "ConcSort", "ConcSlot", "Sorts", "Grammar", "ConcField", "ConcModuleDecl", "Module", "MappingHookDecl", "ImportHookDecl", "InterfaceHookDecl", "BlockHookDecl", "MakeHookDecl", "BuiltinSortDecl", "SortDecl", "Sort", "ConcImportedModule", "Refresh", "Neutral", "Outer", "Inner", "None", "CutFutureOperator", "CutOperator", "CutSort", "CutModule", "ConcProduction", "AtomType", "ExpressionType", "PatternType", "ConcGomType", "Public", "Imports", "ModHookPair", "GomModule", "OperatorDecl", "ConcSortDecl", "ConcHookDecl", "ConcOperator", "ConcSection", "Variadic", "Slots", "ConcGrammar", "ConcAtom", "ConcGomModule", "KindFutureOperator", "KindOperator", "KindSort", "KindModule", "ConcArg", "Slot", "MODULE", "ID", "DOT", "IMPORTS", "PUBLIC", "ABSTRACT", "SYNTAX", "ARROW", "ATOM", "EQUALS", "BINDS", "JAVADOC", "ALT", "SEMI", "LPAREN", "COMMA", "RPAREN", "STAR", "LDIPLE", "RDIPLE", "COLON", "INNER", "OUTER", "NEUTRAL", "LBRACE", "SORT", "OPERATOR", "PRIVATE", "RBRACE", "WS", "SLCOMMENT", "MLCOMMENT"
    };
    public static final int COMMA=121;
    public static final int GomType=43;
    public static final int Sort=70;
    public static final int CutSort=79;
    public static final int ImportHook=22;
    public static final int BINDS=116;
    public static final int Slots=96;
    public static final int ABSTRACT=111;
    public static final int KindSort=102;
    public static final int CutFutureOperator=77;
    public static final int ShortSortClass=30;
    public static final int InterfaceHookDecl=65;
    public static final int KindModule=103;
    public static final int ConcModuleDecl=61;
    public static final int ConcField=60;
    public static final int LDIPLE=124;
    public static final int DOT=108;
    public static final int PRIVATE=133;
    public static final int FullOperatorClass=29;
    public static final int SLCOMMENT=136;
    public static final int Compare=28;
    public static final int HookKind=37;
    public static final int AtomType=82;
    public static final int Slot=105;
    public static final int SlotField=20;
    public static final int CutOperator=78;
    public static final int ConcGrammar=97;
    public static final int ConcSlot=57;
    public static final int MODULE=106;
    public static final int ConcClassName=11;
    public static final int RPAREN=122;
    public static final int PatternType=84;
    public static final int MappingHook=21;
    public static final int BlockHookDecl=66;
    public static final int ConcProduction=81;
    public static final int ATOM=114;
    public static final int NEUTRAL=129;
    public static final int SortClass=17;
    public static final int ConcGomClass=12;
    public static final int MappingHookDecl=63;
    public static final int ConcImportedModule=71;
    public static final int Production=49;
    public static final int ConcArg=104;
    public static final int IsEmpty=35;
    public static final int None=76;
    public static final int Import=42;
    public static final int ConcHookDecl=92;
    public static final int FutureNil=55;
    public static final int InterfaceHook=23;
    public static final int WS=135;
    public static final int Outer=74;
    public static final int Details=38;
    public static final int Imports=87;
    public static final int Code=36;
    public static final int TomMapping=14;
    public static final int SORT=131;
    public static final int CodeList=27;
    public static final int ConcGomType=85;
    public static final int SEMI=119;
    public static final int EQUALS=115;
    public static final int AbstractTypeClass=18;
    public static final int BuiltinSortDecl=68;
    public static final int COLON=126;
    public static final int SYNTAX=112;
    public static final int VariadicOperatorClass=15;
    public static final int OptionList=40;
    public static final int ClassName=13;
    public static final int ConcSortDecl=91;
    public static final int Grammar=59;
    public static final int ExpressionType=83;
    public static final int PUBLIC=110;
    public static final int ConcSlotField=19;
    public static final int ImportHookDecl=64;
    public static final int ARROW=113;
    public static final int FullSortClass=31;
    public static final int MakeBeforeHook=25;
    public static final int OperatorClass=16;
    public static final int SortType=47;
    public static final int INNER=127;
    public static final int JAVADOC=117;
    public static final int Origin=41;
    public static final int ConcOperator=93;
    public static final int ConcHook=10;
    public static final int Arg=53;
    public static final int ModHookPair=88;
    public static final int ConcModule=44;
    public static final int ConcGomModule=99;
    public static final int LBRACE=130;
    public static final int RBRACE=134;
    public static final int Module=62;
    public static final int MLCOMMENT=137;
    public static final int ALT=118;
    public static final int ModuleDecl=45;
    public static final int Variadic=95;
    public static final int Cons=32;
    public static final int KindFutureOperator=100;
    public static final int LPAREN=120;
    public static final int RDIPLE=125;
    public static final int IMPORTS=109;
    public static final int OPERATOR=132;
    public static final int OUTER=128;
    public static final int HasTomCode=39;
    public static final int ID=107;
    public static final int ConcSort=56;
    public static final int FutureCons=54;
    public static final int IsCons=34;
    public static final int SortDecl=69;
    public static final int AtomDecl=46;
    public static final int Hook=48;
    public static final int Public=86;
    public static final int ConcAtom=98;
    public static final int MakeHookDecl=67;
    public static final int NamedField=50;
    public static final int CutModule=80;
    public static final int MakeHook=26;
    public static final int GomModule=89;
    public static final int Inner=75;
    public static final int EOF=-1;
    public static final int Empty=33;
    public static final int GomModuleName=52;
    public static final int StarredField=51;
    public static final int BlockHook=24;
    public static final int KindOperator=101;
    public static final int STAR=123;
    public static final int ConcSection=94;
    public static final int Sorts=58;
    public static final int Neutral=73;
    public static final int OperatorDecl=90;
    public static final int Refresh=72;

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
    public String getGrammarFileName() { return "/Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g"; }


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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:49:1: module : MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) ;
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
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:49:8: ( MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:50:2: MODULE modulename (imps= imports )? section EOF
            {
            MODULE1=(Token)match(input,MODULE,FOLLOW_MODULE_in_module60);  
            stream_MODULE.add(MODULE1);

            pushFollow(FOLLOW_modulename_in_module62);
            modulename2=modulename();

            state._fsp--;

            stream_modulename.add(modulename2.getTree());
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:50:20: (imps= imports )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==IMPORTS) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:50:21: imps= imports
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
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:51:20: ^( GomModule modulename ^( ConcSection imports section ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:51:43: ^( ConcSection imports section )
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
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:52:6: ^( GomModule modulename ^( ConcSection section ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:52:29: ^( ConcSection section )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:55:1: modulename : (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) ;
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
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:58:3: ( (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:59:3: (mod= ID DOT )* moduleName= ID
            {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:59:3: (mod= ID DOT )*
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
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:59:4: mod= ID DOT
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
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:69:6: ^( GomModuleName $moduleName)
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:72:1: imports : IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) ;
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
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:72:9: ( IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:73:3: IMPORTS ( importedModuleName )*
            {
            IMPORTS6=(Token)match(input,IMPORTS,FOLLOW_IMPORTS_in_imports170);  
            stream_IMPORTS.add(IMPORTS6);

            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:73:11: ( importedModuleName )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:73:12: importedModuleName
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
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:73:36: ^( Imports ^( ConcImportedModule ( importedModuleName )* ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Imports, "Imports"), root_1);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:73:46: ^( ConcImportedModule ( importedModuleName )* )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcImportedModule, "ConcImportedModule"), root_2);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:73:67: ( importedModuleName )*
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:75:1: importedModuleName : ID -> ^( Import ^( GomModuleName ID ) ) ;
    public final GomLanguageParser.importedModuleName_return importedModuleName() throws RecognitionException {
        GomLanguageParser.importedModuleName_return retval = new GomLanguageParser.importedModuleName_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID8=null;

        Tree ID8_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:75:20: ( ID -> ^( Import ^( GomModuleName ID ) ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:76:3: ID
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
            // 76:6: -> ^( Import ^( GomModuleName ID ) )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:76:9: ^( Import ^( GomModuleName ID ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Import, "Import"), root_1);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:76:18: ^( GomModuleName ID )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:79:1: section : ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) ;
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
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:79:9: ( ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:80:3: ( PUBLIC )? adtgrammar
            {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:80:3: ( PUBLIC )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==PUBLIC) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:80:4: PUBLIC
                    {
                    PUBLIC9=(Token)match(input,PUBLIC,FOLLOW_PUBLIC_in_section228);  
                    stream_PUBLIC.add(PUBLIC9);


                    }
                    break;

            }

            pushFollow(FOLLOW_adtgrammar_in_section232);
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
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:80:27: ^( Public adtgrammar )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:83:1: adtgrammar : (gr+= syntax )+ -> $gr;
    public final GomLanguageParser.adtgrammar_return adtgrammar() throws RecognitionException {
        GomLanguageParser.adtgrammar_return retval = new GomLanguageParser.adtgrammar_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        List list_gr=null;
        GomLanguageParser.syntax_return gr = null;
         gr = null;
        RewriteRuleSubtreeStream stream_syntax=new RewriteRuleSubtreeStream(adaptor,"rule syntax");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:83:12: ( (gr+= syntax )+ -> $gr)
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:84:3: (gr+= syntax )+
            {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:84:3: (gr+= syntax )+
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
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:84:4: gr+= syntax
            	    {
            	    pushFollow(FOLLOW_syntax_in_adtgrammar256);
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:87:1: syntax : ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) ) ;
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
        RewriteRuleTokenStream stream_ABSTRACT=new RewriteRuleTokenStream(adaptor,"token ABSTRACT");
        RewriteRuleTokenStream stream_SYNTAX=new RewriteRuleTokenStream(adaptor,"token SYNTAX");
        RewriteRuleSubtreeStream stream_atomdecl=new RewriteRuleSubtreeStream(adaptor,"rule atomdecl");
        RewriteRuleSubtreeStream stream_hookConstruct=new RewriteRuleSubtreeStream(adaptor,"rule hookConstruct");
        RewriteRuleSubtreeStream stream_typedecl=new RewriteRuleSubtreeStream(adaptor,"rule typedecl");
        RewriteRuleSubtreeStream stream_production=new RewriteRuleSubtreeStream(adaptor,"rule production");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:87:8: ( ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:88:3: ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )*
            {
            ABSTRACT11=(Token)match(input,ABSTRACT,FOLLOW_ABSTRACT_in_syntax276);  
            stream_ABSTRACT.add(ABSTRACT11);

            SYNTAX12=(Token)match(input,SYNTAX,FOLLOW_SYNTAX_in_syntax278);  
            stream_SYNTAX.add(SYNTAX12);

            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:88:19: (gr1+= production | gr2+= hookConstruct | gr3+= typedecl | gr4+= atomdecl )*
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
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:88:20: gr1+= production
            	    {
            	    pushFollow(FOLLOW_production_in_syntax283);
            	    gr1=production();

            	    state._fsp--;

            	    stream_production.add(gr1.getTree());
            	    if (list_gr1==null) list_gr1=new ArrayList();
            	    list_gr1.add(gr1.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:88:38: gr2+= hookConstruct
            	    {
            	    pushFollow(FOLLOW_hookConstruct_in_syntax289);
            	    gr2=hookConstruct();

            	    state._fsp--;

            	    stream_hookConstruct.add(gr2.getTree());
            	    if (list_gr2==null) list_gr2=new ArrayList();
            	    list_gr2.add(gr2.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:88:59: gr3+= typedecl
            	    {
            	    pushFollow(FOLLOW_typedecl_in_syntax295);
            	    gr3=typedecl();

            	    state._fsp--;

            	    stream_typedecl.add(gr3.getTree());
            	    if (list_gr3==null) list_gr3=new ArrayList();
            	    list_gr3.add(gr3.getTree());


            	    }
            	    break;
            	case 4 :
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:88:75: gr4+= atomdecl
            	    {
            	    pushFollow(FOLLOW_atomdecl_in_syntax301);
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
            // elements: gr4, gr3, gr2, gr1
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: gr3, gr4, gr1, gr2
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_gr3=new RewriteRuleSubtreeStream(adaptor,"token gr3",list_gr3);
            RewriteRuleSubtreeStream stream_gr4=new RewriteRuleSubtreeStream(adaptor,"token gr4",list_gr4);
            RewriteRuleSubtreeStream stream_gr1=new RewriteRuleSubtreeStream(adaptor,"token gr1",list_gr1);
            RewriteRuleSubtreeStream stream_gr2=new RewriteRuleSubtreeStream(adaptor,"token gr2",list_gr2);
            root_0 = (Tree)adaptor.nil();
            // 89:5: -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:89:8: ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcGrammar, "ConcGrammar"), root_1);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:89:22: ^( Grammar ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Grammar, "Grammar"), root_2);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:89:32: ^( ConcProduction ( $gr4)* ( $gr1)* ( $gr2)* ( $gr3)* )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcProduction, "ConcProduction"), root_3);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:89:49: ( $gr4)*
                while ( stream_gr4.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr4.nextTree());

                }
                stream_gr4.reset();
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:89:57: ( $gr1)*
                while ( stream_gr1.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr1.nextTree());

                }
                stream_gr1.reset();
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:89:65: ( $gr2)*
                while ( stream_gr2.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr2.nextTree());

                }
                stream_gr2.reset();
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:89:73: ( $gr3)*
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:92:1: production : ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) ;
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
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:95:3: ( ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:96:3: ID fieldlist ARROW type
            {
            ID13=(Token)match(input,ID,FOLLOW_ID_in_production363);  
            stream_ID.add(ID13);

            pushFollow(FOLLOW_fieldlist_in_production365);
            fieldlist14=fieldlist();

            state._fsp--;

            stream_fieldlist.add(fieldlist14.getTree());
            ARROW15=(Token)match(input,ARROW,FOLLOW_ARROW_in_production367);  
            stream_ARROW.add(ARROW15);

            pushFollow(FOLLOW_type_in_production369);
            type16=type();

            state._fsp--;

            stream_type.add(type16.getTree());


            // AST REWRITE
            // elements: ID, fieldlist, type, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 96:27: -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:96:30: ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                adaptor.addChild(root_1, stream_type.nextTree());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:96:61: ^( Origin ID[startLine] )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:99:1: atomdecl : ATOM atom= ID -> ^( AtomDecl ID[atom] ) ;
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
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:99:10: ( ATOM atom= ID -> ^( AtomDecl ID[atom] ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:100:3: ATOM atom= ID
            {
            ATOM17=(Token)match(input,ATOM,FOLLOW_ATOM_in_atomdecl401);  
            stream_ATOM.add(ATOM17);

            atom=(Token)match(input,ID,FOLLOW_ID_in_atomdecl405);  
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
            // 100:16: -> ^( AtomDecl ID[atom] )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:100:19: ^( AtomDecl ID[atom] )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:103:1: typedecl : (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts) | ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename] -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts) );
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
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:103:10: (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts) | ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename] -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts) )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:104:5: typename= ID EQUALS alts= alternatives[typename]
                    {
                    typename=(Token)match(input,ID,FOLLOW_ID_in_typedecl431);  
                    stream_ID.add(typename);

                    EQUALS18=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl433);  
                    stream_EQUALS.add(EQUALS18);

                    pushFollow(FOLLOW_alternatives_in_typedecl437);
                    alts=alternatives(typename);

                    state._fsp--;

                    stream_alternatives.add(alts.getTree());


                    // AST REWRITE
                    // elements: alts, typename
                    // token labels: typename
                    // rule labels: alts, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_typename=new RewriteRuleTokenStream(adaptor,"token typename",typename);
                    RewriteRuleSubtreeStream stream_alts=new RewriteRuleSubtreeStream(adaptor,"rule alts",alts!=null?alts.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 105:7: -> ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts)
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:105:10: ^( SortType ^( GomType ^( ExpressionType ) $typename) ^( ConcAtom ) $alts)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SortType, "SortType"), root_1);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:105:21: ^( GomType ^( ExpressionType ) $typename)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:105:31: ^( ExpressionType )
                        {
                        Tree root_3 = (Tree)adaptor.nil();
                        root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_typename.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:105:60: ^( ConcAtom )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:106:6: ptypename= ID BINDS b= atoms EQUALS palts= pattern_alternatives[ptypename]
                    {
                    ptypename=(Token)match(input,ID,FOLLOW_ID_in_typedecl477);  
                    stream_ID.add(ptypename);

                    BINDS19=(Token)match(input,BINDS,FOLLOW_BINDS_in_typedecl479);  
                    stream_BINDS.add(BINDS19);

                    pushFollow(FOLLOW_atoms_in_typedecl483);
                    b=atoms();

                    state._fsp--;

                    stream_atoms.add(b.getTree());
                    EQUALS20=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl485);  
                    stream_EQUALS.add(EQUALS20);

                    pushFollow(FOLLOW_pattern_alternatives_in_typedecl489);
                    palts=pattern_alternatives(ptypename);

                    state._fsp--;

                    stream_pattern_alternatives.add(palts.getTree());


                    // AST REWRITE
                    // elements: b, palts, ptypename
                    // token labels: ptypename
                    // rule labels: retval, palts, b
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_ptypename=new RewriteRuleTokenStream(adaptor,"token ptypename",ptypename);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_palts=new RewriteRuleSubtreeStream(adaptor,"rule palts",palts!=null?palts.tree:null);
                    RewriteRuleSubtreeStream stream_b=new RewriteRuleSubtreeStream(adaptor,"rule b",b!=null?b.tree:null);

                    root_0 = (Tree)adaptor.nil();
                    // 107:7: -> ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts)
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:107:10: ^( SortType ^( GomType ^( PatternType ) $ptypename) $b $palts)
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(SortType, "SortType"), root_1);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:107:21: ^( GomType ^( PatternType ) $ptypename)
                        {
                        Tree root_2 = (Tree)adaptor.nil();
                        root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:107:31: ^( PatternType )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:110:1: atoms : (atom+= ID )+ -> ^( ConcAtom ( $atom)+ ) ;
    public final GomLanguageParser.atoms_return atoms() throws RecognitionException {
        GomLanguageParser.atoms_return retval = new GomLanguageParser.atoms_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token atom=null;
        List list_atom=null;

        Tree atom_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:110:7: ( (atom+= ID )+ -> ^( ConcAtom ( $atom)+ ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:111:3: (atom+= ID )+
            {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:111:3: (atom+= ID )+
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
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:111:4: atom+= ID
            	    {
            	    atom=(Token)match(input,ID,FOLLOW_ID_in_atoms535);  
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
            // 111:15: -> ^( ConcAtom ( $atom)+ )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:111:18: ^( ConcAtom ( $atom)+ )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:114:1: alternatives[Token typename] : ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? ) opdecl[typename,jd1] ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) ;
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
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleTokenStream stream_JAVADOC=new RewriteRuleTokenStream(adaptor,"token JAVADOC");
        RewriteRuleSubtreeStream stream_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule opdecl");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:114:30: ( ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? ) opdecl[typename,jd1] ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:3: ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? ) opdecl[typename,jd1] ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )* ( SEMI )?
            {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:3: ( (jd1= JAVADOC ALT ) | ( ALT jd1= JAVADOC ) | jd1= JAVADOC | ( ALT )? )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:4: (jd1= JAVADOC ALT )
                    {
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:4: (jd1= JAVADOC ALT )
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:5: jd1= JAVADOC ALT
                    {
                    jd1=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives567);  
                    stream_JAVADOC.add(jd1);

                    ALT21=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives569);  
                    stream_ALT.add(ALT21);


                    }


                    }
                    break;
                case 2 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:24: ( ALT jd1= JAVADOC )
                    {
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:24: ( ALT jd1= JAVADOC )
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:25: ALT jd1= JAVADOC
                    {
                    ALT22=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives575);  
                    stream_ALT.add(ALT22);

                    jd1=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives579);  
                    stream_JAVADOC.add(jd1);


                    }


                    }
                    break;
                case 3 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:44: jd1= JAVADOC
                    {
                    jd1=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives586);  
                    stream_JAVADOC.add(jd1);


                    }
                    break;
                case 4 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:58: ( ALT )?
                    {
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:58: ( ALT )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==ALT) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:115:59: ALT
                            {
                            ALT23=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives591);  
                            stream_ALT.add(ALT23);


                            }
                            break;

                    }


                    }
                    break;

            }

            pushFollow(FOLLOW_opdecl_in_alternatives598);
            opdecl24=opdecl(typename, jd1);

            state._fsp--;

            stream_opdecl.add(opdecl24.getTree());
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:117:3: ( ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2] )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=JAVADOC && LA12_0<=ALT)) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:118:4: ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT ) opdecl[typename,jd2]
            	    {
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:118:4: ( (jd2= JAVADOC ALT ) | ( ALT jd2= JAVADOC ) | ALT )
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
            	            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:118:5: (jd2= JAVADOC ALT )
            	            {
            	            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:118:5: (jd2= JAVADOC ALT )
            	            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:118:6: jd2= JAVADOC ALT
            	            {
            	            jd2=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives612);  
            	            stream_JAVADOC.add(jd2);

            	            ALT25=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives614);  
            	            stream_ALT.add(ALT25);


            	            }


            	            }
            	            break;
            	        case 2 :
            	            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:118:25: ( ALT jd2= JAVADOC )
            	            {
            	            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:118:25: ( ALT jd2= JAVADOC )
            	            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:118:26: ALT jd2= JAVADOC
            	            {
            	            ALT26=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives620);  
            	            stream_ALT.add(ALT26);

            	            jd2=(Token)match(input,JAVADOC,FOLLOW_JAVADOC_in_alternatives624);  
            	            stream_JAVADOC.add(jd2);


            	            }


            	            }
            	            break;
            	        case 3 :
            	            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:118:45: ALT
            	            {
            	            ALT27=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives629);  
            	            stream_ALT.add(ALT27);

            	            jd2=null;

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_opdecl_in_alternatives637);
            	    opdecl28=opdecl(typename, jd2);

            	    state._fsp--;

            	    stream_opdecl.add(opdecl28.getTree());

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:120:6: ( SEMI )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==SEMI) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:120:7: SEMI
                    {
                    SEMI29=(Token)match(input,SEMI,FOLLOW_SEMI_in_alternatives646);  
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
            // 121:3: -> ^( ConcProduction ( opdecl )+ )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:121:6: ^( ConcProduction ( opdecl )+ )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:125:1: pattern_alternatives[Token typename] : ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( pattern_opdecl )+ ) ;
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
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleSubtreeStream stream_pattern_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule pattern_opdecl");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:125:38: ( ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( pattern_opdecl )+ ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:126:3: ( ALT )? pattern_opdecl[typename] ( ALT pattern_opdecl[typename] )* ( SEMI )?
            {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:126:3: ( ALT )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==ALT) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:126:4: ALT
                    {
                    ALT30=(Token)match(input,ALT,FOLLOW_ALT_in_pattern_alternatives678);  
                    stream_ALT.add(ALT30);


                    }
                    break;

            }

            pushFollow(FOLLOW_pattern_opdecl_in_pattern_alternatives682);
            pattern_opdecl31=pattern_opdecl(typename);

            state._fsp--;

            stream_pattern_opdecl.add(pattern_opdecl31.getTree());
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:126:35: ( ALT pattern_opdecl[typename] )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==ALT) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:126:36: ALT pattern_opdecl[typename]
            	    {
            	    ALT32=(Token)match(input,ALT,FOLLOW_ALT_in_pattern_alternatives686);  
            	    stream_ALT.add(ALT32);

            	    pushFollow(FOLLOW_pattern_opdecl_in_pattern_alternatives688);
            	    pattern_opdecl33=pattern_opdecl(typename);

            	    state._fsp--;

            	    stream_pattern_opdecl.add(pattern_opdecl33.getTree());

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:126:67: ( SEMI )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==SEMI) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:126:68: SEMI
                    {
                    SEMI34=(Token)match(input,SEMI,FOLLOW_SEMI_in_pattern_alternatives694);  
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
            // 127:3: -> ^( ConcProduction ( pattern_opdecl )+ )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:127:6: ^( ConcProduction ( pattern_opdecl )+ )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:130:1: opdecl[Token type, Token JAVADOC] : ID fieldlist -> {JAVADOC!=null}? ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) ) -> ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
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
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:130:35: ( ID fieldlist -> {JAVADOC!=null}? ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) ) -> ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:131:2: ID fieldlist
            {
            ID35=(Token)match(input,ID,FOLLOW_ID_in_opdecl722);  
            stream_ID.add(ID35);

            pushFollow(FOLLOW_fieldlist_in_opdecl724);
            fieldlist36=fieldlist();

            state._fsp--;

            stream_fieldlist.add(fieldlist36.getTree());


            // AST REWRITE
            // elements: ID, fieldlist, ID, ID, ID, fieldlist, ID, ID, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 132:3: -> {JAVADOC!=null}? ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) )
            if (JAVADOC!=null) {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:132:23: ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:132:49: ^( GomType ^( ExpressionType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:132:59: ^( ExpressionType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:133:7: ^( OptionList ^( Origin ID[\"\"+input.LT(1).getLine()] ) ^( Details ID[JAVADOC] ) )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(OptionList, "OptionList"), root_2);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:133:20: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_3);

                adaptor.addChild(root_3, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_2, root_3);
                }
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:133:59: ^( Details ID[JAVADOC] )
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
            else // 134:3: -> ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:134:6: ^( Production ID fieldlist ^( GomType ^( ExpressionType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:134:32: ^( GomType ^( ExpressionType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:134:42: ^( ExpressionType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ExpressionType, "ExpressionType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:135:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:139:1: pattern_opdecl[Token type] : ID pattern_fieldlist -> ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
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
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:139:28: ( ID pattern_fieldlist -> ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:140:2: ID pattern_fieldlist
            {
            ID37=(Token)match(input,ID,FOLLOW_ID_in_pattern_opdecl824);  
            stream_ID.add(ID37);

            pushFollow(FOLLOW_pattern_fieldlist_in_pattern_opdecl826);
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
            // 141:3: -> ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:141:6: ^( Production ID pattern_fieldlist ^( GomType ^( PatternType ) ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_pattern_fieldlist.nextTree());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:141:40: ^( GomType ^( PatternType ) ID[type] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_2);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:141:50: ^( PatternType )
                {
                Tree root_3 = (Tree)adaptor.nil();
                root_3 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(PatternType, "PatternType"), root_3);

                adaptor.addChild(root_2, root_3);
                }
                adaptor.addChild(root_2, (Tree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:142:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:145:1: fieldlist : LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) ;
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
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:145:11: ( LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:146:3: LPAREN ( field ( COMMA field )* )? RPAREN
            {
            LPAREN39=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_fieldlist875);  
            stream_LPAREN.add(LPAREN39);

            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:146:10: ( field ( COMMA field )* )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==ID||LA18_0==LDIPLE) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:146:11: field ( COMMA field )*
                    {
                    pushFollow(FOLLOW_field_in_fieldlist878);
                    field40=field();

                    state._fsp--;

                    stream_field.add(field40.getTree());
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:146:17: ( COMMA field )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0==COMMA) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:146:18: COMMA field
                    	    {
                    	    COMMA41=(Token)match(input,COMMA,FOLLOW_COMMA_in_fieldlist881);  
                    	    stream_COMMA.add(COMMA41);

                    	    pushFollow(FOLLOW_field_in_fieldlist883);
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

            RPAREN43=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_fieldlist890);  
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
            // 146:42: -> ^( ConcField ( field )* )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:146:45: ^( ConcField ( field )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:146:57: ( field )*
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:149:1: pattern_fieldlist : LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN -> ^( ConcField ( pattern_field )* ) ;
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
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_pattern_field=new RewriteRuleSubtreeStream(adaptor,"rule pattern_field");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:149:19: ( LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN -> ^( ConcField ( pattern_field )* ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:150:3: LPAREN ( pattern_field ( COMMA pattern_field )* )? RPAREN
            {
            LPAREN44=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_pattern_fieldlist914);  
            stream_LPAREN.add(LPAREN44);

            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:150:10: ( pattern_field ( COMMA pattern_field )* )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==ID||(LA20_0>=INNER && LA20_0<=NEUTRAL)) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:150:11: pattern_field ( COMMA pattern_field )*
                    {
                    pushFollow(FOLLOW_pattern_field_in_pattern_fieldlist917);
                    pattern_field45=pattern_field();

                    state._fsp--;

                    stream_pattern_field.add(pattern_field45.getTree());
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:150:25: ( COMMA pattern_field )*
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( (LA19_0==COMMA) ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:150:26: COMMA pattern_field
                    	    {
                    	    COMMA46=(Token)match(input,COMMA,FOLLOW_COMMA_in_pattern_fieldlist920);  
                    	    stream_COMMA.add(COMMA46);

                    	    pushFollow(FOLLOW_pattern_field_in_pattern_fieldlist922);
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

            RPAREN48=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_pattern_fieldlist929);  
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
            // 150:58: -> ^( ConcField ( pattern_field )* )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:150:61: ^( ConcField ( pattern_field )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:150:73: ( pattern_field )*
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:152:1: type : ID -> ^( GomType ^( ExpressionType ) ID ) ;
    public final GomLanguageParser.type_return type() throws RecognitionException {
        GomLanguageParser.type_return retval = new GomLanguageParser.type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID49=null;

        Tree ID49_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:152:5: ( ID -> ^( GomType ^( ExpressionType ) ID ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:153:3: ID
            {
            ID49=(Token)match(input,ID,FOLLOW_ID_in_type950);  
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
            // 153:6: -> ^( GomType ^( ExpressionType ) ID )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:153:9: ^( GomType ^( ExpressionType ) ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_1);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:153:19: ^( ExpressionType )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:157:1: pattern_type : ID -> ^( GomType ^( PatternType ) ID ) ;
    public final GomLanguageParser.pattern_type_return pattern_type() throws RecognitionException {
        GomLanguageParser.pattern_type_return retval = new GomLanguageParser.pattern_type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID50=null;

        Tree ID50_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:157:13: ( ID -> ^( GomType ^( PatternType ) ID ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:158:3: ID
            {
            ID50=(Token)match(input,ID,FOLLOW_ID_in_pattern_type976);  
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
            // 158:6: -> ^( GomType ^( PatternType ) ID )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:158:9: ^( GomType ^( PatternType ) ID )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(GomType, "GomType"), root_1);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:158:19: ^( PatternType )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:161:1: field : ( type STAR -> ^( StarredField type ^( None ) ) | LDIPLE pattern_type RDIPLE STAR -> ^( StarredField pattern_type ^( Refresh ) ) | ID COLON type -> ^( NamedField ^( None ) ID type ) | ID COLON LDIPLE pattern_type RDIPLE -> ^( NamedField ^( Refresh ) ID pattern_type ) );
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
        RewriteRuleTokenStream stream_RDIPLE=new RewriteRuleTokenStream(adaptor,"token RDIPLE");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_LDIPLE=new RewriteRuleTokenStream(adaptor,"token LDIPLE");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_pattern_type=new RewriteRuleSubtreeStream(adaptor,"rule pattern_type");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:161:6: ( type STAR -> ^( StarredField type ^( None ) ) | LDIPLE pattern_type RDIPLE STAR -> ^( StarredField pattern_type ^( Refresh ) ) | ID COLON type -> ^( NamedField ^( None ) ID type ) | ID COLON LDIPLE pattern_type RDIPLE -> ^( NamedField ^( Refresh ) ID pattern_type ) )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:162:5: type STAR
                    {
                    pushFollow(FOLLOW_type_in_field1002);
                    type51=type();

                    state._fsp--;

                    stream_type.add(type51.getTree());
                    STAR52=(Token)match(input,STAR,FOLLOW_STAR_in_field1004);  
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
                    // 162:15: -> ^( StarredField type ^( None ) )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:162:18: ^( StarredField type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_type.nextTree());
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:162:38: ^( None )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:163:5: LDIPLE pattern_type RDIPLE STAR
                    {
                    LDIPLE53=(Token)match(input,LDIPLE,FOLLOW_LDIPLE_in_field1022);  
                    stream_LDIPLE.add(LDIPLE53);

                    pushFollow(FOLLOW_pattern_type_in_field1024);
                    pattern_type54=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type54.getTree());
                    RDIPLE55=(Token)match(input,RDIPLE,FOLLOW_RDIPLE_in_field1026);  
                    stream_RDIPLE.add(RDIPLE55);

                    STAR56=(Token)match(input,STAR,FOLLOW_STAR_in_field1028);  
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
                    // 163:37: -> ^( StarredField pattern_type ^( Refresh ) )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:163:40: ^( StarredField pattern_type ^( Refresh ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:163:68: ^( Refresh )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:164:5: ID COLON type
                    {
                    ID57=(Token)match(input,ID,FOLLOW_ID_in_field1046);  
                    stream_ID.add(ID57);

                    COLON58=(Token)match(input,COLON,FOLLOW_COLON_in_field1048);  
                    stream_COLON.add(COLON58);

                    pushFollow(FOLLOW_type_in_field1050);
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
                    // 164:19: -> ^( NamedField ^( None ) ID type )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:164:22: ^( NamedField ^( None ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:164:35: ^( None )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:165:5: ID COLON LDIPLE pattern_type RDIPLE
                    {
                    ID60=(Token)match(input,ID,FOLLOW_ID_in_field1070);  
                    stream_ID.add(ID60);

                    COLON61=(Token)match(input,COLON,FOLLOW_COLON_in_field1072);  
                    stream_COLON.add(COLON61);

                    LDIPLE62=(Token)match(input,LDIPLE,FOLLOW_LDIPLE_in_field1074);  
                    stream_LDIPLE.add(LDIPLE62);

                    pushFollow(FOLLOW_pattern_type_in_field1076);
                    pattern_type63=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type63.getTree());
                    RDIPLE64=(Token)match(input,RDIPLE,FOLLOW_RDIPLE_in_field1078);  
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
                    // 165:41: -> ^( NamedField ^( Refresh ) ID pattern_type )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:165:44: ^( NamedField ^( Refresh ) ID pattern_type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:165:57: ^( Refresh )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:169:1: pattern_field : ( pattern_type STAR -> ^( StarredField pattern_type ^( None ) ) | INNER ID COLON type -> ^( NamedField ^( Inner ) ID type ) | OUTER ID COLON type -> ^( NamedField ^( Outer ) ID type ) | NEUTRAL ID COLON type -> ^( NamedField ^( Neutral ) ID type ) | ID COLON pattern_type -> ^( NamedField ^( None ) ID pattern_type ) );
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
        RewriteRuleTokenStream stream_INNER=new RewriteRuleTokenStream(adaptor,"token INNER");
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_NEUTRAL=new RewriteRuleTokenStream(adaptor,"token NEUTRAL");
        RewriteRuleTokenStream stream_OUTER=new RewriteRuleTokenStream(adaptor,"token OUTER");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_pattern_type=new RewriteRuleSubtreeStream(adaptor,"rule pattern_type");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:169:14: ( pattern_type STAR -> ^( StarredField pattern_type ^( None ) ) | INNER ID COLON type -> ^( NamedField ^( Inner ) ID type ) | OUTER ID COLON type -> ^( NamedField ^( Outer ) ID type ) | NEUTRAL ID COLON type -> ^( NamedField ^( Neutral ) ID type ) | ID COLON pattern_type -> ^( NamedField ^( None ) ID pattern_type ) )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:170:5: pattern_type STAR
                    {
                    pushFollow(FOLLOW_pattern_type_in_pattern_field1108);
                    pattern_type65=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type65.getTree());
                    STAR66=(Token)match(input,STAR,FOLLOW_STAR_in_pattern_field1110);  
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
                    // 170:23: -> ^( StarredField pattern_type ^( None ) )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:170:26: ^( StarredField pattern_type ^( None ) )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_pattern_type.nextTree());
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:170:54: ^( None )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:171:5: INNER ID COLON type
                    {
                    INNER67=(Token)match(input,INNER,FOLLOW_INNER_in_pattern_field1128);  
                    stream_INNER.add(INNER67);

                    ID68=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1130);  
                    stream_ID.add(ID68);

                    COLON69=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1132);  
                    stream_COLON.add(COLON69);

                    pushFollow(FOLLOW_type_in_pattern_field1134);
                    type70=type();

                    state._fsp--;

                    stream_type.add(type70.getTree());


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
                    // 171:25: -> ^( NamedField ^( Inner ) ID type )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:171:28: ^( NamedField ^( Inner ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:171:41: ^( Inner )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:172:5: OUTER ID COLON type
                    {
                    OUTER71=(Token)match(input,OUTER,FOLLOW_OUTER_in_pattern_field1154);  
                    stream_OUTER.add(OUTER71);

                    ID72=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1156);  
                    stream_ID.add(ID72);

                    COLON73=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1158);  
                    stream_COLON.add(COLON73);

                    pushFollow(FOLLOW_type_in_pattern_field1160);
                    type74=type();

                    state._fsp--;

                    stream_type.add(type74.getTree());


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
                    // 172:25: -> ^( NamedField ^( Outer ) ID type )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:172:28: ^( NamedField ^( Outer ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:172:41: ^( Outer )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:173:5: NEUTRAL ID COLON type
                    {
                    NEUTRAL75=(Token)match(input,NEUTRAL,FOLLOW_NEUTRAL_in_pattern_field1180);  
                    stream_NEUTRAL.add(NEUTRAL75);

                    ID76=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1182);  
                    stream_ID.add(ID76);

                    COLON77=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1184);  
                    stream_COLON.add(COLON77);

                    pushFollow(FOLLOW_type_in_pattern_field1186);
                    type78=type();

                    state._fsp--;

                    stream_type.add(type78.getTree());


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
                    // 173:27: -> ^( NamedField ^( Neutral ) ID type )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:173:30: ^( NamedField ^( Neutral ) ID type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:173:43: ^( Neutral )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:174:5: ID COLON pattern_type
                    {
                    ID79=(Token)match(input,ID,FOLLOW_ID_in_pattern_field1206);  
                    stream_ID.add(ID79);

                    COLON80=(Token)match(input,COLON,FOLLOW_COLON_in_pattern_field1208);  
                    stream_COLON.add(COLON80);

                    pushFollow(FOLLOW_pattern_type_in_pattern_field1210);
                    pattern_type81=pattern_type();

                    state._fsp--;

                    stream_pattern_type.add(pattern_type81.getTree());


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
                    // 174:27: -> ^( NamedField ^( None ) ID pattern_type )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:174:30: ^( NamedField ^( None ) ID pattern_type )
                        {
                        Tree root_1 = (Tree)adaptor.nil();
                        root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(NamedField, "NamedField"), root_1);

                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:174:43: ^( None )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:177:1: arglist : ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) ;
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
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:177:8: ( ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:178:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:178:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==LPAREN) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:178:4: LPAREN ( arg ( COMMA arg )* )? RPAREN
                    {
                    LPAREN82=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arglist1237);  
                    stream_LPAREN.add(LPAREN82);

                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:178:11: ( arg ( COMMA arg )* )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==ID) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:178:12: arg ( COMMA arg )*
                            {
                            pushFollow(FOLLOW_arg_in_arglist1240);
                            arg83=arg();

                            state._fsp--;

                            stream_arg.add(arg83.getTree());
                            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:178:16: ( COMMA arg )*
                            loop23:
                            do {
                                int alt23=2;
                                int LA23_0 = input.LA(1);

                                if ( (LA23_0==COMMA) ) {
                                    alt23=1;
                                }


                                switch (alt23) {
                            	case 1 :
                            	    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:178:17: COMMA arg
                            	    {
                            	    COMMA84=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist1243);  
                            	    stream_COMMA.add(COMMA84);

                            	    pushFollow(FOLLOW_arg_in_arglist1245);
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

                    RPAREN86=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arglist1252);  
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
            // 179:3: -> ^( ConcArg ( arg )* )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:179:6: ^( ConcArg ( arg )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(ConcArg, "ConcArg"), root_1);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:179:16: ( arg )*
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:182:1: arg : ID -> ^( Arg ID ) ;
    public final GomLanguageParser.arg_return arg() throws RecognitionException {
        GomLanguageParser.arg_return retval = new GomLanguageParser.arg_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token ID87=null;

        Tree ID87_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:182:5: ( ID -> ^( Arg ID ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:182:7: ID
            {
            ID87=(Token)match(input,ID,FOLLOW_ID_in_arg1278);  
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
            // 182:10: -> ^( Arg ID )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:182:13: ^( Arg ID )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:184:1: hookConstruct : (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
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
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_hookScope=new RewriteRuleSubtreeStream(adaptor,"rule hookScope");
        RewriteRuleSubtreeStream stream_arglist=new RewriteRuleSubtreeStream(adaptor,"rule arglist");
        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:184:15: ( (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:185:3: (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE
            {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:185:3: (hscope= hookScope )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==MODULE||(LA26_0>=SORT && LA26_0<=OPERATOR)) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:185:4: hscope= hookScope
                    {
                    pushFollow(FOLLOW_hookScope_in_hookConstruct1299);
                    hscope=hookScope();

                    state._fsp--;

                    stream_hookScope.add(hscope.getTree());

                    }
                    break;

            }

            pointCut=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct1305);  
            stream_ID.add(pointCut);

            COLON88=(Token)match(input,COLON,FOLLOW_COLON_in_hookConstruct1307);  
            stream_COLON.add(COLON88);

            hookType=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct1311);  
            stream_ID.add(hookType);

            pushFollow(FOLLOW_arglist_in_hookConstruct1313);
            arglist89=arglist();

            state._fsp--;

            stream_arglist.add(arglist89.getTree());
            LBRACE90=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_hookConstruct1315);  
            stream_LBRACE.add(LBRACE90);



            // AST REWRITE
            // elements: ID, pointCut, arglist, LBRACE, pointCut, ID, hookType, arglist, hscope, hookType, LBRACE
            // token labels: pointCut, hookType
            // rule labels: hscope, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_pointCut=new RewriteRuleTokenStream(adaptor,"token pointCut",pointCut);
            RewriteRuleTokenStream stream_hookType=new RewriteRuleTokenStream(adaptor,"token hookType",hookType);
            RewriteRuleSubtreeStream stream_hscope=new RewriteRuleSubtreeStream(adaptor,"rule hscope",hscope!=null?hscope.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 186:3: -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            if (hscope!=null) {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:186:22: ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Hook, "Hook"), root_1);

                adaptor.addChild(root_1, stream_hscope.nextTree());
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:186:47: ^( HookKind $hookType)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:187:24: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (Tree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 188:3: -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:188:6: ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(Hook, "Hook"), root_1);

                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:188:13: ^( KindOperator )
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(KindOperator, "KindOperator"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:188:39: ^( HookKind $hookType)
                {
                Tree root_2 = (Tree)adaptor.nil();
                root_2 = (Tree)adaptor.becomeRoot((Tree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:189:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
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
    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:193:1: hookScope : ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) );
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
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");
        RewriteRuleTokenStream stream_SORT=new RewriteRuleTokenStream(adaptor,"token SORT");
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");

        try {
            // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:193:11: ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:194:3: SORT
                    {
                    SORT91=(Token)match(input,SORT,FOLLOW_SORT_in_hookScope1428);  
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
                    // 194:8: -> ^( KindSort )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:194:11: ^( KindSort )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:195:5: MODULE
                    {
                    MODULE92=(Token)match(input,MODULE,FOLLOW_MODULE_in_hookScope1440);  
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
                    // 195:12: -> ^( KindModule )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:195:15: ^( KindModule )
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
                    // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:196:5: OPERATOR
                    {
                    OPERATOR93=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_hookScope1452);  
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
                    // 196:14: -> ^( KindOperator )
                    {
                        // /Users/tonio/Documents/workspace/jtom/src/tom/gom/parser/GomLanguage.g:196:17: ^( KindOperator )
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


 

    public static final BitSet FOLLOW_MODULE_in_module60 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_modulename_in_module62 = new BitSet(new long[]{0x0000000000000000L,0x0000E00000000000L});
    public static final BitSet FOLLOW_imports_in_module67 = new BitSet(new long[]{0x0000000000000000L,0x0000E00000000000L});
    public static final BitSet FOLLOW_section_in_module71 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module73 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_modulename130 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_DOT_in_modulename132 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_ID_in_modulename142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORTS_in_imports170 = new BitSet(new long[]{0x0000000000000002L,0x0000080000000000L});
    public static final BitSet FOLLOW_importedModuleName_in_imports173 = new BitSet(new long[]{0x0000000000000002L,0x0000080000000000L});
    public static final BitSet FOLLOW_ID_in_importedModuleName202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_section228 = new BitSet(new long[]{0x0000000000000000L,0x0000E00000000000L});
    public static final BitSet FOLLOW_adtgrammar_in_section232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_syntax_in_adtgrammar256 = new BitSet(new long[]{0x0000000000000002L,0x0000E00000000000L});
    public static final BitSet FOLLOW_ABSTRACT_in_syntax276 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_SYNTAX_in_syntax278 = new BitSet(new long[]{0x0000000000000002L,0x00040C0000000000L,0x0000000000000018L});
    public static final BitSet FOLLOW_production_in_syntax283 = new BitSet(new long[]{0x0000000000000002L,0x00040C0000000000L,0x0000000000000018L});
    public static final BitSet FOLLOW_hookConstruct_in_syntax289 = new BitSet(new long[]{0x0000000000000002L,0x00040C0000000000L,0x0000000000000018L});
    public static final BitSet FOLLOW_typedecl_in_syntax295 = new BitSet(new long[]{0x0000000000000002L,0x00040C0000000000L,0x0000000000000018L});
    public static final BitSet FOLLOW_atomdecl_in_syntax301 = new BitSet(new long[]{0x0000000000000002L,0x00040C0000000000L,0x0000000000000018L});
    public static final BitSet FOLLOW_ID_in_production363 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_fieldlist_in_production365 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
    public static final BitSet FOLLOW_ARROW_in_production367 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_type_in_production369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ATOM_in_atomdecl401 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_ID_in_atomdecl405 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl431 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl433 = new BitSet(new long[]{0x0000000000000000L,0x0060080000000000L});
    public static final BitSet FOLLOW_alternatives_in_typedecl437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl477 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
    public static final BitSet FOLLOW_BINDS_in_typedecl479 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_atoms_in_typedecl483 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl485 = new BitSet(new long[]{0x0000000000000000L,0x0040080000000000L});
    public static final BitSet FOLLOW_pattern_alternatives_in_typedecl489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atoms535 = new BitSet(new long[]{0x0000000000000002L,0x0000080000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives567 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives569 = new BitSet(new long[]{0x0000000000000000L,0x0060080000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives575 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives579 = new BitSet(new long[]{0x0000000000000000L,0x0060080000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives586 = new BitSet(new long[]{0x0000000000000000L,0x0060080000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives591 = new BitSet(new long[]{0x0000000000000000L,0x0060080000000000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives598 = new BitSet(new long[]{0x0000000000000002L,0x00E0000000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives612 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives614 = new BitSet(new long[]{0x0000000000000000L,0x0060080000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives620 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_JAVADOC_in_alternatives624 = new BitSet(new long[]{0x0000000000000000L,0x0060080000000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives629 = new BitSet(new long[]{0x0000000000000000L,0x0060080000000000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives637 = new BitSet(new long[]{0x0000000000000002L,0x00E0000000000000L});
    public static final BitSet FOLLOW_SEMI_in_alternatives646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALT_in_pattern_alternatives678 = new BitSet(new long[]{0x0000000000000000L,0x0040080000000000L});
    public static final BitSet FOLLOW_pattern_opdecl_in_pattern_alternatives682 = new BitSet(new long[]{0x0000000000000002L,0x00C0000000000000L});
    public static final BitSet FOLLOW_ALT_in_pattern_alternatives686 = new BitSet(new long[]{0x0000000000000000L,0x0040080000000000L});
    public static final BitSet FOLLOW_pattern_opdecl_in_pattern_alternatives688 = new BitSet(new long[]{0x0000000000000002L,0x00C0000000000000L});
    public static final BitSet FOLLOW_SEMI_in_pattern_alternatives694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_opdecl722 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_fieldlist_in_opdecl724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_opdecl824 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_pattern_fieldlist_in_pattern_opdecl826 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_fieldlist875 = new BitSet(new long[]{0x0000000000000000L,0x1400080000000000L});
    public static final BitSet FOLLOW_field_in_fieldlist878 = new BitSet(new long[]{0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_COMMA_in_fieldlist881 = new BitSet(new long[]{0x0000000000000000L,0x1000080000000000L});
    public static final BitSet FOLLOW_field_in_fieldlist883 = new BitSet(new long[]{0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_fieldlist890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_pattern_fieldlist914 = new BitSet(new long[]{0x0000000000000000L,0x8400080000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_pattern_field_in_pattern_fieldlist917 = new BitSet(new long[]{0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_COMMA_in_pattern_fieldlist920 = new BitSet(new long[]{0x0000000000000000L,0x8000080000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_pattern_field_in_pattern_fieldlist922 = new BitSet(new long[]{0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_pattern_fieldlist929 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_type950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_type976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_field1002 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_STAR_in_field1004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LDIPLE_in_field1022 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_pattern_type_in_field1024 = new BitSet(new long[]{0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_RDIPLE_in_field1026 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_STAR_in_field1028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field1046 = new BitSet(new long[]{0x0000000000000000L,0x4000000000000000L});
    public static final BitSet FOLLOW_COLON_in_field1048 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_type_in_field1050 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field1070 = new BitSet(new long[]{0x0000000000000000L,0x4000000000000000L});
    public static final BitSet FOLLOW_COLON_in_field1072 = new BitSet(new long[]{0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_LDIPLE_in_field1074 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_pattern_type_in_field1076 = new BitSet(new long[]{0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_RDIPLE_in_field1078 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pattern_type_in_pattern_field1108 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_STAR_in_pattern_field1110 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INNER_in_pattern_field1128 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1130 = new BitSet(new long[]{0x0000000000000000L,0x4000000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1132 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OUTER_in_pattern_field1154 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1156 = new BitSet(new long[]{0x0000000000000000L,0x4000000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1158 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEUTRAL_in_pattern_field1180 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_ID_in_pattern_field1182 = new BitSet(new long[]{0x0000000000000000L,0x4000000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1184 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_type_in_pattern_field1186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_pattern_field1206 = new BitSet(new long[]{0x0000000000000000L,0x4000000000000000L});
    public static final BitSet FOLLOW_COLON_in_pattern_field1208 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_pattern_type_in_pattern_field1210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arglist1237 = new BitSet(new long[]{0x0000000000000000L,0x0400080000000000L});
    public static final BitSet FOLLOW_arg_in_arglist1240 = new BitSet(new long[]{0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_COMMA_in_arglist1243 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_arg_in_arglist1245 = new BitSet(new long[]{0x0000000000000000L,0x0600000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_arglist1252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_arg1278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_hookScope_in_hookConstruct1299 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct1305 = new BitSet(new long[]{0x0000000000000000L,0x4000000000000000L});
    public static final BitSet FOLLOW_COLON_in_hookConstruct1307 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct1311 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_arglist_in_hookConstruct1313 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACE_in_hookConstruct1315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SORT_in_hookScope1428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MODULE_in_hookScope1440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATOR_in_hookScope1452 = new BitSet(new long[]{0x0000000000000002L});

}