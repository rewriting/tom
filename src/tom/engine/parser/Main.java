import java.io.*;

import antlr.collections.AST;
import antlr.collections.impl.*;
import antlr.debug.misc.*;
import antlr.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;
import jtom.adt.tomsignature.*;

class Main {
    // Define a selector that can switch from java to javadoc; make visible to lexers
    
    static BufferedWriter writer;

    static TokenStreamSelector selector = new TokenStreamSelector();
    
    Factory tsf = null;

    private static TomEnvironment environment() {
  	return TomEnvironment.getInstance();
    }

    private static TomTaskInput getInput() {
	return TomTaskInput.getInstance();
    }
    
    public Main(){
	tsf = new Factory(new PureFactory());
	TomTaskInput.create();
	jtom.tools.ASTFactory astFactory = new jtom.tools.ASTFactory(tsf);
	SymbolTable symbolTable = new SymbolTable(astFactory);
	TomEnvironment.create(tsf, astFactory, symbolTable);
    }

    private void init(){
	environment().init();
	getInput().init();
    }
    
    public static void main(String[] args) {
	
	Main main = new Main();
	main.init();

      try {
      
	  writer = new BufferedWriter(new FileWriter("Test/myAST"));

	  // open a simple stream to the input
	//      DataInputStream input = new DataInputStream(System.in);
	File file = new File(args[0]);
	System.out.println("---- " + file);

	DataInputStream input = new DataInputStream(new FileInputStream(file));

	// create javadoc lexer; attach to same shared input state as java lexer
	NewTargetLexer targetlexer = new NewTargetLexer(input);
	
	// attach java lexer to the input stream, which also creates a shared input state object
	NewTomLexer tomlexer = new NewTomLexer(targetlexer.getInputState());
	NewBQLexer bqlexer = new NewBQLexer(targetlexer.getInputState());

      // notify selector about various lexers; name them for convenient reference later
      selector.addInputStream(targetlexer,"targetlexer");
      selector.addInputStream(tomlexer, "tomlexer");
      selector.addInputStream(bqlexer, "bqlexer");
      selector.select("targetlexer"); // start with main java lexer

      // Create parser attached to selector
      NewTargetParser parser = new NewTargetParser(selector, file.getAbsolutePath());
      
      // Pull in one or more int decls with optional javadoc
      parser.input();

      writer.close();

      /*
      // spin thru all tokens generated via the SELECTOR.
      Token t;
      while ( (t=selector.nextToken()).getType()!=main.EOF ) {
	System.out.println(t.toString());
      }
      */
    }
    catch(Exception e) {
      System.err.println("exception: "+e);
      e.printStackTrace(System.err);   // so we can get stack trace
    }
  }

}
