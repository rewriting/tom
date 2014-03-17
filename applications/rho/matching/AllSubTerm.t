/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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
 * 	- Neither the name of the Inria nor the names of its
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

import matching.term.*;
import matching.term.types.*;

import tom.library.sl.Strategy;
import tom.library.sl.Position;
import tom.library.sl.Identity;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

import java.util.*;
import java.io.*;

public class AllSubTerm {
	private Collection bag = new HashSet();
	private Collection bagPos = new HashSet();

  %include { term/term.tom }
  %include { util/types/Collection.tom }
  %include { sl.tom }
  
  public final static void main(String[] args) {
    AllSubTerm test = new AllSubTerm();
    test.run();
  }

  public void run() {
		Term subject = `f(f(a()));
		Term subTerm = `f(b());
		Strategy collect = `Collect(bag);
		Strategy getAll = `mu(MuVar("x"),
																	Sequence(collect,All(MuVar("x"))));
		Strategy getPos= `GetPos(subTerm,bagPos);
		Strategy getAllPos=`mu(MuVar("x"),
																	Sequence(getPos,All(MuVar("x"))));
		System.out.println("subject = " + subject);
		try{
			getAll.visit(subject);
			System.out.println("All the subterms are given by " + bag);
			getAllPos.visit(subject);
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
		} else { 
			Iterator c_it=c.iterator();
			Object e=c_it.next();
			Collection h =new HashSet();
			h.add(e);
			result.add(new HashSet(h));
			c.remove(e);
			List l=allSubCollection(c);
			Iterator it=l.iterator();
			while(it.hasNext()) {
				Collection s1=(Collection)it.next();
				result.add(new HashSet(s1));
				s1.add(e);
				result.add(new HashSet(s1));
			}
			return result;
		}
	}
  %strategy Collect(bag:Collection) extends Fail() {
		visit Term {
      x -> { bag.add(`x); }
    }
	}
  %strategy GetPos(a:Term,bagPos:Collection) extends Fail() {
		visit Term { 
      x -> {
        if (`x == a) {
          System.out.println("j'ai trouve un sous-terme");
          bagPos.add(getPosition());
        }
      }
    }
  }
}

