/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2011, INPL, INRIA
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
 **/

package tom.engine.newparser;

import tom.engine.newparser.parser.*;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.Iterator;

import tom.engine.Tom;
import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.exception.TomException;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;
import tom.platform.OptionParser;
import tom.platform.PlatformLogRecord;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.cst.types.*;

import tom.engine.adt.cst.*;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamSelector;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;

import aterm.ATerm;
import shared.*;


/**
 * The New Parser "plugin"
 * The responsability of the plugin is to parse the input file and create the
 * corresponding TomTerm.
 * It get the input file from the TomStreamManger and parse it
 */
public class NewParserPlugin extends TomGenericPlugin {
  
  /** some output suffixes */
  //public static final String PARSED_SUFFIX = ".tfix.parsed";
  //public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table";

  /** the declared options string*/
  public static final String DECLARED_OPTIONS = "<options><boolean name='newparser' altName='np' description='New Parser (deactivated by default)' value='false'/></options>";
  
  /** input file name and stream */
  private String currentFileName;
  private Reader currentReader;
  
  /** the main HostParser */
  private HostParser parser = null;
 

  protected gt_Program cst=null;

  /** Constructor */
  public NewParserPlugin() {
    super("NewParserPlugin");
  }
  
  //creating a new Host parser
  /*
  protected static HostParser newParser(Reader reader, String fileName,
                                        OptionManager optionManager,
                                        TomStreamManager tomStreamManager)
    throws FileNotFoundException,IOException {
    HashSet<String> includedFiles = new HashSet<String>();
    HashSet<String> alreadyParsedFiles = new HashSet<String>();
    return newParser(reader,fileName,
                     includedFiles,alreadyParsedFiles,
                     optionManager, tomStreamManager);
  }
  */

  /*
  protected static HostParser newParser(Reader reader,String fileName,
                                        HashSet<String> includedFiles,
                                        HashSet<String> alreadyParsedFiles,
                                        OptionManager optionManager,
                                        TomStreamManager tomStreamManager)
    throws FileNotFoundException,IOException {
    // a selector to choose the lexer to use
    TokenStreamSelector selector = new TokenStreamSelector();
    // create a lexer for target mode
    HostLexer targetlexer = new HostLexer(reader);
    // create a lexer for tom mode
    TomLexer tomlexer = new TomLexer(targetlexer.getInputState());
    // create a lexer for backquote mode
    BackQuoteLexer bqlexer = new BackQuoteLexer(targetlexer.getInputState());
    // notify selector about various lexers
    selector.addInputStream(targetlexer,"targetlexer");
    selector.addInputStream(tomlexer, "tomlexer");
    selector.addInputStream(bqlexer, "bqlexer");
    selector.select("targetlexer");
    // create the parser for target mode
    // also create tom parser and backquote parser
    return new HostParser(selector, fileName,
        includedFiles, alreadyParsedFiles,
        optionManager, tomStreamManager);
  }
  */

  /**
   * inherited from plugin interface
   * arg[0] should contain the StreamManager from which we can get the input
   */
  public void setArgs(Object[] arg){
    //System.out.println("(DEBUG) NewParser init");
    if (arg[0] instanceof TomStreamManager) {
      setStreamManager((TomStreamManager)arg[0]);
      currentFileName = getStreamManager().getInputFileName();  
      currentReader = getStreamManager().getInputReader();
      /*
       * The following part has been added in order to be able to have many
       * parsers. It is useful as long the new parser is not fully operational
       * A parser waits a StreamManager as first (and single) argument
       * (arg[0]) but the NewParser Plugin is called after the TomParserPlugin,
       * —whatever parser plugin is activated, both are called— therefore
       * it receives two arguments : the parsed code and the StreamManager.
       */
    } else if (arg[1] instanceof TomStreamManager) {
      term = (Code)arg[0];
      setStreamManager((TomStreamManager)arg[1]);
      currentFileName = getStreamManager().getInputFileName();  
      currentReader = getStreamManager().getInputReader();

    } else {
      System.out.println("(debug) erreur new parser");
      TomMessage.error(getLogger(), null, 0, TomMessage.invalidPluginArgument,
          "NewParserPlugin", "[TomStreamManager]", getArgumentArrayString(arg));
    }
  }

  /**
   * inherited from plugin interface
   * Parse the input ans set the "Working" TomTerm to be compiled.
   */
  public synchronized void run(Map informationTracker) {

    long startChrono = System.currentTimeMillis();
    //boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();
    //boolean java         = ((Boolean)getOptionManager().getOptionValue("jCode")).booleanValue();
      boolean java = true;
    //boolean eclipse      = ((Boolean)getOptionManager().getOptionValue("eclipse")).booleanValue();
    boolean newparser    = ((Boolean)getOptionManager().getOptionValue("newparser")).booleanValue();
    if (newparser) {
      //System.out.println("(DEBUG) we are using the new parser / newparser = " + newparser);
      //System.out.println("(DEBUG) NewParser in use");
    try {
        // looking for java package
        if(java && (!currentFileName.equals("-"))) {

          HostParser parser =
            new HostParser(getStreamManager(), getOptionManager());
          ANTLRReaderStream input = new ANTLRReaderStream(currentReader);
          input.name = currentFileName;
          System.out.println("CurrentFileName : "+currentFileName);
          Tree programAsAntrlTree = parser.parseProgram(input);
          cst = (gt_Program)CSTAdaptor.getTerm(programAsAntrlTree);
          //Code programAsAST = CSTAdaptor.adapt(programAsCST);
          //setWorkingTerm(programAsAST);

          /* Do not exhaust the stream !! */
/*        TomJavaParser javaParser = TomJavaParser.createParser(currentFileName);
          String packageName = "";
          try {
            packageName = javaParser.javaPackageDeclaration();
          } catch (TokenStreamException tse) {
            /* no package was found: ignore */
/*        }
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
/*      SymbolTable symbolTable = getStreamManager().getSymbolTable();
        Iterator it = symbolTable.keySymbolIterator();
        while(it.hasNext()) {
          String tomName = (String)it.next();
          TomSymbol tomSymbol = getSymbolFromName(tomName);
          tomSymbol = symbolTable.updateConstrainedSymbolCodomain(tomSymbol, symbolTable);
      */}
        // verbose
        TomMessage.info(getLogger(), null, 0, TomMessage.tomParsingPhase,
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
     }catch(IOException e) {
       TomMessage.error(getLogger(), currentFileName, -1,
           TomMessage.fileNotFound, e.getMessage());// TODO custom ErrMessage
     }
/*    } catch (TokenStreamException e) {
        TomMessage.error(getLogger(), currentFileName, /*getLineFromTomParser()*//*-1,
            TomMessage.tokenStreamException, e.getMessage());
        return;
      } catch (RecognitionException e){
        TomMessage.error(getLogger(), currentFileName, /*getLineFromTomParser()*//*-1, 
            TomMessage.recognitionException, e.getMessage());
        return;
      } catch (TomException e) {
        TomMessage.error(getLogger(), currentFileName, /*getLineFromTomParser()*//*0, 
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
*/    // Some extra stuff
      /*
      if(eclipse) {
        String outputFileName = getStreamManager().getInputParentFile()+
          File.separator + "."+
          getStreamManager().getRawFileName()+ PARSED_TABLE_SUFFIX;
        Tools.generateOutput(outputFileName, getStreamManager().getSymbolTable().toTerm().toATerm());
      }
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() 
            + PARSED_SUFFIX, (tom.library.sl.Visitable)getWorkingTerm());
        Tools.generateOutput(getStreamManager().getOutputFileName() 
            + PARSED_TABLE_SUFFIX, getStreamManager().getSymbolTable().toTerm().toATerm());
      }
      */
    } else {
      // not active plugin
      TomMessage.info(getLogger(), null, 0, TomMessage.newParserNotUsed);
    }
  }
  
  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(NewParserPlugin.DECLARED_OPTIONS);
  }

  /**
   * return the last line number
   */
/*  private int getLineFromTomParser() {
    if(parser == null) {
      return TomMessage.DEFAULT_ERROR_LINE_NUMBER;
    } 
    return parser.getLine();
  }
*/

  public Object[] getArgs() {
    boolean newparser = ((Boolean)getOptionManager().getOptionValue("newparser")).booleanValue();

    return (newparser)? new Object[]{cst, streamManager}
                      : new Object[]{term, streamManager};
  }
  
} //class NewParserPlugin