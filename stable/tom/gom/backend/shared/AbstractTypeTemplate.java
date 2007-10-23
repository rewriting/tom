/* Generated by TOM (version 2.6alpha): Do not edit this file *//*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.backend.shared;

import java.io.*;
import java.util.*;

import tom.gom.backend.TemplateClass;
import tom.gom.backend.TemplateHookedClass;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;
import tom.platform.OptionManager;

public class AbstractTypeTemplate extends TemplateHookedClass {
  ClassName visitor;
  ClassNameList sortList;

  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */private static boolean tom_equal_term_char(char t1, char t2) { return  (t1==t2) ;}private static boolean tom_is_sort_char(char t) { return  true ;} private static boolean tom_equal_term_String(String t1, String t2) { return  (t1.equals(t2)) ;}private static boolean tom_is_sort_String(String t) { return  t instanceof String ;}  /* Generated by TOM (version 2.6alpha): Do not edit this file */private static boolean tom_equal_term_int(int t1, int t2) { return  (t1==t2) ;}private static boolean tom_is_sort_int(int t) { return  true ;} private static boolean tom_equal_term_Code(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Code(Object t) { return  t instanceof tom.gom.adt.code.types.Code ;}private static boolean tom_equal_term_Hook(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Hook(Object t) { return  t instanceof tom.gom.adt.objects.types.Hook ;}private static boolean tom_equal_term_SlotField(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotField(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotField ;}private static boolean tom_equal_term_SlotFieldList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotFieldList(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotFieldList ;}private static boolean tom_equal_term_GomClass(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomClass(Object t) { return  t instanceof tom.gom.adt.objects.types.GomClass ;}private static boolean tom_equal_term_ClassName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ClassName(Object t) { return  t instanceof tom.gom.adt.objects.types.ClassName ;}private static boolean tom_equal_term_GomClassList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomClassList(Object t) { return  t instanceof tom.gom.adt.objects.types.GomClassList ;}private static boolean tom_equal_term_ClassNameList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ClassNameList(Object t) { return  t instanceof tom.gom.adt.objects.types.ClassNameList ;}private static boolean tom_equal_term_HookList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookList(Object t) { return  t instanceof tom.gom.adt.objects.types.HookList ;}private static boolean tom_equal_term_Slot(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Slot(Object t) { return  t instanceof tom.gom.adt.gom.types.Slot ;}private static boolean tom_equal_term_ArgList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ArgList(Object t) { return  t instanceof tom.gom.adt.gom.types.ArgList ;}private static boolean tom_equal_term_IdKind(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_IdKind(Object t) { return  t instanceof tom.gom.adt.gom.types.IdKind ;}private static boolean tom_equal_term_GomModuleList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModuleList(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModuleList ;}private static boolean tom_equal_term_GrammarList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GrammarList(Object t) { return  t instanceof tom.gom.adt.gom.types.GrammarList ;}private static boolean tom_equal_term_TypedProduction(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TypedProduction(Object t) { return  t instanceof tom.gom.adt.gom.types.TypedProduction ;}private static boolean tom_equal_term_SectionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SectionList(Object t) { return  t instanceof tom.gom.adt.gom.types.SectionList ;}private static boolean tom_equal_term_OperatorDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OperatorDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.OperatorDeclList ;}private static boolean tom_equal_term_HookDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.HookDeclList ;}private static boolean tom_equal_term_SortDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.SortDeclList ;}private static boolean tom_equal_term_OperatorDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OperatorDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.OperatorDecl ;}private static boolean tom_equal_term_GomModule(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModule(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModule ;}private static boolean tom_equal_term_Pair(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Pair(Object t) { return  t instanceof tom.gom.adt.gom.types.Pair ;}private static boolean tom_equal_term_Section(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Section(Object t) { return  t instanceof tom.gom.adt.gom.types.Section ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) { return  t instanceof tom.gom.adt.gom.types.GomTypeList ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ProductionList(Object t) { return  t instanceof tom.gom.adt.gom.types.ProductionList ;}private static boolean tom_equal_term_Decl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Decl(Object t) { return  t instanceof tom.gom.adt.gom.types.Decl ;}private static boolean tom_equal_term_ImportList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ImportList(Object t) { return  t instanceof tom.gom.adt.gom.types.ImportList ;}private static boolean tom_equal_term_Sort(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Sort(Object t) { return  t instanceof tom.gom.adt.gom.types.Sort ;}private static boolean tom_equal_term_SortDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.SortDecl ;}private static boolean tom_equal_term_HookDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.HookDecl ;}private static boolean tom_equal_term_ModuleDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleDeclList ;}private static boolean tom_equal_term_Module(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Module(Object t) { return  t instanceof tom.gom.adt.gom.types.Module ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_FieldList(Object t) { return  t instanceof tom.gom.adt.gom.types.FieldList ;}private static boolean tom_equal_term_Grammar(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Grammar(Object t) { return  t instanceof tom.gom.adt.gom.types.Grammar ;}private static boolean tom_equal_term_SlotList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotList(Object t) { return  t instanceof tom.gom.adt.gom.types.SlotList ;}private static boolean tom_equal_term_SortList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortList(Object t) { return  t instanceof tom.gom.adt.gom.types.SortList ;}private static boolean tom_equal_term_Arg(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Arg(Object t) { return  t instanceof tom.gom.adt.gom.types.Arg ;}private static boolean tom_equal_term_GomModuleName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModuleName(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModuleName ;}private static boolean tom_equal_term_Field(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Field(Object t) { return  t instanceof tom.gom.adt.gom.types.Field ;}private static boolean tom_equal_term_Production(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Production(Object t) { return  t instanceof tom.gom.adt.gom.types.Production ;}private static boolean tom_equal_term_ModuleDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleDecl ;}private static boolean tom_equal_term_ModuleList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleList(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleList ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomType(Object t) { return  t instanceof tom.gom.adt.gom.types.GomType ;}private static boolean tom_equal_term_ImportedModule(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ImportedModule(Object t) { return  t instanceof tom.gom.adt.gom.types.ImportedModule ;}private static boolean tom_equal_term_Option(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Option(Object t) { return  t instanceof tom.gom.adt.gom.types.Option ;}private static boolean tom_equal_term_HookKind(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookKind(Object t) { return  t instanceof tom.gom.adt.gom.types.HookKind ;}private static boolean tom_is_fun_sym_AbstractTypeClass( tom.gom.adt.objects.types.GomClass  t) { return  (t instanceof tom.gom.adt.objects.types.gomclass.AbstractTypeClass) ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_AbstractTypeClass_ClassName( tom.gom.adt.objects.types.GomClass  t) { return  t.getClassName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_AbstractTypeClass_Mapping( tom.gom.adt.objects.types.GomClass  t) { return  t.getMapping() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_AbstractTypeClass_Visitor( tom.gom.adt.objects.types.GomClass  t) { return  t.getVisitor() ;}private static  tom.gom.adt.objects.types.ClassNameList  tom_get_slot_AbstractTypeClass_SortList( tom.gom.adt.objects.types.GomClass  t) { return  t.getSortList() ;}private static  tom.gom.adt.objects.types.HookList  tom_get_slot_AbstractTypeClass_Hooks( tom.gom.adt.objects.types.GomClass  t) { return  t.getHooks() ;} 

  public AbstractTypeTemplate(File tomHomePath,
                              OptionManager manager,
                              List importList,
                              GomClass gomClass,
                              TemplateClass mapping) {
    super(gomClass,manager,tomHomePath,importList,mapping);
    if (tom_is_sort_GomClass(gomClass)) {{  tom.gom.adt.objects.types.GomClass  tomMatch374NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClass )gomClass);if (tom_is_fun_sym_AbstractTypeClass(tomMatch374NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.ClassName  tomMatch374NameNumber_freshVar_0=tom_get_slot_AbstractTypeClass_Visitor(tomMatch374NameNumberfreshSubject_1);{  tom.gom.adt.objects.types.ClassNameList  tomMatch374NameNumber_freshVar_1=tom_get_slot_AbstractTypeClass_SortList(tomMatch374NameNumberfreshSubject_1);if ( true ) {


        this.visitor = tomMatch374NameNumber_freshVar_0;
        this.sortList = tomMatch374NameNumber_freshVar_1;
        return;
      }}}}}}

    throw new GomRuntimeException(
        "Bad argument for AbstractTypeTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    
    writer.write(
"\npackage "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getPackage()+";\n"/* Generated by TOM (version 2.6alpha): Do not edit this file */+generateImport()+"\n\npublic abstract class "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+" implements shared.SharedObjectWithID, tom.library.sl.Visitable, Comparable "/* Generated by TOM (version 2.6alpha): Do not edit this file */+generateInterface()+" {\n"




);

    if (! hooks.isEmptyconcHook()) {
      mapping.generate(writer); 
    }
    writer.write(
"\n"/* Generated by TOM (version 2.6alpha): Do not edit this file */+generateBlock()+"\n\n  private int uniqueID;\n\n  protected static final shared.SharedObjectFactory factory = shared.SingletonSharedObjectFactory.getInstance();\n  protected static final aterm.ATermFactory atermFactory = aterm.pure.SingletonFactory.getInstance();\n\n  public abstract aterm.ATerm toATerm();\n\n  public abstract String symbolName();\n\n  @Override\n  public String toString() {\n    java.lang.StringBuffer buffer = new java.lang.StringBuffer();\n    toStringBuffer(buffer);\n    return buffer.toString();\n  }\n\n  public abstract void toStringBuffer(java.lang.StringBuffer buffer);\n\n  public abstract int compareTo(Object o);\n\n  public abstract int compareToLPO(Object o);\n\n  public int getUniqueIdentifier() {\n    return uniqueID;\n  }\n\n  public void setUniqueIdentifier(int uniqueID) {\n    this.uniqueID = uniqueID;\n  }\n\n  abstract public "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+" accept("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(visitor)+" v) throws tom.library.sl.VisitFailure;\n}\n"


































);
 }

}
