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

  static Random random = new java.util.Random();

  static Strategy generateName = new GenerateName();

  static Strategy generateStmt = `Mu(MuVar("x"),ChoiceUndet(Mu(MuVar("y"),ChoiceUndet(Make_ConsBlock(MuVar("x"),MuVar("y")),Make_EmptyBlock())),Make_LocalVariableDecl(Make_Undefined(),generateName,Make_Undefined())));

  static Strategy generateClassDecl = `Mu(MuVar("x"),Make_ClassDecl(generateName,Make_Undefined(),Mu(MuVar("y"),ChoiceUndet(Make_ConsConcBodyDecl(ChoiceUndet(Make_FieldDecl(Make_Undefined(),generateName,Make_Undefined()),Make_MemberClassDecl(MuVar("x")),Make_Initializer(generateStmt)),MuVar("y")),Make_EmptyConcBodyDecl())))); 

  static Strategy generateClassDeclList = `Make_ConsConcClassDecl(generateClassDecl,Mu(MuVar("x"),ChoiceUndet(Make_ConsConcClassDecl(generateClassDecl,MuVar("x")),Make_EmptyConcClassDecl())));

  static Strategy generateProg = `Make_ConsProg(Make_CompUnit(generateName,generateClassDeclList),Mu(MuVar("x"),ChoiceUndet(Make_ConsProg(Make_CompUnit(generateName,generateClassDeclList),MuVar("x")),Make_EmptyProg())));

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
      p = generateInheritanceHierarchy(p);
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
      printAccessibleClassesForInnerClasses(p);
    } catch ( VisitFailure e) {
      throw new RuntimeException("Unexpected strategy failure");
    }      
  }

  %strategy RemoveConflicts() extends Identity() {
    visit BodyDeclList {
      ConcBodyDecl(X*,i1@Initializer[],Y*,i2@Initializer[],Z*) -> ConcBodyDecl(X*,i1,Y*,Z*)
        ConcBodyDecl(X*,c1@MemberClassDecl(ClassDecl[name=n]),Y*,c2@MemberClassDecl(ClassDecl[name=n]),Z*) -> ConcBodyDecl(X*,c2,Y*,Z*)
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
        return (ClassDecl) `InnermostId(RemoveConflictedInnerClasses(n)).visit(`c);
      }
    }
  }

  %strategy RemoveConflictedInnerClasses(name:Name) extends Identity() {
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
    BodyDeclList bodyDeclList = c.getbodyDecl();
    %match ( bodyDeclList ) {
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
            w.write(classname+"() {\n");
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

  public Prog generateInheritanceHierarchy(Prog p) {
    // generate inheritance hierarchy for top level classes
    List<Type> alltopleveltypes = collectTopLevelTypes(p);
    List<Type> undefinedtypes = new ArrayList<Type>();
    undefinedtypes.addAll(alltopleveltypes);
    List<Type> availabletypes = new ArrayList<Type>();
    availabletypes.addAll(alltopleveltypes);
    TypeWrapper current = new TypeWrapper((Type)undefinedtypes.get(0)); 
    try {
      return (Prog) `Mu(MuVar("x"),IfThenElse(IsComplete(undefinedtypes), Identity(), Sequence(ApplyAt(current,RenameSuperClass(availabletypes,alltopleveltypes,undefinedtypes,current)),MuVar("x")))).visit(p);
    } catch (VisitFailure e) {
      throw new RuntimeException(" Unexpected strategy failure");
    }
  }

  public void printAccessibleClassesForInnerClasses(Prog p) {
    List<Type> allMemberTypes = collectMemberTypes(p);
    System.out.println(allMemberTypes);
    for (Type current:allMemberTypes) {
      System.out.println("accessible classes for "+current);
      TypeWrapper wrapper = new TypeWrapper(current);
      Strategy printAllAccessibleClasses =  `ApplyAt(wrapper,UpToOuterClass(
              Mu(MuVar("begin"),Sequence(Print(),_ClassDecl(
                    Identity(),
                    ApplyAtSuperClass(MuVar("begin")),
                    _ConcBodyDecl( IfThenElse(Is_MemberClassDecl(), Print(), Identity())) 
                    ),IfThenElse(Up(Is_MemberClassDecl()), UpToOuterClass(MuVar("begin")), Identity())))));
      try {
        printAllAccessibleClasses.visit(p);
      } catch (VisitFailure e) {
        throw new RuntimeException(" Unexpected strategy failure");
      }
    }
  }

  %op Strategy UpToOuterClass(s:Strategy) {
    make(s) { `Mu(MuVar("y"),Up(IfThenElse(Is_ClassDecl(),s,MuVar("y")))) }
  }

  %strategy ApplyAtSuperClass(s:Strategy) extends Identity() {
    visit ComposedName {
      composedName@Dot(packagename,classname) -> {
        TypeWrapper type = new TypeWrapper(new Type(`packagename.getname(),`classname.getname()));
        System.out.println(`composedName);
        Position current = getPosition();
        getEnvironment().followPath(current.inverse());
        System.out.println(getPosition());
        `ApplyAt(type,s).visit(getEnvironment());
        getEnvironment().followPath(current);
      }
    } 
  }

  %strategy RenameSuperClass(availabletypes:List,alltopleveltypes:List,undefinedtypes:List,current:TypeWrapper) extends Identity() {
    visit ClassDecl {
      c@ClassDecl[super=Undefined()]  -> {
        undefinedtypes.remove(current.type);
        if (availabletypes.isEmpty()) {
          // the hierarchy is now complete
          return `c.setsuper(`Dot(Name("Object")));
        } else {
          availabletypes.remove(current.type);
          int index = random.nextInt(availabletypes.size()+1);
          if (index == availabletypes.size()) {
            // this index corresponds to the case where there is no super-class
            availabletypes.addAll(alltopleveltypes);
            if(! undefinedtypes.isEmpty()) {
              current.type = (Type) undefinedtypes.get(0);
            }
            return `c.setsuper(`Dot(Name("Object")));
          } else {
            current.type = (Type) availabletypes.get(index);
            // construct the ComposedName
            return `c.setsuper(current.type.getComposedName());
          }
        }
      }
      c@ClassDecl[super=!Undefined()] -> {
        availabletypes.addAll(alltopleveltypes);
        if (! undefinedtypes.isEmpty()) {
          current.type = (Type) undefinedtypes.get(0);
        }
      }
    }
  }

  %strategy IsComplete(undefinedtypes:List) extends Fail() {
    visit Prog {
      p -> {
        if (undefinedtypes.isEmpty()) {
          // the hierarchy is now complete
          return `p;
        }
      }
    }
  }

  public static void main(String[] args) {
    Generator generator = new Generator();
    try {
      generator.generateClasses();
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
  }


} 
