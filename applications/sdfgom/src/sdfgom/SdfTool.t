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
    SdfConverter sdfConverter = new SdfConverter();
    //System.out.println("at = " + at);
    Definition definition = Definition.fromTerm(at,sdfConverter);
    HashMap<String,ArrayList<String>> table = new HashMap<String,ArrayList<String>>();
    Module firstModule = null;
    %match(definition) {
      definition(modulelist(_*,module,_*)) -> {
        if(firstModule==null) {
          firstModule = `module;
        }
        try {
          `TopDown(ExtractProduction(table)).visitLight(`module);
        } catch(VisitFailure e) {
          throw new RuntimeException("failure on: " + `module);
        }
      }
    }

    StringBuffer sb = new StringBuffer();
    %match(firstModule) {
      my_module(unparameterized(leaf(moduleName)),imports,_) -> {
        //my_module(unparameterized(path(...)),imports,_) -> 
        String importNames = "";
        sb.append("module " + `moduleName + "\n");
        %match(imports) {
          listImpSection(_*,
              my_imports(listImports(_*,module_name(unparameterized(path(importName))),_*)),
              _*) -> {
            importNames += `importName + " ";
          }
        }
        sb.append("imports String " + `importNames + "\n");
        sb.append("abstract syntax\n");
      }
    }
    for(String sort:table.keySet()) {
      sb.append("\n" + renameIntoGomIdentifier(sort) + " = ");
      for(String prod:table.get(sort)) {
        sb.append(prod);
      }
    }
    return sb.toString();
  }

  %strategy ExtractProduction(table:HashMap) extends Identity() {
    visit Production {
      //prod(lhs,_,!attrs(_*,term(appl(unquoted("cons"),_)),_*)) -> {
      //        throw new RuntimeException("constructor name is missing: " + `lhs);
      //}

      subject@prod(lhs,my_sort((more_chars|one_char)(rhsSortName)),attribute) -> {
        String prod = "";
        String constructorName = null;
        %match(attribute) {
          attrs(_*,(reject|bracket)(),_*) -> {
            // ignore reject and bracket
            return `subject;
          }

          attrs(_*,term(appl(unquoted("cons"),listATerm(fun(quoted(consName))))),_*) -> {
            prod += "\n| " + renameIntoGomIdentifier(removeQuote(`consName)) + "(";
            constructorName = `consName;
          }
        }

        /*
         * detects productions that cannot be translated into Gom
         * Foo "," {Goo}* -> Bar for instance 
         */
        %match {
          listSymbol(_*,iter_sym,_*) << lhs
            && 
            (          (iter|iter_sep|iter_star|iter_star_sep)[]  << iter_sym 
                       || label(_,(iter|iter_sep|iter_star|iter_star_sep)[]) << iter_sym)
            &&
            (listSymbol(_*,label(_,my_sort[]),_*) << lhs || listSymbol(_*,my_sort[],_*) << lhs)
            -> { 
              throw new RuntimeException("too many sorts in a list-operator: " + ((constructorName!=null)?constructorName:`attribute.toString()));
            }

          // no attributes, thus no constructor name
          no_attrs() << attribute -> {
            throw new RuntimeException("constructor name is missing: " + `subject); 
          }

          // error if there is no constructore name for a list operator
          !attrs(_*,term(appl(unquoted("cons"),listATerm(fun(quoted(_))))),_*) << attribute 
            -> {
              throw new RuntimeException("constructor name is missing: " + `subject);
            }
        }

//DEBUG
        if(constructorName==null) {
System.out.println("*** " + `subject);
        }
// TODO : do not extract under context-free-priorities

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

          listSymbol(_*,label(_,(iter|iter_sep|iter_star|iter_star_sep)[Symbol=my_sort((more_chars|one_char)(sortName))]),_*) -> {
            prod += renameIntoGomIdentifier(`sortName) + "*";
            break matchblock;
          }

          listSymbol(_*,(iter|iter_sep|iter_star|iter_star_sep)[Symbol=my_sort((more_chars|one_char)(sortName))],_*) -> {
            prod += renameIntoGomIdentifier(`sortName) + "*";
            break matchblock;
          }

          listSymbol(_*,label(quoteOrUnquotedLabel,my_sort((more_chars|one_char)(sortName))),_*) -> {
            if(!firstLabel) {
              prod += ",";
            }
            %match(quoteOrUnquotedLabel) {
              quoted(labelName) -> { prod += renameIntoGomIdentifier(removeQuote(`labelName)); }
              unquoted(labelName) -> { prod += renameIntoGomIdentifier(`labelName); 
              }
            }
            prod += ":" + renameIntoGomIdentifier(`sortName);
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
        if(prod.length()>0) {
          prod += ")";
        }
        ArrayList listOfEntries = (ArrayList)table.get(`rhsSortName);
        if(listOfEntries==null) {
          listOfEntries = new ArrayList();
        }
        listOfEntries.add(prod);
        table.put(`rhsSortName,listOfEntries);
      }
    }
  }

  private static String removeQuote(String name) {
      return name.substring(1,name.length()-1);
  }

  private static String renameIntoGomIdentifier(String idname) {
      String res = idname.replaceAll("-","_");

      return res;
  }
} 
