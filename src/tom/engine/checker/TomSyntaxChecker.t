package jtom.checker;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.types.*;

/**
 * The TomSyntaxChecker plugin.
 */
public class TomSyntaxChecker extends TomChecker {

  %include { ../adt/TomSignature.tom }
  %include{ ../adt/Options.tom }

  public TomSyntaxChecker() {
    super("TomSyntaxChecker");
  }

  public void run() {
    if(isActivated()) {	
      try {	
        long startChrono = System.currentTimeMillis();
        reinit();
        checkSyntax( (TomTerm)getTerm() );
        if(verbose) {
          System.out.println("TOM syntax checking phase (" +(System.currentTimeMillis()-startChrono)+ " ms)");
        }

        environment().printAlertMessage("TomSyntaxChecker");
        
        if(!environment().isEclipseMode()) {
          // remove all warning (in command line only)
          environment().clearWarnings();
        }
      } catch (Exception e) {
        environment().messageError("Exception occurs in TomSyntaxChecker: "+e.getMessage(), 
                                   environment().getInputFile().getName(), TomMessage.DEFAULT_ERROR_LINE_NUMBER);
        e.printStackTrace();
      }
    } else { // syntax checker desactivated
      if(verbose) {
        System.out.println("The syntax checker is not activated and thus WILL NOT RUN.");
      }
    }
	}


  private boolean isActivated() {
    return !getServer().getOptionBooleanValue("noCheck");
  }
}
