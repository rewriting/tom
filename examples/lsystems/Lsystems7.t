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
import aterm.pure.*;
import lsruntime.*;
import lsruntime.adt.lsystems.*;
import lsruntime.adt.lsystems.types.*;
public class Lsystems7 implements LsystemsInterface {
  
  %include { lsystems.tom }
  
  public Lsystems7(String[] args,LsystemsRuntime runtime) {
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
    Lsystems7 test = new Lsystems7(args,new LsystemsRuntime(args,new Factory(new PureFactory())));
    test.run();
  }
  
  public void run() {
    runtime.run(matchls,n,delta);
    if (verbose) System.out.println(getLsystemsFactory().getPureFactory());
  }
  
  
// ------------------------------------------------------------
  
  private int n = 8;
  private int delta = 85;
  
  public MatchLsystems matchls = new MatchLsystems() {
    
    public NodeList init() {
      double init = 300.0;
      return `concNode(F(init,0.0));
    }
    
    private double pa = 0.3;
    private double q = 1 - pa;
    private double h = Math.sqrt(pa*q);
    
    public NodeList apply(NodeList prev_rev, Node token, NodeList next) {
      %match(Node token) {
        // F(x,t) : t=0 -> F(x*pa,2) - F(x*h,1) + + F(x*h,1) - F(x*q,0)
        // F(x,t) : t>0 -> F(x,t-1)
        F(x,t) -> {
          if (t == 0.0) {

            return `concNode(F(x*pa,2.0),Left,F(x*h,1.0),Right,Right,F(x*h,1.0),Left,F(x*q,0.0));
          } else {
            return `concNode(F(x,t-1));
          }
        }
      }
      return `concNode(token);
    }
  };


// F(@,@)
  %op Node F(arg1:double,arg2:double) {
    fsym {}
    is_fsym(t) { isF(t) }
    get_slot(arg1,t) { t.getPara().getHead().getArg() }
    get_slot(arg2,t) { t.getPara().getTail().getHead().getArg() }
    make(arg1,arg2) { makeF(arg1,arg2) }
  }
  public Node makeF(double arg1,double arg2) { return `Cell("F",concParam(p(arg1),p(arg2))); }
  public boolean isF(ATerm t) {
    %match(Node t) { Cell("F",_) -> { return true; } }
    return false;
  }

}
