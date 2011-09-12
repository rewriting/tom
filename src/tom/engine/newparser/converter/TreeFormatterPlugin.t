package tom.engine.newparser.converter;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Map;
import java.util.Collection;
import java.util.LinkedList;

import tom.engine.Tom;
import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomsignature.types.tomsymbollist.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.cst.types.*;

/**
 * This plugin transform newparser's result (CST) into a Code (AST)
 * compatible with other plugins in the compilation chain.
 */
public class TreeFormatterPlugin extends TomGenericPlugin {

  %include { sl.tom }
  %include { util/types/Collection.tom }
  %include { ../../adt/cst/CST.tom }

  private static Logger logger = Logger.getLogger("tom.engine.newparser.converter.TreeFormatterPlugin");
  /** some output suffixes */
  public static final String FORMATTER_SUFFIX = ".tfix.cstast";

  public TreeFormatterPlugin() {
    super("TreeFormatterPlugin");
  }

  @Override
  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");    
    boolean newparser = ((Boolean)getOptionManager().getOptionValue("newparser")).booleanValue();

    try {
      CstProgram cst = (CstProgram) getWorkingTerm();
      Code ast = this.convert(cst);

      // verbose
      TomMessage.info(logger,null,0,TomMessage.tomFormattingPhase, Integer.valueOf((int)(System.currentTimeMillis()-startChrono)) );
      setWorkingTerm(ast);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + FORMATTER_SUFFIX, (Code)getWorkingTerm());
      }
    } catch(Exception e) {
      TomMessage.error(logger,getStreamManager().getInputFileName(),0,TomMessage.exceptionMessage, e.getMessage());
      e.printStackTrace();
    }

  }

  private Code convert(CstProgram t) {
    return `Tom(concCode());
  }

}
