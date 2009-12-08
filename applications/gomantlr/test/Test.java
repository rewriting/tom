import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;

import gomantlr_java.types.*;

import java.io.*;

public class Test {

    //%include { ./gomantlr_java/Gomantlr_Java.tom }

    public static void main(String args[]) {
	try {
	    InputStream input;
	
	    if(args.length!=0) {
		File inputFile = new File(args[0]);
		input=new FileInputStream(inputFile);
	    } else {
		input=System.in;
	    }
	    
	    // parse tree
	    Gomantlr_JavaLexer lexer = new Gomantlr_JavaLexer(new ANTLRInputStream(input));
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    Gomantlr_JavaParser parser = new Gomantlr_JavaParser(tokens);
	    
	    Gomantlr_Java_compilationUnit cu = parser.compilationUnit();
	    
	    System.out.println(cu);
	} catch (Exception e) {
	    System.err.println("exception: " + e);
	    e.printStackTrace();
	}
    }
}
