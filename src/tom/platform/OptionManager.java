package tom.platform;

import java.util.*;
import tom.library.adt.tnode.types.TNode;

/**
 *
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public interface OptionManager {

  /**
   *
   *
   * @param plugins list of plugins handled by the platform
   * @param args
   * @return an array of String containing the names of the files to compile
   */
  public abstract Object[] initOptionManagement(List plugins, String[] args);

  /**
   *
   *
   * @param optionName the option's name
   * @return the option's value as an Object
   */
  public abstract Object getOptionValue(String optionName);

  /**
   *
   *
   * @param name the option's name
   * @param value the option's value
   */
  public abstract void setOptionValue(String name, Object value);

  /**
   *
   *
   * @param xmlDocument
   */
  public void setGlobalOptionList(TNode xmlDocument);

}
