package tom.gom.parser;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

public class Main {
	public static void main(String[] args) throws Exception {
		CharStream input = new ANTLRFileStream(args[0]);
		GomLanguageLexer lex = new GomLanguageLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lex);
		//System.out.println("tokens="+tokens);
		GomLanguageParser parser = new GomLanguageParser(tokens);
		System.out.println(((Tree)parser.module().getTree()).toStringTree());
	}
}
