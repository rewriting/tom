/*
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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
 * <code>AllSeq(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Strategy combinator with one argument, that applies
 * this argument to all children.
 */

public class AllSeq extends AbstractStrategyCombinator {
  public final static int ARG = 0;

  public AllSeq(Strategy v) {
    initSubterm(v);
  }

  public final <T> T visitLight(T any, Introspector introspector) throws VisitFailure {
    int childCount = introspector.getChildCount(any);
    T result = any;
    for (int i = 0; i < childCount; i++) {
      Object newChild = arguments[ARG].visitLight(introspector.getChildAt(result,i),introspector);
      result = introspector.setChildAt(result, i, newChild);
    }
    return result;
  }

  public int visit(Introspector introspector) {
    int childCount = introspector.getChildCount(environment.getSubject());
    for(int i = 0; i < childCount; i++) {
      environment.down(i+1);
      int status = arguments[ARG].visit(introspector);
      if(status != Environment.SUCCESS) {
        environment.up();
        return status;
      } else {
        /* no need to restore subject */
        environment.up();
      }
    }
    return Environment.SUCCESS;
  }
}
