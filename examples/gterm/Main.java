/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
package gterm;

public class Main {

	public static void main(String[] args) {
		Empty e1 = Empty.make();
    System.out.println("e1.isEmpty() = "+e1.isEmpty());

		System.out.println("e1 = " + e1);
		System.out.println("e1.name = " + e1.getName());

		Empty e2 = Empty.make();
		System.out.println("e1==e2: " + (e1==e2));

		Element p1 = Plop.make();
		Element p2 = Plop.make();
		List l1 = Cons.make(p1,e1);
		List l2 = Cons.make(p2,e2);
		System.out.println("l1 = " + l1.toATerm().toString());
		System.out.println("l1==l2: " + (l1==l2));

		Int i1 = Int.make(1);
		Int i2 = Int.make(1);
		System.out.println("i1==i2: " + (i1==i2));


		List l3 = Cons.make(i1,l1);
		List l4 = Cons.make(i2,l2);
		System.out.println("l3 = " + l3.toATerm().toString());
		System.out.println("l3==l4: " + (l3==l4));

		List l5 = List.fromTerm(l4.toATerm());
		System.out.println("l3==l4: " + (l3==l5));
		System.out.println("l5 = " + l5.toString());
	}
}
