package utils;

import aterm.ATerm;
import aterm.ATermList;
import aterm.ATermFactory;
import aterm.pure.SingletonFactory;

import antlr.collections.AST;

public class AST2ATerm {
  public static ATerm AST2ATerm(AST ast,String[] names) {
    ATermFactory factory=SingletonFactory.getInstance();
    ATermList aterm;

    ATerm[] aterms=new ATerm[ast.getNumberOfChildren()];

    int i=0;
    for(AST child=ast.getFirstChild();child!=null;child=child.getNextSibling()) {
      aterms[i++]=AST2ATerm(child,names);
    }

    aterm=factory.makeList();
    while(i>0) {
      aterm=factory.makeList(aterms[--i],aterm);
    }
    
    return factory.makeAppl(
      factory.makeAFun(names[ast.getType()],2,false),
      factory.makeAppl(
        factory.makeAFun("NodeInfo",3,false),
        factory.makeAppl(factory.makeAFun(ast.getText(),0,false)),
        factory.makeInt(ast.getLine()), 
        factory.makeInt(ast.getColumn())),
      aterm);
  }
}
