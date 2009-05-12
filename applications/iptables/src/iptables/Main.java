package iptables;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import iptables.analyser.types.*;
/*
import iptables.analyser.AnalyserAdaptor;
import iptables.iptables.IptablesAdaptor;
*/
import iptables.ast.AstAdaptor;
import iptables.iptables.types.*;
import iptables.firewall.types.*;
import java.util.*;
import java.io.*;
import org.kohsuke.args4j.*;


public class Main {
	protected static MainOptions options = new MainOptions();

	public static void main(String[] args) {
		CmdLineParser optionParser = new CmdLineParser(options);
		optionParser.setUsageWidth(80);
		try {
			// parse the arguments.
			optionParser.parseArgument(args);
			//if( options.arguments.isEmpty() ) {
			if( options.help || options.h ) {
				throw new CmdLineException("Help");
			}
		} catch( CmdLineException e ) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.err.println(e.getMessage());
			System.err.println("java Main [options...] arguments ...");
			// print the list of available options
			optionParser.printUsage(System.err);
			System.err.println();
			return;
		}

		try {
			InputStream fileinput = System.in;
			if(options.in != null) {
				fileinput = new FileInputStream(options.in);
			}
			// Parse the input expression and build an AST
			IptablesParserLexer lexer = 
				new IptablesParserLexer(
					new ANTLRInputStream(fileinput));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			IptablesParserParser ruleParser = 
				new IptablesParserParser(tokens);
			Tree b1 = (Tree) ruleParser.file().getTree();
			FirewallRules inst = 
				(FirewallRules) AstAdaptor.getTerm(b1);
			System.out.println("inst = " + inst);

			Rules rs = (new IptablesWrapper()).wrap(inst);
			System.out.println("rules = " + rs);

			rs = Analyser.checkIntegrity(rs);
			System.out.println("new rules = " + rs);

			System.out.println("### Iptables ###");
			(new IptablesPrinter()).prettyPrinter(rs);
			System.out.println("### Packet Filter ###");
			(new PacketFilterPrinter()).prettyPrinter(rs);

			PrintStream outputfile = System.out;
			if(options.out != null) {
				outputfile = new PrintStream(options.out);
			}
			PrintStream tomoutputfile = System.out;
		} catch (Exception e) {
			System.err.println("exception: " + e);
			e.printStackTrace();
			return;
		}
	}
}
