/* Generated by TOM (version 2.6alpha): Do not edit this file *//*
 * ANTLR Mapper
 * 
 * Copyright (c) 2000-2008, INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Yoann Toussaint    e-mail: Yoann.Toussaint@loria.fr
 * 
 **/

package tom.antlrmapper;

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

public class ATermAST extends CommonAST {

  private int col = 0;		
  private int line = 0;		

  public void initialize(Token tok) {
    super.initialize(tok);
    line = tok.getLine();
    col = tok.getColumn();
  }	

  public void initialize (AST ast) {
    super.initialize(ast);
    if (ast instanceof ATermAST) {
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

  public ATerm genATermFromAST(Map tokenTypeNames) {    	
    return getATerm(tokenTypeNames);
  }
  
  public ATerm getATerm(Map tokenTypeNames) {    	
    return makeNode(
	getNameFromType(tokenTypeNames,getType()),
	getText(),
	getLine(),
	getColumn(),
	getATermList(this,tokenTypeNames));
  }

  public ATermList getATermList(ATermAST t, Map tokenTypeNames) {
    ATermList children = SingletonFactory.getInstance().makeList();
    if (t == null ) {
      return children;	
    }

    ATermAST tmp = (ATermAST)t.getFirstChild();
    // only if the node has children
    if (tmp != null) {			
      children = children.append(tmp.getATerm(tokenTypeNames));
      while((tmp = (ATermAST)tmp.getNextSibling())!=null) {
	children = children.append(tmp.getATerm(tokenTypeNames));
      }
    }

    return children;		
  }

  private String getNameFromType(Map tokenTypeNames, int astType) {
    return (String)tokenTypeNames.get(new Integer(astType));
  }

}
