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
package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.Visitor;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>All(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children.
 */

public class All extends AbstractMuStrategy {
  public final static int ARG = 0;
  public All(VisitableVisitor v) {
    initSubterm(v);
  }

  public final Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    Visitable result = any;
    if (any instanceof MuVisitable) {
      Visitable[] childs = null;
      if(position==null) {
        for (int i = 0; i < childCount; i++) {
          Visitable oldChild = any.getChildAt(i);
          Visitable newChild = visitors[ARG].visit(oldChild);
          if(childs != null) {
            childs[i] = newChild;
          } else if(newChild != oldChild) {
            // allocate the array, and fill it
            childs = ((MuVisitable) any).getChilds();
            childs[i] = newChild;
          }
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            Visitable oldChild = any.getChildAt(i);
            position.down(i+1);
            Visitable newChild = visitors[ARG].visit(oldChild);
            position.up();
            if(childs != null) {
              childs[i] = newChild;
            } else if(newChild != oldChild) {
              // allocate the array, and fill it
              childs = ((MuVisitable) any).getChilds();
              childs[i] = newChild;
            }
          }
        } catch(VisitFailure f) {
          position.up();
          throw new VisitFailure();
        }
      }
      if(childs!=null) {
        result = ((MuVisitable) any).setChilds(childs);
      }
    } else {
      if(position==null) {
        for (int i = 0; i < childCount; i++) {
          Visitable newChild = visitors[ARG].visit(result.getChildAt(i));
          result = result.setChildAt(i, newChild);
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            //System.out.println(" -> " + getArgument(0).getClass() + ".visit(" + result.getChildAt(i) + ")");
            //System.out.println("All.pos = " + getPosition());
            position.down(i+1);
            Visitable newChild = visitors[ARG].visit(result.getChildAt(i));
            position.up();
            result = result.setChildAt(i, newChild);
          }
        } catch(VisitFailure f) {
          position.up();
          throw new VisitFailure();
        }
      }
    }
    return result;
  }

}
