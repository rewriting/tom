/*
 * Created on 10 août 2004
 *
 * Copyright (c) 2004, Michael Moossen
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
package fr.loria.protheo.psgenerator.generator;

import java.util.List;

import fr.loria.protheo.psgenerator.Tracer;
import fr.loria.protheo.psgenerator.representation.Program;


/**
 *  The AbstractGenerator class
 *
 *      <p align="left"><font color="#003063"><b>Change Log</b></font>
 *      <table border="0" cellpadding="1">
 *        <tr bordercolor="#FF0000" bgcolor="#CCCCCC" align="center"> 
 *          <th width="107"><strong>Date</strong></th>
 *          <th width="67"><strong>Version</strong></th>
 *          <th width="491"><strong>Description</strong></th>
 *        </tr>
 *        <tr align="center"> 
 *          <td>10 août 2004</td>
 *          <td>0</td>
 *          <td align="left"> 
 *            File Creation
 *          </td>
 *        </tr>
 *        <tr bgcolor="#EAEAEA"  align="center"> 
 *          <td>10 août 2004</td>
 *          <td>0.1</td>
 *          <td align="left"> 
 *            Initial Working Version
 *          </td>
 *        </tr>
 *      </table>
 *
 * @author <a href="mailto:moossen@loria.fr">Michael Moossen</a>
 */
public abstract class AbstractGenerator {
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	protected String output;
	
	protected final List factTypes;
	protected final List initialFacts;
	protected final List rules;
	protected final Program program;

	public AbstractGenerator(Program program) {
		assert(Tracer.trace());
		this.factTypes = program.getFactTypes();
		this.initialFacts = program.getInitialFacts();
		this.rules = program.getProductionRules();
		this.program = program;
		this.output = "";
		generate();
	}

	public String getOutput() {
		assert(Tracer.trace());
		return output;
	}

	private void generate() {
		assert(Tracer.trace());
		generateHeader();
		generateDefinitions();
		generateInicialization();
		generateKnowledgeBase();
	}

	protected abstract void generateHeader(); 
	protected abstract void generateKnowledgeBase(); 
	protected abstract void generateInicialization();
	protected abstract void generateDefinitions();

	public String getFileExtension() {
		return GeneratorManager.getFileExtension(this.getClass());
	}

}