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
 * Antoine Reilles      Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.starter;

import java.util.logging.Level;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomGenericPlugin;


/**
 * The GomStarter "plugin". Only here to initialize the GomStreamManager
 * and to initalize the plugin platform set/getargs process with it
 */
public class GomStarter extends GomGenericPlugin {

  /** The args to set during run and to return */
  private Object[] argToRelay;
  /** Saved information during setArgs */
  private String fileName;
  
  public GomStarter() {
    super("GomStarter");
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the input file name
   */
  public void setArgs(Object[] arg) {
    if (arg[0] instanceof String) {
      fileName = (String)arg[0];
    } else {
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomStarter", "[String]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the GomStreamManager as input for next plugin
   */
  public void run() {
    GomStreamManager newStreamManager = new GomStreamManager();
    newStreamManager.initializeFromOptionManager(getOptionManager());
    newStreamManager.prepareForInputFile(fileName);
    argToRelay = new Object[]{newStreamManager};
  }

  /**
   * inherited from plugin interface
   * returns argTorelay initialized during run call
   */
  public Object[] getArgs() {
    return argToRelay;
  }
}
