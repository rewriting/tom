package adt;

import aterm.*;
import aterm.pure.*;
import adt.adt.*;
import adt.adt.types.*;
import adt.vas.*;
import adt.vas.types.*;

import java.util.*;
import java.io.*;

public class VasPrettyPrinter {
  private VasFactory vasFactory;

  %include {Vas.tom}
  
  public VasPrettyPrinter(VasFactory vasFactory) {
    this.vasFactory = vasFactory;
  }
  
  private final VasFactory getVasFactory() {
    return vasFactory;
  }
  
  public void print(VasModule module) {
    //System.out.println(module);
    %match(VasModule module) {
      VasModule(VasModuleName(moduleName),
                concSection(Imports(importList),Public(concGrammar(sortList,_*,grammar,_*)))) -> {
        System.out.print("module "+`moduleName+
                         "\nimports "+printImports(`importList)+
                         "\n\npublic"+
                         "\nsorts "+printSorts(`sortList)+
                         "\n\nabstract syntax\n\n"+printGrammar(`grammar));
      }
    }
  }

  private String printImports(ImportList importList) {
    String result = "";
    %match(ImportList importList) {
      concImportedVasModule(_*,Import(VasModuleName(importName)),_*) -> {
        result += `importName+" ";
      }
    }
    return result;
  }

  private String printSorts(Grammar sortList) {
    String result = "";
    %match(Grammar sortList) {
      Sorts(concVasType(_*,VasType(typeName),_*)) -> {
        result += `typeName+" ";
      }
    }
    return result;
  }

  private String printGrammar(Grammar grammar) {
    String result = "";
    %match(Grammar grammar) {
      Grammar(concProduction(_*,Production(prodName,prodDomain,VasType(prodCodomain)),_*)) -> {
        result += `prodName+"("+printFields(`prodDomain)+") -> "+`prodCodomain+"\n";
      }
    }
    return result;
  }

  private String printFields(FieldList prodDomain) {
    String result = "";
    boolean first = true;
    %match(FieldList prodDomain) {
      concField(_*,NamedField(fieldName,VasType(fieldType)),_*) -> {
        if(!first) {
          result += ",";
        } else {
          first = false;
        }
        result += `fieldName+":"+`fieldType;
      }
      concField(_*,StaredField(VasType(fieldType)),_*) -> {
        result += `fieldType+"*";
      }
    }
    return result;
  }
}
