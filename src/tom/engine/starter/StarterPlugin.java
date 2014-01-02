/*
 * 
 * TOM - To One Matching Compiler
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.starter;

import java.util.Map;

import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.tools.TomGenericPlugin;

/**
 * The TomStarter "plugin". Only here to initialize the TomStreamManager
 * and to initalize the plugin platform set/getargs process with it.
 * The StreamManager contains also the reference to the SymbolTable.
 */
public class StarterPlugin extends TomGenericPlugin {

  /** The args to set during run and to return */
  private Object[] argToRelay;
  /** Saved information during setArgs */
  private String fileName;
  
  /** Constructor*/
  public StarterPlugin() {
    super("StarterPlugin");
  }
  
  /**
   * inherited from plugin interface
   * arg[0] should contain the input file name
   */
  public void setArgs(Object[] arg) {
    if (arg[0] instanceof String) {
      fileName = (String)arg[0];
    } else {
      TomMessage.error(getLogger(), null, 0, TomMessage.invalidPluginArgument,
          "StarterPlugin", "[String]", getArgumentArrayString(arg));
    }
  }

  /**
   * inherited from plugin interface
   * Create the VasStreamManager as input for next plugin
   */
  public void run(Map informationTracker) {
    TomStreamManager localStreamManager = new TomStreamManager();
    localStreamManager.initializeFromOptionManager(getOptionManager());
    localStreamManager.prepareForInputFile(fileName);
    //System.out.println("(debug) I'm in the Tom Starter : file / TSM"+fileName+" / "+localStreamManager.toString());
    argToRelay = new Object[]{localStreamManager};
  }
  
  /**
   * inherited from plugin interface
   * returns argToRelay initialized during run call
   */
  public Object[] getArgs() {
    return (Object[])argToRelay.clone();
  }

}
