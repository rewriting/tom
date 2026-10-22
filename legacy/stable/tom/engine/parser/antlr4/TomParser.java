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

package tom.engine.parser.antlr4;

import java.io.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.platform.OptionManager;
import tom.engine.exception.TomException;
import tom.engine.tools.SymbolTable;
import tom.engine.parser.TomParserTool;

import tom.engine.adt.code.types.*;
import tom.engine.adt.cst.types.*;

public class TomParser {
  private String filename;
  private TomParserTool parserTool;
  private SymbolTable symbolTable;

  private static HashMap<String,CstProgram> parsedFiles = new HashMap<String,CstProgram>();

  public TomParser(String filename, TomParserTool parserTool, SymbolTable symbolTable) {
    this.filename = filename;
    this.parserTool = parserTool;
    this.symbolTable = symbolTable;
  }

  public String getFilename() {
    return this.filename;
  }

  public TomParserTool getParserTool() {
    return this.parserTool;
  }

  public CstProgram parse(ANTLRInputStream input) throws IOException {
    //System.out.print("antlr4: " + getFilename());

    if(parsedFiles.containsKey(getFilename())) {
      //System.out.println("\tin cache: 0ms");
      //return parsedFiles.get(getFilename());
    }

    tom.engine.parser.antlr4.TomIslandLexer lexer = new tom.engine.parser.antlr4.TomIslandLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tom.engine.parser.antlr4.TomIslandParser parser = new tom.engine.parser.antlr4.TomIslandParser(tokens);
    parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree

    long start = System.currentTimeMillis();
    ParseTree tree = null; //parser.start(); // parse

    // try with simpler/faster SLL(*)
    parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
    // we don't want error messages or recovery during first try
    parser.removeErrorListeners();
    parser.setErrorHandler(new BailErrorStrategy());
    try {
      tree = parser.start();
      // if we get here, there was no syntax error and SLL(*) was enough;
      // there is no need to try full LL(*)
    } catch (ParseCancellationException ex) { // thrown by BailErrorStrategy
      System.out.println("\n*** SLL parsing failed on: " + ex + "\n");

      tokens.reset(); // rewind input stream
      parser.reset();
      // back to standard listeners/handlers 
      parser.addErrorListener(ConsoleErrorListener.INSTANCE);
      parser.setErrorHandler(new DefaultErrorStrategy());
      // full now with full LL(*)
      parser.getInterpreter().setPredictionMode(PredictionMode.LL);
      tree = parser.start();
    }
    //System.out.println("\tparsing:" + (System.currentTimeMillis()-start) + " ms");

    // show tree in text form
    // System.out.println(tree.toStringTree(parser));

    start = System.currentTimeMillis();
    ParseTreeWalker walker = new ParseTreeWalker();
    tom.engine.parser.antlr4.CstBuilder cstBuilder = new tom.engine.parser.antlr4.CstBuilder(getFilename(),tokens); 
    walker.walk(cstBuilder, tree);
    CstProgram cst = (CstProgram) cstBuilder.getValue(tree);
    cstBuilder.cleanUsedToken(); // release memory
    //System.out.println("\tbuilding cst:" + (System.currentTimeMillis()-start) + " ms");
    //System.out.println("\tcst:" + cst);

    start = System.currentTimeMillis();
    tom.engine.parser.antlr4.CstConverter cstConverter = new tom.engine.parser.antlr4.CstConverter(getParserTool());
    cst = cstConverter.convert(cst);
    //System.out.println("\tconverting cst:" + (System.currentTimeMillis()-start) + " ms");
    //System.out.println();

    //System.out.println("simplified cst = " + cst);

    parsedFiles.put(getFilename(),cst);

    return cst;
  }

  public String parseJavaPackage(ANTLRInputStream input) throws IOException {
    tom.engine.parser.antlr4.TomJavaLexer lexer = new tom.engine.parser.antlr4.TomJavaLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tom.engine.parser.antlr4.TomJavaParser parser = new tom.engine.parser.antlr4.TomJavaParser(tokens);
    parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree
    ParseTree tree = parser.start();
    input.reset(); // rewind input stream
    // show tree in text form
    //System.out.println(tree.toStringTree(parser));
    //System.out.println(tree.getText());

    String res = tree.getText();
    int len = res.length();
    int plen = "package".length();
    //remove keyword 'package' and trailing ';'
    if(len > plen+1) {
      res = res.substring(plen,len-1).trim();
    }

    //System.out.println("package: " + res);
    return res;
  }

}

