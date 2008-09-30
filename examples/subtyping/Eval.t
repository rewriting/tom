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
    try {
      System.out.println("Please enter the file path that contain the Input information: ");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      File testFile = new File(reader.readLine());
      String instructions = getContents(testFile);
      //System.out.println("Original file contents: " + instructions);
      Input in = Input.fromString(instructions);
      System.out.println("\n------- Running Type Reconstruction Algorithm --------\n");
      Eval test = new Eval();
      test.run(in);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String getContents(File aFile) {
    StringBuilder contents = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(aFile));
      try {
        String line = null;
        while ((line = reader.readLine()) != null) {
          contents.append(line);
        }
      }
      finally {
        reader.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    String instructions = "Input(" + contents.toString() + ")";
    return instructions;
  }
  
  private static int counter = 0;

  public void run(Input in) {
    SubstitutionList allSubs = TypeInference.typeOfAll(in);
    %match(in) {
      Input(_,_,allMatchs)
      -> { writeMatchsAndSubs(allSubs,`allMatchs); }
    }
  }

  private static void writeMatchsAndSubs(SubstitutionList allSubs, TomInstructionList allMatchs) {
    %match {
      SList(sub,subs*) << allSubs &&
      TIList(match,matchs*) << allMatchs
      ->
      {
        System.out.print("Match "+ (counter++) + ": ");
        writeMatch(`match);
        System.out.print("\n Subs: {");
        writeMList(`sub);
        System.out.println("}\n");
        `writeMatchsAndSubs(subs*,matchs*);
      }
    }
  }

  private static void writeMatch(TomInstruction match) {
    %match(match) {
      Match(rule) -> { writeRule(`rule); }
    }
  }

  private static void writeRule(Clause rule) {
    %match(rule) {
      Rule(cond,res) 
      ->
      {
        writeMatching(`cond);
        System.out.print(" -> { ");
        writeTTeList(`res);
        System.out.print(" }");
      }
    }
  }

  private static void writeMatching(Condition cond) {
    %match(cond) {
      Matching(p,s,ty)
      ->
      {
        writeTomTerm(`p);
        System.out.print(" << (");
        writeTomType(`ty);
        System.out.print(") ");
        writeTomTerm(`s);
      }
    }
  }

  private static void writeMList(Substitution subs) {
    %match(subs) {
      //MList() -> { System.out.print(""); }
      MList(map,maps*) &&
      MapsTo(t1,t2) << map -> {
        writeTomType(`t1);
        System.out.print(" |-> ");
        writeTomType(`t2);
        if (`maps != `MList()) {
          System.out.print(", ");
          writeMList(`maps*);
        }
      }
    }
  }

  private static void writeRRList(ReconResultList rrlist) {
    %match(rrlist) {
      RRList() -> { System.out.println("\n"); }
      RRList(rresult,rreslist*) -> {
        System.out.print("Pair "+ (counter++) + ": ");
        writePair(`rresult);
        writeRRList(`rreslist*);
      }
    }
  }

  private static void writePair(ReconResult pair) {
    %match(pair) {
      Pair(ty,cons) -> {
        writeTomType(`ty);
        System.out.print(" : {");
        writeCList(`cons);
        System.out.println("}\n");
      }
    }
  }

  private static void writeCList(ConstraintList cl) {
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
        writeCList(`cs*);
      }
    }
  }

  private static void writeTTeList(TomTermList args) {
    %match(args) {
      //TTeList() -> { System.out.print(""); }
      TTeList(term,terms*)
      ->
      {
        writeTomTerm(`term);
        if (`terms != `TTeList()) {
          System.out.print(", ");
          writeTTeList(`terms*);
        }
      }
    }
  }

  private static void writeTomTerm(TomTerm term) {
    %match(term) {
      Var(n,t)
      ->
      {
        System.out.print(`n + ":");
        writeTomType(`t);
      }
      Fun(n,args)
      ->
      {
        System.out.print(`n + "(");
        writeTTeList(`args);
        System.out.print(")");
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
