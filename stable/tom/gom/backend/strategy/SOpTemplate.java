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

  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */private static boolean tom_equal_term_char(char t1, char t2) { return  (t1==t2) ;}private static boolean tom_is_sort_char(char t) { return  true ;} private static boolean tom_equal_term_String(String t1, String t2) { return  (t1.equals(t2)) ;}private static boolean tom_is_sort_String(String t) { return  t instanceof String ;}  /* Generated by TOM (version 2.6alpha): Do not edit this file */private static boolean tom_equal_term_int(int t1, int t2) { return  (t1==t2) ;}private static boolean tom_is_sort_int(int t) { return  true ;} private static boolean tom_equal_term_Code(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Code(Object t) { return  t instanceof tom.gom.adt.code.types.Code ;}private static boolean tom_equal_term_Hook(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Hook(Object t) { return  t instanceof tom.gom.adt.objects.types.Hook ;}private static boolean tom_equal_term_SlotField(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotField(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotField ;}private static boolean tom_equal_term_SlotFieldList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotFieldList(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotFieldList ;}private static boolean tom_equal_term_GomClass(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomClass(Object t) { return  t instanceof tom.gom.adt.objects.types.GomClass ;}private static boolean tom_equal_term_ClassName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ClassName(Object t) { return  t instanceof tom.gom.adt.objects.types.ClassName ;}private static boolean tom_equal_term_GomClassList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomClassList(Object t) { return  t instanceof tom.gom.adt.objects.types.GomClassList ;}private static boolean tom_equal_term_ClassNameList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ClassNameList(Object t) { return  t instanceof tom.gom.adt.objects.types.ClassNameList ;}private static boolean tom_equal_term_HookList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookList(Object t) { return  t instanceof tom.gom.adt.objects.types.HookList ;}private static boolean tom_equal_term_Slot(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Slot(Object t) { return  t instanceof tom.gom.adt.gom.types.Slot ;}private static boolean tom_equal_term_ArgList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ArgList(Object t) { return  t instanceof tom.gom.adt.gom.types.ArgList ;}private static boolean tom_equal_term_IdKind(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_IdKind(Object t) { return  t instanceof tom.gom.adt.gom.types.IdKind ;}private static boolean tom_equal_term_GomModuleList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModuleList(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModuleList ;}private static boolean tom_equal_term_GrammarList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GrammarList(Object t) { return  t instanceof tom.gom.adt.gom.types.GrammarList ;}private static boolean tom_equal_term_TypedProduction(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TypedProduction(Object t) { return  t instanceof tom.gom.adt.gom.types.TypedProduction ;}private static boolean tom_equal_term_SectionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SectionList(Object t) { return  t instanceof tom.gom.adt.gom.types.SectionList ;}private static boolean tom_equal_term_OperatorDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OperatorDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.OperatorDeclList ;}private static boolean tom_equal_term_HookDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.HookDeclList ;}private static boolean tom_equal_term_SortDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.SortDeclList ;}private static boolean tom_equal_term_OperatorDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OperatorDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.OperatorDecl ;}private static boolean tom_equal_term_GomModule(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModule(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModule ;}private static boolean tom_equal_term_Pair(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Pair(Object t) { return  t instanceof tom.gom.adt.gom.types.Pair ;}private static boolean tom_equal_term_Section(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Section(Object t) { return  t instanceof tom.gom.adt.gom.types.Section ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) { return  t instanceof tom.gom.adt.gom.types.GomTypeList ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ProductionList(Object t) { return  t instanceof tom.gom.adt.gom.types.ProductionList ;}private static boolean tom_equal_term_Decl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Decl(Object t) { return  t instanceof tom.gom.adt.gom.types.Decl ;}private static boolean tom_equal_term_ImportList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ImportList(Object t) { return  t instanceof tom.gom.adt.gom.types.ImportList ;}private static boolean tom_equal_term_Sort(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Sort(Object t) { return  t instanceof tom.gom.adt.gom.types.Sort ;}private static boolean tom_equal_term_SortDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.SortDecl ;}private static boolean tom_equal_term_HookDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.HookDecl ;}private static boolean tom_equal_term_ModuleDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleDeclList ;}private static boolean tom_equal_term_Module(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Module(Object t) { return  t instanceof tom.gom.adt.gom.types.Module ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_FieldList(Object t) { return  t instanceof tom.gom.adt.gom.types.FieldList ;}private static boolean tom_equal_term_Grammar(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Grammar(Object t) { return  t instanceof tom.gom.adt.gom.types.Grammar ;}private static boolean tom_equal_term_SlotList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotList(Object t) { return  t instanceof tom.gom.adt.gom.types.SlotList ;}private static boolean tom_equal_term_SortList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortList(Object t) { return  t instanceof tom.gom.adt.gom.types.SortList ;}private static boolean tom_equal_term_Arg(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Arg(Object t) { return  t instanceof tom.gom.adt.gom.types.Arg ;}private static boolean tom_equal_term_GomModuleName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModuleName(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModuleName ;}private static boolean tom_equal_term_Field(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Field(Object t) { return  t instanceof tom.gom.adt.gom.types.Field ;}private static boolean tom_equal_term_Production(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Production(Object t) { return  t instanceof tom.gom.adt.gom.types.Production ;}private static boolean tom_equal_term_ModuleDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleDecl ;}private static boolean tom_equal_term_ModuleList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleList(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleList ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomType(Object t) { return  t instanceof tom.gom.adt.gom.types.GomType ;}private static boolean tom_equal_term_ImportedModule(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ImportedModule(Object t) { return  t instanceof tom.gom.adt.gom.types.ImportedModule ;}private static boolean tom_equal_term_Option(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Option(Object t) { return  t instanceof tom.gom.adt.gom.types.Option ;}private static boolean tom_equal_term_HookKind(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookKind(Object t) { return  t instanceof tom.gom.adt.gom.types.HookKind ;}private static boolean tom_is_fun_sym_SlotField( tom.gom.adt.objects.types.SlotField  t) { return  (t instanceof tom.gom.adt.objects.types.slotfield.SlotField) ;}private static  String  tom_get_slot_SlotField_Name( tom.gom.adt.objects.types.SlotField  t) { return  t.getName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_SlotField_Domain( tom.gom.adt.objects.types.SlotField  t) { return  t.getDomain() ;}private static boolean tom_is_fun_sym_OperatorClass( tom.gom.adt.objects.types.GomClass  t) { return  (t instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_ClassName( tom.gom.adt.objects.types.GomClass  t) { return  t.getClassName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_AbstractType( tom.gom.adt.objects.types.GomClass  t) { return  t.getAbstractType() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_ExtendsType( tom.gom.adt.objects.types.GomClass  t) { return  t.getExtendsType() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_Mapping( tom.gom.adt.objects.types.GomClass  t) { return  t.getMapping() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_SortName( tom.gom.adt.objects.types.GomClass  t) { return  t.getSortName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_Visitor( tom.gom.adt.objects.types.GomClass  t) { return  t.getVisitor() ;}private static  tom.gom.adt.objects.types.SlotFieldList  tom_get_slot_OperatorClass_Slots( tom.gom.adt.objects.types.GomClass  t) { return  t.getSlots() ;}private static  tom.gom.adt.objects.types.HookList  tom_get_slot_OperatorClass_Hooks( tom.gom.adt.objects.types.GomClass  t) { return  t.getHooks() ;}private static boolean tom_is_fun_sym_ClassName( tom.gom.adt.objects.types.ClassName  t) { return  (t instanceof tom.gom.adt.objects.types.classname.ClassName) ;}private static  tom.gom.adt.objects.types.ClassName  tom_make_ClassName( String  t0,  String  t1) { return  tom.gom.adt.objects.types.classname.ClassName.make(t0, t1) ; }private static  String  tom_get_slot_ClassName_Pkg( tom.gom.adt.objects.types.ClassName  t) { return  t.getPkg() ;}private static  String  tom_get_slot_ClassName_Name( tom.gom.adt.objects.types.ClassName  t) { return  t.getName() ;}private static boolean tom_is_fun_sym_concSlotField( tom.gom.adt.objects.types.SlotFieldList  t) { return  ((t instanceof tom.gom.adt.objects.types.slotfieldlist.ConsconcSlotField) || (t instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyconcSlotField)) ;}private static  tom.gom.adt.objects.types.SlotFieldList  tom_empty_list_concSlotField() { return  tom.gom.adt.objects.types.slotfieldlist.EmptyconcSlotField.make() ; }private static  tom.gom.adt.objects.types.SlotFieldList  tom_cons_list_concSlotField( tom.gom.adt.objects.types.SlotField  e,  tom.gom.adt.objects.types.SlotFieldList  l) { return  tom.gom.adt.objects.types.slotfieldlist.ConsconcSlotField.make(e,l) ; }private static  tom.gom.adt.objects.types.SlotField  tom_get_head_concSlotField_SlotFieldList( tom.gom.adt.objects.types.SlotFieldList  l) { return  l.getHeadconcSlotField() ;}private static  tom.gom.adt.objects.types.SlotFieldList  tom_get_tail_concSlotField_SlotFieldList( tom.gom.adt.objects.types.SlotFieldList  l) { return  l.getTailconcSlotField() ;}private static boolean tom_is_empty_concSlotField_SlotFieldList( tom.gom.adt.objects.types.SlotFieldList  l) { return  l.isEmptyconcSlotField() ;}   private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_concSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {     if( l1.isEmptyconcSlotField() ) {       return l2;     } else if( l2.isEmptyconcSlotField() ) {       return l1;     } else if(  l1.getTailconcSlotField() .isEmptyconcSlotField() ) {       return  tom.gom.adt.objects.types.slotfieldlist.ConsconcSlotField.make( l1.getHeadconcSlotField() ,l2) ;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsconcSlotField.make( l1.getHeadconcSlotField() ,tom_append_list_concSlotField( l1.getTailconcSlotField() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_concSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsconcSlotField.make( begin.getHeadconcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_concSlotField( begin.getTailconcSlotField() ,end,tail)) ;     }   }    

  /*
   * The argument is an operator class, and this template generates the
   * assotiated _Op strategy
   */
  public SOpTemplate(GomClass gomClass) {
    super(gomClass);
    ClassName clsName = this.className;
    if (tom_is_sort_ClassName(clsName)) {{  tom.gom.adt.objects.types.ClassName  tomMatch427NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.ClassName )clsName);if (tom_is_fun_sym_ClassName(tomMatch427NameNumberfreshSubject_1)) {{  String  tomMatch427NameNumber_freshVar_0=tom_get_slot_ClassName_Pkg(tomMatch427NameNumberfreshSubject_1);{  String  tomMatch427NameNumber_freshVar_1=tom_get_slot_ClassName_Name(tomMatch427NameNumberfreshSubject_1);if ( true ) {

        String newpkg = tomMatch427NameNumber_freshVar_0.replaceFirst(".types.",".strategy.");
        String newname = "_"+tomMatch427NameNumber_freshVar_1;
        this.className = tom_make_ClassName(newpkg,newname);
      }}}}}}if (tom_is_sort_GomClass(gomClass)) {{  tom.gom.adt.objects.types.GomClass  tomMatch428NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClass )gomClass);if (tom_is_fun_sym_OperatorClass(tomMatch428NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.ClassName  tomMatch428NameNumber_freshVar_0=tom_get_slot_OperatorClass_ClassName(tomMatch428NameNumberfreshSubject_1);{  tom.gom.adt.objects.types.SlotFieldList  tomMatch428NameNumber_freshVar_1=tom_get_slot_OperatorClass_Slots(tomMatch428NameNumberfreshSubject_1);if ( true ) {



        this.operator = tomMatch428NameNumber_freshVar_0;
        this.slotList = tomMatch428NameNumber_freshVar_1;
        return;
      }}}}}}

    throw new GomRuntimeException(
        "Wrong argument for SOpTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write("\npackage "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getPackage()+";\n\npublic class "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+" implements tom.library.sl.Strategy {\n  private static final String msg = \"Not an "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(operator)+"\";\n  /* Manage an internal environment */\n  protected tom.library.sl.Environment environment;\n  \n  public void setEnvironment(tom.library.sl.Environment env) {\n    this.environment = env;\n  }\n\n  public tom.library.sl.Environment getEnvironment() {\n    if(environment!=null) {\n      return environment;\n    } else {\n      throw new RuntimeException(\"environment not initialized\");\n    }\n  }\n\n  private tom.library.sl.Strategy[] args;\n\n  public tom.library.sl.Strategy getArgument(int i) {\n    return args[i];\n  }\n  public void setArgument(int i, tom.library.sl.Strategy child) {\n    args[i]= child;\n  }\n  public int getChildCount() {\n    return args.length;\n  }\n  public tom.library.sl.Visitable getChildAt(int i) {\n      return args[i];\n  }\n  public tom.library.sl.Visitable setChildAt(int i, tom.library.sl.Visitable child) {\n    args[i]= (tom.library.sl.Strategy) child;\n    return this;\n  }\n\n  public tom.library.sl.Visitable[] getChildren() {\n    return args.clone();\n  }\n\n  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {\n    tom.library.sl.Strategy[] newArgs = new tom.library.sl.Strategy[children.length];\n    for(int i = 0; i < children.length; i++) {\n      newArgs[i] = (tom.library.sl.Strategy) children[i];\n    }\n    args = newArgs;\n    return this;\n  }\n\n  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {\n    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());\n    environment.setRoot(any);\n    int status = visit();\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return environment.getRoot();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws tom.library.sl.VisitFailure {\n    return v.visit_Strategy(this);\n  }\n\n\n  private static boolean[] nonbuiltin = new boolean[]{"/* Generated by TOM (version 2.6alpha): Do not edit this file */+genNonBuiltin()+"};\n  public "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+genConstrArgs(slotList.length(),"tom.library.sl.Strategy arg")+") {\n    args = new tom.library.sl.Strategy[] {"/* Generated by TOM (version 2.6alpha): Do not edit this file */+genConstrArgs(slotList.length(),"arg")+"};\n  }\n\n  public tom.library.sl.Visitable visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {\n    setEnvironment(envt);\n    int status = visit();\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return environment.getRoot();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {\n    if(any instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(operator)+") {\n      tom.library.sl.Visitable result = any;\n      tom.library.sl.Visitable[] childs = null;\n      for (int i = 0, nbi = 0; i < "/* Generated by TOM (version 2.6alpha): Do not edit this file */+slotList.length()+"; i++) {\n        if(nonbuiltin[i]) {\n          tom.library.sl.Visitable oldChild = any.getChildAt(nbi);\n          tom.library.sl.Visitable newChild = args[i].visitLight(oldChild);\n          if(childs != null) {\n            childs[nbi] = newChild;\n          } else if(newChild != oldChild) {\n            // allocate the array, and fill it\n            childs = any.getChildren();\n            childs[nbi] = newChild;\n          }\n          nbi++;\n        }\n      }\n      if(childs!=null) {\n        result = any.setChildren(childs);\n      }\n      return result;\n    } else {\n      throw new tom.library.sl.VisitFailure(msg);\n    }\n  }\n\n  public int visit() {\n    tom.library.sl.Visitable any = environment.getSubject();\n    if(any instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(operator)+") {\n      tom.library.sl.Visitable[] childs = null;\n      for(int i = 0, nbi = 0; i < "/* Generated by TOM (version 2.6alpha): Do not edit this file */+slotList.length()+"; i++) {\n        if(nonbuiltin[i]) {\n          tom.library.sl.Visitable oldChild = any.getChildAt(nbi);\n          environment.down(nbi+1);\n          int status = args[i].visit();\n          if(status != tom.library.sl.Environment.SUCCESS) {\n            environment.upLocal();\n            return status;\n          }\n          tom.library.sl.Visitable newChild = environment.getSubject();\n          if(childs != null) {\n            childs[nbi] = newChild;\n          } else if(newChild != oldChild) {\n            childs = any.getChildren();\n            childs[nbi] = newChild;\n          } \n          environment.upLocal();\n          nbi++;\n        }\n      }\n      if(childs!=null) {\n        environment.setSubject(any.setChildren(childs));\n      }\n      return tom.library.sl.Environment.SUCCESS;\n    } else {\n      return tom.library.sl.Environment.FAILURE;\n    }\n  }\n}\n"














































































































































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

  return "\n    %op Strategy "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+genStratArgs(slotList.length(),"arg")+") {\n      is_fsym(t) { (($t!=null) && ($t instanceof ("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName()+")))}\n      "/* Generated by TOM (version 2.6alpha): Do not edit this file */+genGetSlot(slotList.length(),"arg")+"\n        make("/* Generated by TOM (version 2.6alpha): Do not edit this file */+genConstrArgs(slotList.length(),"arg")+") { new "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName()+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+genConstrArgs(slotList.length(),"arg")+") }\n    }\n  \n  "






;
}

private String genGetSlot(int count, String arg) {
  StringBuffer out = new StringBuffer();
  for (int i = 0; i < count; ++i) {
    out.append("\n        get_slot("/* Generated by TOM (version 2.6alpha): Do not edit this file */+arg+i+", t) { $t.getArgument("/* Generated by TOM (version 2.6alpha): Do not edit this file */+i+") }"
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
  if (tom_is_sort_SlotFieldList(slotList)) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch429NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.SlotFieldList )slotList);if (tom_is_fun_sym_concSlotField(tomMatch429NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch429NameNumber_freshVar_0=tomMatch429NameNumberfreshSubject_1;{  tom.gom.adt.objects.types.SlotFieldList  tomMatch429NameNumber_begin_2=tomMatch429NameNumber_freshVar_0;{  tom.gom.adt.objects.types.SlotFieldList  tomMatch429NameNumber_end_3=tomMatch429NameNumber_freshVar_0;do {{{  tom.gom.adt.objects.types.SlotFieldList  tomMatch429NameNumber_freshVar_1=tomMatch429NameNumber_end_3;if (!(tom_is_empty_concSlotField_SlotFieldList(tomMatch429NameNumber_freshVar_1))) {if (tom_is_fun_sym_SlotField(tom_get_head_concSlotField_SlotFieldList(tomMatch429NameNumber_freshVar_1))) {{  tom.gom.adt.objects.types.ClassName  tomMatch429NameNumber_freshVar_6=tom_get_slot_SlotField_Domain(tom_get_head_concSlotField_SlotFieldList(tomMatch429NameNumber_freshVar_1));{  tom.gom.adt.objects.types.SlotFieldList  tomMatch429NameNumber_freshVar_4=tom_get_tail_concSlotField_SlotFieldList(tomMatch429NameNumber_freshVar_1);if ( true ) {

      if (!GomEnvironment.getInstance().isBuiltinClass(tomMatch429NameNumber_freshVar_6)) {
        out += "true, ";
      } else {
        out += "false, ";
      }
    }}}}}}if (tom_is_empty_concSlotField_SlotFieldList(tomMatch429NameNumber_end_3)) {tomMatch429NameNumber_end_3=tomMatch429NameNumber_begin_2;} else {tomMatch429NameNumber_end_3=tom_get_tail_concSlotField_SlotFieldList(tomMatch429NameNumber_end_3);}}} while(!(tom_equal_term_SlotFieldList(tomMatch429NameNumber_end_3, tomMatch429NameNumber_begin_2)));}}}}}}

  if (out.length()!=0) {
    return out.substring(0,out.length()-2);
  } else {
    return out;
  }
}

private int nonBuiltinChildCount() {
  int count = 0;
  if (tom_is_sort_SlotFieldList(slotList)) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch430NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.SlotFieldList )slotList);if (tom_is_fun_sym_concSlotField(tomMatch430NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch430NameNumber_freshVar_0=tomMatch430NameNumberfreshSubject_1;{  tom.gom.adt.objects.types.SlotFieldList  tomMatch430NameNumber_begin_2=tomMatch430NameNumber_freshVar_0;{  tom.gom.adt.objects.types.SlotFieldList  tomMatch430NameNumber_end_3=tomMatch430NameNumber_freshVar_0;do {{{  tom.gom.adt.objects.types.SlotFieldList  tomMatch430NameNumber_freshVar_1=tomMatch430NameNumber_end_3;if (!(tom_is_empty_concSlotField_SlotFieldList(tomMatch430NameNumber_freshVar_1))) {if (tom_is_fun_sym_SlotField(tom_get_head_concSlotField_SlotFieldList(tomMatch430NameNumber_freshVar_1))) {{  tom.gom.adt.objects.types.ClassName  tomMatch430NameNumber_freshVar_6=tom_get_slot_SlotField_Domain(tom_get_head_concSlotField_SlotFieldList(tomMatch430NameNumber_freshVar_1));{  tom.gom.adt.objects.types.SlotFieldList  tomMatch430NameNumber_freshVar_4=tom_get_tail_concSlotField_SlotFieldList(tomMatch430NameNumber_freshVar_1);if ( true ) {

      if (!GomEnvironment.getInstance().isBuiltinClass(tomMatch430NameNumber_freshVar_6)) {
        count++;
      }
    }}}}}}if (tom_is_empty_concSlotField_SlotFieldList(tomMatch430NameNumber_end_3)) {tomMatch430NameNumber_end_3=tomMatch430NameNumber_begin_2;} else {tomMatch430NameNumber_end_3=tom_get_tail_concSlotField_SlotFieldList(tomMatch430NameNumber_end_3);}}} while(!(tom_equal_term_SlotFieldList(tomMatch430NameNumber_end_3, tomMatch430NameNumber_begin_2)));}}}}}}

  return count;
}

/** the class logger instance*/
private Logger getLogger() {
  return Logger.getLogger(getClass().getName());
}
}
