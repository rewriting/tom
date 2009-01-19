/*
 * Gom
 * 
 * Copyright (c) 2000-2008, INRIA
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
import java.util.Map;

import tom.platform.PlatformLogRecord;
import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;

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

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof GomModule) {
      module = (GomModule)arg[0];
      setGomEnvironment((GomEnvironment)arg[1]);
    } else {
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomExpander", "[GomModule,GomEnvironment]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run(Map informationTracker) {
    boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();
    getLogger().log(Level.INFO, "Start expanding");
    Expander expander = new Expander(getGomEnvironment());
    modules = expander.expand(module);
    // for the moment, the symbol table is only intialised for termgraph and freshgom
    if (getOptionBooleanValue("termgraph") || getOptionBooleanValue("termpointer") || getOptionBooleanValue("fresh")) {
      getGomEnvironment().initSymbolTable(modules);
    }
    if(modules == null) {
      getLogger().log(Level.SEVERE, 
          GomMessage.expansionIssue.getMessage(),
          getStreamManager().getInputFileName());
    } else {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try { tom.library.utils.Viewer.toTree(modules,swriter); }
      catch(java.io.IOException e) { e.printStackTrace(); }
      getLogger().log(Level.FINE, "Imported Modules:\n{0}",swriter);
      getLogger().log(Level.INFO, "Expansion succeeds");
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + EXPANDED_SUFFIX, (aterm.ATerm)modules.toATerm());
      }
    }
    informationTracker.put("lastGeneratedMapping",getGomEnvironment().getLastGeneratedMapping());
    //informationTracker.put(java.lang.Thread.currentThread().getId(),getGomEnvironment().getLastGeneratedMapping());
  }

  /**
   * inherited from plugin interface
   * returns an array containing the parsed module and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{modules, getGomEnvironment()};
  }
}
