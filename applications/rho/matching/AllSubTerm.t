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

package matching;

import aterm.pure.SingletonFactory;
import matching.term.*;
import matching.term.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

import java.util.*;
import java.io.*;

public class AllSubTerm {
  private termFactory factory;
	private Collection bag = new HashSet();
	private Collection bagPos = new HashSet();
  public AllSubTerm(termFactory factory) {
    this.factory = factory;
  }

  public termFactory getTermFactory() {
    return factory;
  }
  %include { term/term.tom }
  %include { mutraveler.tom }
  
  public final static void main(String[] args) {
    AllSubTerm test = new AllSubTerm(termFactory.getInstance(SingletonFactory.getInstance()));
    test.run();
  }

  public void run() {
		Term subject = `f(f(a));
		Term subTerm = `f(b);
		VisitableVisitor collect = new Collect();
		VisitableVisitor getAll = `mu(MuVar("x"),
																	Sequence(collect,All(MuVar("x"))));
		VisitableVisitor getPos=new GetPos(subTerm);
		VisitableVisitor getAllPos=`mu(MuVar("x"),
																	Sequence(getPos,All(MuVar("x"))));
		System.out.println("subject = " + subject);
		try{
			MuTraveler.init(getAll).visit(subject);
			System.out.println("All the subterms are given by " + bag);
			MuTraveler.init(getAllPos).visit(subject);
			System.out.println("The positions of the subterm " + subTerm + " are given by " +bagPos);
			System.out.println("All the subsets of the set of subterms: "+allSubCollection(bag));
		}
		catch(Exception e){
			System.out.println(e);
		}
	} 
	//return a list of collection consisting of all the subCollection of a non-empty collection
	public List allSubCollection(Collection c){
		List result=new ArrayList();
		if (c.size() == 1){
			result.add(c);
			return result;
		}
		else{ 
			Iterator c_it=c.iterator();
			Object e=c_it.next();
			Collection h =new HashSet();
			h.add(e);
			result.add(new HashSet(h));
			c.remove(e);
			List l=allSubCollection(c);
			Iterator it=l.iterator();
			while(it.hasNext()){
				Collection s1=(Collection)it.next();
				result.add(new HashSet(s1));
				s1.add(e);
				result.add(new HashSet(s1));
			}
			return result;
		}
	}
	class Collect extends termVisitableFwd {
		public Collect() {
			super(`Fail());
		}
		public Term visit_Term(Term arg) throws VisitFailure { 
			%match(Term arg){
				x -> {bag.add(arg);}
			}
			return arg;
		}
	}
	class GetPos extends termVisitableFwd {
		Term a;
		public GetPos(Term a) {
			super(`Fail());
			this.a=a;
		}
		public Term visit_Term(Term arg) throws VisitFailure { 
			%match(Term arg){
				x -> {
					if (arg == a) {
						System.out.println("j'ai trouve un sous-terme");
						bagPos.add(MuTraveler.getPosition(this));
					}
				}
			}
			return arg;
		}
	}
}

