/*
 * This example illsutrate the use of list-matching
 * on a simple message exchanging bug
 */
#include<stdio.h>
#include<stdlib.h>

#define A    0
#define B    1
#define F    2
#define MSG  3
#define CONS 4

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

struct term s_a = {A, 0, NULL};
struct term s_b = {B, 0, NULL};
struct term *a  = &s_a;
struct term *b  = &s_b;

struct term *build_term(int symbol, int arity) {
  struct term *res = malloc(sizeof(struct term));
  res->symbol = symbol;
  res->arity = arity;
  res->subterm = malloc(arity*sizeof(struct term *));
  return res;
}

struct list *build_list(struct term *t, struct list *n) {
  struct list *res = malloc(sizeof(struct list));
  res->head = t;
  res->tail = n;
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
  struct term *res = build_term(F,1);
  res->subterm[0] = x;
  return res;
}

struct term *build_msg(struct term *data) {
  struct term *res = build_term(MSG,3);
  setDest(res,b);
  setOrig(res,a);
  setData(res,data);
  return(res);
}

%typeterm term {
  implement { struct term* }
  get_fun_sym(t)      { t->symbol }
  cmp_fun_sym(t1,t2)  { t1 == t2 }
  get_subterm(t, n)   { t->subterm[n] }
}

%op term a                   { fsym { A } }
%op term b                   { fsym { B } }
%op term f(term)             { fsym { F } }
%op term msg(term,term,term) { fsym { MSG } }

%typelist L {
  implement          { struct list* }
  get_fun_sym(t)     { CONS }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l2)      { list_equal(l1,l2) }
  get_head(l)        { l->head }
  get_tail(l)        { (l==NULL)?l:l->tail }
  is_empty(l)        { (l == NULL) }
}

%oplist L cons( term* ) {
  fsym          { CONS }
  make_empty()  { NULL }
  make_insert(l,e) { build_list(e,l) }
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
    cons(X1*,msg(b,_,f(x)) ,X2*) -> {
      if(term_equal(x,data)) {
        printf("read_msg: "); print_term(data); printf("\n");
        return concat(X1,X2);
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
