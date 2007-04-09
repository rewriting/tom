/*
 *
 * Copyright (c) 2000-2007, Pierre-Etienne Moreau
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
 * 
 **/
package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import jjtraveler.Visitable;

/**
 * <code>Omega(i,v)</code>
 * <p>
 * Basic visitor combinator which applies v the i-th subterm
 * 0-th subterm is the term itself
 * 1-th subterm corresponds to the first subterm
 * ...
 * arity-th subterm corresponds to the last subterm
 * <p>
*/

public class Omega extends AbstractMuStrategy {
  public final static int ARG = 0;
  protected int indexPosition;

  public Omega(int indexPosition, VisitableVisitor v) {
    initSubterm(v);
    this.indexPosition = indexPosition;
  }

  public int getPos() {
    return indexPosition;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    if(indexPosition==0) {
      return visitors[ARG].visit(any);
    } else if(indexPosition>0 && indexPosition<=any.getChildCount()) {
      int childNumber = indexPosition-1;
      if(position==null) {
        Visitable newChild = visitors[ARG].visit(any.getChildAt(childNumber));
        return any.setChildAt(childNumber,newChild);
      } else {
        try {
          position.down(indexPosition);
          Visitable newChild = visitors[ARG].visit(any.getChildAt(childNumber));
          position.up();
          return any.setChildAt(childNumber,newChild);
        } catch(VisitFailure f) {
          position.up();
          throw new VisitFailure();
        }
      }
    } else {
      throw new VisitFailure();
    }
  }
}
