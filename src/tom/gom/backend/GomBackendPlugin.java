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

package tom.gom.backend;

import java.util.logging.Level;

import tom.platform.PlatformLogRecord;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomGenericPlugin;

import tom.gom.adt.objects.types.*;

/**
 * The GomBackendPlugin handle the code generation
 */
public class GomBackendPlugin extends GomGenericPlugin {
  
  /** the list of compiled classes */
  private GomClassList classList;

  /** The constructor*/
  public GomBackendPlugin() {
    super("GomBackend");
  }
  
  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof GomClassList) {
	    classList = (GomClassList)arg[0];
      setStreamManager((GomStreamManager)arg[1]);
    } else {
      getLogger().log(Level.SEVERE, 
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomBackend", "[GomClassList,GomStreamManager]",
            getArgumentArrayString(arg)});
    }
  }
  
  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run() {
    getLogger().log(Level.INFO, "Start compilation");
    // make sure the environment has the correct streamManager
    environment().setStreamManager(getStreamManager());
    GomBackend backend = new GomBackend();
    backend.generate(classList);
    if(classList == null) {
      getLogger().log(Level.SEVERE, 
          GomMessage.generationIssue.getMessage(),
          streamManager.getInputFile().getName());
    } else {
      getLogger().log(Level.INFO, "Code generation succeeds");
    }
  }
  
  /**
   * inherited from plugin interface
   * returns an array containing the compiled classes and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{getStreamManager()};
  }

}
