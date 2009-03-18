package sdfgom;

import java.io.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import aterm.*;
import aterm.pure.PureFactory;
import aterm.pure.SingletonFactory;

import sdfgom.mept.*;
import sdfgom.mept.types.*;

import tom.library.sl.*;
import sdfgom.converter.MeptConverter;

public class ImplodeMept {
  %include { sl.tom }
  %include { sdfgom/mept/mept.tom }

  public static PureFactory factory = SingletonFactory.getInstance();

  public ATerm implode(ATerm at) {
    MeptConverter meptConverter = new MeptConverter();

    ParseTree pt = ParseTree.fromTerm(at,meptConverter);
    //System.out.println("\npt =\n"+pt);

    try {
      ParseTree t2 = `BottomUp(RemoveLayout()).visitLight(pt);
      ParseTree t3 = `BottomUp(RemoveNonConstructor()).visitLight(t2);
      ParseTree t1 = `TopDown(RemoveStartNode()).visitLight(t3);

      ATerm aterm = PTtoAST(t1);
      //System.out.println("aterm = " + aterm);
      return aterm;
    } catch(VisitFailure e) {
      System.out.println("failure on " + at);
    }
    return at;
  }

  public static ATerm PTtoAST(ParseTree pt) {
    %match(pt) {
      parsetree(top,_) -> { return `PTtoAST(top); }
    }
    throw new RuntimeException("(PTtoAST) should not be there: " + pt);
  }

  public static ATerm PTtoAST(Tree pt) {
    %match(pt) {
      appl(prod[Attributes=attrs(listAttr(_*,cons(opname),_*))],arglist) -> { 
        ATermList newArgs = factory.makeList();
        Args args = `arglist;
        while(!args.isEmptylistTree()) {
          Tree t = args.getHeadlistTree();
matchblock: {
          %match(t) {
            // special case for list operators
            //appl(list(lex(_)),args2) -> {
            //  String newArgs2 = PTtoString(`args2);
            //  newArgs = newArgs.append(factory.makeAppl(factory.makeAFun(newArgs2,0,true)));
            //  break matchblock;
            //}
            appl(prod(listSymbol(lex(sort)),cf(sort),no_attrs()),args2) -> {
              // case for lexicals
              //System.out.println("args2 = " + `args2);
              ATerm newArgs2 = PTtoString(`args2);
              newArgs = newArgs.append(newArgs2);
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

        //System.out.println("newArgs = " + newArgs);
        int arity=newArgs.getLength();
        AFun fun = factory.makeAFun(`opname,arity,false);
        ATerm res = factory.makeApplList(fun,newArgs);
        return res;
      }

      


    }
    throw new RuntimeException("(PTtoAST [Tree]) should not be there: " + pt);
  }

  public static ATerm PTtoString(Args args) {
    StringBuffer consName = new StringBuffer();
    try {
      // collect the name of the constructore
      `OnceTopDown(ContainsConstructor(consName)).visitLight(`args);
    } catch(VisitFailure e) {
      // there is no consName but this is not a problem
    }

    StringBuffer sb = new StringBuffer();
    try{
      `TopDown(CollectMyChar(sb)).visitLight(args);
    } catch(VisitFailure e) {
      System.out.println("failure on " + args);
    }

    ATerm res = factory.makeAppl(factory.makeAFun(sb.toString(),0,true));
    if(consName.toString().length()>0) {
      res =  factory.makeAppl(factory.makeAFun(consName.toString(),1,false),res);
    }
    return res;
  }

  %typeterm StringBuffer { implement { StringBuffer } }
  %strategy CollectMyChar(sb:StringBuffer) extends Identity() {
    visit Tree {
      my_char(value) -> {
        char c = (char)`value;
        sb.append(c);
      }
    }
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
          StringBuffer sb = new StringBuffer(); // not used here
          `OnceTopDown(ContainsConstructor(sb)).visitLight(`args);
          // do nothing 
        } catch(VisitFailure e) {
          return `removedTree();
        }
      }
    }
  }

  %strategy ContainsConstructor(consName:StringBuffer) extends Fail() {
    visit Tree {
      x@appl(prod[Attributes=attrs(listAttr(_*,cons(name),_*))],_) -> {
        consName.append(`name);
        return `x;
      }
    }
  }


} 
