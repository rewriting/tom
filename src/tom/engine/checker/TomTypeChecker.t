package jtom.checker;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.types.*;

/**
 * The TomTypeChecker plugin.
 */
public class TomTypeChecker extends TomChecker
{
    %include { ../adt/TomSignature.tom }
    %include{ ../adt/Options.tom }

    public void run()
    {
	if(isActivated())
	    {
		try
		    {
			long startChrono = System.currentTimeMillis();
			boolean verbose = getServer().getOptionBooleanValue("verbose");
			
			checkTypeInference(term);
			
			if(verbose)
			    System.out.println("TOM type checking phase (" +(System.currentTimeMillis()-startChrono)+ " ms)");

			environment().printAlertMessage("TomTypeChecker");
			
			if(!environment().isEclipseMode())
			    {
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
	else // type checker desactivated
	    {
		boolean verbose = getServer().getOptionBooleanValue("verbose");
		
		if(verbose)
		    {
			System.out.println("The type checker is not activated and thus WILL NOT RUN.");
		    }
	    }
    }

  private boolean isActivated() {
    return !getServer().getOptionBooleanValue("noCheck");
  }
}
