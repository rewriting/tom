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

import jtom.adt.TomEntry;
import jtom.adt.TomEntryList;
import jtom.adt.TomSignatureFactory;
import jtom.adt.TomSymbol;
import jtom.adt.TomSymbolTable;
import jtom.adt.TomTerm;
import jtom.adt.TomType;

public class SymbolTable {
  private Map mapSymbolName = new HashMap();
  private Map mapTypeName = new HashMap();
  private ASTFactory astFactory;
  public SymbolTable(ASTFactory astFactory) {
    this.astFactory = astFactory;
    init();
  }
  private void init() {
    if(Flags.cCode) {
      putType("bool", ast().makeType("bool","int"));
      putType("int", ast().makeType("int","int"));
      putType("universal", ast().makeType("universal","void*"));
    } else if(Flags.jCode) {
      putType("bool", ast().makeType("bool","boolean"));
      putType("int", ast().makeType("int","int"));
      putType("universal", ast().makeType("universal","Object"));
    } else if(Flags.eCode) {
      putType("bool", ast().makeType("bool","BOOLEAN"));
      putType("int", ast().makeType("int","INTEGER"));
      putType("universal", ast().makeType("universal","ANY"));
    }
  }

  public void regenerateFromTerm(TomSymbolTable symbTable) {
    TomEntryList list =  symbTable.getEntryList();
    while(!list.isEmptyEntryList()) {
      TomEntry symb = list.getHeadEntryList();
      putSymbol(symb.getStrName(), symb.getAstSymbol());
      list = list.getTailEntryList();
    }
  }
  protected ASTFactory ast() {
    return astFactory;
  }

  protected TomSignatureFactory tsf() {
    return ast().tsf();
  }

  private void put(Map map, String name, TomTerm term) {
    TomTerm result = (TomTerm) map.put(name,term);
    if(result != null && result == term) {
      System.out.println("Warning: multiple definition of '" + name + "'");
    }
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
    while(!list.isEmptyEntryList()) {
      TomEntry entry = list.getHeadEntryList();
      putSymbol(entry.getStrName(),entry.getAstSymbol());
      list = list.getTailEntryList();
    }
  }

  public TomSymbolTable toTerm() {
    TomEntryList list = tsf().makeTomEntryList_EmptyEntryList();
    Iterator it = keySymbolIterator();
    while(it.hasNext()) {
      String name = (String)it.next();
      TomSymbol symbol = getSymbol(name);
      TomEntry entry = tsf().makeTomEntry_Entry(name,symbol);
      list = tsf().makeTomEntryList_ConsEntryList(entry,list);
    }
    return tsf().makeTomSymbolTable_Table(list);
  }

}
