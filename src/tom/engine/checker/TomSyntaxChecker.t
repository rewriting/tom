package jtom.checker;

import java.util.logging.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;

/**
 * The TomSyntaxChecker plugin.
 */
public class TomSyntaxChecker extends TomChecker {

  %include { adt/TomSignature.tom }
  %include { adt/PlatformOption.tom }

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

        environment().printAlertMessage("TomSyntaxChecker"); // TODO: soon useless
        
        if(!environment().isEclipseMode()) {
          // remove all warning (in command line only)
          environment().clearWarnings();
        }
      } catch (Exception e) {
	  getLogger().log(Level.SEVERE,
			  "ExceptionMessage",
			  new Object[]{environment().getInputFile().getName(),"TomSyntaxChecker",e.getMessage()});
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
