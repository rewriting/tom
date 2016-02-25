#!/usr/bin/env python

import sys

from sl import *
from composed import *
from term import *

a = Constructor('a',0)
b = Constructor('b',0)
c = Constructor('c',0)
f = Constructor('f',2)


class Bug(AbstractStrategy):
    def __init__(self):
        AbstractStrategy.__init__(self)
        self.initSubterm()

    def visitLight(self,t):
        if t.name == 'a':
            return b()
        if t.name == 'b':
            raise VisitFailure()
        return t

    def visit_noarg(self):
        env = self.getEnvironment()
        any = env.getSubject()
        try:
            env.setSubject(self.visitLight(any))
            return Environment.SUCCESS
        except VisitFailure:
            return Environment.FAILURE

def test1():
    bag = []
    t = f(a(),a())
    try:
        t = OnceTopDownId(Bug()).visitLight(t)
        assert t == f(b(),a()), "It should rewrite one a()"
    except VisitFailure:
        raise Exception("it should not fail")
    
    try:
        t = OnceTopDownId(Bug()).visitLight(t)
        raise Exception("it should fail, but got " + str(t))
    except VisitFailure:
        pass

    t2 = f(c(),c())
    t = OnceTopDownId(Bug()).visit(t2)
    assert t == t2, "they should be equal"


if __name__ == '__main__':

    errors = {}

    for test in dir():
        if test.startswith('test'):
            try:
                eval('%s()' % test)
                sys.stdout.write('.')
            except Exception, e:
                if isinstance(e,AssertionError):
                    errors[test] = "Assertion error :" + str(e)
                else:
                    errors[test] = str(e)
                sys.stdout.write('F')

    print "\n"
    if not errors:
        print "All tests ok"
    for t,e in errors.items():
        print "- in " + t
        print "\t" + e.replace('\n','\n\t')
