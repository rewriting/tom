/**
 *
 * The TomTypeChecker plugin.
 *
 */

package jtom.checker;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.types.*;

public class TomTypeChecker extends TomChecker implements TomPlugin
{
    %include { ../adt/TomSignature.tom }
    %include{ ../adt/Options.tom }

    private TomTerm term;
    private TomOptionList myOptions;

    public TomTypeChecker()
    {
	myOptions = `concTomOption(OptionBoolean("checkType","","",True()) // activationFlag
				);
    }

    public void setInput(ATerm term)
    {
	if (term instanceof TomTerm)
	    this.term = (TomTerm)term;
	else
	    environment().messageError(TomMessage.getString("TomTermExpected"),
				       "TomTypeChecker", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
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
		
		checkTypeInference(term);
		
		if(verbose)
		    System.out.println("TOM type checking phase (" +(System.currentTimeMillis()-startChrono)+ " ms)");

		environment().printAlertMessage("TomTypeChecker");
		if(!environment().isEclipseMode()) {
		    // remove all warning (in command line only)
		    environment().clearWarnings();
		}
	    }
	catch (Exception e) 
	    {
		environment().messageError("Exception occurs in TomTypeChecker: "+e.getMessage(), 
					   environment().getInputFile().getName(), 
					   TomMessage.DEFAULT_ERROR_LINE_NUMBER);
		e.printStackTrace();
	    }
    }

    public TomOptionList declareOptions()
    {
// 	int i = 0;
// 	OptionList list = `concOption(myOptions*);
// 	while(!(list.isEmpty()))
// 	    {
// 		i++;
// 		list = list.getTail();
// 	    }

// 	System.out.println("1.5. The type checker declares " +i+ " options.");
	return myOptions;
    }

    public TomOptionList requiredOptions()
    {
	return `emptyTomOptionList();
    }

    public void setOption(String optionName, String optionValue)
    {
 	%match(TomOptionList myOptions)
 	    {
		concTomOption(av*, OptionBoolean(n, alt, desc, val), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			{
			    %match(String optionValue)
				{
				    ('true') ->
					{ myOptions = `concTomOption(av*, ap*, OptionBoolean(n, alt, desc, True())); }
				    ('false') ->
					{ myOptions = `concTomOption(av*, ap*, OptionBoolean(n, alt, desc, False())); }
				}
			}
		}
		concTomOption(av*, OptionInteger(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concTomOption(av*, ap*, OptionInteger(n, alt, desc, Integer.parseInt(optionValue), attr));
		}
		concTomOption(av*, OptionString(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concTomOption(av*, ap*, OptionString(n, alt, desc, optionValue, attr));
		}
	    }
    }
}
