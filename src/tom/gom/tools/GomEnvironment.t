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
 * Antoine Reilles      e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.tools;

import tom.gom.GomStreamManager;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

import java.util.*;

public class GomEnvironment {
  
	%include { ../adt/gom/Gom.tom}
  /** 
   * GomEnvironment uses the Singleton pattern.
   * Unique instance of the GomEnvironment
   */
  private static GomEnvironment instance;
  private static GomStreamManager streamManager;
  
  /** 
   * A private constructor method to defeat instantiation
   */
  private GomEnvironment() { 
    initBuiltins();
  }
  
  /**
   * Part of the Singleton pattern, get the instance or create it.
   * @returns the instance of the GomEnvironment
   */
  public static GomEnvironment getInstance() {
    if(instance == null) {
      instance = new GomEnvironment();
    }
    return instance;
  }

  private Map importedModules = new HashMap();
  // this map is filled by the GomTypeExpander
  public ModuleDeclList getModuleDependency(ModuleDecl module) {
    ModuleDeclList modulesDecl = (ModuleDeclList) importedModules.get(module);
    return modulesDecl;
  }
  public void addModuleDependency(ModuleDecl module, ModuleDeclList imported) {
    importedModules.put(module,imported);
  }
  public void setStreamManager(GomStreamManager stream) {
    this.streamManager = stream;
  }
  public GomStreamManager getStreamManager() {
    return streamManager;
  }

  private Map builtinSorts = new HashMap();
  private void initBuiltins() {
    builtinSorts.put("int",`BuiltinSortDecl("int"));
    builtinSorts.put("String",`BuiltinSortDecl("String"));
    builtinSorts.put("double",`BuiltinSortDecl("double"));
    builtinSorts.put("aterm",`BuiltinSortDecl("aterm"));
    builtinSorts.put("atermlist",`BuiltinSortDecl("atermlist"));
  }

  /**
   * Check is the argument is a builtin module name
   * Those are not parsed, since they only declare
   * operators for the tom signature, with no support
   */
  public boolean isBuiltin(String moduleName) {
    return builtinSorts.containsKey(moduleName);
  }
  public SortDecl builtinSort(String sortname) {
    if (isBuiltin(sortname)) {
      return `BuiltinSortDecl(sortname);
    } else {
      throw new GomRuntimeException("Not a builtin sort: "+sortname);
    }
  }

}
