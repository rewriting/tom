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
//package ls_ex;

import aterm.*;
import java.util.*;
import java.lang.*;
import lsruntime.*;
import lsruntime.adt.*;

public class Lsystems5 implements LsystemsInterface {
  
  %include { lsystems.signature }
  %include { extras.t }
  
  public Lsystems5(String[] args,LsystemsFactory factory) {
    this.factory = factory;
    runtime = new LsystemsRuntime(args,factory);
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
  
  public boolean verbose;
  
  public final static void main(String[] args) {
    Lsystems5 test = new Lsystems5(args,new LsystemsFactory());
    test.run();
  }
  
  private LsystemsRuntime runtime;
  
  public void run() {
    runtime.run(matchls,n,delta);
    if (verbose) System.out.println(factory);
  }
  
  
// ------------------------------------------------------------
  
  private int n = 12;
  private int delta = 85;
  
  public MatchLsystems matchls = new MatchLsystems() {
    
    public NodeList init() {
      double init = 200.0;
      return `concNode(A(init));
    }
    
    double R = 1.456;
    
    public NodeList apply(NodeList prev_rev, Node token, NodeList next) {
      %match(Node token) {
      // A(s) -> F(s) [+A(s/R)] [-A(s/R)]
        A(s) -> {
            double sr = s / R;
            return `concNode(F(s),SubList(concNode(Left,A(sr))),SubList(concNode(Right,A(sr))));
          }
      }
      return `concNode(token);
    }
  };
  

// F(@)
  %op Node F(arg:double) {
    fsym {}
    is_fsym(t) { isF(t) }
    get_slot(arg,t) { t.getPara().getHead().getArg() }
    make(arg) { makeF(arg) }
  }
  public Node makeF(double arg) { return `Cell("F",concParam(p(arg))); }
  public boolean isF(ATerm t) {
    %match(Node t) { Cell("F",_) -> { return true; } }
    return false;
  }
  
// A(@)
  %op Node A(arg:double) {
    fsym {}
    is_fsym(t) { isA(t) }
    get_slot(arg,t) { t.getPara().getHead().getArg() }
    make(arg) { makeA(arg) }
  }
  public Node makeA(double arg) { return `Cell("A",concParam(p(arg))); }
  public boolean isA(ATerm t) {
    %match(Node t) { Cell("A",_) -> { return true; } }
    return false;
  }

}
