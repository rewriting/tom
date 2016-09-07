/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2016, Universite de Lorraine
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
  private HashSet<String> includedFiles = new HashSet<String>();
  private HashSet<String> alreadyParsedFiles = new HashSet<String>();

  public TomParser(String filename, 
      TomParserTool parserTool,
      SymbolTable symbolTable) {
    this.filename = filename;
    this.parserTool = parserTool;
    this.symbolTable = symbolTable;
    this.includedFiles = new HashSet<String>();
    this.alreadyParsedFiles = new HashSet<String>();
  }

  public String getFilename() {
    return this.filename;
  }

  public TomParserTool getParserTool() {
    return this.parserTool;
  }

  public CstProgram parse(ANTLRInputStream input) throws IOException {
    long startChrono = System.currentTimeMillis();

    System.out.print("antlr4: " + getFilename());
    tom.engine.parser.antlr4.TomIslandLexer lexer = new tom.engine.parser.antlr4.TomIslandLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tom.engine.parser.antlr4.TomIslandParser parser = new tom.engine.parser.antlr4.TomIslandParser(tokens);
    parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree

    long start = System.currentTimeMillis();
    ParseTree tree = null; //parser.start(); // parse

    // try with simpler/faster SLL(*)
    parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
    // we don't want error messages or recovery during first try
    parser.removeErrorListeners(); parser.setErrorHandler(new BailErrorStrategy());
    try {
      tree = parser.start();
      // if we get here, there was no syntax error and SLL(*) was enough;
      // there is no need to try full LL(*)
    } catch (ParseCancellationException ex) { // thrown by BailErrorStrategy
      tokens.reset(); // rewind input stream
      parser.reset();
      // back to standard listeners/handlers 
      parser.addErrorListener(ConsoleErrorListener.INSTANCE);
      parser.setErrorHandler(new DefaultErrorStrategy());
      // full now with full LL(*)
      parser.getInterpreter().setPredictionMode(PredictionMode.LL);
      tree = parser.start();
    }
    System.out.print("\tparsing:" + (System.currentTimeMillis()-start) + " ms");

    // show tree in text form
    // System.out.println(tree.toStringTree(parser));

    start = System.currentTimeMillis();
    ParseTreeWalker walker = new ParseTreeWalker();
    tom.engine.parser.antlr4.CstBuilder cstBuilder = new tom.engine.parser.antlr4.CstBuilder(); 
    walker.walk(cstBuilder, tree);
    CstProgram cst = (CstProgram) cstBuilder.getValue(tree);
    System.out.println("\tbuilding cst:" + (System.currentTimeMillis()-start) + " ms");

    //CstProgram cst = (CstProgram)CSTAdaptor.getTerm(programAsAntrlTree);

    start = System.currentTimeMillis();
    tom.engine.parser.antlr4.CstConverter cstConverter = new tom.engine.parser.antlr4.CstConverter(getParserTool());
    cst = cstConverter.convert(cst);
    //System.out.println("\tconverting cst:" + (System.currentTimeMillis()-start) + " ms");
    //System.out.println();

    //System.out.println("simplified cst = " + cst);

    return cst;

    //tom.engine.parser.antlr4.AstBuilder astBuilder = new tom.engine.parser.antlr4.AstBuilder(symbolTable);
    //Code code = astBuilder.convert(cst);

    //System.out.println("ast = " + code);



  }


}

