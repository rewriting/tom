#!/usr/bin/env python

import sys

from sl import *
from composed import *
from term import *

a = Constructor('a',0)
b = Constructor('b',0)
c = Constructor('c',0)
f = Constructor('f',1)
g = Constructor('g',2)
h = Constructor('b',3)

class R1(AbstractStrategy):
    def __init__(self):
        AbstractStrategy.__init__(self)
        self.initSubterm()

    def visitLight(self,t):
         if t.name == 'f':
             if t.getChildAt(0).name == 'a':
                 return f(b())
         if t.name == 'b':
             return c()
         return t
    
    def visit_noarg(self):
        any = self.environment.getSubject()
        self.environment.setSubject(self.visitLight(any))
        return Environment.SUCCESS

class R2(AbstractStrategy):
    def __init__(self):
        AbstractStrategy.__init__(self)
        self.initSubterm()

    def visitLight(self,t):
         if t.name == 'f':
             if t.getChildAt(0).name == 'a':
                 return f(b())
         if t.name == 'b':
             return c()
         raise VisitFailure()
    
    def visit_noarg(self):
        any = self.environment.getSubject()
        try:
            self.environment.setSubject(self.visitLight(any))
            return Environment.SUCCESS
        except VisitFailure:
            return Environment.FAILURE


class R3(AbstractStrategy):
    def __init__(self):
        AbstractStrategy.__init__(self)
        self.initSubterm()

    def visitLight(self,t):
         if t.name == 'f':
             if t.getChildAt(0).name == 'b':
                 return f(c())
         if t.name == 'c':
             return a()
         raise VisitFailure()
    
    def visit_noarg(self):
        any = self.environment.getSubject()
        try:
            self.environment.setSubject(self.visitLight(any))
            return Environment.SUCCESS
        except VisitFailure:
            return Environment.FAILURE

class R4(AbstractStrategy):
    def __init__(self):
        AbstractStrategy.__init__(self)
        self.initSubterm()

    def visitLight(self,t):
         if t.name == 'f':
             if t.getChildAt(0).name == 'b':
                 return f(c())
         if t.name == 'b':
             return a()
         return t
    
    def visit_noarg(self):
        any = self.environment.getSubject()
        self.environment.setSubject(self.visitLight(any))
        return Environment.SUCCESS


def testIdentity():
    subject = f(a())
    s = Identity()
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(subject == resJ)
    except VisitFailure:
      raise Exception("Identity.visitLight should not fail")
    
    try:
      resS = s.visit(subject)
      assert(subject == resS)
    except VisitFailure:
      raise Exception("Identity.visit should not fail")
    
    assert(resJ == resS)

def testR1():
    subject = f(a())
    s = R1()
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(b()))
    except VisitFailure:
      raise Exception("R1.visitLight should not fail")
    
    try:
      resS = s.visit(subject)
      assert(resS == f(b()))
    except VisitFailure:
      raise Exception("R1.visit should not fail")
    
    assert(resJ == resS)


def testR1Fail():
    subject = f(b())
    s = R1()
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == subject)
    except VisitFailure:
      raise Exception("R1.visitLight should not throw failure on f(b())")
    
    try:
      resS = s.visit(subject)
      assert(resS == subject)
    except VisitFailure:
      raise Exception("R1.visit should not throw failure on f(b())")
    
    assert(resJ == resS)

def testR2():
    subject = f(a())
    s = R2()
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(b()))
    except VisitFailure:
      raise Exception("R1.visitLight should not fail on f(a())")
    
    try:
      resS = s.visit(subject)
      assert(resS == f(b()))
    except VisitFailure:
      raise Exception("R1.visit should not fail on f(a())")
    
    assert(resJ == resS)

def testR2Fail():
    subject = f(b())
    s = R2()
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        raise Exception("R1.visitLight should fail on f(b())")
    except VisitFailure:
        assert(resJ == None)

    try:
      resS = s.visit(subject)
      raise Exception("R1.visit should fail on f(b())")
    except VisitFailure:
        assert(resS == None)
    
    assert(resJ == None)
    assert(resS == None)


def testOne():
    subject = f(b())
    s = One(R1())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(c()))
    except VisitFailure:
      raise Exception("One(R1()).visitLight should not fail on f(b())")
    
    try:
      resS = s.visit(subject)
      assert(resS == f(c()))
    except VisitFailure:
      raise Exception("One(R1()).visit should not fail on f(b())")
    
    assert(resJ == resS)

def testOneFail():
    subject = f(a())
    s = One(R2())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        raise Exception("One(R2).visitLight should fail on f(a())")
    except VisitFailure:
        assert(resJ == None)

    try:
      resS = s.visit(subject)
      raise Exception("One(R2).visit should fail on f(a())")
    except VisitFailure:
        assert(resS == None)
    
    assert(resJ == None)
    assert(resS == None)

def testOnceBottomUp():
    subject = g(f(a()),b())
    s = OnceBottomUp(R2())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == g(f(b()),b()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == g(f(b()),b()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

    #second application
    subject = resJ
    try:
        resJ = s.visitLight(subject)
        assert(resJ == g(f(c()),b()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == g(f(c()),b()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))

    assert(resJ == resS)

    # third application
    subject = resJ
    try:
        resJ = s.visitLight(subject)
        assert(resJ == g(f(c()),c()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == g(f(c()),c()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))

    assert(resJ == resS)

    #fourth application : it fails
    subject = resJ
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)
    
def testAll():
    subject = g(f(a()),b())
    s = All(R2())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == g(f(b()),c()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == g(f(b()),c()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

    # second application should fail
    subject = resJ
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)
    
def testAllFail():
    subject = g(f(b()),b())
    s = All(R2())
    resJ = None
    resS = None
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)
    
def testOmega0():
    subject = f(a())
    s = Omega(0,R2())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(b()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == f(b()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testOmega1():
    subject = g(f(a()),b())
    s = Omega(1,R2())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == g(f(b()),b()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == g(f(b()),b()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testOmega2():
    subject = g(f(a()),b())
    s = Omega(2,R2())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == g(f(a()),c()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == g(f(a()),c()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testOmegaFail():
    subject = g(f(b()),b())
    s = Omega(1,R2())
    resJ = None
    resS = None
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)

def testOmegaAritOverflow():
    subject = g(f(b()),b())
    s = Omega(3,R2())
    resJ = None
    resS = None
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)


class Make_a(AbstractStrategy):
    def __init__(self):
        AbstractStrategy.__init__(self)
        self.initSubterm()

    def visitLight(self,_):
        return a()

    def visit_noarg(self):
        self.getEnvironment().setSubject(a())
        return Environment.SUCCESS
        
class Make_b(AbstractStrategy):
    def __init__(self):
        AbstractStrategy.__init__(self)
        self.initSubterm()

    def visitLight(self,_):
        return b()

    def visit_noarg(self):
        self.getEnvironment().setSubject(b())
        return Environment.SUCCESS

def testIfThenElse1():
    subject = f(a())
    s = IfThenElse(R2(),Make_a(),Make_b())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == a())
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == a())
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testIfThenElse2():
    subject = f(b())
    s = IfThenElse(R2(),Make_a(),Make_b())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == b())
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == b())
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testSequence():
    subject = f(a())
    s = Sequence(R2(),R3())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(c()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == f(c()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testSequenceFail1():
    subject = f(c())
    s = Sequence(R2(),Identity())
    resJ = None
    resS = None
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)

def testSequenceFail2():
    subject = f(c())
    s = Sequence(Identity(),R3())
    resJ = None
    resS = None
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)

def testNotR2():
    subject = f(b())
    s = Not(R2())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(b()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == f(b()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testNotR2Fail():
    subject = f(a())
    s = Not(R2())
    resJ = None
    resS = None
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)

def testSequenceId():
    subject = f(a())
    s = SequenceId(R1(),R4())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(c()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == f(c()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testSequenceIdFail1():
    subject = f(c())
    s = SequenceId(R1(),Identity())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == subject)
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == subject)
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testChoice1():
    subject = f(a())
    s = Choice(R2(),R3())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(b()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == f(b()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testChoice2():
    subject = f(a())
    s = Choice(R3(),R2())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(b()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == f(b()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testChoiceFail():
    subject = f(a())
    s = Choice(R3(),R3())
    resJ = None
    resS = None
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)

def testChoiceId():
    subject = f(a())
    s = ChoiceId(R2(),R3())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(b()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == f(b()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testChoiceId2():
    subject = f(a())
    s = ChoiceId(Identity(),R2())
    resJ = None
    resS = None
    try:
        resJ = s.visitLight(subject)
        assert(resJ == f(b()))
    except VisitFailure:
      raise Exception("%s.visitLight should not fail on %s" % (str(s),str(subject)))
    
    try:
      resS = s.visit(subject)
      assert(resS == f(b()))
    except VisitFailure:
      raise Exception("%s.visit should not fail on %s" % (str(s),str(subject)))
    
    assert(resJ == resS)

def testChoiceIdFail1():
    subject = f(b())
    s = Choice(R2(),R2())
    resJ = None
    resS = None
    try:
        resJ =  s.visitLight(subject)
        raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
        resJ = None
    try:
       resS = s.visit(subject)
       raise Exception("%s.visit should fail on %s" % (str(s),str(subject)))
    except VisitFailure:
       resS = None
     
    assert(resS == None);
    assert(resJ == None)


if __name__ == '__main__':

    errors = {}

    for test in dir():
        if test.startswith('test'):
            try:
                eval('%s()' % test)
                sys.stdout.write('.')
            except Exception, e:
                if isinstance(e,AssertionError):
                    errors[test] = "Assertion error: " + str(e)
                else:
                    errors[test] = str(e)
                sys.stdout.write('F')

    print "\n"
    if not errors:
        print "All tests ok"
    for t,e in errors.items():
        print "- in " + t
        print "\t" + e
