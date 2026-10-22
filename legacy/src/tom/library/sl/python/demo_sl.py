#!/usr/bin/env python

from sl import *
from composed import *
from term import *


a = Constructor('a',0)
b = Constructor('b',0)
f = Constructor('f',2)
g = Constructor('g',1)


class Replace(AbstractStrategy):
    def __init__(self,x,y):
        AbstractStrategy.__init__(self)
        self.x = x
        self.y = y
        self.initSubterm()

    def visitLight(self,t):
         if t == self.x:
            return self.y
         else:
            return t
    
    def visit_noarg(self):
        any = self.environment.getSubject()
        if any == self.x:
            self.environment.setSubject(self.y)
        return Environment.SUCCESS

if __name__ == '__main__':
    t = f(a(),g(a()))
    print "before", t
    s = TopDown(Replace(a(),b()))
    t1 = s.visitLight(t)
    print "TopDown = ", s
    print "after visitLight", t1
    t2 = s.visit(t)
    print "after visit", t2


    

