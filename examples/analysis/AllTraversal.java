/*
 *
 * Copyright (c) 2000-2011, INPL, INRIA
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

package analysis;

import tom.library.sl.*;

public class AllTraversal extends tom.library.sl.AbstractStrategyCombinator {
  public final static int ARG = 0;

  public AllTraversal(Strategy v) {
    initSubterm(v);
  }

  /** 
   *  Visits the subject any without managing any environment
   */ 
  public final <T> T visitLight(T any, Introspector m) throws VisitFailure {
    int childCount = m.getChildCount(any);
    for (int i = 0; i < childCount; i++) {
      Object child = m.getChildAt(any,i);
      getVisitor(ARG).visitLight(child,m);
    }
    return any;
  }

  /**
   *  Visits the current subject (found in the environment)
   *  and place its result in the environment.
   *  Sets the environment flag to Environment.FAILURE in case of failure
   */
  public int visit(Introspector m) {
    environment.setIntrospector(m);
    Object any = environment.getSubject();
    int childCount = m.getChildCount(any);

    for(int i = 0; i < childCount; i++) {
      environment.down(i+1);
      int status = getVisitor(ARG).visit(m);
      environment.upLocal();
      if(status != Environment.SUCCESS) {
        return status;
      }
    }
    return Environment.SUCCESS;
  }

}
