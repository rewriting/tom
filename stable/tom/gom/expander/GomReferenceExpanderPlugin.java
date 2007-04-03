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
 * Emilie Balland    e-mail:Emilie.Balland@loria.fr
 * 
 **/

package tom.gom.expander;

import java.io.File;
import java.util.logging.Level;

import tom.platform.PlatformLogRecord;
import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.GomGenericPlugin;

/**
 * The responsability of the GomReferenceExpander plugin is to 
 * produce an abstract view of the Gom input with type information
 */
public class GomReferenceExpanderPlugin extends GomGenericPlugin {

  public static final String TYPED_SUFFIX = ".tfix.gom.referenced";

  /** the declared options string*/
  private static final String DECLARED_OPTIONS = "<options>" + 
    "<boolean name='pointer' altName='tp' description='Extend the signature for pointers' value='false'/>" +
    "<boolean name='termgraph' altName='tg' description='Extend the signature for term-graphs' value='false'/>" +
    "</options>";

  /** the list of included modules */
  private ModuleList typedModuleList;
  private HookDeclList hookList;
  private ModuleList referencedModuleList;
  private HookDeclList referencedHookList;
  /** The constructor*/
  public GomReferenceExpanderPlugin() {
    super("GomReferenceExpander");
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof ModuleList && arg[1] instanceof HookDeclList) {
      typedModuleList = (ModuleList) arg[0];
      hookList = (HookDeclList) arg[1];
      setStreamManager((GomStreamManager)arg[2]);
    } else {
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomReferenceExpander", "[ModuleList,GomStreamManager]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial ModuleList 
   */
  public void run() {
    if(getOptionBooleanValue("termgraph") || getOptionBooleanValue("pointer")) {
      boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();
      getLogger().log(Level.INFO, "Extend the signature");
      String packagePrefix =
        streamManager.getPackagePath().replace(File.separatorChar,'.');
      GomReferenceExpander referencer = new GomReferenceExpander(packagePrefix,getOptionBooleanValue("termgraph"));
      Pair mpair = referencer.expand(typedModuleList,hookList);
      referencedModuleList = mpair.getModules();
      referencedHookList = mpair.getHooks();
      if(referencedModuleList == null) {
        getLogger().log(Level.SEVERE, 
          GomMessage.expansionIssue.getMessage(),
          streamManager.getInputFileName());
      } else {
        getLogger().log(Level.FINE, "Referenced Modules: {0}",referencedModuleList);
        getLogger().log(Level.INFO, "Signature extension succeeds");
        if(intermediate) {
          Tools.generateOutput(getStreamManager().getInputFileNameWithoutSuffix()
              + TYPED_SUFFIX, (aterm.ATerm)referencedModuleList.toATerm());
        }
      }
    }
    else {
      referencedModuleList = typedModuleList;
      referencedHookList = hookList;
    }
  }

  /**
   * inherited from plugin interface
   * returns an array containing the parsed module and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{referencedModuleList, referencedHookList, getStreamManager()};
  }
}
