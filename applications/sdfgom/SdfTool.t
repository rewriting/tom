package sdfgom;

import java.io.*;
import java.lang.*;
import java.util.*;
import aterm.*;
import aterm.pure.*;

import sdf.*;
import sdf.types.*;

import tom.library.sl.*;
import sdfgom.SDFConverter;

public class SdfTool {
  %include { sl.tom }
  %include { sdf/sdf.tom }
  %include { java/util/types/HashMap.tom }

  public static PureFactory factory = SingletonFactory.getInstance();

  public static String convert(ATerm at) {
    SDFConverter sdfConverter = new SDFConverter();
    Grammars g = Grammars.fromTerm(at,sdfConverter);

    HashMap<String,ArrayList<String>> table = new HashMap<String,ArrayList<String>>();
    String gomGrammar = "";
    try {
      `TopDown(ExtractProduction(table)).visitLight(g);
      for(String sort:table.keySet()) {
        gomGrammar += "\n" + sort + " = ";
        for(String prod:table.get(sort)) {
          gomGrammar += prod;
        }
      }
    } catch(VisitFailure e) {
      System.out.println("failure on: " + g);
    }
    System.out.println(g);

    return gomGrammar;
  }

  %strategy ExtractProduction(table:HashMap) extends Identity() {
    visit Production {
      prod(lhs,my_sort((more_chars|one_char)(rhsSortName)),attrs(_*,term(appl(unquoted("cons"),fun(quoted(consName)))),_*)) -> {
        String prod = "\n| " + `consName.substring(1,`consName.length()-1) + "(";
matchblock: {
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
            prod += `sortName + "*";
            break matchblock;
          }

          listSymbol(_*,label(unquoted(labelName),my_sort((more_chars|one_char)(sortName))),tail*) -> {
            prod += `labelName + ":" + `sortName;
            if(!`tail.isEmptylistSymbol()) {
              prod += ",";
            }
            break matchblock;
          }

        }
            }
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
} 
