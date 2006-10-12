/*
 * Copyright (c) 2004-2006, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this conc of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this conc of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the INRIA nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
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

package termgraph;

import tom.library.strategy.mutraveler.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;

import termgraph.lambdaterm.*;
import termgraph.lambdaterm.types.*;
import termgraph.lambdaterm.strategy.lambdaterm.*;
import termgraph.lambdaterm.types.lambdaterm.posLambdaTerm;

public class LambdaCalculus {

  %include{java/util/HashMap.tom}
  %include {mustrategy.tom }
  %include {strategy/graph.tom }
  %include {lambdaterm/lambdaterm.tom}
  %include {lambdaterm/_lambdaterm.tom}
  
  %strategy ToDeBruinj(table:HashMap) extends Identity(){
    visit LambdaTerm{
      
     app(arg1,arg2) -> {
        LambdaTerm t1 = (LambdaTerm) `ToDeBruinj(table).apply(`arg1);
        LambdaTerm t2 = (LambdaTerm) `ToDeBruinj(table).apply(`arg2);
        return `app(t1,t2);
     }

      abs[var=var,arg=arg] -> {
        Position old = (Position) table.get(`var);
        table.put(`var,getPosition());
        LambdaTerm t = (LambdaTerm) `ToDeBruinj(table).apply(`arg);
        if (old != null) {
          table.put(`var,old);
        }
        else{
          table.remove(`var);
        }
        return `abs2(t);
      }

      var[name=name] -> {
        if(table.containsKey(`name)){
          RelativePosition pos = RelativePosition.make(getPosition(),(Position)table.get(`name));
          int[] array = pos.toArray();
          LambdaTerm ref = `posLambdaTerm();
          for(int i=0;i<pos.depth();i++){
            ref = `posLambdaTerm(ref*,array[i]);
          }
          return ref;
        }
      }
    }
  }


  public static void main(String[] args){
    HashMap table = new HashMap();
    LambdaTerm t1 = `abs("x",abs("y",app(var("x"),var("y"))));
    t1 = (LambdaTerm) `ToDeBruinj(table).apply(t1); 
    System.out.println("l x.l y. x y :"+t1);
    table = new HashMap();
    LambdaTerm t2 = `app(abs("x",abs("y",app(var("x"),var("y")))),var("x"));
    t2 = (LambdaTerm)  `ToDeBruinj(table).apply(t2);
    System.out.println("(l x.l y. x y) x :"+t2);
  }

}
