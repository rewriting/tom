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

public class TomParserPlugin extends TomGenericPlugin {
  
  public static final String PARSED_SUFFIX = ".tfix.parsed";
  public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table";
  public static final String DEBUG_TABLE_SUFFIX = ".tfix.debug.table";
  public static final String DECLARED_OPTIONS = "<options><boolean name='parse' altName='' description='Parser (activated by default)' value='true'/>x</options>";
  
  private String currentFile;
  
  private HostParser parser = null;
  
  public TomParserPlugin(){
    super("TomParserPlugin");
  }
  
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomParserPlugin.DECLARED_OPTIONS);
  }
  
  protected static HostParser newParser(String fileName, OptionManager optionManager) throws FileNotFoundException,IOException{
    HashSet includedFiles = new HashSet();
    HashSet alreadyParsedFiles = new HashSet();
    
    return newParser(fileName,includedFiles,alreadyParsedFiles, optionManager);
  }
  
  //create new parsers
  protected static HostParser newParser(String fileName,HashSet includedFiles,HashSet alreadyParsedFiles,
                                        OptionManager optionManager) throws FileNotFoundException,IOException {
    
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
    return new HostParser(selector,fileName,includedFiles,alreadyParsedFiles, optionManager);
  }
  
  public void run() {
    try {
      int errorsAtStart = getStatusHandler().nbOfErrors();
      int warningsAtStart = getStatusHandler().nbOfWarnings();
      
      long startChrono = System.currentTimeMillis();
	    
      boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();
      boolean java         = ((Boolean)getOptionManager().getOptionValue("jCode")).booleanValue();
      boolean debug        = ((Boolean)getOptionManager().getOptionValue("debug")).booleanValue();
	    
      if(java) {
        TomJavaParser javaParser = TomJavaParser.createParser(currentFile);
        String packageName = javaParser.javaPackageDeclaration();
        // Update taskInput
        environment().setPackagePath(packageName);
        environment().updateOutputFile();
      }
      else{
        environment().setPackagePath("");
      }
      parser = newParser(currentFile, getOptionManager());
      
      super.setArg(parser.input());
      
      getLogger().log(Level.INFO, "TomParsingPhase",
                      new Integer((int)(System.currentTimeMillis()-startChrono)) );      
      
      if(environment().isEclipseMode()) {
        String outputFileName = environment().getInputFile().getParent()+ File.separator + "."
          + environment().getRawFileName()+ PARSED_TABLE_SUFFIX;
        
        Tools.generateOutput(outputFileName, symbolTable().toTerm());
      }
      
      if(intermediate) {
        System.out.println("intermediate");
        Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
                             + PARSED_SUFFIX, (ATerm)getArg());
        Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
                             + PARSED_TABLE_SUFFIX, symbolTable().toTerm());
      }
      
      if(debug) {
        Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
                             + DEBUG_TABLE_SUFFIX, parser.getStructTable());
      }
      printAlertMessage(errorsAtStart, warningsAtStart);
    }
    catch (TokenStreamException e){
      e.printStackTrace();
      getLogger().log( Level.SEVERE, "TokenStreamException",
                       new Object[]{currentFile, new Integer( getLineFromTomParser() ), e.getMessage()} );
    }
    catch (RecognitionException e){
      e.printStackTrace();
      getLogger().log( Level.SEVERE, "RecognitionException",
                       new Object[]{currentFile, new Integer( getLineFromTomParser() ), e.getMessage()} );
    } catch (TomIncludeException e) {
      getLogger().log( Level.SEVERE, "SimpleMessage",
                       new Object[]{currentFile, new Integer( getLineFromTomParser() ), e.getMessage()} );
    } catch (TomException e) {
      getLogger().log( Level.SEVERE, "SimpleMessage",
                       new Object[]{currentFile, new Integer( getLineFromTomParser() ), e.getMessage()} );
    } catch (FileNotFoundException e) {
      getLogger().log( Level.SEVERE, "FileNotFound",
                       new Object[]{currentFile, new Integer( getLineFromTomParser() ), currentFile} ); 
    } catch (Exception e) {
      e.printStackTrace();
      getLogger().log( Level.SEVERE, "UnhandledException", 
                       new Object[]{currentFile, e.getMessage()} );
    }
  }
  
  private int getLineFromTomParser() {
    if(parser == null) {
	    return TomMessage.DEFAULT_ERROR_LINE_NUMBER;
    } 
    return parser.getLine();
  }
  
  public void setArg(Object arg){
    if (arg instanceof String) {
	    currentFile = (String)arg;  
    } else {
	    getLogger().log(Level.SEVERE, "TomParserPlugin: A String was expected.");
    }
  }
  
} //class TomParserPlugin
