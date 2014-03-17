/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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

import aterm.*;
import lsruntime.*;
import lsruntime.adt.lsystems.*;
import lsruntime.adt.lsystems.types.*;

public class Lsystems6 implements LsystemsInterface {
  
  %include { lsystems.tom }
  
  public Lsystems6(String[] args,LsystemsRuntime runtime) {
    this.runtime = runtime;
    if(args.length > 0) {
      for(int i=0; i < args.length; i++) { 
        if(args[i].charAt(0) == '-') {
          if(args[i].equals("--verbose")) {
            this.verbose = true;
          }
        }
      }
    }
  }
  
  private LsystemsRuntime runtime;
  public Factory getLsystemsFactory() {
    return runtime.getLsystemsFactory();
  }
 
  public boolean verbose;
  
  public final static void main(String[] args) {
    Lsystems6 test = new Lsystems6(args,new LsystemsRuntime(args));
    test.run();
  }
  
  public void run() {
    runtime.run(matchls,n,delta);
    if (verbose) System.out.println(getLsystemsFactory().getPureFactory());
  }
  
  
// ------------------------------------------------------------
  
  private int n = 10;
  private int delta = 60;
  
  public MatchLsystems matchls = new MatchLsystems() {
    
    public NodeList init() {
      return `concNode(F,Right,SubList(concNode(Left)),SubList(concNode(Left,A,A)),A,B,A,A,A,Left,SubList(concNode(Left,B,B)),B);
    }
    
    public NodeList apply(NodeList prev_rev, Node token, NodeList next) {
      %match(NodeList prev_rev,Node token, NodeList next) {
        // F < A -> F
        concNode(head*,F(),I*),A(),_ -> {
          if (runtime.UCD(I,extign)) {
            return `concNode(F);
          }
        }
        // F > B -> B F
        _,F(),concNode(I*,B(),tail*) -> {
          if (runtime.UCD(I,extign)) {
            return `concNode(B,F);
          }
        }
        // F > [ B ] B -> F F
        _,F(),concNode(I1*,SubList(concNode(I2*,B(),tail1*)),I3*,B(),tail2*) -> {
          if (runtime.UCD(I1,ign) && runtime.UCD(I2,ign) && runtime.UCD(I3,ign)) {
            return `concNode(F,F);
          }
        }
        // A < B > A -> A
        concNode(head*,A(),I1*),B(),concNode(I2*,A(),tail*) -> {
          if (runtime.UCD(I1,ign) && runtime.UCD(I2,ign)) {
            return `concNode(A);
          }
        }
        // F -> A    Attention : à mettre à la fin
        _,F(),_ -> {
          return `concNode(A);
        }
      }
      return `concNode(token);
    }
  };

  public Ignore ign = new Ignore() {
    public boolean apply(ATerm t) {
      %match(Node t) {
        (Right | Left)() -> { return true; }
      }
      return false;
    }
  };
  
  public Ignore extign = new Ignore() {
    public boolean apply(ATerm t) {
      %match(Node t) {
        (Right|Left)() -> { return true; }
				SubList[] -> { return true; }
      }
      return false;
    }
  };

// F
  %op Node F {
    fsym {}
    is_fsym(t) { isF(t) }
    make() { makeF() }
  }
  public Node makeF() { return `Cell("F",concParam()); }
  public boolean isF(ATerm t) {
    %match(Node t) { Cell("F",_) -> { return true; } }
    return false;
  }

// A
  %op Node A {
    fsym {}
    is_fsym(t) { isA(t) }
    make() { makeA() }
  }
  public Node makeA() { return `Cell("A",concParam()); }
  public boolean isA(ATerm t) {
    %match(Node t) { Cell("A",_) -> { return true; } }
    return false;
  }

// B
  %op Node B {
    fsym {}
    is_fsym(t) { isB(t) }
    make() { makeB() }
  }
  public Node makeB() { return `Cell("B",concParam()); }
  public boolean isB(ATerm t) {
    %match(Node t) { Cell("B",_) -> { return true; } }
    return false;
  }

}
