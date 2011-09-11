package tom.engine.parser.antlr3;

import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTree;

import static tom.engine.parser.antlr3.miniTomParser.*;
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

  public static CommonTree makeOptions(String sourceName,
      int firstCharLine, int firstCharColumn, 
      int lastCharLine, int lastCharColumn) {
    return makeTree(ConcCstOption, "ConcCstOption",
                    makeTree(Cst_OriginTracking, "Cst_OriginTracking", 
                      makeTree(NodeString, sourceName),
                      makeTree(NodeInt, ""+firstCharLine),
                      makeTree(NodeInt, ""+firstCharColumn),
                      makeTree(NodeInt,""+lastCharLine),
                      makeTree(NodeInt,""+lastCharColumn)
                      ));
/*
    return makeTree(ConcCstOption, "ConcCstOption",
                    makeTree(Cst_SourceFile, "Cst_SourceFile",
                              makeTree(NodeString, sourceName)),
                    makeTree(Cst_StartLine, "Cst_StartLine",
                              makeTree(NodeInt, ""+firstCharLine)),
                    makeTree(Cst_StartColumn, "Cst_StartColumn",
                              makeTree(NodeInt, ""+firstCharColumn)),
                    makeTree(Cst_EndLine, "Cst_EndLine",
                              makeTree(NodeInt,""+lastCharLine)),
                    makeTree(Cst_EndColumn, "Cst_EndColumn",
                              makeTree(NodeInt,""+lastCharColumn)));
                              */
  }
}
