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
import aterm.pure.*;

import jtom.tools.*;
import jtom.compiler.*;
import jtom.checker.*;
import jtom.parser.*;
import jtom.verifier.*;
import jtom.exception.*;
import jtom.adt.*;

public class Tom {
  private static String version = "1.1-beta";
  
    private static void usage() {
    System.out.println("Tom usage:");
    System.out.println("\tjava jtom.Tom [options] inputfile");
    if(Flags.version) {
      System.out.println("\tversion " + version);
    }
    System.out.println("Options:");
    System.out.println("\t--verbose: set verbose mode on");
    System.out.println("\t--noOutput: do not generate code");
    System.out.println("\t--atermStat: print internal ATerm statistics");
    System.out.println("\t--cCode | -c: generate C code");
    System.out.println("\t--eCode: generate Eiffel code");
    System.out.println("\t--doCompile: start after type-checking");
    System.out.println("\t--intermediate: generate intermediate files");
    System.out.println("\t--version: print version");
    System.out.println("\t--noVerify: do not verify correctness");
    System.out.println("\t--noWarning: do not print nay warning");
    System.out.println("\t--lazyType: use universal type");
    System.out.println("\t--demo: run demo mode");
    System.out.println("\t--noDeclaration: do not generate code for declarations");
    System.out.println("\t--import <path>: path for %include");
    System.out.println("\t--pretty: generate readable code");
    System.exit(0);
  }

  public static void main(String args[]) {
    String fileName        = "";
    String inputSuffix     = ".t";
    String outputSuffix    = ".java";
    String compiledSuffix  = ".tfix.compiled";
    String parsedSuffix    = ".tfix.parsed";
    String expandedSuffix  = ".tfix.expanded";
    String checkedSuffix   = ".tfix.checked";
    String parsedTableSuffix = ".tfix.parsed.table";
    String checkedTableSuffix = ".tfix.checked.table";

    List importList = new ArrayList();
    
    if(args.length >= 1) {
      for(int i=0; i < args.length; i++) { 
	if(args[i].charAt(0) == '-') {
          if(args[i].equals("--version")) {
            Flags.version = true;
            usage();
          } else if(args[i].equals("--noOutput")) {
	    Flags.printOutput = false;
          } else if(args[i].equals("--verbose")) {
	    Flags.verbose = true;
          } else if(args[i].equals("--atermStat")) {
	    Flags.atermStat = true;
          } else if(args[i].equals("--cCode") || args[i].equals("-c")) {
            Flags.jCode = false;
            Flags.cCode = true;
            outputSuffix = ".tom.c";
          } else if(args[i].equals("--eCode")) {
            Flags.eCode = true;
            Flags.jCode = false;
            Flags.supportedGoto = false;
            Flags.supportedBlock = false;
            outputSuffix = ".e";
          } else if(args[i].equals("--doCompile")) {
            Flags.doParse = false;
            Flags.doExpand = false;
            Flags.doCheck = false;
          } else if(args[i].equals("--intermediate")) {
            Flags.intermediate = true;
          } else if(args[i].equals("--noWarning")) {
            Flags.noWarning = true;
          } else if(args[i].equals("--noVerify")) {
            Flags.doVerify = false;
          } else if(args[i].equals("--strictType")) {
            System.out.println("Warning: --strictType is now set by default");
            Flags.strictType = true;
          } else if(args[i].equals("--lazyType")) {
            Flags.strictType = false;
	  } else if(args[i].equals("--demo")) {
	    Flags.demo = true;
            Flags.noWarning = true;
	  } else if(args[i].equals("--noDeclaration")) {
	    Flags.genDecl = false;
	  } else if(args[i].equals("--import")) {
            i++;
            File importFile = new File(args[i]);
            importList.add(importFile);
          } else if(args[i].equals("--pretty")) {
	    Flags.pretty = true;
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
    
    TomVerifier  tomVerifier = new TomVerifier(environment);
	
    String inputFileName = "";

    if(Flags.doParse) {
      inputFileName = fileName + inputSuffix;
    } else if(Flags.doCompile) {
      inputFileName = fileName + checkedSuffix;
    }

    InputStream input;
    try {
      input = new FileInputStream(inputFileName);
    } catch (FileNotFoundException e) {
      System.out.println("Tom Parser:  File " + inputFileName + " not found.");
      System.out.println("No file generated.");
      return;
    } catch(IOException e) {
      System.out.println("No file generated.");
      throw new InternalError("read error");
    }
        
    TomTerm parsedTerm   = null;
    TomTerm expandedTerm = null;
    TomTerm checkedTerm  = null;
    TomTerm compiledTerm = null;

    if(Flags.doParse && Flags.doExpand && Flags.doCheck) {
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
        
        TomParser tomParser = new TomParser(new TomBuffer(inputBuffer),environment,tomVerifier,fileList);

        startChrono();
        parsedTerm = tomParser.startParsing();
        tomParser.updateSymbol();
        stopChrono();
        if(Flags.verbose) System.out.println("TOM parsing phase " + getChrono());
        if(Flags.intermediate) {
          generateOutput(fileName + parsedSuffix,parsedTerm);
          generateOutput(fileName + parsedTableSuffix,symbolTable.toTerm());
        }
        
	TomChecker tomChecker = new TomChecker(environment,tomVerifier);
        startChrono();
        expandedTerm = tomChecker.expand(parsedTerm);
        stopChrono();
        if(Flags.verbose) System.out.println("TOM expansion phase " + getChrono());
        if(Flags.intermediate) {
          generateOutput(fileName + expandedSuffix,expandedTerm);
        }
        
        startChrono();
        TomTerm context = null;
        tomChecker.updateSymbolPass1();
        checkedTerm  = tomChecker.pass1(context, expandedTerm);
        stopChrono();
        if(Flags.verbose) System.out.println("TOM checking phase " + getChrono());
        if(Flags.intermediate) {
          generateOutput(fileName + checkedSuffix,checkedTerm);
          generateOutput(fileName + checkedTableSuffix,symbolTable.toTerm());
        }

        if(Flags.demo) {
          statistics.initInfoParser();
          statistics.initInfoChecker();
          statistics.initInfoVerifier();
        }

      } catch (FileNotFoundException e) {
        System.out.println("Tom Parser:  File " + inputFileName + " not found.");
        System.out.println("No file generated.");
        return;
      } catch(ParseException e1) {
        System.out.println(e1.getMessage());
        System.out.println("Tom Parser:  Encountered errors during parsing.");
        System.out.println("No file generated.");
        return;
	} catch(TomException e2) {
        System.out.println(e2);
        System.out.println("Tom:  Encountered errors during compilation.");
        System.out.println("No file generated.");
        return;
      } catch(IOException e) {
        System.out.println("No file generated.");
        throw new InternalError("read error");
      }
    }
    
      /*
       * The compilation process is stopped if an error occured during
       * parsing or type checking
       */

    if(Flags.findErrors) {
      System.out.println("*** ERRORS");
      System.out.println("*** COMPILATION ABORTED");
      System.out.println("No file generated.");
      System.exit(0);
    }
    
    if(Flags.doCompile) {
      try {
        if(!Flags.doCheck) {
          checkedTerm = (TomTerm) tomSignatureFactory.readFromTextFile(input);
        }

	TomCompiler tomCompiler = new TomCompiler(environment);
        startChrono();
        TomTerm simpleCheckedTerm = tomCompiler.pass2_1(checkedTerm);
        
        compiledTerm = tomCompiler.pass2_2(simpleCheckedTerm);
          //System.out.println("pass2 =\n" + compiledTerm);
        compiledTerm = tomCompiler.pass3(compiledTerm);
          //System.out.println("pass3 =\n" + compiledTerm);
        stopChrono();
        if(Flags.verbose) System.out.println("TOM compilation phase " + getChrono());
        if(Flags.intermediate) generateOutput(fileName + compiledSuffix,compiledTerm);

	TomGenerator tomGenerator = new TomGenerator(environment);
        startChrono();
        int defaultDeep = 2;
        Writer writer = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(fileName + outputSuffix)));
        OutputCode out = new OutputCode(writer, Flags.pretty);
        tomGenerator.generate(out,defaultDeep,compiledTerm);
        writer.close();
        stopChrono();
        if(Flags.verbose) System.out.println("TOM generation phase " + getChrono());
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
