/*
 * Lsystems implementation for jtom
 * Copyright (C) 2003, LORIA-INRIA
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

import aterm.*;
import lsruntime.*;
import lsruntime.adt.*;

public class Lsystems6 implements LsystemsInterface {
  
  %include { lsruntime/adt/lsystems.tom }
  %include { lsruntime/extras.tom }
  
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
  public LsystemsFactory getLsystemsFactory() {
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
        concNode(head*,F,I*),A,_ -> {
          if (runtime.UCD(I,extign)) {
            return `concNode(F);
          }
        }
        // F > B -> B F
        _,F,concNode(I*,B,tail*) -> {
          if (runtime.UCD(I,extign)) {
            return `concNode(B,F);
          }
        }
        // F > [ B ] B -> F F
        _,F,concNode(I1*,SubList(concNode(I2*,B,tail1*)),I3*,B,tail2*) -> {
          if (runtime.UCD(I1,ign) && runtime.UCD(I2,ign) && runtime.UCD(I3,ign)) {
            return `concNode(F,F);
          }
        }
        // A < B > A -> A
        concNode(head*,A,I1*),B,concNode(I2*,A,tail*) -> {
          if (runtime.UCD(I1,ign) && runtime.UCD(I2,ign)) {
            return `concNode(A);
          }
        }
        // F -> A    Attention : à mettre à la fin
        _,F,_ -> {
          return `concNode(A);
        }
      }
      return `concNode(token);
    }
  };

  public Ignore ign = new Ignore() {
    public boolean apply(ATerm t) {
      %match(Node t) {
        Right | Left -> { return true; }
      }
      return false;
    }
  };
  
  public Ignore extign = new Ignore() {
    public boolean apply(ATerm t) {
      %match(Node t) {
        Right | Left | SubList -> { return true; }
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
