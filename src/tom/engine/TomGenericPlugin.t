package jtom;

import aterm.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.types.*;

/**
 * TomGenericPlugin is an abstract class which provides some code to
 * develop plugins faster. The methods implemented here can be used as such
 * most of the time as they provide a default behaviour shared by most plugins.
 * If this behaviour is not the one that is expected though, they should be
 * overridden. Just remember : extending this class is by no means necessary
 * for a plugin, the only constraint is to implement the TomPlugin interface.
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public abstract class TomGenericPlugin extends TomBase implements TomPlugin {
  %include{ adt/TomSignature.tom }
  %include{ adt/Options.tom }

  /**
   * The term the plugin works on.
   */
  protected TomTerm term;

  /**
   * Constructor method. Does nothing for the time being.
   */
  public TomGenericPlugin() {
  }

  /**
   * Puts the input ATerm into the variable "term", after casting it as a TomTerm.
   * If the cast operation fails, an error will be raised.
   *
   * @param term the input ATerm
   */
  public void setInput(ATerm term) {
    if (term instanceof TomTerm) {
	    this.term = (TomTerm)term;
    } else {
	    environment().messageError(TomMessage.getString("TomTermExpected"),
                                 this.getClass().getName(),
                                 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    }
  }

  /**
   * Returns the ATerm "term" (which is really a TomTerm).
   *
   * @return the ATerm "term"
   */
  public ATerm getOutput() {
    return term;
  }

  /**
   * The run() method is not implemented in TomGenericPlugin.
   * The plugin should implement its own run() method itself.
   */
  public abstract void run();

  /**
   * Returns an empty TomOptionList. By default, the plugin is considered to declare no options.
   *
   * @return an empty TomOptionList
   */
  public TomOptionList declaredOptions() {
    return `emptyTomOptionList;
  }

  /**
   * Returns an empty TomOptionList. By default, the plugin is considered to have no prerequisites.
   *
   * @return an empty TomOptionList
   */
  public TomOptionList requiredOptions() {
    return `emptyTomOptionList();
  }

  /**
   * Sets the specified option to the specified value.
   * By default, no further work is done. Sometimes though, a plugin might need to do more work
   * (for instance if altering the value entails a change in another).
   *
   * @param optionName the option's name
   * @param optionValue the option's value
   */
  public void setOption(String optionName, Object optionValue) {
    putOptionValue(optionName, optionValue);
  }
}
