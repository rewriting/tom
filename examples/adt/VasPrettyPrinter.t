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
  
  public void print(VasModule module, String path) {
    try {
      %match(VasModule module) {
        VasModule(VasModuleName(moduleName),
                  concSection(Imports(importList),Public(concGrammar(sortList,_*,grammar,_*)))) -> {
          PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path+`moduleName+".vas")));
          String result = "module "+`moduleName+
            "\nimports "+printImports(`importList)+
            "\n\npublic"+
            "\nsorts "+printSorts(`sortList)+
            "\n\nabstract syntax\n\n"+printGrammar(`grammar);
          System.out.println(result);
          out.write(result,0,result.length());
          out.flush();
          out.close();
        }
      }
    } catch(IOException e) {
      System.err.println("IO Erreur : " + e);
    } catch(Exception ee) {
      System.err.println("Erreur : " + ee);
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
