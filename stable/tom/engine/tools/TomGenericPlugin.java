/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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

package tom.engine.tools;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import tom.engine.Tom;
import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomtype.types.TomType;
import tom.platform.OptionManager;
import tom.platform.Plugin;
import tom.platform.StatusHandler;
import tom.platform.adt.platformoption.types.PlatformOptionList;


/**
 * TomGenericPlugin is an abstract class which provides some code to
 * develop plugins faster. The methods implemented here can be used as such
 * most of the time as they provide a default behaviour shared by most plugins.
 * If this behaviour is not the one that is expected though, they should be
 * overridden. Just remember : extending this class is by no means necessary
 * for a plugin, the only constraint is to implement the Plugin interface.
 */
public abstract class TomGenericPlugin implements Plugin {
  
        private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }    

  public final static String KEY_LAST_GEN_MAPPING = "lastGeneratedMapping";
  public final static String KEY_LAST_READ_LINE = "lastReadLine";
  
  /** The name of the plugin. */
  private String pluginName;

  /** The term the plugin works on. */
  protected Code term;

  /** The streamanager */
  protected TomStreamManager streamManager;
  
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
   * Code. If the cast operation fails, an error will be raised.
   *
   * @param arg the input Object
   */
  public void setArgs(Object[] arg) {
    if (arg[0] instanceof Code && arg[1] instanceof TomStreamManager) {
      term = (Code)arg[0];
      streamManager = (TomStreamManager)arg[1];
    } else {
      TomMessage.error(getLogger(),null,0,TomMessage.invalidPluginArgument,
         pluginName, "[Code, TomStreamManager]", getArgumentArrayString(arg));
    }
  }

  public void setWorkingTerm(Object arg) {
    if(arg instanceof Code) {
      term = (Code)arg;
    } else {
      TomMessage.error(getLogger(),null,0,TomMessage.codeExpected, pluginName);
    }
  }

  /**
   * From Plugin interface 
   * The run() method is not implemented in TomGenericPlugin.
   * The plugin should implement its own run() method itself.
   */
  public abstract void run(Map informationTracker);

  /**
   * From Plugin interface 
   * Returns the Object "term" (which is really a Code).
   *
   * @return the Object "term"
   */
  public Object[] getArgs() {
    return new Object[]{term, streamManager};
  }
  
  /**
   * Returns the Object "term"
   *
   * @return the Object "term"
   */
  public Object getWorkingTerm() {
    return term;
  }

  public TomStreamManager getStreamManager() {
    return streamManager; 
  }

  public void setStreamManager(TomStreamManager streamManager) {
    this.streamManager = streamManager; 
  }

  public SymbolTable getSymbolTable() {
    return streamManager.getSymbolTable(); 
  }

  protected TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, streamManager.getSymbolTable());
  }
  
  protected TomType getTermType(BQTerm t) {
    return  TomBase.getTermType(t, streamManager.getSymbolTable());
  }

  protected TomType getTermType(TomTerm t) {
    return  TomBase.getTermType(t, streamManager.getSymbolTable());
  }
 
  protected TomType getTermType(Expression t) {
    return  TomBase.getTermType(t, streamManager.getSymbolTable());
  }

  protected TomType getUniversalType() {
    return streamManager.getSymbolTable().getUniversalType();
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
    return  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
  }
  
  /**
   * From OptionOwner interface 
   * Returns an empty PlatformOptionList. By default, the plugin is considered
   * to have no prerequisites.
   *
   * @return an empty PlatformOptionList
   */
  public PlatformOptionList getRequiredOptionList() {
    return  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
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
  public void optionChanged(String optionName, Object optionValue) {
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

  public String getArgumentArrayString(Object[] arg) {
    StringBuilder argString = new StringBuilder("[");
    for(int i=0;i<arg.length;i++) {
      if(arg[i]==null) {
        argString.append("null");
      } else {
        argString.append(arg[i].getClass().getName());
      }
      if (i < arg.length -1) {
        argString.append(",");
      }
    }
    argString.append("]");
    return argString.toString();
  }
  
}
