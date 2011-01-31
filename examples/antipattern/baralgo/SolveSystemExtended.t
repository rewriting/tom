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

public class SolveSystemExtended extends SolveSystem {    

  %include{ ../term/Term.tom }
  %include{ sl.tom }

  public SolveSystemExtended(Collection c,Strategy vis) {
    super(c,vis);            
  }

  public <T> T visitLight(T o, Introspector i) throws VisitFailure {
    T result = null;
    try{
      result = super.visitLight(o,i);
      //if it didn't entered the match of the super.visit_Constraint
      //than maybe it will enter the one of the extendedVisit
      if (isIdentity && o == result){
        return (T)extendedVisit(o,i);
      }
      return result;
    } catch(VisitFailure vf) { //can only happen when the visit failed
      return (T)extendedVisit(o,i);
    }    	
  }

  private Object extendedVisit(Object o, Introspector i) throws VisitFailure {
    if (o instanceof Constraint) {
      Constraint arg = (Constraint) o;
      %match(Constraint arg) { 

        GreaterThan(Appl(var1,_),Appl(var2,_)) ->{
          if (Integer.parseInt(`var1) > Integer.parseInt(`var2)){
            return `True();
          }

          return `False();
        }
        Neg(GreaterThan(Appl(var1,_),Appl(var2,_))) ->{
          if (Integer.parseInt(`var1) > Integer.parseInt(`var2)){
            return `False();
          }

          return `True();
        }
        LessThan(Appl(var1,_),Appl(var2,_)) ->{
          if (Integer.parseInt(`var1) < Integer.parseInt(`var2)){
            return `True();
          }

          return `False();
        }        
        Neg(LessThan(Appl(var1,_),Appl(var2,_))) ->{
          if (Integer.parseInt(`var1) < Integer.parseInt(`var2)){
            return `False();
          }

          return `True();
        }

      }
    }
    return (isIdentity ? o : (Constraint)`Fail().visitLight(o,i));
  }  

}
