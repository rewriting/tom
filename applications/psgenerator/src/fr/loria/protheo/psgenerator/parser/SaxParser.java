/*
 * Created on 14-sept-2004
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
package fr.loria.protheo.psgenerator.parser;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *  The SaxParser class
 *
 *      <p align="left"><font color="#003063"><b>Change Log</b></font>
 *      <table border="0" cellpadding="1">
 *        <tr bordercolor="#FF0000" bgcolor="#CCCCCC" align="center"> 
 *          <th width="107"><strong>Date</strong></th>
 *          <th width="67"><strong>Version</strong></th>
 *          <th width="491"><strong>Description</strong></th>
 *        </tr>
 *        <tr align="center"> 
 *          <td>15 sept 2004</td>
 *          <td>0</td>
 *          <td align="left"> 
 *            File Creation
 *          </td>
 *        </tr>
 *        <tr bgcolor="#EAEAEA"  align="center"> 
 *          <td>15 sept 2004</td>
 *          <td>0.1</td>
 *          <td align="left"> 
 *            Initial Working Version
 *          </td>
 *        </tr>
 *      </table>
 *
 * @author <a href="mailto:moossen@loria.fr">Michael Moossen</a>
 */
public class SaxParser extends AbstractParser {
	private File file;
	
	public SaxParser(String fileName) {
		super(fileName);
	}

	public void readFile(String fileName) {
		file = new File(fileName);
	}

	public void parse() {
		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(file, new DefaultHandler() {

				//===========================================================
				// SAX ErrorHandler methods
				//===========================================================

				public void fatalError(SAXParseException err) throws SAXParseException {
					System.out.println("Fatal Error");
					throw (err);
				}

				// treat validation errors as fatal
				public void error(SAXParseException err) throws SAXParseException {
					System.out.println("Error");
					throw (err);
				}

				// dump warnings too
				public void warning(SAXParseException err) throws SAXParseException {
					System.out.println("** Warning" + ", line " + err.getLineNumber()
							+ ", uri " + err.getSystemId());
					System.out.println("   " + err.getMessage());
				}

				public void endDocument() throws SAXException {
					System.out.println(program);
				}

				private List wd = Collections.checkedList(new Vector(),String.class);
				
				public void startElement(String uri, String localName, String qName,
						Attributes attributes) throws SAXException {
					String eName = localName;
					if (eName.equals(""))
						eName = qName; // not namespaceAware
					if (eName.equals("psp-spec")) {
						//program = new Program(attributes.getValue("name"), attributes.getValue("version"));
					} else if (eName.equals("FactDefinitions")) {
						//program.setFactTypes(new Vector());
					} else if (eName.equals("InitialWorkingMemory")) {
						//program.setInitialFacts(new Vector());
					} else if (eName.equals("InitialProductionMemory")) {
						//program.setRules(new Vector());
					} else if (eName.equals("UnorderedFactDefinition")) {
						//
					} else if (eName.equals("OrderedFactInstance")) {
						//
					} else if (eName.equals("UnorderedFactInstance")) {
						//
					} else if (eName.equals("OrderedPattern")) {
						//
					} else if (eName.equals("UnorderedPattern")) {
						//
					} else if (eName.equals("OrderedNegativePattern")) {
						//
					} else if (eName.equals("UnorderedNegativePattern")) {
						//
					} else if (eName.equals("FactName")) {
						//
					} else if (eName.equals("FactArity")) {
						//
					} else if (eName.equals("Slot")) {
						//
					} else if (eName.equals("SlotName")) {
						//
					} else if (eName.equals("SlotValue")) {
						//
					} else if (eName.equals("Comment")) {
						//
					} else if (eName.equals("ProductionRule")) {
						//
					} else if (eName.equals("RuleName")) {
						//
					} else if (eName.equals("LeftHandSide")) {
						//
					} else if (eName.equals("RightHandSide")) {
						//
					} else if (eName.equals("Patterns")) {
						//
					} else if (eName.equals("Conditions")) {
						//
					} else if (eName.equals("Condition")) {
						//
					} else if (eName.equals("Relation")) {
						//
					} else if (eName.equals("Expression")) {
						//
					} else if (eName.equals("FactsToRemove")) {
						//
					} else if (eName.equals("FactsToAdd")) {
						//
					}
				}

				public void characters(char[] ch, int start, int length)
						throws SAXException {
				}
				
				public void endElement(String uri, String localName, String qName)
						throws SAXException {
					String eName = localName;
					if (eName.equals(""))
						eName = qName; // not namespaceAware
					if (eName.equals("OrderedFactDefinition")) {
						//program.addFactType(new OrderedFactType((String)wd.get(0),Integer.parseInt((String)wd.get(1))));
					}
				}
			});
		} catch (SAXParseException spe) {
			// Error generated by the parser
			System.out.println("\n** Parsing error" + ", line "
					+ spe.getLineNumber() + ", uri " + spe.getSystemId());
			System.out.println("   " + spe.getMessage());

			// Use the contained exception, if any
			Exception x = spe;
			if (spe.getException() != null)
				x = spe.getException();
			x.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}