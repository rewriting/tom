/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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

public class Lsystems1 implements LsystemsInterface {
  
  %include { lsystems.tom }
  
  public Lsystems1(String[] args,LsystemsRuntime runtime) {
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
    Lsystems1 test = new Lsystems1(args,new LsystemsRuntime(args));
    test.run();
  }
  
  
  public void run() {
    runtime.run(matchls,n,delta,longueur);
    //if (verbose) System.out.println(getLsystemsFactory().getPureFactory());
  }
  
  
// ------------------------------------------------------------
  
  private int n = 1;
  private int delta = 30;
  private int longueur = 2;
  
  public MatchLsystems matchls = new MatchLsystems() {
    
    public NodeList init() {
      return `concNode(X);
    }
    
    public NodeList apply(NodeList prev_rev, Node token, NodeList next) {
      %match(Node token) {
        // F -> FF
        F() -> {
          return `concNode(F(),F());
        }
        
        // X -> F[+X][-X]FX
        X() -> {
          return `concNode(F(),SubList(concNode(Left,X())),SubList(concNode(Right,X())),F(),X());
        }
        
      }
      return `concNode(token);
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

// X
  %op Node X {
    fsym {}
    is_fsym(t) { isX(t) }
    make() { makeX() }
  }
  public Node makeX() { return `Cell("X",concParam()); }
  public boolean isX(ATerm t) {
    %match(Node t) { Cell("X",_) -> { return true; } }
    return false;
  }

}
