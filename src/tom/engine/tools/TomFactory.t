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

import jtom.*;
import java.util.*;
import jtom.adt.*;
import jtom.xml.*;
import aterm.ATerm;

public class TomFactory extends TomBase {

  // ------------------------------------------------------------
  %include { ../adt/TomSignature.tom }
// ------------------------------------------------------------

  public TomFactory(jtom.TomEnvironment environment) {
    super(environment);
  }
  
  public String encodeXMLString(SymbolTable symbolTable, String name) {
    name = "\"" + name + "\"";
    ast().makeStringSymbol(symbolTable,name, new LinkedList());
    return name;
  }

  public TomList metaEncodeTermList(SymbolTable symbolTable,TomList list) {
    %match(TomList list) {
      emptyTomList() -> { return `emptyTomList();}
      manyTomList(head,tail) -> {
        return `manyTomList(metaEncodeXMLAppl(symbolTable,head),
                            metaEncodeTermList(symbolTable,tail));
      }
    }
    return list;
  }

  public TomTerm encodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * encode a String into a quoted-string
       * Appl(...,Name("string"),...) becomes
       * Appl(...,Name("\"string\""),...)
       */
    %match(TomTerm term) {
      Appl[astName=Name(name)] -> {
        String tomName = encodeXMLString(symbolTable,name);
        term = term.setAstName(`Name(tomName));
          //System.out.println("encodeXMLAppl = " + term);
      }
    }
    return term;
  }

  public TomTerm metaEncodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * meta-encode a String into a TextNode
       * Appl(...,Name("\"string\""),...) becomes
       * Appl(...,Name("TextNode"),[Appl(...,Name("\"string\""),...)],...)
       */
    System.out.println("metaEncode: " + term);
    %match(TomTerm term) {
      Appl[astName=Name(tomName)] -> {
        System.out.println("tomName = " + tomName);
        TomSymbol tomSymbol = symbolTable.getSymbol(tomName);
        if(tomSymbol != null) {
          TomType type = tomSymbol.getTypesToType().getCodomain();
          System.out.println("type = " + type);
          %match(TomType type) {
            TomTypeAlone("String") -> {
              Option info = ast().makeOriginTracking(Constants.TEXT_NODE,"-1","??");
              term = `Appl( ast().makeOption(info),
                            Name(Constants.TEXT_NODE),concTomTerm(term));
              System.out.println("metaEncodeXmlAppl = " + term);
            }
          }
        }
      }
    }
    return term;
  }

  
}
