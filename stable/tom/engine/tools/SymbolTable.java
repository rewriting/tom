/* Generated by TOM (version 2.5alpha): Do not edit this file *//*
 *
 * TOM - To One Matching Compiler
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import tom.engine.exception.TomRuntimeException;

import tom.engine.TomBase;
import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.platform.OptionManager;

public class SymbolTable {

  /* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file */ private static boolean tom_equal_term_String(String t1, String t2) { return  (t1.equals(t2)) ;}private static boolean tom_is_sort_String(String t) { return  t instanceof String ;}  /* Generated by TOM (version 2.5alpha): Do not edit this file */ private static boolean tom_equal_term_Instruction(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Instruction(Object t) { return  t instanceof tom.engine.adt.tominstruction.types.Instruction ;}private static boolean tom_equal_term_TomTypeList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomTypeList(Object t) { return  t instanceof tom.engine.adt.tomtype.types.TomTypeList ;}private static boolean tom_equal_term_TomType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomType(Object t) { return  t instanceof tom.engine.adt.tomtype.types.TomType ;}private static boolean tom_equal_term_TomTypeDefinition(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomTypeDefinition(Object t) { return  t instanceof tom.engine.adt.tomtype.types.TomTypeDefinition ;}private static boolean tom_equal_term_TomForwardType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomForwardType(Object t) { return  t instanceof tom.engine.adt.tomtype.types.TomForwardType ;}private static boolean tom_equal_term_TomSymbolList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomSymbolList(Object t) { return  t instanceof tom.engine.adt.tomsignature.types.TomSymbolList ;}private static boolean tom_equal_term_TomEntry(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomEntry(Object t) { return  t instanceof tom.engine.adt.tomsignature.types.TomEntry ;}private static boolean tom_equal_term_TomEntryList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomEntryList(Object t) { return  t instanceof tom.engine.adt.tomsignature.types.TomEntryList ;}private static boolean tom_equal_term_TomSymbolTable(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomSymbolTable(Object t) { return  t instanceof tom.engine.adt.tomsignature.types.TomSymbolTable ;}private static boolean tom_equal_term_TomSymbol(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomSymbol(Object t) { return  t instanceof tom.engine.adt.tomsignature.types.TomSymbol ;}private static boolean tom_equal_term_KeyEntry(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_KeyEntry(Object t) { return  t instanceof tom.engine.adt.tomsignature.types.KeyEntry ;}private static boolean tom_equal_term_Declaration(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Declaration(Object t) { return  t instanceof tom.engine.adt.tomdeclaration.types.Declaration ;}private static boolean tom_equal_term_TomName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomName(Object t) { return  t instanceof tom.engine.adt.tomname.types.TomName ;}private static boolean tom_equal_term_TomList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomList(Object t) { return  t instanceof tom.engine.adt.tomterm.types.TomList ;}private static boolean tom_equal_term_Option(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Option(Object t) { return  t instanceof tom.engine.adt.tomoption.types.Option ;}private static boolean tom_equal_term_OptionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OptionList(Object t) { return  t instanceof tom.engine.adt.tomoption.types.OptionList ;}private static boolean tom_equal_term_PairNameDeclList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_PairNameDeclList(Object t) { return  t instanceof tom.engine.adt.tomslot.types.PairNameDeclList ;}private static boolean tom_is_fun_sym_Codomain( tom.engine.adt.tomtype.types.TomType  t) { return  t instanceof tom.engine.adt.tomtype.types.tomtype.Codomain ;}private static  tom.engine.adt.tomname.types.TomName  tom_get_slot_Codomain_AstName( tom.engine.adt.tomtype.types.TomType  t) { return  t.getAstName() ;}private static boolean tom_is_fun_sym_TypesToType( tom.engine.adt.tomtype.types.TomType  t) { return  t instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType ;}private static  tom.engine.adt.tomtype.types.TomType  tom_make_TypesToType( tom.engine.adt.tomtype.types.TomTypeList  t0,  tom.engine.adt.tomtype.types.TomType  t1) { return  tom.engine.adt.tomtype.types.tomtype.TypesToType.make(t0, t1) ; }private static  tom.engine.adt.tomtype.types.TomTypeList  tom_get_slot_TypesToType_Domain( tom.engine.adt.tomtype.types.TomType  t) { return  t.getDomain() ;}private static  tom.engine.adt.tomtype.types.TomType  tom_get_slot_TypesToType_Codomain( tom.engine.adt.tomtype.types.TomType  t) { return  t.getCodomain() ;}private static  tom.engine.adt.tomtype.types.TomTypeDefinition  tom_make_TypeDefinition( tom.engine.adt.tomtype.types.TomType  t0,  tom.engine.adt.tomtype.types.TomForwardType  t1) { return  tom.engine.adt.tomtype.types.tomtypedefinition.TypeDefinition.make(t0, t1) ; }private static  tom.engine.adt.tomsignature.types.TomEntry  tom_make_Entry( String  t0,  tom.engine.adt.tomsignature.types.TomSymbol  t1) { return  tom.engine.adt.tomsignature.types.tomentry.Entry.make(t0, t1) ; }private static  tom.engine.adt.tomsignature.types.TomSymbolTable  tom_make_Table( tom.engine.adt.tomsignature.types.TomEntryList  t0) { return  tom.engine.adt.tomsignature.types.tomsymboltable.Table.make(t0) ; }private static boolean tom_is_fun_sym_Symbol( tom.engine.adt.tomsignature.types.TomSymbol  t) { return  t instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol ;}private static  tom.engine.adt.tomsignature.types.TomSymbol  tom_make_Symbol( tom.engine.adt.tomname.types.TomName  t0,  tom.engine.adt.tomtype.types.TomType  t1,  tom.engine.adt.tomslot.types.PairNameDeclList  t2,  tom.engine.adt.tomoption.types.OptionList  t3) { return  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(t0, t1, t2, t3) ; }private static  tom.engine.adt.tomname.types.TomName  tom_get_slot_Symbol_AstName( tom.engine.adt.tomsignature.types.TomSymbol  t) { return  t.getAstName() ;}private static  tom.engine.adt.tomtype.types.TomType  tom_get_slot_Symbol_TypesToType( tom.engine.adt.tomsignature.types.TomSymbol  t) { return  t.getTypesToType() ;}private static  tom.engine.adt.tomslot.types.PairNameDeclList  tom_get_slot_Symbol_PairNameDeclList( tom.engine.adt.tomsignature.types.TomSymbol  t) { return  t.getPairNameDeclList() ;}private static  tom.engine.adt.tomoption.types.OptionList  tom_get_slot_Symbol_Option( tom.engine.adt.tomsignature.types.TomSymbol  t) { return  t.getOption() ;}private static  tom.engine.adt.tomsignature.types.KeyEntry  tom_make_UsedSymbolConstructor( tom.engine.adt.tomsignature.types.TomSymbol  t0) { return  tom.engine.adt.tomsignature.types.keyentry.UsedSymbolConstructor.make(t0) ; }private static  tom.engine.adt.tomsignature.types.KeyEntry  tom_make_UsedSymbolDestructor( tom.engine.adt.tomsignature.types.TomSymbol  t0) { return  tom.engine.adt.tomsignature.types.keyentry.UsedSymbolDestructor.make(t0) ; }private static  tom.engine.adt.tomsignature.types.KeyEntry  tom_make_UsedTypeDefinition( tom.engine.adt.tomtype.types.TomTypeDefinition  t0) { return  tom.engine.adt.tomsignature.types.keyentry.UsedTypeDefinition.make(t0) ; }private static boolean tom_is_fun_sym_MakeDecl( tom.engine.adt.tomdeclaration.types.Declaration  t) { return  t instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl ;}private static  tom.engine.adt.tomname.types.TomName  tom_get_slot_MakeDecl_AstName( tom.engine.adt.tomdeclaration.types.Declaration  t) { return  t.getAstName() ;}private static  tom.engine.adt.tomtype.types.TomType  tom_get_slot_MakeDecl_AstType( tom.engine.adt.tomdeclaration.types.Declaration  t) { return  t.getAstType() ;}private static  tom.engine.adt.tomterm.types.TomList  tom_get_slot_MakeDecl_Args( tom.engine.adt.tomdeclaration.types.Declaration  t) { return  t.getArgs() ;}private static  tom.engine.adt.tominstruction.types.Instruction  tom_get_slot_MakeDecl_Instr( tom.engine.adt.tomdeclaration.types.Declaration  t) { return  t.getInstr() ;}private static  tom.engine.adt.tomoption.types.Option  tom_get_slot_MakeDecl_OrgTrack( tom.engine.adt.tomdeclaration.types.Declaration  t) { return  t.getOrgTrack() ;}private static boolean tom_is_fun_sym_Name( tom.engine.adt.tomname.types.TomName  t) { return  t instanceof tom.engine.adt.tomname.types.tomname.Name ;}private static  String  tom_get_slot_Name_String( tom.engine.adt.tomname.types.TomName  t) { return  t.getString() ;}private static boolean tom_is_fun_sym_DeclarationToOption( tom.engine.adt.tomoption.types.Option  t) { return  t instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption ;}private static  tom.engine.adt.tomoption.types.Option  tom_make_DeclarationToOption( tom.engine.adt.tomdeclaration.types.Declaration  t0) { return  tom.engine.adt.tomoption.types.option.DeclarationToOption.make(t0) ; }private static  tom.engine.adt.tomdeclaration.types.Declaration  tom_get_slot_DeclarationToOption_AstDeclaration( tom.engine.adt.tomoption.types.Option  t) { return  t.getAstDeclaration() ;}private static boolean tom_is_fun_sym_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbolList  t) { return  t instanceof tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol || t instanceof tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol ;}private static  tom.engine.adt.tomsignature.types.TomSymbolList  tom_empty_list_concTomSymbol() { return  tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol.make() ; }private static  tom.engine.adt.tomsignature.types.TomSymbolList  tom_cons_list_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbol  e,  tom.engine.adt.tomsignature.types.TomSymbolList  l) { return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make(e,l) ; }private static  tom.engine.adt.tomsignature.types.TomSymbol  tom_get_head_concTomSymbol_TomSymbolList( tom.engine.adt.tomsignature.types.TomSymbolList  l) { return  l.getHeadconcTomSymbol() ;}private static  tom.engine.adt.tomsignature.types.TomSymbolList  tom_get_tail_concTomSymbol_TomSymbolList( tom.engine.adt.tomsignature.types.TomSymbolList  l) { return  l.getTailconcTomSymbol() ;}private static boolean tom_is_empty_concTomSymbol_TomSymbolList( tom.engine.adt.tomsignature.types.TomSymbolList  l) { return  l.isEmptyconcTomSymbol() ;}   private static   tom.engine.adt.tomsignature.types.TomSymbolList  tom_append_list_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbolList l1,  tom.engine.adt.tomsignature.types.TomSymbolList  l2) {     if(tom_is_empty_concTomSymbol_TomSymbolList(l1)) {       return l2;     } else if(tom_is_empty_concTomSymbol_TomSymbolList(l2)) {       return l1;     } else if(tom_is_empty_concTomSymbol_TomSymbolList(tom_get_tail_concTomSymbol_TomSymbolList(l1))) {       return ( tom.engine.adt.tomsignature.types.TomSymbolList )tom_cons_list_concTomSymbol(tom_get_head_concTomSymbol_TomSymbolList(l1),l2);     } else {       return ( tom.engine.adt.tomsignature.types.TomSymbolList )tom_cons_list_concTomSymbol(tom_get_head_concTomSymbol_TomSymbolList(l1),tom_append_list_concTomSymbol(tom_get_tail_concTomSymbol_TomSymbolList(l1),l2));     }   }   private static   tom.engine.adt.tomsignature.types.TomSymbolList  tom_get_slice_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbolList  begin,  tom.engine.adt.tomsignature.types.TomSymbolList  end, tom.engine.adt.tomsignature.types.TomSymbolList  tail) {     if(tom_equal_term_TomSymbolList(begin,end)) {       return tail;     } else {       return ( tom.engine.adt.tomsignature.types.TomSymbolList )tom_cons_list_concTomSymbol(tom_get_head_concTomSymbol_TomSymbolList(begin),( tom.engine.adt.tomsignature.types.TomSymbolList )tom_get_slice_concTomSymbol(tom_get_tail_concTomSymbol_TomSymbolList(begin),end,tail));     }   }   private static boolean tom_is_fun_sym_concTomEntry( tom.engine.adt.tomsignature.types.TomEntryList  t) { return  t instanceof tom.engine.adt.tomsignature.types.tomentrylist.ConsconcTomEntry || t instanceof tom.engine.adt.tomsignature.types.tomentrylist.EmptyconcTomEntry ;}private static  tom.engine.adt.tomsignature.types.TomEntryList  tom_empty_list_concTomEntry() { return  tom.engine.adt.tomsignature.types.tomentrylist.EmptyconcTomEntry.make() ; }private static  tom.engine.adt.tomsignature.types.TomEntryList  tom_cons_list_concTomEntry( tom.engine.adt.tomsignature.types.TomEntry  e,  tom.engine.adt.tomsignature.types.TomEntryList  l) { return  tom.engine.adt.tomsignature.types.tomentrylist.ConsconcTomEntry.make(e,l) ; }private static  tom.engine.adt.tomsignature.types.TomEntry  tom_get_head_concTomEntry_TomEntryList( tom.engine.adt.tomsignature.types.TomEntryList  l) { return  l.getHeadconcTomEntry() ;}private static  tom.engine.adt.tomsignature.types.TomEntryList  tom_get_tail_concTomEntry_TomEntryList( tom.engine.adt.tomsignature.types.TomEntryList  l) { return  l.getTailconcTomEntry() ;}private static boolean tom_is_empty_concTomEntry_TomEntryList( tom.engine.adt.tomsignature.types.TomEntryList  l) { return  l.isEmptyconcTomEntry() ;}   private static   tom.engine.adt.tomsignature.types.TomEntryList  tom_append_list_concTomEntry( tom.engine.adt.tomsignature.types.TomEntryList l1,  tom.engine.adt.tomsignature.types.TomEntryList  l2) {     if(tom_is_empty_concTomEntry_TomEntryList(l1)) {       return l2;     } else if(tom_is_empty_concTomEntry_TomEntryList(l2)) {       return l1;     } else if(tom_is_empty_concTomEntry_TomEntryList(tom_get_tail_concTomEntry_TomEntryList(l1))) {       return ( tom.engine.adt.tomsignature.types.TomEntryList )tom_cons_list_concTomEntry(tom_get_head_concTomEntry_TomEntryList(l1),l2);     } else {       return ( tom.engine.adt.tomsignature.types.TomEntryList )tom_cons_list_concTomEntry(tom_get_head_concTomEntry_TomEntryList(l1),tom_append_list_concTomEntry(tom_get_tail_concTomEntry_TomEntryList(l1),l2));     }   }   private static   tom.engine.adt.tomsignature.types.TomEntryList  tom_get_slice_concTomEntry( tom.engine.adt.tomsignature.types.TomEntryList  begin,  tom.engine.adt.tomsignature.types.TomEntryList  end, tom.engine.adt.tomsignature.types.TomEntryList  tail) {     if(tom_equal_term_TomEntryList(begin,end)) {       return tail;     } else {       return ( tom.engine.adt.tomsignature.types.TomEntryList )tom_cons_list_concTomEntry(tom_get_head_concTomEntry_TomEntryList(begin),( tom.engine.adt.tomsignature.types.TomEntryList )tom_get_slice_concTomEntry(tom_get_tail_concTomEntry_TomEntryList(begin),end,tail));     }   }   private static boolean tom_is_fun_sym_concOption( tom.engine.adt.tomoption.types.OptionList  t) { return  t instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption || t instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption ;}private static  tom.engine.adt.tomoption.types.OptionList  tom_empty_list_concOption() { return  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ; }private static  tom.engine.adt.tomoption.types.OptionList  tom_cons_list_concOption( tom.engine.adt.tomoption.types.Option  e,  tom.engine.adt.tomoption.types.OptionList  l) { return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(e,l) ; }private static  tom.engine.adt.tomoption.types.Option  tom_get_head_concOption_OptionList( tom.engine.adt.tomoption.types.OptionList  l) { return  l.getHeadconcOption() ;}private static  tom.engine.adt.tomoption.types.OptionList  tom_get_tail_concOption_OptionList( tom.engine.adt.tomoption.types.OptionList  l) { return  l.getTailconcOption() ;}private static boolean tom_is_empty_concOption_OptionList( tom.engine.adt.tomoption.types.OptionList  l) { return  l.isEmptyconcOption() ;}   private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if(tom_is_empty_concOption_OptionList(l1)) {       return l2;     } else if(tom_is_empty_concOption_OptionList(l2)) {       return l1;     } else if(tom_is_empty_concOption_OptionList(tom_get_tail_concOption_OptionList(l1))) {       return ( tom.engine.adt.tomoption.types.OptionList )tom_cons_list_concOption(tom_get_head_concOption_OptionList(l1),l2);     } else {       return ( tom.engine.adt.tomoption.types.OptionList )tom_cons_list_concOption(tom_get_head_concOption_OptionList(l1),tom_append_list_concOption(tom_get_tail_concOption_OptionList(l1),l2));     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if(tom_equal_term_OptionList(begin,end)) {       return tail;     } else {       return ( tom.engine.adt.tomoption.types.OptionList )tom_cons_list_concOption(tom_get_head_concOption_OptionList(begin),( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption(tom_get_tail_concOption_OptionList(begin),end,tail));     }   }    

  private final static String TYPE_INT       = "int";
  private final static String TYPE_LONG      = "long";
  private final static String TYPE_FLOAT     = "float";
  private final static String TYPE_CHAR      = "char";
  private final static String TYPE_DOUBLE    = "double";
  private final static String TYPE_STRING    = "String";
  private final static String TYPE_BOOLEAN   = "boolean";
  private final static String TYPE_UNIVERSAL = "universal";
  private final static String TYPE_VOID      = "void";

  private Map mapSymbolName = null;
  private Map mapTypeName = null;

  private boolean cCode = false;
  private boolean jCode = false;
  private boolean camlCode = false;
  private boolean pCode = false;

  public void init(OptionManager optionManager) {
    mapSymbolName = new HashMap();
    mapTypeName = new HashMap();

    if( ((Boolean)optionManager.getOptionValue("cCode")).booleanValue() ) {
      cCode = true;
    } else if( ((Boolean)optionManager.getOptionValue("jCode")).booleanValue() ) {
      jCode = true;
    } else if( ((Boolean)optionManager.getOptionValue("camlCode")).booleanValue() ) {
      camlCode = true;
    } else if( ((Boolean)optionManager.getOptionValue("pCode")).booleanValue() ) {
      pCode = true;
    }

  }

  public void regenerateFromTerm(TomSymbolTable symbTable) {
    TomEntryList list =  symbTable.getEntryList();
    while(!list.isEmptyconcTomEntry()) {
      TomEntry symb = list.getHeadconcTomEntry();
      putSymbol(symb.getStrName(), symb.getAstSymbol());
      list = list.getTailconcTomEntry();
    }
  }

  public void putSymbol(String name, TomSymbol astSymbol) {
    TomSymbol result = (TomSymbol) mapSymbolName.put(name,astSymbol);
  }

  public TomSymbol getSymbolFromName(String name) {
    TomSymbol res = (TomSymbol)mapSymbolName.get(name);
    return res;
  }

  public TomSymbolList getSymbolFromType(TomType type) {
    TomSymbolList res = tom_empty_list_concTomSymbol();
    Iterator it = mapSymbolName.values().iterator();
    while(it.hasNext()) {
      TomSymbol symbol = (TomSymbol)it.next();
      if(symbol.getTypesToType().getCodomain() == type) {
        res = tom_cons_list_concTomSymbol(symbol,tom_append_list_concTomSymbol(res,tom_empty_list_concTomSymbol()));
      }
    }
    return res;
  }

  public void putTypeDefinition(String name, TomType astType, TomForwardType fwdType) {
    TomTypeDefinition typeDef = tom_make_TypeDefinition(astType,fwdType);
    mapTypeName.put(name,typeDef);
  }

  public TomTypeDefinition getTypeDefinition(String name) {
    TomTypeDefinition def = (TomTypeDefinition) mapTypeName.get(name);
    return def;
  }

  public TomType getType(String name) {
    TomTypeDefinition def = getTypeDefinition(name);
    if (def != null) {
      TomType result = def.getTomType();
      return result;
    } else {
      return null;
    } 
  }

  public TomForwardType getForwardType(String name) {
    TomTypeDefinition def = getTypeDefinition(name);
    if (def != null) {
      TomForwardType result = def.getForward();
      return result;
    } else { 
      return null;
    } 
  }

  public boolean isUsedSymbolConstructor(TomSymbol symbol) {
    // System.out.println("con " + symbol.getAstName().getString() + ": " + (mapSymbolName.get(`UsedSymbolConstructor(symbol)) != null));
    return (mapSymbolName.get(tom_make_UsedSymbolConstructor(symbol)) != null);
    //return true;
  }

  public boolean isUsedSymbolDestructor(TomSymbol symbol) {
    // System.out.println("des " + symbol.getAstName().getString() + ": " + (mapSymbolName.get(`UsedSymbolDestructor(symbol)) != null));
    return (mapSymbolName.get(tom_make_UsedSymbolDestructor(symbol)) != null);
    //return true;
  }

  public boolean isUsedTypeDefinition(TomTypeDefinition type) {
    return (mapTypeName.get(tom_make_UsedTypeDefinition(type)) != null);
    //return true;
  }

  public void setUsedSymbolConstructor(TomSymbol symbol) {
    TomSymbol result = (TomSymbol) mapSymbolName.put(tom_make_UsedSymbolConstructor(symbol),symbol);
  }

  public void setUsedSymbolDestructor(TomSymbol symbol) {
    //System.out.println("setUsedDestructor: " + symbol.getAstName());
    TomSymbol result = (TomSymbol) mapSymbolName.put(tom_make_UsedSymbolDestructor(symbol),symbol);
  }

  public void setUsedTypeDefinition(TomTypeDefinition type) {
    TomTypeDefinition result = (TomTypeDefinition) mapTypeName.put(tom_make_UsedTypeDefinition(type),type);
  }

  public void setUsedSymbolConstructor(String name) {
    TomSymbol symbol = getSymbolFromName(name);
    if(symbol!=null) {
      setUsedSymbolConstructor(symbol);
    }
  }

  public void setUsedSymbolDestructor(String name) {
    TomSymbol symbol = getSymbolFromName(name);
    if(symbol!=null) {
      setUsedSymbolDestructor(symbol);
    }
  }

  public void setUsedTypeDefinition(String name) {
    TomTypeDefinition type = getTypeDefinition(name);
    if(type!=null) {
      setUsedTypeDefinition(type);
    }
  }

  public boolean isUsedSymbolConstructor(String name) {
    TomSymbol symbol = getSymbolFromName(name);
    if(symbol!=null) {
      return isUsedSymbolConstructor(symbol);
    }
    return false;
  }

  public boolean isUsedSymbolDestructor(String name) {
    TomSymbol symbol = getSymbolFromName(name);
    if(symbol!=null) {
      return isUsedSymbolDestructor(symbol);
    }
    return false;
  }

  public boolean isUsedTypeDefinition(String name) {
    TomTypeDefinition type = getTypeDefinition(name);
    if(type!=null) {
      return isUsedTypeDefinition(type);
    }
    return false;
  }

  public TomType getIntType() {
    return ASTFactory.makeType(TYPE_INT,"int");
  }

  public TomType getLongType() {
    return ASTFactory.makeType(TYPE_LONG,"long");
  }

  public TomType getFloatType() {
    return ASTFactory.makeType(TYPE_FLOAT,"float");
  }

  public TomType getCharType() {
    String type = "char";
    if(pCode) {
      type = "str";
    }
    return ASTFactory.makeType(TYPE_CHAR,type);
  }

  public TomType getDoubleType() {
    String type = "double";
    if(pCode) {
      type = "float";
    }
    return ASTFactory.makeType(TYPE_DOUBLE,type);
  }

  public TomType getBooleanType() {
    String type = "boolean";
    if(cCode) {
      type = "int";
    } else if(camlCode) {
      type = "bool";
    } else if(pCode) {
      type = "bool";
    } 
    return ASTFactory.makeType(TYPE_BOOLEAN,type);
  }

  public TomType getStringType() {
    String type = "String";
    if(pCode) {
      type = "char*";
    } else if(pCode) {
      type = "str";
    } 
    return ASTFactory.makeType(TYPE_STRING,type);
  }

  public TomType getUniversalType() {
    String type = "Object";
    if(cCode) {
      type = "void*";
    } else if(camlCode) {
      type = "None";
    } else if(pCode) {
      type = "None";
    }
    return ASTFactory.makeType(TYPE_UNIVERSAL,type);
  }

  public TomType getVoidType() {
    String type = "void";
    if(camlCode) {
      type = "unit";
    } else if(pCode) {
      type = "function";
    }
    return ASTFactory.makeType(TYPE_VOID,type);
  }

  public boolean isIntType(String type) {
    return type.equals(TYPE_INT);
  }

  public boolean isLongType(String type) {
    return type.equals(TYPE_LONG);
  }

  public boolean isFloatType(String type) {
    return type.equals(TYPE_FLOAT);
  }

  public boolean isCharType(String type) {
    return type.equals(TYPE_CHAR);
  }

  public boolean isStringType(String type) {
    return type.equals(TYPE_STRING);
  }

  public boolean isBooleanType(String type) {
    return type.equals(TYPE_BOOLEAN);
  }

  public boolean isDoubleType(String type) {
    return type.equals(TYPE_DOUBLE);
  }

  public boolean isVoidType(String type) {
    return type.equals(TYPE_VOID);
  }

  public boolean isBuiltinType(String type) {
    return isIntType(type) || isLongType(type) || isCharType(type) ||
      isStringType(type) || isBooleanType(type) || isDoubleType(type);
  }

  public TomType getBuiltinType(String type) {
    if(isIntType(type)) {
      return getIntType();
    } else if(isLongType(type)) {
      return getLongType();
    } else if(isCharType(type)) {
      return getCharType();
    } else if(isStringType(type)) {
      return getStringType();
    } else if(isBooleanType(type)) {
      return getBooleanType();
    } else if(isDoubleType(type)) {
      return getDoubleType();
    } 
    System.out.println("Not a builtin type: " + type);
    throw new TomRuntimeException("getBuiltinType error on term: " + type);
  }

  public Iterator keySymbolIterator() {
    Set keys = mapSymbolName.keySet();
    Iterator it = keys.iterator();
    return it;
  }

  public void fromTerm(TomSymbolTable table) {
    TomEntryList list = table.getEntryList();
    while(!list.isEmptyconcTomEntry()) {
      TomEntry entry = list.getHeadconcTomEntry();
      putSymbol(entry.getStrName(),entry.getAstSymbol());
      list = list.getTailconcTomEntry();
    }
  }

  public TomSymbolTable toTerm() {
    TomEntryList list = tom_empty_list_concTomEntry();
    Iterator it = keySymbolIterator();
    while(it.hasNext()) {
      Object key = it.next();
      if(key instanceof String) {
        String name = (String)key;
        TomSymbol symbol = getSymbolFromName(name);
        TomEntry entry = tom_make_Entry(name,symbol);
        list = tom_cons_list_concTomEntry(entry,tom_append_list_concTomEntry(list,tom_empty_list_concTomEntry()));
      }
    }
    return tom_make_Table(list);
  }

  public TomSymbol updateConstrainedSymbolCodomain(TomSymbol symbol, SymbolTable symbolTable) {
    if (tom_is_sort_TomSymbol(symbol)) {{  tom.engine.adt.tomsignature.types.TomSymbol  tomMatch245NameNumberfreshSubject_1=(( tom.engine.adt.tomsignature.types.TomSymbol )symbol);if (tom_is_fun_sym_Symbol(tomMatch245NameNumberfreshSubject_1)) {{  tom.engine.adt.tomname.types.TomName  tomMatch245NameNumber_freshVar_0=tom_get_slot_Symbol_AstName(tomMatch245NameNumberfreshSubject_1);{  tom.engine.adt.tomtype.types.TomType  tomMatch245NameNumber_freshVar_1=tom_get_slot_Symbol_TypesToType(tomMatch245NameNumberfreshSubject_1);{  tom.engine.adt.tomslot.types.PairNameDeclList  tomMatch245NameNumber_freshVar_2=tom_get_slot_Symbol_PairNameDeclList(tomMatch245NameNumberfreshSubject_1);{  tom.engine.adt.tomoption.types.OptionList  tomMatch245NameNumber_freshVar_3=tom_get_slot_Symbol_Option(tomMatch245NameNumberfreshSubject_1);{  tom.engine.adt.tomname.types.TomName  tom_name=tomMatch245NameNumber_freshVar_0;if (tom_is_fun_sym_TypesToType(tomMatch245NameNumber_freshVar_1)) {{  tom.engine.adt.tomtype.types.TomTypeList  tomMatch245NameNumber_freshVar_4=tom_get_slot_TypesToType_Domain(tomMatch245NameNumber_freshVar_1);{  tom.engine.adt.tomtype.types.TomType  tomMatch245NameNumber_freshVar_5=tom_get_slot_TypesToType_Codomain(tomMatch245NameNumber_freshVar_1);if (tom_is_fun_sym_Codomain(tomMatch245NameNumber_freshVar_5)) {{  tom.engine.adt.tomname.types.TomName  tomMatch245NameNumber_freshVar_6=tom_get_slot_Codomain_AstName(tomMatch245NameNumber_freshVar_5);if (tom_is_fun_sym_Name(tomMatch245NameNumber_freshVar_6)) {{  String  tomMatch245NameNumber_freshVar_7=tom_get_slot_Name_String(tomMatch245NameNumber_freshVar_6);{  tom.engine.adt.tomoption.types.OptionList  tom_options=tomMatch245NameNumber_freshVar_3;if ( true ) {

        //System.out.println("update codomain: " + `name);
        //System.out.println("depend from : " + `opName);
        TomSymbol dependSymbol = symbolTable.getSymbolFromName(tomMatch245NameNumber_freshVar_7);
        //System.out.println("1st depend codomain: " + TomBase.getSymbolCodomain(dependSymbol));
        dependSymbol = updateConstrainedSymbolCodomain(dependSymbol,symbolTable);
        TomType codomain = TomBase.getSymbolCodomain(dependSymbol);
        //System.out.println("2nd depend codomain: " + TomBase.getSymbolCodomain(dependSymbol));
        OptionList newOptions = tom_options;
        if (tom_is_sort_OptionList(tom_options)) {{  tom.engine.adt.tomoption.types.OptionList  tomMatch244NameNumberfreshSubject_1=(( tom.engine.adt.tomoption.types.OptionList )tom_options);if (tom_is_fun_sym_concOption(tomMatch244NameNumberfreshSubject_1)) {{  tom.engine.adt.tomoption.types.OptionList  tomMatch244NameNumber_freshVar_2=tomMatch244NameNumberfreshSubject_1;{  tom.engine.adt.tomoption.types.OptionList  tomMatch244NameNumber_begin_4=tomMatch244NameNumber_freshVar_2;{  tom.engine.adt.tomoption.types.OptionList  tomMatch244NameNumber_end_5=tomMatch244NameNumber_freshVar_2;do {{{  tom.engine.adt.tomoption.types.OptionList  tomMatch244NameNumber_freshVar_3=tomMatch244NameNumber_end_5;if (!(tom_is_empty_concOption_OptionList(tomMatch244NameNumber_freshVar_3))) {if (tom_is_fun_sym_DeclarationToOption(tom_get_head_concOption_OptionList(tomMatch244NameNumber_freshVar_3))) {{  tom.engine.adt.tomdeclaration.types.Declaration  tomMatch244NameNumber_freshVar_39=tom_get_slot_DeclarationToOption_AstDeclaration(tom_get_head_concOption_OptionList(tomMatch244NameNumber_freshVar_3));{  tom.engine.adt.tomdeclaration.types.Declaration  tomMatch244NameNumber_freshVar_0=tomMatch244NameNumber_freshVar_39;{  tom.engine.adt.tomoption.types.OptionList  tomMatch244NameNumber_freshVar_6=tom_get_tail_concOption_OptionList(tomMatch244NameNumber_freshVar_3);if (tom_is_fun_sym_MakeDecl(tomMatch244NameNumber_freshVar_0)) {{  tom.engine.adt.tomtype.types.TomType  tomMatch244NameNumber_freshVar_1=tom_get_slot_MakeDecl_AstType(tomMatch244NameNumber_freshVar_0);if (tom_is_fun_sym_Codomain(tomMatch244NameNumber_freshVar_1)) {if ( true ) {

            Declaration newMake = tomMatch244NameNumber_freshVar_0.setAstType(codomain);
            //System.out.println("newMake: " + newMake);
            newOptions = tom_get_slice_concOption(tomMatch244NameNumber_begin_4,tomMatch244NameNumber_end_5,tom_append_list_concOption(tom_empty_list_concOption(),tom_append_list_concOption(tomMatch244NameNumber_freshVar_6,tom_cons_list_concOption(tom_make_DeclarationToOption(newMake),tom_empty_list_concOption()))));
          }}}}}}}}}}if (tom_is_empty_concOption_OptionList(tomMatch244NameNumber_end_5)) {tomMatch244NameNumber_end_5=tomMatch244NameNumber_begin_4;} else {tomMatch244NameNumber_end_5=tom_get_tail_concOption_OptionList(tomMatch244NameNumber_end_5);}}} while(!(tom_equal_term_OptionList(tomMatch244NameNumber_end_5, tomMatch244NameNumber_begin_4)));}}}}}}

        TomSymbol newSymbol = tom_make_Symbol(tom_name,tom_make_TypesToType(tomMatch245NameNumber_freshVar_4,codomain),tomMatch245NameNumber_freshVar_2,newOptions);
        //System.out.println("newSymbol: " + newSymbol);
        symbolTable.putSymbol(tom_name.getString(),newSymbol);
        return newSymbol;
      }}}}}}}}}}}}}}}}}

    return symbol;
  }

}
