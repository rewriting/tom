import java.util.*;

import tom.platform.ConfigurationManager;
import tom.platform.OptionManager;
import tom.platform.adt.platformoption.types.PlatformOptionList;

/** An OptionManager holding fixed values (J4.1a equivalence helpers). */
public class StubOptionManager implements OptionManager {
  private final Map<String,Object> values = new HashMap<String,Object>();

  public StubOptionManager set(String name, Object value) {
    values.put(name, value);
    return this;
  }

  public int initialize(ConfigurationManager confManager, String[] commandLine) { return 0; }
  public void setGlobalOptionList(PlatformOptionList optionList) { }
  public List<String> getInputToCompileList() { return new ArrayList<String>(); }
  public void setOptionValue(String name, Object value) { values.put(name, value); }

  public Object getOptionValue(String name) {
    if(!values.containsKey(name)) {
      throw new RuntimeException("StubOptionManager: no option " + name);
    }
    return values.get(name);
  }
}
