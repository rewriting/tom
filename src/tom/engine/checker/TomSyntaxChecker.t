package jtom.checker;

import java.util.logging.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;
import tom.platform.*;

/**
 * The TomSyntaxChecker plugin.
 */
public class TomSyntaxChecker extends TomChecker {

  %include { adt/TomSignature.tom }
  %include { adt/PlatformOption.tom }

  /** the declared options string */
  public static final String DECLARED_OPTIONS = "<options><boolean name='NoSyntaxCheck' altName='' description='Do not perform syntax checking' value='true'/></options>";
  
  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomSyntaxChecker.DECLARED_OPTIONS);
  }
  
  /** Constructor */
  public TomSyntaxChecker() {
    super("TomSyntaxChecker");
  }


  public void run() {
    if(isActivated()) {	
      strictType = !getOptionBooleanValue("lazyType");
      int errorsAtStart = getStatusHandler().nbOfErrors();
      int warningsAtStart = getStatusHandler().nbOfWarnings();
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        checkSyntax((TomTerm)getWorkingTerm());
        // verbose
        getLogger().log(Level.INFO, "TomSyntaxCheckingPhase",
                        new Integer((int)(System.currentTimeMillis()-startChrono)));      
        //printAlertMessage(errorsAtStart, warningsAtStart);
      } catch (Exception e) {
        getLogger().log(Level.SEVERE, "ExceptionMessage",
                        new Object[]{getClass().getName(),
                                     getStreamManager().getInputFile().getName(),
                                     e.getMessage() });
        e.printStackTrace();
      }
    } else {
      // syntax checker desactivated
      getLogger().log(Level.INFO, "SyntaxCheckerInactivated");
    }
  }
  
  private boolean isActivated() {
    return !getOptionBooleanValue("NoSyntaxCheck");
  }

} // class TomSyntaxChecker
