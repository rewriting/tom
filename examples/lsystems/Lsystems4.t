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

public class Lsystems4 implements LsystemsInterface {
  
  %include { lsystems.tom }
  
  public Lsystems4(String[] args,LsystemsRuntime runtime) {
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
    Lsystems4 test = new Lsystems4(args,new LsystemsRuntime(args));
    test.run();
  }
  
  public void run() {
    runtime.run(matchls,n,delta);
    if (verbose) System.out.println(getLsystemsFactory().getPureFactory());
  }
  
  
// ------------------------------------------------------------
  
  private int n = 8;
  private int delta = 60;
  
  public MatchLsystems matchls = new MatchLsystems() {
    
    public NodeList init() {
      return `concNode(B(2.0),A(4.0,4.0));
    }
    
    public NodeList apply(NodeList prev_rev, Node token, NodeList next) {
      %match(Node token) {
        // B(x) : x < 1 -> F
        // B(x) : x >= 1 -> B(x-1)
        B(x) -> {
          if ( x < 1) {
            return `concNode(F);
          } else {
            return `concNode(B(x-1));
          }
        }
        // A(x,y) : y < 3 -> A(x*2,x+y)
        // A(x,y) : y >= 3 -> B(x) A(x/y,0)
        A(x,y) -> {
          if (y < 3) {
            return `concNode(A(x*2,x+y));
          } else {
            double t = x / y;
            return `concNode(B(x),A(t,0.0));
          }
        }
      }
      return `concNode(token);
    }
  };


// A(@,@)
  %op Node A(arg1:double,arg2:double) {
    fsym {}
    is_fsym(t) { isA(t) }
    get_slot(arg1,t) { t.getPara().getHead().getArg() }
    get_slot(arg2,t) { t.getPara().getTail().getHead().getArg() }
    make(arg1,arg2) { makeA(arg1,arg2) }
  }
  public Node makeA(double arg1,double arg2) { return `Cell("A",concParam(p(arg1),p(arg2))); }
  public boolean isA(ATerm t) {
    %match(Node t) { Cell("A",_) -> { return true; } }
    return false;
  }

// B(@)
  %op Node B(arg:double) {
    fsym {}
    is_fsym(t) { isB(t) }
    get_slot(arg,t) { t.getPara().getHead().getArg() }
    make(arg) { makeB(arg) }
  }
  public Node makeB(double arg) { return `Cell("B",concParam(p(arg))); }
  public boolean isB(ATerm t) {
    %match(Node t) { Cell("B",_) -> { return true; } }
    return false;
  }

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

}
