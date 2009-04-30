#!/usr/bin/env python

import xmlrpclib
import sys
import os

PORT = 8000

TOM_HOME = os.getenv("TOM_HOME")
CONFIG = TOM_HOME + "/Tom.xml"
INC = TOM_HOME + "/share/tom/java"

def absolute(s):
    if s.endswith(".t"): return os.path.abspath(s)
    else               : return s

userargs = map(absolute, sys.argv[1:])
args = ['-X', CONFIG, '-I', INC, '--noOutput', '--wall']

s = xmlrpclib.ServerProxy('http://localhost:%d' % PORT)
t = s.compile(args + userargs)
(out,err) = eval(t)

sys.stdout.write(out)

def clean(s):
  (b,_,e) = s.partition(':')
  b = b.split('/')[-1]
  return b + ':' + e

for l in err.splitlines():
  sys.stderr.write(clean(l))
  sys.stderr.write('\n')


