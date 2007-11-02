#!/usr/bin/env python

import sys
from term import *

node = Constructor('node',2)
pair = Constructor('pair',2)
enode = Constructor('enode',2)
interval = Constructor('interval',2)

%include { draw_tree.tom }

def movetree(t,delta):
  %match(t) {
    enode(pair(label,x), subtrees) -> { return `enode(pair(label,x+delta), subtrees) }
  }

def moveextent(e,x):
  def f(i):
    %match(i) { interval(p,q) -> { return `interval(p+x,q+x) } }
  return map(f,e)

# to enable flattening in the last clause of merge's match
%op Extent merge(e1:Extent, e2:Extent) { make(e1,e2) { merge(e1,e2) } }

def merge(e1, e2):
  %match(e1, e2) {
    extent() , qs -> { return `qs; }
    ps, extent()  -> { return `ps; }
    extent(interval(p,_),ps*), extent(interval(_,q),qs*)  -> { return `extent(interval(p,q),merge(ps,qs)) }
  }

def mergelist(es):
  return reduce(merge,es,[])

def fit(e1,e2):
  %match(e1,e2) {
    extent(interval(_,p),ps*), extent(interval(q,_),qs*)  -> { return max(`fit(ps,qs),`p-`q+1.0) }
    _                        , _                          -> { return 0.0 }
  }

def fitlistl(es):
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
  if symbol in ['x','y','z']:
    return '_' + symbol + str(counter[symbol])
  else:
    return symbol + str(counter[symbol])

def tomp_aux(t,boxes,equations,draws,parent,counter):
  %match(t) {
      enode(pair(symbol,pos),subtrees) -> {
        s = `next(counter,symbol)
        boxes.append("boxit.%s(btex $%s$ etex);" % (s,`symbol))
        draws.append("drawunboxed(%s);" % s)
        if parent:
          equations.append("%s.c = %s.c + (%fu,-u);" % (s,parent,`pos))
          draws.append("draw %s.c -- %s.c cutbefore bpath %s cutafter bpath %s;" % (parent,s,parent,s))
        else:
          equations.append("%s.c = (0.0,0.0);" % s)
        for x in `subtrees:
          tomp_aux(x,boxes,equations,draws,s,counter)
      }
  }

def tomp(t,out=sys.stdout):

  def writeln(s=''):
    out.write(s + '\n')

  boxes = []
  eqs = []
  draws = []
  tomp_aux(t,boxes,eqs,draws,None,{})
  writeln(r"input boxes;")
  writeln()
  writeln(r"verbatimtex %&latex")
  writeln(r"\documentclass{article}")
  writeln(r"\usepackage{amsmath}")
  writeln(r"\usepackage{amsfonts}")
  writeln(r"\usepackage{amssymb}")
  writeln(r"\begin{document}")
  writeln(r"etex")
  writeln()
  writeln("u:= 1cm;")
  writeln()
  writeln(r"beginfig(0);")
  writeln()

  for l in boxes,eqs,draws:
    for i in l:
      writeln(i)
    writeln()

  writeln()
  writeln(r"endfig;")
  writeln(r"end")


LPAR = 0
RPAR = 1
COMMA = 2
STRING = 3

def TermLexer(s):
    i = 0
    while i<len(s):
        if s[i] == ',':
            i += 1
            yield (COMMA,'')
        if s[i] == '(':
            i += 1
            yield (LPAR,'')
        if s[i] == ')':
            i += 1
            yield (RPAR,'')
        else:
            res = ''
            while s[i].isalpha():
                res += s[i]
                i += 1
            yield (STRING,res)

class TermParser:
    def parse(self,s):
        self.tokens = [t for t in TermLexer(s)]
        self.i = 0
        res = self.parse_fun()
        if self.i != len(self.tokens):
            raise "parse error"
        else:
            return res

    def parse_fun(self):
        t,name = self.tokens[self.i]
        if not t == STRING: raise "expected string"
        self.i += 1

        t,_ = self.tokens[self.i]
        if t == LPAR:
            self.i += 1
            children = self.parse_list()
            children.reverse()
            t,_ = self.tokens[self.i]
            if not t == RPAR : raise "expected right parenthesis"
            self.i += 1
            return `node(name,children)

        elif t == COMMA or t == RPAR:
            return `node(name,[])

        else:
            raise "expected left or right parenthesis or comma"

    def parse_list(self):
        t,_ = self.tokens[self.i]
        if t == STRING:
            f = self.parse_fun()
            t,_ = self.tokens[self.i]
            if t == COMMA:
                self.i += 1
                tail = self.parse_list()
                tail.append(f)
                return tail
            else:
                return [f]
        else:
            return []


if __name__ == '__main__':
  p = TermParser()
  t = p.parse(sys.argv[1])
  tomp(design(t))




