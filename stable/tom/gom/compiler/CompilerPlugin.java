/*
 * Gom
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

package tom.gom.compiler;

import java.util.Map;

import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;

import tom.gom.adt.gom.types.*;

import tom.gom.adt.objects.types.*;

/**
 * The CompilerPlugin translates the algebraic specification into a set of
 * classes
 */
public class CompilerPlugin extends GomGenericPlugin {

  public static final String COMPILED_SUFFIX = ".tfix.gom.compiled";

  /** the list of sorts to compile */
  private ModuleList moduleList;
  private HookDeclList hookList;

  /** the list of compiled classes */
  private GomClassList classList;

  /** The constructor*/
  public CompilerPlugin() {
    super("GomCompiler");
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof ModuleList && arg[1] instanceof HookDeclList) {
      moduleList = (ModuleList) arg[0];
      hookList = (HookDeclList) arg[1];
      setGomEnvironment((GomEnvironment) arg[2]);
    } else {
      GomMessage.error(getLogger(), null, 0, 
          GomMessage.invalidPluginArgument, "GomCompiler", 
          "[ModuleList,HookDeclList,GomEnvironment]", 
          getArgumentArrayString(arg));
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run(Map<String,String> informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    Compiler compiler = new Compiler(getGomEnvironment());
    classList = compiler.compile(moduleList,hookList);
    if (null == classList) {
      GomMessage.error(getLogger(), null, 0, GomMessage.compilationIssue, 
          getStreamManager().getInputFileName());
    } else {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try { tom.library.utils.Viewer.toTree(classList,swriter); }
      catch(java.io.IOException e) { e.printStackTrace(); }
        GomMessage.fine(getLogger(), getStreamManager().getInputFileName(), 0,
            GomMessage.compiledModules, swriter);
        GomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
            GomMessage.gomCompilationPhase,
            (System.currentTimeMillis()-startChrono));
      if (intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + COMPILED_SUFFIX, (aterm.ATerm)classList.toATerm());
      }
    }
    informationTracker.put(KEY_LAST_GEN_MAPPING,getGomEnvironment().getLastGeneratedMapping());
  }

  /**
   * inherited from plugin interface
   * returns an array containing the compiled classes and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{
      classList,
      getGomEnvironment()
    };
  }
}
