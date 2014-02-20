/*
 * Gom
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
 * Emilie Balland    e-mail:Emilie.Balland@loria.fr
 *
 **/

package tom.gom.expander;

import java.util.Map;

import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;

public class GraphExpanderPlugin extends GomGenericPlugin {

  public static final String TYPED_SUFFIX = ".tfix.gom.referenced";

  /** the declared options string*/
  private static final String DECLARED_OPTIONS = "<options>" +
    "<boolean name='termpointer' altName='tp' description='Extend the signature for term pointers' value='false'/>" +
    "<boolean name='termgraph' altName='tg' description='Extend the signature for term-graphs' value='false'/>" +
    "</options>";

  /** the list of included modules */
  private ModuleList typedModuleList;
  private HookDeclList hookList;
  private ModuleList referencedModuleList;
  private HookDeclList referencedHookList;

  /** The constructor */
  public GraphExpanderPlugin() {
    super("GraphExpander");
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof ModuleList && arg[1] instanceof HookDeclList) {
      typedModuleList = (ModuleList) arg[0];
      hookList = (HookDeclList) arg[1];
      setGomEnvironment((GomEnvironment)arg[2]);
    } else {
      GomMessage.error(getLogger(),null,0, GomMessage.invalidPluginArgument,
          "GraphExpander", "[ModuleList,GomEnvironment]",
          getArgumentArrayString(arg));
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial ModuleList
   */
  public void run(Map<String,String> informationTracker) {
    if(getOptionBooleanValue("termgraph") || getOptionBooleanValue("termpointer")) {
      boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();
      GomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
          GomMessage.signatureExtension);
      GraphExpander expander = new GraphExpander(getOptionBooleanValue("termgraph"),getGomEnvironment());
      Pair mpair = expander.expand(typedModuleList,hookList);
      referencedModuleList = mpair.getModules();
      referencedHookList = mpair.getHookDecls();
      if(referencedModuleList == null) {
        GomMessage.error(getLogger(),
            getStreamManager().getInputFileName(), 0,
            GomMessage.expansionIssue);
      } else {
        GomMessage.fine(getLogger(), getStreamManager().getInputFileName(), 0,
            GomMessage.referencedModules,referencedModuleList);
        GomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
            GomMessage.signatureExtensionSuccess);
        if(intermediate) {
          Tools.generateOutput(getStreamManager().getOutputFileName()
              + TYPED_SUFFIX, (aterm.ATerm)referencedModuleList.toATerm());
        }
      }
    }
    else {
      referencedModuleList = typedModuleList;
      referencedHookList = hookList;
    }
    informationTracker.put(KEY_LAST_GEN_MAPPING,
        getGomEnvironment().getLastGeneratedMapping());
  }

  /**
   * inherited from plugin interface
   * returns an array containing the parsed module and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{referencedModuleList, referencedHookList, getGomEnvironment()};
  }
}
