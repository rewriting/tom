/*
 * Created on 16 août 2004
 *
 * Copyright (c) 2004-2006, Michael Moossen
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
package fr.loria.protheo.psgenerator.representation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author mmoossen
 */
public class Program {
	private final String name;
	private final String version;
	private final String comments;
	private List factTypes = Collections.checkedList(new ArrayList(), FactType.class);
	private List initialFacts = Collections.checkedList(new ArrayList(), FactInstance.class);
	private List productionRules = Collections.checkedList(new ArrayList(), Rule.class);
	
	public Program(String name, String version, String comments) {
		this.name = name;
		this.version = version;
		this.comments = comments;
	}
	
	public String getComments() {
		return comments;
	}
	
	public String getName() {
		return name;
	}
	
	public String getVersion() {
		return version;
	}
	
	public List getFactTypes() {
		return factTypes; //Collections.unmodifiableList(factTypes);
	}
	
	public List getInitialFacts() {
		return initialFacts; //Collections.unmodifiableList(initialFacts);
	}
	
	public List getProductionRules() {
		return productionRules; //Collections.unmodifiableList(rules);
	}

	public void addFactType(FactType factType) {
		factTypes.add(factType);
	}

	public void addInitialFact(FactInstance initialFact) {
		initialFacts.add(initialFact);
	}

	public void addProductionRule(Rule rule) {
		productionRules.add(rule);
	}

	public FactType getFactType(String factType) {
		for (Iterator it = factTypes.iterator(); it.hasNext();) {
			FactType type = (FactType) it.next();
			if (type.getName().equals(factType))
				return type;
		}
		return null;
	}
}
