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
 * Antoine Reilles      e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.tools;

import tom.gom.GomStreamManager;
import tom.gom.adt.gom.types.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

import java.util.*;

public class GomEnvironment {

  %include { ../adt/objects/Objects.tom}
  /**
   * GomEnvironment uses the Singleton pattern.
   * Unique instance of the GomEnvironment
   */
  private static GomEnvironment instance;

  private GomStreamManager streamManager;
  private String lastGeneratedMapping;
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
    builtinSorts.put("boolean",`ClassName("","boolean"));
    builtinSorts.put("int",`ClassName("","int"));
    builtinSorts.put("String",`ClassName("","String"));
    builtinSorts.put("char",`ClassName("","char"));
    builtinSorts.put("double",`ClassName("","double"));
    builtinSorts.put("long",`ClassName("","long"));
    builtinSorts.put("float",`ClassName("","float"));
    builtinSorts.put("ATerm",`ClassName("aterm","ATerm"));
    builtinSorts.put("ATermList",`ClassName("aterm","ATermList"));
  }
  private Map usedBuiltinSorts = new HashMap();

  /**
   * Check if the argument is a builtin module name
   * Those are not parsed, since they only declare
   * operators for the tom signature, with no support
   */
  public void markUsedBuiltin(String moduleName) {
    if (builtinSorts.containsKey(moduleName)) {
      usedBuiltinSorts.put(moduleName,builtinSorts.get(moduleName));
    } else {
      throw new GomRuntimeException("Not a builtin module: "+moduleName);
    }
  }
  public boolean isBuiltin(String moduleName) {
    return builtinSorts.containsKey(moduleName);
  }
  public boolean isBuiltinSort(String sortName) {
    return usedBuiltinSorts.containsKey(sortName);
  }
  public boolean isBuiltinClass(ClassName className) {
    return usedBuiltinSorts.containsValue(className);
  }
  public SortDecl builtinSort(String sortname) {
    if (isBuiltin(sortname)) {
      return `BuiltinSortDecl(sortname);
    } else {
      throw new GomRuntimeException("Not a builtin sort: "+sortname);
    }
  }

  public Map builtinSortClassMap() {
    Map sortClass = new HashMap();
    Iterator it = usedBuiltinSorts.keySet().iterator();
    while(it.hasNext()) {
      String name = (String) it.next();
      sortClass.put(`BuiltinSortDecl(name),(ClassName)usedBuiltinSorts.get(name));
    }
    return sortClass;
  }

  /**
   * Keep track of the file name (full canonical path) of the last Tom mapping
   * Gom generated. This is used to allow Tom to include this mapping when
   * using %gom
   */
  public String getLastGeneratedMapping() {
    return lastGeneratedMapping;
  }

  public void setLastGeneratedMapping(String fileName) {
    lastGeneratedMapping = fileName;
  }
}
