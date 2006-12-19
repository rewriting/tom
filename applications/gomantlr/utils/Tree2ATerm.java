package utils;

import aterm.ATerm;
import aterm.ATermList;
import aterm.ATermFactory;
import aterm.pure.SingletonFactory;

import org.antlr.runtime.tree.Tree;

public class Tree2ATerm {
  public static ATerm Tree2ATerm(Tree tree,String[] names) {
    ATermFactory factory=SingletonFactory.getInstance();
    ATermList aterm;

    aterm=factory.makeList();
    for(int i=tree.getChildCount()-1;i>=0;i--) {
        aterm=factory.makeList(Tree2ATerm(tree.getChild(i),names),aterm);
    }

    if(tree.isNil()) {
        return aterm;
    } else {
        return factory.makeAppl(
         factory.makeAFun(names[tree.getType()],2,false),
         factory.makeAppl(
           factory.makeAFun("NodeInfo",3,false),
           factory.makeAppl(factory.makeAFun(tree.getText(),0,false)),
           factory.makeInt(tree.getLine()), 
           factory.makeInt(tree.getCharPositionInLine())),
         aterm);
    }
  }
}
