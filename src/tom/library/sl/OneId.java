/*
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 * <code>T(t1,...,ti,...,tN).accept(OneId(v)) = T(t1,...,ti.accept(v),...,tN)</code>
 * if <code>ti</code> is the first child that is modified.
 * <p>
 * Strategy combinator with one argument, that applies
 * this argument to exactly one child. If no children are visited 
 * successfully, then OneId(v) fails.
 * <p>
 * Note that side-effects of failing visits to children are not
 * undone.
 */

public class OneId extends AbstractStrategyCombinator {
  public final static int ARG = 0;

  public OneId(Strategy v) {
    initSubterm(v);
  }

  public <T> T visitLight(T any, Introspector introspector) throws  VisitFailure {
    int childCount = introspector.getChildCount(any);
    for (int i = 0; i < childCount; i++) {
      Object newSubterm = arguments[ARG].visitLight(introspector.getChildAt(any,i),introspector);
      if (newSubterm != introspector.getChildAt(any,i)) {
        return introspector.setChildAt(any,i,newSubterm);
      } 
    } 
    return any;
  }

  /**
   *  Visits the current subject (found in the environment)
   *  and place its result in the environment.
   *  Sets the environment flag to Environment.FAILURE in case of failure
   */
  public int visit(Introspector introspector) {
    int childCount = introspector.getChildCount(environment.getSubject());
    //Object originalsubject = environment.getSubject();
    for(int i = 0; i < childCount; i++) {
      environment.down(i+1);
      Object oldSubject = environment.getSubject();
      int status = arguments[ARG].visit(introspector);
      Object newSubject = environment.getSubject();
      if(status == Environment.SUCCESS && oldSubject!=newSubject) {
        environment.up();
        return Environment.SUCCESS;
      } else {
        environment.upLocal();
      }
    }
    return Environment.SUCCESS;
  }
}
