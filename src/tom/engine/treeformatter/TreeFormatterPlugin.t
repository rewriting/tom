package tom.engine.treeformatter;

import java.util.Map;

import tom.engine.Tom;
import tom.engine.TomMessage;
import tom.engine.TomStreamManager;

import tom.engine.tools.TomGenericPlugin;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.cst.types.*;

/**
 * This plugin transform newparser's result (CST) into a Code (AST)
 * compatible with other plugins in the compilation chain.
 */
public class TreeFormatterPlugin extends TomGenericPlugin {

  %include {sl.tom}
  %include {../adt/cst/CST.tom}

  protected gt_Program cst;

  public TreeFormatterPlugin() {
    super("TreeFormatterPlugin");
  }


  public void setArgs(Object[] arg) {
    if ( arg[0] instanceof Code && arg[1] instanceof TomStreamManager ) {
      term = (Code)arg[0];
      streamManager = (TomStreamManager)arg[1];
    } else
    if (arg[0] instanceof gt_Program && arg[1] instanceof TomStreamManager ) {
      cst = (gt_Program)arg[0];
      streamManager = (TomStreamManager)arg[1];
    } else {
      TomMessage.error(getLogger(),null,0,TomMessage.invalidPluginArgument,
         "AstPrinterPlugin", "[Code or null, TomStreamManager]",
         getArgumentArrayString(arg));
    }
  }

  @Override
  public void run(Map informationTracker){
/*
    try {
    cst = `BottomUp(toAST()).visit(cst);
    } catch (tom.library.sl.VisitFailure e) {
      System.err.println("UnexpectedException"); // XXX handle this...
    }
*/
  }

  %strategy toAST() extends Identity() {
    visit gt_Block {
      HOSTBLOCK[hContent=code] -> {
        return `wrappedCode(TargetLanguageToCode(TL(code,
                  TextPosition(-1, -1), TextPosition(-1, -1))));
      }
    }
  }

}
