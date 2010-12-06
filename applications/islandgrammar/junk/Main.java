import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.tree.CommonTree;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

public class Main {

  public static void main(String[] args) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

    CharStream charStream = new ANTLRInputStream(input);
    parsematchLexer lexer = new parsematchLexer(charStream);
    CommonTokenStream tokens = new CommonTokenStream();
    tokens.setTokenSource(lexer);
    parsematchParser parser = new parsematchParser(tokens);
    RuleReturnScope result= parser.compilationUnit();
    CommonTree tree=(CommonTree)result.getTree();
    printTree(tree,1);
// And now what are wez supposed to do with that parser ?

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }

  }

  public static void printTree(CommonTree t, int indent) {
	if ( t != null ) {
		StringBuffer sb = new StringBuffer(indent);
		for ( int i = 0; i < indent; i++ )
			sb = sb.append("   ");
		for ( int i = 0; i < t.getChildCount(); i++ ) {
			System.out.println(sb.toString() + t.getChild(i).toString());
			printTree((CommonTree)t.getChild(i), indent+1);
		}
	}
}

}
