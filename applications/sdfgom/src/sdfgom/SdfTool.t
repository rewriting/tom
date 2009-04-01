package sdfgom;

import java.io.*;
import java.lang.*;
import java.util.*;
import aterm.*;
import aterm.pure.*;

import sdfgom.sdf.*;
import sdfgom.sdf.types.*;

import tom.library.sl.*;
import sdfgom.converter.SdfConverter;

public class SdfTool {
  %include { sl.tom }
  %include { sdfgom/sdf/sdf.tom }
  %include { java/util/types/HashMap.tom }

  public static PureFactory factory = SingletonFactory.getInstance();

  public static String convertFromDefinition(ATerm at) {
    StringBuffer sb = new StringBuffer();
    SdfConverter sdfConverter = new SdfConverter();
    //System.out.println("at = " + at);
    Definition definition = Definition.fromTerm(at,sdfConverter);
    %match(definition) {
      definition(modulelist(_*,module,_*)) -> {
        String res = convertFromModule(`module);
        sb.append(res);
      }
    }
    return sb.toString();
  }

  public static String convertFromModule(Module module) {
    //System.out.println("module = " + module);
    HashMap<String,ArrayList<String>> table = new HashMap<String,ArrayList<String>>();
    String gomGrammar = "";
    try {
      // extract the module name
      %match(module) {
        my_module(unparameterized(leaf(moduleName)),imports,_) -> {
          String importNames = "";
          gomGrammar += "module " + `moduleName + "\n";
          %match(imports) {
            listImpSection(_*,
                my_imports(listImports(_*,module_name(unparameterized(path(importName))),_*)),
                _*) -> {
              importNames += `importName + " ";
            }
          }
          gomGrammar += "imports String " + `importNames + "\n";
          gomGrammar += "abstract syntax\n";
        }
      }
      // build the signature
      `TopDown(ExtractProduction(table)).visitLight(module);
      for(String sort:table.keySet()) {
        gomGrammar += "\n" + renameIntoGomIdentifier(sort) + " = ";
        for(String prod:table.get(sort)) {
          gomGrammar += prod;
        }
      }
    } catch(VisitFailure e) {
      throw new RuntimeException("failure on: " + module);
    }
    System.out.println(module);

    return gomGrammar;
  }

  %strategy ExtractProduction(table:HashMap) extends Identity() {
    visit Production {
      prod(lhs,my_sort((more_chars|one_char)(rhsSortName)),attrs(_*,term(appl(unquoted("cons"),listATerm(fun(quoted(consName))))),_*)) -> {
        String prod = "\n| " + renameIntoGomIdentifier(`consName.substring(1,`consName.length()-1)) + "(";

        /*
         * detects productions that cannot be translated into Gom
         * Foo "," {Goo}* -> Bar for instance 
         */
        %match(lhs) {
          listSymbol(_*,iter_sym,_*) 
            && 
            (          (iter|iter_sep|iter_star|iter_star_sep)[]  << iter_sym 
            || label(_,(iter|iter_sep|iter_star|iter_star_sep)[]) << iter_sym)
            &&
            (  listSymbol(_*,label(_,my_sort[]),_*) << lhs
            || listSymbol(_*,my_sort[],_*) << lhs)
            -> {
              throw new RuntimeException("cannot translate constructor: " + `consName);
            }
        }


matchblock: {
              //System.out.println("lhs = " + `lhs);



              boolean firstLabel = true;
        %match(lhs) {
          //listSymbol(_*,my_sort(more_chars(sortName)),tail*) -> INVENT a name

          listSymbol(_*,char_class[],_*) -> {
            prod += "String:String";
            break matchblock;
          }
          listSymbol(_*,(iter|iter_star)(char_class[]),_*) -> {
            prod += "String:String";
            break matchblock;
          }

          listSymbol(_*,(iter|iter_sep|iter_star|iter_star_sep)[Symbol=my_sort((more_chars|one_char)(sortName))],_*) -> {
            prod += renameIntoGomIdentifier(`sortName) + "*";
            break matchblock;
          }

          listSymbol(_*,label((quoted|unquoted)(labelName),my_sort((more_chars|one_char)(sortName))),_*) -> {
            if(!firstLabel) {
              prod += ",";
            }
            prod += renameIntoGomIdentifier(`labelName) + ":" + renameIntoGomIdentifier(`sortName);
            firstLabel = false;
          }
          listSymbol(_*,my_sort((more_chars|one_char)(sortName)),_*) -> {
            // we invent a name for the missing label
            if(!firstLabel) {
              prod += ",";
            }
            prod += renameIntoGomIdentifier(`sortName) + ":" + renameIntoGomIdentifier(`sortName);
            firstLabel = false;

          }

        }
            } // end matchblock
        prod += ")";
        ArrayList listOfEntries = (ArrayList)table.get(`rhsSortName);
        if(listOfEntries==null) {
          listOfEntries = new ArrayList();
        }
        listOfEntries.add(prod);
        table.put(`rhsSortName,listOfEntries);
      }
    }
  }

  private static String renameIntoGomIdentifier(String idname) {
      String res = idname.replaceAll("-","_");

      return res;
  }
} 
