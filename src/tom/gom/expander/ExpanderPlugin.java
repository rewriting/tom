/*
 * Gom
 * 
 * Copyright (c) 2000-2007, INRIA
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

package tom.gom.expander;

import java.util.logging.Level;

import tom.platform.PlatformLogRecord;
import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.GomGenericPlugin;

/**
 * The responsability of the Expander plugin is to
 * parse the Gom files included by the module to be compiled
 *
 * Get the inputs files from GomStreamManager, parse and populate the
 * GomEnvironment
 */
public class ExpanderPlugin extends GomGenericPlugin {

  public static final String EXPANDED_SUFFIX = ".tfix.gom.expanded";

  /** the input module */
  private GomModule module;
  /** the list of included modules */
  private GomModuleList modules;

  /** The constructor*/
  public ExpanderPlugin() {
    super("GomExpander");
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof GomModule) {
      module = (GomModule)arg[0];
      setStreamManager((GomStreamManager)arg[1]);
    } else {
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomExpander", "[GomModule,GomStreamManager]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run() {
    boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();

    getLogger().log(Level.INFO, "Start expanding");
    Expander expander = new Expander(streamManager);
    modules = expander.expand(module);
    if(modules == null) {
      getLogger().log(Level.SEVERE, 
          GomMessage.expansionIssue.getMessage(),
          streamManager.getInputFileName());
    } else {
      getLogger().log(Level.FINE, "Imported Modules: {0}",modules);
      getLogger().log(Level.INFO, "Expansion succeeds");
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getInputFileNameWithoutSuffix()
            + EXPANDED_SUFFIX, (aterm.ATerm)modules.toATerm());
      }
    }
  }

  /**
   * inherited from plugin interface
   * returns an array containing the parsed module and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{modules, getStreamManager()};
  }
}
