/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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

#include<stdio.h>
#include<stdlib.h>

#include <aterm1.h>
#include <aterm2.h>

ATermList genere(int n);
ATermList elim(ATermList l);

%include{ int.tom }
%typeterm List {
	implement      { ATermList }
	is_sort(t)      { 0==0 }
	equals(l1,l2)  { l1 == l2 }
}
%oplist List conc( int* ) {
	is_fsym(t)     { 1 }
	get_head(l)    { ((ATermInt)ATgetFirst(l))->value }
	get_tail(l)    { ATgetNext(l) }
	is_empty(l)    { ATisEmpty(l) }
	make_empty()   { (ATempty) }
	make_insert(e,l) { ATinsert(l,(ATerm)ATmakeInt(e)) }
}

ATermList genere(int n)
{
	ATermList l;
	if(n>2) {
		l = genere(n-1);
		return `conc(n,l*);
	} else {
		return `conc(2);
	}
}

ATermList elim(ATermList l)
{
	%match(List l) {
		conc(x*,e1,y*,e2,z*) -> {
			if(`e2%`e1 == 0) {
				return `elim(conc(x*,e1,y*,z*));
			}
		}
	}
	return l;
}

ATermList reverse(ATermList l)
{
	ATermList res = `conc();
	while(1) {
		%match(List l) {
			conc() -> { return res; }
			conc(h,t*) -> {
				res = `conc(h,res*);
				l = `t;
			}
		}
	}
}

int main(int argc, char *argv[])
{
  int i;
	int max = 100;
  for(i=1; i<argc; i++)
    if(strcmp(argv[i], "-max") == 0)
      max = atoi(argv[++i]);

  ATinitialize(argc, argv);
  ATermList l;

  l = genere(max);
  l = reverse(l);
	l = elim(l);
  /* ATprintf("primes up to %d: %, l\n", max, l); */
}

