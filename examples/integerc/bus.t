/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

/*
 * This example illsutrate the use of list-matching
 * on a simple message exchanging bug
 */
#include<stdio.h>
#include<stdlib.h>

#define A    ((void*)0)
#define B    ((void*)1)
#define F    ((void*)2)
#define MSG  ((void*)3)
#define CONS ((void*)4)

#define setDest(msg,dest) (msg->subterm[0]=(dest))
#define setOrig(msg,orig) (msg->subterm[1]=(orig))
#define setData(msg,data) (msg->subterm[2]=(data))

struct term {
  int symbol;
  int arity;
  struct term **subterm;
};

struct list {
  struct term *head;
  struct list *tail;
};

struct term s_a = {(int)A, 0, NULL};
struct term s_b = {(int)B, 0, NULL};
struct term *a  = &s_a;
struct term *b  = &s_b;

struct term *build_term(int symbol, int arity) {
  struct term *res = malloc(sizeof(struct term));
  res->symbol = symbol;
  res->arity = arity;
  res->subterm = malloc(arity*sizeof(struct term *));
  return res;
}

struct list *build_list(struct term *e, struct list *l) {
  struct list *res = malloc(sizeof(struct list));
  res->head = e;
  res->tail = l;
  return res;
}

int term_equal(struct term *t1, struct term *t2) {
  int i;
  if(t1 == t2) {
    return 1;
  } else if(t1->arity != t2->arity) {
    return 0;
  } else if(t1->symbol != t2->symbol) {
    return 0;
  } else {
    for(i=0 ; i<t1->arity ; i++) {
      if(!term_equal(t1->subterm[i],t2->subterm[i])) {
        return 0;
      }
    }
  }
    return 1;
}

int list_equal(struct list *l1, struct list *l2) {
  if(l1 == l2) {
    return 1;
  } else if(l1==NULL || l2==NULL) {
    return 0;
  } else {
    return term_equal(l1->head,l2->head) && list_equal(l1->tail,l2->tail);
  }
}

struct term *build_f(struct term *x) {
  struct term *res = build_term((int)F,1);
  res->subterm[0] = x;
  return res;
}

struct term *build_msg(struct term *data) {
  struct term *res = build_term((int)MSG,3);
  setDest(res,b);
  setOrig(res,a);
  setData(res,data);
  return(res);
}

%typeterm term {
  implement { struct term* }
  is_sort(t) { (0==0) }
}

%op term a() { 
  is_fsym(t)          { (void*)t->symbol == A }
}
%op term b() { 
  is_fsym(t)          { (void*)t->symbol == B }
}
%op term f(sl:term) { 
  is_fsym(t)          { (void*)t->symbol == F }
  get_slot(sl,t)      { t->subterm[0] }
}
%op term msg(sl1:term,sl2:term,sl3:term) { 
  is_fsym(t)          { (void*)t->symbol == MSG }
  get_slot(sl1,t)     { t->subterm[0] }
  get_slot(sl2,t)     { t->subterm[1] }
  get_slot(sl3,t)     { t->subterm[2] }
}

%typeterm L {
  implement          { struct list* }
  is_sort(t) { (0==0) }
  equals(l1,l2)      { list_equal(l1,l2) }
}

%oplist L cons( term* ) {
  is_fsym(t)         { 1 }
  make_empty()       { NULL }
  make_insert(e,l)   { build_list(e,l) }
  get_head(l)        { l->head }
  get_tail(l)        { (l==NULL)?l:l->tail }
  is_empty(l)        { (l == NULL) }
}

void print_term(struct term *tt) {
  int i;
  static char * symbolnames[] = {"a", "b", "f", "msg"};
  printf("%s", symbolnames[tt->symbol]);
  if (tt->arity!=0) {
    printf("(");
    for(i=0; i<tt->arity; i++) {
      print_term(tt->subterm[i]);
      if (i+1 != tt->arity) printf(",");
    }
    printf(")");
  }
}

struct list *generate_msg(struct list *l, struct term *data) {
  struct list *res;
  struct term *msg;
  msg = build_msg(build_f(data));
  res = build_list(msg,l);
}

#define concat(l1,l2) ((l1==NULL)?l2:(l1->tail=l2))

struct list *read_msg_for_b(struct list *queue, struct term *data) {
  %match(L queue) {
    cons(X1*,msg(b(),_,f(x)) ,X2*) -> {
      if(term_equal(`x,data)) {
        printf("read_msg: "); print_term(data); printf("\n");
        return `concat(X1,X2);
      }
    }
    _ -> { printf("read_msg: msg not found\n"); return queue; }
  }
}


int main() {
  struct list *queue = NULL;

  queue = generate_msg(queue,a);
  queue = generate_msg(queue,b);
  queue = generate_msg(queue,a);

  queue = read_msg_for_b(queue,b);
  queue = read_msg_for_b(queue,a);
  queue = read_msg_for_b(queue,b);
  
}
