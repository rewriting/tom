/* Generated by TOM (version 2.5alpha): Do not edit this file *//*
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

public class AbstractTypeTemplate extends TemplateHookedClass {
  ClassName visitor;
  ClassNameList sortList;

  /* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file */   private static boolean tom_equal_term_GomClass(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_GomClass(Object t) {  return  t instanceof tom.gom.adt.objects.types.GomClass  ;}private static boolean tom_equal_term_ClassName(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_ClassName(Object t) {  return  t instanceof tom.gom.adt.objects.types.ClassName  ;}private static boolean tom_equal_term_ClassNameList(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_ClassNameList(Object t) {  return  t instanceof tom.gom.adt.objects.types.ClassNameList  ;}private static boolean tom_equal_term_HookList(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_HookList(Object t) {  return  t instanceof tom.gom.adt.objects.types.HookList  ;}private static boolean tom_is_fun_sym_AbstractTypeClass( tom.gom.adt.objects.types.GomClass  t) {  return  t instanceof tom.gom.adt.objects.types.gomclass.AbstractTypeClass  ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_AbstractTypeClass_ClassName( tom.gom.adt.objects.types.GomClass  t) {  return  t.getClassName()  ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_AbstractTypeClass_Mapping( tom.gom.adt.objects.types.GomClass  t) {  return  t.getMapping()  ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_AbstractTypeClass_Visitor( tom.gom.adt.objects.types.GomClass  t) {  return  t.getVisitor()  ;}private static  tom.gom.adt.objects.types.ClassNameList  tom_get_slot_AbstractTypeClass_SortList( tom.gom.adt.objects.types.GomClass  t) {  return  t.getSortList()  ;}private static  tom.gom.adt.objects.types.HookList  tom_get_slot_AbstractTypeClass_Hooks( tom.gom.adt.objects.types.GomClass  t) {  return  t.getHooks()  ;} 

  public AbstractTypeTemplate(File tomHomePath,
                              List importList,
                              GomClass gomClass,
                              TemplateClass mapping) {
    super(gomClass,tomHomePath,importList,mapping);
     if (tom_is_sort_GomClass(gomClass)) { { tom.gom.adt.objects.types.GomClass  tomMatch1Position1=(( tom.gom.adt.objects.types.GomClass )gomClass); if ( ( tom_is_fun_sym_AbstractTypeClass(tomMatch1Position1) ||  false  ) ) { { tom.gom.adt.objects.types.ClassName  tom_visitorName=tom_get_slot_AbstractTypeClass_Visitor(tomMatch1Position1); { tom.gom.adt.objects.types.ClassNameList  tom_sortList=tom_get_slot_AbstractTypeClass_SortList(tomMatch1Position1); if ( true ) {



        this.visitor = tom_visitorName;
        this.sortList = tom_sortList;
        return;
       } } } } } }

    throw new GomRuntimeException(
        "Bad argument for AbstractTypeTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    
    writer.write(
"\npackage "/* Generated by TOM (version 2.5alpha): Do not edit this file */+getPackage()+";\n"/* Generated by TOM (version 2.5alpha): Do not edit this file */+generateImport()+"\n\npublic abstract class "/* Generated by TOM (version 2.5alpha): Do not edit this file */+className()+" implements shared.SharedObjectWithID, jjtraveler.Visitable, tom.library.sl.Visitable, Comparable "/* Generated by TOM (version 2.5alpha): Do not edit this file */+generateInterface()+" {\n"




);

    if (! hooks.isEmptyconcHook()) {
      mapping.generate(writer); 
    }
    writer.write(
"\n"/* Generated by TOM (version 2.5alpha): Do not edit this file */+generateBlock()+"\n\n  private int uniqueID;\n\n  public abstract aterm.ATerm toATerm();\n\n  public abstract String symbolName();\n\n  public String toString() {\n    java.lang.StringBuffer buffer = new java.lang.StringBuffer();\n    toStringBuffer(buffer);\n    return buffer.toString();\n  }\n\n  public abstract void toStringBuffer(java.lang.StringBuffer buffer);\n\n  public abstract int compareTo(Object o);\n\n  public abstract int compareToLPO(Object o);\n\n  public int getUniqueIdentifier() {\n    return uniqueID;\n  }\n\n  public void setUniqueIdentifier(int uniqueID) {\n    this.uniqueID = uniqueID;\n  }\n\n  abstract public "/* Generated by TOM (version 2.5alpha): Do not edit this file */+className()+" accept("/* Generated by TOM (version 2.5alpha): Do not edit this file */+fullClassName(visitor)+" v) throws jjtraveler.VisitFailure;\n}\n"






























);
 }

}
