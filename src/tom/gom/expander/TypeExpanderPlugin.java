/*
 * Gom
 * 
 * Copyright (c) 2000-2009, INRIA
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

import java.io.File;
import java.util.logging.Level;
import java.util.Map;

import tom.platform.PlatformLogRecord;
import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;

/**
 * The responsability of the TypeExpander plugin is to 
 * produce an abstract view of the Gom input with type information
 */
public class TypeExpanderPlugin extends GomGenericPlugin {

  public static final String TYPED_SUFFIX = ".tfix.gom.typed";
  public static final String TYPEDHOOK_SUFFIX = ".tfix.hooks.typed";

  /** the list of included modules */
  private GomModuleList moduleList;
  private ModuleList typedModuleList;
  private HookDeclList typedHookList;
  
  /** The constructor*/
  public TypeExpanderPlugin() {
    super("TypeExpander");
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
    if (arg[0] instanceof GomModuleList) {
      moduleList = (GomModuleList)arg[0];
      setGomEnvironment((GomEnvironment)arg[1]);
    } else {
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"TypeExpander", "[GomModuleList,GomEnvironment]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run(Map informationTracker) {
    boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();
    getLogger().log(Level.INFO, "Start typing");
    TypeExpander typer = new TypeExpander(getGomEnvironment());
    typedModuleList = typer.expand(moduleList);
    if(typedModuleList == null) {
      getLogger().log(Level.SEVERE, 
          GomMessage.expansionIssue.getMessage(),
          getStreamManager().getInputFileName());
    } else {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try { tom.library.utils.Viewer.toTree(typedModuleList,swriter); }
      catch(java.io.IOException e) { e.printStackTrace(); }
      getLogger().log(Level.FINE, "Typed Modules:\n{0}",swriter);
      getLogger().log(Level.INFO, "Expansion succeeds");
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPED_SUFFIX, typedModuleList);
      }
    }
    HookTypeExpander hooktyper = new HookTypeExpander(typedModuleList,getGomEnvironment());
    typedHookList = hooktyper.expand(moduleList);
    if(typedHookList == null) {
      getLogger().log(Level.SEVERE, 
          GomMessage.hookExpansionIssue.getMessage(),
          getStreamManager().getInputFileName());
    } else {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try{ tom.library.utils.Viewer.toTree(typedHookList,swriter); }
      catch(java.io.IOException e) { e.printStackTrace(); }
      getLogger().log(Level.FINE, "Typed Hooks:\n{0}",swriter);
      getLogger().log(Level.INFO, "Hook expansion succeeds");
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPEDHOOK_SUFFIX, typedHookList);
      }
    }
    informationTracker.put(KEY_LAST_GEN_MAPPING,getGomEnvironment().getLastGeneratedMapping());
  }

  /**
   * inherited from plugin interface
   * returns an array containing the parsed module and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{
      typedModuleList, typedHookList, getGomEnvironment()
    };
  }
}
