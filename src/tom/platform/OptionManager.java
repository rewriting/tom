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
   * @param plugins
   * @return
   */
  public abstract void setPlugins(List plugins);

  /**
   *
   *
   * @param args
   * @return an array of String containing the names of the files to compile
   */
  public abstract String[] optionManagement(String[] args);

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
   * @param optionName the option's name
   * @return the option's value as a boolean
   */
  public abstract boolean getOptionBooleanValue(String optionName);

  /**
   *
   *
   * @param optionName the option's name
   * @return the option's value as an int
   */
  public abstract int getOptionIntegerValue(String optionName);

  /**
   *
   *
   * @param optionName the option's name
   * @return the option's value as a String
   */
  public abstract String getOptionStringValue(String optionName);


  /**
   *
   *
   * @param xmlDocument
   */
  public void extractOptionList(TNode xmlDocument);
}
