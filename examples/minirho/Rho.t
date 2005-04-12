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


package minirho;

 import aterm.*;
 import aterm.pure.*;
 import minirho.rho.rhoterm.*;
 import minirho.rho.rhoterm.types.*;

 import jjtraveler.reflective.VisitableVisitor;
 import jjtraveler.VisitFailure;

public class Rho {
    private rhotermFactory factory;
    
    public Rho(rhotermFactory factory) {
	this.factory = factory;
    }
    public rhotermFactory getRhotermFactory() {
	return factory;
    }

    %include { mutraveler.tom }
    %vas{
	module rhoterm
	    imports 
	    public
	    sorts RTerm Constraint Subst ListConstraint ListSubst
	    abstract syntax
	    var(na:String) -> RTerm
	    const(na:String) -> RTerm
	    abs(lhs:RTerm,rhs:RTerm) -> RTerm
	    app(lhs:RTerm,rhs:RTerm) -> RTerm
	    struct(lhs:RTerm,rhs:RTerm) -> RTerm //structure is couple
	    appC(co:ListConstraint,term:RTerm) -> RTerm
	    appS(su:ListSubst,term:RTerm) -> RTerm
	    
	    andC( Constraint* ) -> ListConstraint
	    andS( Constraint* ) -> ListSubst
	    match(lhs:RTerm,rhs:RTerm) -> Constraint 
	    eq(var:RTerm,rhs:RTerm) -> Subst 
	    
	    }  
    public final static void main(String[] args) {
	Rho rhoEngine = new Rho(rhotermFactory.getInstance(new PureFactory(16)));
	rhoEngine.run();
    }
    public void run() {
	RTerm subject = `app(abs(var("X"),var("X")),const("a"));
	VisitableVisitor rules = new ReductionRules();
	try {
	    System.out.println("subject       = " + subject);
	    System.out.println("onceBottomUp  = " + `OnceBottomUp(rules).visit(subject));
	} catch (VisitFailure e) {
	    System.out.println("reduction failed on: " + subject);
	}
	
    }
    class ReductionRules extends rhotermVisitableFwd {
	public ReductionRules() {
	    super(`Fail());
	}
	public RTerm visit_RTerm(RTerm arg) throws  VisitFailure { 
	    %match(RTerm arg){
		app(struct(M1,M2),N) -> {return `struct(app(M1,N),app(M2,N));}
		
	    }	    
	    throw new VisitFailure();
	}
    }

}
 
