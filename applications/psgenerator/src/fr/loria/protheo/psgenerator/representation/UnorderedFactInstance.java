/*
 * Created on 13 août 2004
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
package fr.loria.protheo.psgenerator.representation;

import java.util.Iterator;
import java.util.Map;

/**
 * @author mmoossen
 */
public class UnorderedFactInstance extends FactInstance {
	public UnorderedFactInstance(FactType type, String comments, Map values) {
		super(type, comments);
		if (values.size()!=getFactType().getSlotNames().size()) {
			throw new IllegalArgumentException("Expected arity: "+type.getArity() + ", readed arity: "+values.size() + " for fact type "+type.getName());
		}
		for (Iterator it = getFactType().getSlotNames().iterator(); it.hasNext();) {
			String slotName = (String) it.next();
			if (!values.containsKey(slotName)) {
				throw new IllegalArgumentException("value for slot "+slotName+" missing for fact type "+type.getName());
			}
		}
		this.values.putAll(values);
	}
	
	public UnorderedFactType getFactType() {
		return (UnorderedFactType)getType();
	}
	
	public String getValue(String property) {
		if (property==null)
			throw new NullPointerException("property should not be null");
		
		if (!getFactType().hasSlot(property))
			throw new IllegalArgumentException("this fact type does not support any property called " +property);
		
		return (String)values.get(property);
	}
	
	public void setValue(String property, String value) {
		if (value==null)
			throw new NullPointerException("value should not be null");
		
		if (property==null)
			throw new NullPointerException("property should not be null");
		
		if (!getFactType().hasSlot(property))
			throw new IllegalArgumentException("this fact type does not support any property called " +property);
		
		values.put(property, value);
	}

	public String toString() {
		String ret = getType() + "(";
		boolean first = true;
		for (Iterator it = values.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			if (!first)
				ret += ", ";
			else
				first = false;
			ret += key+":="+values.get(key);
		}
		return ret + ")";
	}
}
