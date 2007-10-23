/* Generated by TOM (version 2.6alpha): Do not edit this file *//*
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

  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */private static boolean tom_equal_term_char(char t1, char t2) { return  (t1==t2) ;}private static boolean tom_is_sort_char(char t) { return  true ;} private static boolean tom_equal_term_String(String t1, String t2) { return  (t1.equals(t2)) ;}private static boolean tom_is_sort_String(String t) { return  t instanceof String ;}  /* Generated by TOM (version 2.6alpha): Do not edit this file */private static boolean tom_equal_term_int(int t1, int t2) { return  (t1==t2) ;}private static boolean tom_is_sort_int(int t) { return  true ;} private static boolean tom_equal_term_Code(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Code(Object t) { return  t instanceof tom.gom.adt.code.types.Code ;}private static boolean tom_equal_term_Hook(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Hook(Object t) { return  t instanceof tom.gom.adt.objects.types.Hook ;}private static boolean tom_equal_term_SlotField(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotField(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotField ;}private static boolean tom_equal_term_SlotFieldList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotFieldList(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotFieldList ;}private static boolean tom_equal_term_GomClass(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomClass(Object t) { return  t instanceof tom.gom.adt.objects.types.GomClass ;}private static boolean tom_equal_term_ClassName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ClassName(Object t) { return  t instanceof tom.gom.adt.objects.types.ClassName ;}private static boolean tom_equal_term_GomClassList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomClassList(Object t) { return  t instanceof tom.gom.adt.objects.types.GomClassList ;}private static boolean tom_equal_term_ClassNameList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ClassNameList(Object t) { return  t instanceof tom.gom.adt.objects.types.ClassNameList ;}private static boolean tom_equal_term_HookList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookList(Object t) { return  t instanceof tom.gom.adt.objects.types.HookList ;}private static boolean tom_equal_term_Slot(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Slot(Object t) { return  t instanceof tom.gom.adt.gom.types.Slot ;}private static boolean tom_equal_term_ArgList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ArgList(Object t) { return  t instanceof tom.gom.adt.gom.types.ArgList ;}private static boolean tom_equal_term_IdKind(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_IdKind(Object t) { return  t instanceof tom.gom.adt.gom.types.IdKind ;}private static boolean tom_equal_term_GomModuleList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModuleList(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModuleList ;}private static boolean tom_equal_term_GrammarList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GrammarList(Object t) { return  t instanceof tom.gom.adt.gom.types.GrammarList ;}private static boolean tom_equal_term_TypedProduction(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TypedProduction(Object t) { return  t instanceof tom.gom.adt.gom.types.TypedProduction ;}private static boolean tom_equal_term_SectionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SectionList(Object t) { return  t instanceof tom.gom.adt.gom.types.SectionList ;}private static boolean tom_equal_term_OperatorDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OperatorDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.OperatorDeclList ;}private static boolean tom_equal_term_HookDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.HookDeclList ;}private static boolean tom_equal_term_SortDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.SortDeclList ;}private static boolean tom_equal_term_OperatorDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OperatorDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.OperatorDecl ;}private static boolean tom_equal_term_GomModule(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModule(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModule ;}private static boolean tom_equal_term_Pair(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Pair(Object t) { return  t instanceof tom.gom.adt.gom.types.Pair ;}private static boolean tom_equal_term_Section(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Section(Object t) { return  t instanceof tom.gom.adt.gom.types.Section ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) { return  t instanceof tom.gom.adt.gom.types.GomTypeList ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ProductionList(Object t) { return  t instanceof tom.gom.adt.gom.types.ProductionList ;}private static boolean tom_equal_term_Decl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Decl(Object t) { return  t instanceof tom.gom.adt.gom.types.Decl ;}private static boolean tom_equal_term_ImportList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ImportList(Object t) { return  t instanceof tom.gom.adt.gom.types.ImportList ;}private static boolean tom_equal_term_Sort(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Sort(Object t) { return  t instanceof tom.gom.adt.gom.types.Sort ;}private static boolean tom_equal_term_SortDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.SortDecl ;}private static boolean tom_equal_term_HookDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.HookDecl ;}private static boolean tom_equal_term_ModuleDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleDeclList ;}private static boolean tom_equal_term_Module(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Module(Object t) { return  t instanceof tom.gom.adt.gom.types.Module ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_FieldList(Object t) { return  t instanceof tom.gom.adt.gom.types.FieldList ;}private static boolean tom_equal_term_Grammar(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Grammar(Object t) { return  t instanceof tom.gom.adt.gom.types.Grammar ;}private static boolean tom_equal_term_SlotList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotList(Object t) { return  t instanceof tom.gom.adt.gom.types.SlotList ;}private static boolean tom_equal_term_SortList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortList(Object t) { return  t instanceof tom.gom.adt.gom.types.SortList ;}private static boolean tom_equal_term_Arg(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Arg(Object t) { return  t instanceof tom.gom.adt.gom.types.Arg ;}private static boolean tom_equal_term_GomModuleName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModuleName(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModuleName ;}private static boolean tom_equal_term_Field(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Field(Object t) { return  t instanceof tom.gom.adt.gom.types.Field ;}private static boolean tom_equal_term_Production(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Production(Object t) { return  t instanceof tom.gom.adt.gom.types.Production ;}private static boolean tom_equal_term_ModuleDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleDecl ;}private static boolean tom_equal_term_ModuleList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleList(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleList ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomType(Object t) { return  t instanceof tom.gom.adt.gom.types.GomType ;}private static boolean tom_equal_term_ImportedModule(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ImportedModule(Object t) { return  t instanceof tom.gom.adt.gom.types.ImportedModule ;}private static boolean tom_equal_term_Option(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Option(Object t) { return  t instanceof tom.gom.adt.gom.types.Option ;}private static boolean tom_equal_term_HookKind(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookKind(Object t) { return  t instanceof tom.gom.adt.gom.types.HookKind ;}private static  tom.gom.adt.objects.types.ClassName  tom_make_ClassName( String  t0,  String  t1) { return  tom.gom.adt.objects.types.classname.ClassName.make(t0, t1) ; }private static  tom.gom.adt.gom.types.SortDecl  tom_make_BuiltinSortDecl( String  t0) { return  tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl.make(t0) ; } 
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
    builtinSorts.put("boolean",tom_make_ClassName("","boolean"));
    builtinSorts.put("int",tom_make_ClassName("","int"));
    builtinSorts.put("String",tom_make_ClassName("","String"));
    builtinSorts.put("char",tom_make_ClassName("","char"));
    builtinSorts.put("double",tom_make_ClassName("","double"));
    builtinSorts.put("long",tom_make_ClassName("","long"));
    builtinSorts.put("float",tom_make_ClassName("","float"));
    builtinSorts.put("ATerm",tom_make_ClassName("aterm","ATerm"));
    builtinSorts.put("ATermList",tom_make_ClassName("aterm","ATermList"));
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
      return tom_make_BuiltinSortDecl(sortname);
    } else {
      throw new GomRuntimeException("Not a builtin sort: "+sortname);
    }
  }

  public Map builtinSortClassMap() {
    Map sortClass = new HashMap();
    Iterator it = usedBuiltinSorts.keySet().iterator();
    while(it.hasNext()) {
      String name = (String) it.next();
      sortClass.put(tom_make_BuiltinSortDecl(name),(ClassName)usedBuiltinSorts.get(name));
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
