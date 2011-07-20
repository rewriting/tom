package tom.engine.astprinter;

import tom.platform.OptionManager;
import tom.engine.tools.TomGenericPlugin;
import tom.library.utils.Viewer;

import java.util.Map;

public class AstPrinterPlugin extends TomGenericPlugin {

  public AstPrinterPlugin() {
    super("AstPrinterPlugin");
  }

  @Override
  public void run(Map informationTracker) {
  if(((Boolean)getOptionManager().getOptionValue("printast")).booleanValue()){
    Viewer.toTree(term); 
  }
  }

}
