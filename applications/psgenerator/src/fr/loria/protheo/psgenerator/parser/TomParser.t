/*
 * Created on 15 sept. 2004
 *
 * Copyright (c) 2004-2007, Michael Moossen
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import aterm.*;
import tom.library.traversal.*;
import fr.loria.protheo.psgenerator.representation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *  The TomParser class
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
public class TomParser extends AbstractParser {
    %include{adt/TNode.tom}
    
	private XmlTools xtools;
	private ATerm document;

	private GenericTraversal traversal = new GenericTraversal();

	private TNodeFactory getTNodeFactory() {
		return xtools.getTNodeFactory();
	}

	public TomParser(String fileName) {
        super(fileName);
	}

	public void readFile(String fileName) {
		xtools = new XmlTools();
		document = xtools.convertXMLToATerm(fileName);
    }
	
	public void parse() {
		parse(document);		
	}

  private List slotNamesList(ATerm t) {
	final List slotNames = Collections.checkedList(new Vector(), String.class);
    Collect1 collect = new Collect1 () {
	public boolean apply(ATerm t) {
	    if (t instanceof TNode) {
	      %match(TNode t) {
		<SlotName>#TEXT(slotName)</SlotName> -> {
		  slotNames.add(slotName);
		  return false;
		}
		_ -> {
		  return true;
		}
	      }
	    } else {
	      return true;
	    }
	}
      };
    traversal.genericCollect(t, collect);
    return slotNames;
  }

  private List slotValuesList(ATerm t) {
	final List slotValues = Collections.checkedList(new Vector(), String.class);
    Collect1 collect = new Collect1 () {
	public boolean apply(ATerm t) {
	    if (t instanceof TNode) {
	      %match(TNode t) {
		<SlotValue>#TEXT(slotValue)</SlotValue> -> {
		  slotValues.add(slotValue);
		  return false;
		}
		_ -> {
		  return true;
		}
	      }
	    } else {
	      return true;
	    }
	}
      };
    traversal.genericCollect(t, collect);
    return slotValues;
  }

  private Map slotsList(ATerm t) {
	final Map slots = Collections.checkedMap(new HashMap(), String.class, String.class);
    Collect1 collect = new Collect1 () {
	public boolean apply(ATerm t) {
	    if (t instanceof TNode) {
	      %match(TNode t) {
	      <Slot>
	      	<SlotName>#TEXT(slotName)</SlotName> 
	      	<SlotValue>#TEXT(slotValue)</SlotValue> 
		  </Slot> -> {
		  slots.put(slotName, slotValue);
		  return false;
		}
		_ -> {
		  return true;
		}
	      }
	    } else {
	      return true;
	    }
	}
      };
    traversal.genericCollect(t, collect);
    return slots;
  }

/*
how to use java variables/constantes for identifying XML elements/attributes names? like in this case:
		<UNORDERED_FACT_DEFINITION>
			content*
		</UNORDERED_FACT_DEFINITION>
		-> { ... }
where UNORDERED_FACT_DEFINITION is a constant equals to "UnorderedFactDefinition"

related to that: 
it could be nice to reuse method slotValuesList() for implementing slotNamesList()
the only diff is the XML element's name, which could be given as an additional argument
*/
/*
how to handle efficiently optional attributes and elements without to try all combinations??, like in the following case:
	      <psp-spec name=progName version=progVersion>
	      	<Comments>#TEXT(comments)</Comments> 
	      	<FactDefinitions>factDefs*</FactDefinitions> 
	      	<InitialWorkingMemory>initialFacts*</InitialWorkingMemory> 
	      	<InitialProductionMemory>knowledgeBase*</InitialProductionMemory> 
	      </psp-spec> -> { ... }
where both attributes name and version, and the element Comments are all optional
*/
/*
how to handle efficiently unsorted lists of elements efficiently??, like in this case:
		<UnorderedFactDefinition>
			<SlotName>id</SlotName>
			<Comments>fact type representing a house</Comments>
			<SlotName>color</SlotName>
			<SlotName>price</SlotName>
			<FactName>house</FactName>
			<SlotName>forrent</SlotName>
		</UnorderedFactDefinition>
i know... AC-matching is not in question :(
... but my DomParser does it very efficiently...
*/
	private void parse(ATerm subject) {
		Collect1 collect = new Collect1() {
			public boolean apply(ATerm t) {
				if (t instanceof TNode) {
	    %match(TNode t) {
	      <psp-spec name=progName version=progVersion>
	      	<Comments>#TEXT(comments)</Comments> 
	      	<FactDefinitions>factDefs*</FactDefinitions> 
	      	<InitialWorkingMemory>initialFacts*</InitialWorkingMemory> 
	      	<InitialProductionMemory>knowledgeBase*</InitialProductionMemory> 
	      </psp-spec> -> {
                program = new Program(progName, progVersion, comments);
				parse(`factDefs);
				parse(`initialFacts);
				parse(`knowledgeBase);		
				return false;
	      }

	      <UnorderedFactDefinition>
	      	<FactName>#TEXT(factName)</FactName> 
	      	<Comments>#TEXT(comment)</Comments> 
			slotNamesT*
		  </UnorderedFactDefinition> -> {
		     List slotNames = slotNamesList(slotNamesT);
 			 program.addFactType(new UnorderedFactType(factName,comment,slotNames));
		  }

	      <OrderedFactDefinition>
	      	<Comments>#TEXT(comment)</Comments> 
	      	<FactName>#TEXT(factName)</FactName> 
	      	<FactArity>#TEXT(factArity)</FactArity> 
		  </OrderedFactDefinition> -> {
			 int arity = Integer.parseInt(factArity);
 			 program.addFactType(new OrderedFactType(factName,comment,arity));
		  }
		  
		  <UnorderedFactInstance>
	      	<Comments>#TEXT(comment)</Comments> 
			<FactName>#TEXT(factName)</FactName>
			slotsT*
		  </UnorderedFactInstance> -> {
			FactType type = program.getFactType(factName);
			if (type.getType()!=FactType.UNORDERED_TYPE) {
				throw new IllegalStateException("FactType "+factName+" is not unordered");
			}
		    Map slots = slotsList(slotsT);
			program.addInitialFact(type.newInstance(comment,slots));
		  }
		  
		  <OrderedFactInstance>
	      	<Comments>#TEXT(comment)</Comments> 
			<FactName>#TEXT(factName)</FactName>
			slotValuesT*
		  </OrderedFactInstance> -> {
			FactType type = program.getFactType(factName);
			if (type.getType()!=FactType.ORDERED_TYPE) {
				throw new IllegalStateException("FactType "+factName+" is not ordered");
			}
		    List slotValues = slotValuesList(slotValuesT);
			program.addInitialFact(type.newInstance(comment,slotValues));
		  }
		  
		 	      _ -> {
							return true;
						}
					}	// match
				} else {
					return true;
				}
			} //apply
		};
		traversal.genericCollect(subject, collect);
	} //parse
}