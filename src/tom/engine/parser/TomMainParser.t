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

public class TomMainParser extends TomBase implements TomPlugin {

    %include { TomSignature.tom }
    %include{ Options.tom }
    
    //for debugging
    // private static BufferedWriter writer;

    private TomTerm term;
    private TomOptionList myOptions;
    
    private HashSet includedFileSet;
    private HashSet alreadyParsedFileSet;
    
    public static final String PARSED_SUFFIX = ".tfix.parsed"; // was previously in TomTaskInput
    public static final String PARSED_TABLE_SUFFIX = ".tfix.parsed.table"; // was previously in TomTaskInput
    public static final String DEBUG_TABLE_SUFFIX = ".tfix.debug.table"; // was previously in TomTaskInput

    protected static String currentFile;
    
    // a selector to choose the lexer to use
    protected static TokenStreamSelector selector = new TokenStreamSelector();

    private NewTargetParser parser = null;
    
    public TomMainParser(){
	myOptions = `concTomOption(OptionBoolean("parse","","",True()) // activation flag
				   );
    }

    private void p(String s){
	System.out.println(s);
    }

    private void createParser(){
	try{
	    this.includedFileSet = new HashSet();
	    this.alreadyParsedFileSet = new HashSet();
	    
	    DataInputStream input = new DataInputStream(new FileInputStream(new File(currentFile)));
	    
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
	    this.parser = new NewTargetParser(selector);
	    // create the other parsers
	    parser.init();
	}
	catch(FileNotFoundException e){
	    environment().messageError(TomMessage.getString("FileNotFound"), 
				       new Object[]{currentFile}, 
				       currentFile, 
				       getLineFromTomParser(parser)
				       );
	}
	catch(IOException e){
	    e.printStackTrace();
	    environment().messageError(TomMessage.getString("UnhandledException"), 
				       new Object[]{currentFile, e.getMessage()}, 
				       currentFile, 
				       TomMessage.DEFAULT_ERROR_LINE_NUMBER);
	}

    }

    public TomTerm startParsing() throws TomException{

	return parser.startParsing();
	    /*	catch(TokenStreamException e){
	    throw new TomException("error during parsing :\n\t"+e.getMessage());
	    }*/
	/*	catch(antlr.RecognitionException e){
	    throw new TomException("error during parsing :\n\t"+e.getMessage());
	    }*/
    }


    private static boolean testIncludedFile(String currentFile, HashSet fileSet) {
	// !(true) if the set did not already contain the specified element.
	return !fileSet.add(currentFile);
    }

    public void setInput(ATerm term){
	if (term instanceof TomTerm)
	    this.term = (TomTerm)term;
	else
	    environment().messageError(TomMessage.getString("TomTermExpected"),
				       "TomParserPlugin", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    }

    public ATerm getOutput(){
	return term;
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
	    createParser();

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
	
 	/*catch (TokenMgrError e){
	    environment().messageError(TomMessage.getString("TokenMgrError"), new Object[]{e.getMessage()}, 
				       currentFile,  getLineFromTomParser(parser));
				       } */
	
	/*	catch (TomIncludeException e){
	    environment().messageError(e.getMessage(), currentFile,  getLineFromTomParser(parser));
	    }*/
	/* 	catch (TomException e){
	    environment().messageError(e.getMessage(), currentFile,  getLineFromTomParser(parser));
	    }*/
 	catch (FileNotFoundException e){
	    environment().messageError(TomMessage.getString("FileNotFound"), new Object[]{currentFile}, 
				       currentFile, getLineFromTomParser(parser));
	} /*
 	catch (ParseException e){
	    environment().messageError(TomMessage.getString("ParseException"), new Object[]{e.getMessage()}, 
				       currentFile, getLineFromTomParser(parser));
				       } */
	catch (Exception e){
	    e.printStackTrace();
	    environment().messageError(TomMessage.getString("UnhandledException"), 
				       new Object[]{currentFile, e.getMessage()}, 
				       currentFile, TomMessage.DEFAULT_ERROR_LINE_NUMBER);
	}
    }


    private int getLineFromTomParser(NewTargetParser parser) {
	if(parser == null) {
	    return TomMessage.DEFAULT_ERROR_LINE_NUMBER;
	} 
	return parser.getLine();
    }

    public TomOptionList declareOptions(){
	return myOptions;
    }

    public TomOptionList requiredOptions(){
	return `emptyTomOptionList();
    }
    
    public void setOption(String optionName, String optionValue){
	%match(TomOptionList myOptions){
	    concTomOption(av*, OptionBoolean(n, alt, desc, val), ap*) -> { 
		if(n.equals(optionName)||alt.equals(optionName)) {
		    %match(String optionValue){
			('true') ->{ 
			    myOptions = `concTomOption(
						       av*, 
						       ap*, 
						       OptionBoolean(n, alt, desc, True())
						       ); 
			}
			('false') ->{ 
			    myOptions = `concTomOption(
						       av*, 
						       ap*, 
						       OptionBoolean(n, alt, desc, False())
						       ); 
			}
		    }
		}
	    }
	    concTomOption(av*, OptionInteger(n, alt, desc, val, attr), ap*) -> { 
		if(n.equals(optionName)||alt.equals(optionName))
		    myOptions = `concTomOption(av*, ap*, OptionInteger(n, alt, desc, Integer.parseInt(optionValue), attr));
	    }
	    concTomOption(av*, OptionString(n, alt, desc, val, attr), ap*) -> { 
		if(n.equals(optionName)||alt.equals(optionName))
		    myOptions = `concTomOption(av*, ap*, OptionString(n, alt, desc, optionValue, attr));
	    }
	}
    }
    
}
