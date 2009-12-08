package jrag;

import ast.AST.*;

import jrag.AST.*;
import jrag.AST.Token;
import jrag.AST.SimpleNode;

//import java.util.*;

public aspect Unparse {

  
  
  // Get import declarations
   
  public void SimpleNode.getImports(StringBuffer buf) {
  }
  
  public String ASTCompilationUnit.getImports() {
    StringBuffer buf = new StringBuffer();
    for(int i = 0; i < jjtGetNumChildren(); i++) {
      ((SimpleNode)jjtGetChild(i)).getImports(buf);
    }
    return buf.toString();
  }

  public void ASTImportDeclaration.getImports(StringBuffer buf) {
    unparseNoComments(buf, null);
  }


  // Unparse in aspectJ syntax

  public void SimpleNode.unparseClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {

      Token t1 = firstToken;
      Token t = new Token();
      t.next = t1;

      SimpleNode n;
      for(int i = 0; i < jjtGetNumChildren(); i++) {
        n = (SimpleNode)jjtGetChild(i);
        while(true) {
          t = t.next;
          if(t == n.firstToken) break;
          unparse(t, buf);
        }
        n.unparseClassBodyDeclaration(buf, className, aspectJ);
        t = n.lastToken;
      }

      while(t != lastToken && t != null) {
        t = t.next;
        unparse(t, buf);
      }
  }

  
  public void ASTMethodDeclaration.unparseClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {
    // MethodDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit

	  jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit) {
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      if(aspectJ)
        t.image = " " + className + ".";
      else
        t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
      super.unparse(buf, className);
    }
    else {
      super.unparseClassBodyDeclaration(buf, className, aspectJ);
    }
  }
  
  public void ASTAspectMethodDeclaration.unparseClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {
    // MethodDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit || node == null) {
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      if(aspectJ)
        t.image = " " + className + ".";
      else
        t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
      super.unparse(buf, className);
    }
    else {
      super.unparseClassBodyDeclaration(buf, className, aspectJ);
    }
  }
  public void ASTAspectMethodDeclaration.unparseAbstractClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      if(aspectJ)
        t.image = " " + className + ".";
      else
        t.image = " ";
      t2.specialToken = t;
      t1.next = t2;

      t1 = firstToken;
      t = new Token();
      t.next = t1;

      SimpleNode n;
      int lastIndex = jjtGetNumChildren() >= 3 && jjtGetChild(2) instanceof ASTNameList ? 3 : 2;
      for(int i = 0; i < lastIndex; i++) {
        n = (SimpleNode)jjtGetChild(i);
        while(true) {
          t = t.next;
          if(t == n.firstToken) break;
          unparse(t, buf);
        }
        n.unparse(buf, className);
        t = n.lastToken;
      }
      if(jjtGetNumChildren() > 1)
        buf.append(";\n");

      //while(t != lastToken) {
      //  t = t.next;
      //  unparse(t, buf);
      //}
  }
  public void ASTAspectRefineMethodDeclaration.unparseClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {
    // MethodDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit || node == null) {
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      if(aspectJ)
        t.image = " " + className + ".";
      else
        t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
      super.unparse(buf, className);
    }
    else {
      super.unparseClassBodyDeclaration(buf, className, aspectJ);
    }
  }
  public void ASTAspectRefineMethodDeclaration.unparseAbstractClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      if(aspectJ)
        t.image = " " + className + ".";
      else
        t.image = " ";
      t2.specialToken = t;
      t1.next = t2;

      t1 = firstToken;
      t = new Token();
      t.next = t1;

      SimpleNode n;
      int lastIndex = jjtGetNumChildren() >= 3 && jjtGetChild(2) instanceof ASTNameList ? 3 : 2;
      for(int i = 0; i < lastIndex; i++) {
        n = (SimpleNode)jjtGetChild(i);
        while(true) {
          t = t.next;
          if(t == n.firstToken) break;
          unparse(t, buf);
        }
        n.unparse(buf, className);
        t = n.lastToken;
      }
      if(jjtGetNumChildren() > 1)
        buf.append(";\n");

      //while(t != lastToken) {
      //  t = t.next;
      //  unparse(t, buf);
      //}
  }
  
  public void ASTAspectConstructorDeclaration.unparseClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {
    // ConstructorDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit || node == null) {
      Token t1 = ((SimpleNode)jjtGetChild(0)).firstToken;
      Token t2 = firstToken;
      while(t2.next.next.next != t1)
        t2 = t2.next;
      t2.image ="";
      t2.next.image="";
      super.unparse(buf, className);
    }
    else {
      super.unparseClassBodyDeclaration(buf, className, aspectJ);
    }
  }

  public void ASTAspectRefineConstructorDeclaration.unparseClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {
    // ConstructorDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit || node == null) {
      Token t1 = ((SimpleNode)jjtGetChild(0)).firstToken;
      Token t2 = firstToken;
      while(t2.next.next.next != t1)
        t2 = t2.next;
      t2.image ="";
      t2.next.image="";
      super.unparse(buf, className);
    }
    else {
      super.unparseClassBodyDeclaration(buf, className, aspectJ);
    }
  }


  public void ASTModifiers.unparseClassBodyDeclaration(StringBuffer buf, String className, boolean aspectJ) {
	  buf.append(unparse());
	  buf.append(" ");
  }
  
  public void ASTFieldDeclaration.unparseClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {
    // FieldDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit) {
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      if(aspectJ)
        t.image = " " + className + ".";
      else
        t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
      super.unparse(buf, className);
    }
    else {
      super.unparseClassBodyDeclaration(buf, className, aspectJ);
    }
  }
  
  public void ASTAspectFieldDeclaration.unparseClassBodyDeclaration(StringBuffer buf,
    String className, boolean aspectJ) {
    // FieldDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit || node == null) {
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      if(aspectJ)
        t.image = " " + className + ".";
      else
        t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
      super.unparse(buf, className);
    }
    else {
      super.unparseClassBodyDeclaration(buf, className, aspectJ);
    }
  }

  // Unparse a node and its children
  
  public String SimpleNode.unparse() {
    StringBuffer buf = new StringBuffer();
    unparse(buf, null);
    return buf.toString().trim();
  }
  
  public void ASTMethodDeclaration.unparse(StringBuffer buf, String className) {
    // MethodDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit) {
      // Remove optional "Class." before IdDecl in method declaration
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
    }
    super.unparse(buf, className);
  }
  public void ASTAspectMethodDeclaration.unparse(StringBuffer buf, String className) {
    // MethodDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit || node == null) {
      // Remove optional "Class." before IdDecl in method declaration
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
    }
    super.unparse(buf, className);
  }
  public void ASTAspectRefineMethodDeclaration.unparse(StringBuffer buf, String className) {
    // MethodDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit || node == null) {
      // Remove optional "Class." before IdDecl in method declaration
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
    }
    super.unparse(buf, className);
  }
  
  public void ASTFieldDeclaration.unparse(StringBuffer buf, String className) {
    // FieldDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit) {
      // Remove optional "Class." before IdDecl in field declaration 
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
    }
    super.unparse(buf, className);
  }
  public void ASTAspectFieldDeclaration.unparse(StringBuffer buf, String className) {
    // FieldDeclaration <- ClassBodyDeclaration <- 
    // ClassBody <- UnmodifiedClassDecl <- ClassDecl <- TypeDecl <- CompilationUnit
    jrag.AST.Node node = this;
    for(int i = 0; node != null && !(node instanceof ASTCompilationUnit) && i < 8; i++) {
      node = node.jjtGetParent();
    }
    if(node instanceof ASTCompilationUnit || node == null) {
      // Remove optional "Class." before IdDecl in field declaration 
      Token t1 = ((SimpleNode)jjtGetChild(0)).lastToken;
      Token t2 = ((SimpleNode)jjtGetChild(1)).firstToken;
      Token t = new Token();
      t.image = " ";
      t2.specialToken = t;
      t1.next = t2;
    }
    super.unparse(buf, className);
  }


  public void SimpleNode.unparse(StringBuffer buf, String className) {
      Token t1 = firstToken;
      Token t = new Token();
      t.next = t1;

      SimpleNode n;
      for(int i = 0; i < jjtGetNumChildren(); i++) {
        n = (SimpleNode)jjtGetChild(i);
        if(n != null) {
          while(true) {
            // unparse linked tokens until the first token of the current child is found
            t = t.next;
            if(t == n.firstToken) break;
            unparse(t, buf);
          }
          // unparse the current child
          n.unparse(buf, className);
          t = n.lastToken;
        }
      }

      while(t != lastToken && t != null) {
        t = t.next;
        unparse(t, buf);
      }
  }
          

    public void SimpleNode.unparse(Token t, StringBuffer buf) {
        if(t == null)
          return;
        Token tt = t.specialToken;
        if (tt != null) {
            while (tt.specialToken != null) tt = tt.specialToken;
            while (tt != null) {
                buf.append(addUnicodeEscapes(tt.image));
                tt = tt.next;
            }
        }
        if(t instanceof Token.GTToken) {
          buf.append(">");
        }
        else
          buf.append(t.image);
    }
  
    private String SimpleNode.addUnicodeEscapes(String str) {
        String retval = "";
        char ch;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if ((ch < 0x20 || ch > 0x7e) &&
                ch != '\t' && ch != '\n' && ch != '\r' && ch != '\f') {
                String s = "0000" + Integer.toString(ch, 16);
                retval += "\\u" + s.substring(s.length() - 4, s.length());
            } else {
                retval += ch;
            }
        }
        return retval;
     }

  public void SimpleNode.unparseComment(StringBuffer buf) {
        Token tt = firstToken.specialToken;
        if (tt != null) {
            while (tt.specialToken != null) tt = tt.specialToken;
            while (tt != null) {
                buf.append(addUnicodeEscapes(tt.image));
                tt = tt.next;
            }
        }
  }

  public String SimpleNode.unparseComment() {
    StringBuffer buf = new StringBuffer();
    unparseComment(buf);
    return buf.toString();
  }

    // create a signature used to match refined method declarations
    public String SimpleNode.signature() {
      return "";
    }
    public String ASTAspectMethodDeclaration.signature() {
      // AspectMethodDeclaration = ResultType MethodDeclarator() Block()
      return ((SimpleNode)jjtGetChild(1)).signature();
    }
    public String ASTAspectRefineMethodDeclaration.signature() {
      // AspectRefineMethodDeclaration = ResultType MethodDeclarator() Block()
      return ((SimpleNode)jjtGetChild(1)).signature();
    }
    public String ASTMethodDeclarator.signature() {
      return firstToken.image.trim() + ((SimpleNode)jjtGetChild(0)).signature();
    }

    public String ASTAspectConstructorDeclaration.signature() {
      // AspectConstructorDeclaration = FormalParameters
      return "#constructor#" + ((SimpleNode)jjtGetChild(0)).signature();
    }

    public String ASTAspectRefineConstructorDeclaration.signature() {
      // AspectRefineConstructorDeclaration = FormalParameters
      return "#constructor#" + ((SimpleNode)jjtGetChild(0)).signature();
    }

    public String ASTFormalParameters.signature() {
      StringBuffer s = new StringBuffer();
      for(int i = 0; i < jjtGetNumChildren(); i++)
        s.append(((SimpleNode)jjtGetChild(i)).signature());
      return s.toString();
    }
    public String ASTFormalParameter.signature() {
      // FormalParameter = Type VariableDeclaratorId
      return "_" + ((SimpleNode)jjtGetChild(0)).unparse().trim();
    }
      
      
  public void SimpleNode.unparseNoComments(StringBuffer buf, String className) {
      Token t1 = firstToken;
      Token t = new Token();
      t.next = t1;

      SimpleNode n;
      for(int i = 0; i < jjtGetNumChildren(); i++) {
        n = (SimpleNode)jjtGetChild(i);
        if(n != null) {
          while(true) {
            // unparse linked tokens until the first token of the current child is found
            t = t.next;
            if(t == n.firstToken) break;
            buf.append(t.image);
          }
          // unparse the current child
          n.unparseNoComments(buf, className);
          t = n.lastToken;
        }
      }

      while(t != lastToken && t != null) {
        t = t.next;
        unparse(t, buf);
      }
  }
          
}
