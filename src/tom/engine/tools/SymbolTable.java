/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

package jtom.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jtom.*;
import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.exception.TomRuntimeException;

public class SymbolTable {
  private final static String TYPE_INT       = "int";
  private final static String TYPE_LONG      = "long";
  private final static String TYPE_CHAR      = "char";
  private final static String TYPE_DOUBLE    = "double";
  private final static String TYPE_STRING    = "String";
  private final static String TYPE_BOOL      = "bool";
  private final static String TYPE_UNIVERSAL = "universal";

  private Map mapSymbolName = null;
  private Map mapTypeName = null;
  private ASTFactory astFactory;
  public SymbolTable(ASTFactory astFactory) {
    this.astFactory = astFactory;
  }

  public void init() {
    mapSymbolName = new HashMap();
    mapTypeName = new HashMap();

    if( getServer().getOptionBooleanValue("cCode") ) {
      putType(TYPE_CHAR, ast().makeType(TYPE_CHAR,"char"));
      putType(TYPE_BOOL, ast().makeType(TYPE_BOOL,"int"));
      putType(TYPE_INT, ast().makeType(TYPE_INT,"int"));
      putType(TYPE_LONG, ast().makeType(TYPE_LONG,"long"));
      putType(TYPE_DOUBLE, ast().makeType(TYPE_DOUBLE,"double"));
      putType(TYPE_STRING, ast().makeType(TYPE_STRING,"char*"));
      putType(TYPE_UNIVERSAL, ast().makeType(TYPE_UNIVERSAL,"void*"));
    } else if( getServer().getOptionBooleanValue("jCode") ) {
      putType(TYPE_CHAR, ast().makeType(TYPE_CHAR,"char"));
      putType(TYPE_BOOL, ast().makeType(TYPE_BOOL,"boolean"));
      putType(TYPE_INT, ast().makeType(TYPE_INT,"int"));
      putType(TYPE_LONG, ast().makeType(TYPE_LONG,"long"));
      putType(TYPE_DOUBLE, ast().makeType(TYPE_DOUBLE,"double"));
      putType(TYPE_STRING, ast().makeType(TYPE_STRING,"String"));
      putType(TYPE_UNIVERSAL, ast().makeType(TYPE_UNIVERSAL,"Object"));
    } else if( getServer().getOptionBooleanValue("eCode") ) {
      putType(TYPE_CHAR, ast().makeType(TYPE_CHAR,"CHARACTER"));
      putType(TYPE_BOOL, ast().makeType(TYPE_BOOL,"BOOLEAN"));
      putType(TYPE_INT, ast().makeType(TYPE_INT,"INTEGER"));
      putType(TYPE_LONG, ast().makeType(TYPE_LONG,"INTEGER"));
      putType(TYPE_DOUBLE, ast().makeType(TYPE_DOUBLE,"DOUBLE"));
      putType(TYPE_STRING, ast().makeType(TYPE_STRING,"STRING"));
      putType(TYPE_UNIVERSAL, ast().makeType(TYPE_UNIVERSAL,"ANY"));
    } else if( getServer().getOptionBooleanValue("camlCode") ) { // this is really bad, will need to be improved
      putType(TYPE_CHAR, ast().makeType(TYPE_CHAR,"char"));
      putType(TYPE_BOOL, ast().makeType(TYPE_BOOL,"bool"));
      putType(TYPE_INT, ast().makeType(TYPE_INT,"int"));
      putType(TYPE_LONG, ast().makeType(TYPE_LONG,"long"));
      putType(TYPE_DOUBLE, ast().makeType(TYPE_DOUBLE,"double"));
      putType(TYPE_STRING, ast().makeType(TYPE_STRING,"String"));
      putType(TYPE_UNIVERSAL, ast().makeType(TYPE_UNIVERSAL,"None"));
    }
  }

  private TomServer getServer() {
    return TomServer.getInstance();
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

  public TomSymbol getSymbol(String name) {
    TomSymbol res = (TomSymbol)mapSymbolName.get(name);
    return res;
  }

  public SymbolList getSymbol(TomType type) {
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

  public TomType getBoolType() {
    return getType(TYPE_BOOL);
  }
  
  public TomType getStringType() {
    return getType(TYPE_STRING);
  }

  public TomType getUniversalType() {
    return getType(TYPE_UNIVERSAL);
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

  public boolean isBoolType(String type) {
    return type.equals(TYPE_BOOL);
  }

  public boolean isDoubleType(String type) {
    return type.equals(TYPE_DOUBLE);
  }

  public boolean isBuiltinType(String type) {
    return isIntType(type) || isLongType(type) || isCharType(type) ||
      isStringType(type) || isBoolType(type) || isDoubleType(type);
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
    } else if(isBoolType(type)) {
      return getBoolType();
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
      TomSymbol symbol = getSymbol(name);
      TomEntry entry = tsf().makeTomEntry_Entry(name,symbol);
      list = tsf().makeTomEntryList(entry,list);
    }
    return tsf().makeTomSymbolTable_Table(list);
  }

}
