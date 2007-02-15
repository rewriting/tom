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
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>T(t1,...,ti,...,tN).accept(Some(v)) = T(t1,...,ti.accept(v),...,tN)</code>
 * for each <code>ti</code> that succeeds.
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children. If no children are visited 
 * successfully, then Some(v) fails.
 * <p>
 * Note that side-effects of failing visits to children are not
 * undone.
 *
 */

public class Some extends AbstractMuStrategy {
  public final static int ARG = 0;
  public Some(VisitableVisitor v) {
    initSubterm(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    Visitable result = any;
    int successCount = 0;
    if(position==null) {
      for (int i = 0; i < childCount; i++) {
        try { 
          result = result.setChildAt(i,visitors[ARG].visit(any.getChildAt(i))); 
          successCount++;
        } catch(VisitFailure f) { }
      }
    } else {
      for (int i = 0; i < childCount; i++) {
        try { 
          position.down(i+1);
          Visitable newChild = visitors[ARG].visit(any.getChildAt(i));
          position.up();
          result = result.setChildAt(i,newChild); 
          successCount++;
        } catch(VisitFailure f) { 
          position.up();
        }
      }
    }
    if (successCount == 0) {
      throw new VisitFailure("Some: None of the " + 
                             childCount + " arguments of " +
                             any + " succeeded.");
    }
    return result;
  }

}
