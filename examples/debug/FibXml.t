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

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;
import jtom.runtime.*;
import java.util.*;

public class FibXml {
  
  %include{ TNode.tom }
    
  private XmlTools xtools;

  private Factory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }

  public static void main (String args[]) {
    FibXml test = new FibXml();
    test.run();
  }

  private TNode xml(TNode t) {
    return t;
  }
  
  public void run() {
    xtools = new XmlTools();


    for(int i=0 ; i<100 ; i++) {
      TNode N = int2peano(i);
      assertTrue( peano2int(plus(N,N)) == (i+i) );
    }

    for(int i=0 ; i<18 ; i++) {
      TNode N = int2peano(i);
      System.out.println("fib( " + i + ") = " + peano2int(fib(N)));
      //System.out.println("fibint = " + fibint(i));
      assertTrue( peano2int(fib(N)) == fibint(i) );
    }
    
  }

  TNode plus(TNode t1, TNode t2) {
    %match(TNode t1, TNode t2) {
      x,<zero/>    -> { return x; }
      x,<s>(y)</s> -> { return `xml(<s>plus(x,y)</s>); }
    }
    return null;
  }

  TNode fib(TNode t) {
    %match(TNode t) {
      <zero/>              -> { return `xml(<s><zero/></s>); }
      <s>(<zero/>)</s>     -> { return `xml(<s><zero/></s>); }
      <s>(<s>(x)</s>)</s>  -> { return plus(fib(x),fib(`xml(<s>x</s>))); }
    }
    return null;
  }

  public TNode int2peano(int n) {
    TNode N = `xml(<zero/>);
    for(int i=0 ; i<n ; i++) {
      N = `xml(<s>N</s>);
    }
    return N;
  }

  public int peano2int(TNode N) {
    %match(TNode N) {
      <zero/>    -> { return 0; }
      <s>(x)</s> -> {return 1+peano2int(x); }
    }
    return 0;
  }

  public int fibint(int n) {
    if(n<=1) {
      return 1;
    } else {
      return fibint(n-1)+fibint(n-2);
    }
  }

  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }


}
