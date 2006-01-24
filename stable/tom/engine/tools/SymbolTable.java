/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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

import tom.engine.adt.tomsignature.TomSignatureFactory;
import tom.engine.adt.tomsignature.types.SymbolList;
import tom.engine.adt.tomsignature.types.TomEntry;
import tom.engine.adt.tomsignature.types.TomEntryList;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.tomsignature.types.TomSymbolTable;
import tom.engine.adt.tomsignature.types.TomType;
import tom.engine.exception.TomRuntimeException;
import tom.platform.OptionManager;

public class SymbolTable {
  private final static String TYPE_INT       = "int";
  private final static String TYPE_LONG      = "long";
  private final static String TYPE_CHAR      = "char";
  private final static String TYPE_DOUBLE    = "double";
  private final static String TYPE_STRING    = "String";
  private final static String TYPE_BOOLEAN   = "boolean";
  private final static String TYPE_UNIVERSAL = "universal";
  private final static String TYPE_VOID      = "void";

  private Map mapSymbolName = null;
  private Map mapTypeName = null;
  private ASTFactory astFactory;
  public SymbolTable(ASTFactory astFactory) {
    this.astFactory = astFactory;
  }

  public void init(OptionManager optionManager) {
    mapSymbolName = new HashMap();
    mapTypeName = new HashMap();

    if( ((Boolean)optionManager.getOptionValue("cCode")).booleanValue() ) {
      putType(TYPE_CHAR, ast().makeType(TYPE_CHAR,"char"));
      putType(TYPE_BOOLEAN, ast().makeType(TYPE_BOOLEAN,"int"));
      putType(TYPE_INT, ast().makeType(TYPE_INT,"int"));
      putType(TYPE_LONG, ast().makeType(TYPE_LONG,"long"));
      putType(TYPE_DOUBLE, ast().makeType(TYPE_DOUBLE,"double"));
      putType(TYPE_STRING, ast().makeType(TYPE_STRING,"char*"));
      putType(TYPE_UNIVERSAL, ast().makeType(TYPE_UNIVERSAL,"void*"));
      putType(TYPE_VOID, ast().makeType(TYPE_VOID,"void"));
    } else if( ((Boolean)optionManager.getOptionValue("jCode")).booleanValue() ) {
      putType(TYPE_CHAR, ast().makeType(TYPE_CHAR,"char"));
      putType(TYPE_BOOLEAN, ast().makeType(TYPE_BOOLEAN,"boolean"));
      putType(TYPE_INT, ast().makeType(TYPE_INT,"int"));
      putType(TYPE_LONG, ast().makeType(TYPE_LONG,"long"));
      putType(TYPE_DOUBLE, ast().makeType(TYPE_DOUBLE,"double"));
      putType(TYPE_STRING, ast().makeType(TYPE_STRING,"String"));
      putType(TYPE_UNIVERSAL, ast().makeType(TYPE_UNIVERSAL,"Object"));
      putType(TYPE_VOID, ast().makeType(TYPE_VOID,"void"));
    } else if( ((Boolean)optionManager.getOptionValue("camlCode")).booleanValue() ) { // this is really bad, will need to be improved
      putType(TYPE_CHAR, ast().makeType(TYPE_CHAR,"char"));
      putType(TYPE_BOOLEAN, ast().makeType(TYPE_BOOLEAN,"bool"));
      putType(TYPE_INT, ast().makeType(TYPE_INT,"int"));
      putType(TYPE_LONG, ast().makeType(TYPE_LONG,"long"));
      putType(TYPE_DOUBLE, ast().makeType(TYPE_DOUBLE,"double"));
      putType(TYPE_STRING, ast().makeType(TYPE_STRING,"String"));
      putType(TYPE_UNIVERSAL, ast().makeType(TYPE_UNIVERSAL,"None"));
      putType(TYPE_VOID, ast().makeType(TYPE_VOID,"unit"));
    }
  }

  public void regenerateFromTerm(TomSymbolTable symbTable) {
    TomEntryList list =  symbTable.getEntryList();
    while(!list.isEmpty()) {
      TomEntry symb = list.getHead();
      putSymbol(symb.getStrName(), symb.getAstSymbol());
      list = list.getTail();
    }
  }
  protected ASTFactory ast() {
    return astFactory;
  }

  protected TomSignatureFactory tsf() {
    return ast().tsf();
  }

  public void putSymbol(String name, TomSymbol astSymbol) {
    TomSymbol result = (TomSymbol) mapSymbolName.put(name,astSymbol);
  }

  public void putType(String name, TomType astType) {
    TomType result = (TomType) mapTypeName.put(name,astType);
  }

  public TomSymbol getSymbolFromName(String name) {
    TomSymbol res = (TomSymbol)mapSymbolName.get(name);
    return res;
  }

  public SymbolList getSymbolFromType(TomType type) {
    SymbolList res = tsf().makeSymbolList();
    Iterator it = mapSymbolName.values().iterator();
    while(it.hasNext()) {
      TomSymbol symbol = (TomSymbol)it.next();
      if(symbol.getTypesToType().getCodomain() == type) {
        res = tsf().makeSymbolList(symbol,res);
      }
    }
    return res;
  }

  public TomType getType(String name) {
    TomType res = (TomType)mapTypeName.get(name);
    return res;
  }

  public TomType getIntType() {
    return getType(TYPE_INT);
  }

  public TomType getLongType() {
    return getType(TYPE_LONG);
  }

  public TomType getCharType() {
    return getType(TYPE_CHAR);
  }

  public TomType getDoubleType() {
    return getType(TYPE_DOUBLE);
  }

  public TomType getBooleanType() {
    return getType(TYPE_BOOLEAN);
  }
  
  public TomType getStringType() {
    return getType(TYPE_STRING);
  }

  public TomType getUniversalType() {
    return getType(TYPE_UNIVERSAL);
  }

  public TomType getVoidType() {
    return getType(TYPE_VOID);
  }

  public boolean isIntType(String type) {
    return type.equals(TYPE_INT);
  }

  public boolean isLongType(String type) {
    return type.equals(TYPE_LONG);
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
    while(!list.isEmpty()) {
      TomEntry entry = list.getHead();
      putSymbol(entry.getStrName(),entry.getAstSymbol());
      list = list.getTail();
    }
  }

  public TomSymbolTable toTerm() {
    TomEntryList list = tsf().makeTomEntryList();
    Iterator it = keySymbolIterator();
    while(it.hasNext()) {
      String name = (String)it.next();
      TomSymbol symbol = getSymbolFromName(name);
      TomEntry entry = tsf().makeTomEntry_Entry(name,symbol);
      list = tsf().makeTomEntryList(entry,list);
    }
    return tsf().makeTomSymbolTable_Table(list);
  }

} // class SymbolTable
