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
package tom.library.sl;


/**                                                                                                                
 * <p>                                                 
 * Basic strategy combinator with one strategy argument <code>s</code>, that
 * applies this strategy <code>s</code> to exactly one child. If for all children
 * the strategy <code>s</code> fails, <code>One(s)</code>
 * fails. Applying <code>One</code> combinator to a constant always                                    
 * fails.
 * <p>
 * <code>One(s)[f(t1,...,ti,...,tn)]=f(t1,...,ti',...,,tn)</code> if <code>s[t1], ..., s[ti-1]</code> fail and <code>s[ti]=ti'</code>
 * <p>
 fails if <code>s[t1],...,s[tn]</code> fail.
 * <p>
 * <code> One(s)[c]</code> fails if <code>c</code> is a constant
 * <p>
 */       

public class One extends AbstractStrategy {
  public final static int ARG = 0;

  public One(Strategy v) {
    initSubterm(v);
  }

  /** Method herited from the apply() method of mutraveler library
   * @deprecated use fire() instead
   */ 
  public jjtraveler.Visitable visit(jjtraveler.Visitable any) throws jjtraveler.VisitFailure {
    int childCount = any.getChildCount();
    for(int i = 0; i < childCount; i++) {
      try {
        jjtraveler.Visitable newChild = visitors[ARG].visit(any.getChildAt(i));
        return any.setChildAt(i,newChild);
      } catch(jjtraveler.VisitFailure f) { }
    }
    throw new jjtraveler.VisitFailure();
  }

  /**
   *  Visits the current subject (found in the environment)
   *  and place its result in the environment.
   *  Sets the environment flag to Environment.FAILURE in case of failure
   */
  public void visit() {
    int childCount = environment.getSubject().getChildCount();
    //Visitable originalsubject = environment.getSubject();

    for(int i = 0; i < childCount; i++) {
      environment.down(i+1);
      (visitors[ARG]).visit();
      if(environment.getStatus() == Environment.SUCCESS) {
        environment.up();
        return;
      } else {
        environment.upLocal();
        /* Forget about the failure, to try another subterm */
        environment.setStatus(Environment.SUCCESS);
        //environment.setSubject(originalsubject);
      }
    }
    /* If we reach this point, there is a real failure */
    environment.setStatus(Environment.FAILURE);
    return;
  }
}
