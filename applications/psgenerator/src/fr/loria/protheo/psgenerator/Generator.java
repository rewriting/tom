/*
 * Created on 11 août 2004
 *
 * Copyright (c) 2004-2005, Michael Moossen
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.  
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fr.loria.protheo.psgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import org.freehep.util.commandline.CommandLine;
import org.freehep.util.commandline.CommandLineException;

import fr.loria.protheo.psgenerator.generator.AbstractGenerator;
import fr.loria.protheo.psgenerator.generator.GeneratorManager;
import fr.loria.protheo.psgenerator.parser.AbstractParser;
import fr.loria.protheo.psgenerator.parser.ParserManager;


/**
 *  The Generator class, is the entry point for the application.<br>
 *
 *      <p align="left"><font color="#003063"><b>Change Log</b></font>
 *      <table border="0" cellpadding="1">
 *        <tr bordercolor="#FF0000" bgcolor="#CCCCCC" align="center"> 
 *          <th width="107"><strong>Date</strong></th>
 *          <th width="67"><strong>Version</strong></th>
 *          <th width="491"><strong>Description</strong></th>
 *        </tr>
 *        <tr align="center"> 
 *          <td>11 août 2004</td>
 *          <td>0</td>
 *          <td align="left"> 
 *            File Creation
 *          </td>
 *        </tr>
 *        <tr bgcolor="#EAEAEA"  align="center"> 
 *          <td>12 août 2004</td>
 *          <td>0.1</td>
 *          <td align="left"> 
 *            Initial Working Version
 *          </td>
 *        </tr>
 *        <tr align="center"> 
 *          <td>15 sept 2004</td>
 *          <td>0.2</td>
 *          <td align="left"> 
 *            return code handlers added<br>
 *            Abstract parsers and parser manager added
 *          </td>
 *        </tr>
 *      </table>
 *
 * @author <a href="mailto:moossen@loria.fr">Michael Moossen</a>
 */
public class Generator {
	public static final String VERSION = "0.2";

	public static void main(String[] args) throws FileNotFoundException {
		// ini trace system. 
		// it works only when assertions are enabled, use the -ea flag for javac
		assert(Tracer.setTraceStream(new PrintStream(new FileOutputStream("psgen.trace")))); //System.err
		assert(Tracer.trace());
		
		// registers generators found in fr/loria/protheo/psgenerator/generator/generators.properties
		GeneratorManager.registerGenerators(); 
		// registers parsers found in fr/loria/protheo/psgenerator/parser/parsers.properties
		ParserManager.registerParsers(); 
		
		// for date formatting of generated code
		Locale.setDefault(Locale.ENGLISH);
		
		// ini command line interpreter from freehep
		String cmd = "Production System Generator. Version " + VERSION;
		CommandLine cl = new CommandLine("psgenerator", cmd, 1);
		cl.addBailOutOption("help", "h", "show help on command");
		cl.addBailOutOption("retcodes", "r", "show return codes on command");
		cl.addBailOutOption("version", "v", "print product version");
		cl.addOption("objective", "o", GeneratorManager.getOptionNames(), "objective language, default is clips");
		cl.addOption("parser", "p", ParserManager.getOptionNames(), "XML parser, default is dom");
		cl.addOption("outfile", "f", "output file name without extension", "output file instead of stdout");
		cl.addParameter("infile", "the input xml-file");
		
		try {
			if (!cl.parse(args)) {
				if (cl.hasOption("help")) {
					System.out.println(cl.getHelp());
				} else if (cl.hasOption("version")) {
					System.out.println(cmd);
				} else if (cl.hasOption("retcodes")) {
						System.out.println(ReturnCodeHandler.getSummary());
				} else {
					// if no arguments nor options are given
					System.out.println(cl.getHelp());
					System.exit(ReturnCodeHandler.INCORRECT_USAGE.getId());
				}
				System.exit(ReturnCodeHandler.BAIL_OUT_OPTION.getId());
			} 
			AbstractParser parser = ParserManager.getNewParser(
					cl.getOption("parser", "dom"), cl.getArgument("infile"));
			AbstractGenerator gen = GeneratorManager.getNewGenerator(
					cl.getOption("objective", "clips"), parser.getProgram());
			if (cl.getOption("outfile")==null) {
				System.out.println(gen.getOutput());
			} 
			else {
				if (cl.getOption("outfile").equals("true")) {
					// if the option without an argument is given
					System.out.println(cl.getHelp());
					System.exit(ReturnCodeHandler.INCORRECT_USAGE.getId());
				}
				FileHelper.setFileContents(new File(cl.getOption("outfile")+"."+gen.getFileExtension()) ,gen.getOutput());
			}
			System.exit(ReturnCodeHandler.NO_ERROR.getId());
		} catch (CommandLineException cle) {
			System.err.println(cle.getMessage());
			System.out.println(cl.getHelp());
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		System.exit(ReturnCodeHandler.UNKNOWN_ERROR.getId());
	}
}