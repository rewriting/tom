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
 * applies this strategy <code>s</code> to all children. If there exists one
 * child for which the strategy <code>s</code> fails, <code>All(s)</code>
 * fails. Applying <code>All</code> combinator to a constant always                                    
 * succeeds and behaves like the <code>Identity()</code> strategy.                                                  * <p>                                                 
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

public class All extends AbstractStrategy {
  public final static int ARG = 0;

  public All(Strategy v) {
    initSubterm(v);
  }

  /** Method herited from the apply() method of mutraveler library
   * @deprecated use fire() instead
   */ 
  public final jjtraveler.Visitable visit(jjtraveler.Visitable any) throws jjtraveler.VisitFailure {
    int childCount = any.getChildCount();
    jjtraveler.Visitable result = any;
    if (any instanceof Visitable) {
      jjtraveler.Visitable[] childs = null;
      for (int i = 0; i < childCount; i++) {
        jjtraveler.Visitable oldChild = any.getChildAt(i);
        jjtraveler.Visitable newChild = visitors[ARG].visit(oldChild);
        if(childs != null) {
          childs[i] = newChild;
        } else if(newChild != oldChild) {
          // allocate the array, and fill it
          childs = ((Visitable) any).getChildren();
          childs[i] = newChild;
        }
      }
      if(childs!=null) {
        result = ((Visitable) any).setChildren(childs);
      }
    } else {
      for (int i = 0; i < childCount; i++) {
        jjtraveler.Visitable newChild = visitors[ARG].visit(result.getChildAt(i));
        result = result.setChildAt(i, newChild);
      }
    }
    return result;
  }

  /**
   *  Visits the current subject (found in the environment)
   *  and place its result in the environment.
   *  Sets the environment flag to Environment.FAILURE in case of failure
   */
  public void visit() {
    Visitable any = environment.getSubject();
    int childCount = any.getChildCount();

    Visitable[] childs = null;
    for (int i = 0; i < childCount; i++) {
      Visitable oldChild = (Visitable)any.getChildAt(i);
      environment.down(i+1);
      visitors[ARG].visit();
      if(environment.getStatus() != Environment.SUCCESS) {
        environment.up();
        return;
      }
      Visitable newChild = environment.getSubject();
      if(childs != null) {
        childs[i] = newChild;
        environment.up();
        /* restore subject */
        environment.setSubject(any);
      } else if(newChild != oldChild) {
        // allocate the array, and fill it
        // childs = (Visitable[])any.getChildren();
        jjtraveler.Visitable[] array = any.getChildren();
        childs = new Visitable[childCount];
        for(int j = 0; j < array.length; j++) {
          childs[j] = (Visitable) array[j];
        }
        childs[i] = newChild;
        environment.up();
        /* restore subject */
        environment.setSubject(any);
      } else {
        /* no need to restore subject */
        environment.up();
      }
    }
    if(childs!=null) {
      environment.setSubject((Visitable)any.setChildren(childs));
    }
    return;
  }
}
