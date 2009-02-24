#!/usr/bin/env jython

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SimpleXMLRPCServer import SimpleXMLRPCRequestHandler
from StringIO import StringIO
from tom.engine import Tom
from java.lang import System, String
from java.io import PrintStream, ByteArrayOutputStream
import sys
import os

PORT = 8000

# create server
server = SimpleXMLRPCServer(("localhost",PORT))

# redirect System.out and System.err and call tom
def compile(args):
    print args
    outstream = ByteArrayOutputStream()
    errstream = ByteArrayOutputStream()
    System.setOut(PrintStream(outstream))
    System.setErr(PrintStream(errstream))
    try: Tom.exec(args)
    except: pass
    return str((outstream.toString(),errstream.toString()))

server.register_function(compile)

# run the server's main loop
server.serve_forever()
