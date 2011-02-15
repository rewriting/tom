/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
package antipattern.baralgo;

import aterm.*;
import aterm.pure.*;

import antipattern.*;
import antipattern.term.*;
import antipattern.term.types.*;


import tom.library.sl.*;

public class ReplaceSystem extends AbstractStrategyBasic {
	
	%include{ ../term/Term.tom }
	%include{ sl.tom }

    private Term variable;
    private Term value;
    private boolean isIdentity;

    public ReplaceSystem(Term variable, Term value, Strategy visitor) {
      super(visitor);
      this.variable = variable;
      this.value = value;
      this.isIdentity = (visitor.getClass().equals(`Identity().getClass()) ? 
    		  true : false );       
    }
   
    public <T> T visitLight(T o, Introspector i) throws VisitFailure { 
      if(o==variable) {
        return (T)value;
      } 
      return (isIdentity ? o : (T)`Fail().visitLight(o,i));
    }
  }  
