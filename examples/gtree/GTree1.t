/*
 * Copyright (c) 2004, INRIA
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
 */

package gtree;

import aterm.*;
import aterm.pure.*;
import jtom.runtime.*;
import gtree.term.*;
import gtree.term.types.*;

public class GTree1 {

  private Factory factory;
  private GenericTraversal traversal;
  
  %include { term.tom }

  public GTree1(Factory factory) {
    this.factory = factory;
    this.traversal = new GenericTraversal();
  }

  public Factory getTermFactory() {
    return factory;
  }

  public void run(int n) {
    Tree query = `supT(node(nil,3,nil),ackT(node(nil,5,nil)));
    long startChrono = System.currentTimeMillis();
    Tree res         = eval(query);
    long stopChrono  = System.currentTimeMillis();

    System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");
    
  }

  public Tree eval(Tree tree) {
    %match(Tree tree) {
      supT(ackT(t1),ackT(t2)) -> { return `eval(ackT(eval(supT(t1,t2)))); }
      supT(nil(),x) -> { return x; }
      supT(x,nil()) -> { return x; }

      supT(node(l1,root1,r1),node(l2,root2,r2)) -> {
        int max = (root1>root2)?root1:root2;
        return `node(eval(supT(l1,l2)),max,eval(supT(r1,r2)));
      }

      ackT(x@nil()) -> { return x; }
      ackT(node(l1,root1,r1)) -> {
        return `node(eval(ackT(l1)),ack(2,root1),eval(ackT(r1)));
      }
        
    }
    
    return tree;
  }

    public int ack(int t1, int t2) {
      %match(int t1, int t2) {
        0, x -> { return x+1; }
        p, 0 -> { return p+1; }
        p, x -> { return ack(p-1,ack(p,x-1)); }
      }
    }
  
  public final static void main(String[] args) {
    GTree1 test = new GTree1(new Factory(new PureFactory()));

    test.run(0);
  }
  
}
