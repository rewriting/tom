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
	strictType = !getOptionBooleanValue("lazyType");
	int errorsAtStart = getStatusHandler().nbOfErrors();
	int warningsAtStart = getStatusHandler().nbOfWarnings();

        long startChrono = System.currentTimeMillis();
        checkTypeInference( (TomTerm)getWorkingTerm() );

	getLogger().log( Level.INFO,
			 "TomTypeCheckingPhase",
			 new Integer((int)(System.currentTimeMillis()-startChrono)) );

        printAlertMessage(errorsAtStart, warningsAtStart);
	  
      } catch (Exception e) {
	  getLogger().log( Level.SEVERE,
			   "ExceptionMessage",
			   new Object[]{environment().getInputFile().getName(),"TomTypeChecker",e.getMessage()} );
        e.printStackTrace();
      }
    } else { // type checker desactivated
	
      getLogger().log(Level.INFO, "The type checker is not activated and thus WILL NOT RUN.");
      
    }
  }

  private boolean isActivated() {
    return !getOptionBooleanValue("noCheck");
  }

} // class TomTypeChecker
