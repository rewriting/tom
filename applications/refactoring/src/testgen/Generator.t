/*
 * Copyright (c) 2004-2008, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce:w
 the above copyright
 *	notice, this list of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the INRIA nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
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

package testgen;

import testgen.tinyjava.*;
import testgen.tinyjava.types.*;
import java.io.*;

import tom.library.sl.*;

import java.util.*;


public class Generator {

  %include { util.tom }
  %include { lookup.tom }

  static Random random = new java.util.Random();

  static Strategy generateName = new GenerateName();

  static Strategy generateStmt = `Mu(MuVar("x"),ChoiceUndet(Mu(MuVar("y"),ChoiceUndet(Make_ConsBlock(MuVar("x"),MuVar("y")),Make_EmptyBlock())),Make_LocalVariableDecl(Make_Undefined(),generateName,Make_Undefined())));

  static Strategy generateClassDecl = `Mu(MuVar("x"),Make_ClassDecl(generateName,Make_Undefined(),Mu(MuVar("y"),ChoiceUndet(Make_ConsConcBodyDecl(ChoiceUndet(Make_FieldDecl(Make_Undefined(),generateName,Make_Undefined()),Make_MemberClassDecl(MuVar("x")),Make_Initializer(generateStmt)),MuVar("y")),Make_EmptyConcBodyDecl())))); 

  static Strategy generateClassDeclList = `Make_ConsConcClassDecl(generateClassDecl,Mu(MuVar("x"),ChoiceUndet(Make_ConsConcClassDecl(generateClassDecl,MuVar("x")),Make_EmptyConcClassDecl())));

  static Strategy generateProg = `Make_ConsProg(Make_CompUnit(generateName,generateClassDeclList),Mu(MuVar("x"),ChoiceUndet(Make_ConsProg(Make_CompUnit(generateName,generateClassDeclList),MuVar("x")),Make_EmptyProg())));

  public Prog generateProg() {
    try {
      return (Prog) generateProg.visit(`Prog());
    } catch ( VisitFailure e) {
      throw new RuntimeException("Unexpected strategy failure");
    }
  }

  public Prog removeConflicts(Prog p) {
    try {
      return (Prog) `InnermostId(RemoveConflicts()).visit(p);
    } catch ( VisitFailure e) {
      throw new RuntimeException("Unexpected strategy failure");
    }
  }


  public void generateClasses() throws java.io.IOException {
    StringWriter writer = new StringWriter();
    try {
      Prog p = (Prog) generateProg.visit(`Prog());
      System.out.println("Random class hierarchy generation...");
      System.out.println(collectAllTypes(p));
      System.out.println("Remove name conflicts...");
      p = (Prog) `InnermostId(RemoveConflicts()).visit(p);
      System.out.println(collectAllTypes(p));
      System.out.println("Generation of inheritance hierarchy...");
      System.out.println("For top-level classes");
      p = generateInheritanceHierarchyForTopLevelClasses(p);
      printDeclClass(p);
      System.out.println(p);
      System.out.println("Checking");
      `_Prog(_CompUnit(Identity(),_ConcClassDecl(_ClassDecl(Identity(),CheckSuperClassDefined(),Identity())))).visit(p);
      System.out.println("For member classes");
      p = generateInheritanceHierarchyForMemberClasses(p);
      System.out.println("Print classes");
      printDeclClass(p);
      %match ( p ) {
        Prog(_*,CompUnit(packageName,classes),_*) -> {
          new File("./"+`packageName.getname()).mkdir();
          %match ( classes ) {
            ConcClassDecl(_*,c@ClassDecl[name=name],_*) -> {
              File classfile = new File("./"+`packageName.getname()+"/"+`name.getname()+".java");
              classfile.createNewFile();
              FileWriter cwt = new FileWriter(classfile);
              cwt.write("package "+`packageName.getname()+";\n");
              printClass(cwt,`c);
              cwt.close();
            }
          }
        }
      }
    } catch ( VisitFailure e) {
      throw new RuntimeException("Unexpected strategy failure");
    }      
  }

  %strategy  CheckSuperClassDefined() extends Identity() {
    visit Name {
      Undefined() -> { throw new VisitFailure(); }
    }
  }

  %strategy RemoveConflicts() extends Identity() {
    visit BodyDeclList {
      ConcBodyDecl(X*,i1@Initializer[],Y*,i2@Initializer[],Z*) -> ConcBodyDecl(X*,i1,Y*,Z*)
        ConcBodyDecl(X*,c1@MemberClassDecl(ClassDecl[name=n]),Y*,c2@MemberClassDecl(ClassDecl[name=n]),Z*) -> ConcBodyDecl(X*,c2,Y*,Z*)
        ConcBodyDecl(X*,d1@FieldDecl[name=n],Y*,d2@FieldDecl[name=n],Z*) -> ConcBodyDecl(X*,d2,Y*,Z*)
    }
    visit Prog {
      Prog(X*,cu1@CompUnit[packageName=n],Y*,cu2@CompUnit[packageName=n],Z*) -> Prog(X*,cu1,Y*,Z*)
    }
    visit ClassDeclList {
      ConcClassDecl(X*,c1@ClassDecl[name=n],Y*,c2@ClassDecl[name=n],Z*) -> ConcClassDecl(X*,c1,Y*,Z*)
    }
    visit ClassDecl {
      c@ClassDecl[name=n] -> {
        //remove inner classes with the same name 
        return (ClassDecl) `InnermostId(RemoveConflictedMemberClasses(n)).visit(`c);
      }
    }
    visit Stmt {
      Block(X*,l1@LocalVariableDecl[name=name],Y*,l2@LocalVariableDecl[name=name],Z*) -> Block(X*,Y*,l2,Z*)
    }
  }

  %strategy RemoveConflictedMemberClasses(name:Name) extends Identity() {
    visit BodyDeclList {
      ConcBodyDecl(X*,MemberClassDecl[innerClass=ClassDecl[name=n]],Y*) -> {
        if (name.equals(`n)) {
          System.out.println("find a conflict between inner class names");
          return `ConcBodyDecl(X*,Y*);
        } 
      }
    }
  }

  public void printClass(Writer w, ClassDecl c) throws java.io.IOException {
    String classname = c.getname().getname();
    w.write("\n public class "+classname+" extends "+getComposedName(c.getsuper())+" {\n\n");
    BodyDeclList bodyDeclSet = c.getbodyDecl();
    %match ( bodyDeclSet ) {
      ConcBodyDecl(_*,bodyDecl,_*) -> {
        %match ( bodyDecl ) {
          FieldDecl(fieldType,name,expr) -> {
            //w.write(getComposedName(`fieldType)+" "+`name.getname()+" = "+getComposedName(`expr)+";\n");
            w.write(getComposedName(`fieldType)+" "+`name.getname()+";\n");
          }
          MemberClassDecl(innerClass) -> {
            printClass(w,`innerClass);
          }
          Initializer(body) -> {
            w.write("public "+classname+"() {\n");
            printStmt(w,`body);
            w.write("}\n");
          }
        }
      }
    }
    w.write("}\n");
  }

  public void printStmt(Writer w, Stmt c) throws java.io.IOException {
    %match(c) {
      b@Block(_*) -> {
        w.write("{");
        %match(b) {
          Block(_*,i,_*) -> {
            printStmt(w,`i);
          }
        }
        w.write("}\n");
      }
      LocalVariableDecl(varType,name,expr) -> {
        //w.write(getComposedName(`varType)+" "+`name.getname()+" = "+getComposedName(`expr)+";\n");
        w.write(getComposedName(`varType)+" "+`name.getname()+";\n");
      }
    }
  }

  public static class GenerateName extends BasicStrategy {
    public GenerateName() {
      super(`Identity());
    } 

    public Object visitLight(Object v, Introspector introspector) throws VisitFailure {
      return `Name(""+((char)('a'+(random.nextInt('z'-'a'+ 1)))));
    }
  }


  public Prog generateInheritanceHierarchyForTopLevelClasses(Prog p) {
    List<Type> alltopleveltypes = new ArrayList<Type>();
    alltopleveltypes.addAll(collectTopLevelTypes(p));
    List<Type> undefinedtypes = new ArrayList<Type>();
    undefinedtypes.addAll(alltopleveltypes);
    for (Type type: alltopleveltypes) {
      // type cannot be a superclass for itself
      boolean isUndefined = false;
      if (undefinedtypes.contains(type)) {
        isUndefined = true;
        undefinedtypes.remove(type);
      }
      int size = undefinedtypes.size();
      if (size>0) {
        int nb_subclasses = random.nextInt(size);
        if (nb_subclasses > 0) {
          Set<Integer> set = new HashSet();
          while (set.size() != nb_subclasses) {
            int index = random.nextInt(alltopleveltypes.size());
            Type t = alltopleveltypes.get(index);
            if (undefinedtypes.contains(t) & !set.contains(index)) {
              set.add(index);
            }
          }
          for (int index:set) {
            Type t = alltopleveltypes.get(index);
            undefinedtypes.remove(t);
            t.setsuperclass(type);
          }
        }
      }
      if (isUndefined) {
        undefinedtypes.add(type);
      }
    }
    Prog newp = p;
    for (Type type: alltopleveltypes) {
      TypeWrapper current = new TypeWrapper(type);
      Type superclass = type.getsuperclass();
      Name superclassname = `Dot(Name("Object"));
      if (superclass != null) {
        superclassname = superclass.getComposedName();
      }
      try {
        newp = (Prog) `ApplyAt(current,RenameSuperClass(superclassname)).visit(newp);
      } catch (VisitFailure e) {
        throw new RuntimeException(" Unexpected strategy failure");
      }
    }
    return newp;
  }

  public Prog generateInheritanceHierarchyForMemberClasses(Prog p) {
    Set<Position> undefinedtypes = collectMemberClassesPosition(p);
    if (! undefinedtypes.isEmpty()) {
      try {
        Iterator iter = undefinedtypes.iterator();
        Set<Name> inheritancePath = new HashSet();
        //find the corresponding name
        PositionWrapper current = new PositionWrapper((Position)iter.next());
        NameWrapper currentname = new NameWrapper();
        `ApplyAtPosition(current,GetName(currentname)).visit(p);
        System.out.println("currentname "+currentname.value);
        //add it to the inheritance path
        inheritancePath.add(currentname.value);
        Set<Name> accessibleNames = new HashSet();
        return (Prog) `Mu(MuVar("x"),IfThenElse(IsComplete(undefinedtypes), Identity(), Sequence(PrintContext(current,accessibleNames,inheritancePath,undefinedtypes),CollectAllAccessibleNames(current,accessibleNames,inheritancePath),ApplyAtPosition(current,RenameSuperClassAndUpdate(current,accessibleNames,inheritancePath,undefinedtypes)),MuVar("x")))).visit(p);
      } catch (VisitFailure e) {
        throw new RuntimeException(" Unexpected strategy failure");
      }
    } else {
      return p;
    }
  }

  %strategy PrintContext(current:PositionWrapper,accessibleNames:Set,inheritancePath:Set,undefinedtypes:Set) extends Identity() {
    visit Prog {
      p -> {
        System.out.println("current");
        System.out.println(current.value);
        System.out.println("inheritancePath");
        System.out.println(inheritancePath);
        System.out.println("accessibleNames");
        System.out.println(accessibleNames);
        System.out.println("undefinedtypes");
        System.out.println(undefinedtypes);
        System.out.println("prog");
        System.out.println(`p);
      }
    }
  }


  %strategy IsComplete(undefinedtypes:Set) extends Fail() {
    visit Prog {
      p -> {
        if (undefinedtypes.isEmpty()) {
          // the hierarchy is now complete
          return `p;
        }
      }
    }
  }

  %strategy RenameSuperClassAndUpdate(current:PositionWrapper, accessibleNames:Set, inheritancePath:Set, undefinedtypes:Set) extends Identity() {
    visit ClassDecl {
      c@ClassDecl[super=Undefined()] -> {
        undefinedtypes.remove(current.value);
        if (accessibleNames.isEmpty()) {
          // the hierarchy is now complete
          return `c.setsuper(`Dot(Name("Object")));
        } else {
          int index = random.nextInt(accessibleNames.size()+1);
          if (index == accessibleNames.size()) {
            // this index corresponds to the case where there is no super-class
            inheritancePath.clear();
            if(! undefinedtypes.isEmpty()) {
              current.value = (Position) undefinedtypes.iterator().next();
              NameWrapper currentname = new NameWrapper();
              `ApplyAtPosition(current,GetName(currentname)).visit(getEnvironment());
              System.out.println("currentname "+currentname.value);
              inheritancePath.add(currentname.value);
            }
            return `c.setsuper(`Dot(Name("Object")));
          } else {
            Iterator iter = accessibleNames.iterator();
            for (int i=0;i<index;i++) { iter.next(); }
            Name superclassname = (Name) iter.next();
            getEnvironment().setSubject(`c.setsuper(superclassname));
            PositionWrapper res = new PositionWrapper(new Position());
            getEnvironment().down(2);
            System.out.println("try to lookup "+getEnvironment().getSubject());
            try {
              `Lookup(res).visit(getEnvironment());
              throw new RuntimeException("the lookup fails in RenameSuperClassAndChange");
            } catch (VisitFailure e) {
              getEnvironment().up();
              current.value = res.value;
              NameWrapper currentname = new NameWrapper();
              `ApplyAtPosition(current,GetName(currentname)).visit(getEnvironment());
              System.out.println("currentname "+currentname.value);
              inheritancePath.add(currentname.value);
              return (ClassDecl) getEnvironment().getSubject();
            }
          }
        }
      }
      c@ClassDecl[super=!Undefined()] -> {
        inheritancePath.clear();
        if (! undefinedtypes.isEmpty()) {
          current.value = (Position) undefinedtypes.iterator().next();
        }
      }
    }
  }

  %strategy CollectAllAccessibleNames(current:PositionWrapper, accessibleNames:Set, inheritancePath:Set) extends Identity() {
    visit Prog {
      p -> {
        Set<Type> alltopleveltypes = collectTopLevelTypes(`p); 
        for (Type toplevel: alltopleveltypes) {
          accessibleNames.add(`Dot(Name(toplevel.getpackagename()),Name(toplevel.getname())));
        }
        `ApplyAtPosition(current,Print()).visit(getEnvironment());
        Strategy superclass_case = `Mu(MuVar("begin"),_ClassDecl(
              Identity(),
              ApplyAtSuperClass(MuVar("begin")),
              _ConcBodyDecl( IfThenElse(Is_MemberClassDecl(), _MemberClassDecl(Collect(accessibleNames)), Identity()))));

        Strategy main = `ApplyAtPosition(current,ApplyAtEnclosingClass(
              Mu(MuVar("begin"),Sequence(Print(),Collect(accessibleNames),_ClassDecl(
                    Identity(),
                    ApplyAtSuperClass(superclass_case),
                    _ConcBodyDecl( IfThenElse(Is_MemberClassDecl(), _MemberClassDecl(Collect(accessibleNames)), Identity())) 
                    ),Print(),IfThenElse(Up(Is_MemberClassDecl()),
                      ApplyAtEnclosingClass(MuVar("begin")),
                      Identity())))));
        main.visit(getEnvironment());
        accessibleNames.removeAll(inheritancePath);
      }
    }
  }


  %strategy Collect(allAccessibleNames:Set) extends Identity() {
    visit ClassDecl {
      ClassDecl[name=name] -> {
        allAccessibleNames.add(`Dot(name));
      }
    }
  }

  public void printAccessibleClassesForMemberClasses(Prog p) {
    Set<Type> allMemberTypes = collectMemberTypes(p);
    for (Type current:allMemberTypes) {
      System.out.println("accessible classes for "+current);
      TypeWrapper wrapper = new TypeWrapper(current);
      Strategy printAllAccessibleClasses =  `ApplyAt(wrapper,ApplyAtEnclosingClass(
            Mu(MuVar("begin"),Sequence(Print(),_ClassDecl(
                  Identity(),
                  ApplyAtSuperClass(MuVar("begin")),
                  _ConcBodyDecl( IfThenElse(Is_MemberClassDecl(), _MemberClassDecl(Print()), Identity())) 
                  ),IfThenElse(Up(Is_MemberClassDecl()),
                    ApplyAtEnclosingClass(MuVar("begin")),
                    Identity())))));
      try {
        printAllAccessibleClasses.visit(p);
      } catch (VisitFailure e) {
        throw new RuntimeException(" Unexpected strategy failure");
      }
    }
  }

  %strategy ApplyAtSuperClass(s:Strategy) extends Identity() {
    visit Name {
      n -> {
        PositionWrapper res = new PositionWrapper(new Position());
        System.out.println("try to find the super-class "+`n);
        `Try(Lookup(res)).visit(getEnvironment());
        System.out.println(`ApplyAtPosition(res,Print()).visit(getEnvironment()));
        `Choice(Lookup(res),Sequence(Debug("start to apply at the super class"),ApplyAtPosition(res,s),Debug("end to apply at the super class"))).visit(getEnvironment());
      }
    }
  }

  %strategy RenameSuperClass(name:Name) extends Identity() {
    visit ClassDecl {
      decl -> {
        return `decl.setsuper(name);
      }
    }
  }

  public static void main(String[] args) {
    Generator generator = new Generator();
    try {
      //generator.testLookup();
      generator.generateClasses();
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
  }

  %strategy FindSuperClass() extends Identity() {
    visit ClassDecl {
      decl@ClassDecl[super=name] -> {
        System.out.println("In the class "+`decl.getname());
        System.out.println("Try to find the super-class "+`name);
        getEnvironment().down(2);
        PositionWrapper pos = new PositionWrapper(new Position());
        try {
          `Lookup(pos).visit(getEnvironment());
          System.out.println("not found");
        } catch (VisitFailure e) {
          System.out.println("found at position="+pos.value);
          `ApplyAtPosition(pos,Print()).visit(getEnvironment());
        }
        getEnvironment().up();
      }
    }
  }



  public static void testLookup() {
    Prog p = ` Prog(CompUnit(Name("s"),ConcClassDecl(ClassDecl(Name("t"),Undefined(),ConcBodyDecl(Initializer(Block(LocalVariableDecl(Undefined(),Name("u"),Undefined()))))),ClassDecl(Name("k"),Undefined(),ConcBodyDecl(FieldDecl(Undefined(),Name("k"),Undefined()))),ClassDecl(Name("j"),Undefined(),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("t"),Dot(Name("s"),Name("o")),ConcBodyDecl())))),ClassDecl(Name("o"),Dot(Name("t"),Name("n")),ConcBodyDecl(Initializer(Block(LocalVariableDecl(Undefined(),Name("j"),Undefined()),LocalVariableDecl(Undefined(),Name("y"),Undefined()),LocalVariableDecl(Undefined(),Name("b"),Undefined()),LocalVariableDecl(Undefined(),Name("k"),Undefined()),LocalVariableDecl(Undefined(),Name("m"),Undefined()))),MemberClassDecl(ClassDecl(Name("p"),Undefined(),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("l"),Undefined())),MemberClassDecl(ClassDecl(Name("f"),Undefined(),ConcBodyDecl()))))),MemberClassDecl(ClassDecl(Name("c"),Undefined(),ConcBodyDecl())))))),CompUnit(Name("w"),ConcClassDecl(ClassDecl(Name("k"),Undefined(),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("h"),Undefined())))))),CompUnit(Name("t"),ConcClassDecl(ClassDecl(Name("n"),Dot(Name("w"),Name("k")),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("d"),Undefined(),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("v"),Undefined()))))),Initializer(Block()))),ClassDecl(Name("c"),Undefined(),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("h"),Undefined(),ConcBodyDecl(Initializer(Block(LocalVariableDecl(Undefined(),Name("w"),Undefined())))))),MemberClassDecl(ClassDecl(Name("u"),Undefined(),ConcBodyDecl(FieldDecl(Undefined(),Name("g"),Undefined())))),Initializer(Block(LocalVariableDecl(Undefined(),Name("b"),Undefined()),LocalVariableDecl(Undefined(),Name("u"),Undefined()),LocalVariableDecl(Undefined(),Name("m"),Undefined()))))))));
    /**
      Prog p = `Prog(
      CompUnit(Name("a"),ConcClassDecl(
      ClassDecl(Name("A"),Dot(Name("b"),Name("B")),ConcBodyDecl(
      MemberClassDecl(ClassDecl(Name("C"),Dot(Name("b"),Name("B")),ConcBodyDecl())),
      MemberClassDecl(ClassDecl(Name("D"),Name("C"),ConcBodyDecl())))))),
      CompUnit(Name("b"),ConcClassDecl(
      ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl())))
      );
     */
    System.out.println(p);
    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      throw new RuntimeException("Unexpected strategy failure");
    }
  }

} 
