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
 * <p>
 * Strategy combinator with one strategy argument <code>s</code>, that
 * applies this strategy <code>s</code> to all children. If there exists one
 * child for which the strategy <code>s</code> fails, <code>All(s)</code>
 * fails. Applying <code>All</code> combinator to a constant always                                    
 * succeeds and behaves like the <code>Identity()</code> strategy.
 * <p>                                                 
 * <code>All(s)[f(t1,...,tn)]=f(t1',...,tn')</code> if <code>s[t1]=t1', ..., s[tn]=tn'</code>
 * <p>
 fails if there exists <code>i</code> such that <code>s[ti]</code> fails.
 * <p>
 * <code> All(s)[c]=c</code> is <code>c</code> is a constant 
 * <p>
 * Note that this All is parallel in the sense that s is applied to every child
 * before changing the current subject.
 * If you are interested in a sequential behaviour, {@link tom.library.sl.AllSeq}
 */       

public class All extends AbstractStrategyCombinator {
  public final static int ARG = 0;

  public All(Strategy v) {
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
  public final <T> T visitLight(T any, Introspector introspector) throws VisitFailure {
    Object[] children = null;
    int childCount = introspector.getChildCount(any);
    for (int i = 0; i < childCount; i++) {
      Object oldChild = introspector.getChildAt(any,i);
      Object newChild = arguments[ARG].visitLight(oldChild,introspector);
      if(children != null) {
        children[i] = newChild;
      } else if(newChild != oldChild) {
        // allocate the array, and fill it
        children = introspector.getChildren(any);
        children[i] = newChild;
      }
    }
    if(children==null) {
      return any;
    } else {
      return introspector.setChildren(any,children);
    }
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
    Object any = environment.getSubject();
    int childCount = introspector.getChildCount(any);

    Object[] children = null;

    for(int i = 0; i < childCount; i++) {
      Object oldChild = introspector.getChildAt(any,i);
      environment.down(i+1);
      int status = arguments[ARG].visit(introspector);
      if(status != Environment.SUCCESS) {
        environment.upLocal();
        return status;
      }
      Object newChild = environment.getSubject();
      if(children != null) {
        children[i] = newChild;
      } else if(newChild != oldChild) {
        // allocate the array, and fill it
        children = introspector.getChildren(any);
        children[i] = newChild;
      } 
      environment.upLocal();
    }
    if(children!=null) {
      environment.setSubject(introspector.setChildren(any,children));
    }
    return Environment.SUCCESS;
  }

}
