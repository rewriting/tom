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

import jtom.*;
import jtom.tools.*;
import jtom.exception.*;
import tom.platform.adt.platformoption.types.*;
import tom.platform.*;

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
  public static final String DEBUG_TABLE_SUFFIX = ".tfix.debug.table";

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
  public RuntimeAlert setArgs(Object[] arg){
    if (arg[0] instanceof TomStreamManager) {
      setStreamManager((TomStreamManager)arg[0]);
	    currentFileName = getStreamManager().getInputFile().getAbsolutePath();  
    } else {
      getLogger().log(Level.SEVERE, "InvalidPluginArgument",
                      new Object[]{"TomParserPlugin", "[TomStreamManager]",
                                   getArgumentArrayString(arg)});
    }
    return new RuntimeAlert();
  }

  /**
   * inherited from plugin interface
   * Parse the input ans set the "Working" TomTerm to be compiled.
   */
  public RuntimeAlert run() {
    RuntimeAlert result = new RuntimeAlert();
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
        // Update streamManager to take into account package information
        getStreamManager().setPackagePath(packageName);
      }
      // getting a parser 
      parser = newParser(currentFileName, getOptionManager(), getStreamManager());
      // parsing
      setWorkingTerm(parser.input());
      
      getLogger().log(Level.INFO, "TomParsingPhase",
                      new Integer((int)(System.currentTimeMillis()-startChrono)) );      
      //printAlertMessage(errorsAtStart, warningsAtStart);
    } catch (TokenStreamException e) {
      e.printStackTrace();
      getLogger().log(Level.SEVERE, "TokenStreamException",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), e.getMessage()} );
      return result;
      //return result.addError(e.getMessage(), currentFileName, getLineFromTomParser());
    } catch (RecognitionException e){
      e.printStackTrace();
      getLogger().log(Level.SEVERE, "RecognitionException",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), e.getMessage()});
      return result;
      //return result.addError(e.getMessage(), currentFileName, getLineFromTomParser());
    } catch (TomIncludeException e) {
      getLogger().log(Level.SEVERE, "SimpleMessage",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), e.getMessage()});
      return result;
      //return result.addError(e.getMessage(), currentFileName, getLineFromTomParser());
    } catch (TomException e) {
      getLogger().log(Level.SEVERE, "SimpleMessage",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), e.getMessage()});
      return result;
    } catch (FileNotFoundException e) {
      getLogger().log(Level.SEVERE, "FileNotFound",
                      new Object[]{currentFileName, new Integer(getLineFromTomParser() ), currentFileName}); 
      return result;
    } catch (Exception e) {
      getLogger().log(Level.SEVERE, "ExceptionMessage", 
                      new Object[]{getClass().getName(), currentFileName, e.getMessage()});
      e.printStackTrace();
      return result;
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
    if(debug) {
      Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() 
                           + DEBUG_TABLE_SUFFIX, parser.getStructTable());
    }
    return result;
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
