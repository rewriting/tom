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

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;
import jtom.exception.*;
import jtom.tools.*;

import java.util.logging.*;

/**
 * The TomParser plugin.
 */
public class TomParserPlugin extends TomGenericPlugin {
  %include { adt/TomSignature.tom }
  %include { adt/PlatformOption.tom }

  private TomParser parser;
  private String fileName;

  public static final String PARSED_SUFFIX = ".tfix.parsed";
  public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table";
  public static final String DEBUG_TABLE_SUFFIX = ".tfix.debug.table";

  public TomParserPlugin() {
    super("TomParserPlugin");
  }

  public void setTerm(ATerm term) {
    fileName = ((AFun)term).getName();
  }

  public void run() {
    try {
      int errorsAtStart = getPluginPlatform().getStatusHandler().nbOfErrors();
      int warningsAtStart = getPluginPlatform().getStatusHandler().nbOfWarnings();

      long startChrono = System.currentTimeMillis();
	
      boolean intermediate = getPluginPlatform().getOptionBooleanValue("intermediate");
      boolean java         = getPluginPlatform().getOptionBooleanValue("jCode");
      boolean debug        = getPluginPlatform().getOptionBooleanValue("debug");

      if(java) {
	TomJavaParser javaParser = TomJavaParser.createParser(fileName);
	String packageName = javaParser.JavaPackageDeclaration();
	// Update environment
	environment().setPackagePath(packageName);
	environment().updateOutputFile();
      }	else {
	environment().setPackagePath("");
      }
   
      //System.out.println(fileName);

      this.parser = TomParser.createParser(fileName);
      TomTerm parsedTerm = parser.startParsing();
      super.setTerm(parsedTerm);

      getLogger().log( Level.INFO,
		       "TomParsingPhase",
		       new Integer((int)(System.currentTimeMillis()-startChrono)) );

      if(environment().isEclipseMode()) {
	String outputFileName = environment().getInputFile().getParent()+ File.separator + "."
	    + environment().getRawFileName()+ PARSED_TABLE_SUFFIX;

	Tools.generateOutput(outputFileName, symbolTable().toTerm());
      }

      if(intermediate) {
	Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() + PARSED_SUFFIX, 
			     getTerm());
	Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() + PARSED_TABLE_SUFFIX, 
			     symbolTable().toTerm());
      }
        
      if(debug)
	Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() + DEBUG_TABLE_SUFFIX, 
			     parser.getStructTable());

      printAlertMessage(errorsAtStart, warningsAtStart);

    } catch (TokenMgrError e) {
	getLogger().log( Level.SEVERE,
			 "TokenMgrError",
			 new Object[]{fileName, new Integer( getLineFromTomParser(parser) ), e.getMessage()} );
    } catch (TomIncludeException e) {
	getLogger().log( Level.SEVERE,
			 "SimpleMessage",
			 new Object[]{fileName, new Integer( getLineFromTomParser(parser) ), e.getMessage()} );
    } catch (TomException e) {
	getLogger().log( Level.SEVERE,
			 "SimpleMessage",
			 new Object[]{fileName, new Integer( getLineFromTomParser(parser) ), e.getMessage()} );
    } catch (FileNotFoundException e) {
	getLogger().log( Level.SEVERE,
			 "FileNotFound",
			 new Object[]{fileName, new Integer( getLineFromTomParser(parser) ), fileName} ); 
    } catch (ParseException e) {
	getLogger().log( Level.SEVERE,
			 "ParseException",
			 new Object[]{fileName, new Integer( getLineFromTomParser(parser) ), e.getMessage()} );
    } catch (Exception e) {
	e.printStackTrace();
	getLogger().log( Level.SEVERE,
			 "UnhandledException", 
			 new Object[]{fileName, e.getMessage()} );
    }
  }

  private int getLineFromTomParser(TomParser parser) {
    if(parser == null) {
      return TomMessage.DEFAULT_ERROR_LINE_NUMBER;
    } 
    return parser.getLine();
  }

  private int getLineFromJavaParser(TomJavaParser parser) {
    if(parser == null) {
      return TomMessage.DEFAULT_ERROR_LINE_NUMBER;
    } 
    return parser.getLine();
  }

  public PlatformOptionList declaredOptions() {
    return `concPlatformOption(OptionBoolean("parse","","",True()) // activation flag
			       );
  }
}
