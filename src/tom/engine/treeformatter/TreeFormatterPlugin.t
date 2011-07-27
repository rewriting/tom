package tom.engine.treeformatter;

import java.util.Map;

import tom.engine.Tom;
import tom.engine.TomMessage;
import tom.engine.TomStreamManager;

import tom.engine.tools.TomGenericPlugin;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.newparser.parser.minitom.types.*;

/**
 * This plugin transform newparser's result (CST) into a Code (AST)
 * compatible with other plugins in the compilation chain.
 */
public class TreeFormatterPlugin extends TomGenericPlugin {

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

  }

}
