/*
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

  %include { ../adt/tomsignature/TomSignature.tom }

  private final static String TYPE_INT       = "int";
  private final static String TYPE_LONG      = "long";
  private final static String TYPE_FLOAT     = "float";
  private final static String TYPE_CHAR      = "char";
  private final static String TYPE_DOUBLE    = "double";
  private final static String TYPE_STRING    = "String";
  private final static String TYPE_BOOLEAN   = "boolean";
  private final static String TYPE_UNIVERSAL = "universal";
  private final static String TYPE_VOID      = "void";

  /** associate a symbol to a name */
  private Map<String,TomSymbol> mapSymbolName = null;
  /** associate a type to a name */
  private Map<String,TomTypeDefinition> mapTypeName = null;
  /** store symbols and types that are used */ 
  private Set<KeyEntry> usedKeyEntry = null;

  /** associate an inliner to a name */
  private Map<String,Inliner> mapInliner = null;

  private boolean cCode = false;
  private boolean jCode = false;
  private boolean camlCode = false;
  private boolean pCode = false;

  public void init(OptionManager optionManager) {
    mapSymbolName = new HashMap<String,TomSymbol>();
    mapTypeName = new HashMap<String,TomTypeDefinition>();
    usedKeyEntry = new HashSet<KeyEntry>();
    mapInliner = new HashMap<String,Inliner>();

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
    TomSymbol result = mapSymbolName.put(name,astSymbol);
  }

  public TomSymbol getSymbolFromName(String name) {
    TomSymbol res = mapSymbolName.get(name);
    return res;
  }

  public TomSymbolList getSymbolFromType(TomType type) {
    TomSymbolList res = `concTomSymbol();
    Iterator<TomSymbol> it = mapSymbolName.values().iterator();
    while(it.hasNext()) {
      TomSymbol symbol = it.next();
      if(symbol.getTypesToType().getCodomain() == type) {
        res = `concTomSymbol(symbol,res*);
      }
    }
    return res;
  }

  public void putTypeDefinition(String name, TomType astType, TomForwardType fwdType) {
    TomTypeDefinition typeDef = `TypeDefinition(astType,fwdType);
    mapTypeName.put(name,typeDef);
  }

  public TomTypeDefinition getTypeDefinition(String name) {
    TomTypeDefinition def = mapTypeName.get(name);
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
    return usedKeyEntry.contains(`UsedSymbolConstructor(symbol));
  }

  public boolean isUsedSymbolDestructor(TomSymbol symbol) {
    return usedKeyEntry.contains(`UsedSymbolDestructor(symbol));
  }

  public boolean isUsedTypeDefinition(TomTypeDefinition type) {
    return usedKeyEntry.contains(`UsedTypeDefinition(type));
  }

  public void setUsedSymbolConstructor(TomSymbol symbol) {
    usedKeyEntry.add(`UsedSymbolConstructor(symbol));
  }

  public void setUsedSymbolDestructor(TomSymbol symbol) {
    usedKeyEntry.add(`UsedSymbolDestructor(symbol));
  }

  public void setUsedTypeDefinition(TomTypeDefinition type) {
    usedKeyEntry.add(`UsedTypeDefinition(type));
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
    return `ASTFactory.makeType(TYPE_INT,"int");
  }

  public TomType getLongType() {
    return `ASTFactory.makeType(TYPE_LONG,"long");
  }

  public TomType getFloatType() {
    return `ASTFactory.makeType(TYPE_FLOAT,"float");
  }

  public TomType getCharType() {
    String type = "char";
    if(pCode) {
      type = "str";
    }
    return `ASTFactory.makeType(TYPE_CHAR,type);
  }

  public TomType getDoubleType() {
    String type = "double";
    if(pCode) {
      type = "float";
    }
    return `ASTFactory.makeType(TYPE_DOUBLE,type);
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
    return `ASTFactory.makeType(TYPE_BOOLEAN,type);
  }

  public TomType getStringType() {
    String type = "String";
    if(pCode) {
      type = "char*";
    } else if(pCode) {
      type = "str";
    } 
    return `ASTFactory.makeType(TYPE_STRING,type);
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
    return `ASTFactory.makeType(TYPE_UNIVERSAL,type);
  }

  public TomType getVoidType() {
    String type = "void";
    if(camlCode) {
      type = "unit";
    } else if(pCode) {
      type = "function";
    }
    return `ASTFactory.makeType(TYPE_VOID,type);
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
  
  public boolean isNumericType(String type) {
    return isIntType(type) || isLongType(type) || isDoubleType(type) || isFloatType(type);
  }
  
  public boolean isNumericType(TomType type) {    
    %match(type){
      TomTypeAlone(str) -> {
        return isNumericType(`str);
      }
    }
    if (type.equals(getIntType()) 
        || type.equals(getLongType()) 
        || type.equals(getFloatType())  
        || type.equals(getDoubleType())) {
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
    } 
    System.out.println("Not a builtin type: " + type);
    throw new TomRuntimeException("getBuiltinType error on term: " + type);
  }

  public Iterator<String> keySymbolIterator() {
    Set<String> keys = mapSymbolName.keySet();
    Iterator<String> it = keys.iterator();
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
    TomEntryList list = `concTomEntry();
    for(String name:mapSymbolName.keySet()) {
      TomSymbol symbol = getSymbolFromName(name);
      TomEntry entry = `Entry(name,symbol);
      list = `concTomEntry(entry,list*);
    }
    return `Table(list);
  }

  public TomSymbol updateConstrainedSymbolCodomain(TomSymbol symbol, SymbolTable symbolTable) {
    %match(TomSymbol symbol) {
      Symbol(name,TypesToType(domain,Codomain(Name(opName))),slots,options) -> {
        //System.out.println("update codomain: " + `name);
        //System.out.println("depend from : " + `opName);
        TomSymbol dependSymbol = symbolTable.getSymbolFromName(`opName);
        //System.out.println("1st depend codomain: " + TomBase.getSymbolCodomain(dependSymbol));
        dependSymbol = updateConstrainedSymbolCodomain(dependSymbol,symbolTable);
        TomType codomain = TomBase.getSymbolCodomain(dependSymbol);
        //System.out.println("2nd depend codomain: " + TomBase.getSymbolCodomain(dependSymbol));
        OptionList newOptions = `options;
        %match(OptionList options) {
          concOption(O1*,DeclarationToOption(m@MakeDecl[AstType=Codomain[]]),O2*) -> {
            Declaration newMake = `m.setAstType(codomain);
            //System.out.println("newMake: " + newMake);
            newOptions = `concOption(O1*,O2*,DeclarationToOption(newMake));
          }
        }
        TomSymbol newSymbol = `Symbol(name,TypesToType(domain,codomain),slots,newOptions);
        //System.out.println("newSymbol: " + newSymbol);
        symbolTable.putSymbol(`name.getString(),newSymbol);
        return newSymbol;
      }
    }
    return symbol;
  }


  public void putSymbol(String opname, String code) {
    Inliner inliner = mapInliner.get(opname);
    if(inliner==null) {
      inliner = new Inliner();
    }
    inliner.isfsym = code;
  }

  public String getIsFsym(String opname) {
    Inliner inliner = mapInliner.get(opname);
    if(inliner!=null) {
      return inliner.isfsym;
    }
    return "";
  }

  private static class Inliner {
    public String isfsym;
  }

}
