/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package jtom.tools;

import java.text.*;
import java.util.logging.*;

import aterm.*;

import jtom.*;
import jtom.adt.tomsignature.types.*;

import tom.platform.*;
import tom.platform.adt.platformoption.types.*;

/**
 * TomGenericPlugin is an abstract class which provides some code to
 * develop plugins faster. The methods implemented here can be used as such
 * most of the time as they provide a default behaviour shared by most plugins.
 * If this behaviour is not the one that is expected though, they should be
 * overridden. Just remember : extending this class is by no means necessary
 * for a plugin, the only constraint is to implement the Plugin interface.
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public abstract class TomGenericPlugin extends TomBase implements Plugin {

  %include{ adt/TomSignature.tom }
  %include{ adt/PlatformOption.tom }

  /** The name of the plugin. */
  private String pluginName;

  /** The term the plugin works on. */
  private TomTerm term;

  /** The plugin's logger. */
  private Logger logger;

  /**
   * An accessor method, so that the plugin can see its logger.
   *
   * @return the plugin's logger
   */
  protected Logger getLogger() {
    return logger;
  }

  /** Constructor method. */
  public TomGenericPlugin(String name) {
    pluginName = name;
    logger = Logger.getLogger(getClass().getName());
  }

  /**
   * Puts the input ATerm into the variable "term", after casting it as a TomTerm.
   * If the cast operation fails, an error will be raised.
   *
   * @param term the input ATerm
   */
  public void setTerm(ATerm term) {
    if (term instanceof TomTerm) {
      this.term = (TomTerm)term;
    } else {
      logger.log(Level.SEVERE,
		 "TomTermExpected",
		 pluginName);
    }
  }

  /**
   * Returns the ATerm "term" (which is really a TomTerm).
   *
   * @return the ATerm "term"
   */
  public ATerm getTerm() {
    return term;
  }

  /**
   * The run() method is not implemented in TomGenericPlugin.
   * The plugin should implement its own run() method itself.
   */
  public abstract void run();

  /**
   * Returns an empty PlatformOptionList. By default, the plugin is considered to declare no options.
   *
   * @return an empty PlatformOptionList
   */
  public PlatformOptionList declaredOptions() {
    return `emptyPlatformOptionList;
  }

  /**
   * Returns an empty PlatformOptionList. By default, the plugin is considered to have no prerequisites.
   *
   * @return an empty PlatformOptionList
   */
  public PlatformOptionList requiredOptions() {
    return `emptyPlatformOptionList();
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

  public void printAlertMessage(int errorsAtStart, int warningsAtStart) {
    if(!environment().isEclipseMode()) {
      StatusHandler status = getPluginPlatform().getStatusHandler();

      int nbOfErrors   = status.nbOfErrors()   - errorsAtStart;
      int nbOfWarnings = status.nbOfWarnings() - warningsAtStart;

      if( nbOfErrors > 0 ) {
	logger.log( Level.OFF,
		    "TaskErrorMessage",
		    new Object[]{ pluginName, 
				  new Integer(nbOfErrors), 
				  new Integer(nbOfWarnings) } );
      } else if( nbOfWarnings > 0 ) {
	logger.log( Level.OFF,
		    "TaskWarningMessage",
		    new Object[]{ pluginName, new Integer(nbOfWarnings) } );
      }
    }
  }

}
