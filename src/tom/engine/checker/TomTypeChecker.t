package jtom.checker;

import java.util.logging.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;
import tom.platform.*;

/**
 * The TomTypeChecker plugin.
 */
public class TomTypeChecker extends TomChecker {

  %include { adt/TomSignature.tom }
  %include { adt/PlatformOption.tom }

  /** the declared options string */
  public static final String DECLARED_OPTIONS = "<options><boolean name='noTypeCheck' altName='' description='Do not perform type checking' value='false'/></options>";

  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomTypeChecker.DECLARED_OPTIONS);
  }

  /** Constructor */
  public TomTypeChecker() {
    super("TomTypeChecker");
  }
  
  public RuntimeAlert run() {
    RuntimeAlert result = new RuntimeAlert();
    if(isActivated()) {
      strictType = !getOptionBooleanValue("lazyType");
      //int errorsAtStart = getStatusHandler().nbOfErrors();
      //int warningsAtStart = getStatusHandler().nbOfWarnings();
      long startChrono = System.currentTimeMillis();
      try {
        checkTypeInference( (TomTerm)getWorkingTerm() );
        // verbose
        getLogger().log( Level.INFO, "TomTypeCheckingPhase",
                         new Integer((int)(System.currentTimeMillis()-startChrono)) );
        //printAlertMessage(errorsAtStart, warningsAtStart);
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, "ExceptionMessage",
                         new Object[]{getClass().getName(), getStreamManager().getInputFile().getName(),e.getMessage()} );
        e.printStackTrace();
        // result.addError();
      }
    } else {
      // type checker desactivated    
      getLogger().log(Level.INFO, "TypeCheckerInactivated");
    }
    return result;
  }
  
  private boolean isActivated() {
    return !getOptionBooleanValue("noTypeCheck");
  }
  
} // class TomTypeChecker
