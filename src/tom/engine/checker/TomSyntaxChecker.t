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
        strictType = !getOptionBooleanValue("lazyType");
        int errorsAtStart = getStatusHandler().nbOfErrors();
        int warningsAtStart = getStatusHandler().nbOfWarnings();
        
        long startChrono = System.currentTimeMillis();
        
        reinit();
        checkSyntax((TomTerm)getWorkingTerm());
        
        getLogger().log(Level.INFO, "TomSyntaxCheckingPhase",
                         new Integer((int)(System.currentTimeMillis()-startChrono)));      
        printAlertMessage(errorsAtStart, warningsAtStart);
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, "ExceptionMessage",
                         new Object[]{getStreamManager().getInputFile().getName(),"TomSyntaxChecker",e.getMessage()} );
        e.printStackTrace();
      }
    } else { // syntax checker desactivated
      getLogger().log(Level.INFO, "The syntax checker is not activated and thus WILL NOT RUN.");
    }
  }
  
  private boolean isActivated() {
    return !getOptionBooleanValue("noCheck");
  }

} // class TomSyntaxChecker
