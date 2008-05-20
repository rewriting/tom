package jrag;

import jrag.AST.*;
import ast.AST.*;

import java.util.*;

aspect Doc {

  Map map = new LinkedHashMap();
  ArrayList list;

  before(JragParser parser): call(* JragParser.CompilationUnit()) && target(parser) && if(ASTNode.doc) {
    list = new ArrayList();
    map.put(parser.fileName, list);
  }

  after(SynDecl decl): call(void TypeDecl.addSynDecl(SynDecl)) && args(decl) && if(ASTNode.doc) {
    list.add(decl);
  }

  before(): call(void Grammar.processRefinements()) && if(ASTNode.doc) {
    for(Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
      String key = (String)iter.next();
      System.out.println("CompilationUnit: " + key);
      for(Iterator i2 = ((Collection)map.get(key)).iterator(); i2.hasNext(); ) {
        ASTNode node = (ASTNode)i2.next();
        if(node instanceof SynDecl) {
          System.out.println("  SynDecl: " + ((SynDecl)node).getName());
        }
      }
    }
  }
}
