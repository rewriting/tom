/*
 * Gom
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 *
 **/

package tom.gom.backend;

import java.util.Map;
import java.io.File;
import java.io.IOException;

import tom.gom.GomMessage;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.gom.adt.objects.types.*;

/**
 * The BackendPlugin handle the code generation
 */
public class BackendPlugin extends GomGenericPlugin {
        private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }    

  /** the list of compiled classes */
  private GomClassList classList;
  private TemplateFactory templateFactory;

  /** The constructor*/
  public BackendPlugin() {
    super("GomBackend");
    templateFactory = new SharedTemplateFactory(getOptionManager(),getGomEnvironment());
  }

  /** the declared options string */
  public static final PlatformOptionList PLATFORM_OPTIONS =
     tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("generator", "g", "Select Generator. Possible value: \"shared\"",  tom.platform.adt.platformoption.types.platformvalue.StringValue.make("shared") , "type") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("newtyper", "nt", "New TyperPlugin (activated by default since Tom-2.10)",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.True.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("newparser", "np", "New parser plugin (not activated by default)",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("optimize", "O", "Optimize generated code: perform inlining",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.True.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("optimize2", "O2", "Optimize generated code: discrimination tree",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("inlineplus", "", "Make inlining active",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("withCongruenceStrategies", "wcs", "Include the definition of congruence strategies in the generate file.tom file",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("withSeparateCongruenceStrategies", "wscs", "Generate the definition of congruence strategies in _file.tom file",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("multithread", "mt", "Generate code compatible with multi-threading",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("nosharing", "ns", "Generate code without maximal sharing",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("jmicompatible", "jmi", "Generate code whose syntax is compatible with JMI standards (capitalize getters and setters)",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") 
        , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) ) ) ) ) ) ) ) ) ) ) 











;

  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return PLATFORM_OPTIONS;
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof GomClassList) {
      classList = (GomClassList)arg[0];
      setGomEnvironment((GomEnvironment)arg[1]);
    } else {
      GomMessage.error(getLogger(),null,0,
          GomMessage.invalidPluginArgument,
          "GomBackend", "[GomClassList,GomEnvironment]",
          getArgumentArrayString(arg));
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run(Map<String,String> informationTracker) {
    long startChrono = System.currentTimeMillis();
    // make sure the environment has the correct streamManager
    getGomEnvironment().setStreamManager(getStreamManager());
    /* Try to guess tom.home */
    File tomHomePath = null;
    String tomHome = System.getProperty("tom.home");
    try {
      if (null == tomHome) {
        String configFilename = getOptionStringValue("X");
        tomHome = new File(configFilename).getParent();
      }
      tomHomePath = new File(tomHome).getCanonicalFile();
    } catch (IOException e) {
      GomMessage.finer(getLogger(),null,0, GomMessage.getCanonicalPathFailure,
          tomHome);
    }
    int generateStratMapping = 0;
    if (getOptionBooleanValue("withCongruenceStrategies")) {
      generateStratMapping = 1;
    }
    if (getOptionBooleanValue("withSeparateCongruenceStrategies")) {
      generateStratMapping = 2;
    }
    boolean multithread = getOptionBooleanValue("multithread");
    boolean nosharing = getOptionBooleanValue("nosharing");
    boolean jmicompatible = getOptionBooleanValue("jmicompatible");
    Backend backend =
      new Backend(templateFactory.getFactory(getOptionManager()),
          tomHomePath, generateStratMapping, multithread, nosharing, jmicompatible,
          getStreamManager().getImportList(),getGomEnvironment());
    backend.generate(classList);
    if (null == classList) {
      GomMessage.error(getLogger(), getStreamManager().getInputFileName(),0,
          GomMessage.generationIssue,
          getStreamManager().getInputFileName());
    } else {
      GomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
          GomMessage.gomGenerationPhase,
          (System.currentTimeMillis()-startChrono));
    }
    informationTracker.put(KEY_LAST_GEN_MAPPING,getGomEnvironment().getLastGeneratedMapping());
  }

  /**
   * inherited from plugin interface
   * returns an array containing the compiled classes and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{getGomEnvironment()};
  }
}
