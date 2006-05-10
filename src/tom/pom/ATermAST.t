package tom.pom;

import antlr.collections.AST;
import antlr.collections.ASTEnumeration;
import antlr.collections.impl.ASTEnumerator;
import antlr.collections.impl.Vector;

import antlr.*;

import java.io.Serializable;
import java.io.IOException;
import java.io.Writer;

import java.util.*;

import aterm.*;
import aterm.pure.*;

public class ATermAST extends CommonAST{

  private int col = 0,line = 0;		

  public void initialize(Token tok) {
    super.initialize(tok);

    line = tok.getLine();
    col = tok.getColumn();
  }	

  public void initialize (AST ast) {
    super.initialize(ast);

    if (ast instanceof ATermAST){
      col = ((ATermAST)ast).getColumn();
      line = ((ATermAST)ast).getLine();        	
    }        
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return col;
  }

  private ATerm makeNode(String nodeName, String nodeText, int nodeLine, int nodeColumn, ATermList nodeChildren) {
    ATermFactory factory = SingletonFactory.getInstance();
    return factory.makeAppl(factory.makeAFun(nodeName,2,false),
          factory.makeAppl(factory.makeAFun("NodeInfo",3,false),
            factory.makeAppl(factory.makeAFun(nodeText,0,true)),
            factory.makeInt(nodeLine),
            factory.makeInt(nodeColumn)
            ),
          nodeChildren);

  }

  public ATerm genATermFromAST(Map tokenTypeNames){    	
    return makeNode(getNameFromType(tokenTypeNames,getType()),getText(),getLine(),getColumn(),getATermChildrenForAST(this,tokenTypeNames));
  }

  private ATermList getATermChildrenForAST(ATermAST t, Map tokenTypeNames){

    ATermList children = SingletonFactory.getInstance().makeList();

    if (t == null ){
      return children;	
    }

    ATermAST tmp = (ATermAST)t.getFirstChild();
    // only if the node has children
    if (tmp != null) {			

      children = children.append(makeNode(getNameFromType(tokenTypeNames,tmp.getType()),tmp.getText(),tmp.getLine(),tmp.getColumn(),getATermChildrenForAST(tmp,tokenTypeNames)));

      while((tmp = (ATermAST)tmp.getNextSibling())!=null){				
        children = children.append(makeNode(getNameFromType(tokenTypeNames,tmp.getType()),tmp.getText(),tmp.getLine(),tmp.getColumn(),getATermChildrenForAST(tmp,tokenTypeNames)));
      }
    }

    return children;		
  }

  private String getNameFromType(Map tokenTypeNames, int astType){
    return (String)tokenTypeNames.get(new Integer(astType));
  }

}
