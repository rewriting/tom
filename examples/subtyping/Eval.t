package subtyping;

import subtyping.language.types.*;
import tom.library.sl.*;
import java.util.*;
import java.io.*;
import org.antlr.runtime.*;

public class Eval {
  %include { language/Language.tom }
  %include { sl.tom }

  public static void main(String[] args) {
/*
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(Instructions.in));
      Input in = Input.fromString(reader.readLine());
      //fromForPrintToMain_Verify(InputVerify.fromString(in.readLine()));
    } catch (IOException e) {
      e.printStackTrace();
    }
*/
    System.out.println("\n------- Running Type Reconstruction Algorithm --------\n");
    Eval test = new Eval();
    test.run();
  }
  
  private static int counter = 0;

  public void run() {
    TomInstruction match1 = `Match(Rule(Matching(Simple(Var("x",TypeVar(1))),Fun("zero",TTeList()),TypeVar(2)),TTeList(Var("x",TypeVar(3)))));
    ReconResultList test1 = TypeInference.typeOf(match1);
    System.out.println("x:var1 << (var2) zero() -> {`x:var3}");
    writeRRList(test1);
    counter = 0;

    TomInstruction match2 = `Match(Rule(Matching(Simple(Fun("suc",TTeList(Var("x",TypeVar(1))))),
                                                 Fun("suc",TTeList(Fun("zero",TTeList()))),TypeVar(2)),TTeList(Var("x",TypeVar(3)))));
    ReconResultList test2 = TypeInference.typeOf(match2);
    System.out.println("suc(x:var1) << (var2) suc(zero()) -> {`x:var3}");
    writeRRList(test2);
    counter = 0;

    TomInstruction match3 = `Match(Rule(Matching(Simple(Fun("plus",TTeList((Var("x",TypeVar(1))),(Fun("uminus",TTeList(Var("x",TypeVar(1)))))))),
                                                 Fun("plus",TTeList((Fun("zero",TTeList())),(Fun("uminus",TTeList(Fun("zero",TTeList())))))),TypeVar(2)),
                                        TTeList(Var("x",TypeVar(3)))));
    ReconResultList test3 = TypeInference.typeOf(match3);
    System.out.println("plus(x:var1,uminus(x:var1)) << (var2) plus(zero(),uminus(zero())) -> {`x:var3}");
    writeRRList(test3);
    counter = 0;

    TomInstruction match4 = `Match(Rule(Matching(Simple(Fun("mult",TTeList(Var("x",TypeVar(1)),Fun("suc",TTeList(Var("x",TypeVar(1))))))),
                                                 Fun("mult",TTeList(Fun("zero",TTeList()),Fun("suc",TTeList(Fun("zero",TTeList()))))),TypeVar(2)),
                                        TTeList(Var("x",TypeVar(3)))));
    ReconResultList test4 = TypeInference.typeOf(match4);
    System.out.println("mult(x:var1,suc(x:var1)) << (var2) mult(zero(),suc(zero())) -> {`x:var3}");
    writeRRList(test4);
    counter = 0;

    TomInstruction match5 = `Match(Rule(Matching(Simple(Fun("mult",TTeList((Var("x",TypeVar(1))),(Fun("square",TTeList(Var("x",TypeVar(1)))))))),
                                                 Fun("mult",TTeList((Fun("zero",TTeList())),(Fun("square",TTeList(Fun("zero",TTeList())))))),TypeVar(2)),
                                        TTeList(Var("x",TypeVar(3)))));
    ReconResultList test5 = TypeInference.typeOf(match5);
    System.out.println("mult(x:var1,square(x:var1)) << (var2) mult(zero(),square(zero())) -> {`x:var3}");
    writeRRList(test5);
    counter = 0;

    TomInstruction match6 = `Match(Rule(Matching(Simple(Fun("suc",TTeList(Fun("zero",TTeList())))),
                                                 Fun("suc",TTeList(Fun("zero",TTeList()))),TypeVar(1)),TTeList()));
    ReconResultList test6 = TypeInference.typeOf(match6);
    System.out.println("suc(zero()) << (var1) suc(zero()) -> {`x:var2}");
    writeRRList(test6);
    counter = 0;

  }

  private static void writeRRList(ReconResultList rrlist) {
    %match(rrlist) {
      RRList() -> { System.out.println("\n"); }
      RRList(rresult,rreslist*) -> {
        System.out.print("Pair "+ (counter++) + ": ");
        writePair(`rresult);
        writeRRList(`rreslist);
      }
    }
  }

  private static void writePair(ReconResult pair) {
    %match(pair) {
      Pair(ty,cons) -> {
        writeTomType(`ty);
        System.out.print(" : {");
        writeConstraints(`cons);
        System.out.println("}\n");
      }
    }
  }

  private static void writeConstraints(ConstraintList cl) {
    %match(cl) {
      CList() -> { System.out.print(""); }
      CList(c,cs*) -> {
        %match(c) {
          Equation(t1,t2) -> {
            System.out.print(" (");
            writeTomType(`t1);
            System.out.print(" = ");
            writeTomType(`t2);
            System.out.print(") ");
          }
          Subtype(t1,t2) -> {
            System.out.print(" (");
            writeTomType(`t1);
            System.out.print(" <: ");
            writeTomType(`t2);
            System.out.print(") ");
          }
        }
        writeConstraints(`cs);
      }
    }
  }

  private static void writeTomType(TomType t) {
    %match(t) {
      Type(n) -> { System.out.print(`n); }
      TypeVar(i) -> { System.out.print("var" + `i); }
    }
  }
}
