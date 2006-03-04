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

package tom.gom.compiler;

import java.util.logging.Level;

import tom.platform.PlatformLogRecord;
import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomGenericPlugin;

import tom.gom.adt.gom.types.*;

import tom.gom.adt.objects.types.*;

/**
 * The GomCompilerPlugin translates the algebraic specification into a set of
 * classes
 */
public class GomCompilerPlugin extends GomGenericPlugin {
  
  public static final String COMPILED_SUFFIX = ".tfix.gom.compiled";

  /** the list of sorts to compile */
  private SortList sortList;

  /** the list of compiled classes */
  private GomClassList classList;

  /** The constructor*/
  public GomCompilerPlugin() {
    super("GomCompiler");
  }
  
  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof SortList) {
      sortList = (SortList)arg[0];
      setStreamManager((GomStreamManager)arg[1]);
    } else {
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomCompiler", "[SortList,GomStreamManager]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run() {
    boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();

    getLogger().log(Level.INFO, "Start compilation");
    GomCompiler compiler = new GomCompiler();
    classList = compiler.compile(sortList);
    if(classList == null) {
      getLogger().log(Level.SEVERE, 
          GomMessage.compilationIssue.getMessage(),
          streamManager.getInputFileName());
    } else {
      getLogger().log(Level.FINE, "Compiled Modules: "+classList);
      getLogger().log(Level.INFO, "Compilation succeeds");
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getInputFileNameWithoutSuffix()
            + COMPILED_SUFFIX, (aterm.ATerm) classList);
      }
    }
  }

  /**
   * inherited from plugin interface
   * returns an array containing the compiled classes and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{classList, getStreamManager()};
  }

}
