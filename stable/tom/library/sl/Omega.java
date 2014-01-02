/*
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
 * 	- Neither the name of the Inria nor the names of its
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
 * <code>Omega(i,v)</code>
 * <p>
 * Strategy combinator which applies v the i-th subterm
 * 0-th subterm is the term itself
 * 1-th subterm corresponds to the first subterm
 * ...
 * arity-th subterm corresponds to the last subterm
 * <p>
*/

public class Omega extends AbstractStrategyCombinator {
  public final static int ARG = 0;
  protected int indexPosition;

  public Omega(int indexPosition, Strategy v) {
    initSubterm(v);
    this.indexPosition = indexPosition;
  }

  public int getPos() {
    return indexPosition;
  }

  public <T> T visitLight(T any, Introspector introspector) throws VisitFailure {
    if(indexPosition==0) {
      return arguments[ARG].visitLight(any,introspector);
    } else if(indexPosition>0 && indexPosition<=introspector.getChildCount(any)) {
      int childNumber = indexPosition-1;
      Object newChild = arguments[ARG].visitLight(introspector.getChildAt(any,childNumber),introspector);
      return introspector.setChildAt(any,childNumber,newChild);
    } else {
      throw new VisitFailure();
    }
  }

  public int visit(Introspector introspector) {
    if(indexPosition==0) {
      return arguments[ARG].visit(introspector);
    } else if(indexPosition>0 && indexPosition<=introspector.getChildCount(environment.getSubject())) {
      environment.down(indexPosition);
      int status = arguments[ARG].visit(introspector);
      environment.up();
      return status;
    } else {
      //setStatus(Environment.FAILURE);
      return Environment.FAILURE;
    }
  }
}
