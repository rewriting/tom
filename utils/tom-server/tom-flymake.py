#!/usr/bin/env python

import xmlrpclib
import sys
import os

PORT = 8888

def absolute(s):
  if s.endswith(".t"): return os.path.abspath(s)
  else               : return s

userargs = map(absolute, sys.argv[1:])
args = ['--noOutput', '--wall']

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
