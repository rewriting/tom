package tom.engine.treeformatter;

import java.util.Map;

import tom.engine.tools.TomGenericPlugin;

/**
 * This plugin transform newparser's result (CST) into a Code (AST)
 * compatible with other plugins in the compilation chain.
 */
public class TreeFormatterPlugin extends TomGenericPlugin {

  public TreeFormatterPlugin() {
    super("TreeFormatterPlugin");
  }


//  public void setArgs(Object[] arg) {
    /*if ( (arg[0]==null ||  arg[0] instanceof Code)
        && (arg[1] instanceof TomStreamManager) ) {

      term = (Code)arg[0];
      streamManager = (TomStreamManager)arg[1];
    } else {
      TomMessage.error(getLogger(),null,0,TomMessage.invalidPluginArgument,
         "AstPrinterPlugin", "[Code or null, TomStreamManager]",
         getArgumentArrayString(arg));
    }*/
//  }

  @Override
  public void run(Map informationTracker){
    //System.out.println("this is a lazy plugin doing nothing");
  }

}
