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



import java.util.Collection;

import tom.library.sl.*;

public class ReverseEngAp extends AbstractStrategyBasic {

  %include{ ../term/Term.tom }
  %include{ sl.tom }

  protected boolean isIdentity;

  public ReverseEngAp(Strategy vis) {
    super(vis);      
    this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
        true : false );      
  }

  public <T> T visitLight(T o, Introspector i) throws VisitFailure {
    if (o instanceof Constraint) {
      Constraint arg = (Constraint) o;
      %match(Constraint arg) {
        Neg(Match(a,b)) -> {             
          return (T) `Match(Anti(a),b);
        }        
        And(concAnd(X*,Match(q1,t1),Z*,Match(q2,t2),Y*)) -> {
          // return `Match(conc);
          System.out.println("ici");
          return (T) `And(concAnd(X*,Match(Appl("conc",concTerm(q1,q2)),Appl("conc",concTerm(t1,t2))),Z*,Y*));
        }
        And(concAnd(Match(a,b))) ->{
          return (T) `Match(a,b);
        }
      }
    }
    return (isIdentity ? o : (T)`Fail().visitLight(o,i));
  }

}
