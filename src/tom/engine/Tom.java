/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

package jtom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.backend.*;
import jtom.checker.*;
import jtom.compiler.*;
import jtom.optimizer.*;
import jtom.parser.*;
import jtom.tools.*;
import jtom.verifier.*;
import aterm.ATerm;
import aterm.pure.PureFactory;

public class Tom {
    /*
     * Singleton pattern
     */
  private static Tom instance = null;
  protected Tom() {
      // Not public to defeat instantiation
  }

  public static Tom getInstance() {
    if(instance == null) {
      instance = new Tom();
        /*
         * initialization of the unique instance
         * the order must be kept:
         * - the factory is built
         * - ASTFactory and SymbolTable are created (and initialized latter in Tom.init()->environment().init())
         * - The environment is created
         */
      Factory tomSignatureFactory = new Factory(new PureFactory());
      TomTaskInput.create();
      ASTFactory astFactory = new ASTFactory(tomSignatureFactory);
      SymbolTable symbolTable = new SymbolTable(astFactory);
      TomEnvironment.create(tomSignatureFactory, astFactory, symbolTable);
    }
    return instance;
  }
  
    /*
     * list of files given in the command line
     */
  List inputFileList;
  
  public static String version =
    "\njtom 2.0rc3\n"
    + "\n"
    + "Copyright (C) 2000-2004 INRIA, Nancy, France.\n";
  
  public static String usage =
    "Tom usage:"
    + "\n\ttom [options] input[.t] [... input[.t]]"
    + "\nOptions:"
    + "\n\t--help \t\t| -h:\tShow this help"
    + "\n\t--cCode \t| -c:\tGenerate C code"
    + "\n\t--eCode \t| -e:\tGenerate Eiffel code"
    + "\n\t--camlCode \t\tGenerate Caml code"
    + "\n\t--version \t| -V:\tPrint version"
    + "\n\t--verbose \t| -v:\tSet verbose mode on"
    + "\n\t--intermediate\t| -i:\tGenerate intermediate files"
    + "\n\t--noOutput \t\t:\tDo not generate code"
    + "\n\t--noDeclaration | -D:\tDo not generate code for declarations"
    + "\n\t--doCompile \t| -C:\tStart after type-checking"
    + "\n\t--noCheck \t| -f:\tDo not verify correctness"
    + "\n\t--Wall\t\t\tPrint all warnings"
    + "\n\t--noWarning\t\tDo not print warning"
    + "\n\t--lazyType \t| -l:\tUse universal type"
    + "\n\t--import <path> | -I:\tPath for %include"
    + "\n\t--pretty\t| -p:\tGenerate readable code"
    + "\n\t--atermStat \t| -s:\tPrint internal ATerm statistics"
    + "\n\t--optimize \t| -O:\tOptimized generated code"
    + "\n\t--static\t\tGenerate static functions"
    //+ "\n\t--debug\t\t\tGenerate debug primitives"
    //+ "\n\t--verify\t\t\tVerify correctness of match compilation"
    + "\n\t--output \t| -o:\tSet output file name"
    //+ "\n\t--memory\t\tAdd memory management while debugging (not correct with list matching)"
    + "\n\t--destdir\t| -d <directory>\t\tSpecify where to place generated files"
    ;

  public static void version() {
    System.out.println(version);
  }

  public static void usage() {
    System.out.println(usage);
  }

  private static TomTaskInput getInput() {
    return TomTaskInput.getInstance();
  }
  
  private static TomEnvironment environment() {
  	return TomEnvironment.getInstance();
  }

  private static Factory tsf() {
    return TomEnvironment.getInstance().getTomSignatureFactory();
  }
  
  private void init(String args[]) {
    environment().init();
    getInput().init();
    inputFileList = initInputFromArgs(args);
  }

  private void reinit(String localInputFile) {
    updateInputOutputFile(localInputFile);
    environment().init();
  }
  
  private List initInputFromArgs(String args[]) {
    getInput().setInputSuffix(".t");
    getInput().setOutputSuffix(".java");
    List localUserImportList = new ArrayList();
    List localInputFileList = new ArrayList();
    String localDestDir = null;
    int i =0;
      // Processing the input arguments into taskInput
    try{
      for (i = 0; i < args.length; i++) {
          //System.out.println("args[" + i + "] = '" + args[i] + "'");
        if (args[i].charAt(0) != '-') {
            // Suppose this is the input filename (*[.t])
            // that should never start with a `-` character"
          localInputFileList.add(args[i]);
        } else {
            // This is on option
          if (args[i].equals("--version") || args[i].equals("-V")) {
            version();
            getInput().setVersion(true);
            environment().messageWarning(version, "", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
            return localInputFileList;
          } else if(args[i].equals("--help") || args[i].equals("-h")) {
            usage();
            getInput().setHelp(true);
            environment().messageWarning(usage, "", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
            return localInputFileList;
          } else if(args[i].equals("--import") || args[i].equals("-I")) {
            localUserImportList.add(new File(args[++i]).getAbsoluteFile());
          } else if (args[i].equals("--cCode") || args[i].equals("-c")) {
            getInput().setCCode();
            getInput().setInputSuffix(".t");
            getInput().setOutputSuffix(".tom.c");
          } else if (args[i].equals("--camlCode")) {
            getInput().setCamlCode();
            getInput().setInputSuffix(".t");
            getInput().setOutputSuffix(".tom.ml");
          } else if (args[i].equals("--eCode") || args[i].equals("-e")) {
            getInput().setECode();
            getInput().setInputSuffix(".t");
            getInput().setOutputSuffix(".e");
          } else if(args[i].equals("--noOutput")) {
            getInput().setPrintOutput(false);
          } else if(args[i].equals("--doCompile") || args[i].equals("-C")) {
            getInput().setDoOnlyCompile(true);
            getInput().setDoParse(false);
            getInput().setDoExpand(false);
            getInput().setInputSuffix(TomTaskInput.expandedSuffix);
          } else if (
            args[i].equals("--optimize") || args[i].equals("-O")) {
            getInput().setDoOptimization(true);
          } else if (
            args[i].equals("--noCheck") || args[i].equals("-f")) {
            getInput().setDoCheck(false);
          } else if (
            args[i].equals("--verify")) {
            getInput().setDoVerify(true);
          } else if (
            args[i].equals("--lazyType") || args[i].equals("-l")) {
            getInput().setStrictType(false);
          } else if (
            args[i].equals("--intermediate") || args[i].equals("-i")) {
            getInput().setIntermediate(true);
          } else if (
            args[i].equals("--verbose") || args[i].equals("-v")) {
            getInput().setVerbose(true);
          } else if (
            args[i].equals("--atermStat") || args[i].equals("-s")) {
            getInput().setAtermStat(true);
          } else if (args[i].equals("--Wall")) {
            getInput().setWarningAll(true);
          } else if (args[i].equals("--noWarning")) {
            getInput().setNoWarning(true);
          } else if (
            args[i].equals("--noDeclaration") || args[i].equals("-D")) {
            getInput().setGenDecl(false);
          } else if (
            args[i].equals("--pretty") || args[i].equals("-p")) {
            getInput().setPretty(true);
          } else if (args[i].equals("--static")) {
            getInput().setStaticFunction(true);
          } else if (args[i].equals("--debug")) {
            getInput().setDebugMode(true);
          } else if (args[i].equals("--memory")) {
            getInput().setDebugMemory(true);
          } else if (args[i].equals("--eclipse")) {
            getInput().setEclipseMode(true);
          } else if (args[i].equals("--destdir") || args[i].equals("-d")) {
            localDestDir = args[++i];
              //System.out.println("localDestDir = " + localDestDir);
          } else if (args[i].equals("--output") || args[i].equals("-o")) {
            if(getInput().getUserOutputFile()!= null) {
              environment().messageError(TomMessage.getString("OutputTwice"), "", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
              return localInputFileList;
            }
            getInput().setUserOutputFile(args[++i]);
          } else {
            String s = "'" + args[i] + "' is not a valid option";
            System.out.println(s);
            environment().messageError(TomMessage.getString("InvalidOption"), new Object[]{args[i]},"", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
            getInput().setHelp(true);
            usage();
            return localInputFileList;
          }
        }
      } // end processing arguments

        // For the moment debug is only available for Java as target language
      getInput().setDebugMode(getInput().isJCode() && getInput().isDebugMode());

      
        /*
         * compute destDir:
         */
      if(localDestDir==null || localDestDir.length()==0) {
      	localDestDir=".";
      } else if(getInput().getUserOutputFile() != null) {
        environment().messageError(TomMessage.getString("InvalidOutputDestdir"), "", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
        return localInputFileList;
      }

      getInput().setDestDir(localDestDir);
      
      
        // Setting importList
      getInput().setUserImportList(localUserImportList);


    } catch (ArrayIndexOutOfBoundsException e) {
      environment().messageError(TomMessage.getString("IncompleteOption"), new Object[]{args[--i]}, "", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
      getInput().setHelp(true);
      usage();
      return localInputFileList;
    }
    return localInputFileList;
  }

  private void updateInputOutputFile(String localInputFileName) {
      /*
       * compute inputFile:
       *  - add a suffix if necessary
       */
    if(!localInputFileName.endsWith(getInput().getInputSuffix())) {
      localInputFileName += getInput().getInputSuffix();
    }
    getInput().setInputFile(localInputFileName);
    
      /*
       * compute outputFile:
       *  - either use the given UserOutputFileName
       *  - either concatenate
       *    the outputDir
       *    [the packagePath] will be updated by the parser
       *    and reuse the inputFileName with a good suffix
       */
    if(getInput().isUserOutputFile()) {
      getInput().setOutputFile(getInput().getUserOutputFile().getPath());
    } else {
      String child = new File(getInput().getInputFileNameWithoutSuffix() + getInput().getOutputSuffix()).getName();
      File out = new File(getInput().getDestDir(),child).getAbsoluteFile();
      getInput().setOutputFile(out.getPath());
    }
  }

  private TomTask createTaskChainFromInput() {
    String fileName = getInput().getInputFile().getPath();
    TomExpander expander = new TomExpander();
    TomSyntaxChecker syntaxChecker = new TomSyntaxChecker();
    TomTypeChecker typeChecker = new TomTypeChecker();
    TomTask verifExtract = new TomVerifierExtract();
    TomCompiler compiler = new TomCompiler();

    TomTask initialTask = null;

    InputStream input = null;
      // Create the Chain of responsability    
    if (getInput().isDoParse() && getInput().isDoExpand()) {
      TomTaskParser tomParser = new TomTaskParser(fileName);
      // This is the initial task
      initialTask = tomParser;
      
      if (getInput().isDoCheck()) {
        tomParser.addTask(syntaxChecker);
        syntaxChecker.addTask(expander);
        expander.addTask(typeChecker);
      } else {
        tomParser.addTask(expander);
      }
      
      if (getInput().isDoVerify()) {
        if (getInput().isDoCheck()) {
          typeChecker.addTask(verifExtract);
        } else {
          expander.addTask(verifExtract);
        }
      }
    } //DoParse() && DoExpand
    
    if (getInput().isDoCompile()) {
        // We need a compiler
      if (getInput().isDoOnlyCompile()) {
        ATerm fromFileExpandTerm = null;
        TomTerm expandedTerm = null;
        try {
          fromFileExpandTerm =
            tsf().getPureFactory().readFromFile(
              input);
          expandedTerm =
            tsf().TomTermFromTerm(
              fromFileExpandTerm);
          input = new FileInputStream(getInput().getInputFileNameWithoutSuffix() + ".table");
          ATerm fromFileSymblTable = null;
          fromFileSymblTable =
            tsf().getPureFactory().readFromFile(
              input);
          TomSymbolTable symbTable = tsf().TomSymbolTableFromTerm(fromFileSymblTable);
          environment().getSymbolTable().regenerateFromTerm(symbTable);
        } catch (FileNotFoundException e) {
          environment().messageError(TomMessage.getString("FileNotFound"), new Object[]{fileName}, "", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
          return null;
        } catch (IOException e4) {
          environment().messageError(TomMessage.getString("IOException"), new Object[]{fileName}, "", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
          return null;
        }
          // This is the initial task
        initialTask = compiler;
        environment().setTerm(expandedTerm);
      } else {
        if (getInput().isDoCheck()) {
          if (getInput().isDoVerify()) {
            verifExtract.addTask(compiler);
          } else {
            typeChecker.addTask(compiler);
          }
        } else {
          if (getInput().isDoVerify()) {
            verifExtract.addTask(compiler);
          } else {
            expander.addTask(compiler);
          }
        }
      }
    } //DoCompile
    
    if (getInput().isPrintOutput()) {
      TomTask generator;
      generator = new TomBackend();
      if (getInput().isDoOptimization()) {
        TomOptimizer optimizer = new TomOptimizer();
        compiler.addTask(optimizer);
        optimizer.addTask(generator);
      } else {
        compiler.addTask(generator);
      }
    } //PrintOutput

    return initialTask;
  }

  public void run(String inputFileName) {
    reinit(inputFileName);
    TomTask initialTask = createTaskChainFromInput();
    if (initialTask != null) {
      initialTask.startProcess();
    }
  }

  public static void main(String args[]) {
    exec(args);
  }

  public static int exec(String args[]) {
    Tom tom = Tom.getInstance();
    tom.init(args);
    
    if(getInput().isHelp() || getInput().isVersion()) {
        // no need to do further work
    } else if(tom.inputFileList.isEmpty()) {
        System.out.println("No file to compile");
        usage();
        environment().messageError(TomMessage.getString("NoFileToCompile"), "", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    } else if(tom.inputFileList.size()>1 && getInput().getUserOutputFile() != null) {
      System.out.println("Cannot specify --output with multiple compilations");
      usage();
      environment().messageError(TomMessage.getString("OutputWithMultipleCompilation"), "", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    }

    if(Tom.environment().hasError()) {
      return 1;
    }

    for(Iterator it = tom.inputFileList.iterator() ; it.hasNext() ; ) {
      String inputFileName = (String)it.next();
      tom.run(inputFileName);

      if(Tom.environment().hasError()) {
        return 1;
      }
    }
    
    if(Tom.environment().hasError()) {
      return 1;
    } else {
      return 0;
    }
  }


  
} // class Tom
