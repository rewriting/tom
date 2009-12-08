package iptables;

import iptables.analyser.types.*;

public abstract class Printer {
	protected void printOpt(String optname) {
		if (optname.length() > 0)
			System.out.print(optname + " ");
	}

	protected void print2Opt(String optname, String arg) {
		if ((optname.length() > 0) && (arg.length() > 0))
			System.out.print(optname + " " + arg + " ");
	}

	protected void printStr(String str) {
		if (str.length() > 0)
			System.out.print(str);
	}

	protected void printNewLine() { System.out.println(""); }

	protected abstract void prettyPrinter(Rules rs);
}
