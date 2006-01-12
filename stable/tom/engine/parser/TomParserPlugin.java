/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.logging.Level;

import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.exception.TomException;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.platform.OptionManager;
import tom.platform.OptionParser;
import tom.platform.PlatformLogRecord;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamSelector;
import aterm.ATerm;

/**
 * The TomParser "plugin"
 * The responsability of the plugin is to parse the input file and create the
 * corresponding TomTerm.
 * It get the input file from the TomStreamManger and parse it
 */
public class TomParserPlugin extends TomGenericPlugin {
  
  /** some output suffixes */
  public static final String PARSED_SUFFIX = ".tfix.parsed";
  public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table";

  /** the declared options string*/
  public static final String DECLARED_OPTIONS = "<options><boolean name='parse' altName='' description='Parser (activated by default)' value='true'/></options>";
  
  /** input file name*/
  private String currentFileName;
  
  /** the main HostParser */
  private HostParser parser = null;
  
  /** Constructor */
  public TomParserPlugin(){
    super("TomParserPlugin");
  }
  
  //creating a new Host parser
  protected static HostParser newParser(String fileName, OptionManager optionManager, TomStreamManager tomStreamManager)
    throws FileNotFoundException,IOException {
    HashSet includedFiles = new HashSet();
    HashSet alreadyParsedFiles = new HashSet();
    return newParser(fileName,includedFiles,alreadyParsedFiles, optionManager, tomStreamManager);
  }
  
  protected static HostParser newParser(String fileName,HashSet includedFiles,
                                        HashSet alreadyParsedFiles,
                                        OptionManager optionManager,
                                        TomStreamManager tomStreamManager)
    throws FileNotFoundException,IOException {
    // The input Stream
    DataInputStream input = new DataInputStream(new FileInputStream(new File(fileName)));
    // a selector to choose the lexer to use
    TokenStreamSelector selector = new TokenStreamSelector();
    // create a lexer for target mode
    HostLexer targetlexer = new HostLexer(input);
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
    return new HostParser(selector,fileName,includedFiles,alreadyParsedFiles, optionManager, tomStreamManager);
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the StreamManager from which we can get the input
   */
  public void setArgs(Object[] arg){
    if (arg[0] instanceof TomStreamManager) {
      setStreamManager((TomStreamManager)arg[0]);
      try {
        currentFileName = getStreamManager().getInputFile().getCanonicalPath();  
      } catch (IOException e) {
        System.out.println("IO Exception when computing currentFileName");
        e.printStackTrace();
      }

    } else {
      getLogger().log(Level.SEVERE, TomMessage.invalidPluginArgument.getMessage(),
                      new Object[]{"TomParserPlugin", "[TomStreamManager]",
                                   getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Parse the input ans set the "Working" TomTerm to be compiled.
   */
  public void run() {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();
    boolean java         = ((Boolean)getOptionManager().getOptionValue("jCode")).booleanValue();
    boolean eclipse      = ((Boolean)getOptionManager().getOptionValue("eclipse")).booleanValue();
    try {
      // looking for java package
      if(java) {
        //         TomJavaLexer javaLexer = new TomJavaLexer(new BufferedReader(new FileReader(new File(currentFileName))));
        //         Token token = javaLexer.nextToken();
        //         System.out.println("token = '" + token + "'");
        //         String packageName = "";

        TomJavaParser javaParser = TomJavaParser.createParser(currentFileName);
        String packageName = javaParser.javaPackageDeclaration();
        //System.out.println("packageName = '" +packageName + "'");
 
        // Update streamManager to take into account package information
        getStreamManager().setPackagePath(packageName);
      }
      // getting a parser 
      parser = newParser(currentFileName, getOptionManager(), getStreamManager());
      // parsing
      setWorkingTerm(parser.input());
      // verbose
      getLogger().log(Level.INFO, TomMessage.tomParsingPhase.getMessage(),
                      new Integer((int)(System.currentTimeMillis()-startChrono)) );      
    } catch (TokenStreamException e) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      getLogger().log(new PlatformLogRecord(Level.SEVERE,
                      TomMessage.tokenStreamException,
                      sw.toString(),
                      currentFileName, getLineFromTomParser()));
      return;
    } catch (RecognitionException e){
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      getLogger().log(new PlatformLogRecord(Level.SEVERE,
                      TomMessage.recognitionException,
                      sw.toString(),
                      currentFileName, getLineFromTomParser()));
      return;
    } catch (TomException e) {
       getLogger().log(new PlatformLogRecord(Level.SEVERE, e.getPlatformMessage(),e.getParameters(),
          currentFileName, getLineFromTomParser()));
      return;
    } catch (FileNotFoundException e) {
      getLogger().log(Level.SEVERE, TomMessage.fileNotFound.getMessage(),currentFileName); 
      return;
    } catch (Exception e) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(), 
                      new Object[]{getClass().getName(), currentFileName, sw.toString()});
      return;
    }
    // Some extra stuff
    if(eclipse) {
      String outputFileName = getStreamManager().getInputFile().getParent()+
        File.separator + "."+
        getStreamManager().getRawFileName()+ PARSED_TABLE_SUFFIX;
      Tools.generateOutput(outputFileName, getStreamManager().getSymbolTable().toTerm());
      }
    if(intermediate) {
      Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() 
                           + PARSED_SUFFIX, (ATerm)getWorkingTerm());
      Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() 
                           + PARSED_TABLE_SUFFIX, getStreamManager().getSymbolTable().toTerm());
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
  
} //class TomParserPlugin
