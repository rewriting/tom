package tom.engine.newparser;

import java.util.ArrayList;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.newparser.parser.minitom.types.*;

/**
 * Create a AST (as used by most of compilation chain) from a CST
 * (as produced bay first parsing step).
 */
public class CSTAdaptor {

  %include{ ../adt/code/Code.tom }

  private CSTAdaptor() {;}

  
  public static Code adapt(gt_Program cst) {
    return `TargetLanguageToCode(ITL(""));
  }
 
}
