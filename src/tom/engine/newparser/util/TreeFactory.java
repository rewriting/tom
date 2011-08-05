package tom.engine.newparser.util;

import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;

public class TreeFactory {

  // non-instanciable
  private TreeFactory() {;}
  
/*
  public static Tree makeTree(int type, String text) {


    return res;
  }
*/
  public static Tree makeTree(int type, String text, Tree... childList) {
  
    CommonTreeAdaptor adaptor = new CommonTreeAdaptor();
    
    Tree res = (Tree)adaptor.create(type, text);
    adaptor.setParent(res, res);

    for(Tree child : childList) {
      res.addChild(child);
    }

    return res;
  }
}
