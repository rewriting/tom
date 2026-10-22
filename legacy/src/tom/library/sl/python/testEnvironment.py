#!/usr/bin/env python

import sys

from sl import *
from composed import *
from term import *

a = Constructor('a',0)
b = Constructor('b',0)
c = Constructor('c',0)
f = Constructor('f',2)


def testEqual():
    e1 = Environment()
    e2 = Environment()
    assert(e1 == e2);
    subject = f(a(),b())
    e1.setSubject(subject)
    e2.setSubject(subject)
    assert(e1 == e2);
    e1.down(2)
    e2.down(2)
    assert(e1 == e2)
    e1.up()
    e2.up()
    e1.down(1)
    e2.down(1)
    assert(e1 == e2)

def testUpAndDown():
    e1 = Environment()
    e2 = Environment()
    subject = f(a(),b())
    e1.setSubject(subject)
    e2.setSubject(subject)
    e1.down(2)
    e2.down(2)
    #test of down
    assert (e1.getRoot() == f(a(),b())), "1"
    assert (e1.getPosition() == Position([2])), "2"
    assert (e1.getSubject() == b()), "3"
    e1.up()
    e2.up()
    #test of up
    assert (e1.getRoot() == f(a(),b())), "4"
    assert (e1.getPosition() == Position([])), "5"
    assert (e1.getSubject() == f(a(),b())), "6"
    e1.down(1)
    e2.down(1)
    #test of down
    assert (e1.getRoot() == f(a(),b())), "7"
    assert (e1.getPosition() == Position([1])), "8"
    assert (e1.getSubject() == a()), "9"


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
        print "\t" + e
