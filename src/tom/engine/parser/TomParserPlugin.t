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

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


/**
 * The TomParser "plugin"
 * The responsability of the plugin is to parse the input file and create the
 * corresponding TomTerm.
 * It get the input file from the TomStreamManger and parse it
 */
public class TomParserPlugin extends TomGenericPlugin {
  %include {sl.tom}
  %include {../adt/tomsignature/TomSignature.tom}
  
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
  public TomParserPlugin() {
    super("TomParserPlugin");
  }
  
  //creating a new Host parser
  /*
  public static tom.engine.parser.antlr2.HostParser newParser(Reader reader, String filename,
                                        OptionManager optionManager,
                                        TomStreamManager tomStreamManager)
    throws FileNotFoundException,IOException {
    HashSet<String> includedFiles = new HashSet<String>();
    HashSet<String> alreadyParsedFiles = new HashSet<String>();
    return newParser(reader,filename,
                     includedFiles,alreadyParsedFiles,
                     optionManager, tomStreamManager);
  }
  */

  public static tom.engine.parser.antlr2.HostParser newParser(Reader reader,String filename,
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
    return new tom.engine.parser.antlr2.HostParser(selector, filename,
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
      System.out.println("(DEBUG) error old parser");
      TomMessage.error(getLogger(), null, 0, TomMessage.invalidPluginArgument,
          "TomParserPlugin", "[TomStreamManager]", getArgumentArrayString(arg));
    }
  }

  /**
   * inherited from plugin interface
   * Parse the input and set the "Working" TomTerm to be compiled.
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
        HashSet<String> includedFiles = new HashSet<String>();
        HashSet<String> alreadyParsedFiles = new HashSet<String>();
        parser = newParser(currentReader, currentFileName,includedFiles, alreadyParsedFiles, getOptionManager(), getStreamManager());
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
        Tools.generateOutput(outputFileName, symbolTable.toTerm().toATerm());
      }
    } else {
      try {
        if(!currentFileName.equals("-")) {

          tom.engine.parser.antlr4.TomParser parser = new tom.engine.parser.antlr4.TomParser(currentFileName, getStreamManager(), symbolTable);
          ANTLRInputStream input = new ANTLRInputStream(currentReader);
          CstProgram cst = parser.parse(input);
          if(printcst) {
            printTree(cst);
          }

          long start = System.currentTimeMillis();
          tom.engine.parser.antlr4.AstBuilder astBuilder = new tom.engine.parser.antlr4.AstBuilder(symbolTable);
          Code code = astBuilder.convert(cst);
          System.out.println("\tbuilding ast:" + (System.currentTimeMillis()-start) + " ms");

          //System.out.println("ast = " + code);


          setWorkingTerm(code);
          /*
           * we update codomains which are constrained by a symbolName
           * (come from the %strategy operator)
           */
          Iterator it = symbolTable.keySymbolIterator();
          while(it.hasNext()) {
            String tomName = (String)it.next();
            TomSymbol tomSymbol = getSymbolFromName(tomName);
            //tomSymbol = symbolTable.updateConstrainedSymbolCodomain(tomSymbol, symbolTable);

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
          + PARSED_TABLE_SUFFIX, symbolTable.toTerm().toATerm());
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
        tree = `BottomUp(ToSingleLineTargetLanguage()).visit(tree);
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
  %strategy ToSingleLineTargetLanguage() extends Identity() {
    visit TargetLanguage {
      TL[Code=code, Start=start, End=end] -> {
        return `TL(formatTargetLanguageString(code), start, end);
      }
      ITL[Code=code] -> {
        return `ITL(formatTargetLanguageString(code));
      }
      Comment[Code=code] -> {
        return `Comment(formatTargetLanguageString(code));
      }
    }
  /*
    visit CstBlock {
      HOSTBLOCK(e, x, y) -> {
        return `HOSTBLOCK(formatTargetLanguageString(code), x, y);
      }
    }
  */
  }

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
