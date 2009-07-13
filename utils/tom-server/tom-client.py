#!/usr/bin/env python

import xmlrpclib
import sys
import os

PORT = 8888

def absolute(s):
  if s.endswith(".t"): return os.path.abspath(s)
  else               : return s

userargs = map(absolute, sys.argv[1:])

s = xmlrpclib.ServerProxy('http://localhost:%d' % PORT)
t = s.compile(os.getcwd(),userargs)
(out,err) = eval(t)

sys.stdout.write(out)
sys.stderr.write(err)
