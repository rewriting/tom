/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

package jtom.parser;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import antlr.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;
import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.exception.*;
import tom.platform.adt.platformoption.types.*;
import tom.platform.*;

/**
 * The TomParser "plugin"
 * The responsability of the plugin is to parse the input file to be able to
 * do the other phase.
 * it get the input file from the TomStreamManger and parse it
 */
public class TomParserPlugin extends TomGenericPlugin {
  
  public static final String PARSED_SUFFIX = ".tfix.parsed";
  public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table";
  public static final String DEBUG_TABLE_SUFFIX = ".tfix.debug.table";
  public static final String DECLARED_OPTIONS = "<options><boolean name='parse' altName='' description='Parser (activated by default)' value='true'/></options>";
  
  /** input file name*/
  private String currentFileName;
  
  private HostParser parser = null;
  
  public TomParserPlugin(){
    super("TomParserPlugin");
  }
    
  protected static HostParser newParser(String fileName, OptionManager optionManager, TomStreamManager tomStreamManager)
    throws FileNotFoundException,IOException {
    HashSet includedFiles = new HashSet();
    HashSet alreadyParsedFiles = new HashSet();
    return newParser(fileName,includedFiles,alreadyParsedFiles, optionManager, tomStreamManager);
  }
  
  //create new parsers
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

  
  public void run() {
    int errorsAtStart = getStatusHandler().nbOfErrors();
    int warningsAtStart = getStatusHandler().nbOfWarnings();
    long startChrono = System.currentTimeMillis();
    boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();
    boolean java         = ((Boolean)getOptionManager().getOptionValue("jCode")).booleanValue();
    boolean debug        = ((Boolean)getOptionManager().getOptionValue("debug")).booleanValue();
    boolean eclipse      = ((Boolean)getOptionManager().getOptionValue("eclipse")).booleanValue();
    try {
      // looking for java package
      if(java) {
        TomJavaParser javaParser = TomJavaParser.createParser(currentFileName);
        String packageName = javaParser.javaPackageDeclaration();
        // Update taskInput
        getStreamManager().setPackagePath(packageName);
        getStreamManager().updateOutputFile();
      }
      else{
        getStreamManager().setPackagePath("");
      }
      // general parsing
      parser = newParser(currentFileName, getOptionManager(), getStreamManager());

      setWorkingTerm(parser.input());
      getLogger().log(Level.INFO, "TomParsingPhase",
                      new Integer((int)(System.currentTimeMillis()-startChrono)) );      
      if(eclipse) {
        String outputFileName = getStreamManager().getInputFile().getParent()+ File.separator + "."
          + getStreamManager().getRawFileName()+ PARSED_TABLE_SUFFIX;
        
        Tools.generateOutput(outputFileName, getStreamManager().getSymbolTable().toTerm());
      }
      
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() 
                             + PARSED_SUFFIX, (ATerm)getWorkingTerm());
        Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() 
                             + PARSED_TABLE_SUFFIX, getStreamManager().getSymbolTable().toTerm());
      }
      
      if(debug) {
        Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() 
                             + DEBUG_TABLE_SUFFIX, parser.getStructTable());
      }
      printAlertMessage(errorsAtStart, warningsAtStart);
    }
    catch (TokenStreamException e){
      //e.printStackTrace();
      getLogger().log(Level.SEVERE, "TokenStreamException",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), e.getMessage()} );
    }
    catch (RecognitionException e){
      //e.printStackTrace();
      getLogger().log(Level.SEVERE, "RecognitionException",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), e.getMessage()});
    } catch (TomIncludeException e) {
      getLogger().log(Level.SEVERE, "SimpleMessage",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), e.getMessage()});
    } catch (TomException e) {
      getLogger().log(Level.SEVERE, "SimpleMessage",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), e.getMessage()});
    } catch (FileNotFoundException e) {
      getLogger().log(Level.SEVERE, "FileNotFound",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), currentFileName}); 
    } catch (Exception e) {
      e.printStackTrace();
      getLogger().log(Level.SEVERE, "UnhandledException", 
                      new Object[]{currentFileName, e.getMessage()});
    }
  }
  
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomParserPlugin.DECLARED_OPTIONS);
  }

  private int getLineFromTomParser() {
    if(parser == null) {
	    return TomMessage.DEFAULT_ERROR_LINE_NUMBER;
    } 
    return parser.getLine();
  }
  
  public void setArgs(Object[] arg){
    if (arg[0] instanceof TomStreamManager) {
      setStreamManager((TomStreamManager)arg[0]);
	    currentFileName = getStreamManager().getInputFile().getAbsolutePath();  
    } else {
      getLogger().log(Level.SEVERE, "InvalidPluginArgument",
                      new Object[]{"TomParserPlugin", "[TomStreamManager]",
                                   getArgumentArrayString(arg)});
    }
  }
  
  /**
   * inherited from plugin interface
   * returns an array containing the parsed module and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{getWorkingTerm(), getStreamManager()};
  }
  
} //class TomParserPlugin
