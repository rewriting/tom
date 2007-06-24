/* Generated by TOM (version 2.5rc2): Do not edit this file *//*
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

package tom.gom.backend.strategy;

import java.io.*;
import java.util.logging.*;
import tom.gom.backend.TemplateClass;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public class SOpTemplate extends TemplateClass {
  ClassName operator;
  SlotFieldList slotList;

  /* Generated by TOM (version 2.5rc2): Do not edit this file *//* Generated by TOM (version 2.5rc2): Do not edit this file *//* Generated by TOM (version 2.5rc2): Do not edit this file */ private static boolean tom_equal_term_String(String t1, String t2) { return  (t1.equals(t2)) ;}private static boolean tom_is_sort_String(String t) { return  t instanceof String ;}  /* Generated by TOM (version 2.5rc2): Do not edit this file */ private static boolean tom_equal_term_SlotField(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotField(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotField ;}private static boolean tom_equal_term_SlotFieldList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotFieldList(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotFieldList ;}private static boolean tom_equal_term_GomClass(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomClass(Object t) { return  t instanceof tom.gom.adt.objects.types.GomClass ;}private static boolean tom_equal_term_ClassName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ClassName(Object t) { return  t instanceof tom.gom.adt.objects.types.ClassName ;}private static boolean tom_equal_term_HookList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookList(Object t) { return  t instanceof tom.gom.adt.objects.types.HookList ;}private static boolean tom_is_fun_sym_SlotField( tom.gom.adt.objects.types.SlotField  t) { return  t instanceof tom.gom.adt.objects.types.slotfield.SlotField ;}private static  String  tom_get_slot_SlotField_Name( tom.gom.adt.objects.types.SlotField  t) { return  t.getName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_SlotField_Domain( tom.gom.adt.objects.types.SlotField  t) { return  t.getDomain() ;}private static boolean tom_is_fun_sym_OperatorClass( tom.gom.adt.objects.types.GomClass  t) { return  t instanceof tom.gom.adt.objects.types.gomclass.OperatorClass ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_ClassName( tom.gom.adt.objects.types.GomClass  t) { return  t.getClassName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_AbstractType( tom.gom.adt.objects.types.GomClass  t) { return  t.getAbstractType() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_ExtendsType( tom.gom.adt.objects.types.GomClass  t) { return  t.getExtendsType() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_Mapping( tom.gom.adt.objects.types.GomClass  t) { return  t.getMapping() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_SortName( tom.gom.adt.objects.types.GomClass  t) { return  t.getSortName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_Visitor( tom.gom.adt.objects.types.GomClass  t) { return  t.getVisitor() ;}private static  tom.gom.adt.objects.types.SlotFieldList  tom_get_slot_OperatorClass_Slots( tom.gom.adt.objects.types.GomClass  t) { return  t.getSlots() ;}private static  tom.gom.adt.objects.types.HookList  tom_get_slot_OperatorClass_Hooks( tom.gom.adt.objects.types.GomClass  t) { return  t.getHooks() ;}private static boolean tom_is_fun_sym_ClassName( tom.gom.adt.objects.types.ClassName  t) { return  t instanceof tom.gom.adt.objects.types.classname.ClassName ;}private static  tom.gom.adt.objects.types.ClassName  tom_make_ClassName( String  t0,  String  t1) { return  tom.gom.adt.objects.types.classname.ClassName.make(t0, t1) ; }private static  String  tom_get_slot_ClassName_Pkg( tom.gom.adt.objects.types.ClassName  t) { return  t.getPkg() ;}private static  String  tom_get_slot_ClassName_Name( tom.gom.adt.objects.types.ClassName  t) { return  t.getName() ;}private static boolean tom_is_fun_sym_concSlotField( tom.gom.adt.objects.types.SlotFieldList  t) { return  t instanceof tom.gom.adt.objects.types.slotfieldlist.ConsconcSlotField || t instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyconcSlotField ;}private static  tom.gom.adt.objects.types.SlotFieldList  tom_empty_list_concSlotField() { return  tom.gom.adt.objects.types.slotfieldlist.EmptyconcSlotField.make() ; }private static  tom.gom.adt.objects.types.SlotFieldList  tom_cons_list_concSlotField( tom.gom.adt.objects.types.SlotField  e,  tom.gom.adt.objects.types.SlotFieldList  l) { return  tom.gom.adt.objects.types.slotfieldlist.ConsconcSlotField.make(e,l) ; }private static  tom.gom.adt.objects.types.SlotField  tom_get_head_concSlotField_SlotFieldList( tom.gom.adt.objects.types.SlotFieldList  l) { return  l.getHeadconcSlotField() ;}private static  tom.gom.adt.objects.types.SlotFieldList  tom_get_tail_concSlotField_SlotFieldList( tom.gom.adt.objects.types.SlotFieldList  l) { return  l.getTailconcSlotField() ;}private static boolean tom_is_empty_concSlotField_SlotFieldList( tom.gom.adt.objects.types.SlotFieldList  l) { return  l.isEmptyconcSlotField() ;}   private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_concSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {     if(tom_is_empty_concSlotField_SlotFieldList(l1)) {       return l2;     } else if(tom_is_empty_concSlotField_SlotFieldList(l2)) {       return l1;     } else if(tom_is_empty_concSlotField_SlotFieldList(tom_get_tail_concSlotField_SlotFieldList(l1))) {       return ( tom.gom.adt.objects.types.SlotFieldList )tom_cons_list_concSlotField(tom_get_head_concSlotField_SlotFieldList(l1),l2);     } else {       return ( tom.gom.adt.objects.types.SlotFieldList )tom_cons_list_concSlotField(tom_get_head_concSlotField_SlotFieldList(l1),tom_append_list_concSlotField(tom_get_tail_concSlotField_SlotFieldList(l1),l2));     }   }   private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_concSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {     if(tom_equal_term_SlotFieldList(begin,end)) {       return tail;     } else {       return ( tom.gom.adt.objects.types.SlotFieldList )tom_cons_list_concSlotField(tom_get_head_concSlotField_SlotFieldList(begin),( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_concSlotField(tom_get_tail_concSlotField_SlotFieldList(begin),end,tail));     }   }    

  /*
   * The argument is an operator class, and this template generates the
   * assotiated _Op strategy
   */
  public SOpTemplate(GomClass gomClass) {
    super(gomClass);
    ClassName clsName = this.className;
    if (tom_is_sort_ClassName(clsName)) {{  tom.gom.adt.objects.types.ClassName  tomMatch393NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.ClassName )clsName);if (tom_is_fun_sym_ClassName(tomMatch393NameNumberfreshSubject_1)) {{  String  tomMatch393NameNumber_freshVar_0=tom_get_slot_ClassName_Pkg(tomMatch393NameNumberfreshSubject_1);{  String  tomMatch393NameNumber_freshVar_1=tom_get_slot_ClassName_Name(tomMatch393NameNumberfreshSubject_1);if ( true ) {

        String newpkg = tomMatch393NameNumber_freshVar_0.replaceFirst(".types.",".strategy.");
        String newname = "_"+tomMatch393NameNumber_freshVar_1;
        this.className = tom_make_ClassName(newpkg,newname);
      }}}}}}if (tom_is_sort_GomClass(gomClass)) {{  tom.gom.adt.objects.types.GomClass  tomMatch394NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClass )gomClass);if (tom_is_fun_sym_OperatorClass(tomMatch394NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.ClassName  tomMatch394NameNumber_freshVar_0=tom_get_slot_OperatorClass_ClassName(tomMatch394NameNumberfreshSubject_1);{  tom.gom.adt.objects.types.SlotFieldList  tomMatch394NameNumber_freshVar_1=tom_get_slot_OperatorClass_Slots(tomMatch394NameNumberfreshSubject_1);if ( true ) {



        this.operator = tomMatch394NameNumber_freshVar_0;
        this.slotList = tomMatch394NameNumber_freshVar_1;
        return;
      }}}}}}

    throw new GomRuntimeException(
        "Wrong argument for SOpTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write("\npackage "/* Generated by TOM (version 2.5rc2): Do not edit this file */+getPackage()+";\n\npublic class "/* Generated by TOM (version 2.5rc2): Do not edit this file */+className()+" implements tom.library.sl.Strategy {\n  private static final String msg = \"Not an "/* Generated by TOM (version 2.5rc2): Do not edit this file */+className(operator)+"\";\n  /* Manage an internal environment */\n  protected tom.library.sl.Environment environment;\n  \n  public void setEnvironment(tom.library.sl.Environment env) {\n    this.environment = env;\n  }\n\n  public tom.library.sl.Environment getEnvironment() {\n    if(environment!=null) {\n      return environment;\n    } else {\n      throw new RuntimeException(\"environment not initialized\");\n    }\n  }\n\n  private tom.library.sl.Strategy[] args;\n\n  public tom.library.sl.Strategy getArgument(int i) {\n    return args[i];\n  }\n  public void setArgument(int i, tom.library.sl.Strategy child) {\n    args[i]= child;\n  }\n  public int getChildCount() {\n    return args.length;\n  }\n  public tom.library.sl.Visitable getChildAt(int i) {\n      return args[i];\n  }\n  public tom.library.sl.Visitable setChildAt(int i, tom.library.sl.Visitable child) {\n    args[i]= (tom.library.sl.Strategy) child;\n    return this;\n  }\n\n  public tom.library.sl.Visitable[] getChildren() {\n    return (tom.library.sl.Visitable[]) args.clone();\n  }\n\n  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {\n    tom.library.sl.Strategy[] newArgs = new tom.library.sl.Strategy[children.length];\n    for(int i = 0; i < children.length; i++) {\n      newArgs[i] = (tom.library.sl.Strategy) children[i];\n    }\n    args = newArgs;\n    return this;\n  }\n\n  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {\n    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());\n    environment.setRoot(any);\n    int status = visit();\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return environment.getRoot();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws tom.library.sl.VisitFailure {\n    return v.visit_Strategy(this);\n  }\n\n\n  private static boolean[] nonbuiltin = new boolean[]{"/* Generated by TOM (version 2.5rc2): Do not edit this file */+genNonBuiltin()+"};\n  public "/* Generated by TOM (version 2.5rc2): Do not edit this file */+className()+"("/* Generated by TOM (version 2.5rc2): Do not edit this file */+genConstrArgs(slotList.length(),"tom.library.sl.Strategy arg")+") {\n    args = new tom.library.sl.Strategy[] {"/* Generated by TOM (version 2.5rc2): Do not edit this file */+genConstrArgs(slotList.length(),"arg")+"};\n  }\n\n  public tom.library.sl.Visitable visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {\n    setEnvironment(envt);\n    int status = visit();\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return environment.getRoot();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {\n    if(any instanceof "/* Generated by TOM (version 2.5rc2): Do not edit this file */+fullClassName(operator)+") {\n      tom.library.sl.Visitable result = any;\n      tom.library.sl.Visitable[] childs = null;\n      for (int i = 0, nbi = 0; i < "/* Generated by TOM (version 2.5rc2): Do not edit this file */+slotList.length()+"; i++) {\n        if(nonbuiltin[i]) {\n          tom.library.sl.Visitable oldChild = any.getChildAt(nbi);\n          tom.library.sl.Visitable newChild = args[i].visitLight(oldChild);\n          if(childs != null) {\n            childs[nbi] = newChild;\n          } else if(newChild != oldChild) {\n            // allocate the array, and fill it\n            childs = any.getChildren();\n            childs[nbi] = newChild;\n          }\n          nbi++;\n        }\n      }\n      if(childs!=null) {\n        result = any.setChildren(childs);\n      }\n      return result;\n    } else {\n      throw new tom.library.sl.VisitFailure(msg);\n    }\n  }\n\n  public int visit() {\n    tom.library.sl.Visitable any = environment.getSubject();\n    if(any instanceof "/* Generated by TOM (version 2.5rc2): Do not edit this file */+fullClassName(operator)+") {\n      tom.library.sl.Visitable[] childs = null;\n      for(int i = 0, nbi = 0; i < "/* Generated by TOM (version 2.5rc2): Do not edit this file */+slotList.length()+"; i++) {\n        if(nonbuiltin[i]) {\n          tom.library.sl.Visitable oldChild = (tom.library.sl.Visitable)any.getChildAt(nbi);\n          environment.down(nbi+1);\n          int status = args[i].visit();\n          if(status != tom.library.sl.Environment.SUCCESS) {\n            environment.upLocal();\n            return status;\n          }\n          tom.library.sl.Visitable newChild = environment.getSubject();\n          if(childs != null) {\n            childs[nbi] = newChild;\n          } else if(newChild != oldChild) {\n            childs = ((tom.library.sl.Visitable) any).getChildren();\n            childs[nbi] = newChild;\n          } \n          environment.upLocal();\n          nbi++;\n        }\n      }\n      if(childs!=null) {\n        environment.setSubject((tom.library.sl.Visitable)any.setChildren(childs));\n      }\n      return tom.library.sl.Environment.SUCCESS;\n    } else {\n      return tom.library.sl.Environment.FAILURE;\n    }\n  }\n}\n"














































































































































);
}

private String genConstrArgs(int count, String arg) {
  StringBuffer args = new StringBuffer();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append(arg);
    args.append(i);
  }
  return args.toString();
}

private String genIdArgs(int count) {
  StringBuffer args = new StringBuffer();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append("Identity()");
  }
  return args.toString();
}

public String generateMapping() {

  return "\n    %op Strategy "/* Generated by TOM (version 2.5rc2): Do not edit this file */+className()+"("/* Generated by TOM (version 2.5rc2): Do not edit this file */+genStratArgs(slotList.length(),"arg")+") {\n      is_fsym(t) { (t!=null) && t instanceof ("/* Generated by TOM (version 2.5rc2): Do not edit this file */+fullClassName()+")}\n      "/* Generated by TOM (version 2.5rc2): Do not edit this file */+genGetSlot(slotList.length(),"arg")+"\n        make("/* Generated by TOM (version 2.5rc2): Do not edit this file */+genConstrArgs(slotList.length(),"arg")+") { new "/* Generated by TOM (version 2.5rc2): Do not edit this file */+fullClassName()+"("/* Generated by TOM (version 2.5rc2): Do not edit this file */+genConstrArgs(slotList.length(),"arg")+") }\n    }\n  \n  "






;
}

private String genGetSlot(int count, String arg) {
  StringBuffer out = new StringBuffer();
  for (int i = 0; i < count; ++i) {
    out.append("\n        get_slot("/* Generated by TOM (version 2.5rc2): Do not edit this file */+arg+i+", t) { t.getArgument("/* Generated by TOM (version 2.5rc2): Do not edit this file */+i+") }"
);
  }
  return out.toString();
}

private String genStratArgs(int count, String arg) {
  StringBuffer args = new StringBuffer();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append(arg);
    args.append(i);
    args.append(":Strategy");
  }
  return args.toString();
}

private String genNonBuiltin() {
  String out = "";
  if (tom_is_sort_SlotFieldList(slotList)) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch395NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.SlotFieldList )slotList);if (tom_is_fun_sym_concSlotField(tomMatch395NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch395NameNumber_freshVar_0=tomMatch395NameNumberfreshSubject_1;{  tom.gom.adt.objects.types.SlotFieldList  tomMatch395NameNumber_begin_2=tomMatch395NameNumber_freshVar_0;{  tom.gom.adt.objects.types.SlotFieldList  tomMatch395NameNumber_end_3=tomMatch395NameNumber_freshVar_0;do {{{  tom.gom.adt.objects.types.SlotFieldList  tomMatch395NameNumber_freshVar_1=tomMatch395NameNumber_end_3;if (!(tom_is_empty_concSlotField_SlotFieldList(tomMatch395NameNumber_freshVar_1))) {if (tom_is_fun_sym_SlotField(tom_get_head_concSlotField_SlotFieldList(tomMatch395NameNumber_freshVar_1))) {{  tom.gom.adt.objects.types.ClassName  tomMatch395NameNumber_freshVar_6=tom_get_slot_SlotField_Domain(tom_get_head_concSlotField_SlotFieldList(tomMatch395NameNumber_freshVar_1));{  tom.gom.adt.objects.types.SlotFieldList  tomMatch395NameNumber_freshVar_4=tom_get_tail_concSlotField_SlotFieldList(tomMatch395NameNumber_freshVar_1);if ( true ) {

      if (!GomEnvironment.getInstance().isBuiltinClass(tomMatch395NameNumber_freshVar_6)) {
        out += "true, ";
      } else {
        out += "false, ";
      }
    }}}}}}if (tom_is_empty_concSlotField_SlotFieldList(tomMatch395NameNumber_end_3)) {tomMatch395NameNumber_end_3=tomMatch395NameNumber_begin_2;} else {tomMatch395NameNumber_end_3=tom_get_tail_concSlotField_SlotFieldList(tomMatch395NameNumber_end_3);}}} while(!(tom_equal_term_SlotFieldList(tomMatch395NameNumber_end_3, tomMatch395NameNumber_begin_2)));}}}}}}

  if (out.length()!=0) {
    return out.substring(0,out.length()-2);
  } else {
    return out;
  }
}

private int nonBuiltinChildCount() {
  int count = 0;
  if (tom_is_sort_SlotFieldList(slotList)) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch396NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.SlotFieldList )slotList);if (tom_is_fun_sym_concSlotField(tomMatch396NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch396NameNumber_freshVar_0=tomMatch396NameNumberfreshSubject_1;{  tom.gom.adt.objects.types.SlotFieldList  tomMatch396NameNumber_begin_2=tomMatch396NameNumber_freshVar_0;{  tom.gom.adt.objects.types.SlotFieldList  tomMatch396NameNumber_end_3=tomMatch396NameNumber_freshVar_0;do {{{  tom.gom.adt.objects.types.SlotFieldList  tomMatch396NameNumber_freshVar_1=tomMatch396NameNumber_end_3;if (!(tom_is_empty_concSlotField_SlotFieldList(tomMatch396NameNumber_freshVar_1))) {if (tom_is_fun_sym_SlotField(tom_get_head_concSlotField_SlotFieldList(tomMatch396NameNumber_freshVar_1))) {{  tom.gom.adt.objects.types.ClassName  tomMatch396NameNumber_freshVar_6=tom_get_slot_SlotField_Domain(tom_get_head_concSlotField_SlotFieldList(tomMatch396NameNumber_freshVar_1));{  tom.gom.adt.objects.types.SlotFieldList  tomMatch396NameNumber_freshVar_4=tom_get_tail_concSlotField_SlotFieldList(tomMatch396NameNumber_freshVar_1);if ( true ) {

      if (!GomEnvironment.getInstance().isBuiltinClass(tomMatch396NameNumber_freshVar_6)) {
        count++;
      }
    }}}}}}if (tom_is_empty_concSlotField_SlotFieldList(tomMatch396NameNumber_end_3)) {tomMatch396NameNumber_end_3=tomMatch396NameNumber_begin_2;} else {tomMatch396NameNumber_end_3=tom_get_tail_concSlotField_SlotFieldList(tomMatch396NameNumber_end_3);}}} while(!(tom_equal_term_SlotFieldList(tomMatch396NameNumber_end_3, tomMatch396NameNumber_begin_2)));}}}}}}

  return count;
}

/** the class logger instance*/
private Logger getLogger() {
  return Logger.getLogger(getClass().getName());
}
}
