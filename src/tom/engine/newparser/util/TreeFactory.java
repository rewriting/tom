package tom.engine.newparser.util;

import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;

public class TreeFactory {

  // non-instanciable
  private TreeFactory() {;}
 
  private static CommonTreeAdaptor adaptor = new CommonTreeAdaptor();

  public static Tree makeTree(int type, String text, Tree... childList) {
  
    Tree res = (Tree)adaptor.create(type, text);
    adaptor.setParent(res, res);

    for(Tree child : childList) {
      res.addChild(child);
    }

    return res;
  }

  public static Tree makeRootTree(int type, String text, Tree... childlist) {
    Tree res = makeTree(type, text, childlist);
    adaptor.becomeRoot(res, adaptor.nil());
    return res;
  }
}
