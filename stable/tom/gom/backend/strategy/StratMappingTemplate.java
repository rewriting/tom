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

import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.backend.MappingTemplateClass;
import java.io.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class StratMappingTemplate extends MappingTemplateClass {
  GomClassList operatorClasses;

  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */private static boolean tom_equal_term_char(char t1, char t2) { return  (t1==t2) ;}private static boolean tom_is_sort_char(char t) { return  true ;} private static boolean tom_equal_term_String(String t1, String t2) { return  (t1.equals(t2)) ;}private static boolean tom_is_sort_String(String t) { return  t instanceof String ;}  /* Generated by TOM (version 2.6alpha): Do not edit this file */private static boolean tom_equal_term_int(int t1, int t2) { return  (t1==t2) ;}private static boolean tom_is_sort_int(int t) { return  true ;} private static boolean tom_equal_term_Code(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Code(Object t) { return  t instanceof tom.gom.adt.code.types.Code ;}private static boolean tom_equal_term_Hook(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Hook(Object t) { return  t instanceof tom.gom.adt.objects.types.Hook ;}private static boolean tom_equal_term_SlotField(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotField(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotField ;}private static boolean tom_equal_term_SlotFieldList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotFieldList(Object t) { return  t instanceof tom.gom.adt.objects.types.SlotFieldList ;}private static boolean tom_equal_term_GomClass(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomClass(Object t) { return  t instanceof tom.gom.adt.objects.types.GomClass ;}private static boolean tom_equal_term_ClassName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ClassName(Object t) { return  t instanceof tom.gom.adt.objects.types.ClassName ;}private static boolean tom_equal_term_GomClassList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomClassList(Object t) { return  t instanceof tom.gom.adt.objects.types.GomClassList ;}private static boolean tom_equal_term_ClassNameList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ClassNameList(Object t) { return  t instanceof tom.gom.adt.objects.types.ClassNameList ;}private static boolean tom_equal_term_HookList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookList(Object t) { return  t instanceof tom.gom.adt.objects.types.HookList ;}private static boolean tom_equal_term_Slot(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Slot(Object t) { return  t instanceof tom.gom.adt.gom.types.Slot ;}private static boolean tom_equal_term_ArgList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ArgList(Object t) { return  t instanceof tom.gom.adt.gom.types.ArgList ;}private static boolean tom_equal_term_IdKind(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_IdKind(Object t) { return  t instanceof tom.gom.adt.gom.types.IdKind ;}private static boolean tom_equal_term_GomModuleList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModuleList(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModuleList ;}private static boolean tom_equal_term_GrammarList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GrammarList(Object t) { return  t instanceof tom.gom.adt.gom.types.GrammarList ;}private static boolean tom_equal_term_TypedProduction(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TypedProduction(Object t) { return  t instanceof tom.gom.adt.gom.types.TypedProduction ;}private static boolean tom_equal_term_SectionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SectionList(Object t) { return  t instanceof tom.gom.adt.gom.types.SectionList ;}private static boolean tom_equal_term_OperatorDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OperatorDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.OperatorDeclList ;}private static boolean tom_equal_term_HookDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.HookDeclList ;}private static boolean tom_equal_term_SortDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.SortDeclList ;}private static boolean tom_equal_term_OperatorDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OperatorDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.OperatorDecl ;}private static boolean tom_equal_term_GomModule(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModule(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModule ;}private static boolean tom_equal_term_Pair(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Pair(Object t) { return  t instanceof tom.gom.adt.gom.types.Pair ;}private static boolean tom_equal_term_Section(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Section(Object t) { return  t instanceof tom.gom.adt.gom.types.Section ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) { return  t instanceof tom.gom.adt.gom.types.GomTypeList ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ProductionList(Object t) { return  t instanceof tom.gom.adt.gom.types.ProductionList ;}private static boolean tom_equal_term_Decl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Decl(Object t) { return  t instanceof tom.gom.adt.gom.types.Decl ;}private static boolean tom_equal_term_ImportList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ImportList(Object t) { return  t instanceof tom.gom.adt.gom.types.ImportList ;}private static boolean tom_equal_term_Sort(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Sort(Object t) { return  t instanceof tom.gom.adt.gom.types.Sort ;}private static boolean tom_equal_term_SortDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.SortDecl ;}private static boolean tom_equal_term_HookDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.HookDecl ;}private static boolean tom_equal_term_ModuleDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleDeclList(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleDeclList ;}private static boolean tom_equal_term_Module(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Module(Object t) { return  t instanceof tom.gom.adt.gom.types.Module ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_FieldList(Object t) { return  t instanceof tom.gom.adt.gom.types.FieldList ;}private static boolean tom_equal_term_Grammar(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Grammar(Object t) { return  t instanceof tom.gom.adt.gom.types.Grammar ;}private static boolean tom_equal_term_SlotList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SlotList(Object t) { return  t instanceof tom.gom.adt.gom.types.SlotList ;}private static boolean tom_equal_term_SortList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SortList(Object t) { return  t instanceof tom.gom.adt.gom.types.SortList ;}private static boolean tom_equal_term_Arg(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Arg(Object t) { return  t instanceof tom.gom.adt.gom.types.Arg ;}private static boolean tom_equal_term_GomModuleName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomModuleName(Object t) { return  t instanceof tom.gom.adt.gom.types.GomModuleName ;}private static boolean tom_equal_term_Field(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Field(Object t) { return  t instanceof tom.gom.adt.gom.types.Field ;}private static boolean tom_equal_term_Production(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Production(Object t) { return  t instanceof tom.gom.adt.gom.types.Production ;}private static boolean tom_equal_term_ModuleDecl(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleDecl(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleDecl ;}private static boolean tom_equal_term_ModuleList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ModuleList(Object t) { return  t instanceof tom.gom.adt.gom.types.ModuleList ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_GomType(Object t) { return  t instanceof tom.gom.adt.gom.types.GomType ;}private static boolean tom_equal_term_ImportedModule(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ImportedModule(Object t) { return  t instanceof tom.gom.adt.gom.types.ImportedModule ;}private static boolean tom_equal_term_Option(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Option(Object t) { return  t instanceof tom.gom.adt.gom.types.Option ;}private static boolean tom_equal_term_HookKind(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_HookKind(Object t) { return  t instanceof tom.gom.adt.gom.types.HookKind ;}private static boolean tom_is_fun_sym_OperatorClass( tom.gom.adt.objects.types.GomClass  t) { return  (t instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_ClassName( tom.gom.adt.objects.types.GomClass  t) { return  t.getClassName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_AbstractType( tom.gom.adt.objects.types.GomClass  t) { return  t.getAbstractType() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_ExtendsType( tom.gom.adt.objects.types.GomClass  t) { return  t.getExtendsType() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_Mapping( tom.gom.adt.objects.types.GomClass  t) { return  t.getMapping() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_SortName( tom.gom.adt.objects.types.GomClass  t) { return  t.getSortName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_OperatorClass_Visitor( tom.gom.adt.objects.types.GomClass  t) { return  t.getVisitor() ;}private static  tom.gom.adt.objects.types.SlotFieldList  tom_get_slot_OperatorClass_Slots( tom.gom.adt.objects.types.GomClass  t) { return  t.getSlots() ;}private static  tom.gom.adt.objects.types.HookList  tom_get_slot_OperatorClass_Hooks( tom.gom.adt.objects.types.GomClass  t) { return  t.getHooks() ;}private static boolean tom_is_fun_sym_VariadicOperatorClass( tom.gom.adt.objects.types.GomClass  t) { return  (t instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_VariadicOperatorClass_ClassName( tom.gom.adt.objects.types.GomClass  t) { return  t.getClassName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_VariadicOperatorClass_AbstractType( tom.gom.adt.objects.types.GomClass  t) { return  t.getAbstractType() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_VariadicOperatorClass_Mapping( tom.gom.adt.objects.types.GomClass  t) { return  t.getMapping() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_VariadicOperatorClass_SortName( tom.gom.adt.objects.types.GomClass  t) { return  t.getSortName() ;}private static  tom.gom.adt.objects.types.GomClass  tom_get_slot_VariadicOperatorClass_Empty( tom.gom.adt.objects.types.GomClass  t) { return  t.getEmpty() ;}private static  tom.gom.adt.objects.types.GomClass  tom_get_slot_VariadicOperatorClass_Cons( tom.gom.adt.objects.types.GomClass  t) { return  t.getCons() ;}private static  tom.gom.adt.objects.types.HookList  tom_get_slot_VariadicOperatorClass_Hooks( tom.gom.adt.objects.types.GomClass  t) { return  t.getHooks() ;}private static boolean tom_is_fun_sym_TomMapping( tom.gom.adt.objects.types.GomClass  t) { return  (t instanceof tom.gom.adt.objects.types.gomclass.TomMapping) ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_TomMapping_ClassName( tom.gom.adt.objects.types.GomClass  t) { return  t.getClassName() ;}private static  tom.gom.adt.objects.types.ClassName  tom_get_slot_TomMapping_BasicStrategy( tom.gom.adt.objects.types.GomClass  t) { return  t.getBasicStrategy() ;}private static  tom.gom.adt.objects.types.GomClassList  tom_get_slot_TomMapping_SortClasses( tom.gom.adt.objects.types.GomClass  t) { return  t.getSortClasses() ;}private static  tom.gom.adt.objects.types.GomClassList  tom_get_slot_TomMapping_OperatorClasses( tom.gom.adt.objects.types.GomClass  t) { return  t.getOperatorClasses() ;}private static boolean tom_is_fun_sym_concGomClass( tom.gom.adt.objects.types.GomClassList  t) { return  ((t instanceof tom.gom.adt.objects.types.gomclasslist.ConsconcGomClass) || (t instanceof tom.gom.adt.objects.types.gomclasslist.EmptyconcGomClass)) ;}private static  tom.gom.adt.objects.types.GomClassList  tom_empty_list_concGomClass() { return  tom.gom.adt.objects.types.gomclasslist.EmptyconcGomClass.make() ; }private static  tom.gom.adt.objects.types.GomClassList  tom_cons_list_concGomClass( tom.gom.adt.objects.types.GomClass  e,  tom.gom.adt.objects.types.GomClassList  l) { return  tom.gom.adt.objects.types.gomclasslist.ConsconcGomClass.make(e,l) ; }private static  tom.gom.adt.objects.types.GomClass  tom_get_head_concGomClass_GomClassList( tom.gom.adt.objects.types.GomClassList  l) { return  l.getHeadconcGomClass() ;}private static  tom.gom.adt.objects.types.GomClassList  tom_get_tail_concGomClass_GomClassList( tom.gom.adt.objects.types.GomClassList  l) { return  l.getTailconcGomClass() ;}private static boolean tom_is_empty_concGomClass_GomClassList( tom.gom.adt.objects.types.GomClassList  l) { return  l.isEmptyconcGomClass() ;}   private static   tom.gom.adt.objects.types.GomClassList  tom_append_list_concGomClass( tom.gom.adt.objects.types.GomClassList l1,  tom.gom.adt.objects.types.GomClassList  l2) {     if( l1.isEmptyconcGomClass() ) {       return l2;     } else if( l2.isEmptyconcGomClass() ) {       return l1;     } else if(  l1.getTailconcGomClass() .isEmptyconcGomClass() ) {       return  tom.gom.adt.objects.types.gomclasslist.ConsconcGomClass.make( l1.getHeadconcGomClass() ,l2) ;     } else {       return  tom.gom.adt.objects.types.gomclasslist.ConsconcGomClass.make( l1.getHeadconcGomClass() ,tom_append_list_concGomClass( l1.getTailconcGomClass() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.GomClassList  tom_get_slice_concGomClass( tom.gom.adt.objects.types.GomClassList  begin,  tom.gom.adt.objects.types.GomClassList  end, tom.gom.adt.objects.types.GomClassList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.gom.adt.objects.types.gomclasslist.ConsconcGomClass.make( begin.getHeadconcGomClass() ,( tom.gom.adt.objects.types.GomClassList )tom_get_slice_concGomClass( begin.getTailconcGomClass() ,end,tail)) ;     }   }    

  public StratMappingTemplate(GomClass gomClass) {
    super(gomClass);
    if (tom_is_sort_GomClass(gomClass)) {{  tom.gom.adt.objects.types.GomClass  tomMatch431NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClass )gomClass);if (tom_is_fun_sym_TomMapping(tomMatch431NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.GomClassList  tomMatch431NameNumber_freshVar_0=tom_get_slot_TomMapping_OperatorClasses(tomMatch431NameNumberfreshSubject_1);if ( true ) {

        this.operatorClasses = tomMatch431NameNumber_freshVar_0;
        return;
      }}}}}

    throw new GomRuntimeException(
        "Wrong argument for MappingTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    generateTomMapping(writer, null);
  }

  public void generateTomMapping(Writer writer, ClassName basicStrategy)
      throws java.io.IOException {

    writer.write("\n   /*\n   %include { mustrategy.tom }\n   */\n"



);
    if (tom_is_sort_GomClassList(operatorClasses)) {{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClassList )operatorClasses);if (tom_is_fun_sym_concGomClass(tomMatch432NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_freshVar_0=tomMatch432NameNumberfreshSubject_1;{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_begin_2=tomMatch432NameNumber_freshVar_0;{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_end_3=tomMatch432NameNumber_freshVar_0;do {{{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_freshVar_1=tomMatch432NameNumber_end_3;if (!(tom_is_empty_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_1))) {if (tom_is_fun_sym_OperatorClass(tom_get_head_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_1))) {{  tom.gom.adt.objects.types.GomClass  tom_op=tom_get_head_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_1);{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_freshVar_4=tom_get_tail_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_1);if ( true ) {

        writer.write(
            (new tom.gom.backend.strategy.SOpTemplate(tom_op)).generateMapping());
        writer.write(
            (new tom.gom.backend.strategy.IsOpTemplate(tom_op)).generateMapping());
        writer.write(
            (new tom.gom.backend.strategy.MakeOpTemplate(tom_op)).generateMapping());
      }}}}}}if (tom_is_empty_concGomClass_GomClassList(tomMatch432NameNumber_end_3)) {tomMatch432NameNumber_end_3=tomMatch432NameNumber_begin_2;} else {tomMatch432NameNumber_end_3=tom_get_tail_concGomClass_GomClassList(tomMatch432NameNumber_end_3);}}} while(!(tom_equal_term_GomClassList(tomMatch432NameNumber_end_3, tomMatch432NameNumber_begin_2)));}}}}if (tom_is_fun_sym_concGomClass(tomMatch432NameNumberfreshSubject_1)) {{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_freshVar_6=tomMatch432NameNumberfreshSubject_1;{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_begin_8=tomMatch432NameNumber_freshVar_6;{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_end_9=tomMatch432NameNumber_freshVar_6;do {{{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_freshVar_7=tomMatch432NameNumber_end_9;if (!(tom_is_empty_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_7))) {if (tom_is_fun_sym_VariadicOperatorClass(tom_get_head_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_7))) {{  tom.gom.adt.objects.types.ClassName  tomMatch432NameNumber_freshVar_12=tom_get_slot_VariadicOperatorClass_ClassName(tom_get_head_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_7));{  tom.gom.adt.objects.types.GomClass  tomMatch432NameNumber_freshVar_13=tom_get_slot_VariadicOperatorClass_Empty(tom_get_head_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_7));{  tom.gom.adt.objects.types.GomClass  tomMatch432NameNumber_freshVar_14=tom_get_slot_VariadicOperatorClass_Cons(tom_get_head_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_7));{  tom.gom.adt.objects.types.ClassName  tom_vopName=tomMatch432NameNumber_freshVar_12;if (tom_is_fun_sym_OperatorClass(tomMatch432NameNumber_freshVar_13)) {{  tom.gom.adt.objects.types.ClassName  tomMatch432NameNumber_freshVar_15=tom_get_slot_OperatorClass_ClassName(tomMatch432NameNumber_freshVar_13);if (tom_is_fun_sym_OperatorClass(tomMatch432NameNumber_freshVar_14)) {{  tom.gom.adt.objects.types.ClassName  tomMatch432NameNumber_freshVar_16=tom_get_slot_OperatorClass_ClassName(tomMatch432NameNumber_freshVar_14);{  tom.gom.adt.objects.types.GomClassList  tomMatch432NameNumber_freshVar_10=tom_get_tail_concGomClass_GomClassList(tomMatch432NameNumber_freshVar_7);if ( true ) {





        writer.write("\n            %op Strategy _"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tom_vopName)+"(sub:Strategy) {\n            is_fsym(t) { false }\n            make(sub)  { `mu(MuVar(\"x_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tom_vopName)+"\"),Choice(_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tomMatch432NameNumber_freshVar_16)+"(sub,MuVar(\"x_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tom_vopName)+"\")),_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tomMatch432NameNumber_freshVar_15)+"())) }\n            }\n            "




);
      }}}}}}}}}}}}}if (tom_is_empty_concGomClass_GomClassList(tomMatch432NameNumber_end_9)) {tomMatch432NameNumber_end_9=tomMatch432NameNumber_begin_8;} else {tomMatch432NameNumber_end_9=tom_get_tail_concGomClass_GomClassList(tomMatch432NameNumber_end_9);}}} while(!(tom_equal_term_GomClassList(tomMatch432NameNumber_end_9, tomMatch432NameNumber_begin_8)));}}}}}}

      }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".tom";
  }

}
