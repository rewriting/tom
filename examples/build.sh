#!/bin/sh

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$TOM_HOME" ] &&
    TOM_HOME=`cygpath --unix "$TOM_HOME"`
fi

LOCALCLASSPATH=`echo lib/junit.jar | tr ' ' ':'` 

CLASSPATH=${LOCALCLASSPATH} ant $*
