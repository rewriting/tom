/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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

import java.io.*;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.exception.TomException;
import tom.engine.exception.TomIncludeException;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.engine.parser.TomParserTool;

import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.cst.types.*;
import tom.engine.adt.code.types.*;

import tom.library.sl.Visitable;

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
  %include { ../../platform/adt/platformoption/PlatformOption.tom }
  %include { ../adt/cst/CST.tom }
  
  /** some output suffixes */
  public static final String PARSED_SUFFIX = ".tfix.parsed";
  public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table";

  /** the declared options string*/
  public static final PlatformOptionList PLATFORM_OPTIONS =
    `concPlatformOption(
        PluginOption("parse", "", "Parser (not activated by default)", BooleanValue(False()), ""),
        PluginOption("newparser", "np", "New Parser (not activated by default)", BooleanValue(False()), ""),
        PluginOption("tomjava", "tj", "Parser tailored for Tom+Java (activated by default)", BooleanValue(True()), ""),
        PluginOption("printcst", "cst", "print post-parsing cst (only with new parser)", BooleanValue(False()), ""),
        PluginOption("printast", "ast", "print post-parsing ast", BooleanValue(False()), "")
        );
  
  /** input file name and stream */
  private String currentFileName;
  private Reader currentReader;
  
  /** the main HostParser */
  private tom.engine.parser.antlr2.HostParser parser = null;

  private TomParserTool parserTool = null;
  
  /** Constructor */
  public TomParserPlugin() {
    super("TomParserPlugin");
  }

  private TomParserTool getParserTool() {
    return this.parserTool;
  }
  
  //creating a new Host parser
  public static tom.engine.parser.antlr2.HostParser createHostParser(Reader reader,String filename,
                                        HashSet<String> includedFiles,
                                        HashSet<String> alreadyParsedFiles,
                                        TomParserTool parserTool)
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
        parserTool);
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the StreamManager from which we can get the input
   */
  public void setArgs(Object[] arg) {
    if (arg[0] instanceof TomStreamManager) {
      setStreamManager((TomStreamManager)arg[0]);
      currentFileName = getStreamManager().getInputFileName();  
      currentReader = getStreamManager().getInputReader();
      //System.out.println("(debug) I'm in the TomParserPlugin: file "+currentFileName+" / "+getStreamManager().toString());
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
    boolean tomjava      = ((Boolean)getOptionManager().getOptionValue("tomjava")).booleanValue();
    boolean printcst     = ((Boolean)getOptionManager().getOptionValue("printcst")).booleanValue();
    boolean printast     = ((Boolean)getOptionManager().getOptionValue("printast")).booleanValue();

    SymbolTable symbolTable = getStreamManager().getSymbolTable();
    if(newparser==false && tomjava==false) {
      /*
       * ANTLR2
       */
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

        // creating TomParserTool
        this.parserTool = new TomParserTool(getStreamManager(),getOptionManager());

        // getting a parser 
        HashSet<String> includedFiles = new HashSet<String>();
        HashSet<String> alreadyParsedFiles = new HashSet<String>();
        parser = createHostParser(currentReader, currentFileName,includedFiles, alreadyParsedFiles, getParserTool());
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
            getParserTool().printTree(tomSymbol);
          }
        }

        if(printast) {
          getParserTool().printTree((Visitable)getWorkingTerm());
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
    } else if(newparser==true) { // && tomjava==false) {
      /*
       * ANTLR4
       */
      try {

        if(!currentFileName.equals("-")) {
          // creating TomParserTool
          this.parserTool = new TomParserTool(getStreamManager(),getOptionManager());

          tom.engine.parser.antlr4.TomParser parser = new tom.engine.parser.antlr4.TomParser(currentFileName, getParserTool(), symbolTable);
          ANTLRInputStream input = new ANTLRInputStream(currentReader);

          System.out.print("antlr4: " + currentFileName);
          long start = System.currentTimeMillis();
          if(java) {
            String packageName = parser.parseJavaPackage(input);
            if(packageName.length() > 0) {
              //System.out.println("set package: " + packageName);
              // Update streamManager to take into account package information
              getStreamManager().setPackagePath(packageName);
            }
          }

          CstProgram cst = parser.parse(input);
          if(printcst) {
            getParserTool().printTree(cst);
          }
          System.out.print("\tparsing + building cst:" + (System.currentTimeMillis()-start) + " ms");

          start = System.currentTimeMillis();
          tom.engine.parser.antlr4.AstBuilder astBuilder = new tom.engine.parser.antlr4.AstBuilder(getParserTool(),symbolTable);
          Code code = astBuilder.convert(cst);
          System.out.println("\tbuilding ast:" + (System.currentTimeMillis()-start) + " ms");

          // System.out.println("ast = " + code);


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
              getParserTool().printTree(tomSymbol);
            }
          }

          if(printast) {
            getParserTool().printTree((Visitable)getWorkingTerm());
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
    } else if(newparser==false && tomjava==true) {
      /*
       * ANTLR4 + JAVA GRAMMAR
       */
      try {

        if(!currentFileName.equals("-")) {
          // creating TomParserTool
          this.parserTool = new TomParserTool(getStreamManager(),getOptionManager());

          tom.engine.parser.tomjava.TomParser parser = new tom.engine.parser.tomjava.TomParser(currentFileName, getParserTool(), symbolTable);
          ANTLRInputStream input = new ANTLRInputStream(currentReader);

          System.out.print("tomjava antlr4: " + currentFileName);
          long start = System.currentTimeMillis();
          if(java) {
            String packageName = parser.parseJavaPackage(input);
            if(packageName.length() > 0) {
              //System.out.println("set package: " + packageName);
              // Update streamManager to take into account package information
              getStreamManager().setPackagePath(packageName);
            }
          }

          CstBlockList cst = parser.parse(input, tom.engine.parser.tomjava.TomParser.JAVA_TOP_LEVEL);
          if(printcst) {
            getParserTool().printTree(cst);
          }
          System.out.print("\tparsing + building cst:" + (System.currentTimeMillis()-start) + " ms");

          start = System.currentTimeMillis();
          tom.engine.parser.tomjava.AstBuilder astBuilder = new tom.engine.parser.tomjava.AstBuilder(getParserTool(),symbolTable);
          Code code = astBuilder.convert( `Cst_Program(cst) );
          System.out.println("\tbuilding ast:" + (System.currentTimeMillis()-start) + " ms");

          // System.out.println("ast = " + code);


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
              getParserTool().printTree(tomSymbol);
            }
          }

          if(printast) {
            getParserTool().printTree((Visitable)getWorkingTerm());
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

    } else {
      System.out.println("no active parser");
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
    return PLATFORM_OPTIONS; 
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

} //class TomParserPlugin
