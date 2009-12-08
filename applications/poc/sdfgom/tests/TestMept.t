package sdfgom;

import java.io.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;
import aterm.pure.PureFactory;
import aterm.pure.SingletonFactory;
import mept.*;
import mept.types.*;
import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;
import sdfgom.MEPTConverter;

public class TestMept {

  private static String readFileAsString(String filePath)
    throws java.io.IOException{
      StringBuffer fileData = new StringBuffer(1000);
      BufferedReader reader = new BufferedReader(
          new FileReader(filePath));
      char[] buf = new char[1024];
      int numRead=0;
      while((numRead=reader.read(buf)) != -1){
        fileData.append(buf, 0, numRead);
      }
      reader.close();
      return fileData.toString();
    }

  public static void main(String[] args) {
    int index;
    //String[] s = new String[10];
    String s = new String();
    if(args.length > 0) {
      //index = 0;
      try{
        s = readFileAsString(args[0]);
      } catch (IOException e) {
        System.err.println("Caught IOException: " 
            + e.getMessage());
      }
    } else {
      //s[0] =
      s =
        "parsetree(appl(prod([cf(opt(layout)),cf(sort(\"Block\")),cf(opt(layout))],sort(\"<START>\"),no-attrs),[appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([lit(\"{\"),cf(opt(layout)),cf(sort(\"BoolList\")),cf(opt(layout)),lit(\"}\")],cf(sort(\"Block\")),attrs([term(cons(\"BoolBlock\"))])),[appl(prod([char-class([123])],lit(\"{\"),no-attrs),[123]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([cf(iter-star(sort(\"Bool\")))],cf(sort(\"BoolList\")),attrs([term(cons(\"list\"))])),[appl(list(cf(iter-star(sort(\"Bool\")))),[appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Bool\")),cf(opt(layout)),lit(\"&\"),cf(opt(layout)),cf(sort(\"Bool\"))],cf(sort(\"Bool\")),attrs([term(cons(\"And\"))])),[appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([38])],lit(\"&\"),no-attrs),[38]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Id\"))],cf(sort(\"Bool\")),attrs([term(cons(\"Id\"))])),[appl(prod([lex(sort(\"Id\"))],cf(sort(\"Id\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"Id\")),attrs([term(cons(\"IdCons\"))])),[appl(list(lex(iter(char-class([range(48,57)])))),[48,48,55])])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([char-class([125])],lit(\"}\"),no-attrs),[125])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[10])])])])]),0)";
      //s[1] = 
      //  "parsetree(appl(prod([cf(opt(layout)),cf(sort(\"Block\")),cf(opt(layout))],sort(\"<START>\"),no-attrs),[appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([lit(\"{\"),cf(opt(layout)),cf(sort(\"BoolList\")),cf(opt(layout)),lit(\"}\")],cf(sort(\"Block\")),attrs([term(cons(\"BoolBlock\"))])),[appl(prod([char-class([123])],lit(\"{\"),no-attrs),[123,amb([999,998,997])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([cf(iter-star(sort(\"Bool\")))],cf(sort(\"BoolList\")),attrs([term(cons(\"list\"))])),[appl(list(cf(iter-star(sort(\"Bool\")))),[appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Bool\")),cf(opt(layout)),lit(\"&\"),cf(opt(layout)),cf(sort(\"Bool\"))],cf(sort(\"Bool\")),attrs([term(cons(\"And\"))])),[appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([38])],lit(\"&\"),no-attrs),[38]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Id\"))],cf(sort(\"Bool\")),attrs([term(cons(\"Id\"))])),[appl(prod([lex(sort(\"Id\"))],cf(sort(\"Id\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"Id\")),attrs([term(cons(\"IdCons\"))])),[appl(list(lex(iter(char-class([range(48,57)])))),[48,48,55])])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([char-class([125])],lit(\"}\"),no-attrs),[125])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[10])])])])]),0)";

      //s[2] =
      //  "parsetree(appl(prod([cf(opt(layout)),cf(sort(\"Exp\")),cf(opt(layout))],sort(\"<START>\"),no-attrs),[appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([cf(sort(\"Exp\")),cf(opt(layout)),lit(\"*\"),cf(opt(layout)),cf(sort(\"Exp\"))],cf(sort(\"Exp\")),attrs([assoc(left),term(cons(\"Mul\"))])),[appl(prod([cf(sort(\"IntConst\"))],cf(sort(\"Exp\")),attrs([term(cons(\"Int\"))])),[appl(prod([lex(sort(\"IntConst\"))],cf(sort(\"IntConst\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"IntConst\")),no-attrs),[appl(list(lex(iter(char-class([range(48,57)])))),[51])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([42])],lit(\"*\"),no-attrs),[42]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"IntConst\"))],cf(sort(\"Exp\")),attrs([term(cons(\"Int\"))])),[appl(prod([lex(sort(\"IntConst\"))],cf(sort(\"IntConst\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"IntConst\")),no-attrs),[appl(list(lex(iter(char-class([range(48,57)])))),[53])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[10])])])])]),0)";

      //s[3] =
      //  "parsetree(appl(prod([cf(opt(layout)),cf(sort(\"Exp\")),cf(opt(layout))],sort(\"<START>\"),no-attrs),[appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([cf(sort(\"Exp\")),cf(opt(layout)),lit(\"-\"),cf(opt(layout)),cf(sort(\"Exp\"))],cf(sort(\"Exp\")),attrs([assoc(left),term(cons(\"Minus\"))])),[appl(prod([cf(sort(\"Exp\")),cf(opt(layout)),lit(\"+\"),cf(opt(layout)),cf(sort(\"Exp\"))],cf(sort(\"Exp\")),attrs([assoc(left),term(cons(\"Plus\"))])),[appl(prod([cf(sort(\"Exp\")),cf(opt(layout)),lit(\"*\"),cf(opt(layout)),cf(sort(\"Exp\"))],cf(sort(\"Exp\")),attrs([assoc(left),term(cons(\"Mul\"))])),[appl(prod([cf(sort(\"IntConst\"))],cf(sort(\"Exp\")),attrs([term(cons(\"Int\"))])),[appl(prod([lex(sort(\"IntConst\"))],cf(sort(\"IntConst\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"IntConst\")),no-attrs),[appl(list(lex(iter(char-class([range(48,57)])))),[51])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([42])],lit(\"*\"),no-attrs),[42]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"IntConst\"))],cf(sort(\"Exp\")),attrs([term(cons(\"Int\"))])),[appl(prod([lex(sort(\"IntConst\"))],cf(sort(\"IntConst\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"IntConst\")),no-attrs),[appl(list(lex(iter(char-class([range(48,57)])))),[53])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([43])],lit(\"+\"),no-attrs),[43]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"IntConst\"))],cf(sort(\"Exp\")),attrs([term(cons(\"Int\"))])),[appl(prod([lex(sort(\"IntConst\"))],cf(sort(\"IntConst\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"IntConst\")),no-attrs),[appl(list(lex(iter(char-class([range(48,57)])))),[52,50])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([45])],lit(\"-\"),no-attrs),[45]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Exp\")),cf(opt(layout)),lit(\"/\"),cf(opt(layout)),cf(sort(\"Exp\"))],cf(sort(\"Exp\")),attrs([assoc(left),term(cons(\"Div\"))])),[appl(prod([cf(sort(\"Exp\")),cf(opt(layout)),lit(\"/\"),cf(opt(layout)),cf(sort(\"Exp\"))],cf(sort(\"Exp\")),attrs([assoc(left),term(cons(\"Div\"))])),[appl(prod([cf(sort(\"Id\"))],cf(sort(\"Exp\")),attrs([term(cons(\"Var\"))])),[appl(prod([lex(sort(\"Id\"))],cf(sort(\"Id\")),no-attrs),[appl(prod([lex(iter(char-class([range(65,90),range(97,122)])))],lex(sort(\"Id\")),no-attrs),[appl(list(lex(iter(char-class([range(65,90),range(97,122)])))),[116,111,116,111])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([47])],lit(\"/\"),no-attrs),[47]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Id\"))],cf(sort(\"Exp\")),attrs([term(cons(\"Var\"))])),[appl(prod([lex(sort(\"Id\"))],cf(sort(\"Id\")),no-attrs),[appl(prod([lex(iter(char-class([range(65,90),range(97,122)])))],lex(sort(\"Id\")),no-attrs),[appl(list(lex(iter(char-class([range(65,90),range(97,122)])))),[115,99,104,116,114,111,117,109,112,102])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([47])],lit(\"/\"),no-attrs),[47]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"IntConst\"))],cf(sort(\"Exp\")),attrs([term(cons(\"Int\"))])),[appl(prod([lex(sort(\"IntConst\"))],cf(sort(\"IntConst\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"IntConst\")),no-attrs),[appl(list(lex(iter(char-class([range(48,57)])))),[56])])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),32])],lex(layout),no-attrs),[10])])])])]),0)";

      //index = 3; // index of the String which will be used

    }

    // Converters which will be used
    IdConverter idConv = new IdConverter();
    MEPTConverter meptConv = new MEPTConverter();

    //Parameters :
    ATermConverter usedConverter = meptConv; // ATermConverter which will be used 

    //ATerm at = SingletonFactory.getInstance().parse(s1.replace('-','_').replaceAll("sort","my_sort"));
    //System.out.println("\ns["+index+"] =\n"+s[index]);
    //ATerm at = SingletonFactory.getInstance().parse(s[index]);
    System.out.println("\ns =\n"+s);
    ATerm at = SingletonFactory.getInstance().parse(s);

    System.out.println("\nat =\n"+at);

    //ParseTree t = ParseTree.fromTerm(at); // if uncommented, itshould not work, the error has to be the same as the one when idConv/defaultConv are used
    ParseTree t = ParseTree.fromTerm(at,usedConverter);
    System.out.println("\nParseTree.fromTerm(at,usedConverter) = t =\n" + t + "\n");

    ATerm newATermMept = t.toATerm();
    System.out.println("\nt.toATerm() = newATermMept =\n" + newATermMept + "\n");

  }
} 
