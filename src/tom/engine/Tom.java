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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jtom.adt.*;
import jtom.backend.*;
import jtom.checker.*;
import jtom.compiler.*;
import jtom.parser.*;
import jtom.tools.*;
import aterm.ATerm;


public class Tom {
  private TomTask initialTask;
  private TomTaskInput taskInput;
  
  private TomParser tomParser;
  private TomTypeChecker typeChecker;
  private TomSyntaxChecker syntaxChecker;
  private TomExpander expander;
  private TomKernelExpander kernelExpander;
  private TomKernelCompiler kernelCompiler;
  private TomCompiler compiler;
  private TomOptimizer optimizer;
  private TomGenerator generator;
  
  private static String version =
  "\njtom 1.3gamma\n" +
  "\nCopyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)\n" +
  "                         Nancy, France.\n";
  
  private static void version() {
    System.out.println(version);
  }
  
  private static void usage() {
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
  }
  
  public static void main(String args[]) {
    Tom tomCompiler = new Tom(args);
    tomCompiler.run();
  }
  
  public void setTaskInput(TomTaskInput input) {
	taskInput = input;
  }
  
  public void run() {
    initialTask.process(taskInput);
  }
  
  public Tom(String args[]) {
    String inputSuffix     = ".t";
    
    // Create the base taskInput
	taskInput = new TomTaskInput();
	
	// Processing the input arguments
    List importList = new ArrayList();
    if(args.length < 1) {// Need at least one argument
      usage();
    } else {
      for(int i=0; i < args.length; i++) { 
        if(args[i].charAt(0) != '-') {// Suppose this is the input filename (*[.t]) that should never start with "
          if(args[i].endsWith(inputSuffix)) {
			taskInput.inputFileName = args[i].substring(0,args[i].length()-(inputSuffix.length()));
          } else {
			taskInput.inputFileName = args[i];
          }
        } else {// This is on option
          if(args[i].equals("--version") || args[i].equals("-V")) {
            version();
          } else if(args[i].equals("--help") || args[i].equals("-h")) {
            usage();
		  } else if(args[i].equals("--import") || args[i].equals("-I")) {
			importList.add(new File(args[++i]));
          } else if(args[i].equals("--cCode") || args[i].equals("-c")) {
            taskInput.setJCode(false);
			taskInput.setECode(false);
			taskInput.setCCode(true);
            taskInput.setOutputSuffix(".tom.c");
          } else if(args[i].equals("--eCode") || args[i].equals("-e")) {
			taskInput.setJCode(false);
			taskInput.setECode(true);
			taskInput.setCCode(false);
			taskInput.setOutputSuffix(".e");
			taskInput.setSupportedGoto(false);
			taskInput.setSupportedBlock(false);
		  } else if(args[i].equals("--noOutput") || args[i].equals("-o")) {
			taskInput.setPrintOutput(false);
          } else if(args[i].equals("--doCompile") || args[i].equals("-C")) {
			taskInput.setDoOnlyCompile(true);            
			taskInput.setDoParse(false);            
			taskInput.setDoExpand(false);
		  } else if(args[i].equals("--optimize") || args[i].equals("-O")) {
			taskInput.setDoOptimization(true);
		  } else if(args[i].equals("--noCheck") || args[i].equals("-f")) {
			taskInput.setDoCheck(false);
		  } else if(args[i].equals("--lazyType") || args[i].equals("-l")) {
			taskInput.setStrictType(false);
          } else if(args[i].equals("--intermediate") || args[i].equals("-i")) {
            taskInput.setIntermediate(true);
		  } else if(args[i].equals("--verbose") || args[i].equals("-v")) {
			taskInput.setVerbose(true);
		  } else if(args[i].equals("--atermStat") || args[i].equals("-s")) {
			//Flags.atermStat = true;
          } else if(args[i].equals("--Wall")) {
			taskInput.setWarningAll(true);
          } else if(args[i].equals("--noWarning")) {
			taskInput.setNoWarning(true);
          } else if(args[i].equals("--demo") || args[i].equals("-d")) {
            //Flags.demo = true;            Flags.warningAll = false;
          } else if(args[i].equals("--noDeclaration") || args[i].equals("-D")) {
			taskInput.setGenDecl(false);
          } else if(args[i].equals("--pretty") || args[i].equals("-p")) {
			taskInput.setPretty(true);
          } else if(args[i].equals("--static")) {
			taskInput.setStaticFunction(true);
          } else if(args[i].equals("--debug")) {
			taskInput.setDebugMode(true);
          } else if(args[i].equals("--memory")) {
			taskInput.setDebugMemory(true);
          } else {
            System.out.println("'" + args[i] + "' is not a valid option");
            usage();
          }
        }
      }
    }
    
      // For the moment debug is only available for Java as target language
    taskInput.setDebugMode(taskInput.isJCode() && taskInput.isDebugMode());
    
    if(taskInput.inputFileName.length() == 0) {
      System.out.println("no inputfile");
      usage();
    }

    taskInput.setOutputFileName(taskInput.inputFileName+taskInput.outputSuffix);
	// basic structures
    TomSignatureFactory tomSignatureFactory = new TomSignatureFactory();
    ASTFactory   astFactory   = new ASTFactory(tomSignatureFactory);
    SymbolTable  symbolTable  = new SymbolTable(astFactory, taskInput.isCCode(), taskInput.isJCode(),taskInput.isECode());
    Statistics   statistics   = new Statistics();
    TomEnvironment environment = new TomEnvironment(tomSignatureFactory,
						    astFactory,
						    symbolTable,
						    statistics);

    String inputFileName = "";
    if(taskInput.isDoParse()) {
      inputFileName = taskInput.inputFileName + inputSuffix;
    } else if(taskInput.isDoOnlyCompile()) {
      inputFileName = taskInput.inputFileName + taskInput.expandedSuffix;
    }


    InputStream input;
    try {
      input = new FileInputStream(inputFileName);
    } catch (FileNotFoundException e) {
      System.out.println("Tom Parser:  File " + inputFileName + " not found.");
      System.out.println("No file generated.");
      return;
    }
    

    if(taskInput.isDoParse() && taskInput.isDoExpand()) {
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
        tomParser = new TomParser(new TomBuffer(inputBuffer),environment,fileList,0,inputFileName);
        initialTask = tomParser;
        
        if(taskInput.isDoCheck()) {
          syntaxChecker = new TomSyntaxChecker(environment);
          tomParser.addTask(syntaxChecker);
        }
        
          /*int nbError = Checker.getNumberFoundError();
            if(nbError > 0 ) {
            for(int i=0 ; i<nbError ; i++) {
              //System.out.println(tomChecker.getMessage(i));
              }
              
              String msg = "Tom Checker:  Encountered " + nbError +
              " errors during verification phase.";
              throw new CheckErrorException(msg);
              }*/
        
        expander = new TomExpander(environment, new TomKernelExpander(environment));
        if(taskInput.isDoCheck()) {
          syntaxChecker.addTask(expander);
          typeChecker = new TomTypeChecker(environment);
          expander.addTask(typeChecker);
        } else {
          tomParser.addTask(expander);
        }
        
		

       /* if(Flags.demo) {
          statistics.initInfoParser();
          statistics.initInfoChecker();
          statistics.initInfoVerifier();
        }*/

      } catch (FileNotFoundException e) {
        System.out.println("\nTom Parser:  File " + inputFileName + " not found.");
        System.out.println("No file generated.");
        return;
      }  catch(IOException e4) {
        System.out.println("\nNo file generated.");
        throw new InternalError("read error");
      }
    }
    
    if(taskInput.isDoCompile()) {
	  	 // We need a compiler
	  compiler = new TomCompiler(environment, new TomKernelCompiler(environment, taskInput.isSupportedBlock(), taskInput.isSupportedGoto(), taskInput.isDebugMode()));
      try {
        if(taskInput.isDoOnlyCompile()) {
          ATerm fromFileExpandTerm = null;
          fromFileExpandTerm = tomSignatureFactory.readFromFile(input);
          TomTerm expandedTerm = tomSignatureFactory.TomTermFromTerm(fromFileExpandTerm);
          try {
            input = new FileInputStream(inputFileName+".table");
          } catch (FileNotFoundException e) {
            System.out.println("Tom Compiler:  File " + inputFileName + " not found.");
            System.out.println("No file generated.");
            return;
          }
          ATerm fromFileSymblTable = null;
          fromFileSymblTable = tomSignatureFactory.readFromFile(input);
          TomSymbolTable symbTable = tomSignatureFactory.TomSymbolTableFromTerm(fromFileSymblTable);
          symbolTable.regenerateFromTerm(symbTable);
          // This is the initial task
          initialTask = compiler;
          taskInput.setTerm(expandedTerm);
        } else {
          if(taskInput.isDoCheck()) {
            typeChecker.addTask(compiler);
          }
          else {
            expander.addTask(compiler);
          }
        }
        
        generator = new TomGenerator(environment);
        if (taskInput.isDoOptimization()) {
          optimizer = new TomOptimizer(environment);
          compiler.addTask(optimizer);
          optimizer.addTask(generator);
        } else {
          compiler.addTask(generator);
        }
        
          /*        if(Flags.demo) {
                    statistics.initInfoCompiler();
                    statistics.initInfoGenerator();
                    }*/
      } catch(IOException e) {
        System.out.println("No file generated.");
        throw new InternalError("read error");
      }
    }
      /*
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
        }*/
  }
/*
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
*/
} // class Tom
