package sdfgom;

import java.io.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import aterm.*;
import aterm.pure.PureFactory;
import aterm.pure.SingletonFactory;

import bool.*;
import bool.types.*;
import mept.*;
import mept.types.*;

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;
import tom.library.sl.*;
import sdfgom.MEPTConverter;

public class ImplodeMept {
  %include { sl.tom }
  %include { mept/Mept.tom }
  %include { bool/Bool.tom }
  private static String readFileAsString(String filePath)
    throws java.io.IOException{
      StringBuffer fileData = new StringBuffer(1000);
      BufferedReader reader = new BufferedReader(
          new FileReader(filePath));
      char[] buf = new char[1024];
      int numRead=0;
      while((numRead=reader.read(buf)) != -1) {
        fileData.append(buf, 0, numRead);
      }
      reader.close();
      return fileData.toString();
    }

  public static PureFactory factory = SingletonFactory.getInstance();

  public static void main(String[] args) {
    int index;
    String s = new String();
    if(args.length > 0) {
      try{
        s = readFileAsString(args[0]);
      } catch (IOException e) {
        System.err.println("Caught IOException: " 
            + e.getMessage());
      }
    } else {
      s = "";
    }

    // Converters which will be used
    IdConverter idConv = new IdConverter();
    MEPTConverter meptConv = new MEPTConverter();

    //Parameters :
    ATermConverter usedConverter = meptConv; // ATermConverter which will be used 
    ATerm at = SingletonFactory.getInstance().parse(s);

    //System.out.println("\nat =\n"+at);

    ParseTree t = ParseTree.fromTerm(at,usedConverter);
    //System.out.println("\nParseTree.fromTerm(at,usedConverter) = t =\n" + t + "\n");

    try {
      ParseTree t2 = `BottomUp(RemoveLayout()).visitLight(t);
      //System.out.println("t2 = " + t2);
      ParseTree t3 = `BottomUp(RemoveNonConstructor()).visitLight(t2);
      //System.out.println("t3 = " + t3);
      ParseTree t1 = `TopDown(RemoveStartNode()).visitLight(t3);
      System.out.println("t1 = " + t1);

      ATerm aterm = PTtoAST(t1);
      System.out.println("aterm = " + aterm);
      Block b = Block.fromTerm(aterm);
      System.out.println("bool = " + b);


    } catch(VisitFailure e) {
      System.out.println("failure on " + t);
    }

  }

  public static ATerm PTtoAST(ParseTree pt) {
    %match(pt) {
      parsetree(top,_) -> { return `PTtoAST(top); }
    }
    throw new RuntimeException("should not be there: " + pt);
  }

  public static ATerm PTtoAST(Tree pt) {
    %match(pt) {
      /*
      appl(prod[Attributes=attrs(listAttr(_*,cons(opname),_*))],args) -> { 
        ATermList newArgs = (ATermList) PTtoAST(`args);
        System.out.println("newArgs = " + newArgs);
        int arity=newArgs.getLength();
        AFun fun = factory.makeAFun(`opname,arity,false);
        ATerm res = factory.makeApplList(fun,newArgs);
        return res;
      }
      appl(list(_),args) -> {
        ATermList newArgs = (ATermList) PTtoAST(`args);
        return newArgs;
      }
      */
      appl(prod[Attributes=attrs(listAttr(_*,cons(opname),_*))],arglist) -> { 
        ATermList newArgs = factory.makeList();
        Args args = `arglist;
        while(!args.isEmptylistTree()) {
          Tree t = args.getHeadlistTree();
matchblock: {
          %match(t) {
            // special case for list operators
            appl(list(lex(_)),args2) -> {
              String newArgs2 = PTtoString(`args2);
              newArgs = newArgs.append(factory.makeAppl(factory.makeAFun(newArgs2,0,true)));
              break matchblock;
            }
            appl(list(!lex(_)),args2) -> {
              ATermList newArgs2 = (ATermList) PTtoAST(`args2);
              newArgs = newArgs.concat(newArgs2);
              break matchblock;
            }
            appl(prod[Attributes=no_attrs()],args2) -> {
              ATermList newArgs2 = (ATermList) PTtoAST(`args2);
              newArgs = newArgs.concat(newArgs2);
              break matchblock;
            }

            _ -> {
              // default case
              newArgs = newArgs.append(PTtoAST(t));
            }
          }
          }
          args = args.getTaillistTree();
        }

        System.out.println("newArgs = " + newArgs);
        int arity=newArgs.getLength();
        AFun fun = factory.makeAFun(`opname,arity,false);
        ATerm res = factory.makeApplList(fun,newArgs);
        return res;
      }
    }
    throw new RuntimeException("should not be there: " + pt);
  }

  public static String PTtoString(Args args) {
    String res = "";
    %match(args) {
      listTree(_*,my_char(value),_*) -> {
        char c = (char)`value;
        res += c;
      }
    }
    return res;
  }

  public static ATerm PTtoAST(Args args) {
    ATermList list = factory.makeList();
    %match(args) {
      listTree(_*,e,_*) -> {
        list = list.append(PTtoAST(`e));
      }
    }
    return list;
  }

  %strategy RemoveStartNode() extends Identity() {
    visit Tree {
      appl(prod(listSymbol(_*),my_sort("<START>"),no_attrs()),listTree(x)) -> x
    }
  }

  %strategy RemoveLayout() extends Identity() {
    visit Tree {
      appl(prod(listSymbol(cf(layout())),cf(opt(layout())),no_attrs()),_) -> removedTree()
    }
  }

  %strategy RemoveNonConstructor() extends Identity() {
    visit Tree {
      appl(prod[Attributes=no_attrs()],args) -> {
        try {
          `OnceTopDown(ContainsConstructor()).visitLight(`args);
          // do nothing 
        } catch(VisitFailure e) {
          return `removedTree();
        }
      }
    }
  }

  %strategy ContainsConstructor() extends Fail() {
    visit Tree {
      x@appl(prod[Attributes=attrs(listAttr(_*,cons(_),_*))],_) -> x
    }
  }


} 
