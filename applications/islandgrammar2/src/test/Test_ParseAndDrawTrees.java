package test;

import java.io.BufferedInputStream;
import java.io.IOException;

import newparser.HostParser;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import debug.HostParserDebugger;

public class Test_ParseAndDrawTrees {

	public static void main(String args[]) throws IOException{
		
    HostParserDebugger.turnOn();
	  
	  
	  if(System.getenv("TOM_HOME")==null){
	     throw new RuntimeException("$TOM_HOME is not set...");
	  }
	  
		final String TOM_HOME = System.getenv("TOM_HOME")+"/"; 
		final String RLT_PATH = "../../applications/islandgrammar2/";
		
		final String OUTPUT_DIR = TOM_HOME+RLT_PATH+"output/";
		final String INPUT_DIR = TOM_HOME+RLT_PATH+"input/";
		
		// create output folder if it doesn't exists
		Runtime.getRuntime().exec(
		    "echo \"if [ ! -d output ]; then mkdir output; fi\" | bash");
		
		String testedFiles[];
		
		// === make a list of files to test 
		Process ls = Runtime.getRuntime().exec("ls "+INPUT_DIR);
		BufferedInputStream ls_stdout = new BufferedInputStream(ls.getInputStream());
		
		String ls_res = "";
		int ls_char;
		while((ls_char=ls_stdout.read())!=-1){
			ls_res+=(char)ls_char;
		}
		
		testedFiles = ls_res.split("\n");
		// ===
		
		// === parse and draw
		for(int i=0; i<testedFiles.length; i++){
		  if(!testedFiles[i].startsWith("_")){
		  
		  // useless if previous parsing ended well
		  HostParserDebugger.reset();
		    
		  String inputFileName = INPUT_DIR+testedFiles[i];
		  String outputFileName = OUTPUT_DIR+testedFiles[i]+".png";
		
		  System.out.println("\n> Now parsing "+testedFiles[i]);
		  
		  CharStream input = new ANTLRFileStream(inputFileName);
		  Tree tree = new HostParser().parseProgram(input);
			
			futil.ANTLRTreeDrawer.draw(outputFileName, tree);
			
			System.out.println(((CommonTree)tree).toStringTree());
			
		  }
		}
		
		/*
		CharStream input = new ANTLRFileStream(testedFile);
		Tree tree = new HostParser().parse(input);
		
		futil.ANTLRTreeDrawer.draw(pngFile, tree);
		*/
	}
}
