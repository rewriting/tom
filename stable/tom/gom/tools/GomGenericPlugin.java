/*
 * Gom
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
 * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
 **/

package tom.gom.tools;

import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.Map;

import tom.platform.OptionManager;
import tom.platform.Plugin;
import tom.platform.StatusHandler;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.gom.Gom;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;

public abstract class GomGenericPlugin implements Plugin {

  public GomGenericPlugin(String name) {
    pluginName = name;
    //myadd
    gomEnvironment = new GomEnvironment();
  }

        private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }    

  /** The name of the plugin. */
  private String pluginName;

  /** the status handler */
  private StatusHandler statusHandler;

  /** the option manager */
  private OptionManager optionManager;

  public final static String KEY_LAST_GEN_MAPPING = "lastGeneratedMapping";
  public final static String KEY_LAST_READ_LINE = "lastReadLine";

  /** myadd : GomEnvironment is not yet a Singleton class, therefore it has to be an attribute
   * => need to modify environment() method => become abstract and defined in extended classes
   * add get/set methods
   */
  protected GomEnvironment gomEnvironment;

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
   * @return the common to all Gom plugins statusHandler
   */
  protected StatusHandler getStatusHandler() {
    if(statusHandler == null) {
      findStatusHandler();
    }
    return statusHandler;
  }

  public GomEnvironment getGomEnvironment() {
    return gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomStreamManager getStreamManager() {
    return gomEnvironment.getStreamManager();
  }

  public void setStreamManager(GomStreamManager m) {
    gomEnvironment.setStreamManager(m);
  }

  /**
   * From Plugin interface
   * @param term the input Object
   */
  public abstract void setArgs(Object[] arg);

  /**
   * From Plugin interface
   * The run() method is not implemented in GomGenericPlugin.
   * The plugin should implement its own run() method itself.
   */
  public abstract void run(Map<String,String> informationTracker);

  /**
   * From Plugin interface
   * @return the Object "term"
   */
  public abstract Object[] getArgs();

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
    Handler[] handlers = Logger.getLogger(Gom.LOGRADICAL).getHandlers();
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
    String argString = "[";
    for(int i=0;i<arg.length;i++) {
      if (arg[i] == null) { continue; }
      argString += arg[i].getClass().getName();
      if(i < arg.length-1) {
        argString += ",";
      }
    }
    return argString+"]";
  }
}
