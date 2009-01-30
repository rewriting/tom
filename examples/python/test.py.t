#!/usr/bin/env python

from term import *

a = Constructor('a',0)
b = Constructor('b',0)
f = Constructor('f',2)
g = Constructor('g',1)

%typeterm Term {
    implement { Term }
    is_sort(t) { isinstance(t,Term) }
    equals(t1,t2) { t1 == t2 }
}

%op Term f(x:Term, y:Term) {
    is_fsym(t) { t.name == 'f' }
    get_slot(x,t) { t.children[0]}
    get_slot(y,t) { t.children[1] }
    make(a,b) { f(a,b) }
}

%op Term a() {
    is_fsym(t) { t.name == 'a' }
    make() { a() }
}

%op Term b() {
    is_fsym(t) { t.name == 'b' }
    make() { b() }
}

%op Term g(x:Term) {
    is_fsym(t) { t.name == 'g' }
    get_slot(x,t) { t.children[0]}
    make(a) { g(a) }
}

%typeterm TermList {
    implement { list }
    is_sort(t) { isinstance(t,list) }
    equals(l1,l2) { l1 == l2 }
}

%oplist TermList tlist( Term* ) {
    is_fsym(t)       { isinstance(t,list) }
    make_empty()     { [] }
    make_insert(e,l) { l[:] + [e]  }
    get_head(l)      { l[0]  }  
    get_tail(l)      { l[1:] }
    is_empty(l)      { len(l) == 0 }
}


def toto(t):
  if 1<2: # to test match nested into python blocks
    %match(t) {
      f(x,y) -> { print "x = %s, y = %s" % (`str(x),`str(y))
                  res = `f(b(),y)
                }
    } 
    return res

t = f(a(),g(a()))
print "t = ", t
toto(t)

l = [f(a(),b()), a(), f(b(),b()), g(a())]
print map(str,l)
%match(l) {
  tlist(_*,x,_*) -> { print "iter : " + str(`x) }
  tlist(_*,x@f(_,b()),_*) -> { print "f(_,b()) = " + str(`x) } 
}

