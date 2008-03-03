// $ANTLR 3.0 /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g 2008-03-03 18:58:11

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Grammar", "GomType", "Sort", "ConcSlotField", "CutSort", "ImportHookDecl", "ImportHook", "FullSortClass", "MakeBeforeHook", "Slots", "OperatorClass", "SortType", "KindSort", "Origin", "ConcOperator", "InterfaceHookDecl", "KindModule", "ConcHook", "Arg", "ModHookPair", "ConcModuleDecl", "ConcModule", "ConcField", "ConcGomModule", "FullOperatorClass", "Module", "Compare", "HookKind", "Slot", "CutOperator", "SlotField", "ConcGrammar", "ModuleDecl", "Variadic", "ConcSlot", "ConcClassName", "Cons", "MappingHook", "ConcProduction", "BlockHookDecl", "SortClass", "ConcGomClass", "MappingHookDecl", "ConcSort", "ConcImportedModule", "ConcArg", "Production", "IsCons", "SortDecl", "IsEmpty", "Import", "ConcHookDecl", "InterfaceHook", "Hook", "Imports", "Public", "Code", "TomMapping", "ConcGomType", "CodeList", "MakeHookDecl", "AbstractTypeClass", "NamedField", "CutModule", "GomModule", "MakeHook", "Empty", "GomModuleName", "BuiltinSortDecl", "KindOperator", "BlockHook", "StarredField", "ConcSection", "VariadicOperatorClass", "OptionList", "Sorts", "OperatorDecl", "ClassName", "ConcSortDecl", "MODULE", "ID", "DOT", "IMPORTS", "PUBLIC", "SORTS", "ABSTRACT", "SYNTAX", "ARROW", "EQUALS", "ALT", "SEMI", "LPAREN", "COMMA", "RPAREN", "COLON", "LBRACE", "SORT", "OPERATOR", "STAR", "PRIVATE", "RBRACE", "WS", "SLCOMMENT", "MLCOMMENT"
    };
    public static final int COMMA=96;
    public static final int GomType=5;
    public static final int Sort=6;
    public static final int CutSort=8;
    public static final int ImportHook=10;
    public static final int Slots=13;
    public static final int KindSort=16;
    public static final int ABSTRACT=89;
    public static final int InterfaceHookDecl=19;
    public static final int KindModule=20;
    public static final int ConcModuleDecl=24;
    public static final int ConcField=26;
    public static final int DOT=85;
    public static final int SORTS=88;
    public static final int PRIVATE=103;
    public static final int FullOperatorClass=28;
    public static final int SLCOMMENT=106;
    public static final int Compare=30;
    public static final int HookKind=31;
    public static final int Slot=32;
    public static final int SlotField=34;
    public static final int CutOperator=33;
    public static final int ConcGrammar=35;
    public static final int ConcSlot=38;
    public static final int MODULE=83;
    public static final int ConcClassName=39;
    public static final int RPAREN=97;
    public static final int MappingHook=41;
    public static final int BlockHookDecl=43;
    public static final int ConcProduction=42;
    public static final int SortClass=44;
    public static final int ConcGomClass=45;
    public static final int MappingHookDecl=46;
    public static final int ConcImportedModule=48;
    public static final int ConcArg=49;
    public static final int Production=50;
    public static final int IsEmpty=53;
    public static final int Import=54;
    public static final int ConcHookDecl=55;
    public static final int InterfaceHook=56;
    public static final int WS=105;
    public static final int Imports=58;
    public static final int Code=60;
    public static final int TomMapping=61;
    public static final int SORT=100;
    public static final int ConcGomType=62;
    public static final int CodeList=63;
    public static final int SEMI=94;
    public static final int EQUALS=92;
    public static final int AbstractTypeClass=65;
    public static final int BuiltinSortDecl=72;
    public static final int COLON=98;
    public static final int SYNTAX=90;
    public static final int VariadicOperatorClass=77;
    public static final int OptionList=78;
    public static final int ClassName=81;
    public static final int ConcSortDecl=82;
    public static final int Grammar=4;
    public static final int PUBLIC=87;
    public static final int ConcSlotField=7;
    public static final int ImportHookDecl=9;
    public static final int FullSortClass=11;
    public static final int ARROW=91;
    public static final int MakeBeforeHook=12;
    public static final int OperatorClass=14;
    public static final int SortType=15;
    public static final int Origin=17;
    public static final int ConcOperator=18;
    public static final int Arg=22;
    public static final int ConcHook=21;
    public static final int ModHookPair=23;
    public static final int ConcModule=25;
    public static final int ConcGomModule=27;
    public static final int LBRACE=99;
    public static final int RBRACE=104;
    public static final int Module=29;
    public static final int MLCOMMENT=107;
    public static final int ALT=93;
    public static final int ModuleDecl=36;
    public static final int Variadic=37;
    public static final int Cons=40;
    public static final int LPAREN=95;
    public static final int IMPORTS=86;
    public static final int OPERATOR=101;
    public static final int ID=84;
    public static final int ConcSort=47;
    public static final int IsCons=51;
    public static final int SortDecl=52;
    public static final int Hook=57;
    public static final int Public=59;
    public static final int MakeHookDecl=64;
    public static final int NamedField=66;
    public static final int CutModule=67;
    public static final int GomModule=68;
    public static final int MakeHook=69;
    public static final int EOF=-1;
    public static final int Empty=70;
    public static final int GomModuleName=71;
    public static final int KindOperator=73;
    public static final int BlockHook=74;
    public static final int StarredField=75;
    public static final int STAR=102;
    public static final int ConcSection=76;
    public static final int Sorts=79;
    public static final int OperatorDecl=80;

        public GomLanguageParser(TokenStream input) {
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

    // $ANTLR start module
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:54:1: module : MODULE modulename (imps= imports )? section -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) ;
    public final module_return module() throws RecognitionException {
        module_return retval = new module_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token MODULE1=null;
        imports_return imps = null;

        modulename_return modulename2 = null;

        section_return section3 = null;


        GomTree MODULE1_tree=null;
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");
        RewriteRuleSubtreeStream stream_modulename=new RewriteRuleSubtreeStream(adaptor,"rule modulename");
        RewriteRuleSubtreeStream stream_imports=new RewriteRuleSubtreeStream(adaptor,"rule imports");
        RewriteRuleSubtreeStream stream_section=new RewriteRuleSubtreeStream(adaptor,"rule section");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:55:3: ( MODULE modulename (imps= imports )? section -> {imps!=null}? ^( GomModule modulename ^( ConcSection imports section ) ) -> ^( GomModule modulename ^( ConcSection section ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:55:3: MODULE modulename (imps= imports )? section
            {
            MODULE1=(Token)input.LT(1);
            match(input,MODULE,FOLLOW_MODULE_in_module301); 
            stream_MODULE.add(MODULE1);

            pushFollow(FOLLOW_modulename_in_module303);
            modulename2=modulename();
            _fsp--;

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
                    pushFollow(FOLLOW_imports_in_module308);
                    imps=imports();
                    _fsp--;

                    stream_imports.add(imps.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_section_in_module312);
            section3=section();
            _fsp--;

            stream_section.add(section3.getTree());

            // AST REWRITE
            // elements: imports, section, modulename, section, modulename
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
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.next());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:56:43: ^( ConcSection imports section )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(ConcSection, "ConcSection"), root_2);

                adaptor.addChild(root_2, stream_imports.next());
                adaptor.addChild(root_2, stream_section.next());

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
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(GomModule, "GomModule"), root_1);

                adaptor.addChild(root_1, stream_modulename.next());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:57:29: ^( ConcSection section )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(ConcSection, "ConcSection"), root_2);

                adaptor.addChild(root_2, stream_section.next());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end module

    public static class modulename_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start modulename
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:60:1: modulename : (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) ;
    public final modulename_return modulename() throws RecognitionException {
        modulename_return retval = new modulename_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token mod=null;
        Token moduleName=null;
        Token DOT4=null;

        GomTree mod_tree=null;
        GomTree moduleName_tree=null;
        GomTree DOT4_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");


          StringBuilder packagePrefix = new StringBuilder("");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:64:3: ( (mod= ID DOT )* moduleName= ID -> ^( GomModuleName $moduleName) )
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
            	    mod=(Token)input.LT(1);
            	    match(input,ID,FOLLOW_ID_in_modulename369); 
            	    stream_ID.add(mod);

            	    DOT4=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_modulename371); 
            	    stream_DOT.add(DOT4);

            	     packagePrefix.append(mod.getText()+"."); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            moduleName=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_modulename381); 
            stream_ID.add(moduleName);


                if (packagePrefix.length() > 0) {
                  packagePrefix.deleteCharAt(packagePrefix.length()-1);
                  if (null != streamManager) {
                    streamManager.associatePackagePath(moduleName.getText(),packagePrefix.toString());
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
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(GomModuleName, "GomModuleName"), root_1);

                adaptor.addChild(root_1, stream_moduleName.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end modulename

    public static class imports_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start imports
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:77:1: imports : IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) ;
    public final imports_return imports() throws RecognitionException {
        imports_return retval = new imports_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token IMPORTS5=null;
        importedModuleName_return importedModuleName6 = null;


        GomTree IMPORTS5_tree=null;
        RewriteRuleTokenStream stream_IMPORTS=new RewriteRuleTokenStream(adaptor,"token IMPORTS");
        RewriteRuleSubtreeStream stream_importedModuleName=new RewriteRuleSubtreeStream(adaptor,"rule importedModuleName");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:3: ( IMPORTS ( importedModuleName )* -> ^( Imports ^( ConcImportedModule ( importedModuleName )* ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:3: IMPORTS ( importedModuleName )*
            {
            IMPORTS5=(Token)input.LT(1);
            match(input,IMPORTS,FOLLOW_IMPORTS_in_imports409); 
            stream_IMPORTS.add(IMPORTS5);

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
            	    pushFollow(FOLLOW_importedModuleName_in_imports412);
            	    importedModuleName6=importedModuleName();
            	    _fsp--;

            	    stream_importedModuleName.add(importedModuleName6.getTree());

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
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(Imports, "Imports"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:46: ^( ConcImportedModule ( importedModuleName )* )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(ConcImportedModule, "ConcImportedModule"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:78:67: ( importedModuleName )*
                while ( stream_importedModuleName.hasNext() ) {
                    adaptor.addChild(root_2, stream_importedModuleName.next());

                }
                stream_importedModuleName.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end imports

    public static class importedModuleName_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start importedModuleName
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:80:1: importedModuleName : ID -> ^( Import ^( GomModuleName ID ) ) ;
    public final importedModuleName_return importedModuleName() throws RecognitionException {
        importedModuleName_return retval = new importedModuleName_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID7=null;

        GomTree ID7_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:81:3: ( ID -> ^( Import ^( GomModuleName ID ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:81:3: ID
            {
            ID7=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_importedModuleName441); 
            stream_ID.add(ID7);


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
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(Import, "Import"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:81:18: ^( GomModuleName ID )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(GomModuleName, "GomModuleName"), root_2);

                adaptor.addChild(root_2, stream_ID.next());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end importedModuleName

    public static class section_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start section
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:84:1: section : ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) ;
    public final section_return section() throws RecognitionException {
        section_return retval = new section_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token PUBLIC8=null;
        adtgrammar_return adtgrammar9 = null;


        GomTree PUBLIC8_tree=null;
        RewriteRuleTokenStream stream_PUBLIC=new RewriteRuleTokenStream(adaptor,"token PUBLIC");
        RewriteRuleSubtreeStream stream_adtgrammar=new RewriteRuleSubtreeStream(adaptor,"rule adtgrammar");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:85:3: ( ( PUBLIC )? adtgrammar -> ^( Public adtgrammar ) )
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
                    PUBLIC8=(Token)input.LT(1);
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_section467); 
                    stream_PUBLIC.add(PUBLIC8);


                    }
                    break;

            }

            pushFollow(FOLLOW_adtgrammar_in_section471);
            adtgrammar9=adtgrammar();
            _fsp--;

            stream_adtgrammar.add(adtgrammar9.getTree());

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
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(Public, "Public"), root_1);

                adaptor.addChild(root_1, stream_adtgrammar.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end section

    public static class adtgrammar_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start adtgrammar
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:88:1: adtgrammar : (gr+= sortdef | gr+= syntax )+ -> $gr;
    public final adtgrammar_return adtgrammar() throws RecognitionException {
        adtgrammar_return retval = new adtgrammar_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        List list_gr=null;
        RuleReturnScope gr = null;
        RewriteRuleSubtreeStream stream_sortdef=new RewriteRuleSubtreeStream(adaptor,"rule sortdef");
        RewriteRuleSubtreeStream stream_syntax=new RewriteRuleSubtreeStream(adaptor,"rule syntax");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:89:3: ( (gr+= sortdef | gr+= syntax )+ -> $gr)
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:89:3: (gr+= sortdef | gr+= syntax )+
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:89:3: (gr+= sortdef | gr+= syntax )+
            int cnt5=0;
            loop5:
            do {
                int alt5=3;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==SORTS) ) {
                    alt5=1;
                }
                else if ( (LA5_0==ABSTRACT) ) {
                    alt5=2;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:89:4: gr+= sortdef
            	    {
            	    pushFollow(FOLLOW_sortdef_in_adtgrammar495);
            	    gr=sortdef();
            	    _fsp--;

            	    stream_sortdef.add(gr.getTree());
            	    if (list_gr==null) list_gr=new ArrayList();
            	    list_gr.add(gr);


            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:89:18: gr+= syntax
            	    {
            	    pushFollow(FOLLOW_syntax_in_adtgrammar501);
            	    gr=syntax();
            	    _fsp--;

            	    stream_syntax.add(gr.getTree());
            	    if (list_gr==null) list_gr=new ArrayList();
            	    list_gr.add(gr);


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
            // 89:31: -> $gr
            {
                adaptor.addChild(root_0, ((ParserRuleReturnScope)stream_gr.next()).getTree());

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end adtgrammar

    public static class sortdef_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start sortdef
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:92:1: sortdef : SORTS ( type )* -> ^( ConcGrammar ^( Sorts ( type )* ) ) ;
    public final sortdef_return sortdef() throws RecognitionException {
        sortdef_return retval = new sortdef_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token SORTS10=null;
        type_return type11 = null;


        GomTree SORTS10_tree=null;
        RewriteRuleTokenStream stream_SORTS=new RewriteRuleTokenStream(adaptor,"token SORTS");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:93:3: ( SORTS ( type )* -> ^( ConcGrammar ^( Sorts ( type )* ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:93:3: SORTS ( type )*
            {
            SORTS10=(Token)input.LT(1);
            match(input,SORTS,FOLLOW_SORTS_in_sortdef521); 
            stream_SORTS.add(SORTS10);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:93:9: ( type )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==ID) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:93:10: type
            	    {
            	    pushFollow(FOLLOW_type_in_sortdef524);
            	    type11=type();
            	    _fsp--;

            	    stream_type.add(type11.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            // AST REWRITE
            // elements: type
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 93:17: -> ^( ConcGrammar ^( Sorts ( type )* ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:93:20: ^( ConcGrammar ^( Sorts ( type )* ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(ConcGrammar, "ConcGrammar"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:93:34: ^( Sorts ( type )* )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(Sorts, "Sorts"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:93:42: ( type )*
                while ( stream_type.hasNext() ) {
                    adaptor.addChild(root_2, stream_type.next());

                }
                stream_type.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end sortdef

    public static class type_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start type
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:96:1: type : ID -> ^( GomType ID ) ;
    public final type_return type() throws RecognitionException {
        type_return retval = new type_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID12=null;

        GomTree ID12_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:97:3: ( ID -> ^( GomType ID ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:97:3: ID
            {
            ID12=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_type554); 
            stream_ID.add(ID12);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 97:6: -> ^( GomType ID )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:97:9: ^( GomType ID )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(GomType, "GomType"), root_1);

                adaptor.addChild(root_1, stream_ID.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end type

    public static class syntax_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start syntax
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:99:1: syntax : ABSTRACT SYNTAX (gr+= production | gr+= hookConstruct | gr+= typedecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr)* ) ) ) ;
    public final syntax_return syntax() throws RecognitionException {
        syntax_return retval = new syntax_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ABSTRACT13=null;
        Token SYNTAX14=null;
        List list_gr=null;
        RuleReturnScope gr = null;
        GomTree ABSTRACT13_tree=null;
        GomTree SYNTAX14_tree=null;
        RewriteRuleTokenStream stream_ABSTRACT=new RewriteRuleTokenStream(adaptor,"token ABSTRACT");
        RewriteRuleTokenStream stream_SYNTAX=new RewriteRuleTokenStream(adaptor,"token SYNTAX");
        RewriteRuleSubtreeStream stream_hookConstruct=new RewriteRuleSubtreeStream(adaptor,"rule hookConstruct");
        RewriteRuleSubtreeStream stream_typedecl=new RewriteRuleSubtreeStream(adaptor,"rule typedecl");
        RewriteRuleSubtreeStream stream_production=new RewriteRuleSubtreeStream(adaptor,"rule production");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:100:3: ( ABSTRACT SYNTAX (gr+= production | gr+= hookConstruct | gr+= typedecl )* -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr)* ) ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:100:3: ABSTRACT SYNTAX (gr+= production | gr+= hookConstruct | gr+= typedecl )*
            {
            ABSTRACT13=(Token)input.LT(1);
            match(input,ABSTRACT,FOLLOW_ABSTRACT_in_syntax572); 
            stream_ABSTRACT.add(ABSTRACT13);

            SYNTAX14=(Token)input.LT(1);
            match(input,SYNTAX,FOLLOW_SYNTAX_in_syntax574); 
            stream_SYNTAX.add(SYNTAX14);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:100:19: (gr+= production | gr+= hookConstruct | gr+= typedecl )*
            loop7:
            do {
                int alt7=4;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==ID) ) {
                    switch ( input.LA(2) ) {
                    case EQUALS:
                        {
                        alt7=3;
                        }
                        break;
                    case COLON:
                        {
                        alt7=2;
                        }
                        break;
                    case LPAREN:
                        {
                        alt7=1;
                        }
                        break;

                    }

                }
                else if ( (LA7_0==MODULE||(LA7_0>=SORT && LA7_0<=OPERATOR)) ) {
                    alt7=2;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:100:20: gr+= production
            	    {
            	    pushFollow(FOLLOW_production_in_syntax579);
            	    gr=production();
            	    _fsp--;

            	    stream_production.add(gr.getTree());
            	    if (list_gr==null) list_gr=new ArrayList();
            	    list_gr.add(gr);


            	    }
            	    break;
            	case 2 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:100:37: gr+= hookConstruct
            	    {
            	    pushFollow(FOLLOW_hookConstruct_in_syntax585);
            	    gr=hookConstruct();
            	    _fsp--;

            	    stream_hookConstruct.add(gr.getTree());
            	    if (list_gr==null) list_gr=new ArrayList();
            	    list_gr.add(gr);


            	    }
            	    break;
            	case 3 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:100:57: gr+= typedecl
            	    {
            	    pushFollow(FOLLOW_typedecl_in_syntax591);
            	    gr=typedecl();
            	    _fsp--;

            	    stream_typedecl.add(gr.getTree());
            	    if (list_gr==null) list_gr=new ArrayList();
            	    list_gr.add(gr);


            	    }
            	    break;

            	default :
            	    break loop7;
                }
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
            // 101:3: -> ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr)* ) ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:101:6: ^( ConcGrammar ^( Grammar ^( ConcProduction ( $gr)* ) ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(ConcGrammar, "ConcGrammar"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:101:20: ^( Grammar ^( ConcProduction ( $gr)* ) )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(Grammar, "Grammar"), root_2);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:101:30: ^( ConcProduction ( $gr)* )
                {
                GomTree root_3 = (GomTree)adaptor.nil();
                root_3 = (GomTree)adaptor.becomeRoot(adaptor.create(ConcProduction, "ConcProduction"), root_3);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:101:47: ( $gr)*
                while ( stream_gr.hasNext() ) {
                    adaptor.addChild(root_3, ((ParserRuleReturnScope)stream_gr.next()).getTree());

                }
                stream_gr.reset();

                adaptor.addChild(root_2, root_3);
                }

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end syntax

    public static class production_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start production
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:104:1: production : ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) ;
    public final production_return production() throws RecognitionException {
        production_return retval = new production_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID15=null;
        Token ARROW17=null;
        fieldlist_return fieldlist16 = null;

        type_return type18 = null;


        GomTree ID15_tree=null;
        GomTree ARROW17_tree=null;
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule fieldlist");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");

        String startLine = ""+input.LT(1).getLine();

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:108:3: ( ID fieldlist ARROW type -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:108:3: ID fieldlist ARROW type
            {
            ID15=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_production633); 
            stream_ID.add(ID15);

            pushFollow(FOLLOW_fieldlist_in_production635);
            fieldlist16=fieldlist();
            _fsp--;

            stream_fieldlist.add(fieldlist16.getTree());
            ARROW17=(Token)input.LT(1);
            match(input,ARROW,FOLLOW_ARROW_in_production637); 
            stream_ARROW.add(ARROW17);

            pushFollow(FOLLOW_type_in_production639);
            type18=type();
            _fsp--;

            stream_type.add(type18.getTree());

            // AST REWRITE
            // elements: ID, ID, type, fieldlist
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 108:27: -> ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:108:30: ^( Production ID fieldlist type ^( Origin ID[startLine] ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                adaptor.addChild(root_1, stream_fieldlist.next());
                adaptor.addChild(root_1, stream_type.next());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:108:61: ^( Origin ID[startLine] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, adaptor.create(ID,startLine));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end production

    public static class typedecl_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start typedecl
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:111:1: typedecl : typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType $typename) $alts) ;
    public final typedecl_return typedecl() throws RecognitionException {
        typedecl_return retval = new typedecl_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token typename=null;
        Token EQUALS19=null;
        alternatives_return alts = null;


        GomTree typename_tree=null;
        GomTree EQUALS19_tree=null;
        RewriteRuleTokenStream stream_EQUALS=new RewriteRuleTokenStream(adaptor,"token EQUALS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_alternatives=new RewriteRuleSubtreeStream(adaptor,"rule alternatives");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:112:3: (typename= ID EQUALS alts= alternatives[typename] -> ^( SortType ^( GomType $typename) $alts) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:112:3: typename= ID EQUALS alts= alternatives[typename]
            {
            typename=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_typedecl673); 
            stream_ID.add(typename);

            EQUALS19=(Token)input.LT(1);
            match(input,EQUALS,FOLLOW_EQUALS_in_typedecl675); 
            stream_EQUALS.add(EQUALS19);

            pushFollow(FOLLOW_alternatives_in_typedecl679);
            alts=alternatives(typename);
            _fsp--;

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
            // 113:5: -> ^( SortType ^( GomType $typename) $alts)
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:8: ^( SortType ^( GomType $typename) $alts)
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(SortType, "SortType"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:113:19: ^( GomType $typename)
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(GomType, "GomType"), root_2);

                adaptor.addChild(root_2, stream_typename.next());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_alts.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end typedecl

    public static class alternatives_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start alternatives
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:116:1: alternatives[Token typename] : ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) ;
    public final alternatives_return alternatives(Token typename) throws RecognitionException {
        alternatives_return retval = new alternatives_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ALT20=null;
        Token ALT22=null;
        Token SEMI24=null;
        opdecl_return opdecl21 = null;

        opdecl_return opdecl23 = null;


        GomTree ALT20_tree=null;
        GomTree ALT22_tree=null;
        GomTree SEMI24_tree=null;
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleTokenStream stream_ALT=new RewriteRuleTokenStream(adaptor,"token ALT");
        RewriteRuleSubtreeStream stream_opdecl=new RewriteRuleSubtreeStream(adaptor,"rule opdecl");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:3: ( ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* ( SEMI )? -> ^( ConcProduction ( opdecl )+ ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:3: ( ALT )? opdecl[typename] ( ALT opdecl[typename] )* ( SEMI )?
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:3: ( ALT )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ALT) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:4: ALT
                    {
                    ALT20=(Token)input.LT(1);
                    match(input,ALT,FOLLOW_ALT_in_alternatives715); 
                    stream_ALT.add(ALT20);


                    }
                    break;

            }

            pushFollow(FOLLOW_opdecl_in_alternatives719);
            opdecl21=opdecl(typename);
            _fsp--;

            stream_opdecl.add(opdecl21.getTree());
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:27: ( ALT opdecl[typename] )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==ALT) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:28: ALT opdecl[typename]
            	    {
            	    ALT22=(Token)input.LT(1);
            	    match(input,ALT,FOLLOW_ALT_in_alternatives723); 
            	    stream_ALT.add(ALT22);

            	    pushFollow(FOLLOW_opdecl_in_alternatives725);
            	    opdecl23=opdecl(typename);
            	    _fsp--;

            	    stream_opdecl.add(opdecl23.getTree());

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:51: ( SEMI )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==SEMI) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:117:52: SEMI
                    {
                    SEMI24=(Token)input.LT(1);
                    match(input,SEMI,FOLLOW_SEMI_in_alternatives731); 
                    stream_SEMI.add(SEMI24);


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
            // 118:3: -> ^( ConcProduction ( opdecl )+ )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:118:6: ^( ConcProduction ( opdecl )+ )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(ConcProduction, "ConcProduction"), root_1);

                if ( !(stream_opdecl.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_opdecl.hasNext() ) {
                    adaptor.addChild(root_1, stream_opdecl.next());

                }
                stream_opdecl.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end alternatives

    public static class opdecl_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start opdecl
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:121:1: opdecl[Token type] : ID fieldlist -> ^( Production ID fieldlist ^( GomType ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final opdecl_return opdecl(Token type) throws RecognitionException {
        opdecl_return retval = new opdecl_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID25=null;
        fieldlist_return fieldlist26 = null;


        GomTree ID25_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_fieldlist=new RewriteRuleSubtreeStream(adaptor,"rule fieldlist");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:122:3: ( ID fieldlist -> ^( Production ID fieldlist ^( GomType ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:122:3: ID fieldlist
            {
            ID25=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_opdecl760); 
            stream_ID.add(ID25);

            pushFollow(FOLLOW_fieldlist_in_opdecl762);
            fieldlist26=fieldlist();
            _fsp--;

            stream_fieldlist.add(fieldlist26.getTree());

            // AST REWRITE
            // elements: ID, ID, ID, fieldlist
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 123:3: -> ^( Production ID fieldlist ^( GomType ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:6: ^( Production ID fieldlist ^( GomType ID[type] ) ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(Production, "Production"), root_1);

                adaptor.addChild(root_1, stream_ID.next());
                adaptor.addChild(root_1, stream_fieldlist.next());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:123:32: ^( GomType ID[type] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(GomType, "GomType"), root_2);

                adaptor.addChild(root_2, adaptor.create(ID,type));

                adaptor.addChild(root_1, root_2);
                }
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:124:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, adaptor.create(ID,""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end opdecl

    public static class fieldlist_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start fieldlist
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:127:1: fieldlist : LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) ;
    public final fieldlist_return fieldlist() throws RecognitionException {
        fieldlist_return retval = new fieldlist_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token LPAREN27=null;
        Token COMMA29=null;
        Token RPAREN31=null;
        field_return field28 = null;

        field_return field30 = null;


        GomTree LPAREN27_tree=null;
        GomTree COMMA29_tree=null;
        GomTree RPAREN31_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:3: ( LPAREN ( field ( COMMA field )* )? RPAREN -> ^( ConcField ( field )* ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:3: LPAREN ( field ( COMMA field )* )? RPAREN
            {
            LPAREN27=(Token)input.LT(1);
            match(input,LPAREN,FOLLOW_LPAREN_in_fieldlist807); 
            stream_LPAREN.add(LPAREN27);

            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:10: ( field ( COMMA field )* )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ID) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:11: field ( COMMA field )*
                    {
                    pushFollow(FOLLOW_field_in_fieldlist810);
                    field28=field();
                    _fsp--;

                    stream_field.add(field28.getTree());
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:17: ( COMMA field )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==COMMA) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:18: COMMA field
                    	    {
                    	    COMMA29=(Token)input.LT(1);
                    	    match(input,COMMA,FOLLOW_COMMA_in_fieldlist813); 
                    	    stream_COMMA.add(COMMA29);

                    	    pushFollow(FOLLOW_field_in_fieldlist815);
                    	    field30=field();
                    	    _fsp--;

                    	    stream_field.add(field30.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);


                    }
                    break;

            }

            RPAREN31=(Token)input.LT(1);
            match(input,RPAREN,FOLLOW_RPAREN_in_fieldlist822); 
            stream_RPAREN.add(RPAREN31);


            // AST REWRITE
            // elements: field
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 128:42: -> ^( ConcField ( field )* )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:45: ^( ConcField ( field )* )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(ConcField, "ConcField"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:128:57: ( field )*
                while ( stream_field.hasNext() ) {
                    adaptor.addChild(root_1, stream_field.next());

                }
                stream_field.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end fieldlist

    public static class arglist_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start arglist
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:130:1: arglist : ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) ;
    public final arglist_return arglist() throws RecognitionException {
        arglist_return retval = new arglist_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token LPAREN32=null;
        Token COMMA34=null;
        Token RPAREN36=null;
        arg_return arg33 = null;

        arg_return arg35 = null;


        GomTree LPAREN32_tree=null;
        GomTree COMMA34_tree=null;
        GomTree RPAREN36_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_arg=new RewriteRuleSubtreeStream(adaptor,"rule arg");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:3: ( ( LPAREN ( arg ( COMMA arg )* )? RPAREN )? -> ^( ConcArg ( arg )* ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:3: ( LPAREN ( arg ( COMMA arg )* )? RPAREN )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==LPAREN) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:4: LPAREN ( arg ( COMMA arg )* )? RPAREN
                    {
                    LPAREN32=(Token)input.LT(1);
                    match(input,LPAREN,FOLLOW_LPAREN_in_arglist844); 
                    stream_LPAREN.add(LPAREN32);

                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:11: ( arg ( COMMA arg )* )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==ID) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:12: arg ( COMMA arg )*
                            {
                            pushFollow(FOLLOW_arg_in_arglist847);
                            arg33=arg();
                            _fsp--;

                            stream_arg.add(arg33.getTree());
                            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:16: ( COMMA arg )*
                            loop13:
                            do {
                                int alt13=2;
                                int LA13_0 = input.LA(1);

                                if ( (LA13_0==COMMA) ) {
                                    alt13=1;
                                }


                                switch (alt13) {
                            	case 1 :
                            	    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:131:17: COMMA arg
                            	    {
                            	    COMMA34=(Token)input.LT(1);
                            	    match(input,COMMA,FOLLOW_COMMA_in_arglist850); 
                            	    stream_COMMA.add(COMMA34);

                            	    pushFollow(FOLLOW_arg_in_arglist852);
                            	    arg35=arg();
                            	    _fsp--;

                            	    stream_arg.add(arg35.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop13;
                                }
                            } while (true);


                            }
                            break;

                    }

                    RPAREN36=(Token)input.LT(1);
                    match(input,RPAREN,FOLLOW_RPAREN_in_arglist859); 
                    stream_RPAREN.add(RPAREN36);


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
            // 132:3: -> ^( ConcArg ( arg )* )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:132:6: ^( ConcArg ( arg )* )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(ConcArg, "ConcArg"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:132:16: ( arg )*
                while ( stream_arg.hasNext() ) {
                    adaptor.addChild(root_1, stream_arg.next());

                }
                stream_arg.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end arglist

    public static class arg_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start arg
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:135:1: arg : ID -> ^( Arg ID ) ;
    public final arg_return arg() throws RecognitionException {
        arg_return retval = new arg_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token ID37=null;

        GomTree ID37_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:135:7: ( ID -> ^( Arg ID ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:135:7: ID
            {
            ID37=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_arg886); 
            stream_ID.add(ID37);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (GomTree)adaptor.nil();
            // 135:10: -> ^( Arg ID )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:135:13: ^( Arg ID )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(Arg, "Arg"), root_1);

                adaptor.addChild(root_1, stream_ID.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end arg

    public static class hookConstruct_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start hookConstruct
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:137:1: hookConstruct : (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) ;
    public final hookConstruct_return hookConstruct() throws RecognitionException {
        hookConstruct_return retval = new hookConstruct_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token pointCut=null;
        Token hookType=null;
        Token COLON38=null;
        Token LBRACE40=null;
        hookScope_return hscope = null;

        arglist_return arglist39 = null;


        GomTree pointCut_tree=null;
        GomTree hookType_tree=null;
        GomTree COLON38_tree=null;
        GomTree LBRACE40_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_hookScope=new RewriteRuleSubtreeStream(adaptor,"rule hookScope");
        RewriteRuleSubtreeStream stream_arglist=new RewriteRuleSubtreeStream(adaptor,"rule arglist");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:138:3: ( (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) ) )
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:138:3: (hscope= hookScope )? pointCut= ID COLON hookType= ID arglist LBRACE
            {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:138:3: (hscope= hookScope )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==MODULE||(LA16_0>=SORT && LA16_0<=OPERATOR)) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:138:4: hscope= hookScope
                    {
                    pushFollow(FOLLOW_hookScope_in_hookConstruct907);
                    hscope=hookScope();
                    _fsp--;

                    stream_hookScope.add(hscope.getTree());

                    }
                    break;

            }

            pointCut=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_hookConstruct913); 
            stream_ID.add(pointCut);

            COLON38=(Token)input.LT(1);
            match(input,COLON,FOLLOW_COLON_in_hookConstruct915); 
            stream_COLON.add(COLON38);

            hookType=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_hookConstruct919); 
            stream_ID.add(hookType);

            pushFollow(FOLLOW_arglist_in_hookConstruct921);
            arglist39=arglist();
            _fsp--;

            stream_arglist.add(arglist39.getTree());
            LBRACE40=(Token)input.LT(1);
            match(input,LBRACE,FOLLOW_LBRACE_in_hookConstruct923); 
            stream_LBRACE.add(LBRACE40);


            // AST REWRITE
            // elements: pointCut, hookType, hscope, hookType, LBRACE, pointCut, ID, LBRACE, arglist, ID, arglist
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
            // 139:3: -> {hscope!=null}? ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            if (hscope!=null) {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:139:22: ^( Hook $hscope $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(Hook, "Hook"), root_1);

                adaptor.addChild(root_1, stream_hscope.next());
                adaptor.addChild(root_1, stream_pointCut.next());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:139:47: ^( HookKind $hookType)
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.next());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.next());
                adaptor.addChild(root_1, stream_LBRACE.next());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:140:24: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, adaptor.create(ID,""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 141:3: -> ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
            {
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:141:6: ^( Hook ^( KindOperator ) $pointCut ^( HookKind $hookType) arglist LBRACE ^( Origin ID[\"\"+input.LT(1).getLine()] ) )
                {
                GomTree root_1 = (GomTree)adaptor.nil();
                root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(Hook, "Hook"), root_1);

                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:141:13: ^( KindOperator )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(KindOperator, "KindOperator"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_pointCut.next());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:141:39: ^( HookKind $hookType)
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(HookKind, "HookKind"), root_2);

                adaptor.addChild(root_2, stream_hookType.next());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_arglist.next());
                adaptor.addChild(root_1, stream_LBRACE.next());
                // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:142:7: ^( Origin ID[\"\"+input.LT(1).getLine()] )
                {
                GomTree root_2 = (GomTree)adaptor.nil();
                root_2 = (GomTree)adaptor.becomeRoot(adaptor.create(Origin, "Origin"), root_2);

                adaptor.addChild(root_2, adaptor.create(ID,""+input.LT(1).getLine()));

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (GomTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end hookConstruct

    public static class hookScope_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start hookScope
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:146:1: hookScope : ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) );
    public final hookScope_return hookScope() throws RecognitionException {
        hookScope_return retval = new hookScope_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token SORT41=null;
        Token MODULE42=null;
        Token OPERATOR43=null;

        GomTree SORT41_tree=null;
        GomTree MODULE42_tree=null;
        GomTree OPERATOR43_tree=null;
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");
        RewriteRuleTokenStream stream_SORT=new RewriteRuleTokenStream(adaptor,"token SORT");
        RewriteRuleTokenStream stream_MODULE=new RewriteRuleTokenStream(adaptor,"token MODULE");

        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:147:3: ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) )
            int alt17=3;
            switch ( input.LA(1) ) {
            case SORT:
                {
                alt17=1;
                }
                break;
            case MODULE:
                {
                alt17=2;
                }
                break;
            case OPERATOR:
                {
                alt17=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("146:1: hookScope : ( SORT -> ^( KindSort ) | MODULE -> ^( KindModule ) | OPERATOR -> ^( KindOperator ) );", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:147:3: SORT
                    {
                    SORT41=(Token)input.LT(1);
                    match(input,SORT,FOLLOW_SORT_in_hookScope1036); 
                    stream_SORT.add(SORT41);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 147:8: -> ^( KindSort )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:147:11: ^( KindSort )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(KindSort, "KindSort"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:148:5: MODULE
                    {
                    MODULE42=(Token)input.LT(1);
                    match(input,MODULE,FOLLOW_MODULE_in_hookScope1048); 
                    stream_MODULE.add(MODULE42);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 148:12: -> ^( KindModule )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:148:15: ^( KindModule )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(KindModule, "KindModule"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 3 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:149:5: OPERATOR
                    {
                    OPERATOR43=(Token)input.LT(1);
                    match(input,OPERATOR,FOLLOW_OPERATOR_in_hookScope1060); 
                    stream_OPERATOR.add(OPERATOR43);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 149:14: -> ^( KindOperator )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:149:17: ^( KindOperator )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(KindOperator, "KindOperator"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }



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
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end hookScope

    public static class field_return extends ParserRuleReturnScope {
        GomTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start field
    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:152:1: field : ( type STAR -> ^( StarredField type ) | ID COLON type -> ^( NamedField ID type ) );
    public final field_return field() throws RecognitionException {
        field_return retval = new field_return();
        retval.start = input.LT(1);

        GomTree root_0 = null;

        Token STAR45=null;
        Token ID46=null;
        Token COLON47=null;
        type_return type44 = null;

        type_return type48 = null;


        GomTree STAR45_tree=null;
        GomTree ID46_tree=null;
        GomTree COLON47_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        try {
            // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:153:3: ( type STAR -> ^( StarredField type ) | ID COLON type -> ^( NamedField ID type ) )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==ID) ) {
                int LA18_1 = input.LA(2);

                if ( (LA18_1==COLON) ) {
                    alt18=2;
                }
                else if ( (LA18_1==STAR) ) {
                    alt18=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("152:1: field : ( type STAR -> ^( StarredField type ) | ID COLON type -> ^( NamedField ID type ) );", 18, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("152:1: field : ( type STAR -> ^( StarredField type ) | ID COLON type -> ^( NamedField ID type ) );", 18, 0, input);

                throw nvae;
            }
            switch (alt18) {
                case 1 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:153:3: type STAR
                    {
                    pushFollow(FOLLOW_type_in_field1079);
                    type44=type();
                    _fsp--;

                    stream_type.add(type44.getTree());
                    STAR45=(Token)input.LT(1);
                    match(input,STAR,FOLLOW_STAR_in_field1081); 
                    stream_STAR.add(STAR45);


                    // AST REWRITE
                    // elements: type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 153:13: -> ^( StarredField type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:153:16: ^( StarredField type )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(StarredField, "StarredField"), root_1);

                        adaptor.addChild(root_1, stream_type.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



                    }
                    break;
                case 2 :
                    // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:154:5: ID COLON type
                    {
                    ID46=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_field1095); 
                    stream_ID.add(ID46);

                    COLON47=(Token)input.LT(1);
                    match(input,COLON,FOLLOW_COLON_in_field1097); 
                    stream_COLON.add(COLON47);

                    pushFollow(FOLLOW_type_in_field1099);
                    type48=type();
                    _fsp--;

                    stream_type.add(type48.getTree());

                    // AST REWRITE
                    // elements: ID, type
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (GomTree)adaptor.nil();
                    // 154:19: -> ^( NamedField ID type )
                    {
                        // /Users/pem/workspace/jtom/src/gen/tom/gom/parser/GomLanguage.g:154:22: ^( NamedField ID type )
                        {
                        GomTree root_1 = (GomTree)adaptor.nil();
                        root_1 = (GomTree)adaptor.becomeRoot(adaptor.create(NamedField, "NamedField"), root_1);

                        adaptor.addChild(root_1, stream_ID.next());
                        adaptor.addChild(root_1, stream_type.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }



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
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end field


 

    public static final BitSet FOLLOW_MODULE_in_module301 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_modulename_in_module303 = new BitSet(new long[]{0x0000000000000000L,0x0000000003C00000L});
    public static final BitSet FOLLOW_imports_in_module308 = new BitSet(new long[]{0x0000000000000000L,0x0000000003800000L});
    public static final BitSet FOLLOW_section_in_module312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_modulename369 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_DOT_in_modulename371 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_modulename381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORTS_in_imports409 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_importedModuleName_in_imports412 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_importedModuleName441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_section467 = new BitSet(new long[]{0x0000000000000000L,0x0000000003000000L});
    public static final BitSet FOLLOW_adtgrammar_in_section471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sortdef_in_adtgrammar495 = new BitSet(new long[]{0x0000000000000002L,0x0000000003000000L});
    public static final BitSet FOLLOW_syntax_in_adtgrammar501 = new BitSet(new long[]{0x0000000000000002L,0x0000000003000000L});
    public static final BitSet FOLLOW_SORTS_in_sortdef521 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_type_in_sortdef524 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_type554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_syntax572 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_SYNTAX_in_syntax574 = new BitSet(new long[]{0x0000000000000002L,0x0000003000180000L});
    public static final BitSet FOLLOW_production_in_syntax579 = new BitSet(new long[]{0x0000000000000002L,0x0000003000180000L});
    public static final BitSet FOLLOW_hookConstruct_in_syntax585 = new BitSet(new long[]{0x0000000000000002L,0x0000003000180000L});
    public static final BitSet FOLLOW_typedecl_in_syntax591 = new BitSet(new long[]{0x0000000000000002L,0x0000003000180000L});
    public static final BitSet FOLLOW_ID_in_production633 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_fieldlist_in_production635 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_ARROW_in_production637 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_type_in_production639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_typedecl673 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_EQUALS_in_typedecl675 = new BitSet(new long[]{0x0000000000000000L,0x0000000020100000L});
    public static final BitSet FOLLOW_alternatives_in_typedecl679 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALT_in_alternatives715 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives719 = new BitSet(new long[]{0x0000000000000002L,0x0000000060000000L});
    public static final BitSet FOLLOW_ALT_in_alternatives723 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_opdecl_in_alternatives725 = new BitSet(new long[]{0x0000000000000002L,0x0000000060000000L});
    public static final BitSet FOLLOW_SEMI_in_alternatives731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_opdecl760 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_fieldlist_in_opdecl762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_fieldlist807 = new BitSet(new long[]{0x0000000000000000L,0x0000000200100000L});
    public static final BitSet FOLLOW_field_in_fieldlist810 = new BitSet(new long[]{0x0000000000000000L,0x0000000300000000L});
    public static final BitSet FOLLOW_COMMA_in_fieldlist813 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_field_in_fieldlist815 = new BitSet(new long[]{0x0000000000000000L,0x0000000300000000L});
    public static final BitSet FOLLOW_RPAREN_in_fieldlist822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arglist844 = new BitSet(new long[]{0x0000000000000000L,0x0000000200100000L});
    public static final BitSet FOLLOW_arg_in_arglist847 = new BitSet(new long[]{0x0000000000000000L,0x0000000300000000L});
    public static final BitSet FOLLOW_COMMA_in_arglist850 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_arg_in_arglist852 = new BitSet(new long[]{0x0000000000000000L,0x0000000300000000L});
    public static final BitSet FOLLOW_RPAREN_in_arglist859 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_arg886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_hookScope_in_hookConstruct907 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct913 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
    public static final BitSet FOLLOW_COLON_in_hookConstruct915 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_hookConstruct919 = new BitSet(new long[]{0x0000000000000000L,0x0000000880000000L});
    public static final BitSet FOLLOW_arglist_in_hookConstruct921 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_LBRACE_in_hookConstruct923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SORT_in_hookScope1036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MODULE_in_hookScope1048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATOR_in_hookScope1060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_field1079 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_STAR_in_field1081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field1095 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
    public static final BitSet FOLLOW_COLON_in_field1097 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_type_in_field1099 = new BitSet(new long[]{0x0000000000000002L});

}