package jtom.checker;

import java.util.logging.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;

/**
 * The TomTypeChecker plugin.
 */
public class TomTypeChecker extends TomChecker {

  %include { adt/TomSignature.tom }
  %include { adt/PlatformOption.tom }

  public TomTypeChecker() {
    super("TomTypeChecker");
  }

  public void run() {
    if(isActivated()) {
      try {
      int errorsAtStart = getPluginPlatform().getStatusHandler().nbOfErrors();
      int warningsAtStart = getPluginPlatform().getStatusHandler().nbOfWarnings();

        long startChrono = System.currentTimeMillis();
        checkTypeInference( (TomTerm)getTerm() );
			
        if(verbose) {
          System.out.println("TOM type checking phase (" +(System.currentTimeMillis()-startChrono)+ " ms)");
        }

        printAlertMessage(errorsAtStart, warningsAtStart);
	  
      } catch (Exception e) {
	  getLogger().log( Level.SEVERE,
			   "ExceptionMessage",
			   new Object[]{environment().getInputFile().getName(),"TomTypeChecker",e.getMessage()} );
        e.printStackTrace();
      }
    } else { // type checker desactivated
	
      if(verbose) {
	System.out.println("The type checker is not activated and thus WILL NOT RUN.");
      }
    }
  }

  private boolean isActivated() {
    return !getPluginPlatform().getOptionBooleanValue("noCheck");
  }
}
