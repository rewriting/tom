package jastadd;

import ast.AST.*;

import java.io.*;

import java.util.*;

import jrag.*;
import jrag.AST.SimpleNode;
import jrag.AST.ASTAspectMethodDeclaration;
import jrag.AST.ASTAspectRefineMethodDeclaration;
import jrag.AST.ASTAspectFieldDeclaration;
import jrag.AST.ASTBlock;
import jrag.ClassBodyObject;

aspect JastAddCodeGen {
  public void Grammar.weaveInterfaceIntroductions() {
    for(int i = 0; i < getNumTypeDecl(); i++) {
      if(getTypeDecl(i) instanceof InterfaceDecl) {
        InterfaceDecl d = (InterfaceDecl)getTypeDecl(i);
        String name = d.name();
        //System.out.println("Processing interface " + name);
 
        for(int j = 0; j < getNumTypeDecl(); j++) {
          if(getTypeDecl(j) instanceof ASTDecl) {
            ASTDecl dest = (ASTDecl)getTypeDecl(j);
            if(dest.implementsInterface(name)) {
              //System.out.println("  implemented by " + dest.name());
              for(Iterator iter = d.getClassBodyDecls(); iter.hasNext(); ) {
                ClassBodyObject o = (ClassBodyObject)iter.next();
                if(o.node instanceof ASTAspectMethodDeclaration || o.node instanceof ASTAspectFieldDeclaration) {
                    if(!dest.hasClassBodyDecl(o.signature()))
                        dest.classBodyDecls.add(new ClassBodyObject(o.node/*.fullCopy()*/, o.fileName, o.line));
                }
                else if(o.node instanceof ASTAspectRefineMethodDeclaration) {
                	ClassBodyObject object = new ClassBodyObject(o.node/*.fullCopy()*/, o.fileName, o.line);
                	object.refinesAspect = o.refinesAspect;
                    dest.classBodyDecls.add(object);
                }
                else if(o.node instanceof ASTBlock) {
                  dest.classBodyDecls.add(new ClassBodyObject(o.node/*.fullCopy()*/, o.fileName, o.line));
                }
              }
              for(Iterator iter = d.refinedClassBodyDecls.iterator(); iter.hasNext(); ) {
                  ClassBodyObject o = (ClassBodyObject)iter.next();
                  if(o.node instanceof ASTAspectMethodDeclaration || o.node instanceof ASTAspectFieldDeclaration) {
                      if(!dest.hasClassBodyDecl(o.signature()))
                          dest.refinedClassBodyDecls.add(new ClassBodyObject(o.node/*.fullCopy()*/, o.fileName, o.line));
                  }
                  else if(o.node instanceof ASTAspectRefineMethodDeclaration) {
                  	ClassBodyObject object = new ClassBodyObject(o.node/*.fullCopy()*/, o.fileName, o.line);
                	object.refinesAspect = o.refinesAspect;
                    dest.refinedClassBodyDecls.add(object);
                  }
                  else if(o.node instanceof ASTBlock) {
                    dest.classBodyDecls.add(new ClassBodyObject(o.node/*.fullCopy()*/, o.fileName, o.line));
                  }
              }
             
              for(int k = 0; k < d.getNumSynDecl(); k++) {
                //System.out.println("    adding syndecl " + d.getSynDecl(k).signature());
                dest.addSynDecl((SynDecl)d.getSynDecl(k).fullCopy());
              }
             
              for(int k = 0; k < d.getNumSynEq(); k++) {
                //System.out.println("    adding syneq " + d.getSynEq(k).signature());
                dest.addSynEq((SynEq)d.getSynEq(k).fullCopy());
              }
             
              for(int k = 0; k < d.getNumInhDecl(); k++) {
                //System.out.println("    adding inhdecl " + d.getInhDecl(k).signature());
                dest.addInhDecl((InhDecl)d.getInhDecl(k).fullCopy());
              }
             
              for(int k = 0; k < d.getNumInhEq(); k++) {
                //System.out.println("    adding inheq " + d.getInhEq(k).signature());
                dest.addInhEq((InhEq)d.getInhEq(k).fullCopy());
              }
            }
          }
        }
      }
    }
  }

  public boolean ASTDecl.hasClassBodyDecl(String signature) {
    for(Iterator iter = getClassBodyDecls(); iter.hasNext(); ) {
      ClassBodyObject o = (ClassBodyObject)iter.next();
      if(o.signature().equals(signature))
        return true;
    }
    return false;
  }

  public void Grammar.jastAddGen(File outputDir, String grammarName, String packageName, boolean publicModifier) {
    if(packageName != null && !packageName.equals("")) {
      File dir = new File(outputDir, packageName.replace('.', File.separatorChar));
      dir.mkdirs();
    }

    for(int i = 0; i < getNumTypeDecl(); i++) {
      getTypeDecl(i).jastAddGen(outputDir, grammarName, packageName, publicModifier);
    }
  }

  public String TypeDecl.modifiers() {
    if(modifiers == null || modifiers.equals(""))
      return "public ";
    else
      return modifiers.replaceAll("static ", "");
  }

  public String TypeDecl.typeParameters = "";

  public String TypeDecl.typeDeclarationString() {
    return "";
  }
  public String ClassDecl.typeDeclarationString() {
    String s = interfacesString();
    if(s.equals(""))
      return modifiers() + "class " + getIdDecl().getID() + typeParameters + " extends " + extendsName + " {";
    else
      return modifiers() + "class " + getIdDecl().getID() + typeParameters + " extends " + extendsName + " implements " +
        s + " {";
  }
  public String InterfaceDecl.typeDeclarationString() {
    String s = interfacesString();
    if(s.equals(""))
      return modifiers() + "interface " + getIdDecl().getID() + typeParameters + " {";
    else
      return modifiers() + "interface " + getIdDecl().getID() + typeParameters + " extends " + s + " {";
  }

  public String TypeDecl.javaDocComment() {
    return getComment() != null ? (getComment() + "\n") : "";
  }
    
  public void TypeDecl.jastAddGen(File outputDir, String grammarName, String packageName, boolean publicModifier) {
    File file = null;
    try {
      if(packageName != null && !packageName.equals("")) {
        file = new File(outputDir, packageName.replace('.', File.separatorChar) + File.separator + name() + ".java");
      }
      else {
        file = new File(outputDir, name() + ".java");
      }
      PrintStream stream = new PrintStream(new FileOutputStream(file));

      if(license != null) stream.println(license);

      if(packageName != null && !packageName.equals("")) {
        stream.println("package " + packageName + ";");
      }

      stream.print(env().genImportsList());
      stream.print(javaDocComment());
      stream.println(typeDeclarationString());

      /*
      StringBuffer buf = new StringBuffer();
      for(Iterator iter = getClassBodyDecls(); iter.hasNext(); ) {
        ClassBodyObject o = (ClassBodyObject)iter.next();
        jrag.AST.SimpleNode n = o.node;
        
        if(!(n instanceof ASTAspectMethodDeclaration) && !(n instanceof ASTAspectFieldDeclaration)) {
          buf.append("    // Declared in " + o.fileName + " at line " + o.line + "\n");
          n.unparseClassBodyDeclaration(buf, name(), false); //  Fix AspectJ
          buf.append("\n\n");
        }
      }
      stream.println(buf.toString());
      */

      stream.print(genMembers());
      stream.print(genAbstractSyns());
      //stream.print(genSynEquations());
      stream.print(genInhDeclarations());
      //stream.print(genInhEquations());
      
      stream.println("}");
      stream.close();
    } catch (FileNotFoundException f) {
      System.err.println("Could not create file " + file.getName() + " in " + file.getParent());
      System.exit(1);
    }
  }
  
  public void ClassDecl.jastAddGen(File outputDir, String grammarName, String packageName, boolean publicModifier) {
    File file = null;
    try {
      if(packageName != null && !packageName.equals("")) {
        file = new File(outputDir, packageName.replace('.', File.separatorChar) + File.separator + name() + ".java");
      }
      else {
        file = new File(outputDir, name() + ".java");
      }
      PrintStream stream = new PrintStream(new FileOutputStream(file));
      if(license != null) stream.println(license);

      if(packageName != null && !packageName.equals("")) {
        stream.println("package " + packageName + ";");
      }

      stream.print(env().genImportsList());
      stream.print(javaDocComment());
      stream.println(typeDeclarationString());
      StringBuffer buf = new StringBuffer();
      for(Iterator iter = getClassBodyDecls(); iter.hasNext(); ) {
        ClassBodyObject o = (ClassBodyObject)iter.next();
        jrag.AST.SimpleNode n = o.node;
        
          buf.append("    // Declared in " + o.fileName + " at line " + o.line + "\n");
          n.unparseClassBodyDeclaration(buf, name(), false); //  Fix AspectJ
          buf.append("\n\n");
      }
      stream.println(buf.toString());

      //stream.print(genMembers());
      stream.print(genAbstractSyns());
      //stream.print(genSynEquations());
      stream.print(genInhDeclarations());
      //stream.print(genInhEquations());
      
      stream.println("}");
      stream.close();
    } catch (FileNotFoundException f) {
      System.err.println("Could not create file " + file.getName() + " in " + file.getParent());
      System.exit(1);
    }
  }


  public void ASTDecl.jastAddGen(File outputDir, String grammarName, String packageName, boolean publicModifier) {
    File file = null;
    try {
      if(packageName != null && !packageName.equals("")) {
        file = new File(outputDir, packageName.replace('.', File.separatorChar) + File.separator + name() + ".java");
      }
      else {
        file = new File(outputDir, name() + ".java");
      }
      PrintStream stream = new PrintStream(new FileOutputStream(file));
      if(license != null) stream.println(license);

      if(packageName != null && !packageName.equals("")) {
        stream.println("package " + packageName + ";");
      }

      stream.print(env().genImportsList());

      if(name().equals("ASTNode")) {
        stream.print(JastAdd.VERSIONINFO);
      }
    
      stream.print(javaDocComment());
      stream.print("public ");
      if(hasAbstract()) {
        stream.print("abstract ");
      }
      stream.print("class " + name());
      if(ASTNode.java5 && (name().equals("Opt") || name().equals("List") || name().equals("ASTNode")))
        stream.print("<T extends ASTNode>");
      if(ASTNode.jjtree && name().equals("ASTNode")) {
        stream.print(" extends SimpleNode ");
      }
      else if(ASTNode.beaver && name().equals("ASTNode")) {
        stream.print(" extends beaver.Symbol ");
      }
      else if(hasSuperClass()) {
        stream.print(" extends ");
        String name = getSuperClass().name();
        if(ASTNode.java5) {
          if(name().equals("List") || name().equals("Opt"))
            name = name + "<T>";
          else if(name.equals("ASTNode"))
            name = name + "<ASTNode>";
        }
        stream.print(name);
      }
      stream.print(jastAddImplementsList());
      stream.println(" {");

      java.io.PrintWriter writer = new java.io.PrintWriter(stream);
      jjtGenFlushCache(writer);
      jjtGenCloneNode(writer, grammarName, ASTNode.jjtree, ASTNode.rewriteEnabled);
      writer.flush();

      /*
       // Now done before refinements
      jjtGen(new java.io.PrintWriter(stream), grammarName, JastAdd.jjtree, JastAdd.rewriteEnabled);
      int j = 0;
      for(Iterator iter = getComponents(); iter.hasNext(); ) {
        Components c = (Components)iter.next();
        c.jaddGen(stream, j, publicModifier, name());
        if(!(c instanceof TokenComponent)) {
          j++;
        }
      }
      */
      jastAddAttributes(stream);
      stream.println("}");
      stream.close();
    } catch (FileNotFoundException f) {
      System.err.println("Could not create file " + file.getName() + " in " + file.getParent());
      System.exit(1);
    }
  }

  public String ASTDecl.jastAddImplementsList() {
    StringBuffer buf = new StringBuffer();
    buf.append(" implements Cloneable");
    
    if(ASTNode.parentInterface) {
      for(Iterator iter = inhAttrSet(); iter.hasNext(); ) {
        buf.append(", Defines_" + (String)iter.next());
      }
    }
      
    for(Iterator iter = implementsList.iterator(); iter.hasNext(); ) {
      buf.append(", " + ((SimpleNode)iter.next()).unparse());
    }
    if(name().equals("ASTNode") && ASTNode.java5)
      buf.append(", Iterable<T>");
    return buf.toString();
  }

  public String TypeDecl.interfacesString() {
    StringBuffer s = new StringBuffer();
    Iterator iter = implementsList.iterator();
    if(iter.hasNext()) {
      s.append(((SimpleNode)iter.next()).unparse());
      while(iter.hasNext()) {
        s.append(", " + ((SimpleNode)iter.next()).unparse());
      }
    }
    return s.toString();
  }
  
  public void ASTDecl.jastAddAttributes(PrintStream s) {
    s.print(genMembers());
    s.print(genAbstractSyns());
    s.print(genSynEquations());
    s.print(genInhDeclarations());
    s.print(genInhEquations());
    if(ASTNode.rewriteEnabled)
      s.print(genRewrites());
    s.print(genCollDecls());
    s.print(genCollContributions());
    emitInhEqSignatures(s);
  }

}

  

