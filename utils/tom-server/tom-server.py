#!/usr/bin/env jython

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SimpleXMLRPCServer import SimpleXMLRPCRequestHandler
from StringIO import StringIO
from tom.engine import Tom
from java.lang import System, String
from java.io import PrintStream, ByteArrayOutputStream
import sys
import os

PORT = 8888

# create server
server = SimpleXMLRPCServer(("localhost",PORT))
print "serving on port %d" % PORT

# the tom-server script calls this one with -X and -I
config = userargs = sys.argv[1:]

def compile(wd,args):
    print "changing working directory to %s" % wd
    System.setProperty("user.dir",wd);
    print args
    outstream = ByteArrayOutputStream()
    errstream = ByteArrayOutputStream()
    System.setOut(PrintStream(outstream))
    System.setErr(PrintStream(errstream))
    try: Tom.exec(config + args)
    except: pass
    return str((outstream.toString(),errstream.toString()))

server.register_function(compile)

# run the server's main loop
server.serve_forever()
