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
    TOM_HOME=`cygpath --windows "$TOM_HOME"`
fi

LOCALCLASSPATH=`echo ./lib/junit.jar | tr ' ' ':'`

ARGS=$*
if [ "${ARGS}" -a -d "${ARGS}" ]; then
  ARGS="-Dexample=$ARGS build"
fi
#CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="-XX:PermSize=128m -XX:MaxPermSize=128m" ant $*
CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="" ant ${ARGS}
