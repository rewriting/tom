/*
 * Lsystems implementation for jtom
 * Copyright (c) 2003, LORIA-INRIA
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
import lsruntime.adt.lsystems.*;
import lsruntime.adt.lsystems.types.*;

public class Lsystems2 implements LsystemsInterface {
  
  %include { lsystems.tom }
  
  public Lsystems2(String[] args,LsystemsRuntime runtime) {
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
    Lsystems2 test = new Lsystems2(args,new LsystemsRuntime(args));
    test.run();
  }
  
  public void run() {
    runtime.run(matchls,n,delta,longueur);
    if (verbose) System.out.println(getLsystemsFactory().getPureFactory());
  }
  
  
// ------------------------------------------------------------
  
  private int n = 6;
  private int delta = 33;
  private int longueur = 5;
  
  public MatchLsystems matchls = new MatchLsystems() {
    
    public NodeList init() {
      return `concNode(F);
    }
    
    public NodeList apply(NodeList prev_rev, Node token, NodeList next) {
      %match(Node token) {
        // F .33-> F[+F]F[-F]F
        // F .33-> F[+F]F
        // F .34-> F[-F]F
        F() -> {
          int i = runtime.random();
          if      ( i>=0  && i<33  ) { return `concNode(F,SubList(concNode(Left,F)),F,SubList(concNode(Right,F)),F); }
          else if ( i>=33 && i<66  ) { return `concNode(F,SubList(concNode(Left,F)),F); }
          else if ( i>=66 && i<=100) { return `concNode(F,SubList(concNode(Right,F)),F); }
          else System.err.println("Erreur : i=" + i);
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

}
