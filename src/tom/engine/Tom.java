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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import aterm.pure.PureFactory;

public class Tom {
	//Necessary structure
	private TomTask initialTask;
	private TomTaskInput taskInput;
	private TomSignatureFactory tomSignatureFactory;
	private TomEnvironment environment;
	// potential tasks to be connected together to form the compiler chain

	private static String version =
		"\njtom 1.5gamma\n"
			+ "\nCopyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)\n"
			+ "                         Nancy, France.\n";

	private static String usage =
		"Tom usage:"
			+ "\n\tjava jtom.Tom [options] inputfile[.t]"
			+ "\nOptions:"
			+ "\n\t--help \t\t| -h:\tShow this help"
			+ "\n\t--cCode \t| -c:\tGenerate C code"
			+ "\n\t--eCode \t| -e:\tGenerate Eiffel code"
			+ "\n\t--version \t| -V:\tPrint version"
			+ "\n\t--verbose \t| -v:\tSet verbose mode on"
			+ "\n\t--intermediate\t| -i:\tGenerate intermediate files"
			+ "\n\t--noOutput \t| -o:\tDo not generate code"
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
			+ "\n\t--debug\t\t\tGenerate debug primitives"
			+ "\n\t--memory\t\tAdd memory management while debugging (not correct with list matching)";

	private static void version() {
		System.out.println(version);
	}

	private static void usage() {
		System.out.println(usage);
	}

	public Tom(String args[]) {
          PureFactory pureFactory = new PureFactory();
          tomSignatureFactory = new TomSignatureFactory(pureFactory);
          taskInput = new TomTaskInput(tomSignatureFactory.makeTomErrorList());
          modifyTaskInputFromArgs(args);
          if (taskInput.isHelp() || taskInput.isVersion()) {
              // no need to do further work
            return;
          }
          initializeStructure();
          createTaskChainFromInput();
	}

	public Tom(TomTaskInput input) {
		tomSignatureFactory = new TomSignatureFactory(new PureFactory());
		this.taskInput = input;
		initializeStructure();
		createTaskChainFromInput();

	}

	private void initializeStructure() {
		// create basic structures
		ASTFactory astFactory = new ASTFactory(tomSignatureFactory);
		SymbolTable symbolTable =
			new SymbolTable(
				astFactory,
				taskInput.isCCode(),
				taskInput.isJCode(),
				taskInput.isECode());
		environment =
			new TomEnvironment(tomSignatureFactory, astFactory, symbolTable);
	}

	private void modifyTaskInputFromArgs(String args[]) {
		String inputSuffix = ".t";
		List importList = new ArrayList();

		// Processing the input arguments into taskInput
		for (int i = 0; i < args.length; i++) {
			if (args[i].charAt(0) != '-') {
				// Suppose this is the input filename (*[.t]) that should never start with a `-` character"
				taskInput.setInputFileName(args[i]);
			} else {
				// This is on option
				if (args[i].equals("--version") || args[i].equals("-V")) {
					version();
					taskInput.setVersion(true);
					addError(version, "", 0, 1);
					return;
				} else if (args[i].equals("--help") || args[i].equals("-h")) {
					usage();
					taskInput.setHelp(true);
					addError(usage, "", 0, 1);
					return;
				} else if (
					args[i].equals("--import") || args[i].equals("-I")) {
					importList.add(new File(args[++i]));
				} else if (args[i].equals("--cCode") || args[i].equals("-c")) {
					taskInput.setJCode(false);
					taskInput.setECode(false);
					taskInput.setCCode(true);
					taskInput.setOutputSuffix(".tom.c");
				} else if (args[i].equals("--eCode") || args[i].equals("-e")) {
					taskInput.setJCode(false);
					taskInput.setECode(true);
					taskInput.setCCode(false);
					taskInput.setOutputSuffix(".e");
					taskInput.setSupportedGoto(false);
					taskInput.setSupportedBlock(false);
				} else if (
					args[i].equals("--noOutput") || args[i].equals("-o")) {
					taskInput.setPrintOutput(false);
				} else if (
					args[i].equals("--doCompile") || args[i].equals("-C")) {
					taskInput.setDoOnlyCompile(true);
					taskInput.setDoParse(false);
					taskInput.setDoExpand(false);
				} else if (
					args[i].equals("--optimize") || args[i].equals("-O")) {
					taskInput.setDoOptimization(true);
				} else if (
					args[i].equals("--noCheck") || args[i].equals("-f")) {
					taskInput.setDoCheck(false);
				} else if (
					args[i].equals("--lazyType") || args[i].equals("-l")) {
					taskInput.setStrictType(false);
				} else if (
					args[i].equals("--intermediate") || args[i].equals("-i")) {
					taskInput.setIntermediate(true);
				} else if (
					args[i].equals("--verbose") || args[i].equals("-v")) {
					taskInput.setVerbose(true);
				} else if (
					args[i].equals("--atermStat") || args[i].equals("-s")) {
					taskInput.setAtermStat(true);
				} else if (args[i].equals("--Wall")) {
					taskInput.setWarningAll(true);
				} else if (args[i].equals("--noWarning")) {
					taskInput.setNoWarning(true);
				} else if (
					args[i].equals("--noDeclaration")
						|| args[i].equals("-D")) {
					taskInput.setGenDecl(false);
				} else if (
					args[i].equals("--pretty") || args[i].equals("-p")) {
					taskInput.setPretty(true);
				} else if (args[i].equals("--static")) {
					taskInput.setStaticFunction(true);
				} else if (args[i].equals("--debug")) {
					taskInput.setDebugMode(true);
				} else if (args[i].equals("--memory")) {
					taskInput.setDebugMemory(true);
				} else if (args[i].equals("--eclipse")) {
					taskInput.setEclipseMode(true);
				} else {
					String s = "'" + args[i] + "' is not a valid option";
					System.out.println(s);
					addError(s, "", 0, 0);
					taskInput.setHelp(true);
					usage();
					return;
				}
			}
		} // end processing arguments

		// For the moment debug is only available for Java as target language
		taskInput.setDebugMode(taskInput.isJCode() && taskInput.isDebugMode());

		// setting Base/Input/OutputFileName
		if (taskInput.getInputFileName().length() == 0) {
			System.out.println("No input file name...\n");
			taskInput.setHelp(true);
			usage();
			if (taskInput.isEclipseMode()) {
				addError("No input file name...", "", 0, 0);
			}
		}
		if (taskInput.getInputFileName().endsWith(inputSuffix)) {
			taskInput.setBaseInputFileName(
				taskInput.getInputFileName().substring(
					0,
					taskInput.getInputFileName().length()
						- (inputSuffix.length())));
		} else {
			taskInput.setBaseInputFileName(taskInput.getInputFileName());
			taskInput.setInputFileName(
				taskInput.getInputFileName() + inputSuffix);
		}
		if (taskInput.isDoOnlyCompile()) {
			taskInput.setInputFileName(
				taskInput.getBaseInputFileName() + TomTaskInput.expandedSuffix);
		}
		taskInput.setOutputFileName(
			taskInput.getBaseInputFileName() + taskInput.getOutputSuffix());

		// Setting importList
		taskInput.setImportList(importList);
	}

	private void createTaskChainFromInput() {
		if (taskInput != null) {
			String fileName = taskInput.getInputFileName();
			TomExpander expander =
				new TomExpander(
					environment,
					new TomKernelExpander(environment));
			TomSyntaxChecker syntaxChecker = new TomSyntaxChecker(environment);
			TomTypeChecker typeChecker = new TomTypeChecker(environment);
			TomCompiler compiler =
				new TomCompiler(
					environment,
					new TomKernelCompiler(
						environment,
						taskInput.isSupportedBlock(),
						taskInput.isSupportedGoto(),
						taskInput.isDebugMode()));

			InputStream input = null;
			// Create the Chain of responsability    
			if (taskInput.isDoParse() && taskInput.isDoExpand()) {
				byte inputBuffer[] = null;
				List importList = taskInput.getImportList();
				File fileList[] = new File[importList.size()];
				Iterator it = importList.iterator();
				int index = 0;
				while (it.hasNext()) {
					fileList[index++] = (File) it.next();
				}

				try {
					input = new FileInputStream(fileName);
					// to get the length of the file
					File file = new File(fileName);
					inputBuffer = new byte[(int) file.length() + 1];
					input.read(inputBuffer);
				} catch (FileNotFoundException e) {
					String s = "File `" + fileName + "` not found.";
					System.out.println(s);
					System.out.println("No file generated.");
					if (taskInput.isEclipseMode()) {
						addError(s, "", 0, 0);
					}
					return;
				} catch (IOException e4) {
					String s = "IO Exception reading file `" + fileName + "`";
					System.out.println(s);
					System.out.println("No file generated.");
					if (taskInput.isEclipseMode()) {
						addError(s, "", 0, 0);
					}
					return;
				}
				TomParser tomParser =
					new TomParser(
						new TomBuffer(inputBuffer),
						environment,
						fileList,
						0,
						fileName);
				// This is the initial task
				initialTask = tomParser;

				if (taskInput.isDoCheck()) {
					tomParser.addTask(syntaxChecker);
					syntaxChecker.addTask(expander);
					expander.addTask(typeChecker);
				} else {
					tomParser.addTask(expander);
				}
			} //DoParse() && DoExpand

			if (taskInput.isDoCompile()) {
				// We need a compiler
				if (taskInput.isDoOnlyCompile()) {
					ATerm fromFileExpandTerm = null;
					TomTerm expandedTerm = null;
					try {
						fromFileExpandTerm =
							tomSignatureFactory.getPureFactory().readFromFile(
								input);
						expandedTerm =
							tomSignatureFactory.TomTermFromTerm(
								fromFileExpandTerm);
						input =
							new FileInputStream(
								taskInput.getBaseInputFileName() + ".table");
						ATerm fromFileSymblTable = null;
						fromFileSymblTable =
							tomSignatureFactory.getPureFactory().readFromFile(
								input);
						TomSymbolTable symbTable =
							tomSignatureFactory.TomSymbolTableFromTerm(
								fromFileSymblTable);
						environment.getSymbolTable().regenerateFromTerm(
							symbTable);
					} catch (FileNotFoundException e) {
						String s = "File `" + fileName + "` not found.";
						System.out.println(s);
						System.out.println("No file generated.");
						if (taskInput.isEclipseMode()) {
							addError(s, "", 0, 0);
						}
						return;
					} catch (IOException e4) {
						String s =
							"IO Exception reading file `" + fileName + "`";
						System.out.println(s);
						System.out.println("No file generated.");
						if (taskInput.isEclipseMode()) {
							addError(s, "", 0, 0);
						}
						return;
					}
					// This is the initial task
					initialTask = compiler;
					taskInput.setTerm(expandedTerm);
				} else {
					if (taskInput.isDoCheck()) {
						typeChecker.addTask(compiler);
					} else {
						expander.addTask(compiler);
					}
				}
			} //DoCompile

			if (taskInput.isPrintOutput()) {
				TomGenerator generator = new TomGenerator(environment);
				if (taskInput.isDoOptimization()) {
					TomOptimizer optimizer = new TomOptimizer(environment);
					compiler.addTask(optimizer);
					optimizer.addTask(generator);
				} else {
					compiler.addTask(generator);
				}
			} //PrintOutput
		}
	}

	private void addError(String msg, String file, int line, int level) {
		TomError err =
			tomSignatureFactory.makeTomError_Error(msg, file, line, level);
		taskInput.setErrors(
			tomSignatureFactory.makeTomErrorList(err, taskInput.getErrors()));
	}

	public TomErrorList getErrors() {
		TomErrorList res = taskInput.getErrors();
		if (res == null) {
			// Do not have been initialised
			res = tomSignatureFactory.makeTomErrorList();
			taskInput.setErrors(res);
		}
		return res;
	}

	public void run() {
		if (initialTask != null
			&& !taskInput.isHelp()
			&& !taskInput.isVersion()) {
			initialTask.process(taskInput);
			if (taskInput.isAtermStat()) {
				System.out.println(
					"\nStatistics:\n" + tomSignatureFactory.getPureFactory());
			}
		}
	}

	public static void main(String args[]) {
		Tom tomCompiler = new Tom(args);
		tomCompiler.run();
	}

} // class Tom
