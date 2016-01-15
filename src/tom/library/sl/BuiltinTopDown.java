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
 * <p>
 * Strategy combinator with one strategy argument <code>s</code>, that
 * applies this strategy <code>s</code> in a top-down way to all children. 
 */       

public class BuiltinTopDown extends AbstractStrategyCombinator {
  public final static int ARG = 0;

  public BuiltinTopDown(Strategy v) {
    initSubterm(v);
  }

  /** 
   * Visit the subject any without managing any environment
   *
   * @param any the subject to visit
   * @param introspector the introspector
   * @return a Visitable
   * @throws VisitFailure if visitLight fails
   */ 
  public final <T> T visitLight(T subject, Introspector introspector) throws VisitFailure {
    // apply the strategy
    subject = arguments[ARG].visitLight(subject,introspector);
    // recursive calls
    Object[] children = null;
    int childCount = introspector.getChildCount(subject);
    for(int i = 0; i < childCount ; i++) {
      Object oldChild = introspector.getChildAt(subject,i);
      Object newChild = this.visitLight(oldChild,introspector);
      if(children != null) {
        children[i] = newChild;
      } else if(newChild != oldChild) {
        // allocate the array, and fill it
        children = introspector.getChildren(subject);
        children[i] = newChild;
      }
    }
    if(children!=null) {
      return introspector.setChildren(subject,children);
    }
    return subject;
  }

  /**
   * Visit the current subject (found in the environment)
   * and place its result in the environment.
   * Sets the environment flag to Environment.FAILURE in case of failure
   *
   * @param introspector the introspector
   * @return 0 if success
   */
  public int visit(Introspector introspector) {
    environment.setIntrospector(introspector);
    // apply the strategy
    int status = arguments[ARG].visit(introspector);
    if(status != Environment.SUCCESS) {
      return status;
    }
    // recursive calls
    Object subject = environment.getSubject();
    Object[] children = introspector.getChildren(subject);
    boolean modification = false;
    for(int i = 0; i < children.length ; i++) {
      environment.down(i+1);
      status = this.visit(introspector);
      if(status != Environment.SUCCESS) {
        environment.upLocal();
        return status;
      }
      Object newChild = environment.getSubject();
      if(newChild != children[i]) {
        children[i] = newChild;
        modification = true;
      }
      environment.upLocal();
    }
    if(modification) {
      environment.setSubject(introspector.setChildren(subject,children));
    }
    return Environment.SUCCESS;
  }

}
