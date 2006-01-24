/*
 * Created on 10 août 2004
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
import java.util.Map;

/**
 * @author mmoossen
 */
public class FactType {
	public static final int ORDERED_TYPE = 1;
	public static final int UNORDERED_TYPE = 2;
	
	private final String name;
	private final int arity;
	private final int type;
	private final String comment; 
	
	protected FactType(String name, String comment, int arity, int type) {
		if (name==null || name.trim().equals("")) {
			throw new IllegalArgumentException("fact type name can not be empty nor null");
		}
		if (name.indexOf(' ')>=0) {
			throw new IllegalArgumentException(name + " is an invalid fact type name, should not contain spaces");
		}
		if (arity<0) {
			throw new IllegalArgumentException("arity has to be greater or equals than zero");
		}
		if (type!=ORDERED_TYPE && type!=UNORDERED_TYPE) {
			throw new IllegalArgumentException("unknow fact type. see TYPE constants of FactType");
		}
		this.name = name;
		this.arity = arity;
		this.comment = comment;
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

	public int getArity() {
		return arity;
	}

	public String getName() {
		return name;
	}
	
	public String getComment() {
		return comment;
	}
	
	public FactInstance newInstance(String comments, List values) {
		if (type!=ORDERED_TYPE)
			throw new IllegalArgumentException("for unordered facts use a Map for values");
		return new OrderedFactInstance(this, comments, values);
	}
	
	public FactInstance newInstance(String comments, Map values) {
		if (type!=UNORDERED_TYPE)
			throw new IllegalArgumentException("for ordered facts use a List for values");
		return new UnorderedFactInstance(this, comments, values);
	}
}
