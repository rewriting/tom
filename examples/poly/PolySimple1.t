/*
 * Copyright (c) 2004-2005, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

package poly;

import aterm.*;
import aterm.pure.*;

public class PolySimple1 {
    
  private ATermFactory factory;
  private AFun fzero, fone, fa, fX, fplus, fmult;
  public ATermAppl tzero;
    
  public PolySimple1(ATermFactory factory) {
    this.factory = factory;
	
    fzero = factory.makeAFun("zero", 0, false);
    fone  = factory.makeAFun("one", 0, false);
    fa    = factory.makeAFun("a", 0, false);
    fX    = factory.makeAFun("X", 0, false);
    fmult = factory.makeAFun("mult" , 2, false);
    fplus = factory.makeAFun("plus", 2, false);
    tzero = factory.makeAppl(fzero);
  }    
    // Everything is an Aterm:
  %typeterm term {
    implement { ATerm }
    equals(t1, t2)      { (t1.equals(t2)) }
  }
    
    // My operators
  %op term zero {
    is_fsym(t) { (((ATermAppl)t).getAFun())==fzero }
  }
  %op term one {
    is_fsym(t) { (((ATermAppl)t).getAFun())==fone }
  }
  %op term a {
    is_fsym(t) { (((ATermAppl)t).getAFun())==fa }
  }
  %op term X {
    is_fsym(t) { (((ATermAppl)t).getAFun())==fX }
  }
  %op term plus(s1:term,s2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())==fplus }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0) }
    get_slot(s2,t) { ((ATermAppl)t).getArgument(1) }
		make(t1,t2) { plus(t1,t2)}
  }   
  %op term mult(s1:term, s2:term) {
    is_fsym(t)     { (((ATermAppl)t).getAFun())==fmult }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0) }
    get_slot(s2,t) { ((ATermAppl)t).getArgument(1) }
		make(t1,t2) { mult(t1,t2)}
  }

    
  public ATerm zero() {
    return tzero;
  }
  
  public ATerm one() {
    return factory.makeAppl(fone);
  } 

  public ATerm a() {
    return factory.makeAppl(fa);
  }
  
  public ATerm X() {
    return factory.makeAppl(fX);
  }
  
  public ATerm plus(ATerm arg1, ATerm arg2) {
    return factory.makeAppl(fplus, arg1, arg2);
  }
  
  public ATerm mult(ATerm arg1, ATerm arg2) {
    return factory.makeAppl(fmult,arg1, arg2 );
  } 
    
  public ATerm differentiate(ATerm poly) {
    %match(term poly) {
      a()             -> { return zero(); }
      X()             -> { return one(); }
      plus(arg1,arg2) -> { return plus(differentiate(`arg1),differentiate(`arg2)); }
      mult(arg1,arg2) -> { 
        ATerm res1 = `mult(arg1, differentiate(arg2));
        ATerm res2 = `mult(arg2, differentiate(arg1));
        return `plus(res1,res2);
      }
      _ -> { System.out.println("No match for: "+poly); }
	    
    }
    return null;
  }
    
  public void run() {
    ATerm t = mult(X(),plus(X(), a()));
    ATerm res = null;
	
    res = differentiate(t);
    System.out.println("Derivative form of " + t +" =\n\t" + res);
  }
    
  public final static void main(String[] args) {
    PolySimple1 test = new  PolySimple1(new PureFactory(16));
    test.run();
  }
}
