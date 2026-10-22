/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2017, Universite de Lorraine
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.parser.tomjava;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import tom.engine.parser.TomParserTool;
import tom.engine.adt.cst.types.*;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.adt.gom.types.*;

public class TomParser {
  private String filename;
  private Logger logger;
  
  private static HashMap<String,CstBlockList> parsedFiles = new HashMap<String,CstBlockList>();
  
  public final static int JAVA_TOP_LEVEL = 0;
  public final static int JAVA_DECLARATIONS_LEVEL = 1;
  public final static int JAVA_EXPRESSION_LEVEL = 2;

  public TomParser(String filename, Logger logger) {
    this.filename = filename;
    this.logger = logger;
  }

  public String getFilename() {
    return this.filename;
  }
  
  public Logger getLogger() {
    return this.logger;
  }

  public CstBlockList parse(ANTLRInputStream input, int parseLevel, TomParserTool parserTool) throws IOException {
    //System.out.println("\tTomParser.parse: " + getFilename());

    if(parsedFiles.containsKey(getFilename())) {
      //System.out.println("\tin cache: 0ms");
      //return parsedFiles.get(getFilename());
    }

    tom.engine.parser.tomjava.TomJavaLexer lexer = new tom.engine.parser.tomjava.TomJavaLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tom.engine.parser.tomjava.TomJavaParser parser = new tom.engine.parser.tomjava.TomJavaParser(tokens);
    parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree

    long start = System.currentTimeMillis();
    ParseTree tree = null; //parser.start(); // parse

    // try with simpler/faster SLL(*)
    parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
    // we don't want error messages or recovery during first try
    parser.removeErrorListeners();
    parser.setErrorHandler(new BailErrorStrategy());
    //try {
    //	if(parseLevel == JAVA_DECLARATIONS_LEVEL) {
    //		tree = parser.declarations();
    //	} else { //JAVA_TOP_LEVEL
    //	  tree = parser.compilationUnit();
    //	}
      // if we get here, there was no syntax error and SLL(*) was enough;
      // there is no need to try full LL(*)
    //} catch (ParseCancellationException ex) { // thrown by BailErrorStrategy
      //System.out.println("\n*** SLL parsing failed on: " + ex + "\n");

      tokens.reset(); // rewind input stream
      parser.reset();
      // back to standard listeners/handlers 
      parser.addErrorListener(ConsoleErrorListener.INSTANCE);
      parser.setErrorHandler(new DefaultErrorStrategy());
      // full now with full LL(*)
      parser.getInterpreter().setPredictionMode(PredictionMode.LL);
      switch(parseLevel) {
        case JAVA_DECLARATIONS_LEVEL:
          //System.out.println("\nparse declarations: " + getFilename());
          tree = parser.declarationsUnit();
          break;
        case JAVA_EXPRESSION_LEVEL:
          //System.out.println("\nparse expression: " + getFilename());
          tree = parser.expressionUnit();
          break;
        case JAVA_TOP_LEVEL:
          //System.out.println("\nparse compilationUnit: " + getFilename());
          tree = parser.compilationUnit();
          break;
      }

    //}
    //System.out.println("\tparsing:" + (System.currentTimeMillis()-start) + " ms");

    // show tree in text form
    // System.out.println(tree.toStringTree(parser));

    start = System.currentTimeMillis();
    ParseTreeWalker walker = new ParseTreeWalker();
    tom.engine.parser.tomjava.CstBuilder cstBuilder = new tom.engine.parser.tomjava.CstBuilder(getFilename(),tokens); 
    walker.walk(cstBuilder, tree);
    CstBlockList cst = (CstBlockList) cstBuilder.getValue(tree);
    cstBuilder.cleanUsedToken(); // release memory
    //System.out.println("\tbuilding cst:" + (System.currentTimeMillis()-start) + " ms");
    //System.out.println("\tcst:" + cst);

    start = System.currentTimeMillis();
    tom.engine.parser.tomjava.CstConverter cstConverter = new tom.engine.parser.tomjava.CstConverter(parserTool);
    cst = cstConverter.convert(cst);
    //System.out.println("\tconverting cst:" + (System.currentTimeMillis()-start) + " ms");
    //System.out.println();

    //System.out.println("simplified cst = " + cst);

    parsedFiles.put(getFilename(),cst);

    return cst;
  }

  public String parseJavaPackage(ANTLRInputStream input) {
  	
    tom.engine.parser.tomjava.TomJavaLexer lexer = new tom.engine.parser.tomjava.TomJavaLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tom.engine.parser.tomjava.TomJavaParser parser = new tom.engine.parser.tomjava.TomJavaParser(tokens);
    parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree
    
    TomJavaParser.CompilationUnitContext tree = parser.compilationUnit();
    input.reset(); // rewind input stream
    // show tree in text form
    //System.out.println(tree.toStringTree(parser));
    //System.out.println(tree.getText());
    
    String res = new String();
    
    if (tree.packageDeclaration() != null) {
    	res = tree.packageDeclaration().qualifiedName().getText();
    }

    //System.out.println("package: " + res);
    return res;
  }
  
  public GomModule parseGom(ANTLRInputStream input, GomStreamManager streamManager) {
    //System.out.println("\tTomParser.parse: " + getFilename());

  	GomModule module = null;
  	
    tom.engine.parser.tomjava.TomJavaLexer lexer = new tom.engine.parser.tomjava.TomJavaLexer(input);
    lexer.mode(lexer.GOM_INSIDE);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tom.engine.parser.tomjava.TomJavaParser parser = new tom.engine.parser.tomjava.TomJavaParser(tokens);

    try {
    	parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree

    	long start = System.currentTimeMillis();
    	ParseTree tree = parser.gomUnit();

    	start = System.currentTimeMillis();
    	ParseTreeWalker walker = new ParseTreeWalker();
    	tom.engine.parser.tomjava.CstBuilder cstBuilder = new tom.engine.parser.tomjava.CstBuilder(getFilename(),tokens, streamManager); 
    	walker.walk(cstBuilder, tree);
    	module = (GomModule) cstBuilder.getValue(tree);
    	cstBuilder.cleanUsedToken(); // release memory
    	//System.out.println("\tbuilding cst:" + (System.currentTimeMillis()-start) + " ms");
    	//System.out.println("\tcst:" + cst);
    	
    	java.io.StringWriter swriter = new java.io.StringWriter();
    	
    	try {
    	  tom.library.utils.Viewer.toTree(module,swriter);
    	  GomMessage.fine(getLogger(), getFilename(), 0, GomMessage.parsedModules, swriter);
    	} catch (java.io.IOException e) {}
    	
    	if (module == null) {
    	  GomMessage.error(getLogger(),getFilename(),lexer.getLine(),GomMessage.detailedParseException);
    	}
    } catch (RecognitionException re) {
    	GomMessage.error(getLogger(),getFilename(),lexer.getLine(),GomMessage.detailedParseException,re.toString());
    }

    return module;
  }

}

