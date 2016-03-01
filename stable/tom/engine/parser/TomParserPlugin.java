/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.parser;

import java.io.Reader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.HashSet;
import java.util.Iterator;

import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.exception.TomException;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.code.types.*;
import tom.engine.adt.cst.types.*;

import tom.engine.adt.cst.CSTAdaptor;

import aterm.ATerm;
import tom.library.sl.*;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamSelector;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;
/**
 * The TomParser "plugin"
 * The responsability of the plugin is to parse the input file and create the
 * corresponding TomTerm.
 * It get the input file from the TomStreamManger and parse it
 */
public class TomParserPlugin extends TomGenericPlugin {
          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_BottomUp( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), tom.library.sl.Sequence.make(v, null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}    

  
  /** some output suffixes */
  public static final String PARSED_SUFFIX = ".tfix.parsed";
  public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table";

  /** the declared options string*/
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='parse' altName='' description='Parser (activated by default)' value='true'/>" +
    "<boolean name='newparser' altName='np' description='New Parser (not activated by default)' value='false'/>" +
    "<boolean name='printcst' altName='cst' description='print post-parsing cst (only with new parser)' value='false'/>" +
    "<boolean name='printast' altName='ast' description='print post-parsing ast' value='false'/>" +
    "</options>";
  
  /** input file name and stream */
  private String currentFileName;
  private Reader currentReader;
  
  /** the main HostParser */
  private tom.engine.parser.antlr2.HostParser parser = null;
  
  /** Constructor */
  public TomParserPlugin(){
    super("TomParserPlugin");
  }
  
  //creating a new Host parser
  public static tom.engine.parser.antlr2.HostParser newParser(Reader reader, String fileName,
                                        OptionManager optionManager,
                                        TomStreamManager tomStreamManager)
    throws FileNotFoundException,IOException {
    HashSet<String> includedFiles = new HashSet<String>();
    HashSet<String> alreadyParsedFiles = new HashSet<String>();
    return newParser(reader,fileName,
                     includedFiles,alreadyParsedFiles,
                     optionManager, tomStreamManager);
  }
  
  public static tom.engine.parser.antlr2.HostParser newParser(Reader reader,String fileName,
                                        HashSet<String> includedFiles,
                                        HashSet<String> alreadyParsedFiles,
                                        OptionManager optionManager,
                                        TomStreamManager tomStreamManager)
    throws FileNotFoundException,IOException {
    // a selector to choose the lexer to use
    TokenStreamSelector selector = new TokenStreamSelector();
    // create a lexer for target mode
    tom.engine.parser.antlr2.HostLexer targetlexer = new tom.engine.parser.antlr2.HostLexer(reader);
    // create a lexer for tom mode
    tom.engine.parser.antlr2.TomLexer tomlexer = new tom.engine.parser.antlr2.TomLexer(targetlexer.getInputState());
    // create a lexer for backquote mode
    tom.engine.parser.antlr2.BackQuoteLexer bqlexer = new tom.engine.parser.antlr2.BackQuoteLexer(targetlexer.getInputState());
    // notify selector about various lexers
    selector.addInputStream(targetlexer,"targetlexer");
    selector.addInputStream(tomlexer, "tomlexer");
    selector.addInputStream(bqlexer, "bqlexer");
    selector.select("targetlexer");
    // create the parser for target mode
    // also create tom parser and backquote parser
    return new tom.engine.parser.antlr2.HostParser(selector, fileName,
        includedFiles, alreadyParsedFiles,
        optionManager, tomStreamManager);
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the StreamManager from which we can get the input
   */
  public void setArgs(Object[] arg){
    //System.out.println("(DEBUG) Old parser");
    if (arg[0] instanceof TomStreamManager) {
      setStreamManager((TomStreamManager)arg[0]);
      currentFileName = getStreamManager().getInputFileName();  
      currentReader = getStreamManager().getInputReader();
    } else {
      System.out.println("(DEBUG) erreur old parser");
      TomMessage.error(getLogger(), null, 0, TomMessage.invalidPluginArgument,
          "TomParserPlugin", "[TomStreamManager]", getArgumentArrayString(arg));
    }
  }

  /**
   * inherited from plugin interface
   * Parse the input ans set the "Working" TomTerm to be compiled.
   */
  public synchronized void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();
    boolean java         = ((Boolean)getOptionManager().getOptionValue("jCode")).booleanValue();
    boolean eclipse      = ((Boolean)getOptionManager().getOptionValue("eclipse")).booleanValue();
    boolean newparser    = ((Boolean)getOptionManager().getOptionValue("newparser")).booleanValue();
    boolean printcst     = ((Boolean)getOptionManager().getOptionValue("printcst")).booleanValue();
    boolean printast     = ((Boolean)getOptionManager().getOptionValue("printast")).booleanValue();

    SymbolTable symbolTable = getStreamManager().getSymbolTable();
    if(newparser==false) {
      try {
        // looking for java package
        if(java && (!currentFileName.equals("-"))) {
          /* Do not exhaust the stream !! */
          tom.engine.parser.antlr2.TomJavaParser javaParser = tom.engine.parser.antlr2.TomJavaParser.createParser(currentFileName);
          String packageName = "";
          try {
            packageName = javaParser.javaPackageDeclaration();
          } catch (TokenStreamException tse) {
            /* no package was found: ignore */
          }
          // Update streamManager to take into account package information
          getStreamManager().setPackagePath(packageName);
        }

        // getting a parser 
        parser = newParser(currentReader, currentFileName, getOptionManager(), getStreamManager());
        // parsing

        setWorkingTerm(parser.input());
        /*
         * we update codomains which are constrained by a symbolName
         * (come from the %strategy operator)
         */
        Iterator it = symbolTable.keySymbolIterator();
        while(it.hasNext()) {
          String tomName = (String)it.next();
          TomSymbol tomSymbol = getSymbolFromName(tomName);
          tomSymbol = symbolTable.updateConstrainedSymbolCodomain(tomSymbol, symbolTable);

          if(printast) {
            printTree(tomSymbol);
          }
        }

          if(printast) {
            printTree((Visitable)getWorkingTerm());
          }

        // verbose
        TomMessage.info(getLogger(), currentFileName, getLineFromTomParser(), TomMessage.tomParsingPhase,
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      } catch (TokenStreamException e) {
        TomMessage.error(getLogger(), currentFileName, getLineFromTomParser(),
            TomMessage.tokenStreamException, e.getMessage());
        return;
      } catch (RecognitionException e){
        TomMessage.error(getLogger(), currentFileName, getLineFromTomParser(), 
            TomMessage.recognitionException, e.getMessage());
        return;
      } catch (TomException e) {
        TomMessage.error(getLogger(), currentFileName, getLineFromTomParser(), 
            e.getPlatformMessage(), e.getParameters());
        return;
      } catch (FileNotFoundException e) {
        TomMessage.error(getLogger(), currentFileName, 0, TomMessage.fileNotFound); 
        e.printStackTrace();
        return;
      } catch (Exception e) {
        e.printStackTrace();
        TomMessage.error(getLogger(), currentFileName, 0, TomMessage.exceptionMessage,
            getClass().getName(), currentFileName);
        return;
      }
      // Some extra stuff
      if(eclipse) {
        String outputFileName = getStreamManager().getInputParentFile()+
          File.separator + "."+
          getStreamManager().getRawFileName()+ PARSED_TABLE_SUFFIX;
        Tools.generateOutput(outputFileName, getStreamManager().getSymbolTable().toTerm().toATerm());
      }
    } else {
      try {
        if(!currentFileName.equals("-")) {
          tom.engine.parser.antlr3.HostParser parser = 
            new tom.engine.parser.antlr3.HostParser(getStreamManager(), getOptionManager());
          ANTLRReaderStream input = new ANTLRReaderStream(currentReader);
          input.name = currentFileName;
          System.out.println("CurrentFileName : "+currentFileName);

          long start = System.currentTimeMillis();
          Tree programAsAntrlTree = parser.parseProgram(input);
          System.out.println("parsing antlr3 = " + (System.currentTimeMillis()-start) + " ms");

          CstProgram cst = (CstProgram)CSTAdaptor.getTerm(programAsAntrlTree);

          if(printcst) {
            printTree(cst);
          }

          tom.engine.parser.antlr3.CstConverter converter = new tom.engine.parser.antlr3.CstConverter(symbolTable);
          Code code = converter.convert(cst);
          setWorkingTerm(code);

          Iterator it = symbolTable.keySymbolIterator();
          while(it.hasNext()) {
            String tomName = (String)it.next();
            TomSymbol tomSymbol = getSymbolFromName(tomName);
            if(printast) {
              printTree(tomSymbol);
            }
          }

          if(printast) {
            printTree((Visitable)getWorkingTerm());
          }
        }

        // verbose
        TomMessage.info(getLogger(), currentFileName, getLineFromTomParser(),
            TomMessage.tomParsingPhase,
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      } catch(IOException e) {
         TomMessage.error(getLogger(), currentFileName, -1,
             TomMessage.fileNotFound, e.getMessage());// TODO custom ErrMessage
       }
    }

    if(intermediate) {
      Tools.generateOutput(getStreamManager().getOutputFileName() 
          + PARSED_SUFFIX, (tom.library.sl.Visitable)getWorkingTerm());
      Tools.generateOutput(getStreamManager().getOutputFileName() 
          + PARSED_TABLE_SUFFIX, getStreamManager().getSymbolTable().toTerm().toATerm());
    }

  }
  
  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomParserPlugin.DECLARED_OPTIONS);
  }

  /**
   * return the last line number
   */
  private int getLineFromTomParser() {
    if(parser == null) {
      return TomMessage.DEFAULT_ERROR_LINE_NUMBER;
    } 
    return parser.getLine();
  }

  private void printTree(Visitable tree) {
    if(tree!=null) {
      try {
        tree = tom_make_BottomUp(tom_make_ToSingleLineTargetLanguage()).visit(tree);
        tom.library.utils.Viewer.toTree(tree);
      } catch (tom.library.sl.VisitFailure e) {
        System.err.println("VisitFailure Exception"); //XXX handle cleanly
      }
    } else {
      System.out.println("Nothing to print (tree is null)");
    }
  }

  /**
   * Change every hostCode block so it's on a single line.
   * Make printed tree more easily readable.
   */
  public static class ToSingleLineTargetLanguage extends tom.library.sl.AbstractStrategyBasic {public ToSingleLineTargetLanguage() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.TargetLanguage) ) {return ((T)visit_TargetLanguage((( tom.engine.adt.code.types.TargetLanguage )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.TargetLanguage  _visit_TargetLanguage( tom.engine.adt.code.types.TargetLanguage  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.TargetLanguage )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.TargetLanguage  visit_TargetLanguage( tom.engine.adt.code.types.TargetLanguage  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )(( tom.engine.adt.code.types.TargetLanguage )tom__arg)) instanceof tom.engine.adt.code.types.targetlanguage.TL) ) {


        return  tom.engine.adt.code.types.targetlanguage.TL.make(formatTargetLanguageString( (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getCode() ),  (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getStart() ,  (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getEnd() ) ;
      }}}}{if ( (tom__arg instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )(( tom.engine.adt.code.types.TargetLanguage )tom__arg)) instanceof tom.engine.adt.code.types.targetlanguage.ITL) ) {

        return  tom.engine.adt.code.types.targetlanguage.ITL.make(formatTargetLanguageString( (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getCode() )) ;
      }}}}{if ( (tom__arg instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )(( tom.engine.adt.code.types.TargetLanguage )tom__arg)) instanceof tom.engine.adt.code.types.targetlanguage.Comment) ) {

        return  tom.engine.adt.code.types.targetlanguage.Comment.make(formatTargetLanguageString( (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getCode() )) ;
      }}}}}return _visit_TargetLanguage(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ToSingleLineTargetLanguage() { return new ToSingleLineTargetLanguage();}










  /**
   * Usefull to print every hostCode on a single line in tree.
   * Improve readability.
   */
  private static String formatTargetLanguageString(String s) {
    s = s.replaceAll("\n", "\\\\n");
    s = s.replaceAll("\r", "\\\\r");
    s = s.replaceAll("\t", "\\\\t");
    return "["+s+"]";
  }

} //class TomParserPlugin
