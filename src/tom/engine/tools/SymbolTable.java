/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jtom.adt.*;

public class SymbolTable {
  private boolean cCode = false, jCode = true, eCode =false;
  private Map mapSymbolName = new HashMap();
  private Map mapTypeName = new HashMap();
  private ASTFactory astFactory;
  public SymbolTable(ASTFactory astFactory, boolean cCode, boolean jCode, boolean eCode) {
    this.astFactory = astFactory;
    this.cCode = cCode;
	this.jCode = jCode;
	this.eCode = eCode;
    init();
  }
  private void init() {
    if(cCode) {
      putType("bool", ast().makeType("bool","int"));
      putType("int", ast().makeType("int","int"));
      putType("double", ast().makeType("double","double"));
      putType("String", ast().makeType("String","char*"));
      putType("universal", ast().makeType("universal","void*"));
    } else if(jCode) {
      putType("bool", ast().makeType("bool","boolean"));
      putType("int", ast().makeType("int","int"));
      putType("double", ast().makeType("double","double"));
      putType("String", ast().makeType("String","String"));
      putType("universal", ast().makeType("universal","Object"));
    } else if(eCode) {
      putType("bool", ast().makeType("bool","BOOLEAN"));
      putType("int", ast().makeType("int","INTEGER"));
      putType("double", ast().makeType("double","DOUBLE"));
      putType("String", ast().makeType("String","STRING"));
      putType("universal", ast().makeType("universal","ANY"));
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

  public TomSymbol getSymbol(String name) {
    TomSymbol res = (TomSymbol)mapSymbolName.get(name);
    return res;
  }

  public TomType getType(String name) {
    TomType res = (TomType)mapTypeName.get(name);
    return res;
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
