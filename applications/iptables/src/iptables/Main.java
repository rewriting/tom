package iptables;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import iptables.analyser.types.*;
/*
import iptables.analyser.AnalyserAdaptor;
import iptables.iptables.IptablesAdaptor;
*/
import iptables.ast.IptablesListParserAstAdaptor;
import iptables.ast.IptablesCmdParserAstAdaptor;
import iptables.iptableslist.types.*;
import iptables.iptablescmd.types.*;
import iptables.firewall.types.*;
import java.util.*;
import java.io.*;
import org.kohsuke.args4j.*;


public class Main {
	private static final int NONE=-1, ALL=0, IPTABLES=1, PF=2, IPFW=3;
	protected static MainOptions options = new MainOptions();
	
	public static void main(String[] args) {
		CmdLineParser optionParser = new CmdLineParser(options);
		int outlang = NONE;
		optionParser.setUsageWidth(80);
		try {
			// parse the arguments.
			optionParser.parseArgument(args);
			//if( options.arguments.isEmpty() ) {
			if( options.help || options.h ) {
				throw new CmdLineException("Help");
			}
			if (options.lang.compareTo("all") == 0)
				outlang = ALL;
			else if (options.lang.compareTo("iptables") == 0)
				outlang = IPTABLES;
			else if ((options.lang.compareTo("packetfilter") == 0) 
				|| (options.lang.compareTo("pf") == 0))
				outlang = PF;
			else if (options.lang.compareTo("ipfw") == 0)
				outlang = IPFW;
			else if (options.lang.compareTo("none") == 0)
				outlang = NONE;
			else {
				System.err.println("error: Unknown language '" 
					+ options.lang + "'");
				return;
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
			IptablesListParserLexer lexer = 
				new IptablesListParserLexer(
					new ANTLRInputStream(fileinput));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			IptablesListParserParser ruleParser = 
				new IptablesListParserParser(tokens);
			Tree b1 = (Tree) ruleParser.file().getTree();
			FirewallRules inst = 
				(FirewallRules) IptablesListParserAstAdaptor.getTerm(b1);
			if (options.debug)
				System.out.println("***inst = " + inst + "\n");

			Rules rs = (new IptablesListWrapper()).wrap(inst);
			if (options.debug)
				System.out.println("***rules = " + rs + "\n");

			rs = Analyser.checkIntegrity(rs);
			if (options.debug)
				System.out.println("***new rules = " + rs + "\n");

			switch (outlang) {
			case ALL:
				System.out.println("### Iptables ###");
				(new IptablesPrinter()).prettyPrinter(rs);
				System.out.println("### Packet Filter ###");
				(new PacketFilterPrinter()).prettyPrinter(rs);
				break;
			case IPTABLES:
				(new IptablesPrinter()).prettyPrinter(rs);
				break;
			case PF:
				(new PacketFilterPrinter()).prettyPrinter(rs);
				break;
			default:
				break;
			}

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
