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

public class Lsystems1 implements LsystemsInterface {
  
  %include { lsruntime/adt/lsystems.tom }
  %include { lsruntime/extras.tom }
  
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
  public LsystemsFactory getLsystemsFactory() {
    return runtime.getLsystemsFactory();
  }
 
  public boolean verbose;
  
  public final static void main(String[] args) {
    Lsystems1 test = new Lsystems1(args,new LsystemsRuntime(args));
    test.run();
  }
  
  
  public void run() {
    runtime.run(matchls,n,delta,longueur);
    if (verbose) System.out.println(getLsystemsFactory().getPureFactory());
  }
  
  
// ------------------------------------------------------------
  
  private int n = 10;
  private int delta = 45;
  private int longueur = 3;
  
  public MatchLsystems matchls = new MatchLsystems() {
    
    public NodeList init() {
      return `concNode(X);
    }
    
    public NodeList apply(NodeList prev_rev, Node token, NodeList next) {
      %match(Node token) {
        // F -> FF
        F -> {
          return `concNode(F(),F());
        }
        
        // X -> F[+X][-X]FX
        X -> {
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
