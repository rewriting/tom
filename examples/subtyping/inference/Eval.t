/*
   *
   * Copyright (c) 2004-2011, INPL, INRIA
   * All rights reserved.
   *
   * Redistribution and use in source and binary forms, with or without
   * modification, are permitted provided that the following conditions are
   * met:
   *  - Redistributions of source code must retain the above copyright
   *  notice, this list of conditions and the following disclaimer.
   *  - Redistributions in binary form must reproduce the above copyright
   *  notice, this list of conditions and the following disclaimer in the
   *  documentation and/or other materials provided with the distribution.
   *  - Neither the name of the INRIA nor the names of its
   *  contributors may be used to endorse or promote products derived from
   *  this software without specific prior written permission.
   * 
   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
   * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
   * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
   * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
   * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
   * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
   * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
   */

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
      String path = "";
      if (args.length > 0)
        path = args[0];
      else {
        System.out.println("Please enter the file path that contains the Input information: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        path = reader.readLine();
      }
      File testFile = new File(path);
      String instructions = getContents(testFile);
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
      SList(subs*,sub) << allSubs &&
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
        writeCondition(`cond);
        System.out.print(" -> { ");
        writeTTeList(`res);
        System.out.print(" }");
      }
    }
  }

  private static void writeConjConditionList(ConditionList conds) {
    %match(conds) {
      CondList(c1,cs*)
      ->
      {
        writeCondition(`c1);
        if (`cs != `CondList()) {
          System.out.println(" && ");
          writeConjConditionList(`cs);
        }
      }
    }
  }

  private static void writeDisjConditionList(ConditionList conds) {
    %match(conds) {
      CondList(c1,cs*)
      ->
      {
        writeCondition(`c1);
        if (`cs != `CondList()) {
          System.out.println(" || ");
          writeDisjConditionList(`cs);
        }
      }
    }
  }


  private static void writeCondition(Condition cond) {
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

      Conjunction(conds)
      ->
      {
        System.out.print("(");
        writeConjConditionList(`conds);
        System.out.print(")");
      }

      Disjunction(conds)
      ->
      {
        System.out.print("(");
        writeDisjConditionList(`conds);
        System.out.print(")");
      }

      Equality(t1,t2)
      ->
      {
        System.out.print("(");
        writeTomTerm(`t1);
        System.out.print(" == ");
        writeTomTerm(`t2);
        System.out.print(")");
      }

      Inequality(t1,t2)
      ->
      {
        System.out.print("(");
        writeTomTerm(`t1);
        System.out.print(" != ");
        writeTomTerm(`t2);
        System.out.print(")");
      }

      Greater(t1,t2)
      ->
      {
        System.out.print("(");
        writeTomTerm(`t1);
        System.out.print(" > ");
        writeTomTerm(`t2);
        System.out.print(")");
      }

      GreaterEq(t1,t2)
      ->
      {
        System.out.print("(");
        writeTomTerm(`t1);
        System.out.print(" >= ");
        writeTomTerm(`t2);
        System.out.print(")");
      }

      Less(t1,t2)
      ->
      {
        System.out.print("(");
        writeTomTerm(`t1);
        System.out.print(" < ");
        writeTomTerm(`t2);
        System.out.print(")");
      }

      LessEq(t1,t2)
      ->
      {
        System.out.print("(");
        writeTomTerm(`t1);
        System.out.print(" <= ");
        writeTomTerm(`t2);
        System.out.print(")");
      }

    }
  }

  private static void writeMList(Substitution subs) {
    %match(subs) {
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

      ListVar(n,t)
      ->
      {
        System.out.print(`n + "*:");
        writeTomType(`t);
      }

      Fun(n,args)
      ->
      {
        System.out.print(`n + "(");
        writeTTeList(`args);
        System.out.print(")");
      }
 
      List(n,args)
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
      TypeVar(i) -> { System.out.print("tvar" + `i); }
    }
  }
}
