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
package antipattern.diffterms;

import aterm.*;
import aterm.pure.*;

import antipattern.*;
import antipattern.term.*;
import antipattern.term.types.*;


import tom.library.sl.*;

public class DecomposeAP extends AbstractStrategyBasic {

  %include{ ../term/Term.tom }
  %include{ sl.tom }

  protected boolean isIdentity;

  public static int varCounter = 0;

  public DecomposeAP(Strategy vis) {
    super(vis);		 
    this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
        true : false ); 
  }

  public Object visitLight(Object o, Introspector i) throws VisitFailure {		
    if (o instanceof Term) {
      Term arg = (Term) o;
      %match(Term arg){
        // first rule
        Anti(p) ->{
          return `TermDiff(Variable("x_" + varCounter++),p);
        }
        // second one
        Appl(name, concTerm(X*,TermDiff(firstTerm,secondTerm),Y*)) -> {
          return `TermDiff(Appl(name, concTerm(X*,firstTerm,Y*)),
              Appl(name, concTerm(X*,secondTerm,Y*)));
        }			
      }
    }
    return (isIdentity ? o : (Constraint)`Fail().visitLight(o,i));
  }		
}
