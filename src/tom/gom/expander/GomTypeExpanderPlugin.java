/*
 * Gom
 * 
 * Copyright (c) 2005-2006, INRIA
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
 * The responsability of the GomTypeExpander plugin is to 
 * produce an abstract view of the Gom input with type information
 */
public class GomTypeExpanderPlugin extends GomGenericPlugin {

  public static final String TYPED_SUFFIX = ".tfix.gom.typed";

  /** the list of included modules */
  private GomModuleList moduleList;
  private SortList typedModuleList;
  /** The constructor*/
  public GomTypeExpanderPlugin() {
    super("GomTypeExpander");
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof GomModuleList) {
      moduleList = (GomModuleList)arg[0];
      setStreamManager((GomStreamManager)arg[1]);
    } else {
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomTypeExpander", "[GomModuleList,GomStreamManager]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run() {
    boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();

    getLogger().log(Level.INFO, "Start typing");
    GomTypeExpander typer = new GomTypeExpander(streamManager.getPackagePath());
    typedModuleList = typer.expand(moduleList);
    if(typedModuleList == null) {
      getLogger().log(Level.SEVERE, 
          GomMessage.expansionIssue.getMessage(),
          streamManager.getInputFile().getName());
    } else {
      getLogger().log(Level.FINE, "Typed Modules: "+typedModuleList);
      getLogger().log(Level.INFO, "Expansion succeeds");
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getInputFileNameWithoutSuffix()
            + TYPED_SUFFIX, (aterm.ATerm)typedModuleList);
      }
    }
  }

  /**
   * inherited from plugin interface
   * returns an array containing the parsed module and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{typedModuleList, getStreamManager()};
  }
}
