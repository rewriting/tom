/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

import java.util.*;

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

        private static   tom.engine.adt.tomsignature.types.TomEntryList  tom_append_list_concTomEntry( tom.engine.adt.tomsignature.types.TomEntryList l1,  tom.engine.adt.tomsignature.types.TomEntryList  l2) {     if( l1.isEmptyconcTomEntry() ) {       return l2;     } else if( l2.isEmptyconcTomEntry() ) {       return l1;     } else if(  l1.getTailconcTomEntry() .isEmptyconcTomEntry() ) {       return  tom.engine.adt.tomsignature.types.tomentrylist.ConsconcTomEntry.make( l1.getHeadconcTomEntry() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.tomentrylist.ConsconcTomEntry.make( l1.getHeadconcTomEntry() ,tom_append_list_concTomEntry( l1.getTailconcTomEntry() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.TomEntryList  tom_get_slice_concTomEntry( tom.engine.adt.tomsignature.types.TomEntryList  begin,  tom.engine.adt.tomsignature.types.TomEntryList  end, tom.engine.adt.tomsignature.types.TomEntryList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomEntry()  ||  (end== tom.engine.adt.tomsignature.types.tomentrylist.EmptyconcTomEntry.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.tomentrylist.ConsconcTomEntry.make( begin.getHeadconcTomEntry() ,( tom.engine.adt.tomsignature.types.TomEntryList )tom_get_slice_concTomEntry( begin.getTailconcTomEntry() ,end,tail)) ;   }      private static   tom.engine.adt.tomsignature.types.TomSymbolList  tom_append_list_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbolList l1,  tom.engine.adt.tomsignature.types.TomSymbolList  l2) {     if( l1.isEmptyconcTomSymbol() ) {       return l2;     } else if( l2.isEmptyconcTomSymbol() ) {       return l1;     } else if(  l1.getTailconcTomSymbol() .isEmptyconcTomSymbol() ) {       return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make( l1.getHeadconcTomSymbol() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make( l1.getHeadconcTomSymbol() ,tom_append_list_concTomSymbol( l1.getTailconcTomSymbol() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.TomSymbolList  tom_get_slice_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbolList  begin,  tom.engine.adt.tomsignature.types.TomSymbolList  end, tom.engine.adt.tomsignature.types.TomSymbolList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomSymbol()  ||  (end== tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make( begin.getHeadconcTomSymbol() ,( tom.engine.adt.tomsignature.types.TomSymbolList )tom_get_slice_concTomSymbol( begin.getTailconcTomSymbol() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_append_list_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList l1,  tom.engine.adt.tomtype.types.TypeOptionList  l2) {     if( l1.isEmptyconcTypeOption() ) {       return l2;     } else if( l2.isEmptyconcTypeOption() ) {       return l1;     } else if(  l1.getTailconcTypeOption() .isEmptyconcTypeOption() ) {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,tom_append_list_concTypeOption( l1.getTailconcTypeOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_get_slice_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList  begin,  tom.engine.adt.tomtype.types.TypeOptionList  end, tom.engine.adt.tomtype.types.TypeOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeOption()  ||  (end== tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( begin.getHeadconcTypeOption() ,( tom.engine.adt.tomtype.types.TypeOptionList )tom_get_slice_concTypeOption( begin.getTailconcTypeOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }    

  private final static String TYPE_INT       = "int";
  private final static String TYPE_LONG      = "long";
  private final static String TYPE_FLOAT     = "float";
  private final static String TYPE_CHAR      = "char";
  private final static String TYPE_DOUBLE    = "double";
  private final static String TYPE_STRING    = "String";
  private final static String TYPE_BOOLEAN   = "boolean";
  private final static String TYPE_UNIVERSAL = "universal";
  private final static String TYPE_VOID      = "void";
  private final static String TYPE_INT_ARRAY = "intarray";
  private final static String INT_ARRAY_OP   = "concInt";

  public final static TomType TYPE_UNKNOWN =  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , "unknown type",  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;

  /** associate a symbol to a name */
  private Map<String,TomSymbol> mapSymbolName = null;
  /** associate a type to a name */
  private Map<String,TomType> mapTypeName = null;
  /** store symbols and types that are used */ 
  private Set<KeyEntry> usedKeyEntry = null;

  private boolean cCode = false;
  private boolean jCode = false;
  private boolean camlCode = false;
  private boolean pCode = false;
  private boolean aCode = false;

  public void init(OptionManager optionManager) {
    mapSymbolName = new HashMap<String,TomSymbol>();
    mapTypeName = new HashMap<String,TomType>();
    usedKeyEntry = new HashSet<KeyEntry>();
    mapInliner = new HashMap<String,String>();

    if(((Boolean)optionManager.getOptionValue("cCode")).booleanValue()) {
      cCode = true;
    } else if(((Boolean)optionManager.getOptionValue("jCode")).booleanValue()) {
      jCode = true;
    } else if(((Boolean)optionManager.getOptionValue("camlCode")).booleanValue()) {
      camlCode = true;
    } else if(((Boolean)optionManager.getOptionValue("pCode")).booleanValue()) {
      pCode = true;
    } else if(((Boolean)optionManager.getOptionValue("aCode")).booleanValue()) {
      aCode = true;
    }
  }

  public Map getMapSymbolName() {
    return this.mapSymbolName;
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
    TomSymbol result = mapSymbolName.put(name,astSymbol);
  }

  public TomSymbol getSymbolFromName(String name) {
    return mapSymbolName.get(name);
  }

  public TomSymbolList getSymbolFromType(TomType type) {
    TomSymbolList res =  tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol.make() ;
    for(TomSymbol symbol:mapSymbolName.values()) {
      if(symbol.getTypesToType().getCodomain() == type) {
        res =  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make(symbol,tom_append_list_concTomSymbol(res, tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol.make() )) ;
      }
    }
    return res;
  }

  public void putType(String name, TomType astType) {
    mapTypeName.put(name,astType);
  }

  public Collection<TomType> getUsedTypes() {
    return mapTypeName.values();
  }

  public TomType getType(String name) {
    if (mapTypeName.containsKey(name)) {
      return mapTypeName.get(name);
    }

    if(name.equals(TYPE_INT)) {
      return getIntType();
    }
    return null;
  }

  public boolean isUsedSymbolConstructor(TomSymbol symbol) {    
    return usedKeyEntry.contains( tom.engine.adt.tomsignature.types.keyentry.UsedSymbolConstructor.make(symbol) );
  }

  public boolean isUsedSymbolDestructor(TomSymbol symbol) {
    return usedKeyEntry.contains( tom.engine.adt.tomsignature.types.keyentry.UsedSymbolDestructor.make(symbol) );
  }

  public boolean isUsedSymbolAC(TomSymbol symbol) {
    return usedKeyEntry.contains( tom.engine.adt.tomsignature.types.keyentry.UsedSymbolAC.make(symbol) );
  }

  public boolean isUsedType(TomType type) {
    return usedKeyEntry.contains( tom.engine.adt.tomsignature.types.keyentry.UsedType.make(type) );
  }

  public void setUsedSymbolConstructor(TomSymbol symbol) {
    usedKeyEntry.add( tom.engine.adt.tomsignature.types.keyentry.UsedSymbolConstructor.make(symbol) );
  }

  public void setUsedSymbolDestructor(TomSymbol symbol) {
    usedKeyEntry.add( tom.engine.adt.tomsignature.types.keyentry.UsedSymbolDestructor.make(symbol) );
  }

  public void setUsedSymbolAC(TomSymbol symbol) {
    usedKeyEntry.add( tom.engine.adt.tomsignature.types.keyentry.UsedSymbolAC.make(symbol) );
  }

  public void setUsedType(TomType type) {
    usedKeyEntry.add( tom.engine.adt.tomsignature.types.keyentry.UsedType.make(type) );
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

  public void setUsedSymbolAC(String name) {
    TomSymbol symbol = getSymbolFromName(name);
    if(symbol!=null) {
      setUsedSymbolAC(symbol);
    }
  }

  public void setUsedType(String name) {
    TomType type = getType(name);
    if(type!=null) {
      setUsedType(type);
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

  public boolean isUsedType(String name) {
    TomType type = getType(name);
    if(type!=null) {
      return isUsedType(type);
    }
    return false;
  }

  public TomType getIntType() {
    String type = "int";
    if(aCode) {
      type = "Integer";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_INT,type);
  }

  public TomType getIntArrayType() {
    String type = "int[]";
    if(aCode) {
      type = "array (Positive range <>) of Integer";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_INT_ARRAY,type);
  }

  public TomType getLongType() {
    String type = "long";
    if(aCode) {
      type = "Long_Integer";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_LONG,type);
  }

  public TomType getFloatType() {
    String type = "float";
    if(aCode) {
      type = "Float";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_FLOAT,type);
  }

  public TomType getCharType() {
    String type = "char";
    if(pCode) {
      type = "str";
    } else if(aCode) {
      type = "Character";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_CHAR,type);
  }

  public TomType getDoubleType() {
    String type = "double";
    if(pCode) {
      type = "float";
    } else if(aCode) {
      type = "Long_Float";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_DOUBLE,type);
  }

  public TomType getBooleanType() {
    String type = "boolean";
    if(cCode) {
      type = "int";
    } else if(camlCode) {
      type = "bool";
    } else if(pCode) {
      type = "bool";
    } else if(aCode) {
      type = "Boolean";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_BOOLEAN,type);
  }

  public TomType getStringType() {
    String type = "String";
    if(cCode) {
      type = "char*";
    } else if(pCode) {
      type = "str";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_STRING,type);
  }

  public TomType getUniversalType() {
    String type = "Object";
    if(cCode) {
      type = "void*";
    } else if(camlCode) {
      type = "None";
    } else if(pCode) {
      type = "None";
    } else if(aCode) {
      type = "UnsupportedUniversalType";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_UNIVERSAL,type);
  }

  public TomType getVoidType() {
    String type = "void";
    if(camlCode) {
      type = "unit";
    } else if(pCode) {
      type = "function";
    } else if(aCode) {
      type = "None";
    }
    return ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,TYPE_VOID,type);
  }

  public boolean isIntType(String type) {
    return TYPE_INT.equals(type);
  }

  public boolean isIntArrayType(String type) {
    return TYPE_INT_ARRAY.equals(type);
  }

  public boolean isLongType(String type) {
    return TYPE_LONG.equals(type);
  }

  public boolean isFloatType(String type) {
    return TYPE_FLOAT.equals(type);
  }

  public boolean isCharType(String type) {
    return TYPE_CHAR.equals(type);
  }

  public boolean isStringType(String type) {
    return TYPE_STRING.equals(type);
  }

  public boolean isBooleanType(String type) {
    return TYPE_BOOLEAN.equals(type);
  }

  public boolean isDoubleType(String type) {
    return TYPE_DOUBLE.equals(type);
  }

  public boolean isVoidType(String type) {
    return TYPE_VOID.equals(type);
  }

  public boolean isUnknownType(String type) {
    return  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , type,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) .equals(TYPE_UNKNOWN);
  }

  public String builtinToWrapper(String type) {
    { /* unamed block */{ /* unamed block */if ( true ) {if ( "byte".equals((( String )type)) ) {
 return "java.lang.Byte"; }}}{ /* unamed block */if ( true ) {if ( "short".equals((( String )type)) ) {
 return "java.lang.Short"; }}}{ /* unamed block */if ( true ) {if ( "int".equals((( String )type)) ) {
 return "java.lang.Integer"; }}}{ /* unamed block */if ( true ) {if ( "long".equals((( String )type)) ) {
 return "java.lang.Long"; }}}{ /* unamed block */if ( true ) {if ( "float".equals((( String )type)) ) {
 return "java.lang.Float"; }}}{ /* unamed block */if ( true ) {if ( "double".equals((( String )type)) ) {
 return "java.lang.Double"; }}}{ /* unamed block */if ( true ) {if ( "boolean".equals((( String )type)) ) {
 return "java.lang.Boolean"; }}}{ /* unamed block */if ( true ) {if ( "char".equals((( String )type)) ) {
 return "java.lang.Character"; }}}}

    return type;
  }

  public boolean isBuiltinType(String type) {
    return isIntType(type) || isLongType(type) || isCharType(type) ||
      isStringType(type) || isBooleanType(type) || isDoubleType(type) ||
      isFloatType(type);
  }

  /*
   * %transformation
   */
  public boolean isResolveSymbol(TomSymbol symb) {
    { /* unamed block */{ /* unamed block */if ( (symb instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symb) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch360_1= (( tom.engine.adt.tomsignature.types.TomSymbol )symb).getOptions() ;if ( (((( tom.engine.adt.tomoption.types.OptionList )tomMatch360_1) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tomMatch360_1) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch360_end_7=tomMatch360_1;do {{ /* unamed block */if (!( tomMatch360_end_7.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch360_11= tomMatch360_end_7.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch360_11) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration ) tomMatch360_11.getAstDeclaration() ) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveMakeDecl) ) {
 return true; }}}if ( tomMatch360_end_7.isEmptyconcOption() ) {tomMatch360_end_7=tomMatch360_1;} else {tomMatch360_end_7= tomMatch360_end_7.getTailconcOption() ;}}} while(!( (tomMatch360_end_7==tomMatch360_1) ));}}}}}

    return false;
  }

  public boolean isNumericType(String type) {
    return isIntType(type) || isLongType(type) || isDoubleType(type) || isFloatType(type);
  }

  public boolean isNumericType(TomType type) {    
    { /* unamed block */{ /* unamed block */if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) (( tom.engine.adt.tomtype.types.TomType )type).getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {

        return isNumericType( (( tom.engine.adt.tomtype.types.TomType )type).getTomType() );
      }}}}}

    if (getIntType().equals(type) 
        || getLongType().equals(type) 
        || getFloatType().equals(type)  
        || getDoubleType().equals(type)) {
      return true;
    }
    return false;    
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
    } else if(isFloatType(type)) {
      return getFloatType();
    }
    System.out.println("Not a builtin type: " + type);
    throw new TomRuntimeException("getBuiltinType error on term: " + type);
  }
  /*
     public Iterable<TomType> entryTypeIterable() {
     return mapTypeName.entrySet();
     }
   */
  public Iterable<String> keySymbolIterable() {
    return mapSymbolName.keySet();
  }

  public Iterator<String> keySymbolIterator() {
    Set<String> keys = mapSymbolName.keySet();
    return keys.iterator();
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
    TomEntryList list =  tom.engine.adt.tomsignature.types.tomentrylist.EmptyconcTomEntry.make() ;
    for(String name:mapSymbolName.keySet()) {
      TomSymbol symbol = getSymbolFromName(name);
      TomEntry entry =  tom.engine.adt.tomsignature.types.tomentry.Entry.make(name, symbol) ;
      list =  tom.engine.adt.tomsignature.types.tomentrylist.ConsconcTomEntry.make(entry,tom_append_list_concTomEntry(list, tom.engine.adt.tomsignature.types.tomentrylist.EmptyconcTomEntry.make() )) ;
    }
    return  tom.engine.adt.tomsignature.types.tomsymboltable.Table.make(list) ;
  }

  public TomSymbol updateConstrainedSymbolCodomain(TomSymbol symbol, SymbolTable symbolTable) {
    { /* unamed block */{ /* unamed block */if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch362_2= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom___name= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getAstName() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch362_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch362_8= tomMatch362_2.getCodomain() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch362_8) instanceof tom.engine.adt.tomtype.types.tomtype.Codomain) ) { tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getOptions() ;

        //System.out.println("depend from : " + `opName);
        TomSymbol dependSymbol = symbolTable.getSymbolFromName( tomMatch362_8.getAstName() );
        //System.out.println("1st depend codomain: " + TomBase.getSymbolCodomain(dependSymbol));
        dependSymbol = updateConstrainedSymbolCodomain(dependSymbol,symbolTable);
        TomType codomain = TomBase.getSymbolCodomain(dependSymbol);
        //System.out.println("2nd depend codomain: " + TomBase.getSymbolCodomain(dependSymbol));
        OptionList newOptions = tom___options;
        { /* unamed block */{ /* unamed block */if ( (tom___options instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )tom___options) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tom___options) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch363_end_4=(( tom.engine.adt.tomoption.types.OptionList )tom___options);do {{ /* unamed block */if (!( tomMatch363_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch363_8= tomMatch363_end_4.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch363_8) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) { tom.engine.adt.tomdeclaration.types.Declaration  tomMatch363_7= tomMatch363_8.getAstDeclaration() ;if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch363_7) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) {if ( ((( tom.engine.adt.tomtype.types.TomType ) tomMatch363_7.getAstType() ) instanceof tom.engine.adt.tomtype.types.tomtype.Codomain) ) {

            Declaration newMake = tomMatch363_7.setAstType(codomain);
            //System.out.println("newMake: " + newMake);
            newOptions = tom_append_list_concOption(tom_get_slice_concOption((( tom.engine.adt.tomoption.types.OptionList )tom___options),tomMatch363_end_4, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ),tom_append_list_concOption( tomMatch363_end_4.getTailconcOption() , tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(newMake) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ));
          }}}}if ( tomMatch363_end_4.isEmptyconcOption() ) {tomMatch363_end_4=(( tom.engine.adt.tomoption.types.OptionList )tom___options);} else {tomMatch363_end_4= tomMatch363_end_4.getTailconcOption() ;}}} while(!( (tomMatch363_end_4==(( tom.engine.adt.tomoption.types.OptionList )tom___options)) ));}}}}

        TomSymbol newSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom___name,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tomMatch362_2.getDomain() , codomain) ,  (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getPairNameDeclList() , newOptions) ;
        //System.out.println("newSymbol: " + newSymbol);
        symbolTable.putSymbol(tom___name.getString(),newSymbol);
        return newSymbol;
      }}}}}}

    return symbol;
  }

  /*
     public void checkTomTypes(SymbolTable symbolTable) {
     for (TomType type : mapTypeName.entrySet()) {
     %match(type) {
     Type[TypeOptions=concTypeOption(_*,SubtypeDecl[TomType=supertypeName],_*)] -> {
     if (!mapTypeName.contains(`supertypeName)) {
     TomMessage.error(getLogger(),currentFile(), getLine(),
     TomMessage.typetermNotDefined, 
     supertypeName);
     }
     }
     }
     }
     }
   */

  /*
   * Inlining
   */

  /** associate an inliner to a name */
  private Map<String,String> mapInliner = null;

  private final static String prefixIsFsym = "is_fsym_";
  private final static String prefixGetSlot = "get_slot_";
  private final static String prefixGetHead = "get_head_";
  private final static String prefixGetTail = "get_tail_";
  private final static String prefixGetElementArray = "get_element_array_";
  private final static String prefixGetSizeArray = "get_size_array_";
  private final static String prefixIsEmptyList = "is_empty_list_";
  private final static String prefixIsSort = "is_sort_";
  private final static String prefixMake = "make_";
  private final static String prefixMakeEmptyArray = "make_empty_array_";
  private final static String prefixMakeEmptyList = "make_empty_list_";
  private final static String prefixMakeAddArray = "make_append_";
  private final static String prefixMakeAddList = "make_insert_";
  private final static String prefixEqualTerm = "equal_";

  private void putInliner(String prefix, String opname, String code) {
    mapInliner.put(prefix+opname,code);
  }

  private String getInliner(String prefix, String opname) {
    return mapInliner.get(prefix+opname);
  }

  public void putIsFsym(String opname, String code) {
    //System.out.println("putIsFsym: " + opname + " -> " + code);
    putInliner(prefixIsFsym,opname,code);
  }
  public String getIsFsym(String opname) {
    return getInliner(prefixIsFsym,opname);
  }

  public void putIsSort(String type, String code) {
    //System.out.println("putIsSort: " + type + " -> " + code);
    putInliner(prefixIsSort,type,code);
  }
  public String getIsSort(String type) {
    return getInliner(prefixIsSort,type);
  }

  //the code is generated in the backend
  public void putResolveGetSlot(String opname, String slotname) {
    putInliner(prefixGetSlot,opname+slotname,"");
  }
  public String getResolveGetSlot(String opname, String slotname) {
    return getInliner(prefixGetSlot,opname+slotname);
  }

  public void putGetSlot(String opname, String slotname, String code) {
    putInliner(prefixGetSlot,opname+slotname,code);
  }
  public String getGetSlot(String opname, String slotname) {
    return getInliner(prefixGetSlot,opname+slotname);
  }

  public void putGetHead(String opname, String code) {
    putInliner(prefixGetHead,opname,code);
  }
  public String getGetHead(String opname) {
    return getInliner(prefixGetHead,opname);
  }

  public void putGetTail(String opname, String code) {
    putInliner(prefixGetTail,opname,code);
  }
  public String getGetTail(String opname) {
    return getInliner(prefixGetTail,opname);
  }

  public void putGetElementArray(String opname, String code) {
    putInliner(prefixGetElementArray,opname,code);
  }
  public String getGetElementArray(String opname) {
    return getInliner(prefixGetElementArray,opname);
  }

  public void putGetSizeArray(String opname, String code) {
    //System.out.println("put: " + opname + " -> " + code);
    putInliner(prefixGetSizeArray,opname,code);
  }
  public String getGetSizeArray(String opname) {
    //System.out.println("get: " + opname);
    return getInliner(prefixGetSizeArray,opname);
  }

  public void putIsEmptyList(String opname, String code) {
    putInliner(prefixIsEmptyList,opname,code);
  }
  public String getIsEmptyList(String opname) {
    return getInliner(prefixIsEmptyList,opname);
  }

  public void putMake(String opname, String code) {
    putInliner(prefixMake,opname,code);
  }
  public String getMake(String opname) {
    return getInliner(prefixMake,opname);
  }

  public void putMakeEmptyArray(String opname, String code) {
    putInliner(prefixMakeEmptyArray,opname,code);
  }
  public String getMakeEmptyArray(String opname) {
    return getInliner(prefixMakeEmptyArray,opname);
  }

  public void putMakeEmptyList(String opname, String code) {
    putInliner(prefixMakeEmptyList,opname,code);
  }
  public String getMakeEmptyList(String opname) {
    return getInliner(prefixMakeEmptyList,opname);
  }

  public void putMakeAddArray(String opname, String code) {
    putInliner(prefixMakeAddArray,opname,code);
  }
  public String getMakeAddArray(String opname) {
    return getInliner(prefixMakeAddArray,opname);
  }

  public void putMakeAddList(String opname, String code) {
    putInliner(prefixMakeAddList,opname,code);
  }
  public String getMakeAddList(String opname) {
    return getInliner(prefixMakeAddList,opname);
  }

  public void putEqualTerm(String type, String code) {
    putInliner(prefixEqualTerm,type,code);
  }
  public String getEqualTerm(String type) {
    return getInliner(prefixEqualTerm,type);
  }
  /*
     private static class Inliner {
     public String isfsym;
     public String getslot;
     }
   */
  public String getIntArrayOp() {
    return INT_ARRAY_OP;
  }

}
