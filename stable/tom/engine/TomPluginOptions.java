package jtom;

import jtom.adt.options.types.*;

/**
 * This interface contains the option-related methods of TomPlugin.
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public interface TomPluginOptions {

  /**
   * Returns a list containing the options the plugin declares.
   *
   * @return the options declared by the plugin
   */
  public abstract TomOptionList declaredOptions();

  /**
   * Returns a list containing the options the plugin requires in order
   * to do what is expected of it. A plugin may indeed require that
   * some options are set to specific values depending on which tasks
   * it is told to accomplish.
   *
   * @return the plugin's prerequisites
   */
  public abstract TomOptionList requiredOptions();

  /**
   * Sets the option whose name is given to the specified value.
   *
   * @param name the option's name
   * @param value the option's value
   */
  public abstract void setOption(String name, Object value);
}
