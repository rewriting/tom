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
package tom.library.sl;

/**
 * <code>All(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children.
 */

public class All extends AbstractStrategy {
  public final static int ARG = 0;

  public All(Strategy v) {
    initSubterm(v);
  }

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

  public void visit() throws jjtraveler.VisitFailure {
    Visitable any = getSubject();
    int childCount = any.getChildCount();

    Visitable[] childs = null;
    try {
      for (int i = 0; i < childCount; i++) {
        /* restore subject */
        setSubject(any);
        Visitable oldChild = (Visitable)any.getChildAt(i);
        environment.down(i+1);
        (visitors[ARG]).visit();
        Visitable newChild = getSubject();
        if(childs != null) {
          childs[i] = newChild;
        } else if(newChild != oldChild) {
          // allocate the array, and fill it
          // childs = (Visitable[])any.getChildren();
          jjtraveler.Visitable[] array = any.getChildren();
          childs = new Visitable[childCount];
          for(int j = 0; j < array.length; j++) {
            childs[j] = (Visitable) array[j];
          }
          childs[i] = newChild;
        }
        environment.up();
      }
    } catch(jjtraveler.VisitFailure f) {
      environment.up();
      throw new jjtraveler.VisitFailure();
    }

    if(childs!=null) {
      setSubject((Visitable)any.setChildren(childs));
    }
    return;
  }

}
