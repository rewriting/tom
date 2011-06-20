package newparser;

import org.antlr.runtime.ANTLRStringStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		HostParser parser = new HostParser();
		parser.parse(new ANTLRStringStream("bli\"co\\\"mment\"blu\"\""));
	}

}
