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
  
  %include { adt/PlatformOption.tom }

  /** The name of the plugin. */
  private String pluginName;

  /** The term the plugin works on. */
  private TomTerm term;

  /** the status handler */
  private StatusHandler statusHandler;

  /** the option manager */
  private OptionManager optionManager;
  
  /**
   * An accessor method, so that the plugin can see its logger.
   *
   * @return the plugin's logger
   */
  protected Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  /**
   * An accessor method, so that the plugin can see the unique Status Handler.
   *
   * @return the common to all Tom plugins statusHandler
   */
  protected StatusHandler getStatusHandler() {
    if(statusHandler == null) {
      findStatusHandler();
    }
    return statusHandler;
  }

  /** Constructor method. */
  public TomGenericPlugin(String name) {
    pluginName = name;
  }

  
  /**
   * From Plugin interface 
   * Puts the input Object into the variable "term", after casting it as a
   * TomTerm. If the cast operation fails, an error will be raised.
   *
   * @param term the input Object
   */
  public void setArg(Object arg) {
    if (arg instanceof TomTerm) {
      term = (TomTerm)arg;
    } else {
      getLogger().log(Level.SEVERE, "TomTermExpected", pluginName);
    }
  }

  /**
   * From Plugin interface 
   * The run() method is not implemented in TomGenericPlugin.
   * The plugin should implement its own run() method itself.
   */
  public abstract void run();

  /**
   * From Plugin interface 
   * Returns the Object "term" (which is really a TomTerm).
   *
   * @return the Object "term"
   */
  public Object getArg() {
    return term;
  }
  
  /**
   * From Plugin interface 
   * The setOptionManager save the reference to the OM.
   */
  public void setOptionManager(OptionManager optionManager) {
    this.optionManager = optionManager;
  }
  
  /**
   * From OptionOwner interface 
   * Returns an empty PlatformOptionList. By default, the plugin is considered
   * to declare no options.
   *
   * @return an empty PlatformOptionList
   */
  public PlatformOptionList getDeclaredOptionList() {
    return `emptyPlatformOptionList();
  }
  
  /**
   * From OptionOwner interface 
   * Returns an empty PlatformOptionList. By default, the plugin is considered
   * to have no prerequisites.
   *
   * @return an empty PlatformOptionList
   */
  public PlatformOptionList getRequiredOptionList() {
    return `emptyPlatformOptionList();
  }
  
  /**
   * From OptionOwner interface 
   * Sets the specified option to the specified value.
   * By default, no further work is done. Sometimes though, a plugin might need
   * to do more work
   * (for instance if altering the value entails a change in another).
   *
   * @param optionName the option's name
   * @param optionValue the option's value
   */
  public void setOption(String optionName, Object optionValue) {
    setOptionValue(optionName, optionValue);
  }

  public void printAlertMessage(int errorsAtStart, int warningsAtStart) {
    if(!environment().isEclipseMode()) {
      
      int nbOfErrors   = statusHandler.nbOfErrors()   - errorsAtStart;
      int nbOfWarnings = statusHandler.nbOfWarnings() - warningsAtStart;

      if( nbOfErrors > 0 ) {
        getLogger().log( Level.SEVERE, "TaskErrorMessage",
                    new Object[]{ pluginName, 
                                  new Integer(nbOfErrors), 
                                  new Integer(nbOfWarnings) } );
      } else if( nbOfWarnings > 0 ) {
        getLogger().log( Level.INFO, "TaskWarningMessage",
                    new Object[]{ pluginName, new Integer(nbOfWarnings) } );
      }
    }
  }

  private void findStatusHandler() {
    Handler[] handlers = Logger.getLogger(Tom.LOG_RADICAL).getHandlers();
    for(int i=0;i<handlers.length;i++) {
      if(handlers[i] instanceof StatusHandler) {
        statusHandler = (StatusHandler)handlers[i];
        break;
      }
    }
  }
  
  public OptionManager getOptionManager() {
    return optionManager;
  }

  public void setOptionValue(String name, Object value) {
    optionManager.setOptionValue(name, value);
  }

  protected Object getOptionValue(String name) {
    return optionManager.getOptionValue(name);
  }

  /**
   * Returns the value of a boolean option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return a boolean that is the option's value
   */
  public boolean getOptionBooleanValue(String optionName) {
    return ((Boolean)getOptionValue(optionName)).booleanValue();
  }

  /**
   * Returns the value of an integer option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return an int that is the option's value
   */
  public int getOptionIntegerValue(String optionName) {
    return ((Integer)getOptionValue(optionName)).intValue();
  }
    
  /**
   * Returns the value of a string option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return a String that is the option's value
   */
  public String getOptionStringValue(String optionName) {
    return (String) getOptionValue(optionName);
  }

} // class TomGenericPlugin
