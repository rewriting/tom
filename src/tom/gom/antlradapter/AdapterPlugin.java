/*
 * Gom
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

package tom.gom.antlradapter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import tom.gom.GomMessage;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;

import tom.gom.adt.gom.types.*;

import tom.gom.adt.objects.types.*;

/**
 * The AdapterPlugin translates the algebraic specification into an adapter for
 * the antlr v3 parser generator, and generates the code.
 */
public class AdapterPlugin extends GomGenericPlugin {

  public static final String ADAPTER_SUFFIX = ".tfix.gom.ADAPTER";

  /** the list of sorts to compile */
  private ModuleList moduleList;
  private HookDeclList hookList;

  /** The constructor*/
  public AdapterPlugin() {
    super("GomAntlrAdapter");
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg.length < 3
        || (arg[0] instanceof ModuleList
          && arg[1] instanceof HookDeclList
          && arg[2] instanceof GomEnvironment)) {
      moduleList = (ModuleList) arg[0];
      hookList = (HookDeclList) arg[1];
      setGomEnvironment((GomEnvironment) arg[2]);
    } else {
      GomMessage.error(getLogger(),null,0,
          GomMessage.invalidPluginArgument,
          "AntlrAdapter",
          "[ModuleList,HookDeclList,GomEnvironment]",
          getArgumentArrayString(arg));
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run(Map<String,String> informationTracker) {
    long startChrono = System.currentTimeMillis();
    /* Try to guess tom.home */
    File tomHomePath = null;
    String tomHome = System.getProperty("tom.home");
    try {
      if (null == tomHome) {
        String xmlConfigFilename = getOptionStringValue("X");
        tomHome = new File(xmlConfigFilename).getParent();
      }
      tomHomePath = new File(tomHome).getCanonicalFile();
    } catch (IOException e) {
      GomMessage.finer(getLogger(), null, 0, 
          GomMessage.getCanonicalPathFailure, tomHome);
    }
    AdapterGenerator adapter = new AdapterGenerator(tomHomePath, getGomEnvironment());
    adapter.generate(moduleList,hookList);
    GomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
        GomMessage.gomAntlrAdapterGenerationPhase,
        (System.currentTimeMillis()-startChrono));
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
