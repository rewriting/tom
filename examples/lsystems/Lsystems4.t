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
import java.util.*;
import java.lang.*;
import lsruntime.*;
import lsruntime.adt.*;

public class Lsystems4 implements LsystemsInterface {
  
  %include { lsystems.tom }
  %include { extras.tom }
  
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
  public LsystemsFactory getLsystemsFactory() {
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
          if ( y < 3) {
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
