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
