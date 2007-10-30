#!/usr/bin/env python

from term import *

node = Constructor('node',2)
pair = Constructor('pair',2)
enode = Constructor('enode',2)
interval = Constructor('interval',2)

%typeterm Interval {
    implement { Term }
    is_sort(t) { isinstance(t,Term) }
    equals(t1,t2) { t1 == t2 }
}

%op Interval interval(x:float, y:float) {
    is_fsym(t) { t.name == 'interval' }
    get_slot(x,t) { t.children[0] }
    get_slot(y,t) { t.children[1] }
    make(x,y) { interval(x,y) }
}

%typeterm Extent {
    implement { list }
    is_sort(t) { isinstance(t,list) }
    equals(l1,l2) { l1 == l2 }
}

%oplist Extent extent( Interval* ) {
    is_fsym(t)       { isinstance(t,list) }
    make_empty()     { [] }
    make_insert(e,l) { [e] + l[:] }
    get_head(l)      { l[0]  }  
    get_tail(l)      { l[1:] }
    is_empty(l)      { len(l) == 0 }
}

%typeterm String {
    implement { string }
    is_sort(s) { isinstance(s,string) }
    equals(s1,s2) { s1 == s2 }
}

%typeterm float {
    implement { float }
    is_sort(d) { isinstance(s,float) }
    equals(d1,d2) { d1 == d2 }
}

%typeterm Tree {
    implement { Term }
    is_sort(t) { isinstance(t,Term) }
    equals(t1,t2) { t1 == t2 }
}

%op Tree node(label:String, tl:TreeList) {
    is_fsym(t) { t.name == 'node' }
    get_slot(label,t) { t.children[0] }
    get_slot(tl,t) { t.children[1] }
    make(l,t) { node(l,t) }
}

%typeterm TreeList {
    implement { list }
    is_sort(t) { isinstance(t,list) }
    equals(l1,l2) { l1 == l2 }
}

%oplist TreeList treelist( Tree* ) {
    is_fsym(t)       { isinstance(t,list) }
    make_empty()     { [] }
    make_insert(e,l) { [e] + l[:] }
    get_head(l)      { l[0]  }  
    get_tail(l)      { l[1:] }
    is_empty(l)      { len(l) == 0 }
}

%typeterm Couple {
    implement { Term }
    is_sort(t) { isinstance(t,Term) }
    equals(t1,t2) { t1 == t2 }
}

%op Couple pair(label:Tree, value:float) {
    is_fsym(t) { t.name == 'pair' }
    get_slot(label,t) { t.children[0] }
    get_slot(value,t) { t.children[1] }
    make(l,v) { pair(l,v) }
}

%typeterm ETree {
    implement { Term }
    is_sort(t) { isinstance(t,Term) }
    equals(t1,t2) { t1 == t2 }
}

%op ETree enode(label:Couple, tl:ETreeList) {
    is_fsym(t) { t.name == 'enode' }
    get_slot(label,t) { t.children[0] }
    get_slot(tl,t) { t.children[1] }
    make(l,t) { enode(l,t) }
}

%typeterm ETreeList {
    implement { list }
    is_sort(t) { isinstance(t,list) }
    equals(l1,l2) { l1 == l2 }
}

%oplist ETreeList etreelist( ETree* ) {
    is_fsym(t)       { isinstance(t,list) }
    make_empty()     { [] }
    make_insert(e,l) { [e] + l[:] }
    get_head(l)      { l[0]  }  
    get_tail(l)      { l[1:] }
    is_empty(l)      { len(l) == 0 }
}

%typeterm ExtentList {
    implement { list }
    is_sort(t) { isinstance(t,list) }
    equals(l1,l2) { l1 == l2 }
}

%oplist ExtentList extentlist( Extent* ) {
    is_fsym(t)       { isinstance(t,list) }
    make_empty()     { [] }
    make_insert(e,l) { [e] + l[:] }
    get_head(l)      { l[0]  }  
    get_tail(l)      { l[1:] }
    is_empty(l)      { len(l) == 0 }
}

%typeterm FloatList {
    implement { list }
    is_sort(t) { isinstance(t,list) }
    equals(l1,l2) { l1 == l2 }
}

%oplist FloatList floatlist( float* ) {
    is_fsym(t)       { isinstance(t,list) }
    make_empty()     { [] }
    make_insert(e,l) { [e] + l[:] }
    get_head(l)      { l[0]  }  
    get_tail(l)      { l[1:] }
    is_empty(l)      { len(l) == 0 }
}


def movetree(t,delta):
  %match(t) {
    enode(pair(label,x), subtrees) -> { return `enode(pair(label,x+delta), subtrees) }
  }

def moveextent(e,x):
  #print "moveextent ", map(str,e), x
  def f(i):
    %match(i) { interval(p,q) -> { return `interval(p+x,q+x) } }
  #print "map(f,e) = " , map(str,map(f,e))
  return map(f,e)

# to enable flattening in the last clause of merge's match
%op Extent merge(e1:Extent, e2:Extent) { make(e1,e2) { merge(e1,e2) } }

def merge(e1, e2):
  #print "merge ", map(str,e1), map(str,e2)
  %match(e1, e2) {
    extent() , qs -> { return `qs; }
    ps, extent()  -> { return `ps; }
    extent(interval(p,_),ps*), extent(interval(_,q),qs*)  -> { return `extent(interval(p,q),merge(ps,qs)) }
  }

def mergelist(es):
  return reduce(merge,es,[])

def fit(e1,e2):
  #print "e1 = %s, e2 = %s" % (map(str,e1),map(str,e2))
  %match(e1,e2) {
    extent(interval(_,p),ps*), extent(interval(q,_),qs*)  -> { return max(`fit(ps,qs),`p-`q+1.0) }
    _                        , _                          -> { return 0.0 }
  }

def fitlistl(es):
  #print "es = ", map(lambda x: map(str,x),es)
  %op FloatList fitlistlaux(l1:Extent, l2:ExtentList) { make(l1,l2) { fitlistlaux(l1,l2) } }
  def fitlistlaux(acc,l):
    %match(ExtentList l) {
      ()      -> { return `extentlist() }
      (e,es*) -> {
        x = `fit(acc,e)
        return `floatlist(x,fitlistlaux(merge(acc,moveextent(e,x)),es))
      }
    }
  return fitlistlaux([],es)
     
def fitlistr(es):
  %op FloatList fitlistraux(l1:Extent, l2:ExtentList) { make(l1,l2) { fitlistraux(l1,l2) } }
  def fitlistraux(acc,l):
    %match(ExtentList l) {
      ()      -> { return `extentlist() }
      (e,es*) -> {
        x = -`fit(e,acc)
        return `floatlist(x,fitlistraux(merge(moveextent(e,x),acc),es))
      }
    }
  esc = es[:]
  esc.reverse()
  res = fitlistraux([],esc)
  res.reverse()
  return res

def fitlist(es):
  return map(lambda (x,y): (x+y)/2, zip(fitlistl(es),fitlistr(es)))

def unzip(l):
  return [x for (x,y) in l], [y for (x,y) in l]

def design(tree):
  def designaux(t):
    %match(t) {
      node(label,subtrees) -> {
        trees, extents = `unzip(map(designaux,subtrees))
        positions = fitlist(extents)
        ptrees = [movetree(t,d) for t,d in zip(trees,positions)]
        pextents = [moveextent(e,p) for e,p in zip(extents,positions)]
        resultextent = [interval(0.0,0.0)] + mergelist(pextents)
        resulttree = `enode(pair(label,0.0), ptrees)
      }
    }
    return (resulttree,resultextent)
  return designaux(tree)[0]

def pp(t):
  %match(t) {
      node(x,l) -> { return '%s(%s)' % (`x, ','.join(map(pp,`l))) }
  }
  %match(t) {
      enode(pair(x,y),l) -> { return '[%s,%f](%s)' % (`x, `y, ','.join(map(pp,`l))) }
  }

def next(counter,symbol):
  try:
    counter[symbol] += 1
  except:
    counter[symbol] = 0
  return symbol + str(counter[symbol])


def tomp(t,parent=None,counter={}):
  %match(t) {
      enode(pair(symbol,pos),subtrees) -> {
        s = `next(counter,symbol)
        print "boxit.%s(btex $%s$ etex);" % (s,`symbol)
        print "drawunboxed(%s);" % s
        if parent:
          print "%s.c = %s.c + (%fu,-u);" % (s,parent,`pos)
          print "draw %s.c -- %s.c cutbefore bpath %s cutafter bpath %s;" % (parent,s,parent,s)
        else:
          print "%s.c = (0.0,0.0);" % s
        for x in `subtrees:
          tomp(x,s,counter)
      }
  }


t = `node('f',[node('g',[node('a',[]),node('b',[])]),node('g',[node('a',[])])])
print pp(t)
print pp(design(t))
tomp(design(t))




