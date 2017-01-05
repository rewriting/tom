/*
 * Gom
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
  %include { ../../platform/adt/platformoption/PlatformOption.tom }

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
    `concPlatformOption(
        PluginOption("generator", "g", "Select Generator. Possible value: \"shared\"", StringValue("shared"), "type"),
        PluginOption("newtyper", "nt", "New TyperPlugin (activated by default since Tom-2.10)", BooleanValue(True()), ""),
        PluginOption("newparser", "np", "New parser plugin (not activated by default)", BooleanValue(False()), ""),
        PluginOption("optimize", "O", "Optimize generated code: perform inlining", BooleanValue(True()), ""),
        PluginOption("optimize2", "O2", "Optimize generated code: discrimination tree", BooleanValue(False()), ""),
        PluginOption("inlineplus", "", "Make inlining active", BooleanValue(False()), ""),
        PluginOption("withCongruenceStrategies", "wcs", "Include the definition of congruence strategies in the generate file.tom file", BooleanValue(False()), ""),
        PluginOption("withSeparateCongruenceStrategies", "wscs", "Generate the definition of congruence strategies in _file.tom file", BooleanValue(False()), ""),
        PluginOption("multithread", "mt", "Generate code compatible with multi-threading", BooleanValue(False()), ""),
        PluginOption("nosharing", "ns", "Generate code without maximal sharing", BooleanValue(False()), ""),
        PluginOption("jmicompatible", "jmi", "Generate code whose syntax is compatible with JMI standards (capitalize getters and setters)", BooleanValue(False()), "")
        );

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
