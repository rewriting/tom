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

      SList(st,sts*) << new_tree && Statement(name,ptree,ctx,pair) << st
      -> 
      { 
        System.out.println("\n\n" + `name + " : \n\t\t-------------------------------------------------");

        System.out.print("[");
        `printLabel(ptree);
        System.out.print("] \t\t");
        
        System.out.print("{");
        `printContext(ctx);
        System.out.print("} |- ");
        `printConsequence(pair);
        
        new_tree = `SList(sts*);
        printTree(new_tree);
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

  public static void printConsequence(Mapping pair) {
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
        `printConsequence(TypeOf(t,ty));
        System.out.println(">> is well-formed.\n");
      }
    }
  }
}
