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
    def __init__(self,collection):
        AbstractStrategy.__init__(self)
        self.initSubterm()
        self.bag = collection

    def visit_noarg(self):
        any = self.getEnvironment().getSubject()
        if any.name == 'a':
            pos = self.getEnvironment().getPosition()
            # simulating set behaviour to maintain python2.3 compatibility
            if pos not in self.bag:
                self.bag.append(pos)
        return Environment.SUCCESS

def test1():
    bag = []
    t = f(a(),a())
    try:
        t = TopDown(Bug(bag)).visit(t)
        assert len(bag) == 2, "there are two a()"
    except VisitFailure:
        raise Exception("it should not fail")
    
    for p in bag:
        sts = p.getSubterm()
        st = sts.visit(t)
        assert st == a(), "st == a with visit"

    for p in bag:
        sts = p.getSubterm()
        try:
            st = sts.visitLight(t)
            assert st == a(), "st == a() with visitLight"
        except VisitFailure:
            raise Exception("getSubterm should not fail here")


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
