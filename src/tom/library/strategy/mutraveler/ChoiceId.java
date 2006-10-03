/*
 *
 * Copyright (c) 2000-2006, Pierre-Etienne Moreau
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
 * <code>x.accept(ChoiceId(v1,v2)) = x.accept(v1) if x.accept(v1)!=x</code>
 * <code>x.accept(ChoiceId(v1,v2)) = x.accept(v2) if x.accept(v1)=x</code>
 * <p>
 * Basic visitor combinator with two visitor arguments, that applies
 * these visitors one after the other (sequential composition), if the first
 * one is not the identity.
 */

public class ChoiceId extends AbstractMuStrategy {
	public final static int FIRST = 0;
	public final static int THEN = 1;

  //private VisitableVisitor S_FIRST;
  //private VisitableVisitor S_THEN;
  public ChoiceId(VisitableVisitor first, VisitableVisitor then) {
    initSubterm(first,then);
    //S_FIRST = first;
    //S_THEN = then;
  }

  public ChoiceId(VisitableVisitor v1, VisitableVisitor v2, VisitableVisitor v3) {
    initSubterm(v1,new ChoiceId(v2, v3));
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    Visitable v = visitors[FIRST].visit(any);
    if (v == any) {
      return visitors[THEN].visit(v);
    } else {
      return v;
    }
    
  }
  
}
