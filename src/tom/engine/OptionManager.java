package jtom;

import java.util.*;
import jtom.adt.tnode.types.*;

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
   * @return
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
   * @param key the option's name
   * @param value the option's value
   */
  public abstract void putOptionValue(Object key, Object value);

  /**
   *
   *
   * @param xmlDocument
   */
  public void extractOptionList(TNode xmlDocument);
}
