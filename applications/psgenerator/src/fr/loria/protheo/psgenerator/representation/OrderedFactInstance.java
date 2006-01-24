/*
 * Created on 13 août 2004
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

import java.util.List;

/**
 * @author mmoossen
 */
public class OrderedFactInstance extends FactInstance {
	public OrderedFactInstance(FactType type, String comments, List values) {
		super(type, comments);
		if (values.size()!=getType().getArity()) {
			throw new IllegalArgumentException("Expected arity for fact "+type.getName()+" is: "+type.getArity() + ", and received arity: "+values.size());
		}
		for (int i = 0; i < values.size(); i++) {
			this.values.put(new Integer(i).toString(), values.get(i));			
		}
	}

	public String getValue(int index) {
		if (index < 0 || index >= getType().getArity())
			throw new IllegalArgumentException("index out of range");
		return (String) values.get(new Integer(index).toString());
	}

	public String toString() {
		String ret = getType() + "(";
		for (int i = 0; i < getType().getArity(); i++) {
			if (i != 0)
				ret += ", ";
			ret += getValue(i);
		}
		return ret + ")";
	}
}