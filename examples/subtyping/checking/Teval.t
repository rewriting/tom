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

import subtyping.definition.types.*;
import tom.library.sl.*;
import java.io.*;
import org.antlr.runtime.*;

public class Teval {
	%include { sl.tom }
  %include { definition/Definition.tom }

  public static void printTree(Tree new_tree) {
    %match {
      SList() << new_tree -> { System.out.println("\n\nTO BE CONTINUED WITH THE OTHERS RULES OF TYPE CHECKING SYSTEM."); }

      SList(st,sts*) << new_tree && Statement1(name,ptree,ctx,pair) << st
      -> 
      { 
        System.out.println("\n\n" + `name + " : \n\t\t-------------------------------------------------");

        System.out.print("[");
        `printLabel(ptree);
        System.out.print("] \t\t");
        
        System.out.print("{");
        `printContext(ctx);
        System.out.print("} |- ");
        `printMapping(pair);
        
        `printTree(SList(sts*));
      }

      SList(st,sts*) << new_tree && Statement2(name,ptree,ctx,pair) << st
      -> 
      { 
        System.out.println("\n\n" + `name + " : \n\t\t-------------------------------------------------");

        System.out.print("[");
        `printLabel(ptree);
        System.out.print("] \t\t");
        
        System.out.print("{");
        `printContext(ctx);
        System.out.print("} |- ");
        `printConstraint(pair);
        
        `printTree(SList(sts*));
      }

    }
  }

  public static void printLabel(PathTree ptree) {
    %match {
      PTree(Label(i)) << ptree -> {System.out.print(`i); }
      PTree(Label(i),lbl,lbls*) << ptree
      ->
      { 
        System.out.print(`i + ".");
        `printLabel(PTree(lbl,lbls*));
      }
    }
  }

  public static void printContext(Context ctx) {
    %match {
      Context(SigOf(name,nsig)) << ctx && Atom(tyname) << nsig -> { System.out.print(`name + ":" + `tyname); }
      
      Context(SigOf(name,nsig)) << ctx && Composed(dom,codom) << nsig && Atom(tyname) << codom
      -> 
      { 
        System.out.print(`name + ":");
        `printTyList(dom);
        System.out.println(" -> " + `tyname);
      }

      Context(SigOf(name,nsig),sig,sigs*) << ctx && Atom(tyname) << nsig
      ->
      { 
        System.out.print(`name + ":" + `tyname + ", "); 
        `printContext(Context(sig,sigs*));
      }

      Context(SigOf(name,nsig),sig,sigs*) << ctx && Composed(dom,codom) << nsig &&
      Atom(tyname) << codom
      ->
      { 
        System.out.print(`name + ":");
        `printTyList(dom); 
        System.out.print(" -> " + `tyname + ", ");
        `printContext(Context(sig,sigs*));
      }
    }
  }

  public static void printTyList(TypeList tylist) {
    %match {
      TyList(ty) << tylist && Atom(tyname) << ty -> { System.out.print(`tyname); }

      TyList(ty1,ty2,tys*) << tylist && Atom(tyname) << ty1 
      -> 
      { 
        System.out.print(`tyname + " x ");
        `printTyList(TyList(ty2,tys*));
      }
    }
  }

  public static void printTeList(TermList telist) {
    %match {
      TeList(t) << telist && Var(name) << t -> { System.out.print(`name); }

      TeList(t) << telist && Fun(name,args) << t
      -> 
      { 
        System.out.print(`name + "(");
        `printTeList(args);
        System.out.print(")");
     }

      TeList(t1,t2,ts*) << telist && Var(name) << t1
      -> 
      { 
        System.out.print(`name + ", ");
        `printTeList(TeList(t2,ts*));
      }

      TeList(t1,t2,ts*) << telist && Fun(name,args) << t1
      -> 
      { 
       System.out.print(`name + "(");
        `printTeList(args);
       System.out.print("), ");
       `printTeList(TeList(t2,ts*));
     }
    }
  }

  public static void printMapping(Mapping pair) {
    %match {
      TypeOf(t,ty) << pair && Var(name) << t && Atom(tyname) << ty -> { System.out.println(`name + ":" + `tyname); }

      TypeOf(t,ty) << pair && Fun(name,args) << t && Atom(tyname) << ty
      -> 
      { 
        System.out.print(`name + "(");
        `printTeList(args);
        System.out.print("):" + `tyname);
      }
    } 
  }

  public static void printConstraint(Constraint pair) {
    %match {
      Subtype(ty1,ty2) << pair -> { System.out.println(`ty1 + "<:" + `ty2); }
    } 
  }


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
      System.out.println("\n------- Running Type Checking Algorithm --------\n");
      Teval test = new Teval();
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

  public void run(Input in) {
    Tree tree = `SList(); 
    %match {
      Input(ctx,cl,t,ty) << in
      -> 
      {
        tree = Checker.`check(ctx,cl,t,ty);
        printTree(tree);
        System.out.print("\nTerm <<");
        `printMapping(TypeOf(t,ty));
        System.out.println(">> is well-formed.\n");
      }
    }
  }
}
