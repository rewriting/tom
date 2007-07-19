package parser;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRStringStream;
import parser.rule.RuleTree;
import parser.rule.RuleAdaptor;

public class Main {

	public static void main(String[] args) {
		try {
      String ruleCode = "a() -> b()\n";
      RuleLexer lexer = new RuleLexer(new ANTLRStringStream(ruleCode));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
			RuleParser parser = new RuleParser(tokens);
      parser.setTreeAdaptor(new RuleAdaptor());
			// Parse the input expression
			RuleTree b = (RuleTree) parser.ruleset().getTree();
			System.out.print("Result = " + b.getTerm());
		} catch (Exception e) {
			System.err.println("exception: " + e);
			return;
		}
	}
}

