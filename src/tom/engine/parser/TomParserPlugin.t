package jtom.parser;

import java.io.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.types.*;
import jtom.exception.*;
import jtom.tools.*;

/**
 * The TomParser plugin.
 */
public class TomParserPlugin extends TomGenericPlugin
{
    %include { ../adt/TomSignature.tom }
    %include{ ../adt/Options.tom }

    private TomParser parser; // comes from the old class TomTaskParser
    private String fileName; // comes from the old class TomTaskParser

    public static final String PARSED_SUFFIX = ".tfix.parsed"; // was previously in TomTaskInput
    public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table"; // was previously in TomTaskInput
    public static final String DEBUG_TABLE_SUFFIX = ".tfix.debug.table"; // was previously in TomTaskInput

    public TomParserPlugin() {
    }

    public void setInput(ATerm term)
    {
	fileName = ((AFun)term).getName();
    }

    public void run()
    {
	try
	    {
		long startChrono = System.currentTimeMillis();
		
		boolean verbose = ((Boolean)getServer().getOptionValue("verbose")).booleanValue();
		boolean intermediate = ((Boolean)getServer().getOptionValue("intermediate")).booleanValue();
		boolean java = ((Boolean)getServer().getOptionValue("jCode")).booleanValue();
		boolean debug = ((Boolean)getServer().getOptionValue("debug")).booleanValue();

		if(java)
		    {
			TomJavaParser javaParser = TomJavaParser.createParser(fileName);
			String packageName = javaParser.JavaPackageDeclaration();
			// Update taskInput
			environment().setPackagePath(packageName);
			environment().updateOutputFile();
		    }
		else
		    {
			environment().setPackagePath("");
		    }
 		this.parser = TomParser.createParser(fileName);

 		term = parser.startParsing();
      
		if(verbose) 
		    System.out.println("TOM parsing phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");

		if(environment().isEclipseMode())
		    {
			String outputFileName = environment().getInputFile().getParent()+ File.separator + "."
			    + environment().getRawFileName()+ PARSED_TABLE_SUFFIX;

			Tools.generateOutput(outputFileName, symbolTable().toTerm());
		    }

		if(intermediate)
		    {
			Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
					     + PARSED_SUFFIX, term);
			Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
					     + PARSED_TABLE_SUFFIX, symbolTable().toTerm());
		    }
        
		if(debug)
		    Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
					 + DEBUG_TABLE_SUFFIX, parser.getStructTable());
        
		environment().printAlertMessage("TomParserPlugin");
		if(!environment().isEclipseMode()) {
		    // remove all warning (in command line only)
		    environment().clearWarnings();
		} 
	    }
	
 	catch (TokenMgrError e)
 	    {
 		environment().messageError(TomMessage.getString("TokenMgrError"), new Object[]{e.getMessage()}, 
					   fileName,  getLineFromTomParser(parser));
 	    } 
 	catch (TomIncludeException e)
 	    {
 		environment().messageError(e.getMessage(), fileName,  getLineFromTomParser(parser));
 	    }
 	catch (TomException e) 
 	    {
 		environment().messageError(e.getMessage(), fileName,  getLineFromTomParser(parser));
 	    }
 	catch (FileNotFoundException e) 
 	    {
 		environment().messageError(TomMessage.getString("FileNotFound"), new Object[]{fileName}, 
					   fileName, getLineFromTomParser(parser));
 	    } 
 	catch (ParseException e) 
 	    {
 		environment().messageError(TomMessage.getString("ParseException"), new Object[]{e.getMessage()}, 
					   fileName, getLineFromTomParser(parser));
 	    } 
	catch (Exception e) 
	    {
		e.printStackTrace();
		environment().messageError(TomMessage.getString("UnhandledException"), 
					   new Object[]{fileName, e.getMessage()}, 
					   fileName, TomMessage.DEFAULT_ERROR_LINE_NUMBER);
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

     public TomOptionList declaredOptions() {
	 return `concTomOption(OptionBoolean("parse","","",True()) // activation flag
			      );
     }
}
