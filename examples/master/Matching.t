/*
 * Copyright (c) 2004-2010, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
package master;

import master.matching.term.types.*;

class Matching {
  %gom {
    // ...
    module Term
    imports String
    abstract syntax
    Term = Variable(name:String)
         | Appl(name:String, args:TermList)
         | True()
         | False()
         | Match(pattern:Term, subject:Term)
         | And(a1:Term, a2:Term)
         | decomposeList(l1:TermList, l2:TermList)
    TermList = cons(head:Term,tail:TermList)
             | nil()

    module Term:rules() {
    decomposeList(nil(),nil()) -> True()
    decomposeList(cons(h1,t1),cons(h2,t2)) -> And(Match(h1,h2),decomposeList(t1,t2))
    // Delete
    Match(Appl(name,nil()),Appl(name,nil())) -> True()
    // Decompose
    Match(Appl(name,a1),Appl(name,a2)) -> decomposeList(a1,a2)
    // SymbolClash
    Match(Appl(name1,_),Appl(name2,_)) -> False() //if name1 != name2
    // PropagateClash
    And(False(),_) -> False()
    And(_,False()) -> False()
    // PropagateSuccess
    And(True(),x) -> x
    And(x,True()) -> x
    And(x,x) -> x
    }
  }

  //-------------------------------------------------------

  public void run() {
    Term p1 = `Appl("f",cons(Variable("x"),nil()));
    Term s1 = `Appl("f",cons(Appl("a",nil()),nil()));
    
    Term p2 = `Appl("f",cons(Variable("x"),cons(Appl("g",cons(Variable("y"),nil())),nil())));
    Term s2 = `Appl("f",cons(Appl("a",nil()),cons(Appl("g",cons(Appl("b",nil()),nil())),nil())));

    Term p3 = `Appl("f",cons(Appl("a",nil()),cons(Appl("g",cons(Variable("y"),nil())),nil())));
    Term s3 = `Appl("f",cons(Appl("b",nil()),cons(Appl("g",cons(Appl("b",nil()),nil())),nil())));

    System.out.println("running...");
    System.out.println("p1 = " + p1);
    System.out.println("s1 = " + s1);
    System.out.println("match(p1,s1) = " + `Match(p1,s1));
    System.out.println("p2 = " + p2);
    System.out.println("s2 = " + s2);
    System.out.println("match(p2,s2) = " + `Match(p2,s2));
    System.out.println("match(p3,s3) = " + `Match(p3,s3));
  }
  
  public final static void main(String[] args) {
    Matching test = new Matching();
    test.run();
  }


}
