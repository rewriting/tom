/*
 * Created on 12 sept. 2004
 *
 * Copyright (c) 2004-2007, Michael Moossen
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
package fr.loria.protheo.psgenerator.parser;

/**
 *  The PSSConstants interface includes several constants for xml elements and 
 *  attributes names.
 *
 *      <p align="left"><font color="#003063"><b>Change Log</b></font>
 *      <table border="0" cellpadding="1">
 *        <tr bordercolor="#FF0000" bgcolor="#CCCCCC" align="center"> 
 *          <th width="107"><strong>Date</strong></th>
 *          <th width="67"><strong>Version</strong></th>
 *          <th width="491"><strong>Description</strong></th>
 *        </tr>
 *        <tr align="center"> 
 *          <td>12 sept 2004</td>
 *          <td>0</td>
 *          <td align="left"> 
 *            File Creation
 *          </td>
 *        </tr>
 *        <tr bgcolor="#EAEAEA"  align="center"> 
 *          <td>12 sept 2004</td>
 *          <td>0.1</td>
 *          <td align="left"> 
 *            Initial Working Version
 *          </td>
 *        </tr>
 *      </table>
 *
 * @author <a href="mailto:moossen@loria.fr">Michael Moossen</a>
 */
public interface PSSConstants {
	public static final String PSP_SPEC = "psp-spec";
	public static final String NAME = "name";
	public static final String VERSION = "version";
	public static final String FACT_DEFINITIONS = "FactDefinitions";
	public static final String INTIAL_WORKING_MEMORY = "InitialWorkingMemory";
	public static final String INITIAL_PRODUCTION_MEMORY = "InitialProductionMemory";
	public static final String ORDERED_FACT_DEFINITION = "OrderedFactDefinition";
	public static final String FACT_NAME = "FactName";
	public static final String FACT_ARITY = "FactArity";
	public static final String COMMENTS = "Comments";
	public static final String UNORDERED_FACT_DEFINITION = "UnorderedFactDefinition";
	public static final String SLOT_NAME = "SlotName";
	public static final String ORDERED_FACT_INSTANCE = "OrderedFactInstance";
	public static final String SLOT_VALUE = "SlotValue";
	public static final String UNORDERED_FACT_INSTANCE = "UnorderedFactInstance";
	public static final String SLOT = "Slot";
	public static final String ORDERED_PATTERN = "OrderedPattern";
	public static final String UNORDERED_PATTERN = "UnorderedPattern";
	public static final String ORDERED_NEGATIVE_PATTERN = "OrderedNegativePattern";
	public static final String UNORDERED_NEGATIVE_PATTERN = "UnorderedNegativePattern";
	public static final String PRODUCTION_RULE = "ProductionRule";
	public static final String RULE_NAME = "RuleName";
	public static final String LEFT_HAND_SIDE = "LeftHandSide";
	public static final String RIGHT_HAND_SIDE = "RightHandSide";
	public static final String PATTERNS = "Patterns";
	public static final String CONDITIONS = "Conditions";
	public static final String CONDITION = "Condition";
	public static final String RELATION = "Relation";
	public static final String EXPRESSION = "Expression";
	public static final String FACTS_TO_REMOVE = "FactsToRemove";
	public static final String FACTS_TO_ADD = "FactsToAdd";

}
