package tom.engine.astprinter;

import tom.engine.tools.TomGenericPlugin;
import tom.library.utils.Viewer;

import java.util.Map;

public class AstPrinterPlugin extends TomGenericPlugin {

  public AstPrinterPlugin() {
    super("AstPrinterPlugin");
  }

  @Override
  public void run(Map informationTracker) {
    Viewer.toTree(term); 
  }

}
