// $ANTLR 3.1 /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g 2008-08-26 17:23:29

package tom.gom.parser;
import tom.gom.adt.gom.GomTree;
import tom.gom.GomStreamManager;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class GomLanguageParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Grammar", "GomType", "Sort", "ConcSlotField", "CutSort", "ImportHookDecl", "ImportHook", "FullSortClass", "MakeBeforeHook", "Slots", "OperatorClass", "SortType", "KindSort", "ShortSortClass", "Origin", "ConcOperator", "InterfaceHookDecl", "KindModule", "ConcHook", "Arg", "ModHookPair", "ConcModuleDecl", "ConcModule", "ConcField", "ConcGomModule", "FullOperatorClass", "Module", "Compare", "HookKind", "Slot", "CutOperator", "SlotField", "ConcGrammar", "ModuleDecl", "Variadic", "ConcSlot", "ConcClassName", "Cons", "MappingHook", "ConcProduction", "BlockHookDecl", "SortClass", "ConcGomClass", "MappingHookDecl", "ConcSort", "ConcImportedModule", "ConcArg", "Production", "IsCons", "SortDecl", "IsEmpty", "Import", "ConcHookDecl", "InterfaceHook", "Hook", "Imports", "Public", "Code", "TomMapping", "ConcGomType", "CodeList", "MakeHookDecl", "AbstractTypeClass", "NamedField", "CutModule", "GomModule", "MakeHook", "Empty", "GomModuleName", "BuiltinSortDecl", "KindOperator", "BlockHook", "StarredField", "ConcSection", "VariadicOperatorClass", "OptionList", "Sorts", "OperatorDecl", "ClassName", "ConcSortDecl", "MODULE", "ID", "DOT", "IMPORTS", "PUBLIC", "ABSTRACT", "SYNTAX", "ARROW", "EQUALS", "ALT", "SEMI", "LPAREN", "COMMA", "RPAREN", "COLON", "LBRACE", "SORT", "OPERATOR", "STAR", "PRIVATE", "RBRACE", "WS", "SLCOMMENT", "MLCOMMENT"
    };
    public static final int COMMA=96;
    public static final int GomType=5;
    public static final int Sort=6;
    public static final int CutSort=8;
    public static final int ImportHook=10;
    public static final int Slots=13;
    public static final int KindSort=16;
    public static final int ABSTRACT=89;
    public static final int ShortSortClass=17;
    public static final int InterfaceHookDecl=20;
    public static final int KindModule=21;
    public static final int ConcModuleDecl=25;
    public static final int ConcField=27;
    public static final int DOT=86;
    public static final int PRIVATE=103;
    public static final int FullOperatorClass=29;
    public static final int SLCOMMENT=106;
    public static final int Compare=31;
    public static final int HookKind=32;
    public static final int Slot=33;
    public static final int SlotField=35;
    public static final int CutOperator=34;
    public static final int ConcGrammar=36;
    public static final int ConcSlot=39;
    public static final int MODULE=84;
    public static final int ConcClassName=40;
    public static final int RPAREN=97;
    public static final int MappingHook=42;
    public static final int ConcProduction=43;
    public static final int BlockHookDecl=44;
    public static final int SortClass=45;
    public static final int ConcGomClass=46;
    public static final int MappingHookDecl=47;
    public static final int ConcImportedModule=49;
    public static final int ConcArg=50;
    public static final int Production=51;
    public static final int IsEmpty=54;
    public static final int Import=55;
    public static final int ConcHookDecl=56;
    public static final int InterfaceHook=57;
    public static final int WS=105;
    public static final int Imports=59;
    public static final int Code=61;
    public static final int TomMapping=62;
    public static final int SORT=100;
    public static final int ConcGomType=63;
    public static final int CodeList=64;
    public static final int SEMI=94;
    public static final int EQUALS=92;
    public static final int AbstractTypeClass=66;
    public static final int BuiltinSortDecl=73;
    public static final int COLON=98;
    public static final int SYNTAX=90;
    public static final int VariadicOperatorClass=78;
    public static final int OptionList=79;
    public static final int ClassName=82;
    public static final int ConcSortDecl=83;
    public static final int Grammar=4;
    public static final int PUBLIC=88;
    public static final int ConcSlotField=7;
    public static final int ImportHookDecl=9;
    public static final int FullSortClass=11;
    public static final int ARROW=91;
    public static final int MakeBeforeHook=12;
    public static final int OperatorClass=14;
    public static final int SortType=15;
    public static final int Origin=18;
    public static final int ConcOperator=19;
    public static final int Arg=23;
    public static final int ConcHook=22;
    public static final int ModHookPair=24;
    public static final int ConcModule=26;
    public static final int ConcGomModule=28;
    public static final int LBRACE=99;
    public static final int RBRACE=104;
    public static final int Module=30;
    public static final int MLCOMMENT=107;
    public static final int ALT=93;
    public static final int ModuleDecl=37;
    public static final int Variadic=38;
    public static final int Cons=41;
    public static final int LPAREN=95;
    public static final int IMPORTS=87;
    public static final int OPERATOR=101;
    public static final int ID=85;
    public static final int ConcSort=48;
    public static final int IsCons=52;
    public static final int SortDecl=53;
    public static final int Hook=58;
    public static final int Public=60;
    public static final int MakeHookDecl=65;
    public static final int NamedField=67;
    public static final int CutModule=68;
    public static final int GomModule=69;
    public static final int MakeHook=70;
    public static final int EOF=-1;
    public static final int Empty=71;
    public static final int GomModuleName=72;
    public static final int KindOperator=74;
    public static final int BlockHook=75;
    public static final int StarredField=76;
    public static final int STAR=102;
    public static final int ConcSection=77;
    public static final int Sorts=80;
    public static final int OperatorDecl=81;

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
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "module"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:54:1: module : MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) ;
    public final GomLanguageParser.module_return module() throws RecognitionException {
        GomLanguageParser.module_return retval = new GomLanguageParser.module_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token MODULE1=null;
        Token EOF4=null;
        GomLanguageParser.imports_return imps = null;

        GomLanguageParser.modulename_return modulename2 = null;

        GomLanguageParser.section_return section3 = null;


        GomTree MODULE1_tree=null;
        GomTree EOF4_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");
        RewriteRuleSubtreeStream stream_modulename=new RewriteRuleSubtreeStream(adaptor,"rule modulename");
        RewriteRuleSubtreeStream stream_imports=new RewriteRuleSubtreeStream(adaptor,"rule imports");
        RewriteRuleSubtreeStream stream_section=new RewriteRuleSubtreeStream(adaptor,"rule section");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:54:8: ( MODULE modulename (imps= imports )? section EOF -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:55:3: MODULE modulename (imps= imports )? section EOF
            {
            MODULE1=(Token)match(input,MODULE,FOLLOW_MODULE_in_module304);  
            stream_MODULE.add(MODULE1);

            pushFollow(FOLLOW_modulename_in_module306);
            modulename2=modulename();

            state._fsp--;

            stream_modulename.add(modulename2.getTree());
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:55:21: (imps= imports )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==IMPORTS) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:55:22: imps= imports
                    {
                    pushFollow(FOLLOW_imports_in_module311);
                    imps=imports();

                    state._fsp--;

                    stream_imports.add(imps.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_section_in_module315);
            section3=section();

            state._fsp--;

            stream_section.add(section3.getTree());
            EOF4=(Token)match(input,EOF,FOLLOW_EOF_in_module317);  
            stream_EOF.add(EOF4);



            // AST REWRITE
            // elements: section, section, modulename, modulename, imports
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 56:3: -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) )
            if (imps!=null) {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:56:20: ^( GomModule modulename ^( ConcSection imports section ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:56:43: ^( ConcSection imports section )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(ConcSection, "ConcSection"), root_2);

                adaptor.addChild(root_2, stream_imports.nextTree());
                adaptor.addChild(root_2, stream_section.nextTree());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 57:3: -> ^( GomModule modulename ^( ConcSection section ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:57:6: ^( GomModule modulename ^( ConcSection section ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.nextTree());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:57:29: ^( ConcSection section )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(ConcSection, "ConcSection"), root_2);

                adaptor.addChild(root_2, stream_section.nextTree());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "module"

    public static class modulename_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "modulename"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:60:1: modulename : (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) ;
    public final GomLanguageParser.modulename_return modulename() throws RecognitionException {
        GomLanguageParser.modulename_return retval = new GomLanguageParser.modulename_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token mod=null;
        Token moduleName=null;
        Token DOT5=null;

        GomTree mod_tree=null;
        GomTree moduleName_tree=null;
        GomTree DOT5_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");


          StringBuilder packagePrefix = new StringBuilder("");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:63:3: ( (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:64:3: (mod= ID DOT )* moduleName= ID
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:64:3: (mod= ID DOT )*
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
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:64:4: mod= ID DOT
            	    {
            	    mod=(Token)match(input,ID,FOLLOW_ID_in_modulename374);  
            	    stream_ID.add(mod);

            	    DOT5=(Token)match(input,DOT,FOLLOW_DOT_in_modulename376);  
            	    stream_DOT.add(DOT5);

            	     packagePrefix.append((mod!=null?mod.getText():null)+"."); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            moduleName=(Token)match(input,ID,FOLLOW_ID_in_modulename386);  
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

            root_0 = (GomTree)adaptor.nil();
            // 74:3: -> ^( GomModuleName $moduleName)
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:74:6: ^( GomModuleName $moduleName)
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(GomModuleName, "GomModuleName"), root_1);

                adaptor.addChild(root_1, stream_moduleName.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "modulename"

    public static class imports_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "imports"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:77:1: imports : IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) ;
    public final GomLanguageParser.imports_return imports() throws RecognitionException {
        GomLanguageParser.imports_return retval = new GomLanguageParser.imports_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token IMPORTS6=null;
        GomLanguageParser.importedModuleName_return importedModuleName7 = null;


        GomTree IMPORTS6_tree=null;
        RewriteRuleTokenStream stream_IMPORTS=new RewriteRuleTokenStream(adaptor,"token IMPORTS");
        RewriteRuleSubtreeStream stream_importedModuleName=new RewriteRuleSubtreeStream(adaptor,"rule importedModuleName");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:77:9: ( IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:3: IMPORTS ( importedModuleName )*
            {
            IMPORTS6=(Token)match(input,IMPORTS,FOLLOW_IMPORTS_in_imports414);  
            stream_IMPORTS.add(IMPORTS6);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:11: ( importedModuleName )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:12: importedModuleName
            	    {
            	    pushFollow(FOLLOW_importedModuleName_in_imports417);
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

            root_0 = (GomTree)adaptor.nil();
            // 78:33: -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:36: ^( Imports ^( ConcImportedModule ( importedModuleName )* ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Imports, "Imports"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:46: ^( ConcImportedModule ( importedModuleName )* )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(ConcImportedModule, "ConcImportedModule"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:67: ( importedModuleName )*
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

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "imports"

    public static class importedModuleName_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "importedModuleName"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:80:1: importedModuleName : ID -> ^( Import ^( GomModuleName ID ) ) ;
    public final GomLanguageParser.importedModuleName_return importedModuleName() throws RecognitionException {
        GomLanguageParser.importedModuleName_return retval = new GomLanguageParser.importedModuleName_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID8=null;

        GomTree ID8_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:80:20: ( ID -> ^( Import ^( GomModuleName ID ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:81:3: ID
            {
            ID8=(Token)match(input,ID,FOLLOW_ID_in_importedModuleName446);  
            stream_ID.add(ID8);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 81:6: -> ^( Import ^( GomModuleName ID ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:81:9: ^( Import ^( GomModuleName ID ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Import, "Import"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:81:18: ^( GomModuleName ID )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(GomModuleName, "GomModuleName"), root_2);

                adaptor.addChild(root_2, stream_ID.nextNode());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "importedModuleName"

    public static class section_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "section"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:84:1: section : ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) ;
    public final GomLanguageParser.section_return section() throws RecognitionException {
        GomLanguageParser.section_return retval = new GomLanguageParser.section_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token PUBLIC9=null;
        GomLanguageParser.adtgrammar_return adtgrammar10 = null;


        GomTree PUBLIC9_tree=null;
        RewriteRuleTokenStream stream_PUBLIC=new RewriteRuleTokenStream(adaptor,"token PUBLIC");
        RewriteRuleSubtreeStream stream_adtgrammar=new RewriteRuleSubtreeStream(adaptor,"rule adtgrammar");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:84:9: ( ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:85:3: ( PUBLIC )? adtgrammar
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:85:3: ( PUBLIC )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==PUBLIC) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:85:4: PUBLIC
                    {
                    PUBLIC9=(Token)match(input,PUBLIC,FOLLOW_PUBLIC_in_section472);  
                    stream_PUBLIC.add(PUBLIC9);


                    }
                    break;

            }

            pushFollow(FOLLOW_adtgrammar_in_section476);
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

            root_0 = (GomTree)adaptor.nil();
            // 85:24: -> ^( Public adtgrammar )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:85:27: ^( Public adtgrammar )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Public, "Public"), root_1);

                adaptor.addChild(root_1, stream_adtgrammar.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "section"

    public static class adtgrammar_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "adtgrammar"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:88:1: adtgrammar : (gr+= syntax )+ -> $gr;
    public final GomLanguageParser.adtgrammar_return adtgrammar() throws RecognitionException {
        GomLanguageParser.adtgrammar_return retval = new GomLanguageParser.adtgrammar_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        List list_gr=null;
        GomLanguageParser.syntax_return gr = null;
        RewriteRuleSubtreeStream stream_syntax=new RewriteRuleSubtreeStream(adaptor,"rule syntax");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:88:12: ( (gr+= syntax )+ -> $gr)
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:89:3: (gr+= syntax )+
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:89:3: (gr+= syntax )+
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
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:89:4: gr+= syntax
            	    {
            	    pushFollow(FOLLOW_syntax_in_adtgrammar500);
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
            root_0 = (GomTree)adaptor.nil();
            // 89:17: -> $gr
            {
                adaptor.addChild(root_0, stream_gr.nextTree());

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "adtgrammar"

    public static class type_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "type"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:1: type : ID -> ^( GomType ID ) ;
    public final GomLanguageParser.type_return type() throws RecognitionException {
        GomLanguageParser.type_return retval = new GomLanguageParser.type_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID11=null;

        GomTree ID11_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:6: ( ID -> ^( GomType ID ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:93:3: ID
            {
            ID11=(Token)match(input,ID,FOLLOW_ID_in_type520);  
            stream_ID.add(ID11);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 93:6: -> ^( GomType ID )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:93:9: ^( GomType ID )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(GomType, "GomType"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "type"

    public static class syntax_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "syntax"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:95:1: syntax : ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* ) ) ) ;
    public final GomLanguageParser.syntax_return syntax() throws RecognitionException {
        GomLanguageParser.syntax_return retval = new GomLanguageParser.syntax_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ABSTRACT12=null;
        Token SYNTAX13=null;
        List list_gr1=null;
        List list_gr2=null;
        List list_gr3=null;
        GomLanguageParser.production_return gr1 = null;
        GomLanguageParser.hookConstruct_return gr2 = null;
        GomLanguageParser.typedecl_return gr3 = null;
        GomTree ABSTRACT12_tree=null;
        GomTree SYNTAX13_tree=null;
        RewriteRuleTokenStream stream_ABSTRACT=new RewriteRuleTokenStream(adaptor,"token ABSTRACT");
        RewriteRuleTokenStream stream_SYNTAX=new RewriteRuleTokenStream(adaptor,"token SYNTAX");
        RewriteRuleSubtreeStream stream_hookConstruct=new RewriteRuleSubtreeStream(adaptor,"rule hookConstruct");
        RewriteRuleSubtreeStream stream_typedecl=new RewriteRuleSubtreeStream(adaptor,"rule typedecl");
        RewriteRuleSubtreeStream stream_production=new RewriteRuleSubtreeStream(adaptor,"rule production");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:95:8: ( ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* ) ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:96:3: ABSTRACT SYNTAX (gr1+= production | gr2+= hookConstruct | gr3+= typedecl )*
            {
            ABSTRACT12=(Token)match(input,ABSTRACT,FOLLOW_ABSTRACT_in_syntax538);  
            stream_ABSTRACT.add(ABSTRACT12);

            SYNTAX13=(Token)match(input,SYNTAX,FOLLOW_SYNTAX_in_syntax540);  
            stream_SYNTAX.add(SYNTAX13);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:96:19: (gr1+= production | gr2+= hookConstruct | gr3+= typedecl )*
            loop6:
            do {
                int alt6=4;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==ID) ) {
                    switch ( input.LA(2) ) {
                    case COLON:
                        {
                        alt6=2;
                        }
                        break;
                    case EQUALS:
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
                else if ( (LA6_0==MODULE||(LA6_0>=SORT && LA6_0<=OPERATOR)) ) {
                    alt6=2;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:96:20: gr1+= production
            	    {
            	    pushFollow(FOLLOW_production_in_syntax545);
            	    gr1=production();

            	    state._fsp--;

            	    stream_production.add(gr1.getTree());
            	    if (list_gr1==null) list_gr1=new ArrayList();
            	    list_gr1.add(gr1.getTree());


            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:96:38: gr2+= hookConstruct
            	    {
            	    pushFollow(FOLLOW_hookConstruct_in_syntax551);
            	    gr2=hookConstruct();

            	    state._fsp--;

            	    stream_hookConstruct.add(gr2.getTree());
            	    if (list_gr2==null) list_gr2=new ArrayList();
            	    list_gr2.add(gr2.getTree());


            	    }
            	    break;
            	case 3 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:96:59: gr3+= typedecl
            	    {
            	    pushFollow(FOLLOW_typedecl_in_syntax557);
            	    gr3=typedecl();

            	    state._fsp--;

            	    stream_typedecl.add(gr3.getTree());
            	    if (list_gr3==null) list_gr3=new ArrayList();
            	    list_gr3.add(gr3.getTree());


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);



            // AST REWRITE
            // elements: gr3, gr1, gr2
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: gr3, gr1, gr2
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_gr3=new RewriteRuleSubtreeStream(adaptor,"token gr3",list_gr3);
            RewriteRuleSubtreeStream stream_gr1=new RewriteRuleSubtreeStream(adaptor,"token gr1",list_gr1);
            RewriteRuleSubtreeStream stream_gr2=new RewriteRuleSubtreeStream(adaptor,"token gr2",list_gr2);
            root_0 = (GomTree)adaptor.nil();
            // 97:3: -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* ) ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:97:6: ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* ) ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(ConcGrammar, "ConcGrammar"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:97:20: ^( Grammar ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* ) )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Grammar, "Grammar"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:97:30: ^( ConcProduction ( $gr1)* ( $gr2)* ( $gr3)* )
                {
                GomTree root_3 = (GomTree)adaptor.nil();
                root_3 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(ConcProduction, "ConcProduction"), root_3);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:97:47: ( $gr1)*
                while ( stream_gr1.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr1.nextTree());

                }
                stream_gr1.reset();
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:97:55: ( $gr2)*
                while ( stream_gr2.hasNext() ) {
                    adaptor.addChild(root_3, stream_gr2.nextTree());

                }
                stream_gr2.reset();
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:97:63: ( $gr3)*
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

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "syntax"

    public static class production_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "production"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:100:1: production : ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) ;
    public final GomLanguageParser.production_return production() throws RecognitionException {
        GomLanguageParser.production_return retval = new GomLanguageParser.production_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID14=null;
        Token ARROW16=null;
        GomLanguageParser.fieldlist_return fieldlist15 = null;

        GomLanguageParser.type_return type17 = null;


        GomTree ID14_tree=null;
        GomTree ARROW16_tree=null;
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule fieldlist");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");

        String startLine = ""+input.LT(1).getLine();

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:103:3: ( ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:104:3: ID fieldlist ARROW type
            {
            ID14=(Token)match(input,ID,FOLLOW_ID_in_production611);  
            stream_ID.add(ID14);

            pushFollow(FOLLOW_fieldlist_in_production613);
            fieldlist15=fieldlist();

            state._fsp--;

            stream_fieldlist.add(fieldlist15.getTree());
            ARROW16=(Token)match(input,ARROW,FOLLOW_ARROW_in_production615);  
            stream_ARROW.add(ARROW16);

            pushFollow(FOLLOW_type_in_production617);
            type17=type();

            state._fsp--;

            stream_type.add(type17.getTree());


            // AST REWRITE
            // elements: type, ID, ID, fieldlist
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 104:27: -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:104:30: ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                adaptor.addChild(root_1, stream_type.nextTree());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:104:61: ^( Origin ID[startLine] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (GomTree)adaptor.create(ID, startLine));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "production"

    public static class typedecl_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typedecl"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:107:1: typedecl : typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType $typename) $alts) ;
    public final GomLanguageParser.typedecl_return typedecl() throws RecognitionException {
        GomLanguageParser.typedecl_return retval = new GomLanguageParser.typedecl_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token typename=null;
        Token EQUALS18=null;
        GomLanguageParser.alternatives_return alts = null;


        GomTree typename_tree=null;
        GomTree EQUALS18_tree=null;
        RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_alternatives=new RewriteRuleSubtreeStream(adaptor,"rule alternatives");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:107:10: (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType $typename) $alts) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:108:3: typename= ID EQUALS alts= alternatives[typename]
            {
            typename=(Token)match(input,ID,FOLLOW_ID_in_typedecl651);  
            stream_ID.add(typename);

            EQUALS18=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_typedecl653);  
            stream_EQUALS.add(EQUALS18);

            pushFollow(FOLLOW_alternatives_in_typedecl657);
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

            root_0 = (GomTree)adaptor.nil();
            // 109:5: -> ^( SortType ^( GomType $typename) $alts)
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:109:8: ^( SortType ^( GomType $typename) $alts)
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(SortType, "SortType"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:109:19: ^( GomType $typename)
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(GomType, "GomType"), root_2);

                adaptor.addChild(root_2, stream_typename.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_alts.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typedecl"

    public static class alternatives_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "alternatives"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:112:1: alternatives[Token typename] : ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) ;
    public final GomLanguageParser.alternatives_return alternatives(Token typename) throws RecognitionException {
        GomLanguageParser.alternatives_return retval = new GomLanguageParser.alternatives_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ALT19=null;
        Token ALT21=null;
        Token SEMI23=null;
        GomLanguageParser.opdecl_return opdecl20 = null;

        GomLanguageParser.opdecl_return opdecl22 = null;


        GomTree ALT19_tree=null;
        GomTree ALT21_tree=null;
        GomTree SEMI23_tree=null;
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleSubtreeStream stream_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule opdecl");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:112:30: ( ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:3: ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* ( SEMI )?
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:3: ( ALT )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==ALT) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:4: ALT
                    {
                    ALT19=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives693);  
                    stream_ALT.add(ALT19);


                    }
                    break;

            }

            pushFollow(FOLLOW_opdecl_in_alternatives697);
            opdecl20=opdecl(typename);

            state._fsp--;

            stream_opdecl.add(opdecl20.getTree());
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:27: ( ALT opdecl[typename] )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==ALT) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:28: ALT opdecl[typename]
            	    {
            	    ALT21=(Token)match(input,ALT,FOLLOW_ALT_in_alternatives701);  
            	    stream_ALT.add(ALT21);

            	    pushFollow(FOLLOW_opdecl_in_alternatives703);
            	    opdecl22=opdecl(typename);

            	    state._fsp--;

            	    stream_opdecl.add(opdecl22.getTree());

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:51: ( SEMI )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==SEMI) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:52: SEMI
                    {
                    SEMI23=(Token)match(input,SEMI,FOLLOW_SEMI_in_alternatives709);  
                    stream_SEMI.add(SEMI23);


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

            root_0 = (GomTree)adaptor.nil();
            // 114:3: -> ^( ConcProduction ( opdecl )+ )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:114:6: ^( ConcProduction ( opdecl )+ )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(ConcProduction, "ConcProduction"), root_1);

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

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "alternatives"

    public static class opdecl_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "opdecl"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:1: opdecl[Token type] : ID fieldlist -> ^( Production ID fieldlist ^( GomType ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final GomLanguageParser.opdecl_return opdecl(Token type) throws RecognitionException {
        GomLanguageParser.opdecl_return retval = new GomLanguageParser.opdecl_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID24=null;
        GomLanguageParser.fieldlist_return fieldlist25 = null;


        GomTree ID24_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule fieldlist");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:20: ( ID fieldlist -> ^( Production ID fieldlist ^( GomType ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:118:3: ID fieldlist
            {
            ID24=(Token)match(input,ID,FOLLOW_ID_in_opdecl738);  
            stream_ID.add(ID24);

            pushFollow(FOLLOW_fieldlist_in_opdecl740);
            fieldlist25=fieldlist();

            state._fsp--;

            stream_fieldlist.add(fieldlist25.getTree());


            // AST REWRITE
            // elements: ID, ID, ID, fieldlist
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 119:3: -> ^( Production ID fieldlist ^( GomType ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:119:6: ^( Production ID fieldlist ^( GomType ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                adaptor.addChild(root_1, stream_fieldlist.nextTree());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:119:32: ^( GomType ID[type] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(GomType, "GomType"), root_2);

                adaptor.addChild(root_2, (GomTree)adaptor.create(ID, type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:120:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (GomTree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "opdecl"

    public static class fieldlist_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fieldlist"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:1: fieldlist : LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) ;
    public final GomLanguageParser.fieldlist_return fieldlist() throws RecognitionException {
        GomLanguageParser.fieldlist_return retval = new GomLanguageParser.fieldlist_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token LPAREN26=null;
        Token COMMA28=null;
        Token RPAREN30=null;
        GomLanguageParser.field_return field27 = null;

        GomLanguageParser.field_return field29 = null;


        GomTree LPAREN26_tree=null;
        GomTree COMMA28_tree=null;
        GomTree RPAREN30_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:11: ( LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:124:3: LPAREN ( field ( COMMA field )* )? RPAREN
            {
            LPAREN26=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_fieldlist785);  
            stream_LPAREN.add(LPAREN26);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:124:10: ( field ( COMMA field )* )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==ID) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:124:11: field ( COMMA field )*
                    {
                    pushFollow(FOLLOW_field_in_fieldlist788);
                    field27=field();

                    state._fsp--;

                    stream_field.add(field27.getTree());
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:124:17: ( COMMA field )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==COMMA) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:124:18: COMMA field
                    	    {
                    	    COMMA28=(Token)match(input,COMMA,FOLLOW_COMMA_in_fieldlist791);  
                    	    stream_COMMA.add(COMMA28);

                    	    pushFollow(FOLLOW_field_in_fieldlist793);
                    	    field29=field();

                    	    state._fsp--;

                    	    stream_field.add(field29.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAREN30=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_fieldlist800);  
            stream_RPAREN.add(RPAREN30);



            // AST REWRITE
            // elements: field
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 124:42: -> ^( ConcField ( field )* )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:124:45: ^( ConcField ( field )* )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:124:57: ( field )*
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

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "fieldlist"

    public static class arglist_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arglist"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:126:1: arglist : ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) ;
    public final GomLanguageParser.arglist_return arglist() throws RecognitionException {
        GomLanguageParser.arglist_return retval = new GomLanguageParser.arglist_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token LPAREN31=null;
        Token COMMA33=null;
        Token RPAREN35=null;
        GomLanguageParser.arg_return arg32 = null;

        GomLanguageParser.arg_return arg34 = null;


        GomTree LPAREN31_tree=null;
        GomTree COMMA33_tree=null;
        GomTree RPAREN35_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:126:8: ( ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==LPAREN) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:4: LPAREN ( arg ( COMMA arg )* )? RPAREN
                    {
                    LPAREN31=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arglist822);  
                    stream_LPAREN.add(LPAREN31);

                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:11: ( arg ( COMMA arg )* )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==ID) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:12: arg ( COMMA arg )*
                            {
                            pushFollow(FOLLOW_arg_in_arglist825);
                            arg32=arg();

                            state._fsp--;

                            stream_arg.add(arg32.getTree());
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:16: ( COMMA arg )*
                            loop12:
                            do {
                                int alt12=2;
                                int LA12_0 = input.LA(1);

                                if ( (LA12_0==COMMA) ) {
                                    alt12=1;
                                }


                                switch (alt12) {
                            	case 1 :
                            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:17: COMMA arg
                            	    {
                            	    COMMA33=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist828);  
                            	    stream_COMMA.add(COMMA33);

                            	    pushFollow(FOLLOW_arg_in_arglist830);
                            	    arg34=arg();

                            	    state._fsp--;

                            	    stream_arg.add(arg34.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop12;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAREN35=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arglist837);  
                    stream_RPAREN.add(RPAREN35);


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

            root_0 = (GomTree)adaptor.nil();
            // 128:3: -> ^( ConcArg ( arg )* )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:6: ^( ConcArg ( arg )* )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(ConcArg, "ConcArg"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:16: ( arg )*
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

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arglist"

    public static class arg_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arg"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:1: arg : ID -> ^( Arg ID ) ;
    public final GomLanguageParser.arg_return arg() throws RecognitionException {
        GomLanguageParser.arg_return retval = new GomLanguageParser.arg_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID36=null;

        GomTree ID36_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:5: ( ID -> ^( Arg ID ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:7: ID
            {
            ID36=(Token)match(input,ID,FOLLOW_ID_in_arg864);  
            stream_ID.add(ID36);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 131:10: -> ^( Arg ID )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:13: ^( Arg ID )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Arg, "Arg"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arg"

    public static class hookConstruct_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "hookConstruct"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:133:1: hookConstruct : (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final GomLanguageParser.hookConstruct_return hookConstruct() throws RecognitionException {
        GomLanguageParser.hookConstruct_return retval = new GomLanguageParser.hookConstruct_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token pointCut=null;
        Token hookType=null;
        Token COLON37=null;
        Token LBRACE39=null;
        GomLanguageParser.hookScope_return hscope = null;

        GomLanguageParser.arglist_return arglist38 = null;


        GomTree pointCut_tree=null;
        GomTree hookType_tree=null;
        GomTree COLON37_tree=null;
        GomTree LBRACE39_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_hookScope=new RewriteRuleSubtreeStream(adaptor,"rule hookScope");
        RewriteRuleSubtreeStream stream_arglist=new RewriteRuleSubtreeStream(adaptor,"rule arglist");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:133:15: ( (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:134:3: (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:134:3: (hscope= hookScope )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==MODULE||(LA15_0>=SORT && LA15_0<=OPERATOR)) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:134:4: hscope= hookScope
                    {
                    pushFollow(FOLLOW_hookScope_in_hookConstruct885);
                    hscope=hookScope();

                    state._fsp--;

                    stream_hookScope.add(hscope.getTree());

                    }
                    break;

            }

            pointCut=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct891);  
            stream_ID.add(pointCut);

            COLON37=(Token)match(input,COLON,FOLLOW_COLON_in_hookConstruct893);  
            stream_COLON.add(COLON37);

            hookType=(Token)match(input,ID,FOLLOW_ID_in_hookConstruct897);  
            stream_ID.add(hookType);

            pushFollow(FOLLOW_arglist_in_hookConstruct899);
            arglist38=arglist();

            state._fsp--;

            stream_arglist.add(arglist38.getTree());
            LBRACE39=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_hookConstruct901);  
            stream_LBRACE.add(LBRACE39);



            // AST REWRITE
            // elements: arglist, LBRACE, ID, hookType, LBRACE, pointCut, pointCut, hscope, hookType, ID, arglist
            // token labels: pointCut, hookType
            // rule labels: hscope, retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_pointCut=new RewriteRuleTokenStream(adaptor,"token pointCut",pointCut);
            RewriteRuleTokenStream stream_hookType=new RewriteRuleTokenStream(adaptor,"token hookType",hookType);
            RewriteRuleSubtreeStream stream_hscope=new RewriteRuleSubtreeStream(adaptor,"token hscope",hscope!=null?hscope.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 135:3: -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            if (hscope!=null) {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:135:22: ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Hook, "Hook"), root_1);

                adaptor.addChild(root_1, stream_hscope.nextTree());
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:135:47: ^( HookKind $hookType)
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:136:24: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (GomTree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 137:3: -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:137:6: ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Hook, "Hook"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:137:13: ^( KindOperator )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(KindOperator, "KindOperator"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_pointCut.nextNode());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:137:39: ^( HookKind $hookType)
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.nextNode());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.nextTree());
                adaptor.addChild(root_1, stream_LBRACE.nextNode());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:138:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, (GomTree)adaptor.create(ID, ""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "hookConstruct"

    public static class hookScope_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "hookScope"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:142:1: hookScope : ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) );
    public final GomLanguageParser.hookScope_return hookScope() throws RecognitionException {
        GomLanguageParser.hookScope_return retval = new GomLanguageParser.hookScope_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token SORT40=null;
        Token MODULE41=null;
        Token OPERATOR42=null;

        GomTree SORT40_tree=null;
        GomTree MODULE41_tree=null;
        GomTree OPERATOR42_tree=null;
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");
        RewriteRuleTokenStream stream_SORT=new RewriteRuleTokenStream(adaptor,"token SORT");
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:142:11: ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) )
            int alt16=3;
            switch ( input.LA(1) ) {
            case SORT:
                {
                alt16=1;
                }
                break;
            case MODULE:
                {
                alt16=2;
                }
                break;
            case OPERATOR:
                {
                alt16=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:143:3: SORT
                    {
                    SORT40=(Token)match(input,SORT,FOLLOW_SORT_in_hookScope1014);  
                    stream_SORT.add(SORT40);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 143:8: -> ^( KindSort )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:143:11: ^( KindSort )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(KindSort, "KindSort"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:144:5: MODULE
                    {
                    MODULE41=(Token)match(input,MODULE,FOLLOW_MODULE_in_hookScope1026);  
                    stream_MODULE.add(MODULE41);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 144:12: -> ^( KindModule )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:144:15: ^( KindModule )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(KindModule, "KindModule"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:145:5: OPERATOR
                    {
                    OPERATOR42=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_hookScope1038);  
                    stream_OPERATOR.add(OPERATOR42);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 145:14: -> ^( KindOperator )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:145:17: ^( KindOperator )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(KindOperator, "KindOperator"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "hookScope"

    public static class field_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "field"
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:148:1: field : ( type STAR -> ^( StarredField type ) | ID COLON type -> ^( NamedField ID type ) );
    public final GomLanguageParser.field_return field() throws RecognitionException {
        GomLanguageParser.field_return retval = new GomLanguageParser.field_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token STAR44=null;
        Token ID45=null;
        Token COLON46=null;
        GomLanguageParser.type_return type43 = null;

        GomLanguageParser.type_return type47 = null;


        GomTree STAR44_tree=null;
        GomTree ID45_tree=null;
        GomTree COLON46_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:148:7: ( type STAR -> ^( StarredField type ) | ID COLON type -> ^( NamedField ID type ) )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==ID) ) {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==COLON) ) {
                    alt17=2;
                }
                else if ( (LA17_1==STAR) ) {
                    alt17=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:149:3: type STAR
                    {
                    pushFollow(FOLLOW_type_in_field1057);
                    type43=type();

                    state._fsp--;

                    stream_type.add(type43.getTree());
                    STAR44=(Token)match(input,STAR,FOLLOW_STAR_in_field1059);  
                    stream_STAR.add(STAR44);



                    // AST REWRITE
                    // elements: type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 149:13: -> ^( StarredField type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:149:16: ^( StarredField type )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_type.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:150:5: ID COLON type
                    {
                    ID45=(Token)match(input,ID,FOLLOW_ID_in_field1073);  
                    stream_ID.add(ID45);

                    COLON46=(Token)match(input,COLON,FOLLOW_COLON_in_field1075);  
                    stream_COLON.add(COLON46);

                    pushFollow(FOLLOW_type_in_field1077);
                    type47=type();

                    state._fsp--;

                    stream_type.add(type47.getTree());


                    // AST REWRITE
                    // elements: ID, type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 150:19: -> ^( NamedField ID type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:150:22: ^( NamedField ID type )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot((GomTree)adaptor.create(NamedField, "NamedField"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_type.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (GomTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "field"

    // Delegated rules


 

    public static final BitSet FOLLOW_MODULE_in_module304 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_modulename_in_module306 = new BitSet(new long[]{0x0000000000000000L,0x0000000003800000L});
    public static final BitSet FOLLOW_imports_in_module311 = new BitSet(new long[]{0x0000000000000000L,0x0000000003800000L});
    public static final BitSet FOLLOW_section_in_module315 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_module317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_modulename374 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_DOT_in_modulename376 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_ID_in_modulename386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORTS_in_imports414 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_importedModuleName_in_imports417 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_ID_in_importedModuleName446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_section472 = new BitSet(new long[]{0x0000000000000000L,0x0000000003800000L});
    public static final BitSet FOLLOW_adtgrammar_in_section476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_syntax_in_adtgrammar500 = new BitSet(new long[]{0x0000000000000002L,0x0000000003800000L});
    public static final BitSet FOLLOW_ID_in_type520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_syntax538 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_SYNTAX_in_syntax540 = new BitSet(new long[]{0x0000000000000002L,0x0000003000300000L});
    public static final BitSet FOLLOW_production_in_syntax545 = new BitSet(new long[]{0x0000000000000002L,0x0000003000300000L});
    public static final BitSet FOLLOW_hookConstruct_in_syntax551 = new BitSet(new long[]{0x0000000000000002L,0x0000003000300000L});
    public static final BitSet FOLLOW_typedecl_in_syntax557 = new BitSet(new long[]{0x0000000000000002L,0x0000003000300000L});
    public static final BitSet FOLLOW_ID_in_production611 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_fieldlist_in_production613 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_ARROW_in_production615 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_type_in_production617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl651 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl653 = new BitSet(new long[]{0x0000000000000000L,0x0000000020200000L});
    public static final BitSet FOLLOW_alternatives_in_typedecl657 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALT_in_alternatives693 = new BitSet(new long[]{0x0000000000000000L,0x0000000020200000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives697 = new BitSet(new long[]{0x0000000000000002L,0x0000000060000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives701 = new BitSet(new long[]{0x0000000000000000L,0x0000000020200000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives703 = new BitSet(new long[]{0x0000000000000002L,0x0000000060000000L});
    public static final BitSet FOLLOW_SEMI_in_alternatives709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_opdecl738 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_fieldlist_in_opdecl740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_fieldlist785 = new BitSet(new long[]{0x0000000000000000L,0x0000000200200000L});
    public static final BitSet FOLLOW_field_in_fieldlist788 = new BitSet(new long[]{0x0000000000000000L,0x0000000300000000L});
    public static final BitSet FOLLOW_COMMA_in_fieldlist791 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_field_in_fieldlist793 = new BitSet(new long[]{0x0000000000000000L,0x0000000300000000L});
    public static final BitSet FOLLOW_RPAREN_in_fieldlist800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arglist822 = new BitSet(new long[]{0x0000000000000000L,0x0000000200200000L});
    public static final BitSet FOLLOW_arg_in_arglist825 = new BitSet(new long[]{0x0000000000000000L,0x0000000300000000L});
    public static final BitSet FOLLOW_COMMA_in_arglist828 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_arg_in_arglist830 = new BitSet(new long[]{0x0000000000000000L,0x0000000300000000L});
    public static final BitSet FOLLOW_RPAREN_in_arglist837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_arg864 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_hookScope_in_hookConstruct885 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct891 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
    public static final BitSet FOLLOW_COLON_in_hookConstruct893 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct897 = new BitSet(new long[]{0x0000000000000000L,0x0000000880000000L});
    public static final BitSet FOLLOW_arglist_in_hookConstruct899 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_LBRACE_in_hookConstruct901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SORT_in_hookScope1014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MODULE_in_hookScope1026 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATOR_in_hookScope1038 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_field1057 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_STAR_in_field1059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field1073 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
    public static final BitSet FOLLOW_COLON_in_field1075 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_type_in_field1077 = new BitSet(new long[]{0x0000000000000002L});

}