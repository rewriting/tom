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
 * <code>OmegaU(v)</code>
 * <p>
 * Basic visitor combinator which applies Omega(i,v) with
 * i in [1..arity] with a uniform probability
 * i.e. with probability 1/getChildCount
 * <p>
*/

public class OmegaU extends AbstractStrategy {
  public final static int ARG = 0;
  private static java.util.Random random = null;

  public OmegaU(Strategy v) {
    initSubterm(v);
    if(random == null) {
      random = new java.util.Random();
    }
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable any) throws jjtraveler.VisitFailure {
    int arity = any.getChildCount();
    int selectedSubterm = Math.abs(random.nextInt(arity));
    if(arity==0) {
      return visitors[ARG].visit(any);
    } else {
      return new Omega(selectedSubterm+1,visitors[ARG]).visit(any);
    }
  }

  public int visit() {
    Visitable subject = environment.getSubject();
    int arity = subject.getChildCount();
    int selectedSubterm = Math.abs(random.nextInt(arity));
    if(arity==0) {
      return visitors[ARG].visit();
    } else {
      environment.down(selectedSubterm+1);
      int status = visitors[ARG].visit();
      environment.up();
      return status;
    }
  }
}
