/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom;

import java.util.*;
import java.io.*;

import aterm.*;
import jtom.tools.*;
import jtom.compiler.*;
import jtom.checker.*;
import jtom.parser.*;
import jtom.backend.*;
import jtom.exception.*;
import jtom.adt.*;


public class Tom {
  private static String version =
  "jtom 1.3beta\n\n" +
  "Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)\n" +
  "                         Nancy, France.\n";
  
  private static void usage() {
    if(Flags.version) {
      System.out.println(version);
      System.exit(0);
    }
    System.out.println("Tom usage:");
    System.out.println("\tjava jtom.Tom [options] inputfile");
    System.out.println("Options:");
    System.out.println("\t--help \t\t| -h:\tShow this help");
    System.out.println("\t--cCode \t| -c:\tGenerate C code");
    System.out.println("\t--eCode \t| -e:\tGenerate Eiffel code");
    System.out.println("\t--version \t| -V:\tPrint version");
    System.out.println("\t--verbose \t| -v:\tSet verbose mode on");
    System.out.println("\t--intermediate\t| -i:\tGenerate intermediate files");
    System.out.println("\t--noOutput \t| -o:\tDo not generate code");
    System.out.println("\t--noDeclaration | -D:\tDo not generate code for declarations");
    System.out.println("\t--doCompile \t| -C:\tStart after type-checking");
    System.out.println("\t--noCheck \t| -f:\tDo not verify correctness");
    System.out.println("\t--Wall\t\t\tPrint all warnings");
    System.out.println("\t--noWarning\t\tDo not print warning");
    System.out.println("\t--lazyType \t| -l:\tUse universal type");
    System.out.println("\t--demo \t\t| -d:\tRun demo mode");
    System.out.println("\t--import <path> | -I:\tPath for %include");
    System.out.println("\t--pretty\t| -p:\tGenerate readable code");
    System.out.println("\t--atermStat \t| -s:\tPrint internal ATerm statistics");
    System.out.println("\t--optimize \t| -O:\tOptimized generated code");
    System.out.println("\t--static\t\tGenerate static functions");
    System.out.println("\t--debug\t\t\tGenerate debug primitives");
    System.out.println("\t--memory\t\tAdd memory management while debugging (not correct with list matching)");
    System.exit(0);
  }

  public static void main(String args[]) {
    String fileName        = "";
    String inputSuffix     = ".t";
    String outputSuffix    = ".java";
    String parsedSuffix    = ".tfix.parsed";
    String expandedSuffix  = ".tfix.expanded";
    String compiledSuffix  = ".tfix.compiled";
    String optimizedSuffix  = ".tfix.optimized";
    String parsedTableSuffix = ".tfix.parsed.table";
    String expandedTableSuffix = ".tfix.expanded.table";
    String debugMatchTableSuffix = ".tfix.debug.table";
    
    List importList = new ArrayList();    
    
    if(args.length >= 1) {
      for(int i=0; i < args.length; i++) { 
	if(args[i].charAt(0) == '-') {
          if(args[i].equals("--version") || args[i].equals("-V")) {
            Flags.version = true;
            usage();
          } else if(args[i].equals("--noOutput") || args[i].equals("-o")) {
	    Flags.printOutput = false;
          } else if(args[i].equals("--verbose") || args[i].equals("-v")) {
	    Flags.verbose = true;
          } else if(args[i].equals("--atermStat") || args[i].equals("-s")) {
	    Flags.atermStat = true;
          } else if(args[i].equals("--cCode") || args[i].equals("-c")) {
            Flags.jCode = false;
            Flags.cCode = true;
            outputSuffix = ".tom.c";
          } else if(args[i].equals("--eCode") || args[i].equals("-e")) {
            Flags.eCode = true;
            Flags.jCode = false;
            Flags.supportedGoto = false;
            Flags.supportedBlock = false;
            outputSuffix = ".e";
          } else if(args[i].equals("--doCompile") || args[i].equals("-C")) {
            Flags.doOnlyCompile = true;
            Flags.doParse = false;
            Flags.doExpand = false;
          } else if(args[i].equals("--intermediate") || args[i].equals("-i")) {
            Flags.intermediate = true;
          } else if(args[i].equals("--Wall")) {
            Flags.warningAll = true;
          } else if(args[i].equals("--noWarning")) {
            Flags.noWarning = true;
          } else if(args[i].equals("--noCheck") || args[i].equals("-f")) {
            Flags.doCheck = false;
          } else if(args[i].equals("--lazyType") || args[i].equals("-l")) {
            Flags.strictType = false;
	  } else if(args[i].equals("--demo") || args[i].equals("-d")) {
	    Flags.demo = true;
            Flags.warningAll = false;
	  } else if(args[i].equals("--noDeclaration") || args[i].equals("-D")) {
	    Flags.genDecl = false;
	  } else if(args[i].equals("--import") || args[i].equals("-I")) {
            i++;
            File importFile = new File(args[i]);
            importList.add(importFile);
          } else if(args[i].equals("--pretty") || args[i].equals("-p")) {
	    Flags.pretty = true;
          } else if(args[i].equals("--optimize") || args[i].equals("-O")) {
	    Flags.doOptimization = true;
          } else if(args[i].equals("--static")) {
	    Flags.staticFunction = true;
          } else if(args[i].equals("--debug")) {
	    Flags.debugMode = true;
          } else if(args[i].equals("--memory")) {
	    Flags.debugMemory = true;
          } else if(args[i].equals("--help") || args[i].equals("-h")) {
	    usage();
          } else {
            System.out.println("'" + args[i] + "' is not a valid option");
            usage();
          }
        } else {
          if(args[i].endsWith(inputSuffix)) {
            fileName = args[i].substring(0,args[i].length()-(inputSuffix.length()));
          } else {
            fileName = args[i];
          }
        }
      }
    } else {
      usage();
    }

    Flags.debugMode = Flags.jCode && Flags.debugMode;
      
    if(fileName.length() == 0) {
      System.out.println("no inputfile");
      usage();
    }

    TomSignatureFactory tomSignatureFactory = new TomSignatureFactory();
    ASTFactory   astFactory   = new ASTFactory(tomSignatureFactory);
    SymbolTable  symbolTable  = new SymbolTable(astFactory);
    Statistics   statistics   = new Statistics();

    TomEnvironment environment = new TomEnvironment(tomSignatureFactory,
						    astFactory,
						    symbolTable,
						    statistics);  
	
    String inputFileName = "";

    if(Flags.doParse) {
      inputFileName = fileName + inputSuffix;
    } else if(Flags.doOnlyCompile) {
      inputFileName = fileName + expandedSuffix;
    }

    InputStream input;
    try {
      input = new FileInputStream(inputFileName);
    } catch (FileNotFoundException e) {
      System.out.println("Tom Parser:  File " + inputFileName + " not found.");
      System.out.println("No file generated.");
      return;
    }
        
    TomTerm parsedTerm   = null;
    TomTerm expandedTerm = null;
    TomTerm compiledTerm = null;

    if(Flags.doParse && Flags.doExpand) {
      try {
          // to get the length of the file
        File file = new File(inputFileName);
        byte inputBuffer[] = new byte[(int)file.length()+1];
        input.read(inputBuffer);

        File fileList[] = new File[importList.size()];
        Iterator it = importList.iterator();
        int index=0;
        while(it.hasNext()) {
          fileList[index++] = (File)it.next();
        }
        
        TomParser tomParser = new TomParser(new TomBuffer(inputBuffer),environment,fileList,0,inputFileName);
        startChrono();
        parsedTerm = tomParser.startParsing();
        stopChrono();
        if(Flags.verbose) System.out.println("TOM parsing phase " + getChrono());
        if(Flags.intermediate) {
          generateOutput(fileName + parsedSuffix,parsedTerm);
          generateOutput(fileName + parsedTableSuffix,symbolTable.toTerm());
        }
        
        TomChecker  tomChecker = new TomChecker(environment);
        startChrono();
        tomChecker.checkSyntax(parsedTerm);
        stopChrono();
        if(Flags.verbose) {
          System.out.println("TOM syntax checking phase " + getChrono());
        }
        int nbError = tomChecker.getNumberFoundError();
        if(nbError > 0 ) {
          for(int i=0 ; i<nbError ; i++) {
            System.out.println(tomChecker.getMessage(i));
          }
          
          String msg = "Tom Checker:  Encountered " + nbError +
            " errors during verification phase.";
          throw new CheckErrorException(msg);
        }
        
	TomKernelExpander tomKernelExpander = new TomKernelExpander(environment);
	TomExpander tomExpander = new TomExpander(environment,tomKernelExpander);
        startChrono();
        expandedTerm = tomExpander.expandTomSyntax(parsedTerm);
        tomKernelExpander.updateSymbolTable();
        TomTerm context = null;
        expandedTerm  = tomExpander.expandVariable(context, expandedTerm);
        tomChecker.checkVariableCoherence(expandedTerm);
        if(Flags.debugMode) {
          expandedTerm  = tomKernelExpander.expandMatchPattern(expandedTerm);
        }
        stopChrono();
        if(Flags.verbose) {
          System.out.println("TOM expansion phase " + getChrono());
        }
        if(Flags.intermediate) {
          generateOutput(fileName + expandedSuffix,expandedTerm);
          generateOutput(fileName + expandedTableSuffix,symbolTable.toTerm());
        }

        if (Flags.debugMode) {
          generateOutput(fileName + debugMatchTableSuffix, tomParser.getStructTable());
        }
                
        if(Flags.demo) {
          statistics.initInfoParser();
          statistics.initInfoChecker();
          statistics.initInfoVerifier();
        }

      } catch (FileNotFoundException e) {
        System.out.println("\nTom Parser:  File " + inputFileName + " not found.");
        System.out.println("No file generated.");
        return;
      } catch(ParseException e1) {
        System.out.println(e1);
        System.out.println("\nTom Parser:  Encountered errors during parsing.");
        System.out.println("No file generated.");
        return;
      } catch(CheckErrorException e2) {
        System.out.println(e2);
        System.out.println("No file generated.");
        return;
      } catch(TomException e3) {
        System.out.println(e3);
        System.out.println("\nTom:  Encountered errors during compilation.");
        System.out.println("No file generated.");
        return;
      } catch(IOException e4) {
        System.out.println("\nNo file generated.");
        throw new InternalError("read error");
      }
    }
    
    if(Flags.doCompile) {
      try {
        if(Flags.doOnlyCompile) {
          ATerm fromFileExpandTerm = null;
          fromFileExpandTerm = tomSignatureFactory.readFromFile(input);
          expandedTerm = (TomTerm) TomTerm.fromTerm(fromFileExpandTerm);
          try {
            input = new FileInputStream(inputFileName+".table");
          } catch (FileNotFoundException e) {
            System.out.println("Tom Compiler:  File " + inputFileName + " not found.");
            System.out.println("No file generated.");
            return;
          }
          ATerm fromFileSymblTable = null;
          fromFileSymblTable = tomSignatureFactory.readFromFile(input);
          TomSymbolTable symbTable = (TomSymbolTable) TomSymbolTable.fromTerm(fromFileSymblTable);
          symbolTable.regenerateFromTerm(symbTable);
        }

        TomKernelCompiler tomKernelCompiler = new TomKernelCompiler(environment);
        TomCompiler tomCompiler = new TomCompiler(environment,tomKernelCompiler);
        startChrono();
        compiledTerm = tomCompiler.preProcessing(expandedTerm);
        
        compiledTerm = tomKernelCompiler.compileMatching(compiledTerm);
          //System.out.println("pass2 =\n" + compiledTerm);
        compiledTerm = tomKernelCompiler.postProcessing(compiledTerm);
          //System.out.println("postProcessing =\n" + compiledTerm);
        stopChrono();
        if(Flags.verbose) {
          System.out.println("TOM compilation phase " + getChrono());
        }
        if(Flags.intermediate) {
          generateOutput(fileName + compiledSuffix,compiledTerm);
        }

        if (Flags.doOptimization) {
          TomOptimizer tomOptimizer = new TomOptimizer(environment);
          startChrono();
          compiledTerm = tomOptimizer.optimize(compiledTerm);
          stopChrono();
          if(Flags.verbose) System.out.println("TOM optimization phase " + getChrono());
          if(Flags.intermediate) generateOutput(fileName + optimizedSuffix,compiledTerm);   
        }
        
	TomGenerator tomGenerator = new TomGenerator(environment);
        startChrono();
        int defaultDeep = 2;
        Writer writer = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(fileName + outputSuffix)));
        OutputCode out = new OutputCode(writer);
        tomGenerator.generate(out,defaultDeep,compiledTerm);
        writer.close();
        stopChrono();
        if(Flags.verbose) {
          System.out.println("TOM generation phase " + getChrono());
        }
        if(Flags.demo) {
          statistics.initInfoCompiler();
          statistics.initInfoGenerator();
        }
      } catch(IOException e) {
        System.out.println("No file generated.");
        throw new InternalError("read error");
      }
    }
    
    if(Flags.atermStat) {
      System.out.println("\nStatistics:\n" + tomSignatureFactory);
    }

    if(Flags.demo) {
	TomDemo view = new TomDemo();
	view.complete(statistics.infoParser,
                      statistics.infoChecker,
                      statistics.infoVerifier,
                      statistics.infoCompiler,
                      statistics.infoGenerator);
	view.info(version,fileName);
	view.pack(); 
	view.setSize(550,900);
	view.setVisible(true);
    }
  }

  private static long startChrono,endChrono;
  private static void startChrono() {
    startChrono = System.currentTimeMillis();
  }
  private static void stopChrono() {
    endChrono = System.currentTimeMillis();
  }
  private static String getChrono() {
    return "(" + (endChrono-startChrono) + " ms)";
  }

  private static void generateOutput(String outputFileName, ATerm ast) {
    try {
      OutputStream output = new FileOutputStream(outputFileName);
      Writer       writer = new BufferedWriter(new OutputStreamWriter(output));
      writer.write(ast.toString());
      writer.flush();
      writer.close();
    } catch(IOException e) {
      System.out.println("write error");
      e.printStackTrace();
    }
  }
 
}
