package jtom.parser;

import java.io.*;
import java.util.*;

import antlr.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;
import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.types.*;
import jtom.exception.*;

public class TomMainParser extends TomGenericPlugin implements TomPlugin {

    %include {  ../adt/TomSignature.tom }
    %include{ Options.tom }
    
    public static final String PARSED_SUFFIX = ".tfix.parsed"; // was previously in TomTaskInput
    public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table"; // was previously in TomTaskInput
    public static final String DEBUG_TABLE_SUFFIX = ".tfix.debug.table"; // was previously in TomTaskInput

    protected static String currentFile;
    
    private NewTargetParser parser = null;
    
    public TomMainParser(){
	myOptions = `concTomOption(OptionBoolean("parse","","",True()) // activation flag
				   );
    }

    private void p(String s){
	System.out.println(s);
    }

    
    protected static NewTargetParser newParser(String fileName) throws FileNotFoundException,IOException{
	//	try{
	HashSet includedFiles = new HashSet();
	HashSet alreadyParsedFiles = new HashSet();
	
	return newParser(fileName,includedFiles,alreadyParsedFiles);
    }

    //create new parsers
    protected static NewTargetParser newParser(String fileName,HashSet includedFiles,HashSet alreadyParsedFiles) 
	throws FileNotFoundException,IOException {
	
	// The input Stream
	DataInputStream input = new DataInputStream(new FileInputStream(new File(fileName)));
	    
	// a selector to choose the lexer to use
	TokenStreamSelector selector = new TokenStreamSelector();
	
	// create a lexer for target mode
	NewTargetLexer targetlexer = new NewTargetLexer(input);
	// create a lexer for tom mode
	NewTomLexer tomlexer = new NewTomLexer(targetlexer.getInputState());
	// create a lexer for backquote mode
	NewBQLexer bqlexer = new NewBQLexer(targetlexer.getInputState());
	
	// notify selector about various lexers
	selector.addInputStream(targetlexer,"targetlexer");
	selector.addInputStream(tomlexer, "tomlexer");
	selector.addInputStream(bqlexer, "bqlexer");
	selector.select("targetlexer");
	
	// create the parser for target mode
	// also create tom parser and backquote parser
	return new NewTargetParser(selector,fileName,includedFiles,alreadyParsedFiles);
    }
    
    public void run(){
	try{
	    long startChrono = System.currentTimeMillis();
	    
	    boolean verbose = ((Boolean)getServer().getOptionValue("verbose")).booleanValue();
	    boolean intermediate = ((Boolean)getServer().getOptionValue("intermediate")).booleanValue();
	    boolean java = ((Boolean)getServer().getOptionValue("jCode")).booleanValue();
	    boolean debug = ((Boolean)getServer().getOptionValue("debug")).booleanValue();
	    
	    %match(TomTerm term){
		FileName(n) -> { currentFile = n; }
	    }

	    if(java){
		TomJavaParser javaParser = TomJavaParser.createParser(currentFile);
		String packageName = javaParser.JavaPackageDeclaration();
		// Update taskInput
		environment().setPackagePath(packageName);
		environment().updateOutputFile();
	    }
	    else{
		environment().setPackagePath("");
	    }
	    //createParser();
	    parser = newParser(currentFile);

	    term = parser.input();

	    if(verbose) 
		System.out.println("TOM parsing phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");

	    if(environment().isEclipseMode()){
		String outputFileName = environment().getInputFile().getParent()+ File.separator + "."
		    + environment().getRawFileName()+ PARSED_TABLE_SUFFIX;
		
		Tools.generateOutput(outputFileName, symbolTable().toTerm());
	    }
	    
	    if(intermediate){
		System.out.println("intermediate");
		Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
				     + PARSED_SUFFIX, term);
		Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
				     + PARSED_TABLE_SUFFIX, symbolTable().toTerm());
	    }
        
	    if(debug)
		Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
				     + DEBUG_TABLE_SUFFIX, parser.getStructTable());
        
	    environment().printAlertMessage("TomParserPlugin");
	    if(!environment().isEclipseMode()) {
		// remove all warning (in command line only)
		environment().clearWarnings();
	    } 
	}
	catch (TokenStreamException e){
	    environment().messageError(TomMessage.getString("TokenStreamException"), new Object[]{e.getMessage()}, 
					   currentFile,  getLineFromTomParser());
	}
	catch (RecognitionException e){
	    environment().messageError(TomMessage.getString("RecognitionException"), new Object[]{e.getMessage()}, 
					   currentFile, getLineFromTomParser());
	}
	catch (TomIncludeException e){
	    environment().messageError(e.getMessage(), currentFile,  getLineFromTomParser());
	}
	catch (TomException e){
	    environment().messageError(e.getMessage(), currentFile,  getLineFromTomParser());
	}
 	catch (FileNotFoundException e){
	    environment().messageError(TomMessage.getString("FileNotFound"), new Object[]{currentFile}, 
				       currentFile, getLineFromTomParser());
	} 
	catch (Exception e){
	    e.printStackTrace();
	    environment().messageError(TomMessage.getString("UnhandledException"), 
				       new Object[]{currentFile, e.getMessage()}, 
				       currentFile, TomMessage.DEFAULT_ERROR_LINE_NUMBER);
	}
    }


    private int getLineFromTomParser() {
	if(parser == null) {
	    return TomMessage.DEFAULT_ERROR_LINE_NUMBER;
	} 
	return parser.getLine();
    }


    
}
