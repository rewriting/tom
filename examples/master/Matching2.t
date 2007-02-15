/*
 * Copyright (c) 2004-2007, INRIA
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

import master.matching2.term.types.*;

class Matching2 {
  %gom {
    module Term
    imports String
    abstract syntax
    Term = Variable(name:String)
         | Appl(name:String, args:TermList)
         | True()
         | False()
         | Match(pattern:Term, subject:Term)
         | And(l:TermList)
    TermList = conc( Term* )
    //decomposeList(l1:TermList, l2:TermList) -> TermList
  }

  private static TermList decomposeList(TermList l1, TermList l2) {
    %match(TermList l1, TermList l2) {
      conc(),conc() -> { return `conc(True()); }
      conc(h1,t1*),conc(h2,t2*) -> {
        TermList t3 = `decomposeList(t1,t2);
        return `conc(Match(h1,h2),t3*);
      }
    }
    return `conc();
  }
/*
  %rule {
    decomposeList(conc(),conc()) -> conc()
    decomposeList(conc(h1,t1*),conc(h2,t2*)) -> conc(Match(h1,h2),t3*) where t3:=decomposeList(t1,t2)
  }
*/
  %rule {
    // Delete
    Match(Appl(name,conc()),Appl(name,conc())) -> True()

    // Decompose
    Match(Appl(name,a1),Appl(name,a2)) -> And(decomposeList(a1,a2))

    // SymbolClash
    Match(Appl(name1,args1),Appl(name2,args2)) -> False()
  }

  %rule {
    // PropagateClash
    And(conc(_*,False(),_*)) -> False()
    // PropagateSuccess
    And(conc(X*,True(),Y*)) -> And(conc(X*,Y*))
    And(conc(X*,True(),Y*)) -> And(conc(X*,Y*))
    And(conc(X*,c,Y*,c,Z*)) -> And(conc(X*,c,Y*,Z*))
    And(conc()) -> True()
  }

  //-------------------------------------------------------

  public void run() {
    Term p1 = `Appl("f",conc(Variable("x")));
    Term s1 = `Appl("f",conc(Appl("a",conc())));

    Term p2 = `Appl("f",conc(Variable("x"),Appl("g",conc(Variable("y")))));
    Term s2 = `Appl("f",conc(Appl("a",conc()),Appl("g",conc(Appl("b",conc())))));

    Term p3 = `Appl("f",conc(Appl("a",conc()),Appl("g",conc(Variable("y")))));
    Term s3 = `Appl("f",conc(Appl("b",conc()),Appl("g",conc(Appl("b",conc())))));

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
    Matching2 test = new Matching2();
    test.run();
  }

}
