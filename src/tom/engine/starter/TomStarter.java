/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

package jtom.starter;

import java.util.logging.*;
import jtom.*;
import jtom.tools.*;
import aterm.*;
import tom.platform.RuntimeAlert;

/**
 * The TomStarter "plugin". Only here to initialize the TomStreamManager
 * and to initalize the plugin platform set/getargs process with it.
 * The StreamManager contains also the reference to the SymbolTable.
 */
public class TomStarter extends TomGenericPlugin {

  /** The args to set during run and to return */
  private Object[] argToRelay;
  /** Saved information during setArgs */
  private String fileName;
  
  /** Constructor*/
  public TomStarter() {
    super("TomStarter");
  }
  
  /**
   * inherited from plugin interface
   * arg[0] should contain the input file name
   */
  public RuntimeAlert setArgs(Object[] arg) {
    if (arg[0] instanceof String) {
      fileName = (String)arg[0];  
    } else {
      getLogger().log(Level.SEVERE, "InvalidPluginArgument",
                      new Object[]{"VasStarter", "[String]",
                                   getArgumentArrayString(arg)});
    }
    return new RuntimeAlert();
  }

  /**
   * inherited from plugin interface
   * Create the VasStreamManager as input for next plugin
   */
  public RuntimeAlert run() {
    TomStreamManager streamManager = new TomStreamManager();
    streamManager.initializeFromOptionManager(getOptionManager());
    streamManager.prepareForInputFile(fileName);
    argToRelay = new Object[]{streamManager};
    return new RuntimeAlert();
  }
  
  /**
   * inherited from plugin interface
   * returns argTorelay initialized during run call
   */
  public Object[] getArgs() {
    return argToRelay;
  }

} // class TomStarter
