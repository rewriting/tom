package tom.engine.newparser.util;

import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTree;
import static tom.engine.newparser.parser.miniTomLexer.*;

public class TreeFactory {

  // non-instanciable
  private TreeFactory() {;}
 
  private static CommonTreeAdaptor adaptor = new CommonTreeAdaptor();

  public static CommonTree makeTree(int type, String text, CommonTree... childList) {
  
    CommonTree res = (CommonTree)adaptor.create(type, text);
    adaptor.setParent(res, res);

    for(CommonTree child : childList) {
      res.addChild(child);
    }

    return res;
  }

  public static CommonTree makeRootTree(int type, String text, CommonTree... childlist) {
    CommonTree res = makeTree(type, text, childlist);
    adaptor.becomeRoot(res, adaptor.nil());
    return res;
  }

  public static CommonTree makeOptions(String sourceName, int firstCharLine,
    int firstCharColumn, int lastCharLine, int lastCharColumn) {

    return
    makeTree(CstOptionList, "CstOptionList",
      makeTree(CstSourceFile, "CstSourceFile",
        makeTree(NodeString, sourceName)),
      makeTree(CstStartLine, "CstStartLine",
        makeTree(NodeInt, ""+firstCharLine)),
      makeTree(CstStartColumn, "CstStartColumn",
        makeTree(NodeInt, ""+firstCharColumn)),
      makeTree(CstEndLine, "CstEndLine",
        makeTree(NodeInt,""+lastCharLine)),
      makeTree(CstEndColumn, "CstEndColumn",
        makeTree(NodeInt,""+lastCharColumn)));
  }
}
