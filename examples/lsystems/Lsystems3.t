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

public class Lsystems3 implements LsystemsInterface { 
  
  %include { lsystems.tom }
  
  public Lsystems3(String[] args, LsystemsRuntime runtime) {
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
    Lsystems3 test = new Lsystems3(args,new LsystemsRuntime(args));
    test.run();
  }
  
  public void run() {
    runtime.run(matchls,n,delta,longueur);
    if (verbose) System.out.println(getLsystemsFactory().getPureFactory());
  }
  
  
// ------------------------------------------------------------
  
  private int n = 19;
  private int delta = 26;
  private int longueur = 8;
  
  public MatchLsystems matchls = new MatchLsystems() {
    
    public NodeList init() {
      return `concNode(F,r,F,l,F,l);
    }
    
    public NodeList apply(NodeList prev_rev, Node token, NodeList next) {
      %match(Node token, NodeList prev_rev, NodeList next) {
/*
      r,concNode(I1*,r,head*),concNode(I2*,r,tail*) -> { if (UCD(I1,extign) && UCD(I2,extign)) return `concNode(l); else runtime.iter++; }
      r,concNode(I1*,r,head*),concNode(I2*,l,tail*) -> { if (UCD(I1,extign) && UCD(I2,extign)) return `concNode(r); else runtime.iter++; }
      l,concNode(I1*,r,head*),concNode(I2*,r,tail*) -> { if (UCD(I1,extign) && UCD(I2,extign)) return `concNode(r); else runtime.iter++; }
      l,concNode(I1*,r,head*),concNode(I2*,l,tail*) -> { if (UCD(I1,extign) && UCD(I2,extign)) return `concNode(l,F,l); else runtime.iter++; }
      r,concNode(I1*,l,head*),concNode(I2*,r,tail*) -> { if (UCD(I1,extign) && UCD(I2,extign)) return `concNode(l); else runtime.iter++; }
      r,concNode(I1*,l,head*),concNode(I2*,l,tail*) -> { if (UCD(I1,extign) && UCD(I2,extign)) return `concNode(l,SubList(concNode(Left,F,l,F,l))); else runtime.iter++; }
      l,concNode(I1*,l,head*),concNode(I2*,r,tail*) -> { if (UCD(I1,extign) && UCD(I2,extign)) return `concNode(l); else runtime.iter++; }
      l,concNode(I1*,l,head*),concNode(I2*,l,tail*) -> { if (UCD(I1,extign) && UCD(I2,extign)) return `concNode(r); else runtime.iter++; }

*/
      // r < r > r -> l
      label1 : r(),concNode(I1*,r(),head*),concNode(I2*,r(),tail*) -> {
        if ( !runtime.UCD(I1,extign) ) {
          runtime.iter++;
          break label1;
        } else if ( !runtime.UCD(I2,extign) ) {
          runtime.iter++;
          break label1;
        } else {
          return `concNode(l);
        }
      }
      // r < r > l -> r
      label2: r(),concNode(I1*,r(),head*),concNode(I2*,l(),tail*) -> {
        if ( !runtime.UCD(I1,extign) ) {
          runtime.iter++;
          break label2;
        } else if ( !runtime.UCD(I2,extign) ) {
          runtime.iter++;
          break label2;
        } else {
          return `concNode(r);
        }
      }
      // r < l > r -> r
      label3: l(),concNode(I1*,r(),head*),concNode(I2*,r(),tail*) -> {
        if ( !runtime.UCD(I1,extign) ) {
          runtime.iter++;
          break label3;
        } else if ( !runtime.UCD(I2,extign) ) {
          runtime.iter++;
          break label3;
        } else {
          return `concNode(r);
        }
      }
      // r < l > l -> lFl
      label4: l(),concNode(I1*,r(),head*),concNode(I2*,l(),tail*) -> {
        if ( !runtime.UCD(I1,extign) ) {
          runtime.iter++;
          break label4;
        } else if ( !runtime.UCD(I2,extign) ) {
          runtime.iter++;
          break label4;
        } else {
          return `concNode(l,F,l);
        }
      }
      // r < l > r -> l
      label5: r(),concNode(I1*,l(),head*),concNode(I2*,r(),tail*) -> {
        if ( !runtime.UCD(I1,extign) ) {
          runtime.iter++;
          break label5;
        } else if ( !runtime.UCD(I2,extign) ) {
          runtime.iter++;
          break label5;
        } else {
          return `concNode(l);
        }
      }
      // l < r > l -> l[-FlFl]
      label6: r(),concNode(I1*,l(),head*),concNode(I2*,l(),tail*) -> {
        if ( !runtime.UCD(I1,extign) ) {
          runtime.iter++;
          break label6;
        } else if ( !runtime.UCD(I2,extign) ) {
          runtime.iter++;
          break label6;
        } else {
          return `concNode(l,SubList(concNode(Left,F,l,F,l)));
        }
      }
      label7: l(),concNode(I1*,l(),head*),concNode(I2*,r(),tail*) -> {
        if ( !runtime.UCD(I1,extign) ) {
          runtime.iter++;
          break label7;
        } else if ( !runtime.UCD(I2,extign) ) {
          runtime.iter++;
          break label7;
        } else {
          return `concNode(l);
        }
      }
      label8: l(),concNode(I1*,l(),head*),concNode(I2*,l(),tail*) -> {
        if ( !runtime.UCD(I1,extign) ) {
          runtime.iter++;
          break label8;
        } else if ( !runtime.UCD(I2,extign) ) {
          runtime.iter++;
          break label8;
        } else {
          return `concNode(r);
        }
      }

      Left(),_,_ -> { return `concNode(Right); }
      Right(),_,_ -> { return `concNode(Left); }
    }
    return `concNode(token);
  }
  };
  
  public Ignore ign = new Ignore() {
    public boolean apply(ATerm t) {
      %match(Node t) {
        Right() | Left() | F() -> { return true; }
      }
      return false;
    }
  };
  
  public Ignore extign = new Ignore() {
    public boolean apply(ATerm t) {
      %match(Node t) {
        (Right|Left|F)() -> { return true; }
		SubList[]        -> { return true; }
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

// l
  %op Node l {
    fsym {}
    is_fsym(t) { isl(t) }
    make() { makel() }
  }
  public Node makel() { return `Cell("l",concParam()); }
  public boolean isl(ATerm t) {
    %match(Node t) { Cell("l",_) -> { return true; } }
    return false;
  }

// r
  %op Node r {
    fsym {}
    is_fsym(t) { isr(t) }
    make() { maker() }
  }
  public Node maker() { return `Cell("r",concParam()); }
  public boolean isr(ATerm t) {
    %match(Node t) { Cell("r",_) -> { return true; } }
    return false;
  }

}
