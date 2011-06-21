package test;

import java.io.IOException;

import newparser.HostParser;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;

import debug.HostParserDebugger;

public class Tests_drawtrees {

  public static void main(String args[]) throws IOException{
    
    CharStream input = new ANTLRFileStream(
        "/home/moi/workspace/jtom/applications/islandgrammar2/input/01_match");
    
    HostParser parser = new HostParser();
    Tree tree = parser.parse(input);
    
    futil.ANTLRTreeDrawer.draw("/home/moi/Bureau/tree.png", tree);
    
  }
  
}
