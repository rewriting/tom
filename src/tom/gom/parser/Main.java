package tom.gom.parser;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import tom.gom.adt.gom.GomTree;
import tom.gom.adt.gom.GomAdaptor;
import tom.gom.adt.gom.types.*;

public class Main {
	public static void main(String[] args) throws Exception {
		CharStream input = new ANTLRFileStream(args[0]);
		GomLanguageLexer lex = new GomLanguageLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lex);
		//System.out.println("tokens="+tokens);
		GomLanguageParser parser = new GomLanguageParser(tokens);
    parser.setTreeAdaptor(new GomAdaptor());
    GomTree tree = (GomTree)parser.module().getTree();
    GomModule module = (GomModule) tree.getTerm();
		System.out.println(module);
	}
}
