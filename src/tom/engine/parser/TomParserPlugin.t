/**
 *
 * The TomParser plugin.
 *
 */

package jtom.parser;

import java.io.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import jtom.exception.*;
import jtom.tools.*;

public class TomParserPlugin extends TomBase implements TomPlugin
{
    %include { ../adt/TomSignature.tom }
    private TomTerm term;
    private OptionList myOptions;

    private TomParser parser; // comes from the old class TomTaskParser
    private String fileName; // comes from the old class TomTaskParser

    public static final String PARSED_SUFFIX = ".tfix.parsed"; // was previously in TomTaskInput
    public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table"; // was previously in TomTaskInput
    public static final String DEBUG_TABLE_SUFFIX = ".tfix.debug.table"; // was previously in TomTaskInput

    public TomParserPlugin()
    {
	myOptions = `concOption(OptionBoolean("parse","","",True()) // activation flag
				);
    }

    public void setInput(ATerm term)
    {
	if (term instanceof TomTerm)
	    this.term = (TomTerm)term;
	else
	    environment().messageError(TomMessage.getString("TomTermExpected"),
				       "TomParserPlugin", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    }

    public ATerm getOutput()
    {
	return term;
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

		%match(TomTerm term)
		    {
			FileName(n) -> { fileName = n; }
		    }

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

    public OptionList declareOptions()
    {
// 	int i = 0;
// 	OptionList list = `concOption(myOptions*);
// 	while(!(list.isEmpty()))
// 	    {
// 		i++;
// 		list = list.getTail();
// 	    }

// 	System.out.println("1.2. The parser declares " +i+ " options.");

	return myOptions;
    }

    public OptionList requiredOptions()
    {
	return `emptyOptionList();
    }

    public void setOption(String optionName, String optionValue)
    {
 	%match(OptionList myOptions)
 	    {
		concOption(av*, OptionBoolean(n, alt, desc, val), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			{
			    %match(String optionValue)
				{
				    ('true') ->
					{ myOptions = `concOption(av*, ap*, OptionBoolean(n, alt, desc, True())); }
				    ('false') ->
					{ myOptions = `concOption(av*, ap*, OptionBoolean(n, alt, desc, False())); }
				}
			}
		}
		concOption(av*, OptionInteger(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concOption(av*, ap*, OptionInteger(n, alt, desc, Integer.parseInt(optionValue), attr));
		}
		concOption(av*, OptionString(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concOption(av*, ap*, OptionString(n, alt, desc, optionValue, attr));
		}
	    }
    }

}
